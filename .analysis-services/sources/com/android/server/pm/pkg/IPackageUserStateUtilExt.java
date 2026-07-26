package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageUserStateUtilExt {
    default boolean isOhidePackage(com.android.server.pm.pkg.PackageUserState state) {
        return false;
    }

    default boolean isOhideVisible(com.android.server.pm.pkg.PackageUserState state) {
        return false;
    }
}
