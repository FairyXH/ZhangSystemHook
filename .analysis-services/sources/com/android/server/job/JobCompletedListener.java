package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public interface JobCompletedListener {
    void onJobCompletedLocked(com.android.server.job.controllers.JobStatus jobStatus, int i, int i2, boolean z);
}
