package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class InitAppsHelper {
    private final com.android.server.pm.ApexManager mApexManager;
    private int mCachedSystemApps;
    private final java.util.concurrent.ExecutorService mExecutorService;
    private final com.android.server.pm.InstallPackageHelper mInstallPackageHelper;
    private final boolean mIsDeviceUpgrading;
    private final com.android.server.pm.PackageManagerService mPm;
    private int mScanFlags;
    private int mSystemPackagesCount;
    private final int mSystemParseFlags;
    private final java.util.List<com.android.server.pm.ScanPartition> mSystemPartitions;
    private final int mSystemScanFlags;
    private long mSystemScanTime;
    private final android.util.ArrayMap<java.lang.String, java.io.File> mExpectingBetter = new android.util.ArrayMap<>();
    private final java.util.List<java.lang.String> mPossiblyDeletedUpdatedSystemApps = new java.util.ArrayList();
    private final java.util.List<java.lang.String> mStubSystemApps = new java.util.ArrayList();
    private final java.util.List<com.android.server.pm.ScanPartition> mDirsToScanAsSystem = getSystemScanPartitions();

    InitAppsHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.ApexManager apexManager, com.android.server.pm.InstallPackageHelper installPackageHelper, java.util.List<com.android.server.pm.ScanPartition> systemPartitions) {
        this.mPm = pm;
        this.mApexManager = apexManager;
        this.mInstallPackageHelper = installPackageHelper;
        this.mSystemPartitions = systemPartitions;
        this.mIsDeviceUpgrading = this.mPm.isDeviceUpgrading();
        if (this.mIsDeviceUpgrading || this.mPm.isFirstBoot()) {
            this.mScanFlags = 528 | 4096;
        } else {
            this.mScanFlags = 528;
        }
        this.mSystemParseFlags = this.mPm.getDefParseFlags() | 16;
        this.mSystemScanFlags = this.mScanFlags | 65536;
        this.mExecutorService = com.android.server.pm.ParallelPackageParser.makeExecutorService();
    }

    private java.util.List<com.android.server.pm.ScanPartition> getSystemScanPartitions() {
        java.util.List<com.android.server.pm.ScanPartition> scanPartitions = new java.util.ArrayList<>();
        scanPartitions.addAll(this.mSystemPartitions);
        scanPartitions.addAll(getApexScanPartitions());
        android.util.Slog.d("PackageManager", "Directories scanned as system partitions: " + scanPartitions);
        return scanPartitions;
    }

    private java.util.List<com.android.server.pm.ScanPartition> getApexScanPartitions() {
        java.util.List<com.android.server.pm.ScanPartition> scanPartitions = new java.util.ArrayList<>();
        java.util.List<com.android.server.pm.ApexManager.ActiveApexInfo> activeApexInfos = this.mApexManager.getActiveApexInfos();
        for (int i = 0; i < activeApexInfos.size(); i++) {
            com.android.server.pm.ScanPartition scanPartition = resolveApexToScanPartition(activeApexInfos.get(i));
            if (scanPartition != null) {
                scanPartitions.add(scanPartition);
            }
        }
        return scanPartitions;
    }

    private static com.android.server.pm.ScanPartition resolveApexToScanPartition(com.android.server.pm.ApexManager.ActiveApexInfo apexInfo) {
        int size = com.android.server.pm.PackageManagerService.SYSTEM_PARTITIONS.size();
        for (int i = 0; i < size; i++) {
            com.android.server.pm.ScanPartition sp = com.android.server.pm.PackageManagerService.SYSTEM_PARTITIONS.get(i);
            if (apexInfo.preInstalledApexPath.getAbsolutePath().equals(sp.getFolder().getAbsolutePath()) || apexInfo.preInstalledApexPath.getAbsolutePath().startsWith(sp.getFolder().getAbsolutePath() + java.io.File.separator)) {
                return new com.android.server.pm.ScanPartition(apexInfo.apexDirectory, sp, apexInfo);
            }
        }
        return null;
    }

    private java.util.List<com.android.server.pm.ApexManager.ScanResult> scanApexPackagesTraced(com.android.internal.pm.parsing.PackageParser2 packageParser) {
        android.os.Trace.traceBegin(262144L, "scanApexPackages");
        try {
            return this.mInstallPackageHelper.scanApexPackages(this.mApexManager.getAllApexInfos(), this.mSystemParseFlags, this.mSystemScanFlags, packageParser, this.mExecutorService);
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    public com.android.internal.content.om.OverlayConfig initSystemApps(com.android.internal.pm.parsing.PackageParser2 packageParser, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> packageSettings, int[] userIds, long startTime) {
        java.util.List<com.android.server.pm.ApexManager.ScanResult> apexScanResults = scanApexPackagesTraced(packageParser);
        this.mApexManager.notifyScanResult(apexScanResults);
        scanSystemDirs(packageParser, this.mExecutorService);
        final android.util.ArrayMap<java.lang.String, java.io.File> apkInApexPreInstalledPaths = new android.util.ArrayMap<>();
        for (com.android.server.pm.ApexManager.ActiveApexInfo apexInfo : this.mApexManager.getActiveApexInfos()) {
            java.lang.String apexPackageName = this.mApexManager.getActivePackageNameForApexModuleName(apexInfo.apexModuleName);
            for (java.lang.String packageName : this.mApexManager.getApksInApex(apexPackageName)) {
                apkInApexPreInstalledPaths.put(packageName, apexInfo.preInstalledApexPath);
            }
        }
        com.android.internal.content.om.OverlayConfig overlayConfig = com.android.internal.content.om.OverlayConfig.initializeSystemInstance(new com.android.internal.content.om.OverlayConfig.PackageProvider() { // from class: com.android.server.pm.InitAppsHelper$$ExternalSyntheticLambda1
            public final void forEachPackage(com.android.internal.util.function.TriConsumer triConsumer) {
                this.f$0.lambda$initSystemApps$1(apkInApexPreInstalledPaths, triConsumer);
            }
        });
        this.mPm.mPackageManagerServiceExt.beforeCheckSystemAppScannedInConstructor();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                updateStubSystemAppsList(this.mStubSystemApps);
                this.mInstallPackageHelper.prepareSystemPackageCleanUp(packageSettings, this.mPossiblyDeletedUpdatedSystemApps, this.mExpectingBetter, userIds);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mPm.mPackageManagerServiceExt.afterCheckSystemAppScannedInConstructor();
        logSystemAppsScanningTime(startTime);
        return overlayConfig;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initSystemApps$1(final android.util.ArrayMap apkInApexPreInstalledPaths, final com.android.internal.util.function.TriConsumer consumer) {
        this.mPm.forEachPackageState(this.mPm.snapshotComputer(), new java.util.function.Consumer() { // from class: com.android.server.pm.InitAppsHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.InitAppsHelper.lambda$initSystemApps$0(consumer, apkInApexPreInstalledPaths, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$initSystemApps$0(com.android.internal.util.function.TriConsumer consumer, android.util.ArrayMap apkInApexPreInstalledPaths, com.android.server.pm.pkg.PackageStateInternal packageState) {
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = packageState.getPkg();
        if (pkg != null) {
            consumer.accept(pkg, java.lang.Boolean.valueOf(packageState.isSystem()), (java.io.File) apkInApexPreInstalledPaths.get(pkg.getPackageName()));
        }
    }

    private void logSystemAppsScanningTime(long startTime) {
        this.mCachedSystemApps = com.android.server.pm.parsing.PackageCacher.sCachedPackageReadCount.get();
        this.mPm.mSettings.pruneSharedUsersLPw();
        this.mSystemScanTime = android.os.SystemClock.uptimeMillis() - startTime;
        this.mSystemPackagesCount = this.mPm.mPackages.size();
        android.util.Slog.i("PackageManager", "Finished scanning system apps. Time: " + this.mSystemScanTime + " ms, packageCount: " + this.mSystemPackagesCount + " , timePerPackage: " + (this.mSystemPackagesCount == 0 ? 0L : this.mSystemScanTime / ((long) this.mSystemPackagesCount)) + " , cached: " + this.mCachedSystemApps);
        if (this.mIsDeviceUpgrading && this.mSystemPackagesCount > 0) {
            com.android.internal.util.FrameworkStatsLog.write(239, 15, this.mSystemScanTime / ((long) this.mSystemPackagesCount));
        }
    }

    void fixInstalledAppDirMode() {
        try {
            java.nio.file.DirectoryStream<java.nio.file.Path> files = java.nio.file.Files.newDirectoryStream(this.mPm.getAppInstallDir().toPath());
            try {
                files.forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.InitAppsHelper$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.pm.InitAppsHelper.lambda$fixInstalledAppDirMode$2((java.nio.file.Path) obj);
                    }
                });
                if (files != null) {
                    files.close();
                }
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w("PackageManager", "Failed to walk the app install directory to fix the modes", e);
        }
    }

    static /* synthetic */ void lambda$fixInstalledAppDirMode$2(java.nio.file.Path dir) {
        try {
            android.system.Os.chmod(dir.toString(), 505);
        } catch (android.system.ErrnoException e) {
            android.util.Slog.w("PackageManager", "Failed to fix an installed app dir mode", e);
        }
    }

    public void initNonSystemApps(com.android.internal.pm.parsing.PackageParser2 packageParser, int[] userIds, long startTime) {
        android.util.EventLog.writeEvent(3080, android.os.SystemClock.uptimeMillis());
        if ((this.mScanFlags & 4096) == 4096) {
            fixInstalledAppDirMode();
        }
        if (android.os.Build.MTK_64BIT_ONLY_HBT_SUPPORT > 0) {
            ((com.android.server.pm.IHbtUtilSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IHbtUtilSocExt.class).create()).translatorBeforeScan();
        } else {
            this.mPm.mPackageManagerServiceExt.translatorBeforeScan();
        }
        this.mPm.mPackageManagerServiceExt.beforeScanDataDirInConstructor();
        this.mScanFlags = this.mPm.mPackageManagerServiceExt.adjustScanFlagsForDataDir(this.mScanFlags);
        this.mPm.mPackageManagerServiceExt.customScanRemovableDir(0, this.mScanFlags | 128, packageParser, this.mExecutorService, null);
        scanDirTracedLI(this.mPm.getAppInstallDir(), 0, this.mScanFlags | 128, packageParser, this.mExecutorService, null);
        this.mPm.mPackageManagerServiceExt.afterScanDataDirInConstructor();
        java.util.List<java.lang.Runnable> unfinishedTasks = this.mExecutorService.shutdownNow();
        if (!unfinishedTasks.isEmpty()) {
            throw new java.lang.IllegalStateException("Not all tasks finished before calling close: " + unfinishedTasks);
        }
        fixSystemPackages(userIds);
        logNonSystemAppScanningTime(startTime);
        this.mExpectingBetter.clear();
        this.mPm.mPackageManagerServiceExt.onPackagePrepareFinishedInConstructor();
        if (android.os.Build.MTK_64BIT_ONLY_HBT_SUPPORT > 0) {
            ((com.android.server.pm.IHbtUtilSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IHbtUtilSocExt.class).create()).translatorFinishedScan();
        } else {
            this.mPm.mPackageManagerServiceExt.translatorFinishedScan();
        }
        this.mPm.mSettings.pruneRenamedPackagesLPw();
    }

    private void fixSystemPackages(int[] userIds) {
        this.mInstallPackageHelper.cleanupDisabledPackageSettings(this.mPossiblyDeletedUpdatedSystemApps, userIds, this.mScanFlags);
        this.mInstallPackageHelper.checkExistingBetterPackages(this.mExpectingBetter, this.mStubSystemApps, this.mSystemScanFlags, this.mSystemParseFlags);
        this.mPm.mPackageManagerServiceExt.beforeInstallSystemStubPackagesInConstructor();
        this.mInstallPackageHelper.installSystemStubPackages(this.mStubSystemApps, this.mScanFlags);
    }

    private void logNonSystemAppScanningTime(long startTime) {
        int cachedNonSystemApps = com.android.server.pm.parsing.PackageCacher.sCachedPackageReadCount.get() - this.mCachedSystemApps;
        long dataScanTime = (android.os.SystemClock.uptimeMillis() - this.mSystemScanTime) - startTime;
        int dataPackagesCount = this.mPm.mPackages.size() - this.mSystemPackagesCount;
        android.util.Slog.i("PackageManager", "Finished scanning non-system apps. Time: " + dataScanTime + " ms, packageCount: " + dataPackagesCount + " , timePerPackage: " + (dataPackagesCount == 0 ? 0L : dataScanTime / ((long) dataPackagesCount)) + " , cached: " + cachedNonSystemApps);
        if (this.mIsDeviceUpgrading && dataPackagesCount > 0) {
            com.android.internal.util.FrameworkStatsLog.write(239, 14, dataScanTime / ((long) dataPackagesCount));
        }
    }

    private void scanSystemDirs(com.android.internal.pm.parsing.PackageParser2 packageParser, java.util.concurrent.ExecutorService executorService) {
        java.io.File frameworkDir = new java.io.File(android.os.Environment.getRootDirectory(), "framework");
        for (int i = this.mDirsToScanAsSystem.size() - 1; i >= 0; i--) {
            com.android.server.pm.ScanPartition partition = this.mDirsToScanAsSystem.get(i);
            if (partition.getOverlayFolder() != null) {
                scanDirTracedLI(partition.getOverlayFolder(), this.mSystemParseFlags, partition.scanFlag | this.mSystemScanFlags, packageParser, executorService, partition.apexInfo);
            }
        }
        scanDirTracedLI(frameworkDir, this.mSystemParseFlags, this.mSystemScanFlags | 1 | 131072, packageParser, executorService, null);
        if (!this.mPm.mPackages.containsKey(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME)) {
            throw new java.lang.IllegalStateException("Failed to load frameworks package; check log for warnings");
        }
        this.mPm.mPackageManagerServiceExt.afterFrameworksPackageScannedInConstructor(this.mSystemParseFlags, this.mSystemScanFlags, packageParser, executorService);
        int size = this.mDirsToScanAsSystem.size();
        for (int i2 = 0; i2 < size; i2++) {
            com.android.server.pm.ScanPartition partition2 = this.mDirsToScanAsSystem.get(i2);
            if (partition2.getPrivAppFolder() != null) {
                scanDirTracedLI(partition2.getPrivAppFolder(), this.mSystemParseFlags, partition2.scanFlag | this.mSystemScanFlags | 131072, packageParser, executorService, partition2.apexInfo);
            }
            scanDirTracedLI(partition2.getAppFolder(), this.mSystemParseFlags, partition2.scanFlag | this.mSystemScanFlags, packageParser, executorService, partition2.apexInfo);
        }
    }

    private void updateStubSystemAppsList(java.util.List<java.lang.String> stubSystemApps) {
        int numPackages = this.mPm.mPackages.size();
        for (int index = 0; index < numPackages; index++) {
            com.android.server.pm.pkg.AndroidPackage pkg = this.mPm.mPackages.valueAt(index);
            if (pkg.isStub()) {
                stubSystemApps.add(pkg.getPackageName());
            }
        }
    }

    public void scanDirTracedLI(java.io.File scanDir, int parseFlags, int scanFlags, com.android.internal.pm.parsing.PackageParser2 packageParser, java.util.concurrent.ExecutorService executorService, com.android.server.pm.ApexManager.ActiveApexInfo apexInfo) {
        int parseFlags2;
        android.os.Trace.traceBegin(262144L, "scanDir [" + scanDir.getAbsolutePath() + "]");
        if ((scanFlags & 8388608) == 0) {
            parseFlags2 = parseFlags;
        } else {
            parseFlags2 = parseFlags | 512;
        }
        try {
            if (this.mPm.mPackageManagerServiceExt.shouldUseCustomScanDirLI()) {
                this.mPm.mPackageManagerServiceExt.customScanDirLI(scanDir, parseFlags2, scanFlags, 0L, packageParser, executorService, apexInfo);
            } else {
                this.mInstallPackageHelper.installPackagesFromDir(scanDir, parseFlags2, scanFlags, packageParser, executorService, apexInfo);
            }
        } finally {
            android.os.Trace.traceEnd(262144L);
        }
    }

    public boolean isExpectingBetter(java.lang.String packageName) {
        return this.mExpectingBetter.containsKey(packageName);
    }

    public java.util.List<com.android.server.pm.ScanPartition> getDirsToScanAsSystem() {
        return this.mDirsToScanAsSystem;
    }

    public int getSystemScanFlags() {
        return this.mSystemScanFlags;
    }
}
