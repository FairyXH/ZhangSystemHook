package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class PermissionPolicyService extends com.android.server.SystemService {
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = com.android.server.policy.PermissionPolicyService.class.getSimpleName();
    private static final long NOTIFICATION_PERM_CHANGE_ID = 194833441;
    private static final java.lang.String SYSTEM_PKG = "android";
    private static final long USER_SENSITIVE_UPDATE_DELAY_MS = 60000;
    private java.util.List<java.lang.String> mAppOpPermissions;
    private com.android.internal.app.IAppOpsCallback mAppOpsCallback;
    private boolean mBootCompleted;
    private android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.util.SparseBooleanArray mIsStarted;
    private final android.util.SparseBooleanArray mIsUidResetScheduled;
    private final android.util.SparseBooleanArray mIsUidSyncScheduled;
    private final android.app.KeyguardManager mKeyguardManager;
    private final java.lang.Object mLock;
    private com.android.server.notification.NotificationManagerInternal mNotificationManager;
    private com.android.server.policy.PermissionPolicyInternal.OnInitializedCallback mOnInitializedCallback;
    private final android.content.pm.PackageManager mPackageManager;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManagerInternal;
    public com.android.server.pm.permission.IPermissionManagerServiceExt mPermissionManagerServiceExt;
    public com.android.server.policy.IPermissionPolicyServiceExt mPermissionPolicyServiceExt;
    private com.android.server.policy.IPermissionPolicyServiceWrapper mPermissionPolicyServiceWrapper;
    private final java.util.ArrayList<com.android.server.policy.PermissionPolicyService.PhoneCarrierPrivilegesCallback> mPhoneCarrierPrivilegesCallbacks;
    private final android.content.BroadcastReceiver mSimConfigBroadcastReceiver;
    private android.telephony.TelephonyManager mTelephonyManager;

    /* JADX WARN: Multi-variable type inference failed */
    public PermissionPolicyService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mBootCompleted = false;
        this.mIsStarted = new android.util.SparseBooleanArray();
        this.mIsUidSyncScheduled = new android.util.SparseBooleanArray();
        this.mIsUidResetScheduled = new android.util.SparseBooleanArray();
        this.mPermissionPolicyServiceExt = (com.android.server.policy.IPermissionPolicyServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.policy.IPermissionPolicyServiceExt.class).base(this).create();
        this.mPermissionManagerServiceExt = (com.android.server.pm.permission.IPermissionManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.permission.IPermissionManagerServiceExt.class).base(this).create();
        this.mPhoneCarrierPrivilegesCallbacks = new java.util.ArrayList<>();
        this.mSimConfigBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.policy.PermissionPolicyService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (!"android.telephony.action.MULTI_SIM_CONFIG_CHANGED".equals(intent.getAction())) {
                    return;
                }
                com.android.server.policy.PermissionPolicyService.this.unregisterCarrierPrivilegesCallback();
                com.android.server.policy.PermissionPolicyService.this.registerCarrierPrivilegesCallbacks();
            }
        };
        this.mPermissionPolicyServiceWrapper = new com.android.server.policy.PermissionPolicyService.PermissionPolicyServiceWrapper();
        this.mContext = context;
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        this.mPackageManager = context.getPackageManager();
        this.mKeyguardManager = (android.app.KeyguardManager) context.getSystemService(android.app.KeyguardManager.class);
        com.android.server.LocalServices.addService(com.android.server.policy.PermissionPolicyInternal.class, new com.android.server.policy.PermissionPolicyService.Internal());
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        byte b;
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mPermissionManagerInternal = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
        com.android.internal.app.IAppOpsService appOpsService = com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
        this.mPackageManagerInternal.getPackageList(new android.content.pm.PackageManagerInternal.PackageListObserver() { // from class: com.android.server.policy.PermissionPolicyService.1
            @Override // android.content.pm.PackageManagerInternal.PackageListObserver
            public void onPackageAdded(java.lang.String packageName, int appId) {
                int[] userIds = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
                for (int userId : userIds) {
                    if (com.android.server.policy.PermissionPolicyService.this.isStarted(userId)) {
                        int uid = android.os.UserHandle.getUid(userId, appId);
                        com.android.server.policy.PermissionPolicyService.this.synchronizeUidPermissionsAndAppOps(uid);
                    }
                }
            }

            @Override // android.content.pm.PackageManagerInternal.PackageListObserver
            public void onPackageChanged(java.lang.String packageName, int appId) {
                int[] userIds = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
                for (int userId : userIds) {
                    if (com.android.server.policy.PermissionPolicyService.this.isStarted(userId)) {
                        int uid = android.os.UserHandle.getUid(userId, appId);
                        com.android.server.policy.PermissionPolicyService.this.synchronizeUidPermissionsAndAppOps(uid);
                        com.android.server.policy.PermissionPolicyService.this.resetAppOpPermissionsIfNotRequestedForUid(uid);
                    }
                }
            }

            @Override // android.content.pm.PackageManagerInternal.PackageListObserver
            public void onPackageRemoved(java.lang.String packageName, int appId) {
                int[] userIds = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
                for (int userId : userIds) {
                    if (com.android.server.policy.PermissionPolicyService.this.isStarted(userId)) {
                        int uid = android.os.UserHandle.getUid(userId, appId);
                        com.android.server.policy.PermissionPolicyService.this.resetAppOpPermissionsIfNotRequestedForUid(uid);
                    }
                }
            }
        });
        this.mPackageManager.addOnPermissionsChangeListener(new android.content.pm.PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.policy.PermissionPolicyService$$ExternalSyntheticLambda0
            public final void onPermissionsChanged(int i) {
                this.f$0.synchronizeUidPermissionsAndAppOpsAsync(i);
            }
        });
        this.mAppOpsCallback = new com.android.internal.app.IAppOpsCallback.Stub() { // from class: com.android.server.policy.PermissionPolicyService.2
            public void opChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) {
                if (!java.util.Objects.equals(persistentDeviceId, "default:0")) {
                    return;
                }
                if (packageName != null) {
                    com.android.server.policy.PermissionPolicyService.this.synchronizeUidPermissionsAndAppOpsAsync(uid);
                }
                com.android.server.policy.PermissionPolicyService.this.resetAppOpPermissionsIfNotRequestedForUidAsync(uid);
            }
        };
        java.util.List<android.content.pm.PermissionInfo> dangerousPerms = this.mPermissionManagerInternal.getAllPermissionsWithProtection(1);
        try {
            int numDangerousPerms = dangerousPerms.size();
            for (int i = 0; i < numDangerousPerms; i++) {
                android.content.pm.PermissionInfo perm = dangerousPerms.get(i);
                if (perm.isRuntime()) {
                    appOpsService.startWatchingMode(getSwitchOp(perm.name), (java.lang.String) null, this.mAppOpsCallback);
                }
                if (perm.isSoftRestricted()) {
                    com.android.server.policy.SoftRestrictedPermissionPolicy policy = com.android.server.policy.SoftRestrictedPermissionPolicy.forPermission(null, null, null, null, perm.name);
                    int extraAppOp = policy.getExtraAppOpCode();
                    if (extraAppOp != -1) {
                        appOpsService.startWatchingMode(extraAppOp, (java.lang.String) null, this.mAppOpsCallback);
                    }
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.wtf(LOG_TAG, "Cannot set up app-ops listener");
        }
        java.util.List<android.content.pm.PermissionInfo> appOpPermissionInfos = this.mPermissionManagerInternal.getAllPermissionsWithProtectionFlags(64);
        this.mAppOpPermissions = new java.util.ArrayList();
        int appOpPermissionInfosSize = appOpPermissionInfos.size();
        for (int i2 = 0; i2 < appOpPermissionInfosSize; i2++) {
            android.content.pm.PermissionInfo appOpPermissionInfo = appOpPermissionInfos.get(i2);
            java.lang.String str = appOpPermissionInfo.name;
            switch (str.hashCode()) {
                case 309844284:
                    b = str.equals("android.permission.MANAGE_IPSEC_TUNNELS") ? (byte) 1 : (byte) -1;
                    break;
                case 1353874541:
                    b = str.equals("android.permission.ACCESS_NOTIFICATIONS") ? (byte) 0 : (byte) -1;
                    break;
                case 1777263169:
                    b = str.equals("android.permission.REQUEST_INSTALL_PACKAGES") ? (byte) 2 : (byte) -1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                case 1:
                case 2:
                    break;
                default:
                    int appOpCode = android.app.AppOpsManager.permissionToOpCode(appOpPermissionInfo.name);
                    if (appOpCode != -1) {
                        this.mAppOpPermissions.add(appOpPermissionInfo.name);
                        try {
                            appOpsService.startWatchingMode(appOpCode, (java.lang.String) null, this.mAppOpsCallback);
                        } catch (android.os.RemoteException e2) {
                            android.util.Slog.wtf(LOG_TAG, "Cannot set up app-ops listener", e2);
                        }
                    }
                    break;
            }
        }
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=ENTIRE_PKG_CHANGED");
        intentFilter.addDataScheme("package");
        getContext().registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.policy.PermissionPolicyService.3
            final java.util.List<java.lang.Integer> mUserSetupUids = new java.util.ArrayList(200);
            final java.util.Map<android.os.UserHandle, android.permission.PermissionControllerManager> mPermControllerManagers = new java.util.HashMap();

            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                boolean hasSetupRun = true;
                try {
                    android.content.ContentResolver cr = com.android.server.policy.PermissionPolicyService.this.getContext().getContentResolver();
                    hasSetupRun = android.provider.Settings.Secure.getIntForUser(cr, "user_setup_complete", cr.getUserId()) != 0;
                } catch (android.provider.Settings.SettingNotFoundException e3) {
                }
                int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                if (com.android.server.policy.PermissionPolicyService.this.mPackageManagerInternal.getPackage(uid) == null) {
                    return;
                }
                if (hasSetupRun) {
                    if (!this.mUserSetupUids.isEmpty()) {
                        synchronized (this.mUserSetupUids) {
                            for (int i3 = this.mUserSetupUids.size() - 1; i3 >= 0; i3--) {
                                updateUid(this.mUserSetupUids.get(i3).intValue());
                            }
                            this.mUserSetupUids.clear();
                        }
                    }
                    if (com.android.server.policy.PermissionPolicyService.this.mPermissionPolicyServiceExt.skipUpdateUserSensitiveForApp(uid, intent, com.android.server.policy.PermissionPolicyService.this.mHandler)) {
                        return;
                    }
                    updateUid(uid);
                    return;
                }
                synchronized (this.mUserSetupUids) {
                    if (!this.mUserSetupUids.contains(java.lang.Integer.valueOf(uid))) {
                        this.mUserSetupUids.add(java.lang.Integer.valueOf(uid));
                    }
                }
            }

            private void updateUid(int uid) {
                android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(uid);
                android.permission.PermissionControllerManager manager = this.mPermControllerManagers.get(user);
                if (manager == null) {
                    try {
                        manager = new android.permission.PermissionControllerManager(com.android.server.policy.PermissionPolicyService.getUserContext(com.android.server.policy.PermissionPolicyService.this.getContext(), user), com.android.server.PermissionThread.getHandler());
                        this.mPermControllerManagers.put(user, manager);
                    } catch (java.lang.IllegalArgumentException exception) {
                        android.util.Log.e(com.android.server.policy.PermissionPolicyService.LOG_TAG, "Could not create PermissionControllerManager for user" + user, exception);
                        return;
                    }
                }
                manager.updateUserSensitiveForApp(uid);
            }
        }, android.os.UserHandle.ALL, intentFilter, null, null);
        final android.permission.PermissionControllerManager manager = new android.permission.PermissionControllerManager(getUserContext(getContext(), android.os.Process.myUserHandle()), com.android.server.PermissionThread.getHandler());
        android.os.Handler handler = com.android.server.PermissionThread.getHandler();
        java.util.Objects.requireNonNull(manager);
        handler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.policy.PermissionPolicyService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                manager.updateUserSensitive();
            }
        }, 60000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getSwitchOp(java.lang.String permission) {
        int op = android.app.AppOpsManager.permissionToOpCode(permission);
        if (op == -1) {
            return -1;
        }
        return android.app.AppOpsManager.opToSwitch(op);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void synchronizeUidPermissionsAndAppOpsAsync(int uid) {
        if (this.mPermissionPolicyServiceExt.shouldDelayAppOpsSyncJob(uid) || this.mPermissionPolicyServiceExt.skipSynchronizeUidPermissionsAndAppOpsAsync(uid)) {
            return;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        if (isStarted(userId)) {
            synchronized (this.mLock) {
                if (!this.mIsUidSyncScheduled.get(uid)) {
                    com.android.server.FgThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.policy.PermissionPolicyService$$ExternalSyntheticLambda5
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((com.android.server.policy.PermissionPolicyService) obj).synchronizeUidPermissionsAndAppOps(((java.lang.Integer) obj2).intValue());
                        }
                    }, this, java.lang.Integer.valueOf(uid)));
                    this.mIsUidSyncScheduled.put(uid, true);
                }
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 520) {
            registerCarrierPrivilegesCallbacks();
            android.content.IntentFilter filter = new android.content.IntentFilter("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
            this.mContext.registerReceiver(this.mSimConfigBroadcastReceiver, filter);
        }
        if (phase == 550) {
            com.android.server.pm.UserManagerInternal um = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            for (int userId : um.getUserIds()) {
                if (um.isUserRunning(userId)) {
                    onStartUser(userId);
                }
            }
        }
        if (phase == 550) {
            ((com.android.server.policy.PermissionPolicyService.Internal) com.android.server.LocalServices.getService(com.android.server.policy.PermissionPolicyInternal.class)).onActivityManagerReady();
        }
        if (phase == 1000) {
            synchronized (this.mLock) {
                this.mBootCompleted = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initTelephonyManagerIfNeeded() {
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = android.telephony.TelephonyManager.from(this.mContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerCarrierPrivilegesCallbacks() {
        initTelephonyManagerIfNeeded();
        if (this.mTelephonyManager == null) {
            return;
        }
        int numPhones = this.mTelephonyManager.getActiveModemCount();
        for (int i = 0; i < numPhones; i++) {
            com.android.server.policy.PermissionPolicyService.PhoneCarrierPrivilegesCallback callback = new com.android.server.policy.PermissionPolicyService.PhoneCarrierPrivilegesCallback(i);
            this.mPhoneCarrierPrivilegesCallbacks.add(callback);
            this.mTelephonyManager.registerCarrierPrivilegesCallback(i, this.mContext.getMainExecutor(), callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterCarrierPrivilegesCallback() {
        initTelephonyManagerIfNeeded();
        if (this.mTelephonyManager == null) {
            return;
        }
        for (int i = 0; i < this.mPhoneCarrierPrivilegesCallbacks.size(); i++) {
            com.android.server.policy.PermissionPolicyService.PhoneCarrierPrivilegesCallback callback = this.mPhoneCarrierPrivilegesCallbacks.get(i);
            if (callback != null) {
                this.mTelephonyManager.unregisterCarrierPrivilegesCallback(callback);
            }
        }
        this.mPhoneCarrierPrivilegesCallbacks.clear();
    }

    private final class PhoneCarrierPrivilegesCallback implements android.telephony.TelephonyManager.CarrierPrivilegesCallback {
        private int mPhoneId;

        PhoneCarrierPrivilegesCallback(int phoneId) {
            this.mPhoneId = phoneId;
        }

        public void onCarrierPrivilegesChanged(java.util.Set<java.lang.String> privilegedPackageNames, java.util.Set<java.lang.Integer> privilegedUids) {
            com.android.server.policy.PermissionPolicyService.this.initTelephonyManagerIfNeeded();
            if (com.android.server.policy.PermissionPolicyService.this.mTelephonyManager == null) {
                android.util.Log.e(com.android.server.policy.PermissionPolicyService.LOG_TAG, "Cannot grant default permissions to Carrier Service app. TelephonyManager is null");
                return;
            }
            java.lang.String servicePkg = com.android.server.policy.PermissionPolicyService.this.mTelephonyManager.getCarrierServicePackageNameForLogicalSlot(this.mPhoneId);
            if (servicePkg == null) {
                return;
            }
            int[] users = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
            android.permission.LegacyPermissionManager legacyPermManager = (android.permission.LegacyPermissionManager) com.android.server.policy.PermissionPolicyService.this.mContext.getSystemService(android.permission.LegacyPermissionManager.class);
            for (int i = 0; i < users.length; i++) {
                try {
                    com.android.server.policy.PermissionPolicyService.this.mPackageManager.getPackageInfoAsUser(servicePkg, 0, users[i]);
                    legacyPermManager.grantDefaultPermissionsToCarrierServiceApp(servicePkg, users[i]);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStarted(int userId) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsStarted.get(userId);
        }
        return z;
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        onStartUser(user.getUserIdentifier());
    }

    private void onStartUser(int userId) {
        com.android.server.policy.PermissionPolicyInternal.OnInitializedCallback callback;
        if (isStarted(userId)) {
            return;
        }
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("Permission_grant_default_permissions-" + userId);
        if (this.mPackageManagerInternal.isPermissionUpgradeNeeded(userId)) {
            grantOrUpgradeDefaultRuntimePermissions(userId);
            updateUserSensitive(userId);
            this.mPackageManagerInternal.updateRuntimePermissionsFingerprint(userId);
        }
        t.traceEnd();
        synchronized (this.mLock) {
            this.mIsStarted.put(userId, true);
            callback = this.mOnInitializedCallback;
        }
        t.traceBegin("Permission_synchronize_permissions-" + userId);
        if (this.mPermissionPolicyServiceExt.shouldSynchronizePermissionsAndAppOpsForUser(userId)) {
            synchronizePermissionsAndAppOpsForUser(userId);
        }
        t.traceEnd();
        if (callback != null) {
            t.traceBegin("Permission_onInitialized-" + userId);
            if (!this.mPermissionPolicyServiceExt.skipRunOnInitialized(userId)) {
                callback.onInitialized(userId);
            }
            t.traceEnd();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            this.mIsStarted.delete(user.getUserIdentifier());
        }
    }

    private void grantOrUpgradeDefaultRuntimePermissions(final int userId) {
        if (android.permission.PermissionManager.USE_ACCESS_CHECKING_SERVICE) {
            return;
        }
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        final com.android.internal.infra.AndroidFuture<java.lang.Boolean> future = new com.android.internal.infra.AndroidFuture<>();
        android.permission.PermissionControllerManager permissionControllerManager = new android.permission.PermissionControllerManager(getUserContext(getContext(), android.os.UserHandle.of(userId)), com.android.server.PermissionThread.getHandler());
        permissionControllerManager.grantOrUpgradeDefaultRuntimePermissions(com.android.server.PermissionThread.getExecutor(), new java.util.function.Consumer() { // from class: com.android.server.policy.PermissionPolicyService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.policy.PermissionPolicyService.lambda$grantOrUpgradeDefaultRuntimePermissions$0(future, userId, (java.lang.Boolean) obj);
            }
        });
        try {
            try {
                t.traceBegin("Permission_callback_waiting-" + userId);
                future.get();
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {
                throw new java.lang.IllegalStateException(e);
            }
        } finally {
            t.traceEnd();
        }
    }

    static /* synthetic */ void lambda$grantOrUpgradeDefaultRuntimePermissions$0(com.android.internal.infra.AndroidFuture future, int userId, java.lang.Boolean successful) {
        if (successful.booleanValue()) {
            future.complete((java.lang.Object) null);
            return;
        }
        java.lang.String message = "Error granting/upgrading runtime permissions for user " + userId;
        android.util.Slog.wtf(LOG_TAG, message);
        future.completeExceptionally(new java.lang.IllegalStateException(message));
    }

    private void updateUserSensitive(int userId) {
        android.permission.PermissionControllerManager permissionControllerManager = new android.permission.PermissionControllerManager(getUserContext(getContext(), android.os.UserHandle.of(userId)), com.android.server.PermissionThread.getHandler());
        permissionControllerManager.updateUserSensitive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.content.Context getUserContext(android.content.Context context, android.os.UserHandle user) {
        if (context.getUser().equals(user)) {
            return context;
        }
        try {
            return context.createPackageContextAsUser(context.getPackageName(), 0, user);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(LOG_TAG, "Cannot create context for user " + user, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void synchronizeUidPermissionsAndAppOps(int uid) {
        synchronized (this.mLock) {
            this.mIsUidSyncScheduled.delete(uid);
        }
        android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(uid);
        com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser synchroniser = new com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser(getUserContext(getContext(), user));
        int appId = android.os.UserHandle.getAppId(uid);
        java.util.List<com.android.server.pm.pkg.AndroidPackage> pkgs = this.mPackageManagerInternal.getPackagesForAppId(appId);
        int pkgsSize = pkgs.size();
        for (int i = 0; i < pkgsSize; i++) {
            com.android.server.pm.pkg.AndroidPackage pkg = pkgs.get(i);
            synchroniser.addPackage(pkg.getPackageName());
        }
        synchroniser.syncPackages();
    }

    private void synchronizePermissionsAndAppOpsForUser(int userId) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        final com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser synchronizer = new com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser(getUserContext(getContext(), android.os.UserHandle.of(userId)));
        t.traceBegin("Permission_synchronize_addPackages-" + userId);
        this.mPackageManagerInternal.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.policy.PermissionPolicyService$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                synchronizer.addPackage(((com.android.server.pm.pkg.AndroidPackage) obj).getPackageName());
            }
        });
        t.traceEnd();
        t.traceBegin("Permission_syncPackages-" + userId);
        synchronizer.syncPackages();
        t.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetAppOpPermissionsIfNotRequestedForUidAsync(int uid) {
        if (isStarted(android.os.UserHandle.getUserId(uid))) {
            synchronized (this.mLock) {
                if (!this.mIsUidResetScheduled.get(uid)) {
                    this.mIsUidResetScheduled.put(uid, true);
                    com.android.server.PermissionThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.policy.PermissionPolicyService$$ExternalSyntheticLambda3
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((com.android.server.policy.PermissionPolicyService) obj).resetAppOpPermissionsIfNotRequestedForUid(((java.lang.Integer) obj2).intValue());
                        }
                    }, this, java.lang.Integer.valueOf(uid)));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetAppOpPermissionsIfNotRequestedForUid(int uid) {
        android.app.AppOpsManager appOpsManager;
        android.app.AppOpsManager appOpsManager2;
        int i;
        int i2;
        int defaultAppOpMode;
        int appOpCode;
        java.lang.String appOpPermission;
        int i3;
        synchronized (this.mLock) {
            this.mIsUidResetScheduled.delete(uid);
        }
        android.content.Context context = getContext();
        android.content.pm.PackageManager userPackageManager = getUserContext(context, android.os.UserHandle.getUserHandleForUid(uid)).getPackageManager();
        java.lang.String[] packageNames = userPackageManager.getPackagesForUid(uid);
        if (packageNames != null && packageNames.length != 0) {
            android.util.ArraySet<java.lang.String> requestedPermissions = new android.util.ArraySet<>();
            for (java.lang.String str : packageNames) {
                try {
                    android.content.pm.PackageInfo packageInfo = userPackageManager.getPackageInfo(str, 4096);
                    if (packageInfo != null && packageInfo.requestedPermissions != null) {
                        java.util.Collections.addAll(requestedPermissions, packageInfo.requestedPermissions);
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
            }
            android.app.AppOpsManager appOpsManager3 = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
            android.app.AppOpsManagerInternal appOpsManagerInternal = (android.app.AppOpsManagerInternal) com.android.server.LocalServices.getService(android.app.AppOpsManagerInternal.class);
            int appOpPermissionsSize = this.mAppOpPermissions.size();
            int i4 = 0;
            while (i4 < appOpPermissionsSize) {
                java.lang.String appOpPermission2 = this.mAppOpPermissions.get(i4);
                if (requestedPermissions.contains(appOpPermission2)) {
                    appOpsManager = appOpsManager3;
                } else {
                    int appOpCode2 = android.app.AppOpsManager.permissionToOpCode(appOpPermission2);
                    int defaultAppOpMode2 = android.app.AppOpsManager.opToDefaultMode(appOpCode2);
                    int length = packageNames.length;
                    int i5 = 0;
                    while (i5 < length) {
                        java.lang.String packageName = packageNames[i5];
                        android.content.Context context2 = context;
                        int appOpMode = appOpsManager3.unsafeCheckOpRawNoThrow(appOpCode2, uid, packageName);
                        if (appOpMode == defaultAppOpMode2) {
                            appOpsManager2 = appOpsManager3;
                            i = i5;
                            i2 = length;
                            defaultAppOpMode = defaultAppOpMode2;
                            appOpCode = appOpCode2;
                            appOpPermission = appOpPermission2;
                            i3 = i4;
                        } else {
                            appOpsManager2 = appOpsManager3;
                            appOpsManagerInternal.setUidModeFromPermissionPolicy(appOpCode2, uid, defaultAppOpMode2, this.mAppOpsCallback);
                            i = i5;
                            i2 = length;
                            defaultAppOpMode = defaultAppOpMode2;
                            appOpCode = appOpCode2;
                            appOpPermission = appOpPermission2;
                            i3 = i4;
                            appOpsManagerInternal.setModeFromPermissionPolicy(appOpCode2, uid, packageName, defaultAppOpMode, this.mAppOpsCallback);
                        }
                        i5 = i + 1;
                        context = context2;
                        appOpsManager3 = appOpsManager2;
                        length = i2;
                        defaultAppOpMode2 = defaultAppOpMode;
                        appOpCode2 = appOpCode;
                        appOpPermission2 = appOpPermission;
                        i4 = i3;
                    }
                    appOpsManager = appOpsManager3;
                }
                i4++;
                context = context;
                appOpsManager3 = appOpsManager;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PermissionToOpSynchroniser {
        private final android.app.AppOpsManager mAppOpsManager;
        private final android.content.Context mContext;
        private final android.content.pm.PackageManager mPackageManager;
        private final java.util.ArrayList<com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange> mOpsToAllow = new java.util.ArrayList<>();
        private final java.util.ArrayList<com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange> mOpsToIgnore = new java.util.ArrayList<>();
        private final java.util.ArrayList<com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange> mOpsToIgnoreIfNotAllowed = new java.util.ArrayList<>();
        private final java.util.ArrayList<com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange> mOpsToForeground = new java.util.ArrayList<>();
        private final android.app.AppOpsManagerInternal mAppOpsManagerInternal = (android.app.AppOpsManagerInternal) com.android.server.LocalServices.getService(android.app.AppOpsManagerInternal.class);
        private final android.util.ArrayMap<java.lang.String, android.content.pm.PermissionInfo> mRuntimeAndTheirBgPermissionInfos = new android.util.ArrayMap<>();

        PermissionToOpSynchroniser(android.content.Context context) {
            this.mContext = context;
            this.mPackageManager = context.getPackageManager();
            this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
            com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerInternal = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
            java.util.List<android.content.pm.PermissionInfo> permissionInfos = permissionManagerInternal.getAllPermissionsWithProtection(1);
            int permissionInfosSize = permissionInfos.size();
            for (int i = 0; i < permissionInfosSize; i++) {
                android.content.pm.PermissionInfo permissionInfo = permissionInfos.get(i);
                this.mRuntimeAndTheirBgPermissionInfos.put(permissionInfo.name, permissionInfo);
                if (permissionInfo.backgroundPermission != null) {
                    java.lang.String backgroundNonRuntimePermission = permissionInfo.backgroundPermission;
                    int j = 0;
                    while (true) {
                        if (j >= permissionInfosSize) {
                            break;
                        }
                        android.content.pm.PermissionInfo bgPermissionCandidate = permissionInfos.get(j);
                        if (!permissionInfo.backgroundPermission.equals(bgPermissionCandidate.name)) {
                            j++;
                        } else {
                            backgroundNonRuntimePermission = null;
                            break;
                        }
                    }
                    if (backgroundNonRuntimePermission != null) {
                        try {
                            android.content.pm.PermissionInfo backgroundPermissionInfo = this.mPackageManager.getPermissionInfo(backgroundNonRuntimePermission, 0);
                            this.mRuntimeAndTheirBgPermissionInfos.put(backgroundPermissionInfo.name, backgroundPermissionInfo);
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            android.util.Slog.w(com.android.server.policy.PermissionPolicyService.LOG_TAG, "Unknown background permission: " + backgroundNonRuntimePermission);
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void syncPackages() {
            android.util.LongSparseLongArray alreadySetAppOps = new android.util.LongSparseLongArray();
            int allowCount = this.mOpsToAllow.size();
            for (int i = 0; i < allowCount; i++) {
                com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange op = this.mOpsToAllow.get(i);
                setUidModeAllowed(op.code, op.uid, op.packageName);
                alreadySetAppOps.put(com.android.internal.util.IntPair.of(op.uid, op.code), 1L);
            }
            int foregroundCount = this.mOpsToForeground.size();
            for (int i2 = 0; i2 < foregroundCount; i2++) {
                com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange op2 = this.mOpsToForeground.get(i2);
                if (alreadySetAppOps.indexOfKey(com.android.internal.util.IntPair.of(op2.uid, op2.code)) < 0) {
                    setUidModeForeground(op2.code, op2.uid, op2.packageName);
                    alreadySetAppOps.put(com.android.internal.util.IntPair.of(op2.uid, op2.code), 1L);
                }
            }
            int ignoreCount = this.mOpsToIgnore.size();
            for (int i3 = 0; i3 < ignoreCount; i3++) {
                com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange op3 = this.mOpsToIgnore.get(i3);
                if (alreadySetAppOps.indexOfKey(com.android.internal.util.IntPair.of(op3.uid, op3.code)) < 0) {
                    setUidModeIgnored(op3.code, op3.uid, op3.packageName);
                    alreadySetAppOps.put(com.android.internal.util.IntPair.of(op3.uid, op3.code), 1L);
                }
            }
            int ignoreIfNotAllowedCount = this.mOpsToIgnoreIfNotAllowed.size();
            for (int i4 = 0; i4 < ignoreIfNotAllowedCount; i4++) {
                com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange op4 = this.mOpsToIgnoreIfNotAllowed.get(i4);
                if (alreadySetAppOps.indexOfKey(com.android.internal.util.IntPair.of(op4.uid, op4.code)) < 0) {
                    boolean wasSet = setUidModeIgnoredIfNotAllowed(op4.code, op4.uid, op4.packageName);
                    if (wasSet) {
                        alreadySetAppOps.put(com.android.internal.util.IntPair.of(op4.uid, op4.code), 1L);
                    }
                }
            }
        }

        private void addAppOps(android.content.pm.PackageInfo packageInfo, com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String permissionName) {
            android.content.pm.PermissionInfo permissionInfo = this.mRuntimeAndTheirBgPermissionInfos.get(permissionName);
            if (permissionInfo == null) {
                return;
            }
            addPermissionAppOp(packageInfo, pkg, permissionInfo);
            addExtraAppOp(packageInfo, pkg, permissionInfo);
        }

        private void addPermissionAppOp(android.content.pm.PackageInfo packageInfo, com.android.server.pm.pkg.AndroidPackage pkg, android.content.pm.PermissionInfo permissionInfo) {
            int appOpMode;
            if (!permissionInfo.isRuntime()) {
            }
            java.lang.String permissionName = permissionInfo.name;
            java.lang.String packageName = packageInfo.packageName;
            android.os.UserHandle.getUserHandleForUid(packageInfo.applicationInfo.uid);
            int permissionFlags = this.mPackageManager.getPermissionFlags(permissionName, packageName, this.mContext.getUser());
            boolean isReviewRequired = (permissionFlags & 64) != 0;
            if (isReviewRequired) {
                return;
            }
            int appOpCode = com.android.server.policy.PermissionPolicyService.getSwitchOp(permissionName);
            if (appOpCode == -1 && (appOpCode = com.android.server.policy.PermissionPolicyService.this.mPermissionPolicyServiceExt.getSwitchOp(permissionName)) == -1) {
                return;
            }
            boolean shouldGrantAppOp = shouldGrantAppOp(packageInfo, pkg, permissionInfo);
            if (shouldGrantAppOp) {
                if (permissionInfo.backgroundPermission != null) {
                    android.content.pm.PermissionInfo backgroundPermissionInfo = this.mRuntimeAndTheirBgPermissionInfos.get(permissionInfo.backgroundPermission);
                    boolean shouldGrantBackgroundAppOp = backgroundPermissionInfo != null && shouldGrantAppOp(packageInfo, pkg, backgroundPermissionInfo);
                    appOpMode = shouldGrantBackgroundAppOp ? 0 : 4;
                } else {
                    appOpMode = 0;
                }
            } else {
                appOpMode = 1;
            }
            int uid = packageInfo.applicationInfo.uid;
            com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange opToChange = new com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange(uid, packageName, appOpCode);
            switch (appOpMode) {
                case 0:
                    this.mOpsToAllow.add(opToChange);
                    break;
                case 1:
                    this.mOpsToIgnore.add(opToChange);
                    break;
                case 4:
                    this.mOpsToForeground.add(opToChange);
                    break;
            }
        }

        private boolean shouldGrantAppOp(android.content.pm.PackageInfo packageInfo, com.android.server.pm.pkg.AndroidPackage pkg, android.content.pm.PermissionInfo permissionInfo) {
            java.lang.String permissionName = permissionInfo.name;
            java.lang.String packageName = packageInfo.packageName;
            boolean isGranted = this.mPackageManager.checkPermission(permissionName, packageName) == 0;
            if (!isGranted) {
                return false;
            }
            int permissionFlags = this.mPackageManager.getPermissionFlags(permissionName, packageName, this.mContext.getUser());
            boolean isRevokedCompat = (permissionFlags & 8) == 8;
            if (isRevokedCompat) {
                return false;
            }
            if (permissionInfo.isHardRestricted()) {
                boolean shouldApplyRestriction = (permissionFlags & 16384) == 16384;
                return !shouldApplyRestriction;
            }
            if (!permissionInfo.isSoftRestricted()) {
                return true;
            }
            com.android.server.policy.SoftRestrictedPermissionPolicy policy = com.android.server.policy.SoftRestrictedPermissionPolicy.forPermission(this.mContext, packageInfo.applicationInfo, pkg, this.mContext.getUser(), permissionName);
            return policy.mayGrantPermission();
        }

        private void addExtraAppOp(android.content.pm.PackageInfo packageInfo, com.android.server.pm.pkg.AndroidPackage pkg, android.content.pm.PermissionInfo permissionInfo) {
            if (!permissionInfo.isSoftRestricted()) {
                return;
            }
            java.lang.String permissionName = permissionInfo.name;
            com.android.server.policy.SoftRestrictedPermissionPolicy policy = com.android.server.policy.SoftRestrictedPermissionPolicy.forPermission(this.mContext, packageInfo.applicationInfo, pkg, this.mContext.getUser(), permissionName);
            int extraOpCode = policy.getExtraAppOpCode();
            if (extraOpCode == -1) {
                return;
            }
            int uid = packageInfo.applicationInfo.uid;
            java.lang.String packageName = packageInfo.packageName;
            com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange extraOpToChange = new com.android.server.policy.PermissionPolicyService.PermissionToOpSynchroniser.OpToChange(uid, packageName, extraOpCode);
            if (policy.mayAllowExtraAppOp()) {
                this.mOpsToAllow.add(extraOpToChange);
            } else if (policy.mayDenyExtraAppOpIfGranted()) {
                this.mOpsToIgnore.add(extraOpToChange);
            } else {
                this.mOpsToIgnoreIfNotAllowed.add(extraOpToChange);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void addPackage(java.lang.String pkgName) {
            int uid;
            try {
                android.content.pm.PackageInfo pkgInfo = this.mPackageManager.getPackageInfo(pkgName, 4096);
                com.android.server.pm.pkg.AndroidPackage pkg = com.android.server.policy.PermissionPolicyService.this.mPackageManagerInternal.getPackage(pkgName);
                if (pkgInfo == null || pkg == null || pkgInfo.applicationInfo == null || pkgInfo.requestedPermissions == null || (uid = pkgInfo.applicationInfo.uid) == 0 || uid == 1000) {
                    return;
                }
                for (java.lang.String permission : pkgInfo.requestedPermissions) {
                    addAppOps(pkgInfo, pkg, permission);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }

        private void setUidModeAllowed(int opCode, int uid, java.lang.String packageName) {
            setUidMode(opCode, uid, 0, packageName);
        }

        private void setUidModeForeground(int opCode, int uid, java.lang.String packageName) {
            setUidMode(opCode, uid, 4, packageName);
        }

        private void setUidModeIgnored(int opCode, int uid, java.lang.String packageName) {
            setUidMode(opCode, uid, 1, packageName);
        }

        private boolean setUidModeIgnoredIfNotAllowed(int opCode, int uid, java.lang.String packageName) {
            int currentMode = this.mAppOpsManager.unsafeCheckOpRaw(android.app.AppOpsManager.opToPublicName(opCode), uid, packageName);
            if (currentMode != 0) {
                if (currentMode != 1) {
                    this.mAppOpsManagerInternal.setUidModeFromPermissionPolicy(opCode, uid, 1, com.android.server.policy.PermissionPolicyService.this.mAppOpsCallback);
                }
                return true;
            }
            return false;
        }

        private void setUidMode(int opCode, int uid, int mode, java.lang.String packageName) {
            int oldMode = this.mAppOpsManager.unsafeCheckOpRaw(android.app.AppOpsManager.opToPublicName(opCode), uid, packageName);
            if (oldMode != mode) {
                this.mAppOpsManagerInternal.setUidModeFromPermissionPolicy(opCode, uid, mode, com.android.server.policy.PermissionPolicyService.this.mAppOpsCallback);
                int newMode = this.mAppOpsManager.unsafeCheckOpRaw(android.app.AppOpsManager.opToPublicName(opCode), uid, packageName);
                if (newMode != mode) {
                    this.mAppOpsManagerInternal.setModeFromPermissionPolicy(opCode, uid, packageName, android.app.AppOpsManager.opToDefaultMode(opCode), com.android.server.policy.PermissionPolicyService.this.mAppOpsCallback);
                }
            }
        }

        private class OpToChange {
            final int code;
            final java.lang.String packageName;
            final int uid;

            OpToChange(int uid, java.lang.String packageName, int code) {
                this.uid = uid;
                this.packageName = packageName;
                this.code = code;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Internal extends com.android.server.policy.PermissionPolicyInternal {
        private final com.android.server.wm.ActivityInterceptorCallback mActivityInterceptorCallback;

        private Internal() {
            this.mActivityInterceptorCallback = new com.android.server.policy.PermissionPolicyService.Internal.AnonymousClass1();
        }

        /* JADX INFO: renamed from: com.android.server.policy.PermissionPolicyService$Internal$1, reason: invalid class name */
        class AnonymousClass1 implements com.android.server.wm.ActivityInterceptorCallback {
            AnonymousClass1() {
            }

            @Override // com.android.server.wm.ActivityInterceptorCallback
            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptResult onInterceptActivityLaunch(com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
                return null;
            }

            @Override // com.android.server.wm.ActivityInterceptorCallback
            public void onActivityLaunched(final android.app.TaskInfo taskInfo, final android.content.pm.ActivityInfo activityInfo, final com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
                if (!com.android.server.policy.PermissionPolicyService.Internal.this.shouldShowNotificationDialogOrClearFlags(taskInfo, activityInfo.packageName, info.getCallingPackage(), info.getIntent(), info.getCheckedOptions(), activityInfo.name, true) || com.android.server.policy.PermissionPolicyService.Internal.this.isNoDisplayActivity(activityInfo)) {
                    return;
                }
                android.os.UserHandle user = android.os.UserHandle.of(taskInfo.userId);
                if (!android.app.compat.CompatChanges.isChangeEnabled(com.android.server.policy.PermissionPolicyService.NOTIFICATION_PERM_CHANGE_ID, activityInfo.packageName, user)) {
                    com.android.server.policy.PermissionPolicyService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.PermissionPolicyService$Internal$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onActivityLaunched$0(activityInfo, taskInfo, info);
                        }
                    });
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onActivityLaunched$0(android.content.pm.ActivityInfo activityInfo, android.app.TaskInfo taskInfo, com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
                com.android.server.policy.PermissionPolicyService.Internal.this.showNotificationPromptIfNeeded(activityInfo.packageName, taskInfo.userId, taskInfo.taskId, info);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onActivityManagerReady() {
            com.android.server.wm.ActivityTaskManagerInternal atm = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
            atm.registerActivityStartInterceptor(1, this.mActivityInterceptorCallback);
        }

        @Override // com.android.server.policy.PermissionPolicyInternal
        public boolean checkStartActivity(android.content.Intent intent, int callingUid, java.lang.String callingPackage) {
            if (callingPackage != null && isActionRemovedForCallingPackage(intent, callingUid, callingPackage)) {
                android.util.Slog.w(com.android.server.policy.PermissionPolicyService.LOG_TAG, "Action Removed: starting " + intent.toString() + " from " + callingPackage + " (uid=" + callingUid + ")");
                return false;
            }
            if ("android.content.pm.action.REQUEST_PERMISSIONS_FOR_OTHER".equals(intent.getAction())) {
                if (callingUid != 1000 || !"android".equals(callingPackage)) {
                    return false;
                }
                return true;
            }
            return true;
        }

        @Override // com.android.server.policy.PermissionPolicyInternal
        public void showNotificationPromptIfNeeded(java.lang.String packageName, int userId, int taskId) {
            showNotificationPromptIfNeeded(packageName, userId, taskId, null);
        }

        void showNotificationPromptIfNeeded(java.lang.String packageName, int userId, int taskId, com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
            android.os.UserHandle user = android.os.UserHandle.of(userId);
            if (packageName == null || taskId == -1 || !shouldForceShowNotificationPermissionRequest(packageName, user)) {
                return;
            }
            launchNotificationPermissionRequestDialog(packageName, user, taskId, info);
        }

        @Override // com.android.server.policy.PermissionPolicyInternal
        public boolean isIntentToPermissionDialog(android.content.Intent intent) {
            return java.util.Objects.equals(intent.getPackage(), com.android.server.policy.PermissionPolicyService.this.mPackageManager.getPermissionControllerPackageName()) && (java.util.Objects.equals(intent.getAction(), "android.content.pm.action.REQUEST_PERMISSIONS_FOR_OTHER") || java.util.Objects.equals(intent.getAction(), "android.content.pm.action.REQUEST_PERMISSIONS"));
        }

        @Override // com.android.server.policy.PermissionPolicyInternal
        public boolean shouldShowNotificationDialogForTask(android.app.TaskInfo taskInfo, java.lang.String currPkg, java.lang.String callingPkg, android.content.Intent intent, java.lang.String activityName) {
            return shouldShowNotificationDialogOrClearFlags(taskInfo, currPkg, callingPkg, intent, null, activityName, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isNoDisplayActivity(android.content.pm.ActivityInfo aInfo) {
            com.android.internal.policy.AttributeCache.Entry ent;
            int themeResource = aInfo.getThemeResource();
            if (themeResource != 0 && (ent = com.android.internal.policy.AttributeCache.instance().get(aInfo.packageName, themeResource, com.android.internal.R.styleable.Window, 0)) != null) {
                boolean noDisplay = ent.array.getBoolean(10, false);
                return noDisplay;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean shouldShowNotificationDialogOrClearFlags(android.app.TaskInfo taskInfo, java.lang.String currPkg, java.lang.String callingPkg, android.content.Intent intent, android.app.ActivityOptions options, java.lang.String topActivityName, boolean startedActivity) {
            boolean isStartedFromSecondaryHome;
            if (intent == null || currPkg == null || taskInfo == null || topActivityName == null || !((taskInfo.isFocused && taskInfo.isVisible && taskInfo.isRunning) || startedActivity)) {
                return false;
            }
            if (taskInfo.topActivityInfo == null) {
                isStartedFromSecondaryHome = false;
            } else {
                boolean isStartedFromSecondaryHome2 = ((taskInfo.topActivityInfo.privateFlags & 1024) != 0) & startedActivity;
                if (isStartedFromSecondaryHome2) {
                    android.util.Log.i(com.android.server.policy.PermissionPolicyService.LOG_TAG, "isStartedFromSecondaryHome: true");
                }
                isStartedFromSecondaryHome = isStartedFromSecondaryHome2;
            }
            boolean isStartedFromSecondaryHome3 = isLauncherIntent(intent);
            if (!isStartedFromSecondaryHome3 && !isStartedFromSecondaryHome && ((options == null || !options.isEligibleForLegacyPermissionPrompt()) && !isTaskStartedFromLauncher(currPkg, taskInfo))) {
                if (!isTaskPotentialTrampoline(topActivityName, currPkg, callingPkg, taskInfo, intent)) {
                    return false;
                }
                if (startedActivity && !pkgHasRunningLauncherTask(currPkg, taskInfo)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isTaskPotentialTrampoline(java.lang.String activityName, java.lang.String currPkg, java.lang.String callingPkg, android.app.TaskInfo taskInfo, android.content.Intent intent) {
            return currPkg.equals(callingPkg) && taskInfo.baseIntent.filterEquals(intent) && taskInfo.numActivities == 1 && taskInfo.topActivityInfo != null && activityName.equals(taskInfo.topActivityInfo.name);
        }

        private boolean pkgHasRunningLauncherTask(java.lang.String currPkg, android.app.TaskInfo taskInfo) {
            com.android.server.wm.ActivityTaskManagerInternal m = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
            try {
                java.util.List<android.app.ActivityManager.AppTask> tasks = m.getAppTasks(currPkg, com.android.server.policy.PermissionPolicyService.this.mPackageManager.getPackageUid(currPkg, 0));
                for (int i = 0; i < tasks.size(); i++) {
                    android.app.TaskInfo other = tasks.get(i).getTaskInfo();
                    if (other.taskId != taskInfo.taskId && other.isFocused && other.isRunning && isTaskStartedFromLauncher(currPkg, other)) {
                        return true;
                    }
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
            return false;
        }

        private boolean isLauncherIntent(android.content.Intent intent) {
            return "android.intent.action.MAIN".equals(intent.getAction()) && intent.getCategories() != null && (intent.getCategories().contains("android.intent.category.LAUNCHER") || intent.getCategories().contains("android.intent.category.LEANBACK_LAUNCHER") || intent.getCategories().contains("android.intent.category.CAR_LAUNCHER"));
        }

        private boolean isTaskStartedFromLauncher(java.lang.String currPkg, android.app.TaskInfo taskInfo) {
            return taskInfo.baseActivity != null && currPkg.equals(taskInfo.baseActivity.getPackageName()) && isLauncherIntent(taskInfo.baseIntent);
        }

        private void launchNotificationPermissionRequestDialog(java.lang.String pkgName, android.os.UserHandle user, int taskId, com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
            android.app.ActivityOptions options;
            android.content.Intent grantPermission = com.android.server.policy.PermissionPolicyService.this.mPackageManager.buildRequestPermissionsIntent(new java.lang.String[]{"android.permission.POST_NOTIFICATIONS"});
            grantPermission.addFlags(268697600);
            grantPermission.setAction("android.content.pm.action.REQUEST_PERMISSIONS_FOR_OTHER");
            grantPermission.putExtra("android.intent.extra.PACKAGE_NAME", pkgName);
            boolean remoteAnimation = (info == null || info.getCheckedOptions() == null || info.getCheckedOptions().getAnimationType() != 13 || info.getClearOptionsAnimationRunnable() == null) ? false : true;
            if (remoteAnimation) {
                options = android.app.ActivityOptions.makeRemoteAnimation(info.getCheckedOptions().getRemoteAnimationAdapter(), info.getCheckedOptions().getRemoteTransition());
            } else {
                options = new android.app.ActivityOptions(new android.os.Bundle());
            }
            options.setTaskOverlay(true, false);
            options.setLaunchTaskId(taskId);
            if (remoteAnimation) {
                info.getClearOptionsAnimationRunnable().run();
            }
            try {
                com.android.server.policy.PermissionPolicyService.this.mContext.startActivityAsUser(grantPermission, options.toBundle(), user);
            } catch (java.lang.Exception e) {
                android.util.Log.e(com.android.server.policy.PermissionPolicyService.LOG_TAG, "couldn't start grant permission dialogfor other package " + pkgName, e);
            }
        }

        @Override // com.android.server.policy.PermissionPolicyInternal
        public boolean isInitialized(int userId) {
            return com.android.server.policy.PermissionPolicyService.this.isStarted(userId);
        }

        @Override // com.android.server.policy.PermissionPolicyInternal
        public void setOnInitializedCallback(com.android.server.policy.PermissionPolicyInternal.OnInitializedCallback callback) {
            synchronized (com.android.server.policy.PermissionPolicyService.this.mLock) {
                com.android.server.policy.PermissionPolicyService.this.mOnInitializedCallback = callback;
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private boolean isActionRemovedForCallingPackage(android.content.Intent r7, int r8, java.lang.String r9) {
            /*
                r6 = this;
                java.lang.String r0 = r7.getAction()
                r1 = 0
                if (r0 != 0) goto L8
                return r1
            L8:
                int r2 = r0.hashCode()
                r3 = 1
                switch(r2) {
                    case -1673968409: goto L1b;
                    case 579418056: goto L11;
                    default: goto L10;
                }
            L10:
                goto L25
            L11:
                java.lang.String r2 = "android.telecom.action.CHANGE_DEFAULT_DIALER"
                boolean r2 = r0.equals(r2)
                if (r2 == 0) goto L10
                r2 = r1
                goto L26
            L1b:
                java.lang.String r2 = "android.provider.Telephony.ACTION_CHANGE_DEFAULT"
                boolean r2 = r0.equals(r2)
                if (r2 == 0) goto L10
                r2 = r3
                goto L26
            L25:
                r2 = -1
            L26:
                switch(r2) {
                    case 0: goto L2a;
                    case 1: goto L2a;
                    default: goto L29;
                }
            L29:
                return r1
            L2a:
                com.android.server.policy.PermissionPolicyService r2 = com.android.server.policy.PermissionPolicyService.this     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L44
                android.content.Context r2 = r2.getContext()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L44
                android.content.pm.PackageManager r2 = r2.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L44
                int r4 = android.os.UserHandle.getUserId(r8)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L44
                android.content.pm.ApplicationInfo r2 = r2.getApplicationInfoAsUser(r9, r1, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L44
                int r4 = r2.targetSdkVersion     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L44
                r5 = 29
                if (r4 < r5) goto L43
                return r3
            L43:
                goto L5f
            L44:
                r2 = move-exception
                java.lang.String r3 = com.android.server.policy.PermissionPolicyService.m8096$$Nest$sfgetLOG_TAG()
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Cannot find application info for "
                java.lang.StringBuilder r4 = r4.append(r5)
                java.lang.StringBuilder r4 = r4.append(r9)
                java.lang.String r4 = r4.toString()
                android.util.Slog.i(r3, r4)
            L5f:
                java.lang.String r2 = "android.intent.extra.CALLING_PACKAGE"
                r7.putExtra(r2, r9)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.policy.PermissionPolicyService.Internal.isActionRemovedForCallingPackage(android.content.Intent, int, java.lang.String):boolean");
        }

        private boolean shouldForceShowNotificationPermissionRequest(java.lang.String pkgName, android.os.UserHandle user) {
            boolean hasCreatedNotificationChannels;
            boolean granted;
            boolean explicitlySet;
            com.android.server.pm.pkg.AndroidPackage pkg = com.android.server.policy.PermissionPolicyService.this.mPackageManagerInternal.getPackage(pkgName);
            if (pkg == null || pkg.getPackageName() == null || java.util.Objects.equals(pkgName, com.android.server.policy.PermissionPolicyService.this.mPackageManager.getPermissionControllerPackageName()) || pkg.getTargetSdkVersion() < 23) {
                if (pkg == null) {
                    android.util.Slog.w(com.android.server.policy.PermissionPolicyService.LOG_TAG, "Cannot check for Notification prompt, no package for " + pkgName);
                }
                return false;
            }
            synchronized (com.android.server.policy.PermissionPolicyService.this.mLock) {
                if (!com.android.server.policy.PermissionPolicyService.this.mBootCompleted) {
                    return false;
                }
                if (!pkg.getRequestedPermissions().contains("android.permission.POST_NOTIFICATIONS") || android.app.compat.CompatChanges.isChangeEnabled(com.android.server.policy.PermissionPolicyService.NOTIFICATION_PERM_CHANGE_ID, pkgName, user) || com.android.server.policy.PermissionPolicyService.this.mKeyguardManager.isKeyguardLocked()) {
                    return false;
                }
                int uid = user.getUid(pkg.getUid());
                if (com.android.server.policy.PermissionPolicyService.this.mNotificationManager == null) {
                    com.android.server.policy.PermissionPolicyService.this.mNotificationManager = (com.android.server.notification.NotificationManagerInternal) com.android.server.LocalServices.getService(com.android.server.notification.NotificationManagerInternal.class);
                }
                if (com.android.server.policy.PermissionPolicyService.this.mNotificationManager.getNumNotificationChannelsForPackage(pkgName, uid, true) <= 0) {
                    hasCreatedNotificationChannels = false;
                } else {
                    hasCreatedNotificationChannels = true;
                }
                if (com.android.server.policy.PermissionPolicyService.this.mPermissionManagerInternal.checkUidPermission(uid, "android.permission.POST_NOTIFICATIONS", 0) != 0) {
                    granted = false;
                } else {
                    granted = true;
                }
                int flags = com.android.server.policy.PermissionPolicyService.this.mPackageManager.getPermissionFlags("android.permission.POST_NOTIFICATIONS", pkgName, user);
                if ((32823 & flags) == 0) {
                    explicitlySet = false;
                } else {
                    explicitlySet = true;
                }
                if (granted || !hasCreatedNotificationChannels || explicitlySet) {
                    return false;
                }
                return true;
            }
        }
    }

    public com.android.server.policy.IPermissionPolicyServiceWrapper getWrapper() {
        return this.mPermissionPolicyServiceWrapper;
    }

    private class PermissionPolicyServiceWrapper implements com.android.server.policy.IPermissionPolicyServiceWrapper {
        private PermissionPolicyServiceWrapper() {
        }

        @Override // com.android.server.policy.IPermissionPolicyServiceWrapper
        public void synchronizeUidPermissionsAndAppOpsAsync(int uid) {
            com.android.server.policy.PermissionPolicyService.this.synchronizeUidPermissionsAndAppOpsAsync(uid);
        }
    }
}
