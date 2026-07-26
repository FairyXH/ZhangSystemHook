package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessErrorStateRecordSocExt {
    default boolean startAnrDump(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessErrorStateRecord processESR, java.lang.String activityShortComponentName, android.content.pm.ApplicationInfo apInfo, java.lang.String parentShortComponentName, com.android.server.am.ProcessRecord parentProcess, boolean aboveSystem, java.lang.String annotation, boolean showBackground, long anrTime, boolean onlyDumpSelf, java.util.UUID uuid, java.lang.String criticalEventLog, java.util.concurrent.ExecutorService auxiliaryTaskExecutor, com.android.internal.os.anr.AnrLatencyTracker latencyTracker, java.lang.String memoryHeaders, java.util.concurrent.Future<?> updateCpuStatsNowFirstCall, boolean isContinuousAnr, java.util.concurrent.Future<java.io.File> firstPidFilePromise) {
        return false;
    }
}
