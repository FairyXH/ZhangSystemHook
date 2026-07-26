package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface IJobServiceContextWrapper {
    default com.android.server.job.IJobServiceContextExt getExtImpl() {
        return new com.android.server.job.IJobServiceContextExt() { // from class: com.android.server.job.IJobServiceContextWrapper.1
        };
    }

    default android.app.job.JobParameters getParams() {
        return null;
    }

    default com.android.server.job.controllers.JobStatus getRunningJob() {
        return null;
    }

    default java.lang.Object getLock() {
        return null;
    }
}
