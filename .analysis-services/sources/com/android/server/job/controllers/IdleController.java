package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class IdleController extends com.android.server.job.controllers.RestrictingController implements com.android.server.job.controllers.idle.IdlenessListener {
    private static final java.lang.String TAG = "JobScheduler.IdleController";
    private final com.android.server.job.controllers.FlexibilityController mFlexibilityController;
    public com.android.server.job.controllers.IIdleControllerExt mIdleControllerExt;
    com.android.server.job.controllers.idle.IdlenessTracker mIdleTracker;
    final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mTrackedTasks;

    public IdleController(com.android.server.job.JobSchedulerService service, com.android.server.job.controllers.FlexibilityController flexibilityController) {
        super(service);
        this.mTrackedTasks = new android.util.ArraySet<>();
        this.mIdleControllerExt = (com.android.server.job.controllers.IIdleControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.job.controllers.IIdleControllerExt.class).base(this).create();
        this.mIdleControllerExt.initFastIdle(service);
        initIdleStateTracker();
        this.mFlexibilityController = flexibilityController;
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus lastJob) {
        if (taskStatus.hasIdleConstraint()) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            this.mTrackedTasks.add(taskStatus);
            this.mIdleControllerExt.addTasks(taskStatus);
            taskStatus.setTrackingController(8);
            taskStatus.setIdleConstraintSatisfied(nowElapsed, this.mIdleTracker.isIdle());
        }
    }

    @Override // com.android.server.job.controllers.RestrictingController
    public void startTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        maybeStartTrackingJobLocked(jobStatus, null);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        if (taskStatus.clearTrackingController(8)) {
            this.mTrackedTasks.remove(taskStatus);
            this.mIdleControllerExt.removeTasks(taskStatus);
        }
    }

    @Override // com.android.server.job.controllers.RestrictingController
    public void stopTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (!jobStatus.hasIdleConstraint()) {
            maybeStopTrackingJobLocked(jobStatus, null);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void processConstantLocked(android.provider.DeviceConfig.Properties properties, java.lang.String key) {
        this.mIdleTracker.processConstant(properties, key);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onBatteryStateChangedLocked() {
        this.mIdleTracker.onBatteryStateChanged(this.mService.isBatteryCharging(), this.mService.isBatteryNotLow());
    }

    @Override // com.android.server.job.controllers.idle.IdlenessListener
    public void reportNewIdleState(boolean isIdle) {
        synchronized (this.mLock) {
            logDeviceWideConstraintStateToStatsd(4, isIdle);
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            this.mFlexibilityController.setConstraintSatisfied(4, isIdle, nowElapsed);
            for (int i = this.mTrackedTasks.size() - 1; i >= 0; i--) {
                this.mTrackedTasks.valueAt(i).setIdleConstraintSatisfied(nowElapsed, isIdle);
            }
            if (!this.mTrackedTasks.isEmpty()) {
                this.mStateChangedListener.onControllerStateChanged(this.mTrackedTasks);
            }
        }
    }

    private void initIdleStateTracker() {
        boolean isCar = this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
        if (isCar) {
            this.mIdleTracker = new com.android.server.job.controllers.idle.CarIdlenessTracker();
        } else {
            this.mIdleTracker = new com.android.server.job.controllers.idle.DeviceIdlenessTracker();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void startTrackingLocked() {
        this.mIdleTracker.startTracking(this.mContext, this.mService, this);
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
        pw.println();
        pw.println("IdleController:");
        pw.increaseIndent();
        this.mIdleTracker.dumpConstants(pw);
        pw.decreaseIndent();
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        pw.println("Currently idle: " + this.mIdleTracker.isIdle());
        pw.println("Idleness tracker:");
        this.mIdleTracker.dump(pw);
        pw.println();
        for (int i = 0; i < this.mTrackedTasks.size(); i++) {
            com.android.server.job.controllers.JobStatus js = this.mTrackedTasks.valueAt(i);
            if (predicate.test(js)) {
                pw.print("#");
                js.printUniqueId(pw);
                pw.print(" from ");
                android.os.UserHandle.formatUid(pw, js.getSourceUid());
                pw.println();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long token = proto.start(fieldId);
        long mToken = proto.start(1146756268038L);
        proto.write(1133871366145L, this.mIdleTracker.isIdle());
        this.mIdleTracker.dump(proto, 1146756268035L);
        for (int i = 0; i < this.mTrackedTasks.size(); i++) {
            com.android.server.job.controllers.JobStatus js = this.mTrackedTasks.valueAt(i);
            if (predicate.test(js)) {
                long jsToken = proto.start(2246267895810L);
                js.writeToShortProto(proto, 1146756268033L);
                proto.write(1120986464258L, js.getSourceUid());
                proto.end(jsToken);
            }
        }
        proto.end(mToken);
        proto.end(token);
    }
}
