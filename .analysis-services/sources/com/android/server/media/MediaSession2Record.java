package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public class MediaSession2Record extends com.android.server.media.MediaSessionRecordImpl {
    private final android.media.MediaController2 mController;
    private final com.android.server.media.HandlerExecutor mHandlerExecutor;
    private boolean mIsClosed;
    private boolean mIsConnected;
    private final java.lang.Object mLock = new java.lang.Object();
    private final int mPid;
    private int mPolicies;
    private final com.android.server.media.MediaSessionService mService;
    private final android.media.Session2Token mSessionToken;
    private static final java.lang.String TAG = "MediaSession2Record";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    public MediaSession2Record(android.media.Session2Token sessionToken, com.android.server.media.MediaSessionService service, android.os.Looper handlerLooper, int pid, int policies) {
        synchronized (this.mLock) {
            this.mSessionToken = sessionToken;
            this.mService = service;
            this.mHandlerExecutor = new com.android.server.media.HandlerExecutor(new android.os.Handler(handlerLooper));
            this.mController = new android.media.MediaController2.Builder(service.getContext(), sessionToken).setControllerCallback(this.mHandlerExecutor, new com.android.server.media.MediaSession2Record.Controller2Callback()).build();
            this.mPid = pid;
            this.mPolicies = policies;
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public java.lang.String getPackageName() {
        return this.mSessionToken.getPackageName();
    }

    public android.media.Session2Token getSession2Token() {
        return this.mSessionToken;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getUid() {
        return this.mSessionToken.getUid();
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getUserId() {
        return android.os.UserHandle.getUserHandleForUid(this.mSessionToken.getUid()).getIdentifier();
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public android.app.ForegroundServiceDelegationOptions getForegroundServiceDelegationOptions() {
        return null;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isSystemPriority() {
        return false;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void adjustVolume(java.lang.String packageName, java.lang.String opPackageName, int pid, int uid, boolean asSystemService, int direction, int flags, boolean useSuggested) {
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isActive() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsConnected;
        }
        return z;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean checkPlaybackActiveState(boolean expected) {
        boolean z;
        synchronized (this.mLock) {
            z = true;
            if ((this.mIsConnected && this.mController.isPlaybackActive()) != expected) {
                z = false;
            }
        }
        return z;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isPlaybackTypeLocal() {
        return true;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void close() {
        synchronized (this.mLock) {
            this.mIsClosed = true;
            this.mController.close();
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isClosed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsClosed;
        }
        return z;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void expireTempEngaged() {
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean sendMediaButton(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent ke, int sequenceId, android.os.ResultReceiver cb) {
        return false;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean canHandleVolumeKey() {
        return false;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    boolean isLinkedToNotification(android.app.Notification notification) {
        return false;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getSessionPolicies() {
        int i;
        synchronized (this.mLock) {
            i = this.mPolicies;
        }
        return i;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void setSessionPolicies(int policies) {
        synchronized (this.mLock) {
            this.mPolicies = policies;
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "uniqueId=" + getUniqueId());
        pw.println(prefix + "token=" + this.mSessionToken);
        pw.println(prefix + "controller=" + this.mController);
        java.lang.String indent = prefix + "  ";
        pw.println(indent + "playbackActive=" + this.mController.isPlaybackActive());
    }

    public java.lang.String toString() {
        return getPackageName() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + getUniqueId() + " (userId=" + getUserId() + ")";
    }

    private class Controller2Callback extends android.media.MediaController2.ControllerCallback {
        private Controller2Callback() {
        }

        @Override // android.media.MediaController2.ControllerCallback
        public void onConnected(android.media.MediaController2 controller, android.media.Session2CommandGroup allowedCommands) {
            com.android.server.media.MediaSessionService service;
            if (com.android.server.media.MediaSession2Record.DEBUG) {
                android.util.Log.d(com.android.server.media.MediaSession2Record.TAG, "connected to " + com.android.server.media.MediaSession2Record.this.mSessionToken + ", allowed=" + allowedCommands);
            }
            synchronized (com.android.server.media.MediaSession2Record.this.mLock) {
                com.android.server.media.MediaSession2Record.this.mIsConnected = true;
                service = com.android.server.media.MediaSession2Record.this.mService;
            }
            service.onSessionActiveStateChanged(com.android.server.media.MediaSession2Record.this, null);
        }

        @Override // android.media.MediaController2.ControllerCallback
        public void onDisconnected(android.media.MediaController2 controller) {
            com.android.server.media.MediaSessionService service;
            if (com.android.server.media.MediaSession2Record.DEBUG) {
                android.util.Log.d(com.android.server.media.MediaSession2Record.TAG, "disconnected from " + com.android.server.media.MediaSession2Record.this.mSessionToken);
            }
            synchronized (com.android.server.media.MediaSession2Record.this.mLock) {
                com.android.server.media.MediaSession2Record.this.mIsConnected = false;
                service = com.android.server.media.MediaSession2Record.this.mService;
            }
            service.onSessionDied(com.android.server.media.MediaSession2Record.this);
        }

        @Override // android.media.MediaController2.ControllerCallback
        public void onPlaybackActiveChanged(android.media.MediaController2 controller, boolean playbackActive) {
            com.android.server.media.MediaSessionService service;
            if (com.android.server.media.MediaSession2Record.DEBUG) {
                android.util.Log.d(com.android.server.media.MediaSession2Record.TAG, "playback active changed, " + com.android.server.media.MediaSession2Record.this.mSessionToken + ", active=" + playbackActive);
            }
            synchronized (com.android.server.media.MediaSession2Record.this.mLock) {
                service = com.android.server.media.MediaSession2Record.this.mService;
            }
            service.onSessionPlaybackStateChanged(com.android.server.media.MediaSession2Record.this, playbackActive, null);
        }
    }
}
