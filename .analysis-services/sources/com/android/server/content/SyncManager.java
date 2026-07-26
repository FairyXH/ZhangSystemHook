package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public class SyncManager {
    private static final boolean DEBUG_ACCOUNT_ACCESS = false;
    private static final int DELAY_RETRY_SYNC_IN_PROGRESS_IN_SECONDS = 10;
    private static final java.lang.String HANDLE_SYNC_ALARM_WAKE_LOCK = "SyncManagerHandleSyncAlarm";
    private static final long SYNC_DELAY_ON_CONFLICT = 10000;
    private static final java.lang.String SYNC_LOOP_WAKE_LOCK = "SyncLoopWakeLock";
    private static final int SYNC_MONITOR_PROGRESS_THRESHOLD_BYTES = 10;
    private static final long SYNC_MONITOR_WINDOW_LENGTH_MILLIS = 60000;
    private static final int SYNC_OP_STATE_INVALID_NOT_SYNCABLE = 4;
    private static final int SYNC_OP_STATE_INVALID_NO_ACCOUNT = 3;
    private static final int SYNC_OP_STATE_INVALID_NO_ACCOUNT_ACCESS = 2;
    private static final int SYNC_OP_STATE_INVALID_SYNC_DISABLED = 5;
    private static final int SYNC_OP_STATE_VALID = 0;
    private static final java.lang.String SYNC_WAKE_LOCK_PREFIX = "*sync*/";
    static final java.lang.String TAG = "SyncManager";
    private static final boolean USE_WTF_FOR_ACCOUNT_ERROR = true;
    private static com.android.server.content.SyncManager sInstance;
    private final android.accounts.AccountManager mAccountManager;
    private final android.accounts.AccountManagerInternal mAccountManagerInternal;
    private final android.app.ActivityManagerInternal mAmi;
    private final com.android.internal.config.appcloning.AppCloningDeviceConfigHelper mAppCloningDeviceConfigHelper;
    private final com.android.internal.app.IBatteryStats mBatteryStats;
    private android.net.ConnectivityManager mConnManagerDoNotUseDirectly;
    private final com.android.server.content.SyncManagerConstants mConstants;
    private android.content.Context mContext;
    private android.app.job.JobScheduler mJobScheduler;
    private final com.android.server.content.SyncLogger mLogger;
    private final android.app.NotificationManager mNotificationMgr;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final android.os.PowerManager mPowerManager;
    private volatile boolean mProvisioned;
    protected final android.content.SyncAdaptersCache mSyncAdapters;
    private final com.android.server.content.SyncManager.SyncHandler mSyncHandler;
    private volatile android.os.PowerManager.WakeLock mSyncManagerWakeLock;
    private com.android.server.content.SyncStorageEngine mSyncStorageEngine;
    private final android.os.HandlerThread mThread;
    private final android.os.UserManager mUserManager;
    private static final boolean ENABLE_SUSPICIOUS_CHECK = android.os.Build.IS_DEBUGGABLE;
    private static final long LOCAL_SYNC_DELAY = android.os.SystemProperties.getLong("sync.local_sync_delay", 30000);
    private static final android.content.Context.BindServiceFlags SYNC_ADAPTER_CONNECTION_FLAGS = android.content.Context.BindServiceFlags.of(21);
    private static final android.accounts.AccountAndUser[] INITIAL_ACCOUNTS_ARRAY = new android.accounts.AccountAndUser[0];
    private static final java.util.Comparator<com.android.server.content.SyncOperation> sOpDumpComparator = new java.util.Comparator() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda2
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.content.SyncManager.lambda$static$6((com.android.server.content.SyncOperation) obj, (com.android.server.content.SyncOperation) obj2);
        }
    };
    private static final java.util.Comparator<com.android.server.content.SyncOperation> sOpRuntimeComparator = new java.util.Comparator() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda3
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.content.SyncManager.lambda$static$7((com.android.server.content.SyncOperation) obj, (com.android.server.content.SyncOperation) obj2);
        }
    };
    private final java.lang.Object mAccountsLock = new java.lang.Object();
    private volatile android.accounts.AccountAndUser[] mRunningAccounts = INITIAL_ACCOUNTS_ARRAY;
    private volatile boolean mDataConnectionIsConnected = false;
    private volatile int mNextJobId = 0;
    protected final java.util.ArrayList<com.android.server.content.SyncManager.ActiveSyncContext> mActiveSyncContexts = com.google.android.collect.Lists.newArrayList();
    com.android.server.content.ISyncManagerExt mSyncManagerExt = (com.android.server.content.ISyncManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.content.ISyncManagerExt.class).base(this).create();
    private final java.lang.String[] mEjSyncAllowedPackages = {"com.google.android.google", "com.android.frameworks.servicestests"};
    private final android.content.BroadcastReceiver mAccountsUpdatedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.content.SyncManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.content.SyncStorageEngine.EndPoint target = new com.android.server.content.SyncStorageEngine.EndPoint(null, null, getSendingUserId());
            com.android.server.content.SyncManager.this.updateRunningAccounts(target);
        }
    };
    private android.content.BroadcastReceiver mConnectivityIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.content.SyncManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            boolean wasConnected = com.android.server.content.SyncManager.this.mDataConnectionIsConnected;
            com.android.server.content.SyncManager.this.mDataConnectionIsConnected = com.android.server.content.SyncManager.this.readDataConnectionState();
            if (com.android.server.content.SyncManager.this.mDataConnectionIsConnected && !wasConnected) {
                if (android.util.Log.isLoggable("SyncManager", 2)) {
                    android.util.Slog.v("SyncManager", "Reconnection detected: clearing all backoffs");
                }
                com.android.server.content.SyncManager.this.clearAllBackoffs("network reconnect");
            }
        }
    };
    private android.content.BroadcastReceiver mShutdownIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.content.SyncManager.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            android.util.Log.w("SyncManager", "Writing sync state before shutdown...");
            com.android.server.content.SyncManager.this.getSyncStorageEngine().writeAllState();
            com.android.server.content.SyncManager.this.mLogger.log(com.android.server.content.SyncManager.this.getJobStats());
            com.android.server.content.SyncManager.this.mLogger.log("Shutting down.");
        }
    };
    private final android.content.BroadcastReceiver mOtherIntentsReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.content.SyncManager.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.TIME_SET".equals(intent.getAction())) {
                com.android.server.content.SyncManager.this.mSyncStorageEngine.setClockValid();
            }
        }
    };
    private android.content.BroadcastReceiver mUserIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.content.SyncManager.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) throws java.lang.Throwable {
            java.lang.String action = intent.getAction();
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (userId == -10000) {
                return;
            }
            if ("android.intent.action.USER_REMOVED".equals(action)) {
                com.android.server.content.SyncManager.this.onUserRemoved(userId);
            } else if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                com.android.server.content.SyncManager.this.onUserUnlocked(userId);
            } else if ("android.intent.action.USER_STOPPED".equals(action)) {
                com.android.server.content.SyncManager.this.onUserStopped(userId);
            }
        }
    };
    private final android.util.SparseBooleanArray mUnlockedUsers = new android.util.SparseBooleanArray();

    interface OnReadyCallback {
        void onReady();
    }

    private boolean isJobIdInUseLockedH(int jobId, java.util.List<android.app.job.JobInfo> pendingJobs) {
        int size = pendingJobs.size();
        for (int i = 0; i < size; i++) {
            android.app.job.JobInfo job = pendingJobs.get(i);
            if (job.getId() == jobId) {
                return true;
            }
        }
        int size2 = this.mActiveSyncContexts.size();
        for (int i2 = 0; i2 < size2; i2++) {
            com.android.server.content.SyncManager.ActiveSyncContext asc = this.mActiveSyncContexts.get(i2);
            if (asc.mSyncOperation.jobId == jobId) {
                return true;
            }
        }
        return false;
    }

    private int getUnusedJobIdH() {
        java.util.List<android.app.job.JobInfo> pendingJobs = this.mJobScheduler.getAllPendingJobs();
        while (isJobIdInUseLockedH(this.mNextJobId, pendingJobs)) {
            this.mNextJobId++;
        }
        return this.mNextJobId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<com.android.server.content.SyncOperation> getAllPendingSyncs() {
        verifyJobScheduler();
        java.util.List<android.app.job.JobInfo> pendingJobs = this.mJobScheduler.getAllPendingJobs();
        int numJobs = pendingJobs.size();
        java.util.List<com.android.server.content.SyncOperation> pendingSyncs = new java.util.ArrayList<>(numJobs);
        for (int i = 0; i < numJobs; i++) {
            android.app.job.JobInfo job = pendingJobs.get(i);
            com.android.server.content.SyncOperation op = com.android.server.content.SyncOperation.maybeCreateFromJobExtras(job.getExtras());
            if (op != null) {
                pendingSyncs.add(op);
            } else {
                android.util.Slog.wtf("SyncManager", "Non-sync job inside of SyncManager's namespace");
            }
        }
        return pendingSyncs;
    }

    private java.util.List<android.content.pm.UserInfo> getAllUsers() {
        return this.mUserManager.getUsers();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean containsAccountAndUser(android.accounts.AccountAndUser[] accounts, android.accounts.Account account, int userId) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].userId == userId && accounts[i].account.equals(account)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRunningAccounts(com.android.server.content.SyncStorageEngine.EndPoint target) {
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "sending MESSAGE_ACCOUNTS_UPDATED");
        }
        android.os.Message m = this.mSyncHandler.obtainMessage(9);
        m.obj = target;
        m.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeStaleAccounts() {
        for (android.content.pm.UserInfo user : this.mUserManager.getAliveUsers()) {
            if (!user.partial) {
                android.accounts.Account[] accountsForUser = com.android.server.accounts.AccountManagerService.getSingleton().getAccounts(user.id, this.mContext.getOpPackageName());
                this.mSyncStorageEngine.removeStaleAccounts(accountsForUser, user.id);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAllBackoffs(java.lang.String why) {
        this.mSyncStorageEngine.clearAllBackoffsLocked();
        rescheduleSyncs(com.android.server.content.SyncStorageEngine.EndPoint.USER_ALL_PROVIDER_ALL_ACCOUNTS_ALL, why);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readDataConnectionState() {
        android.net.NetworkInfo networkInfo = getConnectivityManager().getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getJobStats() {
        com.android.server.job.JobSchedulerInternal js = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
        return "JobStats: " + (js == null ? "(JobSchedulerInternal==null)" : js.getPersistStats().toString());
    }

    private static class PackageMonitorImpl extends com.android.internal.content.PackageMonitor {
        private PackageMonitorImpl() {
        }

        public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] packageNames, int uid, boolean doit, android.os.Bundle extras) {
            boolean isLoggable = android.util.Log.isLoggable("SyncManager", 3);
            if (isLoggable) {
                android.util.Log.d("SyncManager", "Package force-stopped: " + java.util.Arrays.toString(packageNames) + ", uid: " + uid);
                return false;
            }
            return false;
        }

        public void onPackageUnstopped(java.lang.String packageName, int uid, android.os.Bundle extras) {
            boolean isLoggable = android.util.Log.isLoggable("SyncManager", 3);
            if (isLoggable) {
                android.util.Log.d("SyncManager", "Package unstopped: " + packageName + ", uid: " + uid);
            }
        }
    }

    private android.net.ConnectivityManager getConnectivityManager() {
        android.net.ConnectivityManager connectivityManager;
        synchronized (this) {
            if (this.mConnManagerDoNotUseDirectly == null) {
                this.mConnManagerDoNotUseDirectly = (android.net.ConnectivityManager) this.mContext.getSystemService("connectivity");
            }
            connectivityManager = this.mConnManagerDoNotUseDirectly;
        }
        return connectivityManager;
    }

    private void cleanupJobs() {
        this.mSyncHandler.postAtFrontOfQueue(new java.lang.Runnable() { // from class: com.android.server.content.SyncManager.6
            @Override // java.lang.Runnable
            public void run() {
                java.util.List<com.android.server.content.SyncOperation> ops = com.android.server.content.SyncManager.this.getAllPendingSyncs();
                java.util.Set<java.lang.String> cleanedKeys = new java.util.HashSet<>();
                for (com.android.server.content.SyncOperation opx : ops) {
                    if (!cleanedKeys.contains(opx.key)) {
                        cleanedKeys.add(opx.key);
                        for (com.android.server.content.SyncOperation opy : ops) {
                            if (opx != opy && opx.key.equals(opy.key)) {
                                com.android.server.content.SyncManager.this.mLogger.log("Removing duplicate sync: ", opy);
                                com.android.server.content.SyncManager.this.cancelJob(opy, "cleanupJobs() x=" + opx + " y=" + opy);
                            }
                        }
                    }
                }
            }
        });
    }

    private void migrateSyncJobNamespaceIfNeeded() {
        boolean namespaceMigrated = this.mSyncStorageEngine.isJobNamespaceMigrated();
        boolean attributionFixed = this.mSyncStorageEngine.isJobAttributionFixed();
        if (namespaceMigrated && attributionFixed) {
            return;
        }
        android.app.job.JobScheduler jobSchedulerDefaultNamespace = (android.app.job.JobScheduler) this.mContext.getSystemService(android.app.job.JobScheduler.class);
        if (!namespaceMigrated) {
            java.util.List<android.app.job.JobInfo> pendingJobs = jobSchedulerDefaultNamespace.getAllPendingJobs();
            boolean allSyncsMigrated = true;
            for (int i = pendingJobs.size() - 1; i >= 0; i--) {
                android.app.job.JobInfo job = pendingJobs.get(i);
                com.android.server.content.SyncOperation op = com.android.server.content.SyncOperation.maybeCreateFromJobExtras(job.getExtras());
                if (op != null) {
                    this.mJobScheduler.scheduleAsPackage(job, op.owningPackage, op.target.userId, op.wakeLockName());
                    jobSchedulerDefaultNamespace.cancel(job.getId());
                    allSyncsMigrated = false;
                }
            }
            this.mSyncStorageEngine.setJobNamespaceMigrated(allSyncsMigrated);
        }
        java.util.List<android.app.job.JobInfo> namespacedJobs = ((com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class)).getSystemScheduledOwnJobs(this.mJobScheduler.getNamespace());
        boolean allSyncsAttributed = true;
        for (int i2 = namespacedJobs.size() - 1; i2 >= 0; i2--) {
            android.app.job.JobInfo job2 = namespacedJobs.get(i2);
            com.android.server.content.SyncOperation op2 = com.android.server.content.SyncOperation.maybeCreateFromJobExtras(job2.getExtras());
            if (op2 != null) {
                this.mJobScheduler.scheduleAsPackage(job2, op2.owningPackage, op2.target.userId, op2.wakeLockName());
                allSyncsAttributed = false;
            }
        }
        this.mSyncStorageEngine.setJobAttributionFixed(allSyncsAttributed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void verifyJobScheduler() {
        if (this.mJobScheduler != null) {
            return;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                try {
                    android.util.Log.d("SyncManager", "initializing JobScheduler object.");
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
            this.mJobScheduler = ((android.app.job.JobScheduler) this.mContext.getSystemService(android.app.job.JobScheduler.class)).forNamespace("SyncManager");
            migrateSyncJobNamespaceIfNeeded();
            java.util.List<android.app.job.JobInfo> pendingJobs = this.mJobScheduler.getAllPendingJobs();
            int numPersistedPeriodicSyncs = 0;
            int numPersistedOneshotSyncs = 0;
            for (android.app.job.JobInfo job : pendingJobs) {
                com.android.server.content.SyncOperation op = com.android.server.content.SyncOperation.maybeCreateFromJobExtras(job.getExtras());
                if (op != null) {
                    if (op.isPeriodic) {
                        numPersistedPeriodicSyncs++;
                    } else {
                        numPersistedOneshotSyncs++;
                        this.mSyncStorageEngine.markPending(op.target, true);
                    }
                } else {
                    android.util.Slog.wtf("SyncManager", "Non-sync job inside of SyncManager namespace");
                }
            }
            java.lang.String summary = "Loaded persisted syncs: " + numPersistedPeriodicSyncs + " periodic syncs, " + numPersistedOneshotSyncs + " oneshot syncs, " + pendingJobs.size() + " total system server jobs, " + getJobStats();
            android.util.Slog.i("SyncManager", summary);
            this.mLogger.log(summary);
            cleanupJobs();
            if (ENABLE_SUSPICIOUS_CHECK && numPersistedPeriodicSyncs == 0 && likelyHasPeriodicSyncs()) {
                android.util.Slog.wtf("SyncManager", "Device booted with no persisted periodic syncs: " + summary);
            }
            android.os.Binder.restoreCallingIdentity(token);
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    private boolean likelyHasPeriodicSyncs() {
        try {
            return this.mSyncStorageEngine.getAuthorityCount() >= 6;
        } catch (java.lang.Throwable th) {
            return false;
        }
    }

    private android.app.job.JobScheduler getJobScheduler() {
        verifyJobScheduler();
        return this.mJobScheduler;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SyncManager(android.content.Context context, boolean z) {
        this.mSyncManagerExt.init(context);
        synchronized (com.android.server.content.SyncManager.class) {
            if (sInstance == null) {
                sInstance = this;
            } else {
                android.util.Slog.wtf("SyncManager", "SyncManager instantiated multiple times");
            }
        }
        this.mContext = context;
        this.mLogger = com.android.server.content.SyncLogger.getInstance();
        com.android.server.content.SyncStorageEngine.init(context, com.android.internal.os.BackgroundThread.get().getLooper());
        this.mSyncStorageEngine = com.android.server.content.SyncStorageEngine.getSingleton();
        this.mSyncStorageEngine.setOnSyncRequestListener(new com.android.server.content.SyncStorageEngine.OnSyncRequestListener() { // from class: com.android.server.content.SyncManager.7
            @Override // com.android.server.content.SyncStorageEngine.OnSyncRequestListener
            public void onSyncRequest(com.android.server.content.SyncStorageEngine.EndPoint info, int reason, android.os.Bundle extras, int syncExemptionFlag, int callingUid, int callingPid) throws java.lang.Throwable {
                com.android.server.content.SyncManager.this.scheduleSync(info.account, info.userId, reason, info.provider, extras, -2, syncExemptionFlag, callingUid, callingPid, null);
            }
        });
        this.mSyncStorageEngine.setPeriodicSyncAddedListener(new com.android.server.content.SyncStorageEngine.PeriodicSyncAddedListener() { // from class: com.android.server.content.SyncManager.8
            @Override // com.android.server.content.SyncStorageEngine.PeriodicSyncAddedListener
            public void onPeriodicSyncAdded(com.android.server.content.SyncStorageEngine.EndPoint target, android.os.Bundle extras, long pollFrequency, long flex) {
                com.android.server.content.SyncManager.this.updateOrAddPeriodicSync(target, pollFrequency, flex, extras);
            }
        });
        this.mSyncStorageEngine.setOnAuthorityRemovedListener(new com.android.server.content.SyncStorageEngine.OnAuthorityRemovedListener() { // from class: com.android.server.content.SyncManager.9
            @Override // com.android.server.content.SyncStorageEngine.OnAuthorityRemovedListener
            public void onAuthorityRemoved(com.android.server.content.SyncStorageEngine.EndPoint removedAuthority) {
                com.android.server.content.SyncManager.this.removeSyncsForAuthority(removedAuthority, "onAuthorityRemoved");
            }
        });
        this.mSyncAdapters = new android.content.SyncAdaptersCache(this.mContext);
        this.mThread = new android.os.HandlerThread("SyncManager", 10);
        this.mThread.start();
        this.mSyncHandler = new com.android.server.content.SyncManager.SyncHandler(this.mThread.getLooper());
        this.mSyncAdapters.setListener(new android.content.pm.RegisteredServicesCacheListener<android.content.SyncAdapterType>() { // from class: com.android.server.content.SyncManager.10
            public void onServiceChanged(android.content.SyncAdapterType type, int userId, boolean removed) throws java.lang.Throwable {
                if (!removed) {
                    com.android.server.content.SyncManager.this.scheduleSync(null, -1, -3, type.authority, null, -2, 0, android.os.Process.myUid(), -1, null);
                }
            }
        }, this.mSyncHandler);
        this.mConstants = new com.android.server.content.SyncManagerConstants(context);
        this.mAppCloningDeviceConfigHelper = com.android.internal.config.appcloning.AppCloningDeviceConfigHelper.getInstance(context);
        context.registerReceiver(this.mConnectivityIntentReceiver, new android.content.IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.setPriority(1000);
        context.registerReceiver(this.mShutdownIntentReceiver, intentFilter);
        android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_REMOVED");
        intentFilter2.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter2.addAction("android.intent.action.USER_STOPPED");
        this.mContext.registerReceiverAsUser(this.mUserIntentReceiver, android.os.UserHandle.ALL, intentFilter2, null, null);
        java.lang.Object[] objArr = 0;
        new com.android.server.content.SyncManager.PackageMonitorImpl().register(this.mContext, (android.os.Looper) null, android.os.UserHandle.ALL, false);
        context.registerReceiver(this.mOtherIntentsReceiver, new android.content.IntentFilter("android.intent.action.TIME_SET"));
        if (!z) {
            this.mNotificationMgr = (android.app.NotificationManager) context.getSystemService("notification");
        } else {
            this.mNotificationMgr = null;
        }
        this.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService("user");
        this.mAccountManager = (android.accounts.AccountManager) this.mContext.getSystemService("account");
        this.mAccountManagerInternal = getAccountManagerInternal();
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mAmi = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mAccountManagerInternal.addOnAppPermissionChangeListener(new android.accounts.AccountManagerInternal.OnAppPermissionChangeListener() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda1
            public final void onAppPermissionChanged(android.accounts.Account account, int i) throws java.lang.Throwable {
                this.f$0.lambda$new$0(account, i);
            }
        });
        this.mBatteryStats = com.android.internal.app.IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService("batterystats"));
        this.mSyncManagerWakeLock = this.mPowerManager.newWakeLock(1, SYNC_LOOP_WAKE_LOCK);
        this.mSyncManagerWakeLock.setReferenceCounted(false);
        this.mProvisioned = isDeviceProvisioned();
        if (!this.mProvisioned) {
            final android.content.ContentResolver contentResolver = context.getContentResolver();
            android.database.ContentObserver contentObserver = new android.database.ContentObserver(objArr == true ? 1 : 0) { // from class: com.android.server.content.SyncManager.11
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    com.android.server.content.SyncManager.this.mProvisioned |= com.android.server.content.SyncManager.this.isDeviceProvisioned();
                    if (com.android.server.content.SyncManager.this.mProvisioned) {
                        contentResolver.unregisterContentObserver(this);
                    }
                }
            };
            synchronized (this.mSyncHandler) {
                contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("device_provisioned"), false, contentObserver);
                this.mProvisioned |= isDeviceProvisioned();
                if (this.mProvisioned) {
                    contentResolver.unregisterContentObserver(contentObserver);
                }
            }
        }
        if (!z) {
            this.mContext.registerReceiverAsUser(this.mAccountsUpdatedReceiver, android.os.UserHandle.ALL, new android.content.IntentFilter("android.accounts.LOGIN_ACCOUNTS_CHANGED"), null, null);
        }
        allowListExistingSyncAdaptersIfNeeded();
        this.mLogger.log("Sync manager initialized: " + android.os.Build.FINGERPRINT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.accounts.Account account, int uid) throws java.lang.Throwable {
        if (this.mAccountManagerInternal.hasAccountAccess(account, uid)) {
            scheduleSync(account, android.os.UserHandle.getUserId(uid), -2, null, null, 3, 0, android.os.Process.myUid(), -2, null);
        }
    }

    protected android.accounts.AccountManagerInternal getAccountManagerInternal() {
        return (android.accounts.AccountManagerInternal) com.android.server.LocalServices.getService(android.accounts.AccountManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartUser$1(int userId) {
        this.mLogger.log("onStartUser: user=", java.lang.Integer.valueOf(userId));
    }

    public void onStartUser(final int userId) {
        this.mSyncHandler.post(new java.lang.Runnable() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStartUser$1(userId);
            }
        });
    }

    public void onUnlockUser(final int userId) {
        synchronized (this.mUnlockedUsers) {
            this.mUnlockedUsers.put(userId, true);
        }
        this.mSyncHandler.post(new java.lang.Runnable() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUnlockUser$2(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUnlockUser$2(int userId) {
        this.mLogger.log("onUnlockUser: user=", java.lang.Integer.valueOf(userId));
    }

    public void onStopUser(final int userId) {
        synchronized (this.mUnlockedUsers) {
            this.mUnlockedUsers.put(userId, false);
        }
        this.mSyncHandler.post(new java.lang.Runnable() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStopUser$3(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStopUser$3(int userId) {
        this.mLogger.log("onStopUser: user=", java.lang.Integer.valueOf(userId));
    }

    private boolean isUserUnlocked(int userId) {
        boolean z;
        synchronized (this.mUnlockedUsers) {
            z = this.mUnlockedUsers.get(userId);
        }
        return z;
    }

    public void onBootPhase(int phase) {
        this.mSyncManagerExt.onBootPhase(phase);
        switch (phase) {
            case 550:
                this.mConstants.start();
                break;
        }
    }

    private void allowListExistingSyncAdaptersIfNeeded() {
        com.android.server.content.SyncManager syncManager = this;
        if (!syncManager.mSyncStorageEngine.shouldGrantSyncAdaptersAccountAccess()) {
            return;
        }
        java.util.List<android.content.pm.UserInfo> users = syncManager.mUserManager.getAliveUsers();
        int userCount = users.size();
        int i = 0;
        while (i < userCount) {
            android.os.UserHandle userHandle = users.get(i).getUserHandle();
            int userId = userHandle.getIdentifier();
            for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> service : syncManager.mSyncAdapters.getAllServices(userId)) {
                java.lang.String packageName = service.componentName.getPackageName();
                android.accounts.Account[] accountsByTypeAsUser = syncManager.mAccountManager.getAccountsByTypeAsUser(((android.content.SyncAdapterType) service.type).accountType, userHandle);
                int length = accountsByTypeAsUser.length;
                int i2 = 0;
                while (i2 < length) {
                    android.accounts.Account account = accountsByTypeAsUser[i2];
                    if (!syncManager.canAccessAccount(account, packageName, userId)) {
                        syncManager.mAccountManager.updateAppPermission(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", service.uid, true);
                    }
                    i2++;
                    syncManager = this;
                }
                syncManager = this;
            }
            i++;
            syncManager = this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceProvisioned() {
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        return android.provider.Settings.Global.getInt(resolver, "device_provisioned", 0) != 0;
    }

    private long jitterize(long minValue, long maxValue) {
        java.util.Random random = new java.util.Random(android.os.SystemClock.elapsedRealtime());
        long spread = maxValue - minValue;
        if (spread > 2147483647L) {
            throw new java.lang.IllegalArgumentException("the difference between the maxValue and the minValue must be less than 2147483647");
        }
        return ((long) random.nextInt((int) spread)) + minValue;
    }

    public com.android.server.content.SyncStorageEngine getSyncStorageEngine() {
        return this.mSyncStorageEngine;
    }

    private boolean areContactWritesEnabledForUser(android.content.pm.UserInfo userInfo) {
        android.os.UserManager um = android.os.UserManager.get(this.mContext);
        try {
            android.content.pm.UserProperties userProperties = um.getUserProperties(userInfo.getUserHandle());
            return !userProperties.getUseParentsContacts();
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.w("SyncManager", "Trying to fetch user properties for non-existing/partial user " + userInfo.getUserHandle());
            return false;
        }
    }

    protected boolean isContactSharingAllowedForCloneProfile() {
        return this.mContext.getResources().getBoolean(android.R.bool.config_earcFeatureEnabled_default) && this.mAppCloningDeviceConfigHelper.getEnableAppCloningBuildingBlocks();
    }

    protected boolean shouldDisableSyncForUser(android.content.pm.UserInfo userInfo, java.lang.String providerName) {
        return (userInfo == null || providerName == null || !isContactSharingAllowedForCloneProfile() || !providerName.equals("com.android.contacts") || areContactWritesEnabledForUser(userInfo)) ? false : true;
    }

    private int getIsSyncable(android.accounts.Account account, int userId, java.lang.String providerName) {
        int isSyncable = this.mSyncStorageEngine.getIsSyncable(account, userId, providerName);
        android.os.UserManager um = android.os.UserManager.get(this.mContext);
        android.content.pm.UserInfo userInfo = um.getUserInfo(userId);
        if (shouldDisableSyncForUser(userInfo, providerName)) {
            android.util.Log.w("SyncManager", "Account sync is disabled for account: " + account + " userId: " + userId + " provider: " + providerName);
            return 0;
        }
        if (userInfo == null || !userInfo.isRestricted()) {
            return isSyncable;
        }
        android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterInfo = this.mSyncAdapters.getServiceInfo(android.content.SyncAdapterType.newKey(providerName, account.type), userId);
        if (syncAdapterInfo == null) {
            return 0;
        }
        try {
            android.content.pm.PackageInfo pInfo = android.app.AppGlobals.getPackageManager().getPackageInfo(syncAdapterInfo.componentName.getPackageName(), 0L, userId);
            if (pInfo == null || pInfo.restrictedAccountType == null || !pInfo.restrictedAccountType.equals(account.type)) {
                return 0;
            }
            return isSyncable;
        } catch (android.os.RemoteException e) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAuthorityPendingState(com.android.server.content.SyncStorageEngine.EndPoint info) {
        java.util.List<com.android.server.content.SyncOperation> ops = getAllPendingSyncs();
        for (com.android.server.content.SyncOperation op : ops) {
            if (!op.isPeriodic && op.target.matchesSpec(info)) {
                getSyncStorageEngine().markPending(info, true);
                return;
            }
        }
        getSyncStorageEngine().markPending(info, false);
    }

    public void scheduleSync(android.accounts.Account requestedAccount, int userId, int reason, java.lang.String requestedAuthority, android.os.Bundle extras, int targetSyncState, int syncExemptionFlag, int callingUid, int callingPid, java.lang.String callingPackage) throws java.lang.Throwable {
        scheduleSync(requestedAccount, userId, reason, requestedAuthority, extras, targetSyncState, 0L, true, syncExemptionFlag, callingUid, callingPid, callingPackage);
    }

    /* JADX WARN: Removed duplicated region for block: B:158:0x0323 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02fb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b3 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void scheduleSync(android.accounts.Account r51, final int r52, final int r53, java.lang.String r54, android.os.Bundle r55, final int r56, final long r57, boolean r59, final int r60, final int r61, final int r62, final java.lang.String r63) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1158
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.content.SyncManager.scheduleSync(android.accounts.Account, int, int, java.lang.String, android.os.Bundle, int, long, boolean, int, int, int, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleSync$4(android.accounts.AccountAndUser account, int userId, int reason, java.lang.String authority, android.os.Bundle finalExtras, int targetSyncState, long minDelayMillis, int syncExemptionFlag, int callingUid, int callingPid, java.lang.String callingPackage, android.os.Bundle result) throws java.lang.Throwable {
        if (result != null && result.getBoolean("booleanResult")) {
            scheduleSync(account.account, userId, reason, authority, finalExtras, targetSyncState, minDelayMillis, true, syncExemptionFlag, callingUid, callingPid, callingPackage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleSync$5(android.accounts.AccountAndUser account, int reason, java.lang.String authority, android.os.Bundle finalExtras, int targetSyncState, long minDelayMillis, int syncExemptionFlag, int callingUid, int callingPid, java.lang.String callingPackage) throws java.lang.Throwable {
        scheduleSync(account.account, account.userId, reason, authority, finalExtras, targetSyncState, minDelayMillis, false, syncExemptionFlag, callingUid, callingPid, callingPackage);
    }

    public int computeSyncable(android.accounts.Account account, int userId, java.lang.String authority, boolean checkAccountAccess, boolean checkStoppedState) {
        int status = getIsSyncable(account, userId, authority);
        if (status == 0) {
            return 0;
        }
        android.content.SyncAdapterType type = android.content.SyncAdapterType.newKey(authority, account.type);
        android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterInfo = this.mSyncAdapters.getServiceInfo(type, userId);
        if (syncAdapterInfo == null) {
            return 0;
        }
        int owningUid = syncAdapterInfo.uid;
        java.lang.String owningPackage = syncAdapterInfo.componentName.getPackageName();
        if (checkStoppedState && isPackageStopped(owningPackage, userId)) {
            return 0;
        }
        if (this.mAmi.isAppStartModeDisabled(owningUid, owningPackage)) {
            android.util.Slog.w("SyncManager", "Not scheduling job " + syncAdapterInfo.uid + ":" + syncAdapterInfo.componentName + " -- package not allowed to start");
            return 0;
        }
        if (checkAccountAccess && !canAccessAccount(account, owningPackage, owningUid)) {
            android.util.Log.w("SyncManager", "Access to " + com.android.server.content.SyncLogger.logSafe(account) + " denied for package " + owningPackage + " in UID " + syncAdapterInfo.uid);
            return 3;
        }
        return status;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPackageStopped(java.lang.String packageName, int userId) {
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.stayStopped()) {
            try {
                return this.mPackageManagerInternal.isPackageStopped(packageName, userId);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e("SyncManager", "Couldn't determine stopped state for unknown package: " + packageName);
                return false;
            }
        }
        return false;
    }

    private boolean canAccessAccount(android.accounts.Account account, java.lang.String packageName, int uid) {
        if (this.mAccountManager.hasAccountAccess(account, packageName, android.os.UserHandle.getUserHandleForUid(uid))) {
            return true;
        }
        try {
            this.mContext.getPackageManager().getApplicationInfoAsUser(packageName, 1048576, android.os.UserHandle.getUserId(uid));
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSyncsForAuthority(com.android.server.content.SyncStorageEngine.EndPoint info, java.lang.String why) {
        this.mLogger.log("removeSyncsForAuthority: ", info, why);
        verifyJobScheduler();
        java.util.List<com.android.server.content.SyncOperation> ops = getAllPendingSyncs();
        for (com.android.server.content.SyncOperation op : ops) {
            if (op.target.matchesSpec(info)) {
                this.mLogger.log("canceling: ", op);
                cancelJob(op, why);
            }
        }
    }

    public void removePeriodicSync(com.android.server.content.SyncStorageEngine.EndPoint target, android.os.Bundle extras, java.lang.String why) {
        android.os.Message m = this.mSyncHandler.obtainMessage(14, android.util.Pair.create(target, why));
        m.setData(extras);
        m.sendToTarget();
    }

    public void updateOrAddPeriodicSync(com.android.server.content.SyncStorageEngine.EndPoint target, long pollFrequency, long flex, android.os.Bundle extras) {
        com.android.server.content.SyncManager.UpdatePeriodicSyncMessagePayload payload = new com.android.server.content.SyncManager.UpdatePeriodicSyncMessagePayload(target, pollFrequency, flex, extras);
        this.mSyncHandler.obtainMessage(13, payload).sendToTarget();
    }

    public java.util.List<android.content.PeriodicSync> getPeriodicSyncs(com.android.server.content.SyncStorageEngine.EndPoint target) {
        java.util.List<com.android.server.content.SyncOperation> ops = getAllPendingSyncs();
        java.util.List<android.content.PeriodicSync> periodicSyncs = new java.util.ArrayList<>();
        for (com.android.server.content.SyncOperation op : ops) {
            if (op.isPeriodic && op.target.matchesSpec(target)) {
                periodicSyncs.add(new android.content.PeriodicSync(op.target.account, op.target.provider, op.getClonedExtras(), op.periodMillis / 1000, op.flexMillis / 1000));
            }
        }
        return periodicSyncs;
    }

    public void scheduleLocalSync(android.accounts.Account account, int userId, int reason, java.lang.String authority, int syncExemptionFlag, int callingUid, int callingPid, java.lang.String callingPackage) throws java.lang.Throwable {
        android.os.Bundle extras = new android.os.Bundle();
        extras.putBoolean("upload", true);
        scheduleSync(account, userId, reason, authority, extras, -2, LOCAL_SYNC_DELAY, true, syncExemptionFlag, callingUid, callingPid, callingPackage);
    }

    public android.content.SyncAdapterType[] getSyncAdapterTypes(int callingUid, int userId) {
        java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType>> serviceInfos = this.mSyncAdapters.getAllServices(userId);
        java.util.List<android.content.SyncAdapterType> types = new java.util.ArrayList<>(serviceInfos.size());
        for (android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> serviceInfo : serviceInfos) {
            java.lang.String packageName = ((android.content.SyncAdapterType) serviceInfo.type).getPackageName();
            if (android.text.TextUtils.isEmpty(packageName) || !this.mPackageManagerInternal.filterAppAccess(packageName, callingUid, userId)) {
                types.add((android.content.SyncAdapterType) serviceInfo.type);
            }
        }
        return (android.content.SyncAdapterType[]) types.toArray(new android.content.SyncAdapterType[0]);
    }

    public java.lang.String[] getSyncAdapterPackagesForAuthorityAsUser(java.lang.String authority, int callingUid, int userId) {
        java.lang.String[] syncAdapterPackages = this.mSyncAdapters.getSyncAdapterPackagesForAuthority(authority, userId);
        java.util.List<java.lang.String> filteredResult = new java.util.ArrayList<>(syncAdapterPackages.length);
        for (java.lang.String packageName : syncAdapterPackages) {
            if (!android.text.TextUtils.isEmpty(packageName) && !this.mPackageManagerInternal.filterAppAccess(packageName, callingUid, userId)) {
                filteredResult.add(packageName);
            }
        }
        return (java.lang.String[]) filteredResult.toArray(new java.lang.String[0]);
    }

    public java.lang.String getSyncAdapterPackageAsUser(java.lang.String accountType, java.lang.String authority, int callingUid, int userId) {
        android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterInfo;
        if (accountType == null || authority == null || (syncAdapterInfo = this.mSyncAdapters.getServiceInfo(android.content.SyncAdapterType.newKey(authority, accountType), userId)) == null) {
            return null;
        }
        java.lang.String packageName = ((android.content.SyncAdapterType) syncAdapterInfo.type).getPackageName();
        if (android.text.TextUtils.isEmpty(packageName) || this.mPackageManagerInternal.filterAppAccess(packageName, callingUid, userId)) {
            return null;
        }
        return packageName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSyncFinishedOrCanceledMessage(com.android.server.content.SyncManager.ActiveSyncContext syncContext, android.content.SyncResult syncResult) {
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "sending MESSAGE_SYNC_FINISHED");
        }
        android.os.Message msg = this.mSyncHandler.obtainMessage();
        msg.what = 1;
        msg.obj = new com.android.server.content.SyncManager.SyncFinishedOrCancelledMessagePayload(syncContext, syncResult);
        this.mSyncHandler.sendMessage(msg);
    }

    private void sendCancelSyncsMessage(com.android.server.content.SyncStorageEngine.EndPoint info, android.os.Bundle extras, java.lang.String why) {
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "sending MESSAGE_CANCEL");
        }
        this.mLogger.log("sendCancelSyncsMessage() ep=", info, " why=", why);
        android.os.Message msg = this.mSyncHandler.obtainMessage();
        msg.what = 6;
        msg.setData(extras);
        msg.obj = info;
        this.mSyncHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postMonitorSyncProgressMessage(com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext) {
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "posting MESSAGE_SYNC_MONITOR in 60s");
        }
        activeSyncContext.mBytesTransferredAtLastPoll = getTotalBytesTransferredByUid(activeSyncContext.mSyncAdapterUid);
        activeSyncContext.mLastPolledTimeElapsed = android.os.SystemClock.elapsedRealtime();
        android.os.Message monitorMessage = this.mSyncHandler.obtainMessage(8, activeSyncContext);
        this.mSyncHandler.sendMessageDelayed(monitorMessage, 60000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postScheduleSyncMessage(com.android.server.content.SyncOperation syncOperation, long minDelayMillis) {
        com.android.server.content.SyncManager.ScheduleSyncMessagePayload payload = new com.android.server.content.SyncManager.ScheduleSyncMessagePayload(syncOperation, minDelayMillis);
        this.mSyncHandler.obtainMessage(12, payload).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getTotalBytesTransferredByUid(int uid) {
        return android.net.TrafficStats.getUidRxBytes(uid) + android.net.TrafficStats.getUidTxBytes(uid);
    }

    private class SyncFinishedOrCancelledMessagePayload {
        public final com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext;
        public final android.content.SyncResult syncResult;

        SyncFinishedOrCancelledMessagePayload(com.android.server.content.SyncManager.ActiveSyncContext syncContext, android.content.SyncResult syncResult) {
            this.activeSyncContext = syncContext;
            this.syncResult = syncResult;
        }
    }

    private class UpdatePeriodicSyncMessagePayload {
        public final android.os.Bundle extras;
        public final long flex;
        public final long pollFrequency;
        public final com.android.server.content.SyncStorageEngine.EndPoint target;

        UpdatePeriodicSyncMessagePayload(com.android.server.content.SyncStorageEngine.EndPoint target, long pollFrequency, long flex, android.os.Bundle extras) {
            this.target = target;
            this.pollFrequency = pollFrequency;
            this.flex = flex;
            this.extras = extras;
        }
    }

    private static class ScheduleSyncMessagePayload {
        final long minDelayMillis;
        final com.android.server.content.SyncOperation syncOperation;

        ScheduleSyncMessagePayload(com.android.server.content.SyncOperation syncOperation, long minDelayMillis) {
            this.syncOperation = syncOperation;
            this.minDelayMillis = minDelayMillis;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearBackoffSetting(com.android.server.content.SyncStorageEngine.EndPoint target, java.lang.String why) {
        android.util.Pair<java.lang.Long, java.lang.Long> backoff = this.mSyncStorageEngine.getBackoff(target);
        if (backoff != null && ((java.lang.Long) backoff.first).longValue() == -1 && ((java.lang.Long) backoff.second).longValue() == -1) {
            return;
        }
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "Clearing backoffs for " + target);
        }
        this.mSyncStorageEngine.setBackoff(target, -1L, -1L);
        rescheduleSyncs(target, why);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void increaseBackoffSetting(com.android.server.content.SyncStorageEngine.EndPoint target) {
        long newDelayInMs;
        long now = android.os.SystemClock.elapsedRealtime();
        android.util.Pair<java.lang.Long, java.lang.Long> previousSettings = this.mSyncStorageEngine.getBackoff(target);
        long newDelayInMs2 = -1;
        if (previousSettings != null) {
            if (now >= ((java.lang.Long) previousSettings.first).longValue()) {
                newDelayInMs2 = (long) (((java.lang.Long) previousSettings.second).longValue() * this.mConstants.getRetryTimeIncreaseFactor());
            } else {
                if (android.util.Log.isLoggable("SyncManager", 2)) {
                    android.util.Slog.v("SyncManager", "Still in backoff, do not increase it. Remaining: " + ((((java.lang.Long) previousSettings.first).longValue() - now) / 1000) + " seconds.");
                    return;
                }
                return;
            }
        }
        if (newDelayInMs2 <= 0) {
            long initialRetryMs = this.mConstants.getInitialSyncRetryTimeInSeconds() * 1000;
            newDelayInMs2 = jitterize(initialRetryMs, (long) (initialRetryMs * 1.1d));
        }
        long maxSyncRetryTimeInSeconds = this.mConstants.getMaxSyncRetryTimeInSeconds();
        if (newDelayInMs2 <= maxSyncRetryTimeInSeconds * 1000) {
            newDelayInMs = newDelayInMs2;
        } else {
            long newDelayInMs3 = maxSyncRetryTimeInSeconds * 1000;
            newDelayInMs = newDelayInMs3;
        }
        long backoff = now + newDelayInMs;
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "Backoff until: " + backoff + ", delayTime: " + newDelayInMs);
        }
        this.mSyncStorageEngine.setBackoff(target, backoff, newDelayInMs);
        rescheduleSyncs(target, "increaseBackoffSetting");
    }

    private void rescheduleSyncs(com.android.server.content.SyncStorageEngine.EndPoint target, java.lang.String why) {
        this.mLogger.log("rescheduleSyncs() ep=", target, " why=", why);
        java.util.List<com.android.server.content.SyncOperation> ops = getAllPendingSyncs();
        int count = 0;
        for (com.android.server.content.SyncOperation op : ops) {
            if (!op.isPeriodic && op.target.matchesSpec(target)) {
                count++;
                cancelJob(op, why);
                postScheduleSyncMessage(op, 0L);
            }
        }
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "Rescheduled " + count + " syncs for " + target);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDelayUntilTime(com.android.server.content.SyncStorageEngine.EndPoint target, long delayUntilSeconds) {
        long newDelayUntilTime;
        long delayUntil = 1000 * delayUntilSeconds;
        long absoluteNow = java.lang.System.currentTimeMillis();
        if (delayUntil > absoluteNow) {
            newDelayUntilTime = android.os.SystemClock.elapsedRealtime() + (delayUntil - absoluteNow);
        } else {
            newDelayUntilTime = 0;
        }
        this.mSyncStorageEngine.setDelayUntilTime(target, newDelayUntilTime);
        if (android.util.Log.isLoggable("SyncManager", 2)) {
            android.util.Slog.v("SyncManager", "Delay Until time set to " + newDelayUntilTime + " for " + target);
        }
        rescheduleSyncs(target, "delayUntil newDelayUntilTime: " + newDelayUntilTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAdapterDelayed(com.android.server.content.SyncStorageEngine.EndPoint target) {
        long now = android.os.SystemClock.elapsedRealtime();
        android.util.Pair<java.lang.Long, java.lang.Long> backoff = this.mSyncStorageEngine.getBackoff(target);
        if ((backoff != null && ((java.lang.Long) backoff.first).longValue() != -1 && ((java.lang.Long) backoff.first).longValue() > now) || this.mSyncStorageEngine.getDelayUntilTime(target) > now) {
            return true;
        }
        return false;
    }

    public void cancelActiveSync(com.android.server.content.SyncStorageEngine.EndPoint info, android.os.Bundle extras, java.lang.String why) {
        sendCancelSyncsMessage(info, extras, why);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleSyncOperationH(com.android.server.content.SyncOperation syncOperation) {
        scheduleSyncOperationH(syncOperation, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleSyncOperationH(com.android.server.content.SyncOperation syncOperation, long minDelay) {
        long minDelay2;
        boolean isLoggable;
        boolean z;
        com.android.server.DeviceIdleInternal dic;
        java.lang.String str;
        boolean isLoggable2;
        long now;
        long delayUntilDelay;
        boolean isLoggable3 = android.util.Log.isLoggable("SyncManager", 2);
        if (syncOperation == null) {
            android.util.Slog.e("SyncManager", "Can't schedule null sync operation.");
            return;
        }
        if (syncOperation.hasIgnoreBackoff()) {
            minDelay2 = minDelay;
        } else {
            android.util.Pair<java.lang.Long, java.lang.Long> backoff = this.mSyncStorageEngine.getBackoff(syncOperation.target);
            if (backoff == null) {
                android.util.Slog.e("SyncManager", "Couldn't find backoff values for " + com.android.server.content.SyncLogger.logSafe(syncOperation.target));
                backoff = new android.util.Pair<>(-1L, -1L);
            } else if (((java.lang.Long) backoff.first).longValue() != -1) {
                syncOperation.scheduleEjAsRegularJob = true;
            }
            long now2 = android.os.SystemClock.elapsedRealtime();
            long backoffDelay = ((java.lang.Long) backoff.first).longValue() == -1 ? 0L : ((java.lang.Long) backoff.first).longValue() - now2;
            long delayUntil = this.mSyncStorageEngine.getDelayUntilTime(syncOperation.target);
            long delayUntilDelay2 = delayUntil > now2 ? delayUntil - now2 : 0L;
            if (!isLoggable3) {
                delayUntilDelay = delayUntilDelay2;
            } else {
                delayUntilDelay = delayUntilDelay2;
                android.util.Slog.v("SyncManager", "backoff delay:" + backoffDelay + " delayUntil delay:" + delayUntilDelay);
            }
            minDelay2 = java.lang.Math.max(minDelay, java.lang.Math.max(backoffDelay, delayUntilDelay));
        }
        if (minDelay2 < 0) {
            minDelay2 = 0;
        } else if (minDelay2 > 0) {
            syncOperation.scheduleEjAsRegularJob = true;
        }
        if (syncOperation.isPeriodic) {
            isLoggable = isLoggable3;
        } else {
            int inheritedSyncExemptionFlag = 0;
            for (com.android.server.content.SyncManager.ActiveSyncContext asc : this.mActiveSyncContexts) {
                if (asc.mSyncOperation.key.equals(syncOperation.key)) {
                    if (isLoggable3) {
                        android.util.Log.v("SyncManager", "Duplicate sync is already running. Not scheduling " + syncOperation);
                        return;
                    }
                    return;
                }
            }
            int duplicatesCount = 0;
            long now3 = android.os.SystemClock.elapsedRealtime();
            syncOperation.expectedRuntime = now3 + minDelay2;
            java.util.List<com.android.server.content.SyncOperation> pending = getAllPendingSyncs();
            com.android.server.content.SyncOperation syncToRun = syncOperation;
            for (com.android.server.content.SyncOperation op : pending) {
                if (!op.isPeriodic) {
                    if (!op.key.equals(syncOperation.key)) {
                        isLoggable2 = isLoggable3;
                        now = now3;
                    } else {
                        now = now3;
                        isLoggable2 = isLoggable3;
                        if (syncToRun.expectedRuntime > op.expectedRuntime) {
                            syncToRun = op;
                        }
                        duplicatesCount++;
                    }
                    isLoggable3 = isLoggable2;
                    now3 = now;
                }
            }
            isLoggable = isLoggable3;
            if (duplicatesCount > 1) {
                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("duplicates found when scheduling a sync operation: owningUid=").append(syncOperation.owningUid).append("; owningPackage=").append(syncOperation.owningPackage).append("; source=").append(syncOperation.syncSource).append("; adapter=");
                if (syncOperation.target != null) {
                    str = syncOperation.target.provider;
                } else {
                    str = "unknown";
                }
                android.util.Slog.wtf("SyncManager", sbAppend.append(str).toString());
            }
            if (syncOperation != syncToRun && minDelay2 == 0 && syncToRun.syncExemptionFlag < syncOperation.syncExemptionFlag) {
                syncToRun = syncOperation;
                inheritedSyncExemptionFlag = java.lang.Math.max(0, syncToRun.syncExemptionFlag);
            }
            for (com.android.server.content.SyncOperation op2 : pending) {
                if (!op2.isPeriodic && op2.key.equals(syncOperation.key) && op2 != syncToRun) {
                    if (isLoggable) {
                        android.util.Slog.v("SyncManager", "Cancelling duplicate sync " + op2);
                    }
                    inheritedSyncExemptionFlag = java.lang.Math.max(inheritedSyncExemptionFlag, op2.syncExemptionFlag);
                    cancelJob(op2, "scheduleSyncOperationH-duplicate");
                }
            }
            if (syncToRun != syncOperation) {
                if (isLoggable) {
                    android.util.Slog.v("SyncManager", "Not scheduling because a duplicate exists.");
                    return;
                }
                return;
            } else if (inheritedSyncExemptionFlag > 0) {
                syncOperation.syncExemptionFlag = inheritedSyncExemptionFlag;
            }
        }
        if (syncOperation.jobId == -1) {
            syncOperation.jobId = getUnusedJobIdH();
        }
        if (isLoggable) {
            android.util.Slog.v("SyncManager", "scheduling sync operation " + syncOperation.toString());
        }
        int bias = syncOperation.getJobBias();
        int networkType = syncOperation.isNotAllowedOnMetered() ? 2 : 1;
        int jobFlags = syncOperation.isAppStandbyExempted() ? 8 : 0;
        android.app.job.JobInfo.Builder b = new android.app.job.JobInfo.Builder(syncOperation.jobId, new android.content.ComponentName(this.mContext, (java.lang.Class<?>) com.android.server.content.SyncJobService.class)).setExtras(syncOperation.toJobInfoExtras()).setRequiredNetworkType(networkType).setRequiresStorageNotLow(true).setPersisted(true).setBias(bias).setFlags(jobFlags);
        if (syncOperation.isPeriodic) {
            b.setPeriodic(syncOperation.periodMillis, syncOperation.flexMillis);
            z = true;
        } else {
            if (minDelay2 > 0) {
                b.setMinimumLatency(minDelay2);
            }
            z = true;
            getSyncStorageEngine().markPending(syncOperation.target, true);
        }
        if (syncOperation.hasRequireCharging()) {
            b.setRequiresCharging(z);
        }
        if (syncOperation.isScheduledAsExpeditedJob() && !syncOperation.scheduleEjAsRegularJob) {
            b.setExpedited(z);
        }
        if (syncOperation.syncExemptionFlag == 2 && (dic = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class)) != null) {
            dic.addPowerSaveTempWhitelistApp(1000, syncOperation.owningPackage, this.mConstants.getKeyExemptionTempWhitelistDurationInSeconds() * 1000, 1, android.os.UserHandle.getUserId(syncOperation.owningUid), false, 306, "sync by top app");
        }
        android.app.usage.UsageStatsManagerInternal usmi = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        if (usmi != null) {
            usmi.reportSyncScheduled(syncOperation.owningPackage, android.os.UserHandle.getUserId(syncOperation.owningUid), syncOperation.isAppStandbyExempted());
        }
        android.app.job.JobInfo ji = b.build();
        int result = getJobScheduler().scheduleAsPackage(ji, syncOperation.owningPackage, syncOperation.target.userId, syncOperation.wakeLockName());
        if (result == 0 && ji.isExpedited()) {
            if (isLoggable) {
                android.util.Slog.i("SyncManager", "Failed to schedule EJ for " + syncOperation.owningPackage + ". Downgrading to regular");
            }
            syncOperation.scheduleEjAsRegularJob = true;
            b.setExpedited(false).setExtras(syncOperation.toJobInfoExtras());
            result = getJobScheduler().scheduleAsPackage(b.build(), syncOperation.owningPackage, syncOperation.target.userId, syncOperation.wakeLockName());
        }
        if (result == 0) {
            android.util.Slog.e("SyncManager", "Failed to schedule job for " + syncOperation.owningPackage);
        }
    }

    public void clearScheduledSyncOperations(com.android.server.content.SyncStorageEngine.EndPoint info) {
        java.util.List<com.android.server.content.SyncOperation> ops = getAllPendingSyncs();
        for (com.android.server.content.SyncOperation op : ops) {
            if (!op.isPeriodic && op.target.matchesSpec(info)) {
                cancelJob(op, "clearScheduledSyncOperations");
                getSyncStorageEngine().markPending(op.target, false);
            }
        }
        this.mSyncStorageEngine.setBackoff(info, -1L, -1L);
    }

    public void cancelScheduledSyncOperation(com.android.server.content.SyncStorageEngine.EndPoint info, android.os.Bundle extras) {
        java.util.List<com.android.server.content.SyncOperation> ops = getAllPendingSyncs();
        for (com.android.server.content.SyncOperation op : ops) {
            if (!op.isPeriodic && op.target.matchesSpec(info) && op.areExtrasEqual(extras, false)) {
                cancelJob(op, "cancelScheduledSyncOperation");
            }
        }
        setAuthorityPendingState(info);
        if (!this.mSyncStorageEngine.isSyncPending(info)) {
            this.mSyncStorageEngine.setBackoff(info, -1L, -1L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeRescheduleSync(android.content.SyncResult syncResult, com.android.server.content.SyncOperation operation) {
        boolean isLoggable = android.util.Log.isLoggable("SyncManager", 3);
        if (isLoggable) {
            android.util.Log.d("SyncManager", "encountered error(s) during the sync: " + syncResult + ", " + operation);
        }
        operation.enableBackoff();
        operation.scheduleEjAsRegularJob = true;
        if (operation.hasDoNotRetry() && !syncResult.syncAlreadyInProgress) {
            if (isLoggable) {
                android.util.Log.d("SyncManager", "not retrying sync operation because SYNC_EXTRAS_DO_NOT_RETRY was specified " + operation);
                return;
            }
            return;
        }
        if (operation.isUpload() && !syncResult.syncAlreadyInProgress) {
            operation.enableTwoWaySync();
            if (isLoggable) {
                android.util.Log.d("SyncManager", "retrying sync operation as a two-way sync because an upload-only sync encountered an error: " + operation);
            }
            scheduleSyncOperationH(operation);
            return;
        }
        if (syncResult.tooManyRetries) {
            if (isLoggable) {
                android.util.Log.d("SyncManager", "not retrying sync operation because it retried too many times: " + operation);
                return;
            }
            return;
        }
        if (syncResult.madeSomeProgress()) {
            if (isLoggable) {
                android.util.Log.d("SyncManager", "retrying sync operation because even though it had an error it achieved some success");
            }
            scheduleSyncOperationH(operation);
        } else if (syncResult.syncAlreadyInProgress) {
            if (isLoggable) {
                android.util.Log.d("SyncManager", "retrying sync operation that failed because there was already a sync in progress: " + operation);
            }
            scheduleSyncOperationH(operation, 10000L);
        } else {
            if (syncResult.hasSoftError()) {
                if (isLoggable) {
                    android.util.Log.d("SyncManager", "retrying sync operation because it encountered a soft error: " + operation);
                }
                scheduleSyncOperationH(operation);
                return;
            }
            android.util.Log.e("SyncManager", "not retrying sync operation because the error is a hard error: " + com.android.server.content.SyncLogger.logSafe(operation));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserUnlocked(int userId) throws java.lang.Throwable {
        com.android.server.accounts.AccountManagerService.getSingleton().validateAccounts(userId);
        this.mSyncAdapters.invalidateCache(userId);
        com.android.server.content.SyncStorageEngine.EndPoint target = new com.android.server.content.SyncStorageEngine.EndPoint(null, null, userId);
        updateRunningAccounts(target);
        android.accounts.Account[] accounts = com.android.server.accounts.AccountManagerService.getSingleton().getAccounts(userId, this.mContext.getOpPackageName());
        for (android.accounts.Account account : accounts) {
            scheduleSync(account, userId, -8, null, null, -1, 0, android.os.Process.myUid(), -3, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStopped(int userId) {
        updateRunningAccounts(null);
        cancelActiveSync(new com.android.server.content.SyncStorageEngine.EndPoint(null, null, userId), null, "onUserStopped");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserRemoved(int userId) {
        this.mLogger.log("onUserRemoved: u", java.lang.Integer.valueOf(userId));
        updateRunningAccounts(null);
        this.mSyncStorageEngine.removeStaleAccounts(null, userId);
        java.util.List<com.android.server.content.SyncOperation> ops = getAllPendingSyncs();
        for (com.android.server.content.SyncOperation op : ops) {
            if (op.target.userId == userId) {
                cancelJob(op, "user removed u" + userId);
            }
        }
    }

    static android.content.Intent getAdapterBindIntent(android.content.Context context, android.content.ComponentName syncAdapterComponent, int userId) {
        android.content.Intent intent = new android.content.Intent();
        intent.setAction("android.content.SyncAdapter");
        intent.setComponent(syncAdapterComponent);
        intent.putExtra("android.intent.extra.client_label", android.R.string.ssl_ca_cert_noti_by_administrator);
        intent.putExtra("android.intent.extra.client_intent", android.app.PendingIntent.getActivityAsUser(context, 0, new android.content.Intent("android.settings.SYNC_SETTINGS"), 67108864, null, android.os.UserHandle.of(userId)));
        return intent;
    }

    class ActiveSyncContext extends android.content.ISyncContext.Stub implements android.content.ServiceConnection, android.os.IBinder.DeathRecipient {
        boolean mBound;
        long mBytesTransferredAtLastPoll;
        java.lang.String mEventName;
        final long mHistoryRowId;
        long mLastPolledTimeElapsed;
        final int mSyncAdapterUid;
        android.content.SyncInfo mSyncInfo;
        final com.android.server.content.SyncOperation mSyncOperation;
        final android.os.PowerManager.WakeLock mSyncWakeLock;
        boolean mIsLinkedToDeath = false;
        android.content.ISyncAdapter mSyncAdapter = null;
        final long mStartTime = android.os.SystemClock.elapsedRealtime();
        long mTimeoutStartTime = this.mStartTime;

        public ActiveSyncContext(com.android.server.content.SyncOperation syncOperation, long historyRowId, int syncAdapterUid) {
            this.mSyncAdapterUid = syncAdapterUid;
            this.mSyncOperation = syncOperation;
            this.mHistoryRowId = historyRowId;
            this.mSyncWakeLock = com.android.server.content.SyncManager.this.mSyncHandler.getSyncWakeLock(this.mSyncOperation);
            this.mSyncWakeLock.setWorkSource(new android.os.WorkSource(syncAdapterUid));
            this.mSyncWakeLock.acquire();
        }

        public void sendHeartbeat() {
        }

        public void onFinished(android.content.SyncResult result) {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "onFinished: " + this);
            }
            com.android.server.content.SyncManager.this.mLogger.log("onFinished result=", result, " endpoint=", this.mSyncOperation == null ? "null" : this.mSyncOperation.target);
            com.android.server.content.SyncManager.this.sendSyncFinishedOrCanceledMessage(this, result);
        }

        public void toString(java.lang.StringBuilder sb, boolean z) {
            java.lang.StringBuilder sbAppend = sb.append("startTime ").append(this.mStartTime).append(", mTimeoutStartTime ").append(this.mTimeoutStartTime).append(", mHistoryRowId ").append(this.mHistoryRowId).append(", syncOperation ");
            com.android.server.content.SyncOperation syncOperation = this.mSyncOperation;
            java.lang.Object objLogSafe = syncOperation;
            if (z) {
                objLogSafe = com.android.server.content.SyncLogger.logSafe(syncOperation);
            }
            sbAppend.append(objLogSafe);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            android.os.Message msg = com.android.server.content.SyncManager.this.mSyncHandler.obtainMessage();
            msg.what = 4;
            msg.obj = com.android.server.content.SyncManager.this.new ServiceConnectionData(this, service);
            com.android.server.content.SyncManager.this.mSyncHandler.sendMessage(msg);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.os.Message msg = com.android.server.content.SyncManager.this.mSyncHandler.obtainMessage();
            msg.what = 5;
            msg.obj = com.android.server.content.SyncManager.this.new ServiceConnectionData(this, null);
            com.android.server.content.SyncManager.this.mSyncHandler.sendMessage(msg);
        }

        boolean bindToSyncAdapter(android.content.ComponentName serviceComponent, int userId) {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Log.d("SyncManager", "bindToSyncAdapter: " + serviceComponent + ", connection " + this);
            }
            android.content.Intent intent = com.android.server.content.SyncManager.getAdapterBindIntent(com.android.server.content.SyncManager.this.mContext, serviceComponent, userId);
            this.mBound = true;
            boolean bindResult = com.android.server.content.SyncManager.this.mContext.bindServiceAsUser(intent, this, com.android.server.content.SyncManager.SYNC_ADAPTER_CONNECTION_FLAGS, new android.os.UserHandle(this.mSyncOperation.target.userId));
            com.android.server.content.SyncManager.this.mLogger.log("bindService() returned=", java.lang.Boolean.valueOf(this.mBound), " for ", this);
            if (!bindResult) {
                this.mBound = false;
            } else {
                try {
                    this.mEventName = this.mSyncOperation.wakeLockName();
                    com.android.server.content.SyncManager.this.mBatteryStats.noteSyncStart(this.mEventName, this.mSyncAdapterUid);
                } catch (android.os.RemoteException e) {
                }
            }
            return bindResult;
        }

        protected void close() {
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Log.d("SyncManager", "unBindFromSyncAdapter: connection " + this);
            }
            if (this.mBound) {
                this.mBound = false;
                com.android.server.content.SyncManager.this.mLogger.log("unbindService for ", this);
                try {
                    com.android.server.content.SyncManager.this.mContext.unbindService(this);
                } catch (java.util.NoSuchElementException e) {
                }
                try {
                    com.android.server.content.SyncManager.this.mBatteryStats.noteSyncFinish(this.mEventName, this.mSyncAdapterUid);
                } catch (android.os.RemoteException e2) {
                }
            }
            this.mSyncWakeLock.release();
            this.mSyncWakeLock.setWorkSource(null);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            toString(sb, false);
            return sb.toString();
        }

        public java.lang.String toSafeString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            toString(sb, true);
            return sb.toString();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.content.SyncManager.this.sendSyncFinishedOrCanceledMessage(this, null);
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, boolean dumpAll) throws java.lang.Throwable {
        this.mSyncManagerExt.dump(fd, pw, dumpAll);
        com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
        com.android.server.content.SyncAdapterStateFetcher buckets = new com.android.server.content.SyncAdapterStateFetcher();
        dumpSyncState(ipw, buckets);
        this.mConstants.dump(pw, "");
        dumpSyncAdapters(ipw);
        if (dumpAll) {
            ipw.println("Detailed Sync History");
            this.mLogger.dumpAll(pw);
        }
    }

    static java.lang.String formatTime(long time) {
        if (time == 0) {
            return "N/A";
        }
        return android.text.format.TimeMigrationUtils.formatMillisWithFixedFormat(time);
    }

    static /* synthetic */ int lambda$static$6(com.android.server.content.SyncOperation op1, com.android.server.content.SyncOperation op2) {
        int res = java.lang.Integer.compare(op1.target.userId, op2.target.userId);
        if (res != 0) {
            return res;
        }
        java.util.Comparator<java.lang.String> stringComparator = java.lang.String.CASE_INSENSITIVE_ORDER;
        int res2 = stringComparator.compare(op1.target.account.type, op2.target.account.type);
        if (res2 != 0) {
            return res2;
        }
        int res3 = stringComparator.compare(op1.target.account.name, op2.target.account.name);
        if (res3 != 0) {
            return res3;
        }
        int res4 = stringComparator.compare(op1.target.provider, op2.target.provider);
        if (res4 != 0) {
            return res4;
        }
        int res5 = java.lang.Integer.compare(op1.reason, op2.reason);
        if (res5 != 0) {
            return res5;
        }
        int res6 = java.lang.Long.compare(op1.periodMillis, op2.periodMillis);
        if (res6 != 0) {
            return res6;
        }
        int res7 = java.lang.Long.compare(op1.expectedRuntime, op2.expectedRuntime);
        if (res7 != 0) {
            return res7;
        }
        int res8 = java.lang.Long.compare(op1.jobId, op2.jobId);
        if (res8 != 0) {
            return res8;
        }
        return 0;
    }

    static /* synthetic */ int lambda$static$7(com.android.server.content.SyncOperation op1, com.android.server.content.SyncOperation op2) {
        int res = java.lang.Long.compare(op1.expectedRuntime, op2.expectedRuntime);
        return res != 0 ? res : sOpDumpComparator.compare(op1, op2);
    }

    private static <T> int countIf(java.util.Collection<T> col, java.util.function.Predicate<T> p) {
        int ret = 0;
        for (T item : col) {
            if (p.test(item)) {
                ret++;
            }
        }
        return ret;
    }

    protected void dumpPendingSyncs(java.io.PrintWriter pw, com.android.server.content.SyncAdapterStateFetcher buckets) {
        java.util.List<com.android.server.content.SyncOperation> pendingSyncs = getAllPendingSyncs();
        pw.print("Pending Syncs: ");
        pw.println(countIf(pendingSyncs, new java.util.function.Predicate() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.content.SyncManager.lambda$dumpPendingSyncs$8((com.android.server.content.SyncOperation) obj);
            }
        }));
        java.util.Collections.sort(pendingSyncs, sOpRuntimeComparator);
        int count = 0;
        for (com.android.server.content.SyncOperation op : pendingSyncs) {
            if (!op.isPeriodic) {
                pw.println(op.dump(null, false, buckets, false));
                count++;
            }
        }
        pw.println();
    }

    static /* synthetic */ boolean lambda$dumpPendingSyncs$8(com.android.server.content.SyncOperation op) {
        return !op.isPeriodic;
    }

    protected void dumpPeriodicSyncs(java.io.PrintWriter pw, com.android.server.content.SyncAdapterStateFetcher buckets) {
        java.util.List<com.android.server.content.SyncOperation> pendingSyncs = getAllPendingSyncs();
        pw.print("Periodic Syncs: ");
        pw.println(countIf(pendingSyncs, new java.util.function.Predicate() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.content.SyncOperation) obj).isPeriodic;
            }
        }));
        java.util.Collections.sort(pendingSyncs, sOpDumpComparator);
        int count = 0;
        for (com.android.server.content.SyncOperation op : pendingSyncs) {
            if (op.isPeriodic) {
                pw.println(op.dump(null, false, buckets, false));
                count++;
            }
        }
        pw.println();
    }

    public static java.lang.StringBuilder formatDurationHMS(java.lang.StringBuilder sb, long duration) {
        long duration2 = duration / 1000;
        if (duration2 < 0) {
            sb.append('-');
            duration2 = -duration2;
        }
        long seconds = duration2 % 60;
        long duration3 = duration2 / 60;
        long minutes = duration3 % 60;
        long duration4 = duration3 / 60;
        long hours = duration4 % 24;
        long duration5 = duration4 / 24;
        boolean print = false;
        if (duration5 > 0) {
            sb.append(duration5);
            sb.append('d');
            print = true;
        }
        if (!printTwoDigitNumber(sb, seconds, 's', printTwoDigitNumber(sb, minutes, 'm', printTwoDigitNumber(sb, hours, 'h', print)))) {
            sb.append("0s");
        }
        return sb;
    }

    private static boolean printTwoDigitNumber(java.lang.StringBuilder sb, long value, char unit, boolean always) {
        if (!always && value == 0) {
            return false;
        }
        if (always && value < 10) {
            sb.append('0');
        }
        sb.append(value);
        sb.append(unit);
        return true;
    }

    @dalvik.annotation.optimization.NeverCompile
    protected void dumpSyncState(java.io.PrintWriter pw, com.android.server.content.SyncAdapterStateFetcher buckets) throws java.lang.Throwable {
        int i;
        boolean unlocked;
        java.util.ArrayList<android.util.Pair<com.android.server.content.SyncStorageEngine.EndPoint, android.content.SyncStatusInfo>> statuses;
        final com.android.server.content.SyncManager syncManager = this;
        java.io.PrintWriter printWriter = pw;
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        printWriter.print("Data connected: ");
        printWriter.println(syncManager.mDataConnectionIsConnected);
        printWriter.print("Battery saver: ");
        printWriter.println(syncManager.mPowerManager != null && syncManager.mPowerManager.isPowerSaveMode());
        printWriter.print("Background network restriction: ");
        android.net.ConnectivityManager cm = getConnectivityManager();
        int status = cm == null ? -1 : cm.getRestrictBackgroundStatus();
        switch (status) {
            case 1:
                printWriter.println(" disabled");
                break;
            case 2:
                printWriter.println(" whitelisted");
                break;
            case 3:
                printWriter.println(" enabled");
                break;
            default:
                printWriter.print("Unknown(");
                printWriter.print(status);
                printWriter.println(")");
                break;
        }
        printWriter.print("Auto sync: ");
        java.util.List<android.content.pm.UserInfo> users = getAllUsers();
        if (users != null) {
            for (android.content.pm.UserInfo user : users) {
                printWriter.print("u" + user.id + "=" + syncManager.mSyncStorageEngine.getMasterSyncAutomatically(user.id) + " ");
            }
            pw.println();
        }
        android.content.Intent storageLowIntent = syncManager.mContext.registerReceiver(null, new android.content.IntentFilter("android.intent.action.DEVICE_STORAGE_LOW"));
        printWriter.print("Storage low: ");
        printWriter.println(storageLowIntent != null);
        printWriter.print("Clock valid: ");
        printWriter.println(syncManager.mSyncStorageEngine.isClockValid());
        android.accounts.AccountAndUser[] accounts = com.android.server.accounts.AccountManagerService.getSingleton().getAllAccountsForSystemProcess();
        printWriter.print("Accounts: ");
        if (accounts != INITIAL_ACCOUNTS_ARRAY) {
            printWriter.println(accounts.length);
        } else {
            printWriter.println("not known yet");
        }
        long now = android.os.SystemClock.elapsedRealtime();
        printWriter.print("Now: ");
        printWriter.print(now);
        printWriter.println(" (" + formatTime(java.lang.System.currentTimeMillis()) + ")");
        sb.setLength(0);
        printWriter.print("Uptime: ");
        printWriter.print(formatDurationHMS(sb, now));
        pw.println();
        printWriter.print("Time spent syncing: ");
        sb.setLength(0);
        printWriter.print(formatDurationHMS(sb, syncManager.mSyncHandler.mSyncTimeTracker.timeSpentSyncing()));
        printWriter.print(", sync ");
        printWriter.print(syncManager.mSyncHandler.mSyncTimeTracker.mLastWasSyncing ? "" : "not ");
        printWriter.println("in progress");
        pw.println();
        printWriter.println("Active Syncs: " + syncManager.mActiveSyncContexts.size());
        android.content.pm.PackageManager pm = syncManager.mContext.getPackageManager();
        for (com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext : syncManager.mActiveSyncContexts) {
            long durationInSeconds = now - activeSyncContext.mStartTime;
            printWriter.print("  ");
            sb.setLength(0);
            printWriter.print(formatDurationHMS(sb, durationInSeconds));
            printWriter.print(" - ");
            printWriter.print(activeSyncContext.mSyncOperation.dump(pm, false, buckets, false));
            pw.println();
        }
        pw.println();
        dumpPendingSyncs(pw, buckets);
        dumpPeriodicSyncs(pw, buckets);
        printWriter.println("Sync Status");
        java.util.ArrayList<android.util.Pair<com.android.server.content.SyncStorageEngine.EndPoint, android.content.SyncStatusInfo>> statuses2 = new java.util.ArrayList<>();
        syncManager.mSyncStorageEngine.resetTodayStats(false);
        int length = accounts.length;
        int i2 = 0;
        while (i2 < length) {
            android.accounts.AccountAndUser account = accounts[i2];
            synchronized (syncManager.mUnlockedUsers) {
                try {
                    i = length;
                    unlocked = syncManager.mUnlockedUsers.get(account.userId);
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
            android.accounts.AccountAndUser[] accounts2 = accounts;
            java.util.List<android.content.pm.UserInfo> users2 = users;
            printWriter.printf("Account %s u%d %s%s\n", account.account.name, java.lang.Integer.valueOf(account.userId), account.account.type, unlocked ? "" : " (locked)");
            printWriter.println("=======================================================================");
            final com.android.server.content.SyncManager.PrintTable table = new com.android.server.content.SyncManager.PrintTable(16);
            table.set(0, 0, "Authority", "Syncable", "Enabled", "Stats", "Loc", "Poll", "Per", "Feed", "User", "Othr", "Tot", "Fail", "Can", "Time", "Last Sync", "Backoff");
            java.util.List<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType>> sorted = com.google.android.collect.Lists.newArrayList();
            sorted.addAll(syncManager.mSyncAdapters.getAllServices(account.userId));
            java.util.Collections.sort(sorted, new java.util.Comparator<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType>>() { // from class: com.android.server.content.SyncManager.12
                @Override // java.util.Comparator
                public int compare(android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> lhs, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> rhs) {
                    return ((android.content.SyncAdapterType) lhs.type).authority.compareTo(((android.content.SyncAdapterType) rhs.type).authority);
                }
            });
            java.util.Iterator<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType>> it = sorted.iterator();
            while (it.hasNext()) {
                android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterType = it.next();
                java.util.List<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType>> sorted2 = sorted;
                if (!((android.content.SyncAdapterType) syncAdapterType.type).accountType.equals(account.account.type)) {
                    sorted = sorted2;
                } else {
                    int row = table.getNumRows();
                    java.util.Iterator<android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType>> it2 = it;
                    android.content.Intent storageLowIntent2 = storageLowIntent;
                    android.content.pm.PackageManager pm2 = pm;
                    android.util.Pair<com.android.server.content.SyncStorageEngine.AuthorityInfo, android.content.SyncStatusInfo> syncAuthoritySyncStatus = syncManager.mSyncStorageEngine.getCopyOfAuthorityWithSyncStatus(new com.android.server.content.SyncStorageEngine.EndPoint(account.account, ((android.content.SyncAdapterType) syncAdapterType.type).authority, account.userId));
                    com.android.server.content.SyncStorageEngine.AuthorityInfo settings = (com.android.server.content.SyncStorageEngine.AuthorityInfo) syncAuthoritySyncStatus.first;
                    android.content.SyncStatusInfo status2 = (android.content.SyncStatusInfo) syncAuthoritySyncStatus.second;
                    statuses2.add(android.util.Pair.create(settings.target, status2));
                    java.lang.String authority = settings.target.provider;
                    if (authority.length() > 50) {
                        authority = authority.substring(authority.length() - 50);
                    }
                    table.set(row, 0, authority, java.lang.Integer.valueOf(settings.syncable), java.lang.Boolean.valueOf(settings.enabled));
                    com.android.internal.util.function.QuadConsumer<java.lang.String, android.content.SyncStatusInfo.Stats, java.util.function.Function<java.lang.Integer, java.lang.String>, java.lang.Integer> c = new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda11
                        public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                            com.android.server.content.SyncManager.lambda$dumpSyncState$10(sb, table, (java.lang.String) obj, (android.content.SyncStatusInfo.Stats) obj2, (java.util.function.Function) obj3, (java.lang.Integer) obj4);
                        }
                    };
                    java.lang.StringBuilder sb2 = sb;
                    android.accounts.AccountAndUser account2 = account;
                    c.accept("Total", status2.totalStats, new java.util.function.Function() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda12
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return java.lang.Integer.toString(((java.lang.Integer) obj).intValue());
                        }
                    }, java.lang.Integer.valueOf(row));
                    c.accept("Today", status2.todayStats, new java.util.function.Function() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda13
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return this.f$0.zeroToEmpty(((java.lang.Integer) obj).intValue());
                        }
                    }, java.lang.Integer.valueOf(row + 1));
                    c.accept("Yestr", status2.yesterdayStats, new java.util.function.Function() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda13
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return this.f$0.zeroToEmpty(((java.lang.Integer) obj).intValue());
                        }
                    }, java.lang.Integer.valueOf(row + 2));
                    if (settings.delayUntil > now) {
                        int row1 = row + 1;
                        statuses = statuses2;
                        table.set(row, 15, "D: " + ((settings.delayUntil - now) / 1000));
                        if (settings.backoffTime > now) {
                            int row12 = row1 + 1;
                            table.set(row1, 15, "B: " + ((settings.backoffTime - now) / 1000));
                            int i3 = row12 + 1;
                            table.set(row12, 15, java.lang.Long.valueOf(settings.backoffDelay / 1000));
                        }
                    } else {
                        statuses = statuses2;
                    }
                    int row13 = row;
                    if (status2.lastSuccessTime != 0) {
                        int row14 = row13 + 1;
                        table.set(row13, 14, com.android.server.content.SyncStorageEngine.SOURCES[status2.lastSuccessSource] + " SUCCESS");
                        row13 = row14 + 1;
                        table.set(row14, 14, formatTime(status2.lastSuccessTime));
                    }
                    if (status2.lastFailureTime != 0) {
                        int row15 = row13 + 1;
                        table.set(row13, 14, com.android.server.content.SyncStorageEngine.SOURCES[status2.lastFailureSource] + " FAILURE");
                        int row16 = row15 + 1;
                        table.set(row15, 14, formatTime(status2.lastFailureTime));
                        int i4 = row16 + 1;
                        table.set(row16, 14, status2.lastFailureMesg);
                    }
                    syncManager = this;
                    statuses2 = statuses;
                    sorted = sorted2;
                    it = it2;
                    storageLowIntent = storageLowIntent2;
                    pm = pm2;
                    sb = sb2;
                    account = account2;
                }
            }
            table.writeTo(pw);
            i2++;
            printWriter = pw;
            length = i;
            accounts = accounts2;
            users = users2;
            syncManager = this;
        }
        java.io.PrintWriter printWriter2 = printWriter;
        java.util.ArrayList<android.util.Pair<com.android.server.content.SyncStorageEngine.EndPoint, android.content.SyncStatusInfo>> statuses3 = statuses2;
        dumpSyncHistory(pw);
        pw.println();
        printWriter2.println("Per Adapter History");
        printWriter2.println("(SERVER is now split up to FEED and OTHER)");
        int i5 = 0;
        while (i5 < statuses3.size()) {
            java.util.ArrayList<android.util.Pair<com.android.server.content.SyncStorageEngine.EndPoint, android.content.SyncStatusInfo>> statuses4 = statuses3;
            android.util.Pair<com.android.server.content.SyncStorageEngine.EndPoint, android.content.SyncStatusInfo> event = statuses4.get(i5);
            printWriter2.print("  ");
            printWriter2.print(((com.android.server.content.SyncStorageEngine.EndPoint) event.first).account.name);
            printWriter2.print('/');
            printWriter2.print(((com.android.server.content.SyncStorageEngine.EndPoint) event.first).account.type);
            printWriter2.print(" u");
            printWriter2.print(((com.android.server.content.SyncStorageEngine.EndPoint) event.first).userId);
            printWriter2.print(" [");
            printWriter2.print(((com.android.server.content.SyncStorageEngine.EndPoint) event.first).provider);
            printWriter2.print("]");
            pw.println();
            printWriter2.println("    Per source last syncs:");
            for (int j = 0; j < com.android.server.content.SyncStorageEngine.SOURCES.length; j++) {
                printWriter2.print("      ");
                printWriter2.print(java.lang.String.format("%8s", com.android.server.content.SyncStorageEngine.SOURCES[j]));
                printWriter2.print("  Success: ");
                printWriter2.print(formatTime(((android.content.SyncStatusInfo) event.second).perSourceLastSuccessTimes[j]));
                printWriter2.print("  Failure: ");
                printWriter2.println(formatTime(((android.content.SyncStatusInfo) event.second).perSourceLastFailureTimes[j]));
            }
            printWriter2.println("    Last syncs:");
            for (int j2 = 0; j2 < ((android.content.SyncStatusInfo) event.second).getEventCount(); j2++) {
                printWriter2.print("      ");
                printWriter2.print(formatTime(((android.content.SyncStatusInfo) event.second).getEventTime(j2)));
                printWriter2.print(' ');
                printWriter2.print(((android.content.SyncStatusInfo) event.second).getEvent(j2));
                pw.println();
            }
            if (((android.content.SyncStatusInfo) event.second).getEventCount() == 0) {
                printWriter2.println("      N/A");
            }
            i5++;
            statuses3 = statuses4;
        }
    }

    static /* synthetic */ void lambda$dumpSyncState$10(java.lang.StringBuilder sb, com.android.server.content.SyncManager.PrintTable table, java.lang.String label, android.content.SyncStatusInfo.Stats stats, java.util.function.Function filter, java.lang.Integer r) {
        sb.setLength(0);
        table.set(r.intValue(), 3, label, filter.apply(java.lang.Integer.valueOf(stats.numSourceLocal)), filter.apply(java.lang.Integer.valueOf(stats.numSourcePoll)), filter.apply(java.lang.Integer.valueOf(stats.numSourcePeriodic)), filter.apply(java.lang.Integer.valueOf(stats.numSourceFeed)), filter.apply(java.lang.Integer.valueOf(stats.numSourceUser)), filter.apply(java.lang.Integer.valueOf(stats.numSourceOther)), filter.apply(java.lang.Integer.valueOf(stats.numSyncs)), filter.apply(java.lang.Integer.valueOf(stats.numFailures)), filter.apply(java.lang.Integer.valueOf(stats.numCancels)), formatDurationHMS(sb, stats.totalElapsedTime));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String zeroToEmpty(int value) {
        return value != 0 ? java.lang.Integer.toString(value) : "";
    }

    private void dumpTimeSec(java.io.PrintWriter pw, long time) {
        pw.print(time / 1000);
        pw.print('.');
        pw.print((time / 100) % 10);
        pw.print('s');
    }

    private void dumpDayStatistic(java.io.PrintWriter pw, com.android.server.content.SyncStorageEngine.DayStats ds) {
        pw.print("Success (");
        pw.print(ds.successCount);
        if (ds.successCount > 0) {
            pw.print(" for ");
            dumpTimeSec(pw, ds.successTime);
            pw.print(" avg=");
            dumpTimeSec(pw, ds.successTime / ((long) ds.successCount));
        }
        pw.print(") Failure (");
        pw.print(ds.failureCount);
        if (ds.failureCount > 0) {
            pw.print(" for ");
            dumpTimeSec(pw, ds.failureTime);
            pw.print(" avg=");
            dumpTimeSec(pw, ds.failureTime / ((long) ds.failureCount));
        }
        pw.println(")");
    }

    protected void dumpSyncHistory(java.io.PrintWriter pw) {
        dumpRecentHistory(pw);
        dumpDayStatistics(pw);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void dumpRecentHistory(java.io.PrintWriter printWriter) {
        java.lang.String str;
        java.lang.String str2;
        int i;
        int i2;
        int i3;
        java.lang.String str3;
        java.lang.String str4;
        int i4;
        android.content.pm.PackageManager packageManager;
        int i5;
        java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> arrayList;
        java.lang.String str5;
        java.lang.String str6;
        java.lang.String str7;
        java.lang.String str8;
        long j;
        int i6;
        int i7;
        java.lang.String str9;
        java.lang.String str10;
        java.lang.String str11;
        java.lang.String str12;
        java.lang.String str13;
        int i8;
        java.lang.String str14;
        java.lang.String strValueOf;
        android.content.pm.PackageManager packageManager2;
        java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> arrayList2;
        java.lang.String str15;
        java.lang.String str16;
        com.android.server.content.SyncManager.AuthoritySyncStats authoritySyncStats;
        com.android.server.content.SyncManager syncManager = this;
        java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> syncHistory = syncManager.mSyncStorageEngine.getSyncHistory();
        if (syncHistory != null && syncHistory.size() > 0) {
            java.util.HashMap mapNewHashMap = com.google.android.collect.Maps.newHashMap();
            long j2 = 0;
            long j3 = 0;
            int size = syncHistory.size();
            int i9 = 0;
            int i10 = 0;
            java.util.Iterator<com.android.server.content.SyncStorageEngine.SyncHistoryItem> it = syncHistory.iterator();
            while (true) {
                boolean zHasNext = it.hasNext();
                str = " u";
                str2 = com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER;
                if (!zHasNext) {
                    break;
                }
                com.android.server.content.SyncStorageEngine.SyncHistoryItem next = it.next();
                java.util.Iterator<com.android.server.content.SyncStorageEngine.SyncHistoryItem> it2 = it;
                com.android.server.content.SyncStorageEngine.AuthorityInfo authority = syncManager.mSyncStorageEngine.getAuthority(next.authorityId);
                if (authority != null) {
                    java.lang.String str17 = authority.target.provider;
                    arrayList2 = syncHistory;
                    str16 = authority.target.account.name + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + authority.target.account.type + " u" + authority.target.userId;
                    str15 = str17;
                } else {
                    arrayList2 = syncHistory;
                    str15 = "Unknown";
                    str16 = "Unknown";
                }
                int length = str15.length();
                if (length > i9) {
                    i9 = length;
                }
                int length2 = str16.length();
                if (length2 > i10) {
                    i10 = length2;
                }
                int i11 = i9;
                int i12 = i10;
                long j4 = next.elapsedTime;
                long j5 = j2 + j4;
                long j6 = j3 + 1;
                com.android.server.content.SyncManager.AuthoritySyncStats authoritySyncStats2 = (com.android.server.content.SyncManager.AuthoritySyncStats) mapNewHashMap.get(str15);
                java.lang.Object[] objArr = 0;
                if (authoritySyncStats2 != null) {
                    authoritySyncStats = authoritySyncStats2;
                } else {
                    authoritySyncStats = new com.android.server.content.SyncManager.AuthoritySyncStats(str15);
                    mapNewHashMap.put(str15, authoritySyncStats);
                }
                authoritySyncStats.elapsedTime += j4;
                authoritySyncStats.times++;
                java.util.Map<java.lang.String, com.android.server.content.SyncManager.AccountSyncStats> map = authoritySyncStats.accountMap;
                com.android.server.content.SyncManager.AccountSyncStats accountSyncStats = map.get(str16);
                if (accountSyncStats == null) {
                    accountSyncStats = new com.android.server.content.SyncManager.AccountSyncStats(str16);
                    map.put(str16, accountSyncStats);
                }
                accountSyncStats.elapsedTime += j4;
                accountSyncStats.times++;
                i9 = i11;
                it = it2;
                i10 = i12;
                syncHistory = arrayList2;
                j2 = j5;
                j3 = j6;
            }
            java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> arrayList3 = syncHistory;
            if (j2 > 0) {
                printWriter.println();
                printWriter.printf("Detailed Statistics (Recent history):  %d (# of times) %ds (sync time)\n", java.lang.Long.valueOf(j3), java.lang.Long.valueOf(j2 / 1000));
                java.util.ArrayList arrayList4 = new java.util.ArrayList(mapNewHashMap.values());
                java.util.Collections.sort(arrayList4, new java.util.Comparator<com.android.server.content.SyncManager.AuthoritySyncStats>() { // from class: com.android.server.content.SyncManager.13
                    @Override // java.util.Comparator
                    public int compare(com.android.server.content.SyncManager.AuthoritySyncStats lhs, com.android.server.content.SyncManager.AuthoritySyncStats rhs) {
                        int compare = java.lang.Integer.compare(rhs.times, lhs.times);
                        if (compare == 0) {
                            return java.lang.Long.compare(rhs.elapsedTime, lhs.elapsedTime);
                        }
                        return compare;
                    }
                });
                int iMax = java.lang.Math.max(i9, i10 + 3);
                char[] cArr = new char[iMax + 4 + 2 + 10 + 11];
                java.util.Arrays.fill(cArr, '-');
                java.lang.String str18 = new java.lang.String(cArr);
                java.lang.String str19 = java.lang.String.format("  %%-%ds: %%-9s  %%-11s\n", java.lang.Integer.valueOf(iMax + 2));
                java.lang.String str20 = java.lang.String.format("    %%-%ds:   %%-9s  %%-11s\n", java.lang.Integer.valueOf(iMax));
                printWriter.println(str18);
                java.util.Iterator it3 = arrayList4.iterator();
                while (it3.hasNext()) {
                    java.util.ArrayList arrayList5 = arrayList4;
                    com.android.server.content.SyncManager.AuthoritySyncStats authoritySyncStats3 = (com.android.server.content.SyncManager.AuthoritySyncStats) it3.next();
                    java.util.Iterator it4 = it3;
                    java.lang.String str21 = authoritySyncStats3.name;
                    java.lang.String str22 = str;
                    java.lang.String str23 = str2;
                    long j7 = authoritySyncStats3.elapsedTime;
                    int i13 = size;
                    int i14 = authoritySyncStats3.times;
                    int i15 = i9;
                    java.lang.String str24 = java.lang.String.format("%ds/%d%%", java.lang.Long.valueOf(j7 / 1000), java.lang.Long.valueOf((j7 * 100) / j2));
                    int i16 = i10;
                    java.lang.String str25 = str20;
                    java.lang.String str26 = java.lang.String.format("%d/%d%%", java.lang.Integer.valueOf(i14), java.lang.Long.valueOf(((long) (i14 * 100)) / j3));
                    printWriter.printf(str19, str21, str26, str24);
                    java.util.ArrayList<com.android.server.content.SyncManager.AccountSyncStats> arrayList6 = new java.util.ArrayList(authoritySyncStats3.accountMap.values());
                    java.util.Collections.sort(arrayList6, new java.util.Comparator<com.android.server.content.SyncManager.AccountSyncStats>() { // from class: com.android.server.content.SyncManager.14
                        @Override // java.util.Comparator
                        public int compare(com.android.server.content.SyncManager.AccountSyncStats lhs, com.android.server.content.SyncManager.AccountSyncStats rhs) {
                            int compare = java.lang.Integer.compare(rhs.times, lhs.times);
                            if (compare == 0) {
                                return java.lang.Long.compare(rhs.elapsedTime, lhs.elapsedTime);
                            }
                            return compare;
                        }
                    });
                    for (com.android.server.content.SyncManager.AccountSyncStats accountSyncStats2 : arrayList6) {
                        com.android.server.content.SyncManager.AuthoritySyncStats authoritySyncStats4 = authoritySyncStats3;
                        long j8 = accountSyncStats2.elapsedTime;
                        int i17 = accountSyncStats2.times;
                        long j9 = j2;
                        java.lang.String str27 = java.lang.String.format("%ds/%d%%", java.lang.Long.valueOf(j8 / 1000), java.lang.Long.valueOf((j8 * 100) / j2));
                        java.lang.String str28 = java.lang.String.format("%d/%d%%", java.lang.Integer.valueOf(i17), java.lang.Long.valueOf(((long) (i17 * 100)) / j3));
                        printWriter.printf(str25, accountSyncStats2.name, str28, str27);
                        i14 = i17;
                        authoritySyncStats3 = authoritySyncStats4;
                        str19 = str19;
                        str24 = str27;
                        str26 = str28;
                        j2 = j9;
                    }
                    printWriter.println(str18);
                    str20 = str25;
                    it3 = it4;
                    arrayList4 = arrayList5;
                    str2 = str23;
                    str = str22;
                    size = i13;
                    i9 = i15;
                    i10 = i16;
                }
                i = size;
                i2 = i9;
                i3 = i10;
                str3 = str;
                str4 = str2;
            } else {
                i = size;
                i2 = i9;
                i3 = i10;
                str3 = " u";
                str4 = com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER;
            }
            printWriter.println();
            printWriter.println("Recent Sync History");
            java.lang.String str29 = "(SERVER is now split up to FEED and OTHER)";
            printWriter.println("(SERVER is now split up to FEED and OTHER)");
            int i18 = i3;
            int i19 = i2;
            java.lang.String str30 = "  %-" + i18 + "s  %-" + i19 + "s %s\n";
            java.util.HashMap mapNewHashMap2 = com.google.android.collect.Maps.newHashMap();
            android.content.pm.PackageManager packageManager3 = syncManager.mContext.getPackageManager();
            int i20 = 0;
            while (true) {
                i4 = i;
                if (i20 >= i4) {
                    break;
                }
                java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> arrayList7 = arrayList3;
                com.android.server.content.SyncStorageEngine.SyncHistoryItem syncHistoryItem = arrayList7.get(i20);
                com.android.server.content.SyncStorageEngine.AuthorityInfo authority2 = syncManager.mSyncStorageEngine.getAuthority(syncHistoryItem.authorityId);
                if (authority2 != null) {
                    str11 = authority2.target.provider;
                    j = j3;
                    str9 = str4;
                    i6 = i18;
                    str10 = str3;
                    i7 = i19;
                    str12 = authority2.target.account.name + str9 + authority2.target.account.type + str10 + authority2.target.userId;
                } else {
                    j = j3;
                    i6 = i18;
                    i7 = i19;
                    str9 = str4;
                    str10 = str3;
                    str11 = "Unknown";
                    str12 = "Unknown";
                }
                long j10 = syncHistoryItem.elapsedTime;
                str3 = str10;
                long j11 = syncHistoryItem.eventTime;
                java.lang.String str31 = str11 + str9 + str12;
                java.lang.Long l = (java.lang.Long) mapNewHashMap2.get(str31);
                if (l == null) {
                    str14 = str29;
                    str13 = str9;
                    i8 = i4;
                    strValueOf = "";
                } else {
                    long jLongValue = (l.longValue() - j11) / 1000;
                    if (jLongValue < 60) {
                        str14 = str29;
                        str13 = str9;
                        i8 = i4;
                        strValueOf = java.lang.String.valueOf(jLongValue);
                    } else if (jLongValue < 3600) {
                        str13 = str9;
                        i8 = i4;
                        str14 = str29;
                        strValueOf = java.lang.String.format("%02d:%02d", java.lang.Long.valueOf(jLongValue / 60), java.lang.Long.valueOf(jLongValue % 60));
                    } else {
                        str13 = str9;
                        i8 = i4;
                        long j12 = jLongValue % 3600;
                        str14 = str29;
                        strValueOf = java.lang.String.format("%02d:%02d:%02d", java.lang.Long.valueOf(jLongValue / 3600), java.lang.Long.valueOf(j12 / 60), java.lang.Long.valueOf(j12 % 60));
                    }
                }
                mapNewHashMap2.put(str31, java.lang.Long.valueOf(j11));
                java.util.HashMap map2 = mapNewHashMap2;
                printWriter.printf("  #%-3d: %s %8s  %5.1fs  %8s", java.lang.Integer.valueOf(i20 + 1), formatTime(j11), com.android.server.content.SyncStorageEngine.SOURCES[syncHistoryItem.source], java.lang.Float.valueOf(j10 / 1000.0f), strValueOf);
                printWriter.printf(str30, str12, str11, com.android.server.content.SyncOperation.reasonToString(packageManager3, syncHistoryItem.reason));
                if (syncHistoryItem.event == 1) {
                    packageManager2 = packageManager3;
                    if (syncHistoryItem.upstreamActivity != 0 || syncHistoryItem.downstreamActivity != 0) {
                    }
                    if (syncHistoryItem.mesg == null && !com.android.server.content.SyncStorageEngine.MESG_SUCCESS.equals(syncHistoryItem.mesg)) {
                        printWriter.printf("    mesg=%s\n", syncHistoryItem.mesg);
                    }
                    i20++;
                    packageManager3 = packageManager2;
                    j3 = j;
                    arrayList3 = arrayList7;
                    mapNewHashMap2 = map2;
                    str4 = str13;
                    i19 = i7;
                    i18 = i6;
                    str29 = str14;
                    i = i8;
                    syncManager = this;
                } else {
                    packageManager2 = packageManager3;
                }
                printWriter.printf("    event=%d upstreamActivity=%d downstreamActivity=%d\n", java.lang.Integer.valueOf(syncHistoryItem.event), java.lang.Long.valueOf(syncHistoryItem.upstreamActivity), java.lang.Long.valueOf(syncHistoryItem.downstreamActivity));
                if (syncHistoryItem.mesg == null) {
                }
                i20++;
                packageManager3 = packageManager2;
                j3 = j;
                arrayList3 = arrayList7;
                mapNewHashMap2 = map2;
                str4 = str13;
                i19 = i7;
                i18 = i6;
                str29 = str14;
                i = i8;
                syncManager = this;
            }
            android.content.pm.PackageManager packageManager4 = packageManager3;
            int i21 = i4;
            java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> arrayList8 = arrayList3;
            java.lang.String str32 = str4;
            printWriter.println();
            printWriter.println("Recent Sync History Extras");
            printWriter.println(str29);
            int i22 = 0;
            while (true) {
                int i23 = i21;
                if (i22 < i23) {
                    java.util.ArrayList<com.android.server.content.SyncStorageEngine.SyncHistoryItem> arrayList9 = arrayList8;
                    com.android.server.content.SyncStorageEngine.SyncHistoryItem syncHistoryItem2 = arrayList9.get(i22);
                    android.os.Bundle bundle = syncHistoryItem2.extras;
                    if (bundle == null) {
                        packageManager = packageManager4;
                        i5 = i23;
                        arrayList = arrayList9;
                        str5 = str3;
                        str6 = str32;
                    } else if (bundle.size() == 0) {
                        packageManager = packageManager4;
                        i5 = i23;
                        arrayList = arrayList9;
                        str5 = str3;
                        str6 = str32;
                    } else {
                        com.android.server.content.SyncStorageEngine.AuthorityInfo authority3 = this.mSyncStorageEngine.getAuthority(syncHistoryItem2.authorityId);
                        if (authority3 != null) {
                            str7 = authority3.target.provider;
                            str6 = str32;
                            str5 = str3;
                            str8 = authority3.target.account.name + str6 + authority3.target.account.type + str5 + authority3.target.userId;
                        } else {
                            str5 = str3;
                            str6 = str32;
                            str7 = "Unknown";
                            str8 = "Unknown";
                        }
                        packageManager = packageManager4;
                        i5 = i23;
                        arrayList = arrayList9;
                        printWriter.printf("  #%-3d: %s %8s ", java.lang.Integer.valueOf(i22 + 1), formatTime(syncHistoryItem2.eventTime), com.android.server.content.SyncStorageEngine.SOURCES[syncHistoryItem2.source]);
                        printWriter.printf(str30, str8, str7, bundle);
                    }
                    i22++;
                    str32 = str6;
                    str3 = str5;
                    packageManager4 = packageManager;
                    arrayList8 = arrayList;
                    i21 = i5;
                } else {
                    return;
                }
            }
        }
    }

    private void dumpDayStatistics(java.io.PrintWriter pw) {
        com.android.server.content.SyncStorageEngine.DayStats ds;
        int delta;
        com.android.server.content.SyncStorageEngine.DayStats[] dses = this.mSyncStorageEngine.getDayStatistics();
        if (dses != null && dses[0] != null) {
            pw.println();
            pw.println("Sync Statistics");
            pw.print("  Today:  ");
            dumpDayStatistic(pw, dses[0]);
            int today = dses[0].day;
            int i = 1;
            while (i <= 6 && i < dses.length && (ds = dses[i]) != null && (delta = today - ds.day) <= 6) {
                pw.print("  Day-");
                pw.print(delta);
                pw.print(":  ");
                dumpDayStatistic(pw, ds);
                i++;
            }
            int weekDay = today;
            while (i < dses.length) {
                com.android.server.content.SyncStorageEngine.DayStats aggr = null;
                weekDay -= 7;
                while (true) {
                    if (i >= dses.length) {
                        break;
                    }
                    com.android.server.content.SyncStorageEngine.DayStats ds2 = dses[i];
                    if (ds2 == null) {
                        i = dses.length;
                        break;
                    }
                    if (weekDay - ds2.day > 6) {
                        break;
                    }
                    i++;
                    if (aggr == null) {
                        aggr = new com.android.server.content.SyncStorageEngine.DayStats(weekDay);
                    }
                    aggr.successCount += ds2.successCount;
                    aggr.successTime += ds2.successTime;
                    aggr.failureCount += ds2.failureCount;
                    aggr.failureTime += ds2.failureTime;
                }
                if (aggr != null) {
                    pw.print("  Week-");
                    pw.print((today - weekDay) / 7);
                    pw.print(": ");
                    dumpDayStatistic(pw, aggr);
                }
            }
        }
    }

    private void dumpSyncAdapters(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println();
        java.util.List<android.content.pm.UserInfo> users = getAllUsers();
        if (users != null) {
            for (android.content.pm.UserInfo user : users) {
                pw.println("Sync adapters for " + user + ":");
                pw.increaseIndent();
                for (android.content.pm.RegisteredServicesCache.ServiceInfo<?> info : this.mSyncAdapters.getAllServices(user.id)) {
                    pw.println(info);
                }
                pw.decreaseIndent();
                pw.println();
            }
        }
    }

    private static class AuthoritySyncStats {
        java.util.Map<java.lang.String, com.android.server.content.SyncManager.AccountSyncStats> accountMap;
        long elapsedTime;
        java.lang.String name;
        int times;

        private AuthoritySyncStats(java.lang.String name) {
            this.accountMap = com.google.android.collect.Maps.newHashMap();
            this.name = name;
        }
    }

    private static class AccountSyncStats {
        long elapsedTime;
        java.lang.String name;
        int times;

        private AccountSyncStats(java.lang.String name) {
            this.name = name;
        }
    }

    static void sendOnUnsyncableAccount(final android.content.Context context, android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterInfo, int userId, com.android.server.content.SyncManager.OnReadyCallback onReadyCallback) {
        final com.android.server.content.SyncManager.OnUnsyncableAccountCheck connection = new com.android.server.content.SyncManager.OnUnsyncableAccountCheck(syncAdapterInfo, onReadyCallback);
        boolean isBound = context.bindServiceAsUser(getAdapterBindIntent(context, syncAdapterInfo.componentName, userId), connection, SYNC_ADAPTER_CONNECTION_FLAGS, android.os.UserHandle.of(userId));
        if (isBound) {
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new java.lang.Runnable() { // from class: com.android.server.content.SyncManager$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    context.unbindService(connection);
                }
            }, 5000L);
        } else {
            connection.onReady();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class OnUnsyncableAccountCheck implements android.content.ServiceConnection {
        static final long SERVICE_BOUND_TIME_MILLIS = 5000;
        private final com.android.server.content.SyncManager.OnReadyCallback mOnReadyCallback;
        private final android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> mSyncAdapterInfo;

        OnUnsyncableAccountCheck(android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterInfo, com.android.server.content.SyncManager.OnReadyCallback onReadyCallback) {
            this.mSyncAdapterInfo = syncAdapterInfo;
            this.mOnReadyCallback = onReadyCallback;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onReady() {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                this.mOnReadyCallback.onReady();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            android.content.ISyncAdapter adapter = android.content.ISyncAdapter.Stub.asInterface(service);
            try {
                adapter.onUnsyncableAccount(new android.content.ISyncAdapterUnsyncableAccountCallback.Stub() { // from class: com.android.server.content.SyncManager.OnUnsyncableAccountCheck.1
                    public void onUnsyncableAccountDone(boolean isReady) {
                        if (isReady) {
                            com.android.server.content.SyncManager.OnUnsyncableAccountCheck.this.onReady();
                        }
                    }
                });
            } catch (android.os.RemoteException e) {
                android.util.Slog.e("SyncManager", "Could not call onUnsyncableAccountDone " + this.mSyncAdapterInfo, e);
                onReady();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
        }
    }

    private class SyncTimeTracker {
        boolean mLastWasSyncing;
        private long mTimeSpentSyncing;
        long mWhenSyncStarted;

        private SyncTimeTracker() {
            this.mLastWasSyncing = false;
            this.mWhenSyncStarted = 0L;
        }

        public synchronized void update() {
            boolean isSyncInProgress = !com.android.server.content.SyncManager.this.mActiveSyncContexts.isEmpty();
            if (isSyncInProgress == this.mLastWasSyncing) {
                return;
            }
            long now = android.os.SystemClock.elapsedRealtime();
            if (isSyncInProgress) {
                this.mWhenSyncStarted = now;
            } else {
                this.mTimeSpentSyncing += now - this.mWhenSyncStarted;
            }
            this.mLastWasSyncing = isSyncInProgress;
        }

        public synchronized long timeSpentSyncing() {
            if (!this.mLastWasSyncing) {
                return this.mTimeSpentSyncing;
            }
            long now = android.os.SystemClock.elapsedRealtime();
            return this.mTimeSpentSyncing + (now - this.mWhenSyncStarted);
        }
    }

    class ServiceConnectionData {
        public final com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext;
        public final android.os.IBinder adapter;

        ServiceConnectionData(com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext, android.os.IBinder adapter) {
            this.activeSyncContext = activeSyncContext;
            this.adapter = adapter;
        }
    }

    private static com.android.server.content.SyncManager getInstance() {
        com.android.server.content.SyncManager syncManager;
        synchronized (com.android.server.content.SyncManager.class) {
            if (sInstance == null) {
                android.util.Slog.wtf("SyncManager", "sInstance == null");
            }
            syncManager = sInstance;
        }
        return syncManager;
    }

    public static boolean readyToSync(int userId) {
        com.android.server.content.SyncManager instance = getInstance();
        return instance != null && com.android.server.content.SyncJobService.isReady() && instance.mProvisioned && instance.isUserUnlocked(userId);
    }

    public static void sendMessage(android.os.Message message) {
        com.android.server.content.SyncManager instance = getInstance();
        if (instance != null && instance.mSyncHandler != null) {
            instance.mSyncHandler.sendMessage(message);
        }
    }

    class SyncHandler extends android.os.Handler {
        private static final int MESSAGE_ACCOUNTS_UPDATED = 9;
        private static final int MESSAGE_CANCEL = 6;
        private static final int MESSAGE_MONITOR_SYNC = 8;
        static final int MESSAGE_REMOVE_PERIODIC_SYNC = 14;
        static final int MESSAGE_SCHEDULE_SYNC = 12;
        private static final int MESSAGE_SERVICE_CONNECTED = 4;
        private static final int MESSAGE_SERVICE_DISCONNECTED = 5;
        static final int MESSAGE_START_SYNC = 10;
        static final int MESSAGE_STOP_SYNC = 11;
        private static final int MESSAGE_SYNC_FINISHED = 1;
        static final int MESSAGE_UPDATE_PERIODIC_SYNC = 13;
        public final com.android.server.content.SyncManager.SyncTimeTracker mSyncTimeTracker;
        private final java.util.HashMap<java.lang.String, android.os.PowerManager.WakeLock> mWakeLocks;

        public SyncHandler(android.os.Looper looper) {
            super(looper);
            this.mSyncTimeTracker = new com.android.server.content.SyncManager.SyncTimeTracker();
            this.mWakeLocks = com.google.android.collect.Maps.newHashMap();
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            com.android.server.content.SyncManager.this.mSyncManagerWakeLock.acquire();
            try {
                handleSyncMessage(msg);
            } finally {
                com.android.server.content.SyncManager.this.mSyncManagerWakeLock.release();
            }
        }

        private void handleSyncMessage(android.os.Message msg) {
            boolean isLoggable = android.util.Log.isLoggable("SyncManager", 2);
            try {
                com.android.server.content.SyncManager.this.mDataConnectionIsConnected = com.android.server.content.SyncManager.this.readDataConnectionState();
                switch (msg.what) {
                    case 1:
                        com.android.server.content.SyncManager.SyncFinishedOrCancelledMessagePayload payload = (com.android.server.content.SyncManager.SyncFinishedOrCancelledMessagePayload) msg.obj;
                        if (!com.android.server.content.SyncManager.this.isSyncStillActiveH(payload.activeSyncContext)) {
                            if (isLoggable) {
                                android.util.Log.d("SyncManager", "handleSyncHandlerMessage: dropping since the sync is no longer active: " + payload.activeSyncContext);
                            }
                        } else {
                            if (isLoggable) {
                                android.util.Slog.v("SyncManager", "syncFinished" + payload.activeSyncContext.mSyncOperation);
                            }
                            com.android.server.content.SyncJobService.callJobFinished(payload.activeSyncContext.mSyncOperation.jobId, false, "sync finished");
                            runSyncFinishedOrCanceledH(payload.syncResult, payload.activeSyncContext);
                        }
                        return;
                    case 2:
                    case 3:
                    case 7:
                    default:
                        return;
                    case 4:
                        com.android.server.content.SyncManager.ServiceConnectionData msgData = (com.android.server.content.SyncManager.ServiceConnectionData) msg.obj;
                        if (isLoggable) {
                            android.util.Log.d("SyncManager", "handleSyncHandlerMessage: MESSAGE_SERVICE_CONNECTED: " + msgData.activeSyncContext);
                        }
                        if (com.android.server.content.SyncManager.this.isSyncStillActiveH(msgData.activeSyncContext)) {
                            runBoundToAdapterH(msgData.activeSyncContext, msgData.adapter);
                        }
                        return;
                    case 5:
                        com.android.server.content.SyncManager.ActiveSyncContext currentSyncContext = ((com.android.server.content.SyncManager.ServiceConnectionData) msg.obj).activeSyncContext;
                        if (isLoggable) {
                            android.util.Log.d("SyncManager", "handleSyncHandlerMessage: MESSAGE_SERVICE_DISCONNECTED: " + currentSyncContext);
                        }
                        if (com.android.server.content.SyncManager.this.isSyncStillActiveH(currentSyncContext)) {
                            try {
                                if (currentSyncContext.mSyncAdapter != null) {
                                    com.android.server.content.SyncManager.this.mLogger.log("Calling cancelSync for SERVICE_DISCONNECTED ", currentSyncContext, " adapter=", currentSyncContext.mSyncAdapter);
                                    currentSyncContext.mSyncAdapter.cancelSync(currentSyncContext);
                                    com.android.server.content.SyncManager.this.mLogger.log("Canceled");
                                }
                            } catch (android.os.RemoteException e) {
                                com.android.server.content.SyncManager.this.mLogger.log("RemoteException ", android.util.Log.getStackTraceString(e));
                            }
                            android.content.SyncResult syncResult = new android.content.SyncResult();
                            syncResult.stats.numIoExceptions++;
                            com.android.server.content.SyncJobService.callJobFinished(currentSyncContext.mSyncOperation.jobId, false, "service disconnected");
                            runSyncFinishedOrCanceledH(syncResult, currentSyncContext);
                            break;
                        }
                        return;
                    case 6:
                        com.android.server.content.SyncStorageEngine.EndPoint endpoint = (com.android.server.content.SyncStorageEngine.EndPoint) msg.obj;
                        android.os.Bundle extras = msg.peekData();
                        if (isLoggable) {
                            android.util.Log.d("SyncManager", "handleSyncHandlerMessage: MESSAGE_CANCEL: " + endpoint + " bundle: " + extras);
                        }
                        cancelActiveSyncH(endpoint, extras, "MESSAGE_CANCEL");
                        return;
                    case 8:
                        com.android.server.content.SyncManager.ActiveSyncContext monitoredSyncContext = (com.android.server.content.SyncManager.ActiveSyncContext) msg.obj;
                        if (isLoggable) {
                            android.util.Log.d("SyncManager", "handleSyncHandlerMessage: MESSAGE_MONITOR_SYNC: " + monitoredSyncContext.mSyncOperation.target);
                        }
                        if (isSyncNotUsingNetworkH(monitoredSyncContext)) {
                            android.util.Log.w("SyncManager", java.lang.String.format("Detected sync making no progress for %s. cancelling.", com.android.server.content.SyncLogger.logSafe(monitoredSyncContext)));
                            com.android.server.content.SyncJobService.callJobFinished(monitoredSyncContext.mSyncOperation.jobId, false, "no network activity");
                            runSyncFinishedOrCanceledH(null, monitoredSyncContext);
                        } else {
                            com.android.server.content.SyncManager.this.postMonitorSyncProgressMessage(monitoredSyncContext);
                        }
                        return;
                    case 9:
                        if (android.util.Log.isLoggable("SyncManager", 2)) {
                            android.util.Slog.v("SyncManager", "handleSyncHandlerMessage: MESSAGE_ACCOUNTS_UPDATED");
                        }
                        com.android.server.content.SyncStorageEngine.EndPoint targets = (com.android.server.content.SyncStorageEngine.EndPoint) msg.obj;
                        updateRunningAccountsH(targets);
                        return;
                    case 10:
                        startSyncH((com.android.server.content.SyncOperation) msg.obj);
                        return;
                    case 11:
                        com.android.server.content.SyncOperation op = (com.android.server.content.SyncOperation) msg.obj;
                        if (isLoggable) {
                            android.util.Slog.v("SyncManager", "Stop sync received.");
                        }
                        com.android.server.content.SyncManager.ActiveSyncContext asc = findActiveSyncContextH(op.jobId);
                        if (asc != null) {
                            runSyncFinishedOrCanceledH(null, asc);
                            boolean reschedule = msg.arg1 != 0;
                            boolean applyBackoff = msg.arg2 != 0;
                            if (isLoggable) {
                                android.util.Slog.v("SyncManager", "Stopping sync. Reschedule: " + reschedule + "Backoff: " + applyBackoff);
                            }
                            if (applyBackoff) {
                                com.android.server.content.SyncManager.this.increaseBackoffSetting(op.target);
                            }
                            if (reschedule) {
                                deferStoppedSyncH(op, 0L);
                            }
                        }
                        return;
                    case 12:
                        com.android.server.content.SyncManager.ScheduleSyncMessagePayload syncPayload = (com.android.server.content.SyncManager.ScheduleSyncMessagePayload) msg.obj;
                        com.android.server.content.SyncManager.this.scheduleSyncOperationH(syncPayload.syncOperation, syncPayload.minDelayMillis);
                        return;
                    case 13:
                        com.android.server.content.SyncManager.UpdatePeriodicSyncMessagePayload data = (com.android.server.content.SyncManager.UpdatePeriodicSyncMessagePayload) msg.obj;
                        updateOrAddPeriodicSyncH(data.target, data.pollFrequency, data.flex, data.extras);
                        return;
                    case 14:
                        android.util.Pair<com.android.server.content.SyncStorageEngine.EndPoint, java.lang.String> args = (android.util.Pair) msg.obj;
                        removePeriodicSyncH((com.android.server.content.SyncStorageEngine.EndPoint) args.first, msg.getData(), (java.lang.String) args.second);
                        return;
                }
            } finally {
            }
            this.mSyncTimeTracker.update();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.os.PowerManager.WakeLock getSyncWakeLock(com.android.server.content.SyncOperation operation) {
            java.lang.String wakeLockKey = operation.wakeLockName();
            android.os.PowerManager.WakeLock wakeLock = this.mWakeLocks.get(wakeLockKey);
            if (wakeLock == null) {
                java.lang.String name = com.android.server.content.SyncManager.SYNC_WAKE_LOCK_PREFIX + wakeLockKey;
                android.os.PowerManager.WakeLock wakeLock2 = com.android.server.content.SyncManager.this.mPowerManager.newWakeLock(1, name);
                wakeLock2.setReferenceCounted(false);
                this.mWakeLocks.put(wakeLockKey, wakeLock2);
                return wakeLock2;
            }
            return wakeLock;
        }

        private void deferSyncH(com.android.server.content.SyncOperation op, long delay, java.lang.String why) {
            com.android.server.content.SyncManager.this.mLogger.log("deferSyncH() ", op.isPeriodic ? "periodic " : "", "sync.  op=", op, " delay=", java.lang.Long.valueOf(delay), " why=", why);
            com.android.server.content.SyncJobService.callJobFinished(op.jobId, false, why);
            if (op.isPeriodic) {
                com.android.server.content.SyncManager.this.scheduleSyncOperationH(op.createOneTimeSyncOperation(), delay);
            } else {
                com.android.server.content.SyncManager.this.cancelJob(op, "deferSyncH()");
                com.android.server.content.SyncManager.this.scheduleSyncOperationH(op, delay);
            }
        }

        private void deferStoppedSyncH(com.android.server.content.SyncOperation op, long delay) {
            if (op.isPeriodic) {
                com.android.server.content.SyncManager.this.scheduleSyncOperationH(op.createOneTimeSyncOperation(), delay);
            } else {
                com.android.server.content.SyncManager.this.scheduleSyncOperationH(op, delay);
            }
        }

        private void deferActiveSyncH(com.android.server.content.SyncManager.ActiveSyncContext asc, java.lang.String why) {
            com.android.server.content.SyncOperation op = asc.mSyncOperation;
            runSyncFinishedOrCanceledH(null, asc);
            deferSyncH(op, 10000L, why);
        }

        private void startSyncH(com.android.server.content.SyncOperation op) {
            boolean isLoggable = android.util.Log.isLoggable("SyncManager", 2);
            if (isLoggable) {
                android.util.Slog.v("SyncManager", op.toString());
            }
            com.android.server.content.SyncManager.this.mSyncStorageEngine.setClockValid();
            com.android.server.content.SyncJobService.markSyncStarted(op.jobId);
            if (op.isPeriodic) {
                java.util.List<com.android.server.content.SyncOperation> ops = com.android.server.content.SyncManager.this.getAllPendingSyncs();
                for (com.android.server.content.SyncOperation syncOperation : ops) {
                    if (syncOperation.sourcePeriodicId == op.jobId) {
                        com.android.server.content.SyncJobService.callJobFinished(op.jobId, false, "periodic sync, pending");
                        return;
                    }
                }
                java.util.Iterator<com.android.server.content.SyncManager.ActiveSyncContext> it = com.android.server.content.SyncManager.this.mActiveSyncContexts.iterator();
                while (it.hasNext()) {
                    if (it.next().mSyncOperation.sourcePeriodicId == op.jobId) {
                        com.android.server.content.SyncJobService.callJobFinished(op.jobId, false, "periodic sync, already running");
                        return;
                    }
                }
                if (com.android.server.content.SyncManager.this.isAdapterDelayed(op.target)) {
                    deferSyncH(op, 0L, "backing off");
                    return;
                }
            }
            java.util.Iterator<com.android.server.content.SyncManager.ActiveSyncContext> it2 = com.android.server.content.SyncManager.this.mActiveSyncContexts.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                com.android.server.content.SyncManager.ActiveSyncContext asc = it2.next();
                if (asc.mSyncOperation.isConflict(op)) {
                    if (asc.mSyncOperation.getJobBias() >= op.getJobBias()) {
                        if (isLoggable) {
                            android.util.Slog.v("SyncManager", "Rescheduling sync due to conflict " + op.toString());
                        }
                        deferSyncH(op, 10000L, "delay on conflict");
                        return;
                    } else {
                        if (isLoggable) {
                            android.util.Slog.v("SyncManager", "Pushing back running sync due to a higher priority sync");
                        }
                        deferActiveSyncH(asc, "preempted");
                    }
                }
            }
            int syncOpState = computeSyncOpState(op);
            if (syncOpState != 0) {
                com.android.server.content.SyncJobService.callJobFinished(op.jobId, false, "invalid op state: " + syncOpState);
                return;
            }
            if (!dispatchSyncOperation(op)) {
                com.android.server.content.SyncJobService.callJobFinished(op.jobId, false, "dispatchSyncOperation() failed");
            }
            com.android.server.content.SyncManager.this.setAuthorityPendingState(op.target);
        }

        private com.android.server.content.SyncManager.ActiveSyncContext findActiveSyncContextH(int jobId) {
            for (com.android.server.content.SyncManager.ActiveSyncContext asc : com.android.server.content.SyncManager.this.mActiveSyncContexts) {
                com.android.server.content.SyncOperation op = asc.mSyncOperation;
                if (op != null && op.jobId == jobId) {
                    return asc;
                }
            }
            return null;
        }

        private void updateRunningAccountsH(com.android.server.content.SyncStorageEngine.EndPoint syncTargets) throws java.lang.Throwable {
            synchronized (com.android.server.content.SyncManager.this.mAccountsLock) {
                android.accounts.AccountAndUser[] oldAccounts = com.android.server.content.SyncManager.this.mRunningAccounts;
                com.android.server.content.SyncManager.this.mRunningAccounts = com.android.server.accounts.AccountManagerService.getSingleton().getRunningAccountsForSystem();
                if (android.util.Log.isLoggable("SyncManager", 2)) {
                    android.util.Slog.v("SyncManager", "Accounts list: ");
                    for (android.accounts.AccountAndUser acc : com.android.server.content.SyncManager.this.mRunningAccounts) {
                        android.util.Slog.v("SyncManager", acc.toString());
                    }
                }
                if (com.android.server.content.SyncManager.this.mLogger.enabled()) {
                    com.android.server.content.SyncManager.this.mLogger.log("updateRunningAccountsH: ", java.util.Arrays.toString(com.android.server.content.SyncManager.this.mRunningAccounts));
                }
                com.android.server.content.SyncManager.this.removeStaleAccounts();
                android.accounts.AccountAndUser[] accounts = com.android.server.content.SyncManager.this.mRunningAccounts;
                int size = com.android.server.content.SyncManager.this.mActiveSyncContexts.size();
                for (int i = 0; i < size; i++) {
                    com.android.server.content.SyncManager.ActiveSyncContext currentSyncContext = com.android.server.content.SyncManager.this.mActiveSyncContexts.get(i);
                    if (!com.android.server.content.SyncManager.this.containsAccountAndUser(accounts, currentSyncContext.mSyncOperation.target.account, currentSyncContext.mSyncOperation.target.userId)) {
                        android.util.Log.d("SyncManager", "canceling sync since the account is no longer running");
                        com.android.server.content.SyncManager.this.sendSyncFinishedOrCanceledMessage(currentSyncContext, null);
                    }
                }
                if (syncTargets != null) {
                    int i2 = 0;
                    int length = com.android.server.content.SyncManager.this.mRunningAccounts.length;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        android.accounts.AccountAndUser aau = com.android.server.content.SyncManager.this.mRunningAccounts[i2];
                        if (com.android.server.content.SyncManager.this.containsAccountAndUser(oldAccounts, aau.account, aau.userId)) {
                            i2++;
                        } else {
                            if (android.util.Log.isLoggable("SyncManager", 3)) {
                                android.util.Log.d("SyncManager", "Account " + aau.account + " added, checking sync restore data");
                            }
                            com.android.server.backup.AccountSyncSettingsBackupHelper.accountAdded(com.android.server.content.SyncManager.this.mContext, syncTargets.userId);
                        }
                    }
                }
            }
            android.accounts.AccountAndUser[] allAccounts = com.android.server.accounts.AccountManagerService.getSingleton().getAllAccountsForSystemProcess();
            java.util.List<com.android.server.content.SyncOperation> ops = com.android.server.content.SyncManager.this.getAllPendingSyncs();
            int opsSize = ops.size();
            for (int i3 = 0; i3 < opsSize; i3++) {
                com.android.server.content.SyncOperation op = ops.get(i3);
                if (!com.android.server.content.SyncManager.this.containsAccountAndUser(allAccounts, op.target.account, op.target.userId)) {
                    com.android.server.content.SyncManager.this.mLogger.log("canceling: ", op);
                    com.android.server.content.SyncManager.this.cancelJob(op, "updateRunningAccountsH()");
                }
            }
            if (syncTargets != null) {
                com.android.server.content.SyncManager.this.scheduleSync(syncTargets.account, syncTargets.userId, -2, syncTargets.provider, null, -1, 0, android.os.Process.myUid(), -4, null);
            }
        }

        private void maybeUpdateSyncPeriodH(com.android.server.content.SyncOperation syncOperation, long pollFrequencyMillis, long flexMillis) {
            if (pollFrequencyMillis != syncOperation.periodMillis || flexMillis != syncOperation.flexMillis) {
                if (android.util.Log.isLoggable("SyncManager", 2)) {
                    android.util.Slog.v("SyncManager", "updating period " + syncOperation + " to " + pollFrequencyMillis + " and flex to " + flexMillis);
                }
                com.android.server.content.SyncOperation newOp = new com.android.server.content.SyncOperation(syncOperation, pollFrequencyMillis, flexMillis);
                newOp.jobId = syncOperation.jobId;
                com.android.server.content.SyncManager.this.scheduleSyncOperationH(newOp);
            }
        }

        private void updateOrAddPeriodicSyncH(final com.android.server.content.SyncStorageEngine.EndPoint target, final long pollFrequency, final long flex, final android.os.Bundle extras) {
            boolean isLoggable = android.util.Log.isLoggable("SyncManager", 2);
            com.android.server.content.SyncManager.this.verifyJobScheduler();
            long pollFrequencyMillis = pollFrequency * 1000;
            long flexMillis = flex * 1000;
            if (isLoggable) {
                android.util.Slog.v("SyncManager", "Addition to periodic syncs requested: " + target + " period: " + pollFrequency + " flexMillis: " + flex + " extras: " + extras.toString());
            }
            java.util.List<com.android.server.content.SyncOperation> ops = com.android.server.content.SyncManager.this.getAllPendingSyncs();
            for (com.android.server.content.SyncOperation op : ops) {
                if (op.isPeriodic && op.target.matchesSpec(target)) {
                    if (op.areExtrasEqual(extras, true)) {
                        if (!com.android.server.content.SyncManager.this.isPackageStopped(op.owningPackage, target.userId)) {
                            maybeUpdateSyncPeriodH(op, pollFrequencyMillis, flexMillis);
                            return;
                        }
                    }
                }
            }
            if (isLoggable) {
                android.util.Slog.v("SyncManager", "Adding new periodic sync: " + target + " period: " + pollFrequency + " flexMillis: " + flex + " extras: " + extras.toString());
            }
            android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterInfo = com.android.server.content.SyncManager.this.mSyncAdapters.getServiceInfo(android.content.SyncAdapterType.newKey(target.provider, target.account.type), target.userId);
            if (syncAdapterInfo == null) {
                return;
            }
            com.android.server.content.SyncOperation op2 = new com.android.server.content.SyncOperation(target, syncAdapterInfo.uid, syncAdapterInfo.componentName.getPackageName(), -4, 4, extras, ((android.content.SyncAdapterType) syncAdapterInfo.type).allowParallelSyncs(), true, -1, pollFrequencyMillis, flexMillis, 0);
            int syncOpState = computeSyncOpState(op2);
            if (syncOpState != 2) {
                if (syncOpState != 0) {
                    com.android.server.content.SyncManager.this.mLogger.log("syncOpState=", java.lang.Integer.valueOf(syncOpState));
                    return;
                } else {
                    com.android.server.content.SyncManager.this.scheduleSyncOperationH(op2);
                    com.android.server.content.SyncManager.this.mSyncStorageEngine.reportChange(1, op2.owningPackage, target.userId);
                    return;
                }
            }
            java.lang.String packageName = op2.owningPackage;
            int userId = android.os.UserHandle.getUserId(op2.owningUid);
            if (!com.android.server.content.SyncManager.this.wasPackageEverLaunched(packageName, userId)) {
                return;
            }
            com.android.server.content.SyncManager.this.mLogger.log("requestAccountAccess for SYNC_OP_STATE_INVALID_NO_ACCOUNT_ACCESS");
            com.android.server.content.SyncManager.this.mAccountManagerInternal.requestAccountAccess(op2.target.account, packageName, userId, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.content.SyncManager$SyncHandler$$ExternalSyntheticLambda0
                public final void onResult(android.os.Bundle bundle) {
                    this.f$0.lambda$updateOrAddPeriodicSyncH$0(target, pollFrequency, flex, extras, bundle);
                }
            }));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateOrAddPeriodicSyncH$0(com.android.server.content.SyncStorageEngine.EndPoint target, long pollFrequency, long flex, android.os.Bundle extras, android.os.Bundle result) {
            if (result != null && result.getBoolean("booleanResult")) {
                com.android.server.content.SyncManager.this.updateOrAddPeriodicSync(target, pollFrequency, flex, extras);
            }
        }

        private void removePeriodicSyncInternalH(com.android.server.content.SyncOperation syncOperation, java.lang.String why) {
            java.util.List<com.android.server.content.SyncOperation> ops = com.android.server.content.SyncManager.this.getAllPendingSyncs();
            for (com.android.server.content.SyncOperation op : ops) {
                if (op.sourcePeriodicId == syncOperation.jobId || op.jobId == syncOperation.jobId) {
                    com.android.server.content.SyncManager.ActiveSyncContext asc = findActiveSyncContextH(syncOperation.jobId);
                    if (asc != null) {
                        com.android.server.content.SyncJobService.callJobFinished(syncOperation.jobId, false, "removePeriodicSyncInternalH");
                        runSyncFinishedOrCanceledH(null, asc);
                    }
                    com.android.server.content.SyncManager.this.mLogger.log("removePeriodicSyncInternalH-canceling: ", op);
                    com.android.server.content.SyncManager.this.cancelJob(op, why);
                }
            }
        }

        private void removePeriodicSyncH(com.android.server.content.SyncStorageEngine.EndPoint target, android.os.Bundle extras, java.lang.String why) {
            com.android.server.content.SyncManager.this.verifyJobScheduler();
            java.util.List<com.android.server.content.SyncOperation> ops = com.android.server.content.SyncManager.this.getAllPendingSyncs();
            for (com.android.server.content.SyncOperation op : ops) {
                if (op.isPeriodic && op.target.matchesSpec(target) && op.areExtrasEqual(extras, true)) {
                    removePeriodicSyncInternalH(op, why);
                }
            }
        }

        private boolean isSyncNotUsingNetworkH(com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext) {
            long bytesTransferredCurrent = com.android.server.content.SyncManager.this.getTotalBytesTransferredByUid(activeSyncContext.mSyncAdapterUid);
            long deltaBytesTransferred = bytesTransferredCurrent - activeSyncContext.mBytesTransferredAtLastPoll;
            if (android.util.Log.isLoggable("SyncManager", 3)) {
                long mb = deltaBytesTransferred / 1048576;
                long remainder = deltaBytesTransferred % 1048576;
                long kb = remainder / 1024;
                android.util.Log.d("SyncManager", java.lang.String.format("Time since last update: %ds. Delta transferred: %dMBs,%dKBs,%dBs", java.lang.Long.valueOf((android.os.SystemClock.elapsedRealtime() - activeSyncContext.mLastPolledTimeElapsed) / 1000), java.lang.Long.valueOf(mb), java.lang.Long.valueOf(kb), java.lang.Long.valueOf(remainder % 1024)));
            }
            return deltaBytesTransferred <= 10;
        }

        private int computeSyncOpState(com.android.server.content.SyncOperation op) {
            boolean isLoggable = android.util.Log.isLoggable("SyncManager", 2);
            com.android.server.content.SyncStorageEngine.EndPoint target = op.target;
            synchronized (com.android.server.content.SyncManager.this.mAccountsLock) {
                android.accounts.AccountAndUser[] accounts = com.android.server.content.SyncManager.this.mRunningAccounts;
                if (!com.android.server.content.SyncManager.this.containsAccountAndUser(accounts, target.account, target.userId)) {
                    if (isLoggable) {
                        android.util.Slog.v("SyncManager", "    Dropping sync operation: account doesn't exist.");
                    }
                    logAccountError("SYNC_OP_STATE_INVALID: account doesn't exist.");
                    return 3;
                }
                int state = com.android.server.content.SyncManager.this.computeSyncable(target.account, target.userId, target.provider, true, true);
                if (state == 3) {
                    if (isLoggable) {
                        android.util.Slog.v("SyncManager", "    Dropping sync operation: isSyncable == SYNCABLE_NO_ACCOUNT_ACCESS");
                    }
                    logAccountError("SYNC_OP_STATE_INVALID_NO_ACCOUNT_ACCESS");
                    return 2;
                }
                if (state == 0) {
                    if (isLoggable) {
                        android.util.Slog.v("SyncManager", "    Dropping sync operation: isSyncable == NOT_SYNCABLE");
                    }
                    logAccountError("SYNC_OP_STATE_INVALID: NOT_SYNCABLE");
                    return 4;
                }
                if (!com.android.server.content.SyncManager.this.mSyncManagerExt.isSyncValid(op.owningUid, op.owningPackage)) {
                    return 4;
                }
                boolean ignoreSystemConfiguration = true;
                boolean syncEnabled = com.android.server.content.SyncManager.this.mSyncStorageEngine.getMasterSyncAutomatically(target.userId) && com.android.server.content.SyncManager.this.mSyncStorageEngine.getSyncAutomatically(target.account, target.userId, target.provider);
                if (!op.isIgnoreSettings() && state >= 0) {
                    ignoreSystemConfiguration = false;
                }
                if (syncEnabled || ignoreSystemConfiguration) {
                    return 0;
                }
                if (isLoggable) {
                    android.util.Slog.v("SyncManager", "    Dropping sync operation: disallowed by settings/network.");
                }
                logAccountError("SYNC_OP_STATE_INVALID: disallowed by settings/network");
                return 5;
            }
        }

        private void logAccountError(java.lang.String message) {
            android.util.Slog.wtf("SyncManager", message);
        }

        private boolean dispatchSyncOperation(com.android.server.content.SyncOperation op) {
            android.app.usage.UsageStatsManagerInternal usmi;
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "dispatchSyncOperation: we are going to sync " + op);
                android.util.Slog.v("SyncManager", "num active syncs: " + com.android.server.content.SyncManager.this.mActiveSyncContexts.size());
                for (com.android.server.content.SyncManager.ActiveSyncContext syncContext : com.android.server.content.SyncManager.this.mActiveSyncContexts) {
                    android.util.Slog.v("SyncManager", syncContext.toString());
                }
            }
            if (op.isAppStandbyExempted() && (usmi = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class)) != null) {
                usmi.reportExemptedSyncStart(op.owningPackage, android.os.UserHandle.getUserId(op.owningUid));
            }
            com.android.server.content.SyncStorageEngine.EndPoint info = op.target;
            android.content.SyncAdapterType syncAdapterType = android.content.SyncAdapterType.newKey(info.provider, info.account.type);
            android.content.pm.RegisteredServicesCache.ServiceInfo<android.content.SyncAdapterType> syncAdapterInfo = com.android.server.content.SyncManager.this.mSyncAdapters.getServiceInfo(syncAdapterType, info.userId);
            if (syncAdapterInfo == null) {
                com.android.server.content.SyncManager.this.mLogger.log("dispatchSyncOperation() failed: no sync adapter info for ", syncAdapterType);
                android.util.Log.d("SyncManager", "can't find a sync adapter for " + syncAdapterType + ", removing settings for it");
                com.android.server.content.SyncManager.this.mSyncStorageEngine.removeAuthority(info);
                return false;
            }
            int targetUid = syncAdapterInfo.uid;
            android.content.ComponentName targetComponent = syncAdapterInfo.componentName;
            if (com.android.server.content.SyncManager.this.mSyncManagerExt.interceptDispatchSyncOperation(targetUid, op, targetComponent)) {
                return false;
            }
            com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext = com.android.server.content.SyncManager.this.new ActiveSyncContext(op, insertStartSyncEvent(op), targetUid);
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "dispatchSyncOperation: starting " + activeSyncContext);
            }
            activeSyncContext.mSyncInfo = com.android.server.content.SyncManager.this.mSyncStorageEngine.addActiveSync(activeSyncContext);
            com.android.server.content.SyncManager.this.mActiveSyncContexts.add(activeSyncContext);
            com.android.server.content.SyncManager.this.postMonitorSyncProgressMessage(activeSyncContext);
            if (!activeSyncContext.bindToSyncAdapter(targetComponent, info.userId)) {
                com.android.server.content.SyncManager.this.mLogger.log("dispatchSyncOperation() failed: bind failed. target: ", targetComponent);
                android.util.Slog.e("SyncManager", "Bind attempt failed - target: " + targetComponent);
                closeActiveSyncContext(activeSyncContext);
                return false;
            }
            return true;
        }

        private void runBoundToAdapterH(com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext, android.os.IBinder syncAdapter) {
            com.android.server.content.SyncOperation syncOperation = activeSyncContext.mSyncOperation;
            try {
                activeSyncContext.mIsLinkedToDeath = true;
                syncAdapter.linkToDeath(activeSyncContext, 0);
                if (com.android.server.content.SyncManager.this.mLogger.enabled()) {
                    com.android.server.content.SyncManager.this.mLogger.log("Sync start: account=" + syncOperation.target.account, " authority=", syncOperation.target.provider, " reason=", com.android.server.content.SyncOperation.reasonToString(null, syncOperation.reason), " extras=", syncOperation.getExtrasAsString(), " adapter=", activeSyncContext.mSyncAdapter);
                }
                activeSyncContext.mSyncAdapter = android.content.ISyncAdapter.Stub.asInterface(syncAdapter);
                activeSyncContext.mSyncAdapter.startSync(activeSyncContext, syncOperation.target.provider, syncOperation.target.account, syncOperation.getClonedExtras());
                com.android.server.content.SyncManager.this.mLogger.log("Sync is running now...");
            } catch (android.os.RemoteException remoteExc) {
                com.android.server.content.SyncManager.this.mLogger.log("Sync failed with RemoteException: ", remoteExc.toString());
                android.util.Log.d("SyncManager", "maybeStartNextSync: caught a RemoteException, rescheduling", remoteExc);
                closeActiveSyncContext(activeSyncContext);
                com.android.server.content.SyncManager.this.increaseBackoffSetting(syncOperation.target);
                com.android.server.content.SyncManager.this.scheduleSyncOperationH(syncOperation);
            } catch (java.lang.RuntimeException exc) {
                com.android.server.content.SyncManager.this.mLogger.log("Sync failed with RuntimeException: ", exc.toString());
                closeActiveSyncContext(activeSyncContext);
                android.util.Slog.e("SyncManager", "Caught RuntimeException while starting the sync " + com.android.server.content.SyncLogger.logSafe(syncOperation), exc);
            }
        }

        private void cancelActiveSyncH(com.android.server.content.SyncStorageEngine.EndPoint info, android.os.Bundle extras, java.lang.String why) {
            java.util.ArrayList<com.android.server.content.SyncManager.ActiveSyncContext> activeSyncs = new java.util.ArrayList<>(com.android.server.content.SyncManager.this.mActiveSyncContexts);
            for (com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext : activeSyncs) {
                if (activeSyncContext != null) {
                    com.android.server.content.SyncStorageEngine.EndPoint opInfo = activeSyncContext.mSyncOperation.target;
                    if (opInfo.matchesSpec(info) && (extras == null || activeSyncContext.mSyncOperation.areExtrasEqual(extras, false))) {
                        com.android.server.content.SyncJobService.callJobFinished(activeSyncContext.mSyncOperation.jobId, false, why);
                        runSyncFinishedOrCanceledH(null, activeSyncContext);
                    }
                }
            }
        }

        private void reschedulePeriodicSyncH(com.android.server.content.SyncOperation syncOperation) {
            com.android.server.content.SyncOperation periodicSync = null;
            java.util.List<com.android.server.content.SyncOperation> ops = com.android.server.content.SyncManager.this.getAllPendingSyncs();
            java.util.Iterator<com.android.server.content.SyncOperation> it = ops.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.content.SyncOperation op = it.next();
                if (op.isPeriodic && syncOperation.matchesPeriodicOperation(op)) {
                    periodicSync = op;
                    break;
                }
            }
            if (periodicSync == null) {
                return;
            }
            com.android.server.content.SyncManager.this.scheduleSyncOperationH(periodicSync);
        }

        private void runSyncFinishedOrCanceledH(android.content.SyncResult syncResult, com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext) {
            java.lang.String historyMessage;
            int downstreamActivity;
            int upstreamActivity;
            int downstreamActivity2;
            int upstreamActivity2;
            boolean isLoggable = android.util.Log.isLoggable("SyncManager", 2);
            com.android.server.content.SyncOperation syncOperation = activeSyncContext.mSyncOperation;
            com.android.server.content.SyncStorageEngine.EndPoint info = syncOperation.target;
            if (activeSyncContext.mIsLinkedToDeath) {
                try {
                    activeSyncContext.mSyncAdapter.asBinder().unlinkToDeath(activeSyncContext, 0);
                    activeSyncContext.mIsLinkedToDeath = false;
                } catch (java.util.NoSuchElementException e) {
                    android.util.Slog.wtf("SyncManager", "Failed to unlink active sync adapter to death", e);
                }
            }
            long elapsedTime = android.os.SystemClock.elapsedRealtime() - activeSyncContext.mStartTime;
            com.android.server.content.SyncManager.this.mLogger.log("runSyncFinishedOrCanceledH() op=", syncOperation, " result=", syncResult);
            if (syncResult != null) {
                if (isLoggable) {
                    android.util.Slog.v("SyncManager", "runSyncFinishedOrCanceled [finished]: " + syncOperation + ", result " + syncResult);
                }
                closeActiveSyncContext(activeSyncContext);
                if (!syncOperation.isPeriodic) {
                    com.android.server.content.SyncManager.this.cancelJob(syncOperation, "runSyncFinishedOrCanceledH()-finished");
                }
                if (!syncResult.hasError()) {
                    historyMessage = com.android.server.content.SyncStorageEngine.MESG_SUCCESS;
                    downstreamActivity2 = 0;
                    upstreamActivity2 = 0;
                    com.android.server.content.SyncManager.this.clearBackoffSetting(syncOperation.target, "sync success");
                    if (syncOperation.isDerivedFromFailedPeriodicSync()) {
                        reschedulePeriodicSyncH(syncOperation);
                    }
                } else {
                    android.util.Log.w("SyncManager", "failed sync operation " + com.android.server.content.SyncLogger.logSafe(syncOperation) + ", " + syncResult);
                    syncOperation.retries++;
                    if (syncOperation.retries > com.android.server.content.SyncManager.this.mConstants.getMaxRetriesWithAppStandbyExemption()) {
                        syncOperation.syncExemptionFlag = 0;
                    }
                    com.android.server.content.SyncManager.this.increaseBackoffSetting(syncOperation.target);
                    if (!syncOperation.isPeriodic) {
                        com.android.server.content.SyncManager.this.maybeRescheduleSync(syncResult, syncOperation);
                    } else {
                        com.android.server.content.SyncManager.this.postScheduleSyncMessage(syncOperation.createOneTimeSyncOperation(), 0L);
                    }
                    historyMessage = android.content.ContentResolver.syncErrorToString(syncResultToErrorNumber(syncResult));
                    downstreamActivity2 = 0;
                    upstreamActivity2 = 0;
                }
                com.android.server.content.SyncManager.this.setDelayUntilTime(syncOperation.target, syncResult.delayUntil);
                downstreamActivity = downstreamActivity2;
                upstreamActivity = upstreamActivity2;
            } else {
                if (isLoggable) {
                    android.util.Slog.v("SyncManager", "runSyncFinishedOrCanceled [canceled]: " + syncOperation);
                }
                if (!syncOperation.isPeriodic) {
                    com.android.server.content.SyncManager.this.cancelJob(syncOperation, "runSyncFinishedOrCanceledH()-canceled");
                }
                if (activeSyncContext.mSyncAdapter != null) {
                    try {
                        com.android.server.content.SyncManager.this.mLogger.log("Calling cancelSync for runSyncFinishedOrCanceled ", activeSyncContext, "  adapter=", activeSyncContext.mSyncAdapter);
                        activeSyncContext.mSyncAdapter.cancelSync(activeSyncContext);
                        com.android.server.content.SyncManager.this.mLogger.log("Canceled");
                    } catch (android.os.RemoteException e2) {
                        com.android.server.content.SyncManager.this.mLogger.log("RemoteException ", android.util.Log.getStackTraceString(e2));
                    }
                }
                historyMessage = com.android.server.content.SyncStorageEngine.MESG_CANCELED;
                closeActiveSyncContext(activeSyncContext);
                downstreamActivity = 0;
                upstreamActivity = 0;
            }
            stopSyncEvent(activeSyncContext.mHistoryRowId, syncOperation, historyMessage, upstreamActivity, downstreamActivity, elapsedTime);
            if (syncResult != null && syncResult.tooManyDeletions) {
                installHandleTooManyDeletesNotification(info.account, info.provider, syncResult.stats.numDeletes, info.userId);
            } else {
                com.android.server.content.SyncManager.this.mNotificationMgr.cancelAsUser(java.lang.Integer.toString(info.account.hashCode() ^ info.provider.hashCode()), 18, new android.os.UserHandle(info.userId));
            }
            if (syncResult != null && syncResult.fullSyncRequested) {
                com.android.server.content.SyncManager.this.scheduleSyncOperationH(new com.android.server.content.SyncOperation(info.account, info.userId, syncOperation.owningUid, syncOperation.owningPackage, syncOperation.reason, syncOperation.syncSource, info.provider, new android.os.Bundle(), syncOperation.allowParallelSyncs, syncOperation.syncExemptionFlag));
            }
        }

        private void closeActiveSyncContext(com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext) {
            activeSyncContext.close();
            com.android.server.content.SyncManager.this.mActiveSyncContexts.remove(activeSyncContext);
            com.android.server.content.SyncManager.this.mSyncStorageEngine.removeActiveSync(activeSyncContext.mSyncInfo, activeSyncContext.mSyncOperation.target.userId);
            if (android.util.Log.isLoggable("SyncManager", 2)) {
                android.util.Slog.v("SyncManager", "removing all MESSAGE_MONITOR_SYNC & MESSAGE_SYNC_EXPIRED for " + activeSyncContext.toString());
            }
            com.android.server.content.SyncManager.this.mSyncHandler.removeMessages(8, activeSyncContext);
            com.android.server.content.SyncManager.this.mLogger.log("closeActiveSyncContext: ", activeSyncContext);
        }

        private int syncResultToErrorNumber(android.content.SyncResult syncResult) {
            if (syncResult.syncAlreadyInProgress) {
                return 1;
            }
            if (syncResult.stats.numAuthExceptions > 0) {
                return 2;
            }
            if (syncResult.stats.numIoExceptions > 0) {
                return 3;
            }
            if (syncResult.stats.numParseExceptions > 0) {
                return 4;
            }
            if (syncResult.stats.numConflictDetectedExceptions > 0) {
                return 5;
            }
            if (syncResult.tooManyDeletions) {
                return 6;
            }
            if (syncResult.tooManyRetries) {
                return 7;
            }
            if (syncResult.databaseError) {
                return 8;
            }
            throw new java.lang.IllegalStateException("we are not in an error state, " + syncResult);
        }

        private void installHandleTooManyDeletesNotification(android.accounts.Account account, java.lang.String authority, long numDeletes, int userId) {
            android.content.pm.ProviderInfo providerInfo;
            if (com.android.server.content.SyncManager.this.mNotificationMgr == null || (providerInfo = com.android.server.content.SyncManager.this.mContext.getPackageManager().resolveContentProvider(authority, 0)) == null) {
                return;
            }
            java.lang.CharSequence authorityName = providerInfo.loadLabel(com.android.server.content.SyncManager.this.mContext.getPackageManager());
            android.content.Intent clickIntent = new android.content.Intent(com.android.server.content.SyncManager.this.mContext, (java.lang.Class<?>) android.content.SyncActivityTooManyDeletes.class);
            clickIntent.putExtra("account", account);
            clickIntent.putExtra("authority", authority);
            clickIntent.putExtra("provider", authorityName.toString());
            clickIntent.putExtra("numDeletes", numDeletes);
            if (!isActivityAvailable(clickIntent)) {
                android.util.Log.w("SyncManager", "No activity found to handle too many deletes.");
                return;
            }
            android.os.UserHandle user = new android.os.UserHandle(userId);
            android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivityAsUser(com.android.server.content.SyncManager.this.mContext, 0, clickIntent, android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF, null, user);
            java.lang.CharSequence tooManyDeletesDescFormat = com.android.server.content.SyncManager.this.mContext.getResources().getText(android.R.string.conversation_single_line_name_display);
            android.content.Context contextForUser = com.android.server.content.SyncManager.this.getContextForUser(user);
            android.app.Notification notification = new android.app.Notification.Builder(contextForUser, com.android.internal.notification.SystemNotificationChannels.ACCOUNT).setSmallIcon(android.R.drawable.seekbar_thumb_material_anim).setTicker(com.android.server.content.SyncManager.this.mContext.getString(android.R.string.content_description_sliding_handle)).setWhen(java.lang.System.currentTimeMillis()).setColor(contextForUser.getColor(android.R.color.system_notification_accent_color)).setContentTitle(contextForUser.getString(android.R.string.conversation_single_line_image_placeholder)).setContentText(java.lang.String.format(tooManyDeletesDescFormat.toString(), authorityName)).setContentIntent(pendingIntent).build();
            notification.flags |= 2;
            com.android.server.content.SyncManager.this.mNotificationMgr.notifyAsUser(java.lang.Integer.toString(account.hashCode() ^ authority.hashCode()), 18, notification, user);
        }

        private boolean isActivityAvailable(android.content.Intent intent) {
            android.content.pm.PackageManager pm = com.android.server.content.SyncManager.this.mContext.getPackageManager();
            java.util.List<android.content.pm.ResolveInfo> list = pm.queryIntentActivities(intent, 0);
            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                android.content.pm.ResolveInfo resolveInfo = list.get(i);
                if ((resolveInfo.activityInfo.applicationInfo.flags & 1) != 0) {
                    return true;
                }
            }
            return false;
        }

        public long insertStartSyncEvent(com.android.server.content.SyncOperation syncOperation) {
            long now = java.lang.System.currentTimeMillis();
            android.util.EventLog.writeEvent(2720, syncOperation.toEventLog(0));
            return com.android.server.content.SyncManager.this.mSyncStorageEngine.insertStartSyncEvent(syncOperation, now);
        }

        public void stopSyncEvent(long rowId, com.android.server.content.SyncOperation syncOperation, java.lang.String resultMessage, int upstreamActivity, int downstreamActivity, long elapsedTime) {
            android.util.EventLog.writeEvent(2720, syncOperation.toEventLog(1));
            com.android.server.content.SyncManager.this.mSyncStorageEngine.stopSyncEvent(rowId, elapsedTime, resultMessage, downstreamActivity, upstreamActivity, syncOperation.owningPackage, syncOperation.target.userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSyncStillActiveH(com.android.server.content.SyncManager.ActiveSyncContext activeSyncContext) {
        for (com.android.server.content.SyncManager.ActiveSyncContext sync : this.mActiveSyncContexts) {
            if (sync == activeSyncContext) {
                return true;
            }
        }
        return false;
    }

    public static boolean syncExtrasEquals(android.os.Bundle b1, android.os.Bundle b2, boolean includeSyncSettings) {
        if (b1 == b2) {
            return true;
        }
        if (includeSyncSettings && b1.size() != b2.size()) {
            return false;
        }
        android.os.Bundle bigger = b1.size() > b2.size() ? b1 : b2;
        android.os.Bundle smaller = b1.size() > b2.size() ? b2 : b1;
        for (java.lang.String key : bigger.keySet()) {
            if (includeSyncSettings || !isSyncSetting(key)) {
                if (!smaller.containsKey(key) || !java.util.Objects.equals(bigger.get(key), smaller.get(key))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isSyncSetting(java.lang.String key) {
        if (key == null) {
            return false;
        }
        return key.equals("expedited") || key.equals("schedule_as_expedited_job") || key.equals("ignore_settings") || key.equals("ignore_backoff") || key.equals("do_not_retry") || key.equals("force") || key.equals("upload") || key.equals("deletions_override") || key.equals("discard_deletions") || key.equals("expected_upload") || key.equals("expected_download") || key.equals("sync_priority") || key.equals("allow_metered") || key.equals("initialize");
    }

    static class PrintTable {
        private final int mCols;
        private java.util.ArrayList<java.lang.String[]> mTable = com.google.android.collect.Lists.newArrayList();

        PrintTable(int cols) {
            this.mCols = cols;
        }

        void set(int row, int col, java.lang.Object... values) {
            if (values.length + col > this.mCols) {
                throw new java.lang.IndexOutOfBoundsException("Table only has " + this.mCols + " columns. can't set " + values.length + " at column " + col);
            }
            for (int i = this.mTable.size(); i <= row; i++) {
                java.lang.String[] list = new java.lang.String[this.mCols];
                this.mTable.add(list);
                for (int j = 0; j < this.mCols; j++) {
                    list[j] = "";
                }
            }
            java.lang.String[] rowArray = this.mTable.get(row);
            for (int i2 = 0; i2 < values.length; i2++) {
                java.lang.Object value = values[i2];
                rowArray[col + i2] = value == null ? "" : value.toString();
            }
        }

        void writeTo(java.io.PrintWriter out) {
            java.lang.String[] formats = new java.lang.String[this.mCols];
            int totalLength = 0;
            for (int col = 0; col < this.mCols; col++) {
                int maxLength = 0;
                for (java.lang.Object[] row : this.mTable) {
                    int length = row[col].toString().length();
                    if (length > maxLength) {
                        maxLength = length;
                    }
                }
                totalLength += maxLength;
                formats[col] = java.lang.String.format("%%-%ds", java.lang.Integer.valueOf(maxLength));
            }
            int col2 = this.mCols;
            formats[col2 - 1] = "%s";
            printRow(out, formats, this.mTable.get(0));
            int totalLength2 = totalLength + ((this.mCols - 1) * 2);
            for (int i = 0; i < totalLength2; i++) {
                out.print("-");
            }
            out.println();
            int mTableSize = this.mTable.size();
            for (int i2 = 1; i2 < mTableSize; i2++) {
                java.lang.Object[] row2 = this.mTable.get(i2);
                printRow(out, formats, row2);
            }
        }

        private void printRow(java.io.PrintWriter out, java.lang.String[] formats, java.lang.Object[] row) {
            int rowLength = row.length;
            for (int j = 0; j < rowLength; j++) {
                out.printf(java.lang.String.format(formats[j], row[j].toString()), new java.lang.Object[0]);
                out.print("  ");
            }
            out.println();
        }

        public int getNumRows() {
            return this.mTable.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.Context getContextForUser(android.os.UserHandle user) {
        try {
            return this.mContext.createPackageContextAsUser(this.mContext.getPackageName(), 0, user);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return this.mContext;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelJob(com.android.server.content.SyncOperation op, java.lang.String why) {
        if (op == null) {
            android.util.Slog.wtf("SyncManager", "Null sync operation detected.");
            return;
        }
        if (op.isPeriodic) {
            this.mLogger.log("Removing periodic sync ", op, " for ", why);
        }
        getJobScheduler().cancel(op.jobId);
    }

    public void resetTodayStats() {
        this.mSyncStorageEngine.resetTodayStats(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean wasPackageEverLaunched(java.lang.String packageName, int userId) {
        try {
            return this.mPackageManagerInternal.wasPackageEverLaunched(packageName, userId);
        } catch (java.lang.IllegalArgumentException e) {
            return false;
        }
    }
}
