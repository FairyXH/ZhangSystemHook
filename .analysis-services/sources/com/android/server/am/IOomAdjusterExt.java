package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IOomAdjusterExt {
    default void onHookadjustUxProcess(com.android.server.am.ProcessRecord app, int renderThreadTid, int action, boolean status) {
    }

    default boolean onHookKillCacheEmpty(com.android.server.wm.WindowProcessController app) {
        return false;
    }

    default com.android.server.am.BroadcastQueue getOplusFgBroadcastQueue() {
        return null;
    }

    default void setImportantAppAdj(com.android.server.am.ProcessRecord topApp, com.android.server.am.ProcessRecord app) {
    }

    default void adjustTopApp(java.lang.String packageName, int appPid, int renderThreadTid, android.util.IntArray hwuiTasks, int uid) {
    }

    default void updateRecentLockApps() {
    }

    default void bindSmallCore(com.android.server.am.ProcessRecord topApp, com.android.server.am.ProcessRecord app) {
    }

    default void notifyProcGrpChange(com.android.server.am.ProcessRecord app, int group) {
    }

    default boolean skipSetSchedGroup(com.android.server.am.ProcessRecord app) {
        return false;
    }

    default void setProcRecdOldSchedGroup(com.android.server.am.ProcessRecord app, int oldSchedGroup) {
    }

    default boolean isFrozen(int uid) {
        return false;
    }

    default void adjustUxProcess(com.android.server.am.ProcessRecord app, int schedGroup, int procState) {
    }

    default boolean isOclGrpRequestMsgAndSetGroup(android.os.Message msg) {
        return false;
    }

    default boolean treatAsFgBroadcast(com.android.server.am.ProcessRecord app) {
        return false;
    }

    default void handleImportantChanged(com.android.server.am.ProcessRecord app, int curProcState, int setProcState, boolean curImportant, boolean setImportant) {
    }

    default void onOomAdjUpdateLSP(com.android.server.am.ProcessRecord app, int reason, boolean fullUpdate, java.lang.String extra) {
    }

    default void onPendingOomAdjUpdateLSP(java.util.ArrayList<com.android.server.am.ProcessRecord> processes, int reason, boolean fullUpdate, java.lang.String extra) {
    }

    default void dumpOomAdjStatsLocked(java.io.PrintWriter pw) {
    }

    default void setFullOomAdjUpdateInfo(int uid, java.lang.String pkgName, java.lang.String extra) {
    }

    default void setUxThreadValueByFile(int pid, int tid, int value) {
    }

    default void updateProcRecdOomAdj(com.android.server.am.ProcessRecord processRecord) {
    }

    default boolean updateDialerAdj(com.android.server.am.ProcessRecord app, int adj) {
        return false;
    }
}
