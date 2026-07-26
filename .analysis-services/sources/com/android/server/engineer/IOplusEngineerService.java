package com.android.server.engineer;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusEngineerService extends android.common.IOplusCommonFeature {
    public static final com.android.server.engineer.IOplusEngineerService DEFAULT = new com.android.server.engineer.IOplusEngineerService() { // from class: com.android.server.engineer.IOplusEngineerService.1
    };
    public static final java.lang.String Name = "IOplusEngineerService";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusEngineerService;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void init() {
    }

    default void onAdbEnabled(boolean enabled) {
    }

    default boolean resolveActivityForOtgTest() {
        return false;
    }

    default boolean shouldPreventStartActivity(android.content.pm.ActivityInfo aInfo, java.lang.String callingPackage, int callingPid, int callingUid) {
        return false;
    }

    default boolean shouldPreventStartService(android.content.Intent service) {
        return false;
    }

    default void onPwkPressed() {
    }

    default void onPwkReleased() {
    }

    default void tryRemoveAllUserRecentTasksLocked() {
    }

    default boolean saveAppUsageHistoryRecord(android.content.ComponentName componentName) {
        return false;
    }

    default void onUsageShutdown() {
    }

    default boolean recordApkDeleteEvent(java.lang.String deleteAppPkgName, java.lang.String callerAppPkgName, java.lang.String dateTime) {
        return false;
    }
}
