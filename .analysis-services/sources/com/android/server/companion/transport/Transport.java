package com.android.server.companion.transport;

/* JADX INFO: loaded from: classes.dex */
public abstract class Transport {
    protected static final boolean DEBUG = android.os.Build.IS_DEBUGGABLE;
    protected static final int HEADER_LENGTH = 12;
    static final int MESSAGE_RESPONSE_FAILURE = 863004019;
    static final int MESSAGE_RESPONSE_SUCCESS = 864257383;
    protected static final java.lang.String TAG = "CDM_CompanionTransport";
    protected final int mAssociationId;
    protected final android.content.Context mContext;
    protected final android.os.ParcelFileDescriptor mFd;
    private com.android.server.companion.transport.Transport.OnTransportClosedListener mOnTransportClosed;
    protected final java.io.InputStream mRemoteIn;
    protected final java.io.OutputStream mRemoteOut;
    protected final android.util.SparseArray<java.util.concurrent.CompletableFuture<byte[]>> mPendingRequests = new android.util.SparseArray<>();
    protected final java.util.concurrent.atomic.AtomicInteger mNextSequence = new java.util.concurrent.atomic.AtomicInteger();
    private final java.util.Map<java.lang.Integer, android.companion.IOnMessageReceivedListener> mListeners = new java.util.HashMap();

    @java.lang.FunctionalInterface
    interface OnTransportClosedListener {
        void onClosed(com.android.server.companion.transport.Transport transport);
    }

    protected abstract void sendMessage(int i, int i2, byte[] bArr) throws java.io.IOException;

    abstract void start();

    abstract void stop();

    private static boolean isRequest(int message) {
        return ((-16777216) & message) == 1660944384;
    }

    private static boolean isResponse(int message) {
        return ((-16777216) & message) == 855638016;
    }

    private static boolean isOneway(int message) {
        return ((-16777216) & message) == 1124073472;
    }

    Transport(int associationId, android.os.ParcelFileDescriptor fd, android.content.Context context) {
        this.mAssociationId = associationId;
        this.mFd = fd;
        this.mRemoteIn = new android.os.ParcelFileDescriptor.AutoCloseInputStream(fd);
        this.mRemoteOut = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(fd);
        this.mContext = context;
    }

    public void addListener(int message, android.companion.IOnMessageReceivedListener listener) {
        this.mListeners.put(java.lang.Integer.valueOf(message), listener);
    }

    public int getAssociationId() {
        return this.mAssociationId;
    }

    protected android.os.ParcelFileDescriptor getFd() {
        return this.mFd;
    }

    void close() {
        if (this.mOnTransportClosed != null) {
            this.mOnTransportClosed.onClosed(this);
        }
    }

    public java.util.concurrent.Future<byte[]> sendMessage(int message, byte[] data) {
        java.util.concurrent.CompletableFuture<byte[]> pending = new java.util.concurrent.CompletableFuture<>();
        if (isOneway(message)) {
            return sendAndForget(message, data);
        }
        if (isRequest(message)) {
            return requestForResponse(message, data);
        }
        android.util.Slog.w(TAG, "Failed to send message 0x" + java.lang.Integer.toHexString(message));
        pending.completeExceptionally(new java.lang.IllegalArgumentException("The message being sent must be either a one-way or a request."));
        return pending;
    }

    @java.lang.Deprecated
    public java.util.concurrent.Future<byte[]> requestForResponse(int message, byte[] data) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Requesting for response");
        }
        int sequence = this.mNextSequence.incrementAndGet();
        java.util.concurrent.CompletableFuture<byte[]> pending = new java.util.concurrent.CompletableFuture<>();
        synchronized (this.mPendingRequests) {
            this.mPendingRequests.put(sequence, pending);
        }
        try {
            sendMessage(message, sequence, data);
        } catch (java.io.IOException e) {
            synchronized (this.mPendingRequests) {
                this.mPendingRequests.remove(sequence);
                pending.completeExceptionally(e);
            }
        }
        return pending;
    }

    private java.util.concurrent.Future<byte[]> sendAndForget(int message, byte[] data) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Sending a one-way message");
        }
        java.util.concurrent.CompletableFuture<byte[]> pending = new java.util.concurrent.CompletableFuture<>();
        try {
            sendMessage(message, -1, data);
            pending.complete(null);
        } catch (java.io.IOException e) {
            pending.completeExceptionally(e);
        }
        return pending;
    }

    protected final void handleMessage(int message, int sequence, byte[] data) throws java.io.IOException {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Received message 0x" + java.lang.Integer.toHexString(message) + " sequence " + sequence + " length " + data.length + " from association " + this.mAssociationId);
        }
        if (isOneway(message)) {
            processOneway(message, data);
            return;
        }
        if (isRequest(message)) {
            try {
                processRequest(message, sequence, data);
                return;
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Failed to respond to 0x" + java.lang.Integer.toHexString(message), e);
                return;
            }
        }
        if (isResponse(message)) {
            processResponse(message, sequence, data);
        } else {
            android.util.Slog.w(TAG, "Unknown message 0x" + java.lang.Integer.toHexString(message));
        }
    }

    private void processOneway(int message, byte[] data) {
        switch (message) {
            case 1131446919:
            case 1132491640:
            case 1132755335:
                callback(message, data);
                break;
            default:
                android.util.Slog.w(TAG, "Ignoring unknown message 0x" + java.lang.Integer.toHexString(message));
                break;
        }
    }

    private void processRequest(int message, int sequence, byte[] data) throws java.io.IOException {
        switch (message) {
            case 1667729539:
            case 1669494629:
                callback(message, data);
                sendMessage(MESSAGE_RESPONSE_SUCCESS, sequence, libcore.util.EmptyArray.BYTE);
                break;
            case 1669362552:
                sendMessage(MESSAGE_RESPONSE_SUCCESS, sequence, data);
                break;
            case 1669491075:
                try {
                    callback(message, data);
                    sendMessage(MESSAGE_RESPONSE_SUCCESS, sequence, libcore.util.EmptyArray.BYTE);
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(TAG, "Failed to restore permissions");
                    sendMessage(MESSAGE_RESPONSE_FAILURE, sequence, libcore.util.EmptyArray.BYTE);
                    return;
                }
                break;
            default:
                android.util.Slog.w(TAG, "Unknown request 0x" + java.lang.Integer.toHexString(message));
                sendMessage(MESSAGE_RESPONSE_FAILURE, sequence, libcore.util.EmptyArray.BYTE);
                break;
        }
    }

    private void callback(int message, byte[] data) {
        if (this.mListeners.containsKey(java.lang.Integer.valueOf(message))) {
            try {
                this.mListeners.get(java.lang.Integer.valueOf(message)).onMessageReceived(getAssociationId(), data);
                android.util.Slog.d(TAG, "Message 0x" + java.lang.Integer.toHexString(message) + " is received from associationId " + this.mAssociationId + ", sending data length " + data.length + " to the listener.");
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void processResponse(int message, int sequence, byte[] data) {
        java.util.concurrent.CompletableFuture<byte[]> future;
        synchronized (this.mPendingRequests) {
            future = (java.util.concurrent.CompletableFuture) this.mPendingRequests.removeReturnOld(sequence);
        }
        if (future == null) {
            android.util.Slog.w(TAG, "Ignoring unknown sequence " + sequence);
            return;
        }
        switch (message) {
            case MESSAGE_RESPONSE_FAILURE /* 863004019 */:
                future.completeExceptionally(new java.lang.RuntimeException("Remote failure"));
                return;
            case MESSAGE_RESPONSE_SUCCESS /* 864257383 */:
                future.complete(data);
                return;
            default:
                android.util.Slog.w(TAG, "Ignoring unknown response 0x" + java.lang.Integer.toHexString(message));
                return;
        }
    }

    void setOnTransportClosedListener(com.android.server.companion.transport.Transport.OnTransportClosedListener callback) {
        this.mOnTransportClosed = callback;
    }
}
