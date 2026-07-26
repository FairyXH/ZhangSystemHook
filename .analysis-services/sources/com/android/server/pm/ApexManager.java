package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ApexManager {
    public static final int MATCH_ACTIVE_PACKAGE = 1;
    static final int MATCH_FACTORY_PACKAGE = 2;
    private static final java.lang.String TAG = "ApexManager";
    private static final android.util.Singleton<com.android.server.pm.ApexManager> sApexManagerSingleton = new android.util.Singleton<com.android.server.pm.ApexManager>() { // from class: com.android.server.pm.ApexManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX INFO: renamed from: create, reason: merged with bridge method [inline-methods] */
        public com.android.server.pm.ApexManager m7580create() {
            return new com.android.server.pm.ApexManager.ApexManagerImpl();
        }
    };

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface PackageInfoFlags {
    }

    abstract boolean abortStagedSession(int i);

    public abstract long calculateSizeForCompressedApex(android.apex.CompressedApexInfoList compressedApexInfoList) throws android.os.RemoteException;

    public abstract boolean destroyCeSnapshots(int i, int i2);

    public abstract boolean destroyCeSnapshotsNotSpecified(int i, int[] iArr);

    public abstract boolean destroyDeSnapshots(int i);

    abstract void dump(java.io.PrintWriter printWriter);

    public abstract java.util.List<com.android.server.pm.ApexManager.ActiveApexInfo> getActiveApexInfos();

    public abstract java.lang.String getActiveApexPackageNameContainingPackage(java.lang.String str);

    public abstract java.lang.String getActivePackageNameForApexModuleName(java.lang.String str);

    abstract android.apex.ApexInfo[] getAllApexInfos();

    public abstract java.lang.String getApexModuleNameForPackageName(java.lang.String str);

    public abstract java.util.List<com.android.server.pm.ApexSystemServiceInfo> getApexSystemServices();

    abstract java.lang.String getApkInApexInstallError(java.lang.String str);

    public abstract java.util.List<java.lang.String> getApksInApex(java.lang.String str);

    public abstract java.io.File getBackingApexFile(java.io.File file);

    abstract android.util.SparseArray<android.apex.ApexSessionInfo> getSessions();

    abstract android.apex.ApexInfo[] getStagedApexInfos(android.apex.ApexSessionParams apexSessionParams);

    abstract android.apex.ApexSessionInfo getStagedSessionInfo(int i);

    abstract android.apex.ApexInfo installPackage(java.io.File file, boolean z) throws com.android.server.pm.PackageManagerException;

    abstract boolean isApexSupported();

    public abstract void markBootCompleted();

    abstract void markStagedSessionReady(int i) throws com.android.server.pm.PackageManagerException;

    abstract void markStagedSessionSuccessful(int i);

    abstract void notifyScanResult(java.util.List<com.android.server.pm.ApexManager.ScanResult> list);

    abstract void registerApkInApex(com.android.server.pm.pkg.AndroidPackage androidPackage);

    abstract void reportErrorWithApkInApex(java.lang.String str, java.lang.String str2);

    public abstract void reserveSpaceForCompressedApex(android.apex.CompressedApexInfoList compressedApexInfoList) throws android.os.RemoteException;

    public abstract boolean restoreCeData(int i, int i2, java.lang.String str);

    abstract boolean revertActiveSessions();

    public abstract boolean snapshotCeData(int i, int i2, java.lang.String str);

    abstract android.apex.ApexInfoList submitStagedSession(android.apex.ApexSessionParams apexSessionParams) throws com.android.server.pm.PackageManagerException;

    abstract boolean uninstallApex(java.lang.String str);

    public static com.android.server.pm.ApexManager getInstance() {
        return (com.android.server.pm.ApexManager) sApexManagerSingleton.get();
    }

    static class ScanResult {
        public final android.apex.ApexInfo apexInfo;
        public final java.lang.String packageName;
        public final com.android.server.pm.pkg.AndroidPackage pkg;

        ScanResult(android.apex.ApexInfo apexInfo, com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String packageName) {
            this.apexInfo = apexInfo;
            this.pkg = pkg;
            this.packageName = packageName;
        }
    }

    public static class ActiveApexInfo {
        public final boolean activeApexChanged;
        public final java.io.File apexDirectory;
        public final java.io.File apexFile;
        public final java.lang.String apexModuleName;
        public final boolean isFactory;
        public final java.io.File preInstalledApexPath;

        private ActiveApexInfo(java.io.File apexDirectory, java.io.File preInstalledApexPath, java.io.File apexFile) {
            this(null, apexDirectory, preInstalledApexPath, true, apexFile, false);
        }

        private ActiveApexInfo(java.lang.String apexModuleName, java.io.File apexDirectory, java.io.File preInstalledApexPath, boolean isFactory, java.io.File apexFile, boolean activeApexChanged) {
            this.apexModuleName = apexModuleName;
            this.apexDirectory = apexDirectory;
            this.preInstalledApexPath = preInstalledApexPath;
            this.isFactory = isFactory;
            this.apexFile = apexFile;
            this.activeApexChanged = activeApexChanged;
        }

        public ActiveApexInfo(android.apex.ApexInfo apexInfo) {
            this(apexInfo.moduleName, new java.io.File(android.os.Environment.getApexDirectory() + java.io.File.separator + apexInfo.moduleName), new java.io.File(apexInfo.preinstalledModulePath), apexInfo.isFactory, new java.io.File(apexInfo.modulePath), apexInfo.activeApexChanged);
        }
    }

    protected static class ApexManagerImpl extends com.android.server.pm.ApexManager {
        private java.util.Set<com.android.server.pm.ApexManager.ActiveApexInfo> mActiveApexInfosCache;
        private android.util.ArrayMap<java.lang.String, java.lang.String> mApexModuleNameToActivePackageName;
        private android.util.ArrayMap<java.lang.String, java.lang.String> mPackageNameToApexModuleName;
        private final java.lang.Object mLock = new java.lang.Object();
        private final java.util.List<com.android.server.pm.ApexSystemServiceInfo> mApexSystemServices = new java.util.ArrayList();
        private final android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> mApksInApex = new android.util.ArrayMap<>();
        private final java.util.Map<java.lang.String, java.lang.String> mErrorWithApkInApex = new android.util.ArrayMap();

        protected ApexManagerImpl() {
        }

        protected android.apex.IApexService waitForApexService() {
            return android.apex.IApexService.Stub.asInterface(android.os.Binder.allowBlocking(android.os.ServiceManager.waitForService("apexservice")));
        }

        @Override // com.android.server.pm.ApexManager
        android.apex.ApexInfo[] getAllApexInfos() {
            try {
                return waitForApexService().getAllPackages();
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to retrieve packages from apexservice: " + re.toString());
                throw new java.lang.RuntimeException(re);
            }
        }

        @Override // com.android.server.pm.ApexManager
        void notifyScanResult(java.util.List<com.android.server.pm.ApexManager.ScanResult> scanResults) {
            synchronized (this.mLock) {
                notifyScanResultLocked(scanResults);
            }
        }

        private void notifyScanResultLocked(java.util.List<com.android.server.pm.ApexManager.ScanResult> scanResults) {
            this.mPackageNameToApexModuleName = new android.util.ArrayMap<>();
            this.mApexModuleNameToActivePackageName = new android.util.ArrayMap<>();
            for (com.android.server.pm.ApexManager.ScanResult scanResult : scanResults) {
                android.apex.ApexInfo ai = scanResult.apexInfo;
                java.lang.String packageName = scanResult.packageName;
                for (com.android.internal.pm.pkg.component.ParsedApexSystemService service : scanResult.pkg.getApexSystemServices()) {
                    java.lang.String minSdkVersion = service.getMinSdkVersion();
                    if (minSdkVersion != null && !com.android.modules.utils.build.UnboundedSdkLevel.isAtLeast(minSdkVersion)) {
                        android.util.Slog.d(com.android.server.pm.ApexManager.TAG, java.lang.String.format("ApexSystemService %s with min_sdk_version=%s is skipped", service.getName(), service.getMinSdkVersion()));
                    } else {
                        java.lang.String maxSdkVersion = service.getMaxSdkVersion();
                        if (maxSdkVersion != null && !com.android.modules.utils.build.UnboundedSdkLevel.isAtMost(maxSdkVersion)) {
                            android.util.Slog.d(com.android.server.pm.ApexManager.TAG, java.lang.String.format("ApexSystemService %s with max_sdk_version=%s is skipped", service.getName(), service.getMaxSdkVersion()));
                        } else if (ai.isActive) {
                            java.lang.String name = service.getName();
                            for (int j = 0; j < this.mApexSystemServices.size(); j++) {
                                com.android.server.pm.ApexSystemServiceInfo info = this.mApexSystemServices.get(j);
                                if (info.getName().equals(name)) {
                                    throw new java.lang.IllegalStateException(android.text.TextUtils.formatSimple("Duplicate apex-system-service %s from %s, %s", new java.lang.Object[]{name, info.mJarPath, service.getJarPath()}));
                                }
                            }
                            this.mApexSystemServices.add(new com.android.server.pm.ApexSystemServiceInfo(service.getName(), service.getJarPath(), service.getInitOrder()));
                        } else {
                            continue;
                        }
                    }
                }
                java.util.Collections.sort(this.mApexSystemServices);
                this.mPackageNameToApexModuleName.put(packageName, ai.moduleName);
                if (ai.isActive) {
                    if (this.mApexModuleNameToActivePackageName.containsKey(ai.moduleName)) {
                        throw new java.lang.IllegalStateException("Two active packages have the same APEX module name: " + ai.moduleName);
                    }
                    this.mApexModuleNameToActivePackageName.put(ai.moduleName, packageName);
                }
            }
        }

        @Override // com.android.server.pm.ApexManager
        public java.util.List<com.android.server.pm.ApexManager.ActiveApexInfo> getActiveApexInfos() {
            com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog("ApexManagerTiming", 262144L);
            synchronized (this.mLock) {
                if (this.mActiveApexInfosCache == null) {
                    t.traceBegin("getActiveApexInfos_noCache");
                    try {
                        this.mActiveApexInfosCache = new android.util.ArraySet();
                        android.apex.ApexInfo[] activePackages = waitForApexService().getActivePackages();
                        for (android.apex.ApexInfo apexInfo : activePackages) {
                            this.mActiveApexInfosCache.add(new com.android.server.pm.ApexManager.ActiveApexInfo(apexInfo));
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to retrieve packages from apexservice", e);
                    }
                    t.traceEnd();
                }
                if (this.mActiveApexInfosCache != null) {
                    return new java.util.ArrayList(this.mActiveApexInfosCache);
                }
                return java.util.Collections.emptyList();
            }
        }

        @Override // com.android.server.pm.ApexManager
        public java.lang.String getActiveApexPackageNameContainingPackage(java.lang.String containedPackageName) {
            java.util.Objects.requireNonNull(containedPackageName);
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mPackageNameToApexModuleName != null, "APEX packages have not been scanned");
                int numApksInApex = this.mApksInApex.size();
                for (int apkInApexNum = 0; apkInApexNum < numApksInApex; apkInApexNum++) {
                    if (this.mApksInApex.valueAt(apkInApexNum).contains(containedPackageName)) {
                        java.lang.String apexModuleName = this.mApksInApex.keyAt(apkInApexNum);
                        int numApexPkgs = this.mPackageNameToApexModuleName.size();
                        for (int apexPkgNum = 0; apexPkgNum < numApexPkgs; apexPkgNum++) {
                            if (this.mPackageNameToApexModuleName.valueAt(apexPkgNum).equals(apexModuleName)) {
                                return this.mPackageNameToApexModuleName.keyAt(apexPkgNum);
                            }
                        }
                    }
                }
                return null;
            }
        }

        @Override // com.android.server.pm.ApexManager
        android.apex.ApexSessionInfo getStagedSessionInfo(int sessionId) {
            try {
                android.apex.ApexSessionInfo apexSessionInfo = waitForApexService().getStagedSessionInfo(sessionId);
                if (apexSessionInfo.isUnknown) {
                    return null;
                }
                return apexSessionInfo;
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice", re);
                throw new java.lang.RuntimeException(re);
            }
        }

        @Override // com.android.server.pm.ApexManager
        android.util.SparseArray<android.apex.ApexSessionInfo> getSessions() {
            try {
                android.apex.ApexSessionInfo[] sessions = waitForApexService().getSessions();
                android.util.SparseArray<android.apex.ApexSessionInfo> result = new android.util.SparseArray<>(sessions.length);
                for (int i = 0; i < sessions.length; i++) {
                    result.put(sessions[i].sessionId, sessions[i]);
                }
                return result;
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice", re);
                throw new java.lang.RuntimeException(re);
            }
        }

        @Override // com.android.server.pm.ApexManager
        android.apex.ApexInfoList submitStagedSession(android.apex.ApexSessionParams params) throws com.android.server.pm.PackageManagerException {
            try {
                android.apex.ApexInfoList apexInfoList = new android.apex.ApexInfoList();
                waitForApexService().submitStagedSession(params, apexInfoList);
                return apexInfoList;
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice", re);
                throw new java.lang.RuntimeException(re);
            } catch (java.lang.Exception e) {
                throw new com.android.server.pm.PackageManagerException(-22, "apexd verification failed : " + e.getMessage());
            }
        }

        @Override // com.android.server.pm.ApexManager
        android.apex.ApexInfo[] getStagedApexInfos(android.apex.ApexSessionParams params) {
            try {
                return waitForApexService().getStagedApexInfos(params);
            } catch (android.os.RemoteException re) {
                android.util.Slog.w(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice" + re.getMessage());
                throw new java.lang.RuntimeException(re);
            } catch (java.lang.Exception e) {
                android.util.Slog.w(com.android.server.pm.ApexManager.TAG, "Failed to collect staged apex infos" + e.getMessage());
                return new android.apex.ApexInfo[0];
            }
        }

        @Override // com.android.server.pm.ApexManager
        void markStagedSessionReady(int sessionId) throws com.android.server.pm.PackageManagerException {
            try {
                waitForApexService().markStagedSessionReady(sessionId);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice", re);
                throw new java.lang.RuntimeException(re);
            } catch (java.lang.Exception e) {
                throw new com.android.server.pm.PackageManagerException(-22, "Failed to mark apexd session as ready : " + e.getMessage());
            }
        }

        @Override // com.android.server.pm.ApexManager
        void markStagedSessionSuccessful(int sessionId) {
            try {
                waitForApexService().markStagedSessionSuccessful(sessionId);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice", re);
                throw new java.lang.RuntimeException(re);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Failed to mark session " + sessionId + " as successful", e);
            }
        }

        @Override // com.android.server.pm.ApexManager
        boolean isApexSupported() {
            return true;
        }

        @Override // com.android.server.pm.ApexManager
        boolean revertActiveSessions() {
            try {
                waitForApexService().revertActiveSessions();
                return true;
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice", re);
                return false;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        boolean abortStagedSession(int sessionId) {
            try {
                waitForApexService().abortStagedSession(sessionId);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        boolean uninstallApex(java.lang.String apexPackagePath) {
            try {
                waitForApexService().unstagePackages(java.util.Collections.singletonList(apexPackagePath));
                return true;
            } catch (java.lang.Exception e) {
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        void registerApkInApex(com.android.server.pm.pkg.AndroidPackage pkg) {
            synchronized (this.mLock) {
                for (com.android.server.pm.ApexManager.ActiveApexInfo aai : this.mActiveApexInfosCache) {
                    if (pkg.getBaseApkPath().startsWith(aai.apexDirectory.getAbsolutePath() + java.io.File.separator)) {
                        java.util.List<java.lang.String> apks = this.mApksInApex.get(aai.apexModuleName);
                        if (apks == null) {
                            apks = com.google.android.collect.Lists.newArrayList();
                            this.mApksInApex.put(aai.apexModuleName, apks);
                        }
                        android.util.Slog.i(com.android.server.pm.ApexManager.TAG, "Registering " + pkg.getPackageName() + " as apk-in-apex of " + aai.apexModuleName);
                        apks.add(pkg.getPackageName());
                    }
                }
            }
        }

        @Override // com.android.server.pm.ApexManager
        void reportErrorWithApkInApex(java.lang.String scanDirPath, java.lang.String errorMsg) {
            synchronized (this.mLock) {
                for (com.android.server.pm.ApexManager.ActiveApexInfo aai : this.mActiveApexInfosCache) {
                    if (scanDirPath.startsWith(aai.apexDirectory.getAbsolutePath())) {
                        this.mErrorWithApkInApex.put(aai.apexModuleName, errorMsg);
                    }
                }
            }
        }

        @Override // com.android.server.pm.ApexManager
        java.lang.String getApkInApexInstallError(java.lang.String apexPackageName) {
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mPackageNameToApexModuleName != null, "APEX packages have not been scanned");
                java.lang.String moduleName = this.mPackageNameToApexModuleName.get(apexPackageName);
                if (moduleName == null) {
                    return null;
                }
                return this.mErrorWithApkInApex.get(moduleName);
            }
        }

        @Override // com.android.server.pm.ApexManager
        public java.util.List<java.lang.String> getApksInApex(java.lang.String apexPackageName) {
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mPackageNameToApexModuleName != null, "APEX packages have not been scanned");
                java.lang.String moduleName = this.mPackageNameToApexModuleName.get(apexPackageName);
                if (moduleName == null) {
                    return java.util.Collections.emptyList();
                }
                return this.mApksInApex.getOrDefault(moduleName, java.util.Collections.emptyList());
            }
        }

        @Override // com.android.server.pm.ApexManager
        public java.lang.String getApexModuleNameForPackageName(java.lang.String apexPackageName) {
            java.lang.String str;
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mPackageNameToApexModuleName != null, "APEX packages have not been scanned");
                str = this.mPackageNameToApexModuleName.get(apexPackageName);
            }
            return str;
        }

        @Override // com.android.server.pm.ApexManager
        public java.lang.String getActivePackageNameForApexModuleName(java.lang.String apexModuleName) {
            java.lang.String str;
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mApexModuleNameToActivePackageName != null, "APEX packages have not been scanned");
                str = this.mApexModuleNameToActivePackageName.get(apexModuleName);
            }
            return str;
        }

        @Override // com.android.server.pm.ApexManager
        public boolean snapshotCeData(int userId, int rollbackId, java.lang.String apexPackageName) {
            java.lang.String apexModuleName;
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mPackageNameToApexModuleName != null, "APEX packages have not been scanned");
                apexModuleName = this.mPackageNameToApexModuleName.get(apexPackageName);
            }
            if (apexModuleName == null) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Invalid apex package name: " + apexPackageName);
                return false;
            }
            try {
                waitForApexService().snapshotCeData(userId, rollbackId, apexModuleName);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        public boolean restoreCeData(int userId, int rollbackId, java.lang.String apexPackageName) {
            java.lang.String apexModuleName;
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mPackageNameToApexModuleName != null, "APEX packages have not been scanned");
                apexModuleName = this.mPackageNameToApexModuleName.get(apexPackageName);
            }
            if (apexModuleName == null) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Invalid apex package name: " + apexPackageName);
                return false;
            }
            try {
                waitForApexService().restoreCeData(userId, rollbackId, apexModuleName);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        public boolean destroyDeSnapshots(int rollbackId) {
            try {
                waitForApexService().destroyDeSnapshots(rollbackId);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        public boolean destroyCeSnapshots(int userId, int rollbackId) {
            try {
                waitForApexService().destroyCeSnapshots(userId, rollbackId);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        public boolean destroyCeSnapshotsNotSpecified(int userId, int[] retainRollbackIds) {
            try {
                waitForApexService().destroyCeSnapshotsNotSpecified(userId, retainRollbackIds);
                return true;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override // com.android.server.pm.ApexManager
        public void markBootCompleted() {
            try {
                waitForApexService().markBootCompleted();
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(com.android.server.pm.ApexManager.TAG, "Unable to contact apexservice", re);
            }
        }

        @Override // com.android.server.pm.ApexManager
        public long calculateSizeForCompressedApex(android.apex.CompressedApexInfoList infoList) throws android.os.RemoteException {
            return waitForApexService().calculateSizeForCompressedApex(infoList);
        }

        @Override // com.android.server.pm.ApexManager
        public void reserveSpaceForCompressedApex(android.apex.CompressedApexInfoList infoList) throws android.os.RemoteException {
            waitForApexService().reserveSpaceForCompressedApex(infoList);
        }

        private android.content.pm.SigningDetails getSigningDetails(android.content.pm.PackageInfo pkg) throws com.android.server.pm.PackageManagerException {
            int minSignatureScheme = android.util.apk.ApkSignatureVerifier.getMinimumSignatureSchemeVersionForTargetSdk(pkg.applicationInfo.targetSdkVersion);
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            android.content.pm.parsing.result.ParseResult<android.content.pm.SigningDetails> result = android.util.apk.ApkSignatureVerifier.verify(input, pkg.applicationInfo.sourceDir, minSignatureScheme);
            if (result.isError()) {
                throw new com.android.server.pm.PackageManagerException(result.getErrorCode(), result.getErrorMessage(), result.getException());
            }
            return (android.content.pm.SigningDetails) result.getResult();
        }

        private void checkApexSignature(android.content.pm.PackageInfo existingApexPkg, android.content.pm.PackageInfo newApexPkg) throws com.android.server.pm.PackageManagerException {
            android.content.pm.SigningDetails existingSigningDetails = getSigningDetails(existingApexPkg);
            android.content.pm.SigningDetails newSigningDetails = getSigningDetails(newApexPkg);
            if (!newSigningDetails.checkCapability(existingSigningDetails, 1)) {
                throw new com.android.server.pm.PackageManagerException(-118, "APK container signature of " + newApexPkg.applicationInfo.sourceDir + " is not compatible with currently installed on device");
            }
        }

        @Override // com.android.server.pm.ApexManager
        android.apex.ApexInfo installPackage(java.io.File apexFile, boolean force) throws com.android.server.pm.PackageManagerException {
            try {
                return waitForApexService().installAndActivatePackage(apexFile.getAbsolutePath(), force);
            } catch (android.os.RemoteException e) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "apexservice not available");
            } catch (java.lang.Exception e2) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, e2.getMessage());
            }
        }

        @Override // com.android.server.pm.ApexManager
        public java.util.List<com.android.server.pm.ApexSystemServiceInfo> getApexSystemServices() {
            java.util.List<com.android.server.pm.ApexSystemServiceInfo> list;
            synchronized (this.mLock) {
                com.android.internal.util.Preconditions.checkState(this.mApexSystemServices != null, "APEX packages have not been scanned");
                list = this.mApexSystemServices;
            }
            return list;
        }

        @Override // com.android.server.pm.ApexManager
        public java.io.File getBackingApexFile(java.io.File file) {
            java.nio.file.Path path = file.toPath();
            if (!path.startsWith(android.os.Environment.getApexDirectory().toPath()) || path.getNameCount() < 2) {
                return null;
            }
            java.lang.String moduleName = file.toPath().getName(1).toString();
            java.util.List<com.android.server.pm.ApexManager.ActiveApexInfo> apexes = getActiveApexInfos();
            for (int i = 0; i < apexes.size(); i++) {
                if (apexes.get(i).apexModuleName.equals(moduleName)) {
                    return apexes.get(i).apexFile;
                }
            }
            return null;
        }

        @Override // com.android.server.pm.ApexManager
        void dump(java.io.PrintWriter pw) {
            com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ", 120);
            try {
                ipw.println();
                ipw.println("APEX session state:");
                ipw.increaseIndent();
                android.apex.ApexSessionInfo[] sessions = waitForApexService().getSessions();
                for (android.apex.ApexSessionInfo si : sessions) {
                    ipw.println("Session ID: " + si.sessionId);
                    ipw.increaseIndent();
                    if (si.isUnknown) {
                        ipw.println("State: UNKNOWN");
                    } else if (si.isVerified) {
                        ipw.println("State: VERIFIED");
                    } else if (si.isStaged) {
                        ipw.println("State: STAGED");
                    } else if (si.isActivated) {
                        ipw.println("State: ACTIVATED");
                    } else if (si.isActivationFailed) {
                        ipw.println("State: ACTIVATION FAILED");
                    } else if (si.isSuccess) {
                        ipw.println("State: SUCCESS");
                    } else if (si.isRevertInProgress) {
                        ipw.println("State: REVERT IN PROGRESS");
                    } else if (si.isReverted) {
                        ipw.println("State: REVERTED");
                    } else if (si.isRevertFailed) {
                        ipw.println("State: REVERT FAILED");
                    }
                    ipw.decreaseIndent();
                }
                ipw.decreaseIndent();
                ipw.println();
            } catch (android.os.RemoteException e) {
                ipw.println("Couldn't communicate with apexd.");
            }
        }
    }
}
