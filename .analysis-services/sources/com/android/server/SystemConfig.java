package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class SystemConfig {
    private static final int ALLOW_ALL = -1;
    private static final int ALLOW_APP_CONFIGS = 8;
    private static final int ALLOW_ASSOCIATIONS = 128;
    private static final int ALLOW_FEATURES = 1;
    private static final int ALLOW_HIDDENAPI_WHITELISTING = 64;
    private static final int ALLOW_IMPLICIT_BROADCASTS = 512;
    private static final int ALLOW_LIBS = 2;
    private static final int ALLOW_OEM_PERMISSIONS = 32;
    private static final int ALLOW_OVERRIDE_APP_RESTRICTIONS = 256;
    private static final int ALLOW_PERMISSIONS = 4;
    private static final int ALLOW_PRIVAPP_PERMISSIONS = 16;
    private static final int ALLOW_SIGNATURE_PERMISSIONS = 2048;
    private static final int ALLOW_VENDOR_APEX = 1024;
    private static final java.lang.String NO_RIL_PROPERTY = "ro.radio.noril";
    private static final java.lang.String PARTITION_PATH_OPEX = "/mnt/opex";
    private static final java.lang.String PRODUCT_SKU_PROPERTY = "ro.boot.hardware.sku";
    private static final java.lang.String SKU_PROPERTY = "ro.boot.product.hardware.sku";
    static final java.lang.String TAG = "SystemConfig";
    private static final java.lang.String VENDOR_SKU_PROPERTY = "ro.boot.product.vendor.sku";
    static com.android.server.SystemConfig sInstance;
    private java.lang.String mModulesInstallerPackageName;
    private java.lang.String mOverlayConfigSignaturePackage;
    private static final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> EMPTY_PERMISSIONS = new android.util.ArrayMap<>();
    private static final com.android.server.ISystemConfigStaticWrapper STATIC_WRAPPER = new com.android.server.SystemConfig.SystemConfigStaticWrapper();
    int[] mGlobalGids = libcore.util.EmptyArray.INT;
    final android.util.SparseArray<android.util.ArraySet<java.lang.String>> mSystemPermissions = new android.util.SparseArray<>();
    final java.util.ArrayList<android.permission.PermissionManager.SplitPermissionInfo> mSplitPermissions = new java.util.ArrayList<>();
    final android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.SharedLibraryEntry> mSharedLibraries = new android.util.ArrayMap<>();
    final android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> mAvailableFeatures = new android.util.ArrayMap<>();
    final android.util.ArraySet<java.lang.String> mUnavailableFeatures = new android.util.ArraySet<>();
    final android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.PermissionEntry> mPermissions = new android.util.ArrayMap<>();
    final android.util.ArraySet<java.lang.String> mAllowInPowerSaveExceptIdle = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mAllowInPowerSave = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mAllowInDataUsageSave = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mAllowUnthrottledLocation = new android.util.ArraySet<>();
    final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> mAllowAdasSettings = new android.util.ArrayMap<>();
    final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> mAllowIgnoreLocationSettings = new android.util.ArrayMap<>();
    final android.util.ArraySet<java.lang.String> mAllowlistCameraPrivacy = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mAllowImplicitBroadcasts = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mBgRestrictionExemption = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mLinkedApps = new android.util.ArraySet<>();
    final android.util.ArraySet<android.content.ComponentName> mDefaultVrComponents = new android.util.ArraySet<>();
    final android.util.ArraySet<android.content.ComponentName> mBackupTransportWhitelist = new android.util.ArraySet<>();
    final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mPackageComponentEnabledState = new android.util.ArrayMap<>();
    final android.util.ArraySet<java.lang.String> mHiddenApiPackageWhitelist = new android.util.ArraySet<>();
    final android.util.ArraySet<java.lang.String> mDisabledUntilUsedPreinstalledCarrierApps = new android.util.ArraySet<>();
    final android.util.ArrayMap<java.lang.String, java.util.List<android.os.CarrierAssociatedAppEntry>> mDisabledUntilUsedPreinstalledCarrierAssociatedApps = new android.util.ArrayMap<>();
    private final com.android.server.pm.permission.PermissionAllowlist mPermissionAllowlist = new com.android.server.pm.permission.PermissionAllowlist();
    final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> mAllowedAssociations = new android.util.ArrayMap<>();
    private final android.util.ArraySet<java.lang.String> mBugreportWhitelistedPackages = new android.util.ArraySet<>();
    private final android.util.ArraySet<java.lang.String> mAppDataIsolationWhitelistedApps = new android.util.ArraySet<>();
    private final java.util.ArrayList<java.lang.String> mPreventUserDisablePackages = new java.util.ArrayList<>();
    private android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> mPackageToUserTypeWhitelist = new android.util.ArrayMap<>();
    private android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> mPackageToUserTypeBlacklist = new android.util.ArrayMap<>();
    private final android.util.ArraySet<java.lang.String> mRollbackWhitelistedPackages = new android.util.ArraySet<>();
    private final android.util.ArraySet<java.lang.String> mWhitelistedStagedInstallers = new android.util.ArraySet<>();
    private final android.util.ArrayMap<java.lang.String, java.lang.String> mAllowedVendorApexes = new android.util.ArrayMap<>();
    private final java.util.Set<java.lang.String> mInstallConstraintsAllowlist = new android.util.ArraySet();
    private final android.util.ArrayMap<java.lang.String, java.lang.String> mUpdateOwnersForSystemApps = new android.util.ArrayMap<>();
    private final java.util.Set<java.lang.String> mInitialNonStoppedSystemPackages = new android.util.ArraySet();
    private final android.util.ArrayMap<java.lang.String, java.lang.String> mPackageToSharedUidAllowList = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, java.lang.String> mAppMetadataFilePaths = new android.util.ArrayMap<>();
    private final java.util.Set<java.lang.String> mPreinstallPackagesWithStrictSignatureCheck = new android.util.ArraySet();
    private final android.util.ArraySet<android.content.pm.SignedPackage> mEnhancedConfirmationTrustedPackages = new android.util.ArraySet<>();
    private final android.util.ArraySet<android.content.pm.SignedPackage> mEnhancedConfirmationTrustedInstallers = new android.util.ArraySet<>();
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mNamedActors = null;
    com.android.server.ISystemConfigExt mSystemConfigExt = (com.android.server.ISystemConfigExt) system.ext.loader.core.ExtLoader.type(com.android.server.ISystemConfigExt.class).base(this).create();
    com.android.server.ISystemConfigSocExt mSystemConfigSocExt = (com.android.server.ISystemConfigSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.ISystemConfigSocExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAtLeastSdkLevel(java.lang.String version) {
        try {
            return com.android.modules.utils.build.UnboundedSdkLevel.isAtLeast(version);
        } catch (java.lang.IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isAtMostSdkLevel(java.lang.String version) {
        try {
            return com.android.modules.utils.build.UnboundedSdkLevel.isAtMost(version);
        } catch (java.lang.IllegalArgumentException e) {
            return true;
        }
    }

    public static final class SharedLibraryEntry {
        public final boolean canBeSafelyIgnored;
        public final java.lang.String[] dependencies;
        public final java.lang.String filename;
        public final boolean isNative;
        public final java.lang.String name;
        public final java.lang.String onBootclasspathBefore;
        public final java.lang.String onBootclasspathSince;

        public SharedLibraryEntry(java.lang.String name, java.lang.String filename, java.lang.String[] dependencies, boolean isNative) {
            this(name, filename, dependencies, null, null, isNative);
        }

        public SharedLibraryEntry(java.lang.String name, java.lang.String filename, java.lang.String[] dependencies, java.lang.String onBootclasspathSince, java.lang.String onBootclasspathBefore) {
            this(name, filename, dependencies, onBootclasspathSince, onBootclasspathBefore, false);
        }

        SharedLibraryEntry(java.lang.String name, java.lang.String filename, java.lang.String[] dependencies, java.lang.String onBootclasspathSince, java.lang.String onBootclasspathBefore, boolean isNative) {
            this.name = name;
            this.filename = filename;
            this.dependencies = dependencies;
            this.onBootclasspathSince = onBootclasspathSince;
            this.onBootclasspathBefore = onBootclasspathBefore;
            this.isNative = isNative;
            this.canBeSafelyIgnored = (this.onBootclasspathSince != null && com.android.server.SystemConfig.isAtLeastSdkLevel(this.onBootclasspathSince)) || !(this.onBootclasspathBefore == null || com.android.server.SystemConfig.isAtLeastSdkLevel(this.onBootclasspathBefore));
        }
    }

    public static final class PermissionEntry {
        public int[] gids;
        public final java.lang.String name;
        public boolean perUser;

        PermissionEntry(java.lang.String name, boolean perUser) {
            this.name = name;
            this.perUser = perUser;
        }
    }

    public static com.android.server.SystemConfig getInstance() {
        com.android.server.SystemConfig systemConfig;
        if (!isSystemProcess()) {
            android.util.Slog.wtf(TAG, "SystemConfig is being accessed by a process other than system_server.");
        }
        synchronized (com.android.server.SystemConfig.class) {
            if (sInstance == null) {
                sInstance = new com.android.server.SystemConfig();
            }
            systemConfig = sInstance;
        }
        return systemConfig;
    }

    public int[] getGlobalGids() {
        return this.mGlobalGids;
    }

    public android.util.SparseArray<android.util.ArraySet<java.lang.String>> getSystemPermissions() {
        return this.mSystemPermissions;
    }

    public java.util.ArrayList<android.permission.PermissionManager.SplitPermissionInfo> getSplitPermissions() {
        return this.mSplitPermissions;
    }

    public android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.SharedLibraryEntry> getSharedLibraries() {
        return this.mSharedLibraries;
    }

    public android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> getAvailableFeatures() {
        return this.mAvailableFeatures;
    }

    public android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.PermissionEntry> getPermissions() {
        return this.mPermissions;
    }

    public android.util.ArraySet<java.lang.String> getAllowImplicitBroadcasts() {
        return this.mAllowImplicitBroadcasts;
    }

    public android.util.ArraySet<java.lang.String> getAllowInPowerSaveExceptIdle() {
        return this.mAllowInPowerSaveExceptIdle;
    }

    public android.util.ArraySet<java.lang.String> getAllowInPowerSave() {
        return this.mAllowInPowerSave;
    }

    public android.util.ArraySet<java.lang.String> getAllowInDataUsageSave() {
        return this.mAllowInDataUsageSave;
    }

    public android.util.ArraySet<java.lang.String> getAllowUnthrottledLocation() {
        return this.mAllowUnthrottledLocation;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> getAllowAdasLocationSettings() {
        return this.mAllowAdasSettings;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> getAllowIgnoreLocationSettings() {
        return this.mAllowIgnoreLocationSettings;
    }

    public android.util.ArraySet<java.lang.String> getBgRestrictionExemption() {
        return this.mBgRestrictionExemption;
    }

    public android.util.ArraySet<java.lang.String> getLinkedApps() {
        return this.mLinkedApps;
    }

    public android.util.ArraySet<java.lang.String> getHiddenApiWhitelistedApps() {
        return this.mHiddenApiPackageWhitelist;
    }

    public android.util.ArraySet<android.content.ComponentName> getDefaultVrComponents() {
        return this.mDefaultVrComponents;
    }

    public android.util.ArraySet<android.content.ComponentName> getBackupTransportWhitelist() {
        return this.mBackupTransportWhitelist;
    }

    public android.util.ArrayMap<java.lang.String, java.lang.Boolean> getComponentsEnabledStates(java.lang.String packageName) {
        return this.mPackageComponentEnabledState.get(packageName);
    }

    public android.util.ArraySet<java.lang.String> getDisabledUntilUsedPreinstalledCarrierApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierApps;
    }

    public android.util.ArrayMap<java.lang.String, java.util.List<android.os.CarrierAssociatedAppEntry>> getDisabledUntilUsedPreinstalledCarrierAssociatedApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps;
    }

    public com.android.server.pm.permission.PermissionAllowlist getPermissionAllowlist() {
        return this.mPermissionAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> getAllowedAssociations() {
        return this.mAllowedAssociations;
    }

    public android.util.ArraySet<java.lang.String> getCameraPrivacyAllowlist() {
        return this.mAllowlistCameraPrivacy;
    }

    public android.util.ArraySet<java.lang.String> getBugreportWhitelistedPackages() {
        return this.mBugreportWhitelistedPackages;
    }

    public java.util.Set<java.lang.String> getRollbackWhitelistedPackages() {
        return this.mRollbackWhitelistedPackages;
    }

    public java.util.Set<java.lang.String> getWhitelistedStagedInstallers() {
        return this.mWhitelistedStagedInstallers;
    }

    public java.util.Map<java.lang.String, java.lang.String> getAllowedVendorApexes() {
        return this.mAllowedVendorApexes;
    }

    public java.util.Set<java.lang.String> getInstallConstraintsAllowlist() {
        return this.mInstallConstraintsAllowlist;
    }

    public java.lang.String getModulesInstallerPackageName() {
        return this.mModulesInstallerPackageName;
    }

    public java.lang.String getSystemAppUpdateOwnerPackageName(java.lang.String packageName) {
        return this.mUpdateOwnersForSystemApps.get(packageName);
    }

    public android.util.ArraySet<java.lang.String> getAppDataIsolationWhitelistedApps() {
        return this.mAppDataIsolationWhitelistedApps;
    }

    public java.util.ArrayList<java.lang.String> getPreventUserDisablePackages() {
        return this.mPreventUserDisablePackages;
    }

    public android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> getAndClearPackageToUserTypeWhitelist() {
        android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> r = this.mPackageToUserTypeWhitelist;
        this.mPackageToUserTypeWhitelist = new android.util.ArrayMap<>(0);
        return r;
    }

    public android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> getAndClearPackageToUserTypeBlacklist() {
        android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> r = this.mPackageToUserTypeBlacklist;
        this.mPackageToUserTypeBlacklist = new android.util.ArrayMap<>(0);
        return r;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getNamedActors() {
        return this.mNamedActors != null ? this.mNamedActors : java.util.Collections.emptyMap();
    }

    public java.lang.String getOverlayConfigSignaturePackage() {
        if (android.text.TextUtils.isEmpty(this.mOverlayConfigSignaturePackage)) {
            return null;
        }
        return this.mOverlayConfigSignaturePackage;
    }

    public java.util.Set<java.lang.String> getInitialNonStoppedSystemPackages() {
        return this.mInitialNonStoppedSystemPackages;
    }

    public android.util.ArrayMap<java.lang.String, java.lang.String> getPackageToSharedUidAllowList() {
        return this.mPackageToSharedUidAllowList;
    }

    public android.util.ArrayMap<java.lang.String, java.lang.String> getAppMetadataFilePaths() {
        return this.mAppMetadataFilePaths;
    }

    public java.util.Set<java.lang.String> getPreinstallPackagesWithStrictSignatureCheck() {
        return this.mPreinstallPackagesWithStrictSignatureCheck;
    }

    public android.util.ArraySet<android.content.pm.SignedPackage> getEnhancedConfirmationTrustedPackages() {
        return this.mEnhancedConfirmationTrustedPackages;
    }

    public android.util.ArraySet<android.content.pm.SignedPackage> getEnhancedConfirmationTrustedInstallers() {
        return this.mEnhancedConfirmationTrustedInstallers;
    }

    public SystemConfig(boolean readPermissions) throws java.lang.Throwable {
        if (readPermissions) {
            android.util.Slog.w(TAG, "Constructing a test SystemConfig");
            readAllPermissions();
            this.mSystemConfigExt.readConfigInConstructor();
            return;
        }
        android.util.Slog.w(TAG, "Constructing an empty test SystemConfig");
    }

    SystemConfig() {
        android.util.TimingsTraceLog log = new android.util.TimingsTraceLog(TAG, 524288L);
        log.traceBegin("readAllPermissions");
        try {
            readAllPermissions();
            readPublicNativeLibrariesList();
            this.mSystemConfigExt.readConfigInConstructor();
        } finally {
            log.traceEnd();
        }
    }

    private void readAllPermissions() throws java.lang.Throwable {
        java.lang.String productSkuProperty;
        java.lang.String vendorSkuProperty;
        org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getRootDirectory(), new java.lang.String[]{"etc", "sysconfig"}), -1);
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getRootDirectory(), new java.lang.String[]{"etc", "permissions"}), -1);
        int vendorPermissionFlag = 3219;
        if (android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT <= 27) {
            vendorPermissionFlag = 3219 | 12;
        }
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{"etc", "sysconfig"}), vendorPermissionFlag);
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{"etc", "permissions"}), vendorPermissionFlag);
        java.lang.String vendorSkuProperty2 = android.os.SystemProperties.get(VENDOR_SKU_PROPERTY, "");
        if (!vendorSkuProperty2.isEmpty()) {
            java.lang.String vendorSkuDir = "sku_" + vendorSkuProperty2;
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{"etc", "sysconfig", vendorSkuDir}), vendorPermissionFlag);
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{"etc", "permissions", vendorSkuDir}), vendorPermissionFlag);
        }
        boolean noRilSupport = android.os.SystemProperties.getBoolean(NO_RIL_PROPERTY, false);
        if (noRilSupport) {
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{"etc", "sysconfig", "noRil"}), vendorPermissionFlag);
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{"etc", "permissions", "noRil"}), vendorPermissionFlag);
        }
        int odmPermissionFlag = vendorPermissionFlag;
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{"etc", "sysconfig"}), odmPermissionFlag);
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{"etc", "permissions"}), odmPermissionFlag);
        java.lang.String skuProperty = android.os.SystemProperties.get(SKU_PROPERTY, "");
        if (!skuProperty.isEmpty()) {
            java.lang.String skuDir = "sku_" + skuProperty;
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{"etc", "sysconfig", skuDir}), odmPermissionFlag);
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{"etc", "permissions", skuDir}), odmPermissionFlag);
        }
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getOemDirectory(), new java.lang.String[]{"etc", "sysconfig"}), 1185);
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getOemDirectory(), new java.lang.String[]{"etc", "permissions"}), 1185);
        int productPermissionFlag = 4063;
        if (android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT <= 30) {
            productPermissionFlag = -1;
        }
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getProductDirectory(), new java.lang.String[]{"etc", "sysconfig"}), productPermissionFlag);
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getProductDirectory(), new java.lang.String[]{"etc", "permissions"}), productPermissionFlag);
        java.lang.String productSkuProperty2 = android.os.SystemProperties.get(PRODUCT_SKU_PROPERTY, "");
        if (!productSkuProperty2.isEmpty()) {
            java.lang.String productSkuDir = "sku_" + productSkuProperty2;
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getProductDirectory(), new java.lang.String[]{"etc", "sysconfig", productSkuDir}), productPermissionFlag);
            readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getProductDirectory(), new java.lang.String[]{"etc", "permissions", productSkuDir}), productPermissionFlag);
        }
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getSystemExtDirectory(), new java.lang.String[]{"etc", "sysconfig"}), -1);
        readPermissions(parser, android.os.Environment.buildPath(android.os.Environment.getSystemExtDirectory(), new java.lang.String[]{"etc", "permissions"}), -1);
        if (!isSystemProcess()) {
            return;
        }
        int apexPermissionFlag = 19;
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.apexSignaturePermissionAllowlistEnabled()) {
            apexPermissionFlag = 19 | 2048;
        }
        java.io.File[] fileArrListFilesOrEmpty = android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getApexDirectory());
        int length = fileArrListFilesOrEmpty.length;
        int i = 0;
        while (i < length) {
            int vendorPermissionFlag2 = vendorPermissionFlag;
            java.io.File f = fileArrListFilesOrEmpty[i];
            if (f.isFile()) {
                productSkuProperty = productSkuProperty2;
                vendorSkuProperty = vendorSkuProperty2;
            } else {
                productSkuProperty = productSkuProperty2;
                vendorSkuProperty = vendorSkuProperty2;
                if (!f.getPath().contains("@")) {
                    readPermissions(parser, android.os.Environment.buildPath(f, new java.lang.String[]{"etc", "permissions"}), apexPermissionFlag);
                }
            }
            i++;
            vendorPermissionFlag = vendorPermissionFlag2;
            productSkuProperty2 = productSkuProperty;
            vendorSkuProperty2 = vendorSkuProperty;
        }
        this.mSystemConfigExt.scanAndLoadAppPermissionInOpex(PARTITION_PATH_OPEX);
    }

    public void readPermissions(org.xmlpull.v1.XmlPullParser parser, java.io.File libraryDir, int permissionFlag) throws java.lang.Throwable {
        if (!libraryDir.exists() || !libraryDir.isDirectory()) {
            if (permissionFlag == -1) {
                android.util.Slog.w(TAG, "No directory " + libraryDir + ", skipping");
                return;
            }
            return;
        }
        if (!libraryDir.canRead()) {
            android.util.Slog.w(TAG, "Directory " + libraryDir + " cannot be read");
            return;
        }
        java.io.File platformFile = null;
        for (java.io.File f : libraryDir.listFiles()) {
            if (f.isFile()) {
                if (f.getPath().endsWith("etc/permissions/platform.xml")) {
                    platformFile = f;
                } else if (!f.getPath().endsWith(".xml")) {
                    android.util.Slog.i(TAG, "Non-xml file " + f + " in " + libraryDir + " directory, ignoring");
                } else if (!f.canRead()) {
                    android.util.Slog.w(TAG, "Permissions library file " + f + " cannot be read");
                } else if (!this.mSystemConfigExt.filterFileInReadPermissions(f)) {
                    readPermissionsFromXml(parser, f, permissionFlag);
                }
            }
        }
        if (platformFile != null) {
            readPermissionsFromXml(parser, platformFile, permissionFlag);
        }
    }

    private void logNotAllowedInPartition(java.lang.String name, java.io.File permFile, org.xmlpull.v1.XmlPullParser parser) {
        android.util.Slog.w(TAG, "<" + name + "> not allowed in partition of " + permFile + " at " + parser.getPositionDescription());
    }

    /* JADX WARN: Removed duplicated region for block: B:230:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0394 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TRY_ENTER, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x03b1 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, FALL_THROUGH, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x03d4 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:252:0x041e A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0494 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:268:0x050a A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0586 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:286:0x05ff  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0642  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x06bb  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x0717 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0754  */
    /* JADX WARN: Removed duplicated region for block: B:337:0x07c5 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0912 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:363:0x091f A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:368:0x095c A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:373:0x0999 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:378:0x09d6  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0a9f  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x0ae2  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0af9  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x0c00  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x0d11  */
    /* JADX WARN: Removed duplicated region for block: B:452:0x0d54  */
    /* JADX WARN: Removed duplicated region for block: B:472:0x0e02  */
    /* JADX WARN: Removed duplicated region for block: B:483:0x0e85 A[Catch: IOException -> 0x15f1, XmlPullParserException -> 0x15f4, all -> 0x16b5, TryCatch #11 {all -> 0x16b5, blocks: (B:701:0x159a, B:231:0x0394, B:233:0x039a, B:235:0x03a0, B:237:0x03b1, B:239:0x03b7, B:241:0x03bd, B:244:0x03d4, B:246:0x03da, B:248:0x03e4, B:249:0x040d, B:252:0x041e, B:254:0x042f, B:255:0x0458, B:257:0x045e, B:258:0x0489, B:260:0x0494, B:262:0x04a5, B:263:0x04ce, B:265:0x04d4, B:266:0x04ff, B:268:0x050a, B:270:0x051b, B:271:0x0544, B:273:0x054a, B:274:0x0575, B:276:0x057b, B:278:0x0586, B:280:0x0597, B:285:0x05f6, B:281:0x05c0, B:283:0x05c6, B:284:0x05f1, B:287:0x0601, B:289:0x0607, B:293:0x0639, B:290:0x0630, B:292:0x0636, B:295:0x0644, B:297:0x0651, B:299:0x067b, B:302:0x06a9, B:305:0x06b2, B:304:0x06af, B:307:0x06bd, B:309:0x06cb, B:312:0x06fb, B:314:0x06ff, B:315:0x0702, B:316:0x0709, B:319:0x070e, B:310:0x06f4, B:318:0x070b, B:320:0x0717, B:322:0x071d, B:324:0x074b, B:323:0x0746, B:326:0x0756, B:328:0x075c, B:336:0x07bc, B:329:0x0785, B:331:0x078d, B:333:0x0794, B:334:0x07b8, B:335:0x07b9, B:337:0x07c5, B:339:0x07e3, B:357:0x08a3, B:340:0x080f, B:342:0x0815, B:343:0x0840, B:345:0x0846, B:346:0x0871, B:348:0x0879, B:350:0x087d, B:351:0x0884, B:353:0x088e, B:356:0x08a0, B:354:0x089a, B:358:0x08ac, B:359:0x08e8, B:360:0x08e9, B:361:0x0911, B:362:0x0912, B:363:0x091f, B:365:0x0925, B:367:0x0953, B:366:0x094e, B:368:0x095c, B:370:0x0962, B:372:0x0990, B:371:0x098b, B:373:0x0999, B:375:0x099f, B:377:0x09cd, B:376:0x09c8, B:379:0x09d8, B:381:0x09e1, B:382:0x0a14, B:384:0x0a1c, B:385:0x0a4f, B:387:0x0a63, B:388:0x0a6e, B:390:0x0a96, B:389:0x0a93, B:392:0x0aa1, B:394:0x0aa7, B:398:0x0ad9, B:395:0x0ad0, B:397:0x0ad6, B:400:0x0ae4, B:401:0x0aed, B:404:0x0afd, B:406:0x0b20, B:411:0x0b47, B:413:0x0bb7, B:415:0x0bc3, B:417:0x0bcf, B:419:0x0bdb, B:420:0x0be5, B:422:0x0bf4, B:425:0x0c04, B:427:0x0c27, B:432:0x0c4e, B:434:0x0cc5, B:436:0x0cd1, B:438:0x0cdd, B:440:0x0cea, B:441:0x0cf6, B:443:0x0d05, B:445:0x0d13, B:447:0x0d19, B:451:0x0d4b, B:448:0x0d42, B:450:0x0d48, B:453:0x0d56, B:457:0x0d65, B:459:0x0d72, B:464:0x0dad, B:466:0x0db7, B:467:0x0dc2, B:471:0x0df9, B:463:0x0d7a, B:468:0x0dcb, B:470:0x0df6, B:473:0x0e04, B:475:0x0e0d, B:482:0x0e7c, B:476:0x0e38, B:478:0x0e3e, B:479:0x0e73, B:481:0x0e79, B:483:0x0e85, B:485:0x0e90, B:487:0x0e9c, B:493:0x0f00, B:489:0x0ec7, B:490:0x0ef2, B:492:0x0efd, B:495:0x0f0b, B:497:0x0f11, B:501:0x0f43, B:498:0x0f3a, B:500:0x0f40, B:503:0x0f4e, B:505:0x0f54, B:509:0x0f86, B:506:0x0f7d, B:508:0x0f83, B:511:0x0f91, B:513:0x0f99, B:517:0x0fcd, B:514:0x0fc4, B:516:0x0fca, B:519:0x0fd8, B:521:0x0fe4, B:536:0x1044, B:522:0x100d, B:524:0x1017, B:527:0x101f, B:528:0x102b, B:530:0x1033, B:533:0x103d, B:535:0x1041, B:538:0x104f, B:540:0x1055, B:544:0x1087, B:541:0x107e, B:543:0x1084, B:546:0x1092, B:548:0x109e, B:563:0x10fe, B:549:0x10c7, B:551:0x10d1, B:554:0x10d9, B:555:0x10e5, B:557:0x10ed, B:560:0x10f7, B:562:0x10fb, B:565:0x1109, B:567:0x110f, B:571:0x1141, B:568:0x1138, B:570:0x113e, B:573:0x114c, B:575:0x1152, B:579:0x1184, B:576:0x117b, B:578:0x1181, B:581:0x118f, B:583:0x1195, B:587:0x11cc, B:584:0x11be, B:586:0x11c9, B:589:0x11d7, B:591:0x11dd, B:595:0x1214, B:592:0x1206, B:594:0x1211, B:597:0x121f, B:599:0x1225, B:603:0x125e, B:600:0x1250, B:602:0x125b, B:606:0x126b, B:611:0x128f, B:616:0x12c7, B:613:0x12ba, B:609:0x127c, B:615:0x12c3, B:622:0x12f8, B:663:0x1409, B:624:0x1326, B:626:0x1356, B:632:0x1362, B:662:0x1406, B:716:0x160f, B:719:0x161a, B:666:0x1414, B:667:0x1419, B:670:0x1429, B:672:0x1431, B:673:0x145e, B:675:0x1468, B:676:0x1497, B:678:0x149d, B:679:0x14d6, B:681:0x14e5, B:682:0x14f0, B:684:0x14f8, B:683:0x14f5, B:687:0x1505, B:689:0x150c, B:690:0x1539, B:691:0x1543, B:694:0x1551, B:696:0x155b, B:700:0x1596, B:697:0x1568, B:699:0x1593, B:703:0x15e1, B:704:0x15f0), top: B:769:0x002c }] */
    /* JADX WARN: Removed duplicated region for block: B:484:0x0e8e  */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0f09  */
    /* JADX WARN: Removed duplicated region for block: B:502:0x0f4c  */
    /* JADX WARN: Removed duplicated region for block: B:510:0x0f8f  */
    /* JADX WARN: Removed duplicated region for block: B:518:0x0fd6  */
    /* JADX WARN: Removed duplicated region for block: B:537:0x104d  */
    /* JADX WARN: Removed duplicated region for block: B:545:0x1090  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x1107  */
    /* JADX WARN: Removed duplicated region for block: B:572:0x114a  */
    /* JADX WARN: Removed duplicated region for block: B:580:0x118d  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x11d5  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x121d  */
    /* JADX WARN: Removed duplicated region for block: B:604:0x1267  */
    /* JADX WARN: Removed duplicated region for block: B:618:0x12d0  */
    /* JADX WARN: Removed duplicated region for block: B:664:0x140e  */
    /* JADX WARN: Removed duplicated region for block: B:668:0x1421  */
    /* JADX WARN: Removed duplicated region for block: B:685:0x14fd  */
    /* JADX WARN: Removed duplicated region for block: B:692:0x154b  */
    /* JADX WARN: Removed duplicated region for block: B:723:0x1628  */
    /* JADX WARN: Removed duplicated region for block: B:724:0x1634  */
    /* JADX WARN: Removed duplicated region for block: B:727:0x163b  */
    /* JADX WARN: Removed duplicated region for block: B:730:0x1646  */
    /* JADX WARN: Removed duplicated region for block: B:731:0x164c  */
    /* JADX WARN: Removed duplicated region for block: B:734:0x1657  */
    /* JADX WARN: Removed duplicated region for block: B:737:0x1668  */
    /* JADX WARN: Removed duplicated region for block: B:740:0x1676  */
    /* JADX WARN: Removed duplicated region for block: B:749:0x16a1  */
    /*  JADX ERROR: UnsupportedOperationException in pass: RegionMakerVisitor
        java.lang.UnsupportedOperationException
        	at java.base/java.util.Collections$UnmodifiableCollection.add(Unknown Source)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker$1.leaveRegion(SwitchRegionMaker.java:390)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:23)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaksForCase(SwitchRegionMaker.java:370)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaks(SwitchRegionMaker.java:85)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.leaveRegion(PostProcessRegions.java:33)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.process(PostProcessRegions.java:23)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:31)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readPermissionsFromXml(org.xmlpull.v1.XmlPullParser r42, java.io.File r43, int r44) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 6120
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemConfig.readPermissionsFromXml(org.xmlpull.v1.XmlPullParser, java.io.File, int):void");
    }

    private android.content.pm.SignedPackage parseEnhancedConfirmationTrustedPackage(org.xmlpull.v1.XmlPullParser parser, java.io.File permFile, java.lang.String elementName) {
        java.lang.String pkgName = parser.getAttributeValue(null, "package");
        if (android.text.TextUtils.isEmpty(pkgName)) {
            android.util.Slog.w(TAG, "<" + elementName + "> without package " + permFile + " at " + parser.getPositionDescription());
            return null;
        }
        java.lang.String certificateDigestStr = parser.getAttributeValue(null, "sha256-cert-digest");
        if (android.text.TextUtils.isEmpty(certificateDigestStr)) {
            android.util.Slog.w(TAG, "<" + elementName + "> without sha256-cert-digest in " + permFile + " at " + parser.getPositionDescription());
            return null;
        }
        try {
            byte[] certificateDigest = new android.content.pm.Signature(certificateDigestStr.replace(":", "")).toByteArray();
            return new android.content.pm.SignedPackage(pkgName, certificateDigest);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.w(TAG, "<" + elementName + "> with invalid sha256-cert-digest in " + permFile + " at " + parser.getPositionDescription());
            return null;
        }
    }

    private void enableIpSecTunnelMigrationOnVsrUAndAbove() {
        int vsrApi = android.os.SystemProperties.getInt("ro.vendor.api_level", android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT);
        if (vsrApi > 33) {
            addFeature("android.software.ipsec_tunnel_migration", 0);
        }
    }

    private void addFeature(java.lang.String name, int version) {
        android.content.pm.FeatureInfo fi = this.mAvailableFeatures.get(name);
        if (fi == null) {
            android.content.pm.FeatureInfo fi2 = new android.content.pm.FeatureInfo();
            fi2.name = name;
            fi2.version = version;
            this.mAvailableFeatures.put(name, fi2);
            return;
        }
        fi.version = java.lang.Math.max(fi.version, version);
    }

    private void removeFeature(java.lang.String name) {
        if (this.mAvailableFeatures.remove(name) != null) {
            android.util.Slog.d(TAG, "Removed unavailable feature " + name);
        }
    }

    void readPermission(org.xmlpull.v1.XmlPullParser parser, java.lang.String name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (this.mPermissions.containsKey(name)) {
            throw new java.lang.IllegalStateException("Duplicate permission definition for " + name);
        }
        boolean perUser = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, "perUser", false);
        com.android.server.SystemConfig.PermissionEntry perm = new com.android.server.SystemConfig.PermissionEntry(name, perUser);
        this.mPermissions.put(name, perm);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if ("group".equals(tagName)) {
                            java.lang.String gidStr = parser.getAttributeValue(null, "gid");
                            if (gidStr != null) {
                                int gid = android.os.Process.getGidForName(gidStr);
                                if (gid != -1) {
                                    perm.gids = com.android.internal.util.ArrayUtils.appendInt(perm.gids, gid);
                                } else {
                                    android.util.Slog.w(TAG, "<group> with unknown gid \"" + gidStr + " for permission " + name + " in " + parser.getPositionDescription());
                                }
                            } else {
                                android.util.Slog.w(TAG, "<group> without gid at " + parser.getPositionDescription());
                            }
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

    private void readPrivAppPermissions(org.xmlpull.v1.XmlPullParser parser, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> allowlist) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        readPermissionAllowlist(parser, allowlist, "privapp-permissions");
    }

    private void readSignatureAppPermissions(org.xmlpull.v1.XmlPullParser parser, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> allowlist) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        readPermissionAllowlist(parser, allowlist, "signature-permissions");
    }

    private void readInstallInUserType(org.xmlpull.v1.XmlPullParser parser, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> doInstallMap, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> nonInstallMap) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String packageName = parser.getAttributeValue(null, "package");
        if (android.text.TextUtils.isEmpty(packageName)) {
            android.util.Slog.w(TAG, "package is required for <install-in-user-type> in " + parser.getPositionDescription());
            return;
        }
        java.util.Set<java.lang.String> userTypesYes = doInstallMap.get(packageName);
        java.util.Set<java.lang.String> userTypesNo = nonInstallMap.get(packageName);
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            java.lang.String name = parser.getName();
            if ("install-in".equals(name)) {
                java.lang.String userType = parser.getAttributeValue(null, "user-type");
                if (android.text.TextUtils.isEmpty(userType)) {
                    android.util.Slog.w(TAG, "user-type is required for <install-in-user-type> in " + parser.getPositionDescription());
                } else {
                    if (userTypesYes == null) {
                        userTypesYes = new android.util.ArraySet();
                        doInstallMap.put(packageName, userTypesYes);
                    }
                    userTypesYes.add(userType);
                }
            } else if ("do-not-install-in".equals(name)) {
                java.lang.String userType2 = parser.getAttributeValue(null, "user-type");
                if (android.text.TextUtils.isEmpty(userType2)) {
                    android.util.Slog.w(TAG, "user-type is required for <install-in-user-type> in " + parser.getPositionDescription());
                } else {
                    if (userTypesNo == null) {
                        userTypesNo = new android.util.ArraySet();
                        nonInstallMap.put(packageName, userTypesNo);
                    }
                    userTypesNo.add(userType2);
                }
            } else {
                android.util.Slog.w(TAG, "unrecognized tag in <install-in-user-type> in " + parser.getPositionDescription());
            }
        }
    }

    void readOemPermissions(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        readPermissionAllowlist(parser, this.mPermissionAllowlist.getOemAppAllowlist(), "oem-permissions");
    }

    private static void readPermissionAllowlist(org.xmlpull.v1.XmlPullParser parser, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> allowlist, java.lang.String tagName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String packageName = parser.getAttributeValue(null, "package");
        if (android.text.TextUtils.isEmpty(packageName)) {
            android.util.Slog.w(TAG, "package is required for <" + tagName + "> in " + parser.getPositionDescription());
            return;
        }
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = allowlist.get(packageName);
        if (permissions == null) {
            permissions = new android.util.ArrayMap<>();
        }
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            java.lang.String name = parser.getName();
            if (com.android.server.permission.access.PermissionUri.SCHEME.equals(name)) {
                java.lang.String permissionName = parser.getAttributeValue(null, "name");
                if (android.text.TextUtils.isEmpty(permissionName)) {
                    android.util.Slog.w(TAG, "name is required for <permission> in " + parser.getPositionDescription());
                } else {
                    permissions.put(permissionName, java.lang.Boolean.TRUE);
                }
            } else if ("deny-permission".equals(name)) {
                java.lang.String permissionName2 = parser.getAttributeValue(null, "name");
                if (android.text.TextUtils.isEmpty(permissionName2)) {
                    android.util.Slog.w(TAG, "name is required for <deny-permission> in " + parser.getPositionDescription());
                } else {
                    permissions.put(permissionName2, java.lang.Boolean.FALSE);
                }
            }
        }
        allowlist.put(packageName, permissions);
    }

    private void readSplitPermission(org.xmlpull.v1.XmlPullParser parser, java.io.File permFile) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String splitPerm = parser.getAttributeValue(null, "name");
        if (splitPerm != null) {
            java.lang.String targetSdkStr = parser.getAttributeValue(null, "targetSdk");
            int targetSdk = 10001;
            if (!android.text.TextUtils.isEmpty(targetSdkStr)) {
                try {
                    targetSdk = java.lang.Integer.parseInt(targetSdkStr);
                } catch (java.lang.NumberFormatException e) {
                    android.util.Slog.w(TAG, "<split-permission> targetSdk not an integer in " + permFile + " at " + parser.getPositionDescription());
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    return;
                }
            }
            int depth = parser.getDepth();
            java.util.List<java.lang.String> newPermissions = new java.util.ArrayList<>();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                java.lang.String name = parser.getName();
                if ("new-permission".equals(name)) {
                    java.lang.String newName = parser.getAttributeValue(null, "name");
                    if (android.text.TextUtils.isEmpty(newName)) {
                        android.util.Slog.w(TAG, "name is required for <new-permission> in " + parser.getPositionDescription());
                    } else {
                        newPermissions.add(newName);
                    }
                } else {
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            }
            if (!newPermissions.isEmpty()) {
                this.mSplitPermissions.add(new android.permission.PermissionManager.SplitPermissionInfo(splitPerm, newPermissions, targetSdk));
                return;
            }
            return;
        }
        android.util.Slog.w(TAG, "<split-permission> without name in " + permFile + " at " + parser.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    private void readComponentOverrides(org.xmlpull.v1.XmlPullParser parser, java.io.File permFile) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String pkgname = parser.getAttributeValue(null, "package");
        if (pkgname == null) {
            android.util.Slog.w(TAG, "<component-override> without package in " + permFile + " at " + parser.getPositionDescription());
            return;
        }
        java.lang.String pkgname2 = pkgname.intern();
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            if ("component".equals(parser.getName())) {
                java.lang.String clsname = parser.getAttributeValue(null, "class");
                java.lang.String enabled = parser.getAttributeValue(null, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
                if (clsname == null) {
                    android.util.Slog.w(TAG, "<component> without class in " + permFile + " at " + parser.getPositionDescription());
                    return;
                }
                if (enabled == null) {
                    android.util.Slog.w(TAG, "<component> without enabled in " + permFile + " at " + parser.getPositionDescription());
                    return;
                }
                if (clsname.startsWith(".")) {
                    clsname = pkgname2 + clsname;
                }
                java.lang.String clsname2 = clsname.intern();
                android.util.ArrayMap<java.lang.String, java.lang.Boolean> componentEnabledStates = this.mPackageComponentEnabledState.get(pkgname2);
                if (componentEnabledStates == null) {
                    componentEnabledStates = new android.util.ArrayMap<>();
                    this.mPackageComponentEnabledState.put(pkgname2, componentEnabledStates);
                }
                componentEnabledStates.put(clsname2, java.lang.Boolean.valueOf(!"false".equals(enabled)));
            }
        }
    }

    private void readPublicNativeLibrariesList() {
        readPublicLibrariesListFile(new java.io.File("/vendor/etc/public.libraries.txt"));
        java.lang.String[] dirs = {"/system/etc", "/system_ext/etc", "/product/etc"};
        for (java.lang.String dir : dirs) {
            java.io.File[] files = new java.io.File(dir).listFiles();
            if (files == null) {
                android.util.Slog.w(TAG, "Public libraries file folder missing: " + dir);
            } else {
                for (java.io.File f : files) {
                    java.lang.String name = f.getName();
                    if (name.startsWith("public.libraries-") && name.endsWith(".txt")) {
                        readPublicLibrariesListFile(f);
                    }
                }
            }
        }
    }

    private void readPublicLibrariesListFile(java.io.File listFile) {
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(listFile));
            while (true) {
                try {
                    java.lang.String line = br.readLine();
                    if (line != null) {
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            java.lang.String soname = line.trim().split(" ")[0];
                            com.android.server.SystemConfig.SharedLibraryEntry entry = new com.android.server.SystemConfig.SharedLibraryEntry(soname, soname, new java.lang.String[0], true);
                            this.mSharedLibraries.put(entry.name, entry);
                        }
                    } else {
                        br.close();
                        return;
                    }
                } finally {
                }
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.d(TAG, listFile + " does not exist");
        } catch (java.io.IOException e2) {
            android.util.Slog.w(TAG, "Failed to read public libraries file " + listFile, e2);
        }
    }

    private java.lang.String getApexModuleNameFromFilePath(java.nio.file.Path path, java.nio.file.Path apexDirectoryPath) {
        if (!path.startsWith(apexDirectoryPath)) {
            throw new java.lang.IllegalArgumentException("File " + path + " is not part of an APEX.");
        }
        if (path.getNameCount() <= apexDirectoryPath.getNameCount() + 1) {
            throw new java.lang.IllegalArgumentException("File " + path + " is in the APEX partition, but not inside a module.");
        }
        return path.getName(apexDirectoryPath.getNameCount()).toString();
    }

    public void readApexPrivAppPermissions(org.xmlpull.v1.XmlPullParser parser, java.io.File permFile, java.nio.file.Path apexDirectoryPath) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String moduleName = getApexModuleNameFromFilePath(permFile.toPath(), apexDirectoryPath);
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>>> allowlists = this.mPermissionAllowlist.getApexPrivilegedAppAllowlists();
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> allowlist = allowlists.get(moduleName);
        if (allowlist == null) {
            allowlist = new android.util.ArrayMap<>();
            allowlists.put(moduleName, allowlist);
        }
        readPrivAppPermissions(parser, allowlist);
    }

    private static boolean isSystemProcess() {
        return android.os.Process.myUid() == 1000;
    }

    private static boolean isErofsSupported() {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("/sys/fs/erofs", new java.lang.String[0]);
            return java.nio.file.Files.exists(path, new java.nio.file.LinkOption[0]);
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private static boolean isKernelVersionAtLeast(int major, int minor) {
        java.lang.String kernelVersion = android.os.VintfRuntimeInfo.getKernelVersion();
        java.lang.String[] parts = kernelVersion.split("\\.");
        if (parts.length < 2) {
            return false;
        }
        try {
            int majorVersion = java.lang.Integer.parseInt(parts[0]);
            int minorVersion = java.lang.Integer.parseInt(parts[1]);
            return majorVersion > major || (majorVersion == major && minorVersion >= minor);
        } catch (java.lang.NumberFormatException e) {
            return false;
        }
    }

    public static com.android.server.ISystemConfigStaticWrapper getStaticWrapper() {
        return STATIC_WRAPPER;
    }

    private static class SystemConfigStaticWrapper implements com.android.server.ISystemConfigStaticWrapper {
        private SystemConfigStaticWrapper() {
        }

        public int getAllowPermissionsFlag() {
            return 4;
        }

        public int getAllowPrivAppPermissionsFlag() {
            return 16;
        }

        public int getAllowSignaturePermissionsFlag() {
            return 2048;
        }

        public int getAllowFeaturesFlag() {
            return 1;
        }

        public int getAllowAllFlag() {
            return -1;
        }

        public int getAllowLibsFlag() {
            return 2;
        }
    }
}
