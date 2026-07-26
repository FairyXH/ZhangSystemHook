package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface IJobServiceContextExt {
    default boolean updateExecutingParameter(com.android.server.job.JobServiceContext jsc, int level) {
        return false;
    }

    default long translateJobTimeout(com.android.server.job.controllers.JobStatus runningJob, int verb, long originTimeout) {
        return originTimeout;
    }
}
