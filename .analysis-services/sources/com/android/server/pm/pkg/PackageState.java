package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface PackageState {
    com.android.server.pm.pkg.AndroidPackage getAndroidPackage();

    java.lang.String getApexModuleName();

    int getAppId();

    int getCategoryOverride();

    java.lang.String getCpuAbiOverride();

    int getHiddenApiEnforcementPolicy();

    long getLastModifiedTime();

    long[] getLastPackageUsageTime();

    long getLastUpdateTime();

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getMimeGroups();

    java.lang.String getPackageName();

    java.io.File getPath();

    java.lang.String getPrimaryCpuAbi();

    byte[] getRestrictUpdateHash();

    java.lang.String getSeInfo();

    java.lang.String getSecondaryCpuAbi();

    java.util.List<com.android.server.pm.pkg.SharedLibrary> getSharedLibraryDependencies();

    int getSharedUserAppId();

    android.content.pm.SigningInfo getSigningInfo();

    com.android.server.pm.pkg.PackageUserState getStateForUser(android.os.UserHandle userHandle);

    int getTargetSdkVersion();

    android.util.SparseArray<? extends com.android.server.pm.pkg.PackageUserState> getUserStates();

    java.util.List<java.lang.String> getUsesLibraryFiles();

    java.lang.String[] getUsesSdkLibraries();

    boolean[] getUsesSdkLibrariesOptional();

    long[] getUsesSdkLibrariesVersionsMajor();

    java.lang.String[] getUsesStaticLibraries();

    long[] getUsesStaticLibrariesVersions();

    long getVersionCode();

    java.lang.String getVolumeUuid();

    boolean hasSharedUser();

    boolean isApex();

    boolean isApkInUpdatedApex();

    boolean isDefaultToDeviceProtectedStorage();

    boolean isExternalStorage();

    boolean isForceQueryableOverride();

    boolean isHiddenUntilInstalled();

    boolean isInstallPermissionsFixed();

    boolean isOdm();

    boolean isOem();

    boolean isPendingRestore();

    boolean isPersistent();

    boolean isPrivileged();

    boolean isProduct();

    boolean isRequiredForSystemUser();

    boolean isScannedAsStoppedSystemApp();

    boolean isSystem();

    boolean isSystemExt();

    boolean isUpdateAvailable();

    boolean isUpdatedSystemApp();

    boolean isVendor();

    default com.android.server.pm.pkg.PackageUserState getUserStateOrDefault(int userId) {
        com.android.server.pm.pkg.PackageUserState userState = getUserStates().get(userId);
        return userState == null ? com.android.server.pm.pkg.PackageUserState.DEFAULT : userState;
    }
}
