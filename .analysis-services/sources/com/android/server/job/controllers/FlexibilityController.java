package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public final class FlexibilityController extends com.android.server.job.controllers.StateController {
    private static final boolean DEBUG;
    static final int FLEXIBLE_CONSTRAINTS = 268435463;
    private static final int JOB_SPECIFIC_FLEXIBLE_CONSTRAINTS = 268435456;
    private static final int MSG_CHECK_ALL_JOBS = 0;
    private static final int MSG_CHECK_JOBS = 1;
    private static final int MSG_CHECK_PACKAGES = 2;
    private static final long NO_LIFECYCLE_END = Long.MAX_VALUE;
    static final int SYSTEM_WIDE_FLEXIBLE_CONSTRAINTS = 7;
    private static final java.lang.String TAG = "JobScheduler.Flex";
    private int mAppliedConstraints;
    private long mDeadlineProximityLimitMs;
    private android.util.SparseLongArray mFallbackFlexibilityAdditionalScoreTimeFactors;
    private long mFallbackFlexibilityDeadlineMs;
    private android.util.SparseIntArray mFallbackFlexibilityDeadlineScores;
    private android.util.SparseLongArray mFallbackFlexibilityDeadlines;
    final com.android.server.job.controllers.FlexibilityController.FcConfig mFcConfig;
    final com.android.server.job.controllers.FlexibilityController.FlexibilityAlarmQueue mFlexibilityAlarmQueue;
    private boolean mFlexibilityEnabled;
    final com.android.server.job.controllers.FlexibilityController.FlexibilityTracker mFlexibilityTracker;
    private final com.android.server.job.controllers.FlexibilityController.FcHandler mHandler;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.job.controllers.FlexibilityController.JobScoreTracker> mJobScoreTrackers;
    private final android.util.ArraySet<com.android.server.job.controllers.JobStatus> mJobsToCheck;
    private final android.util.SparseLongArray mLastSeenConstraintTimesElapsed;
    private boolean mLocalOverride;
    private long mMaxRescheduledDeadline;
    private long mMinTimeBetweenFlexibilityAlarmsMs;
    private final android.util.ArraySet<java.lang.String> mPackagesToCheck;
    private android.util.SparseArray<int[]> mPercentsToDropConstraints;
    final com.android.server.job.controllers.PrefetchController.PrefetchChangedListener mPrefetchChangedListener;
    final com.android.server.job.controllers.PrefetchController mPrefetchController;
    final android.util.SparseArrayMap<java.lang.String, java.lang.Long> mPrefetchLifeCycleStart;
    private long mRescheduledJobDeadline;
    int mSatisfiedFlexibleConstraints;
    private final com.android.server.job.controllers.FlexibilityController.SpecialAppTracker mSpecialAppTracker;
    private final int mSupportedFlexConstraints;
    private long mUnseenConstraintGracePeriodMs;

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class JobScoreTracker {
        private static final long MAX_TIME_WINDOW_MS = 86400000;
        private static final int NUM_SCORE_BUCKETS = 24;
        private int mCachedScore;
        private long mCachedScoreExpirationTimeElapsed;
        private int mScoreBucketIndex;
        private final com.android.server.job.controllers.FlexibilityController.JobScoreTracker.JobScoreBucket[] mScoreBuckets;

        private static class JobScoreBucket {
            public int score;
            public long startTimeElapsed;

            private JobScoreBucket() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void reset() {
                this.startTimeElapsed = 0L;
                this.score = 0;
            }
        }

        private JobScoreTracker() {
            this.mScoreBuckets = new com.android.server.job.controllers.FlexibilityController.JobScoreTracker.JobScoreBucket[24];
            this.mScoreBucketIndex = 0;
        }

        public void addScore(int add, long nowElapsed) {
            com.android.server.job.controllers.FlexibilityController.JobScoreTracker.JobScoreBucket bucket = this.mScoreBuckets[this.mScoreBucketIndex];
            if (bucket == null) {
                bucket = new com.android.server.job.controllers.FlexibilityController.JobScoreTracker.JobScoreBucket();
                bucket.startTimeElapsed = nowElapsed;
                this.mScoreBuckets[this.mScoreBucketIndex] = bucket;
                this.mCachedScoreExpirationTimeElapsed = java.lang.Math.min(this.mCachedScoreExpirationTimeElapsed, 86400000 + nowElapsed);
            } else if (bucket.startTimeElapsed < nowElapsed - 86400000) {
                bucket.reset();
                bucket.startTimeElapsed = nowElapsed;
                this.mCachedScoreExpirationTimeElapsed = nowElapsed;
            } else if (bucket.startTimeElapsed < nowElapsed - 3600000) {
                this.mScoreBucketIndex = (this.mScoreBucketIndex + 1) % 24;
                addScore(add, nowElapsed);
                return;
            }
            bucket.score += add;
            this.mCachedScore += add;
        }

        public int getScore(long nowElapsed) {
            if (nowElapsed < this.mCachedScoreExpirationTimeElapsed) {
                return this.mCachedScore;
            }
            int score = 0;
            long earliestElapsed = nowElapsed - 86400000;
            long earliestValidBucketTimeElapsed = Long.MAX_VALUE;
            for (com.android.server.job.controllers.FlexibilityController.JobScoreTracker.JobScoreBucket bucket : this.mScoreBuckets) {
                if (bucket != null && bucket.startTimeElapsed >= earliestElapsed) {
                    score += bucket.score;
                    if (earliestValidBucketTimeElapsed > bucket.startTimeElapsed) {
                        earliestValidBucketTimeElapsed = bucket.startTimeElapsed;
                    }
                }
            }
            this.mCachedScore = score;
            this.mCachedScoreExpirationTimeElapsed = 86400000 + earliestValidBucketTimeElapsed;
            return score;
        }

        public void dump(android.util.IndentingPrintWriter pw, long nowElapsed) {
            pw.print("{");
            boolean printed = false;
            for (int x = 0; x < this.mScoreBuckets.length; x++) {
                int idx = ((this.mScoreBucketIndex + 1) + x) % this.mScoreBuckets.length;
                com.android.server.job.controllers.FlexibilityController.JobScoreTracker.JobScoreBucket jsb = this.mScoreBuckets[idx];
                if (jsb != null && jsb.startTimeElapsed != 0) {
                    if (printed) {
                        pw.print(", ");
                    }
                    android.util.TimeUtils.formatDuration(jsb.startTimeElapsed, nowElapsed, pw);
                    pw.print("=");
                    pw.print(jsb.score);
                    printed = true;
                }
            }
            pw.print("}");
        }
    }

    public FlexibilityController(com.android.server.job.JobSchedulerService service, com.android.server.job.controllers.PrefetchController prefetchController) {
        super(service);
        this.mFallbackFlexibilityDeadlineMs = 86400000L;
        this.mFallbackFlexibilityDeadlines = com.android.server.job.controllers.FlexibilityController.FcConfig.DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES;
        this.mFallbackFlexibilityDeadlineScores = com.android.server.job.controllers.FlexibilityController.FcConfig.DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES;
        this.mFallbackFlexibilityAdditionalScoreTimeFactors = com.android.server.job.controllers.FlexibilityController.FcConfig.DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS;
        this.mRescheduledJobDeadline = 3600000L;
        this.mMaxRescheduledDeadline = 86400000L;
        this.mUnseenConstraintGracePeriodMs = 259200000L;
        this.mAppliedConstraints = 0;
        this.mMinTimeBetweenFlexibilityAlarmsMs = 60000L;
        this.mDeadlineProximityLimitMs = 900000L;
        this.mLastSeenConstraintTimesElapsed = new android.util.SparseLongArray();
        this.mPrefetchLifeCycleStart = new android.util.SparseArrayMap<>();
        this.mPrefetchChangedListener = new com.android.server.job.controllers.PrefetchController.PrefetchChangedListener() { // from class: com.android.server.job.controllers.FlexibilityController.1
            @Override // com.android.server.job.controllers.PrefetchController.PrefetchChangedListener
            public void onPrefetchCacheUpdated(android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs, int userId, java.lang.String pkgName, long prevEstimatedLaunchTime, long newEstimatedLaunchTime, long nowElapsed) throws java.lang.Throwable {
                synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                    try {
                        long prefetchThreshold = com.android.server.job.controllers.FlexibilityController.this.mPrefetchController.getLaunchTimeThresholdMs();
                        boolean jobWasInPrefetchWindow = prevEstimatedLaunchTime - prefetchThreshold < nowElapsed;
                        boolean jobIsInPrefetchWindow = newEstimatedLaunchTime - prefetchThreshold < nowElapsed;
                        if (jobIsInPrefetchWindow != jobWasInPrefetchWindow) {
                            com.android.server.job.controllers.FlexibilityController.this.mPrefetchLifeCycleStart.add(userId, pkgName, java.lang.Long.valueOf(java.lang.Math.max(nowElapsed, ((java.lang.Long) com.android.server.job.controllers.FlexibilityController.this.mPrefetchLifeCycleStart.getOrDefault(userId, pkgName, 0L)).longValue())));
                        }
                        for (int i = 0; i < jobs.size(); i++) {
                            try {
                                com.android.server.job.controllers.JobStatus js = jobs.valueAt(i);
                                if (js.hasFlexibilityConstraint()) {
                                    com.android.server.job.controllers.FlexibilityController.this.mFlexibilityTracker.calculateNumDroppedConstraints(js, nowElapsed);
                                    com.android.server.job.controllers.FlexibilityController.this.mFlexibilityAlarmQueue.scheduleDropNumConstraintsAlarm(js, nowElapsed);
                                }
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        };
        this.mJobScoreTrackers = new android.util.SparseArrayMap<>();
        this.mJobsToCheck = new android.util.ArraySet<>();
        this.mPackagesToCheck = new android.util.ArraySet<>();
        this.mHandler = new com.android.server.job.controllers.FlexibilityController.FcHandler(com.android.server.AppSchedulingModuleThread.get().getLooper());
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive") || this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
            this.mSupportedFlexConstraints = 0;
        } else {
            this.mSupportedFlexConstraints = FLEXIBLE_CONSTRAINTS;
        }
        this.mFlexibilityEnabled = (this.mAppliedConstraints & this.mSupportedFlexConstraints) != 0;
        this.mFlexibilityTracker = new com.android.server.job.controllers.FlexibilityController.FlexibilityTracker(java.lang.Integer.bitCount(this.mSupportedFlexConstraints));
        this.mFcConfig = new com.android.server.job.controllers.FlexibilityController.FcConfig();
        this.mFlexibilityAlarmQueue = new com.android.server.job.controllers.FlexibilityController.FlexibilityAlarmQueue(this.mContext, com.android.server.AppSchedulingModuleThread.get().getLooper());
        this.mPercentsToDropConstraints = com.android.server.job.controllers.FlexibilityController.FcConfig.DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS;
        this.mPrefetchController = prefetchController;
        this.mSpecialAppTracker = new com.android.server.job.controllers.FlexibilityController.SpecialAppTracker();
        if (this.mFlexibilityEnabled) {
            this.mSpecialAppTracker.startTracking();
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onSystemServicesReady() {
        this.mSpecialAppTracker.onSystemServicesReady();
    }

    @Override // com.android.server.job.controllers.StateController
    public void startTrackingLocked() {
        if (this.mFlexibilityEnabled) {
            this.mPrefetchController.registerPrefetchChangedListener(this.mPrefetchChangedListener);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus js, com.android.server.job.controllers.JobStatus lastJob) {
        if (js.hasFlexibilityConstraint()) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (this.mSupportedFlexConstraints == 0) {
                js.setFlexibilityConstraintSatisfied(nowElapsed, true);
                return;
            }
            js.setNumAppliedFlexibleConstraints(java.lang.Integer.bitCount(getRelevantAppliedConstraintsLocked(js)));
            js.setFlexibilityConstraintSatisfied(nowElapsed, isFlexibilitySatisfiedLocked(js));
            this.mFlexibilityTracker.add(js);
            js.setTrackingController(128);
            this.mFlexibilityAlarmQueue.scheduleDropNumConstraintsAlarm(js, nowElapsed);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.lastEvaluatedBias == 40) {
            return;
        }
        int priority = jobStatus.getJob().getPriority();
        int score = this.mFallbackFlexibilityDeadlineScores.get(priority, com.android.server.job.controllers.FlexibilityController.FcConfig.DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.get(priority, priority / 100));
        com.android.server.job.controllers.FlexibilityController.JobScoreTracker jobScoreTracker = (com.android.server.job.controllers.FlexibilityController.JobScoreTracker) this.mJobScoreTrackers.get(jobStatus.getSourceUid(), jobStatus.getSourcePackageName());
        if (jobScoreTracker == null) {
            jobScoreTracker = new com.android.server.job.controllers.FlexibilityController.JobScoreTracker();
            this.mJobScoreTrackers.add(jobStatus.getSourceUid(), jobStatus.getSourcePackageName(), jobScoreTracker);
        }
        jobScoreTracker.addScore(score, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
    }

    @Override // com.android.server.job.controllers.StateController
    public void unprepareFromExecutionLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        if (jobStatus.lastEvaluatedBias == 40) {
            return;
        }
        com.android.server.job.controllers.FlexibilityController.JobScoreTracker jobScoreTracker = (com.android.server.job.controllers.FlexibilityController.JobScoreTracker) this.mJobScoreTrackers.get(jobStatus.getSourceUid(), jobStatus.getSourcePackageName());
        if (jobScoreTracker == null) {
            android.util.Slog.e(TAG, "Unprepared a job that didn't result in a score change");
            return;
        }
        int priority = jobStatus.getJob().getPriority();
        int score = this.mFallbackFlexibilityDeadlineScores.get(priority, com.android.server.job.controllers.FlexibilityController.FcConfig.DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.get(priority, priority / 100));
        jobScoreTracker.addScore(-score, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis());
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus js, com.android.server.job.controllers.JobStatus incomingJob) {
        if (js.clearTrackingController(128)) {
            this.mFlexibilityAlarmQueue.removeAlarmForKey(js);
            this.mFlexibilityTracker.remove(js);
        }
        this.mJobsToCheck.remove(js);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onAppRemovedLocked(java.lang.String packageName, int uid) {
        int userId = android.os.UserHandle.getUserId(uid);
        this.mPrefetchLifeCycleStart.delete(userId, packageName);
        this.mJobScoreTrackers.delete(uid, packageName);
        this.mSpecialAppTracker.onAppRemoved(userId, packageName);
        for (int i = this.mJobsToCheck.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus js = this.mJobsToCheck.valueAt(i);
            if ((js.getSourceUid() == uid && js.getSourcePackageName().equals(packageName)) || (js.getUid() == uid && js.getCallingPackageName().equals(packageName))) {
                this.mJobsToCheck.removeAt(i);
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserRemovedLocked(int userId) {
        this.mPrefetchLifeCycleStart.delete(userId);
        this.mSpecialAppTracker.onUserRemoved(userId);
        for (int u = this.mJobScoreTrackers.numMaps() - 1; u >= 0; u--) {
            int uid = this.mJobScoreTrackers.keyAt(u);
            if (android.os.UserHandle.getUserId(uid) == userId) {
                this.mJobScoreTrackers.deleteAt(u);
            }
        }
        for (int i = this.mJobsToCheck.size() - 1; i >= 0; i--) {
            com.android.server.job.controllers.JobStatus js = this.mJobsToCheck.valueAt(i);
            if (android.os.UserHandle.getUserId(js.getSourceUid()) == userId || android.os.UserHandle.getUserId(js.getUid()) == userId) {
                this.mJobsToCheck.removeAt(i);
            }
        }
    }

    boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mFlexibilityEnabled;
        }
        return z;
    }

    boolean isFlexibilitySatisfiedLocked(com.android.server.job.controllers.JobStatus js) {
        return !this.mFlexibilityEnabled || this.mService.getUidBias(js.getSourceUid()) == 40 || (this.mService.getUidBias(js.getSourceUid()) >= 30 && js.getEffectivePriority() >= 300) || ((js.getEffectivePriority() >= 300 && this.mSpecialAppTracker.isSpecialApp(js.getSourceUserId(), js.getSourcePackageName())) || hasEnoughSatisfiedConstraintsLocked(js) || this.mService.isCurrentlyRunningLocked(js));
    }

    int getRelevantAppliedConstraintsLocked(com.android.server.job.controllers.JobStatus js) {
        int relevantConstraints = (js.canApplyTransportAffinities() ? 268435456 : 0) | 7;
        return this.mAppliedConstraints & relevantConstraints;
    }

    boolean hasEnoughSatisfiedConstraintsLocked(com.android.server.job.controllers.JobStatus js) {
        int satisfiedConstraints = this.mSatisfiedFlexibleConstraints & this.mAppliedConstraints & ((js.areTransportAffinitiesSatisfied() ? 268435456 : 0) | 7);
        int numSatisfied = java.lang.Integer.bitCount(satisfiedConstraints);
        if (numSatisfied >= js.getNumRequiredFlexibleConstraints()) {
            return true;
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (nowElapsed < this.mUnseenConstraintGracePeriodMs) {
            return false;
        }
        int irrelevantConstraints = ~getRelevantAppliedConstraintsLocked(js);
        for (int i = this.mLastSeenConstraintTimesElapsed.size() - 1; i >= 0; i--) {
            int constraints = this.mLastSeenConstraintTimesElapsed.keyAt(i);
            if ((constraints & irrelevantConstraints) == 0) {
                long lastSeenElapsed = this.mLastSeenConstraintTimesElapsed.valueAt(i);
                boolean seenRecently = nowElapsed - lastSeenElapsed <= this.mUnseenConstraintGracePeriodMs;
                if (java.lang.Integer.bitCount(constraints) > numSatisfied && seenRecently) {
                    return false;
                }
            }
        }
        return true;
    }

    void setConstraintSatisfied(int constraint, boolean state, long nowElapsed) {
        synchronized (this.mLock) {
            boolean old = (this.mSatisfiedFlexibleConstraints & constraint) != 0;
            if (old == state) {
                return;
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, "setConstraintSatisfied:  constraint: " + constraint + " state: " + state);
            }
            this.mLastSeenConstraintTimesElapsed.put(this.mSatisfiedFlexibleConstraints, nowElapsed);
            if (!state) {
                this.mLastSeenConstraintTimesElapsed.put(constraint, nowElapsed);
            }
            this.mSatisfiedFlexibleConstraints = (this.mSatisfiedFlexibleConstraints & (~constraint)) | (state ? constraint : 0);
            if ((268435456 & constraint) != 0) {
                return;
            }
            if (this.mFlexibilityEnabled) {
                this.mHandler.obtainMessage(0).sendToTarget();
            }
        }
    }

    boolean isConstraintSatisfied(int constraint) {
        return (this.mSatisfiedFlexibleConstraints & constraint) != 0;
    }

    long getLifeCycleBeginningElapsedLocked(com.android.server.job.controllers.JobStatus js) {
        long earliestRuntime = js.getEarliestRunTime() == 0 ? js.enqueueTime : js.getEarliestRunTime();
        if (js.getJob().isPeriodic() && js.getNumPreviousAttempts() == 0) {
            long truePeriodicStartTimeElapsed = js.getLatestRunTimeElapsed() - js.getJob().getFlexMillis();
            earliestRuntime = (earliestRuntime + truePeriodicStartTimeElapsed) / 2;
        }
        if (js.getJob().isPrefetch()) {
            long estimatedLaunchTime = this.mPrefetchController.getNextEstimatedLaunchTimeLocked(js);
            long prefetchWindowStart = ((java.lang.Long) this.mPrefetchLifeCycleStart.getOrDefault(js.getSourceUserId(), js.getSourcePackageName(), 0L)).longValue();
            if (estimatedLaunchTime != Long.MAX_VALUE) {
                prefetchWindowStart = java.lang.Math.max(prefetchWindowStart, estimatedLaunchTime - this.mPrefetchController.getLaunchTimeThresholdMs());
            }
            return java.lang.Math.max(prefetchWindowStart, earliestRuntime);
        }
        return earliestRuntime;
    }

    int getScoreLocked(int uid, java.lang.String pkgName, long nowElapsed) {
        com.android.server.job.controllers.FlexibilityController.JobScoreTracker scoreTracker = (com.android.server.job.controllers.FlexibilityController.JobScoreTracker) this.mJobScoreTrackers.get(uid, pkgName);
        if (scoreTracker == null) {
            return 0;
        }
        return scoreTracker.getScore(nowElapsed);
    }

    long getLifeCycleEndElapsedLocked(com.android.server.job.controllers.JobStatus js, long nowElapsed, long earliest) {
        if (js.getJob().isPrefetch()) {
            long estimatedLaunchTime = this.mPrefetchController.getNextEstimatedLaunchTimeLocked(js);
            if (js.getLatestRunTimeElapsed() != Long.MAX_VALUE) {
                return java.lang.Math.min(estimatedLaunchTime - this.mConstants.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS, js.getLatestRunTimeElapsed());
            }
            if (estimatedLaunchTime != Long.MAX_VALUE) {
                return estimatedLaunchTime - this.mConstants.PREFETCH_FORCE_BATCH_RELAX_THRESHOLD_MS;
            }
            return Long.MAX_VALUE;
        }
        if (js.getNumPreviousAttempts() > 1) {
            return earliest + java.lang.Math.min((long) java.lang.Math.scalb(this.mRescheduledJobDeadline, js.getNumPreviousAttempts() - 2), this.mMaxRescheduledDeadline);
        }
        int jobPriority = js.getEffectivePriority();
        int jobScore = getScoreLocked(js.getSourceUid(), js.getSourcePackageName(), nowElapsed);
        long fallbackDurationMs = java.lang.Math.min(this.mFallbackFlexibilityDeadlineMs * 3, this.mFallbackFlexibilityDeadlines.get(jobPriority, this.mFallbackFlexibilityDeadlineMs) + (this.mFallbackFlexibilityAdditionalScoreTimeFactors.get(jobPriority, 60000L) * ((long) jobScore)));
        long fallbackDeadlineMs = earliest + fallbackDurationMs;
        if (js.getLatestRunTimeElapsed() == Long.MAX_VALUE) {
            return fallbackDeadlineMs;
        }
        return java.lang.Math.max(fallbackDeadlineMs, js.getLatestRunTimeElapsed());
    }

    int getCurPercentOfLifecycleLocked(com.android.server.job.controllers.JobStatus js, long nowElapsed) {
        long earliest = getLifeCycleBeginningElapsedLocked(js);
        long latest = getLifeCycleEndElapsedLocked(js, nowElapsed, earliest);
        if (latest == Long.MAX_VALUE || earliest >= nowElapsed) {
            return 0;
        }
        if (nowElapsed > latest || latest == earliest) {
            return 100;
        }
        int percentInTime = (int) (((nowElapsed - earliest) * 100) / (latest - earliest));
        return percentInTime;
    }

    long getNextConstraintDropTimeElapsedLocked(com.android.server.job.controllers.JobStatus js) {
        long earliest = getLifeCycleBeginningElapsedLocked(js);
        long latest = getLifeCycleEndElapsedLocked(js, com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis(), earliest);
        return getNextConstraintDropTimeElapsedLocked(js, earliest, latest);
    }

    long getNextConstraintDropTimeElapsedLocked(com.android.server.job.controllers.JobStatus js, long earliest, long latest) {
        int[] percentsToDropConstraints = getPercentsToDropConstraints(js.getEffectivePriority());
        if (latest == Long.MAX_VALUE || js.getNumDroppedFlexibleConstraints() == percentsToDropConstraints.length) {
            return Long.MAX_VALUE;
        }
        int percent = percentsToDropConstraints[js.getNumDroppedFlexibleConstraints()];
        long percentInTime = ((latest - earliest) * ((long) percent)) / 100;
        return earliest + percentInTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getPercentsToDropConstraints(int priority) {
        int[] percentsToDropConstraints = this.mPercentsToDropConstraints.get(priority);
        if (percentsToDropConstraints == null) {
            android.util.Slog.wtf(TAG, "No %-to-drop for priority " + android.app.job.JobInfo.getPriorityString(priority));
            return new int[]{50, 60, 70, 80};
        }
        return percentsToDropConstraints;
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUidBiasChangedLocked(int uid, int prevBias, int newBias) {
        if (prevBias < 30 && newBias < 30) {
            return;
        }
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobsByUid = this.mService.getJobStore().getJobsBySourceUid(uid);
        boolean hasPrefetch = false;
        for (int i = 0; i < jobsByUid.size(); i++) {
            com.android.server.job.controllers.JobStatus js = jobsByUid.valueAt(i);
            if (js.hasFlexibilityConstraint()) {
                js.setFlexibilityConstraintSatisfied(nowElapsed, isFlexibilitySatisfiedLocked(js));
                hasPrefetch |= js.getJob().isPrefetch();
            }
        }
        if (hasPrefetch && prevBias == 40) {
            int userId = android.os.UserHandle.getUserId(uid);
            android.util.ArraySet<java.lang.String> pkgs = this.mService.getPackagesForUidLocked(uid);
            if (pkgs == null) {
                return;
            }
            for (int i2 = 0; i2 < pkgs.size(); i2++) {
                java.lang.String pkg = pkgs.valueAt(i2);
                this.mPrefetchLifeCycleStart.add(userId, pkg, java.lang.Long.valueOf(java.lang.Math.max(((java.lang.Long) this.mPrefetchLifeCycleStart.getOrDefault(userId, pkg, 0L)).longValue(), nowElapsed)));
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onConstantsUpdatedLocked() {
        if (this.mFcConfig.mShouldReevaluateConstraints) {
            com.android.server.AppSchedulingModuleThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.FlexibilityController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onConstantsUpdatedLocked$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConstantsUpdatedLocked$0() {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
        synchronized (this.mLock) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            for (int j = 0; j < this.mFlexibilityTracker.size(); j++) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mFlexibilityTracker.getJobsByNumRequiredConstraints(j);
                for (int i = jobs.size() - 1; i >= 0; i--) {
                    com.android.server.job.controllers.JobStatus js = jobs.valueAt(i);
                    this.mFlexibilityTracker.updateFlexibleConstraints(js, nowElapsed);
                    this.mFlexibilityAlarmQueue.scheduleDropNumConstraintsAlarm(js, nowElapsed);
                    if (js.setFlexibilityConstraintSatisfied(nowElapsed, isFlexibilitySatisfiedLocked(js))) {
                        changedJobs.add(js);
                    }
                }
            }
        }
        if (changedJobs.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(changedJobs);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForUpdatedConstantsLocked() {
        this.mFcConfig.mShouldReevaluateConstraints = false;
    }

    @Override // com.android.server.job.controllers.StateController
    public void processConstantLocked(android.provider.DeviceConfig.Properties properties, java.lang.String key) {
        this.mFcConfig.processConstantLocked(properties, key);
    }

    class FlexibilityTracker {
        final java.util.ArrayList<android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mTrackedJobs = new java.util.ArrayList<>();

        FlexibilityTracker(int numFlexibleConstraints) {
            for (int i = 0; i <= numFlexibleConstraints; i++) {
                this.mTrackedJobs.add(new android.util.ArraySet<>());
            }
        }

        public android.util.ArraySet<com.android.server.job.controllers.JobStatus> getJobsByNumRequiredConstraints(int numRequired) {
            if (numRequired > this.mTrackedJobs.size()) {
                android.util.Slog.wtfStack(com.android.server.job.controllers.FlexibilityController.TAG, "Asked for a larger number of constraints than exists.");
                return null;
            }
            return this.mTrackedJobs.get(numRequired);
        }

        public void add(com.android.server.job.controllers.JobStatus js) {
            if (js.getNumRequiredFlexibleConstraints() < 0) {
                return;
            }
            this.mTrackedJobs.get(js.getNumRequiredFlexibleConstraints()).add(js);
        }

        public void remove(com.android.server.job.controllers.JobStatus js) {
            this.mTrackedJobs.get(js.getNumRequiredFlexibleConstraints()).remove(js);
        }

        public void updateFlexibleConstraints(com.android.server.job.controllers.JobStatus js, long nowElapsed) {
            int prevNumRequired = js.getNumRequiredFlexibleConstraints();
            int numAppliedConstraints = java.lang.Integer.bitCount(com.android.server.job.controllers.FlexibilityController.this.getRelevantAppliedConstraintsLocked(js));
            js.setNumAppliedFlexibleConstraints(numAppliedConstraints);
            int[] percentsToDropConstraints = com.android.server.job.controllers.FlexibilityController.this.getPercentsToDropConstraints(js.getEffectivePriority());
            int curPercent = com.android.server.job.controllers.FlexibilityController.this.getCurPercentOfLifecycleLocked(js, nowElapsed);
            int toDrop = 0;
            for (int i = 0; i < numAppliedConstraints; i++) {
                if (curPercent >= percentsToDropConstraints[i]) {
                    toDrop++;
                }
            }
            js.setNumDroppedFlexibleConstraints(toDrop);
            if (prevNumRequired == js.getNumRequiredFlexibleConstraints()) {
                return;
            }
            this.mTrackedJobs.get(prevNumRequired).remove(js);
            add(js);
        }

        public void calculateNumDroppedConstraints(com.android.server.job.controllers.JobStatus js, long nowElapsed) {
            int curPercent = com.android.server.job.controllers.FlexibilityController.this.getCurPercentOfLifecycleLocked(js, nowElapsed);
            int toDrop = 0;
            int jsMaxFlexibleConstraints = js.getNumAppliedFlexibleConstraints();
            int[] percentsToDropConstraints = com.android.server.job.controllers.FlexibilityController.this.getPercentsToDropConstraints(js.getEffectivePriority());
            for (int i = 0; i < jsMaxFlexibleConstraints; i++) {
                if (curPercent >= percentsToDropConstraints[i]) {
                    toDrop++;
                }
            }
            setNumDroppedFlexibleConstraints(js, toDrop);
        }

        public java.util.ArrayList<android.util.ArraySet<com.android.server.job.controllers.JobStatus>> getArrayList() {
            return this.mTrackedJobs;
        }

        public void setNumDroppedFlexibleConstraints(com.android.server.job.controllers.JobStatus js, int numDropped) {
            if (numDropped != js.getNumDroppedFlexibleConstraints()) {
                remove(js);
                js.setNumDroppedFlexibleConstraints(numDropped);
                add(js);
            }
        }

        public int size() {
            return this.mTrackedJobs.size();
        }

        public void dump(android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate, long nowElapsed) {
            for (int i = 0; i < this.mTrackedJobs.size(); i++) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = this.mTrackedJobs.get(i);
                for (int j = 0; j < jobs.size(); j++) {
                    com.android.server.job.controllers.JobStatus js = jobs.valueAt(j);
                    if (predicate.test(js)) {
                        js.printUniqueId(pw);
                        pw.print(" from ");
                        android.os.UserHandle.formatUid(pw, js.getSourceUid());
                        pw.print("-> Num Required Constraints: ");
                        pw.print(js.getNumRequiredFlexibleConstraints());
                        pw.print(", lifecycle=[");
                        long earliest = com.android.server.job.controllers.FlexibilityController.this.getLifeCycleBeginningElapsedLocked(js);
                        pw.print(earliest);
                        pw.print(", (");
                        pw.print(com.android.server.job.controllers.FlexibilityController.this.getCurPercentOfLifecycleLocked(js, nowElapsed));
                        pw.print("%), ");
                        pw.print(com.android.server.job.controllers.FlexibilityController.this.getLifeCycleEndElapsedLocked(js, nowElapsed, earliest));
                        pw.print("]");
                        pw.println();
                    }
                }
            }
        }
    }

    class FlexibilityAlarmQueue extends com.android.server.utils.AlarmQueue<com.android.server.job.controllers.JobStatus> {
        private FlexibilityAlarmQueue(android.content.Context context, android.os.Looper looper) {
            super(context, looper, "*job.flexibility_check*", "Flexible Constraint Check", true, com.android.server.job.controllers.FlexibilityController.this.mMinTimeBetweenFlexibilityAlarmsMs);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(com.android.server.job.controllers.JobStatus js, int userId) {
            return js.getSourceUserId() == userId;
        }

        public void scheduleDropNumConstraintsAlarm(com.android.server.job.controllers.JobStatus js, long nowElapsed) {
            synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                long earliest = com.android.server.job.controllers.FlexibilityController.this.getLifeCycleBeginningElapsedLocked(js);
                long latest = com.android.server.job.controllers.FlexibilityController.this.getLifeCycleEndElapsedLocked(js, nowElapsed, earliest);
                if (latest <= earliest) {
                    android.util.Slog.wtf(com.android.server.job.controllers.FlexibilityController.TAG, "Got invalid latest when scheduling alarm. prefetch=" + js.getJob().isPrefetch() + " periodic=" + js.getJob().isPeriodic());
                    com.android.server.job.controllers.FlexibilityController.this.mFlexibilityTracker.setNumDroppedFlexibleConstraints(js, js.getNumAppliedFlexibleConstraints());
                    com.android.server.job.controllers.FlexibilityController.this.mJobsToCheck.add(js);
                    com.android.server.job.controllers.FlexibilityController.this.mHandler.sendEmptyMessage(1);
                    return;
                }
                long nextTimeElapsed = com.android.server.job.controllers.FlexibilityController.this.getNextConstraintDropTimeElapsedLocked(js, earliest, latest);
                if (com.android.server.job.controllers.FlexibilityController.DEBUG) {
                    android.util.Slog.d(com.android.server.job.controllers.FlexibilityController.TAG, "scheduleDropNumConstraintsAlarm: " + js.toShortString() + " numApplied: " + js.getNumAppliedFlexibleConstraints() + " numRequired: " + js.getNumRequiredFlexibleConstraints() + " numSatisfied: " + java.lang.Integer.bitCount(com.android.server.job.controllers.FlexibilityController.this.mSatisfiedFlexibleConstraints & com.android.server.job.controllers.FlexibilityController.this.getRelevantAppliedConstraintsLocked(js)) + " curTime: " + nowElapsed + " earliest: " + earliest + " latest: " + latest + " nextTime: " + nextTimeElapsed);
                }
                if (latest - nowElapsed < com.android.server.job.controllers.FlexibilityController.this.mDeadlineProximityLimitMs) {
                    if (com.android.server.job.controllers.FlexibilityController.DEBUG) {
                        android.util.Slog.d(com.android.server.job.controllers.FlexibilityController.TAG, "deadline proximity met: " + js);
                    }
                    com.android.server.job.controllers.FlexibilityController.this.mFlexibilityTracker.setNumDroppedFlexibleConstraints(js, js.getNumAppliedFlexibleConstraints());
                    com.android.server.job.controllers.FlexibilityController.this.mJobsToCheck.add(js);
                    com.android.server.job.controllers.FlexibilityController.this.mHandler.sendEmptyMessage(1);
                    return;
                }
                if (nextTimeElapsed == Long.MAX_VALUE) {
                    removeAlarmForKey(js);
                } else {
                    if (latest - nextTimeElapsed <= com.android.server.job.controllers.FlexibilityController.this.mDeadlineProximityLimitMs) {
                        if (com.android.server.job.controllers.FlexibilityController.DEBUG) {
                            android.util.Slog.d(com.android.server.job.controllers.FlexibilityController.TAG, "last alarm set: " + js);
                        }
                        addAlarm(js, latest - com.android.server.job.controllers.FlexibilityController.this.mDeadlineProximityLimitMs);
                        return;
                    }
                    addAlarm(js, nextTimeElapsed);
                }
            }
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(android.util.ArraySet<com.android.server.job.controllers.JobStatus> expired) {
            synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                for (int i = 0; i < expired.size(); i++) {
                    com.android.server.job.controllers.JobStatus js = expired.valueAt(i);
                    if (com.android.server.job.controllers.FlexibilityController.DEBUG) {
                        android.util.Slog.d(com.android.server.job.controllers.FlexibilityController.TAG, "Alarm fired for " + js.toShortString());
                    }
                    com.android.server.job.controllers.FlexibilityController.this.mFlexibilityTracker.calculateNumDroppedConstraints(js, nowElapsed);
                    if (js.getNumRequiredFlexibleConstraints() > 0) {
                        scheduleDropNumConstraintsAlarm(js, nowElapsed);
                    }
                    if (js.setFlexibilityConstraintSatisfied(nowElapsed, com.android.server.job.controllers.FlexibilityController.this.isFlexibilitySatisfiedLocked(js))) {
                        changedJobs.add(js);
                    }
                }
                com.android.server.job.controllers.FlexibilityController.this.mStateChangedListener.onControllerStateChanged(changedJobs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class FcHandler extends android.os.Handler {
        FcHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    removeMessages(0);
                    synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                        com.android.server.job.controllers.FlexibilityController.this.mJobsToCheck.clear();
                        com.android.server.job.controllers.FlexibilityController.this.mPackagesToCheck.clear();
                        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
                        int numAppliedSystemWideConstraints = java.lang.Integer.bitCount(com.android.server.job.controllers.FlexibilityController.this.mAppliedConstraints & 7);
                        for (int o = 0; o <= numAppliedSystemWideConstraints; o++) {
                            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobsByNumConstraints = com.android.server.job.controllers.FlexibilityController.this.mFlexibilityTracker.getJobsByNumRequiredConstraints(o);
                            if (jobsByNumConstraints != null) {
                                for (int i = 0; i < jobsByNumConstraints.size(); i++) {
                                    com.android.server.job.controllers.JobStatus js = jobsByNumConstraints.valueAt(i);
                                    if (js.setFlexibilityConstraintSatisfied(nowElapsed, com.android.server.job.controllers.FlexibilityController.this.isFlexibilitySatisfiedLocked(js))) {
                                        changedJobs.add(js);
                                    }
                                }
                            }
                        }
                        int o2 = changedJobs.size();
                        if (o2 > 0) {
                            com.android.server.job.controllers.FlexibilityController.this.mStateChangedListener.onControllerStateChanged(changedJobs);
                        }
                        break;
                    }
                    return;
                case 1:
                    synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                        long nowElapsed2 = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs2 = new android.util.ArraySet<>();
                        for (int i2 = com.android.server.job.controllers.FlexibilityController.this.mJobsToCheck.size() - 1; i2 >= 0; i2--) {
                            com.android.server.job.controllers.JobStatus js2 = (com.android.server.job.controllers.JobStatus) com.android.server.job.controllers.FlexibilityController.this.mJobsToCheck.valueAt(i2);
                            if (com.android.server.job.controllers.FlexibilityController.DEBUG) {
                                android.util.Slog.d(com.android.server.job.controllers.FlexibilityController.TAG, "Checking on " + js2.toShortString());
                            }
                            if (js2.setFlexibilityConstraintSatisfied(nowElapsed2, com.android.server.job.controllers.FlexibilityController.this.isFlexibilitySatisfiedLocked(js2))) {
                                changedJobs2.add(js2);
                            }
                        }
                        com.android.server.job.controllers.FlexibilityController.this.mJobsToCheck.clear();
                        if (changedJobs2.size() > 0) {
                            com.android.server.job.controllers.FlexibilityController.this.mStateChangedListener.onControllerStateChanged(changedJobs2);
                        }
                        break;
                    }
                    return;
                case 2:
                    synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                        final long nowElapsed3 = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                        final android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs3 = new android.util.ArraySet<>();
                        com.android.server.job.controllers.FlexibilityController.this.mService.getJobStore().forEachJob(new java.util.function.Predicate() { // from class: com.android.server.job.controllers.FlexibilityController$FcHandler$$ExternalSyntheticLambda0
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return this.f$0.lambda$handleMessage$0((com.android.server.job.controllers.JobStatus) obj);
                            }
                        }, new java.util.function.Consumer() { // from class: com.android.server.job.controllers.FlexibilityController$FcHandler$$ExternalSyntheticLambda1
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.lambda$handleMessage$1(nowElapsed3, changedJobs3, (com.android.server.job.controllers.JobStatus) obj);
                            }
                        });
                        com.android.server.job.controllers.FlexibilityController.this.mPackagesToCheck.clear();
                        if (changedJobs3.size() > 0) {
                            com.android.server.job.controllers.FlexibilityController.this.mStateChangedListener.onControllerStateChanged(changedJobs3);
                        }
                        break;
                    }
                    return;
                default:
                    return;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$handleMessage$0(com.android.server.job.controllers.JobStatus js) {
            return com.android.server.job.controllers.FlexibilityController.this.mPackagesToCheck.contains(js.getSourcePackageName()) || com.android.server.job.controllers.FlexibilityController.this.mPackagesToCheck.contains(js.getCallingPackageName());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$1(long nowElapsed, android.util.ArraySet changedJobs, com.android.server.job.controllers.JobStatus js) {
            if (com.android.server.job.controllers.FlexibilityController.DEBUG) {
                android.util.Slog.d(com.android.server.job.controllers.FlexibilityController.TAG, "Checking on " + js.toShortString());
            }
            if (js.setFlexibilityConstraintSatisfied(nowElapsed, com.android.server.job.controllers.FlexibilityController.this.isFlexibilitySatisfiedLocked(js))) {
                changedJobs.add(js);
            }
        }
    }

    class FcConfig {
        static final int DEFAULT_APPLIED_CONSTRAINTS = 0;
        static final long DEFAULT_DEADLINE_PROXIMITY_LIMIT_MS = 900000;
        static final long DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_MS = 86400000;
        private static final long DEFAULT_MAX_RESCHEDULED_DEADLINE_MS = 86400000;
        private static final long DEFAULT_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS = 60000;
        private static final long DEFAULT_RESCHEDULED_JOB_DEADLINE_MS = 3600000;
        static final long DEFAULT_UNSEEN_CONSTRAINT_GRACE_PERIOD_MS = 259200000;
        private static final java.lang.String FC_CONFIG_PREFIX = "fc_";
        static final java.lang.String KEY_APPLIED_CONSTRAINTS = "fc_applied_constraints";
        static final java.lang.String KEY_DEADLINE_PROXIMITY_LIMIT = "fc_flexibility_deadline_proximity_limit_ms";
        static final java.lang.String KEY_FALLBACK_FLEXIBILITY_DEADLINE = "fc_fallback_flexibility_deadline_ms";
        static final java.lang.String KEY_FALLBACK_FLEXIBILITY_DEADLINES = "fc_fallback_flexibility_deadlines";
        static final java.lang.String KEY_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS = "fc_fallback_flexibility_deadline_additional_score_time_factors";
        static final java.lang.String KEY_FALLBACK_FLEXIBILITY_DEADLINE_SCORES = "fc_fallback_flexibility_deadline_scores";
        static final java.lang.String KEY_MAX_RESCHEDULED_DEADLINE_MS = "fc_max_rescheduled_deadline_ms";
        static final java.lang.String KEY_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS = "fc_min_time_between_flexibility_alarms_ms";
        static final java.lang.String KEY_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS = "fc_percents_to_drop_flexible_constraints";
        static final java.lang.String KEY_RESCHEDULED_JOB_DEADLINE_MS = "fc_rescheduled_job_deadline_ms";
        static final java.lang.String KEY_UNSEEN_CONSTRAINT_GRACE_PERIOD_MS = "fc_unseen_constraint_grace_period_ms";
        static final android.util.SparseLongArray DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES = new android.util.SparseLongArray();
        static final android.util.SparseIntArray DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES = new android.util.SparseIntArray();
        static final android.util.SparseLongArray DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS = new android.util.SparseLongArray();
        static final android.util.SparseArray<int[]> DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS = new android.util.SparseArray<>();
        private boolean mShouldReevaluateConstraints = false;
        public int APPLIED_CONSTRAINTS = 0;
        public long DEADLINE_PROXIMITY_LIMIT_MS = DEFAULT_DEADLINE_PROXIMITY_LIMIT_MS;
        public long FALLBACK_FLEXIBILITY_DEADLINE_MS = 86400000;
        public long MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS = 60000;
        public android.util.SparseArray<int[]> PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS = new android.util.SparseArray<>();
        public long RESCHEDULED_JOB_DEADLINE_MS = 3600000;
        public long MAX_RESCHEDULED_DEADLINE_MS = 86400000;
        public long UNSEEN_CONSTRAINT_GRACE_PERIOD_MS = DEFAULT_UNSEEN_CONSTRAINT_GRACE_PERIOD_MS;
        public final android.util.SparseLongArray FALLBACK_FLEXIBILITY_DEADLINES = new android.util.SparseLongArray();
        public final android.util.SparseIntArray FALLBACK_FLEXIBILITY_DEADLINE_SCORES = new android.util.SparseIntArray();
        public final android.util.SparseLongArray FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS = new android.util.SparseLongArray();

        static {
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.put(500, 3600000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.put(400, 21600000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.put(300, 43200000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.put(200, 86400000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.put(100, 172800000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.put(500, 5);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.put(400, 4);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.put(300, 3);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.put(200, 2);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.put(100, 1);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.put(500, 0L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.put(400, 180000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.put(300, 120000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.put(200, 60000L);
            DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.put(100, 60000L);
            DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.put(500, new int[]{1, 2, 3, 4});
            DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.put(400, new int[]{33, 50, 60, 75});
            DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.put(300, new int[]{50, 60, 70, 80});
            DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.put(200, new int[]{50, 60, 70, 80});
            DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.put(100, new int[]{55, 65, 75, 85});
        }

        FcConfig() {
            for (int i = 0; i < DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.size(); i++) {
                this.FALLBACK_FLEXIBILITY_DEADLINES.put(DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.keyAt(i), DEFAULT_FALLBACK_FLEXIBILITY_DEADLINES.valueAt(i));
            }
            for (int i2 = 0; i2 < DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.size(); i2++) {
                this.FALLBACK_FLEXIBILITY_DEADLINE_SCORES.put(DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.keyAt(i2), DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_SCORES.valueAt(i2));
            }
            for (int i3 = 0; i3 < DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.size(); i3++) {
                this.FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.put(DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.keyAt(i3), DEFAULT_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS.valueAt(i3));
            }
            for (int i4 = 0; i4 < DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.size(); i4++) {
                this.PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.put(DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.keyAt(i4), DEFAULT_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS.valueAt(i4));
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:38:0x007d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void processConstantLocked(android.provider.DeviceConfig.Properties r7, java.lang.String r8) {
            /*
                Method dump skipped, instruction units count: 602
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.FlexibilityController.FcConfig.processConstantLocked(android.provider.DeviceConfig$Properties, java.lang.String):void");
        }

        private boolean parsePercentToDropKeyValueString(java.lang.String s, android.util.SparseArray<int[]> into, android.util.SparseArray<int[]> defaults) {
            int i;
            int[] iArr;
            int i2;
            int[] iArr2;
            int i3;
            int[] iArr3;
            int i4;
            int[] iArr4;
            int i5;
            int[] iArr5;
            android.util.KeyValueListParser priorityParser = new android.util.KeyValueListParser(',');
            try {
                priorityParser.setString(s);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.wtf(com.android.server.job.controllers.FlexibilityController.TAG, "Bad percent to drop key value string given", e);
                priorityParser.setString((java.lang.String) null);
            }
            int[] oldMax = into.get(500);
            int[] oldHigh = into.get(400);
            int[] oldDefault = into.get(300);
            int[] oldLow = into.get(200);
            int[] oldMin = into.get(100);
            int[] newMax = parsePercentToDropString(priorityParser.getString(java.lang.String.valueOf(500), (java.lang.String) null));
            int[] newHigh = parsePercentToDropString(priorityParser.getString(java.lang.String.valueOf(400), (java.lang.String) null));
            int[] newDefault = parsePercentToDropString(priorityParser.getString(java.lang.String.valueOf(300), (java.lang.String) null));
            int[] newLow = parsePercentToDropString(priorityParser.getString(java.lang.String.valueOf(200), (java.lang.String) null));
            int[] newMin = parsePercentToDropString(priorityParser.getString(java.lang.String.valueOf(100), (java.lang.String) null));
            if (newMax == null) {
                i = 500;
                iArr = defaults.get(500);
            } else {
                i = 500;
                iArr = newMax;
            }
            into.put(i, iArr);
            if (newHigh == null) {
                i2 = 400;
                iArr2 = defaults.get(400);
            } else {
                i2 = 400;
                iArr2 = newHigh;
            }
            into.put(i2, iArr2);
            if (newDefault == null) {
                i3 = 300;
                iArr3 = defaults.get(300);
            } else {
                i3 = 300;
                iArr3 = newDefault;
            }
            into.put(i3, iArr3);
            if (newLow == null) {
                i4 = 200;
                iArr4 = defaults.get(200);
            } else {
                i4 = 200;
                iArr4 = newLow;
            }
            into.put(i4, iArr4);
            if (newMin == null) {
                i5 = 100;
                iArr5 = defaults.get(100);
            } else {
                i5 = 100;
                iArr5 = newMin;
            }
            into.put(i5, iArr5);
            return (java.util.Arrays.equals(oldMax, into.get(500)) && java.util.Arrays.equals(oldHigh, into.get(400)) && java.util.Arrays.equals(oldDefault, into.get(300)) && java.util.Arrays.equals(oldLow, into.get(200)) && java.util.Arrays.equals(oldMin, into.get(100))) ? false : true;
        }

        private int[] parsePercentToDropString(java.lang.String s) {
            if (s == null || s.isEmpty()) {
                return null;
            }
            java.lang.String[] dropPercentString = s.split("\\|");
            int[] dropPercentInt = new int[java.lang.Integer.bitCount(com.android.server.job.controllers.FlexibilityController.FLEXIBLE_CONSTRAINTS)];
            if (dropPercentInt.length != dropPercentString.length) {
                return null;
            }
            int prevPercent = 0;
            for (int i = 0; i < dropPercentString.length; i++) {
                try {
                    dropPercentInt[i] = java.lang.Integer.parseInt(dropPercentString[i]);
                    if (dropPercentInt[i] < prevPercent) {
                        android.util.Slog.wtf(com.android.server.job.controllers.FlexibilityController.TAG, "Percents to drop constraints were not in increasing order.");
                        return null;
                    }
                    if (dropPercentInt[i] > 100) {
                        android.util.Slog.e(com.android.server.job.controllers.FlexibilityController.TAG, "Found % over 100");
                        return null;
                    }
                    prevPercent = dropPercentInt[i];
                } catch (java.lang.NumberFormatException ex) {
                    android.util.Slog.e(com.android.server.job.controllers.FlexibilityController.TAG, "Provided string was improperly formatted.", ex);
                    return null;
                }
            }
            return dropPercentInt;
        }

        private boolean parsePriorityToIntKeyValueString(java.lang.String s, android.util.SparseIntArray into, android.util.SparseIntArray defaults) {
            android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
            try {
                parser.setString(s);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.wtf(com.android.server.job.controllers.FlexibilityController.TAG, "Bad string given", e);
                parser.setString((java.lang.String) null);
            }
            int oldMax = into.get(500);
            int oldHigh = into.get(400);
            int oldDefault = into.get(300);
            int oldLow = into.get(200);
            int oldMin = into.get(100);
            int newMax = parser.getInt(java.lang.String.valueOf(500), defaults.get(500));
            int newHigh = parser.getInt(java.lang.String.valueOf(400), defaults.get(400));
            int newDefault = parser.getInt(java.lang.String.valueOf(300), defaults.get(300));
            int newLow = parser.getInt(java.lang.String.valueOf(200), defaults.get(200));
            int newMin = parser.getInt(java.lang.String.valueOf(100), defaults.get(100));
            into.put(500, newMax);
            into.put(400, newHigh);
            into.put(300, newDefault);
            into.put(200, newLow);
            into.put(100, newMin);
            return (oldMax == newMax && oldHigh == newHigh && oldDefault == newDefault && oldLow == newLow && oldMin == newMin) ? false : true;
        }

        private boolean parsePriorityToLongKeyValueString(java.lang.String s, android.util.SparseLongArray into, android.util.SparseLongArray defaults) {
            android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
            try {
                parser.setString(s);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.wtf(com.android.server.job.controllers.FlexibilityController.TAG, "Bad string given", e);
                parser.setString((java.lang.String) null);
            }
            long oldMax = into.get(500);
            long oldHigh = into.get(400);
            long oldDefault = into.get(300);
            long oldLow = into.get(200);
            long oldMin = into.get(100);
            long newMax = parser.getLong(java.lang.String.valueOf(500), defaults.get(500));
            long newHigh = parser.getLong(java.lang.String.valueOf(400), defaults.get(400));
            long newDefault = parser.getLong(java.lang.String.valueOf(300), defaults.get(300));
            long newLow = parser.getLong(java.lang.String.valueOf(200), defaults.get(200));
            long newMin = parser.getLong(java.lang.String.valueOf(100), defaults.get(100));
            into.put(500, newMax);
            into.put(400, newHigh);
            into.put(300, newDefault);
            into.put(200, newLow);
            into.put(100, newMin);
            return (oldMax == newMax && oldHigh == newHigh && oldDefault == newDefault && oldLow == newLow && oldMin == newMin) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(android.util.IndentingPrintWriter pw) {
            pw.println();
            pw.print(com.android.server.job.controllers.FlexibilityController.class.getSimpleName());
            pw.println(":");
            pw.increaseIndent();
            pw.print(KEY_APPLIED_CONSTRAINTS, java.lang.Integer.valueOf(this.APPLIED_CONSTRAINTS));
            pw.print("(");
            if (this.APPLIED_CONSTRAINTS != 0) {
                com.android.server.job.controllers.JobStatus.dumpConstraints(pw, this.APPLIED_CONSTRAINTS);
            } else {
                pw.print("nothing");
            }
            pw.println(")");
            pw.print(KEY_DEADLINE_PROXIMITY_LIMIT, java.lang.Long.valueOf(this.DEADLINE_PROXIMITY_LIMIT_MS)).println();
            pw.print(KEY_FALLBACK_FLEXIBILITY_DEADLINE, java.lang.Long.valueOf(this.FALLBACK_FLEXIBILITY_DEADLINE_MS)).println();
            pw.print(KEY_FALLBACK_FLEXIBILITY_DEADLINES, this.FALLBACK_FLEXIBILITY_DEADLINES).println();
            pw.print(KEY_FALLBACK_FLEXIBILITY_DEADLINE_SCORES, this.FALLBACK_FLEXIBILITY_DEADLINE_SCORES).println();
            pw.print(KEY_FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS, this.FALLBACK_FLEXIBILITY_DEADLINE_ADDITIONAL_SCORE_TIME_FACTORS).println();
            pw.print(KEY_MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS, java.lang.Long.valueOf(this.MIN_TIME_BETWEEN_FLEXIBILITY_ALARMS_MS)).println();
            pw.print(KEY_PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS, this.PERCENTS_TO_DROP_FLEXIBLE_CONSTRAINTS).println();
            pw.print(KEY_RESCHEDULED_JOB_DEADLINE_MS, java.lang.Long.valueOf(this.RESCHEDULED_JOB_DEADLINE_MS)).println();
            pw.print(KEY_MAX_RESCHEDULED_DEADLINE_MS, java.lang.Long.valueOf(this.MAX_RESCHEDULED_DEADLINE_MS)).println();
            pw.print(KEY_UNSEEN_CONSTRAINT_GRACE_PERIOD_MS, java.lang.Long.valueOf(this.UNSEEN_CONSTRAINT_GRACE_PERIOD_MS)).println();
            pw.decreaseIndent();
        }
    }

    com.android.server.job.controllers.FlexibilityController.FcConfig getFcConfig() {
        return this.mFcConfig;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class SpecialAppTracker {
        private com.android.server.DeviceIdleInternal mDeviceIdleInternal;
        private final boolean mHasFeatureTelephonySubscription;
        private android.telephony.TelephonyManager mTelephonyManager;
        private final java.lang.Object mSatLock = new java.lang.Object();
        private final android.util.SparseSetArray<java.lang.String> mSpecialApps = new android.util.SparseSetArray<>();
        private final android.util.SparseSetArray<java.lang.String> mCarrierPrivilegedApps = new android.util.SparseSetArray<>();
        private final android.util.SparseArray<com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.LogicalIndexCarrierPrivilegesCallback> mCarrierPrivilegedCallbacks = new android.util.SparseArray<>();
        private final android.util.ArraySet<java.lang.String> mPowerAllowlistedApps = new android.util.ArraySet<>();
        private final android.content.BroadcastReceiver mBroadcastReceiver = new com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.AnonymousClass1();

        /* JADX INFO: renamed from: com.android.server.job.controllers.FlexibilityController$SpecialAppTracker$1, reason: invalid class name */
        class AnonymousClass1 extends android.content.BroadcastReceiver {
            AnonymousClass1() {
            }

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:11:0x0020  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r4, android.content.Intent r5) {
                /*
                    r3 = this;
                    java.lang.String r0 = r5.getAction()
                    int r1 = r0.hashCode()
                    switch(r1) {
                        case -65633567: goto L16;
                        case 1093296680: goto Lc;
                        default: goto Lb;
                    }
                Lb:
                    goto L20
                Lc:
                    java.lang.String r1 = "android.telephony.action.MULTI_SIM_CONFIG_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Lb
                    r0 = 0
                    goto L21
                L16:
                    java.lang.String r1 = "android.os.action.POWER_SAVE_WHITELIST_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Lb
                    r0 = 1
                    goto L21
                L20:
                    r0 = -1
                L21:
                    switch(r0) {
                        case 0: goto L38;
                        case 1: goto L25;
                        default: goto L24;
                    }
                L24:
                    goto L3e
                L25:
                    com.android.server.job.controllers.FlexibilityController$SpecialAppTracker r0 = com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this
                    com.android.server.job.controllers.FlexibilityController r0 = com.android.server.job.controllers.FlexibilityController.this
                    com.android.server.job.controllers.FlexibilityController$FcHandler r0 = com.android.server.job.controllers.FlexibilityController.m4640$$Nest$fgetmHandler(r0)
                    com.android.server.job.controllers.FlexibilityController$SpecialAppTracker r1 = com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this
                    com.android.server.job.controllers.FlexibilityController$SpecialAppTracker$1$$ExternalSyntheticLambda0 r2 = new com.android.server.job.controllers.FlexibilityController$SpecialAppTracker$1$$ExternalSyntheticLambda0
                    r2.<init>()
                    r0.post(r2)
                    goto L3e
                L38:
                    com.android.server.job.controllers.FlexibilityController$SpecialAppTracker r0 = com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this
                    com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.m4676$$Nest$mupdateCarrierPrivilegedCallbackRegistration(r0)
                L3e:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        }

        SpecialAppTracker() {
            this.mHasFeatureTelephonySubscription = com.android.server.job.controllers.FlexibilityController.this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony.subscription");
        }

        public boolean isSpecialApp(int userId, java.lang.String packageName) {
            synchronized (this.mSatLock) {
                if (this.mSpecialApps.contains(-1, packageName)) {
                    return true;
                }
                return this.mSpecialApps.contains(userId, packageName);
            }
        }

        private boolean isSpecialAppInternal(int userId, java.lang.String packageName) {
            synchronized (this.mSatLock) {
                if (this.mPowerAllowlistedApps.contains(packageName)) {
                    return true;
                }
                for (int l = this.mCarrierPrivilegedApps.size() - 1; l >= 0; l--) {
                    if (this.mCarrierPrivilegedApps.contains(this.mCarrierPrivilegedApps.keyAt(l), packageName)) {
                        return true;
                    }
                }
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAppRemoved(int userId, java.lang.String packageName) {
            synchronized (this.mSatLock) {
                this.mSpecialApps.remove(userId, packageName);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSystemServicesReady() {
            this.mDeviceIdleInternal = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
            this.mTelephonyManager = (android.telephony.TelephonyManager) com.android.server.job.controllers.FlexibilityController.this.mContext.getSystemService(android.telephony.TelephonyManager.class);
            synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                if (com.android.server.job.controllers.FlexibilityController.this.mFlexibilityEnabled) {
                    com.android.server.job.controllers.FlexibilityController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.FlexibilityController$SpecialAppTracker$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.updateCarrierPrivilegedCallbackRegistration();
                        }
                    });
                    com.android.server.job.controllers.FlexibilityController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.FlexibilityController$SpecialAppTracker$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.updatePowerAllowlistCache();
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onUserRemoved(int userId) {
            synchronized (this.mSatLock) {
                this.mSpecialApps.remove(userId);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startTracking() {
            android.content.IntentFilter filter = new android.content.IntentFilter("android.os.action.POWER_SAVE_WHITELIST_CHANGED");
            if (this.mHasFeatureTelephonySubscription) {
                filter.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
                updateCarrierPrivilegedCallbackRegistration();
            }
            com.android.server.job.controllers.FlexibilityController.this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
            updatePowerAllowlistCache();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stopTracking() {
            com.android.server.job.controllers.FlexibilityController.this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            synchronized (this.mSatLock) {
                this.mCarrierPrivilegedApps.clear();
                this.mPowerAllowlistedApps.clear();
                this.mSpecialApps.clear();
                for (int i = this.mCarrierPrivilegedCallbacks.size() - 1; i >= 0; i--) {
                    this.mTelephonyManager.unregisterCarrierPrivilegesCallback(this.mCarrierPrivilegedCallbacks.valueAt(i));
                }
                this.mCarrierPrivilegedCallbacks.clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateCarrierPrivilegedCallbackRegistration() {
            if (this.mTelephonyManager == null || !this.mHasFeatureTelephonySubscription) {
                return;
            }
            java.util.Collection<android.telephony.UiccSlotMapping> simSlotMapping = this.mTelephonyManager.getSimSlotMapping();
            android.util.ArraySet<java.lang.String> changedPkgs = new android.util.ArraySet<>();
            synchronized (this.mSatLock) {
                android.util.IntArray callbacksToRemove = new android.util.IntArray();
                for (int i = this.mCarrierPrivilegedCallbacks.size() - 1; i >= 0; i--) {
                    callbacksToRemove.add(this.mCarrierPrivilegedCallbacks.keyAt(i));
                }
                for (android.telephony.UiccSlotMapping mapping : simSlotMapping) {
                    int logicalIndex = mapping.getLogicalSlotIndex();
                    if (this.mCarrierPrivilegedCallbacks.contains(logicalIndex)) {
                        int i2 = callbacksToRemove.size() - 1;
                        while (true) {
                            if (i2 < 0) {
                                break;
                            }
                            if (callbacksToRemove.get(i2) == logicalIndex) {
                                callbacksToRemove.remove(i2);
                                break;
                            }
                            i2--;
                        }
                    } else {
                        com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.LogicalIndexCarrierPrivilegesCallback callback = new com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.LogicalIndexCarrierPrivilegesCallback(logicalIndex);
                        this.mCarrierPrivilegedCallbacks.put(logicalIndex, callback);
                        this.mTelephonyManager.registerCarrierPrivilegesCallback(logicalIndex, com.android.server.AppSchedulingModuleThread.getExecutor(), callback);
                    }
                }
                for (int i3 = callbacksToRemove.size() - 1; i3 >= 0; i3--) {
                    int logicalIndex2 = callbacksToRemove.get(i3);
                    this.mTelephonyManager.unregisterCarrierPrivilegesCallback(this.mCarrierPrivilegedCallbacks.get(logicalIndex2));
                    this.mCarrierPrivilegedCallbacks.remove(logicalIndex2);
                    changedPkgs.addAll(this.mCarrierPrivilegedApps.get(logicalIndex2));
                    this.mCarrierPrivilegedApps.remove(logicalIndex2);
                }
            }
            updateSpecialAppSetUnlocked(-1, changedPkgs);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateSpecialAppSetUnlocked(int userId, android.util.ArraySet<java.lang.String> pkgs) {
            if (java.lang.Thread.holdsLock(this.mSatLock)) {
                throw new java.lang.IllegalStateException("Must never hold local mSatLock");
            }
            if (pkgs.size() == 0) {
                return;
            }
            android.util.ArraySet<java.lang.String> changedPkgs = new android.util.ArraySet<>();
            synchronized (this.mSatLock) {
                for (int i = pkgs.size() - 1; i >= 0; i--) {
                    java.lang.String pkgName = pkgs.valueAt(i);
                    if (isSpecialAppInternal(userId, pkgName)) {
                        if (this.mSpecialApps.add(userId, pkgName)) {
                            changedPkgs.add(pkgName);
                        }
                    } else if (this.mSpecialApps.remove(userId, pkgName)) {
                        changedPkgs.add(pkgName);
                    }
                }
            }
            if (changedPkgs.size() > 0) {
                synchronized (com.android.server.job.controllers.FlexibilityController.this.mLock) {
                    com.android.server.job.controllers.FlexibilityController.this.mPackagesToCheck.addAll((android.util.ArraySet) changedPkgs);
                    com.android.server.job.controllers.FlexibilityController.this.mHandler.sendEmptyMessage(2);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePowerAllowlistCache() {
            if (this.mDeviceIdleInternal == null) {
                return;
            }
            java.lang.String[] allowlistedPkgs = this.mDeviceIdleInternal.getFullPowerWhitelistExceptIdle();
            android.util.ArraySet<java.lang.String> changedPkgs = new android.util.ArraySet<>();
            synchronized (this.mSatLock) {
                changedPkgs.addAll((android.util.ArraySet<? extends java.lang.String>) this.mPowerAllowlistedApps);
                this.mPowerAllowlistedApps.clear();
                for (java.lang.String pkgName : allowlistedPkgs) {
                    this.mPowerAllowlistedApps.add(pkgName);
                    if (!changedPkgs.remove(pkgName)) {
                        changedPkgs.add(pkgName);
                    }
                }
            }
            updateSpecialAppSetUnlocked(-1, changedPkgs);
        }

        class LogicalIndexCarrierPrivilegesCallback implements android.telephony.TelephonyManager.CarrierPrivilegesCallback {
            public final int logicalIndex;

            LogicalIndexCarrierPrivilegesCallback(int logicalIndex) {
                this.logicalIndex = logicalIndex;
            }

            public void onCarrierPrivilegesChanged(java.util.Set<java.lang.String> privilegedPackageNames, java.util.Set<java.lang.Integer> privilegedUids) {
                android.util.ArraySet<java.lang.String> changedPkgs = new android.util.ArraySet<>();
                synchronized (com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this.mSatLock) {
                    android.util.ArraySet<java.lang.String> oldPrivilegedSet = com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this.mCarrierPrivilegedApps.get(this.logicalIndex);
                    if (oldPrivilegedSet != null) {
                        changedPkgs.addAll((android.util.ArraySet<? extends java.lang.String>) oldPrivilegedSet);
                        com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this.mCarrierPrivilegedApps.remove(this.logicalIndex);
                    }
                    for (java.lang.String pkgName : privilegedPackageNames) {
                        com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this.mCarrierPrivilegedApps.add(this.logicalIndex, pkgName);
                        if (!changedPkgs.remove(pkgName)) {
                            changedPkgs.add(pkgName);
                        }
                    }
                }
                com.android.server.job.controllers.FlexibilityController.SpecialAppTracker.this.updateSpecialAppSetUnlocked(-1, changedPkgs);
            }
        }

        public void dump(android.util.IndentingPrintWriter pw) {
            pw.println("Special apps:");
            pw.increaseIndent();
            synchronized (this.mSatLock) {
                for (int u = 0; u < this.mSpecialApps.size(); u++) {
                    pw.print("User ");
                    pw.print(this.mSpecialApps.keyAt(u));
                    pw.print(": ");
                    pw.println(this.mSpecialApps.valuesAt(u));
                }
                pw.println();
                pw.println("Carrier privileged packages:");
                pw.increaseIndent();
                for (int i = 0; i < this.mCarrierPrivilegedApps.size(); i++) {
                    pw.print(this.mCarrierPrivilegedApps.keyAt(i));
                    pw.print(": ");
                    pw.println(this.mCarrierPrivilegedApps.valuesAt(i));
                }
                pw.decreaseIndent();
                pw.println();
                pw.print("Power allowlisted packages: ");
                pw.println(this.mPowerAllowlistedApps);
            }
            pw.decreaseIndent();
        }
    }

    public void setLocalPolicyForTesting(boolean override, int appliedConstraints) {
        synchronized (this.mLock) {
            boolean recheckJobs = (this.mLocalOverride == override && this.mAppliedConstraints == appliedConstraints) ? false : true;
            this.mLocalOverride = override;
            if (this.mLocalOverride) {
                this.mAppliedConstraints = appliedConstraints;
            } else {
                this.mAppliedConstraints = this.mFcConfig.APPLIED_CONSTRAINTS;
            }
            if (recheckJobs) {
                this.mHandler.obtainMessage(0).sendToTarget();
            }
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
        this.mFcConfig.dump(pw);
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final android.util.IndentingPrintWriter pw, java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        if (this.mLocalOverride) {
            pw.println("Local override active");
        }
        pw.print("Applied Flexible Constraints:");
        com.android.server.job.controllers.JobStatus.dumpConstraints(pw, this.mAppliedConstraints);
        pw.println();
        pw.print("Satisfied Flexible Constraints:");
        com.android.server.job.controllers.JobStatus.dumpConstraints(pw, this.mSatisfiedFlexibleConstraints);
        pw.println();
        pw.println();
        final long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        pw.println("Time since constraint combos last seen:");
        pw.increaseIndent();
        for (int i = 0; i < this.mLastSeenConstraintTimesElapsed.size(); i++) {
            int constraints = this.mLastSeenConstraintTimesElapsed.keyAt(i);
            if (constraints == this.mSatisfiedFlexibleConstraints) {
                pw.print("0ms");
            } else {
                android.util.TimeUtils.formatDuration(this.mLastSeenConstraintTimesElapsed.valueAt(i), nowElapsed, pw);
            }
            pw.print(":");
            if (constraints != 0) {
                com.android.server.job.controllers.JobStatus.dumpConstraints(pw, constraints);
            } else {
                pw.print(" none");
            }
            pw.println();
        }
        pw.decreaseIndent();
        pw.println();
        this.mSpecialAppTracker.dump(pw);
        pw.println();
        this.mFlexibilityTracker.dump(pw, predicate, nowElapsed);
        pw.println();
        pw.println("Job scores:");
        pw.increaseIndent();
        this.mJobScoreTrackers.forEach(new android.util.SparseArrayMap.TriConsumer() { // from class: com.android.server.job.controllers.FlexibilityController$$ExternalSyntheticLambda0
            public final void accept(int i2, java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.job.controllers.FlexibilityController.lambda$dumpControllerStateLocked$1(pw, nowElapsed, i2, (java.lang.String) obj, (com.android.server.job.controllers.FlexibilityController.JobScoreTracker) obj2);
            }
        });
        pw.decreaseIndent();
        pw.println();
        this.mFlexibilityAlarmQueue.dump(pw);
    }

    static /* synthetic */ void lambda$dumpControllerStateLocked$1(android.util.IndentingPrintWriter pw, long nowElapsed, int uid, java.lang.String pkgName, com.android.server.job.controllers.FlexibilityController.JobScoreTracker jobScoreTracker) {
        pw.print(uid);
        pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
        pw.print(pkgName);
        pw.print(": ");
        jobScoreTracker.dump(pw, nowElapsed);
        pw.println();
    }
}
