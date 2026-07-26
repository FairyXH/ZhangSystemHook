package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IContentProviderHelperExt {
    default boolean hookGetProviderAndInfo(com.android.server.am.ContentProviderRecord cpr, int userId) {
        return false;
    }

    default boolean hookGetProviderAndInfo(com.android.server.am.ContentProviderRecord cpr, int userId, com.android.server.wm.ActivityTaskManagerService atm, java.lang.String name, int callingUid) {
        return false;
    }

    default boolean hookHansProviderIfNeeded(android.content.pm.ProviderInfo cpi, int callingPid, int callingUid, java.lang.String callingPackage) {
        return false;
    }

    default boolean hookPreloadProviderBlock(int processId, android.content.pm.ProviderInfo cpi, int callingUid, java.lang.String callingPackage, com.android.server.am.ProcessRecord pr, com.android.server.am.ContentProviderRecord cpr) {
        return false;
    }

    default void hookComsumeTokenIfNeeded(com.android.server.am.ActivityManagerService mService, com.android.server.am.ContentProviderRecord cpr, android.content.pm.ProviderInfo cpi, int callingUid, java.lang.String callingPackage) {
    }

    default boolean hookAgingVersionWait(java.lang.String callingTag) {
        return false;
    }

    default void hookGetContentProviderImplAfterStartProc(com.android.server.am.ProcessRecord r, com.android.server.am.ContentProviderRecord cpr) {
    }

    default void handleReturnHolder(android.content.pm.ProviderInfo cpi, java.lang.String callingPackage, int callingUid, boolean procStart, int callingProcState) {
    }

    default void noteAssociation(int sourceUid, int targetUid, boolean add) {
    }

    default void logStatsRecord(com.android.server.am.ContentProviderRecord cpr, long callStartTime, boolean needCheckWait) {
    }

    default void updateExecutingComponent(int uid, int mode) {
    }
}
