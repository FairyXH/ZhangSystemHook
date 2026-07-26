package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public interface IAppStandbyControllerExt {
    default void initConstructor(android.content.Context ctx, com.android.server.usage.AppStandbyController asc, com.android.server.usage.AppIdleHistory history, android.os.Handler handler) {
    }

    default boolean matchGoogleRestrictRule(java.lang.String pkg) {
        return false;
    }

    default void uploadAABPredictInfoWhenReportEvent(com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory, java.lang.String pkgname, int newBucket, int reason, int userId) {
    }

    default boolean interceptReportEvent(android.app.usage.UsageEvents.Event event, long elapsedRealtime, int userId) {
        return false;
    }

    default boolean isSystemApp(java.lang.String pkgName, int userId) {
        return false;
    }

    default void printPredict(com.android.server.usage.AppIdleHistory.AppUsageHistory app, java.lang.String packageName, int newBucket, boolean predicted) {
    }

    default void uploadAABPredictInfoWhenSet(java.lang.String pkgname, int activeBucket, int predictBucket, long timeRemainder) {
    }

    default boolean isCustomizeDozeModeDisabled() {
        return false;
    }
}
