package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
public final class RollbackPackageHealthObserver implements com.android.server.PackageWatchdog.PackageHealthObserver {
    private static final java.lang.String NAME = "rollback-observer";
    private static final int PERSISTENT_MASK = 9;
    private static final java.lang.String PROP_DISABLE_HIGH_IMPACT_ROLLBACK_FLAG = "persist.device_config.configuration.disable_high_impact_rollback";
    private static final java.lang.String TAG = "RollbackPackageHealthObserver";
    private final com.android.server.pm.ApexManager mApexManager;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final java.io.File mLastStagedRollbackIdsFile;
    private final java.util.Set<java.lang.Integer> mPendingStagedRollbackIds;
    private boolean mTwoPhaseRollbackEnabled;
    private final java.io.File mTwoPhaseRollbackEnabledFile;

    public RollbackPackageHealthObserver(android.content.Context context, com.android.server.pm.ApexManager apexManager) {
        this.mPendingStagedRollbackIds = new android.util.ArraySet();
        this.mContext = context;
        android.os.HandlerThread handlerThread = new android.os.HandlerThread(TAG);
        handlerThread.start();
        this.mHandler = new android.os.Handler(handlerThread.getLooper());
        java.io.File dataDir = new java.io.File(android.os.Environment.getDataDirectory(), NAME);
        dataDir.mkdirs();
        this.mLastStagedRollbackIdsFile = new java.io.File(dataDir, "last-staged-rollback-ids");
        this.mTwoPhaseRollbackEnabledFile = new java.io.File(dataDir, "two-phase-rollback-enabled");
        com.android.server.PackageWatchdog.getInstance(this.mContext).registerHealthObserver(this);
        this.mApexManager = apexManager;
        if (android.os.SystemProperties.getBoolean("sys.boot_completed", false)) {
            this.mTwoPhaseRollbackEnabled = readBoolean(this.mTwoPhaseRollbackEnabledFile);
        } else {
            this.mTwoPhaseRollbackEnabled = false;
            writeBoolean(this.mTwoPhaseRollbackEnabledFile, false);
        }
    }

    RollbackPackageHealthObserver(android.content.Context context) {
        this(context, com.android.server.pm.ApexManager.getInstance());
    }

    @Override // com.android.server.PackageWatchdog.PackageHealthObserver
    public int onHealthCheckFailed(android.content.pm.VersionedPackage failedPackage, int failureReason, int mitigationCount) {
        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            java.util.List<android.content.rollback.RollbackInfo> availableRollbacks = getAvailableRollbacks();
            java.util.List<android.content.rollback.RollbackInfo> lowImpactRollbacks = getRollbacksAvailableForImpactLevel(availableRollbacks, 0);
            if (lowImpactRollbacks.isEmpty()) {
                return 0;
            }
            if (failureReason == 1 || getRollbackForPackage(failedPackage, lowImpactRollbacks) != null) {
                return 30;
            }
            return 70;
        }
        boolean anyRollbackAvailable = !((android.content.rollback.RollbackManager) this.mContext.getSystemService(android.content.rollback.RollbackManager.class)).getAvailableRollbacks().isEmpty();
        if ((failureReason == 1 && anyRollbackAvailable) || getAvailableRollback(failedPackage) != null) {
            return 30;
        }
        if (!anyRollbackAvailable) {
            return 0;
        }
        return 70;
    }

    @Override // com.android.server.PackageWatchdog.PackageHealthObserver
    public boolean execute(final android.content.pm.VersionedPackage failedPackage, final int rollbackReason, int mitigationCount) {
        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            final java.util.List<android.content.rollback.RollbackInfo> availableRollbacks = getAvailableRollbacks();
            if (rollbackReason == 1) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$execute$0(availableRollbacks, rollbackReason);
                    }
                });
                return true;
            }
            java.util.List<android.content.rollback.RollbackInfo> lowImpactRollbacks = getRollbacksAvailableForImpactLevel(availableRollbacks, 0);
            final android.content.rollback.RollbackInfo rollback = getRollbackForPackage(failedPackage, lowImpactRollbacks);
            if (rollback != null) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$execute$1(rollback, failedPackage, rollbackReason);
                    }
                });
            } else if (!lowImpactRollbacks.isEmpty()) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$execute$2(availableRollbacks, rollbackReason);
                    }
                });
            }
        } else {
            if (rollbackReason == 1) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$execute$3(rollbackReason);
                    }
                });
                return true;
            }
            final android.content.rollback.RollbackInfo rollback2 = getAvailableRollback(failedPackage);
            if (rollback2 != null) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$execute$4(rollback2, failedPackage, rollbackReason);
                    }
                });
            } else {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$execute$5(rollbackReason);
                    }
                });
            }
        }
        return true;
    }

    @Override // com.android.server.PackageWatchdog.PackageHealthObserver
    public int onBootLoop(int mitigationCount) {
        if (!com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            return 0;
        }
        java.util.List<android.content.rollback.RollbackInfo> availableRollbacks = getAvailableRollbacks();
        if (availableRollbacks.isEmpty()) {
            return 0;
        }
        int impact = getUserImpactBasedOnRollbackImpactLevel(availableRollbacks);
        return impact;
    }

    @Override // com.android.server.PackageWatchdog.PackageHealthObserver
    public boolean executeBootLoopMitigation(int mitigationCount) {
        if (com.android.internal.hidden_from_bootclasspath.android.crashrecovery.flags.Flags.recoverabilityDetection()) {
            java.util.List<android.content.rollback.RollbackInfo> availableRollbacks = getAvailableRollbacks();
            triggerLeastImpactLevelRollback(availableRollbacks, 5);
            return true;
        }
        return false;
    }

    @Override // com.android.server.PackageWatchdog.PackageHealthObserver
    public java.lang.String getName() {
        return NAME;
    }

    @Override // com.android.server.PackageWatchdog.PackageHealthObserver
    public boolean isPersistent() {
        return true;
    }

    @Override // com.android.server.PackageWatchdog.PackageHealthObserver
    public boolean mayObservePackage(java.lang.String packageName) {
        if (getAvailableRollbacks().isEmpty()) {
            return false;
        }
        return isPersistentSystemApp(packageName);
    }

    private java.util.List<android.content.rollback.RollbackInfo> getAvailableRollbacks() {
        return ((android.content.rollback.RollbackManager) this.mContext.getSystemService(android.content.rollback.RollbackManager.class)).getAvailableRollbacks();
    }

    private boolean isPersistentSystemApp(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            android.content.pm.ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            return (info.flags & 9) == 9;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void assertInWorkerThread() {
        com.android.internal.util.Preconditions.checkState(this.mHandler.getLooper().isCurrentThread());
    }

    void startObservingHealth(java.util.List<java.lang.String> packages, long durationMs) {
        com.android.server.PackageWatchdog.getInstance(this.mContext).startObservingHealth(this, packages, durationMs);
    }

    void notifyRollbackAvailable(final android.content.rollback.RollbackInfo rollback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyRollbackAvailable$6(rollback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyRollbackAvailable$6(android.content.rollback.RollbackInfo rollback) {
        if (isRebootlessApex(rollback)) {
            this.mTwoPhaseRollbackEnabled = true;
            writeBoolean(this.mTwoPhaseRollbackEnabledFile, true);
        }
    }

    private static boolean isRebootlessApex(android.content.rollback.RollbackInfo rollback) {
        if (!rollback.isStaged()) {
            for (android.content.rollback.PackageRollbackInfo info : rollback.getPackages()) {
                if (info.isApex()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    void onBootCompletedAsync() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBootCompletedAsync$7();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onBootCompleted, reason: merged with bridge method [inline-methods] */
    public void lambda$onBootCompletedAsync$7() {
        assertInWorkerThread();
        android.content.rollback.RollbackManager rollbackManager = (android.content.rollback.RollbackManager) this.mContext.getSystemService(android.content.rollback.RollbackManager.class);
        if (!rollbackManager.getAvailableRollbacks().isEmpty()) {
            com.android.server.PackageWatchdog.getInstance(this.mContext).scheduleCheckAndMitigateNativeCrashes();
        }
        android.util.SparseArray<java.lang.String> rollbackIds = popLastStagedRollbackIds();
        for (int i = 0; i < rollbackIds.size(); i++) {
            com.android.server.rollback.WatchdogRollbackLogger.logRollbackStatusOnBoot(this.mContext, rollbackIds.keyAt(i), rollbackIds.valueAt(i), rollbackManager.getRecentlyCommittedRollbacks());
        }
    }

    private android.content.rollback.RollbackInfo getAvailableRollback(android.content.pm.VersionedPackage failedPackage) {
        android.content.rollback.RollbackManager rollbackManager = (android.content.rollback.RollbackManager) this.mContext.getSystemService(android.content.rollback.RollbackManager.class);
        for (android.content.rollback.RollbackInfo rollback : rollbackManager.getAvailableRollbacks()) {
            for (android.content.rollback.PackageRollbackInfo packageRollback : rollback.getPackages()) {
                if (packageRollback.getVersionRolledBackFrom().equals(failedPackage)) {
                    return rollback;
                }
                if (packageRollback.isApkInApex() && packageRollback.getVersionRolledBackFrom().getPackageName().equals(failedPackage.getPackageName())) {
                    return rollback;
                }
            }
        }
        return null;
    }

    private android.content.rollback.RollbackInfo getRollbackForPackage(android.content.pm.VersionedPackage failedPackage, java.util.List<android.content.rollback.RollbackInfo> availableRollbacks) {
        if (failedPackage == null) {
            return null;
        }
        for (android.content.rollback.RollbackInfo rollback : availableRollbacks) {
            for (android.content.rollback.PackageRollbackInfo packageRollback : rollback.getPackages()) {
                if (packageRollback.getVersionRolledBackFrom().equals(failedPackage)) {
                    return rollback;
                }
                if (packageRollback.isApkInApex() && packageRollback.getVersionRolledBackFrom().getPackageName().equals(failedPackage.getPackageName())) {
                    return rollback;
                }
            }
        }
        return null;
    }

    private boolean markStagedSessionHandled(int rollbackId) {
        assertInWorkerThread();
        return this.mPendingStagedRollbackIds.remove(java.lang.Integer.valueOf(rollbackId));
    }

    private boolean isPendingStagedSessionsEmpty() {
        assertInWorkerThread();
        return this.mPendingStagedRollbackIds.isEmpty();
    }

    private static boolean readBoolean(java.io.File file) {
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            try {
                boolean z = fis.read() == 1;
                fis.close();
                return z;
            } finally {
            }
        } catch (java.io.IOException e) {
            return false;
        }
    }

    private static void writeBoolean(java.io.File file, boolean value) {
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            try {
                fos.write(value ? 1 : 0);
                fos.flush();
                android.os.FileUtils.sync(fos);
                fos.close();
            } finally {
            }
        } catch (java.io.IOException e) {
        }
    }

    private void saveStagedRollbackId(int stagedRollbackId, android.content.pm.VersionedPackage logPackage) {
        assertInWorkerThread();
        writeStagedRollbackId(this.mLastStagedRollbackIdsFile, stagedRollbackId, logPackage);
    }

    static void writeStagedRollbackId(java.io.File file, int stagedRollbackId, android.content.pm.VersionedPackage logPackage) {
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file, true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fos);
            java.lang.String logPackageName = logPackage != null ? logPackage.getPackageName() : "";
            pw.append((java.lang.CharSequence) java.lang.String.valueOf(stagedRollbackId)).append((java.lang.CharSequence) ",").append((java.lang.CharSequence) logPackageName);
            pw.println();
            pw.flush();
            android.os.FileUtils.sync(fos);
            pw.close();
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to save last staged rollback id", e);
            file.delete();
        }
    }

    private android.util.SparseArray<java.lang.String> popLastStagedRollbackIds() {
        assertInWorkerThread();
        try {
            return readStagedRollbackIds(this.mLastStagedRollbackIdsFile);
        } finally {
            this.mLastStagedRollbackIdsFile.delete();
        }
    }

    static android.util.SparseArray<java.lang.String> readStagedRollbackIds(java.io.File file) {
        android.util.SparseArray<java.lang.String> result = new android.util.SparseArray<>();
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            while (true) {
                java.lang.String line = reader.readLine();
                if (line != null) {
                    java.lang.String[] values = line.trim().split(",");
                    java.lang.String rollbackId = values[0];
                    java.lang.String logPackageName = "";
                    if (values.length > 1) {
                        logPackageName = values[1];
                    }
                    result.put(java.lang.Integer.parseInt(rollbackId), logPackageName);
                } else {
                    return result;
                }
            }
        } catch (java.lang.Exception e) {
            return new android.util.SparseArray<>();
        }
    }

    private boolean isModule(java.lang.String packageName) {
        java.lang.String apexPackageName = this.mApexManager.getActiveApexPackageNameContainingPackage(packageName);
        if (apexPackageName != null) {
            packageName = apexPackageName;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            return pm.getModuleInfo(packageName, 0) != null;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: rollbackPackage, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$execute$4(final android.content.rollback.RollbackInfo rollback, android.content.pm.VersionedPackage failedPackage, int rollbackReason) {
        java.lang.String failedPackageToLog;
        android.content.pm.VersionedPackage logPackageTemp;
        assertInWorkerThread();
        android.content.rollback.RollbackManager rollbackManager = (android.content.rollback.RollbackManager) this.mContext.getSystemService(android.content.rollback.RollbackManager.class);
        final int reasonToLog = com.android.server.rollback.WatchdogRollbackLogger.mapFailureReasonToMetric(rollbackReason);
        if (rollbackReason == 1) {
            failedPackageToLog = android.os.SystemProperties.get("sys.init.updatable_crashing_process_name", "");
        } else {
            java.lang.String failedPackageToLog2 = failedPackage.getPackageName();
            failedPackageToLog = failedPackageToLog2;
        }
        if (!isModule(failedPackage.getPackageName())) {
            logPackageTemp = null;
        } else {
            android.content.pm.VersionedPackage logPackageTemp2 = com.android.server.rollback.WatchdogRollbackLogger.getLogPackage(this.mContext, failedPackage);
            logPackageTemp = logPackageTemp2;
        }
        final android.content.pm.VersionedPackage logPackage = logPackageTemp;
        com.android.server.rollback.WatchdogRollbackLogger.logEvent(logPackage, 1, reasonToLog, failedPackageToLog);
        final java.lang.String str = failedPackageToLog;
        final java.util.function.Consumer<android.content.Intent> onResult = new java.util.function.Consumer() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$rollbackPackage$8(rollback, logPackage, reasonToLog, str, (android.content.Intent) obj);
            }
        };
        com.android.server.rollback.LocalIntentReceiver rollbackReceiver = new com.android.server.rollback.LocalIntentReceiver(new java.util.function.Consumer() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$rollbackPackage$10(onResult, (android.content.Intent) obj);
            }
        });
        rollbackManager.commitRollback(rollback.getRollbackId(), java.util.Collections.singletonList(failedPackage), rollbackReceiver.getIntentSender());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rollbackPackage$8(android.content.rollback.RollbackInfo rollback, android.content.pm.VersionedPackage logPackage, int reasonToLog, java.lang.String failedPackageToLog, android.content.Intent result) {
        assertInWorkerThread();
        int status = result.getIntExtra("android.content.rollback.extra.STATUS", 1);
        if (status == 0) {
            if (rollback.isStaged()) {
                int rollbackId = rollback.getRollbackId();
                saveStagedRollbackId(rollbackId, logPackage);
                com.android.server.rollback.WatchdogRollbackLogger.logEvent(logPackage, 4, reasonToLog, failedPackageToLog);
            } else {
                com.android.server.rollback.WatchdogRollbackLogger.logEvent(logPackage, 2, reasonToLog, failedPackageToLog);
            }
        } else {
            com.android.server.rollback.WatchdogRollbackLogger.logEvent(logPackage, 3, reasonToLog, failedPackageToLog);
        }
        if (rollback.isStaged()) {
            markStagedSessionHandled(rollback.getRollbackId());
            if (isPendingStagedSessionsEmpty()) {
                android.os.SystemProperties.set("persist.sys.reconcile.finish", "false");
                android.os.SystemProperties.set("persist.sys.firstreconcilece", "true");
                android.sysprop.CrashRecoveryProperties.attemptingReboot(true);
                ((android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class)).reboot("Rollback staged install");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rollbackPackage$10(final java.util.function.Consumer onResult, final android.content.Intent result) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                onResult.accept(result);
            }
        });
    }

    private boolean useTwoPhaseRollback(java.util.List<android.content.rollback.RollbackInfo> rollbacks) {
        assertInWorkerThread();
        if (!this.mTwoPhaseRollbackEnabled) {
            return false;
        }
        android.util.Slog.i(TAG, "Rolling back all rebootless APEX rollbacks");
        boolean found = false;
        for (android.content.rollback.RollbackInfo rollback : rollbacks) {
            if (isRebootlessApex(rollback)) {
                android.content.pm.VersionedPackage firstRollback = ((android.content.rollback.PackageRollbackInfo) rollback.getPackages().get(0)).getVersionRolledBackFrom();
                lambda$execute$4(rollback, firstRollback, 1);
                found = true;
            }
        }
        return found;
    }

    private void triggerLeastImpactLevelRollback(final java.util.List<android.content.rollback.RollbackInfo> availableRollbacks, final int rollbackReason) {
        int minRollbackImpactLevel = getMinRollbackImpactLevel(availableRollbacks);
        if (minRollbackImpactLevel == 0) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$triggerLeastImpactLevelRollback$11(availableRollbacks, rollbackReason);
                }
            });
        } else {
            if (minRollbackImpactLevel != 1 || android.os.SystemProperties.getBoolean(PROP_DISABLE_HIGH_IMPACT_ROLLBACK_FLAG, false)) {
                return;
            }
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$triggerLeastImpactLevelRollback$12(availableRollbacks, rollbackReason);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: rollbackHighImpact, reason: merged with bridge method [inline-methods] */
    public void lambda$triggerLeastImpactLevelRollback$12(java.util.List<android.content.rollback.RollbackInfo> availableRollbacks, int rollbackReason) {
        assertInWorkerThread();
        java.util.List<android.content.rollback.RollbackInfo> highImpactRollbacks = getRollbacksAvailableForImpactLevel(availableRollbacks, 1);
        java.util.List<android.content.rollback.RollbackInfo> sortedHighImpactRollbacks = highImpactRollbacks.stream().sorted(java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.content.rollback.PackageRollbackInfo) ((android.content.rollback.RollbackInfo) obj).getPackages().get(0)).getPackageName();
            }
        })).toList();
        android.content.pm.VersionedPackage firstRollback = ((android.content.rollback.PackageRollbackInfo) sortedHighImpactRollbacks.get(0).getPackages().get(0)).getVersionRolledBackFrom();
        android.util.Slog.i(TAG, "Rolling back high impact rollback for package: " + firstRollback.getPackageName());
        lambda$execute$4(sortedHighImpactRollbacks.get(0), firstRollback, rollbackReason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: rollbackAll, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$execute$5(int rollbackReason) {
        assertInWorkerThread();
        android.content.rollback.RollbackManager rollbackManager = (android.content.rollback.RollbackManager) this.mContext.getSystemService(android.content.rollback.RollbackManager.class);
        java.util.List<android.content.rollback.RollbackInfo> rollbacks = rollbackManager.getAvailableRollbacks();
        if (useTwoPhaseRollback(rollbacks)) {
            return;
        }
        android.util.Slog.i(TAG, "Rolling back all available rollbacks");
        for (android.content.rollback.RollbackInfo rollback : rollbacks) {
            if (rollback.isStaged()) {
                this.mPendingStagedRollbackIds.add(java.lang.Integer.valueOf(rollback.getRollbackId()));
            }
        }
        for (android.content.rollback.RollbackInfo rollback2 : rollbacks) {
            android.content.pm.VersionedPackage firstRollback = ((android.content.rollback.PackageRollbackInfo) rollback2.getPackages().get(0)).getVersionRolledBackFrom();
            lambda$execute$4(rollback2, firstRollback, rollbackReason);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: rollbackAllLowImpact, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$triggerLeastImpactLevelRollback$11(java.util.List<android.content.rollback.RollbackInfo> availableRollbacks, int rollbackReason) {
        assertInWorkerThread();
        java.util.List<android.content.rollback.RollbackInfo> lowImpactRollbacks = getRollbacksAvailableForImpactLevel(availableRollbacks, 0);
        if (useTwoPhaseRollback(lowImpactRollbacks)) {
            return;
        }
        android.util.Slog.i(TAG, "Rolling back all available low impact rollbacks");
        for (android.content.rollback.RollbackInfo rollback : lowImpactRollbacks) {
            if (rollback.isStaged()) {
                this.mPendingStagedRollbackIds.add(java.lang.Integer.valueOf(rollback.getRollbackId()));
            }
        }
        for (android.content.rollback.RollbackInfo rollback2 : lowImpactRollbacks) {
            android.content.pm.VersionedPackage firstRollback = ((android.content.rollback.PackageRollbackInfo) rollback2.getPackages().get(0)).getVersionRolledBackFrom();
            lambda$execute$4(rollback2, firstRollback, rollbackReason);
        }
    }

    private java.util.List<android.content.rollback.RollbackInfo> getRollbacksAvailableForImpactLevel(java.util.List<android.content.rollback.RollbackInfo> availableRollbacks, final int impactLevel) {
        return availableRollbacks.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.rollback.RollbackPackageHealthObserver.lambda$getRollbacksAvailableForImpactLevel$14(impactLevel, (android.content.rollback.RollbackInfo) obj);
            }
        }).toList();
    }

    static /* synthetic */ boolean lambda$getRollbacksAvailableForImpactLevel$14(int impactLevel, android.content.rollback.RollbackInfo rollbackInfo) {
        return rollbackInfo.getRollbackImpactLevel() == impactLevel;
    }

    private int getMinRollbackImpactLevel(java.util.List<android.content.rollback.RollbackInfo> availableRollbacks) {
        return availableRollbacks.stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda7
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((android.content.rollback.RollbackInfo) obj).getRollbackImpactLevel();
            }
        }).min().orElse(-1);
    }

    private int getUserImpactBasedOnRollbackImpactLevel(java.util.List<android.content.rollback.RollbackInfo> availableRollbacks) {
        int minImpact = getMinRollbackImpactLevel(availableRollbacks);
        switch (minImpact) {
            case 0:
                return 70;
            case 1:
                if (android.os.SystemProperties.getBoolean(PROP_DISABLE_HIGH_IMPACT_ROLLBACK_FLAG, false)) {
                    return 0;
                }
                return 90;
            default:
                return 0;
        }
    }

    android.os.Handler getHandler() {
        return this.mHandler;
    }
}
