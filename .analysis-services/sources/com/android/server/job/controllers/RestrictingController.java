package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public abstract class RestrictingController extends com.android.server.job.controllers.StateController {
    public abstract void startTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus);

    public abstract void stopTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus);

    RestrictingController(com.android.server.job.JobSchedulerService service) {
        super(service);
    }
}
