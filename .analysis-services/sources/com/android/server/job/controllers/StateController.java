package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public abstract class StateController {
    private static final java.lang.String TAG = "JobScheduler.SC";
    protected final com.android.server.job.JobSchedulerService.Constants mConstants;
    protected final android.content.Context mContext;
    protected final java.lang.Object mLock;
    protected final com.android.server.job.JobSchedulerService mService;
    protected final com.android.server.job.StateChangedListener mStateChangedListener;

    public abstract void dumpControllerStateLocked(android.util.IndentingPrintWriter indentingPrintWriter, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate);

    public abstract void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus jobStatus2);

    public abstract void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus jobStatus2);

    StateController(com.android.server.job.JobSchedulerService service) {
        this.mService = service;
        this.mStateChangedListener = service;
        this.mContext = service.getTestableContext();
        this.mLock = service.getLock();
        this.mConstants = service.getConstants();
    }

    public void startTrackingLocked() {
    }

    public void onSystemServicesReady() {
    }

    public void prepareForExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
    }

    public void unprepareFromExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
    }

    public void rescheduleForFailureLocked(com.android.server.job.controllers.JobStatus newJob, com.android.server.job.controllers.JobStatus failureToReschedule) {
    }

    public void prepareForUpdatedConstantsLocked() {
    }

    public void processConstantLocked(android.provider.DeviceConfig.Properties properties, java.lang.String key) {
    }

    public void onConstantsUpdatedLocked() {
    }

    public void onAppRemovedLocked(java.lang.String packageName, int uid) {
    }

    public void onUserAddedLocked(int userId) {
    }

    public void onUserRemovedLocked(int userId) {
    }

    public void evaluateStateLocked(com.android.server.job.controllers.JobStatus jobStatus) {
    }

    public void reevaluateStateLocked(int uid) {
    }

    public void onBatteryStateChangedLocked() {
    }

    public void onUidBiasChangedLocked(int uid, int prevBias, int newBias) {
    }

    protected boolean wouldBeReadyWithConstraintLocked(com.android.server.job.controllers.JobStatus jobStatus, int constraint) {
        boolean jobWouldBeReady = jobStatus.wouldBeReadyWithConstraint(constraint);
        if (com.android.server.job.JobSchedulerService.DEBUG) {
            android.util.Slog.v(TAG, "wouldBeReadyWithConstraintLocked: " + jobStatus.toShortString() + " constraint=" + constraint + " readyWithConstraint=" + jobWouldBeReady);
        }
        if (!jobWouldBeReady) {
            return false;
        }
        return this.mService.areComponentsInPlaceLocked(jobStatus);
    }

    protected void logDeviceWideConstraintStateToStatsd(int constraint, boolean satisfied) {
        int i;
        int protoConstraint = com.android.server.job.controllers.JobStatus.getProtoConstraint(constraint);
        if (satisfied) {
            i = 2;
        } else {
            i = 1;
        }
        com.android.internal.util.FrameworkStatsLog.write(514, protoConstraint, i);
    }

    public void dumpControllerStateLocked(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
    }

    public void dumpConstants(android.util.IndentingPrintWriter pw) {
    }

    public void dumpConstants(android.util.proto.ProtoOutputStream proto) {
    }

    static java.lang.String packageToString(int userId, java.lang.String packageName) {
        return "<" + userId + ">" + packageName;
    }
}
