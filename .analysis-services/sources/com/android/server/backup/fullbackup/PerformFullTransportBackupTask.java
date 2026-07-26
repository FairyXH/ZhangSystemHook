package com.android.server.backup.fullbackup;

/* JADX INFO: loaded from: classes.dex */
public class PerformFullTransportBackupTask extends com.android.server.backup.fullbackup.FullBackupTask implements com.android.server.backup.BackupRestoreTask {
    private static final java.lang.String TAG = "PFTBT";
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private final com.android.server.backup.utils.BackupEligibilityRules mBackupEligibilityRules;
    private com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender;
    android.app.backup.IBackupObserver mBackupObserver;
    com.android.server.backup.fullbackup.PerformFullTransportBackupTask.SinglePackageBackupRunner mBackupRunner;
    private final int mBackupRunnerOpToken;
    private volatile boolean mCancelAll;
    private final java.lang.Object mCancelLock;
    private final int mCurrentOpToken;
    android.content.pm.PackageInfo mCurrentPackage;
    private volatile boolean mIsDoingBackup;
    com.android.server.backup.FullBackupJob mJob;
    java.util.concurrent.CountDownLatch mLatch;
    private final com.android.server.backup.internal.OnTaskFinishedListener mListener;
    com.android.server.backup.OperationStorage mOperationStorage;
    java.util.List<android.content.pm.PackageInfo> mPackages;
    private final com.android.server.backup.transport.TransportConnection mTransportConnection;
    boolean mUpdateSchedule;
    private com.android.server.backup.UserBackupManagerService mUserBackupManagerService;
    private final int mUserId;
    boolean mUserInitiated;

    public static com.android.server.backup.fullbackup.PerformFullTransportBackupTask newWithCurrentTransport(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, android.app.backup.IFullBackupRestoreObserver observer, java.lang.String[] whichPackages, boolean updateSchedule, com.android.server.backup.FullBackupJob runningJob, java.util.concurrent.CountDownLatch latch, android.app.backup.IBackupObserver backupObserver, android.app.backup.IBackupManagerMonitor monitor, boolean userInitiated, java.lang.String caller, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        final com.android.server.backup.TransportManager transportManager = backupManagerService.getTransportManager();
        final com.android.server.backup.transport.TransportConnection transportConnection = transportManager.getCurrentTransportClient(caller);
        if (transportConnection == null) {
            throw new java.lang.IllegalStateException("No TransportConnection available");
        }
        com.android.server.backup.internal.OnTaskFinishedListener listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.fullbackup.PerformFullTransportBackupTask$$ExternalSyntheticLambda0
            @Override // com.android.server.backup.internal.OnTaskFinishedListener
            public final void onFinished(java.lang.String str) {
                transportManager.disposeOfTransportClient(transportConnection, str);
            }
        };
        return new com.android.server.backup.fullbackup.PerformFullTransportBackupTask(backupManagerService, operationStorage, transportConnection, observer, whichPackages, updateSchedule, runningJob, latch, backupObserver, monitor, listener, userInitiated, backupEligibilityRules);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PerformFullTransportBackupTask(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, com.android.server.backup.transport.TransportConnection transportConnection, android.app.backup.IFullBackupRestoreObserver observer, java.lang.String[] whichPackages, boolean updateSchedule, com.android.server.backup.FullBackupJob runningJob, java.util.concurrent.CountDownLatch latch, android.app.backup.IBackupObserver backupObserver, android.app.backup.IBackupManagerMonitor monitor, com.android.server.backup.internal.OnTaskFinishedListener listener, boolean userInitiated, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        super(observer);
        java.lang.String[] strArr = whichPackages;
        this.mCancelLock = new java.lang.Object();
        this.mUserBackupManagerService = backupManagerService;
        this.mOperationStorage = operationStorage;
        this.mTransportConnection = transportConnection;
        this.mUpdateSchedule = updateSchedule;
        this.mLatch = latch;
        this.mJob = runningJob;
        this.mPackages = new java.util.ArrayList(strArr.length);
        this.mBackupObserver = backupObserver;
        this.mListener = listener != null ? listener : com.android.server.backup.internal.OnTaskFinishedListener.NOP;
        this.mUserInitiated = userInitiated;
        this.mCurrentOpToken = backupManagerService.generateRandomIntegerToken();
        this.mBackupRunnerOpToken = backupManagerService.generateRandomIntegerToken();
        this.mBackupManagerMonitorEventSender = new com.android.server.backup.utils.BackupManagerMonitorEventSender(monitor);
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
        this.mUserId = backupManagerService.getUserId();
        this.mBackupEligibilityRules = backupEligibilityRules;
        if (backupManagerService.isBackupOperationInProgress()) {
            android.util.Slog.d(TAG, "Skipping full backup. A backup is already in progress.");
            this.mCancelAll = true;
            return;
        }
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            int i2 = length;
            java.lang.String pkg = strArr[i];
            try {
                android.content.pm.PackageManager pm = backupManagerService.getPackageManager();
                android.content.pm.PackageInfo info = pm.getPackageInfoAsUser(pkg, 134217728, this.mUserId);
                this.mCurrentPackage = info;
                if (!this.mBackupEligibilityRules.appIsEligibleForBackup(info.applicationInfo)) {
                    this.mBackupManagerMonitorEventSender.monitorEvent(9, this.mCurrentPackage, 3, null);
                    com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, pkg, -2001);
                } else if (!this.mBackupEligibilityRules.appGetsFullBackup(info)) {
                    this.mBackupManagerMonitorEventSender.monitorEvent(10, this.mCurrentPackage, 3, null);
                    com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, pkg, -2001);
                } else if (this.mBackupEligibilityRules.appIsStopped(info.applicationInfo)) {
                    this.mBackupManagerMonitorEventSender.monitorEvent(11, this.mCurrentPackage, 3, null);
                    com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(this.mBackupObserver, pkg, -2001);
                } else {
                    this.mPackages.add(info);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.i(TAG, "Requested package " + pkg + " not found; ignoring");
                this.mBackupManagerMonitorEventSender.monitorEvent(12, this.mCurrentPackage, 3, null);
            }
            i++;
            strArr = whichPackages;
            length = i2;
        }
        this.mPackages = backupManagerService.filterUserFacingPackages(this.mPackages);
        java.util.Set<java.lang.String> packageNames = com.google.android.collect.Sets.newHashSet();
        for (android.content.pm.PackageInfo pkgInfo : this.mPackages) {
            packageNames.add(pkgInfo.packageName);
        }
        android.util.Slog.d(TAG, "backupmanager pftbt token=" + java.lang.Integer.toHexString(this.mCurrentOpToken));
        this.mOperationStorage.registerOperationForPackages(this.mCurrentOpToken, 0, packageNames, this, 2);
    }

    public void unregisterTask() {
        this.mOperationStorage.removeOperation(this.mCurrentOpToken);
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void execute() {
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0019 A[Catch: all -> 0x004e, TryCatch #1 {, blocks: (B:5:0x0005, B:6:0x000c, B:8:0x0010, B:9:0x0017, B:11:0x0019, B:13:0x0020, B:14:0x0027, B:17:0x0034, B:18:0x004c), top: B:25:0x0005, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0010 A[Catch: all -> 0x004e, TryCatch #1 {, blocks: (B:5:0x0005, B:6:0x000c, B:8:0x0010, B:9:0x0017, B:11:0x0019, B:13:0x0020, B:14:0x0027, B:17:0x0034, B:18:0x004c), top: B:25:0x0005, inners: #0 }] */
    @Override // com.android.server.backup.BackupRestoreTask
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void handleCancel(boolean r6) {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mCancelLock
            monitor-enter(r0)
            if (r6 != 0) goto Lc
            java.lang.String r1 = "PFTBT"
            java.lang.String r2 = "Expected cancelAll to be true."
            android.util.Slog.wtf(r1, r2)     // Catch: java.lang.Throwable -> L4e
        Lc:
            boolean r1 = r5.mCancelAll     // Catch: java.lang.Throwable -> L4e
            if (r1 == 0) goto L19
            java.lang.String r1 = "PFTBT"
            java.lang.String r2 = "Ignoring duplicate cancel call."
            android.util.Slog.d(r1, r2)     // Catch: java.lang.Throwable -> L4e
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L4e
            return
        L19:
            r1 = 1
            r5.mCancelAll = r1     // Catch: java.lang.Throwable -> L4e
            boolean r1 = r5.mIsDoingBackup     // Catch: java.lang.Throwable -> L4e
            if (r1 == 0) goto L4c
            com.android.server.backup.UserBackupManagerService r1 = r5.mUserBackupManagerService     // Catch: java.lang.Throwable -> L4e
            int r2 = r5.mBackupRunnerOpToken     // Catch: java.lang.Throwable -> L4e
            r1.handleCancel(r2, r6)     // Catch: java.lang.Throwable -> L4e
            com.android.server.backup.transport.TransportConnection r1 = r5.mTransportConnection     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L4e
            java.lang.String r2 = "PFTBT.handleCancel()"
            com.android.server.backup.transport.BackupTransportClient r1 = r1.getConnectedTransport(r2)     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L4e
            r1.cancelFullBackup()     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L4e
            goto L4c
        L33:
            r1 = move-exception
            java.lang.String r2 = "PFTBT"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4e
            r3.<init>()     // Catch: java.lang.Throwable -> L4e
            java.lang.String r4 = "Error calling cancelFullBackup() on transport: "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L4e
            java.lang.StringBuilder r3 = r3.append(r1)     // Catch: java.lang.Throwable -> L4e
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L4e
            android.util.Slog.w(r2, r3)     // Catch: java.lang.Throwable -> L4e
        L4c:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L4e
            return
        L4e:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L4e
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.fullbackup.PerformFullTransportBackupTask.handleCancel(boolean):void");
    }

    @Override // com.android.server.backup.BackupRestoreTask
    public void operationComplete(long result) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:189:0x04f6, code lost:
    
        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(r33.mBackupObserver, r11, -1000);
        android.util.Slog.w(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Transport failed; aborting backup: " + r1);
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.FULL_BACKUP_TRANSPORT_FAILURE, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x051f, code lost:
    
        r33.mUserBackupManagerService.tearDownAgentAndKill(r5.applicationInfo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0528, code lost:
    
        if (r33.mCancelAll == false) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x052a, code lost:
    
        r8 = -2003;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x052e, code lost:
    
        r8 = -1000;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x052f, code lost:
    
        android.util.Slog.i(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Full backup completed with status: " + r8);
        com.android.server.backup.utils.BackupObserverUtils.sendBackupFinished(r33.mBackupObserver, r8);
        cleanUpPipes(r12);
        cleanUpPipes(r32);
        unregisterTask();
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0559, code lost:
    
        if (r33.mJob == null) goto L199;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x055b, code lost:
    
        r33.mJob.finishBackupPass(r33.mUserId);
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0562, code lost:
    
        r13 = r33.mUserBackupManagerService.getQueueLock();
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0568, code lost:
    
        monitor-enter(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x056e, code lost:
    
        r33.mUserBackupManagerService.setRunningFullBackupTask(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0571, code lost:
    
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x0572, code lost:
    
        r33.mListener.onFinished("PFTBT.run()");
        r33.mLatch.countDown();
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0580, code lost:
    
        if (r33.mUpdateSchedule == false) goto L208;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0582, code lost:
    
        r33.mUserBackupManagerService.scheduleNextFullBackupJob(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x0587, code lost:
    
        android.util.Slog.i(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Full data backup pass finished.");
        r33.mUserBackupManagerService.getWakelock().release();
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x0597, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0598, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x0599, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x059b, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x059c, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x059f, code lost:
    
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x05a0, code lost:
    
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x05a1, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x05a2, code lost:
    
        r5 = r0;
        r11 = -1000;
        r1 = r32;
        r2 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x05aa, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x05ab, code lost:
    
        r5 = r0;
        r1 = r32;
        r2 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x068c, code lost:
    
        if (r33.mCancelAll == false) goto L262;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x068e, code lost:
    
        r11 = -2003;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0691, code lost:
    
        r11 = r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0693, code lost:
    
        android.util.Slog.i(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Full backup completed with status: " + r11);
        com.android.server.backup.utils.BackupObserverUtils.sendBackupFinished(r33.mBackupObserver, r11);
        cleanUpPipes(r12);
        cleanUpPipes(r1);
        unregisterTask();
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x06bb, code lost:
    
        if (r33.mJob == null) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x06bd, code lost:
    
        r33.mJob.finishBackupPass(r33.mUserId);
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x06c4, code lost:
    
        r5 = r33.mUserBackupManagerService.getQueueLock();
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x06ca, code lost:
    
        monitor-enter(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x06cb, code lost:
    
        r33.mUserBackupManagerService.setRunningFullBackupTask(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x06d1, code lost:
    
        monitor-exit(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x06d2, code lost:
    
        r33.mListener.onFinished("PFTBT.run()");
        r33.mLatch.countDown();
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x06e0, code lost:
    
        if (r33.mUpdateSchedule == false) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x06e2, code lost:
    
        r6 = r16;
        r33.mUserBackupManagerService.scheduleNextFullBackupJob(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x06ea, code lost:
    
        r6 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x06ec, code lost:
    
        android.util.Slog.i(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Full data backup pass finished.");
        r33.mUserBackupManagerService.getWakelock().release();
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x0700, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0705, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0706, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0876, code lost:
    
        r11 = -2003;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x08a2, code lost:
    
        r33.mJob.finishBackupPass(r33.mUserId);
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x08b0, code lost:
    
        r33.mUserBackupManagerService.setRunningFullBackupTask(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x08b7, code lost:
    
        r33.mListener.onFinished("PFTBT.run()");
        r33.mLatch.countDown();
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x08c5, code lost:
    
        if (r33.mUpdateSchedule != false) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x08c7, code lost:
    
        r33.mUserBackupManagerService.scheduleNextFullBackupJob(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x08cc, code lost:
    
        android.util.Slog.i(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Full data backup pass finished.");
        r33.mUserBackupManagerService.getWakelock().release();
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x08dc, code lost:
    
        throw r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:?, code lost:
    
        return;
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0876  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x08a2  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x08b0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2273
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.fullbackup.PerformFullTransportBackupTask.run():void");
    }

    void cleanUpPipes(android.os.ParcelFileDescriptor[] pipes) {
        if (pipes != null) {
            if (pipes[0] != null) {
                android.os.ParcelFileDescriptor fd = pipes[0];
                pipes[0] = null;
                try {
                    fd.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, "Unable to close pipe!");
                }
            }
            if (pipes[1] != null) {
                android.os.ParcelFileDescriptor fd2 = pipes[1];
                pipes[1] = null;
                try {
                    fd2.close();
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(TAG, "Unable to close pipe!");
                }
            }
        }
    }

    class SinglePackageBackupPreflight implements com.android.server.backup.BackupRestoreTask, com.android.server.backup.fullbackup.FullBackupPreflight {
        private final int mCurrentOpToken;
        final long mQuota;
        final com.android.server.backup.transport.TransportConnection mTransportConnection;
        private final int mTransportFlags;
        final java.util.concurrent.atomic.AtomicLong mResult = new java.util.concurrent.atomic.AtomicLong(-1003);
        final java.util.concurrent.CountDownLatch mLatch = new java.util.concurrent.CountDownLatch(1);

        SinglePackageBackupPreflight(com.android.server.backup.transport.TransportConnection transportConnection, long quota, int currentOpToken, int transportFlags) {
            this.mTransportConnection = transportConnection;
            this.mQuota = quota;
            this.mCurrentOpToken = currentOpToken;
            this.mTransportFlags = transportFlags;
        }

        @Override // com.android.server.backup.fullbackup.FullBackupPreflight
        public int preflightFullBackup(android.content.pm.PackageInfo pkg, final android.app.IBackupAgent agent) {
            long fullBackupAgentTimeoutMillis = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
            try {
                com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mUserBackupManagerService.prepareOperationTimeout(this.mCurrentOpToken, fullBackupAgentTimeoutMillis, this, 0);
                agent.doMeasureFullBackup(this.mQuota, this.mCurrentOpToken, com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mUserBackupManagerService.getBackupManagerBinder(), this.mTransportFlags);
                this.mLatch.await(fullBackupAgentTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
                final long totalSize = this.mResult.get();
                if (totalSize < 0) {
                    return (int) totalSize;
                }
                com.android.server.backup.transport.BackupTransportClient transport = this.mTransportConnection.connectOrThrow("PFTBT$SPBP.preflightFullBackup()");
                int result = transport.checkFullBackupSize(totalSize);
                if (result == -1005) {
                    try {
                        com.android.server.backup.remote.RemoteCall.execute(new com.android.server.backup.remote.RemoteCallable() { // from class: com.android.server.backup.fullbackup.PerformFullTransportBackupTask$SinglePackageBackupPreflight$$ExternalSyntheticLambda0
                            @Override // com.android.server.backup.remote.RemoteCallable
                            public final void call(java.lang.Object obj) throws android.os.RemoteException {
                                this.f$0.lambda$preflightFullBackup$0(agent, totalSize, (android.app.backup.IBackupCallback) obj);
                            }
                        }, com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getQuotaExceededTimeoutMillis());
                    } catch (java.lang.Exception e) {
                        e = e;
                        android.util.Slog.w(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Exception preflighting " + pkg.packageName + ": " + e.getMessage());
                        return -1003;
                    }
                }
                return result;
            } catch (java.lang.Exception e2) {
                e = e2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$preflightFullBackup$0(android.app.IBackupAgent agent, long totalSize, android.app.backup.IBackupCallback callback) throws android.os.RemoteException {
            agent.doQuotaExceeded(totalSize, this.mQuota, callback);
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void execute() {
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void operationComplete(long result) {
            this.mResult.set(result);
            this.mLatch.countDown();
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void handleCancel(boolean cancelAll) {
            this.mResult.set(-1003L);
            this.mLatch.countDown();
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }

        @Override // com.android.server.backup.fullbackup.FullBackupPreflight
        public long getExpectedSizeOrErrorCode() {
            long fullBackupAgentTimeoutMillis = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
            try {
                this.mLatch.await(fullBackupAgentTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
                return this.mResult.get();
            } catch (java.lang.InterruptedException e) {
                return -1L;
            }
        }
    }

    class SinglePackageBackupRunner implements java.lang.Runnable, com.android.server.backup.BackupRestoreTask {
        private final int mCurrentOpToken;
        private com.android.server.backup.fullbackup.FullBackupEngine mEngine;
        private final int mEphemeralToken;
        private volatile boolean mIsCancelled;
        final android.os.ParcelFileDescriptor mOutput;
        final com.android.server.backup.fullbackup.PerformFullTransportBackupTask.SinglePackageBackupPreflight mPreflight;
        private final long mQuota;
        final android.content.pm.PackageInfo mTarget;
        private final int mTransportFlags;
        final java.util.concurrent.CountDownLatch mPreflightLatch = new java.util.concurrent.CountDownLatch(1);
        final java.util.concurrent.CountDownLatch mBackupLatch = new java.util.concurrent.CountDownLatch(1);
        private volatile int mPreflightResult = -1003;
        private volatile int mBackupResult = -1003;

        SinglePackageBackupRunner(android.os.ParcelFileDescriptor output, android.content.pm.PackageInfo target, com.android.server.backup.transport.TransportConnection transportConnection, long quota, int currentOpToken, int transportFlags) throws java.io.IOException {
            this.mOutput = android.os.ParcelFileDescriptor.dup(output.getFileDescriptor());
            this.mTarget = target;
            this.mCurrentOpToken = currentOpToken;
            this.mEphemeralToken = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mUserBackupManagerService.generateRandomIntegerToken();
            this.mPreflight = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.new SinglePackageBackupPreflight(transportConnection, quota, this.mEphemeralToken, transportFlags);
            this.mQuota = quota;
            this.mTransportFlags = transportFlags;
            registerTask(target.packageName);
        }

        void registerTask(java.lang.String packageName) {
            java.util.Set<java.lang.String> packages = com.google.android.collect.Sets.newHashSet(new java.lang.String[]{packageName});
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mOperationStorage.registerOperationForPackages(this.mCurrentOpToken, 0, packages, this, 0);
        }

        void unregisterTask() {
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }

        @Override // java.lang.Runnable
        public void run() throws java.lang.Throwable {
            java.lang.String str;
            java.lang.Throwable th;
            java.lang.Throwable th2;
            java.io.FileOutputStream out = new java.io.FileOutputStream(this.mOutput.getFileDescriptor());
            this.mEngine = new com.android.server.backup.fullbackup.FullBackupEngine(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mUserBackupManagerService, out, this.mPreflight, this.mTarget, false, this, this.mQuota, this.mCurrentOpToken, this.mTransportFlags, com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mBackupEligibilityRules, com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mBackupManagerMonitorEventSender);
            try {
                if (!this.mIsCancelled) {
                    this.mPreflightResult = this.mEngine.preflightCheck();
                }
                try {
                    try {
                        this.mPreflightLatch.countDown();
                        if (this.mPreflightResult == 0) {
                            try {
                                if (!this.mIsCancelled) {
                                    this.mBackupResult = this.mEngine.backupOnePackage();
                                }
                            } catch (java.lang.Throwable th3) {
                                th2 = th3;
                                str = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG;
                                unregisterTask();
                                this.mBackupLatch.countDown();
                                try {
                                    this.mOutput.close();
                                    throw th2;
                                } catch (java.io.IOException e) {
                                    android.util.Slog.w(str, "Error closing transport pipe in runner");
                                    throw th2;
                                }
                            }
                        }
                        unregisterTask();
                        this.mBackupLatch.countDown();
                        try {
                            this.mOutput.close();
                        } catch (java.io.IOException e2) {
                            str = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG;
                            android.util.Slog.w(str, "Error closing transport pipe in runner");
                        }
                    } catch (java.lang.Exception e3) {
                        e = e3;
                        str = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG;
                        android.util.Slog.w(str, "Exception during full package backup of " + this.mTarget.packageName, e);
                        unregisterTask();
                        this.mBackupLatch.countDown();
                        try {
                            this.mOutput.close();
                        } catch (java.io.IOException e4) {
                            android.util.Slog.w(str, "Error closing transport pipe in runner");
                        }
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    str = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG;
                    th2 = th;
                    unregisterTask();
                    this.mBackupLatch.countDown();
                    this.mOutput.close();
                    throw th2;
                }
            } catch (java.lang.Throwable th5) {
                str = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG;
                try {
                    try {
                        this.mPreflightLatch.countDown();
                        throw th5;
                    } catch (java.lang.Exception e5) {
                        e = e5;
                        android.util.Slog.w(str, "Exception during full package backup of " + this.mTarget.packageName, e);
                        unregisterTask();
                        this.mBackupLatch.countDown();
                        this.mOutput.close();
                    }
                } catch (java.lang.Throwable th6) {
                    th = th6;
                    th2 = th;
                    unregisterTask();
                    this.mBackupLatch.countDown();
                    this.mOutput.close();
                    throw th2;
                }
            }
        }

        public void sendQuotaExceeded(long backupDataBytes, long quotaBytes) {
            this.mEngine.sendQuotaExceeded(backupDataBytes, quotaBytes);
        }

        long getPreflightResultBlocking() {
            long fullBackupAgentTimeoutMillis = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
            try {
                this.mPreflightLatch.await(fullBackupAgentTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (this.mIsCancelled) {
                    return -2003L;
                }
                if (this.mPreflightResult == 0) {
                    return this.mPreflight.getExpectedSizeOrErrorCode();
                }
                return this.mPreflightResult;
            } catch (java.lang.InterruptedException e) {
                return -1003L;
            }
        }

        int getBackupResultBlocking() {
            long fullBackupAgentTimeoutMillis = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis();
            try {
                this.mBackupLatch.await(fullBackupAgentTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (this.mIsCancelled) {
                    return -2003;
                }
                return this.mBackupResult;
            } catch (java.lang.InterruptedException e) {
                return -1003;
            }
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void execute() {
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void operationComplete(long result) {
        }

        @Override // com.android.server.backup.BackupRestoreTask
        public void handleCancel(boolean cancelAll) {
            android.util.Slog.w(com.android.server.backup.fullbackup.PerformFullTransportBackupTask.TAG, "Full backup cancel of " + this.mTarget.packageName);
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mBackupManagerMonitorEventSender.monitorEvent(4, com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mCurrentPackage, 2, null);
            this.mIsCancelled = true;
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mUserBackupManagerService.handleCancel(this.mEphemeralToken, cancelAll);
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mUserBackupManagerService.tearDownAgentAndKill(this.mTarget.applicationInfo);
            this.mPreflightLatch.countDown();
            this.mBackupLatch.countDown();
            com.android.server.backup.fullbackup.PerformFullTransportBackupTask.this.mOperationStorage.removeOperation(this.mCurrentOpToken);
        }
    }
}
