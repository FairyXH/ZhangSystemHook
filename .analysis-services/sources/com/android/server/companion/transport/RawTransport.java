package com.android.server.companion.transport;

/* JADX INFO: loaded from: classes.dex */
class RawTransport extends com.android.server.companion.transport.Transport {
    private volatile boolean mStopped;

    RawTransport(int associationId, android.os.ParcelFileDescriptor fd, android.content.Context context) {
        super(associationId, fd, context);
    }

    @Override // com.android.server.companion.transport.Transport
    void start() {
        if (DEBUG) {
            android.util.Slog.d("CDM_CompanionTransport", "Starting raw transport.");
        }
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.companion.transport.RawTransport$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$start$0();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        while (!this.mStopped) {
            try {
                receiveMessage();
            } catch (java.io.IOException e) {
                if (!this.mStopped) {
                    android.util.Slog.w("CDM_CompanionTransport", "Trouble during transport", e);
                    close();
                    return;
                }
                return;
            }
        }
    }

    @Override // com.android.server.companion.transport.Transport
    void stop() {
        if (DEBUG) {
            android.util.Slog.d("CDM_CompanionTransport", "Stopping raw transport.");
        }
        this.mStopped = true;
    }

    @Override // com.android.server.companion.transport.Transport
    void close() {
        stop();
        if (DEBUG) {
            android.util.Slog.d("CDM_CompanionTransport", "Closing raw transport.");
        }
        libcore.io.IoUtils.closeQuietly(this.mRemoteIn);
        libcore.io.IoUtils.closeQuietly(this.mRemoteOut);
        super.close();
    }

    @Override // com.android.server.companion.transport.Transport
    protected void sendMessage(int message, int sequence, byte[] data) throws java.io.IOException {
        if (DEBUG) {
            android.util.Slog.e("CDM_CompanionTransport", "Sending message 0x" + java.lang.Integer.toHexString(message) + " sequence " + sequence + " length " + data.length + " to association " + this.mAssociationId);
        }
        synchronized (this.mRemoteOut) {
            java.nio.ByteBuffer header = java.nio.ByteBuffer.allocate(12).putInt(message).putInt(sequence).putInt(data.length);
            this.mRemoteOut.write(header.array());
            this.mRemoteOut.write(data);
            this.mRemoteOut.flush();
        }
    }

    public java.lang.String toString() {
        return "RawTransport{mAssociationId=" + this.mAssociationId + '}';
    }

    private void receiveMessage() throws java.io.IOException {
        synchronized (this.mRemoteIn) {
            byte[] headerBytes = new byte[12];
            libcore.io.Streams.readFully(this.mRemoteIn, headerBytes);
            java.nio.ByteBuffer header = java.nio.ByteBuffer.wrap(headerBytes);
            int message = header.getInt();
            int sequence = header.getInt();
            int length = header.getInt();
            byte[] data = new byte[length];
            libcore.io.Streams.readFully(this.mRemoteIn, data);
            handleMessage(message, sequence, data);
        }
    }
}
