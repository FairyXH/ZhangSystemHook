package com.android.server.crashrecovery;

/* JADX INFO: loaded from: classes.dex */
public final class CrashRecoveryHelper {
    private static final java.lang.String TAG = "CrashRecoveryHelper";
    private final com.android.server.pm.ApexManager mApexManager = com.android.server.pm.ApexManager.getInstance();
    private final android.net.ConnectivityModuleConnector mConnectivityModuleConnector = android.net.ConnectivityModuleConnector.getInstance();
    private final android.content.Context mContext;

    public CrashRecoveryHelper(android.content.Context context) {
        this.mContext = context;
    }

    public boolean isModule(java.lang.String packageName) {
        java.lang.String apexPackageName = this.mApexManager.getActiveApexPackageNameContainingPackage(packageName);
        if (apexPackageName != null) {
            packageName = apexPackageName;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            return pm.getModuleInfo(packageName, 0) != null;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void registerConnectivityModuleHealthListener(final int failureReason) {
        this.mConnectivityModuleConnector.registerHealthListener(new android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener() { // from class: com.android.server.crashrecovery.CrashRecoveryHelper$$ExternalSyntheticLambda0
            @Override // android.net.ConnectivityModuleConnector.ConnectivityModuleHealthListener
            public final void onNetworkStackFailure(java.lang.String str) {
                this.f$0.lambda$registerConnectivityModuleHealthListener$0(failureReason, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerConnectivityModuleHealthListener$0(int failureReason, java.lang.String packageName) {
        android.content.pm.VersionedPackage pkg = getVersionedPackage(packageName);
        if (pkg == null) {
            android.util.Slog.wtf(TAG, "NetworkStack failed but could not find its package");
        } else {
            java.util.List<android.content.pm.VersionedPackage> pkgList = java.util.Collections.singletonList(pkg);
            com.android.server.PackageWatchdog.getInstance(this.mContext).onPackageFailure(pkgList, failureReason);
        }
    }

    private android.content.pm.VersionedPackage getVersionedPackage(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm == null || android.text.TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            long versionCode = getPackageInfo(packageName).getLongVersionCode();
            return new android.content.pm.VersionedPackage(packageName, versionCode);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, 4194304);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return pm.getPackageInfo(packageName, 1073741824);
        }
    }
}
