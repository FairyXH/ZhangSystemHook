package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
final class TvRemoteProviderProxy implements android.content.ServiceConnection {
    protected static final java.lang.String SERVICE_INTERFACE = "com.android.media.tv.remoteprovider.TvRemoteProvider";
    private boolean mBound;
    private final android.content.ComponentName mComponentName;
    private boolean mConnected;
    private final android.content.Context mContext;
    private final java.lang.Object mLock;
    private boolean mRunning;
    private final int mUid;
    private final int mUserId;
    private static final java.lang.String TAG = "TvRemoteProviderProxy";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 2);

    TvRemoteProviderProxy(android.content.Context context, java.lang.Object lock, android.content.ComponentName componentName, int userId, int uid) {
        this.mContext = context;
        this.mLock = lock;
        this.mComponentName = componentName;
        this.mUserId = userId;
        this.mUid = uid;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "Proxy");
        pw.println(prefix + "  mUserId=" + this.mUserId);
        pw.println(prefix + "  mRunning=" + this.mRunning);
        pw.println(prefix + "  mBound=" + this.mBound);
        pw.println(prefix + "  mConnected=" + this.mConnected);
    }

    public boolean hasComponentName(java.lang.String packageName, java.lang.String className) {
        return this.mComponentName.getPackageName().equals(packageName) && this.mComponentName.getClassName().equals(className);
    }

    public void start() {
        if (!this.mRunning) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Starting");
            }
            this.mRunning = true;
            bind();
        }
    }

    public void stop() {
        if (this.mRunning) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Stopping");
            }
            this.mRunning = false;
            unbind();
        }
    }

    public void rebindIfDisconnected() {
        if (this.mRunning && !this.mConnected) {
            unbind();
            bind();
        }
    }

    private void bind() {
        if (!this.mBound) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this + ": Binding");
            }
            android.content.Intent service = new android.content.Intent(SERVICE_INTERFACE);
            service.setComponent(this.mComponentName);
            try {
                this.mBound = this.mContext.bindServiceAsUser(service, this, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, new android.os.UserHandle(this.mUserId));
                if (DEBUG && !this.mBound) {
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
            this.mContext.unbindService(this);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
        if (DEBUG) {
            android.util.Slog.d(TAG, this + ": onServiceConnected()");
        }
        this.mConnected = true;
        android.media.tv.ITvRemoteProvider provider = android.media.tv.ITvRemoteProvider.Stub.asInterface(service);
        if (provider == null) {
            android.util.Slog.e(TAG, this + ": Invalid binder");
            return;
        }
        try {
            provider.setRemoteServiceInputSink(new com.android.server.tv.TvRemoteServiceInput(this.mLock, provider));
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, this + ": Failed remote call to setRemoteServiceInputSink");
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(android.content.ComponentName name) {
        this.mConnected = false;
        if (DEBUG) {
            android.util.Slog.d(TAG, this + ": onServiceDisconnected()");
        }
    }
}
