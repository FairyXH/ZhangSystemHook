package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: AppOpService.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 I2\u00020\u0001:\u0004IJKLB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0015H\u0016J\b\u0010#\u001a\u00020$H\u0016J\b\u0010%\u001a\u00020$H\u0002J\u001a\u0010&\u001a\u00020\u001f2\u0006\u0010'\u001a\u00020\u001f2\b\b\u0002\u0010(\u001a\u00020\u001fH\u0002J\u0018\u0010)\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001f2\u0006\u0010+\u001a\u00020\tH\u0016J\u0018\u0010)\u001a\u00020\u00112\u0006\u0010,\u001a\u00020\t2\u0006\u0010-\u001a\u00020\u001fH\u0016J\u0018\u0010.\u001a\u00020/2\u0006\u0010,\u001a\u00020\t2\u0006\u0010-\u001a\u00020\u001fH\u0016J\u0018\u00100\u001a\u00020/2\u0006\u0010*\u001a\u00020\u001f2\u0006\u0010+\u001a\u00020\tH\u0016J \u00101\u001a\u00020\u001f2\u0006\u0010,\u001a\u00020\t2\u0006\u00102\u001a\u00020\u001f2\u0006\u0010-\u001a\u00020\u001fH\u0016J&\u00103\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001f\u0018\u00010\b2\u0006\u0010,\u001a\u00020\t2\u0006\u0010-\u001a\u00020\u001fH\u0002J \u00104\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020\u001f2\u0006\u0010+\u001a\u00020\t2\u0006\u00102\u001a\u00020\u001fH\u0016J\u001e\u00105\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001f\u0018\u00010\b2\u0006\u0010*\u001a\u00020\u001fH\u0002J\u0006\u00106\u001a\u00020$J\u001e\u00107\u001a\u00020/2\u0014\u00108\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001f\u0018\u00010\bH\u0002J\b\u00109\u001a\u00020$H\u0016J\u0010\u0010:\u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0015H\u0016J\u0018\u0010;\u001a\u00020!2\u0006\u0010,\u001a\u00020\t2\u0006\u0010-\u001a\u00020\u001fH\u0016J\u0010\u0010<\u001a\u00020$2\u0006\u0010*\u001a\u00020\u001fH\u0016J(\u0010=\u001a\u00020$2\u0006\u0010,\u001a\u00020\t2\u0006\u0010>\u001a\u00020\u001f2\u0006\u0010?\u001a\u00020\u001f2\u0006\u0010-\u001a\u00020\u001fH\u0016J(\u0010@\u001a\u00020!2\u0006\u0010*\u001a\u00020\u001f2\u0006\u0010+\u001a\u00020\t2\u0006\u0010A\u001a\u00020\u001f2\u0006\u0010?\u001a\u00020\u001fH\u0016J\b\u0010B\u001a\u00020$H\u0017J\b\u0010C\u001a\u00020$H\u0016J\b\u0010D\u001a\u00020$H\u0017J,\u0010E\u001a\u00020\u001f*\u00020F2\u0006\u0010G\u001a\u00020\u001f2\u0006\u0010-\u001a\u00020\u001f2\u0006\u0010H\u001a\u00020\t2\u0006\u0010+\u001a\u00020\tH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\n0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\u001dX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001f0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006M"}, d2 = {"Lcom/android/server/permission/access/appop/AppOpService;", "Lcom/android/server/appop/AppOpsCheckingServiceInterface;", com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, "Lcom/android/server/permission/access/AccessCheckingService;", "(Lcom/android/server/permission/access/AccessCheckingService;)V", "appIdPolicy", "Lcom/android/server/permission/access/appop/AppIdAppOpPolicy;", "backgroundToForegroundPermissionNames", "Landroid/util/ArrayMap;", "", "Landroid/util/ArraySet;", "context", "Landroid/content/Context;", "devicePermissionPolicy", "Lcom/android/server/permission/access/permission/DevicePermissionPolicy;", "foregroundToBackgroundPermissionName", "foregroundableOps", "Landroid/util/SparseBooleanArray;", "handler", "Landroid/os/Handler;", "listeners", "Lcom/android/server/appop/AppOpsCheckingServiceInterface$AppOpsModeChangedListener;", "listenersLock", "", "packagePolicy", "Lcom/android/server/permission/access/appop/PackageAppOpPolicy;", "permissionPolicy", "Lcom/android/server/permission/access/permission/AppIdPermissionPolicy;", "runtimeAppOpToPermissionNames", "Landroid/util/SparseArray;", "runtimePermissionNameToAppOp", "", "addAppOpsModeChangedListener", "", "listener", "clearAllModes", "", "createPermissionAppOpMapping", "evaluateModeFromPermissionFlags", "foregroundFlags", "backgroundFlags", "getForegroundOps", "uid", "deviceId", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "userId", "getNonDefaultPackageModes", "Landroid/util/SparseIntArray;", "getNonDefaultUidModes", "getPackageMode", "op", "getPackageModes", "getUidMode", "getUidModes", "initialize", "opNameMapToOpSparseArray", "modes", "readState", "removeAppOpsModeChangedListener", "removePackage", "removeUid", "setPackageMode", "appOpCode", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "setUidMode", "code", "shutdown", "systemReady", "writeState", "getUidModeFromPermissionState", "Lcom/android/server/permission/access/GetStateScope;", "appId", "permissionName", "Companion", "OnAppIdAppOpModeChangedListener", "OnPackageAppOpModeChangedListener", "OnPermissionFlagsChangedListener", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppOpService implements com.android.server.appop.AppOpsCheckingServiceInterface {
    public static final com.android.server.permission.access.appop.AppOpService.Companion Companion = new com.android.server.permission.access.appop.AppOpService.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.appop.AppOpService.class.getSimpleName();
    private final com.android.server.permission.access.appop.AppIdAppOpPolicy appIdPolicy;
    private final android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> backgroundToForegroundPermissionNames;
    private final android.content.Context context;
    private final com.android.server.permission.access.permission.DevicePermissionPolicy devicePermissionPolicy;
    private final android.util.ArrayMap<java.lang.String, java.lang.String> foregroundToBackgroundPermissionName;
    private android.util.SparseBooleanArray foregroundableOps;
    private android.os.Handler handler;
    private volatile android.util.ArraySet<com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener> listeners;
    private final java.lang.Object listenersLock;
    private final com.android.server.permission.access.appop.PackageAppOpPolicy packagePolicy;
    private final com.android.server.permission.access.permission.AppIdPermissionPolicy permissionPolicy;
    private final android.util.SparseArray<java.lang.String> runtimeAppOpToPermissionNames;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> runtimePermissionNameToAppOp;
    private final com.android.server.permission.access.AccessCheckingService service;

    public AppOpService(com.android.server.permission.access.AccessCheckingService service) {
        this.service = service;
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("package", com.android.server.permission.access.AppOpUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar, "null cannot be cast to non-null type com.android.server.permission.access.appop.PackageAppOpPolicy");
        this.packagePolicy = (com.android.server.permission.access.appop.PackageAppOpPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar;
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2 = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("uid", com.android.server.permission.access.AppOpUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2, "null cannot be cast to non-null type com.android.server.permission.access.appop.AppIdAppOpPolicy");
        this.appIdPolicy = (com.android.server.permission.access.appop.AppIdAppOpPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2;
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3 = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("uid", com.android.server.permission.access.PermissionUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3, "null cannot be cast to non-null type com.android.server.permission.access.permission.AppIdPermissionPolicy");
        this.permissionPolicy = (com.android.server.permission.access.permission.AppIdPermissionPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3;
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar4 = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("uid", com.android.server.permission.access.DevicePermissionUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar4, "null cannot be cast to non-null type com.android.server.permission.access.permission.DevicePermissionPolicy");
        this.devicePermissionPolicy = (com.android.server.permission.access.permission.DevicePermissionPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar4;
        this.context = this.service.getContext();
        this.runtimeAppOpToPermissionNames = new android.util.SparseArray<>();
        this.runtimePermissionNameToAppOp = new android.util.ArrayMap<>();
        this.foregroundableOps = new android.util.SparseBooleanArray();
        this.foregroundToBackgroundPermissionName = new android.util.ArrayMap<>();
        this.backgroundToForegroundPermissionNames = new android.util.ArrayMap<>();
        this.listeners = new android.util.ArraySet<>();
        this.listenersLock = new java.lang.Object();
    }

    public final void initialize() {
        this.handler = new android.os.Handler(this.context.getMainLooper());
        this.appIdPolicy.addOnAppOpModeChangedListener(new com.android.server.permission.access.appop.AppOpService.OnAppIdAppOpModeChangedListener());
        this.packagePolicy.addOnAppOpModeChangedListener(new com.android.server.permission.access.appop.AppOpService.OnPackageAppOpModeChangedListener());
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void writeState() {
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void readState() {
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void shutdown() {
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void systemReady() {
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled()) {
            createPermissionAppOpMapping();
            com.android.server.permission.access.appop.AppOpService.OnPermissionFlagsChangedListener permissionListener = new com.android.server.permission.access.appop.AppOpService.OnPermissionFlagsChangedListener();
            this.permissionPolicy.addOnPermissionFlagsChangedListener(permissionListener);
            this.devicePermissionPolicy.addOnPermissionFlagsChangedListener(permissionListener);
        }
    }

    private final void createPermissionAppOpMapping() {
        android.util.ArraySet<java.lang.String> arraySet;
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$createPermissionAppOpMapping_u24lambda_u241 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$createPermissionAppOpMapping_u24lambda_u241_u24lambda_u240 = this.permissionPolicy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$createPermissionAppOpMapping_u24lambda_u241_u24lambda_u240.getPermissions($this$createPermissionAppOpMapping_u24lambda_u241);
        for (int appOpCode = 0; appOpCode < 149; appOpCode++) {
            java.lang.String permissionName = android.app.AppOpsManager.opToPermission(appOpCode);
            if (permissionName != null && appOpCode == android.app.AppOpsManager.permissionToOpCode(permissionName)) {
                com.android.server.permission.access.permission.Permission permission = permissions.get(permissionName);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(permission);
                com.android.server.permission.access.permission.Permission permission2 = permission;
                int $i$f$getProtection = permission2.getPermissionInfo().getProtection() != 1 ? 0 : 1;
                if ($i$f$getProtection != 0) {
                    this.runtimePermissionNameToAppOp.put(permissionName, java.lang.Integer.valueOf(appOpCode));
                    this.runtimeAppOpToPermissionNames.set(appOpCode, permissionName);
                    java.lang.String backgroundPermissionName = permission2.getPermissionInfo().backgroundPermission;
                    if (backgroundPermissionName != null) {
                        android.util.SparseBooleanArray $this$set$iv = this.foregroundableOps;
                        $this$set$iv.put(appOpCode, true);
                        this.foregroundToBackgroundPermissionName.put(permissionName, backgroundPermissionName);
                        java.util.Map $this$getOrPut$iv = this.backgroundToForegroundPermissionNames;
                        android.util.ArraySet<java.lang.String> arraySet2 = $this$getOrPut$iv.get(backgroundPermissionName);
                        if (arraySet2 == null) {
                            arraySet = new android.util.ArraySet<>();
                            $this$getOrPut$iv.put(backgroundPermissionName, arraySet);
                        } else {
                            arraySet = arraySet2;
                        }
                        arraySet.add(permissionName);
                    }
                }
            }
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultUidModes(int uid, java.lang.String deviceId) {
        int appId = android.os.UserHandle.getAppId(uid);
        int userId = android.os.UserHandle.getUserId(uid);
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getNonDefaultUidModes_u24lambda_u246 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.appop.AppIdAppOpPolicy $this$getNonDefaultUidModes_u24lambda_u246_u24lambda_u244 = this.appIdPolicy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> appOpModes = $this$getNonDefaultUidModes_u24lambda_u246_u24lambda_u244.getAppOpModes($this$getNonDefaultUidModes_u24lambda_u246, appId, userId);
        android.util.SparseIntArray sparseIntArrayOpNameMapToOpSparseArray = opNameMapToOpSparseArray(appOpModes != null ? appOpModes.getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() : null);
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled()) {
            android.util.ArrayMap<java.lang.String, java.lang.Integer> arrayMap = this.runtimePermissionNameToAppOp;
            int size = arrayMap.size();
            int index$iv = 0;
            while (index$iv < size) {
                java.lang.String strKeyAt = arrayMap.keyAt(index$iv);
                int appOpCode = arrayMap.valueAt(index$iv).intValue();
                java.lang.String permissionName = strKeyAt;
                int index$iv2 = index$iv;
                int i = size;
                int mode = getUidModeFromPermissionState($this$getNonDefaultUidModes_u24lambda_u246, appId, userId, permissionName, deviceId);
                if (mode != android.app.AppOpsManager.opToDefaultMode(appOpCode)) {
                    sparseIntArrayOpNameMapToOpSparseArray.put(appOpCode, mode);
                }
                index$iv = index$iv2 + 1;
                size = i;
            }
        }
        return sparseIntArrayOpNameMapToOpSparseArray;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultPackageModes(java.lang.String packageName, int userId) {
        return opNameMapToOpSparseArray(getPackageModes(packageName, userId));
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getUidMode(int uid, java.lang.String deviceId, int op) {
        int appId = android.os.UserHandle.getAppId(uid);
        int userId = android.os.UserHandle.getUserId(uid);
        java.lang.String opName = android.app.AppOpsManager.opToPublicName(op);
        java.lang.String permissionName = this.runtimeAppOpToPermissionNames.get(op);
        com.android.server.permission.access.AccessState accessState = null;
        if (!com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled() || permissionName == null) {
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
            if (accessState2 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            com.android.server.permission.access.GetStateScope $this$getUidMode_u24lambda_u248 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.appop.AppIdAppOpPolicy $this$getUidMode_u24lambda_u248_u24lambda_u247 = this.appIdPolicy;
            return $this$getUidMode_u24lambda_u248_u24lambda_u247.getAppOpMode($this$getUidMode_u24lambda_u248, appId, userId, opName);
        }
        com.android.server.permission.access.AccessCheckingService this_$iv2 = this.service;
        com.android.server.permission.access.AccessState accessState3 = this_$iv2.state;
        if (accessState3 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
        } else {
            accessState = accessState3;
        }
        com.android.server.permission.access.GetStateScope $this$getUidMode_u24lambda_u249 = new com.android.server.permission.access.GetStateScope(accessState);
        return getUidModeFromPermissionState($this$getUidMode_u24lambda_u249, appId, userId, permissionName, deviceId);
    }

    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> getUidModes(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        int userId = android.os.UserHandle.getUserId(uid);
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getUidModes_u24lambda_u2411 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.appop.AppIdAppOpPolicy $this$getUidModes_u24lambda_u2411_u24lambda_u2410 = this.appIdPolicy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> appOpModes = $this$getUidModes_u24lambda_u2411_u24lambda_u2410.getAppOpModes($this$getUidModes_u24lambda_u2411, appId, userId);
        if (appOpModes != null) {
            return appOpModes.getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar();
        }
        return null;
    }

    private final int getUidModeFromPermissionState(com.android.server.permission.access.GetStateScope $this$getUidModeFromPermissionState, int appId, int userId, java.lang.String permissionName, java.lang.String deviceId) {
        int permissionFlags;
        int permissionFlags2;
        java.lang.String fullerPermissionName;
        boolean checkDevicePermissionFlags = !com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(deviceId, "default:0") && android.permission.PermissionManager.DEVICE_AWARE_PERMISSIONS.contains(permissionName);
        if (checkDevicePermissionFlags) {
            com.android.server.permission.access.permission.DevicePermissionPolicy $this$getUidModeFromPermissionState_u24lambda_u2412 = this.devicePermissionPolicy;
            permissionFlags = $this$getUidModeFromPermissionState_u24lambda_u2412.getPermissionFlags($this$getUidModeFromPermissionState, appId, deviceId, userId, permissionName);
        } else {
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getUidModeFromPermissionState_u24lambda_u2413 = this.permissionPolicy;
            permissionFlags = $this$getUidModeFromPermissionState_u24lambda_u2413.getPermissionFlags($this$getUidModeFromPermissionState, appId, userId, permissionName);
        }
        int permissionFlags3 = permissionFlags;
        java.lang.String backgroundPermissionName = this.foregroundToBackgroundPermissionName.get(permissionName);
        if (backgroundPermissionName != null) {
            if (checkDevicePermissionFlags) {
                com.android.server.permission.access.permission.DevicePermissionPolicy $this$getUidModeFromPermissionState_u24lambda_u2414 = this.devicePermissionPolicy;
                permissionFlags2 = $this$getUidModeFromPermissionState_u24lambda_u2414.getPermissionFlags($this$getUidModeFromPermissionState, appId, deviceId, userId, backgroundPermissionName);
            } else {
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getUidModeFromPermissionState_u24lambda_u2415 = this.permissionPolicy;
                permissionFlags2 = $this$getUidModeFromPermissionState_u24lambda_u2415.getPermissionFlags($this$getUidModeFromPermissionState, appId, userId, backgroundPermissionName);
            }
        } else {
            permissionFlags2 = 16;
        }
        int backgroundPermissionFlags = permissionFlags2;
        int result = evaluateModeFromPermissionFlags(permissionFlags3, backgroundPermissionFlags);
        return (result == 1 && (fullerPermissionName = com.android.server.permission.access.permission.PermissionService.Companion.getFullerPermission(permissionName)) != null) ? getUidModeFromPermissionState($this$getUidModeFromPermissionState, appId, userId, fullerPermissionName, deviceId) : result;
    }

    static /* synthetic */ int evaluateModeFromPermissionFlags$default(com.android.server.permission.access.appop.AppOpService appOpService, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 16;
        }
        return appOpService.evaluateModeFromPermissionFlags(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int evaluateModeFromPermissionFlags(int foregroundFlags, int backgroundFlags) {
        if (com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(foregroundFlags)) {
            if (com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isAppOpGranted(backgroundFlags)) {
                return 0;
            }
            return 4;
        }
        return 1;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean setUidMode(int uid, java.lang.String deviceId, int code, int mode) {
        int appId = android.os.UserHandle.getAppId(uid);
        int userId = android.os.UserHandle.getUserId(uid);
        java.lang.String appOpName = android.app.AppOpsManager.opToPublicName(code);
        com.android.server.permission.access.AccessState accessState = null;
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled() && this.runtimeAppOpToPermissionNames.contains(code)) {
            com.android.server.permission.access.AccessState accessState2 = this.service.state;
            if (accessState2 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            com.android.server.permission.access.GetStateScope $this$setUidMode_u24lambda_u2417 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.appop.AppIdAppOpPolicy $this$setUidMode_u24lambda_u2417_u24lambda_u2416 = this.appIdPolicy;
            int oldMode = $this$setUidMode_u24lambda_u2417_u24lambda_u2416.getAppOpMode($this$setUidMode_u24lambda_u2417, appId, userId, appOpName);
            boolean wouldHaveChanged = oldMode != mode;
            java.lang.String str = wouldHaveChanged ? "Blocked" : "Ignored";
            java.lang.String logMessage = str + " setUidMode call for runtime permission app op: uid = " + uid + ", code = " + android.app.AppOpsManager.opToName(code) + ", mode = " + android.app.AppOpsManager.modeToName(mode) + ", callingUid = " + android.os.Binder.getCallingUid() + ", oldMode = " + android.app.AppOpsManager.modeToName(oldMode);
            if (wouldHaveChanged) {
                android.util.Slog.e(LOG_TAG, logMessage, new java.lang.RuntimeException());
            } else {
                android.util.Slog.w(LOG_TAG, logMessage);
            }
            return false;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.BooleanRef wasChanged = new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.BooleanRef();
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        synchronized (this_$iv.stateLock) {
            com.android.server.permission.access.AccessState accessState3 = this_$iv.state;
            if (accessState3 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState3;
            }
            com.android.server.permission.access.AccessState oldState$iv = accessState;
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$setUidMode_u24lambda_u2419 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.appop.AppIdAppOpPolicy $this$setUidMode_u24lambda_u2419_u24lambda_u2418 = this.appIdPolicy;
            wasChanged.element = java.lang.Boolean.valueOf($this$setUidMode_u24lambda_u2419_u24lambda_u2418.setAppOpMode($this$setUidMode_u24lambda_u2419, appId, userId, appOpName, mode)).booleanValue();
            this_$iv.persistence.write(newState$iv);
            this_$iv.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
        return wasChanged.element;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getPackageMode(java.lang.String packageName, int op, int userId) {
        java.lang.String opName = android.app.AppOpsManager.opToPublicName(op);
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getPackageMode_u24lambda_u2421 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.appop.PackageAppOpPolicy $this$getPackageMode_u24lambda_u2421_u24lambda_u2420 = this.packagePolicy;
        return $this$getPackageMode_u24lambda_u2421_u24lambda_u2420.getAppOpMode($this$getPackageMode_u24lambda_u2421, packageName, userId, opName);
    }

    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> getPackageModes(java.lang.String packageName, int userId) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getPackageModes_u24lambda_u2423 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.appop.PackageAppOpPolicy $this$getPackageModes_u24lambda_u2423_u24lambda_u2422 = this.packagePolicy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> appOpModes = $this$getPackageModes_u24lambda_u2423_u24lambda_u2422.getAppOpModes($this$getPackageModes_u24lambda_u2423, packageName, userId);
        if (appOpModes != null) {
            return appOpModes.getMap$frameworks__base__services__permission__android_common__services_permission_pre_jarjar();
        }
        return null;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void setPackageMode(java.lang.String packageName, int appOpCode, int mode, int userId) {
        java.lang.String appOpName = android.app.AppOpsManager.opToPublicName(appOpCode);
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled() && this.runtimeAppOpToPermissionNames.contains(appOpCode)) {
            android.util.Slog.w(LOG_TAG, "(packageName=" + packageName + ", userId=" + userId + ")'s appop state for runtime op " + appOpName + " should not be set directly.", new java.lang.RuntimeException());
            return;
        }
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        synchronized (this_$iv.stateLock) {
            com.android.server.permission.access.AccessState accessState = this_$iv.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            com.android.server.permission.access.AccessState oldState$iv = accessState;
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$setPackageMode_u24lambda_u2425 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.appop.PackageAppOpPolicy $this$setPackageMode_u24lambda_u2425_u24lambda_u2424 = this.packagePolicy;
            $this$setPackageMode_u24lambda_u2425_u24lambda_u2424.setAppOpMode($this$setPackageMode_u24lambda_u2425, packageName, userId, appOpName, mode);
            this_$iv.persistence.write(newState$iv);
            this_$iv.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeUid(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        int userId = android.os.UserHandle.getUserId(uid);
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        synchronized (this_$iv.stateLock) {
            com.android.server.permission.access.AccessState oldState$iv = this_$iv.state;
            if (oldState$iv == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                oldState$iv = null;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$removeUid_u24lambda_u2427 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.appop.AppIdAppOpPolicy $this$removeUid_u24lambda_u2427_u24lambda_u2426 = this.appIdPolicy;
            $this$removeUid_u24lambda_u2427_u24lambda_u2426.removeAppOpModes($this$removeUid_u24lambda_u2427, appId, userId);
            this_$iv.persistence.write(newState$iv);
            this_$iv.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removePackage(java.lang.String packageName, int userId) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.BooleanRef wasChanged = new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.BooleanRef();
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        synchronized (this_$iv.stateLock) {
            com.android.server.permission.access.AccessState oldState$iv = this_$iv.state;
            if (oldState$iv == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                oldState$iv = null;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$removePackage_u24lambda_u2429 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.appop.PackageAppOpPolicy $this$removePackage_u24lambda_u2429_u24lambda_u2428 = this.packagePolicy;
            wasChanged.element = java.lang.Boolean.valueOf($this$removePackage_u24lambda_u2429_u24lambda_u2428.removeAppOpModes($this$removePackage_u24lambda_u2429, packageName, userId)).booleanValue();
            this_$iv.persistence.write(newState$iv);
            this_$iv.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
        return wasChanged.element;
    }

    private final android.util.SparseIntArray opNameMapToOpSparseArray(android.util.ArrayMap<java.lang.String, java.lang.Integer> arrayMap) {
        if (arrayMap == null) {
            return new android.util.SparseIntArray();
        }
        android.util.SparseIntArray opSparseArray = new android.util.SparseIntArray(arrayMap.size());
        int size = arrayMap.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String strKeyAt = arrayMap.keyAt(index$iv);
            int opMode = arrayMap.valueAt(index$iv).intValue();
            java.lang.String opName = strKeyAt;
            opSparseArray.put(android.app.AppOpsManager.strOpToOp(opName), opMode);
        }
        return opSparseArray;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void clearAllModes() {
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(int uid, java.lang.String deviceId) {
        android.util.SparseBooleanArray $this$getForegroundOps_u24lambda_u2433 = new android.util.SparseBooleanArray();
        android.util.ArrayMap<java.lang.String, java.lang.Integer> uidModes = getUidModes(uid);
        if (uidModes != null) {
            int size = uidModes.size();
            for (int index$iv = 0; index$iv < size; index$iv++) {
                java.lang.String strKeyAt = uidModes.keyAt(index$iv);
                int mode = uidModes.valueAt(index$iv).intValue();
                java.lang.String op = strKeyAt;
                if (mode == 4) {
                    int key$iv = android.app.AppOpsManager.strOpToOp(op);
                    $this$getForegroundOps_u24lambda_u2433.put(key$iv, true);
                }
            }
        }
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled()) {
            android.util.SparseBooleanArray $this$forEachIndexed$iv = this.foregroundableOps;
            int size2 = $this$forEachIndexed$iv.size();
            for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
                int op2 = $this$forEachIndexed$iv.keyAt(index$iv2);
                $this$forEachIndexed$iv.valueAt(index$iv2);
                if (getUidMode(uid, deviceId, op2) == 4) {
                    $this$getForegroundOps_u24lambda_u2433.put(op2, true);
                }
            }
        }
        return $this$getForegroundOps_u24lambda_u2433;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(java.lang.String packageName, int userId) {
        android.util.SparseBooleanArray $this$getForegroundOps_u24lambda_u2436 = new android.util.SparseBooleanArray();
        android.util.ArrayMap<java.lang.String, java.lang.Integer> packageModes = getPackageModes(packageName, userId);
        if (packageModes != null) {
            int size = packageModes.size();
            for (int index$iv = 0; index$iv < size; index$iv++) {
                java.lang.String strKeyAt = packageModes.keyAt(index$iv);
                int mode = packageModes.valueAt(index$iv).intValue();
                java.lang.String op = strKeyAt;
                if (mode == 4) {
                    int key$iv = android.app.AppOpsManager.strOpToOp(op);
                    $this$getForegroundOps_u24lambda_u2436.put(key$iv, true);
                }
            }
        }
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.runtimePermissionAppopsMappingEnabled()) {
            android.util.SparseBooleanArray $this$forEachIndexed$iv = this.foregroundableOps;
            int size2 = $this$forEachIndexed$iv.size();
            for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
                int op2 = $this$forEachIndexed$iv.keyAt(index$iv2);
                $this$forEachIndexed$iv.valueAt(index$iv2);
                if (getPackageMode(packageName, op2, userId) == 4) {
                    $this$getForegroundOps_u24lambda_u2436.put(op2, true);
                }
            }
        }
        return $this$getForegroundOps_u24lambda_u2436;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean addAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        boolean zAdd;
        synchronized (this.listenersLock) {
            android.util.ArraySet<com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener> arraySet = new android.util.ArraySet<>(this.listeners);
            zAdd = arraySet.add(listener);
            this.listeners = arraySet;
        }
        return zAdd;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removeAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        boolean zRemove;
        synchronized (this.listenersLock) {
            android.util.ArraySet<com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener> arraySet = new android.util.ArraySet<>(this.listeners);
            zRemove = arraySet.remove(listener);
            this.listeners = arraySet;
        }
        return zRemove;
    }

    /* JADX INFO: compiled from: AppOpService.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J0\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u0005H\u0016J\b\u0010\u000e\u001a\u00020\u0007H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/android/server/permission/access/appop/AppOpService$OnAppIdAppOpModeChangedListener;", "Lcom/android/server/permission/access/appop/AppIdAppOpPolicy$OnAppOpModeChangedListener;", "(Lcom/android/server/permission/access/appop/AppOpService;)V", "pendingChanges", "Landroid/util/LongSparseArray;", "", "onAppOpModeChanged", "", "appId", "userId", "appOpName", "", "oldMode", "newMode", "onStateMutated", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class OnAppIdAppOpModeChangedListener extends com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener {
        private final android.util.LongSparseArray<java.lang.Integer> pendingChanges = new android.util.LongSparseArray<>();

        public OnAppIdAppOpModeChangedListener() {
        }

        @Override // com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener
        public void onAppOpModeChanged(int appId, int userId, java.lang.String appOpName, int oldMode, int newMode) {
            int uid = android.os.UserHandle.getUid(userId, appId);
            int appOpCode = android.app.AppOpsManager.strOpToOp(appOpName);
            long key = com.android.internal.util.IntPair.of(uid, appOpCode);
            this.pendingChanges.put(key, java.lang.Integer.valueOf(newMode));
        }

        @Override // com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener
        public void onStateMutated() {
            android.util.ArraySet listenersLocal = com.android.server.permission.access.appop.AppOpService.this.listeners;
            android.util.LongSparseArray<java.lang.Integer> longSparseArray = this.pendingChanges;
            int $i$f$forEachIndexed = 0;
            int size = longSparseArray.size();
            for (int index$iv = 0; index$iv < size; index$iv++) {
                long key = longSparseArray.keyAt(index$iv);
                int mode = longSparseArray.valueAt(index$iv).intValue();
                android.util.ArraySet $this$forEachIndexed$iv = listenersLocal;
                int index$iv2 = 0;
                int size2 = $this$forEachIndexed$iv.size();
                while (index$iv2 < size2) {
                    com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener = (com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener) $this$forEachIndexed$iv.valueAt(index$iv2);
                    android.util.ArraySet listenersLocal2 = listenersLocal;
                    int uid = com.android.internal.util.IntPair.first(key);
                    android.util.LongSparseArray<java.lang.Integer> longSparseArray2 = longSparseArray;
                    int appOpCode = com.android.internal.util.IntPair.second(key);
                    listener.onUidModeChanged(uid, appOpCode, mode, "default:0");
                    index$iv2++;
                    listenersLocal = listenersLocal2;
                    longSparseArray = longSparseArray2;
                    $i$f$forEachIndexed = $i$f$forEachIndexed;
                }
            }
            this.pendingChanges.clear();
        }
    }

    /* JADX INFO: compiled from: AppOpService.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J0\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\b\u0010\u000f\u001a\u00020\tH\u0016R,\u0010\u0003\u001a \u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/android/server/permission/access/appop/AppOpService$OnPackageAppOpModeChangedListener;", "Lcom/android/server/permission/access/appop/PackageAppOpPolicy$OnAppOpModeChangedListener;", "(Lcom/android/server/permission/access/appop/AppOpService;)V", "pendingChanges", "Landroid/util/ArrayMap;", "Lkotlin/Triple;", "", "", "onAppOpModeChanged", "", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "userId", "appOpName", "oldMode", "newMode", "onStateMutated", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class OnPackageAppOpModeChangedListener extends com.android.server.permission.access.appop.PackageAppOpPolicy.OnAppOpModeChangedListener {
        private final android.util.ArrayMap<com.android.server.permission.jarjar.kotlin.Triple<java.lang.String, java.lang.Integer, java.lang.Integer>, java.lang.Integer> pendingChanges = new android.util.ArrayMap<>();

        public OnPackageAppOpModeChangedListener() {
        }

        @Override // com.android.server.permission.access.appop.PackageAppOpPolicy.OnAppOpModeChangedListener
        public void onAppOpModeChanged(java.lang.String packageName, int userId, java.lang.String appOpName, int oldMode, int newMode) {
            int appOpCode = android.app.AppOpsManager.strOpToOp(appOpName);
            this.pendingChanges.put(new com.android.server.permission.jarjar.kotlin.Triple<>(packageName, java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(appOpCode)), java.lang.Integer.valueOf(newMode));
        }

        @Override // com.android.server.permission.access.appop.PackageAppOpPolicy.OnAppOpModeChangedListener
        public void onStateMutated() {
            android.util.ArraySet listenersLocal = com.android.server.permission.access.appop.AppOpService.this.listeners;
            android.util.ArrayMap<com.android.server.permission.jarjar.kotlin.Triple<java.lang.String, java.lang.Integer, java.lang.Integer>, java.lang.Integer> arrayMap = this.pendingChanges;
            int size = arrayMap.size();
            for (int index$iv = 0; index$iv < size; index$iv++) {
                com.android.server.permission.jarjar.kotlin.Triple<java.lang.String, java.lang.Integer, java.lang.Integer> tripleKeyAt = arrayMap.keyAt(index$iv);
                int mode = arrayMap.valueAt(index$iv).intValue();
                com.android.server.permission.jarjar.kotlin.Triple<java.lang.String, java.lang.Integer, java.lang.Integer> triple = tripleKeyAt;
                android.util.ArraySet $this$forEachIndexed$iv = listenersLocal;
                int index$iv2 = 0;
                int size2 = $this$forEachIndexed$iv.size();
                while (index$iv2 < size2) {
                    com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener = (com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener) $this$forEachIndexed$iv.valueAt(index$iv2);
                    java.lang.String packageName = triple.getFirst();
                    android.util.ArraySet listenersLocal2 = listenersLocal;
                    int userId = triple.getSecond().intValue();
                    android.util.ArrayMap<com.android.server.permission.jarjar.kotlin.Triple<java.lang.String, java.lang.Integer, java.lang.Integer>, java.lang.Integer> arrayMap2 = arrayMap;
                    int appOpCode = triple.getThird().intValue();
                    listener.onPackageModeChanged(packageName, userId, appOpCode, mode);
                    index$iv2++;
                    listenersLocal = listenersLocal2;
                    arrayMap = arrayMap2;
                }
            }
            this.pendingChanges.clear();
        }
    }

    /* JADX INFO: compiled from: AppOpService.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0010\b\u0082\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003JH\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0007H\u0002J \u0010\u0013\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\bH\u0002J8\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0007H\u0016J0\u0010\u0018\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0007H\u0016J\b\u0010\u0019\u001a\u00020\nH\u0016R,\u0010\u0004\u001a \u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/android/server/permission/access/appop/AppOpService$OnPermissionFlagsChangedListener;", "Lcom/android/server/permission/access/permission/AppIdPermissionPolicy$OnPermissionFlagsChangedListener;", "Lcom/android/server/permission/access/permission/DevicePermissionPolicy$OnDevicePermissionFlagsChangedListener;", "(Lcom/android/server/permission/access/appop/AppOpService;)V", "pendingChanges", "Landroid/util/ArrayMap;", "Lkotlin/Triple;", "", "", "addPendingChangedModeIfNeeded", "", "appId", "userId", "deviceId", "appOpCode", "oldForegroundFlags", "oldBackgroundFlags", "newForegroundFlags", "newBackgroundFlags", "getPermissionFlags", "permissionName", "onDevicePermissionFlagsChanged", "oldFlags", "newFlags", "onPermissionFlagsChanged", "onStateMutated", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class OnPermissionFlagsChangedListener implements com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener, com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener {
        private final android.util.ArrayMap<com.android.server.permission.jarjar.kotlin.Triple<java.lang.Integer, java.lang.String, java.lang.Integer>, java.lang.Integer> pendingChanges = new android.util.ArrayMap<>();

        public OnPermissionFlagsChangedListener() {
        }

        @Override // com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener
        public void onPermissionFlagsChanged(int appId, int userId, java.lang.String permissionName, int oldFlags, int newFlags) {
            onDevicePermissionFlagsChanged(appId, userId, "default:0", permissionName, oldFlags, newFlags);
        }

        @Override // com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener
        public void onDevicePermissionFlagsChanged(int appId, int userId, java.lang.String deviceId, java.lang.String permissionName, int oldFlags, int newFlags) {
            java.lang.Integer appOpCode;
            java.lang.Integer appOpCode2;
            int index$iv;
            int i;
            android.util.ArraySet $this$forEachIndexed$iv;
            android.util.ArraySet foregroundPermissions = (android.util.ArraySet) com.android.server.permission.access.appop.AppOpService.this.backgroundToForegroundPermissionNames.get(permissionName);
            if (foregroundPermissions == null) {
                java.lang.String backgroundPermission = (java.lang.String) com.android.server.permission.access.appop.AppOpService.this.foregroundToBackgroundPermissionName.get(permissionName);
                com.android.server.permission.jarjar.kotlin.Unit unit = null;
                if (backgroundPermission != null && (appOpCode2 = (java.lang.Integer) com.android.server.permission.access.appop.AppOpService.this.runtimePermissionNameToAppOp.get(permissionName)) != null) {
                    int backgroundPermissionFlags = getPermissionFlags(appId, userId, backgroundPermission);
                    addPendingChangedModeIfNeeded(appId, userId, deviceId, appOpCode2.intValue(), oldFlags, backgroundPermissionFlags, newFlags, backgroundPermissionFlags);
                    unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                }
                if (unit == null && (appOpCode = (java.lang.Integer) com.android.server.permission.access.appop.AppOpService.this.runtimePermissionNameToAppOp.get(permissionName)) != null) {
                    addPendingChangedModeIfNeeded(appId, userId, deviceId, appOpCode.intValue(), oldFlags, 16, newFlags, 16);
                    com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                    return;
                }
                return;
            }
            com.android.server.permission.access.appop.AppOpService appOpService = com.android.server.permission.access.appop.AppOpService.this;
            android.util.ArraySet $this$forEachIndexed$iv2 = foregroundPermissions;
            int size = $this$forEachIndexed$iv2.size();
            int index$iv2 = 0;
            while (index$iv2 < size) {
                java.lang.String foregroundPermissionName = (java.lang.String) $this$forEachIndexed$iv2.valueAt(index$iv2);
                java.lang.Integer appOpCode3 = (java.lang.Integer) appOpService.runtimePermissionNameToAppOp.get(foregroundPermissionName);
                if (appOpCode3 != null) {
                    int foregroundPermissionFlags = getPermissionFlags(appId, userId, foregroundPermissionName);
                    int index$iv3 = appOpCode3.intValue();
                    index$iv = index$iv2;
                    i = size;
                    $this$forEachIndexed$iv = $this$forEachIndexed$iv2;
                    addPendingChangedModeIfNeeded(appId, userId, deviceId, index$iv3, foregroundPermissionFlags, oldFlags, foregroundPermissionFlags, newFlags);
                } else {
                    index$iv = index$iv2;
                    i = size;
                    $this$forEachIndexed$iv = $this$forEachIndexed$iv2;
                }
                index$iv2 = index$iv + 1;
                size = i;
                $this$forEachIndexed$iv2 = $this$forEachIndexed$iv;
            }
            com.android.server.permission.jarjar.kotlin.Unit unit3 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }

        private final int getPermissionFlags(int appId, int userId, java.lang.String permissionName) {
            com.android.server.permission.access.AccessCheckingService this_$iv = com.android.server.permission.access.appop.AppOpService.this.service;
            com.android.server.permission.access.appop.AppOpService appOpService = com.android.server.permission.access.appop.AppOpService.this;
            com.android.server.permission.access.AccessState accessState = this_$iv.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            com.android.server.permission.access.GetStateScope $this$getPermissionFlags_u24lambda_u247 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionFlags_u24lambda_u247_u24lambda_u246 = appOpService.permissionPolicy;
            return $this$getPermissionFlags_u24lambda_u247_u24lambda_u246.getPermissionFlags($this$getPermissionFlags_u24lambda_u247, appId, userId, permissionName);
        }

        private final void addPendingChangedModeIfNeeded(int appId, int userId, java.lang.String deviceId, int appOpCode, int oldForegroundFlags, int oldBackgroundFlags, int newForegroundFlags, int newBackgroundFlags) {
            int oldMode = com.android.server.permission.access.appop.AppOpService.this.evaluateModeFromPermissionFlags(oldForegroundFlags, oldBackgroundFlags);
            int newMode = com.android.server.permission.access.appop.AppOpService.this.evaluateModeFromPermissionFlags(newForegroundFlags, newBackgroundFlags);
            if (oldMode != newMode) {
                int uid = android.os.UserHandle.getUid(userId, appId);
                this.pendingChanges.put(new com.android.server.permission.jarjar.kotlin.Triple<>(java.lang.Integer.valueOf(uid), deviceId, java.lang.Integer.valueOf(appOpCode)), java.lang.Integer.valueOf(newMode));
            }
        }

        @Override // com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener, com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener
        public void onStateMutated() {
            android.util.ArraySet listenersLocal = com.android.server.permission.access.appop.AppOpService.this.listeners;
            android.util.ArrayMap<com.android.server.permission.jarjar.kotlin.Triple<java.lang.Integer, java.lang.String, java.lang.Integer>, java.lang.Integer> arrayMap = this.pendingChanges;
            int size = arrayMap.size();
            for (int index$iv = 0; index$iv < size; index$iv++) {
                com.android.server.permission.jarjar.kotlin.Triple<java.lang.Integer, java.lang.String, java.lang.Integer> tripleKeyAt = arrayMap.keyAt(index$iv);
                int mode = arrayMap.valueAt(index$iv).intValue();
                com.android.server.permission.jarjar.kotlin.Triple<java.lang.Integer, java.lang.String, java.lang.Integer> triple = tripleKeyAt;
                android.util.ArraySet $this$forEachIndexed$iv = listenersLocal;
                int index$iv2 = 0;
                int size2 = $this$forEachIndexed$iv.size();
                while (index$iv2 < size2) {
                    com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener = (com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener) $this$forEachIndexed$iv.valueAt(index$iv2);
                    int uid = triple.getFirst().intValue();
                    android.util.ArraySet listenersLocal2 = listenersLocal;
                    java.lang.String deviceId = triple.getSecond();
                    android.util.ArrayMap<com.android.server.permission.jarjar.kotlin.Triple<java.lang.Integer, java.lang.String, java.lang.Integer>, java.lang.Integer> arrayMap2 = arrayMap;
                    int appOpCode = triple.getThird().intValue();
                    listener.onUidModeChanged(uid, appOpCode, mode, deviceId);
                    index$iv2++;
                    listenersLocal = listenersLocal2;
                    arrayMap = arrayMap2;
                }
            }
            this.pendingChanges.clear();
        }
    }

    /* JADX INFO: compiled from: AppOpService.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/android/server/permission/access/appop/AppOpService$Companion;", "", "()V", "LOG_TAG", "", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
