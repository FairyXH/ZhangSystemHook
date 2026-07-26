package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public interface PackageStateInternal extends com.android.server.pm.pkg.PackageState {
    java.lang.String getAppMetadataFilePath();

    int getAppMetadataSource();

    java.util.UUID getDomainSetId();

    int getFlags();

    com.android.server.pm.InstallSource getInstallSource();

    com.android.server.pm.PackageKeySetData getKeySetData();

    com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState();

    long getLoadingCompletedTime();

    float getLoadingProgress();

    java.util.Set<java.io.File> getOldPaths();

    java.lang.String getPathString();

    com.android.internal.pm.parsing.pkg.AndroidPackageInternal getPkg();

    @java.lang.Deprecated
    java.lang.String getPrimaryCpuAbiLegacy();

    int getPrivateFlags();

    java.lang.String getRealName();

    java.lang.String getSecondaryCpuAbiLegacy();

    android.content.pm.SigningDetails getSigningDetails();

    com.android.server.pm.pkg.PackageStateUnserialized getTransientState();

    @Override // com.android.server.pm.pkg.PackageState
    android.util.SparseArray<? extends com.android.server.pm.pkg.PackageUserStateInternal> getUserStates();

    boolean isLoading();

    @Override // com.android.server.pm.pkg.PackageState
    default com.android.server.pm.pkg.PackageUserStateInternal getUserStateOrDefault(int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal userState = getUserStates().get(userId);
        return userState == null ? com.android.server.pm.pkg.PackageUserStateInternal.DEFAULT : userState;
    }
}
