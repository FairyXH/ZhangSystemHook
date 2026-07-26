package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class OneTimePermissionUserManager {
    private static final boolean DEBUG = false;
    private static final long DEFAULT_KILLED_DELAY_MILLIS = 5000;
    private static final java.lang.String LOG_TAG = com.android.server.pm.permission.OneTimePermissionUserManager.class.getSimpleName();
    public static final java.lang.String PROPERTY_KILLED_DELAY_CONFIG_KEY = "one_time_permissions_killed_delay_millis";
    private final android.app.AlarmManager mAlarmManager;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.permission.PermissionControllerManager mPermissionControllerManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.content.BroadcastReceiver mUninstallListener = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.UID_REMOVED".equals(intent.getAction())) {
                int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener listener = (com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener) com.android.server.pm.permission.OneTimePermissionUserManager.this.mListeners.get(uid);
                if (listener != null) {
                    listener.cancel();
                    com.android.server.pm.permission.OneTimePermissionUserManager.this.mListeners.remove(uid);
                }
            }
        }
    };
    private final android.util.SparseArray<com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener> mListeners = new android.util.SparseArray<>();
    private final android.app.IActivityManager mIActivityManager = android.app.ActivityManager.getService();
    private final android.app.ActivityManagerInternal mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);

    OneTimePermissionUserManager(android.content.Context context) {
        this.mContext = context;
        this.mAlarmManager = (android.app.AlarmManager) context.getSystemService(android.app.AlarmManager.class);
        this.mPermissionControllerManager = new android.permission.PermissionControllerManager(this.mContext, com.android.server.PermissionThread.getHandler());
        this.mHandler = context.getMainThreadHandler();
    }

    void startPackageOneTimeSession(java.lang.String packageName, int deviceId, long timeoutMillis, long revokeAfterKilledDelayMillis) throws java.lang.Throwable {
        try {
            int uid = this.mContext.getPackageManager().getPackageUid(packageName, 0);
            synchronized (this.mLock) {
                try {
                    com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener listener = this.mListeners.get(uid);
                    try {
                        if (listener != null) {
                            listener.updateSessionParameters(timeoutMillis, revokeAfterKilledDelayMillis);
                        } else {
                            this.mListeners.put(uid, new com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener(uid, packageName, deviceId, timeoutMillis, revokeAfterKilledDelayMillis));
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(LOG_TAG, "Unknown package name " + packageName + ", device ID " + deviceId, e);
        }
    }

    void stopPackageOneTimeSession(java.lang.String packageName) {
        com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener listener;
        try {
            int uid = this.mContext.getPackageManager().getPackageUid(packageName, 0);
            synchronized (this.mLock) {
                listener = this.mListeners.get(uid);
                if (listener != null) {
                    this.mListeners.remove(uid);
                }
            }
            if (listener != null) {
                listener.cancel();
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(LOG_TAG, "Unknown package name " + packageName, e);
        }
    }

    void registerUninstallListener() {
        this.mContext.registerReceiver(this.mUninstallListener, new android.content.IntentFilter("android.intent.action.UID_REMOVED"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PackageInactivityListener implements android.app.AlarmManager.OnAlarmListener {
        private static final int STATE_ACTIVE = 2;
        private static final int STATE_GONE = 0;
        private static final int STATE_TIMER = 1;
        private static final long TIMER_INACTIVE = -1;
        private final int mDeviceId;
        private final java.lang.Object mInnerLock;
        private boolean mIsAlarmSet;
        private boolean mIsFinished;
        private final android.app.IUidObserver mObserver;
        private final java.lang.String mPackageName;
        private long mRevokeAfterKilledDelay;
        private long mTimeout;
        private long mTimerStart;
        private final java.lang.Object mToken;
        private final int mUid;

        private PackageInactivityListener(int uid, java.lang.String packageName, int deviceId, long timeout, long revokeAfterkilledDelay) {
            long j;
            this.mTimerStart = -1L;
            this.mInnerLock = new java.lang.Object();
            this.mToken = new java.lang.Object();
            this.mObserver = new android.app.UidObserver() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener.1
                public void onUidGone(int uid2, boolean disabled) {
                    if (uid2 == com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener.this.mUid) {
                        com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener.this.updateUidState(0);
                    }
                }

                public void onUidStateChanged(int uid2, int procState, long procStateSeq, int capability) {
                    if (uid2 == com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener.this.mUid) {
                        if (procState > 4 && procState != 20) {
                            com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener.this.updateUidState(1);
                        } else {
                            com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener.this.updateUidState(2);
                        }
                    }
                }
            };
            android.util.Log.i(com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, "Start tracking " + packageName + ". uid=" + uid + " timeout=" + timeout + " killedDelay=" + revokeAfterkilledDelay);
            this.mUid = uid;
            this.mPackageName = packageName;
            this.mDeviceId = deviceId;
            this.mTimeout = timeout;
            if (revokeAfterkilledDelay == -1) {
                j = android.provider.DeviceConfig.getLong("permissions", com.android.server.pm.permission.OneTimePermissionUserManager.PROPERTY_KILLED_DELAY_CONFIG_KEY, com.android.server.pm.permission.OneTimePermissionUserManager.DEFAULT_KILLED_DELAY_MILLIS);
            } else {
                j = revokeAfterkilledDelay;
            }
            this.mRevokeAfterKilledDelay = j;
            try {
                com.android.server.pm.permission.OneTimePermissionUserManager.this.mIActivityManager.registerUidObserver(this.mObserver, 3, 4, (java.lang.String) null);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, "Couldn't check uid proc state", e);
                synchronized (this.mInnerLock) {
                    onPackageInactiveLocked();
                }
            }
            updateUidState();
        }

        public void updateSessionParameters(long timeoutMillis, long revokeAfterKilledDelayMillis) {
            long j;
            synchronized (this.mInnerLock) {
                this.mTimeout = java.lang.Math.min(this.mTimeout, timeoutMillis);
                long j2 = this.mRevokeAfterKilledDelay;
                if (revokeAfterKilledDelayMillis == -1) {
                    j = android.provider.DeviceConfig.getLong("permissions", com.android.server.pm.permission.OneTimePermissionUserManager.PROPERTY_KILLED_DELAY_CONFIG_KEY, com.android.server.pm.permission.OneTimePermissionUserManager.DEFAULT_KILLED_DELAY_MILLIS);
                } else {
                    j = revokeAfterKilledDelayMillis;
                }
                this.mRevokeAfterKilledDelay = java.lang.Math.min(j2, j);
                android.util.Log.v(com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, "Updated params for " + this.mPackageName + ", device ID " + this.mDeviceId + ". timeout=" + this.mTimeout + " killedDelay=" + this.mRevokeAfterKilledDelay);
                updateUidState();
            }
        }

        private int getCurrentState() {
            return getStateFromProcState(com.android.server.pm.permission.OneTimePermissionUserManager.this.mActivityManagerInternal.getUidProcessState(this.mUid));
        }

        private int getStateFromProcState(int procState) {
            if (procState == 20) {
                return 0;
            }
            if (procState > 4) {
                return 1;
            }
            return 2;
        }

        private void updateUidState() {
            updateUidState(getCurrentState());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateUidState(int state) {
            android.util.Log.v(com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, "Updating state for " + this.mPackageName + " (" + this.mUid + "). device ID=" + this.mDeviceId + ", state=" + state);
            synchronized (this.mInnerLock) {
                com.android.server.pm.permission.OneTimePermissionUserManager.this.mHandler.removeCallbacksAndMessages(this.mToken);
                if (state == 0) {
                    if (this.mRevokeAfterKilledDelay == 0) {
                        onPackageInactiveLocked();
                        return;
                    } else {
                        com.android.server.pm.permission.OneTimePermissionUserManager.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager$PackageInactivityListener$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$updateUidState$0();
                            }
                        }, this.mToken, this.mRevokeAfterKilledDelay);
                        return;
                    }
                }
                if (state == 1) {
                    if (this.mTimerStart == -1) {
                        this.mTimerStart = java.lang.System.currentTimeMillis();
                        setAlarmLocked();
                    }
                } else if (state == 2) {
                    this.mTimerStart = -1L;
                    cancelAlarmLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateUidState$0() {
            synchronized (this.mInnerLock) {
                int currentState = getCurrentState();
                if (currentState == 0) {
                    onPackageInactiveLocked();
                } else {
                    updateUidState(currentState);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancel() {
            synchronized (this.mInnerLock) {
                this.mIsFinished = true;
                cancelAlarmLocked();
                try {
                    com.android.server.pm.permission.OneTimePermissionUserManager.this.mIActivityManager.unregisterUidObserver(this.mObserver);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, "Unable to unregister uid observer.", e);
                }
            }
        }

        private void setAlarmLocked() {
            if (this.mIsAlarmSet) {
                return;
            }
            long revokeTime = this.mTimerStart + this.mTimeout;
            if (revokeTime > java.lang.System.currentTimeMillis()) {
                com.android.server.pm.permission.OneTimePermissionUserManager.this.mAlarmManager.setExact(0, revokeTime, com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, this, com.android.server.pm.permission.OneTimePermissionUserManager.this.mHandler);
                this.mIsAlarmSet = true;
            } else {
                this.mIsAlarmSet = true;
                onAlarm();
            }
        }

        private void cancelAlarmLocked() {
            if (this.mIsAlarmSet) {
                com.android.server.pm.permission.OneTimePermissionUserManager.this.mAlarmManager.cancel(this);
                this.mIsAlarmSet = false;
            }
        }

        private void onPackageInactiveLocked() {
            if (this.mIsFinished) {
                return;
            }
            this.mIsFinished = true;
            cancelAlarmLocked();
            com.android.server.pm.permission.OneTimePermissionUserManager.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager$PackageInactivityListener$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPackageInactiveLocked$1();
                }
            });
            try {
                com.android.server.pm.permission.OneTimePermissionUserManager.this.mIActivityManager.unregisterUidObserver(this.mObserver);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, "Unable to unregister uid observer.", e);
            }
            synchronized (com.android.server.pm.permission.OneTimePermissionUserManager.this.mLock) {
                com.android.server.pm.permission.OneTimePermissionUserManager.this.mListeners.remove(this.mUid);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPackageInactiveLocked$1() {
            android.util.Log.i(com.android.server.pm.permission.OneTimePermissionUserManager.LOG_TAG, "One time session expired for " + this.mPackageName + " (" + this.mUid + "). deviceID " + this.mDeviceId);
            com.android.server.pm.permission.OneTimePermissionUserManager.this.mPermissionControllerManager.notifyOneTimePermissionSessionTimeout(this.mPackageName, this.mDeviceId);
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            synchronized (this.mInnerLock) {
                if (this.mIsAlarmSet) {
                    this.mIsAlarmSet = false;
                    onPackageInactiveLocked();
                }
            }
        }
    }
}
