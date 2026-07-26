package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public final class ContentService extends android.content.IContentService.Stub {
    public static final long ACCOUNT_ACCESS_CHECK_CHANGE_ID = 201794303;
    public static final long AUTHORITY_ACCESS_CHECK_CHANGE_ID = 207133734;
    private static final long BACKGROUND_OBSERVER_DELAY = 10000;
    static final boolean DEBUG = false;
    static final java.lang.String TAG = "ContentService";
    private static final int TOO_MANY_OBSERVERS_THRESHOLD = 1000;
    public static com.android.server.content.IContentServiceExt sContentServiceExt = (com.android.server.content.IContentServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.content.IContentServiceExt.class).create();
    private static final com.android.internal.os.BinderDeathDispatcher<android.database.IContentObserver> sObserverDeathDispatcher = new com.android.internal.os.BinderDeathDispatcher<>();
    private static final android.util.ArraySet<java.lang.Integer> sObserverLeakDetectedUid = new android.util.ArraySet<>(0);
    private final android.accounts.AccountManagerInternal mAccountManagerInternal;
    private android.content.Context mContext;
    private boolean mFactoryTest;
    private final com.android.server.content.ContentService.ObserverNode mRootNode = new com.android.server.content.ContentService.ObserverNode("");
    private com.android.server.content.SyncManager mSyncManager = null;
    private final java.lang.Object mSyncManagerLock = new java.lang.Object();
    private final android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle>>> mCache = new android.util.SparseArray<>();
    private android.content.BroadcastReceiver mCacheReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.content.ContentService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            synchronized (com.android.server.content.ContentService.this.mCache) {
                if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
                    com.android.server.content.ContentService.this.mCache.clear();
                } else {
                    android.net.Uri data = intent.getData();
                    if (data != null) {
                        int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                        java.lang.String packageName = data.getSchemeSpecificPart();
                        com.android.server.content.ContentService.this.invalidateCacheLocked(userId, packageName, null);
                    }
                }
            }
        }
    };

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.content.ContentService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            boolean factoryTest = android.os.FactoryTest.getMode() == 1;
            this.mService = new com.android.server.content.ContentService(getContext(), factoryTest);
            publishBinderService(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT, this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            this.mService.onBootPhase(phase);
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) {
            this.mService.onStartUser(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mService.onUnlockUser(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            this.mService.onStopUser(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            synchronized (this.mService.mCache) {
                this.mService.mCache.remove(user.getUserIdentifier());
            }
        }
    }

    private com.android.server.content.SyncManager getSyncManager() {
        com.android.server.content.SyncManager syncManager;
        synchronized (this.mSyncManagerLock) {
            if (this.mSyncManager == null) {
                this.mSyncManager = new com.android.server.content.SyncManager(this.mContext, this.mFactoryTest);
            }
            syncManager = this.mSyncManager;
        }
        return syncManager;
    }

    void onStartUser(int userHandle) {
        if (this.mSyncManager != null) {
            this.mSyncManager.onStartUser(userHandle);
        }
    }

    void onUnlockUser(int userHandle) {
        if (this.mSyncManager != null) {
            this.mSyncManager.onUnlockUser(userHandle);
        }
    }

    void onStopUser(int userHandle) {
        if (this.mSyncManager != null) {
            this.mSyncManager.onStopUser(userHandle);
        }
    }

    protected synchronized void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw_, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mContext, TAG, pw_)) {
            return;
        }
        java.io.PrintWriter indentingPrintWriter = new com.android.internal.util.IndentingPrintWriter(pw_, "  ");
        boolean dumpAll = com.android.internal.util.ArrayUtils.contains(args, "-a");
        long identityToken = clearCallingIdentity();
        try {
            if (this.mSyncManager == null) {
                indentingPrintWriter.println("SyncManager not available yet");
            } else {
                this.mSyncManager.dump(fd, indentingPrintWriter, dumpAll);
            }
            indentingPrintWriter.println();
            indentingPrintWriter.println("Observer tree:");
            synchronized (this.mRootNode) {
                try {
                    try {
                        int[] counts = new int[2];
                        android.util.SparseIntArray pidCounts = new android.util.SparseIntArray();
                        this.mRootNode.dumpLocked(fd, indentingPrintWriter, args, "", "  ", counts, pidCounts);
                        indentingPrintWriter.println();
                        java.util.ArrayList<java.lang.Integer> sorted = new java.util.ArrayList<>();
                        int i = 0;
                        while (i < pidCounts.size()) {
                            android.util.SparseIntArray pidCounts2 = pidCounts;
                            sorted.add(java.lang.Integer.valueOf(pidCounts2.keyAt(i)));
                            i++;
                            pidCounts = pidCounts2;
                        }
                        final android.util.SparseIntArray pidCounts3 = pidCounts;
                        java.util.Collections.sort(sorted, new java.util.Comparator<java.lang.Integer>() { // from class: com.android.server.content.ContentService.2
                            @Override // java.util.Comparator
                            public int compare(java.lang.Integer lhs, java.lang.Integer rhs) {
                                int lc = pidCounts3.get(lhs.intValue());
                                int rc = pidCounts3.get(rhs.intValue());
                                if (lc < rc) {
                                    return 1;
                                }
                                if (lc > rc) {
                                    return -1;
                                }
                                return 0;
                            }
                        });
                        for (int i2 = 0; i2 < sorted.size(); i2++) {
                            int pid = sorted.get(i2).intValue();
                            indentingPrintWriter.print("  pid ");
                            indentingPrintWriter.print(pid);
                            indentingPrintWriter.print(": ");
                            indentingPrintWriter.print(pidCounts3.get(pid));
                            indentingPrintWriter.println(" observers");
                        }
                        indentingPrintWriter.println();
                        indentingPrintWriter.increaseIndent();
                        indentingPrintWriter.print("Total number of nodes: ");
                        indentingPrintWriter.println(counts[0]);
                        indentingPrintWriter.print("Total number of observers: ");
                        indentingPrintWriter.println(counts[1]);
                        sObserverDeathDispatcher.dump(indentingPrintWriter);
                        indentingPrintWriter.decreaseIndent();
                        synchronized (sObserverLeakDetectedUid) {
                            indentingPrintWriter.println();
                            indentingPrintWriter.print("Observer leaking UIDs: ");
                            indentingPrintWriter.println(sObserverLeakDetectedUid.toString());
                        }
                        try {
                            synchronized (this.mCache) {
                                try {
                                    indentingPrintWriter.println();
                                    indentingPrintWriter.println("Cached content:");
                                    indentingPrintWriter.increaseIndent();
                                    for (int i3 = 0; i3 < this.mCache.size(); i3++) {
                                        indentingPrintWriter.println("User " + this.mCache.keyAt(i3) + ":");
                                        indentingPrintWriter.increaseIndent();
                                        indentingPrintWriter.println(this.mCache.valueAt(i3));
                                        indentingPrintWriter.decreaseIndent();
                                    }
                                    indentingPrintWriter.decreaseIndent();
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        throw th;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            }
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    ContentService(android.content.Context context, boolean factoryTest) {
        this.mContext = context;
        this.mFactoryTest = factoryTest;
        com.android.server.pm.permission.LegacyPermissionManagerInternal permissionManagerInternal = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        permissionManagerInternal.setSyncAdapterPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.SyncAdapterPackagesProvider() { // from class: com.android.server.content.ContentService$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.SyncAdapterPackagesProvider
            public final java.lang.String[] getPackages(java.lang.String str, int i) {
                return this.f$0.lambda$new$0(str, i);
            }
        });
        android.content.IntentFilter packageFilter = new android.content.IntentFilter();
        packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        packageFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        packageFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        packageFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mCacheReceiver, android.os.UserHandle.ALL, packageFilter, null, null);
        android.content.IntentFilter localeFilter = new android.content.IntentFilter();
        localeFilter.addAction("android.intent.action.LOCALE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mCacheReceiver, android.os.UserHandle.ALL, localeFilter, null, null);
        this.mAccountManagerInternal = (android.accounts.AccountManagerInternal) com.android.server.LocalServices.getService(android.accounts.AccountManagerInternal.class);
    }

    void onBootPhase(int phase) {
        switch (phase) {
            case 550:
                getSyncManager();
                break;
        }
        if (this.mSyncManager != null) {
            this.mSyncManager.onBootPhase(phase);
        }
    }

    public void registerContentObserver(android.net.Uri uri, boolean notifyForDescendants, android.database.IContentObserver observer, int userHandle, int targetSdkVersion) throws java.lang.Throwable {
        if (observer == null || uri == null) {
            throw new java.lang.IllegalArgumentException("You must pass a valid uri and observer");
        }
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        int userHandle2 = handleIncomingUser(uri, pid, uid, 1, true, userHandle);
        java.lang.String msg = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).checkContentProviderAccess(uri.getAuthority(), userHandle2);
        if (msg != null) {
            if (targetSdkVersion >= 26) {
                throw new java.lang.SecurityException(msg);
            }
            if (!msg.startsWith("Failed to find provider")) {
                android.util.Log.w(TAG, "Ignoring content changes for " + uri + " from " + uid + ": " + msg);
                return;
            }
        }
        if (sContentServiceExt.interceptRegisterContentObserver(uri, uid, pid)) {
            return;
        }
        int userHandle3 = sContentServiceExt.checkUserHandle(uri.getAuthority(), userHandle2);
        synchronized (this.mRootNode) {
            try {
                try {
                    this.mRootNode.addObserverLocked(uri, observer, notifyForDescendants, this.mRootNode, uid, pid, userHandle3);
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

    public void registerContentObserver(android.net.Uri uri, boolean notifyForDescendants, android.database.IContentObserver observer) throws java.lang.Throwable {
        registerContentObserver(uri, notifyForDescendants, observer, android.os.UserHandle.getCallingUserId(), 10000);
    }

    public void unregisterContentObserver(android.database.IContentObserver observer) {
        if (observer == null) {
            throw new java.lang.IllegalArgumentException("You must pass a valid observer");
        }
        synchronized (this.mRootNode) {
            this.mRootNode.removeObserverLocked(observer);
        }
    }

    public void notifyChange(android.net.Uri[] uris, android.database.IContentObserver observer, boolean observerWantsSelfNotifications, int flags, int userId, int targetSdkVersion, java.lang.String callingPackage) throws java.lang.Throwable {
        android.util.ArrayMap<android.util.Pair<java.lang.String, java.lang.Integer>, java.lang.String> validatedProviders;
        int callingUid;
        int callingUid2 = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        int callingUserId = android.os.UserHandle.getCallingUserId();
        com.android.server.content.ContentService.ObserverCollector collector = new com.android.server.content.ContentService.ObserverCollector();
        android.util.ArrayMap<android.util.Pair<java.lang.String, java.lang.Integer>, java.lang.String> validatedProviders2 = new android.util.ArrayMap<>();
        for (android.net.Uri uri : uris) {
            int resolvedUserId = handleIncomingUser(uri, callingPid, callingUid2, 2, true, userId);
            android.util.Pair<java.lang.String, java.lang.Integer> provider = android.util.Pair.create(uri.getAuthority(), java.lang.Integer.valueOf(resolvedUserId));
            if (!validatedProviders2.containsKey(provider)) {
                java.lang.String msg = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).checkContentProviderAccess(uri.getAuthority(), resolvedUserId);
                if (msg != null) {
                    if (targetSdkVersion >= 26) {
                        throw new java.lang.SecurityException(msg);
                    }
                    if (!msg.startsWith("Failed to find provider")) {
                        android.util.Log.w(TAG, "Ignoring notify for " + uri + " from " + callingUid2 + ": " + msg);
                    }
                }
                java.lang.String packageName = getProviderPackageName(uri, resolvedUserId);
                validatedProviders2.put(provider, packageName);
            }
            synchronized (this.mRootNode) {
                int segmentCount = com.android.server.content.ContentService.ObserverNode.countUriSegments(uri);
                this.mRootNode.collectObserversLocked(uri, segmentCount, 0, observer, observerWantsSelfNotifications, flags, resolvedUserId, collector);
            }
        }
        long token = clearCallingIdentity();
        try {
            collector.dispatch();
            com.android.server.content.SyncManager syncManager = getSyncManager();
            int i = 0;
            while (i < validatedProviders2.size()) {
                java.lang.String authority = (java.lang.String) validatedProviders2.keyAt(i).first;
                int resolvedUserId2 = ((java.lang.Integer) validatedProviders2.keyAt(i).second).intValue();
                java.lang.String packageName2 = validatedProviders2.valueAt(i);
                if ((flags & 1) == 0) {
                    validatedProviders = validatedProviders2;
                    callingUid = callingUid2;
                } else {
                    validatedProviders = validatedProviders2;
                    callingUid = callingUid2;
                    try {
                        syncManager.scheduleLocalSync(null, callingUserId, callingUid2, authority, getSyncExemptionForCaller(callingUid2), callingUid, callingPid, callingPackage);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                }
                synchronized (this.mCache) {
                    for (android.net.Uri uri2 : uris) {
                        if (java.util.Objects.equals(uri2.getAuthority(), authority)) {
                            invalidateCacheLocked(resolvedUserId2, packageName2, uri2);
                        }
                    }
                }
                i++;
                validatedProviders2 = validatedProviders;
                callingUid2 = callingUid;
            }
            android.os.Binder.restoreCallingIdentity(token);
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    private int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, int userHandle) {
        try {
            return android.app.ActivityManager.getService().checkUriPermission(uri, pid, uid, modeFlags, userHandle, (android.os.IBinder) null);
        } catch (android.os.RemoteException e) {
            return -1;
        }
    }

    public static class ObserverCollector {
        private final android.util.ArrayMap<com.android.server.content.ContentService.ObserverCollector.Key, java.util.List<android.net.Uri>> collected = new android.util.ArrayMap<>();

        /* JADX INFO: Access modifiers changed from: private */
        static class Key {
            final int flags;
            final android.database.IContentObserver observer;
            final boolean selfChange;
            final int uid;
            final int userId;

            Key(android.database.IContentObserver observer, int uid, boolean selfChange, int flags, int userId) {
                this.observer = observer;
                this.uid = uid;
                this.selfChange = selfChange;
                this.flags = flags;
                this.userId = userId;
            }

            public boolean equals(java.lang.Object o) {
                if (!(o instanceof com.android.server.content.ContentService.ObserverCollector.Key)) {
                    return false;
                }
                com.android.server.content.ContentService.ObserverCollector.Key other = (com.android.server.content.ContentService.ObserverCollector.Key) o;
                return java.util.Objects.equals(this.observer, other.observer) && this.uid == other.uid && this.selfChange == other.selfChange && this.flags == other.flags && this.userId == other.userId;
            }

            public int hashCode() {
                return java.util.Objects.hash(this.observer, java.lang.Integer.valueOf(this.uid), java.lang.Boolean.valueOf(this.selfChange), java.lang.Integer.valueOf(this.flags), java.lang.Integer.valueOf(this.userId));
            }
        }

        public void collect(android.database.IContentObserver observer, int uid, boolean selfChange, android.net.Uri uri, int flags, int userId) {
            com.android.server.content.ContentService.ObserverCollector.Key key = new com.android.server.content.ContentService.ObserverCollector.Key(observer, uid, selfChange, flags, userId);
            java.util.List<android.net.Uri> value = this.collected.get(key);
            if (value == null) {
                value = new java.util.ArrayList();
                this.collected.put(key, value);
            }
            value.add(uri);
        }

        public void dispatch() {
            for (int i = 0; i < this.collected.size(); i++) {
                final com.android.server.content.ContentService.ObserverCollector.Key key = this.collected.keyAt(i);
                final java.util.List<android.net.Uri> value = this.collected.valueAt(i);
                java.lang.Runnable task = new java.lang.Runnable() { // from class: com.android.server.content.ContentService$ObserverCollector$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.content.ContentService.ObserverCollector.Key key2 = key;
                        java.util.List list = value;
                        key2.observer.onChangeEtc(key2.selfChange, (android.net.Uri[]) list.toArray(new android.net.Uri[list.size()]), key2.flags, key2.userId);
                    }
                };
                boolean noDelay = (key.flags & 32768) != 0;
                int procState = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getUidProcessState(key.uid);
                if (procState <= 6 || noDelay) {
                    task.run();
                } else {
                    com.android.internal.os.BackgroundThread.getHandler().postDelayed(task, 10000L);
                }
            }
        }
    }

    public void requestSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras, java.lang.String callingPackage) {
        android.os.Bundle.setDefusable(extras, true);
        android.content.ContentResolver.validateSyncExtrasBundle(extras);
        int userId = android.os.UserHandle.getCallingUserId();
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(authority, callingUid, userId)) {
            return;
        }
        validateExtras(callingUid, extras);
        int syncExemption = getSyncExemptionAndCleanUpExtrasForCaller(callingUid, extras);
        long identityToken = clearCallingIdentity();
        try {
            getSyncManager().scheduleSync(account, userId, callingUid, authority, extras, -2, syncExemption, callingUid, callingPid, callingPackage);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void sync(android.content.SyncRequest request, java.lang.String callingPackage) throws java.lang.Throwable {
        syncAsUser(request, android.os.UserHandle.getCallingUserId(), callingPackage);
    }

    private long clampPeriod(long period) {
        long minPeriod = android.app.job.JobInfo.getMinPeriodMillis() / 1000;
        if (period < minPeriod) {
            android.util.Slog.w(TAG, "Requested poll frequency of " + period + " seconds being rounded up to " + minPeriod + "s.");
            return minPeriod;
        }
        return period;
    }

    public void syncAsUser(android.content.SyncRequest request, int userId, java.lang.String callingPackage) throws java.lang.Throwable {
        enforceCrossUserPermission(userId, "no permission to request sync as user: " + userId);
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (!hasAccountAccess(true, request.getAccount(), callingUid) || !hasAuthorityAccess(request.getProvider(), callingUid, userId)) {
            return;
        }
        android.os.Bundle extras = request.getBundle();
        validateExtras(callingUid, extras);
        int syncExemption = getSyncExemptionAndCleanUpExtrasForCaller(callingUid, extras);
        long identityToken = clearCallingIdentity();
        try {
            long flextime = request.getSyncFlexTime();
            long runAtTime = request.getSyncRunTime();
            if (request.isPeriodic()) {
                try {
                    this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SYNC_SETTINGS", "no permission to write the sync settings");
                    com.android.server.content.SyncStorageEngine.EndPoint info = new com.android.server.content.SyncStorageEngine.EndPoint(request.getAccount(), request.getProvider(), userId);
                    getSyncManager().updateOrAddPeriodicSync(info, clampPeriod(runAtTime), flextime, extras);
                    restoreCallingIdentity(identityToken);
                    return;
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            } else {
                try {
                    getSyncManager().scheduleSync(request.getAccount(), userId, callingUid, request.getProvider(), extras, -2, syncExemption, callingUid, callingPid, callingPackage);
                    restoreCallingIdentity(identityToken);
                    return;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
        restoreCallingIdentity(identityToken);
        throw th;
    }

    public void cancelSync(android.accounts.Account account, java.lang.String authority, android.content.ComponentName cname) {
        cancelSyncAsUser(account, authority, cname, android.os.UserHandle.getCallingUserId());
    }

    public void cancelSyncAsUser(android.accounts.Account account, java.lang.String authority, android.content.ComponentName cname, int userId) {
        if (authority != null && authority.length() == 0) {
            throw new java.lang.IllegalArgumentException("Authority must be non-empty");
        }
        enforceCrossUserPermission(userId, "no permission to modify the sync settings for user " + userId);
        if (cname != null) {
            android.util.Slog.e(TAG, "cname not null.");
            return;
        }
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.content.SyncStorageEngine.EndPoint info = new com.android.server.content.SyncStorageEngine.EndPoint(account, authority, userId);
            getSyncManager().clearScheduledSyncOperations(info);
            getSyncManager().cancelActiveSync(info, null, "API");
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void cancelRequest(android.content.SyncRequest request) {
        if (request.isPeriodic()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SYNC_SETTINGS", "no permission to write the sync settings");
        }
        int callingUid = android.os.Binder.getCallingUid();
        android.os.Bundle extras = new android.os.Bundle(request.getBundle());
        validateExtras(callingUid, extras);
        int userId = android.os.UserHandle.getCallingUserId();
        long identityToken = clearCallingIdentity();
        try {
            android.accounts.Account account = request.getAccount();
            java.lang.String provider = request.getProvider();
            com.android.server.content.SyncStorageEngine.EndPoint info = new com.android.server.content.SyncStorageEngine.EndPoint(account, provider, userId);
            if (request.isPeriodic()) {
                getSyncManager().removePeriodicSync(info, extras, "cancelRequest() by uid=" + callingUid);
            }
            getSyncManager().cancelScheduledSyncOperation(info, extras);
            getSyncManager().cancelActiveSync(info, extras, "API");
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public android.content.SyncAdapterType[] getSyncAdapterTypes() {
        return getSyncAdapterTypesAsUser(android.os.UserHandle.getCallingUserId());
    }

    public android.content.SyncAdapterType[] getSyncAdapterTypesAsUser(int userId) {
        enforceCrossUserPermission(userId, "no permission to read sync settings for user " + userId);
        int callingUid = android.os.Binder.getCallingUid();
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncAdapterTypes(callingUid, userId);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    /* JADX INFO: renamed from: getSyncAdapterPackagesForAuthorityAsUser, reason: merged with bridge method [inline-methods] */
    public java.lang.String[] lambda$new$0(java.lang.String authority, int userId) {
        enforceCrossUserPermission(userId, "no permission to read sync settings for user " + userId);
        int callingUid = android.os.Binder.getCallingUid();
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncAdapterPackagesForAuthorityAsUser(authority, callingUid, userId);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public java.lang.String getSyncAdapterPackageAsUser(java.lang.String accountType, java.lang.String authority, int userId) {
        enforceCrossUserPermission(userId, "no permission to read sync settings for user " + userId);
        int callingUid = android.os.Binder.getCallingUid();
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncAdapterPackageAsUser(accountType, authority, callingUid, userId);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public boolean getSyncAutomatically(android.accounts.Account account, java.lang.String providerName) {
        return getSyncAutomaticallyAsUser(account, providerName, android.os.UserHandle.getCallingUserId());
    }

    public boolean getSyncAutomaticallyAsUser(android.accounts.Account account, java.lang.String providerName, int userId) {
        enforceCrossUserPermission(userId, "no permission to read the sync settings for user " + userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_SYNC_SETTINGS", "no permission to read the sync settings");
        int callingUid = android.os.Binder.getCallingUid();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(providerName, callingUid, userId)) {
            return false;
        }
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncStorageEngine().getSyncAutomatically(account, userId, providerName);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void setSyncAutomatically(android.accounts.Account account, java.lang.String providerName, boolean sync) {
        setSyncAutomaticallyAsUser(account, providerName, sync, android.os.UserHandle.getCallingUserId());
    }

    public void setSyncAutomaticallyAsUser(android.accounts.Account account, java.lang.String providerName, boolean sync, int userId) {
        if (android.text.TextUtils.isEmpty(providerName)) {
            throw new java.lang.IllegalArgumentException("Authority must be non-empty");
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SYNC_SETTINGS", "no permission to write the sync settings");
        enforceCrossUserPermission(userId, "no permission to modify the sync settings for user " + userId);
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(providerName, callingUid, userId)) {
            return;
        }
        int syncExemptionFlag = getSyncExemptionForCaller(callingUid);
        long identityToken = clearCallingIdentity();
        try {
            getSyncManager().getSyncStorageEngine().setSyncAutomatically(account, userId, providerName, sync, syncExemptionFlag, callingUid, callingPid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void addPeriodicSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras, long pollFrequency) {
        android.os.Bundle.setDefusable(extras, true);
        if (account == null) {
            throw new java.lang.IllegalArgumentException("Account must not be null");
        }
        if (android.text.TextUtils.isEmpty(authority)) {
            throw new java.lang.IllegalArgumentException("Authority must not be empty.");
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SYNC_SETTINGS", "no permission to write the sync settings");
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(authority, callingUid, userId)) {
            return;
        }
        validateExtras(callingUid, extras);
        long pollFrequency2 = clampPeriod(pollFrequency);
        long defaultFlex = com.android.server.content.SyncStorageEngine.calculateDefaultFlexTime(pollFrequency2);
        long identityToken = clearCallingIdentity();
        try {
            com.android.server.content.SyncStorageEngine.EndPoint info = new com.android.server.content.SyncStorageEngine.EndPoint(account, authority, userId);
            getSyncManager().updateOrAddPeriodicSync(info, pollFrequency2, defaultFlex, extras);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void removePeriodicSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras) {
        android.os.Bundle.setDefusable(extras, true);
        if (account == null) {
            throw new java.lang.IllegalArgumentException("Account must not be null");
        }
        if (android.text.TextUtils.isEmpty(authority)) {
            throw new java.lang.IllegalArgumentException("Authority must not be empty");
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SYNC_SETTINGS", "no permission to write the sync settings");
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(authority, callingUid, userId)) {
            return;
        }
        validateExtras(callingUid, extras);
        long identityToken = clearCallingIdentity();
        try {
            getSyncManager().removePeriodicSync(new com.android.server.content.SyncStorageEngine.EndPoint(account, authority, userId), extras, "removePeriodicSync() by uid=" + callingUid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public java.util.List<android.content.PeriodicSync> getPeriodicSyncs(android.accounts.Account account, java.lang.String providerName, android.content.ComponentName cname) {
        if (account == null) {
            throw new java.lang.IllegalArgumentException("Account must not be null");
        }
        if (android.text.TextUtils.isEmpty(providerName)) {
            throw new java.lang.IllegalArgumentException("Authority must not be empty");
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_SYNC_SETTINGS", "no permission to read the sync settings");
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!hasAccountAccess(true, account, callingUid)) {
            return new java.util.ArrayList();
        }
        if (!hasAuthorityAccess(providerName, callingUid, userId)) {
            return new java.util.ArrayList();
        }
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getPeriodicSyncs(new com.android.server.content.SyncStorageEngine.EndPoint(account, providerName, userId));
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public int getIsSyncable(android.accounts.Account account, java.lang.String providerName) {
        return getIsSyncableAsUser(account, providerName, android.os.UserHandle.getCallingUserId());
    }

    public int getIsSyncableAsUser(android.accounts.Account account, java.lang.String providerName, int userId) {
        enforceCrossUserPermission(userId, "no permission to read the sync settings for user " + userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_SYNC_SETTINGS", "no permission to read the sync settings");
        int callingUid = android.os.Binder.getCallingUid();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(providerName, callingUid, userId)) {
            return 0;
        }
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().computeSyncable(account, userId, providerName, false, false);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void setIsSyncable(android.accounts.Account account, java.lang.String providerName, int syncable) {
        setIsSyncableAsUser(account, providerName, syncable, android.os.UserHandle.getCallingUserId());
    }

    public void setIsSyncableAsUser(android.accounts.Account account, java.lang.String providerName, int syncable, int userId) {
        if (android.text.TextUtils.isEmpty(providerName)) {
            throw new java.lang.IllegalArgumentException("Authority must not be empty");
        }
        enforceCrossUserPermission(userId, "no permission to set the sync settings for user " + userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SYNC_SETTINGS", "no permission to write the sync settings");
        int syncable2 = normalizeSyncable(syncable);
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(providerName, callingUid, userId)) {
            return;
        }
        long identityToken = clearCallingIdentity();
        try {
            getSyncManager().getSyncStorageEngine().setIsSyncable(account, userId, providerName, syncable2, callingUid, callingPid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public boolean getMasterSyncAutomatically() {
        return getMasterSyncAutomaticallyAsUser(android.os.UserHandle.getCallingUserId());
    }

    public boolean getMasterSyncAutomaticallyAsUser(int userId) {
        enforceCrossUserPermission(userId, "no permission to read the sync settings for user " + userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_SYNC_SETTINGS", "no permission to read the sync settings");
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncStorageEngine().getMasterSyncAutomatically(userId);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void setMasterSyncAutomatically(boolean flag) {
        setMasterSyncAutomaticallyAsUser(flag, android.os.UserHandle.getCallingUserId());
    }

    public void setMasterSyncAutomaticallyAsUser(boolean flag, int userId) {
        enforceCrossUserPermission(userId, "no permission to set the sync status for user " + userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SYNC_SETTINGS", "no permission to write the sync settings");
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long identityToken = clearCallingIdentity();
        try {
            getSyncManager().getSyncStorageEngine().setMasterSyncAutomatically(flag, userId, getSyncExemptionForCaller(callingUid), callingUid, callingPid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public boolean isSyncActive(android.accounts.Account account, java.lang.String authority, android.content.ComponentName cname) {
        isSyncActive_enforcePermission();
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getCallingUserId();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(authority, callingUid, userId)) {
            return false;
        }
        long identityToken = clearCallingIdentity();
        try {
            return getSyncManager().getSyncStorageEngine().isSyncActive(new com.android.server.content.SyncStorageEngine.EndPoint(account, authority, userId));
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public java.util.List<android.content.SyncInfo> getCurrentSyncs() {
        return getCurrentSyncsAsUser(android.os.UserHandle.getCallingUserId());
    }

    public java.util.List<android.content.SyncInfo> getCurrentSyncsAsUser(final int userId) {
        enforceCrossUserPermission(userId, "no permission to read the sync settings for user " + userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_SYNC_STATS", "no permission to read the sync stats");
        boolean canAccessAccounts = this.mContext.checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") == 0;
        final int callingUid = android.os.Binder.getCallingUid();
        long identityToken = clearCallingIdentity();
        try {
            java.util.List<android.content.SyncInfo> results = getSyncManager().getSyncStorageEngine().getCurrentSyncsCopy(userId, canAccessAccounts);
            restoreCallingIdentity(identityToken);
            results.removeIf(new java.util.function.Predicate() { // from class: com.android.server.content.ContentService$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$getCurrentSyncsAsUser$1(callingUid, userId, (android.content.SyncInfo) obj);
                }
            });
            return results;
        } catch (java.lang.Throwable th) {
            restoreCallingIdentity(identityToken);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getCurrentSyncsAsUser$1(int callingUid, int userId, android.content.SyncInfo i) {
        return !hasAuthorityAccess(i.authority, callingUid, userId);
    }

    public android.content.SyncStatusInfo getSyncStatus(android.accounts.Account account, java.lang.String authority, android.content.ComponentName cname) {
        return getSyncStatusAsUser(account, authority, cname, android.os.UserHandle.getCallingUserId());
    }

    public android.content.SyncStatusInfo getSyncStatusAsUser(android.accounts.Account account, java.lang.String authority, android.content.ComponentName cname, int userId) {
        if (android.text.TextUtils.isEmpty(authority)) {
            throw new java.lang.IllegalArgumentException("Authority must not be empty");
        }
        enforceCrossUserPermission(userId, "no permission to read the sync stats for user " + userId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_SYNC_STATS", "no permission to read the sync stats");
        int callingUid = android.os.Binder.getCallingUid();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(authority, callingUid, userId)) {
            return null;
        }
        long identityToken = clearCallingIdentity();
        try {
            if (account != null && authority != null) {
                com.android.server.content.SyncStorageEngine.EndPoint info = new com.android.server.content.SyncStorageEngine.EndPoint(account, authority, userId);
                return getSyncManager().getSyncStorageEngine().getStatusByAuthority(info);
            }
            throw new java.lang.IllegalArgumentException("Must call sync status with valid authority");
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public boolean isSyncPending(android.accounts.Account account, java.lang.String authority, android.content.ComponentName cname) {
        return isSyncPendingAsUser(account, authority, cname, android.os.UserHandle.getCallingUserId());
    }

    public boolean isSyncPendingAsUser(android.accounts.Account account, java.lang.String authority, android.content.ComponentName cname, int userId) {
        isSyncPendingAsUser_enforcePermission();
        enforceCrossUserPermission(userId, "no permission to retrieve the sync settings for user " + userId);
        int callingUid = android.os.Binder.getCallingUid();
        if (!hasAccountAccess(true, account, callingUid) || !hasAuthorityAccess(authority, callingUid, userId)) {
            return false;
        }
        long identityToken = clearCallingIdentity();
        try {
            if (account != null && authority != null) {
                com.android.server.content.SyncStorageEngine.EndPoint info = new com.android.server.content.SyncStorageEngine.EndPoint(account, authority, userId);
                return getSyncManager().getSyncStorageEngine().isSyncPending(info);
            }
            throw new java.lang.IllegalArgumentException("Invalid authority specified");
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    public void addStatusChangeListener(int mask, android.content.ISyncStatusObserver callback) {
        int callingUid = android.os.Binder.getCallingUid();
        long identityToken = clearCallingIdentity();
        if (callback != null) {
            try {
                getSyncManager().getSyncStorageEngine().addStatusChangeListener(mask, callingUid, callback);
            } finally {
                restoreCallingIdentity(identityToken);
            }
        }
    }

    public void removeStatusChangeListener(android.content.ISyncStatusObserver callback) {
        long identityToken = clearCallingIdentity();
        if (callback != null) {
            try {
                getSyncManager().getSyncStorageEngine().removeStatusChangeListener(callback);
            } finally {
                restoreCallingIdentity(identityToken);
            }
        }
    }

    private java.lang.String getProviderPackageName(android.net.Uri uri, int userId) {
        android.content.pm.ProviderInfo pi = this.mContext.getPackageManager().resolveContentProviderAsUser(uri.getAuthority(), 0, userId);
        if (pi != null) {
            return pi.packageName;
        }
        return null;
    }

    private android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle> findOrCreateCacheLocked(int userId, java.lang.String providerPackageName) {
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle>> userCache = this.mCache.get(userId);
        if (userCache == null) {
            userCache = new android.util.ArrayMap<>();
            this.mCache.put(userId, userCache);
        }
        android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle> packageCache = userCache.get(providerPackageName);
        if (packageCache == null) {
            android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle> packageCache2 = new android.util.ArrayMap<>();
            userCache.put(providerPackageName, packageCache2);
            return packageCache2;
        }
        return packageCache;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateCacheLocked(int userId, java.lang.String providerPackageName, android.net.Uri uri) {
        android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle> packageCache;
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle>> userCache = this.mCache.get(userId);
        if (userCache == null || (packageCache = userCache.get(providerPackageName)) == null) {
            return;
        }
        if (uri != null) {
            int i = 0;
            while (i < packageCache.size()) {
                android.util.Pair<java.lang.String, android.net.Uri> key = packageCache.keyAt(i);
                if (key.second != null && ((android.net.Uri) key.second).toString().startsWith(uri.toString())) {
                    packageCache.removeAt(i);
                } else {
                    i++;
                }
            }
            return;
        }
        packageCache.clear();
    }

    public void putCache(java.lang.String packageName, android.net.Uri key, android.os.Bundle value, int userId) {
        android.os.Bundle.setDefusable(value, true);
        enforceNonFullCrossUserPermission(userId, TAG);
        this.mContext.enforceCallingOrSelfPermission("android.permission.CACHE_CONTENT", TAG);
        ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).checkPackage(android.os.Binder.getCallingUid(), packageName);
        java.lang.String providerPackageName = getProviderPackageName(key, userId);
        android.util.Pair<java.lang.String, android.net.Uri> fullKey = android.util.Pair.create(packageName, key);
        synchronized (this.mCache) {
            android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle> cache = findOrCreateCacheLocked(userId, providerPackageName);
            if (value != null) {
                cache.put(fullKey, value);
            } else {
                cache.remove(fullKey);
            }
        }
    }

    public android.os.Bundle getCache(java.lang.String packageName, android.net.Uri key, int userId) {
        android.os.Bundle bundle;
        enforceNonFullCrossUserPermission(userId, TAG);
        this.mContext.enforceCallingOrSelfPermission("android.permission.CACHE_CONTENT", TAG);
        ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).checkPackage(android.os.Binder.getCallingUid(), packageName);
        java.lang.String providerPackageName = getProviderPackageName(key, userId);
        android.util.Pair<java.lang.String, android.net.Uri> fullKey = android.util.Pair.create(packageName, key);
        synchronized (this.mCache) {
            android.util.ArrayMap<android.util.Pair<java.lang.String, android.net.Uri>, android.os.Bundle> cache = findOrCreateCacheLocked(userId, providerPackageName);
            bundle = cache.get(fullKey);
        }
        return bundle;
    }

    private int handleIncomingUser(android.net.Uri uri, int pid, int uid, int modeFlags, boolean allowNonFull, int userId) {
        if (userId == -2) {
            userId = android.app.ActivityManager.getCurrentUser();
        }
        if (userId == -1) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "No access to " + uri);
        } else {
            if (userId < 0) {
                throw new java.lang.IllegalArgumentException("Invalid user: " + userId);
            }
            if (userId != android.os.UserHandle.getCallingUserId() && checkUriPermission(uri, pid, uid, modeFlags, userId) != 0) {
                boolean allow = false;
                if (this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0) {
                    allow = true;
                } else if (allowNonFull && this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS") == 0) {
                    allow = true;
                }
                if (!allow) {
                    java.lang.String permissions = allowNonFull ? "android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS" : "android.permission.INTERACT_ACROSS_USERS_FULL";
                    throw new java.lang.SecurityException("No access to " + uri + ": neither user " + uid + " nor current process has " + permissions);
                }
            }
        }
        return userId;
    }

    private void enforceCrossUserPermission(int userHandle, java.lang.String message) {
        int callingUser = android.os.UserHandle.getCallingUserId();
        if (callingUser != userHandle) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", message);
        }
    }

    private void enforceNonFullCrossUserPermission(int userHandle, java.lang.String message) {
        int callingUser = android.os.UserHandle.getCallingUserId();
        if (callingUser == userHandle) {
            return;
        }
        int interactAcrossUsersState = this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS");
        if (interactAcrossUsersState == 0) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", message);
    }

    private boolean hasAccountAccess(boolean checkCompatFlag, android.accounts.Account account, int uid) {
        if (account == null) {
            return true;
        }
        if (checkCompatFlag && !android.app.compat.CompatChanges.isChangeEnabled(ACCOUNT_ACCESS_CHECK_CHANGE_ID, uid)) {
            return true;
        }
        long identityToken = clearCallingIdentity();
        try {
            return this.mAccountManagerInternal.hasAccountAccess(account, uid);
        } finally {
            restoreCallingIdentity(identityToken);
        }
    }

    private boolean hasAuthorityAccess(java.lang.String authority, int uid, int userId) {
        if (android.text.TextUtils.isEmpty(authority) || !android.app.compat.CompatChanges.isChangeEnabled(AUTHORITY_ACCESS_CHECK_CHANGE_ID, uid)) {
            return true;
        }
        java.lang.String[] syncAdapterPackages = lambda$new$0(authority, userId);
        return true ^ com.android.internal.util.ArrayUtils.isEmpty(syncAdapterPackages);
    }

    private static int normalizeSyncable(int syncable) {
        if (syncable > 0) {
            return 1;
        }
        if (syncable == 0) {
            return 0;
        }
        return -2;
    }

    private void validateExtras(int callingUid, android.os.Bundle extras) {
        if (extras.containsKey("v_exemption")) {
            switch (callingUid) {
                case 0:
                case 1000:
                case 2000:
                    return;
                default:
                    android.util.Log.w(TAG, "Invalid extras specified. requestsync -f/-F needs to run on 'adb shell'");
                    throw new java.lang.SecurityException("Invalid extras specified.");
            }
        }
    }

    private int getSyncExemptionForCaller(int callingUid) {
        return getSyncExemptionAndCleanUpExtrasForCaller(callingUid, null);
    }

    private int getSyncExemptionAndCleanUpExtrasForCaller(int callingUid, android.os.Bundle extras) {
        if (extras != null) {
            int exemption = extras.getInt("v_exemption", -1);
            extras.remove("v_exemption");
            if (exemption != -1) {
                return exemption;
            }
        }
        android.app.ActivityManagerInternal ami = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        if (ami == null) {
            return 0;
        }
        int procState = ami.getUidProcessState(callingUid);
        boolean isUidActive = ami.isUidActive(callingUid);
        if (procState <= 2 || procState == 3) {
            return 2;
        }
        if (procState > 6 && !isUidActive) {
            return 0;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SYNC_EXEMPTION_OCCURRED, callingUid, getProcStateForStatsd(procState), isUidActive, getRestrictionLevelForStatsd(ami.getRestrictionLevel(callingUid)));
        return 1;
    }

    private int getProcStateForStatsd(int procState) {
        switch (procState) {
        }
        return 0;
    }

    private int getRestrictionLevelForStatsd(int level) {
        switch (level) {
            case 0:
                break;
            case 10:
                break;
            case 20:
                break;
            case 30:
                break;
            case 40:
                break;
            case 50:
                break;
            case 60:
                break;
        }
        return 0;
    }

    public static final class ObserverNode {
        private java.lang.String mName;
        private java.util.ArrayList<com.android.server.content.ContentService.ObserverNode> mChildren = new java.util.ArrayList<>();
        private java.util.ArrayList<com.android.server.content.ContentService.ObserverNode.ObserverEntry> mObservers = new java.util.ArrayList<>();

        private class ObserverEntry implements android.os.IBinder.DeathRecipient {
            public final boolean notifyForDescendants;
            public final android.database.IContentObserver observer;
            private final java.lang.Object observersLock;
            public final int pid;
            public final int uid;
            private final int userHandle;

            public ObserverEntry(android.database.IContentObserver o, boolean n, java.lang.Object observersLock, int _uid, int _pid, int _userHandle, android.net.Uri uri) {
                boolean alreadyDetected;
                this.observersLock = observersLock;
                this.observer = o;
                this.uid = _uid;
                this.pid = _pid;
                this.userHandle = _userHandle;
                this.notifyForDescendants = n;
                int entries = com.android.server.content.ContentService.sObserverDeathDispatcher.linkToDeath(this.observer, this);
                if (entries == -1) {
                    binderDied();
                    return;
                }
                if (entries == 1000) {
                    synchronized (com.android.server.content.ContentService.sObserverLeakDetectedUid) {
                        alreadyDetected = com.android.server.content.ContentService.sObserverLeakDetectedUid.contains(java.lang.Integer.valueOf(this.uid));
                        if (!alreadyDetected) {
                            com.android.server.content.ContentService.sObserverLeakDetectedUid.add(java.lang.Integer.valueOf(this.uid));
                        }
                    }
                    if (!alreadyDetected) {
                        java.lang.String caller = null;
                        try {
                            caller = (java.lang.String) com.android.internal.util.ArrayUtils.firstOrNull(android.app.AppGlobals.getPackageManager().getPackagesForUid(this.uid));
                        } catch (android.os.RemoteException e) {
                        }
                        android.util.Slog.wtf(com.android.server.content.ContentService.TAG, "Observer registered too many times. Leak? cpid=" + this.pid + " cuid=" + this.uid + " cpkg=" + caller + " url=" + uri);
                    }
                }
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                synchronized (this.observersLock) {
                    com.android.server.content.ContentService.ObserverNode.this.removeObserverLocked(this.observer);
                }
            }

            public void dumpLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, java.lang.String name, java.lang.String prefix, android.util.SparseIntArray pidCounts) {
                pidCounts.put(this.pid, pidCounts.get(this.pid) + 1);
                pw.print(prefix);
                pw.print(name);
                pw.print(": pid=");
                pw.print(this.pid);
                pw.print(" uid=");
                pw.print(this.uid);
                pw.print(" user=");
                pw.print(this.userHandle);
                pw.print(" target=");
                pw.println(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.observer != null ? this.observer.asBinder() : null)));
            }
        }

        public ObserverNode(java.lang.String name) {
            this.mName = name;
        }

        public void dumpLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, java.lang.String name, java.lang.String prefix, int[] counts, android.util.SparseIntArray pidCounts) {
            java.lang.String innerName;
            java.lang.String innerName2 = null;
            if (this.mObservers.size() > 0) {
                if (!"".equals(name)) {
                    innerName2 = name + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mName;
                } else {
                    innerName2 = this.mName;
                }
                for (int i = 0; i < this.mObservers.size(); i++) {
                    counts[1] = counts[1] + 1;
                    this.mObservers.get(i).dumpLocked(fd, pw, args, innerName2, prefix, pidCounts);
                }
            }
            if (this.mChildren.size() > 0) {
                if (innerName2 != null) {
                    innerName = innerName2;
                } else if (!"".equals(name)) {
                    java.lang.String innerName3 = name + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mName;
                    innerName = innerName3;
                } else {
                    java.lang.String innerName4 = this.mName;
                    innerName = innerName4;
                }
                for (int i2 = 0; i2 < this.mChildren.size(); i2++) {
                    counts[0] = counts[0] + 1;
                    this.mChildren.get(i2).dumpLocked(fd, pw, args, innerName, prefix, counts, pidCounts);
                }
            }
        }

        public static java.lang.String getUriSegment(android.net.Uri uri, int index) {
            if (uri != null) {
                if (index == 0) {
                    return uri.getAuthority();
                }
                return uri.getPathSegments().get(index - 1);
            }
            return null;
        }

        public static int countUriSegments(android.net.Uri uri) {
            if (uri == null) {
                return 0;
            }
            return uri.getPathSegments().size() + 1;
        }

        public void addObserverLocked(android.net.Uri uri, android.database.IContentObserver observer, boolean notifyForDescendants, java.lang.Object observersLock, int uid, int pid, int userHandle) {
            addObserverLocked(uri, 0, observer, notifyForDescendants, observersLock, uid, pid, userHandle);
        }

        private void addObserverLocked(android.net.Uri uri, int index, android.database.IContentObserver observer, boolean notifyForDescendants, java.lang.Object observersLock, int uid, int pid, int userHandle) {
            if (index == countUriSegments(uri)) {
                for (com.android.server.content.ContentService.ObserverNode.ObserverEntry next : this.mObservers) {
                    if (next.observer.asBinder() == observer.asBinder()) {
                        android.util.Log.w(com.android.server.content.ContentService.TAG, "Observer is already registered. Pid is " + android.os.Binder.getCallingPid());
                        return;
                    }
                }
                this.mObservers.add(new com.android.server.content.ContentService.ObserverNode.ObserverEntry(observer, notifyForDescendants, observersLock, uid, pid, userHandle, uri));
                com.android.server.content.ContentService.sContentServiceExt.addProxyBinder(observer.asBinder(), uid, pid);
                return;
            }
            java.lang.String segment = getUriSegment(uri, index);
            if (segment == null) {
                throw new java.lang.IllegalArgumentException("Invalid Uri (" + uri + ") used for observer");
            }
            int N = this.mChildren.size();
            for (int i = 0; i < N; i++) {
                com.android.server.content.ContentService.ObserverNode node = this.mChildren.get(i);
                if (node.mName.equals(segment)) {
                    node.addObserverLocked(uri, index + 1, observer, notifyForDescendants, observersLock, uid, pid, userHandle);
                    return;
                }
            }
            com.android.server.content.ContentService.ObserverNode node2 = new com.android.server.content.ContentService.ObserverNode(segment);
            this.mChildren.add(node2);
            node2.addObserverLocked(uri, index + 1, observer, notifyForDescendants, observersLock, uid, pid, userHandle);
        }

        public boolean removeObserverLocked(android.database.IContentObserver observer) {
            int size = this.mChildren.size();
            int i = 0;
            while (i < size) {
                boolean empty = this.mChildren.get(i).removeObserverLocked(observer);
                if (empty) {
                    this.mChildren.remove(i);
                    i--;
                    size--;
                }
                i++;
            }
            android.os.IBinder observerBinder = observer.asBinder();
            int size2 = this.mObservers.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size2) {
                    break;
                }
                com.android.server.content.ContentService.ObserverNode.ObserverEntry entry = this.mObservers.get(i2);
                if (entry.observer.asBinder() != observerBinder) {
                    i2++;
                } else {
                    this.mObservers.remove(i2);
                    com.android.server.content.ContentService.sObserverDeathDispatcher.unlinkToDeath(observer, entry);
                    com.android.server.content.ContentService.sContentServiceExt.removeProxyBinder(observerBinder, entry.uid);
                    break;
                }
            }
            return this.mChildren.size() == 0 && this.mObservers.size() == 0;
        }

        /* JADX WARN: Removed duplicated region for block: B:31:0x004f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void collectMyObserversLocked(android.net.Uri r15, boolean r16, android.database.IContentObserver r17, boolean r18, int r19, int r20, com.android.server.content.ContentService.ObserverCollector r21) {
            /*
                r14 = this;
                r0 = r14
                r8 = r20
                java.util.ArrayList<com.android.server.content.ContentService$ObserverNode$ObserverEntry> r1 = r0.mObservers
                int r9 = r1.size()
                if (r17 != 0) goto Ld
                r1 = 0
                goto L11
            Ld:
                android.os.IBinder r1 = r17.asBinder()
            L11:
                r10 = r1
                r1 = 0
                r11 = r1
            L14:
                if (r11 >= r9) goto L61
                java.util.ArrayList<com.android.server.content.ContentService$ObserverNode$ObserverEntry> r1 = r0.mObservers
                java.lang.Object r1 = r1.get(r11)
                r12 = r1
                com.android.server.content.ContentService$ObserverNode$ObserverEntry r12 = (com.android.server.content.ContentService.ObserverNode.ObserverEntry) r12
                android.database.IContentObserver r1 = r12.observer
                android.os.IBinder r1 = r1.asBinder()
                if (r1 != r10) goto L29
                r1 = 1
                goto L2a
            L29:
                r1 = 0
            L2a:
                r13 = r1
                if (r13 == 0) goto L30
                if (r18 != 0) goto L30
                goto L5e
            L30:
                r1 = -1
                if (r8 == r1) goto L3f
                int r2 = com.android.server.content.ContentService.ObserverNode.ObserverEntry.m2957$$Nest$fgetuserHandle(r12)
                if (r2 == r1) goto L3f
                int r1 = com.android.server.content.ContentService.ObserverNode.ObserverEntry.m2957$$Nest$fgetuserHandle(r12)
                if (r8 != r1) goto L5e
            L3f:
                if (r16 == 0) goto L4a
                r1 = r19 & 2
                if (r1 == 0) goto L4f
                boolean r1 = r12.notifyForDescendants
                if (r1 == 0) goto L4f
                goto L5e
            L4a:
                boolean r1 = r12.notifyForDescendants
                if (r1 != 0) goto L4f
                goto L5e
            L4f:
                android.database.IContentObserver r2 = r12.observer
                int r3 = r12.uid
                r1 = r21
                r4 = r13
                r5 = r15
                r6 = r19
                r7 = r20
                r1.collect(r2, r3, r4, r5, r6, r7)
            L5e:
                int r11 = r11 + 1
                goto L14
            L61:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.content.ContentService.ObserverNode.collectMyObserversLocked(android.net.Uri, boolean, android.database.IContentObserver, boolean, int, int, com.android.server.content.ContentService$ObserverCollector):void");
        }

        public void collectObserversLocked(android.net.Uri uri, int index, android.database.IContentObserver observer, boolean observerWantsSelfNotifications, int flags, int targetUserHandle, com.android.server.content.ContentService.ObserverCollector collector) {
            collectObserversLocked(uri, countUriSegments(uri), index, observer, observerWantsSelfNotifications, flags, targetUserHandle, collector);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0046  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void collectObserversLocked(android.net.Uri r19, int r20, int r21, android.database.IContentObserver r22, boolean r23, int r24, int r25, com.android.server.content.ContentService.ObserverCollector r26) {
            /*
                r18 = this;
                r8 = r18
                r15 = r20
                r14 = r21
                r9 = 0
                if (r14 < r15) goto L1e
                r2 = 1
                r0 = r18
                r1 = r19
                r3 = r22
                r4 = r23
                r5 = r24
                r6 = r25
                r7 = r26
                r0.collectMyObserversLocked(r1, r2, r3, r4, r5, r6, r7)
                r13 = r19
                goto L3c
            L1e:
                if (r14 >= r15) goto L3a
                r13 = r19
                java.lang.String r9 = getUriSegment(r13, r14)
                r2 = 0
                r0 = r18
                r1 = r19
                r3 = r22
                r4 = r23
                r5 = r24
                r6 = r25
                r7 = r26
                r0.collectMyObserversLocked(r1, r2, r3, r4, r5, r6, r7)
                r0 = r9
                goto L3d
            L3a:
                r13 = r19
            L3c:
                r0 = r9
            L3d:
                java.util.ArrayList<com.android.server.content.ContentService$ObserverNode> r1 = r8.mChildren
                int r1 = r1.size()
                r2 = 0
            L44:
                if (r2 >= r1) goto L78
                java.util.ArrayList<com.android.server.content.ContentService$ObserverNode> r3 = r8.mChildren
                java.lang.Object r3 = r3.get(r2)
                com.android.server.content.ContentService$ObserverNode r3 = (com.android.server.content.ContentService.ObserverNode) r3
                if (r0 == 0) goto L58
                java.lang.String r4 = r3.mName
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L6f
            L58:
                int r12 = r14 + 1
                r9 = r3
                r10 = r19
                r11 = r20
                r13 = r22
                r14 = r23
                r15 = r24
                r16 = r25
                r17 = r26
                r9.collectObserversLocked(r10, r11, r12, r13, r14, r15, r16, r17)
                if (r0 == 0) goto L6f
                goto L78
            L6f:
                int r2 = r2 + 1
                r13 = r19
                r15 = r20
                r14 = r21
                goto L44
            L78:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.content.ContentService.ObserverNode.collectObserversLocked(android.net.Uri, int, int, android.database.IContentObserver, boolean, int, int, com.android.server.content.ContentService$ObserverCollector):void");
        }
    }

    private void enforceShell(java.lang.String method) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 2000 && callingUid != 0) {
            throw new java.lang.SecurityException("Non-shell user attempted to call " + method);
        }
    }

    public void resetTodayStats() {
        enforceShell("resetTodayStats");
        if (this.mSyncManager != null) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                this.mSyncManager.resetTodayStats();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    public void onDbCorruption(java.lang.String tag, java.lang.String message, java.lang.String stacktrace) {
        android.util.Slog.e(tag, message);
        android.util.Slog.e(tag, "at " + stacktrace);
        android.util.Slog.wtf(tag, message);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.content.ContentShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }
}
