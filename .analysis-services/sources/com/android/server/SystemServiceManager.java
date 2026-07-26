package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class SystemServiceManager implements android.util.Dumpable {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_MAX_USER_POOL_THREADS = 3;
    private static final int SERVICE_CALL_WARN_TIME_MS = 50;
    private static final java.lang.String TAG = com.android.server.SystemServiceManager.class.getSimpleName();
    private static final java.lang.String USER_COMPLETED_EVENT = "CompletedEvent";
    private static final long USER_POOL_SHUTDOWN_TIMEOUT_SECONDS = 30;
    private static final java.lang.String USER_STARTING = "Start";
    private static final java.lang.String USER_STOPPED = "Cleanup";
    private static final java.lang.String USER_STOPPING = "Stop";
    private static final java.lang.String USER_SWITCHING = "Switch";
    private static final java.lang.String USER_UNLOCKED = "Unlocked";
    private static final java.lang.String USER_UNLOCKING = "Unlocking";
    private static volatile int sOtherServicesStartIndex;
    private static java.io.File sSystemDir;
    private final android.content.Context mContext;
    private com.android.server.SystemService.TargetUser mCurrentUser;
    private boolean mRuntimeRestarted;
    private long mRuntimeStartElapsedTime;
    private long mRuntimeStartUptime;
    private boolean mSafeMode;
    private com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private int mCurrentPhase = -1;
    private final android.util.SparseArray<com.android.server.SystemService.TargetUser> mTargetUsers = new android.util.SparseArray<>();
    private com.android.server.ISystemServiceManagerExt mSystemServiceManagerExt = (com.android.server.ISystemServiceManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.ISystemServiceManagerExt.class).create();
    private java.util.List<com.android.server.SystemService> mServices = new java.util.ArrayList();
    private java.util.Set<java.lang.String> mServiceClassnames = new android.util.ArraySet();
    private final int mNumUserPoolThreads = java.lang.Math.min(java.lang.Runtime.getRuntime().availableProcessors(), 3);

    public SystemServiceManager(android.content.Context context) {
        this.mContext = context;
    }

    public com.android.server.SystemService startService(java.lang.String className) {
        java.lang.Class<com.android.server.SystemService> serviceClass = loadClassFromLoader(className, getClass().getClassLoader());
        return startService(serviceClass);
    }

    public com.android.server.SystemService startServiceFromJar(java.lang.String className, java.lang.String path) {
        dalvik.system.PathClassLoader pathClassLoader = com.android.internal.os.SystemServerClassLoaderFactory.getOrCreateClassLoader(path, getClass().getClassLoader(), isJarInTestApex(path));
        java.lang.Class<com.android.server.SystemService> serviceClass = loadClassFromLoader(className, pathClassLoader);
        return startService(serviceClass);
    }

    private boolean isJarInTestApex(java.lang.String pathStr) {
        java.nio.file.Path path = java.nio.file.Paths.get(pathStr, new java.lang.String[0]);
        if (path.getNameCount() >= 2 && path.getName(0).toString().equals("apex")) {
            java.lang.String apexModuleName = path.getName(1).toString();
            com.android.server.pm.ApexManager apexManager = com.android.server.pm.ApexManager.getInstance();
            java.lang.String packageName = apexManager.getActivePackageNameForApexModuleName(apexModuleName);
            try {
                android.content.pm.PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(1073741824L));
                if ((packageInfo.applicationInfo.flags & 256) == 0) {
                    return false;
                }
                return true;
            } catch (java.lang.Exception e) {
            }
        }
        return false;
    }

    private static java.lang.Class<com.android.server.SystemService> loadClassFromLoader(java.lang.String className, java.lang.ClassLoader classLoader) {
        try {
            return java.lang.Class.forName(className, true, classLoader);
        } catch (java.lang.ClassNotFoundException ex) {
            throw new java.lang.RuntimeException("Failed to create service " + className + " from class loader " + classLoader.toString() + ": service class not found, usually indicates that the caller should have called PackageManager.hasSystemFeature() to check whether the feature is available on this device before trying to start the services that implement it. Also ensure that the correct path for the classloader is supplied, if applicable.", ex);
        }
    }

    public <T extends com.android.server.SystemService> T startService(java.lang.Class<T> serviceClass) {
        try {
            java.lang.String name = serviceClass.getName();
            android.util.Slog.i(TAG, "Starting " + name);
            android.os.Trace.traceBegin(524288L, "StartService " + name);
            if (!com.android.server.SystemService.class.isAssignableFrom(serviceClass)) {
                throw new java.lang.RuntimeException("Failed to create " + name + ": service must extend " + com.android.server.SystemService.class.getName());
            }
            try {
                try {
                    try {
                        java.lang.reflect.Constructor<T> constructor = serviceClass.getConstructor(android.content.Context.class);
                        T service = constructor.newInstance(this.mContext);
                        startService(service);
                        return service;
                    } catch (java.lang.IllegalAccessException ex) {
                        throw new java.lang.RuntimeException("Failed to create service " + name + ": service must have a public constructor with a Context argument", ex);
                    }
                } catch (java.lang.InstantiationException ex2) {
                    throw new java.lang.RuntimeException("Failed to create service " + name + ": service could not be instantiated", ex2);
                }
            } catch (java.lang.NoSuchMethodException ex3) {
                throw new java.lang.RuntimeException("Failed to create service " + name + ": service must have a public constructor with a Context argument", ex3);
            } catch (java.lang.reflect.InvocationTargetException ex4) {
                throw new java.lang.RuntimeException("Failed to create service " + name + ": service constructor threw an exception", ex4);
            }
        } finally {
            android.os.Trace.traceEnd(524288L);
        }
    }

    public void startService(com.android.server.SystemService service) {
        java.lang.String className = service.getClass().getName();
        if (this.mServiceClassnames.contains(className)) {
            android.util.Slog.i(TAG, "Not starting an already started service " + className);
            return;
        }
        this.mServiceClassnames.add(className);
        this.mServices.add(service);
        long time = android.os.SystemClock.elapsedRealtime();
        try {
            service.onStart();
            warnIfTooLong(android.os.SystemClock.elapsedRealtime() - time, service, "onStart");
        } catch (java.lang.RuntimeException ex) {
            throw new java.lang.RuntimeException("Failed to start service " + service.getClass().getName() + ": onStart threw an exception", ex);
        }
    }

    public void sealStartedServices() {
        this.mServiceClassnames = java.util.Collections.emptySet();
        this.mServices = java.util.Collections.unmodifiableList(this.mServices);
    }

    public void startBootPhase(com.android.server.utils.TimingsTraceAndSlog t, int phase) {
        if (phase <= this.mCurrentPhase) {
            throw new java.lang.IllegalArgumentException("Next phase must be larger than previous");
        }
        this.mCurrentPhase = phase;
        android.util.Slog.i(TAG, "Starting phase " + this.mCurrentPhase);
        try {
            t.traceBegin("OnBootPhase_" + phase);
            this.mSystemServiceManagerExt.colorSystemServiceOnBootPhase(phase);
            int serviceLen = this.mServices.size();
            for (int i = 0; i < serviceLen; i++) {
                com.android.server.SystemService service = this.mServices.get(i);
                long time = android.os.SystemClock.elapsedRealtime();
                t.traceBegin("OnBootPhase_" + phase + "_" + service.getClass().getName());
                try {
                    service.onBootPhase(this.mCurrentPhase);
                    warnIfTooLong(android.os.SystemClock.elapsedRealtime() - time, service, "onBootPhase");
                    t.traceEnd();
                } catch (java.lang.Exception ex) {
                    throw new java.lang.RuntimeException("Failed to boot service " + service.getClass().getName() + ": onBootPhase threw an exception during phase " + this.mCurrentPhase, ex);
                }
            }
            t.traceEnd();
            if (phase == 1000) {
                long totalBootTime = android.os.SystemClock.uptimeMillis() - this.mRuntimeStartUptime;
                t.logDuration("TotalBootTime", totalBootTime);
                shutdownInitThreadPool();
            }
        } catch (java.lang.Throwable th) {
            t.traceEnd();
            throw th;
        }
    }

    private void shutdownInitThreadPool() {
        com.android.server.SystemServerInitThreadPool.shutdown();
    }

    private void shutdownInitThreadPool$ravenwood() {
    }

    public boolean isBootCompleted() {
        return this.mCurrentPhase >= 1000;
    }

    public void updateOtherServicesStartIndex() {
        if (!isBootCompleted()) {
            sOtherServicesStartIndex = this.mServices.size();
        }
    }

    public void preSystemReady() {
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
    }

    private com.android.server.SystemService.TargetUser getTargetUser(int userId) {
        com.android.server.SystemService.TargetUser targetUser;
        synchronized (this.mTargetUsers) {
            targetUser = this.mTargetUsers.get(userId);
        }
        return targetUser;
    }

    private com.android.server.SystemService.TargetUser newTargetUser(int userId) {
        android.content.pm.UserInfo userInfo = this.mUserManagerInternal.getUserInfo(userId);
        com.android.internal.util.Preconditions.checkState(userInfo != null, "No UserInfo for " + userId);
        return new com.android.server.SystemService.TargetUser(userInfo);
    }

    public void onUserStarting(com.android.server.utils.TimingsTraceAndSlog t, int userId) throws java.lang.InterruptedException {
        com.android.server.SystemService.TargetUser targetUser = newTargetUser(userId);
        synchronized (this.mTargetUsers) {
            if (userId == 0) {
                if (this.mTargetUsers.contains(userId)) {
                    android.util.Slog.e(TAG, "Skipping starting system user twice");
                    return;
                }
            }
            this.mTargetUsers.put(userId, targetUser);
            android.util.Slog.d(TAG, "put userid " + userId + " to mTargetUsers, result = " + (this.mTargetUsers.get(userId) != null ? "true" : "false"));
            android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_STARTING, userId);
            onUser(t, USER_STARTING, null, targetUser);
        }
    }

    public void onUserUnlocking(int userId) throws java.lang.InterruptedException {
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_UNLOCKING, userId);
        onUser(USER_UNLOCKING, userId);
    }

    public void onUserUnlocked(int userId) throws java.lang.InterruptedException {
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_UNLOCKED, userId);
        onUser(USER_UNLOCKED, userId);
    }

    public void onUserSwitching(int from, int to) throws java.lang.InterruptedException {
        com.android.server.SystemService.TargetUser prevUser;
        com.android.server.SystemService.TargetUser curUser;
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_SWITCHING, java.lang.Integer.valueOf(from), java.lang.Integer.valueOf(to));
        synchronized (this.mTargetUsers) {
            if (this.mCurrentUser == null) {
                prevUser = newTargetUser(from);
            } else {
                com.android.server.SystemService.TargetUser prevUser2 = this.mCurrentUser;
                if (from != prevUser2.getUserIdentifier()) {
                    android.util.Slog.wtf(TAG, "switchUser(" + from + "," + to + "): mCurrentUser is " + this.mCurrentUser + ", it should be " + from);
                }
                prevUser = this.mCurrentUser;
            }
            curUser = getTargetUser(to);
            this.mCurrentUser = curUser;
            com.android.internal.util.Preconditions.checkState(curUser != null, "No TargetUser for " + to);
        }
        onUser(com.android.server.utils.TimingsTraceAndSlog.newAsyncLog(), USER_SWITCHING, prevUser, curUser);
    }

    public void onUserStopping(int userId) throws java.lang.InterruptedException {
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_STOPPING, userId);
        onUser(USER_STOPPING, userId);
    }

    public void onUserStopped(int userId) throws java.lang.InterruptedException {
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_STOPPED, userId);
        onUser(USER_STOPPED, userId);
        synchronized (this.mTargetUsers) {
            this.mTargetUsers.remove(userId);
            android.util.Slog.d(TAG, "removed userid " + userId + " from mTargetUsers, result = " + (this.mTargetUsers.get(userId) == null ? "true" : "false"));
        }
    }

    public void onUserCompletedEvent(int userId, int eventFlags) throws java.lang.InterruptedException {
        com.android.server.SystemService.TargetUser targetUser;
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_COMPLETED_EVENT, java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(eventFlags));
        if (eventFlags == 0 || (targetUser = getTargetUser(userId)) == null) {
            return;
        }
        onUser(com.android.server.utils.TimingsTraceAndSlog.newAsyncLog(), USER_COMPLETED_EVENT, null, targetUser, new com.android.server.SystemService.UserCompletedEventType(eventFlags));
    }

    private void onUser(java.lang.String onWhat, int userId) throws java.lang.InterruptedException {
        com.android.server.SystemService.TargetUser targetUser = getTargetUser(userId);
        com.android.internal.util.Preconditions.checkState(targetUser != null, "No TargetUser for " + userId);
        onUser(com.android.server.utils.TimingsTraceAndSlog.newAsyncLog(), onWhat, null, targetUser);
    }

    private void onUser(com.android.server.utils.TimingsTraceAndSlog t, java.lang.String onWhat, com.android.server.SystemService.TargetUser prevUser, com.android.server.SystemService.TargetUser curUser) throws java.lang.InterruptedException {
        onUser(t, onWhat, prevUser, curUser, null);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0244 A[Catch: Exception -> 0x02b1, TryCatch #4 {Exception -> 0x02b1, blocks: (B:59:0x0201, B:80:0x02b3, B:81:0x02c9, B:67:0x023f, B:68:0x0244, B:69:0x0256, B:70:0x0267, B:73:0x0287, B:75:0x0292, B:74:0x028f, B:76:0x0298), top: B:112:0x023f }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0256 A[Catch: Exception -> 0x02b1, TryCatch #4 {Exception -> 0x02b1, blocks: (B:59:0x0201, B:80:0x02b3, B:81:0x02c9, B:67:0x023f, B:68:0x0244, B:69:0x0256, B:70:0x0267, B:73:0x0287, B:75:0x0292, B:74:0x028f, B:76:0x0298), top: B:112:0x023f }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0267 A[Catch: Exception -> 0x02b1, TryCatch #4 {Exception -> 0x02b1, blocks: (B:59:0x0201, B:80:0x02b3, B:81:0x02c9, B:67:0x023f, B:68:0x0244, B:69:0x0256, B:70:0x0267, B:73:0x0287, B:75:0x0292, B:74:0x028f, B:76:0x0298), top: B:112:0x023f }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0298 A[Catch: Exception -> 0x02b1, TryCatch #4 {Exception -> 0x02b1, blocks: (B:59:0x0201, B:80:0x02b3, B:81:0x02c9, B:67:0x023f, B:68:0x0244, B:69:0x0256, B:70:0x0267, B:73:0x0287, B:75:0x0292, B:74:0x028f, B:76:0x0298), top: B:112:0x023f }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x02df  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x030b  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x031e  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0351  */
    /*  JADX ERROR: UnsupportedOperationException in pass: RegionMakerVisitor
        java.lang.UnsupportedOperationException
        	at java.base/java.util.Collections$UnmodifiableCollection.add(Unknown Source)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker$1.leaveRegion(SwitchRegionMaker.java:390)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:23)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaksForCase(SwitchRegionMaker.java:370)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaks(SwitchRegionMaker.java:85)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.leaveRegion(PostProcessRegions.java:33)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.process(PostProcessRegions.java:23)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:31)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onUser(com.android.server.utils.TimingsTraceAndSlog r27, java.lang.String r28, com.android.server.SystemService.TargetUser r29, com.android.server.SystemService.TargetUser r30, com.android.server.SystemService.UserCompletedEventType r31) throws java.lang.InterruptedException {
        /*
            Method dump skipped, instruction units count: 958
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemServiceManager.onUser(com.android.server.utils.TimingsTraceAndSlog, java.lang.String, com.android.server.SystemService$TargetUser, com.android.server.SystemService$TargetUser, com.android.server.SystemService$UserCompletedEventType):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x001e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean useThreadPool(int r4, java.lang.String r5) {
        /*
            r3 = this;
            int r0 = r5.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case 80204866: goto L14;
                case 537825071: goto La;
                default: goto L9;
            }
        L9:
            goto L1e
        La:
            java.lang.String r0 = "CompletedEvent"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L9
            r0 = r2
            goto L1f
        L14:
            java.lang.String r0 = "Start"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L9
            r0 = r1
            goto L1f
        L1e:
            r0 = -1
        L1f:
            switch(r0) {
                case 0: goto L24;
                case 1: goto L23;
                default: goto L22;
            }
        L22:
            return r1
        L23:
            return r2
        L24:
            boolean r0 = android.app.ActivityManager.isLowRamDeviceStatic()
            if (r0 != 0) goto L2d
            if (r4 == 0) goto L2d
            r1 = r2
        L2d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemServiceManager.useThreadPool(int, java.lang.String):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x001e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean useThreadPoolForService(java.lang.String r4, int r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case 80204866: goto L14;
                case 537825071: goto La;
                default: goto L9;
            }
        L9:
            goto L1e
        La:
            java.lang.String r0 = "CompletedEvent"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L9
            r0 = r2
            goto L1f
        L14:
            java.lang.String r0 = "Start"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L9
            r0 = r1
            goto L1f
        L1e:
            r0 = -1
        L1f:
            switch(r0) {
                case 0: goto L24;
                case 1: goto L23;
                default: goto L22;
            }
        L22:
            return r1
        L23:
            return r2
        L24:
            int r0 = com.android.server.SystemServiceManager.sOtherServicesStartIndex
            if (r5 < r0) goto L29
            r1 = r2
        L29:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemServiceManager.useThreadPoolForService(java.lang.String, int):boolean");
    }

    private java.lang.Runnable getOnUserStartingRunnable(final com.android.server.utils.TimingsTraceAndSlog oldTrace, final com.android.server.SystemService service, final com.android.server.SystemService.TargetUser curUser) {
        return new java.lang.Runnable() { // from class: com.android.server.SystemServiceManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getOnUserStartingRunnable$0(oldTrace, service, curUser);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnUserStartingRunnable$0(com.android.server.utils.TimingsTraceAndSlog oldTrace, com.android.server.SystemService service, com.android.server.SystemService.TargetUser curUser) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(oldTrace);
        java.lang.String serviceName = service.getClass().getName();
        int curUserId = curUser.getUserIdentifier();
        t.traceBegin("ssm.onStartUser-" + curUserId + "_" + serviceName);
        try {
            try {
                long time = android.os.SystemClock.elapsedRealtime();
                service.onUserStarting(curUser);
                warnIfTooLong(android.os.SystemClock.elapsedRealtime() - time, service, "onStartUser-" + curUserId);
            } catch (java.lang.Exception e) {
                logFailure(USER_STARTING, curUser, serviceName, e);
            }
        } finally {
            t.traceEnd();
        }
    }

    private java.lang.Runnable getOnUserCompletedEventRunnable(final com.android.server.utils.TimingsTraceAndSlog oldTrace, final com.android.server.SystemService service, final java.lang.String serviceName, final com.android.server.SystemService.TargetUser curUser, final com.android.server.SystemService.UserCompletedEventType eventType) {
        return new java.lang.Runnable() { // from class: com.android.server.SystemServiceManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getOnUserCompletedEventRunnable$1(oldTrace, curUser, eventType, serviceName, service);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnUserCompletedEventRunnable$1(com.android.server.utils.TimingsTraceAndSlog oldTrace, com.android.server.SystemService.TargetUser curUser, com.android.server.SystemService.UserCompletedEventType eventType, java.lang.String serviceName, com.android.server.SystemService service) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(oldTrace);
        int curUserId = curUser.getUserIdentifier();
        t.traceBegin("ssm.onCompletedEventUser-" + curUserId + "_" + eventType + "_" + serviceName);
        try {
            try {
                long time = android.os.SystemClock.elapsedRealtime();
                service.onUserCompletedEvent(curUser, eventType);
                warnIfTooLong(android.os.SystemClock.elapsedRealtime() - time, service, "onCompletedEventUser-" + curUserId);
            } catch (java.lang.Exception e) {
                logFailure(USER_COMPLETED_EVENT, curUser, serviceName, e);
                throw e;
            }
        } finally {
            t.traceEnd();
        }
    }

    private void logFailure(java.lang.String onWhat, com.android.server.SystemService.TargetUser curUser, java.lang.String serviceName, java.lang.Exception ex) {
        android.util.Slog.wtf(TAG, "SystemService failure: Failure reporting " + onWhat + " of user " + curUser + " to service " + serviceName, ex);
    }

    void setSafeMode(boolean safeMode) {
        this.mSafeMode = safeMode;
    }

    public boolean isSafeMode() {
        return this.mSafeMode;
    }

    public boolean isRuntimeRestarted() {
        return this.mRuntimeRestarted;
    }

    public long getRuntimeStartElapsedTime() {
        return this.mRuntimeStartElapsedTime;
    }

    public long getRuntimeStartUptime() {
        return this.mRuntimeStartUptime;
    }

    public void setStartInfo(boolean runtimeRestarted, long runtimeStartElapsedTime, long runtimeStartUptime) {
        this.mRuntimeRestarted = runtimeRestarted;
        this.mRuntimeStartElapsedTime = runtimeStartElapsedTime;
        this.mRuntimeStartUptime = runtimeStartUptime;
    }

    private void warnIfTooLong(long duration, com.android.server.SystemService service, java.lang.String operation) {
        if (duration > 50) {
            android.util.Slog.w(TAG, "Service " + service.getClass().getName() + " took " + duration + " ms in " + operation);
        }
    }

    @java.lang.Deprecated
    public static java.io.File ensureSystemDir() {
        if (sSystemDir == null) {
            java.io.File dataDir = android.os.Environment.getDataDirectory();
            sSystemDir = new java.io.File(dataDir, "system");
            sSystemDir.mkdirs();
        }
        java.io.File dataDir2 = sSystemDir;
        return dataDir2;
    }

    @Override // android.util.Dumpable
    public java.lang.String getDumpableName() {
        return com.android.server.SystemServiceManager.class.getSimpleName();
    }

    @Override // android.util.Dumpable
    public void dump(java.io.PrintWriter pw, java.lang.String[] args) {
        pw.printf("Current phase: %d\n", java.lang.Integer.valueOf(this.mCurrentPhase));
        synchronized (this.mTargetUsers) {
            if (this.mCurrentUser != null) {
                pw.print("Current user: ");
                this.mCurrentUser.dump(pw);
                pw.println();
            } else {
                pw.println("Current user not set!");
            }
            int targetUsersSize = this.mTargetUsers.size();
            if (targetUsersSize > 0) {
                pw.printf("%d target users: ", java.lang.Integer.valueOf(targetUsersSize));
                for (int i = 0; i < targetUsersSize; i++) {
                    this.mTargetUsers.valueAt(i).dump(pw);
                    if (i != targetUsersSize - 1) {
                        pw.print(", ");
                    }
                }
                pw.println();
            } else {
                pw.println("No target users");
            }
        }
        int startedLen = this.mServices.size();
        if (startedLen > 0) {
            pw.printf("%d started services:\n", java.lang.Integer.valueOf(startedLen));
            for (int i2 = 0; i2 < startedLen; i2++) {
                com.android.server.SystemService service = this.mServices.get(i2);
                pw.print("  ");
                pw.println(service.getClass().getCanonicalName());
            }
            return;
        }
        pw.println("No started services");
    }
}
