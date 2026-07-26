package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppFGSTracker extends com.android.server.am.BaseAppStateDurationsTracker<com.android.server.am.AppFGSTracker.AppFGSPolicy, com.android.server.am.AppFGSTracker.PackageDurations> implements android.app.ActivityManagerInternal.ForegroundServiceStateListener {
    static final boolean DEBUG_BACKGROUND_FGS_TRACKER = false;
    static final java.lang.String TAG = "ActivityManager";
    private final com.android.server.am.UidProcessMap<android.util.SparseBooleanArray> mFGSNotificationIDs;
    private final com.android.server.am.AppFGSTracker.MyHandler mHandler;
    final com.android.server.am.AppFGSTracker.NotificationListener mNotificationListener;
    final android.app.IProcessObserver.Stub mProcessObserver;
    private final android.util.ArrayMap<com.android.server.am.AppFGSTracker.PackageDurations, java.lang.Long> mTmpPkgDurations;

    public void onForegroundServiceStateChanged(java.lang.String packageName, int uid, int pid, boolean started) {
        this.mHandler.obtainMessage(started ? 0 : 1, pid, uid, packageName).sendToTarget();
    }

    public void onForegroundServiceNotificationUpdated(java.lang.String packageName, int uid, int foregroundId, boolean canceling) {
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.argi1 = uid;
        args.argi2 = foregroundId;
        args.arg1 = packageName;
        args.arg2 = canceling ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE;
        this.mHandler.obtainMessage(3, args).sendToTarget();
    }

    private static class MyHandler extends android.os.Handler {
        static final int MSG_CHECK_LONG_RUNNING_FGS = 4;
        static final int MSG_FOREGROUND_SERVICES_CHANGED = 2;
        static final int MSG_FOREGROUND_SERVICES_NOTIFICATION_UPDATED = 3;
        static final int MSG_FOREGROUND_SERVICES_STARTED = 0;
        static final int MSG_FOREGROUND_SERVICES_STOPPED = 1;
        static final int MSG_NOTIFICATION_POSTED = 5;
        static final int MSG_NOTIFICATION_REMOVED = 6;
        private final com.android.server.am.AppFGSTracker mTracker;

        MyHandler(com.android.server.am.AppFGSTracker tracker) {
            super(tracker.mBgHandler.getLooper());
            this.mTracker = tracker;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 0:
                    this.mTracker.handleForegroundServicesChanged((java.lang.String) msg.obj, msg.arg1, msg.arg2, true);
                    break;
                case 1:
                    this.mTracker.handleForegroundServicesChanged((java.lang.String) msg.obj, msg.arg1, msg.arg2, false);
                    break;
                case 2:
                    this.mTracker.handleForegroundServicesChanged((java.lang.String) msg.obj, msg.arg1, msg.arg2);
                    break;
                case 3:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    this.mTracker.handleForegroundServiceNotificationUpdated((java.lang.String) args.arg1, args.argi1, args.argi2, ((java.lang.Boolean) args.arg2).booleanValue());
                    args.recycle();
                    break;
                case 4:
                    this.mTracker.checkLongRunningFgs();
                    break;
                case 5:
                    this.mTracker.handleNotificationPosted((java.lang.String) msg.obj, msg.arg1, msg.arg2);
                    break;
                case 6:
                    this.mTracker.handleNotificationRemoved((java.lang.String) msg.obj, msg.arg1, msg.arg2);
                    break;
            }
        }
    }

    AppFGSTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller) {
        this(context, controller, null, null);
    }

    AppFGSTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<com.android.server.am.AppFGSTracker.AppFGSPolicy>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mFGSNotificationIDs = new com.android.server.am.UidProcessMap<>();
        this.mTmpPkgDurations = new android.util.ArrayMap<>();
        this.mNotificationListener = new com.android.server.am.AppFGSTracker.NotificationListener();
        this.mProcessObserver = new android.app.IProcessObserver.Stub() { // from class: com.android.server.am.AppFGSTracker.1
            public void onForegroundActivitiesChanged(int pid, int uid, boolean fg) {
            }

            public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) {
                java.lang.String packageName = com.android.server.am.AppFGSTracker.this.mAppRestrictionController.getPackageName(pid);
                if (packageName != null) {
                    com.android.server.am.AppFGSTracker.this.mHandler.obtainMessage(2, uid, serviceTypes, packageName).sendToTarget();
                }
            }

            public void onProcessStarted(int pid, int processUid, int packageUid, java.lang.String packageName, java.lang.String processName) {
            }

            public void onProcessDied(int pid, int uid) {
            }
        };
        this.mHandler = new com.android.server.am.AppFGSTracker.MyHandler(this);
        this.mInjector.setPolicy(new com.android.server.am.AppFGSTracker.AppFGSPolicy(this.mInjector, this));
    }

    @Override // com.android.server.am.BaseAppStateTracker
    int getType() {
        return 3;
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onSystemReady() {
        super.onSystemReady();
        this.mInjector.getActivityManagerInternal().addForegroundServiceStateListener(this);
        this.mInjector.getActivityManagerInternal().registerProcessObserver(this.mProcessObserver);
    }

    @Override // com.android.server.am.BaseAppStateDurationsTracker, com.android.server.am.BaseAppStateEventsTracker
    void reset() {
        this.mHandler.removeMessages(4);
        super.reset();
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.AppFGSTracker.PackageDurations createAppStateEvents(int uid, java.lang.String packageName) {
        return new com.android.server.am.AppFGSTracker.PackageDurations(uid, packageName, (com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy(), this);
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.AppFGSTracker.PackageDurations createAppStateEvents(com.android.server.am.AppFGSTracker.PackageDurations other) {
        return new com.android.server.am.AppFGSTracker.PackageDurations(other);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void handleForegroundServicesChanged(java.lang.String packageName, int pid, int uid, boolean started) {
        boolean longRunningFGSGone;
        if (!((com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy()).isEnabled()) {
            return;
        }
        long now = android.os.SystemClock.elapsedRealtime();
        int exemptReason = ((com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy()).shouldExemptUid(uid);
        synchronized (this.mLock) {
            com.android.server.am.AppFGSTracker.PackageDurations pkg = (com.android.server.am.AppFGSTracker.PackageDurations) this.mPkgEvents.get(uid, packageName);
            if (pkg == null) {
                pkg = createAppStateEvents(uid, packageName);
                this.mPkgEvents.put(uid, packageName, pkg);
            }
            boolean wasLongRunning = pkg.isLongRunning();
            pkg.addEvent(started, now);
            longRunningFGSGone = wasLongRunning && !pkg.hasForegroundServices();
            if (longRunningFGSGone) {
                pkg.setIsLongRunning(false);
            }
            pkg.mExemptReason = exemptReason;
            scheduleDurationCheckLocked(now);
        }
        if (longRunningFGSGone) {
            ((com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy()).onLongRunningFgsGone(packageName, uid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundServiceNotificationUpdated(java.lang.String packageName, int uid, int notificationId, boolean canceling) {
        int indexOfKey;
        synchronized (this.mLock) {
            android.util.SparseBooleanArray notificationIDs = this.mFGSNotificationIDs.get(uid, packageName);
            if (!canceling) {
                if (notificationIDs == null) {
                    notificationIDs = new android.util.SparseBooleanArray();
                    this.mFGSNotificationIDs.put(uid, packageName, notificationIDs);
                }
                notificationIDs.put(notificationId, false);
            } else if (notificationIDs != null && (indexOfKey = notificationIDs.indexOfKey(notificationId)) >= 0) {
                boolean wasVisible = notificationIDs.valueAt(indexOfKey);
                notificationIDs.removeAt(indexOfKey);
                if (notificationIDs.size() == 0) {
                    this.mFGSNotificationIDs.remove(uid, packageName);
                }
                for (int i = notificationIDs.size() - 1; i >= 0; i--) {
                    if (notificationIDs.valueAt(i)) {
                        return;
                    }
                }
                if (wasVisible) {
                    notifyListenersOnStateChange(uid, packageName, false, android.os.SystemClock.elapsedRealtime(), 8);
                }
            }
        }
    }

    private boolean hasForegroundServiceNotificationsLocked(java.lang.String packageName, int uid) {
        android.util.SparseBooleanArray notificationIDs = this.mFGSNotificationIDs.get(uid, packageName);
        if (notificationIDs == null || notificationIDs.size() == 0) {
            return false;
        }
        for (int i = notificationIDs.size() - 1; i >= 0; i--) {
            if (notificationIDs.valueAt(i)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotificationPosted(java.lang.String pkgName, int uid, int notificationId) throws java.lang.Throwable {
        int indexOfKey;
        boolean anyVisible;
        synchronized (this.mLock) {
            try {
                try {
                    try {
                        android.util.SparseBooleanArray notificationIDs = this.mFGSNotificationIDs.get(uid, pkgName);
                        if (notificationIDs == null || (indexOfKey = notificationIDs.indexOfKey(notificationId)) < 0) {
                            return;
                        }
                        if (notificationIDs.valueAt(indexOfKey)) {
                            return;
                        }
                        int i = notificationIDs.size() - 1;
                        while (true) {
                            if (i < 0) {
                                anyVisible = false;
                                break;
                            } else if (!notificationIDs.valueAt(i)) {
                                i--;
                            } else {
                                anyVisible = true;
                                break;
                            }
                        }
                        notificationIDs.setValueAt(indexOfKey, true);
                        if (!anyVisible) {
                            notifyListenersOnStateChange(uid, pkgName, true, android.os.SystemClock.elapsedRealtime(), 8);
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotificationRemoved(java.lang.String pkgName, int uid, int notificationId) {
        int indexOfKey;
        synchronized (this.mLock) {
            android.util.SparseBooleanArray notificationIDs = this.mFGSNotificationIDs.get(uid, pkgName);
            if (notificationIDs != null && (indexOfKey = notificationIDs.indexOfKey(notificationId)) >= 0) {
                if (notificationIDs.valueAt(indexOfKey)) {
                    notificationIDs.setValueAt(indexOfKey, false);
                    for (int i = notificationIDs.size() - 1; i >= 0; i--) {
                        if (notificationIDs.valueAt(i)) {
                            return;
                        }
                    }
                    notifyListenersOnStateChange(uid, pkgName, false, android.os.SystemClock.elapsedRealtime(), 8);
                }
            }
        }
    }

    private void scheduleDurationCheckLocked(long now) {
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.AppFGSTracker.PackageDurations>> map = this.mPkgEvents.getMap();
        long longest = -1;
        for (int i = map.size() - 1; i >= 0; i--) {
            android.util.ArrayMap<java.lang.String, com.android.server.am.AppFGSTracker.PackageDurations> val = map.valueAt(i);
            for (int j = val.size() - 1; j >= 0; j--) {
                com.android.server.am.AppFGSTracker.PackageDurations pkg = val.valueAt(j);
                if (pkg.hasForegroundServices() && !pkg.isLongRunning()) {
                    longest = java.lang.Math.max(getTotalDurations(pkg, now), longest);
                }
            }
        }
        this.mHandler.removeMessages(4);
        if (longest >= 0) {
            long future = this.mInjector.getServiceStartForegroundTimeout() + java.lang.Math.max(0L, ((com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy()).getFgsLongRunningThreshold() - longest);
            this.mHandler.sendEmptyMessageDelayed(4, future);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLongRunningFgs() throws java.lang.Throwable {
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.AppFGSTracker.PackageDurations>> map;
        long threshold;
        com.android.server.am.AppFGSTracker.AppFGSPolicy policy = (com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy();
        final android.util.ArrayMap<com.android.server.am.AppFGSTracker.PackageDurations, java.lang.Long> pkgWithLongFgs = this.mTmpPkgDurations;
        long now = android.os.SystemClock.elapsedRealtime();
        long threshold2 = policy.getFgsLongRunningThreshold();
        long windowSize = policy.getFgsLongRunningWindowSize();
        long trimTo = java.lang.Math.max(0L, now - windowSize);
        synchronized (this.mLock) {
            try {
                try {
                    android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.AppFGSTracker.PackageDurations>> map2 = this.mPkgEvents.getMap();
                    int i = map2.size() - 1;
                    while (i >= 0) {
                        android.util.ArrayMap<java.lang.String, com.android.server.am.AppFGSTracker.PackageDurations> val = map2.valueAt(i);
                        int j = val.size() - 1;
                        while (j >= 0) {
                            com.android.server.am.AppFGSTracker.PackageDurations pkg = val.valueAt(j);
                            if (!pkg.hasForegroundServices() || pkg.isLongRunning()) {
                                map = map2;
                                threshold = threshold2;
                            } else {
                                map = map2;
                                long totalDuration = getTotalDurations(pkg, now);
                                if (totalDuration < threshold2) {
                                    threshold = threshold2;
                                } else {
                                    threshold = threshold2;
                                    pkgWithLongFgs.put(pkg, java.lang.Long.valueOf(totalDuration));
                                    pkg.setIsLongRunning(true);
                                }
                            }
                            j--;
                            map2 = map;
                            threshold2 = threshold;
                        }
                        i--;
                        threshold2 = threshold2;
                    }
                    trim(trimTo);
                    int size = pkgWithLongFgs.size();
                    if (size > 0) {
                        java.lang.Integer[] indices = new java.lang.Integer[size];
                        for (int i2 = 0; i2 < size; i2++) {
                            indices[i2] = java.lang.Integer.valueOf(i2);
                        }
                        java.util.Arrays.sort(indices, new java.util.Comparator() { // from class: com.android.server.am.AppFGSTracker$$ExternalSyntheticLambda0
                            @Override // java.util.Comparator
                            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                                android.util.ArrayMap arrayMap = pkgWithLongFgs;
                                return java.lang.Long.compare(((java.lang.Long) arrayMap.valueAt(((java.lang.Integer) obj).intValue())).longValue(), ((java.lang.Long) arrayMap.valueAt(((java.lang.Integer) obj2).intValue())).longValue());
                            }
                        });
                        for (int i3 = size - 1; i3 >= 0; i3--) {
                            com.android.server.am.AppFGSTracker.PackageDurations pkg2 = pkgWithLongFgs.keyAt(indices[i3].intValue());
                            policy.onLongRunningFgs(pkg2.mPackageName, pkg2.mUid, pkg2.mExemptReason);
                        }
                        pkgWithLongFgs.clear();
                    }
                    synchronized (this.mLock) {
                        scheduleDurationCheckLocked(now);
                    }
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

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void handleForegroundServicesChanged(java.lang.String packageName, int uid, int serviceTypes) {
        if (!((com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy()).isEnabled()) {
            return;
        }
        int exemptReason = ((com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy()).shouldExemptUid(uid);
        long now = android.os.SystemClock.elapsedRealtime();
        synchronized (this.mLock) {
            com.android.server.am.AppFGSTracker.PackageDurations pkg = (com.android.server.am.AppFGSTracker.PackageDurations) this.mPkgEvents.get(uid, packageName);
            if (pkg == null) {
                pkg = new com.android.server.am.AppFGSTracker.PackageDurations(uid, packageName, (com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy(), this);
                this.mPkgEvents.put(uid, packageName, pkg);
            }
            pkg.setForegroundServiceType(serviceTypes, now);
            pkg.mExemptReason = exemptReason;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBgFgsMonitorEnabled(boolean enabled) {
        if (enabled) {
            synchronized (this.mLock) {
                scheduleDurationCheckLocked(android.os.SystemClock.elapsedRealtime());
            }
            try {
                this.mNotificationListener.registerAsSystemService(this.mContext, new android.content.ComponentName(this.mContext, (java.lang.Class<?>) com.android.server.am.AppFGSTracker.NotificationListener.class), -1);
                return;
            } catch (android.os.RemoteException e) {
                return;
            }
        }
        try {
            this.mNotificationListener.unregisterAsSystemService();
        } catch (android.os.RemoteException e2) {
        }
        this.mHandler.removeMessages(4);
        synchronized (this.mLock) {
            this.mPkgEvents.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBgFgsLongRunningThresholdChanged() {
        synchronized (this.mLock) {
            if (((com.android.server.am.AppFGSTracker.AppFGSPolicy) this.mInjector.getPolicy()).isEnabled()) {
                scheduleDurationCheckLocked(android.os.SystemClock.elapsedRealtime());
            }
        }
    }

    static int foregroundServiceTypeToIndex(int serviceType) {
        if (serviceType == 0) {
            return 0;
        }
        return java.lang.Integer.numberOfTrailingZeros(serviceType) + 1;
    }

    static int indexToForegroundServiceType(int index) {
        if (index == com.android.server.am.AppFGSTracker.PackageDurations.DEFAULT_INDEX) {
            return 0;
        }
        return 1 << (index - 1);
    }

    long getTotalDurations(com.android.server.am.AppFGSTracker.PackageDurations pkg, long now) {
        return getTotalDurations(pkg.mPackageName, pkg.mUid, now, foregroundServiceTypeToIndex(0));
    }

    @Override // com.android.server.am.BaseAppStateDurationsTracker
    long getTotalDurations(int uid, long now) {
        return getTotalDurations(uid, now, foregroundServiceTypeToIndex(0));
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    boolean hasForegroundServices(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.am.AppFGSTracker.PackageDurations pkg = (com.android.server.am.AppFGSTracker.PackageDurations) this.mPkgEvents.get(uid, packageName);
            z = pkg != null && pkg.hasForegroundServices();
        }
        return z;
    }

    boolean hasForegroundServices(int uid) {
        synchronized (this.mLock) {
            android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.AppFGSTracker.PackageDurations>> map = this.mPkgEvents.getMap();
            android.util.ArrayMap<java.lang.String, com.android.server.am.AppFGSTracker.PackageDurations> pkgs = map.get(uid);
            if (pkgs != null) {
                for (int i = pkgs.size() - 1; i >= 0; i--) {
                    com.android.server.am.AppFGSTracker.PackageDurations pkg = pkgs.valueAt(i);
                    if (pkg.hasForegroundServices()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    boolean hasForegroundServiceNotifications(java.lang.String packageName, int uid) {
        boolean zHasForegroundServiceNotificationsLocked;
        synchronized (this.mLock) {
            zHasForegroundServiceNotificationsLocked = hasForegroundServiceNotificationsLocked(packageName, uid);
        }
        return zHasForegroundServiceNotificationsLocked;
    }

    boolean hasForegroundServiceNotifications(int uid) {
        synchronized (this.mLock) {
            android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseBooleanArray>> map = this.mFGSNotificationIDs.getMap();
            android.util.ArrayMap<java.lang.String, android.util.SparseBooleanArray> pkgs = map.get(uid);
            if (pkgs != null) {
                for (int i = pkgs.size() - 1; i >= 0; i--) {
                    if (hasForegroundServiceNotificationsLocked(pkgs.keyAt(i), uid)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    byte[] getTrackerInfoForStatsd(int uid) {
        long fgsDurations = getTotalDurations(uid, android.os.SystemClock.elapsedRealtime());
        if (fgsDurations == 0) {
            return null;
        }
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
        proto.write(1133871366145L, hasForegroundServiceNotifications(uid));
        proto.write(1112396529666L, fgsDurations);
        proto.flush();
        return proto.getBytes();
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println("APP FOREGROUND SERVICE TRACKER:");
        super.dump(pw, "  " + prefix);
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker
    void dumpOthers(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println("APPS WITH ACTIVE FOREGROUND SERVICES:");
        java.lang.String prefix2 = "  " + prefix;
        synchronized (this.mLock) {
            android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseBooleanArray>> map = this.mFGSNotificationIDs.getMap();
            if (map.size() == 0) {
                pw.print(prefix2);
                pw.println("(none)");
            }
            int size = map.size();
            for (int i = 0; i < size; i++) {
                int uid = map.keyAt(i);
                java.lang.String uidString = android.os.UserHandle.formatUid(uid);
                android.util.ArrayMap<java.lang.String, android.util.SparseBooleanArray> pkgs = map.valueAt(i);
                int numOfPkgs = pkgs.size();
                for (int j = 0; j < numOfPkgs; j++) {
                    java.lang.String pkgName = pkgs.keyAt(j);
                    pw.print(prefix2);
                    pw.print(pkgName);
                    pw.print('/');
                    pw.print(uidString);
                    pw.print(" notification=");
                    pw.println(hasForegroundServiceNotificationsLocked(pkgName, uid));
                }
            }
        }
    }

    static class PackageDurations extends com.android.server.am.BaseAppStateDurations<com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent> {
        static final int DEFAULT_INDEX = com.android.server.am.AppFGSTracker.foregroundServiceTypeToIndex(0);
        private int mForegroundServiceTypes;
        private boolean mIsLongRunning;
        private final com.android.server.am.AppFGSTracker mTracker;

        /* JADX WARN: Multi-variable type inference failed */
        PackageDurations(int uid, java.lang.String packageName, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig, com.android.server.am.AppFGSTracker tracker) {
            super(uid, packageName, 31, "ActivityManager", maxTrackingDurationConfig);
            this.mEvents[DEFAULT_INDEX] = new java.util.LinkedList();
            this.mTracker = tracker;
        }

        PackageDurations(com.android.server.am.AppFGSTracker.PackageDurations other) {
            super(other);
            this.mIsLongRunning = other.mIsLongRunning;
            this.mForegroundServiceTypes = other.mForegroundServiceTypes;
            this.mTracker = other.mTracker;
        }

        void addEvent(boolean startFgs, long now) {
            addEvent(startFgs, new com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent(now), DEFAULT_INDEX);
            if (!startFgs && !hasForegroundServices()) {
                this.mIsLongRunning = false;
            }
            if (!startFgs && this.mForegroundServiceTypes != 0) {
                for (int i = 1; i < this.mEvents.length; i++) {
                    if (this.mEvents[i] != null && isActive(i)) {
                        this.mEvents[i].add(new com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent(now));
                        notifyListenersOnStateChangeIfNecessary(false, now, com.android.server.am.AppFGSTracker.indexToForegroundServiceType(i));
                    }
                }
                this.mForegroundServiceTypes = 0;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        void setForegroundServiceType(int serviceTypes, long now) {
            if (serviceTypes == this.mForegroundServiceTypes || !hasForegroundServices()) {
                return;
            }
            int changes = this.mForegroundServiceTypes ^ serviceTypes;
            int serviceType = java.lang.Integer.highestOneBit(changes);
            while (serviceType != 0) {
                int i = com.android.server.am.AppFGSTracker.foregroundServiceTypeToIndex(serviceType);
                if (i < this.mEvents.length) {
                    if ((serviceTypes & serviceType) != 0) {
                        if (this.mEvents[i] == null) {
                            this.mEvents[i] = new java.util.LinkedList();
                        }
                        if (!isActive(i)) {
                            this.mEvents[i].add(new com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent(now));
                            notifyListenersOnStateChangeIfNecessary(true, now, serviceType);
                        }
                    } else if (this.mEvents[i] != null && isActive(i)) {
                        this.mEvents[i].add(new com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent(now));
                        notifyListenersOnStateChangeIfNecessary(false, now, serviceType);
                    }
                }
                changes &= ~serviceType;
                serviceType = java.lang.Integer.highestOneBit(changes);
            }
            this.mForegroundServiceTypes = serviceTypes;
        }

        private void notifyListenersOnStateChangeIfNecessary(boolean start, long now, int serviceType) {
            int stateType;
            switch (serviceType) {
                case 2:
                    stateType = 2;
                    break;
                case 8:
                    stateType = 4;
                    break;
                default:
                    return;
            }
            this.mTracker.notifyListenersOnStateChange(this.mUid, this.mPackageName, start, now, stateType);
        }

        void setIsLongRunning(boolean isLongRunning) {
            this.mIsLongRunning = isLongRunning;
        }

        boolean isLongRunning() {
            return this.mIsLongRunning;
        }

        boolean hasForegroundServices() {
            return isActive(DEFAULT_INDEX);
        }

        @Override // com.android.server.am.BaseAppStateEvents
        java.lang.String formatEventTypeLabel(int index) {
            if (index == DEFAULT_INDEX) {
                return "Overall foreground services: ";
            }
            return android.content.pm.ServiceInfo.foregroundServiceTypeToLabel(com.android.server.am.AppFGSTracker.indexToForegroundServiceType(index)) + ": ";
        }
    }

    class NotificationListener extends android.service.notification.NotificationListenerService {
        NotificationListener() {
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationPosted(android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap map) {
            com.android.server.am.AppFGSTracker.this.mHandler.obtainMessage(5, sbn.getUid(), sbn.getId(), sbn.getPackageName()).sendToTarget();
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationRemoved(android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationListenerService.RankingMap rankingMap, int reason) {
            com.android.server.am.AppFGSTracker.this.mHandler.obtainMessage(6, sbn.getUid(), sbn.getId(), sbn.getPackageName()).sendToTarget();
        }
    }

    static final class AppFGSPolicy extends com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy<com.android.server.am.AppFGSTracker> {
        static final long DEFAULT_BG_FGS_LOCATION_THRESHOLD = 14400000;
        static final long DEFAULT_BG_FGS_LONG_RUNNING_THRESHOLD = 72000000;
        static final long DEFAULT_BG_FGS_LONG_RUNNING_WINDOW = 86400000;
        static final long DEFAULT_BG_FGS_MEDIA_PLAYBACK_THRESHOLD = 14400000;
        static final boolean DEFAULT_BG_FGS_MONITOR_ENABLED = true;
        static final java.lang.String KEY_BG_FGS_LOCATION_THRESHOLD = "bg_fgs_location_threshold";
        static final java.lang.String KEY_BG_FGS_LONG_RUNNING_THRESHOLD = "bg_fgs_long_running_threshold";
        static final java.lang.String KEY_BG_FGS_LONG_RUNNING_WINDOW = "bg_fgs_long_running_window";
        static final java.lang.String KEY_BG_FGS_MEDIA_PLAYBACK_THRESHOLD = "bg_fgs_media_playback_threshold";
        static final java.lang.String KEY_BG_FGS_MONITOR_ENABLED = "bg_fgs_monitor_enabled";
        private volatile long mBgFgsLocationThresholdMs;
        private volatile long mBgFgsLongRunningThresholdMs;
        private volatile long mBgFgsMediaPlaybackThresholdMs;

        AppFGSPolicy(com.android.server.am.BaseAppStateTracker.Injector injector, com.android.server.am.AppFGSTracker tracker) {
            super(injector, tracker, KEY_BG_FGS_MONITOR_ENABLED, true, KEY_BG_FGS_LONG_RUNNING_WINDOW, 86400000L);
            this.mBgFgsLongRunningThresholdMs = DEFAULT_BG_FGS_LONG_RUNNING_THRESHOLD;
            this.mBgFgsMediaPlaybackThresholdMs = 14400000L;
            this.mBgFgsLocationThresholdMs = 14400000L;
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        public void onSystemReady() {
            super.onSystemReady();
            updateBgFgsLongRunningThreshold();
            updateBgFgsMediaPlaybackThreshold();
            updateBgFgsLocationThreshold();
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onPropertiesChanged(java.lang.String r2) {
            /*
                r1 = this;
                int r0 = r2.hashCode()
                switch(r0) {
                    case -2001687768: goto L1c;
                    case 351955503: goto L12;
                    case 803245321: goto L8;
                    default: goto L7;
                }
            L7:
                goto L26
            L8:
                java.lang.String r0 = "bg_fgs_media_playback_threshold"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L7
                r0 = 1
                goto L27
            L12:
                java.lang.String r0 = "bg_fgs_long_running_threshold"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L7
                r0 = 0
                goto L27
            L1c:
                java.lang.String r0 = "bg_fgs_location_threshold"
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L7
                r0 = 2
                goto L27
            L26:
                r0 = -1
            L27:
                switch(r0) {
                    case 0: goto L36;
                    case 1: goto L32;
                    case 2: goto L2e;
                    default: goto L2a;
                }
            L2a:
                super.onPropertiesChanged(r2)
                goto L3a
            L2e:
                r1.updateBgFgsLocationThreshold()
                goto L3a
            L32:
                r1.updateBgFgsMediaPlaybackThreshold()
                goto L3a
            L36:
                r1.updateBgFgsLongRunningThreshold()
            L3a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppFGSTracker.AppFGSPolicy.onPropertiesChanged(java.lang.String):void");
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean enabled) {
            ((com.android.server.am.AppFGSTracker) this.mTracker).onBgFgsMonitorEnabled(enabled);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        public void onMaxTrackingDurationChanged(long maxDuration) {
            ((com.android.server.am.AppFGSTracker) this.mTracker).onBgFgsLongRunningThresholdChanged();
        }

        private void updateBgFgsLongRunningThreshold() {
            long threshold = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_FGS_LONG_RUNNING_THRESHOLD, DEFAULT_BG_FGS_LONG_RUNNING_THRESHOLD);
            if (threshold != this.mBgFgsLongRunningThresholdMs) {
                this.mBgFgsLongRunningThresholdMs = threshold;
                ((com.android.server.am.AppFGSTracker) this.mTracker).onBgFgsLongRunningThresholdChanged();
            }
        }

        private void updateBgFgsMediaPlaybackThreshold() {
            this.mBgFgsMediaPlaybackThresholdMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_FGS_MEDIA_PLAYBACK_THRESHOLD, 14400000L);
        }

        private void updateBgFgsLocationThreshold() {
            this.mBgFgsLocationThresholdMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_FGS_LOCATION_THRESHOLD, 14400000L);
        }

        long getFgsLongRunningThreshold() {
            return this.mBgFgsLongRunningThresholdMs;
        }

        long getFgsLongRunningWindowSize() {
            return getMaxTrackingDuration();
        }

        long getFGSMediaPlaybackThreshold() {
            return this.mBgFgsMediaPlaybackThresholdMs;
        }

        long getLocationFGSThreshold() {
            return this.mBgFgsLocationThresholdMs;
        }

        void onLongRunningFgs(java.lang.String packageName, int uid, int exemptReason) {
            if (exemptReason != -1) {
                return;
            }
            long now = android.os.SystemClock.elapsedRealtime();
            long window = getFgsLongRunningWindowSize();
            long since = java.lang.Math.max(0L, now - window);
            if (shouldExemptMediaPlaybackFGS(packageName, uid, now, window) || shouldExemptLocationFGS(packageName, uid, now, since)) {
                return;
            }
            ((com.android.server.am.AppFGSTracker) this.mTracker).mAppRestrictionController.postLongRunningFgsIfNecessary(packageName, uid);
        }

        boolean shouldExemptMediaPlaybackFGS(java.lang.String packageName, int uid, long now, long window) {
            long mediaPlaybackMs = ((com.android.server.am.AppFGSTracker) this.mTracker).mAppRestrictionController.getCompositeMediaPlaybackDurations(packageName, uid, now, window);
            if (mediaPlaybackMs > 0 && mediaPlaybackMs >= getFGSMediaPlaybackThreshold()) {
                return true;
            }
            return false;
        }

        boolean shouldExemptLocationFGS(java.lang.String packageName, int uid, long now, long since) {
            long locationMs = ((com.android.server.am.AppFGSTracker) this.mTracker).mAppRestrictionController.getForegroundServiceTotalDurationsSince(packageName, uid, since, now, 8);
            if (locationMs > 0 && locationMs >= getLocationFGSThreshold()) {
                return true;
            }
            return false;
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        java.lang.String getExemptionReasonString(java.lang.String packageName, int uid, int reason) {
            if (reason != -1) {
                return super.getExemptionReasonString(packageName, uid, reason);
            }
            long now = android.os.SystemClock.elapsedRealtime();
            long window = getFgsLongRunningWindowSize();
            long since = java.lang.Math.max(0L, now - getFgsLongRunningWindowSize());
            return "{mediaPlayback=" + shouldExemptMediaPlaybackFGS(packageName, uid, now, window) + ", location=" + shouldExemptLocationFGS(packageName, uid, now, since) + "}";
        }

        void onLongRunningFgsGone(java.lang.String packageName, int uid) {
            ((com.android.server.am.AppFGSTracker) this.mTracker).mAppRestrictionController.cancelLongRunningFGSNotificationIfNecessary(packageName, uid);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.println("APP FOREGROUND SERVICE TRACKER POLICY SETTINGS:");
            java.lang.String prefix2 = "  " + prefix;
            super.dump(pw, prefix2);
            if (isEnabled()) {
                pw.print(prefix2);
                pw.print(KEY_BG_FGS_LONG_RUNNING_THRESHOLD);
                pw.print('=');
                pw.println(this.mBgFgsLongRunningThresholdMs);
                pw.print(prefix2);
                pw.print(KEY_BG_FGS_MEDIA_PLAYBACK_THRESHOLD);
                pw.print('=');
                pw.println(this.mBgFgsMediaPlaybackThresholdMs);
                pw.print(prefix2);
                pw.print(KEY_BG_FGS_LOCATION_THRESHOLD);
                pw.print('=');
                pw.println(this.mBgFgsLocationThresholdMs);
            }
        }
    }
}
