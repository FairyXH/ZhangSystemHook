package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessErrorStateRecordExt {
    public static final boolean isAgingVersion = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest"));

    default void initForAnrStackDump() {
    }

    default void resetProcNumDumpStackPids() {
    }

    default void hookAddAnrAppProcNames(int MY_PID, int pid, int parentPid, java.util.ArrayList<java.lang.Integer> firstPids) {
    }

    default void hookAddFirstPids(java.lang.String processName, java.util.ArrayList<java.lang.Integer> firstPids, int myPid) {
    }

    default boolean hookAddPersistentProc(boolean isInterestProc) {
        return false;
    }

    default boolean hookAddLikelyIME() {
        return false;
    }

    default void hookANRInfo(int uid, int pid, java.lang.String packageName) {
    }

    default boolean hookAddANRProc(boolean isInterestProc) {
        return false;
    }

    default void hookSendTheiaEvent(com.android.server.am.ProcessRecord mApp, com.android.server.am.ActivityManagerService mService) {
    }

    default void hookAssertANRInfo(java.io.File tracesFile, int pid) {
    }

    default void hookSendApplicationStop(com.android.server.am.ActivityManagerService mService, com.android.server.am.ProcessRecord mApp) {
    }

    default boolean hookReturnIsInterestProc(com.android.server.am.ProcessRecord r) {
        return false;
    }

    default boolean isOnlyDumpSelf(android.content.pm.ApplicationInfo info) {
        return false;
    }

    default boolean isDumpMiddle(android.content.pm.ApplicationInfo info) {
        return false;
    }

    default boolean isDumpRestart(android.content.pm.ApplicationInfo info) {
        return false;
    }

    default void moveAnrTaskToBackIfNeed(com.android.server.am.ActivityManagerService mService, com.android.server.am.ProcessRecord mApp, boolean isSilentAnr, boolean aboveSystem) {
    }

    default void showAnrErrorDialogs(com.android.server.am.ActivityManagerService mService, java.util.List<android.content.Context> contexts, com.android.server.am.ProcessRecord mApp, int aboveSystem) {
    }

    default void showAnrErrorProgressDialogs(com.android.server.am.ActivityManagerService mService, java.util.List<android.content.Context> contexts, com.android.server.am.ProcessRecord mApp) {
    }

    default void clearAnrErrorDialogs(com.android.server.am.ActivityManagerService mService, com.android.server.am.ProcessRecord mApp) {
    }

    default void clearAnrErrorProgressDialogs(com.android.server.am.ActivityManagerService mService, com.android.server.am.ProcessRecord mApp) {
    }

    default void dumpSystraceWhenAnr(com.android.server.am.ActivityManagerService mService) {
    }

    default boolean isAgingtestSaveSystrace(android.content.pm.ApplicationInfo info) {
        return true;
    }

    default void dumpStackTraces(int pid, java.util.ArrayList<java.lang.Integer> firstPids, java.util.ArrayList<java.lang.Integer> nativePids, java.io.File outputFile) {
    }

    default void notifyTheiaAnrFinished(int pid, int uid, java.lang.String processName, java.lang.String state) {
    }

    default boolean isTheiaAnrTestApp(java.lang.String processName) {
        return false;
    }
}
