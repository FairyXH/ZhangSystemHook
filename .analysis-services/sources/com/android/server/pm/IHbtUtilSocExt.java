package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IHbtUtilSocExt {
    default void hbtCheckUninstall(java.lang.String name, java.lang.String[] hbtIsa) {
    }

    default void hbtCheckInstall(java.lang.String name, com.android.server.pm.PackageSetting oldPkgSetting, com.android.server.pm.ScanResult scanResult) throws com.android.server.pm.PackageManagerException {
    }

    default boolean isHbt64BitOnlyChip() {
        return false;
    }

    default java.lang.String getHbtPrimaryCpuAbi(android.content.pm.ApplicationInfo aInfo) {
        return aInfo.primaryCpuAbi;
    }

    default void translatorCheckScan(com.android.server.pm.PackageManagerService pm, com.android.server.pm.ScanResult scanResult, int scanFlags) {
    }

    default void translatorBeforeScan() {
    }

    default void translatorFinishedScan() {
    }
}
