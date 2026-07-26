package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
public interface IOverlayPackageCacheExt {
    default com.android.server.pm.pkg.AndroidPackage onPackageAdded(java.lang.String packageName, int userId) {
        return null;
    }

    default void onPackageRemoved(java.lang.String packageName, int userId) {
    }

    default com.android.server.pm.pkg.AndroidPackage getPackageForUser(java.lang.String packageName, int userId) {
        return null;
    }

    default void addExtPackageUser(com.android.server.pm.pkg.AndroidPackage pkg, int user) {
    }
}
