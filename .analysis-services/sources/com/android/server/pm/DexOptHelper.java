package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class DexOptHelper {
    private static final long SEVEN_DAYS_IN_MILLISECONDS = 604800000;
    private static boolean sArtManagerLocalIsInitialized = false;
    private volatile long mBootDexoptStartTime;
    private final com.android.server.pm.PackageManagerService mPm;

    DexOptHelper(com.android.server.pm.PackageManagerService pm) {
        this.mPm = pm;
    }

    private static java.lang.String getPrebuildProfilePath(com.android.server.pm.pkg.AndroidPackage pkg) {
        return pkg.getBaseApkPath() + ".prof";
    }

    public void performPackageDexOptUpgradeIfNeeded() {
        int reason;
        com.android.server.pm.PackageManagerServiceUtils.enforceSystemOrRoot("Only the system can request package update");
        if (hasBcpApexesChanged()) {
            android.os.SystemProperties.set("sys.bcp_apex_changed", "1");
        }
        if (this.mPm.isFirstBoot()) {
            reason = 0;
        } else if (this.mPm.isDeviceUpgrading()) {
            reason = 1;
        } else if (hasBcpApexesChanged()) {
            reason = 13;
        } else {
            return;
        }
        android.util.Log.i("PackageManager", "Starting boot dexopt for reason " + com.android.server.pm.dex.DexoptOptions.convertToArtServiceDexoptReason(reason));
        long startTime = java.lang.System.nanoTime();
        java.util.concurrent.ExecutorService progressCallbackExecutor = (java.util.concurrent.ExecutorService) this.mPm.mPackageManagerServiceExt.beforeOnBootUseArtService().first;
        java.util.function.Consumer<com.android.server.art.model.OperationProgress> progressCallback = (java.util.function.Consumer) this.mPm.mPackageManagerServiceExt.beforeOnBootUseArtService().second;
        this.mPm.mPackageManagerServiceExt.beforeOnBoot(reason);
        this.mBootDexoptStartTime = startTime;
        getArtManagerLocal().onBoot(com.android.server.pm.dex.DexoptOptions.convertToArtServiceDexoptReason(reason), progressCallbackExecutor, progressCallback);
        this.mPm.mPackageManagerServiceExt.afterOnBootUseArtService(progressCallbackExecutor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportBootDexopt(long startTime, int numDexopted, int numSkipped, int numFailed) {
        int elapsedTimeSeconds = (int) java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(java.lang.System.nanoTime() - startTime);
        com.android.server.pm.Computer newSnapshot = this.mPm.snapshotComputer();
        com.android.internal.logging.MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_dexopted", numDexopted);
        com.android.internal.logging.MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_skipped", numSkipped);
        com.android.internal.logging.MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_failed", numFailed);
        com.android.internal.logging.MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_total", getOptimizablePackages(newSnapshot).size());
        com.android.internal.logging.MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_time_s", elapsedTimeSeconds);
    }

    public java.util.List<java.lang.String> getOptimizablePackages(com.android.server.pm.Computer snapshot) {
        final java.util.ArrayList<java.lang.String> pkgs = new java.util.ArrayList<>();
        this.mPm.forEachPackageState(snapshot, new java.util.function.Consumer() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getOptimizablePackages$0(pkgs, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        });
        return pkgs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOptimizablePackages$0(java.util.ArrayList pkgs, com.android.server.pm.pkg.PackageStateInternal packageState) {
        com.android.server.pm.pkg.AndroidPackage pkg = packageState.getPkg();
        if (pkg != null && this.mPm.mPackageDexOptimizer.canOptimizePackage(pkg)) {
            pkgs.add(packageState.getPackageName());
        }
    }

    boolean performDexOpt(com.android.server.pm.dex.DexoptOptions options) {
        int dexoptStatus;
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null || snapshot.isInstantApp(options.getPackageName(), android.os.UserHandle.getCallingUserId())) {
            return false;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = snapshot.getPackage(options.getPackageName());
        if (pkg != null && pkg.isApex()) {
            return true;
        }
        if (options.isDexoptOnlySecondaryDex()) {
            dexoptStatus = performDexOptWithArtService(options, 0);
        } else {
            dexoptStatus = performDexOptWithStatus(options);
        }
        return dexoptStatus != -1;
    }

    int performDexOptWithStatus(com.android.server.pm.dex.DexoptOptions options) {
        return performDexOptTraced(options);
    }

    private int performDexOptTraced(com.android.server.pm.dex.DexoptOptions options) {
        ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("PMS:performDexOpt:" + options.getPackageName());
        android.os.Trace.traceBegin(16384L, "dexopt");
        try {
            return performDexOptInternal(options);
        } finally {
            android.os.Trace.traceEnd(16384L);
        }
    }

    private int performDexOptInternal(com.android.server.pm.dex.DexoptOptions options) {
        return performDexOptWithArtService(options, 4);
    }

    private int performDexOptWithArtService(com.android.server.pm.dex.DexoptOptions options, int extraFlags) {
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
            try {
                com.android.server.pm.pkg.PackageState ops = snapshot.getPackageState(options.getPackageName());
                if (ops == null) {
                    if (snapshot != null) {
                        snapshot.close();
                    }
                    return -1;
                }
                com.android.server.pm.pkg.AndroidPackage oap = ops.getAndroidPackage();
                if (oap == null) {
                    if (snapshot != null) {
                        snapshot.close();
                    }
                    return -1;
                }
                com.android.server.art.model.DexoptParams params = options.convertToDexoptParams(extraFlags);
                int iDexoptInPerformDexOptWithArtService = this.mPm.mPackageManagerServiceExt.dexoptInPerformDexOptWithArtService(snapshot, options.getPackageName(), params);
                if (snapshot != null) {
                    snapshot.close();
                }
                return iDexoptInPerformDexOptWithArtService;
            } finally {
            }
        } catch (java.lang.IllegalStateException e) {
            if (android.os.Build.OPLUS_64BIT_ONLY_CHIP) {
            }
            throw e;
        }
        if (android.os.Build.OPLUS_64BIT_ONLY_CHIP || !e.getMessage().contains("Unsupported isa 'arm'")) {
            throw e;
        }
        android.util.Slog.w("PackageManager", "Dexopt with art service is conflict with hbt_translator");
        return -1;
    }

    public boolean performDexOptMode(com.android.server.pm.Computer snapshot, java.lang.String packageName, java.lang.String targetCompilerFilter, boolean force, boolean bootComplete, java.lang.String splitName) throws java.lang.Throwable {
        int flags;
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell() && !isCallerInstallerForPackage(snapshot, packageName)) {
            throw new java.lang.SecurityException("performDexOptMode");
        }
        int flags2 = (bootComplete ? 4 : 0) | (force ? 2 : 0);
        if (!dalvik.system.DexFile.isProfileGuidedCompilerFilter(targetCompilerFilter)) {
            flags = flags2;
        } else {
            flags = flags2 | 1;
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            try {
                boolean zPerformDexOpt = performDexOpt(new com.android.server.pm.dex.DexoptOptions(packageName, 12, targetCompilerFilter, splitName, flags));
                android.os.Binder.restoreCallingIdentity(callingId);
                return zPerformDexOpt;
            } catch (java.lang.Throwable th) {
                th = th;
                android.os.Binder.restoreCallingIdentity(callingId);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    private boolean isCallerInstallerForPackage(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
        if (packageState == null) {
            return false;
        }
        com.android.server.pm.InstallSource installSource = packageState.getInstallSource();
        com.android.server.pm.pkg.PackageStateInternal installerPackageState = snapshot.getPackageStateInternal(installSource.mInstallerPackageName);
        return installerPackageState != null && installerPackageState.getPkg().getUid() == android.os.Binder.getCallingUid();
    }

    public boolean performDexOptSecondary(java.lang.String packageName, java.lang.String compilerFilter, boolean force) {
        if (this.mPm.mPackageManagerServiceExt.interceptPerformDexOptSecondary(packageName, compilerFilter, force)) {
            return false;
        }
        int flags = (force ? 2 : 0) | 13;
        return performDexOpt(new com.android.server.pm.dex.DexoptOptions(packageName, 12, compilerFilter, null, flags));
    }

    public static java.util.List<com.android.server.pm.pkg.PackageStateInternal> getPackagesForDexopt(java.util.Collection<? extends com.android.server.pm.pkg.PackageStateInternal> packages, com.android.server.pm.PackageManagerService packageManagerService) {
        return getPackagesForDexopt(packages, packageManagerService, com.android.server.pm.PackageManagerService.DEBUG_DEXOPT);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<com.android.server.pm.pkg.PackageStateInternal> getPackagesForDexopt(java.util.Collection<? extends com.android.server.pm.pkg.PackageStateInternal> r16, com.android.server.pm.PackageManagerService r17, boolean r18) {
        /*
            Method dump skipped, instruction units count: 279
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.DexOptHelper.getPackagesForDexopt(java.util.Collection, com.android.server.pm.PackageManagerService, boolean):java.util.List");
    }

    static /* synthetic */ boolean lambda$getPackagesForDexopt$5(long cutoffTime, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return pkgSetting.getTransientState().getLatestForegroundPackageUseTimeInMills() >= cutoffTime;
    }

    static /* synthetic */ boolean lambda$getPackagesForDexopt$6(com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return true;
    }

    static /* synthetic */ boolean lambda$getPackagesForDexopt$7(com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        return true;
    }

    private static void applyPackageFilter(com.android.server.pm.Computer snapshot, java.util.function.Predicate<com.android.server.pm.pkg.PackageStateInternal> filter, java.util.Collection<com.android.server.pm.pkg.PackageStateInternal> result, java.util.Collection<com.android.server.pm.pkg.PackageStateInternal> packages, java.util.List<com.android.server.pm.pkg.PackageStateInternal> sortTemp, com.android.server.pm.PackageManagerService packageManagerService) {
        for (com.android.server.pm.pkg.PackageStateInternal pkgSetting : packages) {
            if (filter.test(pkgSetting)) {
                sortTemp.add(pkgSetting);
            }
        }
        sortPackagesByUsageDate(sortTemp, packageManagerService);
        packages.removeAll(sortTemp);
        for (com.android.server.pm.pkg.PackageStateInternal pkgSetting2 : sortTemp) {
            result.add(pkgSetting2);
            java.util.List<com.android.server.pm.pkg.PackageStateInternal> deps = snapshot.findSharedNonSystemLibraries(pkgSetting2);
            if (!deps.isEmpty()) {
                deps.removeAll(result);
                result.addAll(deps);
                packages.removeAll(deps);
            }
        }
        sortTemp.clear();
    }

    private static void sortPackagesByUsageDate(java.util.List<com.android.server.pm.pkg.PackageStateInternal> pkgSettings, com.android.server.pm.PackageManagerService packageManagerService) {
        if (!packageManagerService.isHistoricalPackageUsageAvailable()) {
            return;
        }
        java.util.Collections.sort(pkgSettings, new java.util.Comparator() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda9
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return java.lang.Long.compare(((com.android.server.pm.pkg.PackageStateInternal) obj2).getTransientState().getLatestForegroundPackageUseTimeInMills(), ((com.android.server.pm.pkg.PackageStateInternal) obj).getTransientState().getLatestForegroundPackageUseTimeInMills());
            }
        });
    }

    private static android.util.ArraySet<java.lang.String> getPackageNamesForIntent(android.content.Intent intent, int userId) {
        java.util.List<android.content.pm.ResolveInfo> ris = null;
        try {
            ris = android.app.AppGlobals.getPackageManager().queryIntentReceivers(intent, (java.lang.String) null, 0L, userId).getList();
        } catch (android.os.RemoteException e) {
        }
        android.util.ArraySet<java.lang.String> pkgNames = new android.util.ArraySet<>();
        if (ris != null) {
            for (android.content.pm.ResolveInfo ri : ris) {
                pkgNames.add(ri.activityInfo.packageName);
            }
        }
        return pkgNames;
    }

    public static java.lang.String packagesToString(java.util.List<com.android.server.pm.pkg.PackageStateInternal> pkgSettings) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int index = 0; index < pkgSettings.size(); index++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(pkgSettings.get(index).getPackageName());
        }
        return sb.toString();
    }

    public static void requestCopyPreoptedFiles() {
        if (android.os.SystemProperties.getInt("ro.cp_system_other_odex", 0) == 1) {
            android.os.SystemProperties.set("sys.cppreopt", "requested");
            long timeStart = android.os.SystemClock.uptimeMillis();
            long timeEnd = 100000 + timeStart;
            long timeNow = timeStart;
            while (true) {
                if (android.os.SystemProperties.get("sys.cppreopt").equals("finished")) {
                    break;
                }
                try {
                    java.lang.Thread.sleep(100L);
                } catch (java.lang.InterruptedException e) {
                }
                timeNow = android.os.SystemClock.uptimeMillis();
                if (timeNow > timeEnd) {
                    android.os.SystemProperties.set("sys.cppreopt", "timed-out");
                    android.util.Slog.wtf("PackageManager", "cppreopt did not finish!");
                    break;
                }
            }
            android.util.Slog.i("PackageManager", "cppreopts took " + (timeNow - timeStart) + " ms");
        }
    }

    public static void dumpDexoptState(com.android.internal.util.IndentingPrintWriter ipw, java.lang.String packageName) {
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = com.android.server.pm.PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
            try {
                if (packageName != null) {
                    try {
                        getArtManagerLocal().dumpPackage(ipw, snapshot, packageName);
                    } catch (java.lang.IllegalArgumentException e) {
                        ipw.println(e);
                    }
                } else {
                    getArtManagerLocal().dump(ipw, snapshot);
                }
                if (snapshot != null) {
                    snapshot.close();
                }
            } finally {
            }
        } catch (java.lang.IllegalStateException e2) {
            if (android.os.Build.OPLUS_64BIT_ONLY_CHIP && e2.getMessage().contains("Unsupported isa 'arm'")) {
                android.util.Slog.w("PackageManager", "Dexopt with art service is conflict with hbt_translator");
                return;
            }
            throw e2;
        }
    }

    private static java.util.List<java.lang.String> getBcpApexes() {
        java.lang.String bcp = java.lang.System.getenv("BOOTCLASSPATH");
        if (android.text.TextUtils.isEmpty(bcp)) {
            android.util.Log.e("PackageManager", "Unable to get BOOTCLASSPATH");
            return java.util.List.of();
        }
        java.util.ArrayList<java.lang.String> bcpApexes = new java.util.ArrayList<>();
        for (java.lang.String pathStr : bcp.split(":")) {
            java.nio.file.Path path = java.nio.file.Paths.get(pathStr, new java.lang.String[0]);
            if (path.getNameCount() >= 2 && path.getName(0).toString().equals("apex")) {
                bcpApexes.add(path.getName(1).toString());
            }
        }
        return bcpApexes;
    }

    private static boolean hasBcpApexesChanged() {
        java.util.Set<java.lang.String> bcpApexes = new java.util.HashSet<>(getBcpApexes());
        com.android.server.pm.ApexManager apexManager = com.android.server.pm.ApexManager.getInstance();
        for (com.android.server.pm.ApexManager.ActiveApexInfo apexInfo : apexManager.getActiveApexInfos()) {
            if (bcpApexes.contains(apexInfo.apexModuleName) && apexInfo.activeApexChanged) {
                return true;
            }
        }
        return false;
    }

    public static com.android.server.art.DexUseManagerLocal getDexUseManagerLocal() {
        try {
            return (com.android.server.art.DexUseManagerLocal) com.android.server.LocalManagerRegistry.getManagerOrThrow(com.android.server.art.DexUseManagerLocal.class);
        } catch (com.android.server.LocalManagerRegistry.ManagerNotFoundException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private class DexoptDoneHandler implements com.android.server.art.ArtManagerLocal.DexoptDoneCallback {
        private DexoptDoneHandler() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x002d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onDexoptDone(com.android.server.art.model.DexoptResult r17) {
            /*
                Method dump skipped, instruction units count: 432
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.DexOptHelper.DexoptDoneHandler.onDexoptDone(com.android.server.art.model.DexoptResult):void");
        }
    }

    public static void initializeArtManagerLocal(android.content.Context systemContext, com.android.server.pm.PackageManagerService pm) {
        final com.android.server.art.ArtManagerLocal artManager = new com.android.server.art.ArtManagerLocal(systemContext);
        com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0 systemServerInitThreadPool$$ExternalSyntheticLambda0 = new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0();
        com.android.server.pm.DexOptHelper dexOptHelper = pm.getDexOptHelper();
        java.util.Objects.requireNonNull(dexOptHelper);
        artManager.addDexoptDoneCallback(false, systemServerInitThreadPool$$ExternalSyntheticLambda0, new com.android.server.pm.DexOptHelper.DexoptDoneHandler());
        com.android.server.LocalManagerRegistry.addManager(com.android.server.art.ArtManagerLocal.class, artManager);
        sArtManagerLocalIsInitialized = true;
        systemContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.pm.DexOptHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                context.unregisterReceiver(this);
                artManager.scheduleBackgroundDexoptJob();
            }
        }, new android.content.IntentFilter("android.intent.action.LOCKED_BOOT_COMPLETED"));
        com.android.server.pm.DexOptHelper.StagedApexObserver.registerForStagedApexUpdates(artManager);
        pm.mPackageManagerServiceExt.afterInitializeArtManagerLocal(systemContext);
    }

    public static boolean artManagerLocalIsInitialized() {
        return sArtManagerLocalIsInitialized;
    }

    public static com.android.server.art.ArtManagerLocal getArtManagerLocal() {
        try {
            return (com.android.server.art.ArtManagerLocal) com.android.server.LocalManagerRegistry.getManagerOrThrow(com.android.server.art.ArtManagerLocal.class);
        } catch (com.android.server.LocalManagerRegistry.ManagerNotFoundException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private static int convertToDexOptResult(com.android.server.art.model.DexoptResult result) {
        int status = result.getFinalStatus();
        switch (status) {
            case 10:
                return 0;
            case 20:
                return 1;
            case 30:
                return -1;
            case 40:
                return 2;
            default:
                throw new java.lang.IllegalArgumentException("DexoptResult for " + ((com.android.server.art.model.DexoptResult.PackageDexoptResult) result.getPackageDexoptResults().get(0)).getPackageName() + " has unsupported status " + status);
        }
    }

    static com.android.server.pm.dex.DexoptOptions getDexoptOptionsByInstallRequest(com.android.server.pm.InstallRequest installRequest, com.android.server.pm.dex.DexManager dexManager) {
        com.android.server.pm.PackageSetting ps = installRequest.getScannedPackageSetting();
        java.lang.String packageName = ps.getPackageName();
        boolean isBackupOrRestore = installRequest.getInstallReason() == 2 || installRequest.getInstallReason() == 3;
        int dexoptFlags = (isBackupOrRestore ? 2048 : 0) | 1029;
        int compilationReason = dexManager.getCompilationReasonForInstallScenario(installRequest.getInstallScenario());
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
        com.android.server.pm.dex.DexoptOptions options = new com.android.server.pm.dex.DexoptOptions(packageName, compilationReason, dexoptFlags);
        if (installRequest.getDexoptCompilerFilter() != null) {
            return options.overrideCompilerFilter(installRequest.getDexoptCompilerFilter());
        }
        if (pkg != null && pkg.isDebuggable()) {
            return options.overrideCompilerFilter("skip");
        }
        return options;
    }

    static com.android.server.art.model.DexoptResult dexoptPackageUsingArtService(com.android.server.pm.InstallRequest installRequest, com.android.server.pm.dex.DexoptOptions dexoptOptions) {
        com.android.server.pm.PackageSetting ps = installRequest.getScannedPackageSetting();
        java.lang.String packageName = ps.getPackageName();
        com.android.server.pm.PackageManagerLocal packageManagerLocal = (com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.pm.PackageManagerLocal.class);
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = packageManagerLocal.withFilteredSnapshot();
        try {
            boolean ignoreDexoptProfile = (installRequest.getInstallFlags() & 268435456) != 0;
            int extraFlags = ignoreDexoptProfile ? 128 : 0;
            com.android.server.art.model.DexoptParams params = dexoptOptions.convertToDexoptParams(extraFlags);
            com.android.server.art.model.DexoptResult dexOptResult = com.android.server.pm.PackageManagerService.sStaticExt.hookInDexoptPackageUsingArtService(snapshot, packageName, params);
            if (snapshot != null) {
                snapshot.close();
            }
            return dexOptResult;
        } catch (java.lang.Throwable th) {
            if (snapshot != null) {
                try {
                    snapshot.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    static boolean shouldPerformDexopt(com.android.server.pm.InstallRequest installRequest, com.android.server.pm.dex.DexoptOptions dexoptOptions, android.content.Context context) {
        boolean isApex = (installRequest.getScanFlags() & 67108864) != 0;
        boolean instantApp = (installRequest.getScanFlags() & 8192) != 0;
        com.android.server.pm.PackageSetting ps = installRequest.getScannedPackageSetting();
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
        boolean onIncremental = android.os.incremental.IncrementalManager.isIncrementalPath(ps.getPathString());
        boolean performDexOptForRollback = (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection() && installRequest.isRollback() && installRequest.getInstallSource().mInitiatingPackageName.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME)) ? false : true;
        return ((instantApp && android.provider.Settings.Global.getInt(context.getContentResolver(), "instant_app_dexopt_enabled", 0) == 0) || pkg == null || onIncremental || isApex || !performDexOptForRollback) ? false : true;
    }

    private static class StagedApexObserver extends android.content.pm.IStagedApexObserver.Stub {
        private final com.android.server.art.ArtManagerLocal mArtManager;

        static void registerForStagedApexUpdates(com.android.server.art.ArtManagerLocal artManager) {
            android.content.pm.IPackageManagerNative packageNative = android.content.pm.IPackageManagerNative.Stub.asInterface(android.os.ServiceManager.getService("package_native"));
            if (packageNative == null) {
                android.util.Log.e("PackageManager", "No IPackageManagerNative");
                return;
            }
            try {
                packageNative.registerStagedApexObserver(new com.android.server.pm.DexOptHelper.StagedApexObserver(artManager));
            } catch (android.os.RemoteException e) {
                android.util.Log.e("PackageManager", "Failed to register staged apex observer", e);
            }
        }

        private StagedApexObserver(com.android.server.art.ArtManagerLocal artManager) {
            this.mArtManager = artManager;
        }

        public void onApexStaged(android.content.pm.ApexStagedEvent event) {
            this.mArtManager.onApexStaged(event.stagedApexModuleNames);
        }
    }
}
