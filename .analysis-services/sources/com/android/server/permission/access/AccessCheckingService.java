package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessCheckingService.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000¸\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001d\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\u001c2\u0006\u00103\u001a\u00020\u001cH\u0000¢\u0006\u0002\b4J<\u00105\u001a\u0002H6\"\u0004\b\u0000\u001062\u0017\u00107\u001a\u0013\u0012\u0004\u0012\u000209\u0012\u0004\u0012\u0002H608¢\u0006\u0002\b:H\u0080\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\b;\u0010<J\u0006\u0010=\u001a\u00020>J6\u0010?\u001a\u00020>2\u0019\b\u0004\u00107\u001a\u0013\u0012\u0004\u0012\u00020@\u0012\u0004\u0012\u00020>08¢\u0006\u0002\b:H\u0080\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\bAJ\u0015\u0010B\u001a\u00020>2\u0006\u0010C\u001a\u00020\u001cH\u0000¢\u0006\u0002\bDJ\u001d\u0010E\u001a\u00020>2\u0006\u0010C\u001a\u00020\u001c2\u0006\u0010F\u001a\u00020GH\u0000¢\u0006\u0002\bHJ\u001d\u0010I\u001a\u00020>2\u0006\u0010C\u001a\u00020\u001c2\u0006\u0010J\u001a\u00020GH\u0000¢\u0006\u0002\bKJ%\u0010L\u001a\u00020>2\u0006\u0010C\u001a\u00020\u001c2\u0006\u0010J\u001a\u00020G2\u0006\u0010F\u001a\u00020GH\u0000¢\u0006\u0002\bMJ\b\u0010N\u001a\u00020>H\u0016J-\u0010O\u001a\u00020>2\b\u0010P\u001a\u0004\u0018\u00010\u001c2\f\u0010Q\u001a\b\u0012\u0004\u0012\u00020\u001c0R2\u0006\u0010S\u001a\u00020&H\u0000¢\u0006\u0002\bTJ\r\u0010U\u001a\u00020>H\u0000¢\u0006\u0002\bVJ\u0015\u0010W\u001a\u00020>2\u0006\u0010F\u001a\u00020GH\u0000¢\u0006\u0002\bXJ\u0015\u0010Y\u001a\u00020>2\u0006\u0010F\u001a\u00020GH\u0000¢\u0006\u0002\bZR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.¢\u0006\u0002\n\u0000R<\u0010\u0019\u001a&\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d0\u001b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d0\u001b0\u001a*\u00020\n8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR*\u0010 \u001a\u0014\u0012\u0004\u0012\u00020\u001c\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\"0!*\u00020\u00168BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b#\u0010$R\u0018\u0010%\u001a\u00020&*\u00020\u00168BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b%\u0010'R$\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0*0)*\u00020\b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b+\u0010,R\u001e\u0010-\u001a\b\u0012\u0004\u0012\u00020\u001c0\"*\u00020\u00168BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b.\u0010/¨\u0006["}, d2 = {"Lcom/android/server/permission/access/AccessCheckingService;", "Lcom/android/server/SystemService;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "appOpService", "Lcom/android/server/permission/access/appop/AppOpService;", "packageManagerInternal", "Landroid/content/pm/PackageManagerInternal;", "packageManagerLocal", "Lcom/android/server/pm/PackageManagerLocal;", "permissionService", "Lcom/android/server/permission/access/permission/PermissionService;", "persistence", "Lcom/android/server/permission/access/AccessPersistence;", "policy", "Lcom/android/server/permission/access/AccessPolicy;", "state", "Lcom/android/server/permission/access/AccessState;", "stateLock", "", "systemConfig", "Lcom/android/server/SystemConfig;", "userManagerService", "Lcom/android/server/pm/UserManagerService;", "allPackageStates", "Lkotlin/Pair;", "", "", "Lcom/android/server/pm/pkg/PackageState;", "getAllPackageStates", "(Lcom/android/server/pm/PackageManagerLocal;)Lkotlin/Pair;", "implicitToSourcePermissions", "Lcom/android/server/permission/access/immutable/IndexedMap;", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "getImplicitToSourcePermissions", "(Lcom/android/server/SystemConfig;)Lcom/android/server/permission/access/immutable/IndexedMap;", "isLeanback", "", "(Lcom/android/server/SystemConfig;)Z", "knownPackages", "Lcom/android/server/permission/access/immutable/IntMap;", "", "getKnownPackages", "(Landroid/content/pm/PackageManagerInternal;)Lcom/android/server/permission/access/immutable/IntMap;", "privilegedPermissionAllowlistPackages", "getPrivilegedPermissionAllowlistPackages", "(Lcom/android/server/SystemConfig;)Lcom/android/server/permission/access/immutable/IndexedListSet;", "getSchemePolicy", "Lcom/android/server/permission/access/SchemePolicy;", "subjectScheme", "objectScheme", "getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "getState", "T", "action", "Lkotlin/Function1;", "Lcom/android/server/permission/access/GetStateScope;", "Lkotlin/ExtensionFunctionType;", "getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "(Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "initialize", "", "mutateState", "Lcom/android/server/permission/access/MutateStateScope;", "mutateState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onPackageAdded", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "onPackageAdded$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onPackageInstalled", "userId", "", "onPackageInstalled$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onPackageRemoved", "appId", "onPackageRemoved$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onPackageUninstalled", "onPackageUninstalled$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onStart", "onStorageVolumeMounted", "volumeUuid", com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY, "", "isSystemUpdated", "onStorageVolumeMounted$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onSystemReady", "onSystemReady$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onUserAdded", "onUserAdded$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "onUserRemoved", "onUserRemoved$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AccessCheckingService extends com.android.server.SystemService {
    private com.android.server.permission.access.appop.AppOpService appOpService;
    private android.content.pm.PackageManagerInternal packageManagerInternal;
    private com.android.server.pm.PackageManagerLocal packageManagerLocal;
    private com.android.server.permission.access.permission.PermissionService permissionService;
    private final com.android.server.permission.access.AccessPersistence persistence;
    private final com.android.server.permission.access.AccessPolicy policy;
    private volatile com.android.server.permission.access.AccessState state;
    private final java.lang.Object stateLock;
    private com.android.server.SystemConfig systemConfig;
    private com.android.server.pm.UserManagerService userManagerService;

    public AccessCheckingService(android.content.Context context) {
        super(context);
        this.stateLock = new java.lang.Object();
        this.policy = new com.android.server.permission.access.AccessPolicy();
        this.persistence = new com.android.server.permission.access.AccessPersistence(this.policy);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.appOpService = new com.android.server.permission.access.appop.AppOpService(this);
        this.permissionService = new com.android.server.permission.access.permission.PermissionService(this);
        com.android.server.permission.access.appop.AppOpService appOpService = this.appOpService;
        com.android.server.permission.access.permission.PermissionService permissionService = null;
        if (appOpService == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("appOpService");
            appOpService = null;
        }
        com.android.server.LocalServices.addService(com.android.server.appop.AppOpsCheckingServiceInterface.class, appOpService);
        com.android.server.permission.access.permission.PermissionService permissionService2 = this.permissionService;
        if (permissionService2 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("permissionService");
        } else {
            permissionService = permissionService2;
        }
        com.android.server.LocalServices.addService(com.android.server.pm.permission.PermissionManagerServiceInterface.class, permissionService);
        com.android.server.LocalManagerRegistry.addManager(com.android.server.permission.PermissionManagerLocal.class, new com.android.server.permission.access.permission.PermissionManagerLocalImpl(this));
    }

    public final void initialize() throws java.lang.Exception {
        this.packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.packageManagerLocal = (com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManagerOrThrow(com.android.server.pm.PackageManagerLocal.class);
        this.userManagerService = com.android.server.pm.UserManagerService.getInstance();
        this.systemConfig = com.android.server.SystemConfig.getInstance();
        com.android.server.pm.UserManagerService userManagerService = this.userManagerService;
        if (userManagerService == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerService");
            userManagerService = null;
        }
        com.android.server.permission.access.immutable.MutableIntSet userIds = com.android.server.permission.access.immutable.IntSetExtensionsKt.MutableIntSet(userManagerService.getUserIdsIncludingPreCreated());
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> allPackageStates = getAllPackageStates(packageManagerLocal);
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent1 = allPackageStates.component1();
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent2 = allPackageStates.component2();
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        com.android.server.permission.access.immutable.IntMap<java.lang.String[]> knownPackages = getKnownPackages(packageManagerInternal);
        com.android.server.SystemConfig systemConfig = this.systemConfig;
        if (systemConfig == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig = null;
        }
        boolean isLeanback = isLeanback(systemConfig);
        com.android.server.SystemConfig systemConfig2 = this.systemConfig;
        if (systemConfig2 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig2 = null;
        }
        android.util.ArrayMap<java.lang.String, com.android.server.SystemConfig.PermissionEntry> permissions = systemConfig2.getPermissions();
        com.android.server.SystemConfig systemConfig3 = this.systemConfig;
        if (systemConfig3 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig3 = null;
        }
        com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> privilegedPermissionAllowlistPackages = getPrivilegedPermissionAllowlistPackages(systemConfig3);
        com.android.server.SystemConfig systemConfig4 = this.systemConfig;
        if (systemConfig4 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig4 = null;
        }
        com.android.server.pm.permission.PermissionAllowlist permissionAllowlist = systemConfig4.getPermissionAllowlist();
        com.android.server.SystemConfig systemConfig5 = this.systemConfig;
        if (systemConfig5 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig5 = null;
        }
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> implicitToSourcePermissions = getImplicitToSourcePermissions(systemConfig5);
        com.android.server.permission.access.MutableAccessState state = new com.android.server.permission.access.MutableAccessState();
        this.policy.initialize(state, userIds, mapComponent1, mapComponent2, knownPackages, isLeanback, permissions, privilegedPermissionAllowlistPackages, permissionAllowlist, implicitToSourcePermissions);
        this.persistence.initialize();
        this.persistence.read(state);
        this.state = state;
        com.android.server.permission.access.appop.AppOpService appOpService = this.appOpService;
        if (appOpService == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("appOpService");
            appOpService = null;
        }
        appOpService.initialize();
        com.android.server.permission.access.permission.PermissionService permissionService = this.permissionService;
        if (permissionService == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("permissionService");
            permissionService = null;
        }
        permissionService.initialize();
    }

    private final boolean isLeanback(com.android.server.SystemConfig $this$isLeanback) {
        return $this$isLeanback.getAvailableFeatures().containsKey("android.software.leanback");
    }

    private final com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> getPrivilegedPermissionAllowlistPackages(com.android.server.SystemConfig $this$privilegedPermissionAllowlistPackages) {
        com.android.server.permission.access.immutable.MutableIndexedListSet $this$_get_privilegedPermissionAllowlistPackages__u24lambda_u240 = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
        $this$_get_privilegedPermissionAllowlistPackages__u24lambda_u240.add(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        java.lang.Object element$iv = $this$privilegedPermissionAllowlistPackages.getAvailableFeatures();
        if (((java.util.Map) element$iv).containsKey("android.hardware.type.automotive")) {
            java.lang.String carServicePackage = android.os.SystemProperties.get("ro.android.car.carservice.package");
            if (carServicePackage.length() > 0) {
                $this$_get_privilegedPermissionAllowlistPackages__u24lambda_u240.add(carServicePackage);
            }
        }
        return $this$_get_privilegedPermissionAllowlistPackages__u24lambda_u240;
    }

    private final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>> getImplicitToSourcePermissions(com.android.server.SystemConfig $this$implicitToSourcePermissions) {
        com.android.server.permission.access.immutable.MutableIndexedMap $this$_get_implicitToSourcePermissions__u24lambda_u244;
        int i;
        java.lang.Iterable $this$forEach$iv;
        boolean z;
        boolean z2 = true;
        com.android.server.permission.access.immutable.MutableIndexedMap mutableIndexedMap = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
        com.android.server.permission.access.immutable.MutableIndexedMap $this$_get_implicitToSourcePermissions__u24lambda_u2442 = mutableIndexedMap;
        int i2 = 0;
        java.lang.Iterable $this$forEach$iv2 = $this$implicitToSourcePermissions.getSplitPermissions();
        for (java.lang.Object element$iv : $this$forEach$iv2) {
            android.permission.PermissionManager.SplitPermissionInfo splitPermissionInfo = (android.permission.PermissionManager.SplitPermissionInfo) element$iv;
            java.lang.String sourcePermissionName = splitPermissionInfo.getSplitPermission();
            for (java.lang.Object element$iv2 : splitPermissionInfo.getNewPermissions()) {
                java.lang.String implicitPermissionName = (java.lang.String) element$iv2;
                com.android.server.permission.access.immutable.MutableIndexedMap $this$getOrPut$iv = $this$_get_implicitToSourcePermissions__u24lambda_u2442;
                java.lang.Object it$iv = $this$getOrPut$iv.get(implicitPermissionName);
                if (it$iv == null) {
                    $this$_get_implicitToSourcePermissions__u24lambda_u244 = $this$_get_implicitToSourcePermissions__u24lambda_u2442;
                    i = i2;
                    $this$forEach$iv = $this$forEach$iv2;
                    z = true;
                    it$iv = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
                    $this$getOrPut$iv.put(implicitPermissionName, it$iv);
                } else {
                    $this$_get_implicitToSourcePermissions__u24lambda_u244 = $this$_get_implicitToSourcePermissions__u24lambda_u2442;
                    i = i2;
                    $this$forEach$iv = $this$forEach$iv2;
                    z = true;
                }
                com.android.server.permission.access.immutable.MutableIndexedListSet $this$plusAssign$iv = (com.android.server.permission.access.immutable.MutableIndexedListSet) it$iv;
                $this$plusAssign$iv.add(sourcePermissionName);
                z2 = z;
                $this$forEach$iv2 = $this$forEach$iv;
                $this$_get_implicitToSourcePermissions__u24lambda_u2442 = $this$_get_implicitToSourcePermissions__u24lambda_u244;
                i2 = i;
            }
            $this$forEach$iv2 = $this$forEach$iv2;
        }
        return mutableIndexedMap;
    }

    public final void onUserAdded$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(int userId) {
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState oldState$iv = this.state;
            if (oldState$iv == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                oldState$iv = null;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onUserAdded_u24lambda_u246 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onUserAdded_u24lambda_u246_u24lambda_u245 = this.policy;
            $this$onUserAdded_u24lambda_u246_u24lambda_u245.onUserAdded($this$onUserAdded_u24lambda_u246, userId);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void onUserRemoved$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(int userId) {
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState oldState$iv = this.state;
            if (oldState$iv == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                oldState$iv = null;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onUserRemoved_u24lambda_u248 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onUserRemoved_u24lambda_u248_u24lambda_u247 = this.policy;
            $this$onUserRemoved_u24lambda_u248_u24lambda_u247.onUserRemoved($this$onUserRemoved_u24lambda_u248, userId);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void onStorageVolumeMounted$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(java.lang.String volumeUuid, java.util.List<java.lang.String> list, boolean isSystemUpdated) throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState oldState$iv = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> allPackageStates = getAllPackageStates(packageManagerLocal);
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent1 = allPackageStates.component1();
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent2 = allPackageStates.component2();
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        com.android.server.permission.access.immutable.IntMap<java.lang.String[]> knownPackages = getKnownPackages(packageManagerInternal);
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState accessState = this.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                oldState$iv = accessState;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onStorageVolumeMounted_u24lambda_u2410 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onStorageVolumeMounted_u24lambda_u2410_u24lambda_u249 = this.policy;
            $this$onStorageVolumeMounted_u24lambda_u2410_u24lambda_u249.onStorageVolumeMounted($this$onStorageVolumeMounted_u24lambda_u2410, mapComponent1, mapComponent2, knownPackages, volumeUuid, list, isSystemUpdated);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void onPackageAdded$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(java.lang.String packageName) throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState oldState$iv = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> allPackageStates = getAllPackageStates(packageManagerLocal);
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent1 = allPackageStates.component1();
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent2 = allPackageStates.component2();
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        com.android.server.permission.access.immutable.IntMap<java.lang.String[]> knownPackages = getKnownPackages(packageManagerInternal);
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState accessState = this.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                oldState$iv = accessState;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onPackageAdded_u24lambda_u2412 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onPackageAdded_u24lambda_u2412_u24lambda_u2411 = this.policy;
            $this$onPackageAdded_u24lambda_u2412_u24lambda_u2411.onPackageAdded($this$onPackageAdded_u24lambda_u2412, mapComponent1, mapComponent2, knownPackages, packageName);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void onPackageRemoved$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(java.lang.String packageName, int appId) throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState oldState$iv = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> allPackageStates = getAllPackageStates(packageManagerLocal);
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent1 = allPackageStates.component1();
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent2 = allPackageStates.component2();
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        com.android.server.permission.access.immutable.IntMap<java.lang.String[]> knownPackages = getKnownPackages(packageManagerInternal);
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState accessState = this.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                oldState$iv = accessState;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onPackageRemoved_u24lambda_u2414 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onPackageRemoved_u24lambda_u2414_u24lambda_u2413 = this.policy;
            $this$onPackageRemoved_u24lambda_u2414_u24lambda_u2413.onPackageRemoved($this$onPackageRemoved_u24lambda_u2414, mapComponent1, mapComponent2, knownPackages, packageName, appId);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void onPackageInstalled$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(java.lang.String packageName, int userId) throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState oldState$iv = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> allPackageStates = getAllPackageStates(packageManagerLocal);
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent1 = allPackageStates.component1();
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent2 = allPackageStates.component2();
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        com.android.server.permission.access.immutable.IntMap<java.lang.String[]> knownPackages = getKnownPackages(packageManagerInternal);
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState accessState = this.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                oldState$iv = accessState;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onPackageInstalled_u24lambda_u2416 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onPackageInstalled_u24lambda_u2416_u24lambda_u2415 = this.policy;
            $this$onPackageInstalled_u24lambda_u2416_u24lambda_u2415.onPackageInstalled($this$onPackageInstalled_u24lambda_u2416, mapComponent1, mapComponent2, knownPackages, packageName, userId);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void onPackageUninstalled$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(java.lang.String packageName, int appId, int userId) throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState oldState$iv = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> allPackageStates = getAllPackageStates(packageManagerLocal);
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent1 = allPackageStates.component1();
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> mapComponent2 = allPackageStates.component2();
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        com.android.server.permission.access.immutable.IntMap<java.lang.String[]> knownPackages = getKnownPackages(packageManagerInternal);
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState accessState = this.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                oldState$iv = accessState;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onPackageUninstalled_u24lambda_u2418 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onPackageUninstalled_u24lambda_u2418_u24lambda_u2417 = this.policy;
            $this$onPackageUninstalled_u24lambda_u2418_u24lambda_u2417.onPackageUninstalled($this$onPackageUninstalled_u24lambda_u2418, mapComponent1, mapComponent2, knownPackages, packageName, appId, userId);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void onSystemReady$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        synchronized (this.stateLock) {
            com.android.server.permission.access.AccessState oldState$iv = this.state;
            if (oldState$iv == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                oldState$iv = null;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$onSystemReady_u24lambda_u2420 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.AccessPolicy $this$onSystemReady_u24lambda_u2420_u24lambda_u2419 = this.policy;
            $this$onSystemReady_u24lambda_u2420_u24lambda_u2419.onSystemReady($this$onSystemReady_u24lambda_u2420);
            this.persistence.write(newState$iv);
            this.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    private final com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> getAllPackageStates(com.android.server.pm.PackageManagerLocal $this$allPackageStates) throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = $this$allPackageStates.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot it = unfilteredSnapshotWithUnfilteredSnapshot;
            com.android.server.permission.jarjar.kotlin.Pair<java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>, java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState>> pair = com.android.server.permission.jarjar.kotlin.TuplesKt.to(it.getPackageStates(), it.getDisabledSystemPackageStates());
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            return pair;
        } finally {
        }
    }

    private final com.android.server.permission.access.immutable.IntMap<java.lang.String[]> getKnownPackages(android.content.pm.PackageManagerInternal $this$knownPackages) {
        com.android.server.permission.access.immutable.MutableIntMap $this$_get_knownPackages__u24lambda_u2422 = new com.android.server.permission.access.immutable.MutableIntMap(null, 1, null);
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 2, $this$knownPackages.getKnownPackageNames(2, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 7, $this$knownPackages.getKnownPackageNames(7, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 4, $this$knownPackages.getKnownPackageNames(4, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 1, $this$knownPackages.getKnownPackageNames(1, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 6, $this$knownPackages.getKnownPackageNames(6, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 10, $this$knownPackages.getKnownPackageNames(10, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 11, $this$knownPackages.getKnownPackageNames(11, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 12, $this$knownPackages.getKnownPackageNames(12, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 15, $this$knownPackages.getKnownPackageNames(15, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 16, $this$knownPackages.getKnownPackageNames(16, 0));
        com.android.server.permission.access.immutable.IntMapExtensionsKt.set($this$_get_knownPackages__u24lambda_u2422, 17, $this$knownPackages.getKnownPackageNames(17, 0));
        return $this$_get_knownPackages__u24lambda_u2422;
    }

    public final <T> T getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.access.GetStateScope, ? extends T> function1) {
        com.android.server.permission.access.AccessState accessState = this.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        return function1.invoke(new com.android.server.permission.access.GetStateScope(accessState));
    }

    public final void mutateState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.access.MutateStateScope, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        synchronized (this.stateLock) {
            try {
                com.android.server.permission.access.AccessState oldState = this.state;
                if (oldState == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                    oldState = null;
                }
                com.android.server.permission.access.MutableAccessState newState = oldState.toMutable();
                function1.invoke(new com.android.server.permission.access.MutateStateScope(oldState, newState));
                this.persistence.write(newState);
                this.state = newState;
                com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425 = this.policy;
                $this$mutateState_u24lambda_u2426_u24lambda_u2425.onStateMutated(new com.android.server.permission.access.GetStateScope(newState));
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
            } catch (java.lang.Throwable th) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th;
            }
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
    }

    public final com.android.server.permission.access.SchemePolicy getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(java.lang.String subjectScheme, java.lang.String objectScheme) {
        return this.policy.getSchemePolicy(subjectScheme, objectScheme);
    }
}
