package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
public final class AppHibernationService extends com.android.server.SystemService {
    private static final long PACKAGE_MATCH_FLAGS = 537698816;
    private static final java.lang.String TAG = "AppHibernationService";
    public static boolean sIsServiceEnabled;
    private final java.util.concurrent.Executor mBackgroundExecutor;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final android.content.Context mContext;
    private final java.util.Map<java.lang.String, com.android.server.apphibernation.GlobalLevelState> mGlobalHibernationStates;
    private final com.android.server.apphibernation.HibernationStateDiskStore<com.android.server.apphibernation.GlobalLevelState> mGlobalLevelHibernationDiskStore;
    private final android.app.IActivityManager mIActivityManager;
    private final android.content.pm.IPackageManager mIPackageManager;
    private final com.android.server.apphibernation.AppHibernationService.Injector mInjector;
    private final com.android.server.apphibernation.AppHibernationManagerInternal mLocalService;
    private final java.lang.Object mLock;
    private final boolean mOatArtifactDeletionEnabled;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.server.apphibernation.AppHibernationService.AppHibernationServiceStub mServiceStub;
    private final android.app.usage.StorageStatsManager mStorageStatsManager;
    private final android.app.usage.UsageStatsManagerInternal.UsageEventListener mUsageEventListener;
    private final android.util.SparseArray<com.android.server.apphibernation.HibernationStateDiskStore<com.android.server.apphibernation.UserLevelState>> mUserDiskStores;
    private final android.os.UserManager mUserManager;
    private final android.util.SparseArray<java.util.Map<java.lang.String, com.android.server.apphibernation.UserLevelState>> mUserStates;

    interface Injector {
        android.app.IActivityManager getActivityManager();

        java.util.concurrent.Executor getBackgroundExecutor();

        android.content.Context getContext();

        com.android.server.apphibernation.HibernationStateDiskStore<com.android.server.apphibernation.GlobalLevelState> getGlobalLevelDiskStore();

        android.content.pm.IPackageManager getPackageManager();

        android.content.pm.PackageManagerInternal getPackageManagerInternal();

        android.app.usage.StorageStatsManager getStorageStatsManager();

        android.app.usage.UsageStatsManagerInternal getUsageStatsManagerInternal();

        com.android.server.apphibernation.HibernationStateDiskStore<com.android.server.apphibernation.UserLevelState> getUserLevelDiskStore(int i);

        android.os.UserManager getUserManager();

        boolean isOatArtifactDeletionEnabled();
    }

    public AppHibernationService(android.content.Context context) {
        this(new com.android.server.apphibernation.AppHibernationService.InjectorImpl(context));
    }

    AppHibernationService(com.android.server.apphibernation.AppHibernationService.Injector injector) {
        super(injector.getContext());
        this.mLock = new java.lang.Object();
        this.mUserStates = new android.util.SparseArray<>();
        this.mUserDiskStores = new android.util.SparseArray<>();
        this.mGlobalHibernationStates = new android.util.ArrayMap();
        this.mLocalService = new com.android.server.apphibernation.AppHibernationService.LocalService(this);
        this.mServiceStub = new com.android.server.apphibernation.AppHibernationService.AppHibernationServiceStub(this);
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.apphibernation.AppHibernationService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                if (userId == -10000) {
                    return;
                }
                java.lang.String action = intent.getAction();
                if ("android.intent.action.PACKAGE_ADDED".equals(action) || "android.intent.action.PACKAGE_REMOVED".equals(action)) {
                    java.lang.String packageName = intent.getData().getSchemeSpecificPart();
                    if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                        return;
                    }
                    if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                        com.android.server.apphibernation.AppHibernationService.this.onPackageAdded(packageName, userId);
                    } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                        com.android.server.apphibernation.AppHibernationService.this.onPackageRemoved(packageName, userId);
                        if (intent.getBooleanExtra("android.intent.extra.REMOVED_FOR_ALL_USERS", false)) {
                            com.android.server.apphibernation.AppHibernationService.this.onPackageRemovedForAllUsers(packageName);
                        }
                    }
                }
            }
        };
        this.mUsageEventListener = new android.app.usage.UsageStatsManagerInternal.UsageEventListener() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda4
            @Override // android.app.usage.UsageStatsManagerInternal.UsageEventListener
            public final void onUsageEvent(int i, android.app.usage.UsageEvents.Event event) {
                this.f$0.lambda$new$6(i, event);
            }
        };
        this.mContext = injector.getContext();
        this.mIPackageManager = injector.getPackageManager();
        this.mPackageManagerInternal = injector.getPackageManagerInternal();
        this.mIActivityManager = injector.getActivityManager();
        this.mUserManager = injector.getUserManager();
        this.mStorageStatsManager = injector.getStorageStatsManager();
        this.mGlobalLevelHibernationDiskStore = injector.getGlobalLevelDiskStore();
        this.mBackgroundExecutor = injector.getBackgroundExecutor();
        this.mOatArtifactDeletionEnabled = injector.isOatArtifactDeletionEnabled();
        this.mInjector = injector;
        android.content.Context userAllContext = this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_ADDED@PACKAGE=NOREPLACING");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        userAllContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
        com.android.server.LocalServices.addService(com.android.server.apphibernation.AppHibernationManagerInternal.class, this.mLocalService);
        this.mInjector.getUsageStatsManagerInternal().registerListener(this.mUsageEventListener);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("app_hibernation", this.mServiceStub);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 1000) {
            this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onBootPhase$0();
                }
            });
        }
        if (phase == 500) {
            sIsServiceEnabled = isDeviceConfigAppHibernationEnabled();
            android.provider.DeviceConfig.addOnPropertiesChangedListener("app_hibernation", android.app.ActivityThread.currentApplication().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda6
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.onDeviceConfigChanged(properties);
                }
            });
            android.app.StatsManager statsManager = (android.app.StatsManager) getContext().getSystemService(android.app.StatsManager.class);
            com.android.server.apphibernation.AppHibernationService.StatsPullAtomCallbackImpl pullAtomCallback = new com.android.server.apphibernation.AppHibernationService.StatsPullAtomCallbackImpl();
            statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.USER_LEVEL_HIBERNATED_APPS, (android.app.StatsManager.PullAtomMetadata) null, this.mBackgroundExecutor, pullAtomCallback);
            statsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.GLOBAL_HIBERNATED_APPS, (android.app.StatsManager.PullAtomMetadata) null, this.mBackgroundExecutor, pullAtomCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0() {
        java.util.List<com.android.server.apphibernation.GlobalLevelState> states = this.mGlobalLevelHibernationDiskStore.readHibernationStates();
        synchronized (this.mLock) {
            initializeGlobalHibernationStates(states);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOatArtifactDeletionEnabled() {
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_APP_HIBERNATION", "Caller does not have MANAGE_APP_HIBERNATION permission.");
        return this.mOatArtifactDeletionEnabled;
    }

    boolean isHibernatingForUser(java.lang.String packageName, int userId) {
        if (!sIsServiceEnabled) {
            return false;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_APP_HIBERNATION", "Caller did not have permission while calling isHibernatingForUser");
        int userId2 = handleIncomingUser(userId, "isHibernatingForUser");
        synchronized (this.mLock) {
            if (!checkUserStatesExist(userId2, "isHibernatingForUser", false)) {
                return false;
            }
            java.util.Map<java.lang.String, com.android.server.apphibernation.UserLevelState> packageStates = this.mUserStates.get(userId2);
            if (packageStates == null) {
                return false;
            }
            com.android.server.apphibernation.UserLevelState pkgState = packageStates.get(packageName);
            if (pkgState != null && this.mPackageManagerInternal.canQueryPackage(android.os.Binder.getCallingUid(), packageName)) {
                return pkgState.hibernated;
            }
            return false;
        }
    }

    boolean isHibernatingGlobally(java.lang.String packageName) {
        if (!sIsServiceEnabled) {
            return false;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_APP_HIBERNATION", "Caller does not have MANAGE_APP_HIBERNATION permission.");
        synchronized (this.mLock) {
            com.android.server.apphibernation.GlobalLevelState state = this.mGlobalHibernationStates.get(packageName);
            if (state != null && this.mPackageManagerInternal.canQueryPackage(android.os.Binder.getCallingUid(), packageName)) {
                return state.hibernated;
            }
            return false;
        }
    }

    void setHibernatingForUser(final java.lang.String packageName, int userId, boolean isHibernating) {
        if (!sIsServiceEnabled) {
            return;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_APP_HIBERNATION", "Caller does not have MANAGE_APP_HIBERNATION permission.");
        final int realUserId = handleIncomingUser(userId, "setHibernatingForUser");
        synchronized (this.mLock) {
            if (checkUserStatesExist(realUserId, "setHibernatingForUser", true)) {
                java.util.Map<java.lang.String, com.android.server.apphibernation.UserLevelState> packageStates = this.mUserStates.get(realUserId);
                final com.android.server.apphibernation.UserLevelState pkgState = packageStates.get(packageName);
                if (pkgState != null && this.mPackageManagerInternal.canQueryPackage(android.os.Binder.getCallingUid(), packageName)) {
                    if (pkgState.hibernated == isHibernating) {
                        return;
                    }
                    pkgState.hibernated = isHibernating;
                    if (isHibernating) {
                        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$setHibernatingForUser$1(packageName, realUserId, pkgState);
                            }
                        });
                    } else {
                        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() throws android.os.RemoteException {
                                this.f$0.lambda$setHibernatingForUser$2(packageName, realUserId);
                            }
                        });
                        pkgState.lastUnhibernatedMs = java.lang.System.currentTimeMillis();
                    }
                    final com.android.server.apphibernation.UserLevelState stateSnapshot = new com.android.server.apphibernation.UserLevelState(pkgState);
                    this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.apphibernation.UserLevelState userLevelState = stateSnapshot;
                            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.USER_LEVEL_HIBERNATION_STATE_CHANGED, userLevelState.packageName, realUserId, userLevelState.hibernated);
                        }
                    });
                    java.util.List<com.android.server.apphibernation.UserLevelState> states = new java.util.ArrayList<>(this.mUserStates.get(realUserId).values());
                    this.mUserDiskStores.get(realUserId).scheduleWriteHibernationStates(states);
                    return;
                }
                android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Package %s is not installed for user %s", new java.lang.Object[]{packageName, java.lang.Integer.valueOf(realUserId)}));
            }
        }
    }

    void setHibernatingGlobally(final java.lang.String packageName, boolean isHibernating) {
        if (!sIsServiceEnabled) {
            return;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_APP_HIBERNATION", "Caller does not have MANAGE_APP_HIBERNATION permission.");
        synchronized (this.mLock) {
            final com.android.server.apphibernation.GlobalLevelState state = this.mGlobalHibernationStates.get(packageName);
            if (state != null && this.mPackageManagerInternal.canQueryPackage(android.os.Binder.getCallingUid(), packageName)) {
                if (state.hibernated != isHibernating) {
                    state.hibernated = isHibernating;
                    if (isHibernating) {
                        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$setHibernatingGlobally$4(packageName, state);
                            }
                        });
                    } else {
                        state.savedByte = 0L;
                        state.lastUnhibernatedMs = java.lang.System.currentTimeMillis();
                    }
                    java.util.List<com.android.server.apphibernation.GlobalLevelState> states = new java.util.ArrayList<>(this.mGlobalHibernationStates.values());
                    this.mGlobalLevelHibernationDiskStore.scheduleWriteHibernationStates(states);
                }
                return;
            }
            android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Package %s is not installed for any user", new java.lang.Object[]{packageName}));
        }
    }

    java.util.List<java.lang.String> getHibernatingPackagesForUser(int userId) {
        java.util.ArrayList<java.lang.String> hibernatingPackages = new java.util.ArrayList<>();
        if (!sIsServiceEnabled) {
            return hibernatingPackages;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_APP_HIBERNATION", "Caller does not have MANAGE_APP_HIBERNATION permission.");
        int userId2 = handleIncomingUser(userId, "getHibernatingPackagesForUser");
        synchronized (this.mLock) {
            if (!checkUserStatesExist(userId2, "getHibernatingPackagesForUser", true)) {
                return hibernatingPackages;
            }
            java.util.Map<java.lang.String, com.android.server.apphibernation.UserLevelState> userStates = this.mUserStates.get(userId2);
            for (com.android.server.apphibernation.UserLevelState state : userStates.values()) {
                java.lang.String packageName = state.packageName;
                if (this.mPackageManagerInternal.canQueryPackage(android.os.Binder.getCallingUid(), packageName)) {
                    if (state.hibernated) {
                        hibernatingPackages.add(state.packageName);
                    }
                }
            }
            return hibernatingPackages;
        }
    }

    public java.util.Map<java.lang.String, android.apphibernation.HibernationStats> getHibernationStatsForUser(java.util.Set<java.lang.String> packageNames, int userId) {
        java.util.Map<java.lang.String, android.apphibernation.HibernationStats> statsMap = new android.util.ArrayMap<>();
        if (!sIsServiceEnabled) {
            return statsMap;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_APP_HIBERNATION", "Caller does not have MANAGE_APP_HIBERNATION permission.");
        int userId2 = handleIncomingUser(userId, "getHibernationStatsForUser");
        synchronized (this.mLock) {
            if (!checkUserStatesExist(userId2, "getHibernationStatsForUser", true)) {
                return statsMap;
            }
            java.util.Map<java.lang.String, com.android.server.apphibernation.UserLevelState> userPackageStates = this.mUserStates.get(userId2);
            java.util.Set<java.lang.String> pkgs = packageNames != null ? packageNames : userPackageStates.keySet();
            for (java.lang.String pkgName : pkgs) {
                if (this.mPackageManagerInternal.canQueryPackage(android.os.Binder.getCallingUid(), pkgName)) {
                    if (!this.mGlobalHibernationStates.containsKey(pkgName) || !userPackageStates.containsKey(pkgName)) {
                        android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("No hibernation state associated with package %s user %d. Maybethe package was uninstalled? ", new java.lang.Object[]{pkgName, java.lang.Integer.valueOf(userId2)}));
                    } else {
                        long diskBytesSaved = this.mGlobalHibernationStates.get(pkgName).savedByte + userPackageStates.get(pkgName).savedByte;
                        android.apphibernation.HibernationStats stats = new android.apphibernation.HibernationStats(diskBytesSaved);
                        statsMap.put(pkgName, stats);
                    }
                }
            }
            return statsMap;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: hibernatePackageForUser, reason: merged with bridge method [inline-methods] */
    public void lambda$setHibernatingForUser$1(java.lang.String packageName, int userId, com.android.server.apphibernation.UserLevelState state) {
        android.os.Trace.traceBegin(524288L, "hibernatePackage");
        long caller = android.os.Binder.clearCallingIdentity();
        try {
            try {
                try {
                    android.content.pm.ApplicationInfo info = this.mIPackageManager.getApplicationInfo(packageName, PACKAGE_MATCH_FLAGS, userId);
                    android.app.usage.StorageStats stats = this.mStorageStatsManager.queryStatsForPackage(info.storageUuid, packageName, new android.os.UserHandle(userId));
                    if (android.app.Flags.appRestrictionsApi()) {
                        noteHibernationChange(packageName, info.uid, true);
                    }
                    this.mIActivityManager.forceStopPackage(packageName, userId);
                    this.mIPackageManager.deleteApplicationCacheFilesAsUser(packageName, userId, (android.content.pm.IPackageDataObserver) null);
                    synchronized (this.mLock) {
                        state.savedByte = stats.getCacheBytes();
                    }
                } catch (android.os.RemoteException e) {
                    throw new java.lang.IllegalStateException("Failed to hibernate due to manager not being available", e);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                android.util.Slog.e(TAG, "Package name not found when querying storage stats", e2);
            } catch (java.io.IOException e3) {
                android.util.Slog.e(TAG, "Storage device not found", e3);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(caller);
            android.os.Trace.traceEnd(524288L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: unhibernatePackageForUser, reason: merged with bridge method [inline-methods] */
    public void lambda$setHibernatingForUser$2(java.lang.String packageName, int userId) throws android.os.RemoteException {
        android.os.Trace.traceBegin(524288L, "unhibernatePackage");
        long caller = android.os.Binder.clearCallingIdentity();
        try {
            try {
                if (android.app.Flags.appRestrictionsApi()) {
                    try {
                        android.content.pm.ApplicationInfo info = this.mIPackageManager.getApplicationInfo(packageName, PACKAGE_MATCH_FLAGS, userId);
                        noteHibernationChange(packageName, info.uid, false);
                    } catch (android.os.RemoteException e) {
                        e = e;
                        throw e.rethrowFromSystemServer();
                    } catch (java.lang.Throwable th) {
                        e = th;
                        android.os.Binder.restoreCallingIdentity(caller);
                        android.os.Trace.traceEnd(524288L);
                        throw e;
                    }
                }
                android.content.Intent lockedBcIntent = new android.content.Intent("android.intent.action.LOCKED_BOOT_COMPLETED").setPackage(packageName);
                java.lang.String[] requiredPermissions = {"android.permission.RECEIVE_BOOT_COMPLETED"};
                this.mIActivityManager.broadcastIntentWithFeature((android.app.IApplicationThread) null, (java.lang.String) null, lockedBcIntent, (java.lang.String) null, (android.content.IIntentReceiver) null, -1, (java.lang.String) null, (android.os.Bundle) null, requiredPermissions, (java.lang.String[]) null, (java.lang.String[]) null, -1, (android.os.Bundle) null, false, false, userId);
                android.content.Intent bcIntent = new android.content.Intent("android.intent.action.BOOT_COMPLETED").setPackage(packageName);
                this.mIActivityManager.broadcastIntentWithFeature((android.app.IApplicationThread) null, (java.lang.String) null, bcIntent, (java.lang.String) null, (android.content.IIntentReceiver) null, -1, (java.lang.String) null, (android.os.Bundle) null, requiredPermissions, (java.lang.String[]) null, (java.lang.String[]) null, -1, (android.os.Bundle) null, false, false, userId);
                android.os.Binder.restoreCallingIdentity(caller);
                android.os.Trace.traceEnd(524288L);
            } catch (android.os.RemoteException e2) {
                e = e2;
            }
        } catch (java.lang.Throwable th2) {
            e = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: hibernatePackageGlobally, reason: merged with bridge method [inline-methods] */
    public void lambda$setHibernatingGlobally$4(java.lang.String packageName, com.android.server.apphibernation.GlobalLevelState state) {
        android.os.Trace.traceBegin(524288L, "hibernatePackageGlobally");
        long savedBytes = 0;
        if (this.mOatArtifactDeletionEnabled) {
            savedBytes = java.lang.Math.max(this.mPackageManagerInternal.deleteOatArtifactsOfPackage(packageName), 0L);
        }
        synchronized (this.mLock) {
            state.savedByte = savedBytes;
        }
        android.os.Trace.traceEnd(524288L);
    }

    private void noteHibernationChange(java.lang.String packageName, int uid, boolean hibernated) {
        if (hibernated) {
            try {
                this.mIActivityManager.noteAppRestrictionEnabled(packageName, uid, 60, true, 2, (java.lang.String) null, 3, 7776000000L);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Couldn't set restriction state change");
            }
        }
    }

    private void initializeUserHibernationStates(int userId, java.util.List<com.android.server.apphibernation.UserLevelState> diskStates) {
        try {
            java.util.List<android.content.pm.PackageInfo> packages = this.mIPackageManager.getInstalledPackages(PACKAGE_MATCH_FLAGS, userId).getList();
            java.util.Map<java.lang.String, com.android.server.apphibernation.UserLevelState> userLevelStates = new android.util.ArrayMap<>();
            int size = packages.size();
            for (int i = 0; i < size; i++) {
                java.lang.String packageName = packages.get(i).packageName;
                com.android.server.apphibernation.UserLevelState state = new com.android.server.apphibernation.UserLevelState();
                state.packageName = packageName;
                userLevelStates.put(packageName, state);
            }
            if (diskStates != null) {
                java.util.Map<java.lang.String, android.content.pm.PackageInfo> installedPackages = new android.util.ArrayMap<>();
                int size2 = packages.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    installedPackages.put(packages.get(i2).packageName, packages.get(i2));
                }
                int size3 = diskStates.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    java.lang.String packageName2 = diskStates.get(i3).packageName;
                    android.content.pm.PackageInfo pkgInfo = installedPackages.get(packageName2);
                    com.android.server.apphibernation.UserLevelState currentState = diskStates.get(i3);
                    if (pkgInfo == null) {
                        android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("No hibernation state associated with package %s user %d. Maybethe package was uninstalled? ", new java.lang.Object[]{packageName2, java.lang.Integer.valueOf(userId)}));
                    } else {
                        if (pkgInfo.applicationInfo != null) {
                            android.content.pm.ApplicationInfo applicationInfo = pkgInfo.applicationInfo;
                            int i4 = applicationInfo.flags & 2097152;
                            applicationInfo.flags = i4;
                            if (i4 == 0 && currentState.hibernated) {
                                currentState.hibernated = false;
                                currentState.lastUnhibernatedMs = java.lang.System.currentTimeMillis();
                            }
                        }
                        userLevelStates.put(packageName2, currentState);
                    }
                }
            }
            this.mUserStates.put(userId, userLevelStates);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Package manager not available", e);
        }
    }

    private void initializeGlobalHibernationStates(java.util.List<com.android.server.apphibernation.GlobalLevelState> diskStates) {
        try {
            java.util.List<android.content.pm.PackageInfo> packages = this.mIPackageManager.getInstalledPackages(541893120L, 0).getList();
            int size = packages.size();
            for (int i = 0; i < size; i++) {
                java.lang.String packageName = packages.get(i).packageName;
                com.android.server.apphibernation.GlobalLevelState state = new com.android.server.apphibernation.GlobalLevelState();
                state.packageName = packageName;
                this.mGlobalHibernationStates.put(packageName, state);
            }
            if (diskStates != null) {
                java.util.Set<java.lang.String> installedPackages = new android.util.ArraySet<>();
                int size2 = packages.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    installedPackages.add(packages.get(i2).packageName);
                }
                int size3 = diskStates.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    com.android.server.apphibernation.GlobalLevelState state2 = diskStates.get(i3);
                    if (!installedPackages.contains(state2.packageName)) {
                        android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("No hibernation state associated with package %s. Maybe the package was uninstalled? ", new java.lang.Object[]{state2.packageName}));
                    } else {
                        this.mGlobalHibernationStates.put(state2.packageName, state2);
                    }
                }
            }
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Package manager not available", e);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        final int userId = user.getUserIdentifier();
        final com.android.server.apphibernation.HibernationStateDiskStore<com.android.server.apphibernation.UserLevelState> diskStore = this.mInjector.getUserLevelDiskStore(userId);
        this.mUserDiskStores.put(userId, diskStore);
        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.apphibernation.AppHibernationService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserUnlocking$5(diskStore, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserUnlocking$5(com.android.server.apphibernation.HibernationStateDiskStore diskStore, int userId) {
        java.util.List<com.android.server.apphibernation.UserLevelState> storedStates = diskStore.readHibernationStates();
        synchronized (this.mLock) {
            if (this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
                initializeUserHibernationStates(userId, storedStates);
                for (com.android.server.apphibernation.UserLevelState userState : this.mUserStates.get(userId).values()) {
                    java.lang.String pkgName = userState.packageName;
                    com.android.server.apphibernation.GlobalLevelState globalState = this.mGlobalHibernationStates.get(pkgName);
                    if (globalState.hibernated && !userState.hibernated) {
                        setHibernatingGlobally(pkgName, false);
                    }
                }
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        int userId = user.getUserIdentifier();
        synchronized (this.mLock) {
            this.mUserDiskStores.remove(userId);
            this.mUserStates.remove(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageAdded(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            if (this.mUserStates.contains(userId)) {
                com.android.server.apphibernation.UserLevelState userState = new com.android.server.apphibernation.UserLevelState();
                userState.packageName = packageName;
                this.mUserStates.get(userId).put(packageName, userState);
                if (!this.mGlobalHibernationStates.containsKey(packageName)) {
                    com.android.server.apphibernation.GlobalLevelState globalState = new com.android.server.apphibernation.GlobalLevelState();
                    globalState.packageName = packageName;
                    this.mGlobalHibernationStates.put(packageName, globalState);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemoved(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            if (this.mUserStates.contains(userId)) {
                this.mUserStates.get(userId).remove(packageName);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemovedForAllUsers(java.lang.String packageName) {
        synchronized (this.mLock) {
            this.mGlobalHibernationStates.remove(packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDeviceConfigChanged(android.provider.DeviceConfig.Properties properties) {
        for (java.lang.String key : properties.getKeyset()) {
            if (android.text.TextUtils.equals("app_hibernation_enabled", key)) {
                sIsServiceEnabled = isDeviceConfigAppHibernationEnabled();
                android.util.Slog.d(TAG, "App hibernation changed to enabled=" + sIsServiceEnabled);
                return;
            }
        }
    }

    private int handleIncomingUser(int userId, java.lang.String name) {
        int callingUid = android.os.Binder.getCallingUid();
        try {
            return this.mIActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, true, name, (java.lang.String) null);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    private boolean checkUserStatesExist(int userId, java.lang.String methodName, boolean shouldLog) {
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            if (shouldLog) {
                android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Attempt to call %s on stopped or nonexistent user %d", new java.lang.Object[]{methodName, java.lang.Integer.valueOf(userId)}));
            }
            return false;
        }
        if (!this.mUserStates.contains(userId)) {
            if (shouldLog) {
                android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Attempt to call %s before states have been read from disk", new java.lang.Object[]{methodName}));
            }
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dump(java.io.PrintWriter pw) {
        if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(getContext(), TAG, pw)) {
            com.android.internal.util.IndentingPrintWriter idpw = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
            synchronized (this.mLock) {
                int userCount = this.mUserStates.size();
                for (int i = 0; i < userCount; i++) {
                    int userId = this.mUserStates.keyAt(i);
                    idpw.print("User Level Hibernation States, ");
                    idpw.printPair("user", java.lang.Integer.valueOf(userId));
                    idpw.println();
                    java.util.Map<java.lang.String, com.android.server.apphibernation.UserLevelState> stateMap = this.mUserStates.get(userId);
                    idpw.increaseIndent();
                    for (com.android.server.apphibernation.UserLevelState state : stateMap.values()) {
                        idpw.print(state);
                        idpw.println();
                    }
                    idpw.decreaseIndent();
                }
                idpw.println();
                idpw.print("Global Level Hibernation States");
                idpw.println();
                for (com.android.server.apphibernation.GlobalLevelState state2 : this.mGlobalHibernationStates.values()) {
                    idpw.print(state2);
                    idpw.println();
                }
            }
        }
    }

    private static final class LocalService extends com.android.server.apphibernation.AppHibernationManagerInternal {
        private final com.android.server.apphibernation.AppHibernationService mService;

        LocalService(com.android.server.apphibernation.AppHibernationService service) {
            this.mService = service;
        }

        @Override // com.android.server.apphibernation.AppHibernationManagerInternal
        public boolean isHibernatingForUser(java.lang.String packageName, int userId) {
            return this.mService.isHibernatingForUser(packageName, userId);
        }

        @Override // com.android.server.apphibernation.AppHibernationManagerInternal
        public void setHibernatingForUser(java.lang.String packageName, int userId, boolean isHibernating) {
            this.mService.setHibernatingForUser(packageName, userId, isHibernating);
        }

        @Override // com.android.server.apphibernation.AppHibernationManagerInternal
        public void setHibernatingGlobally(java.lang.String packageName, boolean isHibernating) {
            this.mService.setHibernatingGlobally(packageName, isHibernating);
        }

        @Override // com.android.server.apphibernation.AppHibernationManagerInternal
        public boolean isHibernatingGlobally(java.lang.String packageName) {
            return this.mService.isHibernatingGlobally(packageName);
        }

        @Override // com.android.server.apphibernation.AppHibernationManagerInternal
        public boolean isOatArtifactDeletionEnabled() {
            return this.mService.isOatArtifactDeletionEnabled();
        }
    }

    static final class AppHibernationServiceStub extends android.apphibernation.IAppHibernationService.Stub {
        final com.android.server.apphibernation.AppHibernationService mService;

        AppHibernationServiceStub(com.android.server.apphibernation.AppHibernationService service) {
            this.mService = service;
        }

        public boolean isHibernatingForUser(java.lang.String packageName, int userId) {
            return this.mService.isHibernatingForUser(packageName, userId);
        }

        public void setHibernatingForUser(java.lang.String packageName, int userId, boolean isHibernating) {
            this.mService.setHibernatingForUser(packageName, userId, isHibernating);
        }

        public void setHibernatingGlobally(java.lang.String packageName, boolean isHibernating) {
            this.mService.setHibernatingGlobally(packageName, isHibernating);
        }

        public boolean isHibernatingGlobally(java.lang.String packageName) {
            return this.mService.isHibernatingGlobally(packageName);
        }

        public java.util.List<java.lang.String> getHibernatingPackagesForUser(int userId) {
            return this.mService.getHibernatingPackagesForUser(userId);
        }

        public java.util.Map<java.lang.String, android.apphibernation.HibernationStats> getHibernationStatsForUser(java.util.List<java.lang.String> packageNames, int userId) {
            java.util.Set<java.lang.String> pkgsSet = packageNames != null ? new android.util.ArraySet<>(packageNames) : null;
            return this.mService.getHibernationStatsForUser(pkgsSet, userId);
        }

        public boolean isOatArtifactDeletionEnabled() {
            return this.mService.isOatArtifactDeletionEnabled();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.apphibernation.AppHibernationShellCommand(this.mService).exec(this, in, out, err, args, callback, resultReceiver);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
            this.mService.dump(fout);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(int userId, android.app.usage.UsageEvents.Event event) {
        if (!isAppHibernationEnabled()) {
            return;
        }
        int eventType = event.mEventType;
        if (eventType == 7 || eventType == 1 || eventType == 31) {
            java.lang.String pkgName = event.mPackage;
            setHibernatingForUser(pkgName, userId, false);
            setHibernatingGlobally(pkgName, false);
        }
    }

    public static boolean isAppHibernationEnabled() {
        return sIsServiceEnabled;
    }

    private static boolean isDeviceConfigAppHibernationEnabled() {
        return android.provider.DeviceConfig.getBoolean("app_hibernation", "app_hibernation_enabled", true);
    }

    private static final class InjectorImpl implements com.android.server.apphibernation.AppHibernationService.Injector {
        private static final java.lang.String HIBERNATION_DIR_NAME = "hibernation";
        private final android.content.Context mContext;
        private final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService = java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        private final com.android.server.apphibernation.UserLevelHibernationProto mUserLevelHibernationProto = new com.android.server.apphibernation.UserLevelHibernationProto();

        InjectorImpl(android.content.Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public android.content.Context getContext() {
            return this.mContext;
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public android.content.pm.IPackageManager getPackageManager() {
            return android.content.pm.IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public android.content.pm.PackageManagerInternal getPackageManagerInternal() {
            return (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public android.app.IActivityManager getActivityManager() {
            return android.app.ActivityManager.getService();
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public android.os.UserManager getUserManager() {
            return (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public android.app.usage.StorageStatsManager getStorageStatsManager() {
            return (android.app.usage.StorageStatsManager) this.mContext.getSystemService(android.app.usage.StorageStatsManager.class);
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public java.util.concurrent.Executor getBackgroundExecutor() {
            return this.mScheduledExecutorService;
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public android.app.usage.UsageStatsManagerInternal getUsageStatsManagerInternal() {
            return (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public com.android.server.apphibernation.HibernationStateDiskStore<com.android.server.apphibernation.GlobalLevelState> getGlobalLevelDiskStore() {
            java.io.File dir = new java.io.File(android.os.Environment.getDataSystemDirectory(), HIBERNATION_DIR_NAME);
            return new com.android.server.apphibernation.HibernationStateDiskStore<>(dir, new com.android.server.apphibernation.GlobalLevelHibernationProto(), this.mScheduledExecutorService);
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public com.android.server.apphibernation.HibernationStateDiskStore<com.android.server.apphibernation.UserLevelState> getUserLevelDiskStore(int userId) {
            java.io.File dir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), HIBERNATION_DIR_NAME);
            return new com.android.server.apphibernation.HibernationStateDiskStore<>(dir, this.mUserLevelHibernationProto, this.mScheduledExecutorService);
        }

        @Override // com.android.server.apphibernation.AppHibernationService.Injector
        public boolean isOatArtifactDeletionEnabled() {
            return this.mContext.getResources().getBoolean(android.R.bool.config_flexibleSplitRatios);
        }
    }

    private final class StatsPullAtomCallbackImpl implements android.app.StatsManager.StatsPullAtomCallback {
        private static final int MEGABYTE_IN_BYTES = 1000000;

        private StatsPullAtomCallbackImpl() {
        }

        public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
            if (!com.android.server.apphibernation.AppHibernationService.isAppHibernationEnabled() && (atomTag == 10107 || atomTag == 10109)) {
                return 0;
            }
            switch (atomTag) {
                case com.android.internal.util.FrameworkStatsLog.USER_LEVEL_HIBERNATED_APPS /* 10107 */:
                    java.util.List<android.content.pm.UserInfo> userInfos = com.android.server.apphibernation.AppHibernationService.this.mUserManager.getAliveUsers();
                    int numUsers = userInfos.size();
                    for (int i = 0; i < numUsers; i++) {
                        int userId = userInfos.get(i).id;
                        if (com.android.server.apphibernation.AppHibernationService.this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
                            data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, com.android.server.apphibernation.AppHibernationService.this.getHibernatingPackagesForUser(userId).size(), userId));
                        }
                    }
                    return 0;
                case 10108:
                default:
                    return 1;
                case com.android.internal.util.FrameworkStatsLog.GLOBAL_HIBERNATED_APPS /* 10109 */:
                    int hibernatedAppCount = 0;
                    long storage_saved_byte = 0;
                    synchronized (com.android.server.apphibernation.AppHibernationService.this.mLock) {
                        for (com.android.server.apphibernation.GlobalLevelState state : com.android.server.apphibernation.AppHibernationService.this.mGlobalHibernationStates.values()) {
                            if (state.hibernated) {
                                hibernatedAppCount++;
                                storage_saved_byte += state.savedByte;
                            }
                        }
                        break;
                    }
                    data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, hibernatedAppCount, storage_saved_byte / 1000000));
                    return 0;
            }
        }
    }
}
