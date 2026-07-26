package com.android.server.companion.transport;

/* JADX INFO: loaded from: classes.dex */
class SecureTransport extends com.android.server.companion.transport.Transport implements com.android.server.companion.securechannel.SecureChannel.Callback {
    private final java.util.concurrent.BlockingQueue<byte[]> mRequestQueue;
    private final com.android.server.companion.securechannel.SecureChannel mSecureChannel;
    private volatile boolean mShouldProcessRequests;

    SecureTransport(int associationId, android.os.ParcelFileDescriptor fd, android.content.Context context) {
        super(associationId, fd, context);
        this.mShouldProcessRequests = false;
        this.mRequestQueue = new java.util.concurrent.ArrayBlockingQueue(500);
        this.mSecureChannel = new com.android.server.companion.securechannel.SecureChannel(this.mRemoteIn, this.mRemoteOut, this, context);
    }

    SecureTransport(int associationId, android.os.ParcelFileDescriptor fd, android.content.Context context, byte[] preSharedKey, com.android.server.companion.securechannel.AttestationVerifier verifier) {
        super(associationId, fd, context);
        this.mShouldProcessRequests = false;
        this.mRequestQueue = new java.util.concurrent.ArrayBlockingQueue(500);
        this.mSecureChannel = new com.android.server.companion.securechannel.SecureChannel(this.mRemoteIn, this.mRemoteOut, this, preSharedKey, verifier);
    }

    @Override // com.android.server.companion.transport.Transport
    void start() {
        this.mSecureChannel.start();
    }

    @Override // com.android.server.companion.transport.Transport
    void stop() {
        this.mSecureChannel.stop();
        this.mShouldProcessRequests = false;
    }

    @Override // com.android.server.companion.transport.Transport
    void close() {
        this.mSecureChannel.close();
        this.mShouldProcessRequests = false;
        super.close();
    }

    @Override // com.android.server.companion.transport.Transport
    protected void sendMessage(int message, int sequence, byte[] data) throws java.io.IOException {
        if (!this.mShouldProcessRequests) {
            establishSecureConnection();
        }
        if (DEBUG) {
            android.util.Slog.d("CDM_CompanionTransport", "Queueing message 0x" + java.lang.Integer.toHexString(message) + " sequence " + sequence + " length " + data.length + " to association " + this.mAssociationId);
        }
        try {
            this.mRequestQueue.add(java.nio.ByteBuffer.allocate(data.length + 12).putInt(message).putInt(sequence).putInt(data.length).put(data).array());
        } catch (java.lang.IllegalStateException e) {
            android.util.Slog.w("CDM_CompanionTransport", "Failed to queue message 0x" + java.lang.Integer.toHexString(message) + " . Request buffer is full; detaching transport.", e);
            close();
        }
    }

    private void establishSecureConnection() {
        android.util.Slog.d("CDM_CompanionTransport", "Establishing secure connection.");
        try {
            this.mSecureChannel.establishSecureConnection();
        } catch (java.lang.Exception e) {
            android.util.Slog.e("CDM_CompanionTransport", "Failed to initiate secure channel handshake.", e);
            close();
        }
    }

    @Override // com.android.server.companion.securechannel.SecureChannel.Callback
    public void onSecureConnection() {
        this.mShouldProcessRequests = true;
        android.util.Slog.d("CDM_CompanionTransport", "Secure connection established.");
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.companion.transport.SecureTransport$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSecureConnection$0();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSecureConnection$0() {
        while (this.mShouldProcessRequests) {
            try {
                byte[] request = this.mRequestQueue.take();
                this.mSecureChannel.sendSecureMessage(request);
            } catch (java.lang.Exception e) {
                android.util.Slog.e("CDM_CompanionTransport", "Failed to send secure message.", e);
                close();
            }
        }
    }

    @Override // com.android.server.companion.securechannel.SecureChannel.Callback
    public void onSecureMessageReceived(byte[] data) {
        java.nio.ByteBuffer payload = java.nio.ByteBuffer.wrap(data);
        int message = payload.getInt();
        int sequence = payload.getInt();
        int length = payload.getInt();
        byte[] content = new byte[length];
        payload.get(content);
        try {
            handleMessage(message, sequence, content);
        } catch (java.io.IOException e) {
        }
    }

    @Override // com.android.server.companion.securechannel.SecureChannel.Callback
    public void onError(java.lang.Throwable error) {
        android.util.Slog.e("CDM_CompanionTransport", "Secure transport encountered an error.", error);
        if (this.mSecureChannel.isStopped()) {
            close();
        }
    }

    public java.lang.String toString() {
        return "SecureTransport{mAssociationId=" + this.mAssociationId + ", mSecureChannel=" + this.mSecureChannel + '}';
    }
}
