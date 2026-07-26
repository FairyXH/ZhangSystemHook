package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class BatteryController extends com.android.server.job.controllers.RestrictingController {
    private static final boolean DEBUG;
    private static final java.lang.String TAG = "JobScheduler.Battery";
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mChangedJobs;
    private final com.android.server.job.controllers.FlexibilityController mFlexibilityController;
    private java.lang.Boolean mLastReportedStatsdBatteryNotLow;
    private java.lang.Boolean mLastReportedStatsdStablePower;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mTopStartedJobs;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mTrackedTasks;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    public BatteryController(com.android.server.job.JobSchedulerService service, com.android.server.job.controllers.FlexibilityController flexibilityController) {
        super(service);
        this.mTrackedTasks = new android.util.ArraySet<>();
        this.mTopStartedJobs = new android.util.ArraySet<>();
        this.mChangedJobs = new android.util.ArraySet<>();
        this.mLastReportedStatsdBatteryNotLow = null;
        this.mLastReportedStatsdStablePower = null;
        this.mFlexibilityController = flexibilityController;
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus lastJob) {
        if (taskStatus.hasPowerConstraint()) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            this.mTrackedTasks.add(taskStatus);
            taskStatus.setTrackingController(1);
            if (taskStatus.hasChargingConstraint()) {
                if (hasTopExemptionLocked(taskStatus)) {
                    taskStatus.setChargingConstraintSatisfied(nowElapsed, this.mService.isPowerConnected());
                } else {
                    taskStatus.setChargingConstraintSatisfied(nowElapsed, this.mService.isBatteryCharging() && this.mService.isBatteryNotLow());
                }
            }
            taskStatus.setBatteryNotLowConstraintSatisfied(nowElapsed, this.mService.isBatteryNotLow());
        }
    }

    @Override // com.android.server.job.controllers.RestrictingController
    public void startTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        maybeStartTrackingJobLocked(jobStatus, null);
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (!jobStatus.hasPowerConstraint()) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Prepping for " + jobStatus.toShortString());
        }
        int uid = jobStatus.getSourceUid();
        if (this.mService.getUidBias(uid) == 40 && jobStatus.hasPowerConstraint()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, jobStatus.toShortString() + " is top started job");
            }
            this.mTopStartedJobs.add(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void unprepareFromExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        this.mTopStartedJobs.remove(jobStatus);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus taskStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        if (taskStatus.clearTrackingController(1)) {
            this.mTrackedTasks.remove(taskStatus);
            this.mTopStartedJobs.remove(taskStatus);
        }
    }

    @Override // com.android.server.job.controllers.RestrictingController
    public void stopTrackingRestrictedJobLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (!jobStatus.hasPowerConstraint()) {
            maybeStopTrackingJobLocked(jobStatus, null);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onBatteryStateChangedLocked() {
        com.android.server.AppSchedulingModuleThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.BatteryController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBatteryStateChangedLocked$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBatteryStateChangedLocked$0() {
        synchronized (this.mLock) {
            maybeReportNewChargingStateLocked();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUidBiasChangedLocked(int uid, int prevBias, int newBias) {
        if (prevBias == 40 || newBias == 40) {
            maybeReportNewChargingStateLocked();
        }
    }

    private boolean hasTopExemptionLocked(com.android.server.job.controllers.JobStatus taskStatus) {
        return this.mService.getUidBias(taskStatus.getSourceUid()) == 40 || this.mTopStartedJobs.contains(taskStatus);
    }

    private void maybeReportNewChargingStateLocked() {
        boolean powerConnected = this.mService.isPowerConnected();
        boolean stablePower = this.mService.isBatteryCharging() && this.mService.isBatteryNotLow();
        boolean batteryNotLow = this.mService.isBatteryNotLow();
        if (DEBUG) {
            android.util.Slog.d(TAG, "maybeReportNewChargingStateLocked: " + powerConnected + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + stablePower + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + batteryNotLow);
        }
        if (this.mLastReportedStatsdStablePower == null || this.mLastReportedStatsdStablePower.booleanValue() != stablePower) {
            logDeviceWideConstraintStateToStatsd(1, stablePower);
            this.mLastReportedStatsdStablePower = java.lang.Boolean.valueOf(stablePower);
        }
        if (this.mLastReportedStatsdBatteryNotLow == null || this.mLastReportedStatsdBatteryNotLow.booleanValue() != batteryNotLow) {
            logDeviceWideConstraintStateToStatsd(2, batteryNotLow);
            this.mLastReportedStatsdBatteryNotLow = java.lang.Boolean.valueOf(batteryNotLow);
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        this.mFlexibilityController.setConstraintSatisfied(1, this.mService.isBatteryCharging(), nowElapsed);
        this.mFlexibilityController.setConstraintSatisfied(2, batteryNotLow, nowElapsed);
        for (int i = this.mTrackedTasks.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus ts = this.mTrackedTasks.valueAt(i);
            if (ts.hasChargingConstraint()) {
                if (hasTopExemptionLocked(ts) && ts.getEffectivePriority() >= 300) {
                    if (ts.setChargingConstraintSatisfied(nowElapsed, powerConnected)) {
                        this.mChangedJobs.add(ts);
                    }
                } else if (ts.setChargingConstraintSatisfied(nowElapsed, stablePower)) {
                    this.mChangedJobs.add(ts);
                }
            }
            if (ts.hasBatteryNotLowConstraint() && ts.setBatteryNotLowConstraintSatisfied(nowElapsed, batteryNotLow)) {
                this.mChangedJobs.add(ts);
            }
        }
        if (stablePower || batteryNotLow) {
            this.mStateChangedListener.onRunJobNow(null);
        } else if (this.mChangedJobs.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(this.mChangedJobs);
        }
        this.mChangedJobs.clear();
    }

    android.util.ArraySet<com.android.server.job.controllers.JobStatus> getTrackedJobs() {
        return this.mTrackedTasks;
    }

    android.util.ArraySet<com.android.server.job.controllers.JobStatus> getTopStartedJobs() {
        return this.mTopStartedJobs;
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        pw.println("Stable power: " + (this.mService.isBatteryCharging() && this.mService.isBatteryNotLow()));
        pw.println("Not low: " + this.mService.isBatteryNotLow());
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
        long mToken = proto.start(1146756268034L);
        proto.write(1133871366145L, this.mService.isBatteryCharging() && this.mService.isBatteryNotLow());
        proto.write(1133871366146L, this.mService.isBatteryNotLow());
        for (int i = 0; i < this.mTrackedTasks.size(); i++) {
            com.android.server.job.controllers.JobStatus js = this.mTrackedTasks.valueAt(i);
            if (predicate.test(js)) {
                long jsToken = proto.start(2246267895813L);
                js.writeToShortProto(proto, 1146756268033L);
                proto.write(1120986464258L, js.getSourceUid());
                proto.end(jsToken);
            }
        }
        proto.end(mToken);
        proto.end(token);
    }
}
