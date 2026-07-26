package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
class RollbackManagerServiceImpl extends android.content.rollback.IRollbackManager.Stub implements com.android.server.rollback.RollbackManagerInternal {
    private final com.android.server.rollback.AppDataRollbackHelper mAppDataRollbackHelper;
    private final android.content.Context mContext;
    private final java.util.concurrent.Executor mExecutor;
    private final android.os.Handler mHandler;
    private final com.android.server.pm.Installer mInstaller;
    private final com.android.server.rollback.RollbackPackageHealthObserver mPackageHealthObserver;
    private final com.android.server.rollback.RollbackStore mRollbackStore;
    private final android.util.ArrayMap<java.lang.Integer, android.util.Pair<android.content.Context, android.content.BroadcastReceiver>> mUserBroadcastReceivers;
    private static final java.lang.String TAG = "RollbackManager";
    private static final boolean LOCAL_LOGV = android.util.Log.isLoggable(TAG, 2);
    private static final long DEFAULT_ROLLBACK_LIFETIME_DURATION_MILLIS = java.util.concurrent.TimeUnit.DAYS.toMillis(14);
    private static final long HANDLER_THREAD_TIMEOUT_DURATION_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(10);
    private long mRollbackLifetimeDurationInMillis = DEFAULT_ROLLBACK_LIFETIME_DURATION_MILLIS;
    private final java.util.Random mRandom = new java.security.SecureRandom();
    private final android.util.SparseBooleanArray mAllocatedRollbackIds = new android.util.SparseBooleanArray();
    private final java.util.List<com.android.server.rollback.Rollback> mRollbacks = new java.util.ArrayList();
    private final java.lang.Runnable mRunExpiration = new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda7
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.runExpiration();
        }
    };
    private final android.util.LongArrayQueue mSleepDuration = new android.util.LongArrayQueue();
    private long mRelativeBootTime = calculateRelativeBootTime();

    @java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ExtThread {
    }

    RollbackManagerServiceImpl(final android.content.Context context) {
        this.mContext = context;
        this.mInstaller = new com.android.server.pm.Installer(this.mContext);
        this.mInstaller.onStart();
        this.mRollbackStore = new com.android.server.rollback.RollbackStore(new java.io.File(android.os.Environment.getDataDirectory(), "rollback"), new java.io.File(android.os.Environment.getDataDirectory(), "rollback-history"));
        this.mPackageHealthObserver = new com.android.server.rollback.RollbackPackageHealthObserver(this.mContext);
        this.mAppDataRollbackHelper = new com.android.server.rollback.AppDataRollbackHelper(this.mInstaller);
        android.os.HandlerThread handlerThread = new android.os.HandlerThread("RollbackManagerServiceHandler");
        handlerThread.start();
        this.mHandler = new android.os.Handler(handlerThread.getLooper());
        com.android.server.Watchdog.getInstance().addThread(getHandler(), HANDLER_THREAD_TIMEOUT_DURATION_MILLIS);
        this.mExecutor = new android.os.HandlerExecutor(getHandler());
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(context);
            }
        });
        this.mUserBroadcastReceivers = new android.util.ArrayMap<>();
        android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        for (android.os.UserHandle user : userManager.getUserHandles(true)) {
            registerUserCallbacks(user);
        }
        android.content.IntentFilter enableRollbackFilter = new android.content.IntentFilter();
        enableRollbackFilter.addAction("android.intent.action.PACKAGE_ENABLE_ROLLBACK");
        try {
            enableRollbackFilter.addDataType("application/vnd.android.package-archive");
        } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
            android.util.Slog.e(TAG, "addDataType", e);
        }
        this.mContext.registerReceiver(new com.android.server.rollback.RollbackManagerServiceImpl.AnonymousClass1(), enableRollbackFilter, null, getHandler());
        android.content.IntentFilter enableRollbackTimedOutFilter = new android.content.IntentFilter();
        enableRollbackTimedOutFilter.addAction("android.intent.action.CANCEL_ENABLE_ROLLBACK");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.rollback.RollbackManagerServiceImpl.this.assertInWorkerThread();
                if ("android.intent.action.CANCEL_ENABLE_ROLLBACK".equals(intent.getAction())) {
                    int sessionId = intent.getIntExtra(android.content.pm.PackageManagerInternal.EXTRA_ENABLE_ROLLBACK_SESSION_ID, -1);
                    if (com.android.server.rollback.RollbackManagerServiceImpl.LOCAL_LOGV) {
                        android.util.Slog.v(com.android.server.rollback.RollbackManagerServiceImpl.TAG, "broadcast=ACTION_CANCEL_ENABLE_ROLLBACK id=" + sessionId);
                    }
                    com.android.server.rollback.Rollback rollback = com.android.server.rollback.RollbackManagerServiceImpl.this.getRollbackForSession(sessionId);
                    if (rollback != null && rollback.isEnabling()) {
                        com.android.server.rollback.RollbackManagerServiceImpl.this.mRollbacks.remove(rollback);
                        com.android.server.rollback.RollbackManagerServiceImpl.this.deleteRollback(rollback, "Rollback canceled");
                    }
                }
            }
        }, enableRollbackTimedOutFilter, null, getHandler());
        android.content.IntentFilter userIntentFilter = new android.content.IntentFilter();
        userIntentFilter.addAction("android.intent.action.USER_ADDED");
        userIntentFilter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                int newUserId;
                com.android.server.rollback.RollbackManagerServiceImpl.this.assertInWorkerThread();
                if ("android.intent.action.USER_ADDED".equals(intent.getAction())) {
                    int newUserId2 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    if (newUserId2 == -1) {
                        return;
                    }
                    com.android.server.rollback.RollbackManagerServiceImpl.this.registerUserCallbacks(android.os.UserHandle.of(newUserId2));
                    return;
                }
                if (!"android.intent.action.USER_REMOVED".equals(intent.getAction()) || (newUserId = intent.getIntExtra("android.intent.extra.user_handle", -1)) == -1) {
                    return;
                }
                com.android.server.rollback.RollbackManagerServiceImpl.this.unregisterUserCallbacks(android.os.UserHandle.of(newUserId));
            }
        }, userIntentFilter, null, getHandler());
        registerTimeChangeReceiver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.content.Context context) {
        this.mRollbacks.addAll(this.mRollbackStore.loadRollbacks());
        if (!context.getPackageManager().isDeviceUpgrading()) {
            for (com.android.server.rollback.Rollback rollback : this.mRollbacks) {
                this.mAllocatedRollbackIds.put(rollback.info.getRollbackId(), true);
            }
            return;
        }
        for (com.android.server.rollback.Rollback rollback2 : this.mRollbacks) {
            deleteRollback(rollback2, "Fingerprint changed");
        }
        this.mRollbacks.clear();
    }

    /* JADX INFO: renamed from: com.android.server.rollback.RollbackManagerServiceImpl$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.rollback.RollbackManagerServiceImpl.this.assertInWorkerThread();
            if ("android.intent.action.PACKAGE_ENABLE_ROLLBACK".equals(intent.getAction())) {
                final int token = intent.getIntExtra(android.content.pm.PackageManagerInternal.EXTRA_ENABLE_ROLLBACK_TOKEN, -1);
                final int sessionId = intent.getIntExtra(android.content.pm.PackageManagerInternal.EXTRA_ENABLE_ROLLBACK_SESSION_ID, -1);
                com.android.server.rollback.RollbackManagerServiceImpl.this.queueSleepIfNeeded();
                com.android.server.rollback.RollbackManagerServiceImpl.this.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0(sessionId, token);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(int sessionId, int token) {
            com.android.server.rollback.RollbackManagerServiceImpl.this.assertInWorkerThread();
            boolean success = com.android.server.rollback.RollbackManagerServiceImpl.this.enableRollback(sessionId);
            int ret = 1;
            if (!success) {
                ret = -1;
            }
            android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            pm.setEnableRollbackCode(token, ret);
        }
    }

    private <U> U awaitResult(java.util.function.Supplier<U> supplier) {
        assertNotInWorkerThread();
        try {
            return (U) java.util.concurrent.CompletableFuture.supplyAsync(supplier, this.mExecutor).get();
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private void awaitResult(java.lang.Runnable runnable) {
        assertNotInWorkerThread();
        try {
            java.util.concurrent.CompletableFuture.runAsync(runnable, this.mExecutor).get();
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertInWorkerThread() {
        com.android.internal.util.Preconditions.checkState(getHandler().getLooper().isCurrentThread());
    }

    private void assertNotInWorkerThread() {
        com.android.internal.util.Preconditions.checkState(!getHandler().getLooper().isCurrentThread());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerUserCallbacks(android.os.UserHandle user) {
        android.content.Context context = getContextAsUser(user);
        if (context == null) {
            android.util.Slog.e(TAG, "Unable to register user callbacks for user " + user);
            return;
        }
        context.getPackageManager().getPackageInstaller().registerSessionCallback(new com.android.server.rollback.RollbackManagerServiceImpl.SessionCallback(), getHandler());
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        filter.addDataScheme("package");
        android.content.BroadcastReceiver receiver = new android.content.BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.rollback.RollbackManagerServiceImpl.this.assertInWorkerThread();
                java.lang.String action = intent.getAction();
                if ("android.intent.action.PACKAGE_REPLACED".equals(action)) {
                    java.lang.String packageName = intent.getData().getSchemeSpecificPart();
                    if (com.android.server.rollback.RollbackManagerServiceImpl.LOCAL_LOGV) {
                        android.util.Slog.v(com.android.server.rollback.RollbackManagerServiceImpl.TAG, "broadcast=ACTION_PACKAGE_REPLACED pkg=" + packageName);
                    }
                    com.android.server.rollback.RollbackManagerServiceImpl.this.onPackageReplaced(packageName);
                }
                if ("android.intent.action.PACKAGE_FULLY_REMOVED".equals(action)) {
                    java.lang.String packageName2 = intent.getData().getSchemeSpecificPart();
                    android.util.Slog.i(com.android.server.rollback.RollbackManagerServiceImpl.TAG, "broadcast=ACTION_PACKAGE_FULLY_REMOVED pkg=" + packageName2);
                    com.android.server.rollback.RollbackManagerServiceImpl.this.onPackageFullyRemoved(packageName2);
                }
            }
        };
        context.registerReceiver(receiver, filter, null, getHandler());
        this.mUserBroadcastReceivers.put(java.lang.Integer.valueOf(user.getIdentifier()), new android.util.Pair<>(context, receiver));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterUserCallbacks(android.os.UserHandle user) {
        android.util.Pair<android.content.Context, android.content.BroadcastReceiver> pair = this.mUserBroadcastReceivers.get(java.lang.Integer.valueOf(user.getIdentifier()));
        if (pair == null || pair.first == null || pair.second == null) {
            android.util.Slog.e(TAG, "No receiver found for the user" + user);
        } else {
            ((android.content.Context) pair.first).unregisterReceiver((android.content.BroadcastReceiver) pair.second);
            this.mUserBroadcastReceivers.remove(java.lang.Integer.valueOf(user.getIdentifier()));
        }
    }

    public android.content.pm.ParceledListSlice getAvailableRollbacks() {
        assertNotInWorkerThread();
        enforceManageRollbacks("getAvailableRollbacks");
        return (android.content.pm.ParceledListSlice) awaitResult(new java.util.function.Supplier() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda13
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getAvailableRollbacks$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.ParceledListSlice lambda$getAvailableRollbacks$1() {
        assertInWorkerThread();
        java.util.List<android.content.rollback.RollbackInfo> rollbacks = new java.util.ArrayList<>();
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            com.android.server.rollback.Rollback rollback = this.mRollbacks.get(i);
            if (rollback.isAvailable()) {
                rollbacks.add(rollback.info);
            }
        }
        return new android.content.pm.ParceledListSlice(rollbacks);
    }

    public android.content.pm.ParceledListSlice<android.content.rollback.RollbackInfo> getRecentlyCommittedRollbacks() {
        assertNotInWorkerThread();
        enforceManageRollbacks("getRecentlyCommittedRollbacks");
        return (android.content.pm.ParceledListSlice) awaitResult(new java.util.function.Supplier() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getRecentlyCommittedRollbacks$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.ParceledListSlice lambda$getRecentlyCommittedRollbacks$2() {
        assertInWorkerThread();
        java.util.List<android.content.rollback.RollbackInfo> rollbacks = new java.util.ArrayList<>();
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            com.android.server.rollback.Rollback rollback = this.mRollbacks.get(i);
            if (rollback.isCommitted()) {
                rollbacks.add(rollback.info);
            }
        }
        return new android.content.pm.ParceledListSlice(rollbacks);
    }

    public void commitRollback(final int rollbackId, final android.content.pm.ParceledListSlice causePackages, final java.lang.String callerPackageName, final android.content.IntentSender statusReceiver) {
        assertNotInWorkerThread();
        enforceManageRollbacks("commitRollback");
        int callingUid = android.os.Binder.getCallingUid();
        android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        appOps.checkPackage(callingUid, callerPackageName);
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$commitRollback$3(rollbackId, causePackages, callerPackageName, statusReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commitRollback$3(int rollbackId, android.content.pm.ParceledListSlice causePackages, java.lang.String callerPackageName, android.content.IntentSender statusReceiver) {
        commitRollbackInternal(rollbackId, causePackages.getList(), callerPackageName, statusReceiver);
    }

    private void registerTimeChangeReceiver() {
        android.content.BroadcastReceiver timeChangeIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                com.android.server.rollback.RollbackManagerServiceImpl.this.assertInWorkerThread();
                long oldRelativeBootTime = com.android.server.rollback.RollbackManagerServiceImpl.this.mRelativeBootTime;
                com.android.server.rollback.RollbackManagerServiceImpl.this.mRelativeBootTime = com.android.server.rollback.RollbackManagerServiceImpl.calculateRelativeBootTime();
                long timeDifference = com.android.server.rollback.RollbackManagerServiceImpl.this.mRelativeBootTime - oldRelativeBootTime;
                for (com.android.server.rollback.Rollback rollback : com.android.server.rollback.RollbackManagerServiceImpl.this.mRollbacks) {
                    rollback.setTimestamp(rollback.getTimestamp().plusMillis(timeDifference));
                }
            }
        };
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.TIME_SET");
        this.mContext.registerReceiver(timeChangeIntentReceiver, filter, null, getHandler());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long calculateRelativeBootTime() {
        return java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
    }

    private void commitRollbackInternal(int rollbackId, java.util.List<android.content.pm.VersionedPackage> causePackages, java.lang.String callerPackageName, android.content.IntentSender statusReceiver) {
        assertInWorkerThread();
        android.util.Slog.i(TAG, "commitRollback id=" + rollbackId + " caller=" + callerPackageName);
        com.android.server.rollback.Rollback rollback = getRollbackForId(rollbackId);
        if (rollback == null) {
            sendFailure(this.mContext, statusReceiver, 2, "Rollback unavailable");
        } else {
            rollback.commit(this.mContext, causePackages, callerPackageName, statusReceiver);
        }
    }

    public void reloadPersistedData() {
        assertNotInWorkerThread();
        this.mContext.enforceCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS", "reloadPersistedData");
        awaitResult(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reloadPersistedData$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadPersistedData$4() {
        assertInWorkerThread();
        this.mRollbacks.clear();
        this.mRollbacks.addAll(this.mRollbackStore.loadRollbacks());
    }

    private void expireRollbackForPackageInternal(java.lang.String packageName, java.lang.String reason) {
        assertInWorkerThread();
        java.util.Iterator<com.android.server.rollback.Rollback> iter = this.mRollbacks.iterator();
        while (iter.hasNext()) {
            com.android.server.rollback.Rollback rollback = iter.next();
            if (rollback.includesPackage(packageName)) {
                iter.remove();
                deleteRollback(rollback, reason);
            }
        }
    }

    public void expireRollbackForPackage(final java.lang.String packageName) {
        assertNotInWorkerThread();
        this.mContext.enforceCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS", "expireRollbackForPackage");
        awaitResult(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$expireRollbackForPackage$5(packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$expireRollbackForPackage$5(java.lang.String packageName) {
        expireRollbackForPackageInternal(packageName, "Expired by API");
    }

    public void blockRollbackManager(final long millis) {
        assertNotInWorkerThread();
        this.mContext.enforceCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS", "blockRollbackManager");
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$blockRollbackManager$6(millis);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$blockRollbackManager$6(long millis) {
        assertInWorkerThread();
        this.mSleepDuration.addLast(millis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queueSleepIfNeeded() {
        assertInWorkerThread();
        if (this.mSleepDuration.size() == 0) {
            return;
        }
        final long millis = this.mSleepDuration.removeFirst();
        if (millis <= 0) {
            return;
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$queueSleepIfNeeded$7(millis);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$queueSleepIfNeeded$7(long millis) {
        assertInWorkerThread();
        try {
            java.lang.Thread.sleep(millis);
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.IllegalStateException("RollbackManagerHandlerThread interrupted");
        }
    }

    void onUnlockUser(final int userId) {
        assertNotInWorkerThread();
        if (LOCAL_LOGV) {
            android.util.Slog.v(TAG, "onUnlockUser id=" + userId);
        }
        awaitResult(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUnlockUser$8(userId);
            }
        });
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUnlockUser$9(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUnlockUser$8(int userId) {
        assertInWorkerThread();
        java.util.List<com.android.server.rollback.Rollback> rollbacks = new java.util.ArrayList<>(this.mRollbacks);
        for (int i = 0; i < rollbacks.size(); i++) {
            com.android.server.rollback.Rollback rollback = rollbacks.get(i);
            rollback.commitPendingBackupAndRestoreForUser(userId, this.mAppDataRollbackHelper);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: destroyCeSnapshotsForExpiredRollbacks, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnlockUser$9(int userId) {
        int[] rollbackIds = new int[this.mRollbacks.size()];
        for (int i = 0; i < rollbackIds.length; i++) {
            rollbackIds[i] = this.mRollbacks.get(i).info.getRollbackId();
        }
        com.android.server.pm.ApexManager.getInstance().destroyCeSnapshotsNotSpecified(userId, rollbackIds);
        try {
            this.mInstaller.destroyCeSnapshotsNotSpecified(userId, rollbackIds);
        } catch (com.android.server.pm.Installer.InstallerException ie) {
            android.util.Slog.e(TAG, "Failed to delete snapshots for user: " + userId, ie);
        }
    }

    private void updateRollbackLifetimeDurationInMillis() {
        assertInWorkerThread();
        this.mRollbackLifetimeDurationInMillis = android.provider.DeviceConfig.getLong("rollback_boot", "rollback_lifetime_in_millis", DEFAULT_ROLLBACK_LIFETIME_DURATION_MILLIS);
        if (this.mRollbackLifetimeDurationInMillis < 0) {
            this.mRollbackLifetimeDurationInMillis = DEFAULT_ROLLBACK_LIFETIME_DURATION_MILLIS;
        }
        android.util.Slog.d(TAG, "mRollbackLifetimeDurationInMillis=" + this.mRollbackLifetimeDurationInMillis);
        runExpiration();
    }

    void onBootCompleted() {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("rollback_boot", this.mExecutor, new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda14
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$onBootCompleted$10(properties);
            }
        });
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBootCompleted$11();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootCompleted$10(android.provider.DeviceConfig.Properties properties) {
        updateRollbackLifetimeDurationInMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootCompleted$11() {
        assertInWorkerThread();
        updateRollbackLifetimeDurationInMillis();
        runExpiration();
        java.util.List<com.android.server.rollback.Rollback> enabling = new java.util.ArrayList<>();
        java.util.List<com.android.server.rollback.Rollback> restoreInProgress = new java.util.ArrayList<>();
        java.util.Set<java.lang.String> apexPackageNames = new java.util.HashSet<>();
        java.util.Iterator<com.android.server.rollback.Rollback> iter = this.mRollbacks.iterator();
        while (iter.hasNext()) {
            com.android.server.rollback.Rollback rollback = iter.next();
            if (rollback.isStaged()) {
                android.content.pm.PackageInstaller.SessionInfo session = this.mContext.getPackageManager().getPackageInstaller().getSessionInfo(rollback.getOriginalSessionId());
                if (session == null || session.isStagedSessionFailed()) {
                    if (rollback.isEnabling()) {
                        iter.remove();
                        deleteRollback(rollback, "Session " + rollback.getOriginalSessionId() + " not existed or failed");
                    }
                } else {
                    if (session.isStagedSessionApplied()) {
                        if (rollback.isEnabling()) {
                            enabling.add(rollback);
                        } else if (rollback.isRestoreUserDataInProgress()) {
                            restoreInProgress.add(rollback);
                        }
                    }
                    apexPackageNames.addAll(rollback.getApexPackageNames());
                }
            }
        }
        for (com.android.server.rollback.Rollback rollback2 : enabling) {
            makeRollbackAvailable(rollback2);
        }
        for (com.android.server.rollback.Rollback rollback3 : restoreInProgress) {
            rollback3.setRestoreUserDataInProgress(false);
        }
        for (java.lang.String apexPackageName : apexPackageNames) {
            onPackageReplaced(apexPackageName);
        }
        this.mPackageHealthObserver.onBootCompletedAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageReplaced(java.lang.String packageName) {
        assertInWorkerThread();
        long installedVersion = getInstalledPackageVersion(packageName);
        java.util.Iterator<com.android.server.rollback.Rollback> iter = this.mRollbacks.iterator();
        while (iter.hasNext()) {
            com.android.server.rollback.Rollback rollback = iter.next();
            if (rollback.isAvailable() && rollback.includesPackageWithDifferentVersion(packageName, installedVersion)) {
                iter.remove();
                deleteRollback(rollback, "Package " + packageName + " replaced");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageFullyRemoved(java.lang.String packageName) {
        assertInWorkerThread();
        expireRollbackForPackageInternal(packageName, "Package " + packageName + " removed");
    }

    static void sendFailure(android.content.Context context, android.content.IntentSender statusReceiver, int status, java.lang.String message) {
        android.util.Slog.e(TAG, message);
        try {
            android.content.Intent fillIn = new android.content.Intent();
            fillIn.putExtra("android.content.rollback.extra.STATUS", status);
            fillIn.putExtra("android.content.rollback.extra.STATUS_MESSAGE", message);
            statusReceiver.sendIntent(context, 0, fillIn, null, null);
        } catch (android.content.IntentSender.SendIntentException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runExpiration() {
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.rollbackLifetime()) {
            runExpirationCustomRollbackLifetime();
        } else {
            runExpirationDefaultRollbackLifetime();
        }
    }

    private void runExpirationDefaultRollbackLifetime() {
        getHandler().removeCallbacks(this.mRunExpiration);
        assertInWorkerThread();
        java.time.Instant now = java.time.Instant.now();
        java.time.Instant oldest = null;
        java.util.Iterator<com.android.server.rollback.Rollback> iter = this.mRollbacks.iterator();
        while (iter.hasNext()) {
            com.android.server.rollback.Rollback rollback = iter.next();
            if (rollback.isAvailable() || rollback.isCommitted()) {
                java.time.Instant rollbackTimestamp = rollback.getTimestamp();
                if (!now.isBefore(rollbackTimestamp.plusMillis(this.mRollbackLifetimeDurationInMillis))) {
                    android.util.Slog.i(TAG, "runExpiration id=" + rollback.info.getRollbackId());
                    iter.remove();
                    deleteRollback(rollback, "Expired by timeout");
                } else if (oldest == null || oldest.isAfter(rollbackTimestamp)) {
                    oldest = rollbackTimestamp;
                }
            }
        }
        if (oldest != null) {
            long delay = now.until(oldest.plusMillis(this.mRollbackLifetimeDurationInMillis), java.time.temporal.ChronoUnit.MILLIS);
            getHandler().postDelayed(this.mRunExpiration, delay);
        }
    }

    private void runExpirationCustomRollbackLifetime() {
        getHandler().removeCallbacks(this.mRunExpiration);
        assertInWorkerThread();
        java.time.Instant now = java.time.Instant.now();
        long minDelay = 0;
        java.util.Iterator<com.android.server.rollback.Rollback> iter = this.mRollbacks.iterator();
        while (iter.hasNext()) {
            com.android.server.rollback.Rollback rollback = iter.next();
            if (rollback.isAvailable() || rollback.isCommitted()) {
                long rollbackLifetimeMillis = rollback.getRollbackLifetimeMillis();
                if (rollbackLifetimeMillis <= 0) {
                    rollbackLifetimeMillis = this.mRollbackLifetimeDurationInMillis;
                }
                java.time.Instant rollbackExpiryTimestamp = rollback.getTimestamp().plusMillis(rollbackLifetimeMillis);
                if (!now.isBefore(rollbackExpiryTimestamp)) {
                    android.util.Slog.i(TAG, "runExpiration id=" + rollback.info.getRollbackId());
                    iter.remove();
                    deleteRollback(rollback, "Expired by timeout");
                } else {
                    long delay = now.until(rollbackExpiryTimestamp, java.time.temporal.ChronoUnit.MILLIS);
                    if (minDelay == 0 || delay < minDelay) {
                        minDelay = delay;
                    }
                }
            }
        }
        if (minDelay != 0) {
            getHandler().postDelayed(this.mRunExpiration, minDelay);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.Handler getHandler() {
        return this.mHandler;
    }

    private android.content.Context getContextAsUser(android.os.UserHandle user) {
        try {
            return this.mContext.createPackageContextAsUser(this.mContext.getPackageName(), 0, user);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enableRollback(int sessionId) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            android.util.Slog.v(TAG, "enableRollback sessionId=" + sessionId);
        }
        android.content.pm.PackageInstaller installer = this.mContext.getPackageManager().getPackageInstaller();
        android.content.pm.PackageInstaller.SessionInfo packageSession = installer.getSessionInfo(sessionId);
        if (packageSession == null) {
            android.util.Slog.e(TAG, "Unable to find session for enabled rollback.");
            return false;
        }
        android.content.pm.PackageInstaller.SessionInfo parentSession = packageSession.hasParentSessionId() ? installer.getSessionInfo(packageSession.getParentSessionId()) : packageSession;
        if (parentSession == null) {
            android.util.Slog.e(TAG, "Unable to find parent session for enabled rollback.");
            return false;
        }
        com.android.server.rollback.Rollback newRollback = getRollbackForSession(packageSession.getSessionId());
        if (newRollback == null) {
            newRollback = createNewRollback(parentSession);
        }
        if (!enableRollbackForPackageSession(newRollback, packageSession)) {
            return false;
        }
        if (newRollback.allPackagesEnabled()) {
            return completeEnableRollback(newRollback);
        }
        return true;
    }

    private int computeRollbackDataPolicy(int sessionPolicy, int manifestPolicy) {
        assertInWorkerThread();
        if (manifestPolicy != 0) {
            return manifestPolicy;
        }
        return sessionPolicy;
    }

    private boolean enableRollbackForPackageSession(com.android.server.rollback.Rollback rollback, android.content.pm.PackageInstaller.SessionInfo session) {
        assertInWorkerThread();
        int installFlags = session.installFlags;
        if ((262144 & installFlags) == 0) {
            android.util.Slog.e(TAG, "Rollback is not enabled.");
            return false;
        }
        if ((installFlags & 2048) != 0) {
            android.util.Slog.e(TAG, "Rollbacks not supported for instant app install");
            return false;
        }
        if (session.resolvedBaseCodePath == null) {
            android.util.Slog.e(TAG, "Session code path has not been resolved.");
            return false;
        }
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ApkLite> parseResult = android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input.reset(), new java.io.File(session.resolvedBaseCodePath), 0);
        if (parseResult.isError()) {
            android.util.Slog.e(TAG, "Unable to parse new package: " + parseResult.getErrorMessage(), parseResult.getException());
            return false;
        }
        android.content.pm.parsing.ApkLite newPackage = (android.content.pm.parsing.ApkLite) parseResult.getResult();
        java.lang.String packageName = newPackage.getPackageName();
        int rollbackDataPolicy = computeRollbackDataPolicy(session.rollbackDataPolicy, newPackage.getRollbackDataPolicy());
        if (!session.isStaged() && (installFlags & 131072) != 0 && rollbackDataPolicy != 2) {
            android.util.Slog.e(TAG, "Only RETAIN is supported for rebootless APEX: " + packageName);
            return false;
        }
        android.util.Slog.i(TAG, "Enabling rollback for install of " + packageName + ", session:" + session.sessionId + ", rollbackDataPolicy=" + rollbackDataPolicy + ", rollbackId:" + rollback.info.getRollbackId() + ", originalSessionId:" + rollback.getOriginalSessionId());
        java.lang.String installerPackageName = session.getInstallerPackageName();
        if (!enableRollbackAllowed(installerPackageName, packageName)) {
            android.util.Slog.e(TAG, "Installer " + installerPackageName + " is not allowed to enable rollback on " + packageName);
            return false;
        }
        boolean isApex = (installFlags & 131072) != 0;
        try {
            android.content.pm.PackageInfo pkgInfo = getPackageInfo(packageName);
            if (isApex) {
                android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                java.util.List<java.lang.String> apksInApex = pmi.getApksInApex(packageName);
                for (java.lang.String apkInApex : apksInApex) {
                    try {
                        android.content.pm.PackageInfo apkPkgInfo = getPackageInfo(apkInApex);
                        android.content.pm.PackageManagerInternal pmi2 = pmi;
                        if (!rollback.enableForPackageInApex(apkInApex, apkPkgInfo.getLongVersionCode(), rollbackDataPolicy)) {
                            return false;
                        }
                        pmi = pmi2;
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        android.util.Slog.e(TAG, apkInApex + " is not installed");
                        return false;
                    }
                }
            }
            android.content.pm.ApplicationInfo appInfo = pkgInfo.applicationInfo;
            return rollback.enableForPackage(packageName, newPackage.getVersionCode(), pkgInfo.getLongVersionCode(), isApex, appInfo.sourceDir, appInfo.splitSourceDirs, rollbackDataPolicy, session.rollbackImpactLevel);
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            android.util.Slog.e(TAG, packageName + " is not installed");
            return false;
        }
    }

    @Override // com.android.server.rollback.RollbackManagerInternal
    public void snapshotAndRestoreUserData(java.lang.String packageName, java.util.List<android.os.UserHandle> users, int appId, long ceDataInode, java.lang.String seInfo, int token) {
        assertNotInWorkerThread();
        snapshotAndRestoreUserData(packageName, android.os.UserHandle.fromUserHandles(users), appId, ceDataInode, seInfo, token);
    }

    public void snapshotAndRestoreUserData(final java.lang.String packageName, final int[] userIds, final int appId, long ceDataInode, final java.lang.String seInfo, final int token) {
        assertNotInWorkerThread();
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("snapshotAndRestoreUserData may only be called by the system.");
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$snapshotAndRestoreUserData$12(packageName, userIds, appId, seInfo, token);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$snapshotAndRestoreUserData$12(java.lang.String packageName, int[] userIds, int appId, java.lang.String seInfo, int token) {
        assertInWorkerThread();
        snapshotUserDataInternal(packageName, userIds);
        restoreUserDataInternal(packageName, userIds, appId, seInfo);
        if (token > 0) {
            android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            pmi.finishPackageInstall(token, false);
        }
    }

    private void snapshotUserDataInternal(java.lang.String packageName, int[] userIds) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            android.util.Slog.v(TAG, "snapshotUserData pkg=" + packageName + " users=" + java.util.Arrays.toString(userIds));
        }
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            com.android.server.rollback.Rollback rollback = this.mRollbacks.get(i);
            rollback.snapshotUserData(packageName, userIds, this.mAppDataRollbackHelper);
        }
    }

    private void restoreUserDataInternal(java.lang.String packageName, int[] userIds, int appId, java.lang.String seInfo) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            android.util.Slog.v(TAG, "restoreUserData pkg=" + packageName + " users=" + java.util.Arrays.toString(userIds));
        }
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            com.android.server.rollback.Rollback rollback = this.mRollbacks.get(i);
            if (rollback.restoreUserDataForPackageIfInProgress(packageName, userIds, appId, seInfo, this.mAppDataRollbackHelper)) {
                return;
            }
        }
    }

    @Override // com.android.server.rollback.RollbackManagerInternal
    public int notifyStagedSession(final int sessionId) {
        assertNotInWorkerThread();
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("notifyStagedSession may only be called by the system.");
        }
        return ((java.lang.Integer) awaitResult(new java.util.function.Supplier() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$notifyStagedSession$13(sessionId);
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$notifyStagedSession$13(int sessionId) {
        assertInWorkerThread();
        com.android.server.rollback.Rollback rollback = getRollbackForSession(sessionId);
        return java.lang.Integer.valueOf(rollback != null ? rollback.info.getRollbackId() : -1);
    }

    private boolean enableRollbackAllowed(java.lang.String installerPackageName, java.lang.String packageName) {
        if (installerPackageName == null) {
            return false;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        boolean manageRollbacksGranted = pm.checkPermission("android.permission.MANAGE_ROLLBACKS", installerPackageName) == 0;
        boolean testManageRollbacksGranted = pm.checkPermission("android.permission.TEST_MANAGE_ROLLBACKS", installerPackageName) == 0;
        return (isRollbackAllowed(packageName) && manageRollbacksGranted) || testManageRollbacksGranted;
    }

    private boolean isRollbackAllowed(java.lang.String packageName) {
        return com.android.server.SystemConfig.getInstance().getRollbackWhitelistedPackages().contains(packageName) || isModule(packageName);
    }

    private boolean isModule(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            android.content.pm.ModuleInfo moduleInfo = pm.getModuleInfo(packageName, 0);
            return moduleInfo != null;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private long getInstalledPackageVersion(java.lang.String packageName) {
        try {
            android.content.pm.PackageInfo pkgInfo = getPackageInfo(packageName);
            return pkgInfo.getLongVersionCode();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return -1L;
        }
    }

    private android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, 4194304);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return pm.getPackageInfo(packageName, 1073741824);
        }
    }

    private class SessionCallback extends android.content.pm.PackageInstaller.SessionCallback {
        private SessionCallback() {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onCreated(int sessionId) {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onBadgingChanged(int sessionId) {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onActiveChanged(int sessionId, boolean active) {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onProgressChanged(int sessionId, float progress) {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onFinished(int sessionId, boolean success) {
            com.android.server.rollback.RollbackManagerServiceImpl.this.assertInWorkerThread();
            if (com.android.server.rollback.RollbackManagerServiceImpl.LOCAL_LOGV) {
                android.util.Slog.v(com.android.server.rollback.RollbackManagerServiceImpl.TAG, "SessionCallback.onFinished id=" + sessionId + " success=" + success);
            }
            com.android.server.rollback.Rollback rollback = com.android.server.rollback.RollbackManagerServiceImpl.this.getRollbackForSession(sessionId);
            if (rollback == null || !rollback.isEnabling() || sessionId != rollback.getOriginalSessionId()) {
                return;
            }
            if (success) {
                if (!rollback.isStaged() && com.android.server.rollback.RollbackManagerServiceImpl.this.completeEnableRollback(rollback)) {
                    com.android.server.rollback.RollbackManagerServiceImpl.this.makeRollbackAvailable(rollback);
                    return;
                }
                return;
            }
            android.util.Slog.w(com.android.server.rollback.RollbackManagerServiceImpl.TAG, "Delete rollback id=" + rollback.info.getRollbackId() + " for failed session id=" + sessionId);
            com.android.server.rollback.RollbackManagerServiceImpl.this.mRollbacks.remove(rollback);
            com.android.server.rollback.RollbackManagerServiceImpl.this.deleteRollback(rollback, "Session " + sessionId + " failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean completeEnableRollback(com.android.server.rollback.Rollback rollback) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            android.util.Slog.v(TAG, "completeEnableRollback id=" + rollback.info.getRollbackId());
        }
        if (!rollback.allPackagesEnabled()) {
            android.util.Slog.e(TAG, "Failed to enable rollback for all packages in session.");
            this.mRollbacks.remove(rollback);
            deleteRollback(rollback, "Failed to enable rollback for all packages in session");
            return false;
        }
        rollback.saveRollback();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void makeRollbackAvailable(com.android.server.rollback.Rollback rollback) {
        assertInWorkerThread();
        android.util.Slog.i(TAG, "makeRollbackAvailable id=" + rollback.info.getRollbackId());
        rollback.makeAvailable();
        this.mPackageHealthObserver.notifyRollbackAvailable(rollback.info);
        if (!com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection() || rollback.info.getRollbackImpactLevel() == 0) {
            this.mPackageHealthObserver.startObservingHealth(rollback.getPackageNames(), this.mRollbackLifetimeDurationInMillis);
        }
        runExpiration();
    }

    private com.android.server.rollback.Rollback getRollbackForId(int rollbackId) {
        assertInWorkerThread();
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            com.android.server.rollback.Rollback rollback = this.mRollbacks.get(i);
            if (rollback.info.getRollbackId() == rollbackId) {
                return rollback;
            }
        }
        return null;
    }

    private int allocateRollbackId() {
        assertInWorkerThread();
        int n = 0;
        while (true) {
            int rollbackId = this.mRandom.nextInt(2147483646) + 1;
            if (!this.mAllocatedRollbackIds.get(rollbackId, false)) {
                this.mAllocatedRollbackIds.put(rollbackId, true);
                return rollbackId;
            }
            int n2 = n + 1;
            if (n >= 32) {
                throw new java.lang.IllegalStateException("Failed to allocate rollback ID");
            }
            n = n2;
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        assertNotInWorkerThread();
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            final com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
            awaitResult(new java.lang.Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$dump$14(ipw);
                }
            });
            com.android.server.PackageWatchdog.getInstance(this.mContext).dump(ipw);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$14(com.android.internal.util.IndentingPrintWriter ipw) {
        assertInWorkerThread();
        for (com.android.server.rollback.Rollback rollback : this.mRollbacks) {
            rollback.dump(ipw);
        }
        ipw.println();
        java.util.List<com.android.server.rollback.Rollback> historicalRollbacks = this.mRollbackStore.loadHistorialRollbacks();
        if (!historicalRollbacks.isEmpty()) {
            ipw.println("Historical rollbacks:");
            ipw.increaseIndent();
            for (com.android.server.rollback.Rollback rollback2 : historicalRollbacks) {
                rollback2.dump(ipw);
            }
            ipw.decreaseIndent();
            ipw.println();
        }
    }

    private void enforceManageRollbacks(java.lang.String message) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ROLLBACKS") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS") != 0) {
            throw new java.lang.SecurityException(message + " requires android.permission.MANAGE_ROLLBACKS or android.permission.TEST_MANAGE_ROLLBACKS");
        }
    }

    private com.android.server.rollback.Rollback createNewRollback(android.content.pm.PackageInstaller.SessionInfo parentSession) {
        int userId;
        int[] packageSessionIds;
        com.android.server.rollback.Rollback rollback;
        assertInWorkerThread();
        int rollbackId = allocateRollbackId();
        if (parentSession.getUser().equals(android.os.UserHandle.ALL)) {
            userId = android.os.UserHandle.SYSTEM.getIdentifier();
        } else {
            userId = parentSession.getUser().getIdentifier();
        }
        java.lang.String installerPackageName = parentSession.getInstallerPackageName();
        int parentSessionId = parentSession.getSessionId();
        if (LOCAL_LOGV) {
            android.util.Slog.v(TAG, "createNewRollback id=" + rollbackId + " user=" + userId + " installer=" + installerPackageName);
        }
        if (parentSession.isMultiPackage()) {
            packageSessionIds = parentSession.getChildSessionIds();
        } else {
            int[] packageSessionIds2 = {parentSessionId};
            packageSessionIds = packageSessionIds2;
        }
        if (parentSession.isStaged()) {
            rollback = this.mRollbackStore.createStagedRollback(rollbackId, parentSessionId, userId, installerPackageName, packageSessionIds, getExtensionVersions());
        } else {
            rollback = this.mRollbackStore.createNonStagedRollback(rollbackId, parentSessionId, userId, installerPackageName, packageSessionIds, getExtensionVersions());
        }
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.rollbackLifetime()) {
            rollback.setRollbackLifetimeMillis(parentSession.rollbackLifetimeMillis);
        }
        this.mRollbacks.add(rollback);
        return rollback;
    }

    private android.util.SparseIntArray getExtensionVersions() {
        java.util.Map<java.lang.Integer, java.lang.Integer> allExtensionVersions = android.os.ext.SdkExtensions.getAllExtensionVersions();
        android.util.SparseIntArray result = new android.util.SparseIntArray(allExtensionVersions.size());
        java.util.Iterator<java.lang.Integer> it = allExtensionVersions.keySet().iterator();
        while (it.hasNext()) {
            int extension = it.next().intValue();
            result.put(extension, allExtensionVersions.get(java.lang.Integer.valueOf(extension)).intValue());
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.rollback.Rollback getRollbackForSession(int sessionId) {
        assertInWorkerThread();
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            com.android.server.rollback.Rollback rollback = this.mRollbacks.get(i);
            if (rollback.getOriginalSessionId() == sessionId || rollback.containsSessionId(sessionId)) {
                return rollback;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteRollback(com.android.server.rollback.Rollback rollback, java.lang.String reason) {
        assertInWorkerThread();
        rollback.delete(this.mAppDataRollbackHelper, reason);
        this.mRollbackStore.saveRollbackToHistory(rollback);
    }
}
