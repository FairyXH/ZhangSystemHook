package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionManagerService extends android.permission.IPermissionManager.Stub {
    private static final java.lang.String LOG_TAG = com.android.server.pm.permission.PermissionManagerService.class.getSimpleName();
    private static final java.util.concurrent.ConcurrentHashMap<android.os.IBinder, com.android.server.pm.permission.PermissionManagerService.RegisteredAttribution> sRunningAttributionSources = new java.util.concurrent.ConcurrentHashMap<>();
    private final android.app.AppOpsManager mAppOpsManager;
    private final com.android.server.pm.permission.PermissionManagerService.AttributionSourceRegistry mAttributionSourceRegistry;
    private com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate mCheckPermissionDelegate;
    private final android.content.Context mContext;
    private com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider mHotwordDetectionServiceProvider;
    private final android.content.pm.PackageManagerInternal mPackageManagerInt;
    private final com.android.server.pm.permission.PermissionManagerServiceInterface mPermissionManagerServiceImpl;
    private com.android.server.companion.virtual.VirtualDeviceManagerInternal mVirtualDeviceManagerInternal;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.pm.permission.OneTimePermissionUserManager> mOneTimePermissionUserManagers = new android.util.SparseArray<>();
    public com.android.server.pm.permission.IPermissionManagerServiceExt mPermissionManagerServiceExt = (com.android.server.pm.permission.IPermissionManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.permission.IPermissionManagerServiceExt.class).base(this).create();

    PermissionManagerService(android.content.Context context, android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> availableFeatures) {
        android.content.pm.PackageManager.invalidatePackageInfoCache();
        android.permission.PermissionManager.disablePackageNamePermissionCache();
        this.mContext = context;
        this.mPackageManagerInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mAppOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mVirtualDeviceManagerInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        this.mAttributionSourceRegistry = new com.android.server.pm.permission.PermissionManagerService.AttributionSourceRegistry(context);
        com.android.server.pm.permission.PermissionManagerService.PermissionManagerServiceInternalImpl localService = new com.android.server.pm.permission.PermissionManagerService.PermissionManagerServiceInternalImpl();
        com.android.server.LocalServices.addService(com.android.server.pm.permission.PermissionManagerServiceInternal.class, localService);
        com.android.server.LocalServices.addService(android.permission.PermissionManagerInternal.class, localService);
        if (android.permission.PermissionManager.USE_ACCESS_CHECKING_SERVICE) {
            this.mPermissionManagerServiceImpl = (com.android.server.pm.permission.PermissionManagerServiceInterface) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInterface.class);
        } else {
            this.mPermissionManagerServiceImpl = new com.android.server.pm.permission.PermissionManagerServiceImpl(context, availableFeatures);
        }
        this.mPermissionManagerServiceExt.hookPermissionManagerService(context, this);
    }

    public static com.android.server.pm.permission.PermissionManagerServiceInternal create(android.content.Context context, android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> availableFeatures) {
        com.android.server.pm.permission.PermissionManagerServiceInternal permMgrInt = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
        if (permMgrInt != null) {
            return permMgrInt;
        }
        com.android.server.pm.permission.PermissionManagerService permissionService = (com.android.server.pm.permission.PermissionManagerService) android.os.ServiceManager.getService("permissionmgr");
        if (permissionService == null) {
            android.os.ServiceManager.addService("permissionmgr", new com.android.server.pm.permission.PermissionManagerService(context, availableFeatures));
            android.os.ServiceManager.addService("permission_checker", new com.android.server.pm.permission.PermissionManagerService.PermissionCheckerService(context));
        }
        return (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
    }

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

    public int checkPermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String persistentDeviceId, int userId) {
        com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate checkPermissionDelegate;
        if (packageName == null || permissionName == null) {
            return -1;
        }
        synchronized (this.mLock) {
            checkPermissionDelegate = this.mCheckPermissionDelegate;
        }
        if (checkPermissionDelegate == null) {
            return this.mPermissionManagerServiceImpl.checkPermission(packageName, permissionName, persistentDeviceId, userId);
        }
        final com.android.server.pm.permission.PermissionManagerServiceInterface permissionManagerServiceInterface = this.mPermissionManagerServiceImpl;
        java.util.Objects.requireNonNull(permissionManagerServiceInterface);
        return checkPermissionDelegate.checkPermission(packageName, permissionName, persistentDeviceId, userId, new com.android.internal.util.function.QuadFunction() { // from class: com.android.server.pm.permission.PermissionManagerService$$ExternalSyntheticLambda0
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                return java.lang.Integer.valueOf(permissionManagerServiceInterface.checkPermission((java.lang.String) obj, (java.lang.String) obj2, (java.lang.String) obj3, ((java.lang.Integer) obj4).intValue()));
            }
        });
    }

    public int checkUidPermission(int uid, java.lang.String permissionName, int deviceId) {
        com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate checkPermissionDelegate;
        if (permissionName == null) {
            return -1;
        }
        java.lang.String persistentDeviceId = getPersistentDeviceId(deviceId);
        synchronized (this.mLock) {
            checkPermissionDelegate = this.mCheckPermissionDelegate;
        }
        if (2000 == uid && this.mPermissionManagerServiceExt.hookCheckUidPermissionImpl(this.mContext, permissionName, uid)) {
            return -1;
        }
        if (checkPermissionDelegate == null) {
            return this.mPermissionManagerServiceImpl.checkUidPermission(uid, permissionName, persistentDeviceId);
        }
        final com.android.server.pm.permission.PermissionManagerServiceInterface permissionManagerServiceInterface = this.mPermissionManagerServiceImpl;
        java.util.Objects.requireNonNull(permissionManagerServiceInterface);
        return checkPermissionDelegate.checkUidPermission(uid, permissionName, persistentDeviceId, new com.android.internal.util.function.TriFunction() { // from class: com.android.server.pm.permission.PermissionManagerService$$ExternalSyntheticLambda1
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return java.lang.Integer.valueOf(permissionManagerServiceInterface.checkUidPermission(((java.lang.Integer) obj).intValue(), (java.lang.String) obj2, (java.lang.String) obj3));
            }
        });
    }

    private java.lang.String getPersistentDeviceId(int deviceId) {
        if (deviceId == 0) {
            return "default:0";
        }
        if (this.mVirtualDeviceManagerInternal == null) {
            this.mVirtualDeviceManagerInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        }
        return this.mVirtualDeviceManagerInternal.getPersistentIdForDevice(deviceId);
    }

    public java.util.Map<java.lang.String, android.permission.PermissionManager.PermissionState> getAllPermissionStates(java.lang.String packageName, java.lang.String persistentDeviceId, int userId) {
        return this.mPermissionManagerServiceImpl.getAllPermissionStates(packageName, persistentDeviceId, userId);
    }

    public boolean setAutoRevokeExempted(java.lang.String packageName, boolean exempted, int userId) {
        java.util.Objects.requireNonNull(packageName);
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        int callingUid = android.os.Binder.getCallingUid();
        if (!checkAutoRevokeAccess(pkg, callingUid)) {
            return false;
        }
        return setAutoRevokeExemptedInternal(pkg, exempted, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setAutoRevokeExemptedInternal(com.android.server.pm.pkg.AndroidPackage pkg, boolean exempted, int userId) {
        int packageUid = android.os.UserHandle.getUid(userId, pkg.getUid());
        android.content.AttributionSource attributionSource = new android.content.AttributionSource(packageUid, pkg.getPackageName(), null);
        if (this.mAppOpsManager.checkOpNoThrow(98, attributionSource) != 0) {
            return false;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mAppOpsManager.setMode(97, packageUid, pkg.getPackageName(), exempted ? 1 : 0);
            return true;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCheckPermissionDelegateInternal(com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate delegate) {
        synchronized (this.mLock) {
            this.mCheckPermissionDelegate = delegate;
        }
    }

    private boolean checkAutoRevokeAccess(com.android.server.pm.pkg.AndroidPackage pkg, int callingUid) {
        boolean isCallerPrivileged = this.mContext.checkCallingOrSelfPermission("android.permission.WHITELIST_AUTO_REVOKE_PERMISSIONS") == 0;
        boolean isCallerInstallerOnRecord = this.mPackageManagerInt.isCallerInstallerOfRecord(pkg, callingUid);
        if (isCallerPrivileged || isCallerInstallerOnRecord) {
            return (pkg == null || this.mPackageManagerInt.filterAppAccess(pkg, callingUid, android.os.UserHandle.getUserId(callingUid))) ? false : true;
        }
        throw new java.lang.SecurityException("Caller must either hold android.permission.WHITELIST_AUTO_REVOKE_PERMISSIONS or be the installer on record");
    }

    public boolean isAutoRevokeExempted(java.lang.String packageName, int userId) {
        java.util.Objects.requireNonNull(packageName);
        com.android.server.pm.pkg.AndroidPackage pkg = this.mPackageManagerInt.getPackage(packageName);
        int callingUid = android.os.Binder.getCallingUid();
        if (!checkAutoRevokeAccess(pkg, callingUid)) {
            return false;
        }
        int packageUid = android.os.UserHandle.getUid(userId, pkg.getUid());
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.content.AttributionSource attributionSource = new android.content.AttributionSource(packageUid, packageName, null);
            return this.mAppOpsManager.checkOpNoThrow(97, attributionSource) == 1;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private com.android.server.pm.permission.OneTimePermissionUserManager getOneTimePermissionUserManager(int userId) {
        synchronized (this.mLock) {
            com.android.server.pm.permission.OneTimePermissionUserManager oneTimePermissionUserManager = this.mOneTimePermissionUserManagers.get(userId);
            if (oneTimePermissionUserManager != null) {
                return oneTimePermissionUserManager;
            }
            com.android.server.pm.permission.OneTimePermissionUserManager newOneTimePermissionUserManager = new com.android.server.pm.permission.OneTimePermissionUserManager(this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0));
            synchronized (this.mLock) {
                com.android.server.pm.permission.OneTimePermissionUserManager oneTimePermissionUserManager2 = this.mOneTimePermissionUserManagers.get(userId);
                if (oneTimePermissionUserManager2 != null) {
                    return oneTimePermissionUserManager2;
                }
                this.mOneTimePermissionUserManagers.put(userId, newOneTimePermissionUserManager);
                newOneTimePermissionUserManager.registerUninstallListener();
                return newOneTimePermissionUserManager;
            }
        }
    }

    public void startOneTimePermissionSession(java.lang.String packageName, int deviceId, int userId, long timeoutMillis, long revokeAfterKilledDelayMillis) {
        startOneTimePermissionSession_enforcePermission();
        java.util.Objects.requireNonNull(packageName);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            getOneTimePermissionUserManager(userId).startPackageOneTimeSession(packageName, deviceId, timeoutMillis, revokeAfterKilledDelayMillis);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void stopOneTimePermissionSession(java.lang.String packageName, int userId) {
        super.stopOneTimePermissionSession_enforcePermission();
        java.util.Objects.requireNonNull(packageName);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            getOneTimePermissionUserManager(userId).stopPackageOneTimeSession(packageName);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public android.os.IBinder registerAttributionSource(android.content.AttributionSourceState source) {
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.serverSideAttributionRegistration()) {
            android.os.Binder token = new android.os.Binder();
            this.mAttributionSourceRegistry.registerAttributionSource(new android.content.AttributionSource(source).withToken(token));
            return token;
        }
        this.mAttributionSourceRegistry.registerAttributionSource(new android.content.AttributionSource(source));
        return source.token;
    }

    public boolean isRegisteredAttributionSource(android.content.AttributionSourceState source) {
        return this.mAttributionSourceRegistry.isRegisteredAttributionSource(new android.content.AttributionSource(source));
    }

    public int getRegisteredAttributionSourceCount(int uid) {
        return this.mAttributionSourceRegistry.getRegisteredAttributionSourceCount(uid);
    }

    public java.util.List<java.lang.String> getAutoRevokeExemptionRequestedPackages(int userId) {
        return getPackagesWithAutoRevokePolicy(1, userId);
    }

    public java.util.List<java.lang.String> getAutoRevokeExemptionGrantedPackages(int userId) {
        return getPackagesWithAutoRevokePolicy(2, userId);
    }

    private java.util.List<java.lang.String> getPackagesWithAutoRevokePolicy(final int autoRevokePolicy, int userId) {
        this.mContext.enforceCallingPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY", "Must hold android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY");
        final java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        this.mPackageManagerInt.forEachInstalledPackage(new java.util.function.Consumer() { // from class: com.android.server.pm.permission.PermissionManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.permission.PermissionManagerService.lambda$getPackagesWithAutoRevokePolicy$0(autoRevokePolicy, result, (com.android.server.pm.pkg.AndroidPackage) obj);
            }
        }, userId);
        return result;
    }

    static /* synthetic */ void lambda$getPackagesWithAutoRevokePolicy$0(int autoRevokePolicy, java.util.List result, com.android.server.pm.pkg.AndroidPackage pkg) {
        if (pkg.getAutoRevokePermissions() == autoRevokePolicy) {
            result.add(pkg.getPackageName());
        }
    }

    public android.content.pm.ParceledListSlice<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) {
        return new android.content.pm.ParceledListSlice<>(this.mPermissionManagerServiceImpl.getAllPermissionGroups(flags));
    }

    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String groupName, int flags) {
        return this.mPermissionManagerServiceImpl.getPermissionGroupInfo(groupName, flags);
    }

    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permissionName, java.lang.String packageName, int flags) {
        return this.mPermissionManagerServiceImpl.getPermissionInfo(permissionName, flags, packageName);
    }

    public android.content.pm.ParceledListSlice<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String groupName, int flags) {
        java.util.List<android.content.pm.PermissionInfo> permissionInfo = this.mPermissionManagerServiceImpl.queryPermissionsByGroup(groupName, flags);
        if (permissionInfo == null) {
            return null;
        }
        return new android.content.pm.ParceledListSlice<>(permissionInfo);
    }

    public boolean addPermission(android.content.pm.PermissionInfo permissionInfo, boolean async) {
        return this.mPermissionManagerServiceImpl.addPermission(permissionInfo, async);
    }

    public void removePermission(java.lang.String permissionName) {
        this.mPermissionManagerServiceImpl.removePermission(permissionName);
    }

    public int getPermissionFlags(java.lang.String packageName, java.lang.String permissionName, java.lang.String persistentDeviceId, int userId) {
        return this.mPermissionManagerServiceImpl.getPermissionFlags(packageName, permissionName, persistentDeviceId, userId);
    }

    public void updatePermissionFlags(java.lang.String packageName, java.lang.String permissionName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, java.lang.String persistentDeviceId, int userId) {
        this.mPermissionManagerServiceImpl.updatePermissionFlags(packageName, permissionName, flagMask, flagValues, checkAdjustPolicyFlagPermission, persistentDeviceId, userId);
    }

    public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) {
        this.mPermissionManagerServiceImpl.updatePermissionFlagsForAllApps(flagMask, flagValues, userId);
    }

    public void addOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        this.mPermissionManagerServiceImpl.addOnPermissionsChangeListener(listener);
    }

    public void removeOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        this.mPermissionManagerServiceImpl.removeOnPermissionsChangeListener(listener);
    }

    public java.util.List<java.lang.String> getAllowlistedRestrictedPermissions(java.lang.String packageName, int flags, int userId) {
        return this.mPermissionManagerServiceImpl.getAllowlistedRestrictedPermissions(packageName, flags, userId);
    }

    public boolean addAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permissionName, int flags, int userId) {
        return this.mPermissionManagerServiceImpl.addAllowlistedRestrictedPermission(packageName, permissionName, flags, userId);
    }

    public boolean removeAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permissionName, int flags, int userId) {
        return this.mPermissionManagerServiceImpl.removeAllowlistedRestrictedPermission(packageName, permissionName, flags, userId);
    }

    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String persistentDeviceId, int userId) {
        this.mPermissionManagerServiceImpl.grantRuntimePermission(packageName, permissionName, persistentDeviceId, userId);
    }

    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String persistentDeviceId, int userId, java.lang.String reason) {
        this.mPermissionManagerServiceImpl.revokeRuntimePermission(packageName, permissionName, persistentDeviceId, userId, reason);
    }

    public void revokePostNotificationPermissionWithoutKillForTest(java.lang.String packageName, int userId) {
        this.mPermissionManagerServiceImpl.revokePostNotificationPermissionWithoutKillForTest(packageName, userId);
    }

    public boolean shouldShowRequestPermissionRationale(java.lang.String packageName, java.lang.String permissionName, int deviceId, int userId) {
        java.lang.String persistentDeviceId = getPersistentDeviceId(deviceId);
        return this.mPermissionManagerServiceImpl.shouldShowRequestPermissionRationale(packageName, permissionName, persistentDeviceId, userId);
    }

    public boolean isPermissionRevokedByPolicy(java.lang.String packageName, java.lang.String permissionName, int deviceId, int userId) {
        java.lang.String persistentDeviceId = getPersistentDeviceId(deviceId);
        return this.mPermissionManagerServiceImpl.isPermissionRevokedByPolicy(packageName, permissionName, persistentDeviceId, userId);
    }

    public java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> getSplitPermissions() {
        return this.mPermissionManagerServiceImpl.getSplitPermissions();
    }

    private class PermissionManagerServiceInternalImpl implements com.android.server.pm.permission.PermissionManagerServiceInternal {
        private PermissionManagerServiceInternalImpl() {
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public int checkPermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String persistentDeviceId, int userId) {
            return com.android.server.pm.permission.PermissionManagerService.this.checkPermission(packageName, permissionName, persistentDeviceId, userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public int checkUidPermission(int uid, java.lang.String permissionName, int deviceId) {
            return com.android.server.pm.permission.PermissionManagerService.this.checkUidPermission(uid, permissionName, deviceId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void setHotwordDetectionServiceProvider(com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider provider) {
            com.android.server.pm.permission.PermissionManagerService.this.mHotwordDetectionServiceProvider = provider;
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider getHotwordDetectionServiceProvider() {
            return com.android.server.pm.permission.PermissionManagerService.this.mHotwordDetectionServiceProvider;
        }

        @Override // com.android.server.pm.permission.LegacyPermissionDataProvider
        public int[] getGidsForUid(int uid) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getGidsForUid(uid);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionDataProvider
        public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages() {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getAllAppOpPermissionPackages();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onUserCreated(int userId) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onUserCreated(userId);
        }

        @Override // com.android.server.pm.permission.LegacyPermissionDataProvider
        public java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions() {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getLegacyPermissions();
        }

        @Override // com.android.server.pm.permission.LegacyPermissionDataProvider
        public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int appId) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getLegacyPermissionState(appId);
        }

        public byte[] backupRuntimePermissions(int userId) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.backupRuntimePermissions(userId);
        }

        public void restoreRuntimePermissions(byte[] backup, int userId) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.restoreRuntimePermissions(backup, userId);
        }

        public void restoreDelayedRuntimePermissions(java.lang.String packageName, int userId) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.restoreDelayedRuntimePermissions(packageName, userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.readLegacyPermissionsTEMP(legacyPermissionSettings);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.writeLegacyPermissionsTEMP(legacyPermissionSettings);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public java.lang.String getDefaultPermissionGrantFingerprint(int userId) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getDefaultPermissionGrantFingerprint(userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void setDefaultPermissionGrantFingerprint(java.lang.String fingerprint, int userId) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.setDefaultPermissionGrantFingerprint(fingerprint, userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onPackageAdded(com.android.server.pm.pkg.PackageState packageState, boolean isInstantApp, com.android.server.pm.pkg.AndroidPackage oldPkg) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onPackageAdded(packageState, isInstantApp, oldPkg);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage pkg, int previousAppId, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams params, int rawUserId) {
            java.util.Objects.requireNonNull(pkg, "pkg");
            java.util.Objects.requireNonNull(params, "params");
            com.android.internal.util.Preconditions.checkArgument(rawUserId >= 0 || rawUserId == -1, "userId");
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onPackageInstalled(pkg, previousAppId, params, rawUserId);
            int[] userIds = rawUserId == -1 ? com.android.server.pm.permission.PermissionManagerService.this.getAllUserIds() : new int[]{rawUserId};
            for (int userId : userIds) {
                int autoRevokePermissionsMode = params.getAutoRevokePermissionsMode();
                if (autoRevokePermissionsMode == 0 || autoRevokePermissionsMode == 1) {
                    com.android.server.pm.permission.PermissionManagerService.this.setAutoRevokeExemptedInternal(pkg, autoRevokePermissionsMode == 1, userId);
                }
            }
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage pkg) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onPackageRemoved(pkg);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onPackageUninstalled(java.lang.String packageName, int appId, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<com.android.server.pm.pkg.AndroidPackage> sharedUserPkgs, int userId) {
            if (userId != -1) {
                int[] userIds = com.android.server.pm.permission.PermissionManagerService.this.getAllUserIds();
                if (!com.android.internal.util.ArrayUtils.contains(userIds, userId)) {
                    android.util.Slog.w(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, "Skipping onPackageUninstalled() for non-existent user " + userId);
                    return;
                }
            }
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceExt.beforeOnPackageUninstalled();
            try {
                com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onPackageUninstalled(packageName, appId, packageState, pkg, sharedUserPkgs, userId);
            } finally {
                com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceExt.afterOnPackageUninstalled();
            }
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onSystemReady() {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onSystemReady();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public boolean isPermissionsReviewRequired(java.lang.String packageName, int userId) {
            if (com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceExt.hookIsPermissionsReviewRequiredInternal()) {
                return false;
            }
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.isPermissionsReviewRequired(packageName, userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void readLegacyPermissionStateTEMP() {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.readLegacyPermissionStateTEMP();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal, com.android.server.pm.permission.LegacyPermissionDataProvider
        public void writeLegacyPermissionStateTEMP() {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.writeLegacyPermissionStateTEMP();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onUserRemoved(int userId) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onUserRemoved(userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String packageName) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getInstalledPermissions(packageName);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String packageName, int userId) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getGrantedPermissions(packageName, userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public int[] getPermissionGids(java.lang.String permissionName, int userId) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getPermissionGids(permissionName, userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName) {
            android.util.ArraySet<java.lang.String> packageNames = new android.util.ArraySet<>(com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getAppOpPermissionPackages(permissionName));
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceExt.adjustGetAppOpPermissionPackagesInternal(packageNames);
            return (java.lang.String[]) packageNames.toArray(new java.lang.String[0]);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void onStorageVolumeMounted(java.lang.String volumeUuid, boolean fingerprintChanged) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.onStorageVolumeMounted(volumeUuid, fingerprintChanged);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.resetRuntimePermissions(pkg, userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void resetRuntimePermissionsForUser(int userId) {
            com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.resetRuntimePermissionsForUser(userId);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String permName) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getPermissionTEMP(permName);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int protection) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getAllPermissionsWithProtection(protection);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int protectionFlags) {
            return com.android.server.pm.permission.PermissionManagerService.this.mPermissionManagerServiceImpl.getAllPermissionsWithProtectionFlags(protectionFlags);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal
        public void setCheckPermissionDelegate(com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate delegate) {
            com.android.server.pm.permission.PermissionManagerService.this.setCheckPermissionDelegateInternal(delegate);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getAllUserIds() {
        return com.android.server.pm.UserManagerService.getInstance().getUserIdsIncludingPreCreated();
    }

    private static final class AttributionSourceRegistry {
        private final android.content.Context mContext;
        private final java.lang.Object mLock = new java.lang.Object();
        private final java.util.WeakHashMap<android.os.IBinder, android.content.AttributionSource> mAttributions = new java.util.WeakHashMap<>();

        AttributionSourceRegistry(android.content.Context context) {
            this.mContext = context;
        }

        public void registerAttributionSource(android.content.AttributionSource source) {
            int callingUid = resolveUid(android.os.Binder.getCallingUid());
            int sourceUid = resolveUid(source.getUid());
            if (sourceUid != callingUid && this.mContext.checkPermission("android.permission.UPDATE_APP_OPS_STATS", -1, callingUid) != 0) {
                throw new java.lang.SecurityException("Cannot register attribution source for uid:" + sourceUid + " from uid:" + callingUid);
            }
            android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            int userId = android.os.UserHandle.getUserId(callingUid == 1000 ? sourceUid : callingUid);
            if (packageManagerInternal.getPackageUid(source.getPackageName(), 0L, userId) != sourceUid) {
                throw new java.lang.SecurityException("Cannot register attribution source for package:" + source.getPackageName() + " from uid:" + callingUid);
            }
            android.content.AttributionSource next = source.getNext();
            if (next != null && next.getNext() != null && !isRegisteredAttributionSource(next)) {
                throw new java.lang.SecurityException("Cannot register forged attribution source:" + source);
            }
            synchronized (this.mLock) {
                this.mAttributions.put(source.getToken(), source.withDefaultToken());
            }
        }

        public boolean isRegisteredAttributionSource(android.content.AttributionSource source) {
            synchronized (this.mLock) {
                android.content.AttributionSource cachedSource = this.mAttributions.get(source.getToken());
                if (cachedSource == null) {
                    return false;
                }
                return cachedSource.equalsExceptToken(source);
            }
        }

        public int getRegisteredAttributionSourceCount(int uid) {
            int numForUid;
            this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_APP_OPS_STATS", "getting the number of registered AttributionSources requires UPDATE_APP_OPS_STATS");
            java.lang.System.gc();
            java.lang.System.gc();
            synchronized (this.mLock) {
                numForUid = 0;
                for (java.util.Map.Entry<android.os.IBinder, android.content.AttributionSource> entry : this.mAttributions.entrySet()) {
                    if (entry.getValue().getUid() == uid) {
                        numForUid++;
                    }
                }
            }
            return numForUid;
        }

        private int resolveUid(int uid) {
            android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity hotwordDetectionServiceIdentity;
            android.service.voice.VoiceInteractionManagerInternal vimi = (android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class);
            if (vimi != null && (hotwordDetectionServiceIdentity = vimi.getHotwordDetectionServiceIdentity()) != null && uid == hotwordDetectionServiceIdentity.getIsolatedUid()) {
                return hotwordDetectionServiceIdentity.getOwnerUid();
            }
            return uid;
        }
    }

    private static final class PermissionCheckerService extends android.permission.IPermissionChecker.Stub {
        private final android.content.Context mContext;
        private final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManagerServiceInternal = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
        private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, android.content.pm.PermissionInfo> sPlatformPermissions = new java.util.concurrent.ConcurrentHashMap<>();
        private static final java.util.concurrent.atomic.AtomicInteger sAttributionChainIds = new java.util.concurrent.atomic.AtomicInteger(0);

        PermissionCheckerService(android.content.Context context) {
            this.mContext = context;
        }

        public int checkPermission(java.lang.String permission, android.content.AttributionSourceState attributionSourceState, java.lang.String message, boolean forDataDelivery, boolean startDataDelivery, boolean fromDatasource, int attributedOp) {
            java.util.Objects.requireNonNull(permission);
            java.util.Objects.requireNonNull(attributionSourceState);
            android.content.AttributionSource attributionSource = new android.content.AttributionSource(attributionSourceState);
            int result = checkPermission(this.mContext, this.mPermissionManagerServiceInternal, permission, attributionSource, message, forDataDelivery, startDataDelivery, fromDatasource, attributedOp);
            if (startDataDelivery && result != 0 && result != 1) {
                if (attributedOp == -1) {
                    finishDataDelivery(android.app.AppOpsManager.permissionToOpCode(permission), attributionSource.asState(), fromDatasource);
                } else {
                    finishDataDelivery(attributedOp, attributionSource.asState(), fromDatasource);
                }
            }
            return result;
        }

        public void finishDataDelivery(int op, android.content.AttributionSourceState attributionSourceState, boolean fromDataSource) {
            finishDataDelivery(this.mContext, op, attributionSourceState, fromDataSource);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void finishDataDelivery(android.content.Context context, int op, android.content.AttributionSourceState attributionSourceState, boolean fromDatasource) {
            com.android.server.pm.permission.PermissionManagerService.RegisteredAttribution registered;
            java.util.Objects.requireNonNull(attributionSourceState);
            android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
            if (op == -1) {
                return;
            }
            android.content.AttributionSource current = new android.content.AttributionSource(attributionSourceState);
            android.content.AttributionSource next = null;
            while (true) {
                boolean skipCurrentFinish = fromDatasource || next != null;
                next = current.getNext();
                if ((!fromDatasource || current.asState() != attributionSourceState) && next != null && !current.isTrusted(context)) {
                    return;
                }
                boolean singleReceiverFromDatasource = fromDatasource && current.asState() == attributionSourceState && next != null && next.getNext() == null;
                boolean selfAccess = singleReceiverFromDatasource || next == null;
                android.content.AttributionSource accessorSource = !singleReceiverFromDatasource ? current : next;
                if (selfAccess) {
                    java.lang.String resolvedPackageName = resolvePackageName(context, accessorSource);
                    if (resolvedPackageName == null) {
                        return;
                    }
                    android.content.AttributionSource resolvedAccessorSource = accessorSource.withPackageName(resolvedPackageName);
                    appOpsManager.finishOp(attributionSourceState.token, op, resolvedAccessorSource);
                } else {
                    android.content.AttributionSource resolvedAttributionSource = resolveAttributionSource(context, accessorSource);
                    if (resolvedAttributionSource.getPackageName() == null) {
                        return;
                    } else {
                        appOpsManager.finishProxyOp(attributionSourceState.token, android.app.AppOpsManager.opToPublicName(op), resolvedAttributionSource, skipCurrentFinish);
                    }
                }
                com.android.server.pm.permission.PermissionManagerService.RegisteredAttribution registered2 = (com.android.server.pm.permission.PermissionManagerService.RegisteredAttribution) com.android.server.pm.permission.PermissionManagerService.sRunningAttributionSources.remove(current.getToken());
                if (registered2 != null) {
                    registered2.unregister();
                }
                if (next == null || next.getNext() == null) {
                    break;
                } else {
                    current = next;
                }
            }
            if (next != null && (registered = (com.android.server.pm.permission.PermissionManagerService.RegisteredAttribution) com.android.server.pm.permission.PermissionManagerService.sRunningAttributionSources.remove(next.getToken())) != null) {
                registered.unregister();
            }
        }

        public int checkOp(int op, android.content.AttributionSourceState attributionSource, java.lang.String message, boolean forDataDelivery, boolean startDataDelivery) {
            int result = checkOp(this.mContext, op, this.mPermissionManagerServiceInternal, new android.content.AttributionSource(attributionSource), message, forDataDelivery, startDataDelivery);
            if (result != 0 && startDataDelivery) {
                finishDataDelivery(op, attributionSource, false);
            }
            return result;
        }

        private static int checkPermission(android.content.Context context, com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerServiceInt, java.lang.String permission, android.content.AttributionSource attributionSource, java.lang.String message, boolean forDataDelivery, boolean startDataDelivery, boolean fromDatasource, int attributedOp) {
            android.content.pm.PermissionInfo permissionInfo;
            android.content.pm.PermissionInfo permissionInfo2 = sPlatformPermissions.get(permission);
            if (permissionInfo2 == null) {
                try {
                    android.content.pm.PermissionInfo permissionInfo3 = context.getPackageManager().getPermissionInfo(permission, 0);
                    if (!com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(permissionInfo3.packageName)) {
                        try {
                            if (android.health.connect.HealthConnectManager.isHealthPermission(context, permission)) {
                            }
                            permissionInfo = permissionInfo3;
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            return 2;
                        }
                    }
                    sPlatformPermissions.put(permission, permissionInfo3);
                    permissionInfo = permissionInfo3;
                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                }
            } else {
                permissionInfo = permissionInfo2;
            }
            if (permissionInfo.isAppOp()) {
                return checkAppOpPermission(context, permissionManagerServiceInt, permission, attributionSource, message, forDataDelivery, fromDatasource);
            }
            if (permissionInfo.isRuntime()) {
                return checkRuntimePermission(context, permissionManagerServiceInt, permission, attributionSource, message, forDataDelivery, startDataDelivery, fromDatasource, attributedOp);
            }
            if (!fromDatasource && !checkPermission(context, permissionManagerServiceInt, permission, attributionSource)) {
                return 2;
            }
            if (attributionSource.getNext() != null) {
                return checkPermission(context, permissionManagerServiceInt, permission, attributionSource.getNext(), message, forDataDelivery, startDataDelivery, false, attributedOp);
            }
            return 0;
        }

        private static int checkAppOpPermission(android.content.Context context, com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerServiceInt, java.lang.String permission, android.content.AttributionSource attributionSource, java.lang.String message, boolean forDataDelivery, boolean fromDatasource) {
            android.content.AttributionSource attributionSource2 = attributionSource;
            int op = android.app.AppOpsManager.permissionToOpCode(permission);
            if (op < 0) {
                android.util.Slog.wtf(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, "Appop permission " + permission + " with no app op defined!");
                return 2;
            }
            android.content.AttributionSource next = null;
            android.content.AttributionSource current = attributionSource;
            while (true) {
                boolean skipCurrentChecks = fromDatasource || next != null;
                android.content.AttributionSource next2 = current.getNext();
                if ((!fromDatasource || !current.equals(attributionSource2)) && next2 != null && !current.isTrusted(context)) {
                    return 2;
                }
                boolean singleReceiverFromDatasource = fromDatasource && current.equals(attributionSource2) && next2 != null && next2.getNext() == null;
                boolean selfAccess = singleReceiverFromDatasource || next2 == null;
                int opMode = performOpTransaction(context, attributionSource.getToken(), op, current, message, forDataDelivery, false, skipCurrentChecks, selfAccess, singleReceiverFromDatasource, -1, 0, 0, -1);
                switch (opMode) {
                    case 1:
                    case 2:
                        return 2;
                    case 3:
                        if (!skipCurrentChecks && !checkPermission(context, permissionManagerServiceInt, permission, attributionSource)) {
                            return 2;
                        }
                        next = next2;
                        if (next != null && !checkPermission(context, permissionManagerServiceInt, permission, next)) {
                            return 2;
                        }
                        break;
                    default:
                        next = next2;
                        break;
                }
                if (next != null && next.getNext() != null) {
                    current = next;
                    attributionSource2 = attributionSource;
                }
            }
            return 0;
        }

        private static int checkRuntimePermission(android.content.Context context, com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerServiceInt, java.lang.String permission, android.content.AttributionSource attributionSource, java.lang.String message, boolean forDataDelivery, boolean startDataDelivery, boolean fromDatasource, int attributedOp) {
            int proxyAttributionFlags;
            android.content.AttributionSource next;
            int iResolveProxiedAttributionFlags;
            android.content.Context context2;
            boolean z;
            int op;
            android.content.Context context3 = context;
            com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerServiceInternal = permissionManagerServiceInt;
            java.lang.String str = permission;
            android.content.AttributionSource attributionSource2 = attributionSource;
            boolean z2 = fromDatasource;
            int op2 = android.app.AppOpsManager.permissionToOpCode(permission);
            int attributionChainId = getAttributionChainId(startDataDelivery, attributionSource2);
            boolean hasChain = attributionChainId != -1;
            android.content.AttributionSource next2 = null;
            boolean isChainStartTrusted = !hasChain || checkPermission(context3, permissionManagerServiceInternal, "android.permission.UPDATE_APP_OPS_STATS", attributionSource);
            android.content.AttributionSource current = attributionSource;
            while (true) {
                boolean skipCurrentChecks = z2 || next2 != null;
                android.content.AttributionSource next3 = current.getNext();
                boolean isDatasource = z2 && current.equals(attributionSource2);
                if (!isDatasource && next3 != null && !current.isTrusted(context3)) {
                    return 2;
                }
                if (!skipCurrentChecks && !checkPermission(context3, permissionManagerServiceInternal, str, current)) {
                    return 2;
                }
                if (next3 != null && !checkPermission(context3, permissionManagerServiceInternal, str, next3)) {
                    return 2;
                }
                if (op2 < 0) {
                    if (sPlatformPermissions.containsKey(str) && !"android.permission.ACCESS_BACKGROUND_LOCATION".equals(str) && !"android.permission.BODY_SENSORS_BACKGROUND".equals(str)) {
                        android.util.Slog.wtf(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, "Platform runtime permission " + str + " with no app op defined!");
                    }
                    if (next3 == null) {
                        return 0;
                    }
                    current = next3;
                    next2 = next3;
                } else {
                    boolean singleReceiverFromDatasource = z2 && current.equals(attributionSource2) && next3 != null && next3.getNext() == null;
                    boolean selfAccess = singleReceiverFromDatasource || next3 == null;
                    boolean isLinkTrusted = isChainStartTrusted && (current.isTrusted(context3) || current.equals(attributionSource2)) && (next3 == null || next3.isTrusted(context3));
                    if (!skipCurrentChecks && hasChain) {
                        proxyAttributionFlags = resolveProxyAttributionFlags(attributionSource, current, fromDatasource, startDataDelivery, selfAccess, isLinkTrusted);
                    } else {
                        proxyAttributionFlags = 0;
                    }
                    if (hasChain) {
                        next = next3;
                        iResolveProxiedAttributionFlags = resolveProxiedAttributionFlags(attributionSource, next3, fromDatasource, startDataDelivery, selfAccess, isLinkTrusted);
                    } else {
                        next = next3;
                        iResolveProxiedAttributionFlags = 0;
                    }
                    android.content.AttributionSource current2 = current;
                    int proxiedAttributionFlags = iResolveProxiedAttributionFlags;
                    int attributionChainId2 = attributionChainId;
                    int op3 = op2;
                    android.content.AttributionSource attributionSource3 = attributionSource2;
                    java.lang.String str2 = str;
                    int opMode = performOpTransaction(context, attributionSource.getToken(), op2, current2, message, forDataDelivery, startDataDelivery, skipCurrentChecks, selfAccess, singleReceiverFromDatasource, attributedOp, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId2);
                    switch (opMode) {
                        case 1:
                            return 1;
                        case 2:
                            if (str2.equals("android.permission.BLUETOOTH_CONNECT")) {
                                android.util.Slog.e(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, "BLUETOOTH_CONNECT permission hard denied as op mode is MODE_ERRORED. Permission check was requested for: " + attributionSource3 + " and op transaction was invoked for " + current2);
                            }
                            return 2;
                        default:
                            if (startDataDelivery) {
                                context2 = context;
                                z = fromDatasource;
                                op = op3;
                                com.android.server.pm.permission.PermissionManagerService.RegisteredAttribution registered = new com.android.server.pm.permission.PermissionManagerService.RegisteredAttribution(context2, op, current2, z);
                                com.android.server.pm.permission.PermissionManagerService.sRunningAttributionSources.put(current2.getToken(), registered);
                            } else {
                                context2 = context;
                                z = fromDatasource;
                                op = op3;
                            }
                            if (next != null && next.getNext() != null) {
                                current = next;
                                attributionSource2 = attributionSource;
                                context3 = context2;
                                z2 = z;
                                op2 = op;
                                str = str2;
                                next2 = next;
                                attributionChainId = attributionChainId2;
                                permissionManagerServiceInternal = permissionManagerServiceInt;
                            }
                            break;
                    }
                }
            }
            return 0;
        }

        private static boolean checkPermission(android.content.Context context, com.android.server.pm.permission.PermissionManagerServiceInternal permissionManagerServiceInt, java.lang.String permission, android.content.AttributionSource attributionSource) {
            int uid = attributionSource.getUid();
            int deviceId = attributionSource.getDeviceId();
            android.content.Context deviceContext = context.getDeviceId() == deviceId ? context : context.createDeviceContext(deviceId);
            boolean permissionGranted = deviceContext.checkPermission(permission, -1, uid) == 0;
            if (!permissionGranted && android.os.Process.isIsolated(uid) && (permission.equals("android.permission.RECORD_AUDIO") || permission.equals("android.permission.CAPTURE_AUDIO_HOTWORD") || permission.equals("android.permission.CAPTURE_AUDIO_OUTPUT") || permission.equals("android.permission.CAMERA"))) {
                com.android.server.pm.permission.PermissionManagerServiceInternal.HotwordDetectionServiceProvider hotwordServiceProvider = permissionManagerServiceInt.getHotwordDetectionServiceProvider();
                permissionGranted = hotwordServiceProvider != null && uid == hotwordServiceProvider.getUid();
            }
            java.util.Set<java.lang.String> renouncedPermissions = attributionSource.getRenouncedPermissions();
            if (permissionGranted && renouncedPermissions.contains(permission) && deviceContext.checkPermission("android.permission.RENOUNCE_PERMISSIONS", -1, uid) == 0) {
                return false;
            }
            return permissionGranted;
        }

        private static int resolveProxyAttributionFlags(android.content.AttributionSource attributionChain, android.content.AttributionSource current, boolean fromDatasource, boolean startDataDelivery, boolean selfAccess, boolean isTrusted) {
            return resolveAttributionFlags(attributionChain, current, fromDatasource, startDataDelivery, selfAccess, isTrusted, true);
        }

        private static int resolveProxiedAttributionFlags(android.content.AttributionSource attributionChain, android.content.AttributionSource current, boolean fromDatasource, boolean startDataDelivery, boolean selfAccess, boolean isTrusted) {
            return resolveAttributionFlags(attributionChain, current, fromDatasource, startDataDelivery, selfAccess, isTrusted, false);
        }

        private static int resolveAttributionFlags(android.content.AttributionSource attributionChain, android.content.AttributionSource current, boolean fromDatasource, boolean startDataDelivery, boolean selfAccess, boolean isTrusted, boolean flagsForProxy) {
            int trustedFlag;
            if (current == null || !startDataDelivery) {
                return 0;
            }
            if (!isTrusted) {
                trustedFlag = 0;
            } else {
                trustedFlag = 8;
            }
            if (flagsForProxy) {
                if (selfAccess) {
                    return trustedFlag | 1;
                }
                if (!fromDatasource && current.equals(attributionChain)) {
                    return trustedFlag | 1;
                }
            } else {
                if (selfAccess) {
                    return trustedFlag | 4;
                }
                if (fromDatasource && current.equals(attributionChain.getNext())) {
                    return trustedFlag | 1;
                }
                if (current.getNext() == null) {
                    return trustedFlag | 4;
                }
            }
            if (fromDatasource && current.equals(attributionChain)) {
                return 0;
            }
            return trustedFlag | 2;
        }

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
            	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
            */
        private static int checkOp(android.content.Context r28, int r29, com.android.server.pm.permission.PermissionManagerServiceInternal r30, android.content.AttributionSource r31, java.lang.String r32, boolean r33, boolean r34) {
            /*
                Method dump skipped, instruction units count: 236
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.permission.PermissionManagerService.PermissionCheckerService.checkOp(android.content.Context, int, com.android.server.pm.permission.PermissionManagerServiceInternal, android.content.AttributionSource, java.lang.String, boolean, boolean):int");
        }

        private static int performOpTransaction(android.content.Context context, android.os.IBinder chainStartToken, int op, android.content.AttributionSource attributionSource, java.lang.String message, boolean forDataDelivery, boolean startDataDelivery, boolean skipProxyOperation, boolean selfAccess, boolean singleReceiverFromDatasource, int attributedOp, int proxyAttributionFlags, int proxiedAttributionFlags, int attributionChainId) {
            java.lang.String str;
            int i;
            int notedOpResult;
            java.lang.String str2;
            int startedOp;
            int checkedOpResult;
            int startedOpResult;
            android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
            android.content.AttributionSource accessorSource = !singleReceiverFromDatasource ? attributionSource : attributionSource.getNext();
            if (!forDataDelivery) {
                java.lang.String resolvedAccessorPackageName = resolvePackageName(context, accessorSource);
                if (resolvedAccessorPackageName == null) {
                    return 2;
                }
                int opMode = appOpsManager.unsafeCheckOpRawNoThrow(op, accessorSource.withPackageName(resolvedAccessorPackageName));
                android.content.AttributionSource next = accessorSource.getNext();
                if (!selfAccess && opMode == 0 && next != null) {
                    java.lang.String resolvedNextPackageName = resolvePackageName(context, next);
                    if (resolvedNextPackageName == null) {
                        return 2;
                    }
                    android.content.AttributionSource resolvedNextAttributionSource = next.withPackageName(resolvedNextPackageName);
                    return appOpsManager.unsafeCheckOpRawNoThrow(op, resolvedNextAttributionSource);
                }
                return opMode;
            }
            if (startDataDelivery) {
                android.content.AttributionSource resolvedAttributionSource = resolveAttributionSource(context, accessorSource);
                if (resolvedAttributionSource.getPackageName() == null) {
                    return 2;
                }
                if (attributedOp == -1 || attributedOp == op) {
                    str2 = ", ";
                    startedOp = op;
                    checkedOpResult = 0;
                } else {
                    int checkedOpResult2 = appOpsManager.checkOpNoThrow(op, resolvedAttributionSource);
                    str2 = ", ";
                    if (checkedOpResult2 == 2) {
                        return checkedOpResult2;
                    }
                    checkedOpResult = checkedOpResult2;
                    startedOp = attributedOp;
                }
                if (selfAccess) {
                    try {
                        startedOpResult = appOpsManager.startOpNoThrow(chainStartToken, startedOp, resolvedAttributionSource, false, message, proxyAttributionFlags, attributionChainId);
                    } catch (java.lang.SecurityException e) {
                        android.util.Slog.w(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, "Datasource " + attributionSource + " protecting data with platform defined runtime permission " + android.app.AppOpsManager.opToPermission(op) + " while not having android.permission.UPDATE_APP_OPS_STATS");
                        startedOpResult = appOpsManager.startProxyOpNoThrow(chainStartToken, attributedOp, attributionSource, message, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId);
                    }
                } else {
                    int startedOp2 = startedOp;
                    java.lang.String str3 = str2;
                    try {
                        startedOpResult = appOpsManager.startProxyOpNoThrow(chainStartToken, startedOp2, resolvedAttributionSource, message, skipProxyOperation, proxyAttributionFlags, proxiedAttributionFlags, attributionChainId);
                    } catch (java.lang.SecurityException e2) {
                        java.lang.String msg = "Security exception for op " + startedOp2 + " with source " + attributionSource.getUid() + ":" + attributionSource.getPackageName() + str3 + attributionSource.getNextUid() + ":" + attributionSource.getNextPackageName();
                        if (attributionSource.getNext() != null) {
                            android.content.AttributionSource next2 = attributionSource.getNext();
                            msg = msg + str3 + next2.getNextPackageName() + ":" + next2.getNextUid();
                        }
                        throw new java.lang.SecurityException(msg + ":" + e2.getMessage());
                    }
                }
                return java.lang.Math.max(checkedOpResult, startedOpResult);
            }
            android.content.AttributionSource resolvedAttributionSource2 = resolveAttributionSource(context, accessorSource);
            if (resolvedAttributionSource2.getPackageName() == null) {
                return 2;
            }
            int notedOp = op;
            int checkedOpResult3 = 0;
            if (attributedOp != -1) {
                str = "Datasource ";
                i = op;
                if (attributedOp != i) {
                    checkedOpResult3 = appOpsManager.checkOpNoThrow(i, resolvedAttributionSource2);
                    if (checkedOpResult3 == 2) {
                        return checkedOpResult3;
                    }
                    notedOp = attributedOp;
                }
            } else {
                str = "Datasource ";
                i = op;
            }
            if (selfAccess) {
                try {
                    notedOpResult = appOpsManager.noteOpNoThrow(notedOp, resolvedAttributionSource2, message);
                } catch (java.lang.SecurityException e3) {
                    android.util.Slog.w(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, str + attributionSource + " protecting data with platform defined runtime permission " + android.app.AppOpsManager.opToPermission(op) + " while not having android.permission.UPDATE_APP_OPS_STATS");
                    notedOpResult = appOpsManager.noteProxyOpNoThrow(notedOp, attributionSource, message, skipProxyOperation);
                }
            } else {
                try {
                    notedOpResult = appOpsManager.noteProxyOpNoThrow(notedOp, resolvedAttributionSource2, message, skipProxyOperation);
                } catch (java.lang.SecurityException e4) {
                    java.lang.String msg2 = "Security exception for op " + notedOp + " with source " + attributionSource.getUid() + ":" + attributionSource.getPackageName() + ", " + attributionSource.getNextUid() + ":" + attributionSource.getNextPackageName();
                    if (attributionSource.getNext() != null) {
                        android.content.AttributionSource next3 = attributionSource.getNext();
                        msg2 = msg2 + ", " + next3.getNextPackageName() + ":" + next3.getNextUid();
                    }
                    throw new java.lang.SecurityException(msg2 + ":" + e4.getMessage());
                }
            }
            int result = java.lang.Math.max(checkedOpResult3, notedOpResult);
            if (i == 111 && result == 2) {
                if (result == checkedOpResult3) {
                    android.util.Slog.e(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, "BLUETOOTH_CONNECT permission hard denied as checkOp for resolvedAttributionSource " + resolvedAttributionSource2 + " and op " + i + " returned MODE_ERRORED");
                } else {
                    android.util.Slog.e(com.android.server.pm.permission.PermissionManagerService.LOG_TAG, "BLUETOOTH_CONNECT permission hard denied as noteOp for resolvedAttributionSource " + resolvedAttributionSource2 + " and op " + notedOp + " returned MODE_ERRORED");
                }
            }
            return result;
        }

        private static int getAttributionChainId(boolean startDataDelivery, android.content.AttributionSource source) {
            if (source == null || source.getNext() == null || !startDataDelivery) {
                return -1;
            }
            int attributionChainId = sAttributionChainIds.incrementAndGet();
            if (attributionChainId < 0) {
                sAttributionChainIds.set(0);
                return 0;
            }
            return attributionChainId;
        }

        private static java.lang.String resolvePackageName(android.content.Context context, android.content.AttributionSource attributionSource) {
            if (attributionSource.getPackageName() != null) {
                return attributionSource.getPackageName();
            }
            java.lang.String[] packageNames = context.getPackageManager().getPackagesForUid(attributionSource.getUid());
            if (packageNames != null) {
                return packageNames[0];
            }
            return android.app.AppOpsManager.resolvePackageName(attributionSource.getUid(), attributionSource.getPackageName());
        }

        private static android.content.AttributionSource resolveAttributionSource(android.content.Context context, android.content.AttributionSource attributionSource) {
            if (attributionSource.getPackageName() != null) {
                return attributionSource;
            }
            return attributionSource.withPackageName(resolvePackageName(context, attributionSource));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class RegisteredAttribution {
        private final android.os.IBinder.DeathRecipient mDeathRecipient;
        private final java.util.concurrent.atomic.AtomicBoolean mFinished = new java.util.concurrent.atomic.AtomicBoolean(false);
        private final android.os.IBinder mToken;

        RegisteredAttribution(final android.content.Context context, final int op, final android.content.AttributionSource source, final boolean fromDatasource) {
            this.mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.pm.permission.PermissionManagerService$RegisteredAttribution$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$new$0(context, op, source, fromDatasource);
                }
            };
            this.mToken = source.getToken();
            if (this.mToken != null) {
                try {
                    this.mToken.linkToDeath(this.mDeathRecipient, 0);
                } catch (android.os.RemoteException e) {
                    this.mDeathRecipient.binderDied();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(android.content.Context context, int op, android.content.AttributionSource source, boolean fromDatasource) {
            if (unregister()) {
                com.android.server.pm.permission.PermissionManagerService.PermissionCheckerService.finishDataDelivery(context, op, source.asState(), fromDatasource);
            }
        }

        public boolean unregister() {
            if (!this.mFinished.compareAndSet(false, true)) {
                return false;
            }
            try {
                if (this.mToken != null) {
                    this.mToken.unlinkToDeath(this.mDeathRecipient, 0);
                }
            } catch (java.util.NoSuchElementException e) {
            }
            return true;
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        this.mPermissionManagerServiceImpl.dump(fd, writer, args);
    }
}
