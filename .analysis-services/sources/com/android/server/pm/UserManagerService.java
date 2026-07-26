package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class UserManagerService extends android.os.IUserManager.Stub {
    private static final int ALLOWED_FLAGS_FOR_CREATE_USERS_PERMISSION = 38700;
    private static final java.lang.String ATTR_CONVERTED_FROM_PRE_CREATED = "convertedFromPreCreated";
    private static final java.lang.String ATTR_CREATION_TIME = "created";
    private static final java.lang.String ATTR_FLAGS = "flags";
    private static final java.lang.String ATTR_GUEST_TO_REMOVE = "guestToRemove";
    private static final java.lang.String ATTR_ICON_PATH = "icon";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_KEY = "key";
    private static final java.lang.String ATTR_LAST_ENTERED_FOREGROUND_TIME = "lastEnteredForeground";
    private static final java.lang.String ATTR_LAST_LOGGED_IN_FINGERPRINT = "lastLoggedInFingerprint";
    private static final java.lang.String ATTR_LAST_LOGGED_IN_TIME = "lastLoggedIn";
    private static final java.lang.String ATTR_MULTIPLE = "m";
    private static final java.lang.String ATTR_NEXT_SERIAL_NO = "nextSerialNumber";
    private static final java.lang.String ATTR_PARTIAL = "partial";
    private static final java.lang.String ATTR_PRE_CREATED = "preCreated";
    private static final java.lang.String ATTR_PROFILE_BADGE = "profileBadge";
    private static final java.lang.String ATTR_PROFILE_GROUP_ID = "profileGroupId";
    private static final java.lang.String ATTR_RESTRICTED_PROFILE_PARENT_ID = "restrictedProfileParentId";
    private static final java.lang.String ATTR_SEED_ACCOUNT_NAME = "seedAccountName";
    private static final java.lang.String ATTR_SEED_ACCOUNT_TYPE = "seedAccountType";
    private static final java.lang.String ATTR_SERIAL_NO = "serialNumber";
    private static final java.lang.String ATTR_TYPE = "type";
    private static final java.lang.String ATTR_TYPE_BOOLEAN = "b";
    private static final java.lang.String ATTR_TYPE_BUNDLE = "B";
    private static final java.lang.String ATTR_TYPE_BUNDLE_ARRAY = "BA";
    private static final java.lang.String ATTR_TYPE_INTEGER = "i";
    private static final java.lang.String ATTR_TYPE_STRING = "s";
    private static final java.lang.String ATTR_TYPE_STRING_ARRAY = "sa";
    private static final java.lang.String ATTR_USER_TYPE_VERSION = "userTypeConfigVersion";
    private static final java.lang.String ATTR_USER_VERSION = "version";
    private static final java.lang.String ATTR_VALUE_TYPE = "type";
    private static final long BOOT_USER_SET_TIMEOUT_MS = 300000;
    private static final java.lang.String CUSTOM_BIOMETRIC_PROMPT_LOGO_DESCRIPTION_KEY = "custom_logo_description";
    private static final java.lang.String CUSTOM_BIOMETRIC_PROMPT_LOGO_RES_ID_KEY = "custom_logo_res_id";
    static final boolean DBG = false;
    public static final boolean DBG_ALLOCATION = false;
    static final boolean DBG_MUMD = false;
    private static final boolean DBG_WITH_STACKTRACE = false;
    private static final long EPOCH_PLUS_30_YEARS = 946080000000L;
    private static final java.lang.String LOG_TAG = "UserManagerService";
    static final int MAX_RECENTLY_REMOVED_IDS_SIZE = 100;
    static final int MAX_USER_ID = 21473;
    static final int MIN_USER_ID = 10;
    private static final long PRIVATE_SPACE_AUTO_LOCK_INACTIVITY_TIMEOUT_MS = 300000;
    private static final java.lang.String PRIVATE_SPACE_AUTO_LOCK_TIMER_TAG = "PrivateSpaceAutoLockTimer";
    private static final boolean RELEASE_DELETED_USER_ID = false;
    private static final java.lang.String RESTRICTIONS_FILE_PREFIX = "res_";
    private static final java.lang.String TAG_ACCOUNT = "account";
    private static final java.lang.String TAG_DEVICE_OWNER_USER_ID = "deviceOwnerUserId";
    private static final java.lang.String TAG_DEVICE_POLICY_GLOBAL_RESTRICTIONS = "device_policy_global_restrictions";
    private static final java.lang.String TAG_DEVICE_POLICY_LOCAL_RESTRICTIONS = "device_policy_local_restrictions";
    private static final java.lang.String TAG_DEVICE_POLICY_RESTRICTIONS = "device_policy_restrictions";
    private static final java.lang.String TAG_ENTRY = "entry";
    private static final java.lang.String TAG_GLOBAL_RESTRICTION_OWNER_ID = "globalRestrictionOwnerUserId";
    private static final java.lang.String TAG_GUEST_RESTRICTIONS = "guestRestrictions";
    private static final java.lang.String TAG_IGNORE_PREPARE_STORAGE_ERRORS = "ignorePrepareStorageErrors";
    private static final java.lang.String TAG_LAST_REQUEST_QUIET_MODE_ENABLED_CALL = "lastRequestQuietModeEnabledCall";
    private static final java.lang.String TAG_NAME = "name";
    private static final java.lang.String TAG_RESTRICTIONS = "restrictions";
    private static final java.lang.String TAG_SEED_ACCOUNT_OPTIONS = "seedAccountOptions";
    private static final java.lang.String TAG_USER = "user";
    private static final java.lang.String TAG_USERS = "users";
    private static final java.lang.String TAG_USER_PROPERTIES = "userProperties";
    private static final java.lang.String TAG_VALUE = "value";
    private static final java.lang.String TRON_DEMO_CREATED = "users_demo_created";
    private static final java.lang.String TRON_GUEST_CREATED = "users_guest_created";
    private static final java.lang.String TRON_USER_CREATED = "users_user_created";
    private static final java.lang.String USER_LIST_FILENAME = "userlist.xml";
    private static final java.lang.String USER_PHOTO_FILENAME = "photo.png";
    private static final java.lang.String USER_PHOTO_FILENAME_TMP = "photo.png.tmp";
    private static final int USER_VERSION = 11;
    static final int WRITE_USER_DELAY = 2000;
    static final int WRITE_USER_LIST_MSG = 2;
    static final int WRITE_USER_MSG = 1;
    private static final java.lang.String XML_SUFFIX = ".xml";
    private static com.android.server.pm.UserManagerService sInstance;
    private final java.lang.String ACTION_DISABLE_QUIET_MODE_AFTER_UNLOCK;
    private android.app.ActivityManagerInternal mAmInternal;
    private com.android.internal.app.IAppOpsService mAppOpsService;
    private final java.lang.Object mAppRestrictionsLock;
    private final com.android.server.pm.RestrictionsSet mAppliedUserRestrictions;
    private final com.android.server.pm.RestrictionsSet mBaseUserRestrictions;
    private int mBootUser;
    private final java.util.concurrent.CountDownLatch mBootUserLatch;
    private final com.android.server.pm.RestrictionsSet mCachedEffectiveUserRestrictions;
    private final android.content.BroadcastReceiver mConfigurationChangeReceiver;
    private final android.content.Context mContext;
    private final android.content.BroadcastReceiver mDeviceInactivityBroadcastReceiver;
    private android.app.admin.DevicePolicyManagerInternal mDevicePolicyManagerInternal;
    private final com.android.server.pm.RestrictionsSet mDevicePolicyUserRestrictions;
    private final android.content.BroadcastReceiver mDisableQuietModeCallback;
    private boolean mForceEphemeralUsers;
    private final android.os.Bundle mGuestRestrictions;
    private final android.os.Handler mHandler;
    private final java.util.concurrent.ThreadPoolExecutor mInternalExecutor;
    private boolean mIsDeviceInactivityBroadcastReceiverRegistered;
    private boolean mIsDeviceManaged;
    private final android.util.SparseBooleanArray mIsUserManaged;
    private android.app.KeyguardManager.KeyguardLockedStateListener mKeyguardLockedStateListener;
    private final android.content.res.Configuration mLastConfiguration;
    private final com.android.server.pm.UserManagerService.LocalService mLocalService;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private int mNextSerialNumber;
    private final java.util.concurrent.atomic.AtomicReference<java.lang.String> mOwnerName;
    private final android.util.TypedValue mOwnerNameTypedValue;
    private final java.lang.Object mPackagesLock;
    private final com.android.server.pm.PackageManagerService mPm;
    private android.content.pm.PackageManagerInternal mPmInternal;
    private final com.android.server.pm.UserManagerService.SettingsObserver mPrivateSpaceAutoLockSettingsObserver;
    private com.android.server.pm.UserManagerService.PrivateSpaceAutoLockTimer mPrivateSpaceAutoLockTimer;
    private final java.util.LinkedList<java.lang.Integer> mRecentlyRemovedIds;
    private final android.util.SparseBooleanArray mRemovingUserIds;
    private final java.lang.Object mRestrictionsLock;
    private final com.android.server.pm.UserSystemPackageInstaller mSystemPackageInstaller;
    private boolean mUpdatingSystemUserMode;
    public final java.util.concurrent.atomic.AtomicInteger mUser0Allocations;
    private final com.android.server.pm.UserDataPreparer mUserDataPreparer;
    private int[] mUserIds;
    private int[] mUserIdsIncludingPreCreated;
    private final com.android.server.pm.UserJourneyLogger mUserJourneyLogger;
    private final java.util.ArrayList<com.android.server.pm.UserManagerInternal.UserLifecycleListener> mUserLifecycleListeners;
    private final java.io.File mUserListFile;
    private com.android.server.pm.IUserManagerServiceExt mUserManagerServiceExt;
    private final android.os.IBinder mUserRestrictionToken;
    private final java.util.ArrayList<com.android.server.pm.UserManagerInternal.UserRestrictionsListener> mUserRestrictionsListeners;
    private final com.android.server.pm.UserManagerService.WatchedUserStates mUserStates;
    private int mUserTypeVersion;
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails> mUserTypes;
    private int mUserVersion;
    private final com.android.server.pm.UserVisibilityMediator mUserVisibilityMediator;
    private final android.util.SparseArray<com.android.server.pm.UserManagerService.UserData> mUsers;
    private final java.io.File mUsersDir;
    private final java.lang.Object mUsersLock;
    private com.android.server.pm.UserManagerService.UserManagerServiceWrapper mWrapper;
    private static final java.lang.String USER_INFO_DIR = "system" + java.io.File.separator + "users";
    private static final long PRIVATE_SPACE_AUTO_LOCK_INACTIVITY_ALARM_WINDOW_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(55);

    static class UserData {
        java.lang.String account;
        android.content.pm.UserInfo info;
        private boolean mIgnorePrepareStorageErrors;
        long mLastEnteredForegroundTimeMillis;
        private long mLastRequestQuietModeEnabledMillis;
        boolean persistSeedData;
        java.lang.String seedAccountName;
        android.os.PersistableBundle seedAccountOptions;
        java.lang.String seedAccountType;
        long startRealtime;
        long unlockRealtime;
        android.content.pm.UserProperties userProperties;

        UserData() {
        }

        void setLastRequestQuietModeEnabledMillis(long millis) {
            this.mLastRequestQuietModeEnabledMillis = millis;
        }

        long getLastRequestQuietModeEnabledMillis() {
            return this.mLastRequestQuietModeEnabledMillis;
        }

        boolean getIgnorePrepareStorageErrors() {
            return this.mIgnorePrepareStorageErrors;
        }

        void setIgnorePrepareStorageErrors() {
            if (android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT < 33) {
                this.mIgnorePrepareStorageErrors = true;
            } else {
                android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, "Not setting mIgnorePrepareStorageErrors to true since this is a new device");
            }
        }

        void clearSeedAccountData() {
            this.seedAccountName = null;
            this.seedAccountType = null;
            this.seedAccountOptions = null;
            this.persistSeedData = false;
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (com.android.server.pm.UserManagerService.isAutoLockForPrivateSpaceEnabled()) {
                java.lang.String path = uri.getLastPathSegment();
                if (android.text.TextUtils.equals(path, "private_space_auto_lock")) {
                    int autoLockPreference = android.provider.Settings.Secure.getIntForUser(com.android.server.pm.UserManagerService.this.mContext.getContentResolver(), "private_space_auto_lock", 2, com.android.server.pm.UserManagerService.this.getMainUserIdUnchecked());
                    android.util.Slog.i(com.android.server.pm.UserManagerService.LOG_TAG, "Auto-lock settings changed to " + autoLockPreference);
                    com.android.server.pm.UserManagerService.this.setOrUpdateAutoLockPreferenceForPrivateProfile(autoLockPreference);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingAutoLockAlarms() {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        if (alarmManager != null && this.mPrivateSpaceAutoLockTimer != null) {
            alarmManager.cancel(this.mPrivateSpaceAutoLockTimer);
        }
    }

    void maybeScheduleAlarmToAutoLockPrivateSpace() {
        int privateSpaceAutoLockPreference = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "private_space_auto_lock", 2, getMainUserIdUnchecked());
        if (privateSpaceAutoLockPreference != 1) {
            com.android.server.utils.Slogf.d(LOG_TAG, "Not scheduling auto-lock on inactivity,preference is set to %d", java.lang.Integer.valueOf(privateSpaceAutoLockPreference));
            return;
        }
        int privateProfileUserId = getPrivateProfileUserId();
        if (privateProfileUserId != -10000) {
            if (isQuietModeEnabled(privateProfileUserId)) {
                com.android.server.utils.Slogf.d(LOG_TAG, "Not scheduling auto-lock alarm for %d, quiet mode already enabled", java.lang.Integer.valueOf(privateProfileUserId));
            } else {
                scheduleAlarmToAutoLockPrivateSpace(privateProfileUserId, 300000L);
            }
        }
    }

    void scheduleAlarmToAutoLockPrivateSpace(int userId, long delayInMillis) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        if (alarmManager == null) {
            android.util.Slog.e(LOG_TAG, "AlarmManager not available, cannot schedule auto-lock alarm");
            return;
        }
        initPrivateSpaceAutoLockTimer(userId);
        long alarmWindowStartTime = android.os.SystemClock.elapsedRealtime() + delayInMillis;
        alarmManager.setWindow(2, alarmWindowStartTime, PRIVATE_SPACE_AUTO_LOCK_INACTIVITY_ALARM_WINDOW_MS, PRIVATE_SPACE_AUTO_LOCK_TIMER_TAG, (java.util.concurrent.Executor) new android.os.HandlerExecutor(this.mHandler), (android.app.AlarmManager.OnAlarmListener) this.mPrivateSpaceAutoLockTimer);
    }

    private void initPrivateSpaceAutoLockTimer(int userId) {
        cancelPendingAutoLockAlarms();
        if (this.mPrivateSpaceAutoLockTimer == null || this.mPrivateSpaceAutoLockTimer.getUserId() != userId) {
            this.mPrivateSpaceAutoLockTimer = new com.android.server.pm.UserManagerService.PrivateSpaceAutoLockTimer(userId);
        }
    }

    private class PrivateSpaceAutoLockTimer implements android.app.AlarmManager.OnAlarmListener {
        private final int mUserId;

        PrivateSpaceAutoLockTimer(int userId) {
            this.mUserId = userId;
        }

        int getUserId() {
            return this.mUserId;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            android.os.PowerManager powerManager = (android.os.PowerManager) com.android.server.pm.UserManagerService.this.mContext.getSystemService(android.os.PowerManager.class);
            if (powerManager == null || powerManager.isInteractive()) {
                android.util.Slog.i(com.android.server.pm.UserManagerService.LOG_TAG, "Device is interactive, skipping auto-lock for profile user " + this.mUserId);
            } else {
                android.util.Slog.i(com.android.server.pm.UserManagerService.LOG_TAG, "Auto-locking private space with user-id " + this.mUserId);
                com.android.server.pm.UserManagerService.this.setQuietModeEnabledAsync(this.mUserId, true, null, com.android.server.pm.UserManagerService.this.mContext.getPackageName());
            }
        }
    }

    private void initializeAndRegisterKeyguardLockedStateListener() {
        this.mKeyguardLockedStateListener = new android.app.KeyguardManager.KeyguardLockedStateListener() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda2
            @Override // android.app.KeyguardManager.KeyguardLockedStateListener
            public final void onKeyguardLockedStateChanged(boolean z) {
                this.f$0.tryAutoLockingPrivateSpaceOnKeyguardChanged(z);
            }
        };
        try {
            android.app.KeyguardManager keyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class);
            android.util.Slog.i(LOG_TAG, "Adding keyguard locked state listener");
            keyguardManager.addKeyguardLockedStateListener(new android.os.HandlerExecutor(this.mHandler), this.mKeyguardLockedStateListener);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(LOG_TAG, "Error adding keyguard locked listener ", e);
        }
    }

    void setOrUpdateAutoLockPreferenceForPrivateProfile(int autoLockPreference) {
        int privateProfileUserId = getPrivateProfileUserId();
        if (privateProfileUserId == -10000) {
            android.util.Slog.e(LOG_TAG, "Auto-lock preference updated but private space user not found");
            return;
        }
        if (autoLockPreference == 1) {
            if (!this.mIsDeviceInactivityBroadcastReceiverRegistered) {
                android.util.Slog.i(LOG_TAG, "Registering device inactivity broadcast receivers");
                this.mContext.registerReceiver(this.mDeviceInactivityBroadcastReceiver, new android.content.IntentFilter("android.intent.action.SCREEN_OFF"), null, this.mHandler);
                this.mContext.registerReceiver(this.mDeviceInactivityBroadcastReceiver, new android.content.IntentFilter("android.intent.action.SCREEN_ON"), null, this.mHandler);
                this.mIsDeviceInactivityBroadcastReceiverRegistered = true;
            }
        } else if (this.mIsDeviceInactivityBroadcastReceiverRegistered) {
            android.util.Slog.i(LOG_TAG, "Removing device inactivity broadcast receivers");
            cancelPendingAutoLockAlarms();
            this.mContext.unregisterReceiver(this.mDeviceInactivityBroadcastReceiver);
            this.mIsDeviceInactivityBroadcastReceiverRegistered = false;
        }
        if (autoLockPreference == 0) {
            initializeAndRegisterKeyguardLockedStateListener();
            return;
        }
        try {
            android.app.KeyguardManager keyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class);
            android.util.Slog.i(LOG_TAG, "Removing keyguard locked state listener");
            keyguardManager.removeKeyguardLockedStateListener(this.mKeyguardLockedStateListener);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(LOG_TAG, "Error adding keyguard locked state listener ", e);
        }
    }

    void tryAutoLockingPrivateSpaceOnKeyguardChanged(boolean isKeyguardLocked) {
        if (isAutoLockForPrivateSpaceEnabled()) {
            int autoLockPreference = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "private_space_auto_lock", 2, getMainUserIdUnchecked());
            boolean isAutoLockOnDeviceLockSelected = autoLockPreference == 0;
            if (isKeyguardLocked && isAutoLockOnDeviceLockSelected) {
                autoLockPrivateSpace();
            }
        }
    }

    void autoLockPrivateSpace() {
        int privateProfileUserId = getPrivateProfileUserId();
        if (privateProfileUserId != -10000) {
            android.util.Slog.i(LOG_TAG, "Auto-locking private space with user-id " + privateProfileUserId);
            setQuietModeEnabledAsync(privateProfileUserId, true, null, this.mContext.getPackageName());
        }
    }

    void setQuietModeEnabledAsync(final int userId, final boolean enableQuietMode, final android.content.IntentSender target, final java.lang.String callingPackage) {
        if (android.multiuser.Flags.moveQuietModeOperationsToSeparateThread()) {
            android.util.Slog.d(LOG_TAG, "Calling setQuietModeEnabled for user " + userId + " on a separate thread");
            this.mInternalExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setQuietModeEnabledAsync$0(userId, enableQuietMode, target, callingPackage);
                }
            });
        } else {
            com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setQuietModeEnabledAsync$1(userId, enableQuietMode, target, callingPackage);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DisableQuietModeUserUnlockedCallback extends android.os.IProgressListener.Stub {
        private final android.content.IntentSender mTarget;

        public DisableQuietModeUserUnlockedCallback(android.content.IntentSender target) {
            java.util.Objects.requireNonNull(target);
            this.mTarget = target;
        }

        public void onStarted(int id, android.os.Bundle extras) {
        }

        public void onProgress(int id, int progress, android.os.Bundle extras) {
        }

        public void onFinished(int id, android.os.Bundle extras) {
            com.android.server.pm.UserManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerService$DisableQuietModeUserUnlockedCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onFinished$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFinished$0() {
            try {
                android.app.ActivityOptions activityOptions = android.app.ActivityOptions.makeBasic().setPendingIntentBackgroundActivityStartMode(1);
                com.android.server.pm.UserManagerService.this.mContext.startIntentSender(this.mTarget, null, 0, 0, 0, activityOptions.toBundle());
            } catch (android.content.IntentSender.SendIntentException e) {
                android.util.Slog.e(com.android.server.pm.UserManagerService.LOG_TAG, "Failed to start the target in the callback", e);
            }
        }
    }

    private class WatchedUserStates {
        final android.util.SparseIntArray states = new android.util.SparseIntArray();

        public WatchedUserStates() {
            invalidateIsUserUnlockedCache();
        }

        public int get(int userId) {
            return this.states.get(userId);
        }

        public int get(int userId, int fallback) {
            return this.states.indexOfKey(userId) >= 0 ? this.states.get(userId) : fallback;
        }

        public void put(int userId, int state) {
            this.states.put(userId, state);
            invalidateIsUserUnlockedCache();
        }

        public void delete(int userId) {
            this.states.delete(userId);
            invalidateIsUserUnlockedCache();
        }

        public boolean has(int userId) {
            return this.states.get(userId, -10000) != -10000;
        }

        public java.lang.String toString() {
            return this.states.toString();
        }

        private void invalidateIsUserUnlockedCache() {
            android.os.UserManager.invalidateIsUserUnlockedCache();
        }
    }

    public static com.android.server.pm.UserManagerService getInstance() {
        com.android.server.pm.UserManagerService userManagerService;
        synchronized (com.android.server.pm.UserManagerService.class) {
            userManagerService = sInstance;
        }
        return userManagerService;
    }

    public static class LifeCycle extends com.android.server.SystemService {
        private com.android.server.pm.UserManagerService mUms;

        public LifeCycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mUms = com.android.server.pm.UserManagerService.getInstance();
            publishBinderService(com.android.server.pm.UserManagerService.TAG_USER, this.mUms);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                this.mUms.cleanupPartialUsers();
                if (this.mUms.mPm.isDeviceUpgrading()) {
                    this.mUms.cleanupPreCreatedUsers();
                }
                this.mUms.registerStatsCallbacks();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser targetUser) {
            synchronized (this.mUms.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData user = this.mUms.getUserDataLU(targetUser.getUserIdentifier());
                if (user != null) {
                    user.startRealtime = android.os.SystemClock.elapsedRealtime();
                    if (targetUser.getUserIdentifier() == 0 && targetUser.isFull()) {
                        this.mUms.setLastEnteredForegroundTimeToNow(user);
                    }
                }
            }
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser targetUser) {
            synchronized (this.mUms.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData user = this.mUms.getUserDataLU(targetUser.getUserIdentifier());
                if (user != null) {
                    user.unlockRealtime = android.os.SystemClock.elapsedRealtime();
                }
            }
            if (targetUser.getUserIdentifier() == 0 && android.os.UserManager.isCommunalProfileEnabled()) {
                this.mUms.startCommunalProfile();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
            synchronized (this.mUms.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData user = this.mUms.getUserDataLU(to.getUserIdentifier());
                if (user != null) {
                    this.mUms.setLastEnteredForegroundTimeToNow(user);
                }
            }
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser targetUser) {
            synchronized (this.mUms.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData user = this.mUms.getUserDataLU(targetUser.getUserIdentifier());
                if (user != null) {
                    user.startRealtime = 0L;
                    user.unlockRealtime = 0L;
                }
            }
        }
    }

    UserManagerService(android.content.Context context) {
        this(context, null, null, new java.lang.Object(), context.getCacheDir(), null);
    }

    UserManagerService(android.content.Context context, com.android.server.pm.PackageManagerService pm, com.android.server.pm.UserDataPreparer userDataPreparer, java.lang.Object packagesLock) {
        this(context, pm, userDataPreparer, packagesLock, android.os.Environment.getDataDirectory(), null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    UserManagerService(android.content.Context context, com.android.server.pm.PackageManagerService packageManagerService, com.android.server.pm.UserDataPreparer userDataPreparer, java.lang.Object obj, java.io.File file, android.util.SparseArray<com.android.server.pm.UserManagerService.UserData> sparseArray) throws java.lang.Throwable {
        this.mUsersLock = com.android.server.LockGuard.installNewLock(2);
        this.mRestrictionsLock = new java.lang.Object();
        this.mAppRestrictionsLock = new java.lang.Object();
        this.mUserRestrictionToken = new android.os.Binder();
        this.mBootUserLatch = new java.util.concurrent.CountDownLatch(1);
        this.mBaseUserRestrictions = new com.android.server.pm.RestrictionsSet();
        this.mCachedEffectiveUserRestrictions = new com.android.server.pm.RestrictionsSet();
        this.mAppliedUserRestrictions = new com.android.server.pm.RestrictionsSet();
        this.mDevicePolicyUserRestrictions = new com.android.server.pm.RestrictionsSet();
        this.mGuestRestrictions = new android.os.Bundle();
        this.mRemovingUserIds = new android.util.SparseBooleanArray();
        this.mRecentlyRemovedIds = new java.util.LinkedList<>();
        this.mUserVersion = 0;
        this.mUserTypeVersion = 0;
        this.mIsUserManaged = new android.util.SparseBooleanArray();
        this.mUserRestrictionsListeners = new java.util.ArrayList<>();
        this.mUserLifecycleListeners = new java.util.ArrayList<>();
        this.mUserJourneyLogger = new com.android.server.pm.UserJourneyLogger();
        this.ACTION_DISABLE_QUIET_MODE_AFTER_UNLOCK = "com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK";
        this.mDisableQuietModeCallback = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.UserManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (!"com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK".equals(intent.getAction())) {
                    return;
                }
                android.content.IntentSender target = (android.content.IntentSender) intent.getParcelableExtra("android.intent.extra.INTENT", android.content.IntentSender.class);
                int userId = intent.getIntExtra("android.intent.extra.USER_ID", -10000);
                java.lang.String callingPackage = intent.getStringExtra("android.intent.extra.PACKAGE_NAME");
                com.android.server.pm.UserManagerService.this.setQuietModeEnabledAsync(userId, false, target, callingPackage);
            }
        };
        this.mIsDeviceInactivityBroadcastReceiverRegistered = false;
        this.mDeviceInactivityBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.UserManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (com.android.server.pm.UserManagerService.isAutoLockForPrivateSpaceEnabled()) {
                    if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                        com.android.server.pm.UserManagerService.this.maybeScheduleAlarmToAutoLockPrivateSpace();
                    } else if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                        android.util.Slog.d(com.android.server.pm.UserManagerService.LOG_TAG, "SCREEN_ON broadcast received, removing pending alarms to auto-lock private space");
                        com.android.server.pm.UserManagerService.this.cancelPendingAutoLockAlarms();
                    }
                }
            }
        };
        this.mOwnerName = new java.util.concurrent.atomic.AtomicReference<>();
        this.mOwnerNameTypedValue = new android.util.TypedValue();
        this.mLastConfiguration = new android.content.res.Configuration();
        this.mConfigurationChangeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.UserManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (!"android.intent.action.CONFIGURATION_CHANGED".equals(intent.getAction())) {
                    return;
                }
                com.android.server.pm.UserManagerService.this.invalidateOwnerNameIfNecessary(context2.getResources(), false);
            }
        };
        this.mUserStates = new com.android.server.pm.UserManagerService.WatchedUserStates();
        this.mBootUser = -10000;
        java.lang.Object[] objArr = 0;
        this.mWrapper = new com.android.server.pm.UserManagerService.UserManagerServiceWrapper();
        this.mUserManagerServiceExt = (com.android.server.pm.IUserManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IUserManagerServiceExt.class).base(this).create();
        this.mContext = context;
        this.mWrapper.getExtImpl().init(this.mContext);
        this.mPm = packageManagerService;
        this.mPackagesLock = obj;
        this.mUsers = sparseArray != null ? sparseArray : new android.util.SparseArray<>();
        this.mHandler = new com.android.server.pm.UserManagerService.MainHandler();
        this.mInternalExecutor = new java.util.concurrent.ThreadPoolExecutor(0, 1, 24L, java.util.concurrent.TimeUnit.HOURS, new java.util.concurrent.LinkedBlockingQueue());
        this.mUserVisibilityMediator = new com.android.server.pm.UserVisibilityMediator(this.mHandler);
        this.mUserDataPreparer = userDataPreparer;
        this.mUserTypes = com.android.server.pm.UserTypeFactory.getUserTypes();
        invalidateOwnerNameIfNecessary(context.getResources(), true);
        synchronized (this.mPackagesLock) {
            try {
                try {
                    this.mUsersDir = new java.io.File(file, USER_INFO_DIR);
                    this.mUsersDir.mkdirs();
                    new java.io.File(this.mUsersDir, java.lang.String.valueOf(0)).mkdirs();
                    android.os.FileUtils.setPermissions(this.mUsersDir.toString(), 509, -1, -1);
                    this.mUserListFile = new java.io.File(this.mUsersDir, USER_LIST_FILENAME);
                    initDefaultGuestRestrictions();
                    readUserListLP();
                    sInstance = this;
                    this.mSystemPackageInstaller = new com.android.server.pm.UserSystemPackageInstaller(this, this.mUserTypes);
                    this.mLocalService = new com.android.server.pm.UserManagerService.LocalService();
                    com.android.server.LocalServices.addService(com.android.server.pm.UserManagerInternal.class, this.mLocalService);
                    this.mLockPatternUtils = new com.android.internal.widget.LockPatternUtils(this.mContext);
                    this.mUserStates.put(0, 0);
                    this.mUser0Allocations = null;
                    this.mPrivateSpaceAutoLockSettingsObserver = new com.android.server.pm.UserManagerService.SettingsObserver(this.mHandler);
                    emulateSystemUserModeIfNeeded();
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private boolean doesDeviceHardwareSupportPrivateSpace() {
        return (this.mPm.hasSystemFeature("android.hardware.type.embedded", 0) || this.mPm.hasSystemFeature("android.hardware.type.watch", 0) || this.mPm.hasSystemFeature("android.software.leanback", 0) || this.mPm.hasSystemFeature("android.hardware.type.automotive", 0)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAutoLockForPrivateSpaceEnabled() {
        return com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.supportAutolockForPrivateSpace() && android.multiuser.Flags.enablePrivateSpaceFeatures();
    }

    void systemReady() {
        int mainUserId;
        this.mAppOpsService = com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
        synchronized (this.mRestrictionsLock) {
            applyUserRestrictionsLR(0);
        }
        this.mContext.registerReceiver(this.mDisableQuietModeCallback, new android.content.IntentFilter("com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK"), null, this.mHandler);
        this.mContext.registerReceiver(this.mConfigurationChangeReceiver, new android.content.IntentFilter("android.intent.action.CONFIGURATION_CHANGED"), null, this.mHandler);
        if (isAutoLockForPrivateSpaceEnabled() && (mainUserId = getMainUserIdUnchecked()) != -10000) {
            this.mContext.getContentResolver().registerContentObserverAsUser(android.provider.Settings.Secure.getUriFor("private_space_auto_lock"), false, this.mPrivateSpaceAutoLockSettingsObserver, android.os.UserHandle.of(mainUserId));
            setOrUpdateAutoLockPreferenceForPrivateProfile(android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "private_space_auto_lock", 2, mainUserId));
        }
        if (isAutoLockingPrivateSpaceOnRestartsEnabled()) {
            autoLockPrivateSpace();
        }
        this.mWrapper.getExtImpl().systemReady();
    }

    private boolean isAutoLockingPrivateSpaceOnRestartsEnabled() {
        return com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceAutolockOnRestarts() && android.multiuser.Flags.enablePrivateSpaceFeatures();
    }

    com.android.server.pm.UserManagerInternal getInternalForInjectorOnly() {
        return this.mLocalService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCommunalProfile() {
        int communalProfileId = getCommunalProfileIdUnchecked();
        if (communalProfileId != -10000) {
            com.android.server.utils.Slogf.d(LOG_TAG, "Starting the Communal Profile");
            boolean started = false;
            try {
                started = android.app.ActivityManager.getService().startProfile(communalProfileId);
            } catch (android.os.RemoteException e) {
                e.rethrowAsRuntimeException();
            }
            if (!started) {
                com.android.server.utils.Slogf.wtf(LOG_TAG, "Failed to start communal profile userId=%d", java.lang.Integer.valueOf(communalProfileId));
                return;
            }
            return;
        }
        com.android.server.utils.Slogf.w(LOG_TAG, "Cannot start Communal Profile because there isn't one");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupPartialUsers() {
        java.util.ArrayList<android.content.pm.UserInfo> partials = new java.util.ArrayList<>();
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo ui = this.mUsers.valueAt(i).info;
                if ((ui.partial || ui.guestToRemove) && ui.id != 0) {
                    partials.add(ui);
                    if (!this.mRemovingUserIds.get(ui.id)) {
                        addRemovingUserIdLocked(ui.id);
                    }
                    ui.partial = true;
                }
            }
        }
        int partialsSize = partials.size();
        for (int i2 = 0; i2 < partialsSize; i2++) {
            android.content.pm.UserInfo ui2 = partials.get(i2);
            android.util.Slog.w(LOG_TAG, "Removing partially created user " + ui2.id + " (name=" + ui2.name + ")");
            removeUserState(ui2.id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupPreCreatedUsers() {
        java.util.ArrayList<android.content.pm.UserInfo> preCreatedUsers;
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            preCreatedUsers = new java.util.ArrayList<>(userSize);
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo ui = this.mUsers.valueAt(i).info;
                if (ui.preCreated) {
                    preCreatedUsers.add(ui);
                    addRemovingUserIdLocked(ui.id);
                    ui.flags |= 64;
                    ui.partial = true;
                }
            }
        }
        int preCreatedSize = preCreatedUsers.size();
        for (int i2 = 0; i2 < preCreatedSize; i2++) {
            android.content.pm.UserInfo ui2 = preCreatedUsers.get(i2);
            android.util.Slog.i(LOG_TAG, "Removing pre-created user " + ui2.id);
            removeUserState(ui2.id);
        }
    }

    public java.lang.String getUserAccount(int userId) {
        java.lang.String str;
        checkManageUserAndAcrossUsersFullPermission("get user account");
        synchronized (this.mUsersLock) {
            str = this.mUsers.get(userId).account;
        }
        return str;
    }

    public void setUserAccount(int userId, java.lang.String accountName) {
        checkManageUserAndAcrossUsersFullPermission("set user account");
        com.android.server.pm.UserManagerService.UserData userToUpdate = null;
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
                if (userData == null) {
                    android.util.Slog.e(LOG_TAG, "User not found for setting user account: u" + userId);
                    return;
                }
                java.lang.String currentAccount = userData.account;
                if (!java.util.Objects.equals(currentAccount, accountName)) {
                    userData.account = accountName;
                    userToUpdate = userData;
                }
                if (userToUpdate != null) {
                    writeUserLP(userToUpdate);
                }
            }
        }
    }

    public android.content.pm.UserInfo getPrimaryUser() {
        checkManageUsersPermission("query users");
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo ui = this.mUsers.valueAt(i).info;
                if (ui.isPrimary() && !this.mRemovingUserIds.get(ui.id)) {
                    return ui;
                }
            }
            return null;
        }
    }

    public int getMainUserId() {
        checkQueryOrCreateUsersPermission("get main user id");
        return getMainUserIdUnchecked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMainUserIdUnchecked() {
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo user = this.mUsers.valueAt(i).info;
                if (user.isMain() && !this.mRemovingUserIds.get(user.id)) {
                    return user.id;
                }
            }
            return -10000;
        }
    }

    private int getPrivateProfileUserId() {
        synchronized (this.mUsersLock) {
            for (int userId : getUserIds()) {
                android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
                if (userInfo != null && userInfo.isPrivateProfile()) {
                    return userInfo.id;
                }
            }
            return -10000;
        }
    }

    public void setBootUser(int userId) {
        checkCreateUsersPermission("Set boot user");
        synchronized (this.mUsersLock) {
            com.android.server.utils.Slogf.i(LOG_TAG, "setBootUser %d", java.lang.Integer.valueOf(userId));
            this.mBootUser = userId;
        }
        this.mBootUserLatch.countDown();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public int getBootUser() throws android.os.ServiceSpecificException {
        checkCreateUsersPermission("Get boot user");
        try {
            return getBootUserUnchecked();
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    public int getBootUserUnchecked() throws android.os.UserManager.CheckedUserOperationException {
        synchronized (this.mUsersLock) {
            if (this.mBootUser != -10000) {
                com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(this.mBootUser);
                if (userData != null && userData.info.supportsSwitchToByUser()) {
                    com.android.server.utils.Slogf.i(LOG_TAG, "Using provided boot user: %d", java.lang.Integer.valueOf(this.mBootUser));
                    return this.mBootUser;
                }
                com.android.server.utils.Slogf.w(LOG_TAG, "Provided boot user cannot be switched to: %d", java.lang.Integer.valueOf(this.mBootUser));
            }
            if (isHeadlessSystemUserMode()) {
                int previousUser = getPreviousFullUserToEnterForeground();
                if (previousUser != -10000) {
                    com.android.server.utils.Slogf.i(LOG_TAG, "Boot user is previous user %d", java.lang.Integer.valueOf(previousUser));
                    return previousUser;
                }
                synchronized (this.mUsersLock) {
                    int userSize = this.mUsers.size();
                    for (int i = 0; i < userSize; i++) {
                        com.android.server.pm.UserManagerService.UserData userData2 = this.mUsers.valueAt(i);
                        if (userData2.info.supportsSwitchToByUser()) {
                            int firstSwitchable = userData2.info.id;
                            com.android.server.utils.Slogf.i(LOG_TAG, "Boot user is first switchable user %d", java.lang.Integer.valueOf(firstSwitchable));
                            return firstSwitchable;
                        }
                    }
                    throw new android.os.UserManager.CheckedUserOperationException("No switchable users found", 1);
                }
            }
            return 0;
        }
    }

    public int getPreviousFullUserToEnterForeground() {
        checkQueryOrCreateUsersPermission("get previous user");
        int previousUser = -10000;
        long latestEnteredTime = 0;
        int currentUser = getCurrentUserId();
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                com.android.server.pm.UserManagerService.UserData userData = this.mUsers.valueAt(i);
                int userId = userData.info.id;
                if (userId != currentUser && userData.info.isFull() && !userData.info.partial && userData.info.isEnabled() && !this.mRemovingUserIds.get(userId)) {
                    long userEnteredTime = userData.mLastEnteredForegroundTimeMillis;
                    if (userEnteredTime > latestEnteredTime) {
                        latestEnteredTime = userEnteredTime;
                        previousUser = userId;
                    }
                }
            }
        }
        return previousUser;
    }

    public int getCommunalProfileId() {
        checkQueryOrCreateUsersPermission("get communal profile user id");
        return getCommunalProfileIdUnchecked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCommunalProfileIdUnchecked() {
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo user = this.mUsers.valueAt(i).info;
                if (user.isCommunalProfile() && !this.mRemovingUserIds.get(user.id)) {
                    return user.id;
                }
            }
            return -10000;
        }
    }

    public java.util.List<android.content.pm.UserInfo> getUsers(boolean excludeDying) {
        return getUsers(true, excludeDying, true);
    }

    public java.util.List<android.content.pm.UserInfo> getUsers(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) {
        checkCreateUsersPermission("query users");
        return getUsersInternal(excludePartial, excludeDying, excludePreCreated);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.UserInfo> getUsersInternal(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) {
        java.util.ArrayList<android.content.pm.UserInfo> users;
        synchronized (this.mUsersLock) {
            users = new java.util.ArrayList<>(this.mUsers.size());
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo ui = this.mUsers.valueAt(i).info;
                if ((!excludePartial || !ui.partial) && ((!excludeDying || !this.mRemovingUserIds.get(ui.id)) && (!excludePreCreated || !ui.preCreated))) {
                    users.add(userWithName(ui));
                }
            }
        }
        return users;
    }

    public java.util.List<android.content.pm.UserInfo> getProfiles(int userId, boolean enabledOnly) {
        boolean returnFullInfo;
        java.util.List<android.content.pm.UserInfo> profilesLU;
        if (userId != android.os.UserHandle.getCallingUserId()) {
            checkQueryOrCreateUsersPermission("getting profiles related to user " + userId);
            returnFullInfo = true;
        } else {
            returnFullInfo = hasCreateUsersPermission();
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mUsersLock) {
                profilesLU = getProfilesLU(userId, null, enabledOnly, returnFullInfo);
            }
            return profilesLU;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public int[] getProfileIds(int userId, boolean enabledOnly) {
        return getProfileIds(userId, null, enabledOnly, false);
    }

    public int[] getProfileIds(int userId, java.lang.String userType, boolean enabledOnly, boolean excludeHidden) {
        int[] array;
        if (userId != android.os.UserHandle.getCallingUserId()) {
            checkQueryOrCreateUsersPermission("getting profiles related to user " + userId);
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mUsersLock) {
                array = getProfileIdsLU(userId, userType, enabledOnly, excludeHidden).toArray();
            }
            return array;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private java.util.List<android.content.pm.UserInfo> getProfilesLU(int userId, java.lang.String userType, boolean enabledOnly, boolean fullInfo) {
        android.content.pm.UserInfo userInfo;
        android.util.IntArray profileIds = getProfileIdsLU(userId, userType, enabledOnly, false);
        java.util.ArrayList<android.content.pm.UserInfo> users = new java.util.ArrayList<>(profileIds.size());
        for (int i = 0; i < profileIds.size(); i++) {
            int profileId = profileIds.get(i);
            android.content.pm.UserInfo userInfo2 = this.mUsers.get(profileId).info;
            if (!fullInfo) {
                userInfo = new android.content.pm.UserInfo(userInfo2);
                userInfo.name = null;
                userInfo.iconPath = null;
            } else {
                userInfo = userWithName(userInfo2);
            }
            if (userInfo != null) {
                users.add(userInfo);
            }
        }
        return users;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.IntArray getProfileIdsLU(int userId, java.lang.String userType, boolean enabledOnly, boolean excludeHidden) {
        android.content.pm.UserInfo user = getUserInfoLU(userId);
        android.util.IntArray result = new android.util.IntArray(this.mUsers.size());
        if (user == null) {
            return result;
        }
        int userSize = this.mUsers.size();
        for (int i = 0; i < userSize; i++) {
            android.content.pm.UserInfo profile = this.mUsers.valueAt(i).info;
            if (isProfileOf(user, profile) && ((!enabledOnly || profile.isEnabled()) && !this.mRemovingUserIds.get(profile.id) && !profile.partial && ((userType == null || userType.equals(profile.userType)) && (!excludeHidden || !isProfileHidden(profile.id))))) {
                result.add(profile.id);
            }
        }
        return result;
    }

    public int[] getProfileIdsExcludingHidden(int userId, boolean enabledOnly) {
        return getProfileIds(userId, null, enabledOnly, true);
    }

    private boolean isProfileHidden(int userId) {
        android.content.pm.UserProperties userProperties = getUserPropertiesCopy(userId);
        return com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enableHidingProfiles() && android.multiuser.Flags.enablePrivateSpaceFeatures() && userProperties.getProfileApiVisibility() == 1;
    }

    public int getCredentialOwnerProfile(int userId) {
        checkManageUsersPermission("get the credential owner");
        if (!this.mLockPatternUtils.isSeparateProfileChallengeEnabled(userId)) {
            synchronized (this.mUsersLock) {
                android.content.pm.UserInfo profileParent = getProfileParentLU(userId);
                if (profileParent != null) {
                    return profileParent.id;
                }
            }
        }
        return userId;
    }

    public boolean isSameProfileGroup(int userId, int otherUserId) {
        if (userId == otherUserId) {
            return true;
        }
        checkQueryUsersPermission("check if in the same profile group");
        return isSameProfileGroupNoChecks(userId, otherUserId);
    }

    private boolean isSameProfileGroupNoChecks(int userId, int otherUserId) {
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            if (userInfo != null && userInfo.profileGroupId != -10000) {
                android.content.pm.UserInfo otherUserInfo = getUserInfoLU(otherUserId);
                if (otherUserInfo != null && otherUserInfo.profileGroupId != -10000) {
                    return userInfo.profileGroupId == otherUserInfo.profileGroupId;
                }
                return false;
            }
            return false;
        }
    }

    private boolean isSameUserOrProfileGroupOrTargetIsCommunal(android.content.pm.UserInfo asker, android.content.pm.UserInfo target) {
        if (asker.id == target.id) {
            return true;
        }
        if (android.multiuser.Flags.supportCommunalProfile() && target.isCommunalProfile()) {
            return true;
        }
        return asker.profileGroupId != -10000 && asker.profileGroupId == target.profileGroupId;
    }

    public android.content.pm.UserInfo getProfileParent(int userId) {
        android.content.pm.UserInfo profileParentLU;
        if (!hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            throw new java.lang.SecurityException("You need MANAGE_USERS or INTERACT_ACROSS_USERS permission to get the profile parent");
        }
        synchronized (this.mUsersLock) {
            profileParentLU = getProfileParentLU(userId);
        }
        return profileParentLU;
    }

    public int getProfileParentId(int userId) {
        checkManageUsersPermission("get the profile parent");
        return getProfileParentIdUnchecked(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getProfileParentIdUnchecked(int userId) {
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo profileParent = getProfileParentLU(userId);
            if (profileParent == null) {
                return userId;
            }
            return profileParent.id;
        }
    }

    private android.content.pm.UserInfo getProfileParentLU(int userId) {
        int parentUserId;
        android.content.pm.UserInfo profile = getUserInfoLU(userId);
        if (profile == null || (parentUserId = profile.profileGroupId) == userId || parentUserId == -10000) {
            return null;
        }
        return getUserInfoLU(parentUserId);
    }

    private static boolean isProfileOf(android.content.pm.UserInfo user, android.content.pm.UserInfo profile) {
        return user.id == profile.id || (user.profileGroupId != -10000 && user.profileGroupId == profile.profileGroupId);
    }

    private java.lang.String getAvailabilityIntentAction(boolean enableQuietMode, boolean useManagedActions) {
        if (useManagedActions) {
            if (enableQuietMode) {
                return "android.intent.action.MANAGED_PROFILE_UNAVAILABLE";
            }
            return "android.intent.action.MANAGED_PROFILE_AVAILABLE";
        }
        if (enableQuietMode) {
            return "android.intent.action.PROFILE_UNAVAILABLE";
        }
        return "android.intent.action.PROFILE_AVAILABLE";
    }

    private void broadcastProfileAvailabilityChanges(android.content.pm.UserInfo profileInfo, android.os.UserHandle parentHandle, boolean enableQuietMode, boolean useManagedActions) {
        android.content.Intent availabilityIntent = new android.content.Intent();
        availabilityIntent.setAction(getAvailabilityIntentAction(enableQuietMode, useManagedActions));
        availabilityIntent.putExtra("android.intent.extra.QUIET_MODE", enableQuietMode);
        availabilityIntent.putExtra("android.intent.extra.USER", profileInfo.getUserHandle());
        availabilityIntent.putExtra("android.intent.extra.user_handle", profileInfo.getUserHandle().getIdentifier());
        if (profileInfo.isManagedProfile()) {
            getDevicePolicyManagerInternal().broadcastIntentToManifestReceivers(availabilityIntent, parentHandle, true);
        }
        availabilityIntent.addFlags(1342177280);
        android.os.Bundle options = new android.app.BroadcastOptions().setDeferralPolicy(2).setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey(useManagedActions ? "android.intent.action.MANAGED_PROFILE_AVAILABLE" : "android.intent.action.PROFILE_AVAILABLE", java.lang.String.valueOf(profileInfo.getUserHandle().getIdentifier())).toBundle();
        this.mContext.sendBroadcastAsUser(availabilityIntent, parentHandle, null, options);
    }

    public boolean requestQuietModeEnabled(java.lang.String callingPackage, boolean enableQuietMode, int userId, android.content.IntentSender target, int flags) {
        android.content.pm.UserInfo userInfo;
        android.content.pm.UserProperties userProperties;
        java.util.Objects.requireNonNull(callingPackage);
        if (enableQuietMode && target != null) {
            throw new java.lang.IllegalArgumentException("target should only be specified when we are disabling quiet mode.");
        }
        boolean dontAskCredential = (flags & 2) != 0;
        boolean onlyIfCredentialNotRequired = (flags & 1) != 0;
        if (dontAskCredential && onlyIfCredentialNotRequired) {
            throw new java.lang.IllegalArgumentException("invalid flags: " + flags);
        }
        ensureCanModifyQuietMode(callingPackage, android.os.Binder.getCallingUid(), userId, target != null, dontAskCredential);
        if (onlyIfCredentialNotRequired && callingPackage.equals(getPackageManagerInternal().getSystemUiServiceComponent().getPackageName())) {
            throw new java.lang.SecurityException("SystemUI is not allowed to set QUIET_MODE_DISABLE_ONLY_IF_CREDENTIAL_NOT_REQUIRED");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        if (dontAskCredential) {
            try {
                synchronized (this.mUsersLock) {
                    userInfo = getUserInfo(userId);
                }
                if (userInfo == null) {
                    throw new java.lang.IllegalArgumentException("Invalid user. Can't find user details for userId " + userId);
                }
                if (!userInfo.isManagedProfile()) {
                    throw new java.lang.IllegalArgumentException("Invalid flags: " + flags + ". Can't skip credential check for the user");
                }
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        }
        if (enableQuietMode) {
            lambda$setQuietModeEnabledAsync$1(userId, true, target, callingPackage);
            android.os.Binder.restoreCallingIdentity(identity);
            return true;
        }
        if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures() && (userProperties = getUserPropertiesInternal(userId)) != null && userProperties.isAuthAlwaysRequiredToDisableQuietMode()) {
            if (onlyIfCredentialNotRequired) {
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            }
            android.app.KeyguardManager km = (android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class);
            int parentUserId = getProfileParentId(userId);
            if (km != null && km.isDeviceSecure(parentUserId)) {
                showConfirmCredentialToDisableQuietMode(userId, target, callingPackage);
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            }
            if (km != null && !km.isDeviceSecure(parentUserId) && android.multiuser.Flags.showSetScreenLockDialog() && android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 0, userId) == 1) {
                android.content.Intent setScreenLockPromptIntent = com.android.internal.app.SetScreenLockDialogActivity.createBaseIntent(1);
                setScreenLockPromptIntent.putExtra("origin_user_id", userId);
                this.mContext.startActivityAsUser(setScreenLockPromptIntent, android.os.UserHandle.of(parentUserId));
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            }
            android.util.Slog.w(LOG_TAG, "Allowing profile unlock even when device credentials are not set for user " + userId);
        }
        boolean hasUnifiedChallenge = this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(userId);
        if (hasUnifiedChallenge && (!((android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class)).isDeviceLocked(this.mLocalService.getProfileParentId(userId)) || onlyIfCredentialNotRequired)) {
            this.mLockPatternUtils.tryUnlockWithCachedUnifiedChallenge(userId);
        }
        boolean needToShowConfirmCredential = (dontAskCredential || !this.mLockPatternUtils.isSecure(userId) || (hasUnifiedChallenge && android.os.storage.StorageManager.isCeStorageUnlocked(userId))) ? false : true;
        if (!needToShowConfirmCredential) {
            lambda$setQuietModeEnabledAsync$1(userId, false, target, callingPackage);
            android.os.Binder.restoreCallingIdentity(identity);
            return true;
        }
        if (onlyIfCredentialNotRequired) {
            android.os.Binder.restoreCallingIdentity(identity);
            return false;
        }
        showConfirmCredentialToDisableQuietMode(userId, target, callingPackage);
        android.os.Binder.restoreCallingIdentity(identity);
        return false;
    }

    private void ensureCanModifyQuietMode(java.lang.String callingPackage, int callingUid, int targetUserId, boolean startIntent, boolean dontAskCredential) {
        verifyCallingPackage(callingPackage, callingUid);
        if (hasManageUsersPermission()) {
            return;
        }
        if (startIntent) {
            throw new java.lang.SecurityException("MANAGE_USERS permission is required to start intent after disabling quiet mode.");
        }
        if (dontAskCredential) {
            throw new java.lang.SecurityException("MANAGE_USERS permission is required to disable quiet mode without credentials.");
        }
        if (!isSameProfileGroupNoChecks(android.os.UserHandle.getUserId(callingUid), targetUserId)) {
            throw new java.lang.SecurityException("MANAGE_USERS permission is required to modify quiet mode for a different profile group.");
        }
        boolean hasModifyQuietModePermission = hasPermissionGranted("android.permission.MODIFY_QUIET_MODE", callingUid);
        if (hasModifyQuietModePermission) {
            return;
        }
        android.content.pm.ShortcutServiceInternal shortcutInternal = (android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class);
        if (shortcutInternal != null) {
            boolean isForegroundLauncher = shortcutInternal.isForegroundDefaultLauncher(callingPackage, callingUid);
            if (isForegroundLauncher) {
                return;
            }
        }
        throw new java.lang.SecurityException("Can't modify quiet mode, caller is neither foreground default launcher nor has MANAGE_USERS/MODIFY_QUIET_MODE permission");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setQuietModeEnabled, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$setQuietModeEnabledAsync$1(int userId, boolean enableQuietMode, android.content.IntentSender target, java.lang.String callingPackage) {
        com.android.server.pm.UserManagerService.DisableQuietModeUserUnlockedCallback disableQuietModeUserUnlockedCallback;
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo profile = getUserInfoLU(userId);
            android.content.pm.UserInfo parent = getProfileParentLU(userId);
            if (this.mWrapper.getExtImpl().isMultiAppUser(userId)) {
                android.util.Slog.i(LOG_TAG, "should not setQuietMode for multiapp user: " + userId);
                return;
            }
            if (profile == null || !profile.isProfile()) {
                throw new java.lang.IllegalArgumentException("User " + userId + " is not a profile");
            }
            if (profile.isQuietModeEnabled() == enableQuietMode) {
                android.util.Slog.i(LOG_TAG, "Quiet mode is already " + enableQuietMode);
                return;
            }
            profile.flags ^= 128;
            com.android.server.pm.UserManagerService.UserData profileUserData = getUserDataLU(profile.id);
            synchronized (this.mPackagesLock) {
                writeUserLP(profileUserData);
            }
            try {
                if (enableQuietMode) {
                    stopUserForQuietMode(userId);
                    ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).killForegroundAppsForUser(userId);
                } else {
                    if (target != null) {
                        disableQuietModeUserUnlockedCallback = new com.android.server.pm.UserManagerService.DisableQuietModeUserUnlockedCallback(target);
                    } else {
                        disableQuietModeUserUnlockedCallback = null;
                    }
                    android.app.ActivityManager.getService().startProfileWithListener(userId, disableQuietModeUserUnlockedCallback);
                }
            } catch (android.os.RemoteException e) {
                e.rethrowAsRuntimeException();
            }
            logQuietModeEnabled(userId, enableQuietMode, callingPackage);
            if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures()) {
                broadcastProfileAvailabilityChanges(profile, parent.getUserHandle(), enableQuietMode, false);
            }
            if (profile.isManagedProfile()) {
                broadcastProfileAvailabilityChanges(profile, parent.getUserHandle(), enableQuietMode, true);
            }
        }
    }

    private void stopUserForQuietMode(int userId) throws android.os.RemoteException {
        if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enableBiometricsToUnlockPrivateSpace() && android.multiuser.Flags.enablePrivateSpaceFeatures()) {
            android.app.ActivityManager.getService().stopUserWithDelayedLocking(userId, (android.app.IStopUserCallback) null);
        } else {
            android.app.ActivityManager.getService().stopUserWithCallback(userId, (android.app.IStopUserCallback) null);
        }
    }

    private void logQuietModeEnabled(int userId, boolean enableQuietMode, java.lang.String callingPackage) {
        com.android.server.pm.UserManagerService.UserData userData;
        long period;
        com.android.server.utils.Slogf.i(LOG_TAG, "requestQuietModeEnabled called by package %s, with enableQuietMode %b.", callingPackage, java.lang.Boolean.valueOf(enableQuietMode));
        synchronized (this.mUsersLock) {
            userData = getUserDataLU(userId);
        }
        if (userData == null) {
            return;
        }
        long now = java.lang.System.currentTimeMillis();
        if (userData.getLastRequestQuietModeEnabledMillis() != 0) {
            period = now - userData.getLastRequestQuietModeEnabledMillis();
        } else {
            period = now - userData.info.creationTime;
        }
        android.app.admin.DevicePolicyEventLogger.createEvent(55).setInt(com.android.server.pm.UserJourneyLogger.getUserTypeForStatsd(userData.info.userType)).setStrings(new java.lang.String[]{callingPackage}).setBoolean(enableQuietMode).setTimePeriod(period).write();
        userData.setLastRequestQuietModeEnabledMillis(now);
    }

    public boolean isQuietModeEnabled(int userId) {
        android.content.pm.UserInfo info;
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                info = getUserInfoLU(userId);
            }
            if (info != null && info.isProfile()) {
                return info.isQuietModeEnabled();
            }
            return false;
        }
    }

    private void showConfirmCredentialToDisableQuietMode(int userId, android.content.IntentSender target, java.lang.String callingPackage) {
        int state;
        if (android.app.admin.flags.Flags.quietModeCredentialBugFix() && (!android.multiuser.Flags.restrictQuietModeCredentialBugFixToManagedProfiles() || getUserInfo(userId).isManagedProfile())) {
            synchronized (this.mUserStates) {
                state = this.mUserStates.get(userId, -1);
            }
            if (state != -1) {
                android.util.Slog.i(LOG_TAG, "showConfirmCredentialToDisableQuietMode() called too early, managed user " + userId + " is still alive.");
                return;
            }
        }
        android.app.KeyguardManager km = (android.app.KeyguardManager) this.mContext.getSystemService("keyguard");
        android.content.Intent unlockIntent = km.createConfirmDeviceCredentialIntent(null, null, userId);
        if (unlockIntent == null) {
            return;
        }
        android.content.Intent callBackIntent = new android.content.Intent("com.android.server.pm.DISABLE_QUIET_MODE_AFTER_UNLOCK");
        if (target != null) {
            callBackIntent.putExtra("android.intent.extra.INTENT", target);
        }
        callBackIntent.putExtra("android.intent.extra.USER_ID", userId);
        callBackIntent.setPackage(this.mContext.getPackageName());
        callBackIntent.putExtra("android.intent.extra.PACKAGE_NAME", callingPackage);
        callBackIntent.addFlags(268435456);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(this.mContext, 0, callBackIntent, 1409286144);
        unlockIntent.putExtra("android.intent.extra.INTENT", pendingIntent.getIntentSender());
        unlockIntent.setFlags(276824064);
        if (android.multiuser.Flags.enablePrivateSpaceFeatures() && android.multiuser.Flags.usePrivateSpaceIconInBiometricPrompt() && getUserInfo(userId).isPrivateProfile()) {
            unlockIntent.putExtra(CUSTOM_BIOMETRIC_PROMPT_LOGO_RES_ID_KEY, android.R.drawable.spinner_ab_pressed_holo_light);
            unlockIntent.putExtra(CUSTOM_BIOMETRIC_PROMPT_LOGO_DESCRIPTION_KEY, this.mContext.getString(android.R.string.policydesc_watchLogin));
        }
        this.mContext.startActivityAsUser(unlockIntent, android.os.UserHandle.of(getProfileParentIdUnchecked(userId)));
    }

    public void setUserEnabled(int userId) {
        android.content.pm.UserInfo info;
        checkManageUsersPermission("enable user");
        boolean wasUserDisabled = false;
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                info = getUserInfoLU(userId);
                if (info != null && !info.isEnabled()) {
                    wasUserDisabled = true;
                    info.flags ^= 64;
                    writeUserLP(getUserDataLU(info.id));
                }
            }
        }
        if (wasUserDisabled && info != null && info.isProfile()) {
            sendProfileAddedBroadcast(info.profileGroupId, info.id);
        }
    }

    public void setUserAdmin(int userId) throws java.lang.Throwable {
        checkManageUserAndAcrossUsersFullPermission("set user admin");
        this.mUserJourneyLogger.logUserJourneyBegin(userId, 7);
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData user = getUserDataLU(userId);
                if (user == null) {
                    this.mUserJourneyLogger.logNullUserJourneyError(7, getCurrentUserId(), userId, "", -1);
                    return;
                }
                if (user.info.isAdmin()) {
                    this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), user.info, 7, 5);
                    return;
                }
                user.info.flags ^= 2;
                writeUserLP(user);
                this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), user.info, 7, -1);
            }
        }
    }

    public void revokeUserAdmin(int userId) throws java.lang.Throwable {
        checkManageUserAndAcrossUsersFullPermission("revoke admin privileges");
        this.mUserJourneyLogger.logUserJourneyBegin(userId, 8);
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData user = getUserDataLU(userId);
                if (user == null) {
                    this.mUserJourneyLogger.logNullUserJourneyError(8, getCurrentUserId(), userId, "", -1);
                    return;
                }
                if (!user.info.isAdmin()) {
                    this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), user.info, 8, 6);
                    return;
                }
                user.info.flags ^= 2;
                writeUserLP(user);
                this.mUserJourneyLogger.logUserJourneyFinishWithError(getCurrentUserId(), user.info, 8, -1);
            }
        }
    }

    public void evictCredentialEncryptionKey(int userId) {
        int userStartMode;
        checkManageUsersPermission("evict CE key");
        android.app.IActivityManager am = android.app.ActivityManagerNative.getDefault();
        long identity = android.os.Binder.clearCallingIdentity();
        if (isProfileUnchecked(userId)) {
            userStartMode = 3;
        } else {
            userStartMode = 2;
        }
        try {
            try {
                am.restartUserInBackground(userId, userStartMode);
            } catch (android.os.RemoteException re) {
                throw re.rethrowAsRuntimeException();
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean isUserOfType(int userId, java.lang.String userType) {
        checkQueryOrCreateUsersPermission("check user type");
        return userType != null && userType.equals(getUserTypeNoChecks(userId));
    }

    private java.lang.String getUserTypeNoChecks(int userId) {
        java.lang.String str;
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            str = userInfo != null ? userInfo.userType : null;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.pm.UserTypeDetails getUserTypeDetailsNoChecks(int userId) {
        java.lang.String typeStr = getUserTypeNoChecks(userId);
        if (typeStr != null) {
            return this.mUserTypes.get(typeStr);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.pm.UserTypeDetails getUserTypeDetails(android.content.pm.UserInfo userInfo) {
        java.lang.String typeStr = userInfo != null ? userInfo.userType : null;
        if (typeStr != null) {
            return this.mUserTypes.get(typeStr);
        }
        return null;
    }

    public android.content.pm.UserInfo getUserInfo(int userId) {
        android.content.pm.UserInfo userInfoUserWithName;
        checkQueryOrCreateUsersPermission("query user");
        synchronized (this.mUsersLock) {
            userInfoUserWithName = userWithName(getUserInfoLU(userId));
        }
        return userInfoUserWithName;
    }

    private android.content.pm.UserInfo userWithName(android.content.pm.UserInfo orig) {
        if (orig != null && orig.name == null) {
            java.lang.String name = null;
            if (orig.id == 0 || orig.isMain()) {
                name = getOwnerName();
            } else if (orig.isGuest()) {
                name = getGuestName();
            }
            if (name != null) {
                android.content.pm.UserInfo withName = new android.content.pm.UserInfo(orig);
                withName.name = name;
                return withName;
            }
        }
        return orig;
    }

    boolean isUserTypeSubtypeOfFull(java.lang.String userType) {
        com.android.server.pm.UserTypeDetails userTypeDetails = this.mUserTypes.get(userType);
        return userTypeDetails != null && userTypeDetails.isFull();
    }

    boolean isUserTypeSubtypeOfProfile(java.lang.String userType) {
        com.android.server.pm.UserTypeDetails userTypeDetails = this.mUserTypes.get(userType);
        return userTypeDetails != null && userTypeDetails.isProfile();
    }

    boolean isUserTypeSubtypeOfSystem(java.lang.String userType) {
        com.android.server.pm.UserTypeDetails userTypeDetails = this.mUserTypes.get(userType);
        return userTypeDetails != null && userTypeDetails.isSystem();
    }

    public android.content.pm.UserProperties getUserPropertiesCopy(int userId) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserProperties");
        android.content.pm.UserProperties origProperties = getUserPropertiesInternal(userId);
        if (origProperties != null) {
            boolean exposeAllFields = android.os.Binder.getCallingUid() == 1000;
            boolean hasManage = hasManageUsersPermission();
            boolean hasQuery = hasQueryUsersPermission();
            return new android.content.pm.UserProperties(origProperties, exposeAllFields, hasManage, hasQuery);
        }
        throw new java.lang.IllegalArgumentException("Cannot access properties for user " + userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.pm.UserProperties getUserPropertiesInternal(int userId) {
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData userData = getUserDataLU(userId);
            if (userData != null) {
                return userData.userProperties;
            }
            return null;
        }
    }

    public boolean hasBadge(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "hasBadge");
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetailsNoChecks(userId);
        return userTypeDetails != null && userTypeDetails.hasBadge();
    }

    public int getUserBadgeLabelResId(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserBadgeLabelResId");
        android.content.pm.UserInfo userInfo = getUserInfoNoChecks(userId);
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetails(userInfo);
        if (userInfo == null || userTypeDetails == null || !userTypeDetails.hasBadge()) {
            android.util.Slog.e(LOG_TAG, "Requested badge label for non-badged user " + userId);
            return 0;
        }
        int badgeIndex = userInfo.profileBadge;
        return userTypeDetails.getBadgeLabel(badgeIndex);
    }

    public int getUserBadgeColorResId(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserBadgeColorResId");
        android.content.pm.UserInfo userInfo = getUserInfoNoChecks(userId);
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetails(userInfo);
        if (userInfo == null || userTypeDetails == null || !userTypeDetails.hasBadge()) {
            android.util.Slog.e(LOG_TAG, "Requested badge dark color for non-badged user " + userId);
            return 0;
        }
        return userTypeDetails.getBadgeColor(userInfo.profileBadge);
    }

    public int getUserBadgeDarkColorResId(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserBadgeDarkColorResId");
        android.content.pm.UserInfo userInfo = getUserInfoNoChecks(userId);
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetails(userInfo);
        if (userInfo == null || userTypeDetails == null || !userTypeDetails.hasBadge()) {
            android.util.Slog.e(LOG_TAG, "Requested badge color for non-badged user " + userId);
            return 0;
        }
        return userTypeDetails.getDarkThemeBadgeColor(userInfo.profileBadge);
    }

    public int getUserIconBadgeResId(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserIconBadgeResId");
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetailsNoChecks(userId);
        if (userTypeDetails == null || !userTypeDetails.hasBadge()) {
            android.util.Slog.e(LOG_TAG, "Requested icon badge for non-badged user " + userId);
            return 0;
        }
        return userTypeDetails.getIconBadge();
    }

    public int getUserBadgeResId(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserBadgeResId");
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetailsNoChecks(userId);
        if (userTypeDetails == null || !userTypeDetails.hasBadge()) {
            android.util.Slog.e(LOG_TAG, "Requested badge for non-badged user " + userId);
            return 0;
        }
        return userTypeDetails.getBadgePlain();
    }

    public int getUserBadgeNoBackgroundResId(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserBadgeNoBackgroundResId");
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetailsNoChecks(userId);
        if (userTypeDetails == null || !userTypeDetails.hasBadge()) {
            android.util.Slog.e(LOG_TAG, "Requested badge (no background) for non-badged user " + userId);
            return 0;
        }
        return userTypeDetails.getBadgeNoBackground();
    }

    public int getUserStatusBarIconResId(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserStatusBarIconResId");
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetailsNoChecks(userId);
        if (userTypeDetails == null || !userTypeDetails.hasBadge()) {
            android.util.Slog.w(LOG_TAG, "Requested status bar icon for non-badged user " + userId);
            return 0;
        }
        return userTypeDetails.getStatusBarIcon();
    }

    public int getProfileLabelResId(int userId) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getProfileLabelResId");
        android.content.pm.UserInfo userInfo = getUserInfoNoChecks(userId);
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetails(userInfo);
        if (userInfo == null || userTypeDetails == null) {
            return 0;
        }
        int userIndex = userInfo.profileBadge;
        return userTypeDetails.getLabel(userIndex);
    }

    public int getProfileAccessibilityLabelResId(int userId) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getProfileAccessibilityLabelResId");
        android.content.pm.UserInfo userInfo = getUserInfoNoChecks(userId);
        com.android.server.pm.UserTypeDetails userTypeDetails = getUserTypeDetails(userInfo);
        if (userInfo == null || userTypeDetails == null) {
            return 0;
        }
        return userTypeDetails.getAccessibilityString();
    }

    public boolean isProfile(int userId) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(userId, "isProfile");
        return isProfileUnchecked(userId);
    }

    private boolean isProfileUnchecked(int userId) {
        boolean z;
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            z = userInfo != null && userInfo.isProfile();
        }
        return z;
    }

    public java.lang.String getProfileType(int userId) {
        checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getProfileType");
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            if (userInfo != null) {
                return userInfo.isProfile() ? userInfo.userType : "";
            }
            return null;
        }
    }

    public boolean isUserUnlockingOrUnlocked(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "isUserUnlockingOrUnlocked");
        return this.mLocalService.isUserUnlockingOrUnlocked(userId);
    }

    public boolean isUserUnlocked(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "isUserUnlocked");
        return this.mLocalService.isUserUnlocked(userId);
    }

    public boolean isUserRunning(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "isUserRunning");
        return this.mLocalService.isUserRunning(userId);
    }

    public boolean isUserForeground(int userId) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (callingUserId == userId || hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            return userId == getCurrentUserId();
        }
        throw new java.lang.SecurityException("Caller from user " + callingUserId + " needs MANAGE_USERS or INTERACT_ACROSS_USERS permission to check if another user (" + userId + ") is running in the foreground");
    }

    public boolean isUserVisible(int userId) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (callingUserId != userId && !hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            throw new java.lang.SecurityException("Caller from user " + callingUserId + " needs MANAGE_USERS or INTERACT_ACROSS_USERS permission to check if another user (" + userId + ") is visible");
        }
        return this.mUserVisibilityMediator.isUserVisible(userId);
    }

    android.util.Pair<java.lang.Integer, java.lang.Integer> getCurrentAndTargetUserIds() {
        android.app.ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            android.util.Slog.w(LOG_TAG, "getCurrentAndTargetUserId() called too early, ActivityManagerInternal is not set yet");
            return new android.util.Pair<>(-10000, -10000);
        }
        return activityManagerInternal.getCurrentAndTargetUserIds();
    }

    int getCurrentUserId() {
        android.app.ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            android.util.Slog.w(LOG_TAG, "getCurrentUserId() called too early, ActivityManagerInternal is not set yet");
            return -10000;
        }
        return activityManagerInternal.getCurrentUserId();
    }

    boolean isCurrentUserOrRunningProfileOfCurrentUser(int userId) {
        int currentUserId = getCurrentUserId();
        if (currentUserId == userId) {
            return true;
        }
        if (isProfileUnchecked(userId)) {
            int parentId = getProfileParentIdUnchecked(userId);
            if (parentId == currentUserId) {
                return isUserRunning(userId);
            }
            return false;
        }
        return false;
    }

    boolean isUserVisibleOnDisplay(int userId, int displayId) {
        return this.mUserVisibilityMediator.isUserVisible(userId, displayId);
    }

    public int[] getVisibleUsers() {
        if (!hasManageUsersOrPermission("android.permission.INTERACT_ACROSS_USERS")) {
            throw new java.lang.SecurityException("Caller needs MANAGE_USERS or INTERACT_ACROSS_USERS permission to get list of visible users");
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mUserVisibilityMediator.getVisibleUsers().toArray();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public int getMainDisplayIdAssignedToUser() {
        int userId = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        int displayId = this.mUserVisibilityMediator.getMainDisplayAssignedToUser(userId);
        return displayId;
    }

    public boolean isForegroundUserAdmin() {
        synchronized (this.mUsersLock) {
            int currentUserId = getCurrentUserId();
            boolean z = false;
            if (currentUserId == -10000) {
                return false;
            }
            android.content.pm.UserInfo userInfo = getUserInfoLU(currentUserId);
            if (userInfo != null && userInfo.isAdmin()) {
                z = true;
            }
            return z;
        }
    }

    public java.lang.String getUserName() {
        int callingUid = android.os.Binder.getCallingUid();
        if (!hasQueryOrCreateUsersPermission() && !hasPermissionGranted("android.permission.GET_ACCOUNTS_PRIVILEGED", callingUid)) {
            throw new java.lang.SecurityException("You need MANAGE_USERS, CREATE_USERS, QUERY_USERS, or GET_ACCOUNTS_PRIVILEGED permissions to: get user name");
        }
        int userId = android.os.UserHandle.getUserId(callingUid);
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = userWithName(getUserInfoLU(userId));
            if (userInfo != null && userInfo.name != null) {
                return userInfo.name;
            }
            return "";
        }
    }

    public long getUserStartRealtime() {
        int userId = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData user = getUserDataLU(userId);
            if (user == null) {
                return 0L;
            }
            return user.startRealtime;
        }
    }

    public long getUserUnlockRealtime() {
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData user = getUserDataLU(android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()));
            if (user == null) {
                return 0L;
            }
            return user.unlockRealtime;
        }
    }

    private void checkManageOrInteractPermissionIfCallerInOtherProfileGroup(int userId, java.lang.String name) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (callingUserId == userId || isSameProfileGroupNoChecks(callingUserId, userId) || hasManageUsersPermission() || hasPermissionGranted("android.permission.INTERACT_ACROSS_USERS", android.os.Binder.getCallingUid())) {
        } else {
            throw new java.lang.SecurityException("You need INTERACT_ACROSS_USERS or MANAGE_USERS permission to: check " + name);
        }
    }

    private void checkQueryOrInteractPermissionIfCallerInOtherProfileGroup(int userId, java.lang.String name) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (callingUserId == userId || isSameProfileGroupNoChecks(callingUserId, userId) || hasQueryUsersPermission() || hasPermissionGranted("android.permission.INTERACT_ACROSS_USERS", android.os.Binder.getCallingUid())) {
        } else {
            throw new java.lang.SecurityException("You need INTERACT_ACROSS_USERS, MANAGE_USERS, or QUERY_USERS permission to: check " + name);
        }
    }

    private void checkQueryOrCreateUsersPermissionIfCallerInOtherProfileGroup(int userId, java.lang.String name) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (callingUserId == userId || isSameProfileGroupNoChecks(callingUserId, userId)) {
            return;
        }
        checkQueryOrCreateUsersPermission(name);
    }

    public boolean isDemoUser(int userId) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (callingUserId != userId && !hasManageUsersPermission()) {
            throw new java.lang.SecurityException("You need MANAGE_USERS permission to query if u=" + userId + " is a demo user");
        }
        boolean z = false;
        if (android.os.SystemProperties.getBoolean("ro.boot.arc_demo_mode", false)) {
            return true;
        }
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            if (userInfo != null && userInfo.isDemo()) {
                z = true;
            }
        }
        return z;
    }

    public boolean isAdminUser(int userId) {
        boolean z;
        checkQueryOrCreateUsersPermissionIfCallerInOtherProfileGroup(userId, "isAdminUser");
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            z = userInfo != null && userInfo.isAdmin();
        }
        return z;
    }

    public boolean isPreCreated(int userId) {
        boolean z;
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "isPreCreated");
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            z = userInfo != null && userInfo.preCreated;
        }
        return z;
    }

    public int getUserSwitchability(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserSwitchability");
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("getUserSwitchability-" + userId);
        int flags = 0;
        t.traceBegin("TM.isInCall");
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.telecom.TelecomManager telecomManager = (android.telecom.TelecomManager) this.mContext.getSystemService(android.telecom.TelecomManager.class);
            if (com.android.internal.telephony.flags.Flags.enforceTelephonyFeatureMappingForPublicApis()) {
                if (this.mContext.getPackageManager().hasSystemFeature("android.software.telecom") && telecomManager != null && telecomManager.isInCall()) {
                    flags = 0 | 1;
                }
            } else if (telecomManager != null && telecomManager.isInCall()) {
                flags = 0 | 1;
            }
            android.os.Binder.restoreCallingIdentity(identity);
            t.traceEnd();
            t.traceBegin("hasUserRestriction-DISALLOW_USER_SWITCH");
            if (this.mLocalService.hasUserRestriction("no_user_switch", userId)) {
                flags |= 2;
            }
            t.traceEnd();
            if (!isHeadlessSystemUserMode()) {
                t.traceBegin("getInt-ALLOW_USER_SWITCHING_WHEN_SYSTEM_USER_LOCKED");
                boolean allowUserSwitchingWhenSystemUserLocked = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "allow_user_switching_when_system_user_locked", 0) != 0;
                t.traceEnd();
                t.traceBegin("isUserUnlocked-USER_SYSTEM");
                boolean systemUserUnlocked = this.mLocalService.isUserUnlocked(0);
                t.traceEnd();
                if (!allowUserSwitchingWhenSystemUserLocked && !systemUserUnlocked) {
                    flags |= 4;
                }
            }
            t.traceEnd();
            return flags;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    boolean isUserSwitcherEnabled(int mUserId) {
        boolean multiUserSettingOn = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "user_switcher_enabled", android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_ringtoneVibrationSettingsSupported) ? 1 : 0) != 0;
        return android.os.UserManager.supportsMultipleUsers() && !hasUserRestriction("no_user_switch", mUserId) && !android.os.UserManager.isDeviceInDemoMode(this.mContext) && multiUserSettingOn;
    }

    public boolean isUserSwitcherEnabled(boolean showEvenIfNotActionable, int mUserId) {
        if (isUserSwitcherEnabled(mUserId)) {
            return showEvenIfNotActionable || !hasUserRestriction("no_add_user", mUserId) || areThereMultipleSwitchableUsers();
        }
        return false;
    }

    private boolean areThereMultipleSwitchableUsers() {
        java.util.List<android.content.pm.UserInfo> aliveUsers = getUsers(true, true, true);
        boolean isAnyAliveUser = false;
        for (android.content.pm.UserInfo userInfo : aliveUsers) {
            if (userInfo.supportsSwitchToByUser()) {
                if (isAnyAliveUser) {
                    return true;
                }
                isAnyAliveUser = true;
            }
        }
        return false;
    }

    public boolean isRestricted(int userId) {
        boolean zIsRestricted;
        if (userId != android.os.UserHandle.getCallingUserId()) {
            checkQueryOrCreateUsersPermission("query isRestricted for user " + userId);
        }
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            zIsRestricted = userInfo == null ? false : userInfo.isRestricted();
        }
        return zIsRestricted;
    }

    public boolean canHaveRestrictedProfile(int userId) {
        checkManageUsersPermission("canHaveRestrictedProfile");
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            boolean z = false;
            if (userInfo != null && userInfo.canHaveProfile()) {
                if (!userInfo.isAdmin()) {
                    return false;
                }
                if (!this.mIsDeviceManaged && !this.mIsUserManaged.get(userId)) {
                    z = true;
                }
                return z;
            }
            return false;
        }
    }

    public boolean canAddPrivateProfile(int userId) {
        checkCreateUsersPermission("canHaveRestrictedProfile");
        android.content.pm.UserInfo parentUserInfo = getUserInfo(userId);
        return isUserTypeEnabled("android.os.usertype.profile.PRIVATE") && canAddMoreProfilesToUser("android.os.usertype.profile.PRIVATE", userId, false) && parentUserInfo != null && parentUserInfo.isMain() && doesDeviceHardwareSupportPrivateSpace() && !hasUserRestriction("no_add_private_profile", userId);
    }

    public boolean hasRestrictedProfiles(int userId) {
        checkManageUsersPermission("hasRestrictedProfiles");
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo profile = this.mUsers.valueAt(i).info;
                if (userId != profile.id && profile.restrictedProfileParentId == userId) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.pm.UserInfo getUserInfoLU(int userId) {
        com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
        if (userData != null && userData.info.partial && !this.mRemovingUserIds.get(userId)) {
            android.util.Slog.w(LOG_TAG, "getUserInfo: unknown user #" + userId);
            return null;
        }
        if (userData != null) {
            return userData.info;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.pm.UserManagerService.UserData getUserDataLU(int userId) {
        com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
        if (userData != null && userData.info.partial && !this.mRemovingUserIds.get(userId)) {
            return null;
        }
        return userData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.pm.UserInfo getUserInfoNoChecks(int userId) {
        android.content.pm.UserInfo userInfo;
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
            userInfo = userData != null ? userData.info : null;
        }
        return userInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.pm.UserManagerService.UserData getUserDataNoChecks(int userId) {
        com.android.server.pm.UserManagerService.UserData userData;
        synchronized (this.mUsersLock) {
            userData = this.mUsers.get(userId);
        }
        return userData;
    }

    public boolean exists(int userId) {
        return this.mLocalService.exists(userId);
    }

    private int getCrossProfileIntentFilterAccessControl(int userId) {
        android.content.pm.UserProperties userProperties = getUserPropertiesInternal(userId);
        if (userProperties != null) {
            return userProperties.getCrossProfileIntentFilterAccessControl();
        }
        return 0;
    }

    public void enforceCrossProfileIntentFilterAccess(int sourceUserId, int targetUserId, int callingUid, boolean addCrossProfileIntentFilter) {
        if (!isCrossProfileIntentFilterAccessible(sourceUserId, targetUserId, addCrossProfileIntentFilter)) {
            throw new java.lang.SecurityException("CrossProfileIntentFilter cannot be accessed by user " + callingUid);
        }
    }

    public boolean isCrossProfileIntentFilterAccessible(int sourceUserId, int targetUserId, boolean addCrossProfileIntentFilter) {
        int effectiveAccessControl = getCrossProfileIntentFilterAccessControl(sourceUserId, targetUserId);
        if (10 == effectiveAccessControl && !com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot()) {
            return false;
        }
        if (20 == effectiveAccessControl) {
            return addCrossProfileIntentFilter && com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot();
        }
        return true;
    }

    public int getCrossProfileIntentFilterAccessControl(int sourceUserId, int targetUserId) {
        int sourceAccessControlLevel = getCrossProfileIntentFilterAccessControl(sourceUserId);
        int targetAccessControlLevel = getCrossProfileIntentFilterAccessControl(targetUserId);
        int effectiveAccessControl = java.lang.Math.max(sourceAccessControlLevel, targetAccessControlLevel);
        return effectiveAccessControl;
    }

    public void setUserName(int userId, java.lang.String name) {
        checkManageUsersPermission("rename users");
        synchronized (this.mPackagesLock) {
            com.android.server.pm.UserManagerService.UserData userData = getUserDataNoChecks(userId);
            if (userData != null && !userData.info.partial) {
                if (java.util.Objects.equals(name, userData.info.name)) {
                    com.android.server.utils.Slogf.i(LOG_TAG, "setUserName: ignoring for user #%d as it didn't change (%s)", java.lang.Integer.valueOf(userId), getRedacted(name));
                    return;
                }
                if (name == null) {
                    com.android.server.utils.Slogf.i(LOG_TAG, "setUserName: resetting name of user #%d", java.lang.Integer.valueOf(userId));
                } else {
                    com.android.server.utils.Slogf.i(LOG_TAG, "setUserName: setting name of user #%d to %s", java.lang.Integer.valueOf(userId), getRedacted(name));
                }
                userData.info.name = name;
                writeUserLP(userData);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    sendUserInfoChangedBroadcast(userId);
                    return;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
            com.android.server.utils.Slogf.w(LOG_TAG, "setUserName: unknown user #%d", java.lang.Integer.valueOf(userId));
        }
    }

    public boolean setUserEphemeral(int userId, boolean enableEphemeral) {
        checkCreateUsersPermission("update ephemeral user flag");
        if (enableEphemeral) {
            return android.os.UserManager.isRemoveResultSuccessful(setUserEphemeralUnchecked(userId));
        }
        return setUserNonEphemeralUnchecked(userId);
    }

    private boolean setUserNonEphemeralUnchecked(int userId) {
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
                if (userData == null) {
                    android.util.Slog.e(LOG_TAG, android.text.TextUtils.formatSimple("Cannot set user %d non-ephemeral, invalid user id provided.", new java.lang.Object[]{java.lang.Integer.valueOf(userId)}));
                    return false;
                }
                if (!userData.info.isEphemeral()) {
                    return true;
                }
                if ((userData.info.flags & 8192) != 0) {
                    android.util.Slog.e(LOG_TAG, android.text.TextUtils.formatSimple("User %d can not be changed to non-ephemeral because it was set ephemeral on create.", new java.lang.Object[]{java.lang.Integer.valueOf(userId)}));
                    return false;
                }
                userData.info.flags &= -257;
                writeUserLP(userData);
                return true;
            }
        }
    }

    private int setUserEphemeralUnchecked(int userId) {
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                int userRemovability = getUserRemovabilityLocked(userId, "set as ephemeral");
                if (userRemovability != 3) {
                    return userRemovability;
                }
                com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
                userData.info.flags |= 256;
                writeUserLP(userData);
                android.util.Slog.i(LOG_TAG, android.text.TextUtils.formatSimple("User %d is set ephemeral and will be removed on user switch or reboot.", new java.lang.Object[]{java.lang.Integer.valueOf(userId)}));
                return 1;
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    public void setUserIcon(int userId, android.graphics.Bitmap bitmap) throws android.os.ServiceSpecificException {
        try {
            checkManageUsersPermission("update users");
            enforceUserRestriction("no_set_user_icon", userId, "Cannot set user icon");
            this.mLocalService.setUserIcon(userId, bitmap);
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUserInfoChangedBroadcast(int userId) {
        android.content.Intent changedIntent = new android.content.Intent("android.intent.action.USER_INFO_CHANGED");
        changedIntent.putExtra("android.intent.extra.user_handle", userId);
        changedIntent.addFlags(1073741824);
        this.mContext.sendBroadcastAsUser(changedIntent, android.os.UserHandle.ALL);
    }

    public android.os.ParcelFileDescriptor getUserIcon(int targetUserId) {
        if (!hasManageUsersOrPermission("android.permission.GET_ACCOUNTS_PRIVILEGED")) {
            throw new java.lang.SecurityException("You need MANAGE_USERS or GET_ACCOUNTS_PRIVILEGED permissions to: get user icon");
        }
        synchronized (this.mPackagesLock) {
            android.content.pm.UserInfo targetUserInfo = getUserInfoNoChecks(targetUserId);
            if (targetUserInfo != null && !targetUserInfo.partial) {
                int callingUserId = android.os.UserHandle.getCallingUserId();
                android.content.pm.UserInfo callingUserInfo = getUserInfoNoChecks(callingUserId);
                if (!isSameUserOrProfileGroupOrTargetIsCommunal(callingUserInfo, targetUserInfo)) {
                    checkManageUsersPermission("get the icon of a user who is not related");
                }
                if (targetUserInfo.iconPath == null) {
                    return null;
                }
                java.lang.String iconPath = targetUserInfo.iconPath;
                try {
                    return android.os.ParcelFileDescriptor.open(new java.io.File(iconPath), 268435456);
                } catch (java.io.FileNotFoundException e) {
                    android.util.Slog.e(LOG_TAG, "Couldn't find icon file", e);
                    return null;
                }
            }
            android.util.Slog.w(LOG_TAG, "getUserIcon: unknown user #" + targetUserId);
            return null;
        }
    }

    public void makeInitialized(int userId) {
        checkManageUsersPermission("makeInitialized");
        boolean scheduleWriteUser = false;
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
            if (userData != null && !userData.info.partial) {
                if ((userData.info.flags & 16) == 0) {
                    userData.info.flags |= 16;
                    scheduleWriteUser = true;
                }
                if (scheduleWriteUser) {
                    scheduleWriteUser(userId);
                    return;
                }
                return;
            }
            android.util.Slog.w(LOG_TAG, "makeInitialized: unknown user #" + userId);
        }
    }

    private void initDefaultGuestRestrictions() {
        synchronized (this.mGuestRestrictions) {
            if (this.mGuestRestrictions.isEmpty()) {
                com.android.server.pm.UserTypeDetails guestType = this.mUserTypes.get("android.os.usertype.full.GUEST");
                if (guestType == null) {
                    android.util.Slog.wtf(LOG_TAG, "Can't set default guest restrictions: type doesn't exist.");
                    return;
                }
                guestType.addDefaultRestrictionsTo(this.mGuestRestrictions);
            }
        }
    }

    public android.os.Bundle getDefaultGuestRestrictions() {
        android.os.Bundle bundle;
        checkManageUsersPermission("getDefaultGuestRestrictions");
        synchronized (this.mGuestRestrictions) {
            bundle = new android.os.Bundle(this.mGuestRestrictions);
        }
        return bundle;
    }

    public void setDefaultGuestRestrictions(android.os.Bundle restrictions) {
        checkManageUsersPermission("setDefaultGuestRestrictions");
        java.util.List<android.content.pm.UserInfo> guests = getGuestUsers();
        synchronized (this.mRestrictionsLock) {
            for (int i = 0; i < guests.size(); i++) {
                updateUserRestrictionsInternalLR(restrictions, guests.get(i).id);
            }
        }
        synchronized (this.mGuestRestrictions) {
            this.mGuestRestrictions.clear();
            this.mGuestRestrictions.putAll(restrictions);
        }
        synchronized (this.mPackagesLock) {
            writeUserListLP();
        }
    }

    void setUserRestrictionInner(int userId, java.lang.String key, boolean value) {
        if (!com.android.server.pm.UserRestrictionsUtils.isValidRestriction(key)) {
            android.util.Slog.e(LOG_TAG, "Setting invalid restriction " + key);
            return;
        }
        synchronized (this.mRestrictionsLock) {
            android.os.Bundle newRestrictions = com.android.server.BundleUtils.clone(this.mDevicePolicyUserRestrictions.getRestrictions(userId));
            newRestrictions.putBoolean(key, value);
            if (this.mDevicePolicyUserRestrictions.updateRestrictions(userId, newRestrictions)) {
                if (userId == -1) {
                    applyUserRestrictionsForAllUsersLR();
                } else {
                    applyUserRestrictionsLR(userId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDevicePolicyUserRestrictionsInner(int originatingUserId, android.os.Bundle global, com.android.server.pm.RestrictionsSet local, boolean isDeviceOwner) {
        synchronized (this.mRestrictionsLock) {
            android.util.IntArray updatedUserIds = this.mDevicePolicyUserRestrictions.getUserIds();
            this.mCachedEffectiveUserRestrictions.removeAllRestrictions();
            this.mDevicePolicyUserRestrictions.removeAllRestrictions();
            this.mDevicePolicyUserRestrictions.updateRestrictions(-1, global);
            android.util.IntArray localUserIds = local.getUserIds();
            for (int i = 0; i < localUserIds.size(); i++) {
                int userId = localUserIds.get(i);
                this.mDevicePolicyUserRestrictions.updateRestrictions(userId, local.getRestrictions(userId));
                updatedUserIds.add(userId);
            }
            applyUserRestrictionsForAllUsersLR();
            for (int i2 = 0; i2 < updatedUserIds.size(); i2++) {
                if (updatedUserIds.get(i2) != -1) {
                    applyUserRestrictionsLR(updatedUserIds.get(i2));
                }
            }
        }
    }

    private android.os.Bundle computeEffectiveUserRestrictionsLR(int userId) {
        android.os.Bundle baseRestrictions = this.mBaseUserRestrictions.getRestrictionsNonNull(userId);
        android.os.Bundle global = this.mDevicePolicyUserRestrictions.getRestrictionsNonNull(-1);
        android.os.Bundle local = this.mDevicePolicyUserRestrictions.getRestrictionsNonNull(userId);
        if (global.isEmpty() && local.isEmpty()) {
            return baseRestrictions;
        }
        android.os.Bundle effective = com.android.server.BundleUtils.clone(baseRestrictions);
        com.android.server.pm.UserRestrictionsUtils.merge(effective, global);
        com.android.server.pm.UserRestrictionsUtils.merge(effective, local);
        return effective;
    }

    private void invalidateEffectiveUserRestrictionsLR(int userId) {
        this.mCachedEffectiveUserRestrictions.remove(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.Bundle getEffectiveUserRestrictions(int userId) {
        android.os.Bundle restrictions;
        synchronized (this.mRestrictionsLock) {
            restrictions = this.mCachedEffectiveUserRestrictions.getRestrictions(userId);
            if (restrictions == null) {
                restrictions = computeEffectiveUserRestrictionsLR(userId);
                this.mCachedEffectiveUserRestrictions.updateRestrictions(userId, restrictions);
            }
        }
        return restrictions;
    }

    public boolean hasUserRestriction(java.lang.String restrictionKey, int userId) {
        if (!userExists(userId)) {
            return false;
        }
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "hasUserRestriction");
        return this.mLocalService.hasUserRestriction(restrictionKey, userId);
    }

    public boolean hasUserRestrictionOnAnyUser(java.lang.String restrictionKey) {
        if (!com.android.server.pm.UserRestrictionsUtils.isValidRestriction(restrictionKey)) {
            return false;
        }
        java.util.List<android.content.pm.UserInfo> users = getUsers(true);
        for (int i = 0; i < users.size(); i++) {
            int userId = users.get(i).id;
            android.os.Bundle restrictions = getEffectiveUserRestrictions(userId);
            if (restrictions != null && restrictions.getBoolean(restrictionKey)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSettingRestrictedForUser(java.lang.String setting, int userId, java.lang.String value, int callingUid) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Non-system caller");
        }
        return com.android.server.pm.UserRestrictionsUtils.isSettingRestrictedForUser(this.mContext, setting, userId, value, callingUid);
    }

    public void addUserRestrictionsListener(final android.os.IUserRestrictionsListener listener) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Non-system caller");
        }
        this.mLocalService.addUserRestrictionsListener(new com.android.server.pm.UserManagerInternal.UserRestrictionsListener() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda9
            @Override // com.android.server.pm.UserManagerInternal.UserRestrictionsListener
            public final void onUserRestrictionsChanged(int i, android.os.Bundle bundle, android.os.Bundle bundle2) {
                com.android.server.pm.UserManagerService.lambda$addUserRestrictionsListener$2(listener, i, bundle, bundle2);
            }
        });
    }

    static /* synthetic */ void lambda$addUserRestrictionsListener$2(android.os.IUserRestrictionsListener listener, int userId, android.os.Bundle newRestrict, android.os.Bundle prevRestrict) {
        try {
            listener.onUserRestrictionsChanged(userId, newRestrict, prevRestrict);
        } catch (android.os.RemoteException re) {
            android.util.Slog.e("IUserRestrictionsListener", "Unable to invoke listener: " + re.getMessage());
        }
    }

    public int getUserRestrictionSource(java.lang.String restrictionKey, int userId) {
        java.util.List<android.os.UserManager.EnforcingUser> enforcingUsers = getUserRestrictionSources(restrictionKey, userId);
        int result = 0;
        for (int i = enforcingUsers.size() - 1; i >= 0; i--) {
            result |= enforcingUsers.get(i).getUserRestrictionSource();
        }
        return result;
    }

    public java.util.List<android.os.UserManager.EnforcingUser> getUserRestrictionSources(java.lang.String restrictionKey, int userId) {
        checkQueryUsersPermission("call getUserRestrictionSources.");
        if (!hasUserRestriction(restrictionKey, userId)) {
            return java.util.Collections.emptyList();
        }
        java.util.List<android.os.UserManager.EnforcingUser> result = new java.util.ArrayList<>();
        if (hasBaseUserRestriction(restrictionKey, userId)) {
            result.add(new android.os.UserManager.EnforcingUser(-10000, 1));
        }
        android.app.admin.DevicePolicyManagerInternal dpmi = getDevicePolicyManagerInternal();
        if (dpmi != null) {
            result.addAll(dpmi.getUserRestrictionSources(restrictionKey, userId));
        }
        return result;
    }

    public android.os.Bundle getUserRestrictions(int userId) {
        checkManageOrInteractPermissionIfCallerInOtherProfileGroup(userId, "getUserRestrictions");
        return com.android.server.BundleUtils.clone(getEffectiveUserRestrictions(userId));
    }

    public boolean hasBaseUserRestriction(java.lang.String restrictionKey, int userId) {
        checkCreateUsersPermission("hasBaseUserRestriction");
        boolean z = false;
        if (!com.android.server.pm.UserRestrictionsUtils.isValidRestriction(restrictionKey)) {
            return false;
        }
        synchronized (this.mRestrictionsLock) {
            android.os.Bundle bundle = this.mBaseUserRestrictions.getRestrictions(userId);
            if (bundle != null && bundle.getBoolean(restrictionKey, false)) {
                z = true;
            }
        }
        return z;
    }

    public void setUserRestriction(java.lang.String key, boolean value, int userId) {
        checkManageUsersPermission("setUserRestriction");
        if (!com.android.server.pm.UserRestrictionsUtils.isValidRestriction(key)) {
            return;
        }
        if (!userExists(userId)) {
            com.android.server.utils.Slogf.w(LOG_TAG, "Cannot set user restriction %s. User with id %d does not exist", key, java.lang.Integer.valueOf(userId));
            return;
        }
        synchronized (this.mRestrictionsLock) {
            android.os.Bundle newRestrictions = com.android.server.BundleUtils.clone(this.mBaseUserRestrictions.getRestrictions(userId));
            newRestrictions.putBoolean(key, value);
            updateUserRestrictionsInternalLR(newRestrictions, userId);
        }
    }

    private void updateUserRestrictionsInternalLR(android.os.Bundle newBaseRestrictions, final int userId) {
        android.os.Bundle prevAppliedRestrictions = com.android.server.pm.UserRestrictionsUtils.nonNull(this.mAppliedUserRestrictions.getRestrictions(userId));
        if (newBaseRestrictions != null) {
            android.os.Bundle prevBaseRestrictions = this.mBaseUserRestrictions.getRestrictions(userId);
            com.android.internal.util.Preconditions.checkState(prevBaseRestrictions != newBaseRestrictions);
            com.android.internal.util.Preconditions.checkState(this.mCachedEffectiveUserRestrictions.getRestrictions(userId) != newBaseRestrictions);
            if (this.mBaseUserRestrictions.updateRestrictions(userId, new android.os.Bundle(newBaseRestrictions))) {
                scheduleWriteUser(userId);
            }
        }
        final android.os.Bundle effective = computeEffectiveUserRestrictionsLR(userId);
        this.mCachedEffectiveUserRestrictions.updateRestrictions(userId, new android.os.Bundle(effective));
        if (this.mAppOpsService != null) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateUserRestrictionsInternalLR$3(effective, userId);
                }
            });
        }
        propagateUserRestrictionsLR(userId, effective, prevAppliedRestrictions);
        this.mAppliedUserRestrictions.updateRestrictions(userId, new android.os.Bundle(effective));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUserRestrictionsInternalLR$3(android.os.Bundle effective, int userId) {
        try {
            this.mAppOpsService.setUserRestrictions(effective, this.mUserRestrictionToken, userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(LOG_TAG, "Unable to notify AppOpsService of UserRestrictions");
        }
    }

    private void propagateUserRestrictionsLR(final int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
        if (com.android.server.pm.UserRestrictionsUtils.areEqual(newRestrictions, prevRestrictions)) {
            return;
        }
        final android.os.Bundle newRestrictionsFinal = new android.os.Bundle(newRestrictions);
        final android.os.Bundle prevRestrictionsFinal = new android.os.Bundle(prevRestrictions);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerService.4
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.pm.UserManagerInternal.UserRestrictionsListener[] listeners;
                com.android.server.pm.UserRestrictionsUtils.applyUserRestrictions(com.android.server.pm.UserManagerService.this.mContext, userId, newRestrictionsFinal, prevRestrictionsFinal);
                synchronized (com.android.server.pm.UserManagerService.this.mUserRestrictionsListeners) {
                    listeners = new com.android.server.pm.UserManagerInternal.UserRestrictionsListener[com.android.server.pm.UserManagerService.this.mUserRestrictionsListeners.size()];
                    com.android.server.pm.UserManagerService.this.mUserRestrictionsListeners.toArray(listeners);
                }
                for (com.android.server.pm.UserManagerInternal.UserRestrictionsListener userRestrictionsListener : listeners) {
                    userRestrictionsListener.onUserRestrictionsChanged(userId, newRestrictionsFinal, prevRestrictionsFinal);
                }
                android.content.Intent broadcast = new android.content.Intent("android.os.action.USER_RESTRICTIONS_CHANGED").setFlags(1073741824);
                android.os.Bundle options = android.app.BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).toBundle();
                com.android.server.pm.UserManagerService.this.mContext.sendBroadcastAsUser(broadcast, android.os.UserHandle.of(userId), null, options);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyUserRestrictionsLR(int userId) {
        updateUserRestrictionsInternalLR(null, userId);
        scheduleWriteUser(userId);
    }

    private void applyUserRestrictionsForAllUsersLR() {
        this.mCachedEffectiveUserRestrictions.removeAllRestrictions();
        java.lang.Runnable r = new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerService.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    int[] runningUsers = android.app.ActivityManager.getService().getRunningUserIds();
                    synchronized (com.android.server.pm.UserManagerService.this.mRestrictionsLock) {
                        for (int i : runningUsers) {
                            com.android.server.pm.UserManagerService.this.applyUserRestrictionsLR(i);
                        }
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, "Unable to access ActivityManagerService");
                }
            }
        };
        this.mHandler.post(r);
    }

    private boolean isUserLimitReached() {
        int count;
        synchronized (this.mUsersLock) {
            count = getAliveUsersExcludingGuestsCountLU();
        }
        return count >= android.os.UserManager.getMaxSupportedUsers() && !isCreationOverrideEnabled();
    }

    private boolean canAddMoreUsersOfType(com.android.server.pm.UserTypeDetails userTypeDetails) {
        if (!isUserTypeEnabled(userTypeDetails)) {
            return false;
        }
        int max = userTypeDetails.getMaxAllowed();
        if (max == -1) {
            return true;
        }
        return getNumberOfUsersOfType(userTypeDetails.getName()) < max || isCreationOverrideEnabled();
    }

    public int getRemainingCreatableUserCount(java.lang.String userType) {
        int result;
        boolean z;
        checkQueryOrCreateUsersPermission("get the remaining number of users that can be added.");
        com.android.server.pm.UserTypeDetails type = this.mUserTypes.get(userType);
        if (type == null || !isUserTypeEnabled(type)) {
            return 0;
        }
        synchronized (this.mUsersLock) {
            int userCount = getAliveUsersExcludingGuestsCountLU();
            int maxAllowed = Integer.MAX_VALUE;
            if (android.os.UserManager.isUserTypeGuest(userType) || android.os.UserManager.isUserTypeDemo(userType)) {
                result = Integer.MAX_VALUE;
            } else {
                result = android.os.UserManager.getMaxSupportedUsers() - userCount;
            }
            if (type.isManagedProfile()) {
                if (!this.mContext.getPackageManager().hasSystemFeature("android.software.managed_users")) {
                    return 0;
                }
                boolean z2 = true;
                if (result > 0) {
                    z = false;
                } else {
                    z = true;
                }
                if (userCount != 1) {
                    z2 = false;
                }
                if (z2 & z) {
                    result = 1;
                }
            }
            if (result <= 0) {
                return 0;
            }
            if (type.getMaxAllowed() != -1) {
                maxAllowed = type.getMaxAllowed() - getNumberOfUsersOfType(userType);
            }
            return java.lang.Math.max(0, java.lang.Math.min(result, maxAllowed));
        }
    }

    private int getNumberOfUsersOfType(java.lang.String userType) {
        int count = 0;
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                android.content.pm.UserInfo user = this.mUsers.valueAt(i).info;
                if (user.userType.equals(userType) && !user.guestToRemove && !this.mRemovingUserIds.get(user.id) && !user.preCreated) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean canAddMoreUsersOfType(java.lang.String userType) {
        checkCreateUsersPermission("check if more users can be added.");
        com.android.server.pm.UserTypeDetails userTypeDetails = this.mUserTypes.get(userType);
        return userTypeDetails != null && canAddMoreUsersOfType(userTypeDetails);
    }

    public boolean isUserTypeEnabled(java.lang.String userType) {
        checkCreateUsersPermission("check if user type is enabled.");
        com.android.server.pm.UserTypeDetails userTypeDetails = this.mUserTypes.get(userType);
        return userTypeDetails != null && isUserTypeEnabled(userTypeDetails);
    }

    private boolean isUserTypeEnabled(com.android.server.pm.UserTypeDetails userTypeDetails) {
        return userTypeDetails.isEnabled() || isCreationOverrideEnabled();
    }

    private boolean isCreationOverrideEnabled() {
        return android.os.Build.isDebuggable() && android.os.SystemProperties.getBoolean("debug.user.creation_override", false);
    }

    public boolean canAddMoreManagedProfiles(int userId, boolean allowedToRemoveOne) {
        return canAddMoreProfilesToUser("android.os.usertype.profile.MANAGED", userId, allowedToRemoveOne);
    }

    public boolean canAddMoreProfilesToUser(java.lang.String userType, int userId, boolean allowedToRemoveOne) {
        return getRemainingCreatableProfileCount(userType, userId, allowedToRemoveOne) > 0 || isCreationOverrideEnabled();
    }

    public int getRemainingCreatableProfileCount(java.lang.String userType, int userId) {
        return getRemainingCreatableProfileCount(userType, userId, false);
    }

    private int getRemainingCreatableProfileCount(java.lang.String userType, int userId, boolean allowedToRemoveOne) {
        int profilesRemovedCount;
        checkQueryOrCreateUsersPermission("get the remaining number of profiles that can be added to the given user.");
        com.android.server.pm.UserTypeDetails type = this.mUserTypes.get(userType);
        if (type == null || !isUserTypeEnabled(type)) {
            return 0;
        }
        boolean isManagedProfile = type.isManagedProfile();
        if (isManagedProfile && !this.mContext.getPackageManager().hasSystemFeature("android.software.managed_users")) {
            return 0;
        }
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            if (userInfo != null && userInfo.canHaveProfile()) {
                int userTypeCount = getProfileIds(userId, userType, false, false).length;
                if (userTypeCount <= 0 || !allowedToRemoveOne) {
                    profilesRemovedCount = 0;
                } else {
                    profilesRemovedCount = 1;
                }
                int usersCountAfterRemoving = getAliveUsersExcludingGuestsCountLU() - profilesRemovedCount;
                int result = android.os.UserManager.getMaxSupportedUsers() - usersCountAfterRemoving;
                if (result <= 0 && isManagedProfile && usersCountAfterRemoving == 1) {
                    result = 1;
                }
                int maxUsersOfType = getMaxUsersOfTypePerParent(type);
                if (maxUsersOfType != -1) {
                    result = java.lang.Math.min(result, maxUsersOfType - (userTypeCount - profilesRemovedCount));
                }
                if (result <= 0) {
                    return 0;
                }
                if (type.getMaxAllowed() != -1) {
                    result = java.lang.Math.min(result, type.getMaxAllowed() - (getNumberOfUsersOfType(userType) - profilesRemovedCount));
                }
                return java.lang.Math.max(0, result);
            }
            return 0;
        }
    }

    private int getAliveUsersExcludingGuestsCountLU() {
        int aliveUserCount = 0;
        int totalUserCount = this.mUsers.size();
        for (int i = 0; i < totalUserCount; i++) {
            android.content.pm.UserInfo user = this.mUsers.valueAt(i).info;
            if (!this.mRemovingUserIds.get(user.id) && !user.isGuest() && !user.preCreated && !this.mWrapper.getExtImpl().isCustomUser(user.flags)) {
                aliveUserCount++;
            }
        }
        return aliveUserCount;
    }

    private static final void checkManageUserAndAcrossUsersFullPermission(java.lang.String message) {
        int uid = android.os.Binder.getCallingUid();
        if (uid == 1000 || uid == 0) {
            return;
        }
        if (hasPermissionGranted("android.permission.MANAGE_USERS", uid) && hasPermissionGranted("android.permission.INTERACT_ACROSS_USERS_FULL", uid)) {
        } else {
            throw new java.lang.SecurityException("You need MANAGE_USERS and INTERACT_ACROSS_USERS_FULL permission to: " + message);
        }
    }

    private static boolean hasPermissionGranted(java.lang.String permission, int uid) {
        return android.app.ActivityManager.checkComponentPermission(permission, uid, -1, true) == 0;
    }

    private static final void checkManageUsersPermission(java.lang.String message) {
        if (!hasManageUsersPermission()) {
            throw new java.lang.SecurityException("You need MANAGE_USERS permission to: " + message);
        }
    }

    private static final void checkCreateUsersPermission(java.lang.String message) {
        if (!hasCreateUsersPermission()) {
            throw new java.lang.SecurityException("You either need MANAGE_USERS or CREATE_USERS permission to: " + message);
        }
    }

    private static final void checkQueryUsersPermission(java.lang.String message) {
        if (!hasQueryUsersPermission()) {
            throw new java.lang.SecurityException("You either need MANAGE_USERS or QUERY_USERS permission to: " + message);
        }
    }

    private static final void checkQueryOrCreateUsersPermission(java.lang.String message) {
        if (!hasQueryOrCreateUsersPermission()) {
            throw new java.lang.SecurityException("You either need MANAGE_USERS, CREATE_USERS, or QUERY_USERS permission to: " + message);
        }
    }

    private static final void checkCreateUsersPermission(int creationFlags) {
        if (((-38701) & creationFlags) == 0) {
            if (!hasCreateUsersPermission()) {
                throw new java.lang.SecurityException("You either need MANAGE_USERS or CREATE_USERS permission to create an user with flags: " + creationFlags);
            }
        } else if (!hasManageUsersPermission()) {
            throw new java.lang.SecurityException("You need MANAGE_USERS permission to create an user  with flags: " + creationFlags);
        }
    }

    private static final boolean hasManageUsersPermission() {
        int callingUid = android.os.Binder.getCallingUid();
        return hasManageUsersPermission(callingUid);
    }

    private static boolean hasManageUsersPermission(int callingUid) {
        return android.os.UserHandle.isSameApp(callingUid, 1000) || callingUid == 0 || hasPermissionGranted("android.permission.MANAGE_USERS", callingUid);
    }

    private static final boolean hasManageUsersOrPermission(java.lang.String alternativePermission) {
        int callingUid = android.os.Binder.getCallingUid();
        return hasManageUsersPermission(callingUid) || hasPermissionGranted(alternativePermission, callingUid);
    }

    private static final boolean hasCreateUsersPermission() {
        return hasManageUsersOrPermission("android.permission.CREATE_USERS");
    }

    private static final boolean hasQueryUsersPermission() {
        return hasManageUsersOrPermission("android.permission.QUERY_USERS");
    }

    private static final boolean hasQueryOrCreateUsersPermission() {
        return hasCreateUsersPermission() || hasPermissionGranted("android.permission.QUERY_USERS", android.os.Binder.getCallingUid());
    }

    private static void checkSystemOrRoot(java.lang.String message) {
        int uid = android.os.Binder.getCallingUid();
        if (!android.os.UserHandle.isSameApp(uid, 1000) && uid != 0) {
            throw new java.lang.SecurityException("Only system may: " + message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeBitmapLP(android.content.pm.UserInfo info, android.graphics.Bitmap bitmap) {
        try {
            java.io.File dir = new java.io.File(this.mUsersDir, java.lang.Integer.toString(info.id));
            java.io.File file = new java.io.File(dir, USER_PHOTO_FILENAME);
            java.io.File tmp = new java.io.File(dir, USER_PHOTO_FILENAME_TMP);
            if (!dir.exists()) {
                dir.mkdir();
                android.os.FileUtils.setPermissions(dir.getPath(), 505, -1, -1);
            }
            android.graphics.Bitmap.CompressFormat compressFormat = android.graphics.Bitmap.CompressFormat.PNG;
            java.io.FileOutputStream os = new java.io.FileOutputStream(tmp);
            if (bitmap.compress(compressFormat, 100, os) && tmp.renameTo(file) && android.os.SELinux.restorecon(file)) {
                info.iconPath = file.getAbsolutePath();
            }
            try {
                os.close();
            } catch (java.io.IOException e) {
            }
            tmp.delete();
        } catch (java.io.FileNotFoundException e2) {
            android.util.Slog.w(LOG_TAG, "Error setting photo for user ", e2);
        }
    }

    public int[] getUserIds() {
        int[] iArr;
        synchronized (this.mUsersLock) {
            iArr = this.mUserIds;
        }
        return iArr;
    }

    boolean userExists(int id) {
        synchronized (this.mUsersLock) {
            for (int userId : this.mUserIds) {
                if (userId == id) {
                    return true;
                }
            }
            return false;
        }
    }

    public int[] getUserIdsIncludingPreCreated() {
        int[] iArr;
        synchronized (this.mUsersLock) {
            iArr = this.mUserIdsIncludingPreCreated;
        }
        return iArr;
    }

    public boolean isHeadlessSystemUserMode() {
        boolean z;
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData systemUserData = this.mUsers.get(0);
            z = systemUserData.info.isFull() ? false : true;
        }
        return z;
    }

    private boolean isDefaultHeadlessSystemUserMode() {
        if (!android.os.Build.isDebuggable()) {
            return com.android.internal.os.RoSystemProperties.MULTIUSER_HEADLESS_SYSTEM_USER;
        }
        java.lang.String emulatedValue = android.os.SystemProperties.get("persist.debug.user_mode_emulation");
        if (!android.text.TextUtils.isEmpty(emulatedValue)) {
            if ("headless".equals(emulatedValue)) {
                return true;
            }
            if ("full".equals(emulatedValue)) {
                return false;
            }
            if (!"default".equals(emulatedValue)) {
                com.android.server.utils.Slogf.e(LOG_TAG, "isDefaultHeadlessSystemUserMode(): ignoring invalid valued of property %s: %s", "persist.debug.user_mode_emulation", emulatedValue);
            }
        }
        return com.android.internal.os.RoSystemProperties.MULTIUSER_HEADLESS_SYSTEM_USER;
    }

    private void emulateSystemUserModeIfNeeded() {
        java.lang.String newUserType;
        int newSysFlags;
        android.content.pm.UserInfo newMainUser;
        if (!android.os.Build.isDebuggable() || android.text.TextUtils.isEmpty(android.os.SystemProperties.get("persist.debug.user_mode_emulation"))) {
            return;
        }
        boolean newHeadlessSystemUserMode = isDefaultHeadlessSystemUserMode();
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                boolean mainIsAlreadyNonSystem = false;
                com.android.server.pm.UserManagerService.UserData systemUserData = this.mUsers.get(0);
                if (systemUserData == null) {
                    com.android.server.utils.Slogf.wtf(LOG_TAG, "emulateSystemUserModeIfNeeded(): no system user data");
                    return;
                }
                int oldMainUserId = getMainUserIdUnchecked();
                int oldSysFlags = systemUserData.info.flags;
                if (newHeadlessSystemUserMode) {
                    newUserType = "android.os.usertype.system.HEADLESS";
                    newSysFlags = oldSysFlags & (-1025) & (-16385);
                } else {
                    newUserType = "android.os.usertype.full.SYSTEM";
                    newSysFlags = oldSysFlags | 1024 | 16384;
                }
                if (systemUserData.info.userType.equals(newUserType)) {
                    com.android.server.utils.Slogf.d(LOG_TAG, "emulateSystemUserModeIfNeeded(): system user type is already %s, returning", newUserType);
                    return;
                }
                com.android.server.utils.Slogf.i(LOG_TAG, "Persisting emulated system user data: type changed from %s to %s, flags changed from %s to %s", systemUserData.info.userType, newUserType, android.content.pm.UserInfo.flagsToString(oldSysFlags), android.content.pm.UserInfo.flagsToString(newSysFlags));
                systemUserData.info.userType = newUserType;
                systemUserData.info.flags = newSysFlags;
                writeUserLP(systemUserData);
                com.android.server.pm.UserManagerService.UserData oldMain = getUserDataNoChecks(oldMainUserId);
                if (newHeadlessSystemUserMode) {
                    if (oldMain != null && (oldMain.info.flags & 2048) == 0) {
                        mainIsAlreadyNonSystem = true;
                    }
                    if (!mainIsAlreadyNonSystem && isMainUserPermanentAdmin() && (newMainUser = getEarliestCreatedFullUser()) != null) {
                        com.android.server.utils.Slogf.i(LOG_TAG, "Designating user " + newMainUser.id + " to be Main");
                        newMainUser.flags |= 16384;
                        writeUserLP(getUserDataNoChecks(newMainUser.id));
                    }
                } else if (oldMain != null && (oldMain.info.flags & 2048) == 0) {
                    com.android.server.utils.Slogf.i(LOG_TAG, "Transferring Main to user 0 from " + oldMain.info.id);
                    oldMain.info.flags &= -16385;
                    writeUserLP(oldMain);
                } else {
                    com.android.server.utils.Slogf.i(LOG_TAG, "Designated user 0 to be Main");
                }
                this.mUpdatingSystemUserMode = true;
            }
        }
    }

    private com.android.server.pm.ResilientAtomicFile getUserListFile() {
        java.io.File tempBackup = new java.io.File(this.mUserListFile.getParent(), this.mUserListFile.getName() + ".backup");
        java.io.File reserveCopy = new java.io.File(this.mUserListFile.getParent(), this.mUserListFile.getName() + ".reservecopy");
        return new com.android.server.pm.ResilientAtomicFile(this.mUserListFile, tempBackup, reserveCopy, 505, "user list", new com.android.server.pm.ResilientAtomicFile.ReadEventLogger() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda1
            @Override // com.android.server.pm.ResilientAtomicFile.ReadEventLogger
            public final void logEvent(int i, java.lang.String str) {
                this.f$0.lambda$getUserListFile$4(i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getUserListFile$4(int priority, java.lang.String msg) {
        android.util.Slog.e(LOG_TAG, msg);
        scheduleWriteUserList();
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x0116, code lost:
    
        if (r3.getName().equals(com.android.server.pm.UserManagerService.TAG_RESTRICTIONS) == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0118, code lost:
    
        r9 = r13.mGuestRestrictions;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x011a, code lost:
    
        monitor-enter(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x011b, code lost:
    
        com.android.server.pm.UserRestrictionsUtils.readRestrictions(r3, r13.mGuestRestrictions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0120, code lost:
    
        monitor-exit(r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readUserListLP() {
        /*
            Method dump skipped, instruction units count: 364
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserManagerService.readUserListLP():void");
    }

    private void upgradeIfNecessaryLP() {
        upgradeIfNecessaryLP(this.mUserVersion, this.mUserTypeVersion);
    }

    private void updateUsersWithFeatureFlags(boolean guestRestrictionsArePresentOnUserListXml) {
        if (guestRestrictionsArePresentOnUserListXml == android.multiuser.Flags.saveGlobalAndGuestRestrictionsOnSystemUserXmlReadOnly()) {
            for (int userId : getUserIds()) {
                writeUserLP(getUserDataNoChecks(userId));
            }
            writeUserListLP();
        }
    }

    void upgradeIfNecessaryLP(int userVersion, int userTypeVersion) {
        android.content.pm.UserInfo earliestCreatedUser;
        android.util.Slog.i(LOG_TAG, "Upgrading users from userVersion " + userVersion + " to 11");
        java.util.Set<java.lang.Integer> userIdsToWrite = new android.util.ArraySet<>();
        int originalVersion = this.mUserVersion;
        int originalUserTypeVersion = this.mUserTypeVersion;
        if (userVersion < 1) {
            com.android.server.pm.UserManagerService.UserData userData = getUserDataNoChecks(0);
            if ("Primary".equals(userData.info.name)) {
                userData.info.name = this.mContext.getResources().getString(android.R.string.notification_reply_button_accessibility);
                userIdsToWrite.add(java.lang.Integer.valueOf(userData.info.id));
            }
            userVersion = 1;
        }
        if (userVersion < 2) {
            com.android.server.pm.UserManagerService.UserData userData2 = getUserDataNoChecks(0);
            if ((userData2.info.flags & 16) == 0) {
                userData2.info.flags |= 16;
                userIdsToWrite.add(java.lang.Integer.valueOf(userData2.info.id));
            }
            userVersion = 2;
        }
        if (userVersion < 4) {
            userVersion = 4;
        }
        if (userVersion < 5) {
            initDefaultGuestRestrictions();
            userVersion = 5;
        }
        if (userVersion < 6) {
            synchronized (this.mUsersLock) {
                for (int i = 0; i < this.mUsers.size(); i++) {
                    com.android.server.pm.UserManagerService.UserData userData3 = this.mUsers.valueAt(i);
                    if (userData3.info.isRestricted() && userData3.info.restrictedProfileParentId == -10000) {
                        userData3.info.restrictedProfileParentId = 0;
                        userIdsToWrite.add(java.lang.Integer.valueOf(userData3.info.id));
                    }
                }
            }
            userVersion = 6;
        }
        if (userVersion < 7) {
            synchronized (this.mRestrictionsLock) {
                if (this.mDevicePolicyUserRestrictions.removeRestrictionsForAllUsers("ensure_verify_apps")) {
                    this.mDevicePolicyUserRestrictions.getRestrictionsNonNull(-1).putBoolean("ensure_verify_apps", true);
                }
            }
            java.util.List<android.content.pm.UserInfo> guestUsers = getGuestUsers();
            for (int i2 = 0; i2 < guestUsers.size(); i2++) {
                android.content.pm.UserInfo guestUser = guestUsers.get(i2);
                if (guestUser != null && !hasUserRestriction("no_config_wifi", guestUser.id)) {
                    setUserRestriction("no_config_wifi", true, guestUser.id);
                }
            }
            userVersion = 7;
        }
        this.mWrapper.getExtImpl().hookUsersUpgraded(this.mUsers);
        java.util.Set<java.lang.Integer> userIdsToWrite2 = this.mWrapper.getExtImpl().hookUsersIdToWrite(userIdsToWrite);
        if (userVersion < 8) {
            synchronized (this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData userData4 = this.mUsers.get(0);
                userData4.info.flags |= 2048;
                if (!isDefaultHeadlessSystemUserMode()) {
                    userData4.info.flags |= 1024;
                }
                userIdsToWrite2.add(java.lang.Integer.valueOf(userData4.info.id));
                for (int i3 = 1; i3 < this.mUsers.size(); i3++) {
                    com.android.server.pm.UserManagerService.UserData userData5 = this.mUsers.valueAt(i3);
                    if ((userData5.info.flags & 32) == 0) {
                        userData5.info.flags |= 1024;
                        userIdsToWrite2.add(java.lang.Integer.valueOf(userData5.info.id));
                    }
                }
            }
            userVersion = 8;
        }
        if (userVersion < 9) {
            synchronized (this.mUsersLock) {
                for (int i4 = 0; i4 < this.mUsers.size(); i4++) {
                    com.android.server.pm.UserManagerService.UserData userData6 = this.mUsers.valueAt(i4);
                    int flags = userData6.info.flags;
                    if ((flags & 2048) != 0) {
                        if ((flags & 1024) != 0) {
                            userData6.info.userType = "android.os.usertype.full.SYSTEM";
                        } else {
                            userData6.info.userType = "android.os.usertype.system.HEADLESS";
                        }
                    } else {
                        try {
                            userData6.info.userType = android.content.pm.UserInfo.getDefaultUserType(flags);
                        } catch (java.lang.IllegalArgumentException e) {
                            throw new java.lang.IllegalStateException("Cannot upgrade user with flags " + java.lang.Integer.toHexString(flags) + " because it doesn't correspond to a valid user type.", e);
                        }
                    }
                    com.android.server.pm.UserTypeDetails userTypeDetails = this.mUserTypes.get(userData6.info.userType);
                    if (userTypeDetails == null) {
                        throw new java.lang.IllegalStateException("Cannot upgrade user with flags " + java.lang.Integer.toHexString(flags) + " because " + userData6.info.userType + " isn't defined on this device!");
                    }
                    userData6.info.flags |= userTypeDetails.getDefaultUserInfoFlags();
                    userIdsToWrite2.add(java.lang.Integer.valueOf(userData6.info.id));
                }
            }
            userVersion = 9;
        }
        if (userVersion < 10) {
            synchronized (this.mUsersLock) {
                for (int i5 = 0; i5 < this.mUsers.size(); i5++) {
                    com.android.server.pm.UserManagerService.UserData userData7 = this.mUsers.valueAt(i5);
                    com.android.server.pm.UserTypeDetails userTypeDetails2 = this.mUserTypes.get(userData7.info.userType);
                    if (userTypeDetails2 == null) {
                        throw new java.lang.IllegalStateException("Cannot upgrade user because " + userData7.info.userType + " isn't defined on this device!");
                    }
                    userData7.userProperties = new android.content.pm.UserProperties(userTypeDetails2.getDefaultUserPropertiesReference());
                    userIdsToWrite2.add(java.lang.Integer.valueOf(userData7.info.id));
                }
            }
            userVersion = 10;
        }
        if (userVersion < 11) {
            if (isHeadlessSystemUserMode()) {
                if (isMainUserPermanentAdmin() && (earliestCreatedUser = getEarliestCreatedFullUser()) != null) {
                    earliestCreatedUser.flags |= 16384;
                    userIdsToWrite2.add(java.lang.Integer.valueOf(earliestCreatedUser.id));
                }
            } else {
                synchronized (this.mUsersLock) {
                    com.android.server.pm.UserManagerService.UserData userData8 = this.mUsers.get(0);
                    userData8.info.flags |= 16384;
                    userIdsToWrite2.add(java.lang.Integer.valueOf(userData8.info.id));
                }
            }
            userVersion = 11;
        }
        int newUserTypeVersion = com.android.server.pm.UserTypeFactory.getUserTypeVersion();
        if (newUserTypeVersion > userTypeVersion) {
            synchronized (this.mUsersLock) {
                upgradeUserTypesLU(com.android.server.pm.UserTypeFactory.getUserTypeUpgrades(), this.mUserTypes, userTypeVersion, userIdsToWrite2);
            }
        }
        if (userVersion < 11) {
            android.util.Slog.w(LOG_TAG, "User version " + this.mUserVersion + " didn't upgrade as expected to 11");
            return;
        }
        if (userVersion > 11) {
            android.util.Slog.wtf(LOG_TAG, "Upgraded user version " + this.mUserVersion + " is higher the SDK's one of 11. Someone forgot to update USER_VERSION?");
        }
        this.mUserVersion = userVersion;
        this.mUserTypeVersion = newUserTypeVersion;
        if (originalVersion < this.mUserVersion || originalUserTypeVersion < this.mUserTypeVersion) {
            java.util.Iterator<java.lang.Integer> it = userIdsToWrite2.iterator();
            while (it.hasNext()) {
                int userId = it.next().intValue();
                com.android.server.pm.UserManagerService.UserData userData9 = getUserDataNoChecks(userId);
                if (userData9 != null) {
                    writeUserLP(userData9);
                }
            }
            writeUserListLP();
        }
    }

    private void upgradeUserTypesLU(java.util.List<com.android.server.pm.UserTypeFactory.UserTypeUpgrade> upgradeOps, android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails> userTypes, int formerUserTypeVersion, java.util.Set<java.lang.Integer> userIdsToWrite) {
        for (com.android.server.pm.UserTypeFactory.UserTypeUpgrade userTypeUpgrade : upgradeOps) {
            if (formerUserTypeVersion <= userTypeUpgrade.getUpToVersion()) {
                for (int i = 0; i < this.mUsers.size(); i++) {
                    com.android.server.pm.UserManagerService.UserData userData = this.mUsers.valueAt(i);
                    if (userTypeUpgrade.getFromType().equals(userData.info.userType)) {
                        com.android.server.pm.UserTypeDetails newUserType = userTypes.get(userTypeUpgrade.getToType());
                        if (newUserType == null) {
                            throw new java.lang.IllegalStateException("Upgrade destination user type not defined: " + userTypeUpgrade.getToType());
                        }
                        upgradeProfileToTypeLU(userData.info, newUserType);
                        userIdsToWrite.add(java.lang.Integer.valueOf(userData.info.id));
                    }
                }
            }
        }
    }

    void upgradeProfileToTypeLU(android.content.pm.UserInfo userInfo, com.android.server.pm.UserTypeDetails newUserType) {
        int oldFlags;
        android.util.Slog.i(LOG_TAG, "Upgrading user " + userInfo.id + " from " + userInfo.userType + " to " + newUserType.getName());
        if (!userInfo.isProfile()) {
            throw new java.lang.IllegalStateException("Can only upgrade profile types. " + userInfo.userType + " is not a profile type.");
        }
        if (!canAddMoreProfilesToUser(newUserType.getName(), userInfo.profileGroupId, false)) {
            android.util.Slog.w(LOG_TAG, "Exceeded maximum profiles of type " + newUserType.getName() + " for user " + userInfo.id + ". Maximum allowed= " + newUserType.getMaxAllowedPerParent());
        }
        com.android.server.pm.UserTypeDetails oldUserType = this.mUserTypes.get(userInfo.userType);
        if (oldUserType != null) {
            oldFlags = oldUserType.getDefaultUserInfoFlags();
        } else {
            oldFlags = 4096;
        }
        userInfo.userType = newUserType.getName();
        userInfo.flags = newUserType.getDefaultUserInfoFlags() | (userInfo.flags ^ oldFlags);
        synchronized (this.mRestrictionsLock) {
            if (!com.android.server.BundleUtils.isEmpty(newUserType.getDefaultRestrictions())) {
                android.os.Bundle newRestrictions = com.android.server.BundleUtils.clone(this.mBaseUserRestrictions.getRestrictions(userInfo.id));
                com.android.server.pm.UserRestrictionsUtils.merge(newRestrictions, newUserType.getDefaultRestrictions());
                updateUserRestrictionsInternalLR(newRestrictions, userInfo.id);
            }
        }
        userInfo.profileBadge = getFreeProfileBadgeLU(userInfo.profileGroupId, userInfo.userType);
    }

    private android.content.pm.UserInfo getEarliestCreatedFullUser() {
        java.util.List<android.content.pm.UserInfo> users = getUsersInternal(true, true, true);
        android.content.pm.UserInfo earliestUser = null;
        long earliestCreationTime = Long.MAX_VALUE;
        for (int i = 0; i < users.size(); i++) {
            android.content.pm.UserInfo info = users.get(i);
            if (info.isFull() && info.isAdmin() && info.creationTime >= 0 && info.creationTime < earliestCreationTime) {
                earliestCreationTime = info.creationTime;
                earliestUser = info;
            }
        }
        return earliestUser;
    }

    private void fallbackToSingleUserLP() {
        java.lang.String systemUserType;
        if (isDefaultHeadlessSystemUserMode()) {
            systemUserType = "android.os.usertype.system.HEADLESS";
        } else {
            systemUserType = "android.os.usertype.full.SYSTEM";
        }
        int flags = this.mUserTypes.get(systemUserType).getDefaultUserInfoFlags() | 16;
        android.content.pm.UserInfo system2 = new android.content.pm.UserInfo(0, (java.lang.String) null, (java.lang.String) null, flags, systemUserType);
        com.android.server.pm.UserManagerService.UserData userData = putUserInfo(system2);
        userData.userProperties = new android.content.pm.UserProperties(this.mUserTypes.get(userData.info.userType).getDefaultUserPropertiesReference());
        this.mNextSerialNumber = 10;
        this.mUserVersion = 11;
        this.mUserTypeVersion = com.android.server.pm.UserTypeFactory.getUserTypeVersion();
        android.os.Bundle restrictions = new android.os.Bundle();
        try {
            java.lang.String[] defaultFirstUserRestrictions = this.mContext.getResources().getStringArray(android.R.array.config_convert_to_emergency_number_map);
            for (java.lang.String userRestriction : defaultFirstUserRestrictions) {
                if (com.android.server.pm.UserRestrictionsUtils.isValidRestriction(userRestriction)) {
                    restrictions.putBoolean(userRestriction, true);
                }
            }
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Slog.e(LOG_TAG, "Couldn't find resource: config_defaultFirstUserRestrictions", e);
        }
        if (!restrictions.isEmpty()) {
            synchronized (this.mRestrictionsLock) {
                this.mBaseUserRestrictions.updateRestrictions(0, restrictions);
            }
        }
        initDefaultGuestRestrictions();
        writeUserLP(userData);
        writeUserListLP();
    }

    private java.lang.String getOwnerName() {
        return this.mOwnerName.get();
    }

    private java.lang.String getGuestName() {
        return this.mContext.getString(android.R.string.granularity_label_word);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateOwnerNameIfNecessary(android.content.res.Resources res, boolean forceUpdate) {
        int configChanges = this.mLastConfiguration.updateFrom(res.getConfiguration());
        if (forceUpdate || (this.mOwnerNameTypedValue.changingConfigurations & configChanges) != 0) {
            res.getValue(android.R.string.notification_reply_button_accessibility, this.mOwnerNameTypedValue, true);
            java.lang.CharSequence ownerName = this.mOwnerNameTypedValue.coerceToString();
            this.mOwnerName.set(ownerName != null ? ownerName.toString() : null);
        }
    }

    private void scheduleWriteUserList() {
        if (!this.mHandler.hasMessages(2)) {
            android.os.Message msg = this.mHandler.obtainMessage(2);
            this.mHandler.sendMessageDelayed(msg, 2000L);
        }
    }

    private void scheduleWriteUser(int userId) {
        if (!this.mHandler.hasMessages(1, java.lang.Integer.valueOf(userId))) {
            android.os.Message msg = this.mHandler.obtainMessage(1, java.lang.Integer.valueOf(userId));
            this.mHandler.sendMessageDelayed(msg, 2000L);
        }
    }

    private com.android.server.pm.ResilientAtomicFile getUserFile(final int userId) {
        java.io.File file = new java.io.File(this.mUsersDir, userId + XML_SUFFIX);
        java.io.File tempBackup = new java.io.File(this.mUsersDir, userId + XML_SUFFIX + ".backup");
        java.io.File reserveCopy = new java.io.File(this.mUsersDir, userId + XML_SUFFIX + ".reservecopy");
        return new com.android.server.pm.ResilientAtomicFile(file, tempBackup, reserveCopy, 505, "user info", new com.android.server.pm.ResilientAtomicFile.ReadEventLogger() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda3
            @Override // com.android.server.pm.ResilientAtomicFile.ReadEventLogger
            public final void logEvent(int i, java.lang.String str) {
                this.f$0.lambda$getUserFile$5(userId, i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getUserFile$5(int userId, int priority, java.lang.String msg) {
        android.util.Slog.e(LOG_TAG, msg);
        com.android.server.pm.UserManagerService.UserData userData = getUserDataNoChecks(userId);
        if (userData != null) {
            scheduleWriteUser(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeUserLP(com.android.server.pm.UserManagerService.UserData userData) {
        com.android.server.pm.ResilientAtomicFile userFile = getUserFile(userData.info.id);
        java.io.FileOutputStream fos = null;
        try {
            try {
                fos = userFile.startWrite();
                writeUserLP(userData, fos);
                userFile.finishWrite(fos);
            } catch (java.lang.Exception ioe) {
                android.util.Slog.e(LOG_TAG, "Error writing user info " + userData.info.id, ioe);
                userFile.failWrite(fos);
            }
            if (userFile != null) {
                userFile.close();
            }
        } catch (java.lang.Throwable th) {
            if (userFile != null) {
                try {
                    userFile.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    void writeUserLP(com.android.server.pm.UserManagerService.UserData userData, java.io.OutputStream os) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(os);
        serializer.startDocument((java.lang.String) null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        android.content.pm.UserInfo userInfo = userData.info;
        serializer.startTag((java.lang.String) null, TAG_USER);
        serializer.attributeInt((java.lang.String) null, ATTR_ID, userInfo.id);
        serializer.attributeInt((java.lang.String) null, ATTR_SERIAL_NO, userInfo.serialNumber);
        serializer.attributeInt((java.lang.String) null, ATTR_FLAGS, userInfo.flags);
        serializer.attribute((java.lang.String) null, "type", userInfo.userType);
        serializer.attributeLong((java.lang.String) null, ATTR_CREATION_TIME, userInfo.creationTime);
        serializer.attributeLong((java.lang.String) null, ATTR_LAST_LOGGED_IN_TIME, userInfo.lastLoggedInTime);
        if (userInfo.lastLoggedInFingerprint != null) {
            serializer.attribute((java.lang.String) null, ATTR_LAST_LOGGED_IN_FINGERPRINT, userInfo.lastLoggedInFingerprint);
        }
        serializer.attributeLong((java.lang.String) null, ATTR_LAST_ENTERED_FOREGROUND_TIME, userData.mLastEnteredForegroundTimeMillis);
        if (userInfo.iconPath != null) {
            serializer.attribute((java.lang.String) null, ATTR_ICON_PATH, userInfo.iconPath);
        }
        if (userInfo.partial) {
            serializer.attributeBoolean((java.lang.String) null, ATTR_PARTIAL, true);
        }
        if (userInfo.preCreated) {
            serializer.attributeBoolean((java.lang.String) null, ATTR_PRE_CREATED, true);
        }
        if (userInfo.convertedFromPreCreated) {
            serializer.attributeBoolean((java.lang.String) null, ATTR_CONVERTED_FROM_PRE_CREATED, true);
        }
        if (userInfo.guestToRemove) {
            serializer.attributeBoolean((java.lang.String) null, ATTR_GUEST_TO_REMOVE, true);
        }
        if (userInfo.profileGroupId != -10000) {
            serializer.attributeInt((java.lang.String) null, ATTR_PROFILE_GROUP_ID, userInfo.profileGroupId);
        }
        serializer.attributeInt((java.lang.String) null, ATTR_PROFILE_BADGE, userInfo.profileBadge);
        if (userInfo.restrictedProfileParentId != -10000) {
            serializer.attributeInt((java.lang.String) null, ATTR_RESTRICTED_PROFILE_PARENT_ID, userInfo.restrictedProfileParentId);
        }
        if (userData.persistSeedData) {
            if (userData.seedAccountName != null) {
                serializer.attribute((java.lang.String) null, ATTR_SEED_ACCOUNT_NAME, truncateString(userData.seedAccountName, 500));
            }
            if (userData.seedAccountType != null) {
                serializer.attribute((java.lang.String) null, ATTR_SEED_ACCOUNT_TYPE, truncateString(userData.seedAccountType, 500));
            }
        }
        if (userInfo.name != null) {
            serializer.startTag((java.lang.String) null, "name");
            serializer.text(truncateString(userInfo.name, 100));
            serializer.endTag((java.lang.String) null, "name");
        }
        synchronized (this.mRestrictionsLock) {
            com.android.server.pm.UserRestrictionsUtils.writeRestrictions(serializer, this.mBaseUserRestrictions.getRestrictions(userInfo.id), TAG_RESTRICTIONS);
            if (android.multiuser.Flags.saveGlobalAndGuestRestrictionsOnSystemUserXmlReadOnly()) {
                if (userInfo.id == 0) {
                    com.android.server.pm.UserRestrictionsUtils.writeRestrictions(serializer, this.mDevicePolicyUserRestrictions.getRestrictions(-1), TAG_DEVICE_POLICY_GLOBAL_RESTRICTIONS);
                    serializer.startTag((java.lang.String) null, TAG_GUEST_RESTRICTIONS);
                    synchronized (this.mGuestRestrictions) {
                        com.android.server.pm.UserRestrictionsUtils.writeRestrictions(serializer, this.mGuestRestrictions, TAG_RESTRICTIONS);
                    }
                    serializer.endTag((java.lang.String) null, TAG_GUEST_RESTRICTIONS);
                }
            } else {
                com.android.server.pm.UserRestrictionsUtils.writeRestrictions(serializer, this.mDevicePolicyUserRestrictions.getRestrictions(-1), TAG_DEVICE_POLICY_GLOBAL_RESTRICTIONS);
            }
            com.android.server.pm.UserRestrictionsUtils.writeRestrictions(serializer, this.mDevicePolicyUserRestrictions.getRestrictions(userInfo.id), TAG_DEVICE_POLICY_LOCAL_RESTRICTIONS);
        }
        if (userData.account != null) {
            serializer.startTag((java.lang.String) null, TAG_ACCOUNT);
            serializer.text(userData.account);
            serializer.endTag((java.lang.String) null, TAG_ACCOUNT);
        }
        if (userData.persistSeedData && userData.seedAccountOptions != null) {
            serializer.startTag((java.lang.String) null, TAG_SEED_ACCOUNT_OPTIONS);
            userData.seedAccountOptions.saveToXml(serializer);
            serializer.endTag((java.lang.String) null, TAG_SEED_ACCOUNT_OPTIONS);
        }
        if (userData.userProperties != null) {
            serializer.startTag((java.lang.String) null, TAG_USER_PROPERTIES);
            userData.userProperties.writeToXml(serializer);
            serializer.endTag((java.lang.String) null, TAG_USER_PROPERTIES);
        }
        if (userData.getLastRequestQuietModeEnabledMillis() != 0) {
            serializer.startTag((java.lang.String) null, TAG_LAST_REQUEST_QUIET_MODE_ENABLED_CALL);
            serializer.text(java.lang.String.valueOf(userData.getLastRequestQuietModeEnabledMillis()));
            serializer.endTag((java.lang.String) null, TAG_LAST_REQUEST_QUIET_MODE_ENABLED_CALL);
        }
        serializer.startTag((java.lang.String) null, TAG_IGNORE_PREPARE_STORAGE_ERRORS);
        serializer.text(java.lang.String.valueOf(userData.getIgnorePrepareStorageErrors()));
        serializer.endTag((java.lang.String) null, TAG_IGNORE_PREPARE_STORAGE_ERRORS);
        serializer.endTag((java.lang.String) null, TAG_USER);
        serializer.endDocument();
    }

    private java.lang.String truncateString(java.lang.String original, int limit) {
        if (original == null || original.length() <= limit) {
            return original;
        }
        return original.substring(0, limit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeUserListLP() {
        int[] userIdsToWrite;
        com.android.server.pm.ResilientAtomicFile file = getUserListFile();
        try {
            try {
                java.io.FileOutputStream fos = file.startWrite();
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(fos);
                serializer.startDocument((java.lang.String) null, true);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag((java.lang.String) null, "users");
                serializer.attributeInt((java.lang.String) null, ATTR_NEXT_SERIAL_NO, this.mNextSerialNumber);
                serializer.attributeInt((java.lang.String) null, ATTR_USER_VERSION, this.mUserVersion);
                serializer.attributeInt((java.lang.String) null, ATTR_USER_TYPE_VERSION, this.mUserTypeVersion);
                if (!android.multiuser.Flags.saveGlobalAndGuestRestrictionsOnSystemUserXmlReadOnly()) {
                    serializer.startTag((java.lang.String) null, TAG_GUEST_RESTRICTIONS);
                    synchronized (this.mGuestRestrictions) {
                        com.android.server.pm.UserRestrictionsUtils.writeRestrictions(serializer, this.mGuestRestrictions, TAG_RESTRICTIONS);
                    }
                    serializer.endTag((java.lang.String) null, TAG_GUEST_RESTRICTIONS);
                }
                synchronized (this.mUsersLock) {
                    userIdsToWrite = new int[this.mUsers.size()];
                    for (int i = 0; i < userIdsToWrite.length; i++) {
                        android.content.pm.UserInfo user = this.mUsers.valueAt(i).info;
                        userIdsToWrite[i] = user.id;
                    }
                }
                for (int id : userIdsToWrite) {
                    serializer.startTag((java.lang.String) null, TAG_USER);
                    serializer.attributeInt((java.lang.String) null, ATTR_ID, id);
                    serializer.endTag((java.lang.String) null, TAG_USER);
                }
                serializer.endTag((java.lang.String) null, "users");
                serializer.endDocument();
                file.finishWrite(fos);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(LOG_TAG, "Error writing user list", e);
                file.failWrite(null);
            }
            if (file != null) {
                file.close();
            }
        } catch (java.lang.Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private com.android.server.pm.UserManagerService.UserData readUserLP(int id, int userVersion) {
        com.android.server.pm.ResilientAtomicFile file = getUserFile(id);
        try {
            try {
                java.io.FileInputStream fis = file.openRead();
                if (fis == null) {
                    android.util.Slog.e(LOG_TAG, "User info not found, returning null, user id: " + id);
                    if (file != null) {
                        file.close();
                        return null;
                    }
                    return null;
                }
                com.android.server.pm.UserManagerService.UserData userLP = readUserLP(id, fis, userVersion);
                if (file != null) {
                    file.close();
                }
                return userLP;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(LOG_TAG, "Error reading user info, user id: " + id);
                file.failRead(null, e);
                com.android.server.pm.UserManagerService.UserData userLP2 = readUserLP(id, userVersion);
                if (file != null) {
                    file.close();
                }
                return userLP2;
            }
        } catch (java.lang.Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:117:0x0317, code lost:
    
        r2 = r13;
        r8 = r18;
        r10 = r21;
        r1 = r23;
        r45 = r24;
        r46 = r25;
        r47 = r26;
        r48 = r27;
        r49 = r28;
        r50 = r29;
        r51 = r30;
        r52 = r31;
        r53 = r32;
        r13 = r6;
        r14 = r7;
        r6 = r34;
        r19 = r5;
        r4 = r11;
        r12 = r22;
        r11 = r39;
        r15 = r17;
        r9 = r20;
        r17 = r15;
        r20 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x021e, code lost:
    
        r40 = r40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x022b, code lost:
    
        if (r9.getName().equals(com.android.server.pm.UserManagerService.TAG_RESTRICTIONS) == false) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x022d, code lost:
    
        r3 = r59.mGuestRestrictions;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x022f, code lost:
    
        monitor-enter(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0230, code lost:
    
        com.android.server.pm.UserRestrictionsUtils.readRestrictions(r9, r59.mGuestRestrictions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0235, code lost:
    
        monitor-exit(r3);
     */
    /* JADX WARN: Removed duplicated region for block: B:161:0x048e  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0481 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.android.server.pm.UserManagerService.UserData readUserLP(int r60, java.io.InputStream r61, int r62) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1174
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserManagerService.readUserLP(int, java.io.InputStream, int):com.android.server.pm.UserManagerService$UserData");
    }

    private static boolean cleanAppRestrictionsForPackageLAr(java.lang.String pkg, int userId) {
        java.io.File dir = android.os.Environment.getUserSystemDirectory(userId);
        java.io.File resFile = new java.io.File(dir, packageToRestrictionsFileName(pkg));
        if (resFile.exists()) {
            resFile.delete();
            return true;
        }
        return false;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public android.content.pm.UserInfo createProfileForUserWithThrow(java.lang.String name, java.lang.String userType, int flags, int userId, java.lang.String[] disallowedPackages) throws android.os.ServiceSpecificException {
        checkCreateUsersPermission(flags);
        try {
            return createUserInternal(name, userType, flags, userId, disallowedPackages);
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public android.content.pm.UserInfo createProfileForUserEvenWhenDisallowedWithThrow(java.lang.String name, java.lang.String userType, int flags, int userId, java.lang.String[] disallowedPackages) throws android.os.ServiceSpecificException {
        checkCreateUsersPermission(flags);
        try {
            return createUserInternalUnchecked(name, userType, flags, userId, false, disallowedPackages, null);
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public android.content.pm.UserInfo createUserWithThrow(java.lang.String name, java.lang.String userType, int flags) throws android.os.ServiceSpecificException {
        checkCreateUsersPermission(flags);
        try {
            return createUserInternal(name, userType, flags, -10000, null);
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public android.content.pm.UserInfo preCreateUserWithThrow(java.lang.String userType) throws android.os.ServiceSpecificException {
        com.android.server.pm.UserTypeDetails userTypeDetails = this.mUserTypes.get(userType);
        int flags = userTypeDetails != null ? userTypeDetails.getDefaultUserInfoFlags() : 0;
        checkCreateUsersPermission(flags);
        com.android.internal.util.Preconditions.checkArgument(isUserTypeEligibleForPreCreation(userTypeDetails), "cannot pre-create user of type " + userType);
        android.util.Slog.i(LOG_TAG, "Pre-creating user of type " + userType);
        try {
            return createUserInternalUnchecked(null, userType, flags, -10000, true, null, null);
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    public android.os.UserHandle createUserWithAttributes(java.lang.String userName, java.lang.String userType, int flags, android.graphics.Bitmap userIcon, java.lang.String accountName, java.lang.String accountType, android.os.PersistableBundle accountOptions) throws android.os.ServiceSpecificException {
        checkCreateUsersPermission(flags);
        if (someUserHasAccountNoChecks(accountName, accountType)) {
            throw new android.os.ServiceSpecificException(7);
        }
        try {
            android.content.pm.UserInfo userInfo = createUserInternal(userName, userType, flags, -10000, null);
            if (userIcon != null) {
                this.mLocalService.setUserIcon(userInfo.id, userIcon);
            }
            setSeedAccountDataNoChecks(userInfo.id, accountName, accountType, accountOptions, true);
            return userInfo.getUserHandle();
        } catch (android.os.UserManager.CheckedUserOperationException e) {
            throw e.toServiceSpecificException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    private android.content.pm.UserInfo createUserInternal(java.lang.String name, java.lang.String userType, int flags, int parentId, java.lang.String[] disallowedPackages) throws android.os.UserManager.CheckedUserOperationException {
        java.lang.String restriction = "no_add_user";
        if (android.os.UserManager.isUserTypeCloneProfile(userType)) {
            restriction = "no_add_clone_profile";
        } else if (android.os.UserManager.isUserTypeManagedProfile(userType)) {
            restriction = "no_add_managed_profile";
        } else if (android.os.UserManager.isUserTypePrivateProfile(userType)) {
            restriction = "no_add_private_profile";
        }
        enforceUserRestriction(restriction, android.os.UserHandle.getCallingUserId(), "Cannot add user");
        return createUserInternalUnchecked(name, userType, flags, parentId, false, disallowedPackages, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.pm.UserInfo createUserInternalUnchecked(java.lang.String name, java.lang.String userType, int flags, int parentId, boolean preCreate, java.lang.String[] disallowedPackages, java.lang.Object token) throws android.os.UserManager.CheckedUserOperationException {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        this.mWrapper.getExtImpl().ormsCreateUserBoost(2000);
        t.traceBegin("createUser-" + flags);
        this.mUserJourneyLogger.logUserJourneyBegin(-1, 4);
        android.content.pm.UserInfo newUser = null;
        try {
            newUser = createUserInternalUncheckedNoTracing(name, userType, flags, parentId, preCreate, disallowedPackages, t, token);
            this.mWrapper.getExtImpl().onCreateUserInternal(newUser);
            return newUser;
        } finally {
            if (newUser != null) {
                this.mUserJourneyLogger.logUserCreateJourneyFinish(getCurrentUserId(), newUser);
            } else {
                this.mUserJourneyLogger.logNullUserJourneyError(4, getCurrentUserId(), -1, userType, flags);
            }
            t.traceEnd();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02ef  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x041e  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x043d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0314 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:309:0x0406 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v31 */
    /* JADX WARN: Type inference failed for: r40v1 */
    /* JADX WARN: Type inference failed for: r40v13 */
    /* JADX WARN: Type inference failed for: r40v15 */
    /* JADX WARN: Type inference failed for: r40v16 */
    /* JADX WARN: Type inference failed for: r40v17 */
    /* JADX WARN: Type inference failed for: r40v19 */
    /* JADX WARN: Type inference failed for: r40v3 */
    /* JADX WARN: Type inference failed for: r40v6 */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARN: Type inference failed for: r4v30 */
    /* JADX WARN: Type inference failed for: r4v31 */
    /* JADX WARN: Type inference failed for: r4v32 */
    /* JADX WARN: Type inference failed for: r4v33, types: [com.android.server.pm.UserTypeDetails] */
    /* JADX WARN: Type inference failed for: r4v36, types: [com.android.server.pm.UserTypeDetails] */
    /* JADX WARN: Type inference failed for: r4v37 */
    /* JADX WARN: Type inference failed for: r4v38 */
    /* JADX WARN: Type inference failed for: r4v5, types: [com.android.server.pm.UserTypeDetails] */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9 */
    /* JADX WARN: Type inference failed for: r60v0, types: [com.android.server.pm.UserManagerService] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.content.pm.UserInfo createUserInternalUncheckedNoTracing(java.lang.String r61, java.lang.String r62, int r63, int r64, boolean r65, java.lang.String[] r66, com.android.server.utils.TimingsTraceAndSlog r67, java.lang.Object r68) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1509
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserManagerService.createUserInternalUncheckedNoTracing(java.lang.String, java.lang.String, int, int, boolean, java.lang.String[], com.android.server.utils.TimingsTraceAndSlog, java.lang.Object):android.content.pm.UserInfo");
    }

    private void applyDefaultUserSettings(com.android.server.pm.UserTypeDetails userTypeDetails, int userId) {
        android.os.Bundle systemSettings = userTypeDetails.getDefaultSystemSettings();
        android.os.Bundle secureSettings = userTypeDetails.getDefaultSecureSettings();
        if (systemSettings.isEmpty() && secureSettings.isEmpty()) {
            return;
        }
        int systemSettingsSize = systemSettings.size();
        java.lang.String[] systemSettingsArray = (java.lang.String[]) systemSettings.keySet().toArray(new java.lang.String[systemSettingsSize]);
        for (int i = 0; i < systemSettingsSize; i++) {
            java.lang.String setting = systemSettingsArray[i];
            if (!android.provider.Settings.System.putStringForUser(this.mContext.getContentResolver(), setting, systemSettings.getString(setting), userId)) {
                android.util.Slog.e(LOG_TAG, "Failed to insert default system setting: " + setting);
            }
        }
        int secureSettingsSize = secureSettings.size();
        java.lang.String[] secureSettingsArray = (java.lang.String[]) secureSettings.keySet().toArray(new java.lang.String[secureSettingsSize]);
        for (int i2 = 0; i2 < secureSettingsSize; i2++) {
            java.lang.String setting2 = secureSettingsArray[i2];
            if (!android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), setting2, secureSettings.getString(setting2), userId)) {
                android.util.Slog.e(LOG_TAG, "Failed to insert default secure setting: " + setting2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDefaultCrossProfileIntentFilters(int profileUserId, com.android.server.pm.UserTypeDetails profileDetails, android.os.Bundle profileRestrictions, int parentUserId) {
        if (profileDetails != null && profileDetails.isProfile()) {
            java.util.List<com.android.server.pm.DefaultCrossProfileIntentFilter> filters = profileDetails.getDefaultCrossProfileIntentFilters();
            if (filters.isEmpty()) {
                return;
            }
            boolean disallowSharingIntoProfile = profileRestrictions.getBoolean("no_sharing_into_profile", false);
            int size = profileDetails.getDefaultCrossProfileIntentFilters().size();
            for (int i = 0; i < size; i++) {
                com.android.server.pm.DefaultCrossProfileIntentFilter filter = profileDetails.getDefaultCrossProfileIntentFilters().get(i);
                if (!disallowSharingIntoProfile || !filter.letsPersonalDataIntoProfile) {
                    if (filter.direction == 0) {
                        this.mPm.addCrossProfileIntentFilter(this.mPm.snapshotComputer(), filter.filter, this.mContext.getOpPackageName(), profileUserId, parentUserId, filter.flags);
                    } else {
                        this.mPm.addCrossProfileIntentFilter(this.mPm.snapshotComputer(), filter.filter, this.mContext.getOpPackageName(), parentUserId, profileUserId, filter.flags);
                    }
                }
            }
        }
    }

    private android.content.pm.UserInfo convertPreCreatedUserIfPossible(java.lang.String userType, int flags, java.lang.String name, final java.lang.Object token) {
        com.android.server.pm.UserManagerService.UserData preCreatedUserData;
        synchronized (this.mUsersLock) {
            preCreatedUserData = getPreCreatedUserLU(userType);
        }
        if (preCreatedUserData == null) {
            return null;
        }
        synchronized (this.mUserStates) {
            if (this.mUserStates.has(preCreatedUserData.info.id)) {
                android.util.Slog.w(LOG_TAG, "Cannot reuse pre-created user " + preCreatedUserData.info.id + " because it didn't stop yet");
                return null;
            }
            final android.content.pm.UserInfo preCreatedUser = preCreatedUserData.info;
            int newFlags = preCreatedUser.flags | flags;
            if (!checkUserTypeConsistency(newFlags)) {
                android.util.Slog.wtf(LOG_TAG, "Cannot reuse pre-created user " + preCreatedUser.id + " of type " + userType + " because flags are inconsistent. Flags (" + java.lang.Integer.toHexString(flags) + "); preCreatedUserFlags ( " + java.lang.Integer.toHexString(preCreatedUser.flags) + ").");
                return null;
            }
            android.util.Slog.i(LOG_TAG, "Reusing pre-created user " + preCreatedUser.id + " of type " + userType + " and bestowing on it flags " + android.content.pm.UserInfo.flagsToString(flags));
            preCreatedUser.name = name;
            preCreatedUser.flags = newFlags;
            preCreatedUser.preCreated = false;
            preCreatedUser.convertedFromPreCreated = true;
            preCreatedUser.creationTime = getCreationTime();
            synchronized (this.mPackagesLock) {
                writeUserLP(preCreatedUserData);
                writeUserListLP();
            }
            updateUserIds();
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda7
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$convertPreCreatedUserIfPossible$6(preCreatedUser, token);
                }
            });
            return preCreatedUser;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$convertPreCreatedUserIfPossible$6(android.content.pm.UserInfo preCreatedUser, java.lang.Object token) throws java.lang.Exception {
        this.mPm.onNewUserCreated(preCreatedUser.id, true);
        dispatchUserAdded(preCreatedUser, token);
        android.service.voice.VoiceInteractionManagerInternal vimi = (android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class);
        if (vimi != null) {
            vimi.onPreCreatedUserConversion(preCreatedUser.id);
        }
    }

    static boolean checkUserTypeConsistency(int flags) {
        return isAtMostOneFlag(flags & 4620) && isAtMostOneFlag(flags & com.android.server.wm.IActivityRecordExt.REASON_KEYGUARD_GOING_AWAY_NO_ANIMATION) && isAtMostOneFlag(flags & com.android.server.wm.IActivityRecordExt.REASON_SURFACE_ALREADY_CREATED);
    }

    private static boolean isAtMostOneFlag(int flags) {
        return ((flags + (-1)) & flags) == 0;
    }

    boolean installWhitelistedSystemPackages(boolean isFirstBoot, boolean isUpgrade, android.util.ArraySet<java.lang.String> existingPackages) {
        return this.mSystemPackageInstaller.installWhitelistedSystemPackages(isFirstBoot || this.mUpdatingSystemUserMode, isUpgrade, existingPackages);
    }

    public java.lang.String[] getPreInstallableSystemPackages(java.lang.String userType) {
        checkCreateUsersPermission("getPreInstallableSystemPackages");
        java.util.Set<java.lang.String> installableSystemPackages = this.mSystemPackageInstaller.getInstallablePackagesForUserType(userType);
        if (installableSystemPackages == null) {
            return null;
        }
        return (java.lang.String[]) installableSystemPackages.toArray(new java.lang.String[installableSystemPackages.size()]);
    }

    private long getCreationTime() {
        long now = java.lang.System.currentTimeMillis();
        if (now > EPOCH_PLUS_30_YEARS) {
            return now;
        }
        return 0L;
    }

    private void dispatchUserAdded(android.content.pm.UserInfo userInfo, java.lang.Object token) {
        java.lang.String str;
        synchronized (this.mUserLifecycleListeners) {
            for (int i = 0; i < this.mUserLifecycleListeners.size(); i++) {
                this.mUserLifecycleListeners.get(i).onUserCreated(userInfo, token);
            }
        }
        android.content.Intent addedIntent = new android.content.Intent("android.intent.action.USER_ADDED");
        addedIntent.addFlags(16777216);
        addedIntent.addFlags(67108864);
        addedIntent.putExtra("android.intent.extra.user_handle", userInfo.id);
        addedIntent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(userInfo.id));
        this.mContext.sendBroadcastAsUser(addedIntent, android.os.UserHandle.ALL, "android.permission.MANAGE_USERS");
        android.content.Context context = this.mContext;
        if (userInfo.isGuest()) {
            str = TRON_GUEST_CREATED;
        } else {
            str = userInfo.isDemo() ? TRON_DEMO_CREATED : TRON_USER_CREATED;
        }
        com.android.internal.logging.MetricsLogger.count(context, str, 1);
        if (userInfo.isProfile()) {
            sendProfileAddedBroadcast(userInfo.profileGroupId, userInfo.id);
        } else if (android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "user_switcher_enabled") == null) {
            android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "user_switcher_enabled", 1);
        }
    }

    private com.android.server.pm.UserManagerService.UserData getPreCreatedUserLU(java.lang.String userType) {
        int userSize = this.mUsers.size();
        for (int i = 0; i < userSize; i++) {
            com.android.server.pm.UserManagerService.UserData user = this.mUsers.valueAt(i);
            if (user.info.preCreated && !user.info.partial && user.info.userType.equals(userType)) {
                if (!user.info.isInitialized()) {
                    android.util.Slog.w(LOG_TAG, "found pre-created user of type " + userType + ", but it's not initialized yet: " + user.info.toFullString());
                } else {
                    return user;
                }
            }
        }
        return null;
    }

    private static boolean isUserTypeEligibleForPreCreation(com.android.server.pm.UserTypeDetails userTypeDetails) {
        return (userTypeDetails == null || userTypeDetails.isProfile() || userTypeDetails.getName().equals("android.os.usertype.full.RESTRICTED")) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerStatsCallbacks() {
        android.app.StatsManager statsManager = (android.app.StatsManager) this.mContext.getSystemService(android.app.StatsManager.class);
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.USER_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda6
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.onPullAtom(i, list);
            }
        });
        statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.MULTI_USER_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, new android.app.StatsManager.StatsPullAtomCallback() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda6
            public final int onPullAtom(int i, java.util.List list) {
                return this.f$0.onPullAtom(i, list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
        boolean isUserRunningUnlocked;
        com.android.server.pm.UserManagerService userManagerService = this;
        int i = -1;
        if (atomTag != 10152) {
            if (atomTag != 10160) {
                com.android.server.utils.Slogf.e(LOG_TAG, "Unexpected atom tag: %d", java.lang.Integer.valueOf(atomTag));
                return 1;
            }
            if (android.os.UserManager.getMaxSupportedUsers() > 1) {
                data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.MULTI_USER_INFO, android.os.UserManager.getMaxSupportedUsers(), isUserSwitcherEnabled(-1), android.os.UserManager.supportsMultipleUsers() && !hasUserRestriction("no_add_user", -1)));
                return 0;
            }
            return 0;
        }
        java.util.List<android.content.pm.UserInfo> users = userManagerService.getUsersInternal(true, true, true);
        int size = users.size();
        if (size > 1) {
            int idx = 0;
            while (idx < size) {
                android.content.pm.UserInfo user = users.get(idx);
                int userTypeStandard = com.android.server.pm.UserJourneyLogger.getUserTypeForStatsd(user.userType);
                java.lang.String userTypeCustom = userTypeStandard == 0 ? user.userType : null;
                synchronized (userManagerService.mUserStates) {
                    isUserRunningUnlocked = userManagerService.mUserStates.get(user.id, i) == 3;
                }
                data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.USER_INFO, user.id, userTypeStandard, userTypeCustom, user.flags, user.creationTime, user.lastLoggedInTime, isUserRunningUnlocked));
                idx++;
                i = -1;
                userManagerService = this;
            }
        }
        return 0;
    }

    com.android.server.pm.UserManagerService.UserData putUserInfo(android.content.pm.UserInfo userInfo) {
        com.android.server.pm.UserManagerService.UserData userData = new com.android.server.pm.UserManagerService.UserData();
        userData.info = userInfo;
        synchronized (this.mUsersLock) {
            this.mUsers.put(userInfo.id, userData);
        }
        updateUserIds();
        return userData;
    }

    void removeUserInfo(int userId) {
        synchronized (this.mUsersLock) {
            this.mUsers.remove(userId);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public android.content.pm.UserInfo createRestrictedProfileWithThrow(java.lang.String name, int parentUserId) throws android.os.ServiceSpecificException {
        checkCreateUsersPermission("setupRestrictedProfile");
        android.content.pm.UserInfo user = createProfileForUserWithThrow(name, "android.os.usertype.full.RESTRICTED", 0, parentUserId, null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            setUserRestriction("no_modify_accounts", true, user.id);
            android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "location_mode", 0, user.id);
            setUserRestriction("no_share_location", true, user.id);
            return user;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public java.util.List<android.content.pm.UserInfo> getGuestUsers() {
        checkManageUsersPermission("getGuestUsers");
        java.util.ArrayList<android.content.pm.UserInfo> guestUsers = new java.util.ArrayList<>();
        synchronized (this.mUsersLock) {
            int size = this.mUsers.size();
            for (int i = 0; i < size; i++) {
                android.content.pm.UserInfo user = this.mUsers.valueAt(i).info;
                if (user.isGuest() && !user.guestToRemove && !user.preCreated && !this.mRemovingUserIds.get(user.id)) {
                    guestUsers.add(user);
                }
            }
        }
        return guestUsers;
    }

    public boolean markGuestForDeletion(int userId) {
        checkManageUsersPermission("Only the system can remove users");
        if (getUserRestrictions(android.os.UserHandle.getCallingUserId()).getBoolean("no_remove_user", false)) {
            android.util.Slog.w(LOG_TAG, "Cannot remove user. DISALLOW_REMOVE_USER is enabled.");
            return false;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mPackagesLock) {
                synchronized (this.mUsersLock) {
                    com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
                    if (userId != 0 && userData != null && !this.mRemovingUserIds.get(userId)) {
                        if (!userData.info.isGuest()) {
                            return false;
                        }
                        userData.info.guestToRemove = true;
                        userData.info.flags |= 64;
                        writeUserLP(userData);
                        return true;
                    }
                    return false;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean removeUser(int userId) {
        android.util.Slog.i(LOG_TAG, "removeUser u" + userId);
        checkCreateUsersPermission("Only the system can remove users");
        java.lang.String restriction = getUserRemovalRestriction(userId);
        if (getUserRestrictions(android.os.UserHandle.getCallingUserId()).getBoolean(restriction, false)) {
            android.util.Slog.w(LOG_TAG, "Cannot remove user. " + restriction + " is enabled.");
            return false;
        }
        return removeUserWithProfilesUnchecked(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeUserWithProfilesUnchecked(int userId) {
        synchronized (this.mUsersLock) {
            int userRemovability = getUserRemovabilityLocked(userId, "removed");
            if (userRemovability != 3) {
                return android.os.UserManager.isRemoveResultSuccessful(userRemovability);
            }
            com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
            boolean isProfile = userData.info.isProfile();
            android.util.IntArray profileIdsLU = null;
            if (!isProfile) {
                profileIdsLU = getProfileIdsLU(userId, null, false, false);
            }
            android.util.IntArray profileIds = profileIdsLU;
            if (!isProfile) {
                android.util.Pair<java.lang.Integer, java.lang.Integer> currentAndTargetUserIds = getCurrentAndTargetUserIds();
                if (userId == ((java.lang.Integer) currentAndTargetUserIds.first).intValue()) {
                    android.util.Slog.w(LOG_TAG, "Current user cannot be removed.");
                    return false;
                }
                if (userId == ((java.lang.Integer) currentAndTargetUserIds.second).intValue()) {
                    android.util.Slog.w(LOG_TAG, "Target user of an ongoing user switch cannot be removed.");
                    return false;
                }
                for (int i = profileIds.size() - 1; i >= 0; i--) {
                    int profileId = profileIds.get(i);
                    if (profileId != userId) {
                        android.util.Slog.i(LOG_TAG, "removing profile:" + profileId + " associated with user:" + userId);
                        if (removeUserUnchecked(profileId)) {
                            continue;
                        } else {
                            android.util.Slog.i(LOG_TAG, "Unable to immediately remove profile " + profileId + "associated with user " + userId + ". User is set as ephemeral and will be removed on user switch or reboot.");
                            synchronized (this.mPackagesLock) {
                                com.android.server.pm.UserManagerService.UserData profileData = getUserDataNoChecks(userId);
                                profileData.info.flags |= 256;
                                writeUserLP(profileData);
                            }
                        }
                    }
                }
            }
            return removeUserUnchecked(userId);
        }
    }

    public boolean removeUserEvenWhenDisallowed(int userId) {
        checkCreateUsersPermission("Only the system can remove users");
        return removeUserWithProfilesUnchecked(userId);
    }

    private java.lang.String getUserRemovalRestriction(int userId) {
        android.content.pm.UserInfo userInfo;
        synchronized (this.mUsersLock) {
            userInfo = getUserInfoLU(userId);
        }
        boolean isManagedProfile = userInfo != null && userInfo.isManagedProfile();
        return isManagedProfile ? "no_remove_managed_profile" : "no_remove_user";
    }

    private boolean removeUserUnchecked(int userId) {
        if (this.mWrapper.getExtImpl().checkUserIfNeed(userId)) {
            return false;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mPackagesLock) {
                synchronized (this.mUsersLock) {
                    int userRemovability = getUserRemovabilityLocked(userId, "removed");
                    if (userRemovability != 3) {
                        boolean zIsRemoveResultSuccessful = android.os.UserManager.isRemoveResultSuccessful(userRemovability);
                        android.os.Binder.restoreCallingIdentity(ident);
                        return zIsRemoveResultSuccessful;
                    }
                    final com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
                    android.util.Slog.i(LOG_TAG, "Removing user " + userId);
                    addRemovingUserIdLocked(userId);
                    userData.info.partial = true;
                    userData.info.flags |= 64;
                    writeUserLP(userData);
                    this.mUserJourneyLogger.logUserJourneyBegin(userId, 6);
                    this.mUserJourneyLogger.startSessionForDelayedJourney(userId, 9, userData.info.creationTime);
                    try {
                        this.mAppOpsService.removeUser(userId);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(LOG_TAG, "Unable to notify AppOpsService of removing user.", e);
                    }
                    this.mWrapper.getExtImpl().onRemoveUserUnchecked(userId);
                    if (userData.info.profileGroupId != -10000 && userData.info.isProfile()) {
                        sendProfileRemovedBroadcast(userData.info.profileGroupId, userData.info.id, userData.info.userType);
                    }
                    try {
                        int res = android.app.ActivityManager.getService().stopUserWithCallback(userId, new android.app.IStopUserCallback.Stub() { // from class: com.android.server.pm.UserManagerService.6
                            public void userStopped(int userIdParam) throws java.lang.Throwable {
                                com.android.server.pm.UserManagerService.this.finishRemoveUser(userIdParam);
                                int originUserId = com.android.server.pm.UserManagerService.this.getCurrentUserId();
                                com.android.server.pm.UserManagerService.this.mUserJourneyLogger.logUserJourneyFinishWithError(originUserId, userData.info, 6, -1);
                                com.android.server.pm.UserManagerService.this.mUserJourneyLogger.logDelayedUserJourneyFinishWithError(originUserId, userData.info, 9, -1);
                            }

                            public void userStopAborted(int userIdParam) throws java.lang.Throwable {
                                int originUserId = com.android.server.pm.UserManagerService.this.getCurrentUserId();
                                com.android.server.pm.UserManagerService.this.mUserJourneyLogger.logUserJourneyFinishWithError(originUserId, userData.info, 6, 3);
                                com.android.server.pm.UserManagerService.this.mUserJourneyLogger.logDelayedUserJourneyFinishWithError(originUserId, userData.info, 9, 3);
                            }
                        });
                        boolean z = res == 0;
                        android.os.Binder.restoreCallingIdentity(ident);
                        return z;
                    } catch (android.os.RemoteException e2) {
                        android.util.Slog.w(LOG_TAG, "Failed to stop user during removal.", e2);
                        android.os.Binder.restoreCallingIdentity(ident);
                        return false;
                    }
                }
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    void addRemovingUserId(int userId) {
        synchronized (this.mUsersLock) {
            addRemovingUserIdLocked(userId);
        }
    }

    void addRemovingUserIdLocked(int userId) {
        this.mRemovingUserIds.put(userId, true);
        this.mRecentlyRemovedIds.add(java.lang.Integer.valueOf(userId));
        if (this.mRecentlyRemovedIds.size() > 100) {
            this.mRecentlyRemovedIds.removeFirst();
        }
    }

    public int removeUserWhenPossible(int userId, boolean overrideDevicePolicy) {
        android.util.Slog.i(LOG_TAG, "removeUserWhenPossible u" + userId);
        checkCreateUsersPermission("Only the system can remove users");
        if (!overrideDevicePolicy) {
            java.lang.String restriction = getUserRemovalRestriction(userId);
            if (getUserRestrictions(android.os.UserHandle.getCallingUserId()).getBoolean(restriction, false)) {
                android.util.Slog.w(LOG_TAG, "Cannot remove user. " + restriction + " is enabled.");
                return -2;
            }
        }
        android.util.Slog.i(LOG_TAG, "Attempting to immediately remove user " + userId);
        if (removeUserWithProfilesUnchecked(userId)) {
            return 0;
        }
        android.util.Slog.i(LOG_TAG, android.text.TextUtils.formatSimple("Unable to immediately remove user %d. Now trying to set it ephemeral.", new java.lang.Object[]{java.lang.Integer.valueOf(userId)}));
        return setUserEphemeralUnchecked(userId);
    }

    private int getUserRemovabilityLocked(int userId, java.lang.String msg) {
        java.lang.String prefix = android.text.TextUtils.formatSimple("User %d can not be %s, ", new java.lang.Object[]{java.lang.Integer.valueOf(userId), msg});
        if (userId == 0) {
            android.util.Slog.e(LOG_TAG, prefix + "system user cannot be removed.");
            return -4;
        }
        com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId);
        if (userData == null) {
            android.util.Slog.e(LOG_TAG, prefix + "invalid user id provided.");
            return -3;
        }
        if (isNonRemovableMainUser(userData.info)) {
            android.util.Slog.e(LOG_TAG, prefix + "main user cannot be removed when it's a permanent admin user.");
            return -5;
        }
        if (this.mRemovingUserIds.get(userId)) {
            android.util.Slog.w(LOG_TAG, prefix + "it is already scheduled for removal.");
            return 2;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishRemoveUser(int userId) {
        android.content.pm.UserInfo user;
        android.util.Slog.i(LOG_TAG, "finishRemoveUser " + userId);
        synchronized (this.mUsersLock) {
            user = getUserInfoLU(userId);
        }
        if (user != null && user.preCreated) {
            android.util.Slog.i(LOG_TAG, "Removing a pre-created user with user id: " + userId);
            ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).onUserStopped(userId);
            removeUserState(userId);
            return;
        }
        synchronized (this.mUserLifecycleListeners) {
            for (int i = 0; i < this.mUserLifecycleListeners.size(); i++) {
                this.mUserLifecycleListeners.get(i).onUserRemoved(user);
            }
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.content.Intent removedIntent = new android.content.Intent("android.intent.action.USER_REMOVED");
            removedIntent.addFlags(16777216);
            removedIntent.putExtra("android.intent.extra.user_handle", userId);
            removedIntent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId));
            getActivityManagerInternal().broadcastIntentWithCallback(removedIntent, new com.android.server.pm.UserManagerService.AnonymousClass7(userId), new java.lang.String[]{"android.permission.MANAGE_USERS"}, -1, (int[]) null, (java.util.function.BiFunction) null, (android.os.Bundle) null);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: renamed from: com.android.server.pm.UserManagerService$7, reason: invalid class name */
    class AnonymousClass7 extends android.content.IIntentReceiver.Stub {
        final /* synthetic */ int val$userId;

        AnonymousClass7(int i) {
            this.val$userId = i;
        }

        public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            final int i = this.val$userId;
            new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerService$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$performReceive$0(i);
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$performReceive$0(int userId) {
            com.android.server.pm.UserManagerService.this.getActivityManagerInternal().onUserRemoved(userId);
            com.android.server.pm.UserManagerService.this.removeUserState(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUserState(int userId) {
        android.util.Slog.i(LOG_TAG, "Removing user state of user " + userId);
        this.mLockPatternUtils.removeUser(userId);
        try {
            ((android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class)).destroyUserStorageKeys(userId);
        } catch (java.lang.IllegalStateException e) {
            android.util.Slog.i(LOG_TAG, "Destroying storage keys for user " + userId + " failed, continuing anyway", e);
        }
        this.mPm.cleanUpUser(this, userId);
        this.mUserDataPreparer.destroyUserData(userId, 3);
        synchronized (this.mUsersLock) {
            this.mUsers.remove(userId);
            this.mIsUserManaged.delete(userId);
        }
        synchronized (this.mUserStates) {
            this.mUserStates.delete(userId);
        }
        synchronized (this.mRestrictionsLock) {
            this.mBaseUserRestrictions.remove(userId);
            this.mAppliedUserRestrictions.remove(userId);
            this.mCachedEffectiveUserRestrictions.remove(userId);
            if (this.mDevicePolicyUserRestrictions.remove(userId)) {
                applyUserRestrictionsForAllUsersLR();
            }
        }
        synchronized (this.mPackagesLock) {
            writeUserListLP();
        }
        getUserFile(userId).delete();
        updateUserIds();
        this.mWrapper.getExtImpl().onRemoveUserState(userId);
        this.mWrapper.getExtImpl().onMultiAppUserRemoved(this.mContext, this.mRemovingUserIds, userId);
    }

    private void sendProfileAddedBroadcast(int parentUserId, int addedUserId) {
        sendProfileBroadcast(new android.content.Intent("android.intent.action.PROFILE_ADDED"), parentUserId, addedUserId);
    }

    private void sendProfileRemovedBroadcast(int parentUserId, int removedUserId, java.lang.String userType) {
        if (java.util.Objects.equals(userType, "android.os.usertype.profile.MANAGED")) {
            sendManagedProfileRemovedBroadcast(parentUserId, removedUserId);
        }
        sendProfileBroadcast(new android.content.Intent("android.intent.action.PROFILE_REMOVED"), parentUserId, removedUserId);
    }

    private void sendProfileBroadcast(android.content.Intent intent, int parentUserId, int userId) {
        android.os.UserHandle parentHandle = android.os.UserHandle.of(parentUserId);
        intent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId));
        intent.addFlags(1342177280);
        this.mContext.sendBroadcastAsUser(intent, parentHandle, null);
    }

    private void sendManagedProfileRemovedBroadcast(int parentUserId, int removedUserId) {
        android.content.Intent managedProfileIntent = new android.content.Intent("android.intent.action.MANAGED_PROFILE_REMOVED");
        managedProfileIntent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(removedUserId));
        managedProfileIntent.putExtra("android.intent.extra.user_handle", removedUserId);
        android.os.UserHandle parentHandle = android.os.UserHandle.of(parentUserId);
        getDevicePolicyManagerInternal().broadcastIntentToManifestReceivers(managedProfileIntent, parentHandle, false);
        managedProfileIntent.addFlags(1342177280);
        this.mContext.sendBroadcastAsUser(managedProfileIntent, parentHandle, null);
    }

    public android.os.Bundle getApplicationRestrictions(java.lang.String packageName) {
        return getApplicationRestrictionsForUser(packageName, android.os.UserHandle.getCallingUserId());
    }

    public android.os.Bundle getApplicationRestrictionsForUser(java.lang.String packageName, int userId) {
        android.os.Bundle applicationRestrictionsLAr;
        if (android.os.UserHandle.getCallingUserId() != userId || !android.os.UserHandle.isSameApp(android.os.Binder.getCallingUid(), getUidForPackage(packageName))) {
            checkSystemOrRoot("get application restrictions for other user/app " + packageName);
        }
        synchronized (this.mAppRestrictionsLock) {
            applicationRestrictionsLAr = readApplicationRestrictionsLAr(packageName, userId);
        }
        return applicationRestrictionsLAr;
    }

    public void setApplicationRestrictions(java.lang.String packageName, android.os.Bundle restrictions, int userId) {
        boolean changed;
        checkSystemOrRoot("set application restrictions");
        java.lang.String validationResult = validateName(packageName);
        if (validationResult != null) {
            if (packageName.contains("../")) {
                android.util.EventLog.writeEvent(1397638484, "239701237", -1, "");
            }
            throw new java.lang.IllegalArgumentException("Invalid package name: " + validationResult);
        }
        if (restrictions != null) {
            restrictions.setDefusable(true);
        }
        synchronized (this.mAppRestrictionsLock) {
            if (restrictions == null) {
                changed = cleanAppRestrictionsForPackageLAr(packageName, userId);
            } else if (restrictions.isEmpty()) {
                changed = cleanAppRestrictionsForPackageLAr(packageName, userId);
            } else {
                writeApplicationRestrictionsLAr(packageName, restrictions, userId);
                changed = true;
            }
        }
        if (!changed) {
            return;
        }
        android.content.Intent changeIntent = new android.content.Intent("android.intent.action.APPLICATION_RESTRICTIONS_CHANGED");
        changeIntent.setPackage(packageName);
        changeIntent.addFlags(1073741824);
        this.mContext.sendBroadcastAsUser(changeIntent, android.os.UserHandle.of(userId));
    }

    static java.lang.String validateName(java.lang.String name) {
        int n = name.length();
        boolean front = true;
        for (int i = 0; i < n; i++) {
            char c = name.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                front = false;
            } else {
                if (!front) {
                    if ((c < '0' || c > '9') && c != '_') {
                        if (c == '.') {
                            front = true;
                        }
                    }
                }
                return "bad character '" + c + "'";
            }
        }
        return null;
    }

    private int getUidForPackage(java.lang.String packageName) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            int i = this.mContext.getPackageManager().getApplicationInfo(packageName, 4194304).uid;
            android.os.Binder.restoreCallingIdentity(ident);
            return i;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.os.Binder.restoreCallingIdentity(ident);
            return -1;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    private static android.os.Bundle readApplicationRestrictionsLAr(java.lang.String packageName, int userId) {
        android.util.AtomicFile restrictionsFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getUserSystemDirectory(userId), packageToRestrictionsFileName(packageName)));
        return readApplicationRestrictionsLAr(restrictionsFile);
    }

    static android.os.Bundle readApplicationRestrictionsLAr(android.util.AtomicFile restrictionsFile) {
        com.android.modules.utils.TypedXmlPullParser parser;
        android.os.Bundle restrictions = new android.os.Bundle();
        java.util.ArrayList<java.lang.String> values = new java.util.ArrayList<>();
        if (!restrictionsFile.getBaseFile().exists()) {
            return restrictions;
        }
        java.io.FileInputStream fis = null;
        try {
            try {
                fis = restrictionsFile.openRead();
                parser = android.util.Xml.resolvePullParser(fis);
                com.android.internal.util.XmlUtils.nextElement(parser);
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.w(LOG_TAG, "Error parsing " + restrictionsFile.getBaseFile(), e);
            }
            if (parser.getEventType() != 2) {
                android.util.Slog.e(LOG_TAG, "Unable to read restrictions file " + restrictionsFile.getBaseFile());
                return restrictions;
            }
            while (parser.next() != 1) {
                readEntry(restrictions, values, parser);
            }
            return restrictions;
        } finally {
            libcore.io.IoUtils.closeQuietly((java.lang.AutoCloseable) null);
        }
    }

    private static void readEntry(android.os.Bundle restrictions, java.util.ArrayList<java.lang.String> values, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (parser.getEventType() == 2 && parser.getName().equals(TAG_ENTRY)) {
            java.lang.String key = parser.getAttributeValue((java.lang.String) null, ATTR_KEY);
            java.lang.String valType = parser.getAttributeValue((java.lang.String) null, "type");
            int count = parser.getAttributeInt((java.lang.String) null, ATTR_MULTIPLE, -1);
            if (count != -1) {
                values.clear();
                while (count > 0) {
                    int type = parser.next();
                    if (type == 1) {
                        break;
                    }
                    if (type == 2 && parser.getName().equals(TAG_VALUE)) {
                        values.add(parser.nextText().trim());
                        count--;
                    }
                }
                java.lang.String[] valueStrings = new java.lang.String[values.size()];
                values.toArray(valueStrings);
                restrictions.putStringArray(key, valueStrings);
                return;
            }
            if (ATTR_TYPE_BUNDLE.equals(valType)) {
                restrictions.putBundle(key, readBundleEntry(parser, values));
                return;
            }
            if (ATTR_TYPE_BUNDLE_ARRAY.equals(valType)) {
                int outerDepth = parser.getDepth();
                java.util.ArrayList<android.os.Bundle> bundleList = new java.util.ArrayList<>();
                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                    android.os.Bundle childBundle = readBundleEntry(parser, values);
                    bundleList.add(childBundle);
                }
                restrictions.putParcelableArray(key, (android.os.Parcelable[]) bundleList.toArray(new android.os.Bundle[bundleList.size()]));
                return;
            }
            java.lang.String value = parser.nextText().trim();
            if (ATTR_TYPE_BOOLEAN.equals(valType)) {
                restrictions.putBoolean(key, java.lang.Boolean.parseBoolean(value));
            } else if (ATTR_TYPE_INTEGER.equals(valType)) {
                restrictions.putInt(key, java.lang.Integer.parseInt(value));
            } else {
                restrictions.putString(key, value);
            }
        }
    }

    private static android.os.Bundle readBundleEntry(com.android.modules.utils.TypedXmlPullParser parser, java.util.ArrayList<java.lang.String> values) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.os.Bundle childBundle = new android.os.Bundle();
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            readEntry(childBundle, values, parser);
        }
        return childBundle;
    }

    private static void writeApplicationRestrictionsLAr(java.lang.String packageName, android.os.Bundle restrictions, int userId) {
        android.util.AtomicFile restrictionsFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getUserSystemDirectory(userId), packageToRestrictionsFileName(packageName)));
        writeApplicationRestrictionsLAr(restrictions, restrictionsFile);
    }

    static void writeApplicationRestrictionsLAr(android.os.Bundle restrictions, android.util.AtomicFile restrictionsFile) {
        java.io.FileOutputStream fos = null;
        try {
            fos = restrictionsFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(fos);
            serializer.startDocument((java.lang.String) null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag((java.lang.String) null, TAG_RESTRICTIONS);
            writeBundle(restrictions, serializer);
            serializer.endTag((java.lang.String) null, TAG_RESTRICTIONS);
            serializer.endDocument();
            restrictionsFile.finishWrite(fos);
        } catch (java.lang.Exception e) {
            restrictionsFile.failWrite(fos);
            android.util.Slog.e(LOG_TAG, "Error writing application restrictions list", e);
        }
    }

    private static void writeBundle(android.os.Bundle restrictions, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        for (java.lang.String key : restrictions.keySet()) {
            java.lang.Object value = restrictions.get(key);
            serializer.startTag((java.lang.String) null, TAG_ENTRY);
            serializer.attribute((java.lang.String) null, ATTR_KEY, key);
            if (value instanceof java.lang.Boolean) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BOOLEAN);
                serializer.text(value.toString());
            } else if (value instanceof java.lang.Integer) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_INTEGER);
                serializer.text(value.toString());
            } else if (value == null || (value instanceof java.lang.String)) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_STRING);
                serializer.text(value != null ? (java.lang.String) value : "");
            } else if (value instanceof android.os.Bundle) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BUNDLE);
                writeBundle((android.os.Bundle) value, serializer);
            } else {
                int i = 0;
                if (value instanceof android.os.Parcelable[]) {
                    serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BUNDLE_ARRAY);
                    android.os.Parcelable[] array = (android.os.Parcelable[]) value;
                    int length = array.length;
                    while (i < length) {
                        android.os.Parcelable parcelable = array[i];
                        if (!(parcelable instanceof android.os.Bundle)) {
                            throw new java.lang.IllegalArgumentException("bundle-array can only hold Bundles");
                        }
                        serializer.startTag((java.lang.String) null, TAG_ENTRY);
                        serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BUNDLE);
                        writeBundle((android.os.Bundle) parcelable, serializer);
                        serializer.endTag((java.lang.String) null, TAG_ENTRY);
                        i++;
                    }
                } else {
                    serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_STRING_ARRAY);
                    java.lang.String[] values = (java.lang.String[]) value;
                    serializer.attributeInt((java.lang.String) null, ATTR_MULTIPLE, values.length);
                    int length2 = values.length;
                    while (i < length2) {
                        java.lang.String choice = values[i];
                        serializer.startTag((java.lang.String) null, TAG_VALUE);
                        serializer.text(choice != null ? choice : "");
                        serializer.endTag((java.lang.String) null, TAG_VALUE);
                        i++;
                    }
                }
            }
            serializer.endTag((java.lang.String) null, TAG_ENTRY);
        }
    }

    public int getUserSerialNumber(int userId) {
        int i;
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            i = userInfo != null ? userInfo.serialNumber : -1;
        }
        return i;
    }

    public boolean isUserNameSet(int userId) {
        boolean z;
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (!hasQueryOrCreateUsersPermission() && (callingUserId != userId || !hasPermissionGranted("android.permission.GET_ACCOUNTS_PRIVILEGED", callingUid))) {
            throw new java.lang.SecurityException("You need MANAGE_USERS, CREATE_USERS, QUERY_USERS, or GET_ACCOUNTS_PRIVILEGED permissions to: get whether user name is set");
        }
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            z = (userInfo == null || userInfo.name == null) ? false : true;
        }
        return z;
    }

    public int getUserHandle(int userSerialNumber) {
        synchronized (this.mUsersLock) {
            for (int userId : this.mUserIds) {
                android.content.pm.UserInfo info = getUserInfoLU(userId);
                if (info != null && info.serialNumber == userSerialNumber) {
                    return userId;
                }
            }
            return -1;
        }
    }

    public long getUserCreationTime(int userId) {
        int callingUserId = android.os.UserHandle.getCallingUserId();
        android.content.pm.UserInfo userInfo = null;
        synchronized (this.mUsersLock) {
            if (callingUserId == userId) {
                userInfo = getUserInfoLU(userId);
            } else {
                android.content.pm.UserInfo parent = getProfileParentLU(userId);
                if (parent != null && parent.id == callingUserId) {
                    userInfo = getUserInfoLU(userId);
                }
            }
        }
        if (userInfo == null) {
            throw new java.lang.SecurityException("userId can only be the calling user or a profile associated with this user");
        }
        return userInfo.creationTime;
    }

    private void updateUserIds() {
        int num = 0;
        int numIncludingPreCreated = 0;
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo userInfo = this.mUsers.valueAt(i).info;
                if (!userInfo.partial) {
                    numIncludingPreCreated++;
                    if (!userInfo.preCreated) {
                        num++;
                    }
                }
            }
            int[] newUsers = new int[num];
            int[] newUsersIncludingPreCreated = new int[numIncludingPreCreated];
            int n = 0;
            int n2 = 0;
            for (int i2 = 0; i2 < userSize; i2++) {
                android.content.pm.UserInfo userInfo2 = this.mUsers.valueAt(i2).info;
                if (!userInfo2.partial) {
                    int userId = this.mUsers.keyAt(i2);
                    int nIncludingPreCreated = n2 + 1;
                    newUsersIncludingPreCreated[n2] = userId;
                    if (userInfo2.preCreated) {
                        n2 = nIncludingPreCreated;
                    } else {
                        newUsers[n] = userId;
                        n++;
                        n2 = nIncludingPreCreated;
                    }
                }
            }
            this.mUserIds = newUsers;
            this.mUserIdsIncludingPreCreated = newUsersIncludingPreCreated;
            android.content.pm.UserPackage.setValidUserIds(this.mUserIds);
        }
    }

    public void onBeforeStartUser(int userId) {
        android.content.pm.UserInfo userInfo = getUserInfo(userId);
        if (userInfo == null) {
            return;
        }
        long functionStart = android.os.SystemClock.elapsedRealtime();
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("onBeforeStartUser-" + userId);
        boolean migrateAppsData = !android.content.pm.PackagePartitions.FINGERPRINT.equals(userInfo.lastLoggedInFingerprint);
        t.traceBegin("prepareUserData");
        this.mUserDataPreparer.prepareUserData(userInfo, 1);
        t.traceEnd();
        long pmReconcileStart = android.os.SystemClock.elapsedRealtime();
        long prepareUserDataCost = pmReconcileStart - functionStart;
        t.traceBegin("reconcileAppsData");
        getPackageManagerInternal().reconcileAppsData(userId, 1, migrateAppsData);
        t.traceEnd();
        long pmReconcileCost = android.os.SystemClock.elapsedRealtime() - pmReconcileStart;
        if (userId != 0) {
            t.traceBegin("applyUserRestrictions");
            synchronized (this.mRestrictionsLock) {
                applyUserRestrictionsLR(userId);
            }
            t.traceEnd();
        }
        t.traceEnd();
        this.mWrapper.getExtImpl().onBeforeStartUserExit(userId, android.os.SystemClock.elapsedRealtime() - functionStart, prepareUserDataCost, pmReconcileCost);
    }

    public void onBeforeUnlockUser(int userId) {
        android.content.pm.UserInfo userInfo = getUserInfo(userId);
        if (userInfo == null) {
            return;
        }
        this.mWrapper.getExtImpl().normalizeExternalStorageData(userId);
        boolean migrateAppsData = !android.content.pm.PackagePartitions.FINGERPRINT.equals(userInfo.lastLoggedInFingerprint);
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("prepareUserData-" + userId);
        this.mUserDataPreparer.prepareUserData(userInfo, 2);
        t.traceEnd();
        android.os.storage.StorageManagerInternal smInternal = (android.os.storage.StorageManagerInternal) com.android.server.LocalServices.getService(android.os.storage.StorageManagerInternal.class);
        smInternal.markCeStoragePrepared(userId);
        t.traceBegin("reconcileAppsData-" + userId);
        getPackageManagerInternal().reconcileAppsData(userId, 2, migrateAppsData);
        t.traceEnd();
    }

    void reconcileUsers(java.lang.String volumeUuid) {
        this.mUserDataPreparer.reconcileUsers(volumeUuid, getUsers(true, true, false));
    }

    public void onUserLoggedIn(int userId) {
        com.android.server.pm.UserManagerService.UserData userData = getUserDataNoChecks(userId);
        if (userData == null || userData.info.partial) {
            android.util.Slog.w(LOG_TAG, "userForeground: unknown user #" + userId);
            return;
        }
        long now = java.lang.System.currentTimeMillis();
        if (now > EPOCH_PLUS_30_YEARS) {
            userData.info.lastLoggedInTime = now;
        }
        userData.info.lastLoggedInFingerprint = android.content.pm.PackagePartitions.FINGERPRINT;
        scheduleWriteUser(userId);
    }

    int getNextAvailableId() {
        synchronized (this.mUsersLock) {
            int nextId = scanNextAvailableIdLocked();
            if (nextId >= 0) {
                return nextId;
            }
            if (this.mRemovingUserIds.size() > 0) {
                android.util.Slog.i(LOG_TAG, "All available IDs are used. Recycling LRU ids.");
                this.mRemovingUserIds.clear();
                for (java.lang.Integer recentlyRemovedId : this.mRecentlyRemovedIds) {
                    this.mRemovingUserIds.put(recentlyRemovedId.intValue(), true);
                }
                nextId = scanNextAvailableIdLocked();
            }
            android.os.UserManager.invalidateStaticUserProperties();
            android.os.UserManager.invalidateUserPropertiesCache();
            if (nextId < 0) {
                throw new java.lang.IllegalStateException("No user id available!");
            }
            return nextId;
        }
    }

    private int scanNextAvailableIdLocked() {
        for (int i = 10; i < MAX_USER_ID; i++) {
            if (this.mUsers.indexOfKey(i) < 0 && !this.mRemovingUserIds.get(i) && !this.mWrapper.getExtImpl().skipCustomUserId(i)) {
                return i;
            }
        }
        return -1;
    }

    private static java.lang.String packageToRestrictionsFileName(java.lang.String packageName) {
        return RESTRICTIONS_FILE_PREFIX + packageName + XML_SUFFIX;
    }

    private static java.lang.String getRedacted(java.lang.String string) {
        if (string == null) {
            return null;
        }
        return string.length() + "_chars";
    }

    public void setSeedAccountData(int userId, java.lang.String accountName, java.lang.String accountType, android.os.PersistableBundle accountOptions, boolean persist) {
        checkManageUsersPermission("set user seed account data");
        setSeedAccountDataNoChecks(userId, accountName, accountType, accountOptions, persist);
    }

    private void setSeedAccountDataNoChecks(int userId, java.lang.String accountName, java.lang.String accountType, android.os.PersistableBundle accountOptions, boolean persist) {
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData userData = getUserDataLU(userId);
                if (userData == null) {
                    android.util.Slog.e(LOG_TAG, "No such user for settings seed data u=" + userId);
                    return;
                }
                userData.seedAccountName = truncateString(accountName, 500);
                userData.seedAccountType = truncateString(accountType, 500);
                if (accountOptions != null && accountOptions.isBundleContentsWithinLengthLimit(1000)) {
                    userData.seedAccountOptions = accountOptions;
                }
                userData.persistSeedData = persist;
                if (persist) {
                    writeUserLP(userData);
                }
            }
        }
    }

    public java.lang.String getSeedAccountName(int userId) throws android.os.RemoteException {
        java.lang.String str;
        checkManageUsersPermission("Cannot get seed account information");
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData userData = getUserDataLU(userId);
            str = userData == null ? null : userData.seedAccountName;
        }
        return str;
    }

    public java.lang.String getSeedAccountType(int userId) throws android.os.RemoteException {
        java.lang.String str;
        checkManageUsersPermission("Cannot get seed account information");
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData userData = getUserDataLU(userId);
            str = userData == null ? null : userData.seedAccountType;
        }
        return str;
    }

    public android.os.PersistableBundle getSeedAccountOptions(int userId) throws android.os.RemoteException {
        android.os.PersistableBundle persistableBundle;
        checkManageUsersPermission("Cannot get seed account information");
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData userData = getUserDataLU(userId);
            persistableBundle = userData == null ? null : userData.seedAccountOptions;
        }
        return persistableBundle;
    }

    public void clearSeedAccountData(int userId) throws android.os.RemoteException {
        checkManageUsersPermission("Cannot clear seed account information");
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData userData = getUserDataLU(userId);
                if (userData == null) {
                    return;
                }
                userData.clearSeedAccountData();
                writeUserLP(userData);
            }
        }
    }

    public boolean someUserHasSeedAccount(java.lang.String accountName, java.lang.String accountType) {
        checkManageUsersPermission("check seed account information");
        return someUserHasSeedAccountNoChecks(accountName, accountType);
    }

    private boolean someUserHasSeedAccountNoChecks(java.lang.String accountName, java.lang.String accountType) {
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                com.android.server.pm.UserManagerService.UserData data = this.mUsers.valueAt(i);
                if (!data.info.isInitialized() && !this.mRemovingUserIds.get(data.info.id) && data.seedAccountName != null && data.seedAccountName.equals(accountName) && data.seedAccountType != null && data.seedAccountType.equals(accountType)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean someUserHasAccount(java.lang.String accountName, java.lang.String accountType) {
        checkCreateUsersPermission("check seed account information");
        return someUserHasAccountNoChecks(accountName, accountType);
    }

    private boolean someUserHasAccountNoChecks(final java.lang.String accountName, final java.lang.String accountType) {
        if (android.text.TextUtils.isEmpty(accountName) || android.text.TextUtils.isEmpty(accountType)) {
            return false;
        }
        final android.accounts.Account account = new android.accounts.Account(accountName, accountType);
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.UserManagerService$$ExternalSyntheticLambda8
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$someUserHasAccountNoChecks$7(account, accountName, accountType);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$someUserHasAccountNoChecks$7(android.accounts.Account account, java.lang.String accountName, java.lang.String accountType) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(android.accounts.AccountManager.get(this.mContext).someUserHasAccount(account) || someUserHasSeedAccountNoChecks(accountName, accountType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLastEnteredForegroundTimeToNow(com.android.server.pm.UserManagerService.UserData userData) {
        userData.mLastEnteredForegroundTimeMillis = java.lang.System.currentTimeMillis();
        scheduleWriteUser(userData.info.id);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.pm.UserManagerServiceShellCommand(this, this.mSystemPackageInstaller, this.mLockPatternUtils, this.mContext).exec(this, in, out, err, args, callback, resultReceiver);
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        int i;
        if (!com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, pw)) {
            return;
        }
        long now = java.lang.System.currentTimeMillis();
        long nowRealtime = android.os.SystemClock.elapsedRealtime();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (args != null && args.length > 0) {
            byte b = 0;
            java.lang.String str = args[0];
            switch (str.hashCode()) {
                case -1247813202:
                    b = str.equals("--visibility-mediator") ? (byte) 1 : (byte) -1;
                    break;
                case 1333469547:
                    if (!str.equals("--user")) {
                        b = -1;
                    }
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    dumpUser(pw, android.os.UserHandle.parseUserArg(args[1]), sb, now, nowRealtime);
                    return;
                case 1:
                    this.mUserVisibilityMediator.dump(pw, args);
                    return;
            }
        }
        int currentUserId = getCurrentUserId();
        pw.print("Current user: ");
        if (currentUserId != -10000) {
            pw.println(currentUserId);
        } else {
            pw.println("N/A");
        }
        pw.println();
        synchronized (this.mPackagesLock) {
            synchronized (this.mUsersLock) {
                pw.println("Users:");
                int i2 = 0;
                while (i2 < this.mUsers.size()) {
                    com.android.server.pm.UserManagerService.UserData userData = this.mUsers.valueAt(i2);
                    if (userData == null) {
                        i = i2;
                    } else {
                        i = i2;
                        dumpUserLocked(pw, userData, sb, now, nowRealtime);
                    }
                    i2 = i + 1;
                }
            }
            pw.println();
            pw.println("Device properties:");
            pw.println("  Device policy global restrictions:");
            synchronized (this.mRestrictionsLock) {
                com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, "    ", this.mDevicePolicyUserRestrictions.getRestrictions(-1));
            }
            pw.println("  Guest restrictions:");
            synchronized (this.mGuestRestrictions) {
                com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, "    ", this.mGuestRestrictions);
            }
            synchronized (this.mUsersLock) {
                pw.println();
                pw.println("  Device managed: " + this.mIsDeviceManaged);
                if (this.mRemovingUserIds.size() > 0) {
                    pw.println();
                    pw.println("  Recently removed userIds: " + this.mRecentlyRemovedIds);
                }
            }
            synchronized (this.mUserStates) {
                pw.print("  Started users state: [");
                int size = this.mUserStates.states.size();
                for (int i3 = 0; i3 < size; i3++) {
                    int userId = this.mUserStates.states.keyAt(i3);
                    int state = this.mUserStates.states.valueAt(i3);
                    pw.print(userId);
                    pw.print('=');
                    pw.print(com.android.server.am.UserState.stateToString(state));
                    if (i3 != size - 1) {
                        pw.print(", ");
                    }
                }
                pw.println(']');
            }
            synchronized (this.mUsersLock) {
                pw.print("  Cached user IDs: ");
                pw.println(java.util.Arrays.toString(this.mUserIds));
                pw.print("  Cached user IDs (including pre-created): ");
                pw.println(java.util.Arrays.toString(this.mUserIdsIncludingPreCreated));
            }
        }
        pw.println();
        this.mUserVisibilityMediator.dump(pw, args);
        pw.println();
        pw.println();
        pw.print("  Max users: " + android.os.UserManager.getMaxSupportedUsers());
        pw.println(" (limit reached: " + isUserLimitReached() + ")");
        pw.println("  Supports switchable users: " + android.os.UserManager.supportsMultipleUsers());
        pw.println("  All guests ephemeral: " + android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_expandLockScreenUserSwitcher));
        pw.println("  Force ephemeral users: " + this.mForceEphemeralUsers);
        boolean isHeadlessSystemUserMode = isHeadlessSystemUserMode();
        pw.println("  Is headless-system mode: " + isHeadlessSystemUserMode);
        if (isHeadlessSystemUserMode != com.android.internal.os.RoSystemProperties.MULTIUSER_HEADLESS_SYSTEM_USER) {
            pw.println("  (differs from the current default build value)");
        }
        if (!android.text.TextUtils.isEmpty(android.os.SystemProperties.get("persist.debug.user_mode_emulation"))) {
            pw.println("  (emulated by 'cmd user set-system-user-mode-emulation')");
            if (this.mUpdatingSystemUserMode) {
                pw.println("  (and being updated after boot)");
            }
        }
        pw.println("  User version: " + this.mUserVersion);
        pw.println("  Owner name: " + getOwnerName());
        synchronized (this.mUsersLock) {
            pw.println("  Boot user: " + this.mBootUser);
        }
        pw.println("Can add private profile: " + canAddPrivateProfile(currentUserId));
        pw.println();
        pw.println("Number of listeners for");
        synchronized (this.mUserRestrictionsListeners) {
            pw.println("  restrictions: " + this.mUserRestrictionsListeners.size());
        }
        synchronized (this.mUserLifecycleListeners) {
            pw.println("  user lifecycle events: " + this.mUserLifecycleListeners.size());
        }
        pw.println();
        pw.println("User types version: " + this.mUserTypeVersion);
        pw.println("User types (" + this.mUserTypes.size() + " types):");
        for (int i4 = 0; i4 < this.mUserTypes.size(); i4++) {
            pw.println("    " + this.mUserTypes.keyAt(i4) + ": ");
            this.mUserTypes.valueAt(i4).dump(pw, "        ");
        }
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
        try {
            ipw.println();
            this.mSystemPackageInstaller.dump(ipw);
            ipw.close();
        } finally {
        }
    }

    private void dumpUser(java.io.PrintWriter pw, int userId, java.lang.StringBuilder sb, long now, long nowRealtime) {
        int userId2;
        if (userId != -2) {
            userId2 = userId;
        } else {
            int currentUserId = getCurrentUserId();
            pw.print("Current user: ");
            if (currentUserId == -10000) {
                pw.println("Cannot determine current user");
                return;
            }
            userId2 = currentUserId;
        }
        synchronized (this.mUsersLock) {
            com.android.server.pm.UserManagerService.UserData userData = this.mUsers.get(userId2);
            if (userData == null) {
                pw.println("User " + userId2 + " not found");
            } else {
                dumpUserLocked(pw, userData, sb, now, nowRealtime);
            }
        }
    }

    private void dumpUserLocked(java.io.PrintWriter pw, com.android.server.pm.UserManagerService.UserData userData, java.lang.StringBuilder tempStringBuilder, long now, long nowRealtime) {
        int state;
        android.content.pm.UserInfo userInfo = userData.info;
        int userId = userInfo.id;
        pw.print("  ");
        pw.print(userInfo);
        pw.print(" serialNo=");
        pw.print(userInfo.serialNumber);
        pw.print(" isPrimary=");
        pw.print(userInfo.isPrimary());
        if (userInfo.profileGroupId != userInfo.id && userInfo.profileGroupId != -10000) {
            pw.print(" parentId=");
            pw.print(userInfo.profileGroupId);
        }
        if (this.mRemovingUserIds.get(userId)) {
            pw.print(" <removing> ");
        }
        if (userInfo.partial) {
            pw.print(" <partial>");
        }
        if (userInfo.preCreated) {
            pw.print(" <pre-created>");
        }
        if (userInfo.convertedFromPreCreated) {
            pw.print(" <converted>");
        }
        pw.println();
        pw.print("    Type: ");
        pw.println(userInfo.userType);
        pw.print("    Flags: ");
        pw.print(userInfo.flags);
        pw.print(" (");
        pw.print(android.content.pm.UserInfo.flagsToString(userInfo.flags));
        pw.println(")");
        pw.print("    State: ");
        synchronized (this.mUserStates) {
            state = this.mUserStates.get(userId, -1);
        }
        pw.println(com.android.server.am.UserState.stateToString(state));
        pw.print("    Created: ");
        dumpTimeAgo(pw, tempStringBuilder, now, userInfo.creationTime);
        pw.print("    Last logged in: ");
        dumpTimeAgo(pw, tempStringBuilder, now, userInfo.lastLoggedInTime);
        pw.print("    Last logged in fingerprint: ");
        pw.println(userInfo.lastLoggedInFingerprint);
        pw.print("    Start time: ");
        dumpTimeAgo(pw, tempStringBuilder, nowRealtime, userData.startRealtime);
        pw.print("    Unlock time: ");
        dumpTimeAgo(pw, tempStringBuilder, nowRealtime, userData.unlockRealtime);
        pw.print("    Last entered foreground: ");
        dumpTimeAgo(pw, tempStringBuilder, now, userData.mLastEnteredForegroundTimeMillis);
        pw.print("    Has profile owner: ");
        pw.println(this.mIsUserManaged.get(userId));
        pw.println("    Restrictions:");
        synchronized (this.mRestrictionsLock) {
            com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, "      ", this.mBaseUserRestrictions.getRestrictions(userInfo.id));
            pw.println("    Device policy restrictions:");
            com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, "      ", this.mDevicePolicyUserRestrictions.getRestrictions(userInfo.id));
            pw.println("    Effective restrictions:");
            com.android.server.pm.UserRestrictionsUtils.dumpRestrictions(pw, "      ", this.mCachedEffectiveUserRestrictions.getRestrictions(userInfo.id));
        }
        if (userData.account != null) {
            pw.print("    Account name: " + userData.account);
            pw.println();
        }
        if (userData.seedAccountName != null) {
            pw.print("    Seed account name: " + userData.seedAccountName);
            pw.println();
            if (userData.seedAccountType != null) {
                pw.print("         account type: " + userData.seedAccountType);
                pw.println();
            }
            if (userData.seedAccountOptions != null) {
                pw.print("         account options exist");
                pw.println();
            }
        }
        if (userData.userProperties != null) {
            userData.userProperties.println(pw, "    ");
        }
        pw.println("    Ignore errors preparing storage: " + userData.getIgnorePrepareStorageErrors());
    }

    private static void dumpTimeAgo(java.io.PrintWriter pw, java.lang.StringBuilder sb, long nowTime, long time) {
        if (time == 0) {
            pw.println("<unknown>");
            return;
        }
        sb.setLength(0);
        android.util.TimeUtils.formatDuration(nowTime - time, sb);
        sb.append(" ago");
        pw.println(sb);
    }

    final class MainHandler extends android.os.Handler {
        MainHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    removeMessages(1, msg.obj);
                    synchronized (com.android.server.pm.UserManagerService.this.mPackagesLock) {
                        int userId = ((java.lang.Integer) msg.obj).intValue();
                        com.android.server.pm.UserManagerService.UserData userData = com.android.server.pm.UserManagerService.this.getUserDataNoChecks(userId);
                        if (userData != null) {
                            com.android.server.pm.UserManagerService.this.writeUserLP(userData);
                        } else {
                            android.util.Slog.i(com.android.server.pm.UserManagerService.LOG_TAG, "handle(WRITE_USER_MSG): no data for user " + userId + ", it was probably removed before handler could handle it");
                        }
                        break;
                    }
                    return;
                case 2:
                    removeMessages(2);
                    synchronized (com.android.server.pm.UserManagerService.this.mPackagesLock) {
                        com.android.server.pm.UserManagerService.this.writeUserListLP();
                        break;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    boolean isUserInitialized(int userId) {
        return this.mLocalService.isUserInitialized(userId);
    }

    private class LocalService extends com.android.server.pm.UserManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setDevicePolicyUserRestrictions(int originatingUserId, android.os.Bundle global, com.android.server.pm.RestrictionsSet local, boolean isDeviceOwner) {
            com.android.server.pm.UserManagerService.this.setDevicePolicyUserRestrictionsInner(originatingUserId, global, local, isDeviceOwner);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserRestriction(int userId, java.lang.String key, boolean value) {
            com.android.server.pm.UserManagerService.this.setUserRestrictionInner(userId, key, value);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean getUserRestriction(int userId, java.lang.String key) {
            return com.android.server.pm.UserManagerService.this.getUserRestrictions(userId).getBoolean(key);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void addUserRestrictionsListener(com.android.server.pm.UserManagerInternal.UserRestrictionsListener listener) {
            synchronized (com.android.server.pm.UserManagerService.this.mUserRestrictionsListeners) {
                com.android.server.pm.UserManagerService.this.mUserRestrictionsListeners.add(listener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserRestrictionsListener(com.android.server.pm.UserManagerInternal.UserRestrictionsListener listener) {
            synchronized (com.android.server.pm.UserManagerService.this.mUserRestrictionsListeners) {
                com.android.server.pm.UserManagerService.this.mUserRestrictionsListeners.remove(listener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void addUserLifecycleListener(com.android.server.pm.UserManagerInternal.UserLifecycleListener listener) {
            synchronized (com.android.server.pm.UserManagerService.this.mUserLifecycleListeners) {
                com.android.server.pm.UserManagerService.this.mUserLifecycleListeners.add(listener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserLifecycleListener(com.android.server.pm.UserManagerInternal.UserLifecycleListener listener) {
            synchronized (com.android.server.pm.UserManagerService.this.mUserLifecycleListeners) {
                com.android.server.pm.UserManagerService.this.mUserLifecycleListeners.remove(listener);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setDeviceManaged(boolean isManaged) {
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                com.android.server.pm.UserManagerService.this.mIsDeviceManaged = isManaged;
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isDeviceManaged() {
            boolean z;
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                z = com.android.server.pm.UserManagerService.this.mIsDeviceManaged;
            }
            return z;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserManaged(int userId, boolean isManaged) {
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                com.android.server.pm.UserManagerService.this.mIsUserManaged.put(userId, isManaged);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserManaged(int userId) {
            boolean z;
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                z = com.android.server.pm.UserManagerService.this.mIsUserManaged.get(userId);
            }
            return z;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserIcon(int userId, android.graphics.Bitmap bitmap) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.pm.UserManagerService.this.mPackagesLock) {
                    com.android.server.pm.UserManagerService.UserData userData = com.android.server.pm.UserManagerService.this.getUserDataNoChecks(userId);
                    if (userData != null && !userData.info.partial) {
                        com.android.server.pm.UserManagerService.this.writeBitmapLP(userData.info, bitmap);
                        com.android.server.pm.UserManagerService.this.writeUserLP(userData);
                        com.android.server.pm.UserManagerService.this.sendUserInfoChangedBroadcast(userId);
                        return;
                    }
                    android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, "setUserIcon: unknown user #" + userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setForceEphemeralUsers(boolean forceEphemeralUsers) {
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                com.android.server.pm.UserManagerService.this.mForceEphemeralUsers = forceEphemeralUsers;
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeAllUsers() {
            if (com.android.server.pm.UserManagerService.this.getCurrentUserId() == 0) {
                com.android.server.pm.UserManagerService.this.removeAllUsersExceptSystemAndPermanentAdminMain();
                return;
            }
            android.content.BroadcastReceiver userSwitchedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.UserManagerService.LocalService.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    if (userId != 0) {
                        return;
                    }
                    com.android.server.pm.UserManagerService.this.mContext.unregisterReceiver(this);
                    com.android.server.pm.UserManagerService.this.removeAllUsersExceptSystemAndPermanentAdminMain();
                }
            };
            android.content.IntentFilter userSwitchedFilter = new android.content.IntentFilter();
            userSwitchedFilter.addAction("android.intent.action.USER_SWITCHED");
            com.android.server.pm.UserManagerService.this.mContext.registerReceiver(userSwitchedReceiver, userSwitchedFilter, null, com.android.server.pm.UserManagerService.this.mHandler);
            android.app.ActivityManager am = (android.app.ActivityManager) com.android.server.pm.UserManagerService.this.mContext.getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
            am.switchUser(0);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void onEphemeralUserStop(int userId) {
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                android.content.pm.UserInfo userInfo = com.android.server.pm.UserManagerService.this.getUserInfoLU(userId);
                if (userInfo != null && userInfo.isEphemeral()) {
                    userInfo.flags |= 64;
                    if (userInfo.isGuest()) {
                        userInfo.guestToRemove = true;
                    }
                }
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public android.content.pm.UserInfo createUserEvenWhenDisallowed(java.lang.String name, java.lang.String userType, int flags, java.lang.String[] disallowedPackages, java.lang.Object token) throws android.os.UserManager.CheckedUserOperationException {
            return com.android.server.pm.UserManagerService.this.createUserInternalUnchecked(name, userType, flags, -10000, false, disallowedPackages, token);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean removeUserEvenWhenDisallowed(int userId) {
            return com.android.server.pm.UserManagerService.this.removeUserWithProfilesUnchecked(userId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserRunning(int userId) {
            int state;
            synchronized (com.android.server.pm.UserManagerService.this.mUserStates) {
                state = com.android.server.pm.UserManagerService.this.mUserStates.get(userId, -1);
            }
            return (state == -1 || state == 4 || state == 5) ? false : true;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setUserState(int userId, int userState) {
            synchronized (com.android.server.pm.UserManagerService.this.mUserStates) {
                com.android.server.pm.UserManagerService.this.mUserStates.put(userId, userState);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserState(int userId) {
            synchronized (com.android.server.pm.UserManagerService.this.mUserStates) {
                com.android.server.pm.UserManagerService.this.mUserStates.delete(userId);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getUserIds() {
            return com.android.server.pm.UserManagerService.this.getUserIds();
        }

        @Override // com.android.server.pm.UserManagerInternal
        public java.util.List<android.content.pm.UserInfo> getUsers(boolean excludeDying) {
            return getUsers(true, excludeDying, true);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public java.util.List<android.content.pm.UserInfo> getUsers(boolean excludePartial, boolean excludeDying, boolean excludePreCreated) {
            return com.android.server.pm.UserManagerService.this.getUsersInternal(excludePartial, excludeDying, excludePreCreated);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getProfileIds(int userId, boolean enabledOnly) {
            int[] array;
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                array = com.android.server.pm.UserManagerService.this.getProfileIdsLU(userId, null, enabledOnly, false).toArray();
            }
            return array;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public android.content.pm.LauncherUserInfo getLauncherUserInfo(int userId) {
            android.content.pm.UserInfo userInfo;
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                userInfo = com.android.server.pm.UserManagerService.this.getUserInfoLU(userId);
            }
            if (userInfo != null) {
                com.android.server.pm.UserTypeDetails userDetails = com.android.server.pm.UserManagerService.this.getUserTypeDetails(userInfo);
                android.content.pm.LauncherUserInfo uiInfo = new android.content.pm.LauncherUserInfo.Builder(userDetails.getName(), userInfo.serialNumber).build();
                return uiInfo;
            }
            return null;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserUnlockingOrUnlocked(int userId) {
            int state;
            synchronized (com.android.server.pm.UserManagerService.this.mUserStates) {
                state = com.android.server.pm.UserManagerService.this.mUserStates.get(userId, -1);
            }
            if (state == 4 || state == 5) {
                return android.os.storage.StorageManager.isCeStorageUnlocked(userId);
            }
            return state == 2 || state == 3;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserUnlocked(int userId) {
            int state;
            synchronized (com.android.server.pm.UserManagerService.this.mUserStates) {
                state = com.android.server.pm.UserManagerService.this.mUserStates.get(userId, -1);
            }
            if (state == 4 || state == 5) {
                return android.os.storage.StorageManager.isCeStorageUnlocked(userId);
            }
            return state == 3;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserInitialized(int userId) {
            android.content.pm.UserInfo userInfo = getUserInfo(userId);
            return (userInfo == null || (userInfo.flags & 16) == 0) ? false : true;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean exists(int userId) {
            return com.android.server.pm.UserManagerService.this.getUserInfoNoChecks(userId) != null;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isProfileAccessible(int callingUserId, int targetUserId, java.lang.String debugMsg, boolean throwSecurityException) {
            if (targetUserId == callingUserId) {
                return true;
            }
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                android.content.pm.UserInfo callingUserInfo = com.android.server.pm.UserManagerService.this.getUserInfoLU(callingUserId);
                if (callingUserInfo != null && !callingUserInfo.isProfile()) {
                    android.content.pm.UserInfo targetUserInfo = com.android.server.pm.UserManagerService.this.getUserInfoLU(targetUserId);
                    if (targetUserInfo != null && targetUserInfo.isEnabled()) {
                        if (targetUserInfo.profileGroupId != -10000 && targetUserInfo.profileGroupId == callingUserInfo.profileGroupId) {
                            return true;
                        }
                        if (!throwSecurityException) {
                            return false;
                        }
                        throw new java.lang.SecurityException(debugMsg + " for unrelated profile " + targetUserId);
                    }
                    if (throwSecurityException) {
                        android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, debugMsg + " for disabled profile " + targetUserId + " from " + callingUserId);
                    }
                    return false;
                }
                if (throwSecurityException) {
                    throw new java.lang.SecurityException(debugMsg + " for another profile " + targetUserId + " from " + callingUserId);
                }
                android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, debugMsg + " for another profile " + targetUserId + " from " + callingUserId);
                return false;
            }
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getProfileParentId(int userId) {
            return com.android.server.pm.UserManagerService.this.getProfileParentIdUnchecked(userId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isSettingRestrictedForUser(java.lang.String setting, int userId, java.lang.String value, int callingUid) {
            return com.android.server.pm.UserManagerService.this.isSettingRestrictedForUser(setting, userId, value, callingUid);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean hasUserRestriction(java.lang.String restrictionKey, int userId) {
            android.os.Bundle restrictions;
            return com.android.server.pm.UserRestrictionsUtils.isValidRestriction(restrictionKey) && (restrictions = com.android.server.pm.UserManagerService.this.getEffectiveUserRestrictions(userId)) != null && restrictions.getBoolean(restrictionKey);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public android.content.pm.UserInfo getUserInfo(int userId) {
            com.android.server.pm.UserManagerService.UserData userData;
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                userData = (com.android.server.pm.UserManagerService.UserData) com.android.server.pm.UserManagerService.this.mUsers.get(userId);
            }
            if (userData == null) {
                return null;
            }
            return userData.info;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public android.content.pm.UserInfo[] getUserInfos() {
            android.content.pm.UserInfo[] allInfos;
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                int userSize = com.android.server.pm.UserManagerService.this.mUsers.size();
                allInfos = new android.content.pm.UserInfo[userSize];
                for (int i = 0; i < userSize; i++) {
                    allInfos[i] = ((com.android.server.pm.UserManagerService.UserData) com.android.server.pm.UserManagerService.this.mUsers.valueAt(i)).info;
                }
            }
            return allInfos;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void setDefaultCrossProfileIntentFilters(int parentUserId, int profileUserId) {
            com.android.server.pm.UserTypeDetails userTypeDetails = com.android.server.pm.UserManagerService.this.getUserTypeDetailsNoChecks(profileUserId);
            android.os.Bundle restrictions = com.android.server.pm.UserManagerService.this.getEffectiveUserRestrictions(profileUserId);
            com.android.server.pm.UserManagerService.this.setDefaultCrossProfileIntentFilters(profileUserId, userTypeDetails, restrictions, parentUserId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean shouldIgnorePrepareStorageErrors(int userId) {
            boolean z;
            synchronized (com.android.server.pm.UserManagerService.this.mUsersLock) {
                com.android.server.pm.UserManagerService.UserData userData = (com.android.server.pm.UserManagerService.UserData) com.android.server.pm.UserManagerService.this.mUsers.get(userId);
                z = userData != null && userData.getIgnorePrepareStorageErrors();
            }
            return z;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public android.content.pm.UserProperties getUserProperties(int userId) {
            android.content.pm.UserProperties props = com.android.server.pm.UserManagerService.this.getUserPropertiesInternal(userId);
            if (props == null) {
                android.util.Slog.w(com.android.server.pm.UserManagerService.LOG_TAG, "A null UserProperties was returned for user " + userId);
            }
            return props;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int assignUserToDisplayOnStart(int userId, int profileGroupId, int userStartMode, int displayId) {
            android.content.pm.UserProperties properties = getUserProperties(userId);
            boolean isAlwaysVisible = properties != null && properties.getAlwaysVisible();
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.assignUserToDisplayOnStart(userId, profileGroupId, userStartMode, displayId, isAlwaysVisible);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean assignUserToExtraDisplay(int userId, int displayId) {
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.assignUserToExtraDisplay(userId, displayId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean unassignUserFromExtraDisplay(int userId, int displayId) {
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.unassignUserFromExtraDisplay(userId, displayId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void unassignUserFromDisplayOnStop(int userId) {
            com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.unassignUserFromDisplayOnStop(userId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserVisible(int userId) {
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.isUserVisible(userId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public boolean isUserVisible(int userId, int displayId) {
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.isUserVisible(userId, displayId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getMainDisplayAssignedToUser(int userId) {
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.getMainDisplayAssignedToUser(userId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getDisplaysAssignedToUser(int userId) {
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.getDisplaysAssignedToUser(userId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getUserAssignedToDisplay(int displayId) {
            return com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.getUserAssignedToDisplay(displayId);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void addUserVisibilityListener(com.android.server.pm.UserManagerInternal.UserVisibilityListener listener) {
            com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.addListener(listener);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void removeUserVisibilityListener(com.android.server.pm.UserManagerInternal.UserVisibilityListener listener) {
            com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.removeListener(listener);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public void onSystemUserVisibilityChanged(boolean visible) {
            com.android.server.pm.UserManagerService.this.mUserVisibilityMediator.onSystemUserVisibilityChanged(visible);
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int[] getUserTypesForStatsd(int[] userIds) {
            if (userIds == null) {
                return null;
            }
            int[] userTypes = new int[userIds.length];
            for (int i = 0; i < userTypes.length; i++) {
                android.content.pm.UserInfo userInfo = getUserInfo(userIds[i]);
                if (userInfo == null) {
                    com.android.server.pm.UserJourneyLogger unused = com.android.server.pm.UserManagerService.this.mUserJourneyLogger;
                    userTypes[i] = com.android.server.pm.UserJourneyLogger.getUserTypeForStatsd("");
                } else {
                    com.android.server.pm.UserJourneyLogger unused2 = com.android.server.pm.UserManagerService.this.mUserJourneyLogger;
                    userTypes[i] = com.android.server.pm.UserJourneyLogger.getUserTypeForStatsd(userInfo.userType);
                }
            }
            return userTypes;
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getMainUserId() {
            return com.android.server.pm.UserManagerService.this.getMainUserIdUnchecked();
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getBootUser(boolean waitUntilSet) throws android.os.UserManager.CheckedUserOperationException {
            if (waitUntilSet) {
                com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
                t.traceBegin("wait-boot-user");
                try {
                    if (com.android.server.pm.UserManagerService.this.mBootUserLatch.getCount() != 0) {
                        com.android.server.utils.Slogf.d(com.android.server.pm.UserManagerService.LOG_TAG, "Sleeping for boot user to be set. Max sleep for Time: %d", 300000L);
                    }
                    if (!com.android.server.pm.UserManagerService.this.mBootUserLatch.await(300000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                        com.android.server.utils.Slogf.w(com.android.server.pm.UserManagerService.LOG_TAG, "Boot user not set. Timeout: %d", 300000L);
                    }
                } catch (java.lang.InterruptedException e) {
                    java.lang.Thread.currentThread().interrupt();
                    com.android.server.utils.Slogf.w(com.android.server.pm.UserManagerService.LOG_TAG, e, "InterruptedException during wait for boot user.", new java.lang.Object[0]);
                }
                t.traceEnd();
            }
            return com.android.server.pm.UserManagerService.this.getBootUserUnchecked();
        }

        @Override // com.android.server.pm.UserManagerInternal
        public int getCommunalProfileId() {
            return com.android.server.pm.UserManagerService.this.getCommunalProfileIdUnchecked();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    private void enforceUserRestriction(java.lang.String restriction, int userId, java.lang.String message) throws android.os.UserManager.CheckedUserOperationException {
        if (hasUserRestriction(restriction, userId)) {
            java.lang.String errorMessage = (message != null ? message + ": " : "") + restriction + " is enabled.";
            android.util.Slog.w(LOG_TAG, errorMessage);
            throw new android.os.UserManager.CheckedUserOperationException(errorMessage, 1);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.UserManager$CheckedUserOperationException */
    private void throwCheckedUserOperationException(java.lang.String message, int userOperationResult) throws android.os.UserManager.CheckedUserOperationException {
        android.util.Slog.e(LOG_TAG, message);
        throw new android.os.UserManager.CheckedUserOperationException(message, userOperationResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllUsersExceptSystemAndPermanentAdminMain() {
        java.util.ArrayList<android.content.pm.UserInfo> usersToRemove = new java.util.ArrayList<>();
        synchronized (this.mUsersLock) {
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo ui = this.mUsers.valueAt(i).info;
                if (ui.id != 0 && !isNonRemovableMainUser(ui)) {
                    usersToRemove.add(ui);
                }
            }
        }
        java.util.Iterator<android.content.pm.UserInfo> it = usersToRemove.iterator();
        while (it.hasNext()) {
            removeUser(it.next().id);
        }
    }

    private static void debug(java.lang.String message) {
        android.util.Slog.d(LOG_TAG, message + "");
    }

    int getMaxUsersOfTypePerParent(java.lang.String userType) {
        com.android.server.pm.UserTypeDetails type = this.mUserTypes.get(userType);
        if (type == null) {
            return 0;
        }
        return getMaxUsersOfTypePerParent(type);
    }

    private static int getMaxUsersOfTypePerParent(com.android.server.pm.UserTypeDetails userTypeDetails) {
        int defaultMax = userTypeDetails.getMaxAllowedPerParent();
        if (android.os.Build.IS_DEBUGGABLE && userTypeDetails.isManagedProfile()) {
            return android.os.SystemProperties.getInt("persist.sys.max_profiles", defaultMax);
        }
        return defaultMax;
    }

    int getFreeProfileBadgeLU(int parentUserId, java.lang.String userType) {
        java.util.Set<java.lang.Integer> usedBadges = new android.util.ArraySet<>();
        int userSize = this.mUsers.size();
        for (int i = 0; i < userSize; i++) {
            android.content.pm.UserInfo ui = this.mUsers.valueAt(i).info;
            if (ui.userType.equals(userType) && ui.profileGroupId == parentUserId && !this.mRemovingUserIds.get(ui.id)) {
                usedBadges.add(java.lang.Integer.valueOf(ui.profileBadge));
            }
        }
        int maxUsersOfType = getMaxUsersOfTypePerParent(userType);
        if (maxUsersOfType == -1) {
            maxUsersOfType = Integer.MAX_VALUE;
        }
        for (int i2 = 0; i2 < maxUsersOfType; i2++) {
            if (!usedBadges.contains(java.lang.Integer.valueOf(i2))) {
                return i2;
            }
        }
        return 0;
    }

    boolean hasProfile(int userId) {
        synchronized (this.mUsersLock) {
            android.content.pm.UserInfo userInfo = getUserInfoLU(userId);
            int userSize = this.mUsers.size();
            for (int i = 0; i < userSize; i++) {
                android.content.pm.UserInfo profile = this.mUsers.valueAt(i).info;
                if (userId != profile.id && isProfileOf(userInfo, profile)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void verifyCallingPackage(java.lang.String callingPackage, int callingUid) {
        int packageUid = this.mPm.snapshotComputer().getPackageUid(callingPackage, 0L, android.os.UserHandle.getUserId(callingUid));
        if (packageUid != callingUid) {
            throw new java.lang.SecurityException("Specified package " + callingPackage + " does not match the calling uid " + callingUid);
        }
    }

    private android.content.pm.PackageManagerInternal getPackageManagerInternal() {
        if (this.mPmInternal == null) {
            this.mPmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }
        return this.mPmInternal;
    }

    private android.app.admin.DevicePolicyManagerInternal getDevicePolicyManagerInternal() {
        if (this.mDevicePolicyManagerInternal == null) {
            this.mDevicePolicyManagerInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        }
        return this.mDevicePolicyManagerInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.ActivityManagerInternal getActivityManagerInternal() {
        if (this.mAmInternal == null) {
            this.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        }
        return this.mAmInternal;
    }

    private boolean isNonRemovableMainUser(android.content.pm.UserInfo userInfo) {
        return userInfo.isMain() && isMainUserPermanentAdmin();
    }

    public boolean isMainUserPermanentAdmin() {
        return android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_guestUserEphemeral);
    }

    public boolean canSwitchToHeadlessSystemUser() {
        return android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_carDockEnablesAccelerometer);
    }

    public com.android.server.pm.IUserManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    private class UserManagerServiceWrapper implements com.android.server.pm.IUserManagerServiceWrapper {
        private UserManagerServiceWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.pm.IUserManagerServiceExt getExtImpl() {
            return com.android.server.pm.UserManagerService.this.mUserManagerServiceExt;
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public android.content.Context getContext() {
            return com.android.server.pm.UserManagerService.this.mContext;
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public void setUserRestriction(java.lang.String key, boolean value, int userId) {
            com.android.server.pm.UserManagerService.this.setUserRestriction(key, value, userId);
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public void writeUserLP(com.android.server.pm.UserManagerService.UserData userData) {
            com.android.server.pm.UserManagerService.this.writeUserLP(userData);
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public void writeUserListLP() {
            com.android.server.pm.UserManagerService.this.writeUserListLP();
        }

        @Override // com.android.server.pm.IUserManagerServiceWrapper
        public java.lang.Object getUsersLock() {
            return com.android.server.pm.UserManagerService.this.mUsersLock;
        }
    }

    public com.android.server.pm.UserJourneyLogger getUserJourneyLogger() {
        return this.mUserJourneyLogger;
    }
}
