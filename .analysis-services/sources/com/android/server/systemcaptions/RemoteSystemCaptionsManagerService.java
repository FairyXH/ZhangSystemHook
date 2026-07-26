package com.android.server.systemcaptions;

/* JADX INFO: loaded from: classes3.dex */
final class RemoteSystemCaptionsManagerService {
    private static final int MSG_BIND = 1;
    private static final java.lang.String SERVICE_INTERFACE = "android.service.systemcaptions.SystemCaptionsManagerService";
    private static final java.lang.String TAG = com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.class.getSimpleName();
    private final android.content.ComponentName mComponentName;
    private final android.content.Context mContext;
    private final android.content.Intent mIntent;
    private android.os.IBinder mService;
    private final int mUserId;
    private final boolean mVerbose;
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.RemoteServiceConnection mServiceConnection = new com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.RemoteServiceConnection();
    private boolean mBinding = false;
    private boolean mDestroyed = false;
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    RemoteSystemCaptionsManagerService(android.content.Context context, android.content.ComponentName componentName, int userId, boolean verbose) {
        this.mContext = context;
        this.mComponentName = componentName;
        this.mUserId = userId;
        this.mVerbose = verbose;
        this.mIntent = new android.content.Intent(SERVICE_INTERFACE).setComponent(componentName);
    }

    void initialize() {
        if (this.mVerbose) {
            android.util.Slog.v(TAG, "initialize()");
        }
        scheduleBind();
    }

    public void destroy() {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.systemcaptions.RemoteSystemCaptionsManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.systemcaptions.RemoteSystemCaptionsManagerService) obj).handleDestroy();
            }
        }, this));
    }

    void handleDestroy() {
        if (this.mVerbose) {
            android.util.Slog.v(TAG, "handleDestroy()");
        }
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                if (this.mVerbose) {
                    android.util.Slog.v(TAG, "handleDestroy(): Already destroyed");
                }
            } else {
                this.mDestroyed = true;
                ensureUnboundLocked();
            }
        }
    }

    boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    private void scheduleBind() {
        if (this.mHandler.hasMessages(1)) {
            if (this.mVerbose) {
                android.util.Slog.v(TAG, "scheduleBind(): already scheduled");
                return;
            }
            return;
        }
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.systemcaptions.RemoteSystemCaptionsManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.systemcaptions.RemoteSystemCaptionsManagerService) obj).handleEnsureBound();
            }
        }, this).setWhat(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnsureBound() {
        synchronized (this.mLock) {
            if (this.mService == null && !this.mBinding) {
                if (this.mVerbose) {
                    android.util.Slog.v(TAG, "handleEnsureBound(): binding");
                }
                this.mBinding = true;
                boolean willBind = this.mContext.bindServiceAsUser(this.mIntent, this.mServiceConnection, 67112961, this.mHandler, new android.os.UserHandle(this.mUserId));
                if (!willBind) {
                    android.util.Slog.w(TAG, "Could not bind to " + this.mIntent + " with flags 67112961");
                    this.mBinding = false;
                    this.mService = null;
                }
            }
        }
    }

    private void ensureUnboundLocked() {
        if (this.mService == null && !this.mBinding) {
            return;
        }
        this.mBinding = false;
        this.mService = null;
        if (this.mVerbose) {
            android.util.Slog.v(TAG, "ensureUnbound(): unbinding");
        }
        this.mContext.unbindService(this.mServiceConnection);
    }

    private class RemoteServiceConnection implements android.content.ServiceConnection {
        private RemoteServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mLock) {
                if (com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mVerbose) {
                    android.util.Slog.v(com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.TAG, "onServiceConnected()");
                }
                if (!com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mDestroyed && com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mBinding) {
                    com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mBinding = false;
                    com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mService = service;
                    return;
                }
                android.util.Slog.wtf(com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.TAG, "onServiceConnected() dispatched after unbindService");
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized (com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mLock) {
                if (com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mVerbose) {
                    android.util.Slog.v(com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.TAG, "onServiceDisconnected()");
                }
                com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mBinding = true;
                com.android.server.systemcaptions.RemoteSystemCaptionsManagerService.this.mService = null;
            }
        }
    }
}
