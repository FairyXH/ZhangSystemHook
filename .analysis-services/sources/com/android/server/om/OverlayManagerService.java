package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
public final class OverlayManagerService extends com.android.server.SystemService {
    static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String DEFAULT_OVERLAYS_PROP = "ro.boot.vendor.overlay.theme";
    static final java.lang.String TAG = "OverlayManager";
    private final com.android.server.om.OverlayActorEnforcer mActorEnforcer;
    private final com.android.server.om.OverlayManagerServiceImpl mImpl;
    private final java.lang.Object mLock;
    private com.android.server.om.IOverlayManagerServiceExt mOmServiceExt;
    private final com.android.server.om.OverlayManagerService.PackageManagerHelperImpl mPackageManager;
    private final com.android.internal.content.PackageMonitor mPackageMonitor;
    private int mPrevStartedUserId;
    private final android.os.IBinder mService;
    private final com.android.server.om.OverlayManagerSettings mSettings;
    private final android.util.AtomicFile mSettingsFile;
    private final com.android.server.pm.UserManagerService mUserManager;

    /* JADX WARN: Multi-variable type inference failed */
    public OverlayManagerService(android.content.Context context) {
        super(context);
        this.mOmServiceExt = (com.android.server.om.IOverlayManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.om.IOverlayManagerServiceExt.class).base(this).create();
        this.mLock = new java.lang.Object();
        java.lang.Object[] objArr = 0;
        java.lang.Object[] objArr2 = 0;
        this.mPackageMonitor = new com.android.server.om.OverlayManagerService.OverlayManagerPackageMonitor();
        this.mPrevStartedUserId = -1;
        this.mService = new com.android.server.om.OverlayManagerService.AnonymousClass1();
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#OverlayManagerService");
            this.mSettingsFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), "overlays.xml"), "overlays");
            this.mPackageManager = new com.android.server.om.OverlayManagerService.PackageManagerHelperImpl(context);
            this.mUserManager = com.android.server.pm.UserManagerService.getInstance();
            com.android.server.om.IdmapManager idmapManager = new com.android.server.om.IdmapManager(com.android.server.om.IdmapDaemon.getInstance(), this.mPackageManager);
            this.mSettings = new com.android.server.om.OverlayManagerSettings();
            this.mImpl = new com.android.server.om.OverlayManagerServiceImpl(this.mPackageManager, idmapManager, this.mSettings, com.android.internal.content.om.OverlayConfig.getSystemInstance(), getDefaultOverlayPackages());
            this.mActorEnforcer = new com.android.server.om.OverlayActorEnforcer(this.mPackageManager);
            android.os.HandlerThread handlerThread = new android.os.HandlerThread(TAG);
            handlerThread.start();
            this.mPackageMonitor.register(context, handlerThread.getLooper(), android.os.UserHandle.ALL, true);
            android.content.IntentFilter intentFilter = new android.content.IntentFilter();
            intentFilter.addAction("android.intent.action.USER_ADDED");
            intentFilter.addAction("android.intent.action.USER_REMOVED");
            getContext().registerReceiverAsUser(new com.android.server.om.OverlayManagerService.UserReceiver(), android.os.UserHandle.ALL, intentFilter, null, null);
            ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).addUserLifecycleListener(new com.android.server.om.OverlayManagerService.UserLifecycleListener());
            restoreSettings();
            final java.lang.String strEmptyIfNull = android.text.TextUtils.emptyIfNull(getContext().getString(android.R.string.config_systemShell));
            this.mSettings.removeIf(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.om.OverlayManagerService.lambda$new$0(strEmptyIfNull, (android.content.om.OverlayInfo) obj);
                }
            });
            initIfNeeded();
            onStartUser(0);
            publishBinderService("overlay", this.mService);
            publishLocalService(com.android.server.om.OverlayManagerService.class, this);
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    static /* synthetic */ boolean lambda$new$0(java.lang.String shellPkgName, android.content.om.OverlayInfo overlayInfo) {
        return overlayInfo.isFabricated && shellPkgName.equals(overlayInfo.packageName);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    private void initIfNeeded() {
        android.os.UserManager um = (android.os.UserManager) getContext().getSystemService(android.os.UserManager.class);
        java.util.List<android.content.pm.UserInfo> users = um.getAliveUsers();
        synchronized (this.mLock) {
            int userCount = users.size();
            for (int i = 0; i < userCount; i++) {
                android.content.pm.UserInfo userInfo = users.get(i);
                if (!userInfo.supportsSwitchTo() && userInfo.id != 0) {
                    updatePackageManagerLocked(this.mImpl.updateOverlaysForUser(users.get(i).id));
                }
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(final com.android.server.SystemService.TargetUser from, final com.android.server.SystemService.TargetUser to) {
        com.android.server.FgThread.getHandler().postAtFrontOfQueue(new java.lang.Runnable() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserSwitching$1(from, to);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserSwitching$1(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        onSwitchUser(from.getUserIdentifier(), to.getUserIdentifier());
    }

    private void onSwitchUser(int fromUserId, int newUserId) {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#onSwitchUser " + newUserId);
            synchronized (this.mLock) {
                updateTargetPackagesLocked(this.mImpl.updateOverlaysForUser(newUserId), true, fromUserId);
            }
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    private void onSwitchUser(int newUserId) {
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(final com.android.server.SystemService.TargetUser user) {
        com.android.server.FgThread.getHandler().postAtFrontOfQueue(new java.lang.Runnable() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserStarting$2(user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserStarting$2(com.android.server.SystemService.TargetUser user) {
        onStartUser(user.getUserIdentifier());
    }

    private void onStartUser(int newUserId) {
        if (newUserId == this.mPrevStartedUserId) {
            return;
        }
        android.util.Slog.i(TAG, "Updating overlays for starting user " + newUserId);
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#onStartUser " + newUserId);
            synchronized (this.mLock) {
                updateTargetPackagesLocked(this.mImpl.updateOverlaysForUser(newUserId), true, -10000);
            }
            android.os.Trace.traceEnd(67108864L);
            this.mPrevStartedUserId = newUserId;
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(67108864L);
            throw th;
        }
    }

    private static java.lang.String[] getDefaultOverlayPackages() {
        java.lang.String str = android.os.SystemProperties.get(DEFAULT_OVERLAYS_PROP);
        if (android.text.TextUtils.isEmpty(str)) {
            return libcore.util.EmptyArray.STRING;
        }
        android.util.ArraySet<java.lang.String> defaultPackages = new android.util.ArraySet<>();
        for (java.lang.String packageName : str.split(";")) {
            if (!android.text.TextUtils.isEmpty(packageName)) {
                defaultPackages.add(packageName);
            }
        }
        return (java.lang.String[]) defaultPackages.toArray(new java.lang.String[0]);
    }

    private final class OverlayManagerPackageMonitor extends com.android.internal.content.PackageMonitor {
        private OverlayManagerPackageMonitor() {
        }

        public void onPackageAppearedWithExtras(java.lang.String packageName, android.os.Bundle extras) {
            com.android.server.om.OverlayManagerService.this.handlePackageAdd(packageName, extras, getChangingUserId());
        }

        public void onPackageChangedWithExtras(java.lang.String packageName, android.os.Bundle extras) {
            com.android.server.om.OverlayManagerService.this.handlePackageChange(packageName, extras, getChangingUserId());
        }

        public void onPackageDisappearedWithExtras(java.lang.String packageName, android.os.Bundle extras) {
            com.android.server.om.OverlayManagerService.this.handlePackageRemove(packageName, extras, getChangingUserId());
        }
    }

    private int[] getUserIds(int uid) {
        if (uid == -1) {
            int[] userIds = this.mUserManager.getUserIds();
            return userIds;
        }
        int[] userIds2 = {android.os.UserHandle.getUserId(uid)};
        return userIds2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageAdd(java.lang.String packageName, android.os.Bundle extras, int userId) {
        boolean replacing = extras.getBoolean("android.intent.extra.REPLACING", false);
        if (replacing) {
            onPackageReplaced(packageName, userId);
        } else {
            onPackageAdded(packageName, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageChange(java.lang.String packageName, android.os.Bundle extras, int userId) {
        if (!"android.intent.action.OVERLAY_CHANGED".equals(extras.getString("android.intent.extra.REASON"))) {
            onPackageChanged(packageName, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageRemove(java.lang.String packageName, android.os.Bundle extras, int userId) {
        boolean replacing = extras.getBoolean("android.intent.extra.REPLACING", false);
        boolean systemUpdateUninstall = extras.getBoolean("android.intent.extra.SYSTEM_UPDATE_UNINSTALL", false);
        if (replacing) {
            onPackageReplacing(packageName, systemUpdateUninstall, userId);
        } else {
            onPackageRemoved(packageName, userId);
        }
    }

    private void onPackageAdded(java.lang.String packageName, int userId) {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#onPackageAdded " + packageName);
            synchronized (this.mLock) {
                com.android.server.pm.pkg.PackageState packageState = this.mPackageManager.onPackageAdded(packageName, userId);
                if (packageState != null && !this.mPackageManager.isInstantApp(packageName, userId)) {
                    try {
                        updateTargetPackagesLocked(this.mImpl.onPackageAdded(packageName, userId));
                    } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                        android.util.Slog.e(TAG, "onPackageAdded internal error", e);
                    }
                }
            }
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    private void onPackageChanged(java.lang.String packageName, int userId) {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#onPackageChanged " + packageName);
            synchronized (this.mLock) {
                com.android.server.pm.pkg.PackageState packageState = this.mPackageManager.onPackageUpdated(packageName, userId);
                if (packageState != null && !this.mPackageManager.isInstantApp(packageName, userId)) {
                    try {
                        updateTargetPackagesLocked(this.mImpl.onPackageChanged(packageName, userId));
                    } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                        android.util.Slog.e(TAG, "onPackageChanged internal error", e);
                    }
                }
            }
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    private void onPackageReplacing(java.lang.String packageName, boolean systemUpdateUninstall, int userId) {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#onPackageReplacing " + packageName);
            synchronized (this.mLock) {
                com.android.server.pm.pkg.PackageState packageState = this.mPackageManager.onPackageUpdated(packageName, userId);
                if (packageState != null && !this.mPackageManager.isInstantApp(packageName, userId)) {
                    try {
                        updateTargetPackagesLocked(this.mImpl.onPackageReplacing(packageName, systemUpdateUninstall, userId));
                    } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                        android.util.Slog.e(TAG, "onPackageReplacing internal error", e);
                    }
                }
            }
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    private void onPackageReplaced(java.lang.String packageName, int userId) {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#onPackageReplaced " + packageName);
            synchronized (this.mLock) {
                com.android.server.pm.pkg.PackageState packageState = this.mPackageManager.onPackageUpdated(packageName, userId);
                if (packageState != null && !this.mPackageManager.isInstantApp(packageName, userId)) {
                    try {
                        updateTargetPackagesLocked(this.mImpl.onPackageReplaced(packageName, userId));
                    } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                        android.util.Slog.e(TAG, "onPackageReplaced internal error", e);
                    }
                }
            }
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    private void onPackageRemoved(java.lang.String packageName, int userId) {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#onPackageRemoved " + packageName);
            synchronized (this.mLock) {
                this.mPackageManager.onPackageRemoved(packageName, userId);
                updateTargetPackagesLocked(this.mImpl.onPackageRemoved(packageName, userId));
            }
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isHighPriorityUserCreation(android.content.pm.UserInfo user) {
        return user != null && user.isMain();
    }

    private final class UserLifecycleListener implements com.android.server.pm.UserManagerInternal.UserLifecycleListener {
        private UserLifecycleListener() {
        }

        @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
        public void onUserCreated(android.content.pm.UserInfo user, java.lang.Object token) {
            if (com.android.server.om.OverlayManagerService.isHighPriorityUserCreation(user)) {
                int userId = user.id;
                try {
                    android.util.Slog.i(com.android.server.om.OverlayManagerService.TAG, "Updating overlays for onUserCreated " + userId);
                    android.os.Trace.traceBegin(67108864L, "OMS#onUserCreated " + userId);
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        com.android.server.om.OverlayManagerService.this.updatePackageManagerLocked(com.android.server.om.OverlayManagerService.this.mImpl.updateOverlaysForUser(userId));
                    }
                } finally {
                    android.os.Trace.traceEnd(67108864L);
                }
            }
        }
    }

    private final class UserReceiver extends android.content.BroadcastReceiver {
        private UserReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:11:0x0028  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
                r8 = this;
                java.lang.String r0 = "android.intent.extra.user_handle"
                r1 = -10000(0xffffffffffffd8f0, float:NaN)
                int r0 = r10.getIntExtra(r0, r1)
                java.lang.String r2 = r10.getAction()
                int r3 = r2.hashCode()
                switch(r3) {
                    case -2061058799: goto L1e;
                    case 1121780209: goto L14;
                    default: goto L13;
                }
            L13:
                goto L28
            L14:
                java.lang.String r3 = "android.intent.action.USER_ADDED"
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L13
                r2 = 0
                goto L29
            L1e:
                java.lang.String r3 = "android.intent.action.USER_REMOVED"
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L13
                r2 = 1
                goto L29
            L28:
                r2 = -1
            L29:
                r3 = 67108864(0x4000000, double:3.3156184E-316)
                switch(r2) {
                    case 0: goto L5e;
                    case 1: goto L31;
                    default: goto L2f;
                }
            L2f:
                goto Lb2
            L31:
                if (r0 == r1) goto Lb2
                java.lang.String r1 = "OMS ACTION_USER_REMOVED"
                android.os.Trace.traceBegin(r3, r1)     // Catch: java.lang.Throwable -> L59
                com.android.server.om.OverlayManagerService r1 = com.android.server.om.OverlayManagerService.this     // Catch: java.lang.Throwable -> L59
                java.lang.Object r1 = com.android.server.om.OverlayManagerService.m5894$$Nest$fgetmLock(r1)     // Catch: java.lang.Throwable -> L59
                monitor-enter(r1)     // Catch: java.lang.Throwable -> L59
                com.android.server.om.OverlayManagerService r2 = com.android.server.om.OverlayManagerService.this     // Catch: java.lang.Throwable -> L56
                com.android.server.om.OverlayManagerServiceImpl r2 = com.android.server.om.OverlayManagerService.m5893$$Nest$fgetmImpl(r2)     // Catch: java.lang.Throwable -> L56
                r2.onUserRemoved(r0)     // Catch: java.lang.Throwable -> L56
                com.android.server.om.OverlayManagerService r2 = com.android.server.om.OverlayManagerService.this     // Catch: java.lang.Throwable -> L56
                com.android.server.om.OverlayManagerService$PackageManagerHelperImpl r2 = com.android.server.om.OverlayManagerService.m5895$$Nest$fgetmPackageManager(r2)     // Catch: java.lang.Throwable -> L56
                r2.forgetAllPackageInfos(r0)     // Catch: java.lang.Throwable -> L56
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L56
                android.os.Trace.traceEnd(r3)
                goto Lb2
            L56:
                r2 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L56
                throw r2     // Catch: java.lang.Throwable -> L59
            L59:
                r1 = move-exception
                android.os.Trace.traceEnd(r3)
                throw r1
            L5e:
                java.lang.Class<com.android.server.pm.UserManagerInternal> r2 = com.android.server.pm.UserManagerInternal.class
                java.lang.Object r2 = com.android.server.LocalServices.getService(r2)
                com.android.server.pm.UserManagerInternal r2 = (com.android.server.pm.UserManagerInternal) r2
                android.content.pm.UserInfo r5 = r2.getUserInfo(r0)
                if (r0 == r1) goto Lb2
                boolean r1 = com.android.server.om.OverlayManagerService.m5902$$Nest$smisHighPriorityUserCreation(r5)
                if (r1 != 0) goto Lb2
                java.lang.String r1 = "OverlayManager"
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lad
                r6.<init>()     // Catch: java.lang.Throwable -> Lad
                java.lang.String r7 = "Updating overlays for added user "
                java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> Lad
                java.lang.StringBuilder r6 = r6.append(r0)     // Catch: java.lang.Throwable -> Lad
                java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> Lad
                android.util.Slog.i(r1, r6)     // Catch: java.lang.Throwable -> Lad
                java.lang.String r1 = "OMS ACTION_USER_ADDED"
                android.os.Trace.traceBegin(r3, r1)     // Catch: java.lang.Throwable -> Lad
                com.android.server.om.OverlayManagerService r1 = com.android.server.om.OverlayManagerService.this     // Catch: java.lang.Throwable -> Lad
                java.lang.Object r1 = com.android.server.om.OverlayManagerService.m5894$$Nest$fgetmLock(r1)     // Catch: java.lang.Throwable -> Lad
                monitor-enter(r1)     // Catch: java.lang.Throwable -> Lad
                com.android.server.om.OverlayManagerService r6 = com.android.server.om.OverlayManagerService.this     // Catch: java.lang.Throwable -> Laa
                com.android.server.om.OverlayManagerService r7 = com.android.server.om.OverlayManagerService.this     // Catch: java.lang.Throwable -> Laa
                com.android.server.om.OverlayManagerServiceImpl r7 = com.android.server.om.OverlayManagerService.m5893$$Nest$fgetmImpl(r7)     // Catch: java.lang.Throwable -> Laa
                android.util.ArraySet r7 = r7.updateOverlaysForUser(r0)     // Catch: java.lang.Throwable -> Laa
                com.android.server.om.OverlayManagerService.m5900$$Nest$mupdatePackageManagerLocked(r6, r7)     // Catch: java.lang.Throwable -> Laa
                monitor-exit(r1)     // Catch: java.lang.Throwable -> Laa
                android.os.Trace.traceEnd(r3)
                goto Lb2
            Laa:
                r6 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> Laa
                throw r6     // Catch: java.lang.Throwable -> Lad
            Lad:
                r1 = move-exception
                android.os.Trace.traceEnd(r3)
                throw r1
            Lb2:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.om.OverlayManagerService.UserReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    /* JADX INFO: renamed from: com.android.server.om.OverlayManagerService$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.om.IOverlayManager.Stub {
        AnonymousClass1() {
        }

        public java.util.Map<java.lang.String, java.util.List<android.content.om.OverlayInfo>> getAllOverlays(int userIdArg) {
            java.util.Map<java.lang.String, java.util.List<android.content.om.OverlayInfo>> overlaysForUser;
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#getAllOverlays " + userIdArg);
                int realUserId = handleIncomingUser(userIdArg, "getAllOverlays");
                synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                    overlaysForUser = com.android.server.om.OverlayManagerService.this.mImpl.getOverlaysForUser(realUserId);
                }
                return overlaysForUser;
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public java.util.List<android.content.om.OverlayInfo> getOverlayInfosForTarget(java.lang.String targetPackageName, int userIdArg) {
            java.util.List<android.content.om.OverlayInfo> overlayInfosForTarget;
            if (targetPackageName == null) {
                return java.util.Collections.emptyList();
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#getOverlayInfosForTarget " + targetPackageName);
                int realUserId = handleIncomingUser(userIdArg, "getOverlayInfosForTarget");
                synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                    overlayInfosForTarget = com.android.server.om.OverlayManagerService.this.mImpl.getOverlayInfosForTarget(targetPackageName, realUserId);
                }
                return overlayInfosForTarget;
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public android.content.om.OverlayInfo getOverlayInfo(java.lang.String packageName, int userIdArg) {
            return getOverlayInfoByIdentifier(new android.content.om.OverlayIdentifier(packageName), userIdArg);
        }

        public android.content.om.OverlayInfo getOverlayInfoByIdentifier(android.content.om.OverlayIdentifier overlay, int userIdArg) {
            android.content.om.OverlayInfo overlayInfo;
            if (overlay == null || overlay.getPackageName() == null) {
                return null;
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#getOverlayInfo " + overlay);
                int realUserId = handleIncomingUser(userIdArg, "getOverlayInfo");
                synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                    overlayInfo = com.android.server.om.OverlayManagerService.this.mImpl.getOverlayInfo(overlay, realUserId);
                }
                return overlayInfo;
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public boolean setEnabled(java.lang.String packageName, boolean enable, int userIdArg) {
            if (packageName == null) {
                return false;
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#setEnabled " + packageName + " " + enable);
                android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(packageName);
                int realUserId = handleIncomingUser(userIdArg, "setEnabled");
                enforceActor(overlay, "setEnabled", realUserId);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        try {
                            com.android.server.om.OverlayManagerService.this.updateTargetPackagesLocked(com.android.server.om.OverlayManagerService.this.mImpl.setEnabled(overlay, enable, realUserId));
                        } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                            return false;
                        }
                    }
                    android.os.Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public boolean setEnabledExclusive(java.lang.String packageName, boolean enable, int userIdArg) {
            if (packageName == null || !enable) {
                return false;
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#setEnabledExclusive " + packageName + " " + enable);
                android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(packageName);
                int realUserId = handleIncomingUser(userIdArg, "setEnabledExclusive");
                enforceActor(overlay, "setEnabledExclusive", realUserId);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        try {
                            com.android.server.om.OverlayManagerService.this.mImpl.setEnabledExclusive(overlay, false, realUserId).ifPresent(new com.android.server.om.OverlayManagerService$1$$ExternalSyntheticLambda0(com.android.server.om.OverlayManagerService.this));
                        } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                            return false;
                        }
                    }
                    android.os.Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public boolean setEnabledExclusiveInCategory(java.lang.String packageName, int userIdArg) {
            if (packageName == null) {
                return false;
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#setEnabledExclusiveInCategory " + packageName);
                android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(packageName);
                int realUserId = handleIncomingUser(userIdArg, "setEnabledExclusiveInCategory");
                enforceActor(overlay, "setEnabledExclusiveInCategory", realUserId);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        try {
                            com.android.server.om.OverlayManagerService.this.mImpl.setEnabledExclusive(overlay, true, realUserId).ifPresent(new com.android.server.om.OverlayManagerService$1$$ExternalSyntheticLambda0(com.android.server.om.OverlayManagerService.this));
                        } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                            return false;
                        }
                    }
                    return true;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public boolean setPriority(java.lang.String packageName, java.lang.String parentPackageName, int userIdArg) {
            if (packageName == null || parentPackageName == null) {
                return false;
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#setPriority " + packageName + " " + parentPackageName);
                android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(packageName);
                android.content.om.OverlayIdentifier parentOverlay = new android.content.om.OverlayIdentifier(parentPackageName);
                int realUserId = handleIncomingUser(userIdArg, "setPriority");
                enforceActor(overlay, "setPriority", realUserId);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        try {
                            com.android.server.om.OverlayManagerService.this.mImpl.setPriority(overlay, parentOverlay, realUserId).ifPresent(new com.android.server.om.OverlayManagerService$1$$ExternalSyntheticLambda0(com.android.server.om.OverlayManagerService.this));
                        } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                            return false;
                        }
                    }
                    android.os.Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public boolean setHighestPriority(java.lang.String packageName, int userIdArg) {
            if (packageName == null) {
                return false;
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#setHighestPriority " + packageName);
                android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(packageName);
                int realUserId = handleIncomingUser(userIdArg, "setHighestPriority");
                enforceActor(overlay, "setHighestPriority", realUserId);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        try {
                            com.android.server.om.OverlayManagerService.this.updateTargetPackagesLocked(com.android.server.om.OverlayManagerService.this.mImpl.setHighestPriority(overlay, realUserId));
                        } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                            return false;
                        }
                    }
                    android.os.Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public boolean setLowestPriority(java.lang.String packageName, int userIdArg) {
            if (packageName == null) {
                return false;
            }
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#setLowestPriority " + packageName);
                android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(packageName);
                int realUserId = handleIncomingUser(userIdArg, "setLowestPriority");
                enforceActor(overlay, "setLowestPriority", realUserId);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        try {
                            com.android.server.om.OverlayManagerService.this.mImpl.setLowestPriority(overlay, realUserId).ifPresent(new com.android.server.om.OverlayManagerService$1$$ExternalSyntheticLambda0(com.android.server.om.OverlayManagerService.this));
                        } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                            return false;
                        }
                    }
                    android.os.Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public java.lang.String[] getDefaultOverlayPackages() {
            java.lang.String[] defaultOverlayPackages;
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#getDefaultOverlayPackages");
                com.android.server.om.OverlayManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.MODIFY_THEME_OVERLAY", null);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                        defaultOverlayPackages = com.android.server.om.OverlayManagerService.this.mImpl.getDefaultOverlayPackages();
                    }
                    return defaultOverlayPackages;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        public void invalidateCachesForOverlay(java.lang.String packageName, int userIdArg) {
            if (packageName == null) {
                return;
            }
            android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(packageName);
            int realUserId = handleIncomingUser(userIdArg, "invalidateCachesForOverlay");
            enforceActor(overlay, "invalidateCachesForOverlay", realUserId);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                    try {
                        com.android.server.om.OverlayManagerService.this.mImpl.removeIdmapForOverlay(overlay, realUserId);
                    } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                        android.util.Slog.w(com.android.server.om.OverlayManagerService.TAG, "invalidate caches for overlay '" + overlay + "' failed", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void commit(android.content.om.OverlayManagerTransaction transaction) throws android.os.RemoteException {
            try {
                android.os.Trace.traceBegin(67108864L, "OMS#commit " + transaction);
                try {
                    executeAllRequests(transaction);
                } catch (java.lang.Exception e) {
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.server.om.OverlayManagerService.this.restoreSettings();
                        android.os.Binder.restoreCallingIdentity(ident);
                        android.util.Slog.d(com.android.server.om.OverlayManagerService.TAG, "commit failed: " + e.getMessage(), e);
                        throw new java.lang.SecurityException("commit failed" + ((com.android.server.om.OverlayManagerService.DEBUG || android.os.Build.IS_DEBUGGABLE) ? ": " + e.getMessage() : ""));
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                }
            } finally {
                android.os.Trace.traceEnd(67108864L);
            }
        }

        private java.util.Set<android.content.pm.UserPackage> executeRequest(android.content.om.OverlayManagerTransaction.Request request) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
            int realUserId;
            java.util.Objects.requireNonNull(request, "Transaction contains a null request");
            java.util.Objects.requireNonNull(request.overlay, "Transaction overlay identifier must be non-null");
            int callingUid = android.os.Binder.getCallingUid();
            if (request.type != 2 && request.type != 3) {
                realUserId = handleIncomingUser(request.userId, request.typeToString());
                enforceActor(request.overlay, request.typeToString(), realUserId);
            } else {
                if (request.userId != -1) {
                    throw new java.lang.IllegalArgumentException(request.typeToString() + " unsupported for user " + request.userId);
                }
                if (callingUid == 2000) {
                    android.util.EventLog.writeEvent(1397638484, "202768292", -1, "");
                    throw new java.lang.IllegalArgumentException("Non-root shell cannot fabricate overlays");
                }
                realUserId = -1;
                java.lang.String pkgName = request.overlay.getPackageName();
                if (callingUid != 0 && !com.android.internal.util.ArrayUtils.contains(com.android.server.om.OverlayManagerService.this.mPackageManager.getPackagesForUid(callingUid), pkgName)) {
                    throw new java.lang.IllegalArgumentException("UID " + callingUid + " does own packagename " + pkgName);
                }
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                switch (request.type) {
                    case 0:
                        java.util.Set<android.content.pm.UserPackage> result = com.android.internal.util.CollectionUtils.addAll((java.util.Set) null, com.android.server.om.OverlayManagerService.this.mImpl.setEnabled(request.overlay, true, realUserId));
                        return com.android.internal.util.CollectionUtils.emptyIfNull(com.android.internal.util.CollectionUtils.addAll(result, com.android.server.om.OverlayManagerService.this.mImpl.setHighestPriority(request.overlay, realUserId)));
                    case 1:
                        return com.android.server.om.OverlayManagerService.this.mImpl.setEnabled(request.overlay, false, realUserId);
                    case 2:
                        android.os.FabricatedOverlayInternal fabricated = (android.os.FabricatedOverlayInternal) request.extras.getParcelable("fabricated_overlay", android.os.FabricatedOverlayInternal.class);
                        java.util.Objects.requireNonNull(fabricated, "no fabricated overlay attached to request");
                        return com.android.server.om.OverlayManagerService.this.mImpl.registerFabricatedOverlay(fabricated);
                    case 3:
                        return com.android.server.om.OverlayManagerService.this.mImpl.unregisterFabricatedOverlay(request.overlay);
                    default:
                        throw new java.lang.IllegalArgumentException("unsupported request: " + request);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        private void executeAllRequests(android.content.om.OverlayManagerTransaction transaction) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
            if (com.android.server.om.OverlayManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.om.OverlayManagerService.TAG, "commit " + transaction);
            }
            if (transaction == null) {
                throw new java.lang.IllegalArgumentException("null transaction");
            }
            synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                java.util.Set<android.content.pm.UserPackage> affectedPackagesToUpdate = null;
                java.util.Iterator<android.content.om.OverlayManagerTransaction.Request> it = transaction.getRequests();
                while (it.hasNext()) {
                    android.content.om.OverlayManagerTransaction.Request request = it.next();
                    affectedPackagesToUpdate = com.android.internal.util.CollectionUtils.addAll(affectedPackagesToUpdate, executeRequest(request));
                }
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.om.OverlayManagerService.this.updateTargetPackagesLocked(affectedPackagesToUpdate);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.om.OverlayManagerShellCommand(com.android.server.om.OverlayManagerService.this.getContext(), this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            java.lang.String opt;
            com.android.server.om.DumpState dumpState = new com.android.server.om.DumpState();
            byte b = -1;
            dumpState.setUserId(-1);
            int opti = 0;
            while (opti < args.length && (opt = args[opti]) != null && opt.length() > 0 && opt.charAt(0) == '-') {
                opti++;
                if (!"-a".equals(opt)) {
                    if ("-h".equals(opt)) {
                        pw.println("dump [-h] [--verbose] [--user USER_ID] [[FIELD] PACKAGE]");
                        pw.println("  Print debugging information about the overlay manager.");
                        pw.println("  With optional parameter PACKAGE, limit output to the specified");
                        pw.println("  package. With optional parameter FIELD, limit output to");
                        pw.println("  the value of that SettingsItem field. Field names are");
                        pw.println("  case insensitive and out.println the m prefix can be omitted,");
                        pw.println("  so the following are equivalent: mState, mstate, State, state.");
                        return;
                    }
                    if ("--user".equals(opt)) {
                        if (opti >= args.length) {
                            pw.println("Error: user missing argument");
                            return;
                        }
                        try {
                            dumpState.setUserId(java.lang.Integer.parseInt(args[opti]));
                            opti++;
                        } catch (java.lang.NumberFormatException e) {
                            pw.println("Error: user argument is not a number: " + args[opti]);
                            return;
                        }
                    } else if ("--verbose".equals(opt)) {
                        dumpState.setVerbose(true);
                    } else {
                        pw.println("Unknown argument: " + opt + "; use -h for help");
                    }
                }
            }
            if (opti < args.length) {
                java.lang.String arg = args[opti];
                opti++;
                switch (arg.hashCode()) {
                    case -1750736508:
                        if (arg.equals("targetoverlayablename")) {
                            b = 3;
                        }
                        break;
                    case -1248283232:
                        if (arg.equals("targetpackagename")) {
                            b = 2;
                        }
                        break;
                    case -1165461084:
                        if (arg.equals("priority")) {
                            b = 8;
                        }
                        break;
                    case -836029914:
                        if (arg.equals("userid")) {
                            b = 1;
                        }
                        break;
                    case -831052100:
                        if (arg.equals("ismutable")) {
                            b = 7;
                        }
                        break;
                    case 50511102:
                        if (arg.equals("category")) {
                            b = 9;
                        }
                        break;
                    case 109757585:
                        if (arg.equals("state")) {
                            b = 5;
                        }
                        break;
                    case 440941271:
                        if (arg.equals("isenabled")) {
                            b = 6;
                        }
                        break;
                    case 909712337:
                        if (arg.equals("packagename")) {
                            b = 0;
                        }
                        break;
                    case 1693907299:
                        if (arg.equals("basecodepath")) {
                            b = 4;
                        }
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        dumpState.setField(arg);
                        break;
                    default:
                        dumpState.setOverlyIdentifier(arg);
                        break;
                }
            }
            if (dumpState.getPackageName() == null && opti < args.length) {
                dumpState.setOverlyIdentifier(args[opti]);
                int i = opti + 1;
            }
            enforceDumpPermission("dump");
            synchronized (com.android.server.om.OverlayManagerService.this.mLock) {
                com.android.server.om.OverlayManagerService.this.mImpl.dump(pw, dumpState);
                if (dumpState.getPackageName() == null) {
                    com.android.server.om.OverlayManagerService.this.mPackageManager.dump(pw, dumpState);
                }
            }
        }

        private int handleIncomingUser(int userId, java.lang.String message) {
            return android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, message, null);
        }

        private void enforceDumpPermission(java.lang.String message) {
            com.android.server.om.OverlayManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DUMP", message);
        }

        private void enforceActor(android.content.om.OverlayIdentifier overlay, java.lang.String methodName, int realUserId) throws java.lang.SecurityException {
            android.content.om.OverlayInfo overlayInfo = com.android.server.om.OverlayManagerService.this.mImpl.getOverlayInfo(overlay, realUserId);
            if (overlayInfo == null) {
                throw new java.lang.IllegalArgumentException("Unable to retrieve overlay information for " + overlay);
            }
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.om.OverlayManagerService.this.mActorEnforcer.enforceActor(overlayInfo, methodName, callingUid, realUserId);
        }

        public java.lang.String getPartitionOrder() {
            return com.android.server.om.OverlayManagerService.this.mImpl.getOverlayConfig().getPartitionOrder();
        }

        public boolean isDefaultPartitionOrder() {
            return com.android.server.om.OverlayManagerService.this.mImpl.getOverlayConfig().isDefaultPartitionOrder();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class PackageManagerHelperImpl implements com.android.server.om.PackageManagerHelper {
        private static final java.lang.String TAB1 = "    ";
        private final android.content.Context mContext;
        private final android.util.ArrayMap<java.lang.String, com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers> mCache = new android.util.ArrayMap<>();
        private final android.util.ArraySet<java.lang.Integer> mInitializedUsers = new android.util.ArraySet<>();
        private final android.content.pm.IPackageManager mPackageManager = android.app.AppGlobals.getPackageManager();
        private final android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);

        private static class PackageStateUsers {
            private final java.util.Set<java.lang.Integer> mInstalledUsers;
            private com.android.server.pm.pkg.PackageState mPackageState;

            private PackageStateUsers(com.android.server.pm.pkg.PackageState packageState) {
                this.mInstalledUsers = new android.util.ArraySet();
                this.mPackageState = packageState;
            }
        }

        PackageManagerHelperImpl(android.content.Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.om.PackageManagerHelper
        public android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.PackageState> initializeForUser(final int userId) {
            if (this.mInitializedUsers.add(java.lang.Integer.valueOf(userId))) {
                this.mPackageManagerInternal.forEachPackageState(new java.util.function.Consumer() { // from class: com.android.server.om.OverlayManagerService$PackageManagerHelperImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$initializeForUser$0(userId, (com.android.server.pm.pkg.PackageStateInternal) obj);
                    }
                });
            }
            android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.PackageState> userPackages = new android.util.ArrayMap<>();
            int n = this.mCache.size();
            for (int i = 0; i < n; i++) {
                com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers pkg = this.mCache.valueAt(i);
                if (pkg.mInstalledUsers.contains(java.lang.Integer.valueOf(userId))) {
                    userPackages.put(this.mCache.keyAt(i), pkg.mPackageState);
                }
            }
            return userPackages;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$initializeForUser$0(int userId, com.android.server.pm.pkg.PackageStateInternal packageState) {
            if (packageState.getPkg() != null && packageState.getUserStateOrDefault(userId).isInstalled()) {
                addPackageUser(packageState, userId);
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public com.android.server.pm.pkg.PackageState getPackageStateForUser(java.lang.String packageName, int userId) {
            com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers pkg = this.mCache.get(packageName);
            if (pkg != null && pkg.mInstalledUsers.contains(java.lang.Integer.valueOf(userId))) {
                return pkg.mPackageState;
            }
            try {
                if (!this.mPackageManager.isPackageAvailable(packageName, userId)) {
                    return null;
                }
                return addPackageUser(packageName, userId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.om.OverlayManagerService.TAG, "Failed to check availability of package '" + packageName + "' for user " + userId, e);
                return null;
            }
        }

        private com.android.server.pm.pkg.PackageState addPackageUser(java.lang.String packageName, int user) {
            com.android.server.pm.pkg.PackageState pkg = this.mPackageManagerInternal.getPackageStateInternal(packageName);
            if (pkg == null) {
                android.util.Slog.w(com.android.server.om.OverlayManagerService.TAG, "Android package for '" + packageName + "' could not be found; continuing as if package was never added", new java.lang.Throwable());
                return null;
            }
            return addPackageUser(pkg, user);
        }

        private com.android.server.pm.pkg.PackageState addPackageUser(com.android.server.pm.pkg.PackageState pkg, int user) {
            com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers pkgUsers = this.mCache.get(pkg.getPackageName());
            if (pkgUsers == null) {
                pkgUsers = new com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers(pkg);
                this.mCache.put(pkg.getPackageName(), pkgUsers);
            } else {
                pkgUsers.mPackageState = pkg;
            }
            pkgUsers.mInstalledUsers.add(java.lang.Integer.valueOf(user));
            return pkgUsers.mPackageState;
        }

        private void removePackageUser(java.lang.String packageName, int user) {
            com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers pkgUsers = this.mCache.get(packageName);
            if (pkgUsers == null) {
                return;
            }
            removePackageUser(pkgUsers, user);
        }

        private void removePackageUser(com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers pkg, int user) {
            pkg.mInstalledUsers.remove(java.lang.Integer.valueOf(user));
            if (pkg.mInstalledUsers.isEmpty()) {
                this.mCache.remove(pkg.mPackageState.getPackageName());
            }
        }

        public com.android.server.pm.pkg.PackageState onPackageAdded(java.lang.String packageName, int userId) {
            return addPackageUser(packageName, userId);
        }

        public com.android.server.pm.pkg.PackageState onPackageUpdated(java.lang.String packageName, int userId) {
            return addPackageUser(packageName, userId);
        }

        public void onPackageRemoved(java.lang.String packageName, int userId) {
            removePackageUser(packageName, userId);
        }

        @Override // com.android.server.om.PackageManagerHelper
        public boolean isInstantApp(java.lang.String packageName, int userId) {
            return this.mPackageManagerInternal.isInstantApp(packageName, userId);
        }

        @Override // com.android.server.om.PackageManagerHelper
        public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getNamedActors() {
            return com.android.server.SystemConfig.getInstance().getNamedActors();
        }

        @Override // com.android.server.om.PackageManagerHelper
        public boolean signaturesMatching(java.lang.String packageName1, java.lang.String packageName2, int userId) {
            try {
                return this.mPackageManager.checkSignatures(packageName1, packageName2, userId) == 0;
            } catch (android.os.RemoteException e) {
                return false;
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public java.lang.String getConfigSignaturePackage() {
            java.lang.String[] pkgs = this.mPackageManagerInternal.getKnownPackageNames(13, 0);
            if (pkgs.length == 0) {
                return null;
            }
            return pkgs[0];
        }

        @Override // com.android.server.om.PackageManagerHelper
        public android.content.om.OverlayableInfo getOverlayableForTarget(java.lang.String packageName, java.lang.String targetOverlayableName, int userId) throws java.io.IOException {
            com.android.server.pm.pkg.PackageState packageState = getPackageStateForUser(packageName, userId);
            com.android.server.pm.pkg.AndroidPackage pkg = packageState == null ? null : packageState.getAndroidPackage();
            if (pkg == null) {
                throw new java.io.IOException("Unable to get target package");
            }
            android.content.res.ApkAssets apkAssets = null;
            try {
                apkAssets = android.content.res.ApkAssets.loadFromPath(((com.android.server.pm.pkg.AndroidPackageSplit) pkg.getSplits().get(0)).getPath(), 32);
                return apkAssets.getOverlayableInfo(targetOverlayableName);
            } finally {
                if (apkAssets != null) {
                    try {
                        apkAssets.close();
                    } catch (java.lang.Throwable th) {
                    }
                }
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public boolean doesTargetDefineOverlayable(java.lang.String targetPackageName, int userId) throws java.io.IOException {
            com.android.server.pm.pkg.PackageState packageState = getPackageStateForUser(targetPackageName, userId);
            com.android.server.pm.pkg.AndroidPackage pkg = packageState == null ? null : packageState.getAndroidPackage();
            if (pkg == null) {
                throw new java.io.IOException("Unable to get target package");
            }
            android.content.res.ApkAssets apkAssets = null;
            try {
                apkAssets = android.content.res.ApkAssets.loadFromPath(((com.android.server.pm.pkg.AndroidPackageSplit) pkg.getSplits().get(0)).getPath(), 32);
                return apkAssets.definesOverlayable();
            } finally {
                if (apkAssets != null) {
                    try {
                        apkAssets.close();
                    } catch (java.lang.Throwable th) {
                    }
                }
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public void enforcePermission(java.lang.String permission, java.lang.String message) throws java.lang.SecurityException {
            this.mContext.enforceCallingOrSelfPermission(permission, message);
        }

        public void forgetAllPackageInfos(int userId) {
            for (int i = this.mCache.size() - 1; i >= 0; i--) {
                removePackageUser(this.mCache.valueAt(i), userId);
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public java.lang.String[] getPackagesForUid(int uid) {
            try {
                return this.mPackageManager.getPackagesForUid(uid);
            } catch (android.os.RemoteException e) {
                return null;
            }
        }

        public void dump(java.io.PrintWriter pw, com.android.server.om.DumpState dumpState) {
            pw.println("AndroidPackage cache");
            if (!dumpState.isVerbose()) {
                pw.println(TAB1 + this.mCache.size() + " package(s)");
                return;
            }
            if (this.mCache.size() == 0) {
                pw.println("    <empty>");
                return;
            }
            int n = this.mCache.size();
            for (int i = 0; i < n; i++) {
                java.lang.String packageName = this.mCache.keyAt(i);
                com.android.server.om.OverlayManagerService.PackageManagerHelperImpl.PackageStateUsers pkg = this.mCache.valueAt(i);
                pw.print(TAB1 + packageName + ": " + pkg.mPackageState + " users=");
                pw.println(android.text.TextUtils.join(", ", pkg.mInstalledUsers));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTargetPackagesLocked(android.content.pm.UserPackage updatedTarget) {
        if (updatedTarget != null) {
            updateTargetPackagesLocked(java.util.Set.of(updatedTarget));
        }
    }

    public void updateTargetPackagesLocked(java.util.Set<android.content.pm.UserPackage> updatedTargets) {
        updateTargetPackagesLocked(updatedTargets, false, -10000);
    }

    private void updateTargetPackagesLocked(java.util.Set<android.content.pm.UserPackage> updatedTargets, final boolean isSwitchUser, int fromUserId) {
        com.android.server.om.OverlayManagerService overlayManagerService = this;
        if (com.android.internal.util.CollectionUtils.isEmpty(updatedTargets)) {
            return;
        }
        persistSettingsLocked();
        android.util.SparseArray<android.util.ArraySet<java.lang.String>> userTargets = groupTargetsByUserId(updatedTargets);
        int n = userTargets.size();
        int i = 0;
        while (i < n) {
            final android.util.ArraySet<java.lang.String> targets = userTargets.valueAt(i);
            final int userId = userTargets.keyAt(i);
            final java.util.List<java.lang.String> affectedPackages = overlayManagerService.updatePackageManagerLocked(targets, userId);
            if (!affectedPackages.isEmpty()) {
                overlayManagerService.mOmServiceExt.hookAffectedPackages(affectedPackages, isSwitchUser, fromUserId, userId);
                com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$updateTargetPackagesLocked$3(affectedPackages, userId, isSwitchUser, targets);
                    }
                });
            }
            i++;
            overlayManagerService = this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTargetPackagesLocked$3(java.util.List affectedPackages, int userId, boolean isSwitchUser, android.util.ArraySet targets) {
        updateActivityManager(affectedPackages, userId);
        if (!isSwitchUser) {
            broadcastActionOverlayChanged(targets, userId);
        }
    }

    private static android.util.SparseArray<android.util.ArraySet<java.lang.String>> groupTargetsByUserId(java.util.Set<android.content.pm.UserPackage> targetsAndUsers) {
        final android.util.SparseArray<android.util.ArraySet<java.lang.String>> userTargets = new android.util.SparseArray<>();
        com.android.internal.util.CollectionUtils.forEach(targetsAndUsers, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda1
            public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                com.android.server.om.OverlayManagerService.lambda$groupTargetsByUserId$4(userTargets, (android.content.pm.UserPackage) obj);
            }
        });
        return userTargets;
    }

    static /* synthetic */ void lambda$groupTargetsByUserId$4(android.util.SparseArray userTargets, android.content.pm.UserPackage target) throws java.lang.Exception {
        android.util.ArraySet<java.lang.String> targets = (android.util.ArraySet) userTargets.get(target.userId);
        if (targets == null) {
            targets = new android.util.ArraySet<>();
            userTargets.put(target.userId, targets);
        }
        targets.add(target.packageName);
    }

    private static void broadcastActionOverlayChanged(java.util.Set<java.lang.String> targetPackages, final int userId) {
        final android.app.ActivityManagerInternal amInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        com.android.internal.util.CollectionUtils.forEach(targetPackages, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda4
            public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                com.android.server.om.OverlayManagerService.lambda$broadcastActionOverlayChanged$5(userId, amInternal, (java.lang.String) obj);
            }
        });
    }

    static /* synthetic */ void lambda$broadcastActionOverlayChanged$5(int userId, android.app.ActivityManagerInternal amInternal, java.lang.String target) throws java.lang.Exception {
        android.content.Intent intent = new android.content.Intent("android.intent.action.OVERLAY_CHANGED", android.net.Uri.fromParts("package", target, null));
        intent.setFlags(67108864);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", target);
        intent.putExtra("android.intent.extra.USER_ID", userId);
        amInternal.broadcastIntent(intent, (android.content.IIntentReceiver) null, (java.lang.String[]) null, false, userId, (int[]) null, new java.util.function.BiFunction() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda6
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.om.OverlayManagerService.filterReceiverAccess(((java.lang.Integer) obj).intValue(), (android.os.Bundle) obj2);
            }
        }, (android.os.Bundle) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.os.Bundle filterReceiverAccess(int callingUid, android.os.Bundle extras) {
        java.lang.String packageName = extras.getString("android.intent.extra.PACKAGE_NAME");
        int userId = extras.getInt("android.intent.extra.USER_ID");
        if (((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).filterAppAccess(packageName, callingUid, userId, false)) {
            return null;
        }
        return extras;
    }

    private void updateActivityManager(java.util.List<java.lang.String> targetPackageNames, int userId) {
        android.app.IActivityManager am = android.app.ActivityManager.getService();
        try {
            am.scheduleApplicationInfoChanged(targetPackageNames, userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "updateActivityManager remote exception", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.SparseArray<java.util.List<java.lang.String>> updatePackageManagerLocked(java.util.Set<android.content.pm.UserPackage> targets) {
        if (com.android.internal.util.CollectionUtils.isEmpty(targets)) {
            return new android.util.SparseArray<>();
        }
        android.util.SparseArray<java.util.List<java.lang.String>> affectedTargets = new android.util.SparseArray<>();
        android.util.SparseArray<android.util.ArraySet<java.lang.String>> userTargets = groupTargetsByUserId(targets);
        int n = userTargets.size();
        for (int i = 0; i < n; i++) {
            int userId = userTargets.keyAt(i);
            affectedTargets.put(userId, updatePackageManagerLocked(userTargets.valueAt(i), userId));
        }
        return affectedTargets;
    }

    private java.util.List<java.lang.String> updatePackageManagerLocked(java.util.Collection<java.lang.String> targetPackageNames, int userId) {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#updatePackageManagerLocked " + targetPackageNames);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Update package manager about changed overlays");
            }
            android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            boolean updateFrameworkRes = targetPackageNames.contains(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            if (updateFrameworkRes) {
                targetPackageNames = packageManagerInternal.getTargetPackageNames(userId);
            }
            android.util.ArrayMap<java.lang.String, android.content.pm.overlay.OverlayPaths> pendingChanges = new android.util.ArrayMap<>(targetPackageNames.size());
            synchronized (this.mLock) {
                android.content.pm.overlay.OverlayPaths frameworkOverlays = this.mImpl.getEnabledOverlayPaths(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, userId, false);
                for (java.lang.String targetPackageName : targetPackageNames) {
                    android.content.pm.overlay.OverlayPaths.Builder list = new android.content.pm.overlay.OverlayPaths.Builder(frameworkOverlays);
                    if (!com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(targetPackageName)) {
                        list.addAll(this.mImpl.getEnabledOverlayPaths(targetPackageName, userId, true));
                    }
                    pendingChanges.put(targetPackageName, list.build());
                }
            }
            java.util.HashSet<java.lang.String> updatedPackages = new java.util.HashSet<>();
            java.util.HashSet<java.lang.String> invalidPackages = new java.util.HashSet<>();
            packageManagerInternal.setEnabledOverlayPackages(userId, pendingChanges, updatedPackages, invalidPackages);
            if (DEBUG || !invalidPackages.isEmpty()) {
                for (java.lang.String targetPackageName2 : targetPackageNames) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "-> Updating overlay: target=" + targetPackageName2 + " overlays=[" + pendingChanges.get(targetPackageName2) + "] userId=" + userId);
                    }
                    if (invalidPackages.contains(targetPackageName2)) {
                        android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Failed to change enabled overlays for %s user %d", new java.lang.Object[]{targetPackageName2, java.lang.Integer.valueOf(userId)}));
                    }
                }
            }
            return new java.util.ArrayList(updatedPackages);
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }

    private void persistSettingsLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Writing overlay settings");
        }
        java.io.FileOutputStream stream = null;
        try {
            stream = this.mSettingsFile.startWrite();
            this.mSettings.persist(stream);
            this.mSettingsFile.finishWrite(stream);
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            this.mSettingsFile.failWrite(stream);
            android.util.Slog.e(TAG, "failed to persist overlay state", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreSettings() {
        try {
            android.os.Trace.traceBegin(67108864L, "OMS#restoreSettings");
            synchronized (this.mLock) {
                if (!this.mSettingsFile.getBaseFile().exists()) {
                    return;
                }
                try {
                    java.io.FileInputStream stream = this.mSettingsFile.openRead();
                    try {
                        this.mSettings.restore(stream);
                        java.util.List<android.content.pm.UserInfo> liveUsers = this.mUserManager.getUsers(true);
                        int[] liveUserIds = new int[liveUsers.size()];
                        for (int i = 0; i < liveUsers.size(); i++) {
                            liveUserIds[i] = liveUsers.get(i).getUserHandle().getIdentifier();
                        }
                        java.util.Arrays.sort(liveUserIds);
                        for (int userId : this.mSettings.getUsers()) {
                            if (java.util.Arrays.binarySearch(liveUserIds, userId) < 0) {
                                this.mSettings.removeUser(userId);
                            }
                        }
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (java.lang.Throwable th) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Slog.e(TAG, "failed to restore overlay state", e);
                }
            }
        } finally {
            android.os.Trace.traceEnd(67108864L);
        }
    }
}
