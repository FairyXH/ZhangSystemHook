package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionManagerServiceImpl implements com.android.server.pm.permission.PermissionManagerServiceInterface {
    private static final long BACKGROUND_RATIONALE_CHANGE_ID = 147316723;
    private static final int BLOCKING_PERMISSION_FLAGS = 52;
    private static final int MAX_PERMISSION_TREE_FOOTPRINT = 32768;
    private static final java.lang.String SKIP_KILL_APP_REASON_NOTIFICATION_TEST = "skip permission revoke app kill for notification test";
    private static final java.lang.String TAG = "PermissionManager";
    private static final int UPDATE_PERMISSIONS_ALL = 1;
    private static final int UPDATE_PERMISSIONS_REPLACE_ALL = 4;
    private static final int UPDATE_PERMISSIONS_REPLACE_PKG = 2;
    private static final int USER_PERMISSION_FLAGS = 3;
    private final com.android.server.pm.ApexManager mApexManager;
    private final android.content.Context mContext;
    private final int[] mGlobalGids;
    private final android.os.Handler mHandler;
    private final boolean mIsLeanback;
    private final com.android.server.pm.permission.PermissionManagerServiceImpl.OnPermissionChangeListeners mOnPermissionChangeListeners;
    private final android.content.pm.PackageManagerInternal mPackageManagerInt;
    private android.permission.PermissionControllerManager mPermissionControllerManager;
    private com.android.server.policy.PermissionPolicyInternal mPermissionPolicyInternal;
    private android.util.ArraySet<java.lang.String> mPrivappPermissionsViolations;
    private final android.util.SparseArray<android.util.ArraySet<java.lang.String>> mSystemPermissions;
    private boolean mSystemReady;
    private final com.android.server.pm.UserManagerInternal mUserManagerInt;
    private static final java.lang.String LOG_TAG = com.android.server.pm.permission.PermissionManagerServiceImpl.class.getSimpleName();
    private static final long BACKUP_TIMEOUT_MILLIS = java.util.concurrent.TimeUnit.SECONDS.toMillis(60);
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final java.util.List<java.lang.String> STORAGE_PERMISSIONS = new java.util.ArrayList();
    private static final java.util.Set<java.lang.String> READ_MEDIA_AURAL_PERMISSIONS = new android.util.ArraySet();
    private static final java.util.Set<java.lang.String> READ_MEDIA_VISUAL_PERMISSIONS = new android.util.ArraySet();
    private static final java.util.List<java.lang.String> NEARBY_DEVICES_PERMISSIONS = new java.util.ArrayList();
    private static final java.util.List<java.lang.String> NOTIFICATION_PERMISSIONS = new java.util.ArrayList();
    private static final java.util.Map<java.lang.String, java.lang.String> FULLER_PERMISSION_MAP = new java.util.HashMap();
    private final android.util.ArraySet<java.lang.String> mPrivilegedPermissionAllowlistSourcePackageNames = new android.util.ArraySet<>();
    private final com.android.server.pm.PackageManagerTracedLock mLock = new com.android.server.pm.PackageManagerTracedLock();
    private final com.android.server.pm.permission.DevicePermissionState mState = new com.android.server.pm.permission.DevicePermissionState();
    private final com.android.internal.logging.MetricsLogger mMetricsLogger = new com.android.internal.logging.MetricsLogger();
    private final com.android.internal.compat.IPlatformCompat mPlatformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));
    private final com.android.server.pm.permission.PermissionRegistry mRegistry = new com.android.server.pm.permission.PermissionRegistry();
    private final android.util.SparseBooleanArray mHasNoDelayedPermBackup = new android.util.SparseBooleanArray();
    public com.android.server.pm.permission.IPermissionManagerServiceExt mPermissionManagerServiceExt = (com.android.server.pm.permission.IPermissionManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.permission.IPermissionManagerServiceExt.class).base(this).create();
    private final com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback mDefaultPermissionCallback = new com.android.server.pm.permission.PermissionManagerServiceImpl.AnonymousClass1();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface UpdatePermissionFlags {
    }

    static {
        FULLER_PERMISSION_MAP.put("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION");
        FULLER_PERMISSION_MAP.put("android.permission.INTERACT_ACROSS_USERS", "android.permission.INTERACT_ACROSS_USERS_FULL");
        STORAGE_PERMISSIONS.add("android.permission.READ_EXTERNAL_STORAGE");
        STORAGE_PERMISSIONS.add("android.permission.WRITE_EXTERNAL_STORAGE");
        READ_MEDIA_AURAL_PERMISSIONS.add("android.permission.READ_MEDIA_AUDIO");
        READ_MEDIA_VISUAL_PERMISSIONS.add("android.permission.READ_MEDIA_VIDEO");
        READ_MEDIA_VISUAL_PERMISSIONS.add("android.permission.READ_MEDIA_IMAGES");
        READ_MEDIA_VISUAL_PERMISSIONS.add("android.permission.ACCESS_MEDIA_LOCATION");
        READ_MEDIA_VISUAL_PERMISSIONS.add("android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.BLUETOOTH_ADVERTISE");
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.BLUETOOTH_CONNECT");
        NEARBY_DEVICES_PERMISSIONS.add("android.permission.BLUETOOTH_SCAN");
        NOTIFICATION_PERMISSIONS.add("android.permission.POST_NOTIFICATIONS");
    }

    /* JADX INFO: renamed from: com.android.server.pm.permission.PermissionManagerServiceImpl$1, reason: invalid class name */
    class AnonymousClass1 extends com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback {
        AnonymousClass1() {
            super();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onGidsChanged(final int appId, final int userId) {
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$1$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.pm.permission.PermissionManagerServiceImpl.killUid(appId, userId, "permission grant or revoke changed gids");
                }
            });
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionGranted(int uid, int userId) {
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(uid);
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onInstallPermissionGranted() {
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionRevoked(final int uid, final int userId, final java.lang.String reason, boolean overrideKill, final java.lang.String permissionName) {
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(uid);
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(false);
            if (overrideKill) {
                return;
            }
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPermissionRevoked$1(permissionName, uid, reason, userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPermissionRevoked$1(java.lang.String permissionName, int uid, java.lang.String reason, int userId) {
            if ("android.permission.POST_NOTIFICATIONS".equals(permissionName) && isAppBackupAndRestoreRunning(uid)) {
                return;
            }
            int appId = android.os.UserHandle.getAppId(uid);
            if (reason == null) {
                com.android.server.pm.permission.PermissionManagerServiceImpl.killUid(appId, userId, "permissions revoked");
            } else {
                com.android.server.pm.permission.PermissionManagerServiceImpl.killUid(appId, userId, reason);
            }
        }

        private boolean isAppBackupAndRestoreRunning(int uid) {
            if (com.android.server.pm.permission.PermissionManagerServiceImpl.this.checkUidPermission(uid, "android.permission.BACKUP") != 0) {
                return false;
            }
            int userId = android.os.UserHandle.getUserId(uid);
            boolean isInSetup = ((java.lang.Boolean) getSecureInt("user_setup_complete", userId).map(new java.util.function.Function() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$1$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Boolean.valueOf(((java.lang.Integer) obj).intValue() == 0);
                }
            }).orElse(false)).booleanValue();
            if (isInSetup) {
                return true;
            }
            boolean isInDeferredSetup = ((java.lang.Boolean) getSecureInt("user_setup_personalization_state", userId).map(new java.util.function.Function() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$1$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Boolean.valueOf(((java.lang.Integer) obj).intValue() == 1);
                }
            }).orElse(false)).booleanValue();
            return isInDeferredSetup;
        }

        private java.util.Optional<java.lang.Integer> getSecureInt(java.lang.String settingName, int userId) {
            try {
                return java.util.Optional.of(java.lang.Integer.valueOf(android.provider.Settings.Secure.getIntForUser(com.android.server.pm.permission.PermissionManagerServiceImpl.this.mContext.getContentResolver(), settingName, userId)));
            } catch (android.provider.Settings.SettingNotFoundException e) {
                android.util.Slog.i(com.android.server.pm.permission.PermissionManagerServiceImpl.LOG_TAG, "Setting " + settingName + " not found", e);
                return java.util.Optional.empty();
            }
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onInstallPermissionRevoked() {
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionUpdated(int[] userIds, boolean sync, int appId) {
            for (int i : userIds) {
                int uid = android.os.UserHandle.getUid(i, appId);
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(uid);
            }
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mPackageManagerInt.writePermissionSettings(userIds, !sync);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onInstallPermissionUpdated() {
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionRemoved() {
            com.android.server.pm.permission.PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(false);
        }
    }

    public PermissionManagerServiceImpl(android.content.Context context, android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> availableFeatures) {
        java.lang.String carServicePackage;
        android.content.pm.PackageManager.invalidatePackageInfoCache();
        android.permission.PermissionManager.disablePackageNamePermissionCache();
        this.mContext = context;
        this.mPackageManagerInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mUserManagerInt = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mIsLeanback = availableFeatures.containsKey("android.software.leanback");
        this.mApexManager = com.android.server.pm.ApexManager.getInstance();
        this.mPrivilegedPermissionAllowlistSourcePackageNames.add(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        if (availableFeatures.containsKey("android.hardware.type.automotive") && (carServicePackage = android.os.SystemProperties.get("ro.android.car.carservice.package", (java.lang.String) null)) != null) {
            this.mPrivilegedPermissionAllowlistSourcePackageNames.add(carServicePackage);
        }
        android.os.HandlerThread handlerThread = new com.android.server.ServiceThread(TAG, 10, true);
        handlerThread.start();
        this.mHandler = new android.os.Handler(handlerThread.getLooper());
        com.android.server.Watchdog.getInstance().addThread(this.mHandler);
        com.android.server.SystemConfig systemConfig = com.android.server.SystemConfig.getInstance();
        this.mSystemPermissions = systemConfig.getSystemPermissions();
        this.mGlobalGids = systemConfig.getGlobalGids();
        this.mOnPermissionChangeListeners = new com.android.server.pm.permission.PermissionManagerServiceImpl.OnPermissionChangeListeners(com.android.server.FgThread.get().getLooper());
        android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.PermissionEntry> permConfig = com.android.server.SystemConfig.getInstance().getPermissions();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            for (int i = 0; i < permConfig.size(); i++) {
                try {
                    com.android.server.SystemConfig.PermissionEntry perm = permConfig.valueAt(i);
                    com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(perm.name);
                    if (bp == null) {
                        bp = new com.android.server.pm.permission.Permission(perm.name, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 1);
                        this.mRegistry.addPermission(bp);
                    }
                    if (perm.gids != null) {
                        bp.setGids(perm.gids, perm.perUser);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void killUid(int appId, int userId, java.lang.String reason) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.app.IActivityManager am = android.app.ActivityManager.getService();
            if (am != null) {
                try {
                    am.killUidForPermissionChange(appId, userId, reason);
                } catch (android.os.RemoteException e) {
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private java.lang.String[] getAppOpPermissionPackagesInternal(java.lang.String permissionName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                android.util.ArraySet<java.lang.String> packageNames = this.mRegistry.getAppOpPermissionPackages(permissionName);
                if (packageNames == null) {
                    java.lang.String[] strArr = libcore.util.EmptyArray.STRING;
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return strArr;
                }
                java.lang.String[] strArr2 = (java.lang.String[]) packageNames.toArray(new java.lang.String[0]);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return strArr2;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) {
        final int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return java.util.Collections.emptyList();
        }
        java.util.List<android.content.pm.PermissionGroupInfo> out = new java.util.ArrayList<>();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.internal.pm.pkg.component.ParsedPermissionGroup pg : this.mRegistry.getPermissionGroups()) {
                    out.add(com.android.server.pm.parsing.PackageInfoUtils.generatePermissionGroupInfo(pg, flags));
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        final int callingUserId = android.os.UserHandle.getUserId(callingUid);
        out.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getAllPermissionGroups$0(callingUid, callingUserId, (android.content.pm.PermissionGroupInfo) obj);
            }
        });
        return out;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getAllPermissionGroups$0(int callingUid, int callingUserId, android.content.pm.PermissionGroupInfo it) {
        return this.mPackageManagerInt.filterAppAccess(it.packageName, callingUid, callingUserId, false);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String groupName, int flags) {
        int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.internal.pm.pkg.component.ParsedPermissionGroup permissionGroup = this.mRegistry.getPermissionGroup(groupName);
                if (permissionGroup == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return null;
                }
                android.content.pm.PermissionGroupInfo permissionGroupInfo = com.android.server.pm.parsing.PackageInfoUtils.generatePermissionGroupInfo(permissionGroup, flags);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                int callingUserId = android.os.UserHandle.getUserId(callingUid);
                if (!this.mPackageManagerInt.filterAppAccess(permissionGroupInfo.packageName, callingUid, callingUserId, false)) {
                    return permissionGroupInfo;
                }
                android.util.EventLog.writeEvent(1397638484, "186113473", java.lang.Integer.valueOf(callingUid), groupName);
                return null;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permName, int flags, java.lang.String opPackageName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        com.android.server.pm.pkg.AndroidPackage opPackage = this.mPackageManagerInt.getPackage(opPackageName);
        int targetSdkVersion = getPermissionInfoCallingTargetSdkVersion(opPackage, callingUid);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permName);
                if (bp == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return null;
                }
                android.content.pm.PermissionInfo permissionInfo = bp.generatePermissionInfo(flags, targetSdkVersion);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                int callingUserId = android.os.UserHandle.getUserId(callingUid);
                if (!this.mPackageManagerInt.filterAppAccess(permissionInfo.packageName, callingUid, callingUserId, false)) {
                    return permissionInfo;
                }
                android.util.EventLog.writeEvent(1397638484, "183122164", java.lang.Integer.valueOf(callingUid), permName);
                return null;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private int getPermissionInfoCallingTargetSdkVersion(com.android.server.pm.pkg.AndroidPackage pkg, int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        if (appId == 0 || appId == 1000 || appId == 2000 || pkg == null) {
            return 10000;
        }
        return pkg.getTargetSdkVersion();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String groupName, int flags) {
        final int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        java.util.List<android.content.pm.PermissionInfo> out = new java.util.ArrayList<>(10);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.internal.pm.pkg.component.ParsedPermissionGroup permissionGroup = this.mRegistry.getPermissionGroup(groupName);
                if (groupName != null && permissionGroup == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return null;
                }
                for (com.android.server.pm.permission.Permission bp : this.mRegistry.getPermissions()) {
                    if (java.util.Objects.equals(bp.getGroup(), groupName)) {
                        out.add(bp.generatePermissionInfo(flags));
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                final int callingUserId = android.os.UserHandle.getUserId(callingUid);
                if (permissionGroup != null && this.mPackageManagerInt.filterAppAccess(permissionGroup.getPackageName(), callingUid, callingUserId, false)) {
                    return null;
                }
                out.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda5
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return this.f$0.lambda$queryPermissionsByGroup$1(callingUid, callingUserId, (android.content.pm.PermissionInfo) obj);
                    }
                });
                return out;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$queryPermissionsByGroup$1(int callingUid, int callingUserId, android.content.pm.PermissionInfo it) {
        return this.mPackageManagerInt.filterAppAccess(it.packageName, callingUid, callingUserId, false);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(android.content.pm.PermissionInfo info, boolean async) {
        boolean added;
        boolean changed;
        int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            throw new java.lang.SecurityException("Instant apps can't add permissions");
        }
        if (info.labelRes == 0 && info.nonLocalizedLabel == null) {
            throw new java.lang.SecurityException("Label must be specified in permission");
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.Permission tree = this.mRegistry.enforcePermissionTree(info.name, callingUid);
                com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(info.name);
                added = bp == null;
                int fixedLevel = android.content.pm.PermissionInfo.fixProtectionLevel(info.protectionLevel);
                enforcePermissionCapLocked(info, tree);
                if (added) {
                    bp = new com.android.server.pm.permission.Permission(info.name, tree.getPackageName(), 2);
                } else if (!bp.isDynamic()) {
                    throw new java.lang.SecurityException("Not allowed to modify non-dynamic permission " + info.name);
                }
                changed = bp.addToTree(fixedLevel, info, tree);
                if (added) {
                    this.mRegistry.addPermission(bp);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (changed) {
            this.mPackageManagerInt.writeSettings(async);
        }
        return added;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(java.lang.String permName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            throw new java.lang.SecurityException("Instant applications don't have access to this method");
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mRegistry.enforcePermissionTree(permName, callingUid);
                com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permName);
                if (bp == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                if (!bp.isDynamic()) {
                    android.util.Slog.wtf(TAG, "Not allowed to modify non-dynamic permission " + permName);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                } else {
                    this.mRegistry.removePermission(permName);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    this.mPackageManagerInt.writeSettings(false);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        return getPermissionFlagsInternal(packageName, permName, callingUid, userId);
    }

    private int getPermissionFlagsInternal(java.lang.String packageName, java.lang.String permName, int callingUid, int userId) {
        if (!this.mUserManagerInt.exists(userId)) {
            return 0;
        }
        enforceGrantRevokeGetRuntimePermissionPermissions("getPermissionFlags");
        enforceCrossUserPermission(callingUid, userId, true, false, "getPermissionFlags");
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        if (pkg == null || this.mPackageManagerInt.filterAppAccess(packageName, callingUid, userId, false)) {
            return 0;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                if (this.mRegistry.getPermission(permName) == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return 0;
                }
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for " + packageName + " and user " + userId);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return 0;
                }
                int permissionFlags = uidState.getPermissionFlags(permName);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return permissionFlags;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlags(java.lang.String packageName, java.lang.String permName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, java.lang.String deviceId, int userId) {
        boolean overridePolicy;
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000 && callingUid != 0 && (flagMask & 4) != 0) {
            if (checkAdjustPolicyFlagPermission) {
                this.mContext.enforceCallingOrSelfPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY", "Need android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY to change policy flags");
            } else if (this.mPackageManagerInt.getUidTargetSdkVersion(callingUid) >= 29) {
                throw new java.lang.IllegalArgumentException("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY needs  to be checked for packages targeting 29 or later when changing policy flags");
            }
            overridePolicy = true;
        } else {
            overridePolicy = false;
        }
        updatePermissionFlagsInternal(packageName, permName, flagMask, flagValues, callingUid, userId, overridePolicy, this.mDefaultPermissionCallback);
    }

    private void updatePermissionFlagsInternal(java.lang.String packageName, java.lang.String permName, int flagMask, int flagValues, int callingUid, int userId, boolean overridePolicy, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback) {
        int flagValues2;
        int flagValues3;
        boolean isRequested;
        if (this.mUserManagerInt.exists(userId)) {
            enforceGrantRevokeRuntimePermissionPermissions("updatePermissionFlags");
            enforceCrossUserPermission(callingUid, userId, true, true, "updatePermissionFlags");
            if ((flagMask & 4) != 0 && !overridePolicy) {
                throw new java.lang.SecurityException("updatePermissionFlags requires android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY");
            }
            if (callingUid == 1000) {
                flagValues2 = flagMask;
                flagValues3 = flagValues;
            } else {
                flagValues3 = flagValues & (-17) & (-33) & (-4097) & (-2049) & (-8193) & (-16385);
                flagValues2 = flagMask & (-17) & (-33);
            }
            com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
            if (pkg == null) {
                android.util.Log.e(TAG, "Unknown package: " + packageName);
                return;
            }
            if (this.mPackageManagerInt.filterAppAccess(packageName, callingUid, userId, false)) {
                throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
            }
            boolean isRequested2 = false;
            if (pkg.getRequestedPermissions().contains(permName)) {
                isRequested2 = true;
            }
            if (!isRequested2) {
                java.lang.String[] sharedUserPackageNames = this.mPackageManagerInt.getSharedUserPackagesForPackage(packageName, userId);
                for (java.lang.String sharedUserPackageName : sharedUserPackageNames) {
                    com.android.server.pm.pkg.AndroidPackage sharedUserPkg = this.mPackageManagerInt.getPackage(sharedUserPackageName);
                    if (sharedUserPkg != null && sharedUserPkg.getRequestedPermissions().contains(permName)) {
                        isRequested = true;
                        break;
                    }
                }
                isRequested = isRequested2;
            } else {
                isRequested = isRequested2;
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permName);
                    if (bp == null) {
                        throw new java.lang.IllegalArgumentException("Unknown permission: " + permName);
                    }
                    boolean isRuntimePermission = bp.isRuntime();
                    com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                    if (uidState == null) {
                        android.util.Slog.e(TAG, "Missing permissions state for " + packageName + " and user " + userId);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return;
                    }
                    if (!uidState.hasPermissionState(permName) && !isRequested) {
                        android.util.Log.e(TAG, "Permission " + permName + " isn't requested by package " + packageName);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return;
                    }
                    boolean permissionUpdated = uidState.updatePermissionFlags(bp, flagValues2, flagValues3);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    if (permissionUpdated && callback != null) {
                        if (!isRuntimePermission) {
                            callback.onInstallPermissionUpdated();
                        } else {
                            callback.onPermissionUpdated(new int[]{userId}, false, pkg.getUid());
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, final int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (!this.mUserManagerInt.exists(userId)) {
            return;
        }
        enforceGrantRevokeRuntimePermissionPermissions("updatePermissionFlagsForAllApps");
        enforceCrossUserPermission(callingUid, userId, true, true, "updatePermissionFlagsForAllApps");
        final int effectiveFlagMask = callingUid != 1000 ? flagMask : flagMask & (-17);
        final int effectiveFlagValues = callingUid != 1000 ? flagValues : flagValues & (-17);
        final boolean[] changed = new boolean[1];
        this.mPackageManagerInt.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updatePermissionFlagsForAllApps$2(userId, changed, effectiveFlagMask, effectiveFlagValues, (com.android.server.pm.pkg.AndroidPackage) obj);
            }
        });
        if (changed[0]) {
            this.mPackageManagerInt.writePermissionSettings(new int[]{userId}, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissionFlagsForAllApps$2(int userId, boolean[] changed, int effectiveFlagMask, int effectiveFlagValues, com.android.server.pm.pkg.AndroidPackage pkg) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for " + pkg.getPackageName() + " and user " + userId);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                } else {
                    changed[0] = changed[0] | uidState.updatePermissionFlagsForAllPermissions(effectiveFlagMask, effectiveFlagValues);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    this.mOnPermissionChangeListeners.onPermissionsChanged(pkg.getUid());
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private int checkPermission(java.lang.String pkgName, java.lang.String permName, int userId) {
        return checkPermission(pkgName, permName, "default:0", userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(java.lang.String pkgName, java.lang.String permName, java.lang.String deviceId, int userId) {
        com.android.server.pm.pkg.AndroidPackage pkg;
        if (this.mUserManagerInt.exists(userId) && (pkg = this.mPackageManagerInt.getPackage(pkgName)) != null) {
            return checkPermissionInternal(pkg, true, permName, userId);
        }
        return -1;
    }

    private int checkPermissionInternal(com.android.server.pm.pkg.AndroidPackage pkg, boolean isPackageExplicit, java.lang.String permissionName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (isPackageExplicit || pkg.getSharedUserId() == null) {
            if (this.mPackageManagerInt.filterAppAccess(pkg.getPackageName(), callingUid, userId, false)) {
                return -1;
            }
        } else if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return -1;
        }
        int uid = android.os.UserHandle.getUid(userId, pkg.getUid());
        boolean isInstantApp = this.mPackageManagerInt.getInstantAppPackageName(uid) != null;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for " + pkg.getPackageName() + " and user " + userId);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return -1;
                }
                if (checkSinglePermissionInternalLocked(uidState, permissionName, isInstantApp)) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return 0;
                }
                java.lang.String fullerPermissionName = FULLER_PERMISSION_MAP.get(permissionName);
                if (fullerPermissionName == null || !checkSinglePermissionInternalLocked(uidState, fullerPermissionName, isInstantApp)) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return -1;
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return 0;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private boolean checkSinglePermissionInternalLocked(com.android.server.pm.permission.UidPermissionState uidState, java.lang.String permissionName, boolean isInstantApp) {
        if (!uidState.isPermissionGranted(permissionName)) {
            return false;
        }
        if (!isInstantApp) {
            return true;
        }
        com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permissionName);
        return permission != null && permission.isInstant();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkUidPermission(int uid, java.lang.String permName) {
        return checkUidPermission(uid, permName, "default:0");
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int uid, java.lang.String permName, java.lang.String deviceId) {
        int userId = android.os.UserHandle.getUserId(uid);
        if (!this.mUserManagerInt.exists(userId)) {
            return -1;
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(uid);
        return checkUidPermissionInternal(pkg, uid, permName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, android.permission.PermissionManager.PermissionState> getAllPermissionStates(java.lang.String packageName, java.lang.String deviceId, int userId) {
        throw new java.lang.UnsupportedOperationException("This method is supported in newer implementation only");
    }

    private int checkUidPermissionInternal(com.android.server.pm.pkg.AndroidPackage pkg, int uid, java.lang.String permissionName) {
        if (pkg != null) {
            int userId = android.os.UserHandle.getUserId(uid);
            return checkPermissionInternal(pkg, false, permissionName, userId);
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                if (checkSingleUidPermissionInternalLocked(uid, permissionName)) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return 0;
                }
                java.lang.String fullerPermissionName = FULLER_PERMISSION_MAP.get(permissionName);
                if (fullerPermissionName != null && checkSingleUidPermissionInternalLocked(uid, fullerPermissionName)) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return 0;
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return -1;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private boolean checkSingleUidPermissionInternalLocked(int uid, java.lang.String permissionName) {
        android.util.ArraySet<java.lang.String> permissions = this.mSystemPermissions.get(uid);
        return permissions != null && permissions.contains(permissionName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS", "addOnPermissionsChangeListener");
        this.mOnPermissionChangeListeners.addListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        if (this.mPackageManagerInt.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            throw new java.lang.SecurityException("Instant applications don't have access to this method");
        }
        this.mOnPermissionChangeListeners.removeListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<java.lang.String> getAllowlistedRestrictedPermissions(java.lang.String packageName, int flags, int userId) {
        java.util.Objects.requireNonNull(packageName);
        com.android.internal.util.Preconditions.checkFlagsArgument(flags, 7);
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, (java.lang.String) null);
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "getAllowlistedRestrictedPermissions for user " + userId);
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        if (pkg == null) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.filterAppAccess(packageName, callingUid, android.os.UserHandle.getCallingUserId(), false)) {
            return null;
        }
        boolean isCallerPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
        boolean isCallerInstallerOnRecord = this.mPackageManagerInt.isCallerInstallerOfRecord(pkg, callingUid);
        if ((flags & 1) != 0 && !isCallerPrivileged) {
            throw new java.lang.SecurityException("Querying system allowlist requires android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        if ((flags & 6) != 0 && !isCallerPrivileged && !isCallerInstallerOnRecord) {
            throw new java.lang.SecurityException("Querying upgrade or installer allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return getAllowlistedRestrictedPermissionsInternal(pkg, flags, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private java.util.List<java.lang.String> getAllowlistedRestrictedPermissionsInternal(com.android.server.pm.pkg.AndroidPackage pkg, int flags, int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for " + pkg.getPackageName() + " and user " + userId);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return null;
                }
                int queryFlags = 0;
                if ((flags & 1) != 0) {
                    queryFlags = 0 | 4096;
                }
                if ((flags & 4) != 0) {
                    queryFlags |= 8192;
                }
                if ((flags & 2) != 0) {
                    queryFlags |= 2048;
                }
                java.util.ArrayList<java.lang.String> allowlistedPermissions = null;
                for (java.lang.String permissionName : pkg.getRequestedPermissions()) {
                    int currentFlags = uidState.getPermissionFlags(permissionName);
                    if ((currentFlags & queryFlags) != 0) {
                        if (allowlistedPermissions == null) {
                            allowlistedPermissions = new java.util.ArrayList<>();
                        }
                        allowlistedPermissions.add(permissionName);
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return allowlistedPermissions;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        java.util.Objects.requireNonNull(permName);
        if (!checkExistsAndEnforceCannotModifyImmutablyRestrictedPermission(permName)) {
            return false;
        }
        java.util.List<java.lang.String> permissions = getAllowlistedRestrictedPermissions(packageName, flags, userId);
        if (permissions == null) {
            permissions = new java.util.ArrayList(1);
        }
        if (permissions.indexOf(permName) >= 0) {
            return false;
        }
        permissions.add(permName);
        return setAllowlistedRestrictedPermissions(packageName, permissions, flags, userId);
    }

    private boolean checkExistsAndEnforceCannotModifyImmutablyRestrictedPermission(java.lang.String permName) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permName);
                if (bp == null) {
                    android.util.Slog.w(TAG, "No such permissions: " + permName);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return false;
                }
                java.lang.String permissionPackageName = bp.getPackageName();
                boolean isImmutablyRestrictedPermission = bp.isHardOrSoftRestricted() && bp.isImmutablyRestricted();
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                int callingUid = android.os.Binder.getCallingUid();
                int callingUserId = android.os.UserHandle.getUserId(callingUid);
                if (this.mPackageManagerInt.filterAppAccess(permissionPackageName, callingUid, callingUserId, false)) {
                    android.util.EventLog.writeEvent(1397638484, "186404356", java.lang.Integer.valueOf(callingUid), permName);
                    return false;
                }
                if (!isImmutablyRestrictedPermission || this.mContext.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0) {
                    return true;
                }
                throw new java.lang.SecurityException("Cannot modify allowlisting of an immutably restricted permission: " + permName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permName, int flags, int userId) {
        java.util.List<java.lang.String> permissions;
        java.util.Objects.requireNonNull(permName);
        if (checkExistsAndEnforceCannotModifyImmutablyRestrictedPermission(permName) && (permissions = getAllowlistedRestrictedPermissions(packageName, flags, userId)) != null && permissions.remove(permName)) {
            return setAllowlistedRestrictedPermissions(packageName, permissions, flags, userId);
        }
        return false;
    }

    private boolean setAllowlistedRestrictedPermissions(java.lang.String packageName, java.util.List<java.lang.String> permissions, int flags, int userId) {
        java.util.Objects.requireNonNull(packageName);
        com.android.internal.util.Preconditions.checkFlagsArgument(flags, 7);
        com.android.internal.util.Preconditions.checkArgument(java.lang.Integer.bitCount(flags) == 1);
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, (java.lang.String) null);
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "setAllowlistedRestrictedPermissions for user " + userId);
        }
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        if (pkg == null) {
            return false;
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.filterAppAccess(packageName, callingUid, android.os.UserHandle.getCallingUserId(), false)) {
            return false;
        }
        boolean isCallerPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
        boolean isCallerInstallerOnRecord = this.mPackageManagerInt.isCallerInstallerOfRecord(pkg, callingUid);
        if ((flags & 1) != 0 && !isCallerPrivileged) {
            throw new java.lang.SecurityException("Modifying system allowlist requires android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        if ((flags & 4) != 0) {
            if (!isCallerPrivileged && !isCallerInstallerOnRecord) {
                throw new java.lang.SecurityException("Modifying upgrade allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
            }
            java.util.List<java.lang.String> allowlistedPermissions = getAllowlistedRestrictedPermissions(pkg.getPackageName(), flags, userId);
            if (permissions == null || permissions.isEmpty()) {
                if (allowlistedPermissions == null || allowlistedPermissions.isEmpty()) {
                    return true;
                }
            } else {
                int permissionCount = permissions.size();
                for (int i = 0; i < permissionCount; i++) {
                    if ((allowlistedPermissions == null || !allowlistedPermissions.contains(permissions.get(i))) && !isCallerPrivileged) {
                        throw new java.lang.SecurityException("Adding to upgrade allowlist requiresandroid.permission.WHITELIST_RESTRICTED_PERMISSIONS");
                    }
                }
            }
        }
        if ((flags & 2) != 0 && !isCallerPrivileged && !isCallerInstallerOnRecord) {
            throw new java.lang.SecurityException("Modifying installer allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            setAllowlistedRestrictedPermissionsInternal(pkg, permissions, flags, userId);
            return true;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) throws java.lang.Throwable {
        int callingUid = android.os.Binder.getCallingUid();
        boolean overridePolicy = checkUidPermission(callingUid, "android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") == 0;
        grantRuntimePermissionInternal(packageName, permName, overridePolicy, callingUid, userId, this.mDefaultPermissionCallback);
    }

    private void grantRuntimePermissionInternal(java.lang.String packageName, java.lang.String permName, boolean overridePolicy, int callingUid, int userId, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback) throws java.lang.Throwable {
        boolean isRolePermission;
        boolean isSoftRestrictedPermission;
        if (!this.mUserManagerInt.exists(userId)) {
            android.util.Log.e(TAG, "No such user:" + userId);
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.GRANT_RUNTIME_PERMISSIONS", "grantRuntimePermission");
        enforceCrossUserPermission(callingUid, userId, true, true, "grantRuntimePermission");
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        com.android.server.pm.pkg.PackageStateInternal ps = this.mPackageManagerInt.getPackageStateInternal(packageName);
        if (pkg == null || ps == null) {
            android.util.Log.e(TAG, "Unknown package: " + packageName);
            return;
        }
        boolean mayGrantSoftRestrictedPermission = false;
        if (this.mPackageManagerInt.filterAppAccess(packageName, callingUid, userId, false)) {
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permName);
                if (permission == null) {
                    throw new java.lang.IllegalArgumentException("Unknown permission: " + permName);
                }
                isRolePermission = permission.isRole();
                isSoftRestrictedPermission = permission.isSoftRestricted();
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        boolean mayGrantRolePermission = isRolePermission && mayManageRolePermission(callingUid);
        if (isSoftRestrictedPermission && com.android.server.policy.SoftRestrictedPermissionPolicy.forPermission(this.mContext, com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg), pkg, android.os.UserHandle.of(userId), permName).mayGrantPermission()) {
            mayGrantSoftRestrictedPermission = true;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                try {
                    com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permName);
                    if (bp == null) {
                        throw new java.lang.IllegalArgumentException("Unknown permission: " + permName);
                    }
                    boolean isRuntimePermission = bp.isRuntime();
                    boolean permissionHasGids = bp.hasGids();
                    if (!isRuntimePermission) {
                        try {
                            if (!bp.isDevelopment()) {
                                try {
                                    if (!bp.isRole()) {
                                        throw new java.lang.SecurityException("Permission " + permName + " requested by " + pkg.getPackageName() + " is not a changeable permission type");
                                    }
                                    if (!mayGrantRolePermission) {
                                        try {
                                            throw new java.lang.SecurityException("Permission " + permName + " is managed by role");
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                        }
                                    }
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                }
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    }
                    try {
                        com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                        if (uidState == null) {
                            android.util.Slog.e(TAG, "Missing permissions state for " + pkg.getPackageName() + " and user " + userId);
                            return;
                        }
                        if (!uidState.hasPermissionState(permName) && !pkg.getRequestedPermissions().contains(permName)) {
                            throw new java.lang.SecurityException("Package " + pkg.getPackageName() + " has not requested permission " + permName);
                        }
                        if (pkg.getTargetSdkVersion() < 23 && bp.isRuntime()) {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            return;
                        }
                        int flags = uidState.getPermissionFlags(permName);
                        if ((flags & 16) != 0) {
                            android.util.Log.e(TAG, "Cannot grant system fixed permission " + permName + " for package " + packageName);
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            return;
                        }
                        if (!overridePolicy && (flags & 4) != 0) {
                            android.util.Log.e(TAG, "Cannot grant policy fixed permission " + permName + " for package " + packageName);
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            return;
                        }
                        if (bp.isHardRestricted() && (flags & 14336) == 0) {
                            android.util.Log.e(TAG, "Cannot grant hard restricted non-exempt permission " + permName + " for package " + packageName);
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            return;
                        }
                        if (bp.isSoftRestricted() && !mayGrantSoftRestrictedPermission) {
                            android.util.Log.e(TAG, "Cannot grant soft restricted permission " + permName + " for package " + packageName);
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            return;
                        }
                        if (!bp.isDevelopment() && !bp.isRole()) {
                            if (ps.getUserStateOrDefault(userId).isInstantApp() && !bp.isInstant()) {
                                throw new java.lang.SecurityException("Cannot grant non-ephemeral permission " + permName + " for package " + packageName);
                            }
                            if (pkg.getTargetSdkVersion() < 23) {
                                android.util.Slog.w(TAG, "Cannot grant runtime permission to a legacy app");
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                return;
                            } else if (!uidState.grantPermission(bp)) {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                return;
                            }
                        } else if (!uidState.grantPermission(bp)) {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            return;
                        }
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        if (isRuntimePermission) {
                            logPermission(1243, permName, packageName);
                        }
                        int uid = android.os.UserHandle.getUid(userId, pkg.getUid());
                        if (callback != null) {
                            if (isRuntimePermission) {
                                callback.onPermissionGranted(uid, userId);
                            } else {
                                callback.onInstallPermissionGranted();
                            }
                            if (permissionHasGids) {
                                callback.onGidsChanged(android.os.UserHandle.getAppId(pkg.getUid()), userId);
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            throw th;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId, java.lang.String reason) {
        int callingUid = android.os.Binder.getCallingUid();
        boolean overridePolicy = checkUidPermission(callingUid, "android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY", "default:0") == 0;
        revokeRuntimePermissionInternal(packageName, permName, overridePolicy, callingUid, userId, reason, this.mDefaultPermissionCallback);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        boolean overridePolicy = checkUidPermission(callingUid, "android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") == 0;
        this.mContext.enforceCallingPermission("android.permission.REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL", "");
        revokeRuntimePermissionInternal(packageName, "android.permission.POST_NOTIFICATIONS", overridePolicy, true, callingUid, userId, SKIP_KILL_APP_REASON_NOTIFICATION_TEST, this.mDefaultPermissionCallback);
    }

    private void revokeRuntimePermissionInternal(java.lang.String packageName, java.lang.String permName, boolean overridePolicy, int callingUid, int userId, java.lang.String reason, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback) {
        revokeRuntimePermissionInternal(packageName, permName, overridePolicy, false, callingUid, userId, reason, callback);
    }

    private void revokeRuntimePermissionInternal(java.lang.String packageName, java.lang.String permName, boolean overridePolicy, boolean overrideKill, int callingUid, int userId, java.lang.String reason, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback) {
        boolean isRolePermission;
        if (!this.mUserManagerInt.exists(userId)) {
            android.util.Log.e(TAG, "No such user:" + userId);
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.REVOKE_RUNTIME_PERMISSIONS", "revokeRuntimePermission");
        enforceCrossUserPermission(callingUid, userId, true, true, "revokeRuntimePermission");
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        if (pkg == null) {
            android.util.Log.e(TAG, "Unknown package: " + packageName);
            return;
        }
        boolean z = false;
        if (this.mPackageManagerInt.filterAppAccess(packageName, callingUid, userId, false)) {
            throw new java.lang.IllegalArgumentException("Unknown package: " + packageName);
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permName);
                if (permission == null) {
                    throw new java.lang.IllegalArgumentException("Unknown permission: " + permName);
                }
                isRolePermission = permission.isRole();
            } finally {
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (isRolePermission && (callingUid == android.os.Process.myUid() || mayManageRolePermission(callingUid))) {
            z = true;
        }
        boolean mayRevokeRolePermission = z;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permName);
                if (bp == null) {
                    throw new java.lang.IllegalArgumentException("Unknown permission: " + permName);
                }
                boolean isRuntimePermission = bp.isRuntime();
                if (!isRuntimePermission && !bp.isDevelopment()) {
                    if (bp.isRole()) {
                        if (!mayRevokeRolePermission) {
                            throw new java.lang.SecurityException("Permission " + permName + " is managed by role");
                        }
                    } else {
                        throw new java.lang.SecurityException("Permission " + permName + " requested by " + pkg.getPackageName() + " is not a changeable permission type");
                    }
                }
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for " + pkg.getPackageName() + " and user " + userId);
                    return;
                }
                if (!uidState.hasPermissionState(permName) && !pkg.getRequestedPermissions().contains(permName)) {
                    throw new java.lang.SecurityException("Package " + pkg.getPackageName() + " has not requested permission " + permName);
                }
                if (pkg.getTargetSdkVersion() < 23 && bp.isRuntime()) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                int flags = uidState.getPermissionFlags(permName);
                if ((flags & 16) != 0 && android.os.UserHandle.getCallingAppId() != 1000) {
                    throw new java.lang.SecurityException("Non-System UID cannot revoke system fixed permission " + permName + " for package " + packageName);
                }
                if (!overridePolicy && (flags & 4) != 0) {
                    throw new java.lang.SecurityException("Cannot revoke policy fixed permission " + permName + " for package " + packageName);
                }
                if (!uidState.revokePermission(bp)) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                if (isRuntimePermission) {
                    logPermission(1245, permName, packageName);
                }
                if (callback != null) {
                    if (isRuntimePermission) {
                        callback.onPermissionRevoked(android.os.UserHandle.getUid(userId, pkg.getUid()), userId, reason, overrideKill, permName);
                    } else {
                        this.mDefaultPermissionCallback.onInstallPermissionRevoked();
                    }
                }
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
    }

    private boolean mayManageRolePermission(int uid) {
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        java.lang.String[] packageNames = packageManager.getPackagesForUid(uid);
        if (packageNames == null) {
            return false;
        }
        java.lang.String permissionControllerPackageName = packageManager.getPermissionControllerPackageName();
        return java.util.Arrays.asList(packageNames).contains(permissionControllerPackageName);
    }

    private void resetRuntimePermissionsInternal(com.android.server.pm.pkg.AndroidPackage filterPkg, final int userId) throws java.lang.Throwable {
        final boolean[] permissionRemoved = new boolean[1];
        final android.util.ArraySet<java.lang.Long> revokedPermissions = new android.util.ArraySet<>();
        final android.util.ArraySet<java.lang.Integer> syncUpdatedUsers = new android.util.ArraySet<>();
        final android.util.ArraySet<java.lang.Integer> asyncUpdatedUsers = new android.util.ArraySet<>();
        final com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback delayingPermCallback = new com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onGidsChanged(int appId, int userId2) {
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onGidsChanged(appId, userId2);
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionChanged() {
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onPermissionChanged();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionGranted(int uid, int userId2) {
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onPermissionGranted(uid, userId2);
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onInstallPermissionGranted() {
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onInstallPermissionGranted();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionRevoked(int uid, int userId2, java.lang.String reason, boolean overrideKill, java.lang.String permissionName) {
                revokedPermissions.add(java.lang.Long.valueOf(com.android.internal.util.IntPair.of(uid, userId2)));
                syncUpdatedUsers.add(java.lang.Integer.valueOf(userId2));
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onInstallPermissionRevoked() {
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onInstallPermissionRevoked();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionUpdated(int[] userIds, boolean sync, int appId) {
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(appId);
                for (int userId2 : userIds) {
                    if (sync) {
                        syncUpdatedUsers.add(java.lang.Integer.valueOf(userId2));
                        asyncUpdatedUsers.remove(java.lang.Integer.valueOf(userId2));
                    } else if (syncUpdatedUsers.indexOf(java.lang.Integer.valueOf(userId2)) == -1) {
                        asyncUpdatedUsers.add(java.lang.Integer.valueOf(userId2));
                    }
                }
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionRemoved() {
                permissionRemoved[0] = true;
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onInstallPermissionUpdated() {
                com.android.server.pm.permission.PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onInstallPermissionUpdated();
            }
        };
        if (filterPkg != null) {
            lambda$resetRuntimePermissionsInternal$3(filterPkg, userId, delayingPermCallback);
        } else {
            this.mPackageManagerInt.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) throws java.lang.Throwable {
                    this.f$0.lambda$resetRuntimePermissionsInternal$3(userId, delayingPermCallback, (com.android.server.pm.pkg.AndroidPackage) obj);
                }
            });
        }
        if (permissionRemoved[0]) {
            this.mDefaultPermissionCallback.onPermissionRemoved();
        }
        if (!revokedPermissions.isEmpty()) {
            int numRevokedPermissions = revokedPermissions.size();
            for (int i = 0; i < numRevokedPermissions; i++) {
                final int revocationUID = com.android.internal.util.IntPair.first(revokedPermissions.valueAt(i).longValue());
                final int revocationUserId = com.android.internal.util.IntPair.second(revokedPermissions.valueAt(i).longValue());
                this.mOnPermissionChangeListeners.onPermissionsChanged(revocationUID);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.pm.permission.PermissionManagerServiceImpl.killUid(android.os.UserHandle.getAppId(revocationUID), revocationUserId, "permissions revoked");
                    }
                });
            }
        }
        this.mPackageManagerInt.writePermissionSettings(com.android.internal.util.ArrayUtils.convertToIntArray(syncUpdatedUsers), false);
        this.mPackageManagerInt.writePermissionSettings(com.android.internal.util.ArrayUtils.convertToIntArray(asyncUpdatedUsers), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: resetRuntimePermissionsInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$resetRuntimePermissionsInternal$3(com.android.server.pm.pkg.AndroidPackage pkg, int userId, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback delayingPermCallback) throws java.lang.Throwable {
        java.lang.String packageName = pkg.getPackageName();
        for (java.lang.String permName : pkg.getRequestedPermissions()) {
            if (!this.mIsLeanback || !NOTIFICATION_PERMISSIONS.contains(permName)) {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permName);
                        if (permission == null) {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        } else if (permission.isRemoved()) {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        } else {
                            boolean isRuntimePermission = permission.isRuntime();
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            java.lang.String[] pkgNames = this.mPackageManagerInt.getSharedUserPackagesForPackage(pkg.getPackageName(), userId);
                            int i = 0;
                            if (pkgNames.length > 0) {
                                boolean used = false;
                                int length = pkgNames.length;
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= length) {
                                        break;
                                    }
                                    java.lang.String sharedPkgName = pkgNames[i2];
                                    com.android.server.pm.pkg.AndroidPackage sharedPkg = this.mPackageManagerInt.getPackage(sharedPkgName);
                                    if (sharedPkg == null || sharedPkg.getPackageName().equals(packageName) || !sharedPkg.getRequestedPermissions().contains(permName)) {
                                        i2++;
                                    } else {
                                        used = true;
                                        break;
                                    }
                                }
                                if (used) {
                                }
                            }
                            int oldFlags = getPermissionFlagsInternal(packageName, permName, 1000, userId);
                            int uid = this.mPackageManagerInt.getPackageUid(packageName, 0L, userId);
                            int targetSdk = this.mPackageManagerInt.getUidTargetSdkVersion(uid);
                            if (targetSdk < 23 && isRuntimePermission) {
                                i = 72;
                            }
                            int flags = i;
                            updatePermissionFlagsInternal(packageName, permName, 589899, flags, 1000, userId, false, delayingPermCallback);
                            if (isRuntimePermission && (oldFlags & 20) == 0) {
                                if ((oldFlags & 32) != 0 || (oldFlags & 32768) != 0) {
                                    grantRuntimePermissionInternal(packageName, permName, false, 1000, userId, delayingPermCallback);
                                } else if ((flags & 64) == 0 && !isPermissionSplitFromNonRuntime(permName, targetSdk)) {
                                    revokeRuntimePermissionInternal(packageName, permName, false, 1000, userId, null, delayingPermCallback);
                                }
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
            }
        }
    }

    private boolean isPermissionSplitFromNonRuntime(java.lang.String permName, int targetSdk) {
        java.util.List<android.permission.PermissionManager.SplitPermissionInfo> splitPerms = getSplitPermissionInfos();
        int size = splitPerms.size();
        int i = 0;
        while (true) {
            boolean z = false;
            if (i >= size) {
                return false;
            }
            android.permission.PermissionManager.SplitPermissionInfo splitPerm = splitPerms.get(i);
            if (targetSdk >= splitPerm.getTargetSdk() || !splitPerm.getNewPermissions().contains(permName)) {
                i++;
            } else {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        com.android.server.pm.permission.Permission perm = this.mRegistry.getPermission(splitPerm.getSplitPermission());
                        if (perm != null && !perm.isRuntime()) {
                            z = true;
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return z;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00bf A[ORIG_RETURN, RETURN] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean shouldShowRequestPermissionRationale(java.lang.String r18, java.lang.String r19, java.lang.String r20, int r21) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 214
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.permission.PermissionManagerServiceImpl.shouldShowRequestPermissionRationale(java.lang.String, java.lang.String, java.lang.String, int):boolean");
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(java.lang.String packageName, java.lang.String permName, java.lang.String deviceId, int userId) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "isPermissionRevokedByPolicy for user " + userId);
        }
        if (checkPermission(packageName, permName, userId) == 0) {
            return false;
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (this.mPackageManagerInt.filterAppAccess(packageName, callingUid, userId, false)) {
            return false;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int flags = getPermissionFlagsInternal(packageName, permName, callingUid, userId);
            return (flags & 4) != 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public byte[] backupRuntimePermissions(int userId) {
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        final java.util.concurrent.CompletableFuture<byte[]> backup = new java.util.concurrent.CompletableFuture<>();
        android.permission.PermissionControllerManager permissionControllerManager = this.mPermissionControllerManager;
        android.os.UserHandle userHandleOf = android.os.UserHandle.of(userId);
        java.util.concurrent.Executor executor = com.android.server.PermissionThread.getExecutor();
        java.util.Objects.requireNonNull(backup);
        permissionControllerManager.getRuntimePermissionBackup(userHandleOf, executor, new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                backup.complete((byte[]) obj);
            }
        });
        try {
            return backup.get(BACKUP_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
            android.util.Slog.e(TAG, "Cannot create permission backup for user " + userId, e);
            return null;
        }
    }

    public void restoreRuntimePermissions(byte[] backup, int userId) {
        java.util.Objects.requireNonNull(backup, com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP);
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mHasNoDelayedPermBackup.delete(userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mPermissionControllerManager.stageAndApplyRuntimePermissionsBackup(backup, android.os.UserHandle.of(userId));
    }

    public void restoreDelayedRuntimePermissions(java.lang.String packageName, final int userId) {
        java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                if (this.mHasNoDelayedPermBackup.get(userId, false)) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                } else {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    this.mPermissionControllerManager.applyStagedRuntimePermissionBackup(packageName, android.os.UserHandle.of(userId), com.android.server.PermissionThread.getExecutor(), new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda14
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$restoreDelayedRuntimePermissions$5(userId, (java.lang.Boolean) obj);
                        }
                    });
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreDelayedRuntimePermissions$5(int userId, java.lang.Boolean hasMoreBackup) {
        if (hasMoreBackup.booleanValue()) {
            return;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mHasNoDelayedPermBackup.put(userId, true);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void revokeStoragePermissionsIfScopeExpandedInternal(com.android.server.pm.pkg.AndroidPackage newPackage, com.android.server.pm.pkg.AndroidPackage oldPackage) {
        android.content.pm.PermissionInfo permInfo;
        int userId;
        int i;
        int i2;
        int[] iArr;
        int userId2;
        boolean downgradedSdk = oldPackage.getTargetSdkVersion() >= 29 && newPackage.getTargetSdkVersion() < 29;
        boolean upgradedSdk = oldPackage.getTargetSdkVersion() < 29 && newPackage.getTargetSdkVersion() >= 29;
        boolean newlyRequestsLegacy = (upgradedSdk || oldPackage.isRequestLegacyExternalStorage() || !newPackage.isRequestLegacyExternalStorage()) ? false : true;
        if (!newlyRequestsLegacy && !downgradedSdk) {
            return;
        }
        int callingUid = android.os.Binder.getCallingUid();
        int[] allUserIds = getAllUserIds();
        int length = allUserIds.length;
        int i3 = 0;
        while (i3 < length) {
            int userId3 = allUserIds[i3];
            for (java.lang.String permName : newPackage.getRequestedPermissions()) {
                android.content.pm.PermissionInfo permInfo2 = getPermissionInfo(permName, 0, newPackage.getPackageName());
                if (permInfo2 != null) {
                    boolean isStorageOrMedia = STORAGE_PERMISSIONS.contains(permInfo2.name) || READ_MEDIA_AURAL_PERMISSIONS.contains(permInfo2.name) || READ_MEDIA_VISUAL_PERMISSIONS.contains(permInfo2.name);
                    if (isStorageOrMedia) {
                        boolean isSystemOrPolicyFixed = (getPermissionFlags(newPackage.getPackageName(), permInfo2.name, "default:0", userId3) & 20) != 0;
                        if (!isSystemOrPolicyFixed) {
                            android.util.EventLog.writeEvent(1397638484, "171430330", java.lang.Integer.valueOf(newPackage.getUid()), "Revoking permission " + permInfo2.name + " from package " + newPackage.getPackageName() + " as either the sdk downgraded " + downgradedSdk + " or newly requested legacy full storage " + newlyRequestsLegacy);
                            try {
                                permInfo = permInfo2;
                                userId = userId3;
                                i = i3;
                                i2 = length;
                                iArr = allUserIds;
                            } catch (java.lang.IllegalStateException | java.lang.SecurityException e) {
                                e = e;
                                permInfo = permInfo2;
                                userId = userId3;
                                i = i3;
                                i2 = length;
                                iArr = allUserIds;
                            }
                            try {
                                revokeRuntimePermissionInternal(newPackage.getPackageName(), permInfo2.name, false, callingUid, userId, null, this.mDefaultPermissionCallback);
                                userId2 = userId;
                            } catch (java.lang.IllegalStateException | java.lang.SecurityException e2) {
                                e = e2;
                                userId2 = userId;
                                android.util.Log.e(TAG, "unable to revoke " + permInfo.name + " for " + newPackage.getPackageName() + " user " + userId2, e);
                            }
                            userId3 = userId2;
                            allUserIds = iArr;
                            i3 = i;
                            length = i2;
                        }
                    }
                }
            }
            i3++;
        }
    }

    private void revokeSystemAlertWindowIfUpgradedPast23(com.android.server.pm.pkg.AndroidPackage newPackage, com.android.server.pm.pkg.AndroidPackage oldPackage) {
        com.android.server.pm.permission.Permission saw;
        if (oldPackage.getTargetSdkVersion() >= 23 || newPackage.getTargetSdkVersion() < 23 || !newPackage.getRequestedPermissions().contains("android.permission.SYSTEM_ALERT_WINDOW")) {
            return;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                saw = this.mRegistry.getPermission("android.permission.SYSTEM_ALERT_WINDOW");
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        com.android.server.pm.pkg.PackageStateInternal ps = this.mPackageManagerInt.getPackageStateInternal(newPackage.getPackageName());
        if (shouldGrantPermissionByProtectionFlags(newPackage, ps, saw, new android.util.ArraySet<>()) || shouldGrantPermissionBySignature(newPackage, saw)) {
            return;
        }
        for (int userId : getAllUserIds()) {
            try {
                revokePermissionFromPackageForUser(newPackage.getPackageName(), "android.permission.SYSTEM_ALERT_WINDOW", false, userId, this.mDefaultPermissionCallback);
            } catch (java.lang.IllegalStateException | java.lang.SecurityException e) {
                android.util.Log.e(TAG, "unable to revoke SYSTEM_ALERT_WINDOW for " + newPackage.getPackageName() + " user " + userId, e);
            }
        }
    }

    private void revokeRuntimePermissionsIfGroupChangedInternal(final com.android.server.pm.pkg.AndroidPackage newPackage, com.android.server.pm.pkg.AndroidPackage oldPackage) {
        int numOldPackagePermissions;
        com.android.server.pm.permission.PermissionManagerServiceImpl permissionManagerServiceImpl = this;
        int numOldPackagePermissions2 = com.android.internal.util.ArrayUtils.size(oldPackage.getPermissions());
        android.util.ArrayMap<java.lang.String, java.lang.String> oldPermissionNameToGroupName = new android.util.ArrayMap<>(numOldPackagePermissions2);
        for (int i = 0; i < numOldPackagePermissions2; i++) {
            com.android.internal.pm.pkg.component.ParsedPermission permission = (com.android.internal.pm.pkg.component.ParsedPermission) oldPackage.getPermissions().get(i);
            if (permission.getParsedPermissionGroup() != null) {
                oldPermissionNameToGroupName.put(permission.getName(), permission.getParsedPermissionGroup().getName());
            }
        }
        final int callingUid = android.os.Binder.getCallingUid();
        int numNewPackagePermissions = com.android.internal.util.ArrayUtils.size(newPackage.getPermissions());
        int newPermissionNum = 0;
        while (newPermissionNum < numNewPackagePermissions) {
            com.android.internal.pm.pkg.component.ParsedPermission newPermission = (com.android.internal.pm.pkg.component.ParsedPermission) newPackage.getPermissions().get(newPermissionNum);
            int newProtection = com.android.internal.pm.pkg.component.ParsedPermissionUtils.getProtection(newPermission);
            if ((newProtection & 1) == 0) {
                numOldPackagePermissions = numOldPackagePermissions2;
            } else {
                final java.lang.String permissionName = newPermission.getName();
                final java.lang.String newPermissionGroupName = newPermission.getParsedPermissionGroup() == null ? null : newPermission.getParsedPermissionGroup().getName();
                final java.lang.String oldPermissionGroupName = oldPermissionNameToGroupName.get(permissionName);
                if (newPermissionGroupName == null) {
                    numOldPackagePermissions = numOldPackagePermissions2;
                } else if (newPermissionGroupName.equals(oldPermissionGroupName)) {
                    numOldPackagePermissions = numOldPackagePermissions2;
                } else {
                    final int[] userIds = permissionManagerServiceImpl.mUserManagerInt.getUserIds();
                    numOldPackagePermissions = numOldPackagePermissions2;
                    permissionManagerServiceImpl.mPackageManagerInt.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda9
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$revokeRuntimePermissionsIfGroupChangedInternal$6(userIds, permissionName, newPackage, oldPermissionGroupName, newPermissionGroupName, callingUid, (com.android.server.pm.pkg.AndroidPackage) obj);
                        }
                    });
                }
            }
            newPermissionNum++;
            permissionManagerServiceImpl = this;
            numOldPackagePermissions2 = numOldPackagePermissions;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeRuntimePermissionsIfGroupChangedInternal$6(int[] userIds, java.lang.String permissionName, com.android.server.pm.pkg.AndroidPackage newPackage, java.lang.String oldPermissionGroupName, java.lang.String newPermissionGroupName, int callingUid, com.android.server.pm.pkg.AndroidPackage pkg) {
        java.lang.String packageName = pkg.getPackageName();
        for (int userId : userIds) {
            int permissionState = checkPermission(packageName, permissionName, userId);
            if (permissionState == 0) {
                android.util.EventLog.writeEvent(1397638484, "72710897", java.lang.Integer.valueOf(newPackage.getUid()), "Revoking permission " + permissionName + " from package " + packageName + " as the group changed from " + oldPermissionGroupName + " to " + newPermissionGroupName);
                try {
                    revokeRuntimePermissionInternal(packageName, permissionName, false, callingUid, userId, null, this.mDefaultPermissionCallback);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(TAG, "Could not revoke " + permissionName + " from " + packageName, e);
                }
            }
        }
    }

    private void revokeRuntimePermissionsIfPermissionDefinitionChangedInternal(java.util.List<java.lang.String> permissionsToRevoke) {
        final int[] userIds = this.mUserManagerInt.getUserIds();
        int numPermissions = permissionsToRevoke.size();
        final int callingUid = android.os.Binder.getCallingUid();
        for (int permNum = 0; permNum < numPermissions; permNum++) {
            final java.lang.String permName = permissionsToRevoke.get(permNum);
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permName);
                    if (bp == null || !(bp.isInternal() || bp.isRuntime())) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    } else {
                        final boolean isInternalPermission = bp.isInternal();
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        this.mPackageManagerInt.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.lambda$revokeRuntimePermissionsIfPermissionDefinitionChangedInternal$7(userIds, permName, isInternalPermission, callingUid, (com.android.server.pm.pkg.AndroidPackage) obj);
                            }
                        });
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeRuntimePermissionsIfPermissionDefinitionChangedInternal$7(int[] userIds, java.lang.String permName, boolean isInternalPermission, int callingUid, com.android.server.pm.pkg.AndroidPackage pkg) {
        java.lang.String str;
        com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback permissionCallback;
        java.lang.String packageName = pkg.getPackageName();
        int appId = pkg.getUid();
        if (appId < 10000) {
            return;
        }
        for (int userId : userIds) {
            int permissionState = checkPermission(packageName, permName, userId);
            int flags = getPermissionFlags(packageName, permName, "default:0", userId);
            if (permissionState == 0 && (flags & 32820) == 0) {
                int uid = android.os.UserHandle.getUid(userId, appId);
                if (isInternalPermission) {
                    android.util.EventLog.writeEvent(1397638484, "195338390", java.lang.Integer.valueOf(uid), "Revoking permission " + permName + " from package " + packageName + " due to definition change");
                } else {
                    android.util.EventLog.writeEvent(1397638484, "154505240", java.lang.Integer.valueOf(uid), "Revoking permission " + permName + " from package " + packageName + " due to definition change");
                    android.util.EventLog.writeEvent(1397638484, "168319670", java.lang.Integer.valueOf(uid), "Revoking permission " + permName + " from package " + packageName + " due to definition change");
                }
                android.util.Slog.e(TAG, "Revoking permission " + permName + " from package " + packageName + " due to definition change");
                try {
                    permissionCallback = this.mDefaultPermissionCallback;
                    str = TAG;
                } catch (java.lang.Exception e) {
                    e = e;
                    str = TAG;
                }
                try {
                    revokeRuntimePermissionInternal(packageName, permName, false, callingUid, userId, null, permissionCallback);
                } catch (java.lang.Exception e2) {
                    e = e2;
                    android.util.Slog.e(str, "Could not revoke " + permName + " from " + packageName, e);
                }
            }
        }
    }

    private java.util.List<java.lang.String> addAllPermissionsInternal(com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg) {
        android.content.pm.PermissionInfo permissionInfo;
        com.android.server.pm.permission.Permission oldPermission;
        int N = com.android.internal.util.ArrayUtils.size(pkg.getPermissions());
        java.util.ArrayList<java.lang.String> definitionChangedPermissions = new java.util.ArrayList<>();
        for (int i = 0; i < N; i++) {
            com.android.internal.pm.pkg.component.ParsedPermission p = (com.android.internal.pm.pkg.component.ParsedPermission) pkg.getPermissions().get(i);
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    if (pkg.getTargetSdkVersion() > 22) {
                        com.android.internal.pm.pkg.component.ComponentMutateUtils.setParsedPermissionGroup(p, this.mRegistry.getPermissionGroup(p.getGroup()));
                        if (com.android.server.pm.PackageManagerService.DEBUG_PERMISSIONS && p.getGroup() != null && p.getParsedPermissionGroup() == null) {
                            android.util.Slog.i(TAG, "Permission " + p.getName() + " from package " + p.getPackageName() + " in an unknown group " + p.getGroup());
                        }
                    }
                    permissionInfo = com.android.server.pm.parsing.PackageInfoUtils.generatePermissionInfo(p, 128L);
                    oldPermission = p.isTree() ? this.mRegistry.getPermissionTree(p.getName()) : this.mRegistry.getPermission(p.getName());
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            boolean isOverridingSystemPermission = com.android.server.pm.permission.Permission.isOverridingSystemPermission(oldPermission, permissionInfo, this.mPackageManagerInt);
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    com.android.server.pm.permission.Permission permission = com.android.server.pm.permission.Permission.createOrUpdate(oldPermission, permissionInfo, packageState, this.mRegistry.getPermissionTrees(), isOverridingSystemPermission);
                    if (p.isTree()) {
                        this.mRegistry.addPermissionTree(permission);
                    } else {
                        this.mRegistry.addPermission(permission);
                    }
                    if (permission.isDefinitionChanged()) {
                        definitionChangedPermissions.add(p.getName());
                        permission.setDefinitionChanged(false);
                    }
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
        return definitionChangedPermissions;
    }

    private void addAllPermissionGroupsInternal(com.android.server.pm.pkg.AndroidPackage pkg) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int N = com.android.internal.util.ArrayUtils.size(pkg.getPermissionGroups());
                java.lang.StringBuilder r = null;
                for (int i = 0; i < N; i++) {
                    com.android.internal.pm.pkg.component.ParsedPermissionGroup pg = (com.android.internal.pm.pkg.component.ParsedPermissionGroup) pkg.getPermissionGroups().get(i);
                    com.android.internal.pm.pkg.component.ParsedPermissionGroup cur = this.mRegistry.getPermissionGroup(pg.getName());
                    java.lang.String curPackageName = cur == null ? null : cur.getPackageName();
                    boolean isPackageUpdate = pg.getPackageName().equals(curPackageName);
                    if (cur == null || isPackageUpdate) {
                        this.mRegistry.addPermissionGroup(pg);
                        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                            if (r == null) {
                                r = new java.lang.StringBuilder(256);
                            } else {
                                r.append(' ');
                            }
                            if (isPackageUpdate) {
                                r.append("UPD:");
                            }
                            r.append(pg.getName());
                        }
                    } else {
                        android.util.Slog.w(TAG, "Permission group " + pg.getName() + " from package " + pg.getPackageName() + " ignored: original from " + cur.getPackageName());
                        if (com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                            if (r == null) {
                                r = new java.lang.StringBuilder(256);
                            } else {
                                r.append(' ');
                            }
                            r.append("DUP:");
                            r.append(pg.getName());
                        }
                    }
                }
                if (r != null && com.android.server.pm.PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                    android.util.Log.d(TAG, "  Permission Groups: " + ((java.lang.Object) r));
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void removeAllPermissionsInternal(com.android.server.pm.pkg.AndroidPackage pkg) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int n = com.android.internal.util.ArrayUtils.size(pkg.getPermissions());
                java.lang.StringBuilder r = null;
                for (int i = 0; i < n; i++) {
                    com.android.internal.pm.pkg.component.ParsedPermission p = (com.android.internal.pm.pkg.component.ParsedPermission) pkg.getPermissions().get(i);
                    com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(p.getName());
                    if (bp == null) {
                        bp = this.mRegistry.getPermissionTree(p.getName());
                    }
                    if (bp != null && bp.isPermission(p)) {
                        bp.setPermissionInfo(null);
                        if (com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                            if (r == null) {
                                r = new java.lang.StringBuilder(256);
                            } else {
                                r.append(' ');
                            }
                            r.append(p.getName());
                        }
                    }
                    if (com.android.internal.pm.pkg.component.ParsedPermissionUtils.isAppOp(p)) {
                        this.mRegistry.removeAppOpPermissionPackage(p.getName(), pkg.getPackageName());
                    }
                }
                if (r != null && com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                    android.util.Log.d(TAG, "  Permissions: " + ((java.lang.Object) r));
                }
                for (java.lang.String permissionName : pkg.getRequestedPermissions()) {
                    com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permissionName);
                    if (permission != null && permission.isAppOp()) {
                        this.mRegistry.removeAppOpPermissionPackage(permissionName, pkg.getPackageName());
                    }
                }
                if (0 != 0 && com.android.server.pm.PackageManagerService.DEBUG_REMOVE) {
                    android.util.Log.d(TAG, "  Permissions: " + ((java.lang.Object) null));
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int userId) {
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mState.removeUserState(userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private java.util.Set<java.lang.String> getGrantedPermissionsInternal(java.lang.String packageName, final int userId) {
        final com.android.server.pm.pkg.PackageStateInternal ps = this.mPackageManagerInt.getPackageStateInternal(packageName);
        if (ps == null) {
            return java.util.Collections.emptySet();
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(ps, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for " + packageName + " and user " + userId);
                    java.util.Set<java.lang.String> setEmptySet = java.util.Collections.emptySet();
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return setEmptySet;
                }
                if (!ps.getUserStateOrDefault(userId).isInstantApp()) {
                    java.util.Set<java.lang.String> grantedPermissions = uidState.getGrantedPermissions();
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return grantedPermissions;
                }
                java.util.Set<java.lang.String> instantPermissions = new android.util.ArraySet<>(uidState.getGrantedPermissions());
                instantPermissions.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return this.f$0.lambda$getGrantedPermissionsInternal$8(userId, ps, (java.lang.String) obj);
                    }
                });
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return instantPermissions;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getGrantedPermissionsInternal$8(int userId, com.android.server.pm.pkg.PackageStateInternal ps, java.lang.String permissionName) {
        com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permissionName);
        if (permission == null) {
            return true;
        }
        if (!permission.isInstant()) {
            android.util.EventLog.writeEvent(1397638484, "140256621", java.lang.Integer.valueOf(android.os.UserHandle.getUid(userId, ps.getAppId())), permissionName);
            return true;
        }
        return false;
    }

    private int[] getPermissionGidsInternal(java.lang.String permissionName, int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permissionName);
                if (permission == null) {
                    int[] iArr = libcore.util.EmptyArray.INT;
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return iArr;
                }
                int[] iArrComputeGids = permission.computeGids(userId);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return iArrComputeGids;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(20:524|79|80|(5:572|81|82|(5:552|84|(7:87|88|564|89|(3:625|91|628)(8:624|92|93|582|94|(4:100|(1:104)|105|(1:107))|108|627)|626|85)|623|111)(1:114)|115)|(16:117|(1:119)(2:120|(2:122|123))|534|128|129|580|130|131|(14:588|134|135|540|136|137|(1:139)(1:140)|574|141|(7:578|143|(1:151)(4:146|147|520|148)|(2:616|(1:158))(5:532|162|(2:166|(1:168)(1:170))|171|(6:615|178|(1:180)|181|182|(7:194|197|198|(1:200)(1:202)|203|(3:554|205|(13:209|558|333|(16:338|(7:576|340|(4:342|590|343|(0)(1:346))(1:349)|350|538|351|(4:353|(5:355|356|(1:358)|364|(0)(1:367))(1:359)|360|(2:364|(0)))(1:368))(1:373)|528|374|(3:562|376|(3:570|378|(3:384|385|(0))(1:381))(0))(1:405)|568|406|(2:408|(5:414|(1:416)(1:418)|419|420|421)(1:413))(1:422)|423|(1:425)|542|426|(1:428)(1:429)|430|431|619)(1:337)|536|397|(1:399)(1:400)|542|426|(0)(0)|430|431|619)(4:210|(12:212|(1:214)(1:216)|217|218|219|(1:221)(1:222)|223|550|224|225|(1:227)(1:228)|(1:(9:246|(1:252)(1:251)|526|253|(1:257)|258|(1:(1:271))(1:262)|272|(4:283|(3:312|(1:314)(1:315)|316)|(3:318|600|319)(1:320)|321)(5:276|(1:282)(0)|(3:312|(0)(0)|316)|(0)(0)|321))(1:(1:(9:548|234|(2:236|(1:238))(1:241)|(1:243)(1:244)|526|253|(2:255|257)|258|(2:260|(5:267|269|271|272|(5:274|283|(0)|(0)(0)|321)(0))(5:267|269|271|272|(0)(0)))(0))(0))(7:245|252|526|253|(0)|258|(0)(0))))(9:286|(3:522|288|(1:292))(1:295)|592|296|(1:300)|(1:306)|(0)|(0)(0)|321))(2:326|327)|431|619))(4:332|558|333|(17:336|338|(0)(0)|528|374|(0)(0)|568|406|(0)(0)|423|(0)|542|426|(0)(0)|430|431|619)(0))|618)(6:192|198|(0)(0)|203|(0)(0)|618))(3:617|174|(1:176)))|177|620|618)(0)|622|(5:594|515|621|516|517)|544|132)|613|448|(8:450|(0)(1:453)|598|462|(2:530|464)(1:466)|(8:477|478|586|479|480|560|481|482)(10:468|(1:475)(3:596|471|472)|477|478|586|479|480|560|481|482)|622|(5:594|515|621|516|517))(1:454)|455|(4:598|462|(0)(0)|(0)(0))(0)|622|(5:594|515|621|516|517))(1:126)|127|534|128|129|580|130|131|(2:544|132)|613|448|(0)(0)|455|(5:457|598|462|(0)(0)|(0)(0))(0)|622|(5:594|515|621|516|517)) */
    /* JADX WARN: Can't wrap try/catch for region: R(24:524|79|80|572|81|82|(5:552|84|(7:87|88|564|89|(3:625|91|628)(8:624|92|93|582|94|(4:100|(1:104)|105|(1:107))|108|627)|626|85)|623|111)(1:114)|115|(16:117|(1:119)(2:120|(2:122|123))|534|128|129|580|130|131|(14:588|134|135|540|136|137|(1:139)(1:140)|574|141|(7:578|143|(1:151)(4:146|147|520|148)|(2:616|(1:158))(5:532|162|(2:166|(1:168)(1:170))|171|(6:615|178|(1:180)|181|182|(7:194|197|198|(1:200)(1:202)|203|(3:554|205|(13:209|558|333|(16:338|(7:576|340|(4:342|590|343|(0)(1:346))(1:349)|350|538|351|(4:353|(5:355|356|(1:358)|364|(0)(1:367))(1:359)|360|(2:364|(0)))(1:368))(1:373)|528|374|(3:562|376|(3:570|378|(3:384|385|(0))(1:381))(0))(1:405)|568|406|(2:408|(5:414|(1:416)(1:418)|419|420|421)(1:413))(1:422)|423|(1:425)|542|426|(1:428)(1:429)|430|431|619)(1:337)|536|397|(1:399)(1:400)|542|426|(0)(0)|430|431|619)(4:210|(12:212|(1:214)(1:216)|217|218|219|(1:221)(1:222)|223|550|224|225|(1:227)(1:228)|(1:(9:246|(1:252)(1:251)|526|253|(1:257)|258|(1:(1:271))(1:262)|272|(4:283|(3:312|(1:314)(1:315)|316)|(3:318|600|319)(1:320)|321)(5:276|(1:282)(0)|(3:312|(0)(0)|316)|(0)(0)|321))(1:(1:(9:548|234|(2:236|(1:238))(1:241)|(1:243)(1:244)|526|253|(2:255|257)|258|(2:260|(5:267|269|271|272|(5:274|283|(0)|(0)(0)|321)(0))(5:267|269|271|272|(0)(0)))(0))(0))(7:245|252|526|253|(0)|258|(0)(0))))(9:286|(3:522|288|(1:292))(1:295)|592|296|(1:300)|(1:306)|(0)|(0)(0)|321))(2:326|327)|431|619))(4:332|558|333|(17:336|338|(0)(0)|528|374|(0)(0)|568|406|(0)(0)|423|(0)|542|426|(0)(0)|430|431|619)(0))|618)(6:192|198|(0)(0)|203|(0)(0)|618))(3:617|174|(1:176)))|177|620|618)(0)|622|(5:594|515|621|516|517)|544|132)|613|448|(8:450|(0)(1:453)|598|462|(2:530|464)(1:466)|(8:477|478|586|479|480|560|481|482)(10:468|(1:475)(3:596|471|472)|477|478|586|479|480|560|481|482)|622|(5:594|515|621|516|517))(1:454)|455|(4:598|462|(0)(0)|(0)(0))(0)|622|(5:594|515|621|516|517))(1:126)|127|534|128|129|580|130|131|(2:544|132)|613|448|(0)(0)|455|(5:457|598|462|(0)(0)|(0)(0))(0)|622|(5:594|515|621|516|517)) */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0bcd, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0bce, code lost:
    
        r28 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0be9, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0bea, code lost:
    
        r28 = r3;
     */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0486 A[Catch: all -> 0x03a0, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x03a0, blocks: (B:148:0x031c, B:154:0x036e, B:156:0x0378, B:158:0x037c, B:164:0x03c1, B:166:0x03cb, B:168:0x03d4, B:174:0x03fd, B:176:0x0401, B:180:0x043f, B:184:0x044f, B:186:0x0455, B:188:0x045b, B:190:0x0465, B:192:0x046b, B:200:0x0486, B:214:0x04e8), top: B:520:0x031c }] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x04af  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x054d  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0587 A[Catch: all -> 0x05e3, TryCatch #3 {all -> 0x05e3, blocks: (B:253:0x057f, B:255:0x0587, B:257:0x058b, B:258:0x058f, B:260:0x0593, B:262:0x059d, B:272:0x05b8, B:274:0x05bc, B:276:0x05c4, B:278:0x05c9, B:280:0x05cf, B:267:0x05aa, B:269:0x05b0), top: B:526:0x057f }] */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0593 A[Catch: all -> 0x05e3, TryCatch #3 {all -> 0x05e3, blocks: (B:253:0x057f, B:255:0x0587, B:257:0x058b, B:258:0x058f, B:260:0x0593, B:262:0x059d, B:272:0x05b8, B:274:0x05bc, B:276:0x05c4, B:278:0x05c9, B:280:0x05cf, B:267:0x05aa, B:269:0x05b0), top: B:526:0x057f }] */
    /* JADX WARN: Removed duplicated region for block: B:263:0x05a2  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x05bc A[Catch: all -> 0x05e3, TryCatch #3 {all -> 0x05e3, blocks: (B:253:0x057f, B:255:0x0587, B:257:0x058b, B:258:0x058f, B:260:0x0593, B:262:0x059d, B:272:0x05b8, B:274:0x05bc, B:276:0x05c4, B:278:0x05c9, B:280:0x05cf, B:267:0x05aa, B:269:0x05b0), top: B:526:0x057f }] */
    /* JADX WARN: Removed duplicated region for block: B:283:0x05dd  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x0664 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0670  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0674  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0679  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x0681  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x075d A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Removed duplicated region for block: B:338:0x077a A[Catch: all -> 0x0a01, TRY_LEAVE, TryCatch #19 {all -> 0x0a01, blocks: (B:333:0x0767, B:338:0x077a), top: B:558:0x0767 }] */
    /* JADX WARN: Removed duplicated region for block: B:367:0x07e2  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x082a  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x0862  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x08e2  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x08e8 A[Catch: all -> 0x09c7, TRY_LEAVE, TryCatch #24 {all -> 0x09c7, blocks: (B:406:0x08e4, B:408:0x08e8, B:414:0x0906, B:419:0x0916), top: B:568:0x08e4 }] */
    /* JADX WARN: Removed duplicated region for block: B:422:0x096d  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x097e  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x0986 A[Catch: all -> 0x09af, TryCatch #11 {all -> 0x09af, blocks: (B:426:0x0980, B:428:0x0986, B:430:0x098c, B:423:0x0978, B:421:0x0951), top: B:542:0x0980 }] */
    /* JADX WARN: Removed duplicated region for block: B:429:0x098b  */
    /* JADX WARN: Removed duplicated region for block: B:450:0x0ac8  */
    /* JADX WARN: Removed duplicated region for block: B:454:0x0ad0  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0b0a  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x0b0d  */
    /* JADX WARN: Removed duplicated region for block: B:477:0x0b31 A[Catch: all -> 0x0b9a, PHI: r8 r19
  0x0b31: PHI (r8v5 'updatedUserIds' int[]) = (r8v4 'updatedUserIds' int[]), (r8v4 'updatedUserIds' int[]), (r8v7 'updatedUserIds' int[]) binds: [B:467:0x0b0b, B:475:0x0b2e, B:472:0x0b16] A[DONT_GENERATE, DONT_INLINE]
  0x0b31: PHI (r19v3 'installPermissionsChanged' boolean) = 
  (r19v2 'installPermissionsChanged' boolean)
  (r19v4 'installPermissionsChanged' boolean)
  (r19v5 'installPermissionsChanged' boolean)
 binds: [B:467:0x0b0b, B:475:0x0b2e, B:472:0x0b16] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TRY_LEAVE, TryCatch #39 {all -> 0x0b9a, blocks: (B:462:0x0afb, B:477:0x0b31, B:455:0x0ad2), top: B:598:0x0afb }] */
    /* JADX WARN: Removed duplicated region for block: B:530:0x0b01 A[EXC_TOP_SPLITTER, PHI: r10 r15
  0x0b01: PHI (r10v4 boolean) = (r10v3 boolean), (r10v6 boolean) binds: [B:458:0x0ae2, B:463:0x0aff] A[DONT_GENERATE, DONT_INLINE]
  0x0b01: PHI (r15v8 'userState' com.android.server.pm.permission.UserPermissionState) = 
  (r15v7 'userState' com.android.server.pm.permission.UserPermissionState)
  (r15v10 'userState' com.android.server.pm.permission.UserPermissionState)
 binds: [B:458:0x0ae2, B:463:0x0aff] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:554:0x04b7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:562:0x0836 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:576:0x0780 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:588:0x02ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:598:0x0afb A[EXC_TOP_SPLITTER, PHI: r10 r15
  0x0afb: PHI (r10v6 boolean) = (r10v3 boolean), (r10v3 boolean), (r10v7 boolean) binds: [B:456:0x0adc, B:458:0x0ae2, B:453:0x0acd] A[DONT_GENERATE, DONT_INLINE]
  0x0afb: PHI (r15v10 'userState' com.android.server.pm.permission.UserPermissionState) = 
  (r15v7 'userState' com.android.server.pm.permission.UserPermissionState)
  (r15v7 'userState' com.android.server.pm.permission.UserPermissionState)
  (r15v11 'userState' com.android.server.pm.permission.UserPermissionState)
 binds: [B:456:0x0adc, B:458:0x0ae2, B:453:0x0acd] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void restorePermissionState(com.android.server.pm.pkg.AndroidPackage r45, boolean r46, java.lang.String r47, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback r48, int r49) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 3226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.permission.PermissionManagerServiceImpl.restorePermissionState(com.android.server.pm.pkg.AndroidPackage, boolean, java.lang.String, com.android.server.pm.permission.PermissionManagerServiceImpl$PermissionCallback, int):void");
    }

    private int[] getAllUserIds() {
        return com.android.server.pm.UserManagerService.getInstance().getUserIdsIncludingPreCreated();
    }

    private int[] revokePermissionsNoLongerImplicitLocked(com.android.server.pm.permission.UidPermissionState ps, java.lang.String packageName, java.util.Collection<java.lang.String> uidImplicitPermissions, int uidTargetSdkVersion, int userId, int[] updatedUserIds) {
        boolean supportsRuntimePermissions = uidTargetSdkVersion >= 23;
        int[] updatedUserIds2 = updatedUserIds;
        for (java.lang.String permission : ps.getGrantedPermissions()) {
            if (!uidImplicitPermissions.contains(permission)) {
                com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(permission);
                if (bp != null && bp.isRuntime()) {
                    int flags = ps.getPermissionFlags(permission);
                    if ((flags & 128) != 0) {
                        int flagsToRemove = 128;
                        boolean preserveGrant = false;
                        if (com.android.internal.util.ArrayUtils.contains(NEARBY_DEVICES_PERMISSIONS, permission) && ps.isPermissionGranted("android.permission.ACCESS_BACKGROUND_LOCATION") && (ps.getPermissionFlags("android.permission.ACCESS_BACKGROUND_LOCATION") & 136) == 0) {
                            preserveGrant = true;
                        }
                        if ((flags & 52) == 0 && supportsRuntimePermissions && !preserveGrant) {
                            if (ps.revokePermission(bp) && com.android.server.pm.PackageManagerService.DEBUG_PERMISSIONS) {
                                android.util.Slog.i(TAG, "Revoking runtime permission " + permission + " for " + packageName + " as it is now requested");
                            }
                            flagsToRemove = 128 | 3;
                        }
                        ps.updatePermissionFlags(bp, flagsToRemove, 0);
                        updatedUserIds2 = com.android.internal.util.ArrayUtils.appendInt(updatedUserIds2, userId);
                    }
                }
            }
        }
        return updatedUserIds2;
    }

    private void inheritPermissionStateToNewImplicitPermissionLocked(android.util.ArraySet<java.lang.String> sourcePerms, java.lang.String newPerm, com.android.server.pm.permission.UidPermissionState ps, com.android.server.pm.pkg.AndroidPackage pkg) {
        java.lang.String pkgName = pkg.getPackageName();
        boolean isGranted = false;
        int flags = 0;
        int numSourcePerm = sourcePerms.size();
        for (int i = 0; i < numSourcePerm; i++) {
            java.lang.String sourcePerm = sourcePerms.valueAt(i);
            if (ps.isPermissionGranted(sourcePerm)) {
                if (!isGranted) {
                    flags = 0;
                }
                isGranted = true;
                flags |= ps.getPermissionFlags(sourcePerm);
            } else if (!isGranted) {
                flags |= ps.getPermissionFlags(sourcePerm);
            }
        }
        if (isGranted) {
            if (com.android.server.pm.PackageManagerService.DEBUG_PERMISSIONS) {
                android.util.Slog.i(TAG, newPerm + " inherits runtime perm grant from " + sourcePerms + " for " + pkgName);
            }
            ps.grantPermission(this.mRegistry.getPermission(newPerm));
        }
        ps.updatePermissionFlags(this.mRegistry.getPermission(newPerm), flags, flags);
    }

    private int[] checkIfLegacyStorageOpsNeedToBeUpdated(com.android.server.pm.pkg.AndroidPackage pkg, boolean replace, int[] userIds, int[] updatedUserIds) {
        if (replace && pkg.isRequestLegacyExternalStorage() && (pkg.getRequestedPermissions().contains("android.permission.READ_EXTERNAL_STORAGE") || pkg.getRequestedPermissions().contains("android.permission.WRITE_EXTERNAL_STORAGE"))) {
            return (int[]) userIds.clone();
        }
        return updatedUserIds;
    }

    private int[] setInitialGrantForNewImplicitPermissionsLocked(com.android.server.pm.permission.UidPermissionState origPs, com.android.server.pm.permission.UidPermissionState ps, com.android.server.pm.pkg.AndroidPackage pkg, android.util.ArraySet<java.lang.String> newImplicitPermissions, int userId, int[] updatedUserIds) {
        android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> newToSplitPerms;
        java.util.List<android.permission.PermissionManager.SplitPermissionInfo> permissionList;
        int numSplitPerms;
        int numNewImplicitPerms;
        java.lang.String pkgName = pkg.getPackageName();
        android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> newToSplitPerms2 = new android.util.ArrayMap<>();
        java.util.List<android.permission.PermissionManager.SplitPermissionInfo> permissionList2 = getSplitPermissionInfos();
        int numSplitPerms2 = permissionList2.size();
        for (int splitPermNum = 0; splitPermNum < numSplitPerms2; splitPermNum++) {
            android.permission.PermissionManager.SplitPermissionInfo spi = permissionList2.get(splitPermNum);
            java.util.List<java.lang.String> newPerms = spi.getNewPermissions();
            int numNewPerms = newPerms.size();
            for (int newPermNum = 0; newPermNum < numNewPerms; newPermNum++) {
                java.lang.String newPerm = newPerms.get(newPermNum);
                android.util.ArraySet<java.lang.String> splitPerms = newToSplitPerms2.get(newPerm);
                if (splitPerms == null) {
                    splitPerms = new android.util.ArraySet<>();
                    newToSplitPerms2.put(newPerm, splitPerms);
                }
                splitPerms.add(spi.getSplitPermission());
            }
        }
        int numNewImplicitPerms2 = newImplicitPermissions.size();
        int newImplicitPermNum = 0;
        int[] updatedUserIds2 = updatedUserIds;
        while (newImplicitPermNum < numNewImplicitPerms2) {
            java.lang.String newPerm2 = newImplicitPermissions.valueAt(newImplicitPermNum);
            android.util.ArraySet<java.lang.String> sourcePerms = newToSplitPerms2.get(newPerm2);
            if (sourcePerms != null) {
                com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(newPerm2);
                if (bp == null) {
                    throw new java.lang.IllegalStateException("Unknown new permission in split permission: " + newPerm2);
                }
                if (bp.isRuntime()) {
                    if (!newPerm2.equals("android.permission.ACTIVITY_RECOGNITION") && !READ_MEDIA_AURAL_PERMISSIONS.contains(newPerm2) && !READ_MEDIA_VISUAL_PERMISSIONS.contains(newPerm2)) {
                        ps.updatePermissionFlags(bp, 128, 128);
                    }
                    updatedUserIds2 = com.android.internal.util.ArrayUtils.appendInt(updatedUserIds2, userId);
                    if (origPs.hasPermissionState(sourcePerms)) {
                        newToSplitPerms = newToSplitPerms2;
                        permissionList = permissionList2;
                        numSplitPerms = numSplitPerms2;
                        numNewImplicitPerms = numNewImplicitPerms2;
                    } else {
                        boolean inheritsFromInstallPerm = false;
                        newToSplitPerms = newToSplitPerms2;
                        int sourcePermNum = 0;
                        while (true) {
                            permissionList = permissionList2;
                            if (sourcePermNum >= sourcePerms.size()) {
                                numSplitPerms = numSplitPerms2;
                                numNewImplicitPerms = numNewImplicitPerms2;
                                break;
                            }
                            java.lang.String sourcePerm = sourcePerms.valueAt(sourcePermNum);
                            numSplitPerms = numSplitPerms2;
                            com.android.server.pm.permission.Permission sourceBp = this.mRegistry.getPermission(sourcePerm);
                            if (sourceBp == null) {
                                throw new java.lang.IllegalStateException("Unknown source permission in split permission: " + sourcePerm);
                            }
                            if (sourceBp.isRuntime()) {
                                sourcePermNum++;
                                permissionList2 = permissionList;
                                numSplitPerms2 = numSplitPerms;
                            } else {
                                inheritsFromInstallPerm = true;
                                numNewImplicitPerms = numNewImplicitPerms2;
                                break;
                            }
                        }
                        if (!inheritsFromInstallPerm) {
                            if (com.android.server.pm.PackageManagerService.DEBUG_PERMISSIONS) {
                                android.util.Slog.i(TAG, newPerm2 + " does not inherit from " + sourcePerms + " for " + pkgName + " as split permission is also new");
                            }
                        }
                    }
                    inheritPermissionStateToNewImplicitPermissionLocked(sourcePerms, newPerm2, ps, pkg);
                } else {
                    newToSplitPerms = newToSplitPerms2;
                    permissionList = permissionList2;
                    numSplitPerms = numSplitPerms2;
                    numNewImplicitPerms = numNewImplicitPerms2;
                }
            } else {
                newToSplitPerms = newToSplitPerms2;
                permissionList = permissionList2;
                numSplitPerms = numSplitPerms2;
                numNewImplicitPerms = numNewImplicitPerms2;
            }
            newImplicitPermNum++;
            permissionList2 = permissionList;
            newToSplitPerms2 = newToSplitPerms;
            numSplitPerms2 = numSplitPerms;
            numNewImplicitPerms2 = numNewImplicitPerms;
        }
        return updatedUserIds2;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> getSplitPermissions() {
        return android.permission.PermissionManager.splitPermissionInfoListToParcelableList(getSplitPermissionInfos());
    }

    private java.util.List<android.permission.PermissionManager.SplitPermissionInfo> getSplitPermissionInfos() {
        return com.android.server.SystemConfig.getInstance().getSplitPermissions();
    }

    private static boolean isCompatPlatformPermissionForPackage(java.lang.String perm, com.android.server.pm.pkg.AndroidPackage pkg) {
        int size = com.android.internal.pm.permission.CompatibilityPermissionInfo.COMPAT_PERMS.length;
        for (int i = 0; i < size; i++) {
            com.android.internal.pm.permission.CompatibilityPermissionInfo info = com.android.internal.pm.permission.CompatibilityPermissionInfo.COMPAT_PERMS[i];
            if (info.getName().equals(perm) && pkg.getTargetSdkVersion() < info.getSdkVersion()) {
                android.util.Log.i(TAG, "Auto-granting " + perm + " to old pkg " + pkg.getPackageName());
                return true;
            }
        }
        return false;
    }

    private boolean checkPrivilegedPermissionAllowlist(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal packageSetting, com.android.server.pm.permission.Permission permission) {
        if (com.android.internal.os.RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_DISABLE) {
            return true;
        }
        java.lang.String packageName = pkg.getPackageName();
        if (java.util.Objects.equals(packageName, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) || !packageSetting.isSystem() || !packageSetting.isPrivileged() || !this.mPrivilegedPermissionAllowlistSourcePackageNames.contains(permission.getPackageName())) {
            return true;
        }
        java.lang.String permissionName = permission.getName();
        java.lang.String containingApexPackageName = this.mApexManager.getActiveApexPackageNameContainingPackage(packageName);
        java.lang.Boolean allowlistState = getPrivilegedPermissionAllowlistState(packageSetting, permissionName, containingApexPackageName);
        if (allowlistState != null) {
            return allowlistState.booleanValue();
        }
        if (packageSetting.isUpdatedSystemApp()) {
            return true;
        }
        if (!this.mSystemReady) {
            boolean isInUpdatedApex = packageSetting.isApkInUpdatedApex();
            if (!isInUpdatedApex) {
                android.util.Slog.w(TAG, "Privileged permission " + permissionName + " for package " + packageName + " (" + pkg.getPath() + ") not in privapp-permissions allowlist");
                if (com.android.internal.os.RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_ENFORCE) {
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock) {
                        try {
                            if (this.mPrivappPermissionsViolations == null) {
                                this.mPrivappPermissionsViolations = new android.util.ArraySet<>();
                            }
                        } catch (java.lang.Throwable th) {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
        }
        boolean isInUpdatedApex2 = com.android.internal.os.RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_ENFORCE;
        return true ^ isInUpdatedApex2;
    }

    private java.lang.Boolean getPrivilegedPermissionAllowlistState(com.android.server.pm.pkg.PackageState packageState, java.lang.String permissionName, java.lang.String containingApexPackageName) {
        com.android.server.pm.permission.PermissionAllowlist permissionAllowlist = com.android.server.SystemConfig.getInstance().getPermissionAllowlist();
        java.lang.String packageName = packageState.getPackageName();
        if (packageState.isVendor() || packageState.isOdm()) {
            return permissionAllowlist.getVendorPrivilegedAppAllowlistState(packageName, permissionName);
        }
        if (packageState.isProduct()) {
            return permissionAllowlist.getProductPrivilegedAppAllowlistState(packageName, permissionName);
        }
        if (packageState.isSystemExt()) {
            return permissionAllowlist.getSystemExtPrivilegedAppAllowlistState(packageName, permissionName);
        }
        if (containingApexPackageName != null) {
            java.lang.Boolean nonApexAllowlistState = permissionAllowlist.getPrivilegedAppAllowlistState(packageName, permissionName);
            if (nonApexAllowlistState != null) {
                android.util.Slog.w(TAG, "Package " + packageName + " is an APK in APEX, but has permission allowlist on the system image. Please bundle the allowlist in the " + containingApexPackageName + " APEX instead.");
            }
            java.lang.String moduleName = this.mApexManager.getApexModuleNameForPackageName(containingApexPackageName);
            java.lang.Boolean apexAllowlistState = permissionAllowlist.getApexPrivilegedAppAllowlistState(moduleName, packageName, permissionName);
            if (apexAllowlistState != null) {
                return apexAllowlistState;
            }
            return nonApexAllowlistState;
        }
        return permissionAllowlist.getPrivilegedAppAllowlistState(packageName, permissionName);
    }

    private boolean shouldGrantPermissionBySignature(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.permission.Permission bp) {
        java.lang.String systemPackageName = (java.lang.String) com.android.internal.util.ArrayUtils.firstOrNull(this.mPackageManagerInt.getKnownPackageNames(0, 0));
        com.android.server.pm.pkg.AndroidPackage systemPackage = this.mPackageManagerInt.getPackage(systemPackageName);
        android.content.pm.SigningDetails sourceSigningDetails = getSourcePackageSigningDetails(bp);
        boolean shouldGrant = sourceSigningDetails.hasCommonSignerWithCapability(pkg.getSigningDetails(), 4) || pkg.getSigningDetails().hasAncestorOrSelf(systemPackage.getSigningDetails()) || systemPackage.getSigningDetails().checkCapability(pkg.getSigningDetails(), 4);
        if (shouldGrant || !this.mPermissionManagerServiceExt.hookShouldGrantPermissionBySignature(pkg, bp.getName(), false, bp.getPackageName())) {
            return shouldGrant;
        }
        return true;
    }

    private boolean shouldGrantPermissionByProtectionFlags(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.PackageStateInternal pkgSetting, com.android.server.pm.permission.Permission bp, android.util.ArraySet<java.lang.String> shouldGrantPrivilegedPermissionIfWasGranted) {
        boolean allowed = false;
        boolean isPrivilegedPermission = bp.isPrivileged();
        boolean isOemPermission = bp.isOem();
        if (0 == 0 && ((isPrivilegedPermission || isOemPermission) && pkgSetting.isSystem())) {
            java.lang.String permissionName = bp.getName();
            if (pkgSetting.isUpdatedSystemApp()) {
                com.android.server.pm.pkg.PackageStateInternal disabledPs = this.mPackageManagerInt.getDisabledSystemPackage(pkg.getPackageName());
                com.android.server.pm.pkg.AndroidPackage disabledPkg = disabledPs == null ? null : disabledPs.getPkg();
                if (disabledPkg != null && ((isPrivilegedPermission && disabledPs.isPrivileged()) || (isOemPermission && canGrantOemPermission(disabledPs, permissionName)))) {
                    if (disabledPkg.getRequestedPermissions().contains(permissionName)) {
                        allowed = true;
                    } else {
                        shouldGrantPrivilegedPermissionIfWasGranted.add(permissionName);
                    }
                }
            } else {
                allowed = (isPrivilegedPermission && pkgSetting.isPrivileged()) || (isOemPermission && canGrantOemPermission(pkgSetting, permissionName));
            }
            if (allowed && isPrivilegedPermission && !bp.isVendorPrivileged() && (pkgSetting.isVendor() || pkgSetting.isOdm())) {
                android.util.Slog.w(TAG, "Permission " + permissionName + " cannot be granted to privileged vendor apk " + pkg.getPackageName() + " because it isn't a 'vendorPrivileged' permission.");
                allowed = false;
            }
        }
        if (!allowed && bp.isPre23() && pkg.getTargetSdkVersion() < 23) {
            allowed = true;
        }
        if (!allowed && bp.isInstaller() && (com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(2, 0), pkg.getPackageName()) || com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(7, 0), pkg.getPackageName()))) {
            allowed = true;
        }
        if (!allowed && bp.isVerifier() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(4, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isPreInstalled() && pkgSetting.isSystem()) {
            allowed = true;
        }
        if (!allowed && bp.isKnownSigner()) {
            allowed = pkg.getSigningDetails().hasAncestorOrSelfWithDigest(bp.getKnownCerts());
        }
        if (!allowed && bp.isSetup() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(1, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isSystemTextClassifier() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(6, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isConfigurator() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(10, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isIncidentReportApprover() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(11, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isAppPredictor() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(12, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isCompanion() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(15, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isRetailDemo() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(16, 0), pkg.getPackageName()) && isProfileOwner(pkg.getUid())) {
            allowed = true;
        }
        if (!allowed && bp.isRecents() && com.android.internal.util.ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(17, 0), pkg.getPackageName())) {
            allowed = true;
        }
        if (!allowed && bp.isModule() && this.mApexManager.getActiveApexPackageNameContainingPackage(pkg.getPackageName()) != null) {
            return true;
        }
        return allowed;
    }

    private android.content.pm.SigningDetails getSourcePackageSigningDetails(com.android.server.pm.permission.Permission bp) {
        com.android.server.pm.pkg.PackageStateInternal ps = getSourcePackageSetting(bp);
        if (ps == null) {
            return android.content.pm.SigningDetails.UNKNOWN;
        }
        return ps.getSigningDetails();
    }

    private com.android.server.pm.pkg.PackageStateInternal getSourcePackageSetting(com.android.server.pm.permission.Permission bp) {
        java.lang.String sourcePackageName = bp.getPackageName();
        return this.mPackageManagerInt.getPackageStateInternal(sourcePackageName);
    }

    private static boolean canGrantOemPermission(com.android.server.pm.pkg.PackageState packageState, java.lang.String permission) {
        if (!packageState.isOem()) {
            return false;
        }
        java.lang.String packageName = packageState.getPackageName();
        java.lang.Boolean granted = com.android.server.SystemConfig.getInstance().getPermissionAllowlist().getOemAppAllowlistState(packageState.getPackageName(), permission);
        if (granted != null) {
            return java.lang.Boolean.TRUE == granted;
        }
        throw new java.lang.IllegalStateException("OEM permission " + permission + " requested by package " + packageName + " must be explicitly declared granted or not");
    }

    private static boolean isProfileOwner(int uid) {
        android.app.admin.DevicePolicyManagerInternal dpmInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        if (dpmInternal != null) {
            return dpmInternal.isActiveProfileOwner(uid) || dpmInternal.isActiveDeviceOwner(uid);
        }
        return false;
    }

    private boolean isPermissionsReviewRequiredInternal(java.lang.String packageName, int userId) {
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        if (pkg == null || pkg.getTargetSdkVersion() >= 23) {
            return false;
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(pkg, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for " + pkg.getPackageName() + " and user " + userId);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return false;
                }
                boolean zIsPermissionsReviewRequired = uidState.isPermissionsReviewRequired();
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return zIsPermissionsReviewRequired;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x005b, code lost:
    
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void grantRequestedPermissionsInternal(com.android.server.pm.pkg.AndroidPackage r25, android.util.ArrayMap<java.lang.String, java.lang.Integer> r26, int r27) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 304
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.permission.PermissionManagerServiceImpl.grantRequestedPermissionsInternal(com.android.server.pm.pkg.AndroidPackage, android.util.ArrayMap, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$grantRequestedPermissionsInternal$9(java.lang.String appOp, int uid, int mode) {
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        appOpsManager.setUidMode(appOp, uid, mode);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x006a, code lost:
    
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0079, code lost:
    
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007c, code lost:
    
        if (r3 == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007e, code lost:
    
        if (r7 != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0080, code lost:
    
        r7 = new android.util.ArraySet<>();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0086, code lost:
    
        r7.add(r9);
        r16 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x008c, code lost:
    
        r16 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008e, code lost:
    
        r7 = getPermissionFlagsInternal(r27.getPackageName(), r9, r14, r30);
        r1 = r7;
        r2 = 0;
        r17 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x009c, code lost:
    
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x009d, code lost:
    
        if (r17 == 0) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x009f, code lost:
    
        r3 = 1 << java.lang.Integer.numberOfTrailingZeros(r17);
        r17 = r17 & (~r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a7, code lost:
    
        switch(r3) {
            case 1: goto L131;
            case 2: goto L130;
            case 3: goto L142;
            case 4: goto L129;
            default: goto L142;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00ab, code lost:
    
        r2 = r2 | 8192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ad, code lost:
    
        if (r28 == null) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00b3, code lost:
    
        if (r28.contains(r9) == false) goto L133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b5, code lost:
    
        r1 = r1 | 8192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b8, code lost:
    
        r1 = r1 & (-8193);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00bb, code lost:
    
        r2 = r2 | 2048;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00bd, code lost:
    
        if (r28 == null) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00c3, code lost:
    
        if (r28.contains(r9) == false) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c5, code lost:
    
        r1 = r1 | 2048;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00c8, code lost:
    
        r1 = r1 & (-2049);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00cb, code lost:
    
        r2 = r2 | 4096;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00cd, code lost:
    
        if (r28 == null) goto L139;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00d3, code lost:
    
        if (r28.contains(r9) == false) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00d5, code lost:
    
        r1 = r1 | 4096;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00d8, code lost:
    
        r1 = r1 & (-4097);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00dc, code lost:
    
        if (r7 != r1) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00de, code lost:
    
        r7 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00e7, code lost:
    
        if ((r7 & 14336) == 0) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00e9, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00eb, code lost:
    
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00ec, code lost:
    
        r19 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00f0, code lost:
    
        if ((r1 & 14336) == 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00f3, code lost:
    
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00f4, code lost:
    
        r20 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00f8, code lost:
    
        if ((r7 & 4) == 0) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00fa, code lost:
    
        if (r20 != false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00fc, code lost:
    
        if (r3 == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00fe, code lost:
    
        r2 = r2 | 4;
        r1 = r1 & (-5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0108, code lost:
    
        if (r27.getTargetSdkVersion() >= 23) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x010a, code lost:
    
        if (r19 != false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x010c, code lost:
    
        if (r20 == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x010e, code lost:
    
        r2 = r2 | 64;
        r1 = r1 | 64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x011b, code lost:
    
        updatePermissionFlagsInternal(r27.getPackageName(), r9, r2, r1, r14, r30, false, null);
        r7 = r16;
        r8 = true;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:113:? -> B:82:0x014b). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void setAllowlistedRestrictedPermissionsInternal(com.android.server.pm.pkg.AndroidPackage r27, java.util.List<java.lang.String> r28, int r29, int r30) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 472
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.permission.PermissionManagerServiceImpl.setAllowlistedRestrictedPermissionsInternal(com.android.server.pm.pkg.AndroidPackage, java.util.List, int, int):void");
    }

    private void revokeSharedUserPermissionsForLeavingPackageInternal(com.android.server.pm.pkg.AndroidPackage pkg, final int appId, java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs, int userId) {
        if (pkg == null) {
            android.util.Slog.i(TAG, "Trying to update info for null package. Just ignoring");
            return;
        }
        if (sharedUserPkgs.isEmpty()) {
            return;
        }
        com.android.server.pm.pkg.PackageStateInternal disabledPs = this.mPackageManagerInt.getDisabledSystemPackage(pkg.getPackageName());
        boolean isShadowingSystemPkg = disabledPs != null && disabledPs.getAppId() == pkg.getUid();
        boolean shouldKillUid = false;
        for (java.lang.String eachPerm : pkg.getRequestedPermissions()) {
            boolean used = false;
            java.util.Iterator<com.android.server.pm.pkg.AndroidPackage> it = sharedUserPkgs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.pm.pkg.AndroidPackage sharedUserpkg = it.next();
                if (sharedUserpkg != null && !sharedUserpkg.getPackageName().equals(pkg.getPackageName()) && sharedUserpkg.getRequestedPermissions().contains(eachPerm)) {
                    used = true;
                    break;
                }
            }
            if (!used && (!isShadowingSystemPkg || !disabledPs.getPkg().getRequestedPermissions().contains(eachPerm))) {
                com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
                com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                synchronized (packageManagerTracedLock) {
                    try {
                        com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(appId, userId);
                        if (uidState == null) {
                            android.util.Slog.e(TAG, "Missing permissions state for " + pkg.getPackageName() + " and user " + userId);
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        } else {
                            com.android.server.pm.permission.Permission bp = this.mRegistry.getPermission(eachPerm);
                            if (bp == null) {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            } else {
                                if (uidState.removePermissionState(bp.getName()) && bp.hasGids()) {
                                    shouldKillUid = true;
                                }
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
            }
        }
        if (shouldKillUid) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.pm.permission.PermissionManagerServiceImpl.killUid(appId, -1, "permission grant or revoke changed gids");
                }
            });
        }
    }

    private boolean revokeUnusedSharedUserPermissionsLocked(java.util.Collection<java.lang.String> uidRequestedPermissions, com.android.server.pm.permission.UidPermissionState uidState) {
        com.android.server.pm.permission.Permission bp;
        boolean runtimePermissionChanged = false;
        java.util.List<com.android.server.pm.permission.PermissionState> permissionStates = uidState.getPermissionStates();
        int permissionStatesSize = permissionStates.size();
        for (int i = permissionStatesSize - 1; i >= 0; i--) {
            com.android.server.pm.permission.PermissionState permissionState = permissionStates.get(i);
            if (!uidRequestedPermissions.contains(permissionState.getName()) && (bp = this.mRegistry.getPermission(permissionState.getName())) != null && uidState.removePermissionState(bp.getName()) && bp.isRuntime()) {
                runtimePermissionChanged = true;
            }
        }
        return runtimePermissionChanged;
    }

    private void updatePermissions(java.lang.String packageName, com.android.server.pm.pkg.AndroidPackage pkg) throws java.lang.Throwable {
        int flags = pkg == null ? 3 : 2;
        updatePermissions(packageName, pkg, getVolumeUuidForPackage(pkg), flags, this.mDefaultPermissionCallback);
    }

    private void updateAllPermissions(java.lang.String volumeUuid, boolean fingerprintChanged) {
        int i;
        android.content.pm.PackageManager.corkPackageInfoCache();
        if (fingerprintChanged) {
            i = 6;
        } else {
            i = 0;
        }
        int flags = i | 1;
        try {
            updatePermissions(null, null, volumeUuid, flags, this.mDefaultPermissionCallback);
        } finally {
            android.content.pm.PackageManager.uncorkPackageInfoCache();
        }
    }

    private void updatePermissions(final java.lang.String changingPkgName, final com.android.server.pm.pkg.AndroidPackage changingPkg, final java.lang.String replaceVolumeUuid, int flags, final com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback) throws java.lang.Throwable {
        int flags2;
        boolean permissionTreesSourcePackageChanged = updatePermissionTreeSourcePackage(changingPkgName, changingPkg);
        boolean permissionSourcePackageChanged = updatePermissionSourcePackage(changingPkgName, callback);
        if (!(permissionTreesSourcePackageChanged | permissionSourcePackageChanged)) {
            flags2 = flags;
        } else {
            android.util.Slog.i(TAG, "Permission ownership changed. Updating all permissions.");
            flags2 = flags | 1;
        }
        updatePermissionGroupSourcePackage(changingPkgName);
        android.os.Trace.traceBegin(262144L, "restorePermissionState");
        if ((flags2 & 1) != 0) {
            final boolean replaceAll = (flags2 & 4) != 0;
            this.mPackageManagerInt.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) throws java.lang.Throwable {
                    this.f$0.lambda$updatePermissions$11(changingPkg, replaceAll, replaceVolumeUuid, changingPkgName, callback, (com.android.server.pm.pkg.AndroidPackage) obj);
                }
            });
        }
        if (changingPkg != null) {
            java.lang.String volumeUuid = getVolumeUuidForPackage(changingPkg);
            boolean replace = (flags2 & 2) != 0 && java.util.Objects.equals(replaceVolumeUuid, volumeUuid);
            restorePermissionState(changingPkg, replace, changingPkgName, callback, -1);
        }
        android.os.Trace.traceEnd(262144L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissions$11(com.android.server.pm.pkg.AndroidPackage changingPkg, boolean replaceAll, java.lang.String replaceVolumeUuid, java.lang.String changingPkgName, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback, com.android.server.pm.pkg.AndroidPackage pkg) throws java.lang.Throwable {
        if (pkg == changingPkg) {
            return;
        }
        java.lang.String volumeUuid = getVolumeUuidForPackage(pkg);
        boolean replace = replaceAll && java.util.Objects.equals(replaceVolumeUuid, volumeUuid);
        restorePermissionState(pkg, replace, changingPkgName, callback, -1);
    }

    private boolean updatePermissionSourcePackage(java.lang.String packageName, final com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback) {
        if (packageName == null) {
            return true;
        }
        boolean changed = false;
        java.util.Set<com.android.server.pm.permission.Permission> needsUpdate = null;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.server.pm.permission.Permission bp : this.mRegistry.getPermissions()) {
                    if (bp.isDynamic()) {
                        bp.updateDynamicPermission(this.mRegistry.getPermissionTrees());
                    }
                    if (packageName.equals(bp.getPackageName())) {
                        changed = true;
                        if (needsUpdate == null) {
                            needsUpdate = new android.util.ArraySet<>();
                        }
                        needsUpdate.add(bp);
                    }
                }
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (needsUpdate != null) {
            com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
            for (final com.android.server.pm.permission.Permission bp2 : needsUpdate) {
                if (pkg == null || !hasPermission(pkg, bp2.getName())) {
                    if (!isPermissionDeclaredByDisabledSystemPkg(bp2)) {
                        android.util.Slog.i(TAG, "Removing permission " + bp2.getName() + " that used to be declared by " + bp2.getPackageName());
                        if (bp2.isRuntime()) {
                            int[] userIds = this.mUserManagerInt.getUserIds();
                            for (final int userId : userIds) {
                                this.mPackageManagerInt.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda3
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        this.f$0.lambda$updatePermissionSourcePackage$12(bp2, userId, callback, (com.android.server.pm.pkg.AndroidPackage) obj);
                                    }
                                });
                            }
                        } else {
                            this.mPackageManagerInt.forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda4
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    this.f$0.lambda$updatePermissionSourcePackage$13(bp2, (com.android.server.pm.pkg.AndroidPackage) obj);
                                }
                            });
                        }
                    }
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock2) {
                        try {
                            this.mRegistry.removePermission(bp2.getName());
                        } finally {
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                } else {
                    com.android.server.pm.pkg.AndroidPackage sourcePkg = this.mPackageManagerInt.getPackage(bp2.getPackageName());
                    com.android.server.pm.pkg.PackageStateInternal sourcePs = this.mPackageManagerInt.getPackageStateInternal(bp2.getPackageName());
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock3) {
                        if (sourcePkg == null || sourcePs == null) {
                            android.util.Slog.w(TAG, "Removing dangling permission: " + bp2.getName() + " from package " + bp2.getPackageName());
                            this.mRegistry.removePermission(bp2.getName());
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        } else {
                            try {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            } finally {
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissionSourcePackage$12(com.android.server.pm.permission.Permission bp, int userId, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback, com.android.server.pm.pkg.AndroidPackage p) {
        revokePermissionFromPackageForUser(p.getPackageName(), bp.getName(), true, userId, callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissionSourcePackage$13(com.android.server.pm.permission.Permission bp, com.android.server.pm.pkg.AndroidPackage p) {
        int[] userIds = this.mUserManagerInt.getUserIds();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (int userId : userIds) {
                    com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(p, userId);
                    if (uidState == null) {
                        android.util.Slog.e(TAG, "Missing permissions state for " + p.getPackageName() + " and user " + userId);
                    } else {
                        uidState.removePermissionState(bp.getName());
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void updatePermissionGroupSourcePackage(java.lang.String packageName) {
        if (packageName == null) {
            return;
        }
        java.util.Set<com.android.internal.pm.pkg.component.ParsedPermissionGroup> needsUpdate = null;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.internal.pm.pkg.component.ParsedPermissionGroup bp : this.mRegistry.getPermissionGroups()) {
                    if (packageName.equals(bp.getPackageName())) {
                        if (needsUpdate == null) {
                            needsUpdate = new android.util.ArraySet<>();
                        }
                        needsUpdate.add(bp);
                    }
                }
            } finally {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (needsUpdate != null) {
            com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
            for (com.android.internal.pm.pkg.component.ParsedPermissionGroup bp2 : needsUpdate) {
                if (pkg == null) {
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock2) {
                        try {
                            android.util.Slog.w(TAG, "Removing permission group: " + bp2.getName() + " from package " + packageName + " that was not existed");
                            this.mRegistry.removePermissionGroup(bp2.getName());
                        } finally {
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                } else {
                    com.android.server.pm.pkg.AndroidPackage sourcePkg = this.mPackageManagerInt.getPackage(bp2.getPackageName());
                    com.android.server.pm.pkg.PackageStateInternal sourcePs = this.mPackageManagerInt.getPackageStateInternal(bp2.getPackageName());
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock3 = this.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock3) {
                        if (sourcePkg == null || sourcePs == null) {
                            android.util.Slog.w(TAG, "Removing dangling permission group: " + bp2.getName() + " from package " + bp2.getPackageName());
                            this.mRegistry.removePermissionGroup(bp2.getName());
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        } else {
                            try {
                                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            } finally {
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isPermissionDeclaredByDisabledSystemPkg(com.android.server.pm.permission.Permission permission) {
        com.android.server.pm.pkg.PackageStateInternal disabledSourcePs = this.mPackageManagerInt.getDisabledSystemPackage(permission.getPackageName());
        if (disabledSourcePs != null && disabledSourcePs.getPkg() != null) {
            java.lang.String permissionName = permission.getName();
            java.util.List<com.android.internal.pm.pkg.component.ParsedPermission> sourcePerms = disabledSourcePs.getPkg().getPermissions();
            for (com.android.internal.pm.pkg.component.ParsedPermission sourcePerm : sourcePerms) {
                if (android.text.TextUtils.equals(permissionName, sourcePerm.getName()) && permission.getProtectionLevel() == sourcePerm.getProtectionLevel()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void revokePermissionFromPackageForUser(java.lang.String pName, java.lang.String permissionName, boolean overridePolicy, int userId, com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback callback) {
        android.content.pm.ApplicationInfo appInfo = this.mPackageManagerInt.getApplicationInfo(pName, 0L, 1000, 0);
        if ((appInfo == null || appInfo.targetSdkVersion >= 23) && checkPermission(pName, permissionName, userId) == 0) {
            try {
                revokeRuntimePermissionInternal(pName, permissionName, overridePolicy, 1000, userId, null, callback);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "Failed to revoke " + permissionName + " from " + pName, e);
            }
        }
    }

    private boolean updatePermissionTreeSourcePackage(java.lang.String packageName, com.android.server.pm.pkg.AndroidPackage pkg) {
        if (packageName == null) {
            return true;
        }
        boolean changed = false;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                java.util.Iterator<com.android.server.pm.permission.Permission> it = this.mRegistry.getPermissionTrees().iterator();
                while (it.hasNext()) {
                    com.android.server.pm.permission.Permission bp = it.next();
                    if (packageName.equals(bp.getPackageName())) {
                        changed = true;
                        if (pkg == null || !hasPermission(pkg, bp.getName())) {
                            android.util.Slog.i(TAG, "Removing permission tree " + bp.getName() + " that used to be declared by " + bp.getPackageName());
                            it.remove();
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return changed;
    }

    private void enforceGrantRevokeRuntimePermissionPermissions(java.lang.String message) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.GRANT_RUNTIME_PERMISSIONS") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.REVOKE_RUNTIME_PERMISSIONS") != 0) {
            throw new java.lang.SecurityException(message + " requires android.permission.GRANT_RUNTIME_PERMISSIONS or android.permission.REVOKE_RUNTIME_PERMISSIONS");
        }
    }

    private void enforceGrantRevokeGetRuntimePermissionPermissions(java.lang.String message) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.GET_RUNTIME_PERMISSIONS") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.GRANT_RUNTIME_PERMISSIONS") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.REVOKE_RUNTIME_PERMISSIONS") != 0) {
            throw new java.lang.SecurityException(message + " requires android.permission.GRANT_RUNTIME_PERMISSIONS or android.permission.REVOKE_RUNTIME_PERMISSIONS or android.permission.GET_RUNTIME_PERMISSIONS");
        }
    }

    private void enforceCrossUserPermission(int callingUid, int userId, boolean requireFullPermission, boolean checkShell, java.lang.String message) {
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Invalid userId " + userId);
        }
        if (checkShell) {
            enforceShellRestriction("no_debugging_features", callingUid, userId);
        }
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (checkCrossUserPermission(callingUid, callingUserId, userId, requireFullPermission)) {
            return;
        }
        java.lang.String errorMessage = buildInvalidCrossUserPermissionMessage(callingUid, userId, message, requireFullPermission);
        android.util.Slog.w(TAG, errorMessage);
        throw new java.lang.SecurityException(errorMessage);
    }

    private void enforceShellRestriction(java.lang.String restriction, int callingUid, int userId) {
        if (callingUid == 2000) {
            if (userId >= 0 && this.mUserManagerInt.hasUserRestriction(restriction, userId)) {
                throw new java.lang.SecurityException("Shell does not have permission to access user " + userId);
            }
            if (userId < 0) {
                android.util.Slog.e(LOG_TAG, "Unable to check shell permission for user " + userId + "\n\t" + android.os.Debug.getCallers(3));
            }
        }
    }

    private boolean checkCrossUserPermission(int callingUid, int callingUserId, int userId, boolean requireFullPermission) {
        if (userId == callingUserId || callingUid == 1000 || callingUid == 0) {
            return true;
        }
        if (requireFullPermission) {
            return checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL");
        }
        return checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") || checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS");
    }

    private boolean checkCallingOrSelfPermission(java.lang.String permission) {
        return this.mContext.checkCallingOrSelfPermission(permission) == 0;
    }

    private static java.lang.String buildInvalidCrossUserPermissionMessage(int callingUid, int userId, java.lang.String message, boolean requireFullPermission) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (message != null) {
            builder.append(message);
            builder.append(": ");
        }
        builder.append("UID ");
        builder.append(callingUid);
        builder.append(" requires ");
        builder.append("android.permission.INTERACT_ACROSS_USERS_FULL");
        if (!requireFullPermission) {
            builder.append(" or ");
            builder.append("android.permission.INTERACT_ACROSS_USERS");
        }
        builder.append(" to access user ");
        builder.append(userId);
        builder.append(".");
        return builder.toString();
    }

    private int calculateCurrentPermissionFootprintLocked(com.android.server.pm.permission.Permission permissionTree) {
        int size = 0;
        for (com.android.server.pm.permission.Permission permission : this.mRegistry.getPermissions()) {
            size += permissionTree.calculateFootprint(permission);
        }
        return size;
    }

    private void enforcePermissionCapLocked(android.content.pm.PermissionInfo info, com.android.server.pm.permission.Permission tree) {
        if (tree.getUid() != 1000) {
            int curTreeSize = calculateCurrentPermissionFootprintLocked(tree);
            if (info.calculateFootprint() + curTreeSize > 32768) {
                throw new java.lang.SecurityException("Permission tree size cap exceeded");
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        updateAllPermissions(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, false);
        com.android.server.policy.PermissionPolicyInternal permissionPolicyInternal = (com.android.server.policy.PermissionPolicyInternal) com.android.server.LocalServices.getService(com.android.server.policy.PermissionPolicyInternal.class);
        permissionPolicyInternal.setOnInitializedCallback(new com.android.server.policy.PermissionPolicyInternal.OnInitializedCallback() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda18
            @Override // com.android.server.policy.PermissionPolicyInternal.OnInitializedCallback
            public final void onInitialized(int i) {
                this.f$0.lambda$onSystemReady$14(i);
            }
        });
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mSystemReady = true;
                if (this.mPrivappPermissionsViolations != null) {
                    throw new java.lang.IllegalStateException("Signature|privileged permissions not in privapp-permissions allowlist: " + this.mPrivappPermissionsViolations);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mPermissionControllerManager = new android.permission.PermissionControllerManager(this.mContext, com.android.server.PermissionThread.getHandler());
        this.mPermissionPolicyInternal = (com.android.server.policy.PermissionPolicyInternal) com.android.server.LocalServices.getService(com.android.server.policy.PermissionPolicyInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$14(int userId) {
        updateAllPermissions(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, false);
    }

    private static java.lang.String getVolumeUuidForPackage(com.android.server.pm.pkg.AndroidPackage pkg) {
        if (pkg == null) {
            return android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL;
        }
        if (pkg.isExternalStorage()) {
            if (android.text.TextUtils.isEmpty(pkg.getVolumeUuid())) {
                return "primary_physical";
            }
            return pkg.getVolumeUuid();
        }
        return android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL;
    }

    private static boolean hasPermission(com.android.server.pm.pkg.AndroidPackage pkg, java.lang.String permName) {
        if (pkg.getPermissions().isEmpty()) {
            return false;
        }
        for (int i = pkg.getPermissions().size() - 1; i >= 0; i--) {
            if (((com.android.internal.pm.pkg.component.ParsedPermission) pkg.getPermissions().get(i)).getName().equals(permName)) {
                return true;
            }
        }
        return false;
    }

    private void logPermission(int action, java.lang.String name, java.lang.String packageName) {
        android.metrics.LogMaker log = new android.metrics.LogMaker(action);
        log.setPackageName(packageName);
        log.addTaggedData(1241, name);
        this.mMetricsLogger.write(log);
    }

    private com.android.server.pm.permission.UidPermissionState getUidStateLocked(com.android.server.pm.pkg.PackageStateInternal ps, int userId) {
        return getUidStateLocked(ps.getAppId(), userId);
    }

    private com.android.server.pm.permission.UidPermissionState getUidStateLocked(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
        return getUidStateLocked(pkg.getUid(), userId);
    }

    private com.android.server.pm.permission.UidPermissionState getUidStateLocked(int appId, int userId) {
        com.android.server.pm.permission.UserPermissionState userState = this.mState.getUserState(userId);
        if (userState == null) {
            return null;
        }
        return userState.getUidState(appId);
    }

    private void removeUidStateAndResetPackageInstallPermissionsFixed(int appId, java.lang.String packageName, int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.UserPermissionState userState = this.mState.getUserState(userId);
                if (userState == null) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return;
                }
                userState.removeUidState(appId);
                userState.setInstallPermissionsFixed(packageName, false);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
        final int[] userIds = getAllUserIds();
        this.mPackageManagerInt.forEachPackageState(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$readLegacyPermissionStateTEMP$15(userIds, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$readLegacyPermissionStateTEMP$15(int[] userIds, com.android.server.pm.pkg.PackageStateInternal ps) {
        com.android.server.pm.permission.LegacyPermissionState legacyState;
        int appId = ps.getAppId();
        if (ps.hasSharedUser()) {
            int sharedUserId = ps.getSharedUserAppId();
            com.android.server.pm.pkg.SharedUserApi sharedUserApi = this.mPackageManagerInt.getSharedUserApi(sharedUserId);
            if (sharedUserApi == null) {
                android.util.Slog.wtf(TAG, "Missing shared user Api for " + sharedUserId);
                return;
            }
            legacyState = sharedUserApi.getSharedUserLegacyPermissionState();
        } else {
            legacyState = ps.getLegacyPermissionState();
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (int userId : userIds) {
                    com.android.server.pm.permission.UserPermissionState userState = this.mState.getOrCreateUserState(userId);
                    userState.setInstallPermissionsFixed(ps.getPackageName(), ps.isInstallPermissionsFixed());
                    com.android.server.pm.permission.UidPermissionState uidState = userState.getOrCreateUidState(appId);
                    uidState.reset();
                    uidState.setMissing(legacyState.isMissing(userId));
                    readLegacyPermissionStatesLocked(uidState, legacyState.getPermissionStates(userId));
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private void readLegacyPermissionStatesLocked(com.android.server.pm.permission.UidPermissionState uidState, java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> permissionStates) {
        for (com.android.server.pm.permission.LegacyPermissionState.PermissionState permissionState : permissionStates) {
            java.lang.String permissionName = permissionState.getName();
            com.android.server.pm.permission.Permission permission = this.mRegistry.getPermission(permissionName);
            if (permission == null) {
                android.util.Slog.w(TAG, "Unknown permission: " + permissionName);
            } else {
                uidState.putPermissionState(permission, permissionState.isGranted(), permissionState.getFlags());
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
        final int[] userIds;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                userIds = this.mState.getUserIds();
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        this.mPackageManagerInt.forEachPackageSetting(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) throws java.lang.Throwable {
                this.f$0.lambda$writeLegacyPermissionStateTEMP$16(userIds, (com.android.server.pm.PackageSetting) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeLegacyPermissionStateTEMP$16(int[] userIds, com.android.server.pm.PackageSetting ps) throws java.lang.Throwable {
        com.android.server.pm.permission.LegacyPermissionState legacyState;
        com.android.server.pm.permission.PermissionManagerServiceImpl permissionManagerServiceImpl = this;
        int[] iArr = userIds;
        com.android.server.pm.PackageSetting packageSetting = ps;
        int i = 0;
        packageSetting.setInstallPermissionsFixed(false);
        if (ps.hasSharedUser()) {
            int sharedUserId = ps.getSharedUserAppId();
            com.android.server.pm.pkg.SharedUserApi sharedUserApi = permissionManagerServiceImpl.mPackageManagerInt.getSharedUserApi(sharedUserId);
            if (sharedUserApi == null) {
                android.util.Slog.wtf(TAG, "Missing shared user Api for " + sharedUserId);
                return;
            }
            legacyState = sharedUserApi.getSharedUserLegacyPermissionState();
        } else {
            legacyState = ps.getLegacyPermissionState();
        }
        legacyState.reset();
        int appId = ps.getAppId();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = permissionManagerServiceImpl.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int length = iArr.length;
                while (i < length) {
                    int userId = iArr[i];
                    com.android.server.pm.permission.UserPermissionState userState = permissionManagerServiceImpl.mState.getUserState(userId);
                    if (userState == null) {
                        try {
                            android.util.Slog.e(TAG, "Missing user state for " + userId);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        }
                    } else {
                        if (userState.areInstallPermissionsFixed(ps.getPackageName())) {
                            packageSetting.setInstallPermissionsFixed(true);
                        }
                        com.android.server.pm.permission.UidPermissionState uidState = userState.getUidState(appId);
                        if (uidState == null) {
                            android.util.Slog.e(TAG, "Missing permission state for " + ps.getPackageName() + " and user " + userId);
                        } else {
                            legacyState.setMissing(uidState.isMissing(), userId);
                            java.util.List<com.android.server.pm.permission.PermissionState> permissionStates = uidState.getPermissionStates();
                            int permissionStatesSize = permissionStates.size();
                            int i2 = 0;
                            while (i2 < permissionStatesSize) {
                                com.android.server.pm.permission.PermissionState permissionState = permissionStates.get(i2);
                                int appId2 = appId;
                                com.android.server.pm.permission.LegacyPermissionState.PermissionState legacyPermissionState = new com.android.server.pm.permission.LegacyPermissionState.PermissionState(permissionState.getName(), permissionState.getPermission().isRuntime(), permissionState.isGranted(), permissionState.getFlags());
                                legacyState.putPermissionState(legacyPermissionState, userId);
                                i2++;
                                appId = appId2;
                            }
                        }
                    }
                    try {
                        i++;
                        permissionManagerServiceImpl = this;
                        iArr = userIds;
                        packageSetting = ps;
                        appId = appId;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                }
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        java.util.List<com.android.server.pm.permission.LegacyPermission> legacyPermissions;
        for (int readPermissionOrPermissionTree = 0; readPermissionOrPermissionTree < 2; readPermissionOrPermissionTree++) {
            if (readPermissionOrPermissionTree == 0) {
                legacyPermissions = legacyPermissionSettings.getPermissions();
            } else {
                legacyPermissions = legacyPermissionSettings.getPermissionTrees();
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    int legacyPermissionsSize = legacyPermissions.size();
                    for (int i = 0; i < legacyPermissionsSize; i++) {
                        com.android.server.pm.permission.LegacyPermission legacyPermission = legacyPermissions.get(i);
                        com.android.server.pm.permission.Permission permission = new com.android.server.pm.permission.Permission(legacyPermission.getPermissionInfo(), legacyPermission.getType());
                        if (readPermissionOrPermissionTree == 0) {
                            com.android.server.pm.permission.Permission configPermission = this.mRegistry.getPermission(permission.getName());
                            if (configPermission != null && configPermission.getType() == 1) {
                                permission.setGids(configPermission.getRawGids(), configPermission.areGidsPerUser());
                            }
                            this.mRegistry.addPermission(permission);
                        } else {
                            this.mRegistry.addPermissionTree(permission);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        java.util.Collection<com.android.server.pm.permission.Permission> permissions;
        for (int writePermissionOrPermissionTree = 0; writePermissionOrPermissionTree < 2; writePermissionOrPermissionTree++) {
            java.util.List<com.android.server.pm.permission.LegacyPermission> legacyPermissions = new java.util.ArrayList<>();
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                if (writePermissionOrPermissionTree == 0) {
                    try {
                        permissions = this.mRegistry.getPermissions();
                    } catch (java.lang.Throwable th) {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        throw th;
                    }
                } else {
                    permissions = this.mRegistry.getPermissionTrees();
                }
                for (com.android.server.pm.permission.Permission permission : permissions) {
                    com.android.server.pm.permission.LegacyPermission legacyPermission = new com.android.server.pm.permission.LegacyPermission(permission.getPermissionInfo(), permission.getType(), 0, libcore.util.EmptyArray.INT);
                    legacyPermissions.add(legacyPermission);
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (writePermissionOrPermissionTree == 0) {
                legacyPermissionSettings.replacePermissions(legacyPermissions);
            } else {
                legacyPermissionSettings.replacePermissionTrees(legacyPermissions);
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String getDefaultPermissionGrantFingerprint(int userId) {
        if (this.mPackageManagerInt.isPermissionUpgradeNeeded(userId)) {
            return null;
        }
        return android.os.Build.FINGERPRINT;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void setDefaultPermissionGrantFingerprint(java.lang.String fingerprint, int userId) {
    }

    private void onPackageAddedInternal(com.android.server.pm.pkg.PackageState packageState, final com.android.server.pm.pkg.AndroidPackage pkg, boolean isInstantApp, final com.android.server.pm.pkg.AndroidPackage oldPkg) {
        java.util.List<java.lang.String> permissionsWithChangedDefinition;
        if (!pkg.getAdoptPermissions().isEmpty()) {
            for (int i = pkg.getAdoptPermissions().size() - 1; i >= 0; i--) {
                java.lang.String origName = (java.lang.String) pkg.getAdoptPermissions().get(i);
                if (canAdoptPermissionsInternal(origName, pkg)) {
                    android.util.Slog.i(TAG, "Adopting permissions from " + origName + " to " + pkg.getPackageName());
                    com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
                    com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
                    synchronized (packageManagerTracedLock) {
                        try {
                            this.mRegistry.transferPermissions(origName, pkg.getPackageName());
                        } catch (java.lang.Throwable th) {
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
        }
        if (isInstantApp) {
            android.util.Slog.w(TAG, "Permission groups from package " + pkg.getPackageName() + " ignored: instant apps cannot define new permission groups.");
        } else {
            addAllPermissionGroupsInternal(pkg);
        }
        if (isInstantApp) {
            permissionsWithChangedDefinition = null;
            android.util.Slog.w(TAG, "Permissions from package " + pkg.getPackageName() + " ignored: instant apps cannot define new permissions.");
        } else {
            permissionsWithChangedDefinition = addAllPermissionsInternal(packageState, pkg);
        }
        final boolean hasOldPkg = oldPkg != null;
        final boolean hasPermissionDefinitionChanges = true ^ com.android.internal.util.CollectionUtils.isEmpty(permissionsWithChangedDefinition);
        if (hasOldPkg || hasPermissionDefinitionChanges) {
            final java.util.List<java.lang.String> list = permissionsWithChangedDefinition;
            android.os.AsyncTask.execute(new java.lang.Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPackageAddedInternal$17(hasOldPkg, pkg, oldPkg, hasPermissionDefinitionChanges, list);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPackageAddedInternal$17(boolean hasOldPkg, com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.AndroidPackage oldPkg, boolean hasPermissionDefinitionChanges, java.util.List permissionsWithChangedDefinition) {
        if (hasOldPkg) {
            revokeRuntimePermissionsIfGroupChangedInternal(pkg, oldPkg);
            revokeStoragePermissionsIfScopeExpandedInternal(pkg, oldPkg);
            revokeSystemAlertWindowIfUpgradedPast23(pkg, oldPkg);
        }
        if (hasPermissionDefinitionChanges) {
            revokeRuntimePermissionsIfPermissionDefinitionChangedInternal(permissionsWithChangedDefinition);
        }
    }

    private boolean canAdoptPermissionsInternal(java.lang.String oldPackageName, com.android.server.pm.pkg.AndroidPackage newPkg) {
        com.android.server.pm.pkg.PackageStateInternal oldPs = this.mPackageManagerInt.getPackageStateInternal(oldPackageName);
        if (oldPs == null) {
            return false;
        }
        if (!oldPs.isSystem()) {
            android.util.Slog.w(TAG, "Unable to update from " + oldPs.getPackageName() + " to " + newPkg.getPackageName() + ": old package not in system partition");
            return false;
        }
        if (this.mPackageManagerInt.getPackage(oldPs.getPackageName()) != null) {
            android.util.Slog.w(TAG, "Unable to update from " + oldPs.getPackageName() + " to " + newPkg.getPackageName() + ": old package still exists");
            return false;
        }
        return true;
    }

    private boolean isEffectivelyGranted(com.android.server.pm.permission.PermissionState state) {
        int flags = state.getFlags();
        if ((flags & 16) != 0) {
            return true;
        }
        if ((flags & 4) != 0) {
            return (flags & 8) == 0 && state.isGranted();
        }
        if ((65608 & flags) != 0) {
            return false;
        }
        return state.isGranted();
    }

    private android.util.Pair<java.lang.Boolean, java.lang.Integer> mergePermissionState(int appId, com.android.server.pm.permission.PermissionState srcState, com.android.server.pm.permission.PermissionState destState) {
        boolean effectivelyGranted;
        int newFlags;
        boolean newGrantState;
        int destFlags = destState.getFlags();
        boolean destIsGranted = isEffectivelyGranted(destState);
        int srcFlags = srcState.getFlags();
        boolean srcIsGranted = isEffectivelyGranted(srcState);
        int combinedFlags = destFlags | srcFlags;
        int newFlags2 = 0 | (524291 & destFlags) | (combinedFlags & 14336);
        if ((newFlags2 & 14336) == 0) {
            newFlags2 |= 16384;
        }
        int newFlags3 = newFlags2 | (combinedFlags & 32820);
        if ((combinedFlags & 32820) == 0) {
            newFlags3 |= combinedFlags & 128;
        }
        if ((newFlags3 & 20) == 0) {
            if ((557091 & newFlags3) == 0 && NOTIFICATION_PERMISSIONS.contains(srcState.getName())) {
                newFlags3 |= combinedFlags & 64;
            } else if ((32820 & newFlags3) == 0) {
                newFlags3 |= destFlags & 64;
            }
        }
        if ((newFlags3 & 16) != 0) {
            effectivelyGranted = true;
        } else if ((destFlags & 4) != 0) {
            effectivelyGranted = destIsGranted;
        } else {
            if ((srcFlags & 4) != 0) {
                effectivelyGranted = destIsGranted || srcIsGranted;
                if (destIsGranted != srcIsGranted) {
                    newFlags3 &= -5;
                }
            } else if ((destFlags & 32800) != 0) {
                effectivelyGranted = destIsGranted;
            } else if ((32800 & srcFlags) != 0) {
                effectivelyGranted = destIsGranted || srcIsGranted;
            } else if ((destFlags & 128) == 0 && (srcFlags & 128) != 0) {
                effectivelyGranted = destIsGranted || srcIsGranted;
                if (destIsGranted) {
                    newFlags3 &= -129;
                }
            } else {
                effectivelyGranted = destIsGranted;
            }
        }
        if (!effectivelyGranted) {
            newFlags = (newFlags3 | (131072 & combinedFlags)) & (-129);
        } else {
            newFlags = newFlags3 & (-65);
        }
        if (effectivelyGranted != destIsGranted) {
            newFlags &= -524292;
        }
        if (!effectivelyGranted && isPermissionSplitFromNonRuntime(srcState.getName(), this.mPackageManagerInt.getUidTargetSdkVersion(appId))) {
            newFlags |= 8;
            newGrantState = true;
        } else {
            newGrantState = effectivelyGranted;
        }
        return new android.util.Pair<>(java.lang.Boolean.valueOf(newGrantState), java.lang.Integer.valueOf(newFlags));
    }

    private void handleAppIdMigration(com.android.server.pm.pkg.AndroidPackage pkg, int previousAppId) throws java.lang.Throwable {
        com.android.server.pm.permission.UidPermissionState prevUidState;
        int[] iArr;
        int i;
        int[] iArr2;
        int i2;
        int userId;
        com.android.server.pm.permission.UidPermissionState uidState;
        com.android.server.pm.pkg.PackageStateInternal ps = this.mPackageManagerInt.getPackageStateInternal(pkg.getPackageName());
        int i3 = 0;
        if (ps.hasSharedUser()) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    int[] allUserIds = getAllUserIds();
                    int length = allUserIds.length;
                    while (i3 < length) {
                        int userId2 = allUserIds[i3];
                        com.android.server.pm.permission.UserPermissionState userState = this.mState.getOrCreateUserState(userId2);
                        com.android.server.pm.permission.UidPermissionState uidState2 = userState.getUidState(previousAppId);
                        if (uidState2 == null) {
                            iArr = allUserIds;
                            i = length;
                        } else {
                            com.android.server.pm.permission.UidPermissionState sharedUidState = userState.getUidState(ps.getAppId());
                            if (sharedUidState == null) {
                                userState.createUidStateWithExisting(ps.getAppId(), uidState2);
                                iArr = allUserIds;
                                i = length;
                            } else {
                                java.util.List<com.android.server.pm.permission.PermissionState> states = uidState2.getPermissionStates();
                                int count = states.size();
                                int i4 = 0;
                                while (i4 < count) {
                                    com.android.server.pm.permission.PermissionState srcState = states.get(i4);
                                    com.android.server.pm.permission.PermissionState destState = sharedUidState.getPermissionState(srcState.getName());
                                    if (destState != null) {
                                        iArr2 = allUserIds;
                                        android.util.Pair<java.lang.Boolean, java.lang.Integer> newState = mergePermissionState(ps.getAppId(), srcState, destState);
                                        i2 = length;
                                        userId = userId2;
                                        uidState = uidState2;
                                        sharedUidState.putPermissionState(srcState.getPermission(), ((java.lang.Boolean) newState.first).booleanValue(), ((java.lang.Integer) newState.second).intValue());
                                    } else {
                                        iArr2 = allUserIds;
                                        i2 = length;
                                        userId = userId2;
                                        uidState = uidState2;
                                        sharedUidState.putPermissionState(srcState.getPermission(), srcState.isGranted(), srcState.getFlags());
                                    }
                                    i4++;
                                    allUserIds = iArr2;
                                    length = i2;
                                    userId2 = userId;
                                    uidState2 = uidState;
                                }
                                iArr = allUserIds;
                                i = length;
                            }
                            userState.removeUidState(previousAppId);
                        }
                        i3++;
                        allUserIds = iArr;
                        length = i;
                    }
                } finally {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            return;
        }
        java.util.List<com.android.server.pm.pkg.AndroidPackage> origSharedUserPackages = this.mPackageManagerInt.getPackagesForAppId(previousAppId);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                try {
                    int[] allUserIds2 = getAllUserIds();
                    int length2 = allUserIds2.length;
                    while (i3 < length2) {
                        int userId3 = allUserIds2[i3];
                        com.android.server.pm.permission.UserPermissionState userState2 = this.mState.getUserState(userId3);
                        if (userState2 != null && (prevUidState = userState2.getUidState(previousAppId)) != null) {
                            userState2.createUidStateWithExisting(ps.getAppId(), prevUidState);
                            if (!origSharedUserPackages.isEmpty()) {
                                revokeSharedUserPermissionsForLeavingPackageInternal(pkg, previousAppId, origSharedUserPackages, userId3);
                            } else {
                                removeUidStateAndResetPackageInstallPermissionsFixed(previousAppId, pkg.getPackageName(), userId3);
                            }
                        }
                        i3++;
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    private void onPackageInstalledInternal(com.android.server.pm.pkg.AndroidPackage pkg, int previousAppId, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams params, int[] userIds) throws java.lang.Throwable {
        if (previousAppId != -1) {
            handleAppIdMigration(pkg, previousAppId);
        }
        updatePermissions(pkg.getPackageName(), pkg);
        for (int userId : userIds) {
            addAllowlistedRestrictedPermissionsInternal(pkg, params.getAllowlistedRestrictedPermissions(), 2, userId);
            grantRequestedPermissionsInternal(pkg, params.getPermissionStates(), userId);
        }
    }

    private void addAllowlistedRestrictedPermissionsInternal(com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<java.lang.String> allowlistedRestrictedPermissions, int flags, int userId) throws java.lang.Throwable {
        java.util.List<java.lang.String> permissions;
        java.util.List<java.lang.String> permissions2 = getAllowlistedRestrictedPermissionsInternal(pkg, flags, userId);
        if (permissions2 != null) {
            android.util.ArraySet<java.lang.String> permissionSet = new android.util.ArraySet<>(permissions2);
            permissionSet.addAll(allowlistedRestrictedPermissions);
            permissions = new java.util.ArrayList(permissionSet);
        } else {
            permissions = allowlistedRestrictedPermissions;
        }
        setAllowlistedRestrictedPermissionsInternal(pkg, permissions, flags, userId);
    }

    private void onPackageRemovedInternal(com.android.server.pm.pkg.AndroidPackage pkg) {
        removeAllPermissionsInternal(pkg);
    }

    private void onPackageUninstalledInternal(java.lang.String packageName, int appId, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs, int[] userIds) throws java.lang.Throwable {
        int i = 0;
        if (packageState.isSystem() && pkg != null && this.mPackageManagerInt.getPackage(packageName) != null) {
            int length = userIds.length;
            while (i < length) {
                resetRuntimePermissionsInternal(pkg, userIds[i]);
                i++;
            }
            return;
        }
        updatePermissions(packageName, null);
        int length2 = userIds.length;
        while (i < length2) {
            int userId = userIds[i];
            if (sharedUserPkgs.isEmpty()) {
                removeUidStateAndResetPackageInstallPermissionsFixed(appId, packageName, userId);
            } else {
                revokeSharedUserPermissionsForLeavingPackageInternal(pkg, appId, sharedUserPkgs, userId);
            }
            i++;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions() {
        java.util.List<com.android.server.pm.permission.LegacyPermission> legacyPermissions;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                legacyPermissions = new java.util.ArrayList<>();
                for (com.android.server.pm.permission.Permission permission : this.mRegistry.getPermissions()) {
                    com.android.server.pm.permission.LegacyPermission legacyPermission = new com.android.server.pm.permission.LegacyPermission(permission.getPermissionInfo(), permission.getType(), permission.getUid(), permission.getRawGids());
                    legacyPermissions.add(legacyPermission);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return legacyPermissions;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages() {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> deepClone;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> appOpPermissionPackages = this.mRegistry.getAllAppOpPermissionPackages();
                deepClone = new android.util.ArrayMap<>();
                int appOpPermissionPackagesSize = appOpPermissionPackages.size();
                for (int i = 0; i < appOpPermissionPackagesSize; i++) {
                    java.lang.String appOpPermission = appOpPermissionPackages.keyAt(i);
                    android.util.ArraySet<java.lang.String> packageNames = appOpPermissionPackages.valueAt(i);
                    deepClone.put(appOpPermission, new android.util.ArraySet<>((android.util.ArraySet) packageNames));
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return deepClone;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int appId) {
        int[] userIds;
        com.android.server.pm.permission.PermissionManagerServiceImpl permissionManagerServiceImpl = this;
        com.android.server.pm.permission.LegacyPermissionState legacyState = new com.android.server.pm.permission.LegacyPermissionState();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = permissionManagerServiceImpl.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                int[] userIds2 = permissionManagerServiceImpl.mState.getUserIds();
                int length = userIds2.length;
                int i = 0;
                while (i < length) {
                    int userId = userIds2[i];
                    com.android.server.pm.permission.UidPermissionState uidState = permissionManagerServiceImpl.getUidStateLocked(appId, userId);
                    if (uidState == null) {
                        android.util.Slog.e(TAG, "Missing permissions state for app ID " + appId + " and user ID " + userId);
                        userIds = userIds2;
                    } else {
                        java.util.List<com.android.server.pm.permission.PermissionState> permissionStates = uidState.getPermissionStates();
                        int permissionStatesSize = permissionStates.size();
                        int i2 = 0;
                        while (i2 < permissionStatesSize) {
                            com.android.server.pm.permission.PermissionState permissionState = permissionStates.get(i2);
                            com.android.server.pm.permission.LegacyPermissionState.PermissionState legacyPermissionState = new com.android.server.pm.permission.LegacyPermissionState.PermissionState(permissionState.getName(), permissionState.getPermission().isRuntime(), permissionState.isGranted(), permissionState.getFlags());
                            legacyState.putPermissionState(legacyPermissionState, userId);
                            i2++;
                            userIds2 = userIds2;
                        }
                        userIds = userIds2;
                    }
                    i++;
                    permissionManagerServiceImpl = this;
                    userIds2 = userIds;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return legacyState;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getGidsForUid(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        int userId = android.os.UserHandle.getUserId(uid);
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.permission.UidPermissionState uidState = getUidStateLocked(appId, userId);
                if (uidState == null) {
                    android.util.Slog.e(TAG, "Missing permissions state for app ID " + appId + " and user ID " + userId);
                    int[] iArr = EMPTY_INT_ARRAY;
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    return iArr;
                }
                int[] iArrComputeGids = uidState.computeGids(this.mGlobalGids, userId);
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                return iArrComputeGids;
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(java.lang.String packageName, int userId) {
        java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        return isPermissionsReviewRequiredInternal(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String packageName) {
        java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        java.util.Set<java.lang.String> installedPermissions = new android.util.ArraySet<>();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.server.pm.permission.Permission permission : this.mRegistry.getPermissions()) {
                    if (java.util.Objects.equals(permission.getPackageName(), packageName)) {
                        installedPermissions.add(permission.getName());
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return installedPermissions;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String packageName, int userId) {
        java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        return getGrantedPermissionsInternal(packageName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getPermissionGids(java.lang.String permissionName, int userId) {
        java.util.Objects.requireNonNull(permissionName, "permissionName");
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        return getPermissionGidsInternal(permissionName, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName) {
        java.util.Objects.requireNonNull(permissionName, "permissionName");
        return getAppOpPermissionPackagesInternal(permissionName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(java.lang.String volumeUuid, boolean fingerprintChanged) {
        updateAllPermissions(volumeUuid, fingerprintChanged);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage pkg, int userId) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(pkg, "pkg");
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        resetRuntimePermissionsInternal(pkg, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int userId) throws java.lang.Throwable {
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        resetRuntimePermissionsInternal(null, userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String permName) {
        com.android.server.pm.permission.Permission permission;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                permission = this.mRegistry.getPermission(permName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return permission;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int protection) {
        java.util.List<android.content.pm.PermissionInfo> matchingPermissions = new java.util.ArrayList<>();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.server.pm.permission.Permission permission : this.mRegistry.getPermissions()) {
                    if (permission.getProtection() == protection) {
                        matchingPermissions.add(permission.generatePermissionInfo(0));
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return matchingPermissions;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int protectionFlags) {
        java.util.List<android.content.pm.PermissionInfo> matchingPermissions = new java.util.ArrayList<>();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.server.pm.permission.Permission permission : this.mRegistry.getPermissions()) {
                    if ((permission.getProtectionFlags() & protectionFlags) == protectionFlags) {
                        matchingPermissions.add(permission.generatePermissionInfo(0));
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return matchingPermissions;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int userId) {
        com.android.internal.util.Preconditions.checkArgumentNonNegative(userId, "userId");
        updateAllPermissions(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, true);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(com.android.server.pm.pkg.PackageState packageState, boolean isInstantApp, com.android.server.pm.pkg.AndroidPackage oldPkg) {
        java.util.Objects.requireNonNull(packageState);
        com.android.server.pm.pkg.AndroidPackage pkg = packageState.getAndroidPackage();
        java.util.Objects.requireNonNull(pkg);
        onPackageAddedInternal(packageState, pkg, isInstantApp, oldPkg);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage pkg, int previousAppId, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams params, int userId) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(pkg, "pkg");
        java.util.Objects.requireNonNull(params, "params");
        com.android.internal.util.Preconditions.checkArgument(userId >= 0 || userId == -1, "userId");
        int[] userIds = userId == -1 ? getAllUserIds() : new int[]{userId};
        onPackageInstalledInternal(pkg, previousAppId, params, userIds);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage pkg) {
        java.util.Objects.requireNonNull(pkg);
        onPackageRemovedInternal(pkg);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageUninstalled(java.lang.String packageName, int appId, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs, int userId) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(packageState, "packageState");
        java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        java.util.Objects.requireNonNull(sharedUserPkgs, "sharedUserPkgs");
        com.android.internal.util.Preconditions.checkArgument(userId >= 0 || userId == -1, "userId");
        int[] userIds = userId == -1 ? getAllUserIds() : new int[]{userId};
        onPackageUninstalledInternal(packageName, appId, packageState, pkg, sharedUserPkgs, userIds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class PermissionCallback {
        private PermissionCallback() {
        }

        public void onGidsChanged(int appId, int userId) {
        }

        public void onPermissionChanged() {
        }

        public void onPermissionGranted(int uid, int userId) {
        }

        public void onInstallPermissionGranted() {
        }

        public void onPermissionRevoked(int uid, int userId, java.lang.String reason) {
            onPermissionRevoked(uid, userId, reason, false, null);
        }

        public void onPermissionRevoked(int uid, int userId, java.lang.String reason, boolean overrideKill, java.lang.String permissionName) {
        }

        public void onInstallPermissionRevoked() {
        }

        public void onPermissionUpdated(int[] userIds, boolean sync, int appId) {
        }

        public void onPermissionRemoved() {
        }

        public void onInstallPermissionUpdated() {
        }
    }

    private static final class OnPermissionChangeListeners extends android.os.Handler {
        private static final int MSG_ON_PERMISSIONS_CHANGED = 1;
        private final android.os.RemoteCallbackList<android.permission.IOnPermissionsChangeListener> mPermissionListeners;

        OnPermissionChangeListeners(android.os.Looper looper) {
            super(looper);
            this.mPermissionListeners = new android.os.RemoteCallbackList<>();
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int uid = msg.arg1;
                    handleOnPermissionsChanged(uid);
                    break;
            }
        }

        public void addListener(android.permission.IOnPermissionsChangeListener listener) {
            this.mPermissionListeners.register(listener);
        }

        public void removeListener(android.permission.IOnPermissionsChangeListener listener) {
            this.mPermissionListeners.unregister(listener);
        }

        public void onPermissionsChanged(int uid) {
            if (this.mPermissionListeners.getRegisteredCallbackCount() > 0) {
                obtainMessage(1, uid, 0).sendToTarget();
            }
        }

        private void handleOnPermissionsChanged(int uid) {
            int count = this.mPermissionListeners.beginBroadcast();
            for (int i = 0; i < count; i++) {
                try {
                    android.permission.IOnPermissionsChangeListener callback = this.mPermissionListeners.getBroadcastItem(i);
                    try {
                        callback.onPermissionsChanged(uid, "default:0");
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(com.android.server.pm.permission.PermissionManagerServiceImpl.TAG, "Permission listener is dead", e);
                    }
                } finally {
                    this.mPermissionListeners.finishBroadcast();
                }
            }
        }
    }
}
