package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerService implements com.android.server.pm.PackageSender, android.content.pm.TestUtilityService {
    public static final java.lang.String APP_METADATA_FILE_NAME = "app.metadata";
    private static final android.os.Handler.Callback BACKGROUND_HANDLER_CALLBACK;
    private static final int BLUETOOTH_UID = 1002;
    private static final long BROADCAST_DELAY = 1000;
    private static final long BROADCAST_DELAY_DURING_STARTUP = 10000;
    static final int CHECK_PENDING_INTEGRITY_VERIFICATION = 26;
    static final int CHECK_PENDING_VERIFICATION = 16;
    private static final java.lang.String COMPANION_PACKAGE_NAME = "com.android.companiondevicemanager";
    public static final java.lang.String COMPRESSED_EXTENSION = ".gz";
    static final int DEFAULT_FILE_ACCESS_MODE = 420;
    static final int DEFAULT_NATIVE_LIBRARY_FILE_ACCESS_MODE = 493;
    static final long DEFAULT_UNUSED_STATIC_SHARED_LIB_MIN_CACHE_PERIOD;
    static final int DEFAULT_VERIFICATION_RESPONSE = 1;
    static final int DEFERRED_NO_KILL_INSTALL_OBSERVER = 24;
    private static final int DEFERRED_NO_KILL_INSTALL_OBSERVER_DELAY_MS = 500;
    static final int DEFERRED_NO_KILL_POST_DELETE = 23;
    private static final int DEFERRED_NO_KILL_POST_DELETE_DELAY_MS = 3000;
    private static final long DEFERRED_NO_KILL_POST_DELETE_DELAY_MS_EXTENDED;
    static final int DEFERRED_PENDING_KILL_INSTALL_OBSERVER = 29;
    private static final int DEFERRED_PENDING_KILL_INSTALL_OBSERVER_DELAY_MS = 1000;
    static final int DOMAIN_VERIFICATION = 27;
    private static final android.os.incremental.PerUidReadTimeouts[] EMPTY_PER_UID_READ_TIMEOUTS_ARRAY;
    private static final boolean ENABLE_BOOST = false;
    static final int ENABLE_ROLLBACK_STATUS = 21;
    static final int ENABLE_ROLLBACK_TIMEOUT = 22;
    static final boolean HIDE_EPHEMERAL_APIS = false;
    static final int INSTANT_APP_RESOLUTION_PHASE_TWO = 20;
    static final int INTEGRITY_VERIFICATION_COMPLETE = 25;
    private static final int LOG_UID = 1007;
    public static final int MIN_INSTALLABLE_TARGET_SDK;
    private static final int NETWORKSTACK_UID = 1073;
    private static final int NFC_UID = 1027;
    static final java.lang.String PACKAGE_MIME_TYPE = "application/vnd.android.package-archive";
    static final java.lang.String PACKAGE_SCHEME = "package";
    public static final int PACKAGE_STARTABILITY_DIRECT_BOOT_UNSUPPORTED = 4;
    public static final int PACKAGE_STARTABILITY_FROZEN = 3;
    public static final int PACKAGE_STARTABILITY_NOT_FOUND = 1;
    public static final int PACKAGE_STARTABILITY_NOT_SYSTEM = 2;
    public static final int PACKAGE_STARTABILITY_OK = 0;
    static final int PACKAGE_VERIFIED = 15;
    public static final java.lang.String PLATFORM_PACKAGE_NAME = "android";
    static final int POST_INSTALL = 9;
    static final java.lang.String PRECOMPILE_LAYOUTS = "pm.precompile_layouts";
    private static final java.lang.String PROPERTY_DEFERRED_NO_KILL_POST_DELETE_DELAY_MS_EXTENDED = "deferred_no_kill_post_delete_delay_ms_extended";
    private static final java.lang.String PROPERTY_INCFS_DEFAULT_TIMEOUTS = "incfs_default_timeouts";
    private static final java.lang.String PROPERTY_IS_PRE_APPROVAL_REQUEST_AVAILABLE = "is_preapproval_available";
    private static final java.lang.String PROPERTY_IS_UPDATE_OWNERSHIP_ENFORCEMENT_AVAILABLE = "is_update_ownership_enforcement_available";
    private static final java.lang.String PROPERTY_KNOWN_DIGESTERS_LIST = "known_digesters_list";
    private static final long PRUNE_UNUSED_SHARED_LIBRARIES_DELAY;
    static final int PRUNE_UNUSED_STATIC_SHARED_LIBRARIES = 28;
    private static final int RADIO_UID = 1001;
    static final char RANDOM_CODEPATH_PREFIX = '-';
    static final java.lang.String RANDOM_DIR_PREFIX = "~~";
    public static final int REASON_AB_OTA = 10;
    public static final int REASON_BACKGROUND_DEXOPT = 9;
    public static final int REASON_BOOT_AFTER_MAINLINE_UPDATE = 13;
    public static final int REASON_BOOT_AFTER_OTA = 1;
    public static final int REASON_CMDLINE = 12;
    public static final int REASON_FIRST_BOOT = 0;
    public static final int REASON_INACTIVE_PACKAGE_DOWNGRADE = 11;
    public static final int REASON_INSTALL = 3;
    public static final int REASON_INSTALL_BULK = 5;
    public static final int REASON_INSTALL_BULK_DOWNGRADED = 7;
    public static final int REASON_INSTALL_BULK_SECONDARY = 6;
    public static final int REASON_INSTALL_BULK_SECONDARY_DOWNGRADED = 8;
    public static final int REASON_INSTALL_FAST = 4;
    public static int REASON_LAST = 0;
    public static final int REASON_POST_BOOT = 2;
    public static int REASON_SHARED = 0;
    private static final int REQUIRED_VERIFIERS_MAX_COUNT = 2;
    static final int SCAN_AS_APEX = 67108864;
    public static final int SCAN_AS_APK_IN_APEX = 8388608;
    static final int SCAN_AS_FACTORY = 33554432;
    static final int SCAN_AS_FULL_APP = 16384;
    static final int SCAN_AS_INSTANT_APP = 8192;
    static final int SCAN_AS_ODM = 4194304;
    static final int SCAN_AS_OEM = 262144;
    public static final int SCAN_AS_PRIVILEGED = 131072;
    public static final int SCAN_AS_PRODUCT = 1048576;
    static final int SCAN_AS_STOPPED_SYSTEM_APP = 134217728;
    public static final int SCAN_AS_SYSTEM = 65536;
    static final int SCAN_AS_SYSTEM_EXT = 2097152;
    public static final int SCAN_AS_VENDOR = 524288;
    static final int SCAN_AS_VIRTUAL_PRELOAD = 32768;
    static final int SCAN_BOOTING = 16;
    static final int SCAN_DONT_KILL_APP = 1024;
    static final int SCAN_DROP_CACHE = 16777216;
    static final int SCAN_FIRST_BOOT_OR_UPGRADE = 4096;
    static final int SCAN_IGNORE_FROZEN = 2048;
    static final int SCAN_INITIAL = 512;
    static final int SCAN_MOVE = 256;
    static final int SCAN_NEW_INSTALL = 4;
    public static final int SCAN_NO_DEX = 1;
    static final int SCAN_REQUIRE_KNOWN = 128;
    static final int SCAN_UPDATE_SIGNATURE = 2;
    static final int SCAN_UPDATE_TIME = 8;
    static final int SEND_PENDING_BROADCAST = 1;
    private static final int SE_UID = 1068;
    static final java.lang.String SHELL_PACKAGE_NAME = "com.android.shell";
    private static final int SHELL_UID = 2000;
    private static final java.lang.String STATIC_SHARED_LIB_DELIMITER = "_";
    public static final java.lang.String STUB_SUFFIX = "-Stub";
    public static final java.util.List<com.android.server.pm.ScanPartition> SYSTEM_PARTITIONS;
    static final java.lang.String TAG = "PackageManager";
    private static final long THROW_EXCEPTION_ON_REQUIRE_INSTALL_PACKAGES_TO_ADD_INSTALLER_PACKAGE = 150857253;
    private static final int UWB_UID = 1083;
    static final long WATCHDOG_TIMEOUT = 600000;
    static final int WRITE_DIRTY_PACKAGE_RESTRICTIONS = 14;
    static final int WRITE_PACKAGE_LIST = 19;
    static final int WRITE_SETTINGS = 13;
    static final int WRITE_SETTINGS_DELAY = 10000;
    static final int WRITE_USER_PACKAGE_RESTRICTIONS = 30;
    private static final java.util.concurrent.atomic.AtomicReference<com.android.server.pm.Computer> sSnapshot;
    private static final java.util.concurrent.atomic.AtomicInteger sSnapshotPendingVersion;
    public static com.android.server.pm.IPackageManagerServiceExt.IStaticExt sStaticExt;
    private static com.android.server.ThreadPriorityBooster sThreadPriorityBooster;
    final java.lang.String mAmbientContextDetectionPackage;

    @com.android.server.utils.Watched(manual = true)
    private android.content.pm.ApplicationInfo mAndroidApplication;
    final com.android.server.pm.ApexManager mApexManager;
    private final com.android.server.pm.AppDataHelper mAppDataHelper;
    private final java.io.File mAppInstallDir;
    final java.lang.String mAppPredictionServicePackage;

    @com.android.server.utils.Watched
    final com.android.server.pm.AppsFilterImpl mAppsFilter;
    final com.android.server.pm.dex.ArtManagerService mArtManagerService;
    private final android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> mAvailableFeatures;
    final android.os.Handler mBackgroundHandler;
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private java.io.File mCacheDir;
    final com.android.server.pm.ChangedPackagesTracker mChangedPackagesTracker;
    final com.android.server.pm.CompilerStats mCompilerStats;

    @com.android.server.utils.Watched
    final com.android.server.pm.resolution.ComponentResolver mComponentResolver;
    final java.lang.String mConfiguratorPackage;
    final android.content.Context mContext;
    android.content.ComponentName mCustomResolverComponentName;
    private final int mDefParseFlags;
    private final com.android.server.pm.DefaultAppProvider mDefaultAppProvider;
    final java.lang.String mDefaultTextClassifierPackage;
    public final com.android.server.pm.DeletePackageHelper mDeletePackageHelper;
    private android.app.admin.IDevicePolicyManager mDevicePolicyManager;
    private final com.android.server.pm.dex.DexManager mDexManager;
    private final com.android.server.pm.DexOptHelper mDexOptHelper;
    final android.util.ArraySet<java.lang.Integer> mDirtyUsers;
    private final com.android.server.pm.DistractingPackageHelper mDistractingPackageHelper;
    private final com.android.server.pm.DomainVerificationConnection mDomainVerificationConnection;
    final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;
    private final com.android.server.pm.dex.DynamicCodeLogger mDynamicCodeLogger;
    private android.util.ArraySet<java.lang.String> mExistingPackages;
    android.content.pm.PackageManagerInternal.ExternalSourcesPolicy mExternalSourcesPolicy;
    final boolean mFactoryTest;
    private boolean mFirstBoot;
    private final com.android.server.pm.FreeStorageHelper mFreeStorageHelper;
    final com.android.server.utils.WatchedArrayMap<java.lang.String, java.lang.Integer> mFrozenPackages;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayMap<java.lang.String, java.lang.Integer>> mFrozenPackagesSnapshot;
    final android.os.Handler mHandler;
    final java.lang.String mIncidentReportApproverPackage;
    final android.os.incremental.IncrementalManager mIncrementalManager;
    private final java.lang.String mIncrementalVersion;
    public final com.android.server.pm.InitAppsHelper mInitAppsHelper;
    final java.util.Set<java.lang.String> mInitialNonStoppedSystemPackages;
    final com.android.server.pm.PackageManagerServiceInjector mInjector;
    final com.android.server.pm.PackageManagerTracedLock mInstallLock;
    private final com.android.server.pm.InstallPackageHelper mInstallPackageHelper;
    final com.android.server.pm.Installer mInstaller;
    final com.android.server.pm.PackageInstallerService mInstallerService;

    @com.android.server.utils.Watched(manual = true)
    android.content.pm.ActivityInfo mInstantAppInstallerActivity;

    @com.android.server.utils.Watched(manual = true)
    private final android.content.pm.ResolveInfo mInstantAppInstallerInfo;

    @com.android.server.utils.Watched
    final com.android.server.pm.InstantAppRegistry mInstantAppRegistry;
    final com.android.server.pm.InstantAppResolverConnection mInstantAppResolverConnection;
    final android.content.ComponentName mInstantAppResolverSettingsComponent;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedInstrumentation> mInstrumentation;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedInstrumentation>> mInstrumentationSnapshot;
    final boolean mIsEngBuild;
    private final boolean mIsPreNMR1Upgrade;
    private final boolean mIsPreQUpgrade;
    private final boolean mIsUpgrade;
    private final boolean mIsUserDebugBuild;

    @com.android.server.utils.Watched
    final com.android.server.utils.WatchedSparseIntArray mIsolatedOwners;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseIntArray> mIsolatedOwnersSnapshot;
    private final android.util.ArraySet<java.lang.String> mKeepUninstalledPackages;
    private final com.android.server.pm.permission.LegacyPermissionManagerInternal mLegacyPermissionManager;
    private com.android.server.pm.ComputerLocked mLiveComputer;
    final com.android.server.pm.PackageManagerTracedLock mLock;
    final android.util.DisplayMetrics mMetrics;
    private final com.android.server.pm.ModuleInfoProvider mModuleInfoProvider;
    final com.android.server.pm.MovePackageHelper.MoveCallbacks mMoveCallbacks;
    int mNextInstallToken;
    private final java.util.concurrent.atomic.AtomicInteger mNextMoveId;
    private final java.util.Map<java.lang.String, com.android.server.pm.InstallRequest> mNoKillInstallObservers;
    private final com.android.internal.content.om.OverlayConfig mOverlayConfig;
    final java.lang.String mOverlayConfigSignaturePackage;
    private final com.android.server.pm.PackageManagerTracedLock mOverlayPathsLock;
    final com.android.server.pm.PackageDexOptimizer mPackageDexOptimizer;
    public com.android.server.pm.IPackageManagerServiceExt mPackageManagerServiceExt;
    com.android.server.pm.IPackageManagerServiceSocExt mPackageManagerServiceSocExt;
    private final com.android.server.pm.PackageMonitorCallbackHelper mPackageMonitorCallbackHelper;
    private final com.android.server.pm.PackageObserverHelper mPackageObserverHelper;
    final com.android.internal.pm.parsing.PackageParser2.Callback mPackageParserCallback;
    private final com.android.server.pm.PackageProperty mPackageProperty;
    private final com.android.server.pm.pkg.mutate.PackageStateMutator mPackageStateMutator;
    private final com.android.server.pm.PackageManagerTracedLock mPackageStateWriteLock;
    private final com.android.server.pm.PackageUsage mPackageUsage;

    @com.android.server.utils.Watched
    final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> mPackages;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage>> mPackagesSnapshot;
    final com.android.server.pm.PendingPackageBroadcasts mPendingBroadcasts;
    final android.util.SparseArray<com.android.server.pm.VerifyingSession> mPendingEnableRollback;
    int mPendingEnableRollbackToken;
    private final java.util.Map<java.lang.String, com.android.server.pm.InstallRequest> mPendingKillInstallObservers;
    final android.util.SparseArray<com.android.server.pm.PackageVerificationState> mPendingVerification;
    int mPendingVerificationToken;
    android.os.incremental.PerUidReadTimeouts[] mPerUidReadTimeoutsCache;
    final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManager;
    private com.android.server.pm.pkg.AndroidPackage mPlatformPackage;
    private java.lang.String[] mPlatformPackageOverlayPaths;
    private java.lang.String[] mPlatformPackageOverlayResourceDirs;
    private final com.android.server.pm.PreferredActivityHelper mPreferredActivityHelper;
    private java.util.concurrent.Future<?> mPrepareAppDataFuture;
    private final int mPriorSdkVersion;
    final com.android.server.pm.ProcessLoggingHandler mProcessLoggingHandler;
    boolean mPromoteSystemApps;
    final android.util.ArraySet<java.lang.String> mProtectedBroadcasts;
    final com.android.server.pm.ProtectedPackages mProtectedPackages;
    final java.lang.String mRecentsPackage;
    java.util.List<java.io.File> mReleaseOnSystemReady;
    private final com.android.server.pm.RemovePackageHelper mRemovePackageHelper;
    private java.lang.String[] mReplacedResolverPackageOverlayPaths;
    private java.lang.String[] mReplacedResolverPackageOverlayResourceDirs;
    final java.lang.String mRequiredInstallerPackage;
    final java.lang.String mRequiredPermissionControllerPackage;
    private final java.lang.String mRequiredSdkSandboxPackage;
    final java.lang.String mRequiredUninstallerPackage;
    final java.lang.String[] mRequiredVerifierPackages;

    @com.android.server.utils.Watched(manual = true)
    private final android.content.pm.ActivityInfo mResolveActivity;

    @com.android.server.utils.Watched(manual = true)
    android.content.ComponentName mResolveComponentName;
    private final android.content.pm.ResolveInfo mResolveInfo;
    private final com.android.server.pm.ResolveIntentHelper mResolveIntentHelper;
    private boolean mResolverReplaced;
    final java.lang.String mRetailDemoPackage;
    final android.util.SparseArray<com.android.server.pm.InstallRequest> mRunningInstalls;

    @com.android.server.utils.Watched(manual = true)
    private volatile boolean mSafeMode;
    private final int mSdkVersion;
    private final java.lang.String[] mSeparateProcesses;
    private long mServiceStartWithDelay;
    final java.lang.String mServicesExtensionPackageName;

    @com.android.server.utils.Watched
    final com.android.server.pm.Settings mSettings;
    final java.lang.String mSetupWizardPackage;

    @com.android.server.utils.Watched
    private final com.android.server.pm.SharedLibrariesImpl mSharedLibraries;
    final java.lang.String mSharedSystemSharedLibraryPackageName;
    final boolean mShouldStopSystemPackagesByDefault;
    private final java.lang.Object mSnapshotLock;
    private final com.android.server.pm.SnapshotStatistics mSnapshotStatistics;
    private final com.android.server.pm.StorageEventHelper mStorageEventHelper;
    final java.lang.String mStorageManagerPackage;
    private final com.android.server.pm.SuspendPackageHelper mSuspendPackageHelper;

    @com.android.server.utils.Watched(manual = true)
    private volatile boolean mSystemReady;
    final java.lang.String mSystemTextClassifierPackageName;
    private final android.content.pm.TestUtilityService mTestUtilityService;
    final android.util.ArraySet<java.lang.String> mTransferredPackages;
    final com.android.server.pm.UserManagerService mUserManager;
    final com.android.server.pm.UserNeedsBadgingCache mUserNeedsBadging;
    private final com.android.server.utils.Watcher mWatcher;
    final java.lang.String mWearableSensingPackage;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseBooleanArray mWebInstantAppsDisabled;
    private final com.android.server.pm.IPackageManagerServiceWrapper mWrapper;
    public static boolean DEBUG_SETTINGS = false;
    public static boolean DEBUG_PREFERRED = false;
    public static boolean DEBUG_UPGRADE = false;
    public static boolean DEBUG_DOMAIN_VERIFICATION = false;
    public static boolean DEBUG_BACKUP = false;
    public static boolean DEBUG_INSTALL = false;
    public static boolean DEBUG_REMOVE = false;
    public static boolean DEBUG_PACKAGE_INFO = false;
    public static boolean DEBUG_INTENT_MATCHING = false;
    public static boolean DEBUG_PACKAGE_SCANNING = false;
    public static boolean DEBUG_VERIFY = false;
    public static boolean DEBUG_PERMISSIONS = false;
    public static boolean DEBUG_COMPRESSION = android.os.Build.IS_DEBUGGABLE;
    public static boolean TRACE_SNAPSHOTS = false;
    private static boolean DEBUG_PER_UID_READ_TIMEOUTS = false;
    public static boolean DEBUG_ART_STATSLOG = android.os.SystemProperties.getBoolean("persist.sys.pm.art.statslog", false);
    public static boolean DEBUG_DEXOPT = false;
    public static boolean DEBUG_ABI_SELECTION = false;
    public static boolean DEBUG_INSTANT = android.os.Build.IS_DEBUGGABLE;
    public static boolean DEBUG_APP_DATA = false;
    public static boolean DEBUG_BROADCASTS = false;
    public static boolean DEBUG_SHARED_LIBRARIES = false;
    static final int[] EMPTY_INT_ARRAY = new int[0];

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PackageStartability {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ScanFlags {
    }

    static {
        MIN_INSTALLABLE_TARGET_SDK = com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.minTargetSdk24() ? 24 : 23;
        REASON_SHARED = 14;
        REASON_LAST = REASON_SHARED;
        sStaticExt = (com.android.server.pm.IPackageManagerServiceExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceExt.IStaticExt.class).create();
        SYSTEM_PARTITIONS = java.util.Collections.unmodifiableList(android.content.pm.PackagePartitions.getOrderedPartitions(new java.util.function.Function() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda21
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return new com.android.server.pm.ScanPartition((android.content.pm.PackagePartitions.SystemPartition) obj);
            }
        }));
        EMPTY_PER_UID_READ_TIMEOUTS_ARRAY = new android.os.incremental.PerUidReadTimeouts[0];
        DEFERRED_NO_KILL_POST_DELETE_DELAY_MS_EXTENDED = java.util.concurrent.TimeUnit.DAYS.toMillis(1L);
        PRUNE_UNUSED_SHARED_LIBRARIES_DELAY = java.util.concurrent.TimeUnit.MINUTES.toMillis(3L);
        DEFAULT_UNUSED_STATIC_SHARED_LIB_MIN_CACHE_PERIOD = java.util.concurrent.TimeUnit.DAYS.toMillis(7L);
        sThreadPriorityBooster = new com.android.server.ThreadPriorityBooster(-2, 3);
        sSnapshot = new java.util.concurrent.atomic.AtomicReference<>();
        sSnapshotPendingVersion = new java.util.concurrent.atomic.AtomicInteger(1);
        BACKGROUND_HANDLER_CALLBACK = new android.os.Handler.Callback() { // from class: com.android.server.pm.PackageManagerService.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 14:
                        android.os.Trace.traceBegin(262144L, "PackageManagerBg writePendingRestrictions");
                        com.android.server.pm.PackageManagerService pm = (com.android.server.pm.PackageManagerService) msg.obj;
                        pm.writePendingRestrictions();
                        android.os.Trace.traceEnd(262144L);
                        break;
                    case 30:
                        android.os.Trace.traceBegin(262144L, "PackageManagerBg writePackageRestrictions");
                        java.lang.Runnable r = (java.lang.Runnable) msg.obj;
                        r.run();
                        android.os.Trace.traceEnd(262144L);
                        break;
                }
                return true;
            }
        };
    }

    private static class DefaultSystemWrapper implements com.android.server.pm.PackageManagerServiceInjector.SystemWrapper {
        private DefaultSystemWrapper() {
        }

        @Override // com.android.server.pm.PackageManagerServiceInjector.SystemWrapper
        public void disablePackageCaches() {
            android.content.pm.PackageManager.disableApplicationInfoCache();
            android.content.pm.PackageManager.disablePackageInfoCache();
            android.app.ApplicationPackageManager.invalidateGetPackagesForUidCache();
            android.app.ApplicationPackageManager.disableGetPackagesForUidCache();
            android.app.ApplicationPackageManager.invalidateHasSystemFeatureCache();
            android.content.pm.PackageManager.corkPackageInfoCache();
        }

        @Override // com.android.server.pm.PackageManagerServiceInjector.SystemWrapper
        public void enablePackageCaches() {
            android.content.pm.PackageManager.uncorkPackageInfoCache();
        }
    }

    public static void boostPriorityForPackageManagerTracedLockedSection() {
    }

    public static void resetPriorityAfterPackageManagerTracedLockedSection() {
    }

    public static void invalidatePackageInfoCache() {
        android.content.pm.PackageManager.invalidatePackageInfoCache();
        onChanged();
    }

    class Snapshot {
        public static final int LIVE = 1;
        public static final int SNAPPED = 2;
        public final android.content.pm.ApplicationInfo androidApplication;
        public final java.lang.String appPredictionServicePackage;
        public final com.android.server.pm.AppsFilterSnapshot appsFilter;
        public final com.android.server.pm.resolution.ComponentResolverApi componentResolver;
        public final com.android.server.utils.WatchedArrayMap<java.lang.String, java.lang.Integer> frozenPackages;
        public final android.content.pm.ActivityInfo instantAppInstallerActivity;
        public final android.content.pm.ResolveInfo instantAppInstallerInfo;
        public final com.android.server.pm.InstantAppRegistry instantAppRegistry;
        public final com.android.server.utils.WatchedArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedInstrumentation> instrumentation;
        public final com.android.server.utils.WatchedSparseIntArray isolatedOwners;
        public final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> packages;
        public final android.content.pm.ActivityInfo resolveActivity;
        public final android.content.ComponentName resolveComponentName;
        public final com.android.server.pm.PackageManagerService service;
        public final com.android.server.pm.Settings settings;
        public final com.android.server.pm.SharedLibrariesRead sharedLibraries;
        public final com.android.server.utils.WatchedSparseBooleanArray webInstantAppsDisabled;

        Snapshot(int type) {
            android.content.pm.ActivityInfo activityInfo;
            if (type == 2) {
                this.settings = com.android.server.pm.PackageManagerService.this.mSettings.snapshot();
                this.isolatedOwners = (com.android.server.utils.WatchedSparseIntArray) com.android.server.pm.PackageManagerService.this.mIsolatedOwnersSnapshot.snapshot();
                this.packages = (com.android.server.utils.WatchedArrayMap) com.android.server.pm.PackageManagerService.this.mPackagesSnapshot.snapshot();
                this.instrumentation = (com.android.server.utils.WatchedArrayMap) com.android.server.pm.PackageManagerService.this.mInstrumentationSnapshot.snapshot();
                this.resolveComponentName = com.android.server.pm.PackageManagerService.this.mResolveComponentName == null ? null : com.android.server.pm.PackageManagerService.this.mResolveComponentName.clone();
                this.resolveActivity = new android.content.pm.ActivityInfo(com.android.server.pm.PackageManagerService.this.mResolveActivity);
                if (com.android.server.pm.PackageManagerService.this.mInstantAppInstallerActivity == null) {
                    activityInfo = null;
                } else {
                    activityInfo = new android.content.pm.ActivityInfo(com.android.server.pm.PackageManagerService.this.mInstantAppInstallerActivity);
                }
                this.instantAppInstallerActivity = activityInfo;
                this.instantAppInstallerInfo = new android.content.pm.ResolveInfo(com.android.server.pm.PackageManagerService.this.mInstantAppInstallerInfo);
                this.webInstantAppsDisabled = com.android.server.pm.PackageManagerService.this.mWebInstantAppsDisabled.snapshot();
                this.instantAppRegistry = com.android.server.pm.PackageManagerService.this.mInstantAppRegistry.snapshot();
                this.androidApplication = com.android.server.pm.PackageManagerService.this.mAndroidApplication != null ? new android.content.pm.ApplicationInfo(com.android.server.pm.PackageManagerService.this.mAndroidApplication) : null;
                this.appPredictionServicePackage = com.android.server.pm.PackageManagerService.this.mAppPredictionServicePackage;
                this.appsFilter = com.android.server.pm.PackageManagerService.this.mAppsFilter.snapshot();
                this.componentResolver = com.android.server.pm.PackageManagerService.this.mComponentResolver.snapshot();
                this.frozenPackages = (com.android.server.utils.WatchedArrayMap) com.android.server.pm.PackageManagerService.this.mFrozenPackagesSnapshot.snapshot();
                this.sharedLibraries = com.android.server.pm.PackageManagerService.this.mSharedLibraries.snapshot();
            } else if (type == 1) {
                this.settings = com.android.server.pm.PackageManagerService.this.mSettings;
                this.isolatedOwners = com.android.server.pm.PackageManagerService.this.mIsolatedOwners;
                this.packages = com.android.server.pm.PackageManagerService.this.mPackages;
                this.instrumentation = com.android.server.pm.PackageManagerService.this.mInstrumentation;
                this.resolveComponentName = com.android.server.pm.PackageManagerService.this.mResolveComponentName;
                this.resolveActivity = com.android.server.pm.PackageManagerService.this.mResolveActivity;
                this.instantAppInstallerActivity = com.android.server.pm.PackageManagerService.this.mInstantAppInstallerActivity;
                this.instantAppInstallerInfo = com.android.server.pm.PackageManagerService.this.mInstantAppInstallerInfo;
                this.webInstantAppsDisabled = com.android.server.pm.PackageManagerService.this.mWebInstantAppsDisabled;
                this.instantAppRegistry = com.android.server.pm.PackageManagerService.this.mInstantAppRegistry;
                this.androidApplication = com.android.server.pm.PackageManagerService.this.mAndroidApplication;
                this.appPredictionServicePackage = com.android.server.pm.PackageManagerService.this.mAppPredictionServicePackage;
                this.appsFilter = com.android.server.pm.PackageManagerService.this.mAppsFilter;
                this.componentResolver = com.android.server.pm.PackageManagerService.this.mComponentResolver;
                this.frozenPackages = com.android.server.pm.PackageManagerService.this.mFrozenPackages;
                this.sharedLibraries = com.android.server.pm.PackageManagerService.this.mSharedLibraries;
            } else {
                throw new java.lang.IllegalArgumentException();
            }
            this.service = com.android.server.pm.PackageManagerService.this;
        }
    }

    public com.android.server.pm.Computer snapshotComputer() {
        return snapshotComputer(true);
    }

    @java.lang.Deprecated
    public com.android.server.pm.Computer snapshotComputer(boolean allowLiveComputer) throws java.lang.Throwable {
        boolean isHoldingPackageLock = java.lang.Thread.holdsLock(this.mLock);
        if (allowLiveComputer && isHoldingPackageLock) {
            return this.mLiveComputer;
        }
        if (this.mPackageManagerServiceExt.shouldUseLiveComputerInSnapshotComputer()) {
            return this.mLiveComputer;
        }
        com.android.server.pm.Computer oldSnapshot = sSnapshot.get();
        int pendingVersion = sSnapshotPendingVersion.get();
        if (oldSnapshot != null && oldSnapshot.getVersion() == pendingVersion) {
            return oldSnapshot.use();
        }
        if (isHoldingPackageLock) {
            com.android.server.pm.Computer newSnapshot = rebuildSnapshot(oldSnapshot, pendingVersion);
            sSnapshot.set(newSnapshot);
            return newSnapshot.use();
        }
        synchronized (this.mSnapshotLock) {
            com.android.server.pm.Computer rebuildSnapshot = sSnapshot.get();
            int rebuildVersion = sSnapshotPendingVersion.get();
            if (rebuildSnapshot != null && rebuildSnapshot.getVersion() == rebuildVersion) {
                return rebuildSnapshot.use();
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.Computer rebuildSnapshot2 = sSnapshot.get();
                    int rebuildVersion2 = sSnapshotPendingVersion.get();
                    if (rebuildSnapshot2 != null && rebuildSnapshot2.getVersion() == rebuildVersion2) {
                        com.android.server.pm.Computer computerUse = rebuildSnapshot2.use();
                        resetPriorityAfterPackageManagerTracedLockedSection();
                        return computerUse;
                    }
                    com.android.server.pm.Computer newSnapshot2 = rebuildSnapshot(rebuildSnapshot2, rebuildVersion2);
                    sSnapshot.set(newSnapshot2);
                    com.android.server.pm.Computer computerUse2 = newSnapshot2.use();
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    return computerUse2;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
        }
    }

    private com.android.server.pm.Computer rebuildSnapshot(com.android.server.pm.Computer oldSnapshot, int newVersion) throws java.lang.Throwable {
        long now = android.os.SystemClock.currentTimeMicro();
        int hits = oldSnapshot == null ? -1 : oldSnapshot.getUsed();
        com.android.server.pm.PackageManagerService.Snapshot args = new com.android.server.pm.PackageManagerService.Snapshot(2);
        com.android.server.pm.ComputerEngine newSnapshot = new com.android.server.pm.ComputerEngine(args, newVersion);
        long done = android.os.SystemClock.currentTimeMicro();
        if (this.mSnapshotStatistics != null) {
            this.mSnapshotStatistics.rebuild(now, done, hits, newSnapshot.getPackageStates().size());
        }
        return newSnapshot;
    }

    private com.android.server.pm.ComputerLocked createLiveComputer() {
        return new com.android.server.pm.ComputerLocked(new com.android.server.pm.PackageManagerService.Snapshot(1));
    }

    public static void onChange(com.android.server.utils.Watchable what) {
        if (TRACE_SNAPSHOTS) {
            android.util.Log.i(TAG, "snapshot: onChange(" + what + ")");
        }
        sSnapshotPendingVersion.incrementAndGet();
    }

    static void onChanged() {
        onChange(null);
    }

    void notifyInstallObserver(java.lang.String packageName, boolean killApp) {
        com.android.server.pm.InstallRequest installRequest = killApp ? this.mPendingKillInstallObservers.remove(packageName) : this.mNoKillInstallObservers.remove(packageName);
        if (installRequest != null) {
            notifyInstallObserver(installRequest);
        }
    }

    void notifyInstallObserver(com.android.server.pm.InstallRequest request) {
        if (DEBUG_INSTALL) {
            android.util.Slog.d(TAG, "notifyInstallObserver " + (request == null ? null : ", " + request.getName() + ", " + request.getReturnCode() + ", " + request.getReturnMsg()));
        }
        if (request.getObserver() != null) {
            try {
                android.os.Bundle extras = extrasForInstallResult(request);
                request.getObserver().onPackageInstalled(request.getName(), request.getReturnCode(), request.getReturnMsg(), extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.i(TAG, "Observer no longer exists.");
            }
        }
        this.mPackageManagerServiceExt.onNotifyInstallObserver(request.getName(), request.getReturnCode());
    }

    void scheduleDeferredNoKillInstallObserver(com.android.server.pm.InstallRequest request) {
        java.lang.String packageName = request.getPkg().getPackageName();
        this.mNoKillInstallObservers.put(packageName, request);
        android.os.Message message = this.mHandler.obtainMessage(24, packageName);
        this.mHandler.sendMessageDelayed(message, 500L);
    }

    void scheduleDeferredNoKillPostDelete(com.android.server.pm.CleanUpArgs args) {
        android.os.Message message = this.mHandler.obtainMessage(23, args);
        long deleteDelayMillis = 3000;
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.improveInstallDontKill()) {
            deleteDelayMillis = ((java.lang.Long) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda4
                public final java.lang.Object getOrThrow() {
                    return java.lang.Long.valueOf(android.provider.DeviceConfig.getLong("package_manager_service", com.android.server.pm.PackageManagerService.PROPERTY_DEFERRED_NO_KILL_POST_DELETE_DELAY_MS_EXTENDED, com.android.server.pm.PackageManagerService.DEFERRED_NO_KILL_POST_DELETE_DELAY_MS_EXTENDED));
                }
            })).longValue();
            android.util.Slog.w(TAG, "Delaying the deletion of <" + args.getCodePath() + "> by " + deleteDelayMillis + "ms or till the next reboot");
        }
        this.mHandler.sendMessageDelayed(message, deleteDelayMillis);
    }

    void schedulePruneUnusedStaticSharedLibraries(boolean delay) {
        this.mHandler.removeMessages(28);
        this.mHandler.sendEmptyMessageDelayed(28, delay ? getPruneUnusedSharedLibrariesDelay() : 0L);
    }

    void scheduleDeferredPendingKillInstallObserver(com.android.server.pm.InstallRequest request) {
        java.lang.String packageName = request.getPkg().getPackageName();
        this.mPendingKillInstallObservers.put(packageName, request);
        android.os.Message message = this.mHandler.obtainMessage(29, packageName);
        this.mHandler.sendMessageDelayed(message, 1000L);
    }

    private static long getPruneUnusedSharedLibrariesDelay() {
        return android.os.SystemProperties.getLong("debug.pm.prune_unused_shared_libraries_delay", PRUNE_UNUSED_SHARED_LIBRARIES_DELAY);
    }

    public void requestFileChecksums(java.io.File file, final java.lang.String installerPackageName, final int optional, final int required, java.util.List trustedInstallers, final android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener) throws java.io.FileNotFoundException {
        if (!file.exists()) {
            throw new java.io.FileNotFoundException(file.getAbsolutePath());
        }
        java.util.concurrent.Executor executor = this.mInjector.getBackgroundExecutor();
        final android.os.Handler handler = this.mInjector.getBackgroundHandler();
        final java.security.cert.Certificate[] trustedCerts = trustedInstallers != null ? decodeCertificates(trustedInstallers) : null;
        final java.util.List<android.util.Pair<java.lang.String, java.io.File>> filesToChecksum = new java.util.ArrayList<>(1);
        filesToChecksum.add(android.util.Pair.create(null, file));
        executor.execute(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$requestFileChecksums$4(handler, filesToChecksum, optional, required, installerPackageName, trustedCerts, onChecksumsReadyListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestFileChecksums$4(final android.os.Handler handler, java.util.List filesToChecksum, int optional, int required, java.lang.String installerPackageName, java.security.cert.Certificate[] trustedCerts, android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener) {
        com.android.server.pm.ApkChecksums.Injector.Producer producer = new com.android.server.pm.ApkChecksums.Injector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda24
            @Override // com.android.server.pm.ApkChecksums.Injector.Producer
            public final java.lang.Object produce() {
                return this.f$0.lambda$requestFileChecksums$1();
            }
        };
        com.android.server.pm.ApkChecksums.Injector.Producer producer2 = new com.android.server.pm.ApkChecksums.Injector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda25
            @Override // com.android.server.pm.ApkChecksums.Injector.Producer
            public final java.lang.Object produce() {
                return com.android.server.pm.PackageManagerService.lambda$requestFileChecksums$2(handler);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector = this.mInjector;
        java.util.Objects.requireNonNull(packageManagerServiceInjector);
        com.android.server.pm.ApkChecksums.Injector injector = new com.android.server.pm.ApkChecksums.Injector(producer, producer2, new com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda14(packageManagerServiceInjector), new com.android.server.pm.ApkChecksums.Injector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda26
            @Override // com.android.server.pm.ApkChecksums.Injector.Producer
            public final java.lang.Object produce() {
                return this.f$0.lambda$requestFileChecksums$3();
            }
        });
        com.android.server.pm.ApkChecksums.getChecksums(filesToChecksum, optional, required, installerPackageName, trustedCerts, onChecksumsReadyListener, injector);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Context lambda$requestFileChecksums$1() {
        return this.mContext;
    }

    static /* synthetic */ android.os.Handler lambda$requestFileChecksums$2(android.os.Handler handler) {
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.PackageManagerInternal lambda$requestFileChecksums$3() {
        return (android.content.pm.PackageManagerInternal) this.mInjector.getLocalService(android.content.pm.PackageManagerInternal.class);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    void requestChecksumsInternal(com.android.server.pm.Computer snapshot, java.lang.String packageName, boolean includeSplits, final int optional, final int required, java.util.List trustedInstallers, final android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener, int userId, java.util.concurrent.Executor executor, final android.os.Handler handler) throws android.os.ParcelableException {
        java.lang.String installerPackageName;
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(onChecksumsReadyListener);
        java.util.Objects.requireNonNull(executor);
        java.util.Objects.requireNonNull(handler);
        android.content.pm.ApplicationInfo applicationInfo = snapshot.getApplicationInfoInternal(packageName, 0L, android.os.Binder.getCallingUid(), userId);
        if (applicationInfo != null) {
            android.content.pm.InstallSourceInfo installSourceInfo = snapshot.getInstallSourceInfo(packageName, userId);
            if (installSourceInfo != null) {
                java.lang.String initiatingPackageName = installSourceInfo.getInitiatingPackageName();
                if (!com.android.server.pm.PackageManagerServiceUtils.isInstalledByAdb(initiatingPackageName)) {
                    installerPackageName = initiatingPackageName;
                } else {
                    installerPackageName = installSourceInfo.getInstallingPackageName();
                }
            } else {
                installerPackageName = null;
            }
            final java.util.List<android.util.Pair<java.lang.String, java.io.File>> filesToChecksum = new java.util.ArrayList<>();
            filesToChecksum.add(android.util.Pair.create(null, new java.io.File(applicationInfo.sourceDir)));
            if (includeSplits && applicationInfo.splitNames != null) {
                int size = applicationInfo.splitNames.length;
                for (int i = 0; i < size; i++) {
                    filesToChecksum.add(android.util.Pair.create(applicationInfo.splitNames[i], new java.io.File(applicationInfo.splitSourceDirs[i])));
                }
            }
            final java.security.cert.Certificate[] trustedCerts = trustedInstallers != null ? decodeCertificates(trustedInstallers) : null;
            final java.lang.String str = installerPackageName;
            executor.execute(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda64
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$requestChecksumsInternal$8(handler, filesToChecksum, optional, required, str, trustedCerts, onChecksumsReadyListener);
                }
            });
            return;
        }
        throw new android.os.ParcelableException(new android.content.pm.PackageManager.NameNotFoundException(packageName));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestChecksumsInternal$8(final android.os.Handler handler, java.util.List filesToChecksum, int optional, int required, java.lang.String installerPackageName, java.security.cert.Certificate[] trustedCerts, android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener) {
        com.android.server.pm.ApkChecksums.Injector.Producer producer = new com.android.server.pm.ApkChecksums.Injector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda12
            @Override // com.android.server.pm.ApkChecksums.Injector.Producer
            public final java.lang.Object produce() {
                return this.f$0.lambda$requestChecksumsInternal$5();
            }
        };
        com.android.server.pm.ApkChecksums.Injector.Producer producer2 = new com.android.server.pm.ApkChecksums.Injector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda13
            @Override // com.android.server.pm.ApkChecksums.Injector.Producer
            public final java.lang.Object produce() {
                return com.android.server.pm.PackageManagerService.lambda$requestChecksumsInternal$6(handler);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector = this.mInjector;
        java.util.Objects.requireNonNull(packageManagerServiceInjector);
        com.android.server.pm.ApkChecksums.Injector injector = new com.android.server.pm.ApkChecksums.Injector(producer, producer2, new com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda14(packageManagerServiceInjector), new com.android.server.pm.ApkChecksums.Injector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda15
            @Override // com.android.server.pm.ApkChecksums.Injector.Producer
            public final java.lang.Object produce() {
                return this.f$0.lambda$requestChecksumsInternal$7();
            }
        });
        com.android.server.pm.ApkChecksums.getChecksums(filesToChecksum, optional, required, installerPackageName, trustedCerts, onChecksumsReadyListener, injector);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Context lambda$requestChecksumsInternal$5() {
        return this.mContext;
    }

    static /* synthetic */ android.os.Handler lambda$requestChecksumsInternal$6(android.os.Handler handler) {
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.PackageManagerInternal lambda$requestChecksumsInternal$7() {
        return (android.content.pm.PackageManagerInternal) this.mInjector.getLocalService(android.content.pm.PackageManagerInternal.class);
    }

    private static java.security.cert.Certificate[] decodeCertificates(java.util.List certs) {
        try {
            java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate[] result = new java.security.cert.Certificate[certs.size()];
            int size = certs.size();
            for (int i = 0; i < size; i++) {
                java.io.InputStream is = new java.io.ByteArrayInputStream((byte[]) certs.get(i));
                java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate) cf.generateCertificate(is);
                result[i] = cert;
            }
            return result;
        } catch (java.security.cert.CertificateException e) {
            throw android.util.ExceptionUtils.propagate(e);
        }
    }

    private static android.os.Bundle extrasForInstallResult(com.android.server.pm.InstallRequest request) {
        android.os.Bundle extras = null;
        switch (request.getReturnCode()) {
            case -112:
                extras = new android.os.Bundle();
                extras.putString("android.content.pm.extra.FAILURE_EXISTING_PERMISSION", request.getOrigPermission());
                extras.putString("android.content.pm.extra.FAILURE_EXISTING_PACKAGE", request.getOrigPackage());
                break;
            case 1:
                extras = new android.os.Bundle();
                extras.putBoolean("android.intent.extra.REPLACING", (request.getRemovedInfo() == null || request.getRemovedInfo().mRemovedPackage == null) ? false : true);
                break;
        }
        if (!request.getWarnings().isEmpty()) {
            extras.putStringArrayList("android.content.pm.extra.WARNINGS", request.getWarnings());
        }
        return extras;
    }

    android.content.pm.ArchivedPackageParcel getArchivedPackageInternal(java.lang.String packageName, int userId) {
        java.util.Objects.requireNonNull(packageName);
        int binderUid = android.os.Binder.getCallingUid();
        com.android.server.pm.Computer snapshot = snapshotComputer();
        snapshot.enforceCrossUserPermission(binderUid, userId, true, true, "getArchivedPackage");
        android.content.pm.ArchivedPackageParcel archPkg = new android.content.pm.ArchivedPackageParcel();
        archPkg.packageName = packageName;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.PackageSetting ps = this.mSettings.getPackageLPr(packageName);
                if (ps == null) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    return null;
                }
                com.android.server.pm.pkg.PackageUserStateInternal psi = ps.getUserStateOrDefault(userId);
                com.android.server.pm.pkg.ArchiveState archiveState = psi.getArchiveState();
                if (archiveState == null && !psi.isInstalled()) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    return null;
                }
                archPkg.signingDetails = ps.getSigningDetails();
                long longVersionCode = ps.getVersionCode();
                archPkg.versionCodeMajor = (int) (longVersionCode >> 32);
                archPkg.versionCode = (int) longVersionCode;
                archPkg.targetSdkVersion = ps.getTargetSdkVersion();
                archPkg.defaultToDeviceProtectedStorage = java.lang.String.valueOf(ps.isDefaultToDeviceProtectedStorage());
                archPkg.requestLegacyExternalStorage = java.lang.String.valueOf(ps.isRequestLegacyExternalStorage());
                archPkg.userDataFragile = java.lang.String.valueOf(ps.isUserDataFragile());
                resetPriorityAfterPackageManagerTracedLockedSection();
                try {
                    if (archiveState != null) {
                        archPkg.archivedActivities = com.android.server.pm.PackageArchiver.createArchivedActivities(archiveState);
                    } else {
                        int iconSize = ((android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class)).getLauncherLargeIconSize();
                        java.util.List<android.content.pm.LauncherActivityInfo> mainActivities = this.mInstallerService.mPackageArchiver.getLauncherActivityInfos(packageName, userId);
                        archPkg.archivedActivities = com.android.server.pm.PackageArchiver.createArchivedActivities(mainActivities, iconSize);
                    }
                    return archPkg;
                } catch (java.lang.Exception e) {
                    throw new java.lang.IllegalArgumentException("Package does not have a main activity", e);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    void markPackageAsArchivedIfNeeded(com.android.server.pm.PackageSetting pkgSetting, android.content.pm.ArchivedPackageParcel archivePackage, android.util.SparseArray<java.lang.String> responsibleInstallerTitles, int[] userIds) {
        if (pkgSetting == null || archivePackage == null || archivePackage.archivedActivities == null || responsibleInstallerTitles == null || userIds == null || userIds.length == 0) {
            return;
        }
        pkgSetting.setPkg(null).setPendingRestore(true);
        for (int i : userIds) {
            pkgSetting.modifyUserState(i).setInstalled(false);
        }
        java.lang.String responsibleInstallerPackage = com.android.server.pm.PackageArchiver.getResponsibleInstallerPackage(pkgSetting);
        if (android.text.TextUtils.isEmpty(responsibleInstallerPackage)) {
            android.util.Slog.e(TAG, "Can't create archive state: responsible installer is empty");
            return;
        }
        for (int userId : userIds) {
            com.android.server.pm.pkg.ArchiveState archiveState = this.mInstallerService.mPackageArchiver.createArchiveState(archivePackage, userId, responsibleInstallerPackage, responsibleInstallerTitles.get(userId));
            if (archiveState != null) {
                pkgSetting.modifyUserState(userId).setArchiveState(archiveState);
            }
        }
    }

    public void scheduleWriteSettings() {
        invalidatePackageInfoCache();
        if (!this.mHandler.hasMessages(13)) {
            this.mHandler.sendEmptyMessageDelayed(13, 10000L);
        }
    }

    void scheduleWritePackageListLocked(int userId) {
        invalidatePackageInfoCache();
        if (!this.mHandler.hasMessages(19)) {
            android.os.Message msg = this.mHandler.obtainMessage(19);
            msg.arg1 = userId;
            this.mHandler.sendMessageDelayed(msg, 10000L);
        }
    }

    void scheduleWritePackageRestrictions(android.os.UserHandle user) {
        int userId = user == null ? -1 : user.getIdentifier();
        scheduleWritePackageRestrictions(userId);
    }

    void scheduleWritePackageRestrictions(int userId) {
        invalidatePackageInfoCache();
        if (userId == -1) {
            synchronized (this.mDirtyUsers) {
                for (int aUserId : this.mUserManager.getUserIds()) {
                    this.mDirtyUsers.add(java.lang.Integer.valueOf(aUserId));
                }
            }
        } else {
            if (!this.mUserManager.exists(userId)) {
                return;
            }
            synchronized (this.mDirtyUsers) {
                this.mDirtyUsers.add(java.lang.Integer.valueOf(userId));
            }
        }
        if (!this.mBackgroundHandler.hasMessages(14)) {
            this.mBackgroundHandler.sendMessageDelayed(this.mBackgroundHandler.obtainMessage(14, this), 10000L);
        }
    }

    void writePendingRestrictions() {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mBackgroundHandler.removeMessages(14);
                synchronized (this.mDirtyUsers) {
                    if (this.mDirtyUsers.isEmpty()) {
                        resetPriorityAfterPackageManagerTracedLockedSection();
                        return;
                    }
                    java.lang.Integer[] dirtyUsers = (java.lang.Integer[]) this.mDirtyUsers.toArray(new java.util.function.IntFunction() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda66
                        @Override // java.util.function.IntFunction
                        public final java.lang.Object apply(int i) {
                            return com.android.server.pm.PackageManagerService.lambda$writePendingRestrictions$9(i);
                        }
                    });
                    this.mDirtyUsers.clear();
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    this.mPackageManagerServiceExt.adjustWritePackageRestrictionsInHandler(false);
                    this.mSettings.writePackageRestrictions(dirtyUsers);
                    this.mPackageManagerServiceExt.adjustWritePackageRestrictionsInHandler(true);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    static /* synthetic */ java.lang.Integer[] lambda$writePendingRestrictions$9(int x$0) {
        return new java.lang.Integer[x$0];
    }

    void writeSettings(boolean sync) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mHandler.removeMessages(13);
                this.mBackgroundHandler.removeMessages(14);
                writeSettingsLPrTEMP(sync);
                synchronized (this.mDirtyUsers) {
                    this.mDirtyUsers.clear();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
    }

    void writePackageList(int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mHandler.removeMessages(19);
                this.mSettings.writePackageListLPr(userId);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
    }

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
    public static com.android.server.pm.PackageManagerService main(final android.content.Context context, final com.android.server.pm.Installer installer, final com.android.server.pm.verify.domain.DomainVerificationService domainVerificationService, boolean factoryTest) {
        com.android.server.pm.PackageManagerServiceCompilerMapping.checkProperties();
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog("PackageManagerTiming", 262144L);
        t.traceBegin("create package manager");
        final com.android.server.pm.PackageManagerTracedLock lock = new com.android.server.pm.PackageManagerTracedLock("mLock");
        final com.android.server.pm.PackageManagerTracedLock installLock = new com.android.server.pm.PackageManagerTracedLock("mInstallLock");
        android.os.HandlerThread backgroundThread = new com.android.server.ServiceThread("PackageManagerBg", 10, true);
        backgroundThread.start();
        final android.os.Handler backgroundHandler = new android.os.Handler(backgroundThread.getLooper(), BACKGROUND_HANDLER_CALLBACK);
        com.android.server.pm.PackageAbiHelperImpl packageAbiHelperImpl = new com.android.server.pm.PackageAbiHelperImpl();
        java.util.List<com.android.server.pm.ScanPartition> list = SYSTEM_PARTITIONS;
        com.android.server.pm.PackageManagerServiceInjector.Producer producer = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda28
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$10(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer2 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda39
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.permission.PermissionManagerService.create(context, packageManagerServiceInjector.getSystemConfig().getAvailableFeatures());
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer3 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda50
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$12(context, installer, installLock, lock, packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer4 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda53
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$13(domainVerificationService, backgroundHandler, lock, packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer5 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda54
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.AppsFilterImpl.create(packageManagerServiceInjector, (android.content.pm.PackageManagerInternal) packageManagerServiceInjector.getLocalService(android.content.pm.PackageManagerInternal.class));
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer6 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda55
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$15(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer7 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda56
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.SystemConfig.getInstance();
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer8 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda57
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$17(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer9 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda58
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$18(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer10 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda59
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$19(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer11 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda29
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$20(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer12 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda30
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.ApexManager.getInstance();
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer13 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda31
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$22(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer14 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda32
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$25(context, packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer15 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda33
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$26(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer16 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda34
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$27(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer17 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda35
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$28(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer18 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda36
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$29(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer19 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda37
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$30(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.ProducerWithArgument producerWithArgument = new com.android.server.pm.PackageManagerServiceInjector.ProducerWithArgument() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda38
            @Override // com.android.server.pm.PackageManagerServiceInjector.ProducerWithArgument
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService, java.lang.Object obj) {
                return com.android.server.pm.PackageManagerService.lambda$main$31(packageManagerServiceInjector, packageManagerService, (android.content.ComponentName) obj);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer20 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda40
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$32(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer21 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda41
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.permission.LegacyPermissionManagerService.create(packageManagerServiceInjector.getContext());
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer22 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda42
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$34(domainVerificationService, packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerServiceInjector.Producer producer23 = new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda43
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$35(packageManagerServiceInjector, packageManagerService);
            }
        };
        com.android.server.pm.PackageManagerService.DefaultSystemWrapper defaultSystemWrapper = new com.android.server.pm.PackageManagerService.DefaultSystemWrapper();
        com.android.server.pm.PackageManagerServiceInjector.ServiceProducer serviceProducer = new com.android.server.pm.PackageManagerServiceInjector.ServiceProducer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda44
            @Override // com.android.server.pm.PackageManagerServiceInjector.ServiceProducer
            public final java.lang.Object produce(java.lang.Class cls) {
                return com.android.server.LocalServices.getService(cls);
            }
        };
        java.util.Objects.requireNonNull(context);
        com.android.server.pm.PackageManagerServiceInjector injector = new com.android.server.pm.PackageManagerServiceInjector(context, lock, installer, installLock, packageAbiHelperImpl, backgroundHandler, list, producer, producer2, producer3, producer4, producer5, producer6, producer7, producer8, producer9, producer10, producer11, producer12, producer13, producer14, producer15, producer16, producer17, producer18, producer19, producerWithArgument, producer20, producer21, producer22, producer23, defaultSystemWrapper, serviceProducer, new com.android.server.pm.PackageManagerServiceInjector.ServiceProducer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda45
            @Override // com.android.server.pm.PackageManagerServiceInjector.ServiceProducer
            public final java.lang.Object produce(java.lang.Class cls) {
                return context.getSystemService(cls);
            }
        }, new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda46
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return android.app.backup.IBackupManager.Stub.asInterface(android.os.ServiceManager.getService(com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP));
            }
        }, new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda47
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$37(packageManagerServiceInjector, packageManagerService);
            }
        }, new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda48
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$38(context, packageManagerServiceInjector, packageManagerService);
            }
        }, new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda49
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$39(packageManagerServiceInjector, packageManagerService);
            }
        }, new com.android.server.pm.PackageManagerServiceInjector.Producer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda51
            @Override // com.android.server.pm.PackageManagerServiceInjector.Producer
            public final java.lang.Object produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService) {
                return com.android.server.pm.PackageManagerService.lambda$main$40(packageManagerServiceInjector, packageManagerService);
            }
        });
        final com.android.server.pm.PackageManagerService m = new com.android.server.pm.PackageManagerService(injector, factoryTest, android.content.pm.PackagePartitions.FINGERPRINT, android.os.Build.IS_ENG, android.os.Build.IS_USERDEBUG, android.os.Build.VERSION.SDK_INT, android.os.Build.VERSION.INCREMENTAL);
        t.traceEnd();
        com.android.server.compat.CompatChange.ChangeListener selinuxChangeListener = new com.android.server.compat.CompatChange.ChangeListener() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda52
            @Override // com.android.server.compat.CompatChange.ChangeListener
            public final void onCompatChange(java.lang.String str) {
                com.android.server.pm.PackageManagerService.lambda$main$42(this.f$0, str);
            }
        };
        injector.getCompatibility().registerListener(143539591L, selinuxChangeListener);
        injector.getCompatibility().registerListener(168782947L, selinuxChangeListener);
        m.installAllowlistedSystemPackages();
        java.util.Objects.requireNonNull(m);
        android.content.pm.IPackageManager.Stub iPackageManagerImpl = m.new IPackageManagerImpl();
        m.mPackageManagerServiceExt.initOplusBinderExtensionInConstructor(iPackageManagerImpl);
        android.os.ServiceManager.addService("package", iPackageManagerImpl);
        m.mPackageManagerServiceExt.initInMain();
        android.os.ServiceManager.addService("package_native", new com.android.server.pm.PackageManagerNative(m));
        return m;
    }

    static /* synthetic */ com.android.server.pm.resolution.ComponentResolver lambda$main$10(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.resolution.ComponentResolver(i.getUserManagerService(), pm.mUserNeedsBadging);
    }

    static /* synthetic */ com.android.server.pm.UserManagerService lambda$main$12(android.content.Context context, com.android.server.pm.Installer installer, com.android.server.pm.PackageManagerTracedLock installLock, com.android.server.pm.PackageManagerTracedLock lock, com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.UserManagerService(context, pm, new com.android.server.pm.UserDataPreparer(installer, installLock, context), lock);
    }

    static /* synthetic */ com.android.server.pm.Settings lambda$main$13(com.android.server.pm.verify.domain.DomainVerificationService domainVerificationService, android.os.Handler backgroundHandler, com.android.server.pm.PackageManagerTracedLock lock, com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.Settings(android.os.Environment.getDataDirectory(), com.android.permission.persistence.RuntimePermissionsPersistence.createInstance(), i.getPermissionManagerServiceInternal(), domainVerificationService, backgroundHandler, lock);
    }

    static /* synthetic */ com.android.server.compat.PlatformCompat lambda$main$15(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return (com.android.server.compat.PlatformCompat) android.os.ServiceManager.getService("platform_compat");
    }

    static /* synthetic */ com.android.server.pm.PackageDexOptimizer lambda$main$17(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.PackageDexOptimizer(i.getInstaller(), i.getInstallLock(), i.getContext(), "*dexopt*");
    }

    static /* synthetic */ com.android.server.pm.dex.DexManager lambda$main$18(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.dex.DexManager(i.getContext(), i.getPackageDexOptimizer(), i.getDynamicCodeLogger());
    }

    static /* synthetic */ com.android.server.pm.dex.DynamicCodeLogger lambda$main$19(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.dex.DynamicCodeLogger(i.getInstaller());
    }

    static /* synthetic */ com.android.server.pm.dex.ArtManagerService lambda$main$20(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.dex.ArtManagerService(i.getContext(), i.getInstaller(), i.getInstallLock());
    }

    static /* synthetic */ android.os.incremental.IncrementalManager lambda$main$22(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return (android.os.incremental.IncrementalManager) i.getContext().getSystemService("incremental");
    }

    static /* synthetic */ android.app.role.RoleManager lambda$main$23(android.content.Context context) {
        return (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
    }

    static /* synthetic */ com.android.server.pm.DefaultAppProvider lambda$main$25(final android.content.Context context, com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.DefaultAppProvider(new java.util.function.Supplier() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda61
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.pm.PackageManagerService.lambda$main$23(context);
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda62
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.pm.PackageManagerService.lambda$main$24();
            }
        });
    }

    static /* synthetic */ com.android.server.pm.UserManagerInternal lambda$main$24() {
        return (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
    }

    static /* synthetic */ android.util.DisplayMetrics lambda$main$26(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new android.util.DisplayMetrics();
    }

    static /* synthetic */ com.android.internal.pm.parsing.PackageParser2 lambda$main$27(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.internal.pm.parsing.PackageParser2(pm.mSeparateProcesses, i.getDisplayMetrics(), new com.android.server.pm.parsing.PackageCacher(pm.mCacheDir, pm.mPackageParserCallback), pm.mPackageParserCallback);
    }

    static /* synthetic */ com.android.internal.pm.parsing.PackageParser2 lambda$main$28(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.internal.pm.parsing.PackageParser2(pm.mSeparateProcesses, i.getDisplayMetrics(), (com.android.internal.pm.parsing.IPackageCacher) null, pm.mPackageParserCallback);
    }

    static /* synthetic */ com.android.internal.pm.parsing.PackageParser2 lambda$main$29(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.internal.pm.parsing.PackageParser2(pm.mSeparateProcesses, i.getDisplayMetrics(), (com.android.internal.pm.parsing.IPackageCacher) null, pm.mPackageParserCallback);
    }

    static /* synthetic */ com.android.server.pm.PackageInstallerService lambda$main$30(final com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        android.content.Context context = i.getContext();
        java.util.Objects.requireNonNull(i);
        return new com.android.server.pm.PackageInstallerService(context, pm, new java.util.function.Supplier() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda16
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return i.getScanningPackageParser();
            }
        });
    }

    static /* synthetic */ com.android.server.pm.InstantAppResolverConnection lambda$main$31(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm, android.content.ComponentName cn) {
        return new com.android.server.pm.InstantAppResolverConnection(i.getContext(), cn, "android.intent.action.RESOLVE_INSTANT_APP_PACKAGE");
    }

    static /* synthetic */ com.android.server.pm.ModuleInfoProvider lambda$main$32(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.ModuleInfoProvider(i.getContext());
    }

    static /* synthetic */ com.android.server.pm.verify.domain.DomainVerificationManagerInternal lambda$main$34(com.android.server.pm.verify.domain.DomainVerificationService domainVerificationService, com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return domainVerificationService;
    }

    static /* synthetic */ android.os.Handler lambda$main$35(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        android.os.HandlerThread thread = new com.android.server.ServiceThread(TAG, 0, true);
        thread.start();
        return new com.android.server.pm.PackageHandler(thread.getLooper(), pm);
    }

    static /* synthetic */ com.android.server.pm.SharedLibrariesImpl lambda$main$37(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.SharedLibrariesImpl(pm, i);
    }

    static /* synthetic */ com.android.server.pm.CrossProfileIntentFilterHelper lambda$main$38(android.content.Context context, com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.CrossProfileIntentFilterHelper(i.getSettings(), i.getUserManagerService(), i.getLock(), i.getUserManagerInternal(), context);
    }

    static /* synthetic */ com.android.server.pm.UpdateOwnershipHelper lambda$main$39(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.UpdateOwnershipHelper();
    }

    static /* synthetic */ com.android.server.pm.PackageMonitorCallbackHelper lambda$main$40(com.android.server.pm.PackageManagerServiceInjector i, com.android.server.pm.PackageManagerService pm) {
        return new com.android.server.pm.PackageMonitorCallbackHelper();
    }

    static /* synthetic */ void lambda$main$42(com.android.server.pm.PackageManagerService m, java.lang.String packageName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = m.mInstallLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.Computer snapshot = m.snapshotComputer();
                com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
                if (packageState == null) {
                    android.util.Slog.e(TAG, "Failed to find package setting " + packageName);
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                com.android.server.pm.pkg.AndroidPackage pkg = packageState.getPkg();
                com.android.server.pm.pkg.SharedUserApi sharedUser = snapshot.getSharedUser(packageState.getSharedUserAppId());
                java.lang.String oldSeInfo = packageState.getSeInfo();
                if (pkg == null) {
                    android.util.Slog.e(TAG, "Failed to find package " + packageName);
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                final java.lang.String newSeInfo = com.android.server.pm.SELinuxMMAC.getSeInfo(packageState, pkg, sharedUser, m.mInjector.getCompatibility());
                if (!newSeInfo.equals(oldSeInfo)) {
                    android.util.Slog.i(TAG, "Updating seInfo for package " + packageName + " from: " + oldSeInfo + " to: " + newSeInfo);
                    m.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda17
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setOverrideSeInfo(newSeInfo);
                        }
                    });
                    m.mAppDataHelper.prepareAppDataAfterInstallLIF(pkg);
                }
                resetPriorityAfterPackageManagerTracedLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private void installAllowlistedSystemPackages() {
        if (this.mUserManager.installWhitelistedSystemPackages(isFirstBoot(), isDeviceUpgrading(), this.mExistingPackages)) {
            scheduleWritePackageRestrictions(-1);
            scheduleWriteSettings();
        }
    }

    private void registerObservers(boolean verify) {
        if (this.mPackages != null) {
            this.mPackages.registerObserver(this.mWatcher);
        }
        if (this.mSharedLibraries != null) {
            this.mSharedLibraries.registerObserver(this.mWatcher);
        }
        if (this.mInstrumentation != null) {
            this.mInstrumentation.registerObserver(this.mWatcher);
        }
        if (this.mWebInstantAppsDisabled != null) {
            this.mWebInstantAppsDisabled.registerObserver(this.mWatcher);
        }
        if (this.mAppsFilter != null) {
            this.mAppsFilter.registerObserver(this.mWatcher);
        }
        if (this.mInstantAppRegistry != null) {
            this.mInstantAppRegistry.registerObserver(this.mWatcher);
        }
        if (this.mSettings != null) {
            this.mSettings.registerObserver(this.mWatcher);
        }
        if (this.mIsolatedOwners != null) {
            this.mIsolatedOwners.registerObserver(this.mWatcher);
        }
        if (this.mComponentResolver != null) {
            this.mComponentResolver.registerObserver(this.mWatcher);
        }
        if (this.mFrozenPackages != null) {
            this.mFrozenPackages.registerObserver(this.mWatcher);
        }
        if (verify) {
            com.android.server.utils.Watchable.verifyWatchedAttributes(this, this.mWatcher, (this.mIsEngBuild || this.mIsUserDebugBuild) ? false : true);
        }
    }

    public PackageManagerService(com.android.server.pm.PackageManagerServiceInjector injector, com.android.server.pm.PackageManagerServiceTestParams testParams) {
        this.mOverlayPathsLock = new com.android.server.pm.PackageManagerTracedLock();
        this.mPackageStateMutator = new com.android.server.pm.pkg.mutate.PackageStateMutator(new java.util.function.Function() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.getPackageSettingForMutation((java.lang.String) obj);
            }
        }, new java.util.function.Function() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.getDisabledPackageSettingForMutation((java.lang.String) obj);
            }
        });
        this.mPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPackages, this.mPackages, "PackageManagerService.mPackages");
        this.mIsolatedOwners = new com.android.server.utils.WatchedSparseIntArray();
        this.mIsolatedOwnersSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mIsolatedOwners, this.mIsolatedOwners, "PackageManagerService.mIsolatedOwners");
        this.mExistingPackages = null;
        this.mFrozenPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mFrozenPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mFrozenPackages, this.mFrozenPackages, "PackageManagerService.mFrozenPackages");
        this.mPackageObserverHelper = new com.android.server.pm.PackageObserverHelper();
        this.mInstrumentation = new com.android.server.utils.WatchedArrayMap<>();
        this.mInstrumentationSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mInstrumentation, this.mInstrumentation, "PackageManagerService.mInstrumentation");
        this.mTransferredPackages = new android.util.ArraySet<>();
        this.mProtectedBroadcasts = new android.util.ArraySet<>();
        this.mPendingVerification = new android.util.SparseArray<>();
        this.mPendingEnableRollback = new android.util.SparseArray<>();
        this.mNextMoveId = new java.util.concurrent.atomic.AtomicInteger();
        this.mPendingVerificationToken = 0;
        this.mPendingEnableRollbackToken = 0;
        this.mWebInstantAppsDisabled = new com.android.server.utils.WatchedSparseBooleanArray();
        this.mResolveActivity = new android.content.pm.ActivityInfo();
        this.mResolveInfo = new android.content.pm.ResolveInfo();
        this.mPlatformPackageOverlayPaths = null;
        this.mPlatformPackageOverlayResourceDirs = null;
        this.mReplacedResolverPackageOverlayPaths = null;
        this.mReplacedResolverPackageOverlayResourceDirs = null;
        this.mResolverReplaced = false;
        this.mInstantAppInstallerInfo = new android.content.pm.ResolveInfo();
        this.mNoKillInstallObservers = java.util.Collections.synchronizedMap(new java.util.HashMap());
        this.mPendingKillInstallObservers = java.util.Collections.synchronizedMap(new java.util.HashMap());
        this.mKeepUninstalledPackages = new android.util.ArraySet<>();
        this.mDevicePolicyManager = null;
        this.mPackageProperty = new com.android.server.pm.PackageProperty();
        this.mDirtyUsers = new android.util.ArraySet<>();
        this.mRunningInstalls = new android.util.SparseArray<>();
        this.mNextInstallToken = 1;
        this.mPackageUsage = new com.android.server.pm.PackageUsage();
        this.mCompilerStats = new com.android.server.pm.CompilerStats();
        this.mWatcher = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.PackageManagerService.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.PackageManagerService.onChange(what);
            }
        };
        this.mSnapshotLock = new java.lang.Object();
        this.mWrapper = new com.android.server.pm.PackageManagerService.PackageManagerServiceWrapper();
        this.mPackageManagerServiceExt = (com.android.server.pm.IPackageManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceExt.class).base(this).create();
        this.mPackageManagerServiceSocExt = (com.android.server.pm.IPackageManagerServiceSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceSocExt.class).base(this).create();
        this.mInjector = injector;
        this.mInjector.bootstrap(this);
        this.mAppsFilter = injector.getAppsFilter();
        this.mComponentResolver = injector.getComponentResolver();
        this.mContext = injector.getContext();
        this.mInstaller = injector.getInstaller();
        this.mInstallLock = injector.getInstallLock();
        this.mLock = injector.getLock();
        this.mPackageStateWriteLock = this.mLock;
        this.mPermissionManager = injector.getPermissionManagerServiceInternal();
        this.mSettings = injector.getSettings();
        this.mUserManager = injector.getUserManagerService();
        this.mUserNeedsBadging = new com.android.server.pm.UserNeedsBadgingCache(this.mUserManager);
        this.mDomainVerificationManager = injector.getDomainVerificationManagerInternal();
        this.mHandler = injector.getHandler();
        this.mBackgroundHandler = injector.getBackgroundHandler();
        this.mSharedLibraries = injector.getSharedLibrariesImpl();
        this.mApexManager = testParams.apexManager;
        this.mArtManagerService = testParams.artManagerService;
        this.mAvailableFeatures = testParams.availableFeatures;
        this.mDefParseFlags = testParams.defParseFlags;
        this.mDefaultAppProvider = testParams.defaultAppProvider;
        this.mLegacyPermissionManager = testParams.legacyPermissionManagerInternal;
        this.mDexManager = testParams.dexManager;
        this.mDynamicCodeLogger = testParams.dynamicCodeLogger;
        this.mFactoryTest = testParams.factoryTest;
        this.mIncrementalManager = testParams.incrementalManager;
        this.mInstallerService = testParams.installerService;
        this.mInstantAppRegistry = testParams.instantAppRegistry;
        this.mChangedPackagesTracker = testParams.changedPackagesTracker;
        this.mInstantAppResolverConnection = testParams.instantAppResolverConnection;
        this.mInstantAppResolverSettingsComponent = testParams.instantAppResolverSettingsComponent;
        this.mIsPreNMR1Upgrade = testParams.isPreNmr1Upgrade;
        this.mIsPreQUpgrade = testParams.isPreQupgrade;
        this.mPriorSdkVersion = testParams.priorSdkVersion;
        this.mIsUpgrade = testParams.isUpgrade;
        this.mMetrics = testParams.Metrics;
        this.mModuleInfoProvider = testParams.moduleInfoProvider;
        this.mMoveCallbacks = testParams.moveCallbacks;
        this.mOverlayConfig = testParams.overlayConfig;
        this.mPackageDexOptimizer = testParams.packageDexOptimizer;
        this.mPackageParserCallback = testParams.packageParserCallback;
        this.mPendingBroadcasts = testParams.pendingPackageBroadcasts;
        this.mTestUtilityService = testParams.testUtilityService;
        this.mProcessLoggingHandler = testParams.processLoggingHandler;
        this.mProtectedPackages = testParams.protectedPackages;
        this.mSeparateProcesses = testParams.separateProcesses;
        this.mRequiredVerifierPackages = testParams.requiredVerifierPackages;
        this.mRequiredInstallerPackage = testParams.requiredInstallerPackage;
        this.mRequiredUninstallerPackage = testParams.requiredUninstallerPackage;
        this.mRequiredPermissionControllerPackage = testParams.requiredPermissionControllerPackage;
        this.mSetupWizardPackage = testParams.setupWizardPackage;
        this.mStorageManagerPackage = testParams.storageManagerPackage;
        this.mDefaultTextClassifierPackage = testParams.defaultTextClassifierPackage;
        this.mSystemTextClassifierPackageName = testParams.systemTextClassifierPackage;
        this.mRetailDemoPackage = testParams.retailDemoPackage;
        this.mRecentsPackage = testParams.recentsPackage;
        this.mAmbientContextDetectionPackage = testParams.ambientContextDetectionPackage;
        this.mWearableSensingPackage = testParams.wearableSensingPackage;
        this.mConfiguratorPackage = testParams.configuratorPackage;
        this.mAppPredictionServicePackage = testParams.appPredictionServicePackage;
        this.mIncidentReportApproverPackage = testParams.incidentReportApproverPackage;
        this.mServicesExtensionPackageName = testParams.servicesExtensionPackageName;
        this.mSharedSystemSharedLibraryPackageName = testParams.sharedSystemSharedLibraryPackageName;
        this.mOverlayConfigSignaturePackage = testParams.overlayConfigSignaturePackage;
        this.mResolveComponentName = testParams.resolveComponentName;
        this.mRequiredSdkSandboxPackage = testParams.requiredSdkSandboxPackage;
        this.mInitialNonStoppedSystemPackages = testParams.initialNonStoppedSystemPackages;
        this.mShouldStopSystemPackagesByDefault = testParams.shouldStopSystemPackagesByDefault;
        this.mLiveComputer = createLiveComputer();
        this.mSnapshotStatistics = null;
        this.mPackages.putAll(testParams.packages);
        this.mFreeStorageHelper = testParams.freeStorageHelper;
        this.mSdkVersion = testParams.sdkVersion;
        this.mAppInstallDir = testParams.appInstallDir;
        this.mIsEngBuild = testParams.isEngBuild;
        this.mIsUserDebugBuild = testParams.isUserDebugBuild;
        this.mIncrementalVersion = testParams.incrementalVersion;
        this.mDomainVerificationConnection = new com.android.server.pm.DomainVerificationConnection(this);
        this.mBroadcastHelper = testParams.broadcastHelper;
        this.mAppDataHelper = testParams.appDataHelper;
        this.mInstallPackageHelper = testParams.installPackageHelper;
        this.mRemovePackageHelper = testParams.removePackageHelper;
        this.mInitAppsHelper = testParams.initAndSystemPackageHelper;
        this.mDeletePackageHelper = testParams.deletePackageHelper;
        this.mPreferredActivityHelper = testParams.preferredActivityHelper;
        this.mResolveIntentHelper = testParams.resolveIntentHelper;
        this.mDexOptHelper = testParams.dexOptHelper;
        this.mSuspendPackageHelper = testParams.suspendPackageHelper;
        this.mDistractingPackageHelper = testParams.distractingPackageHelper;
        this.mSharedLibraries.setDeletePackageHelper(this.mDeletePackageHelper);
        this.mStorageEventHelper = testParams.storageEventHelper;
        this.mPackageMonitorCallbackHelper = testParams.packageMonitorCallbackHelper;
        registerObservers(false);
        invalidatePackageInfoCache();
    }

    public PackageManagerService(com.android.server.pm.PackageManagerServiceInjector injector, boolean factoryTest, java.lang.String partitionsFingerprint, boolean isEngBuild, boolean isUserDebugBuild, int sdkVersion, java.lang.String incrementalVersion) throws java.lang.Throwable {
        boolean z;
        java.lang.String str;
        java.util.Map<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> userPackages;
        com.android.server.pm.Computer computer;
        java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.String>> it;
        com.android.server.SystemConfig.SharedLibraryEntry entry;
        this.mOverlayPathsLock = new com.android.server.pm.PackageManagerTracedLock();
        this.mPackageStateMutator = new com.android.server.pm.pkg.mutate.PackageStateMutator(new java.util.function.Function() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.getPackageSettingForMutation((java.lang.String) obj);
            }
        }, new java.util.function.Function() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.getDisabledPackageSettingForMutation((java.lang.String) obj);
            }
        });
        this.mPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPackages, this.mPackages, "PackageManagerService.mPackages");
        this.mIsolatedOwners = new com.android.server.utils.WatchedSparseIntArray();
        this.mIsolatedOwnersSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mIsolatedOwners, this.mIsolatedOwners, "PackageManagerService.mIsolatedOwners");
        this.mExistingPackages = null;
        this.mFrozenPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mFrozenPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mFrozenPackages, this.mFrozenPackages, "PackageManagerService.mFrozenPackages");
        this.mPackageObserverHelper = new com.android.server.pm.PackageObserverHelper();
        this.mInstrumentation = new com.android.server.utils.WatchedArrayMap<>();
        this.mInstrumentationSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mInstrumentation, this.mInstrumentation, "PackageManagerService.mInstrumentation");
        this.mTransferredPackages = new android.util.ArraySet<>();
        this.mProtectedBroadcasts = new android.util.ArraySet<>();
        this.mPendingVerification = new android.util.SparseArray<>();
        this.mPendingEnableRollback = new android.util.SparseArray<>();
        this.mNextMoveId = new java.util.concurrent.atomic.AtomicInteger();
        this.mPendingVerificationToken = 0;
        this.mPendingEnableRollbackToken = 0;
        this.mWebInstantAppsDisabled = new com.android.server.utils.WatchedSparseBooleanArray();
        this.mResolveActivity = new android.content.pm.ActivityInfo();
        this.mResolveInfo = new android.content.pm.ResolveInfo();
        this.mPlatformPackageOverlayPaths = null;
        this.mPlatformPackageOverlayResourceDirs = null;
        this.mReplacedResolverPackageOverlayPaths = null;
        this.mReplacedResolverPackageOverlayResourceDirs = null;
        this.mResolverReplaced = false;
        this.mInstantAppInstallerInfo = new android.content.pm.ResolveInfo();
        this.mNoKillInstallObservers = java.util.Collections.synchronizedMap(new java.util.HashMap());
        this.mPendingKillInstallObservers = java.util.Collections.synchronizedMap(new java.util.HashMap());
        this.mKeepUninstalledPackages = new android.util.ArraySet<>();
        this.mDevicePolicyManager = null;
        this.mPackageProperty = new com.android.server.pm.PackageProperty();
        this.mDirtyUsers = new android.util.ArraySet<>();
        this.mRunningInstalls = new android.util.SparseArray<>();
        this.mNextInstallToken = 1;
        this.mPackageUsage = new com.android.server.pm.PackageUsage();
        this.mCompilerStats = new com.android.server.pm.CompilerStats();
        this.mWatcher = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.PackageManagerService.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.PackageManagerService.onChange(what);
            }
        };
        this.mSnapshotLock = new java.lang.Object();
        this.mWrapper = new com.android.server.pm.PackageManagerService.PackageManagerServiceWrapper();
        this.mPackageManagerServiceExt = (com.android.server.pm.IPackageManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceExt.class).base(this).create();
        this.mPackageManagerServiceSocExt = (com.android.server.pm.IPackageManagerServiceSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceSocExt.class).base(this).create();
        this.mIsEngBuild = isEngBuild;
        this.mIsUserDebugBuild = isUserDebugBuild;
        this.mSdkVersion = sdkVersion;
        this.mIncrementalVersion = incrementalVersion;
        this.mInjector = injector;
        this.mInjector.getSystemWrapper().disablePackageCaches();
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog("PackageManagerTiming", 262144L);
        this.mPendingBroadcasts = new com.android.server.pm.PendingPackageBroadcasts();
        this.mInjector.bootstrap(this);
        this.mLock = injector.getLock();
        this.mPackageStateWriteLock = this.mLock;
        this.mInstallLock = injector.getInstallLock();
        com.android.server.LockGuard.installLock(this.mLock, 3);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BOOT_PROGRESS_PMS_START, android.os.SystemClock.uptimeMillis());
        this.mPackageManagerServiceExt.afterPmsStartEventInConstructor();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:PackageManagerService_Start");
        this.mContext = injector.getContext();
        this.mFactoryTest = factoryTest;
        this.mMetrics = injector.getDisplayMetrics();
        this.mInstaller = injector.getInstaller();
        this.mFreeStorageHelper = new com.android.server.pm.FreeStorageHelper(this);
        this.mPackageManagerServiceExt.beforeCreateSubComponentsInConstructor();
        t.traceBegin("createSubComponents");
        com.android.server.LocalServices.addService(android.content.pm.PackageManagerInternal.class, new com.android.server.pm.PackageManagerService.PackageManagerInternalImpl());
        com.android.server.LocalManagerRegistry.addManager(com.android.server.pm.PackageManagerLocal.class, new com.android.server.pm.local.PackageManagerLocalImpl(this));
        com.android.server.LocalServices.addService(android.content.pm.TestUtilityService.class, this);
        this.mTestUtilityService = (android.content.pm.TestUtilityService) com.android.server.LocalServices.getService(android.content.pm.TestUtilityService.class);
        this.mUserManager = injector.getUserManagerService();
        this.mUserNeedsBadging = new com.android.server.pm.UserNeedsBadgingCache(this.mUserManager);
        this.mComponentResolver = injector.getComponentResolver();
        this.mPermissionManager = injector.getPermissionManagerServiceInternal();
        this.mSettings = injector.getSettings();
        this.mIncrementalManager = this.mInjector.getIncrementalManager();
        this.mDefaultAppProvider = this.mInjector.getDefaultAppProvider();
        this.mLegacyPermissionManager = this.mInjector.getLegacyPermissionManagerInternal();
        final com.android.server.compat.PlatformCompat platformCompat = this.mInjector.getCompatibility();
        this.mPackageParserCallback = new com.android.internal.pm.parsing.PackageParser2.Callback() { // from class: com.android.server.pm.PackageManagerService.3
            public boolean isChangeEnabled(long changeId, android.content.pm.ApplicationInfo appInfo) {
                if (com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.ignoreChangeInPackageParserCallback(changeId, appInfo)) {
                    return false;
                }
                return platformCompat.isChangeEnabled(changeId, appInfo);
            }

            public boolean hasFeature(java.lang.String feature) {
                return com.android.server.pm.PackageManagerService.this.hasSystemFeature(feature, 0);
            }

            public java.util.Set<java.lang.String> getHiddenApiWhitelistedApps() {
                return com.android.server.SystemConfig.getInstance().getHiddenApiWhitelistedApps();
            }

            public java.util.Set<java.lang.String> getInstallConstraintsAllowlist() {
                return com.android.server.SystemConfig.getInstance().getInstallConstraintsAllowlist();
            }
        };
        t.traceEnd();
        this.mPackageManagerServiceExt.beforeAddSharedUsersInConstructor();
        t.traceBegin("addSharedUsers");
        this.mSettings.addSharedUserLPw("android.uid.system", 1000, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.phone", 1001, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.log", 1007, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.nfc", 1027, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.bluetooth", 1002, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.shell", 2000, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.se", 1068, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.networkstack", NETWORKSTACK_UID, 1, 8);
        this.mSettings.addSharedUserLPw("android.uid.uwb", 1083, 1, 8);
        this.mPackageManagerServiceSocExt.addVendorDataUid(this.mSettings);
        t.traceEnd();
        java.lang.String separateProcesses = android.os.SystemProperties.get("debug.separate_processes");
        if (separateProcesses != null && separateProcesses.length() > 0) {
            if (com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER.equals(separateProcesses)) {
                this.mDefParseFlags = 2;
                this.mSeparateProcesses = null;
                android.util.Slog.w(TAG, "Running with debug.separate_processes: * (ALL)");
            } else {
                this.mDefParseFlags = 0;
                this.mSeparateProcesses = separateProcesses.split(",");
                android.util.Slog.w(TAG, "Running with debug.separate_processes: " + separateProcesses);
            }
        } else {
            this.mDefParseFlags = 0;
            this.mSeparateProcesses = null;
        }
        this.mPackageDexOptimizer = injector.getPackageDexOptimizer();
        this.mDexManager = injector.getDexManager();
        this.mDynamicCodeLogger = injector.getDynamicCodeLogger();
        this.mArtManagerService = injector.getArtManagerService();
        this.mMoveCallbacks = new com.android.server.pm.MovePackageHelper.MoveCallbacks(com.android.server.FgThread.get().getLooper());
        this.mSharedLibraries = this.mInjector.getSharedLibrariesImpl();
        this.mBackgroundHandler = injector.getBackgroundHandler();
        ((android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class)).getDisplay(0).getMetrics(this.mMetrics);
        t.traceBegin("get system config");
        com.android.server.SystemConfig systemConfig = injector.getSystemConfig();
        this.mAvailableFeatures = systemConfig.getAvailableFeatures();
        t.traceEnd();
        this.mPackageManagerServiceExt.afterGetSystemConfigInConstructor();
        this.mProtectedPackages = new com.android.server.pm.ProtectedPackages(this.mContext);
        this.mApexManager = injector.getApexManager();
        this.mAppsFilter = this.mInjector.getAppsFilter();
        this.mChangedPackagesTracker = new com.android.server.pm.ChangedPackagesTracker();
        this.mAppInstallDir = new java.io.File(android.os.Environment.getDataDirectory(), "app");
        this.mDomainVerificationConnection = new com.android.server.pm.DomainVerificationConnection(this);
        this.mDomainVerificationManager = injector.getDomainVerificationManagerInternal();
        this.mDomainVerificationManager.setConnection(this.mDomainVerificationConnection);
        this.mBroadcastHelper = new com.android.server.pm.BroadcastHelper(this.mInjector);
        this.mPackageMonitorCallbackHelper = injector.getPackageMonitorCallbackHelper();
        this.mAppDataHelper = new com.android.server.pm.AppDataHelper(this);
        this.mRemovePackageHelper = new com.android.server.pm.RemovePackageHelper(this, this.mAppDataHelper, this.mBroadcastHelper);
        this.mDeletePackageHelper = new com.android.server.pm.DeletePackageHelper(this, this.mRemovePackageHelper, this.mBroadcastHelper);
        this.mInstallPackageHelper = new com.android.server.pm.InstallPackageHelper(this, this.mAppDataHelper, this.mRemovePackageHelper, this.mDeletePackageHelper, this.mBroadcastHelper);
        this.mInstantAppRegistry = new com.android.server.pm.InstantAppRegistry(this.mContext, this.mPermissionManager, this.mInjector.getUserManagerInternal(), this.mDeletePackageHelper);
        this.mSharedLibraries.setDeletePackageHelper(this.mDeletePackageHelper);
        this.mPreferredActivityHelper = new com.android.server.pm.PreferredActivityHelper(this, this.mBroadcastHelper);
        this.mResolveIntentHelper = new com.android.server.pm.ResolveIntentHelper(this.mContext, this.mPreferredActivityHelper, injector.getCompatibility(), this.mUserManager, this.mDomainVerificationManager, this.mUserNeedsBadging, new java.util.function.Supplier() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda7
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$new$43();
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda8
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$new$44();
            }
        });
        this.mDexOptHelper = new com.android.server.pm.DexOptHelper(this);
        this.mSuspendPackageHelper = new com.android.server.pm.SuspendPackageHelper(this, this.mInjector, this.mBroadcastHelper, this.mProtectedPackages);
        this.mDistractingPackageHelper = new com.android.server.pm.DistractingPackageHelper(this, this.mBroadcastHelper, this.mSuspendPackageHelper);
        this.mStorageEventHelper = new com.android.server.pm.StorageEventHelper(this, this.mDeletePackageHelper, this.mRemovePackageHelper);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mSnapshotStatistics = new com.android.server.pm.SnapshotStatistics();
                sSnapshotPendingVersion.incrementAndGet();
                this.mLiveComputer = createLiveComputer();
                registerObservers(true);
            } finally {
                resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
        com.android.server.pm.Computer computer2 = this.mLiveComputer;
        this.mPackageManagerServiceExt.onStartLockedWorkInConstructor();
        this.mHandler = injector.getHandler();
        this.mProcessLoggingHandler = new com.android.server.pm.ProcessLoggingHandler();
        com.android.server.Watchdog.getInstance().addThread(this.mHandler, 600000L);
        android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.SharedLibraryEntry> libConfig = systemConfig.getSharedLibraries();
        int builtInLibCount = libConfig.size();
        for (int i = 0; i < builtInLibCount; i++) {
            this.mSharedLibraries.addBuiltInSharedLibraryLPw(libConfig.valueAt(i));
        }
        int i2 = 0;
        while (i2 < builtInLibCount) {
            java.lang.String name = libConfig.keyAt(i2);
            com.android.server.SystemConfig.SharedLibraryEntry entry2 = libConfig.valueAt(i2);
            int dependencyCount = entry2.dependencies.length;
            android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.SharedLibraryEntry> libConfig2 = libConfig;
            int j = 0;
            while (j < dependencyCount) {
                int builtInLibCount2 = builtInLibCount;
                android.content.pm.SharedLibraryInfo dependency = computer2.getSharedLibraryInfo(entry2.dependencies[j], -1L);
                if (dependency == null) {
                    entry = entry2;
                } else {
                    entry = entry2;
                    computer2.getSharedLibraryInfo(name, -1L).addDependency(dependency);
                }
                j++;
                builtInLibCount = builtInLibCount2;
                entry2 = entry;
            }
            i2++;
            libConfig = libConfig2;
        }
        com.android.server.pm.SELinuxMMAC.readInstallPolicy();
        t.traceBegin("loadFallbacks");
        android.content.pm.FallbackCategoryProvider.loadFallbacks();
        t.traceEnd();
        t.traceBegin("read user settings");
        this.mFirstBoot = !this.mSettings.readLPw(computer2, this.mInjector.getUserManagerInternal().getUsers(true, false, false));
        t.traceEnd();
        if (this.mFirstBoot) {
            t.traceBegin("setFirstBoot: ");
            try {
                this.mInstaller.setFirstBoot();
            } catch (com.android.server.pm.Installer.InstallerException e) {
                android.util.Slog.w(TAG, "Could not set First Boot: ", e);
            }
            t.traceEnd();
        }
        this.mPackageManagerServiceExt.afterReadUserSettingsInConstructor();
        this.mPermissionManager.readLegacyPermissionsTEMP(this.mSettings.mPermissions);
        this.mPermissionManager.readLegacyPermissionStateTEMP();
        if (this.mFirstBoot) {
            com.android.server.pm.DexOptHelper.requestCopyPreoptedFiles();
        }
        java.lang.String customResolverActivityName = android.content.res.Resources.getSystem().getString(android.R.string.config_defaultAssistantAccessComponent);
        if (!android.text.TextUtils.isEmpty(customResolverActivityName)) {
            this.mCustomResolverComponentName = android.content.ComponentName.unflattenFromString(customResolverActivityName);
        }
        long startTime = android.os.SystemClock.uptimeMillis();
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BOOT_PROGRESS_PMS_SYSTEM_SCAN_START, startTime);
        this.mPackageManagerServiceExt.afterPmsSystemScanStartEventInConstructor();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:PMS_scan_START");
        java.lang.String bootClassPath = java.lang.System.getenv("BOOTCLASSPATH");
        java.lang.String systemServerClassPath = java.lang.System.getenv("SYSTEMSERVERCLASSPATH");
        if (bootClassPath == null) {
            android.util.Slog.w(TAG, "No BOOTCLASSPATH found!");
        }
        if (systemServerClassPath == null) {
            android.util.Slog.w(TAG, "No SYSTEMSERVERCLASSPATH found!");
        }
        com.android.server.pm.Settings.VersionInfo ver = this.mSettings.getInternalVersion();
        this.mIsUpgrade = this.mPackageManagerServiceExt.adjustIsUpgradeFlag(!partitionsFingerprint.equals(ver.fingerprint));
        if (this.mIsUpgrade) {
            com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(4, "Upgrading from " + ver.fingerprint + " (" + ver.buildFingerprint + ") to " + android.content.pm.PackagePartitions.FINGERPRINT + " (" + android.os.Build.FINGERPRINT + ")");
        }
        this.mPriorSdkVersion = this.mIsUpgrade ? ver.sdkVersion : -1;
        this.mInitAppsHelper = new com.android.server.pm.InitAppsHelper(this, this.mApexManager, this.mInstallPackageHelper, this.mInjector.getSystemPartitions());
        this.mPackageManagerServiceExt.afterApexGetListAndWaitForOpexFinishInConstructor();
        this.mPromoteSystemApps = this.mIsUpgrade && ver.sdkVersion <= 22;
        this.mIsPreNMR1Upgrade = this.mIsUpgrade && ver.sdkVersion < 25;
        this.mIsPreQUpgrade = this.mIsUpgrade && ver.sdkVersion < 29;
        this.mPackageManagerServiceExt.afterCalculateUpgradeFlagInConstructor(ver);
        com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> packageSettings = this.mSettings.getPackagesLocked();
        if (isDeviceUpgrading()) {
            this.mExistingPackages = new android.util.ArraySet<>(packageSettings.size());
            for (int i3 = 0; i3 < packageSettings.size(); i3++) {
                this.mExistingPackages.add(packageSettings.valueAt(i3).getPackageName());
            }
            t.traceBegin("cross profile intent filter update");
            this.mInjector.getCrossProfileIntentFilterHelper().updateDefaultCrossProfileIntentFilter();
            t.traceEnd();
        }
        this.mCacheDir = com.android.server.pm.PackageManagerServiceUtils.preparePackageParserCache(this.mIsEngBuild, this.mIsUserDebugBuild, this.mIncrementalVersion);
        this.mInitialNonStoppedSystemPackages = this.mInjector.getSystemConfig().getInitialNonStoppedSystemPackages();
        this.mShouldStopSystemPackagesByDefault = this.mContext.getResources().getBoolean(android.R.bool.config_showBuiltinWirelessChargingAnim);
        final int[] userIds = this.mUserManager.getUserIds();
        com.android.internal.pm.parsing.PackageParser2 packageParser = this.mInjector.getScanningCachingPackageParser();
        this.mOverlayConfig = this.mInitAppsHelper.initSystemApps(packageParser, packageSettings, userIds, startTime);
        this.mInitAppsHelper.initNonSystemApps(packageParser, userIds, startTime);
        packageParser.close();
        this.mRequiredVerifierPackages = getRequiredButNotReallyRequiredVerifiersLPr(computer2);
        this.mRequiredInstallerPackage = getRequiredInstallerLPr(computer2);
        this.mRequiredUninstallerPackage = getRequiredUninstallerLPr(computer2);
        this.mRequiredPermissionControllerPackage = getRequiredPermissionControllerLPr(computer2);
        this.mStorageManagerPackage = getStorageManagerPackageName(computer2);
        this.mSetupWizardPackage = getSetupWizardPackageNameImpl(computer2);
        this.mComponentResolver.fixProtectedFilterPriorities(this.mSetupWizardPackage);
        this.mDefaultTextClassifierPackage = ensureSystemPackageName(computer2, this.mContext.getString(android.R.string.config_slicePermissionComponent));
        this.mSystemTextClassifierPackageName = ensureSystemPackageName(computer2, this.mContext.getString(android.R.string.config_deviceSpecificDevicePolicyManagerService));
        this.mConfiguratorPackage = ensureSystemPackageName(computer2, this.mContext.getString(android.R.string.config_doubleTouchGestureEnableFile));
        this.mAppPredictionServicePackage = ensureSystemPackageName(computer2, getPackageFromComponentString(android.R.string.config_defaultContextualSearchEnabled));
        this.mIncidentReportApproverPackage = ensureSystemPackageName(computer2, this.mContext.getString(android.R.string.config_mediaProjectionPermissionDialogComponent));
        this.mRetailDemoPackage = getRetailDemoPackageName();
        this.mOverlayConfigSignaturePackage = ensureSystemPackageName(computer2, this.mInjector.getSystemConfig().getOverlayConfigSignaturePackage());
        this.mRecentsPackage = ensureSystemPackageName(computer2, getPackageFromComponentString(android.R.string.config_satellite_nidd_apn_name));
        this.mAmbientContextDetectionPackage = ensureSystemPackageName(computer2, getPackageFromComponentString(android.R.string.config_defaultContentSuggestionsService));
        this.mWearableSensingPackage = ensureSystemPackageName(computer2, getPackageFromComponentString(android.R.string.config_displayWhiteBalanceColorTemperatureSensorName));
        this.mSharedLibraries.updateAllSharedLibrariesLPw(null, null, java.util.Collections.unmodifiableMap(this.mPackages));
        for (java.util.Iterator<com.android.server.pm.SharedUserSetting> it2 = this.mSettings.getAllSharedUsersLPw().iterator(); it2.hasNext(); it2 = it2) {
            com.android.server.pm.SharedUserSetting setting = it2.next();
            com.android.server.pm.ScanPackageUtils.applyAdjustedAbiToSharedUser(setting, null, this.mInjector.getAbiHelper().getAdjustedAbiForSharedUser(setting.getPackageStates(), null));
            setting.fixSeInfoLocked();
            setting.updateProcesses();
        }
        this.mPackageUsage.read(packageSettings);
        this.mCompilerStats.read();
        this.mPackageManagerServiceExt.readAbiInfoAfterScanEnd(packageSettings);
        this.mPackageManagerServiceExt.beforeRecordScanEndInConstructor();
        android.util.EventLog.writeEvent(3090, android.os.SystemClock.uptimeMillis());
        this.mPackageManagerServiceExt.afterPmsScanEndEventInConstructor();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:PMS_scan_END");
        android.util.Slog.i(TAG, "Time to scan packages: " + ((android.os.SystemClock.uptimeMillis() - startTime) / 1000.0f) + " seconds");
        if (this.mIsUpgrade) {
            android.util.Slog.i(TAG, "Partitions fingerprint changed from " + ver.fingerprint + " to " + android.content.pm.PackagePartitions.FINGERPRINT + "; regranting permissions for internal storage");
        }
        this.mPermissionManager.onStorageVolumeMounted(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, this.mIsUpgrade);
        ver.sdkVersion = this.mSdkVersion;
        if (this.mPromoteSystemApps || this.mFirstBoot) {
            java.util.List<android.content.pm.UserInfo> users = this.mInjector.getUserManagerInternal().getUsers(true);
            for (int i4 = 0; i4 < users.size(); i4++) {
                this.mSettings.applyDefaultPreferredAppsLPw(users.get(i4).id);
            }
        }
        if (this.mIsUpgrade) {
            android.util.Slog.i(TAG, "Build fingerprint changed; clearing code caches");
            for (int i5 = 0; i5 < packageSettings.size(); i5++) {
                com.android.server.pm.PackageSetting ps = packageSettings.valueAt(i5);
                if (java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, ps.getVolumeUuid())) {
                    this.mAppDataHelper.clearAppDataLIF(ps.getPkg(), -1, 131111);
                }
            }
            ver.buildFingerprint = android.os.Build.FINGERPRINT;
            ver.fingerprint = android.content.pm.PackagePartitions.FINGERPRINT;
        }
        this.mPrepareAppDataFuture = this.mAppDataHelper.fixAppsDataOnBoot();
        if (!this.mIsPreQUpgrade) {
            z = false;
        } else {
            android.util.Slog.i(TAG, "Allowlisting all existing apps to hide their icons");
            int size = packageSettings.size();
            for (int i6 = 0; i6 < size; i6++) {
                com.android.server.pm.PackageSetting ps2 = packageSettings.valueAt(i6);
                if ((ps2.getFlags() & 1) == 0) {
                    ps2.disableComponentLPw(android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME, 0);
                }
            }
            z = false;
        }
        this.mPromoteSystemApps = z;
        ver.databaseVersion = 3;
        this.mPackageManagerServiceExt.beforeWriteSettingsInConstructor();
        t.traceBegin("write settings");
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                writeSettingsLPrTEMP();
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
        resetPriorityAfterPackageManagerTracedLockedSection();
        t.traceEnd();
        android.util.EventLog.writeEvent(3100, android.os.SystemClock.uptimeMillis());
        this.mPackageManagerServiceExt.afterPmsReadyEventInConstructor();
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:PMS_READY");
        android.content.ComponentName intentFilterVerifierComponent = getIntentFilterVerifierComponentNameLPr(computer2);
        android.content.ComponentName domainVerificationAgent = getDomainVerificationAgentComponentNameLPr(computer2, 0);
        com.android.server.pm.verify.domain.proxy.DomainVerificationProxy domainVerificationProxy = com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.makeProxy(intentFilterVerifierComponent, domainVerificationAgent, this.mContext, this.mDomainVerificationManager, this.mDomainVerificationManager.getCollector(), this.mDomainVerificationConnection);
        this.mDomainVerificationManager.setProxy(domainVerificationProxy);
        this.mServicesExtensionPackageName = getRequiredServicesExtensionPackageLPr(computer2);
        this.mSharedSystemSharedLibraryPackageName = getRequiredSharedLibrary(computer2, "android.ext.shared", -1);
        int i7 = 0;
        this.mSettings.setPermissionControllerVersion(computer2.getPackageInfo(this.mRequiredPermissionControllerPackage, 0L, 0).getLongVersionCode());
        this.mRequiredSdkSandboxPackage = getRequiredSdkSandboxPackageName(computer2);
        forEachPackageState(computer2, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$45(userIds, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        });
        this.mInstallerService = this.mInjector.getPackageInstallerService();
        android.content.ComponentName instantAppResolverComponent = getInstantAppResolver(computer2);
        if (instantAppResolverComponent != null) {
            if (DEBUG_INSTANT) {
                android.util.Slog.d(TAG, "Set ephemeral resolver: " + instantAppResolverComponent);
            }
            this.mInstantAppResolverConnection = this.mInjector.getInstantAppResolverConnection(instantAppResolverComponent);
            this.mInstantAppResolverSettingsComponent = getInstantAppResolverSettingsLPr(computer2, instantAppResolverComponent);
            str = null;
        } else {
            str = null;
            this.mInstantAppResolverConnection = null;
            this.mInstantAppResolverSettingsComponent = null;
        }
        updateInstantAppInstallerLocked(str);
        java.util.Map<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> userPackages2 = new java.util.HashMap<>();
        int length = userIds.length;
        while (i7 < length) {
            int userId = userIds[i7];
            userPackages2.put(java.lang.Integer.valueOf(userId), computer2.getInstalledPackages(0L, userId).getList());
            i7++;
            instantAppResolverComponent = instantAppResolverComponent;
            userIds = userIds;
            domainVerificationProxy = domainVerificationProxy;
        }
        this.mPackageManagerServiceExt.onPrepareSaveIconPack(this.mContext, userPackages2);
        this.mDexManager.load(userPackages2);
        this.mDynamicCodeLogger.load(userPackages2);
        if (this.mIsUpgrade) {
            com.android.internal.util.FrameworkStatsLog.write(239, 13, android.os.SystemClock.uptimeMillis() - startTime);
        }
        if (this.mFirstBoot || isDeviceUpgrading()) {
            android.util.ArrayMap<java.lang.String, java.lang.String> paths = systemConfig.getAppMetadataFilePaths();
            for (java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.String>> it3 = paths.entrySet().iterator(); it3.hasNext(); it3 = it) {
                java.util.Map.Entry<java.lang.String, java.lang.String> entry3 = it3.next();
                java.lang.String pkgName = entry3.getKey();
                java.lang.String path = entry3.getValue();
                java.io.File file = new java.io.File(path);
                path = file.exists() ? path : null;
                android.util.ArrayMap<java.lang.String, java.lang.String> paths2 = paths;
                com.android.server.pm.PackageSetting disabledPkgSetting = this.mSettings.getDisabledSystemPkgLPr(pkgName);
                if (disabledPkgSetting == null) {
                    userPackages = userPackages2;
                    com.android.server.pm.PackageSetting pkgSetting = this.mSettings.getPackageLPr(pkgName);
                    if (pkgSetting != null) {
                        pkgSetting.setAppMetadataFilePath(path);
                        if (!com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.aslInApkAppMetadataSource()) {
                            computer = computer2;
                            it = it3;
                        } else {
                            computer = computer2;
                            pkgSetting.setAppMetadataSource(3);
                            it = it3;
                        }
                    } else {
                        computer = computer2;
                        it = it3;
                        android.util.Slog.w(TAG, "Cannot set app metadata file for nonexistent package " + pkgName);
                    }
                } else {
                    userPackages = userPackages2;
                    computer = computer2;
                    it = it3;
                    disabledPkgSetting.setAppMetadataFilePath(path);
                    if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.aslInApkAppMetadataSource()) {
                        disabledPkgSetting.setAppMetadataSource(3);
                    }
                }
                paths = paths2;
                userPackages2 = userPackages;
                computer2 = computer;
            }
        }
        this.mLiveComputer = createLiveComputer();
        this.mPackageManagerServiceExt.onEndLockedWorkInConstructor();
        this.mModuleInfoProvider = this.mInjector.getModuleInfoProvider();
        this.mInjector.getSystemWrapper().enablePackageCaches();
        this.mInstaller.setWarnIfHeld(this.mLock);
        com.android.internal.pm.pkg.parsing.ParsingPackageUtils.readConfigUseRoundIcon(this.mContext.getResources());
        this.mServiceStartWithDelay = android.os.SystemClock.uptimeMillis() + 60000;
        android.util.Slog.i(TAG, "Fix for b/169414761 is applied");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.ResolveInfo lambda$new$43() {
        return this.mResolveInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.ActivityInfo lambda$new$44() {
        return this.mInstantAppInstallerActivity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$45(int[] userIds, com.android.server.pm.pkg.PackageStateInternal packageState) {
        com.android.server.pm.pkg.AndroidPackage pkg = packageState.getAndroidPackage();
        if (pkg == null || packageState.isSystem()) {
            return;
        }
        for (int userId : userIds) {
            if (packageState.getUserStateOrDefault(userId).isInstantApp() && packageState.getUserStateOrDefault(userId).isInstalled()) {
                this.mInstantAppRegistry.addInstantApp(userId, packageState.getAppId());
            }
        }
    }

    void updateInstantAppInstallerLocked(java.lang.String modifiedPackage) {
        if (this.mInstantAppInstallerActivity != null && !this.mInstantAppInstallerActivity.getComponentName().getPackageName().equals(modifiedPackage)) {
            return;
        }
        setUpInstantAppInstallerActivityLP(getInstantAppInstallerLPr());
    }

    public boolean isFirstBoot() {
        return this.mFirstBoot;
    }

    public boolean isDeviceUpgrading() {
        return this.mIsUpgrade || android.os.SystemProperties.getBoolean("persist.pm.mock-upgrade", false);
    }

    private java.lang.String[] getRequiredButNotReallyRequiredVerifiersLPr(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.PACKAGE_NEEDS_VERIFICATION");
        java.util.List<android.content.pm.ResolveInfo> matches = this.mResolveIntentHelper.queryIntentReceiversInternal(computer, intent, PACKAGE_MIME_TYPE, 1835008L, 0, android.os.Binder.getCallingUid());
        int size = matches.size();
        if (size == 0) {
            android.util.Log.w(TAG, "There should probably be a verifier, but, none were found");
            return libcore.util.EmptyArray.STRING;
        }
        if (size <= 2) {
            java.lang.String[] verifiers = new java.lang.String[size];
            for (int i = 0; i < size; i++) {
                verifiers[i] = matches.get(i).getComponentInfo().packageName;
                if (android.text.TextUtils.isEmpty(verifiers[i])) {
                    throw new java.lang.RuntimeException("Invalid verifier: " + matches);
                }
            }
            return verifiers;
        }
        throw new java.lang.RuntimeException("There must be no more than 2 verifiers; found " + matches);
    }

    private java.lang.String getRequiredSharedLibrary(com.android.server.pm.Computer snapshot, java.lang.String name, int version) {
        android.content.pm.SharedLibraryInfo libraryInfo = snapshot.getSharedLibraryInfo(name, version);
        if (libraryInfo == null) {
            throw new java.lang.IllegalStateException("Missing required shared library:" + name);
        }
        java.lang.String packageName = libraryInfo.getPackageName();
        if (packageName == null) {
            throw new java.lang.IllegalStateException("Expected a package for shared library " + name);
        }
        return packageName;
    }

    private java.lang.String getRequiredServicesExtensionPackageLPr(com.android.server.pm.Computer computer) {
        java.lang.String configServicesExtensionPackage = this.mContext.getString(android.R.string.config_slicePermissionComponent);
        if (android.text.TextUtils.isEmpty(configServicesExtensionPackage)) {
            throw new java.lang.RuntimeException("Required services extension package failed due to config_servicesExtensionPackage is empty.");
        }
        java.lang.String servicesExtensionPackage = ensureSystemPackageName(computer, configServicesExtensionPackage);
        if (android.text.TextUtils.isEmpty(servicesExtensionPackage)) {
            this.mPackageManagerServiceExt.getRequiredServicesExtensionPackageError();
            throw new java.lang.RuntimeException("Required services extension package is missing, config_servicesExtensionPackage had defined with " + configServicesExtensionPackage + ", but can not find the package info on the system image, check if the package has a problem.");
        }
        return servicesExtensionPackage;
    }

    private java.lang.String getRequiredInstallerLPr(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.INSTALL_PACKAGE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(android.net.Uri.parse("content://com.example/foo.apk"), PACKAGE_MIME_TYPE);
        java.util.List<android.content.pm.ResolveInfo> matches = computer.queryIntentActivitiesInternal(intent, PACKAGE_MIME_TYPE, 1835008L, 0);
        if (matches.size() == 1) {
            android.content.pm.ResolveInfo resolveInfo = matches.get(0);
            if (!resolveInfo.activityInfo.applicationInfo.isPrivilegedApp()) {
                throw new java.lang.RuntimeException("The installer must be a privileged app");
            }
            return matches.get(0).getComponentInfo().packageName;
        }
        throw new java.lang.RuntimeException("There must be exactly one installer; found " + matches);
    }

    private java.lang.String getRequiredUninstallerLPr(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.UNINSTALL_PACKAGE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(android.net.Uri.fromParts("package", "foo.bar", null));
        android.content.pm.ResolveInfo resolveInfo = this.mResolveIntentHelper.resolveIntentInternal(computer, intent, null, 1835008L, 0L, 0, false, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
        if (resolveInfo == null || this.mResolveActivity.name.equals(resolveInfo.getComponentInfo().name)) {
            throw new java.lang.RuntimeException("There must be exactly one uninstaller; found " + resolveInfo);
        }
        return resolveInfo.getComponentInfo().packageName;
    }

    private java.lang.String getRequiredPermissionControllerLPr(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.MANAGE_PERMISSIONS");
        intent.addCategory("android.intent.category.DEFAULT");
        java.util.List<android.content.pm.ResolveInfo> matches = computer.queryIntentActivitiesInternal(intent, null, 1835008L, 0);
        if (matches.size() == 1) {
            android.content.pm.ResolveInfo resolveInfo = matches.get(0);
            if (!resolveInfo.activityInfo.applicationInfo.isPrivilegedApp()) {
                throw new java.lang.RuntimeException("The permissions manager must be a privileged app");
            }
            return matches.get(0).getComponentInfo().packageName;
        }
        throw new java.lang.RuntimeException("There must be exactly one permissions manager; found " + matches);
    }

    private android.content.ComponentName getIntentFilterVerifierComponentNameLPr(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.INTENT_FILTER_NEEDS_VERIFICATION");
        java.util.List<android.content.pm.ResolveInfo> matches = this.mResolveIntentHelper.queryIntentReceiversInternal(computer, intent, PACKAGE_MIME_TYPE, 1835008L, 0, android.os.Binder.getCallingUid());
        android.content.pm.ResolveInfo best = null;
        int N = matches.size();
        for (int i = 0; i < N; i++) {
            android.content.pm.ResolveInfo cur = matches.get(i);
            java.lang.String packageName = cur.getComponentInfo().packageName;
            if (checkPermission("android.permission.INTENT_FILTER_VERIFICATION_AGENT", packageName, 0) == 0 && (best == null || cur.priority > best.priority)) {
                best = cur;
            }
        }
        if (best != null) {
            return best.getComponentInfo().getComponentName();
        }
        android.util.Slog.w(TAG, "Intent filter verifier not found");
        return null;
    }

    private android.content.ComponentName getDomainVerificationAgentComponentNameLPr(com.android.server.pm.Computer computer, int userId) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.DOMAINS_NEED_VERIFICATION");
        java.util.List<android.content.pm.ResolveInfo> matches = this.mResolveIntentHelper.queryIntentReceiversInternal(computer, intent, null, 1835008L, userId, android.os.Binder.getCallingUid());
        android.content.pm.ResolveInfo best = null;
        int N = matches.size();
        for (int i = 0; i < N; i++) {
            android.content.pm.ResolveInfo cur = matches.get(i);
            java.lang.String packageName = cur.getComponentInfo().packageName;
            if (checkPermission("android.permission.DOMAIN_VERIFICATION_AGENT", packageName, userId) != 0) {
                android.util.Slog.w(TAG, "Domain verification agent found but does not hold permission: " + packageName);
            } else if (best == null || cur.priority > best.priority) {
                if (computer.isComponentEffectivelyEnabled(cur.getComponentInfo(), android.os.UserHandle.of(userId))) {
                    best = cur;
                } else {
                    android.util.Slog.w(TAG, "Domain verification agent found but not enabled");
                }
            }
        }
        if (best != null) {
            return best.getComponentInfo().getComponentName();
        }
        android.util.Slog.w(TAG, "Domain verification agent not found");
        return null;
    }

    android.content.ComponentName getInstantAppResolver(com.android.server.pm.Computer snapshot) {
        java.lang.String[] packageArray = this.mContext.getResources().getStringArray(android.R.array.config_emergency_iso_country_codes);
        if (packageArray.length == 0 && !android.os.Build.IS_DEBUGGABLE) {
            if (DEBUG_INSTANT) {
                android.util.Slog.d(TAG, "Ephemeral resolver NOT found; empty package list");
            }
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        int resolveFlags = (!android.os.Build.IS_DEBUGGABLE ? 1048576 : 0) | com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED;
        android.content.Intent resolverIntent = new android.content.Intent("android.intent.action.RESOLVE_INSTANT_APP_PACKAGE");
        java.util.List<android.content.pm.ResolveInfo> resolvers = snapshot.queryIntentServicesInternal(resolverIntent, null, resolveFlags, 0, callingUid, -1, false, false);
        int N = resolvers.size();
        if (N == 0) {
            if (DEBUG_INSTANT) {
                android.util.Slog.d(TAG, "Ephemeral resolver NOT found; no matching intent filters");
            }
            return null;
        }
        java.util.Set<java.lang.String> possiblePackages = new android.util.ArraySet<>(java.util.Arrays.asList(packageArray));
        for (int i = 0; i < N; i++) {
            android.content.pm.ResolveInfo info = resolvers.get(i);
            if (info.serviceInfo != null) {
                java.lang.String packageName = info.serviceInfo.packageName;
                if (!possiblePackages.contains(packageName) && !android.os.Build.IS_DEBUGGABLE) {
                    if (DEBUG_INSTANT) {
                        android.util.Slog.d(TAG, "Ephemeral resolver not in allowed package list; pkg: " + packageName + ", info:" + info);
                    }
                } else {
                    if (DEBUG_INSTANT) {
                        android.util.Slog.v(TAG, "Ephemeral resolver found; pkg: " + packageName + ", info:" + info);
                    }
                    return new android.content.ComponentName(packageName, info.serviceInfo.name);
                }
            }
        }
        if (DEBUG_INSTANT) {
            android.util.Slog.v(TAG, "Ephemeral resolver NOT found");
        }
        return null;
    }

    private android.content.pm.ActivityInfo getInstantAppInstallerLPr() {
        java.lang.String[] orderedActions;
        if (this.mIsEngBuild) {
            orderedActions = new java.lang.String[]{"android.intent.action.INSTALL_INSTANT_APP_PACKAGE_TEST", "android.intent.action.INSTALL_INSTANT_APP_PACKAGE"};
        } else {
            orderedActions = new java.lang.String[]{"android.intent.action.INSTALL_INSTANT_APP_PACKAGE"};
        }
        int resolveFlags = (this.mIsEngBuild ? 0 : 1048576) | (-2146697216);
        com.android.server.pm.Computer computer = snapshotComputer();
        android.content.Intent intent = new android.content.Intent();
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(android.net.Uri.fromFile(new java.io.File("foo.apk")), PACKAGE_MIME_TYPE);
        java.util.List<android.content.pm.ResolveInfo> matches = null;
        for (java.lang.String action : orderedActions) {
            intent.setAction(action);
            matches = computer.queryIntentActivitiesInternal(intent, PACKAGE_MIME_TYPE, resolveFlags, 0);
            if (!matches.isEmpty()) {
                break;
            }
            if (DEBUG_INSTANT) {
                android.util.Slog.d(TAG, "Instant App installer not found with " + action);
            }
        }
        java.util.Iterator<android.content.pm.ResolveInfo> iter = matches.iterator();
        while (iter.hasNext()) {
            android.content.pm.ResolveInfo rInfo = iter.next();
            if (checkPermission("android.permission.INSTALL_PACKAGES", rInfo.activityInfo.packageName, 0) != 0 && !this.mIsEngBuild) {
                iter.remove();
            }
        }
        if (matches.size() == 0) {
            return null;
        }
        if (matches.size() == 1) {
            return (android.content.pm.ActivityInfo) matches.get(0).getComponentInfo();
        }
        throw new java.lang.RuntimeException("There must be at most one ephemeral installer; found " + matches);
    }

    private android.content.ComponentName getInstantAppResolverSettingsLPr(com.android.server.pm.Computer computer, android.content.ComponentName resolver) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.INSTANT_APP_RESOLVER_SETTINGS").addCategory("android.intent.category.DEFAULT").setPackage(resolver.getPackageName());
        java.util.List<android.content.pm.ResolveInfo> matches = computer.queryIntentActivitiesInternal(intent, null, 786432L, 0);
        if (matches.isEmpty()) {
            return null;
        }
        return matches.get(0).getComponentInfo().getComponentName();
    }

    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String groupName, int flags) {
        return ((android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class)).getPermissionGroupInfo(groupName, flags);
    }

    public void freeAllAppCacheAboveQuota(java.lang.String volumeUuid) throws java.io.IOException {
        try {
            com.android.server.pm.PackageManagerTracedLock installLock = this.mInstallLock.acquireLock();
            try {
                this.mInstaller.freeCache(volumeUuid, Long.MAX_VALUE, com.android.server.wm.IActivityRecordExt.REASON_STARTING_WINDOW_ALREADY_SHOWN);
                if (installLock != null) {
                    installLock.close();
                }
            } finally {
            }
        } catch (com.android.server.pm.Installer.InstallerException e) {
        }
    }

    public void freeStorage(java.lang.String volumeUuid, long bytes, int flags) throws java.io.IOException {
        this.mFreeStorageHelper.freeStorage(volumeUuid, bytes, flags);
    }

    public static java.lang.String deriveCodePathName(java.lang.String codePath) {
        if (codePath == null) {
            return null;
        }
        java.io.File codeFile = new java.io.File(codePath);
        java.lang.String name = codeFile.getName();
        if (codeFile.isDirectory()) {
            return name;
        }
        if (name.endsWith(".apk") || name.endsWith(".tmp")) {
            int lastDot = name.lastIndexOf(46);
            return name.substring(0, lastDot);
        }
        android.util.Slog.w(TAG, "Odd, " + codePath + " doesn't look like an APK");
        return null;
    }

    int freeCacheForInstallation(int recommendedInstallLocation, android.content.pm.parsing.PackageLite pkgLite, java.lang.String resolvedPath, java.lang.String mPackageAbiOverride, int installFlags) {
        return this.mFreeStorageHelper.freeCacheForInstallation(recommendedInstallLocation, pkgLite, resolvedPath, mPackageAbiOverride, installFlags);
    }

    public android.content.pm.ModuleInfo getModuleInfo(java.lang.String packageName, int flags) {
        return this.mModuleInfoProvider.getModuleInfo(packageName, flags);
    }

    void updateSequenceNumberLP(com.android.server.pm.PackageSetting pkgSetting, int[] userList) {
        this.mChangedPackagesTracker.updateSequenceNumber(pkgSetting.getPackageName(), userList);
    }

    public boolean hasSystemFeature(java.lang.String name, int version) {
        java.lang.Boolean extRet = this.mPackageManagerServiceExt.hasSystemFeatureExtAtBegin(name, version);
        if (extRet != null) {
            return extRet.booleanValue();
        }
        android.content.pm.FeatureInfo feat = this.mAvailableFeatures.get(name);
        return feat != null && feat.version >= version;
    }

    public int checkPermission(java.lang.String permName, java.lang.String pkgName, int userId) {
        java.lang.Integer customRet = this.mPackageManagerServiceExt.checkPermissionExtAtBegin(permName, pkgName, userId);
        if (customRet != null) {
            return customRet.intValue();
        }
        return this.mPermissionManager.checkPermission(pkgName, permName, "default:0", userId);
    }

    public java.lang.String getSdkSandboxPackageName() {
        return this.mRequiredSdkSandboxPackage;
    }

    java.lang.String getPackageInstallerPackageName() {
        return this.mRequiredInstallerPackage;
    }

    void requestInstantAppResolutionPhaseTwo(android.content.pm.AuxiliaryResolveInfo responseObj, android.content.Intent origIntent, java.lang.String resolvedType, java.lang.String callingPackage, java.lang.String callingFeatureId, boolean isRequesterInstantApp, android.os.Bundle verificationBundle, int userId) {
        android.os.Message msg = this.mHandler.obtainMessage(20, new android.content.pm.InstantAppRequest(responseObj, origIntent, resolvedType, callingPackage, callingFeatureId, isRequesterInstantApp, userId, verificationBundle, false, responseObj.hostDigestPrefixSecure, responseObj.token));
        this.mHandler.sendMessage(msg);
    }

    static class FindPreferredActivityBodyResult {
        boolean mChanged;
        android.content.pm.ResolveInfo mPreferredResolveInfo;

        FindPreferredActivityBodyResult() {
        }
    }

    public android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> queryIntentReceivers(com.android.server.pm.Computer snapshot, android.content.Intent intent, java.lang.String resolvedType, long flags, int userId) {
        return new android.content.pm.ParceledListSlice<>(this.mResolveIntentHelper.queryIntentReceiversInternal(snapshot, intent, resolvedType, flags, userId, android.os.Binder.getCallingUid()));
    }

    public static void reportSettingsProblem(int priority, java.lang.String msg) {
        com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(priority, msg);
    }

    static void renameStaticSharedLibraryPackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage) {
        parsedPackage.setPackageName(toStaticSharedLibraryPackageName(parsedPackage.getPackageName(), parsedPackage.getStaticSharedLibraryVersion()));
    }

    private static java.lang.String toStaticSharedLibraryPackageName(java.lang.String packageName, long libraryVersion) {
        return packageName + STATIC_SHARED_LIB_DELIMITER + libraryVersion;
    }

    public void performFstrimIfNeeded() {
        this.mFreeStorageHelper.performFstrimIfNeeded();
    }

    public void updatePackagesIfNeeded() {
        this.mDexOptHelper.performPackageDexOptUpgradeIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPackageUseInternal(java.lang.String packageName, int reason) {
        this.mPackageManagerServiceExt.notifyPackageUseLocked(packageName, reason);
        long time = java.lang.System.currentTimeMillis();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.PackageSetting pkgSetting = this.mSettings.getPackageLPr(packageName);
                if (pkgSetting == null) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                } else {
                    pkgSetting.getPkgState().setLastPackageUsageTimeInMills(reason, time);
                    resetPriorityAfterPackageManagerTracedLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    com.android.server.pm.dex.DexManager getDexManager() {
        return this.mDexManager;
    }

    com.android.server.pm.DexOptHelper getDexOptHelper() {
        return this.mDexOptHelper;
    }

    com.android.server.pm.dex.DynamicCodeLogger getDynamicCodeLogger() {
        return this.mDynamicCodeLogger;
    }

    public void shutdown() {
        this.mCompilerStats.writeNow();
        this.mDexManager.writePackageDexUsageNow();
        this.mDynamicCodeLogger.writeNow();
        com.android.server.PackageWatchdog.getInstance(this.mContext).writeNow();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPackageUsage.writeNow(this.mSettings.getPackagesLocked());
                if (this.mHandler.hasMessages(13) || this.mBackgroundHandler.hasMessages(14) || this.mHandler.hasMessages(19)) {
                    writeSettings(true);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
        this.mPackageManagerServiceExt.shutdownExtAtEnd();
    }

    int[] resolveUserIds(int userId) {
        return userId == -1 ? this.mUserManager.getUserIds() : new int[]{userId};
    }

    private void setUpInstantAppInstallerActivityLP(android.content.pm.ActivityInfo installerActivity) {
        if (installerActivity == null) {
            if (DEBUG_INSTANT) {
                android.util.Slog.d(TAG, "Clear ephemeral installer activity");
            }
            this.mInstantAppInstallerActivity = null;
            onChanged();
            return;
        }
        if (DEBUG_INSTANT) {
            android.util.Slog.d(TAG, "Set ephemeral installer activity: " + installerActivity.getComponentName());
        }
        this.mInstantAppInstallerActivity = installerActivity;
        this.mInstantAppInstallerActivity.flags |= 288;
        this.mInstantAppInstallerActivity.exported = true;
        this.mInstantAppInstallerActivity.enabled = true;
        this.mInstantAppInstallerInfo.activityInfo = this.mInstantAppInstallerActivity;
        this.mInstantAppInstallerInfo.priority = 1;
        this.mInstantAppInstallerInfo.preferredOrder = 1;
        this.mInstantAppInstallerInfo.isDefault = true;
        this.mInstantAppInstallerInfo.match = 5799936;
        onChanged();
    }

    void killApplication(java.lang.String pkgName, int appId, java.lang.String reason, int exitInfoReason) {
        killApplication(pkgName, appId, -1, reason, exitInfoReason);
    }

    void killApplication(java.lang.String pkgName, int appId, int userId, java.lang.String reason, int exitInfoReason) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.IActivityManager am = android.app.ActivityManager.getService();
            if (am != null) {
                try {
                    am.killApplication(pkgName, appId, userId, reason, exitInfoReason);
                } catch (android.os.RemoteException e) {
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void killApplicationSync(java.lang.String pkgName, int appId, int userId, java.lang.String reason, int exitInfoReason) {
        android.app.ActivityManagerInternal mAmi = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        if (java.lang.Thread.holdsLock(this.mLock) || mAmi == null) {
            android.util.Slog.e(TAG, "Holds PM's lock, unable kill application synchronized");
            killApplication(pkgName, appId, userId, reason, exitInfoReason);
            return;
        }
        com.android.server.pm.KillAppBlocker blocker = new com.android.server.pm.KillAppBlocker();
        try {
            blocker.register();
            mAmi.killApplicationSync(pkgName, appId, userId, reason, exitInfoReason);
            blocker.waitAppProcessGone(mAmi, snapshotComputer(), this.mUserManager, pkgName);
        } finally {
            blocker.unregister();
        }
    }

    @Override // com.android.server.pm.PackageSender
    public void notifyPackageAdded(java.lang.String packageName, int uid) {
        this.mPackageObserverHelper.notifyAdded(packageName, uid);
    }

    @Override // com.android.server.pm.PackageSender
    public void notifyPackageChanged(java.lang.String packageName, int uid) {
        this.mPackageObserverHelper.notifyChanged(packageName, uid);
    }

    @Override // com.android.server.pm.PackageSender
    public void notifyPackageRemoved(java.lang.String packageName, int uid) {
        this.mPackageObserverHelper.notifyRemoved(packageName, uid);
        android.content.pm.UserPackage.removeFromCache(android.os.UserHandle.getUserId(uid), packageName);
    }

    boolean isUserRestricted(int userId, java.lang.String restrictionKey) {
        android.os.Bundle restrictions = this.mUserManager.getUserRestrictions(userId);
        if (!restrictions.getBoolean(restrictionKey, false)) {
            return false;
        }
        android.util.Log.w(TAG, "User is restricted: " + restrictionKey);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCanSetPackagesSuspendedAsUser(com.android.server.pm.Computer snapshot, boolean quarantined, android.content.pm.UserPackage suspender, int callingUid, int targetUserId, java.lang.String callingMethod) {
        if (callingUid == 0 || android.os.UserHandle.getAppId(callingUid) == 1000) {
            return;
        }
        java.lang.String ownerPackage = this.mProtectedPackages.getDeviceOwnerOrProfileOwnerPackage(targetUserId);
        if (ownerPackage != null) {
            int ownerUid = snapshot.getPackageUid(ownerPackage, 0L, targetUserId);
            if (ownerUid == callingUid) {
                return;
            }
        }
        if (quarantined) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.QUARANTINE_APPS", callingMethod);
        } else {
            this.mContext.enforceCallingOrSelfPermission("android.permission.SUSPEND_APPS", callingMethod);
        }
        if (android.app.admin.flags.Flags.crossUserSuspensionEnabledRo()) {
            int suspendingPackageUid = snapshot.getPackageUid(suspender.packageName, 0L, suspender.userId);
            if (suspendingPackageUid != callingUid) {
                throw new java.lang.SecurityException("Suspender package %s doesn't match calling uid %d".formatted(suspender.packageName, java.lang.Integer.valueOf(callingUid)));
            }
            if (targetUserId != suspender.userId) {
                this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingMethod);
                return;
            }
            return;
        }
        int packageUid = snapshot.getPackageUid(suspender.packageName, 0L, targetUserId);
        boolean allowedPackageUid = packageUid == callingUid;
        boolean allowedShell = callingUid == 2000 && android.os.UserHandle.isSameApp(packageUid, callingUid);
        if (!allowedShell && !allowedPackageUid) {
            throw new java.lang.SecurityException("Suspending package " + suspender.packageName + " in user " + targetUserId + " does not belong to calling uid " + callingUid);
        }
    }

    void unsuspendForSuspendingPackage(com.android.server.pm.Computer computer, java.lang.String suspendingPackage, int suspendingUserId, boolean inAllUsers) {
        java.lang.String[] allPackages = (java.lang.String[]) computer.getPackageStates().keySet().toArray(new java.lang.String[0]);
        final android.content.pm.UserPackage userPackageOf = android.content.pm.UserPackage.of(suspendingUserId, suspendingPackage);
        java.util.Objects.requireNonNull(userPackageOf);
        java.util.function.Predicate<android.content.pm.UserPackage> suspenderPredicate = new java.util.function.Predicate() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda60
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return userPackageOf.equals((android.content.pm.UserPackage) obj);
            }
        };
        if (!android.app.admin.flags.Flags.crossUserSuspensionEnabledRo() || !inAllUsers) {
            this.mSuspendPackageHelper.removeSuspensionsBySuspendingPackage(computer, allPackages, suspenderPredicate, suspendingUserId);
            return;
        }
        for (int targetUserId : this.mUserManager.getUserIds()) {
            this.mSuspendPackageHelper.removeSuspensionsBySuspendingPackage(computer, allPackages, suspenderPredicate, targetUserId);
        }
    }

    void removeAllDistractingPackageRestrictions(com.android.server.pm.Computer snapshot, int userId) {
        java.lang.String[] allPackages = snapshot.getAllAvailablePackageNames();
        this.mDistractingPackageHelper.removeDistractingPackageRestrictions(snapshot, allPackages, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCanSetDistractingPackageRestrictionsAsUser(int callingUid, int userId, java.lang.String callingMethod) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.SUSPEND_APPS", callingMethod);
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot(callingUid) && android.os.UserHandle.getUserId(callingUid) != userId) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " cannot call for user " + userId);
        }
        if (DEBUG_SETTINGS) {
            android.util.Slog.d(TAG, "removeDistractingPackageRestrictions end");
        }
    }

    void setEnableRollbackCode(int token, int enableRollbackCode) {
        android.os.Message msg = this.mHandler.obtainMessage(21);
        msg.arg1 = token;
        msg.arg2 = enableRollbackCode;
        this.mHandler.sendMessage(msg);
    }

    void notifyFirstLaunch(final java.lang.String packageName, final java.lang.String installerPackage, final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyFirstLaunch$46(packageName, userId, installerPackage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyFirstLaunch$46(java.lang.String packageName, int userId, java.lang.String installerPackage) {
        for (int i = 0; i < this.mRunningInstalls.size(); i++) {
            com.android.server.pm.InstallRequest installRequest = this.mRunningInstalls.valueAt(i);
            if (installRequest.getReturnCode() == 1 && packageName.equals(installRequest.getPkg().getPackageName())) {
                for (int uIndex = 0; uIndex < installRequest.getNewUsers().length; uIndex++) {
                    if (userId == installRequest.getNewUsers()[uIndex]) {
                        if (DEBUG_BACKUP) {
                            android.util.Slog.i(TAG, "Package " + packageName + " being restored so deferring FIRST_LAUNCH");
                            return;
                        }
                        return;
                    }
                }
            }
        }
        if (DEBUG_BACKUP) {
            android.util.Slog.i(TAG, "Package " + packageName + " sending normal FIRST_LAUNCH");
        }
        boolean isInstantApp = snapshotComputer().isInstantAppInternal(packageName, userId, 1000);
        int[] userIds = isInstantApp ? EMPTY_INT_ARRAY : new int[]{userId};
        int[] instantUserIds = isInstantApp ? new int[]{userId} : EMPTY_INT_ARRAY;
        this.mBroadcastHelper.sendFirstLaunchBroadcast(packageName, installerPackage, userIds, instantUserIds);
    }

    com.android.server.pm.Settings.VersionInfo getSettingsVersionForPackage(com.android.server.pm.pkg.AndroidPackage pkg) {
        if (pkg.isExternalStorage()) {
            if (android.text.TextUtils.isEmpty(pkg.getVolumeUuid())) {
                return this.mSettings.getExternalVersion();
            }
            return this.mSettings.findOrCreateVersion(pkg.getVolumeUuid());
        }
        return this.mSettings.getInternalVersion();
    }

    public void deleteExistingPackageAsUser(android.content.pm.VersionedPackage versionedPackage, android.content.pm.IPackageDeleteObserver2 observer, int userId) {
        this.mDeletePackageHelper.deleteExistingPackageAsUser(versionedPackage, observer, userId);
    }

    public void deletePackageVersioned(android.content.pm.VersionedPackage versionedPackage, android.content.pm.IPackageDeleteObserver2 observer, int userId, int deleteFlags) {
        this.mDeletePackageHelper.deletePackageVersionedInternal(versionedPackage, observer, userId, deleteFlags, false);
    }

    boolean isCallerVerifier(com.android.server.pm.Computer snapshot, int callingUid) {
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        for (java.lang.String requiredVerifierPackage : this.mRequiredVerifierPackages) {
            if (callingUid == snapshot.getPackageUid(requiredVerifierPackage, 0L, callingUserId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPackageDeviceAdminOnAnyUser(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (snapshot.checkUidPermission("android.permission.MANAGE_USERS", callingUid) != 0) {
            android.util.EventLog.writeEvent(1397638484, "128599183", -1, "");
            throw new java.lang.SecurityException("android.permission.MANAGE_USERS permission is required to call this API");
        }
        if (snapshot.getInstantAppPackageName(callingUid) != null && !snapshot.isCallerSameApp(packageName, callingUid)) {
            return false;
        }
        return isPackageDeviceAdmin(packageName, -1);
    }

    boolean isPackageDeviceAdmin(java.lang.String packageName, int userId) {
        int[] targetUsers;
        android.app.admin.IDevicePolicyManager dpm = getDevicePolicyManager();
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) this.mInjector.getLocalService(android.app.admin.DevicePolicyManagerInternal.class);
        if (dpm != null && dpmi != null) {
            try {
                android.content.ComponentName deviceOwnerComponentName = dpm.getDeviceOwnerComponent(false);
                java.lang.String deviceOwnerPackageName = deviceOwnerComponentName == null ? null : deviceOwnerComponentName.getPackageName();
                if (packageName.equals(deviceOwnerPackageName)) {
                    return true;
                }
                int[] allUsers = this.mUserManager.getUserIds();
                if (userId == -1) {
                    targetUsers = allUsers;
                } else {
                    try {
                        targetUsers = new int[]{userId};
                    } catch (android.os.RemoteException e) {
                    }
                }
                for (int i : targetUsers) {
                    if (dpm.packageHasActiveAdmins(packageName, i)) {
                        return true;
                    }
                }
                com.android.server.pm.pkg.PackageStateInternal packageState = snapshotComputer().getPackageStateInternal(packageName);
                if (packageState == null) {
                    return false;
                }
                for (int user : packageState.isSystem() ? allUsers : targetUsers) {
                    if (isDeviceManagementRoleHolder(packageName, user) && dpmi.isUserOrganizationManaged(user)) {
                        return true;
                    }
                }
            } catch (android.os.RemoteException e2) {
            }
        }
        return false;
    }

    private boolean isDeviceManagementRoleHolder(java.lang.String packageName, int userId) {
        return java.util.Objects.equals(packageName, getDevicePolicyManagementRoleHolderPackageName(userId));
    }

    public java.lang.String getDevicePolicyManagementRoleHolderPackageName(final int userId) {
        return (java.lang.String) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda22
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$getDevicePolicyManagementRoleHolderPackageName$47(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String lambda$getDevicePolicyManagementRoleHolderPackageName$47(int userId) throws java.lang.Exception {
        android.app.role.RoleManager roleManager = (android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class);
        java.util.List<java.lang.String> roleHolders = roleManager.getRoleHoldersAsUser("android.app.role.DEVICE_POLICY_MANAGEMENT", android.os.UserHandle.of(userId));
        if (roleHolders.isEmpty()) {
            return null;
        }
        return roleHolders.get(0);
    }

    private android.app.admin.IDevicePolicyManager getDevicePolicyManager() {
        if (this.mDevicePolicyManager == null) {
            this.mDevicePolicyManager = android.app.admin.IDevicePolicyManager.Stub.asInterface(android.os.ServiceManager.getService("device_policy"));
        }
        return this.mDevicePolicyManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clearApplicationUserDataLIF(com.android.server.pm.Computer snapshot, java.lang.String packageName, int userId) {
        int flags;
        if (packageName == null) {
            android.util.Slog.w(TAG, "Attempt to delete null packageName.");
            return false;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = snapshot.getPackage(packageName);
        if (pkg == null) {
            android.util.Slog.w(TAG, "Package named '" + packageName + "' doesn't exist.");
            return false;
        }
        this.mPermissionManager.resetRuntimePermissions(pkg, userId);
        this.mAppDataHelper.clearAppDataLIF(pkg, userId, 7);
        int appId = android.os.UserHandle.getAppId(pkg.getUid());
        this.mAppDataHelper.clearKeystoreData(userId, appId);
        com.android.server.pm.UserManagerInternal umInternal = this.mInjector.getUserManagerInternal();
        android.os.storage.StorageManagerInternal smInternal = (android.os.storage.StorageManagerInternal) this.mInjector.getLocalService(android.os.storage.StorageManagerInternal.class);
        if (android.os.storage.StorageManager.isCeStorageUnlocked(userId) && smInternal.isCeStoragePrepared(userId)) {
            flags = 3;
        } else if (umInternal.isUserRunning(userId)) {
            flags = 1;
        } else {
            flags = 0;
        }
        this.mAppDataHelper.prepareAppDataContentsLIF(pkg, snapshot.getPackageStateInternal(packageName), userId, flags);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetComponentEnabledSettingsIfNeededLPw(java.lang.String packageName, final int userId) {
        final com.android.server.pm.PackageSetting pkgSetting;
        com.android.server.pm.pkg.AndroidPackage pkg = packageName != null ? this.mPackages.get(packageName) : null;
        if (pkg == null || !pkg.isResetEnabledSettingsOnAppDataCleared() || (pkgSetting = this.mSettings.getPackageLPr(packageName)) == null) {
            return;
        }
        final java.util.ArrayList<java.lang.String> updatedComponents = new java.util.ArrayList<>();
        java.util.function.Consumer<? super com.android.internal.pm.pkg.component.ParsedMainComponent> resetSettings = new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda63
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.PackageManagerService.lambda$resetComponentEnabledSettingsIfNeededLPw$48(pkgSetting, userId, updatedComponents, (com.android.internal.pm.pkg.component.ParsedMainComponent) obj);
            }
        };
        for (int i = 0; i < pkg.getActivities().size(); i++) {
            resetSettings.accept(pkg.getActivities().get(i));
        }
        for (int i2 = 0; i2 < pkg.getReceivers().size(); i2++) {
            resetSettings.accept(pkg.getReceivers().get(i2));
        }
        for (int i3 = 0; i3 < pkg.getServices().size(); i3++) {
            resetSettings.accept(pkg.getServices().get(i3));
        }
        for (int i4 = 0; i4 < pkg.getProviders().size(); i4++) {
            resetSettings.accept(pkg.getProviders().get(i4));
        }
        if (com.android.internal.util.ArrayUtils.isEmpty(updatedComponents)) {
            return;
        }
        updateSequenceNumberLP(pkgSetting, new int[]{userId});
        updateInstantAppInstallerLocked(packageName);
        scheduleWritePackageRestrictions(userId);
        this.mPendingBroadcasts.addComponents(userId, packageName, updatedComponents);
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.sendEmptyMessageDelayed(1, 1000L);
        }
    }

    static /* synthetic */ void lambda$resetComponentEnabledSettingsIfNeededLPw$48(com.android.server.pm.PackageSetting pkgSetting, int userId, java.util.ArrayList updatedComponents, com.android.internal.pm.pkg.component.ParsedMainComponent component) {
        if (pkgSetting.restoreComponentLPw(component.getClassName(), userId)) {
            updatedComponents.add(component.getClassName());
        }
    }

    void clearPackagePreferredActivitiesLPw(java.lang.String packageName, android.util.SparseBooleanArray outUserChanged, int userId) {
        this.mSettings.clearPackagePreferredActivities(packageName, outUserChanged, userId);
    }

    void restorePermissionsAndUpdateRolesForNewUserInstall(java.lang.String packageName, int userId) {
        java.lang.String defaultBrowser;
        this.mPermissionManager.restoreDelayedRuntimePermissions(packageName, userId);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                defaultBrowser = this.mSettings.getPendingDefaultBrowserLPr(userId);
            } finally {
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
        if (java.util.Objects.equals(packageName, defaultBrowser)) {
            this.mDefaultAppProvider.setDefaultBrowser(packageName, userId);
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
            boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    this.mSettings.removePendingDefaultBrowserLPw(userId);
                } finally {
                }
            }
            resetPriorityAfterPackageManagerTracedLockedSection();
        }
        this.mPreferredActivityHelper.updateDefaultHomeNotLocked(snapshotComputer(), userId);
    }

    public void addCrossProfileIntentFilter(com.android.server.pm.Computer snapshot, com.android.server.pm.WatchedIntentFilter intentFilter, java.lang.String ownerPackage, int sourceUserId, int targetUserId, int flags) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        int callingUid = android.os.Binder.getCallingUid();
        enforceOwnerRights(snapshot, ownerPackage, callingUid);
        this.mUserManager.enforceCrossProfileIntentFilterAccess(sourceUserId, targetUserId, callingUid, true);
        com.android.server.pm.PackageManagerServiceUtils.enforceShellRestriction(this.mInjector.getUserManagerInternal(), "no_debugging_features", callingUid, sourceUserId);
        if (!intentFilter.checkDataPathAndSchemeSpecificParts()) {
            android.util.EventLog.writeEvent(1397638484, "246749936", java.lang.Integer.valueOf(callingUid));
            throw new java.lang.IllegalArgumentException("Invalid intent data paths or scheme specific parts in the filter.");
        }
        if (intentFilter.countActions() == 0) {
            android.util.Slog.w(TAG, "Cannot set a crossProfile intent filter with no filter actions");
            return;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.CrossProfileIntentFilter newFilter = new com.android.server.pm.CrossProfileIntentFilter(intentFilter, ownerPackage, targetUserId, flags, this.mUserManager.getCrossProfileIntentFilterAccessControl(sourceUserId, targetUserId));
                com.android.server.pm.CrossProfileIntentResolver resolver = this.mSettings.editCrossProfileIntentResolverLPw(sourceUserId);
                java.util.ArrayList<com.android.server.pm.CrossProfileIntentFilter> existing = resolver.findFilters(intentFilter);
                if (existing != null) {
                    int size = existing.size();
                    for (int i = 0; i < size; i++) {
                        if (newFilter.equalsIgnoreFilter(existing.get(i))) {
                            resetPriorityAfterPackageManagerTracedLockedSection();
                            return;
                        }
                    }
                }
                resolver.addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) snapshotComputer(), newFilter);
                resetPriorityAfterPackageManagerTracedLockedSection();
                scheduleWritePackageRestrictions(sourceUserId);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceOwnerRights(com.android.server.pm.Computer snapshot, java.lang.String pkg, int callingUid) {
        if (android.os.UserHandle.getAppId(callingUid) == 1000) {
            return;
        }
        java.lang.String[] callerPackageNames = snapshot.getPackagesForUid(callingUid);
        if (!com.android.internal.util.ArrayUtils.contains(callerPackageNames, pkg)) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " does not own package " + pkg);
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        android.content.pm.PackageInfo pi = snapshot.getPackageInfo(pkg, 0L, callingUserId);
        if (pi == null) {
            throw new java.lang.IllegalArgumentException("Unknown package " + pkg + " on user " + callingUserId);
        }
    }

    public void sendSessionCommitBroadcast(android.content.pm.PackageInstaller.SessionInfo sessionInfo, int userId) {
        this.mBroadcastHelper.sendSessionCommitBroadcast(snapshotComputer(), sessionInfo, userId, this.mAppPredictionServicePackage);
    }

    private java.lang.String getSetupWizardPackageNameImpl(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.SETUP_WIZARD");
        java.util.List<android.content.pm.ResolveInfo> matches = computer.queryIntentActivitiesInternal(intent, null, 1835520L, android.os.UserHandle.myUserId());
        if (matches.size() == 1) {
            return matches.get(0).getComponentInfo().packageName;
        }
        android.util.Slog.e(TAG, "There should probably be exactly one setup wizard; found " + matches.size() + ": matches=" + matches);
        return null;
    }

    private java.lang.String getStorageManagerPackageName(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("android.os.storage.action.MANAGE_STORAGE");
        java.util.List<android.content.pm.ResolveInfo> matches = computer.queryIntentActivitiesInternal(intent, null, 1835520L, android.os.UserHandle.myUserId());
        if (matches.size() == 1) {
            return matches.get(0).getComponentInfo().packageName;
        }
        android.util.Slog.w(TAG, "There should probably be exactly one storage manager; found " + matches.size() + ": matches=" + matches);
        return null;
    }

    private static java.lang.String getRequiredSdkSandboxPackageName(com.android.server.pm.Computer computer) {
        android.content.Intent intent = new android.content.Intent("com.android.sdksandbox.SdkSandboxService");
        java.util.List<android.content.pm.ResolveInfo> matches = computer.queryIntentServicesInternal(intent, null, 1835008L, 0, android.os.Process.myUid(), -1, false, false);
        if (matches.size() == 1) {
            return matches.get(0).getComponentInfo().packageName;
        }
        throw new java.lang.RuntimeException("There should exactly one sdk sandbox package; found " + matches.size() + ": matches=" + matches);
    }

    private java.lang.String getRetailDemoPackageName() {
        com.android.server.pm.pkg.AndroidPackage androidPkg;
        android.content.pm.SigningDetails signingDetail;
        java.lang.String predefinedPkgName = this.mContext.getString(android.R.string.config_satellite_service_package);
        java.lang.String predefinedSignature = this.mContext.getString(android.R.string.config_satellite_sim_plmn_identifier);
        if (!android.text.TextUtils.isEmpty(predefinedPkgName) && !android.text.TextUtils.isEmpty(predefinedSignature) && (androidPkg = this.mPackages.get(predefinedPkgName)) != null && (signingDetail = androidPkg.getSigningDetails()) != null && signingDetail.getSignatures() != null) {
            try {
                java.security.MessageDigest msgDigest = java.security.MessageDigest.getInstance("SHA-256");
                for (android.content.pm.Signature signature : signingDetail.getSignatures()) {
                    if (android.text.TextUtils.equals(predefinedSignature, libcore.util.HexEncoding.encodeToString(msgDigest.digest(signature.toByteArray()), false))) {
                        return predefinedPkgName;
                    }
                }
            } catch (java.security.NoSuchAlgorithmException e) {
                android.util.Slog.e(TAG, "Unable to verify signatures as getting the retail demo package name", e);
            }
        }
        return null;
    }

    java.lang.String getPackageFromComponentString(int stringResId) {
        android.content.ComponentName component;
        java.lang.String componentString = this.mContext.getString(stringResId);
        if (android.text.TextUtils.isEmpty(componentString) || (component = android.content.ComponentName.unflattenFromString(componentString)) == null) {
            return null;
        }
        return component.getPackageName();
    }

    java.lang.String ensureSystemPackageName(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        if (packageName == null) {
            return null;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (snapshot.getPackageInfo(packageName, 2097152L, 0) == null) {
                android.content.pm.PackageInfo packageInfo = snapshot.getPackageInfo(packageName, 0L, 0);
                if (packageInfo != null) {
                    android.util.EventLog.writeEvent(1397638484, "145981139", java.lang.Integer.valueOf(packageInfo.applicationInfo.uid), "");
                }
                android.util.Log.w(TAG, "Missing required system package: " + packageName + (packageInfo != null ? ", but found with extended search." : "."));
                return null;
            }
            return packageName;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void updateComponentLabelIcon(final android.content.ComponentName componentName, final java.lang.String nonLocalizedLabel, final java.lang.Integer icon, final int userId) {
        if (componentName == null) {
            throw new java.lang.IllegalArgumentException("Must specify a component");
        }
        int callingUid = android.os.Binder.getCallingUid();
        java.lang.String componentPkgName = componentName.getPackageName();
        com.android.server.pm.Computer computer = snapshotComputer();
        int componentUid = computer.getPackageUid(componentPkgName, 0L, userId);
        if (!android.os.UserHandle.isSameApp(callingUid, componentUid)) {
            throw new java.lang.SecurityException("The calling UID (" + callingUid + ") does not match the target UID");
        }
        java.lang.String allowedCallerPkg = this.mContext.getString(android.R.string.config_pointing_ui_package);
        if (android.text.TextUtils.isEmpty(allowedCallerPkg)) {
            throw new java.lang.SecurityException("There is no package defined as allowed to change a component's label or icon");
        }
        int allowedCallerUid = computer.getPackageUid(allowedCallerPkg, 1048576L, userId);
        if (allowedCallerUid == -1 || !android.os.UserHandle.isSameApp(callingUid, allowedCallerUid)) {
            throw new java.lang.SecurityException("The calling UID (" + callingUid + ") is not allowed to change a component's label or icon");
        }
        com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateInternal(componentPkgName);
        if (packageState != null && packageState.getPkg() != null) {
            if (packageState.isSystem() || packageState.isUpdatedSystemApp()) {
                if (!computer.getComponentResolver().componentExists(componentName)) {
                    throw new java.lang.IllegalArgumentException("Component " + componentName + " not found");
                }
                android.util.Pair<java.lang.String, java.lang.Integer> overrideLabelIcon = packageState.getUserStateOrDefault(userId).getOverrideLabelIconForComponent(componentName);
                java.lang.String existingLabel = overrideLabelIcon == null ? null : (java.lang.String) overrideLabelIcon.first;
                java.lang.Integer existingIcon = overrideLabelIcon == null ? null : (java.lang.Integer) overrideLabelIcon.second;
                if (!android.text.TextUtils.equals(existingLabel, nonLocalizedLabel) || !java.util.Objects.equals(existingIcon, icon)) {
                    commitPackageStateMutation(null, componentPkgName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).userState(userId).setComponentLabelIcon(componentName, nonLocalizedLabel, icon);
                        }
                    });
                    this.mPendingBroadcasts.addComponent(userId, componentPkgName, componentName.getClassName());
                    if (!this.mHandler.hasMessages(1)) {
                        this.mHandler.sendEmptyMessageDelayed(1, 1000L);
                        return;
                    }
                    return;
                }
                return;
            }
        }
        throw new java.lang.SecurityException("Changing the label is not allowed for " + componentName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnabledSettings(java.util.List<android.content.pm.PackageManager.ComponentEnabledSetting> settings, int userId, java.lang.String callingPackage) throws java.lang.Throwable {
        com.android.server.pm.Computer snapshot;
        int i;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock;
        int i2;
        int inputFlags;
        int inputFlags2;
        com.android.server.pm.Computer computer;
        int inputFlags3;
        int targetSize;
        android.content.pm.PackageManager.ComponentEnabledSetting setting;
        java.lang.String packageName;
        boolean[] updateAllowed;
        com.android.server.pm.Computer snapshot2;
        java.util.List<android.content.pm.PackageManager.ComponentEnabledSetting> list = settings;
        int i3 = userId;
        this.mPackageManagerServiceExt.onStartSetEnabledSettingForInformation(list, i3, callingPackage);
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.Computer preLockSnapshot = snapshotComputer();
        preLockSnapshot.enforceCrossUserPermission(callingUid, userId, false, true, "set enabled");
        int callingPid = android.os.Binder.getCallingPid();
        int targetSize2 = settings.size();
        for (int i4 = 0; i4 < targetSize2; i4++) {
            int newState = list.get(i4).getEnabledState();
            if (newState != 0 && newState != 1 && newState != 2 && newState != 3 && newState != 4) {
                throw new java.lang.IllegalArgumentException("Invalid new component state: " + newState);
            }
        }
        if (targetSize2 > 1) {
            android.util.ArraySet<java.lang.String> checkDuplicatedPackage = new android.util.ArraySet<>();
            android.util.ArraySet<android.content.ComponentName> checkDuplicatedComponent = new android.util.ArraySet<>();
            android.util.ArrayMap<java.lang.String, java.lang.Integer> checkConflictFlag = new android.util.ArrayMap<>();
            for (int i5 = 0; i5 < targetSize2; i5++) {
                android.content.pm.PackageManager.ComponentEnabledSetting setting2 = list.get(i5);
                java.lang.String packageName2 = setting2.getPackageName();
                if (setting2.isComponent()) {
                    android.content.ComponentName componentName = setting2.getComponentName();
                    if (checkDuplicatedComponent.contains(componentName)) {
                        throw new java.lang.IllegalArgumentException("The component " + componentName + " is duplicated");
                    }
                    checkDuplicatedComponent.add(componentName);
                    java.lang.Integer enabledFlags = checkConflictFlag.get(packageName2);
                    if (enabledFlags == null) {
                        checkConflictFlag.put(packageName2, java.lang.Integer.valueOf(setting2.getEnabledFlags()));
                    } else if ((enabledFlags.intValue() & 1) != (setting2.getEnabledFlags() & 1)) {
                        throw new java.lang.IllegalArgumentException("A conflict of the DONT_KILL_APP flag between components in the package " + packageName2);
                    }
                } else {
                    if (checkDuplicatedPackage.contains(packageName2)) {
                        throw new java.lang.IllegalArgumentException("The package " + packageName2 + " is duplicated");
                    }
                    checkDuplicatedPackage.add(packageName2);
                }
            }
        }
        boolean allowedByPermission = this.mPackageManagerServiceExt.adjustPermissionStateCheckInSetEnabledSetting(this.mContext, this.mContext.checkCallingOrSelfPermission("android.permission.CHANGE_COMPONENT_ENABLED_STATE")) == 0;
        boolean[] updateAllowed2 = new boolean[targetSize2];
        java.util.Arrays.fill(updateAllowed2, true);
        java.util.Map<java.lang.String, com.android.server.pm.PackageSetting> pkgSettings = new android.util.ArrayMap<>(targetSize2);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                snapshot = snapshotComputer();
                i = 0;
            } catch (java.lang.Throwable th) {
                th = th;
            }
            while (i < targetSize2) {
                try {
                    setting = list.get(i);
                    packageName = setting.getPackageName();
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
                if (pkgSettings.containsKey(packageName)) {
                    snapshot2 = snapshot;
                    updateAllowed = updateAllowed2;
                } else {
                    boolean isCallerTargetApp = com.android.internal.util.ArrayUtils.contains(snapshot.getPackagesForUid(callingUid), packageName);
                    com.android.server.pm.PackageSetting pkgSetting = this.mSettings.getPackageLPr(packageName);
                    if (!isCallerTargetApp && !allowedByPermission) {
                        throw new java.lang.SecurityException("Attempt to change component state; pid=" + android.os.Binder.getCallingPid() + ", uid=" + callingUid + (!setting.isComponent() ? ", package=" + packageName : ", component=" + setting.getComponentName()));
                    }
                    updateAllowed = updateAllowed2;
                    if (pkgSetting != null) {
                        try {
                            if (!snapshot.shouldFilterApplicationIncludingUninstalled(pkgSetting, callingUid, i3)) {
                                if (!isCallerTargetApp && this.mProtectedPackages.isPackageStateProtected(i3, packageName)) {
                                    throw new java.lang.SecurityException("Cannot disable a protected package: " + packageName);
                                }
                                if (callingUid == 2000 && (pkgSetting.getFlags() & 256) == 0) {
                                    int oldState = pkgSetting.getEnabled(i3);
                                    int newState2 = setting.getEnabledState();
                                    if (setting.isComponent() || !((oldState == 3 || oldState == 0 || oldState == 1) && (newState2 == 3 || newState2 == 0 || newState2 == 1))) {
                                        throw new java.lang.SecurityException("Shell cannot change component state for " + setting.getComponentName() + " to " + newState2);
                                    }
                                    snapshot2 = snapshot;
                                } else {
                                    snapshot2 = snapshot;
                                }
                                pkgSettings.put(packageName, pkgSetting);
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    }
                    throw new java.lang.IllegalArgumentException(setting.isComponent() ? "Unknown component: " + setting.getComponentName() : "Unknown package: " + packageName);
                    while (true) {
                        try {
                            resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                        }
                    }
                }
                i++;
                list = settings;
                snapshot = snapshot2;
                updateAllowed2 = updateAllowed;
            }
            boolean[] updateAllowed3 = updateAllowed2;
            for (int i6 = 0; i6 < targetSize2; i6++) {
                android.content.pm.PackageManager.ComponentEnabledSetting setting3 = settings.get(i6);
                if (setting3.isComponent()) {
                    java.lang.String packageName3 = setting3.getPackageName();
                    java.lang.String className = setting3.getClassName();
                    if (!allowedByPermission && android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(className)) {
                        throw new java.lang.SecurityException("Cannot disable a system-generated component");
                    }
                    com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = pkgSettings.get(packageName3).getPkg();
                    if (pkg == null || !com.android.server.pm.parsing.pkg.AndroidPackageUtils.hasComponentClassName(pkg, className)) {
                        if (pkg != null && pkg.getTargetSdkVersion() >= 16) {
                            throw new java.lang.IllegalArgumentException("Component class " + className + " does not exist in " + packageName3);
                        }
                        android.util.Slog.w(TAG, "Failed setComponentEnabledSetting: component class " + className + " does not exist in " + packageName3);
                        updateAllowed3[i6] = false;
                    }
                }
            }
            try {
                resetPriorityAfterPackageManagerTracedLockedSection();
                for (int i7 = 0; i7 < targetSize2; i7++) {
                    android.content.pm.PackageManager.ComponentEnabledSetting setting4 = settings.get(i7);
                    if (!setting4.isComponent()) {
                        com.android.server.pm.PackageSetting pkgSetting2 = pkgSettings.get(setting4.getPackageName());
                        int newState3 = setting4.getEnabledState();
                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mLock;
                        boostPriorityForPackageManagerTracedLockedSection();
                        synchronized (packageManagerTracedLock3) {
                            try {
                                if (pkgSetting2.getEnabled(i3) == newState3) {
                                    updateAllowed3[i7] = false;
                                    resetPriorityAfterPackageManagerTracedLockedSection();
                                } else {
                                    resetPriorityAfterPackageManagerTracedLockedSection();
                                    com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg2 = pkgSetting2.getPkg();
                                    boolean isSystemStub = pkg2 != null && pkg2.isStub() && pkgSetting2.isSystem();
                                    if (isSystemStub && ((newState3 == 0 || newState3 == 1) && !enableCompressedPackage(pkg2, pkgSetting2))) {
                                        android.util.Slog.w(TAG, "Failed setApplicationEnabledSetting: failed to enable commpressed package " + setting4.getPackageName());
                                        updateAllowed3[i7] = false;
                                    }
                                }
                            } finally {
                                resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                    }
                }
                android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> sendNowBroadcasts = new android.util.ArrayMap<>(targetSize2);
                java.util.List<com.android.server.pm.PackageMetrics.ComponentStateMetrics> componentStateMetricsList = new java.util.ArrayList<>();
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock4 = this.mLock;
                boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock4) {
                    try {
                        com.android.server.pm.Computer computer2 = snapshotComputer();
                        boolean scheduleBroadcastMessage = false;
                        boolean isSynchronous = false;
                        boolean anyChanged = false;
                        int inputFlags4 = 0;
                        int inputFlags5 = 0;
                        while (inputFlags5 < targetSize2) {
                            try {
                                if (updateAllowed3[inputFlags5]) {
                                    android.content.pm.PackageManager.ComponentEnabledSetting setting5 = settings.get(inputFlags5);
                                    java.lang.String packageName4 = setting5.getPackageName();
                                    com.android.server.pm.PackageSetting packageSetting = pkgSettings.get(packageName4);
                                    i2 = inputFlags5;
                                    int i8 = packageSetting.getAppId();
                                    int uid = android.os.UserHandle.getUid(i3, i8);
                                    if (setting5.isComponent()) {
                                        inputFlags = inputFlags4;
                                        try {
                                            inputFlags2 = computer2.getComponentEnabledSettingInternal(setting5.getComponentName(), callingUid, i3);
                                        } catch (java.lang.Throwable th5) {
                                            th = th5;
                                            packageManagerTracedLock = packageManagerTracedLock4;
                                            while (true) {
                                                try {
                                                    resetPriorityAfterPackageManagerTracedLockedSection();
                                                    throw th;
                                                } catch (java.lang.Throwable th6) {
                                                    th = th6;
                                                }
                                            }
                                        }
                                    } else {
                                        inputFlags = inputFlags4;
                                        inputFlags2 = packageSetting.getEnabled(i3);
                                    }
                                    com.android.server.pm.PackageMetrics.ComponentStateMetrics componentStateMetrics = new com.android.server.pm.PackageMetrics.ComponentStateMetrics(setting5, uid, inputFlags2, callingUid);
                                    if (this.mPackageManagerServiceExt.interceptActionInSetEnabledSetting(callingUid, setting5.getEnabledState(), packageName4)) {
                                        computer = computer2;
                                        packageManagerTracedLock = packageManagerTracedLock4;
                                        inputFlags3 = inputFlags;
                                        targetSize = targetSize2;
                                    } else {
                                        com.android.server.pm.Computer computer3 = computer2;
                                        computer = computer2;
                                        inputFlags3 = inputFlags;
                                        targetSize = targetSize2;
                                        packageManagerTracedLock = packageManagerTracedLock4;
                                        try {
                                            if (setEnabledSettingInternalLocked(computer3, packageSetting, setting5, userId, callingPackage)) {
                                                componentStateMetricsList.add(componentStateMetrics);
                                                if ((setting5.getEnabledFlags() & 2) != 0) {
                                                    isSynchronous = true;
                                                }
                                                java.lang.String componentName2 = setting5.isComponent() ? setting5.getClassName() : packageName4;
                                                if ((setting5.getEnabledFlags() & 1) == 0) {
                                                    java.util.ArrayList<java.lang.String> componentList = sendNowBroadcasts.get(packageName4);
                                                    java.util.ArrayList<java.lang.String> componentList2 = componentList == null ? new java.util.ArrayList<>() : componentList;
                                                    if (!componentList2.contains(componentName2)) {
                                                        componentList2.add(componentName2);
                                                    }
                                                    sendNowBroadcasts.put(packageName4, componentList2);
                                                    this.mPendingBroadcasts.remove(i3, packageName4);
                                                    inputFlags4 = inputFlags3;
                                                    anyChanged = true;
                                                } else {
                                                    this.mPendingBroadcasts.addComponent(i3, packageName4, componentName2);
                                                    inputFlags4 = inputFlags3 | setting5.getEnabledFlags();
                                                    anyChanged = true;
                                                    scheduleBroadcastMessage = true;
                                                }
                                            }
                                            inputFlags5 = i2 + 1;
                                            targetSize2 = targetSize;
                                            computer2 = computer;
                                            packageManagerTracedLock4 = packageManagerTracedLock;
                                        } catch (java.lang.Throwable th7) {
                                            th = th7;
                                            while (true) {
                                                resetPriorityAfterPackageManagerTracedLockedSection();
                                                throw th;
                                            }
                                        }
                                    }
                                } else {
                                    computer = computer2;
                                    i2 = inputFlags5;
                                    inputFlags3 = inputFlags4;
                                    packageManagerTracedLock = packageManagerTracedLock4;
                                    targetSize = targetSize2;
                                }
                                inputFlags4 = inputFlags3;
                                inputFlags5 = i2 + 1;
                                targetSize2 = targetSize;
                                computer2 = computer;
                                packageManagerTracedLock4 = packageManagerTracedLock;
                            } catch (java.lang.Throwable th8) {
                                th = th8;
                                packageManagerTracedLock = packageManagerTracedLock4;
                            }
                        }
                        int inputFlags6 = inputFlags4;
                        packageManagerTracedLock = packageManagerTracedLock4;
                        int targetSize3 = targetSize2;
                        if (!anyChanged) {
                            return;
                        }
                        if (isSynchronous) {
                            flushPackageRestrictionsAsUserInternalLocked(i3);
                        } else {
                            try {
                                scheduleWritePackageRestrictions(i3);
                            } catch (java.lang.Throwable th9) {
                                th = th9;
                                while (true) {
                                    resetPriorityAfterPackageManagerTracedLockedSection();
                                    throw th;
                                }
                            }
                        }
                        if (scheduleBroadcastMessage && !this.mHandler.hasMessages(1)) {
                            long broadcastDelay = (android.os.SystemClock.uptimeMillis() <= this.mServiceStartWithDelay || this.mPackageManagerServiceExt.useLongBroadcastDelayInSetEnabledSetting(inputFlags6)) ? 10000L : 1000L;
                            this.mHandler.sendEmptyMessageDelayed(1, broadcastDelay);
                        }
                        resetPriorityAfterPackageManagerTracedLockedSection();
                        com.android.server.pm.PackageMetrics.reportComponentStateChanged(snapshotComputer(), componentStateMetricsList, i3);
                        long callingId = android.os.Binder.clearCallingIdentity();
                        try {
                            com.android.server.pm.Computer newSnapshot = snapshotComputer();
                            int i9 = 0;
                            while (i9 < sendNowBroadcasts.size()) {
                                try {
                                    java.lang.String packageName5 = sendNowBroadcasts.keyAt(i9);
                                    java.util.ArrayList<java.lang.String> components = sendNowBroadcasts.valueAt(i9);
                                    int packageUid = android.os.UserHandle.getUid(i3, pkgSettings.get(packageName5).getAppId());
                                    java.util.List<com.android.server.pm.PackageMetrics.ComponentStateMetrics> componentStateMetricsList2 = componentStateMetricsList;
                                    boolean[] updateAllowed4 = updateAllowed3;
                                    try {
                                        int targetSize4 = targetSize3;
                                        int callingUid2 = callingUid;
                                        android.util.ArrayMap<java.lang.String, java.util.ArrayList<java.lang.String>> sendNowBroadcasts2 = sendNowBroadcasts;
                                        try {
                                            this.mPackageManagerServiceExt.sendPackageChangedBroadcastInSetEnabledSetting(newSnapshot, settings, packageName5, components, packageUid, null, callingPid);
                                            i9++;
                                            i3 = userId;
                                            componentStateMetricsList = componentStateMetricsList2;
                                            targetSize3 = targetSize4;
                                            callingUid = callingUid2;
                                            sendNowBroadcasts = sendNowBroadcasts2;
                                            updateAllowed3 = updateAllowed4;
                                        } catch (java.lang.Throwable th10) {
                                            th = th10;
                                            android.os.Binder.restoreCallingIdentity(callingId);
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th11) {
                                        th = th11;
                                    }
                                } catch (java.lang.Throwable th12) {
                                    th = th12;
                                }
                            }
                            android.os.Binder.restoreCallingIdentity(callingId);
                        } catch (java.lang.Throwable th13) {
                            th = th13;
                        }
                    } catch (java.lang.Throwable th14) {
                        th = th14;
                        packageManagerTracedLock = packageManagerTracedLock4;
                    }
                }
            } catch (java.lang.Throwable th15) {
                th = th15;
            }
        }
    }

    private boolean setEnabledSettingInternalLocked(com.android.server.pm.Computer computer, com.android.server.pm.PackageSetting pkgSetting, android.content.pm.PackageManager.ComponentEnabledSetting setting, int userId, java.lang.String callingPackage) {
        int newState = setting.getEnabledState();
        java.lang.String packageName = setting.getPackageName();
        boolean success = false;
        if (!setting.isComponent()) {
            pkgSetting.setEnabled(newState, userId, callingPackage);
            if ((newState == 3 || newState == 2) && checkPermission("android.permission.SUSPEND_APPS", packageName, userId) == 0) {
                unsuspendForSuspendingPackage(computer, packageName, userId, true);
                removeAllDistractingPackageRestrictions(computer, userId);
            }
            success = true;
        } else {
            java.lang.String className = setting.getClassName();
            switch (newState) {
                case 0:
                    success = pkgSetting.restoreComponentLPw(className, userId);
                    break;
                case 1:
                    success = pkgSetting.enableComponentLPw(className, userId);
                    break;
                case 2:
                    success = pkgSetting.disableComponentLPw(className, userId);
                    break;
                default:
                    android.util.Slog.e(TAG, "Failed setComponentEnabledSetting: component " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + className + " requested an invalid new component state: " + newState);
                    break;
            }
        }
        if (!success) {
            return false;
        }
        updateSequenceNumberLP(pkgSetting, new int[]{userId});
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            updateInstantAppInstallerLocked(packageName);
            return true;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void flushPackageRestrictionsAsUserInternalLocked(int userId) {
        this.mSettings.writePackageRestrictionsLPr(userId);
        synchronized (this.mDirtyUsers) {
            this.mDirtyUsers.remove(java.lang.Integer.valueOf(userId));
            if (this.mDirtyUsers.isEmpty()) {
                this.mBackgroundHandler.removeMessages(14);
            }
        }
    }

    public void waitForAppDataPrepared() {
        if (this.mPrepareAppDataFuture == null) {
            return;
        }
        com.android.internal.util.ConcurrentUtils.waitForFutureNoInterrupt(this.mPrepareAppDataFuture, "wait for prepareAppData");
        this.mPrepareAppDataFuture = null;
    }

    public void systemReady() {
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRoot("Only the system can claim the system is ready");
        final android.content.ContentResolver resolver = this.mContext.getContentResolver();
        if (this.mReleaseOnSystemReady != null) {
            for (int i = this.mReleaseOnSystemReady.size() - 1; i >= 0; i--) {
                java.io.File dstCodePath = this.mReleaseOnSystemReady.get(i);
                com.android.internal.content.F2fsUtils.releaseCompressedBlocks(resolver, dstCodePath);
            }
            this.mReleaseOnSystemReady = null;
        }
        this.mSystemReady = true;
        android.database.ContentObserver co2 = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.pm.PackageManagerService.4
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                boolean ephemeralFeatureDisabled = android.provider.Settings.Global.getInt(resolver, "enable_ephemeral_feature", 1) == 0;
                for (int userId : com.android.server.pm.UserManagerService.getInstance().getUserIds()) {
                    boolean instantAppsDisabledForUser = ephemeralFeatureDisabled || android.provider.Settings.Secure.getIntForUser(resolver, "instant_apps_enabled", 1, userId) == 0;
                    com.android.server.pm.PackageManagerService.this.mWebInstantAppsDisabled.put(userId, instantAppsDisabledForUser);
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("enable_ephemeral_feature"), false, co2, -1);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("instant_apps_enabled"), false, co2, -1);
        co2.onChange(true);
        this.mAppsFilter.onSystemReady((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class));
        com.android.internal.telephony.CarrierAppUtils.disableCarrierAppsUntilPrivileged(this.mContext.getOpPackageName(), 0, this.mContext);
        disableSkuSpecificApps();
        boolean compatibilityModeEnabled = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "compatibility_mode", 1) == 1;
        com.android.internal.pm.pkg.parsing.ParsingPackageUtils.setCompatibilityModeEnabled(compatibilityModeEnabled);
        if (DEBUG_SETTINGS) {
            android.util.Log.d(TAG, "compatibility mode:" + compatibilityModeEnabled);
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                java.util.ArrayList<java.lang.Integer> changed = this.mSettings.systemReady(this.mComponentResolver);
                for (int i2 = 0; i2 < changed.size(); i2++) {
                    this.mSettings.writePackageRestrictionsLPr(changed.get(i2).intValue());
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
        this.mUserManager.systemReady();
        this.mPackageManagerServiceExt.afterUserManagerSystemReady();
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mInjector.getSystemService(android.os.storage.StorageManager.class);
        storage.registerListener(this.mStorageEventHelper);
        this.mInstallerService.systemReady();
        this.mPackageDexOptimizer.systemReady();
        this.mUserManager.reconcileUsers(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL);
        this.mStorageEventHelper.reconcileApps(snapshotComputer(), android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL);
        this.mPermissionManager.onSystemReady();
        this.mPackageManagerServiceExt.afterPermissionManagerSystemReady();
        int[] grantPermissionsUserIds = EMPTY_INT_ARRAY;
        java.util.List<android.content.pm.UserInfo> livingUsers = this.mInjector.getUserManagerInternal().getUsers(true, true, false);
        int livingUserCount = livingUsers.size();
        for (int i3 = 0; i3 < livingUserCount; i3++) {
            int userId = livingUsers.get(i3).id;
            boolean isPermissionUpgradeNeeded = !java.util.Objects.equals(this.mPermissionManager.getDefaultPermissionGrantFingerprint(userId), android.os.Build.FINGERPRINT);
            if (isPermissionUpgradeNeeded) {
                grantPermissionsUserIds = com.android.internal.util.ArrayUtils.appendInt(grantPermissionsUserIds, userId);
            }
        }
        for (int userId2 : grantPermissionsUserIds) {
            this.mLegacyPermissionManager.grantDefaultPermissions(userId2);
            this.mPermissionManager.setDefaultPermissionGrantFingerprint(android.os.Build.FINGERPRINT, userId2);
        }
        if (grantPermissionsUserIds == EMPTY_INT_ARRAY) {
            this.mLegacyPermissionManager.scheduleReadDefaultPermissionExceptions();
        }
        if (this.mInstantAppResolverConnection != null) {
            this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.pm.PackageManagerService.5
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    com.android.server.pm.PackageManagerService.this.mInstantAppResolverConnection.optimisticBind();
                    com.android.server.pm.PackageManagerService.this.mContext.unregisterReceiver(this);
                }
            }, new android.content.IntentFilter("android.intent.action.BOOT_COMPLETED"));
        }
        android.content.IntentFilter overlayFilter = new android.content.IntentFilter("android.intent.action.OVERLAY_CHANGED");
        overlayFilter.addDataScheme("package");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.pm.PackageManagerService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                android.net.Uri data;
                java.lang.String packageName;
                com.android.server.pm.Computer snapshot;
                com.android.server.pm.pkg.AndroidPackage pkg;
                if (intent == null || (data = intent.getData()) == null || (packageName = data.getSchemeSpecificPart()) == null || (pkg = (snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer()).getPackage(packageName)) == null) {
                    return;
                }
                com.android.server.pm.PackageManagerService.this.mBroadcastHelper.sendPackageChangedBroadcast(snapshot, pkg.getPackageName(), true, new java.util.ArrayList<>(java.util.Collections.singletonList(pkg.getPackageName())), pkg.getUid(), "android.intent.action.OVERLAY_CHANGED");
            }
        }, overlayFilter);
        this.mModuleInfoProvider.systemReady();
        this.mPackageManagerServiceSocExt.createBoostFrameworkOnSystemReady();
        this.mPackageManagerServiceSocExt.registerHbtRusOnSystemReady();
        this.mInstallerService.restoreAndApplyStagedSessionIfNeeded();
        this.mExistingPackages = null;
        android.provider.DeviceConfig.addOnPropertiesChangedListener("package_manager_service", this.mInjector.getBackgroundExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda11
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$systemReady$50(properties);
            }
        });
        schedulePruneUnusedStaticSharedLibraries(false);
        this.mPackageManagerServiceExt.afterPackageManagerSystemReady(this.mIsPreQUpgrade);
        com.android.server.art.DexUseManagerLocal dexUseManager = com.android.server.pm.DexOptHelper.getDexUseManagerLocal();
        if (dexUseManager != null) {
            dexUseManager.systemReady();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$50(android.provider.DeviceConfig.Properties properties) {
        java.util.Set<java.lang.String> keyset = properties.getKeyset();
        if (keyset.contains(PROPERTY_INCFS_DEFAULT_TIMEOUTS) || keyset.contains(PROPERTY_KNOWN_DIGESTERS_LIST)) {
            this.mPerUidReadTimeoutsCache = null;
        }
    }

    private void disableSkuSpecificApps() {
        java.lang.String[] apkList = this.mContext.getResources().getStringArray(android.R.array.config_deviceStatesToReverseDefaultDisplayRotationAroundZAxis);
        java.lang.String[] skuArray = this.mContext.getResources().getStringArray(android.R.array.config_deviceStatesOnWhichToWakeUp);
        if (com.android.internal.util.ArrayUtils.isEmpty(apkList)) {
            return;
        }
        java.lang.String sku = android.os.SystemProperties.get("ro.boot.hardware.sku");
        if (!android.text.TextUtils.isEmpty(sku) && com.android.internal.util.ArrayUtils.contains(skuArray, sku)) {
            return;
        }
        com.android.server.pm.Computer snapshot = snapshotComputer();
        for (java.lang.String packageName : apkList) {
            setSystemAppHiddenUntilInstalled(snapshot, packageName, true);
            java.util.List<android.content.pm.UserInfo> users = this.mInjector.getUserManagerInternal().getUsers(false);
            for (int i = 0; i < users.size(); i++) {
                setSystemAppInstallState(snapshot, packageName, false, users.get(i).id);
            }
        }
    }

    public com.android.server.pm.PackageFreezer freezePackage(java.lang.String packageName, int userId, java.lang.String killReason, int exitInfoReason, com.android.server.pm.InstallRequest request) {
        return freezePackage(packageName, userId, killReason, exitInfoReason, request, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.pm.PackageFreezer freezePackage(java.lang.String packageName, int userId, java.lang.String killReason, int exitInfoReason, com.android.server.pm.InstallRequest request, boolean waitAppKilled) {
        return new com.android.server.pm.PackageFreezer(packageName, userId, killReason, this, exitInfoReason, request, waitAppKilled);
    }

    public com.android.server.pm.PackageFreezer freezePackageForDelete(java.lang.String packageName, int userId, int deleteFlags, java.lang.String killReason, int exitInfoReason) {
        if ((deleteFlags & 8) != 0) {
            return new com.android.server.pm.PackageFreezer(this, null);
        }
        return freezePackage(packageName, userId, killReason, exitInfoReason, null);
    }

    void cleanUpUser(com.android.server.pm.UserManagerService userManager, int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                synchronized (this.mDirtyUsers) {
                    this.mDirtyUsers.remove(java.lang.Integer.valueOf(userId));
                }
                this.mUserNeedsBadging.delete(userId);
                this.mDeletePackageHelper.removeUnusedPackagesLPw(userManager, userId);
                this.mSettings.removeUserLPw(userId);
                this.mPendingBroadcasts.remove(userId);
                this.mAppsFilter.onUserDeleted(snapshotComputer(), userId);
                this.mPermissionManager.onUserRemoved(userId);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
        this.mInstantAppRegistry.onUserRemoved(userId);
        this.mPackageMonitorCallbackHelper.onUserRemoved(userId);
        if (android.app.admin.flags.Flags.crossUserSuspensionEnabledRo()) {
            cleanUpCrossUserSuspension(userId);
        }
    }

    private void cleanUpCrossUserSuspension(final int removedUser) {
        com.android.server.pm.Computer computer = snapshotComputer();
        java.lang.String[] allPackages = computer.getAllAvailablePackageNames();
        for (int targetUserId : this.mUserManager.getUserIds()) {
            if (targetUserId != removedUser) {
                this.mSuspendPackageHelper.removeSuspensionsBySuspendingPackage(computer, allPackages, new java.util.function.Predicate() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda3
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.pm.PackageManagerService.lambda$cleanUpCrossUserSuspension$51(removedUser, (android.content.pm.UserPackage) obj);
                    }
                }, targetUserId);
            }
        }
    }

    static /* synthetic */ boolean lambda$cleanUpCrossUserSuspension$51(int removedUser, android.content.pm.UserPackage userPackage) {
        return userPackage.userId == removedUser;
    }

    void createNewUser(int userId, java.util.Set<java.lang.String> userTypeInstallablePackages, java.lang.String[] disallowedPackages) {
        this.mPackageManagerServiceExt.beforeCreateNewUser(userId);
        com.android.server.pm.PackageManagerTracedLock installLock = this.mInstallLock.acquireLock();
        try {
            this.mSettings.createNewUserLI(this, this.mInstaller, userId, userTypeInstallablePackages, disallowedPackages);
            if (installLock != null) {
                installLock.close();
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    scheduleWritePackageRestrictions(userId);
                    scheduleWritePackageListLocked(userId);
                    this.mAppsFilter.onUserCreated(snapshotComputer(), userId);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterPackageManagerTracedLockedSection();
        } catch (java.lang.Throwable th2) {
            if (installLock != null) {
                try {
                    installLock.close();
                } catch (java.lang.Throwable th3) {
                    th2.addSuppressed(th3);
                }
            }
            throw th2;
        }
    }

    void onNewUserCreated(int userId, boolean convertedFromPreCreated) {
        if (DEBUG_PERMISSIONS) {
            android.util.Slog.d(TAG, "onNewUserCreated(id=" + userId + ", convertedFromPreCreated=" + convertedFromPreCreated + ")");
        }
        if (!convertedFromPreCreated || !readPermissionStateForUser(userId)) {
            this.mPermissionManager.onUserCreated(userId);
            this.mLegacyPermissionManager.grantDefaultPermissions(userId);
            this.mPermissionManager.setDefaultPermissionGrantFingerprint(android.os.Build.FINGERPRINT, userId);
            this.mDomainVerificationManager.clearUser(userId);
        }
        this.mPackageManagerServiceExt.handleNewUserInONUC(userId);
    }

    private boolean readPermissionStateForUser(int userId) {
        boolean isPermissionUpgradeNeeded;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPermissionManager.writeLegacyPermissionStateTEMP();
                this.mSettings.readPermissionStateForUserSyncLPr(userId);
                this.mPermissionManager.readLegacyPermissionStateTEMP();
                isPermissionUpgradeNeeded = !java.util.Objects.equals(this.mPermissionManager.getDefaultPermissionGrantFingerprint(userId), android.os.Build.FINGERPRINT);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
        return isPermissionUpgradeNeeded;
    }

    public boolean isStorageLow() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.storage.DeviceStorageMonitorInternal dsm = (com.android.server.storage.DeviceStorageMonitorInternal) this.mInjector.getLocalService(com.android.server.storage.DeviceStorageMonitorInternal.class);
            if (dsm != null) {
                return dsm.isMemoryLow();
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void deletePackageIfUnused(com.android.server.pm.Computer snapshot, final java.lang.String packageName) {
        com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateInternal(packageName);
        if (ps == null) {
            return;
        }
        android.util.SparseArray<? extends com.android.server.pm.pkg.PackageUserStateInternal> userStates = ps.getUserStates();
        for (int index = 0; index < userStates.size(); index++) {
            if (userStates.valueAt(index).isInstalled()) {
                return;
            }
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$deletePackageIfUnused$52(packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deletePackageIfUnused$52(java.lang.String packageName) {
        this.mDeletePackageHelper.deletePackageX(packageName, -1L, 0, 2, true);
    }

    void deletePreloadsFileCache() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.CLEAR_APP_CACHE", "deletePreloadsFileCache");
        java.io.File dir = android.os.Environment.getDataPreloadsFileCacheDirectory();
        android.util.Slog.i(TAG, "Deleting preloaded file cache " + dir);
        android.os.FileUtils.deleteContents(dir);
    }

    void setSystemAppHiddenUntilInstalled(com.android.server.pm.Computer snapshot, final java.lang.String packageName, final boolean hidden) {
        int callingUid = android.os.Binder.getCallingUid();
        boolean calledFromSystemOrPhone = callingUid == 1001 || callingUid == 1000;
        if (!calledFromSystemOrPhone) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.SUSPEND_APPS", "setSystemAppHiddenUntilInstalled");
        }
        com.android.server.pm.pkg.PackageStateInternal stateRead = snapshot.getPackageStateInternal(packageName);
        if (stateRead == null || !stateRead.isSystem() || stateRead.getPkg() == null) {
            return;
        }
        if (stateRead.getPkg().isCoreApp() && !calledFromSystemOrPhone) {
            throw new java.lang.SecurityException("Only system or phone callers can modify core apps");
        }
        commitPackageStateMutation(null, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda65
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.PackageManagerService.lambda$setSystemAppHiddenUntilInstalled$53(packageName, hidden, (com.android.server.pm.pkg.mutate.PackageStateMutator) obj);
            }
        });
    }

    static /* synthetic */ void lambda$setSystemAppHiddenUntilInstalled$53(java.lang.String packageName, boolean hidden, com.android.server.pm.pkg.mutate.PackageStateMutator mutator) {
        mutator.forPackage(packageName).setHiddenUntilInstalled(hidden);
        mutator.forDisabledSystemPackage(packageName).setHiddenUntilInstalled(hidden);
    }

    boolean setSystemAppInstallState(com.android.server.pm.Computer snapshot, java.lang.String packageName, boolean installed, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        boolean calledFromSystemOrPhone = callingUid == 1001 || callingUid == 1000;
        if (!calledFromSystemOrPhone) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.SUSPEND_APPS", "setSystemAppHiddenUntilInstalled");
        }
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
        if (packageState == null || !packageState.isSystem() || packageState.getPkg() == null) {
            return false;
        }
        if (packageState.getPkg().isCoreApp() && !calledFromSystemOrPhone) {
            throw new java.lang.SecurityException("Only system or phone callers can modify core apps");
        }
        if (packageState.getUserStateOrDefault(userId).isInstalled() == installed) {
            return false;
        }
        if (2000 == callingUid && !installed) {
            deletePackageVersioned(new android.content.pm.VersionedPackage(packageName, -1), new android.content.pm.PackageManager.LegacyPackageDeleteObserver((android.content.pm.IPackageDeleteObserver) null).getBinder(), userId, 4);
            return true;
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            if (installed) {
                this.mInstallPackageHelper.installExistingPackageAsUser(packageName, userId, 4194304, 3, null, null);
                return true;
            }
            deletePackageVersioned(new android.content.pm.VersionedPackage(packageName, -1), new android.content.pm.PackageManager.LegacyPackageDeleteObserver((android.content.pm.IPackageDeleteObserver) null).getBinder(), userId, 4);
            return true;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    void finishPackageInstall(int i, boolean z) {
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRoot("Only the system is allowed to finish installs");
        if (DEBUG_INSTALL) {
            android.util.Slog.v(TAG, "BM finishing package install for " + i);
        }
        android.os.Trace.asyncTraceEnd(262144L, "restore", i);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9, i, z ? 1 : 0));
    }

    void checkPackageStartable(com.android.server.pm.Computer snapshot, java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (snapshot.getInstantAppPackageName(callingUid) != null) {
            throw new java.lang.SecurityException("Instant applications don't have access to this method");
        }
        if (!this.mUserManager.exists(userId)) {
            throw new java.lang.SecurityException("User doesn't exist");
        }
        snapshot.enforceCrossUserPermission(callingUid, userId, false, false, "checkPackageStartable");
        switch (snapshot.getPackageStartability(this.mSafeMode, packageName, callingUid, userId)) {
            case 1:
                throw new java.lang.SecurityException("Package " + packageName + " was not found!");
            case 2:
                throw new java.lang.SecurityException("Package " + packageName + " not a system app!");
            case 3:
                throw new java.lang.SecurityException("Package " + packageName + " is currently frozen!");
            case 4:
                throw new java.lang.SecurityException("Package " + packageName + " is not encryption aware!");
            default:
                return;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void setPackageStoppedState(com.android.server.pm.Computer r23, final java.lang.String r24, final boolean r25, final int r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 322
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageManagerService.setPackageStoppedState(com.android.server.pm.Computer, java.lang.String, boolean, int):void");
    }

    static /* synthetic */ void lambda$setPackageStoppedState$54(int userId, boolean stopped, boolean wasNotLaunched, com.android.server.pm.pkg.mutate.PackageStateWrite state) {
        com.android.server.pm.pkg.mutate.PackageUserStateWrite userState = state.userState(userId);
        userState.setStopped(stopped);
        if (wasNotLaunched) {
            userState.setNotLaunched(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setPackageStoppedState$55(java.lang.String packageName, int userId) {
        com.android.server.apphibernation.AppHibernationManagerInternal ah = (com.android.server.apphibernation.AppHibernationManagerInternal) this.mInjector.getLocalService(com.android.server.apphibernation.AppHibernationManagerInternal.class);
        if (ah != null && ah.isHibernatingForUser(packageName, userId)) {
            ah.setHibernatingForUser(packageName, userId, false);
            ah.setHibernatingGlobally(packageName, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setPackageStoppedState$56(java.lang.String packageName, android.os.Bundle extras, int[] userIds, android.util.SparseArray broadcastAllowList) {
        this.mBroadcastHelper.sendPackageBroadcast("android.intent.action.PACKAGE_UNSTOPPED", packageName, extras, 1073741824, null, null, userIds, null, broadcastAllowList, null, null);
    }

    void notifyComponentUsed(com.android.server.pm.Computer snapshot, java.lang.String packageName, int userId, java.lang.String recentCallingPackage, java.lang.String debugInfo) throws java.lang.Throwable {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.PackageSetting pkgSetting = this.mSettings.getPackageLPr(packageName);
                if (pkgSetting == null) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                if (pkgSetting.getUserStateOrDefault(userId).isQuarantined()) {
                    android.util.Slog.i(TAG, "Component is quarantined+suspended but being used: " + packageName + " by " + recentCallingPackage + ", debugInfo: " + debugInfo);
                }
                resetPriorityAfterPackageManagerTracedLockedSection();
                setPackageStoppedState(snapshot, packageName, false, userId);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    public class IPackageManagerImpl extends com.android.server.pm.IPackageManagerBase {
        public IPackageManagerImpl() {
            super(com.android.server.pm.PackageManagerService.this, com.android.server.pm.PackageManagerService.this.mContext, com.android.server.pm.PackageManagerService.this.mDexOptHelper, com.android.server.pm.PackageManagerService.this.mModuleInfoProvider, com.android.server.pm.PackageManagerService.this.mPreferredActivityHelper, com.android.server.pm.PackageManagerService.this.mResolveIntentHelper, com.android.server.pm.PackageManagerService.this.mDomainVerificationManager, com.android.server.pm.PackageManagerService.this.mDomainVerificationConnection, com.android.server.pm.PackageManagerService.this.mInstallerService, com.android.server.pm.PackageManagerService.this.mPackageProperty, com.android.server.pm.PackageManagerService.this.mResolveComponentName, com.android.server.pm.PackageManagerService.this.mInstantAppResolverSettingsComponent, com.android.server.pm.PackageManagerService.this.mServicesExtensionPackageName, com.android.server.pm.PackageManagerService.this.mSharedSystemSharedLibraryPackageName);
        }

        public void checkPackageStartable(java.lang.String packageName, int userId) {
            com.android.server.pm.PackageManagerService.this.checkPackageStartable(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageName, userId);
        }

        public void clearApplicationProfileData(java.lang.String packageName) {
            com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRootOrShell("Only the system or shell can clear all profile data");
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.AndroidPackage pkg = snapshot.getPackage(packageName);
            com.android.server.pm.PackageFreezer ignored = com.android.server.pm.PackageManagerService.this.freezePackage(packageName, -1, "clearApplicationProfileData", 13, null);
            try {
                com.android.server.pm.PackageManagerTracedLock installLock = com.android.server.pm.PackageManagerService.this.mInstallLock.acquireLock();
                try {
                    com.android.server.pm.PackageManagerService.this.mAppDataHelper.clearAppProfilesLIF(pkg);
                    if (installLock != null) {
                        installLock.close();
                    }
                    if (ignored != null) {
                        ignored.close();
                    }
                } finally {
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void clearApplicationUserData(final java.lang.String packageName, final android.content.pm.IPackageDataObserver observer, final int userId) {
            clearApplicationUserData_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, userId, true, false, "clear application data");
            if (snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId) == null) {
                if (observer != null) {
                    com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda21
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.pm.PackageManagerService.IPackageManagerImpl.lambda$clearApplicationUserData$0(observer, packageName);
                        }
                    });
                }
            } else {
                if (com.android.server.pm.PackageManagerService.this.mProtectedPackages.isPackageDataProtected(userId, packageName)) {
                    throw new java.lang.SecurityException("Cannot clear data for a protected package: " + packageName);
                }
                int callingPid = android.os.Binder.getCallingPid();
                android.util.EventLog.writeEvent(3132, java.lang.Integer.valueOf(callingPid), java.lang.Integer.valueOf(callingUid), packageName);
                com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService.IPackageManagerImpl.1
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.pm.PackageManagerService.this.mHandler.removeCallbacks(this);
                        com.android.server.pm.PackageFreezer freezer = com.android.server.pm.PackageManagerService.this.freezePackage(packageName, -1, "clearApplicationUserData", 10, null, true);
                        try {
                            com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.beforeclearApplicationUserData(packageName);
                            com.android.server.pm.PackageManagerTracedLock installLock = com.android.server.pm.PackageManagerService.this.mInstallLock.acquireLock();
                            try {
                                boolean succeeded = com.android.server.pm.PackageManagerService.this.clearApplicationUserDataLIF(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageName, userId);
                                if (installLock != null) {
                                    installLock.close();
                                }
                                com.android.server.pm.PackageManagerService.this.mInstantAppRegistry.deleteInstantApplicationMetadata(packageName, userId);
                                if (succeeded) {
                                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
                                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                                    synchronized (packageManagerTracedLock) {
                                        try {
                                            com.android.server.pm.PackageManagerService.this.resetComponentEnabledSettingsIfNeededLPw(packageName, userId);
                                        } finally {
                                        }
                                    }
                                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                }
                                if (freezer != null) {
                                    freezer.close();
                                }
                                if (succeeded) {
                                    com.android.server.storage.DeviceStorageMonitorInternal dsm = (com.android.server.storage.DeviceStorageMonitorInternal) com.android.server.LocalServices.getService(com.android.server.storage.DeviceStorageMonitorInternal.class);
                                    if (dsm != null) {
                                        dsm.checkMemory();
                                    }
                                    if (com.android.server.pm.PackageManagerService.IPackageManagerImpl.this.checkPermission("android.permission.SUSPEND_APPS", packageName, userId) == 0) {
                                        com.android.server.pm.Computer snapshot2 = com.android.server.pm.PackageManagerService.this.snapshotComputer();
                                        com.android.server.pm.PackageManagerService.this.unsuspendForSuspendingPackage(snapshot2, packageName, userId, true);
                                        com.android.server.pm.PackageManagerService.this.removeAllDistractingPackageRestrictions(snapshot2, userId);
                                        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = com.android.server.pm.PackageManagerService.this.mLock;
                                        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                                        synchronized (packageManagerTracedLock2) {
                                            try {
                                                com.android.server.pm.PackageManagerService.this.flushPackageRestrictionsAsUserInternalLocked(userId);
                                            } finally {
                                            }
                                        }
                                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                    }
                                }
                                if (observer != null) {
                                    try {
                                        observer.onRemoveCompleted(packageName, succeeded);
                                    } catch (android.os.RemoteException e) {
                                        android.util.Log.i(com.android.server.pm.PackageManagerService.TAG, "Observer no longer exists.");
                                    }
                                }
                            } finally {
                            }
                        } catch (java.lang.Throwable th) {
                            if (freezer != null) {
                                try {
                                    freezer.close();
                                } catch (java.lang.Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    }
                });
            }
        }

        static /* synthetic */ void lambda$clearApplicationUserData$0(android.content.pm.IPackageDataObserver observer, java.lang.String packageName) {
            try {
                observer.onRemoveCompleted(packageName, false);
            } catch (android.os.RemoteException e) {
                android.util.Log.i(com.android.server.pm.PackageManagerService.TAG, "Observer no longer exists.");
            }
        }

        public void clearCrossProfileIntentFilters(int sourceUserId, java.lang.String ownerPackage) {
            clearCrossProfileIntentFilters_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.PackageManagerService.this.enforceOwnerRights(snapshot, ownerPackage, callingUid);
            com.android.server.pm.PackageManagerServiceUtils.enforceShellRestriction(com.android.server.pm.PackageManagerService.this.mInjector.getUserManagerInternal(), "no_debugging_features", callingUid, sourceUserId);
            com.android.server.pm.PackageManagerService.this.mInjector.getCrossProfileIntentFilterHelper().clearCrossProfileIntentFilters(sourceUserId, ownerPackage, null);
            com.android.server.pm.PackageManagerService.this.scheduleWritePackageRestrictions(sourceUserId);
        }

        public boolean removeCrossProfileIntentFilter(android.content.IntentFilter intentFilter, java.lang.String ownerPackage, int sourceUserId, int targetUserId, int flags) {
            removeCrossProfileIntentFilter_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.PackageManagerService.this.enforceOwnerRights(com.android.server.pm.PackageManagerService.this.snapshotComputer(), ownerPackage, callingUid);
            com.android.server.pm.PackageManagerService.this.mUserManager.enforceCrossProfileIntentFilterAccess(sourceUserId, targetUserId, callingUid, false);
            com.android.server.pm.PackageManagerServiceUtils.enforceShellRestriction(com.android.server.pm.PackageManagerService.this.mInjector.getUserManagerInternal(), "no_debugging_features", callingUid, sourceUserId);
            boolean removedMatchingFilter = false;
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.CrossProfileIntentResolver resolver = com.android.server.pm.PackageManagerService.this.mSettings.editCrossProfileIntentResolverLPw(sourceUserId);
                    android.util.ArraySet<com.android.server.pm.CrossProfileIntentFilter> set = new android.util.ArraySet<>((java.util.Collection<? extends com.android.server.pm.CrossProfileIntentFilter>) resolver.filterSet());
                    int i = 0;
                    while (true) {
                        if (i >= set.size()) {
                            break;
                        }
                        com.android.server.pm.CrossProfileIntentFilter filter = set.valueAt(i);
                        if (!android.content.IntentFilter.filterEquals(filter.mFilter, intentFilter) || !filter.getOwnerPackage().equals(ownerPackage) || filter.getTargetUserId() != targetUserId || filter.getFlags() != flags) {
                            i++;
                        } else {
                            resolver.removeFilter(filter);
                            removedMatchingFilter = true;
                            break;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (removedMatchingFilter) {
                com.android.server.pm.PackageManagerService.this.scheduleWritePackageRestrictions(sourceUserId);
            }
            return removedMatchingFilter;
        }

        public final void deleteApplicationCacheFiles(java.lang.String packageName, android.content.pm.IPackageDataObserver observer) {
            int userId = android.os.UserHandle.getCallingUserId();
            deleteApplicationCacheFilesAsUser(packageName, userId, observer);
        }

        public void deleteApplicationCacheFilesAsUser(final java.lang.String packageName, final int userId, final android.content.pm.IPackageDataObserver observer) {
            final int callingUid = android.os.Binder.getCallingUid();
            if (com.android.server.pm.PackageManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_DELETE_CACHE_FILES") != 0) {
                if (com.android.server.pm.PackageManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DELETE_CACHE_FILES") == 0) {
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Calling uid " + callingUid + " does not have android.permission.INTERNAL_DELETE_CACHE_FILES, silently ignoring");
                    return;
                }
                com.android.server.pm.PackageManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERNAL_DELETE_CACHE_FILES", null);
            }
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, userId, true, false, "delete application cache files");
            final int hasAccessInstantApps = com.android.server.pm.PackageManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_INSTANT_APPS");
            int callingPid = android.os.Binder.getCallingPid();
            android.util.EventLog.writeEvent(3132, java.lang.Integer.valueOf(callingPid), java.lang.Integer.valueOf(callingUid), packageName);
            com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$deleteApplicationCacheFilesAsUser$1(packageName, callingUid, hasAccessInstantApps, userId, observer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$deleteApplicationCacheFilesAsUser$1(java.lang.String packageName, int callingUid, int hasAccessInstantApps, int userId, android.content.pm.IPackageDataObserver observer) {
            com.android.server.pm.Computer newSnapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal ps = newSnapshot.getPackageStateInternal(packageName);
            boolean doClearData = true;
            if (ps != null) {
                boolean targetIsInstantApp = ps.getUserStateOrDefault(android.os.UserHandle.getUserId(callingUid)).isInstantApp();
                doClearData = !targetIsInstantApp || hasAccessInstantApps == 0;
            }
            if (doClearData) {
                com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.beforeDeleteApplicationCacheFiles();
                com.android.server.pm.PackageManagerTracedLock installLock = com.android.server.pm.PackageManagerService.this.mInstallLock.acquireLock();
                try {
                    com.android.server.pm.pkg.AndroidPackage pkg = com.android.server.pm.PackageManagerService.this.snapshotComputer().getPackage(packageName);
                    com.android.server.pm.PackageManagerService.this.mAppDataHelper.clearAppDataLIF(pkg, userId, 23);
                    com.android.server.pm.PackageManagerService.this.mAppDataHelper.clearAppDataLIF(pkg, userId, 39);
                    if (installLock != null) {
                        installLock.close();
                    }
                } catch (java.lang.Throwable th) {
                    if (installLock != null) {
                        try {
                            installLock.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            if (observer != null) {
                try {
                    observer.onRemoveCompleted(packageName, true);
                } catch (android.os.RemoteException e) {
                    android.util.Log.i(com.android.server.pm.PackageManagerService.TAG, "Observer no longer exists.");
                }
            }
        }

        public void enterSafeMode() {
            com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRoot("Only the system can request entering safe mode");
            if (!com.android.server.pm.PackageManagerService.this.mSystemReady) {
                com.android.server.pm.PackageManagerService.this.mSafeMode = true;
            }
        }

        public void extendVerificationTimeout(final int verificationId, final int verificationCodeAtTimeout, final long millisecondsToDelay) {
            if (verificationId >= 0) {
                com.android.server.pm.PackageManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.PACKAGE_VERIFICATION_AGENT", "Only package verification agents can extend verification timeouts");
            }
            final int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$extendVerificationTimeout$2(verificationId, callingUid, verificationCodeAtTimeout, millisecondsToDelay);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$extendVerificationTimeout$2(int verificationId, int callingUid, int verificationCodeAtTimeout, long millisecondsToDelay) {
            int id = verificationId >= 0 ? verificationId : -verificationId;
            com.android.server.pm.PackageVerificationState state = com.android.server.pm.PackageManagerService.this.mPendingVerification.get(id);
            if (state == null || !state.extendTimeout(callingUid)) {
                return;
            }
            com.android.server.pm.PackageVerificationResponse response = new com.android.server.pm.PackageVerificationResponse(verificationCodeAtTimeout, callingUid);
            long delay = millisecondsToDelay;
            if (delay > 3600000) {
                delay = 3600000;
            }
            if (delay < 0) {
                delay = 0;
            }
            android.os.Message msg = com.android.server.pm.PackageManagerService.this.mHandler.obtainMessage(15);
            msg.arg1 = id;
            msg.obj = response;
            com.android.server.pm.PackageManagerService.this.mHandler.sendMessageDelayed(msg, delay);
        }

        public void flushPackageRestrictionsAsUser(int userId) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            int callingUid = android.os.Binder.getCallingUid();
            if (snapshot.getInstantAppPackageName(callingUid) != null || !com.android.server.pm.PackageManagerService.this.mUserManager.exists(userId)) {
                return;
            }
            snapshot.enforceCrossUserPermission(callingUid, userId, false, false, "flushPackageRestrictions");
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.PackageManagerService.this.flushPackageRestrictionsAsUserInternalLocked(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        public void freeStorage(final java.lang.String volumeUuid, final long freeStorageSize, final int flags, final android.content.IntentSender pi) {
            freeStorage_enforcePermission();
            com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$freeStorage$3(volumeUuid, freeStorageSize, flags, pi);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$freeStorage$3(java.lang.String volumeUuid, long freeStorageSize, int flags, android.content.IntentSender pi) {
            boolean success = false;
            try {
            } catch (java.io.IOException e) {
                e = e;
            }
            try {
                com.android.server.pm.PackageManagerService.this.freeStorage(volumeUuid, freeStorageSize, flags);
                success = true;
            } catch (java.io.IOException e2) {
                e = e2;
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, e);
            }
            if (pi != null) {
                try {
                    android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
                    options.setPendingIntentBackgroundActivityLaunchAllowed(false);
                    pi.sendIntent(null, success ? 1 : 0, null, null, null, null, options.toBundle());
                } catch (android.content.IntentSender.SendIntentException e3) {
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, e3);
                }
            }
        }

        public void freeStorageAndNotify(final java.lang.String volumeUuid, final long freeStorageSize, final int flags, final android.content.pm.IPackageDataObserver observer) {
            freeStorageAndNotify_enforcePermission();
            com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$freeStorageAndNotify$4(volumeUuid, freeStorageSize, flags, observer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$freeStorageAndNotify$4(java.lang.String volumeUuid, long freeStorageSize, int flags, android.content.pm.IPackageDataObserver observer) {
            boolean success = false;
            try {
                com.android.server.pm.PackageManagerService.this.freeStorage(volumeUuid, freeStorageSize, flags);
                success = true;
            } catch (java.io.IOException e) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, e);
            }
            if (observer != null) {
                try {
                    observer.onRemoveCompleted((java.lang.String) null, success);
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, e2);
                }
            }
        }

        public android.content.pm.ChangedPackages getChangedPackages(int sequenceNumber, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            if (snapshot.getInstantAppPackageName(callingUid) != null || !com.android.server.pm.PackageManagerService.this.mUserManager.exists(userId)) {
                return null;
            }
            snapshot.enforceCrossUserPermission(callingUid, userId, false, false, "getChangedPackages");
            android.content.pm.ChangedPackages changedPackages = com.android.server.pm.PackageManagerService.this.mChangedPackagesTracker.getChangedPackages(sequenceNumber, userId);
            if (changedPackages != null) {
                java.util.List<java.lang.String> packageNames = changedPackages.getPackageNames();
                for (int index = packageNames.size() - 1; index >= 0; index--) {
                    com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageNames.get(index));
                    if (snapshot.shouldFilterApplication(packageState, callingUid, userId)) {
                        packageNames.remove(index);
                    }
                }
            }
            return changedPackages;
        }

        public byte[] getDomainVerificationBackup(int userId) {
            if (android.os.Binder.getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Only the system may call getDomainVerificationBackup()");
            }
            try {
                java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
                try {
                    com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(output);
                    com.android.server.pm.PackageManagerService.this.mDomainVerificationManager.writeSettings(com.android.server.pm.PackageManagerService.this.snapshotComputer(), serializer, true, userId);
                    byte[] byteArray = output.toByteArray();
                    output.close();
                    return byteArray;
                } finally {
                }
            } catch (java.lang.Exception e) {
                if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                    android.util.Slog.e(com.android.server.pm.PackageManagerService.TAG, "Unable to write domain verification for backup", e);
                    return null;
                }
                return null;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public android.os.IBinder getHoldLockToken() {
            if (!android.os.Build.IS_DEBUGGABLE) {
                throw new java.lang.SecurityException("getHoldLockToken requires a debuggable build");
            }
            com.android.server.pm.PackageManagerService.this.mContext.enforceCallingPermission("android.permission.INJECT_EVENTS", "getHoldLockToken requires INJECT_EVENTS permission");
            android.os.Binder token = new android.os.Binder();
            token.attachInterface(this, "holdLock:" + android.os.Binder.getCallingUid());
            return token;
        }

        public java.lang.String getInstantAppAndroidId(java.lang.String packageName, int userId) {
            getInstantAppAndroidId_enforcePermission();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, true, false, "getInstantAppAndroidId");
            if (!snapshot.isInstantApp(packageName, userId)) {
                return null;
            }
            return com.android.server.pm.PackageManagerService.this.mInstantAppRegistry.getInstantAppAndroidId(packageName, userId);
        }

        public byte[] getInstantAppCookie(java.lang.String packageName, int userId) {
            com.android.server.pm.pkg.PackageStateInternal packageState;
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, true, false, "getInstantAppCookie");
            if (!snapshot.isCallerSameApp(packageName, android.os.Binder.getCallingUid()) || (packageState = snapshot.getPackageStateInternal(packageName)) == null || packageState.getPkg() == null) {
                return null;
            }
            return com.android.server.pm.PackageManagerService.this.mInstantAppRegistry.getInstantAppCookie(packageState.getPkg(), userId);
        }

        public android.graphics.Bitmap getInstantAppIcon(java.lang.String packageName, int userId) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            if (!snapshot.canViewInstantApps(android.os.Binder.getCallingUid(), userId)) {
                com.android.server.pm.PackageManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_INSTANT_APPS", "getInstantAppIcon");
            }
            snapshot.enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, true, false, "getInstantAppIcon");
            return com.android.server.pm.PackageManagerService.this.mInstantAppRegistry.getInstantAppIcon(packageName, userId);
        }

        public android.content.pm.ParceledListSlice<android.content.pm.InstantAppInfo> getInstantApps(int userId) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            if (!snapshot.canViewInstantApps(android.os.Binder.getCallingUid(), userId)) {
                com.android.server.pm.PackageManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_INSTANT_APPS", "getEphemeralApplications");
            }
            snapshot.enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, true, false, "getEphemeralApplications");
            java.util.List<android.content.pm.InstantAppInfo> instantApps = com.android.server.pm.PackageManagerService.this.mInstantAppRegistry.getInstantApps(snapshot, userId);
            if (instantApps != null) {
                return new android.content.pm.ParceledListSlice<>(instantApps);
            }
            return null;
        }

        public android.content.pm.ResolveInfo getLastChosenActivity(android.content.Intent intent, java.lang.String resolvedType, int flags) {
            return com.android.server.pm.PackageManagerService.this.mPreferredActivityHelper.getLastChosenActivity(com.android.server.pm.PackageManagerService.this.snapshotComputer(), intent, resolvedType, flags);
        }

        public android.content.IntentSender getLaunchIntentSenderForPackage(java.lang.String packageName, java.lang.String callingPackage, java.lang.String featureId, int userId) throws android.os.RemoteException {
            return com.android.server.pm.PackageManagerService.this.mResolveIntentHelper.getLaunchIntentSenderForPackage(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageName, callingPackage, featureId, userId);
        }

        public java.util.List<java.lang.String> getMimeGroup(java.lang.String packageName, java.lang.String mimeGroup) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.PackageManagerService.this.enforceOwnerRights(snapshot, packageName, android.os.Binder.getCallingUid());
            return com.android.server.pm.PackageManagerService.this.getMimeGroupInternal(snapshot, packageName, mimeGroup);
        }

        public int getMoveStatus(int moveId) {
            getMoveStatus_enforcePermission();
            return com.android.server.pm.PackageManagerService.this.mMoveCallbacks.mLastStatus.get(moveId);
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
        public android.os.ParcelFileDescriptor getAppMetadataFd(java.lang.String packageName, int userId) throws android.os.ParcelableException {
            getAppMetadataFd_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (ps == null) {
                throw new android.os.ParcelableException(new android.content.pm.PackageManager.NameNotFoundException(packageName));
            }
            java.lang.String filePath = ps.getAppMetadataFilePath();
            if (filePath == null) {
                return null;
            }
            java.io.File file = new java.io.File(filePath);
            try {
                return android.os.ParcelFileDescriptor.open(file, 268435456);
            } catch (java.io.FileNotFoundException e) {
                return null;
            }
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
        public int getAppMetadataSource(java.lang.String packageName, int userId) throws android.os.ParcelableException {
            getAppMetadataSource_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (ps == null) {
                throw new android.os.ParcelableException(new android.content.pm.PackageManager.NameNotFoundException(packageName));
            }
            return ps.getAppMetadataSource();
        }

        public java.lang.String getPermissionControllerPackageName() {
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            if (snapshot.getPackageStateForInstalledAndFiltered(com.android.server.pm.PackageManagerService.this.mRequiredPermissionControllerPackage, callingUid, callingUserId) != null) {
                return com.android.server.pm.PackageManagerService.this.mRequiredPermissionControllerPackage;
            }
            throw new java.lang.IllegalStateException("PermissionController is not found");
        }

        public int getRuntimePermissionsVersion(int userId) {
            com.android.internal.util.Preconditions.checkArgumentNonnegative(userId);
            com.android.server.pm.PackageManagerService.this.enforceAdjustRuntimePermissionsPolicyOrUpgradeRuntimePermissions("getRuntimePermissionVersion");
            return com.android.server.pm.PackageManagerService.this.mSettings.getDefaultRuntimePermissionsVersion(userId);
        }

        public java.lang.String getSplashScreenTheme(java.lang.String packageName, int userId) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            int callingUid = android.os.Binder.getCallingUid();
            snapshot.enforceCrossUserPermission(callingUid, userId, false, false, "getSplashScreenTheme");
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (packageState == null) {
                return null;
            }
            return packageState.getUserStateOrDefault(userId).getSplashScreenTheme();
        }

        public int getUserMinAspectRatio(java.lang.String packageName, int userId) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (packageState == null) {
                return 0;
            }
            return packageState.getUserStateOrDefault(userId).getMinAspectRatio();
        }

        public android.os.Bundle getSuspendedPackageAppExtras(java.lang.String packageName, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = snapshot();
            if (snapshot.getPackageUid(packageName, 0L, userId) != callingUid) {
                throw new java.lang.SecurityException("Calling package " + packageName + " does not belong to calling uid " + callingUid);
            }
            return com.android.server.pm.SuspendPackageHelper.getSuspendedPackageAppExtras(snapshot, packageName, userId, callingUid);
        }

        public java.lang.String getSuspendingPackage(java.lang.String packageName, int userId) {
            android.content.pm.UserPackage suspender;
            try {
                int callingUid = android.os.Binder.getCallingUid();
                com.android.server.pm.Computer snapshot = snapshot();
                if (snapshot.isPackageSuspendedForUser(packageName, userId) && (suspender = com.android.server.pm.PackageManagerService.this.mSuspendPackageHelper.getSuspendingPackage(snapshot, packageName, userId, callingUid)) != null) {
                    return suspender.packageName;
                }
                return null;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return null;
            }
        }

        public android.content.pm.ParceledListSlice<android.content.pm.FeatureInfo> getSystemAvailableFeatures() {
            java.util.ArrayList<android.content.pm.FeatureInfo> res = new java.util.ArrayList<>(com.android.server.pm.PackageManagerService.this.mAvailableFeatures.size() + 1);
            res.addAll(com.android.server.pm.PackageManagerService.this.mAvailableFeatures.values());
            android.content.pm.FeatureInfo fi = new android.content.pm.FeatureInfo();
            fi.reqGlEsVersion = android.os.SystemProperties.getInt("ro.opengles.version", 0);
            res.add(fi);
            return new android.content.pm.ParceledListSlice<>(res);
        }

        public java.util.List<java.lang.String> getInitialNonStoppedSystemPackages() {
            return com.android.server.pm.PackageManagerService.this.mInitialNonStoppedSystemPackages != null ? new java.util.ArrayList(com.android.server.pm.PackageManagerService.this.mInitialNonStoppedSystemPackages) : new java.util.ArrayList();
        }

        public java.lang.String[] getUnsuspendablePackagesForUser(java.lang.String[] packageNames, int userId) {
            java.util.Objects.requireNonNull(packageNames, "packageNames cannot be null");
            com.android.server.pm.PackageManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.SUSPEND_APPS", "getUnsuspendablePackagesForUser");
            int callingUid = android.os.Binder.getCallingUid();
            if (android.os.UserHandle.getUserId(callingUid) != userId) {
                com.android.server.pm.PackageManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "Calling uid " + callingUid + " cannot query getUnsuspendablePackagesForUser for user " + userId);
            }
            return com.android.server.pm.PackageManagerService.this.mSuspendPackageHelper.getUnsuspendablePackagesForUser(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageNames, userId, callingUid);
        }

        public android.content.pm.VerifierDeviceIdentity getVerifierDeviceIdentity() throws android.os.RemoteException {
            android.content.pm.VerifierDeviceIdentity verifierDeviceIdentityLPw;
            getVerifierDeviceIdentity_enforcePermission();
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    verifierDeviceIdentityLPw = com.android.server.pm.PackageManagerService.this.mSettings.getVerifierDeviceIdentityLPw(com.android.server.pm.PackageManagerService.this.mLiveComputer);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return verifierDeviceIdentityLPw;
        }

        public void makeProviderVisible(int recipientUid, java.lang.String visibleAuthority) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            int recipientUserId = android.os.UserHandle.getUserId(recipientUid);
            android.content.pm.ProviderInfo providerInfo = snapshot.getGrantImplicitAccessProviderInfo(recipientUid, visibleAuthority);
            if (providerInfo == null) {
                return;
            }
            int visibleUid = providerInfo.applicationInfo.uid;
            com.android.server.pm.PackageManagerService.this.grantImplicitAccess(snapshot, recipientUserId, null, android.os.UserHandle.getAppId(recipientUid), visibleUid, false, false);
        }

        public void makeUidVisible(int recipientUid, int visibleUid) {
            makeUidVisible_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            int recipientUserId = android.os.UserHandle.getUserId(recipientUid);
            int visibleUserId = android.os.UserHandle.getUserId(visibleUid);
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, recipientUserId, false, false, "makeUidVisible");
            snapshot.enforceCrossUserPermission(callingUid, visibleUserId, false, false, "makeUidVisible");
            snapshot.enforceCrossUserPermission(recipientUid, visibleUserId, false, false, "makeUidVisible");
            com.android.server.pm.PackageManagerService.this.grantImplicitAccess(snapshot, recipientUserId, null, android.os.UserHandle.getAppId(recipientUid), visibleUid, false, false);
        }

        public void holdLock(android.os.IBinder token, int durationMs) {
            com.android.server.pm.PackageManagerService.this.mTestUtilityService.verifyHoldLockToken(token);
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    android.os.SystemClock.sleep(durationMs);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        public int installExistingPackageAsUser(java.lang.String packageName, int userId, int installFlags, int installReason, java.util.List<java.lang.String> whiteListedPermissions) {
            return ((java.lang.Integer) com.android.server.pm.PackageManagerService.this.mInstallPackageHelper.installExistingPackageAsUser(packageName, userId, installFlags, installReason, whiteListedPermissions, null).first).intValue();
        }

        public boolean isAutoRevokeWhitelisted(java.lang.String packageName) {
            int mode = ((android.app.AppOpsManager) com.android.server.pm.PackageManagerService.this.mInjector.getSystemService(android.app.AppOpsManager.class)).checkOpNoThrow(97, android.os.Binder.getCallingUid(), packageName);
            return mode == 1;
        }

        public boolean isPackageStateProtected(java.lang.String packageName, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingAppId = android.os.UserHandle.getAppId(callingUid);
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, userId, false, true, "isPackageStateProtected");
            if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot(callingAppId) && snapshot.checkUidPermission("android.permission.MANAGE_DEVICE_ADMINS", callingUid) != 0) {
                throw new java.lang.SecurityException("Caller must have the android.permission.MANAGE_DEVICE_ADMINS permission.");
            }
            return com.android.server.pm.PackageManagerService.this.mProtectedPackages.isPackageStateProtected(userId, packageName);
        }

        public boolean isProtectedBroadcast(java.lang.String actionName) {
            boolean zContains;
            if (actionName != null && (actionName.startsWith("android.net.netmon.lingerExpired") || actionName.startsWith("com.android.server.sip.SipWakeupTimer") || actionName.startsWith("com.android.internal.telephony.data-reconnect") || actionName.startsWith("android.net.netmon.launchCaptivePortalApp"))) {
                return true;
            }
            synchronized (com.android.server.pm.PackageManagerService.this.mProtectedBroadcasts) {
                zContains = com.android.server.pm.PackageManagerService.this.mProtectedBroadcasts.contains(actionName);
            }
            return zContains;
        }

        public void logAppProcessStartIfNeeded(java.lang.String packageName, java.lang.String processName, int uid, java.lang.String seinfo, java.lang.String apkFile, int pid) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null || !android.app.admin.SecurityLog.isLoggingEnabled()) {
                return;
            }
            com.android.server.pm.PackageManagerService.this.mProcessLoggingHandler.logAppProcessStart(com.android.server.pm.PackageManagerService.this.mContext, (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class), apkFile, packageName, processName, uid, seinfo, pid);
        }

        public int movePackage(final java.lang.String packageName, final java.lang.String volumeUuid) {
            movePackage_enforcePermission();
            final int callingUid = android.os.Binder.getCallingUid();
            final android.os.UserHandle user = new android.os.UserHandle(android.os.UserHandle.getUserId(callingUid));
            final int moveId = com.android.server.pm.PackageManagerService.this.mNextMoveId.getAndIncrement();
            com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$movePackage$5(packageName, volumeUuid, moveId, callingUid, user);
                }
            });
            return moveId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$movePackage$5(java.lang.String packageName, java.lang.String volumeUuid, int moveId, int callingUid, android.os.UserHandle user) {
            try {
                com.android.server.pm.MovePackageHelper movePackageHelper = new com.android.server.pm.MovePackageHelper(com.android.server.pm.PackageManagerService.this);
                movePackageHelper.movePackageInternal(packageName, volumeUuid, moveId, callingUid, user);
            } catch (com.android.server.pm.PackageManagerException e) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Failed to move " + packageName, e);
                com.android.server.pm.PackageManagerService.this.mMoveCallbacks.notifyStatusChanged(moveId, e.error);
            }
        }

        public int movePrimaryStorage(java.lang.String volumeUuid) throws android.os.RemoteException {
            movePrimaryStorage_enforcePermission();
            final int realMoveId = com.android.server.pm.PackageManagerService.this.mNextMoveId.getAndIncrement();
            android.os.Bundle extras = new android.os.Bundle();
            extras.putString("android.os.storage.extra.FS_UUID", volumeUuid);
            com.android.server.pm.PackageManagerService.this.mMoveCallbacks.notifyCreated(realMoveId, extras);
            android.content.pm.IPackageMoveObserver callback = new android.content.pm.IPackageMoveObserver.Stub() { // from class: com.android.server.pm.PackageManagerService.IPackageManagerImpl.2
                public void onCreated(int moveId, android.os.Bundle extras2) {
                }

                public void onStatusChanged(int moveId, int status, long estMillis) {
                    com.android.server.pm.PackageManagerService.this.mMoveCallbacks.notifyStatusChanged(realMoveId, status, estMillis);
                }
            };
            android.os.storage.StorageManager storage = (android.os.storage.StorageManager) com.android.server.pm.PackageManagerService.this.mInjector.getSystemService(android.os.storage.StorageManager.class);
            storage.setPrimaryStorageUuid(volumeUuid, callback);
            return realMoveId;
        }

        public void notifyDexLoad(java.lang.String loadingPackageName, java.util.Map<java.lang.String, java.lang.String> classLoaderContextMap, java.lang.String loaderIsa) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = snapshot();
            if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot() && !snapshot.isCallerSameApp(loadingPackageName, callingUid, true)) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, android.text.TextUtils.formatSimple("Invalid dex load report. loadingPackageName=%s, uid=%d", new java.lang.Object[]{loadingPackageName, java.lang.Integer.valueOf(callingUid)}));
                return;
            }
            android.os.UserHandle user = android.os.Binder.getCallingUserHandle();
            int userId = user.getIdentifier();
            com.android.server.art.DexUseManagerLocal dexUseManager = com.android.server.pm.DexOptHelper.getDexUseManagerLocal();
            if (dexUseManager != null) {
                com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshot = ((com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.pm.PackageManagerLocal.class)).withFilteredSnapshot(callingUid, user);
                if (loaderIsa != null) {
                    try {
                        com.android.server.pm.pkg.PackageState loadingPkgState = filteredSnapshot.getPackageState(loadingPackageName);
                        if (loadingPkgState != null) {
                            java.lang.String loadingPkgAbi = loadingPkgState.getPrimaryCpuAbi();
                            if (loadingPkgAbi == null) {
                                loadingPkgAbi = android.os.Build.SUPPORTED_ABIS[0];
                            }
                            java.lang.String loadingPkgDexCodeIsa = com.android.server.pm.InstructionSets.getDexCodeInstructionSet(dalvik.system.VMRuntime.getInstructionSet(loadingPkgAbi));
                            if (!loaderIsa.equals(loadingPkgDexCodeIsa)) {
                                android.util.Log.wtf(com.android.server.pm.PackageManagerService.TAG, "Invalid loaderIsa in notifyDexLoad call from " + loadingPackageName + ", uid " + callingUid + ": expected " + loadingPkgDexCodeIsa + ", got " + loaderIsa);
                                if (filteredSnapshot != null) {
                                    filteredSnapshot.close();
                                    return;
                                }
                                return;
                            }
                        }
                    } finally {
                    }
                }
                android.content.pm.ApplicationInfo ai = snapshot.getApplicationInfo(loadingPackageName, 0L, userId);
                if (ai == null) {
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Loading a package that does not exist for the calling user. package=" + loadingPackageName + ", user=" + userId);
                }
                boolean needBgDexopt = com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.needNotifyDexLoad(ai, loadingPackageName, classLoaderContextMap);
                try {
                    dexUseManager.notifyDexContainersLoaded(filteredSnapshot, loadingPackageName, classLoaderContextMap);
                } catch (java.lang.IllegalStateException e) {
                    if (!android.os.Build.OPLUS_64BIT_ONLY_CHIP || !e.getMessage().contains("Unsupported isa 'arm'")) {
                        throw e;
                    }
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Dex with art service is conflict with hbt_translator");
                }
                if (needBgDexopt) {
                    com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.notifyDexLoad(ai, loadingPackageName, classLoaderContextMap, loaderIsa);
                }
                if (filteredSnapshot != null) {
                    filteredSnapshot.close();
                    return;
                }
                return;
            }
            android.content.pm.ApplicationInfo ai2 = snapshot.getApplicationInfo(loadingPackageName, 0L, userId);
            if (ai2 == null) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Loading a package that does not exist for the calling user. package=" + loadingPackageName + ", user=" + userId);
            } else {
                com.android.server.pm.PackageManagerService.this.mDexManager.notifyDexLoad(ai2, classLoaderContextMap, loaderIsa, userId, android.os.Process.isIsolated(callingUid));
            }
        }

        public void notifyPackageUse(java.lang.String packageName, int reason) {
            boolean notify;
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            if (snapshot.getInstantAppPackageName(callingUid) != null) {
                notify = snapshot.isCallerSameApp(packageName, callingUid);
            } else {
                notify = !snapshot.isInstantAppInternal(packageName, callingUserId, 1000);
            }
            if (!notify) {
                return;
            }
            com.android.server.pm.PackageManagerService.this.notifyPackageUseInternal(packageName, reason);
        }

        public void overrideLabelAndIcon(android.content.ComponentName componentName, java.lang.String nonLocalizedLabel, int icon, int userId) {
            if (android.text.TextUtils.isEmpty(nonLocalizedLabel)) {
                throw new java.lang.IllegalArgumentException("Override label should be a valid String");
            }
            com.android.server.pm.PackageManagerService.this.updateComponentLabelIcon(componentName, nonLocalizedLabel, java.lang.Integer.valueOf(icon), userId);
        }

        public android.content.pm.ParceledListSlice<android.content.pm.PackageManager.Property> queryProperty(java.lang.String propertyName, int componentType) {
            java.util.Objects.requireNonNull(propertyName);
            final int callingUid = android.os.Binder.getCallingUid();
            final int callingUserId = android.os.UserHandle.getCallingUserId();
            final com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            java.util.List<android.content.pm.PackageManager.Property> result = com.android.server.pm.PackageManagerService.this.mPackageProperty.queryProperty(propertyName, componentType, new java.util.function.Predicate() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.PackageManagerService.IPackageManagerImpl.lambda$queryProperty$6(snapshot, callingUid, callingUserId, (java.lang.String) obj);
                }
            });
            if (result == null) {
                return android.content.pm.ParceledListSlice.emptyList();
            }
            return new android.content.pm.ParceledListSlice<>(result);
        }

        static /* synthetic */ boolean lambda$queryProperty$6(com.android.server.pm.Computer snapshot, int callingUid, int callingUserId, java.lang.String packageName) {
            return snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, callingUserId) == null;
        }

        public void registerDexModule(java.lang.String packageName, final java.lang.String dexModulePath, boolean isSharedModule, final android.content.pm.IDexModuleRegisterCallback callback) {
            android.util.Slog.i(com.android.server.pm.PackageManagerService.TAG, "Ignored unsupported registerDexModule call for " + dexModulePath + " in " + packageName);
            final com.android.server.pm.dex.DexManager.RegisterDexModuleResult result = new com.android.server.pm.dex.DexManager.RegisterDexModuleResult(false, "registerDexModule call not supported since Android U");
            if (callback != null) {
                com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.pm.PackageManagerService.IPackageManagerImpl.lambda$registerDexModule$7(callback, dexModulePath, result);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$registerDexModule$7(android.content.pm.IDexModuleRegisterCallback callback, java.lang.String dexModulePath, com.android.server.pm.dex.DexManager.RegisterDexModuleResult result) {
            try {
                callback.onDexModuleRegistered(dexModulePath, result.success, result.message);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Failed to callback after module registration " + dexModulePath, e);
            }
        }

        public void registerMoveCallback(android.content.pm.IPackageMoveObserver callback) {
            registerMoveCallback_enforcePermission();
            com.android.server.pm.PackageManagerService.this.mMoveCallbacks.register(callback);
        }

        public void restoreDomainVerification(byte[] backup, int userId) {
            if (android.os.Binder.getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Only the system may call restorePreferredActivities()");
            }
            try {
                java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(backup);
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(input);
                com.android.server.pm.PackageManagerService.this.mDomainVerificationManager.restoreSettings(com.android.server.pm.PackageManagerService.this.snapshotComputer(), parser);
                input.close();
            } catch (java.lang.Exception e) {
                if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                    android.util.Slog.e(com.android.server.pm.PackageManagerService.TAG, "Exception restoring domain verification: " + e.getMessage());
                }
            }
        }

        public void restoreLabelAndIcon(android.content.ComponentName componentName, int userId) {
            com.android.server.pm.PackageManagerService.this.updateComponentLabelIcon(componentName, null, null, userId);
        }

        public void sendDeviceCustomizationReadyBroadcast() {
            com.android.server.pm.PackageManagerService.this.mContext.enforceCallingPermission("android.permission.SEND_DEVICE_CUSTOMIZATION_READY", "sendDeviceCustomizationReadyBroadcast");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.pm.BroadcastHelper.sendDeviceCustomizationReadyBroadcast();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setApplicationCategoryHint(final java.lang.String packageName, final int categoryHint, final java.lang.String callerPackageName) {
            final int callingUid = android.os.Binder.getCallingUid();
            final int userId = android.os.UserHandle.getCallingUserId();
            com.android.internal.util.FunctionalUtils.ThrowingBiFunction<com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState, com.android.server.pm.Computer, com.android.server.pm.pkg.mutate.PackageStateMutator.Result> implementation = new com.android.internal.util.FunctionalUtils.ThrowingBiFunction() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda20
                public final java.lang.Object applyOrThrow(java.lang.Object obj, java.lang.Object obj2) {
                    return this.f$0.lambda$setApplicationCategoryHint$9(callingUid, callerPackageName, userId, packageName, categoryHint, (com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState) obj, (com.android.server.pm.Computer) obj2);
                }
            };
            com.android.server.pm.pkg.mutate.PackageStateMutator.Result result = (com.android.server.pm.pkg.mutate.PackageStateMutator.Result) implementation.apply(com.android.server.pm.PackageManagerService.this.recordInitialState(), com.android.server.pm.PackageManagerService.this.snapshotComputer());
            if (result != null && result.isStateChanged() && !result.isSpecificPackageNull()) {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mPackageStateWriteLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        result = (com.android.server.pm.pkg.mutate.PackageStateMutator.Result) implementation.apply(com.android.server.pm.PackageManagerService.this.recordInitialState(), com.android.server.pm.PackageManagerService.this.snapshotComputer());
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
            if (result != null && result.isCommitted()) {
                com.android.server.pm.PackageManagerService.this.scheduleWriteSettings();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.pm.pkg.mutate.PackageStateMutator.Result lambda$setApplicationCategoryHint$9(int callingUid, java.lang.String callerPackageName, int userId, java.lang.String packageName, final int categoryHint, com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState initialState, com.android.server.pm.Computer computer) throws java.lang.Exception {
            if (computer.getInstantAppPackageName(callingUid) != null) {
                throw new java.lang.SecurityException("Instant applications don't have access to this method");
            }
            int callerPackageUid = computer.getPackageUid(callerPackageName, 0L, userId);
            if (callerPackageUid != callingUid) {
                throw new java.lang.SecurityException("Package " + callerPackageName + " does not belong to " + callingUid);
            }
            com.android.server.pm.pkg.PackageStateInternal packageState = computer.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (packageState == null) {
                throw new java.lang.IllegalArgumentException("Unknown target package " + packageName);
            }
            if (!java.util.Objects.equals(callerPackageName, packageState.getInstallSource().mInstallerPackageName)) {
                throw new java.lang.IllegalArgumentException("Calling package " + callerPackageName + " is not installer for " + packageName);
            }
            if (packageState.getCategoryOverride() != categoryHint) {
                return com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(initialState, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda17
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setCategoryOverride(categoryHint);
                    }
                });
            }
            return null;
        }

        public void setApplicationEnabledSetting(java.lang.String appPackageName, int newState, int flags, int userId, java.lang.String callingPackage) throws java.lang.Throwable {
            if (com.android.server.pm.PackageManagerService.this.mUserManager.exists(userId)) {
                if (callingPackage == null) {
                    callingPackage = java.lang.Integer.toString(android.os.Binder.getCallingUid());
                }
                com.android.server.pm.PackageManagerService.this.setEnabledSettings(java.util.List.of(new android.content.pm.PackageManager.ComponentEnabledSetting(appPackageName, newState, flags)), userId, callingPackage);
            }
        }

        public boolean setApplicationHiddenSettingAsUser(java.lang.String packageName, final boolean hidden, final int userId) {
            setApplicationHiddenSettingAsUser_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, userId, true, true, "setApplicationHiddenSetting for user " + userId);
            if (hidden && com.android.server.pm.PackageManagerService.this.isPackageDeviceAdmin(packageName, userId)) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Not hiding package " + packageName + ": has active device admin");
                return false;
            }
            if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Cannot hide package: android");
                return false;
            }
            if (com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.interceptHideInSetApplicationHiddenSettingAsUser(hidden, packageName)) {
                return false;
            }
            long callingId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
                if (packageState == null) {
                    return false;
                }
                com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
                if (userState.isHidden() == hidden || !userState.isInstalled() || snapshot.shouldFilterApplication(packageState, callingUid, userId)) {
                    return false;
                }
                com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
                if (pkg != null) {
                    if (pkg.getSdkLibraryName() != null) {
                        android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Cannot hide package: " + packageName + " providing SDK library: " + pkg.getSdkLibraryName());
                        return false;
                    }
                    if (pkg.getStaticSharedLibraryName() != null) {
                        android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Cannot hide package: " + packageName + " providing static shared library: " + pkg.getStaticSharedLibraryName());
                        return false;
                    }
                }
                if (hidden && !android.os.UserHandle.isSameApp(callingUid, packageState.getAppId()) && com.android.server.pm.PackageManagerService.this.mProtectedPackages.isPackageStateProtected(userId, packageName)) {
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Not hiding protected package: " + packageName);
                    return false;
                }
                com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda8
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).userState(userId).setHidden(hidden);
                    }
                });
                com.android.server.pm.Computer newSnapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
                com.android.server.pm.pkg.PackageStateInternal newPackageState = newSnapshot.getPackageStateInternal(packageName);
                if (hidden) {
                    com.android.server.pm.PackageManagerService.this.killApplication(packageName, newPackageState.getAppId(), userId, "hiding pkg", 13);
                    com.android.server.pm.PackageManagerService.this.mBroadcastHelper.sendApplicationHiddenForUser(packageName, newPackageState, userId, com.android.server.pm.PackageManagerService.this);
                } else {
                    com.android.server.pm.PackageManagerService.this.mBroadcastHelper.sendPackageAddedForUser(newSnapshot, packageName, newPackageState, userId, false, 0, com.android.server.pm.PackageManagerService.this.mAppPredictionServicePackage);
                }
                com.android.server.pm.PackageManagerService.this.scheduleWritePackageRestrictions(userId);
                android.os.Binder.restoreCallingIdentity(callingId);
                return true;
            } finally {
                android.os.Binder.restoreCallingIdentity(callingId);
            }
        }

        public boolean setBlockUninstallForUser(java.lang.String packageName, boolean blockUninstall, int userId) {
            setBlockUninstallForUser_enforcePermission();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
            if (packageState != null && packageState.getPkg() != null) {
                com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
                if (pkg.getSdkLibraryName() != null) {
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Cannot block uninstall of package: " + packageName + " providing SDK library: " + pkg.getSdkLibraryName());
                    return false;
                }
                if (pkg.getStaticSharedLibraryName() != null) {
                    android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Cannot block uninstall of package: " + packageName + " providing static shared library: " + pkg.getStaticSharedLibraryName());
                    return false;
                }
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.PackageManagerService.this.mSettings.setBlockUninstallLPw(userId, packageName, blockUninstall);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            com.android.server.pm.PackageManagerService.this.scheduleWritePackageRestrictions(userId);
            return true;
        }

        public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags, int userId, java.lang.String callingPackage) throws java.lang.Throwable {
            if (com.android.server.pm.PackageManagerService.this.mUserManager.exists(userId)) {
                if (callingPackage == null) {
                    callingPackage = java.lang.Integer.toString(android.os.Binder.getCallingUid());
                }
                com.android.server.pm.PackageManagerService.this.setEnabledSettings(java.util.List.of(new android.content.pm.PackageManager.ComponentEnabledSetting(componentName, newState, flags)), userId, callingPackage);
            }
        }

        public void setComponentEnabledSettings(java.util.List<android.content.pm.PackageManager.ComponentEnabledSetting> settings, int userId, java.lang.String callingPackage) throws java.lang.Throwable {
            if (com.android.server.pm.PackageManagerService.this.mUserManager.exists(userId)) {
                if (settings == null || settings.isEmpty()) {
                    throw new java.lang.IllegalArgumentException("The list of enabled settings is empty");
                }
                if (callingPackage == null) {
                    callingPackage = java.lang.Integer.toString(android.os.Binder.getCallingUid());
                }
                com.android.server.pm.PackageManagerService.this.setEnabledSettings(settings, userId, callingPackage);
            }
        }

        public java.lang.String[] setDistractingPackageRestrictionsAsUser(java.lang.String[] packageNames, int restrictionFlags, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            android.util.Slog.d(com.android.server.pm.PackageManagerService.TAG, "setDistractingPackageRestrictionsAsUser packageNames " + java.util.Arrays.toString(packageNames) + " restrictionFlags " + restrictionFlags + " userId " + userId);
            if (com.android.server.pm.PackageManagerService.DEBUG_SETTINGS) {
                android.util.Slog.d(com.android.server.pm.PackageManagerService.TAG, "setDistractingPackageRestrictionsAsUser callerPackageNames :" + java.util.Arrays.toString(snapshot.getPackagesForUid(callingUid)));
            }
            com.android.server.pm.PackageManagerService.this.enforceCanSetDistractingPackageRestrictionsAsUser(callingUid, userId, "setDistractingPackageRestrictionsAsUser");
            java.util.Objects.requireNonNull(packageNames, "packageNames cannot be null");
            return com.android.server.pm.PackageManagerService.this.mDistractingPackageHelper.setDistractingPackageRestrictionsAsUser(snapshot, packageNames, restrictionFlags, userId, callingUid);
        }

        public void setHarmfulAppWarning(java.lang.String packageName, final java.lang.CharSequence warning, final int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingAppId = android.os.UserHandle.getAppId(callingUid);
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, userId, true, true, "setHarmfulAppInfo");
            if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRoot(callingAppId) && snapshot.checkUidPermission("android.permission.SET_HARMFUL_APP_WARNINGS", callingUid) != 0) {
                throw new java.lang.SecurityException("Caller must have the android.permission.SET_HARMFUL_APP_WARNINGS permission.");
            }
            com.android.server.pm.pkg.mutate.PackageStateMutator.Result result = com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    int i = userId;
                    java.lang.CharSequence charSequence = warning;
                    ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).userState(i).setHarmfulAppWarning(charSequence == null ? null : charSequence.toString());
                }
            });
            if (result.isSpecificPackageNull()) {
                throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
            }
            com.android.server.pm.PackageManagerService.this.scheduleWritePackageRestrictions(userId);
        }

        public boolean setInstallLocation(int loc) {
            setInstallLocation_enforcePermission();
            if (getInstallLocation() == loc) {
                return true;
            }
            if (loc == 0 || loc == 1 || loc == 2) {
                android.provider.Settings.Global.putInt(com.android.server.pm.PackageManagerService.this.mContext.getContentResolver(), "default_install_location", loc);
                return true;
            }
            return false;
        }

        public void setInstallerPackageName(final java.lang.String targetPackage, final java.lang.String installerPackageName) {
            final int callingUid = android.os.Binder.getCallingUid();
            final int callingUserId = android.os.UserHandle.getUserId(callingUid);
            com.android.internal.util.FunctionalUtils.ThrowingCheckedFunction<com.android.server.pm.Computer, java.lang.Boolean, java.lang.RuntimeException> implementation = new com.android.internal.util.FunctionalUtils.ThrowingCheckedFunction() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda1
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$setInstallerPackageName$12(callingUid, targetPackage, callingUserId, installerPackageName, (com.android.server.pm.Computer) obj);
                }
            };
            com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState initialState = com.android.server.pm.PackageManagerService.this.recordInitialState();
            boolean allowed = ((java.lang.Boolean) implementation.apply(com.android.server.pm.PackageManagerService.this.snapshotComputer())).booleanValue();
            if (allowed) {
                final int installerPackageUid = installerPackageName == null ? -1 : com.android.server.pm.PackageManagerService.this.snapshotComputer().getPackageUid(installerPackageName, 0L, callingUserId);
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        com.android.server.pm.pkg.mutate.PackageStateMutator.Result result = com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(initialState, targetPackage, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda2
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setInstaller(installerPackageName, installerPackageUid);
                            }
                        });
                        if (result.isPackagesChanged() || result.isStateChanged()) {
                            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = com.android.server.pm.PackageManagerService.this.mPackageStateWriteLock;
                            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                            synchronized (packageManagerTracedLock2) {
                                try {
                                    boolean allowed2 = ((java.lang.Boolean) implementation.apply(com.android.server.pm.PackageManagerService.this.snapshotComputer())).booleanValue();
                                    if (!allowed2) {
                                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                        return;
                                    } else {
                                        com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, targetPackage, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda3
                                            @Override // java.util.function.Consumer
                                            public final void accept(java.lang.Object obj) {
                                                ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setInstaller(installerPackageName, installerPackageUid);
                                            }
                                        });
                                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                    }
                                } finally {
                                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                }
                            }
                        }
                        com.android.server.pm.pkg.PackageStateInternal targetPackageState = com.android.server.pm.PackageManagerService.this.snapshotComputer().getPackageStateInternal(targetPackage);
                        com.android.server.pm.PackageManagerService.this.mSettings.addInstallerPackageNames(targetPackageState.getInstallSource());
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        com.android.server.pm.PackageManagerService.this.mAppsFilter.addPackage(com.android.server.pm.PackageManagerService.this.snapshotComputer(), targetPackageState);
                        com.android.server.pm.PackageManagerService.this.scheduleWriteSettings();
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.lang.Boolean lambda$setInstallerPackageName$12(int callingUid, java.lang.String targetPackage, int callingUserId, java.lang.String installerPackageName, com.android.server.pm.Computer snapshot) throws java.lang.Throwable {
            com.android.server.pm.pkg.PackageStateInternal installerPackageState;
            android.content.pm.SigningDetails callerSigningDetails;
            if (snapshot.getInstantAppPackageName(callingUid) != null) {
                return false;
            }
            com.android.server.pm.pkg.PackageStateInternal targetPackageState = snapshot.getPackageStateForInstalledAndFiltered(targetPackage, callingUid, callingUserId);
            if (targetPackageState == null) {
                throw new java.lang.IllegalArgumentException("Unknown target package: " + targetPackage);
            }
            if (installerPackageName == null) {
                installerPackageState = null;
            } else {
                com.android.server.pm.pkg.PackageStateInternal installerPackageState2 = snapshot.getPackageStateForInstalledAndFiltered(installerPackageName, callingUid, callingUserId);
                if (installerPackageState2 == null) {
                    throw new java.lang.IllegalArgumentException("Unknown installer package: " + installerPackageName);
                }
                installerPackageState = installerPackageState2;
            }
            int appId = android.os.UserHandle.getAppId(callingUid);
            android.util.Pair<com.android.server.pm.pkg.PackageStateInternal, com.android.server.pm.pkg.SharedUserApi> either = snapshot.getPackageOrSharedUser(appId);
            if (either == null) {
                throw new java.lang.SecurityException("Unknown calling UID: " + callingUid);
            }
            if (either.first != null) {
                callerSigningDetails = ((com.android.server.pm.pkg.PackageStateInternal) either.first).getSigningDetails();
            } else {
                callerSigningDetails = ((com.android.server.pm.pkg.SharedUserApi) either.second).getSigningDetails();
            }
            if (installerPackageState != null && com.android.server.pm.PackageManagerServiceUtils.compareSignatures(callerSigningDetails, installerPackageState.getSigningDetails()) != 0) {
                throw new java.lang.SecurityException("Caller does not have same cert as new installer package " + installerPackageName);
            }
            java.lang.String targetInstallerPackageName = targetPackageState.getInstallSource().mInstallerPackageName;
            com.android.server.pm.pkg.PackageStateInternal targetInstallerPkgSetting = targetInstallerPackageName == null ? null : snapshot.getPackageStateInternal(targetInstallerPackageName);
            if (targetInstallerPkgSetting != null) {
                if (com.android.server.pm.PackageManagerServiceUtils.compareSignatures(callerSigningDetails, targetInstallerPkgSetting.getSigningDetails()) != 0) {
                    throw new java.lang.SecurityException("Caller does not have same cert as old installer package " + targetInstallerPackageName);
                }
            } else if (com.android.server.pm.PackageManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") != 0) {
                android.util.EventLog.writeEvent(1397638484, "150857253", java.lang.Integer.valueOf(callingUid), "");
                long binderToken = android.os.Binder.clearCallingIdentity();
                try {
                    try {
                        if (com.android.server.pm.PackageManagerService.this.mInjector.getCompatibility().isChangeEnabledByUid(com.android.server.pm.PackageManagerService.THROW_EXCEPTION_ON_REQUIRE_INSTALL_PACKAGES_TO_ADD_INSTALLER_PACKAGE, callingUid)) {
                            com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.beforeFailReturnInSetInstallerPackageNameOfVerificationPermission(installerPackageName);
                            throw new java.lang.SecurityException("Neither user " + callingUid + " nor current process has android.permission.INSTALL_PACKAGES");
                        }
                        android.os.Binder.restoreCallingIdentity(binderToken);
                        return false;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(binderToken);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
            return true;
        }

        public void relinquishUpdateOwnership(java.lang.String targetPackage) {
            com.android.server.pm.pkg.PackageStateInternal targetUpdateOwnerPkgSetting;
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal targetPackageState = snapshot.getPackageStateForInstalledAndFiltered(targetPackage, callingUid, callingUserId);
            if (targetPackageState == null) {
                throw new java.lang.IllegalArgumentException("Unknown target package: " + targetPackage);
            }
            java.lang.String targetUpdateOwnerPackageName = targetPackageState.getInstallSource().mUpdateOwnerPackageName;
            if (targetUpdateOwnerPackageName == null) {
                targetUpdateOwnerPkgSetting = null;
            } else {
                targetUpdateOwnerPkgSetting = snapshot.getPackageStateInternal(targetUpdateOwnerPackageName);
            }
            if (targetUpdateOwnerPkgSetting == null) {
                return;
            }
            int callingAppId = android.os.UserHandle.getAppId(callingUid);
            int targetUpdateOwnerAppId = targetUpdateOwnerPkgSetting.getAppId();
            if (callingAppId == 1000 || callingAppId == 2000 || callingAppId == targetUpdateOwnerAppId) {
                com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, targetPackage, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda22
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setUpdateOwner(null);
                    }
                });
                com.android.server.pm.PackageManagerService.this.scheduleWriteSettings();
                return;
            }
            throw new java.lang.SecurityException("Caller is not the current update owner.");
        }

        public boolean setInstantAppCookie(java.lang.String packageName, byte[] cookie, int userId) {
            com.android.server.pm.pkg.PackageStateInternal packageState;
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(android.os.Binder.getCallingUid(), userId, true, true, "setInstantAppCookie");
            if (!snapshot.isCallerSameApp(packageName, android.os.Binder.getCallingUid()) || (packageState = snapshot.getPackageStateInternal(packageName)) == null || packageState.getPkg() == null) {
                return false;
            }
            return com.android.server.pm.PackageManagerService.this.mInstantAppRegistry.setInstantAppCookie(packageState.getPkg(), cookie, com.android.server.pm.PackageManagerService.this.mContext.getPackageManager().getInstantAppCookieMaxBytes(), userId);
        }

        public void setKeepUninstalledPackages(java.util.List<java.lang.String> packageList) {
            com.android.server.pm.PackageManagerService.this.mContext.enforceCallingPermission("android.permission.KEEP_UNINSTALLED_PACKAGES", "setKeepUninstalledPackages requires KEEP_UNINSTALLED_PACKAGES permission");
            java.util.Objects.requireNonNull(packageList);
            com.android.server.pm.PackageManagerService.this.setKeepUninstalledPackagesInternal(snapshot(), packageList);
        }

        public void setMimeGroup(final java.lang.String packageName, final java.lang.String mimeGroup, java.util.List<java.lang.String> mimeTypes) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.PackageManagerService.this.enforceOwnerRights(snapshot, packageName, android.os.Binder.getCallingUid());
            java.util.List<java.lang.String> mimeTypes2 = com.android.internal.util.CollectionUtils.emptyIfNull(mimeTypes);
            for (int i = 0; i < mimeTypes2.size(); i++) {
                if (mimeTypes2.get(i).length() > 255) {
                    throw new java.lang.IllegalArgumentException("MIME type length exceeds 255 characters");
                }
            }
            final com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
            java.util.Set<java.lang.String> existingMimeTypes = packageState.getMimeGroups().get(mimeGroup);
            if (existingMimeTypes == null) {
                throw new java.lang.IllegalArgumentException("Unknown MIME group " + mimeGroup + " for package " + packageName);
            }
            if (existingMimeTypes.size() == mimeTypes2.size() && existingMimeTypes.containsAll(mimeTypes2)) {
                return;
            }
            if (mimeTypes2.size() > 500) {
                throw new java.lang.IllegalStateException("Max limit on MIME types for MIME group " + mimeGroup + " exceeded for package " + packageName);
            }
            final android.util.ArraySet<java.lang.String> mimeTypesSet = new android.util.ArraySet<>(mimeTypes2);
            com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setMimeGroup(mimeGroup, mimeTypesSet);
                }
            });
            if (com.android.server.pm.PackageManagerService.this.mComponentResolver.updateMimeGroup(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageName, mimeGroup)) {
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda16
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$setMimeGroup$17(packageName, packageState);
                    }
                });
            }
            com.android.server.pm.PackageManagerService.this.scheduleWriteSettings();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setMimeGroup$17(java.lang.String packageName, com.android.server.pm.pkg.PackageStateInternal packageState) throws java.lang.Exception {
            com.android.server.pm.PackageManagerService.this.mPreferredActivityHelper.clearPackagePreferredActivities(packageName, -1);
            com.android.server.pm.Computer snapShot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            java.util.ArrayList<java.lang.String> components = new java.util.ArrayList<>(java.util.Collections.singletonList(packageName));
            int appId = packageState.getAppId();
            int[] userIds = com.android.server.pm.PackageManagerService.this.resolveUserIds(-1);
            for (int i = 0; i < userIds.length; i++) {
                com.android.server.pm.pkg.PackageUserStateInternal pkgUserState = packageState.getUserStates().get(userIds[i]);
                if (pkgUserState != null && pkgUserState.isInstalled()) {
                    int packageUid = android.os.UserHandle.getUid(userIds[i], appId);
                    com.android.server.pm.PackageManagerService.this.mBroadcastHelper.sendPackageChangedBroadcast(snapShot, packageName, true, components, packageUid, "The mimeGroup is changed");
                }
            }
        }

        public void setPackageStoppedState(java.lang.String packageName, boolean stopped, int userId) throws java.lang.Throwable {
            com.android.server.pm.PackageManagerService.this.setPackageStoppedState(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageName, stopped, userId);
        }

        public java.lang.String[] setPackagesSuspendedAsUser(java.lang.String[] packageNames, boolean suspended, android.os.PersistableBundle appExtras, android.os.PersistableBundle launcherExtras, android.content.pm.SuspendDialogInfo dialogInfo, int flags, java.lang.String suspendingPackage, int suspendingUserId, int targetUserId) {
            int callingUid = android.os.Binder.getCallingUid();
            boolean quarantined = (flags & 1) != 0 && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.quarantinedEnabled();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            android.content.pm.UserPackage suspender = android.app.admin.flags.Flags.crossUserSuspensionEnabledRo() ? android.content.pm.UserPackage.of(suspendingUserId, suspendingPackage) : android.content.pm.UserPackage.of(targetUserId, suspendingPackage);
            com.android.server.pm.PackageManagerService.this.enforceCanSetPackagesSuspendedAsUser(snapshot, quarantined, suspender, callingUid, targetUserId, "setPackagesSuspendedAsUser");
            return com.android.server.pm.PackageManagerService.this.mSuspendPackageHelper.setPackagesSuspended(snapshot, packageNames, suspended, appExtras, launcherExtras, dialogInfo, suspender, targetUserId, callingUid, quarantined);
        }

        public boolean setRequiredForSystemUser(java.lang.String packageName, final boolean requiredForSystemUser) {
            com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRoot("setRequiredForSystemUser can only be run by the system or root");
            com.android.server.pm.pkg.mutate.PackageStateMutator.Result result = com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda14
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setRequiredForSystemUser(requiredForSystemUser);
                }
            });
            if (!result.isCommitted()) {
                return false;
            }
            com.android.server.pm.PackageManagerService.this.scheduleWriteSettings();
            return true;
        }

        public void setUserMinAspectRatio(java.lang.String packageName, final int userId, final int aspectRatio) {
            setUserMinAspectRatio_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, userId, false, false, "setUserMinAspectRatio");
            com.android.server.pm.PackageManagerService.this.enforceOwnerRights(snapshot, packageName, callingUid);
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (packageState == null || packageState.getUserStateOrDefault(userId).getMinAspectRatio() == aspectRatio) {
                return;
            }
            com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).userState(userId).setMinAspectRatio(aspectRatio);
                }
            });
        }

        public void setRuntimePermissionsVersion(int version, int userId) {
            com.android.internal.util.Preconditions.checkArgumentNonnegative(version);
            com.android.internal.util.Preconditions.checkArgumentNonnegative(userId);
            com.android.server.pm.PackageManagerService.this.enforceAdjustRuntimePermissionsPolicyOrUpgradeRuntimePermissions("setRuntimePermissionVersion");
            com.android.server.pm.PackageManagerService.this.mSettings.setDefaultRuntimePermissionsVersion(version, userId);
        }

        public void setSplashScreenTheme(java.lang.String packageName, final java.lang.String themeId, final int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            snapshot.enforceCrossUserPermission(callingUid, userId, false, false, "setSplashScreenTheme");
            com.android.server.pm.PackageManagerService.this.enforceOwnerRights(snapshot, packageName, callingUid);
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateForInstalledAndFiltered(packageName, callingUid, userId);
            if (packageState == null) {
                return;
            }
            com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).userState(userId).setSplashScreenTheme(themeId);
                }
            });
        }

        public void setUpdateAvailable(java.lang.String packageName, final boolean updateAvailable) {
            setUpdateAvailable_enforcePermission();
            com.android.server.pm.PackageManagerService.this.commitPackageStateMutation(null, packageName, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.pkg.mutate.PackageStateWrite) obj).setUpdateAvailable(updateAvailable);
                }
            });
        }

        public void unregisterMoveCallback(android.content.pm.IPackageMoveObserver callback) {
            unregisterMoveCallback_enforcePermission();
            com.android.server.pm.PackageManagerService.this.mMoveCallbacks.unregister(callback);
        }

        public void verifyPendingInstall(final int verificationId, final int verificationCode) throws android.os.RemoteException {
            if (verificationId >= 0) {
                com.android.server.pm.PackageManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.PACKAGE_VERIFICATION_AGENT", "Only package verification agents can verify applications");
            }
            final int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$IPackageManagerImpl$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$verifyPendingInstall$22(verificationId, callingUid, verificationCode);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$verifyPendingInstall$22(int verificationId, int callingUid, int verificationCode) {
            int id = verificationId >= 0 ? verificationId : -verificationId;
            com.android.server.pm.PackageVerificationState state = com.android.server.pm.PackageManagerService.this.mPendingVerification.get(id);
            if (state == null) {
                return;
            }
            if (!state.checkRequiredVerifierUid(callingUid) && !state.checkSufficientVerifierUid(callingUid)) {
                return;
            }
            android.os.Message msg = com.android.server.pm.PackageManagerService.this.mHandler.obtainMessage(15);
            com.android.server.pm.PackageVerificationResponse response = new com.android.server.pm.PackageVerificationResponse(verificationCode, callingUid);
            msg.arg1 = id;
            msg.obj = response;
            com.android.server.pm.PackageManagerService.this.mHandler.sendMessage(msg);
        }

        public void registerPackageMonitorCallback(android.os.IRemoteCallback callback, int userId) {
            int uid = android.os.Binder.getCallingUid();
            int targetUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), uid, userId, true, true, "registerPackageMonitorCallback", com.android.server.pm.PackageManagerService.this.mContext.getPackageName());
            com.android.server.pm.PackageManagerService.this.mPackageMonitorCallbackHelper.registerPackageMonitorCallback(callback, targetUserId, uid);
        }

        public void unregisterPackageMonitorCallback(android.os.IRemoteCallback callback) {
            com.android.server.pm.PackageManagerService.this.mPackageMonitorCallbackHelper.unregisterPackageMonitorCallback(callback);
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
        public void requestPackageChecksums(java.lang.String packageName, boolean includeSplits, int optional, int required, java.util.List trustedInstallers, android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener, int userId) throws android.os.ParcelableException {
            com.android.server.pm.PackageManagerService.this.requestChecksumsInternal(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageName, includeSplits, optional, required, trustedInstallers, onChecksumsReadyListener, userId, com.android.server.pm.PackageManagerService.this.mInjector.getBackgroundExecutor(), com.android.server.pm.PackageManagerService.this.mInjector.getBackgroundHandler());
        }

        public void notifyPackagesReplacedReceived(java.lang.String[] packages) {
            com.android.server.pm.Computer computer = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            android.util.ArraySet<java.lang.String> packagesToNotify = computer.getNotifyPackagesForReplacedReceived(packages);
            for (int index = 0; index < packagesToNotify.size(); index++) {
                com.android.server.pm.PackageManagerService.this.notifyInstallObserver(packagesToNotify.valueAt(index), false);
            }
        }

        public android.content.pm.ArchivedPackageParcel getArchivedPackage(java.lang.String packageName, int userId) {
            return com.android.server.pm.PackageManagerService.this.getArchivedPackageInternal(packageName, userId);
        }

        public android.graphics.Bitmap getArchivedAppIcon(java.lang.String packageName, android.os.UserHandle user, java.lang.String callingPackageName) {
            return com.android.server.pm.PackageManagerService.this.mInstallerService.mPackageArchiver.getArchivedAppIcon(packageName, user, callingPackageName);
        }

        public boolean isAppArchivable(java.lang.String packageName, android.os.UserHandle user) {
            return com.android.server.pm.PackageManagerService.this.mInstallerService.mPackageArchiver.isAppArchivable(packageName, user);
        }

        public boolean waitForHandler(long timeoutMillis, boolean forBackgroundHandler) {
            boolean z = true;
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            if (forBackgroundHandler) {
                android.os.Handler handler = com.android.server.pm.PackageManagerService.this.mBackgroundHandler;
                java.util.Objects.requireNonNull(latch);
                handler.post(new com.android.server.location.settings.SettingsStore$$ExternalSyntheticLambda0(latch));
            } else {
                android.os.Handler handler2 = com.android.server.pm.PackageManagerService.this.mHandler;
                java.util.Objects.requireNonNull(latch);
                handler2.post(new com.android.server.location.settings.SettingsStore$$ExternalSyntheticLambda0(latch));
            }
            long endTimeMillis = java.lang.System.currentTimeMillis() + timeoutMillis;
            while (latch.getCount() > 0) {
                try {
                    long remainingTimeMillis = endTimeMillis - java.lang.System.currentTimeMillis();
                    if (remainingTimeMillis <= 0) {
                        return false;
                    }
                    return latch.await(remainingTimeMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (java.lang.InterruptedException e) {
                }
            }
            return z;
        }

        public android.content.ComponentName getDomainVerificationAgent(int userId) {
            int callerUid = android.os.Binder.getCallingUid();
            if (!com.android.server.pm.PackageManagerServiceUtils.isRootOrShell(callerUid)) {
                throw new java.lang.SecurityException("Not allowed to query domain verification agent");
            }
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            android.content.ComponentName agent = com.android.server.pm.PackageManagerService.this.mDomainVerificationManager.getProxy().getComponentName();
            com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateForInstalledAndFiltered(agent.getPackageName(), callerUid, userId);
            if (ps == null) {
                return null;
            }
            com.android.server.utils.WatchedArraySet<java.lang.String> disabledComponents = ps.getUserStateOrDefault(userId).getDisabledComponentsNoCopy();
            if (disabledComponents != null && disabledComponents.contains(agent.getClassName())) {
                return null;
            }
            return agent;
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            try {
                return super.onTransact(code, data, reply, flags);
            } catch (java.lang.RuntimeException e) {
                if (!(e instanceof java.lang.SecurityException) && !(e instanceof java.lang.IllegalArgumentException) && !(e instanceof android.os.ParcelableException)) {
                    android.util.Slog.wtf(com.android.server.pm.PackageManagerService.TAG, "Package Manager Unexpected Exception", e);
                }
                throw e;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.pm.PackageManagerShellCommand(this, com.android.server.pm.PackageManagerService.this.mContext, com.android.server.pm.PackageManagerService.this.mDomainVerificationManager.getShell()).exec(this, in, out, err, args, callback, resultReceiver);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) throws java.lang.Throwable {
            android.util.ArraySet<java.lang.String> protectedBroadcasts;
            if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.pm.PackageManagerService.this.mContext, com.android.server.pm.PackageManagerService.TAG, pw)) {
                com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
                com.android.server.pm.KnownPackages knownPackages = new com.android.server.pm.KnownPackages(com.android.server.pm.PackageManagerService.this.mDefaultAppProvider, com.android.server.pm.PackageManagerService.this.mRequiredInstallerPackage, com.android.server.pm.PackageManagerService.this.mRequiredUninstallerPackage, com.android.server.pm.PackageManagerService.this.mSetupWizardPackage, com.android.server.pm.PackageManagerService.this.mRequiredVerifierPackages, com.android.server.pm.PackageManagerService.this.mDefaultTextClassifierPackage, com.android.server.pm.PackageManagerService.this.mSystemTextClassifierPackageName, com.android.server.pm.PackageManagerService.this.mRequiredPermissionControllerPackage, com.android.server.pm.PackageManagerService.this.mConfiguratorPackage, com.android.server.pm.PackageManagerService.this.mIncidentReportApproverPackage, com.android.server.pm.PackageManagerService.this.mAmbientContextDetectionPackage, com.android.server.pm.PackageManagerService.this.mWearableSensingPackage, com.android.server.pm.PackageManagerService.this.mAppPredictionServicePackage, com.android.server.pm.PackageManagerService.COMPANION_PACKAGE_NAME, com.android.server.pm.PackageManagerService.this.mRetailDemoPackage, com.android.server.pm.PackageManagerService.this.mOverlayConfigSignaturePackage, com.android.server.pm.PackageManagerService.this.mRecentsPackage);
                android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> availableFeatures = new android.util.ArrayMap<>((android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo>) com.android.server.pm.PackageManagerService.this.mAvailableFeatures);
                synchronized (com.android.server.pm.PackageManagerService.this.mProtectedBroadcasts) {
                    try {
                        protectedBroadcasts = new android.util.ArraySet<>(com.android.server.pm.PackageManagerService.this.mProtectedBroadcasts);
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
                new com.android.server.pm.DumpHelper(com.android.server.pm.PackageManagerService.this.mPermissionManager, com.android.server.pm.PackageManagerService.this.mStorageEventHelper, com.android.server.pm.PackageManagerService.this.mDomainVerificationManager, com.android.server.pm.PackageManagerService.this.mInstallerService, com.android.server.pm.PackageManagerService.this.mRequiredVerifierPackages, knownPackages, com.android.server.pm.PackageManagerService.this.mChangedPackagesTracker, availableFeatures, protectedBroadcasts, com.android.server.pm.PackageManagerService.this.getPerUidReadTimeouts(snapshot), com.android.server.pm.PackageManagerService.this.mSnapshotStatistics).doDump(snapshot, fd, pw, args);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PackageManagerInternalImpl extends com.android.server.pm.PackageManagerInternalBase {
        public PackageManagerInternalImpl() {
            super(com.android.server.pm.PackageManagerService.this);
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected android.content.Context getContext() {
            return com.android.server.pm.PackageManagerService.this.mContext;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManager() {
            return com.android.server.pm.PackageManagerService.this.mPermissionManager;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.AppDataHelper getAppDataHelper() {
            return com.android.server.pm.PackageManagerService.this.mAppDataHelper;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.PackageObserverHelper getPackageObserverHelper() {
            return com.android.server.pm.PackageManagerService.this.mPackageObserverHelper;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.ResolveIntentHelper getResolveIntentHelper() {
            return com.android.server.pm.PackageManagerService.this.mResolveIntentHelper;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.SuspendPackageHelper getSuspendPackageHelper() {
            return com.android.server.pm.PackageManagerService.this.mSuspendPackageHelper;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.DistractingPackageHelper getDistractingPackageHelper() {
            return com.android.server.pm.PackageManagerService.this.mDistractingPackageHelper;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.ProtectedPackages getProtectedPackages() {
            return com.android.server.pm.PackageManagerService.this.mProtectedPackages;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.UserNeedsBadgingCache getUserNeedsBadging() {
            return com.android.server.pm.PackageManagerService.this.mUserNeedsBadging;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.InstantAppRegistry getInstantAppRegistry() {
            return com.android.server.pm.PackageManagerService.this.mInstantAppRegistry;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.ApexManager getApexManager() {
            return com.android.server.pm.PackageManagerService.this.mApexManager;
        }

        @Override // com.android.server.pm.PackageManagerInternalBase
        protected com.android.server.pm.dex.DexManager getDexManager() {
            return com.android.server.pm.PackageManagerService.this.mDexManager;
        }

        @Override // android.content.pm.PackageManagerInternal
        public com.android.server.pm.dex.DynamicCodeLogger getDynamicCodeLogger() {
            return com.android.server.pm.PackageManagerService.this.mDynamicCodeLogger;
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isPlatformSigned(java.lang.String packageName) {
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot().getPackageStateInternal(packageName);
            if (packageState == null) {
                return false;
            }
            android.content.pm.SigningDetails signingDetails = packageState.getSigningDetails();
            return signingDetails.hasAncestorOrSelf(com.android.server.pm.PackageManagerService.this.mPlatformPackage.getSigningDetails()) || com.android.server.pm.PackageManagerService.this.mPlatformPackage.getSigningDetails().checkCapability(signingDetails, 4);
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isDataRestoreSafe(byte[] restoringFromSigHash, java.lang.String packageName) {
            com.android.server.pm.Computer snapshot = snapshot();
            android.content.pm.SigningDetails sd = snapshot.getSigningDetails(packageName);
            if (sd == null) {
                return false;
            }
            return sd.hasSha256Certificate(restoringFromSigHash, 1);
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isDataRestoreSafe(android.content.pm.Signature restoringFromSig, java.lang.String packageName) {
            com.android.server.pm.Computer snapshot = snapshot();
            android.content.pm.SigningDetails sd = snapshot.getSigningDetails(packageName);
            if (sd == null) {
                return false;
            }
            return sd.hasCertificate(restoringFromSig, 1);
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean hasSignatureCapability(int serverUid, int clientUid, int capability) {
            com.android.server.pm.Computer snapshot = snapshot();
            android.content.pm.SigningDetails serverSigningDetails = snapshot.getSigningDetails(serverUid);
            android.content.pm.SigningDetails clientSigningDetails = snapshot.getSigningDetails(clientUid);
            return serverSigningDetails.checkCapability(clientSigningDetails, capability) || clientSigningDetails.hasAncestorOrSelf(serverSigningDetails);
        }

        @Override // android.content.pm.PackageManagerInternal
        public com.android.server.pm.PackageList getPackageList(android.content.pm.PackageManagerInternal.PackageListObserver observer) {
            final java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
            com.android.server.pm.PackageManagerService.this.forEachPackageState(snapshot(), new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$PackageManagerInternalImpl$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.pm.PackageManagerService.PackageManagerInternalImpl.lambda$getPackageList$0(list, (com.android.server.pm.pkg.PackageStateInternal) obj);
                }
            });
            com.android.server.pm.PackageList packageList = new com.android.server.pm.PackageList(list, observer);
            if (observer != null) {
                com.android.server.pm.PackageManagerService.this.mPackageObserverHelper.addObserver(packageList);
            }
            return packageList;
        }

        static /* synthetic */ void lambda$getPackageList$0(java.util.ArrayList list, com.android.server.pm.pkg.PackageStateInternal packageState) {
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
            if (pkg != null) {
                list.add(pkg.getPackageName());
            }
        }

        @Override // android.content.pm.PackageManagerInternal
        public java.lang.String getDisabledSystemPackageName(java.lang.String packageName) {
            com.android.server.pm.pkg.PackageStateInternal disabledPkgSetting = snapshot().getDisabledSystemPackage(packageName);
            com.android.server.pm.pkg.AndroidPackage disabledPkg = disabledPkgSetting == null ? null : disabledPkgSetting.getPkg();
            if (disabledPkg == null) {
                return null;
            }
            return disabledPkg.getPackageName();
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isResolveActivityComponent(android.content.pm.ComponentInfo component) {
            return com.android.server.pm.PackageManagerService.this.mResolveActivity.packageName.equals(component.packageName) && com.android.server.pm.PackageManagerService.this.mResolveActivity.name.equals(component.name);
        }

        @Override // android.content.pm.PackageManagerInternal
        public long getCeDataInode(java.lang.String packageName, int userId) {
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot().getPackageStateInternal(packageName);
            if (packageState == null) {
                return 0L;
            }
            return packageState.getUserStateOrDefault(userId).getCeDataInode();
        }

        @Override // android.content.pm.PackageManagerInternal
        public void removeAllNonSystemPackageSuspensions(int userId) {
            com.android.server.pm.Computer computer = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            java.lang.String[] allPackages = computer.getAllAvailablePackageNames();
            com.android.server.pm.PackageManagerService.this.mSuspendPackageHelper.removeSuspensionsBySuspendingPackage(computer, allPackages, new java.util.function.Predicate() { // from class: com.android.server.pm.PackageManagerService$PackageManagerInternalImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.PackageManagerService.PackageManagerInternalImpl.lambda$removeAllNonSystemPackageSuspensions$1((android.content.pm.UserPackage) obj);
                }
            }, userId);
        }

        static /* synthetic */ boolean lambda$removeAllNonSystemPackageSuspensions$1(android.content.pm.UserPackage suspender) {
            return !com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(suspender.packageName);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void flushPackageRestrictions(int userId) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.PackageManagerService.this.flushPackageRestrictionsAsUserInternalLocked(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        @Override // android.content.pm.PackageManagerInternal
        public java.lang.String[] setPackagesSuspendedByAdmin(int userId, java.lang.String[] packageNames, boolean suspended) {
            int suspendingUserId = android.app.admin.flags.Flags.crossUserSuspensionEnabledRo() ? 0 : userId;
            android.content.pm.UserPackage suspender = android.content.pm.UserPackage.of(suspendingUserId, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            return com.android.server.pm.PackageManagerService.this.mSuspendPackageHelper.setPackagesSuspended(com.android.server.pm.PackageManagerService.this.snapshotComputer(), packageNames, suspended, null, null, null, suspender, userId, 1000, false);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void setDeviceAndProfileOwnerPackages(int deviceOwnerUserId, java.lang.String deviceOwnerPackage, android.util.SparseArray<java.lang.String> profileOwnerPackages) {
            com.android.server.pm.PackageManagerService.this.mProtectedPackages.setDeviceAndProfileOwnerPackages(deviceOwnerUserId, deviceOwnerPackage, profileOwnerPackages);
            android.util.ArraySet<java.lang.Integer> usersWithPoOrDo = new android.util.ArraySet<>();
            if (deviceOwnerPackage != null) {
                usersWithPoOrDo.add(java.lang.Integer.valueOf(deviceOwnerUserId));
            }
            int sz = profileOwnerPackages.size();
            for (int i = 0; i < sz; i++) {
                if (profileOwnerPackages.valueAt(i) != null) {
                    removeAllNonSystemPackageSuspensions(profileOwnerPackages.keyAt(i));
                }
            }
        }

        @Override // android.content.pm.PackageManagerInternal
        public void setExternalSourcesPolicy(android.content.pm.PackageManagerInternal.ExternalSourcesPolicy policy) {
            if (policy != null) {
                com.android.server.pm.PackageManagerService.this.mExternalSourcesPolicy = policy;
            }
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isPackagePersistent(java.lang.String packageName) {
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg;
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot().getPackageStateInternal(packageName);
            return packageState != null && (pkg = packageState.getPkg()) != null && packageState.isSystem() && pkg.isPersistent();
        }

        @Override // android.content.pm.PackageManagerInternal
        public java.util.List<android.content.pm.PackageInfo> getOverlayPackages(int userId) {
            android.content.pm.PackageInfo pkgInfo;
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            java.util.ArrayList<android.content.pm.PackageInfo> overlayPackages = new java.util.ArrayList<>();
            android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = snapshot.getPackageStates();
            for (int index = 0; index < packageStates.size(); index++) {
                com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.valueAt(index);
                com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
                if (pkg != null && pkg.getOverlayTarget() != null && (pkgInfo = snapshot.generatePackageInfo(packageState, 0L, userId)) != null) {
                    overlayPackages.add(pkgInfo);
                }
            }
            return overlayPackages;
        }

        @Override // android.content.pm.PackageManagerInternal
        public java.util.List<java.lang.String> getTargetPackageNames(int userId) {
            final java.util.List<java.lang.String> targetPackages = new java.util.ArrayList<>();
            com.android.server.pm.PackageManagerService.this.forEachPackageState(snapshot(), new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$PackageManagerInternalImpl$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.pm.PackageManagerService.PackageManagerInternalImpl.lambda$getTargetPackageNames$2(targetPackages, (com.android.server.pm.pkg.PackageStateInternal) obj);
                }
            });
            return targetPackages;
        }

        static /* synthetic */ void lambda$getTargetPackageNames$2(java.util.List targetPackages, com.android.server.pm.pkg.PackageStateInternal packageState) {
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
            if (pkg != null && !pkg.isResourceOverlay()) {
                targetPackages.add(pkg.getPackageName());
            }
        }

        @Override // android.content.pm.PackageManagerInternal
        public void setEnabledOverlayPackages(int userId, android.util.ArrayMap<java.lang.String, android.content.pm.overlay.OverlayPaths> pendingChanges, java.util.Set<java.lang.String> outUpdatedPackageNames, java.util.Set<java.lang.String> outInvalidPackageNames) throws java.lang.Throwable {
            com.android.server.pm.PackageManagerService.this.setEnabledOverlayPackages(userId, pendingChanges, outUpdatedPackageNames, outInvalidPackageNames);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void addIsolatedUid(int isolatedUid, int ownerUid) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.PackageManagerService.this.mIsolatedOwners.put(isolatedUid, ownerUid);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        @Override // android.content.pm.PackageManagerInternal
        public void removeIsolatedUid(int isolatedUid) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.PackageManagerService.this.mIsolatedOwners.delete(isolatedUid);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        @Override // android.content.pm.PackageManagerInternal
        public void notifyPackageUse(java.lang.String packageName, int reason) {
            com.android.server.pm.PackageManagerService.this.notifyPackageUseInternal(packageName, reason);
        }

        @Override // android.content.pm.PackageManagerInternal
        public java.lang.String removeLegacyDefaultBrowserPackageName(int userId) {
            java.lang.String strRemovePendingDefaultBrowserLPw;
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    strRemovePendingDefaultBrowserLPw = com.android.server.pm.PackageManagerService.this.mSettings.removePendingDefaultBrowserLPw(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return strRemovePendingDefaultBrowserLPw;
        }

        @Override // android.content.pm.PackageManagerInternal
        public void uninstallApex(java.lang.String packageName, long versionCode, int userId, android.content.IntentSender intentSender, int flags) {
            int callerUid = android.os.Binder.getCallingUid();
            if (!com.android.server.pm.PackageManagerServiceUtils.isRootOrShell(callerUid)) {
                throw new java.lang.SecurityException("Not allowed to uninstall apexes");
            }
            com.android.server.pm.PackageInstallerService.PackageDeleteObserverAdapter adapter = new com.android.server.pm.PackageInstallerService.PackageDeleteObserverAdapter(com.android.server.pm.PackageManagerService.this.mContext, intentSender, packageName, false, userId);
            if ((flags & 2) == 0) {
                adapter.onPackageDeleted(packageName, -5, "Can't uninstall an apex for a single user");
                return;
            }
            com.android.server.pm.ApexManager am = com.android.server.pm.PackageManagerService.this.mApexManager;
            android.content.pm.PackageInfo activePackage = snapshot().getPackageInfo(packageName, 1073741824L, 0);
            if (activePackage == null) {
                adapter.onPackageDeleted(packageName, -5, packageName + " is not an apex package");
                return;
            }
            if (versionCode != -1 && activePackage.getLongVersionCode() != versionCode) {
                adapter.onPackageDeleted(packageName, -5, "Active version " + activePackage.getLongVersionCode() + " is not equal to " + versionCode + "]");
            } else if (!am.uninstallApex(activePackage.applicationInfo.sourceDir)) {
                adapter.onPackageDeleted(packageName, -5, "Failed to uninstall apex " + packageName);
            } else {
                adapter.onPackageDeleted(packageName, 1, null);
            }
        }

        @Override // android.content.pm.PackageManagerInternal
        public void updateRuntimePermissionsFingerprint(int userId) {
            com.android.server.pm.PackageManagerService.this.mSettings.updateRuntimePermissionsFingerprint(userId);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void migrateLegacyObbData() {
            try {
                com.android.server.pm.PackageManagerService.this.mInstaller.migrateLegacyObbData();
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(com.android.server.pm.PackageManagerService.TAG, e);
            }
        }

        @Override // android.content.pm.PackageManagerInternal
        public void writeSettings(boolean async) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    if (async) {
                        com.android.server.pm.PackageManagerService.this.scheduleWriteSettings();
                    } else {
                        com.android.server.pm.PackageManagerService.this.writeSettingsLPrTEMP();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        @Override // android.content.pm.PackageManagerInternal
        public void writePermissionSettings(int[] userIds, boolean async) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    for (int userId : userIds) {
                        com.android.server.pm.PackageManagerService.this.mSettings.writePermissionStateForUserLPr(userId, !async);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        @Override // android.content.pm.PackageManagerInternal
        public com.android.server.pm.permission.LegacyPermissionSettings getLegacyPermissions() {
            com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings;
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    legacyPermissionSettings = com.android.server.pm.PackageManagerService.this.mSettings.mPermissions;
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return legacyPermissionSettings;
        }

        @Override // android.content.pm.PackageManagerInternal
        public com.android.permission.persistence.RuntimePermissionsState getLegacyPermissionsState(int userId) {
            com.android.permission.persistence.RuntimePermissionsState legacyPermissionsState;
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    legacyPermissionsState = com.android.server.pm.PackageManagerService.this.mSettings.getLegacyPermissionsState(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return legacyPermissionsState;
        }

        @Override // android.content.pm.PackageManagerInternal
        public int getLegacyPermissionsVersion(int userId) {
            int defaultRuntimePermissionsVersion;
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    defaultRuntimePermissionsVersion = com.android.server.pm.PackageManagerService.this.mSettings.getDefaultRuntimePermissionsVersion(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return defaultRuntimePermissionsVersion;
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isPermissionUpgradeNeeded(int userId) {
            return com.android.server.pm.PackageManagerService.this.mSettings.isPermissionUpgradeNeeded(userId) || com.android.server.pm.PackageManagerService.this.mPackageManagerServiceExt.customPermissionUpgradeNeeded();
        }

        @Override // android.content.pm.PackageManagerInternal
        public void setIntegrityVerificationResult(int verificationId, int verificationResult) {
            android.os.Message msg = com.android.server.pm.PackageManagerService.this.mHandler.obtainMessage(25);
            msg.arg1 = verificationId;
            msg.obj = java.lang.Integer.valueOf(verificationResult);
            com.android.server.pm.PackageManagerService.this.mHandler.sendMessage(msg);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void setVisibilityLogging(java.lang.String packageName, boolean enable) {
            com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRootOrShell("Only the system or shell can set visibility logging.");
            com.android.server.pm.pkg.PackageStateInternal packageState = snapshot().getPackageStateInternal(packageName);
            if (packageState == null) {
                throw new java.lang.IllegalStateException("No package found for " + packageName);
            }
            com.android.server.pm.PackageManagerService.this.mAppsFilter.getFeatureConfig().enableLogging(packageState.getAppId(), enable);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void clearBlockUninstallForUser(int userId) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = com.android.server.pm.PackageManagerService.this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.PackageManagerService.this.mSettings.clearBlockUninstallLPw(userId);
                    com.android.server.pm.PackageManagerService.this.mSettings.writePackageRestrictionsLPr(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean registerInstalledLoadingProgressCallback(java.lang.String packageName, android.content.pm.PackageManagerInternal.InstalledLoadingProgressCallback callback, int userId) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateForInstalledAndFiltered(packageName, android.os.Binder.getCallingUid(), userId);
            if (ps == null) {
                return false;
            }
            if (!ps.isLoading()) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Failed registering loading progress callback. Package is fully loaded.");
                return false;
            }
            if (com.android.server.pm.PackageManagerService.this.mIncrementalManager == null) {
                android.util.Slog.w(com.android.server.pm.PackageManagerService.TAG, "Failed registering loading progress callback. Incremental is not enabled");
                return false;
            }
            return com.android.server.pm.PackageManagerService.this.mIncrementalManager.registerLoadingProgressCallback(ps.getPathString(), callback.getBinder());
        }

        @Override // android.content.pm.PackageManagerInternal
        public android.content.pm.IncrementalStatesInfo getIncrementalStatesInfo(java.lang.String packageName, int filterCallingUid, int userId) {
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateForInstalledAndFiltered(packageName, filterCallingUid, userId);
            if (ps == null) {
                return null;
            }
            return new android.content.pm.IncrementalStatesInfo(ps.isLoading(), ps.getLoadingProgress(), ps.getLoadingCompletedTime());
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isSameApp(java.lang.String packageName, int callingUid, int userId) {
            return isSameApp(packageName, 0L, callingUid, userId);
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isSameApp(java.lang.String packageName, long flags, int callingUid, int userId) {
            if (packageName == null) {
                return false;
            }
            if (android.os.Process.isSdkSandboxUid(callingUid)) {
                return packageName.equals(com.android.server.pm.PackageManagerService.this.mRequiredSdkSandboxPackage);
            }
            com.android.server.pm.Computer snapshot = snapshot();
            int uid = snapshot.getPackageUid(packageName, flags, userId);
            return android.os.UserHandle.isSameApp(uid, callingUid);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPackageProcessKilledForUninstall$3(java.lang.String packageName) {
            com.android.server.pm.PackageManagerService.this.notifyInstallObserver(packageName, true);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void onPackageProcessKilledForUninstall(final java.lang.String packageName) {
            com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$PackageManagerInternalImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPackageProcessKilledForUninstall$3(packageName);
                }
            });
        }

        @Override // android.content.pm.PackageManagerInternal
        public int[] getDistractingPackageRestrictionsAsUser(java.lang.String[] packageNames, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.Computer snapshot = com.android.server.pm.PackageManagerService.this.snapshotComputer();
            java.util.Objects.requireNonNull(packageNames, "packageNames cannot be null");
            return com.android.server.pm.PackageManagerService.this.mDistractingPackageHelper.getDistractingPackageRestrictionsAsUser(snapshot, packageNames, userId, callingUid);
        }

        @Override // android.content.pm.PackageManagerInternal
        public android.content.pm.ParceledListSlice<android.content.pm.PackageInstaller.SessionInfo> getHistoricalSessions(int userId) {
            return com.android.server.pm.PackageManagerService.this.mInstallerService.getHistoricalSessions(userId);
        }

        @Override // android.content.pm.PackageManagerInternal
        public com.android.server.pm.PackageArchiver getPackageArchiver() {
            return com.android.server.pm.PackageManagerService.this.mInstallerService.mPackageArchiver;
        }

        @Override // android.content.pm.PackageManagerInternal
        public void sendPackageRestartedBroadcast(final java.lang.String packageName, int uid, final int flags) throws java.lang.Throwable {
            android.os.Bundle extras;
            int[] userIds;
            int userId = android.os.UserHandle.getUserId(uid);
            final int[] userIds2 = com.android.server.pm.PackageManagerService.this.resolveUserIds(userId);
            final android.util.SparseArray<int[]> broadcastAllowList = com.android.server.pm.PackageManagerService.this.snapshotComputer().getVisibilityAllowLists(packageName, userIds2);
            final android.os.Bundle extras2 = new android.os.Bundle();
            extras2.putInt("android.intent.extra.UID", uid);
            extras2.putInt("android.intent.extra.user_handle", userId);
            if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.stayStopped()) {
                extras2.putLong("android.intent.extra.TIME", android.os.SystemClock.elapsedRealtime());
                com.android.server.pm.PackageManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageManagerService$PackageManagerInternalImpl$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$sendPackageRestartedBroadcast$4(packageName, extras2, flags, userIds2, broadcastAllowList);
                    }
                });
                extras = extras2;
                userIds = userIds2;
            } else {
                extras = extras2;
                userIds = userIds2;
                com.android.server.pm.PackageManagerService.this.mBroadcastHelper.sendPackageBroadcast("android.intent.action.PACKAGE_RESTARTED", packageName, extras2, flags, null, null, userIds, null, broadcastAllowList, null, null);
            }
            com.android.server.pm.PackageManagerService.this.mPackageMonitorCallbackHelper.notifyPackageMonitor("android.intent.action.PACKAGE_RESTARTED", packageName, extras, userIds, null, broadcastAllowList, com.android.server.pm.PackageManagerService.this.mHandler, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sendPackageRestartedBroadcast$4(java.lang.String packageName, android.os.Bundle extras, int flags, int[] userIds, android.util.SparseArray broadcastAllowList) {
            com.android.server.pm.PackageManagerService.this.mBroadcastHelper.sendPackageBroadcast("android.intent.action.PACKAGE_RESTARTED", packageName, extras, flags, null, null, userIds, null, broadcastAllowList, null, null);
        }

        @Override // android.content.pm.PackageManagerInternal
        public void sendPackageDataClearedBroadcast(java.lang.String packageName, int uid, int userId, boolean isRestore, boolean isInstantApp) {
            int[] visibilityAllowList = com.android.server.pm.PackageManagerService.this.snapshotComputer().getVisibilityAllowList(packageName, userId);
            android.content.Intent intent = new android.content.Intent("android.intent.action.PACKAGE_DATA_CLEARED", android.net.Uri.fromParts("package", packageName, null));
            intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.HE_AAC_V1);
            intent.putExtra("android.intent.extra.UID", uid);
            intent.putExtra("android.intent.extra.user_handle", userId);
            if (isRestore) {
                intent.putExtra("android.intent.extra.IS_RESTORE", true);
            }
            if (isInstantApp) {
                intent.putExtra("android.intent.extra.PACKAGE_NAME", packageName);
            }
            com.android.server.pm.PackageManagerService.this.mBroadcastHelper.sendPackageBroadcastWithIntent(intent, userId, isInstantApp, 0, visibilityAllowList, null, null, null);
            com.android.server.pm.PackageManagerService.this.mPackageMonitorCallbackHelper.notifyPackageMonitorWithIntent(intent, userId, visibilityAllowList, com.android.server.pm.PackageManagerService.this.mHandler);
        }

        @Override // android.content.pm.PackageManagerInternal
        public boolean isUpgradingFromLowerThan(int sdkVersion) {
            boolean isUpgrading = com.android.server.pm.PackageManagerService.this.mPriorSdkVersion != -1;
            return isUpgrading && com.android.server.pm.PackageManagerService.this.mPriorSdkVersion < sdkVersion;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnabledOverlayPackages(final int userId, final android.util.ArrayMap<java.lang.String, android.content.pm.overlay.OverlayPaths> pendingChanges, final java.util.Set<java.lang.String> outUpdatedPackageNames, java.util.Set<java.lang.String> outInvalidPackageNames) throws java.lang.Throwable {
        com.android.server.pm.Computer computer;
        int i;
        int i2;
        com.android.server.pm.Computer computer2;
        int numberOfPendingChanges;
        android.content.pm.overlay.OverlayPaths newOverlayPaths;
        com.android.server.pm.pkg.PackageStateInternal packageState;
        java.lang.String targetPackageName;
        com.android.server.pm.pkg.AndroidPackage targetPkg;
        com.android.server.pm.pkg.PackageStateInternal packageState2;
        java.util.List<java.lang.String> libraryNames;
        java.lang.String targetPackageName2;
        com.android.server.pm.Computer computer3;
        android.content.pm.overlay.OverlayPaths newOverlayPaths2;
        com.android.server.pm.Computer computer4;
        android.util.Pair<java.util.List<android.content.pm.VersionedPackage>, java.util.List<java.lang.Boolean>> usingSharedLibraryPair;
        android.content.pm.overlay.OverlayPaths newOverlayPaths3;
        android.util.ArrayMap<java.lang.String, android.content.pm.overlay.OverlayPaths> arrayMap = pendingChanges;
        final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>>> targetPkgToLibNameToModifiedDependents = new android.util.ArrayMap<>();
        int numberOfPendingChanges2 = pendingChanges.size();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mOverlayPathsLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                computer = snapshotComputer();
                i = 0;
            } catch (java.lang.Throwable th) {
                th = th;
            }
            while (true) {
                com.android.server.pm.pkg.AndroidPackage targetPkg2 = null;
                if (i >= numberOfPendingChanges2) {
                    break;
                }
                try {
                    java.lang.String targetPackageName3 = arrayMap.keyAt(i);
                    android.content.pm.overlay.OverlayPaths newOverlayPaths4 = arrayMap.valueAt(i);
                    com.android.server.pm.pkg.PackageStateInternal packageState3 = computer.getPackageStateInternal(targetPackageName3);
                    if (packageState3 != null) {
                        targetPkg2 = packageState3.getPkg();
                    }
                    if (targetPackageName3 == null || targetPkg2 == null) {
                        computer2 = computer;
                        numberOfPendingChanges = numberOfPendingChanges2;
                        java.lang.String targetPackageName4 = targetPackageName3;
                        try {
                            android.util.Slog.e(TAG, "failed to find package " + targetPackageName4);
                            try {
                                outInvalidPackageNames.add(targetPackageName4);
                                i++;
                                arrayMap = pendingChanges;
                                numberOfPendingChanges2 = numberOfPendingChanges;
                                computer = computer2;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                while (true) {
                                    try {
                                        resetPriorityAfterPackageManagerTracedLockedSection();
                                        throw th;
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                    }
                                }
                            }
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                        }
                    } else {
                        try {
                            if (java.util.Objects.equals(packageState3.getUserStateOrDefault(userId).getOverlayPaths(), newOverlayPaths4)) {
                                computer2 = computer;
                                numberOfPendingChanges = numberOfPendingChanges2;
                            } else {
                                if (targetPkg2.getLibraryNames() != null) {
                                    java.util.List<java.lang.String> libraryNames2 = targetPkg2.getLibraryNames();
                                    int j = 0;
                                    while (j < libraryNames2.size()) {
                                        java.lang.String libName = libraryNames2.get(j);
                                        int j2 = j;
                                        com.android.server.pm.pkg.PackageStateInternal packageState4 = packageState3;
                                        android.content.pm.SharedLibraryInfo info = computer.getSharedLibraryInfo(libName, -1L);
                                        if (info == null) {
                                            computer3 = computer;
                                            newOverlayPaths2 = newOverlayPaths4;
                                            numberOfPendingChanges = numberOfPendingChanges2;
                                            targetPkg = targetPkg2;
                                            packageState2 = packageState4;
                                            libraryNames = libraryNames2;
                                            targetPackageName2 = targetPackageName3;
                                        } else {
                                            targetPkg = targetPkg2;
                                            packageState2 = packageState4;
                                            libraryNames = libraryNames2;
                                            android.content.pm.overlay.OverlayPaths newOverlayPaths5 = newOverlayPaths4;
                                            numberOfPendingChanges = numberOfPendingChanges2;
                                            targetPackageName2 = targetPackageName3;
                                            try {
                                                android.util.Pair<java.util.List<android.content.pm.VersionedPackage>, java.util.List<java.lang.Boolean>> usingSharedLibraryPair2 = computer.getPackagesUsingSharedLibrary(info, 0L, 1000, userId);
                                                java.util.List<android.content.pm.VersionedPackage> dependents = (java.util.List) usingSharedLibraryPair2.first;
                                                if (dependents == null) {
                                                    computer3 = computer;
                                                    newOverlayPaths2 = newOverlayPaths5;
                                                } else {
                                                    int k = 0;
                                                    android.util.ArraySet<java.lang.String> modifiedDependents = null;
                                                    while (k < dependents.size()) {
                                                        android.content.pm.VersionedPackage dependent = dependents.get(k);
                                                        com.android.server.pm.pkg.PackageStateInternal dependentState = computer.getPackageStateInternal(dependent.getPackageName());
                                                        if (dependentState == null) {
                                                            computer4 = computer;
                                                            usingSharedLibraryPair = usingSharedLibraryPair2;
                                                            newOverlayPaths3 = newOverlayPaths5;
                                                        } else {
                                                            computer4 = computer;
                                                            android.content.pm.overlay.OverlayPaths overlayPaths = dependentState.getUserStateOrDefault(userId).getSharedLibraryOverlayPaths().get(libName);
                                                            usingSharedLibraryPair = usingSharedLibraryPair2;
                                                            newOverlayPaths3 = newOverlayPaths5;
                                                            if (canSetOverlayPaths(overlayPaths, newOverlayPaths3)) {
                                                                java.lang.String dependentPackageName = dependent.getPackageName();
                                                                modifiedDependents = com.android.internal.util.ArrayUtils.add(modifiedDependents, dependentPackageName);
                                                                outUpdatedPackageNames.add(dependentPackageName);
                                                            }
                                                        }
                                                        k++;
                                                        newOverlayPaths5 = newOverlayPaths3;
                                                        usingSharedLibraryPair2 = usingSharedLibraryPair;
                                                        computer = computer4;
                                                    }
                                                    computer3 = computer;
                                                    newOverlayPaths2 = newOverlayPaths5;
                                                    if (modifiedDependents != null) {
                                                        android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> libNameToModifiedDependents = targetPkgToLibNameToModifiedDependents.get(targetPackageName2);
                                                        if (libNameToModifiedDependents == null) {
                                                            libNameToModifiedDependents = new android.util.ArrayMap<>();
                                                            targetPkgToLibNameToModifiedDependents.put(targetPackageName2, libNameToModifiedDependents);
                                                        }
                                                        libNameToModifiedDependents.put(libName, modifiedDependents);
                                                    }
                                                }
                                            } catch (java.lang.Throwable th5) {
                                                th = th5;
                                                while (true) {
                                                    resetPriorityAfterPackageManagerTracedLockedSection();
                                                    throw th;
                                                }
                                            }
                                        }
                                        j = j2 + 1;
                                        newOverlayPaths4 = newOverlayPaths2;
                                        targetPackageName3 = targetPackageName2;
                                        packageState3 = packageState2;
                                        libraryNames2 = libraryNames;
                                        numberOfPendingChanges2 = numberOfPendingChanges;
                                        computer = computer3;
                                        targetPkg2 = targetPkg;
                                    }
                                    computer2 = computer;
                                    newOverlayPaths = newOverlayPaths4;
                                    numberOfPendingChanges = numberOfPendingChanges2;
                                    packageState = packageState3;
                                    targetPackageName = targetPackageName3;
                                } else {
                                    computer2 = computer;
                                    newOverlayPaths = newOverlayPaths4;
                                    numberOfPendingChanges = numberOfPendingChanges2;
                                    packageState = packageState3;
                                    targetPackageName = targetPackageName3;
                                }
                                if (canSetOverlayPaths(packageState.getUserStateOrDefault(userId).getOverlayPaths(), newOverlayPaths)) {
                                    outUpdatedPackageNames.add(targetPackageName);
                                }
                            }
                            i++;
                            arrayMap = pendingChanges;
                            numberOfPendingChanges2 = numberOfPendingChanges;
                            computer = computer2;
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                            i2 = numberOfPendingChanges2;
                            while (true) {
                                resetPriorityAfterPackageManagerTracedLockedSection();
                                throw th;
                            }
                        }
                    }
                } catch (java.lang.Throwable th7) {
                    th = th7;
                    i2 = numberOfPendingChanges2;
                }
                while (true) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            final int numberOfPendingChanges3 = numberOfPendingChanges2;
            try {
                commitPackageStateMutation(null, new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.pm.PackageManagerService.lambda$setEnabledOverlayPackages$57(numberOfPendingChanges3, pendingChanges, outUpdatedPackageNames, userId, targetPkgToLibNameToModifiedDependents, (com.android.server.pm.pkg.mutate.PackageStateMutator) obj);
                    }
                });
                resetPriorityAfterPackageManagerTracedLockedSection();
                if (userId == 0) {
                    int i3 = 0;
                    while (true) {
                        int numberOfPendingChanges4 = numberOfPendingChanges3;
                        if (i3 >= numberOfPendingChanges4) {
                            break;
                        }
                        maybeUpdateSystemOverlays(pendingChanges.keyAt(i3), pendingChanges.valueAt(i3));
                        i3++;
                        numberOfPendingChanges3 = numberOfPendingChanges4;
                    }
                }
                invalidatePackageInfoCache();
            } catch (java.lang.Throwable th8) {
                th = th8;
            }
        }
    }

    static /* synthetic */ void lambda$setEnabledOverlayPackages$57(int numberOfPendingChanges, android.util.ArrayMap pendingChanges, java.util.Set outUpdatedPackageNames, int userId, android.util.ArrayMap targetPkgToLibNameToModifiedDependents, com.android.server.pm.pkg.mutate.PackageStateMutator mutator) {
        for (int i = 0; i < numberOfPendingChanges; i++) {
            java.lang.String targetPackageName = (java.lang.String) pendingChanges.keyAt(i);
            android.content.pm.overlay.OverlayPaths newOverlayPaths = (android.content.pm.overlay.OverlayPaths) pendingChanges.valueAt(i);
            if (outUpdatedPackageNames.contains(targetPackageName)) {
                mutator.forPackage(targetPackageName).userState(userId).setOverlayPaths(newOverlayPaths);
                android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> libNameToModifiedDependents = (android.util.ArrayMap) targetPkgToLibNameToModifiedDependents.get(targetPackageName);
                if (libNameToModifiedDependents != null) {
                    for (int mapIndex = 0; mapIndex < libNameToModifiedDependents.size(); mapIndex++) {
                        java.lang.String libName = libNameToModifiedDependents.keyAt(mapIndex);
                        android.util.ArraySet<java.lang.String> modifiedDependents = libNameToModifiedDependents.valueAt(mapIndex);
                        for (int setIndex = 0; setIndex < modifiedDependents.size(); setIndex++) {
                            mutator.forPackage(modifiedDependents.valueAt(setIndex)).userState(userId).setOverlayPathsForLibrary(libName, newOverlayPaths);
                        }
                    }
                }
            }
        }
    }

    private boolean canSetOverlayPaths(android.content.pm.overlay.OverlayPaths origPaths, android.content.pm.overlay.OverlayPaths newPaths) {
        if (java.util.Objects.equals(origPaths, newPaths)) {
            return false;
        }
        return ((origPaths == null && newPaths.isEmpty()) || (newPaths == null && origPaths.isEmpty())) ? false : true;
    }

    private void maybeUpdateSystemOverlays(java.lang.String targetPackageName, android.content.pm.overlay.OverlayPaths newOverlayPaths) {
        if (!this.mResolverReplaced) {
            if (targetPackageName.equals(PLATFORM_PACKAGE_NAME)) {
                if (newOverlayPaths == null) {
                    this.mPlatformPackageOverlayPaths = null;
                    this.mPlatformPackageOverlayResourceDirs = null;
                } else {
                    this.mPlatformPackageOverlayPaths = (java.lang.String[]) newOverlayPaths.getOverlayPaths().toArray(new java.lang.String[0]);
                    this.mPlatformPackageOverlayResourceDirs = (java.lang.String[]) newOverlayPaths.getResourceDirs().toArray(new java.lang.String[0]);
                }
                applyUpdatedSystemOverlayPaths();
                return;
            }
            return;
        }
        if (targetPackageName.equals(this.mResolveActivity.applicationInfo.packageName)) {
            if (newOverlayPaths == null) {
                this.mReplacedResolverPackageOverlayPaths = null;
                this.mReplacedResolverPackageOverlayResourceDirs = null;
            } else {
                this.mReplacedResolverPackageOverlayPaths = (java.lang.String[]) newOverlayPaths.getOverlayPaths().toArray(new java.lang.String[0]);
                this.mReplacedResolverPackageOverlayResourceDirs = (java.lang.String[]) newOverlayPaths.getResourceDirs().toArray(new java.lang.String[0]);
            }
            applyUpdatedSystemOverlayPaths();
        }
    }

    private void applyUpdatedSystemOverlayPaths() {
        if (this.mAndroidApplication == null) {
            android.util.Slog.i(TAG, "Skipped the AndroidApplication overlay paths update - no app yet");
        } else {
            this.mAndroidApplication.overlayPaths = this.mPlatformPackageOverlayPaths;
            this.mAndroidApplication.resourceDirs = this.mPlatformPackageOverlayResourceDirs;
        }
        if (this.mResolverReplaced) {
            this.mResolveActivity.applicationInfo.overlayPaths = this.mReplacedResolverPackageOverlayPaths;
            this.mResolveActivity.applicationInfo.resourceDirs = this.mReplacedResolverPackageOverlayResourceDirs;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceAdjustRuntimePermissionsPolicyOrUpgradeRuntimePermissions(java.lang.String message) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.UPGRADE_RUNTIME_PERMISSIONS") != 0) {
            throw new java.lang.SecurityException(message + " requires android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY or android.permission.UPGRADE_RUNTIME_PERMISSIONS");
        }
    }

    @java.lang.Deprecated
    com.android.server.pm.PackageSetting getPackageSettingForMutation(java.lang.String packageName) {
        return this.mSettings.getPackageLPr(packageName);
    }

    @java.lang.Deprecated
    com.android.server.pm.PackageSetting getDisabledPackageSettingForMutation(java.lang.String packageName) {
        return this.mSettings.getDisabledSystemPkgLPr(packageName);
    }

    @java.lang.Deprecated
    void forEachPackageSetting(java.util.function.Consumer<com.android.server.pm.PackageSetting> actionLocked) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int size = this.mSettings.getPackagesLocked().size();
                for (int index = 0; index < size; index++) {
                    actionLocked.accept(this.mSettings.getPackagesLocked().valueAt(index));
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
    }

    void forEachPackageState(com.android.server.pm.Computer snapshot, java.util.function.Consumer<com.android.server.pm.pkg.PackageStateInternal> consumer) {
        forEachPackageState(snapshot.getPackageStates(), consumer);
    }

    void forEachPackage(com.android.server.pm.Computer snapshot, java.util.function.Consumer<com.android.server.pm.pkg.AndroidPackage> consumer) {
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = snapshot.getPackageStates();
        int size = packageStates.size();
        for (int index = 0; index < size; index++) {
            com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.valueAt(index);
            if (packageState.getPkg() != null) {
                consumer.accept(packageState.getPkg());
            }
        }
    }

    void forEachPackageInternal(com.android.server.pm.Computer snapshot, java.util.function.Consumer<com.android.internal.pm.parsing.pkg.AndroidPackageInternal> consumer) {
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = snapshot.getPackageStates();
        int size = packageStates.size();
        for (int index = 0; index < size; index++) {
            com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.valueAt(index);
            if (packageState.getPkg() != null) {
                consumer.accept(packageState.getPkg());
            }
        }
    }

    private void forEachPackageState(android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates, java.util.function.Consumer<com.android.server.pm.pkg.PackageStateInternal> consumer) {
        int size = packageStates.size();
        for (int index = 0; index < size; index++) {
            com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.valueAt(index);
            consumer.accept(packageState);
        }
    }

    void forEachInstalledPackage(com.android.server.pm.Computer snapshot, final java.util.function.Consumer<com.android.server.pm.pkg.AndroidPackage> action, final int userId) {
        java.util.function.Consumer<com.android.server.pm.pkg.PackageStateInternal> actionWrapped = new java.util.function.Consumer() { // from class: com.android.server.pm.PackageManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.PackageManagerService.lambda$forEachInstalledPackage$58(userId, action, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        };
        forEachPackageState(snapshot.getPackageStates(), actionWrapped);
    }

    static /* synthetic */ void lambda$forEachInstalledPackage$58(int userId, java.util.function.Consumer action, com.android.server.pm.pkg.PackageStateInternal packageState) {
        if (packageState.getPkg() != null && packageState.getUserStateOrDefault(userId).isInstalled()) {
            action.accept(packageState.getPkg());
        }
    }

    boolean isHistoricalPackageUsageAvailable() {
        return this.mPackageUsage.isHistoricalPackageUsageAvailable();
    }

    public com.android.server.pm.CompilerStats.PackageStats getOrCreateCompilerPackageStats(com.android.server.pm.pkg.AndroidPackage pkg) {
        return getOrCreateCompilerPackageStats(pkg.getPackageName());
    }

    public com.android.server.pm.CompilerStats.PackageStats getOrCreateCompilerPackageStats(java.lang.String pkgName) {
        return this.mCompilerStats.getOrCreatePackageStats(pkgName);
    }

    void grantImplicitAccess(com.android.server.pm.Computer snapshot, int userId, android.content.Intent intent, int recipientAppId, int visibleUid, boolean direct, boolean retainOnUpdate) {
        boolean accessGranted;
        com.android.server.pm.pkg.AndroidPackage visiblePackage = snapshot.getPackage(visibleUid);
        int recipientUid = android.os.UserHandle.getUid(userId, recipientAppId);
        if (visiblePackage == null || snapshot.getPackage(recipientUid) == null) {
            return;
        }
        boolean instantApp = snapshot.isInstantAppInternal(visiblePackage.getPackageName(), userId, visibleUid);
        if (instantApp) {
            if (!direct) {
                return;
            } else {
                accessGranted = this.mInstantAppRegistry.grantInstantAccess(userId, intent, recipientAppId, android.os.UserHandle.getAppId(visibleUid));
            }
        } else {
            accessGranted = this.mAppsFilter.grantImplicitAccess(recipientUid, visibleUid, retainOnUpdate);
        }
        if (accessGranted) {
            android.app.ApplicationPackageManager.invalidateGetPackagesForUidCache();
        }
    }

    boolean canHaveOatDir(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
        if (packageState == null || packageState.getPkg() == null) {
            return false;
        }
        return com.android.server.pm.parsing.pkg.AndroidPackageUtils.canHaveOatDir(packageState, packageState.getPkg());
    }

    long deleteOatArtifactsOfPackage(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRootOrShell("Only the system or shell can delete oat artifacts");
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
        try {
            try {
                com.android.server.art.model.DeleteResult res = com.android.server.pm.DexOptHelper.getArtManagerLocal().deleteDexoptArtifacts(filteredSnapshot, packageName);
                long freedBytes = res.getFreedBytes();
                if (filteredSnapshot != null) {
                    filteredSnapshot.close();
                }
                return freedBytes;
            } catch (java.lang.Throwable th) {
                if (filteredSnapshot != null) {
                    try {
                        filteredSnapshot.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(TAG, e.toString());
            if (filteredSnapshot != null) {
                filteredSnapshot.close();
            }
            return -1L;
        } catch (java.lang.IllegalStateException e2) {
            android.util.Slog.wtfStack(TAG, e2.toString());
            if (filteredSnapshot != null) {
                filteredSnapshot.close();
            }
            return -1L;
        }
    }

    java.util.List<java.lang.String> getMimeGroupInternal(com.android.server.pm.Computer snapshot, java.lang.String packageName, java.lang.String mimeGroup) {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
        if (packageState == null) {
            return java.util.Collections.emptyList();
        }
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mimeGroups = packageState.getMimeGroups();
        java.util.Set<java.lang.String> mimeTypes = mimeGroups != null ? mimeGroups.get(mimeGroup) : null;
        if (mimeTypes == null) {
            throw new java.lang.IllegalArgumentException("Unknown MIME group " + mimeGroup + " for package " + packageName);
        }
        return new java.util.ArrayList(mimeTypes);
    }

    void writeSettingsLPrTEMP(boolean sync) throws java.lang.Throwable {
        if (this.mHandler.hasMessages(13)) {
            this.mHandler.removeMessages(13);
        }
        snapshotComputer(false);
        this.mPermissionManager.writeLegacyPermissionsTEMP(this.mSettings.mPermissions);
        this.mSettings.writeLPr(this.mLiveComputer, sync);
        synchronized (this.mDirtyUsers) {
            this.mDirtyUsers.clear();
        }
    }

    void writeSettingsLPrTEMP() throws java.lang.Throwable {
        writeSettingsLPrTEMP(false);
    }

    @Override // android.content.pm.TestUtilityService
    public void verifyHoldLockToken(android.os.IBinder token) {
        if (!android.os.Build.IS_DEBUGGABLE) {
            throw new java.lang.SecurityException("holdLock requires a debuggable build");
        }
        if (token == null) {
            throw new java.lang.SecurityException("null holdLockToken");
        }
        if (token.queryLocalInterface("holdLock:" + android.os.Binder.getCallingUid()) != this) {
            throw new java.lang.SecurityException("Invalid holdLock() token");
        }
    }

    static java.lang.String getDefaultTimeouts() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getString("package_manager_service", PROPERTY_INCFS_DEFAULT_TIMEOUTS, "");
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    static java.lang.String getKnownDigestersList() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getString("package_manager_service", PROPERTY_KNOWN_DIGESTERS_LIST, "");
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    static boolean isPreapprovalRequestAvailable() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_handleVolumeAliasesUsingVolumeGroups)) {
                return android.provider.DeviceConfig.getBoolean("package_manager_service", PROPERTY_IS_PRE_APPROVAL_REQUEST_AVAILABLE, true);
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    static boolean isUpdateOwnershipEnforcementAvailable() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getBoolean("package_manager_service", PROPERTY_IS_UPDATE_OWNERSHIP_ENFORCEMENT_AVAILABLE, false);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public android.os.incremental.PerUidReadTimeouts[] getPerUidReadTimeouts(com.android.server.pm.Computer snapshot) {
        android.os.incremental.PerUidReadTimeouts[] result = this.mPerUidReadTimeoutsCache;
        if (result == null) {
            android.os.incremental.PerUidReadTimeouts[] result2 = parsePerUidReadTimeouts(snapshot);
            this.mPerUidReadTimeoutsCache = result2;
            return result2;
        }
        return result;
    }

    private android.os.incremental.PerUidReadTimeouts[] parsePerUidReadTimeouts(com.android.server.pm.Computer snapshot) {
        java.lang.String defaultTimeouts;
        java.lang.String knownDigestersList;
        java.util.List<com.android.server.pm.PerPackageReadTimeouts> perPackageReadTimeouts;
        java.lang.String defaultTimeouts2 = getDefaultTimeouts();
        java.lang.String knownDigestersList2 = getKnownDigestersList();
        java.util.List<com.android.server.pm.PerPackageReadTimeouts> perPackageReadTimeouts2 = com.android.server.pm.PerPackageReadTimeouts.parseDigestersList(defaultTimeouts2, knownDigestersList2);
        if (perPackageReadTimeouts2.size() == 0) {
            return EMPTY_PER_UID_READ_TIMEOUTS_ARRAY;
        }
        int[] allUsers = this.mInjector.getUserManagerService().getUserIds();
        java.util.List<android.os.incremental.PerUidReadTimeouts> result = new java.util.ArrayList<>(perPackageReadTimeouts2.size());
        int i = 0;
        int size = perPackageReadTimeouts2.size();
        while (i < size) {
            com.android.server.pm.PerPackageReadTimeouts perPackage = perPackageReadTimeouts2.get(i);
            com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateInternal(perPackage.packageName);
            if (ps == null) {
                if (DEBUG_PER_UID_READ_TIMEOUTS) {
                    android.util.Slog.i(TAG, "PerUidReadTimeouts: package not found = " + perPackage.packageName);
                    defaultTimeouts = defaultTimeouts2;
                    knownDigestersList = knownDigestersList2;
                    perPackageReadTimeouts = perPackageReadTimeouts2;
                } else {
                    defaultTimeouts = defaultTimeouts2;
                    knownDigestersList = knownDigestersList2;
                    perPackageReadTimeouts = perPackageReadTimeouts2;
                }
            } else if (ps.getAppId() < 10000) {
                if (!DEBUG_PER_UID_READ_TIMEOUTS) {
                    defaultTimeouts = defaultTimeouts2;
                    knownDigestersList = knownDigestersList2;
                    perPackageReadTimeouts = perPackageReadTimeouts2;
                } else {
                    android.util.Slog.i(TAG, "PerUidReadTimeouts: package is system, appId=" + ps.getAppId());
                    defaultTimeouts = defaultTimeouts2;
                    knownDigestersList = knownDigestersList2;
                    perPackageReadTimeouts = perPackageReadTimeouts2;
                }
            } else {
                com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
                defaultTimeouts = defaultTimeouts2;
                knownDigestersList = knownDigestersList2;
                if (pkg.getLongVersionCode() >= perPackage.versionCodes.minVersionCode) {
                    perPackageReadTimeouts = perPackageReadTimeouts2;
                    if (pkg.getLongVersionCode() <= perPackage.versionCodes.maxVersionCode) {
                        if (perPackage.sha256certificate == null || pkg.getSigningDetails().hasSha256Certificate(perPackage.sha256certificate)) {
                            for (int userId : allUsers) {
                                if (ps.getUserStateOrDefault(userId).isInstalled()) {
                                    int uid = android.os.UserHandle.getUid(userId, ps.getAppId());
                                    android.os.incremental.PerUidReadTimeouts perUid = new android.os.incremental.PerUidReadTimeouts();
                                    perUid.uid = uid;
                                    perUid.minTimeUs = perPackage.timeouts.minTimeUs;
                                    perUid.minPendingTimeUs = perPackage.timeouts.minPendingTimeUs;
                                    perUid.maxPendingTimeUs = perPackage.timeouts.maxPendingTimeUs;
                                    result.add(perUid);
                                }
                            }
                        } else if (DEBUG_PER_UID_READ_TIMEOUTS) {
                            android.util.Slog.i(TAG, "PerUidReadTimeouts: invalid certificate = " + perPackage.packageName + ":" + pkg.getLongVersionCode());
                        }
                    }
                } else {
                    perPackageReadTimeouts = perPackageReadTimeouts2;
                }
                if (DEBUG_PER_UID_READ_TIMEOUTS) {
                    android.util.Slog.i(TAG, "PerUidReadTimeouts: version code is not in range = " + perPackage.packageName + ":" + pkg.getLongVersionCode());
                }
            }
            i++;
            perPackageReadTimeouts2 = perPackageReadTimeouts;
            defaultTimeouts2 = defaultTimeouts;
            knownDigestersList2 = knownDigestersList;
        }
        return (android.os.incremental.PerUidReadTimeouts[]) result.toArray(new android.os.incremental.PerUidReadTimeouts[result.size()]);
    }

    void setKeepUninstalledPackagesInternal(com.android.server.pm.Computer snapshot, java.util.List<java.lang.String> packageList) {
        com.android.internal.util.Preconditions.checkNotNull(packageList);
        synchronized (this.mKeepUninstalledPackages) {
            java.util.List<java.lang.String> toRemove = new java.util.ArrayList<>(this.mKeepUninstalledPackages);
            toRemove.removeAll(packageList);
            this.mKeepUninstalledPackages.clear();
            this.mKeepUninstalledPackages.addAll(packageList);
            for (int i = 0; i < toRemove.size(); i++) {
                deletePackageIfUnused(snapshot, toRemove.get(i));
            }
        }
    }

    boolean shouldKeepUninstalledPackageLPr(java.lang.String packageName) {
        boolean zContains;
        synchronized (this.mKeepUninstalledPackages) {
            zContains = this.mKeepUninstalledPackages.contains(packageName);
        }
        return zContains;
    }

    boolean getSafeMode() {
        return this.mSafeMode;
    }

    android.content.ComponentName getResolveComponentName() {
        return this.mResolveComponentName;
    }

    com.android.server.pm.DefaultAppProvider getDefaultAppProvider() {
        return this.mDefaultAppProvider;
    }

    java.io.File getCacheDir() {
        return this.mCacheDir;
    }

    com.android.server.pm.PackageProperty getPackageProperty() {
        return this.mPackageProperty;
    }

    com.android.server.utils.WatchedArrayMap<android.content.ComponentName, com.android.internal.pm.pkg.component.ParsedInstrumentation> getInstrumentation() {
        return this.mInstrumentation;
    }

    int getSdkVersion() {
        return this.mSdkVersion;
    }

    void addAllPackageProperties(com.android.server.pm.pkg.AndroidPackage pkg) {
        this.mPackageProperty.addAllProperties(pkg);
    }

    void addInstrumentation(android.content.ComponentName name, com.android.internal.pm.pkg.component.ParsedInstrumentation instrumentation) {
        this.mInstrumentation.put(name, instrumentation);
    }

    java.lang.String[] getKnownPackageNamesInternal(com.android.server.pm.Computer snapshot, int knownPackage, int userId) {
        return new com.android.server.pm.KnownPackages(this.mDefaultAppProvider, this.mRequiredInstallerPackage, this.mRequiredUninstallerPackage, this.mSetupWizardPackage, this.mRequiredVerifierPackages, this.mDefaultTextClassifierPackage, this.mSystemTextClassifierPackageName, this.mRequiredPermissionControllerPackage, this.mConfiguratorPackage, this.mIncidentReportApproverPackage, this.mAmbientContextDetectionPackage, this.mWearableSensingPackage, this.mAppPredictionServicePackage, COMPANION_PACKAGE_NAME, this.mRetailDemoPackage, this.mOverlayConfigSignaturePackage, this.mRecentsPackage).getKnownPackageNames(snapshot, knownPackage, userId);
    }

    java.lang.String getActiveLauncherPackageName(int userId) {
        return this.mDefaultAppProvider.getDefaultHome(userId);
    }

    boolean setActiveLauncherPackage(java.lang.String packageName, int userId, java.util.function.Consumer<java.lang.Boolean> callback) {
        return this.mDefaultAppProvider.setDefaultHome(packageName, userId, this.mContext.getMainExecutor(), callback);
    }

    java.lang.String getDefaultBrowser(int userId) {
        return this.mDefaultAppProvider.getDefaultBrowser(userId);
    }

    void setDefaultBrowser(java.lang.String packageName, int userId) {
        this.mDefaultAppProvider.setDefaultBrowser(packageName, userId);
    }

    com.android.server.pm.PackageUsage getPackageUsage() {
        return this.mPackageUsage;
    }

    java.lang.String getModuleMetadataPackageName() {
        return this.mModuleInfoProvider.getPackageName();
    }

    java.io.File getAppInstallDir() {
        return this.mAppInstallDir;
    }

    boolean isExpectingBetter(java.lang.String packageName) {
        return this.mInitAppsHelper.isExpectingBetter(packageName);
    }

    int getDefParseFlags() {
        return this.mDefParseFlags;
    }

    void setUpCustomResolverActivity(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting pkgSetting) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mResolverReplaced = true;
                android.content.pm.ApplicationInfo appInfo = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(pkg, 0L, com.android.server.pm.pkg.PackageUserStateInternal.DEFAULT, 0, pkgSetting);
                this.mResolveActivity.applicationInfo = appInfo;
                this.mResolveActivity.name = this.mCustomResolverComponentName.getClassName();
                this.mResolveActivity.packageName = pkg.getPackageName();
                this.mResolveActivity.processName = pkg.getProcessName();
                this.mResolveActivity.launchMode = 0;
                this.mResolveActivity.flags = 66336;
                this.mResolveActivity.theme = 0;
                this.mResolveActivity.exported = true;
                this.mResolveActivity.enabled = true;
                this.mResolveInfo.activityInfo = this.mResolveActivity;
                this.mResolveInfo.priority = 0;
                this.mResolveInfo.preferredOrder = 0;
                this.mResolveInfo.match = 0;
                this.mResolveComponentName = this.mCustomResolverComponentName;
                onChanged();
                android.util.Slog.i(TAG, "Replacing default ResolverActivity with custom activity: " + this.mResolveComponentName);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
    }

    void setPlatformPackage(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting pkgSetting) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPlatformPackage = pkg;
                this.mAndroidApplication = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(pkg, 0L, com.android.server.pm.pkg.PackageUserStateInternal.DEFAULT, 0, pkgSetting);
                if (!this.mResolverReplaced) {
                    this.mResolveActivity.applicationInfo = this.mAndroidApplication;
                    this.mResolveActivity.name = com.android.internal.app.ResolverActivity.class.getName();
                    this.mResolveActivity.packageName = this.mAndroidApplication.packageName;
                    this.mResolveActivity.processName = "system:ui";
                    this.mResolveActivity.launchMode = 0;
                    this.mResolveActivity.documentLaunchMode = 3;
                    this.mResolveActivity.flags = 70176;
                    this.mResolveActivity.theme = android.R.style.Theme.Material.Dialog.Alert;
                    this.mResolveActivity.exported = true;
                    this.mResolveActivity.enabled = true;
                    this.mResolveActivity.resizeMode = 2;
                    this.mResolveActivity.configChanges = 3504;
                    this.mResolveInfo.activityInfo = this.mResolveActivity;
                    this.mResolveInfo.priority = 0;
                    this.mResolveInfo.preferredOrder = 0;
                    this.mResolveInfo.match = 0;
                    this.mResolveComponentName = new android.content.ComponentName(this.mAndroidApplication.packageName, this.mResolveActivity.name);
                }
                onChanged();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
        applyUpdatedSystemOverlayPaths();
    }

    android.content.pm.ApplicationInfo getCoreAndroidApplication() {
        return this.mAndroidApplication;
    }

    boolean isSystemReady() {
        return this.mSystemReady;
    }

    com.android.server.pm.pkg.AndroidPackage getPlatformPackage() {
        return this.mPlatformPackage;
    }

    boolean isPreNMR1Upgrade() {
        return this.mIsPreNMR1Upgrade;
    }

    boolean isOverlayMutable(java.lang.String packageName) {
        return this.mOverlayConfig.isMutable(packageName);
    }

    int getSystemPackageScanFlags(java.io.File codePath) {
        java.util.List<com.android.server.pm.ScanPartition> dirsToScanAsSystem = this.mInitAppsHelper.getDirsToScanAsSystem();
        for (int i = dirsToScanAsSystem.size() - 1; i >= 0; i--) {
            com.android.server.pm.ScanPartition partition = dirsToScanAsSystem.get(i);
            if (partition.containsFile(codePath)) {
                int scanFlags = 65536 | partition.scanFlag;
                if (partition.containsPrivApp(codePath)) {
                    return scanFlags | 131072;
                }
                return scanFlags;
            }
        }
        return 65536;
    }

    android.util.Pair<java.lang.Integer, java.lang.Integer> getSystemPackageRescanFlagsAndReparseFlags(java.io.File scanFile, int systemScanFlags, int systemParseFlags) {
        java.util.List<com.android.server.pm.ScanPartition> dirsToScanAsSystem = this.mInitAppsHelper.getDirsToScanAsSystem();
        int reparseFlags = 0;
        int rescanFlags = 0;
        int i1 = dirsToScanAsSystem.size() - 1;
        while (true) {
            if (i1 < 0) {
                break;
            }
            com.android.server.pm.ScanPartition partition = dirsToScanAsSystem.get(i1);
            if (partition.containsPrivApp(scanFile)) {
                reparseFlags = systemParseFlags;
                rescanFlags = 131072 | systemScanFlags | partition.scanFlag;
                break;
            }
            if (!partition.containsApp(scanFile)) {
                i1--;
            } else {
                reparseFlags = systemParseFlags;
                rescanFlags = systemScanFlags | partition.scanFlag;
                break;
            }
        }
        return new android.util.Pair<>(java.lang.Integer.valueOf(rescanFlags), java.lang.Integer.valueOf(reparseFlags));
    }

    public com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState recordInitialState() {
        return this.mPackageStateMutator.initialState(this.mChangedPackagesTracker.getSequenceNumber());
    }

    public com.android.server.pm.pkg.mutate.PackageStateMutator.Result commitPackageStateMutation(com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState initialState, java.util.function.Consumer<com.android.server.pm.pkg.mutate.PackageStateMutator> consumer) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPackageStateWriteLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.pkg.mutate.PackageStateMutator.Result result = this.mPackageStateMutator.generateResult(initialState, this.mChangedPackagesTracker.getSequenceNumber());
                if (result != com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SUCCESS) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    return result;
                }
                consumer.accept(this.mPackageStateMutator);
                this.mPackageStateMutator.onFinished();
                resetPriorityAfterPackageManagerTracedLockedSection();
                return com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SUCCESS;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    public com.android.server.pm.pkg.mutate.PackageStateMutator.Result commitPackageStateMutation(com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState initialState, java.lang.String packageName, java.util.function.Consumer<com.android.server.pm.pkg.mutate.PackageStateWrite> consumer) {
        com.android.server.pm.pkg.mutate.PackageStateMutator.Result result = null;
        if (java.lang.Thread.holdsLock(this.mPackageStateWriteLock)) {
            result = com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SUCCESS;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPackageStateWriteLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            if (result == null) {
                try {
                    result = this.mPackageStateMutator.generateResult(initialState, this.mChangedPackagesTracker.getSequenceNumber());
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            if (result != com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SUCCESS) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                return result;
            }
            com.android.server.pm.pkg.mutate.PackageStateWrite state = this.mPackageStateMutator.forPackage(packageName);
            if (state == null) {
                com.android.server.pm.pkg.mutate.PackageStateMutator.Result result2 = com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SPECIFIC_PACKAGE_NULL;
                resetPriorityAfterPackageManagerTracedLockedSection();
                return result2;
            }
            consumer.accept(state);
            state.onChanged();
            resetPriorityAfterPackageManagerTracedLockedSection();
            return com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SUCCESS;
        }
    }

    void notifyInstantAppPackageInstalled(java.lang.String packageName, int[] newUsers) {
        this.mInstantAppRegistry.onPackageInstalled(snapshotComputer(), packageName, newUsers);
    }

    void addInstallerPackageName(com.android.server.pm.InstallSource installSource) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mSettings.addInstallerPackageNames(installSource);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public void reconcileSdkData(java.lang.String volumeUuid, java.lang.String packageName, java.util.List<java.lang.String> subDirNames, int userId, int appId, int previousAppId, java.lang.String seInfo, int flags) throws java.io.IOException {
        android.os.ReconcileSdkDataArgs args = com.android.server.pm.Installer.buildReconcileSdkDataArgs(volumeUuid, packageName, subDirNames, userId, appId, seInfo, flags);
        args.previousAppId = previousAppId;
        try {
            com.android.server.pm.PackageManagerTracedLock installLock = this.mInstallLock.acquireLock();
            try {
                this.mInstaller.reconcileSdkData(args);
                if (installLock != null) {
                    installLock.close();
                }
            } finally {
            }
        } catch (com.android.server.pm.Installer.InstallerException e) {
            throw new java.io.IOException(e.getMessage());
        }
    }

    void removeCodePath(java.io.File codePath) {
        this.mRemovePackageHelper.removeCodePath(codePath);
    }

    void cleanUpResources(java.lang.String packageName, java.io.File codeFile, java.lang.String[] instructionSets) {
        this.mRemovePackageHelper.cleanUpResources(packageName, codeFile, instructionSets);
    }

    void cleanUpForMoveInstall(java.lang.String volumeUuid, java.lang.String packageName, java.lang.String fromCodePath) {
        this.mRemovePackageHelper.cleanUpForMoveInstall(volumeUuid, packageName, fromCodePath);
    }

    void sendPendingBroadcasts() {
        this.mInstallPackageHelper.sendPendingBroadcasts();
    }

    void handlePackagePostInstall(com.android.server.pm.InstallRequest request, boolean launchedForRestore) {
        this.mInstallPackageHelper.handlePackagePostInstall(request, launchedForRestore);
    }

    android.util.Pair<java.lang.Integer, android.content.IntentSender> installExistingPackageAsUser(java.lang.String packageName, int userId, int installFlags, int installReason, java.util.List<java.lang.String> allowlistedRestrictedPermissions, android.content.IntentSender intentSender) {
        return this.mInstallPackageHelper.installExistingPackageAsUser(packageName, userId, installFlags, installReason, allowlistedRestrictedPermissions, intentSender);
    }

    com.android.server.pm.pkg.AndroidPackage initPackageTracedLI(java.io.File scanFile, int parseFlags, int scanFlags) throws com.android.server.pm.PackageManagerException {
        return this.mInstallPackageHelper.initPackageTracedLI(scanFile, parseFlags, scanFlags);
    }

    void restoreDisabledSystemPackageLIF(com.android.server.pm.DeletePackageAction action, int[] allUserHandles, boolean writeSettings) throws com.android.server.pm.SystemDeleteException {
        this.mInstallPackageHelper.restoreDisabledSystemPackageLIF(action, allUserHandles, writeSettings);
    }

    boolean enableCompressedPackage(com.android.server.pm.pkg.AndroidPackage stubPkg, com.android.server.pm.PackageSetting stubPs) {
        return this.mInstallPackageHelper.enableCompressedPackage(stubPkg, stubPs);
    }

    void installPackagesTraced(java.util.List<com.android.server.pm.InstallRequest> requests) {
        this.mInstallPackageHelper.installPackagesTraced(requests);
    }

    void restoreAndPostInstall(com.android.server.pm.InstallRequest request) {
        this.mInstallPackageHelper.restoreAndPostInstall(request);
    }

    android.util.Pair<java.lang.Integer, java.lang.String> verifyReplacingVersionCode(android.content.pm.PackageInfoLite pkgLite, long requiredInstalledVersionCode, int installFlags) {
        return this.mInstallPackageHelper.verifyReplacingVersionCode(pkgLite, requiredInstalledVersionCode, installFlags);
    }

    int getUidForVerifier(android.content.pm.VerifierInfo verifierInfo) {
        return this.mInstallPackageHelper.getUidForVerifier(verifierInfo);
    }

    int deletePackageX(java.lang.String packageName, long versionCode, int userId, int deleteFlags, boolean removedBySystem) {
        return this.mDeletePackageHelper.deletePackageX(packageName, -1L, 0, 2, true);
    }

    public com.android.server.pm.IPackageManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    private class PackageManagerServiceWrapper implements com.android.server.pm.IPackageManagerServiceWrapper {
        private PackageManagerServiceWrapper() {
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.PackageDexOptimizer getPackageDexOptimizer() {
            return com.android.server.pm.PackageManagerService.this.mPackageDexOptimizer;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.dex.DexManager getDexManager() {
            return com.android.server.pm.PackageManagerService.this.mDexManager;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManager() {
            return com.android.server.pm.PackageManagerService.this.mPermissionManager;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.ApexManager getApexManager() {
            return com.android.server.pm.PackageManagerService.this.mApexManager;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.AppDataHelper getAppDataHelper() {
            return com.android.server.pm.PackageManagerService.this.mAppDataHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.DexOptHelper getDexOptHelper() {
            return com.android.server.pm.PackageManagerService.this.mDexOptHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.InitAppsHelper getInitAppsHelper() {
            return com.android.server.pm.PackageManagerService.this.mInitAppsHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.ResolveIntentHelper getResolveIntentHelper() {
            return com.android.server.pm.PackageManagerService.this.mResolveIntentHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.RemovePackageHelper getRemovePackageHelper() {
            return com.android.server.pm.PackageManagerService.this.mRemovePackageHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.InstallPackageHelper getInstallPackageHelper() {
            return com.android.server.pm.PackageManagerService.this.mInstallPackageHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.BroadcastHelper getBroadcastHelper() {
            return com.android.server.pm.PackageManagerService.this.mBroadcastHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.PackageMonitorCallbackHelper getPackageMonitorCallbackHelper() {
            return com.android.server.pm.PackageManagerService.this.mPackageMonitorCallbackHelper;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.SharedLibrariesImpl getSharedLibraries() {
            return com.android.server.pm.PackageManagerService.this.mSharedLibraries;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.ComputerLocked getLiveComputer() {
            return com.android.server.pm.PackageManagerService.this.mLiveComputer;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.AppsFilterImpl getAppsFilter() {
            return com.android.server.pm.PackageManagerService.this.mAppsFilter;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.pm.resolution.ComponentResolver getComponentResolver() {
            return com.android.server.pm.PackageManagerService.this.mComponentResolver;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> getPackages() {
            return com.android.server.pm.PackageManagerService.this.mPackages;
        }

        @Override // com.android.server.pm.IPackageManagerServiceWrapper
        public java.io.File getCacheDir() {
            return com.android.server.pm.PackageManagerService.this.mCacheDir;
        }
    }
}
