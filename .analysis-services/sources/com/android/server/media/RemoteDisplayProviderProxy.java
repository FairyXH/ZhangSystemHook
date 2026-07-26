package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
final class RemoteDisplayProviderProxy implements android.content.ServiceConnection {
    private com.android.server.media.RemoteDisplayProviderProxy.Connection mActiveConnection;
    private boolean mBound;
    private final android.content.ComponentName mComponentName;
    private boolean mConnectionReady;
    private final android.content.Context mContext;
    private int mDiscoveryMode;
    private android.media.RemoteDisplayState mDisplayState;
    private com.android.server.media.RemoteDisplayProviderProxy.Callback mDisplayStateCallback;
    private final java.lang.Runnable mDisplayStateChanged = new java.lang.Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.1
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.media.RemoteDisplayProviderProxy.this.mScheduledDisplayStateChangedCallback = false;
            if (com.android.server.media.RemoteDisplayProviderProxy.this.mDisplayStateCallback != null) {
                com.android.server.media.RemoteDisplayProviderProxy.this.mDisplayStateCallback.onDisplayStateChanged(com.android.server.media.RemoteDisplayProviderProxy.this, com.android.server.media.RemoteDisplayProviderProxy.this.mDisplayState);
            }
        }
    };
    private final android.os.Handler mHandler = new android.os.Handler();
    private boolean mRunning;
    private boolean mScheduledDisplayStateChangedCallback;
    private java.lang.String mSelectedDisplayId;
    private final int mUserId;
    private static final java.lang.String TAG = "RemoteDisplayProvider";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    public interface Callback {
        void onDisplayStateChanged(com.android.server.media.RemoteDisplayProviderProxy remoteDisplayProviderProxy, android.media.RemoteDisplayState remoteDisplayState);
    }

    public RemoteDisplayProviderProxy(android.content.Context context, android.content.ComponentName componentName, int userId) {
        this.mContext = context;
        this.mComponentName = componentName;
        this.mUserId = userId;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "Proxy");
        pw.println(prefix + "  mUserId=" + this.mUserId);
        pw.println(prefix + "  mRunning=" + this.mRunning);
        pw.println(prefix + "  mBound=" + this.mBound);
        pw.println(prefix + "  mActiveConnection=" + this.mActiveConnection);
        pw.println(prefix + "  mConnectionReady=" + this.mConnectionReady);
        pw.println(prefix + "  mDiscoveryMode=" + this.mDiscoveryMode);
        pw.println(prefix + "  mSelectedDisplayId=" + this.mSelectedDisplayId);
        pw.println(prefix + "  mDisplayState=" + this.mDisplayState);
    }

    public void setCallback(com.android.server.media.RemoteDisplayProviderProxy.Callback callback) {
        this.mDisplayStateCallback = callback;
    }

    public android.media.RemoteDisplayState getDisplayState() {
        return this.mDisplayState;
    }

    public void setDiscoveryMode(int mode) {
        if (this.mDiscoveryMode != mode) {
            this.mDiscoveryMode = mode;
            if (this.mConnectionReady) {
                this.mActiveConnection.setDiscoveryMode(mode);
            }
            updateBinding();
        }
    }

    public void setSelectedDisplay(java.lang.String id) {
        if (!java.util.Objects.equals(this.mSelectedDisplayId, id)) {
            if (this.mConnectionReady && this.mSelectedDisplayId != null) {
                this.mActiveConnection.disconnect(this.mSelectedDisplayId);
            }
            this.mSelectedDisplayId = id;
            if (this.mConnectionReady && id != null) {
                this.mActiveConnection.connect(id);
            }
            updateBinding();
        }
    }

    public void setDisplayVolume(int volume) {
        if (this.mConnectionReady && this.mSelectedDisplayId != null) {
            this.mActiveConnection.setVolume(this.mSelectedDisplayId, volume);
        }
    }

    public void adjustDisplayVolume(int delta) {
        if (this.mConnectionReady && this.mSelectedDisplayId != null) {
            this.mActiveConnection.adjustVolume(this.mSelectedDisplayId, delta);
        }
    }

    public boolean hasComponentName(java.lang.String packageName, java.lang.String className) {
        return this.mComponentName.getPackageName().equals(packageName) && this.mComponentName.getClassName().equals(className);
    }

    public java.lang.String getFlattenedComponentName() {
        return this.mComponentName.flattenToShortString();
    }

    public void start() {
        if (!this.mRunning) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Starting");
            }
            this.mRunning = true;
            updateBinding();
        }
    }

    public void stop() {
        if (this.mRunning) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Stopping");
            }
            this.mRunning = false;
            updateBinding();
        }
    }

    public void rebindIfDisconnected() {
        if (this.mActiveConnection == null && shouldBind()) {
            unbind();
            bind();
        }
    }

    private void updateBinding() {
        if (shouldBind()) {
            bind();
        } else {
            unbind();
        }
    }

    private boolean shouldBind() {
        if (this.mRunning) {
            if (this.mDiscoveryMode != 0 || this.mSelectedDisplayId != null) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void bind() {
        if (!this.mBound) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Binding");
            }
            android.content.Intent service = new android.content.Intent("com.android.media.remotedisplay.RemoteDisplayProvider");
            service.setComponent(this.mComponentName);
            try {
                this.mBound = this.mContext.bindServiceAsUser(service, this, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, new android.os.UserHandle(this.mUserId));
                if (!this.mBound && DEBUG) {
                    android.util.Slog.d(TAG, this + ": Bind failed");
                }
            } catch (java.lang.SecurityException ex) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, this + ": Bind failed", ex);
                }
            }
        }
    }

    private void unbind() {
        if (this.mBound) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Unbinding");
            }
            this.mBound = false;
            disconnect();
            this.mContext.unbindService(this);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
        if (DEBUG) {
            android.util.Slog.d(TAG, this + ": Connected");
        }
        if (this.mBound) {
            disconnect();
            android.media.IRemoteDisplayProvider provider = android.media.IRemoteDisplayProvider.Stub.asInterface(service);
            if (provider != null) {
                com.android.server.media.RemoteDisplayProviderProxy.Connection connection = new com.android.server.media.RemoteDisplayProviderProxy.Connection(provider);
                if (connection.register()) {
                    this.mActiveConnection = connection;
                    return;
                } else {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, this + ": Registration failed");
                        return;
                    }
                    return;
                }
            }
            android.util.Slog.e(TAG, this + ": Service returned invalid remote display provider binder");
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(android.content.ComponentName name) {
        if (DEBUG) {
            android.util.Slog.d(TAG, this + ": Service disconnected");
        }
        disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectionReady(com.android.server.media.RemoteDisplayProviderProxy.Connection connection) {
        if (this.mActiveConnection == connection) {
            this.mConnectionReady = true;
            if (this.mDiscoveryMode != 0) {
                this.mActiveConnection.setDiscoveryMode(this.mDiscoveryMode);
            }
            if (this.mSelectedDisplayId != null) {
                this.mActiveConnection.connect(this.mSelectedDisplayId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectionDied(com.android.server.media.RemoteDisplayProviderProxy.Connection connection) {
        if (this.mActiveConnection == connection) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Service connection died");
            }
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDisplayStateChanged(com.android.server.media.RemoteDisplayProviderProxy.Connection connection, android.media.RemoteDisplayState state) {
        if (this.mActiveConnection == connection) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": State changed, state=" + state);
            }
            setDisplayState(state);
        }
    }

    private synchronized void disconnect() {
        if (this.mActiveConnection != null) {
            if (this.mSelectedDisplayId != null) {
                this.mActiveConnection.disconnect(this.mSelectedDisplayId);
            }
            this.mConnectionReady = false;
            this.mActiveConnection.dispose();
            this.mActiveConnection = null;
            setDisplayState(null);
        }
    }

    private void setDisplayState(android.media.RemoteDisplayState state) {
        if (!java.util.Objects.equals(this.mDisplayState, state)) {
            this.mDisplayState = state;
            if (!this.mScheduledDisplayStateChangedCallback) {
                this.mScheduledDisplayStateChangedCallback = true;
                this.mHandler.post(this.mDisplayStateChanged);
            }
        }
    }

    public java.lang.String toString() {
        return "Service connection " + this.mComponentName.flattenToShortString();
    }

    private final class Connection implements android.os.IBinder.DeathRecipient {
        private final com.android.server.media.RemoteDisplayProviderProxy.ProviderCallback mCallback = new com.android.server.media.RemoteDisplayProviderProxy.ProviderCallback(this);
        private final android.media.IRemoteDisplayProvider mProvider;

        public Connection(android.media.IRemoteDisplayProvider provider) {
            this.mProvider = provider;
        }

        public boolean register() {
            try {
                this.mProvider.asBinder().linkToDeath(this, 0);
                this.mProvider.setCallback(this.mCallback);
                com.android.server.media.RemoteDisplayProviderProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.Connection.1
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.media.RemoteDisplayProviderProxy.this.onConnectionReady(com.android.server.media.RemoteDisplayProviderProxy.Connection.this);
                    }
                });
                return true;
            } catch (android.os.RemoteException e) {
                binderDied();
                return false;
            }
        }

        public void dispose() {
            try {
                this.mProvider.asBinder().unlinkToDeath(this, 0);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
            this.mCallback.dispose();
        }

        public void setDiscoveryMode(int mode) {
            try {
                this.mProvider.setDiscoveryMode(mode);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.RemoteDisplayProviderProxy.TAG, "Failed to deliver request to set discovery mode.", ex);
            }
        }

        public void connect(java.lang.String id) {
            try {
                this.mProvider.connect(id);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.RemoteDisplayProviderProxy.TAG, "Failed to deliver request to connect to display.", ex);
            }
        }

        public void disconnect(java.lang.String id) {
            try {
                this.mProvider.disconnect(id);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.RemoteDisplayProviderProxy.TAG, "Failed to deliver request to disconnect from display.", ex);
            }
        }

        public void setVolume(java.lang.String id, int volume) {
            try {
                this.mProvider.setVolume(id, volume);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.RemoteDisplayProviderProxy.TAG, "Failed to deliver request to set display volume.", ex);
            }
        }

        public void adjustVolume(java.lang.String id, int volume) {
            try {
                this.mProvider.adjustVolume(id, volume);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(com.android.server.media.RemoteDisplayProviderProxy.TAG, "Failed to deliver request to adjust display volume.", ex);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.media.RemoteDisplayProviderProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.Connection.2
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.media.RemoteDisplayProviderProxy.this.onConnectionDied(com.android.server.media.RemoteDisplayProviderProxy.Connection.this);
                }
            });
        }

        void postStateChanged(final android.media.RemoteDisplayState state) {
            com.android.server.media.RemoteDisplayProviderProxy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.Connection.3
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.media.RemoteDisplayProviderProxy.this.onDisplayStateChanged(com.android.server.media.RemoteDisplayProviderProxy.Connection.this, state);
                }
            });
        }
    }

    private static final class ProviderCallback extends android.media.IRemoteDisplayCallback.Stub {
        private final java.lang.ref.WeakReference<com.android.server.media.RemoteDisplayProviderProxy.Connection> mConnectionRef;

        public ProviderCallback(com.android.server.media.RemoteDisplayProviderProxy.Connection connection) {
            this.mConnectionRef = new java.lang.ref.WeakReference<>(connection);
        }

        public void dispose() {
            this.mConnectionRef.clear();
        }

        public void onStateChanged(android.media.RemoteDisplayState state) throws android.os.RemoteException {
            com.android.server.media.RemoteDisplayProviderProxy.Connection connection = this.mConnectionRef.get();
            if (connection != null) {
                connection.postStateChanged(state);
            }
        }
    }
}
