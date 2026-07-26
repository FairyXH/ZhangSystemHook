package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class GentleUpdateHelper {
    private static final int JOB_ID = 235306967;
    private static final long PENDING_CHECK_MILLIS = java.util.concurrent.TimeUnit.SECONDS.toMillis(10);
    private static final java.lang.String TAG = "GentleUpdateHelper";
    private final com.android.server.pm.AppStateHelper mAppStateHelper;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private boolean mHasPendingIdleJob;
    private final java.util.ArrayDeque<com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck> mPendingChecks = new java.util.ArrayDeque<>();
    private final java.util.ArrayList<java.util.concurrent.CompletableFuture<java.lang.Boolean>> mPendingIdleFutures = new java.util.ArrayList<>();

    public static class Service extends android.app.job.JobService {
        @Override // android.app.job.JobService
        public boolean onStartJob(android.app.job.JobParameters params) {
            try {
                com.android.server.pm.PackageInstallerService pis = android.app.ActivityThread.getPackageManager().getPackageInstaller();
                final com.android.server.pm.GentleUpdateHelper helper = pis.getGentleUpdateHelper();
                android.os.Handler handler = helper.mHandler;
                java.util.Objects.requireNonNull(helper);
                handler.post(new java.lang.Runnable() { // from class: com.android.server.pm.GentleUpdateHelper$Service$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        helper.runIdleJob();
                    }
                });
                return false;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.pm.GentleUpdateHelper.TAG, "Failed to get PackageInstallerService", e);
                return false;
            }
        }

        @Override // android.app.job.JobService
        public boolean onStopJob(android.app.job.JobParameters params) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class PendingInstallConstraintsCheck {
        public final android.content.pm.PackageInstaller.InstallConstraints constraints;
        public final java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> future;
        private final long mFinishTime;
        public final java.util.List<java.lang.String> packageNames;

        PendingInstallConstraintsCheck(java.util.List<java.lang.String> packageNames, android.content.pm.PackageInstaller.InstallConstraints constraints, java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> future, long timeoutMillis) {
            this.packageNames = packageNames;
            this.constraints = constraints;
            this.future = future;
            this.mFinishTime = android.os.SystemClock.elapsedRealtime() + java.lang.Math.max(0L, java.lang.Math.min(com.android.server.usage.UnixCalendar.WEEK_IN_MILLIS, timeoutMillis));
        }

        public boolean isTimedOut() {
            return android.os.SystemClock.elapsedRealtime() >= this.mFinishTime;
        }

        public long getRemainingTimeMillis() {
            long timeout = this.mFinishTime - android.os.SystemClock.elapsedRealtime();
            return java.lang.Math.max(timeout, 0L);
        }

        void dump(com.android.internal.util.IndentingPrintWriter pw) {
            pw.printPair(com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY, this.packageNames);
            pw.println();
            pw.printPair("finishTime", java.lang.Long.valueOf(this.mFinishTime));
            pw.println();
            pw.printPair("constraints notInCallRequired", java.lang.Boolean.valueOf(this.constraints.isNotInCallRequired()));
            pw.println();
            pw.printPair("constraints deviceIdleRequired", java.lang.Boolean.valueOf(this.constraints.isDeviceIdleRequired()));
            pw.println();
            pw.printPair("constraints appNotForegroundRequired", java.lang.Boolean.valueOf(this.constraints.isAppNotForegroundRequired()));
            pw.println();
            pw.printPair("constraints appNotInteractingRequired", java.lang.Boolean.valueOf(this.constraints.isAppNotInteractingRequired()));
            pw.println();
            pw.printPair("constraints appNotTopVisibleRequired", java.lang.Boolean.valueOf(this.constraints.isAppNotTopVisibleRequired()));
        }
    }

    GentleUpdateHelper(android.content.Context context, android.os.Looper looper, com.android.server.pm.AppStateHelper appStateHelper) {
        this.mContext = context;
        this.mHandler = new android.os.Handler(looper);
        this.mAppStateHelper = appStateHelper;
    }

    void systemReady() {
        android.app.ActivityManager am = (android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class);
        am.addOnUidImportanceListener(new android.app.ActivityManager.OnUidImportanceListener() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda5
            public final void onUidImportance(int i, int i2) {
                this.f$0.onUidImportance(i, i2);
            }
        }, 100);
        am.addOnUidImportanceListener(new android.app.ActivityManager.OnUidImportanceListener() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda5
            public final void onUidImportance(int i, int i2) {
                this.f$0.onUidImportance(i, i2);
            }
        }, 125);
    }

    java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> checkInstallConstraints(final java.util.List<java.lang.String> packageNames, final android.content.pm.PackageInstaller.InstallConstraints constraints, final long timeoutMillis) {
        final java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> resultFuture = new java.util.concurrent.CompletableFuture<>();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$checkInstallConstraints$2(packageNames, constraints, resultFuture, timeoutMillis);
            }
        });
        return resultFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkInstallConstraints$2(java.util.List packageNames, android.content.pm.PackageInstaller.InstallConstraints constraints, java.util.concurrent.CompletableFuture resultFuture, long timeoutMillis) {
        final com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck pendingCheck = new com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck(packageNames, constraints, resultFuture, timeoutMillis);
        java.util.concurrent.CompletableFuture<java.lang.Boolean> deviceIdleFuture = constraints.isDeviceIdleRequired() ? checkDeviceIdle() : java.util.concurrent.CompletableFuture.completedFuture(false);
        deviceIdleFuture.thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$checkInstallConstraints$1(pendingCheck, (java.lang.Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkInstallConstraints$1(final com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck pendingCheck, java.lang.Boolean isIdle) {
        com.android.internal.util.Preconditions.checkState(this.mHandler.getLooper().isCurrentThread());
        if (!processPendingCheck(pendingCheck, isIdle.booleanValue())) {
            this.mPendingChecks.add(pendingCheck);
            scheduleIdleJob();
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$checkInstallConstraints$0(pendingCheck);
                }
            }, pendingCheck.getRemainingTimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkInstallConstraints$0(com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck pendingCheck) {
        processPendingCheck(pendingCheck, false);
    }

    private java.util.concurrent.CompletableFuture<java.lang.Boolean> checkDeviceIdle() {
        final java.util.concurrent.CompletableFuture<java.lang.Boolean> future = new java.util.concurrent.CompletableFuture<>();
        this.mPendingIdleFutures.add(future);
        scheduleIdleJob();
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                future.complete(false);
            }
        }, PENDING_CHECK_MILLIS);
        return future;
    }

    private void scheduleIdleJob() {
        boolean isIdle = android.os.SystemProperties.getBoolean("debug.pm.gentle_update_test.is_idle", false);
        if (isIdle) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.runIdleJob();
                }
            });
            return;
        }
        if (this.mHasPendingIdleJob) {
            return;
        }
        this.mHasPendingIdleJob = true;
        android.content.ComponentName componentName = new android.content.ComponentName(this.mContext.getPackageName(), com.android.server.pm.GentleUpdateHelper.Service.class.getName());
        android.app.job.JobInfo jobInfo = new android.app.job.JobInfo.Builder(JOB_ID, componentName).setRequiresDeviceIdle(true).build();
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler) this.mContext.getSystemService(android.app.job.JobScheduler.class);
        jobScheduler.schedule(jobInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runIdleJob() {
        this.mHasPendingIdleJob = false;
        processPendingChecksInIdle();
        for (java.util.concurrent.CompletableFuture<java.lang.Boolean> f : this.mPendingIdleFutures) {
            f.complete(true);
        }
        this.mPendingIdleFutures.clear();
    }

    private boolean areConstraintsSatisfied(java.util.List<java.lang.String> packageNames, android.content.pm.PackageInstaller.InstallConstraints constraints, boolean isIdle) {
        return (!constraints.isDeviceIdleRequired() || isIdle) && !((constraints.isAppNotForegroundRequired() && this.mAppStateHelper.hasForegroundApp(packageNames)) || ((constraints.isAppNotInteractingRequired() && this.mAppStateHelper.hasInteractingApp(packageNames)) || ((constraints.isAppNotTopVisibleRequired() && this.mAppStateHelper.hasTopVisibleApp(packageNames)) || (constraints.isNotInCallRequired() && this.mAppStateHelper.isInCall()))));
    }

    private boolean processPendingCheck(com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck pendingCheck, boolean isIdle) {
        java.util.concurrent.CompletableFuture<android.content.pm.PackageInstaller.InstallConstraintsResult> future = pendingCheck.future;
        if (future.isDone()) {
            return true;
        }
        android.content.pm.PackageInstaller.InstallConstraints constraints = pendingCheck.constraints;
        java.util.List<java.lang.String> packageNames = this.mAppStateHelper.getDependencyPackages(pendingCheck.packageNames);
        boolean satisfied = areConstraintsSatisfied(packageNames, constraints, isIdle);
        if (satisfied || pendingCheck.isTimedOut()) {
            future.complete(new android.content.pm.PackageInstaller.InstallConstraintsResult(satisfied));
            return true;
        }
        return false;
    }

    private void processPendingChecksInIdle() {
        int size = this.mPendingChecks.size();
        for (int i = 0; i < size; i++) {
            com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck pendingCheck = this.mPendingChecks.remove();
            if (!processPendingCheck(pendingCheck, true)) {
                this.mPendingChecks.add(pendingCheck);
            }
        }
        if (!this.mPendingChecks.isEmpty()) {
            scheduleIdleJob();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onUidImportance, reason: merged with bridge method [inline-methods] */
    public void lambda$onUidImportance$4(java.lang.String packageName, int importance) {
        int size = this.mPendingChecks.size();
        for (int i = 0; i < size; i++) {
            com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck pendingCheck = this.mPendingChecks.remove();
            java.util.List<java.lang.String> dependencyPackages = this.mAppStateHelper.getDependencyPackages(pendingCheck.packageNames);
            if (!dependencyPackages.contains(packageName) || !processPendingCheck(pendingCheck, false)) {
                this.mPendingChecks.add(pendingCheck);
            }
        }
        if (!this.mPendingChecks.isEmpty()) {
            scheduleIdleJob();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUidImportance(int uid, final int importance) {
        android.content.pm.IPackageManager pm = android.app.ActivityThread.getPackageManager();
        try {
            final java.lang.String packageName = pm.getNameForUid(uid);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.GentleUpdateHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUidImportance$4(packageName, importance);
                }
            });
        } catch (android.os.RemoteException e) {
        }
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("Gentle update with constraints info:");
        pw.increaseIndent();
        pw.printPair("hasPendingIdleJob", java.lang.Boolean.valueOf(this.mHasPendingIdleJob));
        pw.println();
        pw.printPair("Num of PendingIdleFutures", java.lang.Integer.valueOf(this.mPendingIdleFutures.size()));
        pw.println();
        java.util.ArrayDeque<com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck> pendingChecks = this.mPendingChecks.clone();
        int size = pendingChecks.size();
        pw.printPair("Num of PendingChecks", java.lang.Integer.valueOf(size));
        pw.println();
        pw.increaseIndent();
        for (int i = 0; i < size; i++) {
            pw.print(i);
            pw.print(":");
            com.android.server.pm.GentleUpdateHelper.PendingInstallConstraintsCheck pendingInstallConstraintsCheck = pendingChecks.remove();
            pendingInstallConstraintsCheck.dump(pw);
            pw.println();
        }
    }
}
