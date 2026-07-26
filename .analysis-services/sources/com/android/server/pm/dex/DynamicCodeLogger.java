package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public class DynamicCodeLogger {
    private static final java.lang.String DCL_DEX_SUBTAG = "dcl";
    private static final java.lang.String DCL_NATIVE_SUBTAG = "dcln";
    private static final int SNET_TAG = 1397638484;
    private static final java.lang.String TAG = "DynamicCodeLogger";
    private final com.android.server.pm.Installer mInstaller;
    private final com.android.server.pm.dex.PackageDynamicCodeLoading mPackageDynamicCodeLoading;
    private android.content.pm.IPackageManager mPackageManager;

    public DynamicCodeLogger(com.android.server.pm.Installer installer) {
        this.mInstaller = installer;
        this.mPackageDynamicCodeLoading = new com.android.server.pm.dex.PackageDynamicCodeLoading();
    }

    DynamicCodeLogger(android.content.pm.IPackageManager packageManager, com.android.server.pm.Installer installer, com.android.server.pm.dex.PackageDynamicCodeLoading packageDynamicCodeLoading) {
        this.mPackageManager = packageManager;
        this.mInstaller = installer;
        this.mPackageDynamicCodeLoading = packageDynamicCodeLoading;
    }

    private android.content.pm.IPackageManager getPackageManager() {
        if (this.mPackageManager == null) {
            this.mPackageManager = android.content.pm.IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
        }
        return this.mPackageManager;
    }

    public java.util.Set<java.lang.String> getAllPackagesWithDynamicCodeLoading() {
        return this.mPackageDynamicCodeLoading.getAllPackagesWithDynamicCodeLoading();
    }

    /* JADX WARN: Incorrect condition in loop: B:7:0x0021 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void logDynamicCodeLoading(java.lang.String r26) {
        /*
            Method dump skipped, instruction units count: 478
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.dex.DynamicCodeLogger.logDynamicCodeLoading(java.lang.String):void");
    }

    private boolean fileIsUnder(java.lang.String filePath, java.lang.String directoryPath) {
        if (directoryPath == null) {
            return false;
        }
        try {
            return android.os.FileUtils.contains(new java.io.File(directoryPath).getCanonicalPath(), new java.io.File(filePath).getCanonicalPath());
        } catch (java.io.IOException e) {
            return false;
        }
    }

    com.android.server.pm.dex.PackageDynamicCodeLoading.PackageDynamicCode getPackageDynamicCodeInfo(java.lang.String packageName) {
        return this.mPackageDynamicCodeLoading.getPackageDynamicCodeInfo(packageName);
    }

    void writeDclEvent(java.lang.String subtag, int uid, java.lang.String message) {
        android.util.EventLog.writeEvent(SNET_TAG, subtag, java.lang.Integer.valueOf(uid), message);
    }

    public void recordDex(int loaderUserId, java.lang.String dexPath, java.lang.String owningPackageName, java.lang.String loadingPackageName) {
        if (this.mPackageDynamicCodeLoading.record(owningPackageName, dexPath, 68, loaderUserId, loadingPackageName)) {
            this.mPackageDynamicCodeLoading.maybeWriteAsync();
        }
    }

    public void recordNative(int loadingUid, java.lang.String path) {
        try {
            java.lang.String[] packages = getPackageManager().getPackagesForUid(loadingUid);
            if (packages != null) {
                if (packages.length == 0) {
                    return;
                }
                java.lang.String loadingPackageName = packages[0];
                int loadingUserId = android.os.UserHandle.getUserId(loadingUid);
                if (this.mPackageDynamicCodeLoading.record(loadingPackageName, path, 78, loadingUserId, loadingPackageName)) {
                    this.mPackageDynamicCodeLoading.maybeWriteAsync();
                }
            }
        } catch (android.os.RemoteException e) {
        }
    }

    void clear() {
        this.mPackageDynamicCodeLoading.clear();
    }

    void removePackage(java.lang.String packageName) {
        if (this.mPackageDynamicCodeLoading.removePackage(packageName)) {
            this.mPackageDynamicCodeLoading.maybeWriteAsync();
        }
    }

    void removeUserPackage(java.lang.String packageName, int userId) {
        if (this.mPackageDynamicCodeLoading.removeUserPackage(packageName, userId)) {
            this.mPackageDynamicCodeLoading.maybeWriteAsync();
        }
    }

    void readAndSync(java.util.Map<java.lang.String, java.util.Set<java.lang.Integer>> packageToUsersMap) {
        this.mPackageDynamicCodeLoading.read();
        this.mPackageDynamicCodeLoading.syncData(packageToUsersMap);
    }

    public void writeNow() {
        this.mPackageDynamicCodeLoading.writeNow();
    }

    public void load(java.util.Map<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> userToPackagesMap) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.Integer>> packageToUsersMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.Integer, java.util.List<android.content.pm.PackageInfo>> entry : userToPackagesMap.entrySet()) {
            java.util.List<android.content.pm.PackageInfo> packageInfoList = entry.getValue();
            int userId = entry.getKey().intValue();
            for (android.content.pm.PackageInfo pi : packageInfoList) {
                java.util.Set<java.lang.Integer> users = packageToUsersMap.computeIfAbsent(pi.packageName, new java.util.function.Function() { // from class: com.android.server.pm.dex.DynamicCodeLogger$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.pm.dex.DynamicCodeLogger.lambda$load$0((java.lang.String) obj);
                    }
                });
                users.add(java.lang.Integer.valueOf(userId));
            }
        }
        readAndSync(packageToUsersMap);
    }

    static /* synthetic */ java.util.Set lambda$load$0(java.lang.String k) {
        return new java.util.HashSet();
    }

    public void notifyPackageDataDestroyed(java.lang.String packageName, int userId) {
        if (userId == -1) {
            removePackage(packageName);
        } else {
            removeUserPackage(packageName, userId);
        }
    }
}
