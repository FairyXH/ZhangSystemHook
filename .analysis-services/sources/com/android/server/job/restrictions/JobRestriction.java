package com.android.server.job.restrictions;

/* JADX INFO: loaded from: classes2.dex */
public abstract class JobRestriction {
    private final int mInternalReason;
    private final int mPendingReason;
    final com.android.server.job.JobSchedulerService mService;
    private final int mStopReason;

    public abstract void dumpConstants(android.util.IndentingPrintWriter indentingPrintWriter);

    public abstract boolean isJobRestricted(com.android.server.job.controllers.JobStatus jobStatus, int i);

    protected JobRestriction(com.android.server.job.JobSchedulerService service, int stopReason, int pendingReason, int internalReason) {
        this.mService = service;
        this.mPendingReason = pendingReason;
        this.mStopReason = stopReason;
        this.mInternalReason = internalReason;
    }

    public void onSystemServicesReady() {
    }

    public void dumpConstants(android.util.proto.ProtoOutputStream proto) {
    }

    public final int getPendingReason() {
        return this.mPendingReason;
    }

    public final int getStopReason() {
        return this.mStopReason;
    }

    public final int getInternalReason() {
        return this.mInternalReason;
    }
}
