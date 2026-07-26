package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface IJobSchedulerServiceWrapper {
    default com.android.server.job.IJobSchedulerServiceExt getExtImpl() {
        return new com.android.server.job.IJobSchedulerServiceExt() { // from class: com.android.server.job.IJobSchedulerServiceWrapper.1
        };
    }

    default int getMAX_JOBS_PER_APP() {
        return 0;
    }

    default void cancelJobImplLocked(com.android.server.job.controllers.JobStatus cancelled, com.android.server.job.controllers.JobStatus incomingJob, int reason, int internalReasonCode, java.lang.String debugReason) {
    }

    default boolean stopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob, boolean removeFromPersisted) {
        return false;
    }

    default void cancelJobsForPackageAndUidLocked(java.lang.String pkgName, int uid, int reason, int internalReasonCode, java.lang.String debugReason) {
    }
}
