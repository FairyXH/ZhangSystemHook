package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IAppDataHelperExt {
    default void init(com.android.server.pm.PackageManagerService pms, com.android.server.pm.AppDataHelper appDataHelper) {
    }

    default java.util.List<java.lang.String> customLogicForCeInReconcileAppsDataLI(java.util.List<java.lang.String> result, java.io.File ceDir, java.lang.String volumeUuid, int userId, int flags, boolean onlyCoreApps, boolean migrateAppData, com.android.server.pm.Computer snapshot) {
        return result;
    }

    default boolean skipDestroyCeDataInReconcileAppsDataLI(java.lang.String volumeUuid, java.lang.String packageName, int userId) {
        return false;
    }

    default void afterExecuteBatchInReconcileAppsDataLI(java.lang.String volumeUuid, int userId, int flags) {
    }

    default void afterExecuteBatchInReconcileAppsDataLI0(com.android.server.pm.IPkgReconcileDelayedExt pkgReconcileDelayed) {
    }

    default void afterDataPreparedInPrepareAppDataAndMigrate(com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags) {
    }

    default android.os.CreateAppDataResult fixDataForExceptionInPrepareAppDataLeaf(android.os.CreateAppDataResult createAppDataResult, com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags, int appId, java.lang.String volumeUuid, java.lang.String seInfo, java.lang.String packageName) {
        return createAppDataResult;
    }

    default void afterCreateAppDataCompleted(java.lang.Long ceDataInode, java.lang.Throwable e, com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags) {
    }

    default void onEndInReconcileAppsData(boolean isUpgrade, boolean debugDexopt) {
    }

    default boolean useCustomLogicForCeInReconcileAppsDataLI(boolean onlyCoreApps, int flags, int userId) {
        return false;
    }

    default boolean skipDestroyDeDataInReconcileAppsDataLI(java.lang.String volumeUuid, java.lang.String packageName, int userId) {
        return false;
    }

    default boolean delayPrepareAppDataInRADL(com.android.server.pm.IPkgReconcileDelayedExt pkgReconcileDelayed, com.android.server.pm.pkg.PackageStateInternal ps) {
        return false;
    }

    default void onEndInReconcileAppsDataLI(int flags) {
    }

    default boolean shouldReconcileAppsDataInConstructor(com.android.server.pm.PackageManagerService service) {
        return true;
    }

    default com.android.server.pm.IPkgReconcileDelayedExt beforePrepareAppDataInRADL(int flags, int userId, boolean isUpgrade, java.lang.String volumeUuid, boolean migrateAppData) {
        return com.android.server.pm.IPkgReconcileDelayedExt.DEFAULT;
    }

    default com.android.server.pm.IPkgReconcileSkipExt beforePrepareAppDataInRADL2(int flags, int userId, java.lang.String volumeUuid) {
        return com.android.server.pm.IPkgReconcileSkipExt.DEFAULT;
    }

    default boolean skipPrepareAppDataForPkgInRADL(com.android.server.pm.IPkgReconcileSkipExt pkgReconcileSkip, com.android.server.pm.pkg.PackageStateInternal ps) {
        return false;
    }

    default void beforeReconcileAppsDataInConstructor() {
    }

    default void onPrepareAppDataFutureEndByNoDefer() {
    }

    default void onPrepareAppDataFutureEndByDeferDone(int storageFlags) {
    }

    default boolean skipDestroyAppDataInDestroyAppDataLeafLIF(java.lang.String volumeUuid, java.lang.String packageName, int realUserId, int flags) {
        return false;
    }

    default boolean skipWorkAfterCreateAppData(com.android.server.pm.IBatchExt batchExt, com.android.server.pm.PackageSetting ps) {
        return false;
    }

    default void beforeReconcileAppsData(java.lang.String reason) {
    }

    default void afterReconcileAppsData(java.lang.String reason) {
    }
}
