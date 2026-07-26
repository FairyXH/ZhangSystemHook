package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ActivityManagerService extends android.app.IActivityManager.Stub implements com.android.server.Watchdog.Monitor, com.android.server.power.stats.BatteryStatsImpl.BatteryCallback, com.android.server.am.ActivityManagerGlobalLock {
    static final int ABORT_DUMPHEAP_MSG = 51;
    public static final java.lang.String ACTION_TRIGGER_IDLE = "com.android.server.ACTION_TRIGGER_IDLE";
    static final int ADD_UID_TO_OBSERVER_MSG = 80;
    static final int BINDER_HEAVYHITTER_AUTOSAMPLER_TIMEOUT_MSG = 72;
    private static final long BINDER_HEAVY_HITTER_AUTO_SAMPLER_DURATION_MS = 300000;
    private static final long BINDER_HEAVY_HITTER_AUTO_SAMPLER_THROTTLE_MS = 3600000;
    private static final int BINDER_PROXY_HIGH_WATERMARK = 6000;
    private static final int BINDER_PROXY_LOW_WATERMARK = 5500;
    private static final int BINDER_PROXY_WARNING_WATERMARK = 5750;
    static final int BIND_APPLICATION_TIMEOUT_HARD_MSG = 83;
    static final int BIND_APPLICATION_TIMEOUT_SOFT_MSG = 82;
    static final int CHECK_EXCESSIVE_POWER_USE_MSG = 27;
    static final int CLEAR_DNS_CACHE_MSG = 28;
    static final int CONTENT_PROVIDER_PUBLISH_TIMEOUT_MSG = 57;
    public static final java.lang.String DATA_FILE_PATH_FOOTER = "End Data File\n";
    public static final java.lang.String DATA_FILE_PATH_HEADER = "Data File: ";
    static final int DISPATCH_BINDING_SERVICE_EVENT = 75;
    static final int DISPATCH_OOM_ADJ_OBSERVER_MSG = 70;
    static final int DISPATCH_PROCESSES_CHANGED_UI_MSG = 31;
    static final int DISPATCH_PROCESS_DIED_UI_MSG = 32;
    static final int DISPATCH_SENDING_BROADCAST_EVENT = 74;
    static final int DROPBOX_DEFAULT_MAX_SIZE = 196608;
    private static final long DYNAMIC_RECEIVER_EXPLICIT_EXPORT_REQUIRED = 161145287;
    private static final boolean ENABLE_PROC_LOCK = true;
    static final java.lang.String EXTRA_BUGREPORT_NONCE = "android.intent.extra.BUGREPORT_NONCE";
    static final java.lang.String EXTRA_BUGREPORT_TYPE = "android.intent.extra.BUGREPORT_TYPE";
    static final java.lang.String EXTRA_DESCRIPTION = "android.intent.extra.DESCRIPTION";
    static final java.lang.String EXTRA_EXTRA_ATTACHMENT_URI = "android.intent.extra.EXTRA_ATTACHMENT_URI";
    static final java.lang.String EXTRA_TITLE = "android.intent.extra.TITLE";
    static final int FIRST_BROADCAST_QUEUE_MSG = 200;
    static final int FOLLOW_UP_OOMADJUSTER_UPDATE_MSG = 86;
    static final int GC_BACKGROUND_PROCESSES_MSG = 5;
    static final int HANDLE_TRUST_STORAGE_UPDATE_MSG = 63;
    private static final long HOME_LAUNCH_TIMEOUT_MS = 15000;
    static final int IDLE_UIDS_MSG = 58;
    private static final int INDEX_DALVIK_PRIVATE_DIRTY = 7;
    private static final int INDEX_DALVIK_PSS = 4;
    private static final int INDEX_DALVIK_RSS = 6;
    private static final int INDEX_DALVIK_SWAP_PSS = 5;
    private static final int INDEX_LAST = 19;
    private static final int INDEX_NATIVE_PRIVATE_DIRTY = 3;
    private static final int INDEX_NATIVE_PSS = 0;
    private static final int INDEX_NATIVE_RSS = 2;
    private static final int INDEX_NATIVE_SWAP_PSS = 1;
    private static final int INDEX_OTHER_PRIVATE_DIRTY = 11;
    private static final int INDEX_OTHER_PSS = 8;
    private static final int INDEX_OTHER_RSS = 10;
    private static final int INDEX_OTHER_SWAP_PSS = 9;
    private static final int INDEX_TOTAL_MEMTRACK_GL = 18;
    private static final int INDEX_TOTAL_MEMTRACK_GRAPHICS = 17;
    private static final int INDEX_TOTAL_NATIVE_PSS = 16;
    private static final int INDEX_TOTAL_PRIVATE_DIRTY = 15;
    private static final int INDEX_TOTAL_PSS = 12;
    private static final int INDEX_TOTAL_RSS = 14;
    private static final int INDEX_TOTAL_SWAP_PSS = 13;
    private static final java.lang.String INTENT_BUGREPORT_REQUESTED = "com.android.internal.intent.action.BUGREPORT_REQUESTED";
    private static final java.lang.String INTENT_REMOTE_BUGREPORT_FINISHED = "com.android.internal.intent.action.REMOTE_BUGREPORT_FINISHED";
    static final int KILL_APPLICATION_MSG = 22;
    static final int KILL_APP_ZYGOTE_DELAY_MS = 5000;
    static final int KILL_APP_ZYGOTE_MSG = 71;
    static final int KSM_SHARED = 0;
    static final int KSM_SHARING = 1;
    static final int KSM_UNSHARED = 2;
    static final int KSM_VOLATILE = 3;
    static final int LOGCAT_TIMEOUT_SEC = 10;
    private static final int MAX_BUGREPORT_DESCRIPTION_SIZE = 150;
    private static final int MAX_BUGREPORT_TITLE_SIZE = 100;
    private static final int MAX_DUP_SUPPRESSED_STACKS = 5000;
    private static final int MAX_RECEIVERS_ALLOWED_PER_APP = 1000;
    static final int MAX_STATE_DATA_SIZE = 128;
    private static final int MEMINFO_COMPACT_VERSION = 1;
    private static final int MINIMUM_MEMORY_GROWTH_THRESHOLD = 10000;
    static final int NOTIFY_CLEARTEXT_NETWORK_MSG = 49;
    static final int PERSISTENT_MASK = 9;
    static final int POST_DUMP_HEAP_NOTIFICATION_MSG = 50;
    static final int PROC_START_TIMEOUT_MSG = 20;
    static final int PROC_START_TIMEOUT_WITH_WRAPPER = 1200000;
    static final int PUSH_TEMP_ALLOWLIST_UI_MSG = 68;
    static final int REMOVE_UID_FROM_OBSERVER_MSG = 81;
    static final int REPORT_MEM_USAGE_MSG = 33;
    static final int RESERVED_BYTES_PER_LOGCAT_LINE = 100;
    static final int SERVICE_FGS_CRASH_TIMEOUT_MSG = 85;
    static final int SERVICE_FGS_TIMEOUT_MSG = 84;
    static final int SERVICE_FOREGROUND_CRASH_MSG = 69;
    static final int SERVICE_FOREGROUND_TIMEOUT_ANR_MSG = 67;
    static final int SERVICE_FOREGROUND_TIMEOUT_MSG = 66;
    static final int SERVICE_SHORT_FGS_ANR_TIMEOUT_MSG = 78;
    static final int SERVICE_SHORT_FGS_PROCSTATE_TIMEOUT_MSG = 77;
    static final int SERVICE_SHORT_FGS_TIMEOUT_MSG = 76;
    static final int SERVICE_TIMEOUT_MSG = 12;
    private static final java.lang.String SHELL_APP_PACKAGE = "com.android.shell";
    static final int SHOW_ANR_FIX_UI_MSG = 202;
    static final int SHOW_ANR_UI_MSG = 201;
    static final int SHOW_ERROR_UI_MSG = 1;
    static final int SHOW_NOT_RESPONDING_UI_MSG = 2;
    static final int SHOW_STRICT_MODE_VIOLATION_UI_MSG = 26;
    static final int SHUTDOWN_UI_AUTOMATION_CONNECTION_MSG = 56;
    public static final int STOCK_PM_FLAGS = 1024;
    private static final java.lang.String SYSTEMUI_PROCESS_NAME = "com.android.systemui";
    private static final java.lang.String SYSTEM_PROPERTY_DEVICE_PROVISIONED = "persist.sys.device_provisioned";
    static final java.lang.String SYSTEM_USER_HOME_NEEDED = "ro.system_user_home_needed";
    static final java.lang.String TAG = "ActivityManager";
    static final java.lang.String TAG_MU = "ActivityManager_MU";
    static final java.lang.String TAG_NETWORK = "ActivityManager_Network";
    private static final java.lang.String TICK = "-------------------------------------------------------------------------------";
    static final boolean TRACK_PROCSTATS_ASSOCIATIONS = true;
    static final int UPDATE_CACHED_APP_HIGH_WATERMARK = 79;
    static final int UPDATE_HTTP_PROXY_MSG = 29;
    static final int UPDATE_TIME_PREFERENCE_MSG = 41;
    static final int UPDATE_TIME_ZONE = 13;
    static final int WAIT_FOR_CONTENT_PROVIDER_TIMEOUT_MSG = 73;
    static final int WAIT_FOR_DEBUGGER_UI_MSG = 6;
    private com.android.server.am.AccessCheckDelegateHelper mAccessCheckDelegateHelper;
    final android.util.IntArray mActiveCameraUids;
    final java.util.ArrayList<com.android.server.am.ActiveInstrumentation> mActiveInstrumentation;
    private final com.android.server.wm.ActivityMetricsLaunchObserver mActivityLaunchObserver;
    public com.android.server.am.IActivityManagerServiceExt mActivityManagerServiceExt;
    public com.android.server.wm.ActivityTaskManagerService mActivityTaskManager;
    boolean mAllowSpecifiedFifoScheduling;
    private android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.PackageAssociationInfo> mAllowedAssociations;
    private final java.util.HashSet<java.lang.Integer> mAlreadyLoggedViolatedStacks;
    boolean mAlwaysFinishActivities;
    private com.android.server.am.ActivityManagerService.ActivityManagerServiceWrapper mAmsWrapper;
    final com.android.server.am.AnrHelper mAnrHelper;
    android.util.ArrayMap<java.lang.String, android.os.IBinder> mAppBindArgs;
    final com.android.server.am.AppErrors mAppErrors;
    private android.app.AppOpsManager mAppOpsManager;
    final com.android.server.appop.AppOpsService mAppOpsService;
    public com.android.server.am.AppProfiler mAppProfiler;
    final com.android.server.am.AppRestrictionController mAppRestrictionController;
    final android.util.SparseArray<android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>>> mAssociations;
    public com.android.server.wm.ActivityTaskManagerInternal mAtmInternal;
    private int[] mBackgroundAppIdAllowlist;
    private android.util.ArraySet<java.lang.String> mBackgroundLaunchBroadcasts;
    final android.util.SparseArray<com.android.server.am.BackupRecord> mBackupTargets;
    public com.android.server.power.stats.IBatteryStatsImplExt mBatteryStatsImplExt;
    public final com.android.server.am.BatteryStatsService mBatteryStatsService;
    final java.util.concurrent.CopyOnWriteArrayList<android.app.ActivityManagerInternal.BindServiceEventListener> mBindServiceEventListeners;
    private volatile boolean mBinderTransactionTrackingEnabled;
    boolean mBootAnimationComplete;
    volatile long mBootCompletedTimestamp;
    int mBootPhase;
    volatile boolean mBooted;
    volatile boolean mBooting;
    final java.util.concurrent.CopyOnWriteArrayList<android.app.ActivityManagerInternal.BroadcastEventListener> mBroadcastEventListeners;
    private com.android.server.am.BroadcastQueue mBroadcastQueue;
    boolean mCallFinishBooting;
    public int mCallingPid;
    private final java.util.Map<java.lang.Integer, java.util.Set<java.lang.Integer>> mCompanionAppUidsMap;
    final com.android.server.am.ComponentAliasResolver mComponentAliasResolver;
    com.android.server.am.ActivityManagerConstants mConstants;
    volatile com.android.server.contentcapture.ContentCaptureManagerInternal mContentCaptureService;
    final android.content.Context mContext;
    com.android.server.am.CoreSettingsObserver mCoreSettingsObserver;
    final com.android.server.am.ContentProviderHelper mCpHelper;
    com.android.server.am.BroadcastStats mCurBroadcastStats;
    com.android.server.am.ActivityManagerService.OomAdjObserver mCurOomAdjObserver;
    int mCurOomAdjUid;
    private final java.lang.Object mCurResumedAppLock;
    private java.lang.String mCurResumedPackage;
    private int mCurResumedUid;
    private java.lang.String mDebugApp;
    private boolean mDebugTransient;
    private final android.util.ArraySet<java.lang.String> mDeliveryGroupPolicyIgnoredActions;
    boolean mDeterministicUidIdle;
    int[] mDeviceIdleAllowlist;
    int[] mDeviceIdleExceptIdleAllowlist;
    int[] mDeviceIdleTempAllowlist;
    private volatile int mDeviceOwnerUid;
    private final com.android.server.am.DropboxRateLimiter mDropboxRateLimiter;
    final int mFactoryTest;
    final com.android.server.am.FgsTempAllowList<com.android.server.am.ActivityManagerService.FgsTempAllowListItem> mFgsStartTempAllowList;
    private final com.android.server.am.FgsTempAllowList<java.lang.String> mFgsWhileInUseTempAllowList;
    private boolean mForceBackgroundCheck;
    final com.android.internal.app.ProcessMap<java.util.ArrayList<com.android.server.am.ProcessRecord>> mForegroundPackages;
    final java.util.ArrayList<android.app.ActivityManagerInternal.ForegroundServiceStateListener> mForegroundServiceStateListeners;
    private final com.android.server.am.ActivityManagerService.GetBackgroundStartPrivilegesFunctor mGetBackgroundStartPrivilegesFunctor;
    final com.android.server.am.ActivityManagerGlobalLock mGlobalLock;
    final com.android.server.am.ActivityManagerService.MainHandler mHandler;
    public final com.android.server.ServiceThread mHandlerThread;
    private final java.util.concurrent.atomic.AtomicBoolean mHasHomeDelay;
    final com.android.server.am.ActivityManagerService.HiddenApiSettings mHiddenApiBlacklist;
    final android.util.SparseArray<com.android.server.am.ActivityManagerService.ImportanceToken> mImportantProcesses;
    final com.android.server.am.ActivityManagerService.Injector mInjector;
    private com.android.server.pm.Installer mInstaller;
    final com.android.server.am.InstrumentationReporter mInstrumentationReporter;
    public final com.android.server.firewall.IntentFirewall mIntentFirewall;
    public final android.app.ActivityManagerInternal mInternal;
    android.util.ArrayMap<java.lang.String, android.os.IBinder> mIsolatedAppBindArgs;
    int mKillBackgroundProcessesCallingUid;
    private long mLastBinderHeavyHitterAutoSamplerStart;
    com.android.server.am.BroadcastStats mLastBroadcastStats;
    long mLastIdleTime;
    long mLastPowerCheckUptime;
    private android.os.ParcelFileDescriptor[] mLifeMonitorFds;
    com.android.server.DeviceIdleInternal mLocalDeviceIdleController;
    android.os.PowerManagerInternal mLocalPowerManager;
    private final android.util.SparseArray<android.util.ArraySet<android.os.IBinder>> mMediaProjectionTokenMap;
    java.lang.String mNativeDebuggingApp;
    private volatile android.app.IUidObserver mNetworkPolicyUidObserver;
    volatile boolean mOnBattery;
    final java.lang.Object mOomAdjObserverLock;
    com.android.server.am.OomAdjuster mOomAdjuster;
    private java.lang.String mOrigDebugApp;
    private boolean mOrigWaitForDebugger;
    android.content.pm.PackageManagerInternal mPackageManagerInt;
    final com.android.server.PackageWatchdog mPackageWatchdog;
    public final com.android.server.am.PendingIntentController mPendingIntentController;
    private final com.android.server.am.PendingStartActivityUids mPendingStartActivityUids;
    final com.android.server.am.PendingTempAllowlists mPendingTempAllowlist;
    com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManagerInt;
    final java.util.ArrayList<com.android.server.am.ProcessRecord> mPersistentStartingProcesses;
    final com.android.server.am.PhantomProcessList mPhantomProcessList;
    final com.android.server.am.ActivityManagerService.PidMap mPidsSelfLocked;
    private final com.android.server.compat.PlatformCompat mPlatformCompat;
    private final com.android.server.utils.PriorityDump.PriorityDumper mPriorityDumper;
    private final android.util.ArraySet<java.lang.String> mPrivateSpaceBootCompletedPackages;
    final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    final com.android.server.am.ProcessList.ProcStartHandler mProcStartHandler;
    final com.android.server.ServiceThread mProcStartHandlerThread;
    public final com.android.server.am.ProcessList mProcessList;
    final com.android.server.am.ProcessStatsService mProcessStats;
    final java.util.ArrayList<com.android.server.am.ProcessRecord> mProcessesOnHold;
    volatile boolean mProcessesReady;
    private android.util.ArraySet<java.lang.Integer> mProfileOwnerUids;
    final com.android.server.IntentResolver<com.android.server.am.BroadcastFilter, com.android.server.am.BroadcastFilter> mReceiverResolver;
    final java.util.HashMap<android.os.IBinder, com.android.server.am.ReceiverList> mRegisteredReceivers;
    boolean mSafeMode;
    final com.android.server.am.ActiveServices mServices;
    private com.android.server.am.IActivityManagerServiceSocExt mSocExt;
    final java.util.ArrayList<com.android.server.am.ProcessRecord> mSpecifiedFifoProcesses;
    com.android.server.stats.pull.StatsPullAtomServiceInternal mStatsPullAtomServiceInternal;
    final android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.server.am.ActivityManagerService.StickyBroadcast>>> mStickyBroadcasts;
    private final android.util.SparseArray<android.app.IUnsafeIntentStrictModeCallback> mStrictModeCallbacks;
    private boolean mSuspendUponWait;
    volatile boolean mSystemReady;
    com.android.server.SystemServiceManager mSystemServiceManager;
    final android.app.ActivityThread mSystemThread;
    com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private android.content.pm.TestUtilityService mTestUtilityService;
    private android.os.ITheiaManagerExt mTheiaManagerExt;
    private final java.util.Set<java.lang.Integer> mThemeOverlayReadyUsers;
    com.android.server.am.TraceErrorLogger mTraceErrorLogger;
    private java.lang.String mTrackAllocationApp;
    boolean mTrackingAssociations;
    com.android.server.uri.UriGrantsManagerInternal mUgmInternal;
    final android.content.Context mUiContext;
    public final android.os.Handler mUiHandler;
    private final android.os.RemoteCallbackList<android.app.IUidFrozenStateChangedCallback> mUidFrozenStateChangedCallbackList;
    private final android.util.SparseIntArray mUidNetworkBlockedReasons;
    final com.android.server.am.UidObserverController mUidObserverController;
    volatile android.app.usage.UsageStatsManagerInternal mUsageStatsService;
    final boolean mUseFifoUiScheduling;
    final com.android.server.am.UserController mUserController;
    private volatile boolean mUserIsMonkey;
    volatile android.app.ActivityManagerInternal.VoiceInteractionManagerProvider mVoiceInteractionManagerProvider;
    private boolean mWaitForDebugger;
    java.util.concurrent.atomic.AtomicInteger mWakefulness;
    public com.android.server.wm.WindowManagerService mWindowManager;
    com.android.server.wm.WindowManagerInternal mWmInternal;
    static final java.lang.String TAG_BACKUP = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_BACKUP;
    private static final java.lang.String TAG_BROADCAST = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_BROADCAST;
    private static final java.lang.String TAG_CLEANUP = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_CLEANUP;
    private static final java.lang.String TAG_CONFIGURATION = "ActivityManager" + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_CONFIGURATION;
    private static final java.lang.String TAG_LOCKTASK = "ActivityManager" + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_LOCKTASK;
    static final java.lang.String TAG_LRU = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_LRU;
    static final java.lang.String TAG_OOM_ADJ = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_OOM_ADJ;
    private static final java.lang.String TAG_POWER = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_POWER;
    static final java.lang.String TAG_PROCESSES = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_PROCESSES;
    private static final java.lang.String TAG_SERVICE = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_SERVICE;
    private static final java.lang.String TAG_SWITCH = "ActivityManager" + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    static final java.lang.String TAG_UID_OBSERVERS = "ActivityManager" + com.android.server.am.ActivityManagerDebugConfig.POSTFIX_UID_OBSERVERS;
    static final int PROC_START_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 10000;
    static final int BIND_APPLICATION_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * com.android.server.am.ProcessList.PSS_MIN_TIME_FROM_STATE_CHANGE;
    static final int BROADCAST_FG_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 10000;
    static final int BROADCAST_BG_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 60000;
    public static final int MY_PID = android.os.Process.myPid();
    static final java.lang.String[] EMPTY_STRING_ARRAY = new java.lang.String[0];
    public static boolean mForceStopKill = false;
    private static final java.time.format.DateTimeFormatter DROPBOX_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ");
    private static android.os.IAnrLogEnhancementHelperExt sAnrLogEnhancementHelper = (android.os.IAnrLogEnhancementHelperExt) system.ext.loader.core.ExtLoader.type(android.os.IAnrLogEnhancementHelperExt.class).create();
    private static boolean isScreenOn = false;
    private static boolean LTW_DISABLE = android.os.SystemProperties.getBoolean("persist.sys.ltw.disable", false);
    private static com.android.server.ThreadPriorityBooster sThreadPriorityBooster = new com.android.server.ThreadPriorityBooster(-2, 7);
    private static com.android.server.ThreadPriorityBooster sProcThreadPriorityBooster = new com.android.server.ThreadPriorityBooster(-2, 6);
    static final android.util.SparseArray<android.content.pm.ProcessInfo> sActiveProcessInfoSelfLocked = new android.util.SparseArray<>();
    static final com.android.server.am.ActivityManagerService.FgsTempAllowListItem FAKE_TEMP_ALLOW_LIST_ITEM = new com.android.server.am.ActivityManagerService.FgsTempAllowListItem(Long.MAX_VALUE, 300, "", -1);
    private static java.lang.String sTheRealBuildSerial = "unknown";
    static final com.android.server.am.HostingRecord sNullHostingRecord = new com.android.server.am.HostingRecord("");
    static final long[] DUMP_MEM_BUCKETS = {5120, 7168, 10240, 15360, 20480, 30720, 40960, 81920, 122880, 163840, 204800, 256000, 307200, 358400, 409600, 512000, 614400, 819200, 1048576, 2097152, 5242880, 10485760, 20971520};
    static final int[] DUMP_MEM_OOM_ADJ = {-1000, com.android.server.am.ProcessList.SYSTEM_ADJ, com.android.server.am.ProcessList.PERSISTENT_PROC_ADJ, com.android.server.am.ProcessList.PERSISTENT_SERVICE_ADJ, 0, 100, 200, com.android.server.am.ProcessList.PERCEPTIBLE_MEDIUM_APP_ADJ, 250, 300, 400, 500, 600, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ, 800, 900};
    static final java.lang.String[] DUMP_MEM_OOM_LABEL = {"Native", "System", "Persistent", "Persistent Service", "Foreground", "Visible", "Perceptible", "Perceptible Medium", "Perceptible Low", "Backup", "Heavy Weight", "A Services", "Home", "Previous", "B Services", "Cached"};
    static final java.lang.String[] DUMP_MEM_OOM_COMPACT_LABEL = {"native", "sys", "pers", "persvc", "fore", "vis", "percept", "perceptm", "perceptl", com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP, "heavy", "servicea", "home", "prev", "serviceb", "cached"};
    private static com.oplus.uifirst.IOplusUIFirstManagerExt mUIFirstManagerExt = (com.oplus.uifirst.IOplusUIFirstManagerExt) system.ext.loader.core.ExtLoader.type(com.oplus.uifirst.IOplusUIFirstManagerExt.class).create();
    private static com.oplus.osense.IOplusUserAwareManagerExt sUserAwareManagerExt = (com.oplus.osense.IOplusUserAwareManagerExt) system.ext.loader.core.ExtLoader.type(com.oplus.osense.IOplusUserAwareManagerExt.class).create();
    private static com.android.server.am.IActivityManagerServiceExt.IStaticExt mStaticExt = (com.android.server.am.IActivityManagerServiceExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IActivityManagerServiceExt.IStaticExt.class).create();

    interface OomAdjObserver {
        void onOomAdjMessage(java.lang.String str);
    }

    public static void boostPriorityForLockedSection() {
        sThreadPriorityBooster.boost();
    }

    public static void resetPriorityAfterLockedSection() {
        sThreadPriorityBooster.reset();
    }

    public static void boostPriorityForProcLockedSection() {
        sProcThreadPriorityBooster.boost();
    }

    public static void resetPriorityAfterProcLockedSection() {
        sProcThreadPriorityBooster.reset();
    }

    private final class PackageAssociationInfo {
        private final android.util.ArraySet<java.lang.String> mAllowedPackageAssociations;
        private boolean mIsDebuggable;
        private final java.lang.String mSourcePackage;

        PackageAssociationInfo(java.lang.String sourcePackage, android.util.ArraySet<java.lang.String> allowedPackages, boolean isDebuggable) {
            this.mSourcePackage = sourcePackage;
            this.mAllowedPackageAssociations = allowedPackages;
            this.mIsDebuggable = isDebuggable;
        }

        boolean isPackageAssociationAllowed(java.lang.String targetPackage) {
            return this.mIsDebuggable || this.mAllowedPackageAssociations.contains(targetPackage);
        }

        boolean isDebuggable() {
            return this.mIsDebuggable;
        }

        void setDebuggable(boolean isDebuggable) {
            this.mIsDebuggable = isDebuggable;
        }

        android.util.ArraySet<java.lang.String> getAllowedPackageAssociations() {
            return this.mAllowedPackageAssociations;
        }
    }

    static final class PidMap {
        private final android.util.SparseArray<com.android.server.am.ProcessRecord> mPidMap = new android.util.SparseArray<>();

        PidMap() {
        }

        com.android.server.am.ProcessRecord get(int pid) {
            return this.mPidMap.get(pid);
        }

        int size() {
            return this.mPidMap.size();
        }

        com.android.server.am.ProcessRecord valueAt(int index) {
            return this.mPidMap.valueAt(index);
        }

        int keyAt(int index) {
            return this.mPidMap.keyAt(index);
        }

        int indexOfKey(int key) {
            return this.mPidMap.indexOfKey(key);
        }

        void doAddInternal(int pid, com.android.server.am.ProcessRecord app) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                android.util.Slog.i("ActivityManager", "mPidMap put " + app);
            }
            this.mPidMap.put(pid, app);
        }

        boolean doRemoveInternal(int pid, com.android.server.am.ProcessRecord app) {
            com.android.server.am.ProcessRecord existingApp = this.mPidMap.get(pid);
            if (existingApp != null && existingApp.getStartSeq() == app.getStartSeq()) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                    android.util.Slog.i("ActivityManager", "mPidMap remove " + app);
                }
                com.android.server.am.ActivityManagerService.mUIFirstManagerExt.handleProcessStop(app.info.packageName, app.uid, pid);
                com.android.server.am.ActivityManagerService.sUserAwareManagerExt.notifyProcessKilled(app.info.packageName, pid);
                com.android.server.am.ActivityManagerService.mStaticExt.handleProcessStop(app, pid);
                this.mPidMap.remove(pid);
                return true;
            }
            return false;
        }
    }

    void addPidLocked(com.android.server.am.ProcessRecord app) {
        int pid = app.getPid();
        synchronized (this.mPidsSelfLocked) {
            this.mPidsSelfLocked.doAddInternal(pid, app);
        }
        synchronized (sActiveProcessInfoSelfLocked) {
            if (app.processInfo != null) {
                sActiveProcessInfoSelfLocked.put(pid, app.processInfo);
            } else {
                sActiveProcessInfoSelfLocked.remove(pid);
            }
        }
        this.mSocExt.addPidLocked(app);
        this.mAtmInternal.onProcessMapped(pid, app.getWindowProcessController());
    }

    boolean removePidLocked(int pid, com.android.server.am.ProcessRecord app) {
        boolean removed;
        synchronized (this.mPidsSelfLocked) {
            removed = this.mPidsSelfLocked.doRemoveInternal(pid, app);
        }
        if (removed) {
            synchronized (sActiveProcessInfoSelfLocked) {
                sActiveProcessInfoSelfLocked.remove(pid);
            }
            this.mSocExt.removePidLocked(app);
            this.mAtmInternal.onProcessUnMapped(pid);
        }
        return removed;
    }

    abstract class ImportanceToken implements android.os.IBinder.DeathRecipient {
        final int pid;
        final java.lang.String reason;
        final android.os.IBinder token;

        ImportanceToken(int _pid, android.os.IBinder _token, java.lang.String _reason) {
            this.pid = _pid;
            this.token = _token;
            this.reason = _reason;
        }

        public java.lang.String toString() {
            return "ImportanceToken { " + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.reason + " " + this.pid + " " + this.token + " }";
        }

        void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long pToken = proto.start(fieldId);
            proto.write(1120986464257L, this.pid);
            if (this.token != null) {
                proto.write(1138166333442L, this.token.toString());
            }
            proto.write(1138166333443L, this.reason);
            proto.end(pToken);
        }
    }

    static final class StickyBroadcast {
        public boolean deferUntilActive;
        public android.content.Intent intent;
        public com.android.server.am.IStickyBroadcastExt mStickyBroadcastExt = (com.android.server.am.IStickyBroadcastExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IStickyBroadcastExt.class).base(this).create();
        public int originalCallingAppProcessState;
        public int originalCallingUid;
        public java.lang.String resolvedDataType;

        StickyBroadcast() {
        }

        public static com.android.server.am.ActivityManagerService.StickyBroadcast create(android.content.Intent intent, boolean deferUntilActive, int originalCallingUid, int originalCallingAppProcessState, java.lang.String resolvedDataType) {
            com.android.server.am.ActivityManagerService.StickyBroadcast b = new com.android.server.am.ActivityManagerService.StickyBroadcast();
            b.intent = intent;
            b.deferUntilActive = deferUntilActive;
            b.originalCallingUid = originalCallingUid;
            b.originalCallingAppProcessState = originalCallingAppProcessState;
            b.resolvedDataType = resolvedDataType;
            return b;
        }

        public java.lang.String toString() {
            return "{intent=" + this.intent + ", defer=" + this.deferUntilActive + ", originalCallingUid=" + this.originalCallingUid + ", originalCallingAppProcessState=" + this.originalCallingAppProcessState + ", type=" + this.resolvedDataType + this.mStickyBroadcastExt.stickyBroadcastToString() + "}";
        }
    }

    static final class Association {
        int mCount;
        long mLastStateUptime;
        int mNesting;
        final java.lang.String mSourceProcess;
        final int mSourceUid;
        long mStartTime;
        final android.content.ComponentName mTargetComponent;
        final java.lang.String mTargetProcess;
        final int mTargetUid;
        long mTime;
        int mLastState = 21;
        long[] mStateTimes = new long[21];

        Association(int sourceUid, java.lang.String sourceProcess, int targetUid, android.content.ComponentName targetComponent, java.lang.String targetProcess) {
            this.mSourceUid = sourceUid;
            this.mSourceProcess = sourceProcess;
            this.mTargetUid = targetUid;
            this.mTargetComponent = targetComponent;
            this.mTargetProcess = targetProcess;
        }
    }

    static final class PendingTempAllowlist {
        final int callingUid;
        final long duration;
        final int reasonCode;
        final java.lang.String tag;
        final int targetUid;
        final int type;

        PendingTempAllowlist(int targetUid, long duration, int reasonCode, java.lang.String tag, int type, int callingUid) {
            this.targetUid = targetUid;
            this.duration = duration;
            this.tag = tag;
            this.type = type;
            this.reasonCode = reasonCode;
            this.callingUid = callingUid;
        }

        void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.targetUid);
            proto.write(1112396529666L, this.duration);
            proto.write(1138166333443L, this.tag);
            proto.write(1120986464260L, this.type);
            proto.write(1120986464261L, this.reasonCode);
            proto.write(1120986464262L, this.callingUid);
            proto.end(token);
        }
    }

    public static final class FgsTempAllowListItem {
        final int mCallingUid;
        final long mDuration;
        final java.lang.String mReason;
        final int mReasonCode;

        FgsTempAllowListItem(long duration, int reasonCode, java.lang.String reason, int callingUid) {
            this.mDuration = duration;
            this.mReasonCode = reasonCode;
            this.mReason = reason;
            this.mCallingUid = callingUid;
        }

        void dump(java.io.PrintWriter pw) {
            pw.print(" duration=" + this.mDuration + " callingUid=" + android.os.UserHandle.formatUid(this.mCallingUid) + " reasonCode=" + android.os.PowerExemptionManager.reasonCodeToString(this.mReasonCode) + " reason=" + this.mReason);
        }
    }

    static final class ProcessChangeItem {
        static final int CHANGE_ACTIVITIES = 1;
        static final int CHANGE_FOREGROUND_SERVICES = 2;
        int changes;
        boolean foregroundActivities;
        int foregroundServiceTypes;
        int pid;
        int processState;
        int uid;

        ProcessChangeItem() {
        }
    }

    private final class AppDeathRecipient implements android.os.IBinder.DeathRecipient {
        final com.android.server.am.ProcessRecord mApp;
        final android.app.IApplicationThread mAppThread;
        final int mPid;

        AppDeathRecipient(com.android.server.am.ProcessRecord app, int pid, android.app.IApplicationThread thread) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ALL) {
                android.util.Slog.v("ActivityManager", "New death recipient " + this + " for thread " + thread.asBinder());
            }
            this.mApp = app;
            this.mPid = pid;
            this.mAppThread = thread;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.onDeathRecipient(com.android.server.am.ActivityManagerService.this, this.mApp, this.mPid, this.mAppThread);
        }
    }

    final class UiHandler extends android.os.Handler {
        public UiHandler() {
            super(com.android.server.UiThread.get().getLooper(), null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.am.ActivityManagerService.this.mAppErrors.handleShowAppErrorUi(msg);
                    com.android.server.am.ActivityManagerService.this.ensureBootCompleted();
                    return;
                case 2:
                    com.android.server.am.ActivityManagerService.this.mAppErrors.handleShowAnrUi(msg);
                    com.android.server.am.ActivityManagerService.this.ensureBootCompleted();
                    return;
                case 6:
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            com.android.server.am.ProcessRecord app = (com.android.server.am.ProcessRecord) msg.obj;
                            if (msg.arg1 != 0) {
                                if (!app.hasWaitedForDebugger()) {
                                    app.mErrorState.getDialogController().showDebugWaitingDialogs();
                                    app.setWaitedForDebugger(true);
                                }
                            } else {
                                app.mErrorState.getDialogController().clearWaitingDialog();
                            }
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                case 26:
                    java.util.HashMap<java.lang.String, java.lang.Object> data = (java.util.HashMap) msg.obj;
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock2) {
                        try {
                            com.android.server.am.ProcessRecord proc = (com.android.server.am.ProcessRecord) data.get("app");
                            if (proc == null) {
                                android.util.Slog.e("ActivityManager", "App not found when showing strict mode dialog.");
                                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                return;
                            } else {
                                if (proc.mErrorState.getDialogController().hasViolationDialogs()) {
                                    android.util.Slog.e("ActivityManager", "App already has strict mode dialog: " + proc);
                                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                    return;
                                }
                                com.android.server.am.AppErrorResult res = (com.android.server.am.AppErrorResult) data.get("result");
                                if (com.android.server.am.ActivityManagerService.this.mAtmInternal.showStrictModeViolationDialog()) {
                                    proc.mErrorState.getDialogController().showViolationDialogs(res);
                                } else {
                                    res.set(0);
                                }
                                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                com.android.server.am.ActivityManagerService.this.ensureBootCompleted();
                                return;
                            }
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        }
                    }
                case 31:
                    com.android.server.am.ActivityManagerService.this.mProcessList.dispatchProcessesChanged();
                    return;
                case 32:
                    int pid = msg.arg1;
                    int uid = msg.arg2;
                    com.android.server.am.ActivityManagerService.this.mProcessList.dispatchProcessDied(pid, uid);
                    return;
                case 68:
                    com.android.server.OplusIoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$UiHandler$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$handleMessage$0();
                        }
                    });
                    return;
                case 70:
                    com.android.server.am.ActivityManagerService.this.dispatchOomAdjObserver((java.lang.String) msg.obj);
                    return;
                case 80:
                    com.android.server.am.ActivityManagerService.this.mUidObserverController.addUidToObserverImpl((android.os.IBinder) msg.obj, msg.arg1);
                    return;
                case 81:
                    com.android.server.am.ActivityManagerService.this.mUidObserverController.removeUidFromObserverImpl((android.os.IBinder) msg.obj, msg.arg1);
                    return;
                case 201:
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock3 = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock3) {
                        try {
                            ((com.android.server.am.ProcessRecord) msg.obj).mErrorState.getDialogController().showAnrErrorDialogs(msg.arg1);
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                case 202:
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock4 = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock4) {
                        try {
                            ((com.android.server.am.ProcessRecord) msg.obj).mErrorState.getDialogController().showAnrProgressDialogs();
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                default:
                    com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.handleOplusMessage(msg, 2);
                    return;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0() {
            com.android.server.am.ActivityManagerService.this.pushTempAllowlist();
        }
    }

    final class MainHandler extends android.os.Handler {
        public MainHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(final android.os.Message msg) {
            switch (msg.what) {
                case 5:
                    com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mAppProfiler.performAppGcsIfAppropriateLocked();
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                case 12:
                    if (com.android.server.am.ActivityManagerService.this.mSocExt.delayMessage(com.android.server.am.ActivityManagerService.this.mHandler, msg, 12, (int) com.android.server.am.ActivityManagerService.this.mConstants.SERVICE_TIMEOUT) || com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.handleServiceTimeOut(msg)) {
                        return;
                    }
                    com.android.server.am.ActivityManagerService.this.mServices.serviceTimeout((com.android.server.am.ProcessRecord) msg.obj);
                    return;
                case 13:
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$MainHandler$$ExternalSyntheticLambda0
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    com.android.server.am.ActivityManagerService.MainHandler.lambda$handleMessage$0((com.android.server.am.ProcessRecord) obj);
                                }
                            });
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                case 20:
                    if (com.android.server.am.ActivityManagerService.this.mSocExt.delayMessage(com.android.server.am.ActivityManagerService.this.mHandler, msg, 20, com.android.server.am.ActivityManagerService.PROC_START_TIMEOUT)) {
                        return;
                    }
                    com.android.server.am.ProcessRecord app = (com.android.server.am.ProcessRecord) msg.obj;
                    com.android.server.am.ActivityManagerService activityManagerService2 = com.android.server.am.ActivityManagerService.this;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService2) {
                        try {
                            com.android.server.am.ActivityManagerService.this.handleProcessStartOrKillTimeoutLocked(app, false);
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    if (app != null && com.android.server.am.ActivityManagerService.SYSTEMUI_PROCESS_NAME.equals(app.processName)) {
                        com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.startSystemUIService();
                        return;
                    }
                    return;
                case 22:
                    com.android.server.am.ActivityManagerService activityManagerService3 = com.android.server.am.ActivityManagerService.this;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService3) {
                        try {
                            int appId = msg.arg1;
                            int userId = msg.arg2;
                            com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                            java.lang.String pkg = (java.lang.String) args.arg1;
                            java.lang.String reason = (java.lang.String) args.arg2;
                            int exitInfoReason = ((java.lang.Integer) args.arg3).intValue();
                            args.recycle();
                            com.android.server.am.ActivityManagerService.this.forceStopPackageLocked(pkg, appId, false, false, true, false, false, false, userId, reason, exitInfoReason);
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                case 27:
                    com.android.server.am.ActivityManagerService.this.checkExcessivePowerUsage();
                    removeMessages(27);
                    android.os.Message nmsg = obtainMessage(27);
                    sendMessageDelayed(nmsg, com.android.server.am.ActivityManagerService.this.mConstants.POWER_CHECK_INTERVAL);
                    return;
                case 28:
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock2) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mProcessList.clearAllDnsCacheLOSP();
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                case 29:
                    com.android.server.am.ActivityManagerService.this.mProcessList.setAllHttpProxy();
                    return;
                case 33:
                    final java.util.ArrayList<com.android.server.am.ProcessMemInfo> memInfos = (java.util.ArrayList) msg.obj;
                    new java.lang.Thread() { // from class: com.android.server.am.ActivityManagerService.MainHandler.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            com.android.server.am.ActivityManagerService.this.mAppProfiler.reportMemUsage(memInfos);
                        }
                    }.start();
                    return;
                case 41:
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock3 = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock3) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mProcessList.updateAllTimePrefsLOSP(msg.arg1);
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                case 49:
                    int uid = msg.arg1;
                    byte[] firstPacket = (byte[]) msg.obj;
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock4 = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock4) {
                        try {
                            synchronized (com.android.server.am.ActivityManagerService.this.mPidsSelfLocked) {
                                for (int i = 0; i < com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.size(); i++) {
                                    com.android.server.am.ProcessRecord p = com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.valueAt(i);
                                    android.app.IApplicationThread thread = p.getThread();
                                    if (p.uid == uid && thread != null) {
                                        try {
                                            thread.notifyCleartextNetwork(firstPacket);
                                        } catch (android.os.RemoteException e) {
                                        }
                                    }
                                }
                            }
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                case 50:
                    com.android.server.am.ActivityManagerService.this.mAppProfiler.handlePostDumpHeapNotification();
                    return;
                case 51:
                    com.android.server.am.ActivityManagerService.this.mAppProfiler.handleAbortDumpHeap((java.lang.String) msg.obj);
                    return;
                case 56:
                    android.app.IUiAutomationConnection connection = (android.app.IUiAutomationConnection) msg.obj;
                    try {
                        connection.shutdown();
                        break;
                    } catch (android.os.RemoteException e2) {
                        android.util.Slog.w("ActivityManager", "Error shutting down UiAutomationConnection");
                    }
                    com.android.server.am.ActivityManagerService.this.mUserIsMonkey = false;
                    return;
                case 57:
                    com.android.server.am.ProcessRecord app2 = (com.android.server.am.ProcessRecord) msg.obj;
                    com.android.server.am.ActivityManagerService activityManagerService4 = com.android.server.am.ActivityManagerService.this;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService4) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mCpHelper.processContentProviderPublishTimedOutLocked(app2);
                        } finally {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                case 58:
                    com.android.server.am.ActivityManagerService.this.idleUids();
                    return;
                case 63:
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock5 = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock5) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mProcessList.handleAllTrustStorageUpdateLOSP();
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return;
                case 66:
                    com.android.server.am.ActivityManagerService.this.mServices.serviceForegroundTimeout((com.android.server.am.ServiceRecord) msg.obj);
                    return;
                case 67:
                    com.android.internal.os.SomeArgs args2 = (com.android.internal.os.SomeArgs) msg.obj;
                    com.android.server.am.ActivityManagerService.this.mServices.serviceForegroundTimeoutANR((com.android.server.am.ProcessRecord) args2.arg1, (com.android.internal.os.TimeoutRecord) args2.arg2);
                    args2.recycle();
                    return;
                case 69:
                    com.android.internal.os.SomeArgs args3 = (com.android.internal.os.SomeArgs) msg.obj;
                    com.android.server.am.ActivityManagerService.this.mServices.serviceForegroundCrash((com.android.server.am.ProcessRecord) args3.arg1, (java.lang.String) args3.arg2, (android.content.ComponentName) args3.arg3);
                    args3.recycle();
                    return;
                case 71:
                    com.android.server.am.ActivityManagerService activityManagerService5 = com.android.server.am.ActivityManagerService.this;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService5) {
                        try {
                            android.os.AppZygote appZygote = (android.os.AppZygote) msg.obj;
                            com.android.server.am.ActivityManagerService.this.mProcessList.killAppZygoteIfNeededLocked(appZygote, false);
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                case 72:
                    com.android.server.am.ActivityManagerService.this.handleBinderHeavyHitterAutoSamplerTimeOut();
                    return;
                case 73:
                    com.android.server.am.ActivityManagerService activityManagerService6 = com.android.server.am.ActivityManagerService.this;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService6) {
                        try {
                            ((com.android.server.am.ContentProviderRecord) msg.obj).onProviderPublishStatusLocked(false);
                        } finally {
                        }
                        break;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                case 74:
                    com.android.server.am.ActivityManagerService.this.mBroadcastEventListeners.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$MainHandler$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            android.os.Message message = msg;
                            ((android.app.ActivityManagerInternal.BroadcastEventListener) obj).onSendingBroadcast((java.lang.String) message.obj, message.arg1);
                        }
                    });
                    return;
                case 75:
                    com.android.server.am.ActivityManagerService.this.mBindServiceEventListeners.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$MainHandler$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            android.os.Message message = msg;
                            ((android.app.ActivityManagerInternal.BindServiceEventListener) obj).onBindingService((java.lang.String) message.obj, message.arg1);
                        }
                    });
                    return;
                case 76:
                    com.android.server.am.ActivityManagerService.this.mServices.onShortFgsTimeout((com.android.server.am.ServiceRecord) msg.obj);
                    return;
                case 77:
                    com.android.server.am.ActivityManagerService.this.mServices.onShortFgsProcstateTimeout((com.android.server.am.ServiceRecord) msg.obj);
                    return;
                case 78:
                    com.android.server.am.ActivityManagerService.this.mServices.onShortFgsAnrTimeout((com.android.server.am.ServiceRecord) msg.obj);
                    return;
                case 79:
                    com.android.server.am.ActivityManagerService.this.mAppProfiler.mCachedAppsWatermarkData.updateCachedAppsSnapshot(((java.lang.Long) msg.obj).longValue());
                    return;
                case 82:
                    com.android.server.am.ActivityManagerService.this.handleBindApplicationTimeoutSoft((com.android.server.am.ProcessRecord) msg.obj, msg.arg1);
                    return;
                case 83:
                    com.android.server.am.ActivityManagerService.this.handleBindApplicationTimeoutHard((com.android.server.am.ProcessRecord) msg.obj);
                    return;
                case 84:
                    com.android.server.am.ActivityManagerService.this.mServices.onFgsTimeout((com.android.server.am.ServiceRecord) msg.obj);
                    return;
                case 85:
                    com.android.server.am.ActivityManagerService.this.mServices.onFgsCrashTimeout((com.android.server.am.ServiceRecord) msg.obj);
                    return;
                case 86:
                    com.android.server.am.ActivityManagerService.this.handleFollowUpOomAdjusterUpdate();
                    return;
                default:
                    com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.handleOplusMessage(msg, 1);
                    return;
            }
        }

        static /* synthetic */ void lambda$handleMessage$0(com.android.server.am.ProcessRecord app) {
            android.app.IApplicationThread thread = app.getThread();
            if (thread != null) {
                try {
                    thread.updateTimeZone();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w("ActivityManager", "Failed to update time zone for: " + app.info.processName);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setSystemProcess() {
        try {
            android.os.ServiceManager.addService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY, this, true, 21);
            android.os.ServiceManager.addService("procstats", this.mProcessStats);
            android.os.ServiceManager.addService("meminfo", new com.android.server.am.ActivityManagerService.MemBinder(this), false, 2);
            android.os.ServiceManager.addService("gfxinfo", new com.android.server.am.ActivityManagerService.GraphicsBinder(this));
            android.os.ServiceManager.addService("dbinfo", new com.android.server.am.ActivityManagerService.DbBinder(this));
            this.mAppProfiler.setCpuInfoService();
            android.os.ServiceManager.addService(com.android.server.permission.access.PermissionUri.SCHEME, new com.android.server.am.ActivityManagerService.PermissionController(this));
            android.os.ServiceManager.addService("processinfo", new com.android.server.am.ActivityManagerService.ProcessInfoService(this));
            android.os.ServiceManager.addService("cacheinfo", new com.android.server.am.ActivityManagerService.CacheBinder(this));
            this.mSocExt.addAnrManagerService();
            android.content.pm.ApplicationInfo info = this.mContext.getPackageManager().getApplicationInfo(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 1049600);
            this.mSystemThread.installSystemApplicationInfo(info, getClass().getClassLoader());
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    com.android.server.am.ProcessRecord app = this.mProcessList.newProcessRecordLocked(info, info.processName, false, 0, false, 0, null, new com.android.server.am.HostingRecord("system"));
                    app.setPersistent(true);
                    app.setPid(MY_PID);
                    app.mState.setMaxAdj(com.android.server.am.ProcessList.SYSTEM_ADJ);
                    app.makeActive(this.mSystemThread.getApplicationThread(), this.mProcessStats);
                    app.mProfile.addHostingComponentType(1);
                    addPidLocked(app);
                    updateLruProcessLocked(app, false, null);
                    updateOomAdjLocked(14);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            this.mAppOpsService.startWatchingMode(63, null, new com.android.internal.app.IAppOpsCallback.Stub() { // from class: com.android.server.am.ActivityManagerService.4
                public void opChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) {
                    if (op == 63 && packageName != null && com.android.server.am.ActivityManagerService.this.getAppOpsManager().checkOpNoThrow(op, uid, packageName) != 0) {
                        com.android.server.am.ActivityManagerService.this.runInBackgroundDisabled(uid);
                    }
                }
            });
            int[] cameraOp = {26};
            this.mAppOpsService.startWatchingActive(cameraOp, new com.android.internal.app.IAppOpsActiveCallback.Stub() { // from class: com.android.server.am.ActivityManagerService.5
                public void opActiveChanged(int op, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean active, int attributionFlags, int attributionChainId) {
                    com.android.server.am.ActivityManagerService.this.cameraActiveChanged(uid, active);
                    if (active) {
                        com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.cameraActiveChanged(uid);
                    }
                }
            });
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.RuntimeException("Unable to find android system package", e);
        }
    }

    public void setWindowManager(com.android.server.wm.WindowManagerService wm) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mWindowManager = wm;
                this.mWmInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
                this.mActivityTaskManager.setWindowManager(wm);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setUsageStatsManager(android.app.usage.UsageStatsManagerInternal usageStatsManager) {
        this.mUsageStatsService = usageStatsManager;
        this.mActivityTaskManager.setUsageStatsManager(usageStatsManager);
    }

    public void setContentCaptureManager(com.android.server.contentcapture.ContentCaptureManagerInternal contentCaptureManager) {
        this.mContentCaptureService = contentCaptureManager;
    }

    public void startObservingNativeCrashes() {
        com.android.server.am.NativeCrashListener ncl = new com.android.server.am.NativeCrashListener(this);
        ncl.start();
    }

    public void setAppOpsPolicy(android.app.AppOpsManagerInternal.CheckOpsDelegate appOpsPolicy) {
        this.mAppOpsService.setAppOpsPolicy(appOpsPolicy);
    }

    public com.android.internal.app.IAppOpsService getAppOpsService() {
        return this.mAppOpsService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVoiceInteractionManagerProvider(android.app.ActivityManagerInternal.VoiceInteractionManagerProvider provider) {
        this.mVoiceInteractionManagerProvider = provider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static class VolatileDropboxEntryStates {
        private final java.lang.Boolean mIsProcessFrozen;
        private final java.time.ZonedDateTime mTimestamp;

        private VolatileDropboxEntryStates(java.lang.Boolean frozenState, java.time.ZonedDateTime timestamp) {
            this.mIsProcessFrozen = frozenState;
            this.mTimestamp = timestamp;
        }

        public static com.android.server.am.ActivityManagerService.VolatileDropboxEntryStates withProcessFrozenStateAndTimestamp(boolean frozenState, java.time.ZonedDateTime timestamp) {
            return new com.android.server.am.ActivityManagerService.VolatileDropboxEntryStates(java.lang.Boolean.valueOf(frozenState), timestamp);
        }

        public java.lang.Boolean isProcessFrozen() {
            return this.mIsProcessFrozen;
        }

        public java.time.ZonedDateTime getTimestamp() {
            return this.mTimestamp;
        }
    }

    static class MemBinder extends android.os.Binder {
        com.android.server.am.ActivityManagerService mActivityManagerService;
        private final com.android.server.utils.PriorityDump.PriorityDumper mPriorityDumper = new com.android.server.utils.PriorityDump.PriorityDumper() { // from class: com.android.server.am.ActivityManagerService.MemBinder.1
            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpHigh(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                dump(fd, pw, new java.lang.String[]{"-a"}, asProto);
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                com.android.server.am.ActivityManagerService.MemBinder.this.mActivityManagerService.dumpApplicationMemoryUsage(fd, pw, "  ", args, false, null, asProto);
            }
        };

        MemBinder(com.android.server.am.ActivityManagerService activityManagerService) {
            this.mActivityManagerService = activityManagerService;
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            try {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
                if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mActivityManagerService.mContext, "meminfo", pw)) {
                    return;
                }
                com.android.server.utils.PriorityDump.dump(this.mPriorityDumper, fd, pw, args);
            } finally {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
            }
        }
    }

    static class GraphicsBinder extends android.os.Binder {
        com.android.server.am.ActivityManagerService mActivityManagerService;

        GraphicsBinder(com.android.server.am.ActivityManagerService activityManagerService) {
            this.mActivityManagerService = activityManagerService;
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            try {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
                if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mActivityManagerService.mContext, "gfxinfo", pw)) {
                    return;
                }
                this.mActivityManagerService.dumpGraphicsHardwareUsage(fd, pw, args);
            } finally {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
            }
        }
    }

    static class DbBinder extends android.os.Binder {
        com.android.server.am.ActivityManagerService mActivityManagerService;

        DbBinder(com.android.server.am.ActivityManagerService activityManagerService) {
            this.mActivityManagerService = activityManagerService;
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            try {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
                if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mActivityManagerService.mContext, "dbinfo", pw)) {
                    return;
                }
                this.mActivityManagerService.dumpDbInfo(fd, pw, args);
            } finally {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
            }
        }
    }

    static class CacheBinder extends android.os.Binder {
        com.android.server.am.ActivityManagerService mActivityManagerService;

        CacheBinder(com.android.server.am.ActivityManagerService activityManagerService) {
            this.mActivityManagerService = activityManagerService;
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            try {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
                if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mActivityManagerService.mContext, "cacheinfo", pw)) {
                    return;
                }
                this.mActivityManagerService.dumpBinderCacheContents(fd, pw, args);
            } finally {
                this.mActivityManagerService.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
            }
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private static com.android.server.wm.ActivityTaskManagerService sAtm;
        private final com.android.server.am.ActivityManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mService = new com.android.server.am.ActivityManagerService(context, sAtm);
        }

        public static com.android.server.am.ActivityManagerService startService(com.android.server.SystemServiceManager ssm, com.android.server.wm.ActivityTaskManagerService atm) {
            sAtm = atm;
            return ((com.android.server.am.ActivityManagerService.Lifecycle) ssm.startService(com.android.server.am.ActivityManagerService.Lifecycle.class)).getService();
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService.start();
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            this.mService.mBootPhase = phase;
            if (phase == 500) {
                this.mService.mBatteryStatsService.systemServicesReady();
                this.mService.mServices.systemServicesReady();
            } else if (phase == 550) {
                this.mService.startBroadcastObservers();
            } else if (phase == 600) {
                this.mService.mPackageWatchdog.onPackagesReady();
                this.mService.scheduleHomeTimeout();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            this.mService.mBatteryStatsService.onCleanupUser(user.getUserIdentifier());
            if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures()) {
                com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
                android.content.pm.UserInfo userInfo = umInternal.getUserInfo(user.getUserIdentifier());
                if (userInfo != null && userInfo.isPrivateProfile()) {
                    com.android.server.am.ActivityManagerService activityManagerService = this.mService;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            this.mService.mPrivateSpaceBootCompletedPackages.clear();
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                }
            }
        }

        public com.android.server.am.ActivityManagerService getService() {
            return this.mService;
        }
    }

    private void maybeLogUserspaceRebootEvent() {
        int userId;
        if (!com.android.server.UserspaceRebootLogger.shouldLogUserspaceRebootEvent() || (userId = this.mUserController.getCurrentUserId()) != 0) {
            return;
        }
        com.android.server.UserspaceRebootLogger.logEventAsync(android.os.storage.StorageManager.isCeStorageUnlocked(userId), com.android.internal.os.BackgroundThread.getExecutor());
    }

    static class HiddenApiSettings extends android.database.ContentObserver implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        public static final java.lang.String HIDDEN_API_ACCESS_LOG_SAMPLING_RATE = "hidden_api_access_log_sampling_rate";
        public static final java.lang.String HIDDEN_API_ACCESS_STATSLOG_SAMPLING_RATE = "hidden_api_access_statslog_sampling_rate";
        private boolean mBlacklistDisabled;
        private final android.content.Context mContext;
        private java.util.List<java.lang.String> mExemptions;
        private java.lang.String mExemptionsStr;
        private int mLogSampleRate;
        private int mPolicy;
        private int mStatslogSampleRate;

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            int logSampleRate = properties.getInt(HIDDEN_API_ACCESS_LOG_SAMPLING_RATE, 0);
            if (logSampleRate < 0 || logSampleRate > 65536) {
                logSampleRate = -1;
            }
            if (logSampleRate != -1 && logSampleRate != this.mLogSampleRate) {
                this.mLogSampleRate = logSampleRate;
                android.os.Process.ZYGOTE_PROCESS.setHiddenApiAccessLogSampleRate(this.mLogSampleRate);
            }
            int statslogSampleRate = properties.getInt(HIDDEN_API_ACCESS_STATSLOG_SAMPLING_RATE, 0);
            if (statslogSampleRate < 0 || statslogSampleRate > 65536) {
                statslogSampleRate = -1;
            }
            if (statslogSampleRate != -1 && statslogSampleRate != this.mStatslogSampleRate) {
                this.mStatslogSampleRate = statslogSampleRate;
                android.os.Process.ZYGOTE_PROCESS.setHiddenApiAccessStatslogSampleRate(this.mStatslogSampleRate);
            }
        }

        public HiddenApiSettings(android.os.Handler handler, android.content.Context context) {
            super(handler);
            this.mExemptions = java.util.Collections.emptyList();
            this.mLogSampleRate = -1;
            this.mStatslogSampleRate = -1;
            this.mPolicy = -1;
            this.mContext = context;
        }

        public void registerObserver() {
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("hidden_api_blacklist_exemptions"), false, this);
            this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("hidden_api_policy"), false, this);
            android.provider.DeviceConfig.addOnPropertiesChangedListener("app_compat", this.mContext.getMainExecutor(), this);
            update();
        }

        private void update() {
            java.util.List<java.lang.String> listAsList;
            java.lang.String exemptions = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "hidden_api_blacklist_exemptions");
            if (!android.text.TextUtils.equals(exemptions, this.mExemptionsStr)) {
                this.mExemptionsStr = exemptions;
                if (com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER.equals(exemptions)) {
                    this.mBlacklistDisabled = true;
                    this.mExemptions = java.util.Collections.emptyList();
                } else {
                    this.mBlacklistDisabled = false;
                    if (android.text.TextUtils.isEmpty(exemptions)) {
                        listAsList = java.util.Collections.emptyList();
                    } else {
                        listAsList = java.util.Arrays.asList(exemptions.split(","));
                    }
                    this.mExemptions = listAsList;
                }
                if (!android.os.Process.ZYGOTE_PROCESS.setApiDenylistExemptions(this.mExemptions)) {
                    android.util.Slog.e("ActivityManager", "Failed to set API blacklist exemptions!");
                    this.mExemptions = java.util.Collections.emptyList();
                }
            }
            this.mPolicy = getValidEnforcementPolicy("hidden_api_policy");
        }

        private int getValidEnforcementPolicy(java.lang.String settingsKey) {
            int policy = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), settingsKey, -1);
            if (android.content.pm.ApplicationInfo.isValidHiddenApiEnforcementPolicy(policy)) {
                return policy;
            }
            return -1;
        }

        boolean isDisabled() {
            return this.mBlacklistDisabled;
        }

        int getPolicy() {
            return this.mPolicy;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            update();
        }
    }

    android.app.AppOpsManager getAppOpsManager() {
        if (this.mAppOpsManager == null) {
            this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        }
        return this.mAppOpsManager;
    }

    ActivityManagerService(com.android.server.am.ActivityManagerService.Injector injector, com.android.server.ServiceThread handlerThread) {
        this(injector, handlerThread, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    ActivityManagerService(com.android.server.am.ActivityManagerService.Injector injector, com.android.server.ServiceThread serviceThread, com.android.server.am.UserController userController) {
        com.android.server.am.OomAdjuster oomAdjuster;
        this.mInstrumentationReporter = new com.android.server.am.InstrumentationReporter();
        this.mActiveInstrumentation = new java.util.ArrayList<>();
        this.mGlobalLock = this;
        this.mProcLock = new com.android.server.am.ActivityManagerProcLock();
        this.mAllowSpecifiedFifoScheduling = true;
        this.mStrictModeCallbacks = new android.util.SparseArray<>();
        this.mDeviceOwnerUid = -1;
        this.mCompanionAppUidsMap = new android.util.ArrayMap();
        this.mProfileOwnerUids = null;
        this.mDeliveryGroupPolicyIgnoredActions = new android.util.ArraySet<>();
        this.mActiveCameraUids = new android.util.IntArray(4);
        this.mPriorityDumper = new com.android.server.utils.PriorityDump.PriorityDumper() { // from class: com.android.server.am.ActivityManagerService.1
            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpCritical(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                if (asProto) {
                    return;
                }
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, new java.lang.String[]{com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_CMD}, asProto);
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, new java.lang.String[]{com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, "all-platform-critical"}, asProto);
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpNormal(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, new java.lang.String[]{"-a", "--normal-priority"}, asProto);
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, args, asProto);
            }
        };
        this.mBackgroundAppIdAllowlist = new int[]{1002};
        this.mHasHomeDelay = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mThemeOverlayReadyUsers = new java.util.HashSet();
        this.mPidsSelfLocked = new com.android.server.am.ActivityManagerService.PidMap();
        this.mImportantProcesses = new android.util.SparseArray<>();
        this.mSpecifiedFifoProcesses = new java.util.ArrayList<>();
        this.mProcessesOnHold = new java.util.ArrayList<>();
        this.mPersistentStartingProcesses = new java.util.ArrayList<>();
        this.mActivityLaunchObserver = new com.android.server.wm.ActivityMetricsLaunchObserver() { // from class: com.android.server.am.ActivityManagerService.2
            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onIntentStarted(android.content.Intent intent, long timestampNanos) {
                synchronized (this) {
                    com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onIntentStarted(intent, timestampNanos);
                }
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onIntentFailed(long id) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onIntentFailed(id);
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onActivityLaunched(long id, android.content.ComponentName name, int temperature, int userId) {
                com.android.server.am.ActivityManagerService.this.mAppProfiler.onActivityLaunched();
                com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    com.android.server.am.ProcessRecord record = null;
                    try {
                        try {
                            record = com.android.server.am.ActivityManagerService.this.getProcessRecordLocked(name.getPackageName(), com.android.server.am.ActivityManagerService.this.mContext.getPackageManager().getPackageUidAsUser(name.getPackageName(), 0, userId));
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    }
                    com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onActivityLaunched(id, name, temperature, record);
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onActivityLaunchCancelled(long id) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onActivityLaunchCancelled(id);
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onActivityLaunchFinished(long id, android.content.ComponentName name, long timestampNanos, int launchMode) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onActivityLaunchFinished(id, name, timestampNanos, launchMode);
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onReportFullyDrawn(long id, long timestampNanos) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onReportFullyDrawn(id, timestampNanos);
            }
        };
        this.mBinderTransactionTrackingEnabled = false;
        this.mAlreadyLoggedViolatedStacks = new java.util.HashSet<>();
        this.mRegisteredReceivers = new java.util.HashMap<>();
        this.mReceiverResolver = new com.android.server.IntentResolver<com.android.server.am.BroadcastFilter, com.android.server.am.BroadcastFilter>() { // from class: com.android.server.am.ActivityManagerService.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public boolean allowFilterResult(com.android.server.am.BroadcastFilter filter, java.util.List<com.android.server.am.BroadcastFilter> dest) {
                android.os.IBinder target = filter.receiverList.receiver.asBinder();
                for (int i = dest.size() - 1; i >= 0; i--) {
                    if (dest.get(i).receiverList.receiver.asBinder() == target) {
                        return false;
                    }
                }
                return true;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public com.android.server.am.BroadcastFilter newResult(com.android.server.pm.Computer computer, com.android.server.am.BroadcastFilter filter, int match, int userId, long customFlags) {
                if (userId == -1 || filter.owningUserId == -1 || userId == filter.owningUserId) {
                    return (com.android.server.am.BroadcastFilter) super.newResult(computer, filter, match, userId, customFlags);
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public android.content.IntentFilter getIntentFilter(com.android.server.am.BroadcastFilter input) {
                return input;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.server.IntentResolver
            public com.android.server.am.BroadcastFilter[] newArray(int size) {
                return new com.android.server.am.BroadcastFilter[size];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public boolean isPackageForFilter(java.lang.String packageName, com.android.server.am.BroadcastFilter filter) {
                return packageName.equals(filter.packageName);
            }
        };
        this.mStickyBroadcasts = new android.util.SparseArray<>();
        this.mAssociations = new android.util.SparseArray<>();
        this.mBackupTargets = new android.util.SparseArray<>();
        this.mDeviceIdleAllowlist = new int[0];
        this.mDeviceIdleExceptIdleAllowlist = new int[0];
        this.mDeviceIdleTempAllowlist = new int[0];
        this.mPendingTempAllowlist = new com.android.server.am.PendingTempAllowlists(this);
        this.mFgsStartTempAllowList = new com.android.server.am.FgsTempAllowList<>();
        this.mFgsWhileInUseTempAllowList = new com.android.server.am.FgsTempAllowList<>();
        this.mProcessesReady = false;
        this.mSystemReady = false;
        this.mOnBattery = false;
        this.mBooting = false;
        this.mCallFinishBooting = false;
        this.mBootAnimationComplete = false;
        this.mWakefulness = new java.util.concurrent.atomic.AtomicInteger(1);
        this.mLastIdleTime = android.os.SystemClock.uptimeMillis();
        this.mCurResumedPackage = null;
        this.mCurResumedUid = -1;
        this.mCurResumedAppLock = new java.lang.Object();
        this.mForegroundPackages = new com.android.internal.app.ProcessMap<>();
        this.mForegroundServiceStateListeners = new java.util.ArrayList<>();
        this.mBroadcastEventListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mBindServiceEventListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mDebugApp = null;
        this.mWaitForDebugger = false;
        this.mSuspendUponWait = false;
        this.mDebugTransient = false;
        this.mOrigDebugApp = null;
        this.mOrigWaitForDebugger = false;
        this.mAlwaysFinishActivities = false;
        this.mTrackAllocationApp = null;
        this.mNativeDebuggingApp = null;
        this.mOomAdjObserverLock = new java.lang.Object();
        this.mAnrHelper = new com.android.server.am.AnrHelper(this);
        this.mBooted = false;
        this.mDeterministicUidIdle = false;
        this.mUidNetworkBlockedReasons = new android.util.SparseIntArray();
        this.mMediaProjectionTokenMap = new android.util.SparseArray<>();
        this.mPrivateSpaceBootCompletedPackages = new android.util.ArraySet<>();
        this.mLastBinderHeavyHitterAutoSamplerStart = 0L;
        this.mGetBackgroundStartPrivilegesFunctor = new com.android.server.am.ActivityManagerService.GetBackgroundStartPrivilegesFunctor();
        this.mUidFrozenStateChangedCallbackList = new android.os.RemoteCallbackList<>();
        this.mDropboxRateLimiter = new com.android.server.am.DropboxRateLimiter();
        this.mCallingPid = 0;
        this.mAmsWrapper = new com.android.server.am.ActivityManagerService.ActivityManagerServiceWrapper();
        this.mActivityManagerServiceExt = (com.android.server.am.IActivityManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IActivityManagerServiceExt.class).create();
        this.mSocExt = (com.android.server.am.IActivityManagerServiceSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IActivityManagerServiceSocExt.class).base(this).create();
        this.mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).create();
        this.mKillBackgroundProcessesCallingUid = -1;
        this.mInjector = injector;
        this.mContext = this.mInjector.getContext();
        this.mUiContext = null;
        this.mAppErrors = injector.getAppErrors();
        this.mPackageWatchdog = null;
        this.mAppOpsService = this.mInjector.getAppOpsService(null, null, null);
        this.mBatteryStatsService = this.mInjector.getBatteryStatsService();
        this.mHandler = new com.android.server.am.ActivityManagerService.MainHandler(serviceThread.getLooper());
        this.mHandlerThread = serviceThread;
        this.mConstants = new com.android.server.am.ActivityManagerConstants(this.mContext, this, this.mHandler);
        com.android.server.am.ActiveUids activeUids = new com.android.server.am.ActiveUids(this, false);
        this.mPlatformCompat = null;
        this.mProcessList = injector.getProcessList(this);
        this.mProcessList.init(this, activeUids, this.mPlatformCompat);
        this.mAppProfiler = new com.android.server.am.AppProfiler(this, com.android.internal.os.BackgroundThread.getHandler().getLooper(), null);
        this.mPhantomProcessList = new com.android.server.am.PhantomProcessList(this);
        if (this.mConstants.ENABLE_NEW_OOMADJ) {
            oomAdjuster = new com.android.server.am.OomAdjusterModernImpl(this, this.mProcessList, activeUids, serviceThread);
        } else {
            oomAdjuster = new com.android.server.am.OomAdjuster(this, this.mProcessList, activeUids, serviceThread);
        }
        this.mOomAdjuster = oomAdjuster;
        this.mIntentFirewall = injector.getIntentFirewall();
        this.mProcessStats = new com.android.server.am.ProcessStatsService(this, this.mContext.getCacheDir());
        this.mCpHelper = new com.android.server.am.ContentProviderHelper(this, false);
        this.mServices = this.mInjector.getActiveServices(this);
        this.mSystemThread = null;
        this.mUiHandler = injector.getUiHandler(null);
        this.mUidObserverController = new com.android.server.am.UidObserverController(this.mUiHandler);
        this.mUserController = userController == null ? new com.android.server.am.UserController(this) : userController;
        this.mInjector.mUserController = this.mUserController;
        this.mPendingIntentController = new com.android.server.am.PendingIntentController(serviceThread.getLooper(), this.mUserController, this.mConstants);
        this.mAppRestrictionController = new com.android.server.am.AppRestrictionController(this.mContext, this);
        this.mProcStartHandlerThread = null;
        this.mProcStartHandler = null;
        this.mHiddenApiBlacklist = null;
        this.mFactoryTest = 0;
        this.mUgmInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
        this.mInternal = new com.android.server.am.ActivityManagerService.LocalService();
        this.mPendingStartActivityUids = new com.android.server.am.PendingStartActivityUids();
        this.mUseFifoUiScheduling = false;
        this.mBroadcastQueue = injector.getBroadcastQueue(this);
        this.mComponentAliasResolver = new com.android.server.am.ComponentAliasResolver(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ActivityManagerService(android.content.Context context, com.android.server.wm.ActivityTaskManagerService activityTaskManagerService) {
        com.android.server.am.OomAdjuster oomAdjuster;
        boolean isOnBattery;
        this.mInstrumentationReporter = new com.android.server.am.InstrumentationReporter();
        this.mActiveInstrumentation = new java.util.ArrayList<>();
        this.mGlobalLock = this;
        this.mProcLock = new com.android.server.am.ActivityManagerProcLock();
        this.mAllowSpecifiedFifoScheduling = true;
        this.mStrictModeCallbacks = new android.util.SparseArray<>();
        this.mDeviceOwnerUid = -1;
        this.mCompanionAppUidsMap = new android.util.ArrayMap();
        this.mProfileOwnerUids = null;
        this.mDeliveryGroupPolicyIgnoredActions = new android.util.ArraySet<>();
        this.mActiveCameraUids = new android.util.IntArray(4);
        this.mPriorityDumper = new com.android.server.utils.PriorityDump.PriorityDumper() { // from class: com.android.server.am.ActivityManagerService.1
            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpCritical(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                if (asProto) {
                    return;
                }
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, new java.lang.String[]{com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_CMD}, asProto);
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, new java.lang.String[]{com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, "all-platform-critical"}, asProto);
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpNormal(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, new java.lang.String[]{"-a", "--normal-priority"}, asProto);
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) throws java.lang.Throwable {
                com.android.server.am.ActivityManagerService.this.doDump(fd, pw, args, asProto);
            }
        };
        this.mBackgroundAppIdAllowlist = new int[]{1002};
        this.mHasHomeDelay = new java.util.concurrent.atomic.AtomicBoolean(false);
        this.mThemeOverlayReadyUsers = new java.util.HashSet();
        this.mPidsSelfLocked = new com.android.server.am.ActivityManagerService.PidMap();
        this.mImportantProcesses = new android.util.SparseArray<>();
        this.mSpecifiedFifoProcesses = new java.util.ArrayList<>();
        this.mProcessesOnHold = new java.util.ArrayList<>();
        this.mPersistentStartingProcesses = new java.util.ArrayList<>();
        this.mActivityLaunchObserver = new com.android.server.wm.ActivityMetricsLaunchObserver() { // from class: com.android.server.am.ActivityManagerService.2
            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onIntentStarted(android.content.Intent intent, long timestampNanos) {
                synchronized (this) {
                    com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onIntentStarted(intent, timestampNanos);
                }
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onIntentFailed(long id) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onIntentFailed(id);
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onActivityLaunched(long id, android.content.ComponentName name, int temperature, int userId) {
                com.android.server.am.ActivityManagerService.this.mAppProfiler.onActivityLaunched();
                com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    com.android.server.am.ProcessRecord record = null;
                    try {
                        try {
                            record = com.android.server.am.ActivityManagerService.this.getProcessRecordLocked(name.getPackageName(), com.android.server.am.ActivityManagerService.this.mContext.getPackageManager().getPackageUidAsUser(name.getPackageName(), 0, userId));
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    }
                    com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onActivityLaunched(id, name, temperature, record);
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onActivityLaunchCancelled(long id) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onActivityLaunchCancelled(id);
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onActivityLaunchFinished(long id, android.content.ComponentName name, long timestampNanos, int launchMode) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onActivityLaunchFinished(id, name, timestampNanos, launchMode);
            }

            @Override // com.android.server.wm.ActivityMetricsLaunchObserver
            public void onReportFullyDrawn(long id, long timestampNanos) {
                com.android.server.am.ActivityManagerService.this.mProcessList.getAppStartInfoTracker().onReportFullyDrawn(id, timestampNanos);
            }
        };
        this.mBinderTransactionTrackingEnabled = false;
        this.mAlreadyLoggedViolatedStacks = new java.util.HashSet<>();
        this.mRegisteredReceivers = new java.util.HashMap<>();
        this.mReceiverResolver = new com.android.server.IntentResolver<com.android.server.am.BroadcastFilter, com.android.server.am.BroadcastFilter>() { // from class: com.android.server.am.ActivityManagerService.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public boolean allowFilterResult(com.android.server.am.BroadcastFilter filter, java.util.List<com.android.server.am.BroadcastFilter> dest) {
                android.os.IBinder target = filter.receiverList.receiver.asBinder();
                for (int i = dest.size() - 1; i >= 0; i--) {
                    if (dest.get(i).receiverList.receiver.asBinder() == target) {
                        return false;
                    }
                }
                return true;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public com.android.server.am.BroadcastFilter newResult(com.android.server.pm.Computer computer, com.android.server.am.BroadcastFilter filter, int match, int userId, long customFlags) {
                if (userId == -1 || filter.owningUserId == -1 || userId == filter.owningUserId) {
                    return (com.android.server.am.BroadcastFilter) super.newResult(computer, filter, match, userId, customFlags);
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public android.content.IntentFilter getIntentFilter(com.android.server.am.BroadcastFilter input) {
                return input;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.android.server.IntentResolver
            public com.android.server.am.BroadcastFilter[] newArray(int size) {
                return new com.android.server.am.BroadcastFilter[size];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.android.server.IntentResolver
            public boolean isPackageForFilter(java.lang.String packageName, com.android.server.am.BroadcastFilter filter) {
                return packageName.equals(filter.packageName);
            }
        };
        this.mStickyBroadcasts = new android.util.SparseArray<>();
        this.mAssociations = new android.util.SparseArray<>();
        this.mBackupTargets = new android.util.SparseArray<>();
        this.mDeviceIdleAllowlist = new int[0];
        this.mDeviceIdleExceptIdleAllowlist = new int[0];
        this.mDeviceIdleTempAllowlist = new int[0];
        this.mPendingTempAllowlist = new com.android.server.am.PendingTempAllowlists(this);
        this.mFgsStartTempAllowList = new com.android.server.am.FgsTempAllowList<>();
        this.mFgsWhileInUseTempAllowList = new com.android.server.am.FgsTempAllowList<>();
        this.mProcessesReady = false;
        this.mSystemReady = false;
        this.mOnBattery = false;
        this.mBooting = false;
        this.mCallFinishBooting = false;
        this.mBootAnimationComplete = false;
        this.mWakefulness = new java.util.concurrent.atomic.AtomicInteger(1);
        this.mLastIdleTime = android.os.SystemClock.uptimeMillis();
        this.mCurResumedPackage = null;
        this.mCurResumedUid = -1;
        this.mCurResumedAppLock = new java.lang.Object();
        this.mForegroundPackages = new com.android.internal.app.ProcessMap<>();
        this.mForegroundServiceStateListeners = new java.util.ArrayList<>();
        this.mBroadcastEventListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mBindServiceEventListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mDebugApp = null;
        this.mWaitForDebugger = false;
        this.mSuspendUponWait = false;
        this.mDebugTransient = false;
        this.mOrigDebugApp = null;
        this.mOrigWaitForDebugger = false;
        this.mAlwaysFinishActivities = false;
        this.mTrackAllocationApp = null;
        this.mNativeDebuggingApp = null;
        this.mOomAdjObserverLock = new java.lang.Object();
        this.mAnrHelper = new com.android.server.am.AnrHelper(this);
        this.mBooted = false;
        this.mDeterministicUidIdle = false;
        this.mUidNetworkBlockedReasons = new android.util.SparseIntArray();
        this.mMediaProjectionTokenMap = new android.util.SparseArray<>();
        this.mPrivateSpaceBootCompletedPackages = new android.util.ArraySet<>();
        this.mLastBinderHeavyHitterAutoSamplerStart = 0L;
        this.mGetBackgroundStartPrivilegesFunctor = new com.android.server.am.ActivityManagerService.GetBackgroundStartPrivilegesFunctor();
        this.mUidFrozenStateChangedCallbackList = new android.os.RemoteCallbackList<>();
        this.mDropboxRateLimiter = new com.android.server.am.DropboxRateLimiter();
        this.mCallingPid = 0;
        this.mAmsWrapper = new com.android.server.am.ActivityManagerService.ActivityManagerServiceWrapper();
        this.mActivityManagerServiceExt = (com.android.server.am.IActivityManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IActivityManagerServiceExt.class).create();
        this.mSocExt = (com.android.server.am.IActivityManagerServiceSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IActivityManagerServiceSocExt.class).base(this).create();
        this.mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).create();
        this.mKillBackgroundProcessesCallingUid = -1;
        this.mActivityManagerServiceExt.initAmsExAndInner(context, this, activityTaskManagerService);
        com.android.server.LockGuard.installLock(this, 7);
        this.mInjector = new com.android.server.am.ActivityManagerService.Injector(context);
        this.mContext = context;
        this.mFactoryTest = android.os.FactoryTest.getMode();
        this.mSystemThread = android.app.ActivityThread.currentActivityThread();
        this.mUiContext = this.mSystemThread.getSystemUiContext();
        android.util.Slog.i("ActivityManager", "Memory class: " + android.app.ActivityManager.staticGetMemoryClass());
        this.mHandlerThread = new com.android.server.ServiceThread("ActivityManager", -2, false);
        this.mHandlerThread.start();
        this.mHandler = new com.android.server.am.ActivityManagerService.MainHandler(this.mHandlerThread.getLooper());
        this.mActivityManagerServiceExt.setThreadSchedPolicy(this.mHandlerThread.getThreadId(), "ActivityManager", 14);
        this.mUiHandler = this.mInjector.getUiHandler(this);
        this.mProcStartHandlerThread = new com.android.server.ServiceThread("ActivityManager:procStart", -2, false);
        this.mProcStartHandlerThread.start();
        this.mProcStartHandler = new com.android.server.am.ProcessList.ProcStartHandler(this, this.mProcStartHandlerThread.getLooper());
        this.mActivityManagerServiceExt.setThreadSchedPolicy(this.mProcStartHandlerThread.getThreadId(), "ActivityManager:procStart", 14);
        this.mConstants = new com.android.server.am.ActivityManagerConstants(this.mContext, this, this.mHandler);
        com.android.server.am.ActiveUids activeUids = new com.android.server.am.ActiveUids(this, true);
        this.mPlatformCompat = (com.android.server.compat.PlatformCompat) android.os.ServiceManager.getService("platform_compat");
        this.mProcessList = this.mInjector.getProcessList(this);
        this.mProcessList.init(this, activeUids, this.mPlatformCompat);
        this.mAppProfiler = new com.android.server.am.AppProfiler(this, com.android.internal.os.BackgroundThread.getHandler().getLooper(), new com.android.server.am.LowMemDetector(this));
        this.mPhantomProcessList = new com.android.server.am.PhantomProcessList(this);
        if (this.mConstants.ENABLE_NEW_OOMADJ) {
            oomAdjuster = new com.android.server.am.OomAdjusterModernImpl(this, this.mProcessList, activeUids);
        } else {
            oomAdjuster = new com.android.server.am.OomAdjuster(this, this.mProcessList, activeUids);
        }
        this.mOomAdjuster = oomAdjuster;
        this.mActivityManagerServiceExt.initBroadcastAndBootPressure(this);
        this.mBroadcastQueue = this.mInjector.getBroadcastQueue(this);
        this.mServices = new com.android.server.am.ActiveServices(this);
        this.mCpHelper = new com.android.server.am.ContentProviderHelper(this, true);
        this.mPackageWatchdog = com.android.server.PackageWatchdog.getInstance(this.mUiContext);
        this.mAppErrors = new com.android.server.am.AppErrors(this.mUiContext, this, this.mPackageWatchdog);
        this.mUidObserverController = new com.android.server.am.UidObserverController(this.mUiHandler);
        java.io.File fileEnsureSystemDir = com.android.server.SystemServiceManager.ensureSystemDir();
        this.mBatteryStatsService = com.android.server.am.BatteryStatsService.create(context, fileEnsureSystemDir, com.android.internal.os.BackgroundThread.getHandler(), this);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER) {
            isOnBattery = true;
        } else {
            isOnBattery = this.mBatteryStatsService.getActiveStatistics().getIsOnBattery();
        }
        this.mOnBattery = isOnBattery;
        this.mProcessStats = new com.android.server.am.ProcessStatsService(this, new java.io.File(fileEnsureSystemDir, "procstats"));
        this.mAppOpsService = this.mInjector.getAppOpsService(new java.io.File(fileEnsureSystemDir, "appops_accesses.xml"), new java.io.File(fileEnsureSystemDir, "appops.xml"), this.mHandler);
        this.mUgmInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
        this.mUserController = new com.android.server.am.UserController(this);
        this.mInjector.mUserController = this.mUserController;
        this.mPendingIntentController = new com.android.server.am.PendingIntentController(this.mHandlerThread.getLooper(), this.mUserController, this.mConstants);
        this.mAppRestrictionController = new com.android.server.am.AppRestrictionController(this.mContext, this);
        this.mUseFifoUiScheduling = android.os.SystemProperties.getInt("sys.use_fifo_ui", 0) != 0;
        this.mTrackingAssociations = "1".equals(android.os.SystemProperties.get("debug.track-associations"));
        this.mIntentFirewall = new com.android.server.firewall.IntentFirewall(new com.android.server.am.ActivityManagerService.IntentFirewallInterface(), this.mHandler);
        this.mActivityTaskManager = activityTaskManagerService;
        this.mActivityTaskManager.initialize(this.mIntentFirewall, this.mPendingIntentController, com.android.server.DisplayThread.get().getLooper());
        this.mAtmInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mTaskSupervisor = this.mActivityTaskManager.mTaskSupervisor;
        this.mHiddenApiBlacklist = new com.android.server.am.ActivityManagerService.HiddenApiSettings(this.mHandler, this.mContext);
        com.android.server.Watchdog.getInstance().addMonitor(this);
        com.android.server.Watchdog.getInstance().addThread(this.mHandler);
        this.mActivityManagerServiceExt.addMonitor(this);
        updateOomAdjLocked(14);
        try {
            android.os.Process.setThreadGroupAndCpuset(com.android.internal.os.BackgroundThread.get().getThreadId(), 2);
            android.os.Process.setThreadGroupAndCpuset(this.mOomAdjuster.mCachedAppOptimizer.mCachedAppOptimizerThread.getThreadId(), 2);
        } catch (java.lang.Exception e) {
            android.util.Slog.w("ActivityManager", "Setting background thread cpuset failed");
        }
        this.mInternal = new com.android.server.am.ActivityManagerService.LocalService();
        this.mActivityManagerServiceExt.hookAMSConstructEnd();
        this.mPendingStartActivityUids = new com.android.server.am.PendingStartActivityUids();
        this.mTraceErrorLogger = new com.android.server.am.TraceErrorLogger();
        this.mComponentAliasResolver = new com.android.server.am.ComponentAliasResolver(this);
    }

    void setBroadcastQueueForTest(com.android.server.am.BroadcastQueue broadcastQueue) {
        this.mBroadcastQueue = broadcastQueue;
    }

    com.android.server.am.BroadcastQueue getBroadcastQueue() {
        return this.mBroadcastQueue;
    }

    public void setSystemServiceManager(com.android.server.SystemServiceManager mgr) {
        this.mSystemServiceManager = mgr;
    }

    public void setInstaller(com.android.server.pm.Installer installer) {
        this.mInstaller = installer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        this.mBatteryStatsService.publish();
        this.mAppOpsService.publish();
        this.mProcessStats.publish();
        android.util.Slog.d("AppOps", "AppOpsService published");
        com.android.server.LocalServices.addService(android.app.ActivityManagerInternal.class, this.mInternal);
        com.android.server.LocalManagerRegistry.addManager(com.android.server.am.ActivityManagerLocal.class, this.mInternal);
        this.mActivityTaskManager.onActivityManagerInternalAdded();
        this.mPendingIntentController.onActivityManagerInternalAdded();
        this.mAppProfiler.onActivityManagerInternalAdded();
        com.android.server.criticalevents.CriticalEventLog.init();
        this.mActivityManagerServiceExt.onOplusStart();
        this.mSocExt.startAnrManagerService(MY_PID);
    }

    public void initPowerManagement() {
        this.mActivityTaskManager.onInitPowerManagement();
        this.mBatteryStatsService.initPowerManagement();
        this.mLocalPowerManager = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
    }

    private android.util.ArraySet<java.lang.String> getBackgroundLaunchBroadcasts() {
        if (this.mBackgroundLaunchBroadcasts == null) {
            this.mBackgroundLaunchBroadcasts = com.android.server.SystemConfig.getInstance().getAllowImplicitBroadcasts();
        }
        return this.mBackgroundLaunchBroadcasts;
    }

    private java.lang.String getWearRemoteIntentAction() {
        return this.mContext.getResources().getString(android.R.string.config_wearableAmbientContextEventArrayExtraKey);
    }

    void requireAllowedAssociationsLocked(java.lang.String packageName) {
        ensureAllowedAssociations();
        if (this.mAllowedAssociations.get(packageName) == null) {
            this.mAllowedAssociations.put(packageName, new com.android.server.am.ActivityManagerService.PackageAssociationInfo(packageName, new android.util.ArraySet(), false));
        }
    }

    boolean validateAssociationAllowedLocked(java.lang.String pkg1, int uid1, java.lang.String pkg2, int uid2) {
        ensureAllowedAssociations();
        if (uid1 == uid2 || android.os.UserHandle.getAppId(uid1) == 1000 || android.os.UserHandle.getAppId(uid2) == 1000) {
            return true;
        }
        com.android.server.am.ActivityManagerService.PackageAssociationInfo pai = this.mAllowedAssociations.get(pkg1);
        if (pai != null && !pai.isPackageAssociationAllowed(pkg2)) {
            return false;
        }
        com.android.server.am.ActivityManagerService.PackageAssociationInfo pai2 = this.mAllowedAssociations.get(pkg2);
        if (pai2 == null || pai2.isPackageAssociationAllowed(pkg1)) {
            return true;
        }
        return false;
    }

    private void ensureAllowedAssociations() {
        if (this.mAllowedAssociations == null) {
            android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> allowedAssociations = com.android.server.SystemConfig.getInstance().getAllowedAssociations();
            this.mAllowedAssociations = new android.util.ArrayMap<>(allowedAssociations.size());
            getPackageManagerInternal();
            for (int i = 0; i < allowedAssociations.size(); i++) {
                java.lang.String pkg = allowedAssociations.keyAt(i);
                android.util.ArraySet<java.lang.String> asc = allowedAssociations.valueAt(i);
                boolean isDebuggable = false;
                try {
                    android.content.pm.ApplicationInfo ai = android.app.AppGlobals.getPackageManager().getApplicationInfo(pkg, 131072L, 0);
                    if (ai != null) {
                        isDebuggable = (ai.flags & 2) != 0;
                    }
                } catch (android.os.RemoteException e) {
                }
                this.mAllowedAssociations.put(pkg, new com.android.server.am.ActivityManagerService.PackageAssociationInfo(pkg, asc, isDebuggable));
            }
        }
    }

    private void updateAssociationForApp(android.content.pm.ApplicationInfo appInfo) {
        ensureAllowedAssociations();
        com.android.server.am.ActivityManagerService.PackageAssociationInfo pai = this.mAllowedAssociations.get(appInfo.packageName);
        if (pai != null) {
            pai.setDebuggable((appInfo.flags & 2) != 0);
        }
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        if (code == 1599295570) {
            java.util.ArrayList<android.os.IBinder> procs = new java.util.ArrayList<>();
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.ProcessRecord>> pmap = this.mProcessList.getProcessNamesLOSP().getMap();
                    int numOfNames = pmap.size();
                    for (int ip = 0; ip < numOfNames; ip++) {
                        android.util.SparseArray<com.android.server.am.ProcessRecord> apps = pmap.valueAt(ip);
                        int numOfApps = apps.size();
                        for (int ia = 0; ia < numOfApps; ia++) {
                            com.android.server.am.ProcessRecord app = apps.valueAt(ia);
                            android.app.IApplicationThread thread = app.getThread();
                            if (thread != null) {
                                procs.add(thread.asBinder());
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterProcLockedSection();
            int N = procs.size();
            for (int i = 0; i < N; i++) {
                android.os.Parcel data2 = android.os.Parcel.obtain();
                try {
                    procs.get(i).transact(1599295570, data2, null, 1);
                } catch (android.os.RemoteException e) {
                }
                data2.recycle();
            }
        }
        try {
            if (super.onTransact(code, data, reply, flags)) {
                return true;
            }
            try {
                return this.mActivityManagerServiceExt.hookOnTransact(code, data, reply, flags);
            } catch (java.lang.RuntimeException e2) {
                e = e2;
            }
        } catch (java.lang.RuntimeException e3) {
            e = e3;
        }
        if (!(e instanceof java.lang.SecurityException) && !(e instanceof java.lang.IllegalArgumentException) && !(e instanceof java.lang.IllegalStateException)) {
            android.util.Slog.wtf("ActivityManager", "Activity Manager Crash. UID:" + android.os.Binder.getCallingUid() + " PID:" + android.os.Binder.getCallingPid() + " TRANS:" + code, e);
        }
        throw e;
    }

    void updateCpuStats() {
        this.mAppProfiler.updateCpuStats();
    }

    void updateCpuStatsNow() {
        this.mAppProfiler.updateCpuStatsNow();
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.BatteryCallback
    public void batteryNeedsCpuUpdate() {
        updateCpuStatsNow();
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.BatteryCallback
    public void batteryPowerChanged(boolean onBattery) {
        updateCpuStatsNow();
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mOnBattery = com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER ? true : onBattery;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.BatteryCallback
    public void batteryStatsReset() {
    }

    @Override // com.android.server.power.stats.BatteryStatsImpl.BatteryCallback
    public void batterySendBroadcast(android.content.Intent intent) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                broadcastIntentLocked(null, null, null, intent, null, null, 0, null, null, null, null, null, -1, null, false, false, -1, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), -1);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    private android.util.ArrayMap<java.lang.String, android.os.IBinder> getCommonServicesLocked(boolean isolated) {
        if (isolated) {
            if (this.mIsolatedAppBindArgs == null) {
                this.mIsolatedAppBindArgs = new android.util.ArrayMap<>(1);
                addServiceToMap(this.mIsolatedAppBindArgs, "package");
                addServiceToMap(this.mIsolatedAppBindArgs, "permissionmgr");
            }
            return this.mIsolatedAppBindArgs;
        }
        if (this.mAppBindArgs == null) {
            this.mAppBindArgs = new android.util.ArrayMap<>();
            addServiceToMap(this.mAppBindArgs, "package");
            addServiceToMap(this.mAppBindArgs, "permissionmgr");
            addServiceToMap(this.mAppBindArgs, "window");
            addServiceToMap(this.mAppBindArgs, com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
            addServiceToMap(this.mAppBindArgs, "display");
            addServiceToMap(this.mAppBindArgs, "network_management");
            addServiceToMap(this.mAppBindArgs, "connectivity");
            addServiceToMap(this.mAppBindArgs, "accessibility");
            addServiceToMap(this.mAppBindArgs, "input_method");
            addServiceToMap(this.mAppBindArgs, com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT);
            addServiceToMap(this.mAppBindArgs, "graphicsstats");
            addServiceToMap(this.mAppBindArgs, "appops");
            addServiceToMap(this.mAppBindArgs, com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT);
            addServiceToMap(this.mAppBindArgs, "jobscheduler");
            addServiceToMap(this.mAppBindArgs, "notification");
            addServiceToMap(this.mAppBindArgs, "vibrator");
            addServiceToMap(this.mAppBindArgs, "account");
            addServiceToMap(this.mAppBindArgs, "power");
            addServiceToMap(this.mAppBindArgs, "user");
            addServiceToMap(this.mAppBindArgs, "mount");
            addServiceToMap(this.mAppBindArgs, "platform_compat");
            this.mActivityManagerServiceExt.addCustomServiceToMap();
        }
        return this.mAppBindArgs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addServiceToMap(android.util.ArrayMap<java.lang.String, android.os.IBinder> map, java.lang.String name) {
        android.os.IBinder service = android.os.ServiceManager.getService(name);
        if (service != null) {
            map.put(name, service);
        }
    }

    public void setFocusedRootTask(int taskId) {
        this.mActivityTaskManager.setFocusedRootTask(taskId);
    }

    public void registerTaskStackListener(android.app.ITaskStackListener listener) {
        this.mActivityTaskManager.registerTaskStackListener(listener);
    }

    public void unregisterTaskStackListener(android.app.ITaskStackListener listener) {
        this.mActivityTaskManager.unregisterTaskStackListener(listener);
    }

    final void updateLruProcessLocked(com.android.server.am.ProcessRecord app, boolean activityChange, com.android.server.am.ProcessRecord client) {
        this.mProcessList.updateLruProcessLocked(app, activityChange, client);
    }

    final void removeLruProcessLocked(com.android.server.am.ProcessRecord app) {
        this.mProcessList.removeLruProcessLocked(app);
    }

    final com.android.server.am.ProcessRecord getProcessRecordLocked(java.lang.String processName, int uid) {
        return this.mProcessList.getProcessRecordLocked(processName, uid);
    }

    final com.android.internal.app.ProcessMap<com.android.server.am.ProcessRecord> getProcessNamesLOSP() {
        return this.mProcessList.getProcessNamesLOSP();
    }

    void notifyPackageUse(java.lang.String packageName, int reason) {
        getPackageManagerInternal().notifyPackageUse(packageName, reason);
    }

    boolean startIsolatedProcess(java.lang.String entryPoint, java.lang.String[] entryPointArgs, java.lang.String processName, java.lang.String abiOverride, int uid, java.lang.Runnable crashHandler) {
        boolean z;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                android.content.pm.ApplicationInfo info = new android.content.pm.ApplicationInfo();
                info.uid = 1000;
                info.processName = processName;
                info.className = entryPoint;
                info.packageName = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
                info.seInfoUser = com.android.server.pm.pkg.SELinuxUtil.COMPLETE_STR;
                info.targetSdkVersion = android.os.Build.VERSION.SDK_INT;
                com.android.server.am.ProcessRecord proc = this.mProcessList.startProcessLocked(processName, info, false, 0, sNullHostingRecord, 0, true, true, uid, false, 0, null, abiOverride, entryPoint, entryPointArgs, crashHandler);
                z = proc != null;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return z;
    }

    final com.android.server.am.ProcessRecord startSdkSandboxProcessLocked(java.lang.String processName, android.content.pm.ApplicationInfo info, boolean knownToBeDead, int intentFlags, com.android.server.am.HostingRecord hostingRecord, int zygotePolicyFlags, int sdkSandboxUid, java.lang.String sdkSandboxClientAppPackage) {
        return this.mProcessList.startProcessLocked(processName, info, knownToBeDead, intentFlags, hostingRecord, zygotePolicyFlags, false, false, 0, true, sdkSandboxUid, sdkSandboxClientAppPackage, null, null, null, null);
    }

    final com.android.server.am.ProcessRecord startProcessLocked(java.lang.String processName, android.content.pm.ApplicationInfo info, boolean knownToBeDead, int intentFlags, com.android.server.am.HostingRecord hostingRecord, int zygotePolicyFlags, boolean allowWhileBooting, boolean isolated) {
        return this.mProcessList.startProcessLocked(processName, info, knownToBeDead, intentFlags, hostingRecord, zygotePolicyFlags, allowWhileBooting, isolated, 0, false, 0, null, null, null, null, null);
    }

    boolean isAllowedWhileBooting(android.content.pm.ApplicationInfo ai) {
        return (ai.flags & 8) != 0;
    }

    void updateBatteryStats(android.content.ComponentName activity, int uid, int userId, boolean resumed) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            android.util.Slog.d(TAG_SWITCH, "updateBatteryStats: comp=" + activity + "res=" + resumed);
        }
        this.mBatteryStatsService.updateBatteryStatsOnActivityUsage(activity.getPackageName(), activity.getShortClassName(), uid, userId, resumed);
        this.mBatteryStatsImplExt = this.mBatteryStatsService.getActiveStatistics().mBatteryStatsImplExt;
        synchronized (this.mBatteryStatsImplExt) {
            this.mBatteryStatsImplExt.noteActivityLocked(uid, activity, this.mOnBattery, null, android.os.SystemClock.elapsedRealtime(), new android.os.Handler(), activity.getPackageName(), resumed, this.mContext);
        }
    }

    public void updateActivityUsageStats(android.content.ComponentName activity, int userId, int event, android.os.IBinder appToken, android.content.ComponentName taskRoot, android.app.assist.ActivityId activityId) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
            android.util.Slog.d(TAG_SWITCH, "updateActivityUsageStats: comp=" + activity + " hash=" + appToken.hashCode() + " event=" + event);
        }
        if (this.mUsageStatsService != null) {
            this.mUsageStatsService.reportEvent(activity, userId, event, appToken.hashCode(), taskRoot);
            if (event == 1) {
                this.mUsageStatsService.reportEvent(activity.getPackageName(), userId, 31);
            }
        }
        com.android.server.contentcapture.ContentCaptureManagerInternal contentCaptureService = this.mContentCaptureService;
        if (contentCaptureService != null && (event == 2 || event == 1 || event == 23 || event == 24)) {
            contentCaptureService.notifyActivityEvent(userId, activity, event, activityId);
        }
        if (this.mVoiceInteractionManagerProvider != null && event == 24) {
            this.mVoiceInteractionManagerProvider.notifyActivityDestroyed(appToken);
        }
    }

    public void updateActivityUsageStats(java.lang.String packageName, int userId, int event) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
            android.util.Slog.d(TAG_SWITCH, "updateActivityUsageStats: package=" + packageName + " event=" + event);
        }
        if (this.mUsageStatsService != null) {
            this.mUsageStatsService.reportEvent(packageName, userId, event);
        }
    }

    void updateForegroundServiceUsageStats(android.content.ComponentName service, int userId, boolean started) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
            android.util.Slog.d(TAG_SWITCH, "updateForegroundServiceUsageStats: comp=" + service + " started=" + started);
        }
        if (this.mUsageStatsService != null) {
            this.mUsageStatsService.reportEvent(service, userId, started ? 19 : 20, 0, null);
        }
    }

    android.content.res.CompatibilityInfo compatibilityInfoForPackage(android.content.pm.ApplicationInfo ai) {
        return this.mAtmInternal.compatibilityInfoForPackage(ai);
    }

    void enforceNotIsolatedCaller(java.lang.String caller) {
        if (android.os.UserHandle.isIsolated(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Isolated process not allowed to call " + caller);
        }
    }

    void enforceNotIsolatedOrSdkSandboxCaller(java.lang.String caller) {
        enforceNotIsolatedCaller(caller);
        if (android.os.Process.isSdkSandboxUid(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("SDK sandbox process not allowed to call " + caller);
        }
    }

    private void enforceAllowedToStartOrBindServiceIfSdkSandbox(android.content.Intent intent) {
        if (android.os.Process.isSdkSandboxUid(android.os.Binder.getCallingUid())) {
            com.android.server.sdksandbox.SdkSandboxManagerLocal sdkSandboxManagerLocal = (com.android.server.sdksandbox.SdkSandboxManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.sdksandbox.SdkSandboxManagerLocal.class);
            if (sdkSandboxManagerLocal != null) {
                sdkSandboxManagerLocal.enforceAllowedToStartOrBindService(intent);
                return;
            }
            throw new java.lang.IllegalStateException("SdkSandboxManagerLocal not found when checking whether SDK sandbox uid may start or bind to a service.");
        }
    }

    private void enforceCallingPackage(java.lang.String packageName, int callingUid) {
        int userId = android.os.UserHandle.getUserId(callingUid);
        int packageUid = getPackageManagerInternal().getPackageUid(packageName, 0L, userId);
        if (packageUid != callingUid) {
            throw new java.lang.SecurityException(packageName + " does not belong to uid " + callingUid);
        }
    }

    public void setPackageScreenCompatMode(java.lang.String packageName, int mode) {
        this.mActivityTaskManager.setPackageScreenCompatMode(packageName, mode);
    }

    private boolean hasUsageStatsPermission(java.lang.String callingPackage, int callingUid, int callingPid) {
        int mode = this.mAppOpsService.noteOperation(43, callingUid, callingPackage, null, false, "", false).getOpMode();
        return mode == 3 ? checkPermission("android.permission.PACKAGE_USAGE_STATS", callingPid, callingUid) == 0 : mode == 0;
    }

    private boolean hasUsageStatsPermission(java.lang.String callingPackage) {
        return hasUsageStatsPermission(callingPackage, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
    }

    private void enforceUsageStatsPermission(java.lang.String callingPackage, int callingUid, int callingPid, java.lang.String operation) {
        if (!hasUsageStatsPermission(callingPackage, callingUid, callingPid)) {
            java.lang.String errorMsg = "Permission denial for <" + operation + "> from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " which requires PACKAGE_USAGE_STATS permission";
            throw new java.lang.SecurityException(errorMsg);
        }
    }

    public int getPackageProcessState(final java.lang.String packageName, java.lang.String callingPackage) {
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "getPackageProcessState");
        }
        final int[] procState = {20};
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.am.ActivityManagerService.lambda$getPackageProcessState$0(procState, packageName, (com.android.server.am.ProcessRecord) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return procState[0];
    }

    static /* synthetic */ void lambda$getPackageProcessState$0(int[] procState, java.lang.String packageName, com.android.server.am.ProcessRecord proc) {
        if (procState[0] > proc.mState.getSetProcState()) {
            if (proc.getPkgList().containsKey(packageName) || (proc.getPkgDeps() != null && proc.getPkgDeps().contains(packageName))) {
                procState[0] = proc.mState.getSetProcState();
            }
        }
    }

    public boolean setProcessMemoryTrimLevel(java.lang.String process, int userId, int level) throws android.os.RemoteException {
        if (!isCallerShell()) {
            throw new java.lang.SecurityException("Only shell can call it");
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ProcessRecord app = findProcessLOSP(process, userId, "setProcessMemoryTrimLevel");
                if (app == null) {
                    throw new java.lang.IllegalArgumentException("Unknown process: " + process);
                }
                android.app.IApplicationThread thread = app.getThread();
                if (thread == null) {
                    throw new java.lang.IllegalArgumentException("Process has no app thread");
                }
                if (app.mProfile.getTrimMemoryLevel() >= level) {
                    throw new java.lang.IllegalArgumentException("Unable to set a higher trim level than current level");
                }
                if (level >= 20 && app.mState.getCurProcState() <= 6) {
                    throw new java.lang.IllegalArgumentException("Unable to set a background trim level on a foreground process");
                }
                thread.scheduleTrimMemory(level);
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        app.mProfile.setTrimMemoryLevel(level);
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
        return true;
    }

    void dispatchOomAdjObserver(java.lang.String msg) {
        com.android.server.am.ActivityManagerService.OomAdjObserver observer;
        synchronized (this.mOomAdjObserverLock) {
            observer = this.mCurOomAdjObserver;
        }
        if (observer != null) {
            observer.onOomAdjMessage(msg);
        }
    }

    void setOomAdjObserver(int uid, com.android.server.am.ActivityManagerService.OomAdjObserver observer) {
        synchronized (this.mOomAdjObserverLock) {
            this.mCurOomAdjUid = uid;
            this.mCurOomAdjObserver = observer;
        }
    }

    void clearOomAdjObserver() {
        synchronized (this.mOomAdjObserverLock) {
            this.mCurOomAdjUid = -1;
            this.mCurOomAdjObserver = null;
        }
    }

    void reportUidInfoMessageLocked(java.lang.String tag, java.lang.String msg, int uid) {
        android.util.Slog.i("ActivityManager", msg);
        synchronized (this.mOomAdjObserverLock) {
            if (this.mCurOomAdjObserver != null && uid == this.mCurOomAdjUid) {
                this.mUiHandler.obtainMessage(70, msg).sendToTarget();
            }
        }
    }

    @java.lang.Deprecated
    public int startActivity(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions) {
        return this.mActivityTaskManager.startActivity(caller, callingPackage, null, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, bOptions);
    }

    public int startActivityWithFeature(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions) {
        return this.mActivityTaskManager.startActivity(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, bOptions);
    }

    @java.lang.Deprecated
    public final int startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, int userId) {
        return startActivityAsUserWithFeature(caller, callingPackage, null, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, bOptions, userId);
    }

    public final int startActivityAsUserWithFeature(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, int userId) {
        return this.mActivityTaskManager.startActivityAsUser(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, bOptions, userId);
    }

    android.app.WaitResult startActivityAndWait(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, int userId) {
        return this.mActivityTaskManager.startActivityAndWait(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, bOptions, userId);
    }

    public final int startActivityFromRecents(int taskId, android.os.Bundle bOptions) {
        return this.mActivityTaskManager.startActivityFromRecents(taskId, bOptions);
    }

    public int startActivityAsUserEmpty(android.os.Bundle options) {
        android.content.Intent intent_l;
        android.content.pm.ActivityInfo aInfo;
        java.util.ArrayList<java.lang.String> pApps = options.getStringArrayList("start_empty_apps");
        if (pApps != null && pApps.size() > 0) {
            for (java.lang.String app_str : pApps) {
                if (app_str != null) {
                    boostPriorityForLockedSection();
                    synchronized (this) {
                        try {
                            try {
                                intent_l = this.mContext.getPackageManager().getLaunchIntentForPackage(app_str);
                            } catch (java.lang.Throwable th) {
                                resetPriorityAfterLockedSection();
                                throw th;
                            }
                        } catch (java.lang.Exception e) {
                            e = e;
                        }
                        if (intent_l == null) {
                            resetPriorityAfterLockedSection();
                        } else {
                            try {
                                aInfo = this.mTaskSupervisor.resolveActivity(intent_l, null, 0, null, 0, 0, android.os.Binder.getCallingPid());
                            } catch (java.lang.Exception e2) {
                                e = e2;
                                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                                    android.util.Slog.w("ActivityManager", "Exception raised trying to start app as empty " + e);
                                }
                            }
                            if (aInfo == null) {
                                resetPriorityAfterLockedSection();
                            } else {
                                com.android.server.am.ProcessRecord empty_app = startProcessLocked(app_str, aInfo.applicationInfo, false, 0, sNullHostingRecord, 0, false, false);
                                if (empty_app != null) {
                                    updateOomAdjLocked(empty_app, 0);
                                }
                                resetPriorityAfterLockedSection();
                            }
                        }
                    }
                }
            }
            return 1;
        }
        return 1;
    }

    public final boolean finishActivity(android.os.IBinder token, int resultCode, android.content.Intent resultData, int finishTask) {
        return android.app.ActivityClient.getInstance().finishActivity(token, resultCode, resultData, finishTask);
    }

    public void setRequestedOrientation(android.os.IBinder token, int requestedOrientation) {
        android.app.ActivityClient.getInstance().setRequestedOrientation(token, requestedOrientation);
    }

    public final void finishHeavyWeightApp() {
        if (checkCallingPermission("android.permission.FORCE_STOP_PACKAGES") != 0) {
            java.lang.String msg = "Permission Denial: finishHeavyWeightApp() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.FORCE_STOP_PACKAGES";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        this.mAtmInternal.finishHeavyWeightApp();
    }

    public void crashApplicationWithType(int uid, int initialPid, java.lang.String packageName, int userId, java.lang.String message, boolean force, int exceptionTypeId) {
        crashApplicationWithTypeWithExtras(uid, initialPid, packageName, userId, message, force, exceptionTypeId, null);
    }

    public void crashApplicationWithTypeWithExtras(int uid, int initialPid, java.lang.String packageName, int userId, java.lang.String message, boolean force, int exceptionTypeId, android.os.Bundle extras) {
        if (checkCallingPermission("android.permission.FORCE_STOP_PACKAGES") != 0) {
            java.lang.String msg = "Permission Denial: crashApplication() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.FORCE_STOP_PACKAGES";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mAppErrors.scheduleAppCrashLocked(uid, initialPid, packageName, userId, message, force, exceptionTypeId, extras);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    final void handleAppDiedLocked(final com.android.server.am.ProcessRecord app, int pid, boolean restarting, boolean allowRestart, boolean fromBinderDied) {
        boolean kept = cleanUpApplicationRecordLocked(app, pid, restarting, allowRestart, -1, false, fromBinderDied);
        this.mSocExt.onNotifyAppCrash(pid, app.uid, app.info.packageName);
        if (!kept && !restarting) {
            removeLruProcessLocked(app);
            if (pid > 0) {
                com.android.server.am.ProcessList.remove(pid);
            }
        }
        this.mAppProfiler.onAppDiedLocked(app);
        if (app.info != null) {
            this.mActivityManagerServiceExt.activityPreloadHandleAppDied(app.info.packageName, app.info.uid, pid);
        }
        this.mAtmInternal.handleAppDied(app.getWindowProcessController(), restarting, new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$handleAppDiedLocked$1(app);
            }
        });
        if (app.info != null) {
            this.mActivityManagerServiceExt.handleAppDiedLocked(app.info.packageName, app.getWindowProcessController(), pid);
        }
        app.getWrapper().getExtImpl().setExplicitDisableRestart(false);
        if (!LTW_DISABLE) {
            this.mActivityTaskManager.getWrapper().getExtImpl().getRemoteTaskManager().handleProcessDied(app.getWindowProcessController());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleAppDiedLocked$1(com.android.server.am.ProcessRecord app) throws java.lang.Throwable {
        android.util.Slog.w("ActivityManager", "Crash of app " + app.processName + " running instrumentation " + app.getActiveInstrumentation().mClass);
        android.os.Bundle info = new android.os.Bundle();
        info.putString("shortMsg", "Process crashed.");
        finishInstrumentationLocked(app, 0, info);
    }

    com.android.server.am.ProcessRecord getRecordForAppLOSP(android.app.IApplicationThread thread) {
        if (thread == null) {
            return null;
        }
        return getRecordForAppLOSP(thread.asBinder());
    }

    com.android.server.am.ProcessRecord getRecordForAppLOSP(android.os.IBinder threadBinder) {
        if (threadBinder == null) {
            return null;
        }
        com.android.server.am.ProcessRecord record = this.mProcessList.getLRURecordForAppLOSP(threadBinder);
        if (record != null) {
            return record;
        }
        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.ProcessRecord>> pmap = this.mProcessList.getProcessNamesLOSP().getMap();
        for (int i = pmap.size() - 1; i >= 0; i--) {
            android.util.SparseArray<com.android.server.am.ProcessRecord> procs = pmap.valueAt(i);
            for (int j = procs.size() - 1; j >= 0; j--) {
                com.android.server.am.ProcessRecord proc = procs.valueAt(j);
                android.app.IApplicationThread procThread = proc.getThread();
                if (procThread != null && procThread.asBinder() == threadBinder) {
                    if (!proc.isPendingFinishAttach()) {
                        android.util.Slog.wtf("ActivityManager", "getRecordForApp: exists in name list but not in LRU list: " + proc);
                    }
                    return proc;
                }
            }
        }
        return null;
    }

    final void appDiedLocked(com.android.server.am.ProcessRecord app, java.lang.String reason) throws java.lang.Throwable {
        appDiedLocked(app, app.getPid(), app.getThread(), false, reason);
    }

    /* JADX WARN: Removed duplicated region for block: B:113:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x02f6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final void appDiedLocked(com.android.server.am.ProcessRecord r18, int r19, android.app.IApplicationThread r20, boolean r21, java.lang.String r22) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 777
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.appDiedLocked(com.android.server.am.ProcessRecord, int, android.app.IApplicationThread, boolean, java.lang.String):void");
    }

    public boolean clearApplicationUserData(java.lang.String packageName, boolean keepState, android.content.pm.IPackageDataObserver observer, int userId) {
        return clearApplicationUserData(packageName, keepState, false, observer, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clearApplicationUserData(java.lang.String packageName, boolean keepState, final boolean isRestore, final android.content.pm.IPackageDataObserver observer, int userId) throws java.lang.Throwable {
        boolean permitted;
        boolean permitted2;
        android.content.pm.ApplicationInfo applicationInfo;
        int i;
        boolean z;
        int pid;
        enforceNotIsolatedCaller("clearApplicationUserData");
        int uid = android.os.Binder.getCallingUid();
        int pid2 = android.os.Binder.getCallingPid();
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.AM_CLEAR_APP_DATA_CALLER, java.lang.Integer.valueOf(pid2), java.lang.Integer.valueOf(uid), packageName);
        this.mActivityManagerServiceExt.hookInterceptClearUserDataIfNeeded(packageName);
        final int resolvedUserId = this.mUserController.handleIncomingUser(pid2, uid, userId, false, 2, "clearApplicationUserData", null);
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
            if (getPackageManagerInternal().isPackageDataProtected(resolvedUserId, packageName)) {
                try {
                    if (android.app.ActivityManager.checkUidPermission("android.permission.MANAGE_USERS", uid) == 0) {
                        throw new java.lang.SecurityException("Cannot clear data for a protected package: " + packageName);
                    }
                    permitted = false;
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } else {
                permitted = true;
            }
            android.content.pm.ApplicationInfo applicationInfo2 = null;
            if (permitted) {
                try {
                    applicationInfo2 = pm.getApplicationInfo(packageName, 8192L, resolvedUserId);
                } catch (android.os.RemoteException e) {
                }
                boolean permitted3 = (applicationInfo2 != null && applicationInfo2.uid == uid) || checkComponentPermission("android.permission.CLEAR_APP_USER_DATA", pid2, uid, -1, true) == 0;
                permitted2 = permitted3;
                applicationInfo = applicationInfo2;
            } else {
                permitted2 = permitted;
                applicationInfo = null;
            }
            if (permitted2) {
                final android.content.pm.ApplicationInfo appInfo = applicationInfo;
                try {
                    boolean hasInstantMetadata = getPackageManagerInternal().hasInstantApplicationMetadata(packageName, resolvedUserId);
                    boolean isUninstalledAppWithoutInstantMetadata = appInfo == null && !hasInstantMetadata;
                    final boolean isInstantApp = (appInfo != null && appInfo.isInstantApp()) || hasInstantMetadata;
                    boolean canAccessInstantApps = checkComponentPermission("android.permission.ACCESS_INSTANT_APPS", pid2, uid, -1, true) == 0;
                    try {
                        if (isUninstalledAppWithoutInstantMetadata) {
                            z = false;
                            pid = pid2;
                        } else if (!isInstantApp || canAccessInstantApps) {
                            try {
                                if (this.mActivityManagerServiceExt.forbidClearAppUserData(packageName, observer, userId)) {
                                    android.os.Binder.restoreCallingIdentity(callingId);
                                    return false;
                                }
                                boostPriorityForLockedSection();
                                synchronized (this) {
                                    if (appInfo != null) {
                                        try {
                                            forceStopPackageLocked(packageName, appInfo.uid, "clear data");
                                            this.mAtmInternal.removeRecentTasksByPackageName(packageName, resolvedUserId);
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            while (true) {
                                                try {
                                                    resetPriorityAfterLockedSection();
                                                    throw th;
                                                } catch (java.lang.Throwable th3) {
                                                    th = th3;
                                                }
                                            }
                                        }
                                    }
                                    try {
                                        resetPriorityAfterLockedSection();
                                        try {
                                            try {
                                                pm.clearApplicationUserData(packageName, new android.content.pm.IPackageDataObserver.Stub() { // from class: com.android.server.am.ActivityManagerService.6
                                                    public void onRemoveCompleted(java.lang.String packageName2, boolean succeeded) throws android.os.RemoteException {
                                                        if (appInfo != null) {
                                                            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                                                            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                                                            synchronized (activityManagerService) {
                                                                try {
                                                                    com.android.server.am.ActivityManagerService.this.finishForceStopPackageLocked(packageName2, appInfo.uid);
                                                                } catch (java.lang.Throwable th4) {
                                                                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                                                    throw th4;
                                                                }
                                                            }
                                                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                                        }
                                                        if (succeeded) {
                                                            com.android.server.am.ActivityManagerService.this.mPackageManagerInt.sendPackageDataClearedBroadcast(packageName2, appInfo != null ? appInfo.uid : -1, resolvedUserId, isRestore, isInstantApp);
                                                        }
                                                        if (observer != null) {
                                                            observer.onRemoveCompleted(packageName2, succeeded);
                                                        }
                                                    }
                                                }, resolvedUserId);
                                                if (appInfo != null) {
                                                    if (!keepState) {
                                                        this.mUgmInternal.removeUriPermissionsForPackage(packageName, resolvedUserId, true, false);
                                                        android.app.INotificationManager inm = android.app.NotificationManager.getService();
                                                        try {
                                                            inm.clearData(packageName, appInfo.uid, uid == appInfo.uid);
                                                        } catch (android.os.RemoteException e2) {
                                                        }
                                                    }
                                                    com.android.server.job.JobSchedulerInternal js = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
                                                    js.cancelJobsForUid(appInfo.uid, true, 13, 8, "clear data");
                                                    com.android.server.AlarmManagerInternal ami = (com.android.server.AlarmManagerInternal) com.android.server.LocalServices.getService(com.android.server.AlarmManagerInternal.class);
                                                    ami.removeAlarmsForUid(appInfo.uid);
                                                }
                                            } catch (android.os.RemoteException e3) {
                                            }
                                            android.os.Binder.restoreCallingIdentity(callingId);
                                            return true;
                                        } catch (java.lang.Throwable th4) {
                                            th = th4;
                                        }
                                    } catch (java.lang.Throwable th5) {
                                        th = th5;
                                        while (true) {
                                            resetPriorityAfterLockedSection();
                                            throw th;
                                        }
                                    }
                                }
                            } catch (java.lang.Throwable th6) {
                                th = th6;
                                i = pid2;
                            }
                        } else {
                            z = false;
                            pid = pid2;
                        }
                        android.util.Slog.w("ActivityManager", "Invalid packageName: " + packageName);
                        boolean z2 = z;
                        try {
                            if (observer != null) {
                                try {
                                    observer.onRemoveCompleted(packageName, z2);
                                } catch (android.os.RemoteException e4) {
                                    android.util.Slog.i("ActivityManager", "Observer no longer exists.");
                                }
                            }
                            android.os.Binder.restoreCallingIdentity(callingId);
                            return z2;
                        } catch (java.lang.Throwable th7) {
                            th = th7;
                            android.os.Binder.restoreCallingIdentity(callingId);
                            throw th;
                        }
                    } catch (java.lang.Throwable th8) {
                        th = th8;
                    }
                } catch (java.lang.Throwable th9) {
                    th = th9;
                    i = pid2;
                }
            } else {
                try {
                    try {
                        throw new java.lang.SecurityException("PID " + pid2 + " does not have permission android.permission.CLEAR_APP_USER_DATA to clear data of package " + packageName);
                    } catch (java.lang.Throwable th10) {
                        th = th10;
                    }
                } catch (java.lang.Throwable th11) {
                    th = th11;
                }
            }
        } catch (java.lang.Throwable th12) {
            th = th12;
        }
        android.os.Binder.restoreCallingIdentity(callingId);
        throw th;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:72:? -> B:49:0x0118). Please report as a decompilation issue!!! */
    public void killBackgroundProcesses(java.lang.String packageName, int userId) throws java.lang.Throwable {
        int appId;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock;
        int i;
        int i2;
        android.content.pm.IPackageManager pm;
        int callingAppId;
        if (checkCallingPermission("android.permission.KILL_BACKGROUND_PROCESSES") != 0 && checkCallingPermission("android.permission.RESTART_PACKAGES") != 0) {
            java.lang.String msg = "Permission Denial: killBackgroundProcesses() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.KILL_BACKGROUND_PROCESSES";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        boolean hasKillAllPermission = checkCallingPermission("android.permission.KILL_ALL_BACKGROUND_PROCESSES") == 0;
        int callingUid = android.os.Binder.getCallingUid();
        int callingAppId2 = android.os.UserHandle.getAppId(callingUid);
        if (this.mActivityManagerServiceExt.killBackgroundProcessFilter(packageName, android.os.Binder.getCallingUid())) {
            return;
        }
        int[] userIds = this.mUserController.expandUserId(this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, true, 2, "killBackgroundProcesses", null));
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.IPackageManager pm2 = android.app.AppGlobals.getPackageManager();
            int length = userIds.length;
            int i3 = 0;
            while (i3 < length) {
                int targetUserId = userIds[i3];
                try {
                    int appId2 = android.os.UserHandle.getAppId(pm2.getPackageUid(packageName, 268435456L, targetUserId));
                    appId = appId2;
                } catch (android.os.RemoteException e) {
                    appId = -1;
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(callingId);
                    throw th;
                }
                if (appId != -1 && (hasKillAllPermission || appId == callingAppId2)) {
                    boostPriorityForLockedSection();
                    synchronized (this) {
                        try {
                            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mProcLock;
                            boostPriorityForProcLockedSection();
                            synchronized (activityManagerGlobalLock2) {
                                try {
                                    activityManagerGlobalLock = activityManagerGlobalLock2;
                                    i = i3;
                                    i2 = length;
                                    pm = pm2;
                                    callingAppId = callingAppId2;
                                    try {
                                        this.mProcessList.killPackageProcessesLSP(packageName, appId, targetUserId, 500, 10, 24, "kill background");
                                        try {
                                            resetPriorityAfterProcLockedSection();
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            resetPriorityAfterLockedSection();
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                        resetPriorityAfterProcLockedSection();
                                        throw th;
                                    }
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    activityManagerGlobalLock = activityManagerGlobalLock2;
                                    resetPriorityAfterProcLockedSection();
                                    throw th;
                                }
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    }
                    try {
                        resetPriorityAfterLockedSection();
                        i3 = i + 1;
                        length = i2;
                        pm2 = pm;
                        callingAppId2 = callingAppId;
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                        android.os.Binder.restoreCallingIdentity(callingId);
                        throw th;
                    }
                }
                android.util.Slog.w("ActivityManager", "Invalid packageName: " + packageName);
                android.os.Binder.restoreCallingIdentity(callingId);
                return;
            }
            android.os.Binder.restoreCallingIdentity(callingId);
        } catch (java.lang.Throwable th7) {
            th = th7;
        }
    }

    public void killAllBackgroundProcesses() {
        if (checkCallingPermission("android.permission.KILL_ALL_BACKGROUND_PROCESSES") != 0) {
            java.lang.String msg = "Permission Denial: killAllBackgroundProcesses() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.KILL_ALL_BACKGROUND_PROCESSES";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        int binderCallingUid = android.os.Binder.getCallingUid();
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    this.mAppProfiler.setAllowLowerMemLevelLocked(true);
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            this.mKillBackgroundProcessesCallingUid = binderCallingUid;
                            this.mProcessList.killPackageProcessesLSP(null, -1, -1, 900, 10, 24, "kill all background");
                            this.mKillBackgroundProcessesCallingUid = -1;
                        } catch (java.lang.Throwable th) {
                            resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    resetPriorityAfterProcLockedSection();
                    this.mAppProfiler.doLowMemReportIfNeededLocked(null);
                } catch (java.lang.Throwable th2) {
                    resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    void killAllBackgroundProcessesExcept(int minTargetSdk, int maxProcState) {
        if (checkCallingPermission("android.permission.KILL_ALL_BACKGROUND_PROCESSES") != 0) {
            java.lang.String msg = "Permission Denial: killAllBackgroundProcessesExcept() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.KILL_ALL_BACKGROUND_PROCESSES";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            this.mProcessList.killAllBackgroundProcessesExceptLSP(minTargetSdk, maxProcState);
                        } catch (java.lang.Throwable th) {
                            resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    resetPriorityAfterProcLockedSection();
                } catch (java.lang.Throwable th2) {
                    resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void stopAppForUser(java.lang.String packageName, int userId) {
        if (checkCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS") != 0) {
            java.lang.String msg = "Permission Denial: stopAppForUser() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.MANAGE_ACTIVITY_TASKS";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        int callingPid = android.os.Binder.getCallingPid();
        int userId2 = this.mUserController.handleIncomingUser(callingPid, android.os.Binder.getCallingUid(), userId, true, 2, "stopAppForUser", null);
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            stopAppForUserInternal(packageName, userId2);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public boolean registerForegroundServiceObserver(android.app.IForegroundServiceObserver callback) {
        boolean zRegisterForegroundServiceObserverLocked;
        int callingUid = android.os.Binder.getCallingUid();
        int permActivityTasks = checkCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS");
        int permAcrossUsersFull = checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL");
        if (permActivityTasks != 0 || permAcrossUsersFull != 0) {
            java.lang.String msg = "Permission Denial: registerForegroundServiceObserver() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + callingUid + " requires android.permission.MANAGE_ACTIVITY_TASKS and android.permission.INTERACT_ACROSS_USERS_FULL";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                zRegisterForegroundServiceObserverLocked = this.mServices.registerForegroundServiceObserverLocked(callingUid, callback);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return zRegisterForegroundServiceObserverLocked;
    }

    public void forceStopPackage(java.lang.String packageName, int userId) {
        forceStopPackage(packageName, userId, 0, null);
    }

    public void forceStopPackageEvenWhenStopping(java.lang.String packageName, int userId) {
        forceStopPackage(packageName, userId, 1, null);
    }

    private void forceStopPackage(java.lang.String packageName, int userId, int userRunningFlags, java.lang.String reason) {
        int[] users;
        int pkgUid;
        int i;
        int i2;
        int[] users2;
        int i3;
        android.content.pm.IPackageManager pm;
        java.lang.String description;
        com.android.server.am.ProcessRecord callerApp;
        if (checkCallingPermission("android.permission.FORCE_STOP_PACKAGES") != 0) {
            java.lang.String msg = "Permission Denial: forceStopPackage() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.FORCE_STOP_PACKAGES";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        int callingPid = android.os.Binder.getCallingPid();
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
            android.util.Slog.i("ActivityManager", "forceStopPackage callingPid " + callingPid + " " + android.os.Debug.getCallers(4));
        }
        int userId2 = this.mUserController.handleIncomingUser(callingPid, android.os.Binder.getCallingUid(), userId, true, 2, "forceStopPackage", null);
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.IPackageManager pm2 = android.app.AppGlobals.getPackageManager();
            boostPriorityForLockedSection();
            synchronized (this) {
                int i4 = -1;
                if (userId2 == -1) {
                    try {
                        users = this.mUserController.getUsers();
                    } catch (java.lang.Throwable th) {
                        th = th;
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                } else {
                    try {
                        users = new int[]{userId2};
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                int[] users3 = users;
                int length = users3.length;
                int i5 = 0;
                while (i5 < length) {
                    int user = users3[i5];
                    if (getPackageManagerInternal().isPackageStateProtected(packageName, user)) {
                        android.util.Slog.w("ActivityManager", "Ignoring request to force stop protected package " + packageName + " u" + user);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    try {
                        int pkgUid2 = pm2.getPackageUid(packageName, 268435456L, user);
                        pkgUid = pkgUid2;
                    } catch (android.os.RemoteException e) {
                        pkgUid = -1;
                    }
                    if (pkgUid == i4) {
                        android.util.Slog.w("ActivityManager", "Invalid packageName: " + packageName);
                        i = i5;
                        i2 = length;
                        users2 = users3;
                        i3 = i4;
                        pm = pm2;
                    } else {
                        try {
                            pm2.setPackageStoppedState(packageName, true, user);
                        } catch (android.os.RemoteException e2) {
                        } catch (java.lang.IllegalArgumentException e3) {
                            android.util.Slog.w("ActivityManager", "Failed trying to unstop package " + packageName + ": " + e3);
                        }
                        if (this.mUserController.isUserRunning(user, userRunningFlags)) {
                            if (reason == null) {
                                java.lang.String description2 = "from pid " + callingPid;
                                synchronized (this.mPidsSelfLocked) {
                                    callerApp = this.mPidsSelfLocked.get(callingPid);
                                }
                                if (callerApp != null) {
                                    description2 = description2 + " (" + callerApp.processName + ")";
                                }
                                description = description2;
                            } else {
                                description = reason;
                            }
                            int pkgUid3 = pkgUid;
                            i = i5;
                            i2 = length;
                            users2 = users3;
                            i3 = i4;
                            pm = pm2;
                            try {
                                forceStopPackageLocked(packageName, android.os.UserHandle.getAppId(pkgUid), false, false, true, false, false, true, user, description);
                                finishForceStopPackageLocked(packageName, pkgUid3);
                                this.mActivityManagerServiceExt.sendApplicationStopByForceStop(this.mHandler, callingPid, this.mContext, packageName, this.mPidsSelfLocked);
                                this.mActivityManagerServiceExt.sendForcestopInfoToPreload(packageName, callingPid, userId2);
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                resetPriorityAfterLockedSection();
                                throw th;
                            }
                        } else {
                            i = i5;
                            i2 = length;
                            users2 = users3;
                            i3 = i4;
                            pm = pm2;
                        }
                    }
                    i5 = i + 1;
                    users3 = users2;
                    i4 = i3;
                    length = i2;
                    pm2 = pm;
                }
                resetPriorityAfterLockedSection();
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void addPackageDependency(java.lang.String packageName) {
        com.android.server.am.ProcessRecord proc;
        int callingPid = android.os.Binder.getCallingPid();
        if (callingPid == android.os.Process.myPid()) {
            return;
        }
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (getPackageManagerInternal().filterAppAccess(packageName, callingUid, callingUserId)) {
            android.util.Slog.w("ActivityManager", "Failed trying to add dependency on non-existing package: " + packageName);
            return;
        }
        synchronized (this.mPidsSelfLocked) {
            proc = this.mPidsSelfLocked.get(android.os.Binder.getCallingPid());
        }
        if (proc != null) {
            android.util.ArraySet<java.lang.String> pkgDeps = proc.getPkgDeps();
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        if (pkgDeps == null) {
                            try {
                                android.util.ArraySet<java.lang.String> arraySet = new android.util.ArraySet<>(1);
                                pkgDeps = arraySet;
                                proc.setPkgDeps(arraySet);
                            } catch (java.lang.Throwable th) {
                                resetPriorityAfterProcLockedSection();
                                throw th;
                            }
                        }
                        pkgDeps.add(packageName);
                    }
                    resetPriorityAfterProcLockedSection();
                } catch (java.lang.Throwable th2) {
                    resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            resetPriorityAfterLockedSection();
        }
    }

    public void killApplication(java.lang.String pkg, int appId, int userId, java.lang.String reason, int exitInfoReason) {
        if (pkg == null) {
            return;
        }
        if (appId < 0) {
            android.util.Slog.w("ActivityManager", "Invalid appid specified for pkg : " + pkg);
            return;
        }
        int callerUid = android.os.Binder.getCallingUid();
        if (android.os.UserHandle.getAppId(callerUid) == 1000) {
            android.os.Message msg = this.mHandler.obtainMessage(22);
            msg.arg1 = appId;
            msg.arg2 = userId;
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = pkg;
            args.arg2 = reason;
            args.arg3 = java.lang.Integer.valueOf(exitInfoReason);
            msg.obj = args;
            if ("vold reset".equals(reason)) {
                this.mHandler.sendMessageAtFrontOfQueue(msg);
                return;
            } else {
                this.mHandler.sendMessage(msg);
                return;
            }
        }
        throw new java.lang.SecurityException(callerUid + " cannot kill pkg: " + pkg);
    }

    public void closeSystemDialogs(java.lang.String reason) {
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            android.util.Slog.d("ActivityManager", "Process " + callingPid + " uid " + callingUid + " call closeSystemDialogs.");
        }
        android.os.Trace.traceBegin(64L, "closeSystemDialogs pid " + callingPid + " uid " + callingUid);
        this.mAtmInternal.closeSystemDialogs(reason);
        android.os.Trace.traceEnd(64L);
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x0103  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:180:? -> B:106:0x01ba). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] r44) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 596
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.getProcessMemoryInfo(int[]):android.os.Debug$MemoryInfo[]");
    }

    public long[] getProcessPss(int[] pids) {
        com.android.server.am.ProcessRecord proc;
        int oomAdj;
        enforceNotIsolatedCaller("getProcessPss");
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(callingUid);
        boolean allUsers = android.app.ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) == 0;
        boolean allUids = this.mAtmInternal.isGetTasksAllowed("getProcessPss", callingPid, callingUid);
        long[] pss = new long[pids.length];
        for (int i = pids.length - 1; i >= 0; i--) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    synchronized (this.mPidsSelfLocked) {
                        proc = this.mPidsSelfLocked.get(pids[i]);
                        oomAdj = proc != null ? proc.mState.getSetAdj() : 0;
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterProcLockedSection();
            if (allUids && (allUsers || android.os.UserHandle.getUserId(proc.uid) == userId)) {
                long[] tmpUss = new long[3];
                long startTime = android.os.SystemClock.currentThreadTimeMillis();
                long pi = android.os.Debug.getPss(pids[i], tmpUss, null);
                pss[i] = pi;
                long duration = android.os.SystemClock.currentThreadTimeMillis() - startTime;
                if (proc != null) {
                    com.android.server.am.ProcessProfileRecord profile = proc.mProfile;
                    synchronized (this.mAppProfiler.mProfilerLock) {
                        if (profile.getThread() != null && profile.getSetAdj() == oomAdj) {
                            profile.addPss(pi, tmpUss[0], tmpUss[2], false, 3, duration);
                        }
                    }
                }
            }
        }
        return pss;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0048 A[Catch: all -> 0x006f, TRY_ENTER, TryCatch #1 {all -> 0x006f, blocks: (B:13:0x0035, B:15:0x003b, B:18:0x0042, B:23:0x006a, B:22:0x0048), top: B:32:0x0035 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void killApplicationProcess(java.lang.String r6, int r7) {
        /*
            r5 = this;
            if (r6 != 0) goto L3
            return
        L3:
            int r0 = android.os.Binder.getCallingUid()
            r1 = 1000(0x3e8, float:1.401E-42)
            if (r0 == r1) goto L31
            com.android.server.am.IActivityManagerServiceExt r1 = r5.mActivityManagerServiceExt
            boolean r1 = r1.isAllowedCallerKillProcess(r0)
            if (r1 == 0) goto L14
            goto L31
        L14:
            java.lang.SecurityException r1 = new java.lang.SecurityException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r3 = " cannot kill app process: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L31:
            boostPriorityForLockedSection()
            monitor-enter(r5)
            com.android.server.am.ProcessRecord r1 = r5.getProcessRecordLocked(r6, r7)     // Catch: java.lang.Throwable -> L6f
            if (r1 == 0) goto L48
            android.app.IApplicationThread r2 = r1.getThread()     // Catch: java.lang.Throwable -> L6f
            r3 = r2
            if (r2 == 0) goto L48
            r3.scheduleSuicide()     // Catch: android.os.RemoteException -> L46 java.lang.Throwable -> L6f
        L45:
            goto L6a
        L46:
            r2 = move-exception
            goto L45
        L48:
            java.lang.String r2 = "ActivityManager"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6f
            r3.<init>()     // Catch: java.lang.Throwable -> L6f
            java.lang.String r4 = "Process/uid not found attempting kill of "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L6f
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch: java.lang.Throwable -> L6f
            java.lang.String r4 = " / "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L6f
            java.lang.StringBuilder r3 = r3.append(r7)     // Catch: java.lang.Throwable -> L6f
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L6f
            android.util.Slog.w(r2, r3)     // Catch: java.lang.Throwable -> L6f
        L6a:
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L6f
            resetPriorityAfterLockedSection()
            return
        L6f:
            r1 = move-exception
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L6f
            resetPriorityAfterLockedSection()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.killApplicationProcess(java.lang.String, int):void");
    }

    private void forceStopPackageLocked(java.lang.String packageName, int uid, java.lang.String reason) {
        forceStopPackageLocked(packageName, android.os.UserHandle.getAppId(uid), false, false, true, false, false, false, android.os.UserHandle.getUserId(uid), reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishForceStopPackageLocked(java.lang.String packageName, int uid) {
        int flags = 0;
        if (!this.mProcessesReady) {
            flags = 1342177280;
        }
        this.mPackageManagerInt.sendPackageRestartedBroadcast(packageName, uid, flags);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
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
    public void cleanupDisabledPackageComponentsLocked(java.lang.String packageName, int userId, java.lang.String[] changedClasses) {
        java.util.Set<java.lang.String> disabledClasses;
        boolean z;
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        if (changedClasses == null) {
            return;
        }
        int i = changedClasses.length - 1;
        int enabled = 0;
        int enabled2 = 0;
        java.util.Set<java.lang.String> disabledClasses2 = null;
        while (true) {
            if (i < 0) {
                disabledClasses = disabledClasses2;
                z = enabled2;
                break;
            }
            java.lang.String changedClass = changedClasses[i];
            if (changedClass.equals(packageName)) {
                try {
                    enabled = pm.getApplicationEnabledSetting(packageName, userId != -1 ? userId : 0);
                    if (enabled != 1 && enabled != 0) {
                        i = 1;
                    }
                    enabled2 = i;
                    if (enabled2 != 0) {
                        disabledClasses = null;
                        z = enabled2;
                        break;
                    }
                } catch (java.lang.Exception e) {
                    return;
                }
            } else {
                try {
                    int enabled3 = pm.getComponentEnabledSetting(new android.content.ComponentName(packageName, changedClass), userId != -1 ? userId : 0);
                    if (enabled3 == 1 || enabled3 == 0) {
                        enabled = enabled3;
                    } else {
                        if (disabledClasses2 == null) {
                            disabledClasses2 = new android.util.ArraySet<>(changedClasses.length);
                        }
                        disabledClasses2.add(changedClass);
                        enabled = enabled3;
                    }
                } catch (java.lang.Exception e2) {
                    return;
                }
            }
            i--;
        }
        if (z == 0 && disabledClasses == null) {
            return;
        }
        this.mActivityManagerServiceExt.handlePackageDisabled(packageName, userId, z);
        this.mAtmInternal.cleanupDisabledPackageComponents(packageName, disabledClasses, userId, this.mBooted);
        java.util.Set<java.lang.String> set = disabledClasses;
        this.mServices.bringDownDisabledPackageServicesLocked(packageName, set, userId, false, false, true);
        java.util.ArrayList<com.android.server.am.ContentProviderRecord> providers = new java.util.ArrayList<>();
        this.mCpHelper.getProviderMap().collectPackageProvidersLocked(packageName, set, true, false, userId, providers);
        for (int i2 = providers.size() - 1; i2 >= 0; i2--) {
            this.mCpHelper.removeDyingProviderLocked(null, providers.get(i2), true);
        }
        this.mBroadcastQueue.cleanupDisabledPackageReceiversLocked(packageName, disabledClasses, userId);
    }

    final boolean clearBroadcastQueueForUserLocked(int userId) {
        boolean didSomething = this.mBroadcastQueue.cleanupDisabledPackageReceiversLocked(null, null, userId);
        return didSomething;
    }

    final void forceStopAppZygoteLocked(java.lang.String packageName, int appId, int userId) {
        if (packageName == null) {
            return;
        }
        if (appId < 0) {
            appId = android.os.UserHandle.getAppId(getPackageManagerInternal().getPackageUid(packageName, 272629760L, 0));
        }
        this.mProcessList.killAppZygotesLocked(packageName, appId, userId, true);
    }

    void stopAppForUserInternal(java.lang.String packageName, int userId) throws java.lang.Throwable {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock;
        int uid = getPackageManagerInternal().getPackageUid(packageName, 272629760L, userId);
        if (uid < 0) {
            android.util.Slog.w("ActivityManager", "Asked to stop " + packageName + "/u" + userId + " but does not exist in that user");
            return;
        }
        if (getPackageManagerInternal().isPackageStateProtected(packageName, userId)) {
            android.util.Slog.w("ActivityManager", "Asked to stop " + packageName + "/u" + userId + " but it is protected");
            return;
        }
        android.util.Slog.i("ActivityManager", "Stopping app for user: " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + userId);
        this.mSocExt.updateForceStopKillFlag();
        int appId = android.os.UserHandle.getAppId(uid);
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mProcLock;
                    boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock2) {
                        try {
                            this.mAtmInternal.onForceStopPackage(packageName, true, false, userId);
                            activityManagerGlobalLock = activityManagerGlobalLock2;
                            try {
                                this.mProcessList.killPackageProcessesLSP(packageName, appId, userId, -10000, true, false, true, false, true, false, 10, 23, "fully stop " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + userId + " by user request");
                                try {
                                    resetPriorityAfterProcLockedSection();
                                    this.mServices.bringDownDisabledPackageServicesLocked(packageName, null, userId, false, true, true);
                                    this.mServices.onUidRemovedLocked(uid);
                                    if (this.mBooted) {
                                        this.mAtmInternal.resumeTopActivities(true);
                                    }
                                    resetPriorityAfterLockedSection();
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    resetPriorityAfterLockedSection();
                                    throw th;
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                while (true) {
                                    try {
                                        resetPriorityAfterProcLockedSection();
                                        throw th;
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                    }
                                }
                            }
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                            activityManagerGlobalLock = activityManagerGlobalLock2;
                        }
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
        }
    }

    final boolean forceStopPackageLocked(java.lang.String packageName, int appId, boolean callerWillRestart, boolean purgeCache, boolean doit, boolean evenPersistent, boolean uninstalling, boolean packageStateStopped, int userId, java.lang.String reasonString) {
        int reason = packageName == null ? 11 : 10;
        return forceStopPackageLocked(packageName, appId, callerWillRestart, purgeCache, doit, evenPersistent, uninstalling, packageStateStopped, userId, reasonString, reason);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v25 */
    /* JADX WARN: Type inference failed for: r0v26 */
    /* JADX WARN: Type inference failed for: r2v21, types: [com.android.server.wm.ActivityTaskManagerInternal] */
    /* JADX WARN: Type inference failed for: r2v23 */
    /* JADX WARN: Type inference failed for: r2v25, types: [com.android.server.am.ContentProviderHelper] */
    /* JADX WARN: Type inference failed for: r2v47 */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    final boolean forceStopPackageLocked(java.lang.String str, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i2, java.lang.String str2, int i3) throws java.lang.Throwable {
        int appId;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock;
        boolean zOnForceStopPackage;
        int i4;
        com.android.server.am.ProcessList processList;
        java.lang.StringBuilder sb;
        java.lang.String str3;
        int i5;
        ?? r9;
        boolean zRemovePendingIntentsForPackage;
        int i6;
        ?? r0;
        int i7;
        com.android.internal.policy.AttributeCache attributeCacheInstance;
        if (i2 == -1 && str == null) {
            android.util.Slog.w("ActivityManager", "Can't force stop all processes of all users, that is insane!");
        }
        int packageUid = getPackageManagerInternal().getPackageUid(str, 272629760L, 0);
        if (i < 0 && str != null) {
            appId = android.os.UserHandle.getAppId(packageUid);
        } else {
            appId = i;
        }
        java.lang.String strUpdateStopReasonIfNeeded = this.mActivityManagerServiceExt.updateStopReasonIfNeeded(str2);
        if (z3) {
            if (str != null) {
                android.util.Slog.i("ActivityManager", "Force stopping " + str + " appid=" + appId + " user=" + i2 + ": " + strUpdateStopReasonIfNeeded);
            } else {
                android.util.Slog.i("ActivityManager", "Force stopping u" + i2 + ": " + strUpdateStopReasonIfNeeded);
            }
            this.mAppErrors.resetProcessCrashTime(str == null, appId, i2);
        }
        this.mSocExt.updateForceStopKillFlag();
        this.mActivityManagerServiceExt.handleForceStopPackage(str, i2);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock2) {
            try {
                zOnForceStopPackage = this.mAtmInternal.onForceStopPackage(str, z3, z4, i2);
                if (i3 == 10) {
                    i4 = 21;
                } else {
                    i4 = 0;
                }
                processList = this.mProcessList;
                sb = new java.lang.StringBuilder();
                if (str != null) {
                    str3 = "stop " + str;
                } else {
                    try {
                        str3 = "stop user " + i2;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        activityManagerGlobalLock = activityManagerGlobalLock2;
                        while (true) {
                            try {
                                resetPriorityAfterProcLockedSection();
                                throw th;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    }
                }
                activityManagerGlobalLock = activityManagerGlobalLock2;
                i5 = appId;
            } catch (java.lang.Throwable th3) {
                th = th3;
                activityManagerGlobalLock = activityManagerGlobalLock2;
            }
            try {
                boolean zKillPackageProcessesLSP = zOnForceStopPackage | processList.killPackageProcessesLSP(str, appId, i2, -10000, z, false, z3, z4, true, z5, i3, i4, sb.append(str3).append(" due to ").append(strUpdateStopReasonIfNeeded).toString());
                resetPriorityAfterProcLockedSection();
                if (!this.mServices.bringDownDisabledPackageServicesLocked(str, null, i2, z4, true, z3)) {
                    r9 = 1;
                    zRemovePendingIntentsForPackage = zKillPackageProcessesLSP;
                } else {
                    if (!z3) {
                        return true;
                    }
                    r9 = 1;
                    zRemovePendingIntentsForPackage = true;
                }
                this.mServices.onUidRemovedLocked(packageUid);
                if (str != null) {
                    i6 = i2;
                } else {
                    synchronized (this.mStickyBroadcasts) {
                        try {
                            try {
                                i6 = i2;
                                this.mStickyBroadcasts.remove(i6);
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                                throw th;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                            throw th;
                        }
                    }
                }
                java.util.ArrayList<com.android.server.am.ContentProviderRecord> arrayList = new java.util.ArrayList<>();
                if (this.mCpHelper.getProviderMap().collectPackageProvidersLocked(str, (java.util.Set<java.lang.String>) null, z3, z4, i2, arrayList)) {
                    if (!z3) {
                        return r9;
                    }
                    zRemovePendingIntentsForPackage = true;
                }
                for (int size = arrayList.size() - r9; size >= 0; size--) {
                    this.mCpHelper.removeDyingProviderLocked(null, arrayList.get(size), r9);
                }
                ?? r3 = 0;
                r3 = 0;
                this.mUgmInternal.removeUriPermissionsForPackage(str, i6, false, false);
                if (z3) {
                    zRemovePendingIntentsForPackage |= this.mBroadcastQueue.cleanupDisabledPackageReceiversLocked(str, null, i6);
                }
                if (z6) {
                    try {
                        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.stayStopped()) {
                            r3 = r9;
                        }
                        r0 = r3 == true ? 1 : 0;
                    } catch (java.lang.IllegalStateException e) {
                        r0 = 0;
                    }
                } else {
                    r0 = r3 == true ? 1 : 0;
                }
                if (str == null || z5 || r0 != 0) {
                    if (str == null) {
                        i7 = 1;
                    } else if (z5) {
                        i7 = 2;
                    } else {
                        i7 = 4;
                    }
                    if (i7 != 4 || !this.mActivityManagerServiceExt.isFilterRemovePackage(str)) {
                        zRemovePendingIntentsForPackage |= this.mPendingIntentController.removePendingIntentsForPackage(str, i2, i5, z3, i7);
                    }
                }
                if (z3) {
                    if (z2 && str != null && (attributeCacheInstance = com.android.internal.policy.AttributeCache.instance()) != null) {
                        attributeCacheInstance.removePackage(str);
                    }
                    if (this.mBooted) {
                        this.mAtmInternal.resumeTopActivities(r9);
                    }
                }
                return zRemovePendingIntentsForPackage;
            } catch (java.lang.Throwable th6) {
                th = th6;
                while (true) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
        }
    }

    void handleProcessStartOrKillTimeoutLocked(com.android.server.am.ProcessRecord app, boolean isKillTimeout) throws java.lang.Throwable {
        com.android.server.am.ProcessRecord successor;
        int pid = app.getPid();
        boolean gone = isKillTimeout || removePidLocked(pid, app);
        if (gone) {
            if (isKillTimeout) {
                successor = app.mSuccessor;
                if (successor == null) {
                    return;
                }
                android.util.Slog.wtf("ActivityManager", app.toString() + " " + app.getDyingPid() + " refused to die while trying to launch " + successor + ", cancelling the process start");
                app.mSuccessorStartRunnable = null;
                app.mSuccessor = null;
                successor.mPredecessor = null;
            } else {
                java.lang.String msg = "Process " + app + " failed to attach";
                android.util.Slog.w("ActivityManager", msg);
                com.android.server.am.EventLogTags.writeAmProcessStartTimeout(app.userId, pid, app.uid, app.processName);
                if (app.getActiveInstrumentation() != null) {
                    android.os.Bundle info = new android.os.Bundle();
                    info.putString("shortMsg", "failed to attach");
                    info.putString("longMsg", msg);
                    finishInstrumentationLocked(app, 0, info);
                }
                successor = app;
            }
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    this.mProcessList.removeProcessNameLocked(successor.processName, successor.uid);
                    this.mAtmInternal.clearHeavyWeightProcessIfEquals(successor.getWindowProcessController());
                    this.mCpHelper.cleanupAppInLaunchingProvidersLocked(successor, true);
                    this.mServices.processStartTimedOutLocked(successor);
                    this.mBroadcastQueue.onApplicationTimeoutLocked(successor);
                    if (!isKillTimeout) {
                        this.mBatteryStatsService.noteProcessFinish(successor.processName, successor.info.uid);
                        successor.killLocked("start timeout", 7, true);
                        removeLruProcessLocked(successor);
                        successor.makeInactive(this.mProcessStats);
                    }
                    if (successor.isolated) {
                        this.mBatteryStatsService.removeIsolatedUid(successor.uid, successor.info.uid);
                        this.mProcessList.mAppExitInfoTracker.mIsolatedUidRecords.removeIsolatedUid(successor.uid, successor.info.uid);
                        getPackageManagerInternal().removeIsolatedUid(successor.uid);
                        this.mActivityManagerServiceExt.removeIsolatedUid(successor.uid, successor.info.uid, successor.info.packageName);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterProcLockedSection();
            com.android.server.am.BackupRecord backupTarget = this.mActivityManagerServiceExt.hookGetBackupTargets(successor.uid, this.mBackupTargets.get(successor.userId));
            if (!isKillTimeout && backupTarget != null && backupTarget.app.getPid() == pid) {
                android.util.Slog.w("ActivityManager", "Unattached app died before backup, skipping");
                final int userId = successor.userId;
                final java.lang.String packageName = successor.info.packageName;
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService.7
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            android.app.backup.IBackupManager bm = android.app.backup.IBackupManager.Stub.asInterface(android.os.ServiceManager.getService(com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP));
                            bm.agentDisconnectedForUser(userId, packageName);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                });
            }
            if (!isKillTimeout) {
                this.mActivityManagerServiceExt.hookProcessStartTimeout(successor);
            }
            return;
        }
        android.util.Slog.w("ActivityManager", "Spurious process start timeout - pid not known for " + app);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(23:334|111|(2:113|114)|328|118|119|120|(1:137)(3:124|(1:134)(3:126|(3:128|129|(0))(1:132)|133)|135)|138|(5:322|140|141|351|142)(1:148)|(5:318|149|(1:151)|(1:153)(1:155)|156)|(16:158|357|159|(1:161)|309|167|168|(3:313|170|(19:172|173|179|320|180|181|(1:187)(2:185|186)|188|189|190|(2:192|193)(1:194)|195|(1:197)|198|(2:200|201)(11:202|203|(2:205|206)(1:207)|208|(1:210)|211|(1:216)(1:215)|217|218|347|219)|220|(3:340|222|223)(1:226)|227|5a7)(1:174))(1:177)|178|179|320|180|181|(14:183|187|188|189|190|(0)(0)|195|(0)|198|(0)(0)|220|(0)(0)|227|5a7)(0)|295|296)(1:165)|166|309|167|168|(0)(0)|178|179|320|180|181|(0)(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(27:334|111|(2:113|114)|328|118|119|120|(1:137)(3:124|(1:134)(3:126|(3:128|129|(0))(1:132)|133)|135)|138|(5:322|140|141|351|142)(1:148)|318|149|(1:151)|(1:153)(1:155)|156|(16:158|357|159|(1:161)|309|167|168|(3:313|170|(19:172|173|179|320|180|181|(1:187)(2:185|186)|188|189|190|(2:192|193)(1:194)|195|(1:197)|198|(2:200|201)(11:202|203|(2:205|206)(1:207)|208|(1:210)|211|(1:216)(1:215)|217|218|347|219)|220|(3:340|222|223)(1:226)|227|5a7)(1:174))(1:177)|178|179|320|180|181|(14:183|187|188|189|190|(0)(0)|195|(0)|198|(0)(0)|220|(0)(0)|227|5a7)(0)|295|296)(1:165)|166|309|167|168|(0)(0)|178|179|320|180|181|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x06a0, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x06a1, code lost:
    
        r13 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x06ab, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x06b5, code lost:
    
        r13 = r5;
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:137:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0328  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0331 A[Catch: Exception -> 0x0314, TRY_ENTER, TryCatch #23 {Exception -> 0x0314, blocks: (B:142:0x0310, B:151:0x0331, B:153:0x0355), top: B:351:0x0310 }] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0355 A[Catch: Exception -> 0x0314, TRY_LEAVE, TryCatch #23 {Exception -> 0x0314, blocks: (B:142:0x0310, B:151:0x0331, B:153:0x0355), top: B:351:0x0310 }] */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0358 A[Catch: Exception -> 0x06b1, TRY_ENTER, TryCatch #6 {Exception -> 0x06b1, blocks: (B:149:0x032a, B:156:0x035a, B:155:0x0358), top: B:318:0x032a }] */
    /* JADX WARN: Removed duplicated region for block: B:158:0x036e  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0383  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x03c3  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x03d4 A[Catch: Exception -> 0x03b7, TRY_ENTER, TryCatch #3 {Exception -> 0x03b7, blocks: (B:170:0x0397, B:172:0x03a1, B:183:0x03d4, B:185:0x03de, B:192:0x03fb, B:197:0x0448, B:200:0x0467, B:205:0x04a9, B:210:0x04db), top: B:313:0x0397 }] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x03ec  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x03fb A[Catch: Exception -> 0x03b7, TRY_ENTER, TRY_LEAVE, TryCatch #3 {Exception -> 0x03b7, blocks: (B:170:0x0397, B:172:0x03a1, B:183:0x03d4, B:185:0x03de, B:192:0x03fb, B:197:0x0448, B:200:0x0467, B:205:0x04a9, B:210:0x04db), top: B:313:0x0397 }] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0448 A[Catch: Exception -> 0x03b7, TRY_ENTER, TRY_LEAVE, TryCatch #3 {Exception -> 0x03b7, blocks: (B:170:0x0397, B:172:0x03a1, B:183:0x03d4, B:185:0x03de, B:192:0x03fb, B:197:0x0448, B:200:0x0467, B:205:0x04a9, B:210:0x04db), top: B:313:0x0397 }] */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0467 A[Catch: Exception -> 0x03b7, TRY_ENTER, TRY_LEAVE, TryCatch #3 {Exception -> 0x03b7, blocks: (B:170:0x0397, B:172:0x03a1, B:183:0x03d4, B:185:0x03de, B:192:0x03fb, B:197:0x0448, B:200:0x0467, B:205:0x04a9, B:210:0x04db), top: B:313:0x0397 }] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0489  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0594  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x0397 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x0307 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:332:0x05a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0583 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r13v0, types: [int] */
    /* JADX WARN: Type inference failed for: r13v1 */
    /* JADX WARN: Type inference failed for: r13v10 */
    /* JADX WARN: Type inference failed for: r13v12 */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v21 */
    /* JADX WARN: Type inference failed for: r13v22 */
    /* JADX WARN: Type inference failed for: r13v24, types: [long] */
    /* JADX WARN: Type inference failed for: r13v28 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v7 */
    /* JADX WARN: Type inference failed for: r13v8 */
    /* JADX WARN: Type inference failed for: r13v9 */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.server.am.IActivityManagerServiceExt] */
    /* JADX WARN: Type inference failed for: r65v0, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void attachApplicationLocked(android.app.IApplicationThread r66, int r67, int r68, long r69) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 1860
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.attachApplicationLocked(android.app.IApplicationThread, int, int, long):void");
    }

    public final void attachApplication(android.app.IApplicationThread thread, long startSeq) {
        if (thread == null) {
            throw new java.lang.SecurityException("Invalid application interface");
        }
        long callStart = sAnrLogEnhancementHelper.getCallStartTime();
        int callPid = android.os.Binder.getCallingPid();
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                int callingPid = android.os.Binder.getCallingPid();
                int callingUid = android.os.Binder.getCallingUid();
                long origId = android.os.Binder.clearCallingIdentity();
                if (this.mActivityManagerServiceExt.isLogToolRun()) {
                    android.util.Slog.d("ActivityManager", "attachApplication pid:" + callingPid + " uid:" + callingUid);
                }
                attachApplicationLocked(thread, callingPid, callingUid, startSeq);
                android.os.Binder.restoreCallingIdentity(origId);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        sAnrLogEnhancementHelper.printSlowLog(callStart, "attachApplicationLocked", callPid, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0051  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void finishAttachApplicationInner(long r30, int r32, int r33) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 588
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.finishAttachApplicationInner(long, int, int):void");
    }

    public final void finishAttachApplication(long startSeq, long timestampApplicationOnCreateNs) {
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        if (!this.mConstants.mEnableWaitForFinishAttachApplication) {
            android.util.Slog.i("ActivityManager", "Flag disabled. Ignoring finishAttachApplication from uid: " + uid + ". pid: " + pid);
            return;
        }
        if (pid == MY_PID && uid == 1000) {
            return;
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            finishAttachApplicationInner(startSeq, uid, pid);
            android.os.Binder.restoreCallingIdentity(origId);
            if (android.app.Flags.appStartInfoTimestamps() && timestampApplicationOnCreateNs > 0) {
                addStartInfoTimestampInternal(2, timestampApplicationOnCreateNs, android.os.UserHandle.getUserId(uid), uid);
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(origId);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBindApplicationTimeoutSoft(com.android.server.am.ProcessRecord app, int softTimeoutMillis) {
        long cpuDelayTime = app.getCpuDelayTime() - app.mProfile.mLastCpuDelayTime.get();
        long hardTimeoutMillis = android.util.MathUtils.constrain(cpuDelayTime, 0L, softTimeoutMillis);
        if (hardTimeoutMillis == 0) {
            handleBindApplicationTimeoutHard(app);
            return;
        }
        android.util.Slog.i("ActivityManager", "Extending process start timeout by " + hardTimeoutMillis + "ms for " + app);
        android.os.Trace.instant(64L, "bindApplicationTimeSoft " + app.processName + "(" + app.getPid() + ")");
        android.os.Message msg = this.mHandler.obtainMessage(83, app);
        this.mHandler.sendMessageDelayed(msg, hardTimeoutMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBindApplicationTimeoutHard(com.android.server.am.ProcessRecord app) {
        java.lang.String anrMessage;
        synchronized (app) {
            anrMessage = "Process " + app + " failed to complete startup";
        }
        this.mAnrHelper.appNotResponding(app, com.android.internal.os.TimeoutRecord.forAppStart(anrMessage));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFollowUpOomAdjusterUpdate() {
        this.mHandler.removeMessages(86);
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mOomAdjuster.updateOomAdjFollowUpTargetsLocked();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    static java.lang.String getShortAction(java.lang.String action) {
        int index;
        if (action == null || (index = action.lastIndexOf(46)) == -1 || index == action.length() - 1) {
            return action;
        }
        java.lang.String shortAction = action.substring(index + 1);
        return shortAction;
    }

    void checkTime(long startTime, java.lang.String where) {
        long now = android.os.SystemClock.uptimeMillis();
        if (now - startTime > 50) {
            android.util.Slog.w("ActivityManager", "Slow operation: " + (now - startTime) + "ms so far, now at " + where);
        }
    }

    private void maybeSendBootCompletedLocked(com.android.server.am.ProcessRecord app, boolean isRestrictedBackupMode) throws java.lang.Throwable {
        boolean sendBroadcast = false;
        if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures()) {
            com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            android.content.pm.UserInfo userInfo = umInternal.getUserInfo(app.userId);
            if (userInfo != null && userInfo.isPrivateProfile() && !this.mPrivateSpaceBootCompletedPackages.contains(app.info.packageName)) {
                this.mPrivateSpaceBootCompletedPackages.add(app.info.packageName);
                sendBroadcast = true;
            }
        }
        boolean wasForceStopped = app.wasForceStopped() || app.getWindowProcessController().wasForceStopped();
        if (android.app.Flags.appRestrictionsApi() && wasForceStopped) {
            noteAppRestrictionEnabled(app.info.packageName, app.uid, 60, false, 3, "unknown", 1, 0L);
        }
        if (isRestrictedBackupMode) {
            return;
        }
        if ((!sendBroadcast && (!com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.stayStopped() || !wasForceStopped)) || this.mActivityManagerServiceExt.interceptMaybeSendBootCompleted(app)) {
            return;
        }
        if (app.getApplicationInfo().isEncryptionAware()) {
            sendBootBroadcastToAppLocked(app, new android.content.Intent("android.intent.action.LOCKED_BOOT_COMPLETED"), 202);
        }
        if (android.os.storage.StorageManager.isCeStorageUnlocked(app.userId)) {
            sendBootBroadcastToAppLocked(app, new android.content.Intent("android.intent.action.BOOT_COMPLETED"), 200);
        }
    }

    private void sendBootBroadcastToAppLocked(com.android.server.am.ProcessRecord app, android.content.Intent intent, int reason) {
        intent.setPackage(app.info.packageName);
        intent.putExtra("android.intent.extra.user_handle", app.userId);
        intent.addFlags(150994976);
        broadcastIntentLocked(null, null, null, intent, null, null, 0, null, null, new java.lang.String[]{"android.permission.RECEIVE_BOOT_COMPLETED"}, null, null, -1, null, true, false, MY_PID, 1000, 1000, MY_PID, app.userId);
    }

    public void showBootMessage(java.lang.CharSequence msg, boolean always) throws java.lang.Throwable {
        if (android.os.Binder.getCallingUid() != android.os.Process.myUid()) {
            throw new java.lang.SecurityException();
        }
        this.mWindowManager.showBootMessage(msg, always);
    }

    final void finishBooting() {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog("ActivityManagerTiming", 64L);
        t.traceBegin("FinishBooting");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (!this.mBootAnimationComplete) {
                    this.mCallFinishBooting = true;
                    return;
                }
                this.mCallFinishBooting = false;
                resetPriorityAfterLockedSection();
                android.os.Process.ZYGOTE_PROCESS.bootCompleted();
                dalvik.system.VMRuntime.bootCompleted();
                android.content.IntentFilter pkgFilter = new android.content.IntentFilter();
                pkgFilter.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
                pkgFilter.addDataScheme("package");
                this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.am.ActivityManagerService.8
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(android.content.Context context, android.content.Intent intent) throws java.lang.Throwable {
                        com.android.server.am.ActivityManagerService activityManagerService;
                        java.lang.String[] pkgs = intent.getStringArrayExtra("android.intent.extra.PACKAGES");
                        if (pkgs != null) {
                            for (java.lang.String pkg : pkgs) {
                                com.android.server.am.ActivityManagerService activityManagerService2 = com.android.server.am.ActivityManagerService.this;
                                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                                synchronized (activityManagerService2) {
                                    try {
                                        activityManagerService = activityManagerService2;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        activityManagerService = activityManagerService2;
                                    }
                                    try {
                                        if (com.android.server.am.ActivityManagerService.this.forceStopPackageLocked(pkg, -1, false, false, false, false, false, false, 0, "query restart")) {
                                            setResultCode(-1);
                                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                            return;
                                        }
                                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                        throw th;
                                    }
                                }
                            }
                        }
                    }
                }, pkgFilter);
                try {
                    android.util.Slog.i("ActivityManager", "About to commit checkpoint");
                    android.os.storage.IStorageManager storageManager = com.android.internal.content.InstallLocationUtils.getStorageManager();
                    storageManager.commitChanges();
                } catch (java.lang.Exception e) {
                    android.os.PowerManager pm = (android.os.PowerManager) this.mInjector.getContext().getSystemService("power");
                    pm.reboot("Checkpoint commit failed");
                }
                this.mSystemServiceManager.startBootPhase(t, 1000);
                boostPriorityForLockedSection();
                synchronized (this) {
                    try {
                        int NP = this.mProcessesOnHold.size();
                        if (NP > 0) {
                            java.util.ArrayList<com.android.server.am.ProcessRecord> procs = new java.util.ArrayList<>(this.mProcessesOnHold);
                            for (int ip = 0; ip < NP; ip++) {
                                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
                                    android.util.Slog.v(TAG_PROCESSES, "Starting process on hold: " + procs.get(ip));
                                }
                                this.mProcessList.startProcessLocked(procs.get(ip), new com.android.server.am.HostingRecord(com.android.server.am.HostingRecord.HOSTING_TYPE_ON_HOLD), 2);
                            }
                        }
                        if (this.mFactoryTest == 1) {
                            return;
                        }
                        android.os.Message nmsg = this.mHandler.obtainMessage(27);
                        this.mHandler.sendMessageDelayed(nmsg, this.mConstants.POWER_CHECK_INTERVAL);
                        if (((java.lang.Boolean) android.sysprop.InitProperties.userspace_reboot_in_progress().orElse(false)).booleanValue()) {
                            com.android.server.UserspaceRebootLogger.noteUserspaceRebootSuccess();
                        }
                        this.mActivityManagerServiceExt.setBootstage();
                        this.mActivityManagerServiceExt.recordBootSuccess();
                        android.os.SystemProperties.set("sys.boot_completed", "1");
                        android.os.SystemProperties.set("dev.bootcomplete", "1");
                        com.android.server.am.ProcessList.startPsiMonitoringAfterBoot();
                        this.mUserController.onBootComplete(new com.android.server.am.ActivityManagerService.AnonymousClass9());
                        maybeLogUserspaceRebootEvent();
                        this.mUserController.scheduleStartProfiles();
                        resetPriorityAfterLockedSection();
                        showConsoleNotificationIfActive();
                        showMteOverrideNotificationIfActive();
                        this.mActivityManagerServiceExt.hookBootCompleted();
                        t.traceEnd();
                    } finally {
                        resetPriorityAfterLockedSection();
                    }
                }
            } finally {
                resetPriorityAfterLockedSection();
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.am.ActivityManagerService$9, reason: invalid class name */
    class AnonymousClass9 extends android.content.IIntentReceiver.Stub {
        AnonymousClass9() {
        }

        public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            com.android.server.am.ActivityManagerService.this.mBootCompletedTimestamp = android.os.SystemClock.uptimeMillis();
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    com.android.server.am.ActivityManagerService.this.mSocExt.compactAllSystem();
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            com.android.server.am.ActivityManagerService.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$9$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$performReceive$0();
                }
            }, com.android.server.am.ActivityManagerService.this.mConstants.FULL_PSS_MIN_INTERVAL);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$performReceive$0() {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    com.android.server.am.ActivityManagerService.this.mAppProfiler.requestPssAllProcsLPr(android.os.SystemClock.uptimeMillis(), true, false);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        }
    }

    private void showConsoleNotificationIfActive() {
        if (!android.os.SystemProperties.get("init.svc.console").equals(android.net.INetd.IF_FLAG_RUNNING)) {
            return;
        }
        java.lang.String title = this.mContext.getString(android.R.string.content_description_expanded);
        java.lang.String message = this.mContext.getString(android.R.string.content_description_collapsed);
        android.app.Notification notification = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.DEVELOPER).setSmallIcon(android.R.drawable.seekbar_thumb_pressed_to_unpressed_animation).setWhen(0L).setOngoing(true).setTicker(title).setDefaults(0).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentText(message).setVisibility(1).build();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        notificationManager.notifyAsUser(null, 55, notification, android.os.UserHandle.ALL);
        this.mSocExt.writeBootCompletedEvent();
    }

    private void showMteOverrideNotificationIfActive() {
        java.lang.String bootctl = android.os.SystemProperties.get("arm64.memtag.bootctl");
        if (java.util.Arrays.asList(bootctl.split(",")).contains("memtag") || !android.os.SystemProperties.getBoolean("ro.arm64.memtag.bootctl_supported", false) || !com.android.internal.os.Zygote.nativeSupportsMemoryTagging()) {
            return;
        }
        java.lang.String title = this.mContext.getString(android.R.string.minutes);
        java.lang.String message = this.mContext.getString(android.R.string.minute_picker_description);
        android.app.Notification notification = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.DEVELOPER).setSmallIcon(android.R.drawable.seekbar_thumb_pressed_to_unpressed_animation).setOngoing(true).setTicker(title).setDefaults(0).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentText(message).setVisibility(1).build();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        notificationManager.notifyAsUser(null, 69, notification, android.os.UserHandle.ALL);
    }

    public void bootAnimationComplete() {
        boolean callFinishBooting;
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ALL) {
            android.util.Slog.d("ActivityManager", "bootAnimationComplete: Callers=" + android.os.Debug.getCallers(4));
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                android.util.Slog.d("ActivityManager", "bootAnimationComplete, mCallFinishBooting = " + this.mCallFinishBooting);
                callFinishBooting = this.mCallFinishBooting;
                this.mBootAnimationComplete = true;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        if (callFinishBooting) {
            finishBooting();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleHomeTimeout() {
        if (!isHomeLaunchDelayable()) {
            android.util.Slog.d("ActivityManager", "ThemeHomeDelay: Home launch is not delayable, skipping timeout creation");
        } else if (this.mHasHomeDelay.compareAndSet(false, true)) {
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleHomeTimeout$3();
                }
            }, HOME_LAUNCH_TIMEOUT_MS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleHomeTimeout$3() {
        int userId = this.mUserController.getCurrentUserId();
        if (!isThemeOverlayReady(userId)) {
            android.util.Slog.d("ActivityManager", "ThemeHomeDelay: ThemeOverlayController not responding, launching Home after 15000ms with user " + userId);
            setThemeOverlayReady(userId);
        }
    }

    public void setThemeOverlayReady(int userId) {
        boolean updateUser;
        if (!isHomeLaunchDelayable()) {
            android.util.Slog.d("ActivityManager", "ThemeHomeDelay: Home launch is not delayable, ignoring setThemeOverlayReady() call");
            return;
        }
        enforceCallingPermission("android.permission.SET_THEME_OVERLAY_CONTROLLER_READY", "setThemeOverlayReady");
        android.util.Slog.d("ActivityManager", "ThemeHomeDelay: userId " + userId + " notified ThemeOverlayController completeness");
        synchronized (this.mThemeOverlayReadyUsers) {
            updateUser = this.mThemeOverlayReadyUsers.add(java.lang.Integer.valueOf(userId));
            android.util.Slog.d("ActivityManager", "ThemeHomeDelay: updateUser " + userId + " isUpdatable: " + updateUser);
        }
        if (updateUser) {
            android.util.Slog.d("ActivityManager", "ThemeHomeDelay: updating user " + userId);
            this.mAtmInternal.startHomeOnAllDisplays(userId, "setThemeOverlayReady");
        }
    }

    public boolean isThemeOverlayReady(int userId) {
        boolean zContains;
        synchronized (this.mThemeOverlayReadyUsers) {
            zContains = this.mThemeOverlayReadyUsers.contains(java.lang.Integer.valueOf(userId));
        }
        return zContains;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHomeLaunchDelayable() {
        return com.android.systemui.shared.Flags.enableHomeDelay() && !this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    final void ensureBootCompleted() {
        boolean booting;
        boolean enableScreen;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                booting = this.mBooting;
                this.mBooting = false;
                enableScreen = this.mBooted ? false : true;
                this.mBooted = true;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        if (booting) {
            finishBooting();
        }
        if (enableScreen) {
            this.mAtmInternal.enableScreenAfterBoot(this.mBooted);
        }
    }

    @java.lang.Deprecated
    public android.content.IIntentSender getIntentSender(int type, java.lang.String packageName, android.os.IBinder token, java.lang.String resultWho, int requestCode, android.content.Intent[] intents, java.lang.String[] resolvedTypes, int flags, android.os.Bundle bOptions, int userId) {
        return getIntentSenderWithFeature(type, packageName, null, token, resultWho, requestCode, intents, resolvedTypes, flags, bOptions, userId);
    }

    public android.content.IIntentSender getIntentSenderWithFeature(int type, java.lang.String packageName, java.lang.String featureId, android.os.IBinder token, java.lang.String resultWho, int requestCode, android.content.Intent[] intents, java.lang.String[] resolvedTypes, int flags, android.os.Bundle bOptions, int userId) {
        enforceNotIsolatedCaller("getIntentSender");
        return getIntentSenderWithFeatureAsApp(type, packageName, featureId, token, resultWho, requestCode, intents, resolvedTypes, flags, bOptions, userId, android.os.Binder.getCallingUid());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public android.content.IIntentSender getIntentSenderWithFeatureAsApp(int type, java.lang.String packageName, java.lang.String featureId, android.os.IBinder token, java.lang.String resultWho, int requestCode, android.content.Intent[] intents, java.lang.String[] resolvedTypes, int flags, android.os.Bundle bOptions, int userId, int owningUid) {
        int userId2;
        android.content.Intent intent;
        int i = 3;
        int i2 = 1;
        if (intents != null) {
            if (intents.length < 1) {
                throw new java.lang.IllegalArgumentException("Intents array length must be >= 1");
            }
            int i3 = 0;
            while (i3 < intents.length) {
                android.content.Intent intent2 = intents[i3];
                if (intent2 != null) {
                    if (intent2.hasFileDescriptors()) {
                        throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
                    }
                    if (type == i2 && (intent2.getFlags() & 33554432) != 0) {
                        throw new java.lang.IllegalArgumentException("Can't use FLAG_RECEIVER_BOOT_UPGRADE here");
                    }
                    if (android.app.PendingIntent.isNewMutableDisallowedImplicitPendingIntent(flags, intent2, type == i ? i2 : 0)) {
                        boolean isChangeEnabled = android.app.compat.CompatChanges.isChangeEnabled(236704164L, packageName, android.os.UserHandle.of(userId));
                        java.lang.String resolvedType = (resolvedTypes == null || i3 >= resolvedTypes.length) ? null : resolvedTypes[i3];
                        intent = intent2;
                        com.android.server.pm.SaferIntentUtils.reportUnsafeIntentEvent(4, owningUid, -1, intent2, resolvedType, isChangeEnabled);
                        if (isChangeEnabled) {
                            throw new java.lang.IllegalArgumentException(packageName + ": Targeting U+ (version 34 and above) disallows creating or retrieving a PendingIntent with FLAG_MUTABLE, an implicit Intent within and without FLAG_NO_CREATE and FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT for security reasons. To retrieve an already existing PendingIntent, use FLAG_NO_CREATE, however, to create a new PendingIntent with an implicit Intent use FLAG_IMMUTABLE.");
                        }
                    } else {
                        intent = intent2;
                    }
                    intents[i3] = new android.content.Intent(intent);
                    intents[i3].removeExtendedFlags(1);
                }
                i3++;
                i2 = 1;
                i = 3;
            }
            if (resolvedTypes != null && resolvedTypes.length != intents.length) {
                throw new java.lang.IllegalArgumentException("Intent array length does not match resolvedTypes length");
            }
        }
        if (bOptions != null && bOptions.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in options");
        }
        int userId3 = this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), owningUid, userId, type == 1, 0, "getIntentSender", null);
        if (userId != -2) {
            userId2 = userId3;
        } else {
            userId2 = -2;
        }
        if (owningUid != 0 && owningUid != 1000 && !getPackageManagerInternal().isSameApp(packageName, 268435456L, owningUid, android.os.UserHandle.getUserId(owningUid))) {
            java.lang.String msg = "Permission Denial: getIntentSender() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + owningUid + " is not allowed to send as package " + packageName;
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.d("ActivityManager", "getIntentSender type is " + type);
        }
        if (type != 3) {
            return this.mPendingIntentController.getIntentSender(type, packageName, featureId, owningUid, userId2, token, resultWho, requestCode, intents, resolvedTypes, flags, bOptions);
        }
        return this.mAtmInternal.getIntentSender(type, packageName, featureId, owningUid, userId2, token, resultWho, requestCode, intents, resolvedTypes, flags, bOptions);
    }

    public int sendIntentSender(android.app.IApplicationThread caller, android.content.IIntentSender target, android.os.IBinder allowlistToken, int code, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) {
        android.content.Intent intent2;
        if (target instanceof com.android.server.am.PendingIntentRecord) {
            com.android.server.am.PendingIntentRecord originalRecord = (com.android.server.am.PendingIntentRecord) target;
            com.android.server.am.PendingIntentRecord.Key originalKey = originalRecord.key;
            com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (android.os.UserManager.isVisibleBackgroundUsersEnabled() && originalKey.userId == -2 && callingUserId != 0 && umInternal.isUserVisible(callingUserId)) {
                com.android.server.am.EventLogTags.writeAmIntentSenderRedirectUser(callingUserId);
                com.android.server.am.PendingIntentRecord.Key key = new com.android.server.am.PendingIntentRecord.Key(originalKey.type, originalKey.packageName, originalKey.featureId, originalKey.activity, originalKey.who, originalKey.requestCode, originalKey.allIntents, originalKey.allResolvedTypes, originalKey.flags, originalKey.options, callingUserId);
                com.android.server.am.PendingIntentRecord newRecord = new com.android.server.am.PendingIntentRecord(originalRecord.controller, key, originalRecord.uid);
                if (this.mActivityManagerServiceExt.blockPendingIntent(caller, newRecord, allowlistToken, code, intent, resolvedType, finishedReceiver, requiredPermission, options)) {
                    return 0;
                }
                return newRecord.sendWithResult(caller, code, intent, resolvedType, allowlistToken, finishedReceiver, requiredPermission, options);
            }
            if (this.mActivityManagerServiceExt.blockPendingIntent(caller, originalRecord, allowlistToken, code, intent, resolvedType, finishedReceiver, requiredPermission, options)) {
                return 0;
            }
            return originalRecord.sendWithResult(caller, code, intent, resolvedType, allowlistToken, finishedReceiver, requiredPermission, options);
        }
        if (intent == null) {
            android.util.Slog.wtf("ActivityManager", "Can't use null intent with direct IIntentSender call");
            intent2 = new android.content.Intent("android.intent.action.MAIN");
        } else {
            intent2 = intent;
        }
        try {
            if (allowlistToken != null) {
                try {
                    int callingUid = android.os.Binder.getCallingUid();
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        java.lang.String packageName = android.app.AppGlobals.getPackageManager().getNameForUid(callingUid);
                        android.os.Binder.restoreCallingIdentity(token);
                        android.util.Slog.wtf("ActivityManager", "Send a non-null allowlistToken to a non-PI target. Calling package: " + packageName + "; intent: " + intent2 + "; options: " + options);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                } catch (android.os.RemoteException e) {
                }
            }
            target.send(code, intent2, resolvedType, (android.os.IBinder) null, (android.content.IIntentReceiver) null, requiredPermission, options);
        } catch (android.os.RemoteException e2) {
        }
        if (finishedReceiver != null) {
            try {
                try {
                    finishedReceiver.performReceive(intent2, 0, (java.lang.String) null, (android.os.Bundle) null, false, false, android.os.UserHandle.getCallingUserId());
                    return 0;
                } catch (android.os.RemoteException e3) {
                    return 0;
                }
            } catch (android.os.RemoteException e4) {
                return 0;
            }
        }
        return 0;
    }

    public void cancelIntentSender(android.content.IIntentSender sender) {
        this.mPendingIntentController.cancelIntentSender(sender);
    }

    public boolean registerIntentSenderCancelListenerEx(android.content.IIntentSender sender, com.android.internal.os.IResultReceiver receiver) {
        return this.mPendingIntentController.registerIntentSenderCancelListener(sender, receiver);
    }

    public void unregisterIntentSenderCancelListener(android.content.IIntentSender sender, com.android.internal.os.IResultReceiver receiver) {
        this.mPendingIntentController.unregisterIntentSenderCancelListener(sender, receiver);
    }

    public android.app.ActivityManager.PendingIntentInfo getInfoForIntentSender(android.content.IIntentSender sender) {
        if (sender instanceof com.android.server.am.PendingIntentRecord) {
            com.android.server.am.PendingIntentRecord res = (com.android.server.am.PendingIntentRecord) sender;
            java.lang.String packageName = res.key.packageName;
            int uid = res.uid;
            boolean shouldFilter = getPackageManagerInternal().filterAppAccess(packageName, android.os.Binder.getCallingUid(), android.os.UserHandle.getUserId(uid));
            return new android.app.ActivityManager.PendingIntentInfo(shouldFilter ? null : packageName, shouldFilter ? -1 : uid, (res.key.flags & 67108864) != 0, res.key.type);
        }
        return new android.app.ActivityManager.PendingIntentInfo((java.lang.String) null, -1, false, 0);
    }

    public boolean isIntentSenderTargetedToPackage(android.content.IIntentSender pendingResult) {
        if (!(pendingResult instanceof com.android.server.am.PendingIntentRecord)) {
            return false;
        }
        try {
            com.android.server.am.PendingIntentRecord res = (com.android.server.am.PendingIntentRecord) pendingResult;
            if (res.key.allIntents == null) {
                return false;
            }
            for (int i = 0; i < res.key.allIntents.length; i++) {
                android.content.Intent intent = res.key.allIntents[i];
                if (intent.getPackage() != null && intent.getComponent() != null) {
                    return false;
                }
            }
            return true;
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    public boolean isIntentSenderAnActivity(android.content.IIntentSender pendingResult) {
        if (!(pendingResult instanceof com.android.server.am.PendingIntentRecord)) {
            return false;
        }
        try {
            com.android.server.am.PendingIntentRecord res = (com.android.server.am.PendingIntentRecord) pendingResult;
            return res.key.type == 2;
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    public android.content.Intent getIntentForIntentSender(android.content.IIntentSender pendingResult) {
        enforceCallingPermission("android.permission.GET_INTENT_SENDER_INTENT", "getIntentForIntentSender()");
        if (!(pendingResult instanceof com.android.server.am.PendingIntentRecord)) {
            return null;
        }
        try {
            com.android.server.am.PendingIntentRecord res = (com.android.server.am.PendingIntentRecord) pendingResult;
            if (res.key.requestIntent != null) {
                return new android.content.Intent(res.key.requestIntent);
            }
            return null;
        } catch (java.lang.ClassCastException e) {
            return null;
        }
    }

    public android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> queryIntentComponentsForIntentSender(android.content.IIntentSender pendingResult, int matchFlags) {
        enforceCallingPermission("android.permission.GET_INTENT_SENDER_INTENT", "queryIntentComponentsForIntentSender()");
        java.util.Objects.requireNonNull(pendingResult);
        try {
            com.android.server.am.PendingIntentRecord res = (com.android.server.am.PendingIntentRecord) pendingResult;
            android.content.Intent intent = res.key.requestIntent;
            if (intent == null) {
                return null;
            }
            int userId = res.key.userId;
            int uid = res.uid;
            java.lang.String resolvedType = res.key.requestResolvedType;
            switch (res.key.type) {
                case 1:
                    return new android.content.pm.ParceledListSlice<>(this.mPackageManagerInt.queryIntentReceivers(intent, resolvedType, matchFlags, uid, -1, userId, false));
                case 2:
                    return new android.content.pm.ParceledListSlice<>(this.mPackageManagerInt.queryIntentActivities(intent, resolvedType, matchFlags, uid, userId));
                case 3:
                default:
                    throw new java.lang.IllegalStateException("Unsupported intent sender type: " + res.key.type);
                case 4:
                case 5:
                    return new android.content.pm.ParceledListSlice<>(this.mPackageManagerInt.queryIntentServices(intent, matchFlags, uid, userId));
            }
        } catch (java.lang.ClassCastException e) {
            return null;
        }
    }

    public java.lang.String getTagForIntentSender(android.content.IIntentSender pendingResult, java.lang.String prefix) {
        java.lang.String tagForIntentSenderLocked;
        if (!(pendingResult instanceof com.android.server.am.PendingIntentRecord)) {
            return null;
        }
        try {
            com.android.server.am.PendingIntentRecord res = (com.android.server.am.PendingIntentRecord) pendingResult;
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    tagForIntentSenderLocked = getTagForIntentSenderLocked(res, prefix);
                } finally {
                }
            }
            resetPriorityAfterLockedSection();
            return tagForIntentSenderLocked;
        } catch (java.lang.ClassCastException e) {
            return null;
        }
    }

    java.lang.String getTagForIntentSenderLocked(com.android.server.am.PendingIntentRecord res, java.lang.String prefix) {
        android.content.Intent intent = res.key.requestIntent;
        if (intent != null) {
            if (res.lastTag != null && res.lastTagPrefix == prefix && (res.lastTagPrefix == null || res.lastTagPrefix.equals(prefix))) {
                return res.lastTag;
            }
            res.lastTagPrefix = prefix;
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            if (prefix != null) {
                sb.append(prefix);
            }
            if (intent.getAction() != null) {
                sb.append(intent.getAction());
            } else if (intent.getComponent() != null) {
                intent.getComponent().appendShortString(sb);
            } else {
                sb.append("?");
            }
            java.lang.String string = sb.toString();
            res.lastTag = string;
            return string;
        }
        return null;
    }

    public void setProcessLimit(int max) {
        enforceCallingPermission("android.permission.SET_PROCESS_LIMIT", "setProcessLimit()");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mConstants.setOverrideMaxCachedProcesses(max);
                trimApplicationsLocked(true, 12);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public int getProcessLimit() {
        int overrideMaxCachedProcesses;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                overrideMaxCachedProcesses = this.mConstants.getOverrideMaxCachedProcesses();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return overrideMaxCachedProcesses;
    }

    void importanceTokenDied(com.android.server.am.ActivityManagerService.ImportanceToken token) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                synchronized (this.mPidsSelfLocked) {
                    com.android.server.am.ActivityManagerService.ImportanceToken cur = this.mImportantProcesses.get(token.pid);
                    if (cur != token) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    this.mImportantProcesses.remove(token.pid);
                    com.android.server.am.ProcessRecord pr = this.mPidsSelfLocked.get(token.pid);
                    if (pr == null) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    pr.mState.setForcingToImportant(null);
                    clearProcessForegroundLocked(pr);
                    updateOomAdjLocked(pr, 9);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setProcessImportant(android.os.IBinder token, int pid, boolean isForeground, java.lang.String reason) {
        enforceCallingPermission("android.permission.SET_PROCESS_LIMIT", "setProcessImportant()");
        boostPriorityForLockedSection();
        synchronized (this) {
            boolean changed = false;
            try {
                synchronized (this.mPidsSelfLocked) {
                    com.android.server.am.ProcessRecord pr = this.mPidsSelfLocked.get(pid);
                    if (pr == null && isForeground) {
                        android.util.Slog.w("ActivityManager", "setProcessForeground called on unknown pid: " + pid);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.am.ActivityManagerService.ImportanceToken oldToken = this.mImportantProcesses.get(pid);
                    if (oldToken != null) {
                        oldToken.token.unlinkToDeath(oldToken, 0);
                        this.mImportantProcesses.remove(pid);
                        if (pr != null) {
                            pr.mState.setForcingToImportant(null);
                        }
                        changed = true;
                    }
                    if (isForeground && token != null) {
                        com.android.server.am.ActivityManagerService.ImportanceToken newToken = new com.android.server.am.ActivityManagerService.ImportanceToken(pid, token, reason) { // from class: com.android.server.am.ActivityManagerService.10
                            @Override // android.os.IBinder.DeathRecipient
                            public void binderDied() {
                                com.android.server.am.ActivityManagerService.this.importanceTokenDied(this);
                            }
                        };
                        try {
                            token.linkToDeath(newToken, 0);
                            this.mImportantProcesses.put(pid, newToken);
                            pr.mState.setForcingToImportant(newToken);
                            changed = true;
                        } catch (android.os.RemoteException e) {
                        }
                    }
                    if (changed) {
                        updateOomAdjLocked(pr, 9);
                    }
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAppForeground(int uid) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.am.UidRecord uidRec = this.mProcessList.mActiveUids.get(uid);
                if (uidRec != null && !uidRec.isIdle()) {
                    boolean z = uidRec.getCurProcState() <= 6;
                    resetPriorityAfterProcLockedSection();
                    return z;
                }
                resetPriorityAfterProcLockedSection();
                return false;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAppBad(java.lang.String processName, int uid) {
        return this.mAppErrors.isBadProcess(processName, uid);
    }

    int getUidState(int uid) {
        int uidProcStateLOSP;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                uidProcStateLOSP = this.mProcessList.getUidProcStateLOSP(uid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return uidProcStateLOSP;
    }

    int getUidStateLocked(int uid) {
        return this.mProcessList.getUidProcStateLOSP(uid);
    }

    int getUidProcessCapabilityLocked(int uid) {
        return this.mProcessList.getUidProcessCapabilityLOSP(uid);
    }

    static class ProcessInfoService extends android.os.IProcessInfoService.Stub {
        final com.android.server.am.ActivityManagerService mActivityManagerService;

        ProcessInfoService(com.android.server.am.ActivityManagerService activityManagerService) {
            this.mActivityManagerService = activityManagerService;
        }

        public void getProcessStatesFromPids(int[] pids, int[] states) {
            this.mActivityManagerService.getProcessStatesAndOomScoresForPIDs(pids, states, null);
        }

        public void getProcessStatesAndOomScoresFromPids(int[] pids, int[] states, int[] scores) {
            this.mActivityManagerService.getProcessStatesAndOomScoresForPIDs(pids, states, scores);
        }
    }

    public void getProcessStatesAndOomScoresForPIDs(int[] pids, int[] states, int[] scores) {
        com.android.server.am.ProcessRecord pr;
        if (scores != null) {
            enforceCallingPermission("android.permission.GET_PROCESS_STATE_AND_OOM_SCORE", "getProcessStatesAndOomScoresForPIDs()");
        }
        if (pids == null) {
            throw new java.lang.NullPointerException("pids");
        }
        if (states == null) {
            throw new java.lang.NullPointerException("states");
        }
        if (pids.length != states.length) {
            throw new java.lang.IllegalArgumentException("pids and states arrays have different lengths!");
        }
        if (scores != null && pids.length != scores.length) {
            throw new java.lang.IllegalArgumentException("pids and scores arrays have different lengths!");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            int newestTimeIndex = -1;
            long newestTime = Long.MIN_VALUE;
            for (int i = 0; i < pids.length; i++) {
                try {
                    synchronized (this.mPidsSelfLocked) {
                        pr = this.mPidsSelfLocked.get(pids[i]);
                    }
                    if (pr != null) {
                        long pendingTopTime = this.mPendingStartActivityUids.getPendingTopPidTime(pr.uid, pids[i]);
                        if (pendingTopTime != 0) {
                            states[i] = 2;
                            if (scores != null) {
                                scores[i] = -1;
                            }
                            if (pendingTopTime > newestTime) {
                                newestTimeIndex = i;
                                newestTime = pendingTopTime;
                            }
                        } else {
                            states[i] = pr.mState.getCurProcState();
                            if (scores != null) {
                                scores[i] = pr.mState.getCurAdj();
                            }
                        }
                    } else {
                        states[i] = 20;
                        if (scores != null) {
                            scores[i] = -10000;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            if (newestTimeIndex != -1 && scores != null) {
                scores[newestTimeIndex] = -2;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    static class PermissionController extends android.os.IPermissionController.Stub {
        com.android.server.am.ActivityManagerService mActivityManagerService;

        PermissionController(com.android.server.am.ActivityManagerService activityManagerService) {
            this.mActivityManagerService = activityManagerService;
        }

        public boolean checkPermission(java.lang.String permission, int pid, int uid) {
            return this.mActivityManagerService.checkPermission(permission, pid, uid) == 0;
        }

        public int noteOp(java.lang.String op, int uid, java.lang.String packageName) {
            return this.mActivityManagerService.mAppOpsService.noteOperation(android.app.AppOpsManager.strOpToOp(op), uid, packageName, null, false, "", false).getOpMode();
        }

        public java.lang.String[] getPackagesForUid(int uid) {
            return this.mActivityManagerService.mContext.getPackageManager().getPackagesForUid(uid);
        }

        public boolean isRuntimePermission(java.lang.String permission) {
            try {
                android.content.pm.PermissionInfo info = this.mActivityManagerService.mContext.getPackageManager().getPermissionInfo(permission, 0);
                return (info.protectionLevel & 15) == 1;
            } catch (android.content.pm.PackageManager.NameNotFoundException nnfe) {
                android.util.Slog.e("ActivityManager", "No such permission: " + permission, nnfe);
                return false;
            }
        }

        public int getPackageUid(java.lang.String packageName, int flags) {
            try {
                return this.mActivityManagerService.mContext.getPackageManager().getPackageUid(packageName, flags);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return -1;
            }
        }
    }

    class IntentFirewallInterface implements com.android.server.firewall.IntentFirewall.AMSInterface {
        IntentFirewallInterface() {
        }

        @Override // com.android.server.firewall.IntentFirewall.AMSInterface
        public int checkComponentPermission(java.lang.String permission, int pid, int uid, int owningUid, boolean exported) {
            return com.android.server.am.ActivityManagerService.checkComponentPermission(permission, pid, uid, owningUid, exported);
        }

        @Override // com.android.server.firewall.IntentFirewall.AMSInterface
        public java.lang.Object getAMSLock() {
            return com.android.server.am.ActivityManagerService.this;
        }
    }

    public static int checkComponentPermission(java.lang.String permission, int pid, int uid, int owningUid, boolean exported) {
        return checkComponentPermission(permission, pid, uid, 0, owningUid, exported);
    }

    public static int checkComponentPermission(java.lang.String permission, int pid, int uid, int deviceId, int owningUid, boolean exported) {
        if (pid == MY_PID) {
            return 0;
        }
        if (permission != null) {
            if (mStaticExt.checkSafeWindowPermission(permission, uid) && pid != -1) {
                return 0;
            }
            synchronized (sActiveProcessInfoSelfLocked) {
                android.content.pm.ProcessInfo procInfo = sActiveProcessInfoSelfLocked.get(pid);
                if (procInfo != null && procInfo.deniedPermissions != null && procInfo.deniedPermissions.contains(permission)) {
                    return -1;
                }
            }
        }
        return android.app.ActivityManager.checkComponentPermission(permission, uid, deviceId, owningUid, exported);
    }

    private void enforceDebuggable(com.android.server.am.ProcessRecord proc) {
        if (!android.os.Build.IS_DEBUGGABLE && !proc.isDebuggable()) {
            throw new java.lang.SecurityException("Process not debuggable: " + proc.info.packageName);
        }
    }

    private void enforceDebuggable(android.content.pm.ApplicationInfo info) {
        if (!android.os.Build.IS_DEBUGGABLE && (info.flags & 2) == 0) {
            throw new java.lang.SecurityException("Process not debuggable: " + info.packageName);
        }
    }

    public int checkPermission(java.lang.String permission, int pid, int uid) {
        return checkPermissionForDevice(permission, pid, uid, 0);
    }

    public int checkPermissionForDevice(java.lang.String permission, int pid, int uid, int deviceId) {
        if (permission == null) {
            return -1;
        }
        return checkComponentPermission(permission, pid, uid, deviceId, -1, true);
    }

    int checkCallingPermission(java.lang.String permission) {
        return checkPermission(permission, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
    }

    void enforceCallingPermission(java.lang.String permission, java.lang.String func) {
        if (checkCallingPermission(permission) == 0 || this.mActivityManagerServiceExt.enforceCallingOplusWindowPermission(this, permission)) {
            return;
        }
        java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires " + permission;
        android.util.Slog.w("ActivityManager", msg);
        throw new java.lang.SecurityException(msg);
    }

    private void enforceCallingHasAtLeastOnePermission(java.lang.String func, java.lang.String... permissions) {
        for (java.lang.String permission : permissions) {
            if (checkCallingPermission(permission) == 0) {
                return;
            }
        }
        java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires one of " + java.util.Arrays.toString(permissions);
        android.util.Slog.w("ActivityManager", msg);
        throw new java.lang.SecurityException(msg);
    }

    void enforcePermission(java.lang.String permission, int pid, int uid, java.lang.String func) {
        if (checkPermission(permission, pid, uid) == 0) {
            return;
        }
        java.lang.String msg = "Permission Denial: " + func + " from pid=" + pid + ", uid=" + uid + " requires " + permission;
        android.util.Slog.w("ActivityManager", msg);
        throw new java.lang.SecurityException(msg);
    }

    public boolean isAppStartModeDisabled(int uid, java.lang.String packageName) {
        boolean z;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                z = getAppStartModeLOSP(uid, packageName, 0, -1, false, true, false) == 3;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return z;
    }

    private boolean isInRestrictedBucket(int userId, java.lang.String packageName, long nowElapsed) {
        return 45 <= this.mUsageStatsService.getAppStandbyBucket(packageName, userId, nowElapsed);
    }

    int appRestrictedInBackgroundLOSP(int uid, java.lang.String packageName, int packageTargetSdk) {
        if (packageTargetSdk >= 26) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                android.util.Slog.i("ActivityManager", "App " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " targets O+, restricted");
            }
            return 2;
        }
        if (this.mOnBattery && this.mConstants.FORCE_BACKGROUND_CHECK_ON_RESTRICTED_APPS && isInRestrictedBucket(android.os.UserHandle.getUserId(uid), packageName, android.os.SystemClock.elapsedRealtime())) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                android.util.Slog.i("ActivityManager", "Legacy app " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " in RESTRICTED bucket");
            }
            return 1;
        }
        int appop = getAppOpsManager().noteOpNoThrow(63, uid, packageName, (java.lang.String) null, "");
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
            android.util.Slog.i("ActivityManager", "Legacy app " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " bg appop " + appop);
        }
        switch (appop) {
            case 0:
                if (this.mForceBackgroundCheck && !android.os.UserHandle.isCore(uid) && !isOnDeviceIdleAllowlistLOSP(uid, true)) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                        android.util.Slog.i("ActivityManager", "Force background check: " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " restricted");
                    }
                    return 1;
                }
                return 0;
            case 1:
                return 1;
            default:
                return 2;
        }
    }

    int appServicesRestrictedInBackgroundLOSP(int uid, java.lang.String packageName, int packageTargetSdk) {
        if (this.mPackageManagerInt.isPackagePersistent(packageName)) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                android.util.Slog.i("ActivityManager", "App " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " is persistent; not restricted in background");
            }
            return 0;
        }
        if (uidOnBackgroundAllowlistLOSP(uid)) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                android.util.Slog.i("ActivityManager", "App " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " on background allowlist; not restricted in background");
            }
            return 0;
        }
        if (isOnDeviceIdleAllowlistLOSP(uid, false)) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                android.util.Slog.i("ActivityManager", "App " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " on idle allowlist; not restricted in background");
            }
            return 0;
        }
        if (this.mActivityManagerServiceExt.isOnBackgroundServiceWhitelist(packageName, uid)) {
            return 0;
        }
        return appRestrictedInBackgroundLOSP(uid, packageName, packageTargetSdk);
    }

    int getAppStartModeLOSP(int uid, java.lang.String packageName, int packageTargetSdk, int callingPid, boolean alwaysRestrict, boolean disabledOnly, boolean forcedStandby) {
        boolean ephemeral;
        int startMode;
        com.android.server.am.ProcessRecord proc;
        if (this.mInternal.isPendingTopUid(uid)) {
            return 0;
        }
        com.android.server.am.UidRecord uidRec = this.mProcessList.getUidRecordLOSP(uid);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
            android.util.Slog.d("ActivityManager", "checkAllowBackground: uid=" + uid + " pkg=" + packageName + " rec=" + uidRec + " always=" + alwaysRestrict + " idle=" + (uidRec != null ? uidRec.isIdle() : false));
        }
        if (uidRec != null && !alwaysRestrict && !forcedStandby && !uidRec.isIdle()) {
            return 0;
        }
        if (uidRec == null) {
            ephemeral = getPackageManagerInternal().isPackageEphemeral(android.os.UserHandle.getUserId(uid), packageName);
        } else {
            ephemeral = uidRec.isEphemeral();
        }
        if (ephemeral) {
            return 3;
        }
        if (disabledOnly) {
            return 0;
        }
        if (alwaysRestrict) {
            startMode = appRestrictedInBackgroundLOSP(uid, packageName, packageTargetSdk);
        } else {
            startMode = appServicesRestrictedInBackgroundLOSP(uid, packageName, packageTargetSdk);
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
            android.util.Slog.d("ActivityManager", "checkAllowBackground: uid=" + uid + " pkg=" + packageName + " startMode=" + startMode + " onallowlist=" + isOnDeviceIdleAllowlistLOSP(uid, false) + " onallowlist(ei)=" + isOnDeviceIdleAllowlistLOSP(uid, true));
        }
        if (startMode == 1 && callingPid >= 0) {
            synchronized (this.mPidsSelfLocked) {
                proc = this.mPidsSelfLocked.get(callingPid);
            }
            if (proc != null && !android.app.ActivityManager.isProcStateBackground(proc.mState.getCurProcState())) {
                return 0;
            }
        }
        return startMode;
    }

    boolean isOnDeviceIdleAllowlistLOSP(int uid, boolean allowExceptIdleToo) {
        int[] allowlist;
        int appId = android.os.UserHandle.getAppId(uid);
        if (allowExceptIdleToo) {
            allowlist = this.mDeviceIdleExceptIdleAllowlist;
        } else {
            allowlist = this.mDeviceIdleAllowlist;
        }
        return java.util.Arrays.binarySearch(allowlist, appId) >= 0 || java.util.Arrays.binarySearch(this.mDeviceIdleTempAllowlist, appId) >= 0 || this.mPendingTempAllowlist.get(uid) != null;
    }

    com.android.server.am.ActivityManagerService.FgsTempAllowListItem isAllowlistedForFgsStartLOSP(int uid) {
        if (java.util.Arrays.binarySearch(this.mDeviceIdleExceptIdleAllowlist, android.os.UserHandle.getAppId(uid)) >= 0) {
            return FAKE_TEMP_ALLOW_LIST_ITEM;
        }
        android.util.Pair<java.lang.Long, com.android.server.am.ActivityManagerService.FgsTempAllowListItem> entry = this.mFgsStartTempAllowList.get(uid);
        if (entry == null) {
            return null;
        }
        return (com.android.server.am.ActivityManagerService.FgsTempAllowListItem) entry.second;
    }

    private static class GetBackgroundStartPrivilegesFunctor implements java.util.function.Consumer<com.android.server.am.ProcessRecord> {
        private android.app.BackgroundStartPrivileges mBackgroundStartPrivileges;
        private int mUid;

        private GetBackgroundStartPrivilegesFunctor() {
            this.mBackgroundStartPrivileges = android.app.BackgroundStartPrivileges.NONE;
        }

        void prepare(int uid) {
            this.mUid = uid;
            this.mBackgroundStartPrivileges = android.app.BackgroundStartPrivileges.NONE;
        }

        android.app.BackgroundStartPrivileges getResult() {
            return this.mBackgroundStartPrivileges;
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.am.ProcessRecord pr) {
            if (pr.uid == this.mUid) {
                this.mBackgroundStartPrivileges = this.mBackgroundStartPrivileges.merge(pr.getBackgroundStartPrivileges());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.BackgroundStartPrivileges getBackgroundStartPrivileges(int uid) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.am.UidRecord uidRecord = this.mProcessList.getUidRecordLOSP(uid);
                if (uidRecord == null) {
                    android.app.BackgroundStartPrivileges backgroundStartPrivileges = android.app.BackgroundStartPrivileges.NONE;
                    resetPriorityAfterProcLockedSection();
                    return backgroundStartPrivileges;
                }
                this.mGetBackgroundStartPrivilegesFunctor.prepare(uid);
                uidRecord.forEachProcess(this.mGetBackgroundStartPrivilegesFunctor);
                android.app.BackgroundStartPrivileges result = this.mGetBackgroundStartPrivilegesFunctor.getResult();
                resetPriorityAfterProcLockedSection();
                return result;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    static boolean doesReasonCodeAllowSchedulingUserInitiatedJobs(int reasonCode, int uid) {
        switch (reasonCode) {
            case 10:
            case 11:
            case 12:
            case 13:
            case 50:
            case 51:
            case 53:
            case 57:
            case 58:
            case 60:
            case 67:
                break;
            case 62:
                if (!com.android.server.am.Flags.fgsDisableSaw() || !android.app.compat.CompatChanges.isChangeEnabled(com.android.server.am.ActiveServices.FGS_SAW_RESTRICTIONS, uid)) {
                }
                break;
        }
        return true;
    }

    private boolean isProcessInStateToScheduleUserInitiatedJobsLocked(com.android.server.am.ProcessRecord pr, long nowElapsed, int uid) {
        if (pr == null) {
            return false;
        }
        android.app.BackgroundStartPrivileges backgroundStartPrivileges = pr.getBackgroundStartPrivileges();
        if (backgroundStartPrivileges.allowsBackgroundActivityStarts()) {
            return true;
        }
        com.android.server.am.ProcessStateRecord state = pr.mState;
        int procstate = state.getCurProcState();
        if (procstate <= 3 && doesReasonCodeAllowSchedulingUserInitiatedJobs(android.os.PowerExemptionManager.getReasonCodeFromProcState(procstate), uid)) {
            return true;
        }
        long lastInvisibleTime = state.getLastInvisibleTime();
        if (lastInvisibleTime > 0 && lastInvisibleTime < Long.MAX_VALUE) {
            long timeSinceVisibleMs = nowElapsed - lastInvisibleTime;
            if (timeSinceVisibleMs < this.mConstants.mVisibleToInvisibleUijScheduleGraceDurationMs) {
                return true;
            }
        }
        com.android.server.am.ProcessServiceRecord psr = pr.mServices;
        if (psr != null && psr.hasForegroundServices()) {
            for (int s = psr.numberOfRunningServices() - 1; s >= 0; s--) {
                com.android.server.am.ServiceRecord sr = psr.getRunningServiceAt(s);
                if (sr.isForeground && sr.mAllowUiJobScheduling) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean canScheduleUserInitiatedJobs(int uid, int pid, java.lang.String pkgName) {
        com.android.server.am.ProcessRecord processRecord;
        android.app.BackgroundStartPrivileges backgroundStartPrivileges;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                synchronized (this.mPidsSelfLocked) {
                    processRecord = this.mPidsSelfLocked.get(pid);
                }
                long nowElapsed = android.os.SystemClock.elapsedRealtime();
                if (processRecord != null) {
                    if (isProcessInStateToScheduleUserInitiatedJobsLocked(processRecord, nowElapsed, uid)) {
                        resetPriorityAfterLockedSection();
                        return true;
                    }
                    backgroundStartPrivileges = processRecord.getBackgroundStartPrivileges();
                } else {
                    android.app.BackgroundStartPrivileges backgroundStartPrivileges2 = getBackgroundStartPrivileges(uid);
                    backgroundStartPrivileges = backgroundStartPrivileges2;
                }
                if (backgroundStartPrivileges.allowsBackgroundActivityStarts()) {
                    resetPriorityAfterLockedSection();
                    return true;
                }
                if (this.mServices.canAllowWhileInUsePermissionInFgsLocked(pid, uid, pkgName, processRecord, backgroundStartPrivileges)) {
                    resetPriorityAfterLockedSection();
                    return true;
                }
                com.android.server.am.UidRecord uidRecord = this.mProcessList.getUidRecordLOSP(uid);
                boolean hasSawPermission = this.mAtmInternal.hasSystemAlertWindowPermission(uid, pid, pkgName);
                boolean strictSawCheckEnabled = com.android.server.am.Flags.fgsDisableSaw() && android.app.compat.CompatChanges.isChangeEnabled(com.android.server.am.ActiveServices.FGS_SAW_RESTRICTIONS, uid);
                if (uidRecord != null) {
                    for (int i = uidRecord.getNumOfProcs() - 1; i >= 0; i--) {
                        com.android.server.am.ProcessRecord pr = uidRecord.getProcessRecordByIndex(i);
                        if (isProcessInStateToScheduleUserInitiatedJobsLocked(pr, nowElapsed, uid)) {
                            resetPriorityAfterLockedSection();
                            return true;
                        }
                        if (hasSawPermission && strictSawCheckEnabled && pr != null && pr.mState.hasOverlayUi()) {
                            resetPriorityAfterLockedSection();
                            return true;
                        }
                    }
                }
                if (hasSawPermission && !strictSawCheckEnabled) {
                    resetPriorityAfterLockedSection();
                    return true;
                }
                int userId = android.os.UserHandle.getUserId(uid);
                boolean isCompanionApp = this.mInternal.isAssociatedCompanionApp(userId, uid);
                if (isCompanionApp && checkPermission("android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND", pid, uid) == 0) {
                    resetPriorityAfterLockedSection();
                    return true;
                }
                resetPriorityAfterLockedSection();
                return false;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    java.lang.String getPendingTempAllowlistTagForUidLOSP(int uid) {
        com.android.server.am.ActivityManagerService.PendingTempAllowlist ptw = this.mPendingTempAllowlist.get(uid);
        if (ptw != null) {
            return ptw.tag;
        }
        return null;
    }

    public void grantImplicitAccess(int userId, android.content.Intent intent, int visibleUid, int recipientAppId) {
        getPackageManagerInternal().grantImplicitAccess(userId, intent, recipientAppId, visibleUid, true);
    }

    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, int userId, android.os.IBinder callerToken) {
        return checkUriPermission(uri, pid, uid, modeFlags, userId, false, "checkUriPermission");
    }

    public int checkContentUriPermissionFull(android.net.Uri uri, int pid, int uid, int modeFlags, int userId) {
        return checkUriPermission(uri, pid, uid, modeFlags, userId, true, "checkContentUriPermissionFull");
    }

    private int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, int userId, boolean isFullAccessForContentUri, java.lang.String methodName) {
        enforceNotIsolatedCaller(methodName);
        if (pid == MY_PID) {
            return 0;
        }
        if (uid != 0 && this.mPackageManagerInt.filterAppAccess(uid, android.os.Binder.getCallingUid())) {
            return -1;
        }
        boolean granted = this.mUgmInternal.checkUriPermission(new com.android.server.uri.GrantUri(userId, uri, modeFlags), uid, modeFlags, isFullAccessForContentUri);
        return granted ? 0 : -1;
    }

    public int[] checkUriPermissions(java.util.List<android.net.Uri> uris, int pid, int uid, int modeFlags, int userId, android.os.IBinder callerToken) {
        int size = uris.size();
        int[] res = new int[size];
        java.util.Arrays.fill(res, -1);
        for (int i = 0; i < size; i++) {
            android.net.Uri uri = uris.get(i);
            int userIdFromUri = android.content.ContentProvider.getUserIdFromUri(uri, userId);
            res[i] = checkUriPermission(android.content.ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, userIdFromUri, callerToken);
        }
        return res;
    }

    public void grantUriPermission(android.app.IApplicationThread caller, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int userId) {
        enforceNotIsolatedCaller("grantUriPermission");
        com.android.server.uri.GrantUri grantUri = new com.android.server.uri.GrantUri(userId, uri, modeFlags);
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ProcessRecord r = getRecordForAppLOSP(caller);
                if (r == null) {
                    throw new java.lang.SecurityException("Unable to find app for caller " + caller + " when granting permission to uri " + grantUri);
                }
                if (targetPkg == null) {
                    throw new java.lang.IllegalArgumentException("null target");
                }
                int callingUserId = android.os.UserHandle.getUserId(r.uid);
                if (this.mPackageManagerInt.filterAppAccess(targetPkg, r.uid, callingUserId)) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                com.android.internal.util.Preconditions.checkFlagsArgument(modeFlags, 195);
                android.content.Intent intent = new android.content.Intent();
                intent.setData(android.content.ContentProvider.maybeAddUserId(uri, userId));
                intent.setFlags(modeFlags);
                com.android.server.uri.NeededUriGrants needed = this.mUgmInternal.checkGrantUriPermissionFromIntent(intent, r.uid, targetPkg, callingUserId);
                this.mUgmInternal.grantUriPermissionUncheckedFromIntent(needed, null);
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void revokeUriPermission(android.app.IApplicationThread caller, java.lang.String targetPackage, android.net.Uri uri, int modeFlags, int userId) {
        enforceNotIsolatedCaller("revokeUriPermission");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ProcessRecord r = getRecordForAppLOSP(caller);
                if (r == null) {
                    throw new java.lang.SecurityException("Unable to find app for caller " + caller + " when revoking permission to uri " + uri);
                }
                if (uri == null) {
                    android.util.Slog.w("ActivityManager", "revokeUriPermission: null uri");
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!android.content.Intent.isAccessUriMode(modeFlags)) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                java.lang.String authority = uri.getAuthority();
                android.content.pm.ProviderInfo pi = this.mCpHelper.getProviderInfoLocked(authority, userId, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED);
                if (pi == null) {
                    android.util.Slog.w("ActivityManager", "No content provider found for permission revoke: " + uri.toSafeString());
                    resetPriorityAfterLockedSection();
                } else {
                    this.mUgmInternal.revokeUriPermission(targetPackage, r.uid, new com.android.server.uri.GrantUri(userId, uri, modeFlags), modeFlags);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void showWaitingForDebugger(android.app.IApplicationThread who, boolean waiting) {
        com.android.server.am.ProcessRecord app;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            if (who == null) {
                app = null;
            } else {
                try {
                    app = getRecordForAppLOSP(who);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            if (app == null) {
                resetPriorityAfterProcLockedSection();
                return;
            }
            android.os.Message msg = android.os.Message.obtain();
            msg.what = 6;
            msg.obj = app;
            msg.arg1 = waiting ? 1 : 0;
            this.mUiHandler.sendMessage(msg);
            resetPriorityAfterProcLockedSection();
        }
    }

    public void getMemoryInfo(android.app.ActivityManager.MemoryInfo outInfo) {
        this.mProcessList.getMemoryInfo(outInfo);
    }

    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getTasks(int maxNum) {
        return this.mActivityTaskManager.getTasks(maxNum);
    }

    public void cancelTaskWindowTransition(int taskId) {
        this.mActivityTaskManager.cancelTaskWindowTransition(taskId);
    }

    public void setTaskResizeable(int taskId, int resizeableMode) {
        this.mActivityTaskManager.setTaskResizeable(taskId, resizeableMode);
    }

    public void resizeTask(int taskId, android.graphics.Rect bounds, int resizeMode) {
        this.mActivityTaskManager.resizeTask(taskId, bounds, resizeMode);
    }

    public android.graphics.Rect getTaskBounds(int taskId) {
        return this.mActivityTaskManager.getTaskBounds(taskId);
    }

    public boolean removeTask(int taskId) {
        return this.mActivityTaskManager.removeTask(taskId);
    }

    public void moveTaskToFront(android.app.IApplicationThread appThread, java.lang.String callingPackage, int taskId, int flags, android.os.Bundle bOptions) {
        this.mActivityTaskManager.moveTaskToFront(appThread, callingPackage, taskId, flags, bOptions);
    }

    public boolean moveActivityTaskToBack(android.os.IBinder token, boolean nonRoot) {
        return android.app.ActivityClient.getInstance().moveActivityTaskToBack(token, nonRoot);
    }

    public void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop) {
        this.mActivityTaskManager.moveTaskToRootTask(taskId, rootTaskId, toTop);
    }

    public android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) {
        return this.mActivityTaskManager.getRecentTasks(maxNum, flags, userId);
    }

    public java.util.List<android.app.ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() {
        return this.mActivityTaskManager.getAllRootTaskInfos();
    }

    public int getTaskForActivity(android.os.IBinder token, boolean onlyRoot) {
        return android.app.ActivityClient.getInstance().getTaskForActivity(token, onlyRoot);
    }

    public void updateLockTaskPackages(int userId, java.lang.String[] packages) {
        this.mActivityTaskManager.updateLockTaskPackages(userId, packages);
    }

    public boolean isInLockTaskMode() {
        return this.mActivityTaskManager.isInLockTaskMode();
    }

    public int getLockTaskModeState() {
        return this.mActivityTaskManager.getLockTaskModeState();
    }

    public void startSystemLockTaskMode(int taskId) throws android.os.RemoteException {
        this.mActivityTaskManager.startSystemLockTaskMode(taskId);
    }

    public android.content.pm.IPackageManager getPackageManager() {
        return android.app.AppGlobals.getPackageManager();
    }

    public android.content.pm.PackageManagerInternal getPackageManagerInternal() {
        if (this.mPackageManagerInt == null) {
            this.mPackageManagerInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }
        return this.mPackageManagerInt;
    }

    private com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManagerInternal() {
        if (this.mPermissionManagerInt == null) {
            this.mPermissionManagerInt = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
        }
        return this.mPermissionManagerInt;
    }

    private com.android.server.am.AccessCheckDelegateHelper getAccessCheckDelegateHelper() {
        if (this.mAccessCheckDelegateHelper == null) {
            this.mAccessCheckDelegateHelper = new com.android.server.am.AccessCheckDelegateHelper(this.mProcLock, this.mActiveInstrumentation, this.mAppOpsService, getPermissionManagerInternal());
        }
        return this.mAccessCheckDelegateHelper;
    }

    boolean wasPackageEverLaunched(java.lang.String packageName, int userId) {
        try {
            boolean wasLaunched = getPackageManagerInternal().wasPackageEverLaunched(packageName, userId);
            return wasLaunched;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private android.content.pm.TestUtilityService getTestUtilityServiceLocked() {
        if (this.mTestUtilityService == null) {
            this.mTestUtilityService = (android.content.pm.TestUtilityService) com.android.server.LocalServices.getService(android.content.pm.TestUtilityService.class);
        }
        return this.mTestUtilityService;
    }

    public void appNotResponding(java.lang.String reason) {
        appNotResponding(reason, false);
    }

    public void appNotResponding(java.lang.String reason, boolean isContinuousAnr) {
        com.android.internal.os.TimeoutRecord timeoutRecord = com.android.internal.os.TimeoutRecord.forApp("App requested: " + reason);
        int callingPid = android.os.Binder.getCallingPid();
        timeoutRecord.mLatencyTracker.waitingOnPidLockStarted();
        synchronized (this.mPidsSelfLocked) {
            timeoutRecord.mLatencyTracker.waitingOnPidLockEnded();
            com.android.server.am.ProcessRecord app = this.mPidsSelfLocked.get(callingPid);
            if (app == null) {
                throw new java.lang.SecurityException("Unknown process: " + callingPid);
            }
            this.mAnrHelper.appNotResponding(app, null, app.info, null, null, false, timeoutRecord, isContinuousAnr);
        }
    }

    void appNotResponding(com.android.server.am.ProcessRecord anrProcess, com.android.internal.os.TimeoutRecord timeoutRecord) {
        this.mAnrHelper.appNotResponding(anrProcess, timeoutRecord);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appNotResponding(java.lang.String processName, int uid, com.android.internal.os.TimeoutRecord timeoutRecord) {
        java.util.Objects.requireNonNull(processName);
        java.util.Objects.requireNonNull(timeoutRecord);
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ProcessRecord app = getProcessRecordLocked(processName, uid);
                if (app == null) {
                    android.util.Slog.e("ActivityManager", "Unknown process: " + processName);
                    resetPriorityAfterLockedSection();
                } else {
                    this.mAnrHelper.appNotResponding(app, timeoutRecord);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void startPersistentApps(int matchFlags) {
        com.android.server.am.ProcessRecord proc;
        if (this.mFactoryTest == 1) {
            return;
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                try {
                    java.util.List<android.content.pm.ApplicationInfo> apps = android.app.AppGlobals.getPackageManager().getPersistentApplications(matchFlags | 1024).getList();
                    this.mActivityManagerServiceExt.reorderPersistAppsIfNeeded(apps);
                    for (android.content.pm.ApplicationInfo app : apps) {
                        if (!com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(app.packageName) && (proc = addAppLocked(app, null, false, null, 2)) != null) {
                            proc.mProfile.addHostingComponentType(2);
                        }
                    }
                } catch (android.os.RemoteException e) {
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public com.android.server.am.ContentProviderHelper getContentProviderHelper() {
        return this.mCpHelper;
    }

    public final android.app.ContentProviderHolder getContentProvider(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String name, int userId, boolean stable) {
        traceBegin(64L, "getContentProvider: ", name);
        long callStart = sAnrLogEnhancementHelper.getCallStartTime();
        int callingPid = android.os.Binder.getCallingPid();
        this.mActivityManagerServiceExt.hookBeforeGetContentProvider(caller, callingPackage, name, userId, stable);
        try {
            return this.mCpHelper.getContentProvider(caller, callingPackage, name, userId, stable);
        } finally {
            sAnrLogEnhancementHelper.printSlowLog(callStart, "getContentProvider", callingPid, true);
            this.mActivityManagerServiceExt.hookAfterGetContentProvider();
            android.os.Trace.traceEnd(64L);
        }
    }

    public android.app.ContentProviderHolder getContentProviderExternal(java.lang.String name, int userId, android.os.IBinder token, java.lang.String tag) {
        traceBegin(64L, "getContentProviderExternal: ", name);
        try {
            return this.mCpHelper.getContentProviderExternal(name, userId, token, tag);
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    public void removeContentProvider(android.os.IBinder connection, boolean stable) {
        this.mCpHelper.removeContentProvider(connection, stable);
    }

    @java.lang.Deprecated
    public void removeContentProviderExternal(java.lang.String name, android.os.IBinder token) {
        traceBegin(64L, "removeContentProviderExternal: ", name);
        try {
            removeContentProviderExternalAsUser(name, token, android.os.UserHandle.getCallingUserId());
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    public void removeContentProviderExternalAsUser(java.lang.String name, android.os.IBinder token, int userId) {
        traceBegin(64L, "removeContentProviderExternalAsUser: ", name);
        try {
            this.mCpHelper.removeContentProviderExternalAsUser(name, token, userId);
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    public final void publishContentProviders(android.app.IApplicationThread caller, java.util.List<android.app.ContentProviderHolder> providers) {
        if (android.os.Trace.isTagEnabled(64L)) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(256);
            sb.append("publishContentProviders: ");
            if (providers != null) {
                boolean first = true;
                int i = 0;
                int size = providers.size();
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    android.app.ContentProviderHolder holder = providers.get(i);
                    if (holder != null && holder.info != null && holder.info.authority != null) {
                        int len = holder.info.authority.length();
                        if (sb.length() + len > 256) {
                            sb.append("[[TRUNCATED]]");
                            break;
                        }
                        if (!first) {
                            sb.append(';');
                        } else {
                            first = false;
                        }
                        sb.append(holder.info.authority);
                    }
                    i++;
                }
            }
            android.os.Trace.traceBegin(64L, sb.toString());
        }
        try {
            long callStart = sAnrLogEnhancementHelper.getCallStartTime();
            int callingPid = android.os.Binder.getCallingPid();
            this.mCpHelper.publishContentProviders(caller, providers);
            sAnrLogEnhancementHelper.printSlowLog(callStart, "publishContentProviders", callingPid, true);
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    public boolean refContentProvider(android.os.IBinder connection, int stable, int unstable) {
        return this.mCpHelper.refContentProvider(connection, stable, unstable);
    }

    public void unstableProviderDied(android.os.IBinder connection) {
        this.mCpHelper.unstableProviderDied(connection);
    }

    public void appNotRespondingViaProvider(android.os.IBinder connection) {
        this.mCpHelper.appNotRespondingViaProvider(connection);
    }

    public void getMimeTypeFilterAsync(android.net.Uri uri, int userId, android.os.RemoteCallback resultCallback) {
        this.mCpHelper.getMimeTypeFilterAsync(uri, userId, resultCallback);
    }

    private boolean uidOnBackgroundAllowlistLOSP(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        int[] allowlist = this.mBackgroundAppIdAllowlist;
        for (int i : allowlist) {
            if (appId == i) {
                return true;
            }
        }
        return false;
    }

    public boolean isBackgroundRestricted(java.lang.String packageName) {
        int callingUid = android.os.Binder.getCallingUid();
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        try {
            int packageUid = pm.getPackageUid(packageName, 268435456L, android.os.UserHandle.getUserId(callingUid));
            if (packageUid != callingUid) {
                throw new java.lang.IllegalArgumentException("Uid " + callingUid + " cannot query restriction state for package " + packageName);
            }
        } catch (android.os.RemoteException e) {
        }
        return isBackgroundRestrictedNoCheck(callingUid, packageName);
    }

    public boolean isBackgroundRestrictedNoCheck(int uid, java.lang.String packageName) {
        int mode = getAppOpsManager().checkOpNoThrow(70, uid, packageName);
        return mode != 0;
    }

    public void backgroundAllowlistUid(int uid) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only the OS may call backgroundAllowlistUid()");
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
            android.util.Slog.i("ActivityManager", "Adding uid " + uid + " to bg uid allowlist");
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        int num = this.mBackgroundAppIdAllowlist.length;
                        int[] newList = new int[num + 1];
                        java.lang.System.arraycopy(this.mBackgroundAppIdAllowlist, 0, newList, 0, num);
                        newList[num] = android.os.UserHandle.getAppId(uid);
                        this.mBackgroundAppIdAllowlist = newList;
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
    }

    final com.android.server.am.ProcessRecord addAppLocked(android.content.pm.ApplicationInfo info, java.lang.String customProcess, boolean isolated, java.lang.String abiOverride, int zygotePolicyFlags) {
        return addAppLocked(info, customProcess, isolated, false, abiOverride, zygotePolicyFlags);
    }

    final com.android.server.am.ProcessRecord addAppLocked(android.content.pm.ApplicationInfo info, java.lang.String customProcess, boolean isolated, boolean disableHiddenApiChecks, java.lang.String abiOverride, int zygotePolicyFlags) {
        return addAppLocked(info, customProcess, isolated, disableHiddenApiChecks, false, abiOverride, zygotePolicyFlags);
    }

    final com.android.server.am.ProcessRecord addAppLocked(android.content.pm.ApplicationInfo info, java.lang.String customProcess, boolean isolated, boolean disableHiddenApiChecks, boolean disableTestApiChecks, java.lang.String abiOverride, int zygotePolicyFlags) {
        return addAppLocked(info, customProcess, isolated, false, 0, null, disableHiddenApiChecks, disableTestApiChecks, abiOverride, zygotePolicyFlags);
    }

    final com.android.server.am.ProcessRecord addAppLocked(android.content.pm.ApplicationInfo info, java.lang.String customProcess, boolean isolated, boolean isSdkSandbox, int sdkSandboxUid, java.lang.String sdkSandboxClientAppPackage, boolean disableHiddenApiChecks, boolean disableTestApiChecks, java.lang.String abiOverride, int zygotePolicyFlags) {
        com.android.server.am.ProcessRecord app;
        com.android.server.am.ProcessRecord app2;
        if (!isolated) {
            app = getProcessRecordLocked(customProcess != null ? customProcess : info.processName, info.uid);
        } else {
            app = null;
        }
        if (app != null) {
            app2 = app;
        } else {
            com.android.server.am.ProcessRecord app3 = this.mProcessList.newProcessRecordLocked(info, customProcess, isolated, 0, isSdkSandbox, sdkSandboxUid, sdkSandboxClientAppPackage, new com.android.server.am.HostingRecord(com.android.server.am.HostingRecord.HOSTING_TYPE_ADDED_APPLICATION, customProcess != null ? customProcess : info.processName));
            updateLruProcessLocked(app3, false, null);
            updateOomAdjLocked(app3, 11);
            app2 = app3;
        }
        this.mUsageStatsService.reportEvent(info.packageName, android.os.UserHandle.getUserId(app2.uid), 31);
        if (!isSdkSandbox) {
            try {
                this.mPackageManagerInt.setPackageStoppedState(info.packageName, false, android.os.UserHandle.getUserId(app2.uid));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.w("ActivityManager", "Failed trying to unstop package " + info.packageName + ": " + e);
            }
        }
        if ((info.flags & 9) == 9) {
            app2.setPersistent(true);
            app2.mState.setMaxAdj(com.android.server.am.ProcessList.PERSISTENT_PROC_ADJ);
        }
        if (app2.getThread() == null && this.mPersistentStartingProcesses.indexOf(app2) < 0) {
            this.mPersistentStartingProcesses.add(app2);
            this.mProcessList.startProcessLocked(app2, new com.android.server.am.HostingRecord(com.android.server.am.HostingRecord.HOSTING_TYPE_ADDED_APPLICATION, customProcess != null ? customProcess : app2.processName), zygotePolicyFlags, disableHiddenApiChecks, disableTestApiChecks, abiOverride);
        }
        return app2;
    }

    public void unhandledBack() {
        this.mActivityTaskManager.unhandledBack();
    }

    public android.os.ParcelFileDescriptor openContentUri(java.lang.String uriString) throws android.os.RemoteException {
        enforceNotIsolatedCaller("openContentUri");
        int userId = android.os.UserHandle.getCallingUserId();
        android.net.Uri uri = android.net.Uri.parse(uriString);
        java.lang.String name = uri.getAuthority();
        android.app.ContentProviderHolder cph = this.mCpHelper.getContentProviderExternalUnchecked(name, null, android.os.Binder.getCallingUid(), "*opencontent*", userId);
        if (cph == null) {
            android.util.Slog.d("ActivityManager", "Failed to get provider for authority '" + name + "'");
            return null;
        }
        try {
            int uid = android.os.Binder.getCallingUid();
            java.lang.String packageName = android.app.AppOpsManager.resolvePackageName(uid, null);
            com.android.server.pm.pkg.AndroidPackage androidPackage = packageName != null ? this.mPackageManagerInt.getPackage(packageName) : this.mPackageManagerInt.getPackage(uid);
            if (androidPackage == null) {
                android.util.Log.e("ActivityManager", "Cannot find package for uid: " + uid);
                return null;
            }
            android.content.pm.ApplicationInfo appInfo = this.mPackageManagerInt.getApplicationInfo(androidPackage.getPackageName(), 0L, 1000, 0);
            if (!appInfo.isVendor() && !appInfo.isSystemApp() && !appInfo.isSystemExt() && !appInfo.isProduct()) {
                android.util.Log.e("ActivityManager", "openContentUri may only be used by vendor/system/product.");
                return null;
            }
            android.content.AttributionSource attributionSource = new android.content.AttributionSource(android.os.Binder.getCallingUid(), androidPackage.getPackageName(), null);
            android.os.ParcelFileDescriptor pfd = cph.provider.openFile(attributionSource, uri, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD, (android.os.ICancellationSignal) null);
            return pfd;
        } catch (java.io.FileNotFoundException e) {
            return null;
        } finally {
            this.mCpHelper.removeContentProviderExternalUnchecked(name, null, userId);
        }
    }

    void reportGlobalUsageEvent(int event) {
        int currentUserId = this.mUserController.getCurrentUserId();
        this.mUsageStatsService.reportEvent(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, currentUserId, event);
        int[] profiles = this.mUserController.getCurrentProfileIds();
        if (profiles != null) {
            for (int i = profiles.length - 1; i >= 0; i--) {
                if (profiles[i] != currentUserId) {
                    this.mUsageStatsService.reportEvent(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, profiles[i], event);
                }
            }
        }
    }

    void reportCurWakefulnessUsageEvent() {
        int i;
        if (this.mWakefulness.get() == 1) {
            i = 15;
        } else {
            i = 16;
        }
        reportGlobalUsageEvent(i);
    }

    void onWakefulnessChanged(int wakefulness) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                boolean wasAwake = this.mWakefulness.getAndSet(wakefulness) == 1;
                boolean isAwake = wakefulness == 1;
                if (isScreenOn != isAwake) {
                    isScreenOn = isAwake;
                    if (isScreenOn) {
                        this.mActivityManagerServiceExt.ormsSetNotification(isScreenOn);
                    }
                }
                if (wasAwake != isAwake) {
                    this.mServices.updateScreenStateLocked(isAwake);
                    reportCurWakefulnessUsageEvent();
                    this.mActivityTaskManager.onScreenAwakeChanged(isAwake);
                    this.mOomAdjuster.onWakefulnessChanged(wakefulness);
                    updateOomAdjLocked(9);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void notifyCleartextNetwork(int uid, byte[] firstPacket) {
        this.mHandler.obtainMessage(49, uid, 0, firstPacket).sendToTarget();
    }

    public boolean shutdown(int timeout) throws java.io.IOException {
        if (checkCallingPermission("android.permission.SHUTDOWN") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SHUTDOWN");
        }
        this.mActivityManagerServiceExt.hookShutdown();
        boolean timedout = this.mAtmInternal.shuttingDown(this.mBooted, timeout);
        this.mAppOpsService.shutdown();
        if (this.mUsageStatsService != null) {
            this.mUsageStatsService.prepareShutdown();
        }
        this.mBatteryStatsService.shutdown();
        this.mProcessStats.shutdown();
        return timedout;
    }

    public void notifyLockedProfile(int userId) {
        this.mAtmInternal.notifyLockedProfile(userId);
    }

    public void startConfirmDeviceCredentialIntent(android.content.Intent intent, android.os.Bundle options) {
        this.mAtmInternal.startConfirmDeviceCredentialIntent(intent, options);
    }

    public void stopAppSwitches() {
        this.mActivityTaskManager.stopAppSwitches();
    }

    public void resumeAppSwitches() {
        this.mActivityTaskManager.resumeAppSwitches();
    }

    public void setDebugApp(java.lang.String packageName, boolean waitForDebugger, boolean persistent) {
        setDebugApp(packageName, waitForDebugger, persistent, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDebugApp(java.lang.String packageName, boolean waitForDebugger, boolean persistent, boolean suspendUponWait) {
        enforceCallingPermission("android.permission.SET_DEBUG_APP", "setDebugApp()");
        long ident = android.os.Binder.clearCallingIdentity();
        boolean z = true;
        if (persistent) {
            try {
                android.content.ContentResolver resolver = this.mContext.getContentResolver();
                android.provider.Settings.Global.putString(resolver, "debug_app", packageName);
                android.provider.Settings.Global.putInt(resolver, "wait_for_debugger", waitForDebugger ? 1 : 0);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            if (!persistent) {
                try {
                    this.mOrigDebugApp = this.mDebugApp;
                    this.mOrigWaitForDebugger = this.mWaitForDebugger;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            this.mDebugApp = packageName;
            this.mWaitForDebugger = waitForDebugger;
            this.mSuspendUponWait = suspendUponWait;
            if (persistent) {
                z = false;
            }
            this.mDebugTransient = z;
            if (packageName != null) {
                forceStopPackageLocked(packageName, -1, false, false, true, true, false, false, -1, "set debug app");
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setAgentApp(java.lang.String packageName, java.lang.String agent) {
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        synchronized (this.mAppProfiler.mProfilerLock) {
            this.mAppProfiler.setAgentAppLPf(packageName, agent);
        }
    }

    void setTrackAllocationApp(android.content.pm.ApplicationInfo app, java.lang.String processName) {
        enforceDebuggable(app);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mTrackAllocationApp = processName;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    void setProfileApp(android.content.pm.ApplicationInfo app, java.lang.String processName, android.app.ProfilerInfo profilerInfo, android.content.pm.ApplicationInfo sdkSandboxClientApp) {
        synchronized (this.mAppProfiler.mProfilerLock) {
            if (!android.os.Build.IS_DEBUGGABLE) {
                boolean z = true;
                boolean isAppDebuggable = (app.flags & 2) != 0;
                boolean isAppProfileable = app.isProfileableByShell();
                if (sdkSandboxClientApp != null) {
                    if ((sdkSandboxClientApp.flags & 2) == 0) {
                        z = false;
                    }
                    isAppDebuggable |= z;
                    isAppProfileable |= sdkSandboxClientApp.isProfileableByShell();
                }
                if (!isAppDebuggable && !isAppProfileable) {
                    throw new java.lang.SecurityException("Process not debuggable, and not profileable by shell: " + app.packageName);
                }
            }
            this.mAppProfiler.setProfileAppLPf(processName, profilerInfo);
        }
    }

    void setNativeDebuggingAppLocked(android.content.pm.ApplicationInfo app, java.lang.String processName) {
        enforceDebuggable(app);
        this.mNativeDebuggingApp = processName;
    }

    public void setAlwaysFinish(boolean enabled) {
        enforceCallingPermission("android.permission.SET_ALWAYS_FINISH", "setAlwaysFinish()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "always_finish_activities", enabled ? 1 : 0);
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    this.mAlwaysFinishActivities = enabled;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setActivityController(android.app.IActivityController controller, boolean imAMonkey) {
        if (controller != null) {
            android.os.Binder.allowBlocking(controller.asBinder());
        }
        this.mActivityTaskManager.setActivityController(controller, imAMonkey);
    }

    public void setUserIsMonkey(boolean userIsMonkey) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                synchronized (this.mPidsSelfLocked) {
                    int callingPid = android.os.Binder.getCallingPid();
                    com.android.server.am.ProcessRecord proc = this.mPidsSelfLocked.get(callingPid);
                    if (proc == null) {
                        throw new java.lang.SecurityException("Unknown process: " + callingPid);
                    }
                    if (proc.getActiveInstrumentation() == null || proc.getActiveInstrumentation().mUiAutomationConnection == null) {
                        throw new java.lang.SecurityException("Only an instrumentation process with a UiAutomation can call setUserIsMonkey");
                    }
                }
                this.mUserIsMonkey = userIsMonkey;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    public boolean isUserAMonkey() {
        boolean z;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                z = this.mUserIsMonkey || this.mActivityTaskManager.isControllerAMonkey();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return z;
    }

    public void requestSystemServerHeapDump() {
        com.android.server.am.ProcessRecord pr;
        if (!android.os.Build.IS_DEBUGGABLE) {
            android.util.Slog.wtf("ActivityManager", "requestSystemServerHeapDump called on a user build");
            return;
        }
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only the system process is allowed to request a system heap dump");
        }
        synchronized (this.mPidsSelfLocked) {
            pr = this.mPidsSelfLocked.get(android.os.Process.myPid());
        }
        if (pr == null) {
            android.util.Slog.w("ActivityManager", "system process not in mPidsSelfLocked: " + android.os.Process.myPid());
            return;
        }
        synchronized (this.mAppProfiler.mProfilerLock) {
            this.mAppProfiler.startHeapDumpLPf(pr.mProfile, true);
        }
    }

    public void requestBugReport(int bugreportType) {
        requestBugReportWithDescription(null, null, bugreportType, 0L);
    }

    public void requestBugReportWithDescription(java.lang.String shareTitle, java.lang.String shareDescription, int bugreportType) {
        requestBugReportWithDescription(shareTitle, shareDescription, bugreportType, 0L);
    }

    public void requestBugReportWithDescription(java.lang.String shareTitle, java.lang.String shareDescription, int bugreportType, long nonce) {
        requestBugReportWithDescription(shareTitle, shareDescription, bugreportType, nonce, null);
    }

    public void requestBugReportWithDescription(java.lang.String shareTitle, java.lang.String shareDescription, int bugreportType, long nonce, android.net.Uri extraAttachment) {
        java.lang.String type;
        switch (bugreportType) {
            case 0:
                type = "bugreportfull";
                break;
            case 1:
                type = "bugreportplus";
                break;
            case 2:
                type = "bugreportremote";
                break;
            case 3:
                type = "bugreportwear";
                break;
            case 4:
                type = "bugreporttelephony";
                break;
            case 5:
                type = "bugreportwifi";
                break;
            case 6:
            default:
                throw new java.lang.IllegalArgumentException("Provided bugreport type is not correct, value: " + bugreportType);
            case 7:
                type = "bugreportonboarding";
                break;
        }
        android.util.Slog.i("ActivityManager", type + " requested by UID " + android.os.Binder.getCallingUid());
        enforceCallingPermission("android.permission.DUMP", "requestBugReport");
        if (!android.text.TextUtils.isEmpty(shareTitle)) {
            if (shareTitle.length() > 100) {
                throw new java.lang.IllegalArgumentException("shareTitle should be less than 100 characters");
            }
            if (!android.text.TextUtils.isEmpty(shareDescription) && shareDescription.length() > 150) {
                throw new java.lang.IllegalArgumentException("shareDescription should be less than 150 characters");
            }
            android.util.Slog.d("ActivityManager", "Bugreport notification title " + shareTitle + " description " + shareDescription);
        }
        android.content.Intent triggerShellBugreport = new android.content.Intent();
        triggerShellBugreport.setAction(INTENT_BUGREPORT_REQUESTED);
        triggerShellBugreport.setPackage("com.android.shell");
        triggerShellBugreport.putExtra(EXTRA_BUGREPORT_TYPE, bugreportType);
        triggerShellBugreport.putExtra(EXTRA_BUGREPORT_NONCE, nonce);
        if (extraAttachment != null) {
            triggerShellBugreport.putExtra(EXTRA_EXTRA_ATTACHMENT_URI, extraAttachment);
            triggerShellBugreport.addFlags(1);
        }
        triggerShellBugreport.addFlags(268435456);
        triggerShellBugreport.addFlags(16777216);
        if (shareTitle != null) {
            triggerShellBugreport.putExtra(EXTRA_TITLE, shareTitle);
        }
        if (shareDescription != null) {
            triggerShellBugreport.putExtra(EXTRA_DESCRIPTION, shareDescription);
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (bugreportType == 2) {
                this.mContext.sendBroadcastAsUser(triggerShellBugreport, android.os.UserHandle.SYSTEM);
            } else {
                this.mContext.sendBroadcastAsUser(triggerShellBugreport, getCurrentUser().getUserHandle());
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void requestTelephonyBugReport(java.lang.String shareTitle, java.lang.String shareDescription) {
        requestBugReportWithDescription(shareTitle, shareDescription, 4);
    }

    public void requestWifiBugReport(java.lang.String shareTitle, java.lang.String shareDescription) {
        requestBugReportWithDescription(shareTitle, shareDescription, 5);
    }

    public void requestInteractiveBugReport() {
        requestBugReportWithDescription(null, null, 1);
    }

    public void requestBugReportWithExtraAttachment(android.net.Uri extraAttachment) {
        requestBugReportWithDescription(null, null, 1, 0L, extraAttachment);
    }

    public void requestInteractiveBugReportWithDescription(java.lang.String shareTitle, java.lang.String shareDescription) {
        requestBugReportWithDescription(shareTitle, shareDescription, 1);
    }

    public void requestFullBugReport() {
        requestBugReportWithDescription(null, null, 0);
    }

    public void requestRemoteBugReport(long nonce) {
        requestBugReportWithDescription(null, null, 2, nonce);
    }

    public boolean launchBugReportHandlerApp() {
        android.content.Context currentUserContext = this.mContext.createContextAsUser(getCurrentUser().getUserHandle(), 0);
        if (!com.android.server.am.BugReportHandlerUtil.isBugReportHandlerEnabled(currentUserContext)) {
            return false;
        }
        android.util.Slog.i("ActivityManager", "launchBugReportHandlerApp requested by UID " + android.os.Binder.getCallingUid());
        enforceCallingPermission("android.permission.DUMP", "launchBugReportHandlerApp");
        return com.android.server.am.BugReportHandlerUtil.launchBugReportHandlerApp(currentUserContext);
    }

    public java.util.List<java.lang.String> getBugreportWhitelistedPackages() {
        enforceCallingPermission("android.permission.MANAGE_DEBUGGING", "getBugreportWhitelistedPackages");
        return new java.util.ArrayList(com.android.server.SystemConfig.getInstance().getBugreportWhitelistedPackages());
    }

    public void registerProcessObserver(android.app.IProcessObserver observer) {
        enforceCallingPermission("android.permission.SET_ACTIVITY_WATCHER", "registerProcessObserver()");
        this.mProcessList.registerProcessObserver(observer);
    }

    public void unregisterProcessObserver(android.app.IProcessObserver observer) {
        this.mProcessList.unregisterProcessObserver(observer);
    }

    public int getUidProcessState(int uid, java.lang.String callingPackage) {
        int uidProcessStateInnerLOSP;
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "getUidProcessState");
        }
        this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), android.os.UserHandle.getUserId(uid), false, 2, "getUidProcessState", callingPackage);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                uidProcessStateInnerLOSP = getUidProcessStateInnerLOSP(uid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return uidProcessStateInnerLOSP;
    }

    /* JADX WARN: Finally extract failed */
    public int getBindingUidProcessState(int targetUid, java.lang.String callingPackage) {
        boolean allowed;
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.GET_BINDING_UID_IMPORTANCE", "getBindingUidProcessState");
        }
        int callingUid = android.os.Binder.getCallingUid();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            boostPriorityForLockedSection();
            synchronized (this) {
                if (callingUid != targetUid) {
                    try {
                        allowed = hasServiceBindingOrProviderUseLocked(callingUid, targetUid);
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                if (allowed) {
                    int uidProcessStateInnerLOSP = getUidProcessStateInnerLOSP(targetUid);
                    resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(token);
                    return uidProcessStateInnerLOSP;
                }
                resetPriorityAfterLockedSection();
                android.os.Binder.restoreCallingIdentity(token);
                return 20;
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th2;
        }
    }

    private int getUidProcessStateInnerLOSP(int uid) {
        if (this.mPendingStartActivityUids.isPendingTopUid(uid)) {
            return 2;
        }
        return this.mProcessList.getUidProcStateLOSP(uid);
    }

    private boolean hasServiceBindingOrProviderUseLocked(final int callingUid, final int clientUid) {
        java.lang.Boolean hasBinding = (java.lang.Boolean) this.mProcessList.searchEachLruProcessesLOSP(false, new java.util.function.Function() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda25
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.am.ActivityManagerService.lambda$hasServiceBindingOrProviderUseLocked$4(callingUid, clientUid, (com.android.server.am.ProcessRecord) obj);
            }
        });
        if (java.lang.Boolean.TRUE.equals(hasBinding)) {
            return true;
        }
        java.lang.Boolean hasProviderClient = (java.lang.Boolean) this.mProcessList.searchEachLruProcessesLOSP(false, new java.util.function.Function() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda26
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.am.ActivityManagerService.lambda$hasServiceBindingOrProviderUseLocked$5(callingUid, clientUid, (com.android.server.am.ProcessRecord) obj);
            }
        });
        return java.lang.Boolean.TRUE.equals(hasProviderClient);
    }

    static /* synthetic */ java.lang.Boolean lambda$hasServiceBindingOrProviderUseLocked$4(int callingUid, int clientUid, com.android.server.am.ProcessRecord pr) {
        if (pr.uid == callingUid) {
            com.android.server.am.ProcessServiceRecord psr = pr.mServices;
            int serviceCount = psr.mServices.size();
            for (int svc = 0; svc < serviceCount; svc++) {
                android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> conns = psr.mServices.valueAt(svc).getConnections();
                int size = conns.size();
                for (int conni = 0; conni < size; conni++) {
                    java.util.ArrayList<com.android.server.am.ConnectionRecord> crs = conns.valueAt(conni);
                    for (int con = 0; con < crs.size(); con++) {
                        com.android.server.am.ConnectionRecord cr = crs.get(con);
                        com.android.server.am.ProcessRecord clientPr = cr.binding.client;
                        if (clientPr.uid == clientUid) {
                            return java.lang.Boolean.TRUE;
                        }
                    }
                }
            }
            return null;
        }
        return null;
    }

    static /* synthetic */ java.lang.Boolean lambda$hasServiceBindingOrProviderUseLocked$5(int callingUid, int clientUid, com.android.server.am.ProcessRecord pr) {
        if (pr.uid == callingUid) {
            com.android.server.am.ProcessProviderRecord ppr = pr.mProviders;
            for (int provi = ppr.numberOfProviders() - 1; provi >= 0; provi--) {
                com.android.server.am.ContentProviderRecord cpr = ppr.getProviderAt(provi);
                for (int i = cpr.connections.size() - 1; i >= 0; i--) {
                    com.android.server.am.ContentProviderConnection conn = cpr.connections.get(i);
                    com.android.server.am.ProcessRecord client = conn.client;
                    if (client.uid == clientUid) {
                        return java.lang.Boolean.TRUE;
                    }
                }
            }
            return null;
        }
        return null;
    }

    public int getUidProcessCapabilities(int uid, java.lang.String callingPackage) {
        int uidProcessCapabilityLOSP;
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "getUidProcessState");
        }
        this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), android.os.UserHandle.getUserId(uid), false, 2, "getUidProcessCapabilities", callingPackage);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                uidProcessCapabilityLOSP = this.mProcessList.getUidProcessCapabilityLOSP(uid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return uidProcessCapabilityLOSP;
    }

    public void registerUidObserver(android.app.IUidObserver observer, int which, int cutpoint, java.lang.String callingPackage) {
        registerUidObserverForUids(observer, which, cutpoint, callingPackage, null);
    }

    public android.os.IBinder registerUidObserverForUids(android.app.IUidObserver observer, int which, int cutpoint, java.lang.String callingPackage, int[] uids) {
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "registerUidObserver");
        }
        return this.mUidObserverController.register(observer, which, cutpoint, callingPackage, android.os.Binder.getCallingUid(), uids);
    }

    public void unregisterUidObserver(android.app.IUidObserver observer) {
        this.mUidObserverController.unregister(observer);
    }

    public void addUidToObserver(android.os.IBinder observerToken, java.lang.String callingPackage, int uid) {
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "registerUidObserver");
        }
        this.mActivityManagerServiceExt.addAppMonitoredUid(callingPackage, uid);
        this.mUidObserverController.addUidToObserver(observerToken, uid);
    }

    public void removeUidFromObserver(android.os.IBinder observerToken, java.lang.String callingPackage, int uid) {
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "registerUidObserver");
        }
        this.mActivityManagerServiceExt.removeAppMonitoredUid(callingPackage, uid);
        this.mUidObserverController.removeUidFromObserver(observerToken, uid);
    }

    public boolean isUidActive(int uid, java.lang.String callingPackage) {
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "isUidActive");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (isUidActiveLOSP(uid)) {
                    resetPriorityAfterProcLockedSection();
                    return true;
                }
                resetPriorityAfterProcLockedSection();
                return this.mInternal.isPendingTopUid(uid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    boolean isUidActiveLOSP(int uid) {
        com.android.server.am.UidRecord uidRecord = this.mProcessList.getUidRecordLOSP(uid);
        return (uidRecord == null || uidRecord.isSetIdle()) ? false : true;
    }

    public long getUidLastIdleElapsedTime(int uid, java.lang.String callingPackage) {
        long realLastIdleTime;
        if (!hasUsageStatsPermission(callingPackage)) {
            enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "getUidLastIdleElapsedTime");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.am.UidRecord uidRecord = this.mProcessList.getUidRecordLOSP(uid);
                realLastIdleTime = uidRecord != null ? uidRecord.getRealLastIdleTime() : 0L;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return realLastIdleTime;
    }

    public void registerUidFrozenStateChangedCallback(android.app.IUidFrozenStateChangedCallback callback) {
        com.android.internal.util.Preconditions.checkNotNull(callback, "callback cannot be null");
        enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "registerUidFrozenStateChangedCallback()");
        synchronized (this.mUidFrozenStateChangedCallbackList) {
            boolean registered = this.mUidFrozenStateChangedCallbackList.register(callback);
            if (!registered) {
                android.util.Slog.w("ActivityManager", "Failed to register with RemoteCallbackList!");
            }
        }
    }

    public void unregisterUidFrozenStateChangedCallback(android.app.IUidFrozenStateChangedCallback callback) {
        com.android.internal.util.Preconditions.checkNotNull(callback, "callback cannot be null");
        enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "unregisterUidFrozenStateChangedCallback()");
        synchronized (this.mUidFrozenStateChangedCallbackList) {
            this.mUidFrozenStateChangedCallbackList.unregister(callback);
        }
    }

    public int[] getUidFrozenState(int[] uids) {
        com.android.internal.util.Preconditions.checkNotNull(uids, "uid array cannot be null");
        enforceCallingPermission("android.permission.PACKAGE_USAGE_STATS", "getUidFrozenState()");
        int[] frozenStates = new int[uids.length];
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            for (int i = 0; i < uids.length; i++) {
                try {
                    com.android.server.am.UidRecord uidRec = this.mProcessList.mActiveUids.get(uids[i]);
                    if (uidRec != null && uidRec.areAllProcessesFrozen()) {
                        frozenStates[i] = 1;
                    } else {
                        frozenStates[i] = 2;
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
        }
        resetPriorityAfterProcLockedSection();
        return frozenStates;
    }

    public void reportUidFrozenStateChanged(int[] uids, int[] frozenStates) {
        synchronized (this.mUidFrozenStateChangedCallbackList) {
            int n = this.mUidFrozenStateChangedCallbackList.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    this.mUidFrozenStateChangedCallbackList.getBroadcastItem(i).onUidFrozenStateChanged(uids, frozenStates);
                } catch (android.os.RemoteException e) {
                }
            }
            this.mUidFrozenStateChangedCallbackList.finishBroadcast();
        }
    }

    public void setPersistentVrThread(int tid) {
        this.mActivityTaskManager.setPersistentVrThread(tid);
    }

    public static boolean scheduleAsRegularPriority(int tid, boolean suppressLogs) {
        try {
            android.os.Process.setThreadScheduler(tid, 0, 0);
            return true;
        } catch (java.lang.IllegalArgumentException e) {
            if (!suppressLogs) {
                android.util.Slog.w("ActivityManager", "Failed to set scheduling policy, thread does not exist:\n" + e);
            }
            return false;
        } catch (java.lang.SecurityException e2) {
            if (!suppressLogs) {
                android.util.Slog.w("ActivityManager", "Failed to set scheduling policy, not allowed:\n" + e2);
            }
            return false;
        }
    }

    public static boolean scheduleAsFifoPriority(int tid, boolean suppressLogs) {
        try {
            android.os.Process.setThreadScheduler(tid, com.android.server.policy.WindowManagerPolicy.COLOR_FADE_LAYER, 1);
            return true;
        } catch (java.lang.IllegalArgumentException e) {
            if (!suppressLogs) {
                android.util.Slog.w("ActivityManager", "Failed to set scheduling policy, thread does not exist:\n" + e);
                return false;
            }
            return false;
        } catch (java.lang.SecurityException e2) {
            if (!suppressLogs) {
                android.util.Slog.w("ActivityManager", "Failed to set scheduling policy, not allowed:\n" + e2);
                return false;
            }
            return false;
        }
    }

    static void setFifoPriority(com.android.server.am.ProcessRecord app, boolean enable) {
        int pid = app.getPid();
        int renderThreadTid = app.getRenderThreadTid();
        if (enable) {
            scheduleAsFifoPriority(pid, true);
            if (renderThreadTid != 0) {
                scheduleAsFifoPriority(renderThreadTid, true);
                return;
            }
            return;
        }
        scheduleAsRegularPriority(pid, true);
        if (renderThreadTid != 0) {
            scheduleAsRegularPriority(renderThreadTid, true);
        }
    }

    public void setRenderThread(int tid) {
        com.android.server.am.ProcessRecord proc;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                int pid = android.os.Binder.getCallingPid();
                if (pid == android.os.Process.myPid()) {
                    demoteSystemServerRenderThread(tid);
                    resetPriorityAfterProcLockedSection();
                    return;
                }
                synchronized (this.mPidsSelfLocked) {
                    proc = this.mPidsSelfLocked.get(pid);
                }
                if (proc != null && proc.getRenderThreadTid() == 0 && tid > 0) {
                    if (!android.os.Process.isThreadInProcess(pid, tid)) {
                        throw new java.lang.IllegalArgumentException("Render thread does not belong to process");
                    }
                    proc.setRenderThreadTid(tid);
                    sUserAwareManagerExt.notifyAppRenderThreadCreated(proc.info.packageName, pid, tid, proc.uid);
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                        android.util.Slog.d("UI_FIFO", "Set RenderThread tid " + tid + " for pid " + pid);
                    }
                    if (proc.mState.getCurrentSchedulingGroup() == 3) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                            android.util.Slog.d("UI_FIFO", "Promoting " + tid + "out of band");
                        }
                        if (proc.useFifoUiScheduling()) {
                            android.os.Process.setThreadScheduler(proc.getRenderThreadTid(), com.android.server.policy.WindowManagerPolicy.COLOR_FADE_LAYER, 1);
                        } else {
                            android.os.Process.setThreadPriority(proc.getRenderThreadTid(), -10);
                        }
                        mUIFirstManagerExt.setRenderThreadTid(proc.info.packageName, proc.uid, pid, tid);
                    }
                } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                    android.util.Slog.d("UI_FIFO", "Didn't set thread from setRenderThread? PID: " + pid + ", TID: " + tid + " FIFO: " + this.mUseFifoUiScheduling);
                }
                resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    private void demoteSystemServerRenderThread(int tid) {
        android.os.Process.setThreadPriority(tid, -4);
    }

    public boolean isVrModePackageEnabled(android.content.ComponentName packageName) {
        this.mActivityTaskManager.enforceSystemHasVrFeature();
        com.android.server.vr.VrManagerInternal vrService = (com.android.server.vr.VrManagerInternal) com.android.server.LocalServices.getService(com.android.server.vr.VrManagerInternal.class);
        return vrService.hasVrPackage(packageName, android.os.UserHandle.getCallingUserId()) == 0;
    }

    public boolean isTopActivityImmersive() {
        return this.mActivityTaskManager.isTopActivityImmersive();
    }

    public boolean isTopOfTask(android.os.IBinder token) {
        return android.app.ActivityClient.getInstance().isTopOfTask(token);
    }

    public void setHasTopUi(boolean hasTopUi) throws android.os.RemoteException {
        if (checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW") != 0) {
            java.lang.String msg = "Permission Denial: setHasTopUi() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.INTERNAL_SYSTEM_WINDOW";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        int pid = android.os.Binder.getCallingPid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            boostPriorityForLockedSection();
            synchronized (this) {
                boolean changed = false;
                try {
                    synchronized (this.mPidsSelfLocked) {
                        com.android.server.am.ProcessRecord pr = this.mPidsSelfLocked.get(pid);
                        if (pr == null) {
                            android.util.Slog.w("ActivityManager", "setHasTopUi called on unknown pid: " + pid);
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        if (pr.mState.hasTopUi() != hasTopUi) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                                android.util.Slog.d("ActivityManager", "Setting hasTopUi=" + hasTopUi + " for pid=" + pid);
                            }
                            pr.mState.setHasTopUi(hasTopUi);
                            changed = true;
                        }
                        if (changed) {
                            updateOomAdjLocked(pr, 9);
                        }
                        resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public final void enterSafeMode() {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (!this.mSystemReady) {
                    try {
                        android.app.AppGlobals.getPackageManager().enterSafeMode();
                    } catch (android.os.RemoteException e) {
                    }
                }
                this.mSafeMode = true;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public final void showSafeModeOverlay() {
        android.view.View v = android.view.LayoutInflater.from(this.mContext).inflate(android.R.layout.preference_child_material, (android.view.ViewGroup) null);
        android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams();
        lp.type = 2015;
        lp.width = -2;
        lp.height = -2;
        lp.gravity = 8388691;
        lp.format = v.getBackground().getOpacity();
        lp.flags = 24;
        lp.privateFlags |= 16;
        ((android.view.WindowManager) this.mContext.getSystemService("window")).addView(v, lp);
    }

    public void noteWakeupAlarm(android.content.IIntentSender sender, android.os.WorkSource workSource, int sourceUid, java.lang.String sourcePkg, java.lang.String tag) {
        android.os.WorkSource workSource2;
        int sourceUid2;
        int standbyBucket;
        java.lang.String str;
        java.lang.String str2;
        if (workSource != null && workSource.isEmpty()) {
            workSource2 = null;
        } else {
            workSource2 = workSource;
        }
        if (sourceUid <= 0 && workSource2 == null) {
            if (sender == null || !(sender instanceof com.android.server.am.PendingIntentRecord)) {
                return;
            }
            com.android.server.am.PendingIntentRecord rec = (com.android.server.am.PendingIntentRecord) sender;
            int callerUid = android.os.Binder.getCallingUid();
            sourceUid2 = rec.uid == callerUid ? 1000 : rec.uid;
        } else {
            sourceUid2 = sourceUid;
        }
        int standbyBucket2 = 0;
        this.mBatteryStatsService.noteWakupAlarm(sourcePkg, sourceUid2, workSource2, tag);
        if (workSource2 == null) {
            if (this.mUsageStatsService == null) {
                standbyBucket = 0;
            } else {
                int standbyBucket3 = this.mUsageStatsService.getAppStandbyBucket(sourcePkg, android.os.UserHandle.getUserId(sourceUid2), android.os.SystemClock.elapsedRealtime());
                standbyBucket = standbyBucket3;
            }
            com.android.internal.util.FrameworkStatsLog.write_non_chained(35, sourceUid2, (java.lang.String) null, tag, sourcePkg, standbyBucket);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER) {
                android.util.Slog.w("ActivityManager", "noteWakeupAlarm[ sourcePkg=" + sourcePkg + ", sourceUid=" + sourceUid2 + ", workSource=" + workSource2 + ", tag=" + tag + ", standbyBucket=" + standbyBucket + "]");
            }
            return;
        }
        java.lang.String workSourcePackage = workSource2.getPackageName(0);
        int workSourceUid = workSource2.getAttributionUid();
        if (workSourcePackage == null) {
            workSourcePackage = sourcePkg;
            workSourceUid = sourceUid2;
        }
        if (this.mUsageStatsService != null) {
            str = ", standbyBucket=";
            str2 = ", tag=";
            standbyBucket2 = this.mUsageStatsService.getAppStandbyBucket(workSourcePackage, android.os.UserHandle.getUserId(workSourceUid), android.os.SystemClock.elapsedRealtime());
        } else {
            str = ", standbyBucket=";
            str2 = ", tag=";
        }
        com.android.internal.util.FrameworkStatsLog.write(35, workSource2, tag, sourcePkg, standbyBucket2);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER) {
            android.util.Slog.w("ActivityManager", "noteWakeupAlarm[ sourcePkg=" + sourcePkg + ", sourceUid=" + sourceUid2 + ", workSource=" + workSource2 + str2 + tag + str + standbyBucket2 + " wsName=" + workSourcePackage + ")]");
        }
    }

    public void noteAlarmStart(android.content.IIntentSender sender, android.os.WorkSource workSource, int sourceUid, java.lang.String tag) {
        if (workSource != null && workSource.isEmpty()) {
            workSource = null;
        }
        if (sourceUid <= 0 && workSource == null) {
            if (sender == null || !(sender instanceof com.android.server.am.PendingIntentRecord)) {
                return;
            }
            com.android.server.am.PendingIntentRecord rec = (com.android.server.am.PendingIntentRecord) sender;
            int callerUid = android.os.Binder.getCallingUid();
            sourceUid = rec.uid == callerUid ? 1000 : rec.uid;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER) {
            android.util.Slog.w("ActivityManager", "noteAlarmStart[sourceUid=" + sourceUid + ", workSource=" + workSource + ", tag=" + tag + "]");
        }
        this.mBatteryStatsService.noteAlarmStart(tag, workSource, sourceUid);
    }

    public void noteAlarmFinish(android.content.IIntentSender sender, android.os.WorkSource workSource, int sourceUid, java.lang.String tag) {
        if (workSource != null && workSource.isEmpty()) {
            workSource = null;
        }
        if (sourceUid <= 0 && workSource == null) {
            if (sender == null || !(sender instanceof com.android.server.am.PendingIntentRecord)) {
                return;
            }
            com.android.server.am.PendingIntentRecord rec = (com.android.server.am.PendingIntentRecord) sender;
            int callerUid = android.os.Binder.getCallingUid();
            sourceUid = rec.uid == callerUid ? 1000 : rec.uid;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER) {
            android.util.Slog.w("ActivityManager", "noteAlarmFinish[sourceUid=" + sourceUid + ", workSource=" + workSource + ", tag=" + tag + "]");
        }
        this.mBatteryStatsService.noteAlarmFinish(tag, workSource, sourceUid);
    }

    public boolean killPids(int[] pids, java.lang.String pReason, boolean secure) {
        int type;
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("killPids only available to the system");
        }
        final java.lang.String reason = pReason == null ? "Unknown" : pReason;
        boolean killed = false;
        final java.util.ArrayList<com.android.server.am.ProcessRecord> killCandidates = new java.util.ArrayList<>();
        synchronized (this.mPidsSelfLocked) {
            int worstType = 0;
            for (int i : pids) {
                com.android.server.am.ProcessRecord proc = this.mPidsSelfLocked.get(i);
                if (proc != null && (type = proc.mState.getSetAdj()) > worstType) {
                    worstType = type;
                }
            }
            if (worstType < 999 && worstType > 900) {
                worstType = 900;
            }
            if (!secure && worstType < 500) {
                worstType = 500;
            }
            android.util.Slog.w("ActivityManager", "Killing processes " + reason + " at adjustment " + worstType);
            for (int i2 : pids) {
                com.android.server.am.ProcessRecord proc2 = this.mPidsSelfLocked.get(i2);
                if (proc2 != null) {
                    int adj = proc2.mState.getSetAdj();
                    if (adj >= worstType && !proc2.isKilledByAm()) {
                        killCandidates.add(proc2);
                        killed = true;
                    }
                }
            }
        }
        if (!killCandidates.isEmpty()) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$killPids$6(killCandidates, reason);
                }
            });
        }
        return killed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$killPids$6(java.util.ArrayList killCandidates, java.lang.String reason) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                int size = killCandidates.size();
                for (int i = 0; i < size; i++) {
                    ((com.android.server.am.ProcessRecord) killCandidates.get(i)).killLocked(reason, 13, 12, true);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void killUid(int appId, int userId, int reason, int subReason, java.lang.String reasonAsString) {
        enforceCallingPermission("android.permission.KILL_UID", "killUid");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            this.mProcessList.killPackageProcessesLSP(null, appId, userId, com.android.server.am.ProcessList.PERSISTENT_PROC_ADJ, false, true, true, true, false, false, reason, subReason, reasonAsString != null ? reasonAsString : "kill uid");
                        } catch (java.lang.Throwable th) {
                            resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    resetPriorityAfterProcLockedSection();
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void killUid(int appId, int userId, java.lang.String reason) {
        killUid(appId, userId, 13, 11, reason);
    }

    public void killUidForPermissionChange(int appId, int userId, java.lang.String reason) {
        enforceCallingPermission("android.permission.KILL_UID", "killUid");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            this.mProcessList.killPackageProcessesLSP(null, appId, userId, com.android.server.am.ProcessList.PERSISTENT_PROC_ADJ, false, true, true, true, false, false, 8, 0, reason != null ? reason : "kill uid");
                        } catch (java.lang.Throwable th) {
                            resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    resetPriorityAfterProcLockedSection();
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public boolean killProcessesBelowForeground(java.lang.String reason) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("killProcessesBelowForeground() only available to system");
        }
        return killProcessesBelowAdj(0, reason);
    }

    private boolean killProcessesBelowAdj(int belowAdj, java.lang.String reason) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("killProcessesBelowAdj() only available to system");
        }
        boolean killed = false;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        synchronized (this.mPidsSelfLocked) {
                            int size = this.mPidsSelfLocked.size();
                            for (int i = 0; i < size; i++) {
                                this.mPidsSelfLocked.keyAt(i);
                                com.android.server.am.ProcessRecord proc = this.mPidsSelfLocked.valueAt(i);
                                if (proc != null) {
                                    int adj = proc.mState.getSetAdj();
                                    if (adj > belowAdj && !proc.isKilledByAm()) {
                                        proc.killLocked(reason, 8, true);
                                        killed = true;
                                    }
                                }
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
        return killed;
    }

    public void killProcessesWhenImperceptible(int[] pids, java.lang.String reason) {
        if (checkCallingPermission("android.permission.FORCE_STOP_PACKAGES") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.FORCE_STOP_PACKAGES");
        }
        int callerUid = android.os.Binder.getCallingUid();
        long iden = android.os.Binder.clearCallingIdentity();
        try {
            this.mProcessList.killProcessesWhenImperceptible(pids, reason, callerUid);
        } finally {
            android.os.Binder.restoreCallingIdentity(iden);
        }
    }

    public void hang(android.os.IBinder who, boolean allowRestart) {
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        android.os.IBinder.DeathRecipient death = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.am.ActivityManagerService.11
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                synchronized (this) {
                    notifyAll();
                }
            }
        };
        try {
            who.linkToDeath(death, 0);
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    com.android.server.Watchdog.getInstance().setAllowRestart(allowRestart);
                    android.util.Slog.i("ActivityManager", "Hanging system process at request of pid " + android.os.Binder.getCallingPid());
                    synchronized (death) {
                        while (who.isBinderAlive()) {
                            try {
                                death.wait();
                            } catch (java.lang.InterruptedException e) {
                            }
                        }
                    }
                    com.android.server.Watchdog.getInstance().setAllowRestart(true);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w("ActivityManager", "hang: given caller IBinder is already dead.");
        }
    }

    public void restart() {
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        android.util.Log.i("ActivityManager", "Sending shutdown broadcast...");
        android.content.BroadcastReceiver br = new android.content.BroadcastReceiver() { // from class: com.android.server.am.ActivityManagerService.12
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) throws java.io.IOException {
                android.util.Log.i("ActivityManager", "Shutting down activity manager...");
                com.android.server.am.ActivityManagerService.this.shutdown(10000);
                android.util.Log.i("ActivityManager", "Shutdown complete, restarting!");
                android.os.Process.killProcess(android.os.Process.myPid());
                java.lang.System.exit(10);
            }
        };
        android.content.Intent intent = new android.content.Intent("android.intent.action.ACTION_SHUTDOWN");
        intent.addFlags(268435456);
        intent.putExtra("android.intent.extra.SHUTDOWN_USERSPACE_ONLY", true);
        br.onReceive(this.mContext, intent);
    }

    public void performIdleMaintenance() {
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                final long now = android.os.SystemClock.uptimeMillis();
                final long timeSinceLastIdle = now - this.mLastIdleTime;
                if (!com.android.server.flags.Flags.disableSystemCompaction()) {
                    this.mOomAdjuster.mCachedAppOptimizer.compactAllSystem();
                }
                final long lowRamSinceLastIdle = this.mAppProfiler.getLowRamTimeSinceIdleLPr(now);
                this.mLastIdleTime = now;
                this.mAppProfiler.updateLowRamTimestampLPr(now);
                java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
                sb.append("Idle maintenance over ");
                android.util.TimeUtils.formatDuration(timeSinceLastIdle, sb);
                sb.append(" low RAM for ");
                android.util.TimeUtils.formatDuration(lowRamSinceLastIdle, sb);
                android.util.Slog.i("ActivityManager", sb.toString());
                final boolean doKilling = lowRamSinceLastIdle > timeSinceLastIdle / 3;
                long totalMemoryInKb = android.os.Process.getTotalMemory() / 1000;
                final long memoryGrowthThreshold = java.lang.Math.max(totalMemoryInKb / 100, 10000L);
                this.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda35
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) throws java.lang.Throwable {
                        this.f$0.lambda$performIdleMaintenance$8(doKilling, memoryGrowthThreshold, timeSinceLastIdle, lowRamSinceLastIdle, now, (com.android.server.am.ProcessRecord) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performIdleMaintenance$8(boolean doKilling, long memoryGrowthThreshold, long timeSinceLastIdle, long lowRamSinceLastIdle, long now, final com.android.server.am.ProcessRecord proc) throws java.lang.Throwable {
        long lastPss;
        com.android.server.am.ProcessProfileRecord pr;
        if (proc.getThread() == null) {
            return;
        }
        com.android.server.am.ProcessProfileRecord pr2 = proc.mProfile;
        com.android.server.am.ProcessStateRecord state = proc.mState;
        int setProcState = state.getSetProcState();
        if (state.isNotCachedSinceIdle()) {
            if (setProcState >= 5 && setProcState <= 10) {
                synchronized (this.mAppProfiler.mProfilerLock) {
                    try {
                        final long initialIdlePssOrRss = pr2.getInitialIdlePssOrRss();
                        if (this.mAppProfiler.isProfilingPss()) {
                            try {
                                lastPss = pr2.getLastPss();
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
                        } else {
                            lastPss = pr2.getLastRss();
                        }
                        final long lastPssOrRss = lastPss;
                        long lastSwapPss = pr2.getLastSwapPss();
                        if (!doKilling || initialIdlePssOrRss == 0 || lastPssOrRss <= (3 * initialIdlePssOrRss) / 2 || lastPssOrRss <= initialIdlePssOrRss + memoryGrowthThreshold) {
                            pr = pr2;
                        } else {
                            java.lang.StringBuilder sb2 = new java.lang.StringBuilder(128);
                            sb2.append("Kill");
                            sb2.append(proc.processName);
                            if (this.mAppProfiler.isProfilingPss()) {
                                sb2.append(" in idle maint: pss=");
                            } else {
                                sb2.append(" in idle maint: rss=");
                            }
                            sb2.append(lastPssOrRss);
                            if (this.mAppProfiler.isProfilingPss()) {
                                sb2.append(", swapPss=");
                                sb2.append(lastSwapPss);
                                sb2.append(", initialPss=");
                            } else {
                                sb2.append(", initialRss=");
                            }
                            sb2.append(initialIdlePssOrRss);
                            sb2.append(", period=");
                            android.util.TimeUtils.formatDuration(timeSinceLastIdle, sb2);
                            sb2.append(", lowRamPeriod=");
                            pr = pr2;
                            android.util.TimeUtils.formatDuration(lowRamSinceLastIdle, sb2);
                            android.util.Slog.wtfQuiet("ActivityManager", sb2.toString());
                            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda14
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$performIdleMaintenance$7(proc, lastPssOrRss, initialIdlePssOrRss);
                                }
                            });
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                }
            }
        } else if (setProcState < 14 && setProcState >= 0) {
            state.setNotCachedSinceIdle(true);
            synchronized (this.mAppProfiler.mProfilerLock) {
                try {
                    try {
                        pr2.setInitialIdlePssOrRss(0L);
                        this.mAppProfiler.updateNextPssTimeLPf(state.getSetProcState(), proc.mProfile, now, true);
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                        throw th;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performIdleMaintenance$7(com.android.server.am.ProcessRecord proc, long lastPssOrRss, long initialIdlePssOrRss) {
        java.lang.String str;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (this.mAppProfiler.isProfilingPss()) {
                    str = "idle maint (pss ";
                } else {
                    str = "idle maint (rss " + lastPssOrRss + " from " + initialIdlePssOrRss + ")";
                }
                proc.killLocked(str, 13, 6, true);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void sendIdleJobTrigger() {
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.content.Intent intent = new android.content.Intent(ACTION_TRIGGER_IDLE).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).addFlags(1073741824);
            broadcastIntent(null, intent, null, null, 0, null, null, null, -1, null, false, false, -1);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void retrieveSettings() {
        android.content.res.Resources res;
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        this.mActivityTaskManager.retrieveSettings(resolver);
        java.lang.String debugApp = android.provider.Settings.Global.getString(resolver, "debug_app");
        boolean waitForDebugger = android.provider.Settings.Global.getInt(resolver, "wait_for_debugger", 0) != 0;
        boolean alwaysFinishActivities = android.provider.Settings.Global.getInt(resolver, "always_finish_activities", 0) != 0;
        this.mHiddenApiBlacklist.registerObserver();
        this.mPlatformCompat.registerContentObserver();
        this.mAppProfiler.retrieveSettings();
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mOrigDebugApp = debugApp;
                this.mDebugApp = debugApp;
                this.mOrigWaitForDebugger = waitForDebugger;
                this.mWaitForDebugger = waitForDebugger;
                this.mAlwaysFinishActivities = alwaysFinishActivities;
                res = this.mContext.getResources();
                boolean userSwitchUiEnabled = res.getBoolean(android.R.bool.config_debugEnableAutomaticSystemServerHeapDumps) ? false : true;
                int maxRunningUsers = res.getInteger(android.R.integer.config_maxShortcutTargetsPerApp);
                boolean delayUserDataLocking = res.getBoolean(android.R.bool.config_letterboxIsHorizontalReachabilityEnabled);
                int backgroundUserScheduledStopTimeSecs = res.getInteger(android.R.integer.config_autoBrightnessInitialLightSensorRate);
                this.mUserController.setInitialConfig(userSwitchUiEnabled, maxRunningUsers, delayUserDataLocking, backgroundUserScheduledStopTimeSecs);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        this.mAppErrors.loadAppsNotReportingCrashesFromConfig(res.getString(android.R.string.config_batterymeterPowersavePath));
    }

    /* JADX WARN: Finally extract failed */
    public void systemReady(java.lang.Runnable goingCallback, com.android.server.utils.TimingsTraceAndSlog t) {
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("AMS:systemReady");
        t.traceBegin("PhaseActivityManagerReady");
        this.mSystemServiceManager.preSystemReady();
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (this.mSystemReady) {
                    if (goingCallback != null) {
                        goingCallback.run();
                    }
                    t.traceEnd();
                    return;
                }
                t.traceBegin("controllersReady");
                this.mLocalDeviceIdleController = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
                this.mActivityTaskManager.onSystemReady();
                this.mUserController.onSystemReady();
                this.mAppOpsService.systemReady();
                this.mProcessList.onSystemReady();
                this.mAppRestrictionController.onSystemReady();
                this.mSystemReady = true;
                t.traceEnd();
                resetPriorityAfterLockedSection();
                try {
                    sTheRealBuildSerial = android.os.IDeviceIdentifiersPolicyService.Stub.asInterface(android.os.ServiceManager.getService("device_identifiers")).getSerial();
                } catch (android.os.RemoteException e) {
                }
                t.traceBegin("killProcesses");
                java.util.ArrayList<com.android.server.am.ProcessRecord> procsToKill = null;
                synchronized (this.mPidsSelfLocked) {
                    for (int i = this.mPidsSelfLocked.size() - 1; i >= 0; i--) {
                        com.android.server.am.ProcessRecord proc = this.mPidsSelfLocked.valueAt(i);
                        if (!isAllowedWhileBooting(proc.info)) {
                            if (procsToKill == null) {
                                procsToKill = new java.util.ArrayList<>();
                            }
                            procsToKill.add(proc);
                        }
                    }
                }
                boostPriorityForLockedSection();
                synchronized (this) {
                    if (procsToKill != null) {
                        try {
                            for (int i2 = procsToKill.size() - 1; i2 >= 0; i2--) {
                                com.android.server.am.ProcessRecord proc2 = procsToKill.get(i2);
                                android.util.Slog.i("ActivityManager", "Removing system update proc: " + proc2);
                                this.mProcessList.removeProcessLocked(proc2, true, false, 13, 8, "system update done");
                            }
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    this.mProcessesReady = true;
                }
                resetPriorityAfterLockedSection();
                t.traceEnd();
                android.util.Slog.i("ActivityManager", "System now ready");
                com.android.server.am.EventLogTags.writeBootProgressAmsReady(android.os.SystemClock.uptimeMillis());
                ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("AMS:AMS_READY");
                t.traceBegin("updateTopComponentForFactoryTest");
                this.mActivityManagerServiceExt.onOplusSystemReady();
                this.mAtmInternal.updateTopComponentForFactoryTest();
                t.traceEnd();
                t.traceBegin("registerActivityLaunchObserver");
                this.mAtmInternal.getLaunchObserverRegistry().registerLaunchObserver(this.mActivityLaunchObserver);
                t.traceEnd();
                t.traceBegin("watchDeviceProvisioning");
                watchDeviceProvisioning(this.mContext);
                t.traceEnd();
                t.traceBegin("retrieveSettings");
                retrieveSettings();
                t.traceEnd();
                t.traceBegin("Ugm.onSystemReady");
                this.mUgmInternal.onSystemReady();
                t.traceEnd();
                t.traceBegin("updateForceBackgroundCheck");
                android.os.PowerManagerInternal pmi = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
                if (pmi != null) {
                    pmi.registerLowPowerModeObserver(12, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda28
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$systemReady$9((android.os.PowerSaveState) obj);
                        }
                    });
                    updateForceBackgroundCheck(pmi.getLowPowerState(12).batterySaverEnabled);
                } else {
                    android.util.Slog.wtf("ActivityManager", "PowerManagerInternal not found.");
                }
                t.traceEnd();
                if (goingCallback != null) {
                    goingCallback.run();
                }
                t.traceBegin("getCurrentUser");
                int currentUserId = this.mUserController.getCurrentUserId();
                android.util.Slog.i("ActivityManager", "Current user:" + currentUserId);
                if (currentUserId != 0 && !this.mUserController.isSystemUserStarted()) {
                    throw new java.lang.RuntimeException("System user not started while current user is:" + currentUserId);
                }
                t.traceEnd();
                t.traceBegin("ActivityManagerStartApps");
                this.mBatteryStatsService.onSystemReady();
                this.mBatteryStatsService.noteEvent(32775, java.lang.Integer.toString(currentUserId), currentUserId);
                this.mBatteryStatsService.noteEvent(32776, java.lang.Integer.toString(currentUserId), currentUserId);
                this.mUserController.onSystemUserStarting();
                boostPriorityForLockedSection();
                synchronized (this) {
                    try {
                        t.traceBegin("startPersistentApps");
                        startPersistentApps(524288);
                        t.traceEnd();
                        this.mBooting = true;
                        if (android.os.SystemProperties.getBoolean(SYSTEM_USER_HOME_NEEDED, false)) {
                            t.traceBegin("enableHomeActivity");
                            android.content.ComponentName cName = new android.content.ComponentName(this.mContext, (java.lang.Class<?>) com.android.internal.app.SystemUserHomeActivity.class);
                            try {
                                android.app.AppGlobals.getPackageManager().setComponentEnabledSetting(cName, 1, 0, 0, "am");
                                t.traceEnd();
                            } catch (android.os.RemoteException e2) {
                                throw e2.rethrowAsRuntimeException();
                            }
                        }
                        boolean isBootingSystemUser = currentUserId == 0;
                        if (isBootingSystemUser && !android.os.UserManager.isHeadlessSystemUserMode()) {
                            t.traceBegin("startHomeOnAllDisplays");
                            this.mAtmInternal.startHomeOnAllDisplays(currentUserId, "systemReady");
                            t.traceEnd();
                        }
                        com.android.server.am.ActivityManagerService.MainHandler mainHandler = this.mHandler;
                        final com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal = this.mAtmInternal;
                        java.util.Objects.requireNonNull(activityTaskManagerInternal);
                        mainHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda29
                            @Override // java.lang.Runnable
                            public final void run() {
                                activityTaskManagerInternal.showSystemReadyErrorDialogsIfNeeded();
                            }
                        });
                        if (isBootingSystemUser) {
                            t.traceBegin("sendUserStartBroadcast");
                            int callingUid = android.os.Binder.getCallingUid();
                            int callingPid = android.os.Binder.getCallingPid();
                            long ident = android.os.Binder.clearCallingIdentity();
                            try {
                                this.mUserController.sendUserStartedBroadcast(currentUserId, callingUid, callingPid);
                                this.mUserController.sendUserStartingBroadcast(currentUserId, callingUid, callingPid);
                            } finally {
                                try {
                                } finally {
                                }
                            }
                            t.traceEnd();
                        } else {
                            android.util.Slog.i("ActivityManager", "Not sending multi-user broadcasts for non-system user " + currentUserId);
                        }
                        t.traceBegin("resumeTopActivities");
                        this.mAtmInternal.resumeTopActivities(false);
                        t.traceEnd();
                        if (isBootingSystemUser) {
                            t.traceBegin("sendUserSwitchBroadcasts");
                            this.mUserController.sendUserSwitchBroadcasts(-1, currentUserId);
                            t.traceEnd();
                        }
                        t.traceBegin("setBinderProxies");
                        com.android.internal.os.BinderInternal.nSetBinderProxyCountWatermarks(6000, BINDER_PROXY_LOW_WATERMARK, BINDER_PROXY_WARNING_WATERMARK);
                        com.android.internal.os.BinderInternal.nSetBinderProxyCountEnabled(true);
                        com.android.internal.os.BinderInternal.setBinderProxyCountCallback(new com.android.server.am.ActivityManagerService.MyBinderProxyCountEventListener(), this.mHandler);
                        t.traceEnd();
                        t.traceEnd();
                        t.traceBegin("componentAlias");
                        this.mComponentAliasResolver.onSystemReady(this.mConstants.mEnableComponentAlias, this.mConstants.mComponentAliasOverrides);
                        t.traceEnd();
                        t.traceEnd();
                    } finally {
                        resetPriorityAfterLockedSection();
                    }
                }
                resetPriorityAfterLockedSection();
                this.mActivityManagerServiceExt.hookSystemReady(this.mUiContext, this.mUiHandler, this.mContext, this);
                this.mActivityManagerServiceExt.isDisableDelayMCPKill(this);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$9(android.os.PowerSaveState state) {
        updateForceBackgroundCheck(state.batterySaverEnabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class MyBinderProxyCountEventListener implements com.android.internal.os.BinderInternal.BinderProxyCountEventListener {
        private MyBinderProxyCountEventListener() {
        }

        public void onLimitReached(final int uid) {
            new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$MyBinderProxyCountEventListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onLimitReached$0(uid);
                }
            }, "BinderProxy Dump: " + uid).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: handleLimitReached, reason: merged with bridge method [inline-methods] */
        public void lambda$onLimitReached$0(int uid) {
            android.util.Slog.wtf("ActivityManager", "Uid " + uid + " sent too many Binders to uid " + android.os.Process.myUid());
            try {
                android.os.BinderProxy.dumpProxyDebugInfo();
            } catch (java.util.ConcurrentModificationException e) {
                android.util.Slog.e("ActivityManager", "ConcurrentModificationException while BinderProxy.dumpProxyDebugInfo");
            }
            com.android.server.criticalevents.CriticalEventLog.getInstance().logExcessiveBinderCalls(uid);
            if (uid == 1000 || uid == 1073) {
                android.util.Slog.i("ActivityManager", "Skipping kill (uid is SYSTEM)");
            } else {
                com.android.server.am.ActivityManagerService.this.killUid(android.os.UserHandle.getAppId(uid), android.os.UserHandle.getUserId(uid), 9, 29, "Too many Binders sent to SYSTEM");
                dalvik.system.VMRuntime.getRuntime().requestConcurrentGC();
            }
        }

        public void onWarningThresholdReached(int uid) {
            if (com.android.server.am.Flags.logExcessiveBinderProxies()) {
                android.util.Slog.w("ActivityManager", "Uid " + uid + " sent too many (" + com.android.server.am.ActivityManagerService.BINDER_PROXY_WARNING_WATERMARK + ") Binders to uid " + android.os.Process.myUid());
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.EXCESSIVE_BINDER_PROXY_COUNT_REPORTED, uid);
            }
        }
    }

    private void watchDeviceProvisioning(final android.content.Context context) {
        if (isDeviceProvisioned(context)) {
            this.mActivityManagerServiceExt.hookWatchDeviceProvisioning(false);
            android.os.SystemProperties.set(SYSTEM_PROPERTY_DEVICE_PROVISIONED, "1");
        } else {
            context.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("device_provisioned"), false, new android.database.ContentObserver(new android.os.Handler(android.os.Looper.getMainLooper())) { // from class: com.android.server.am.ActivityManagerService.13
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    if (com.android.server.am.ActivityManagerService.this.isDeviceProvisioned(context)) {
                        com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.hookWatchDeviceProvisioning(true);
                        android.os.SystemProperties.set(com.android.server.am.ActivityManagerService.SYSTEM_PROPERTY_DEVICE_PROVISIONED, "1");
                        context.getContentResolver().unregisterContentObserver(this);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceProvisioned(android.content.Context context) {
        return android.provider.Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startBroadcastObservers() {
        this.mBroadcastQueue.start(this.mContext.getContentResolver());
    }

    private void updateForceBackgroundCheck(boolean enabled) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        if (this.mForceBackgroundCheck != enabled) {
                            this.mForceBackgroundCheck = enabled;
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                                android.util.Slog.i("ActivityManager", "Force background check " + (enabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
                            }
                            if (this.mForceBackgroundCheck) {
                                this.mProcessList.doStopUidForIdleUidsLocked();
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void killAppAtUsersRequest(com.android.server.am.ProcessRecord app) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mAppErrors.killAppAtUserRequestLocked(app);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void handleApplicationCrash(android.os.IBinder app, android.app.ApplicationErrorReport.ParcelableCrashInfo crashInfo) {
        java.lang.String str;
        com.android.server.am.ProcessRecord r = findAppProcess(app, "Crash");
        if (app == null) {
            str = "system_server";
        } else {
            str = r == null ? "unknown" : r.processName;
        }
        java.lang.String processName = str;
        this.mActivityManagerServiceExt.hookHandleApplicationCrashBeforeInner(r, crashInfo);
        this.mActivityManagerServiceExt.hookHandlerMarketCrash(processName, crashInfo);
        handleApplicationCrashInner("crash", r, processName, crashInfo);
        this.mActivityManagerServiceExt.handleApplicationCrash(this.mUsageStatsService, app, r, 32, android.os.Process.myPid());
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x008b A[PHI: r0
  0x008b: PHI (r0v1 float) = (r0v0 float), (r0v0 float), (r0v0 float), (r0v47 float), (r0v47 float), (r0v47 float), (r0v47 float) binds: [B:3:0x000f, B:5:0x0013, B:7:0x0019, B:12:0x0035, B:14:0x003b, B:16:0x0041, B:18:0x0078] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void handleApplicationCrashInner(java.lang.String r47, com.android.server.am.ProcessRecord r48, java.lang.String r49, android.app.ApplicationErrorReport.CrashInfo r50) {
        /*
            Method dump skipped, instruction units count: 720
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.handleApplicationCrashInner(java.lang.String, com.android.server.am.ProcessRecord, java.lang.String, android.app.ApplicationErrorReport$CrashInfo):void");
    }

    public void handleApplicationStrictModeViolation(android.os.IBinder app, int penaltyMask, android.os.StrictMode.ViolationInfo info) {
        com.android.server.am.ProcessRecord r = findAppProcess(app, "StrictMode");
        if ((67108864 & penaltyMask) != 0) {
            java.lang.Integer stackFingerprint = java.lang.Integer.valueOf(info.hashCode());
            boolean logIt = true;
            synchronized (this.mAlreadyLoggedViolatedStacks) {
                if (this.mAlreadyLoggedViolatedStacks.contains(stackFingerprint)) {
                    logIt = false;
                } else {
                    if (this.mAlreadyLoggedViolatedStacks.size() >= 5000) {
                        this.mAlreadyLoggedViolatedStacks.clear();
                    }
                    this.mAlreadyLoggedViolatedStacks.add(stackFingerprint);
                }
            }
            if (logIt) {
                logStrictModeViolationToDropBox(r, info);
            }
        }
        if ((536870912 & penaltyMask) != 0) {
            com.android.server.am.AppErrorResult result = new com.android.server.am.AppErrorResult();
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                android.os.Message msg = android.os.Message.obtain();
                msg.what = 26;
                java.util.HashMap<java.lang.String, java.lang.Object> data = new java.util.HashMap<>();
                data.put("result", result);
                data.put("app", r);
                data.put("info", info);
                msg.obj = data;
                this.mUiHandler.sendMessage(msg);
                android.os.Binder.restoreCallingIdentity(origId);
                int res = result.get();
                android.util.Slog.w("ActivityManager", "handleApplicationStrictModeViolation; res=" + res);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(origId);
                throw th;
            }
        }
    }

    public synchronized void registerStrictModeCallback(android.os.IBinder callback) {
        boostPriorityForLockedSection();
        final int callingPid = android.os.Binder.getCallingPid();
        this.mStrictModeCallbacks.put(callingPid, android.app.IUnsafeIntentStrictModeCallback.Stub.asInterface(callback));
        try {
            callback.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda6
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$registerStrictModeCallback$10(callingPid);
                }
            }, 0);
        } catch (android.os.RemoteException e) {
            this.mStrictModeCallbacks.remove(callingPid);
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerStrictModeCallback$10(int callingPid) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mStrictModeCallbacks.remove(callingPid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    private void logStrictModeViolationToDropBox(com.android.server.am.ProcessRecord process, android.os.StrictMode.ViolationInfo info) {
        java.lang.String processName;
        if (info == null) {
            return;
        }
        boolean isSystemApp = process == null || (process.info.flags & 129) != 0;
        if (process != null && process.getPid() == MY_PID) {
            processName = "system_server";
        } else {
            processName = process == null ? "unknown" : process.processName;
        }
        final android.os.DropBoxManager dbox = (android.os.DropBoxManager) this.mContext.getSystemService("dropbox");
        final java.lang.String dropboxTag = processClass(process) + "_strictmode";
        if (dbox == null || !dbox.isTagEnabled(dropboxTag)) {
            return;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(1024);
        synchronized (sb) {
            appendDropBoxProcessHeaders(process, processName, null, sb);
            sb.append("Build: ").append(android.os.Build.FINGERPRINT).append("\n");
            sb.append("System-App: ").append(isSystemApp).append("\n");
            sb.append("Uptime-Millis: ").append(info.violationUptimeMillis).append("\n");
            if (info.violationNumThisLoop != 0) {
                sb.append("Loop-Violation-Number: ").append(info.violationNumThisLoop).append("\n");
            }
            if (info.numAnimationsRunning != 0) {
                sb.append("Animations-Running: ").append(info.numAnimationsRunning).append("\n");
            }
            if (info.broadcastIntentAction != null) {
                sb.append("Broadcast-Intent-Action: ").append(info.broadcastIntentAction).append("\n");
            }
            if (info.durationMillis != -1) {
                sb.append("Duration-Millis: ").append(info.durationMillis).append("\n");
            }
            if (info.numInstances != -1) {
                sb.append("Instance-Count: ").append(info.numInstances).append("\n");
            }
            if (info.tags != null) {
                for (java.lang.String tag : info.tags) {
                    sb.append("Span-Tag: ").append(tag).append("\n");
                }
            }
            sb.append("\n");
            sb.append(info.getStackTrace());
            sb.append("\n");
            if (info.getViolationDetails() != null) {
                sb.append(info.getViolationDetails());
                sb.append("\n");
            }
        }
        final java.lang.String res = sb.toString();
        com.android.server.IoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                dbox.addText(dropboxTag, res);
            }
        });
    }

    public boolean handleApplicationWtf(final android.os.IBinder app, final java.lang.String tag, boolean system2, final android.app.ApplicationErrorReport.ParcelableCrashInfo crashInfo, int immediateCallerPid) {
        final int callingUid = android.os.Binder.getCallingUid();
        final int callingPid = android.os.Binder.getCallingPid();
        com.android.internal.util.Preconditions.checkNotNull(crashInfo);
        if (system2 || immediateCallerPid == android.os.Process.myPid()) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService.14
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.am.ActivityManagerService.this.handleApplicationWtfInner(callingUid, callingPid, app, tag, crashInfo);
                }
            });
            return false;
        }
        com.android.server.am.ProcessRecord r = handleApplicationWtfInner(callingUid, callingPid, app, tag, crashInfo);
        boolean isFatal = android.os.Build.IS_ENG || android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "wtf_is_fatal", 0) != 0;
        boolean isSystem = r == null || r.isPersistent();
        if (!isFatal || isSystem) {
            return false;
        }
        this.mAppErrors.crashApplication(r, crashInfo);
        return true;
    }

    com.android.server.am.ProcessRecord handleApplicationWtfInner(int callingUid, int callingPid, android.os.IBinder app, java.lang.String tag, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
        java.lang.String processName;
        com.android.server.am.ProcessRecord r = findAppProcess(app, "WTF");
        if (app == null) {
            processName = "system_server";
        } else {
            processName = r == null ? "unknown" : r.processName;
        }
        com.android.server.am.EventLogTags.writeAmWtf(android.os.UserHandle.getUserId(callingUid), callingPid, processName, r == null ? -1 : r.info.flags, tag, crashInfo != null ? crashInfo.exceptionMessage : "unknown");
        com.android.internal.util.FrameworkStatsLog.write(80, callingUid, tag, processName, callingPid, r != null ? r.getProcessClassEnum() : 0);
        addErrorToDropBox("wtf", r, processName, null, null, null, tag, null, null, crashInfo, null, null, null, null);
        return r;
    }

    public void schedulePendingSystemServerWtfs(final java.util.LinkedList<android.util.Pair<java.lang.String, android.app.ApplicationErrorReport.CrashInfo>> list) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$schedulePendingSystemServerWtfs$12(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handlePendingSystemServerWtfs, reason: merged with bridge method [inline-methods] */
    public void lambda$schedulePendingSystemServerWtfs$12(java.util.LinkedList<android.util.Pair<java.lang.String, android.app.ApplicationErrorReport.CrashInfo>> list) {
        com.android.server.am.ProcessRecord proc;
        synchronized (this.mPidsSelfLocked) {
            proc = this.mPidsSelfLocked.get(MY_PID);
        }
        android.util.Pair<java.lang.String, android.app.ApplicationErrorReport.CrashInfo> p = list.poll();
        while (p != null) {
            addErrorToDropBox("wtf", proc, "system_server", null, null, null, (java.lang.String) p.first, null, null, (android.app.ApplicationErrorReport.CrashInfo) p.second, null, null, null, null);
            android.util.Pair<java.lang.String, android.app.ApplicationErrorReport.CrashInfo> p2 = list.poll();
            p = p2;
        }
    }

    private com.android.server.am.ProcessRecord findAppProcess(android.os.IBinder app, java.lang.String reason) {
        com.android.server.am.ProcessRecord processRecordFindAppProcessLOSP;
        if (app == null) {
            return null;
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                processRecordFindAppProcessLOSP = this.mProcessList.findAppProcessLOSP(app, reason);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return processRecordFindAppProcessLOSP;
    }

    void appendDropBoxProcessHeaders(com.android.server.am.ProcessRecord process, java.lang.String processName, com.android.server.am.ActivityManagerService.VolatileDropboxEntryStates volatileStates, final java.lang.StringBuilder sb) {
        sb.append("SystemUptimeMs: ").append(android.os.SystemClock.uptimeMillis()).append("\n");
        if (process == null) {
            sb.append("Process: ").append(processName).append("\n");
            return;
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                sb.append("Process: ").append(processName).append("\n");
                sb.append("PID: ").append(process.getPid()).append("\n");
                sb.append("UID: ").append(process.uid).append("\n");
                if (process.mOptRecord != null) {
                    sb.append("Frozen: ").append((volatileStates == null || volatileStates.isProcessFrozen() == null) ? process.mOptRecord.isFrozen() : volatileStates.isProcessFrozen().booleanValue()).append("\n");
                }
                if (volatileStates != null && volatileStates.getTimestamp() != null) {
                    java.lang.String formattedTime = DROPBOX_TIME_FORMATTER.format(volatileStates.getTimestamp());
                    sb.append("Timestamp: ").append(formattedTime).append("\n");
                }
                int flags = process.info.flags;
                final android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
                sb.append("Flags: 0x").append(java.lang.Integer.toHexString(flags)).append("\n");
                final int callingUserId = android.os.UserHandle.getCallingUserId();
                process.getPkgList().forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$appendDropBoxProcessHeaders$13(sb, pm, callingUserId, (java.lang.String) obj);
                    }
                });
                sb.append("PID: ").append(process.getPid()).append("\n");
                if (process.info.isInstantApp()) {
                    sb.append("Instant-App: true\n");
                }
                if (process.isSdkSandbox) {
                    java.lang.String clientPackage = process.sdkSandboxClientAppPackage;
                    try {
                        android.content.pm.PackageInfo pi = pm.getPackageInfo(clientPackage, 1024L, callingUserId);
                        if (pi != null) {
                            appendSdkSandboxClientPackageHeader(sb, pi);
                            appendSdkSandboxLibraryHeaders(sb, pi);
                        } else {
                            android.util.Slog.e("ActivityManager", "PackageInfo is null for SDK sandbox client: " + clientPackage);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e("ActivityManager", "Error getting package info for SDK sandbox client: " + clientPackage, e);
                    }
                    sb.append("SdkSandbox: true\n");
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$appendDropBoxProcessHeaders$13(java.lang.StringBuilder sb, android.content.pm.IPackageManager pm, int callingUserId, java.lang.String pkg) {
        sb.append("Package: ").append(pkg);
        this.mActivityManagerServiceExt.setErrorPackageName(pkg);
        try {
            android.content.pm.PackageInfo pi = pm.getPackageInfo(pkg, 0L, callingUserId);
            if (pi != null) {
                sb.append(" v").append(pi.getLongVersionCode());
                if (pi.versionName != null) {
                    sb.append(" (").append(pi.versionName).append(")");
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e("ActivityManager", "Error getting package info: " + pkg, e);
        }
        sb.append("\n");
    }

    private void appendSdkSandboxClientPackageHeader(java.lang.StringBuilder sb, android.content.pm.PackageInfo clientPackageInfo) {
        sb.append("SdkSandbox-Client-Package: ").append(clientPackageInfo.packageName);
        sb.append(" v").append(clientPackageInfo.getLongVersionCode());
        if (clientPackageInfo.versionName != null) {
            sb.append(" (").append(clientPackageInfo.versionName).append(")");
        }
        sb.append("\n");
    }

    private void appendSdkSandboxLibraryHeaders(java.lang.StringBuilder sb, android.content.pm.PackageInfo clientPackageInfo) {
        android.content.pm.ApplicationInfo info = clientPackageInfo.applicationInfo;
        java.util.List<android.content.pm.SharedLibraryInfo> sharedLibraries = info.getSharedLibraryInfos();
        int size = sharedLibraries.size();
        for (int j = 0; j < size; j++) {
            android.content.pm.SharedLibraryInfo sharedLibrary = sharedLibraries.get(j);
            if (sharedLibrary.isSdk()) {
                sb.append("SdkSandbox-Library: ").append(sharedLibrary.getPackageName());
                android.content.pm.VersionedPackage versionedPackage = sharedLibrary.getDeclaringPackage();
                sb.append(" v").append(versionedPackage.getLongVersionCode());
                sb.append("\n");
            }
        }
    }

    private static java.lang.String processClass(com.android.server.am.ProcessRecord process) {
        if (process == null || process.getPid() == MY_PID) {
            return "system_server";
        }
        if (process.info.isSystemApp() || process.info.isSystemExt()) {
            return "system_app";
        }
        return "data_app";
    }

    public void initDropboxRateLimiter() {
        this.mDropboxRateLimiter.init();
    }

    public void handleAppNotResponding(com.android.server.am.ProcessRecord anrProcess, java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo aInfo, java.lang.String parentShortComponentName, com.android.server.wm.WindowProcessController parentProcess, boolean aboveSystem, java.lang.String annotation, java.lang.String eventId) {
        try {
            com.android.server.am.OplusCrashInfo oplusCrashIfno = new com.android.server.am.OplusCrashInfo();
            oplusCrashIfno.anrProcess = anrProcess;
            oplusCrashIfno.activityShortComponentName = activityShortComponentName;
            oplusCrashIfno.aInfo = aInfo;
            oplusCrashIfno.parentShortComponentName = parentShortComponentName;
            oplusCrashIfno.parentProcess = parentProcess;
            oplusCrashIfno.aboveSystem = aboveSystem;
            oplusCrashIfno.annotation = annotation;
            oplusCrashIfno.eventId = eventId;
            this.mActivityManagerServiceExt.hookHandleAppNotResponding(oplusCrashIfno);
        } catch (java.lang.NoClassDefFoundError e) {
            android.util.Slog.e("ActivityManager", "handleAppNotResponding NoClassDefFoundError");
        }
    }

    public void addErrorToDropBox(java.lang.String eventType, com.android.server.am.ProcessRecord process, java.lang.String processName, java.lang.String activityShortComponentName, java.lang.String parentShortComponentName, com.android.server.am.ProcessRecord parentProcess, java.lang.String subject, java.lang.String report, java.io.File dataFile, android.app.ApplicationErrorReport.CrashInfo crashInfo, java.lang.Float loadingProgress, android.os.incremental.IncrementalMetrics incrementalMetrics, java.util.UUID errorId) {
        addErrorToDropBox(eventType, process, processName, activityShortComponentName, parentShortComponentName, parentProcess, subject, report, dataFile, crashInfo, loadingProgress, incrementalMetrics, errorId, null);
    }

    public void addErrorToDropBox(java.lang.String eventType, com.android.server.am.ProcessRecord process, java.lang.String processName, java.lang.String activityShortComponentName, java.lang.String parentShortComponentName, com.android.server.am.ProcessRecord parentProcess, java.lang.String subject, java.lang.String report, java.io.File dataFile, android.app.ApplicationErrorReport.CrashInfo crashInfo, java.lang.Float loadingProgress, android.os.incremental.IncrementalMetrics incrementalMetrics, java.util.UUID errorId, com.android.server.am.ActivityManagerService.VolatileDropboxEntryStates volatileStates) {
        java.lang.String eventId;
        if (errorId != null) {
            eventId = errorId.toString();
        } else {
            eventId = java.util.UUID.randomUUID().toString();
        }
        addErrorToDropBox(eventType, process, processName, activityShortComponentName, parentShortComponentName, parentProcess, subject, report, dataFile, crashInfo, loadingProgress, incrementalMetrics, errorId, volatileStates, eventId);
    }

    public void addErrorToDropBox(java.lang.String eventType, final com.android.server.am.ProcessRecord process, java.lang.String processName, java.lang.String activityShortComponentName, java.lang.String parentShortComponentName, com.android.server.am.ProcessRecord parentProcess, java.lang.String subject, final java.lang.String report, final java.io.File dataFile, final android.app.ApplicationErrorReport.CrashInfo crashInfo, java.lang.Float loadingProgress, android.os.incremental.IncrementalMetrics incrementalMetrics, java.util.UUID errorId, com.android.server.am.ActivityManagerService.VolatileDropboxEntryStates volatileStates, java.lang.String eventId) {
        com.android.server.am.OplusCrashInfo oplusCrashIfno;
        java.lang.Object obj;
        long pageSize;
        try {
            final android.os.DropBoxManager dbox = (android.os.DropBoxManager) this.mContext.getSystemService(android.os.DropBoxManager.class);
            final java.lang.String dropboxTag = processClass(process) + "_" + eventType;
            if (dbox != null && dbox.isTagEnabled(dropboxTag)) {
                com.android.server.am.DropboxRateLimiter.RateLimitResult rateLimitResult = this.mDropboxRateLimiter.shouldRateLimit(eventType, processName);
                if (rateLimitResult.shouldRateLimit()) {
                    return;
                }
                final java.lang.StringBuilder sb = new java.lang.StringBuilder(1024);
                appendDropBoxProcessHeaders(process, processName, volatileStates, sb);
                com.android.server.am.OplusCrashInfo oplusCrashIfno2 = new com.android.server.am.OplusCrashInfo();
                oplusCrashIfno2.context = this.mContext;
                oplusCrashIfno2.dropboxTag = dropboxTag;
                oplusCrashIfno2.eventType = eventType;
                oplusCrashIfno2.process = process;
                oplusCrashIfno2.processName = processName;
                oplusCrashIfno2.subject = subject;
                oplusCrashIfno2.dataFile = dataFile;
                oplusCrashIfno2.crashInfo = crashInfo;
                oplusCrashIfno2.eventId = eventId;
                this.mActivityManagerServiceExt.hookAddErrorToDropBox(oplusCrashIfno2);
                if (process == null) {
                    oplusCrashIfno = oplusCrashIfno2;
                } else {
                    oplusCrashIfno = oplusCrashIfno2;
                    sb.append("Foreground: ").append(process.isInterestingToUserLocked() ? "Yes" : "No").append("\n");
                    if (process.getStartUptime() > 0) {
                        long runtimeMillis = android.os.SystemClock.uptimeMillis() - process.getStartUptime();
                        sb.append("Process-Runtime: ").append(runtimeMillis).append("\n");
                    }
                }
                if (eventType.equals("crash")) {
                    obj = "crash";
                    java.lang.String formattedTime = DROPBOX_TIME_FORMATTER.format(java.time.Instant.now().atZone(java.time.ZoneId.systemDefault()));
                    sb.append("Timestamp: ").append(formattedTime).append("\n");
                } else {
                    obj = "crash";
                }
                if (activityShortComponentName != null) {
                    sb.append("Activity: ").append(activityShortComponentName).append("\n");
                }
                if (parentShortComponentName != null) {
                    if (parentProcess != null && parentProcess.getPid() != process.getPid()) {
                        sb.append("Parent-Process: ").append(parentProcess.processName).append("\n");
                    }
                    if (!parentShortComponentName.equals(activityShortComponentName)) {
                        sb.append("Parent-Activity: ").append(parentShortComponentName).append("\n");
                    }
                }
                if (subject != null) {
                    sb.append("Subject: ").append(subject).append("\n");
                }
                if (errorId != null) {
                    sb.append("ErrorId: ").append(errorId.toString()).append("\n");
                }
                sb.append("Build: ").append(android.os.Build.FINGERPRINT).append("\n");
                long pageSize2 = android.system.Os.sysconf(android.system.OsConstants._SC_PAGESIZE);
                if (pageSize2 != 4096) {
                    sb.append("PageSize: ").append(pageSize2).append("\n");
                }
                if (android.os.Debug.isDebuggerConnected()) {
                    sb.append("Debugger: Connected\n");
                }
                if (crashInfo == null || crashInfo.exceptionHandlerClassName == null || crashInfo.exceptionHandlerClassName.isEmpty()) {
                    pageSize = pageSize2;
                } else {
                    pageSize = pageSize2;
                    sb.append("Crash-Handler: ").append(crashInfo.exceptionHandlerClassName).append("\n");
                }
                if (crashInfo != null && crashInfo.crashTag != null && !crashInfo.crashTag.isEmpty()) {
                    sb.append("Crash-Tag: ").append(crashInfo.crashTag).append("\n");
                }
                if (loadingProgress != null) {
                    sb.append("Loading-Progress: ").append(loadingProgress.floatValue()).append("\n");
                }
                if (incrementalMetrics != null) {
                    sb.append("Incremental: Yes").append("\n");
                    long millisSinceOldestPendingRead = incrementalMetrics.getMillisSinceOldestPendingRead();
                    if (millisSinceOldestPendingRead > 0) {
                        sb.append("Millis-Since-Oldest-Pending-Read: ").append(millisSinceOldestPendingRead).append("\n");
                    }
                }
                sb.append(rateLimitResult.createHeader());
                sb.append("\n");
                final boolean runSynchronously = process == null;
                java.lang.Object obj2 = obj;
                java.lang.Thread worker = new java.lang.Thread("Error dump: " + dropboxTag) { // from class: com.android.server.am.ActivityManagerService.15
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        int logcatLines;
                        int kernelLogLines;
                        if (report != null) {
                            sb.append(report);
                        }
                        java.lang.String logcatSetting = "logcat_for_" + dropboxTag;
                        java.lang.String kerLogSetting = "kernel_logs_for_" + dropboxTag;
                        java.lang.String maxBytesSetting = "max_error_bytes_for_" + dropboxTag;
                        if (android.os.Build.IS_USER) {
                            logcatLines = 0;
                        } else {
                            logcatLines = android.provider.Settings.Global.getInt(com.android.server.am.ActivityManagerService.this.mContext.getContentResolver(), logcatSetting, 0);
                        }
                        if (android.os.Build.IS_USER) {
                            kernelLogLines = 0;
                        } else {
                            kernelLogLines = android.provider.Settings.Global.getInt(com.android.server.am.ActivityManagerService.this.mContext.getContentResolver(), kerLogSetting, 0);
                        }
                        int dropboxMaxSize = android.provider.Settings.Global.getInt(com.android.server.am.ActivityManagerService.this.mContext.getContentResolver(), maxBytesSetting, com.android.server.am.ActivityManagerService.DROPBOX_DEFAULT_MAX_SIZE);
                        if (dataFile != null) {
                            sb.append(com.android.server.am.ActivityManagerService.DATA_FILE_PATH_HEADER).append(dataFile.getAbsolutePath()).append('\n');
                            int maxDataFileSize = com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.getDataFileSizeAjusted((((dropboxMaxSize - sb.length()) - (logcatLines * 100)) - (kernelLogLines * 100)) - com.android.server.am.ActivityManagerService.DATA_FILE_PATH_FOOTER.length(), logcatLines, dataFile);
                            if (maxDataFileSize > 0) {
                                try {
                                    sb.append(android.os.FileUtils.readTextFile(dataFile, maxDataFileSize, "\n\n[[TRUNCATED]]\n"));
                                } catch (java.io.IOException e) {
                                    android.util.Slog.e("ActivityManager", "Error reading " + dataFile, e);
                                }
                            }
                            sb.append(com.android.server.am.ActivityManagerService.DATA_FILE_PATH_FOOTER);
                        }
                        if (crashInfo != null && crashInfo.stackTrace != null) {
                            sb.append(crashInfo.stackTrace);
                        }
                        boolean shouldAddLogs = logcatLines > 0 || kernelLogLines > 0;
                        if (!runSynchronously && shouldAddLogs) {
                            sb.append("\n");
                            if (logcatLines > 0) {
                                com.android.server.am.ActivityManagerService.fetchLogcatBuffers(sb, logcatLines, 10, java.util.List.of("events", "system", "main", "crash"));
                            }
                            if (kernelLogLines > 0) {
                                com.android.server.am.ActivityManagerService.fetchLogcatBuffers(sb, kernelLogLines, 5, java.util.List.of("kernel"));
                            }
                        }
                        dbox.addText(dropboxTag, sb.toString());
                        com.android.server.am.ActivityManagerService.this.mSocExt.onAddErrorToDropBox(dropboxTag, sb.toString(), process == null ? com.android.server.am.ActivityManagerService.MY_PID : process.mPid);
                    }
                };
                if (runSynchronously) {
                    int oldMask = android.os.StrictMode.allowThreadDiskWritesMask();
                    try {
                        worker.run();
                    } finally {
                        android.os.StrictMode.setThreadPolicyMask(oldMask);
                    }
                } else {
                    worker.start();
                    if (process != null && process.mPid == MY_PID && obj2.equals(eventType)) {
                        try {
                            worker.join(2000L);
                        } catch (java.lang.InterruptedException e) {
                        }
                    }
                }
                this.mActivityManagerServiceExt.waitForDumpCondition(processClass(process).equals("system_server"), eventType);
            }
        } catch (java.lang.Exception e2) {
        }
    }

    public java.util.List<android.app.ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() {
        enforceNotIsolatedCaller("getProcessesInErrorState");
        final java.util.List<android.app.ActivityManager.ProcessErrorStateInfo>[] errList = new java.util.List[1];
        final int callingUid = android.os.Binder.getCallingUid();
        final boolean allUsers = android.app.ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) == 0;
        final int userId = android.os.UserHandle.getUserId(callingUid);
        final boolean hasDumpPermission = android.app.ActivityManager.checkUidPermission("android.permission.DUMP", callingUid) == 0;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda21
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.am.ActivityManagerService.lambda$getProcessesInErrorState$14(allUsers, userId, hasDumpPermission, callingUid, errList, (com.android.server.am.ProcessRecord) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return errList[0];
    }

    static /* synthetic */ void lambda$getProcessesInErrorState$14(boolean allUsers, int userId, boolean hasDumpPermission, int callingUid, java.util.List[] errList, com.android.server.am.ProcessRecord app) {
        if (!allUsers && app.userId != userId) {
            return;
        }
        if (!hasDumpPermission && app.info.uid != callingUid) {
            return;
        }
        com.android.server.am.ProcessErrorStateRecord errState = app.mErrorState;
        boolean crashing = errState.isCrashing();
        boolean notResponding = errState.isNotResponding();
        if (app.getThread() != null) {
            if (crashing || notResponding) {
                android.app.ActivityManager.ProcessErrorStateInfo report = null;
                if (crashing) {
                    report = errState.getCrashingReport();
                } else if (notResponding) {
                    report = errState.getNotRespondingReport();
                }
                if (report != null) {
                    if (errList[0] == null) {
                        errList[0] = new java.util.ArrayList(1);
                    }
                    errList[0].add(report);
                    return;
                }
                android.util.Slog.w("ActivityManager", "Missing app error report, app = " + app.processName + " crashing = " + crashing + " notResponding = " + notResponding);
            }
        }
    }

    public java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() {
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> runningAppProcessesLOSP;
        enforceNotIsolatedCaller("getRunningAppProcesses");
        int callingUid = android.os.Binder.getCallingUid();
        int clientTargetSdk = this.mPackageManagerInt.getUidTargetSdkVersion(callingUid);
        boolean allUsers = android.app.ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) == 0;
        int userId = android.os.UserHandle.getUserId(callingUid);
        boolean allUids = this.mAtmInternal.isGetTasksAllowed("getRunningAppProcesses", android.os.Binder.getCallingPid(), callingUid);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                runningAppProcessesLOSP = this.mProcessList.getRunningAppProcessesLOSP(allUsers, userId, allUids, callingUid, clientTargetSdk);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return runningAppProcessesLOSP;
    }

    public java.util.List<android.content.pm.ApplicationInfo> getRunningExternalApplications() {
        enforceNotIsolatedCaller("getRunningExternalApplications");
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> runningApps = getRunningAppProcesses();
        java.util.List<android.content.pm.ApplicationInfo> retList = new java.util.ArrayList<>();
        if (runningApps != null && runningApps.size() > 0) {
            java.util.Set<java.lang.String> extList = new java.util.HashSet<>();
            for (android.app.ActivityManager.RunningAppProcessInfo app : runningApps) {
                if (app.pkgList != null) {
                    for (java.lang.String pkg : app.pkgList) {
                        extList.add(pkg);
                    }
                }
            }
            android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
            for (java.lang.String pkg2 : extList) {
                try {
                    android.content.pm.ApplicationInfo info = pm.getApplicationInfo(pkg2, 0L, android.os.UserHandle.getCallingUserId());
                    if ((info.flags & 262144) != 0) {
                        retList.add(info);
                    }
                } catch (android.os.RemoteException e) {
                }
            }
        }
        return retList;
    }

    public android.content.pm.ParceledListSlice<android.app.ApplicationStartInfo> getHistoricalProcessStartReasons(java.lang.String packageName, int maxNum, int userId) {
        enforceNotIsolatedCaller("getHistoricalProcessStartReasons");
        if (userId == -1 || userId == -2) {
            throw new java.lang.IllegalArgumentException("Unsupported userId");
        }
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        this.mUserController.handleIncomingUser(callingPid, callingUid, userId, true, 0, "getHistoricalProcessStartReasons", null);
        java.util.ArrayList<android.app.ApplicationStartInfo> results = new java.util.ArrayList<>();
        if (!android.text.TextUtils.isEmpty(packageName)) {
            int uid = enforceDumpPermissionForPackage(packageName, userId, callingUid, "getHistoricalProcessStartReasons");
            if (uid != -1) {
                this.mProcessList.getAppStartInfoTracker().getStartInfo(packageName, uid, callingPid, maxNum, results);
            }
        } else {
            this.mProcessList.getAppStartInfoTracker().getStartInfo(packageName, callingUid, callingPid, maxNum, results);
        }
        return new android.content.pm.ParceledListSlice<>(results);
    }

    public void addApplicationStartInfoCompleteListener(android.app.IApplicationStartInfoCompleteListener listener, int userId) {
        enforceNotIsolatedCaller("setApplicationStartInfoCompleteListener");
        if (userId == -1 || userId == -2) {
            throw new java.lang.IllegalArgumentException("Unsupported userId");
        }
        int callingUid = android.os.Binder.getCallingUid();
        this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, true, 0, "addApplicationStartInfoCompleteListener", null);
        this.mProcessList.getAppStartInfoTracker().addStartInfoCompleteListener(listener, android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(callingUid)));
    }

    public void removeApplicationStartInfoCompleteListener(android.app.IApplicationStartInfoCompleteListener listener, int userId) {
        enforceNotIsolatedCaller("clearApplicationStartInfoCompleteListener");
        if (userId == -1 || userId == -2) {
            throw new java.lang.IllegalArgumentException("Unsupported userId");
        }
        int callingUid = android.os.Binder.getCallingUid();
        this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, true, 0, "removeApplicationStartInfoCompleteListener", null);
        this.mProcessList.getAppStartInfoTracker().removeStartInfoCompleteListener(listener, android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(callingUid)), true);
    }

    public void addStartInfoTimestamp(int key, long timestampNs, int userId) {
        enforceNotIsolatedCaller("addStartInfoTimestamp");
        if (userId == -1 || userId == -2) {
            throw new java.lang.IllegalArgumentException("Unsupported userId");
        }
        int callingUid = android.os.Binder.getCallingUid();
        this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, true, 0, "addStartInfoTimestamp", null);
        addStartInfoTimestampInternal(key, timestampNs, userId, callingUid);
    }

    public void reportStartInfoViewTimestamps(long renderThreadDrawStartTimeNs, long framePresentedTimeNs) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(callingUid);
        addStartInfoTimestampInternal(6, renderThreadDrawStartTimeNs, userId, callingUid);
        addStartInfoTimestampInternal(7, framePresentedTimeNs, userId, callingUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addStartInfoTimestampInternal(int key, long timestampNs, int userId, int uid) {
        this.mProcessList.getAppStartInfoTracker().addTimestampToStart(android.provider.Settings.getPackageNameForUid(this.mContext, uid), android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(uid)), timestampNs, key);
    }

    public android.content.pm.ParceledListSlice<android.app.ApplicationExitInfo> getHistoricalProcessExitReasons(java.lang.String packageName, int pid, int maxNum, int userId) {
        enforceNotIsolatedCaller("getHistoricalProcessExitReasons");
        if (userId == -1 || userId == -2) {
            throw new java.lang.IllegalArgumentException("Unsupported userId");
        }
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        android.os.UserHandle.getCallingUserId();
        this.mUserController.handleIncomingUser(callingPid, callingUid, userId, true, 0, "getHistoricalProcessExitReasons", null);
        com.android.server.os.NativeTombstoneManager tombstoneService = (com.android.server.os.NativeTombstoneManager) com.android.server.LocalServices.getService(com.android.server.os.NativeTombstoneManager.class);
        java.util.ArrayList<android.app.ApplicationExitInfo> results = new java.util.ArrayList<>();
        if (!android.text.TextUtils.isEmpty(packageName)) {
            int uid = enforceDumpPermissionForPackage(packageName, userId, callingUid, "getHistoricalProcessExitReasons");
            if (uid != -1) {
                this.mProcessList.mAppExitInfoTracker.getExitInfo(packageName, uid, pid, maxNum, results);
                tombstoneService.collectTombstones(results, uid, pid, maxNum);
            }
        } else {
            this.mProcessList.mAppExitInfoTracker.getExitInfo(packageName, callingUid, pid, maxNum, results);
            tombstoneService.collectTombstones(results, callingUid, pid, maxNum);
        }
        return new android.content.pm.ParceledListSlice<>(results);
    }

    public void setProcessStateSummary(byte[] state) {
        if (state != null && state.length > 128) {
            throw new java.lang.IllegalArgumentException("Data size is too large");
        }
        this.mProcessList.mAppExitInfoTracker.setProcessStateSummary(android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fetchLogcatBuffers(java.lang.StringBuilder sb, int lines, int timeout, java.util.List<java.lang.String> buffers) {
        if (buffers.size() == 0 || lines <= 0 || timeout <= 0) {
            return;
        }
        java.util.List<java.lang.String> command = new java.util.ArrayList<>((buffers.size() * 2) + 10);
        command.add("/system/bin/timeout");
        command.add("-i");
        command.add("-s");
        command.add("SEGV");
        command.add(timeout + "s");
        command.add("/system/bin/logcat");
        command.add("-v");
        command.add("threadtime,UTC");
        for (java.lang.String buffer : buffers) {
            command.add("-b");
            command.add(buffer);
        }
        command.add("-t");
        command.add(java.lang.String.valueOf(lines));
        try {
            java.lang.Process proc = new java.lang.ProcessBuilder(command).redirectErrorStream(true).start();
            try {
                proc.getOutputStream().close();
            } catch (java.io.IOException e) {
            }
            java.io.InputStreamReader reader = new java.io.InputStreamReader(proc.getInputStream());
            try {
                char[] buffer2 = new char[8192];
                while (true) {
                    int numRead = reader.read(buffer2, 0, buffer2.length);
                    if (numRead > 0) {
                        sb.append(buffer2, 0, numRead);
                    } else {
                        reader.close();
                        return;
                    }
                }
            } finally {
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.e("ActivityManager", "Error running logcat", e2);
        }
    }

    int enforceDumpPermissionForPackage(java.lang.String packageName, int userId, int callingUid, java.lang.String function) {
        try {
            if (android.os.Process.isSdkSandboxUid(callingUid)) {
                if (getPackageManager().getSdkSandboxPackageName().equals(packageName)) {
                    return callingUid;
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e("ActivityManager", "Could not get SDK sandbox package name");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int uid = this.mPackageManagerInt.getPackageUid(packageName, 786432L, userId);
            android.os.Binder.restoreCallingIdentity(identity);
            if (android.os.UserHandle.getAppId(uid) != android.os.UserHandle.getAppId(callingUid)) {
                enforceCallingPermission("android.permission.DUMP", function);
            }
            return uid;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    public void getMyMemoryState(android.app.ActivityManager.RunningAppProcessInfo outState) {
        com.android.server.am.ProcessRecord proc;
        if (outState == null) {
            throw new java.lang.IllegalArgumentException("outState is null");
        }
        enforceNotIsolatedCaller("getMyMemoryState");
        int callingUid = android.os.Binder.getCallingUid();
        int clientTargetSdk = this.mPackageManagerInt.getUidTargetSdkVersion(callingUid);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                synchronized (this.mPidsSelfLocked) {
                    proc = this.mPidsSelfLocked.get(android.os.Binder.getCallingPid());
                }
                if (proc != null) {
                    this.mProcessList.fillInProcMemInfoLOSP(proc, outState, clientTargetSdk);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    public int getMemoryTrimLevel() {
        int lastMemoryLevelLocked;
        enforceNotIsolatedCaller("getMyMemoryState");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                lastMemoryLevelLocked = this.mAppProfiler.getLastMemoryLevelLocked();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return lastMemoryLevelLocked;
    }

    void setMemFactorOverride(int level) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (level == this.mAppProfiler.getLastMemoryLevelLocked()) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mAppProfiler.setMemFactorOverrideLocked(level);
                updateOomAdjLocked(16);
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void setServiceRestartBackoffEnabled(java.lang.String packageName, boolean enable, java.lang.String reason) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mServices.setServiceRestartBackoffEnabledLocked(packageName, enable, reason);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    boolean isServiceRestartBackoffEnabled(java.lang.String packageName) {
        boolean zIsServiceRestartBackoffEnabledLocked;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                zIsServiceRestartBackoffEnabledLocked = this.mServices.isServiceRestartBackoffEnabledLocked(packageName);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return zIsServiceRestartBackoffEnabledLocked;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.am.ActivityManagerShellCommand(this, false).exec(this, in, out, err, args, callback, resultReceiver);
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        com.android.server.utils.PriorityDump.dump(this.mPriorityDumper, fd, pw, args);
    }

    private void dumpEverything(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage, int displayIdFilter, boolean dumpClient, boolean dumpNormalPriority, int dumpAppId, boolean dumpProxies) throws java.lang.Throwable {
        java.lang.String str;
        java.io.PrintWriter printWriter;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mConstants.dump(pw);
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        try {
                            this.mOomAdjuster.dumpCachedAppOptimizerSettings(pw);
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
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterProcLockedSection();
                this.mOomAdjuster.dumpCacheOomRankerSettings(pw);
                pw.println();
                if (dumpAll) {
                    pw.println(TICK);
                }
                dumpAllowedAssociationsLocked(fd, pw, args, opti, dumpAll, dumpPackage);
                pw.println();
                if (dumpAll) {
                    pw.println(TICK);
                }
                this.mPendingIntentController.dumpPendingIntents(pw, dumpAll, dumpPackage);
                pw.println();
                if (dumpAll) {
                    pw.println(TICK);
                }
                dumpBroadcastsLocked(fd, pw, args, opti, dumpAll, dumpPackage);
                pw.println();
                if (dumpAll) {
                    pw.println(TICK);
                }
                if (dumpAll || dumpPackage != null) {
                    dumpBroadcastStatsLocked(fd, pw, args, opti, dumpAll, dumpPackage);
                    pw.println();
                    if (dumpAll) {
                        pw.println(TICK);
                    }
                }
                this.mCpHelper.dumpProvidersLocked(fd, pw, args, opti, dumpAll, dumpPackage);
                pw.println();
                if (dumpAll) {
                    pw.println(TICK);
                }
                dumpPermissions(fd, pw, args, opti, dumpAll, dumpPackage);
                pw.println();
                com.android.server.am.ActiveServices.ServiceDumper sdumper = this.mServices.newServiceDumperLocked(fd, pw, args, opti, dumpAll, dumpPackage);
                if (!dumpClient) {
                    if (dumpAll) {
                        pw.println(TICK);
                    }
                    sdumper.dumpLocked();
                }
                resetPriorityAfterLockedSection();
                pw.println(TICK);
                com.android.server.utils.AnrTimer.dump(pw, false);
                if (dumpClient) {
                    if (dumpAll) {
                        pw.println(TICK);
                    }
                    sdumper.dumpWithClient();
                }
                if (dumpPackage == null && dumpProxies) {
                    pw.println();
                    if (dumpAll) {
                        pw.println(TICK);
                    }
                    this.mActivityManagerServiceExt.dumpBinderProxies(pw, 6000);
                }
                boostPriorityForLockedSection();
                synchronized (this) {
                    try {
                        pw.println();
                        if (dumpAll) {
                            pw.println(TICK);
                        }
                        try {
                            this.mAtmInternal.dump(com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_CMD, fd, pw, args, opti, dumpAll, dumpClient, dumpPackage, displayIdFilter);
                            pw.println();
                            if (dumpAll) {
                                pw.println(TICK);
                            }
                            this.mAtmInternal.dump(com.android.server.wm.ActivityTaskManagerService.DUMP_LASTANR_CMD, fd, pw, args, opti, dumpAll, dumpClient, dumpPackage, displayIdFilter);
                            pw.println();
                            if (dumpAll) {
                                pw.println(TICK);
                            }
                            this.mAtmInternal.dump(com.android.server.wm.ActivityTaskManagerService.DUMP_STARTER_CMD, fd, pw, args, opti, dumpAll, dumpClient, dumpPackage, displayIdFilter);
                            if (dumpPackage == null) {
                                pw.println();
                                if (dumpAll) {
                                    pw.println(TICK);
                                }
                                this.mAtmInternal.dump(com.android.server.wm.ActivityTaskManagerService.DUMP_CONTAINERS_CMD, fd, pw, args, opti, dumpAll, dumpClient, dumpPackage, displayIdFilter);
                            }
                            if (!dumpNormalPriority) {
                                pw.println();
                                if (dumpAll) {
                                    pw.println(TICK);
                                }
                                this.mAtmInternal.dump(com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_CMD, fd, pw, args, opti, dumpAll, dumpClient, dumpPackage, displayIdFilter);
                            }
                            if (this.mAssociations.size() <= 0) {
                                str = dumpPackage;
                                printWriter = pw;
                            } else {
                                pw.println();
                                if (dumpAll) {
                                    pw.println(TICK);
                                }
                                str = dumpPackage;
                                printWriter = pw;
                                try {
                                    dumpAssociationsLocked(fd, pw, args, opti, dumpAll, dumpClient, dumpPackage);
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    resetPriorityAfterLockedSection();
                                    throw th;
                                }
                            }
                            pw.println();
                            if (dumpAll) {
                                printWriter.println(TICK);
                                this.mProcessList.getAppStartInfoTracker().dumpHistoryProcessStartInfo(printWriter, str);
                                printWriter.println(TICK);
                                this.mProcessList.mAppExitInfoTracker.dumpHistoryProcessExitInfo(printWriter, str);
                            }
                            if (str == null) {
                                pw.println();
                                if (dumpAll) {
                                    printWriter.println(TICK);
                                }
                                dumpLmkLocked(printWriter);
                            }
                            pw.println();
                            if (dumpAll) {
                                printWriter.println(TICK);
                            }
                            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mProcLock;
                            boostPriorityForProcLockedSection();
                            synchronized (activityManagerGlobalLock2) {
                                try {
                                    this.mProcessList.dumpProcessesLSP(fd, pw, args, opti, dumpAll, dumpPackage, dumpAppId);
                                } finally {
                                    resetPriorityAfterProcLockedSection();
                                }
                            }
                            resetPriorityAfterProcLockedSection();
                            pw.println();
                            if (dumpAll) {
                                printWriter.println(TICK);
                            }
                            dumpUsers(printWriter);
                            pw.println();
                            if (dumpAll) {
                                printWriter.println(TICK);
                            }
                            this.mComponentAliasResolver.dump(printWriter);
                            resetPriorityAfterLockedSection();
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                    }
                }
            } catch (java.lang.Throwable th7) {
                th = th7;
            }
        }
    }

    private void dumpAppRestrictionController(java.io.PrintWriter pw) {
        pw.println(TICK);
        this.mAppRestrictionController.dump(pw, "");
    }

    void dumpAppRestrictionController(android.util.proto.ProtoOutputStream proto, int uid) {
        this.mAppRestrictionController.dumpAsProto(proto, uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:529:0x09ff  */
    /* JADX WARN: Removed duplicated region for block: B:531:0x0a03  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01a0  */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void doDump(java.io.FileDescriptor r33, java.io.PrintWriter r34, java.lang.String[] r35, boolean r36) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2677
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.doDump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[], boolean):void");
    }

    void dumpAssociationsLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, boolean dumpClient, java.lang.String dumpPackage) {
        android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association> sourceProcesses;
        int dumpUid;
        int N1;
        android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>> targetComponents;
        com.android.server.am.ActivityManagerService activityManagerService = this;
        java.lang.String str = dumpPackage;
        pw.println("ACTIVITY MANAGER ASSOCIATIONS (dumpsys activity associations)");
        int dumpUid2 = 0;
        int i = 0;
        if (str != null) {
            android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
            try {
                dumpUid2 = pm.getPackageUid(str, 4194304L, 0);
            } catch (android.os.RemoteException e) {
            }
        }
        boolean printedAnything = false;
        long now = android.os.SystemClock.uptimeMillis();
        int i1 = 0;
        int N12 = activityManagerService.mAssociations.size();
        while (i1 < N12) {
            android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>> targetComponents2 = activityManagerService.mAssociations.valueAt(i1);
            int i2 = 0;
            int N2 = targetComponents2.size();
            while (i2 < N2) {
                android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>> sourceUids = targetComponents2.valueAt(i2);
                int i3 = 0;
                int N3 = sourceUids.size();
                while (i3 < N3) {
                    android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association> sourceProcesses2 = sourceUids.valueAt(i3);
                    boolean printedAnything2 = printedAnything;
                    int N4 = sourceProcesses2.size();
                    int i4 = 0;
                    while (i4 < N4) {
                        int N42 = N4;
                        com.android.server.am.ActivityManagerService.Association ass = sourceProcesses2.valueAt(i4);
                        if (str == null) {
                            sourceProcesses = sourceProcesses2;
                        } else {
                            sourceProcesses = sourceProcesses2;
                            if (!ass.mTargetComponent.getPackageName().equals(str) && android.os.UserHandle.getAppId(ass.mSourceUid) != dumpUid2) {
                                dumpUid = dumpUid2;
                                N1 = N12;
                                targetComponents = targetComponents2;
                            }
                            i4++;
                            str = dumpPackage;
                            sourceProcesses2 = sourceProcesses;
                            N4 = N42;
                            dumpUid2 = dumpUid;
                            N12 = N1;
                            targetComponents2 = targetComponents;
                        }
                        printedAnything2 = true;
                        pw.print("  ");
                        pw.print(ass.mTargetProcess);
                        pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                        dumpUid = dumpUid2;
                        android.os.UserHandle.formatUid(pw, ass.mTargetUid);
                        pw.print(" <- ");
                        pw.print(ass.mSourceProcess);
                        pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                        android.os.UserHandle.formatUid(pw, ass.mSourceUid);
                        pw.println();
                        pw.print("    via ");
                        pw.print(ass.mTargetComponent.flattenToShortString());
                        pw.println();
                        pw.print("    ");
                        long dur = ass.mTime;
                        N1 = N12;
                        int N13 = ass.mNesting;
                        if (N13 <= 0) {
                            targetComponents = targetComponents2;
                        } else {
                            targetComponents = targetComponents2;
                            dur += now - ass.mStartTime;
                        }
                        android.util.TimeUtils.formatDuration(dur, pw);
                        pw.print(" (");
                        pw.print(ass.mCount);
                        pw.print(" times)");
                        pw.print("  ");
                        int i5 = 0;
                        while (i5 < ass.mStateTimes.length) {
                            long amt = ass.mStateTimes[i5];
                            long dur2 = dur;
                            if (ass.mLastState - 0 == i5) {
                                amt += now - ass.mLastStateUptime;
                            }
                            if (amt != 0) {
                                pw.print(" ");
                                pw.print(com.android.server.am.ProcessList.makeProcStateString(i5 + 0));
                                pw.print("=");
                                android.util.TimeUtils.formatDuration(amt, pw);
                                if (ass.mLastState - 0 == i5) {
                                    pw.print(com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER);
                                }
                            }
                            i5++;
                            dur = dur2;
                        }
                        pw.println();
                        if (ass.mNesting > 0) {
                            pw.print("    Currently active: ");
                            android.util.TimeUtils.formatDuration(now - ass.mStartTime, pw);
                            pw.println();
                        }
                        i4++;
                        str = dumpPackage;
                        sourceProcesses2 = sourceProcesses;
                        N4 = N42;
                        dumpUid2 = dumpUid;
                        N12 = N1;
                        targetComponents2 = targetComponents;
                    }
                    i3++;
                    str = dumpPackage;
                    i = 0;
                    printedAnything = printedAnything2;
                    dumpUid2 = dumpUid2;
                }
                i2++;
                str = dumpPackage;
                dumpUid2 = dumpUid2;
            }
            i1++;
            activityManagerService = this;
            str = dumpPackage;
            dumpUid2 = dumpUid2;
        }
        if (!printedAnything) {
            pw.println("  (nothing)");
        }
    }

    int getAppId(java.lang.String dumpPackage) {
        if (dumpPackage != null) {
            try {
                android.content.pm.ApplicationInfo info = this.mContext.getPackageManager().getApplicationInfo(dumpPackage, 0);
                return android.os.UserHandle.getAppId(info.uid);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    void dumpBinderProxyInterfaceCounts(java.io.PrintWriter pw, java.lang.String header) {
        android.os.BinderProxy.InterfaceCount[] proxyCounts = android.os.BinderProxy.getSortedInterfaceCounts(50);
        pw.println(header);
        for (int i = 0; i < proxyCounts.length; i++) {
            pw.println("    #" + (i + 1) + ": " + proxyCounts[i]);
        }
    }

    boolean dumpBinderProxiesCounts(java.io.PrintWriter pw, java.lang.String header) {
        android.util.SparseIntArray counts = com.android.internal.os.BinderInternal.nGetBinderProxyPerUidCounts();
        if (counts != null) {
            pw.println(header);
            for (int i = 0; i < counts.size(); i++) {
                int uid = counts.keyAt(i);
                int binderCount = counts.valueAt(i);
                pw.print("    UID ");
                pw.print(uid);
                pw.print(", binder count = ");
                pw.print(binderCount);
                pw.print(", package(s)= ");
                java.lang.String[] pkgNames = this.mContext.getPackageManager().getPackagesForUid(uid);
                if (pkgNames != null) {
                    for (java.lang.String str : pkgNames) {
                        pw.print(str);
                        pw.print("; ");
                    }
                } else {
                    pw.print("NO PACKAGE NAME FOUND");
                }
                pw.println();
            }
            return true;
        }
        return false;
    }

    void dumpBinderProxies(java.io.PrintWriter pw, int minCountToDumpInterfaces) {
        pw.println("ACTIVITY MANAGER BINDER PROXY STATE (dumpsys activity binder-proxies)");
        int proxyCount = android.os.BinderProxy.getProxyCount();
        if (proxyCount >= minCountToDumpInterfaces) {
            dumpBinderProxyInterfaceCounts(pw, "Top proxy interface names held by SYSTEM");
        } else {
            pw.print("Not dumping proxy interface counts because size (" + java.lang.Integer.toString(proxyCount) + ") looks reasonable");
            pw.println();
        }
        dumpBinderProxiesCounts(pw, "  Counts of Binder Proxies held by SYSTEM");
    }

    boolean dumpActiveInstruments(java.io.PrintWriter pw, java.lang.String dumpPackage, boolean needSep) {
        int size = this.mActiveInstrumentation.size();
        if (size > 0) {
            boolean printed = false;
            for (int i = 0; i < size; i++) {
                com.android.server.am.ActiveInstrumentation ai = this.mActiveInstrumentation.get(i);
                if (dumpPackage == null || ai.mClass.getPackageName().equals(dumpPackage) || ai.mTargetInfo.packageName.equals(dumpPackage)) {
                    if (!printed) {
                        if (needSep) {
                            pw.println();
                        }
                        pw.println("  Active instrumentation:");
                        printed = true;
                        needSep = true;
                    }
                    pw.print("    Instrumentation #");
                    pw.print(i);
                    pw.print(": ");
                    pw.println(ai);
                    ai.dump(pw, "      ");
                }
            }
        }
        return needSep;
    }

    @dalvik.annotation.optimization.NeverCompile
    void dumpOtherProcessesInfoLSP(java.io.FileDescriptor fd, final java.io.PrintWriter pw, boolean dumpAll, java.lang.String dumpPackage, int dumpAppId, int numPers, boolean needSep) throws java.lang.Throwable {
        boolean needSep2;
        java.lang.String str;
        java.io.PrintWriter printWriter;
        boolean needSep3;
        boolean needSep4;
        if (dumpAll || dumpPackage != null) {
            android.util.SparseArray<com.android.server.am.ProcessRecord> pidToProcess = new android.util.SparseArray<>();
            synchronized (this.mPidsSelfLocked) {
                boolean printed = false;
                try {
                    int size = this.mPidsSelfLocked.size();
                    needSep2 = needSep;
                    for (int i = 0; i < size; i++) {
                        try {
                            com.android.server.am.ProcessRecord r = this.mPidsSelfLocked.valueAt(i);
                            pidToProcess.put(r.getPid(), r);
                            if (dumpPackage == null || r.getPkgList().containsKey(dumpPackage)) {
                                if (!printed) {
                                    if (needSep2) {
                                        pw.println();
                                    }
                                    needSep2 = true;
                                    pw.println("  PID mappings:");
                                    printed = true;
                                }
                                pw.print("    PID #");
                                pw.print(this.mPidsSelfLocked.keyAt(i));
                                pw.print(": ");
                                pw.println(this.mPidsSelfLocked.valueAt(i));
                            }
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
                    synchronized (sActiveProcessInfoSelfLocked) {
                        boolean printed2 = false;
                        try {
                            int size2 = sActiveProcessInfoSelfLocked.size();
                            for (int i2 = 0; i2 < size2; i2++) {
                                android.content.pm.ProcessInfo info = sActiveProcessInfoSelfLocked.valueAt(i2);
                                com.android.server.am.ProcessRecord r2 = pidToProcess.get(sActiveProcessInfoSelfLocked.keyAt(i2));
                                if (r2 == null || dumpPackage == null || r2.getPkgList().containsKey(dumpPackage)) {
                                    if (!printed2) {
                                        if (needSep2) {
                                            pw.println();
                                        }
                                        needSep2 = true;
                                        pw.println("  Active process infos:");
                                        printed2 = true;
                                    }
                                    pw.print("    Pinfo PID #");
                                    pw.print(sActiveProcessInfoSelfLocked.keyAt(i2));
                                    pw.println(":");
                                    pw.print("      name=");
                                    pw.println(info.name);
                                    if (info.deniedPermissions != null) {
                                        for (int j = 0; j < info.deniedPermissions.size(); j++) {
                                            pw.print("      deny: ");
                                            pw.println((java.lang.String) info.deniedPermissions.valueAt(j));
                                        }
                                    }
                                }
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            while (true) {
                                try {
                                    throw th;
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                }
                            }
                        }
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            }
        } else {
            needSep2 = needSep;
        }
        if (dumpAll) {
            this.mPhantomProcessList.dump(pw, "  ");
        }
        if (this.mImportantProcesses.size() > 0) {
            synchronized (this.mPidsSelfLocked) {
                boolean printed3 = false;
                int size3 = this.mImportantProcesses.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    com.android.server.am.ProcessRecord r3 = this.mPidsSelfLocked.get(this.mImportantProcesses.valueAt(i3).pid);
                    if (dumpPackage == null || (r3 != null && r3.getPkgList().containsKey(dumpPackage))) {
                        if (!printed3) {
                            if (needSep2) {
                                pw.println();
                            }
                            needSep2 = true;
                            pw.println("  Foreground Processes:");
                            printed3 = true;
                        }
                        pw.print("    PID #");
                        pw.print(this.mImportantProcesses.keyAt(i3));
                        pw.print(": ");
                        pw.println(this.mImportantProcesses.valueAt(i3));
                    }
                }
            }
        }
        if (this.mPersistentStartingProcesses.size() > 0) {
            if (needSep2) {
                pw.println();
            }
            pw.println("  Persisent processes that are starting:");
            dumpProcessList(pw, this, this.mPersistentStartingProcesses, "    ", "Starting Norm", "Restarting PERS", dumpPackage);
            needSep2 = true;
        }
        if (this.mProcessList.mRemovedProcesses.size() > 0) {
            if (needSep2) {
                pw.println();
            }
            pw.println("  Processes that are being removed:");
            dumpProcessList(pw, this, this.mProcessList.mRemovedProcesses, "    ", "Removed Norm", "Removed PERS", dumpPackage);
            needSep2 = true;
        }
        if (this.mProcessesOnHold.size() > 0) {
            if (needSep2) {
                pw.println();
            }
            pw.println("  Processes that are on old until the system is ready:");
            dumpProcessList(pw, this, this.mProcessesOnHold, "    ", "OnHold Norm", "OnHold PERS", dumpPackage);
            needSep2 = true;
        }
        boolean needSep5 = this.mAtmInternal.dumpForProcesses(fd, pw, dumpAll, dumpPackage, dumpAppId, this.mAppErrors.dumpLPr(fd, pw, needSep2, dumpPackage), this.mAppProfiler.getTestPssMode(), this.mWakefulness.get());
        if (dumpAll && this.mProcessList.mPendingStarts.size() > 0) {
            if (needSep5) {
                pw.println();
            }
            needSep5 = true;
            pw.println("  mPendingStarts: ");
            int len = this.mProcessList.mPendingStarts.size();
            for (int i4 = 0; i4 < len; i4++) {
                pw.println("    " + this.mProcessList.mPendingStarts.keyAt(i4) + ": " + this.mProcessList.mPendingStarts.valueAt(i4));
            }
        }
        if (!dumpAll) {
            str = dumpPackage;
            printWriter = pw;
        } else {
            this.mUidObserverController.dump(pw, dumpPackage);
            pw.println("  mDeviceIdleAllowlist=" + java.util.Arrays.toString(this.mDeviceIdleAllowlist));
            pw.println("  mDeviceIdleExceptIdleAllowlist=" + java.util.Arrays.toString(this.mDeviceIdleExceptIdleAllowlist));
            pw.println("  mDeviceIdleTempAllowlist=" + java.util.Arrays.toString(this.mDeviceIdleTempAllowlist));
            if (this.mPendingTempAllowlist.size() > 0) {
                pw.println("  mPendingTempAllowlist:");
                int size4 = this.mPendingTempAllowlist.size();
                for (int i5 = 0; i5 < size4; i5++) {
                    com.android.server.am.ActivityManagerService.PendingTempAllowlist ptw = this.mPendingTempAllowlist.valueAt(i5);
                    pw.print("    ");
                    android.os.UserHandle.formatUid(pw, ptw.targetUid);
                    pw.print(": ");
                    android.util.TimeUtils.formatDuration(ptw.duration, pw);
                    pw.print(" ");
                    pw.println(ptw.tag);
                    pw.print(" ");
                    pw.print(ptw.type);
                    pw.print(" ");
                    pw.print(ptw.reasonCode);
                    pw.print(" ");
                    pw.print(ptw.callingUid);
                }
            }
            pw.println("  mFgsStartTempAllowList:");
            final long currentTimeNow = java.lang.System.currentTimeMillis();
            final long elapsedRealtimeNow = android.os.SystemClock.elapsedRealtime();
            str = dumpPackage;
            printWriter = pw;
            this.mFgsStartTempAllowList.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda18
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.am.ActivityManagerService.lambda$dumpOtherProcessesInfoLSP$15(pw, currentTimeNow, elapsedRealtimeNow, (java.lang.Integer) obj, (android.util.Pair) obj2);
                }
            });
            if (!this.mProcessList.mAppsInBackgroundRestricted.isEmpty()) {
                printWriter.println("  Processes that are in background restricted:");
                int size5 = this.mProcessList.mAppsInBackgroundRestricted.size();
                for (int i6 = 0; i6 < size5; i6++) {
                    printWriter.println(java.lang.String.format("%s #%2d: %s", "    ", java.lang.Integer.valueOf(i6), this.mProcessList.mAppsInBackgroundRestricted.valueAt(i6).toString()));
                }
            }
        }
        if ((this.mDebugApp != null || this.mOrigDebugApp != null || this.mDebugTransient || this.mOrigWaitForDebugger) && (str == null || str.equals(this.mDebugApp) || str.equals(this.mOrigDebugApp))) {
            if (needSep5) {
                pw.println();
                needSep5 = false;
            }
            printWriter.println("  mDebugApp=" + this.mDebugApp + "/orig=" + this.mOrigDebugApp + " mDebugTransient=" + this.mDebugTransient + " mOrigWaitForDebugger=" + this.mOrigWaitForDebugger);
            needSep3 = needSep5;
        } else {
            needSep3 = needSep5;
        }
        synchronized (this.mAppProfiler.mProfilerLock) {
            try {
                needSep4 = this.mAppProfiler.dumpMemWatchProcessesLPf(printWriter, needSep3);
            } catch (java.lang.Throwable th6) {
                th = th6;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th7) {
                        th = th7;
                    }
                }
            }
        }
        if (this.mTrackAllocationApp != null && (str == null || str.equals(this.mTrackAllocationApp))) {
            if (needSep4) {
                pw.println();
                needSep4 = false;
            }
            printWriter.println("  mTrackAllocationApp=" + this.mTrackAllocationApp);
        }
        boolean needSep6 = this.mAppProfiler.dumpProfileDataLocked(printWriter, str, needSep4);
        if (this.mNativeDebuggingApp != null && (str == null || str.equals(this.mNativeDebuggingApp))) {
            if (needSep6) {
                pw.println();
            }
            printWriter.println("  mNativeDebuggingApp=" + this.mNativeDebuggingApp);
        }
        if (str == null) {
            if (this.mAlwaysFinishActivities) {
                printWriter.println("  mAlwaysFinishActivities=" + this.mAlwaysFinishActivities);
            }
            if (this.mAllowSpecifiedFifoScheduling) {
                printWriter.println("  mAllowSpecifiedFifoScheduling=true");
            }
            if (dumpAll) {
                printWriter.println("  Total persistent processes: " + numPers);
                printWriter.println("  mProcessesReady=" + this.mProcessesReady + " mSystemReady=" + this.mSystemReady + " mBooted=" + this.mBooted + " mFactoryTest=" + this.mFactoryTest);
                printWriter.println("  mBooting=" + this.mBooting + " mCallFinishBooting=" + this.mCallFinishBooting + " mBootAnimationComplete=" + this.mBootAnimationComplete);
                printWriter.print("  mLastPowerCheckUptime=");
                android.util.TimeUtils.formatDuration(this.mLastPowerCheckUptime, printWriter);
                printWriter.println("");
                this.mOomAdjuster.dumpSequenceNumbersLocked(printWriter);
                this.mOomAdjuster.dumpProcCountsLocked(printWriter);
                this.mAppProfiler.dumpMemoryLevelsLocked(printWriter);
                long now = android.os.SystemClock.uptimeMillis();
                printWriter.print("  mLastIdleTime=");
                android.util.TimeUtils.formatDuration(now, this.mLastIdleTime, printWriter);
                printWriter.print(" mLowRamSinceLastIdle=");
                android.util.TimeUtils.formatDuration(this.mAppProfiler.getLowRamTimeSinceIdleLPr(now), printWriter);
                pw.println();
                pw.println();
                printWriter.println("  ServiceManager statistics:");
                android.os.ServiceManager.sStatLogger.dump(printWriter, "    ");
                pw.println();
            }
        }
        printWriter.println("  mForceBackgroundCheck=" + this.mForceBackgroundCheck);
    }

    static /* synthetic */ void lambda$dumpOtherProcessesInfoLSP$15(java.io.PrintWriter pw, long currentTimeNow, long elapsedRealtimeNow, java.lang.Integer uid, android.util.Pair entry) {
        pw.print("    " + android.os.UserHandle.formatUid(uid.intValue()) + ": ");
        ((com.android.server.am.ActivityManagerService.FgsTempAllowListItem) entry.second).dump(pw);
        pw.print(" expiration=");
        long expirationInCurrentTime = (currentTimeNow - elapsedRealtimeNow) + ((java.lang.Long) entry.first).longValue();
        android.util.TimeUtils.dumpTimeWithDelta(pw, expirationInCurrentTime, currentTimeNow);
        pw.println();
    }

    private void dumpUsers(java.io.PrintWriter pw) {
        pw.println("ACTIVITY MANAGER USERS (dumpsys activity users)");
        this.mUserController.dump(pw);
    }

    void writeOtherProcessesInfoToProtoLSP(android.util.proto.ProtoOutputStream proto, java.lang.String dumpPackage, int dumpAppId, int numPers) {
        int size = this.mActiveInstrumentation.size();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ActiveInstrumentation ai = this.mActiveInstrumentation.get(i);
            if (dumpPackage == null || ai.mClass.getPackageName().equals(dumpPackage) || ai.mTargetInfo.packageName.equals(dumpPackage)) {
                ai.dumpDebug(proto, 2246267895811L);
            }
        }
        this.mUidObserverController.dumpValidateUidsProto(proto, dumpPackage, dumpAppId, 2246267895813L);
        if (dumpPackage != null) {
            synchronized (this.mPidsSelfLocked) {
                int size2 = this.mPidsSelfLocked.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    com.android.server.am.ProcessRecord r = this.mPidsSelfLocked.valueAt(i2);
                    if (r.getPkgList().containsKey(dumpPackage)) {
                        r.dumpDebug(proto, 2246267895815L);
                    }
                }
            }
        }
        if (this.mImportantProcesses.size() > 0) {
            synchronized (this.mPidsSelfLocked) {
                int size3 = this.mImportantProcesses.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    com.android.server.am.ActivityManagerService.ImportanceToken it = this.mImportantProcesses.valueAt(i3);
                    com.android.server.am.ProcessRecord r2 = this.mPidsSelfLocked.get(it.pid);
                    if (dumpPackage == null || (r2 != null && r2.getPkgList().containsKey(dumpPackage))) {
                        it.dumpDebug(proto, 2246267895816L);
                    }
                }
            }
        }
        int size4 = this.mPersistentStartingProcesses.size();
        for (int i4 = 0; i4 < size4; i4++) {
            com.android.server.am.ProcessRecord r3 = this.mPersistentStartingProcesses.get(i4);
            if (dumpPackage == null || dumpPackage.equals(r3.info.packageName)) {
                r3.dumpDebug(proto, 2246267895817L);
            }
        }
        int size5 = this.mProcessList.mRemovedProcesses.size();
        for (int i5 = 0; i5 < size5; i5++) {
            com.android.server.am.ProcessRecord r4 = this.mProcessList.mRemovedProcesses.get(i5);
            if (dumpPackage == null || dumpPackage.equals(r4.info.packageName)) {
                r4.dumpDebug(proto, 2246267895818L);
            }
        }
        int size6 = this.mProcessesOnHold.size();
        for (int i6 = 0; i6 < size6; i6++) {
            com.android.server.am.ProcessRecord r5 = this.mProcessesOnHold.get(i6);
            if (dumpPackage == null || dumpPackage.equals(r5.info.packageName)) {
                r5.dumpDebug(proto, 2246267895819L);
            }
        }
        synchronized (this.mAppProfiler.mProfilerLock) {
            this.mAppProfiler.writeProcessesToGcToProto(proto, 2246267895820L, dumpPackage);
        }
        this.mAppErrors.dumpDebugLPr(proto, 1146756268045L, dumpPackage);
        this.mAtmInternal.writeProcessesToProto(proto, dumpPackage, this.mWakefulness.get(), this.mAppProfiler.getTestPssMode());
        if (dumpPackage == null) {
            this.mUserController.dumpDebug(proto, 1146756268046L);
        }
        this.mUidObserverController.dumpDebug(proto, dumpPackage);
        for (int v : this.mDeviceIdleAllowlist) {
            proto.write(2220498092056L, v);
        }
        for (int v2 : this.mDeviceIdleTempAllowlist) {
            proto.write(2220498092057L, v2);
        }
        if (this.mPendingTempAllowlist.size() > 0) {
            int size7 = this.mPendingTempAllowlist.size();
            for (int i7 = 0; i7 < size7; i7++) {
                this.mPendingTempAllowlist.valueAt(i7).dumpDebug(proto, 2246267895834L);
            }
        }
        if ((this.mDebugApp != null || this.mOrigDebugApp != null || this.mDebugTransient || this.mOrigWaitForDebugger) && (dumpPackage == null || dumpPackage.equals(this.mDebugApp) || dumpPackage.equals(this.mOrigDebugApp))) {
            long debugAppToken = proto.start(1146756268062L);
            proto.write(1138166333441L, this.mDebugApp);
            proto.write(1138166333442L, this.mOrigDebugApp);
            proto.write(1133871366147L, this.mDebugTransient);
            proto.write(1133871366148L, this.mOrigWaitForDebugger);
            proto.end(debugAppToken);
        }
        synchronized (this.mAppProfiler.mProfilerLock) {
            this.mAppProfiler.writeMemWatchProcessToProtoLPf(proto);
        }
        if (this.mTrackAllocationApp != null && (dumpPackage == null || dumpPackage.equals(this.mTrackAllocationApp))) {
            proto.write(1138166333473L, this.mTrackAllocationApp);
        }
        this.mAppProfiler.writeProfileDataToProtoLocked(proto, dumpPackage);
        if (dumpPackage == null || dumpPackage.equals(this.mNativeDebuggingApp)) {
            proto.write(1138166333475L, this.mNativeDebuggingApp);
        }
        if (dumpPackage == null) {
            proto.write(1133871366180L, this.mAlwaysFinishActivities);
            proto.write(1120986464294L, numPers);
            proto.write(1133871366183L, this.mProcessesReady);
            proto.write(1133871366184L, this.mSystemReady);
            proto.write(1133871366185L, this.mBooted);
            proto.write(1120986464298L, this.mFactoryTest);
            proto.write(1133871366187L, this.mBooting);
            proto.write(1133871366188L, this.mCallFinishBooting);
            proto.write(1133871366189L, this.mBootAnimationComplete);
            proto.write(1112396529710L, this.mLastPowerCheckUptime);
            this.mOomAdjuster.dumpProcessListVariablesLocked(proto);
            this.mAppProfiler.writeMemoryLevelsToProtoLocked(proto);
            long now = android.os.SystemClock.uptimeMillis();
            android.util.proto.ProtoUtils.toDuration(proto, 1146756268090L, this.mLastIdleTime, now);
            proto.write(1112396529723L, this.mAppProfiler.getLowRamTimeSinceIdleLPr(now));
        }
    }

    private boolean reportLmkKillAtOrBelow(java.io.PrintWriter pw, int oom_adj) {
        java.lang.Integer cnt = com.android.server.am.ProcessList.getLmkdKillCount(0, oom_adj);
        if (cnt == null) {
            return false;
        }
        pw.println("    kills at or below oom_adj " + oom_adj + ": " + cnt);
        return true;
    }

    boolean dumpLmkLocked(java.io.PrintWriter pw) {
        pw.println("ACTIVITY MANAGER LMK KILLS (dumpsys activity lmk)");
        java.lang.Integer cnt = com.android.server.am.ProcessList.getLmkdKillCount(1001, 1001);
        if (cnt == null) {
            return false;
        }
        pw.println("  Total number of kills: " + cnt);
        return reportLmkKillAtOrBelow(pw, 999) && reportLmkKillAtOrBelow(pw, 900) && reportLmkKillAtOrBelow(pw, 800) && reportLmkKillAtOrBelow(pw, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ) && reportLmkKillAtOrBelow(pw, 600) && reportLmkKillAtOrBelow(pw, 500) && reportLmkKillAtOrBelow(pw, 400) && reportLmkKillAtOrBelow(pw, 300) && reportLmkKillAtOrBelow(pw, 250) && reportLmkKillAtOrBelow(pw, 200) && reportLmkKillAtOrBelow(pw, 100) && reportLmkKillAtOrBelow(pw, 0);
    }

    public static class ItemMatcher {
        boolean all = true;
        java.util.ArrayList<android.content.ComponentName> components;
        java.util.ArrayList<java.lang.Integer> objects;
        java.util.ArrayList<java.lang.String> strings;

        public void build(java.lang.String name) {
            android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(name);
            if (componentName != null) {
                if (this.components == null) {
                    this.components = new java.util.ArrayList<>();
                }
                this.components.add(componentName);
                this.all = false;
                return;
            }
            try {
                int objectId = java.lang.Integer.parseInt(name, 16);
                if (this.objects == null) {
                    this.objects = new java.util.ArrayList<>();
                }
                this.objects.add(java.lang.Integer.valueOf(objectId));
                this.all = false;
            } catch (java.lang.RuntimeException e) {
                if (this.strings == null) {
                    this.strings = new java.util.ArrayList<>();
                }
                this.strings.add(name);
                this.all = false;
            }
        }

        public int build(java.lang.String[] args, int opti) {
            while (opti < args.length) {
                java.lang.String name = args[opti];
                if ("--".equals(name)) {
                    return opti + 1;
                }
                build(name);
                opti++;
            }
            return opti;
        }

        public boolean match(java.lang.Object object, android.content.ComponentName comp) {
            if (this.all) {
                return true;
            }
            if (this.components != null) {
                for (int i = 0; i < this.components.size(); i++) {
                    if (this.components.get(i).equals(comp)) {
                        return true;
                    }
                }
            }
            if (this.objects != null) {
                for (int i2 = 0; i2 < this.objects.size(); i2++) {
                    if (java.lang.System.identityHashCode(object) == this.objects.get(i2).intValue()) {
                        return true;
                    }
                }
            }
            if (this.strings != null) {
                java.lang.String flat = comp.flattenToString();
                for (int i3 = 0; i3 < this.strings.size(); i3++) {
                    if (flat.contains(this.strings.get(i3))) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
    }

    void writeBroadcastsToProtoLocked(android.util.proto.ProtoOutputStream proto) {
        if (this.mRegisteredReceivers.size() > 0) {
            for (com.android.server.am.ReceiverList r : this.mRegisteredReceivers.values()) {
                r.dumpDebug(proto, 2246267895809L);
            }
        }
        this.mReceiverResolver.dumpDebug(proto, 1146756268034L);
        this.mBroadcastQueue.dumpDebug(proto, 2246267895811L);
        synchronized (this.mStickyBroadcasts) {
            int user = 0;
            while (true) {
                long token = 1138166333441L;
                if (user < this.mStickyBroadcasts.size()) {
                    long token2 = proto.start(2246267895812L);
                    proto.write(1120986464257L, this.mStickyBroadcasts.keyAt(user));
                    for (java.util.Map.Entry<java.lang.String, java.util.ArrayList<com.android.server.am.ActivityManagerService.StickyBroadcast>> ent : this.mStickyBroadcasts.valueAt(user).entrySet()) {
                        long actionToken = proto.start(2246267895810L);
                        proto.write(token, ent.getKey());
                        for (com.android.server.am.ActivityManagerService.StickyBroadcast broadcast : ent.getValue()) {
                            broadcast.intent.dumpDebug(proto, 2246267895810L, false, true, true, false);
                            actionToken = actionToken;
                            token2 = token2;
                        }
                        proto.end(actionToken);
                        token2 = token2;
                        token = 1138166333441L;
                    }
                    proto.end(token2);
                    user++;
                }
            }
        }
        long handlerToken = proto.start(1146756268037L);
        proto.write(1138166333441L, this.mHandler.toString());
        this.mHandler.getLooper().dumpDebug(proto, 1146756268034L);
        proto.end(handlerToken);
    }

    void dumpAllowedAssociationsLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage) {
        pw.println("ACTIVITY MANAGER ALLOWED ASSOCIATION STATE (dumpsys activity allowed-associations)");
        boolean printed = false;
        if (this.mAllowedAssociations != null) {
            for (int i = 0; i < this.mAllowedAssociations.size(); i++) {
                java.lang.String pkg = this.mAllowedAssociations.keyAt(i);
                android.util.ArraySet<java.lang.String> asc = this.mAllowedAssociations.valueAt(i).getAllowedPackageAssociations();
                if (!printed) {
                    pw.println("  Allowed associations (by restricted package):");
                    printed = true;
                }
                pw.print("  * ");
                pw.print(pkg);
                pw.println(":");
                for (int j = 0; j < asc.size(); j++) {
                    if (dumpPackage == null || pkg.equals(dumpPackage) || asc.valueAt(j).equals(dumpPackage)) {
                        pw.print("      Allow: ");
                        pw.println(asc.valueAt(j));
                    }
                }
                if (this.mAllowedAssociations.valueAt(i).isDebuggable()) {
                    pw.println("      (debuggable)");
                }
            }
        }
        if (!printed) {
            pw.println("  (No association restrictions)");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x015e  */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void dumpBroadcastsLocked(java.io.FileDescriptor r29, java.io.PrintWriter r30, java.lang.String[] r31, int r32, boolean r33, java.lang.String r34) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 752
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.dumpBroadcastsLocked(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[], int, boolean, java.lang.String):void");
    }

    @dalvik.annotation.optimization.NeverCompile
    void dumpBroadcastStatsLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage) {
        if (this.mCurBroadcastStats == null) {
            return;
        }
        pw.println("ACTIVITY MANAGER BROADCAST STATS STATE (dumpsys activity broadcast-stats)");
        long now = android.os.SystemClock.elapsedRealtime();
        if (this.mLastBroadcastStats != null) {
            pw.print("  Last stats (from ");
            android.util.TimeUtils.formatDuration(this.mLastBroadcastStats.mStartRealtime, now, pw);
            pw.print(" to ");
            android.util.TimeUtils.formatDuration(this.mLastBroadcastStats.mEndRealtime, now, pw);
            pw.print(", ");
            android.util.TimeUtils.formatDuration(this.mLastBroadcastStats.mEndUptime - this.mLastBroadcastStats.mStartUptime, pw);
            pw.println(" uptime):");
            if (!this.mLastBroadcastStats.dumpStats(pw, "    ", dumpPackage)) {
                pw.println("    (nothing)");
            }
            pw.println();
        }
        pw.print("  Current stats (from ");
        android.util.TimeUtils.formatDuration(this.mCurBroadcastStats.mStartRealtime, now, pw);
        pw.print(" to now, ");
        android.util.TimeUtils.formatDuration(android.os.SystemClock.uptimeMillis() - this.mCurBroadcastStats.mStartUptime, pw);
        pw.println(" uptime):");
        if (!this.mCurBroadcastStats.dumpStats(pw, "    ", dumpPackage)) {
            pw.println("    (nothing)");
        }
    }

    @dalvik.annotation.optimization.NeverCompile
    void dumpBroadcastStatsCheckinLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean fullCheckin, java.lang.String dumpPackage) {
        if (this.mCurBroadcastStats == null) {
            return;
        }
        if (this.mLastBroadcastStats != null) {
            this.mLastBroadcastStats.dumpCheckinStats(pw, dumpPackage);
            if (fullCheckin) {
                this.mLastBroadcastStats = null;
                return;
            }
        }
        this.mCurBroadcastStats.dumpCheckinStats(pw, dumpPackage);
        if (fullCheckin) {
            this.mCurBroadcastStats = null;
        }
    }

    void dumpPermissions(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage) {
        pw.println("ACTIVITY MANAGER URI PERMISSIONS (dumpsys activity permissions)");
        this.mUgmInternal.dump(pw, dumpAll, dumpPackage);
    }

    private static int dumpProcessList(java.io.PrintWriter pw, com.android.server.am.ActivityManagerService service, java.util.List list, java.lang.String prefix, java.lang.String normalLabel, java.lang.String persistentLabel, java.lang.String dumpPackage) {
        int numPers = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) list.get(i);
            if (dumpPackage == null || dumpPackage.equals(r.info.packageName)) {
                pw.println(java.lang.String.format("%s%s #%2d: %s", prefix, r.isPersistent() ? persistentLabel : normalLabel, java.lang.Integer.valueOf(i), r.toString()));
                if (r.isPersistent()) {
                    numPers++;
                }
            }
        }
        return numPers;
    }

    java.util.ArrayList<com.android.server.am.ProcessRecord> collectProcesses(java.io.PrintWriter pw, int start, boolean allPkgs, java.lang.String[] args) {
        java.util.ArrayList<com.android.server.am.ProcessRecord> arrayListCollectProcessesLOSP;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                arrayListCollectProcessesLOSP = this.mProcessList.collectProcessesLOSP(start, allPkgs, args);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return arrayListCollectProcessesLOSP;
    }

    final void dumpGraphicsHardwareUsage(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.util.ArrayList<com.android.server.am.ProcessRecord> procs = collectProcesses(pw, 0, false, args);
        if (procs == null) {
            pw.println("No process found for: " + args[0]);
            return;
        }
        long uptime = android.os.SystemClock.uptimeMillis();
        long realtime = android.os.SystemClock.elapsedRealtime();
        pw.println("Applications Graphics Acceleration Info:");
        pw.println("Uptime: " + uptime + " Realtime: " + realtime);
        for (int i = procs.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = procs.get(i);
            int pid = r.getPid();
            android.app.IApplicationThread thread = r.getThread();
            if (thread != null) {
                pw.println("\n** Graphics info for pid " + pid + " [" + r.processName + "] **");
                pw.flush();
                try {
                    com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                    try {
                        if (MY_PID == pid) {
                            thread.dumpGfxInfo(new android.os.ParcelFileDescriptor(fd), args);
                        } else {
                            thread.dumpGfxInfo(tp.getWriteFd(), args);
                            tp.go(fd);
                        }
                        tp.kill();
                    } catch (java.lang.Throwable th) {
                        tp.kill();
                        throw th;
                    }
                } catch (android.os.RemoteException e) {
                    pw.println("Got a RemoteException while dumping the app " + r);
                    pw.flush();
                } catch (java.io.IOException e2) {
                    pw.println("Failure while dumping the app: " + r);
                    pw.flush();
                }
            }
        }
    }

    final void dumpBinderCacheContents(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.util.ArrayList<com.android.server.am.ProcessRecord> procs = collectProcesses(pw, 0, false, args);
        if (procs == null) {
            pw.println("No process found for: " + args[0]);
            return;
        }
        pw.println("Per-process Binder Cache Contents");
        for (int i = procs.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = procs.get(i);
            int pid = r.getPid();
            android.app.IApplicationThread thread = r.getThread();
            if (thread != null) {
                pw.println("\n\n** Cache info for pid " + pid + " [" + r.processName + "] **");
                pw.flush();
                try {
                    if (pid == android.os.Process.myPid()) {
                        android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.fromFd(fd.getInt$());
                        try {
                            thread.dumpCacheInfo(pfd, args);
                            if (pfd != null) {
                                pfd.close();
                            }
                        } catch (java.lang.Throwable th) {
                            if (pfd != null) {
                                try {
                                    pfd.close();
                                } catch (java.lang.Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    } else {
                        com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                        try {
                            thread.dumpCacheInfo(tp.getWriteFd(), args);
                            tp.go(fd);
                            tp.kill();
                        } catch (java.lang.Throwable th3) {
                            tp.kill();
                            throw th3;
                        }
                    }
                } catch (android.os.RemoteException e) {
                    pw.println("Got a RemoteException while dumping the app " + r);
                    pw.flush();
                } catch (java.io.IOException e2) {
                    pw.println("Failure while dumping the app " + r);
                    pw.flush();
                }
            }
        }
    }

    final void dumpDbInfo(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.util.ArrayList<com.android.server.am.ProcessRecord> procs = collectProcesses(pw, 0, false, args);
        if (procs == null) {
            pw.println("No process found for: " + args[0]);
            return;
        }
        pw.println("Applications Database Info:");
        for (int i = procs.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord r = procs.get(i);
            int pid = r.getPid();
            android.app.IApplicationThread thread = r.getThread();
            if (thread != null) {
                pw.println("\n** Database info for pid " + pid + " [" + r.processName + "] **");
                pw.flush();
                try {
                    com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                    try {
                        thread.dumpDbInfo(tp.getWriteFd(), args);
                        tp.go(fd);
                        tp.kill();
                    } catch (java.lang.Throwable th) {
                        tp.kill();
                        throw th;
                    }
                } catch (android.os.RemoteException e) {
                    pw.println("Got a RemoteException while dumping the app " + r);
                    pw.flush();
                } catch (java.io.IOException e2) {
                    pw.println("Failure while dumping the app: " + r);
                    pw.flush();
                }
            }
        }
    }

    static final class MemItem {
        final boolean hasActivities;
        final int id;
        final boolean isProc;
        final java.lang.String label;
        final long mPrivateDirty;
        final long mRss;
        final long pss;
        final java.lang.String shortLabel;
        java.util.ArrayList<com.android.server.am.ActivityManagerService.MemItem> subitems;
        final long swapPss;
        final int userId;

        MemItem(java.lang.String label, java.lang.String shortLabel, long pss, long swapPss, long rss, long privateDirty, int id, int userId, boolean hasActivities) {
            this.isProc = true;
            this.label = label;
            this.shortLabel = shortLabel;
            this.pss = pss;
            this.swapPss = swapPss;
            this.mRss = rss;
            this.mPrivateDirty = privateDirty;
            this.id = id;
            this.userId = userId;
            this.hasActivities = hasActivities;
        }

        MemItem(java.lang.String label, java.lang.String shortLabel, long pss, long swapPss, long rss, long privateDirty, int id) {
            this.isProc = false;
            this.label = label;
            this.shortLabel = shortLabel;
            this.pss = pss;
            this.swapPss = swapPss;
            this.mRss = rss;
            this.mPrivateDirty = privateDirty;
            this.id = id;
            this.userId = 0;
            this.hasActivities = false;
        }
    }

    private static void sortMemItems(java.util.List<com.android.server.am.ActivityManagerService.MemItem> items, final boolean pss) {
        java.util.Collections.sort(items, new java.util.Comparator<com.android.server.am.ActivityManagerService.MemItem>() { // from class: com.android.server.am.ActivityManagerService.16
            @Override // java.util.Comparator
            public int compare(com.android.server.am.ActivityManagerService.MemItem lhs, com.android.server.am.ActivityManagerService.MemItem rhs) {
                long lss = pss ? lhs.pss : lhs.mRss;
                long rss = pss ? rhs.pss : rhs.mRss;
                if (lss < rss) {
                    return 1;
                }
                if (lss > rss) {
                    return -1;
                }
                return 0;
            }
        });
    }

    static final void dumpMemItems(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String tag, java.util.ArrayList<com.android.server.am.ActivityManagerService.MemItem> items, boolean sort, boolean isCompact, boolean dumpPss, boolean dumpSwapPss, boolean dumpPrivateDirty) {
        if (sort && !isCompact) {
            sortMemItems(items, dumpPss);
        }
        for (int i = 0; i < items.size(); i++) {
            com.android.server.am.ActivityManagerService.MemItem mi = items.get(i);
            if (isCompact) {
                if (mi.isProc) {
                    pw.print("proc,");
                    pw.print(tag);
                    pw.print(",");
                    pw.print(mi.shortLabel);
                    pw.print(",");
                    pw.print(mi.id);
                    pw.print(",");
                    pw.print(dumpPss ? mi.pss : mi.mRss);
                    pw.print(",");
                    pw.print(dumpSwapPss ? java.lang.Long.valueOf(mi.swapPss) : "N/A");
                    pw.println(mi.hasActivities ? ",a" : ",e");
                } else {
                    pw.print(tag);
                    pw.print(",");
                    pw.print(mi.shortLabel);
                    pw.print(",");
                    pw.print(dumpPss ? mi.pss : mi.mRss);
                    pw.print(",");
                    pw.println(dumpSwapPss ? java.lang.Long.valueOf(mi.swapPss) : "N/A");
                }
            } else {
                java.lang.String printFormat = "%s%s: %s%s\n";
                if ((dumpPss && dumpSwapPss) || dumpPrivateDirty) {
                    java.lang.StringBuilder format = new java.lang.StringBuilder();
                    format.append("%s%s: %-60s%s");
                    if (dumpSwapPss) {
                        format.append(java.lang.String.format("(%s in swap%s", stringifyKBSize(mi.swapPss), dumpPrivateDirty ? ", " : ")"));
                    }
                    if (dumpPrivateDirty) {
                        format.append(java.lang.String.format("%s%s private dirty)", dumpSwapPss ? "" : "(", stringifyKBSize(mi.mPrivateDirty)));
                    }
                    printFormat = format.append("\n").toString();
                }
                pw.printf(printFormat, prefix, stringifyKBSize(dumpPss ? mi.pss : mi.mRss), mi.label, mi.userId != 0 ? " (user " + mi.userId + ")" : "");
            }
            if (mi.subitems != null) {
                dumpMemItems(pw, prefix + "    ", mi.shortLabel, mi.subitems, true, isCompact, dumpPss, dumpSwapPss, dumpPrivateDirty);
            }
        }
    }

    static final void dumpMemItems(android.util.proto.ProtoOutputStream proto, long fieldId, java.lang.String tag, java.util.ArrayList<com.android.server.am.ActivityManagerService.MemItem> items, boolean sort, boolean dumpPss, boolean dumpSwapPss) {
        if (sort) {
            sortMemItems(items, dumpPss);
        }
        for (int i = 0; i < items.size(); i++) {
            com.android.server.am.ActivityManagerService.MemItem mi = items.get(i);
            long token = proto.start(fieldId);
            proto.write(1138166333441L, tag);
            proto.write(1138166333442L, mi.shortLabel);
            proto.write(1133871366148L, mi.isProc);
            proto.write(1120986464259L, mi.id);
            proto.write(1133871366149L, mi.hasActivities);
            proto.write(1112396529670L, mi.pss);
            proto.write(1112396529673L, mi.mRss);
            if (dumpSwapPss) {
                proto.write(1112396529671L, mi.swapPss);
            }
            if (mi.subitems != null) {
                dumpMemItems(proto, 2246267895816L, mi.shortLabel, mi.subitems, true, dumpPss, dumpSwapPss);
            }
            proto.end(token);
        }
    }

    static final void appendMemBucket(java.lang.StringBuilder out, long memKB, java.lang.String label, boolean stackLike) {
        int start = label.lastIndexOf(46);
        int start2 = start >= 0 ? start + 1 : 0;
        int end = label.length();
        int i = 0;
        while (true) {
            if (i >= DUMP_MEM_BUCKETS.length) {
                out.append(memKB / 1024);
                out.append(stackLike ? "MB." : "MB ");
                out.append((java.lang.CharSequence) label, start2, end);
                return;
            } else if (DUMP_MEM_BUCKETS[i] < memKB) {
                i++;
            } else {
                long bucket = DUMP_MEM_BUCKETS[i] / 1024;
                out.append(bucket);
                out.append(stackLike ? "MB." : "MB ");
                out.append((java.lang.CharSequence) label, start2, end);
                return;
            }
        }
    }

    private final void dumpApplicationMemoryUsageHeader(java.io.PrintWriter pw, long uptime, long realtime, boolean isCheckinRequest, boolean isCompact) {
        if (isCompact) {
            pw.print("version,");
            pw.println(1);
        }
        if (isCheckinRequest || isCompact) {
            pw.print("time,");
            pw.print(uptime);
            pw.print(",");
            pw.println(realtime);
            return;
        }
        pw.println("Applications Memory Usage (in Kilobytes):");
        pw.println("Uptime: " + uptime + " Realtime: " + realtime);
    }

    static final long[] getKsmInfo() {
        int[] SINGLE_LONG_FORMAT = {8224};
        long[] longTmp = {0};
        android.os.Process.readProcFile("/sys/kernel/mm/ksm/pages_shared", SINGLE_LONG_FORMAT, null, longTmp, null);
        android.os.Process.readProcFile("/sys/kernel/mm/ksm/pages_sharing", SINGLE_LONG_FORMAT, null, longTmp, null);
        longTmp[0] = 0;
        android.os.Process.readProcFile("/sys/kernel/mm/ksm/pages_unshared", SINGLE_LONG_FORMAT, null, longTmp, null);
        longTmp[0] = 0;
        android.os.Process.readProcFile("/sys/kernel/mm/ksm/pages_volatile", SINGLE_LONG_FORMAT, null, longTmp, null);
        long[] longOut = {(longTmp[0] * ((long) com.android.server.am.ProcessList.PAGE_SIZE)) / 1024, (longTmp[0] * ((long) com.android.server.am.ProcessList.PAGE_SIZE)) / 1024, (longTmp[0] * ((long) com.android.server.am.ProcessList.PAGE_SIZE)) / 1024, (longTmp[0] * ((long) com.android.server.am.ProcessList.PAGE_SIZE)) / 1024};
        return longOut;
    }

    static java.lang.String stringifySize(long size, int order) {
        java.util.Locale locale = java.util.Locale.US;
        switch (order) {
            case 1:
                return java.lang.String.format(locale, "%,13d", java.lang.Long.valueOf(size));
            case 1024:
                return java.lang.String.format(locale, "%,9dK", java.lang.Long.valueOf(size / 1024));
            case 1048576:
                return java.lang.String.format(locale, "%,5dM", java.lang.Long.valueOf((size / 1024) / 1024));
            case 1073741824:
                return java.lang.String.format(locale, "%,1dG", java.lang.Long.valueOf(((size / 1024) / 1024) / 1024));
            default:
                throw new java.lang.IllegalArgumentException("Invalid size order");
        }
    }

    static java.lang.String stringifyKBSize(long size) {
        return stringifySize(1024 * size, 1024);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class MemoryUsageDumpOptions {
        boolean dumpDalvik;
        boolean dumpDetails;
        boolean dumpFullDetails;
        boolean dumpProto;
        boolean dumpSummaryOnly;
        boolean dumpSwapPss;
        boolean dumpUnreachable;
        boolean isCheckinRequest;
        boolean isCompact;
        boolean localOnly;
        boolean mDumpAllocatorStats;
        boolean mDumpPrivateDirty;
        boolean oomOnly;
        boolean packages;

        private MemoryUsageDumpOptions() {
        }
    }

    @dalvik.annotation.optimization.NeverCompile
    final void dumpApplicationMemoryUsage(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String prefix, java.lang.String[] args, boolean brief, java.io.PrintWriter categoryPw, boolean asProto) throws java.lang.Throwable {
        java.lang.String opt;
        com.android.server.am.ActivityManagerService.MemoryUsageDumpOptions opts = new com.android.server.am.ActivityManagerService.MemoryUsageDumpOptions();
        opts.dumpDetails = false;
        opts.dumpFullDetails = false;
        opts.dumpDalvik = false;
        opts.dumpSummaryOnly = false;
        opts.dumpUnreachable = false;
        opts.oomOnly = false;
        opts.isCompact = false;
        opts.localOnly = false;
        opts.packages = false;
        opts.isCheckinRequest = false;
        opts.dumpSwapPss = false;
        opts.dumpProto = asProto;
        opts.mDumpPrivateDirty = false;
        opts.mDumpAllocatorStats = false;
        int opti = 0;
        while (opti < args.length && (opt = args[opti]) != null && opt.length() > 0 && opt.charAt(0) == '-') {
            opti++;
            if ("-a".equals(opt)) {
                opts.dumpDetails = true;
                opts.dumpFullDetails = true;
                opts.dumpDalvik = true;
                opts.dumpSwapPss = true;
            } else if ("-d".equals(opt)) {
                opts.dumpDalvik = true;
            } else if ("-c".equals(opt)) {
                opts.isCompact = true;
            } else if ("-s".equals(opt)) {
                opts.dumpDetails = true;
                opts.dumpSummaryOnly = true;
            } else if ("-S".equals(opt)) {
                opts.dumpSwapPss = true;
            } else if ("-p".equals(opt)) {
                opts.mDumpPrivateDirty = true;
            } else if ("--unreachable".equals(opt)) {
                opts.dumpUnreachable = true;
            } else if ("--oom".equals(opt)) {
                opts.oomOnly = true;
            } else if ("--local".equals(opt)) {
                opts.localOnly = true;
            } else if ("--package".equals(opt)) {
                opts.packages = true;
            } else if ("--checkin".equals(opt)) {
                opts.isCheckinRequest = true;
            } else if ("--proto".equals(opt)) {
                opts.dumpProto = true;
            } else if ("--logstats".equals(opt)) {
                opts.mDumpAllocatorStats = true;
            } else {
                if ("-h".equals(opt)) {
                    pw.println("meminfo dump options: [-a] [-d] [-c] [-s] [--oom] [process]");
                    pw.println("  -a: include all available information for each process.");
                    pw.println("  -d: include dalvik details.");
                    pw.println("  -c: dump in a compact machine-parseable representation.");
                    pw.println("  -s: dump only summary of application memory usage.");
                    pw.println("  -S: dump also SwapPss.");
                    pw.println("  -p: dump also private dirty memory usage.");
                    pw.println("  --oom: only show processes organized by oom adj.");
                    pw.println("  --local: only collect details locally, don't call process.");
                    pw.println("  --package: interpret process arg as package, dumping all");
                    pw.println("             processes that have loaded that package.");
                    pw.println("  --checkin: dump data for a checkin");
                    pw.println("  --proto: dump data to proto");
                    pw.println("If [process] is specified it can be the name or ");
                    pw.println("pid of a specific process to dump.");
                    return;
                }
                pw.println("Unknown argument: " + opt + "; use -h for help");
            }
        }
        java.lang.String[] innerArgs = new java.lang.String[args.length - opti];
        java.lang.System.arraycopy(args, opti, innerArgs, 0, args.length - opti);
        java.util.ArrayList<com.android.server.am.ProcessRecord> procs = collectProcesses(pw, opti, opts.packages, args);
        if (opts.dumpProto) {
            dumpApplicationMemoryUsage(fd, opts, innerArgs, brief, procs);
        } else {
            dumpApplicationMemoryUsage(fd, pw, prefix, opts, innerArgs, brief, procs, categoryPw);
        }
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0371  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0489  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x04c3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:442:0x012a A[SYNTHETIC] */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void dumpApplicationMemoryUsage(java.io.FileDescriptor r104, java.io.PrintWriter r105, java.lang.String r106, final com.android.server.am.ActivityManagerService.MemoryUsageDumpOptions r107, final java.lang.String[] r108, final boolean r109, java.util.ArrayList<com.android.server.am.ProcessRecord> r110, java.io.PrintWriter r111) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 3709
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.dumpApplicationMemoryUsage(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String, com.android.server.am.ActivityManagerService$MemoryUsageDumpOptions, java.lang.String[], boolean, java.util.ArrayList, java.io.PrintWriter):void");
    }

    static /* synthetic */ void lambda$dumpApplicationMemoryUsage$16(int fFindPid, java.lang.String[] innerArgs, java.util.ArrayList nativeProcs, com.android.internal.os.ProcessCpuTracker.Stats st) {
        if (st.pid == fFindPid || (st.baseName != null && st.baseName.equals(innerArgs[0]))) {
            nativeProcs.add(st);
        }
    }

    static /* synthetic */ void lambda$dumpApplicationMemoryUsage$17(android.util.SparseArray procMemsMap, android.os.Debug.MemoryInfo[] memInfos, boolean brief, com.android.server.am.ActivityManagerService.MemoryUsageDumpOptions opts, long[] tmpLong, long[] memtrackTmp, long[] ss, java.util.ArrayList procMems, long[] dalvikSubitemPss, long[] dalvikSubitemSwapPss, long[] dalvikSubitemRss, long[] dalvikSubitemPrivateDirty, long[] miscPss, long[] miscSwapPss, long[] miscRss, long[] miscPrivateDirty, long[] oomPss, long[] oomSwapPss, java.util.ArrayList[] oomProcs, long[] oomRss, long[] oomPrivateDirty, com.android.internal.os.ProcessCpuTracker.Stats st) {
        long memtrackGraphics;
        long memtrackGl;
        if (st.vsize > 0 && procMemsMap.indexOfKey(st.pid) < 0) {
            if (memInfos[0] == null) {
                memInfos[0] = new android.os.Debug.MemoryInfo();
            }
            android.os.Debug.MemoryInfo info = memInfos[0];
            if (brief || opts.oomOnly) {
                long pss = android.os.Debug.getPss(st.pid, tmpLong, memtrackTmp);
                if (pss == 0) {
                    return;
                }
                info.nativePss = (int) pss;
                info.nativePrivateDirty = (int) tmpLong[0];
                info.nativeRss = (int) tmpLong[2];
                memtrackGraphics = memtrackTmp[1];
                memtrackGl = memtrackTmp[2];
            } else {
                if (!android.os.Debug.getMemoryInfo(st.pid, info)) {
                    return;
                }
                long memtrackGraphics2 = info.getOtherPrivate(14);
                memtrackGl = info.getOtherPrivate(15);
                memtrackGraphics = memtrackGraphics2;
            }
            long myTotalPss = info.getTotalPss();
            long myTotalSwapPss = info.getTotalSwappedOutPss();
            long myTotalRss = info.getTotalRss();
            long myTotalPrivateDirty = info.getTotalPrivateDirty();
            ss[12] = ss[12] + myTotalPss;
            ss[13] = ss[13] + myTotalSwapPss;
            ss[14] = ss[14] + myTotalRss;
            ss[16] = ss[16] + myTotalPss;
            ss[17] = ss[17] + memtrackGraphics;
            ss[18] = ss[18] + memtrackGl;
            com.android.server.am.ActivityManagerService.MemItem pssItem = new com.android.server.am.ActivityManagerService.MemItem(st.name + " (pid " + st.pid + ")", st.name, myTotalPss, info.getSummaryTotalSwapPss(), myTotalRss, myTotalPrivateDirty, st.pid, android.os.UserHandle.getUserId(st.uid), false);
            procMems.add(pssItem);
            ss[0] = ss[0] + ((long) info.nativePss);
            ss[1] = ss[1] + ((long) info.nativeSwappedOutPss);
            ss[2] = ss[2] + ((long) info.nativeRss);
            ss[3] = ss[3] + ((long) info.nativePrivateDirty);
            ss[4] = ss[4] + ((long) info.dalvikPss);
            ss[5] = ss[5] + ((long) info.dalvikSwappedOutPss);
            ss[6] = ss[6] + ((long) info.dalvikRss);
            ss[7] = ss[7] + ((long) info.dalvikPrivateDirty);
            for (int j = 0; j < dalvikSubitemPss.length; j++) {
                dalvikSubitemPss[j] = dalvikSubitemPss[j] + ((long) info.getOtherPss(j + 17));
                dalvikSubitemSwapPss[j] = dalvikSubitemSwapPss[j] + ((long) info.getOtherSwappedOutPss(j + 17));
                dalvikSubitemRss[j] = dalvikSubitemRss[j] + ((long) info.getOtherRss(j + 17));
                dalvikSubitemPrivateDirty[j] = dalvikSubitemPrivateDirty[j] + ((long) info.getOtherPrivateDirty(j + 17));
            }
            ss[8] = ss[8] + ((long) info.otherPss);
            ss[9] = ss[9] + ((long) info.otherSwappedOutPss);
            ss[10] = ss[10] + ((long) info.otherRss);
            ss[11] = ss[11] + ((long) info.otherPrivateDirty);
            for (int j2 = 0; j2 < 17; j2++) {
                long mem = info.getOtherPss(j2);
                miscPss[j2] = miscPss[j2] + mem;
                ss[8] = ss[8] - mem;
                long mem2 = info.getOtherSwappedOutPss(j2);
                miscSwapPss[j2] = miscSwapPss[j2] + mem2;
                ss[9] = ss[9] - mem2;
                long mem3 = info.getOtherRss(j2);
                miscRss[j2] = miscRss[j2] + mem3;
                ss[10] = ss[10] - mem3;
                long mem4 = info.getOtherPrivateDirty(j2);
                miscPrivateDirty[j2] = miscPrivateDirty[j2] + mem4;
                ss[11] = ss[11] - mem4;
            }
            oomPss[0] = oomPss[0] + myTotalPss;
            oomSwapPss[0] = oomSwapPss[0] + myTotalSwapPss;
            if (oomProcs[0] == null) {
                oomProcs[0] = new java.util.ArrayList();
            }
            oomProcs[0].add(pssItem);
            oomRss[0] = oomRss[0] + myTotalRss;
            oomPrivateDirty[0] = oomPrivateDirty[0] + myTotalPrivateDirty;
        }
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:108:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0443  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x047b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r44v1 */
    /* JADX WARN: Type inference failed for: r44v12 */
    /* JADX WARN: Type inference failed for: r44v16 */
    /* JADX WARN: Type inference failed for: r44v17 */
    /* JADX WARN: Type inference failed for: r44v18 */
    /* JADX WARN: Type inference failed for: r44v2 */
    @dalvik.annotation.optimization.NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void dumpApplicationMemoryUsage(java.io.FileDescriptor r97, final com.android.server.am.ActivityManagerService.MemoryUsageDumpOptions r98, final java.lang.String[] r99, final boolean r100, java.util.ArrayList<com.android.server.am.ProcessRecord> r101) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2794
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.dumpApplicationMemoryUsage(java.io.FileDescriptor, com.android.server.am.ActivityManagerService$MemoryUsageDumpOptions, java.lang.String[], boolean, java.util.ArrayList):void");
    }

    static /* synthetic */ void lambda$dumpApplicationMemoryUsage$18(int fFindPid, java.lang.String[] innerArgs, java.util.ArrayList nativeProcs, com.android.internal.os.ProcessCpuTracker.Stats st) {
        if (st.pid == fFindPid || (st.baseName != null && st.baseName.equals(innerArgs[0]))) {
            nativeProcs.add(st);
        }
    }

    static /* synthetic */ void lambda$dumpApplicationMemoryUsage$19(android.util.SparseArray procMemsMap, android.os.Debug.MemoryInfo[] memInfos, boolean brief, com.android.server.am.ActivityManagerService.MemoryUsageDumpOptions opts, long[] tmpLong, long[] ss, java.util.ArrayList procMems, long[] dalvikSubitemPss, long[] dalvikSubitemSwapPss, long[] dalvikSubitemRss, long[] miscPss, long[] miscSwapPss, long[] miscRss, long[] oomPss, long[] oomSwapPss, java.util.ArrayList[] oomProcs, long[] oomRss, com.android.internal.os.ProcessCpuTracker.Stats st) {
        if (st.vsize > 0 && procMemsMap.indexOfKey(st.pid) < 0) {
            if (memInfos[0] == null) {
                memInfos[0] = new android.os.Debug.MemoryInfo();
            }
            android.os.Debug.MemoryInfo info = memInfos[0];
            if (brief || opts.oomOnly) {
                long pss = android.os.Debug.getPss(st.pid, tmpLong, null);
                if (pss == 0) {
                    return;
                }
                info.nativePss = (int) pss;
                info.nativePrivateDirty = (int) tmpLong[0];
                info.nativeRss = (int) tmpLong[2];
            } else if (!android.os.Debug.getMemoryInfo(st.pid, info)) {
                return;
            }
            long myTotalPss = info.getTotalPss();
            long myTotalSwapPss = info.getTotalSwappedOutPss();
            long myTotalRss = info.getTotalRss();
            ss[12] = ss[12] + myTotalPss;
            ss[13] = ss[13] + myTotalSwapPss;
            ss[14] = ss[14] + myTotalRss;
            ss[16] = ss[16] + myTotalPss;
            com.android.server.am.ActivityManagerService.MemItem pssItem = new com.android.server.am.ActivityManagerService.MemItem(st.name + " (pid " + st.pid + ")", st.name, myTotalPss, info.getSummaryTotalSwapPss(), myTotalRss, 0L, st.pid, android.os.UserHandle.getUserId(st.uid), false);
            procMems.add(pssItem);
            ss[0] = ss[0] + ((long) info.nativePss);
            ss[1] = ss[1] + ((long) info.nativeSwappedOutPss);
            ss[2] = ss[2] + ((long) info.nativeRss);
            ss[4] = ss[4] + ((long) info.dalvikPss);
            ss[5] = ss[5] + ((long) info.dalvikSwappedOutPss);
            ss[6] = ss[6] + ((long) info.dalvikRss);
            for (int j = 0; j < dalvikSubitemPss.length; j++) {
                dalvikSubitemPss[j] = dalvikSubitemPss[j] + ((long) info.getOtherPss(j + 17));
                dalvikSubitemSwapPss[j] = dalvikSubitemSwapPss[j] + ((long) info.getOtherSwappedOutPss(j + 17));
                dalvikSubitemRss[j] = dalvikSubitemRss[j] + ((long) info.getOtherRss(j + 17));
            }
            ss[8] = ss[8] + ((long) info.otherPss);
            ss[9] = ss[9] + ((long) info.otherSwappedOutPss);
            ss[10] = ss[10] + ((long) info.otherRss);
            for (int j2 = 0; j2 < 17; j2++) {
                long mem = info.getOtherPss(j2);
                miscPss[j2] = miscPss[j2] + mem;
                ss[8] = ss[8] - mem;
                long mem2 = info.getOtherSwappedOutPss(j2);
                miscSwapPss[j2] = miscSwapPss[j2] + mem2;
                ss[9] = ss[9] - mem2;
                long mem3 = info.getOtherRss(j2);
                miscRss[j2] = miscRss[j2] + mem3;
                ss[10] = ss[10] - mem3;
            }
            oomPss[0] = oomPss[0] + myTotalPss;
            oomSwapPss[0] = oomSwapPss[0] + myTotalSwapPss;
            if (oomProcs[0] == null) {
                oomProcs[0] = new java.util.ArrayList();
            }
            oomProcs[0].add(pssItem);
            oomRss[0] = oomRss[0] + myTotalRss;
        }
    }

    static void appendBasicMemEntry(java.lang.StringBuilder sb, int oomAdj, int procState, long pss, long memtrack, java.lang.String name) {
        sb.append("  ");
        sb.append(com.android.server.am.ProcessList.makeOomAdjString(oomAdj, false));
        sb.append(' ');
        sb.append(com.android.server.am.ProcessList.makeProcStateString(procState));
        sb.append(' ');
        com.android.server.am.ProcessList.appendRamKb(sb, pss);
        sb.append(": ");
        sb.append(name);
        if (memtrack > 0) {
            sb.append(" (");
            sb.append(stringifyKBSize(memtrack));
            sb.append(" memtrack)");
        }
    }

    static void appendMemInfo(java.lang.StringBuilder sb, com.android.server.am.ProcessMemInfo mi) {
        appendBasicMemEntry(sb, mi.oomAdj, mi.procState, mi.pss, mi.memtrack, mi.name);
        sb.append(" (pid ");
        sb.append(mi.pid);
        sb.append(") ");
        sb.append(mi.adjType);
        sb.append('\n');
        if (mi.adjReason != null) {
            sb.append("                      ");
            sb.append(mi.adjReason);
            sb.append('\n');
        }
    }

    private static boolean scanArgs(java.lang.String[] args, java.lang.String value) {
        if (args != null) {
            for (java.lang.String arg : args) {
                if (value.equals(arg)) {
                    return true;
                }
            }
        }
        return false;
    }

    final boolean cleanUpApplicationRecordLocked(final com.android.server.am.ProcessRecord app, int pid, boolean restarting, boolean allowRestart, int index, boolean replacingPid, boolean fromBinderDied) {
        boolean restart;
        boolean allowRestart2 = this.mActivityManagerServiceExt.setAllowRestartBeforeCleanUpApplicationRecord(allowRestart, app);
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            if (index >= 0) {
                try {
                    removeLruProcessLocked(app);
                    com.android.server.am.ProcessList.remove(pid);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            restart = app.onCleanupApplicationRecordLSP(this.mProcessStats, allowRestart2, fromBinderDied || app.isolated);
            this.mOomAdjuster.mCachedAppOptimizer.onCleanupApplicationRecordLocked(app);
        }
        resetPriorityAfterProcLockedSection();
        boolean restart2 = this.mActivityManagerServiceExt.setRestartAfterCleanUpApplicationRecord(restart, app);
        this.mAppProfiler.onCleanupApplicationRecordLocked(app);
        this.mBroadcastQueue.onApplicationCleanupLocked(app);
        clearProcessForegroundLocked(app);
        this.mServices.killServicesLocked(app, allowRestart2);
        this.mPhantomProcessList.onAppDied(pid);
        com.android.server.am.BackupRecord backupTarget = this.mActivityManagerServiceExt.hookGetBackupTargets(app.uid, this.mBackupTargets.get(app.userId));
        if (backupTarget != null && pid == backupTarget.app.getPid()) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKUP || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                android.util.Slog.d(TAG_CLEANUP, "App " + backupTarget.appInfo + " died during backup");
            }
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService.17
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        android.app.backup.IBackupManager bm = android.app.backup.IBackupManager.Stub.asInterface(android.os.ServiceManager.getService(com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP));
                        bm.agentDisconnectedForUser(app.userId, app.info.packageName);
                    } catch (android.os.RemoteException e) {
                    }
                }
            });
        }
        this.mProcessList.scheduleDispatchProcessDiedLocked(pid, app.info.uid);
        boolean allowRestart3 = this.mProcessList.handlePrecedingAppDiedLocked(app);
        com.android.server.am.ProcessRecord predecessor = app.mPredecessor;
        if (predecessor != null) {
            predecessor.mSuccessor = null;
            predecessor.mSuccessorStartRunnable = null;
            app.mPredecessor = null;
        }
        if (restarting) {
            return false;
        }
        if (!app.isPersistent() || app.isolated) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                android.util.Slog.v(TAG_CLEANUP, "Removing non-persistent process during cleanup: " + app);
            }
            if (!replacingPid) {
                this.mProcessList.removeProcessNameLocked(app.processName, app.uid, app);
            }
            this.mAtmInternal.clearHeavyWeightProcessIfEquals(app.getWindowProcessController());
        } else if (!app.isRemoved() && this.mPersistentStartingProcesses.indexOf(app) < 0) {
            this.mPersistentStartingProcesses.add(app);
            restart2 = true;
        }
        if ((com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) && this.mProcessesOnHold.contains(app)) {
            android.util.Slog.v(TAG_CLEANUP, "Clean-up removing on hold: " + app);
        }
        this.mProcessesOnHold.remove(app);
        this.mAtmInternal.onCleanUpApplicationRecord(app.getWindowProcessController());
        this.mProcessList.noteProcessDiedLocked(app);
        if (this.mActivityManagerServiceExt.setRestartBeforeRestartProc(restart2, app) && allowRestart3 && !app.isolated) {
            if (index < 0) {
                com.android.server.am.ProcessList.remove(pid);
            }
            this.mHandler.removeMessages(57, app);
            this.mProcessList.addProcessNameLocked(app);
            app.setPendingStart(false);
            this.mProcessList.startProcessLocked(app, new com.android.server.am.HostingRecord(com.android.server.am.HostingRecord.HOSTING_TYPE_RESTART, app.processName), 0);
            this.mActivityManagerServiceExt.hookCleanUpApplicationRecordAfterRestartProc(app);
            return true;
        }
        if (pid > 0 && pid != MY_PID) {
            removePidLocked(pid, app);
            this.mHandler.removeMessages(20, app);
            this.mHandler.removeMessages(82, app);
            this.mHandler.removeMessages(83, app);
            this.mBatteryStatsService.noteProcessFinish(app.processName, app.info.uid);
            if (app.isolated) {
                this.mBatteryStatsService.removeIsolatedUid(app.uid, app.info.uid);
                this.mActivityManagerServiceExt.removeIsolatedUid(app.uid, app.info.uid, app.info.packageName);
            }
            app.setPid(0);
        }
        return false;
    }

    public java.util.List<android.app.ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) {
        java.util.List<android.app.ActivityManager.RunningServiceInfo> runningServiceInfoLocked;
        enforceNotIsolatedCaller("getServices");
        int callingUid = android.os.Binder.getCallingUid();
        boolean canInteractAcrossUsers = android.app.ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) == 0;
        boolean allowed = this.mAtmInternal.isGetTasksAllowed("getServices", android.os.Binder.getCallingPid(), callingUid);
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                runningServiceInfoLocked = this.mServices.getRunningServiceInfoLocked(maxNum, flags, callingUid, allowed, canInteractAcrossUsers);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return runningServiceInfoLocked;
    }

    public android.app.PendingIntent getRunningServiceControlPanel(android.content.ComponentName name) {
        android.app.PendingIntent runningServiceControlPanelLocked;
        enforceNotIsolatedCaller("getRunningServiceControlPanel");
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (name == null || getPackageManagerInternal().filterAppAccess(name.getPackageName(), callingUid, callingUserId)) {
            return null;
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                runningServiceControlPanelLocked = this.mServices.getRunningServiceControlPanelLocked(name);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return runningServiceControlPanelLocked;
    }

    public void logFgsApiBegin(int apiType, int uid, int pid) {
        enforceCallingPermission("android.permission.LOG_FOREGROUND_RESOURCE_USE", "logFgsApiBegin");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mServices.logFgsApiBeginLocked(apiType, uid, pid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void logFgsApiEnd(int apiType, int uid, int pid) {
        enforceCallingPermission("android.permission.LOG_FOREGROUND_RESOURCE_USE", "logFgsApiEnd");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mServices.logFgsApiEndLocked(apiType, uid, pid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void logFgsApiStateChanged(int apiType, int state, int uid, int pid) {
        enforceCallingPermission("android.permission.LOG_FOREGROUND_RESOURCE_USE", "logFgsApiEvent");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mServices.logFgsApiStateChangedLocked(apiType, uid, pid, state);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public android.content.ComponentName startService(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, boolean requireForeground, java.lang.String callingPackage, java.lang.String callingFeatureId, int userId) throws android.os.TransactionTooLargeException {
        return startService(caller, service, resolvedType, requireForeground, callingPackage, callingFeatureId, userId, false, -1, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00cc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.content.ComponentName startService(android.app.IApplicationThread r29, android.content.Intent r30, java.lang.String r31, boolean r32, java.lang.String r33, java.lang.String r34, int r35, boolean r36, int r37, java.lang.String r38, java.lang.String r39) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 366
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.startService(android.app.IApplicationThread, android.content.Intent, java.lang.String, boolean, java.lang.String, java.lang.String, int, boolean, int, java.lang.String, java.lang.String):android.content.ComponentName");
    }

    private void validateServiceInstanceName(java.lang.String instanceName) {
        if (instanceName != null && !instanceName.matches("[a-zA-Z0-9_.]+")) {
            throw new java.lang.IllegalArgumentException("Illegal instanceName");
        }
    }

    public int stopService(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, int userId) {
        return stopService(caller, service, resolvedType, userId, false, -1, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int stopService(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, int userId, boolean isSdkSandboxService, int sdkSandboxClientAppUid, java.lang.String sdkSandboxClientAppPackage, java.lang.String instanceName) throws java.lang.Throwable {
        long j;
        enforceNotIsolatedCaller("stopService");
        if (service != null && service.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        long beginTime = android.os.SystemClock.elapsedRealtime();
        try {
            if (android.os.Trace.isTagEnabled(64L)) {
                android.os.Trace.traceBegin(64L, "stopService: " + service);
            }
            boostPriorityForLockedSection();
            try {
                synchronized (this) {
                    try {
                        int iStopServiceLocked = this.mServices.stopServiceLocked(caller, service, resolvedType, userId, isSdkSandboxService, sdkSandboxClientAppUid, sdkSandboxClientAppPackage, instanceName);
                        resetPriorityAfterLockedSection();
                        com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100182, 3, android.os.SystemClock.elapsedRealtime() - beginTime);
                        android.os.Trace.traceEnd(64L);
                        return iStopServiceLocked;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        j = 64;
                        try {
                            resetPriorityAfterLockedSection();
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100182, 3, android.os.SystemClock.elapsedRealtime() - beginTime);
                            android.os.Trace.traceEnd(j);
                            throw th;
                        }
                    }
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
            j = 64;
        }
    }

    public android.os.IBinder peekService(android.content.Intent service, java.lang.String resolvedType, java.lang.String callingPackage) {
        android.os.IBinder iBinderPeekServiceLocked;
        enforceNotIsolatedCaller("peekService");
        if (service != null && service.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        if (callingPackage == null) {
            throw new java.lang.IllegalArgumentException("callingPackage cannot be null");
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                iBinderPeekServiceLocked = this.mServices.peekServiceLocked(service, resolvedType, callingPackage);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return iBinderPeekServiceLocked;
    }

    public boolean stopServiceToken(android.content.ComponentName className, android.os.IBinder token, int startId) {
        boolean zStopServiceTokenLocked;
        long beginTime = android.os.SystemClock.elapsedRealtime();
        try {
            if (android.os.Trace.isTagEnabled(64L)) {
                android.os.Trace.traceBegin(64L, "stopServiceToken: " + (className != null ? className.toShortString() : "from " + android.os.Binder.getCallingPid()));
            }
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    zStopServiceTokenLocked = this.mServices.stopServiceTokenLocked(className, token, startId);
                } finally {
                }
            }
            resetPriorityAfterLockedSection();
            return zStopServiceTokenLocked;
        } finally {
            com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100182, 6, android.os.SystemClock.elapsedRealtime() - beginTime);
            android.os.Trace.traceEnd(64L);
        }
    }

    public void setServiceForeground(android.content.ComponentName className, android.os.IBinder token, int id, android.app.Notification notification, int flags, int foregroundServiceType) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mServices.setServiceForegroundLocked(className, token, id, notification, flags, foregroundServiceType);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public int getForegroundServiceType(android.content.ComponentName className, android.os.IBinder token) {
        int foregroundServiceTypeLocked;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                foregroundServiceTypeLocked = this.mServices.getForegroundServiceTypeLocked(className, token);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return foregroundServiceTypeLocked;
    }

    public boolean shouldServiceTimeOut(android.content.ComponentName className, android.os.IBinder token) {
        boolean zShouldServiceTimeOutLocked;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                zShouldServiceTimeOutLocked = this.mServices.shouldServiceTimeOutLocked(className, token);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return zShouldServiceTimeOutLocked;
    }

    public boolean hasServiceTimeLimitExceeded(android.content.ComponentName className, android.os.IBinder token) {
        boolean zHasServiceTimedOutLocked;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                zHasServiceTimedOutLocked = this.mServices.hasServiceTimedOutLocked(className, token);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return zHasServiceTimedOutLocked;
    }

    public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, java.lang.String name, java.lang.String callerPackage) {
        return this.mUserController.handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull ? 2 : 0, name, callerPackage);
    }

    boolean isSingleton(java.lang.String componentProcessName, android.content.pm.ApplicationInfo aInfo, java.lang.String className, int flags) {
        boolean result = false;
        if (android.os.UserHandle.getAppId(aInfo.uid) >= 10000) {
            if ((flags & 1073741824) != 0) {
                if (android.app.ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS", aInfo.uid) != 0) {
                    android.content.ComponentName comp = new android.content.ComponentName(aInfo.packageName, className);
                    java.lang.String msg = "Permission Denial: Component " + comp.flattenToShortString() + " requests FLAG_SINGLE_USER, but app does not hold android.permission.INTERACT_ACROSS_USERS";
                    android.util.Slog.w("ActivityManager", msg);
                    throw new java.lang.SecurityException(msg);
                }
                result = true;
            }
        } else if ("system".equals(componentProcessName)) {
            result = true;
        } else if ((flags & 1073741824) != 0) {
            result = android.os.UserHandle.isSameApp(aInfo.uid, 1001) || (aInfo.flags & 8) != 0;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            android.util.Slog.v(TAG_MU, "isSingleton(" + componentProcessName + ", " + aInfo + ", " + className + ", 0x" + java.lang.Integer.toHexString(flags) + ") = " + result);
        }
        return result;
    }

    boolean isSystemUserOnly(int flags) {
        return android.multiuser.Flags.enableSystemUserOnlyForServicesAndProviders() && (536870912 & flags) != 0;
    }

    boolean isValidSingletonCall(int callingUid, int componentUid) {
        int componentAppId = android.os.UserHandle.getAppId(componentUid);
        return android.os.UserHandle.isSameApp(callingUid, componentUid) || componentAppId == 1000 || componentAppId == 1001 || android.app.ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS_FULL", componentUid) == 0;
    }

    public int bindService(android.app.IApplicationThread caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, long flags, java.lang.String callingPackage, int userId) throws android.os.TransactionTooLargeException {
        return bindServiceInstance(caller, token, service, resolvedType, connection, flags, null, callingPackage, userId);
    }

    public int bindServiceInstance(android.app.IApplicationThread caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, long flags, java.lang.String instanceName, java.lang.String callingPackage, int userId) throws android.os.TransactionTooLargeException {
        return bindServiceInstance(caller, token, service, resolvedType, connection, flags, instanceName, false, -1, null, null, callingPackage, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int bindServiceInstance(android.app.IApplicationThread caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, long flags, java.lang.String instanceName, boolean isSdkSandboxService, int sdkSandboxClientAppUid, java.lang.String sdkSandboxClientAppPackage, android.app.IApplicationThread sdkSandboxClientApplicationThread, java.lang.String callingPackage, int userId) throws java.lang.Throwable {
        long j;
        int i;
        int i2;
        enforceNotIsolatedCaller("bindService");
        enforceAllowedToStartOrBindServiceIfSdkSandbox(service);
        if (service != null) {
            if (service.hasFileDescriptors()) {
                throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
            }
            service.removeExtendedFlags(1);
        }
        if (callingPackage == null) {
            throw new java.lang.IllegalArgumentException("callingPackage cannot be null");
        }
        if (isSdkSandboxService && instanceName == null) {
            throw new java.lang.IllegalArgumentException("No instance name provided for isolated process");
        }
        validateServiceInstanceName(instanceName);
        long beginTime = android.os.SystemClock.elapsedRealtime();
        long callStart = sAnrLogEnhancementHelper.getCallStartTime();
        int callingPid = android.os.Binder.getCallingPid();
        try {
            if (android.os.Trace.isTagEnabled(64L)) {
                android.content.ComponentName cn = service.getComponent();
                android.os.Trace.traceBegin(64L, "bindService:" + (cn != null ? cn.toShortString() : service.getAction()));
            }
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    j = 64;
                } catch (java.lang.Throwable th) {
                    th = th;
                    j = 64;
                    i = 100182;
                    i2 = 4;
                }
                try {
                    int iBindServiceLocked = this.mServices.bindServiceLocked(caller, token, service, resolvedType, connection, flags, instanceName, isSdkSandboxService, sdkSandboxClientAppUid, sdkSandboxClientAppPackage, sdkSandboxClientApplicationThread, callingPackage, userId, beginTime);
                    resetPriorityAfterLockedSection();
                    sAnrLogEnhancementHelper.printSlowLog(callStart, "bindServiceLocked", callingPid, true);
                    com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100182, 4, android.os.SystemClock.elapsedRealtime() - beginTime);
                    android.os.Trace.traceEnd(64L);
                    return iBindServiceLocked;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    i2 = 4;
                    i = 100182;
                    while (true) {
                        try {
                            try {
                                resetPriorityAfterLockedSection();
                                throw th;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                sAnrLogEnhancementHelper.printSlowLog(callStart, "bindServiceLocked", callingPid, true);
                                com.oplus.android.internal.util.OplusFrameworkStatsLog.write(i, i2, android.os.SystemClock.elapsedRealtime() - beginTime);
                                android.os.Trace.traceEnd(j);
                                throw th;
                            }
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                        }
                    }
                }
            }
        } catch (java.lang.Throwable th5) {
            th = th5;
            j = 64;
            i = 100182;
            i2 = 4;
        }
    }

    public void updateServiceGroup(android.app.IServiceConnection connection, int group, int importance) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mServices.updateServiceGroupLocked(connection, group, importance);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public boolean unbindService(android.app.IServiceConnection connection) {
        boolean zUnbindServiceLocked;
        long beginTime = android.os.SystemClock.elapsedRealtime();
        try {
            if (android.os.Trace.isTagEnabled(64L)) {
                android.os.Trace.traceBegin(64L, "unbindService");
            }
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    zUnbindServiceLocked = this.mServices.unbindServiceLocked(connection);
                } finally {
                }
            }
            resetPriorityAfterLockedSection();
            return zUnbindServiceLocked;
        } finally {
            com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100182, 5, android.os.SystemClock.elapsedRealtime() - beginTime);
            android.os.Trace.traceEnd(64L);
        }
    }

    public void publishService(android.os.IBinder token, android.content.Intent intent, android.os.IBinder service) {
        if (intent != null && intent.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        long beginTime = android.os.SystemClock.elapsedRealtime();
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (!(token instanceof com.android.server.am.ServiceRecord)) {
                    throw new java.lang.IllegalArgumentException("Invalid service token");
                }
                this.mServices.publishServiceLocked((com.android.server.am.ServiceRecord) token, intent, service, beginTime);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void unbindFinished(android.os.IBinder token, android.content.Intent intent, boolean doRebind) {
        if (intent != null && intent.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mServices.unbindFinishedLocked((com.android.server.am.ServiceRecord) token, intent, doRebind);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void serviceDoneExecuting(android.os.IBinder token, int type, int startId, int res, android.content.Intent intent) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (!(token instanceof com.android.server.am.ServiceRecord)) {
                    android.util.Slog.e("ActivityManager", "serviceDoneExecuting: Invalid service token=" + token);
                    throw new java.lang.IllegalArgumentException("Invalid service token");
                }
                this.mServices.serviceDoneExecutingLocked((com.android.server.am.ServiceRecord) token, type, startId, res, false, intent);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public boolean bindBackupAgent(java.lang.String packageName, int backupMode, int targetUserId, int backupDestination) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.ApplicationInfo app;
        android.content.pm.ApplicationInfo app2;
        boolean z;
        boolean wasStopped;
        android.content.pm.ApplicationInfo app3;
        com.android.server.am.BackupRecord r;
        com.android.server.am.ProcessRecord proc;
        long startTimeNs = android.os.SystemClock.uptimeNanos();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKUP) {
            android.util.Slog.v("ActivityManager", "bindBackupAgent: app=" + packageName + " mode=" + backupMode + " targetUserId=" + targetUserId + " callingUid = " + android.os.Binder.getCallingUid() + " uid = " + android.os.Process.myUid());
        }
        enforceCallingPermission("android.permission.CONFIRM_FULL_BACKUP", "bindBackupAgent");
        boolean useSystemUser = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName) || getPackageManagerInternal().getSystemUiServiceComponent().getPackageName().equals(packageName);
        int instantiatedUserId = useSystemUser ? 0 : targetUserId;
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        try {
            android.content.pm.ApplicationInfo app4 = pm.getApplicationInfo(packageName, 1024L, instantiatedUserId);
            app = app4;
        } catch (android.os.RemoteException e) {
            app = null;
        }
        if (app == null) {
            android.util.Slog.w("ActivityManager", "Unable to bind backup agent for " + packageName);
            return false;
        }
        if (app.backupAgentName != null) {
            android.content.ComponentName backupAgentName = new android.content.ComponentName(app.packageName, app.backupAgentName);
            int enableState = 0;
            try {
                enableState = pm.getComponentEnabledSetting(backupAgentName, instantiatedUserId);
            } catch (android.os.RemoteException e2) {
            }
            switch (enableState) {
                case 2:
                case 3:
                case 4:
                    android.util.Slog.w("ActivityManager", "Unable to bind backup agent for " + backupAgentName + ", the backup agent component is disabled.");
                    return false;
            }
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                try {
                    try {
                        if (android.app.Flags.appRestrictionsApi()) {
                            try {
                                wasStopped = this.mPackageManagerInt.isPackageStopped(app.packageName, android.os.UserHandle.getUserId(app.uid));
                            } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
                                e = e3;
                                app2 = app;
                                z = false;
                            } catch (java.lang.Throwable th) {
                                e = th;
                                resetPriorityAfterLockedSection();
                                throw e;
                            }
                            if (wasStopped) {
                                app2 = app;
                                z = false;
                                try {
                                    noteAppRestrictionEnabled(app.packageName, app.uid, 60, false, 1, "restore", 3, 0L);
                                } catch (android.content.pm.PackageManager.NameNotFoundException e4) {
                                    e = e4;
                                    android.util.Slog.w("ActivityManager", "No such package", e);
                                }
                            } else {
                                app2 = app;
                                z = false;
                            }
                            android.util.Slog.w("ActivityManager", "No such package", e);
                        } else {
                            app2 = app;
                            z = false;
                        }
                    } catch (java.lang.Throwable th2) {
                        e = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    e = th3;
                }
            } catch (java.lang.Throwable th4) {
                e = th4;
            }
            try {
                app3 = app2;
                try {
                    this.mPackageManagerInt.setPackageStoppedState(app3.packageName, z, android.os.UserHandle.getUserId(app3.uid));
                } catch (java.lang.IllegalArgumentException e5) {
                    e = e5;
                    android.util.Slog.w("ActivityManager", "Failed trying to unstop package " + app3.packageName + ": " + e);
                }
            } catch (java.lang.IllegalArgumentException e6) {
                e = e6;
                app3 = app2;
            } catch (java.lang.Throwable th5) {
                e = th5;
                resetPriorityAfterLockedSection();
                throw e;
            }
            com.android.server.am.BackupRecord r2 = new com.android.server.am.BackupRecord(app3, backupMode, targetUserId, backupDestination);
            android.content.ComponentName hostingName = (backupMode == 0 || backupMode == 2) ? new android.content.ComponentName(app3.packageName, app3.backupAgentName) : new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, "FullBackupAgent");
            com.android.server.am.ProcessRecord proc2 = getProcessRecordLocked(app3.processName, app3.uid);
            boolean isProcessStarted = proc2 != null ? true : z;
            if (isProcessStarted) {
                r = r2;
                proc = proc2;
            } else {
                r = r2;
                proc = startProcessLocked(app3.processName, app3, false, 0, new com.android.server.am.HostingRecord(com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP, hostingName), 4, false, false);
            }
            if (proc == null) {
                android.util.Slog.e("ActivityManager", "Unable to start backup agent process " + r);
                resetPriorityAfterLockedSection();
                return false;
            }
            this.mProcessList.getAppStartInfoTracker().handleProcessBackupStart(startTimeNs, proc, r, !isProcessStarted);
            this.mActivityManagerServiceExt.hookBindBackupAgentAfterStartProc(proc, app3);
            if (android.os.UserHandle.isApp(app3.uid) && backupMode == 1) {
                proc.setInFullBackup(true);
            }
            r.app = proc;
            com.android.server.am.BackupRecord backupTarget = this.mBackupTargets.get(targetUserId);
            int oldBackupUid = backupTarget != null ? backupTarget.appInfo.uid : -1;
            int newBackupUid = proc.isInFullBackup() ? r.appInfo.uid : -1;
            this.mBackupTargets.put(targetUserId, r);
            this.mActivityManagerServiceExt.hookBindBackupAgentAfterPutBackupTargets(r);
            proc.mProfile.addHostingComponentType(4);
            updateOomAdjLocked(proc, 15);
            android.app.IApplicationThread thread = proc.getThread();
            if (thread != null) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKUP) {
                    android.util.Slog.v(TAG_BACKUP, "Agent proc already running: " + proc);
                }
                try {
                    thread.scheduleCreateBackupAgent(app3, backupMode, targetUserId, backupDestination);
                } catch (android.os.RemoteException e7) {
                }
            } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKUP) {
                android.util.Slog.v(TAG_BACKUP, "Agent proc not running, waiting for attach");
            }
            resetPriorityAfterLockedSection();
            com.android.server.job.JobSchedulerInternal js = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
            if (oldBackupUid != -1 && backupDestination != 3) {
                js.removeBackingUpUid(oldBackupUid);
            }
            if (newBackupUid == -1) {
                return true;
            }
            js.addBackingUpUid(newBackupUid);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearPendingBackup(int userId) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKUP) {
            android.util.Slog.v(TAG_BACKUP, "clearPendingBackup: userId = " + userId + " callingUid = " + android.os.Binder.getCallingUid() + " uid = " + android.os.Process.myUid());
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                int indexOfKey = this.mBackupTargets.indexOfKey(userId);
                if (indexOfKey >= 0) {
                    com.android.server.am.BackupRecord backupTarget = this.mBackupTargets.valueAt(indexOfKey);
                    if (backupTarget != null && backupTarget.app != null) {
                        backupTarget.app.mProfile.clearHostingComponentType(4);
                    }
                    this.mBackupTargets.removeAt(indexOfKey);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        com.android.server.job.JobSchedulerInternal js = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
        js.clearAllBackingUpUids();
    }

    public void backupAgentCreated(java.lang.String agentPackageName, android.os.IBinder agent, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        enforceCallingPackage(agentPackageName, callingUid);
        int userId2 = this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, 2, "backupAgentCreated", null);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKUP) {
            android.util.Slog.v(TAG_BACKUP, "backupAgentCreated: " + agentPackageName + " = " + agent + " callingUserId = " + android.os.UserHandle.getCallingUserId() + " userId = " + userId2 + " callingUid = " + callingUid + " uid = " + android.os.Process.myUid());
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.BackupRecord backupTarget = this.mActivityManagerServiceExt.hookGetBackupTargets(callingUid, this.mBackupTargets.get(userId2));
                java.lang.String backupAppName = backupTarget == null ? null : backupTarget.appInfo.packageName;
                if (!agentPackageName.equals(backupAppName)) {
                    android.util.Slog.e("ActivityManager", "Backup agent created for " + agentPackageName + " but not requested!");
                    resetPriorityAfterLockedSection();
                    return;
                }
                resetPriorityAfterLockedSection();
                long oldIdent = android.os.Binder.clearCallingIdentity();
                try {
                    try {
                        android.app.backup.IBackupManager bm = android.app.backup.IBackupManager.Stub.asInterface(android.os.ServiceManager.getService(com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP));
                        bm.agentConnectedForUser(userId2, agentPackageName, agent);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(oldIdent);
                    }
                } catch (android.os.RemoteException e) {
                } catch (java.lang.Exception e2) {
                    android.util.Slog.w("ActivityManager", "Exception trying to deliver BackupAgent binding: ");
                    e2.printStackTrace();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void unbindBackupAgent(android.content.pm.ApplicationInfo appInfo) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BACKUP) {
            android.util.Slog.v(TAG_BACKUP, "unbindBackupAgent: " + appInfo + " appInfo.uid = " + appInfo.uid + " callingUid = " + android.os.Binder.getCallingUid() + " uid = " + android.os.Process.myUid());
        }
        enforceCallingPermission("android.permission.CONFIRM_FULL_BACKUP", "unbindBackupAgent");
        if (appInfo == null) {
            android.util.Slog.w("ActivityManager", "unbind backup agent for null app");
            return;
        }
        int userId = android.os.UserHandle.getUserId(appInfo.uid);
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mActivityManagerServiceExt.hookUnbindBackupAgent(appInfo);
                com.android.server.am.BackupRecord backupTarget = this.mActivityManagerServiceExt.hookGetBackupTargets(appInfo.uid, this.mBackupTargets.get(userId));
                java.lang.String backupAppName = backupTarget == null ? null : backupTarget.appInfo.packageName;
                try {
                    if (backupAppName == null) {
                        android.util.Slog.w("ActivityManager", "Unbinding backup agent with no active backup");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (!backupAppName.equals(appInfo.packageName)) {
                        android.util.Slog.e("ActivityManager", "Unbind of " + appInfo + " but is not the current backup target");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.am.ProcessRecord proc = backupTarget.app;
                    updateOomAdjLocked(proc, 15);
                    proc.setInFullBackup(false);
                    proc.mProfile.clearHostingComponentType(4);
                    int oldBackupUid = backupTarget != null ? backupTarget.appInfo.uid : -1;
                    android.app.IApplicationThread thread = proc.getThread();
                    if (thread != null) {
                        try {
                            thread.scheduleDestroyBackupAgent(appInfo, userId);
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e("ActivityManager", "Exception when unbinding backup agent:");
                            e.printStackTrace();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    if (oldBackupUid != -1) {
                        com.android.server.job.JobSchedulerInternal js = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
                        js.removeBackingUpUid(oldBackupUid);
                    }
                } finally {
                    this.mBackupTargets.delete(userId);
                    this.mActivityManagerServiceExt.hookAfterDeleteBackupTargets(appInfo.uid);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean isInstantApp(com.android.server.am.ProcessRecord record, java.lang.String callerPackage, int uid) throws java.lang.Throwable {
        if (android.os.UserHandle.getAppId(uid) < 10000) {
            return false;
        }
        if (record != null) {
            return record.info.isInstantApp();
        }
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        if (callerPackage == null) {
            try {
                java.lang.String[] packageNames = pm.getPackagesForUid(uid);
                if (packageNames == null || packageNames.length == 0) {
                    throw new java.lang.IllegalArgumentException("Unable to determine caller package name");
                }
                callerPackage = packageNames[0];
            } catch (android.os.RemoteException e) {
                android.util.Slog.e("ActivityManager", "Error looking up if " + callerPackage + " is an instant app.", e);
                return true;
            }
        }
        this.mAppOpsService.checkPackage(uid, callerPackage);
        return pm.isInstantApp(callerPackage, android.os.UserHandle.getUserId(uid));
    }

    @java.lang.Deprecated
    public android.content.Intent registerReceiver(android.app.IApplicationThread caller, java.lang.String callerPackage, android.content.IIntentReceiver receiver, android.content.IntentFilter filter, java.lang.String permission, int userId, int flags) {
        return registerReceiverWithFeature(caller, callerPackage, null, null, receiver, filter, permission, userId, flags);
    }

    public android.content.Intent registerReceiverWithFeature(android.app.IApplicationThread caller, java.lang.String callerPackage, java.lang.String callerFeatureId, java.lang.String receiverId, android.content.IIntentReceiver receiver, android.content.IntentFilter filter, java.lang.String permission, int userId, int flags) {
        traceRegistrationBegin(receiverId, receiver, filter, userId);
        try {
            return registerReceiverWithFeatureTraced(caller, callerPackage, callerFeatureId, receiverId, receiver, filter, permission, userId, flags);
        } finally {
            traceRegistrationEnd();
        }
    }

    private static void traceRegistrationBegin(java.lang.String receiverId, android.content.IIntentReceiver receiver, android.content.IntentFilter filter, int userId) {
        if (com.android.server.am.Flags.traceReceiverRegistration() && android.os.Trace.isTagEnabled(64L)) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("registerReceiver: ");
            sb.append(android.os.Binder.getCallingUid());
            sb.append('/');
            sb.append(receiverId == null ? "null" : receiverId);
            sb.append('/');
            int actionsCount = filter.safeCountActions();
            if (actionsCount > 0) {
                for (int i = 0; i < actionsCount; i++) {
                    sb.append(filter.getAction(i));
                    if (i != actionsCount - 1) {
                        sb.append(',');
                    }
                }
            } else {
                sb.append("null");
            }
            sb.append('/');
            sb.append('u');
            sb.append(userId);
            sb.append('/');
            sb.append(receiver != null ? receiver.asBinder() : "null");
            android.os.Trace.traceBegin(64L, sb.toString());
        }
    }

    private static void traceRegistrationEnd() {
        if (com.android.server.am.Flags.traceReceiverRegistration() && android.os.Trace.isTagEnabled(64L)) {
            android.os.Trace.traceEnd(64L);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:177:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x030f  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x036e A[Catch: SecurityException -> 0x040a, TRY_ENTER, TryCatch #27 {SecurityException -> 0x040a, blocks: (B:181:0x0315, B:183:0x0324, B:193:0x036e, B:195:0x0374), top: B:426:0x0315 }] */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0414  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0426 A[Catch: SecurityException -> 0x0430, TRY_LEAVE, TryCatch #15 {SecurityException -> 0x0430, blocks: (B:203:0x03e1, B:197:0x03b6, B:201:0x03d8, B:202:0x03de, B:198:0x03bb, B:209:0x0426, B:217:0x0442, B:223:0x046f, B:225:0x0477, B:227:0x047f), top: B:404:0x03b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x043a  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0442 A[Catch: SecurityException -> 0x0430, TRY_ENTER, TRY_LEAVE, TryCatch #15 {SecurityException -> 0x0430, blocks: (B:203:0x03e1, B:197:0x03b6, B:201:0x03d8, B:202:0x03de, B:198:0x03bb, B:209:0x0426, B:217:0x0442, B:223:0x046f, B:225:0x0477, B:227:0x047f), top: B:404:0x03b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0466 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0467 A[Catch: SecurityException -> 0x07ad, TRY_ENTER, TRY_LEAVE, TryCatch #14 {SecurityException -> 0x07ad, blocks: (B:215:0x043e, B:220:0x0467, B:228:0x0491, B:229:0x0494), top: B:402:0x043e }] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x047f A[Catch: SecurityException -> 0x0430, TRY_LEAVE, TryCatch #15 {SecurityException -> 0x0430, blocks: (B:203:0x03e1, B:197:0x03b6, B:201:0x03d8, B:202:0x03de, B:198:0x03bb, B:209:0x0426, B:217:0x0442, B:223:0x046f, B:225:0x0477, B:227:0x047f), top: B:404:0x03b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0582  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x05cc A[Catch: all -> 0x06d7, TryCatch #23 {all -> 0x06d7, blocks: (B:285:0x05c6, B:287:0x05cc, B:293:0x061a, B:295:0x062a, B:299:0x0640, B:305:0x0652, B:303:0x064a, B:308:0x06bc, B:310:0x06c5, B:312:0x06cb, B:313:0x06d2, B:288:0x05fb, B:290:0x0604, B:291:0x060b), top: B:419:0x05c6 }] */
    /* JADX WARN: Removed duplicated region for block: B:288:0x05fb A[Catch: all -> 0x06d7, TryCatch #23 {all -> 0x06d7, blocks: (B:285:0x05c6, B:287:0x05cc, B:293:0x061a, B:295:0x062a, B:299:0x0640, B:305:0x0652, B:303:0x064a, B:308:0x06bc, B:310:0x06c5, B:312:0x06cb, B:313:0x06d2, B:288:0x05fb, B:290:0x0604, B:291:0x060b), top: B:419:0x05c6 }] */
    /* JADX WARN: Removed duplicated region for block: B:293:0x061a A[Catch: all -> 0x06d7, TryCatch #23 {all -> 0x06d7, blocks: (B:285:0x05c6, B:287:0x05cc, B:293:0x061a, B:295:0x062a, B:299:0x0640, B:305:0x0652, B:303:0x064a, B:308:0x06bc, B:310:0x06c5, B:312:0x06cb, B:313:0x06d2, B:288:0x05fb, B:290:0x0604, B:291:0x060b), top: B:419:0x05c6 }] */
    /* JADX WARN: Removed duplicated region for block: B:307:0x06b8  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x06c5 A[Catch: all -> 0x06d7, TryCatch #23 {all -> 0x06d7, blocks: (B:285:0x05c6, B:287:0x05cc, B:293:0x061a, B:295:0x062a, B:299:0x0640, B:305:0x0652, B:303:0x064a, B:308:0x06bc, B:310:0x06c5, B:312:0x06cb, B:313:0x06d2, B:288:0x05fb, B:290:0x0604, B:291:0x060b), top: B:419:0x05c6 }] */
    /* JADX WARN: Removed duplicated region for block: B:311:0x06ca  */
    /* JADX WARN: Removed duplicated region for block: B:396:0x04ca A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:426:0x0315 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0168 A[Catch: SecurityException -> 0x014b, TRY_ENTER, TRY_LEAVE, TryCatch #28 {SecurityException -> 0x014b, blocks: (B:37:0x00c9, B:45:0x00d9, B:47:0x00e0, B:49:0x00ec, B:51:0x00f4, B:53:0x00fc, B:55:0x0104, B:57:0x010c, B:60:0x0115, B:61:0x0118, B:63:0x011c, B:65:0x0145, B:71:0x0155, B:73:0x015d, B:76:0x0168, B:109:0x0206, B:111:0x0210, B:114:0x0217, B:115:0x0233, B:116:0x0234, B:117:0x023b, B:118:0x023c, B:122:0x0243, B:124:0x0249, B:127:0x024e, B:128:0x0255, B:145:0x027f, B:146:0x0286, B:150:0x028d, B:154:0x029a, B:155:0x02b2), top: B:428:0x00c9 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x018b  */
    /* JADX WARN: Type inference failed for: r15v13 */
    /* JADX WARN: Type inference failed for: r15v16 */
    /* JADX WARN: Type inference failed for: r15v17 */
    /* JADX WARN: Type inference failed for: r15v18 */
    /* JADX WARN: Type inference failed for: r1v11, types: [com.android.server.am.IActivityManagerServiceExt] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.server.am.IActivityManagerServiceExt] */
    /* JADX WARN: Type inference failed for: r2v41, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r2v49, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r2v57, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.server.am.IActivityManagerServiceExt] */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.server.am.ProcessRecord, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v22 */
    /* JADX WARN: Type inference failed for: r3v37, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r3v40 */
    /* JADX WARN: Type inference failed for: r3v41 */
    /* JADX WARN: Type inference failed for: r3v46 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r4v44, types: [com.android.server.am.ProcessRecord] */
    /* JADX WARN: Type inference failed for: r4v60, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r72v0, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r7v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v13 */
    /* JADX WARN: Type inference failed for: r7v14 */
    /* JADX WARN: Type inference failed for: r7v15 */
    /* JADX WARN: Type inference failed for: r7v17, types: [android.content.IIntentReceiver] */
    /* JADX WARN: Type inference failed for: r7v18 */
    /* JADX WARN: Type inference failed for: r7v19 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v20 */
    /* JADX WARN: Type inference failed for: r7v21 */
    /* JADX WARN: Type inference failed for: r7v23 */
    /* JADX WARN: Type inference failed for: r7v24, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v25, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v26 */
    /* JADX WARN: Type inference failed for: r7v31 */
    /* JADX WARN: Type inference failed for: r7v32 */
    /* JADX WARN: Type inference failed for: r7v33 */
    /* JADX WARN: Type inference failed for: r7v34 */
    /* JADX WARN: Type inference failed for: r7v35 */
    /* JADX WARN: Type inference failed for: r7v36 */
    /* JADX WARN: Type inference failed for: r7v37 */
    /* JADX WARN: Type inference failed for: r7v42 */
    /* JADX WARN: Type inference failed for: r7v43 */
    /* JADX WARN: Type inference failed for: r7v44 */
    /* JADX WARN: Type inference failed for: r7v45 */
    /* JADX WARN: Type inference failed for: r7v46 */
    /* JADX WARN: Type inference failed for: r7v47 */
    /* JADX WARN: Type inference failed for: r7v8 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.content.Intent registerReceiverWithFeatureTraced(android.app.IApplicationThread r73, java.lang.String r74, java.lang.String r75, java.lang.String r76, android.content.IIntentReceiver r77, android.content.IntentFilter r78, java.lang.String r79, int r80, int r81) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2027
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.registerReceiverWithFeatureTraced(android.app.IApplicationThread, java.lang.String, java.lang.String, java.lang.String, android.content.IIntentReceiver, android.content.IntentFilter, java.lang.String, int, int):android.content.Intent");
    }

    public void unregisterReceiver(android.content.IIntentReceiver receiver) {
        traceUnregistrationBegin(receiver);
        try {
            unregisterReceiverTraced(receiver);
        } finally {
            traceUnregistrationEnd();
        }
    }

    private static void traceUnregistrationBegin(android.content.IIntentReceiver receiver) {
        if (com.android.server.am.Flags.traceReceiverRegistration() && android.os.Trace.isTagEnabled(64L)) {
            android.os.Trace.traceBegin(64L, android.text.TextUtils.formatSimple("unregisterReceiver: %d/%s", new java.lang.Object[]{java.lang.Integer.valueOf(android.os.Binder.getCallingUid()), receiver == null ? "null" : receiver.asBinder()}));
        }
    }

    private static void traceUnregistrationEnd() {
        if (com.android.server.am.Flags.traceReceiverRegistration() && android.os.Trace.isTagEnabled(64L)) {
            android.os.Trace.traceEnd(64L);
        }
    }

    private void unregisterReceiverTraced(android.content.IIntentReceiver receiver) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            android.util.Slog.v(TAG_BROADCAST, "Unregister receiver: " + receiver);
        }
        long startTime = android.os.SystemClock.elapsedRealtime();
        long origId = android.os.Binder.clearCallingIdentity();
        boolean doTrim = false;
        try {
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    com.android.server.am.ReceiverList rl = this.mRegisteredReceivers.get(receiver.asBinder());
                    if (rl != null) {
                        com.android.server.am.BroadcastRecord r = rl.curBroadcast;
                        if (r != null) {
                            boolean doNext = r.queue.finishReceiverLocked(rl.app, r.resultCode, r.resultData, r.resultExtras, r.resultAbort, false);
                            if (doNext) {
                                doTrim = true;
                            }
                        }
                        if (rl.app != null) {
                            rl.app.mReceivers.removeReceiver(rl);
                        }
                        removeReceiverLocked(rl);
                        if (rl.linkedToDeath) {
                            rl.linkedToDeath = false;
                            rl.receiver.asBinder().unlinkToDeath(rl, 0);
                        }
                    }
                    if (!doTrim) {
                        resetPriorityAfterLockedSection();
                    } else {
                        trimApplicationsLocked(false, 2);
                        resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
            com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100184, 2, android.os.SystemClock.elapsedRealtime() - startTime);
        }
    }

    void removeReceiverLocked(com.android.server.am.ReceiverList rl) {
        this.mRegisteredReceivers.remove(rl.receiver.asBinder());
        for (int i = rl.size() - 1; i >= 0; i--) {
            this.mReceiverResolver.removeFilter(rl.get(i));
        }
    }

    private final void sendPackageBroadcastLocked(int cmd, java.lang.String[] packages, int userId) {
        this.mProcessList.sendPackageBroadcastLocked(cmd, packages, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.ResolveInfo> collectReceiverComponents(android.content.Intent intent, java.lang.String resolvedType, int callingUid, int callingPid, int[] users, int[] broadcastAllowList) {
        int i;
        java.util.List<android.content.pm.ResolveInfo> newReceivers;
        java.util.List<android.content.pm.ResolveInfo> newReceivers2;
        java.util.HashSet<android.content.ComponentName> singleUserReceivers;
        java.util.HashSet<android.content.ComponentName> singleUserReceivers2;
        com.android.server.am.ActivityManagerService activityManagerService = this;
        int[] iArr = users;
        long pmFlags = activityManagerService.mActivityManagerServiceExt.adjustQueryReceiverPmFlags(268436480L);
        java.util.List<android.content.pm.ResolveInfo> receivers = null;
        int length = iArr.length;
        java.util.HashSet<android.content.ComponentName> singleUserReceivers3 = null;
        boolean scannedFirstReceivers = false;
        int i2 = 0;
        while (i2 < length) {
            int user = iArr[i2];
            if (callingUid == 2000 && activityManagerService.mUserController.hasUserRestriction("no_debugging_features", user)) {
                i = i2;
            } else {
                i = i2;
                java.util.List<android.content.pm.ResolveInfo> newReceivers3 = activityManagerService.mPackageManagerInt.queryIntentReceivers(intent, resolvedType, pmFlags, callingUid, callingPid, user, true);
                if (user != 0 && newReceivers3 != null) {
                    int i3 = 0;
                    while (i3 < newReceivers3.size()) {
                        if ((newReceivers3.get(i3).activityInfo.flags & 536870912) != 0) {
                            newReceivers3.remove(i3);
                            i3--;
                        }
                        i3++;
                    }
                }
                if (newReceivers3 == null) {
                    newReceivers = newReceivers3;
                } else {
                    int i4 = newReceivers3.size() - 1;
                    while (i4 >= 0) {
                        android.content.pm.ResolveInfo ri = newReceivers3.get(i4);
                        com.android.server.am.ComponentAliasResolver componentAliasResolver = activityManagerService.mComponentAliasResolver;
                        int i5 = i4;
                        java.util.List<android.content.pm.ResolveInfo> newReceivers4 = newReceivers3;
                        com.android.server.am.ComponentAliasResolver.Resolution<android.content.pm.ResolveInfo> resolution = componentAliasResolver.resolveReceiver(intent, ri, resolvedType, pmFlags, user, callingUid, callingPid);
                        if (resolution == null) {
                            newReceivers4.remove(i5);
                        } else if (resolution.isAlias()) {
                            newReceivers4.set(i5, resolution.getTarget());
                        }
                        i4 = i5 - 1;
                        activityManagerService = this;
                        newReceivers3 = newReceivers4;
                    }
                    newReceivers = newReceivers3;
                }
                if (newReceivers != null && newReceivers.size() == 0) {
                    newReceivers2 = null;
                } else {
                    newReceivers2 = newReceivers;
                }
                if (receivers == null) {
                    receivers = newReceivers2;
                } else if (newReceivers2 != null) {
                    if (!scannedFirstReceivers) {
                        scannedFirstReceivers = true;
                        for (int i6 = 0; i6 < receivers.size(); i6++) {
                            android.content.pm.ResolveInfo ri2 = receivers.get(i6);
                            if ((ri2.activityInfo.flags & 1073741824) != 0) {
                                android.content.ComponentName cn = new android.content.ComponentName(ri2.activityInfo.packageName, ri2.activityInfo.name);
                                if (singleUserReceivers3 != null) {
                                    singleUserReceivers2 = singleUserReceivers3;
                                } else {
                                    singleUserReceivers2 = new java.util.HashSet<>();
                                }
                                singleUserReceivers2.add(cn);
                                singleUserReceivers3 = singleUserReceivers2;
                            }
                        }
                    }
                    for (int i7 = 0; i7 < newReceivers2.size(); i7++) {
                        android.content.pm.ResolveInfo ri3 = newReceivers2.get(i7);
                        if ((ri3.activityInfo.flags & 1073741824) != 0) {
                            android.content.ComponentName cn2 = new android.content.ComponentName(ri3.activityInfo.packageName, ri3.activityInfo.name);
                            if (singleUserReceivers3 != null) {
                                singleUserReceivers = singleUserReceivers3;
                            } else {
                                singleUserReceivers = new java.util.HashSet<>();
                            }
                            if (!singleUserReceivers.contains(cn2)) {
                                singleUserReceivers.add(cn2);
                                receivers.add(ri3);
                            }
                            singleUserReceivers3 = singleUserReceivers;
                        } else {
                            receivers.add(ri3);
                        }
                    }
                }
            }
            i2 = i + 1;
            activityManagerService = this;
            iArr = users;
        }
        if (receivers != null && broadcastAllowList != null) {
            for (int i8 = receivers.size() - 1; i8 >= 0; i8--) {
                int receiverAppId = android.os.UserHandle.getAppId(receivers.get(i8).activityInfo.applicationInfo.uid);
                if (receiverAppId >= 10000 && java.util.Arrays.binarySearch(broadcastAllowList, receiverAppId) < 0) {
                    receivers.remove(i8);
                }
            }
        }
        return receivers;
    }

    private void checkBroadcastFromSystem(android.content.Intent intent, com.android.server.am.ProcessRecord callerApp, java.lang.String callerPackage, int callingUid, boolean isProtectedBroadcast, java.util.List receivers) {
        if ((intent.getFlags() & 4194304) != 0) {
            return;
        }
        java.lang.String action = intent.getAction();
        if (isProtectedBroadcast || "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) || "com.android.intent.action.DISMISS_KEYBOARD_SHORTCUTS".equals(action) || "android.intent.action.MEDIA_BUTTON".equals(action) || "android.intent.action.MEDIA_SCANNER_SCAN_FILE".equals(action) || "com.android.intent.action.SHOW_KEYBOARD_SHORTCUTS".equals(action) || "android.intent.action.MASTER_CLEAR".equals(action) || "android.intent.action.FACTORY_RESET".equals(action) || "android.appwidget.action.APPWIDGET_CONFIGURE".equals(action) || "android.appwidget.action.APPWIDGET_UPDATE".equals(action) || "com.android.omadm.service.CONFIGURATION_UPDATE".equals(action) || "android.text.style.SUGGESTION_PICKED".equals(action) || "android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION".equals(action) || "android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION".equals(action)) {
            return;
        }
        if (intent.getPackage() != null || intent.getComponent() != null) {
            if (receivers == null || receivers.size() == 0) {
                return;
            }
            boolean allProtected = true;
            int i = receivers.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                java.lang.Object target = receivers.get(i);
                if (target instanceof android.content.pm.ResolveInfo) {
                    android.content.pm.ResolveInfo ri = (android.content.pm.ResolveInfo) target;
                    if (!ri.activityInfo.exported || ri.activityInfo.permission != null) {
                        i--;
                    } else {
                        allProtected = false;
                        break;
                    }
                } else {
                    com.android.server.am.BroadcastFilter bf = (com.android.server.am.BroadcastFilter) target;
                    if (!bf.exported || bf.requiredPermission != null) {
                        i--;
                    } else {
                        allProtected = false;
                        break;
                    }
                }
            }
            if (allProtected) {
                return;
            }
        }
        if (callerApp != null) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                android.util.Log.w("ActivityManager", "Sending non-protected broadcast " + action + " from system " + callerApp.toShortString() + " pkg " + callerPackage);
            }
        } else if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            android.util.Log.w("ActivityManager", "Sending non-protected broadcast " + action + " from system uid " + android.os.UserHandle.formatUid(callingUid) + " pkg " + callerPackage);
        }
    }

    void enforceBroadcastOptionPermissionsInternal(android.os.Bundle options, int callingUid) {
        enforceBroadcastOptionPermissionsInternal(android.app.BroadcastOptions.fromBundleNullable(options), callingUid);
    }

    void enforceBroadcastOptionPermissionsInternal(android.app.BroadcastOptions options, int callingUid) {
        if (options != null && callingUid != 1000) {
            if (options.isAlarmBroadcast()) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST_LIGHT) {
                    android.util.Slog.w("ActivityManager", "Non-system caller " + callingUid + " may not flag broadcast as alarm");
                }
                throw new java.lang.SecurityException("Non-system callers may not flag broadcasts as alarm");
            }
            if (options.isInteractive()) {
                enforceCallingPermission("android.permission.BROADCAST_OPTION_INTERACTIVE", "setInteractive");
            }
        }
    }

    final int broadcastIntentLocked(com.android.server.am.ProcessRecord callerApp, java.lang.String callerPackage, java.lang.String callerFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, java.lang.String[] requiredPermissions, java.lang.String[] excludedPermissions, java.lang.String[] excludedPackages, int appOp, android.os.Bundle bOptions, boolean ordered, boolean sticky, int callingPid, int callingUid, int realCallingUid, int realCallingPid, int userId) {
        return broadcastIntentLocked(callerApp, callerPackage, callerFeatureId, intent, resolvedType, null, resultTo, resultCode, resultData, resultExtras, requiredPermissions, excludedPermissions, excludedPackages, appOp, bOptions, ordered, sticky, callingPid, callingUid, realCallingUid, realCallingPid, userId, android.app.BackgroundStartPrivileges.NONE, null, null);
    }

    final int broadcastIntentLocked(com.android.server.am.ProcessRecord callerApp, java.lang.String callerPackage, java.lang.String callerFeatureId, android.content.Intent intent, java.lang.String resolvedType, com.android.server.am.ProcessRecord resultToApp, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, java.lang.String[] requiredPermissions, java.lang.String[] excludedPermissions, java.lang.String[] excludedPackages, int appOp, android.os.Bundle bOptions, boolean ordered, boolean sticky, int callingPid, int callingUid, int realCallingUid, int realCallingPid, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges, int[] broadcastAllowList, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver) {
        int cookie = traceBroadcastIntentBegin(intent, resultTo, ordered, sticky, callingUid, realCallingUid, userId);
        try {
            int res = broadcastIntentLockedTraced(callerApp, callerPackage, callerFeatureId, intent, resolvedType, resultToApp, resultTo, resultCode, resultData, resultExtras, requiredPermissions, excludedPermissions, excludedPackages, appOp, android.app.BroadcastOptions.fromBundleNullable(bOptions), ordered, sticky, callingPid, callingUid, realCallingUid, realCallingPid, userId, backgroundStartPrivileges, broadcastAllowList, filterExtrasForReceiver);
            return res;
        } finally {
            traceBroadcastIntentEnd(cookie);
        }
    }

    private static int traceBroadcastIntentBegin(android.content.Intent intent, android.content.IIntentReceiver resultTo, boolean ordered, boolean sticky, int callingUid, int realCallingUid, int userId) {
        if (!com.android.server.am.Flags.traceReceiverRegistration()) {
            return com.android.server.am.BroadcastQueue.traceBegin("broadcastIntentLockedTraced");
        }
        if (android.os.Trace.isTagEnabled(64L)) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("broadcastIntent: ");
            sb.append(callingUid);
            sb.append('/');
            java.lang.String action = intent.getAction();
            sb.append(action == null ? null : action);
            sb.append('/');
            sb.append("0x");
            sb.append(java.lang.Integer.toHexString(intent.getFlags()));
            sb.append('/');
            sb.append(ordered ? "O" : "_");
            sb.append(sticky ? "S" : "_");
            sb.append(resultTo != null ? "C" : "_");
            sb.append('/');
            sb.append('u');
            sb.append(userId);
            if (callingUid != realCallingUid) {
                sb.append('/');
                sb.append("sender=");
                sb.append(realCallingUid);
            }
            return com.android.server.am.BroadcastQueue.traceBegin(sb.toString());
        }
        return 0;
    }

    private static void traceBroadcastIntentEnd(int cookie) {
        if (android.os.Trace.isTagEnabled(64L)) {
            com.android.server.am.BroadcastQueue.traceEnd(cookie);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x038c  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x03a5  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x03b2  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x03be  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0484 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x05e0  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x08b6  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x024e  */
    /* JADX WARN: Type inference failed for: r0v100 */
    /* JADX WARN: Type inference failed for: r0v103 */
    /* JADX WARN: Type inference failed for: r0v106 */
    /* JADX WARN: Type inference failed for: r0v109 */
    /* JADX WARN: Type inference failed for: r0v112 */
    /* JADX WARN: Type inference failed for: r0v113 */
    /* JADX WARN: Type inference failed for: r0v209 */
    /* JADX WARN: Type inference failed for: r0v52 */
    /* JADX WARN: Type inference failed for: r0v55 */
    /* JADX WARN: Type inference failed for: r0v58 */
    /* JADX WARN: Type inference failed for: r0v61 */
    /* JADX WARN: Type inference failed for: r0v64 */
    /* JADX WARN: Type inference failed for: r0v67 */
    /* JADX WARN: Type inference failed for: r0v70 */
    /* JADX WARN: Type inference failed for: r0v73 */
    /* JADX WARN: Type inference failed for: r0v76 */
    /* JADX WARN: Type inference failed for: r0v79 */
    /* JADX WARN: Type inference failed for: r0v82 */
    /* JADX WARN: Type inference failed for: r0v85 */
    /* JADX WARN: Type inference failed for: r0v88 */
    /* JADX WARN: Type inference failed for: r0v91 */
    /* JADX WARN: Type inference failed for: r0v94 */
    /* JADX WARN: Type inference failed for: r0v97 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final int broadcastIntentLockedTraced(com.android.server.am.ProcessRecord r84, java.lang.String r85, java.lang.String r86, android.content.Intent r87, java.lang.String r88, com.android.server.am.ProcessRecord r89, android.content.IIntentReceiver r90, int r91, java.lang.String r92, android.os.Bundle r93, java.lang.String[] r94, java.lang.String[] r95, java.lang.String[] r96, int r97, android.app.BroadcastOptions r98, boolean r99, boolean r100, int r101, int r102, int r103, int r104, int r105, android.app.BackgroundStartPrivileges r106, int[] r107, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> r108) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 4652
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.broadcastIntentLockedTraced(com.android.server.am.ProcessRecord, java.lang.String, java.lang.String, android.content.Intent, java.lang.String, com.android.server.am.ProcessRecord, android.content.IIntentReceiver, int, java.lang.String, android.os.Bundle, java.lang.String[], java.lang.String[], java.lang.String[], int, android.app.BroadcastOptions, boolean, boolean, int, int, int, int, int, android.app.BackgroundStartPrivileges, int[], java.util.function.BiFunction):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void scheduleCanceledResultTo(com.android.server.am.ProcessRecord r19, android.content.IIntentReceiver r20, android.content.Intent r21, int r22, android.app.BroadcastOptions r23, int r24, java.lang.String r25) {
        /*
            r18 = this;
            if (r20 != 0) goto L3
            return
        L3:
            r14 = r19
            r0 = 0
            if (r14 == 0) goto Ld
            android.app.IApplicationThread r1 = r14.getOnewayThread()
            goto Le
        Ld:
            r1 = r0
        Le:
            r15 = r1
            if (r15 == 0) goto L8a
            r13 = 1
            if (r23 == 0) goto L21
            boolean r1 = r23.isShareIdentityEnabled()     // Catch: android.os.RemoteException -> L1c
            if (r1 == 0) goto L21
            r1 = r13
            goto L22
        L1c:
            r0 = move-exception
            r17 = r15
            r15 = r13
            goto L52
        L21:
            r1 = 0
        L22:
            r16 = r1
            com.android.server.am.ProcessStateRecord r1 = r14.mState     // Catch: android.os.RemoteException -> L4e
            int r11 = r1.getReportedProcState()     // Catch: android.os.RemoteException -> L4e
            if (r16 == 0) goto L2f
            r12 = r24
            goto L31
        L2f:
            r1 = -1
            r12 = r1
        L31:
            if (r16 == 0) goto L35
            r0 = r25
        L35:
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 1
            r1 = r15
            r2 = r20
            r3 = r21
            r10 = r22
            r17 = r15
            r15 = r13
            r13 = r0
            r1.scheduleRegisteredReceiver(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)     // Catch: android.os.RemoteException -> L4c
            r2 = r21
            goto L8e
        L4c:
            r0 = move-exception
            goto L52
        L4e:
            r0 = move-exception
            r17 = r15
            r15 = r13
        L52:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Failed to schedule result of "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r21
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r3 = " via "
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.StringBuilder r1 = r1.append(r14)
            java.lang.String r3 = ": "
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.StringBuilder r1 = r1.append(r0)
            java.lang.String r1 = r1.toString()
            r3 = 13
            r4 = 26
            java.lang.String r5 = "Can't schedule resultTo"
            r14.killLocked(r5, r3, r4, r15)
            java.lang.String r3 = "ActivityManager"
            android.util.Slog.d(r3, r1)
            goto L8e
        L8a:
            r2 = r21
            r17 = r15
        L8e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.scheduleCanceledResultTo(com.android.server.am.ProcessRecord, android.content.IIntentReceiver, android.content.Intent, int, android.app.BroadcastOptions, int, java.lang.String):void");
    }

    private int getRealProcessStateLocked(com.android.server.am.ProcessRecord app, int pid) {
        if (app == null) {
            synchronized (this.mPidsSelfLocked) {
                app = this.mPidsSelfLocked.get(pid);
            }
        }
        if (app != null && app.getThread() != null && !app.isKilled()) {
            return app.mState.getCurProcState();
        }
        return 20;
    }

    java.util.ArrayList<com.android.server.am.ActivityManagerService.StickyBroadcast> getStickyBroadcastsForTest(java.lang.String action, int userId) {
        synchronized (this.mStickyBroadcasts) {
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.server.am.ActivityManagerService.StickyBroadcast>> stickyBroadcasts = this.mStickyBroadcasts.get(userId);
            if (stickyBroadcasts == null) {
                return null;
            }
            return stickyBroadcasts.get(action);
        }
    }

    private int getUidFromIntent(android.content.Intent intent) {
        if (intent == null) {
            return -1;
        }
        android.os.Bundle intentExtras = intent.getExtras();
        if (!intent.hasExtra("android.intent.extra.UID")) {
            return -1;
        }
        return intentExtras.getInt("android.intent.extra.UID");
    }

    final void rotateBroadcastStatsIfNeededLocked() {
        long now = android.os.SystemClock.elapsedRealtime();
        if (this.mCurBroadcastStats == null || this.mCurBroadcastStats.mStartRealtime + 86400000 < now) {
            this.mLastBroadcastStats = this.mCurBroadcastStats;
            if (this.mLastBroadcastStats != null) {
                this.mLastBroadcastStats.mEndRealtime = android.os.SystemClock.elapsedRealtime();
                this.mLastBroadcastStats.mEndUptime = android.os.SystemClock.uptimeMillis();
            }
            this.mCurBroadcastStats = new com.android.server.am.BroadcastStats();
        }
    }

    final void addBroadcastStatLocked(java.lang.String action, java.lang.String srcPackage, int receiveCount, int skipCount, long dispatchTime) {
        rotateBroadcastStatsIfNeededLocked();
        this.mCurBroadcastStats.addBroadcast(action, srcPackage, receiveCount, skipCount, dispatchTime);
    }

    final void addBackgroundCheckViolationLocked(java.lang.String action, java.lang.String targetPackage) {
        rotateBroadcastStatsIfNeededLocked();
        this.mCurBroadcastStats.addBackgroundCheckViolation(action, targetPackage);
    }

    final void notifyBroadcastFinishedLocked(com.android.server.am.BroadcastRecord original) {
        android.content.pm.ApplicationInfo info = original.callerApp != null ? original.callerApp.info : null;
        java.lang.String callerPackage = info != null ? info.packageName : original.callerPackage;
        if (callerPackage != null) {
            this.mHandler.obtainMessage(74, original.callingUid, 0, callerPackage).sendToTarget();
        }
    }

    final android.content.Intent verifyBroadcastLocked(android.content.Intent intent) {
        if (intent != null) {
            if (intent.hasFileDescriptors()) {
                throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
            }
            intent.removeExtendedFlags(1);
        }
        int flags = intent.getFlags();
        if (!this.mProcessesReady && (67108864 & flags) == 0 && (flags & 1073741824) == 0) {
            android.util.Slog.wtf("ActivityManager", "Attempt to launch receivers of broadcast intent " + intent + " before boot completion");
            intent = new android.content.Intent(intent);
            intent.addFlags(1073741824);
        }
        if ((33554432 & flags) != 0) {
            throw new java.lang.IllegalArgumentException("Can't use FLAG_RECEIVER_BOOT_UPGRADE here");
        }
        if ((flags & 4194304) != 0) {
            switch (android.os.Binder.getCallingUid()) {
                default:
                    android.util.Slog.w("ActivityManager", "Removing FLAG_RECEIVER_FROM_SHELL because caller is UID " + android.os.Binder.getCallingUid());
                    intent.removeFlags(4194304);
                case 0:
                case 2000:
                    return intent;
            }
        }
        return intent;
    }

    @java.lang.Deprecated
    public final int broadcastIntent(android.app.IApplicationThread caller, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, java.lang.String[] requiredPermissions, int appOp, android.os.Bundle bOptions, boolean serialized, boolean sticky, int userId) {
        return broadcastIntentWithFeature(caller, null, intent, resolvedType, resultTo, resultCode, resultData, resultExtras, requiredPermissions, null, null, appOp, bOptions, serialized, sticky, userId);
    }

    public final int broadcastIntentWithFeature(android.app.IApplicationThread caller, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, java.lang.String[] requiredPermissions, java.lang.String[] excludedPermissions, java.lang.String[] excludedPackages, int appOp, android.os.Bundle bOptions, boolean serialized, boolean sticky, int userId) throws java.lang.Throwable {
        java.lang.String str;
        enforceNotIsolatedCaller("broadcastIntent");
        long callStart = sAnrLogEnhancementHelper.getCallStartTime();
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                try {
                    android.content.Intent intent2 = verifyBroadcastLocked(intent);
                    com.android.server.am.ProcessRecord callerApp = getRecordForAppLOSP(caller);
                    int callingPid = android.os.Binder.getCallingPid();
                    int callingUid = android.os.Binder.getCallingUid();
                    enforceBroadcastOptionPermissionsInternal(bOptions, callingUid);
                    long origId = android.os.Binder.clearCallingIdentity();
                    if (callerApp == null) {
                        str = null;
                    } else {
                        try {
                            str = callerApp.info.packageName;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(origId);
                            sAnrLogEnhancementHelper.printSlowLog(callStart, "broadcastIntentLocked", callingPid, true);
                            throw th;
                        }
                    }
                    try {
                        try {
                            int iBroadcastIntentLocked = broadcastIntentLocked(callerApp, str, callingFeatureId, intent2, resolvedType, callerApp, resultTo, resultCode, resultData, resultExtras, requiredPermissions, excludedPermissions, excludedPackages, appOp, bOptions, serialized, sticky, callingPid, callingUid, callingUid, callingPid, userId, android.app.BackgroundStartPrivileges.NONE, null, null);
                            android.os.Binder.restoreCallingIdentity(origId);
                            sAnrLogEnhancementHelper.printSlowLog(callStart, "broadcastIntentLocked", callingPid, true);
                            resetPriorityAfterLockedSection();
                            return iBroadcastIntentLocked;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            android.os.Binder.restoreCallingIdentity(origId);
                            sAnrLogEnhancementHelper.printSlowLog(callStart, "broadcastIntentLocked", callingPid, true);
                            throw th;
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    int broadcastIntentInPackage(java.lang.String packageName, java.lang.String featureId, int uid, int realCallingUid, int realCallingPid, android.content.Intent intent, java.lang.String resolvedType, com.android.server.am.ProcessRecord resultToApp, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, java.lang.String requiredPermission, android.os.Bundle bOptions, boolean serialized, boolean sticky, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges, int[] broadcastAllowList) throws java.lang.Throwable {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                android.content.Intent intent2 = verifyBroadcastLocked(intent);
                try {
                    long origId = android.os.Binder.clearCallingIdentity();
                    java.lang.String[] requiredPermissions = requiredPermission == null ? null : new java.lang.String[]{requiredPermission};
                    try {
                        int iBroadcastIntentLocked = broadcastIntentLocked(null, packageName, featureId, intent2, resolvedType, resultToApp, resultTo, resultCode, resultData, resultExtras, requiredPermissions, null, null, -1, bOptions, serialized, sticky, -1, uid, realCallingUid, realCallingPid, userId, backgroundStartPrivileges, broadcastAllowList, null);
                        android.os.Binder.restoreCallingIdentity(origId);
                        resetPriorityAfterLockedSection();
                        return iBroadcastIntentLocked;
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(origId);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public final void unbroadcastIntent(android.app.IApplicationThread caller, android.content.Intent intent, int userId) {
        if (intent != null && intent.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        int userId2 = this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, 0, "removeStickyBroadcast", null);
        if (checkCallingPermission("android.permission.BROADCAST_STICKY") != 0) {
            java.lang.String msg = "Permission Denial: unbroadcastIntent() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.BROADCAST_STICKY";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        synchronized (this.mStickyBroadcasts) {
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.server.am.ActivityManagerService.StickyBroadcast>> stickies = this.mStickyBroadcasts.get(userId2);
            if (stickies != null) {
                java.util.ArrayList<com.android.server.am.ActivityManagerService.StickyBroadcast> list = stickies.get(intent.getAction());
                if (list != null) {
                    int N = list.size();
                    int i = 0;
                    while (true) {
                        if (i >= N) {
                            break;
                        }
                        if (!intent.filterEquals(list.get(i).intent)) {
                            i++;
                        } else {
                            list.remove(i);
                            break;
                        }
                    }
                    if (list.size() <= 0) {
                        stickies.remove(intent.getAction());
                    }
                }
                int N2 = stickies.size();
                if (N2 <= 0) {
                    this.mStickyBroadcasts.remove(userId2);
                }
            }
        }
    }

    void backgroundServicesFinishedLocked(int userId) {
        this.mBroadcastQueue.backgroundServicesFinishedLocked(userId);
    }

    public void finishReceiver(android.os.IBinder caller, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, boolean resultAbort, int flags) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            android.util.Slog.v(TAG_BROADCAST, "Finish receiver: " + caller);
        }
        if (resultExtras != null && resultExtras.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Bundle");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    com.android.server.am.ProcessRecord callerApp = getRecordForAppLOSP(caller);
                    if (callerApp == null) {
                        android.util.Slog.w("ActivityManager", "finishReceiver: no app for " + caller);
                        resetPriorityAfterLockedSection();
                    } else {
                        this.mBroadcastQueue.finishReceiverLocked(callerApp, resultCode, resultData, resultExtras, resultAbort, true);
                        trimApplicationsLocked(false, 2);
                        resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    /* JADX WARN: Not initialized variable reg: 23, insn: 0x0256: MOVE (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r23 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('userId' int)]), block:B:109:0x0256 */
    /* JADX WARN: Not initialized variable reg: 24, insn: 0x0258: MOVE (r26 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r24 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('callingPid' int)]), block:B:109:0x0256 */
    /* JADX WARN: Not initialized variable reg: 25, insn: 0x025a: MOVE (r2 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r25 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('callingUid' int)]), block:B:109:0x0256 */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x02c8  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x02d3  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x038d A[Catch: all -> 0x03cc, TryCatch #2 {all -> 0x03cc, blocks: (B:154:0x037e, B:156:0x038d, B:157:0x0392, B:173:0x03c7), top: B:192:0x02e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x039a A[Catch: all -> 0x03e8, TryCatch #1 {all -> 0x03e8, blocks: (B:184:0x03e3, B:158:0x0393, B:160:0x039a, B:161:0x03a5, B:163:0x03aa, B:164:0x03ad, B:174:0x03c8, B:175:0x03cb), top: B:191:0x004f }] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x03aa A[Catch: all -> 0x03e8, TryCatch #1 {all -> 0x03e8, blocks: (B:184:0x03e3, B:158:0x0393, B:160:0x039a, B:161:0x03a5, B:163:0x03aa, B:164:0x03ad, B:174:0x03c8, B:175:0x03cb), top: B:191:0x004f }] */
    /* JADX WARN: Removed duplicated region for block: B:192:0x02e9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean startInstrumentation(android.content.ComponentName r37, java.lang.String r38, int r39, android.os.Bundle r40, android.app.IInstrumentationWatcher r41, android.app.IUiAutomationConnection r42, int r43, java.lang.String r44) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1002
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.startInstrumentation(android.content.ComponentName, java.lang.String, int, android.os.Bundle, android.app.IInstrumentationWatcher, android.app.IUiAutomationConnection, int, java.lang.String):boolean");
    }

    private boolean hasActiveInstrumentationLocked(int pid) {
        boolean z = false;
        if (pid == 0) {
            return false;
        }
        synchronized (this.mPidsSelfLocked) {
            com.android.server.am.ProcessRecord process = this.mPidsSelfLocked.get(pid);
            if (process != null && process.getActiveInstrumentation() != null) {
                z = true;
            }
        }
        return z;
    }

    private boolean startInstrumentationOfSdkSandbox(android.content.ComponentName className, java.lang.String profileFile, android.os.Bundle arguments, android.app.IInstrumentationWatcher watcher, android.app.IUiAutomationConnection uiAutomationConnection, int userId, java.lang.String abiOverride, android.content.pm.InstrumentationInfo instrumentationInfo, android.content.pm.ApplicationInfo sdkSandboxClientAppInfo, boolean noRestart, boolean disableHiddenApiChecks, boolean disableTestApiChecks, boolean isSdkInSandbox) throws java.lang.Throwable {
        boolean z;
        android.content.pm.ApplicationInfo sdkSandboxInfo;
        java.lang.String processName;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock;
        if (noRestart) {
            reportStartInstrumentationFailureLocked(watcher, className, "Instrumenting sdk sandbox with --no-restart flag is not supported");
            return false;
        }
        com.android.server.sdksandbox.SdkSandboxManagerLocal sandboxManagerLocal = (com.android.server.sdksandbox.SdkSandboxManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.sdksandbox.SdkSandboxManagerLocal.class);
        if (sandboxManagerLocal == null) {
            reportStartInstrumentationFailureLocked(watcher, className, "Can't locate SdkSandboxManagerLocal");
            return false;
        }
        try {
            if (com.android.sdksandbox.flags.Flags.sdkSandboxInstrumentationInfo()) {
                try {
                    android.content.pm.ApplicationInfo sdkSandboxInfo2 = sandboxManagerLocal.getSdkSandboxApplicationInfoForInstrumentation(sdkSandboxClientAppInfo, isSdkInSandbox);
                    sdkSandboxInfo = sdkSandboxInfo2;
                    processName = sdkSandboxInfo2.processName;
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    z = false;
                    reportStartInstrumentationFailureLocked(watcher, className, "Can't find SdkSandbox package");
                    return z;
                }
            } else {
                android.content.pm.PackageManager pm = this.mContext.getPackageManager();
                android.content.pm.ApplicationInfo sdkSandboxInfo3 = pm.getApplicationInfoAsUser(pm.getSdkSandboxPackageName(), 0, userId);
                java.lang.String processName2 = sandboxManagerLocal.getSdkSandboxProcessNameForInstrumentation(sdkSandboxClientAppInfo);
                sdkSandboxInfo3.uid = android.os.Process.toSdkSandboxUid(sdkSandboxClientAppInfo.uid);
                sdkSandboxInfo = sdkSandboxInfo3;
                processName = processName2;
            }
            com.android.server.am.ActiveInstrumentation activeInstr = new com.android.server.am.ActiveInstrumentation(this);
            activeInstr.mClass = className;
            activeInstr.mTargetProcesses = new java.lang.String[]{processName};
            activeInstr.mTargetInfo = sdkSandboxInfo;
            activeInstr.mIsSdkInSandbox = isSdkInSandbox;
            activeInstr.mProfileFile = profileFile;
            activeInstr.mArguments = arguments;
            activeInstr.mWatcher = watcher;
            activeInstr.mUiAutomationConnection = uiAutomationConnection;
            activeInstr.mResultClass = className;
            activeInstr.mHasBackgroundActivityStartsPermission = isSdkInSandbox && checkPermission("android.permission.START_ACTIVITIES_FROM_SDK_SANDBOX", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) == 0;
            activeInstr.mHasBackgroundForegroundServiceStartsPermission = false;
            activeInstr.mNoRestart = false;
            int callingUid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                sandboxManagerLocal.notifyInstrumentationStarted(sdkSandboxClientAppInfo.packageName, sdkSandboxClientAppInfo.uid);
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock2) {
                    try {
                        try {
                            try {
                                activityManagerGlobalLock = activityManagerGlobalLock2;
                                android.content.pm.ApplicationInfo sdkSandboxInfo4 = sdkSandboxInfo;
                                try {
                                    forceStopPackageLocked(instrumentationInfo.targetPackage, -1, true, false, true, true, false, false, userId, "start instr");
                                    try {
                                        try {
                                            com.android.server.am.ProcessRecord app = addAppLocked(sdkSandboxInfo4, processName, false, true, sdkSandboxInfo4.uid, sdkSandboxClientAppInfo.packageName, disableHiddenApiChecks, disableTestApiChecks, abiOverride, 0);
                                            try {
                                                app.setActiveInstrumentation(activeInstr);
                                                activeInstr.mFinished = false;
                                                activeInstr.mSourceUid = callingUid;
                                                activeInstr.mRunningProcesses.add(app);
                                                if (!this.mActiveInstrumentation.contains(activeInstr)) {
                                                    this.mActiveInstrumentation.add(activeInstr);
                                                }
                                                app.mProfile.addHostingComponentType(8);
                                                resetPriorityAfterProcLockedSection();
                                                android.os.Binder.restoreCallingIdentity(token);
                                                return true;
                                            } catch (java.lang.Throwable th) {
                                                th = th;
                                                resetPriorityAfterProcLockedSection();
                                                throw th;
                                            }
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                        }
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                    }
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                }
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                                android.os.Binder.restoreCallingIdentity(token);
                                throw th;
                            }
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                            activityManagerGlobalLock = activityManagerGlobalLock2;
                        }
                    } catch (java.lang.Throwable th7) {
                        th = th7;
                    }
                }
            } catch (java.lang.Throwable th8) {
                th = th8;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            z = false;
        }
    }

    private void instrumentWithoutRestart(com.android.server.am.ActiveInstrumentation activeInstr, android.content.pm.ApplicationInfo targetInfo) {
        com.android.server.am.ProcessRecord pr;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                pr = getProcessRecordLocked(targetInfo.processName, targetInfo.uid);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        try {
            pr.getThread().instrumentWithoutRestart(activeInstr.mClass, activeInstr.mArguments, activeInstr.mWatcher, activeInstr.mUiAutomationConnection, targetInfo);
        } catch (android.os.RemoteException e) {
            android.util.Slog.i("ActivityManager", "RemoteException from instrumentWithoutRestart", e);
        }
    }

    private java.lang.String getPackageNameByPid(int pid) {
        synchronized (this.mPidsSelfLocked) {
            com.android.server.am.ProcessRecord app = this.mPidsSelfLocked.get(pid);
            if (app == null || app.info == null) {
                return null;
            }
            return app.info.packageName;
        }
    }

    private boolean isCallerShell() {
        int callingUid = android.os.Binder.getCallingUid();
        return callingUid == 2000 || callingUid == 0;
    }

    private void reportStartInstrumentationFailureLocked(android.app.IInstrumentationWatcher watcher, android.content.ComponentName cn, java.lang.String report) {
        android.util.Slog.w("ActivityManager", report);
        if (watcher != null) {
            android.os.Bundle results = new android.os.Bundle();
            results.putString("id", "ActivityManagerService");
            results.putString("Error", report);
            this.mInstrumentationReporter.reportStatus(watcher, cn, -1, results);
        }
    }

    void addInstrumentationResultsLocked(com.android.server.am.ProcessRecord app, android.os.Bundle results) {
        com.android.server.am.ActiveInstrumentation instr = app.getActiveInstrumentation();
        if (instr == null) {
            android.util.Slog.w("ActivityManager", "finishInstrumentation called on non-instrumented: " + app);
            return;
        }
        if (!instr.mFinished && results != null) {
            if (instr.mCurResults == null) {
                instr.mCurResults = new android.os.Bundle(results);
            } else {
                instr.mCurResults.putAll(results);
            }
        }
    }

    public void addInstrumentationResults(android.app.IApplicationThread target, android.os.Bundle results) {
        android.os.UserHandle.getCallingUserId();
        if (results != null && results.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ProcessRecord app = getRecordForAppLOSP(target);
                if (app == null) {
                    android.util.Slog.w("ActivityManager", "addInstrumentationResults: no app for " + target);
                    resetPriorityAfterLockedSection();
                    return;
                }
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    addInstrumentationResultsLocked(app, results);
                    resetPriorityAfterLockedSection();
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void finishInstrumentationLocked(com.android.server.am.ProcessRecord app, int resultCode, android.os.Bundle results) throws java.lang.Throwable {
        long j;
        try {
            android.os.Trace.traceBegin(64L, "finishInstrumentationLocked()");
            com.android.server.am.ActiveInstrumentation instr = app.getActiveInstrumentation();
            if (instr == null) {
                android.util.Slog.w("ActivityManager", "finishInstrumentation called on non-instrumented: " + app);
                android.os.Trace.traceEnd(64L);
                return;
            }
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            try {
                synchronized (activityManagerGlobalLock) {
                    try {
                        if (!instr.mFinished) {
                            try {
                                if (instr.mWatcher != null) {
                                    android.os.Bundle finalResults = instr.mCurResults;
                                    if (finalResults == null) {
                                        finalResults = results;
                                    } else if (instr.mCurResults != null && results != null) {
                                        finalResults.putAll(results);
                                    }
                                    this.mInstrumentationReporter.reportFinished(instr.mWatcher, instr.mClass, resultCode, finalResults);
                                }
                                if (instr.mUiAutomationConnection != null) {
                                    this.mAppOpsService.setMode(99, app.uid, app.info.packageName, 2);
                                    getAccessCheckDelegateHelper().onInstrumentationFinished(app.uid, app.info.packageName);
                                    this.mHandler.obtainMessage(56, instr.mUiAutomationConnection).sendToTarget();
                                }
                                instr.mFinished = true;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                while (true) {
                                    try {
                                        resetPriorityAfterProcLockedSection();
                                        throw th;
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                }
                            }
                        }
                        instr.removeProcess(app);
                        app.setActiveInstrumentation(null);
                        resetPriorityAfterProcLockedSection();
                        app.mProfile.clearHostingComponentType(8);
                        if (app.isSdkSandbox) {
                            killUid(android.os.UserHandle.getAppId(app.uid), android.os.UserHandle.getUserId(app.uid), "finished instr");
                            com.android.server.sdksandbox.SdkSandboxManagerLocal sandboxManagerLocal = (com.android.server.sdksandbox.SdkSandboxManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.sdksandbox.SdkSandboxManagerLocal.class);
                            if (sandboxManagerLocal != null) {
                                sandboxManagerLocal.notifyInstrumentationFinished(app.sdkSandboxClientAppPackage, android.os.Process.getAppUidForSdkSandboxUid(app.uid));
                            }
                            j = 64;
                        } else if (instr.mNoRestart) {
                            j = 64;
                        } else {
                            j = 64;
                            forceStopPackageLocked(app.info.packageName, -1, false, false, true, true, false, false, app.userId, "finished inst");
                        }
                        android.os.Trace.traceEnd(j);
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
                android.os.Trace.traceEnd(64L);
                throw th;
            }
        } catch (java.lang.Throwable th5) {
            th = th5;
        }
    }

    public void finishInstrumentation(android.app.IApplicationThread target, int resultCode, android.os.Bundle results) {
        android.os.UserHandle.getCallingUserId();
        if (results != null && results.hasFileDescriptors()) {
            throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ProcessRecord app = getRecordForAppLOSP(target);
                if (app == null) {
                    android.util.Slog.w("ActivityManager", "finishInstrumentation: no app for " + target);
                    resetPriorityAfterLockedSection();
                } else {
                    long origId = android.os.Binder.clearCallingIdentity();
                    finishInstrumentationLocked(app, resultCode, results);
                    android.os.Binder.restoreCallingIdentity(origId);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public android.app.ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws android.os.RemoteException {
        return this.mActivityTaskManager.getFocusedRootTaskInfo();
    }

    public android.content.res.Configuration getConfiguration() {
        return this.mActivityTaskManager.getConfiguration();
    }

    public void suppressResizeConfigChanges(boolean suppress) throws android.os.RemoteException {
        this.mActivityTaskManager.suppressResizeConfigChanges(suppress);
    }

    public void updatePersistentConfiguration(android.content.res.Configuration values) {
        updatePersistentConfigurationWithAttribution(values, android.provider.Settings.getPackageNameForUid(this.mContext, android.os.Binder.getCallingUid()), null);
    }

    public void updatePersistentConfigurationWithAttribution(android.content.res.Configuration values, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
        enforceCallingPermission("android.permission.CHANGE_CONFIGURATION", "updatePersistentConfiguration()");
        enforceWriteSettingsPermission("updatePersistentConfiguration()", callingPackage, callingAttributionTag);
        if (values == null) {
            throw new java.lang.NullPointerException("Configuration must not be null");
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (android.os.UserManager.isVisibleBackgroundUsersEnabled() && userId != getCurrentUserId()) {
            android.util.Slog.w("ActivityManager", "Only current user is allowed to update persistent configuration if visible background users are enabled. Current User" + getCurrentUserId() + ". Calling User: " + userId);
            throw new java.lang.SecurityException("Only current user is allowed to update persistent configuration.");
        }
        this.mActivityTaskManager.updatePersistentConfiguration(values, userId);
    }

    private void enforceWriteSettingsPermission(java.lang.String func, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
        int uid = android.os.Binder.getCallingUid();
        if (uid == 0 || android.provider.Settings.checkAndNoteWriteSettingsOperation(this.mContext, uid, callingPackage, callingAttributionTag, false)) {
            return;
        }
        java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + uid + " requires android.permission.WRITE_SETTINGS";
        android.util.Slog.w("ActivityManager", msg);
        throw new java.lang.SecurityException(msg);
    }

    public boolean updateConfiguration(android.content.res.Configuration values) {
        this.mActivityManagerServiceExt.hookUpdateConfigForFontFlip(values);
        this.mActivityManagerServiceExt.updateBurmeseConfig(values);
        android.util.Slog.i("ActivityManager", "updateConfiguration callingPid:" + android.os.Binder.getCallingPid() + ", callingUid:" + android.os.Binder.getCallingUid() + ", values:" + values);
        return this.mActivityTaskManager.updateConfiguration(values);
    }

    public boolean updateMccMncConfiguration(java.lang.String mcc, java.lang.String mnc) {
        try {
            int mccInt = java.lang.Integer.parseInt(mcc);
            int mncInt = java.lang.Integer.parseInt(mnc);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.mcc = mccInt;
            config.mnc = mncInt == 0 ? 65535 : mncInt;
            return this.mActivityTaskManager.updateConfiguration(config);
        } catch (java.lang.NumberFormatException | java.lang.StringIndexOutOfBoundsException ex) {
            android.util.Slog.e("ActivityManager", "Error parsing mcc: " + mcc + " mnc: " + mnc + ". ex=" + ex);
            return false;
        }
    }

    public int getLaunchedFromUid(android.os.IBinder activityToken) {
        return android.app.ActivityClient.getInstance().getLaunchedFromUid(activityToken);
    }

    public java.lang.String getLaunchedFromPackage(android.os.IBinder activityToken) {
        return android.app.ActivityClient.getInstance().getLaunchedFromPackage(activityToken);
    }

    boolean isReceivingBroadcastLocked(com.android.server.am.ProcessRecord app, int[] outSchedGroup) {
        if (this.mActivityManagerServiceExt.isReceivingBroadcastLocked(app)) {
            return true;
        }
        int res = this.mBroadcastQueue.getPreferredSchedulingGroupLocked(app);
        outSchedGroup[0] = res;
        return res != Integer.MIN_VALUE;
    }

    com.android.server.am.ActivityManagerService.Association startAssociationLocked(int sourceUid, java.lang.String sourceProcess, int sourceState, int targetUid, long targetVersionCode, android.content.ComponentName targetComponent, java.lang.String targetProcess) {
        android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>> components;
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>> sourceUids;
        android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association> sourceProcesses;
        this.mActivityManagerServiceExt.noteAssociation(sourceUid, targetUid, true);
        if (!this.mTrackingAssociations) {
            return null;
        }
        android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>> components2 = this.mAssociations.get(targetUid);
        if (components2 != null) {
            components = components2;
        } else {
            android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>> components3 = new android.util.ArrayMap<>();
            this.mAssociations.put(targetUid, components3);
            components = components3;
        }
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>> sourceUids2 = components.get(targetComponent);
        if (sourceUids2 != null) {
            sourceUids = sourceUids2;
        } else {
            android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>> sourceUids3 = new android.util.SparseArray<>();
            components.put(targetComponent, sourceUids3);
            sourceUids = sourceUids3;
        }
        android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association> sourceProcesses2 = sourceUids.get(sourceUid);
        if (sourceProcesses2 != null) {
            sourceProcesses = sourceProcesses2;
        } else {
            android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association> sourceProcesses3 = new android.util.ArrayMap<>();
            sourceUids.put(sourceUid, sourceProcesses3);
            sourceProcesses = sourceProcesses3;
        }
        com.android.server.am.ActivityManagerService.Association ass = sourceProcesses.get(sourceProcess);
        if (ass == null) {
            ass = new com.android.server.am.ActivityManagerService.Association(sourceUid, sourceProcess, targetUid, targetComponent, targetProcess);
            sourceProcesses.put(sourceProcess, ass);
        }
        ass.mCount++;
        ass.mNesting++;
        if (ass.mNesting == 1) {
            long jUptimeMillis = android.os.SystemClock.uptimeMillis();
            ass.mLastStateUptime = jUptimeMillis;
            ass.mStartTime = jUptimeMillis;
            ass.mLastState = sourceState;
        }
        return ass;
    }

    void stopAssociationLocked(int sourceUid, java.lang.String sourceProcess, int targetUid, long targetVersionCode, android.content.ComponentName targetComponent, java.lang.String targetProcess) {
        android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>> components;
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>> sourceUids;
        android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association> sourceProcesses;
        com.android.server.am.ActivityManagerService.Association ass;
        this.mActivityManagerServiceExt.noteAssociation(sourceUid, targetUid, false);
        if (!this.mTrackingAssociations || (components = this.mAssociations.get(targetUid)) == null || (sourceUids = components.get(targetComponent)) == null || (sourceProcesses = sourceUids.get(sourceUid)) == null || (ass = sourceProcesses.get(sourceProcess)) == null || ass.mNesting <= 0) {
            return;
        }
        ass.mNesting--;
        if (ass.mNesting == 0) {
            long uptime = android.os.SystemClock.uptimeMillis();
            ass.mTime += uptime - ass.mStartTime;
            long[] jArr = ass.mStateTimes;
            int i = ass.mLastState - 0;
            jArr[i] = jArr[i] + (uptime - ass.mLastStateUptime);
            ass.mLastState = 22;
        }
    }

    void noteUidProcessState(int uid, int state, int capability) {
        int N1;
        com.android.server.am.ActivityManagerService activityManagerService = this;
        int i = uid;
        activityManagerService.mBatteryStatsService.noteUidProcessState(i, state);
        activityManagerService.mAppOpsService.updateUidProcState(i, state, capability);
        if (com.android.server.stats.pull.StatsPullAtomService.ENABLE_MOBILE_DATA_STATS_AGGREGATED_PULLER) {
            try {
                if (activityManagerService.mStatsPullAtomServiceInternal == null) {
                    activityManagerService.mStatsPullAtomServiceInternal = (com.android.server.stats.pull.StatsPullAtomServiceInternal) com.android.server.LocalServices.getService(com.android.server.stats.pull.StatsPullAtomServiceInternal.class);
                }
                if (activityManagerService.mStatsPullAtomServiceInternal != null) {
                    activityManagerService.mStatsPullAtomServiceInternal.noteUidProcessState(i, state);
                } else {
                    android.util.Slog.d("ActivityManager", "StatsPullAtomService not ready yet");
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e("ActivityManager", "Exception during logging uid proc state change event", e);
            }
        }
        if (activityManagerService.mTrackingAssociations) {
            int i1 = 0;
            int N12 = activityManagerService.mAssociations.size();
            while (i1 < N12) {
                android.util.ArrayMap<android.content.ComponentName, android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>>> targetComponents = activityManagerService.mAssociations.valueAt(i1);
                int i2 = 0;
                int N2 = targetComponents.size();
                while (i2 < N2) {
                    android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association>> sourceUids = targetComponents.valueAt(i2);
                    android.util.ArrayMap<java.lang.String, com.android.server.am.ActivityManagerService.Association> sourceProcesses = sourceUids.get(i);
                    if (sourceProcesses != null) {
                        int i4 = 0;
                        int N4 = sourceProcesses.size();
                        while (i4 < N4) {
                            com.android.server.am.ActivityManagerService.Association ass = sourceProcesses.valueAt(i4);
                            if (ass.mNesting < 1) {
                                N1 = N12;
                            } else {
                                long uptime = android.os.SystemClock.uptimeMillis();
                                long[] jArr = ass.mStateTimes;
                                int i3 = ass.mLastState + 0;
                                N1 = N12;
                                jArr[i3] = jArr[i3] + (uptime - ass.mLastStateUptime);
                                ass.mLastState = state;
                                ass.mLastStateUptime = uptime;
                            }
                            i4++;
                            N12 = N1;
                        }
                    }
                    i2++;
                    i = uid;
                    N12 = N12;
                }
                i1++;
                activityManagerService = this;
                i = uid;
            }
        }
    }

    final boolean canGcNowLocked() {
        if (!this.mBroadcastQueue.isIdleLocked()) {
            return false;
        }
        return this.mAtmInternal.canGcNow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkExcessivePowerUsage() {
        updateCpuStatsNow();
        final boolean monitorPhantomProcs = this.mSystemReady && android.util.FeatureFlagUtils.isEnabled(this.mContext, "settings_enable_monitor_phantom_procs");
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                final boolean doCpuKills = this.mLastPowerCheckUptime != 0;
                final long curUptime = android.os.SystemClock.uptimeMillis();
                final long uptimeSince = curUptime - this.mLastPowerCheckUptime;
                this.mLastPowerCheckUptime = curUptime;
                this.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda8
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$checkExcessivePowerUsage$20(curUptime, uptimeSince, doCpuKills, monitorPhantomProcs, (com.android.server.am.ProcessRecord) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkExcessivePowerUsage$20(long curUptime, long uptimeSince, boolean doCpuKills, boolean monitorPhantomProcs, com.android.server.am.ProcessRecord app) {
        int cpuLimit;
        if (app.getThread() != null && app.mState.getSetProcState() >= 14) {
            long checkDur = curUptime - app.mState.getWhenUnimportant();
            if (checkDur <= this.mConstants.POWER_CHECK_INTERVAL) {
                cpuLimit = this.mConstants.POWER_CHECK_MAX_CPU_1;
            } else if (checkDur <= this.mConstants.POWER_CHECK_INTERVAL * 2 || app.mState.getSetProcState() <= 14) {
                cpuLimit = this.mConstants.POWER_CHECK_MAX_CPU_2;
            } else if (checkDur <= this.mConstants.POWER_CHECK_INTERVAL * 3 || app.mState.getSetProcState() <= 15 || this.mActivityManagerServiceExt.isRecentLockTask(app.info.packageName, app.userId)) {
                cpuLimit = this.mConstants.POWER_CHECK_MAX_CPU_3;
            } else {
                cpuLimit = this.mConstants.POWER_CHECK_MAX_CPU_4;
            }
            int cpuLimit2 = this.mActivityManagerServiceExt.adjustExcessivePowerUsage(cpuLimit, app, this.mConstants.POWER_CHECK_MAX_CPU_1, this.mConstants.POWER_CHECK_MAX_CPU_2, this.mConstants.POWER_CHECK_MAX_CPU_3, this.mConstants.POWER_CHECK_MAX_CPU_4);
            updateAppProcessCpuTimeLPr(uptimeSince, doCpuKills, checkDur, cpuLimit2, app);
            if (monitorPhantomProcs) {
                updatePhantomProcessCpuTimeLPr(uptimeSince, doCpuKills, checkDur, cpuLimit2, app);
            }
        }
    }

    private void updateAppProcessCpuTimeLPr(final long uptimeSince, boolean doCpuKills, final long checkDur, final int cpuLimit, final com.android.server.am.ProcessRecord app) {
        synchronized (this.mAppProfiler.mProfilerLock) {
            com.android.server.am.ProcessProfileRecord profile = app.mProfile;
            long curCpuTime = profile.mCurCpuTime.get();
            long lastCpuTime = profile.mLastCpuTime.get();
            if (lastCpuTime > 0) {
                final long cpuTimeUsed = curCpuTime - lastCpuTime;
                if (checkExcessivePowerUsageLPr(uptimeSince, doCpuKills, cpuTimeUsed, app.processName, app.toShortString(), cpuLimit, app)) {
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda36
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$updateAppProcessCpuTimeLPr$21(app, cpuTimeUsed, uptimeSince, checkDur, cpuLimit);
                        }
                    });
                    profile.reportExcessiveCpu();
                }
            }
            profile.mLastCpuTime.set(curCpuTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAppProcessCpuTimeLPr$21(com.android.server.am.ProcessRecord app, long cpuTimeUsed, long uptimeSince, long checkDur, int cpuLimit) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                if (app.getThread() != null && app.mState.getSetProcState() >= 14) {
                    app.killLocked("excessive cpu " + cpuTimeUsed + " during " + uptimeSince + " dur=" + checkDur + " limit=" + cpuLimit, 9, 7, true);
                    resetPriorityAfterLockedSection();
                    return;
                }
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void updatePhantomProcessCpuTimeLPr(final long uptimeSince, final boolean doCpuKills, final long checkDur, final int cpuLimit, final com.android.server.am.ProcessRecord app) {
        this.mPhantomProcessList.forEachPhantomProcessOfApp(app, new java.util.function.Function() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$updatePhantomProcessCpuTimeLPr$23(uptimeSince, doCpuKills, app, cpuLimit, checkDur, (com.android.server.am.PhantomProcessRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$updatePhantomProcessCpuTimeLPr$23(final long uptimeSince, boolean doCpuKills, final com.android.server.am.ProcessRecord app, final int cpuLimit, final long checkDur, final com.android.server.am.PhantomProcessRecord r) {
        if (r.mLastCputime > 0) {
            final long cpuTimeUsed = r.mCurrentCputime - r.mLastCputime;
            if (checkExcessivePowerUsageLPr(uptimeSince, doCpuKills, cpuTimeUsed, app.processName, r.toString(), cpuLimit, app)) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() throws java.lang.Throwable {
                        this.f$0.lambda$updatePhantomProcessCpuTimeLPr$22(app, r, cpuTimeUsed, uptimeSince, checkDur, cpuLimit);
                    }
                });
                return false;
            }
        }
        r.mLastCputime = r.mCurrentCputime;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePhantomProcessCpuTimeLPr$22(com.android.server.am.ProcessRecord app, com.android.server.am.PhantomProcessRecord r, long cpuTimeUsed, long uptimeSince, long checkDur, int cpuLimit) throws java.lang.Throwable {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                try {
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
            if (app.getThread() != null) {
                try {
                    if (app.mState.getSetProcState() >= 14) {
                        try {
                            try {
                                try {
                                    this.mPhantomProcessList.killPhantomProcessGroupLocked(app, r, 9, 7, "excessive cpu " + cpuTimeUsed + " during " + uptimeSince + " dur=" + checkDur + " limit=" + cpuLimit);
                                    resetPriorityAfterLockedSection();
                                    return;
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                }
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    }
                } catch (java.lang.Throwable th6) {
                    th = th6;
                }
                resetPriorityAfterLockedSection();
                throw th;
            }
            resetPriorityAfterLockedSection();
        }
    }

    private boolean checkExcessivePowerUsageLPr(long uptimeSince, boolean doCpuKills, long cputimeUsed, final java.lang.String processName, java.lang.String description, int cpuLimit, final com.android.server.am.ProcessRecord app) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_POWER && uptimeSince > 0) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("CPU for ");
            sb.append(description);
            sb.append(": over ");
            android.util.TimeUtils.formatDuration(uptimeSince, sb);
            sb.append(" used ");
            android.util.TimeUtils.formatDuration(cputimeUsed, sb);
            sb.append(" (");
            sb.append((cputimeUsed * 100.0d) / uptimeSince);
            sb.append("%)");
            android.util.Slog.i(TAG_POWER, sb.toString());
        }
        if (!doCpuKills || uptimeSince <= 0) {
            return false;
        }
        if ((100 * cputimeUsed) / uptimeSince >= cpuLimit) {
            this.mBatteryStatsService.reportExcessiveCpu(app.info.uid, app.processName, uptimeSince, cputimeUsed);
            app.getPkgList().forEachPackageProcessStats(new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda37
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.am.ActivityManagerService.lambda$checkExcessivePowerUsageLPr$24(app, processName, (com.android.internal.app.procstats.ProcessStats.ProcessStateHolder) obj);
                }
            });
            return true;
        }
        return false;
    }

    static /* synthetic */ void lambda$checkExcessivePowerUsageLPr$24(com.android.server.am.ProcessRecord app, java.lang.String processName, com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder) {
        com.android.internal.app.procstats.ProcessState state = holder.state;
        com.android.internal.util.FrameworkStatsLog.write(16, app.info.uid, processName, state != null ? state.getPackage() : app.info.packageName, holder.appVersion);
    }

    private boolean isEphemeralLocked(int uid) {
        java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(uid);
        if (packages == null || packages.length != 1) {
            return false;
        }
        return getPackageManagerInternal().isPackageEphemeral(android.os.UserHandle.getUserId(uid), packages[0]);
    }

    void enqueueUidChangeLocked(com.android.server.am.UidRecord uidRec, int uid, int change) {
        int uid2 = uidRec != null ? uidRec.getUid() : uid;
        if (uid2 < 0) {
            throw new java.lang.IllegalArgumentException("No UidRecord or uid");
        }
        int procState = uidRec != null ? uidRec.getSetProcState() : 20;
        int procAdj = uidRec != null ? uidRec.getMinProcAdj() : -10000;
        long procStateSeq = uidRec != null ? uidRec.curProcStateSeq : 0L;
        int capability = uidRec != null ? uidRec.getSetCapability() : 0;
        boolean ephemeral = uidRec != null ? uidRec.isEphemeral() : isEphemeralLocked(uid2);
        if (uidRec != null && uidRec.isIdle() && (change & 2) != 0) {
            this.mProcessList.killAppIfBgRestrictedAndCachedIdleLocked(uidRec);
        }
        if (uidRec != null && !uidRec.isIdle() && (change & 1) != 0) {
            change |= 2;
        }
        int enqueuedChange = this.mUidObserverController.enqueueUidChange(uidRec == null ? null : uidRec.pendingChange, uid2, change, procState, procAdj, procStateSeq, capability, ephemeral);
        if (uidRec != null) {
            uidRec.setLastReportedChange(enqueuedChange);
        }
        if (this.mLocalPowerManager != null) {
            if ((enqueuedChange & 4) != 0) {
                this.mLocalPowerManager.uidActive(uid2);
            }
            if ((enqueuedChange & 2) != 0) {
                this.mLocalPowerManager.uidIdle(uid2);
            }
            if ((enqueuedChange & 1) != 0) {
                this.mLocalPowerManager.uidGone(uid2);
            } else if ((Integer.MIN_VALUE & enqueuedChange) != 0) {
                this.mLocalPowerManager.updateUidProcState(uid2, procState);
            }
        }
    }

    final void setProcessTrackerStateLOSP(com.android.server.am.ProcessRecord proc, int memFactor) {
        if (proc.getThread() != null) {
            proc.mProfile.setProcessTrackerState(proc.mState.getReportedProcState(), memFactor);
        }
    }

    final void clearProcessForegroundLocked(com.android.server.am.ProcessRecord proc) {
        updateProcessForegroundLocked(proc, false, 0, false, false);
    }

    final void updateProcessForegroundLocked(com.android.server.am.ProcessRecord proc, boolean isForeground, int fgServiceTypes, boolean hasTypeNoneFgs, boolean oomAdj) {
        com.android.server.am.ProcessServiceRecord psr = proc.mServices;
        boolean foregroundStateChanged = isForeground != psr.hasForegroundServices();
        if (foregroundStateChanged || !psr.areForegroundServiceTypesSame(fgServiceTypes, hasTypeNoneFgs)) {
            if (foregroundStateChanged) {
                for (int i = this.mForegroundServiceStateListeners.size() - 1; i >= 0; i--) {
                    this.mForegroundServiceStateListeners.get(i).onForegroundServiceStateChanged(proc.info.packageName, proc.info.uid, proc.getPid(), isForeground);
                }
            }
            psr.setHasForegroundServices(isForeground, fgServiceTypes, hasTypeNoneFgs);
            java.util.ArrayList<com.android.server.am.ProcessRecord> curProcs = (java.util.ArrayList) this.mForegroundPackages.get(proc.info.packageName, proc.info.uid);
            if (isForeground) {
                if (curProcs == null) {
                    curProcs = new java.util.ArrayList<>();
                    this.mForegroundPackages.put(proc.info.packageName, proc.info.uid, curProcs);
                    this.mActivityManagerServiceExt.hookUpdateForegroundServiceState(proc.info.uid, proc.info.packageName, isForeground);
                }
                if (!curProcs.contains(proc)) {
                    curProcs.add(proc);
                    this.mBatteryStatsService.noteEvent(32770, proc.info.packageName, proc.info.uid);
                }
            } else if (curProcs != null && curProcs.remove(proc)) {
                this.mBatteryStatsService.noteEvent(16386, proc.info.packageName, proc.info.uid);
                if (curProcs.size() <= 0) {
                    this.mForegroundPackages.remove(proc.info.packageName, proc.info.uid);
                    this.mActivityManagerServiceExt.hookUpdateForegroundServiceState(proc.info.uid, proc.info.packageName, isForeground);
                }
            }
            psr.setReportedForegroundServiceTypes(fgServiceTypes);
            com.android.server.am.ActivityManagerService.ProcessChangeItem item = this.mProcessList.enqueueProcessChangeItemLocked(proc.getPid(), proc.info.uid);
            item.changes |= 2;
            item.foregroundServiceTypes = fgServiceTypes;
        }
        if (oomAdj) {
            updateOomAdjLocked(proc, 9);
        }
    }

    com.android.server.am.ProcessRecord getTopApp() {
        java.lang.String pkg;
        int uid;
        com.android.server.wm.WindowProcessController wpc = this.mAtmInternal != null ? this.mAtmInternal.getTopApp() : null;
        com.android.server.am.ProcessRecord r = wpc != null ? (com.android.server.am.ProcessRecord) wpc.mOwner : null;
        if (r != null) {
            pkg = r.processName;
            uid = r.info.uid;
        } else {
            pkg = null;
            uid = -1;
        }
        synchronized (this.mCurResumedAppLock) {
            if (uid != this.mCurResumedUid || (pkg != this.mCurResumedPackage && (pkg == null || !pkg.equals(this.mCurResumedPackage)))) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    if (this.mCurResumedPackage != null) {
                        this.mBatteryStatsService.noteEvent(16387, this.mCurResumedPackage, this.mCurResumedUid);
                    }
                    this.mCurResumedPackage = pkg;
                    this.mCurResumedUid = uid;
                    if (this.mCurResumedPackage != null) {
                        this.mBatteryStatsService.noteEvent(32771, this.mCurResumedPackage, this.mCurResumedUid);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }
        return r;
    }

    com.android.server.am.ProcessRecord getTopAppOnlyLocked() {
        com.android.server.wm.WindowProcessController wpc = this.mAtmInternal != null ? this.mAtmInternal.getTopApp() : null;
        if (wpc == null) {
            return null;
        }
        com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) wpc.mOwner;
        return r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: enqueueOomAdjTargetLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$appDiedLocked$2(com.android.server.am.ProcessRecord app) {
        this.mOomAdjuster.enqueueOomAdjTargetLocked(app);
    }

    void removeOomAdjTargetLocked(com.android.server.am.ProcessRecord app, boolean procDied) {
        this.mOomAdjuster.removeOomAdjTargetLocked(app, procDied);
    }

    void updateOomAdjPendingTargetsLocked(int oomAdjReason) {
        this.mOomAdjuster.updateOomAdjPendingTargetsLocked(oomAdjReason);
    }

    static final class ProcStatsRunnable implements java.lang.Runnable {
        private final com.android.server.am.ProcessStatsService mProcessStats;
        private final com.android.server.am.ActivityManagerService mService;

        ProcStatsRunnable(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessStatsService mProcessStats) {
            this.mService = service;
            this.mProcessStats = mProcessStats;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mProcessStats.writeStateAsync();
        }
    }

    final void updateOomAdjLocked(int oomAdjReason) {
        this.mOomAdjuster.updateOomAdjLocked(oomAdjReason);
    }

    final boolean updateOomAdjLocked(com.android.server.am.ProcessRecord app, int oomAdjReason) {
        return this.mOomAdjuster.updateOomAdjLocked(app, oomAdjReason);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:66:0x010a A[Catch: all -> 0x0111, TryCatch #1 {all -> 0x0111, blocks: (B:64:0x0106, B:66:0x010a, B:68:0x0110, B:55:0x00f1, B:57:0x00f5, B:58:0x00fa), top: B:82:0x003e, outer: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void makePackageIdle(java.lang.String r19, int r20) {
        /*
            Method dump skipped, instruction units count: 371
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.makePackageIdle(java.lang.String, int):void");
    }

    public void setDeterministicUidIdle(boolean deterministic) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mDeterministicUidIdle = deterministic;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    final void idleUids() {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                this.mOomAdjuster.idleUidsLocked();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    final void runInBackgroundDisabled(int uid) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.UidRecord uidRec = this.mProcessList.getUidRecordLOSP(uid);
                if (uidRec != null) {
                    if (uidRec.isIdle()) {
                        doStopUidLocked(uidRec.getUid(), uidRec);
                    }
                } else {
                    doStopUidLocked(uid, null);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    final void cameraActiveChanged(int uid, boolean active) {
        synchronized (this.mActiveCameraUids) {
            int curIndex = this.mActiveCameraUids.indexOf(uid);
            if (active) {
                if (curIndex < 0) {
                    this.mActiveCameraUids.add(uid);
                }
            } else if (curIndex >= 0) {
                this.mActiveCameraUids.remove(curIndex);
            }
        }
        if (com.android.window.flags.Flags.fifoPriorityForMajorUiProcesses()) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    adjustFifoProcessesIfNeeded(uid, !active);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterProcLockedSection();
        }
    }

    final boolean isCameraActiveForUid(int uid) {
        boolean z;
        synchronized (this.mActiveCameraUids) {
            z = this.mActiveCameraUids.indexOf(uid) >= 0;
        }
        return z;
    }

    void adjustFifoProcessesIfNeeded(int preemptiveUid, boolean allowSpecifiedFifo) {
        com.android.server.am.UidRecord uidRec;
        if (allowSpecifiedFifo == this.mAllowSpecifiedFifoScheduling) {
            return;
        }
        if (!allowSpecifiedFifo && ((uidRec = this.mProcessList.mActiveUids.get(preemptiveUid)) == null || uidRec.getCurProcState() > 2)) {
            return;
        }
        this.mAllowSpecifiedFifoScheduling = allowSpecifiedFifo;
        for (int i = this.mSpecifiedFifoProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord proc = this.mSpecifiedFifoProcesses.get(i);
            if (proc.mState.getSetSchedGroup() == 3) {
                setFifoPriority(proc, allowSpecifiedFifo);
            }
        }
    }

    final void doStopUidLocked(int uid, com.android.server.am.UidRecord uidRec) {
        this.mServices.stopInBackgroundLocked(uid);
        enqueueUidChangeLocked(uidRec, uid, -2147483646);
    }

    void tempAllowlistForPendingIntentLocked(int callerPid, int callerUid, int targetUid, long duration, int type, int reasonCode, java.lang.String reason) throws java.lang.Throwable {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ALLOWLISTS) {
            android.util.Slog.d("ActivityManager", "tempAllowlistForPendingIntentLocked(" + callerPid + ", " + callerUid + ", " + targetUid + ", " + duration + ", " + type + ")");
        }
        synchronized (this.mPidsSelfLocked) {
            com.android.server.am.ProcessRecord pr = this.mPidsSelfLocked.get(callerPid);
            if (pr == null) {
                android.util.Slog.w("ActivityManager", "tempAllowlistForPendingIntentLocked() no ProcessRecord for pid " + callerPid);
                return;
            }
            if (!pr.mServices.mAllowlistManager && checkPermission("android.permission.CHANGE_DEVICE_IDLE_TEMP_WHITELIST", callerPid, callerUid) != 0 && checkPermission("android.permission.START_ACTIVITIES_FROM_BACKGROUND", callerPid, callerUid) != 0 && checkPermission("android.permission.START_FOREGROUND_SERVICES_FROM_BACKGROUND", callerPid, callerUid) != 0) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ALLOWLISTS) {
                    android.util.Slog.d("ActivityManager", "tempAllowlistForPendingIntentLocked() for target " + targetUid + ": pid " + callerPid + " is not allowed");
                }
            } else {
                tempAllowlistUidLocked(targetUid, duration, reasonCode, reason, type, callerUid);
            }
        }
    }

    void tempAllowlistUidLocked(int targetUid, long duration, int reasonCode, java.lang.String reason, int type, int callingUid) throws java.lang.Throwable {
        int i;
        int type2;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                try {
                    if (this.mLocalDeviceIdleController != null) {
                        try {
                            i = type;
                            try {
                                type2 = this.mLocalDeviceIdleController.getTempAllowListType(reasonCode, i);
                            } catch (java.lang.Throwable th) {
                                th = th;
                                resetPriorityAfterProcLockedSection();
                                throw th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            i = type;
                        }
                    } else {
                        type2 = type;
                    }
                    try {
                        if (type2 == -1) {
                            resetPriorityAfterProcLockedSection();
                            return;
                        }
                        this.mPendingTempAllowlist.put(targetUid, new com.android.server.am.ActivityManagerService.PendingTempAllowlist(targetUid, duration, reasonCode, reason, type2, callingUid));
                        setUidTempAllowlistStateLSP(targetUid, true);
                        this.mUiHandler.obtainMessage(68).sendToTarget();
                        if (type2 == 0) {
                            this.mFgsStartTempAllowList.add(targetUid, duration, new com.android.server.am.ActivityManagerService.FgsTempAllowListItem(duration, reasonCode, reason, callingUid));
                        }
                        resetPriorityAfterProcLockedSection();
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
                i = type;
            }
        }
    }

    void pushTempAllowlist() {
        int N;
        com.android.server.am.ActivityManagerService.PendingTempAllowlist[] list;
        int index;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        N = this.mPendingTempAllowlist.size();
                        list = new com.android.server.am.ActivityManagerService.PendingTempAllowlist[N];
                        for (int i = 0; i < N; i++) {
                            list[i] = this.mPendingTempAllowlist.valueAt(i);
                        }
                    } finally {
                    }
                }
                resetPriorityAfterProcLockedSection();
            } finally {
            }
        }
        resetPriorityAfterLockedSection();
        if (this.mLocalDeviceIdleController != null) {
            for (int i2 = 0; i2 < N; i2++) {
                com.android.server.am.ActivityManagerService.PendingTempAllowlist ptw = list[i2];
                if (ptw != null) {
                    this.mLocalDeviceIdleController.addPowerSaveTempWhitelistAppDirect(ptw.targetUid, ptw.duration, ptw.type, false, ptw.reasonCode, ptw.tag, ptw.callingUid);
                }
            }
        }
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock2) {
                    for (int i3 = 0; i3 < N; i3++) {
                        try {
                            com.android.server.am.ActivityManagerService.PendingTempAllowlist ptw2 = list[i3];
                            if (ptw2 != null && (index = this.mPendingTempAllowlist.indexOfKey(ptw2.targetUid)) >= 0 && this.mPendingTempAllowlist.valueAt(index) == ptw2) {
                                this.mPendingTempAllowlist.removeAt(index);
                            }
                        } finally {
                        }
                    }
                }
                resetPriorityAfterProcLockedSection();
            } finally {
                resetPriorityAfterLockedSection();
            }
        }
    }

    final void setUidTempAllowlistStateLSP(int uid, boolean onAllowlist) {
        this.mOomAdjuster.setUidTempAllowlistStateLSP(uid, onAllowlist);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trimApplications(boolean forceFullOomAdj, int oomAdjReason) {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                trimApplicationsLocked(forceFullOomAdj, oomAdjReason);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    private void trimApplicationsLocked(boolean forceFullOomAdj, int oomAdjReason) {
        boolean didSomething = false;
        for (int i = this.mProcessList.mRemovedProcesses.size() - 1; i >= 0; i--) {
            com.android.server.am.ProcessRecord app = this.mProcessList.mRemovedProcesses.get(i);
            int size = app.getWrapper().getExtImpl().getOplusReceiverRecordListSize();
            if (!app.hasActivitiesOrRecentTasks() && app.mReceivers.numberOfCurReceivers() == 0 && size == 0 && app.mServices.numberOfRunningServices() == 0) {
                android.app.IApplicationThread thread = app.getThread();
                android.util.Slog.i("ActivityManager", "Exiting empty application process " + app.toShortString() + " (" + (thread != null ? thread.asBinder() : null) + ")\n");
                int pid = app.getPid();
                if (pid > 0 && pid != MY_PID) {
                    app.killLocked("empty", 13, 4, false);
                } else if (thread != null) {
                    try {
                        thread.scheduleExit();
                    } catch (java.lang.Exception e) {
                    }
                }
                cleanUpApplicationRecordLocked(app, pid, false, true, -1, false, false);
                this.mProcessList.mRemovedProcesses.remove(i);
                if (app.isPersistent()) {
                    addAppLocked(app.info, null, false, null, 2);
                    app.mProfile.addHostingComponentType(2);
                }
                didSomething = true;
            }
        }
        if (didSomething || forceFullOomAdj) {
            updateOomAdjLocked(oomAdjReason);
        } else {
            updateOomAdjPendingTargetsLocked(oomAdjReason);
        }
    }

    public void signalPersistentProcesses(final int sig) throws android.os.RemoteException {
        if (sig != 10) {
            throw new java.lang.SecurityException("Only SIGNAL_USR1 is allowed");
        }
        if (checkCallingPermission("android.permission.SIGNAL_PERSISTENT_PROCESSES") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SIGNAL_PERSISTENT_PROCESSES");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mProcessList.forEachLruProcessesLOSP(false, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda11
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.am.ActivityManagerService.lambda$signalPersistentProcesses$25(sig, (com.android.server.am.ProcessRecord) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    static /* synthetic */ void lambda$signalPersistentProcesses$25(int sig, com.android.server.am.ProcessRecord app) {
        if (app.getThread() != null && app.isPersistent()) {
            android.os.Process.sendSignal(app.getPid(), sig);
        }
    }

    public boolean profileControl(java.lang.String process, int userId, boolean start, android.app.ProfilerInfo profilerInfo, int profileType) throws android.os.RemoteException {
        boolean zProfileControlLPf;
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        if (start && (profilerInfo == null || profilerInfo.profileFd == null)) {
            throw new java.lang.IllegalArgumentException("null profile info or fd");
        }
        com.android.server.am.ProcessRecord proc = null;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            if (process != null) {
                try {
                    proc = findProcessLOSP(process, userId, "profileControl");
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            if (start && (proc == null || proc.getThread() == null)) {
                throw new java.lang.IllegalArgumentException("Unknown process: " + process);
            }
        }
        resetPriorityAfterProcLockedSection();
        synchronized (this.mAppProfiler.mProfilerLock) {
            zProfileControlLPf = this.mAppProfiler.profileControlLPf(proc, start, profilerInfo, profileType);
        }
        return zProfileControlLPf;
    }

    private com.android.server.am.ProcessRecord findProcessLOSP(java.lang.String process, int userId, java.lang.String callName) {
        int userId2 = this.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, 2, callName, null);
        com.android.server.am.ProcessRecord proc = null;
        try {
            int pid = java.lang.Integer.parseInt(process);
            synchronized (this.mPidsSelfLocked) {
                proc = this.mPidsSelfLocked.get(pid);
            }
        } catch (java.lang.NumberFormatException e) {
        }
        if (proc == null) {
            android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.ProcessRecord>> all = this.mProcessList.getProcessNamesLOSP().getMap();
            android.util.SparseArray<com.android.server.am.ProcessRecord> procs = all.get(process);
            if (procs != null && procs.size() > 0) {
                com.android.server.am.ProcessRecord proc2 = procs.valueAt(0);
                if (userId2 != -1 && proc2.userId != userId2) {
                    for (int i = 1; i < procs.size(); i++) {
                        com.android.server.am.ProcessRecord thisProc = procs.valueAt(i);
                        if (thisProc.userId == userId2) {
                            return thisProc;
                        }
                    }
                    return proc2;
                }
                return proc2;
            }
            return proc;
        }
        return proc;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0053, code lost:
    
        resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0056, code lost:
    
        if (0 == 0) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0058, code lost:
    
        r4.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00cc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:? A[SYNTHETIC] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean dumpHeap(java.lang.String r15, int r16, boolean r17, boolean r18, boolean r19, java.lang.String r20, java.lang.String r21, android.os.ParcelFileDescriptor r22, final android.os.RemoteCallback r23) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 210
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.dumpHeap(java.lang.String, int, boolean, boolean, boolean, java.lang.String, java.lang.String, android.os.ParcelFileDescriptor, android.os.RemoteCallback):boolean");
    }

    public boolean dumpResources(java.lang.String process, android.os.ParcelFileDescriptor fd, android.os.RemoteCallback callback) throws android.os.RemoteException {
        android.app.IApplicationThread thread;
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ProcessRecord proc = findProcessLOSP(process, -2, "dumpResources");
                if (proc == null || (thread = proc.getThread()) == null) {
                    throw new java.lang.IllegalArgumentException("Unknown process: " + process);
                }
                thread.dumpResources(fd, callback);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return true;
    }

    public void dumpAllResources(android.os.ParcelFileDescriptor fd, java.io.PrintWriter pw) throws android.os.RemoteException {
        java.util.ArrayList<com.android.server.am.ProcessRecord> processes = new java.util.ArrayList<>();
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                processes.addAll(this.mProcessList.getLruProcessesLOSP());
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        int size = processes.size();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ProcessRecord app = processes.get(i);
            pw.println(java.lang.String.format("Resources History for %s (%s)", app.processName, app.info.packageName));
            if (app.mOptRecord.isFrozen()) {
                pw.println("  Skipping frozen process");
                pw.flush();
            } else {
                pw.flush();
                try {
                    com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe("  ");
                    try {
                        android.app.IApplicationThread thread = app.getThread();
                        if (thread != null) {
                            app.getThread().dumpResources(tp.getWriteFd(), (android.os.RemoteCallback) null);
                            tp.go(fd.getFileDescriptor(), 2000L);
                        } else {
                            pw.println(java.lang.String.format("  Resources history for %s (%s) failed, no thread", app.processName, app.info.packageName));
                        }
                        tp.kill();
                    } catch (java.lang.Throwable th2) {
                        tp.kill();
                        throw th2;
                    }
                } catch (java.io.IOException e) {
                    pw.println("  " + e.getMessage());
                    pw.flush();
                }
            }
        }
    }

    public void setDumpHeapDebugLimit(java.lang.String processName, int uid, long maxMemSize, java.lang.String reportPackage) {
        if (processName != null) {
            enforceCallingPermission("android.permission.SET_DEBUG_APP", "setDumpHeapDebugLimit()");
        } else {
            synchronized (this.mPidsSelfLocked) {
                com.android.server.am.ProcessRecord proc = this.mPidsSelfLocked.get(android.os.Binder.getCallingPid());
                if (proc == null) {
                    throw new java.lang.SecurityException("No process found for calling pid " + android.os.Binder.getCallingPid());
                }
                enforceDebuggable(proc);
                processName = proc.processName;
                uid = proc.uid;
                if (reportPackage != null && !proc.getPkgList().containsKey(reportPackage)) {
                    throw new java.lang.SecurityException("Package " + reportPackage + " is not running in " + proc);
                }
            }
        }
        this.mAppProfiler.setDumpHeapDebugLimit(processName, uid, maxMemSize, reportPackage);
    }

    public void dumpHeapFinished(java.lang.String path) {
        this.mAppProfiler.dumpHeapFinished(path, android.os.Binder.getCallingPid());
        this.mActivityManagerServiceExt.updateDumpUid(android.os.Binder.getCallingUid(), false, 1);
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void onCoreSettingsChange(android.os.Bundle settings) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mProcessList.updateCoreSettingsLOSP(settings);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
    }

    public boolean startUserInBackground(int userId) {
        return startUserInBackgroundWithListener(userId, null);
    }

    public boolean startUserInBackgroundWithListener(int userId, android.os.IProgressListener unlockListener) {
        return this.mUserController.startUser(userId, 2, unlockListener);
    }

    public boolean startUserInForegroundWithListener(int userId, android.os.IProgressListener unlockListener) {
        return this.mUserController.startUser(userId, 1, unlockListener);
    }

    public boolean startUserInBackgroundVisibleOnDisplay(int userId, int displayId, android.os.IProgressListener unlockListener) {
        int[] displayIds = getDisplayIdsForStartingVisibleBackgroundUsers();
        boolean validDisplay = false;
        if (displayIds != null) {
            int i = 0;
            while (true) {
                if (i >= displayIds.length) {
                    break;
                }
                if (displayId != displayIds[i]) {
                    i++;
                } else {
                    validDisplay = true;
                    break;
                }
            }
        }
        if (!validDisplay) {
            throw new java.lang.IllegalArgumentException("Invalid display (" + displayId + ") to start user. Valid options are: " + java.util.Arrays.toString(displayIds));
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
            com.android.server.utils.Slogf.d(TAG_MU, "Calling startUserOnSecondaryDisplay(%d, %d, %s) using injector %s", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId), unlockListener, this.mInjector);
        }
        return this.mInjector.startUserInBackgroundVisibleOnDisplay(userId, displayId, unlockListener);
    }

    public int[] getDisplayIdsForStartingVisibleBackgroundUsers() {
        enforceCallingHasAtLeastOnePermission("getDisplayIdsForStartingVisibleBackgroundUsers()", "android.permission.MANAGE_USERS", "android.permission.INTERACT_ACROSS_USERS");
        return this.mInjector.getDisplayIdsForStartingVisibleBackgroundUsers();
    }

    @java.lang.Deprecated
    public boolean unlockUser(int userId, byte[] token, byte[] secret, android.os.IProgressListener listener) {
        return this.mUserController.unlockUser(userId, listener);
    }

    public boolean unlockUser2(int userId, android.os.IProgressListener listener) {
        return this.mUserController.unlockUser(userId, listener);
    }

    public boolean switchUser(int targetUserId) {
        return this.mUserController.switchUser(targetUserId);
    }

    public java.lang.String getSwitchingFromUserMessage() {
        return this.mUserController.getSwitchingFromSystemUserMessage();
    }

    public java.lang.String getSwitchingToUserMessage() {
        return this.mUserController.getSwitchingToSystemUserMessage();
    }

    public void setStopUserOnSwitch(int value) {
        this.mUserController.setStopUserOnSwitch(value);
    }

    @java.lang.Deprecated
    public int stopUser(int userId, boolean stopProfileRegardlessOfParent, android.app.IStopUserCallback callback) {
        return stopUserExceptCertainProfiles(userId, stopProfileRegardlessOfParent, callback);
    }

    public int stopUserWithCallback(int userId, android.app.IStopUserCallback callback) {
        return this.mUserController.stopUser(userId, false, callback, null);
    }

    public int stopUserExceptCertainProfiles(int userId, boolean stopProfileRegardlessOfParent, android.app.IStopUserCallback callback) {
        return this.mUserController.stopUser(userId, stopProfileRegardlessOfParent, false, callback, null);
    }

    public int stopUserWithDelayedLocking(int userId, android.app.IStopUserCallback callback) {
        return this.mUserController.stopUser(userId, true, callback, null);
    }

    public boolean startProfile(int userId) {
        return this.mUserController.startProfile(userId, false, null);
    }

    public boolean startProfileWithListener(int userId, android.os.IProgressListener unlockListener) {
        return this.mUserController.startProfile(userId, false, unlockListener);
    }

    public boolean stopProfile(int userId) {
        return this.mUserController.stopProfile(userId);
    }

    public android.content.pm.UserInfo getCurrentUser() {
        return this.mUserController.getCurrentUser();
    }

    public int getCurrentUserId() {
        return this.mUserController.getCurrentUserIdChecked();
    }

    java.lang.String getStartedUserState(int userId) {
        com.android.server.am.UserState userState = this.mUserController.getStartedUserState(userId);
        return com.android.server.am.UserState.stateToString(userState.state);
    }

    public boolean isUserRunning(int userId, int flags) {
        if (!this.mUserController.isSameProfileGroup(userId, android.os.UserHandle.getCallingUserId()) && checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") != 0) {
            java.lang.String msg = "Permission Denial: isUserRunning() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.INTERACT_ACROSS_USERS";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        return this.mUserController.isUserRunning(userId, flags);
    }

    public int[] getRunningUserIds() {
        if (checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") != 0) {
            java.lang.String msg = "Permission Denial: isUserRunning() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.INTERACT_ACROSS_USERS";
            android.util.Slog.w("ActivityManager", msg);
            throw new java.lang.SecurityException(msg);
        }
        return this.mUserController.getStartedUserArray();
    }

    public void registerUserSwitchObserver(android.app.IUserSwitchObserver observer, java.lang.String name) {
        this.mUserController.registerUserSwitchObserver(observer, name);
    }

    public void unregisterUserSwitchObserver(android.app.IUserSwitchObserver observer) {
        this.mUserController.unregisterUserSwitchObserver(observer);
    }

    android.content.pm.ApplicationInfo getAppInfoForUser(android.content.pm.ApplicationInfo info, int userId) {
        if (info == null) {
            return null;
        }
        android.content.pm.ApplicationInfo newInfo = new android.content.pm.ApplicationInfo(info);
        newInfo.initForUser(userId);
        return newInfo;
    }

    public boolean isUserStopped(int userId) {
        return this.mUserController.getStartedUserState(userId) == null;
    }

    android.content.pm.ActivityInfo getActivityInfoForUser(android.content.pm.ActivityInfo aInfo, int userId) {
        if (aInfo == null || (userId < 1 && aInfo.applicationInfo.uid < 100000)) {
            return aInfo;
        }
        android.content.pm.ActivityInfo info = new android.content.pm.ActivityInfo(aInfo);
        info.applicationInfo = getAppInfoForUser(info.applicationInfo, userId);
        return info;
    }

    private boolean processSanityChecksLPr(com.android.server.am.ProcessRecord process, android.app.IApplicationThread thread) {
        if (process == null || thread == null) {
            return false;
        }
        return android.os.Build.IS_DEBUGGABLE || process.isDebuggable();
    }

    public boolean startBinderTracking() throws android.os.RemoteException {
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                this.mBinderTransactionTrackingEnabled = true;
                this.mProcessList.forEachLruProcessesLOSP(true, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda15
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$startBinderTracking$26((com.android.server.am.ProcessRecord) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startBinderTracking$26(com.android.server.am.ProcessRecord process) {
        android.app.IApplicationThread thread = process.getThread();
        if (!processSanityChecksLPr(process, thread)) {
            return;
        }
        try {
            thread.startBinderTracking();
        } catch (android.os.RemoteException e) {
            android.util.Log.v("ActivityManager", "Process disappared");
        }
    }

    public boolean stopBinderTrackingAndDump(final android.os.ParcelFileDescriptor fd) throws android.os.RemoteException {
        if (checkCallingPermission("android.permission.SET_ACTIVITY_WATCHER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.SET_ACTIVITY_WATCHER");
        }
        boolean closeFd = true;
        try {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    if (fd == null) {
                        throw new java.lang.IllegalArgumentException("null fd");
                    }
                    this.mBinderTransactionTrackingEnabled = false;
                    final com.android.internal.util.FastPrintWriter fastPrintWriter = new com.android.internal.util.FastPrintWriter(new java.io.FileOutputStream(fd.getFileDescriptor()));
                    fastPrintWriter.println("Binder transaction traces for all processes.\n");
                    this.mProcessList.forEachLruProcessesLOSP(true, new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda30
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$stopBinderTrackingAndDump$27(fastPrintWriter, fd, (com.android.server.am.ProcessRecord) obj);
                        }
                    });
                    closeFd = false;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterProcLockedSection();
            return true;
        } finally {
            if (fd != null && closeFd) {
                try {
                    fd.close();
                } catch (java.io.IOException e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopBinderTrackingAndDump$27(java.io.PrintWriter pw, android.os.ParcelFileDescriptor fd, com.android.server.am.ProcessRecord process) {
        android.app.IApplicationThread thread = process.getThread();
        if (!processSanityChecksLPr(process, thread)) {
            return;
        }
        pw.println("Traces for process: " + process.processName);
        pw.flush();
        try {
            com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
            try {
                thread.stopBinderTrackingAndDump(tp.getWriteFd());
                tp.go(fd.getFileDescriptor());
                tp.kill();
            } catch (java.lang.Throwable th) {
                tp.kill();
                throw th;
            }
        } catch (android.os.RemoteException e) {
            pw.println("Got a RemoteException while dumping IPC traces from " + process + ".  Exception: " + e);
            pw.flush();
        } catch (java.io.IOException e2) {
            pw.println("Failure while dumping IPC traces from " + process + ".  Exception: " + e2);
            pw.flush();
        }
    }

    void onProcessFreezableChangedLocked(com.android.server.am.ProcessRecord app) {
        this.mBroadcastQueue.onProcessFreezableChangedLocked(app);
    }

    public final class LocalService extends android.app.ActivityManagerInternal implements com.android.server.am.ActivityManagerLocal {
        public LocalService() {
        }

        public java.util.List<android.app.PendingIntentStats> getPendingIntentStats() {
            return com.android.server.am.ActivityManagerService.this.mPendingIntentController.dumpPendingIntentStatsForStatsd();
        }

        public android.util.Pair<java.lang.String, java.lang.String> getAppProfileStatsForDebugging(long time, int lines) {
            return com.android.server.am.ActivityManagerService.this.mAppProfiler.getAppProfileStatsForDebugging(time, lines);
        }

        public java.lang.String checkContentProviderAccess(java.lang.String authority, int userId) {
            return com.android.server.am.ActivityManagerService.this.mCpHelper.checkContentProviderAccess(authority, userId);
        }

        public int checkContentProviderUriPermission(android.net.Uri uri, int userId, int callingUid, int modeFlags) {
            return com.android.server.am.ActivityManagerService.this.mCpHelper.checkContentProviderUriPermission(uri, userId, callingUid, modeFlags);
        }

        public void onWakefulnessChanged(int wakefulness) {
            com.android.server.am.ActivityManagerService.this.onWakefulnessChanged(wakefulness);
        }

        public boolean startIsolatedProcess(java.lang.String entryPoint, java.lang.String[] entryPointArgs, java.lang.String processName, java.lang.String abiOverride, int uid, java.lang.Runnable crashHandler) {
            return com.android.server.am.ActivityManagerService.this.startIsolatedProcess(entryPoint, entryPointArgs, processName, abiOverride, uid, crashHandler);
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public android.content.ComponentName startSdkSandboxService(android.content.Intent service, int clientAppUid, java.lang.String clientAppPackage, java.lang.String processName) throws java.lang.Throwable {
            validateSdkSandboxParams(service, clientAppUid, clientAppPackage, processName);
            if (com.android.server.am.ActivityManagerService.this.mAppOpsService.checkPackage(clientAppUid, clientAppPackage) != 0) {
                throw new java.lang.IllegalArgumentException("uid does not belong to provided package");
            }
            android.content.ComponentName cn = com.android.server.am.ActivityManagerService.this.startService(com.android.server.am.ActivityManagerService.this.mContext.getIApplicationThread(), service, service.resolveTypeIfNeeded(com.android.server.am.ActivityManagerService.this.mContext.getContentResolver()), false, com.android.server.am.ActivityManagerService.this.mContext.getOpPackageName(), com.android.server.am.ActivityManagerService.this.mContext.getAttributionTag(), android.os.UserHandle.getUserId(clientAppUid), true, clientAppUid, clientAppPackage, processName);
            if (cn != null) {
                if (cn.getPackageName().equals("!")) {
                    throw new java.lang.SecurityException("Not allowed to start service " + service + " without permission " + cn.getClassName());
                }
                if (cn.getPackageName().equals("!!")) {
                    throw new java.lang.SecurityException("Unable to start service " + service + ": " + cn.getClassName());
                }
                if (cn.getPackageName().equals("?")) {
                    throw android.app.ServiceStartNotAllowedException.newInstance(false, "Not allowed to start service " + service + ": " + cn.getClassName());
                }
            }
            return cn;
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public boolean stopSdkSandboxService(android.content.Intent service, int clientAppUid, java.lang.String clientAppPackage, java.lang.String processName) throws java.lang.Throwable {
            validateSdkSandboxParams(service, clientAppUid, clientAppPackage, processName);
            int res = com.android.server.am.ActivityManagerService.this.stopService(com.android.server.am.ActivityManagerService.this.mContext.getIApplicationThread(), service, service.resolveTypeIfNeeded(com.android.server.am.ActivityManagerService.this.mContext.getContentResolver()), android.os.UserHandle.getUserId(clientAppUid), true, clientAppUid, clientAppPackage, processName);
            if (res >= 0) {
                return res != 0;
            }
            throw new java.lang.SecurityException("Not allowed to stop service " + service);
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public boolean bindSdkSandboxService(android.content.Intent service, android.content.ServiceConnection conn, int clientAppUid, android.os.IBinder clientApplicationThread, java.lang.String clientAppPackage, java.lang.String processName, int flags) throws android.os.RemoteException {
            return bindSdkSandboxServiceInternal(service, conn, clientAppUid, clientApplicationThread, clientAppPackage, processName, java.lang.Integer.toUnsignedLong(flags));
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public boolean bindSdkSandboxService(android.content.Intent service, android.content.ServiceConnection conn, int clientAppUid, android.os.IBinder clientApplicationThread, java.lang.String clientAppPackage, java.lang.String processName, android.content.Context.BindServiceFlags flags) throws android.os.RemoteException {
            return bindSdkSandboxServiceInternal(service, conn, clientAppUid, clientApplicationThread, clientAppPackage, processName, flags.getValue());
        }

        private boolean bindSdkSandboxServiceInternal(android.content.Intent service, android.content.ServiceConnection conn, int clientAppUid, android.os.IBinder clientApplicationThread, java.lang.String clientAppPackage, java.lang.String processName, long flags) throws android.os.RemoteException {
            android.app.IApplicationThread clientApplicationThreadVerified;
            validateSdkSandboxParams(service, clientAppUid, clientAppPackage, processName);
            if (com.android.server.am.ActivityManagerService.this.mAppOpsService.checkPackage(clientAppUid, clientAppPackage) != 0) {
                throw new java.lang.IllegalArgumentException("uid does not belong to provided package");
            }
            if (conn == null) {
                throw new java.lang.IllegalArgumentException("connection is null");
            }
            android.os.Handler handler = com.android.server.am.ActivityManagerService.this.mContext.getMainThreadHandler();
            if (clientApplicationThread == null) {
                clientApplicationThreadVerified = null;
            } else {
                synchronized (this) {
                    com.android.server.am.ProcessRecord rec = com.android.server.am.ActivityManagerService.this.getRecordForAppLOSP(clientApplicationThread);
                    if (rec == null) {
                        android.util.Slog.i("ActivityManager", "clientApplicationThread process not found.");
                        return false;
                    }
                    if (rec.info.uid != clientAppUid) {
                        throw new java.lang.IllegalArgumentException("clientApplicationThread does not match  client uid");
                    }
                    android.app.IApplicationThread clientApplicationThreadVerified2 = rec.getThread();
                    clientApplicationThreadVerified = clientApplicationThreadVerified2;
                }
            }
            android.app.IServiceConnection sd = com.android.server.am.ActivityManagerService.this.mContext.getServiceDispatcher(conn, handler, flags);
            service.prepareToLeaveProcess(com.android.server.am.ActivityManagerService.this.mContext);
            return com.android.server.am.ActivityManagerService.this.bindServiceInstance(com.android.server.am.ActivityManagerService.this.mContext.getIApplicationThread(), com.android.server.am.ActivityManagerService.this.mContext.getActivityToken(), service, service.resolveTypeIfNeeded(com.android.server.am.ActivityManagerService.this.mContext.getContentResolver()), sd, flags, processName, true, clientAppUid, clientAppPackage, clientApplicationThreadVerified, com.android.server.am.ActivityManagerService.this.mContext.getOpPackageName(), android.os.UserHandle.getUserId(clientAppUid)) != 0;
        }

        private void validateSdkSandboxParams(android.content.Intent service, int clientAppUid, java.lang.String clientAppPackage, java.lang.String processName) {
            if (service == null) {
                throw new java.lang.IllegalArgumentException("intent is null");
            }
            if (clientAppPackage == null) {
                throw new java.lang.IllegalArgumentException("clientAppPackage is null");
            }
            if (processName == null) {
                throw new java.lang.IllegalArgumentException("processName is null");
            }
            if (service.getComponent() == null) {
                throw new java.lang.IllegalArgumentException("service must specify explicit component");
            }
            if (!android.os.UserHandle.isApp(clientAppUid)) {
                throw new java.lang.IllegalArgumentException("uid is not within application range");
            }
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public boolean bindSdkSandboxService(android.content.Intent service, android.content.ServiceConnection conn, int clientAppUid, java.lang.String clientAppPackage, java.lang.String processName, int flags) throws android.os.RemoteException {
            return bindSdkSandboxService(service, conn, clientAppUid, (android.os.IBinder) null, clientAppPackage, processName, flags);
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public void killSdkSandboxClientAppProcess(android.os.IBinder clientApplicationThreadBinder) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ProcessRecord r = com.android.server.am.ActivityManagerService.this.getRecordForAppLOSP(clientApplicationThreadBinder);
                    if (r != null) {
                        r.killLocked("sdk sandbox died", 12, 27, true);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void onUserRemoved(int userId) {
            com.android.server.am.ActivityManagerService.this.mAtmInternal.onUserStopped(userId);
            com.android.server.am.ActivityManagerService.this.mBatteryStatsService.onUserRemoved(userId);
            if (com.android.server.am.ActivityManagerService.this.isHomeLaunchDelayable()) {
                synchronized (com.android.server.am.ActivityManagerService.this.mThemeOverlayReadyUsers) {
                    com.android.server.am.ActivityManagerService.this.mThemeOverlayReadyUsers.remove(java.lang.Integer.valueOf(userId));
                }
            }
        }

        public int startActivityAsUserEmpty(android.os.Bundle options) {
            return com.android.server.am.ActivityManagerService.this.startActivityAsUserEmpty(options);
        }

        public boolean startUserInBackground(int userId) {
            return com.android.server.am.ActivityManagerService.this.startUserInBackground(userId);
        }

        public void killForegroundAppsForUser(int userId) {
            java.util.ArrayList<com.android.server.am.ProcessRecord> procs = new java.util.ArrayList<>();
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    int numOfProcs = com.android.server.am.ActivityManagerService.this.mProcessList.getProcessNamesLOSP().getMap().size();
                    for (int ip = 0; ip < numOfProcs; ip++) {
                        android.util.SparseArray<com.android.server.am.ProcessRecord> apps = (android.util.SparseArray) com.android.server.am.ActivityManagerService.this.mProcessList.getProcessNamesLOSP().getMap().valueAt(ip);
                        int NA = apps.size();
                        for (int ia = 0; ia < NA; ia++) {
                            com.android.server.am.ProcessRecord app = apps.valueAt(ia);
                            if (!app.isPersistent() && (app.isRemoved() || (app.userId == userId && app.mState.hasForegroundActivities()))) {
                                procs.add(app);
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            int numOfProcs2 = procs.size();
            if (numOfProcs2 > 0) {
                com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    for (int i = 0; i < numOfProcs2; i++) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mProcessList.removeProcessLocked(procs.get(i), false, true, 13, 9, "kill all fg");
                        } catch (java.lang.Throwable th2) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th2;
                        }
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            }
        }

        public void setPendingIntentAllowlistDuration(android.content.IIntentSender target, android.os.IBinder allowlistToken, long duration, int type, int reasonCode, java.lang.String reason) {
            com.android.server.am.ActivityManagerService.this.mPendingIntentController.setPendingIntentAllowlistDuration(target, allowlistToken, duration, type, reasonCode, reason);
        }

        public int getPendingIntentFlags(android.content.IIntentSender target) {
            return com.android.server.am.ActivityManagerService.this.mPendingIntentController.getPendingIntentFlags(target);
        }

        public int[] getStartedUserIds() {
            return com.android.server.am.ActivityManagerService.this.mUserController.getStartedUserArray();
        }

        public void setPendingIntentAllowBgActivityStarts(android.content.IIntentSender target, android.os.IBinder allowlistToken, int flags) {
            if (!(target instanceof com.android.server.am.PendingIntentRecord)) {
                android.util.Slog.w("ActivityManager", "setPendingIntentAllowBgActivityStarts(): not a PendingIntentRecord: " + target);
                return;
            }
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    ((com.android.server.am.PendingIntentRecord) target).setAllowBgActivityStarts(allowlistToken, flags);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void clearPendingIntentAllowBgActivityStarts(android.content.IIntentSender target, android.os.IBinder allowlistToken) {
            if (!(target instanceof com.android.server.am.PendingIntentRecord)) {
                android.util.Slog.w("ActivityManager", "clearPendingIntentAllowBgActivityStarts(): not a PendingIntentRecord: " + target);
                return;
            }
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    ((com.android.server.am.PendingIntentRecord) target).clearAllowBgActivityStarts(allowlistToken);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void setDeviceIdleAllowlist(int[] allAppids, int[] exceptIdleAppids) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            com.android.server.am.ActivityManagerService.this.mDeviceIdleAllowlist = allAppids;
                            com.android.server.am.ActivityManagerService.this.mDeviceIdleExceptIdleAllowlist = exceptIdleAppids;
                            com.android.server.am.ActivityManagerService.this.mAppRestrictionController.setDeviceIdleAllowlist(allAppids, exceptIdleAppids);
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                } catch (java.lang.Throwable th2) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void updateDeviceIdleTempAllowlist(int[] appids, int changingUid, boolean adding, long durationMs, int type, int reasonCode, java.lang.String reason, int callingUid) throws java.lang.Throwable {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    try {
                        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
                        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                        synchronized (activityManagerGlobalLock) {
                            try {
                                if (appids != null) {
                                    try {
                                        com.android.server.am.ActivityManagerService.this.mDeviceIdleTempAllowlist = appids;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                        throw th;
                                    }
                                }
                                if (!adding) {
                                    com.android.server.am.ActivityManagerService.this.mFgsStartTempAllowList.removeUid(changingUid);
                                } else if (type == 0) {
                                    com.android.server.am.ActivityManagerService.this.mFgsStartTempAllowList.add(changingUid, durationMs, new com.android.server.am.ActivityManagerService.FgsTempAllowListItem(durationMs, reasonCode, reason, callingUid));
                                }
                                com.android.server.am.ActivityManagerService.this.setUidTempAllowlistStateLSP(changingUid, adding);
                                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        public int getUidProcessState(int uid) {
            return com.android.server.am.ActivityManagerService.this.getUidState(uid);
        }

        public java.util.Map<java.lang.Integer, java.lang.String> getProcessesWithPendingBindMounts(int userId) {
            return com.android.server.am.ActivityManagerService.this.mProcessList.getProcessesWithPendingBindMounts(userId);
        }

        public boolean isSystemReady() {
            return com.android.server.am.ActivityManagerService.this.mSystemReady;
        }

        public void enforceBroadcastOptionsPermissions(android.os.Bundle options, int callingUid) {
            com.android.server.am.ActivityManagerService.this.enforceBroadcastOptionPermissionsInternal(options, callingUid);
        }

        public java.lang.String getPackageNameByPid(int pid) {
            synchronized (com.android.server.am.ActivityManagerService.this.mPidsSelfLocked) {
                com.android.server.am.ProcessRecord app = com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.get(pid);
                if (app == null || app.info == null) {
                    return null;
                }
                return app.info.packageName;
            }
        }

        public void setHasOverlayUi(int pid, boolean hasOverlayUi) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    synchronized (com.android.server.am.ActivityManagerService.this.mPidsSelfLocked) {
                        com.android.server.am.ProcessRecord pr = com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.get(pid);
                        if (pr == null) {
                            android.util.Slog.w("ActivityManager", "setHasOverlayUi called on unknown pid: " + pid);
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        } else {
                            if (pr.mState.hasOverlayUi() == hasOverlayUi) {
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            pr.mState.setHasOverlayUi(hasOverlayUi);
                            com.android.server.am.ActivityManagerService.this.updateOomAdjLocked(pr, 9);
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        public void notifyNetworkPolicyRulesUpdated(int uid, long procStateSeq) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_NETWORK, "Got update from NPMS for uid: " + uid + " seq: " + procStateSeq);
            }
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    com.android.server.am.UidRecord record = com.android.server.am.ActivityManagerService.this.mProcessList.getUidRecordLOSP(uid);
                    if (record == null) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                            android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_NETWORK, "No active uidRecord for uid: " + uid + " procStateSeq: " + procStateSeq);
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        return;
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    synchronized (record.networkStateLock) {
                        if (record.lastNetworkUpdatedProcStateSeq >= procStateSeq) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_NETWORK, "procStateSeq: " + procStateSeq + " has already been handled for uid: " + uid);
                            }
                            return;
                        }
                        record.lastNetworkUpdatedProcStateSeq = procStateSeq;
                        if (record.procStateSeqWaitingForNetwork != 0 && procStateSeq >= record.procStateSeqWaitingForNetwork) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                                android.util.Slog.d(com.android.server.am.ActivityManagerService.TAG_NETWORK, "Notifying all blocking threads for uid: " + uid + ", procStateSeq: " + procStateSeq + ", procStateSeqWaitingForNetwork: " + record.procStateSeqWaitingForNetwork);
                            }
                            record.networkStateLock.notifyAll();
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
        }

        public void onUidBlockedReasonsChanged(int uid, int blockedReasons) {
            synchronized (com.android.server.am.ActivityManagerService.this.mUidNetworkBlockedReasons) {
                if (blockedReasons == 0) {
                    com.android.server.am.ActivityManagerService.this.mUidNetworkBlockedReasons.delete(uid);
                } else {
                    com.android.server.am.ActivityManagerService.this.mUidNetworkBlockedReasons.put(uid, blockedReasons);
                }
            }
        }

        public boolean isRuntimeRestarted() {
            return com.android.server.am.ActivityManagerService.this.mSystemServiceManager.isRuntimeRestarted();
        }

        public boolean canStartMoreUsers() {
            return com.android.server.am.ActivityManagerService.this.mUserController.canStartMoreUsers();
        }

        public void setSwitchingFromSystemUserMessage(java.lang.String switchingFromSystemUserMessage) {
            com.android.server.am.ActivityManagerService.this.mUserController.setSwitchingFromSystemUserMessage(switchingFromSystemUserMessage);
        }

        public void setSwitchingToSystemUserMessage(java.lang.String switchingToSystemUserMessage) {
            com.android.server.am.ActivityManagerService.this.mUserController.setSwitchingToSystemUserMessage(switchingToSystemUserMessage);
        }

        public int getMaxRunningUsers() {
            return com.android.server.am.ActivityManagerService.this.mUserController.getMaxRunningUsers();
        }

        public boolean isUidActive(int uid) {
            boolean zIsUidActiveLOSP;
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    zIsUidActiveLOSP = com.android.server.am.ActivityManagerService.this.isUidActiveLOSP(uid);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            return zIsUidActiveLOSP;
        }

        public java.util.List<android.app.ProcessMemoryState> getMemoryStateForProcesses() {
            java.util.List<android.app.ProcessMemoryState> processMemoryStates = new java.util.ArrayList<>();
            synchronized (com.android.server.am.ActivityManagerService.this.mPidsSelfLocked) {
                int size = com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.size();
                for (int i = 0; i < size; i++) {
                    com.android.server.am.ProcessRecord r = com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.valueAt(i);
                    processMemoryStates.add(new android.app.ProcessMemoryState(r.uid, r.getPid(), r.processName, r.mState.getCurAdj(), r.mServices.hasForegroundServices(), r.mProfile.getCurrentHostingComponentTypes(), r.mProfile.getHistoricalHostingComponentTypes()));
                }
            }
            return processMemoryStates;
        }

        public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, int allowMode, java.lang.String name, java.lang.String callerPackage) {
            return com.android.server.am.ActivityManagerService.this.mUserController.handleIncomingUser(callingPid, callingUid, userId, allowAll, allowMode, name, callerPackage);
        }

        public void enforceCallingPermission(java.lang.String permission, java.lang.String func) {
            com.android.server.am.ActivityManagerService.this.enforceCallingPermission(permission, func);
        }

        public android.util.Pair<java.lang.Integer, java.lang.Integer> getCurrentAndTargetUserIds() {
            return com.android.server.am.ActivityManagerService.this.mUserController.getCurrentAndTargetUserIds();
        }

        public int getCurrentUserId() {
            return com.android.server.am.ActivityManagerService.this.mUserController.getCurrentUserId();
        }

        public boolean isUserRunning(int userId, int flags) {
            return com.android.server.am.ActivityManagerService.this.mUserController.isUserRunning(userId, flags);
        }

        public void trimApplications() {
            com.android.server.am.ActivityManagerService.this.trimApplications(true, 1);
        }

        public void killProcessesForRemovedTask(java.util.ArrayList<java.lang.Object> procsToKill) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                for (int i = 0; i < procsToKill.size(); i++) {
                    try {
                        com.android.server.wm.WindowProcessController wpc = (com.android.server.wm.WindowProcessController) procsToKill.get(i);
                        com.android.server.am.ProcessRecord pr = (com.android.server.am.ProcessRecord) wpc.mOwner;
                        if (android.app.ActivityManager.isProcStateBackground(pr.mState.getSetProcState()) && pr.mReceivers.numberOfCurReceivers() == 0 && !pr.mState.hasStartedServices()) {
                            pr.killLocked("remove task", 10, 22, true);
                            if (pr.getThread() == null) {
                                android.util.Slog.v("ActivityManager", "the actual proc is null, call handleAppDiedLocked for " + pr.toString());
                                com.android.server.am.ActivityManagerService.this.handleAppDiedLocked(pr, pr.getPid(), false, true, false);
                            }
                        } else {
                            pr.setWaitingToKill("remove task");
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void killProcess(java.lang.String processName, int uid, java.lang.String reason) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ProcessRecord proc = com.android.server.am.ActivityManagerService.this.getProcessRecordLocked(processName, uid);
                    if (proc != null) {
                        com.android.server.am.ActivityManagerService.this.mProcessList.removeProcessLocked(proc, false, true, 13, reason);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public boolean hasRunningActivity(final int uid, final java.lang.String packageName) {
            boolean z;
            if (packageName == null) {
                return false;
            }
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    z = com.android.server.am.ActivityManagerService.this.mProcessList.searchEachLruProcessesLOSP(true, new java.util.function.Function() { // from class: com.android.server.am.ActivityManagerService$LocalService$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return com.android.server.am.ActivityManagerService.LocalService.lambda$hasRunningActivity$0(uid, packageName, (com.android.server.am.ProcessRecord) obj);
                        }
                    }) != null;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            return z;
        }

        static /* synthetic */ java.lang.Boolean lambda$hasRunningActivity$0(int uid, java.lang.String packageName, com.android.server.am.ProcessRecord app) {
            if (app.uid == uid && app.getWindowProcessController().hasRunningActivity(packageName)) {
                return java.lang.Boolean.TRUE;
            }
            return null;
        }

        public void updateOomAdj(int oomAdjReason) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.updateOomAdjLocked(oomAdjReason);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void updateCpuStats() {
            com.android.server.am.ActivityManagerService.this.updateCpuStats();
        }

        public void updateBatteryStats(android.content.ComponentName activity, int uid, int userId, boolean resumed) {
            com.android.server.am.ActivityManagerService.this.updateBatteryStats(activity, uid, userId, resumed);
        }

        public void updateActivityUsageStats(android.content.ComponentName activity, int userId, int event, android.os.IBinder appToken, android.content.ComponentName taskRoot, android.app.assist.ActivityId activityId) {
            com.android.server.am.ActivityManagerService.this.updateActivityUsageStats(activity, userId, event, appToken, taskRoot, activityId);
        }

        public void updateForegroundTimeIfOnBattery(java.lang.String packageName, int uid, long cpuTimeDiff) {
            com.android.server.am.ActivityManagerService.this.mBatteryStatsService.updateForegroundTimeIfOnBattery(packageName, uid, cpuTimeDiff);
        }

        public void sendForegroundProfileChanged(int userId) {
            com.android.server.am.ActivityManagerService.this.mUserController.sendForegroundProfileChanged(userId);
        }

        public boolean shouldConfirmCredentials(int userId) {
            return com.android.server.am.ActivityManagerService.this.mUserController.shouldConfirmCredentials(userId);
        }

        public void noteAlarmFinish(android.app.PendingIntent ps, android.os.WorkSource workSource, int sourceUid, java.lang.String tag) {
            com.android.server.am.ActivityManagerService.this.noteAlarmFinish(ps != null ? ps.getTarget() : null, workSource, sourceUid, tag);
        }

        public void noteAlarmStart(android.app.PendingIntent ps, android.os.WorkSource workSource, int sourceUid, java.lang.String tag) {
            com.android.server.am.ActivityManagerService.this.noteAlarmStart(ps != null ? ps.getTarget() : null, workSource, sourceUid, tag);
        }

        public void noteWakeupAlarm(android.app.PendingIntent ps, android.os.WorkSource workSource, int sourceUid, java.lang.String sourcePkg, java.lang.String tag) {
            com.android.server.am.ActivityManagerService.this.noteWakeupAlarm(ps != null ? ps.getTarget() : null, workSource, sourceUid, sourcePkg, tag);
        }

        public boolean isAppStartModeDisabled(int uid, java.lang.String packageName) {
            return com.android.server.am.ActivityManagerService.this.isAppStartModeDisabled(uid, packageName);
        }

        public int[] getCurrentProfileIds() {
            return com.android.server.am.ActivityManagerService.this.mUserController.getCurrentProfileIds();
        }

        public android.content.pm.UserInfo getCurrentUser() {
            return com.android.server.am.ActivityManagerService.this.mUserController.getCurrentUser();
        }

        public void ensureNotSpecialUser(int userId) {
            com.android.server.am.ActivityManagerService.this.mUserController.ensureNotSpecialUser(userId);
        }

        public boolean isCurrentProfile(int userId) {
            return com.android.server.am.ActivityManagerService.this.mUserController.isCurrentProfile(userId);
        }

        public boolean hasStartedUserState(int userId) {
            return com.android.server.am.ActivityManagerService.this.mUserController.hasStartedUserState(userId);
        }

        public void finishUserSwitch(java.lang.Object uss) {
            com.android.server.am.ActivityManagerService.this.mUserController.finishUserSwitch((com.android.server.am.UserState) uss);
        }

        public void scheduleAppGcs() {
            synchronized (com.android.server.am.ActivityManagerService.this.mAppProfiler.mProfilerLock) {
                com.android.server.am.ActivityManagerService.this.mAppProfiler.scheduleAppGcsLPf();
            }
        }

        public int getTaskIdForActivity(android.os.IBinder token, boolean onlyRoot) {
            return com.android.server.am.ActivityManagerService.this.getTaskForActivity(token, onlyRoot);
        }

        public android.content.pm.ActivityPresentationInfo getActivityPresentationInfo(android.os.IBinder token) {
            android.app.ActivityClient ac = android.app.ActivityClient.getInstance();
            return new android.content.pm.ActivityPresentationInfo(ac.getTaskForActivity(token, false), ac.getDisplayId(token), com.android.server.am.ActivityManagerService.this.mAtmInternal.getActivityName(token));
        }

        public void setBooting(boolean booting) {
            com.android.server.am.ActivityManagerService.this.mBooting = booting;
        }

        public boolean isBooting() {
            return com.android.server.am.ActivityManagerService.this.mBooting;
        }

        public void setBooted(boolean booted) {
            com.android.server.am.ActivityManagerService.this.mBooted = booted;
        }

        public boolean isBooted() {
            return com.android.server.am.ActivityManagerService.this.mBooted;
        }

        public void finishBooting() {
            com.android.server.am.ActivityManagerService.this.finishBooting();
        }

        public void tempAllowlistForPendingIntent(int callerPid, int callerUid, int targetUid, long duration, int type, int reasonCode, java.lang.String reason) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.tempAllowlistForPendingIntentLocked(callerPid, callerUid, targetUid, duration, type, reasonCode, reason);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public int broadcastIntentInPackage(java.lang.String packageName, java.lang.String featureId, int uid, int realCallingUid, int realCallingPid, android.content.Intent intent, java.lang.String resolvedType, android.app.IApplicationThread resultToThread, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, java.lang.String requiredPermission, android.os.Bundle bOptions, boolean serialized, boolean sticky, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges, int[] broadcastAllowList) throws java.lang.Throwable {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    try {
                        com.android.server.am.ProcessRecord resultToApp = com.android.server.am.ActivityManagerService.this.getRecordForAppLOSP(resultToThread);
                        int iBroadcastIntentInPackage = com.android.server.am.ActivityManagerService.this.broadcastIntentInPackage(packageName, featureId, uid, realCallingUid, realCallingPid, intent, resolvedType, resultToApp, resultTo, resultCode, resultData, resultExtras, requiredPermission, bOptions, serialized, sticky, userId, backgroundStartPrivileges, broadcastAllowList);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        return iBroadcastIntentInPackage;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        public int broadcastIntent(android.content.Intent intent, android.content.IIntentReceiver resultTo, java.lang.String[] requiredPermissions, boolean serialized, int userId, int[] appIdAllowList, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, android.os.Bundle bOptions) throws java.lang.Throwable {
            android.content.Intent intent2;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    try {
                        intent2 = intent;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                    try {
                        android.content.Intent intent3 = com.android.server.am.ActivityManagerService.this.verifyBroadcastLocked(intent2);
                        int callingPid = android.os.Binder.getCallingPid();
                        int callingUid = android.os.Binder.getCallingUid();
                        long origId = android.os.Binder.clearCallingIdentity();
                        try {
                            int iBroadcastIntentLocked = com.android.server.am.ActivityManagerService.this.broadcastIntentLocked(null, null, null, intent3, null, null, resultTo, 0, null, null, requiredPermissions, null, null, -1, bOptions, serialized, false, callingPid, callingUid, callingUid, callingPid, userId, android.app.BackgroundStartPrivileges.NONE, appIdAllowList, filterExtrasForReceiver);
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return iBroadcastIntentLocked;
                        } finally {
                            android.os.Binder.restoreCallingIdentity(origId);
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    intent2 = intent;
                }
            }
        }

        public int broadcastIntentWithCallback(android.content.Intent intent, android.content.IIntentReceiver resultTo, java.lang.String[] requiredPermissions, int userId, int[] appIdAllowList, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, android.os.Bundle bOptions) {
            return broadcastIntent(intent, resultTo, requiredPermissions, false, userId, appIdAllowList, filterExtrasForReceiver, bOptions);
        }

        public android.content.ComponentName startServiceInPackage(int uid, android.content.Intent service, java.lang.String resolvedType, boolean fgRequired, java.lang.String callingPackage, java.lang.String callingFeatureId, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges) throws java.lang.Throwable {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_SERVICE) {
                android.util.Slog.v(com.android.server.am.ActivityManagerService.TAG_SERVICE, "startServiceInPackage: " + service + " type=" + resolvedType);
            }
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                if (android.os.Trace.isTagEnabled(64L)) {
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                    try {
                        android.os.Trace.traceBegin(64L, "startServiceInPackage: intent=" + service + ", caller=" + callingPackage + ", fgRequired=" + fgRequired);
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Trace.traceEnd(64L);
                        android.os.Binder.restoreCallingIdentity(origId);
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                try {
                    try {
                        synchronized (activityManagerService) {
                            try {
                                android.content.ComponentName res = com.android.server.am.ActivityManagerService.this.mServices.startServiceLocked(null, service, resolvedType, -1, uid, fgRequired, callingPackage, callingFeatureId, userId, backgroundStartPrivileges);
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                android.os.Trace.traceEnd(64L);
                                android.os.Binder.restoreCallingIdentity(origId);
                                return res;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                throw th;
                            }
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                        android.os.Trace.traceEnd(64L);
                        android.os.Binder.restoreCallingIdentity(origId);
                        throw th;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
        }

        public void disconnectActivityFromServices(java.lang.Object connectionHolder) {
            final com.android.server.wm.ActivityServiceConnectionsHolder holder = (com.android.server.wm.ActivityServiceConnectionsHolder) connectionHolder;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
                    com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            holder.forEachConnection(new java.util.function.Consumer() { // from class: com.android.server.am.ActivityManagerService$LocalService$$ExternalSyntheticLambda2
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    this.f$0.lambda$disconnectActivityFromServices$1(holder, obj);
                                }
                            });
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                } catch (java.lang.Throwable th2) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$disconnectActivityFromServices$1(com.android.server.wm.ActivityServiceConnectionsHolder holder, java.lang.Object cr) {
            com.android.server.am.ActivityManagerService.this.mServices.removeConnectionLocked((com.android.server.am.ConnectionRecord) cr, null, holder, false);
        }

        public void cleanUpServices(int userId, android.content.ComponentName component, android.content.Intent baseIntent) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.mServices.cleanUpServices(userId, component, baseIntent);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public android.content.pm.ActivityInfo getActivityInfoForUser(android.content.pm.ActivityInfo aInfo, int userId) {
            return com.android.server.am.ActivityManagerService.this.getActivityInfoForUser(aInfo, userId);
        }

        public void ensureBootCompleted() {
            com.android.server.am.ActivityManagerService.this.ensureBootCompleted();
        }

        public void updateOomLevelsForDisplay(int displayId) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (com.android.server.am.ActivityManagerService.this.mWindowManager != null) {
                        com.android.server.am.ActivityManagerService.this.mProcessList.applyDisplaySize(com.android.server.am.ActivityManagerService.this.mWindowManager);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public boolean isActivityStartsLoggingEnabled() {
            return com.android.server.am.ActivityManagerService.this.mConstants.mFlagActivityStartsLoggingEnabled;
        }

        public boolean isBackgroundActivityStartsEnabled() {
            return com.android.server.am.ActivityManagerService.this.mConstants.mFlagBackgroundActivityStartsEnabled;
        }

        public android.app.BackgroundStartPrivileges getBackgroundStartPrivileges(int uid) {
            return com.android.server.am.ActivityManagerService.this.getBackgroundStartPrivileges(uid);
        }

        public boolean canScheduleUserInitiatedJobs(int uid, int pid, java.lang.String pkgName) {
            return com.android.server.am.ActivityManagerService.this.canScheduleUserInitiatedJobs(uid, pid, pkgName);
        }

        public void reportCurKeyguardUsageEvent(boolean keyguardShowing) {
            int i;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            if (keyguardShowing) {
                i = 17;
            } else {
                i = 18;
            }
            activityManagerService.reportGlobalUsageEvent(i);
        }

        public void monitor() {
            com.android.server.am.ActivityManagerService.this.monitor();
        }

        public long inputDispatchingTimedOut(int pid, boolean aboveSystem, com.android.internal.os.TimeoutRecord timeoutRecord) {
            return com.android.server.am.ActivityManagerService.this.inputDispatchingTimedOut(pid, aboveSystem, timeoutRecord);
        }

        public boolean inputDispatchingTimedOut(java.lang.Object proc, java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo aInfo, java.lang.String parentShortComponentName, java.lang.Object parentProc, boolean aboveSystem, com.android.internal.os.TimeoutRecord timeoutRecord) {
            return com.android.server.am.ActivityManagerService.this.inputDispatchingTimedOut((com.android.server.am.ProcessRecord) proc, activityShortComponentName, aInfo, parentShortComponentName, (com.android.server.wm.WindowProcessController) parentProc, aboveSystem, timeoutRecord);
        }

        public void inputDispatchingResumed(int pid) {
            com.android.server.am.ProcessRecord proc;
            synchronized (com.android.server.am.ActivityManagerService.this.mPidsSelfLocked) {
                proc = com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.get(pid);
            }
            if (proc != null) {
                com.android.server.am.ActivityManagerService.this.mAppErrors.handleDismissAnrDialogs(proc);
            }
        }

        public void rescheduleAnrDialog(java.lang.Object data) {
            android.os.Message msg = android.os.Message.obtain();
            msg.what = 2;
            msg.obj = (com.android.server.am.AppNotRespondingDialog.Data) data;
            com.android.server.am.ActivityManagerService.this.mUiHandler.sendMessageDelayed(msg, android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        }

        public void broadcastGlobalConfigurationChanged(int changes, boolean initLocale) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    android.content.Intent intent = new android.content.Intent("android.intent.action.CONFIGURATION_CHANGED");
                    intent.addFlags(1881145344);
                    android.os.Bundle configChangedOptions = new android.app.BroadcastOptions().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
                    com.android.server.am.ActivityManagerService.this.broadcastIntentLocked(null, null, null, intent, null, null, 0, null, null, null, null, null, -1, configChangedOptions, false, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), -1);
                    if ((changes & 4) != 0) {
                        android.content.Intent intent2 = new android.content.Intent("android.intent.action.LOCALE_CHANGED");
                        intent2.addFlags(18876416);
                        if (initLocale || !com.android.server.am.ActivityManagerService.this.mProcessesReady) {
                            intent2.addFlags(1073741824);
                        }
                        android.app.BroadcastOptions bOptions = android.app.BroadcastOptions.makeBasic();
                        bOptions.setTemporaryAppAllowlist(com.android.server.am.ActivityManagerService.this.mInternal.getBootTimeTempAllowListDuration(), 0, 206, "");
                        bOptions.setDeliveryGroupPolicy(1);
                        bOptions.setDeferralPolicy(2);
                        com.android.server.am.ActivityManagerService.this.broadcastIntentLocked(null, null, null, intent2, null, null, 0, null, null, null, null, null, -1, bOptions.toBundle(), false, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), -1);
                    }
                    if (!initLocale && isSplitConfigurationChange(changes)) {
                        android.content.Intent intent3 = new android.content.Intent("android.intent.action.SPLIT_CONFIGURATION_CHANGED");
                        intent3.addFlags(android.hardware.audio.common.V2_0.AudioFormat.APTX_HD);
                        java.lang.String[] permissions = {"android.permission.INSTALL_PACKAGES"};
                        com.android.server.am.ActivityManagerService.this.broadcastIntentLocked(null, null, null, intent3, null, null, 0, null, null, permissions, null, null, -1, null, false, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), -1);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        private boolean isSplitConfigurationChange(int configDiff) {
            return (configDiff & 4100) != 0;
        }

        public void broadcastCloseSystemDialogs(java.lang.String reason) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    android.content.Intent intent = new android.content.Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
                    intent.addFlags(1342177280);
                    if (reason != null) {
                        intent.putExtra(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, reason);
                    }
                    android.app.BroadcastOptions options = new android.app.BroadcastOptions().setDeliveryGroupPolicy(1).setDeferralPolicy(2);
                    if (reason != null) {
                        options.setDeliveryGroupMatchingKey("android.intent.action.CLOSE_SYSTEM_DIALOGS", reason);
                    }
                    com.android.server.am.ActivityManagerService.this.broadcastIntentLocked(null, null, null, intent, null, null, 0, null, null, null, null, null, -1, options.toBundle(), false, false, -1, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), -1);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void appNotResponding(java.lang.String processName, int uid, com.android.internal.os.TimeoutRecord timeoutRecord) {
            com.android.server.am.ActivityManagerService.this.appNotResponding(processName, uid, timeoutRecord);
        }

        public void killAllBackgroundProcessesExcept(int minTargetSdk, int maxProcState) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.killAllBackgroundProcessesExcept(minTargetSdk, maxProcState);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void startProcess(java.lang.String processName, android.content.pm.ApplicationInfo info, boolean knownToBeDead, boolean isTop, java.lang.String hostingType, android.content.ComponentName hostingName) throws java.lang.Throwable {
            try {
                if (android.os.Trace.isTagEnabled(64L)) {
                    android.os.Trace.traceBegin(64L, "startProcess:" + processName);
                }
                com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                try {
                    try {
                        synchronized (activityManagerService) {
                            try {
                                com.android.server.am.HostingRecord hostingRecord = new com.android.server.am.HostingRecord(hostingType, hostingName, isTop);
                                com.android.server.am.ActivityManagerService.this.getProcessRecordLocked(processName, info.uid);
                                if (com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt.isLogToolRun()) {
                                    android.util.Slog.d("ActivityManager", "startProcessLocked:" + processName);
                                }
                                com.android.server.am.ActivityManagerService.this.startProcessLocked(processName, info, knownToBeDead, 0, hostingRecord, 1, false, false);
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                android.os.Trace.traceEnd(64L);
                            } catch (java.lang.Throwable th) {
                                th = th;
                                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                                throw th;
                            }
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    android.os.Trace.traceEnd(64L);
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }

        public void setDebugFlagsForStartingActivity(android.content.pm.ActivityInfo aInfo, int startFlags, android.app.ProfilerInfo profilerInfo, java.lang.Object wmLock) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    synchronized (wmLock) {
                        if ((startFlags & 2) != 0) {
                            boolean suspend = (startFlags & 16) != 0;
                            try {
                                com.android.server.am.ActivityManagerService.this.setDebugApp(aInfo.processName, true, false, suspend);
                            } catch (java.lang.Throwable th) {
                                throw th;
                            }
                        }
                        if ((startFlags & 8) != 0) {
                            com.android.server.am.ActivityManagerService.this.setNativeDebuggingAppLocked(aInfo.applicationInfo, aInfo.processName);
                        }
                        if ((startFlags & 4) != 0) {
                            com.android.server.am.ActivityManagerService.this.setTrackAllocationApp(aInfo.applicationInfo, aInfo.processName);
                        }
                        if (profilerInfo != null) {
                            com.android.server.am.ActivityManagerService.this.setProfileApp(aInfo.applicationInfo, aInfo.processName, profilerInfo, null);
                        }
                        wmLock.notify();
                    }
                } catch (java.lang.Throwable th2) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public int getStorageMountMode(int pid, int uid) {
            int mountMode;
            if (uid == 2000 || uid == 0) {
                return 1;
            }
            synchronized (com.android.server.am.ActivityManagerService.this.mPidsSelfLocked) {
                com.android.server.am.ProcessRecord pr = com.android.server.am.ActivityManagerService.this.mPidsSelfLocked.get(pid);
                mountMode = pr == null ? 0 : pr.getMountMode();
            }
            return mountMode;
        }

        public boolean isAppForeground(int uid) {
            return com.android.server.am.ActivityManagerService.this.isAppForeground(uid);
        }

        public boolean isAppBad(java.lang.String processName, int uid) {
            return com.android.server.am.ActivityManagerService.this.isAppBad(processName, uid);
        }

        public void clearPendingBackup(int userId) {
            com.android.server.am.ActivityManagerService.this.clearPendingBackup(userId);
        }

        public void prepareForPossibleShutdown() {
            com.android.server.am.ActivityManagerService.this.prepareForPossibleShutdown();
        }

        public boolean hasRunningForegroundService(int uid, int foregroundServicetype) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.UidRecord uidRec = com.android.server.am.ActivityManagerService.this.mProcessList.mActiveUids.get(uid);
                    if (uidRec == null) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    for (int i = uidRec.getNumOfProcs() - 1; i >= 0; i--) {
                        com.android.server.am.ProcessRecord app = uidRec.getProcessRecordByIndex(i);
                        if (app.mServices.containsAnyForegroundServiceTypes(foregroundServicetype)) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        public boolean hasForegroundServiceNotification(java.lang.String pkg, int userId, java.lang.String channelId) {
            boolean zHasForegroundServiceNotificationLocked;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    zHasForegroundServiceNotificationLocked = com.android.server.am.ActivityManagerService.this.mServices.hasForegroundServiceNotificationLocked(pkg, userId, channelId);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return zHasForegroundServiceNotificationLocked;
        }

        public android.app.ActivityManagerInternal.ServiceNotificationPolicy applyForegroundServiceNotification(android.app.Notification notification, java.lang.String tag, int id, java.lang.String pkg, int userId) {
            android.app.ActivityManagerInternal.ServiceNotificationPolicy serviceNotificationPolicyApplyForegroundServiceNotificationLocked;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    serviceNotificationPolicyApplyForegroundServiceNotificationLocked = com.android.server.am.ActivityManagerService.this.mServices.applyForegroundServiceNotificationLocked(notification, tag, id, pkg, userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return serviceNotificationPolicyApplyForegroundServiceNotificationLocked;
        }

        public void onForegroundServiceNotificationUpdate(boolean shown, android.app.Notification notification, int id, java.lang.String pkg, int userId) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.mServices.onForegroundServiceNotificationUpdateLocked(shown, notification, id, pkg, userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void stopAppForUser(java.lang.String pkg, int userId) throws java.lang.Throwable {
            com.android.server.am.ActivityManagerService.this.stopAppForUserInternal(pkg, userId);
        }

        public void registerProcessObserver(android.app.IProcessObserver processObserver) {
            com.android.server.am.ActivityManagerService.this.registerProcessObserver(processObserver);
        }

        public void unregisterProcessObserver(android.app.IProcessObserver processObserver) {
            com.android.server.am.ActivityManagerService.this.unregisterProcessObserver(processObserver);
        }

        public int getInstrumentationSourceUid(int uid) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.ActivityManagerService.this.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    for (int i = com.android.server.am.ActivityManagerService.this.mActiveInstrumentation.size() - 1; i >= 0; i--) {
                        com.android.server.am.ActiveInstrumentation activeInst = com.android.server.am.ActivityManagerService.this.mActiveInstrumentation.get(i);
                        if (!activeInst.mFinished && activeInst.mTargetInfo != null && activeInst.mTargetInfo.uid == uid) {
                            int i2 = activeInst.mSourceUid;
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            return i2;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return -1;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
        }

        public void setDeviceOwnerUid(int uid) {
            com.android.server.am.ActivityManagerService.this.mDeviceOwnerUid = uid;
        }

        public boolean isDeviceOwner(int uid) {
            int cachedUid = com.android.server.am.ActivityManagerService.this.mDeviceOwnerUid;
            return uid >= 0 && cachedUid == uid;
        }

        public void setProfileOwnerUid(android.util.ArraySet<java.lang.Integer> profileOwnerUids) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.mProfileOwnerUids = profileOwnerUids;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public boolean isProfileOwner(int uid) {
            boolean z;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    z = com.android.server.am.ActivityManagerService.this.mProfileOwnerUids != null && com.android.server.am.ActivityManagerService.this.mProfileOwnerUids.indexOf(java.lang.Integer.valueOf(uid)) >= 0;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        public void setCompanionAppUids(int userId, java.util.Set<java.lang.Integer> companionAppUids) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.mCompanionAppUidsMap.put(java.lang.Integer.valueOf(userId), companionAppUids);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public boolean isAssociatedCompanionApp(int userId, int uid) {
            java.util.Set<java.lang.Integer> allUids = (java.util.Set) com.android.server.am.ActivityManagerService.this.mCompanionAppUidsMap.get(java.lang.Integer.valueOf(userId));
            if (allUids == null) {
                return false;
            }
            return allUids.contains(java.lang.Integer.valueOf(uid));
        }

        public void addPendingTopUid(int uid, int pid, android.app.IApplicationThread thread) {
            boolean isNewPending = com.android.server.am.ActivityManagerService.this.mPendingStartActivityUids.add(uid, pid);
            if (isNewPending) {
                com.android.server.am.ActivityManagerService.this.mOomAdjuster.mCachedAppOptimizer.unfreezeProcess(pid, 1);
            }
            if (isNewPending && com.android.server.am.ActivityManagerService.this.mNetworkPolicyUidObserver != null) {
                try {
                    long procStateSeq = com.android.server.am.ActivityManagerService.this.mProcessList.getNextProcStateSeq();
                    com.android.server.am.ActivityManagerService.this.mNetworkPolicyUidObserver.onUidStateChanged(uid, 2, procStateSeq, 127);
                    if (thread != null && shouldWaitForNetworkRulesUpdate(uid)) {
                        thread.setNetworkBlockSeq(procStateSeq);
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.d("ActivityManager", "Error calling setNetworkBlockSeq", e);
                }
            }
        }

        private boolean shouldWaitForNetworkRulesUpdate(int uid) {
            boolean z;
            synchronized (com.android.server.am.ActivityManagerService.this.mUidNetworkBlockedReasons) {
                z = false;
                int uidBlockedReasons = com.android.server.am.ActivityManagerService.this.mUidNetworkBlockedReasons.get(uid, 0);
                if (uidBlockedReasons != 0 && com.android.server.net.NetworkPolicyManagerInternal.updateBlockedReasonsWithProcState(uidBlockedReasons, 2) == 0) {
                    z = true;
                }
            }
            return z;
        }

        public void deletePendingTopUid(int uid, long nowElapsed) {
            com.android.server.am.ActivityManagerService.this.mPendingStartActivityUids.delete(uid, nowElapsed);
        }

        public boolean isPendingTopUid(int uid) {
            return com.android.server.am.ActivityManagerService.this.mPendingStartActivityUids.isPendingTopUid(uid);
        }

        public android.content.Intent getIntentForIntentSender(android.content.IIntentSender sender) {
            return com.android.server.am.ActivityManagerService.this.getIntentForIntentSender(sender);
        }

        public android.app.PendingIntent getPendingIntentActivityAsApp(int requestCode, android.content.Intent intent, int flags, android.os.Bundle options, java.lang.String ownerPkg, int ownerUid) {
            return getPendingIntentActivityAsApp(requestCode, new android.content.Intent[]{intent}, flags, options, ownerPkg, ownerUid);
        }

        public android.app.PendingIntent getPendingIntentActivityAsApp(int requestCode, android.content.Intent[] intents, int flags, android.os.Bundle options, java.lang.String ownerPkg, int ownerUid) {
            boolean flagImmutableSet = (flags & 67108864) != 0;
            boolean flagMutableSet = (flags & 33554432) != 0;
            if (flagImmutableSet == flagMutableSet) {
                throw new java.lang.IllegalArgumentException("Must set exactly one of FLAG_IMMUTABLE or FLAG_MUTABLE");
            }
            android.content.Context context = com.android.server.am.ActivityManagerService.this.mContext;
            android.content.ContentResolver resolver = context.getContentResolver();
            int len = intents.length;
            java.lang.String[] resolvedTypes = new java.lang.String[len];
            for (int i = 0; i < len; i++) {
                android.content.Intent intent = intents[i];
                resolvedTypes[i] = intent.resolveTypeIfNeeded(resolver);
                intent.migrateExtraStreamToClipData(context);
                intent.prepareToLeaveProcess(context);
            }
            android.content.IIntentSender target = com.android.server.am.ActivityManagerService.this.getIntentSenderWithFeatureAsApp(2, ownerPkg, context.getAttributionTag(), null, null, requestCode, intents, resolvedTypes, flags, options, android.os.UserHandle.getUserId(ownerUid), ownerUid);
            if (target != null) {
                return new android.app.PendingIntent(target);
            }
            return null;
        }

        public long getBootTimeTempAllowListDuration() {
            return com.android.server.am.ActivityManagerService.this.mConstants.mBootTimeTempAllowlistDuration;
        }

        public void registerAnrController(android.app.AnrController controller) {
            com.android.server.am.ActivityManagerService.this.mActivityTaskManager.registerAnrController(controller);
        }

        public void unregisterAnrController(android.app.AnrController controller) {
            com.android.server.am.ActivityManagerService.this.mActivityTaskManager.unregisterAnrController(controller);
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public boolean canStartForegroundService(int pid, int uid, java.lang.String packageName) {
            boolean zCanStartForegroundServiceLocked;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    zCanStartForegroundServiceLocked = com.android.server.am.ActivityManagerService.this.mServices.canStartForegroundServiceLocked(pid, uid, packageName);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return zCanStartForegroundServiceLocked;
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public void tempAllowWhileInUsePermissionInFgs(int uid, long durationMs) {
            com.android.server.am.ActivityManagerService.this.mFgsWhileInUseTempAllowList.add(uid, durationMs, "");
        }

        public boolean isTempAllowlistedForFgsWhileInUse(int uid) {
            return com.android.server.am.ActivityManagerService.this.mFgsWhileInUseTempAllowList.isAllowed(uid);
        }

        @Override // com.android.server.am.ActivityManagerLocal
        public boolean canAllowWhileInUsePermissionInFgs(int pid, int uid, java.lang.String packageName) {
            boolean zCanAllowWhileInUsePermissionInFgsLocked;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    zCanAllowWhileInUsePermissionInFgsLocked = com.android.server.am.ActivityManagerService.this.mServices.canAllowWhileInUsePermissionInFgsLocked(pid, uid, packageName);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return zCanAllowWhileInUsePermissionInFgsLocked;
        }

        public int getPushMessagingOverQuotaBehavior() {
            int i;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    i = com.android.server.am.ActivityManagerService.this.mConstants.mPushMessagingOverQuotaBehavior;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return i;
        }

        public int getServiceStartForegroundTimeout() {
            return com.android.server.am.ActivityManagerService.this.mConstants.mServiceStartForegroundTimeoutMs;
        }

        public int getUidCapability(int uid) {
            int curCapability;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.UidRecord uidRecord = com.android.server.am.ActivityManagerService.this.mProcessList.getUidRecordLOSP(uid);
                    if (uidRecord == null) {
                        throw new java.lang.IllegalArgumentException("uid record for " + uid + " not found");
                    }
                    curCapability = uidRecord.getCurCapability();
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return curCapability;
        }

        public java.util.List<java.lang.Integer> getIsolatedProcesses(int uid) {
            java.util.List<java.lang.Integer> isolatedProcessesLocked;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    isolatedProcessesLocked = com.android.server.am.ActivityManagerService.this.mProcessList.getIsolatedProcessesLocked(uid);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return isolatedProcessesLocked;
        }

        public int sendIntentSender(android.content.IIntentSender target, android.os.IBinder allowlistToken, int code, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) {
            return com.android.server.am.ActivityManagerService.this.sendIntentSender(null, target, allowlistToken, code, intent, resolvedType, finishedReceiver, requiredPermission, options);
        }

        public void setVoiceInteractionManagerProvider(android.app.ActivityManagerInternal.VoiceInteractionManagerProvider provider) {
            com.android.server.am.ActivityManagerService.this.setVoiceInteractionManagerProvider(provider);
        }

        public void setStopUserOnSwitch(int value) {
            com.android.server.am.ActivityManagerService.this.setStopUserOnSwitch(value);
        }

        public int getRestrictionLevel(int uid) {
            return com.android.server.am.ActivityManagerService.this.mAppRestrictionController.getRestrictionLevel(uid);
        }

        public int getRestrictionLevel(java.lang.String pkg, int userId) {
            return com.android.server.am.ActivityManagerService.this.mAppRestrictionController.getRestrictionLevel(pkg, userId);
        }

        public boolean isBgAutoRestrictedBucketFeatureFlagEnabled() {
            return com.android.server.am.ActivityManagerService.this.mAppRestrictionController.isBgAutoRestrictedBucketFeatureFlagEnabled();
        }

        public void addAppBackgroundRestrictionListener(android.app.ActivityManagerInternal.AppBackgroundRestrictionListener listener) {
            com.android.server.am.ActivityManagerService.this.mAppRestrictionController.addAppBackgroundRestrictionListener(listener);
        }

        public void addForegroundServiceStateListener(android.app.ActivityManagerInternal.ForegroundServiceStateListener listener) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.mForegroundServiceStateListeners.add(listener);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void addBroadcastEventListener(android.app.ActivityManagerInternal.BroadcastEventListener listener) {
            com.android.server.am.ActivityManagerService.this.mBroadcastEventListeners.add(listener);
        }

        public void addBindServiceEventListener(android.app.ActivityManagerInternal.BindServiceEventListener listener) {
            com.android.server.am.ActivityManagerService.this.mBindServiceEventListeners.add(listener);
        }

        public void restart() {
            com.android.server.am.ActivityManagerService.this.restart();
        }

        public void registerNetworkPolicyUidObserver(android.app.IUidObserver observer, int which, int cutpoint, java.lang.String callingPackage) {
            com.android.server.am.ActivityManagerService.this.mNetworkPolicyUidObserver = observer;
            com.android.server.am.ActivityManagerService.this.mUidObserverController.register(observer, which, cutpoint, callingPackage, android.os.Binder.getCallingUid(), null);
        }

        public boolean startForegroundServiceDelegate(android.app.ForegroundServiceDelegationOptions options, android.content.ServiceConnection connection) {
            boolean zStartForegroundServiceDelegateLocked;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    zStartForegroundServiceDelegateLocked = com.android.server.am.ActivityManagerService.this.mServices.startForegroundServiceDelegateLocked(options, connection);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return zStartForegroundServiceDelegateLocked;
        }

        public void stopForegroundServiceDelegate(android.app.ForegroundServiceDelegationOptions options) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.mServices.stopForegroundServiceDelegateLocked(options);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public void stopForegroundServiceDelegate(android.content.ServiceConnection connection) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.mServices.stopForegroundServiceDelegateLocked(connection);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }

        public android.util.ArraySet<java.lang.String> getClientPackages(java.lang.String servicePackageName) {
            android.util.ArraySet<java.lang.String> clientPackagesLocked;
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    clientPackagesLocked = com.android.server.am.ActivityManagerService.this.mServices.getClientPackagesLocked(servicePackageName);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            return clientPackagesLocked;
        }

        public void triggerUnsafeIntentStrictMode(final int callingPid, final int type, android.content.Intent intent) {
            final android.app.IUnsafeIntentStrictModeCallback callback;
            final android.content.Intent i = intent.cloneFilter();
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    callback = (android.app.IUnsafeIntentStrictModeCallback) com.android.server.am.ActivityManagerService.this.mStrictModeCallbacks.get(callingPid);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            if (callback != null) {
                com.android.internal.os.BackgroundThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$LocalService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$triggerUnsafeIntentStrictMode$2(callback, type, i, callingPid);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$triggerUnsafeIntentStrictMode$2(android.app.IUnsafeIntentStrictModeCallback callback, int type, android.content.Intent i, int callingPid) {
            try {
                callback.onUnsafeIntent(type, i);
            } catch (android.os.RemoteException e) {
                com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        com.android.server.am.ActivityManagerService.this.mStrictModeCallbacks.remove(callingPid);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
        }

        public boolean startProfileEvenWhenDisabled(int userId) {
            return com.android.server.am.ActivityManagerService.this.mUserController.startProfile(userId, true, null);
        }

        public void logFgsApiBegin(int apiType, int uid, int pid) {
            synchronized (this) {
                com.android.server.am.ActivityManagerService.this.mServices.logFgsApiBeginLocked(apiType, uid, pid);
            }
        }

        public void logFgsApiEnd(int apiType, int uid, int pid) {
            synchronized (this) {
                com.android.server.am.ActivityManagerService.this.mServices.logFgsApiEndLocked(apiType, uid, pid);
            }
        }

        public void notifyMediaProjectionEvent(int uid, android.os.IBinder projectionToken, int event) {
            com.android.server.am.ActivityManagerService.this.notifyMediaProjectionEvent(uid, projectionToken, event);
        }

        /* JADX INFO: renamed from: getCachedAppsHighWatermarkStats, reason: merged with bridge method [inline-methods] */
        public android.util.StatsEvent m1192getCachedAppsHighWatermarkStats(int atomTag, boolean resetAfterPull) {
            return com.android.server.am.ActivityManagerService.this.mAppProfiler.mCachedAppsWatermarkData.getCachedAppsHighWatermarkStats(atomTag, resetAfterPull);
        }

        public boolean clearApplicationUserData(java.lang.String packageName, boolean keepState, boolean isRestore, android.content.pm.IPackageDataObserver observer, int userId) {
            return com.android.server.am.ActivityManagerService.this.clearApplicationUserData(packageName, keepState, isRestore, observer, userId);
        }

        public boolean shouldDelayHomeLaunch(int userId) {
            boolean z;
            if (!com.android.server.am.ActivityManagerService.this.isHomeLaunchDelayable()) {
                return false;
            }
            synchronized (com.android.server.am.ActivityManagerService.this.mThemeOverlayReadyUsers) {
                z = com.android.server.am.ActivityManagerService.this.mThemeOverlayReadyUsers.contains(java.lang.Integer.valueOf(userId)) ? false : true;
            }
            return z;
        }

        public void addStartInfoTimestamp(int key, long timestampNs, int uid, int pid, int userId) {
            if (userId == -1 || userId == -2) {
                throw new java.lang.IllegalArgumentException("Unsupported userId");
            }
            com.android.server.am.ActivityManagerService.this.mUserController.handleIncomingUser(pid, uid, userId, true, 0, "addStartInfoTimestampSystem", null);
            com.android.server.am.ActivityManagerService.this.addStartInfoTimestampInternal(key, timestampNs, userId, uid);
        }

        public void killApplicationSync(java.lang.String pkgName, int appId, int userId, java.lang.String reason, int exitInfoReason) {
            if (pkgName == null) {
                return;
            }
            if (appId < 0) {
                android.util.Slog.w("ActivityManager", "Invalid appid specified for pkg : " + pkgName);
                return;
            }
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    com.android.server.am.ActivityManagerService.this.forceStopPackageLocked(pkgName, appId, false, false, true, false, false, false, userId, reason, exitInfoReason);
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    long inputDispatchingTimedOut(int pid, boolean aboveSystem, com.android.internal.os.TimeoutRecord timeoutRecord) {
        com.android.server.am.ProcessRecord proc;
        if (checkCallingPermission("android.permission.FILTER_EVENTS") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.FILTER_EVENTS");
        }
        timeoutRecord.mLatencyTracker.waitingOnPidLockStarted();
        synchronized (this.mPidsSelfLocked) {
            timeoutRecord.mLatencyTracker.waitingOnPidLockEnded();
            proc = this.mPidsSelfLocked.get(pid);
        }
        if (this.mActivityManagerServiceExt.isWaitingPermissionChoice(proc)) {
            return BROADCAST_BG_TIMEOUT;
        }
        long timeoutMillis = proc != null ? proc.getInputDispatchingTimeoutMillis() : android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        if (inputDispatchingTimedOut(proc, null, null, null, null, aboveSystem, timeoutRecord)) {
            return 0L;
        }
        return timeoutMillis;
    }

    boolean inputDispatchingTimedOut(com.android.server.am.ProcessRecord proc, java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo aInfo, java.lang.String parentShortComponentName, com.android.server.wm.WindowProcessController parentProcess, boolean aboveSystem, com.android.internal.os.TimeoutRecord timeoutRecord) {
        try {
            android.os.Trace.traceBegin(64L, "inputDispatchingTimedOut()");
            if (checkCallingPermission("android.permission.FILTER_EVENTS") != 0) {
                throw new java.lang.SecurityException("Requires permission android.permission.FILTER_EVENTS");
            }
            if (this.mActivityManagerServiceExt.isWaitingPermissionChoice(proc)) {
                return true;
            }
            if (proc != null) {
                this.mActivityManagerServiceExt.cancelCheck(proc);
                timeoutRecord.mLatencyTracker.waitingOnAMSLockStarted();
                boostPriorityForLockedSection();
                synchronized (this) {
                    try {
                        timeoutRecord.mLatencyTracker.waitingOnAMSLockEnded();
                        if (proc.isDebugging()) {
                            resetPriorityAfterLockedSection();
                            return false;
                        }
                        if (this.mSocExt.isAnrDeferrable()) {
                            resetPriorityAfterLockedSection();
                            return false;
                        }
                        if (proc.getActiveInstrumentation() != null) {
                            android.os.Bundle info = new android.os.Bundle();
                            info.putString("shortMsg", "keyDispatchingTimedOut");
                            info.putString("longMsg", timeoutRecord.mReason);
                            finishInstrumentationLocked(proc, 0, info);
                            resetPriorityAfterLockedSection();
                            return true;
                        }
                        resetPriorityAfterLockedSection();
                        this.mActivityManagerServiceExt.dumpActivityAndWindow();
                        this.mAnrHelper.appNotResponding(proc, activityShortComponentName, aInfo, parentShortComponentName, parentProcess, aboveSystem, timeoutRecord, true);
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
            return true;
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    public void waitForNetworkStateUpdate(long procStateSeq) {
        int callingUid = android.os.Binder.getCallingUid();
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
            android.util.Slog.d(TAG_NETWORK, "Called from " + callingUid + " to wait for seq: " + procStateSeq);
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                com.android.server.am.UidRecord record = this.mProcessList.getUidRecordLOSP(callingUid);
                if (record == null) {
                    resetPriorityAfterProcLockedSection();
                    return;
                }
                resetPriorityAfterProcLockedSection();
                synchronized (record.networkStateLock) {
                    if (record.lastNetworkUpdatedProcStateSeq >= procStateSeq) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                            android.util.Slog.d(TAG_NETWORK, "Network rules have been already updated for seq no. " + procStateSeq + ", so no need to wait. Uid: " + callingUid + ", lastProcStateSeqWithUpdatedNetworkState: " + record.lastNetworkUpdatedProcStateSeq);
                        }
                        return;
                    }
                    try {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                            android.util.Slog.d(TAG_NETWORK, "Starting to wait for the network rules update. Uid: " + callingUid + " procStateSeq: " + procStateSeq);
                        }
                        long startTime = android.os.SystemClock.uptimeMillis();
                        record.procStateSeqWaitingForNetwork = procStateSeq;
                        record.networkStateLock.wait(this.mConstants.mNetworkAccessTimeoutMs);
                        record.procStateSeqWaitingForNetwork = 0L;
                        long totalTime = android.os.SystemClock.uptimeMillis() - startTime;
                        if (totalTime >= this.mConstants.mNetworkAccessTimeoutMs || com.android.server.am.ActivityManagerDebugConfig.DEBUG_NETWORK) {
                            android.util.Slog.wtf(TAG_NETWORK, "Total time waited for network rules to get updated: " + totalTime + ". Uid: " + callingUid + " procStateSeq: " + procStateSeq + " UidRec: " + record + " validateUidRec: " + this.mUidObserverController.getValidateUidRecord(callingUid));
                        }
                    } catch (java.lang.InterruptedException e) {
                        java.lang.Thread.currentThread().interrupt();
                    }
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    public void waitForBroadcastIdle() {
        waitForBroadcastIdle(com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO, false);
    }

    void waitForBroadcastIdle(java.io.PrintWriter pw, boolean flushBroadcastLoopers) {
        enforceCallingPermission("android.permission.DUMP", "waitForBroadcastIdle()");
        if (flushBroadcastLoopers) {
            com.android.server.am.BroadcastLoopers.waitForIdle(pw);
        }
        this.mBroadcastQueue.waitForIdle(pw);
        pw.println("All broadcast queues are idle!");
        pw.flush();
    }

    public void waitForBroadcastBarrier() {
        waitForBroadcastBarrier(com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO, false, false);
    }

    void waitForBroadcastBarrier(java.io.PrintWriter pw, boolean flushBroadcastLoopers, boolean flushApplicationThreads) {
        enforceCallingPermission("android.permission.DUMP", "waitForBroadcastBarrier()");
        if (flushBroadcastLoopers) {
            com.android.server.am.BroadcastLoopers.waitForBarrier(pw);
        }
        this.mBroadcastQueue.waitForBarrier(pw);
        if (flushApplicationThreads) {
            waitForApplicationBarrier(pw);
        }
    }

    void waitForApplicationBarrier(java.io.PrintWriter pw) {
        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.ProcessRecord>> pmap;
        final java.util.concurrent.CountDownLatch finishedLatch = new java.util.concurrent.CountDownLatch(1);
        final java.util.concurrent.atomic.AtomicInteger pingCount = new java.util.concurrent.atomic.AtomicInteger(0);
        final java.util.concurrent.atomic.AtomicInteger pongCount = new java.util.concurrent.atomic.AtomicInteger(0);
        android.os.RemoteCallback pongCallback = new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda2
            public final void onResult(android.os.Bundle bundle) {
                com.android.server.am.ActivityManagerService.lambda$waitForApplicationBarrier$28(pongCount, pingCount, finishedLatch, bundle);
            }
        });
        pingCount.incrementAndGet();
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.ProcessRecord>> pmap2 = this.mProcessList.getProcessNamesLOSP().getMap();
                        int numProc = pmap2.size();
                        for (int iProc = 0; iProc < numProc; iProc++) {
                            android.util.SparseArray<com.android.server.am.ProcessRecord> apps = pmap2.valueAt(iProc);
                            int numApps = apps.size();
                            int iApp = 0;
                            while (iApp < numApps) {
                                com.android.server.am.ProcessRecord app = apps.valueAt(iApp);
                                android.app.IApplicationThread thread = app.getOnewayThread();
                                if (thread == null) {
                                    pmap = pmap2;
                                } else {
                                    pmap = pmap2;
                                    this.mOomAdjuster.mCachedAppOptimizer.unfreezeTemporarily(app, 15);
                                    pingCount.incrementAndGet();
                                    try {
                                        thread.schedulePing(pongCallback);
                                    } catch (android.os.RemoteException e) {
                                        pongCallback.sendResult((android.os.Bundle) null);
                                    }
                                }
                                iApp++;
                                pmap2 = pmap;
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
        pongCallback.sendResult((android.os.Bundle) null);
        for (int i = 0; i < 30; i++) {
            try {
                if (finishedLatch.await(1L, java.util.concurrent.TimeUnit.SECONDS)) {
                    pw.println("Finished application barriers!");
                    pw.flush();
                    return;
                } else {
                    pw.println("Waiting for application barriers, at " + pongCount.get() + " of " + pingCount.get() + "...");
                    pw.flush();
                }
            } catch (java.lang.InterruptedException e2) {
            }
        }
        pw.println("Gave up waiting for application barriers!");
        pw.flush();
    }

    static /* synthetic */ void lambda$waitForApplicationBarrier$28(java.util.concurrent.atomic.AtomicInteger pongCount, java.util.concurrent.atomic.AtomicInteger pingCount, java.util.concurrent.CountDownLatch finishedLatch, android.os.Bundle result) {
        if (pongCount.incrementAndGet() == pingCount.get()) {
            finishedLatch.countDown();
        }
    }

    void waitForBroadcastDispatch(java.io.PrintWriter pw, android.content.Intent intent) {
        enforceCallingPermission("android.permission.DUMP", "waitForBroadcastDispatch");
        this.mBroadcastQueue.waitForDispatched(intent, pw);
    }

    void setIgnoreDeliveryGroupPolicy(java.lang.String broadcastAction) {
        java.util.Objects.requireNonNull(broadcastAction);
        enforceCallingPermission("android.permission.DUMP", "waitForBroadcastBarrier()");
        synchronized (this.mDeliveryGroupPolicyIgnoredActions) {
            this.mDeliveryGroupPolicyIgnoredActions.add(broadcastAction);
        }
    }

    void clearIgnoreDeliveryGroupPolicy(java.lang.String broadcastAction) {
        java.util.Objects.requireNonNull(broadcastAction);
        enforceCallingPermission("android.permission.DUMP", "waitForBroadcastBarrier()");
        synchronized (this.mDeliveryGroupPolicyIgnoredActions) {
            this.mDeliveryGroupPolicyIgnoredActions.remove(broadcastAction);
        }
    }

    boolean shouldIgnoreDeliveryGroupPolicy(java.lang.String broadcastAction) {
        boolean zContains;
        if (broadcastAction == null) {
            return false;
        }
        synchronized (this.mDeliveryGroupPolicyIgnoredActions) {
            zContains = this.mDeliveryGroupPolicyIgnoredActions.contains(broadcastAction);
        }
        return zContains;
    }

    void dumpDeliveryGroupPolicyIgnoredActions(android.util.IndentingPrintWriter ipw) {
        synchronized (this.mDeliveryGroupPolicyIgnoredActions) {
            ipw.println(this.mDeliveryGroupPolicyIgnoredActions);
        }
    }

    public void forceDelayBroadcastDelivery(java.lang.String targetPackage, long delayedDurationMs) {
        java.util.Objects.requireNonNull(targetPackage);
        com.android.internal.util.Preconditions.checkArgumentNonnegative(delayedDurationMs);
        enforceCallingPermission("android.permission.DUMP", "forceDelayBroadcastDelivery()");
        this.mBroadcastQueue.forceDelayBroadcastDelivery(targetPackage, delayedDurationMs);
    }

    public boolean isProcessFrozen(int pid) {
        enforceCallingPermission("android.permission.DUMP", "isProcessFrozen()");
        return this.mOomAdjuster.mCachedAppOptimizer.isProcessFrozen(pid);
    }

    public int getBackgroundRestrictionExemptionReason(int uid) {
        enforceCallingPermission("android.permission.DEVICE_POWER", "getBackgroundRestrictionExemptionReason()");
        return this.mAppRestrictionController.getBackgroundRestrictionExemptionReason(uid);
    }

    void setBackgroundRestrictionLevel(java.lang.String packageName, int uid, int userId, int level, int reason, int subReason) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000 && callingUid != 0 && callingUid != 2000) {
            throw new java.lang.SecurityException("No permission to change app restriction level");
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            int curBucket = this.mUsageStatsService.getAppStandbyBucket(packageName, userId, android.os.SystemClock.elapsedRealtime());
            this.mAppRestrictionController.applyRestrictionLevel(packageName, uid, level, null, curBucket, true, reason, subReason);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void noteAppRestrictionEnabled(java.lang.String packageName, int uid, int restrictionType, boolean enabled, int reason, java.lang.String subReason, int source, long threshold) throws java.lang.Throwable {
        if (android.app.Flags.appRestrictionsApi()) {
            enforceCallingPermission("android.permission.DEVICE_POWER", "noteAppRestrictionEnabled()");
            int userId = android.os.UserHandle.getCallingUserId();
            long callingId = android.os.Binder.clearCallingIdentity();
            int uid2 = uid;
            if (uid2 == -1) {
                try {
                    try {
                        uid2 = this.mPackageManagerInt.getPackageUid(packageName, 0L, userId);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(callingId);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
            try {
                this.mAppRestrictionController.noteAppRestrictionEnabled(packageName, uid2, restrictionType, enabled, reason, subReason, source, threshold);
                android.os.Binder.restoreCallingIdentity(callingId);
            } catch (java.lang.Throwable th3) {
                th = th3;
                android.os.Binder.restoreCallingIdentity(callingId);
                throw th;
            }
        }
    }

    int getBackgroundRestrictionLevel(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000 && callingUid != 0 && callingUid != 2000) {
            throw new java.lang.SecurityException("Don't have permission to query app background restriction level");
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInternal.getRestrictionLevel(packageName, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(3:(4:82|18|(2:31|93)(8:21|80|22|23|75|24|25|94)|32)|16|77) */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00e3, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00e4, code lost:
    
        r19 = r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void setForegroundServiceDelegate(java.lang.String r22, int r23, boolean r24, int r25, java.lang.String r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 255
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerService.setForegroundServiceDelegate(java.lang.String, int, boolean, int, java.lang.String):void");
    }

    void refreshSettingsCache() {
        this.mCoreSettingsObserver.onChange(true);
    }

    void resetDropboxRateLimiter() {
        this.mDropboxRateLimiter.reset();
        com.android.server.BootReceiver.resetDropboxRateLimiter();
    }

    public void killPackageDependents(java.lang.String packageName, int userId) {
        enforceCallingPermission("android.permission.KILL_UID", "killPackageDependents()");
        if (packageName == null) {
            throw new java.lang.NullPointerException("Cannot kill the dependents of a package without its name.");
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
        int pkgUid = -1;
        try {
            pkgUid = pm.getPackageUid(packageName, 268435456L, userId);
        } catch (android.os.RemoteException e) {
        }
        if (userId != -1 && pkgUid == -1) {
            throw new java.lang.IllegalArgumentException("Cannot kill dependents of non-existing package " + packageName);
        }
        try {
            boostPriorityForLockedSection();
            synchronized (this) {
                try {
                    com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
                    boostPriorityForProcLockedSection();
                    synchronized (activityManagerGlobalLock) {
                        try {
                            this.mProcessList.killPackageProcessesLSP(packageName, android.os.UserHandle.getAppId(pkgUid), userId, 0, 12, 0, "dep: " + packageName);
                        } catch (java.lang.Throwable th) {
                            resetPriorityAfterProcLockedSection();
                            throw th;
                        }
                    }
                    resetPriorityAfterProcLockedSection();
                } catch (java.lang.Throwable th2) {
                    resetPriorityAfterLockedSection();
                    throw th2;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public int restartUserInBackground(int userId, int userStartMode) {
        return this.mUserController.restartUser(userId, userStartMode);
    }

    public void scheduleApplicationInfoChanged(java.util.List<java.lang.String> packageNames, int userId) {
        enforceCallingPermission("android.permission.CHANGE_CONFIGURATION", "scheduleApplicationInfoChanged()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            boolean updateFrameworkRes = packageNames.contains(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    updateApplicationInfoLOSP(packageNames, updateFrameworkRes, userId);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterProcLockedSection();
            android.appwidget.AppWidgetManagerInternal widgets = (android.appwidget.AppWidgetManagerInternal) com.android.server.LocalServices.getService(android.appwidget.AppWidgetManagerInternal.class);
            if (widgets != null) {
                widgets.applyResourceOverlaysToWidgets(new java.util.HashSet(packageNames), userId, updateFrameworkRes);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void updateSystemUiContext() {
        android.content.pm.PackageManagerInternal packageManagerInternal = getPackageManagerInternal();
        android.content.pm.ApplicationInfo ai = packageManagerInternal.getApplicationInfo(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 1024L, android.os.Binder.getCallingUid(), 0);
        android.app.ActivityThread.currentActivityThread().handleSystemApplicationInfoChanged(ai);
    }

    private void updateApplicationInfoLOSP(java.util.List<java.lang.String> packagesToUpdate, boolean updateFrameworkRes, int userId) {
        if (updateFrameworkRes) {
            com.android.internal.pm.pkg.parsing.ParsingPackageUtils.readConfigUseRoundIcon((android.content.res.Resources) null);
        }
        this.mProcessList.updateApplicationInfoLOSP(packagesToUpdate, userId, updateFrameworkRes);
        if (updateFrameworkRes) {
            java.util.concurrent.Executor executor = android.app.ActivityThread.currentActivityThread().getExecutor();
            final android.hardware.display.DisplayManagerInternal display = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
            if (display != null) {
                java.util.Objects.requireNonNull(display);
                executor.execute(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda38
                    @Override // java.lang.Runnable
                    public final void run() {
                        display.onOverlayChanged();
                    }
                });
            }
            if (this.mWindowManager != null) {
                final com.android.server.wm.WindowManagerService windowManagerService = this.mWindowManager;
                java.util.Objects.requireNonNull(windowManagerService);
                executor.execute(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda39
                    @Override // java.lang.Runnable
                    public final void run() {
                        windowManagerService.onOverlayChanged();
                    }
                });
            }
        }
    }

    void scheduleUpdateBinderHeavyHitterWatcherConfig() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$33();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$33() {
        boolean enabled;
        int batchSize;
        float threshold;
        com.android.internal.os.BinderCallHeavyHitterWatcher.BinderCallHeavyHitterListener listener;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_WATCHER_ENABLED) {
                    this.mHandler.removeMessages(72);
                    enabled = true;
                    batchSize = com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_WATCHER_BATCHSIZE;
                    threshold = com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_WATCHER_THRESHOLD;
                    listener = new com.android.internal.os.BinderCallHeavyHitterWatcher.BinderCallHeavyHitterListener() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda22
                        public final void onHeavyHit(java.util.List list, int i, float f, long j) {
                            this.f$0.lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$30(list, i, f, j);
                        }
                    };
                } else if (this.mHandler.hasMessages(72)) {
                    enabled = com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED;
                    batchSize = com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE;
                    threshold = com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD;
                    listener = new com.android.internal.os.BinderCallHeavyHitterWatcher.BinderCallHeavyHitterListener() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda23
                        public final void onHeavyHit(java.util.List list, int i, float f, long j) {
                            this.f$0.lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$32(list, i, f, j);
                        }
                    };
                } else {
                    enabled = false;
                    batchSize = 0;
                    threshold = 0.0f;
                    listener = null;
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        android.os.Binder.setHeavyHitterWatcherConfig(enabled, batchSize, threshold, listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$30(final java.util.List a, final int b, final float c, final long d) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$29(a, b, c, d);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$32(final java.util.List a, final int b, final float c, final long d) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$31(a, b, c, d);
            }
        });
    }

    void scheduleBinderHeavyHitterAutoSampler() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleBinderHeavyHitterAutoSampler$36();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleBinderHeavyHitterAutoSampler$36() {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (!com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_AUTO_SAMPLER_ENABLED) {
                    resetPriorityAfterProcLockedSection();
                    return;
                }
                if (com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_WATCHER_ENABLED) {
                    resetPriorityAfterProcLockedSection();
                    return;
                }
                long now = android.os.SystemClock.uptimeMillis();
                if (this.mLastBinderHeavyHitterAutoSamplerStart + 3600000 > now) {
                    resetPriorityAfterProcLockedSection();
                    return;
                }
                int batchSize = com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_AUTO_SAMPLER_BATCHSIZE;
                float threshold = com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_AUTO_SAMPLER_THRESHOLD;
                resetPriorityAfterProcLockedSection();
                this.mLastBinderHeavyHitterAutoSamplerStart = now;
                android.os.Binder.setHeavyHitterWatcherConfig(true, batchSize, threshold, new com.android.internal.os.BinderCallHeavyHitterWatcher.BinderCallHeavyHitterListener() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda13
                    public final void onHeavyHit(java.util.List list, int i, float f, long j) {
                        this.f$0.lambda$scheduleBinderHeavyHitterAutoSampler$35(list, i, f, j);
                    }
                });
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(72), 300000L);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleBinderHeavyHitterAutoSampler$35(final java.util.List a, final int b, final float c, final long d) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerService$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleBinderHeavyHitterAutoSampler$34(a, b, c, d);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBinderHeavyHitterAutoSamplerTimeOut() {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (com.android.server.am.ActivityManagerConstants.BINDER_HEAVY_HITTER_WATCHER_ENABLED) {
                    resetPriorityAfterProcLockedSection();
                } else {
                    resetPriorityAfterProcLockedSection();
                    android.os.Binder.setHeavyHitterWatcherConfig(false, 0, 0.0f, null);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleBinderHeavyHitters, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$scheduleUpdateBinderHeavyHitterWatcherConfig$31(java.util.List<com.android.internal.os.BinderCallHeavyHitterWatcher.HeavyHitterContainer> hitters, int totalBinderCalls, float threshold, long timeSpan) {
        int size = hitters.size();
        if (size == 0) {
            return;
        }
        com.android.internal.os.BinderTransactionNameResolver resolver = new com.android.internal.os.BinderTransactionNameResolver();
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Excessive incoming binder calls(>").append(java.lang.String.format("%.1f%%", java.lang.Float.valueOf(threshold * 100.0f))).append(',').append(totalBinderCalls).append(',').append(timeSpan).append("ms): ");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            com.android.internal.os.BinderCallHeavyHitterWatcher.HeavyHitterContainer container = hitters.get(i);
            sb.append('[').append(container.mUid).append(',').append(container.mClass.getName()).append(',').append(resolver.getMethodName(container.mClass, container.mCode)).append(',').append(container.mCode).append(',').append(java.lang.String.format("%.1f%%", java.lang.Float.valueOf(container.mFrequency * 100.0f))).append(']');
        }
        android.util.Slog.w("ActivityManager", sb.toString());
    }

    public void attachAgent(java.lang.String process, java.lang.String path) {
        android.app.IApplicationThread thread;
        try {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
            boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    com.android.server.am.ProcessRecord proc = findProcessLOSP(process, 0, "attachAgent");
                    if (proc == null || (thread = proc.getThread()) == null) {
                        throw new java.lang.IllegalArgumentException("Unknown process: " + process);
                    }
                    enforceDebuggable(proc);
                    thread.attachAgent(path);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterProcLockedSection();
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Process disappeared");
        }
    }

    public void prepareForPossibleShutdown() {
        if (this.mUsageStatsService != null) {
            this.mUsageStatsService.prepareForPossibleShutdown();
        }
    }

    public static class Injector {
        private final android.content.Context mContext;
        private com.android.server.net.NetworkManagementInternal mNmi;
        private com.android.server.am.UserController mUserController;

        public Injector(android.content.Context context) {
            this.mContext = context;
        }

        public android.content.Context getContext() {
            return this.mContext;
        }

        public com.android.server.appop.AppOpsService getAppOpsService(java.io.File recentAccessesFile, java.io.File storageFile, android.os.Handler handler) {
            return new com.android.server.appop.AppOpsService(recentAccessesFile, storageFile, handler, getContext());
        }

        public android.os.Handler getUiHandler(com.android.server.am.ActivityManagerService service) {
            java.util.Objects.requireNonNull(service);
            return service.new UiHandler();
        }

        public boolean isNetworkRestrictedForUid(int uid) {
            if (ensureHasNetworkManagementInternal()) {
                return this.mNmi.isNetworkRestrictedForUid(uid);
            }
            return false;
        }

        public int[] getDisplayIdsForStartingVisibleBackgroundUsers() {
            if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
                com.android.server.utils.Slogf.w("ActivityManager", "getDisplayIdsForStartingVisibleBackgroundUsers(): not supported");
                return null;
            }
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
            android.view.Display[] allDisplays = displayManager.getDisplays();
            if (allDisplays == null || allDisplays.length == 0) {
                com.android.server.utils.Slogf.wtf("ActivityManager", "displayManager (%s) returned no displays", displayManager);
                return null;
            }
            boolean hasDefaultDisplay = false;
            int length = allDisplays.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (allDisplays[i].getDisplayId() != 0) {
                    i++;
                } else {
                    hasDefaultDisplay = true;
                    break;
                }
            }
            if (!hasDefaultDisplay) {
                com.android.server.utils.Slogf.wtf("ActivityManager", "displayManager (%s) has %d displays (%s), but none has id DEFAULT_DISPLAY (%d)", displayManager, java.lang.Integer.valueOf(allDisplays.length), java.util.Arrays.toString(allDisplays), 0);
                return null;
            }
            boolean allowOnDefaultDisplay = android.os.UserManager.isVisibleBackgroundUsersOnDefaultDisplayEnabled();
            int displaysSize = allDisplays.length;
            if (!allowOnDefaultDisplay) {
                displaysSize--;
            }
            int[] displayIds = new int[displaysSize];
            int numberValidDisplays = 0;
            for (android.view.Display display : allDisplays) {
                int displayId = display.getDisplayId();
                if (display.isValid() && (display.getFlags() & 4) == 0 && (allowOnDefaultDisplay || displayId != 0)) {
                    displayIds[numberValidDisplays] = displayId;
                    numberValidDisplays++;
                }
            }
            if (numberValidDisplays == 0) {
                int displayId2 = android.os.SystemProperties.getInt("fw.display_ids_for_starting_users_for_testing_purposes", 0);
                if ((allowOnDefaultDisplay && displayId2 == 0) || displayId2 > 0) {
                    com.android.server.utils.Slogf.w("ActivityManager", "getDisplayIdsForStartingVisibleBackgroundUsers(): no valid display found, but returning %d as set by property %s", java.lang.Integer.valueOf(displayId2), "fw.display_ids_for_starting_users_for_testing_purposes");
                    return new int[]{displayId2};
                }
                com.android.server.utils.Slogf.e("ActivityManager", "getDisplayIdsForStartingVisibleBackgroundUsers(): no valid display on %s", java.util.Arrays.toString(allDisplays));
                return null;
            }
            if (numberValidDisplays != displayIds.length) {
                int[] validDisplayIds = new int[numberValidDisplays];
                java.lang.System.arraycopy(displayIds, 0, validDisplayIds, 0, numberValidDisplays);
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
                    com.android.server.utils.Slogf.d("ActivityManager", "getDisplayIdsForStartingVisibleBackgroundUsers(): returning only valid displays (%d instead of %d): %s", java.lang.Integer.valueOf(numberValidDisplays), java.lang.Integer.valueOf(displayIds.length), java.util.Arrays.toString(validDisplayIds));
                }
                return validDisplayIds;
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
                com.android.server.utils.Slogf.d("ActivityManager", "getDisplayIdsForStartingVisibleBackgroundUsers(): returning all (but DEFAULT_DISPLAY) displays : %s", java.util.Arrays.toString(displayIds));
            }
            return displayIds;
        }

        public boolean startUserInBackgroundVisibleOnDisplay(int userId, int displayId, android.os.IProgressListener unlockProgressListener) {
            return this.mUserController.startUserVisibleOnDisplay(userId, displayId, unlockProgressListener);
        }

        public com.android.server.am.ProcessList getProcessList(com.android.server.am.ActivityManagerService service) {
            return new com.android.server.am.ProcessList();
        }

        public com.android.server.am.BatteryStatsService getBatteryStatsService() {
            return new com.android.server.am.BatteryStatsService(this.mContext, com.android.server.SystemServiceManager.ensureSystemDir());
        }

        public com.android.server.am.ActiveServices getActiveServices(com.android.server.am.ActivityManagerService service) {
            return new com.android.server.am.ActiveServices(service);
        }

        private boolean ensureHasNetworkManagementInternal() {
            if (this.mNmi == null) {
                this.mNmi = (com.android.server.net.NetworkManagementInternal) com.android.server.LocalServices.getService(com.android.server.net.NetworkManagementInternal.class);
            }
            return this.mNmi != null;
        }

        public com.android.server.am.BroadcastQueue getBroadcastQueue(com.android.server.am.ActivityManagerService service) {
            com.android.server.am.BroadcastConstants foreConstants = new com.android.server.am.BroadcastConstants("bcast_fg_constants");
            foreConstants.TIMEOUT = com.android.server.am.ActivityManagerService.BROADCAST_FG_TIMEOUT;
            com.android.server.am.BroadcastConstants backConstants = new com.android.server.am.BroadcastConstants("bcast_bg_constants");
            backConstants.TIMEOUT = com.android.server.am.ActivityManagerService.BROADCAST_BG_TIMEOUT;
            android.os.Handler handler = service.mActivityManagerServiceExt.getBroadcastHandler(service.mHandler);
            return new com.android.server.am.BroadcastQueueModernImpl(service, handler, foreConstants, backConstants);
        }

        public int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        public int getCallingPid() {
            return android.os.Binder.getCallingPid();
        }

        public long clearCallingIdentity() {
            return android.os.Binder.clearCallingIdentity();
        }

        public void restoreCallingIdentity(long ident) {
            android.os.Binder.restoreCallingIdentity(ident);
        }

        public com.android.server.am.AppErrors getAppErrors() {
            return null;
        }

        public com.android.server.firewall.IntentFirewall getIntentFirewall() {
            return null;
        }
    }

    public void startDelegateShellPermissionIdentity(int delegateUid, java.lang.String[] permissions) {
        getAccessCheckDelegateHelper().startDelegateShellPermissionIdentity(delegateUid, permissions);
    }

    public void stopDelegateShellPermissionIdentity() {
        getAccessCheckDelegateHelper().stopDelegateShellPermissionIdentity();
    }

    public java.util.List<java.lang.String> getDelegatedShellPermissions() {
        return getAccessCheckDelegateHelper().getDelegatedShellPermissions();
    }

    public void addOverridePermissionState(int originatingUid, int uid, java.lang.String permission, int result) {
        getAccessCheckDelegateHelper().addOverridePermissionState(originatingUid, uid, permission, result);
    }

    public void removeOverridePermissionState(int originatingUid, int uid, java.lang.String permission) {
        getAccessCheckDelegateHelper().removeOverridePermissionState(originatingUid, uid, permission);
    }

    public void clearOverridePermissionStates(int originatingUid, int uid) {
        getAccessCheckDelegateHelper().clearOverridePermissionStates(originatingUid, uid);
    }

    public void clearAllOverridePermissionStates(int originatingUid) {
        getAccessCheckDelegateHelper().clearAllOverridePermissionStates(originatingUid);
    }

    void maybeTriggerWatchdog() {
    }

    public android.os.ParcelFileDescriptor getLifeMonitor() {
        android.os.ParcelFileDescriptor parcelFileDescriptorDup;
        if (!isCallerShell()) {
            throw new java.lang.SecurityException("Only shell can call it");
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                try {
                    if (this.mLifeMonitorFds == null) {
                        this.mLifeMonitorFds = android.os.ParcelFileDescriptor.createPipe();
                    }
                    parcelFileDescriptorDup = this.mLifeMonitorFds[0].dup();
                } catch (java.io.IOException e) {
                    android.util.Slog.w("ActivityManager", "Unable to create pipe", e);
                    resetPriorityAfterProcLockedSection();
                    return null;
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        resetPriorityAfterProcLockedSection();
        return parcelFileDescriptorDup;
    }

    public void setActivityLocusContext(android.content.ComponentName activity, android.content.LocusId locusId, android.os.IBinder appToken) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (getPackageManagerInternal().getPackageUid(activity.getPackageName(), 0L, userId) != callingUid) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " cannot set locusIdfor package " + activity.getPackageName());
        }
        this.mActivityTaskManager.setLocusId(locusId, appToken);
        if (this.mUsageStatsService != null) {
            this.mUsageStatsService.reportLocusUpdate(activity, userId, locusId, appToken);
        }
    }

    public boolean isAppFreezerSupported() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return com.android.server.am.CachedAppOptimizer.isFreezerSupported();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean isAppFreezerEnabled() {
        return this.mOomAdjuster.mCachedAppOptimizer.useFreezer();
    }

    public boolean isAppFreezerExemptInstPkg() {
        return this.mOomAdjuster.mCachedAppOptimizer.freezerExemptInstPkg();
    }

    public void onBackPressedOnTheiaMonitor(long pressNow) throws android.os.RemoteException {
        if (android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
            java.lang.String msg = "onBackPressedOnTheiaMonitor() from pid=" + android.os.Binder.getCallingPid() + ", not system server. ignore!";
            android.util.Slog.w("ActivityManager", msg);
        } else {
            this.mActivityManagerServiceExt.onBackPressedOnTheiaMonitor(pressNow);
        }
    }

    public void sendTheiaEvent(long category, android.content.Intent args) throws android.os.RemoteException {
        this.mActivityManagerServiceExt.sendTheiaEvent(category, args);
    }

    public void resetAppErrors() {
        enforceCallingPermission("android.permission.RESET_APP_ERRORS", "resetAppErrors");
        this.mAppErrors.resetState();
    }

    public boolean enableAppFreezer(boolean enable) {
        int callerUid = android.os.Binder.getCallingUid();
        if (callerUid == 1000 || android.os.Build.IS_DEBUGGABLE) {
            return this.mOomAdjuster.mCachedAppOptimizer.enableFreezer(enable);
        }
        throw new java.lang.SecurityException("Caller uid " + callerUid + " cannot set freezer state ");
    }

    public boolean enableFgsNotificationRateLimit(boolean enable) {
        boolean zEnableFgsNotificationRateLimitLocked;
        enforceCallingPermission("android.permission.WRITE_DEVICE_CONFIG", "enableFgsNotificationRateLimit");
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                zEnableFgsNotificationRateLimitLocked = this.mServices.enableFgsNotificationRateLimitLocked(enable);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return zEnableFgsNotificationRateLimitLocked;
    }

    public void holdLock(android.os.IBinder token, int durationMs) {
        getTestUtilityServiceLocked().verifyHoldLockToken(token);
        boostPriorityForLockedSection();
        synchronized (this) {
            try {
                android.os.SystemClock.sleep(durationMs);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    static void traceBegin(long traceTag, java.lang.String methodName, java.lang.String subInfo) {
        if (android.os.Trace.isTagEnabled(traceTag)) {
            android.os.Trace.traceBegin(traceTag, methodName + subInfo);
        }
    }

    private static int getIntArg(java.io.PrintWriter pw, java.lang.String[] args, int index, int invalidValue) {
        if (index > args.length) {
            pw.println("Missing argument");
            return invalidValue;
        }
        java.lang.String arg = args[index];
        try {
            return java.lang.Integer.parseInt(arg);
        } catch (java.lang.Exception e) {
            pw.printf("Non-numeric argument at index %d: %s\n", java.lang.Integer.valueOf(index), arg);
            return invalidValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMediaProjectionEvent(int uid, android.os.IBinder projectionToken, int event) {
        android.util.ArraySet<android.os.IBinder> tokens;
        synchronized (this.mMediaProjectionTokenMap) {
            int index = this.mMediaProjectionTokenMap.indexOfKey(uid);
            if (event == 0) {
                if (index < 0) {
                    tokens = new android.util.ArraySet<>();
                    this.mMediaProjectionTokenMap.put(uid, tokens);
                } else {
                    tokens = this.mMediaProjectionTokenMap.valueAt(index);
                }
                tokens.add(projectionToken);
            } else if (event == 1 && index >= 0) {
                android.util.ArraySet<android.os.IBinder> tokens2 = this.mMediaProjectionTokenMap.valueAt(index);
                tokens2.remove(projectionToken);
                if (tokens2.isEmpty()) {
                    this.mMediaProjectionTokenMap.removeAt(index);
                }
            }
        }
    }

    boolean isAllowedMediaProjectionNoOpCheck(int uid) {
        boolean z;
        synchronized (this.mMediaProjectionTokenMap) {
            int index = this.mMediaProjectionTokenMap.indexOfKey(uid);
            z = index >= 0 && !this.mMediaProjectionTokenMap.valueAt(index).isEmpty();
        }
        return z;
    }

    public void frozenBinderTransactionDetected(int debugPid, int code, int flags, int err) {
        com.android.server.am.ProcessRecord app;
        synchronized (this.mPidsSelfLocked) {
            app = this.mPidsSelfLocked.get(debugPid);
        }
        this.mOomAdjuster.mCachedAppOptimizer.binderError(debugPid, app, code, flags, err);
    }

    void enqueuePendingTopAppIfNecessaryLocked() {
        this.mPendingStartActivityUids.enqueuePendingTopAppIfNecessaryLocked(this);
    }

    void clearPendingTopAppLocked() {
        this.mPendingStartActivityUids.clear();
    }

    public com.android.server.am.IActivityManagerServiceWrapper getWrapper() {
        return this.mAmsWrapper;
    }

    private class ActivityManagerServiceWrapper implements com.android.server.am.IActivityManagerServiceWrapper {
        private ActivityManagerServiceWrapper() {
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public com.android.server.am.IActivityManagerServiceExt getExtImpl() {
            return com.android.server.am.ActivityManagerService.this.mActivityManagerServiceExt;
        }

        private com.android.server.am.IActivityManagerServiceSocExt getSocExtImpl() {
            return com.android.server.am.ActivityManagerService.this.mSocExt;
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void addServiceToMap(android.util.ArrayMap<java.lang.String, android.os.IBinder> map, java.lang.String name) {
            com.android.server.am.ActivityManagerService.addServiceToMap(map, name);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public java.util.List<android.content.pm.ResolveInfo> collectReceiverComponents(android.content.Intent intent, java.lang.String resolvedType, int callingUid, int callingPid, int[] users, int[] broadcastAllowList) {
            return com.android.server.am.ActivityManagerService.this.collectReceiverComponents(intent, resolvedType, callingUid, callingPid, users, broadcastAllowList);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void cleanupDisabledPackageComponentsLocked(java.lang.String packageName, int userId, java.lang.String[] changedClasses) {
            com.android.server.am.ActivityManagerService.this.cleanupDisabledPackageComponentsLocked(packageName, userId, changedClasses);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void trimApplications(boolean forceFullOomAdj, int oomAdjReason) {
            com.android.server.am.ActivityManagerService.this.trimApplications(forceFullOomAdj, oomAdjReason);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public boolean startUser(int userId, int userStartMode, android.os.IProgressListener unlockListener) {
            return com.android.server.am.ActivityManagerService.this.mUserController.startUser(userId, userStartMode, unlockListener);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public int getCurrentUserIdLU() {
            return com.android.server.am.ActivityManagerService.this.mUserController.getCurrentUserIdLU();
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void removeUriPermissionsForPackage(java.lang.String packageName, int userHandle, boolean persistable, boolean targetOnly) {
            com.android.server.am.ActivityManagerService.this.mUgmInternal.removeUriPermissionsForPackage(packageName, userHandle, persistable, targetOnly);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void removeRecentTasksByPackageName(java.lang.String packageName, int userId) {
            com.android.server.am.ActivityManagerService.this.mAtmInternal.removeRecentTasksByPackageName(packageName, userId);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void killPackageProcessesLocked(java.lang.String packageName, int appId, int userId, int minOomAdj, int reasonCode, int subReason, java.lang.String reason) {
            com.android.server.am.ActivityManagerService.this.mProcessList.killPackageProcessesLSP(packageName, appId, userId, minOomAdj, reasonCode, subReason, reason);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public boolean forceStopPackageLocked(java.lang.String packageName, int appId, boolean callerWillRestart, boolean purgeCache, boolean doit, boolean evenPersistent, boolean uninstalling, boolean packageStateStopped, int userId, java.lang.String reason) {
            boolean result = com.android.server.am.ActivityManagerService.this.forceStopPackageLocked(packageName, appId, callerWillRestart, purgeCache, doit, evenPersistent, uninstalling, packageStateStopped, userId, reason);
            return result;
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void forceStopPackageLocked(java.lang.String packageName, int userId) {
            com.android.server.am.ActivityManagerService.this.mServices.forceStopPackageLocked(packageName, userId);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void removeDyingProviderLocked(com.android.server.am.ProcessRecord proc, com.android.server.am.ContentProviderRecord cpr, boolean always) {
            com.android.server.am.ActivityManagerService.this.mCpHelper.removeDyingProviderLocked(proc, cpr, always);
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public com.android.server.am.ProcessRecord getTopAppLockedForBroadcast() {
            com.android.server.wm.WindowProcessController wpc = com.android.server.am.ActivityManagerService.this.mAtmInternal != null ? com.android.server.am.ActivityManagerService.this.mAtmInternal.getTopApp() : null;
            if (wpc == null) {
                return null;
            }
            com.android.server.am.ProcessRecord r = (com.android.server.am.ProcessRecord) wpc.mOwner;
            return r;
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public void dynamicalConfigLog(java.lang.String categoryTypeName, android.app.IApplicationThread thread, boolean on) {
            if (categoryTypeName == null || categoryTypeName.isEmpty()) {
                return;
            }
            if ("ActiveServices".equals(categoryTypeName)) {
                com.android.server.am.ActivityManagerService.this.mServices.getWrapper().getExtImpl().setActiveServicesDynamicalLogEnable(on);
            } else if ("ActivityThread".equals(categoryTypeName) && thread != null) {
                try {
                    thread.setDynamicalLogEnable(on);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w("ActivityManager", "Got a RemoteException while open the activity log ");
                }
            }
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public boolean isInRestartingServicesList(java.lang.String pkgName, int uid) {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ActivityManagerService.this;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                for (int i = 0; i < com.android.server.am.ActivityManagerService.this.mServices.mRestartingServices.size(); i++) {
                    try {
                        com.android.server.am.ServiceRecord sr = com.android.server.am.ActivityManagerService.this.mServices.mRestartingServices.get(i);
                        if (sr.appInfo.uid == uid && sr.appInfo.packageName.equals(pkgName)) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                return false;
            }
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public java.lang.Object getAnrManager() {
            return com.android.server.am.ActivityManagerService.this.mSocExt.getAnrManager();
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public java.lang.Object getAmsExt() {
            return com.android.server.am.ActivityManagerService.this.mSocExt.getAmsExt();
        }

        @Override // com.android.server.am.IActivityManagerServiceWrapper
        public boolean isCameraActiveForUid(int uid) {
            return com.android.server.am.ActivityManagerService.this.isCameraActiveForUid(uid);
        }
    }
}
