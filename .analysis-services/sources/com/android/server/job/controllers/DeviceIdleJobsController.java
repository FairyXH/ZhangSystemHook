package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class DeviceIdleJobsController extends com.android.server.job.controllers.StateController {
    private static final long BACKGROUND_JOBS_DELAY = 3000;
    private static final boolean DEBUG;
    static final int PROCESS_BACKGROUND_JOBS = 1;
    private static final java.lang.String TAG = "JobScheduler.DeviceIdle";
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mAllowInIdleJobs;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private boolean mDeviceIdleMode;
    private final com.android.server.job.controllers.DeviceIdleJobsController.DeviceIdleUpdateFunctor mDeviceIdleUpdateFunctor;
    private int[] mDeviceIdleWhitelistAppIds;
    private final android.util.SparseBooleanArray mForegroundUids;
    private final com.android.server.job.controllers.DeviceIdleJobsController.DeviceIdleJobsDelayHandler mHandler;
    private final com.android.server.DeviceIdleInternal mLocalDeviceIdleController;
    private final android.os.PowerManager mPowerManager;
    private int[] mPowerSaveTempWhitelistAppIds;
    private final java.util.function.Predicate<com.android.server.job.controllers.JobStatus> mShouldRushEvaluation;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(com.android.server.job.controllers.JobStatus jobStatus) {
        return jobStatus.isRequestedExpeditedJob() || this.mForegroundUids.get(jobStatus.getSourceUid());
    }

    public DeviceIdleJobsController(com.android.server.job.JobSchedulerService service) {
        super(service);
        this.mForegroundUids = new android.util.SparseBooleanArray();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.job.controllers.DeviceIdleJobsController.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:17:0x0036  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r8, android.content.Intent r9) {
                /*
                    Method dump skipped, instruction units count: 338
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.DeviceIdleJobsController.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mShouldRushEvaluation = new java.util.function.Predicate() { // from class: com.android.server.job.controllers.DeviceIdleJobsController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$new$0((com.android.server.job.controllers.JobStatus) obj);
            }
        };
        this.mHandler = new com.android.server.job.controllers.DeviceIdleJobsController.DeviceIdleJobsDelayHandler(com.android.server.AppSchedulingModuleThread.get().getLooper());
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService("power");
        this.mLocalDeviceIdleController = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
        this.mDeviceIdleWhitelistAppIds = this.mLocalDeviceIdleController.getPowerSaveWhitelistUserAppIds();
        this.mPowerSaveTempWhitelistAppIds = this.mLocalDeviceIdleController.getPowerSaveTempWhitelistAppIds();
        this.mDeviceIdleUpdateFunctor = new com.android.server.job.controllers.DeviceIdleJobsController.DeviceIdleUpdateFunctor();
        this.mAllowInIdleJobs = new android.util.ArraySet<>();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        filter.addAction("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
        filter.addAction("android.os.action.POWER_SAVE_WHITELIST_CHANGED");
        filter.addAction("android.os.action.POWER_SAVE_TEMP_WHITELIST_CHANGED");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, filter, null, null);
    }

    void updateIdleMode(boolean enabled) {
        boolean changed = false;
        synchronized (this.mLock) {
            if (this.mDeviceIdleMode != enabled) {
                changed = true;
            }
            this.mDeviceIdleMode = enabled;
            logDeviceWideConstraintStateToStatsd(33554432, !this.mDeviceIdleMode);
            if (DEBUG) {
                android.util.Slog.d(TAG, "mDeviceIdleMode=" + this.mDeviceIdleMode);
            }
            this.mDeviceIdleUpdateFunctor.prepare();
            if (enabled) {
                this.mHandler.removeMessages(1);
                this.mService.getJobStore().forEachJob(this.mDeviceIdleUpdateFunctor);
            } else {
                this.mService.getJobStore().forEachJob(this.mShouldRushEvaluation, this.mDeviceIdleUpdateFunctor);
                this.mHandler.sendEmptyMessageDelayed(1, 3000L);
            }
        }
        if (changed) {
            this.mStateChangedListener.onDeviceIdleStateChanged(enabled);
        }
    }

    public void setUidActiveLocked(int uid, boolean active) {
        boolean changed = active != this.mForegroundUids.get(uid);
        if (!changed) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "uid " + uid + " going " + (active ? com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE : "inactive"));
        }
        this.mForegroundUids.put(uid, active);
        this.mDeviceIdleUpdateFunctor.prepare();
        this.mService.getJobStore().forEachJobForSourceUid(uid, this.mDeviceIdleUpdateFunctor);
        if (this.mDeviceIdleUpdateFunctor.mChangedJobs.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(this.mDeviceIdleUpdateFunctor.mChangedJobs);
        }
    }

    boolean isWhitelistedLocked(com.android.server.job.controllers.JobStatus job) {
        return java.util.Arrays.binarySearch(this.mDeviceIdleWhitelistAppIds, android.os.UserHandle.getAppId(job.getSourceUid())) >= 0;
    }

    boolean isTempWhitelistedLocked(com.android.server.job.controllers.JobStatus job) {
        return com.android.internal.util.ArrayUtils.contains(this.mPowerSaveTempWhitelistAppIds, android.os.UserHandle.getAppId(job.getSourceUid()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateTaskStateLocked(com.android.server.job.controllers.JobStatus task, long nowElapsed) {
        boolean enableTask = true;
        boolean allowInIdle = (task.getFlags() & 2) != 0 && (this.mForegroundUids.get(task.getSourceUid()) || isTempWhitelistedLocked(task));
        boolean whitelisted = isWhitelistedLocked(task);
        if (this.mDeviceIdleMode && !whitelisted && !allowInIdle) {
            enableTask = false;
        }
        return task.setDeviceNotDozingConstraintSatisfied(nowElapsed, enableTask, whitelisted);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus lastJob) {
        if ((jobStatus.getFlags() & 2) != 0) {
            this.mAllowInIdleJobs.add(jobStatus);
        }
        updateTaskStateLocked(jobStatus, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        if ((jobStatus.getFlags() & 2) != 0) {
            this.mAllowInIdleJobs.remove(jobStatus);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        pw.println("Idle mode: " + this.mDeviceIdleMode);
        pw.println();
        this.mService.getJobStore().forEachJob(predicate, new java.util.function.Consumer() { // from class: com.android.server.job.controllers.DeviceIdleJobsController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$dumpControllerStateLocked$1(pw, (com.android.server.job.controllers.JobStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$1(android.util.IndentingPrintWriter pw, com.android.server.job.controllers.JobStatus jobStatus) {
        pw.print("#");
        jobStatus.printUniqueId(pw);
        pw.print(" from ");
        android.os.UserHandle.formatUid(pw, jobStatus.getSourceUid());
        pw.print(": ");
        pw.print(jobStatus.getSourcePackageName());
        pw.print((jobStatus.satisfiedConstraints & 33554432) != 0 ? " RUNNABLE" : " WAITING");
        if (jobStatus.appHasDozeExemption) {
            pw.print(" WHITELISTED");
        }
        if (this.mAllowInIdleJobs.contains(jobStatus)) {
            pw.print(" ALLOWED_IN_DOZE");
        }
        pw.println();
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long token = proto.start(fieldId);
        long mToken = proto.start(1146756268037L);
        proto.write(1133871366145L, this.mDeviceIdleMode);
        this.mService.getJobStore().forEachJob(predicate, new java.util.function.Consumer() { // from class: com.android.server.job.controllers.DeviceIdleJobsController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$dumpControllerStateLocked$2(proto, (com.android.server.job.controllers.JobStatus) obj);
            }
        });
        proto.end(mToken);
        proto.end(token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$2(android.util.proto.ProtoOutputStream proto, com.android.server.job.controllers.JobStatus jobStatus) {
        long jsToken = proto.start(2246267895810L);
        jobStatus.writeToShortProto(proto, 1146756268033L);
        proto.write(1120986464258L, jobStatus.getSourceUid());
        proto.write(1138166333443L, jobStatus.getSourcePackageName());
        proto.write(1133871366148L, (jobStatus.satisfiedConstraints & 33554432) != 0);
        proto.write(1133871366149L, jobStatus.appHasDozeExemption);
        proto.write(1133871366150L, this.mAllowInIdleJobs.contains(jobStatus));
        proto.end(jsToken);
    }

    final class DeviceIdleUpdateFunctor implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
        final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mChangedJobs = new android.util.ArraySet<>();
        long mUpdateTimeElapsed = 0;

        DeviceIdleUpdateFunctor() {
        }

        void prepare() {
            this.mChangedJobs.clear();
            this.mUpdateTimeElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.JobStatus jobStatus) {
            if (com.android.server.job.controllers.DeviceIdleJobsController.this.updateTaskStateLocked(jobStatus, this.mUpdateTimeElapsed)) {
                this.mChangedJobs.add(jobStatus);
            }
        }
    }

    final class DeviceIdleJobsDelayHandler extends android.os.Handler {
        public DeviceIdleJobsDelayHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    synchronized (com.android.server.job.controllers.DeviceIdleJobsController.this.mLock) {
                        com.android.server.job.controllers.DeviceIdleJobsController.this.mDeviceIdleUpdateFunctor.prepare();
                        com.android.server.job.controllers.DeviceIdleJobsController.this.mService.getJobStore().forEachJob(com.android.server.job.controllers.DeviceIdleJobsController.this.mDeviceIdleUpdateFunctor);
                        if (com.android.server.job.controllers.DeviceIdleJobsController.this.mDeviceIdleUpdateFunctor.mChangedJobs.size() > 0) {
                            com.android.server.job.controllers.DeviceIdleJobsController.this.mStateChangedListener.onControllerStateChanged(com.android.server.job.controllers.DeviceIdleJobsController.this.mDeviceIdleUpdateFunctor.mChangedJobs);
                        }
                        break;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
