package com.android.server.backup.internal;

/* JADX INFO: loaded from: classes.dex */
public class PerformInitializeTask implements java.lang.Runnable {
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final java.io.File mBaseStateDir;
    private final com.android.server.backup.internal.OnTaskFinishedListener mListener;
    private android.app.backup.IBackupObserver mObserver;
    private final java.lang.String[] mQueue;
    private final com.android.server.backup.TransportManager mTransportManager;

    public PerformInitializeTask(com.android.server.backup.UserBackupManagerService backupManagerService, java.lang.String[] transportNames, android.app.backup.IBackupObserver observer, com.android.server.backup.internal.OnTaskFinishedListener listener) {
        this(backupManagerService, backupManagerService.getTransportManager(), transportNames, observer, listener, backupManagerService.getBaseStateDir());
    }

    PerformInitializeTask(com.android.server.backup.UserBackupManagerService backupManagerService, com.android.server.backup.TransportManager transportManager, java.lang.String[] transportNames, android.app.backup.IBackupObserver observer, com.android.server.backup.internal.OnTaskFinishedListener listener, java.io.File baseStateDir) {
        this.mBackupManagerService = backupManagerService;
        this.mTransportManager = transportManager;
        this.mQueue = transportNames;
        this.mObserver = observer;
        this.mListener = listener;
        this.mBaseStateDir = baseStateDir;
    }

    private void notifyResult(java.lang.String target, int status) {
        try {
            if (this.mObserver != null) {
                this.mObserver.onResult(target, status);
            }
        } catch (android.os.RemoteException e) {
            this.mObserver = null;
        }
    }

    private void notifyFinished(int status) {
        try {
            if (this.mObserver != null) {
                this.mObserver.backupFinished(status);
            }
        } catch (android.os.RemoteException e) {
            this.mObserver = null;
        }
    }

    @Override // java.lang.Runnable
    public void run() throws java.lang.Throwable {
        java.lang.String[] strArr;
        int i;
        java.util.List<com.android.server.backup.transport.TransportConnection> transportClientsToDisposeOf = new java.util.ArrayList<>(this.mQueue.length);
        int result = 0;
        try {
            try {
                java.lang.String[] strArr2 = this.mQueue;
                int i2 = 0;
                for (int length = strArr2.length; i2 < length; length = i) {
                    java.lang.String transportName = strArr2[i2];
                    com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getTransportClient(transportName, "PerformInitializeTask.run()");
                    if (transportConnection == null) {
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Requested init for " + transportName + " but not found");
                        strArr = strArr2;
                        i = length;
                    } else {
                        transportClientsToDisposeOf.add(transportConnection);
                        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Initializing (wiping) backup transport storage: " + transportName);
                        java.lang.String transportDirName = this.mTransportManager.getTransportDirName(transportConnection.getTransportComponent());
                        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_START, transportDirName);
                        long startRealtime = android.os.SystemClock.elapsedRealtime();
                        com.android.server.backup.transport.BackupTransportClient transport = transportConnection.connectOrThrow("PerformInitializeTask.run()");
                        int status = transport.initializeDevice();
                        if (status != 0) {
                            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Transport error in initializeDevice()");
                        } else {
                            status = transport.finishBackup();
                            if (status != 0) {
                                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Transport error in finishBackup()");
                            }
                        }
                        if (status == 0) {
                            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Device init successful");
                            i = length;
                            int millis = (int) (android.os.SystemClock.elapsedRealtime() - startRealtime);
                            strArr = strArr2;
                            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_INITIALIZE, new java.lang.Object[0]);
                            java.io.File stateFileDir = new java.io.File(this.mBaseStateDir, transportDirName);
                            this.mBackupManagerService.resetBackupState(stateFileDir);
                            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_SUCCESS, 0, java.lang.Integer.valueOf(millis));
                            this.mBackupManagerService.recordInitPending(false, transportName, transportDirName);
                            notifyResult(transportName, 0);
                        } else {
                            strArr = strArr2;
                            i = length;
                            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_TRANSPORT_FAILURE, "(initialize)");
                            this.mBackupManagerService.recordInitPending(true, transportName, transportDirName);
                            notifyResult(transportName, status);
                            result = status;
                            try {
                                long delay = transport.requestBackupTime();
                                try {
                                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Init failed on " + transportName + " resched in " + delay);
                                    this.mBackupManagerService.getAlarmManager().set(0, java.lang.System.currentTimeMillis() + delay, this.mBackupManagerService.getRunInitIntent());
                                    result = result;
                                    i2++;
                                    strArr2 = strArr;
                                } catch (java.lang.Exception e) {
                                    e = e;
                                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unexpected error performing init", e);
                                    result = -1000;
                                    for (com.android.server.backup.transport.TransportConnection transportConnection2 : transportClientsToDisposeOf) {
                                        this.mTransportManager.disposeOfTransportClient(transportConnection2, "PerformInitializeTask.run()");
                                    }
                                    notifyFinished(result);
                                    this.mListener.onFinished("PerformInitializeTask.run()");
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    result = result;
                                    for (com.android.server.backup.transport.TransportConnection transportConnection3 : transportClientsToDisposeOf) {
                                        this.mTransportManager.disposeOfTransportClient(transportConnection3, "PerformInitializeTask.run()");
                                    }
                                    notifyFinished(result);
                                    this.mListener.onFinished("PerformInitializeTask.run()");
                                    throw th;
                                }
                            } catch (java.lang.Exception e2) {
                                e = e2;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    }
                    i2++;
                    strArr2 = strArr;
                }
                for (com.android.server.backup.transport.TransportConnection transportConnection4 : transportClientsToDisposeOf) {
                    this.mTransportManager.disposeOfTransportClient(transportConnection4, "PerformInitializeTask.run()");
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Exception e3) {
            e = e3;
        }
        notifyFinished(result);
        this.mListener.onFinished("PerformInitializeTask.run()");
    }
}
