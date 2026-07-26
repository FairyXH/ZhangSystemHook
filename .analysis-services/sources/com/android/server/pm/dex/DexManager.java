package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public class DexManager {
    private static final int DEX_SEARCH_FOUND_PRIMARY = 1;
    private static final int DEX_SEARCH_FOUND_SECONDARY = 3;
    private static final int DEX_SEARCH_FOUND_SPLIT = 2;
    private static final int DEX_SEARCH_NOT_FOUND = 0;
    private static final java.lang.String ISOLATED_PROCESS_PACKAGE_SUFFIX = "..isolated";
    private static final java.lang.String SYSTEM_SERVER_COMPILER_FILTER = "verify";
    private android.os.BatteryManager mBatteryManager;
    private final android.content.Context mContext;
    private final int mCriticalBatteryLevel;
    private final com.android.server.pm.dex.DynamicCodeLogger mDynamicCodeLogger;
    private final java.util.Map<java.lang.String, com.android.server.pm.dex.DexManager.PackageCodeLocations> mPackageCodeLocationsCache;
    private final com.android.server.pm.PackageDexOptimizer mPackageDexOptimizer;
    private final com.android.server.pm.dex.PackageDexUsage mPackageDexUsage;
    private android.content.pm.IPackageManager mPackageManager;
    private android.os.PowerManager mPowerManager;
    private static final java.lang.String TAG = "DexManager";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    public DexManager(android.content.Context context, com.android.server.pm.PackageDexOptimizer pdo, com.android.server.pm.dex.DynamicCodeLogger dynamicCodeLogger) {
        this(context, pdo, dynamicCodeLogger, null);
    }

    public DexManager(android.content.Context context, com.android.server.pm.PackageDexOptimizer pdo, com.android.server.pm.dex.DynamicCodeLogger dynamicCodeLogger, android.content.pm.IPackageManager packageManager) {
        this.mBatteryManager = null;
        this.mPowerManager = null;
        this.mContext = context;
        this.mPackageCodeLocationsCache = new java.util.HashMap();
        this.mPackageDexUsage = new com.android.server.pm.dex.PackageDexUsage();
        this.mPackageDexOptimizer = pdo;
        this.mDynamicCodeLogger = dynamicCodeLogger;
        this.mPackageManager = packageManager;
        if (this.mContext != null) {
            this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
            if (this.mPowerManager == null) {
                android.util.Slog.wtf(TAG, "Power Manager is unavailable at time of Dex Manager start");
            }
            this.mCriticalBatteryLevel = this.mContext.getResources().getInteger(android.R.integer.config_carDockKeepsScreenOn);
            return;
        }
        this.mCriticalBatteryLevel = 0;
    }

    private android.content.pm.IPackageManager getPackageManager() {
        if (this.mPackageManager == null) {
            this.mPackageManager = android.content.pm.IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
        }
        return this.mPackageManager;
    }

    public void notifyDexLoad(android.content.pm.ApplicationInfo loadingAppInfo, java.util.Map<java.lang.String, java.lang.String> classLoaderContextMap, java.lang.String loaderIsa, int loaderUserId, boolean loaderIsIsolatedProcess) {
        try {
            notifyDexLoadInternal(loadingAppInfo, classLoaderContextMap, loaderIsa, loaderUserId, loaderIsIsolatedProcess);
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.w(TAG, "Exception while notifying dex load for package " + loadingAppInfo.packageName, e);
        }
    }

    void notifyDexLoadInternal(android.content.pm.ApplicationInfo loadingAppInfo, java.util.Map<java.lang.String, java.lang.String> classLoaderContextMap, java.lang.String loaderIsa, int loaderUserId, boolean loaderIsIsolatedProcess) {
        java.lang.String loadingPackageAmendedName;
        android.content.pm.ApplicationInfo applicationInfo = loadingAppInfo;
        if (classLoaderContextMap == null) {
            return;
        }
        if (classLoaderContextMap.isEmpty()) {
            android.util.Slog.wtf(TAG, "Bad call to notifyDexLoad: class loaders list is empty");
            return;
        }
        if (!com.android.server.pm.PackageManagerServiceUtils.checkISA(loaderIsa)) {
            android.util.Slog.w(TAG, "Loading dex files " + classLoaderContextMap.keySet() + " in unsupported ISA: " + loaderIsa + "?");
            return;
        }
        java.lang.String loadingPackageAmendedName2 = applicationInfo.packageName;
        if (!loaderIsIsolatedProcess) {
            loadingPackageAmendedName = loadingPackageAmendedName2;
        } else {
            loadingPackageAmendedName = loadingPackageAmendedName2 + ISOLATED_PROCESS_PACKAGE_SUFFIX;
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.String> mapping : classLoaderContextMap.entrySet()) {
            java.lang.String dexPath = mapping.getKey();
            com.android.server.pm.dex.DexManager.DexSearchResult searchResult = getDexPackage(applicationInfo, dexPath, loaderUserId);
            if (DEBUG) {
                android.util.Slog.i(TAG, loadingPackageAmendedName + " loads from " + searchResult + " : " + loaderUserId + " : " + dexPath);
            }
            if (searchResult.mOutcome != 0) {
                boolean z = true;
                boolean isUsedByOtherApps = !loadingPackageAmendedName.equals(searchResult.mOwningPackageName);
                if (searchResult.mOutcome != 1 && searchResult.mOutcome != 2) {
                    z = false;
                }
                boolean primaryOrSplit = z;
                if (!primaryOrSplit || isUsedByOtherApps || isPlatformPackage(searchResult.mOwningPackageName)) {
                    if (!primaryOrSplit) {
                        this.mDynamicCodeLogger.recordDex(loaderUserId, dexPath, searchResult.mOwningPackageName, applicationInfo.packageName);
                    }
                    java.lang.String classLoaderContext = mapping.getValue();
                    boolean overwriteCLC = isPlatformPackage(searchResult.mOwningPackageName);
                    if (classLoaderContext != null && dalvik.system.VMRuntime.isValidClassLoaderContext(classLoaderContext) && this.mPackageDexUsage.record(searchResult.mOwningPackageName, dexPath, loaderUserId, loaderIsa, primaryOrSplit, loadingPackageAmendedName, classLoaderContext, overwriteCLC)) {
                        this.mPackageDexUsage.maybeWriteAsync();
                    }
                }
            } else if (DEBUG) {
                android.util.Slog.i(TAG, "Could not find owning package for dex file: " + dexPath);
            }
            applicationInfo = loadingAppInfo;
        }
    }

    private boolean isSystemServerDexPathSupportedForOdex(java.lang.String dexPath) {
        java.util.ArrayList<android.content.pm.PackagePartitions.SystemPartition> partitions = android.content.pm.PackagePartitions.getOrderedPartitions(java.util.function.Function.identity());
        if (dexPath.startsWith("/apex/")) {
            return true;
        }
        for (int i = 0; i < partitions.size(); i++) {
            if (partitions.get(i).containsPath(dexPath)) {
                return true;
            }
        }
        return false;
    }

    public void load(java.util.Map<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> existingPackages) {
        try {
            loadInternal(existingPackages);
        } catch (java.lang.RuntimeException e) {
            this.mPackageDexUsage.clear();
            android.util.Slog.w(TAG, "Exception while loading. Starting with a fresh state.", e);
        }
    }

    public void notifyPackageInstalled(android.content.pm.PackageInfo pi, int userId) {
        if (userId == -1) {
            throw new java.lang.IllegalArgumentException("notifyPackageInstalled called with USER_ALL");
        }
        cachePackageInfo(pi, userId);
    }

    public void notifyPackageUpdated(java.lang.String packageName, java.lang.String baseCodePath, java.lang.String[] splitCodePaths) {
        cachePackageCodeLocation(packageName, baseCodePath, splitCodePaths, null, -1);
        if (this.mPackageDexUsage.clearUsedByOtherApps(packageName)) {
            this.mPackageDexUsage.maybeWriteAsync();
        }
    }

    public void notifyPackageDataDestroyed(java.lang.String packageName, int userId) {
        if (userId == -1) {
            if (this.mPackageDexUsage.removePackage(packageName)) {
                this.mPackageDexUsage.maybeWriteAsync();
            }
        } else if (this.mPackageDexUsage.removeUserPackage(packageName, userId)) {
            this.mPackageDexUsage.maybeWriteAsync();
        }
    }

    private void cachePackageInfo(android.content.pm.PackageInfo pi, int userId) {
        android.content.pm.ApplicationInfo ai = pi.applicationInfo;
        java.lang.String[] dataDirs = {ai.dataDir, ai.deviceProtectedDataDir, ai.credentialProtectedDataDir};
        cachePackageCodeLocation(pi.packageName, ai.sourceDir, ai.splitSourceDirs, dataDirs, userId);
    }

    private void cachePackageCodeLocation(java.lang.String packageName, java.lang.String baseCodePath, java.lang.String[] splitCodePaths, java.lang.String[] dataDirs, int userId) {
        synchronized (this.mPackageCodeLocationsCache) {
            com.android.server.pm.dex.DexManager.PackageCodeLocations pcl = (com.android.server.pm.dex.DexManager.PackageCodeLocations) putIfAbsent(this.mPackageCodeLocationsCache, packageName, new com.android.server.pm.dex.DexManager.PackageCodeLocations(packageName, baseCodePath, splitCodePaths));
            pcl.updateCodeLocation(baseCodePath, splitCodePaths);
            if (dataDirs != null) {
                for (java.lang.String dataDir : dataDirs) {
                    if (dataDir != null) {
                        pcl.mergeAppDataDirs(dataDir, userId);
                    }
                }
            }
        }
    }

    private void loadInternal(java.util.Map<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> existingPackages) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.Integer>> packageToUsersMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> packageToCodePaths = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> entry : existingPackages.entrySet()) {
            java.util.List<android.content.pm.PackageInfo> packageInfoList = entry.getValue();
            int userId = entry.getKey().intValue();
            for (android.content.pm.PackageInfo pi : packageInfoList) {
                cachePackageInfo(pi, userId);
                java.util.Set<java.lang.Integer> users = (java.util.Set) putIfAbsent(packageToUsersMap, pi.packageName, new java.util.HashSet());
                users.add(java.lang.Integer.valueOf(userId));
                java.util.Set<java.lang.String> codePaths = (java.util.Set) putIfAbsent(packageToCodePaths, pi.packageName, new java.util.HashSet());
                codePaths.add(pi.applicationInfo.sourceDir);
                if (pi.applicationInfo.splitSourceDirs != null) {
                    java.util.Collections.addAll(codePaths, pi.applicationInfo.splitSourceDirs);
                }
            }
        }
        try {
            this.mPackageDexUsage.read();
            java.util.List<java.lang.String> packagesToKeepDataAbout = new java.util.ArrayList<>();
            this.mPackageDexUsage.syncData(packageToUsersMap, packageToCodePaths, packagesToKeepDataAbout);
        } catch (java.lang.RuntimeException e) {
            this.mPackageDexUsage.clear();
            android.util.Slog.w(TAG, "Exception while loading package dex usage. Starting with a fresh state.", e);
        }
    }

    public com.android.server.pm.dex.PackageDexUsage.PackageUseInfo getPackageUseInfoOrDefault(java.lang.String packageName) {
        com.android.server.pm.dex.PackageDexUsage.PackageUseInfo useInfo = this.mPackageDexUsage.getPackageUseInfo(packageName);
        return useInfo == null ? new com.android.server.pm.dex.PackageDexUsage.PackageUseInfo(packageName) : useInfo;
    }

    boolean hasInfoOnPackage(java.lang.String packageName) {
        return this.mPackageDexUsage.getPackageUseInfo(packageName) != null;
    }

    private com.android.server.pm.PackageDexOptimizer getPackageDexOptimizer(com.android.server.pm.dex.DexoptOptions options) {
        if (options.isForce()) {
            return new com.android.server.pm.PackageDexOptimizer.ForcedUpdatePackageDexOptimizer(this.mPackageDexOptimizer);
        }
        return this.mPackageDexOptimizer;
    }

    public java.util.Set<java.lang.String> getAllPackagesWithSecondaryDexFiles() {
        return this.mPackageDexUsage.getAllPackagesWithSecondaryDexFiles();
    }

    private com.android.server.pm.dex.DexManager.DexSearchResult getDexPackage(android.content.pm.ApplicationInfo loadingAppInfo, java.lang.String dexPath, int userId) {
        com.android.server.pm.dex.DexManager.PackageCodeLocations loadingPackageCodeLocations = new com.android.server.pm.dex.DexManager.PackageCodeLocations(loadingAppInfo, userId);
        int outcome = loadingPackageCodeLocations.searchDex(dexPath, userId);
        if (outcome != 0) {
            return new com.android.server.pm.dex.DexManager.DexSearchResult(loadingPackageCodeLocations.mPackageName, outcome);
        }
        synchronized (this.mPackageCodeLocationsCache) {
            for (com.android.server.pm.dex.DexManager.PackageCodeLocations pcl : this.mPackageCodeLocationsCache.values()) {
                int outcome2 = pcl.searchDex(dexPath, userId);
                if (outcome2 != 0) {
                    return new com.android.server.pm.dex.DexManager.DexSearchResult(pcl.mPackageName, outcome2);
                }
            }
            if (isPlatformPackage(loadingAppInfo.packageName)) {
                if (isSystemServerDexPathSupportedForOdex(dexPath)) {
                    return new com.android.server.pm.dex.DexManager.DexSearchResult(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 3);
                }
                android.util.Slog.wtf(TAG, "System server loads dex files outside paths supported for odex: " + dexPath);
            }
            if (DEBUG) {
                try {
                    java.lang.String dexPathReal = com.android.server.pm.PackageManagerServiceUtils.realpath(new java.io.File(dexPath));
                    if (!dexPath.equals(dexPathReal)) {
                        android.util.Slog.d(TAG, "Dex loaded with symlink. dexPath=" + dexPath + " dexPathReal=" + dexPathReal);
                    }
                } catch (java.io.IOException e) {
                }
            }
            return new com.android.server.pm.dex.DexManager.DexSearchResult(null, 0);
        }
    }

    private static boolean isPlatformPackage(java.lang.String packageName) {
        return com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> V putIfAbsent(java.util.Map<K, V> map, K key, V newValue) {
        V existingValue = map.putIfAbsent(key, newValue);
        return existingValue == null ? newValue : existingValue;
    }

    public void writePackageDexUsageNow() {
        this.mPackageDexUsage.writeNow();
    }

    public static boolean auditUncompressedDexInApk(java.lang.String fileName) {
        android.util.jar.StrictJarFile jarFile = null;
        try {
            try {
                jarFile = new android.util.jar.StrictJarFile(fileName, false, false);
                boolean allCorrect = true;
                for (java.util.zip.ZipEntry entry : jarFile) {
                    if (entry.getName().endsWith(".dex")) {
                        if (entry.getMethod() != 0) {
                            allCorrect = false;
                            android.util.Slog.w(TAG, "APK " + fileName + " has compressed dex code " + entry.getName());
                        } else if ((entry.getDataOffset() & 3) != 0) {
                            allCorrect = false;
                            android.util.Slog.w(TAG, "APK " + fileName + " has unaligned dex code " + entry.getName());
                        }
                    }
                }
                try {
                    jarFile.close();
                } catch (java.io.IOException e) {
                }
                return allCorrect;
            } catch (java.io.IOException e2) {
                android.util.Slog.wtf(TAG, "Error when parsing APK " + fileName);
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (java.io.IOException e3) {
                    }
                }
                return false;
            }
        } catch (java.lang.Throwable th) {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (java.io.IOException e4) {
                }
            }
            throw th;
        }
    }

    public int getCompilationReasonForInstallScenario(int installScenario) {
        boolean resourcesAreCritical = areBatteryThermalOrMemoryCritical();
        switch (installScenario) {
            case 0:
                return 3;
            case 1:
                return 4;
            case 2:
                if (resourcesAreCritical) {
                    return 7;
                }
                return 5;
            case 3:
                if (resourcesAreCritical) {
                    return 8;
                }
                return 6;
            default:
                throw new java.lang.IllegalArgumentException("Invalid installation scenario");
        }
    }

    private android.os.BatteryManager getBatteryManager() {
        if (this.mBatteryManager == null && this.mContext != null) {
            this.mBatteryManager = (android.os.BatteryManager) this.mContext.getSystemService(android.os.BatteryManager.class);
        }
        return this.mBatteryManager;
    }

    private boolean areBatteryThermalOrMemoryCritical() {
        android.os.BatteryManager batteryManager = getBatteryManager();
        return (batteryManager != null && batteryManager.getIntProperty(6) == 3 && batteryManager.getIntProperty(4) <= this.mCriticalBatteryLevel) || (this.mPowerManager != null && this.mPowerManager.getCurrentThermalStatus() >= 3);
    }

    public static class RegisterDexModuleResult {
        public final java.lang.String message;
        public final boolean success;

        public RegisterDexModuleResult() {
            this(false, null);
        }

        public RegisterDexModuleResult(boolean success, java.lang.String message) {
            this.success = success;
            this.message = message;
        }
    }

    private static class PackageCodeLocations {
        private final java.util.Map<java.lang.Integer, java.util.Set<java.lang.String>> mAppDataDirs;
        private java.lang.String mBaseCodePath;
        private final java.lang.String mPackageName;
        private final java.util.Set<java.lang.String> mSplitCodePaths;

        public PackageCodeLocations(android.content.pm.ApplicationInfo ai, int userId) {
            this(ai.packageName, ai.sourceDir, ai.splitSourceDirs);
            mergeAppDataDirs(ai.dataDir, userId);
        }

        public PackageCodeLocations(java.lang.String packageName, java.lang.String baseCodePath, java.lang.String[] splitCodePaths) {
            this.mPackageName = packageName;
            this.mSplitCodePaths = new java.util.HashSet();
            this.mAppDataDirs = new java.util.HashMap();
            updateCodeLocation(baseCodePath, splitCodePaths);
        }

        public void updateCodeLocation(java.lang.String baseCodePath, java.lang.String[] splitCodePaths) {
            this.mBaseCodePath = baseCodePath;
            this.mSplitCodePaths.clear();
            if (splitCodePaths != null) {
                for (java.lang.String split : splitCodePaths) {
                    this.mSplitCodePaths.add(split);
                }
            }
        }

        public void mergeAppDataDirs(java.lang.String dataDir, int userId) {
            java.util.Set<java.lang.String> dataDirs = (java.util.Set) com.android.server.pm.dex.DexManager.putIfAbsent(this.mAppDataDirs, java.lang.Integer.valueOf(userId), new java.util.HashSet());
            dataDirs.add(dataDir);
        }

        public int searchDex(java.lang.String dexPath, int userId) {
            java.util.Set<java.lang.String> userDataDirs = this.mAppDataDirs.get(java.lang.Integer.valueOf(userId));
            if (userDataDirs == null) {
                return 0;
            }
            if (this.mBaseCodePath.equals(dexPath)) {
                return 1;
            }
            if (this.mSplitCodePaths.contains(dexPath)) {
                return 2;
            }
            for (java.lang.String dataDir : userDataDirs) {
                if (dexPath.startsWith(dataDir)) {
                    return 3;
                }
            }
            return 0;
        }
    }

    private class DexSearchResult {
        private final int mOutcome;
        private final java.lang.String mOwningPackageName;

        public DexSearchResult(java.lang.String owningPackageName, int outcome) {
            this.mOwningPackageName = owningPackageName;
            this.mOutcome = outcome;
        }

        public java.lang.String toString() {
            return this.mOwningPackageName + "-" + this.mOutcome;
        }
    }
}
