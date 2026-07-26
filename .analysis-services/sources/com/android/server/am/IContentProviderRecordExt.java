package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IContentProviderRecordExt {
    default boolean isNeedRelease(android.content.ComponentName cmpName) {
        return false;
    }

    default void hookProviderTimeout(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessRecord launchingApp, android.content.pm.ApplicationInfo appInfo) {
    }

    default void addWaitTime(long startTime) {
    }

    default void setLogState(int logState) {
    }

    default void settleWaitTime(boolean isTimeOut) {
    }

    default void handleExtendDump(java.io.PrintWriter pw, java.lang.String prefix) {
    }
}
