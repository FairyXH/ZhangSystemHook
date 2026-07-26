package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public class ActiveRestoreSession extends android.app.backup.IRestoreSession.Stub {
    private static final java.lang.String DEVICE_NAME_FOR_D2D_SET = "D2D";
    private static final java.lang.String TAG = "RestoreSession";
    private final com.android.server.backup.utils.BackupEligibilityRules mBackupEligibilityRules;
    private final com.android.server.backup.UserBackupManagerService mBackupManagerService;
    private final java.lang.String mPackageName;
    private final com.android.server.backup.TransportManager mTransportManager;
    private final java.lang.String mTransportName;
    private final int mUserId;
    public java.util.List<android.app.backup.RestoreSet> mRestoreSets = null;
    boolean mEnded = false;
    boolean mTimedOut = false;

    public ActiveRestoreSession(com.android.server.backup.UserBackupManagerService backupManagerService, java.lang.String packageName, java.lang.String transportName, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        this.mBackupManagerService = backupManagerService;
        this.mPackageName = packageName;
        this.mTransportManager = backupManagerService.getTransportManager();
        this.mTransportName = transportName;
        this.mUserId = backupManagerService.getUserId();
        this.mBackupEligibilityRules = backupEligibilityRules;
    }

    public void markTimedOut() {
        this.mTimedOut = true;
    }

    public synchronized int getAvailableRestoreSets(android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor) {
        this.mBackupManagerService.getContext().enforceCallingOrSelfPermission("android.permission.BACKUP", "getAvailableRestoreSets");
        if (observer == null) {
            throw new java.lang.IllegalArgumentException("Observer must not be null");
        }
        if (this.mEnded) {
            throw new java.lang.IllegalStateException("Restore session already ended");
        }
        if (this.mTimedOut) {
            android.util.Slog.i(TAG, "Session already timed out");
            return -1;
        }
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            final com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getTransportClient(this.mTransportName, "RestoreSession.getAvailableRestoreSets()");
            if (transportConnection == null) {
                android.util.Slog.w(TAG, "Null transport client getting restore sets");
                return -1;
            }
            this.mBackupManagerService.getBackupHandler().removeMessages(8);
            final com.android.server.backup.UserBackupManagerService.BackupWakeLock wakelock = this.mBackupManagerService.getWakelock();
            wakelock.acquire();
            final com.android.server.backup.TransportManager transportManager = this.mTransportManager;
            com.android.server.backup.internal.OnTaskFinishedListener listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.restore.ActiveRestoreSession$$ExternalSyntheticLambda1
                @Override // com.android.server.backup.internal.OnTaskFinishedListener
                public final void onFinished(java.lang.String str) {
                    com.android.server.backup.restore.ActiveRestoreSession.lambda$getAvailableRestoreSets$0(transportManager, transportConnection, wakelock, str);
                }
            };
            android.os.Message msg = this.mBackupManagerService.getBackupHandler().obtainMessage(6, new com.android.server.backup.params.RestoreGetSetsParams(transportConnection, this, observer, monitor, listener));
            this.mBackupManagerService.getBackupHandler().sendMessage(msg);
            return 0;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error in getAvailableRestoreSets", e);
            return -1;
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    static /* synthetic */ void lambda$getAvailableRestoreSets$0(com.android.server.backup.TransportManager transportManager, com.android.server.backup.transport.TransportConnection transportConnection, com.android.server.backup.UserBackupManagerService.BackupWakeLock wakelock, java.lang.String caller) {
        transportManager.disposeOfTransportClient(transportConnection, caller);
        wakelock.release();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(3:(3:61|31|(8:62|33|58|34|35|36|37|38)(1:42))|60|29) */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0106, code lost:
    
        r0 = th;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized int restoreAll(final long r15, final android.app.backup.IRestoreObserver r17, final android.app.backup.IBackupManagerMonitor r18) {
        /*
            Method dump skipped, instruction units count: 276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.restore.ActiveRestoreSession.restoreAll(long, android.app.backup.IRestoreObserver, android.app.backup.IBackupManagerMonitor):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.backup.params.RestoreParams lambda$restoreAll$1(android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, android.app.backup.RestoreSet restoreSet, com.android.server.backup.transport.TransportConnection transportClient, com.android.server.backup.internal.OnTaskFinishedListener listener) {
        return com.android.server.backup.params.RestoreParams.createForRestoreAll(transportClient, observer, monitor, token, listener, getBackupEligibilityRules(restoreSet));
    }

    /* JADX WARN: Can't wrap try/catch for region: R(3:(3:79|50|(8:82|52|76|53|54|55|56|57)(1:61))|48|78) */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0171, code lost:
    
        r0 = th;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized int restorePackages(final long r16, final android.app.backup.IRestoreObserver r18, final java.lang.String[] r19, final android.app.backup.IBackupManagerMonitor r20) {
        /*
            Method dump skipped, instruction units count: 383
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.restore.ActiveRestoreSession.restorePackages(long, android.app.backup.IRestoreObserver, java.lang.String[], android.app.backup.IBackupManagerMonitor):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.backup.params.RestoreParams lambda$restorePackages$2(android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, java.lang.String[] packages, android.app.backup.RestoreSet restoreSet, com.android.server.backup.transport.TransportConnection transportClient, com.android.server.backup.internal.OnTaskFinishedListener listener) {
        return com.android.server.backup.params.RestoreParams.createForRestorePackages(transportClient, observer, monitor, token, packages, packages.length > 1, listener, getBackupEligibilityRules(restoreSet));
    }

    com.android.server.backup.utils.BackupEligibilityRules getBackupEligibilityRules(android.app.backup.RestoreSet restoreSet) {
        int backupDestination = DEVICE_NAME_FOR_D2D_SET.equals(restoreSet.device) ? 1 : 0;
        if (!com.android.server.backup.Flags.enableSkippingRestoreLaunchedApps()) {
            return this.mBackupManagerService.getEligibilityRulesForOperation(backupDestination);
        }
        boolean skipRestoreForLaunchedApps = (restoreSet.backupTransportFlags & 4) != 0;
        return new com.android.server.backup.utils.BackupEligibilityRules(this.mBackupManagerService.getPackageManager(), (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class), this.mUserId, this.mBackupManagerService.getContext(), backupDestination, skipRestoreForLaunchedApps);
    }

    public synchronized int restorePackage(java.lang.String packageName, final android.app.backup.IRestoreObserver observer, final android.app.backup.IBackupManagerMonitor monitor) {
        android.util.Slog.v(TAG, "restorePackage pkg=" + packageName + " obs=" + observer + "monitor=" + monitor);
        if (this.mEnded) {
            throw new java.lang.IllegalStateException("Restore session already ended");
        }
        if (this.mTimedOut) {
            android.util.Slog.i(TAG, "Session already timed out");
            return -1;
        }
        if (this.mPackageName != null && !this.mPackageName.equals(packageName)) {
            android.util.Slog.e(TAG, "Ignoring attempt to restore pkg=" + packageName + " on session for package " + this.mPackageName);
            return -1;
        }
        try {
            final android.content.pm.PackageInfo app = this.mBackupManagerService.getPackageManager().getPackageInfoAsUser(packageName, 0, this.mUserId);
            int perm = this.mBackupManagerService.getContext().checkPermission("android.permission.BACKUP", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
            if (perm == -1 && app.applicationInfo.uid != android.os.Binder.getCallingUid()) {
                android.util.Slog.w(TAG, "restorePackage: bad packageName=" + packageName + " or calling uid=" + android.os.Binder.getCallingUid());
                throw new java.lang.SecurityException("No permission to restore other packages");
            }
            if (!this.mTransportManager.isTransportRegistered(this.mTransportName)) {
                android.util.Slog.e(TAG, "Transport " + this.mTransportName + " not registered");
                return -1;
            }
            long oldId = android.os.Binder.clearCallingIdentity();
            try {
                final long token = this.mBackupManagerService.getAvailableRestoreToken(packageName);
                android.util.Slog.v(TAG, "restorePackage pkg=" + packageName + " token=" + java.lang.Long.toHexString(token));
                if (token == 0) {
                    android.util.Slog.w(TAG, "No data available for this package; not restoring");
                    return -1;
                }
                return sendRestoreToHandlerLocked(new java.util.function.BiFunction() { // from class: com.android.server.backup.restore.ActiveRestoreSession$$ExternalSyntheticLambda4
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                        return this.f$0.lambda$restorePackage$3(observer, monitor, token, app, (com.android.server.backup.transport.TransportConnection) obj, (com.android.server.backup.internal.OnTaskFinishedListener) obj2);
                    }
                }, "RestoreSession.restorePackage(" + packageName + ")");
            } finally {
                android.os.Binder.restoreCallingIdentity(oldId);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, "Asked to restore nonexistent pkg " + packageName);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.backup.params.RestoreParams lambda$restorePackage$3(android.app.backup.IRestoreObserver observer, android.app.backup.IBackupManagerMonitor monitor, long token, android.content.pm.PackageInfo app, com.android.server.backup.transport.TransportConnection transportClient, com.android.server.backup.internal.OnTaskFinishedListener listener) {
        return com.android.server.backup.params.RestoreParams.createForSinglePackage(transportClient, observer, monitor, token, app, listener, this.mBackupEligibilityRules);
    }

    public void setRestoreSets(java.util.List<android.app.backup.RestoreSet> restoreSets) {
        this.mRestoreSets = restoreSets;
    }

    private int sendRestoreToHandlerLocked(java.util.function.BiFunction<com.android.server.backup.transport.TransportConnection, com.android.server.backup.internal.OnTaskFinishedListener, com.android.server.backup.params.RestoreParams> restoreParamsBuilder, java.lang.String callerLogString) {
        final com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getTransportClient(this.mTransportName, callerLogString);
        if (transportConnection == null) {
            android.util.Slog.e(TAG, "Transport " + this.mTransportName + " got unregistered");
            return -1;
        }
        android.os.Handler backupHandler = this.mBackupManagerService.getBackupHandler();
        backupHandler.removeMessages(8);
        final com.android.server.backup.UserBackupManagerService.BackupWakeLock wakelock = this.mBackupManagerService.getWakelock();
        wakelock.acquire();
        final com.android.server.backup.TransportManager transportManager = this.mTransportManager;
        com.android.server.backup.internal.OnTaskFinishedListener listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.restore.ActiveRestoreSession$$ExternalSyntheticLambda0
            @Override // com.android.server.backup.internal.OnTaskFinishedListener
            public final void onFinished(java.lang.String str) {
                com.android.server.backup.restore.ActiveRestoreSession.lambda$sendRestoreToHandlerLocked$4(transportManager, transportConnection, wakelock, str);
            }
        };
        android.os.Message msg = backupHandler.obtainMessage(3);
        msg.obj = restoreParamsBuilder.apply(transportConnection, listener);
        backupHandler.sendMessage(msg);
        return 0;
    }

    static /* synthetic */ void lambda$sendRestoreToHandlerLocked$4(com.android.server.backup.TransportManager transportManager, com.android.server.backup.transport.TransportConnection transportConnection, com.android.server.backup.UserBackupManagerService.BackupWakeLock wakelock, java.lang.String caller) {
        transportManager.disposeOfTransportClient(transportConnection, caller);
        wakelock.release();
    }

    public class EndRestoreRunnable implements java.lang.Runnable {
        com.android.server.backup.UserBackupManagerService mBackupManager;
        com.android.server.backup.restore.ActiveRestoreSession mSession;

        public EndRestoreRunnable(com.android.server.backup.UserBackupManagerService manager, com.android.server.backup.restore.ActiveRestoreSession session) {
            this.mBackupManager = manager;
            this.mSession = session;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this.mSession) {
                this.mSession.mEnded = true;
            }
            this.mBackupManager.clearRestoreSession(this.mSession);
        }
    }

    public synchronized void endRestoreSession() {
        android.util.Slog.d(TAG, "endRestoreSession");
        if (this.mTimedOut) {
            android.util.Slog.i(TAG, "Session already timed out");
        } else {
            if (this.mEnded) {
                throw new java.lang.IllegalStateException("Restore session already ended");
            }
            this.mBackupManagerService.getBackupHandler().post(new com.android.server.backup.restore.ActiveRestoreSession.EndRestoreRunnable(this.mBackupManagerService, this));
        }
    }
}
