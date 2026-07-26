package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageManagerServiceSocExt {
    default void createBoostFrameworkOnSystemReady() {
    }

    default void acquireUxPerfLockPkgUninstall(java.lang.String packageName, int userId, boolean res) {
    }

    default void acquireUxPerfLockPkgInstall(java.lang.String packageName) {
    }

    default void acquireUxPerfLockPkgUpdate(java.lang.String packageName) {
    }

    default void addVendorDataUid(com.android.server.pm.Settings settings) {
    }

    default void setInstallationBoost(boolean enable) {
    }

    default void registerHbtRusOnSystemReady() {
    }
}
