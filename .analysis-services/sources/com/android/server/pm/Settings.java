package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class Settings implements com.android.server.utils.Watchable, com.android.server.utils.Snappable, com.android.server.pm.ResilientAtomicFile.ReadEventLogger {
    private static final java.lang.String ATTR_APP_LINK_GENERATION = "app-link-generation";
    private static final java.lang.String ATTR_ARCHIVE_ACTIVITY_TITLE = "activity-title";
    private static final java.lang.String ATTR_ARCHIVE_ICON_PATH = "icon-path";
    private static final java.lang.String ATTR_ARCHIVE_INSTALLER_TITLE = "installer-title";
    private static final java.lang.String ATTR_ARCHIVE_MONOCHROME_ICON_PATH = "monochrome-icon-path";
    private static final java.lang.String ATTR_ARCHIVE_ORIGINAL_COMPONENT_NAME = "original-component-name";
    private static final java.lang.String ATTR_ARCHIVE_TIME = "archive-time";
    private static final java.lang.String ATTR_BLOCKED = "blocked";

    @java.lang.Deprecated
    private static final java.lang.String ATTR_BLOCK_UNINSTALL = "blockUninstall";
    private static final java.lang.String ATTR_BUILD_FINGERPRINT = "buildFingerprint";
    private static final java.lang.String ATTR_CE_DATA_INODE = "ceDataInode";
    private static final java.lang.String ATTR_DATABASE_VERSION = "databaseVersion";
    private static final java.lang.String ATTR_DE_DATA_INODE = "deDataInode";
    private static final java.lang.String ATTR_DISTRACTION_FLAGS = "distraction_flags";
    private static final java.lang.String ATTR_DOMAIN_VERIFICATION_STATE = "domainVerificationStatus";
    private static final java.lang.String ATTR_ENABLED = "enabled";
    private static final java.lang.String ATTR_ENABLED_CALLER = "enabledCaller";
    private static final java.lang.String ATTR_ENFORCEMENT = "enforcement";
    private static final java.lang.String ATTR_FINGERPRINT = "fingerprint";
    private static final java.lang.String ATTR_FIRST_INSTALL_TIME = "first-install-time";
    private static final java.lang.String ATTR_FLAGS = "flags";
    private static final java.lang.String ATTR_GRANTED = "granted";
    private static final java.lang.String ATTR_HARMFUL_APP_WARNING = "harmful-app-warning";
    private static final java.lang.String ATTR_HIDDEN = "hidden";
    private static final java.lang.String ATTR_INSTALLED = "inst";
    private static final java.lang.String ATTR_INSTALL_REASON = "install-reason";
    private static final java.lang.String ATTR_INSTANT_APP = "instant-app";
    private static final java.lang.String ATTR_MIN_ASPECT_RATIO = "min-aspect-ratio";
    public static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_NOT_LAUNCHED = "nl";
    private static final java.lang.String ATTR_OPTIONAL = "optional";
    public static final java.lang.String ATTR_PACKAGE = "package";
    private static final java.lang.String ATTR_PACKAGE_NAME = "packageName";
    private static final java.lang.String ATTR_SDK_VERSION = "sdkVersion";
    private static final java.lang.String ATTR_SPLASH_SCREEN_THEME = "splash-screen-theme";
    private static final java.lang.String ATTR_STOPPED = "stopped";
    private static final java.lang.String ATTR_SUSPENDED = "suspended";
    private static final java.lang.String ATTR_SUSPENDING_PACKAGE = "suspending-package";
    private static final java.lang.String ATTR_SUSPENDING_USER = "suspending-user";

    @java.lang.Deprecated
    private static final java.lang.String ATTR_SUSPEND_DIALOG_MESSAGE = "suspend_dialog_message";
    private static final java.lang.String ATTR_UNINSTALL_REASON = "uninstall-reason";
    private static final java.lang.String ATTR_VALUE = "value";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final java.lang.String ATTR_VIRTUAL_PRELOAD = "virtual-preload";
    private static final java.lang.String ATTR_VOLUME_UUID = "volumeUuid";
    public static final int CURRENT_DATABASE_VERSION = 3;
    private static final boolean DEBUG_KERNEL = false;
    private static final boolean DEBUG_MU = false;
    private static final boolean DEBUG_PARSER = false;
    static final boolean DEBUG_STOPPED = false;
    private static final int PRE_M_APP_INFO_FLAG_CANT_SAVE_STATE = 268435456;
    private static final int PRE_M_APP_INFO_FLAG_HIDDEN = 134217728;
    private static final int PRE_M_APP_INFO_FLAG_PRIVILEGED = 1073741824;
    private static final java.lang.String RUNTIME_PERMISSIONS_FILE_NAME = "runtime-permissions.xml";
    private static final java.lang.String TAG = "PackageSettings";
    public static final java.lang.String TAG_ALL_INTENT_FILTER_VERIFICATION = "all-intent-filter-verifications";
    private static final java.lang.String TAG_ARCHIVE_ACTIVITY_INFO = "archive-activity-info";
    private static final java.lang.String TAG_ARCHIVE_STATE = "archive-state";
    private static final java.lang.String TAG_BLOCK_UNINSTALL = "block-uninstall";
    private static final java.lang.String TAG_BLOCK_UNINSTALL_PACKAGES = "block-uninstall-packages";
    private static final java.lang.String TAG_CHILD_PACKAGE = "child-package";
    static final java.lang.String TAG_CROSS_PROFILE_INTENT_FILTERS = "crossProfile-intent-filters";
    private static final java.lang.String TAG_DEFAULT_APPS = "default-apps";
    private static final java.lang.String TAG_DEFAULT_BROWSER = "default-browser";
    private static final java.lang.String TAG_DEFAULT_DIALER = "default-dialer";
    private static final java.lang.String TAG_DISABLED_COMPONENTS = "disabled-components";
    public static final java.lang.String TAG_DOMAIN_VERIFICATION = "domain-verification";
    private static final java.lang.String TAG_ENABLED_COMPONENTS = "enabled-components";
    public static final java.lang.String TAG_ITEM = "item";
    private static final java.lang.String TAG_MIME_GROUP = "mime-group";
    private static final java.lang.String TAG_MIME_TYPE = "mime-type";
    private static final java.lang.String TAG_PACKAGE = "pkg";
    private static final java.lang.String TAG_PACKAGE_RESTRICTIONS = "package-restrictions";
    private static final java.lang.String TAG_PERMISSIONS = "perms";
    private static final java.lang.String TAG_PERSISTENT_PREFERRED_ACTIVITIES = "persistent-preferred-activities";
    private static final java.lang.String TAG_READ_EXTERNAL_STORAGE = "read-external-storage";
    private static final java.lang.String TAG_RUNTIME_PERMISSIONS = "runtime-permissions";
    private static final java.lang.String TAG_SHARED_USER = "shared-user";

    @java.lang.Deprecated
    private static final java.lang.String TAG_SUSPENDED_APP_EXTRAS = "suspended-app-extras";

    @java.lang.Deprecated
    private static final java.lang.String TAG_SUSPENDED_DIALOG_INFO = "suspended-dialog-info";

    @java.lang.Deprecated
    private static final java.lang.String TAG_SUSPENDED_LAUNCHER_EXTRAS = "suspended-launcher-extras";
    private static final java.lang.String TAG_SUSPEND_PARAMS = "suspend-params";
    private static final java.lang.String TAG_USES_SDK_LIB = "uses-sdk-lib";
    private static final java.lang.String TAG_USES_STATIC_LIB = "uses-static-lib";
    private static final java.lang.String TAG_VERSION = "version";

    @com.android.server.utils.Watched(manual = true)
    private final com.android.server.pm.AppIdSettingMap mAppIds;
    private final java.io.File mBackupStoppedPackagesFilename;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseArray<android.util.ArraySet<java.lang.String>> mBlockUninstallPackages;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseArray<com.android.server.pm.CrossProfileIntentResolver> mCrossProfileIntentResolvers;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseArray<com.android.server.pm.CrossProfileIntentResolver>> mCrossProfileIntentResolversSnapshot;

    @com.android.server.utils.Watched
    final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> mDisabledSysPackages;

    @com.android.server.utils.Watched(manual = true)
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;
    private final android.os.Handler mHandler;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArraySet<java.lang.String> mInstallerPackages;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArraySet<java.lang.String>> mInstallerPackagesSnapshot;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.Settings.KernelPackageState> mKernelMapping;
    private final java.io.File mKernelMappingFilename;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.Settings.KernelPackageState>> mKernelMappingSnapshot;
    private final com.android.server.pm.KeySetManagerService mKeySetManagerService;
    private final com.android.server.pm.PackageManagerTracedLock mLock;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseIntArray mNextAppLinkGeneration;
    private final com.android.server.utils.Watcher mObserver;
    private final java.io.File mPackageListFilename;
    private final java.lang.Object mPackageRestrictionsLock;

    @com.android.server.utils.Watched
    final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> mPackages;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting>> mPackagesSnapshot;
    private final android.util.SparseIntArray mPendingAsyncPackageRestrictionsWrites;

    @com.android.server.utils.Watched
    final com.android.server.utils.WatchedSparseArray<java.lang.String> mPendingDefaultBrowser;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArrayList<com.android.server.pm.PackageSetting> mPendingPackages;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayList<com.android.server.pm.PackageSetting>> mPendingPackagesSnapshot;

    @com.android.server.utils.Watched(manual = true)
    private final com.android.server.pm.permission.LegacyPermissionDataProvider mPermissionDataProvider;

    @com.android.server.utils.Watched(manual = true)
    final com.android.server.pm.permission.LegacyPermissionSettings mPermissions;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseArray<com.android.server.pm.PersistentPreferredIntentResolver> mPersistentPreferredActivities;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseArray<com.android.server.pm.PersistentPreferredIntentResolver>> mPersistentPreferredActivitiesSnapshot;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseArray<com.android.server.pm.PreferredIntentResolver> mPreferredActivities;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseArray<com.android.server.pm.PreferredIntentResolver>> mPreferredActivitiesSnapshot;
    private final java.io.File mPreviousSettingsFilename;
    final java.lang.StringBuilder mReadMessages;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, java.lang.String> mRenamedPackages;

    @com.android.server.utils.Watched(manual = true)
    private final com.android.server.pm.Settings.RuntimePermissionPersistence mRuntimePermissionsPersistence;
    public final com.android.server.pm.ISettingsExt mSettingsExt;
    private final java.io.File mSettingsFilename;
    private final java.io.File mSettingsReserveCopyFilename;

    @com.android.server.utils.Watched
    final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> mSharedUsers;
    private final com.android.server.utils.SnapshotCache<com.android.server.pm.Settings> mSnapshot;
    private final java.io.File mStoppedPackagesFilename;
    private final java.io.File mSystemDir;

    @com.android.server.utils.Watched(manual = true)
    private android.content.pm.VerifierDeviceIdentity mVerifierDeviceIdentity;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.Settings.VersionInfo> mVersion;
    private final com.android.server.utils.WatchableImpl mWatchable;
    static final java.lang.Object[] FLAG_DUMP_SPEC = {1, "SYSTEM", 2, "DEBUGGABLE", 4, "HAS_CODE", 8, "PERSISTENT", 16, "FACTORY_TEST", 32, "ALLOW_TASK_REPARENTING", 64, "ALLOW_CLEAR_USER_DATA", 128, "UPDATED_SYSTEM_APP", 256, "TEST_ONLY", 16384, "VM_SAFE_MODE", 32768, "ALLOW_BACKUP", 65536, "KILL_AFTER_RESTORE", 131072, "RESTORE_ANY_VERSION", 262144, "EXTERNAL_STORAGE", 1048576, "LARGE_HEAP"};
    private static final java.lang.Object[] PRIVATE_FLAG_DUMP_SPEC = {1024, "PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE", 4096, "PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION", 2048, "PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_UNRESIZEABLE", 134217728, "ALLOW_AUDIO_PLAYBACK_CAPTURE", 536870912, "PRIVATE_FLAG_REQUEST_LEGACY_EXTERNAL_STORAGE", 8192, "BACKUP_IN_FOREGROUND", 2, "CANT_SAVE_STATE", 32, "DEFAULT_TO_DEVICE_PROTECTED_STORAGE", 64, "DIRECT_BOOT_AWARE", 16, "HAS_DOMAIN_URLS", 1, "HIDDEN", 128, "EPHEMERAL", 32768, "ISOLATED_SPLIT_LOADING", 131072, "OEM", 256, "PARTIALLY_DIRECT_BOOT_AWARE", 8, "PRIVILEGED", 512, "REQUIRED_FOR_SYSTEM_USER", 16384, "STATIC_SHARED_LIBRARY", 262144, "VENDOR", 524288, "PRODUCT", 2097152, "SYSTEM_EXT", 65536, "VIRTUAL_PRELOAD", 1073741824, "ODM", Integer.MIN_VALUE, "PRIVATE_FLAG_ALLOW_NATIVE_HEAP_POINTER_TAGGING", 16777216, "PRIVATE_FLAG_HAS_FRAGILE_USER_DATA"};

    public static class DatabaseVersion {
        public static final int FIRST_VERSION = 1;
        public static final int SIGNATURE_END_ENTITY = 2;
        public static final int SIGNATURE_MALFORMED_RECOVER = 3;
    }

    @Override // com.android.server.utils.Watchable
    public void registerObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.registerObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.unregisterObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(com.android.server.utils.Watcher observer) {
        return this.mWatchable.isRegisteredObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(com.android.server.utils.Watchable what) {
        this.mWatchable.dispatchChange(what);
    }

    protected void onChanged() {
        dispatchChange(this);
    }

    private static final class KernelPackageState {
        int appId;
        int[] excludedUserIds;

        private KernelPackageState() {
        }
    }

    public static class VersionInfo {
        java.lang.String buildFingerprint;
        int databaseVersion;
        java.lang.String fingerprint;
        int sdkVersion;

        public void forceCurrent() {
            this.sdkVersion = android.os.Build.VERSION.SDK_INT;
            this.databaseVersion = 3;
            this.buildFingerprint = android.os.Build.FINGERPRINT;
            this.fingerprint = android.content.pm.PackagePartitions.FINGERPRINT;
        }
    }

    private com.android.server.utils.SnapshotCache<com.android.server.pm.Settings> makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.Settings>(this, this) { // from class: com.android.server.pm.Settings.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.Settings createSnapshot() {
                com.android.server.pm.Settings s = new com.android.server.pm.Settings();
                s.mWatchable.seal();
                return s;
            }
        };
    }

    private void registerObservers() {
        this.mPackages.registerObserver(this.mObserver);
        this.mInstallerPackages.registerObserver(this.mObserver);
        this.mKernelMapping.registerObserver(this.mObserver);
        this.mDisabledSysPackages.registerObserver(this.mObserver);
        this.mBlockUninstallPackages.registerObserver(this.mObserver);
        this.mVersion.registerObserver(this.mObserver);
        this.mPreferredActivities.registerObserver(this.mObserver);
        this.mPersistentPreferredActivities.registerObserver(this.mObserver);
        this.mCrossProfileIntentResolvers.registerObserver(this.mObserver);
        this.mSharedUsers.registerObserver(this.mObserver);
        this.mAppIds.registerObserver(this.mObserver);
        this.mRenamedPackages.registerObserver(this.mObserver);
        this.mNextAppLinkGeneration.registerObserver(this.mObserver);
        this.mPendingDefaultBrowser.registerObserver(this.mObserver);
        this.mPendingPackages.registerObserver(this.mObserver);
    }

    public Settings(java.util.Map<java.lang.String, com.android.server.pm.PackageSetting> pkgSettings) {
        this.mWatchable = new com.android.server.utils.WatchableImpl();
        this.mPackageRestrictionsLock = new java.lang.Object();
        this.mPendingAsyncPackageRestrictionsWrites = new android.util.SparseIntArray();
        this.mDisabledSysPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mBlockUninstallPackages = new com.android.server.utils.WatchedSparseArray<>();
        this.mVersion = new com.android.server.utils.WatchedArrayMap<>();
        this.mSharedUsers = new com.android.server.utils.WatchedArrayMap<>();
        this.mRenamedPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mPendingDefaultBrowser = new com.android.server.utils.WatchedSparseArray<>();
        this.mNextAppLinkGeneration = new com.android.server.utils.WatchedSparseIntArray();
        this.mReadMessages = new java.lang.StringBuilder();
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.Settings.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.Settings.this.dispatchChange(what);
            }
        };
        this.mPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPackages, this.mPackages, "Settings.mPackages");
        this.mKernelMapping = new com.android.server.utils.WatchedArrayMap<>();
        this.mKernelMappingSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mKernelMapping, this.mKernelMapping, "Settings.mKernelMapping");
        this.mInstallerPackages = new com.android.server.utils.WatchedArraySet<>();
        this.mInstallerPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mInstallerPackages, this.mInstallerPackages, "Settings.mInstallerPackages");
        this.mPreferredActivities = new com.android.server.utils.WatchedSparseArray<>();
        this.mPreferredActivitiesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPreferredActivities, this.mPreferredActivities, "Settings.mPreferredActivities");
        this.mPersistentPreferredActivities = new com.android.server.utils.WatchedSparseArray<>();
        this.mPersistentPreferredActivitiesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPersistentPreferredActivities, this.mPersistentPreferredActivities, "Settings.mPersistentPreferredActivities");
        this.mCrossProfileIntentResolvers = new com.android.server.utils.WatchedSparseArray<>();
        this.mCrossProfileIntentResolversSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mCrossProfileIntentResolvers, this.mCrossProfileIntentResolvers, "Settings.mCrossProfileIntentResolvers");
        this.mPendingPackages = new com.android.server.utils.WatchedArrayList<>();
        this.mPendingPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPendingPackages, this.mPendingPackages, "Settings.mPendingPackages");
        this.mKeySetManagerService = new com.android.server.pm.KeySetManagerService(this.mPackages);
        this.mHandler = new android.os.Handler(com.android.internal.os.BackgroundThread.getHandler().getLooper());
        this.mLock = new com.android.server.pm.PackageManagerTracedLock();
        this.mPackages.putAll(pkgSettings);
        this.mAppIds = new com.android.server.pm.AppIdSettingMap();
        this.mSystemDir = null;
        this.mPermissions = null;
        this.mRuntimePermissionsPersistence = null;
        this.mPermissionDataProvider = null;
        this.mSettingsFilename = null;
        this.mSettingsReserveCopyFilename = null;
        this.mPreviousSettingsFilename = null;
        this.mPackageListFilename = null;
        this.mStoppedPackagesFilename = null;
        this.mBackupStoppedPackagesFilename = null;
        this.mKernelMappingFilename = null;
        this.mDomainVerificationManager = null;
        this.mSettingsExt = (com.android.server.pm.ISettingsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.ISettingsExt.class).base(this).create();
        registerObservers();
        com.android.server.utils.Watchable.verifyWatchedAttributes(this, this.mObserver);
        this.mSnapshot = makeCache();
    }

    Settings(java.io.File dataDir, com.android.permission.persistence.RuntimePermissionsPersistence runtimePermissionsPersistence, com.android.server.pm.permission.LegacyPermissionDataProvider permissionDataProvider, com.android.server.pm.verify.domain.DomainVerificationManagerInternal domainVerificationManager, android.os.Handler handler, com.android.server.pm.PackageManagerTracedLock lock) {
        this.mWatchable = new com.android.server.utils.WatchableImpl();
        this.mPackageRestrictionsLock = new java.lang.Object();
        this.mPendingAsyncPackageRestrictionsWrites = new android.util.SparseIntArray();
        this.mDisabledSysPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mBlockUninstallPackages = new com.android.server.utils.WatchedSparseArray<>();
        this.mVersion = new com.android.server.utils.WatchedArrayMap<>();
        this.mSharedUsers = new com.android.server.utils.WatchedArrayMap<>();
        this.mRenamedPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mPendingDefaultBrowser = new com.android.server.utils.WatchedSparseArray<>();
        this.mNextAppLinkGeneration = new com.android.server.utils.WatchedSparseIntArray();
        this.mReadMessages = new java.lang.StringBuilder();
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.Settings.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.Settings.this.dispatchChange(what);
            }
        };
        this.mPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPackages, this.mPackages, "Settings.mPackages");
        this.mKernelMapping = new com.android.server.utils.WatchedArrayMap<>();
        this.mKernelMappingSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mKernelMapping, this.mKernelMapping, "Settings.mKernelMapping");
        this.mInstallerPackages = new com.android.server.utils.WatchedArraySet<>();
        this.mInstallerPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mInstallerPackages, this.mInstallerPackages, "Settings.mInstallerPackages");
        this.mPreferredActivities = new com.android.server.utils.WatchedSparseArray<>();
        this.mPreferredActivitiesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPreferredActivities, this.mPreferredActivities, "Settings.mPreferredActivities");
        this.mPersistentPreferredActivities = new com.android.server.utils.WatchedSparseArray<>();
        this.mPersistentPreferredActivitiesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPersistentPreferredActivities, this.mPersistentPreferredActivities, "Settings.mPersistentPreferredActivities");
        this.mCrossProfileIntentResolvers = new com.android.server.utils.WatchedSparseArray<>();
        this.mCrossProfileIntentResolversSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mCrossProfileIntentResolvers, this.mCrossProfileIntentResolvers, "Settings.mCrossProfileIntentResolvers");
        this.mPendingPackages = new com.android.server.utils.WatchedArrayList<>();
        this.mPendingPackagesSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mPendingPackages, this.mPendingPackages, "Settings.mPendingPackages");
        this.mKeySetManagerService = new com.android.server.pm.KeySetManagerService(this.mPackages);
        this.mHandler = handler;
        this.mLock = lock;
        this.mAppIds = new com.android.server.pm.AppIdSettingMap();
        this.mPermissions = new com.android.server.pm.permission.LegacyPermissionSettings();
        this.mRuntimePermissionsPersistence = new com.android.server.pm.Settings.RuntimePermissionPersistence(runtimePermissionsPersistence, new java.util.function.Consumer<java.lang.Integer>() { // from class: com.android.server.pm.Settings.3
            @Override // java.util.function.Consumer
            public void accept(java.lang.Integer userId) {
                com.android.server.pm.Settings.this.mRuntimePermissionsPersistence.writeStateForUser(userId.intValue(), com.android.server.pm.Settings.this.mPermissionDataProvider, com.android.server.pm.Settings.this.mPackages, com.android.server.pm.Settings.this.mSharedUsers, com.android.server.pm.Settings.this.mHandler, com.android.server.pm.Settings.this.mLock, false);
            }
        });
        this.mPermissionDataProvider = permissionDataProvider;
        this.mSystemDir = new java.io.File(dataDir, "system");
        this.mSystemDir.mkdirs();
        android.os.FileUtils.setPermissions(this.mSystemDir.toString(), 509, -1, -1);
        this.mSettingsFilename = new java.io.File(this.mSystemDir, "packages.xml");
        this.mSettingsReserveCopyFilename = new java.io.File(this.mSystemDir, "packages.xml.reservecopy");
        this.mPreviousSettingsFilename = new java.io.File(this.mSystemDir, "packages-backup.xml");
        this.mPackageListFilename = new java.io.File(this.mSystemDir, "packages.list");
        android.os.FileUtils.setPermissions(this.mPackageListFilename, com.android.internal.util.FrameworkStatsLog.DISPLAY_HBM_STATE_CHANGED, 1000, 1032);
        java.io.File kernelDir = new java.io.File("/config/sdcardfs");
        this.mKernelMappingFilename = kernelDir.exists() ? kernelDir : null;
        this.mStoppedPackagesFilename = new java.io.File(this.mSystemDir, "packages-stopped.xml");
        this.mBackupStoppedPackagesFilename = new java.io.File(this.mSystemDir, "packages-stopped-backup.xml");
        this.mDomainVerificationManager = domainVerificationManager;
        this.mSettingsExt = (com.android.server.pm.ISettingsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.ISettingsExt.class).base(this).create();
        registerObservers();
        com.android.server.utils.Watchable.verifyWatchedAttributes(this, this.mObserver);
        this.mSnapshot = makeCache();
    }

    private Settings(com.android.server.pm.Settings r) {
        this.mWatchable = new com.android.server.utils.WatchableImpl();
        this.mPackageRestrictionsLock = new java.lang.Object();
        this.mPendingAsyncPackageRestrictionsWrites = new android.util.SparseIntArray();
        this.mDisabledSysPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mBlockUninstallPackages = new com.android.server.utils.WatchedSparseArray<>();
        this.mVersion = new com.android.server.utils.WatchedArrayMap<>();
        this.mSharedUsers = new com.android.server.utils.WatchedArrayMap<>();
        this.mRenamedPackages = new com.android.server.utils.WatchedArrayMap<>();
        this.mPendingDefaultBrowser = new com.android.server.utils.WatchedSparseArray<>();
        this.mNextAppLinkGeneration = new com.android.server.utils.WatchedSparseIntArray();
        this.mReadMessages = new java.lang.StringBuilder();
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.Settings.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.Settings.this.dispatchChange(what);
            }
        };
        this.mPackages = r.mPackagesSnapshot.snapshot();
        this.mPackagesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mKernelMapping = r.mKernelMappingSnapshot.snapshot();
        this.mKernelMappingSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mInstallerPackages = r.mInstallerPackagesSnapshot.snapshot();
        this.mInstallerPackagesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mKeySetManagerService = new com.android.server.pm.KeySetManagerService(r.mKeySetManagerService, this.mPackages);
        this.mHandler = null;
        this.mLock = null;
        this.mRuntimePermissionsPersistence = r.mRuntimePermissionsPersistence;
        this.mSettingsFilename = null;
        this.mSettingsReserveCopyFilename = null;
        this.mPreviousSettingsFilename = null;
        this.mPackageListFilename = null;
        this.mStoppedPackagesFilename = null;
        this.mBackupStoppedPackagesFilename = null;
        this.mKernelMappingFilename = null;
        this.mDomainVerificationManager = r.mDomainVerificationManager;
        this.mSettingsExt = r.mSettingsExt;
        this.mDisabledSysPackages.snapshot(r.mDisabledSysPackages);
        this.mBlockUninstallPackages.snapshot(r.mBlockUninstallPackages);
        this.mVersion.putAll(r.mVersion);
        this.mVerifierDeviceIdentity = r.mVerifierDeviceIdentity;
        this.mPreferredActivities = r.mPreferredActivitiesSnapshot.snapshot();
        this.mPreferredActivitiesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mPersistentPreferredActivities = r.mPersistentPreferredActivitiesSnapshot.snapshot();
        this.mPersistentPreferredActivitiesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mCrossProfileIntentResolvers = r.mCrossProfileIntentResolversSnapshot.snapshot();
        this.mCrossProfileIntentResolversSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mSharedUsers.snapshot(r.mSharedUsers);
        this.mAppIds = r.mAppIds.snapshot();
        this.mRenamedPackages.snapshot(r.mRenamedPackages);
        this.mNextAppLinkGeneration.snapshot(r.mNextAppLinkGeneration);
        this.mPendingDefaultBrowser.snapshot(r.mPendingDefaultBrowser);
        this.mPendingPackages = r.mPendingPackagesSnapshot.snapshot();
        this.mPendingPackagesSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mSystemDir = null;
        this.mPermissions = r.mPermissions;
        this.mPermissionDataProvider = r.mPermissionDataProvider;
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.Settings snapshot() {
        return this.mSnapshot.snapshot();
    }

    private void invalidatePackageCache() {
        com.android.server.pm.PackageManagerService.invalidatePackageInfoCache();
        android.app.compat.ChangeIdStateCache.invalidate();
        onChanged();
    }

    com.android.server.pm.PackageSetting getPackageLPr(java.lang.String pkgName) {
        return this.mPackages.get(pkgName);
    }

    com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> getPackagesLocked() {
        return this.mPackages;
    }

    com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> getDisabledSystemPackagesLocked() {
        return this.mDisabledSysPackages;
    }

    com.android.server.pm.KeySetManagerService getKeySetManagerService() {
        return this.mKeySetManagerService;
    }

    java.lang.String getRenamedPackageLPr(java.lang.String pkgName) {
        return this.mRenamedPackages.get(pkgName);
    }

    java.lang.String addRenamedPackageLPw(java.lang.String pkgName, java.lang.String origPkgName) {
        return this.mRenamedPackages.put(pkgName, origPkgName);
    }

    void removeRenamedPackageLPw(java.lang.String pkgName) {
        this.mRenamedPackages.remove(pkgName);
    }

    void pruneRenamedPackagesLPw() {
        for (int i = this.mRenamedPackages.size() - 1; i >= 0; i--) {
            com.android.server.pm.PackageSetting ps = this.mPackages.get(this.mRenamedPackages.valueAt(i));
            if (ps == null) {
                this.mRenamedPackages.removeAt(i);
            }
        }
    }

    com.android.server.pm.SharedUserSetting getSharedUserLPw(java.lang.String name, int pkgFlags, int pkgPrivateFlags, boolean create) throws com.android.server.pm.PackageManagerException {
        com.android.server.pm.SharedUserSetting s = this.mSharedUsers.get(name);
        if (s == null && create) {
            s = new com.android.server.pm.SharedUserSetting(name, pkgFlags, pkgPrivateFlags);
            s.mAppId = this.mAppIds.acquireAndRegisterNewAppId(s);
            if (s.mAppId < 0) {
                throw new com.android.server.pm.PackageManagerException(-4, "Creating shared user " + name + " failed");
            }
            android.util.Log.i("PackageManager", "New shared user " + name + ": id=" + s.mAppId);
            this.mSharedUsers.put(name, s);
        }
        return s;
    }

    com.android.server.utils.WatchedArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.SharedUserApi> getSharedUsersLocked() {
        return this.mSharedUsers;
    }

    java.util.Collection<com.android.server.pm.SharedUserSetting> getAllSharedUsersLPw() {
        return this.mSharedUsers.values();
    }

    boolean disableSystemPackageLPw(java.lang.String name, boolean replaced) {
        com.android.server.pm.PackageSetting disabled;
        com.android.server.pm.PackageSetting p = this.mPackages.get(name);
        if (p == null) {
            android.util.Log.w("PackageManager", "Package " + name + " is not an installed package");
            return false;
        }
        com.android.server.pm.PackageSetting dp = this.mDisabledSysPackages.get(name);
        if (dp != null || p.getPkg() == null || !p.isSystem() || p.isUpdatedSystemApp()) {
            return false;
        }
        if (replaced) {
            disabled = new com.android.server.pm.PackageSetting(p);
        } else {
            disabled = p;
        }
        p.getPkgState().setUpdatedSystemApp(true);
        this.mDisabledSysPackages.put(name, disabled);
        com.android.server.pm.SharedUserSetting sharedUserSetting = getSharedUserSettingLPr(disabled);
        if (sharedUserSetting != null) {
            sharedUserSetting.mDisabledPackages.add(disabled);
        }
        return true;
    }

    com.android.server.pm.PackageSetting enableSystemPackageLPw(java.lang.String name) {
        com.android.server.pm.PackageSetting p = this.mDisabledSysPackages.get(name);
        if (p == null) {
            android.util.Log.w("PackageManager", "Package " + name + " is not disabled");
            return null;
        }
        com.android.server.pm.SharedUserSetting sharedUserSetting = getSharedUserSettingLPr(p);
        if (sharedUserSetting != null) {
            sharedUserSetting.mDisabledPackages.remove(p);
        }
        p.getPkgState().setUpdatedSystemApp(false);
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = p.getPkg();
        com.android.server.pm.PackageSetting ret = addPackageLPw(name, p.getRealName(), p.getPath(), p.getAppId(), p.getFlags(), p.getPrivateFlags(), this.mDomainVerificationManager.generateNewId(), pkg == null ? false : pkg.isSdkLibrary());
        if (ret != null) {
            ret.setLegacyNativeLibraryPath(p.getLegacyNativeLibraryPath());
            ret.setPrimaryCpuAbi(p.getPrimaryCpuAbiLegacy());
            ret.setSecondaryCpuAbi(p.getSecondaryCpuAbiLegacy());
            ret.setCpuAbiOverride(p.getCpuAbiOverride());
            ret.setLongVersionCode(p.getVersionCode());
            ret.setUsesSdkLibraries(p.getUsesSdkLibraries());
            ret.setUsesSdkLibrariesVersionsMajor(p.getUsesSdkLibrariesVersionsMajor());
            ret.setUsesSdkLibrariesOptional(p.getUsesSdkLibrariesOptional());
            ret.setUsesStaticLibraries(p.getUsesStaticLibraries());
            ret.setUsesStaticLibrariesVersions(p.getUsesStaticLibrariesVersions());
            ret.setMimeGroups(p.getMimeGroups());
            ret.setAppMetadataFilePath(p.getAppMetadataFilePath());
            ret.setAppMetadataSource(p.getAppMetadataSource());
            ret.getPkgState().setUpdatedSystemApp(false);
            ret.setTargetSdkVersion(p.getTargetSdkVersion());
            ret.setRestrictUpdateHash(p.getRestrictUpdateHash());
            ret.setScannedAsStoppedSystemApp(p.isScannedAsStoppedSystemApp());
            ret.setInstallSource(p.getInstallSource());
        }
        this.mDisabledSysPackages.remove(name);
        return ret;
    }

    boolean isDisabledSystemPackageLPr(java.lang.String name) {
        return this.mDisabledSysPackages.containsKey(name);
    }

    void removeDisabledSystemPackageLPw(java.lang.String name) {
        com.android.server.pm.SharedUserSetting sharedUserSetting;
        com.android.server.pm.PackageSetting p = this.mDisabledSysPackages.remove(name);
        if (p != null && (sharedUserSetting = getSharedUserSettingLPr(p)) != null) {
            sharedUserSetting.mDisabledPackages.remove(p);
            checkAndPruneSharedUserLPw(sharedUserSetting, false);
        }
    }

    com.android.server.pm.PackageSetting addPackageLPw(java.lang.String name, java.lang.String realName, java.io.File codePath, int uid, int pkgFlags, int pkgPrivateFlags, java.util.UUID domainSetId, boolean isSdkLibrary) {
        com.android.server.pm.PackageSetting p = this.mPackages.get(name);
        if (p != null) {
            if (p.getAppId() == uid) {
                return p;
            }
            com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Adding duplicate package, keeping first: " + name);
            return null;
        }
        com.android.server.pm.PackageSetting p2 = new com.android.server.pm.PackageSetting(name, realName, codePath, pkgFlags, pkgPrivateFlags, domainSetId).setAppId(uid);
        if ((uid != -1 || !isSdkLibrary || !com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.disallowSdkLibsToBeApps()) && !this.mAppIds.registerExistingAppId(uid, p2, name)) {
            return null;
        }
        this.mPackages.put(name, p2);
        return p2;
    }

    com.android.server.pm.SharedUserSetting addSharedUserLPw(java.lang.String name, int uid, int pkgFlags, int pkgPrivateFlags) {
        com.android.server.pm.SharedUserSetting s = this.mSharedUsers.get(name);
        if (s != null) {
            if (s.mAppId == uid) {
                return s;
            }
            com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Adding duplicate shared user, keeping first: " + name);
            return null;
        }
        com.android.server.pm.SharedUserSetting s2 = new com.android.server.pm.SharedUserSetting(name, pkgFlags, pkgPrivateFlags);
        s2.mAppId = uid;
        if (!this.mAppIds.registerExistingAppId(uid, s2, name)) {
            return null;
        }
        this.mSharedUsers.put(name, s2);
        return s2;
    }

    void pruneSharedUsersLPw() {
        java.util.List<java.lang.String> removeKeys = new java.util.ArrayList<>();
        java.util.List<com.android.server.pm.SharedUserSetting> removeValues = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, com.android.server.pm.SharedUserSetting> entry : this.mSharedUsers.entrySet()) {
            com.android.server.pm.SharedUserSetting sus = entry.getValue();
            if (sus == null) {
                removeKeys.add(entry.getKey());
            } else {
                boolean changed = false;
                com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting> sharedUserPackageSettings = sus.getPackageSettings();
                for (int i = sharedUserPackageSettings.size() - 1; i >= 0; i--) {
                    com.android.server.pm.PackageSetting ps = sharedUserPackageSettings.valueAt(i);
                    if (this.mPackages.get(ps.getPackageName()) == null) {
                        sharedUserPackageSettings.removeAt(i);
                        changed = true;
                    }
                }
                com.android.server.utils.WatchedArraySet<com.android.server.pm.PackageSetting> sharedUserDisabledPackageSettings = sus.getDisabledPackageSettings();
                for (int i2 = sharedUserDisabledPackageSettings.size() - 1; i2 >= 0; i2--) {
                    com.android.server.pm.PackageSetting ps2 = sharedUserDisabledPackageSettings.valueAt(i2);
                    if (this.mDisabledSysPackages.get(ps2.getPackageName()) == null) {
                        sharedUserDisabledPackageSettings.removeAt(i2);
                        changed = true;
                    }
                }
                if (changed) {
                    sus.onChanged();
                }
                if (sharedUserPackageSettings.isEmpty() && sharedUserDisabledPackageSettings.isEmpty()) {
                    removeValues.add(sus);
                }
            }
        }
        final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> watchedArrayMap = this.mSharedUsers;
        java.util.Objects.requireNonNull(watchedArrayMap);
        removeKeys.forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.Settings$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                watchedArrayMap.remove((java.lang.String) obj);
            }
        });
        removeValues.forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.Settings$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$pruneSharedUsersLPw$0((com.android.server.pm.SharedUserSetting) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pruneSharedUsersLPw$0(com.android.server.pm.SharedUserSetting sus) {
        checkAndPruneSharedUserLPw(sus, true);
    }

    static com.android.server.pm.PackageSetting createNewSetting(java.lang.String pkgName, com.android.server.pm.PackageSetting originalPkg, com.android.server.pm.PackageSetting disabledPkg, java.lang.String realPkgName, com.android.server.pm.SharedUserSetting sharedUser, java.io.File codePath, java.lang.String legacyNativeLibraryPath, java.lang.String primaryCpuAbi, java.lang.String secondaryCpuAbi, long versionCode, int pkgFlags, int pkgPrivateFlags, android.os.UserHandle installUser, boolean allowInstall, boolean instantApp, boolean virtualPreload, boolean isStoppedSystemApp, com.android.server.pm.UserManagerService userManager, java.lang.String[] usesSdkLibraries, long[] usesSdkLibrariesVersions, boolean[] usesSdkLibrariesOptional, java.lang.String[] usesStaticLibraries, long[] usesStaticLibrariesVersions, java.util.Set<java.lang.String> mimeGroupNames, java.util.UUID domainSetId, int targetSdkVersion, byte[] restrictUpdatedHash) {
        boolean z;
        int i;
        int installUserId;
        int installUserId2;
        boolean installed;
        if (originalPkg != null) {
            if (com.android.server.pm.PackageManagerService.DEBUG_UPGRADE) {
                android.util.Log.v("PackageManager", "Package " + pkgName + " is adopting original package " + originalPkg.getPackageName());
            }
            com.android.server.pm.PackageSetting pkgSetting = new com.android.server.pm.PackageSetting(originalPkg, pkgName).setPath(codePath).setLegacyNativeLibraryPath(legacyNativeLibraryPath).setPrimaryCpuAbi(primaryCpuAbi).setSecondaryCpuAbi(secondaryCpuAbi).setSignatures(new com.android.server.pm.PackageSignatures()).setLongVersionCode(versionCode).setUsesSdkLibraries(usesSdkLibraries).setUsesSdkLibrariesVersionsMajor(usesSdkLibrariesVersions).setUsesSdkLibrariesOptional(usesSdkLibrariesOptional).setUsesStaticLibraries(usesStaticLibraries).setUsesStaticLibrariesVersions(usesStaticLibrariesVersions).setLastModifiedTime(codePath.lastModified()).setDomainSetId(domainSetId).setTargetSdkVersion(targetSdkVersion).setRestrictUpdateHash(restrictUpdatedHash);
            pkgSetting.setFlags(pkgFlags).setPrivateFlags(pkgPrivateFlags);
            return pkgSetting;
        }
        int installUserId3 = installUser != null ? installUser.getIdentifier() : 0;
        com.android.server.pm.PackageSetting pkgSetting2 = new com.android.server.pm.PackageSetting(pkgName, realPkgName, codePath, pkgFlags, pkgPrivateFlags, domainSetId).setUsesSdkLibraries(usesSdkLibraries).setUsesSdkLibrariesVersionsMajor(usesSdkLibrariesVersions).setUsesSdkLibrariesOptional(usesSdkLibrariesOptional).setUsesStaticLibraries(usesStaticLibraries).setUsesStaticLibrariesVersions(usesStaticLibrariesVersions).setLegacyNativeLibraryPath(legacyNativeLibraryPath).setPrimaryCpuAbi(primaryCpuAbi).setSecondaryCpuAbi(secondaryCpuAbi).setLongVersionCode(versionCode).setMimeGroups(createMimeGroups(mimeGroupNames)).setTargetSdkVersion(targetSdkVersion).setRestrictUpdateHash(restrictUpdatedHash).setLastModifiedTime(codePath.lastModified());
        if (sharedUser != null) {
            pkgSetting2.setSharedUserAppId(sharedUser.mAppId);
        }
        if ((pkgFlags & 1) == 0) {
            java.util.List<android.content.pm.UserInfo> users = getAllUsers(userManager);
            if (users != null) {
                z = allowInstall;
                i = pkgFlags;
                if (z) {
                    for (android.content.pm.UserInfo user : users) {
                        if (installUser != null) {
                            installUserId2 = installUserId3;
                            if (installUserId2 != -1 || isAdbInstallDisallowed(userManager, user.id) || user.preCreated) {
                                if (installUserId2 != user.id) {
                                    installed = false;
                                }
                            }
                            pkgSetting2.setUserState(user.id, 0L, 0L, 0, installed, true, true, false, 0, null, instantApp, virtualPreload, null, null, null, 0, 0, null, null, 0L, 0, null);
                            pkgSetting2.mExtImpl.afterSetForNonSysAppInCreateNewSetting(user);
                            installUserId3 = installUserId2;
                        } else {
                            installUserId2 = installUserId3;
                        }
                        installed = true;
                        pkgSetting2.setUserState(user.id, 0L, 0L, 0, installed, true, true, false, 0, null, instantApp, virtualPreload, null, null, null, 0, 0, null, null, 0L, 0, null);
                        pkgSetting2.mExtImpl.afterSetForNonSysAppInCreateNewSetting(user);
                        installUserId3 = installUserId2;
                    }
                    installUserId = installUserId3;
                }
            } else {
                z = allowInstall;
                i = pkgFlags;
            }
            installUserId = installUserId3;
        } else {
            z = allowInstall;
            i = pkgFlags;
            installUserId = installUserId3;
            if (isStoppedSystemApp) {
                pkgSetting2.setStopped(true, installUserId);
                pkgSetting2.setScannedAsStoppedSystemApp(true);
            }
        }
        if (pkgSetting2 != null) {
            pkgSetting2.mExtImpl.afterCreateWithoutOriginInCreateNewSetting(i, z, getAllUsers(userManager));
        }
        if (sharedUser != null) {
            pkgSetting2.setAppId(sharedUser.mAppId);
            return pkgSetting2;
        }
        if (disabledPkg != null && disabledPkg.getPath() != null && disabledPkg.getPath().exists() && disabledPkg.getPkg() != null) {
            pkgSetting2.setSignatures(new com.android.server.pm.PackageSignatures(disabledPkg.getSignatures()));
            pkgSetting2.setAppId(disabledPkg.getAppId());
            pkgSetting2.getLegacyPermissionState().copyFrom(disabledPkg.getLegacyPermissionState());
            java.util.List<android.content.pm.UserInfo> users2 = getAllUsers(userManager);
            if (users2 != null) {
                java.util.Iterator<android.content.pm.UserInfo> it = users2.iterator();
                while (it.hasNext()) {
                    int userId = it.next().id;
                    pkgSetting2.setDisabledComponentsCopy(disabledPkg.getDisabledComponents(userId), userId);
                    pkgSetting2.setEnabledComponentsCopy(disabledPkg.getEnabledComponents(userId), userId);
                    installUserId = installUserId;
                }
                return pkgSetting2;
            }
            return pkgSetting2;
        }
        return pkgSetting2;
    }

    private static java.util.Map<java.lang.String, java.util.Set<java.lang.String>> createMimeGroups(java.util.Set<java.lang.String> mimeGroupNames) {
        if (mimeGroupNames == null) {
            return null;
        }
        return new com.android.server.pm.Settings.KeySetToValueMap(mimeGroupNames, new android.util.ArraySet());
    }

    static void updatePackageSetting(com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.PackageSetting disabledPkg, com.android.server.pm.SharedUserSetting existingSharedUserSetting, com.android.server.pm.SharedUserSetting sharedUser, java.io.File codePath, java.lang.String legacyNativeLibraryPath, java.lang.String primaryCpuAbi, java.lang.String secondaryCpuAbi, int pkgFlags, int pkgPrivateFlags, com.android.server.pm.UserManagerService userManager, java.lang.String[] usesSdkLibraries, long[] usesSdkLibrariesVersions, boolean[] usesSdkLibrariesOptional, java.lang.String[] usesStaticLibraries, long[] usesStaticLibrariesVersions, java.util.Set<java.lang.String> mimeGroupNames, java.util.UUID domainSetId, int targetSdkVersion, byte[] restrictUpdatedHash, boolean isDontKill) throws com.android.server.pm.PackageManagerException {
        int pkgPrivateFlags2;
        java.util.List<android.content.pm.UserInfo> allUserInfos;
        java.lang.String pkgName = pkgSetting.getPackageName();
        java.io.File oldCodePath = pkgSetting.getPath();
        if (sharedUser == null) {
            pkgSetting.setSharedUserAppId(-1);
        } else if (java.util.Objects.equals(existingSharedUserSetting, sharedUser)) {
            pkgSetting.setSharedUserAppId(sharedUser.mAppId);
        } else {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Package " + pkgName + " shared user changed from " + (existingSharedUserSetting != null ? existingSharedUserSetting.name : "<nothing>") + " to " + sharedUser.name);
            throw new com.android.server.pm.PackageManagerException(-24, "Updating application package " + pkgName + " failed");
        }
        if (!oldCodePath.equals(codePath)) {
            boolean isSystem = pkgSetting.isSystem();
            android.util.Slog.i("PackageManager", "Update" + (isSystem ? " system" : "") + " package " + pkgName + " code path from " + pkgSetting.getPathString() + " to " + codePath.toString() + "; Retain data and using new");
            if (!isSystem) {
                if ((pkgFlags & 1) != 0 && disabledPkg == null && (allUserInfos = getAllUsers(userManager)) != null) {
                    for (android.content.pm.UserInfo userInfo : allUserInfos) {
                        if (!pkgSetting.mExtImpl.interceptSetInstalledInUpdatePackageSetting(userInfo)) {
                            pkgSetting.setInstalled(true, userInfo.id);
                            pkgSetting.setUninstallReason(0, userInfo.id);
                        }
                    }
                }
                pkgSetting.setLegacyNativeLibraryPath(legacyNativeLibraryPath);
            }
            pkgSetting.setPath(codePath);
            if (isDontKill && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.improveInstallDontKill()) {
                pkgSetting.addOldPath(oldCodePath);
            }
        }
        pkgSetting.setPrimaryCpuAbi(primaryCpuAbi).setSecondaryCpuAbi(secondaryCpuAbi).updateMimeGroups(mimeGroupNames).setDomainSetId(domainSetId).setTargetSdkVersion(targetSdkVersion).setRestrictUpdateHash(restrictUpdatedHash);
        if (usesSdkLibraries != null && usesSdkLibrariesVersions != null && usesSdkLibrariesOptional != null && usesSdkLibraries.length == usesSdkLibrariesVersions.length && usesSdkLibraries.length == usesSdkLibrariesOptional.length) {
            pkgSetting.setUsesSdkLibraries(usesSdkLibraries).setUsesSdkLibrariesVersionsMajor(usesSdkLibrariesVersions).setUsesSdkLibrariesOptional(usesSdkLibrariesOptional);
        } else {
            pkgSetting.setUsesSdkLibraries(null).setUsesSdkLibrariesVersionsMajor(null).setUsesSdkLibrariesOptional(null);
        }
        if (usesStaticLibraries != null && usesStaticLibrariesVersions != null && usesStaticLibraries.length == usesStaticLibrariesVersions.length) {
            pkgSetting.setUsesStaticLibraries(usesStaticLibraries).setUsesStaticLibrariesVersions(usesStaticLibrariesVersions);
        } else {
            pkgSetting.setUsesStaticLibraries(null).setUsesStaticLibrariesVersions(null);
        }
        int newPkgFlags = pkgSetting.getFlags();
        pkgSetting.setFlags((newPkgFlags & (-2)) | (pkgFlags & 1));
        boolean wasRequiredForSystemUser = (pkgSetting.getPrivateFlags() & 512) != 0;
        if (wasRequiredForSystemUser) {
            pkgPrivateFlags2 = pkgPrivateFlags | 512;
        } else {
            pkgPrivateFlags2 = pkgPrivateFlags & (-513);
        }
        pkgSetting.setPrivateFlags(pkgPrivateFlags2);
    }

    boolean registerAppIdLPw(com.android.server.pm.PackageSetting p, boolean forceNew) throws com.android.server.pm.PackageManagerException {
        boolean createdNew;
        if (p.getAppId() == 0 || forceNew) {
            p.setAppId(this.mAppIds.acquireAndRegisterNewAppId(p));
            createdNew = true;
        } else {
            createdNew = this.mAppIds.registerExistingAppId(p.getAppId(), p, p.getPackageName());
        }
        if (p.getAppId() < 0) {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Package " + p.getPackageName() + " could not be assigned a valid UID");
            throw new com.android.server.pm.PackageManagerException(-4, "Package " + p.getPackageName() + " could not be assigned a valid UID");
        }
        return createdNew;
    }

    void writeUserRestrictionsLPw(com.android.server.pm.PackageSetting newPackage, com.android.server.pm.PackageSetting oldPackage) {
        java.util.List<android.content.pm.UserInfo> allUsers;
        com.android.server.pm.pkg.PackageUserState oldUserState;
        if (getPackageLPr(newPackage.getPackageName()) == null || (allUsers = getAllUsers(com.android.server.pm.UserManagerService.getInstance())) == null) {
            return;
        }
        for (android.content.pm.UserInfo user : allUsers) {
            if (oldPackage == null) {
                oldUserState = com.android.server.pm.pkg.PackageUserState.DEFAULT;
            } else {
                oldUserState = oldPackage.readUserState(user.id);
            }
            if (!oldUserState.equals(newPackage.readUserState(user.id))) {
                writePackageRestrictionsLPr(user.id);
            }
        }
    }

    static boolean isAdbInstallDisallowed(com.android.server.pm.UserManagerService userManager, int userId) {
        return userManager.hasUserRestriction("no_debugging_features", userId);
    }

    void insertPackageSettingLPw(com.android.server.pm.PackageSetting p, com.android.server.pm.pkg.AndroidPackage pkg) {
        if (p.getSigningDetails().getSignatures() == null) {
            p.setSigningDetails(pkg.getSigningDetails());
        }
        com.android.server.pm.SharedUserSetting sharedUserSetting = getSharedUserSettingLPr(p);
        if (sharedUserSetting != null && sharedUserSetting.signatures.mSigningDetails.getSignatures() == null) {
            sharedUserSetting.signatures.mSigningDetails = pkg.getSigningDetails();
        }
        addPackageSettingLPw(p, sharedUserSetting);
    }

    void addPackageSettingLPw(com.android.server.pm.PackageSetting p, com.android.server.pm.SharedUserSetting sharedUser) {
        this.mPackages.put(p.getPackageName(), p);
        if (sharedUser != null) {
            com.android.server.pm.SharedUserSetting existingSharedUserSetting = getSharedUserSettingLPr(p);
            if (existingSharedUserSetting != null && existingSharedUserSetting != sharedUser) {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Package " + p.getPackageName() + " was user " + existingSharedUserSetting + " but is now " + sharedUser + "; I am not changing its files so it will probably fail!");
                existingSharedUserSetting.removePackage(p);
            } else if (p.getAppId() != 0 && p.getAppId() != sharedUser.mAppId) {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Package " + p.getPackageName() + " was app id " + p.getAppId() + " but is now user " + sharedUser + " with app id " + sharedUser.mAppId + "; I am not changing its files so it will probably fail!");
            }
            sharedUser.addPackage(p);
            p.setSharedUserAppId(sharedUser.mAppId);
            p.setAppId(sharedUser.mAppId);
        }
        java.lang.Object appIdPs = getSettingLPr(p.getAppId());
        if (sharedUser == null) {
            if (appIdPs != null && appIdPs != p) {
                this.mAppIds.replaceSetting(p.getAppId(), p);
                return;
            }
            return;
        }
        if (appIdPs != null && appIdPs != sharedUser) {
            this.mAppIds.replaceSetting(p.getAppId(), sharedUser);
        }
    }

    boolean checkAndPruneSharedUserLPw(com.android.server.pm.SharedUserSetting s, boolean skipCheck) {
        if ((skipCheck || (s.getPackageStates().isEmpty() && s.getDisabledPackageStates().isEmpty())) && this.mSharedUsers.remove(s.name) != null) {
            removeAppIdLPw(s.mAppId);
            return true;
        }
        return false;
    }

    boolean removePackageAndAppIdLPw(java.lang.String name) {
        com.android.server.pm.PackageSetting p = this.mPackages.remove(name);
        if (p == null) {
            return false;
        }
        removeInstallerPackageStatus(name);
        com.android.server.pm.SharedUserSetting sharedUserSetting = getSharedUserSettingLPr(p);
        if (sharedUserSetting != null) {
            sharedUserSetting.removePackage(p);
            return checkAndPruneSharedUserLPw(sharedUserSetting, false);
        }
        removeAppIdLPw(p.getAppId());
        return true;
    }

    private void removeInstallerPackageStatus(java.lang.String packageName) {
        if (!this.mInstallerPackages.contains(packageName)) {
            return;
        }
        for (int i = 0; i < this.mPackages.size(); i++) {
            this.mPackages.valueAt(i).removeInstallerPackage(packageName);
        }
        this.mInstallerPackages.remove(packageName);
    }

    public com.android.server.pm.SettingBase getSettingLPr(int appId) {
        return this.mAppIds.getSetting(appId);
    }

    void removeAppIdLPw(int appId) {
        this.mAppIds.removeSetting(appId);
    }

    void convertSharedUserSettingsLPw(com.android.server.pm.SharedUserSetting sharedUser) {
        com.android.server.pm.PackageSetting ps = sharedUser.getPackageSettings().valueAt(0);
        this.mAppIds.replaceSetting(sharedUser.getAppId(), ps);
        ps.setSharedUserAppId(-1);
        if (!sharedUser.getDisabledPackageSettings().isEmpty()) {
            com.android.server.pm.PackageSetting disabledPs = sharedUser.getDisabledPackageSettings().valueAt(0);
            disabledPs.setSharedUserAppId(-1);
        }
        this.mSharedUsers.remove(sharedUser.getName());
    }

    void checkAndConvertSharedUserSettingsLPw(com.android.server.pm.SharedUserSetting sharedUser) {
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg;
        if (sharedUser.isSingleUser() && (pkg = sharedUser.getPackageSettings().valueAt(0).getPkg()) != null && pkg.isLeavingSharedUser() && com.android.server.pm.SharedUidMigration.applyStrategy(2)) {
            convertSharedUserSettingsLPw(sharedUser);
        }
    }

    com.android.server.pm.PreferredIntentResolver editPreferredActivitiesLPw(int userId) {
        com.android.server.pm.PreferredIntentResolver pir = this.mPreferredActivities.get(userId);
        if (pir == null) {
            com.android.server.pm.PreferredIntentResolver pir2 = new com.android.server.pm.PreferredIntentResolver();
            this.mPreferredActivities.put(userId, pir2);
            return pir2;
        }
        return pir;
    }

    com.android.server.pm.PersistentPreferredIntentResolver editPersistentPreferredActivitiesLPw(int userId) {
        com.android.server.pm.PersistentPreferredIntentResolver ppir = this.mPersistentPreferredActivities.get(userId);
        if (ppir == null) {
            com.android.server.pm.PersistentPreferredIntentResolver ppir2 = new com.android.server.pm.PersistentPreferredIntentResolver();
            this.mPersistentPreferredActivities.put(userId, ppir2);
            return ppir2;
        }
        return ppir;
    }

    com.android.server.pm.CrossProfileIntentResolver editCrossProfileIntentResolverLPw(int userId) {
        com.android.server.pm.CrossProfileIntentResolver cpir = this.mCrossProfileIntentResolvers.get(userId);
        if (cpir == null) {
            com.android.server.pm.CrossProfileIntentResolver cpir2 = new com.android.server.pm.CrossProfileIntentResolver();
            this.mCrossProfileIntentResolvers.put(userId, cpir2);
            return cpir2;
        }
        return cpir;
    }

    java.lang.String getPendingDefaultBrowserLPr(int userId) {
        return this.mPendingDefaultBrowser.get(userId);
    }

    void setPendingDefaultBrowserLPw(java.lang.String defaultBrowser, int userId) {
        this.mPendingDefaultBrowser.put(userId, defaultBrowser);
    }

    java.lang.String removePendingDefaultBrowserLPw(int userId) {
        return this.mPendingDefaultBrowser.removeReturnOld(userId);
    }

    private java.io.File getUserSystemDirectory(int userId) {
        return new java.io.File(new java.io.File(this.mSystemDir, com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS), java.lang.Integer.toString(userId));
    }

    private com.android.server.pm.ResilientAtomicFile getUserPackagesStateFile(int userId) {
        java.io.File mainFile = new java.io.File(getUserSystemDirectory(userId), "package-restrictions.xml");
        java.io.File temporaryBackup = new java.io.File(getUserSystemDirectory(userId), "package-restrictions-backup.xml");
        java.io.File reserveCopy = new java.io.File(getUserSystemDirectory(userId), "package-restrictions.xml.reservecopy");
        return new com.android.server.pm.ResilientAtomicFile(mainFile, temporaryBackup, reserveCopy, com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTION_SERVICE_RESTARTED, "package restrictions", this);
    }

    private com.android.server.pm.ResilientAtomicFile getSettingsFile() {
        return new com.android.server.pm.ResilientAtomicFile(this.mSettingsFilename, this.mPreviousSettingsFilename, this.mSettingsReserveCopyFilename, com.android.internal.util.FrameworkStatsLog.HOTWORD_DETECTION_SERVICE_RESTARTED, "package manager settings", this);
    }

    private java.io.File getUserRuntimePermissionsFile(int userId) {
        return new java.io.File(getUserSystemDirectory(userId), RUNTIME_PERMISSIONS_FILE_NAME);
    }

    void writeAllUsersPackageRestrictionsLPr() throws java.lang.Throwable {
        writeAllUsersPackageRestrictionsLPr(false);
    }

    void writeAllUsersPackageRestrictionsLPr(boolean sync) throws java.lang.Throwable {
        java.util.List<android.content.pm.UserInfo> users = getAllUsers(com.android.server.pm.UserManagerService.getInstance());
        if (users == null) {
            return;
        }
        if (sync) {
            synchronized (this.mPackageRestrictionsLock) {
                this.mPendingAsyncPackageRestrictionsWrites.clear();
            }
            this.mHandler.removeMessages(30);
        }
        for (android.content.pm.UserInfo user : users) {
            writePackageRestrictionsLPr(user.id, sync);
        }
    }

    void writeAllRuntimePermissionsLPr() {
        for (int userId : com.android.server.pm.UserManagerService.getInstance().getUserIds()) {
            this.mRuntimePermissionsPersistence.writeStateForUserAsync(userId);
        }
    }

    boolean isPermissionUpgradeNeeded(int userId) {
        return this.mRuntimePermissionsPersistence.isPermissionUpgradeNeeded(userId);
    }

    void updateRuntimePermissionsFingerprint(int userId) {
        this.mRuntimePermissionsPersistence.updateRuntimePermissionsFingerprint(userId);
    }

    int getDefaultRuntimePermissionsVersion(int userId) {
        return this.mRuntimePermissionsPersistence.getVersion(userId);
    }

    void setDefaultRuntimePermissionsVersion(int version, int userId) {
        this.mRuntimePermissionsPersistence.setVersion(version, userId);
    }

    void setPermissionControllerVersion(long version) {
        this.mRuntimePermissionsPersistence.setPermissionControllerVersion(version);
    }

    public com.android.server.pm.Settings.VersionInfo findOrCreateVersion(java.lang.String volumeUuid) {
        com.android.server.pm.Settings.VersionInfo ver = this.mVersion.get(volumeUuid);
        if (ver == null) {
            com.android.server.pm.Settings.VersionInfo ver2 = new com.android.server.pm.Settings.VersionInfo();
            this.mVersion.put(volumeUuid, ver2);
            return ver2;
        }
        return ver;
    }

    public com.android.server.pm.Settings.VersionInfo getInternalVersion() {
        return this.mVersion.get(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL);
    }

    public com.android.server.pm.Settings.VersionInfo getExternalVersion() {
        return this.mVersion.get("primary_physical");
    }

    public void onVolumeForgotten(java.lang.String fsUuid) {
        this.mVersion.remove(fsUuid);
    }

    void readPreferredActivitiesLPw(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        try {
                            java.lang.String tagName = parser.getName();
                            if (tagName.equals(TAG_ITEM)) {
                                com.android.server.pm.PreferredActivity pa = new com.android.server.pm.PreferredActivity(parser);
                                if (pa.mPref.getParseError() == null) {
                                    com.android.server.pm.PreferredIntentResolver resolver = editPreferredActivitiesLPw(userId);
                                    if (resolver.shouldAddPreferredActivity(pa)) {
                                        resolver.addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) null, pa);
                                    }
                                } else {
                                    com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <preferred-activity> " + pa.mPref.getParseError() + " at " + parser.getPositionDescription());
                                }
                            } else {
                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <preferred-activities>: " + parser.getName());
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                            }
                        } catch (java.lang.Exception e) {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Error reading settings: " + e);
                            android.util.Slog.wtf("PackageManager", "Error reading package manager stopped packages", e);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readPersistentPreferredActivitiesLPw(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(TAG_ITEM)) {
                            com.android.server.pm.PersistentPreferredActivity ppa = new com.android.server.pm.PersistentPreferredActivity(parser);
                            editPersistentPreferredActivitiesLPw(userId).addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) null, ppa);
                        } else {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <persistent-preferred-activities>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readCrossProfileIntentFiltersLPw(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(TAG_ITEM)) {
                            com.android.server.pm.CrossProfileIntentFilter cpif = new com.android.server.pm.CrossProfileIntentFilter(parser);
                            editCrossProfileIntentResolverLPw(userId).addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) null, cpif);
                        } else {
                            java.lang.String msg = "Unknown element under crossProfile-intent-filters: " + tagName;
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, msg);
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void readDefaultAppsLPw(org.xmlpull.v1.XmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String defaultBrowser = readDefaultApps(parser);
        if (defaultBrowser != null) {
            this.mPendingDefaultBrowser.put(userId, defaultBrowser);
        }
    }

    static java.lang.String readDefaultApps(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String defaultBrowser = null;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals(TAG_DEFAULT_BROWSER)) {
                    defaultBrowser = parser.getAttributeValue(null, "packageName");
                } else if (!tagName.equals(TAG_DEFAULT_DIALER)) {
                    java.lang.String msg = "Unknown element under default-apps: " + parser.getName();
                    com.android.server.pm.PackageManagerService.reportSettingsProblem(5, msg);
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        return defaultBrowser;
    }

    void readBlockUninstallPackagesLPw(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        android.util.ArraySet<java.lang.String> packages = new android.util.ArraySet<>();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals(TAG_BLOCK_UNINSTALL)) {
                    java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, "packageName");
                    packages.add(packageName);
                } else {
                    java.lang.String msg = "Unknown element under block-uninstall-packages: " + parser.getName();
                    com.android.server.pm.PackageManagerService.reportSettingsProblem(5, msg);
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        if (packages.isEmpty()) {
            this.mBlockUninstallPackages.remove(userId);
        } else {
            this.mBlockUninstallPackages.put(userId, packages);
        }
    }

    @Override // com.android.server.pm.ResilientAtomicFile.ReadEventLogger
    public void logEvent(int priority, java.lang.String msg) {
        this.mReadMessages.append(msg + "\n");
        com.android.server.pm.PackageManagerService.reportSettingsProblem(priority, msg);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:129:0x030c. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:128:0x030b A[PHI: r51
  0x030b: PHI (r51v20 'type' int) = 
  (r51v11 'type' int)
  (r51v12 'type' int)
  (r51v13 'type' int)
  (r51v14 'type' int)
  (r51v15 'type' int)
  (r51v16 'type' int)
  (r51v17 'type' int)
  (r51v21 'type' int)
 binds: [B:126:0x0307, B:123:0x02fb, B:120:0x02ef, B:117:0x02e3, B:114:0x02d6, B:111:0x02c8, B:108:0x02ba, B:106:0x02ae] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:251:0x05cf  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x05d7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:338:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:339:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void readPackageRestrictionsLPr(int r64, android.util.ArrayMap<java.lang.String, java.lang.Long> r65) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1554
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.readPackageRestrictionsLPr(int, android.util.ArrayMap):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:22:0x004b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.util.Map.Entry<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> readSuspensionParamsLPr(int r4, com.android.modules.utils.TypedXmlPullParser r5) throws java.io.IOException {
        /*
            java.lang.String r0 = "suspending-package"
            r1 = 0
            java.lang.String r0 = r5.getAttributeValue(r1, r0)
            if (r0 != 0) goto L12
            java.lang.String r2 = "PackageSettings"
            java.lang.String r3 = "No suspendingPackage found inside tag suspend-params"
            android.util.Slog.wtf(r2, r3)
            return r1
        L12:
            boolean r2 = android.app.admin.flags.Flags.crossUserSuspensionEnabledRo()
            if (r2 == 0) goto L54
            java.lang.String r2 = "suspending-user"
            r3 = -10000(0xffffffffffffd8f0, float:NaN)
            int r1 = r5.getAttributeInt(r1, r2, r3)
            if (r1 != r3) goto L55
            int r2 = r0.hashCode()
            r3 = 0
            switch(r2) {
                case -861391249: goto L41;
                case 3506402: goto L36;
                case 1547057220: goto L2c;
                default: goto L2b;
            }
        L2b:
            goto L4b
        L2c:
            java.lang.String r2 = "com.android.shell"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L2b
            r2 = 1
            goto L4c
        L36:
            java.lang.String r2 = "root"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L2b
            r2 = r3
            goto L4c
        L41:
            java.lang.String r2 = "android"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L2b
            r2 = 2
            goto L4c
        L4b:
            r2 = -1
        L4c:
            switch(r2) {
                case 0: goto L51;
                case 1: goto L51;
                case 2: goto L51;
                default: goto L4f;
            }
        L4f:
            r3 = r4
            goto L52
        L51:
        L52:
            r1 = r3
            goto L55
        L54:
            r1 = r4
        L55:
            android.content.pm.UserPackage r2 = android.content.pm.UserPackage.of(r1, r0)
            com.android.server.pm.pkg.SuspendParams r3 = com.android.server.pm.pkg.SuspendParams.restoreFromXml(r5)
            java.util.Map$Entry r2 = java.util.Map.entry(r2, r3)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.readSuspensionParamsLPr(int, com.android.modules.utils.TypedXmlPullParser):java.util.Map$Entry");
    }

    private static com.android.server.pm.pkg.ArchiveState parseArchiveState(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String installerTitle = parser.getAttributeValue((java.lang.String) null, ATTR_ARCHIVE_INSTALLER_TITLE);
        long archiveTimeMillis = parser.getAttributeLongHex((java.lang.String) null, ATTR_ARCHIVE_TIME, 0L);
        java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> activityInfos = parseArchiveActivityInfos(parser);
        if (installerTitle == null) {
            android.util.Slog.wtf(TAG, "parseArchiveState: installerTitle is null");
            return null;
        }
        if (activityInfos.size() < 1) {
            android.util.Slog.wtf(TAG, "parseArchiveState: activityInfos is empty");
            return null;
        }
        return new com.android.server.pm.pkg.ArchiveState(activityInfos, installerTitle, archiveTimeMillis);
    }

    private static java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> parseArchiveActivityInfos(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlPullParser typedXmlPullParser = parser;
        java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> activityInfos = new java.util.ArrayList<>();
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type == 3) {
                typedXmlPullParser = parser;
            } else if (type != 4) {
                java.lang.String tagName = parser.getName();
                if (!tagName.equals(TAG_ARCHIVE_ACTIVITY_INFO)) {
                    typedXmlPullParser = parser;
                } else {
                    java.lang.String title = typedXmlPullParser.getAttributeValue((java.lang.String) null, ATTR_ARCHIVE_ACTIVITY_TITLE);
                    java.lang.String originalComponentName = typedXmlPullParser.getAttributeValue((java.lang.String) null, ATTR_ARCHIVE_ORIGINAL_COMPONENT_NAME);
                    java.lang.String iconAttribute = typedXmlPullParser.getAttributeValue((java.lang.String) null, ATTR_ARCHIVE_ICON_PATH);
                    java.nio.file.Path iconPath = iconAttribute == null ? null : java.nio.file.Path.of(iconAttribute, new java.lang.String[0]);
                    java.lang.String monochromeAttribute = typedXmlPullParser.getAttributeValue((java.lang.String) null, ATTR_ARCHIVE_MONOCHROME_ICON_PATH);
                    java.nio.file.Path monochromeIconPath = monochromeAttribute != null ? java.nio.file.Path.of(monochromeAttribute, new java.lang.String[0]) : null;
                    if (title == null || originalComponentName == null || iconPath == null) {
                        android.util.Slog.wtf(TAG, android.text.TextUtils.formatSimple("Missing attributes in tag %s. %s: %s, %s: %s, %s: %s", new java.lang.Object[]{TAG_ARCHIVE_ACTIVITY_INFO, ATTR_ARCHIVE_ACTIVITY_TITLE, title, ATTR_ARCHIVE_ORIGINAL_COMPONENT_NAME, originalComponentName, ATTR_ARCHIVE_ICON_PATH, iconPath}));
                        typedXmlPullParser = parser;
                    } else {
                        android.content.ComponentName unflattenOriginalComponentName = android.content.ComponentName.unflattenFromString(originalComponentName);
                        if (unflattenOriginalComponentName == null) {
                            android.util.Slog.wtf(TAG, "Incorrect component name: " + originalComponentName + " from the attributes");
                        } else {
                            activityInfos.add(new com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo(title, unflattenOriginalComponentName, iconPath, monochromeIconPath));
                        }
                    }
                }
            }
        }
        return activityInfos;
    }

    void setBlockUninstallLPw(int userId, java.lang.String packageName, boolean blockUninstall) {
        android.util.ArraySet<java.lang.String> packages = this.mBlockUninstallPackages.get(userId);
        if (blockUninstall) {
            if (packages == null) {
                packages = new android.util.ArraySet<>();
                this.mBlockUninstallPackages.put(userId, packages);
            }
            packages.add(packageName);
            return;
        }
        if (packages != null) {
            packages.remove(packageName);
            if (packages.isEmpty()) {
                this.mBlockUninstallPackages.remove(userId);
            }
        }
    }

    void clearBlockUninstallLPw(int userId) {
        this.mBlockUninstallPackages.remove(userId);
    }

    boolean getBlockUninstallLPr(int userId, java.lang.String packageName) {
        android.util.ArraySet<java.lang.String> packages = this.mBlockUninstallPackages.get(userId);
        if (packages == null) {
            return false;
        }
        return packages.contains(packageName);
    }

    private android.util.ArraySet<java.lang.String> readComponentsLPr(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String componentName;
        android.util.ArraySet<java.lang.String> components = null;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals(TAG_ITEM) && (componentName = parser.getAttributeValue((java.lang.String) null, "name")) != null) {
                    if (components == null) {
                        components = new android.util.ArraySet<>();
                    }
                    components.add(componentName);
                }
            }
        }
        return components;
    }

    void writePreferredActivitiesLPr(com.android.modules.utils.TypedXmlSerializer serializer, int userId, boolean full) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        serializer.startTag((java.lang.String) null, "preferred-activities");
        com.android.server.pm.PreferredIntentResolver pir = this.mPreferredActivities.get(userId);
        if (pir != null) {
            for (F pa : pir.filterSet()) {
                if (pa != null) {
                    serializer.startTag((java.lang.String) null, TAG_ITEM);
                    pa.writeToXml(serializer, full);
                    serializer.endTag((java.lang.String) null, TAG_ITEM);
                }
            }
        }
        serializer.endTag((java.lang.String) null, "preferred-activities");
    }

    void writePersistentPreferredActivitiesLPr(com.android.modules.utils.TypedXmlSerializer serializer, int userId) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        serializer.startTag((java.lang.String) null, TAG_PERSISTENT_PREFERRED_ACTIVITIES);
        com.android.server.pm.PersistentPreferredIntentResolver ppir = this.mPersistentPreferredActivities.get(userId);
        if (ppir != null) {
            for (F ppa : ppir.filterSet()) {
                serializer.startTag((java.lang.String) null, TAG_ITEM);
                ppa.writeToXml(serializer);
                serializer.endTag((java.lang.String) null, TAG_ITEM);
            }
        }
        serializer.endTag((java.lang.String) null, TAG_PERSISTENT_PREFERRED_ACTIVITIES);
    }

    void writeCrossProfileIntentFiltersLPr(com.android.modules.utils.TypedXmlSerializer serializer, int userId) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        serializer.startTag((java.lang.String) null, TAG_CROSS_PROFILE_INTENT_FILTERS);
        com.android.server.pm.CrossProfileIntentResolver cpir = this.mCrossProfileIntentResolvers.get(userId);
        if (cpir != null) {
            for (F cpif : cpir.filterSet()) {
                serializer.startTag((java.lang.String) null, TAG_ITEM);
                cpif.writeToXml(serializer);
                serializer.endTag((java.lang.String) null, TAG_ITEM);
            }
        }
        serializer.endTag((java.lang.String) null, TAG_CROSS_PROFILE_INTENT_FILTERS);
    }

    void writeDefaultAppsLPr(org.xmlpull.v1.XmlSerializer serializer, int userId) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        java.lang.String defaultBrowser = this.mPendingDefaultBrowser.get(userId);
        writeDefaultApps(serializer, defaultBrowser);
    }

    static void writeDefaultApps(org.xmlpull.v1.XmlSerializer serializer, java.lang.String defaultBrowser) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        serializer.startTag(null, TAG_DEFAULT_APPS);
        if (!android.text.TextUtils.isEmpty(defaultBrowser)) {
            serializer.startTag(null, TAG_DEFAULT_BROWSER);
            serializer.attribute(null, "packageName", defaultBrowser);
            serializer.endTag(null, TAG_DEFAULT_BROWSER);
        }
        serializer.endTag(null, TAG_DEFAULT_APPS);
    }

    void writeBlockUninstallPackagesLPr(com.android.modules.utils.TypedXmlSerializer serializer, int userId) throws java.io.IOException {
        android.util.ArraySet<java.lang.String> packages = this.mBlockUninstallPackages.get(userId);
        if (packages != null) {
            serializer.startTag((java.lang.String) null, TAG_BLOCK_UNINSTALL_PACKAGES);
            for (int i = 0; i < packages.size(); i++) {
                serializer.startTag((java.lang.String) null, TAG_BLOCK_UNINSTALL);
                serializer.attribute((java.lang.String) null, "packageName", packages.valueAt(i));
                serializer.endTag((java.lang.String) null, TAG_BLOCK_UNINSTALL);
            }
            serializer.endTag((java.lang.String) null, TAG_BLOCK_UNINSTALL_PACKAGES);
        }
    }

    void writePackageRestrictionsLPr(int userId) {
        writePackageRestrictionsLPr(userId, false);
    }

    void writePackageRestrictionsLPr(final int userId, final boolean sync) throws java.lang.Throwable {
        invalidatePackageCache();
        final long startTime = android.os.SystemClock.uptimeMillis();
        if (sync) {
            lambda$writePackageRestrictionsLPr$1(userId, startTime, sync);
            return;
        }
        synchronized (this.mPackageRestrictionsLock) {
            int pending = this.mPendingAsyncPackageRestrictionsWrites.get(userId, 0) + 1;
            this.mPendingAsyncPackageRestrictionsWrites.put(userId, pending);
        }
        java.lang.Runnable r = new java.lang.Runnable() { // from class: com.android.server.pm.Settings$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$writePackageRestrictionsLPr$1(userId, startTime, sync);
            }
        };
        this.mHandler.obtainMessage(30, r).sendToTarget();
    }

    void writePackageRestrictions(java.lang.Integer[] userIds) {
        invalidatePackageCache();
        long startTime = android.os.SystemClock.uptimeMillis();
        for (java.lang.Integer num : userIds) {
            int userId = num.intValue();
            lambda$writePackageRestrictionsLPr$1(userId, startTime, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:160:0x02f5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:182:? A[SYNTHETIC] */
    /* JADX INFO: renamed from: writePackageRestrictions, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void lambda$writePackageRestrictionsLPr$1(int r18, long r19, boolean r21) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 767
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.lambda$writePackageRestrictionsLPr$1(int, long, boolean):void");
    }

    private void writeArchiveStateLPr(com.android.modules.utils.TypedXmlSerializer serializer, com.android.server.pm.pkg.ArchiveState archiveState) throws java.io.IOException {
        if (archiveState == null) {
            return;
        }
        serializer.startTag((java.lang.String) null, TAG_ARCHIVE_STATE);
        serializer.attribute((java.lang.String) null, ATTR_ARCHIVE_INSTALLER_TITLE, archiveState.getInstallerTitle());
        serializer.attributeLongHex((java.lang.String) null, ATTR_ARCHIVE_TIME, archiveState.getArchiveTimeMillis());
        for (com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo activityInfo : archiveState.getActivityInfos()) {
            serializer.startTag((java.lang.String) null, TAG_ARCHIVE_ACTIVITY_INFO);
            serializer.attribute((java.lang.String) null, ATTR_ARCHIVE_ACTIVITY_TITLE, activityInfo.getTitle());
            serializer.attribute((java.lang.String) null, ATTR_ARCHIVE_ORIGINAL_COMPONENT_NAME, activityInfo.getOriginalComponentName().flattenToString());
            if (activityInfo.getIconBitmap() != null) {
                serializer.attribute((java.lang.String) null, ATTR_ARCHIVE_ICON_PATH, activityInfo.getIconBitmap().toAbsolutePath().toString());
            }
            if (activityInfo.getMonochromeIconBitmap() != null) {
                serializer.attribute((java.lang.String) null, ATTR_ARCHIVE_MONOCHROME_ICON_PATH, activityInfo.getMonochromeIconBitmap().toAbsolutePath().toString());
            }
            serializer.endTag((java.lang.String) null, TAG_ARCHIVE_ACTIVITY_INFO);
        }
        serializer.endTag((java.lang.String) null, TAG_ARCHIVE_STATE);
    }

    void readInstallPermissionsLPr(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.pm.permission.LegacyPermissionState permissionsState, java.util.List<android.content.pm.UserInfo> users) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(TAG_ITEM)) {
                            java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
                            boolean granted = parser.getAttributeBoolean((java.lang.String) null, ATTR_GRANTED, true);
                            int flags = parser.getAttributeIntHex((java.lang.String) null, ATTR_FLAGS, 0);
                            for (android.content.pm.UserInfo user : users) {
                                permissionsState.putPermissionState(new com.android.server.pm.permission.LegacyPermissionState.PermissionState(name, false, granted, flags), user.id);
                            }
                        } else {
                            android.util.Slog.w("PackageManager", "Unknown element under <permissions>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void readUsesSdkLibLPw(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.pm.PackageSetting outPs) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String libName = parser.getAttributeValue((java.lang.String) null, "name");
        long libVersion = parser.getAttributeLong((java.lang.String) null, "version", -1L);
        boolean optional = parser.getAttributeBoolean((java.lang.String) null, ATTR_OPTIONAL, true);
        if (libName != null && libVersion >= 0) {
            outPs.setUsesSdkLibraries((java.lang.String[]) com.android.internal.util.ArrayUtils.appendElement(java.lang.String.class, outPs.getUsesSdkLibraries(), libName));
            outPs.setUsesSdkLibrariesVersionsMajor(com.android.internal.util.ArrayUtils.appendLong(outPs.getUsesSdkLibrariesVersionsMajor(), libVersion));
            outPs.setUsesSdkLibrariesOptional(com.android.internal.util.ArrayUtils.appendBoolean(outPs.getUsesSdkLibrariesOptional(), optional));
        }
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    void readUsesStaticLibLPw(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.pm.PackageSetting outPs) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String libName = parser.getAttributeValue((java.lang.String) null, "name");
        long libVersion = parser.getAttributeLong((java.lang.String) null, "version", -1L);
        if (libName != null && libVersion >= 0) {
            outPs.setUsesStaticLibraries((java.lang.String[]) com.android.internal.util.ArrayUtils.appendElement(java.lang.String.class, outPs.getUsesStaticLibraries(), libName));
            outPs.setUsesStaticLibrariesVersions(com.android.internal.util.ArrayUtils.appendLong(outPs.getUsesStaticLibrariesVersions(), libVersion));
        }
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    void writeUsesSdkLibLPw(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.String[] usesSdkLibraries, long[] usesSdkLibraryVersions, boolean[] usesSdkLibrariesOptional) throws java.io.IOException {
        if (com.android.internal.util.ArrayUtils.isEmpty(usesSdkLibraries) || com.android.internal.util.ArrayUtils.isEmpty(usesSdkLibraryVersions) || usesSdkLibraries.length != usesSdkLibraryVersions.length) {
            return;
        }
        int libCount = usesSdkLibraries.length;
        for (int i = 0; i < libCount; i++) {
            java.lang.String libName = usesSdkLibraries[i];
            long libVersion = usesSdkLibraryVersions[i];
            boolean libOptional = usesSdkLibrariesOptional[i];
            serializer.startTag((java.lang.String) null, TAG_USES_SDK_LIB);
            serializer.attribute((java.lang.String) null, "name", libName);
            serializer.attributeLong((java.lang.String) null, "version", libVersion);
            serializer.attributeBoolean((java.lang.String) null, ATTR_OPTIONAL, libOptional);
            serializer.endTag((java.lang.String) null, TAG_USES_SDK_LIB);
        }
    }

    void writeUsesStaticLibLPw(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.String[] usesStaticLibraries, long[] usesStaticLibraryVersions) throws java.io.IOException {
        if (com.android.internal.util.ArrayUtils.isEmpty(usesStaticLibraries) || com.android.internal.util.ArrayUtils.isEmpty(usesStaticLibraryVersions) || usesStaticLibraries.length != usesStaticLibraryVersions.length) {
            return;
        }
        int libCount = usesStaticLibraries.length;
        for (int i = 0; i < libCount; i++) {
            java.lang.String libName = usesStaticLibraries[i];
            long libVersion = usesStaticLibraryVersions[i];
            serializer.startTag((java.lang.String) null, TAG_USES_STATIC_LIB);
            serializer.attribute((java.lang.String) null, "name", libName);
            serializer.attributeLong((java.lang.String) null, "version", libVersion);
            serializer.endTag((java.lang.String) null, TAG_USES_STATIC_LIB);
        }
    }

    void readStoppedLPw() {
        int type;
        java.io.FileInputStream str = null;
        int i = 4;
        if (this.mBackupStoppedPackagesFilename.exists()) {
            try {
                str = new java.io.FileInputStream(this.mBackupStoppedPackagesFilename);
                this.mReadMessages.append("Reading from backup stopped packages file\n");
                com.android.server.pm.PackageManagerService.reportSettingsProblem(4, "Need to read from backup stopped packages file");
                if (this.mStoppedPackagesFilename.exists()) {
                    android.util.Slog.w("PackageManager", "Cleaning up stopped packages file " + this.mStoppedPackagesFilename);
                    this.mStoppedPackagesFilename.delete();
                }
            } catch (java.io.IOException e) {
            }
        }
        int i2 = 0;
        if (str == null) {
            try {
                if (!this.mStoppedPackagesFilename.exists()) {
                    this.mReadMessages.append("No stopped packages file found\n");
                    com.android.server.pm.PackageManagerService.reportSettingsProblem(4, "No stopped packages file file; assuming all started");
                    for (com.android.server.pm.PackageSetting pkg : this.mPackages.values()) {
                        pkg.setStopped(false, 0);
                        pkg.setNotLaunched(false, 0);
                    }
                    return;
                }
                str = new java.io.FileInputStream(this.mStoppedPackagesFilename);
            } catch (java.io.IOException e2) {
                this.mReadMessages.append("Error reading: " + e2.toString());
                com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Error reading stopped packages: " + e2);
                android.util.Slog.wtf("PackageManager", "Error reading package manager stopped packages", e2);
                return;
            } catch (org.xmlpull.v1.XmlPullParserException e3) {
                this.mReadMessages.append("Error reading: " + e3.toString());
                com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Error reading stopped packages: " + e3);
                android.util.Slog.wtf("PackageManager", "Error reading package manager stopped packages", e3);
                return;
            }
        }
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(str);
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            this.mReadMessages.append("No start tag found in stopped packages file\n");
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "No start tag found in package manager stopped packages");
            return;
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type2 = parser.next();
            if (type2 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type2 != 3 && type2 != i) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals(TAG_PACKAGE)) {
                    java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
                    com.android.server.pm.PackageSetting ps = this.mPackages.get(name);
                    if (ps != null) {
                        ps.setStopped(true, i2);
                        if (!"1".equals(parser.getAttributeValue((java.lang.String) null, ATTR_NOT_LAUNCHED))) {
                            i2 = 0;
                        } else {
                            i2 = 0;
                            ps.setNotLaunched(true, 0);
                        }
                    } else {
                        android.util.Slog.w("PackageManager", "No package known for stopped package " + name);
                    }
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                } else {
                    android.util.Slog.w("PackageManager", "Unknown element under <stopped-packages>: " + parser.getName());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
            i = 4;
        }
        str.close();
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x0260 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:144:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:145:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0253 A[Catch: all -> 0x025c, TRY_LEAVE, TryCatch #15 {all -> 0x025c, blocks: (B:93:0x024a, B:95:0x0253, B:69:0x0201), top: B:107:0x0201 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0258  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void writeLPr(com.android.server.pm.Computer r21, boolean r22) {
        /*
            Method dump skipped, instruction units count: 618
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.writeLPr(com.android.server.pm.Computer, boolean):void");
    }

    private void writeKernelRemoveUserLPr(int userId) {
        if (this.mKernelMappingFilename == null) {
            return;
        }
        java.io.File removeUserIdFile = new java.io.File(this.mKernelMappingFilename, "remove_userid");
        writeIntToFile(removeUserIdFile, userId);
    }

    void writeKernelMappingLPr() {
        if (this.mKernelMappingFilename == null) {
            return;
        }
        java.lang.String[] known = this.mKernelMappingFilename.list();
        android.util.ArraySet<java.lang.String> knownSet = new android.util.ArraySet<>(known.length);
        for (java.lang.String str : known) {
            knownSet.add(str);
        }
        for (com.android.server.pm.PackageSetting ps : this.mPackages.values()) {
            knownSet.remove(ps.getPackageName());
            writeKernelMappingLPr(ps);
        }
        for (int i = 0; i < knownSet.size(); i++) {
            java.lang.String name = knownSet.valueAt(i);
            this.mKernelMapping.remove(name);
            new java.io.File(this.mKernelMappingFilename, name).delete();
        }
    }

    void writeKernelMappingLPr(com.android.server.pm.PackageSetting ps) {
        if (this.mKernelMappingFilename == null || ps == null || ps.getPackageName() == null) {
            return;
        }
        writeKernelMappingLPr(ps.getPackageName(), ps.getAppId(), ps.getNotInstalledUserIds());
    }

    void writeKernelMappingLPr(java.lang.String name, int appId, int[] excludedUserIds) {
        com.android.server.pm.Settings.KernelPackageState cur = this.mKernelMapping.get(name);
        boolean firstTime = cur == null;
        boolean userIdsChanged = firstTime || !java.util.Arrays.equals(excludedUserIds, cur.excludedUserIds);
        java.io.File dir = new java.io.File(this.mKernelMappingFilename, name);
        if (firstTime) {
            dir.mkdir();
            cur = new com.android.server.pm.Settings.KernelPackageState();
            this.mKernelMapping.put(name, cur);
        }
        if (cur.appId != appId) {
            java.io.File appIdFile = new java.io.File(dir, "appid");
            writeIntToFile(appIdFile, appId);
        }
        if (userIdsChanged) {
            for (int i = 0; i < excludedUserIds.length; i++) {
                if (cur.excludedUserIds == null || !com.android.internal.util.ArrayUtils.contains(cur.excludedUserIds, excludedUserIds[i])) {
                    writeIntToFile(new java.io.File(dir, "excluded_userids"), excludedUserIds[i]);
                }
            }
            if (cur.excludedUserIds != null) {
                for (int i2 = 0; i2 < cur.excludedUserIds.length; i2++) {
                    if (!com.android.internal.util.ArrayUtils.contains(excludedUserIds, cur.excludedUserIds[i2])) {
                        writeIntToFile(new java.io.File(dir, "clear_userid"), cur.excludedUserIds[i2]);
                    }
                }
            }
            cur.excludedUserIds = excludedUserIds;
        }
    }

    private void writeIntToFile(java.io.File file, int value) {
        try {
            android.os.FileUtils.bytesToFile(file.getAbsolutePath(), java.lang.Integer.toString(value).getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Couldn't write " + value + " to " + file.getAbsolutePath());
        }
    }

    void writePackageListLPr() {
        writePackageListLPr(-1);
    }

    void writePackageListLPr(int creatingUserId) {
        java.lang.String filename = this.mPackageListFilename.getAbsolutePath();
        java.lang.String ctx = android.os.SELinux.fileSelabelLookup(filename);
        if (ctx == null) {
            android.util.Slog.wtf(TAG, "Failed to get SELinux context for " + this.mPackageListFilename.getAbsolutePath());
        }
        if (!android.os.SELinux.setFSCreateContext(ctx)) {
            android.util.Slog.wtf(TAG, "Failed to set packages.list SELinux context");
        }
        try {
            writePackageListLPrInternal(creatingUserId);
        } finally {
            android.os.SELinux.setFSCreateContext((java.lang.String) null);
        }
    }

    private void writePackageListLPrInternal(int creatingUserId) {
        int[] userIds;
        com.android.server.pm.Settings settings = this;
        java.util.List<android.content.pm.UserInfo> users = getActiveUsers(com.android.server.pm.UserManagerService.getInstance(), true);
        int[] userIds2 = new int[users.size()];
        for (int i = 0; i < userIds2.length; i++) {
            userIds2[i] = users.get(i).id;
        }
        if (creatingUserId != -1) {
            userIds2 = com.android.internal.util.ArrayUtils.appendInt(userIds2, creatingUserId);
        }
        java.io.File tempFile = new java.io.File(settings.mPackageListFilename.getAbsolutePath() + ".tmp");
        com.android.internal.util.JournaledFile journal = new com.android.internal.util.JournaledFile(settings.mPackageListFilename, tempFile);
        java.io.File writeTarget = journal.chooseForWrite();
        java.io.BufferedWriter writer = null;
        try {
            java.io.FileOutputStream fstr = new java.io.FileOutputStream(writeTarget);
            writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(fstr, java.nio.charset.Charset.defaultCharset()));
            android.os.FileUtils.setPermissions(fstr.getFD(), com.android.internal.util.FrameworkStatsLog.DISPLAY_HBM_STATE_CHANGED, 1000, 1032);
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (com.android.server.pm.PackageSetting ps : settings.mPackages.values()) {
                if (ps.getPkg() == null) {
                    try {
                        if (!com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(ps.getPackageName())) {
                            android.util.Slog.w(TAG, "Skipping " + ps + " due to missing metadata");
                        }
                    } catch (java.lang.Exception e) {
                        e = e;
                        android.util.Slog.wtf(TAG, "Failed to write packages.list", e);
                        libcore.io.IoUtils.closeQuietly(writer);
                        journal.rollback();
                        return;
                    }
                } else if (!ps.getPkg().isApex()) {
                    java.io.File dataDir = com.android.server.pm.parsing.PackageInfoUtils.getDataDir(ps, 0);
                    java.lang.String dataPath = dataDir == null ? "null" : dataDir.getAbsolutePath();
                    boolean isDebug = ps.getPkg().isDebuggable();
                    android.util.IntArray gids = new android.util.IntArray();
                    int length = userIds2.length;
                    int i2 = 0;
                    while (i2 < length) {
                        int userId = userIds2[i2];
                        java.util.List<android.content.pm.UserInfo> users2 = users;
                        try {
                            userIds = userIds2;
                        } catch (java.lang.Exception e2) {
                            e = e2;
                            android.util.Slog.wtf(TAG, "Failed to write packages.list", e);
                            libcore.io.IoUtils.closeQuietly(writer);
                            journal.rollback();
                            return;
                        }
                        try {
                            android.util.IntArray gids2 = gids;
                            gids2.addAll(settings.mPermissionDataProvider.getGidsForUid(android.os.UserHandle.getUid(userId, ps.getAppId())));
                            i2++;
                            settings = this;
                            gids = gids2;
                            users = users2;
                            userIds2 = userIds;
                        } catch (java.lang.Exception e3) {
                            e = e3;
                            android.util.Slog.wtf(TAG, "Failed to write packages.list", e);
                            libcore.io.IoUtils.closeQuietly(writer);
                            journal.rollback();
                            return;
                        }
                    }
                    java.util.List<android.content.pm.UserInfo> users3 = users;
                    int[] userIds3 = userIds2;
                    android.util.IntArray gids3 = gids;
                    if (dataPath.indexOf(32) >= 0) {
                        settings = this;
                        users = users3;
                        userIds2 = userIds3;
                    } else {
                        sb.setLength(0);
                        sb.append(ps.getPkg().getPackageName());
                        sb.append(" ");
                        sb.append(ps.getPkg().getUid());
                        sb.append(isDebug ? " 1 " : " 0 ");
                        sb.append(dataPath);
                        sb.append(" ");
                        sb.append(ps.getSeInfo());
                        sb.append(" ");
                        int gidsSize = gids3.size();
                        if (gids3.size() > 0) {
                            sb.append(gids3.get(0));
                            for (int i3 = 1; i3 < gidsSize; i3++) {
                                sb.append(",");
                                sb.append(gids3.get(i3));
                            }
                        } else {
                            sb.append("none");
                        }
                        sb.append(" ");
                        java.lang.String str = "1";
                        sb.append(ps.getPkg().isProfileableByShell() ? "1" : "0");
                        sb.append(" ");
                        sb.append(ps.getPkg().getLongVersionCode());
                        sb.append(" ");
                        if (!ps.getPkg().isProfileable()) {
                            str = "0";
                        }
                        sb.append(str);
                        sb.append(" ");
                        if (ps.isSystem()) {
                            sb.append("@system");
                        } else if (ps.isProduct()) {
                            sb.append("@product");
                        } else if (ps.getInstallSource().mInstallerPackageName != null && !ps.getInstallSource().mInstallerPackageName.isEmpty()) {
                            sb.append(ps.getInstallSource().mInstallerPackageName);
                        } else {
                            sb.append("@null");
                        }
                        sb.append("\n");
                        writer.append((java.lang.CharSequence) sb);
                        settings = this;
                        users = users3;
                        userIds2 = userIds3;
                    }
                }
            }
            writer.flush();
            android.os.FileUtils.sync(fstr);
            writer.close();
            journal.commit();
        } catch (java.lang.Exception e4) {
            e = e4;
        }
    }

    void writeDisabledSysPackageLPr(com.android.modules.utils.TypedXmlSerializer serializer, com.android.server.pm.PackageSetting pkg) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, "updated-package");
        serializer.attribute((java.lang.String) null, "name", pkg.getPackageName());
        if (pkg.getRealName() != null) {
            serializer.attribute((java.lang.String) null, "realName", pkg.getRealName());
        }
        serializer.attribute((java.lang.String) null, "codePath", pkg.getPathString());
        serializer.attributeLongHex((java.lang.String) null, "ft", pkg.getLastModifiedTime());
        serializer.attributeLongHex((java.lang.String) null, "ut", pkg.getLastUpdateTime());
        serializer.attributeLong((java.lang.String) null, "version", pkg.getVersionCode());
        serializer.attributeInt((java.lang.String) null, "targetSdkVersion", pkg.getTargetSdkVersion());
        if (pkg.getRestrictUpdateHash() != null) {
            serializer.attributeBytesBase64((java.lang.String) null, "restrictUpdateHash", pkg.getRestrictUpdateHash());
        }
        serializer.attributeBoolean((java.lang.String) null, "scannedAsStoppedSystemApp", pkg.isScannedAsStoppedSystemApp());
        if (pkg.getLegacyNativeLibraryPath() != null) {
            serializer.attribute((java.lang.String) null, "nativeLibraryPath", pkg.getLegacyNativeLibraryPath());
        }
        if (pkg.getPrimaryCpuAbiLegacy() != null) {
            serializer.attribute((java.lang.String) null, "primaryCpuAbi", pkg.getPrimaryCpuAbiLegacy());
        }
        if (pkg.getSecondaryCpuAbiLegacy() != null) {
            serializer.attribute((java.lang.String) null, "secondaryCpuAbi", pkg.getSecondaryCpuAbiLegacy());
        }
        if (pkg.getCpuAbiOverride() != null) {
            serializer.attribute((java.lang.String) null, "cpuAbiOverride", pkg.getCpuAbiOverride());
        }
        if (!pkg.hasSharedUser()) {
            serializer.attributeInt((java.lang.String) null, "userId", pkg.getAppId());
        } else {
            serializer.attributeInt((java.lang.String) null, "sharedUserId", pkg.getAppId());
        }
        serializer.attributeFloat((java.lang.String) null, "loadingProgress", pkg.getLoadingProgress());
        serializer.attributeLongHex((java.lang.String) null, "loadingCompletedTime", pkg.getLoadingCompletedTime());
        if (pkg.getAppMetadataFilePath() != null) {
            serializer.attribute((java.lang.String) null, "appMetadataFilePath", pkg.getAppMetadataFilePath());
        }
        serializer.attributeInt((java.lang.String) null, "appMetadataSource", pkg.getAppMetadataSource());
        writeUsesSdkLibLPw(serializer, pkg.getUsesSdkLibraries(), pkg.getUsesSdkLibrariesVersionsMajor(), pkg.getUsesSdkLibrariesOptional());
        writeUsesStaticLibLPw(serializer, pkg.getUsesStaticLibraries(), pkg.getUsesStaticLibrariesVersions());
        serializer.endTag((java.lang.String) null, "updated-package");
    }

    void writePackageLPr(com.android.modules.utils.TypedXmlSerializer serializer, java.util.ArrayList<android.content.pm.Signature> writtenSignatures, com.android.server.pm.PackageSetting pkg) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, "package");
        serializer.attribute((java.lang.String) null, "name", pkg.getPackageName());
        if (pkg.getRealName() != null) {
            serializer.attribute((java.lang.String) null, "realName", pkg.getRealName());
        }
        serializer.attribute((java.lang.String) null, "codePath", pkg.getPathString());
        if (pkg.getLegacyNativeLibraryPath() != null) {
            serializer.attribute((java.lang.String) null, "nativeLibraryPath", pkg.getLegacyNativeLibraryPath());
        }
        if (pkg.getPrimaryCpuAbiLegacy() != null) {
            serializer.attribute((java.lang.String) null, "primaryCpuAbi", pkg.getPrimaryCpuAbiLegacy());
        }
        if (pkg.getSecondaryCpuAbiLegacy() != null) {
            serializer.attribute((java.lang.String) null, "secondaryCpuAbi", pkg.getSecondaryCpuAbiLegacy());
        }
        if (pkg.getCpuAbiOverride() != null) {
            serializer.attribute((java.lang.String) null, "cpuAbiOverride", pkg.getCpuAbiOverride());
        }
        serializer.attributeInt((java.lang.String) null, "publicFlags", pkg.getFlags());
        serializer.attributeInt((java.lang.String) null, "privateFlags", pkg.getPrivateFlags());
        serializer.attributeLongHex((java.lang.String) null, "ft", pkg.getLastModifiedTime());
        serializer.attributeLongHex((java.lang.String) null, "ut", pkg.getLastUpdateTime());
        serializer.attributeLong((java.lang.String) null, "version", pkg.getVersionCode());
        serializer.attributeInt((java.lang.String) null, "targetSdkVersion", pkg.getTargetSdkVersion());
        if (pkg.getRestrictUpdateHash() != null) {
            serializer.attributeBytesBase64((java.lang.String) null, "restrictUpdateHash", pkg.getRestrictUpdateHash());
        }
        serializer.attributeBoolean((java.lang.String) null, "scannedAsStoppedSystemApp", pkg.isScannedAsStoppedSystemApp());
        if (!pkg.hasSharedUser()) {
            serializer.attributeInt((java.lang.String) null, "userId", pkg.getAppId());
            serializer.attributeBoolean((java.lang.String) null, "isSdkLibrary", pkg.getAndroidPackage() != null && pkg.getAndroidPackage().isSdkLibrary());
        } else {
            serializer.attributeInt((java.lang.String) null, "sharedUserId", pkg.getAppId());
        }
        com.android.server.pm.InstallSource installSource = pkg.getInstallSource();
        if (installSource.mInstallerPackageName != null) {
            serializer.attribute((java.lang.String) null, "installer", installSource.mInstallerPackageName);
        }
        if (installSource.mInstallerPackageUid != -1) {
            serializer.attributeInt((java.lang.String) null, "installerUid", installSource.mInstallerPackageUid);
        }
        if (installSource.mUpdateOwnerPackageName != null) {
            serializer.attribute((java.lang.String) null, "updateOwner", installSource.mUpdateOwnerPackageName);
        }
        if (installSource.mInstallerAttributionTag != null) {
            serializer.attribute((java.lang.String) null, "installerAttributionTag", installSource.mInstallerAttributionTag);
        }
        serializer.attributeInt((java.lang.String) null, "packageSource", installSource.mPackageSource);
        if (installSource.mIsOrphaned) {
            serializer.attributeBoolean((java.lang.String) null, "isOrphaned", true);
        }
        if (installSource.mInitiatingPackageName != null) {
            serializer.attribute((java.lang.String) null, "installInitiator", installSource.mInitiatingPackageName);
        }
        if (installSource.mIsInitiatingPackageUninstalled) {
            serializer.attributeBoolean((java.lang.String) null, "installInitiatorUninstalled", true);
        }
        if (installSource.mOriginatingPackageName != null) {
            serializer.attribute((java.lang.String) null, "installOriginator", installSource.mOriginatingPackageName);
        }
        if (pkg.getVolumeUuid() != null) {
            serializer.attribute((java.lang.String) null, ATTR_VOLUME_UUID, pkg.getVolumeUuid());
        }
        if (pkg.getCategoryOverride() != -1) {
            serializer.attributeInt((java.lang.String) null, "categoryHint", pkg.getCategoryOverride());
        }
        if (pkg.isUpdateAvailable()) {
            serializer.attributeBoolean((java.lang.String) null, "updateAvailable", true);
        }
        if (pkg.isForceQueryableOverride()) {
            serializer.attributeBoolean((java.lang.String) null, "forceQueryable", true);
        }
        if (pkg.isPendingRestore()) {
            serializer.attributeBoolean((java.lang.String) null, "pendingRestore", true);
        }
        if (pkg.isLoading()) {
            serializer.attributeBoolean((java.lang.String) null, "isLoading", true);
        }
        serializer.attributeFloat((java.lang.String) null, "loadingProgress", pkg.getLoadingProgress());
        serializer.attributeLongHex((java.lang.String) null, "loadingCompletedTime", pkg.getLoadingCompletedTime());
        serializer.attribute((java.lang.String) null, "domainSetId", pkg.getDomainSetId().toString());
        if (pkg.getAppMetadataFilePath() != null) {
            serializer.attribute((java.lang.String) null, "appMetadataFilePath", pkg.getAppMetadataFilePath());
        }
        serializer.attributeInt((java.lang.String) null, "appMetadataSource", pkg.getAppMetadataSource());
        writeUsesSdkLibLPw(serializer, pkg.getUsesSdkLibraries(), pkg.getUsesSdkLibrariesVersionsMajor(), pkg.getUsesSdkLibrariesOptional());
        writeUsesStaticLibLPw(serializer, pkg.getUsesStaticLibraries(), pkg.getUsesStaticLibrariesVersions());
        pkg.getSignatures().writeXml(serializer, "sigs", writtenSignatures);
        if (installSource.mInitiatingPackageSignatures != null) {
            installSource.mInitiatingPackageSignatures.writeXml(serializer, "install-initiator-sigs", writtenSignatures);
        }
        writeSigningKeySetLPr(serializer, pkg.getKeySetData());
        writeUpgradeKeySetsLPr(serializer, pkg.getKeySetData());
        writeKeySetAliasesLPr(serializer, pkg.getKeySetData());
        writeMimeGroupLPr(serializer, pkg.getMimeGroups());
        serializer.endTag((java.lang.String) null, "package");
    }

    void writeSigningKeySetLPr(com.android.modules.utils.TypedXmlSerializer serializer, com.android.server.pm.PackageKeySetData data) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, "proper-signing-keyset");
        serializer.attributeLong((java.lang.String) null, "identifier", data.getProperSigningKeySet());
        serializer.endTag((java.lang.String) null, "proper-signing-keyset");
    }

    void writeUpgradeKeySetsLPr(com.android.modules.utils.TypedXmlSerializer serializer, com.android.server.pm.PackageKeySetData data) throws java.io.IOException {
        if (data.isUsingUpgradeKeySets()) {
            for (long id : data.getUpgradeKeySets()) {
                serializer.startTag((java.lang.String) null, "upgrade-keyset");
                serializer.attributeLong((java.lang.String) null, "identifier", id);
                serializer.endTag((java.lang.String) null, "upgrade-keyset");
            }
        }
    }

    void writeKeySetAliasesLPr(com.android.modules.utils.TypedXmlSerializer serializer, com.android.server.pm.PackageKeySetData data) throws java.io.IOException {
        for (java.util.Map.Entry<java.lang.String, java.lang.Long> e : data.getAliases().entrySet()) {
            serializer.startTag((java.lang.String) null, "defined-keyset");
            serializer.attribute((java.lang.String) null, "alias", e.getKey());
            serializer.attributeLong((java.lang.String) null, "identifier", e.getValue().longValue());
            serializer.endTag((java.lang.String) null, "defined-keyset");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x02fe  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0307 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:144:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:146:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean readSettingsLPw(com.android.server.pm.Computer r22, java.util.List<android.content.pm.UserInfo> r23, android.util.ArrayMap<java.lang.String, java.lang.Long> r24) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 785
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.readSettingsLPw(com.android.server.pm.Computer, java.util.List, android.util.ArrayMap):boolean");
    }

    boolean readLPw(com.android.server.pm.Computer computer, java.util.List<android.content.pm.UserInfo> users) throws java.lang.Throwable {
        android.util.ArrayMap<java.lang.String, java.lang.Long> originalFirstInstallTimes = new android.util.ArrayMap<>();
        try {
            if (!readSettingsLPw(computer, users, originalFirstInstallTimes)) {
                return false;
            }
            if (!this.mVersion.containsKey(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL)) {
                android.util.Slog.wtf("PackageManager", "No internal VersionInfo found in settings, using current.");
                findOrCreateVersion(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL).forceCurrent();
            }
            if (!this.mVersion.containsKey("primary_physical")) {
                android.util.Slog.wtf("PackageManager", "No external VersionInfo found in settings, using current.");
                findOrCreateVersion("primary_physical").forceCurrent();
            }
            int N = this.mPendingPackages.size();
            for (int i = 0; i < N; i++) {
                com.android.server.pm.PackageSetting p = this.mPendingPackages.get(i);
                int sharedUserAppId = p.getSharedUserAppId();
                if (sharedUserAppId > 0) {
                    java.lang.Object idObj = getSettingLPr(sharedUserAppId);
                    if (idObj instanceof com.android.server.pm.SharedUserSetting) {
                        com.android.server.pm.SharedUserSetting sharedUser = (com.android.server.pm.SharedUserSetting) idObj;
                        addPackageSettingLPw(p, sharedUser);
                    } else if (idObj != null) {
                        java.lang.String msg = "Bad package setting: package " + p.getPackageName() + " has shared uid " + sharedUserAppId + " that is not a shared uid\n";
                        this.mReadMessages.append(msg);
                        com.android.server.pm.PackageManagerService.reportSettingsProblem(6, msg);
                    } else {
                        java.lang.String msg2 = "Bad package setting: package " + p.getPackageName() + " has shared uid " + sharedUserAppId + " that is not defined\n";
                        this.mReadMessages.append(msg2);
                        com.android.server.pm.PackageManagerService.reportSettingsProblem(6, msg2);
                    }
                }
            }
            this.mPendingPackages.clear();
            if (this.mBackupStoppedPackagesFilename.exists() || this.mStoppedPackagesFilename.exists()) {
                readStoppedLPw();
                this.mBackupStoppedPackagesFilename.delete();
                this.mStoppedPackagesFilename.delete();
                writePackageRestrictionsLPr(0, true);
            } else {
                java.util.Iterator<android.content.pm.UserInfo> it = users.iterator();
                while (it.hasNext()) {
                    readPackageRestrictionsLPr(it.next().id, originalFirstInstallTimes);
                }
            }
            for (android.content.pm.UserInfo user : users) {
                this.mRuntimePermissionsPersistence.readStateForUserSync(user.id, getInternalVersion(), this.mPackages, this.mSharedUsers, getUserRuntimePermissionsFile(user.id));
            }
            for (com.android.server.pm.PackageSetting disabledPs : this.mDisabledSysPackages.values()) {
                java.lang.Object id = getSettingLPr(disabledPs.getAppId());
                if (id instanceof com.android.server.pm.SharedUserSetting) {
                    com.android.server.pm.SharedUserSetting sharedUserSetting = (com.android.server.pm.SharedUserSetting) id;
                    sharedUserSetting.mDisabledPackages.add(disabledPs);
                    disabledPs.setSharedUserAppId(sharedUserSetting.mAppId);
                }
            }
            this.mReadMessages.append("Read completed successfully: ").append(this.mPackages.size()).append(" packages, ").append(this.mSharedUsers.size()).append(" shared uids\n");
            writeKernelMappingLPr();
            return true;
        } finally {
            if (!this.mVersion.containsKey(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL)) {
                android.util.Slog.wtf("PackageManager", "No internal VersionInfo found in settings, using current.");
                findOrCreateVersion(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL).forceCurrent();
            }
            if (!this.mVersion.containsKey("primary_physical")) {
                android.util.Slog.wtf("PackageManager", "No external VersionInfo found in settings, using current.");
                findOrCreateVersion("primary_physical").forceCurrent();
            }
        }
    }

    void readPermissionStateForUserSyncLPr(int userId) throws java.lang.Throwable {
        this.mRuntimePermissionsPersistence.readStateForUserSync(userId, getInternalVersion(), this.mPackages, this.mSharedUsers, getUserRuntimePermissionsFile(userId));
    }

    com.android.permission.persistence.RuntimePermissionsState getLegacyPermissionsState(int userId) {
        return this.mRuntimePermissionsPersistence.getLegacyPermissionsState(userId, this.mPackages, this.mSharedUsers);
    }

    void applyDefaultPreferredAppsLPw(int userId) {
        android.content.pm.PackageManagerInternal pmInternal;
        int size;
        android.content.pm.PackageManagerInternal pmInternal2;
        int size2;
        com.android.server.pm.ScanPartition partition;
        java.lang.Throwable th;
        int type;
        android.content.pm.PackageManagerInternal pmInternal3 = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        for (com.android.server.pm.PackageSetting ps : this.mPackages.values()) {
            if ((1 & ps.getFlags()) != 0 && ps.getPkg() != null && !ps.getPkg().getPreferredActivityFilters().isEmpty()) {
                java.util.List<android.util.Pair<java.lang.String, com.android.internal.pm.pkg.component.ParsedIntentInfo>> intents = ps.getPkg().getPreferredActivityFilters();
                for (int i = 0; i < intents.size(); i++) {
                    android.util.Pair<java.lang.String, com.android.internal.pm.pkg.component.ParsedIntentInfo> pair = intents.get(i);
                    applyDefaultPreferredActivityLPw(pmInternal3, ((com.android.internal.pm.pkg.component.ParsedIntentInfo) pair.second).getIntentFilter(), new android.content.ComponentName(ps.getPackageName(), (java.lang.String) pair.first), userId);
                }
            }
        }
        int size3 = com.android.server.pm.PackageManagerService.SYSTEM_PARTITIONS.size();
        int index = 0;
        while (index < size3) {
            com.android.server.pm.ScanPartition partition2 = com.android.server.pm.PackageManagerService.SYSTEM_PARTITIONS.get(index);
            java.io.File preferredDir = new java.io.File(partition2.getFolder(), "etc/preferred-apps");
            if (!preferredDir.exists()) {
                pmInternal = pmInternal3;
                size = size3;
            } else if (!preferredDir.isDirectory()) {
                pmInternal = pmInternal3;
                size = size3;
            } else if (preferredDir.canRead()) {
                java.io.File[] files = preferredDir.listFiles();
                if (com.android.internal.util.ArrayUtils.isEmpty(files)) {
                    pmInternal = pmInternal3;
                    size = size3;
                } else {
                    int length = files.length;
                    int i2 = 0;
                    while (i2 < length) {
                        java.io.File f = files[i2];
                        if (!f.getPath().endsWith(".xml")) {
                            android.util.Slog.i(TAG, "Non-xml file " + f + " in " + preferredDir + " directory, ignoring");
                            pmInternal2 = pmInternal3;
                            size2 = size3;
                            partition = partition2;
                        } else if (f.canRead()) {
                            if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                                pmInternal2 = pmInternal3;
                                android.util.Log.d(TAG, "Reading default preferred " + f);
                            } else {
                                pmInternal2 = pmInternal3;
                            }
                            try {
                                java.io.InputStream str = new java.io.FileInputStream(f);
                                try {
                                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(str);
                                    while (true) {
                                        size2 = size3;
                                        try {
                                            int type2 = parser.next();
                                            partition = partition2;
                                            if (type2 == 2) {
                                                type = type2;
                                                break;
                                            }
                                            type = type2;
                                            if (type == 1) {
                                                break;
                                            }
                                            size3 = size2;
                                            partition2 = partition;
                                        } catch (java.lang.Throwable th2) {
                                            partition = partition2;
                                            th = th2;
                                        }
                                    }
                                    if (type != 2) {
                                        try {
                                            android.util.Slog.w(TAG, "Preferred apps file " + f + " does not have start tag");
                                            try {
                                                str.close();
                                            } catch (java.io.IOException e) {
                                                e = e;
                                                android.util.Slog.w(TAG, "Error reading apps file " + f, e);
                                            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                                                e = e2;
                                                android.util.Slog.w(TAG, "Error reading apps file " + f, e);
                                            }
                                        } catch (java.lang.Throwable th3) {
                                            th = th3;
                                            try {
                                                str.close();
                                            } catch (java.lang.Throwable th4) {
                                                th.addSuppressed(th4);
                                            }
                                            throw th;
                                        }
                                    } else if ("preferred-activities".equals(parser.getName())) {
                                        readDefaultPreferredActivitiesLPw(parser, userId);
                                        str.close();
                                    } else {
                                        android.util.Slog.w(TAG, "Preferred apps file " + f + " does not start with 'preferred-activities'");
                                        str.close();
                                    }
                                } catch (java.lang.Throwable th5) {
                                    size2 = size3;
                                    partition = partition2;
                                    th = th5;
                                }
                            } catch (java.io.IOException e3) {
                                e = e3;
                                size2 = size3;
                                partition = partition2;
                            } catch (org.xmlpull.v1.XmlPullParserException e4) {
                                e = e4;
                                size2 = size3;
                                partition = partition2;
                            }
                        } else {
                            android.util.Slog.w(TAG, "Preferred apps file " + f + " cannot be read");
                            pmInternal2 = pmInternal3;
                            size2 = size3;
                            partition = partition2;
                        }
                        i2++;
                        pmInternal3 = pmInternal2;
                        size3 = size2;
                        partition2 = partition;
                    }
                    pmInternal = pmInternal3;
                    size = size3;
                }
            } else {
                android.util.Slog.w(TAG, "Directory " + preferredDir + " cannot be read");
                pmInternal = pmInternal3;
                size = size3;
            }
            index++;
            pmInternal3 = pmInternal;
            size3 = size;
        }
    }

    static void removeFilters(com.android.server.pm.PreferredIntentResolver pir, com.android.server.pm.WatchedIntentFilter filter, java.util.List<com.android.server.pm.PreferredActivity> existing) {
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
            android.util.Slog.i(TAG, existing.size() + " preferred matches for:");
            filter.dump(new android.util.LogPrinter(4, TAG), "  ");
        }
        for (int i = existing.size() - 1; i >= 0; i--) {
            com.android.server.pm.PreferredActivity pa = existing.get(i);
            if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                android.util.Slog.i(TAG, "Removing preferred activity " + pa.mPref.mComponent + ":");
                pa.dump(new android.util.LogPrinter(4, TAG), "  ");
            }
            pir.removeFilter(pa);
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:7:0x002d */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void applyDefaultPreferredActivityLPw(android.content.pm.PackageManagerInternal r31, android.content.IntentFilter r32, android.content.ComponentName r33, int r34) {
        /*
            Method dump skipped, instruction units count: 609
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.applyDefaultPreferredActivityLPw(android.content.pm.PackageManagerInternal, android.content.IntentFilter, android.content.ComponentName, int):void");
    }

    private void applyDefaultPreferredActivityLPw(android.content.pm.PackageManagerInternal pmInternal, android.content.Intent intent, int flags, android.content.ComponentName cn, java.lang.String scheme, android.os.PatternMatcher ssp, android.content.IntentFilter.AuthorityEntry auth, android.os.PatternMatcher path, int userId) {
        android.content.ComponentName haveNonSys;
        java.util.List<android.content.pm.ResolveInfo> ri = pmInternal.queryIntentActivities(intent, intent.getType(), flags, android.os.Binder.getCallingUid(), userId);
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
            android.util.Log.d(TAG, "Queried " + intent + " results: " + ri);
        }
        int numMatches = ri == null ? 0 : ri.size();
        if (numMatches < 1) {
            android.util.Slog.w(TAG, "No potential matches found for " + intent + " while setting preferred " + cn.flattenToShortString());
            return;
        }
        android.content.ComponentName haveNonSys2 = null;
        android.content.ComponentName[] set = new android.content.ComponentName[ri.size()];
        int i = 0;
        boolean haveAct = false;
        int systemMatch = 0;
        while (true) {
            if (i >= numMatches) {
                break;
            }
            android.content.pm.ActivityInfo ai = ri.get(i).activityInfo;
            int numMatches2 = numMatches;
            set[i] = new android.content.ComponentName(ai.packageName, ai.name);
            if ((ai.applicationInfo.flags & 1) == 0) {
                if (ri.get(i).match < 0) {
                    haveNonSys = haveNonSys2;
                } else {
                    if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                        android.util.Log.d(TAG, "Result " + ai.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + ai.name + ": non-system!");
                    }
                    haveNonSys2 = set[i];
                }
            } else {
                haveNonSys = haveNonSys2;
                if (cn.getPackageName().equals(ai.packageName) && cn.getClassName().equals(ai.name)) {
                    if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                        android.util.Log.d(TAG, "Result " + ai.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + ai.name + ": default!");
                    }
                    haveAct = true;
                    systemMatch = ri.get(i).match;
                } else {
                    boolean haveAct2 = com.android.server.pm.PackageManagerService.DEBUG_PREFERRED;
                    if (haveAct2) {
                        android.util.Log.d(TAG, "Result " + ai.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + ai.name + ": skipped");
                    }
                }
            }
            i++;
            haveNonSys2 = haveNonSys;
            numMatches = numMatches2;
        }
        if (haveNonSys2 != null && 0 < systemMatch) {
            haveNonSys2 = null;
        }
        if (haveAct && haveNonSys2 == null) {
            com.android.server.pm.WatchedIntentFilter filter = new com.android.server.pm.WatchedIntentFilter();
            if (intent.getAction() != null) {
                filter.addAction(intent.getAction());
            }
            if (intent.getCategories() != null) {
                for (java.lang.String cat : intent.getCategories()) {
                    filter.addCategory(cat);
                }
            }
            if ((65536 & flags) != 0) {
                filter.addCategory("android.intent.category.DEFAULT");
            }
            if (scheme != null) {
                filter.addDataScheme(scheme);
            }
            if (ssp != null) {
                filter.addDataSchemeSpecificPart(ssp.getPath(), ssp.getType());
            }
            if (auth != null) {
                filter.addDataAuthority(auth);
            }
            if (path != null) {
                filter.addDataPath(path);
            }
            if (intent.getType() != null) {
                try {
                    filter.addDataType(intent.getType());
                } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
                    android.util.Slog.w(TAG, "Malformed mimetype " + intent.getType() + " for " + cn);
                }
            }
            com.android.server.pm.PreferredIntentResolver pir = editPreferredActivitiesLPw(userId);
            java.util.List<com.android.server.pm.PreferredActivity> existing = pir.findFilters(filter);
            if (existing != null) {
                removeFilters(pir, filter, existing);
            }
            com.android.server.pm.PreferredActivity pa = new com.android.server.pm.PreferredActivity(filter, systemMatch, set, cn, true);
            pir.addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) null, pa);
            return;
        }
        if (haveNonSys2 == null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("No component ");
            sb.append(cn.flattenToShortString());
            sb.append(" found setting preferred ");
            sb.append(intent);
            sb.append("; possible matches are ");
            for (int i2 = 0; i2 < set.length; i2++) {
                if (i2 > 0) {
                    sb.append(", ");
                }
                sb.append(set[i2].flattenToShortString());
            }
            android.util.Slog.w(TAG, sb.toString());
            return;
        }
        android.util.Slog.i(TAG, "Not setting preferred " + intent + "; found third party match " + haveNonSys2.flattenToShortString());
    }

    private void readDefaultPreferredActivitiesLPw(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.content.pm.PackageManagerInternal pmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(TAG_ITEM)) {
                            com.android.server.pm.PreferredActivity tmpPa = new com.android.server.pm.PreferredActivity(parser);
                            if (tmpPa.mPref.getParseError() == null) {
                                applyDefaultPreferredActivityLPw(pmInternal, tmpPa.getIntentFilter(), tmpPa.mPref.mComponent, userId);
                            } else {
                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <preferred-activity> " + tmpPa.mPref.getParseError() + " at " + parser.getPositionDescription());
                            }
                        } else {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <preferred-activities>: " + parser.getName());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readDisabledSysPackageLPw(com.android.modules.utils.TypedXmlPullParser parser, java.util.List<android.content.pm.UserInfo> users) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String primaryCpuAbiStr;
        com.android.server.pm.Settings settings;
        com.android.server.pm.Settings settings2;
        com.android.server.pm.permission.LegacyPermissionState legacyState;
        int outerDepth;
        java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
        java.lang.String realName = parser.getAttributeValue((java.lang.String) null, "realName");
        java.lang.String codePathStr = parser.getAttributeValue((java.lang.String) null, "codePath");
        java.lang.String legacyCpuAbiStr = parser.getAttributeValue((java.lang.String) null, "requiredCpuAbi");
        java.lang.String legacyNativeLibraryPathStr = parser.getAttributeValue((java.lang.String) null, "nativeLibraryPath");
        java.lang.String primaryCpuAbiStr2 = parser.getAttributeValue((java.lang.String) null, "primaryCpuAbi");
        java.lang.String secondaryCpuAbiStr = parser.getAttributeValue((java.lang.String) null, "secondaryCpuAbi");
        java.lang.String cpuAbiOverrideStr = parser.getAttributeValue((java.lang.String) null, "cpuAbiOverride");
        if (primaryCpuAbiStr2 == null && legacyCpuAbiStr != null) {
            primaryCpuAbiStr = legacyCpuAbiStr;
        } else {
            primaryCpuAbiStr = primaryCpuAbiStr2;
        }
        long versionCode = parser.getAttributeLong((java.lang.String) null, "version", 0L);
        int targetSdkVersion = parser.getAttributeInt((java.lang.String) null, "targetSdkVersion", 0);
        byte[] restrictUpdateHash = parser.getAttributeBytesBase64((java.lang.String) null, "restrictUpdateHash", (byte[]) null);
        boolean isScannedAsStoppedSystemApp = parser.getAttributeBoolean((java.lang.String) null, "scannedAsStoppedSystemApp", false);
        int pkgPrivateFlags = 0;
        int pkgFlags = 0 | 1;
        if (codePathStr.contains("/priv-app/")) {
            pkgPrivateFlags = 0 | 8;
        }
        java.util.UUID domainSetId = com.android.server.pm.verify.domain.DomainVerificationManagerInternal.DISABLED_ID;
        com.android.server.pm.PackageSetting ps = new com.android.server.pm.PackageSetting(name, realName, new java.io.File(codePathStr), pkgFlags, pkgPrivateFlags, domainSetId).setLegacyNativeLibraryPath(legacyNativeLibraryPathStr).setPrimaryCpuAbi(primaryCpuAbiStr).setSecondaryCpuAbi(secondaryCpuAbiStr).setCpuAbiOverride(cpuAbiOverrideStr).setLongVersionCode(versionCode).setTargetSdkVersion(targetSdkVersion).setRestrictUpdateHash(restrictUpdateHash).setScannedAsStoppedSystemApp(isScannedAsStoppedSystemApp);
        long timeStamp = parser.getAttributeLongHex((java.lang.String) null, "ft", 0L);
        ps.setLastModifiedTime(timeStamp == 0 ? parser.getAttributeLong((java.lang.String) null, "ts", 0L) : timeStamp);
        ps.setLastUpdateTime(parser.getAttributeLongHex((java.lang.String) null, "ut", 0L));
        ps.setAppId(parseAppId(parser));
        if (ps.getAppId() <= 0) {
            int sharedUserAppId = parseSharedUserAppId(parser);
            ps.setAppId(sharedUserAppId);
            ps.setSharedUserAppId(sharedUserAppId);
        }
        ps.setAppMetadataFilePath(parser.getAttributeValue((java.lang.String) null, "appMetadataFilePath"));
        ps.setAppMetadataSource(parser.getAttributeInt((java.lang.String) null, "appMetadataSource", 0));
        int outerDepth2 = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1) {
                settings = this;
                break;
            }
            if (type == 3 && parser.getDepth() <= outerDepth2) {
                settings = this;
                break;
            }
            if (type != 3 && type != 4) {
                if (parser.getName().equals(TAG_PERMISSIONS)) {
                    if (ps.hasSharedUser()) {
                        settings2 = this;
                        com.android.server.pm.SettingBase sharedUserSettings = settings2.getSettingLPr(ps.getSharedUserAppId());
                        legacyState = sharedUserSettings != null ? sharedUserSettings.getLegacyPermissionState() : null;
                    } else {
                        settings2 = this;
                        legacyState = ps.getLegacyPermissionState();
                    }
                    if (legacyState == null) {
                        outerDepth = outerDepth2;
                    } else {
                        outerDepth = outerDepth2;
                        settings2.readInstallPermissionsLPr(parser, legacyState, users);
                    }
                    outerDepth2 = outerDepth;
                } else {
                    int outerDepth3 = outerDepth2;
                    if (parser.getName().equals(TAG_USES_STATIC_LIB)) {
                        readUsesStaticLibLPw(parser, ps);
                        outerDepth2 = outerDepth3;
                    } else if (parser.getName().equals(TAG_USES_SDK_LIB)) {
                        readUsesSdkLibLPw(parser, ps);
                        outerDepth2 = outerDepth3;
                    } else {
                        com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <updated-package>: " + parser.getName());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        outerDepth2 = outerDepth3;
                    }
                }
            }
        }
        settings.mDisabledSysPackages.put(name, ps);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(17:112|(2:114|(2:(2:(17:393|122|123|442|124|125|387|126|127|430|128|129|408|130|(1:132)|133|202)(10:144|412|145|146|458|147|148|401|149|150)|263)(12:157|426|158|159|440|160|161|383|162|163|375|164)|(0)(0))(1:119))(1:176)|450|177|178|379|179|180|432|181|385|182|(2:444|184)|(2:186|187)(16:190|191|428|192|193|389|194|195|436|196|197|377|198|199|418|200)|201|202|(0)(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(30:0|2|373|3|4|391|5|6|(22:454|7|8|424|9|10|438|11|12|414|13|14|448|15|16|434|17|18|416|19|20|(10:456|21|22|446|23|24|(1:28)(1:27)|29|30|(10:462|31|32|33|34|452|35|(3:422|37|38)(1:41)|42|43)))|(7:403|45|46|48|49|(3:460|51|52)(1:54)|(6:265|(6:395|267|268|405|269|270)(1:286)|287|(2:288|(3:296|(3:469|298|(3:468|300|473)(5:467|301|(1:303)(2:304|(1:306)(2:307|(1:309)(2:310|(4:312|(3:314|(1:316)(1:317)|318)(1:319)|(1:321)(1:322)|323)(3:324|(3:326|(1:328)(1:329)|330)(2:331|(0)(2:334|(1:336)(2:337|(3:339|(1:341)(1:342)|343)(2:344|(1:346)(2:347|(1:349)(2:350|(3:352|(1:354)|355)(2:356|(1:358)(2:359|(1:361)(1:362)))))))))|333))))|363|472))(3:466|364|471)|470)(0))|366|(2:368|475)(2:369|474))(2:371|372))(3:55|56|(10:381|58|59|61|(1:63)|64|(1:66)|67|(1:69)|70)(3:71|72|(5:74|410|75|(1:77)(1:78)|79)(1:82)))|83|84|85|86|(2:88|89)(1:90)|91|92|397|93|94|420|95|96|(2:399|98)|(2:100|101)(1:104)|407|(1:106)(1:(2:110|111)(17:112|(2:114|(2:(2:(17:393|122|123|442|124|125|387|126|127|430|128|129|408|130|(1:132)|133|202)(10:144|412|145|146|458|147|148|401|149|150)|263)(12:157|426|158|159|440|160|161|383|162|163|375|164)|(0)(0))(1:119))(1:176)|450|177|178|379|179|180|432|181|385|182|(2:444|184)|(2:186|187)(16:190|191|428|192|193|389|194|195|436|196|197|377|198|199|418|200)|201|202|(0)(0)))|165|202|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0315, code lost:
    
        r2 = r11;
        r73 = r16;
        r18 = r0;
        r63 = " has bad appId ";
        r72 = r61;
        r17 = r0;
        r16 = r14;
        r6 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x0784, code lost:
    
        r40 = r1;
        r1 = r10;
        r3 = r13;
        r16 = r17;
        r2 = r35;
        r6 = r0;
        r4 = r60;
        r5 = r61;
        r34 = r64;
        r17 = r11;
        r7 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x07a4, code lost:
    
        r1 = r10;
        r3 = r13;
        r16 = r17;
        r2 = r35;
        r6 = r0;
        r4 = r60;
        r5 = r61;
        r34 = r64;
        r17 = r11;
        r7 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x07c2, code lost:
    
        r63 = r10;
        r73 = r16;
        r72 = r61;
        r3 = r3;
        r16 = r17;
        r2 = r11;
        r6 = r0;
        r4 = r60;
        r5 = r5;
        r34 = r14;
        r17 = r62;
        r7 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x07f3, code lost:
    
        r63 = r10;
        r73 = r16;
        r72 = r61;
        r3 = r3;
        r16 = r17;
        r2 = r11;
        r6 = r0;
        r4 = r4;
        r5 = r5;
        r34 = r14;
        r17 = r62;
        r7 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x0826, code lost:
    
        r73 = r16;
        r63 = " has bad appId ";
        r72 = r61;
        r18 = r0;
        r3 = r3;
        r7 = r7;
        r16 = r14;
        r6 = r0;
        r4 = r4;
        r5 = r5;
        r2 = r2;
        r1 = r1;
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x0857, code lost:
    
        r73 = r16;
        r63 = " has bad appId ";
        r72 = r61;
        r18 = r0;
        r1 = r1;
        r7 = r7;
        r16 = r14;
        r6 = r0;
        r4 = r4;
        r5 = r5;
        r17 = r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0a48  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0df6  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 11 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 25 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 6 */
    /* JADX WARN: Unreachable blocks removed: 3, instructions: 12 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readPackageLPw(com.android.modules.utils.TypedXmlPullParser r84, java.util.ArrayList<android.content.pm.Signature> r85, android.util.ArrayMap<java.lang.Long, java.lang.Integer> r86, java.util.List<android.content.pm.UserInfo> r87, android.util.ArrayMap<java.lang.String, java.lang.Long> r88) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 3616
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.readPackageLPw(com.android.modules.utils.TypedXmlPullParser, java.util.ArrayList, android.util.ArrayMap, java.util.List, android.util.ArrayMap):void");
    }

    private static int parseAppId(com.android.modules.utils.TypedXmlPullParser parser) {
        return parser.getAttributeInt((java.lang.String) null, "userId", 0);
    }

    private static int parseSharedUserAppId(com.android.modules.utils.TypedXmlPullParser parser) {
        return parser.getAttributeInt((java.lang.String) null, "sharedUserId", 0);
    }

    void addInstallerPackageNames(com.android.server.pm.InstallSource installSource) {
        if (installSource.mInstallerPackageName != null) {
            this.mInstallerPackages.add(installSource.mInstallerPackageName);
        }
        if (installSource.mInitiatingPackageName != null) {
            this.mInstallerPackages.add(installSource.mInitiatingPackageName);
        }
        if (installSource.mOriginatingPackageName != null) {
            this.mInstallerPackages.add(installSource.mOriginatingPackageName);
        }
    }

    private android.util.Pair<java.lang.String, java.util.Set<java.lang.String>> readMimeGroupLPw(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String groupName = parser.getAttributeValue((java.lang.String) null, "name");
        if (groupName == null) {
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            return null;
        }
        java.util.Set<java.lang.String> mimeTypes = new android.util.ArraySet<>();
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type != 3 && type != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals(TAG_MIME_TYPE)) {
                    java.lang.String typeName = parser.getAttributeValue((java.lang.String) null, ATTR_VALUE);
                    if (typeName != null) {
                        mimeTypes.add(typeName);
                    }
                } else {
                    com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <mime-group>: " + parser.getName());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        return android.util.Pair.create(groupName, mimeTypes);
    }

    private void writeMimeGroupLPr(com.android.modules.utils.TypedXmlSerializer serializer, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mimeGroups) throws java.io.IOException {
        if (mimeGroups == null) {
            return;
        }
        for (java.lang.String mimeGroup : mimeGroups.keySet()) {
            serializer.startTag((java.lang.String) null, TAG_MIME_GROUP);
            serializer.attribute((java.lang.String) null, "name", mimeGroup);
            for (java.lang.String mimeType : mimeGroups.get(mimeGroup)) {
                serializer.startTag((java.lang.String) null, TAG_MIME_TYPE);
                serializer.attribute((java.lang.String) null, ATTR_VALUE, mimeType);
                serializer.endTag((java.lang.String) null, TAG_MIME_TYPE);
            }
            serializer.endTag((java.lang.String) null, TAG_MIME_GROUP);
        }
    }

    private void readDisabledComponentsLPw(com.android.server.pm.PackageSetting packageSetting, com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(TAG_ITEM)) {
                            java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
                            if (name != null) {
                                packageSetting.addDisabledComponent(name.intern(), userId);
                            } else {
                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <disabled-components> has no name at " + parser.getPositionDescription());
                            }
                        } else {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <disabled-components>: " + parser.getName());
                        }
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readEnabledComponentsLPw(com.android.server.pm.PackageSetting packageSetting, com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(TAG_ITEM)) {
                            java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
                            if (name != null) {
                                packageSetting.addEnabledComponent(name.intern(), userId);
                            } else {
                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <enabled-components> has no name at " + parser.getPositionDescription());
                            }
                        } else {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <enabled-components>: " + parser.getName());
                        }
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readSharedUserLPw(com.android.modules.utils.TypedXmlPullParser parser, java.util.ArrayList<android.content.pm.Signature> readSignatures, java.util.List<android.content.pm.UserInfo> users) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int pkgFlags = 0;
        com.android.server.pm.SharedUserSetting su = null;
        java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
        int appId = parseAppId(parser);
        if (parser.getAttributeBoolean((java.lang.String) null, "system", false)) {
            pkgFlags = 0 | 1;
        }
        if (name == null) {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <shared-user> has no name at " + parser.getPositionDescription());
        } else if (appId != 0) {
            com.android.server.pm.SharedUserSetting sharedUserSettingAddSharedUserLPw = addSharedUserLPw(name.intern(), appId, pkgFlags, 0);
            su = sharedUserSettingAddSharedUserLPw;
            if (sharedUserSettingAddSharedUserLPw == null) {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(6, "Occurred while parsing settings at " + parser.getPositionDescription());
            }
        } else {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: shared-user " + name + " has bad appId " + appId + " at " + parser.getPositionDescription());
        }
        if (su != null) {
            int outerDepth = parser.getDepth();
            while (true) {
                int type = parser.next();
                if (type != 1) {
                    if (type != 3 || parser.getDepth() > outerDepth) {
                        if (type != 3 && type != 4) {
                            java.lang.String tagName = parser.getName();
                            if (tagName.equals("sigs")) {
                                su.signatures.readXml(parser, readSignatures);
                            } else if (tagName.equals(TAG_PERMISSIONS)) {
                                readInstallPermissionsLPr(parser, su.getLegacyPermissionState(), users);
                            } else {
                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <shared-user>: " + parser.getName());
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                            }
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        } else {
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void createNewUserLI(com.android.server.pm.PackageManagerService r29, com.android.server.pm.Installer r30, int r31, java.util.Set<java.lang.String> r32, java.lang.String[] r33) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 509
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.createNewUserLI(com.android.server.pm.PackageManagerService, com.android.server.pm.Installer, int, java.util.Set, java.lang.String[]):void");
    }

    void removeUserLPw(int userId) {
        java.util.Set<java.util.Map.Entry<java.lang.String, com.android.server.pm.PackageSetting>> entries = this.mPackages.entrySet();
        for (java.util.Map.Entry<java.lang.String, com.android.server.pm.PackageSetting> entry : entries) {
            entry.getValue().removeUser(userId);
        }
        this.mPreferredActivities.remove(userId);
        synchronized (this.mPackageRestrictionsLock) {
            getUserPackagesStateFile(userId).delete();
            this.mPendingAsyncPackageRestrictionsWrites.delete(userId);
        }
        removeCrossProfileIntentFiltersLPw(userId);
        this.mRuntimePermissionsPersistence.onUserRemoved(userId);
        this.mDomainVerificationManager.clearUser(userId);
        this.mSettingsExt.onRemoveUserLPw(userId);
        writePackageListLPr();
        writeKernelRemoveUserLPr(userId);
    }

    void removeCrossProfileIntentFiltersLPw(int userId) {
        synchronized (this.mCrossProfileIntentResolvers) {
            if (this.mCrossProfileIntentResolvers.get(userId) != null) {
                this.mCrossProfileIntentResolvers.remove(userId);
                writePackageRestrictionsLPr(userId);
            }
            int count = this.mCrossProfileIntentResolvers.size();
            for (int i = 0; i < count; i++) {
                int sourceUserId = this.mCrossProfileIntentResolvers.keyAt(i);
                com.android.server.pm.CrossProfileIntentResolver cpir = this.mCrossProfileIntentResolvers.get(sourceUserId);
                boolean needsWriting = false;
                android.util.ArraySet<com.android.server.pm.CrossProfileIntentFilter> cpifs = new android.util.ArraySet<>((java.util.Collection<? extends com.android.server.pm.CrossProfileIntentFilter>) cpir.filterSet());
                for (com.android.server.pm.CrossProfileIntentFilter cpif : cpifs) {
                    if (cpif.getTargetUserId() == userId) {
                        needsWriting = true;
                        cpir.removeFilter(cpif);
                    }
                }
                if (needsWriting) {
                    writePackageRestrictionsLPr(sourceUserId);
                }
            }
        }
    }

    public android.content.pm.VerifierDeviceIdentity getVerifierDeviceIdentityLPw(com.android.server.pm.Computer computer) {
        if (this.mVerifierDeviceIdentity == null) {
            this.mVerifierDeviceIdentity = android.content.pm.VerifierDeviceIdentity.generate();
            writeLPr(computer, false);
        }
        return this.mVerifierDeviceIdentity;
    }

    public com.android.server.pm.PackageSetting getDisabledSystemPkgLPr(java.lang.String name) {
        com.android.server.pm.PackageSetting ps = this.mDisabledSysPackages.get(name);
        return ps;
    }

    public com.android.server.pm.PackageSetting getDisabledSystemPkgLPr(com.android.server.pm.PackageSetting enabledPackageSetting) {
        if (enabledPackageSetting == null) {
            return null;
        }
        return getDisabledSystemPkgLPr(enabledPackageSetting.getPackageName());
    }

    int getApplicationEnabledSettingLPr(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        com.android.server.pm.PackageSetting pkg = this.mPackages.get(packageName);
        if (pkg == null) {
            throw new android.content.pm.PackageManager.NameNotFoundException(packageName);
        }
        return pkg.getEnabled(userId);
    }

    int getComponentEnabledSettingLPr(android.content.ComponentName componentName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        java.lang.String packageName = componentName.getPackageName();
        com.android.server.pm.PackageSetting pkg = this.mPackages.get(packageName);
        if (pkg == null) {
            throw new android.content.pm.PackageManager.NameNotFoundException(componentName.getPackageName());
        }
        java.lang.String classNameStr = componentName.getClassName();
        return pkg.getCurrentEnabledStateLPr(classNameStr, userId);
    }

    com.android.server.pm.SharedUserSetting getSharedUserSettingLPr(java.lang.String packageName) {
        com.android.server.pm.PackageSetting ps = this.mPackages.get(packageName);
        return getSharedUserSettingLPr(ps);
    }

    com.android.server.pm.SharedUserSetting getSharedUserSettingLPr(com.android.server.pm.PackageSetting ps) {
        if (ps == null || !ps.hasSharedUser()) {
            return null;
        }
        return (com.android.server.pm.SharedUserSetting) getSettingLPr(ps.getSharedUserAppId());
    }

    private static java.util.List<android.content.pm.UserInfo> getAllUsers(com.android.server.pm.UserManagerService userManager) {
        return getUsers(userManager, false, false);
    }

    private static java.util.List<android.content.pm.UserInfo> getActiveUsers(com.android.server.pm.UserManagerService userManager, boolean excludeDying) {
        return getUsers(userManager, excludeDying, true);
    }

    private static java.util.List<android.content.pm.UserInfo> getUsers(com.android.server.pm.UserManagerService userManager, boolean excludeDying, boolean excludePreCreated) {
        long id = android.os.Binder.clearCallingIdentity();
        try {
            java.util.List<android.content.pm.UserInfo> users = userManager.getUsers(true, excludeDying, excludePreCreated);
            android.os.Binder.restoreCallingIdentity(id);
            return users;
        } catch (java.lang.NullPointerException e) {
            android.os.Binder.restoreCallingIdentity(id);
            return null;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(id);
            throw th;
        }
    }

    java.util.List<? extends com.android.server.pm.pkg.PackageStateInternal> getVolumePackagesLPr(java.lang.String volumeUuid) {
        java.util.ArrayList<com.android.server.pm.pkg.PackageStateInternal> res = new java.util.ArrayList<>();
        for (int i = 0; i < this.mPackages.size(); i++) {
            com.android.server.pm.PackageSetting setting = this.mPackages.valueAt(i);
            if (java.util.Objects.equals(volumeUuid, setting.getVolumeUuid())) {
                res.add(setting);
            }
        }
        return res;
    }

    static void printFlags(java.io.PrintWriter pw, int val, java.lang.Object[] spec) {
        pw.print("[ ");
        for (int i = 0; i < spec.length; i += 2) {
            int mask = ((java.lang.Integer) spec[i]).intValue();
            if ((val & mask) != 0) {
                pw.print(spec[i + 1]);
                pw.print(" ");
            }
        }
        pw.print("]");
    }

    void dumpVersionLPr(com.android.internal.util.IndentingPrintWriter pw) {
        pw.increaseIndent();
        for (int i = 0; i < this.mVersion.size(); i++) {
            java.lang.String volumeUuid = this.mVersion.keyAt(i);
            com.android.server.pm.Settings.VersionInfo ver = this.mVersion.valueAt(i);
            if (java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, volumeUuid)) {
                pw.println("Internal:");
            } else if (java.util.Objects.equals("primary_physical", volumeUuid)) {
                pw.println("External:");
            } else {
                pw.println("UUID " + volumeUuid + ":");
            }
            pw.increaseIndent();
            pw.printPair(ATTR_SDK_VERSION, java.lang.Integer.valueOf(ver.sdkVersion));
            pw.printPair(ATTR_DATABASE_VERSION, java.lang.Integer.valueOf(ver.databaseVersion));
            pw.println();
            pw.printPair(ATTR_BUILD_FINGERPRINT, ver.buildFingerprint);
            pw.printPair(ATTR_FINGERPRINT, ver.fingerprint);
            pw.println();
            pw.decreaseIndent();
        }
        pw.decreaseIndent();
    }

    @dalvik.annotation.optimization.NeverCompile
    void dumpPackageLPr(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String checkinTag, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.PackageSetting ps, com.android.server.pm.permission.LegacyPermissionState permissionsState, java.text.SimpleDateFormat sdf, java.util.Date date, java.util.List<android.content.pm.UserInfo> users, boolean dumpAll, boolean dumpAllComponents) {
        java.lang.String str;
        java.util.Iterator<java.util.Map.Entry<java.lang.String, android.content.pm.overlay.OverlayPaths>> it;
        int apkSigningVersion;
        com.android.server.pm.Settings settings = this;
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
        if (checkinTag != null) {
            pw.print(checkinTag);
            pw.print(",");
            pw.print(ps.getRealName() != null ? ps.getRealName() : ps.getPackageName());
            pw.print(",");
            pw.print(ps.getAppId());
            pw.print(",");
            pw.print(ps.getVersionCode());
            pw.print(",");
            pw.print(ps.getLastUpdateTime());
            pw.print(",");
            java.lang.String str2 = "?";
            pw.print(ps.getInstallSource().mInstallerPackageName != null ? ps.getInstallSource().mInstallerPackageName : "?");
            pw.print(ps.getInstallSource().mInstallerPackageUid);
            pw.print(ps.getInstallSource().mUpdateOwnerPackageName != null ? ps.getInstallSource().mUpdateOwnerPackageName : "?");
            pw.print(ps.getInstallSource().mInstallerAttributionTag != null ? "(" + ps.getInstallSource().mInstallerAttributionTag + ")" : "");
            pw.print(",");
            pw.print(ps.getInstallSource().mPackageSource);
            pw.println();
            java.lang.String str3 = "-";
            if (pkg != null) {
                pw.print(checkinTag);
                pw.print("-");
                pw.print("splt,");
                pw.print("base,");
                pw.println(pkg.getBaseRevisionCode());
                int[] splitRevisionCodes = pkg.getSplitRevisionCodes();
                for (int i = 0; i < pkg.getSplitNames().length; i++) {
                    pw.print(checkinTag);
                    pw.print("-");
                    pw.print("splt,");
                    pw.print(pkg.getSplitNames()[i]);
                    pw.print(",");
                    pw.println(splitRevisionCodes[i]);
                }
            }
            for (android.content.pm.UserInfo user : users) {
                com.android.server.pm.pkg.PackageUserStateInternal userState = ps.getUserStateOrDefault(user.id);
                pw.print(checkinTag);
                pw.print(str3);
                pw.print("usr");
                pw.print(",");
                pw.print(user.id);
                pw.print(",");
                pw.print(userState.isInstalled() ? "I" : "i");
                pw.print(userState.isHidden() ? "B" : "b");
                pw.print(userState.isSuspended() ? "SU" : "su");
                pw.print(userState.isStopped() ? "S" : "s");
                pw.print(userState.isNotLaunched() ? "l" : "L");
                pw.print(userState.isInstantApp() ? "IA" : "ia");
                pw.print(userState.isVirtualPreload() ? "VPI" : "vpi");
                pw.print(userState.isQuarantined() ? "Q" : "q");
                pw.print(userState.getHarmfulAppWarning() != null ? "HA" : "ha");
                pw.print(",");
                pw.print(userState.getEnabledState());
                java.lang.String lastDisabledAppCaller = userState.getLastDisableAppCaller();
                pw.print(",");
                java.lang.String str4 = str3;
                pw.print(lastDisabledAppCaller != null ? lastDisabledAppCaller : str2);
                pw.print(",");
                pw.print(ps.readUserState(user.id).getFirstInstallTimeMillis());
                pw.print(",");
                pw.println();
                str3 = str4;
                str2 = str2;
            }
            return;
        }
        pw.print(prefix);
        pw.print("Package [");
        pw.print(ps.getRealName() != null ? ps.getRealName() : ps.getPackageName());
        pw.print("] (");
        pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(ps)));
        pw.println("):");
        if (ps.getRealName() != null) {
            pw.print(prefix);
            pw.print("  compat name=");
            pw.println(ps.getPackageName());
        }
        pw.print(prefix);
        pw.print("  appId=");
        pw.println(ps.getAppId());
        com.android.server.pm.SharedUserSetting sharedUserSetting = settings.getSharedUserSettingLPr(ps);
        if (sharedUserSetting != null) {
            pw.print(prefix);
            pw.print("  sharedUser=");
            pw.println(sharedUserSetting);
        }
        pw.print(prefix);
        pw.print("  pkg=");
        pw.println(pkg);
        pw.print(prefix);
        pw.print("  codePath=");
        pw.println(ps.getPathString());
        if (ps.getOldPaths() != null && ps.getOldPaths().size() > 0) {
            for (java.io.File oldPath : ps.getOldPaths()) {
                pw.print(prefix);
                pw.println("    oldCodePath=" + oldPath.getAbsolutePath());
            }
        }
        if (permissionNames == null) {
            pw.print(prefix);
            pw.print("  resourcePath=");
            pw.println(ps.getPathString());
            pw.print(prefix);
            pw.print("  legacyNativeLibraryDir=");
            pw.println(ps.getLegacyNativeLibraryPath());
            pw.print(prefix);
            pw.print("  extractNativeLibs=");
            pw.println((ps.getFlags() & 268435456) != 0 ? "true" : "false");
            pw.print(prefix);
            pw.print("  primaryCpuAbi=");
            pw.println(ps.getPrimaryCpuAbiLegacy());
            pw.print(prefix);
            pw.print("  secondaryCpuAbi=");
            pw.println(ps.getSecondaryCpuAbiLegacy());
            pw.print(prefix);
            pw.print("  cpuAbiOverride=");
            pw.println(ps.getCpuAbiOverride());
        }
        pw.print(prefix);
        pw.print("  versionCode=");
        pw.print(ps.getVersionCode());
        if (pkg != null) {
            pw.print(" minSdk=");
            pw.print(pkg.getMinSdkVersion());
        }
        pw.print(" targetSdk=");
        pw.println(ps.getTargetSdkVersion());
        if (pkg != null) {
            android.util.SparseIntArray minExtensionVersions = pkg.getMinExtensionVersions();
            pw.print(prefix);
            pw.print("  minExtensionVersions=[");
            if (minExtensionVersions != null) {
                java.util.List<java.lang.String> minExtVerStrings = new java.util.ArrayList<>();
                int size = minExtensionVersions.size();
                int index = 0;
                while (index < size) {
                    int key = minExtensionVersions.keyAt(index);
                    int size2 = size;
                    int value = minExtensionVersions.valueAt(index);
                    minExtVerStrings.add(key + "=" + value);
                    index++;
                    size = size2;
                    minExtensionVersions = minExtensionVersions;
                }
                pw.print(android.text.TextUtils.join(", ", minExtVerStrings));
            }
            pw.print("]");
        }
        pw.println();
        if (pkg != null) {
            pw.print(prefix);
            pw.print("  versionName=");
            pw.println(pkg.getVersionName());
            pw.print(prefix);
            pw.print("  hiddenApiEnforcementPolicy=");
            pw.println(ps.getHiddenApiEnforcementPolicy());
            pw.print(prefix);
            pw.print("  usesNonSdkApi=");
            pw.println(pkg.isNonSdkApiRequested());
            pw.print(prefix);
            pw.print("  splits=");
            dumpSplitNames(pw, pkg);
            pw.println();
            int apkSigningVersion2 = pkg.getSigningDetails().getSignatureSchemeVersion();
            pw.print(prefix);
            pw.print("  apkSigningVersion=");
            pw.println(apkSigningVersion2);
            pw.print(prefix);
            pw.print("  flags=");
            printFlags(pw, com.android.server.pm.parsing.PackageInfoUtils.appInfoFlags((com.android.server.pm.pkg.AndroidPackage) pkg, (com.android.server.pm.pkg.PackageStateInternal) ps), FLAG_DUMP_SPEC);
            pw.println();
            int privateFlags = com.android.server.pm.parsing.PackageInfoUtils.appInfoPrivateFlags((com.android.server.pm.pkg.AndroidPackage) pkg, (com.android.server.pm.pkg.PackageStateInternal) ps);
            if (privateFlags != 0) {
                pw.print(prefix);
                pw.print("  privateFlags=");
                printFlags(pw, privateFlags, PRIVATE_FLAG_DUMP_SPEC);
                pw.println();
            }
            if (ps.isPendingRestore()) {
                pw.print(prefix);
                pw.print("  pendingRestore=true");
                pw.println();
            }
            if (!pkg.isUpdatableSystem()) {
                pw.print(prefix);
                pw.print("  updatableSystem=false");
                pw.println();
            }
            if (pkg.getEmergencyInstaller() != null) {
                pw.print(prefix);
                pw.print("  emergencyInstaller=");
                pw.println(pkg.getEmergencyInstaller());
            }
            if (pkg.hasPreserveLegacyExternalStorage()) {
                pw.print(prefix);
                pw.print("  hasPreserveLegacyExternalStorage=true");
                pw.println();
            }
            pw.print(prefix);
            pw.print("  forceQueryable=");
            pw.print(ps.getPkg().isForceQueryable());
            if (ps.isForceQueryableOverride()) {
                pw.print(" (override=true)");
            }
            pw.println();
            if (!ps.getPkg().getQueriesPackages().isEmpty()) {
                pw.append((java.lang.CharSequence) prefix).append((java.lang.CharSequence) "  queriesPackages=").println(ps.getPkg().getQueriesPackages());
            }
            if (!ps.getPkg().getQueriesIntents().isEmpty()) {
                pw.append((java.lang.CharSequence) prefix).append((java.lang.CharSequence) "  queriesIntents=").println(ps.getPkg().getQueriesIntents());
            }
            pw.print(prefix);
            pw.print("  scannedAsStoppedSystemApp=");
            pw.println(ps.isScannedAsStoppedSystemApp());
            pw.print(prefix);
            pw.print("  supportsScreens=[");
            boolean first = true;
            if (pkg.isSmallScreensSupported()) {
                if (1 == 0) {
                    pw.print(", ");
                }
                first = false;
                pw.print("small");
            }
            if (pkg.isNormalScreensSupported()) {
                if (!first) {
                    pw.print(", ");
                }
                first = false;
                pw.print("medium");
            }
            if (pkg.isLargeScreensSupported()) {
                if (!first) {
                    pw.print(", ");
                }
                first = false;
                pw.print("large");
            }
            if (pkg.isExtraLargeScreensSupported()) {
                if (!first) {
                    pw.print(", ");
                }
                first = false;
                pw.print("xlarge");
            }
            if (pkg.isResizeable()) {
                if (!first) {
                    pw.print(", ");
                }
                first = false;
                pw.print("resizeable");
            }
            if (pkg.isAnyDensity()) {
                if (!first) {
                    pw.print(", ");
                }
                first = false;
                pw.print("anyDensity");
            }
            pw.println("]");
            java.util.List<java.lang.String> libraryNames = pkg.getLibraryNames();
            if (libraryNames != null && libraryNames.size() > 0) {
                pw.print(prefix);
                pw.println("  dynamic libraries:");
                for (int i2 = 0; i2 < libraryNames.size(); i2++) {
                    pw.print(prefix);
                    pw.print("    ");
                    pw.println(libraryNames.get(i2));
                }
            }
            java.lang.String str5 = " version:";
            if (pkg.getStaticSharedLibraryName() == null) {
                apkSigningVersion = apkSigningVersion2;
            } else {
                pw.print(prefix);
                pw.println("  static library:");
                pw.print(prefix);
                pw.print("    ");
                pw.print("name:");
                pw.print(pkg.getStaticSharedLibraryName());
                pw.print(" version:");
                apkSigningVersion = apkSigningVersion2;
                pw.println(pkg.getStaticSharedLibraryVersion());
            }
            if (pkg.getSdkLibraryName() != null) {
                pw.print(prefix);
                pw.println("  SDK library:");
                pw.print(prefix);
                pw.print("    ");
                pw.print("name:");
                pw.print(pkg.getSdkLibraryName());
                pw.print(" versionMajor:");
                pw.println(pkg.getSdkLibVersionMajor());
            }
            java.util.List<java.lang.String> usesLibraries = pkg.getUsesLibraries();
            if (usesLibraries.size() > 0) {
                pw.print(prefix);
                pw.println("  usesLibraries:");
                for (int i3 = 0; i3 < usesLibraries.size(); i3++) {
                    pw.print(prefix);
                    pw.print("    ");
                    pw.println(usesLibraries.get(i3));
                }
            }
            java.util.List<java.lang.String> usesStaticLibraries = pkg.getUsesStaticLibraries();
            long[] usesStaticLibrariesVersions = pkg.getUsesStaticLibrariesVersions();
            if (usesStaticLibraries.size() > 0) {
                pw.print(prefix);
                pw.println("  usesStaticLibraries:");
                int i4 = 0;
                while (true) {
                    java.util.List<java.lang.String> usesLibraries2 = usesLibraries;
                    if (i4 >= usesStaticLibraries.size()) {
                        break;
                    }
                    pw.print(prefix);
                    pw.print("    ");
                    pw.print(usesStaticLibraries.get(i4));
                    pw.print(" version:");
                    pw.println(usesStaticLibrariesVersions[i4]);
                    i4++;
                    usesLibraries = usesLibraries2;
                    usesStaticLibraries = usesStaticLibraries;
                }
            }
            java.util.List<java.lang.String> usesSdkLibraries = pkg.getUsesSdkLibraries();
            long[] usesSdkLibrariesVersionsMajor = pkg.getUsesSdkLibrariesVersionsMajor();
            boolean[] usesSdkLibrariesOptional = pkg.getUsesSdkLibrariesOptional();
            if (usesSdkLibraries.size() > 0) {
                pw.print(prefix);
                pw.println("  usesSdkLibraries:");
                int size3 = usesSdkLibraries.size();
                int i5 = 0;
                while (i5 < size3) {
                    pw.print(prefix);
                    pw.print("    ");
                    pw.print(usesSdkLibraries.get(i5));
                    pw.print(str5);
                    pw.println(usesSdkLibrariesVersionsMajor[i5]);
                    pw.print(" optional:");
                    pw.println(usesSdkLibrariesOptional[i5]);
                    i5++;
                    str5 = str5;
                    usesSdkLibraries = usesSdkLibraries;
                }
            }
            java.util.List<java.lang.String> usesOptionalLibraries = pkg.getUsesOptionalLibraries();
            if (usesOptionalLibraries.size() > 0) {
                pw.print(prefix);
                pw.println("  usesOptionalLibraries:");
                for (int i6 = 0; i6 < usesOptionalLibraries.size(); i6++) {
                    pw.print(prefix);
                    pw.print("    ");
                    pw.println(usesOptionalLibraries.get(i6));
                }
            }
            java.util.List<java.lang.String> usesNativeLibraries = pkg.getUsesNativeLibraries();
            if (usesNativeLibraries.size() > 0) {
                pw.print(prefix);
                pw.println("  usesNativeLibraries:");
                for (int i7 = 0; i7 < usesNativeLibraries.size(); i7++) {
                    pw.print(prefix);
                    pw.print("    ");
                    pw.println(usesNativeLibraries.get(i7));
                }
            }
            java.util.List<java.lang.String> usesOptionalNativeLibraries = pkg.getUsesOptionalNativeLibraries();
            if (usesOptionalNativeLibraries.size() > 0) {
                pw.print(prefix);
                pw.println("  usesOptionalNativeLibraries:");
                int i8 = 0;
                while (true) {
                    java.util.List<java.lang.String> usesOptionalLibraries2 = usesOptionalLibraries;
                    if (i8 >= usesOptionalNativeLibraries.size()) {
                        break;
                    }
                    pw.print(prefix);
                    pw.print("    ");
                    pw.println(usesOptionalNativeLibraries.get(i8));
                    i8++;
                    usesOptionalLibraries = usesOptionalLibraries2;
                }
            }
            java.util.List<java.lang.String> usesLibraryFiles = ps.getPkgState().getUsesLibraryFiles();
            if (usesLibraryFiles.size() > 0) {
                pw.print(prefix);
                pw.println("  usesLibraryFiles:");
                int i9 = 0;
                while (true) {
                    java.util.List<java.lang.String> usesNativeLibraries2 = usesNativeLibraries;
                    if (i9 >= usesLibraryFiles.size()) {
                        break;
                    }
                    pw.print(prefix);
                    pw.print("    ");
                    pw.println(usesLibraryFiles.get(i9));
                    i9++;
                    usesNativeLibraries = usesNativeLibraries2;
                }
            }
            java.util.Map<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> procs = pkg.getProcesses();
            if (!procs.isEmpty()) {
                pw.print(prefix);
                pw.println("  processes:");
                for (com.android.internal.pm.pkg.component.ParsedProcess proc : procs.values()) {
                    pw.print(prefix);
                    pw.print("    ");
                    java.util.List<java.lang.String> usesLibraryFiles2 = usesLibraryFiles;
                    pw.println(proc.getName());
                    if (proc.getDeniedPermissions() != null) {
                        java.util.Iterator it2 = proc.getDeniedPermissions().iterator();
                        while (it2.hasNext()) {
                            java.util.Iterator it3 = it2;
                            java.lang.String deniedPermission = (java.lang.String) it2.next();
                            pw.print(prefix);
                            pw.print("      deny: ");
                            pw.println(deniedPermission);
                            procs = procs;
                            it2 = it3;
                        }
                    }
                    usesLibraryFiles = usesLibraryFiles2;
                    procs = procs;
                }
            }
        }
        pw.print(prefix);
        pw.print("  timeStamp=");
        date.setTime(ps.getLastModifiedTime());
        pw.println(sdf.format(date));
        pw.print(prefix);
        pw.print("  lastUpdateTime=");
        date.setTime(ps.getLastUpdateTime());
        pw.println(sdf.format(date));
        pw.print(prefix);
        pw.print("  installerPackageName=");
        pw.println(ps.getInstallSource().mInstallerPackageName);
        pw.print(prefix);
        pw.print("  installerPackageUid=");
        pw.println(ps.getInstallSource().mInstallerPackageUid);
        pw.print(prefix);
        pw.print("  initiatingPackageName=");
        pw.println(ps.getInstallSource().mInitiatingPackageName);
        pw.print(prefix);
        pw.print("  originatingPackageName=");
        pw.println(ps.getInstallSource().mOriginatingPackageName);
        if (ps.getInstallSource().mUpdateOwnerPackageName != null) {
            pw.print(prefix);
            pw.print("  updateOwnerPackageName=");
            pw.println(ps.getInstallSource().mUpdateOwnerPackageName);
        }
        if (ps.getInstallSource().mInstallerAttributionTag != null) {
            pw.print(prefix);
            pw.print("  installerAttributionTag=");
            pw.println(ps.getInstallSource().mInstallerAttributionTag);
        }
        pw.print(prefix);
        pw.print("  packageSource=");
        pw.println(ps.getInstallSource().mPackageSource);
        if (ps.isIncremental()) {
            pw.print(prefix);
            pw.println("  loadingProgress=" + ((int) (ps.getLoadingProgress() * 100.0f)) + "%");
            date.setTime(ps.getLoadingCompletedTime());
            pw.print(prefix);
            pw.println("  loadingCompletedTime=" + sdf.format(date));
        }
        pw.print(prefix);
        pw.print("  appMetadataFilePath=");
        pw.println(ps.getAppMetadataFilePath());
        pw.print(prefix);
        pw.print("  appMetadataSource=");
        pw.println(ps.getAppMetadataSource());
        if (ps.getVolumeUuid() != null) {
            pw.print(prefix);
            pw.print("  volumeUuid=");
            pw.println(ps.getVolumeUuid());
        }
        pw.print(prefix);
        pw.print("  signatures=");
        pw.println(ps.getSignatures());
        pw.print(prefix);
        pw.print("  installPermissionsFixed=");
        pw.print(ps.isInstallPermissionsFixed());
        pw.println();
        pw.print(prefix);
        pw.print("  pkgFlags=");
        printFlags(pw, ps.getFlags(), FLAG_DUMP_SPEC);
        pw.println();
        pw.print(prefix);
        pw.print("  privatePkgFlags=");
        printFlags(pw, ps.getPrivateFlags(), PRIVATE_FLAG_DUMP_SPEC);
        pw.println();
        if (ps.isPendingRestore()) {
            pw.print(prefix);
            pw.println("  pendingRestore=true");
        }
        pw.print(prefix);
        pw.print("  apexModuleName=");
        pw.println(ps.getApexModuleName());
        if (pkg != null && pkg.getOverlayTarget() != null) {
            pw.print(prefix);
            pw.print("  overlayTarget=");
            pw.println(pkg.getOverlayTarget());
            pw.print(prefix);
            pw.print("  overlayCategory=");
            pw.println(pkg.getOverlayCategory());
        }
        if (pkg != null && !pkg.getPermissions().isEmpty()) {
            java.util.List<com.android.internal.pm.pkg.component.ParsedPermission> perms = pkg.getPermissions();
            pw.print(prefix);
            pw.println("  declared permissions:");
            for (int i10 = 0; i10 < perms.size(); i10++) {
                com.android.internal.pm.pkg.component.ParsedPermission perm = perms.get(i10);
                if (permissionNames == null || permissionNames.contains(perm.getName())) {
                    pw.print(prefix);
                    pw.print("    ");
                    pw.print(perm.getName());
                    pw.print(": prot=");
                    pw.print(android.content.pm.PermissionInfo.protectionToString(perm.getProtectionLevel()));
                    if ((perm.getFlags() & 1) != 0) {
                        pw.print(", COSTS_MONEY");
                    }
                    if ((perm.getFlags() & 2) != 0) {
                        pw.print(", HIDDEN");
                    }
                    if ((perm.getFlags() & 1073741824) != 0) {
                        pw.print(", INSTALLED");
                    }
                    pw.println();
                }
            }
        }
        if ((permissionNames != null || dumpAll) && pkg != null && pkg.getRequestedPermissions() != null && pkg.getRequestedPermissions().size() > 0) {
            java.util.Set<java.lang.String> perms2 = pkg.getRequestedPermissions();
            pw.print(prefix);
            pw.println("  requested permissions:");
            for (java.lang.String perm2 : perms2) {
                if (permissionNames == null || permissionNames.contains(perm2)) {
                    pw.print(prefix);
                    pw.print("    ");
                    pw.println(perm2);
                }
            }
        }
        if (!ps.hasSharedUser() || permissionNames != null || dumpAll) {
            dumpInstallPermissionsLPr(pw, prefix + "  ", permissionNames, permissionsState, users);
        }
        if (dumpAllComponents) {
            settings.dumpComponents(pw, prefix + "  ", ps);
        }
        for (android.content.pm.UserInfo user2 : users) {
            com.android.server.pm.pkg.PackageUserStateInternal userState2 = ps.getUserStateOrDefault(user2.id);
            pw.print(prefix);
            pw.print("  User ");
            pw.print(user2.id);
            pw.print(": ");
            pw.print("ceDataInode=");
            pw.print(userState2.getCeDataInode());
            pw.print(" deDataInode=");
            pw.print(userState2.getDeDataInode());
            pw.print(" installed=");
            pw.print(userState2.isInstalled());
            pw.print(" hidden=");
            pw.print(userState2.isHidden());
            pw.print(" suspended=");
            pw.print(userState2.isSuspended());
            pw.print(" distractionFlags=");
            pw.print(userState2.getDistractionFlags());
            pw.print(" stopped=");
            pw.print(userState2.isStopped());
            pw.print(" notLaunched=");
            pw.print(userState2.isNotLaunched());
            pw.print(" enabled=");
            pw.print(userState2.getEnabledState());
            settings.mSettingsExt.onPrintPackageAttrInDumpPackageLPr(pw, ps, user2);
            pw.print(" instant=");
            pw.print(userState2.isInstantApp());
            pw.print(" virtual=");
            pw.print(userState2.isVirtualPreload());
            pw.print(" quarantined=");
            pw.print(userState2.isQuarantined());
            pw.println();
            pw.print("      installReason=");
            pw.println(userState2.getInstallReason());
            java.io.File dataDir = com.android.server.pm.parsing.PackageInfoUtils.getDataDir(ps, user2.id);
            pw.print("      dataDir=");
            pw.println(dataDir == null ? "null" : dataDir.getAbsolutePath());
            com.android.server.pm.pkg.PackageUserStateInternal pus = ps.readUserState(user2.id);
            pw.print("      firstInstallTime=");
            date.setTime(pus.getFirstInstallTimeMillis());
            pw.println(sdf.format(date));
            if (pus.getArchiveState() != null) {
                com.android.server.pm.pkg.ArchiveState archiveState = pus.getArchiveState();
                pw.print("      archiveTime=");
                date.setTime(archiveState.getArchiveTimeMillis());
                pw.println(sdf.format(date));
                pw.print("      unarchiveInstallerTitle=");
                pw.println(archiveState.getInstallerTitle());
                for (com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo activity : archiveState.getActivityInfos()) {
                    pw.print("        archiveActivityInfo=");
                    pw.println(activity.toString());
                }
            }
            pw.print("      uninstallReason=");
            pw.println(userState2.getUninstallReason());
            if (userState2.isSuspended()) {
                pw.print(prefix);
                pw.println("  Suspend params:");
                for (int i11 = 0; i11 < userState2.getSuspendParams().size(); i11++) {
                    pw.print(prefix);
                    pw.print("    suspendingPackage=");
                    pw.print(userState2.getSuspendParams().keyAt(i11));
                    com.android.server.pm.pkg.SuspendParams params = userState2.getSuspendParams().valueAt(i11);
                    if (params != null) {
                        pw.print(" dialogInfo=");
                        pw.print(params.getDialogInfo());
                        pw.print(" quarantined=");
                        pw.println(params.isQuarantined());
                    }
                    pw.println();
                }
            }
            android.content.pm.overlay.OverlayPaths overlayPaths = userState2.getOverlayPaths();
            if (overlayPaths != null) {
                if (!overlayPaths.getOverlayPaths().isEmpty()) {
                    pw.print(prefix);
                    pw.println("    overlay paths:");
                    for (java.lang.String path : overlayPaths.getOverlayPaths()) {
                        pw.print(prefix);
                        pw.print("      ");
                        pw.println(path);
                    }
                }
                if (!overlayPaths.getResourceDirs().isEmpty()) {
                    pw.print(prefix);
                    pw.println("    legacy overlay paths:");
                    for (java.lang.String path2 : overlayPaths.getResourceDirs()) {
                        pw.print(prefix);
                        pw.print("      ");
                        pw.println(path2);
                    }
                }
            }
            java.util.Map<java.lang.String, android.content.pm.overlay.OverlayPaths> sharedLibraryOverlayPaths = userState2.getSharedLibraryOverlayPaths();
            if (sharedLibraryOverlayPaths != null) {
                java.util.Iterator<java.util.Map.Entry<java.lang.String, android.content.pm.overlay.OverlayPaths>> it4 = sharedLibraryOverlayPaths.entrySet().iterator();
                while (it4.hasNext()) {
                    java.util.Map.Entry<java.lang.String, android.content.pm.overlay.OverlayPaths> libOverlayPaths = it4.next();
                    android.content.pm.overlay.OverlayPaths paths = libOverlayPaths.getValue();
                    if (paths != null) {
                        if (paths.getOverlayPaths().isEmpty()) {
                            it = it4;
                        } else {
                            pw.print(prefix);
                            pw.println("    ");
                            pw.print(libOverlayPaths.getKey());
                            pw.println(" overlay paths:");
                            for (java.lang.String path3 : paths.getOverlayPaths()) {
                                pw.print(prefix);
                                pw.print("        ");
                                pw.println(path3);
                                it4 = it4;
                            }
                            it = it4;
                        }
                        if (!paths.getResourceDirs().isEmpty()) {
                            pw.print(prefix);
                            pw.println("      ");
                            pw.print(libOverlayPaths.getKey());
                            pw.println(" legacy overlay paths:");
                            for (java.lang.String path4 : paths.getResourceDirs()) {
                                pw.print(prefix);
                                pw.print("      ");
                                pw.println(path4);
                            }
                        }
                        it4 = it;
                    }
                }
            }
            java.lang.String lastDisabledAppCaller2 = userState2.getLastDisableAppCaller();
            if (lastDisabledAppCaller2 != null) {
                pw.print(prefix);
                pw.print("    lastDisabledCaller: ");
                pw.println(lastDisabledAppCaller2);
            }
            if (ps.hasSharedUser()) {
                str = "      ";
            } else {
                settings.dumpGidsLPr(pw, prefix + "    ", settings.mPermissionDataProvider.getGidsForUid(android.os.UserHandle.getUid(user2.id, ps.getAppId())));
                str = "      ";
                dumpRuntimePermissionsLPr(pw, prefix + "    ", permissionNames, permissionsState.getPermissionStates(user2.id), dumpAll);
            }
            java.lang.String harmfulAppWarning = userState2.getHarmfulAppWarning();
            if (harmfulAppWarning != null) {
                pw.print(prefix);
                pw.print("      harmfulAppWarning: ");
                pw.println(harmfulAppWarning);
            }
            if (permissionNames == null) {
                com.android.server.utils.WatchedArraySet<java.lang.String> cmp = userState2.getDisabledComponentsNoCopy();
                if (cmp != null && cmp.size() > 0) {
                    pw.print(prefix);
                    pw.println("    disabledComponents:");
                    for (int i12 = 0; i12 < cmp.size(); i12++) {
                        pw.print(prefix);
                        pw.print(str);
                        pw.println(cmp.valueAt(i12));
                    }
                }
                com.android.server.utils.WatchedArraySet<java.lang.String> cmp2 = userState2.getEnabledComponentsNoCopy();
                if (cmp2 != null && cmp2.size() > 0) {
                    pw.print(prefix);
                    pw.println("    enabledComponents:");
                    for (int i13 = 0; i13 < cmp2.size(); i13++) {
                        pw.print(prefix);
                        pw.print(str);
                        pw.println(cmp2.valueAt(i13));
                    }
                }
            }
            settings = this;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:91:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01c5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void dumpPackagesLPr(java.io.PrintWriter r25, java.lang.String r26, android.util.ArraySet<java.lang.String> r27, com.android.server.pm.DumpState r28, boolean r29) {
        /*
            Method dump skipped, instruction units count: 492
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.dumpPackagesLPr(java.io.PrintWriter, java.lang.String, android.util.ArraySet, com.android.server.pm.DumpState, boolean):void");
    }

    void dumpPackagesProto(android.util.proto.ProtoOutputStream proto) {
        java.util.List<android.content.pm.UserInfo> users = getAllUsers(com.android.server.pm.UserManagerService.getInstance());
        int count = this.mPackages.size();
        for (int i = 0; i < count; i++) {
            com.android.server.pm.PackageSetting ps = this.mPackages.valueAt(i);
            ps.dumpDebug(proto, 2246267895813L, users, this.mPermissionDataProvider);
        }
    }

    void dumpPermissions(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState) {
        com.android.server.pm.permission.LegacyPermissionSettings.dumpPermissions(pw, packageName, permissionNames, this.mPermissionDataProvider.getLegacyPermissions(), this.mPermissionDataProvider.getAllAppOpPermissionPackages(), true, dumpState);
    }

    void dumpSharedUsersLPr(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, com.android.server.pm.DumpState dumpState, boolean checkin) {
        boolean printedSomething;
        android.util.ArraySet<java.lang.String> arraySet = permissionNames;
        boolean printedSomething2 = false;
        for (com.android.server.pm.SharedUserSetting su : this.mSharedUsers.values()) {
            if (packageName == null || su == dumpState.getSharedUser()) {
                com.android.server.pm.permission.LegacyPermissionState permissionsState = this.mPermissionDataProvider.getLegacyPermissionState(su.mAppId);
                if (arraySet == null || permissionsState.hasPermissionState(arraySet)) {
                    if (!checkin) {
                        if (printedSomething2) {
                            printedSomething = printedSomething2;
                        } else {
                            if (dumpState.onTitlePrinted()) {
                                pw.println();
                            }
                            pw.println("Shared users:");
                            printedSomething = true;
                        }
                        pw.print("  SharedUser [");
                        pw.print(su.name);
                        pw.print("] (");
                        pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(su)));
                        pw.println("):");
                        pw.print("    ");
                        pw.print("appId=");
                        pw.println(su.mAppId);
                        pw.print("    ");
                        pw.println("Packages");
                        android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = su.getPackageStates();
                        int numPackages = packageStates.size();
                        for (int i = 0; i < numPackages; i++) {
                            com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(i);
                            if (ps != null) {
                                pw.print("      ");
                                pw.println(ps);
                            } else {
                                pw.print("      ");
                                pw.println("NULL?!");
                            }
                        }
                        if (dumpState.isOptionEnabled(4)) {
                            printedSomething2 = printedSomething;
                        } else {
                            java.util.List<android.content.pm.UserInfo> users = getAllUsers(com.android.server.pm.UserManagerService.getInstance());
                            dumpInstallPermissionsLPr(pw, "    ", permissionNames, permissionsState, users);
                            for (android.content.pm.UserInfo user : users) {
                                int userId = user.id;
                                int[] gids = this.mPermissionDataProvider.getGidsForUid(android.os.UserHandle.getUid(userId, su.mAppId));
                                java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> permissions = permissionsState.getPermissionStates(userId);
                                if (!com.android.internal.util.ArrayUtils.isEmpty(gids) || !permissions.isEmpty()) {
                                    pw.print("    ");
                                    pw.print("User ");
                                    pw.print(userId);
                                    pw.println(": ");
                                    dumpGidsLPr(pw, "      ", gids);
                                    dumpRuntimePermissionsLPr(pw, "      ", permissionNames, permissions, packageName != null);
                                }
                            }
                            printedSomething2 = printedSomething;
                        }
                    } else {
                        pw.print("suid,");
                        pw.print(su.mAppId);
                        pw.print(",");
                        pw.println(su.name);
                    }
                    arraySet = permissionNames;
                }
            }
        }
    }

    void dumpSharedUsersProto(android.util.proto.ProtoOutputStream proto) {
        int count = this.mSharedUsers.size();
        for (int i = 0; i < count; i++) {
            this.mSharedUsers.valueAt(i).dumpDebug(proto, 2246267895814L);
        }
    }

    void dumpReadMessages(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState) {
        pw.println("Settings parse messages:");
        pw.print(this.mReadMessages.toString());
    }

    private static void dumpSplitNames(java.io.PrintWriter pw, com.android.server.pm.pkg.AndroidPackage pkg) {
        if (pkg == null) {
            pw.print("unknown");
            return;
        }
        pw.print("[");
        pw.print("base");
        if (pkg.getBaseRevisionCode() != 0) {
            pw.print(":");
            pw.print(pkg.getBaseRevisionCode());
        }
        java.lang.String[] splitNames = pkg.getSplitNames();
        int[] splitRevisionCodes = pkg.getSplitRevisionCodes();
        for (int i = 0; i < splitNames.length; i++) {
            pw.print(", ");
            pw.print(splitNames[i]);
            if (splitRevisionCodes[i] != 0) {
                pw.print(":");
                pw.print(splitRevisionCodes[i]);
            }
        }
        pw.print("]");
    }

    void dumpGidsLPr(java.io.PrintWriter pw, java.lang.String prefix, int[] gids) {
        if (!com.android.internal.util.ArrayUtils.isEmpty(gids)) {
            pw.print(prefix);
            pw.print("gids=");
            pw.println(com.android.server.pm.PackageManagerServiceUtils.arrayToString(gids));
        }
    }

    void dumpRuntimePermissionsLPr(java.io.PrintWriter pw, java.lang.String prefix, android.util.ArraySet<java.lang.String> permissionNames, java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> permissionStates, boolean dumpAll) {
        boolean hasRuntimePermissions = false;
        java.util.Iterator<com.android.server.pm.permission.LegacyPermissionState.PermissionState> it = permissionStates.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().isRuntime()) {
                    hasRuntimePermissions = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (hasRuntimePermissions || dumpAll) {
            pw.print(prefix);
            pw.println("runtime permissions:");
            for (com.android.server.pm.permission.LegacyPermissionState.PermissionState permissionState : permissionStates) {
                if (permissionState.isRuntime() && (permissionNames == null || permissionNames.contains(permissionState.getName()))) {
                    pw.print(prefix);
                    pw.print("  ");
                    pw.print(permissionState.getName());
                    pw.print(": granted=");
                    pw.print(permissionState.isGranted());
                    pw.println(permissionFlagsToString(", flags=", permissionState.getFlags()));
                }
            }
        }
    }

    private static java.lang.String permissionFlagsToString(java.lang.String prefix, int flags) {
        java.lang.StringBuilder flagsString = null;
        while (flags != 0) {
            if (flagsString == null) {
                flagsString = new java.lang.StringBuilder();
                flagsString.append(prefix);
                flagsString.append("[ ");
            }
            int flag = 1 << java.lang.Integer.numberOfTrailingZeros(flags);
            flags &= ~flag;
            flagsString.append(android.content.pm.PackageManager.permissionFlagToString(flag));
            if (flags != 0) {
                flagsString.append('|');
            }
        }
        if (flagsString != null) {
            flagsString.append(']');
            return flagsString.toString();
        }
        return "";
    }

    void dumpInstallPermissionsLPr(java.io.PrintWriter pw, java.lang.String prefix, android.util.ArraySet<java.lang.String> filterPermissionNames, com.android.server.pm.permission.LegacyPermissionState permissionsState, java.util.List<android.content.pm.UserInfo> users) {
        com.android.server.pm.permission.LegacyPermissionState.PermissionState permissionState;
        android.util.ArraySet<java.lang.String> dumpPermissionNames = new android.util.ArraySet<>();
        for (android.content.pm.UserInfo user : users) {
            java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> permissionStates = permissionsState.getPermissionStates(user.id);
            for (com.android.server.pm.permission.LegacyPermissionState.PermissionState permissionState2 : permissionStates) {
                if (!permissionState2.isRuntime()) {
                    java.lang.String permissionName = permissionState2.getName();
                    if (filterPermissionNames == null || filterPermissionNames.contains(permissionName)) {
                        dumpPermissionNames.add(permissionName);
                    }
                }
            }
        }
        boolean printedSomething = false;
        for (java.lang.String permissionName2 : dumpPermissionNames) {
            com.android.server.pm.permission.LegacyPermissionState.PermissionState systemPermissionState = permissionsState.getPermissionState(permissionName2, 0);
            for (android.content.pm.UserInfo user2 : users) {
                int userId = user2.id;
                if (userId == 0) {
                    permissionState = systemPermissionState;
                } else {
                    permissionState = permissionsState.getPermissionState(permissionName2, userId);
                    if (java.util.Objects.equals(permissionState, systemPermissionState)) {
                    }
                }
                if (!printedSomething) {
                    pw.print(prefix);
                    pw.println("install permissions:");
                    printedSomething = true;
                }
                pw.print(prefix);
                pw.print("  ");
                pw.print(permissionName2);
                pw.print(": granted=");
                pw.print(permissionState != null && permissionState.isGranted());
                pw.print(permissionFlagsToString(", flags=", permissionState != null ? permissionState.getFlags() : 0));
                if (userId != 0) {
                    pw.print(", userId=");
                    pw.println(userId);
                } else {
                    pw.println();
                }
            }
        }
    }

    void dumpComponents(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.pm.PackageSetting ps) {
        dumpComponents(pw, prefix, "activities:", ps.getPkg().getActivities());
        dumpComponents(pw, prefix, "services:", ps.getPkg().getServices());
        dumpComponents(pw, prefix, "receivers:", ps.getPkg().getReceivers());
        dumpComponents(pw, prefix, "providers:", ps.getPkg().getProviders());
        dumpComponents(pw, prefix, "instrumentations:", ps.getPkg().getInstrumentations());
    }

    void dumpComponents(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String label, java.util.List<? extends com.android.internal.pm.pkg.component.ParsedComponent> list) {
        int size = com.android.internal.util.CollectionUtils.size(list);
        if (size == 0) {
            return;
        }
        pw.print(prefix);
        pw.println(label);
        for (int i = 0; i < size; i++) {
            com.android.internal.pm.pkg.component.ParsedComponent component = list.get(i);
            pw.print(prefix);
            pw.print("  ");
            pw.println(component.getComponentName().flattenToShortString());
        }
    }

    public void writePermissionStateForUserLPr(int userId, boolean sync) {
        if (sync) {
            this.mRuntimePermissionsPersistence.writeStateForUser(userId, this.mPermissionDataProvider, this.mPackages, this.mSharedUsers, null, this.mLock, true);
        } else {
            this.mRuntimePermissionsPersistence.writeStateForUserAsync(userId);
        }
    }

    private static class KeySetToValueMap<K, V> implements java.util.Map<K, V> {
        private final java.util.Set<K> mKeySet;
        private final V mValue;

        KeySetToValueMap(java.util.Set<K> keySet, V value) {
            this.mKeySet = keySet;
            this.mValue = value;
        }

        @Override // java.util.Map
        public int size() {
            return this.mKeySet.size();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.mKeySet.isEmpty();
        }

        @Override // java.util.Map
        public boolean containsKey(java.lang.Object key) {
            return this.mKeySet.contains(key);
        }

        @Override // java.util.Map
        public boolean containsValue(java.lang.Object value) {
            return this.mValue == value;
        }

        @Override // java.util.Map
        public V get(java.lang.Object key) {
            return this.mValue;
        }

        @Override // java.util.Map
        public V put(K key, V value) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override // java.util.Map
        public V remove(java.lang.Object key) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override // java.util.Map
        public void putAll(java.util.Map<? extends K, ? extends V> m) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override // java.util.Map
        public void clear() {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override // java.util.Map
        public java.util.Set<K> keySet() {
            return this.mKeySet;
        }

        @Override // java.util.Map
        public java.util.Collection<V> values() {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override // java.util.Map
        public java.util.Set<java.util.Map.Entry<K, V>> entrySet() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class RuntimePermissionPersistence {
        private static final int INITIAL_VERSION = 0;
        private static final long MAX_WRITE_PERMISSIONS_DELAY_MILLIS = 2000;
        private static final int UPGRADE_VERSION = -1;
        private static final double WRITE_PERMISSIONS_DELAY_JITTER = 0.3d;
        private static final long WRITE_PERMISSIONS_DELAY_MILLIS = 1000;
        private static final java.util.Random sRandom = new java.util.Random();
        private java.lang.String mExtendedFingerprint;
        private final java.util.function.Consumer<java.lang.Integer> mInvokeWriteUserStateAsyncCallback;
        private final com.android.permission.persistence.RuntimePermissionsPersistence mPersistence;
        private final java.lang.Object mPersistenceLock = new java.lang.Object();
        private final android.os.Handler mAsyncHandler = new com.android.server.pm.Settings.RuntimePermissionPersistence.MyHandler();
        private final android.os.Handler mPersistenceHandler = new com.android.server.pm.Settings.RuntimePermissionPersistence.PersistenceHandler();
        private final java.lang.Object mLock = new java.lang.Object();
        private final android.util.SparseBooleanArray mWriteScheduled = new android.util.SparseBooleanArray();
        private final android.util.SparseLongArray mLastNotWrittenMutationTimesMillis = new android.util.SparseLongArray();
        private final java.util.concurrent.atomic.AtomicBoolean mIsLegacyPermissionStateStale = new java.util.concurrent.atomic.AtomicBoolean(false);
        private final android.util.SparseIntArray mVersions = new android.util.SparseIntArray();
        private final android.util.SparseArray<java.lang.String> mFingerprints = new android.util.SparseArray<>();
        private final android.util.SparseBooleanArray mPermissionUpgradeNeeded = new android.util.SparseBooleanArray();
        private final android.util.SparseArray<com.android.permission.persistence.RuntimePermissionsState> mPendingStatesToWrite = new android.util.SparseArray<>();

        public RuntimePermissionPersistence(com.android.permission.persistence.RuntimePermissionsPersistence persistence, java.util.function.Consumer<java.lang.Integer> invokeWriteUserStateAsyncCallback) {
            this.mPersistence = persistence;
            this.mInvokeWriteUserStateAsyncCallback = invokeWriteUserStateAsyncCallback;
        }

        int getVersion(int userId) {
            int i;
            synchronized (this.mLock) {
                if (userId == 999) {
                    int multiVersion = this.mVersions.get(userId, 0);
                    int version = this.mVersions.get(0, 0);
                    if (multiVersion != version) {
                        android.util.Slog.w(com.android.server.pm.Settings.TAG, "change version of multi user from: " + multiVersion + " to: " + version);
                        this.mVersions.put(userId, version);
                        writeStateForUserAsync(userId);
                    }
                    i = this.mVersions.get(userId, 0);
                } else {
                    i = this.mVersions.get(userId, 0);
                }
            }
            return i;
        }

        void setVersion(int version, int userId) {
            synchronized (this.mLock) {
                this.mVersions.put(userId, version);
                writeStateForUserAsync(userId);
            }
        }

        public boolean isPermissionUpgradeNeeded(int userId) {
            boolean z;
            synchronized (this.mLock) {
                z = this.mPermissionUpgradeNeeded.get(userId, true);
            }
            return z;
        }

        public void updateRuntimePermissionsFingerprint(int userId) {
            synchronized (this.mLock) {
                if (this.mExtendedFingerprint == null) {
                    throw new java.lang.RuntimeException("The version of the permission controller hasn't been set before trying to update the fingerprint.");
                }
                this.mFingerprints.put(userId, this.mExtendedFingerprint);
                this.mPermissionUpgradeNeeded.put(userId, false);
                writeStateForUserAsync(userId);
            }
        }

        public void setPermissionControllerVersion(long version) {
            synchronized (this.mLock) {
                int numUser = this.mFingerprints.size();
                this.mExtendedFingerprint = getExtendedFingerprint(version);
                for (int i = 0; i < numUser; i++) {
                    int userId = this.mFingerprints.keyAt(i);
                    java.lang.String fingerprint = this.mFingerprints.valueAt(i);
                    this.mPermissionUpgradeNeeded.put(userId, !android.text.TextUtils.equals(this.mExtendedFingerprint, fingerprint));
                }
            }
        }

        private java.lang.String getExtendedFingerprint(long version) {
            return android.content.pm.PackagePartitions.FINGERPRINT + "?pc_version=" + version;
        }

        private static long uniformRandom(double low, double high) {
            double mag = high - low;
            return (long) ((sRandom.nextDouble() * mag) + low);
        }

        private static long nextWritePermissionDelayMillis() {
            return uniformRandom(-300.0d, 300.0d) + 1000;
        }

        public void writeStateForUserAsync(int userId) {
            this.mIsLegacyPermissionStateStale.set(true);
            synchronized (this.mLock) {
                long currentTimeMillis = android.os.SystemClock.uptimeMillis();
                long writePermissionDelayMillis = nextWritePermissionDelayMillis();
                if (this.mWriteScheduled.get(userId)) {
                    this.mAsyncHandler.removeMessages(userId);
                    long lastNotWrittenMutationTimeMillis = this.mLastNotWrittenMutationTimesMillis.get(userId);
                    long timeSinceLastNotWrittenMutationMillis = currentTimeMillis - lastNotWrittenMutationTimeMillis;
                    if (timeSinceLastNotWrittenMutationMillis >= MAX_WRITE_PERMISSIONS_DELAY_MILLIS) {
                        this.mAsyncHandler.obtainMessage(userId).sendToTarget();
                        return;
                    }
                    long maxDelayMillis = java.lang.Math.max((MAX_WRITE_PERMISSIONS_DELAY_MILLIS + lastNotWrittenMutationTimeMillis) - currentTimeMillis, 0L);
                    long writeDelayMillis = java.lang.Math.min(writePermissionDelayMillis, maxDelayMillis);
                    android.os.Message message = this.mAsyncHandler.obtainMessage(userId);
                    this.mAsyncHandler.sendMessageDelayed(message, writeDelayMillis);
                } else {
                    this.mLastNotWrittenMutationTimesMillis.put(userId, currentTimeMillis);
                    android.os.Message message2 = this.mAsyncHandler.obtainMessage(userId);
                    this.mAsyncHandler.sendMessageDelayed(message2, writePermissionDelayMillis);
                    this.mWriteScheduled.put(userId, true);
                }
            }
        }

        public void writeStateForUser(final int userId, final com.android.server.pm.permission.LegacyPermissionDataProvider legacyPermissionDataProvider, final com.android.server.utils.WatchedArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates, final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> sharedUsers, final android.os.Handler pmHandler, final com.android.server.pm.PackageManagerTracedLock pmLock, final boolean sync) {
            synchronized (this.mLock) {
                this.mAsyncHandler.removeMessages(userId);
                this.mWriteScheduled.delete(userId);
            }
            java.lang.Runnable writer = new java.lang.Runnable() { // from class: com.android.server.pm.Settings$RuntimePermissionPersistence$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() throws java.lang.Throwable {
                    this.f$0.lambda$writeStateForUser$0(pmHandler, pmLock, sync, legacyPermissionDataProvider, userId, packageStates, sharedUsers);
                }
            };
            if (pmHandler != null) {
                pmHandler.post(writer);
            } else {
                writer.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0034 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public /* synthetic */ void lambda$writeStateForUser$0(android.os.Handler r15, com.android.server.pm.PackageManagerTracedLock r16, boolean r17, com.android.server.pm.permission.LegacyPermissionDataProvider r18, int r19, com.android.server.utils.WatchedArrayMap r20, com.android.server.utils.WatchedArrayMap r21) throws java.lang.Throwable {
            /*
                r14 = this;
                r1 = r14
                r2 = r19
                r3 = 262144(0x40000, double:1.295163E-318)
                if (r15 == 0) goto Ld
                java.lang.String r0 = "PackageManagerBg writeStateForUser"
                android.os.Trace.traceBegin(r3, r0)
            Ld:
                java.util.concurrent.atomic.AtomicBoolean r0 = r1.mIsLegacyPermissionStateStale
                r5 = 0
                boolean r6 = r0.getAndSet(r5)
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection()
                monitor-enter(r16)
                if (r17 != 0) goto L1c
                if (r6 == 0) goto L1f
            L1c:
                r18.writeLegacyPermissionStateTEMP()     // Catch: java.lang.Throwable -> L65
            L1f:
                r7 = r20
                java.util.Map r0 = r14.getPackagePermissions(r2, r7)     // Catch: java.lang.Throwable -> L63
                r8 = r0
                r9 = r21
                java.util.Map r0 = r14.getShareUsersPermissions(r2, r9)     // Catch: java.lang.Throwable -> L6f
                r10 = r0
                monitor-exit(r16)     // Catch: java.lang.Throwable -> L6f
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection()
                java.lang.Object r11 = r1.mLock
                monitor-enter(r11)
                android.util.SparseIntArray r0 = r1.mVersions     // Catch: java.lang.Throwable -> L60
                int r0 = r0.get(r2, r5)     // Catch: java.lang.Throwable -> L60
                android.util.SparseArray<java.lang.String> r5 = r1.mFingerprints     // Catch: java.lang.Throwable -> L60
                java.lang.Object r5 = r5.get(r2)     // Catch: java.lang.Throwable -> L60
                java.lang.String r5 = (java.lang.String) r5     // Catch: java.lang.Throwable -> L60
                com.android.permission.persistence.RuntimePermissionsState r12 = new com.android.permission.persistence.RuntimePermissionsState     // Catch: java.lang.Throwable -> L60
                r12.<init>(r0, r5, r8, r10)     // Catch: java.lang.Throwable -> L60
                android.util.SparseArray<com.android.permission.persistence.RuntimePermissionsState> r13 = r1.mPendingStatesToWrite     // Catch: java.lang.Throwable -> L60
                r13.put(r2, r12)     // Catch: java.lang.Throwable -> L60
                monitor-exit(r11)     // Catch: java.lang.Throwable -> L60
                if (r15 == 0) goto L5c
                android.os.Handler r0 = r1.mPersistenceHandler
                android.os.Message r0 = r0.obtainMessage(r2)
                r0.sendToTarget()
                android.os.Trace.traceEnd(r3)
                goto L5f
            L5c:
                r14.writePendingStates()
            L5f:
                return
            L60:
                r0 = move-exception
                monitor-exit(r11)     // Catch: java.lang.Throwable -> L60
                throw r0
            L63:
                r0 = move-exception
                goto L68
            L65:
                r0 = move-exception
                r7 = r20
            L68:
                r9 = r21
            L6a:
                monitor-exit(r16)     // Catch: java.lang.Throwable -> L6f
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection()
                throw r0
            L6f:
                r0 = move-exception
                goto L6a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.RuntimePermissionPersistence.lambda$writeStateForUser$0(android.os.Handler, com.android.server.pm.PackageManagerTracedLock, boolean, com.android.server.pm.permission.LegacyPermissionDataProvider, int, com.android.server.utils.WatchedArrayMap, com.android.server.utils.WatchedArrayMap):void");
        }

        com.android.permission.persistence.RuntimePermissionsState getLegacyPermissionsState(int userId, com.android.server.utils.WatchedArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> sharedUsers) {
            int version;
            java.lang.String fingerprint;
            synchronized (this.mLock) {
                version = this.mVersions.get(userId, 0);
                fingerprint = this.mFingerprints.get(userId);
            }
            return new com.android.permission.persistence.RuntimePermissionsState(version, fingerprint, getPackagePermissions(userId, packageStates), getShareUsersPermissions(userId, sharedUsers));
        }

        private java.util.Map<java.lang.String, java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState>> getPackagePermissions(int userId, com.android.server.utils.WatchedArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates) {
            java.util.Map<java.lang.String, java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState>> packagePermissions = new android.util.ArrayMap<>();
            int packagesSize = packageStates.size();
            for (int i = 0; i < packagesSize; i++) {
                java.lang.String packageName = packageStates.keyAt(i);
                com.android.server.pm.pkg.PackageStateInternal packageState = packageStates.valueAt(i);
                if (!packageState.hasSharedUser()) {
                    java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> permissions = getPermissionsFromPermissionsState(packageState.getLegacyPermissionState(), userId);
                    if (!permissions.isEmpty() || packageState.isInstallPermissionsFixed()) {
                        packagePermissions.put(packageName, permissions);
                    }
                }
            }
            return packagePermissions;
        }

        private java.util.Map<java.lang.String, java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState>> getShareUsersPermissions(int userId, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> sharedUsers) {
            java.util.Map<java.lang.String, java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState>> sharedUserPermissions = new android.util.ArrayMap<>();
            int sharedUsersSize = sharedUsers.size();
            for (int i = 0; i < sharedUsersSize; i++) {
                java.lang.String sharedUserName = sharedUsers.keyAt(i);
                com.android.server.pm.SharedUserSetting sharedUserSetting = sharedUsers.valueAt(i);
                java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> permissions = getPermissionsFromPermissionsState(sharedUserSetting.getLegacyPermissionState(), userId);
                sharedUserPermissions.put(sharedUserName, permissions);
            }
            return sharedUserPermissions;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writePendingStates() {
            int userId;
            com.android.permission.persistence.RuntimePermissionsState runtimePermissions;
            while (true) {
                synchronized (this.mLock) {
                    if (this.mPendingStatesToWrite.size() != 0) {
                        userId = this.mPendingStatesToWrite.keyAt(0);
                        runtimePermissions = this.mPendingStatesToWrite.valueAt(0);
                        this.mPendingStatesToWrite.removeAt(0);
                    } else {
                        return;
                    }
                }
                synchronized (this.mPersistenceLock) {
                    this.mPersistence.writeForUser(runtimePermissions, android.os.UserHandle.of(userId));
                }
            }
        }

        private java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> getPermissionsFromPermissionsState(com.android.server.pm.permission.LegacyPermissionState permissionsState, int userId) {
            java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> permissionStates = permissionsState.getPermissionStates(userId);
            java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> permissions = new java.util.ArrayList<>();
            for (com.android.server.pm.permission.LegacyPermissionState.PermissionState permissionState : permissionStates) {
                com.android.permission.persistence.RuntimePermissionsState.PermissionState permission = new com.android.permission.persistence.RuntimePermissionsState.PermissionState(permissionState.getName(), permissionState.isGranted(), permissionState.getFlags());
                permissions.add(permission);
            }
            return permissions;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onUserRemoved(int userId) {
            synchronized (this.mLock) {
                this.mAsyncHandler.removeMessages(userId);
                this.mPermissionUpgradeNeeded.delete(userId);
                this.mVersions.delete(userId);
                this.mFingerprints.remove(userId);
            }
        }

        public void deleteUserRuntimePermissionsFile(int userId) {
            synchronized (this.mPersistenceLock) {
                this.mPersistence.deleteForUser(android.os.UserHandle.of(userId));
            }
        }

        public void readStateForUserSync(int userId, com.android.server.pm.Settings.VersionInfo internalVersion, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> packageSettings, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> sharedUsers, java.io.File userRuntimePermissionsFile) throws java.lang.Throwable {
            com.android.permission.persistence.RuntimePermissionsState runtimePermissions;
            java.util.Map<java.lang.String, java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState>> sharedUserPermissions;
            int version;
            java.lang.String fingerprint;
            com.android.server.pm.Settings.RuntimePermissionPersistence runtimePermissionPersistence = this;
            com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> watchedArrayMap = packageSettings;
            synchronized (runtimePermissionPersistence.mPersistenceLock) {
                try {
                    runtimePermissions = runtimePermissionPersistence.mPersistence.readForUser(android.os.UserHandle.of(userId));
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
            if (runtimePermissions == null) {
                runtimePermissionPersistence.readLegacyStateForUserSync(userId, userRuntimePermissionsFile, watchedArrayMap, sharedUsers);
                writeStateForUserAsync(userId);
                return;
            }
            synchronized (runtimePermissionPersistence.mLock) {
                try {
                    try {
                        int version2 = runtimePermissions.getVersion();
                        if (version2 == -1) {
                            version2 = -1;
                        }
                        runtimePermissionPersistence.mVersions.put(userId, version2);
                        java.lang.String fingerprint2 = runtimePermissions.getFingerprint();
                        runtimePermissionPersistence.mFingerprints.put(userId, fingerprint2);
                        boolean isUpgradeToR = internalVersion.sdkVersion < 30;
                        java.util.Map<java.lang.String, java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState>> packagePermissions = runtimePermissions.getPackagePermissions();
                        int packagesSize = packageSettings.size();
                        int i = 0;
                        while (i < packagesSize) {
                            java.lang.String packageName = watchedArrayMap.keyAt(i);
                            com.android.server.pm.PackageSetting packageSetting = watchedArrayMap.valueAt(i);
                            java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> permissions = packagePermissions.get(packageName);
                            if (permissions != null) {
                                version = version2;
                                runtimePermissionPersistence.readPermissionsState(permissions, packageSetting.getLegacyPermissionState(), userId);
                                packageSetting.setInstallPermissionsFixed(true);
                                fingerprint = fingerprint2;
                            } else {
                                version = version2;
                                if (packageSetting.hasSharedUser() || isUpgradeToR) {
                                    fingerprint = fingerprint2;
                                } else {
                                    fingerprint = fingerprint2;
                                    com.android.server.utils.Slogf.w(com.android.server.pm.Settings.TAG, "Missing permission state for package %s on user %d", packageName, java.lang.Integer.valueOf(userId));
                                    packageSetting.getLegacyPermissionState().setMissing(true, userId);
                                }
                            }
                            i++;
                            watchedArrayMap = packageSettings;
                            fingerprint2 = fingerprint;
                            version2 = version;
                        }
                        java.util.Map<java.lang.String, java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState>> sharedUserPermissions2 = runtimePermissions.getSharedUserPermissions();
                        int sharedUsersSize = sharedUsers.size();
                        int i2 = 0;
                        while (i2 < sharedUsersSize) {
                            java.lang.String sharedUserName = sharedUsers.keyAt(i2);
                            com.android.server.pm.SharedUserSetting sharedUserSetting = sharedUsers.valueAt(i2);
                            java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> permissions2 = sharedUserPermissions2.get(sharedUserName);
                            if (permissions2 != null) {
                                runtimePermissionPersistence.readPermissionsState(permissions2, sharedUserSetting.getLegacyPermissionState(), userId);
                                sharedUserPermissions = sharedUserPermissions2;
                            } else if (isUpgradeToR) {
                                sharedUserPermissions = sharedUserPermissions2;
                            } else {
                                sharedUserPermissions = sharedUserPermissions2;
                                android.util.Slog.w(com.android.server.pm.Settings.TAG, "Missing permission state for shared user: " + sharedUserName);
                                sharedUserSetting.getLegacyPermissionState().setMissing(true, userId);
                            }
                            i2++;
                            runtimePermissionPersistence = this;
                            sharedUserPermissions2 = sharedUserPermissions;
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        throw th;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            }
        }

        private void readPermissionsState(java.util.List<com.android.permission.persistence.RuntimePermissionsState.PermissionState> permissions, com.android.server.pm.permission.LegacyPermissionState permissionsState, int userId) {
            int permissionsSize = permissions.size();
            for (int i = 0; i < permissionsSize; i++) {
                com.android.permission.persistence.RuntimePermissionsState.PermissionState permission = permissions.get(i);
                java.lang.String name = permission.getName();
                boolean granted = permission.isGranted();
                int flags = permission.getFlags();
                permissionsState.putPermissionState(new com.android.server.pm.permission.LegacyPermissionState.PermissionState(name, true, granted, flags), userId);
            }
        }

        private void readLegacyStateForUserSync(int userId, java.io.File permissionsFile, com.android.server.utils.WatchedArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> sharedUsers) {
            synchronized (this.mLock) {
                if (permissionsFile.exists()) {
                    try {
                        java.io.FileInputStream in = new android.util.AtomicFile(permissionsFile).openRead();
                        try {
                            try {
                                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                                parseLegacyRuntimePermissions(parser, userId, packageStates, sharedUsers);
                            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                                throw new java.lang.IllegalStateException("Failed parsing permissions file: " + permissionsFile, e);
                            }
                        } finally {
                            libcore.io.IoUtils.closeQuietly(in);
                        }
                    } catch (java.io.FileNotFoundException e2) {
                        android.util.Slog.i("PackageManager", "No permissions state");
                    }
                }
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:17:0x002a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void parseLegacyRuntimePermissions(com.android.modules.utils.TypedXmlPullParser r9, int r10, com.android.server.utils.WatchedArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> r11, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.SharedUserSetting> r12) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                Method dump skipped, instruction units count: 240
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.Settings.RuntimePermissionPersistence.parseLegacyRuntimePermissions(com.android.modules.utils.TypedXmlPullParser, int, com.android.server.utils.WatchedArrayMap, com.android.server.utils.WatchedArrayMap):void");
        }

        private void parseLegacyPermissionsLPr(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.pm.permission.LegacyPermissionState permissionsState, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            byte b;
            synchronized (this.mLock) {
                int outerDepth = parser.getDepth();
                while (true) {
                    int type = parser.next();
                    if (type != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
                        if (type != 3 && type != 4) {
                            java.lang.String name = parser.getName();
                            switch (name.hashCode()) {
                                case 3242771:
                                    if (name.equals(com.android.server.pm.Settings.TAG_ITEM)) {
                                        b = 0;
                                        break;
                                    }
                                default:
                                    b = -1;
                                    break;
                            }
                            switch (b) {
                                case 0:
                                    java.lang.String name2 = parser.getAttributeValue((java.lang.String) null, "name");
                                    boolean granted = parser.getAttributeBoolean((java.lang.String) null, com.android.server.pm.Settings.ATTR_GRANTED, true);
                                    int flags = parser.getAttributeIntHex((java.lang.String) null, com.android.server.pm.Settings.ATTR_FLAGS, 0);
                                    permissionsState.putPermissionState(new com.android.server.pm.permission.LegacyPermissionState.PermissionState(name2, true, granted, flags), userId);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        private final class MyHandler extends android.os.Handler {
            public MyHandler() {
                super(com.android.internal.os.BackgroundThread.getHandler().getLooper());
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message message) {
                int userId = message.what;
                java.lang.Runnable callback = (java.lang.Runnable) message.obj;
                com.android.server.pm.Settings.RuntimePermissionPersistence.this.mInvokeWriteUserStateAsyncCallback.accept(java.lang.Integer.valueOf(userId));
                if (callback != null) {
                    callback.run();
                }
            }
        }

        private final class PersistenceHandler extends android.os.Handler {
            PersistenceHandler() {
                super(com.android.internal.os.BackgroundThread.getHandler().getLooper());
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message message) {
                com.android.server.pm.Settings.RuntimePermissionPersistence.this.writePendingStates();
            }
        }
    }

    com.android.server.pm.PersistentPreferredIntentResolver getPersistentPreferredActivities(int userId) {
        return this.mPersistentPreferredActivities.get(userId);
    }

    com.android.server.pm.PreferredIntentResolver getPreferredActivities(int userId) {
        return this.mPreferredActivities.get(userId);
    }

    com.android.server.pm.CrossProfileIntentResolver getCrossProfileIntentResolver(int userId) {
        return this.mCrossProfileIntentResolvers.get(userId);
    }

    void clearPackagePreferredActivities(java.lang.String packageName, android.util.SparseBooleanArray outUserChanged, int userId) {
        boolean changed = false;
        java.util.ArrayList<com.android.server.pm.PreferredActivity> removed = null;
        for (int i = 0; i < this.mPreferredActivities.size(); i++) {
            int thisUserId = this.mPreferredActivities.keyAt(i);
            com.android.server.pm.PreferredIntentResolver pir = this.mPreferredActivities.valueAt(i);
            if (userId == -1 || userId == thisUserId) {
                java.util.Iterator<F> itFilterIterator = pir.filterIterator();
                while (itFilterIterator.hasNext()) {
                    com.android.server.pm.PreferredActivity pa = (com.android.server.pm.PreferredActivity) itFilterIterator.next();
                    if (packageName == null || (pa != null && pa.mPref != null && pa.mPref.mComponent.getPackageName().equals(packageName) && pa.mPref.mAlways)) {
                        if (removed == null) {
                            removed = new java.util.ArrayList<>();
                        }
                        removed.add(pa);
                    }
                }
                if (removed != null) {
                    for (int j = 0; j < removed.size(); j++) {
                        pir.removeFilter(removed.get(j));
                    }
                    outUserChanged.put(thisUserId, true);
                    changed = true;
                }
            }
        }
        if (changed) {
            onChanged();
        }
    }

    boolean clearPackagePersistentPreferredActivities(java.lang.String packageName, int userId) {
        java.util.ArrayList<com.android.server.pm.PersistentPreferredActivity> removed = null;
        boolean changed = false;
        for (int i = 0; i < this.mPersistentPreferredActivities.size(); i++) {
            int thisUserId = this.mPersistentPreferredActivities.keyAt(i);
            com.android.server.pm.PersistentPreferredIntentResolver ppir = this.mPersistentPreferredActivities.valueAt(i);
            if (userId == thisUserId) {
                java.util.Iterator<F> itFilterIterator = ppir.filterIterator();
                while (itFilterIterator.hasNext()) {
                    com.android.server.pm.PersistentPreferredActivity ppa = (com.android.server.pm.PersistentPreferredActivity) itFilterIterator.next();
                    if (ppa.mComponent.getPackageName().equals(packageName)) {
                        if (removed == null) {
                            removed = new java.util.ArrayList<>();
                        }
                        removed.add(ppa);
                    }
                }
                if (removed != null) {
                    for (int j = 0; j < removed.size(); j++) {
                        ppir.removeFilter(removed.get(j));
                    }
                    changed = true;
                }
            }
        }
        if (changed) {
            onChanged();
        }
        return changed;
    }

    boolean clearPersistentPreferredActivity(android.content.IntentFilter filter, int userId) {
        java.util.ArrayList<com.android.server.pm.PersistentPreferredActivity> removed = null;
        com.android.server.pm.PersistentPreferredIntentResolver ppir = this.mPersistentPreferredActivities.get(userId);
        java.util.Iterator<F> itFilterIterator = ppir.filterIterator();
        boolean changed = false;
        while (itFilterIterator.hasNext()) {
            com.android.server.pm.PersistentPreferredActivity ppa = (com.android.server.pm.PersistentPreferredActivity) itFilterIterator.next();
            if (android.content.IntentFilter.filterEquals(ppa.getIntentFilter(), filter)) {
                if (removed == null) {
                    removed = new java.util.ArrayList<>();
                }
                removed.add(ppa);
            }
        }
        if (removed != null) {
            for (int i = 0; i < removed.size(); i++) {
                ppir.removeFilter(removed.get(i));
            }
            changed = true;
        }
        if (changed) {
            onChanged();
        }
        return changed;
    }

    java.util.ArrayList<java.lang.Integer> systemReady(com.android.server.pm.resolution.ComponentResolver resolver) {
        java.util.ArrayList<java.lang.Integer> changed = new java.util.ArrayList<>();
        java.util.ArrayList<com.android.server.pm.PreferredActivity> removed = new java.util.ArrayList<>();
        for (int i = 0; i < this.mPreferredActivities.size(); i++) {
            com.android.server.pm.PreferredIntentResolver pir = this.mPreferredActivities.valueAt(i);
            removed.clear();
            for (F pa : pir.filterSet()) {
                if (!resolver.isActivityDefined(pa.mPref.mComponent)) {
                    removed.add(pa);
                }
            }
            if (removed.size() > 0) {
                for (int r = 0; r < removed.size(); r++) {
                    com.android.server.pm.PreferredActivity pa2 = removed.get(r);
                    android.util.Slog.w(TAG, "Removing dangling preferred activity: " + pa2.mPref.mComponent);
                    pir.removeFilter(pa2);
                }
                changed.add(java.lang.Integer.valueOf(this.mPreferredActivities.keyAt(i)));
            }
        }
        onChanged();
        return changed;
    }

    void dumpPreferred(java.io.PrintWriter pw, com.android.server.pm.DumpState dumpState, java.lang.String packageName) {
        java.lang.String str;
        for (int i = 0; i < this.mPreferredActivities.size(); i++) {
            com.android.server.pm.PreferredIntentResolver pir = this.mPreferredActivities.valueAt(i);
            int user = this.mPreferredActivities.keyAt(i);
            if (dumpState.getTitlePrinted()) {
                str = "\nPreferred Activities User " + user + ":";
            } else {
                str = "Preferred Activities User " + user + ":";
            }
            if (pir.dump(pw, str, "  ", packageName, true, false)) {
                dumpState.setTitlePrinted(true);
            }
        }
    }

    boolean isInstallerPackage(java.lang.String packageName) {
        return this.mInstallerPackages.contains(packageName);
    }
}
