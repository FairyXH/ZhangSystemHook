package com.android.server.backup.internal;

import com.android.server.backup.restore.ActiveRestoreSession.EndRestoreRunnable;

/* JADX INFO: loaded from: classes.dex */
public class BackupHandler extends android.os.Handler {
    public static final int MSG_BACKUP_OPERATION_TIMEOUT = 17;
    public static final int MSG_BACKUP_RESTORE_STEP = 20;
    public static final int MSG_FULL_CONFIRMATION_TIMEOUT = 9;
    public static final int MSG_OP_COMPLETE = 21;
    public static final int MSG_REQUEST_BACKUP = 15;
    public static final int MSG_RESTORE_OPERATION_TIMEOUT = 18;
    public static final int MSG_RESTORE_SESSION_TIMEOUT = 8;
    public static final int MSG_RETRY_CLEAR = 12;
    public static final int MSG_RUN_ADB_BACKUP = 2;
    public static final int MSG_RUN_ADB_RESTORE = 10;
    public static final int MSG_RUN_BACKUP = 1;
    public static final int MSG_RUN_CLEAR = 4;
    public static final int MSG_RUN_GET_RESTORE_SETS = 6;
    public static final int MSG_RUN_RESTORE = 3;
    public static final int MSG_SCHEDULE_BACKUP_PACKAGE = 16;
    public static final int MSG_STOP = 22;
    private final com.android.server.backup.UserBackupManagerService backupManagerService;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private final android.os.HandlerThread mBackupThread;
    volatile boolean mIsStopping;
    private final com.android.server.backup.OperationStorage mOperationStorage;

    public BackupHandler(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.OperationStorage operationStorage, android.os.HandlerThread backupThread) {
        super(backupThread.getLooper());
        this.mIsStopping = false;
        this.mBackupThread = backupThread;
        this.backupManagerService = backupManagerService;
        this.mOperationStorage = operationStorage;
        this.mAgentTimeoutParameters = (com.android.server.backup.BackupAgentTimeoutParameters) java.util.Objects.requireNonNull(backupManagerService.getAgentTimeoutParameters(), "Timeout parameters cannot be null");
    }

    public void stop() {
        this.mIsStopping = true;
        sendMessage(obtainMessage(22));
    }

    @Override // android.os.Handler
    public void dispatchMessage(android.os.Message message) throws java.lang.Exception {
        try {
            dispatchMessageInternal(message);
        } catch (java.lang.Exception e) {
            if (!this.mIsStopping) {
                throw e;
            }
        }
    }

    void dispatchMessageInternal(android.os.Message message) {
        super.dispatchMessage(message);
    }

    @Override // android.os.Handler
    public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
        com.android.server.backup.internal.OnTaskFinishedListener listener;
        java.lang.String str;
        java.lang.StringBuilder sb;
        if (msg.what == 22) {
            android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, "Stopping backup handler");
            this.backupManagerService.getWakelock().quit();
            this.mBackupThread.quitSafely();
        }
        if (this.mIsStopping) {
            return;
        }
        final com.android.server.backup.TransportManager transportManager = this.backupManagerService.getTransportManager();
        switch (msg.what) {
            case 1:
                this.backupManagerService.setLastBackupPass(java.lang.System.currentTimeMillis());
                final com.android.server.backup.transport.TransportConnection transportConnection = transportManager.getCurrentTransportClient("BH/MSG_RUN_BACKUP");
                com.android.server.backup.transport.BackupTransportClient transport = transportConnection != null ? transportConnection.connect("BH/MSG_RUN_BACKUP") : null;
                if (transport == null) {
                    if (transportConnection != null) {
                        transportManager.disposeOfTransportClient(transportConnection, "BH/MSG_RUN_BACKUP");
                    }
                    android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, "Backup requested but no transport available");
                } else {
                    java.util.List<java.lang.String> queue = new java.util.ArrayList<>();
                    com.android.server.backup.DataChangedJournal oldJournal = this.backupManagerService.getJournal();
                    synchronized (this.backupManagerService.getQueueLock()) {
                        try {
                            try {
                                if (this.backupManagerService.isBackupRunning()) {
                                    android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Backup time but one already running");
                                    return;
                                }
                                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, "Running a backup pass");
                                this.backupManagerService.setBackupRunning(true);
                                this.backupManagerService.getWakelock().acquire();
                                if (this.backupManagerService.getPendingBackups().size() > 0) {
                                    for (com.android.server.backup.keyvalue.BackupRequest b : this.backupManagerService.getPendingBackups().values()) {
                                        queue.add(b.packageName);
                                    }
                                    android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, "clearing pending backups");
                                    this.backupManagerService.getPendingBackups().clear();
                                    this.backupManagerService.setJournal(null);
                                }
                                android.app.backup.IBackupManagerMonitor monitor = null;
                                try {
                                    monitor = transport.getBackupManagerMonitor();
                                } catch (android.os.RemoteException e) {
                                    android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Failed to retrieve monitor from transport");
                                }
                                boolean staged = true;
                                if (queue.size() > 0) {
                                    try {
                                        listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.internal.BackupHandler$$ExternalSyntheticLambda0
                                            @Override // com.android.server.backup.internal.OnTaskFinishedListener
                                            public final void onFinished(java.lang.String str2) {
                                                transportManager.disposeOfTransportClient(transportConnection, str2);
                                            }
                                        };
                                    } catch (java.lang.Exception e2) {
                                        e = e2;
                                    }
                                    try {
                                        com.android.server.backup.keyvalue.KeyValueBackupTask.start(this.backupManagerService, this.mOperationStorage, transportConnection, transport.transportDirName(), queue, oldJournal, null, monitor, listener, java.util.Collections.emptyList(), false, false, this.backupManagerService.getEligibilityRulesForOperation(0));
                                    } catch (java.lang.Exception e3) {
                                        e = e3;
                                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Transport became unavailable attempting backup or error initializing backup task", e);
                                        staged = false;
                                    }
                                } else {
                                    android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, "Backup requested but nothing pending");
                                    staged = false;
                                }
                                if (!staged) {
                                    transportManager.disposeOfTransportClient(transportConnection, "BH/MSG_RUN_BACKUP");
                                    synchronized (this.backupManagerService.getQueueLock()) {
                                        this.backupManagerService.setBackupRunning(false);
                                        break;
                                    }
                                    this.backupManagerService.getWakelock().release();
                                }
                            } catch (java.lang.Throwable th) {
                                th = th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                        while (true) {
                            try {
                                throw th;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                            }
                        }
                    }
                }
                break;
            case 2:
                com.android.server.backup.params.AdbBackupParams params = (com.android.server.backup.params.AdbBackupParams) msg.obj;
                com.android.server.backup.fullbackup.PerformAdbBackupTask task = new com.android.server.backup.fullbackup.PerformAdbBackupTask(this.backupManagerService, this.mOperationStorage, params.fd, params.observer, params.includeApks, params.includeObbs, params.includeShared, params.doWidgets, params.curPassword, params.encryptPassword, params.allApps, params.includeSystem, params.doCompress, params.includeKeyValue, params.packages, params.latch, params.backupEligibilityRules);
                new java.lang.Thread(task, "adb-backup").start();
                break;
            case 3:
                com.android.server.backup.params.RestoreParams params2 = (com.android.server.backup.params.RestoreParams) msg.obj;
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "MSG_RUN_RESTORE observer=" + params2.observer);
                com.android.server.backup.restore.PerformUnifiedRestoreTask task2 = new com.android.server.backup.restore.PerformUnifiedRestoreTask(this.backupManagerService, this.mOperationStorage, params2.mTransportConnection, params2.observer, params2.monitor, params2.token, params2.packageInfo, params2.pmToken, params2.isSystemRestore, params2.filterSet, params2.listener, params2.backupEligibilityRules);
                synchronized (this.backupManagerService.getPendingRestores()) {
                    if (this.backupManagerService.isRestoreInProgress()) {
                        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Restore in progress, queueing.");
                        this.backupManagerService.getPendingRestores().add(task2);
                    } else {
                        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Starting restore.");
                        this.backupManagerService.setRestoreInProgress(true);
                        android.os.Message restoreMsg = obtainMessage(20, task2);
                        sendMessage(restoreMsg);
                    }
                    break;
                }
                break;
            case 4:
                com.android.server.backup.params.ClearParams params3 = (com.android.server.backup.params.ClearParams) msg.obj;
                java.lang.Runnable task3 = new com.android.server.backup.internal.PerformClearTask(this.backupManagerService, params3.mTransportConnection, params3.packageInfo, params3.listener);
                task3.run();
                break;
            case 6:
                java.util.List<android.app.backup.RestoreSet> sets = null;
                com.android.server.backup.params.RestoreGetSetsParams params4 = (com.android.server.backup.params.RestoreGetSetsParams) msg.obj;
                try {
                    try {
                        java.util.List<android.app.backup.RestoreSet> sets2 = params4.mTransportConnection.connectOrThrow("BH/MSG_RUN_GET_RESTORE_SETS").getAvailableRestoreSets();
                        synchronized (params4.session) {
                            params4.session.setRestoreSets(sets2);
                            break;
                        }
                        if (sets2 == null) {
                            android.util.EventLog.writeEvent(com.android.server.EventLogTags.RESTORE_TRANSPORT_FAILURE, new java.lang.Object[0]);
                        }
                        if (params4.observer != null) {
                            try {
                                if (sets2 == null) {
                                    params4.observer.restoreSetsAvailable((android.app.backup.RestoreSet[]) null);
                                } else {
                                    params4.observer.restoreSetsAvailable((android.app.backup.RestoreSet[]) sets2.toArray(new android.app.backup.RestoreSet[0]));
                                }
                            } catch (android.os.RemoteException e4) {
                                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to report listing to observer");
                            } catch (java.lang.Exception e5) {
                                e = e5;
                                str = com.android.server.backup.BackupManagerService.TAG;
                                sb = new java.lang.StringBuilder();
                                android.util.Slog.e(str, sb.append("Restore observer threw: ").append(e.getMessage()).toString());
                            }
                        }
                    } catch (java.lang.Exception e6) {
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Error from transport getting set list: " + e6.getMessage());
                        if (params4.observer != null) {
                            try {
                                if (0 == 0) {
                                    params4.observer.restoreSetsAvailable((android.app.backup.RestoreSet[]) null);
                                } else {
                                    params4.observer.restoreSetsAvailable((android.app.backup.RestoreSet[]) sets.toArray(new android.app.backup.RestoreSet[0]));
                                }
                            } catch (android.os.RemoteException e7) {
                                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to report listing to observer");
                            } catch (java.lang.Exception e8) {
                                e = e8;
                                str = com.android.server.backup.BackupManagerService.TAG;
                                sb = new java.lang.StringBuilder();
                                android.util.Slog.e(str, sb.append("Restore observer threw: ").append(e.getMessage()).toString());
                            }
                        }
                        removeMessages(8);
                        sendEmptyMessageDelayed(8, this.mAgentTimeoutParameters.getRestoreSessionTimeoutMillis());
                        params4.listener.onFinished("BH/MSG_RUN_GET_RESTORE_SETS");
                        break;
                    }
                    removeMessages(8);
                    sendEmptyMessageDelayed(8, this.mAgentTimeoutParameters.getRestoreSessionTimeoutMillis());
                    params4.listener.onFinished("BH/MSG_RUN_GET_RESTORE_SETS");
                } catch (java.lang.Throwable th4) {
                    if (params4.observer != null) {
                        try {
                            if (0 == 0) {
                                params4.observer.restoreSetsAvailable((android.app.backup.RestoreSet[]) null);
                            } else {
                                params4.observer.restoreSetsAvailable((android.app.backup.RestoreSet[]) sets.toArray(new android.app.backup.RestoreSet[0]));
                            }
                        } catch (android.os.RemoteException e9) {
                            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to report listing to observer");
                        } catch (java.lang.Exception e10) {
                            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Restore observer threw: " + e10.getMessage());
                        }
                        break;
                    }
                    removeMessages(8);
                    sendEmptyMessageDelayed(8, this.mAgentTimeoutParameters.getRestoreSessionTimeoutMillis());
                    params4.listener.onFinished("BH/MSG_RUN_GET_RESTORE_SETS");
                    throw th4;
                }
                break;
            case 8:
                synchronized (this.backupManagerService) {
                    if (this.backupManagerService.getActiveRestoreSession() != null) {
                        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Restore session timed out; aborting");
                        this.backupManagerService.getActiveRestoreSession().markTimedOut();
                        com.android.server.backup.restore.ActiveRestoreSession activeRestoreSession = this.backupManagerService.getActiveRestoreSession();
                        java.util.Objects.requireNonNull(activeRestoreSession);
                        post(activeRestoreSession.new EndRestoreRunnable(this.backupManagerService, this.backupManagerService.getActiveRestoreSession()));
                    }
                    break;
                }
                break;
            case 9:
                synchronized (this.backupManagerService.getAdbBackupRestoreConfirmations()) {
                    com.android.server.backup.params.AdbParams params5 = this.backupManagerService.getAdbBackupRestoreConfirmations().get(msg.arg1);
                    if (params5 != null) {
                        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Full backup/restore timed out waiting for user confirmation");
                        this.backupManagerService.signalAdbBackupRestoreCompletion(params5);
                        this.backupManagerService.getAdbBackupRestoreConfirmations().delete(msg.arg1);
                        if (params5.observer != null) {
                            try {
                                params5.observer.onTimeout();
                                break;
                            } catch (android.os.RemoteException e11) {
                            }
                        }
                    } else {
                        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "couldn't find params for token " + msg.arg1);
                    }
                    break;
                }
                break;
            case 10:
                com.android.server.backup.params.AdbRestoreParams params6 = (com.android.server.backup.params.AdbRestoreParams) msg.obj;
                com.android.server.backup.restore.PerformAdbRestoreTask task4 = new com.android.server.backup.restore.PerformAdbRestoreTask(this.backupManagerService, this.mOperationStorage, params6.fd, params6.curPassword, params6.encryptPassword, params6.observer, params6.latch);
                new java.lang.Thread(task4, "adb-restore").start();
                break;
            case 12:
                com.android.server.backup.params.ClearRetryParams params7 = (com.android.server.backup.params.ClearRetryParams) msg.obj;
                this.backupManagerService.clearBackupData(params7.transportName, params7.packageName);
                break;
            case 15:
                com.android.server.backup.params.BackupParams params8 = (com.android.server.backup.params.BackupParams) msg.obj;
                this.backupManagerService.setBackupRunning(true);
                this.backupManagerService.getWakelock().acquire();
                com.android.server.backup.keyvalue.KeyValueBackupTask.start(this.backupManagerService, this.mOperationStorage, params8.mTransportConnection, params8.dirName, params8.kvPackages, null, params8.observer, params8.monitor, params8.listener, params8.fullPackages, true, params8.nonIncrementalBackup, params8.mBackupEligibilityRules);
                break;
            case 16:
                java.lang.String pkgName = (java.lang.String) msg.obj;
                this.backupManagerService.dataChangedImpl(pkgName);
                break;
            case 17:
            case 18:
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "Timeout message received for token=" + java.lang.Integer.toHexString(msg.arg1));
                this.backupManagerService.getWrapper().getExtImpl().registerTokenForTimeout(msg.arg1);
                this.backupManagerService.handleCancel(msg.arg1, false);
                break;
            case 20:
                try {
                    com.android.server.backup.BackupRestoreTask task5 = (com.android.server.backup.BackupRestoreTask) msg.obj;
                    task5.execute();
                } catch (java.lang.ClassCastException e12) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Invalid backup/restore task in flight, obj=" + msg.obj);
                }
                break;
            case 21:
                try {
                    android.util.Pair<com.android.server.backup.BackupRestoreTask, java.lang.Long> taskWithResult = (android.util.Pair) msg.obj;
                    ((com.android.server.backup.BackupRestoreTask) taskWithResult.first).operationComplete(((java.lang.Long) taskWithResult.second).longValue());
                } catch (java.lang.ClassCastException e13) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Invalid completion in flight, obj=" + msg.obj);
                }
                break;
        }
        this.backupManagerService.getWrapper().getExtImpl().handleOplusMessage(msg);
    }
}
