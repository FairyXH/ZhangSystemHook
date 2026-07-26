package com.android.server.appbinding;

/* JADX INFO: loaded from: classes.dex */
public class AppBindingService extends android.os.Binder {
    public static final boolean DEBUG = false;
    public static final java.lang.String TAG = "AppBindingService";
    private final java.util.ArrayList<com.android.server.appbinding.finders.AppServiceFinder> mApps;
    private final java.util.ArrayList<com.android.server.appbinding.AppBindingService.AppServiceConnection> mConnections;
    private com.android.server.appbinding.AppBindingConstants mConstants;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.content.pm.IPackageManager mIPackageManager;
    private final com.android.server.appbinding.AppBindingService.Injector mInjector;
    private final java.lang.Object mLock;
    final android.content.BroadcastReceiver mPackageUserMonitor;
    private final android.util.SparseBooleanArray mRunningUsers;
    private final android.database.ContentObserver mSettingsObserver;

    static class Injector {
        Injector() {
        }

        public android.content.pm.IPackageManager getIPackageManager() {
            return android.app.AppGlobals.getPackageManager();
        }

        public java.lang.String getGlobalSettingString(android.content.ContentResolver resolver, java.lang.String key) {
            return android.provider.Settings.Global.getString(resolver, key);
        }
    }

    public static class Lifecycle extends com.android.server.SystemService {
        final com.android.server.appbinding.AppBindingService mService;

        public Lifecycle(android.content.Context context) {
            this(context, new com.android.server.appbinding.AppBindingService.Injector());
        }

        Lifecycle(android.content.Context context, com.android.server.appbinding.AppBindingService.Injector injector) {
            super(context);
            this.mService = new com.android.server.appbinding.AppBindingService(injector, context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishBinderService("app_binding", this.mService);
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
    }

    private AppBindingService(com.android.server.appbinding.AppBindingService.Injector injector, android.content.Context context) {
        this.mLock = new java.lang.Object();
        this.mRunningUsers = new android.util.SparseBooleanArray(2);
        this.mApps = new java.util.ArrayList<>();
        this.mConnections = new java.util.ArrayList<>();
        this.mSettingsObserver = new android.database.ContentObserver(null) { // from class: com.android.server.appbinding.AppBindingService.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.appbinding.AppBindingService.this.refreshConstants();
            }
        };
        this.mPackageUserMonitor = new android.content.BroadcastReceiver() { // from class: com.android.server.appbinding.AppBindingService.2
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:27:0x007d  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r8, android.content.Intent r9) {
                /*
                    r7 = this;
                    java.lang.String r0 = "android.intent.extra.user_handle"
                    r1 = -10000(0xffffffffffffd8f0, float:NaN)
                    int r0 = r9.getIntExtra(r0, r1)
                    java.lang.String r2 = "AppBindingService"
                    if (r0 != r1) goto L23
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r3 = "Intent broadcast does not contain user handle: "
                    java.lang.StringBuilder r1 = r1.append(r3)
                    java.lang.StringBuilder r1 = r1.append(r9)
                    java.lang.String r1 = r1.toString()
                    android.util.Slog.w(r2, r1)
                    return
                L23:
                    java.lang.String r1 = r9.getAction()
                    java.lang.String r3 = "android.intent.action.USER_REMOVED"
                    boolean r3 = r3.equals(r1)
                    if (r3 == 0) goto L35
                    com.android.server.appbinding.AppBindingService r2 = com.android.server.appbinding.AppBindingService.this
                    com.android.server.appbinding.AppBindingService.m1632$$Nest$monUserRemoved(r2, r0)
                    return
                L35:
                    android.net.Uri r3 = r9.getData()
                    if (r3 == 0) goto L40
                    java.lang.String r4 = r3.getSchemeSpecificPart()
                    goto L41
                L40:
                    r4 = 0
                L41:
                    if (r4 != 0) goto L5b
                    java.lang.StringBuilder r5 = new java.lang.StringBuilder
                    r5.<init>()
                    java.lang.String r6 = "Intent broadcast does not contain package name: "
                    java.lang.StringBuilder r5 = r5.append(r6)
                    java.lang.StringBuilder r5 = r5.append(r9)
                    java.lang.String r5 = r5.toString()
                    android.util.Slog.w(r2, r5)
                    return
                L5b:
                    java.lang.String r2 = "android.intent.extra.REPLACING"
                    r5 = 0
                    boolean r2 = r9.getBooleanExtra(r2, r5)
                    int r6 = r1.hashCode()
                    switch(r6) {
                        case 172491798: goto L73;
                        case 1544582882: goto L6a;
                        default: goto L69;
                    }
                L69:
                    goto L7d
                L6a:
                    java.lang.String r6 = "android.intent.action.PACKAGE_ADDED"
                    boolean r6 = r1.equals(r6)
                    if (r6 == 0) goto L69
                    goto L7e
                L73:
                    java.lang.String r5 = "android.intent.action.PACKAGE_CHANGED"
                    boolean r5 = r1.equals(r5)
                    if (r5 == 0) goto L69
                    r5 = 1
                    goto L7e
                L7d:
                    r5 = -1
                L7e:
                    switch(r5) {
                        case 0: goto L88;
                        case 1: goto L82;
                        default: goto L81;
                    }
                L81:
                    goto L8f
                L82:
                    com.android.server.appbinding.AppBindingService r5 = com.android.server.appbinding.AppBindingService.this
                    com.android.server.appbinding.AppBindingService.m1627$$Nest$mhandlePackageAddedReplacing(r5, r4, r0)
                    goto L8f
                L88:
                    if (r2 == 0) goto L8f
                    com.android.server.appbinding.AppBindingService r5 = com.android.server.appbinding.AppBindingService.this
                    com.android.server.appbinding.AppBindingService.m1627$$Nest$mhandlePackageAddedReplacing(r5, r4, r0)
                L8f:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.appbinding.AppBindingService.AnonymousClass2.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mInjector = injector;
        this.mContext = context;
        this.mIPackageManager = injector.getIPackageManager();
        this.mHandler = com.android.internal.os.BackgroundThread.getHandler();
        this.mApps.add(new com.android.server.appbinding.finders.CarrierMessagingClientServiceFinder(context, new java.util.function.BiConsumer() { // from class: com.android.server.appbinding.AppBindingService$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.onAppChanged((com.android.server.appbinding.finders.AppServiceFinder) obj, ((java.lang.Integer) obj2).intValue());
            }
        }, this.mHandler));
        this.mConstants = com.android.server.appbinding.AppBindingConstants.initializeFromString("");
    }

    private void forAllAppsLocked(java.util.function.Consumer<com.android.server.appbinding.finders.AppServiceFinder> consumer) {
        for (int i = 0; i < this.mApps.size(); i++) {
            consumer.accept(this.mApps.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBootPhase(int phase) {
        switch (phase) {
            case 550:
                onPhaseActivityManagerReady();
                break;
            case 600:
                onPhaseThirdPartyAppsCanStart();
                break;
        }
    }

    private void onPhaseActivityManagerReady() {
        android.content.IntentFilter packageFilter = new android.content.IntentFilter();
        packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        packageFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        packageFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mPackageUserMonitor, android.os.UserHandle.ALL, packageFilter, null, this.mHandler);
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        userFilter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiverAsUser(this.mPackageUserMonitor, android.os.UserHandle.ALL, userFilter, null, this.mHandler);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("app_binding_constants"), false, this.mSettingsObserver);
        refreshConstants();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshConstants() {
        java.lang.String newSetting = this.mInjector.getGlobalSettingString(this.mContext.getContentResolver(), "app_binding_constants");
        synchronized (this.mLock) {
            if (android.text.TextUtils.equals(this.mConstants.sourceSettings, newSetting)) {
                return;
            }
            android.util.Slog.i(TAG, "Updating constants with: " + newSetting);
            this.mConstants = com.android.server.appbinding.AppBindingConstants.initializeFromString(newSetting);
            rebindAllLocked("settings update");
        }
    }

    private void onPhaseThirdPartyAppsCanStart() {
        synchronized (this.mLock) {
            forAllAppsLocked(new java.util.function.Consumer() { // from class: com.android.server.appbinding.AppBindingService$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appbinding.finders.AppServiceFinder) obj).startMonitoring();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStartUser(int userId) {
        synchronized (this.mLock) {
            this.mRunningUsers.append(userId, true);
            bindServicesLocked(userId, null, "user start");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnlockUser(int userId) {
        synchronized (this.mLock) {
            bindServicesLocked(userId, null, "user unlock");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStopUser(int userId) {
        synchronized (this.mLock) {
            unbindServicesLocked(userId, null, "user stop");
            this.mRunningUsers.delete(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserRemoved(final int userId) {
        synchronized (this.mLock) {
            forAllAppsLocked(new java.util.function.Consumer() { // from class: com.android.server.appbinding.AppBindingService$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appbinding.finders.AppServiceFinder) obj).onUserRemoved(userId);
                }
            });
            this.mRunningUsers.delete(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppChanged(com.android.server.appbinding.finders.AppServiceFinder finder, int userId) {
        synchronized (this.mLock) {
            java.lang.String reason = finder.getAppDescription() + " changed";
            unbindServicesLocked(userId, finder, reason);
            bindServicesLocked(userId, finder, reason);
        }
    }

    private com.android.server.appbinding.finders.AppServiceFinder findFinderLocked(int userId, java.lang.String packageName) {
        for (int i = 0; i < this.mApps.size(); i++) {
            com.android.server.appbinding.finders.AppServiceFinder app = this.mApps.get(i);
            if (packageName.equals(app.getTargetPackage(userId))) {
                return app;
            }
        }
        return null;
    }

    private com.android.server.appbinding.AppBindingService.AppServiceConnection findConnectionLock(int userId, com.android.server.appbinding.finders.AppServiceFinder target) {
        for (int i = 0; i < this.mConnections.size(); i++) {
            com.android.server.appbinding.AppBindingService.AppServiceConnection conn = this.mConnections.get(i);
            if (conn.getUserId() == userId && conn.getFinder() == target) {
                return conn;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageAddedReplacing(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            com.android.server.appbinding.finders.AppServiceFinder finder = findFinderLocked(userId, packageName);
            if (finder != null) {
                unbindServicesLocked(userId, finder, "package update");
                bindServicesLocked(userId, finder, "package update");
            }
        }
    }

    private void rebindAllLocked(java.lang.String reason) {
        for (int i = 0; i < this.mRunningUsers.size(); i++) {
            if (this.mRunningUsers.valueAt(i)) {
                int userId = this.mRunningUsers.keyAt(i);
                unbindServicesLocked(userId, null, reason);
                bindServicesLocked(userId, null, reason);
            }
        }
    }

    private void bindServicesLocked(int userId, com.android.server.appbinding.finders.AppServiceFinder target, java.lang.String reasonForLog) {
        for (int i = 0; i < this.mApps.size(); i++) {
            com.android.server.appbinding.finders.AppServiceFinder app = this.mApps.get(i);
            if (target == null || target == app) {
                com.android.server.appbinding.AppBindingService.AppServiceConnection existingConn = findConnectionLock(userId, app);
                if (existingConn != null) {
                    unbindServicesLocked(userId, target, reasonForLog);
                }
                android.content.pm.ServiceInfo service = app.findService(userId, this.mIPackageManager, this.mConstants);
                if (service != null) {
                    com.android.server.appbinding.AppBindingService.AppServiceConnection conn = new com.android.server.appbinding.AppBindingService.AppServiceConnection(this.mContext, userId, this.mConstants, this.mHandler, app, service.getComponentName());
                    this.mConnections.add(conn);
                    conn.bind();
                }
            }
        }
    }

    private void unbindServicesLocked(int userId, com.android.server.appbinding.finders.AppServiceFinder target, java.lang.String reasonForLog) {
        for (int i = this.mConnections.size() - 1; i >= 0; i--) {
            com.android.server.appbinding.AppBindingService.AppServiceConnection conn = this.mConnections.get(i);
            if (conn.getUserId() == userId && (target == null || conn.getFinder() == target)) {
                this.mConnections.remove(i);
                conn.unbind();
            }
        }
    }

    private static class AppServiceConnection extends com.android.server.am.PersistentConnection<android.os.IInterface> {
        private final com.android.server.appbinding.AppBindingConstants mConstants;
        private final com.android.server.appbinding.finders.AppServiceFinder mFinder;

        AppServiceConnection(android.content.Context context, int userId, com.android.server.appbinding.AppBindingConstants constants, android.os.Handler handler, com.android.server.appbinding.finders.AppServiceFinder finder, android.content.ComponentName componentName) {
            super(com.android.server.appbinding.AppBindingService.TAG, context, handler, userId, componentName, constants.SERVICE_RECONNECT_BACKOFF_SEC, constants.SERVICE_RECONNECT_BACKOFF_INCREASE, constants.SERVICE_RECONNECT_MAX_BACKOFF_SEC, constants.SERVICE_STABLE_CONNECTION_THRESHOLD_SEC);
            this.mFinder = finder;
            this.mConstants = constants;
        }

        @Override // com.android.server.am.PersistentConnection
        protected int getBindFlags() {
            return this.mFinder.getBindFlags(this.mConstants);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.am.PersistentConnection
        public android.os.IInterface asInterface(android.os.IBinder obj) {
            return this.mFinder.asInterface(obj);
        }

        public com.android.server.appbinding.finders.AppServiceFinder getFinder() {
            return this.mFinder;
        }
    }

    @Override // android.os.Binder
    public void dump(java.io.FileDescriptor fd, final java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            if (args.length > 0 && "-s".equals(args[0])) {
                dumpSimple(pw);
                return;
            }
            synchronized (this.mLock) {
                this.mConstants.dump("  ", pw);
                pw.println();
                pw.print("  Running users:");
                for (int i = 0; i < this.mRunningUsers.size(); i++) {
                    if (this.mRunningUsers.valueAt(i)) {
                        pw.print(" ");
                        pw.print(this.mRunningUsers.keyAt(i));
                    }
                }
                pw.println();
                pw.println("  Connections:");
                for (int i2 = 0; i2 < this.mConnections.size(); i2++) {
                    com.android.server.appbinding.AppBindingService.AppServiceConnection conn = this.mConnections.get(i2);
                    pw.print("    App type: ");
                    pw.print(conn.getFinder().getAppDescription());
                    pw.println();
                    conn.dump("      ", pw);
                }
                if (this.mConnections.size() == 0) {
                    pw.println("    None:");
                }
                pw.println();
                pw.println("  Finders:");
                forAllAppsLocked(new java.util.function.Consumer() { // from class: com.android.server.appbinding.AppBindingService$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.appbinding.finders.AppServiceFinder) obj).dump("    ", pw);
                    }
                });
            }
        }
    }

    private void dumpSimple(final java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mConnections.size(); i++) {
                com.android.server.appbinding.AppBindingService.AppServiceConnection conn = this.mConnections.get(i);
                pw.print("conn,");
                pw.print(conn.getFinder().getAppDescription());
                pw.print(",");
                pw.print(conn.getUserId());
                pw.print(",");
                pw.print(conn.getComponentName().getPackageName());
                pw.print(",");
                pw.print(conn.getComponentName().getClassName());
                pw.print(",");
                pw.print(conn.isBound() ? "bound" : "not-bound");
                pw.print(",");
                pw.print(conn.isConnected() ? "connected" : "not-connected");
                pw.print(",#con=");
                pw.print(conn.getNumConnected());
                pw.print(",#dis=");
                pw.print(conn.getNumDisconnected());
                pw.print(",#died=");
                pw.print(conn.getNumBindingDied());
                pw.print(",backoff=");
                pw.print(conn.getNextBackoffMs());
                pw.println();
            }
            forAllAppsLocked(new java.util.function.Consumer() { // from class: com.android.server.appbinding.AppBindingService$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.appbinding.finders.AppServiceFinder) obj).dumpSimple(pw);
                }
            });
        }
    }

    com.android.server.appbinding.AppBindingConstants getConstantsForTest() {
        return this.mConstants;
    }
}
