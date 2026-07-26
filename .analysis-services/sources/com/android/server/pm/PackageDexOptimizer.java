package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageDexOptimizer {
    public static final int DEX_OPT_CANCELLED = 2;
    public static final int DEX_OPT_FAILED = -1;
    public static final int DEX_OPT_PERFORMED = 1;
    public static final int DEX_OPT_SKIPPED = 0;
    static final java.lang.String OAT_DIR_NAME = "oat";
    private static final java.lang.String TAG = "PackageDexOptimizer";
    private static final long WAKELOCK_TIMEOUT_MS = 660000;
    private static final java.util.Random sRandom = new java.util.Random();
    private final com.android.server.pm.dex.ArtStatsLogUtils.ArtStatsLogger mArtStatsLogger;
    private final android.content.Context mContext;
    private final android.os.PowerManager.WakeLock mDexoptWakeLock;
    private final com.android.server.pm.PackageDexOptimizer.Injector mInjector;
    private final com.android.server.pm.PackageManagerTracedLock mInstallLock;
    private final com.android.server.pm.Installer mInstaller;
    com.android.server.pm.IPackageDexOptimizerExt mPackageDexOptimizerExt;
    private volatile boolean mSystemReady;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DexOptResult {
    }

    interface Injector {
        com.android.server.apphibernation.AppHibernationManagerInternal getAppHibernationManagerInternal();

        android.os.PowerManager getPowerManager(android.content.Context context);
    }

    PackageDexOptimizer(com.android.server.pm.Installer installer, com.android.server.pm.PackageManagerTracedLock installLock, android.content.Context context, java.lang.String wakeLockTag) {
        this(new com.android.server.pm.PackageDexOptimizer.Injector() { // from class: com.android.server.pm.PackageDexOptimizer.1
            @Override // com.android.server.pm.PackageDexOptimizer.Injector
            public com.android.server.apphibernation.AppHibernationManagerInternal getAppHibernationManagerInternal() {
                return (com.android.server.apphibernation.AppHibernationManagerInternal) com.android.server.LocalServices.getService(com.android.server.apphibernation.AppHibernationManagerInternal.class);
            }

            @Override // com.android.server.pm.PackageDexOptimizer.Injector
            public android.os.PowerManager getPowerManager(android.content.Context context2) {
                return (android.os.PowerManager) context2.getSystemService(android.os.PowerManager.class);
            }
        }, installer, installLock, context, wakeLockTag);
    }

    protected PackageDexOptimizer(com.android.server.pm.PackageDexOptimizer from) {
        this.mArtStatsLogger = new com.android.server.pm.dex.ArtStatsLogUtils.ArtStatsLogger();
        this.mPackageDexOptimizerExt = (com.android.server.pm.IPackageDexOptimizerExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageDexOptimizerExt.class).base(this).create();
        this.mContext = from.mContext;
        this.mInstaller = from.mInstaller;
        this.mInstallLock = from.mInstallLock;
        this.mDexoptWakeLock = from.mDexoptWakeLock;
        this.mSystemReady = from.mSystemReady;
        this.mInjector = from.mInjector;
    }

    PackageDexOptimizer(com.android.server.pm.PackageDexOptimizer.Injector injector, com.android.server.pm.Installer installer, com.android.server.pm.PackageManagerTracedLock installLock, android.content.Context context, java.lang.String wakeLockTag) {
        this.mArtStatsLogger = new com.android.server.pm.dex.ArtStatsLogUtils.ArtStatsLogger();
        this.mPackageDexOptimizerExt = (com.android.server.pm.IPackageDexOptimizerExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageDexOptimizerExt.class).base(this).create();
        this.mContext = context;
        this.mInstaller = installer;
        this.mInstallLock = installLock;
        android.os.PowerManager powerManager = injector.getPowerManager(context);
        this.mDexoptWakeLock = powerManager.newWakeLock(1, wakeLockTag);
        this.mInjector = injector;
    }

    boolean canOptimizePackage(com.android.server.pm.pkg.AndroidPackage pkg) {
        if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(pkg.getPackageName()) || !pkg.isDeclaredHavingCode() || pkg.isApex()) {
            return false;
        }
        com.android.server.apphibernation.AppHibernationManagerInternal ahm = this.mInjector.getAppHibernationManagerInternal();
        return (ahm != null && ahm.isHibernatingGlobally(pkg.getPackageName()) && ahm.isOatArtifactDeletionEnabled()) ? false : true;
    }

    int performDexOpt(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, java.lang.String[] instructionSets, com.android.server.pm.CompilerStats.PackageStats packageStats, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo packageUseInfo, com.android.server.pm.dex.DexoptOptions options) throws com.android.server.pm.Installer.LegacyDexoptDisabledException {
        if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(pkg.getPackageName())) {
            throw new java.lang.IllegalArgumentException("System server dexopting should be done via odrefresh");
        }
        if (pkg.getUid() == -1) {
            throw new java.lang.IllegalArgumentException("Dexopt for " + pkg.getPackageName() + " has invalid uid.");
        }
        if (!canOptimizePackage(pkg)) {
            return 0;
        }
        com.android.server.pm.PackageManagerTracedLock installLock = this.mInstallLock.acquireLock();
        try {
            long acquireTime = acquireWakeLockLI(pkg.getUid());
            try {
                int iPerformDexOptLI = performDexOptLI(pkg, pkgSetting, instructionSets, packageStats, packageUseInfo, options);
                if (installLock != null) {
                    installLock.close();
                }
                return iPerformDexOptLI;
            } finally {
                releaseWakeLockLI(acquireTime);
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

    /* JADX WARN: Incorrect condition in loop: B:28:0x00e6 */
    /* JADX WARN: Removed duplicated region for block: B:150:0x03a9  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03af  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0112  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int performDexOptLI(com.android.server.pm.pkg.AndroidPackage r54, com.android.server.pm.pkg.PackageStateInternal r55, java.lang.String[] r56, com.android.server.pm.CompilerStats.PackageStats r57, com.android.server.pm.dex.PackageDexUsage.PackageUseInfo r58, com.android.server.pm.dex.DexoptOptions r59) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 993
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageDexOptimizer.performDexOptLI(com.android.server.pm.pkg.AndroidPackage, com.android.server.pm.pkg.PackageStateInternal, java.lang.String[], com.android.server.pm.CompilerStats$PackageStats, com.android.server.pm.dex.PackageDexUsage$PackageUseInfo, com.android.server.pm.dex.DexoptOptions):int");
    }

    private boolean prepareCloudProfile(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String profileName, java.lang.String path, java.lang.String dexMetadataPath) throws com.android.server.pm.Installer.LegacyDexoptDisabledException {
        if (dexMetadataPath != null) {
            if (!this.mInstaller.isIsolated()) {
                throw new com.android.server.pm.Installer.LegacyDexoptDisabledException();
            }
            return true;
        }
        return false;
    }

    private int dexOptPath(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, java.lang.String path, java.lang.String isa, java.lang.String compilerFilter, int profileAnalysisResult, java.lang.String classLoaderContext, int dexoptFlags, int uid, com.android.server.pm.CompilerStats.PackageStats packageStats, boolean downgrade, java.lang.String profileName, java.lang.String dexMetadataPath, int compilationReason) throws com.android.server.pm.Installer.LegacyDexoptDisabledException {
        java.lang.String oatDir = getPackageOatDirIfSupported(pkgSetting, pkg);
        int dexoptNeeded = getDexoptNeeded(pkg.getPackageName(), path, isa, compilerFilter, classLoaderContext, profileAnalysisResult, downgrade, dexoptFlags, oatDir);
        if (java.lang.Math.abs(dexoptNeeded) == 0 || this.mPackageDexOptimizerExt.skipDexoptInDexOptPath(pkg.getPackageName(), compilationReason)) {
            return 0;
        }
        java.lang.String compilerFilter2 = this.mPackageDexOptimizerExt.configDexoptBeforDoing(pkg.getPackageName(), compilerFilter, getAugmentedReasonName(compilationReason, dexMetadataPath != null));
        if (compilerFilter2 == null) {
            return -1;
        }
        android.util.Log.i(TAG, "Running dexopt (dexoptNeeded=" + dexoptNeeded + ") on: " + path + " pkg=" + pkg.getPackageName() + " isa=" + isa + " dexoptFlags=" + printDexoptFlags(dexoptFlags) + " targetFilter=" + compilerFilter2 + " oatDir=" + oatDir + " classLoaderContext=" + classLoaderContext);
        try {
            long startTime = java.lang.System.currentTimeMillis();
            java.lang.String seInfo = pkgSetting.getSeInfo();
            try {
                boolean completed = getInstallerLI().dexopt(path, uid, pkg.getPackageName(), isa, dexoptNeeded, oatDir, dexoptFlags, compilerFilter2, pkg.getVolumeUuid(), classLoaderContext, seInfo, false, pkg.getTargetSdkVersion(), profileName, dexMetadataPath, getAugmentedReasonName(compilationReason, dexMetadataPath != null));
                if (!completed) {
                    return 2;
                }
                if (packageStats != null) {
                    long endTime = java.lang.System.currentTimeMillis();
                    packageStats.setCompileTime(path, (int) (endTime - startTime));
                }
                if (oatDir != null) {
                    android.content.ContentResolver resolver = this.mContext.getContentResolver();
                    com.android.internal.content.F2fsUtils.releaseCompressedBlocks(resolver, new java.io.File(oatDir));
                }
                return 1;
            } catch (com.android.server.pm.Installer.InstallerException e) {
                e = e;
                android.util.Slog.w(TAG, "Failed to dexopt", e);
                return -1;
            }
        } catch (com.android.server.pm.Installer.InstallerException e2) {
            e = e2;
        }
    }

    private java.lang.String getAugmentedReasonName(int compilationReason, boolean useDexMetadata) {
        java.lang.String annotation = useDexMetadata ? com.android.server.pm.dex.ArtManagerService.DEXOPT_REASON_WITH_DEX_METADATA_ANNOTATION : "";
        return com.android.server.pm.PackageManagerServiceCompilerMapping.getReasonName(compilationReason) + annotation;
    }

    private long acquireWakeLockLI(int uid) {
        if (!this.mSystemReady) {
            return -1L;
        }
        this.mDexoptWakeLock.setWorkSource(new android.os.WorkSource(uid));
        this.mDexoptWakeLock.acquire(WAKELOCK_TIMEOUT_MS);
        return android.os.SystemClock.elapsedRealtime();
    }

    private void releaseWakeLockLI(long acquireTime) {
        if (acquireTime < 0) {
            return;
        }
        try {
            if (this.mDexoptWakeLock.isHeld()) {
                this.mDexoptWakeLock.release();
            }
            long duration = android.os.SystemClock.elapsedRealtime() - acquireTime;
            if (duration >= WAKELOCK_TIMEOUT_MS) {
                android.util.Slog.wtf(TAG, "WakeLock " + this.mDexoptWakeLock.getTag() + " time out. Operation took " + duration + " ms. Thread: " + java.lang.Thread.currentThread().getName());
            }
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.wtf(TAG, "Error while releasing " + this.mDexoptWakeLock.getTag() + " lock", e);
        }
    }

    protected int adjustDexoptNeeded(int dexoptNeeded) {
        return dexoptNeeded;
    }

    protected int adjustDexoptFlags(int dexoptFlags) {
        return dexoptFlags;
    }

    private java.lang.String getRealCompilerFilter(android.content.pm.ApplicationInfo info, java.lang.String targetCompilerFilter, boolean isUsedByOtherApps) {
        if (info.isEmbeddedDexUsed()) {
            return dalvik.system.DexFile.isOptimizedCompilerFilter(targetCompilerFilter) ? "verify" : targetCompilerFilter;
        }
        boolean vmSafeModeOrDebuggable = ((info.flags & 16384) == 0 && (info.flags & 2) == 0) ? false : true;
        if (vmSafeModeOrDebuggable) {
            return dalvik.system.DexFile.getSafeModeCompilerFilter(targetCompilerFilter);
        }
        if (dalvik.system.DexFile.isProfileGuidedCompilerFilter(targetCompilerFilter) && isUsedByOtherApps) {
            return com.android.server.pm.PackageManagerServiceCompilerMapping.getCompilerFilterForReason(com.android.server.pm.PackageManagerService.REASON_SHARED);
        }
        return targetCompilerFilter;
    }

    private java.lang.String getRealCompilerFilter(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String targetCompilerFilter) {
        if (pkg.isUseEmbeddedDex()) {
            return dalvik.system.DexFile.isOptimizedCompilerFilter(targetCompilerFilter) ? "verify" : targetCompilerFilter;
        }
        boolean vmSafeModeOrDebuggable = pkg.isVmSafeMode() || pkg.isDebuggable();
        if (vmSafeModeOrDebuggable) {
            return dalvik.system.DexFile.getSafeModeCompilerFilter(targetCompilerFilter);
        }
        return targetCompilerFilter;
    }

    private boolean isAppImageEnabled() {
        return android.os.SystemProperties.get("dalvik.vm.appimageformat", "").length() > 0;
    }

    private int getDexFlags(android.content.pm.ApplicationInfo info, java.lang.String compilerFilter, com.android.server.pm.dex.DexoptOptions options) {
        return getDexFlags((info.flags & 2) != 0, info.getHiddenApiEnforcementPolicy(), info.splitDependencies, info.requestsIsolatedSplitLoading(), compilerFilter, false, options);
    }

    private int getDexFlags(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, java.lang.String compilerFilter, boolean useCloudProfile, com.android.server.pm.dex.DexoptOptions options) {
        return getDexFlags(pkg.isDebuggable(), com.android.server.pm.parsing.pkg.AndroidPackageUtils.getHiddenApiEnforcementPolicy(pkg, pkgSetting), pkg.getSplitDependencies(), pkg.isIsolatedSplitLoading(), compilerFilter, useCloudProfile, options);
    }

    private int getDexFlags(boolean debuggable, int hiddenApiEnforcementPolicy, android.util.SparseArray<int[]> splitDependencies, boolean requestsIsolatedSplitLoading, java.lang.String compilerFilter, boolean useCloudProfile, com.android.server.pm.dex.DexoptOptions options) {
        int hiddenApiFlag;
        boolean isProfileGuidedFilter = dalvik.system.DexFile.isProfileGuidedCompilerFilter(compilerFilter);
        boolean isPublic = !isProfileGuidedFilter || options.isDexoptInstallWithDexMetadata() || useCloudProfile;
        int profileFlag = isProfileGuidedFilter ? 16 : 0;
        if (hiddenApiEnforcementPolicy == 0) {
            hiddenApiFlag = 0;
        } else {
            hiddenApiFlag = 1024;
        }
        int compilationReason = options.getCompilationReason();
        boolean generateCompactDex = true;
        switch (compilationReason) {
            case 0:
            case 1:
            case 2:
            case 3:
                generateCompactDex = false;
                break;
        }
        boolean generateCompactDex2 = this.mPackageDexOptimizerExt.configGenerateCompactDex(compilationReason, generateCompactDex);
        boolean generateAppImage = isProfileGuidedFilter && (splitDependencies == null || !requestsIsolatedSplitLoading) && isAppImageEnabled();
        int dexFlags = (options.isDexoptInstallForRestore() ? 8192 : 0) | (isPublic ? 2 : 0) | (debuggable ? 4 : 0) | profileFlag | (options.isBootComplete() ? 8 : 0) | (options.isDexoptIdleBackgroundJob() ? 512 : 0) | (generateCompactDex2 ? 2048 : 0) | (generateAppImage ? 4096 : 0) | hiddenApiFlag;
        return adjustDexoptFlags(dexFlags);
    }

    private int getDexoptNeeded(java.lang.String packageName, java.lang.String path, java.lang.String isa, java.lang.String compilerFilter, java.lang.String classLoaderContext, int profileAnalysisResult, boolean downgrade, int dexoptFlags, java.lang.String oatDir) throws com.android.server.pm.Installer.LegacyDexoptDisabledException {
        boolean newProfile;
        java.lang.String actualCompilerFilter;
        if (!this.mInstaller.isIsolated()) {
            throw new com.android.server.pm.Installer.LegacyDexoptDisabledException();
        }
        boolean shouldBePublic = (dexoptFlags & 2) != 0;
        boolean isProfileGuidedFilter = (dexoptFlags & 16) != 0;
        boolean newProfile2 = profileAnalysisResult == 1;
        try {
            if (!newProfile2 && isProfileGuidedFilter && shouldBePublic) {
                if (isOdexPrivate(packageName, path, isa, oatDir)) {
                    newProfile = true;
                }
                actualCompilerFilter = compilerFilter;
                if (compilerFilterDependsOnProfiles(compilerFilter) && profileAnalysisResult == 3) {
                    actualCompilerFilter = "verify";
                }
                int dexoptNeeded = dalvik.system.DexFile.getDexOptNeeded(path, isa, actualCompilerFilter, classLoaderContext, newProfile, downgrade);
                return adjustDexoptNeeded(dexoptNeeded);
            }
            if (compilerFilterDependsOnProfiles(compilerFilter)) {
                actualCompilerFilter = "verify";
            }
            int dexoptNeeded2 = dalvik.system.DexFile.getDexOptNeeded(path, isa, actualCompilerFilter, classLoaderContext, newProfile, downgrade);
            return adjustDexoptNeeded(dexoptNeeded2);
        } catch (java.io.IOException ioe) {
            android.util.Slog.w(TAG, "IOException reading apk: " + path, ioe);
            return -1;
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.wtf(TAG, "Unexpected exception when calling dexoptNeeded on " + path, e);
            return -1;
        }
        newProfile = newProfile2;
        actualCompilerFilter = compilerFilter;
    }

    private boolean compilerFilterDependsOnProfiles(java.lang.String compilerFilter) {
        return compilerFilter.endsWith("-profile");
    }

    private boolean isOdexPrivate(java.lang.String packageName, java.lang.String path, java.lang.String isa, java.lang.String oatDir) throws com.android.server.pm.Installer.LegacyDexoptDisabledException {
        throw new com.android.server.pm.Installer.LegacyDexoptDisabledException();
    }

    private int analyseProfiles(com.android.server.pm.pkg.AndroidPackage pkg, int uid, java.lang.String profileName, java.lang.String compilerFilter) throws com.android.server.pm.Installer.LegacyDexoptDisabledException {
        throw new com.android.server.pm.Installer.LegacyDexoptDisabledException();
    }

    private java.lang.String getPackageOatDirIfSupported(com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg) {
        if (!com.android.server.pm.parsing.pkg.AndroidPackageUtils.canHaveOatDir(packageState, pkg)) {
            return null;
        }
        java.io.File codePath = new java.io.File(pkg.getPath());
        if (codePath.isDirectory()) {
            return getOatDir(codePath).getAbsolutePath();
        }
        return null;
    }

    public static java.io.File getOatDir(java.io.File codePath) {
        return new java.io.File(codePath, OAT_DIR_NAME);
    }

    void systemReady() {
        this.mSystemReady = true;
    }

    private java.lang.String printDexoptFlags(int flags) {
        java.util.ArrayList<java.lang.String> flagsList = new java.util.ArrayList<>();
        if ((flags & 8) == 8) {
            flagsList.add("boot_complete");
        }
        if ((flags & 4) == 4) {
            flagsList.add("debuggable");
        }
        if ((flags & 16) == 16) {
            flagsList.add("profile_guided");
        }
        if ((flags & 2) == 2) {
            flagsList.add("public");
        }
        if ((flags & 32) == 32) {
            flagsList.add("secondary");
        }
        if ((flags & 64) == 64) {
            flagsList.add("force");
        }
        if ((flags & 128) == 128) {
            flagsList.add("storage_ce");
        }
        if ((flags & 256) == 256) {
            flagsList.add("storage_de");
        }
        if ((flags & 512) == 512) {
            flagsList.add("idle_background_job");
        }
        if ((flags & 1024) == 1024) {
            flagsList.add("enable_hidden_api_checks");
        }
        return java.lang.String.join(",", flagsList);
    }

    public static class ForcedUpdatePackageDexOptimizer extends com.android.server.pm.PackageDexOptimizer {
        public ForcedUpdatePackageDexOptimizer(com.android.server.pm.Installer installer, com.android.server.pm.PackageManagerTracedLock installLock, android.content.Context context, java.lang.String wakeLockTag) {
            super(installer, installLock, context, wakeLockTag);
        }

        public ForcedUpdatePackageDexOptimizer(com.android.server.pm.PackageDexOptimizer from) {
            super(from);
        }

        @Override // com.android.server.pm.PackageDexOptimizer
        protected int adjustDexoptNeeded(int dexoptNeeded) {
            if (dexoptNeeded == 0) {
                return -3;
            }
            return dexoptNeeded;
        }

        @Override // com.android.server.pm.PackageDexOptimizer
        protected int adjustDexoptFlags(int flags) {
            return flags | 64;
        }
    }

    private com.android.server.pm.Installer getInstallerLI() {
        return this.mInstaller;
    }
}
