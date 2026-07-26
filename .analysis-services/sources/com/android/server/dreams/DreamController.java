package com.android.server.dreams;

/* JADX INFO: loaded from: classes2.dex */
final class DreamController {
    private static final int DREAM_CONNECTION_TIMEOUT = 10000;
    private static final int DREAM_FINISH_TIMEOUT = 5000;
    private static final java.lang.String EXTRA_REASON_KEY = "reason";
    private static final java.lang.String EXTRA_REASON_VALUE = "dream";
    private static final java.lang.String TAG = "DreamController";
    private final android.app.ActivityTaskManager mActivityTaskManager;
    private final android.os.Bundle mCloseNotificationShadeOptions;
    private final android.content.Context mContext;
    private com.android.server.dreams.DreamController.DreamRecord mCurrentDream;
    private final android.os.Handler mHandler;
    private final com.android.server.dreams.DreamController.Listener mListener;
    private final android.os.PowerManager mPowerManager;
    private final boolean mResetScreenTimeoutOnUnexpectedDreamExit;
    private static final java.lang.String DREAMING_DELIVERY_GROUP_NAMESPACE = java.util.UUID.randomUUID().toString();
    private static final java.lang.String DREAMING_DELIVERY_GROUP_KEY = java.util.UUID.randomUUID().toString();
    private final android.content.Intent mDreamingStartedIntent = new android.content.Intent("android.intent.action.DREAMING_STARTED").addFlags(1342177280);
    private final android.content.Intent mDreamingStoppedIntent = new android.content.Intent("android.intent.action.DREAMING_STOPPED").addFlags(1342177280);
    private final android.os.Bundle mDreamingStartedStoppedOptions = createDreamingStartedStoppedOptions();
    private boolean mSentStartBroadcast = false;
    private final java.util.ArrayList<com.android.server.dreams.DreamController.DreamRecord> mPreviousDreams = new java.util.ArrayList<>();
    private final android.content.Intent mCloseNotificationShadeIntent = new android.content.Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");

    public interface Listener {
        void onDreamStarted(android.os.Binder binder);

        void onDreamStopped(android.os.Binder binder);
    }

    public DreamController(android.content.Context context, android.os.Handler handler, com.android.server.dreams.DreamController.Listener listener) {
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = listener;
        this.mActivityTaskManager = (android.app.ActivityTaskManager) this.mContext.getSystemService(android.app.ActivityTaskManager.class);
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
        this.mCloseNotificationShadeIntent.putExtra("reason", EXTRA_REASON_VALUE);
        this.mCloseNotificationShadeIntent.addFlags(268435456);
        this.mCloseNotificationShadeOptions = android.app.BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey("android.intent.action.CLOSE_SYSTEM_DIALOGS", EXTRA_REASON_VALUE).setDeferralPolicy(2).toBundle();
        this.mResetScreenTimeoutOnUnexpectedDreamExit = context.getResources().getBoolean(android.R.bool.config_pauseWallpaperRenderWhenStateChangeEnabled);
    }

    private android.os.Bundle createDreamingStartedStoppedOptions() {
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setDeliveryGroupPolicy(1);
        options.setDeliveryGroupMatchingKey(DREAMING_DELIVERY_GROUP_NAMESPACE, DREAMING_DELIVERY_GROUP_KEY);
        options.setDeferralPolicy(2);
        return options.toBundle();
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("Dreamland:");
        if (this.mCurrentDream != null) {
            pw.println("  mCurrentDream:");
            pw.println("    mToken=" + this.mCurrentDream.mToken);
            pw.println("    mName=" + this.mCurrentDream.mName);
            pw.println("    mIsPreviewMode=" + this.mCurrentDream.mIsPreviewMode);
            pw.println("    mCanDoze=" + this.mCurrentDream.mCanDoze);
            pw.println("    mUserId=" + this.mCurrentDream.mUserId);
            pw.println("    mBound=" + this.mCurrentDream.mBound);
            pw.println("    mService=" + this.mCurrentDream.mService);
            pw.println("    mWakingGently=" + this.mCurrentDream.mWakingGently);
        } else {
            pw.println("  mCurrentDream: null");
        }
        pw.println("  mSentStartBroadcast=" + this.mSentStartBroadcast);
    }

    public void startDream(android.os.Binder token, android.content.ComponentName name, boolean isPreviewMode, boolean canDoze, int userId, android.os.PowerManager.WakeLock wakeLock, android.content.ComponentName overlayComponentName, java.lang.String reason) {
        android.os.Trace.traceBegin(131072L, "startDream");
        try {
            this.mContext.sendBroadcastAsUser(this.mCloseNotificationShadeIntent, android.os.UserHandle.ALL, null, this.mCloseNotificationShadeOptions);
            android.util.Slog.i(TAG, "Starting dream: name=" + name + ", isPreviewMode=" + isPreviewMode + ", canDoze=" + canDoze + ", userId=" + userId + ", reason='" + reason + "'");
            com.android.server.dreams.DreamController.DreamRecord oldDream = this.mCurrentDream;
            this.mCurrentDream = new com.android.server.dreams.DreamController.DreamRecord(token, name, isPreviewMode, canDoze, userId, wakeLock);
            if (oldDream != null) {
                if (java.util.Objects.equals(oldDream.mName, this.mCurrentDream.mName)) {
                    stopDreamInstance(true, "restarting same dream", oldDream);
                } else {
                    this.mPreviousDreams.add(oldDream);
                }
            }
            this.mCurrentDream.mDreamStartTime = android.os.SystemClock.elapsedRealtime();
            com.android.internal.logging.MetricsLogger.visible(this.mContext, this.mCurrentDream.mCanDoze ? com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED : 222);
            android.content.Intent intent = new android.content.Intent("android.service.dreams.DreamService");
            intent.setComponent(name);
            intent.addFlags(8388608);
            try {
                android.service.dreams.DreamService.setDreamOverlayComponent(intent, overlayComponentName);
                try {
                    if (this.mContext.bindServiceAsUser(intent, this.mCurrentDream, 71303169, new android.os.UserHandle(userId))) {
                        this.mCurrentDream.mBound = true;
                        this.mHandler.postDelayed(this.mCurrentDream.mStopUnconnectedDreamRunnable, 10000L);
                        android.os.Trace.traceEnd(131072L);
                    } else {
                        android.util.Slog.e(TAG, "Unable to bind dream service: " + intent);
                        stopDream(true, "bindService failed");
                        android.os.Trace.traceEnd(131072L);
                    }
                } catch (java.lang.SecurityException ex) {
                    android.util.Slog.e(TAG, "Unable to bind dream service: " + intent, ex);
                    stopDream(true, "unable to bind service: SecExp.");
                    android.os.Trace.traceEnd(131072L);
                }
            } catch (java.lang.Throwable th) {
                ex = th;
                android.os.Trace.traceEnd(131072L);
                throw ex;
            }
        } catch (java.lang.Throwable th2) {
            ex = th2;
        }
    }

    void setDreamAppTask(android.os.Binder dreamToken, android.app.IAppTask appTask) {
        if (this.mCurrentDream == null || this.mCurrentDream.mToken != dreamToken || this.mCurrentDream.mAppTask != null) {
            android.util.Slog.e(TAG, "Illegal dream activity start. mCurrentDream.mToken = " + this.mCurrentDream.mToken + ", illegal dreamToken = " + dreamToken + ". Ending this dream activity.");
            try {
                appTask.finishAndRemoveTask();
                return;
            } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                android.util.Slog.e(TAG, "Unable to stop illegal dream activity.");
                return;
            }
        }
        this.mCurrentDream.mAppTask = appTask;
    }

    void setDreamIsObscured(boolean isObscured) {
        if (this.mCurrentDream != null) {
            this.mCurrentDream.mDreamIsObscured = isObscured;
        }
    }

    boolean dreamIsFrontmost() {
        return this.mCurrentDream != null && this.mCurrentDream.dreamIsFrontmost();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetScreenTimeout() {
        android.util.Slog.i(TAG, "Resetting screen timeout");
        long time = android.os.SystemClock.uptimeMillis();
        this.mPowerManager.userActivity(time, 0, 1);
    }

    public void stopDream(boolean immediate, java.lang.String reason) {
        stopPreviousDreams();
        stopDreamInstance(immediate, reason, this.mCurrentDream);
    }

    public boolean bringDreamToFront() {
        if (this.mCurrentDream == null || this.mCurrentDream.mService == null) {
            return false;
        }
        try {
            this.mCurrentDream.mService.comeToFront();
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error asking dream to come to the front", e);
            return false;
        }
    }

    private void stopDreamInstance(boolean immediate, java.lang.String reason, com.android.server.dreams.DreamController.DreamRecord dream) {
        if (dream == null) {
            return;
        }
        android.os.Trace.traceBegin(131072L, "stopDream");
        if (!immediate) {
            try {
                if (dream.mWakingGently) {
                    return;
                }
                if (dream.mService != null) {
                    dream.mWakingGently = true;
                    try {
                        dream.mStopReason = reason;
                        dream.mService.wakeUp();
                        this.mHandler.postDelayed(dream.mStopStubbornDreamRunnable, 5000L);
                        return;
                    } catch (android.os.RemoteException e) {
                    }
                }
            } finally {
                android.os.Trace.traceEnd(131072L);
            }
        }
        android.util.Slog.i(TAG, "Stopping dream: name=" + dream.mName + ", isPreviewMode=" + dream.mIsPreviewMode + ", canDoze=" + dream.mCanDoze + ", userId=" + dream.mUserId + ", reason='" + reason + "'" + (dream.mStopReason == null ? "" : "(from '" + dream.mStopReason + "')"));
        com.android.internal.logging.MetricsLogger.hidden(this.mContext, dream.mCanDoze ? com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED : 222);
        com.android.internal.logging.MetricsLogger.histogram(this.mContext, dream.mCanDoze ? "dozing_minutes" : "dreaming_minutes", (int) ((android.os.SystemClock.elapsedRealtime() - dream.mDreamStartTime) / 60000));
        this.mHandler.removeCallbacks(dream.mStopUnconnectedDreamRunnable);
        this.mHandler.removeCallbacks(dream.mStopStubbornDreamRunnable);
        if (dream.mService != null) {
            try {
                dream.mService.detach();
            } catch (android.os.RemoteException e2) {
            }
            try {
                dream.mService.asBinder().unlinkToDeath(dream, 0);
            } catch (java.util.NoSuchElementException e3) {
            }
            dream.mService = null;
        }
        if (dream.mBound) {
            this.mContext.unbindService(dream);
        }
        dream.releaseWakeLockIfNeeded();
        if (dream == this.mCurrentDream) {
            this.mCurrentDream = null;
            if (this.mSentStartBroadcast) {
                this.mContext.sendBroadcastAsUser(this.mDreamingStoppedIntent, android.os.UserHandle.ALL, null, this.mDreamingStartedStoppedOptions);
                this.mSentStartBroadcast = false;
            }
            if (this.mCurrentDream != null && this.mCurrentDream.mAppTask != null) {
                try {
                    this.mCurrentDream.mAppTask.finishAndRemoveTask();
                } catch (android.os.RemoteException | java.lang.RuntimeException e4) {
                    android.util.Slog.e(TAG, "Unable to stop dream activity.");
                }
            }
            this.mListener.onDreamStopped(dream.mToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPreviousDreams() {
        if (this.mPreviousDreams.isEmpty()) {
            return;
        }
        java.util.Iterator<com.android.server.dreams.DreamController.DreamRecord> it = this.mPreviousDreams.iterator();
        while (it.hasNext()) {
            stopDreamInstance(true, "stop previous dream", it.next());
            it.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attach(android.service.dreams.IDreamService service) {
        try {
            service.asBinder().linkToDeath(this.mCurrentDream, 0);
            service.attach(this.mCurrentDream.mToken, this.mCurrentDream.mCanDoze, this.mCurrentDream.mIsPreviewMode, this.mCurrentDream.mDreamingStartedCallback);
            this.mCurrentDream.mService = service;
            if (!this.mCurrentDream.mIsPreviewMode && !this.mSentStartBroadcast) {
                this.mContext.sendBroadcastAsUser(this.mDreamingStartedIntent, android.os.UserHandle.ALL, null, this.mDreamingStartedStoppedOptions);
                this.mListener.onDreamStarted(this.mCurrentDream.mToken);
                this.mSentStartBroadcast = true;
            }
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(TAG, "The dream service died unexpectedly.", ex);
            stopDream(true, "attach failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class DreamRecord implements android.os.IBinder.DeathRecipient, android.content.ServiceConnection {
        public android.app.IAppTask mAppTask;
        public boolean mBound;
        public final boolean mCanDoze;
        public boolean mConnected;
        private boolean mDreamIsObscured;
        private long mDreamStartTime;
        public final boolean mIsPreviewMode;
        public final android.content.ComponentName mName;
        public android.service.dreams.IDreamService mService;
        private java.lang.String mStopReason;
        public final android.os.Binder mToken;
        public final int mUserId;
        public android.os.PowerManager.WakeLock mWakeLock;
        public boolean mWakingGently;
        private final java.lang.Runnable mStopPreviousDreamsIfNeeded = new java.lang.Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.stopPreviousDreamsIfNeeded();
            }
        };
        private final java.lang.Runnable mReleaseWakeLockIfNeeded = new java.lang.Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.releaseWakeLockIfNeeded();
            }
        };
        private final java.lang.Runnable mStopUnconnectedDreamRunnable = new java.lang.Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        private final java.lang.Runnable mStopStubbornDreamRunnable = new java.lang.Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        };
        private final android.os.IRemoteCallback mDreamingStartedCallback = new android.os.IRemoteCallback.Stub() { // from class: com.android.server.dreams.DreamController.DreamRecord.1
            public void sendResult(android.os.Bundle data) {
                com.android.server.dreams.DreamController.this.mHandler.post(com.android.server.dreams.DreamController.DreamRecord.this.mStopPreviousDreamsIfNeeded);
                com.android.server.dreams.DreamController.this.mHandler.post(com.android.server.dreams.DreamController.DreamRecord.this.mReleaseWakeLockIfNeeded);
            }
        };

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            if (this.mBound && !this.mConnected) {
                android.util.Slog.w(com.android.server.dreams.DreamController.TAG, "Bound dream did not connect in the time allotted");
                com.android.server.dreams.DreamController.this.stopDream(true, "slow to connect");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            android.util.Slog.w(com.android.server.dreams.DreamController.TAG, "Stubborn dream did not finish itself in the time allotted");
            com.android.server.dreams.DreamController.this.stopDream(true, "slow to finish");
            this.mStopReason = null;
        }

        DreamRecord(android.os.Binder token, android.content.ComponentName name, boolean isPreviewMode, boolean canDoze, int userId, android.os.PowerManager.WakeLock wakeLock) {
            this.mToken = token;
            this.mName = name;
            this.mIsPreviewMode = isPreviewMode;
            this.mCanDoze = canDoze;
            this.mUserId = userId;
            this.mWakeLock = wakeLock;
            if (this.mWakeLock != null) {
                this.mWakeLock.acquire();
            }
            com.android.server.dreams.DreamController.this.mHandler.postDelayed(this.mReleaseWakeLockIfNeeded, 10000L);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.dreams.DreamController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$binderDied$2();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$binderDied$2() {
            this.mService = null;
            if (com.android.server.dreams.DreamController.this.mCurrentDream == this) {
                if (com.android.server.dreams.DreamController.this.mResetScreenTimeoutOnUnexpectedDreamExit) {
                    com.android.server.dreams.DreamController.this.resetScreenTimeout();
                }
                com.android.server.dreams.DreamController.this.stopDream(true, "binder died");
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, final android.os.IBinder service) {
            com.android.server.dreams.DreamController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceConnected$3(service);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceConnected$3(android.os.IBinder service) {
            this.mConnected = true;
            if (com.android.server.dreams.DreamController.this.mCurrentDream == this && this.mService == null) {
                com.android.server.dreams.DreamController.this.attach(android.service.dreams.IDreamService.Stub.asInterface(service));
            } else {
                releaseWakeLockIfNeeded();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            com.android.server.dreams.DreamController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.dreams.DreamController$DreamRecord$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceDisconnected$4();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceDisconnected$4() {
            this.mService = null;
            if (com.android.server.dreams.DreamController.this.mCurrentDream == this) {
                if (com.android.server.dreams.DreamController.this.mResetScreenTimeoutOnUnexpectedDreamExit) {
                    com.android.server.dreams.DreamController.this.resetScreenTimeout();
                }
                com.android.server.dreams.DreamController.this.stopDream(true, "service disconnected");
            }
        }

        void stopPreviousDreamsIfNeeded() {
            if (com.android.server.dreams.DreamController.this.mCurrentDream == this) {
                com.android.server.dreams.DreamController.this.stopPreviousDreams();
            }
        }

        void releaseWakeLockIfNeeded() {
            if (this.mWakeLock != null) {
                this.mWakeLock.release();
                this.mWakeLock = null;
                com.android.server.dreams.DreamController.this.mHandler.removeCallbacks(this.mReleaseWakeLockIfNeeded);
            }
        }

        boolean dreamIsFrontmost() {
            return !this.mDreamIsObscured;
        }
    }
}
