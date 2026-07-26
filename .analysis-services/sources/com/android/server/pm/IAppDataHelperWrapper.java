package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IAppDataHelperWrapper {
    default com.android.server.pm.IAppDataHelperExt getExtImpl() {
        return null;
    }

    default void prepareAppDataAndMigrate(com.android.server.pm.Installer.Batch batch, com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags, boolean maybeMigrateAppData) {
    }

    default void assertPackageStorageValid(com.android.server.pm.Computer snapshot, java.lang.String volumeUuid, java.lang.String packageName, int userId) throws com.android.server.pm.PackageManagerException {
    }

    default void executeBatchLI(com.android.server.pm.Installer.Batch batch) {
    }

    default boolean shouldHaveAppStorage(com.android.server.pm.pkg.AndroidPackage pkg) {
        return true;
    }

    default void prepareAppDataContentsLeafLIF(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int userId, int flags) {
    }
}
