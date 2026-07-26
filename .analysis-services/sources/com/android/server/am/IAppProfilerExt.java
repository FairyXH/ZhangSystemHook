package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IAppProfilerExt {
    default boolean checkCPUBusy() {
        return false;
    }

    default boolean isGameScene() {
        return false;
    }

    default boolean isEnableHighLoadSkipPss() {
        return true;
    }

    default boolean isNeedSkipDumpPss(android.os.Handler handler) {
        return false;
    }

    default void recordPssStats(java.lang.String pkgName, java.lang.String procName, int uid, int pid, int procState, long pss, long uss, long swapPss, long rss) {
    }

    default void recordRssStats(com.android.server.am.ProcessRecord proc, int procState, long rss) {
    }

    default void hookRssUpdateFinish() {
    }

    default void boost(com.android.server.am.ProcessProfileRecord profile, int boostToPriority) {
    }

    default void reset(com.android.server.am.ProcessProfileRecord profile) {
    }

    default boolean isSkipTrimMemoryForQuickBootScene(java.lang.String processName) {
        return false;
    }

    default boolean filterNativeProcessGetPss(com.android.internal.os.ProcessCpuTracker.Stats stats) {
        return false;
    }

    default boolean handleMemLevelChanged(int oldMemFactor, int newMemFactor, android.os.Handler handler, int msg) {
        return false;
    }
}
