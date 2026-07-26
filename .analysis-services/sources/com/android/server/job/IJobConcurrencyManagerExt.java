package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface IJobConcurrencyManagerExt {
    default void hookStartJobErrorExecute(com.android.server.job.controllers.JobStatus jobStatus, int workType) {
    }
}
