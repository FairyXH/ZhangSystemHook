package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface JobSchedulerInternalExt {
    public static final int PENDING_FAIL = 1;
    public static final int PENDING_PROCESSING = 2;
    public static final java.lang.String REASON_PENDING_JOB = "frozen_pending_internal";
    public static final int RESTORE_IGNORE = 1;
    public static final int RESTORE_SUCCESS = 2;
    public static final int SCENE_SCREEN_STATE_CHANGE = 100;

    android.util.ArrayMap<java.lang.Integer, java.lang.String> getOsenseStandardHistory();

    android.util.ArrayMap<java.lang.String, java.lang.Integer> getOsenseWhiteList();

    void resetOsenseHistory();

    void stopStrictModeOnJob();

    void updateOsenseRestrictMode(int i, boolean z, boolean z2);

    void updateProcStartByJob(java.lang.String str);

    void updateWhiteListPackage(int i, java.lang.String str, int i2, boolean z);

    default int cancelExecutingJobsForHans(int uid, java.util.ArrayList<java.lang.String> whiteComponentInfo) {
        return 1;
    }

    default int pendingJobs(int uid) {
        return 1;
    }

    default int restoreJobs(int uid) {
        return 1;
    }

    default void cancelJobsForKilledApp(java.lang.String pkgName, int pkgUid) {
    }
}
