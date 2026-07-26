package com.android.server.trust;

/* JADX INFO: loaded from: classes3.dex */
public class TrustManagerService extends com.android.server.SystemService {
    private static final int MSG_CLEANUP_USER = 8;
    private static final int MSG_DISPATCH_UNLOCK_ATTEMPT = 3;
    private static final int MSG_DISPATCH_UNLOCK_LOCKOUT = 13;
    private static final int MSG_ENABLED_AGENTS_CHANGED = 4;
    private static final int MSG_FLUSH_TRUST_USUALLY_MANAGED = 10;
    private static final int MSG_KEYGUARD_SHOWING_CHANGED = 6;
    private static final int MSG_REFRESH_DEVICE_LOCKED_FOR_USER = 14;
    private static final int MSG_REFRESH_TRUSTABLE_TIMERS_AFTER_AUTH = 17;
    private static final int MSG_REGISTER_LISTENER = 1;
    private static final int MSG_SCHEDULE_TRUST_TIMEOUT = 15;
    private static final int MSG_START_USER = 7;
    private static final int MSG_STOP_USER = 12;
    private static final int MSG_SWITCH_USER = 9;
    private static final int MSG_UNLOCK_USER = 11;
    private static final int MSG_UNREGISTER_LISTENER = 2;
    private static final int MSG_USER_MAY_REQUEST_UNLOCK = 18;
    private static final int MSG_USER_REQUESTED_UNLOCK = 16;
    private static final java.lang.String PERMISSION_PROVIDE_AGENT = "android.permission.PROVIDE_TRUST_AGENT";
    private static final java.lang.String PRIV_NAMESPACE = "http://schemas.android.com/apk/prv/res/android";
    private static final java.lang.String REFRESH_DEVICE_LOCKED_EXCEPT_USER = "except";
    private static final java.lang.String TAG = "TrustManagerService";
    private static final long TRUSTABLE_IDLE_TIMEOUT_IN_MILLIS = 28800000;
    private static final long TRUSTABLE_TIMEOUT_IN_MILLIS = 86400000;
    private static final java.lang.String TRUST_TIMEOUT_ALARM_TAG = "TrustManagerService.trustTimeoutForUser";
    private static final long TRUST_TIMEOUT_IN_MILLIS = 14400000;
    private static final int TRUST_USUALLY_MANAGED_FLUSH_DELAY = 120000;
    private final android.util.ArraySet<com.android.server.trust.TrustManagerService.AgentInfo> mActiveAgents;
    private final android.app.ActivityManager mActivityManager;
    private final java.lang.Object mAlarmLock;
    private android.app.AlarmManager mAlarmManager;
    final com.android.server.trust.TrustArchive mArchive;
    private final android.content.Context mContext;
    private int mCurrentUser;
    private final android.util.SparseBooleanArray mDeviceLockedForUser;
    private android.hardware.face.FaceManager mFaceManager;
    private android.hardware.fingerprint.FingerprintManager mFingerprintManager;
    private final android.os.Handler mHandler;
    private final android.util.SparseArray<com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener> mIdleTrustableTimeoutAlarmListenerForUser;
    private volatile boolean mIsInSignificantPlace;
    private final android.security.KeyStoreAuthorization mKeyStoreAuthorization;
    private final android.util.SparseBooleanArray mLastActiveUnlockRunningState;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    final com.android.internal.content.PackageMonitor mPackageMonitor;
    private final com.android.server.trust.TrustManagerService.Receiver mReceiver;
    private final android.os.IBinder mService;
    private com.android.server.servicewatcher.ServiceWatcher mSignificantPlaceServiceWatcher;
    private final com.android.server.trust.TrustManagerService.StrongAuthTracker mStrongAuthTracker;
    private boolean mTrustAgentsCanRun;
    private final java.util.ArrayList<android.app.trust.ITrustListener> mTrustListeners;
    private final android.util.ArrayMap<java.lang.Integer, com.android.server.trust.TrustManagerService.TrustedTimeoutAlarmListener> mTrustTimeoutAlarmListenerForUser;
    private final android.util.SparseBooleanArray mTrustUsuallyManagedForUser;
    private final android.util.SparseArray<com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener> mTrustableTimeoutAlarmListenerForUser;
    private final android.os.UserManager mUserManager;
    private final android.util.SparseArray<com.android.server.trust.TrustManagerService.TrustState> mUserTrustState;
    private final android.util.SparseBooleanArray mUsersUnlockedByBiometric;
    static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final android.content.Intent TRUST_AGENT_INTENT = new android.content.Intent("android.service.trust.TrustAgentService");

    private enum TimeoutType {
        TRUSTED,
        TRUSTABLE
    }

    private enum TrustState {
        UNTRUSTED,
        TRUSTABLE,
        TRUSTED
    }

    protected static class Injector {
        private final android.content.Context mContext;

        public Injector(android.content.Context context) {
            this.mContext = context;
        }

        com.android.internal.widget.LockPatternUtils getLockPatternUtils() {
            return new com.android.internal.widget.LockPatternUtils(this.mContext);
        }

        android.security.KeyStoreAuthorization getKeyStoreAuthorization() {
            return android.security.KeyStoreAuthorization.getInstance();
        }

        android.app.AlarmManager getAlarmManager() {
            return (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        }

        android.os.Looper getLooper() {
            return android.os.Looper.myLooper();
        }
    }

    public TrustManagerService(android.content.Context context) {
        this(context, new com.android.server.trust.TrustManagerService.Injector(context));
    }

    protected TrustManagerService(android.content.Context context, com.android.server.trust.TrustManagerService.Injector injector) {
        super(context);
        this.mActiveAgents = new android.util.ArraySet<>();
        this.mLastActiveUnlockRunningState = new android.util.SparseBooleanArray();
        this.mTrustListeners = new java.util.ArrayList<>();
        this.mReceiver = new com.android.server.trust.TrustManagerService.Receiver();
        this.mArchive = new com.android.server.trust.TrustArchive();
        this.mUserTrustState = new android.util.SparseArray<>();
        this.mDeviceLockedForUser = new android.util.SparseBooleanArray();
        this.mTrustUsuallyManagedForUser = new android.util.SparseBooleanArray();
        this.mUsersUnlockedByBiometric = new android.util.SparseBooleanArray();
        this.mTrustTimeoutAlarmListenerForUser = new android.util.ArrayMap<>();
        this.mTrustableTimeoutAlarmListenerForUser = new android.util.SparseArray<>();
        this.mIdleTrustableTimeoutAlarmListenerForUser = new android.util.SparseArray<>();
        this.mAlarmLock = new java.lang.Object();
        this.mTrustAgentsCanRun = false;
        this.mCurrentUser = 0;
        this.mIsInSignificantPlace = false;
        this.mService = new com.android.server.trust.TrustManagerService.AnonymousClass2();
        this.mPackageMonitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.trust.TrustManagerService.4
            public void onSomePackagesChanged() {
                com.android.server.trust.TrustManagerService.this.refreshAgentList(-1);
            }

            public void onPackageAdded(java.lang.String packageName, int uid) {
                com.android.server.trust.TrustManagerService.this.checkNewAgentsForUser(android.os.UserHandle.getUserId(uid));
            }

            public boolean onPackageChanged(java.lang.String packageName, int uid, java.lang.String[] components) {
                com.android.server.trust.TrustManagerService.this.checkNewAgentsForUser(android.os.UserHandle.getUserId(uid));
                return true;
            }

            public void onPackageDisappeared(java.lang.String packageName, int reason) {
                com.android.server.trust.TrustManagerService.this.removeAgentsOfPackage(packageName);
            }
        };
        this.mContext = context;
        this.mHandler = createHandler(injector.getLooper());
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService("user");
        this.mActivityManager = (android.app.ActivityManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
        this.mLockPatternUtils = injector.getLockPatternUtils();
        this.mKeyStoreAuthorization = injector.getKeyStoreAuthorization();
        this.mStrongAuthTracker = new com.android.server.trust.TrustManagerService.StrongAuthTracker(context, injector.getLooper());
        this.mAlarmManager = injector.getAlarmManager();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("trust", this.mService);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) throws java.lang.Throwable {
        if (isSafeMode()) {
            return;
        }
        if (phase == 500) {
            checkNewAgents();
            this.mPackageMonitor.register(this.mContext, this.mHandler.getLooper(), android.os.UserHandle.ALL, true);
            this.mReceiver.register(this.mContext);
            this.mLockPatternUtils.registerStrongAuthTracker(this.mStrongAuthTracker);
            this.mFingerprintManager = (android.hardware.fingerprint.FingerprintManager) this.mContext.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
            this.mFaceManager = (android.hardware.face.FaceManager) this.mContext.getSystemService(android.hardware.face.FaceManager.class);
            return;
        }
        if (phase == 600) {
            this.mTrustAgentsCanRun = true;
            refreshAgentList(-1);
            refreshDeviceLockedForUser(-1);
            if (android.security.Flags.significantPlaces()) {
                this.mSignificantPlaceServiceWatcher = com.android.server.servicewatcher.ServiceWatcher.create(this.mContext, TAG, com.android.server.servicewatcher.CurrentUserServiceSupplier.create(this.mContext, "com.android.trust.provider.SignificantPlaceProvider.BIND", null, null, null), new com.android.server.servicewatcher.ServiceWatcher.ServiceListener<com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo>() { // from class: com.android.server.trust.TrustManagerService.1
                    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
                    public void onBind(android.os.IBinder binder, com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo service) throws android.os.RemoteException {
                        android.hardware.location.ISignificantPlaceProvider.Stub.asInterface(binder).setSignificantPlaceProviderManager(new android.hardware.location.ISignificantPlaceProviderManager.Stub() { // from class: com.android.server.trust.TrustManagerService.1.1
                            public void setInSignificantPlace(boolean inSignificantPlace) {
                                com.android.server.trust.TrustManagerService.this.mIsInSignificantPlace = inSignificantPlace;
                            }
                        });
                    }

                    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
                    public void onUnbind() {
                        com.android.server.trust.TrustManagerService.this.mIsInSignificantPlace = false;
                    }
                });
                this.mSignificantPlaceServiceWatcher.register();
                return;
            }
            return;
        }
        if (phase == 1000) {
            maybeEnableFactoryTrustAgents(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAutomotive() {
        return getContext().getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    private void scheduleTrustTimeout(boolean z, boolean z2) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "scheduleTrustTimeout(override=%s, isTrustable=%s)", java.lang.Boolean.valueOf(z), java.lang.Boolean.valueOf(z2));
        }
        this.mHandler.obtainMessage(15, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScheduleTrustTimeout(boolean shouldOverride, com.android.server.trust.TrustManagerService.TimeoutType timeoutType) {
        int userId = this.mCurrentUser;
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "handleScheduleTrustTimeout(shouldOverride=%s, timeoutType=%s)", java.lang.Boolean.valueOf(shouldOverride), timeoutType);
        }
        if (timeoutType == com.android.server.trust.TrustManagerService.TimeoutType.TRUSTABLE) {
            handleScheduleTrustableTimeouts(userId, shouldOverride, false);
        } else {
            handleScheduleTrustedTimeout(userId, shouldOverride);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshTrustableTimers(int userId) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "refreshTrustableTimers(userId=%s)", java.lang.Integer.valueOf(userId));
        }
        handleScheduleTrustableTimeouts(userId, true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelBothTrustableAlarms(int userId) {
        com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener idleTimeout = this.mIdleTrustableTimeoutAlarmListenerForUser.get(userId);
        com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener trustableTimeout = this.mTrustableTimeoutAlarmListenerForUser.get(userId);
        if (idleTimeout != null && idleTimeout.isQueued()) {
            idleTimeout.setQueued(false);
            this.mAlarmManager.cancel(idleTimeout);
        }
        if (trustableTimeout != null && trustableTimeout.isQueued()) {
            trustableTimeout.setQueued(false);
            this.mAlarmManager.cancel(trustableTimeout);
        }
    }

    private void handleScheduleTrustedTimeout(int userId, boolean shouldOverride) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "handleScheduleTrustedTimeout(userId=%s, shouldOverride=%s)", java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(shouldOverride));
        }
        long when = android.os.SystemClock.elapsedRealtime() + 14400000;
        com.android.server.trust.TrustManagerService.TrustedTimeoutAlarmListener alarm = this.mTrustTimeoutAlarmListenerForUser.get(java.lang.Integer.valueOf(userId));
        if (alarm != null) {
            if (!shouldOverride && alarm.isQueued()) {
                if (DEBUG) {
                    com.android.server.utils.Slogf.d(TAG, "Found existing trust timeout alarm. Skipping.");
                    return;
                }
                return;
            }
            this.mAlarmManager.cancel(alarm);
        } else {
            alarm = new com.android.server.trust.TrustManagerService.TrustedTimeoutAlarmListener(userId);
            this.mTrustTimeoutAlarmListenerForUser.put(java.lang.Integer.valueOf(userId), alarm);
        }
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "\tSetting up trust timeout alarm triggering at elapsedRealTime=%s", java.lang.Long.valueOf(when));
        }
        alarm.setQueued(true);
        this.mAlarmManager.setExact(2, when, TRUST_TIMEOUT_ALARM_TAG, alarm, this.mHandler);
    }

    private void handleScheduleTrustableTimeouts(int userId, boolean overrideIdleTimeout, boolean overrideHardTimeout) {
        setUpIdleTimeout(userId, overrideIdleTimeout);
        setUpHardTimeout(userId, overrideHardTimeout);
    }

    private void setUpIdleTimeout(int userId, boolean overrideIdleTimeout) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "setUpIdleTimeout(userId=%s, overrideIdleTimeout=%s)", java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(overrideIdleTimeout));
        }
        long when = android.os.SystemClock.elapsedRealtime() + TRUSTABLE_IDLE_TIMEOUT_IN_MILLIS;
        com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener alarm = this.mIdleTrustableTimeoutAlarmListenerForUser.get(userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.SCHEDULE_EXACT_ALARM", null);
        if (alarm != null) {
            if (!overrideIdleTimeout && alarm.isQueued()) {
                if (DEBUG) {
                    com.android.server.utils.Slogf.d(TAG, "Found existing trustable timeout alarm. Skipping.");
                    return;
                }
                return;
            }
            this.mAlarmManager.cancel(alarm);
        } else {
            alarm = new com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener(userId);
            this.mIdleTrustableTimeoutAlarmListenerForUser.put(userId, alarm);
        }
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "\tSetting up trustable idle timeout alarm triggering at elapsedRealTime=%s", java.lang.Long.valueOf(when));
        }
        alarm.setQueued(true);
        this.mAlarmManager.setExact(2, when, TRUST_TIMEOUT_ALARM_TAG, alarm, this.mHandler);
    }

    private void setUpHardTimeout(int userId, boolean overrideHardTimeout) {
        if (DEBUG) {
            com.android.server.utils.Slogf.i(TAG, "setUpHardTimeout(userId=%s, overrideHardTimeout=%s)", java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(overrideHardTimeout));
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.SCHEDULE_EXACT_ALARM", null);
        com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener alarm = this.mTrustableTimeoutAlarmListenerForUser.get(userId);
        if (alarm == null || !alarm.isQueued() || overrideHardTimeout) {
            long when = android.os.SystemClock.elapsedRealtime() + 86400000;
            if (alarm == null) {
                alarm = new com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener(userId);
                this.mTrustableTimeoutAlarmListenerForUser.put(userId, alarm);
            } else if (overrideHardTimeout) {
                this.mAlarmManager.cancel(alarm);
            }
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "\tSetting up trustable hard timeout alarm triggering at elapsedRealTime=%s", java.lang.Long.valueOf(when));
            }
            alarm.setQueued(true);
            this.mAlarmManager.setExact(2, when, TRUST_TIMEOUT_ALARM_TAG, alarm, this.mHandler);
        }
    }

    private static final class AgentInfo {
        com.android.server.trust.TrustAgentWrapper agent;
        android.content.ComponentName component;
        android.graphics.drawable.Drawable icon;
        java.lang.CharSequence label;
        com.android.server.trust.TrustManagerService.SettingsAttrs settings;
        int userId;

        private AgentInfo() {
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.trust.TrustManagerService.AgentInfo)) {
                return false;
            }
            com.android.server.trust.TrustManagerService.AgentInfo o = (com.android.server.trust.TrustManagerService.AgentInfo) other;
            return this.component.equals(o.component) && this.userId == o.userId;
        }

        public int hashCode() {
            return (this.component.hashCode() * 31) + this.userId;
        }

        public java.lang.String toString() {
            return java.lang.String.format("AgentInfo{label=%s, component=%s, userId=%s}", this.label, this.component, java.lang.Integer.valueOf(this.userId));
        }
    }

    private void updateTrustAll() {
        java.util.List<android.content.pm.UserInfo> userInfos = this.mUserManager.getAliveUsers();
        for (android.content.pm.UserInfo userInfo : userInfos) {
            updateTrust(userInfo.id, 0);
        }
    }

    public void updateTrust(int userId, int flags) {
        updateTrust(userId, flags, null);
    }

    public void updateTrust(int userId, int flags, com.android.internal.infra.AndroidFuture<android.service.trust.GrantTrustResult> resultCallback) {
        updateTrust(userId, flags, false, resultCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTrust(int userId, int flags, boolean isFromUnlock, com.android.internal.infra.AndroidFuture<android.service.trust.GrantTrustResult> resultCallback) throws java.lang.Throwable {
        boolean alreadyUnlocked;
        com.android.server.trust.TrustManagerService.TrustState pendingTrustState;
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "updateTrust(userId=%s, flags=%s, isFromUnlock=%s, resultCallbackPresent=%s)", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(flags), java.lang.Boolean.valueOf(isFromUnlock), java.lang.Boolean.valueOf(java.util.Objects.isNull(resultCallback)));
        }
        boolean managed = aggregateIsTrustManaged(userId);
        dispatchOnTrustManagedChanged(managed, userId);
        if (this.mStrongAuthTracker.isTrustAllowedForUser(userId) && isTrustUsuallyManagedInternal(userId) != managed) {
            updateTrustUsuallyManaged(userId, managed);
        }
        boolean trustedByAtLeastOneAgent = aggregateIsTrusted(userId);
        boolean trustableByAtLeastOneAgent = aggregateIsTrustable(userId);
        android.view.IWindowManager wm = android.view.WindowManagerGlobal.getWindowManagerService();
        try {
            boolean alreadyUnlocked2 = !wm.isKeyguardLocked();
            alreadyUnlocked = alreadyUnlocked2;
        } catch (android.os.RemoteException e) {
            alreadyUnlocked = false;
        }
        synchronized (this.mUserTrustState) {
            boolean wasTrusted = this.mUserTrustState.get(userId) == com.android.server.trust.TrustManagerService.TrustState.TRUSTED;
            boolean wasTrustable = this.mUserTrustState.get(userId) == com.android.server.trust.TrustManagerService.TrustState.TRUSTABLE;
            boolean renewingTrust = wasTrustable && (flags & 4) != 0;
            boolean canMoveToTrusted = alreadyUnlocked || isFromUnlock || renewingTrust || isAutomotive();
            boolean updatingTrustForCurrentUser = userId == this.mCurrentUser;
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "updateTrust: alreadyUnlocked=%s, wasTrusted=%s, wasTrustable=%s, renewingTrust=%s, canMoveToTrusted=%s, updatingTrustForCurrentUser=%s", java.lang.Boolean.valueOf(alreadyUnlocked), java.lang.Boolean.valueOf(wasTrusted), java.lang.Boolean.valueOf(wasTrustable), java.lang.Boolean.valueOf(renewingTrust), java.lang.Boolean.valueOf(canMoveToTrusted), java.lang.Boolean.valueOf(updatingTrustForCurrentUser));
            }
            if (trustedByAtLeastOneAgent && wasTrusted) {
                return;
            }
            if (trustedByAtLeastOneAgent && canMoveToTrusted && updatingTrustForCurrentUser) {
                pendingTrustState = com.android.server.trust.TrustManagerService.TrustState.TRUSTED;
            } else if (trustableByAtLeastOneAgent && ((wasTrusted || wasTrustable) && updatingTrustForCurrentUser)) {
                pendingTrustState = com.android.server.trust.TrustManagerService.TrustState.TRUSTABLE;
            } else {
                com.android.server.trust.TrustManagerService.TrustState pendingTrustState2 = com.android.server.trust.TrustManagerService.TrustState.UNTRUSTED;
                pendingTrustState = pendingTrustState2;
            }
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "updateTrust: pendingTrustState=%s", pendingTrustState);
            }
            this.mUserTrustState.put(userId, pendingTrustState);
            boolean isNowTrusted = pendingTrustState == com.android.server.trust.TrustManagerService.TrustState.TRUSTED;
            boolean newlyUnlocked = !alreadyUnlocked && isNowTrusted;
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "updateTrust: isNowTrusted=%s, newlyUnlocked=%s", java.lang.Boolean.valueOf(isNowTrusted), java.lang.Boolean.valueOf(newlyUnlocked));
            }
            maybeActiveUnlockRunningChanged(userId);
            dispatchOnTrustChanged(isNowTrusted, newlyUnlocked, userId, flags, getTrustGrantedMessages(userId));
            if (isNowTrusted != wasTrusted) {
                refreshDeviceLockedForUser(userId);
                if (isNowTrusted) {
                    boolean isTrustableTimeout = (flags & 4) != 0;
                    scheduleTrustTimeout(isTrustableTimeout, isTrustableTimeout);
                }
            }
            if (newlyUnlocked && resultCallback != null) {
                if (DEBUG) {
                    com.android.server.utils.Slogf.d(TAG, "calling back with UNLOCKED_BY_GRANT");
                }
                resultCallback.complete(new android.service.trust.GrantTrustResult(1));
            }
            if ((wasTrusted || wasTrustable) && pendingTrustState == com.android.server.trust.TrustManagerService.TrustState.UNTRUSTED) {
                if (DEBUG) {
                    com.android.server.utils.Slogf.d(TAG, "Trust was revoked, destroy trustable alarms");
                }
                cancelBothTrustableAlarms(userId);
            }
        }
    }

    private void updateTrustUsuallyManaged(int userId, boolean managed) {
        synchronized (this.mTrustUsuallyManagedForUser) {
            this.mTrustUsuallyManagedForUser.put(userId, managed);
        }
        this.mHandler.removeMessages(10);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(10), 120000L);
    }

    public long addEscrowToken(byte[] token, int userId) {
        return this.mLockPatternUtils.addEscrowToken(token, userId, new com.android.internal.widget.LockPatternUtils.EscrowTokenStateChangeCallback() { // from class: com.android.server.trust.TrustManagerService$$ExternalSyntheticLambda0
            public final void onEscrowTokenActivated(long j, int i) {
                this.f$0.lambda$addEscrowToken$0(j, i);
            }
        });
    }

    public boolean removeEscrowToken(long handle, int userId) {
        return this.mLockPatternUtils.removeEscrowToken(handle, userId);
    }

    public boolean isEscrowTokenActive(long handle, int userId) {
        return this.mLockPatternUtils.isEscrowTokenActive(handle, userId);
    }

    public void unlockUserWithToken(long handle, byte[] token, int userId) {
        this.mLockPatternUtils.unlockUserWithToken(handle, token, userId);
    }

    public void lockUser(int userId) {
        this.mLockPatternUtils.requireStrongAuth(256, userId);
        try {
            android.view.WindowManagerGlobal.getWindowManagerService().lockNow((android.os.Bundle) null);
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.e(TAG, "Error locking screen when called from trust agent");
        }
    }

    void showKeyguardErrorMessage(java.lang.CharSequence message) {
        dispatchOnTrustError(message);
    }

    void refreshAgentList(int userIdOrAll) {
        java.util.List<android.content.pm.UserInfo> userInfos;
        java.util.Iterator<android.content.pm.UserInfo> it;
        java.util.List<android.content.ComponentName> enabledAgents;
        int flag;
        java.util.List<android.os.PersistableBundle> config;
        int userIdOrAll2 = userIdOrAll;
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "refreshAgentList(userIdOrAll=%s)", java.lang.Integer.valueOf(userIdOrAll));
        }
        if (!this.mTrustAgentsCanRun) {
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "Did not refresh agent list because agents cannot run.");
                return;
            }
            return;
        }
        if (userIdOrAll2 != -1 && userIdOrAll2 < 0) {
            android.util.Log.e(TAG, "refreshAgentList(userId=" + userIdOrAll2 + "): Invalid user handle, must be USER_ALL or a specific user.", new java.lang.Throwable("here"));
            userIdOrAll2 = -1;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (userIdOrAll2 == -1) {
            userInfos = this.mUserManager.getAliveUsers();
        } else {
            userInfos = new java.util.ArrayList<>();
            userInfos.add(this.mUserManager.getUserInfo(userIdOrAll2));
        }
        com.android.internal.widget.LockPatternUtils lockPatternUtils = this.mLockPatternUtils;
        android.util.ArraySet<com.android.server.trust.TrustManagerService.AgentInfo> obsoleteAgents = new android.util.ArraySet<>();
        obsoleteAgents.addAll(this.mActiveAgents);
        java.util.Iterator<android.content.pm.UserInfo> it2 = userInfos.iterator();
        while (it2.hasNext()) {
            android.content.pm.UserInfo userInfo = it2.next();
            if (userInfo != null && !userInfo.partial && userInfo.isEnabled()) {
                if (!userInfo.guestToRemove) {
                    if (!userInfo.supportsSwitchToByUser()) {
                        if (DEBUG) {
                            com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping user %s: switchToByUser=false", java.lang.Integer.valueOf(userInfo.id));
                        }
                    } else if (!this.mActivityManager.isUserRunning(userInfo.id)) {
                        if (DEBUG) {
                            com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping user %s: user not started", java.lang.Integer.valueOf(userInfo.id));
                        }
                    } else if (!lockPatternUtils.isSecure(userInfo.id)) {
                        if (DEBUG) {
                            com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping user %s: no secure credential", java.lang.Integer.valueOf(userInfo.id));
                        }
                    } else {
                        android.app.admin.DevicePolicyManager dpm = lockPatternUtils.getDevicePolicyManager();
                        int disabledFeatures = dpm.getKeyguardDisabledFeatures(null, userInfo.id);
                        boolean disableTrustAgents = (disabledFeatures & 16) != 0;
                        java.util.List<android.content.ComponentName> enabledAgents2 = lockPatternUtils.getEnabledTrustAgents(userInfo.id);
                        if (!enabledAgents2.isEmpty()) {
                            java.util.List<android.content.pm.ResolveInfo> resolveInfos = resolveAllowedTrustAgents(pm, userInfo.id);
                            for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
                                android.content.ComponentName name = getComponentName(resolveInfo);
                                if (!enabledAgents2.contains(name)) {
                                    if (DEBUG) {
                                        com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping %s u%s: not enabled by user", name.flattenToShortString(), java.lang.Integer.valueOf(userInfo.id));
                                        resolveInfos = resolveInfos;
                                        lockPatternUtils = lockPatternUtils;
                                    }
                                } else {
                                    java.util.List<android.content.pm.ResolveInfo> resolveInfos2 = resolveInfos;
                                    com.android.internal.widget.LockPatternUtils lockPatternUtils2 = lockPatternUtils;
                                    if (disableTrustAgents && ((config = dpm.getTrustAgentConfiguration(null, name, userInfo.id)) == null || config.isEmpty())) {
                                        if (DEBUG) {
                                            com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping %s u%s: not allowed by DPM", name.flattenToShortString(), java.lang.Integer.valueOf(userInfo.id));
                                            resolveInfos = resolveInfos2;
                                            lockPatternUtils = lockPatternUtils2;
                                        } else {
                                            resolveInfos = resolveInfos2;
                                            lockPatternUtils = lockPatternUtils2;
                                        }
                                    } else {
                                        com.android.server.trust.TrustManagerService.AgentInfo agentInfo = new com.android.server.trust.TrustManagerService.AgentInfo();
                                        agentInfo.component = name;
                                        agentInfo.userId = userInfo.id;
                                        if (!this.mActiveAgents.contains(agentInfo)) {
                                            agentInfo.label = resolveInfo.loadLabel(pm);
                                            agentInfo.icon = resolveInfo.loadIcon(pm);
                                            agentInfo.settings = getSettingsAttrs(pm, resolveInfo);
                                        } else {
                                            int index = this.mActiveAgents.indexOf(agentInfo);
                                            agentInfo = this.mActiveAgents.valueAt(index);
                                        }
                                        int index2 = 0;
                                        android.content.pm.PackageManager pm2 = pm;
                                        if (agentInfo.settings != null) {
                                            index2 = (resolveInfo.serviceInfo.directBootAware && agentInfo.settings.canUnlockProfile) ? 1 : 0;
                                        }
                                        if (index2 == 0 || !DEBUG) {
                                            it = it2;
                                        } else {
                                            it = it2;
                                            com.android.server.utils.Slogf.d(TAG, "refreshAgentList: trustagent %s of user %s can unlock user profile.", name, java.lang.Integer.valueOf(userInfo.id));
                                        }
                                        if (!this.mUserManager.isUserUnlockingOrUnlocked(userInfo.id) && index2 == 0) {
                                            if (!DEBUG) {
                                                resolveInfos = resolveInfos2;
                                                lockPatternUtils = lockPatternUtils2;
                                                pm = pm2;
                                                it2 = it;
                                            } else {
                                                com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping user %s's trust agent %s: FBE still locked and the agent cannot unlock user profile.", java.lang.Integer.valueOf(userInfo.id), name);
                                                resolveInfos = resolveInfos2;
                                                lockPatternUtils = lockPatternUtils2;
                                                pm = pm2;
                                                it2 = it;
                                            }
                                        } else {
                                            if (!this.mStrongAuthTracker.canAgentsRunForUser(userInfo.id) && (flag = this.mStrongAuthTracker.getStrongAuthForUser(userInfo.id)) != 8 && (flag != 1 || index2 == 0)) {
                                                if (DEBUG) {
                                                    com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping user %s: prevented by StrongAuthTracker = 0x%s", java.lang.Integer.valueOf(userInfo.id), java.lang.Integer.toHexString(this.mStrongAuthTracker.getStrongAuthForUser(userInfo.id)));
                                                    resolveInfos = resolveInfos2;
                                                    lockPatternUtils = lockPatternUtils2;
                                                    pm = pm2;
                                                    it2 = it;
                                                } else {
                                                    resolveInfos = resolveInfos2;
                                                    lockPatternUtils = lockPatternUtils2;
                                                    pm = pm2;
                                                    it2 = it;
                                                }
                                            }
                                            if (agentInfo.agent == null) {
                                                enabledAgents = enabledAgents2;
                                                agentInfo.agent = new com.android.server.trust.TrustAgentWrapper(this.mContext, this, new android.content.Intent().setComponent(name), userInfo.getUserHandle());
                                            } else {
                                                enabledAgents = enabledAgents2;
                                            }
                                            if (!this.mActiveAgents.contains(agentInfo)) {
                                                this.mActiveAgents.add(agentInfo);
                                            } else {
                                                obsoleteAgents.remove(agentInfo);
                                            }
                                            resolveInfos = resolveInfos2;
                                            lockPatternUtils = lockPatternUtils2;
                                            pm = pm2;
                                            it2 = it;
                                            enabledAgents2 = enabledAgents;
                                        }
                                    }
                                }
                            }
                        } else if (DEBUG) {
                            com.android.server.utils.Slogf.d(TAG, "refreshAgentList: skipping user %s: no agents enabled by user", java.lang.Integer.valueOf(userInfo.id));
                        }
                    }
                }
            }
        }
        boolean trustMayHaveChanged = false;
        for (int i = 0; i < obsoleteAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = obsoleteAgents.valueAt(i);
            if (userIdOrAll2 == -1 || userIdOrAll2 == info.userId) {
                if (info.agent.isManagingTrust()) {
                    trustMayHaveChanged = true;
                }
                info.agent.destroy();
                this.mActiveAgents.remove(info);
            }
        }
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "refreshAgentList: userInfos=%s, obsoleteAgents=%s, trustMayHaveChanged=%s", userInfos, obsoleteAgents, java.lang.Boolean.valueOf(trustMayHaveChanged));
        }
        if (trustMayHaveChanged) {
            if (userIdOrAll2 != -1) {
                updateTrust(userIdOrAll2, 0);
            } else {
                updateTrustAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.trust.TrustManagerService.TrustState getUserTrustStateInner(int userId) {
        com.android.server.trust.TrustManagerService.TrustState trustState;
        synchronized (this.mUserTrustState) {
            trustState = this.mUserTrustState.get(userId, com.android.server.trust.TrustManagerService.TrustState.UNTRUSTED);
        }
        return trustState;
    }

    boolean isDeviceLockedInner(int userId) {
        boolean z;
        synchronized (this.mDeviceLockedForUser) {
            z = this.mDeviceLockedForUser.get(userId, true);
        }
        return z;
    }

    private void maybeActiveUnlockRunningChanged(int userId) {
        boolean oldValue = this.mLastActiveUnlockRunningState.get(userId);
        boolean newValue = aggregateIsActiveUnlockRunning(userId);
        if (oldValue == newValue) {
            return;
        }
        this.mLastActiveUnlockRunningState.put(userId, newValue);
        for (int i = 0; i < this.mTrustListeners.size(); i++) {
            notifyListenerIsActiveUnlockRunning(this.mTrustListeners.get(i), newValue, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshDeviceLockedForUser(int userId) throws java.lang.Throwable {
        refreshDeviceLockedForUser(userId, -10000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshDeviceLockedForUser(int userId, int unlockedUser) throws java.lang.Throwable {
        int userId2;
        java.util.List<android.content.pm.UserInfo> userInfos;
        boolean trusted;
        if (userId != -1 && userId < 0) {
            android.util.Log.e(TAG, "refreshDeviceLockedForUser(userId=" + userId + "): Invalid user handle, must be USER_ALL or a specific user.", new java.lang.Throwable("here"));
            userId2 = -1;
        } else {
            userId2 = userId;
        }
        if (userId2 == -1) {
            userInfos = this.mUserManager.getAliveUsers();
        } else {
            java.util.List<android.content.pm.UserInfo> userInfos2 = new java.util.ArrayList<>();
            userInfos2.add(this.mUserManager.getUserInfo(userId2));
            userInfos = userInfos2;
        }
        android.view.IWindowManager wm = android.view.WindowManagerGlobal.getWindowManagerService();
        for (int i = 0; i < userInfos.size(); i++) {
            android.content.pm.UserInfo info = userInfos.get(i);
            if (info != null && !info.partial && info.isEnabled()) {
                if (!info.guestToRemove) {
                    int id = info.id;
                    boolean secure = this.mLockPatternUtils.isSecure(id);
                    if (!info.supportsSwitchToByUser()) {
                        if (info.isProfile() && !secure) {
                            if (!this.mLockPatternUtils.isProfileWithUnifiedChallenge(id)) {
                                setDeviceLockedForUser(id, false);
                            }
                        }
                    } else {
                        if (android.security.Flags.fixUnlockedDeviceRequiredKeysV2()) {
                            trusted = getUserTrustStateInner(id) == com.android.server.trust.TrustManagerService.TrustState.TRUSTED;
                        } else {
                            boolean trusted2 = aggregateIsTrusted(id);
                            trusted = trusted2;
                        }
                        boolean showingKeyguard = true;
                        boolean biometricAuthenticated = false;
                        boolean currentUserIsUnlocked = false;
                        if (this.mCurrentUser == id) {
                            synchronized (this.mUsersUnlockedByBiometric) {
                                try {
                                    biometricAuthenticated = this.mUsersUnlockedByBiometric.get(id, false);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    while (true) {
                                        try {
                                            throw th;
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                        }
                                    }
                                }
                            }
                            try {
                                showingKeyguard = wm.isKeyguardLocked();
                            } catch (android.os.RemoteException e) {
                                android.util.Log.w(TAG, "Unable to check keyguard lock state", e);
                            }
                            currentUserIsUnlocked = unlockedUser == id;
                        }
                        boolean deviceLocked = secure && showingKeyguard && !trusted && !biometricAuthenticated;
                        if (!deviceLocked || !currentUserIsUnlocked) {
                            setDeviceLockedForUser(id, deviceLocked);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDeviceLockedForUser(int userId, boolean locked) {
        int i;
        boolean changed;
        synchronized (this.mDeviceLockedForUser) {
            changed = isDeviceLockedInner(userId) != locked;
            this.mDeviceLockedForUser.put(userId, locked);
        }
        if (changed) {
            notifyTrustAgentsOfDeviceLockState(userId, locked);
            notifyKeystoreOfDeviceLockState(userId, locked);
            for (int profileHandle : this.mUserManager.getEnabledProfileIds(userId)) {
                if (this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(profileHandle)) {
                    notifyKeystoreOfDeviceLockState(profileHandle, locked);
                }
            }
        }
    }

    private void notifyTrustAgentsOfDeviceLockState(int userId, boolean isLocked) {
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo agent = this.mActiveAgents.valueAt(i);
            if (agent.userId == userId) {
                if (isLocked) {
                    agent.agent.onDeviceLocked();
                } else {
                    agent.agent.onDeviceUnlocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyKeystoreOfDeviceLockState(int userId, boolean isLocked) {
        if (isLocked) {
            if (android.security.Flags.fixUnlockedDeviceRequiredKeysV2()) {
                int authUserId = this.mLockPatternUtils.isProfileWithUnifiedChallenge(userId) ? resolveProfileParent(userId) : userId;
                this.mKeyStoreAuthorization.onDeviceLocked(userId, getBiometricSids(authUserId), isWeakUnlockMethodEnabled(authUserId));
                return;
            } else {
                this.mKeyStoreAuthorization.onDeviceLocked(userId, getBiometricSids(userId), false);
                return;
            }
        }
        this.mKeyStoreAuthorization.onDeviceUnlocked(userId, (byte[]) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dispatchEscrowTokenActivatedLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$addEscrowToken$0(long handle, int userId) {
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo agent = this.mActiveAgents.valueAt(i);
            if (agent.userId == userId) {
                agent.agent.onEscrowTokenActivated(handle, userId);
            }
        }
    }

    void updateDevicePolicyFeatures() {
        boolean changed = false;
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.agent.isConnected()) {
                info.agent.updateDevicePolicyFeatures();
                changed = true;
            }
        }
        if (changed) {
            this.mArchive.logDevicePolicyChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAgentsOfPackage(java.lang.String packageName) {
        boolean trustMayHaveChanged = false;
        for (int i = this.mActiveAgents.size() - 1; i >= 0; i--) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (packageName.equals(info.component.getPackageName())) {
                android.util.Log.i(TAG, "Resetting agent " + info.component.flattenToShortString());
                if (info.agent.isManagingTrust()) {
                    trustMayHaveChanged = true;
                }
                info.agent.destroy();
                this.mActiveAgents.removeAt(i);
            }
        }
        if (trustMayHaveChanged) {
            updateTrustAll();
        }
    }

    public void resetAgent(android.content.ComponentName name, int userId) {
        boolean trustMayHaveChanged = false;
        for (int i = this.mActiveAgents.size() - 1; i >= 0; i--) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (name.equals(info.component) && userId == info.userId) {
                android.util.Log.i(TAG, "Resetting agent " + info.component.flattenToShortString());
                if (info.agent.isManagingTrust()) {
                    trustMayHaveChanged = true;
                }
                info.agent.destroy();
                this.mActiveAgents.removeAt(i);
            }
        }
        if (trustMayHaveChanged) {
            updateTrust(userId, 0);
        }
        refreshAgentList(userId);
    }

    private com.android.server.trust.TrustManagerService.SettingsAttrs getSettingsAttrs(android.content.pm.PackageManager pm, android.content.pm.ResolveInfo resolveInfo) {
        int type;
        if (resolveInfo == null || resolveInfo.serviceInfo == null || resolveInfo.serviceInfo.metaData == null) {
            return null;
        }
        java.lang.String cn = null;
        boolean canUnlockProfile = false;
        android.content.res.XmlResourceParser parser = null;
        java.lang.Exception caughtException = null;
        try {
            parser = resolveInfo.serviceInfo.loadXmlMetaData(pm, "android.service.trust.trustagent");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            caughtException = e;
            if (0 != 0) {
            }
        } catch (java.io.IOException e2) {
            caughtException = e2;
            if (0 != 0) {
            }
        } catch (org.xmlpull.v1.XmlPullParserException e3) {
            caughtException = e3;
            if (0 != 0) {
            }
        } catch (java.lang.Throwable th) {
            if (0 != 0) {
                parser.close();
            }
            throw th;
        }
        if (parser == null) {
            com.android.server.utils.Slogf.w(TAG, "Can't find %s meta-data", "android.service.trust.trustagent");
            if (parser != null) {
                parser.close();
            }
            return null;
        }
        android.content.res.Resources res = pm.getResourcesForApplication(resolveInfo.serviceInfo.applicationInfo);
        android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
        do {
            type = parser.next();
            if (type == 1) {
                break;
            }
        } while (type != 2);
        java.lang.String nodeName = parser.getName();
        if (!"trust-agent".equals(nodeName)) {
            com.android.server.utils.Slogf.w(TAG, "Meta-data does not start with trust-agent tag");
            if (parser != null) {
                parser.close();
            }
            return null;
        }
        android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.TrustAgent);
        cn = sa.getString(2);
        canUnlockProfile = attrs.getAttributeBooleanValue(PRIV_NAMESPACE, "unlockProfile", false);
        sa.recycle();
        if (parser != null) {
            parser.close();
        }
        if (caughtException != null) {
            com.android.server.utils.Slogf.w(TAG, caughtException, "Error parsing : %s", resolveInfo.serviceInfo.packageName);
            return null;
        }
        if (cn == null) {
            return null;
        }
        if (cn.indexOf(47) < 0) {
            cn = resolveInfo.serviceInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + cn;
        }
        return new com.android.server.trust.TrustManagerService.SettingsAttrs(android.content.ComponentName.unflattenFromString(cn), canUnlockProfile);
    }

    private android.content.ComponentName getComponentName(android.content.pm.ResolveInfo resolveInfo) {
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            return null;
        }
        return new android.content.ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeEnableFactoryTrustAgents(int userId) {
        if (android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "trust_agents_initialized", 0, userId) != 0) {
            return;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = resolveAllowedTrustAgents(pm, userId);
        android.content.ComponentName defaultAgent = getDefaultFactoryTrustAgent(this.mContext);
        boolean shouldUseDefaultAgent = defaultAgent != null;
        android.util.ArraySet<android.content.ComponentName> discoveredAgents = new android.util.ArraySet<>();
        if (shouldUseDefaultAgent) {
            discoveredAgents.add(defaultAgent);
            android.util.Log.i(TAG, "Enabling " + defaultAgent + " because it is a default agent.");
        } else {
            for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
                android.content.ComponentName componentName = getComponentName(resolveInfo);
                if (!isSystemTrustAgent(resolveInfo)) {
                    android.util.Log.i(TAG, "Leaving agent " + componentName + " disabled because package is not a system package.");
                } else {
                    discoveredAgents.add(componentName);
                }
            }
        }
        enableNewAgents(discoveredAgents, userId);
        android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "trust_agents_initialized", 1, userId);
    }

    private void checkNewAgents() {
        for (android.content.pm.UserInfo userInfo : this.mUserManager.getAliveUsers()) {
            checkNewAgentsForUser(userInfo.id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkNewAgentsForUser(int userId) {
        if (android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "known_trust_agents_initialized", 0, userId) == 0) {
            initializeKnownAgents(userId);
            return;
        }
        java.util.List<android.content.ComponentName> knownAgents = this.mLockPatternUtils.getKnownTrustAgents(userId);
        java.util.List<android.content.pm.ResolveInfo> agentInfoList = resolveAllowedTrustAgents(this.mContext.getPackageManager(), userId);
        android.util.ArraySet<? extends android.content.ComponentName> newAgents = new android.util.ArraySet<>(agentInfoList.size());
        android.util.ArraySet<android.content.ComponentName> newSystemAgents = new android.util.ArraySet<>(agentInfoList.size());
        for (android.content.pm.ResolveInfo agentInfo : agentInfoList) {
            android.content.ComponentName agentComponentName = getComponentName(agentInfo);
            if (!knownAgents.contains(agentComponentName)) {
                newAgents.add(agentComponentName);
                if (isSystemTrustAgent(agentInfo)) {
                    newSystemAgents.add(agentComponentName);
                }
            }
        }
        if (newAgents.isEmpty()) {
            return;
        }
        android.util.ArraySet<android.content.ComponentName> updatedKnowAgents = new android.util.ArraySet<>(knownAgents);
        updatedKnowAgents.addAll(newAgents);
        this.mLockPatternUtils.setKnownTrustAgents(updatedKnowAgents, userId);
        boolean hasDefaultAgent = getDefaultFactoryTrustAgent(this.mContext) != null;
        if (!hasDefaultAgent) {
            enableNewAgents(newSystemAgents, userId);
        }
    }

    private void enableNewAgents(java.util.Collection<android.content.ComponentName> agents, int userId) {
        if (agents.isEmpty()) {
            return;
        }
        android.util.ArraySet<android.content.ComponentName> agentsToEnable = new android.util.ArraySet<>(agents);
        agentsToEnable.addAll(this.mLockPatternUtils.getEnabledTrustAgents(userId));
        this.mLockPatternUtils.setEnabledTrustAgents(agentsToEnable, userId);
    }

    private void initializeKnownAgents(int userId) {
        java.util.List<android.content.pm.ResolveInfo> agentInfoList = resolveAllowedTrustAgents(this.mContext.getPackageManager(), userId);
        android.util.ArraySet<android.content.ComponentName> agentComponentNames = new android.util.ArraySet<>(agentInfoList.size());
        for (android.content.pm.ResolveInfo agentInfo : agentInfoList) {
            agentComponentNames.add(getComponentName(agentInfo));
        }
        this.mLockPatternUtils.setKnownTrustAgents(agentComponentNames, userId);
        android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "known_trust_agents_initialized", 1, userId);
    }

    private static android.content.ComponentName getDefaultFactoryTrustAgent(android.content.Context context) {
        java.lang.String defaultTrustAgent = context.getResources().getString(android.R.string.config_deviceSpecificDisplayAreaPolicyProvider);
        if (android.text.TextUtils.isEmpty(defaultTrustAgent)) {
            return null;
        }
        return android.content.ComponentName.unflattenFromString(defaultTrustAgent);
    }

    private java.util.List<android.content.pm.ResolveInfo> resolveAllowedTrustAgents(android.content.pm.PackageManager pm, int userId) {
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = pm.queryIntentServicesAsUser(TRUST_AGENT_INTENT, 786560, userId);
        java.util.ArrayList<android.content.pm.ResolveInfo> allowedAgents = new java.util.ArrayList<>(resolveInfos.size());
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
            if (resolveInfo.serviceInfo != null && resolveInfo.serviceInfo.applicationInfo != null) {
                java.lang.String packageName = resolveInfo.serviceInfo.packageName;
                if (pm.checkPermission(PERMISSION_PROVIDE_AGENT, packageName) != 0) {
                    android.content.ComponentName name = getComponentName(resolveInfo);
                    android.util.Log.w(TAG, "Skipping agent " + name + " because package does not have permission " + PERMISSION_PROVIDE_AGENT + ".");
                } else {
                    allowedAgents.add(resolveInfo);
                }
            }
        }
        return allowedAgents;
    }

    private static boolean isSystemTrustAgent(android.content.pm.ResolveInfo agentInfo) {
        return (agentInfo.serviceInfo.applicationInfo.flags & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean aggregateIsTrusted(int userId) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "aggregateIsTrusted(userId=%s)", java.lang.Integer.valueOf(userId));
        }
        if (!this.mStrongAuthTracker.isTrustAllowedForUser(userId)) {
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "not trusted because trust not allowed for userId=%s", java.lang.Integer.valueOf(userId));
            }
            return false;
        }
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId && info.agent.isTrusted()) {
                if (DEBUG) {
                    com.android.server.utils.Slogf.d(TAG, "trusted by %s", info);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    private boolean aggregateIsTrustable(int userId) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "aggregateIsTrustable(userId=%s)", java.lang.Integer.valueOf(userId));
        }
        if (!this.mStrongAuthTracker.isTrustAllowedForUser(userId)) {
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "not trustable because trust not allowed for userId=%s", java.lang.Integer.valueOf(userId));
            }
            return false;
        }
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId && info.agent.isTrustable()) {
                if (DEBUG) {
                    com.android.server.utils.Slogf.d(TAG, "trustable by %s", info);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean aggregateIsActiveUnlockRunning(int userId) {
        if (!this.mStrongAuthTracker.isTrustAllowedForUser(userId)) {
            return false;
        }
        synchronized (this.mUserTrustState) {
            com.android.server.trust.TrustManagerService.TrustState currentState = this.mUserTrustState.get(userId);
            if (currentState != com.android.server.trust.TrustManagerService.TrustState.TRUSTED && currentState != com.android.server.trust.TrustManagerService.TrustState.TRUSTABLE) {
                return false;
            }
            for (int i = 0; i < this.mActiveAgents.size(); i++) {
                com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
                if (info.userId == userId && info.agent.isTrustableOrWaitingForDowngrade()) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchTrustableDowngrade() {
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == this.mCurrentUser) {
                info.agent.downgradeToTrustable();
            }
        }
    }

    private java.util.List<java.lang.String> getTrustGrantedMessages(int userId) {
        if (!this.mStrongAuthTracker.isTrustAllowedForUser(userId)) {
            return new java.util.ArrayList();
        }
        java.util.List<java.lang.String> trustGrantedMessages = new java.util.ArrayList<>();
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId && info.agent.isTrusted() && info.agent.shouldDisplayTrustGrantedMessage() && info.agent.getMessage() != null) {
                trustGrantedMessages.add(info.agent.getMessage().toString());
            }
        }
        return trustGrantedMessages;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean aggregateIsTrustManaged(int userId) {
        if (!this.mStrongAuthTracker.isTrustAllowedForUser(userId)) {
            if (DEBUG) {
                com.android.server.utils.Slogf.d(TAG, "trust not managed due to trust not being allowed for userId=%s", java.lang.Integer.valueOf(userId));
            }
            return false;
        }
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId && info.agent.isManagingTrust()) {
                if (DEBUG) {
                    com.android.server.utils.Slogf.d(TAG, "trust managed for userId=%s", java.lang.Integer.valueOf(userId));
                    return true;
                }
                return true;
            }
        }
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "trust not managed for userId=%s", java.lang.Integer.valueOf(userId));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUnlockAttempt(boolean successful, int userId) throws java.lang.Throwable {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "dispatchUnlockAttempt(successful=%s, userId=%s)", java.lang.Boolean.valueOf(successful), java.lang.Integer.valueOf(userId));
        }
        if (successful) {
            this.mStrongAuthTracker.allowTrustFromUnlock(userId);
            updateTrust(userId, 0, true, null);
            this.mHandler.obtainMessage(17, java.lang.Integer.valueOf(userId)).sendToTarget();
        }
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId) {
                info.agent.onUnlockAttempt(successful);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUserRequestedUnlock(int userId, boolean dismissKeyguard) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "dispatchUserRequestedUnlock(user=%s, dismissKeyguard=%s)", java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(dismissKeyguard));
        }
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId) {
                info.agent.onUserRequestedUnlock(dismissKeyguard);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUserMayRequestUnlock(int userId) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "dispatchUserMayRequestUnlock(user=%s)", java.lang.Integer.valueOf(userId));
        }
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId) {
                info.agent.onUserMayRequestUnlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUnlockLockout(int timeoutMs, int userId) {
        for (int i = 0; i < this.mActiveAgents.size(); i++) {
            com.android.server.trust.TrustManagerService.AgentInfo info = this.mActiveAgents.valueAt(i);
            if (info.userId == userId) {
                info.agent.onUnlockLockout(timeoutMs);
            }
        }
    }

    private void notifyListenerIsActiveUnlockRunningInitialState(android.app.trust.ITrustListener listener) {
        int numUsers = this.mLastActiveUnlockRunningState.size();
        for (int i = 0; i < numUsers; i++) {
            int userId = this.mLastActiveUnlockRunningState.keyAt(i);
            boolean isRunning = aggregateIsActiveUnlockRunning(userId);
            notifyListenerIsActiveUnlockRunning(listener, isRunning, userId);
        }
    }

    private void notifyListenerIsActiveUnlockRunning(android.app.trust.ITrustListener listener, boolean isRunning, int userId) {
        try {
            listener.onIsActiveUnlockRunningChanged(isRunning, userId);
        } catch (android.os.DeadObjectException e) {
            com.android.server.utils.Slogf.d(TAG, "TrustListener dead while trying to notify Active Unlock running state");
        } catch (android.os.RemoteException e2) {
            com.android.server.utils.Slogf.e(TAG, "Exception while notifying TrustListener.", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addListener(android.app.trust.ITrustListener listener) {
        for (int i = 0; i < this.mTrustListeners.size(); i++) {
            if (this.mTrustListeners.get(i).asBinder() == listener.asBinder()) {
                return;
            }
        }
        this.mTrustListeners.add(listener);
        notifyListenerIsActiveUnlockRunningInitialState(listener);
        updateTrustAll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeListener(android.app.trust.ITrustListener listener) {
        for (int i = 0; i < this.mTrustListeners.size(); i++) {
            if (this.mTrustListeners.get(i).asBinder() == listener.asBinder()) {
                this.mTrustListeners.remove(i);
                return;
            }
        }
    }

    private void dispatchOnTrustChanged(boolean enabled, boolean newlyUnlocked, int userId, int flags, java.util.List<java.lang.String> trustGrantedMessages) {
        if (DEBUG) {
            android.util.Log.i(TAG, "onTrustChanged(" + enabled + ", " + newlyUnlocked + ", " + userId + ", 0x" + java.lang.Integer.toHexString(flags) + ")");
        }
        if (!enabled) {
            flags = 0;
        }
        int i = 0;
        while (i < this.mTrustListeners.size()) {
            try {
                this.mTrustListeners.get(i).onTrustChanged(enabled, newlyUnlocked, userId, flags, trustGrantedMessages);
            } catch (android.os.DeadObjectException e) {
                com.android.server.utils.Slogf.d(TAG, "Removing dead TrustListener.");
                this.mTrustListeners.remove(i);
                i--;
            } catch (android.os.RemoteException e2) {
                com.android.server.utils.Slogf.e(TAG, "Exception while notifying TrustListener.", e2);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOnEnabledTrustAgentsChanged(int userId) {
        if (DEBUG) {
            android.util.Log.i(TAG, "onEnabledTrustAgentsChanged(" + userId + ")");
        }
        int i = 0;
        while (i < this.mTrustListeners.size()) {
            try {
                this.mTrustListeners.get(i).onEnabledTrustAgentsChanged(userId);
            } catch (android.os.DeadObjectException e) {
                com.android.server.utils.Slogf.d(TAG, "Removing dead TrustListener.");
                this.mTrustListeners.remove(i);
                i--;
            } catch (android.os.RemoteException e2) {
                com.android.server.utils.Slogf.e(TAG, "Exception while notifying TrustListener.", e2);
            }
            i++;
        }
    }

    private void dispatchOnTrustManagedChanged(boolean managed, int userId) {
        if (DEBUG) {
            android.util.Log.i(TAG, "onTrustManagedChanged(" + managed + ", " + userId + ")");
        }
        int i = 0;
        while (i < this.mTrustListeners.size()) {
            try {
                this.mTrustListeners.get(i).onTrustManagedChanged(managed, userId);
            } catch (android.os.DeadObjectException e) {
                com.android.server.utils.Slogf.d(TAG, "Removing dead TrustListener.");
                this.mTrustListeners.remove(i);
                i--;
            } catch (android.os.RemoteException e2) {
                com.android.server.utils.Slogf.e(TAG, "Exception while notifying TrustListener.", e2);
            }
            i++;
        }
    }

    private void dispatchOnTrustError(java.lang.CharSequence message) {
        if (DEBUG) {
            android.util.Log.i(TAG, "onTrustError(" + ((java.lang.Object) message) + ")");
        }
        int i = 0;
        while (i < this.mTrustListeners.size()) {
            try {
                this.mTrustListeners.get(i).onTrustError(message);
            } catch (android.os.DeadObjectException e) {
                com.android.server.utils.Slogf.d(TAG, "Removing dead TrustListener.");
                this.mTrustListeners.remove(i);
                i--;
            } catch (android.os.RemoteException e2) {
                com.android.server.utils.Slogf.e(TAG, "Exception while notifying TrustListener.", e2);
            }
            i++;
        }
    }

    private long[] getBiometricSids(int userId) {
        android.hardware.biometrics.BiometricManager biometricManager = (android.hardware.biometrics.BiometricManager) this.mContext.getSystemService(android.hardware.biometrics.BiometricManager.class);
        if (biometricManager == null) {
            return new long[0];
        }
        return biometricManager.getAuthenticatorIds(userId);
    }

    private boolean isWeakUnlockMethodEnabled(int userId) {
        if (this.mStrongAuthTracker.isBiometricAllowedForUser(false, userId)) {
            android.app.admin.DevicePolicyManager dpm = this.mLockPatternUtils.getDevicePolicyManager();
            int disabledFeatures = dpm.getKeyguardDisabledFeatures(null, userId);
            if (this.mFingerprintManager != null && (disabledFeatures & 32) == 0 && this.mFingerprintManager.hasEnrolledTemplates(userId) && isWeakOrConvenienceSensor((android.hardware.biometrics.SensorProperties) this.mFingerprintManager.getSensorProperties().get(0))) {
                com.android.server.utils.Slogf.i(TAG, "User is unlockable by non-strong fingerprint auth");
                return true;
            }
            if (this.mFaceManager != null && (disabledFeatures & 128) == 0 && this.mFaceManager.hasEnrolledTemplates(userId) && isWeakOrConvenienceSensor((android.hardware.biometrics.SensorProperties) this.mFaceManager.getSensorProperties().get(0))) {
                com.android.server.utils.Slogf.i(TAG, "User is unlockable by non-strong face auth");
                return true;
            }
        }
        if (getUserTrustStateInner(userId) != com.android.server.trust.TrustManagerService.TrustState.TRUSTABLE && (!isAutomotive() || !isTrustUsuallyManagedInternal(userId))) {
            return false;
        }
        com.android.server.utils.Slogf.i(TAG, "User is unlockable by trust agent");
        return true;
    }

    private static boolean isWeakOrConvenienceSensor(android.hardware.biometrics.SensorProperties sensor) {
        return sensor.getSensorStrength() == 1 || sensor.getSensorStrength() == 0;
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        this.mHandler.obtainMessage(7, user.getUserIdentifier(), 0, null).sendToTarget();
    }

    @Override // com.android.server.SystemService
    public void onUserStopped(com.android.server.SystemService.TargetUser user) {
        this.mHandler.obtainMessage(8, user.getUserIdentifier(), 0, null).sendToTarget();
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        this.mHandler.obtainMessage(9, to.getUserIdentifier(), 0, null).sendToTarget();
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        this.mHandler.obtainMessage(11, user.getUserIdentifier(), 0, null).sendToTarget();
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        this.mHandler.obtainMessage(12, user.getUserIdentifier(), 0, null).sendToTarget();
    }

    /* JADX INFO: renamed from: com.android.server.trust.TrustManagerService$2, reason: invalid class name */
    class AnonymousClass2 extends android.app.trust.ITrustManager.Stub {
        private final com.android.server.ServiceThread mServiceSubThread = new com.android.server.ServiceThread("TrustManager", -2, true);
        private final android.os.Handler mSubHandler;

        AnonymousClass2() {
            this.mServiceSubThread.start();
            this.mSubHandler = new android.os.Handler(this.mServiceSubThread.getLooper()) { // from class: com.android.server.trust.TrustManagerService.2.2
                @Override // android.os.Handler
                public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
                    switch (msg.what) {
                        case 6:
                            android.util.Log.d(com.android.server.trust.TrustManagerService.TAG, "MSG_KEYGUARD_SHOWING_CHANGED enter");
                            com.android.server.trust.TrustManagerService.this.refreshDeviceLockedForUser(com.android.server.trust.TrustManagerService.this.mCurrentUser);
                            break;
                    }
                }
            };
        }

        public void reportUnlockAttempt(boolean z, int i) throws android.os.RemoteException {
            if (com.android.server.trust.TrustManagerService.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustManagerService.TAG, "reportUnlockAttempt(authenticated=%s, userId=%s)", java.lang.Boolean.valueOf(z), java.lang.Integer.valueOf(i));
            }
            enforceReportPermission();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(3, z ? 1 : 0, i).sendToTarget();
        }

        public void reportUserRequestedUnlock(int i, boolean z) throws android.os.RemoteException {
            enforceReportPermission();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(16, i, z ? 1 : 0).sendToTarget();
        }

        public void reportUserMayRequestUnlock(int userId) throws android.os.RemoteException {
            enforceReportPermission();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(18, userId, 0).sendToTarget();
        }

        public void reportUnlockLockout(int timeoutMs, int userId) throws android.os.RemoteException {
            enforceReportPermission();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(13, timeoutMs, userId).sendToTarget();
        }

        public void reportEnabledTrustAgentsChanged(int userId) throws android.os.RemoteException {
            enforceReportPermission();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(4, userId, 0).sendToTarget();
        }

        public void reportKeyguardShowingChanged() throws android.os.RemoteException {
            enforceReportPermission();
            this.mSubHandler.removeMessages(6);
            this.mSubHandler.sendMessageAtFrontOfQueue(com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(6));
            if (com.android.server.trust.TrustManagerService.DEBUG) {
                android.util.Log.d(com.android.server.trust.TrustManagerService.TAG, "reportKeyguardShowingChanged begin");
            }
            notifyVoldDecryptAEKey(com.android.server.trust.TrustManagerService.this.mCurrentUser);
            this.mSubHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.trust.TrustManagerService$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.trust.TrustManagerService.AnonymousClass2.lambda$reportKeyguardShowingChanged$0();
                }
            }, 0L);
            if (com.android.server.trust.TrustManagerService.DEBUG) {
                android.util.Log.d(com.android.server.trust.TrustManagerService.TAG, "reportKeyguardShowingChanged end");
            }
        }

        static /* synthetic */ void lambda$reportKeyguardShowingChanged$0() {
        }

        private void notifyVoldDecryptAEKey(final int userId) {
            com.android.server.trust.TrustManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.trust.TrustManagerService.2.1
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.trust.TrustManagerService.this.mUserManager.getUserInfo(userId);
                    boolean trusted = com.android.server.trust.TrustManagerService.this.aggregateIsTrusted(userId);
                    boolean showingKeyguard = true;
                    boolean secure = com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isSecure(userId);
                    android.view.IWindowManager wm = android.view.WindowManagerGlobal.getWindowManagerService();
                    try {
                        showingKeyguard = wm.isKeyguardLocked();
                    } catch (java.lang.Exception e) {
                    }
                    if (com.android.server.trust.TrustManagerService.DEBUG) {
                        com.android.server.utils.Slogf.v(com.android.server.trust.TrustManagerService.TAG, "notifyVoldDecryptAEKey trusted:" + trusted + " showingKeyguard:" + showingKeyguard + " secure:" + secure);
                    }
                    if (trusted && !showingKeyguard && secure) {
                        try {
                            android.os.storage.IStorageManager storageManager = android.os.storage.IStorageManager.Stub.asInterface(android.os.ServiceManager.getService("mount"));
                            storageManager.unlockCeStorage(userId, (byte[]) null);
                            if (com.android.server.trust.TrustManagerService.DEBUG) {
                                com.android.server.utils.Slogf.v(com.android.server.trust.TrustManagerService.TAG, "trust notify Vold end");
                            }
                        } catch (java.lang.Exception e2) {
                            com.android.server.utils.Slogf.w(com.android.server.trust.TrustManagerService.TAG, "Failed to unlock: " + e2.getMessage());
                        }
                    }
                }
            });
        }

        public void registerTrustListener(android.app.trust.ITrustListener trustListener) throws android.os.RemoteException {
            enforceListenerPermission();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(1, trustListener).sendToTarget();
        }

        public void unregisterTrustListener(android.app.trust.ITrustListener trustListener) throws android.os.RemoteException {
            enforceListenerPermission();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(2, trustListener).sendToTarget();
        }

        public boolean isDeviceLocked(int userId, int deviceId) throws android.os.RemoteException {
            if (deviceId != 0) {
                return false;
            }
            int userId2 = android.app.ActivityManager.handleIncomingUser(getCallingPid(), getCallingUid(), userId, false, true, "isDeviceLocked", null);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (!com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isSeparateProfileChallengeEnabled(userId2)) {
                    userId2 = com.android.server.trust.TrustManagerService.this.resolveProfileParent(userId2);
                }
                return com.android.server.trust.TrustManagerService.this.isDeviceLockedInner(userId2);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isDeviceSecure(int userId, int deviceId) throws android.os.RemoteException {
            if (deviceId != 0) {
                return false;
            }
            int userId2 = android.app.ActivityManager.handleIncomingUser(getCallingPid(), getCallingUid(), userId, false, true, "isDeviceSecure", null);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (!com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isSeparateProfileChallengeEnabled(userId2)) {
                    userId2 = com.android.server.trust.TrustManagerService.this.resolveProfileParent(userId2);
                }
                return com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isSecure(userId2);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isInSignificantPlace() {
            super.isInSignificantPlace_enforcePermission();
            if (android.security.Flags.significantPlaces()) {
                com.android.server.trust.TrustManagerService.this.mSignificantPlaceServiceWatcher.runOnBinder(new com.android.server.servicewatcher.ServiceWatcher.BinderOperation() { // from class: com.android.server.trust.TrustManagerService$2$$ExternalSyntheticLambda0
                    @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
                    public final void run(android.os.IBinder iBinder) {
                        android.hardware.location.ISignificantPlaceProvider.Stub.asInterface(iBinder).onSignificantPlaceCheck();
                    }
                });
            }
            return com.android.server.trust.TrustManagerService.this.mIsInSignificantPlace;
        }

        private void enforceReportPermission() {
            com.android.server.trust.TrustManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_KEYGUARD_SECURE_STORAGE", "reporting trust events");
        }

        private void enforceListenerPermission() {
            com.android.server.trust.TrustManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.TRUST_LISTENER", "register trust listener");
        }

        protected void dump(java.io.FileDescriptor fd, final java.io.PrintWriter fout, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.trust.TrustManagerService.this.mContext, com.android.server.trust.TrustManagerService.TAG, fout)) {
                if (com.android.server.trust.TrustManagerService.this.isSafeMode()) {
                    fout.println("disabled because the system is in safe mode.");
                } else if (!com.android.server.trust.TrustManagerService.this.mTrustAgentsCanRun) {
                    fout.println("disabled because the third-party apps can't run yet.");
                } else {
                    final java.util.List<android.content.pm.UserInfo> userInfos = com.android.server.trust.TrustManagerService.this.mUserManager.getAliveUsers();
                    com.android.server.trust.TrustManagerService.this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.trust.TrustManagerService.2.3
                        @Override // java.lang.Runnable
                        public void run() {
                            fout.println("Trust manager state:");
                            for (android.content.pm.UserInfo user : userInfos) {
                                com.android.server.trust.TrustManagerService.AnonymousClass2.this.dumpUser(fout, user, user.id == com.android.server.trust.TrustManagerService.this.mCurrentUser);
                            }
                            if (com.android.server.trust.TrustManagerService.this.mSignificantPlaceServiceWatcher != null) {
                                com.android.server.trust.TrustManagerService.this.mSignificantPlaceServiceWatcher.dump(fout);
                            }
                        }
                    }, 1500L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpUser(java.io.PrintWriter fout, android.content.pm.UserInfo user, boolean isCurrent) {
            boolean locked;
            fout.printf(" User \"%s\" (id=%d, flags=%#x)", user.name, java.lang.Integer.valueOf(user.id), java.lang.Integer.valueOf(user.flags));
            if (!user.supportsSwitchToByUser()) {
                if (com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isProfileWithUnifiedChallenge(user.id)) {
                    fout.print(" (profile with unified challenge)");
                    locked = com.android.server.trust.TrustManagerService.this.isDeviceLockedInner(com.android.server.trust.TrustManagerService.this.resolveProfileParent(user.id));
                } else if (com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isSeparateProfileChallengeEnabled(user.id)) {
                    fout.print(" (profile with separate challenge)");
                    locked = com.android.server.trust.TrustManagerService.this.isDeviceLockedInner(user.id);
                } else {
                    fout.println(" (user that cannot be switched to)");
                    locked = com.android.server.trust.TrustManagerService.this.isDeviceLockedInner(user.id);
                }
                fout.println(": deviceLocked=" + dumpBool(locked));
                fout.println("   Trust agents disabled because switching to this user is not possible.");
                return;
            }
            if (isCurrent) {
                fout.print(" (current)");
            }
            fout.print(": trustState=" + com.android.server.trust.TrustManagerService.this.getUserTrustStateInner(user.id));
            fout.print(", trustManaged=" + dumpBool(com.android.server.trust.TrustManagerService.this.aggregateIsTrustManaged(user.id)));
            fout.print(", deviceLocked=" + dumpBool(com.android.server.trust.TrustManagerService.this.isDeviceLockedInner(user.id)));
            fout.print(", isActiveUnlockRunning=" + dumpBool(com.android.server.trust.TrustManagerService.this.aggregateIsActiveUnlockRunning(user.id)));
            fout.print(", strongAuthRequired=" + dumpHex(com.android.server.trust.TrustManagerService.this.mStrongAuthTracker.getStrongAuthForUser(user.id)));
            fout.println();
            fout.println("   Enabled agents:");
            boolean duplicateSimpleNames = false;
            android.util.ArraySet<java.lang.String> simpleNames = new android.util.ArraySet<>();
            for (com.android.server.trust.TrustManagerService.AgentInfo info : com.android.server.trust.TrustManagerService.this.mActiveAgents) {
                if (info.userId == user.id) {
                    boolean trusted = info.agent.isTrusted();
                    fout.print("    ");
                    fout.println(info.component.flattenToShortString());
                    fout.print("     bound=" + dumpBool(info.agent.isBound()));
                    fout.print(", connected=" + dumpBool(info.agent.isConnected()));
                    fout.print(", managingTrust=" + dumpBool(info.agent.isManagingTrust()));
                    fout.print(", trusted=" + dumpBool(trusted));
                    fout.println();
                    if (trusted) {
                        fout.println("      message=\"" + ((java.lang.Object) info.agent.getMessage()) + "\"");
                    }
                    if (!info.agent.isConnected()) {
                        java.lang.String restartTime = com.android.server.trust.TrustArchive.formatDuration(info.agent.getScheduledRestartUptimeMillis() - android.os.SystemClock.uptimeMillis());
                        fout.println("      restartScheduledAt=" + restartTime);
                    }
                    if (!simpleNames.add(com.android.server.trust.TrustArchive.getSimpleName(info.component))) {
                        duplicateSimpleNames = true;
                    }
                }
            }
            fout.println("   Events:");
            com.android.server.trust.TrustManagerService.this.mArchive.dump(fout, 50, user.id, "    ", duplicateSimpleNames);
            fout.println();
        }

        private java.lang.String dumpBool(boolean b) {
            return b ? "1" : "0";
        }

        private java.lang.String dumpHex(int i) {
            return "0x" + java.lang.Integer.toHexString(i);
        }

        public void setDeviceLockedForUser(int userId, boolean locked) {
            enforceReportPermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isSeparateProfileChallengeEnabled(userId) && com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isSecure(userId)) {
                    synchronized (com.android.server.trust.TrustManagerService.this.mDeviceLockedForUser) {
                        com.android.server.trust.TrustManagerService.this.mDeviceLockedForUser.put(userId, locked);
                    }
                    com.android.server.trust.TrustManagerService.this.notifyKeystoreOfDeviceLockState(userId, locked);
                    if (locked) {
                        try {
                            android.app.ActivityManager.getService().notifyLockedProfile(userId);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    android.content.Intent lockIntent = new android.content.Intent("android.intent.action.DEVICE_LOCKED_CHANGED");
                    lockIntent.addFlags(1073741824);
                    lockIntent.putExtra("android.intent.extra.user_handle", userId);
                    com.android.server.trust.TrustManagerService.this.mContext.sendBroadcastAsUser(lockIntent, android.os.UserHandle.SYSTEM, "android.permission.TRUST_LISTENER", null);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean isTrustUsuallyManaged(int userId) {
            super.isTrustUsuallyManaged_enforcePermission();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.trust.TrustManagerService.this.isTrustUsuallyManagedInternal(userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unlockedByBiometricForUser(int i, android.hardware.biometrics.BiometricSourceType biometricSourceType) {
            enforceReportPermission();
            synchronized (com.android.server.trust.TrustManagerService.this.mUsersUnlockedByBiometric) {
                com.android.server.trust.TrustManagerService.this.mUsersUnlockedByBiometric.put(i, true);
            }
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(14, i, !com.android.server.trust.TrustManagerService.this.isAutomotive() ? 1 : 0).sendToTarget();
            com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(17, java.lang.Integer.valueOf(i)).sendToTarget();
        }

        public void clearAllBiometricRecognized(android.hardware.biometrics.BiometricSourceType biometricSource, int unlockedUser) {
            enforceReportPermission();
            synchronized (com.android.server.trust.TrustManagerService.this.mUsersUnlockedByBiometric) {
                com.android.server.trust.TrustManagerService.this.mUsersUnlockedByBiometric.clear();
            }
            android.os.Message message = com.android.server.trust.TrustManagerService.this.mHandler.obtainMessage(14, -1, 0);
            if (unlockedUser >= 0) {
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putInt(com.android.server.trust.TrustManagerService.REFRESH_DEVICE_LOCKED_EXCEPT_USER, unlockedUser);
                message.setData(bundle);
            }
            message.sendToTarget();
        }

        public boolean isActiveUnlockRunning(int userId) throws android.os.RemoteException {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.trust.TrustManagerService.this.aggregateIsActiveUnlockRunning(userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    static /* synthetic */ void lambda$waitForIdle$1() {
    }

    void waitForIdle() {
        this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.trust.TrustManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.trust.TrustManagerService.lambda$waitForIdle$1();
            }
        }, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTrustUsuallyManagedInternal(int userId) {
        synchronized (this.mTrustUsuallyManagedForUser) {
            int i = this.mTrustUsuallyManagedForUser.indexOfKey(userId);
            if (i >= 0) {
                return this.mTrustUsuallyManagedForUser.valueAt(i);
            }
            boolean persistedValue = this.mLockPatternUtils.isTrustUsuallyManaged(userId);
            synchronized (this.mTrustUsuallyManagedForUser) {
                int i2 = this.mTrustUsuallyManagedForUser.indexOfKey(userId);
                if (i2 >= 0) {
                    return this.mTrustUsuallyManagedForUser.valueAt(i2);
                }
                this.mTrustUsuallyManagedForUser.put(userId, persistedValue);
                return persistedValue;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int resolveProfileParent(int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo parent = this.mUserManager.getProfileParent(userId);
            if (parent != null) {
                return parent.getUserHandle().getIdentifier();
            }
            return userId;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private android.os.Handler createHandler(android.os.Looper looper) {
        return new android.os.Handler(looper) { // from class: com.android.server.trust.TrustManagerService.3
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
                android.util.SparseBooleanArray usuallyManaged;
                if (com.android.server.trust.TrustManagerService.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.trust.TrustManagerService.TAG, "handler: %s", java.lang.Integer.valueOf(msg.what));
                }
                switch (msg.what) {
                    case 1:
                        com.android.server.trust.TrustManagerService.this.addListener((android.app.trust.ITrustListener) msg.obj);
                        return;
                    case 2:
                        com.android.server.trust.TrustManagerService.this.removeListener((android.app.trust.ITrustListener) msg.obj);
                        return;
                    case 3:
                        com.android.server.trust.TrustManagerService.this.dispatchUnlockAttempt(msg.arg1 != 0, msg.arg2);
                        return;
                    case 4:
                        com.android.server.trust.TrustManagerService.this.refreshAgentList(-1);
                        com.android.server.trust.TrustManagerService.this.refreshDeviceLockedForUser(-1);
                        com.android.server.trust.TrustManagerService.this.dispatchOnEnabledTrustAgentsChanged(msg.arg1);
                        return;
                    case 5:
                    default:
                        return;
                    case 6:
                        com.android.server.trust.TrustManagerService.this.dispatchTrustableDowngrade();
                        com.android.server.trust.TrustManagerService.this.refreshDeviceLockedForUser(com.android.server.trust.TrustManagerService.this.mCurrentUser);
                        return;
                    case 7:
                    case 8:
                    case 11:
                        com.android.server.trust.TrustManagerService.this.refreshAgentList(msg.arg1);
                        return;
                    case 9:
                        com.android.server.trust.TrustManagerService.this.mCurrentUser = msg.arg1;
                        com.android.server.trust.TrustManagerService.this.refreshDeviceLockedForUser(-1);
                        return;
                    case 10:
                        synchronized (com.android.server.trust.TrustManagerService.this.mTrustUsuallyManagedForUser) {
                            usuallyManaged = com.android.server.trust.TrustManagerService.this.mTrustUsuallyManagedForUser.clone();
                            break;
                        }
                        for (int i = 0; i < usuallyManaged.size(); i++) {
                            int userId = usuallyManaged.keyAt(i);
                            boolean value = usuallyManaged.valueAt(i);
                            if (value != com.android.server.trust.TrustManagerService.this.mLockPatternUtils.isTrustUsuallyManaged(userId)) {
                                com.android.server.trust.TrustManagerService.this.mLockPatternUtils.setTrustUsuallyManaged(value, userId);
                            }
                        }
                        return;
                    case 12:
                        com.android.server.trust.TrustManagerService.this.setDeviceLockedForUser(msg.arg1, true);
                        return;
                    case 13:
                        com.android.server.trust.TrustManagerService.this.dispatchUnlockLockout(msg.arg1, msg.arg2);
                        return;
                    case 14:
                        if (msg.arg2 == 1) {
                            com.android.server.trust.TrustManagerService.this.updateTrust(msg.arg1, 0, true, null);
                        }
                        int unlockedUser = msg.getData().getInt(com.android.server.trust.TrustManagerService.REFRESH_DEVICE_LOCKED_EXCEPT_USER, -10000);
                        com.android.server.trust.TrustManagerService.this.refreshDeviceLockedForUser(msg.arg1, unlockedUser);
                        return;
                    case 15:
                        boolean shouldOverride = msg.arg1 == 1;
                        com.android.server.trust.TrustManagerService.TimeoutType timeoutType = msg.arg2 == 1 ? com.android.server.trust.TrustManagerService.TimeoutType.TRUSTABLE : com.android.server.trust.TrustManagerService.TimeoutType.TRUSTED;
                        com.android.server.trust.TrustManagerService.this.handleScheduleTrustTimeout(shouldOverride, timeoutType);
                        return;
                    case 16:
                        com.android.server.trust.TrustManagerService.this.dispatchUserRequestedUnlock(msg.arg1, msg.arg2 != 0);
                        return;
                    case 17:
                        if (com.android.server.trust.TrustManagerService.DEBUG) {
                            com.android.server.utils.Slogf.d(com.android.server.trust.TrustManagerService.TAG, "REFRESH_TRUSTABLE_TIMERS_AFTER_AUTH userId=%s", java.lang.Integer.valueOf(msg.arg1));
                        }
                        com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener trustableAlarm = (com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener) com.android.server.trust.TrustManagerService.this.mTrustableTimeoutAlarmListenerForUser.get(msg.arg1);
                        if (com.android.server.trust.TrustManagerService.DEBUG) {
                            if (trustableAlarm != null) {
                                com.android.server.utils.Slogf.d(com.android.server.trust.TrustManagerService.TAG, "REFRESH_TRUSTABLE_TIMERS_AFTER_AUTH trustable alarm isQueued=%s", java.lang.Boolean.valueOf(trustableAlarm.mIsQueued));
                            } else {
                                com.android.server.utils.Slogf.d(com.android.server.trust.TrustManagerService.TAG, "REFRESH_TRUSTABLE_TIMERS_AFTER_AUTH no trustable alarm");
                            }
                        }
                        if (trustableAlarm != null && trustableAlarm.isQueued()) {
                            com.android.server.trust.TrustManagerService.this.refreshTrustableTimers(msg.arg1);
                            return;
                        }
                        return;
                    case 18:
                        com.android.server.trust.TrustManagerService.this.dispatchUserMayRequestUnlock(msg.arg1);
                        return;
                }
            }
        };
    }

    private static class SettingsAttrs {
        public boolean canUnlockProfile;
        public android.content.ComponentName componentName;

        public SettingsAttrs(android.content.ComponentName componentName, boolean canUnlockProfile) {
            this.componentName = componentName;
            this.canUnlockProfile = canUnlockProfile;
        }
    }

    private class Receiver extends android.content.BroadcastReceiver {
        private Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) throws java.lang.Throwable {
            int userId;
            java.lang.String action = intent.getAction();
            if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action)) {
                com.android.server.trust.TrustManagerService.this.refreshAgentList(getSendingUserId());
                com.android.server.trust.TrustManagerService.this.updateDevicePolicyFeatures();
                return;
            }
            if ("android.intent.action.USER_ADDED".equals(action) || "android.intent.action.USER_STARTED".equals(action)) {
                int userId2 = getUserId(intent);
                if (userId2 > 0) {
                    com.android.server.trust.TrustManagerService.this.maybeEnableFactoryTrustAgents(userId2);
                    return;
                }
                return;
            }
            if ("android.intent.action.USER_REMOVED".equals(action) && (userId = getUserId(intent)) > 0) {
                synchronized (com.android.server.trust.TrustManagerService.this.mDeviceLockedForUser) {
                    com.android.server.trust.TrustManagerService.this.mDeviceLockedForUser.delete(userId);
                }
                synchronized (com.android.server.trust.TrustManagerService.this.mTrustUsuallyManagedForUser) {
                    com.android.server.trust.TrustManagerService.this.mTrustUsuallyManagedForUser.delete(userId);
                }
                synchronized (com.android.server.trust.TrustManagerService.this.mUsersUnlockedByBiometric) {
                    com.android.server.trust.TrustManagerService.this.mUsersUnlockedByBiometric.delete(userId);
                }
                com.android.server.trust.TrustManagerService.this.refreshAgentList(userId);
                com.android.server.trust.TrustManagerService.this.refreshDeviceLockedForUser(userId);
            }
        }

        private int getUserId(android.content.Intent intent) {
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -100);
            if (userId > 0) {
                return userId;
            }
            android.util.Log.w(com.android.server.trust.TrustManagerService.TAG, "EXTRA_USER_HANDLE missing or invalid, value=" + userId);
            return -100;
        }

        public void register(android.content.Context context) {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
            filter.addAction("android.intent.action.USER_ADDED");
            filter.addAction("android.intent.action.USER_REMOVED");
            filter.addAction("android.intent.action.USER_STARTED");
            context.registerReceiverAsUser(this, android.os.UserHandle.ALL, filter, null, null);
        }
    }

    private class StrongAuthTracker extends com.android.internal.widget.LockPatternUtils.StrongAuthTracker {
        android.util.SparseBooleanArray mStartFromSuccessfulUnlock;

        StrongAuthTracker(android.content.Context context, android.os.Looper looper) {
            super(context, looper);
            this.mStartFromSuccessfulUnlock = new android.util.SparseBooleanArray();
        }

        public void onStrongAuthRequiredChanged(int userId) {
            this.mStartFromSuccessfulUnlock.delete(userId);
            if (com.android.server.trust.TrustManagerService.DEBUG) {
                android.util.Log.i(com.android.server.trust.TrustManagerService.TAG, "onStrongAuthRequiredChanged(" + userId + ") -> trustAllowed=" + isTrustAllowedForUser(userId) + " agentsCanRun=" + canAgentsRunForUser(userId));
            }
            if (!isTrustAllowedForUser(userId)) {
                com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener alarm = (com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener) com.android.server.trust.TrustManagerService.this.mTrustTimeoutAlarmListenerForUser.get(java.lang.Integer.valueOf(userId));
                cancelPendingAlarm(alarm);
                com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener alarm2 = (com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener) com.android.server.trust.TrustManagerService.this.mTrustableTimeoutAlarmListenerForUser.get(userId);
                cancelPendingAlarm(alarm2);
                com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener alarm3 = (com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener) com.android.server.trust.TrustManagerService.this.mIdleTrustableTimeoutAlarmListenerForUser.get(userId);
                cancelPendingAlarm(alarm3);
            }
            com.android.server.trust.TrustManagerService.this.refreshAgentList(userId);
            com.android.server.trust.TrustManagerService.this.updateTrust(userId, 0);
        }

        private void cancelPendingAlarm(com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener alarm) {
            if (alarm != null && alarm.isQueued()) {
                alarm.setQueued(false);
                com.android.server.trust.TrustManagerService.this.mAlarmManager.cancel(alarm);
            }
        }

        boolean canAgentsRunForUser(int userId) {
            return this.mStartFromSuccessfulUnlock.get(userId) || super.isTrustAllowedForUser(userId);
        }

        void allowTrustFromUnlock(int userId) {
            if (userId < 0) {
                throw new java.lang.IllegalArgumentException("userId must be a valid user: " + userId);
            }
            boolean previous = canAgentsRunForUser(userId);
            this.mStartFromSuccessfulUnlock.put(userId, true);
            if (com.android.server.trust.TrustManagerService.DEBUG) {
                android.util.Log.i(com.android.server.trust.TrustManagerService.TAG, "allowTrustFromUnlock(" + userId + ") -> trustAllowed=" + isTrustAllowedForUser(userId) + " agentsCanRun=" + canAgentsRunForUser(userId));
            }
            if (canAgentsRunForUser(userId) != previous) {
                com.android.server.trust.TrustManagerService.this.refreshAgentList(userId);
            }
        }
    }

    private abstract class TrustTimeoutAlarmListener implements android.app.AlarmManager.OnAlarmListener {
        protected boolean mIsQueued = false;
        protected final int mUserId;

        protected abstract void handleAlarm();

        TrustTimeoutAlarmListener(int userId) {
            this.mUserId = userId;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            this.mIsQueued = false;
            handleAlarm();
            if (com.android.server.trust.TrustManagerService.this.mStrongAuthTracker.isTrustAllowedForUser(this.mUserId)) {
                if (com.android.server.trust.TrustManagerService.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.trust.TrustManagerService.TAG, "Revoking all trust because of trust timeout");
                }
                com.android.internal.widget.LockPatternUtils lockPatternUtils = com.android.server.trust.TrustManagerService.this.mLockPatternUtils;
                com.android.server.trust.TrustManagerService.StrongAuthTracker unused = com.android.server.trust.TrustManagerService.this.mStrongAuthTracker;
                lockPatternUtils.requireStrongAuth(256, this.mUserId);
            }
        }

        public boolean isQueued() {
            return this.mIsQueued;
        }

        public void setQueued(boolean isQueued) {
            this.mIsQueued = isQueued;
        }
    }

    private class TrustedTimeoutAlarmListener extends com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener {
        TrustedTimeoutAlarmListener(int userId) {
            super(userId);
        }

        @Override // com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener
        public void handleAlarm() {
            com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener otherAlarm = (com.android.server.trust.TrustManagerService.TrustableTimeoutAlarmListener) com.android.server.trust.TrustManagerService.this.mTrustableTimeoutAlarmListenerForUser.get(this.mUserId);
            if (otherAlarm != null && otherAlarm.isQueued()) {
                synchronized (com.android.server.trust.TrustManagerService.this.mAlarmLock) {
                    disableNonrenewableTrustWhileRenewableTrustIsPresent();
                }
            }
        }

        private void disableNonrenewableTrustWhileRenewableTrustIsPresent() {
            synchronized (com.android.server.trust.TrustManagerService.this.mUserTrustState) {
                if (com.android.server.trust.TrustManagerService.this.mUserTrustState.get(this.mUserId) == com.android.server.trust.TrustManagerService.TrustState.TRUSTED) {
                    com.android.server.trust.TrustManagerService.this.mUserTrustState.put(this.mUserId, com.android.server.trust.TrustManagerService.TrustState.TRUSTABLE);
                    com.android.server.trust.TrustManagerService.this.updateTrust(this.mUserId, 0);
                }
            }
        }
    }

    private class TrustableTimeoutAlarmListener extends com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener {
        TrustableTimeoutAlarmListener(int userId) {
            super(userId);
        }

        @Override // com.android.server.trust.TrustManagerService.TrustTimeoutAlarmListener
        public void handleAlarm() {
            com.android.server.trust.TrustManagerService.this.cancelBothTrustableAlarms(this.mUserId);
            com.android.server.trust.TrustManagerService.TrustedTimeoutAlarmListener otherAlarm = (com.android.server.trust.TrustManagerService.TrustedTimeoutAlarmListener) com.android.server.trust.TrustManagerService.this.mTrustTimeoutAlarmListenerForUser.get(java.lang.Integer.valueOf(this.mUserId));
            if (otherAlarm != null && otherAlarm.isQueued()) {
                synchronized (com.android.server.trust.TrustManagerService.this.mAlarmLock) {
                    disableRenewableTrustWhileNonrenewableTrustIsPresent();
                }
            }
        }

        private void disableRenewableTrustWhileNonrenewableTrustIsPresent() {
            for (com.android.server.trust.TrustManagerService.AgentInfo agentInfo : com.android.server.trust.TrustManagerService.this.mActiveAgents) {
                agentInfo.agent.setUntrustable();
            }
            com.android.server.trust.TrustManagerService.this.updateTrust(this.mUserId, 0);
        }
    }
}
