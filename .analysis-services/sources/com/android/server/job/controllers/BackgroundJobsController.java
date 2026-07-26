package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class BackgroundJobsController extends com.android.server.job.controllers.StateController {
    private static final boolean DEBUG;
    static final int KNOWN_ACTIVE = 1;
    static final int KNOWN_INACTIVE = 2;
    private static final java.lang.String TAG = "JobScheduler.Background";
    static final int UNKNOWN = 0;
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final com.android.server.AppStateTrackerImpl mAppStateTracker;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final com.android.server.AppStateTrackerImpl.Listener mForceAppStandbyListener;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final android.util.SparseArrayMap<java.lang.String, java.lang.Boolean> mPackageStoppedState;
    private final com.android.server.job.controllers.BackgroundJobsController.UpdateJobFunctor mUpdateJobFunctor;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    public BackgroundJobsController(com.android.server.job.JobSchedulerService service) {
        super(service);
        this.mPackageStoppedState = new android.util.SparseArrayMap<>();
        this.mUpdateJobFunctor = new com.android.server.job.controllers.BackgroundJobsController.UpdateJobFunctor();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.job.controllers.BackgroundJobsController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                java.lang.String pkgName = com.android.server.job.JobSchedulerService.getPackageName(intent);
                byte b = -1;
                int pkgUid = intent.getIntExtra("android.intent.extra.UID", -1);
                java.lang.String action = intent.getAction();
                if (pkgUid == -1) {
                    android.util.Slog.e(com.android.server.job.controllers.BackgroundJobsController.TAG, "Didn't get package UID in intent (" + action + ")");
                    return;
                }
                if (com.android.server.job.controllers.BackgroundJobsController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.BackgroundJobsController.TAG, "Got " + action + " for " + pkgUid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pkgName);
                }
                switch (action.hashCode()) {
                    case -757780528:
                        if (action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                            b = 0;
                        }
                        break;
                    case 928080374:
                        if (action.equals("android.intent.action.PACKAGE_UNSTOPPED")) {
                            b = 1;
                        }
                        break;
                }
                switch (b) {
                    case 0:
                        synchronized (com.android.server.job.controllers.BackgroundJobsController.this.mLock) {
                            com.android.server.job.controllers.BackgroundJobsController.this.mPackageStoppedState.delete(pkgUid, pkgName);
                            com.android.server.job.controllers.BackgroundJobsController.this.updateJobRestrictionsForUidLocked(pkgUid, false);
                            break;
                        }
                        return;
                    case 1:
                        synchronized (com.android.server.job.controllers.BackgroundJobsController.this.mLock) {
                            com.android.server.job.controllers.BackgroundJobsController.this.mPackageStoppedState.add(pkgUid, pkgName, java.lang.Boolean.FALSE);
                            com.android.server.job.controllers.BackgroundJobsController.this.updateJobRestrictionsLocked(pkgUid, 0);
                            break;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mForceAppStandbyListener = new com.android.server.AppStateTrackerImpl.Listener() { // from class: com.android.server.job.controllers.BackgroundJobsController.2
            @Override // com.android.server.AppStateTrackerImpl.Listener
            public void updateAllJobs() {
                synchronized (com.android.server.job.controllers.BackgroundJobsController.this.mLock) {
                    com.android.server.job.controllers.BackgroundJobsController.this.updateAllJobRestrictionsLocked();
                }
            }

            @Override // com.android.server.AppStateTrackerImpl.Listener
            public void updateJobsForUid(int uid, boolean isActive) {
                synchronized (com.android.server.job.controllers.BackgroundJobsController.this.mLock) {
                    com.android.server.job.controllers.BackgroundJobsController.this.updateJobRestrictionsForUidLocked(uid, isActive);
                }
            }

            @Override // com.android.server.AppStateTrackerImpl.Listener
            public void updateJobsForUidPackage(int uid, java.lang.String packageName, boolean isActive) {
                synchronized (com.android.server.job.controllers.BackgroundJobsController.this.mLock) {
                    com.android.server.job.controllers.BackgroundJobsController.this.updateJobRestrictionsForUidLocked(uid, isActive);
                }
            }
        };
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
        this.mAppStateTracker = (com.android.server.AppStateTrackerImpl) java.util.Objects.requireNonNull((com.android.server.AppStateTracker) com.android.server.LocalServices.getService(com.android.server.AppStateTracker.class));
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
    }

    @Override // com.android.server.job.controllers.StateController
    public void startTrackingLocked() {
        this.mAppStateTracker.addListener(this.mForceAppStandbyListener);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_RESTARTED");
        filter.addAction("android.intent.action.PACKAGE_UNSTOPPED");
        filter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, filter, null, null);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus lastJob) {
        updateSingleJobRestrictionLocked(jobStatus, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis(), 0);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob) {
    }

    @Override // com.android.server.job.controllers.StateController
    public void evaluateStateLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.isRequestedExpeditedJob()) {
            updateSingleJobRestrictionLocked(jobStatus, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis(), 0);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onAppRemovedLocked(java.lang.String packageName, int uid) {
        this.mPackageStoppedState.delete(uid, packageName);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserRemovedLocked(int userId) {
        for (int u = this.mPackageStoppedState.numMaps() - 1; u >= 0; u--) {
            int uid = this.mPackageStoppedState.keyAt(u);
            if (android.os.UserHandle.getUserId(uid) == userId) {
                this.mPackageStoppedState.deleteAt(u);
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        pw.println("Aconfig flags:");
        pw.increaseIndent();
        pw.print("android.content.pm.stay_stopped", java.lang.Boolean.valueOf(com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.stayStopped()));
        pw.println();
        pw.decreaseIndent();
        pw.println();
        this.mAppStateTracker.dump(pw);
        pw.println();
        pw.println("Stopped packages:");
        pw.increaseIndent();
        this.mPackageStoppedState.forEach(new android.util.SparseArrayMap.TriConsumer() { // from class: com.android.server.job.controllers.BackgroundJobsController$$ExternalSyntheticLambda0
            public final void accept(int i, java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.job.controllers.BackgroundJobsController.lambda$dumpControllerStateLocked$0(pw, i, (java.lang.String) obj, (java.lang.Boolean) obj2);
            }
        });
        pw.println();
        this.mService.getJobStore().forEachJob(predicate, new java.util.function.Consumer() { // from class: com.android.server.job.controllers.BackgroundJobsController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$dumpControllerStateLocked$1(pw, (com.android.server.job.controllers.JobStatus) obj);
            }
        });
    }

    static /* synthetic */ void lambda$dumpControllerStateLocked$0(android.util.IndentingPrintWriter pw, int uid, java.lang.String pkgName, java.lang.Boolean isStopped) {
        pw.print(uid);
        pw.print(":");
        pw.print(pkgName);
        pw.print("=");
        pw.println(isStopped);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpControllerStateLocked$1(android.util.IndentingPrintWriter pw, com.android.server.job.controllers.JobStatus jobStatus) {
        int uid = jobStatus.getSourceUid();
        java.lang.String sourcePkg = jobStatus.getSourcePackageName();
        pw.print("#");
        jobStatus.printUniqueId(pw);
        pw.print(" from ");
        android.os.UserHandle.formatUid(pw, uid);
        pw.print(this.mAppStateTracker.isUidActive(uid) ? " active" : " idle");
        if (this.mAppStateTracker.isUidPowerSaveExempt(uid) || this.mAppStateTracker.isUidTempPowerSaveExempt(uid)) {
            pw.print(", exempted");
        }
        pw.print(": ");
        pw.print(sourcePkg);
        pw.print(" [RUN_ANY_IN_BACKGROUND ");
        pw.print(this.mAppStateTracker.isRunAnyInBackgroundAppOpsAllowed(uid, sourcePkg) ? "allowed]" : "disallowed]");
        if ((jobStatus.satisfiedConstraints & 4194304) != 0) {
            pw.println(" RUNNABLE");
        } else {
            pw.println(" WAITING");
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long token = proto.start(fieldId);
        long mToken = proto.start(1146756268033L);
        this.mAppStateTracker.dumpProto(proto, 1146756268033L);
        this.mService.getJobStore().forEachJob(predicate, new java.util.function.Consumer() { // from class: com.android.server.job.controllers.BackgroundJobsController$$ExternalSyntheticLambda2
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
        int sourceUid = jobStatus.getSourceUid();
        proto.write(1120986464258L, sourceUid);
        java.lang.String sourcePkg = jobStatus.getSourcePackageName();
        proto.write(1138166333443L, sourcePkg);
        proto.write(1133871366148L, this.mAppStateTracker.isUidActive(sourceUid));
        proto.write(1133871366149L, this.mAppStateTracker.isUidPowerSaveExempt(sourceUid) || this.mAppStateTracker.isUidTempPowerSaveExempt(sourceUid));
        proto.write(1133871366150L, this.mAppStateTracker.isRunAnyInBackgroundAppOpsAllowed(sourceUid, sourcePkg));
        proto.write(1133871366151L, (jobStatus.satisfiedConstraints & 4194304) != 0);
        proto.end(jsToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllJobRestrictionsLocked() {
        updateJobRestrictionsLocked(-1, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateJobRestrictionsForUidLocked(int uid, boolean isActive) {
        updateJobRestrictionsLocked(uid, isActive ? 1 : 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateJobRestrictionsLocked(int filterUid, int newActiveState) {
        this.mUpdateJobFunctor.prepare(newActiveState);
        long start = DEBUG ? android.os.SystemClock.elapsedRealtimeNanos() : 0L;
        com.android.server.job.JobStore store = this.mService.getJobStore();
        if (filterUid > 0) {
            store.forEachJobForSourceUid(filterUid, this.mUpdateJobFunctor);
        } else {
            store.forEachJob(this.mUpdateJobFunctor);
        }
        long time = DEBUG ? android.os.SystemClock.elapsedRealtimeNanos() - start : 0L;
        if (DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("Job status updated: %d/%d checked/total jobs, %d us", java.lang.Integer.valueOf(this.mUpdateJobFunctor.mCheckedCount), java.lang.Integer.valueOf(this.mUpdateJobFunctor.mTotalCount), java.lang.Long.valueOf(time / 1000)));
        }
        if (this.mUpdateJobFunctor.mChangedJobs.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(this.mUpdateJobFunctor.mChangedJobs);
        }
    }

    private boolean isPackageStoppedLocked(java.lang.String packageName, int uid) {
        if (this.mPackageStoppedState.contains(uid, packageName)) {
            return ((java.lang.Boolean) this.mPackageStoppedState.get(uid, packageName)).booleanValue();
        }
        try {
            boolean isStopped = this.mPackageManagerInternal.isPackageStopped(packageName, uid);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Pulled stopped state of " + packageName + " (" + uid + "): " + isStopped);
            }
            this.mPackageStoppedState.add(uid, packageName, java.lang.Boolean.valueOf(isStopped));
            return isStopped;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Couldn't determine stopped state for unknown package: " + packageName);
            return false;
        }
    }

    boolean updateSingleJobRestrictionLocked(com.android.server.job.controllers.JobStatus jobStatus, long nowElapsed, int activeState) {
        boolean isCallingPkgStopped;
        int uid = jobStatus.getSourceUid();
        java.lang.String packageName = jobStatus.getSourcePackageName();
        boolean isSourcePkgStopped = isPackageStoppedLocked(jobStatus.getSourcePackageName(), jobStatus.getSourceUid());
        if (!jobStatus.isProxyJob()) {
            isCallingPkgStopped = isSourcePkgStopped;
        } else {
            isCallingPkgStopped = isPackageStoppedLocked(jobStatus.getCallingPackageName(), jobStatus.getUid());
        }
        boolean isActive = false;
        boolean isStopped = com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.stayStopped() && (isCallingPkgStopped || isSourcePkgStopped);
        boolean isUserBgRestricted = isStopped || !(this.mActivityManagerInternal.isBgAutoRestrictedBucketFeatureFlagEnabled() || this.mAppStateTracker.isRunAnyInBackgroundAppOpsAllowed(uid, packageName));
        boolean shouldStopImmediately = jobStatus.startedWithForegroundFlag && isUserBgRestricted && this.mService.getUidProcState(uid) > 5;
        boolean canRun = (isStopped || shouldStopImmediately || this.mAppStateTracker.areJobsRestricted(uid, packageName, jobStatus.canRunInBatterySaver())) ? false : true;
        if (activeState == 0) {
            isActive = this.mAppStateTracker.isUidActive(uid);
        } else if (activeState == 1) {
            isActive = true;
        }
        if (isActive && jobStatus.getStandbyBucket() == 4) {
            jobStatus.maybeLogBucketMismatch();
        }
        boolean didChange = jobStatus.setBackgroundNotRestrictedConstraintSatisfied(nowElapsed, canRun, isUserBgRestricted);
        return didChange | jobStatus.setUidActive(isActive);
    }

    private final class UpdateJobFunctor implements java.util.function.Consumer<com.android.server.job.controllers.JobStatus> {
        int mActiveState;
        final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mChangedJobs;
        int mCheckedCount;
        int mTotalCount;
        long mUpdateTimeElapsed;

        private UpdateJobFunctor() {
            this.mChangedJobs = new android.util.ArraySet<>();
            this.mTotalCount = 0;
            this.mCheckedCount = 0;
            this.mUpdateTimeElapsed = 0L;
        }

        void prepare(int newActiveState) {
            this.mActiveState = newActiveState;
            this.mUpdateTimeElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            this.mChangedJobs.clear();
            this.mTotalCount = 0;
            this.mCheckedCount = 0;
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.job.controllers.JobStatus jobStatus) {
            this.mTotalCount++;
            this.mCheckedCount++;
            if (com.android.server.job.controllers.BackgroundJobsController.this.updateSingleJobRestrictionLocked(jobStatus, this.mUpdateTimeElapsed, this.mActiveState)) {
                this.mChangedJobs.add(jobStatus);
            }
        }
    }
}
