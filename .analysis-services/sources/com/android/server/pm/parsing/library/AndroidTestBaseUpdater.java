package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public class AndroidTestBaseUpdater extends com.android.server.pm.parsing.library.PackageSharedLibraryUpdater {
    private static final long REMOVE_ANDROID_TEST_BASE = 133396946;
    private static final java.lang.String TAG = "AndroidTestBaseUpdater";

    private static boolean isChangeEnabled(com.android.server.pm.pkg.AndroidPackage pkg, boolean isSystemApp) {
        if (!isSystemApp) {
            com.android.internal.compat.IPlatformCompat platformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));
            try {
                return platformCompat.isChangeEnabled(REMOVE_ANDROID_TEST_BASE, com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg));
            } catch (android.os.RemoteException | java.lang.NullPointerException e) {
                android.util.Log.e(TAG, "Failed to get a response from PLATFORM_COMPAT_SERVICE", e);
            }
        }
        return pkg.getTargetSdkVersion() > 29;
    }

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage pkg, boolean isSystemApp, boolean isUpdatedSystemApp) {
        if (!isChangeEnabled(pkg, isSystemApp)) {
            prefixRequiredLibrary(pkg, "android.test.base");
        } else {
            prefixImplicitDependency(pkg, "android.test.runner", "android.test.base");
        }
    }
}
