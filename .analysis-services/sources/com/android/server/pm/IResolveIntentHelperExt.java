package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IResolveIntentHelperExt {
    default android.content.pm.ResolveInfo findPriorBeforeUsePreferenceInChooseBestActivity(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> query) {
        return null;
    }

    default int changeUserIdInChooseBestActivity(int originUserId, android.content.pm.ResolveInfo rInfo) {
        return originUserId;
    }

    default android.content.pm.ResolveInfo adjustQueryAndResultForUsePrefInChooseBestActivity(com.android.server.pm.Computer computer, android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> query, android.content.pm.ResolveInfo ri) {
        return null;
    }

    default boolean hasOplusPackageName(java.lang.String cmd) {
        return false;
    }

    default android.content.pm.ResolveInfo interceptAppDetailsToMarket(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> query, com.android.server.pm.Computer computer, int userId) {
        return null;
    }

    default boolean interceptHandler(android.content.Intent intent) {
        return false;
    }

    default boolean interceptHttpAppDetails(android.content.Intent intent) {
        return false;
    }

    default void adjustResultBeforeApplyPostResolutionFilter(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> results) {
    }

    default void filterResolveInfoForMultiApp(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> queryResults, int userId, int callingPid) {
    }
}
