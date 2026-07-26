package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class UserController implements android.os.Handler.Callback {
    static final int CLEAR_USER_JOURNEY_SESSION_MSG = 200;
    static final int COMPLETE_USER_SWITCH_MSG = 130;
    static final int CONTINUE_USER_SWITCH_MSG = 20;
    static final int DEFAULT_USER_SWITCH_TIMEOUT_MS = 3000;
    private static final int DISMISS_KEYGUARD_TIMEOUT_MS = 2000;
    static final int FOREGROUND_PROFILE_CHANGED_MSG = 70;
    private static final int LONG_USER_SWITCH_OBSERVER_WARNING_TIME_MS = 500;
    private static final int NO_ARG2 = 0;
    static final int REPORT_LOCKED_BOOT_COMPLETE_MSG = 110;
    static final int REPORT_USER_SWITCH_COMPLETE_MSG = 80;
    static final int REPORT_USER_SWITCH_MSG = 10;
    static final int SCHEDULED_STOP_BACKGROUND_USER_MSG = 150;
    static final int SHOW_KEYGUARD_TIMEOUT_MS = 20000;
    static final int START_PROFILES_MSG = 40;
    static final int START_USER_SWITCH_FG_MSG = 120;
    static final int START_USER_SWITCH_UI_MSG = 1000;
    private static final java.lang.String TAG = "ActivityManager";
    private static final int USER_COMPLETED_EVENT_DELAY_MS = 5000;
    static final int USER_COMPLETED_EVENT_MSG = 140;
    static final int USER_CURRENT_MSG = 60;
    private static final int USER_JOURNEY_TIMEOUT_MS = 90000;
    static final int USER_START_MSG = 50;
    private static final int USER_SWITCH_CALLBACKS_TIMEOUT_MS = 5000;
    static final int USER_SWITCH_CALLBACKS_TIMEOUT_MSG = 90;
    static final int USER_SWITCH_TIMEOUT_MSG = 30;
    static final int USER_UNLOCKED_MSG = 105;
    static final int USER_UNLOCK_MSG = 100;
    public static com.android.server.am.IUserControllerExt.IStaticExt mStaticExt = (com.android.server.am.IUserControllerExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IUserControllerExt.IStaticExt.class).create();
    private volatile boolean mAllowUserUnlocking;
    private int mBackgroundUserScheduledStopTimeSecs;
    volatile boolean mBootCompleted;
    private final android.util.SparseIntArray mCompletedEventTypes;
    private volatile android.util.ArraySet<java.lang.String> mCurWaitingUserSwitchCallbacks;
    private int[] mCurrentProfileIds;
    private volatile int mCurrentUserId;
    private boolean mDelayUserDataLocking;
    private final android.os.Handler mHandler;
    private boolean mInitialized;
    private final com.android.server.am.UserController.Injector mInjector;
    private boolean mIsBroadcastSentForSystemUserStarted;
    private boolean mIsBroadcastSentForSystemUserStarting;
    private final java.util.ArrayList<java.lang.Integer> mLastActiveUsersForDelayedLocking;
    private volatile long mLastUserUnlockingUptime;
    private final java.lang.Object mLock;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private int mMaxRunningUsers;
    private final java.util.ArrayDeque<java.lang.Integer> mPendingTargetUserIds;
    private final java.util.List<com.android.server.am.UserController.PendingUserStart> mPendingUserStarts;
    private int[] mStartedUserArray;
    private final android.util.SparseArray<com.android.server.am.UserState> mStartedUsers;
    private int mStopUserOnSwitch;
    private java.lang.String mSwitchingFromSystemUserMessage;
    private java.lang.String mSwitchingToSystemUserMessage;
    private volatile int mTargetUserId;
    private android.util.ArraySet<java.lang.String> mTimeoutUserSwitchCallbacks;
    private final android.os.Handler mUiHandler;
    public com.android.server.am.IUserControllerExt mUserControllerExt;
    private final com.android.server.pm.UserManagerInternal.UserLifecycleListener mUserLifecycleListener;
    private final java.util.ArrayList<java.lang.Integer> mUserLru;
    private final android.util.SparseIntArray mUserProfileGroupIds;
    private final android.os.RemoteCallbackList<android.app.IUserSwitchObserver> mUserSwitchObservers;
    private boolean mUserSwitchUiEnabled;
    private com.android.server.am.UserController.UserControllerWrapper mWrapper;

    UserController(com.android.server.am.ActivityManagerService service) {
        this(new com.android.server.am.UserController.Injector(service));
        this.mUserControllerExt.setInjector(service, this.mLock, this.mStartedUsers);
    }

    /* JADX WARN: Multi-variable type inference failed */
    UserController(com.android.server.am.UserController.Injector injector) {
        this.mBackgroundUserScheduledStopTimeSecs = -1;
        this.mLock = new java.lang.Object();
        this.mCurrentUserId = 0;
        this.mTargetUserId = -10000;
        this.mPendingTargetUserIds = new java.util.ArrayDeque<>();
        this.mStartedUsers = new android.util.SparseArray<>();
        this.mUserLru = new java.util.ArrayList<>();
        this.mStartedUserArray = new int[]{0};
        this.mCurrentProfileIds = new int[0];
        this.mUserProfileGroupIds = new android.util.SparseIntArray();
        this.mUserSwitchObservers = new android.os.RemoteCallbackList<>();
        this.mUserSwitchUiEnabled = true;
        this.mLastActiveUsersForDelayedLocking = new java.util.ArrayList<>();
        this.mCompletedEventTypes = new android.util.SparseIntArray();
        this.mStopUserOnSwitch = -1;
        this.mLastUserUnlockingUptime = 0L;
        this.mPendingUserStarts = new java.util.ArrayList();
        this.mUserLifecycleListener = new com.android.server.pm.UserManagerInternal.UserLifecycleListener() { // from class: com.android.server.am.UserController.1
            @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
            public void onUserCreated(android.content.pm.UserInfo user, java.lang.Object token) {
                com.android.server.am.UserController.this.onUserAdded(user);
            }

            @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
            public void onUserRemoved(android.content.pm.UserInfo user) {
                com.android.server.am.UserController.this.onUserRemoved(user.id);
            }
        };
        this.mWrapper = new com.android.server.am.UserController.UserControllerWrapper();
        this.mUserControllerExt = (com.android.server.am.IUserControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IUserControllerExt.class).create();
        this.mInjector = injector;
        this.mHandler = this.mInjector.getHandler(this);
        this.mUiHandler = this.mInjector.getUiHandler(this);
        com.android.server.am.UserState userState = new com.android.server.am.UserState(android.os.UserHandle.SYSTEM);
        userState.mUnlockProgress.addListener(new com.android.server.am.UserController.UserProgressListener());
        this.mStartedUsers.put(0, userState);
        this.mUserLru.add(0);
        this.mLockPatternUtils = this.mInjector.getLockPatternUtils();
        updateStartedUserArrayLU();
    }

    void setInitialConfig(boolean userSwitchUiEnabled, int maxRunningUsers, boolean delayUserDataLocking, int backgroundUserScheduledStopTimeSecs) {
        synchronized (this.mLock) {
            this.mUserSwitchUiEnabled = userSwitchUiEnabled;
            this.mMaxRunningUsers = maxRunningUsers;
            this.mDelayUserDataLocking = delayUserDataLocking;
            this.mBackgroundUserScheduledStopTimeSecs = backgroundUserScheduledStopTimeSecs;
            this.mInitialized = true;
        }
    }

    private boolean isUserSwitchUiEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mUserSwitchUiEnabled;
        }
        return z;
    }

    int getMaxRunningUsers() {
        int i;
        synchronized (this.mLock) {
            i = this.mMaxRunningUsers;
        }
        return i;
    }

    void setStopUserOnSwitch(int value) {
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") == -1 && this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") == -1) {
            throw new java.lang.SecurityException("You either need MANAGE_USERS or INTERACT_ACROSS_USERS permission to call setStopUserOnSwitch()");
        }
        synchronized (this.mLock) {
            com.android.server.utils.Slogf.i("ActivityManager", "setStopUserOnSwitch(): %d -> %d", java.lang.Integer.valueOf(this.mStopUserOnSwitch), java.lang.Integer.valueOf(value));
            this.mStopUserOnSwitch = value;
        }
    }

    private boolean shouldStopUserOnSwitch() {
        synchronized (this.mLock) {
            if (this.mStopUserOnSwitch != -1) {
                boolean value = this.mStopUserOnSwitch == 1;
                com.android.server.utils.Slogf.i("ActivityManager", "shouldStopUserOnSwitch(): returning overridden value (%b)", java.lang.Boolean.valueOf(value));
                return value;
            }
            int property = android.os.SystemProperties.getInt("fw.stop_bg_users_on_switch", -1);
            return property == -1 ? this.mDelayUserDataLocking : property == 1;
        }
    }

    void finishUserSwitch(final com.android.server.am.UserState uss) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$finishUserSwitch$0(uss);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserSwitch$0(com.android.server.am.UserState uss) throws java.lang.Throwable {
        finishUserBoot(uss);
        startProfiles();
        stopExcessRunningUsers();
    }

    private void addUserToUserLru(int userId) {
        synchronized (this.mLock) {
            java.lang.Integer userIdObj = java.lang.Integer.valueOf(userId);
            this.mUserLru.remove(userIdObj);
            this.mUserLru.add(userIdObj);
            java.lang.Integer parentIdObj = java.lang.Integer.valueOf(this.mUserProfileGroupIds.get(userId, -10000));
            if (parentIdObj.intValue() != -10000 && !parentIdObj.equals(userIdObj) && this.mUserLru.remove(parentIdObj)) {
                this.mUserLru.add(parentIdObj);
            }
        }
    }

    java.util.List<java.lang.Integer> getRunningUsersLU() {
        java.util.ArrayList<java.lang.Integer> runningUsers = new java.util.ArrayList<>();
        for (java.lang.Integer userId : this.mUserLru) {
            com.android.server.am.UserState uss = this.mStartedUsers.get(userId.intValue());
            if (uss != null && uss.state != 4 && uss.state != 5) {
                runningUsers.add(userId);
            }
        }
        return runningUsers;
    }

    private void stopExcessRunningUsers() {
        android.util.ArraySet<java.lang.Integer> exemptedUsers = new android.util.ArraySet<>();
        java.util.List<android.content.pm.UserInfo> users = this.mInjector.getUserManager().getUsers(true);
        for (int i = 0; i < users.size(); i++) {
            int userId = users.get(i).id;
            if (isAlwaysVisibleUser(userId)) {
                exemptedUsers.add(java.lang.Integer.valueOf(userId));
            }
        }
        synchronized (this.mLock) {
            stopExcessRunningUsersLU(this.mMaxRunningUsers, exemptedUsers);
        }
    }

    private void stopExcessRunningUsersLU(int maxRunningUsers, android.util.ArraySet<java.lang.Integer> exemptedUsers) {
        java.util.List<java.lang.Integer> currentlyRunningLru = getRunningUsersLU();
        java.util.Iterator<java.lang.Integer> iterator = currentlyRunningLru.iterator();
        while (currentlyRunningLru.size() > maxRunningUsers && iterator.hasNext()) {
            java.lang.Integer userId = iterator.next();
            if (userId.intValue() != 0 && userId.intValue() != this.mCurrentUserId && !exemptedUsers.contains(userId) && !this.mUserControllerExt.checkUserIfNeed(userId.intValue())) {
                com.android.server.utils.Slogf.i("ActivityManager", "Too many running users (%d). Attempting to stop user %d", java.lang.Integer.valueOf(currentlyRunningLru.size()), userId);
                if (stopUsersLU(userId.intValue(), false, true, null, null) == 0) {
                    iterator.remove();
                }
            }
        }
    }

    boolean canStartMoreUsers() {
        boolean z;
        synchronized (this.mLock) {
            z = getRunningUsersLU().size() < this.mMaxRunningUsers;
        }
        return z;
    }

    private void finishUserBoot(com.android.server.am.UserState uss) throws java.lang.Throwable {
        finishUserBoot(uss, null);
    }

    private void finishUserBoot(com.android.server.am.UserState uss, android.content.IIntentReceiver resultTo) throws java.lang.Throwable {
        int userId = uss.mHandle.getIdentifier();
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_FINISH_USER_BOOT, userId);
        synchronized (this.mLock) {
            if (this.mStartedUsers.get(userId) != uss) {
                return;
            }
            if (uss.setState(0, 1)) {
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(userId, 4, 0);
                this.mInjector.getUserManagerInternal().setUserState(userId, uss.state);
                if (userId == 0 && !this.mInjector.isRuntimeRestarted() && !this.mInjector.isFirstBootOrUpgrade()) {
                    long elapsedTimeMs = android.os.SystemClock.elapsedRealtime();
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 12, elapsedTimeMs);
                    if (elapsedTimeMs > 120000) {
                        com.android.server.utils.Slogf.wtf(com.android.server.utils.TimingsTraceAndSlog.SYSTEM_SERVER_TIMING_TAG, "finishUserBoot took too long. elapsedTimeMs=" + elapsedTimeMs);
                    }
                }
                if (!this.mInjector.getUserManager().isPreCreated(userId)) {
                    this.mHandler.sendMessage(this.mHandler.obtainMessage(110, userId, 0));
                    if (this.mAllowUserUnlocking) {
                        sendLockedBootCompletedBroadcast(resultTo, userId);
                    }
                }
            }
            android.content.pm.UserInfo parent = this.mInjector.getUserManager().getProfileParent(userId);
            if (parent == null) {
                maybeUnlockUser(userId);
            } else if (isUserRunning(parent.id, 4)) {
                com.android.server.utils.Slogf.d("ActivityManager", "User " + userId + " (parent " + parent.id + "): attempting unlock because parent is unlocked");
                maybeUnlockUser(userId);
            } else {
                com.android.server.utils.Slogf.d("ActivityManager", "User " + userId + " (parent " + parent.id + "): delaying unlock because parent is locked");
            }
        }
    }

    private void sendLockedBootCompletedBroadcast(android.content.IIntentReceiver receiver, int userId) throws java.lang.Throwable {
        android.content.pm.UserInfo userInfo;
        if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures() && (userInfo = getUserInfo(userId)) != null && userInfo.isPrivateProfile()) {
            com.android.server.utils.Slogf.i("ActivityManager", "Skipping LOCKED_BOOT_COMPLETED for private profile user #" + userId);
            return;
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.LOCKED_BOOT_COMPLETED", (android.net.Uri) null);
        intent.putExtra("android.intent.extra.user_handle", userId);
        intent.addFlags(-1996488704);
        this.mInjector.broadcastIntent(intent, null, receiver, 0, null, null, new java.lang.String[]{"android.permission.RECEIVE_BOOT_COMPLETED"}, -1, getTemporaryAppAllowlistBroadcastOptions(202).toBundle(), false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), userId);
        this.mUserControllerExt.hookAgingUserBoot(userId);
    }

    private boolean finishUserUnlocking(final com.android.server.am.UserState uss) {
        final int userId = uss.mHandle.getIdentifier();
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_FINISH_USER_UNLOCKING, userId);
        this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(userId, 5, 1);
        if (!android.os.storage.StorageManager.isCeStorageUnlocked(userId)) {
            return false;
        }
        synchronized (this.mLock) {
            if (this.mStartedUsers.get(userId) == uss && uss.state == 1) {
                uss.mUnlockProgress.start();
                uss.mUnlockProgress.setProgress(5, this.mInjector.getContext().getString(android.R.string.aerr_wait));
                this.mUserControllerExt.hookFgHandler(com.android.server.FgThread.getHandler()).post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$finishUserUnlocking$1(userId, uss);
                    }
                });
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserUnlocking$1(int userId, com.android.server.am.UserState uss) {
        if (!android.os.storage.StorageManager.isCeStorageUnlocked(userId)) {
            com.android.server.utils.Slogf.w("ActivityManager", "User's CE storage got locked unexpectedly, leaving user locked.");
            return;
        }
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("UM.onBeforeUnlockUser-" + userId);
        this.mUserControllerExt.ormsUnlockUserBoost(2000);
        this.mInjector.getUserManager().onBeforeUnlockUser(userId);
        t.traceEnd();
        synchronized (this.mLock) {
            if (uss.setState(1, 2)) {
                this.mInjector.getUserManagerInternal().setUserState(userId, uss.state);
                uss.mUnlockProgress.setProgress(20);
                this.mUserControllerExt.ormsUnlockUserBoost(30000);
                this.mLastUserUnlockingUptime = android.os.SystemClock.uptimeMillis();
                this.mHandler.obtainMessage(100, userId, 0, uss).sendToTarget();
            }
        }
    }

    private void finishUserUnlocked(final com.android.server.am.UserState uss) throws java.lang.Throwable {
        int userId;
        android.content.pm.UserInfo parent;
        int userId2 = uss.mHandle.getIdentifier();
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_FINISH_USER_UNLOCKED, userId2);
        if (!android.os.storage.StorageManager.isCeStorageUnlocked(userId2)) {
            return;
        }
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mStartedUsers.get(uss.mHandle.getIdentifier()) != uss) {
                        return;
                    }
                    if (uss.setState(2, 3)) {
                        this.mInjector.getUserManagerInternal().setUserState(userId2, uss.state);
                        uss.mUnlockProgress.finish();
                        this.mUserControllerExt.reUnlockMultiAppUser(userId2);
                        this.mUserControllerExt.setUnlockedForDexopt();
                        if (userId2 == 0) {
                            this.mInjector.startPersistentApps(262144);
                        }
                        this.mInjector.installEncryptionUnawareProviders(userId2);
                        if (this.mInjector.getUserManager().isPreCreated(userId2)) {
                            userId = userId2;
                        } else {
                            android.content.Intent unlockedIntent = new android.content.Intent("android.intent.action.USER_UNLOCKED");
                            unlockedIntent.putExtra("android.intent.extra.user_handle", userId2);
                            unlockedIntent.addFlags(1342177280);
                            userId = userId2;
                            this.mInjector.broadcastIntent(unlockedIntent, null, null, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), userId);
                        }
                        int userId3 = userId;
                        android.content.pm.UserInfo userInfo = getUserInfo(userId3);
                        if (userInfo.isProfile() && (parent = this.mInjector.getUserManager().getProfileParent(userId3)) != null) {
                            broadcastProfileAccessibleStateChanged(userId3, parent.id, "android.intent.action.PROFILE_ACCESSIBLE");
                            if (userInfo.isManagedProfile()) {
                                android.content.Intent profileUnlockedIntent = new android.content.Intent("android.intent.action.MANAGED_PROFILE_UNLOCKED");
                                profileUnlockedIntent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId3));
                                profileUnlockedIntent.addFlags(1342177280);
                                this.mInjector.broadcastIntent(profileUnlockedIntent, null, null, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), parent.id);
                            }
                        }
                        android.content.pm.UserInfo info = getUserInfo(userId3);
                        if (!java.util.Objects.equals(info.lastLoggedInFingerprint, android.content.pm.PackagePartitions.FINGERPRINT) || android.os.SystemProperties.getBoolean("persist.pm.mock-upgrade", false)) {
                            synchronized (this.mLock) {
                                if (this.mStartedUsers.get(uss.mHandle.getIdentifier()) != uss) {
                                    return;
                                }
                                android.content.pm.UserInfo tmpUserInfo = getUserInfo(userId3);
                                if (tmpUserInfo == null || !android.os.storage.StorageManager.isCeStorageUnlocked(userId3)) {
                                    return;
                                }
                                this.mInjector.startUserWidgets(userId3);
                                boolean quiet = info.isManagedProfile();
                                this.mInjector.sendPreBootBroadcast(userId3, quiet, new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda22
                                    @Override // java.lang.Runnable
                                    public final void run() throws java.lang.Throwable {
                                        this.f$0.lambda$finishUserUnlocked$2(uss);
                                    }
                                });
                                return;
                            }
                        }
                        lambda$finishUserUnlocked$2(uss);
                        return;
                    }
                    return;
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
            while (true) {
                try {
                    throw th;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: finishUserUnlockedCompleted, reason: merged with bridge method [inline-methods] */
    public void lambda$finishUserUnlocked$2(com.android.server.am.UserState uss) throws java.lang.Throwable {
        final int userId = uss.mHandle.getIdentifier();
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_FINISH_USER_UNLOCKED_COMPLETED, userId);
        synchronized (this.mLock) {
            try {
                if (this.mStartedUsers.get(uss.mHandle.getIdentifier()) != uss) {
                    try {
                        return;
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                } else {
                    final android.content.pm.UserInfo userInfo = getUserInfo(userId);
                    if (userInfo != null && android.os.storage.StorageManager.isCeStorageUnlocked(userId)) {
                        this.mInjector.getUserManager().onUserLoggedIn(userId);
                        final java.lang.Runnable initializeUser = new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$finishUserUnlockedCompleted$3(userInfo);
                            }
                        };
                        if (!userInfo.isInitialized()) {
                            com.android.server.utils.Slogf.d("ActivityManager", "Initializing user #" + userId);
                            if (userInfo.preCreated) {
                                initializeUser.run();
                            } else if (userId != 0) {
                                android.content.Intent intent = new android.content.Intent("android.intent.action.USER_INITIALIZE");
                                intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB);
                                this.mInjector.broadcastIntent(intent, null, new android.content.IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.2
                                    public void performReceive(android.content.Intent intent2, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                                        initializeUser.run();
                                    }
                                }, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), userId);
                            }
                        }
                        if (userInfo.preCreated) {
                            com.android.server.utils.Slogf.i("ActivityManager", "Stopping pre-created user " + userInfo.toFullString());
                            stopUser(userInfo.id, false, null, null);
                            return;
                        }
                        this.mInjector.startUserWidgets(userId);
                        this.mHandler.obtainMessage(105, userId, 0).sendToTarget();
                        if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures() && userInfo.isPrivateProfile()) {
                            com.android.server.utils.Slogf.i("ActivityManager", "Skipping BOOT_COMPLETED for private profile user #" + userId);
                            return;
                        }
                        com.android.server.utils.Slogf.i("ActivityManager", "Posting BOOT_COMPLETED user #" + userId);
                        if (userId == 0 && !this.mInjector.isRuntimeRestarted() && !this.mInjector.isFirstBootOrUpgrade()) {
                            long elapsedTimeMs = android.os.SystemClock.elapsedRealtime();
                            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, 13, elapsedTimeMs);
                        }
                        final android.content.Intent bootIntent = new android.content.Intent("android.intent.action.BOOT_COMPLETED", (android.net.Uri) null);
                        bootIntent.putExtra("android.intent.extra.user_handle", userId);
                        bootIntent.addFlags(-1996488704);
                        final int callingUid = android.os.Binder.getCallingUid();
                        final int callingPid = android.os.Binder.getCallingPid();
                        this.mUserControllerExt.hookFgHandler(com.android.server.FgThread.getHandler()).post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() throws java.lang.Throwable {
                                this.f$0.lambda$finishUserUnlockedCompleted$4(bootIntent, userId, callingUid, callingPid);
                            }
                        });
                        this.mUserControllerExt.triggerBootCompleteBroadcast(userId);
                        this.mUserControllerExt.recordRootState();
                        this.mUserControllerExt.hookAgingUserUnlockedCompleted(userId);
                        return;
                    }
                    return;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
            while (true) {
                try {
                    throw th;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserUnlockedCompleted$3(android.content.pm.UserInfo userInfo) {
        this.mInjector.getUserManager().makeInitialized(userInfo.id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserUnlockedCompleted$4(android.content.Intent bootIntent, final int userId, int callingUid, int callingPid) throws java.lang.Throwable {
        this.mInjector.broadcastIntent(bootIntent, null, new android.content.IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.3
            public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) throws android.os.RemoteException {
                com.android.server.utils.Slogf.i("ActivityManager", "Finished processing BOOT_COMPLETED for u" + userId);
                com.android.server.am.UserController.this.mBootCompleted = true;
            }
        }, 0, null, null, new java.lang.String[]{"android.permission.RECEIVE_BOOT_COMPLETED"}, -1, getTemporaryAppAllowlistBroadcastOptions(200).toBundle(), false, com.android.server.am.ActivityManagerService.MY_PID, 1000, callingUid, callingPid, userId);
    }

    /* JADX INFO: renamed from: com.android.server.am.UserController$4, reason: invalid class name */
    class AnonymousClass4 implements com.android.server.am.UserState.KeyEvictedCallback {
        final /* synthetic */ int val$userStartMode;

        AnonymousClass4(int i) {
            this.val$userStartMode = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$keyEvicted$0(int userId, int userStartMode) {
            com.android.server.am.UserController.this.startUser(userId, userStartMode);
        }

        @Override // com.android.server.am.UserState.KeyEvictedCallback
        public void keyEvicted(final int userId) {
            android.os.Handler handler = com.android.server.am.UserController.this.mHandler;
            final int i = this.val$userStartMode;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$keyEvicted$0(userId, i);
                }
            });
        }
    }

    int restartUser(int userId, int userStartMode) {
        return stopUser(userId, false, null, new com.android.server.am.UserController.AnonymousClass4(userStartMode));
    }

    boolean stopProfile(int userId) {
        boolean z;
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") == -1 && this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == -1) {
            throw new java.lang.SecurityException("You either need MANAGE_USERS or INTERACT_ACROSS_USERS_FULL permission to stop a profile");
        }
        android.content.pm.UserInfo userInfo = getUserInfo(userId);
        if (userInfo == null || !userInfo.isProfile()) {
            throw new java.lang.IllegalArgumentException("User " + userId + " is not a profile");
        }
        enforceShellRestriction("no_debugging_features", userId);
        synchronized (this.mLock) {
            z = stopUsersLU(userId, false, null, null) == 0;
        }
        return z;
    }

    int stopUser(int userId, boolean allowDelayedLocking, android.app.IStopUserCallback stopUserCallback, com.android.server.am.UserState.KeyEvictedCallback keyEvictedCallback) {
        return stopUser(userId, true, allowDelayedLocking, stopUserCallback, keyEvictedCallback);
    }

    int stopUser(int userId, boolean stopProfileRegardlessOfParent, boolean allowDelayedLocking, android.app.IStopUserCallback stopUserCallback, com.android.server.am.UserState.KeyEvictedCallback keyEvictedCallback) {
        int iStopUsersLU;
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("UserController" + (stopProfileRegardlessOfParent ? "-stopProfileRegardlessOfParent" : "") + (allowDelayedLocking ? "-allowDelayedLocking" : "") + (stopUserCallback != null ? "-withStopUserCallback" : "") + "-" + userId + "-[stopUser]");
        try {
            checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "stopUser");
            com.android.internal.util.Preconditions.checkArgument(userId >= 0, "Invalid user id %d", new java.lang.Object[]{java.lang.Integer.valueOf(userId)});
            enforceShellRestriction("no_debugging_features", userId);
            synchronized (this.mLock) {
                iStopUsersLU = stopUsersLU(userId, stopProfileRegardlessOfParent, allowDelayedLocking, stopUserCallback, keyEvictedCallback);
            }
            return iStopUsersLU;
        } finally {
            t.traceEnd();
        }
    }

    private int stopUsersLU(int userId, boolean allowDelayedLocking, android.app.IStopUserCallback stopUserCallback, com.android.server.am.UserState.KeyEvictedCallback keyEvictedCallback) {
        return stopUsersLU(userId, true, allowDelayedLocking, stopUserCallback, keyEvictedCallback);
    }

    private int stopUsersLU(int userId, boolean stopProfileRegardlessOfParent, boolean allowDelayedLocking, android.app.IStopUserCallback stopUserCallback, com.android.server.am.UserState.KeyEvictedCallback keyEvictedCallback) {
        int parentId;
        if (userId == 0) {
            return -3;
        }
        if (isCurrentUserLU(userId)) {
            return -2;
        }
        if (this.mUserControllerExt.checkUserIfNeed(userId)) {
            return -4;
        }
        if (!stopProfileRegardlessOfParent && (parentId = this.mUserProfileGroupIds.get(userId, -10000)) != -10000 && parentId != userId && (parentId == 0 || isCurrentUserLU(parentId))) {
            return -4;
        }
        int[] usersToStop = getUsersToStopLU(userId);
        for (int relatedUserId : usersToStop) {
            if (relatedUserId == 0 || isCurrentUserLU(relatedUserId)) {
                com.android.server.utils.Slogf.e("ActivityManager", "Cannot stop user %d because it is related to user %d. ", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(relatedUserId));
                return -4;
            }
        }
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.i("ActivityManager", "stopUsersLocked usersToStop=" + java.util.Arrays.toString(usersToStop));
        }
        int length = usersToStop.length;
        for (int i = 0; i < length; i++) {
            int userIdToStop = usersToStop[i];
            t.traceBegin("stopSingleUserLU-" + userIdToStop + "-[stopUser]");
            com.android.server.am.UserState.KeyEvictedCallback keyEvictedCallback2 = null;
            android.app.IStopUserCallback iStopUserCallback = userIdToStop == userId ? stopUserCallback : null;
            if (userIdToStop == userId) {
                keyEvictedCallback2 = keyEvictedCallback;
            }
            stopSingleUserLU(userIdToStop, allowDelayedLocking, iStopUserCallback, keyEvictedCallback2);
            t.traceEnd();
        }
        return 0;
    }

    private void stopSingleUserLU(final int userId, final boolean allowDelayedLocking, final android.app.IStopUserCallback stopUserCallback, com.android.server.am.UserState.KeyEvictedCallback keyEvictedCallback) {
        java.util.ArrayList<com.android.server.am.UserState.KeyEvictedCallback> keyEvictedCallbacks;
        com.android.server.utils.Slogf.i("ActivityManager", "stopSingleUserLU userId=" + userId);
        if (android.multiuser.Flags.scheduleStopOfBackgroundUser()) {
            this.mHandler.removeEqualMessages(150, java.lang.Integer.valueOf(userId));
        }
        final com.android.server.am.UserState uss = this.mStartedUsers.get(userId);
        this.mUserControllerExt.userRemoved(userId);
        if (uss == null) {
            if (canDelayDataLockingForUser(userId)) {
                if (allowDelayedLocking && keyEvictedCallback != null) {
                    com.android.server.utils.Slogf.wtf("ActivityManager", "allowDelayedLocking set with KeyEvictedCallback, ignore it and lock user:" + userId, new java.lang.RuntimeException());
                    allowDelayedLocking = false;
                }
                if (!allowDelayedLocking && this.mLastActiveUsersForDelayedLocking.remove(java.lang.Integer.valueOf(userId))) {
                    if (keyEvictedCallback != null) {
                        keyEvictedCallbacks = new java.util.ArrayList<>(1);
                        keyEvictedCallbacks.add(keyEvictedCallback);
                    } else {
                        keyEvictedCallbacks = null;
                    }
                    dispatchUserLocking(userId, keyEvictedCallbacks);
                }
            }
            if (stopUserCallback != null) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        stopUserCallback.userStopped(userId);
                    }
                });
                return;
            }
            return;
        }
        logUserJourneyBegin(userId, 5);
        if (stopUserCallback != null) {
            uss.mStopCallbacks.add(stopUserCallback);
        }
        if (keyEvictedCallback != null) {
            uss.mKeyEvictedCallbacks.add(keyEvictedCallback);
        }
        if (uss.state != 4 && uss.state != 5) {
            uss.setState(4);
            com.android.server.pm.UserManagerInternal userManagerInternal = this.mInjector.getUserManagerInternal();
            com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
            t.traceBegin("setUserState-STATE_STOPPING-" + userId + "-[stopUser]");
            userManagerInternal.setUserState(userId, uss.state);
            t.traceEnd();
            t.traceBegin("unassignUserFromDisplayOnStop-" + userId + "-[stopUser]");
            userManagerInternal.unassignUserFromDisplayOnStop(userId);
            t.traceEnd();
            updateStartedUserArrayLU();
            final java.lang.Runnable finishUserStoppingAsync = new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$stopSingleUserLU$7(userId, uss, allowDelayedLocking);
                }
            };
            if (this.mInjector.getUserManager().isPreCreated(userId)) {
                finishUserStoppingAsync.run();
            } else {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() throws java.lang.Throwable {
                        this.f$0.lambda$stopSingleUserLU$8(userId, finishUserStoppingAsync);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopSingleUserLU$7(final int userId, final com.android.server.am.UserState uss, final boolean allowDelayedLockingCopied) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$stopSingleUserLU$6(userId, uss, allowDelayedLockingCopied);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopSingleUserLU$6(int userId, com.android.server.am.UserState uss, boolean allowDelayedLockingCopied) throws java.lang.Throwable {
        com.android.server.utils.TimingsTraceAndSlog t2 = new com.android.server.utils.TimingsTraceAndSlog();
        t2.traceBegin("finishUserStopping-" + userId + "-[stopUser]");
        finishUserStopping(userId, uss, allowDelayedLockingCopied);
        t2.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopSingleUserLU$8(final int userId, final java.lang.Runnable finishUserStoppingAsync) throws java.lang.Throwable {
        android.content.Intent stoppingIntent = new android.content.Intent("android.intent.action.USER_STOPPING");
        stoppingIntent.addFlags(1073741824);
        stoppingIntent.putExtra("android.intent.extra.user_handle", userId);
        stoppingIntent.putExtra("android.intent.extra.SHUTDOWN_USERSPACE_ONLY", true);
        android.content.IIntentReceiver stoppingReceiver = new android.content.IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.5
            public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                com.android.server.am.UserController.asyncTraceEnd("broadcast-ACTION_USER_STOPPING-" + userId + "-[stopUser]", userId);
                finishUserStoppingAsync.run();
            }
        };
        com.android.server.utils.TimingsTraceAndSlog t2 = new com.android.server.utils.TimingsTraceAndSlog();
        t2.traceBegin("clearBroadcastQueueForUser-" + userId + "-[stopUser]");
        this.mInjector.clearBroadcastQueueForUser(userId);
        t2.traceEnd();
        asyncTraceBegin("broadcast-ACTION_USER_STOPPING-" + userId + "-[stopUser]", userId);
        this.mInjector.broadcastIntent(stoppingIntent, null, stoppingReceiver, 0, null, null, new java.lang.String[]{"android.permission.INTERACT_ACROSS_USERS"}, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), -1);
    }

    private void finishUserStopping(final int userId, final com.android.server.am.UserState uss, final boolean allowDelayedLocking) throws java.lang.Throwable {
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_FINISH_USER_STOPPING, userId);
        synchronized (this.mLock) {
            if (uss.state != 4) {
                com.android.server.pm.UserJourneyLogger.UserJourneySession session = this.mInjector.getUserJourneyLogger().logUserJourneyFinishWithError(-1, getUserInfo(userId), 5, 3);
                if (session != null) {
                    this.mHandler.removeMessages(200, session);
                } else {
                    this.mInjector.getUserJourneyLogger().logUserJourneyFinishWithError(-1, getUserInfo(userId), 5, 0);
                }
                return;
            }
            uss.setState(5);
            com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
            t.traceBegin("setUserState-STATE_SHUTDOWN-" + userId + "-[stopUser]");
            this.mInjector.getUserManagerInternal().setUserState(userId, uss.state);
            t.traceEnd();
            this.mInjector.batteryStatsServiceNoteEvent(16391, java.lang.Integer.toString(userId), userId);
            this.mInjector.getSystemServiceManager().onUserStopping(userId);
            final java.lang.Runnable finishUserStoppedAsync = new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$finishUserStopping$10(userId, uss, allowDelayedLocking);
                }
            };
            if (this.mInjector.getUserManager().isPreCreated(userId)) {
                finishUserStoppedAsync.run();
                return;
            }
            android.content.Intent shutdownIntent = new android.content.Intent("android.intent.action.ACTION_SHUTDOWN");
            android.content.IIntentReceiver shutdownReceiver = new android.content.IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.6
                public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                    com.android.server.am.UserController.asyncTraceEnd("broadcast-ACTION_SHUTDOWN-" + userId + "-[stopUser]", userId);
                    finishUserStoppedAsync.run();
                }
            };
            asyncTraceBegin("broadcast-ACTION_SHUTDOWN-" + userId + "-[stopUser]", userId);
            this.mInjector.broadcastIntent(shutdownIntent, null, shutdownReceiver, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserStopping$10(final int userId, final com.android.server.am.UserState uss, final boolean allowDelayedLocking) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$finishUserStopping$9(userId, uss, allowDelayedLocking);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUserStopping$9(int userId, com.android.server.am.UserState uss, boolean allowDelayedLocking) throws java.lang.Throwable {
        com.android.server.utils.TimingsTraceAndSlog t2 = new com.android.server.utils.TimingsTraceAndSlog();
        t2.traceBegin("finishUserStopped-" + userId + "-[stopUser]");
        finishUserStopped(uss, allowDelayedLocking);
        t2.traceEnd();
    }

    void finishUserStopped(com.android.server.am.UserState uss, boolean allowDelayedLocking) throws java.lang.Throwable {
        java.util.ArrayList<android.app.IStopUserCallback> stopCallbacks;
        java.util.ArrayList<com.android.server.am.UserState.KeyEvictedCallback> keyEvictedCallbacks;
        boolean stopped;
        int userId = uss.mHandle.getIdentifier();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.i("ActivityManager", "finishUserStopped(%d): allowDelayedLocking=%b", java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(allowDelayedLocking));
        }
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_FINISH_USER_STOPPED, userId);
        boolean lockUser = true;
        int userIdToLock = userId;
        android.content.pm.UserInfo userInfo = getUserInfo(userId);
        synchronized (this.mLock) {
            stopCallbacks = new java.util.ArrayList<>(uss.mStopCallbacks);
            keyEvictedCallbacks = new java.util.ArrayList<>(uss.mKeyEvictedCallbacks);
            if (this.mStartedUsers.get(userId) != uss || uss.state != 5) {
                stopped = false;
            } else {
                stopped = true;
                com.android.server.utils.Slogf.i("ActivityManager", "Removing user state from UserController.mStartedUsers for user #" + userId + " as a result of user being stopped");
                this.mStartedUsers.remove(userId);
                this.mMaxRunningUsers = this.mUserControllerExt.decreaseCountIfNeed(this.mMaxRunningUsers, userId);
                this.mUserLru.remove(java.lang.Integer.valueOf(userId));
                updateStartedUserArrayLU();
                if (allowDelayedLocking && !keyEvictedCallbacks.isEmpty()) {
                    com.android.server.utils.Slogf.wtf("ActivityManager", "Delayed locking enabled while KeyEvictedCallbacks not empty, userId:" + userId + " callbacks:" + keyEvictedCallbacks);
                    allowDelayedLocking = false;
                }
                userIdToLock = updateUserToLockLU(userId, allowDelayedLocking);
                if (userIdToLock == -10000) {
                    lockUser = false;
                }
            }
        }
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        if (stopped) {
            com.android.server.utils.Slogf.i("ActivityManager", "Removing user state from UserManager.mUserStates for user #" + userId + " as a result of user being stopped");
            this.mInjector.getUserManagerInternal().removeUserState(userId);
            this.mInjector.activityManagerOnUserStopped(userId);
            t.traceBegin("stopPackagesOfStoppedUser-" + userId + "-[stopUser]");
            stopPackagesOfStoppedUser(userId, "finish user");
            t.traceEnd();
        }
        for (android.app.IStopUserCallback callback : stopCallbacks) {
            if (stopped) {
                try {
                    t.traceBegin("stopCallbacks.userStopped-" + userId + "-[stopUser]");
                    callback.userStopped(userId);
                    t.traceEnd();
                } catch (android.os.RemoteException e) {
                }
            } else {
                t.traceBegin("stopCallbacks.userStopAborted-" + userId + "-[stopUser]");
                callback.userStopAborted(userId);
                t.traceEnd();
            }
        }
        if (stopped) {
            t.traceBegin("systemServiceManagerOnUserStopped-" + userId + "-[stopUser]");
            this.mInjector.systemServiceManagerOnUserStopped(userId);
            t.traceEnd();
            t.traceBegin("taskSupervisorRemoveUser-" + userId + "-[stopUser]");
            this.mInjector.taskSupervisorRemoveUser(userId);
            t.traceEnd();
            if (userInfo.isEphemeral() && !userInfo.preCreated) {
                this.mInjector.getUserManager().removeUserEvenWhenDisallowed(userId);
            }
            com.android.server.pm.UserJourneyLogger.UserJourneySession session = this.mInjector.getUserJourneyLogger().logUserJourneyFinish(-1, userInfo, 5);
            if (session != null) {
                this.mHandler.removeMessages(200, session);
            }
            if (lockUser) {
                dispatchUserLocking(userIdToLock, keyEvictedCallbacks);
            }
            resumePendingUserStarts(userId);
            return;
        }
        com.android.server.pm.UserJourneyLogger.UserJourneySession session2 = this.mInjector.getUserJourneyLogger().finishAndClearIncompleteUserJourney(userId, 5);
        if (session2 != null) {
            this.mHandler.removeMessages(200, session2);
        }
    }

    private void resumePendingUserStarts(int userId) {
        synchronized (this.mLock) {
            java.util.List<com.android.server.am.UserController.PendingUserStart> handledUserStarts = new java.util.ArrayList<>();
            for (final com.android.server.am.UserController.PendingUserStart userStart : this.mPendingUserStarts) {
                if (userStart.userId == userId) {
                    com.android.server.utils.Slogf.i("ActivityManager", "resumePendingUserStart for" + userStart);
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$resumePendingUserStarts$11(userStart);
                        }
                    });
                    handledUserStarts.add(userStart);
                }
            }
            this.mPendingUserStarts.removeAll(handledUserStarts);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resumePendingUserStarts$11(com.android.server.am.UserController.PendingUserStart userStart) {
        startUser(userStart.userId, userStart.userStartMode, userStart.unlockListener);
    }

    private void dispatchUserLocking(final int userId, final java.util.List<com.android.server.am.UserState.KeyEvictedCallback> keyEvictedCallbacks) {
        this.mUserControllerExt.hookFgHandler(com.android.server.FgThread.getHandler()).post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dispatchUserLocking$12(userId, keyEvictedCallbacks);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchUserLocking$12(int userId, java.util.List keyEvictedCallbacks) {
        synchronized (this.mLock) {
            if (this.mStartedUsers.get(userId) != null) {
                com.android.server.utils.Slogf.w("ActivityManager", "User was restarted, skipping key eviction");
                return;
            }
            try {
                com.android.server.utils.Slogf.i("ActivityManager", "Locking CE storage for user #" + userId);
                this.mInjector.getStorageManager().lockCeStorage(userId);
                if (keyEvictedCallbacks == null) {
                    return;
                }
                for (int i = 0; i < keyEvictedCallbacks.size(); i++) {
                    ((com.android.server.am.UserState.KeyEvictedCallback) keyEvictedCallbacks.get(i)).keyEvicted(userId);
                }
            } catch (android.os.RemoteException re) {
                throw re.rethrowAsRuntimeException();
            }
        }
    }

    private int updateUserToLockLU(int userId, boolean allowDelayedLocking) {
        if (!canDelayDataLockingForUser(userId) || !allowDelayedLocking || getUserInfo(userId).isEphemeral() || hasUserRestriction("no_run_in_background", userId)) {
            return userId;
        }
        if (this.mDelayUserDataLocking) {
            this.mLastActiveUsersForDelayedLocking.remove(java.lang.Integer.valueOf(userId));
            this.mLastActiveUsersForDelayedLocking.add(0, java.lang.Integer.valueOf(userId));
            int totalUnlockedUsers = this.mStartedUsers.size() + this.mLastActiveUsersForDelayedLocking.size();
            if (totalUnlockedUsers > this.mMaxRunningUsers) {
                int userIdToLock = this.mLastActiveUsersForDelayedLocking.get(this.mLastActiveUsersForDelayedLocking.size() - 1).intValue();
                this.mLastActiveUsersForDelayedLocking.remove(this.mLastActiveUsersForDelayedLocking.size() - 1);
                com.android.server.utils.Slogf.i("ActivityManager", "finishUserStopped: should stop user " + userId + " but should lock user " + userIdToLock);
                return userIdToLock;
            }
        }
        com.android.server.utils.Slogf.i("ActivityManager", "finishUserStopped: should stop user " + userId + " but without any locking");
        return -10000;
    }

    private boolean canDelayDataLockingForUser(int userIdToLock) {
        if (allowBiometricUnlockForPrivateProfile()) {
            android.content.pm.UserProperties userProperties = getUserProperties(userIdToLock);
            return this.mDelayUserDataLocking || (userProperties != null && userProperties.getAllowStoppingUserWithDelayedLocking());
        }
        return this.mDelayUserDataLocking;
    }

    private boolean allowBiometricUnlockForPrivateProfile() {
        return com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enableBiometricsToUnlockPrivateSpace() && android.multiuser.Flags.enablePrivateSpaceFeatures();
    }

    private int[] getUsersToStopLU(int userId) {
        int startedUsersSize = this.mStartedUsers.size();
        android.util.IntArray userIds = new android.util.IntArray();
        userIds.add(userId);
        int userGroupId = this.mUserProfileGroupIds.get(userId, -10000);
        if (userGroupId == userId) {
            for (int i = 0; i < startedUsersSize; i++) {
                com.android.server.am.UserState uss = this.mStartedUsers.valueAt(i);
                int startedUserId = uss.mHandle.getIdentifier();
                int startedUserGroupId = this.mUserProfileGroupIds.get(startedUserId, -10000);
                boolean sameGroup = userGroupId != -10000 && userGroupId == startedUserGroupId;
                boolean sameUserId = startedUserId == userId;
                if (sameGroup && !sameUserId) {
                    userIds.add(startedUserId);
                }
            }
        }
        return userIds.toArray();
    }

    private void stopPackagesOfStoppedUser(int userId, java.lang.String reason) throws java.lang.Throwable {
        android.content.pm.UserInfo parent;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.i("ActivityManager", "stopPackagesOfStoppedUser(%d): %s", java.lang.Integer.valueOf(userId), reason);
        }
        this.mInjector.activityManagerForceStopPackage(userId, reason);
        if (this.mInjector.getUserManager().isPreCreated(userId)) {
            return;
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.USER_STOPPED");
        intent.addFlags(1342177280);
        intent.putExtra("android.intent.extra.user_handle", userId);
        this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), -1);
        android.content.pm.UserInfo userInfo = getUserInfo(userId);
        if (userInfo != null && userInfo.isProfile() && (parent = this.mInjector.getUserManager().getProfileParent(userId)) != null) {
            broadcastProfileAccessibleStateChanged(userId, parent.id, "android.intent.action.PROFILE_INACCESSIBLE");
        }
    }

    private void stopGuestOrEphemeralUserIfBackground(int oldUserId) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.i("ActivityManager", "Stop guest or ephemeral user if background: " + oldUserId);
        }
        synchronized (this.mLock) {
            com.android.server.am.UserState oldUss = this.mStartedUsers.get(oldUserId);
            if (oldUserId != 0 && oldUserId != this.mCurrentUserId && oldUss != null && oldUss.state != 4 && oldUss.state != 5) {
                android.content.pm.UserInfo userInfo = getUserInfo(oldUserId);
                if (userInfo.isEphemeral()) {
                    ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).onEphemeralUserStop(oldUserId);
                }
                if (userInfo.isGuest() || userInfo.isEphemeral()) {
                    com.android.server.utils.Slogf.i("ActivityManager", "Stopping background guest or ephemeral user " + oldUserId);
                    synchronized (this.mLock) {
                        stopUsersLU(oldUserId, false, null, null);
                    }
                }
            }
        }
    }

    void scheduleStartProfiles() {
        this.mUserControllerExt.hookFgHandler(com.android.server.FgThread.getHandler()).post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleStartProfiles$13();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleStartProfiles$13() {
        if (!this.mHandler.hasMessages(40)) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(40), 1000L);
        }
    }

    private void startProfiles() {
        int currentUserId = getCurrentUserId();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.i("ActivityManager", "startProfilesLocked");
        }
        java.util.List<android.content.pm.UserInfo> profiles = this.mInjector.getUserManager().getProfiles(currentUserId, false);
        java.util.List<android.content.pm.UserInfo> profilesToStart = new java.util.ArrayList<>(profiles.size());
        for (android.content.pm.UserInfo user : profiles) {
            if ((user.flags & 16) == 16 && user.id != currentUserId && shouldStartWithParent(user)) {
                profilesToStart.add(user);
            }
        }
        int profilesToStartSize = profilesToStart.size();
        int i = 0;
        while (i < profilesToStartSize && i < this.mUserControllerExt.modifyIfWorkProfileExist(getMaxRunningUsers(), profilesToStart) - 1) {
            startUser(profilesToStart.get(i).id, 3);
            i++;
        }
        if (i < profilesToStartSize) {
            com.android.server.utils.Slogf.w("ActivityManager", "More profiles than MAX_RUNNING_USERS");
        }
    }

    private boolean shouldStartWithParent(android.content.pm.UserInfo user) {
        android.content.pm.UserProperties properties = getUserProperties(user.id);
        return (properties == null || !properties.getStartWithParent() || user.isQuietModeEnabled()) ? false : true;
    }

    boolean startProfile(int userId, boolean evenWhenDisabled, android.os.IProgressListener unlockListener) {
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") == -1 && this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == -1) {
            throw new java.lang.SecurityException("You either need MANAGE_USERS or INTERACT_ACROSS_USERS_FULL permission to start a profile");
        }
        android.content.pm.UserInfo userInfo = getUserInfo(userId);
        if (userInfo == null || !userInfo.isProfile()) {
            throw new java.lang.IllegalArgumentException("User " + userId + " is not a profile");
        }
        if (!userInfo.isEnabled() && !evenWhenDisabled) {
            com.android.server.utils.Slogf.w("ActivityManager", "Cannot start disabled profile #%d", java.lang.Integer.valueOf(userId));
            return false;
        }
        return startUserNoChecks(userId, 0, 3, unlockListener);
    }

    boolean startUser(int userId, int userStartMode) {
        return startUser(userId, userStartMode, null);
    }

    boolean startUser(int userId, int userStartMode, android.os.IProgressListener unlockListener) {
        checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "startUser");
        return startUserNoChecks(userId, 0, userStartMode, unlockListener);
    }

    boolean startUserVisibleOnDisplay(int userId, int displayId, android.os.IProgressListener unlockListener) {
        checkCallingHasOneOfThosePermissions("startUserOnDisplay", "android.permission.MANAGE_USERS", "android.permission.INTERACT_ACROSS_USERS");
        try {
            return startUserNoChecks(userId, displayId, 3, unlockListener);
        } catch (java.lang.RuntimeException e) {
            com.android.server.utils.Slogf.e("ActivityManager", "startUserOnSecondaryDisplay(%d, %d) failed: %s", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId), e);
            return false;
        }
    }

    private boolean startUserNoChecks(int userId, int displayId, int userStartMode, android.os.IProgressListener unlockListener) {
        if (userStartMode == 1 && isUserSwitchUiEnabled()) {
            this.mUserControllerExt.startFreezingScreenIfNeeded(getCurrentUserId(), userId);
        }
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("UserController.startUser-" + userId + (displayId == 0 ? "" : "-display-" + displayId) + "-" + (userStartMode == 1 ? "fg" : "bg") + "-start-mode-" + userStartMode);
        try {
            return startUserInternal(userId, displayId, userStartMode, unlockListener, t);
        } finally {
            t.traceEnd();
        }
    }

    /* JADX WARN: Not initialized variable reg: 25, insn: 0x021f: MOVE (r2 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r25 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('needStart' boolean)]), block:B:89:0x0215 */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02c4 A[Catch: all -> 0x042f, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x042f, blocks: (B:104:0x0285, B:125:0x02d8, B:150:0x0337, B:155:0x0362, B:161:0x03a7, B:138:0x030e, B:119:0x02c4), top: B:231:0x0285 }] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x02e8 A[Catch: all -> 0x02bf, TRY_ENTER, TryCatch #16 {all -> 0x02bf, blocks: (B:106:0x028a, B:107:0x0293, B:110:0x0299, B:112:0x02b1, B:122:0x02cf, B:124:0x02d5, B:128:0x02e8, B:129:0x0300, B:133:0x0307, B:152:0x033b, B:157:0x036a, B:165:0x03af, B:137:0x030d, B:140:0x0312, B:141:0x0329, B:145:0x0330, B:149:0x0336, B:116:0x02be, B:130:0x0301, B:131:0x0304, B:142:0x032a, B:143:0x032d, B:108:0x0294, B:109:0x0298), top: B:244:0x028a, inners: #5, #11, #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x030e A[Catch: all -> 0x042f, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x042f, blocks: (B:104:0x0285, B:125:0x02d8, B:150:0x0337, B:155:0x0362, B:161:0x03a7, B:138:0x030e, B:119:0x02c4), top: B:231:0x0285 }] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x033b A[Catch: all -> 0x02bf, TRY_ENTER, TRY_LEAVE, TryCatch #16 {all -> 0x02bf, blocks: (B:106:0x028a, B:107:0x0293, B:110:0x0299, B:112:0x02b1, B:122:0x02cf, B:124:0x02d5, B:128:0x02e8, B:129:0x0300, B:133:0x0307, B:152:0x033b, B:157:0x036a, B:165:0x03af, B:137:0x030d, B:140:0x0312, B:141:0x0329, B:145:0x0330, B:149:0x0336, B:116:0x02be, B:130:0x0301, B:131:0x0304, B:142:0x032a, B:143:0x032d, B:108:0x0294, B:109:0x0298), top: B:244:0x028a, inners: #5, #11, #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0361  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x036a A[Catch: all -> 0x02bf, TRY_ENTER, TRY_LEAVE, TryCatch #16 {all -> 0x02bf, blocks: (B:106:0x028a, B:107:0x0293, B:110:0x0299, B:112:0x02b1, B:122:0x02cf, B:124:0x02d5, B:128:0x02e8, B:129:0x0300, B:133:0x0307, B:152:0x033b, B:157:0x036a, B:165:0x03af, B:137:0x030d, B:140:0x0312, B:141:0x0329, B:145:0x0330, B:149:0x0336, B:116:0x02be, B:130:0x0301, B:131:0x0304, B:142:0x032a, B:143:0x032d, B:108:0x0294, B:109:0x0298), top: B:244:0x028a, inners: #5, #11, #15 }] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x03a3  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x03ab  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x03c3  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x03f4 A[Catch: all -> 0x042b, TRY_ENTER, TryCatch #4 {all -> 0x042b, blocks: (B:176:0x03c8, B:187:0x0411, B:186:0x0405, B:183:0x03f4, B:175:0x03c5), top: B:222:0x03c5 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0405 A[Catch: all -> 0x042b, TryCatch #4 {all -> 0x042b, blocks: (B:176:0x03c8, B:187:0x0411, B:186:0x0405, B:183:0x03f4, B:175:0x03c5), top: B:222:0x03c5 }] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x03d5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x028a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00b7 A[Catch: all -> 0x00ab, TRY_ENTER, TRY_LEAVE, TryCatch #6 {all -> 0x00ab, blocks: (B:27:0x0086, B:29:0x008c, B:31:0x0096, B:34:0x009b, B:36:0x00a0, B:37:0x00a3, B:44:0x00b7, B:47:0x00d7, B:51:0x00f6, B:53:0x00fc, B:66:0x0162, B:94:0x022f, B:96:0x023c, B:99:0x0246, B:60:0x0127), top: B:226:0x0086 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00d7 A[Catch: all -> 0x00ab, TRY_ENTER, TRY_LEAVE, TryCatch #6 {all -> 0x00ab, blocks: (B:27:0x0086, B:29:0x008c, B:31:0x0096, B:34:0x009b, B:36:0x00a0, B:37:0x00a3, B:44:0x00b7, B:47:0x00d7, B:51:0x00f6, B:53:0x00fc, B:66:0x0162, B:94:0x022f, B:96:0x023c, B:99:0x0246, B:60:0x0127), top: B:226:0x0086 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x022f A[Catch: all -> 0x00ab, TRY_ENTER, TryCatch #6 {all -> 0x00ab, blocks: (B:27:0x0086, B:29:0x008c, B:31:0x0096, B:34:0x009b, B:36:0x00a0, B:37:0x00a3, B:44:0x00b7, B:47:0x00d7, B:51:0x00f6, B:53:0x00fc, B:66:0x0162, B:94:0x022f, B:96:0x023c, B:99:0x0246, B:60:0x0127), top: B:226:0x0086 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x023c A[Catch: all -> 0x00ab, TRY_LEAVE, TryCatch #6 {all -> 0x00ab, blocks: (B:27:0x0086, B:29:0x008c, B:31:0x0096, B:34:0x009b, B:36:0x00a0, B:37:0x00a3, B:44:0x00b7, B:47:0x00d7, B:51:0x00f6, B:53:0x00fc, B:66:0x0162, B:94:0x022f, B:96:0x023c, B:99:0x0246, B:60:0x0127), top: B:226:0x0086 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0246 A[Catch: all -> 0x00ab, TRY_ENTER, TRY_LEAVE, TryCatch #6 {all -> 0x00ab, blocks: (B:27:0x0086, B:29:0x008c, B:31:0x0096, B:34:0x009b, B:36:0x00a0, B:37:0x00a3, B:44:0x00b7, B:47:0x00d7, B:51:0x00f6, B:53:0x00fc, B:66:0x0162, B:94:0x022f, B:96:0x023c, B:99:0x0246, B:60:0x0127), top: B:226:0x0086 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean startUserInternal(int r37, int r38, int r39, android.os.IProgressListener r40, com.android.server.utils.TimingsTraceAndSlog r41) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1137
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.UserController.startUserInternal(int, int, int, android.os.IProgressListener, com.android.server.utils.TimingsTraceAndSlog):boolean");
    }

    void startUserInForeground(int targetUserId) {
        if (android.multiuser.Flags.setPowerModeDuringUserSwitch()) {
            this.mInjector.setPerformancePowerMode(true);
        }
        boolean success = startUser(targetUserId, 1);
        if (!success) {
            this.mUserControllerExt.stopFreezingScreenIfNeeded(getCurrentUserId(), targetUserId);
            this.mInjector.getWindowManager().setSwitchingUser(false);
            lambda$completeUserSwitch$18(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.endUserSwitch();
                }
            });
        }
    }

    boolean unlockUser(int userId, android.os.IProgressListener listener) {
        checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "unlockUser");
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_UNLOCK_USER, userId);
        long binderToken = android.os.Binder.clearCallingIdentity();
        try {
            return maybeUnlockUser(userId, listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(binderToken);
        }
    }

    private static void notifyFinished(int userId, android.os.IProgressListener listener) {
        if (listener == null) {
            return;
        }
        try {
            listener.onFinished(userId, (android.os.Bundle) null);
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeUnlockUser(int userId) {
        return maybeUnlockUser(userId, null);
    }

    private boolean maybeUnlockUser(int userId, android.os.IProgressListener listener) {
        com.android.server.am.UserState uss;
        int[] userIds;
        if (!this.mAllowUserUnlocking) {
            com.android.server.utils.Slogf.i("ActivityManager", "Not unlocking user %d yet because boot hasn't completed", java.lang.Integer.valueOf(userId));
            notifyFinished(userId, listener);
            return false;
        }
        this.mUserControllerExt.ormsUnlockUserBoost(500);
        if (!android.os.storage.StorageManager.isCeStorageUnlocked(userId)) {
            this.mLockPatternUtils.unlockUserKeyIfUnsecured(userId);
        }
        synchronized (this.mLock) {
            uss = this.mStartedUsers.get(userId);
            if (uss != null) {
                uss.mUnlockProgress.addListener(listener);
            }
        }
        if (uss == null) {
            notifyFinished(userId, listener);
            return false;
        }
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("finishUserUnlocking-" + userId);
        boolean finishUserUnlockingResult = finishUserUnlocking(uss);
        t.traceEnd();
        if (!finishUserUnlockingResult) {
            notifyFinished(userId, listener);
            return false;
        }
        synchronized (this.mLock) {
            userIds = new int[this.mStartedUsers.size()];
            for (int i = 0; i < userIds.length; i++) {
                userIds[i] = this.mStartedUsers.keyAt(i);
            }
        }
        for (int testUserId : userIds) {
            android.content.pm.UserInfo parent = this.mInjector.getUserManager().getProfileParent(testUserId);
            if (parent != null && parent.id == userId && testUserId != userId) {
                com.android.server.utils.Slogf.d("ActivityManager", "User " + testUserId + " (parent " + parent.id + "): attempting unlock because parent was just unlocked");
                maybeUnlockUser(testUserId);
            }
        }
        return true;
    }

    boolean switchUser(int targetUserId) {
        int uid = android.os.Binder.getCallingUid();
        enforceShellRestriction("no_debugging_features", targetUserId);
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_SWITCH_USER, targetUserId);
        int currentUserId = getCurrentUserId();
        android.content.pm.UserInfo targetUserInfo = getUserInfo(targetUserId);
        synchronized (this.mLock) {
            if (targetUserId == currentUserId) {
                try {
                    if (this.mTargetUserId == -10000) {
                        com.android.server.utils.Slogf.i("ActivityManager", "user #" + targetUserId + " is already the current user");
                        return true;
                    }
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            if (targetUserInfo == null) {
                com.android.server.utils.Slogf.w("ActivityManager", "No user info for user #" + targetUserId);
                return false;
            }
            if (!targetUserInfo.supportsSwitchTo()) {
                com.android.server.utils.Slogf.w("ActivityManager", "Cannot switch to User #" + targetUserId + ": not supported");
                return false;
            }
            if (com.android.server.FactoryResetter.isFactoryResetting()) {
                com.android.server.utils.Slogf.w("ActivityManager", "Cannot switch to User #" + targetUserId + ": factory reset in progress");
                return false;
            }
            if (this.mInitialized) {
                if (this.mTargetUserId != -10000) {
                    com.android.server.utils.Slogf.w("ActivityManager", "There is already an ongoing user switch to User #" + this.mTargetUserId + ". User #" + targetUserId + " will be added to the queue.");
                    this.mPendingTargetUserIds.offer(java.lang.Integer.valueOf(targetUserId));
                    return true;
                }
                this.mTargetUserId = targetUserId;
                boolean userSwitchUiEnabled = this.mUserSwitchUiEnabled;
                this.mUserControllerExt.switchUser(userSwitchUiEnabled, getUserInfo(currentUserId), targetUserInfo, uid);
                this.mUserControllerExt.ormsSwitchUserBoost(5000);
                if (userSwitchUiEnabled) {
                    android.content.pm.UserInfo currentUserInfo = getUserInfo(currentUserId);
                    android.util.Pair<android.content.pm.UserInfo, android.content.pm.UserInfo> userNames = new android.util.Pair<>(currentUserInfo, targetUserInfo);
                    this.mUiHandler.removeMessages(1000);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(1000, userNames));
                } else {
                    sendStartUserSwitchFgMessage(targetUserId);
                }
                return true;
            }
            com.android.server.utils.Slogf.e("ActivityManager", "Cannot switch to User #" + targetUserId + ": UserController not ready yet");
            return false;
        }
    }

    private void sendStartUserSwitchFgMessage(int targetUserId) {
        this.mHandler.removeMessages(120);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(120, targetUserId, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dismissUserSwitchDialog, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$completeUserSwitch$18(final java.lang.Runnable onDismissed) {
        this.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dismissUserSwitchDialog$14(onDismissed);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissUserSwitchDialog$14(java.lang.Runnable onDismissed) {
        this.mInjector.dismissUserSwitchingDialog(onDismissed);
    }

    private void showUserSwitchDialog(final android.util.Pair<android.content.pm.UserInfo, android.content.pm.UserInfo> fromToUserPair) {
        if (this.mUserControllerExt.hookShowUserSwitchDialog((android.content.pm.UserInfo) fromToUserPair.first, (android.content.pm.UserInfo) fromToUserPair.second)) {
            return;
        }
        this.mInjector.showUserSwitchingDialog((android.content.pm.UserInfo) fromToUserPair.first, (android.content.pm.UserInfo) fromToUserPair.second, getSwitchingFromSystemUserMessageUnchecked(), getSwitchingToSystemUserMessageUnchecked(), new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showUserSwitchDialog$15(fromToUserPair);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showUserSwitchDialog$15(android.util.Pair fromToUserPair) {
        sendStartUserSwitchFgMessage(((android.content.pm.UserInfo) fromToUserPair.second).id);
    }

    private void dispatchForegroundProfileChanged(int userId) {
        int observerCount = this.mUserSwitchObservers.beginBroadcast();
        for (int i = 0; i < observerCount; i++) {
            try {
                this.mUserSwitchObservers.getBroadcastItem(i).onForegroundProfileSwitch(userId);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mUserSwitchObservers.finishBroadcast();
    }

    void dispatchUserSwitchComplete(int oldUserId, int newUserId) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("dispatchUserSwitchComplete-" + newUserId);
        this.mInjector.getWindowManager().setSwitchingUser(false);
        int observerCount = this.mUserSwitchObservers.beginBroadcast();
        for (int i = 0; i < observerCount; i++) {
            try {
                t.traceBegin("onUserSwitchComplete-" + newUserId + " #" + i + " " + this.mUserSwitchObservers.getBroadcastCookie(i));
                this.mUserSwitchObservers.getBroadcastItem(i).onUserSwitchComplete(newUserId);
                t.traceEnd();
            } catch (android.os.RemoteException e) {
            }
        }
        this.mUserSwitchObservers.finishBroadcast();
        t.traceBegin("sendUserSwitchBroadcasts-" + oldUserId + "-" + newUserId);
        sendUserSwitchBroadcasts(oldUserId, newUserId);
        t.traceEnd();
        t.traceEnd();
        endUserSwitch();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endUserSwitch() {
        int nextUserId;
        if (android.multiuser.Flags.setPowerModeDuringUserSwitch()) {
            this.mInjector.setPerformancePowerMode(false);
        }
        synchronized (this.mLock) {
            nextUserId = ((java.lang.Integer) com.android.internal.util.ObjectUtils.getOrElse(this.mPendingTargetUserIds.poll(), -10000)).intValue();
            this.mTargetUserId = -10000;
        }
        if (nextUserId != -10000) {
            switchUser(nextUserId);
        }
    }

    private void dispatchLockedBootComplete(int userId) {
        int observerCount = this.mUserSwitchObservers.beginBroadcast();
        for (int i = 0; i < observerCount; i++) {
            try {
                this.mUserSwitchObservers.getBroadcastItem(i).onLockedBootComplete(userId);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mUserSwitchObservers.finishBroadcast();
    }

    private void stopUserOnSwitchIfEnforced(int oldUserId) {
        if (oldUserId == 0) {
            return;
        }
        boolean hasRestriction = hasUserRestriction("no_run_in_background", oldUserId);
        synchronized (this.mLock) {
            if (!hasRestriction) {
                if (!shouldStopUserOnSwitch()) {
                    java.util.List<android.content.pm.UserInfo> profiles = this.mInjector.getUserManager().getProfiles(oldUserId, false);
                    int count = profiles.size();
                    for (int i = 0; i < count; i++) {
                        int profileUserId = profiles.get(i).id;
                        if (hasUserRestriction("no_run_in_background", profileUserId)) {
                            com.android.server.utils.Slogf.i("ActivityManager", "Stopping profile %d on user switch", java.lang.Integer.valueOf(profileUserId));
                            synchronized (this.mLock) {
                                stopUsersLU(profileUserId, false, false, null, null);
                            }
                        }
                    }
                    return;
                }
            }
            com.android.server.utils.Slogf.i("ActivityManager", "Stopping user %d and its profiles on user switch", java.lang.Integer.valueOf(oldUserId));
            stopUsersLU(oldUserId, false, null, null);
        }
    }

    private void scheduleStopOfBackgroundUser(int oldUserId) {
        int delayUptimeSecs;
        if (!android.multiuser.Flags.scheduleStopOfBackgroundUser() || (delayUptimeSecs = this.mBackgroundUserScheduledStopTimeSecs) <= 0 || android.os.UserManager.isVisibleBackgroundUsersEnabled() || oldUserId == 0) {
            return;
        }
        if (oldUserId == this.mInjector.getUserManagerInternal().getMainUserId()) {
            com.android.server.utils.Slogf.i("ActivityManager", "Exempting user %d from being stopped due to inactivity by virtue of it being the main user", java.lang.Integer.valueOf(oldUserId));
            return;
        }
        com.android.server.utils.Slogf.d("ActivityManager", "Scheduling to stop user %d in %d seconds", java.lang.Integer.valueOf(oldUserId), java.lang.Integer.valueOf(delayUptimeSecs));
        int delayUptimeMs = delayUptimeSecs * 1000;
        java.lang.Object msgObj = java.lang.Integer.valueOf(oldUserId);
        this.mHandler.removeEqualMessages(150, msgObj);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(150, msgObj), delayUptimeMs);
    }

    void processScheduledStopOfBackgroundUser(java.lang.Integer userIdInteger) {
        int userId = userIdInteger.intValue();
        com.android.server.utils.Slogf.d("ActivityManager", "Considering stopping background user %d due to inactivity", java.lang.Integer.valueOf(userId));
        synchronized (this.mLock) {
            if (getCurrentOrTargetUserIdLU() == userId) {
                return;
            }
            if (this.mPendingTargetUserIds.contains(userIdInteger)) {
                return;
            }
            com.android.server.utils.Slogf.i("ActivityManager", "Stopping background user %d due to inactivity", java.lang.Integer.valueOf(userId));
            stopUsersLU(userId, true, null, null);
        }
    }

    private void timeoutUserSwitch(com.android.server.am.UserState uss, int oldUserId, int newUserId) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog("ActivityManager");
        t.traceBegin("timeoutUserSwitch-" + oldUserId + "-to-" + newUserId);
        synchronized (this.mLock) {
            com.android.server.utils.Slogf.e("ActivityManager", "User switch timeout: from " + oldUserId + " to " + newUserId);
            this.mTimeoutUserSwitchCallbacks = this.mCurWaitingUserSwitchCallbacks;
            this.mUserControllerExt.timeoutUserSwitch(this.mCurWaitingUserSwitchCallbacks, uss, oldUserId, newUserId);
            this.mHandler.removeMessages(90);
            sendContinueUserSwitchLU(uss, oldUserId, newUserId);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(90, oldUserId, newUserId), 5000L);
        }
        t.traceEnd();
    }

    private void timeoutUserSwitchCallbacks(int oldUserId, int newUserId) {
        synchronized (this.mLock) {
            if (this.mTimeoutUserSwitchCallbacks != null && !this.mTimeoutUserSwitchCallbacks.isEmpty()) {
                com.android.server.utils.Slogf.wtf("ActivityManager", "User switch timeout: from " + oldUserId + " to " + newUserId + ". Observers that didn't respond: " + this.mTimeoutUserSwitchCallbacks);
                this.mTimeoutUserSwitchCallbacks = null;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r10v6 */
    /* JADX WARN: Type inference failed for: r15v1, types: [com.android.server.am.UserController] */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Type inference failed for: r15v3 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v13, types: [int] */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.server.am.UserController] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.android.server.am.UserController] */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARN: Type inference failed for: r5v4, types: [int] */
    /* JADX WARN: Type inference failed for: r5v5 */
    /* JADX WARN: Type inference failed for: r5v6 */
    /* JADX WARN: Type inference failed for: r5v7 */
    /* JADX WARN: Type inference failed for: r5v9 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    void dispatchUserSwitch(final com.android.server.am.UserState userState, final int i, final int i2) throws java.lang.Throwable {
        com.android.server.utils.TimingsTraceAndSlog timingsTraceAndSlog;
        ?? r3;
        final long j;
        final android.util.ArraySet<java.lang.String> arraySet;
        int i3;
        com.android.server.utils.TimingsTraceAndSlog timingsTraceAndSlog2;
        java.lang.String str;
        java.lang.String str2;
        com.android.server.am.UserController userController = this;
        int i4 = i2;
        com.android.server.utils.TimingsTraceAndSlog timingsTraceAndSlog3 = new com.android.server.utils.TimingsTraceAndSlog();
        timingsTraceAndSlog3.traceBegin("dispatchUserSwitch-" + i + "-to-" + (i4 == true ? 1 : 0));
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_DISPATCH_USER_SWITCH, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(i2));
        userController.mUserControllerExt.dispatchSwitch(userState, i, i4 == true ? 1 : 0);
        int iBeginBroadcast = userController.mUserSwitchObservers.beginBroadcast();
        if (iBeginBroadcast > 0) {
            for (int i5 = 0; i5 < iBeginBroadcast; i5++) {
                str = "#" + i5 + " " + userController.mUserSwitchObservers.getBroadcastCookie(i5);
                str2 = "onBeforeUserSwitching-";
                timingsTraceAndSlog3.traceBegin("onBeforeUserSwitching-" + str);
                try {
                    userController.mUserSwitchObservers.getBroadcastItem(i5).onBeforeUserSwitching(i4 == true ? 1 : 0);
                } catch (android.os.RemoteException e) {
                } catch (java.lang.Throwable th) {
                    timingsTraceAndSlog3.traceEnd();
                    throw th;
                }
                timingsTraceAndSlog3.traceEnd();
            }
            android.util.ArraySet<java.lang.String> arraySet2 = new android.util.ArraySet<>();
            synchronized (userController.mLock) {
                try {
                    userState.switching = true;
                    userController.mCurWaitingUserSwitchCallbacks = arraySet2;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    }
                }
            }
            final java.util.concurrent.atomic.AtomicInteger atomicInteger = new java.util.concurrent.atomic.AtomicInteger(iBeginBroadcast);
            long userSwitchTimeoutMs = getUserSwitchTimeoutMs();
            final long jElapsedRealtime = android.os.SystemClock.elapsedRealtime();
            int i6 = 0;
            ?? r2 = str;
            ?? r32 = str2;
            ?? r5 = userSwitchTimeoutMs;
            ?? r10 = i4;
            ?? r15 = userController;
            while (i6 < iBeginBroadcast) {
                final long jElapsedRealtime2 = android.os.SystemClock.elapsedRealtime();
                try {
                    final java.lang.String str3 = "#" + i6 + " " + r15.mUserSwitchObservers.getBroadcastCookie(i6);
                    synchronized (r15.mLock) {
                        try {
                            arraySet2.add(str3);
                        } finally {
                            th = th;
                            while (true) {
                                try {
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                }
                            }
                        }
                    }
                    j = r5 == true ? 1 : 0;
                    arraySet = arraySet2;
                    int i7 = i6;
                    i3 = iBeginBroadcast;
                    timingsTraceAndSlog2 = timingsTraceAndSlog3;
                    try {
                        android.os.IRemoteCallback.Stub stub = new android.os.IRemoteCallback.Stub() { // from class: com.android.server.am.UserController.7
                            public void sendResult(android.os.Bundle data) throws android.os.RemoteException {
                                com.android.server.am.UserController.asyncTraceEnd("onUserSwitching-" + str3, i2);
                                synchronized (com.android.server.am.UserController.this.mLock) {
                                    long delayForObserver = android.os.SystemClock.elapsedRealtime() - jElapsedRealtime2;
                                    if (delayForObserver > 500) {
                                        com.android.server.utils.Slogf.w("ActivityManager", "User switch slowed down by observer " + str3 + ": result took " + delayForObserver + " ms to process.");
                                    }
                                    long totalDelay = android.os.SystemClock.elapsedRealtime() - jElapsedRealtime;
                                    com.android.server.am.UserController.this.mUserControllerExt.dispatchSwitchSendResult(totalDelay, str3, i, i2);
                                    if (totalDelay > j) {
                                        com.android.server.utils.Slogf.e("ActivityManager", "User switch timeout: observer " + str3 + "'s result was received " + totalDelay + " ms after dispatchUserSwitch.");
                                    }
                                    arraySet.remove(str3);
                                    if (atomicInteger.decrementAndGet() == 0 && arraySet == com.android.server.am.UserController.this.mCurWaitingUserSwitchCallbacks) {
                                        com.android.server.am.UserController.this.sendContinueUserSwitchLU(userState, i, i2);
                                    }
                                }
                            }
                        };
                        r2 = i2;
                        try {
                            asyncTraceBegin("onUserSwitching-" + str3, r2);
                            r32 = this;
                        } catch (android.os.RemoteException e2) {
                            r32 = this;
                        }
                        try {
                            r5 = i7;
                            try {
                                r32.mUserSwitchObservers.getBroadcastItem(r5 == true ? 1 : 0).onUserSwitching((int) r2, stub);
                            } catch (android.os.RemoteException e3) {
                            }
                        } catch (android.os.RemoteException e4) {
                            r5 = i7;
                        }
                    } catch (android.os.RemoteException e5) {
                        r32 = this;
                        r2 = i2;
                    }
                } catch (android.os.RemoteException e6) {
                    j = r5 == true ? 1 : 0;
                    arraySet = arraySet2;
                    r5 = i6;
                    i3 = iBeginBroadcast;
                    r2 = r10;
                    timingsTraceAndSlog2 = timingsTraceAndSlog3;
                    r32 = r15;
                }
                i6 = r5 + 1;
                r10 = r2;
                r15 = r32;
                r5 = j;
                arraySet2 = arraySet;
                iBeginBroadcast = i3;
                timingsTraceAndSlog3 = timingsTraceAndSlog2;
                r2 = r2;
                r32 = r32;
            }
            long j2 = r5 == true ? 1 : 0;
            timingsTraceAndSlog = timingsTraceAndSlog3;
            r3 = r15;
        } else {
            timingsTraceAndSlog = timingsTraceAndSlog3;
            com.android.server.am.UserController userController2 = userController;
            synchronized (userController2.mLock) {
                sendContinueUserSwitchLU(userState, i, i2);
                r3 = userController2;
            }
        }
        r3.mUserSwitchObservers.finishBroadcast();
        timingsTraceAndSlog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendContinueUserSwitchLU(com.android.server.am.UserState uss, int oldUserId, int newUserId) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog("ActivityManager");
        t.traceBegin("sendContinueUserSwitchLU-" + oldUserId + "-to-" + newUserId);
        this.mCurWaitingUserSwitchCallbacks = null;
        this.mHandler.removeMessages(30);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(20, oldUserId, newUserId, uss));
        t.traceEnd();
    }

    void continueUserSwitch(com.android.server.am.UserState uss, int oldUserId, int newUserId) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("continueUserSwitch-" + oldUserId + "-to-" + newUserId);
        this.mUserControllerExt.continueUserSwitch(uss, oldUserId, newUserId);
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_CONTINUE_USER_SWITCH, java.lang.Integer.valueOf(oldUserId), java.lang.Integer.valueOf(newUserId));
        this.mHandler.removeMessages(130);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(130, oldUserId, newUserId));
        uss.switching = false;
        stopGuestOrEphemeralUserIfBackground(oldUserId);
        stopUserOnSwitchIfEnforced(oldUserId);
        scheduleStopOfBackgroundUser(oldUserId);
        t.traceEnd();
    }

    void completeUserSwitch(final int oldUserId, final int newUserId) {
        final java.lang.Runnable sendUserSwitchCompleteMessage = new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$completeUserSwitch$16(oldUserId, newUserId);
            }
        };
        if (isUserSwitchUiEnabled()) {
            if (this.mInjector.getKeyguardManager().isDeviceSecure(newUserId)) {
                if ((this.mUserControllerExt.isMultiSystemUserId(newUserId) || this.mUserControllerExt.isMultiSystemUserId(oldUserId)) && !this.mUserControllerExt.getWaitForKeyguardShown()) {
                    this.mInjector.getWindowManager().lockDeviceNow();
                    sendUserSwitchCompleteMessage.run();
                } else {
                    showKeyguard(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda18
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$completeUserSwitch$17(sendUserSwitchCompleteMessage);
                        }
                    });
                }
                this.mUserControllerExt.setWaitForKeyguardShown(true);
                return;
            }
            dismissKeyguard(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$completeUserSwitch$18(sendUserSwitchCompleteMessage);
                }
            });
            return;
        }
        sendUserSwitchCompleteMessage.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$completeUserSwitch$16(int oldUserId, int newUserId) {
        this.mHandler.removeMessages(80);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(80, oldUserId, newUserId));
        this.mUserControllerExt.stopFreezingScreenIfNeeded(oldUserId, newUserId);
    }

    protected void showKeyguard(java.lang.Runnable runnable) {
        final com.android.server.am.UserController.Injector injector = this.mInjector;
        java.util.Objects.requireNonNull(injector);
        runWithTimeout(new java.util.function.Consumer() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                injector.showKeyguard((java.lang.Runnable) obj);
            }
        }, SHOW_KEYGUARD_TIMEOUT_MS, runnable, new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.am.UserController.lambda$showKeyguard$19();
            }
        }, "showKeyguard");
    }

    static /* synthetic */ void lambda$showKeyguard$19() {
        throw new java.lang.RuntimeException("Keyguard is not shown in 20000 ms.");
    }

    protected void dismissKeyguard(java.lang.Runnable runnable) {
        final com.android.server.am.UserController.Injector injector = this.mInjector;
        java.util.Objects.requireNonNull(injector);
        runWithTimeout(new java.util.function.Consumer() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                injector.dismissKeyguard((java.lang.Runnable) obj);
            }
        }, 2000, runnable, runnable, "dismissKeyguard");
    }

    private void runWithTimeout(java.util.function.Consumer<java.lang.Runnable> task, final int timeoutMs, final java.lang.Runnable onSuccess, final java.lang.Runnable onTimeout, final java.lang.String traceMsg) {
        final java.util.concurrent.atomic.AtomicInteger state = new java.util.concurrent.atomic.AtomicInteger(0);
        asyncTraceBegin(traceMsg, 0);
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.am.UserController.lambda$runWithTimeout$20(state, traceMsg, timeoutMs, onTimeout);
            }
        }, timeoutMs);
        task.accept(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.am.UserController.lambda$runWithTimeout$21(state, traceMsg, onSuccess);
            }
        });
    }

    static /* synthetic */ void lambda$runWithTimeout$20(java.util.concurrent.atomic.AtomicInteger state, java.lang.String traceMsg, int timeoutMs, java.lang.Runnable onTimeout) {
        if (state.compareAndSet(0, 1)) {
            asyncTraceEnd(traceMsg, 0);
            com.android.server.utils.Slogf.w("ActivityManager", "Timeout: %s did not finish in %d ms", traceMsg, java.lang.Integer.valueOf(timeoutMs));
            onTimeout.run();
        }
    }

    static /* synthetic */ void lambda$runWithTimeout$21(java.util.concurrent.atomic.AtomicInteger state, java.lang.String traceMsg, java.lang.Runnable onSuccess) {
        if (state.compareAndSet(0, 2)) {
            asyncTraceEnd(traceMsg, 0);
            onSuccess.run();
        }
    }

    private void moveUserToForeground(com.android.server.am.UserState uss, int newUserId) {
        boolean homeInFront = this.mInjector.taskSupervisorSwitchUser(newUserId, uss);
        if (homeInFront) {
            this.mInjector.startHomeActivity(newUserId, "moveUserToForeground");
        } else {
            this.mInjector.taskSupervisorResumeFocusedStackTopActivity();
        }
        com.android.server.am.EventLogTags.writeAmSwitchUser(newUserId);
    }

    void sendUserStartedBroadcast(int userId, int callingUid, int callingPid) throws java.lang.Throwable {
        if (userId == 0) {
            synchronized (this.mLock) {
                if (this.mIsBroadcastSentForSystemUserStarted) {
                    return;
                } else {
                    this.mIsBroadcastSentForSystemUserStarted = true;
                }
            }
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.USER_STARTED");
        intent.addFlags(1342177280);
        intent.putExtra("android.intent.extra.user_handle", userId);
        this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, callingUid, callingPid, userId);
    }

    void sendUserStartingBroadcast(int userId, int callingUid, int callingPid) throws java.lang.Throwable {
        if (userId == 0) {
            synchronized (this.mLock) {
                if (this.mIsBroadcastSentForSystemUserStarting) {
                    return;
                } else {
                    this.mIsBroadcastSentForSystemUserStarting = true;
                }
            }
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.USER_STARTING");
        intent.addFlags(1073741824);
        intent.putExtra("android.intent.extra.user_handle", userId);
        this.mInjector.broadcastIntent(intent, null, new android.content.IIntentReceiver.Stub() { // from class: com.android.server.am.UserController.8
            public void performReceive(android.content.Intent intent2, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) throws android.os.RemoteException {
            }
        }, 0, null, null, new java.lang.String[]{"android.permission.INTERACT_ACROSS_USERS"}, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, callingUid, callingPid, -1);
    }

    void sendUserSwitchBroadcasts(int oldUserId, int newUserId) {
        java.lang.String str;
        java.lang.String str2;
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long ident = android.os.Binder.clearCallingIdentity();
        java.lang.String str3 = "android.intent.extra.USER";
        java.lang.String str4 = "android.intent.extra.user_handle";
        int i = 1342177280;
        if (oldUserId >= 0) {
            try {
                java.util.List<android.content.pm.UserInfo> profiles = this.mInjector.getUserManager().getProfiles(oldUserId, false);
                int count = profiles.size();
                int i2 = 0;
                while (i2 < count) {
                    int profileUserId = profiles.get(i2).id;
                    android.content.Intent intent = new android.content.Intent("android.intent.action.USER_BACKGROUND");
                    intent.addFlags(i);
                    intent.putExtra(str4, profileUserId);
                    intent.putExtra(str3, android.os.UserHandle.of(profileUserId));
                    this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, callingUid, callingPid, profileUserId);
                    i2++;
                    count = count;
                    profiles = profiles;
                    str4 = str4;
                    str3 = str3;
                    i = 1342177280;
                }
                str = str4;
                str2 = str3;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        } else {
            str = "android.intent.extra.user_handle";
            str2 = "android.intent.extra.USER";
        }
        if (newUserId >= 0) {
            java.util.List<android.content.pm.UserInfo> profiles2 = this.mInjector.getUserManager().getProfiles(newUserId, false);
            int count2 = profiles2.size();
            int i3 = 0;
            while (i3 < count2) {
                int profileUserId2 = profiles2.get(i3).id;
                android.content.Intent intent2 = new android.content.Intent("android.intent.action.USER_FOREGROUND");
                intent2.addFlags(1342177280);
                java.lang.String str5 = str;
                intent2.putExtra(str5, profileUserId2);
                java.lang.String str6 = str2;
                intent2.putExtra(str6, android.os.UserHandle.of(profileUserId2));
                this.mInjector.broadcastIntent(intent2, null, null, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, callingUid, callingPid, profileUserId2);
                i3++;
                count2 = count2;
                str2 = str6;
                str = str5;
            }
            android.content.Intent intent3 = new android.content.Intent("android.intent.action.USER_SWITCHED");
            intent3.addFlags(1342177280);
            intent3.putExtra(str, newUserId);
            intent3.putExtra(str2, android.os.UserHandle.of(newUserId));
            this.mInjector.broadcastIntent(intent3, null, null, 0, null, null, new java.lang.String[]{"android.permission.MANAGE_USERS"}, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, callingUid, callingPid, -1);
        }
        android.os.Binder.restoreCallingIdentity(ident);
    }

    private void broadcastProfileAccessibleStateChanged(int userId, int parentId, java.lang.String intentAction) throws java.lang.Throwable {
        android.content.Intent intent = new android.content.Intent(intentAction);
        intent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId));
        intent.addFlags(1342177280);
        this.mInjector.broadcastIntent(intent, null, null, 0, null, null, null, -1, null, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), parentId);
    }

    int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, int allowMode, java.lang.String name, java.lang.String callerPackage) {
        int i;
        int i2;
        boolean allow;
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (callingUserId == userId) {
            return userId;
        }
        if (this.mUserControllerExt.hookHandleIncomingUser(callingUid, userId)) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
                android.util.Slog.v("ActivityManager", "multi app -> handleIncomingUser: bypass user[" + userId + "] for uid[" + callingUid + "(" + callerPackage + ")]");
            }
            return userId;
        }
        int targetUserId = unsafeConvertIncomingUser(userId);
        if (callingUid != 0 && callingUid != 1000) {
            boolean isSameProfileGroup = isSameProfileGroup(callingUserId, targetUserId);
            if (this.mInjector.isCallerRecents(callingUid) && isSameProfileGroup) {
                allow = true;
                i = 2;
                i2 = 1;
            } else {
                i = 2;
                if (this.mInjector.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingPid, callingUid, -1, true) == 0) {
                    allow = true;
                    i2 = 1;
                } else if (allowMode == 2) {
                    allow = false;
                    i2 = 1;
                } else if (canInteractWithAcrossProfilesPermission(allowMode, isSameProfileGroup, callingPid, callingUid, callerPackage)) {
                    allow = true;
                    i2 = 1;
                } else if (this.mInjector.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS", callingPid, callingUid, -1, true) != 0) {
                    allow = false;
                    i2 = 1;
                } else if (allowMode == 0 || allowMode == 3) {
                    i2 = 1;
                    allow = true;
                } else {
                    i2 = 1;
                    if (allowMode == 1) {
                        allow = isSameProfileGroup;
                    } else {
                        throw new java.lang.IllegalArgumentException("Unknown mode: " + allowMode);
                    }
                }
            }
            if (!allow) {
                if (userId == -3) {
                    targetUserId = callingUserId;
                } else {
                    java.lang.StringBuilder builder = new java.lang.StringBuilder(128);
                    builder.append("Permission Denial: ");
                    builder.append(name);
                    if (callerPackage != null) {
                        builder.append(" from ");
                        builder.append(callerPackage);
                    }
                    builder.append(" asks to run as user ");
                    builder.append(userId);
                    builder.append(" but is calling from uid ");
                    android.os.UserHandle.formatUid(builder, callingUid);
                    builder.append("; this requires ");
                    builder.append("android.permission.INTERACT_ACROSS_USERS_FULL");
                    if (allowMode != i) {
                        if (allowMode == 0 || allowMode == 3 || (allowMode == i2 && isSameProfileGroup)) {
                            builder.append(" or ");
                            builder.append("android.permission.INTERACT_ACROSS_USERS");
                        }
                        if (isSameProfileGroup && allowMode == 3) {
                            builder.append(" or ");
                            builder.append("android.permission.INTERACT_ACROSS_PROFILES");
                        }
                    }
                    java.lang.String msg = builder.toString();
                    com.android.server.utils.Slogf.w("ActivityManager", msg);
                    throw new java.lang.SecurityException(msg);
                }
            }
        }
        if (!allowAll) {
            ensureNotSpecialUser(targetUserId);
        }
        if (callingUid == 2000 && targetUserId >= 0 && hasUserRestriction("no_debugging_features", targetUserId)) {
            throw new java.lang.SecurityException("Shell does not have permission to access user " + targetUserId + "\n " + android.os.Debug.getCallers(3));
        }
        return targetUserId;
    }

    private boolean canInteractWithAcrossProfilesPermission(int allowMode, boolean isSameProfileGroup, int callingPid, int callingUid, java.lang.String callingPackage) {
        if (allowMode == 3 && isSameProfileGroup) {
            return this.mInjector.checkPermissionForPreflight("android.permission.INTERACT_ACROSS_PROFILES", callingPid, callingUid, callingPackage);
        }
        return false;
    }

    int unsafeConvertIncomingUser(int userId) {
        return (userId == -2 || userId == -3) ? getCurrentUserId() : userId;
    }

    void ensureNotSpecialUser(int userId) {
        if (userId >= 0) {
        } else {
            throw new java.lang.IllegalArgumentException("Call does not support special user #" + userId);
        }
    }

    void registerUserSwitchObserver(android.app.IUserSwitchObserver observer, java.lang.String name) {
        java.util.Objects.requireNonNull(name, "Observer name cannot be null");
        checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "registerUserSwitchObserver");
        this.mUserSwitchObservers.register(observer, name);
    }

    void sendForegroundProfileChanged(int userId) {
        this.mHandler.removeMessages(70);
        this.mHandler.obtainMessage(70, userId, 0).sendToTarget();
    }

    void unregisterUserSwitchObserver(android.app.IUserSwitchObserver observer) {
        this.mUserSwitchObservers.unregister(observer);
    }

    com.android.server.am.UserState getStartedUserState(int userId) {
        com.android.server.am.UserState userState;
        synchronized (this.mLock) {
            userState = this.mStartedUsers.get(userId);
        }
        return userState;
    }

    boolean hasStartedUserState(int userId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mStartedUsers.get(userId) != null;
        }
        return z;
    }

    private void updateStartedUserArrayLU() {
        int num = 0;
        for (int i = 0; i < this.mStartedUsers.size(); i++) {
            com.android.server.am.UserState uss = this.mStartedUsers.valueAt(i);
            if (uss.state != 4 && uss.state != 5) {
                num++;
            }
        }
        this.mStartedUserArray = new int[num];
        int num2 = 0;
        for (int i2 = 0; i2 < this.mStartedUsers.size(); i2++) {
            com.android.server.am.UserState uss2 = this.mStartedUsers.valueAt(i2);
            if (uss2.state != 4 && uss2.state != 5) {
                this.mStartedUserArray[num2] = this.mStartedUsers.keyAt(i2);
                num2++;
            }
        }
    }

    void setAllowUserUnlocking(boolean allowed) {
        this.mAllowUserUnlocking = allowed;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.d("ActivityManager", new java.lang.Exception(), "setAllowUserUnlocking(%b)", java.lang.Boolean.valueOf(allowed));
        }
    }

    void onBootComplete(android.content.IIntentReceiver resultTo) throws java.lang.Throwable {
        android.util.SparseArray<com.android.server.am.UserState> startedUsers;
        boolean z = true;
        setAllowUserUnlocking(true);
        synchronized (this.mLock) {
            startedUsers = this.mStartedUsers.clone();
        }
        if (startedUsers.keyAt(0) != 0) {
            z = false;
        }
        com.android.internal.util.Preconditions.checkArgument(z);
        for (int i = 0; i < startedUsers.size(); i++) {
            int userId = startedUsers.keyAt(i);
            com.android.server.am.UserState uss = startedUsers.valueAt(i);
            if (!this.mInjector.isHeadlessSystemUserMode()) {
                finishUserBoot(uss, resultTo);
            } else {
                sendLockedBootCompletedBroadcast(resultTo, userId);
                maybeUnlockUser(userId);
            }
        }
    }

    void onSystemReady() {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.d("ActivityManager", "onSystemReady()");
        }
        this.mInjector.getUserManagerInternal().addUserLifecycleListener(this.mUserLifecycleListener);
        updateProfileRelatedCaches();
        this.mInjector.reportCurWakefulnessUsageEvent();
    }

    void onSystemUserStarting() {
        if (!this.mInjector.isHeadlessSystemUserMode()) {
            this.mInjector.onUserStarting(0);
            this.mInjector.onSystemUserVisibilityChanged(true);
        }
    }

    private void updateProfileRelatedCaches() {
        java.util.List<android.content.pm.UserInfo> profiles = this.mInjector.getUserManager().getProfiles(getCurrentUserId(), false);
        int[] currentProfileIds = new int[profiles.size()];
        for (int i = 0; i < currentProfileIds.length; i++) {
            currentProfileIds[i] = profiles.get(i).id;
        }
        java.util.List<android.content.pm.UserInfo> users = this.mInjector.getUserManager().getUsers(false);
        synchronized (this.mLock) {
            this.mCurrentProfileIds = currentProfileIds;
            this.mUserProfileGroupIds.clear();
            for (int i2 = 0; i2 < users.size(); i2++) {
                android.content.pm.UserInfo user = users.get(i2);
                if (user.profileGroupId != -10000) {
                    this.mUserProfileGroupIds.put(user.id, user.profileGroupId);
                }
            }
        }
    }

    int[] getStartedUserArray() {
        int[] iArr;
        synchronized (this.mLock) {
            iArr = this.mStartedUserArray;
        }
        return iArr;
    }

    boolean isUserRunning(int userId, int flags) {
        com.android.server.am.UserState state = getStartedUserState(userId);
        if (state == null) {
            return false;
        }
        if ((flags & 1) != 0) {
            return true;
        }
        if ((flags & 2) != 0) {
            switch (state.state) {
                case 0:
                case 1:
                    return true;
                default:
                    return false;
            }
        }
        if ((flags & 8) != 0) {
            switch (state.state) {
                case 2:
                case 3:
                    return true;
                case 4:
                case 5:
                    return android.os.storage.StorageManager.isCeStorageUnlocked(userId);
                default:
                    return false;
            }
        }
        if ((flags & 4) == 0) {
            return (state.state == 4 || state.state == 5) ? false : true;
        }
        switch (state.state) {
            case 3:
                return true;
            case 4:
            case 5:
                return android.os.storage.StorageManager.isCeStorageUnlocked(userId);
            default:
                return false;
        }
    }

    boolean isSystemUserStarted() {
        synchronized (this.mLock) {
            com.android.server.am.UserState uss = this.mStartedUsers.get(0);
            if (uss == null) {
                return false;
            }
            return uss.state == 1 || uss.state == 2 || uss.state == 3;
        }
    }

    private void checkGetCurrentUserPermissions() {
        if (this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") != 0 && this.mInjector.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0) {
            java.lang.String msg = "Permission Denial: getCurrentUser() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.INTERACT_ACROSS_USERS";
            com.android.server.utils.Slogf.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
    }

    android.content.pm.UserInfo getCurrentUser() {
        android.content.pm.UserInfo currentUserLU;
        checkGetCurrentUserPermissions();
        if (this.mTargetUserId == -10000) {
            return getUserInfo(this.mCurrentUserId);
        }
        synchronized (this.mLock) {
            currentUserLU = getCurrentUserLU();
        }
        return currentUserLU;
    }

    int getCurrentUserIdChecked() {
        checkGetCurrentUserPermissions();
        if (this.mTargetUserId == -10000) {
            return this.mCurrentUserId;
        }
        return getCurrentOrTargetUserId();
    }

    private android.content.pm.UserInfo getCurrentUserLU() {
        int userId = getCurrentOrTargetUserIdLU();
        return getUserInfo(userId);
    }

    int getCurrentOrTargetUserId() {
        int currentOrTargetUserIdLU;
        synchronized (this.mLock) {
            currentOrTargetUserIdLU = getCurrentOrTargetUserIdLU();
        }
        return currentOrTargetUserIdLU;
    }

    private int getCurrentOrTargetUserIdLU() {
        return this.mTargetUserId != -10000 ? this.mTargetUserId : this.mCurrentUserId;
    }

    android.util.Pair<java.lang.Integer, java.lang.Integer> getCurrentAndTargetUserIds() {
        android.util.Pair<java.lang.Integer, java.lang.Integer> pair;
        synchronized (this.mLock) {
            pair = new android.util.Pair<>(java.lang.Integer.valueOf(this.mCurrentUserId), java.lang.Integer.valueOf(this.mTargetUserId));
        }
        return pair;
    }

    int getCurrentUserIdLU() {
        return this.mCurrentUserId;
    }

    int getCurrentUserId() {
        int i;
        synchronized (this.mLock) {
            i = this.mCurrentUserId;
        }
        return i;
    }

    private boolean isCurrentUserLU(int userId) {
        return userId == getCurrentOrTargetUserIdLU();
    }

    private boolean isAlwaysVisibleUser(int userId) {
        android.content.pm.UserProperties properties = getUserProperties(userId);
        return properties != null && properties.getAlwaysVisible();
    }

    int[] getUsers() {
        com.android.server.pm.UserManagerService ums = this.mInjector.getUserManager();
        return ums != null ? ums.getUserIds() : new int[]{0};
    }

    private android.content.pm.UserInfo getUserInfo(int userId) {
        return this.mInjector.getUserManager().getUserInfo(userId);
    }

    private android.content.pm.UserProperties getUserProperties(int userId) {
        return this.mInjector.getUserManagerInternal().getUserProperties(userId);
    }

    int[] getUserIds() {
        return this.mInjector.getUserManager().getUserIds();
    }

    int[] expandUserId(int userId) {
        if (userId != -1) {
            return new int[]{userId};
        }
        return getUsers();
    }

    boolean exists(int userId) {
        return this.mInjector.getUserManager().exists(userId);
    }

    private void checkCallingPermission(java.lang.String permission, java.lang.String methodName) {
        checkCallingHasOneOfThosePermissions(methodName, permission);
    }

    private void checkCallingHasOneOfThosePermissions(java.lang.String methodName, java.lang.String... permissions) {
        java.lang.String str;
        for (java.lang.String permission : permissions) {
            if (this.mInjector.checkCallingPermission(permission) == 0) {
                return;
            }
        }
        java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Permission denial: ").append(methodName).append("() from pid=").append(android.os.Binder.getCallingPid()).append(", uid=").append(android.os.Binder.getCallingUid()).append(" requires ");
        if (permissions.length == 1) {
            str = permissions[0];
        } else {
            str = "one of " + java.util.Arrays.toString(permissions);
        }
        java.lang.String msg = sbAppend.append(str).toString();
        com.android.server.utils.Slogf.w("ActivityManager", msg);
        throw new java.lang.SecurityException(msg);
    }

    private void enforceShellRestriction(java.lang.String restriction, int userId) {
        if (android.os.Binder.getCallingUid() == 2000) {
            if (userId < 0 || hasUserRestriction(restriction, userId)) {
                throw new java.lang.SecurityException("Shell does not have permission to access user " + userId);
            }
        }
    }

    boolean hasUserRestriction(java.lang.String restriction, int userId) {
        return this.mInjector.getUserManager().hasUserRestriction(restriction, userId);
    }

    boolean isSameProfileGroup(int callingUserId, int targetUserId) {
        boolean z = true;
        if (callingUserId == targetUserId) {
            return true;
        }
        synchronized (this.mLock) {
            int callingProfile = this.mUserProfileGroupIds.get(callingUserId, -10000);
            int targetProfile = this.mUserProfileGroupIds.get(targetUserId, -10000);
            if (callingProfile == -10000 || callingProfile != targetProfile) {
                z = false;
            }
        }
        return z;
    }

    boolean isUserOrItsParentRunning(int userId) {
        synchronized (this.mLock) {
            if (isUserRunning(userId, 0)) {
                return true;
            }
            int parentUserId = this.mUserProfileGroupIds.get(userId, -10000);
            if (parentUserId == -10000) {
                return false;
            }
            return isUserRunning(parentUserId, 0);
        }
    }

    boolean isCurrentProfile(int userId) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = com.android.internal.util.ArrayUtils.contains(this.mCurrentProfileIds, userId);
        }
        return zContains;
    }

    int[] getCurrentProfileIds() {
        int[] iArr;
        synchronized (this.mLock) {
            iArr = this.mCurrentProfileIds;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserAdded(android.content.pm.UserInfo user) {
        if (!user.isProfile()) {
            return;
        }
        synchronized (this.mLock) {
            if (user.profileGroupId == this.mCurrentUserId) {
                this.mCurrentProfileIds = com.android.internal.util.ArrayUtils.appendInt(this.mCurrentProfileIds, user.id);
            }
            if (user.profileGroupId != -10000) {
                this.mUserProfileGroupIds.put(user.id, user.profileGroupId);
            }
        }
    }

    void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            int size = this.mUserProfileGroupIds.size();
            for (int i = size - 1; i >= 0; i--) {
                if (this.mUserProfileGroupIds.keyAt(i) == userId || this.mUserProfileGroupIds.valueAt(i) == userId) {
                    this.mUserProfileGroupIds.removeAt(i);
                }
            }
            this.mCurrentProfileIds = com.android.internal.util.ArrayUtils.removeInt(this.mCurrentProfileIds, userId);
        }
    }

    protected boolean shouldConfirmCredentials(int userId) {
        android.content.pm.UserProperties properties;
        if (getStartedUserState(userId) == null || (properties = getUserProperties(userId)) == null || !properties.isCredentialShareableWithParent()) {
            return false;
        }
        if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(userId)) {
            android.app.KeyguardManager km = this.mInjector.getKeyguardManager();
            return km.isDeviceLocked(userId) && km.isDeviceSecure(userId);
        }
        return isUserRunning(userId, 2);
    }

    boolean isLockScreenDisabled(int userId) {
        return this.mLockPatternUtils.isLockScreenDisabled(userId);
    }

    void setSwitchingFromSystemUserMessage(java.lang.String switchingFromSystemUserMessage) {
        synchronized (this.mLock) {
            this.mSwitchingFromSystemUserMessage = switchingFromSystemUserMessage;
        }
    }

    void setSwitchingToSystemUserMessage(java.lang.String switchingToSystemUserMessage) {
        synchronized (this.mLock) {
            this.mSwitchingToSystemUserMessage = switchingToSystemUserMessage;
        }
    }

    java.lang.String getSwitchingFromSystemUserMessage() {
        checkHasManageUsersPermission("getSwitchingFromSystemUserMessage()");
        return getSwitchingFromSystemUserMessageUnchecked();
    }

    java.lang.String getSwitchingToSystemUserMessage() {
        checkHasManageUsersPermission("getSwitchingToSystemUserMessage()");
        return getSwitchingToSystemUserMessageUnchecked();
    }

    private java.lang.String getSwitchingFromSystemUserMessageUnchecked() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mSwitchingFromSystemUserMessage;
        }
        return str;
    }

    private java.lang.String getSwitchingToSystemUserMessageUnchecked() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mSwitchingToSystemUserMessage;
        }
        return str;
    }

    private void checkHasManageUsersPermission(java.lang.String operation) {
        if (this.mInjector.checkCallingPermission("android.permission.MANAGE_USERS") == -1) {
            throw new java.lang.SecurityException("You need MANAGE_USERS permission to call " + operation);
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        synchronized (this.mLock) {
            long token = proto.start(fieldId);
            for (int i = 0; i < this.mStartedUsers.size(); i++) {
                com.android.server.am.UserState uss = this.mStartedUsers.valueAt(i);
                long uToken = proto.start(2246267895809L);
                proto.write(1120986464257L, uss.mHandle.getIdentifier());
                uss.dumpDebug(proto, 1146756268034L);
                proto.end(uToken);
            }
            for (int i2 = 0; i2 < this.mStartedUserArray.length; i2++) {
                proto.write(2220498092034L, this.mStartedUserArray[i2]);
            }
            for (int i3 = 0; i3 < this.mUserLru.size(); i3++) {
                proto.write(2220498092035L, this.mUserLru.get(i3).intValue());
            }
            if (this.mUserProfileGroupIds.size() > 0) {
                for (int i4 = 0; i4 < this.mUserProfileGroupIds.size(); i4++) {
                    long uToken2 = proto.start(2246267895812L);
                    proto.write(1120986464257L, this.mUserProfileGroupIds.keyAt(i4));
                    proto.write(1120986464258L, this.mUserProfileGroupIds.valueAt(i4));
                    proto.end(uToken2);
                }
            }
            int i5 = this.mCurrentUserId;
            proto.write(1120986464261L, i5);
            for (int i6 = 0; i6 < this.mCurrentProfileIds.length; i6++) {
                proto.write(2220498092038L, this.mCurrentProfileIds[i6]);
            }
            proto.end(token);
        }
    }

    void dump(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("  mStartedUsers:");
            for (int i = 0; i < this.mStartedUsers.size(); i++) {
                com.android.server.am.UserState uss = this.mStartedUsers.valueAt(i);
                pw.print("    User #");
                pw.print(uss.mHandle.getIdentifier());
                pw.print(": ");
                uss.dump("", pw);
            }
            pw.print("  mStartedUserArray: [");
            for (int i2 = 0; i2 < this.mStartedUserArray.length; i2++) {
                if (i2 > 0) {
                    pw.print(", ");
                }
                pw.print(this.mStartedUserArray[i2]);
            }
            pw.println("]");
            pw.print("  mUserLru: [");
            for (int i3 = 0; i3 < this.mUserLru.size(); i3++) {
                if (i3 > 0) {
                    pw.print(", ");
                }
                pw.print(this.mUserLru.get(i3));
            }
            pw.println("]");
            if (this.mUserProfileGroupIds.size() > 0) {
                pw.println("  mUserProfileGroupIds:");
                for (int i4 = 0; i4 < this.mUserProfileGroupIds.size(); i4++) {
                    pw.print("    User #");
                    pw.print(this.mUserProfileGroupIds.keyAt(i4));
                    pw.print(" -> profile #");
                    pw.println(this.mUserProfileGroupIds.valueAt(i4));
                }
            }
            pw.println("  mCurrentProfileIds:" + java.util.Arrays.toString(this.mCurrentProfileIds));
            pw.println("  mCurrentUserId:" + this.mCurrentUserId);
            pw.println("  mTargetUserId:" + this.mTargetUserId);
            pw.println("  mLastActiveUsersForDelayedLocking:" + this.mLastActiveUsersForDelayedLocking);
            pw.println("  mDelayUserDataLocking:" + this.mDelayUserDataLocking);
            pw.println("  mAllowUserUnlocking:" + this.mAllowUserUnlocking);
            pw.println("  shouldStopUserOnSwitch():" + shouldStopUserOnSwitch());
            pw.println("  mStopUserOnSwitch:" + this.mStopUserOnSwitch);
            pw.println("  mMaxRunningUsers:" + this.mMaxRunningUsers);
            pw.println("  mBackgroundUserScheduledStopTimeSecs:" + this.mBackgroundUserScheduledStopTimeSecs);
            pw.println("  mUserSwitchUiEnabled:" + this.mUserSwitchUiEnabled);
            pw.println("  mInitialized:" + this.mInitialized);
            pw.println("  mIsBroadcastSentForSystemUserStarted:" + this.mIsBroadcastSentForSystemUserStarted);
            pw.println("  mIsBroadcastSentForSystemUserStarting:" + this.mIsBroadcastSentForSystemUserStarting);
            if (this.mSwitchingFromSystemUserMessage != null) {
                pw.println("  mSwitchingFromSystemUserMessage: " + this.mSwitchingFromSystemUserMessage);
            }
            if (this.mSwitchingToSystemUserMessage != null) {
                pw.println("  mSwitchingToSystemUserMessage: " + this.mSwitchingToSystemUserMessage);
            }
            pw.println("  mLastUserUnlockingUptime: " + this.mLastUserUnlockingUptime);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message msg) throws java.lang.Throwable {
        switch (msg.what) {
            case 10:
                dispatchUserSwitch((com.android.server.am.UserState) msg.obj, msg.arg1, msg.arg2);
                break;
            case 20:
                continueUserSwitch((com.android.server.am.UserState) msg.obj, msg.arg1, msg.arg2);
                break;
            case 30:
                timeoutUserSwitch((com.android.server.am.UserState) msg.obj, msg.arg1, msg.arg2);
                break;
            case 40:
                startProfiles();
                break;
            case 50:
                this.mInjector.batteryStatsServiceNoteEvent(32775, java.lang.Integer.toString(msg.arg1), msg.arg1);
                logUserJourneyBegin(msg.arg1, 3);
                this.mInjector.onUserStarting(msg.arg1);
                scheduleOnUserCompletedEvent(msg.arg1, 1, 5000);
                this.mInjector.getUserJourneyLogger().logUserJourneyFinish(-1, getUserInfo(msg.arg1), 3);
                break;
            case 60:
                this.mInjector.batteryStatsServiceNoteEvent(16392, java.lang.Integer.toString(msg.arg2), msg.arg2);
                this.mInjector.batteryStatsServiceNoteEvent(32776, java.lang.Integer.toString(msg.arg1), msg.arg1);
                this.mInjector.getSystemServiceManager().onUserSwitching(msg.arg2, msg.arg1);
                scheduleOnUserCompletedEvent(msg.arg1, 4, 5000);
                break;
            case 70:
                dispatchForegroundProfileChanged(msg.arg1);
                break;
            case 80:
                dispatchUserSwitchComplete(msg.arg1, msg.arg2);
                com.android.server.pm.UserJourneyLogger.UserJourneySession session = this.mInjector.getUserJourneyLogger().logUserSwitchJourneyFinish(msg.arg1, getUserInfo(msg.arg2));
                if (session != null) {
                    this.mHandler.removeMessages(200, session);
                }
                break;
            case 90:
                timeoutUserSwitchCallbacks(msg.arg1, msg.arg2);
                break;
            case 100:
                final int userId = msg.arg1;
                this.mInjector.getSystemServiceManager().onUserUnlocking(userId);
                this.mUserControllerExt.hookFgHandler(com.android.server.FgThread.getHandler()).post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleMessage$22(userId);
                    }
                });
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(msg.arg1, 5, 2);
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(msg.arg1, 6, 1);
                com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
                t.traceBegin("finishUserUnlocked-" + userId);
                finishUserUnlocked((com.android.server.am.UserState) msg.obj);
                t.traceEnd();
                break;
            case 105:
                this.mInjector.getSystemServiceManager().onUserUnlocked(msg.arg1);
                scheduleOnUserCompletedEvent(msg.arg1, 2, this.mCurrentUserId != msg.arg1 ? 1000 : 5000);
                this.mInjector.getUserJourneyLogger().logUserLifecycleEvent(msg.arg1, 6, 2);
                break;
            case 110:
                dispatchLockedBootComplete(msg.arg1);
                break;
            case 120:
                logUserJourneyBegin(msg.arg1, 2);
                startUserInForeground(msg.arg1);
                break;
            case 130:
                completeUserSwitch(msg.arg1, msg.arg2);
                break;
            case 140:
                reportOnUserCompletedEvent((java.lang.Integer) msg.obj);
                break;
            case 150:
                processScheduledStopOfBackgroundUser((java.lang.Integer) msg.obj);
                break;
            case 200:
                this.mInjector.getUserJourneyLogger().finishAndClearIncompleteUserJourney(msg.arg1, msg.arg2);
                this.mHandler.removeMessages(200, msg.obj);
                break;
            case 1000:
                android.util.Pair<android.content.pm.UserInfo, android.content.pm.UserInfo> fromToUserPair = (android.util.Pair) msg.obj;
                logUserJourneyBegin(((android.content.pm.UserInfo) fromToUserPair.second).id, 1);
                showUserSwitchDialog(fromToUserPair);
                break;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleMessage$22(int userId) {
        this.mInjector.loadUserRecents(userId);
    }

    void scheduleOnUserCompletedEvent(int userId, int eventType, int delayMs) {
        if (eventType != 0) {
            synchronized (this.mCompletedEventTypes) {
                this.mCompletedEventTypes.put(userId, this.mCompletedEventTypes.get(userId, 0) | eventType);
            }
        }
        java.lang.Object msgObj = java.lang.Integer.valueOf(userId);
        this.mHandler.removeEqualMessages(140, msgObj);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(140, msgObj), delayMs);
    }

    void reportOnUserCompletedEvent(java.lang.Integer userId) throws java.lang.InterruptedException {
        int eventTypes;
        this.mHandler.removeEqualMessages(140, userId);
        synchronized (this.mCompletedEventTypes) {
            eventTypes = this.mCompletedEventTypes.get(userId.intValue(), 0);
            this.mCompletedEventTypes.delete(userId.intValue());
        }
        int eligibleEventTypes = 0;
        synchronized (this.mLock) {
            com.android.server.am.UserState uss = this.mStartedUsers.get(userId.intValue());
            if (uss != null && uss.state != 5) {
                eligibleEventTypes = 0 | 1;
            }
            if (uss != null && uss.state == 3) {
                eligibleEventTypes |= 2;
            }
            if (userId.intValue() == this.mCurrentUserId) {
                eligibleEventTypes |= 4;
            }
        }
        com.android.server.utils.Slogf.i("ActivityManager", "reportOnUserCompletedEvent(%d): stored=%s, eligible=%s", userId, java.lang.Integer.toBinaryString(eventTypes), java.lang.Integer.toBinaryString(eligibleEventTypes));
        this.mInjector.systemServiceManagerOnUserCompletedEvent(userId.intValue(), eventTypes & eligibleEventTypes);
    }

    private void logUserJourneyBegin(int targetId, int journey) {
        com.android.server.pm.UserJourneyLogger.UserJourneySession oldSession = this.mInjector.getUserJourneyLogger().finishAndClearIncompleteUserJourney(targetId, journey);
        if (oldSession != null) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
                com.android.server.utils.Slogf.d("ActivityManager", "Starting a new journey: " + journey + " with session id: " + oldSession);
            }
            this.mHandler.removeMessages(200, oldSession);
        }
        com.android.server.pm.UserJourneyLogger.UserJourneySession newSession = this.mInjector.getUserJourneyLogger().logUserJourneyBegin(targetId, journey);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(200, targetId, journey, newSession), 90000L);
    }

    android.app.BroadcastOptions getTemporaryAppAllowlistBroadcastOptions(int reasonCode) {
        long duration = 10000;
        android.app.ActivityManagerInternal amInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        if (amInternal != null) {
            duration = amInternal.getBootTimeTempAllowListDuration();
        }
        android.app.BroadcastOptions bOptions = android.app.BroadcastOptions.makeBasic();
        bOptions.setTemporaryAppAllowlist(duration, 0, reasonCode, "");
        return bOptions;
    }

    private static int getUserSwitchTimeoutMs() {
        java.lang.String userSwitchTimeoutMs = android.os.SystemProperties.get("debug.usercontroller.user_switch_timeout_ms");
        if (!android.text.TextUtils.isEmpty(userSwitchTimeoutMs)) {
            try {
                return java.lang.Integer.parseInt(userSwitchTimeoutMs);
            } catch (java.lang.NumberFormatException e) {
                return 3000;
            }
        }
        return 3000;
    }

    private static void asyncTraceBegin(java.lang.String msg, int cookie) {
        com.android.server.utils.Slogf.d("ActivityManager", "%s - asyncTraceBegin(%d)", msg, java.lang.Integer.valueOf(cookie));
        android.os.Trace.asyncTraceBegin(64L, msg, cookie);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void asyncTraceEnd(java.lang.String msg, int cookie) {
        android.os.Trace.asyncTraceEnd(64L, msg, cookie);
        com.android.server.utils.Slogf.d("ActivityManager", "%s - asyncTraceEnd(%d)", msg, java.lang.Integer.valueOf(cookie));
    }

    public long getLastUserUnlockingUptime() {
        return this.mLastUserUnlockingUptime;
    }

    private static class UserProgressListener extends android.os.IProgressListener.Stub {
        private volatile long mUnlockStarted;

        private UserProgressListener() {
        }

        public void onStarted(int id, android.os.Bundle extras) throws android.os.RemoteException {
            com.android.server.utils.Slogf.d("ActivityManager", "Started unlocking user " + id);
            this.mUnlockStarted = android.os.SystemClock.uptimeMillis();
        }

        public void onProgress(int id, int progress, android.os.Bundle extras) throws android.os.RemoteException {
            com.android.server.utils.Slogf.d("ActivityManager", "Unlocking user " + id + " progress " + progress);
        }

        public void onFinished(int id, android.os.Bundle extras) throws android.os.RemoteException {
            long unlockTime = android.os.SystemClock.uptimeMillis() - this.mUnlockStarted;
            if (id == 0) {
                new com.android.server.utils.TimingsTraceAndSlog().logDuration("SystemUserUnlock", unlockTime);
            } else {
                new com.android.server.utils.TimingsTraceAndSlog().logDuration("User" + id + "Unlock", unlockTime);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class PendingUserStart {
        public final android.os.IProgressListener unlockListener;
        public final int userId;
        public final int userStartMode;

        PendingUserStart(int userId, int userStartMode, android.os.IProgressListener unlockListener) {
            this.userId = userId;
            this.userStartMode = userStartMode;
            this.unlockListener = unlockListener;
        }

        public java.lang.String toString() {
            return "PendingUserStart{userId=" + this.userId + ", userStartMode=" + com.android.server.pm.UserManagerInternal.userStartModeToString(this.userStartMode) + ", unlockListener=" + this.unlockListener + '}';
        }
    }

    static class Injector {
        private android.os.Handler mHandler;
        private android.os.PowerManagerInternal mPowerManagerInternal;
        private final com.android.server.am.ActivityManagerService mService;
        private com.android.server.pm.UserManagerService mUserManager;
        private com.android.server.pm.UserManagerInternal mUserManagerInternal;
        private com.android.server.am.UserSwitchingDialog mUserSwitchingDialog;
        private final java.lang.Object mUserSwitchingDialogLock = new java.lang.Object();

        Injector(com.android.server.am.ActivityManagerService service) {
            this.mService = service;
        }

        protected android.os.Handler getHandler(android.os.Handler.Callback callback) {
            android.os.Handler handler = new android.os.Handler(this.mService.mHandlerThread.getLooper(), callback);
            this.mHandler = handler;
            return handler;
        }

        protected android.os.Handler getUiHandler(android.os.Handler.Callback callback) {
            android.os.Handler handler = com.android.server.am.UserController.mStaticExt.hookGetUiHandler(callback);
            if (handler != null) {
                return handler;
            }
            return new android.os.Handler(this.mService.mUiHandler.getLooper(), callback);
        }

        protected com.android.server.pm.UserJourneyLogger getUserJourneyLogger() {
            return getUserManager().getUserJourneyLogger();
        }

        protected android.content.Context getContext() {
            return this.mService.mContext;
        }

        protected com.android.internal.widget.LockPatternUtils getLockPatternUtils() {
            return new com.android.internal.widget.LockPatternUtils(getContext());
        }

        protected int broadcastIntent(android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, java.lang.String[] requiredPermissions, int appOp, android.os.Bundle bOptions, boolean sticky, int callingPid, int callingUid, int realCallingUid, int realCallingPid, int userId) throws java.lang.Throwable {
            int logUserId;
            int logUserId2 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (logUserId2 != -10000) {
                logUserId = logUserId2;
            } else {
                logUserId = userId;
            }
            android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_SEND_USER_BROADCAST, java.lang.Integer.valueOf(logUserId), intent.getAction());
            com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
            com.android.server.am.ActivityManagerService activityManagerService = this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    try {
                        t.traceBegin("broadcastIntent-" + userId + "-" + intent.getAction());
                        int result = this.mService.broadcastIntentLocked(null, null, null, intent, resolvedType, resultTo, resultCode, resultData, resultExtras, requiredPermissions, null, null, appOp, bOptions, false, sticky, callingPid, callingUid, realCallingUid, realCallingPid, userId);
                        t.traceEnd();
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        return result;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        int checkCallingPermission(java.lang.String permission) {
            return this.mService.checkCallingPermission(permission);
        }

        com.android.server.wm.WindowManagerService getWindowManager() {
            return this.mService.mWindowManager;
        }

        com.android.server.wm.ActivityTaskManagerInternal getActivityTaskManagerInternal() {
            return this.mService.mAtmInternal;
        }

        void activityManagerOnUserStopped(int userId) {
            ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).onUserStopped(userId);
        }

        void systemServiceManagerOnUserStopped(int userId) throws java.lang.InterruptedException {
            getSystemServiceManager().onUserStopped(userId);
        }

        void systemServiceManagerOnUserCompletedEvent(int userId, int eventTypes) throws java.lang.InterruptedException {
            getSystemServiceManager().onUserCompletedEvent(userId, eventTypes);
        }

        protected com.android.server.pm.UserManagerService getUserManager() {
            if (this.mUserManager == null) {
                android.os.IBinder b = android.os.ServiceManager.getService("user");
                this.mUserManager = android.os.IUserManager.Stub.asInterface(b);
            }
            return this.mUserManager;
        }

        com.android.server.pm.UserManagerInternal getUserManagerInternal() {
            if (this.mUserManagerInternal == null) {
                this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            }
            return this.mUserManagerInternal;
        }

        android.os.PowerManagerInternal getPowerManagerInternal() {
            if (this.mPowerManagerInternal == null) {
                this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
            }
            return this.mPowerManagerInternal;
        }

        android.app.KeyguardManager getKeyguardManager() {
            return (android.app.KeyguardManager) this.mService.mContext.getSystemService(android.app.KeyguardManager.class);
        }

        void batteryStatsServiceNoteEvent(int code, java.lang.String name, int uid) {
            this.mService.mBatteryStatsService.noteEvent(code, name, uid);
        }

        boolean isRuntimeRestarted() {
            return getSystemServiceManager().isRuntimeRestarted();
        }

        com.android.server.SystemServiceManager getSystemServiceManager() {
            return this.mService.mSystemServiceManager;
        }

        boolean isFirstBootOrUpgrade() {
            android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
            try {
                if (!pm.isFirstBoot()) {
                    if (!pm.isDeviceUpgrading()) {
                        return false;
                    }
                }
                return true;
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        void sendPreBootBroadcast(int userId, boolean quiet, final java.lang.Runnable onFinish) {
            android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UC_SEND_USER_BROADCAST, java.lang.Integer.valueOf(userId), "android.intent.action.PRE_BOOT_COMPLETED");
            new com.android.server.am.PreBootBroadcaster(this.mService, userId, null, quiet) { // from class: com.android.server.am.UserController.Injector.1
                @Override // com.android.server.am.PreBootBroadcaster
                public void onFinished() {
                    onFinish.run();
                }
            }.sendNext();
        }

        void activityManagerForceStopPackage(int userId, java.lang.String reason) {
            com.android.server.am.ActivityManagerService activityManagerService = this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    this.mService.forceStopPackageLocked(null, -1, false, false, true, false, false, false, userId, reason);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        int checkComponentPermission(java.lang.String permission, int pid, int uid, int owningUid, boolean exported) {
            return com.android.server.am.ActivityManagerService.checkComponentPermission(permission, pid, uid, owningUid, exported);
        }

        boolean checkPermissionForPreflight(java.lang.String permission, int pid, int uid, java.lang.String pkg) {
            return android.content.PermissionChecker.checkPermissionForPreflight(getContext(), permission, pid, uid, pkg) == 0;
        }

        protected void startHomeActivity(int userId, java.lang.String reason) {
            this.mService.mAtmInternal.startHomeActivity(userId, reason);
        }

        void startUserWidgets(final int userId) {
            final android.appwidget.AppWidgetManagerInternal awm = (android.appwidget.AppWidgetManagerInternal) com.android.server.LocalServices.getService(android.appwidget.AppWidgetManagerInternal.class);
            if (awm != null) {
                com.android.server.am.UserController.mStaticExt.hookFgHandler(com.android.server.FgThread.getHandler()).post(new java.lang.Runnable() { // from class: com.android.server.am.UserController$Injector$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        awm.unlockUser(userId);
                    }
                });
            }
        }

        void updateUserConfiguration() {
            this.mService.mAtmInternal.updateUserConfiguration();
        }

        void clearBroadcastQueueForUser(int userId) {
            com.android.server.am.ActivityManagerService activityManagerService = this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    this.mService.clearBroadcastQueueForUserLocked(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        void loadUserRecents(int userId) {
            this.mService.mAtmInternal.loadRecentTasksForUser(userId);
        }

        void startPersistentApps(int matchFlags) {
            this.mService.startPersistentApps(matchFlags);
        }

        void installEncryptionUnawareProviders(int userId) {
            this.mService.mCpHelper.installEncryptionUnawareProviders(userId);
        }

        void dismissUserSwitchingDialog(java.lang.Runnable onDismissed) {
            synchronized (this.mUserSwitchingDialogLock) {
                if (this.mUserSwitchingDialog != null) {
                    this.mUserSwitchingDialog.dismiss(onDismissed);
                    this.mUserSwitchingDialog = null;
                } else if (onDismissed != null) {
                    onDismissed.run();
                }
            }
        }

        void showUserSwitchingDialog(android.content.pm.UserInfo fromUser, android.content.pm.UserInfo toUser, java.lang.String switchingFromSystemUserMessage, java.lang.String switchingToSystemUserMessage, java.lang.Runnable onShown) {
            if (this.mService.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
                com.android.server.utils.Slogf.w("ActivityManager", "Showing user switch dialog on UserController, it could cause a race condition if it's shown by CarSystemUI as well");
            }
            synchronized (this.mUserSwitchingDialogLock) {
                dismissUserSwitchingDialog(null);
                this.mUserSwitchingDialog = new com.android.server.am.UserSwitchingDialog(this.mService.mContext, fromUser, toUser, switchingFromSystemUserMessage, switchingToSystemUserMessage, getWindowManager());
                this.mUserSwitchingDialog.show(onShown);
            }
        }

        void reportGlobalUsageEvent(int event) {
            this.mService.reportGlobalUsageEvent(event);
        }

        void reportCurWakefulnessUsageEvent() {
            this.mService.reportCurWakefulnessUsageEvent();
        }

        void taskSupervisorRemoveUser(int userId) {
            this.mService.mAtmInternal.removeUser(userId);
        }

        protected boolean taskSupervisorSwitchUser(int userId, com.android.server.am.UserState uss) {
            return this.mService.mAtmInternal.switchUser(userId, uss);
        }

        protected void taskSupervisorResumeFocusedStackTopActivity() {
            this.mService.mAtmInternal.resumeTopActivities(false);
        }

        protected void clearAllLockedTasks(java.lang.String reason) {
            this.mService.mAtmInternal.clearLockedTasks(reason);
        }

        boolean isCallerRecents(int callingUid) {
            return this.mService.mAtmInternal.isCallerRecents(callingUid);
        }

        protected android.os.storage.IStorageManager getStorageManager() {
            return android.os.storage.IStorageManager.Stub.asInterface(android.os.ServiceManager.getService("mount"));
        }

        protected void showKeyguard(final java.lang.Runnable runnable) {
            if (getWindowManager().isKeyguardLocked()) {
                runnable.run();
            } else {
                getActivityTaskManagerInternal().registerScreenObserver(new com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver() { // from class: com.android.server.am.UserController.Injector.2
                    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
                    public void onAwakeStateChanged(boolean isAwake) {
                    }

                    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
                    public void onKeyguardStateChanged(boolean isShowing) {
                        android.util.Slog.d("ActivityManager", "UserController#onKeyguardStateChanged, isShowing = " + isShowing);
                        if (isShowing) {
                            com.android.server.am.UserController.Injector.this.getActivityTaskManagerInternal().unregisterScreenObserver(this);
                            runnable.run();
                        }
                    }
                });
                getWindowManager().lockDeviceNow();
            }
        }

        protected void dismissKeyguard(final java.lang.Runnable runnable) {
            getWindowManager().dismissKeyguard(new com.android.internal.policy.IKeyguardDismissCallback.Stub() { // from class: com.android.server.am.UserController.Injector.3
                public void onDismissError() throws android.os.RemoteException {
                    runnable.run();
                }

                public void onDismissSucceeded() throws android.os.RemoteException {
                    runnable.run();
                }

                public void onDismissCancelled() throws android.os.RemoteException {
                    runnable.run();
                }
            }, null);
        }

        boolean isHeadlessSystemUserMode() {
            return android.os.UserManager.isHeadlessSystemUserMode();
        }

        boolean isUsersOnSecondaryDisplaysEnabled() {
            return android.os.UserManager.isVisibleBackgroundUsersEnabled();
        }

        void onUserStarting(int userId) throws java.lang.InterruptedException {
            getSystemServiceManager().onUserStarting(com.android.server.utils.TimingsTraceAndSlog.newAsyncLog(), userId);
        }

        void setPerformancePowerMode(boolean enabled) {
            com.android.server.utils.Slogf.i("ActivityManager", "Setting power mode MODE_FIXED_PERFORMANCE to " + enabled);
            getPowerManagerInternal().setPowerMode(3, enabled);
        }

        void onSystemUserVisibilityChanged(boolean visible) {
            getUserManagerInternal().onSystemUserVisibilityChanged(visible);
        }
    }

    public com.android.server.am.IUserControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class UserControllerWrapper implements com.android.server.am.IUserControllerWrapper {
        private UserControllerWrapper() {
        }

        @Override // com.android.server.am.IUserControllerWrapper
        public android.util.SparseIntArray getUserProfileGroupIds() {
            return com.android.server.am.UserController.this.mUserProfileGroupIds;
        }

        @Override // com.android.server.am.IUserControllerWrapper
        public void startUserInForeground(int targetUserId) {
            com.android.server.am.UserController.this.startUserInForeground(targetUserId);
        }

        @Override // com.android.server.am.IUserControllerWrapper
        public boolean maybeUnlockUser(int userId) {
            return com.android.server.am.UserController.this.maybeUnlockUser(userId);
        }
    }
}
