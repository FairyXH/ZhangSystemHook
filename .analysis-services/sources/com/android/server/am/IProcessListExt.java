package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessListExt {
    default void addComputeGids(int mountExternal, int uid, java.util.ArrayList<java.lang.Integer> gidList) {
    }

    default boolean returnIsRunningDisallowed(java.lang.String packageName) {
        return false;
    }

    default void decideHandleActivityStart(com.android.server.am.HostingRecord hostingRecord, com.android.server.am.ProcessRecord app) {
    }

    default void decideCleanupAppInLaunchingProvidersLocked(com.android.server.am.ActivityManagerService mService, com.android.server.am.ProcessRecord app) {
    }

    default void sendApplicationStartAndDump(com.android.server.am.ProcessRecord app, int pid, com.android.server.am.ActivityManagerService mService) {
    }

    default void hookHandleProcessStart(com.android.server.am.ProcessRecord app) {
    }

    default boolean returnKillPackageProcessesFilter(com.android.server.am.ProcessRecord app, java.lang.String packageName, boolean isDep, int appId) {
        return false;
    }

    default int returnRrocessRecordPid(int pid) {
        return pid;
    }

    default void hookRemoveIsolatedUid(com.android.server.am.ProcessRecord app) {
    }

    default void hookAddIsolatedUid(int uid, int infoUid, java.lang.String packageName) {
    }

    default boolean returnSkipForLru(int lrui, com.android.server.am.ProcessRecord app) {
        return false;
    }

    default void hookHandleProcessKilled(boolean needRestart, boolean callerWillRestart, com.android.server.am.ProcessRecord app, int processId, java.lang.String reason) {
    }

    default void noteAppKill(com.android.server.am.ProcessRecord app) {
    }

    default boolean returnIsFromSwitchUser(java.util.List<java.lang.String> packagesToUpdate) {
        return false;
    }

    default boolean interceptStartProcessBeforePendingStartCheck(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessRecord app, com.android.server.am.HostingRecord hostingRecord) {
        return false;
    }

    default void hookStartProcessBeforeCheckPackageStartable(com.android.server.am.ProcessRecord app, com.android.server.am.HostingRecord hostingRecord) {
    }

    default boolean interceptStartProcessBeforeHandle(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessRecord app, com.android.server.am.HostingRecord hostingRecord) {
        return false;
    }

    default void hookStartProcessAfterHandleProcessStartAsync(com.android.server.am.ActiveUids activeUids, com.android.server.am.ProcessRecord app) {
    }

    default void hookStartProcessAfterHandleProcessStart(com.android.server.am.ActiveUids activeUids, com.android.server.am.ProcessRecord app) {
    }

    default com.android.server.am.ProcessRecord replaceProcessRecordAtNewProcessRecord(android.content.pm.ApplicationInfo info, com.android.server.am.ActiveUids activeUids, int uid, java.lang.String proc, com.android.server.am.HostingRecord hostingRecord) {
        return null;
    }

    default void hookScheduleApplicationInfoChanged(java.util.List<java.lang.String> packagesToUpdate, android.content.pm.ApplicationInfo ai, boolean updateFrameworkRes, com.android.server.am.ProcessRecord app, boolean fromSwitchUser) {
    }

    default int hookRuntimeFlags(com.android.server.am.ActivityManagerService mService, com.android.server.am.ProcessRecord app, int runtimeFlags) {
        return runtimeFlags;
    }

    default void hookOnSystemReady(com.android.server.am.ActivityManagerService service) {
    }

    default void customizeMinfreeLevels(int[] oomAdj, int[] oomMinFree, long totalMemMb, android.content.Context context) {
    }

    default int customizeExtraFreeKbytes(int reserve, long totalMemMb) {
        return reserve;
    }

    default void addGidsForMultiApp(int uid, java.util.ArrayList<java.lang.Integer> gidList) {
    }

    default int updateReasonCodeIfNeeded(int reasonCode) {
        return reasonCode;
    }

    default int updateSubReasonIfNeeded(int subReason) {
        return subReason;
    }

    default void handleAppZygoteStart(android.content.pm.ApplicationInfo info) {
    }

    public interface IStaticExt {
        default boolean returnPidNotMatchUid(int pid, int uid) {
            return false;
        }

        default boolean returnIsNotThreadGroudTid(int pid) {
            return false;
        }
    }

    default void setThreadSchedPolicy(int tid, java.lang.String tidName, int group) {
    }

    default boolean needAddPersistentStartingProcesses(com.android.server.am.ProcessRecord app) {
        return true;
    }

    default void setUxForStartProcess(android.os.Process.ProcessStartResult startResult, com.android.server.am.ProcessRecord app, boolean isTopApp) {
    }

    default void onBootComplete() {
    }

    default java.lang.String callerInfoPrint(com.android.server.am.ProcessRecord app, java.lang.String processType) {
        return null;
    }

    default boolean isForbidKill(java.lang.String pkgName) {
        return false;
    }
}
