package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public class PrefetchController extends com.android.server.job.controllers.StateController {
    private static final boolean DEBUG;
    private static final int MSG_PROCESS_TOP_STATE_CHANGE = 2;
    private static final int MSG_PROCESS_UPDATED_ESTIMATED_LAUNCH_TIME = 1;
    private static final int MSG_RETRIEVE_ESTIMATED_LAUNCH_TIME = 0;
    private static final java.lang.String TAG = "JobScheduler.Prefetch";
    private android.appwidget.AppWidgetManager mAppWidgetManager;
    private final android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener mEstimatedLaunchTimeChangedListener;
    private final android.util.SparseArrayMap<java.lang.String, java.lang.Long> mEstimatedLaunchTimes;
    private final com.android.server.job.controllers.PrefetchController.PcHandler mHandler;
    private long mLaunchTimeAllowanceMs;
    private long mLaunchTimeThresholdMs;
    private final com.android.server.job.controllers.PrefetchController.PcConstants mPcConstants;
    private final android.util.ArraySet<com.android.server.job.controllers.PrefetchController.PrefetchChangedListener> mPrefetchChangedListeners;
    private final com.android.server.job.controllers.PrefetchController.ThresholdAlarmListener mThresholdAlarmListener;
    private final android.util.SparseArrayMap<java.lang.String, android.util.ArraySet<com.android.server.job.controllers.JobStatus>> mTrackedJobs;
    private final android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal;

    public interface PrefetchChangedListener {
        void onPrefetchCacheUpdated(android.util.ArraySet<com.android.server.job.controllers.JobStatus> arraySet, int i, java.lang.String str, long j, long j2, long j3);
    }

    static {
        DEBUG = com.android.server.job.JobSchedulerService.DEBUG || android.util.Log.isLoggable(TAG, 3);
    }

    public PrefetchController(com.android.server.job.JobSchedulerService service) {
        super(service);
        this.mTrackedJobs = new android.util.SparseArrayMap<>();
        this.mEstimatedLaunchTimes = new android.util.SparseArrayMap<>();
        this.mPrefetchChangedListeners = new android.util.ArraySet<>();
        this.mLaunchTimeThresholdMs = 3600000L;
        this.mLaunchTimeAllowanceMs = 1800000L;
        this.mEstimatedLaunchTimeChangedListener = new android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener() { // from class: com.android.server.job.controllers.PrefetchController.1
            @Override // android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener
            public void onEstimatedLaunchTimeChanged(int userId, java.lang.String packageName, long newEstimatedLaunchTime) {
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = packageName;
                args.argi1 = userId;
                args.argl1 = newEstimatedLaunchTime;
                com.android.server.job.controllers.PrefetchController.this.mHandler.obtainMessage(1, args).sendToTarget();
            }
        };
        this.mPcConstants = new com.android.server.job.controllers.PrefetchController.PcConstants();
        this.mHandler = new com.android.server.job.controllers.PrefetchController.PcHandler(com.android.server.AppSchedulingModuleThread.get().getLooper());
        this.mThresholdAlarmListener = new com.android.server.job.controllers.PrefetchController.ThresholdAlarmListener(this.mContext, com.android.server.AppSchedulingModuleThread.get().getLooper());
        this.mUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
    }

    @Override // com.android.server.job.controllers.StateController
    public void startTrackingLocked() {
        this.mUsageStatsManagerInternal.registerLaunchTimeChangedListener(this.mEstimatedLaunchTimeChangedListener);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onSystemServicesReady() {
        this.mAppWidgetManager = (android.appwidget.AppWidgetManager) this.mContext.getSystemService(android.appwidget.AppWidgetManager.class);
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStartTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus lastJob) {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs;
        if (jobStatus.getJob().isPrefetch()) {
            int userId = jobStatus.getSourceUserId();
            java.lang.String pkgName = jobStatus.getSourcePackageName();
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs2 = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
            if (jobs2 != null) {
                jobs = jobs2;
            } else {
                android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs3 = new android.util.ArraySet<>();
                this.mTrackedJobs.add(userId, pkgName, jobs3);
                jobs = jobs3;
            }
            long now = com.android.server.job.JobSchedulerService.sSystemClock.millis();
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            if (jobs.add(jobStatus) && jobs.size() == 1 && !willBeLaunchedSoonLocked(userId, pkgName, now)) {
                updateThresholdAlarmLocked(userId, pkgName, now, nowElapsed);
            }
            updateConstraintLocked(jobStatus, now, nowElapsed);
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void maybeStopTrackingJobLocked(com.android.server.job.controllers.JobStatus jobStatus, com.android.server.job.controllers.JobStatus incomingJob) {
        int userId = jobStatus.getSourceUserId();
        java.lang.String pkgName = jobStatus.getSourcePackageName();
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
        if (jobs != null && jobs.remove(jobStatus) && jobs.size() == 0) {
            this.mThresholdAlarmListener.removeAlarmForKey(android.content.pm.UserPackage.of(userId, pkgName));
        }
    }

    @Override // com.android.server.job.controllers.StateController
    public void onAppRemovedLocked(java.lang.String packageName, int uid) {
        if (packageName == null) {
            android.util.Slog.wtf(TAG, "Told app removed but given null package name.");
            return;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        this.mTrackedJobs.delete(userId, packageName);
        this.mEstimatedLaunchTimes.delete(userId, packageName);
        this.mThresholdAlarmListener.removeAlarmForKey(android.content.pm.UserPackage.of(userId, packageName));
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUserRemovedLocked(int userId) {
        this.mTrackedJobs.delete(userId);
        this.mEstimatedLaunchTimes.delete(userId);
        this.mThresholdAlarmListener.removeAlarmsForUserId(userId);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onUidBiasChangedLocked(int uid, int prevBias, int newBias) {
        boolean isNowTop = newBias == 40;
        boolean wasTop = prevBias == 40;
        if (isNowTop != wasTop) {
            this.mHandler.obtainMessage(2, uid, 0).sendToTarget();
        }
    }

    public long getNextEstimatedLaunchTimeLocked(com.android.server.job.controllers.JobStatus jobStatus) {
        int userId = jobStatus.getSourceUserId();
        java.lang.String pkgName = jobStatus.getSourcePackageName();
        return getNextEstimatedLaunchTimeLocked(userId, pkgName, com.android.server.job.JobSchedulerService.sSystemClock.millis());
    }

    private long getNextEstimatedLaunchTimeLocked(int userId, java.lang.String pkgName, long now) {
        java.lang.Long nextEstimatedLaunchTime = (java.lang.Long) this.mEstimatedLaunchTimes.get(userId, pkgName);
        if (nextEstimatedLaunchTime == null || nextEstimatedLaunchTime.longValue() < now - this.mLaunchTimeAllowanceMs) {
            this.mHandler.obtainMessage(0, userId, 0, pkgName).sendToTarget();
            this.mEstimatedLaunchTimes.add(userId, pkgName, Long.MAX_VALUE);
            return Long.MAX_VALUE;
        }
        return nextEstimatedLaunchTime.longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeUpdateConstraintForPkgLocked(long now, long nowElapsed, int userId, java.lang.String pkgName) {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
        if (jobs == null) {
            return false;
        }
        boolean changed = false;
        for (int i = 0; i < jobs.size(); i++) {
            com.android.server.job.controllers.JobStatus js = jobs.valueAt(i);
            changed |= updateConstraintLocked(js, now, nowElapsed);
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeUpdateConstraintForUid(int uid) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    android.util.ArraySet<java.lang.String> pkgs = this.mService.getPackagesForUidLocked(uid);
                    if (pkgs == null) {
                        return;
                    }
                    int userId = android.os.UserHandle.getUserId(uid);
                    android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
                    long now = com.android.server.job.JobSchedulerService.sSystemClock.millis();
                    long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                    for (int p = pkgs.size() - 1; p >= 0; p--) {
                        java.lang.String pkgName = pkgs.valueAt(p);
                        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
                        if (jobs != null) {
                            for (int i = 0; i < jobs.size(); i++) {
                                com.android.server.job.controllers.JobStatus js = jobs.valueAt(i);
                                if (updateConstraintLocked(js, now, nowElapsed)) {
                                    changedJobs.add(js);
                                }
                            }
                        }
                    }
                    if (changedJobs.size() > 0) {
                        this.mStateChangedListener.onControllerStateChanged(changedJobs);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v29, types: [java.lang.String] */
    public void processUpdatedEstimatedLaunchTime(int userId, java.lang.String pkgName, long newEstimatedLaunchTime) throws java.lang.Throwable {
        ?? r1;
        java.lang.Object obj;
        if (DEBUG) {
            r1 = "Estimated launch time for " + packageToString(userId, pkgName) + " changed to " + newEstimatedLaunchTime + " (" + android.util.TimeUtils.formatDuration(newEstimatedLaunchTime - com.android.server.job.JobSchedulerService.sSystemClock.millis()) + " from now)";
            android.util.Slog.d(TAG, (java.lang.String) r1);
        }
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                try {
                    android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
                    if (jobs == null) {
                        if (!DEBUG) {
                            obj = obj2;
                        } else {
                            android.util.Slog.i(TAG, "Not caching launch time since we haven't seen any prefetch jobs for " + packageToString(userId, pkgName));
                            obj = obj2;
                        }
                    } else {
                        long prevEstimatedLaunchTime = ((java.lang.Long) this.mEstimatedLaunchTimes.get(userId, pkgName)).longValue();
                        this.mEstimatedLaunchTimes.add(userId, pkgName, java.lang.Long.valueOf(newEstimatedLaunchTime));
                        if (jobs.isEmpty()) {
                            obj = obj2;
                        } else {
                            long now = com.android.server.job.JobSchedulerService.sSystemClock.millis();
                            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                            updateThresholdAlarmLocked(userId, pkgName, now, nowElapsed);
                            for (int i = 0; i < this.mPrefetchChangedListeners.size(); i++) {
                                this.mPrefetchChangedListeners.valueAt(i).onPrefetchCacheUpdated(jobs, userId, pkgName, prevEstimatedLaunchTime, newEstimatedLaunchTime, nowElapsed);
                            }
                            obj = obj2;
                            if (maybeUpdateConstraintForPkgLocked(now, nowElapsed, userId, pkgName)) {
                                this.mStateChangedListener.onControllerStateChanged(jobs);
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                r1 = obj2;
                throw th;
            }
        }
    }

    private boolean updateConstraintLocked(com.android.server.job.controllers.JobStatus jobStatus, long now, long nowElapsed) {
        boolean satisfied;
        boolean z = true;
        boolean appIsOpen = this.mService.getUidBias(jobStatus.getSourceUid()) == 40;
        if (!appIsOpen) {
            int userId = jobStatus.getSourceUserId();
            java.lang.String pkgName = jobStatus.getSourcePackageName();
            if (!willBeLaunchedSoonLocked(userId, pkgName, now) && (this.mAppWidgetManager == null || !this.mAppWidgetManager.isBoundWidgetPackage(pkgName, userId))) {
                z = false;
            }
            satisfied = z;
        } else {
            satisfied = this.mService.isCurrentlyRunningLocked(jobStatus);
        }
        return jobStatus.setPrefetchConstraintSatisfied(nowElapsed, satisfied);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThresholdAlarmLocked(int userId, java.lang.String pkgName, long now, long nowElapsed) {
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> jobs = (android.util.ArraySet) this.mTrackedJobs.get(userId, pkgName);
        if (jobs == null || jobs.size() == 0) {
            this.mThresholdAlarmListener.removeAlarmForKey(android.content.pm.UserPackage.of(userId, pkgName));
            return;
        }
        long nextEstimatedLaunchTime = getNextEstimatedLaunchTimeLocked(userId, pkgName, now);
        if (nextEstimatedLaunchTime != Long.MAX_VALUE && nextEstimatedLaunchTime - now > this.mLaunchTimeThresholdMs) {
            long timeToCrossThresholdMs = nextEstimatedLaunchTime - (this.mLaunchTimeThresholdMs + now);
            this.mThresholdAlarmListener.addAlarm(android.content.pm.UserPackage.of(userId, pkgName), nowElapsed + timeToCrossThresholdMs);
        } else {
            this.mThresholdAlarmListener.removeAlarmForKey(android.content.pm.UserPackage.of(userId, pkgName));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean willBeLaunchedSoonLocked(int userId, java.lang.String pkgName, long now) {
        return getNextEstimatedLaunchTimeLocked(userId, pkgName, now) <= (this.mLaunchTimeThresholdMs + now) - this.mLaunchTimeAllowanceMs;
    }

    @Override // com.android.server.job.controllers.StateController
    public void prepareForUpdatedConstantsLocked() {
        this.mPcConstants.mShouldReevaluateConstraints = false;
    }

    @Override // com.android.server.job.controllers.StateController
    public void processConstantLocked(android.provider.DeviceConfig.Properties properties, java.lang.String key) {
        this.mPcConstants.processConstantLocked(properties, key);
    }

    @Override // com.android.server.job.controllers.StateController
    public void onConstantsUpdatedLocked() {
        if (this.mPcConstants.mShouldReevaluateConstraints) {
            com.android.server.AppSchedulingModuleThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.job.controllers.PrefetchController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onConstantsUpdatedLocked$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConstantsUpdatedLocked$0() {
        int userId;
        int p;
        long now;
        android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
        synchronized (this.mLock) {
            long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            long now2 = com.android.server.job.JobSchedulerService.sSystemClock.millis();
            for (int u = 0; u < this.mTrackedJobs.numMaps(); u++) {
                int userId2 = this.mTrackedJobs.keyAt(u);
                int p2 = 0;
                while (p2 < this.mTrackedJobs.numElementsForKey(userId2)) {
                    java.lang.String packageName = (java.lang.String) this.mTrackedJobs.keyAt(u, p2);
                    if (maybeUpdateConstraintForPkgLocked(now2, nowElapsed, userId2, packageName)) {
                        changedJobs.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) this.mTrackedJobs.valueAt(u, p2));
                    }
                    if (willBeLaunchedSoonLocked(userId2, packageName, now2)) {
                        userId = userId2;
                        p = p2;
                        now = now2;
                    } else {
                        userId = userId2;
                        p = p2;
                        now = now2;
                        updateThresholdAlarmLocked(userId2, packageName, now2, nowElapsed);
                    }
                    p2 = p + 1;
                    userId2 = userId;
                    now2 = now;
                }
            }
        }
        if (changedJobs.size() > 0) {
            this.mStateChangedListener.onControllerStateChanged(changedJobs);
        }
    }

    private class ThresholdAlarmListener extends com.android.server.utils.AlarmQueue<android.content.pm.UserPackage> {
        private ThresholdAlarmListener(android.content.Context context, android.os.Looper looper) {
            super(context, looper, "*job.prefetch*", "Prefetch threshold", false, 360000L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(android.content.pm.UserPackage key, int userId) {
            return key.userId == userId;
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(android.util.ArraySet<android.content.pm.UserPackage> expired) {
            android.util.ArraySet<com.android.server.job.controllers.JobStatus> changedJobs = new android.util.ArraySet<>();
            synchronized (com.android.server.job.controllers.PrefetchController.this.mLock) {
                long now = com.android.server.job.JobSchedulerService.sSystemClock.millis();
                long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
                for (int i = 0; i < expired.size(); i++) {
                    android.content.pm.UserPackage p = expired.valueAt(i);
                    if (!com.android.server.job.controllers.PrefetchController.this.willBeLaunchedSoonLocked(p.userId, p.packageName, now)) {
                        android.util.Slog.e(com.android.server.job.controllers.PrefetchController.TAG, "Alarm expired for " + com.android.server.job.controllers.StateController.packageToString(p.userId, p.packageName) + " at the wrong time");
                        com.android.server.job.controllers.PrefetchController.this.updateThresholdAlarmLocked(p.userId, p.packageName, now, nowElapsed);
                    } else if (com.android.server.job.controllers.PrefetchController.this.maybeUpdateConstraintForPkgLocked(now, nowElapsed, p.userId, p.packageName)) {
                        changedJobs.addAll((android.util.ArraySet<? extends com.android.server.job.controllers.JobStatus>) com.android.server.job.controllers.PrefetchController.this.mTrackedJobs.get(p.userId, p.packageName));
                    }
                }
            }
            if (changedJobs.size() > 0) {
                com.android.server.job.controllers.PrefetchController.this.mStateChangedListener.onControllerStateChanged(changedJobs);
            }
        }
    }

    void registerPrefetchChangedListener(com.android.server.job.controllers.PrefetchController.PrefetchChangedListener listener) {
        synchronized (this.mLock) {
            this.mPrefetchChangedListeners.add(listener);
        }
    }

    void unRegisterPrefetchChangedListener(com.android.server.job.controllers.PrefetchController.PrefetchChangedListener listener) {
        synchronized (this.mLock) {
            this.mPrefetchChangedListeners.remove(listener);
        }
    }

    private class PcHandler extends android.os.Handler {
        PcHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 0:
                    int userId = msg.arg1;
                    java.lang.String pkgName = (java.lang.String) msg.obj;
                    long nextEstimatedLaunchTime = com.android.server.job.controllers.PrefetchController.this.mUsageStatsManagerInternal.getEstimatedPackageLaunchTime(pkgName, userId);
                    if (com.android.server.job.controllers.PrefetchController.DEBUG) {
                        android.util.Slog.d(com.android.server.job.controllers.PrefetchController.TAG, "Retrieved launch time for " + com.android.server.job.controllers.StateController.packageToString(userId, pkgName) + " of " + nextEstimatedLaunchTime + " (" + android.util.TimeUtils.formatDuration(nextEstimatedLaunchTime - com.android.server.job.JobSchedulerService.sSystemClock.millis()) + " from now)");
                    }
                    synchronized (com.android.server.job.controllers.PrefetchController.this.mLock) {
                        java.lang.Long curEstimatedLaunchTime = (java.lang.Long) com.android.server.job.controllers.PrefetchController.this.mEstimatedLaunchTimes.get(userId, pkgName);
                        if (curEstimatedLaunchTime == null || nextEstimatedLaunchTime != curEstimatedLaunchTime.longValue()) {
                            com.android.server.job.controllers.PrefetchController.this.processUpdatedEstimatedLaunchTime(userId, pkgName, nextEstimatedLaunchTime);
                        }
                        break;
                    }
                    return;
                case 1:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    com.android.server.job.controllers.PrefetchController.this.processUpdatedEstimatedLaunchTime(args.argi1, (java.lang.String) args.arg1, args.argl1);
                    args.recycle();
                    return;
                case 2:
                    int uid = msg.arg1;
                    com.android.server.job.controllers.PrefetchController.this.maybeUpdateConstraintForUid(uid);
                    return;
                default:
                    return;
            }
        }
    }

    class PcConstants {
        private static final long DEFAULT_LAUNCH_TIME_ALLOWANCE_MS = 1800000;
        private static final long DEFAULT_LAUNCH_TIME_THRESHOLD_MS = 3600000;
        static final java.lang.String KEY_LAUNCH_TIME_ALLOWANCE_MS = "pc_launch_time_allowance_ms";
        static final java.lang.String KEY_LAUNCH_TIME_THRESHOLD_MS = "pc_launch_time_threshold_ms";
        private static final java.lang.String PC_CONSTANT_PREFIX = "pc_";
        private boolean mShouldReevaluateConstraints = false;
        public long LAUNCH_TIME_THRESHOLD_MS = 3600000;
        public long LAUNCH_TIME_ALLOWANCE_MS = 1800000;

        PcConstants() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:11:0x001f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void processConstantLocked(android.provider.DeviceConfig.Properties r9, java.lang.String r10) {
            /*
                r8 = this;
                int r0 = r10.hashCode()
                r1 = 1
                switch(r0) {
                    case 1521894047: goto L14;
                    case 1566126444: goto L9;
                    default: goto L8;
                }
            L8:
                goto L1f
            L9:
                java.lang.String r0 = "pc_launch_time_threshold_ms"
                boolean r0 = r10.equals(r0)
                if (r0 == 0) goto L8
                r0 = r1
                goto L20
            L14:
                java.lang.String r0 = "pc_launch_time_allowance_ms"
                boolean r0 = r10.equals(r0)
                if (r0 == 0) goto L8
                r0 = 0
                goto L20
            L1f:
                r0 = -1
            L20:
                switch(r0) {
                    case 0: goto L5f;
                    case 1: goto L24;
                    default: goto L23;
                }
            L23:
                goto L89
            L24:
                r2 = 3600000(0x36ee80, double:1.7786363E-317)
                long r4 = r9.getLong(r10, r2)
                r8.LAUNCH_TIME_THRESHOLD_MS = r4
                long r4 = r8.LAUNCH_TIME_THRESHOLD_MS
                long r2 = java.lang.Math.max(r2, r4)
                r4 = 86400000(0x5265c00, double:4.2687272E-316)
                long r2 = java.lang.Math.min(r4, r2)
                com.android.server.job.controllers.PrefetchController r0 = com.android.server.job.controllers.PrefetchController.this
                long r4 = com.android.server.job.controllers.PrefetchController.m4685$$Nest$fgetmLaunchTimeThresholdMs(r0)
                int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
                if (r0 == 0) goto L89
                com.android.server.job.controllers.PrefetchController r0 = com.android.server.job.controllers.PrefetchController.this
                com.android.server.job.controllers.PrefetchController.m4690$$Nest$fputmLaunchTimeThresholdMs(r0, r2)
                r8.mShouldReevaluateConstraints = r1
                com.android.server.job.controllers.PrefetchController r0 = com.android.server.job.controllers.PrefetchController.this
                com.android.server.job.controllers.PrefetchController$ThresholdAlarmListener r0 = com.android.server.job.controllers.PrefetchController.m4686$$Nest$fgetmThresholdAlarmListener(r0)
                com.android.server.job.controllers.PrefetchController r1 = com.android.server.job.controllers.PrefetchController.this
                long r4 = com.android.server.job.controllers.PrefetchController.m4685$$Nest$fgetmLaunchTimeThresholdMs(r1)
                r6 = 10
                long r4 = r4 / r6
                r0.setMinTimeBetweenAlarmsMs(r4)
                goto L89
            L5f:
                r2 = 1800000(0x1b7740, double:8.89318E-318)
                long r2 = r9.getLong(r10, r2)
                r8.LAUNCH_TIME_ALLOWANCE_MS = r2
                long r2 = r8.LAUNCH_TIME_ALLOWANCE_MS
                r4 = 0
                long r2 = java.lang.Math.max(r4, r2)
                r4 = 7200000(0x6ddd00, double:3.5572727E-317)
                long r2 = java.lang.Math.min(r4, r2)
                com.android.server.job.controllers.PrefetchController r0 = com.android.server.job.controllers.PrefetchController.this
                long r4 = com.android.server.job.controllers.PrefetchController.m4684$$Nest$fgetmLaunchTimeAllowanceMs(r0)
                int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
                if (r0 == 0) goto L89
                com.android.server.job.controllers.PrefetchController r0 = com.android.server.job.controllers.PrefetchController.this
                com.android.server.job.controllers.PrefetchController.m4689$$Nest$fputmLaunchTimeAllowanceMs(r0, r2)
                r8.mShouldReevaluateConstraints = r1
            L89:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.controllers.PrefetchController.PcConstants.processConstantLocked(android.provider.DeviceConfig$Properties, java.lang.String):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(android.util.IndentingPrintWriter pw) {
            pw.println();
            pw.print(com.android.server.job.controllers.PrefetchController.class.getSimpleName());
            pw.println(":");
            pw.increaseIndent();
            pw.print(KEY_LAUNCH_TIME_THRESHOLD_MS, java.lang.Long.valueOf(this.LAUNCH_TIME_THRESHOLD_MS)).println();
            pw.print(KEY_LAUNCH_TIME_ALLOWANCE_MS, java.lang.Long.valueOf(this.LAUNCH_TIME_ALLOWANCE_MS)).println();
            pw.decreaseIndent();
        }
    }

    long getLaunchTimeAllowanceMs() {
        return this.mLaunchTimeAllowanceMs;
    }

    long getLaunchTimeThresholdMs() {
        return this.mLaunchTimeThresholdMs;
    }

    com.android.server.job.controllers.PrefetchController.PcConstants getPcConstants() {
        return this.mPcConstants;
    }

    @Override // com.android.server.job.controllers.StateController
    public void dumpControllerStateLocked(final android.util.IndentingPrintWriter pw, final java.util.function.Predicate<com.android.server.job.controllers.JobStatus> predicate) {
        long now = com.android.server.job.JobSchedulerService.sSystemClock.millis();
        pw.println("Cached launch times:");
        pw.increaseIndent();
        for (int u = 0; u < this.mEstimatedLaunchTimes.numMaps(); u++) {
            int userId = this.mEstimatedLaunchTimes.keyAt(u);
            for (int p = 0; p < this.mEstimatedLaunchTimes.numElementsForKey(userId); p++) {
                java.lang.String pkgName = (java.lang.String) this.mEstimatedLaunchTimes.keyAt(u, p);
                long estimatedLaunchTime = ((java.lang.Long) this.mEstimatedLaunchTimes.valueAt(u, p)).longValue();
                pw.print(packageToString(userId, pkgName));
                pw.print(": ");
                pw.print(estimatedLaunchTime);
                pw.print(" (");
                android.util.TimeUtils.formatDuration(estimatedLaunchTime - now, pw, 19);
                pw.println(" from now)");
            }
        }
        pw.decreaseIndent();
        pw.println();
        this.mTrackedJobs.forEach(new java.util.function.Consumer() { // from class: com.android.server.job.controllers.PrefetchController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.job.controllers.PrefetchController.lambda$dumpControllerStateLocked$1(predicate, pw, (android.util.ArraySet) obj);
            }
        });
        pw.println();
        this.mThresholdAlarmListener.dump(pw);
    }

    static /* synthetic */ void lambda$dumpControllerStateLocked$1(java.util.function.Predicate predicate, android.util.IndentingPrintWriter pw, android.util.ArraySet jobs) {
        for (int j = 0; j < jobs.size(); j++) {
            com.android.server.job.controllers.JobStatus js = (com.android.server.job.controllers.JobStatus) jobs.valueAt(j);
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
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
        this.mPcConstants.dump(pw);
    }
}
