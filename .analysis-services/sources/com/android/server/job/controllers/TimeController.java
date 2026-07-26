package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class TimeController extends com.android.server.job.controllers.StateController {
    private static final boolean DEBUG;
    static final long DELAY_COALESCE_TIME_MS = 30000;
    private static final java.lang.String TAG = "JobScheduler.Time";
    private final java.lang.String DEADLINE_TAG;
    private final java.lang.String DELAY_TAG;
    private android.app.AlarmManager mAlarmService;
    private final android.app.AlarmManager.OnAlarmListener mDeadlineExpiredListener;
    private volatile long mLastFiredDelayExpiredElapsedMillis;
    private long mNextDelayExpiredElapsedMillis;
    private final android.app.AlarmManager.OnAlarmListener mNextDelayExpiredListener;
    private long mNextJobExpiredElapsedMillis;
    private final java.util.PriorityQueue<com.android.server.job.controllers.JobStatus> mTrackedJobs;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    public TimeController(com.android.server.job.JobSchedulerService service) {
        super(service);
        this.DEADLINE_TAG = "*job.deadline*";
        this.DELAY_TAG = "*job.delay*";
        this.mAlarmService = null;
        this.mTrackedJobs = new java.util.PriorityQueue<>(new java.util.Comparator<com.android.server.job.controllers.JobStatus>() { // from class: com.android.server.job.controllers.TimeController.1
            @Override // java.util.Comparator
            public int compare(com.android.server.job.controllers.JobStatus left, com.android.server.job.controllers.JobStatus right) {
                return java.lang.Long.compare(left.getLatestRunTimeElapsed(), right.getLatestRunTimeElapsed());
            }
        });
        this.mDeadlineExpiredListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.job.controllers.TimeController.2
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                if (com.android.server.job.controllers.TimeController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.TimeController.TAG, "Deadline-expired alarm fired");
                }
                com.android.server.job.controllers.TimeController.this.checkExpiredDeadlinesAndResetAlarm();
            }
        };
        this.mNextDelayExpiredListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.job.controllers.TimeController.3
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                if (com.android.server.job.controllers.TimeController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.TimeController.TAG, "Delay-expired alarm fired");
                }
                com.android.server.job.controllers.TimeController.this.mLastFiredDelayExpiredElapsedMillis = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                com.android.server.job.controllers.TimeController.this.checkExpiredDelaysAndResetAlarm();
            }
        };
        this.mNextJobExpiredElapsedMillis = Long.MAX_VALUE;
        this.mNextDelayExpiredElapsedMillis = Long.MAX_VALUE;
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus job, com.android.server.job.controllers.JobStatus lastJob) {
        if (job.hasTimingDelayConstraint() || job.hasDeadlineConstraint()) {
            maybeStopTrackingJobLocked(job, null);
            if (this.mService.getWrapper().getExtImpl() != null && this.mService.getWrapper().getExtImpl().isProxyJob(job, "maybeStartTrackingJobLocked")) {
                return;
            }
            long nowElapsedMillis = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (job.hasDeadlineConstraint() && evaluateDeadlineConstraint(job, nowElapsedMillis)) {
                return;
            }
            if (job.hasTimingDelayConstraint() && evaluateTimingDelayConstraint(job, nowElapsedMillis) && !job.hasDeadlineConstraint()) {
                return;
            }
            this.mTrackedJobs.add(job);
            job.setTrackingController(32);
            android.os.WorkSource ws = this.mService.deriveWorkSource(job.getSourceUid(), job.getSourcePackageName());
            if (job.hasTimingDelayConstraint() && wouldBeReadyWithConstraintLocked(job, Integer.MIN_VALUE)) {
                maybeUpdateDelayAlarmLocked(job.getEarliestRunTime(), ws);
            }
            if (job.hasDeadlineConstraint() && wouldBeReadyWithConstraintLocked(job, 1073741824)) {
                maybeUpdateDeadlineAlarmLocked(job.getLatestRunTimeElapsed(), ws);
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus job, com.android.server.job.controllers.JobStatus incomingJob) {
        if (job.clearTrackingController(32) && this.mTrackedJobs.remove(job)) {
            checkExpiredDelaysAndResetAlarm();
            checkExpiredDeadlinesAndResetAlarm();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void evaluateStateLocked(com.android.server.job.controllers.JobStatus job) {
        long nowElapsedMillis = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (job.hasDeadlineConstraint() && !job.isConstraintSatisfied(1073741824) && job.getLatestRunTimeElapsed() <= this.mNextJobExpiredElapsedMillis) {
            if (evaluateDeadlineConstraint(job, nowElapsedMillis)) {
                if (job.isReady()) {
                    this.mStateChangedListener.onRunJobNow(job);
                }
                this.mTrackedJobs.remove(job);
                com.android.modules.expresslog.Counter.logIncrement("job_scheduler.value_job_scheduler_job_deadline_expired_counter");
            } else if (wouldBeReadyWithConstraintLocked(job, 1073741824)) {
                setDeadlineExpiredAlarmLocked(job.getLatestRunTimeElapsed(), this.mService.deriveWorkSource(job.getSourceUid(), job.getSourcePackageName()));
            }
        }
        if (job.hasTimingDelayConstraint() && !job.isConstraintSatisfied(Integer.MIN_VALUE) && job.getEarliestRunTime() <= this.mNextDelayExpiredElapsedMillis) {
            if (evaluateTimingDelayConstraint(job, nowElapsedMillis)) {
                if (canStopTrackingJobLocked(job)) {
                    this.mTrackedJobs.remove(job);
                }
            } else if (wouldBeReadyWithConstraintLocked(job, Integer.MIN_VALUE)) {
                setDelayExpiredAlarmLocked(job.getEarliestRunTime(), this.mService.deriveWorkSource(job.getSourceUid(), job.getSourcePackageName()));
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void reevaluateStateLocked(int uid) {
        checkExpiredDeadlinesAndResetAlarm();
        checkExpiredDelaysAndResetAlarm();
    }

    private boolean canStopTrackingJobLocked(com.android.server.job.controllers.JobStatus job) {
        return (!job.hasTimingDelayConstraint() || job.isConstraintSatisfied(Integer.MIN_VALUE)) && (!job.hasDeadlineConstraint() || job.isConstraintSatisfied(1073741824));
    }

    private void ensureAlarmServiceLocked() {
        if (this.mAlarmService == null) {
            this.mAlarmService = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        }
    }

    void checkExpiredDeadlinesAndResetAlarm() {
        synchronized (this.mLock) {
            long nextExpiryTime = Long.MAX_VALUE;
            int nextExpiryUid = 0;
            java.lang.String nextExpiryPackageName = null;
            long nowElapsedMillis = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            java.util.Iterator<com.android.server.job.controllers.JobStatus> it = this.mTrackedJobs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.job.controllers.JobStatus job = it.next();
                if (job.hasDeadlineConstraint()) {
                    if (evaluateDeadlineConstraint(job, nowElapsedMillis)) {
                        if (job.isReady()) {
                            this.mStateChangedListener.onRunJobNow(job);
                        }
                        com.android.modules.expresslog.Counter.logIncrement("job_scheduler.value_job_scheduler_job_deadline_expired_counter");
                        it.remove();
                    } else if (!wouldBeReadyWithConstraintLocked(job, 1073741824)) {
                        if (DEBUG) {
                            android.util.Slog.i(TAG, "Skipping " + job + " because deadline won't make it ready.");
                        }
                    } else {
                        nextExpiryTime = job.getLatestRunTimeElapsed();
                        nextExpiryUid = job.getSourceUid();
                        nextExpiryPackageName = job.getSourcePackageName();
                        break;
                    }
                }
            }
            setDeadlineExpiredAlarmLocked(nextExpiryTime, this.mService.deriveWorkSource(nextExpiryUid, nextExpiryPackageName));
        }
    }

    private boolean evaluateDeadlineConstraint(com.android.server.job.controllers.JobStatus job, long nowElapsedMillis) {
        long jobDeadline = job.getLatestRunTimeElapsed();
        if (jobDeadline <= nowElapsedMillis) {
            if (job.hasTimingDelayConstraint()) {
                job.setTimingDelayConstraintSatisfied(nowElapsedMillis, true);
            }
            job.setDeadlineConstraintSatisfied(nowElapsedMillis, true);
            return true;
        }
        return false;
    }

    void checkExpiredDelaysAndResetAlarm() {
        synchronized (this.mLock) {
            long nextDelayTime = Long.MAX_VALUE;
            int nextDelayUid = 0;
            java.lang.String nextDelayPackageName = null;
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
            java.util.Iterator<com.android.server.job.controllers.JobStatus> it = this.mTrackedJobs.iterator();
            long nowElapsedMillis = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            while (it.hasNext()) {
                com.android.server.job.controllers.JobStatus job = it.next();
                if (job != null && job.hasTimingDelayConstraint()) {
                    if (evaluateTimingDelayConstraint(job, nowElapsedMillis)) {
                        if (canStopTrackingJobLocked(job)) {
                            it.remove();
                        }
                        changedJobs.add(job);
                    } else if (!wouldBeReadyWithConstraintLocked(job, Integer.MIN_VALUE)) {
                        if (DEBUG) {
                            android.util.Slog.i(TAG, "Skipping " + job + " because delay won't make it ready.");
                        }
                    } else {
                        long jobDelayTime = job.getEarliestRunTime();
                        if (nextDelayTime > jobDelayTime) {
                            nextDelayTime = jobDelayTime;
                            nextDelayUid = job.getSourceUid();
                            nextDelayPackageName = job.getSourcePackageName();
                        }
                    }
                }
            }
            if (changedJobs.size() > 0) {
                this.mStateChangedListener.onControllerStateChanged(changedJobs);
            }
            setDelayExpiredAlarmLocked(nextDelayTime, this.mService.deriveWorkSource(nextDelayUid, nextDelayPackageName));
        }
    }

    private boolean evaluateTimingDelayConstraint(com.android.server.job.controllers.JobStatus job, long nowElapsedMillis) {
        long jobDelayTime = job.getEarliestRunTime();
        if (jobDelayTime <= nowElapsedMillis) {
            job.setTimingDelayConstraintSatisfied(nowElapsedMillis, true);
            return true;
        }
        return false;
    }

    private void maybeUpdateDelayAlarmLocked(long delayExpiredElapsed, android.os.WorkSource ws) {
        if (delayExpiredElapsed < this.mNextDelayExpiredElapsedMillis) {
            setDelayExpiredAlarmLocked(delayExpiredElapsed, ws);
        }
    }

    private void maybeUpdateDeadlineAlarmLocked(long deadlineExpiredElapsed, android.os.WorkSource ws) {
        if (deadlineExpiredElapsed < this.mNextJobExpiredElapsedMillis) {
            setDeadlineExpiredAlarmLocked(deadlineExpiredElapsed, ws);
        }
    }

    private void setDelayExpiredAlarmLocked(long alarmTimeElapsedMillis, android.os.WorkSource ws) {
        long alarmTimeElapsedMillis2 = maybeAdjustAlarmTime(java.lang.Math.max(alarmTimeElapsedMillis, this.mLastFiredDelayExpiredElapsedMillis + 30000));
        if (this.mNextDelayExpiredElapsedMillis == alarmTimeElapsedMillis2) {
            return;
        }
        this.mNextDelayExpiredElapsedMillis = alarmTimeElapsedMillis2;
        updateAlarmWithListenerLocked("*job.delay*", 3, this.mNextDelayExpiredListener, this.mNextDelayExpiredElapsedMillis, ws);
    }

    private void setDeadlineExpiredAlarmLocked(long alarmTimeElapsedMillis, android.os.WorkSource ws) {
        long alarmTimeElapsedMillis2 = maybeAdjustAlarmTime(alarmTimeElapsedMillis);
        if (this.mNextJobExpiredElapsedMillis == alarmTimeElapsedMillis2) {
            return;
        }
        this.mNextJobExpiredElapsedMillis = alarmTimeElapsedMillis2;
        updateAlarmWithListenerLocked("*job.deadline*", 2, this.mDeadlineExpiredListener, this.mNextJobExpiredElapsedMillis, ws);
    }

    private long maybeAdjustAlarmTime(long proposedAlarmTimeElapsedMillis) {
        return java.lang.Math.max(proposedAlarmTimeElapsedMillis, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
    }

    private void updateAlarmWithListenerLocked(java.lang.String tag, int alarmType, android.app.AlarmManager.OnAlarmListener listener, long alarmTimeElapsed, android.os.WorkSource ws) {
        ensureAlarmServiceLocked();
        if (alarmTimeElapsed == Long.MAX_VALUE) {
            this.mAlarmService.cancel(listener);
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Setting " + tag + " for: " + alarmTimeElapsed);
        }
        this.mAlarmService.set(alarmType, alarmTimeElapsed, -1L, 0L, tag, listener, com.android.server.AppSchedulingModuleThread.getHandler(), ws);
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        pw.println("Elapsed clock: " + nowElapsed);
        pw.print("Next delay alarm in ");
        android.util.TimeUtils.formatDuration(this.mNextDelayExpiredElapsedMillis, nowElapsed, pw);
        pw.println();
        pw.print("Last delay alarm fired @ ");
        android.util.TimeUtils.formatDuration(nowElapsed, this.mLastFiredDelayExpiredElapsedMillis, pw);
        pw.println();
        pw.print("Next deadline alarm in ");
        android.util.TimeUtils.formatDuration(this.mNextJobExpiredElapsedMillis, nowElapsed, pw);
        pw.println();
        pw.println();
        for (com.android.server.job.controllers.JobStatus ts : this.mTrackedJobs) {
            if (predicate.test(ts)) {
                pw.print("#");
                ts.printUniqueId(pw);
                pw.print(" from ");
                android.os.UserHandle.formatUid(pw, ts.getSourceUid());
                pw.print(": Delay=");
                if (ts.hasTimingDelayConstraint()) {
                    android.util.TimeUtils.formatDuration(ts.getEarliestRunTime(), nowElapsed, pw);
                } else {
                    pw.print("N/A");
                }
                pw.print(", Deadline=");
                if (ts.hasDeadlineConstraint()) {
                    android.util.TimeUtils.formatDuration(ts.getLatestRunTimeElapsed(), nowElapsed, pw);
                } else {
                    pw.print("N/A");
                }
                pw.println();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long token = proto.start(fieldId);
        long mToken = proto.start(1146756268040L);
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        proto.write(1112396529665L, nowElapsed);
        proto.write(1112396529666L, this.mNextDelayExpiredElapsedMillis - nowElapsed);
        proto.write(1112396529667L, this.mNextJobExpiredElapsedMillis - nowElapsed);
        for (com.android.server.job.controllers.JobStatus ts : this.mTrackedJobs) {
            if (predicate.test(ts)) {
                long tsToken = proto.start(2246267895812L);
                ts.writeToShortProto(proto, 1146756268033L);
                proto.write(1133871366147L, ts.hasTimingDelayConstraint());
                proto.write(1112396529668L, ts.getEarliestRunTime() - nowElapsed);
                proto.write(1133871366149L, ts.hasDeadlineConstraint());
                proto.write(1112396529670L, ts.getLatestRunTimeElapsed() - nowElapsed);
                proto.end(tsToken);
                token = token;
            }
        }
        proto.end(mToken);
        proto.end(token);
    }
}
