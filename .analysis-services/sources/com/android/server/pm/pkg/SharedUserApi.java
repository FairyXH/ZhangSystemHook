package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public interface SharedUserApi {
    int getAppId();

    android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> getDisabledPackageStates();

    java.lang.String getName();

    android.util.ArraySet<? extends com.android.server.pm.pkg.PackageStateInternal> getPackageStates();

    java.util.List<com.android.server.pm.pkg.AndroidPackage> getPackages();

    int getPrivateUidFlags();

    android.util.ArrayMap<java.lang.String, com.android.internal.pm.pkg.component.ParsedProcess> getProcesses();

    int getSeInfoTargetSdkVersion();

    com.android.server.pm.permission.LegacyPermissionState getSharedUserLegacyPermissionState();

    android.content.pm.SigningDetails getSigningDetails();

    int getUidFlags();

    boolean isPrivileged();
}
