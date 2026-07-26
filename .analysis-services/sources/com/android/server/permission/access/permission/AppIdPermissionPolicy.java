package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: AppIdPermissionPolicy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000È\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u0000 \u0083\u00012\u00020\u0001:\u0004\u0083\u0001\u0084\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0010J(\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u000bH\u0002J\u0018\u0010'\u001a\u00020\u00042\u0006\u0010(\u001a\u00020)2\u0006\u0010&\u001a\u00020\u000bH\u0002J \u0010*\u001a\u00020\u00042\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020!2\u0006\u0010&\u001a\u00020\u000bH\u0002J\u0010\u0010.\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020/H\u0016J\u0018\u00100\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020/2\u0006\u0010%\u001a\u00020!H\u0016J\u000e\u00101\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0010J\u001c\u00102\u001a\u00020\u001e*\u0002032\u0006\u00104\u001a\u0002052\b\b\u0002\u00106\u001a\u00020\u0004J\u0014\u00107\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,H\u0002J\u0012\u00108\u001a\u00020\u001e*\u0002032\u0006\u00104\u001a\u000205J\"\u00109\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,2\f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0018H\u0002J\"\u0010;\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,2\f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0018H\u0002J3\u0010<\u001a\u00020\u0004*\u0002032\u0006\u0010$\u001a\u00020!2\b\b\u0002\u0010\"\u001a\u00020#2\u0012\u0010=\u001a\u000e\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020\u00040>H\u0082\bJ\u001c\u0010?\u001a\u00020\u0004*\u0002032\u0006\u0010@\u001a\u00020\u000b2\u0006\u0010A\u001a\u00020\u000bH\u0002J\u001c\u0010B\u001a\u00020\u0004*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u00104\u001a\u000205H\u0002J\u001c\u0010C\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u0010%\u001a\u00020!H\u0002J\u001e\u0010D\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,2\b\u0010E\u001a\u0004\u0018\u00010,H\u0002J&\u0010F\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u0010%\u001a\u00020!2\b\u0010E\u001a\u0004\u0018\u00010,H\u0002J.\u0010G\u001a\u00020\u001e*\u0002032\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u000b2\b\u0010E\u001a\u0004\u0018\u00010,H\u0002J\u001e\u0010H\u001a\u00020\u001e*\u0002032\u0006\u0010&\u001a\u00020\u000b2\b\u0010E\u001a\u0004\u0018\u00010,H\u0002J\u0014\u0010I\u001a\u0004\u0018\u000105*\u00020J2\u0006\u0010&\u001a\u00020\u000bJ3\u0010K\u001a\u00020\u001e*\u0002032\u0006\u0010$\u001a\u00020!2\b\b\u0002\u0010\"\u001a\u00020#2\u0012\u0010L\u001a\u000e\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020\u001e0>H\u0082\bJ(\u0010M\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020!\u0018\u00010N*\u00020J2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!J$\u0010O\u001a\u00020!*\u0002032\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u000bH\u0002J\"\u0010 \u001a\u00020!*\u00020J2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u000bJ\u0016\u0010P\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020Q0N*\u00020JJ\u0016\u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u0002050N*\u00020JJ\u0016\u0010S\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u0002050N*\u00020JJ#\u0010T\u001a\u0004\u0018\u00010\u0004*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u0010&\u001a\u00020\u000bH\u0002¢\u0006\u0002\u0010UJ#\u0010V\u001a\u0004\u0018\u00010\u0004*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u0010&\u001a\u00020\u000bH\u0002¢\u0006\u0002\u0010UJ(\u0010W\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020!\u0018\u00010N*\u00020J2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!J\u001c\u0010X\u001a\u00020\u001e*\u0002032\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!H\u0002J\u0014\u0010Y\u001a\u00020\u001e*\u0002032\u0006\u0010$\u001a\u00020!H\u0016J\u0014\u0010Z\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,H\u0016J\u001c\u0010[\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u0010%\u001a\u00020!H\u0016J\u001c\u0010\\\u001a\u00020\u001e*\u0002032\u0006\u0010@\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020!H\u0016J$\u0010]\u001a\u00020\u001e*\u0002032\u0006\u0010@\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!H\u0016J\f\u0010^\u001a\u00020\u001e*\u00020JH\u0016J,\u0010_\u001a\u00020\u001e*\u0002032\b\u0010`\u001a\u0004\u0018\u00010\u000b2\f\u0010a\u001a\b\u0012\u0004\u0012\u00020\u000b0b2\u0006\u0010c\u001a\u00020\u0004H\u0016J\f\u0010d\u001a\u00020\u001e*\u000203H\u0016J\u0014\u0010e\u001a\u00020\u001e*\u0002032\u0006\u0010%\u001a\u00020!H\u0016J\u0014\u0010f\u001a\u00020\u001e*\u00020g2\u0006\u0010\"\u001a\u00020/H\u0016J\u001c\u0010h\u001a\u00020\u001e*\u00020g2\u0006\u0010\"\u001a\u00020/2\u0006\u0010%\u001a\u00020!H\u0016JA\u0010i\u001a\u00020!*\u0002032\u0006\u0010$\u001a\u00020!2\u0006\u0010j\u001a\u00020!2\b\b\u0002\u0010\"\u001a\u00020#2\u0018\u0010k\u001a\u0014\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020,\u0012\u0004\u0012\u00020!0lH\u0082\bJ\u0012\u0010m\u001a\u00020\u001e*\u0002032\u0006\u00104\u001a\u000205J\u001a\u0010n\u001a\u00020\u001e*\u0002032\u0006\u0010@\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020!J\u0014\u0010o\u001a\u00020\u001e*\u0002032\u0006\u0010$\u001a\u00020!H\u0002J\u0014\u0010p\u001a\u00020\u001e*\u00020q2\u0006\u0010\"\u001a\u00020#H\u0016J\u001c\u0010r\u001a\u00020\u001e*\u00020q2\u0006\u0010\"\u001a\u00020#2\u0006\u0010%\u001a\u00020!H\u0016J*\u0010s\u001a\u00020\u0004*\u0002032\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u000b2\u0006\u0010t\u001a\u00020!J\u001c\u0010u\u001a\u00020\u0004*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u00104\u001a\u000205H\u0002J\u001c\u0010v\u001a\u00020\u0004*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u00104\u001a\u000205H\u0002J\u001c\u0010w\u001a\u00020\u0004*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u00104\u001a\u000205H\u0002J\u0014\u0010x\u001a\u00020\u001e*\u0002032\u0006\u0010$\u001a\u00020!H\u0002J\"\u0010y\u001a\u00020\u001e*\u0002032\u0006\u0010@\u001a\u00020\u000b2\f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0018H\u0002J2\u0010z\u001a\u00020\u001e*\u0002032\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\u0006\u00104\u001a\u0002052\u0006\u0010{\u001a\u00020!2\u0006\u0010|\u001a\u00020!J2\u0010}\u001a\u00020\u0004*\u0002032\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u000b2\u0006\u0010~\u001a\u00020!2\u0006\u0010\u007f\u001a\u00020!J\u0015\u0010\u0080\u0001\u001a\u000205*\u0002032\u0006\u00104\u001a\u000205H\u0002J&\u0010\u0081\u0001\u001a\u00020\u001e*\u0002032\u0006\u0010+\u001a\u00020,2\u0006\u0010%\u001a\u00020!2\u0007\u0010\u0082\u0001\u001a\u00020!H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0003\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\u00020\u000b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\rR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0085\u0001"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionPolicy;", "Lcom/android/server/permission/access/SchemePolicy;", "()V", "isSignaturePermissionAllowlistForceEnforced", "", "()Z", "setSignaturePermissionAllowlistForceEnforced", "(Z)V", "migration", "Lcom/android/server/permission/access/permission/AppIdPermissionMigration;", "objectScheme", "", "getObjectScheme", "()Ljava/lang/String;", "onPermissionFlagsChangedListeners", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "Lcom/android/server/permission/access/permission/AppIdPermissionPolicy$OnPermissionFlagsChangedListener;", "onPermissionFlagsChangedListenersLock", "", "permissionManagerServiceExt", "Lcom/android/server/pm/permission/IPermissionManagerServiceExt;", "persistence", "Lcom/android/server/permission/access/permission/AppIdPermissionPersistence;", "privilegedPermissionAllowlistViolations", "Lcom/android/server/permission/access/immutable/MutableIndexedSet;", "subjectScheme", "getSubjectScheme", "upgrade", "Lcom/android/server/permission/access/permission/AppIdPermissionUpgrade;", "addOnPermissionFlagsChangedListener", "", "listener", "getPermissionFlags", "", "state", "Lcom/android/server/permission/access/AccessState;", "appId", "userId", "permissionName", "isCompatibilityPermissionForPackage", "androidPackage", "Lcom/android/server/pm/pkg/AndroidPackage;", "isSoftRestrictedPermissionExemptForPackage", "packageState", "Lcom/android/server/pm/pkg/PackageState;", "appIdTargetSdkVersion", "migrateSystemState", "Lcom/android/server/permission/access/MutableAccessState;", "migrateUserState", "removeOnPermissionFlagsChangedListener", "addPermission", "Lcom/android/server/permission/access/MutateStateScope;", com.android.server.permission.access.PermissionUri.SCHEME, "Lcom/android/server/permission/access/permission/Permission;", "isSynchronousWrite", "addPermissionGroups", "addPermissionTree", "addPermissions", "changedPermissionNames", "adoptPermissions", "anyPackageInAppId", "predicate", "Lkotlin/Function1;", "canAdoptPermissions", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "originalPackageName", "checkPrivilegedPermissionAllowlist", "clearRestrictedPermissionImplicitExemption", "evaluateAllPermissionStatesForPackage", "installedPackageState", "evaluateAllPermissionStatesForPackageAndUser", "evaluatePermissionState", "evaluatePermissionStateForAllPackages", "findPermissionTree", "Lcom/android/server/permission/access/GetStateScope;", "forEachPackageInAppId", "action", "getAllPermissionFlags", "Lcom/android/server/permission/access/immutable/IndexedMap;", "getOldStatePermissionFlags", "getPermissionGroups", "Landroid/content/pm/PermissionGroupInfo;", "getPermissionTrees", "getPermissions", "getPrivilegedPermissionAllowlistState", "(Lcom/android/server/permission/access/MutateStateScope;Lcom/android/server/pm/pkg/PackageState;Ljava/lang/String;)Ljava/lang/Boolean;", "getSignaturePermissionAllowlistState", "getUidPermissionFlags", "inheritImplicitPermissionStates", "onAppIdRemoved", "onPackageAdded", "onPackageInstalled", "onPackageRemoved", "onPackageUninstalled", "onStateMutated", "onStorageVolumeMounted", "volumeUuid", com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY, "", "isSystemUpdated", "onSystemReady", "onUserAdded", "parseSystemState", "Lcom/android/modules/utils/BinaryXmlPullParser;", "parseUserState", "reducePackageInAppId", "initialValue", "accumulator", "Lkotlin/Function2;", "removePermission", "resetRuntimePermissions", "revokePermissionsOnPackageUpdate", "serializeSystemState", "Lcom/android/modules/utils/BinaryXmlSerializer;", "serializeUserState", "setPermissionFlags", "flags", "shouldGrantPermissionByProtectionFlags", "shouldGrantPermissionBySignature", "shouldGrantPrivilegedOrOemPermission", "trimPermissionStates", "trimPermissions", "updatePermissionExemptFlags", "exemptFlagMask", "exemptFlagValues", "updatePermissionFlags", "flagMask", "flagValues", "updatePermissionIfDynamic", "upgradePackageState", "version", "Companion", "OnPermissionFlagsChangedListener", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppIdPermissionPolicy extends com.android.server.permission.access.SchemePolicy {
    private static final java.lang.String PLATFORM_PACKAGE_NAME = "android";
    private static final int SYSTEM_OR_POLICY_FIXED_MASK = 384;
    private static final int USER_SETTABLE_MASK = 15728736;
    private volatile boolean isSignaturePermissionAllowlistForceEnforced;
    private final com.android.server.pm.permission.IPermissionManagerServiceExt permissionManagerServiceExt;
    public static final com.android.server.permission.access.permission.AppIdPermissionPolicy.Companion Companion = new com.android.server.permission.access.permission.AppIdPermissionPolicy.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.permission.AppIdPermissionPolicy.class.getSimpleName();
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> NO_IMPLICIT_FLAG_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.ACCESS_MEDIA_LOCATION", "android.permission.ACTIVITY_RECOGNITION", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> NEARBY_DEVICES_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN", "android.permission.NEARBY_WIFI_DEVICES");
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> NOTIFICATIONS_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.POST_NOTIFICATIONS");
    private static final com.android.server.permission.access.immutable.IndexedSet<java.lang.String> STORAGE_AND_MEDIA_PERMISSIONS = com.android.server.permission.access.immutable.IndexedSetExtensionsKt.indexedSetOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_IMAGES", "android.permission.ACCESS_MEDIA_LOCATION", "android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
    private final com.android.server.permission.access.permission.AppIdPermissionPersistence persistence = new com.android.server.permission.access.permission.AppIdPermissionPersistence();
    private final com.android.server.permission.access.permission.AppIdPermissionMigration migration = new com.android.server.permission.access.permission.AppIdPermissionMigration();
    private final com.android.server.permission.access.permission.AppIdPermissionUpgrade upgrade = new com.android.server.permission.access.permission.AppIdPermissionUpgrade(this);
    private volatile com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener> onPermissionFlagsChangedListeners = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
    private final java.lang.Object onPermissionFlagsChangedListenersLock = new java.lang.Object();
    private final com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> privilegedPermissionAllowlistViolations = new com.android.server.permission.access.immutable.MutableIndexedSet<>(null, 1, null);

    /* JADX INFO: compiled from: AppIdPermissionPolicy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005H&J\b\u0010\u000b\u001a\u00020\u0003H&ø\u0001\u0000\u0082\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionPolicy$OnPermissionFlagsChangedListener;", "", "onPermissionFlagsChanged", "", "appId", "", "userId", "permissionName", "", "oldFlags", "newFlags", "onStateMutated", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface OnPermissionFlagsChangedListener {
        void onPermissionFlagsChanged(int i, int i2, java.lang.String str, int i3, int i4);

        void onStateMutated();
    }

    public AppIdPermissionPolicy() {
        java.lang.Object objCreate = system.ext.loader.core.ExtLoader.type(com.android.server.pm.permission.IPermissionManagerServiceExt.class).base(this).create();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(objCreate);
        this.permissionManagerServiceExt = (com.android.server.pm.permission.IPermissionManagerServiceExt) objCreate;
    }

    public final boolean isSignaturePermissionAllowlistForceEnforced() {
        return this.isSignaturePermissionAllowlistForceEnforced;
    }

    public final void setSignaturePermissionAllowlistForceEnforced(boolean z) {
        this.isSignaturePermissionAllowlistForceEnforced = z;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public java.lang.String getSubjectScheme() {
        return "uid";
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public java.lang.String getObjectScheme() {
        return com.android.server.permission.access.PermissionUri.SCHEME;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onStateMutated(com.android.server.permission.access.GetStateScope $this$onStateMutated) {
        com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener> indexedListSet = this.onPermissionFlagsChangedListeners;
        int size = indexedListSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener it = indexedListSet.elementAt(index$iv);
            it.onStateMutated();
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onUserAdded(com.android.server.permission.access.MutateStateScope $this$onUserAdded, int userId) {
        java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState>> it = $this$onUserAdded.getNewState().getExternalState().getPackageStates().entrySet().iterator();
        while (it.hasNext()) {
            com.android.server.pm.pkg.PackageState packageState = it.next().getValue();
            if (!packageState.isApex()) {
                evaluateAllPermissionStatesForPackageAndUser($this$onUserAdded, packageState, userId, null);
            }
        }
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>> appIdPackageNames = $this$onUserAdded.getNewState().getExternalState().getAppIdPackageNames();
        int size = appIdPackageNames.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int appId = appIdPackageNames.keyAt(index$iv);
            inheritImplicitPermissionStates($this$onUserAdded, appId, userId);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onAppIdRemoved(com.android.server.permission.access.MutateStateScope $this$onAppIdRemoved, int appId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$onAppIdRemoved.getNewState().getUserStates();
        int size = userStates.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            int userStateIndex = index$iv;
            if (userState.getAppIdPermissionFlags().contains(appId)) {
                com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.minusAssign(com.android.server.permission.access.MutableAccessState.mutateUserStateAt$default($this$onAppIdRemoved.getNewState(), userStateIndex, 0, 2, null).mutateAppIdPermissionFlags(), appId);
            }
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onStorageVolumeMounted(com.android.server.permission.access.MutateStateScope $this$onStorageVolumeMounted, java.lang.String volumeUuid, java.util.List<java.lang.String> list, boolean isSystemUpdated) {
        com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> mutableIndexedSet = new com.android.server.permission.access.immutable.MutableIndexedSet<>(null, 1, null);
        int size = list.size();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String packageName = list.get(index$iv);
            com.android.server.pm.pkg.PackageState packageState = $this$onStorageVolumeMounted.getNewState().getExternalState().getPackageStates().get(packageName);
            if (packageState != null) {
                adoptPermissions($this$onStorageVolumeMounted, packageState, mutableIndexedSet);
                addPermissionGroups($this$onStorageVolumeMounted, packageState);
                addPermissions($this$onStorageVolumeMounted, packageState, mutableIndexedSet);
                trimPermissions($this$onStorageVolumeMounted, packageState.getPackageName(), mutableIndexedSet);
                trimPermissionStates($this$onStorageVolumeMounted, packageState.getAppId());
                revokePermissionsOnPackageUpdate($this$onStorageVolumeMounted, packageState.getAppId());
            }
        }
        com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> $this$forEachIndexed$iv = mutableIndexedSet;
        int size2 = $this$forEachIndexed$iv.getSize();
        for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
            java.lang.String permissionName = $this$forEachIndexed$iv.elementAt(index$iv2);
            evaluatePermissionStateForAllPackages($this$onStorageVolumeMounted, permissionName, null);
        }
        int size3 = list.size();
        for (int index$iv3 = 0; index$iv3 < size3; index$iv3++) {
            java.lang.String packageName2 = list.get(index$iv3);
            com.android.server.pm.pkg.PackageState packageState2 = $this$onStorageVolumeMounted.getNewState().getExternalState().getPackageStates().get(packageName2);
            if (packageState2 != null) {
                com.android.server.pm.pkg.PackageState installedPackageState = isSystemUpdated ? packageState2 : null;
                evaluateAllPermissionStatesForPackage($this$onStorageVolumeMounted, packageState2, installedPackageState);
            }
        }
        int index$iv4 = 0;
        int size4 = list.size();
        while (index$iv4 < size4) {
            java.lang.String packageName3 = list.get(index$iv4);
            com.android.server.pm.pkg.PackageState packageState3 = $this$onStorageVolumeMounted.getNewState().getExternalState().getPackageStates().get(packageName3);
            if (packageState3 != null) {
                com.android.server.permission.access.immutable.IntSet $this$forEachIndexed$iv2 = $this$onStorageVolumeMounted.getNewState().getExternalState().getUserIds();
                int index$iv5 = 0;
                int size5 = $this$forEachIndexed$iv2.getSize();
                while (index$iv5 < size5) {
                    int userId = $this$forEachIndexed$iv2.elementAt(index$iv5);
                    inheritImplicitPermissionStates($this$onStorageVolumeMounted, packageState3.getAppId(), userId);
                    index$iv5++;
                    mutableIndexedSet = mutableIndexedSet;
                }
            }
            index$iv4++;
            mutableIndexedSet = mutableIndexedSet;
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageAdded(com.android.server.permission.access.MutateStateScope $this$onPackageAdded, com.android.server.pm.pkg.PackageState packageState) {
        com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> mutableIndexedSet = new com.android.server.permission.access.immutable.MutableIndexedSet<>(null, 1, null);
        adoptPermissions($this$onPackageAdded, packageState, mutableIndexedSet);
        addPermissionGroups($this$onPackageAdded, packageState);
        addPermissions($this$onPackageAdded, packageState, mutableIndexedSet);
        trimPermissions($this$onPackageAdded, packageState.getPackageName(), mutableIndexedSet);
        trimPermissionStates($this$onPackageAdded, packageState.getAppId());
        revokePermissionsOnPackageUpdate($this$onPackageAdded, packageState.getAppId());
        com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> $this$forEachIndexed$iv = mutableIndexedSet;
        int size = $this$forEachIndexed$iv.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String permissionName = $this$forEachIndexed$iv.elementAt(index$iv);
            evaluatePermissionStateForAllPackages($this$onPackageAdded, permissionName, null);
        }
        evaluateAllPermissionStatesForPackage($this$onPackageAdded, packageState, packageState);
        com.android.server.permission.access.immutable.IntSet $this$forEachIndexed$iv2 = $this$onPackageAdded.getNewState().getExternalState().getUserIds();
        int size2 = $this$forEachIndexed$iv2.getSize();
        for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
            int userId = $this$forEachIndexed$iv2.elementAt(index$iv2);
            inheritImplicitPermissionStates($this$onPackageAdded, packageState.getAppId(), userId);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageRemoved(com.android.server.permission.access.MutateStateScope $this$onPackageRemoved, java.lang.String packageName, int appId) {
        if (!(!$this$onPackageRemoved.getNewState().getExternalState().getDisabledSystemPackageStates().containsKey(packageName))) {
            throw new java.lang.IllegalStateException(("Package " + packageName + " reported as removed before disabled system package is enabled").toString());
        }
        com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> mutableIndexedSet = new com.android.server.permission.access.immutable.MutableIndexedSet<>(null, 1, null);
        trimPermissions($this$onPackageRemoved, packageName, mutableIndexedSet);
        if ($this$onPackageRemoved.getNewState().getExternalState().getAppIdPackageNames().contains(appId)) {
            trimPermissionStates($this$onPackageRemoved, appId);
        }
        com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> $this$forEachIndexed$iv = mutableIndexedSet;
        int size = $this$forEachIndexed$iv.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String permissionName = $this$forEachIndexed$iv.elementAt(index$iv);
            evaluatePermissionStateForAllPackages($this$onPackageRemoved, permissionName, null);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageInstalled(com.android.server.permission.access.MutateStateScope $this$onPackageInstalled, com.android.server.pm.pkg.PackageState packageState, int userId) {
        clearRestrictedPermissionImplicitExemption($this$onPackageInstalled, packageState, userId);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00f2 A[LOOP:1: B:25:0x009a->B:41:0x00f2, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00f0 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void clearRestrictedPermissionImplicitExemption(com.android.server.permission.access.MutateStateScope r29, com.android.server.pm.pkg.PackageState r30, int r31) {
        /*
            Method dump skipped, instruction units count: 289
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.clearRestrictedPermissionImplicitExemption(com.android.server.permission.access.MutateStateScope, com.android.server.pm.pkg.PackageState, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x01ba A[LOOP:1: B:27:0x0155->B:45:0x01ba, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01b7 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updatePermissionExemptFlags(com.android.server.permission.access.MutateStateScope r32, int r33, int r34, com.android.server.permission.access.permission.Permission r35, int r36, int r37) {
        /*
            Method dump skipped, instruction units count: 494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.updatePermissionExemptFlags(com.android.server.permission.access.MutateStateScope, int, int, com.android.server.permission.access.permission.Permission, int, int):void");
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageUninstalled(com.android.server.permission.access.MutateStateScope $this$onPackageUninstalled, java.lang.String packageName, int appId, int userId) {
        resetRuntimePermissions($this$onPackageUninstalled, packageName, userId);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x010a A[LOOP:1: B:23:0x00ab->B:39:0x010a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0108 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resetRuntimePermissions(com.android.server.permission.access.MutateStateScope r30, java.lang.String r31, int r32) {
        /*
            Method dump skipped, instruction units count: 390
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.resetRuntimePermissions(com.android.server.permission.access.MutateStateScope, java.lang.String, int):void");
    }

    private final void adoptPermissions(com.android.server.permission.access.MutateStateScope $this$adoptPermissions, com.android.server.pm.pkg.PackageState packageState, com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> mutableIndexedSet) {
        com.android.server.pm.pkg.AndroidPackage androidPackage;
        java.util.List $this$forEachIndexed$iv;
        int $i$f$forEachIndexed;
        int i;
        java.util.List $this$forEachIndexed$iv2;
        int $i$f$forEachIndexed2;
        int i2;
        java.lang.String originalPackageName;
        com.android.server.pm.pkg.AndroidPackage androidPackage2 = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage2);
        java.util.List $this$forEachIndexed$iv3 = androidPackage2.getAdoptPermissions();
        int $i$f$forEachIndexed3 = 0;
        int index$iv = 0;
        int size = $this$forEachIndexed$iv3.size();
        while (index$iv < size) {
            java.lang.String originalPackageName2 = (java.lang.String) $this$forEachIndexed$iv3.get(index$iv);
            java.lang.String packageName = androidPackage2.getPackageName();
            if (!canAdoptPermissions($this$adoptPermissions, packageName, originalPackageName2)) {
                androidPackage = androidPackage2;
                $this$forEachIndexed$iv = $this$forEachIndexed$iv3;
                $i$f$forEachIndexed = $i$f$forEachIndexed3;
                i = size;
            } else {
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$adoptPermissions.getNewState().getSystemState().getPermissions();
                int index$iv2 = 0;
                int size2 = permissions.getSize();
                while (index$iv2 < size2) {
                    java.lang.String strKeyAt = permissions.keyAt(index$iv2);
                    com.android.server.permission.access.permission.Permission oldPermission = permissions.valueAt(index$iv2);
                    java.lang.String permissionName = strKeyAt;
                    int permissionIndex = index$iv2;
                    com.android.server.pm.pkg.AndroidPackage androidPackage3 = androidPackage2;
                    if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(oldPermission.getPermissionInfo().packageName, originalPackageName2)) {
                        $this$forEachIndexed$iv2 = $this$forEachIndexed$iv3;
                        $i$f$forEachIndexed2 = $i$f$forEachIndexed3;
                        i2 = size;
                        originalPackageName = originalPackageName2;
                    } else {
                        android.content.pm.PermissionInfo newPermissionInfo = new android.content.pm.PermissionInfo();
                        $this$forEachIndexed$iv2 = $this$forEachIndexed$iv3;
                        newPermissionInfo.name = oldPermission.getPermissionInfo().name;
                        newPermissionInfo.packageName = packageName;
                        newPermissionInfo.protectionLevel = oldPermission.getPermissionInfo().protectionLevel;
                        com.android.server.permission.access.permission.Permission newPermission = com.android.server.permission.access.permission.Permission.copy$default(oldPermission, newPermissionInfo, false, 0, 0, null, false, 52, null);
                        $i$f$forEachIndexed2 = $i$f$forEachIndexed3;
                        i2 = size;
                        originalPackageName = originalPackageName2;
                        com.android.server.permission.access.MutableAccessState.mutateSystemState$default($this$adoptPermissions.getNewState(), 0, 1, null).mutatePermissions().putAt(permissionIndex, newPermission);
                        mutableIndexedSet.add(permissionName);
                    }
                    index$iv2++;
                    $i$f$forEachIndexed3 = $i$f$forEachIndexed2;
                    size = i2;
                    originalPackageName2 = originalPackageName;
                    androidPackage2 = androidPackage3;
                    $this$forEachIndexed$iv3 = $this$forEachIndexed$iv2;
                }
                androidPackage = androidPackage2;
                $this$forEachIndexed$iv = $this$forEachIndexed$iv3;
                $i$f$forEachIndexed = $i$f$forEachIndexed3;
                i = size;
            }
            index$iv++;
            $i$f$forEachIndexed3 = $i$f$forEachIndexed;
            size = i;
            androidPackage2 = androidPackage;
            $this$forEachIndexed$iv3 = $this$forEachIndexed$iv;
        }
    }

    private final boolean canAdoptPermissions(com.android.server.permission.access.MutateStateScope $this$canAdoptPermissions, java.lang.String packageName, java.lang.String originalPackageName) {
        com.android.server.pm.pkg.PackageState originalPackageState = $this$canAdoptPermissions.getNewState().getExternalState().getPackageStates().get(originalPackageName);
        if (originalPackageState == null) {
            return false;
        }
        if (!originalPackageState.isSystem()) {
            android.util.Slog.w(LOG_TAG, "Unable to adopt permissions from " + originalPackageName + " to " + packageName + ": original package not in system partition");
            return false;
        }
        if (originalPackageState.getAndroidPackage() != null) {
            android.util.Slog.w(LOG_TAG, "Unable to adopt permissions from " + originalPackageName + " to " + packageName + ": original package still exists");
            return false;
        }
        return true;
    }

    private final void addPermissionGroups(com.android.server.permission.access.MutateStateScope $this$addPermissionGroups, com.android.server.pm.pkg.PackageState packageState) {
        boolean isInstantApp;
        android.util.SparseArray<? extends com.android.server.pm.pkg.PackageUserState> userStates = packageState.getUserStates();
        int index$iv$iv = 0;
        int size = userStates.size();
        while (true) {
            if (index$iv$iv < size) {
                userStates.keyAt(index$iv$iv);
                java.lang.Object value$iv = userStates.valueAt(index$iv$iv);
                com.android.server.pm.pkg.PackageUserState it = (com.android.server.pm.pkg.PackageUserState) value$iv;
                if (it.isInstantApp()) {
                    index$iv$iv++;
                } else {
                    isInstantApp = false;
                    break;
                }
            } else {
                isInstantApp = true;
                break;
            }
        }
        if (isInstantApp) {
            android.util.Slog.w(LOG_TAG, "Ignoring permission groups declared in package " + packageState.getPackageName() + ": instant apps cannot declare permission groups");
            return;
        }
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
        java.util.List $this$forEachIndexed$iv = androidPackage.getPermissionGroups();
        int size2 = $this$forEachIndexed$iv.size();
        for (int index$iv = 0; index$iv < size2; index$iv++) {
            com.android.internal.pm.pkg.component.ParsedPermissionGroup parsedPermissionGroup = (com.android.internal.pm.pkg.component.ParsedPermissionGroup) $this$forEachIndexed$iv.get(index$iv);
            android.content.pm.PermissionGroupInfo newPermissionGroup = com.android.server.pm.parsing.PackageInfoUtils.generatePermissionGroupInfo(parsedPermissionGroup, 128L);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(newPermissionGroup);
            java.lang.String permissionGroupName = newPermissionGroup.name;
            android.content.pm.PermissionGroupInfo oldPermissionGroup = $this$addPermissionGroups.getNewState().getSystemState().getPermissionGroups().get(permissionGroupName);
            if (oldPermissionGroup != null && !com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(newPermissionGroup.packageName, oldPermissionGroup.packageName)) {
                java.lang.String newPackageName = newPermissionGroup.packageName;
                java.lang.String oldPackageName = oldPermissionGroup.packageName;
                if (!packageState.isSystem()) {
                    android.util.Slog.w(LOG_TAG, "Ignoring permission group " + permissionGroupName + " declared in package " + newPackageName + ": already declared in another package " + oldPackageName);
                } else {
                    com.android.server.pm.pkg.PackageState packageState2 = $this$addPermissionGroups.getNewState().getExternalState().getPackageStates().get(oldPackageName);
                    if (packageState2 != null && packageState2.isSystem()) {
                        android.util.Slog.w(LOG_TAG, "Ignoring permission group " + permissionGroupName + " declared in system package " + newPackageName + ": already declared in another system package " + oldPackageName);
                    } else {
                        android.util.Slog.w(LOG_TAG, "Overriding permission group " + permissionGroupName + " with new declaration in system package " + newPackageName + ": originally declared in another package " + oldPackageName);
                        com.android.server.permission.access.MutableAccessState.mutateSystemState$default($this$addPermissionGroups.getNewState(), 0, 1, null).mutatePermissionGroups().put(permissionGroupName, newPermissionGroup);
                    }
                }
            } else {
                com.android.server.permission.access.MutableAccessState.mutateSystemState$default($this$addPermissionGroups.getNewState(), 0, 1, null).mutatePermissionGroups().put(permissionGroupName, newPermissionGroup);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:147:0x0583  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x058b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x02f3  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0307  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void addPermissions(com.android.server.permission.access.MutateStateScope r37, com.android.server.pm.pkg.PackageState r38, com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> r39) {
        /*
            Method dump skipped, instruction units count: 1438
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.addPermissions(com.android.server.permission.access.MutateStateScope, com.android.server.pm.pkg.PackageState, com.android.server.permission.access.immutable.MutableIndexedSet):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x00b0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x00fa A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x01bb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:130:0x020c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b2 A[LOOP:1: B:20:0x0087->B:30:0x00b2, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00fc A[LOOP:2: B:37:0x00d1->B:47:0x00fc, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01bd A[LOOP:4: B:63:0x0192->B:73:0x01bd, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x020e A[LOOP:5: B:83:0x01e3->B:93:0x020e, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void trimPermissions(com.android.server.permission.access.MutateStateScope r32, java.lang.String r33, com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String> r34) {
        /*
            Method dump skipped, instruction units count: 740
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.trimPermissions(com.android.server.permission.access.MutateStateScope, java.lang.String, com.android.server.permission.access.immutable.MutableIndexedSet):void");
    }

    private final com.android.server.permission.access.permission.Permission updatePermissionIfDynamic(com.android.server.permission.access.MutateStateScope $this$updatePermissionIfDynamic, com.android.server.permission.access.permission.Permission permission) {
        com.android.server.permission.access.permission.Permission permissionTree;
        if (!(permission.getType() == 2) || (permissionTree = findPermissionTree($this$updatePermissionIfDynamic, permission.getPermissionInfo().name)) == null) {
            return permission;
        }
        android.content.pm.PermissionInfo $this$updatePermissionIfDynamic_u24lambda_u2438 = new android.content.pm.PermissionInfo(permission.getPermissionInfo());
        $this$updatePermissionIfDynamic_u24lambda_u2438.packageName = permissionTree.getPermissionInfo().packageName;
        return com.android.server.permission.access.permission.Permission.copy$default(permission, $this$updatePermissionIfDynamic_u24lambda_u2438, true, 0, permissionTree.getAppId(), null, false, 52, null);
    }

    private final void trimPermissionStates(com.android.server.permission.access.MutateStateScope $this$trimPermissionStates, int appId) {
        int index$iv;
        com.android.server.permission.access.immutable.MutableIndexedSet requestedPermissions = new com.android.server.permission.access.immutable.MutableIndexedSet(null, 1, null);
        com.android.server.permission.access.AccessState state$iv = $this$trimPermissionStates.getNewState();
        com.android.server.permission.access.immutable.Immutable immutable = state$iv.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames$iv = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames$iv.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            java.lang.String packageName$iv = (java.lang.String) packageNames$iv.elementAt(index$iv$iv);
            com.android.server.pm.pkg.PackageState packageState = state$iv.getExternalState().getPackageStates().get(packageName$iv);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState$iv = packageState;
            if (packageState$iv.getAndroidPackage() != null) {
                com.android.server.pm.pkg.AndroidPackage androidPackage = packageState$iv.getAndroidPackage();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
                com.android.server.permission.access.immutable.IndexedSetExtensionsKt.plusAssign(requestedPermissions, (java.util.Collection) androidPackage.getRequestedPermissions());
            }
        }
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$trimPermissionStates.getNewState().getUserStates();
        int size2 = userStates.getSize();
        for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
            int userId = userStates.keyAt(index$iv2);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv2);
            com.android.server.permission.access.immutable.IndexedMap $this$lastIndex$iv$iv = (com.android.server.permission.access.immutable.IndexedMap) userState.getAppIdPermissionFlags().get(appId);
            if ($this$lastIndex$iv$iv != null) {
                int index$iv3 = $this$lastIndex$iv$iv.getSize() - 1;
                while (-1 < index$iv3) {
                    java.lang.Object objKeyAt = $this$lastIndex$iv$iv.keyAt(index$iv3);
                    ((java.lang.Number) $this$lastIndex$iv$iv.valueAt(index$iv3)).intValue();
                    java.lang.String permissionName = (java.lang.String) objKeyAt;
                    if (requestedPermissions.contains(permissionName)) {
                        index$iv = index$iv3;
                    } else {
                        index$iv = index$iv3;
                        setPermissionFlags($this$trimPermissionStates, appId, userId, permissionName, 0);
                    }
                    index$iv3 = index$iv - 1;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x02ac  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void revokePermissionsOnPackageUpdate(com.android.server.permission.access.MutateStateScope r31, int r32) {
        /*
            Method dump skipped, instruction units count: 1013
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.revokePermissionsOnPackageUpdate(com.android.server.permission.access.MutateStateScope, int):void");
    }

    private final void evaluatePermissionStateForAllPackages(com.android.server.permission.access.MutateStateScope $this$evaluatePermissionStateForAllPackages, java.lang.String permissionName, com.android.server.pm.pkg.PackageState installedPackageState) {
        int index$iv$iv$iv;
        int $i$f$forEachIndexed;
        int index$iv;
        com.android.server.permission.access.ExternalState externalState = $this$evaluatePermissionStateForAllPackages.getNewState().getExternalState();
        com.android.server.permission.access.immutable.IntSet $this$forEachIndexed$iv = externalState.getUserIds();
        int $i$f$forEachIndexed2 = 0;
        int index$iv2 = 0;
        int size = $this$forEachIndexed$iv.getSize();
        while (index$iv2 < size) {
            int userId = $this$forEachIndexed$iv.elementAt(index$iv2);
            com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedListSet<java.lang.String>, com.android.server.permission.access.immutable.MutableIndexedListSet<java.lang.String>> appIdPackageNames = externalState.getAppIdPackageNames();
            int size2 = appIdPackageNames.getSize();
            int index$iv3 = 0;
            while (index$iv3 < size2) {
                int appId = appIdPackageNames.keyAt(index$iv3);
                com.android.server.permission.access.AccessState state$iv = $this$evaluatePermissionStateForAllPackages.getNewState();
                com.android.server.permission.access.immutable.Immutable immutable = state$iv.getExternalState().getAppIdPackageNames().get(appId);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
                com.android.server.permission.access.immutable.IndexedListSet packageNames$iv = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
                com.android.server.permission.access.immutable.IndexedListSet $this$forEachIndexed$iv$iv$iv = packageNames$iv;
                com.android.server.permission.access.ExternalState externalState2 = externalState;
                int size3 = $this$forEachIndexed$iv$iv$iv.getSize();
                com.android.server.permission.access.immutable.IntSet $this$forEachIndexed$iv2 = $this$forEachIndexed$iv;
                int index$iv$iv$iv2 = 0;
                while (true) {
                    index$iv$iv$iv = 0;
                    if (index$iv$iv$iv2 >= size3) {
                        $i$f$forEachIndexed = $i$f$forEachIndexed2;
                        break;
                    }
                    int i = size3;
                    com.android.server.permission.access.immutable.IndexedListSet $this$forEachIndexed$iv$iv$iv2 = $this$forEachIndexed$iv$iv$iv;
                    java.lang.Object element$iv$iv = $this$forEachIndexed$iv$iv$iv2.elementAt(index$iv$iv$iv2);
                    java.lang.String packageName$iv = (java.lang.String) element$iv$iv;
                    $i$f$forEachIndexed = $i$f$forEachIndexed2;
                    com.android.server.pm.pkg.PackageState packageState = state$iv.getExternalState().getPackageStates().get(packageName$iv);
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
                    com.android.server.pm.pkg.PackageState packageState$iv = packageState;
                    if (packageState$iv.getAndroidPackage() != null) {
                        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState$iv.getAndroidPackage();
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
                        if (androidPackage.getRequestedPermissions().contains(permissionName)) {
                            index$iv$iv$iv = 1;
                        }
                    }
                    if (index$iv$iv$iv != 0) {
                        index$iv$iv$iv = 1;
                        break;
                    }
                    index$iv$iv$iv2++;
                    size3 = i;
                    $this$forEachIndexed$iv$iv$iv = $this$forEachIndexed$iv$iv$iv2;
                    $i$f$forEachIndexed2 = $i$f$forEachIndexed;
                }
                if (index$iv$iv$iv != 0) {
                    index$iv = index$iv3;
                    evaluatePermissionState($this$evaluatePermissionStateForAllPackages, appId, userId, permissionName, installedPackageState);
                } else {
                    index$iv = index$iv3;
                }
                index$iv3 = index$iv + 1;
                externalState = externalState2;
                $this$forEachIndexed$iv = $this$forEachIndexed$iv2;
                $i$f$forEachIndexed2 = $i$f$forEachIndexed;
            }
            index$iv2++;
            $i$f$forEachIndexed2 = $i$f$forEachIndexed2;
        }
    }

    private final void evaluateAllPermissionStatesForPackage(com.android.server.permission.access.MutateStateScope $this$evaluateAllPermissionStatesForPackage, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.PackageState installedPackageState) {
        com.android.server.permission.access.immutable.IntSet $this$forEachIndexed$iv = $this$evaluateAllPermissionStatesForPackage.getNewState().getExternalState().getUserIds();
        int size = $this$forEachIndexed$iv.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int userId = $this$forEachIndexed$iv.elementAt(index$iv);
            evaluateAllPermissionStatesForPackageAndUser($this$evaluateAllPermissionStatesForPackage, packageState, userId, installedPackageState);
        }
    }

    private final void evaluateAllPermissionStatesForPackageAndUser(com.android.server.permission.access.MutateStateScope $this$evaluateAllPermissionStatesForPackageAndUser, com.android.server.pm.pkg.PackageState packageState, int userId, com.android.server.pm.pkg.PackageState installedPackageState) {
        java.lang.Iterable requestedPermissions;
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
        if (androidPackage == null || (requestedPermissions = androidPackage.getRequestedPermissions()) == null) {
            return;
        }
        java.lang.Iterable $this$forEach$iv = requestedPermissions;
        for (java.lang.Object element$iv : $this$forEach$iv) {
            java.lang.String permissionName = (java.lang.String) element$iv;
            evaluatePermissionState($this$evaluateAllPermissionStatesForPackageAndUser, packageState.getAppId(), userId, permissionName, installedPackageState);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:232:0x05d4  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x061e  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00dc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void evaluatePermissionState(com.android.server.permission.access.MutateStateScope r46, int r47, int r48, java.lang.String r49, com.android.server.pm.pkg.PackageState r50) {
        /*
            Method dump skipped, instruction units count: 1720
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.evaluatePermissionState(com.android.server.permission.access.MutateStateScope, int, int, java.lang.String, com.android.server.pm.pkg.PackageState):void");
    }

    private final void inheritImplicitPermissionStates(com.android.server.permission.access.MutateStateScope $this$inheritImplicitPermissionStates, int appId, int userId) {
        com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet;
        int newFlags;
        com.android.server.permission.access.AccessState state$iv;
        com.android.server.permission.access.permission.AppIdPermissionPolicy appIdPermissionPolicy = this;
        com.android.server.permission.access.immutable.MutableIndexedSet implicitPermissions = new com.android.server.permission.access.immutable.MutableIndexedSet(null, 1, null);
        com.android.server.permission.access.AccessState state$iv2 = $this$inheritImplicitPermissionStates.getNewState();
        com.android.server.permission.access.immutable.Immutable immutable = state$iv2.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames$iv = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames$iv.getSize();
        int targetSdkVersion = 10000;
        int targetSdkVersion2 = 0;
        while (targetSdkVersion2 < size) {
            java.lang.String packageName$iv = (java.lang.String) packageNames$iv.elementAt(targetSdkVersion2);
            com.android.server.pm.pkg.PackageState packageState = state$iv2.getExternalState().getPackageStates().get(packageName$iv);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState$iv = packageState;
            if (packageState$iv.getAndroidPackage() != null) {
                com.android.server.pm.pkg.AndroidPackage androidPackage = packageState$iv.getAndroidPackage();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
                state$iv = state$iv2;
                int targetSdkVersion3 = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceAtMost(targetSdkVersion, androidPackage.getTargetSdkVersion());
                com.android.server.pm.pkg.AndroidPackage androidPackage2 = packageState$iv.getAndroidPackage();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage2);
                com.android.server.permission.access.immutable.IndexedSetExtensionsKt.plusAssign(implicitPermissions, (java.util.Collection) androidPackage2.getImplicitPermissions());
                targetSdkVersion = targetSdkVersion3;
            } else {
                state$iv = state$iv2;
            }
            targetSdkVersion2++;
            state$iv2 = state$iv;
        }
        com.android.server.permission.access.immutable.MutableIndexedSet $this$forEachIndexed$iv = implicitPermissions;
        int size2 = $this$forEachIndexed$iv.getSize();
        int index$iv = 0;
        while (index$iv < size2) {
            java.lang.String implicitPermissionName = (java.lang.String) $this$forEachIndexed$iv.elementAt(index$iv);
            com.android.server.permission.access.permission.Permission implicitPermission = $this$inheritImplicitPermissionStates.getNewState().getSystemState().getPermissions().get(implicitPermissionName);
            if (implicitPermission == null) {
                throw new java.lang.IllegalStateException(("Unknown implicit permission " + implicitPermissionName + " in split permissions").toString());
            }
            com.android.server.permission.access.immutable.IndexedSet $this$forEachIndexed$iv2 = $this$forEachIndexed$iv;
            if (implicitPermission.getPermissionInfo().getProtection() == 1) {
                boolean isNewPermission = appIdPermissionPolicy.getOldStatePermissionFlags($this$inheritImplicitPermissionStates, appId, userId, implicitPermissionName) == 0;
                if (isNewPermission && (indexedListSet = $this$inheritImplicitPermissionStates.getNewState().getExternalState().getImplicitToSourcePermissions().get(implicitPermissionName)) != null) {
                    int newFlags2 = appIdPermissionPolicy.getPermissionFlags($this$inheritImplicitPermissionStates, appId, userId, implicitPermissionName);
                    com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet2 = indexedListSet;
                    int $i$f$forEachIndexed = 0;
                    int size3 = indexedListSet2.getSize();
                    int index$iv2 = 0;
                    while (index$iv2 < size3) {
                        com.android.server.permission.access.immutable.IndexedListSet<java.lang.String> indexedListSet3 = indexedListSet2;
                        java.lang.String sourcePermissionName = indexedListSet2.elementAt(index$iv2);
                        int $i$f$forEachIndexed2 = $i$f$forEachIndexed;
                        com.android.server.permission.access.permission.Permission sourcePermission = $this$inheritImplicitPermissionStates.getNewState().getSystemState().getPermissions().get(sourcePermissionName);
                        if (sourcePermission == null) {
                            throw new java.lang.IllegalStateException(("Unknown source permission " + sourcePermissionName + " in split permissions").toString());
                        }
                        int sourceFlags = appIdPermissionPolicy.getPermissionFlags($this$inheritImplicitPermissionStates, appId, userId, sourcePermissionName);
                        int i = size3;
                        boolean isSourceGranted = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(sourceFlags);
                        boolean isNewGranted = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(newFlags2);
                        boolean isGrantingNewFromRevoke = isSourceGranted && !isNewGranted;
                        if (isSourceGranted == isNewGranted || isGrantingNewFromRevoke) {
                            if (isGrantingNewFromRevoke) {
                                newFlags2 = 0;
                            }
                            newFlags2 |= sourceFlags & com.android.server.permission.access.permission.PermissionFlags.MASK_RUNTIME;
                        }
                        index$iv2++;
                        appIdPermissionPolicy = this;
                        indexedListSet2 = indexedListSet3;
                        $i$f$forEachIndexed = $i$f$forEachIndexed2;
                        size3 = i;
                    }
                    if (targetSdkVersion >= 23 && NO_IMPLICIT_FLAG_PERMISSIONS.contains(implicitPermissionName)) {
                        newFlags = com.android.server.permission.access.util.IntExtensionsKt.andInv(newFlags2, 4096);
                    } else {
                        newFlags = newFlags2 | 4096;
                    }
                    setPermissionFlags($this$inheritImplicitPermissionStates, appId, userId, implicitPermissionName, newFlags);
                }
            }
            index$iv++;
            appIdPermissionPolicy = this;
            $this$forEachIndexed$iv = $this$forEachIndexed$iv2;
        }
    }

    private final boolean isCompatibilityPermissionForPackage(com.android.server.pm.pkg.AndroidPackage androidPackage, java.lang.String permissionName) {
        for (com.android.internal.pm.permission.CompatibilityPermissionInfo compatibilityPermission : com.android.internal.pm.permission.CompatibilityPermissionInfo.COMPAT_PERMS) {
            if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(compatibilityPermission.getName(), permissionName) && androidPackage.getTargetSdkVersion() < compatibilityPermission.getSdkVersion()) {
                android.util.Slog.i(LOG_TAG, "Auto-granting " + permissionName + " to old package " + androidPackage.getPackageName());
                return true;
            }
        }
        return false;
    }

    private final boolean shouldGrantPermissionBySignature(com.android.server.permission.access.MutateStateScope $this$shouldGrantPermissionBySignature, com.android.server.pm.pkg.PackageState packageState, com.android.server.permission.access.permission.Permission permission) {
        boolean isRequestedByFactoryApp;
        com.android.server.pm.pkg.AndroidPackage androidPackage;
        com.android.server.pm.pkg.AndroidPackage androidPackage2 = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage2);
        android.content.pm.SigningDetails packageSigningDetails = androidPackage2.getSigningDetails();
        com.android.server.pm.pkg.PackageState packageState2 = $this$shouldGrantPermissionBySignature.getNewState().getExternalState().getPackageStates().get(permission.getPermissionInfo().packageName);
        android.content.pm.SigningDetails sourceSigningDetails = (packageState2 == null || (androidPackage = packageState2.getAndroidPackage()) == null) ? null : androidPackage.getSigningDetails();
        com.android.server.pm.pkg.PackageState packageState3 = $this$shouldGrantPermissionBySignature.getNewState().getExternalState().getPackageStates().get("android");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState3);
        com.android.server.pm.pkg.AndroidPackage androidPackage3 = packageState3.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage3);
        android.content.pm.SigningDetails platformSigningDetails = androidPackage3.getSigningDetails();
        boolean z = sourceSigningDetails != null && sourceSigningDetails.hasCommonSignerWithCapability(packageSigningDetails, 4);
        boolean hasCommonSigner = z || packageSigningDetails.hasAncestorOrSelf(platformSigningDetails) || platformSigningDetails.checkCapability(packageSigningDetails, 4);
        if (!hasCommonSigner && this.permissionManagerServiceExt.hookShouldGrantPermissionBySignature(packageState.getAndroidPackage(), permission.getPermissionInfo().name, false, permission.getPermissionInfo().packageName)) {
            return true;
        }
        if (!com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.signaturePermissionAllowlistEnabled()) {
            return hasCommonSigner;
        }
        if (!hasCommonSigner) {
            return false;
        }
        if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(permission.getPermissionInfo().packageName, "android")) {
            if (!packageState.isSystem()) {
                isRequestedByFactoryApp = false;
            } else if (packageState.isUpdatedSystemApp()) {
                com.android.server.pm.pkg.PackageState packageState4 = $this$shouldGrantPermissionBySignature.getNewState().getExternalState().getDisabledSystemPackageStates().get(packageState.getPackageName());
                com.android.server.pm.pkg.AndroidPackage disabledSystemPackage = packageState4 != null ? packageState4.getAndroidPackage() : null;
                isRequestedByFactoryApp = disabledSystemPackage != null && disabledSystemPackage.getRequestedPermissions().contains(permission.getPermissionInfo().name);
            } else {
                isRequestedByFactoryApp = true;
            }
            if (!isRequestedByFactoryApp && !com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) getSignaturePermissionAllowlistState($this$shouldGrantPermissionBySignature, packageState, permission.getPermissionInfo().name), (java.lang.Object) true)) {
                android.util.Slog.w(LOG_TAG, "Signature permission " + permission.getPermissionInfo().name + " for package " + packageState.getPackageName() + " (" + packageState.getPath() + ") not in signature permission allowlist");
                if (!android.os.Build.isDebuggable() || this.isSignaturePermissionAllowlistForceEnforced) {
                    return false;
                }
            }
        }
        return true;
    }

    private final java.lang.Boolean getSignaturePermissionAllowlistState(com.android.server.permission.access.MutateStateScope $this$getSignaturePermissionAllowlistState, com.android.server.pm.pkg.PackageState packageState, java.lang.String permissionName) {
        com.android.server.pm.permission.PermissionAllowlist permissionAllowlist = $this$getSignaturePermissionAllowlistState.getNewState().getExternalState().getPermissionAllowlist();
        java.lang.String packageName = packageState.getPackageName();
        if (packageState.isVendor() || packageState.isOdm()) {
            return permissionAllowlist.getVendorSignatureAppAllowlistState(packageName, permissionName);
        }
        if (packageState.isProduct()) {
            return permissionAllowlist.getProductSignatureAppAllowlistState(packageName, permissionName);
        }
        if (packageState.isSystemExt()) {
            return permissionAllowlist.getSystemExtSignatureAppAllowlistState(packageName, permissionName);
        }
        java.lang.Boolean apexSignatureAppAllowlistState = permissionAllowlist.getApexSignatureAppAllowlistState(packageName, permissionName);
        if (apexSignatureAppAllowlistState != null) {
            return apexSignatureAppAllowlistState;
        }
        java.lang.Boolean productSignatureAppAllowlistState = permissionAllowlist.getProductSignatureAppAllowlistState(packageName, permissionName);
        if (productSignatureAppAllowlistState != null) {
            return productSignatureAppAllowlistState;
        }
        java.lang.Boolean vendorSignatureAppAllowlistState = permissionAllowlist.getVendorSignatureAppAllowlistState(packageName, permissionName);
        if (vendorSignatureAppAllowlistState != null) {
            return vendorSignatureAppAllowlistState;
        }
        java.lang.Boolean systemExtSignatureAppAllowlistState = permissionAllowlist.getSystemExtSignatureAppAllowlistState(packageName, permissionName);
        return systemExtSignatureAppAllowlistState == null ? permissionAllowlist.getSignatureAppAllowlistState(packageName, permissionName) : systemExtSignatureAppAllowlistState;
    }

    private final boolean checkPrivilegedPermissionAllowlist(com.android.server.permission.access.MutateStateScope $this$checkPrivilegedPermissionAllowlist, com.android.server.pm.pkg.PackageState packageState, com.android.server.permission.access.permission.Permission permission) {
        if (com.android.internal.os.RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_DISABLE || com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(packageState.getPackageName(), "android") || !packageState.isSystem() || !packageState.isPrivileged() || !$this$checkPrivilegedPermissionAllowlist.getNewState().getExternalState().getPrivilegedPermissionAllowlistPackages().contains(permission.getPermissionInfo().packageName)) {
            return true;
        }
        java.lang.Boolean allowlistState = getPrivilegedPermissionAllowlistState($this$checkPrivilegedPermissionAllowlist, packageState, permission.getPermissionInfo().name);
        if (allowlistState != null) {
            return allowlistState.booleanValue();
        }
        if (packageState.isUpdatedSystemApp()) {
            return true;
        }
        if (!$this$checkPrivilegedPermissionAllowlist.getNewState().getExternalState().isSystemReady() && !packageState.isApkInUpdatedApex()) {
            android.util.Slog.w(LOG_TAG, "Privileged permission " + permission.getPermissionInfo().name + " for package " + packageState.getPackageName() + " (" + packageState.getPath() + ") not in privileged permission allowlist");
        }
        return true ^ com.android.internal.os.RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_ENFORCE;
    }

    private final java.lang.Boolean getPrivilegedPermissionAllowlistState(com.android.server.permission.access.MutateStateScope $this$getPrivilegedPermissionAllowlistState, com.android.server.pm.pkg.PackageState packageState, java.lang.String permissionName) {
        com.android.server.pm.permission.PermissionAllowlist permissionAllowlist = $this$getPrivilegedPermissionAllowlistState.getNewState().getExternalState().getPermissionAllowlist();
        java.lang.String apexModuleName = packageState.getApexModuleName();
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
        if (apexModuleName != null) {
            java.lang.Boolean nonApexAllowlistState = permissionAllowlist.getPrivilegedAppAllowlistState(packageName, permissionName);
            if (nonApexAllowlistState != null) {
                android.util.Slog.w(LOG_TAG, "Package " + packageName + " is an APK in APEX but has permission allowlist on the system image, please bundle the allowlist in the " + apexModuleName + " APEX instead");
            }
            java.lang.Boolean apexAllowlistState = permissionAllowlist.getApexPrivilegedAppAllowlistState(apexModuleName, packageName, permissionName);
            return apexAllowlistState == null ? nonApexAllowlistState : apexAllowlistState;
        }
        return permissionAllowlist.getPrivilegedAppAllowlistState(packageName, permissionName);
    }

    private final boolean isSoftRestrictedPermissionExemptForPackage(com.android.server.pm.pkg.PackageState packageState, int appIdTargetSdkVersion, java.lang.String permissionName) {
        return (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(permissionName, "android.permission.READ_EXTERNAL_STORAGE") ? true : com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(permissionName, "android.permission.WRITE_EXTERNAL_STORAGE")) && appIdTargetSdkVersion >= 29;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006c A[LOOP:0: B:7:0x002a->B:17:0x006c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0069 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static /* synthetic */ boolean anyPackageInAppId$default(com.android.server.permission.access.permission.AppIdPermissionPolicy r18, com.android.server.permission.access.MutateStateScope r19, int r20, com.android.server.permission.access.AccessState r21, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 r22, int r23, java.lang.Object r24) {
        /*
            r0 = r23 & 2
            if (r0 == 0) goto Lb
            com.android.server.permission.access.MutableAccessState r0 = r19.getNewState()
            com.android.server.permission.access.AccessState r0 = (com.android.server.permission.access.AccessState) r0
            goto Ld
        Lb:
            r0 = r21
        Ld:
            r1 = 0
            com.android.server.permission.access.ExternalState r2 = r0.getExternalState()
            com.android.server.permission.access.immutable.IntReferenceMap r2 = r2.getAppIdPackageNames()
            r3 = r20
            com.android.server.permission.access.immutable.Immutable r2 = r2.get(r3)
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            com.android.server.permission.access.immutable.IndexedListSet r2 = (com.android.server.permission.access.immutable.IndexedListSet) r2
            r4 = r2
            r5 = 0
            r6 = r4
            r7 = 0
            r8 = 0
            int r9 = r6.getSize()
        L2a:
            if (r8 >= r9) goto L73
            java.lang.Object r11 = r6.elementAt(r8)
            r12 = r8
            r13 = 0
            r14 = r11
            java.lang.String r14 = (java.lang.String) r14
            r15 = 0
            com.android.server.permission.access.ExternalState r16 = r0.getExternalState()
            java.util.Map r10 = r16.getPackageStates()
            java.lang.Object r10 = r10.get(r14)
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r10)
            com.android.server.pm.pkg.PackageState r10 = (com.android.server.pm.pkg.PackageState) r10
            com.android.server.pm.pkg.AndroidPackage r16 = r10.getAndroidPackage()
            r17 = 1
            if (r16 == 0) goto L62
            r23 = r0
            r0 = r22
            java.lang.Object r16 = r0.invoke(r10)
            java.lang.Boolean r16 = (java.lang.Boolean) r16
            boolean r16 = r16.booleanValue()
            if (r16 == 0) goto L66
            r10 = r17
            goto L67
        L62:
            r23 = r0
            r0 = r22
        L66:
            r10 = 0
        L67:
            if (r10 == 0) goto L6c
            r10 = r17
            goto L79
        L6c:
            int r8 = r8 + 1
            r0 = r23
            goto L2a
        L73:
            r23 = r0
            r0 = r22
            r10 = 0
        L79:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.anyPackageInAppId$default(com.android.server.permission.access.permission.AppIdPermissionPolicy, com.android.server.permission.access.MutateStateScope, int, com.android.server.permission.access.AccessState, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1, int, java.lang.Object):boolean");
    }

    private final boolean anyPackageInAppId(com.android.server.permission.access.MutateStateScope $this$anyPackageInAppId, int appId, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.pm.pkg.PackageState, java.lang.Boolean> function1) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            java.lang.Object element$iv = packageNames.elementAt(index$iv$iv);
            java.lang.String packageName = (java.lang.String) element$iv;
            com.android.server.pm.pkg.PackageState packageState = state.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null && function1.invoke(packageState2).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    static /* synthetic */ void forEachPackageInAppId$default(com.android.server.permission.access.permission.AppIdPermissionPolicy $this, com.android.server.permission.access.MutateStateScope $receiver, int appId, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 action, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            com.android.server.permission.access.AccessState state2 = $receiver.getNewState();
            state = state2;
        }
        com.android.server.permission.access.immutable.Immutable immutable = state.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String packageName = (java.lang.String) packageNames.elementAt(index$iv);
            com.android.server.pm.pkg.PackageState packageState = state.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                action.invoke(packageState2);
            }
        }
    }

    private final void forEachPackageInAppId(com.android.server.permission.access.MutateStateScope $this$forEachPackageInAppId, int appId, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.pm.pkg.PackageState, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.access.immutable.Immutable immutable = state.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int size = packageNames.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            java.lang.String packageName = (java.lang.String) packageNames.elementAt(index$iv);
            com.android.server.pm.pkg.PackageState packageState = state.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                function1.invoke(packageState2);
            }
        }
    }

    static /* synthetic */ int reducePackageInAppId$default(com.android.server.permission.access.permission.AppIdPermissionPolicy $this, com.android.server.permission.access.MutateStateScope $receiver, int appId, int initialValue, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2 accumulator, int i, java.lang.Object obj) {
        com.android.server.permission.access.MutableAccessState state2;
        int $i$f$reducePackageInAppId;
        com.android.server.permission.access.immutable.IndexedListSet packageNames;
        int $i$f$reducePackageInAppId2;
        if ((i & 4) == 0) {
            state2 = state;
        } else {
            state2 = $receiver.getNewState();
        }
        int $i$f$reducePackageInAppId3 = 0;
        com.android.server.permission.access.immutable.Immutable immutable = state2.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames2 = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int value$iv = initialValue;
        int index$iv$iv = 0;
        int size = packageNames2.getSize();
        while (index$iv$iv < size) {
            java.lang.Object element$iv = packageNames2.elementAt(index$iv$iv);
            java.lang.String packageName = (java.lang.String) element$iv;
            int value = value$iv;
            com.android.server.permission.access.AccessState state3 = state2;
            com.android.server.pm.pkg.PackageState packageState = state2.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                $i$f$reducePackageInAppId = $i$f$reducePackageInAppId3;
                packageNames = packageNames2;
                $i$f$reducePackageInAppId2 = ((java.lang.Number) accumulator.invoke(java.lang.Integer.valueOf(value), packageState2)).intValue();
            } else {
                $i$f$reducePackageInAppId = $i$f$reducePackageInAppId3;
                packageNames = packageNames2;
                $i$f$reducePackageInAppId2 = value;
            }
            value$iv = $i$f$reducePackageInAppId2;
            index$iv$iv++;
            state2 = state3;
            $i$f$reducePackageInAppId3 = $i$f$reducePackageInAppId;
            packageNames2 = packageNames;
        }
        return value$iv;
    }

    private final int reducePackageInAppId(com.android.server.permission.access.MutateStateScope $this$reducePackageInAppId, int appId, int initialValue, com.android.server.permission.access.AccessState state, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super com.android.server.pm.pkg.PackageState, java.lang.Integer> function2) {
        com.android.server.permission.access.immutable.IndexedListSet packageNames;
        int iIntValue;
        int $i$f$reducePackageInAppId = 0;
        com.android.server.permission.access.immutable.Immutable immutable = state.getExternalState().getAppIdPackageNames().get(appId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        com.android.server.permission.access.immutable.IndexedListSet packageNames2 = (com.android.server.permission.access.immutable.IndexedListSet) immutable;
        int value$iv = initialValue;
        int index$iv$iv = 0;
        int size = packageNames2.getSize();
        while (index$iv$iv < size) {
            java.lang.Object element$iv = packageNames2.elementAt(index$iv$iv);
            java.lang.String packageName = (java.lang.String) element$iv;
            int value = value$iv;
            int $i$f$reducePackageInAppId2 = $i$f$reducePackageInAppId;
            com.android.server.pm.pkg.PackageState packageState = state.getExternalState().getPackageStates().get(packageName);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
            com.android.server.pm.pkg.PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                packageNames = packageNames2;
                iIntValue = function2.invoke(java.lang.Integer.valueOf(value), packageState2).intValue();
            } else {
                packageNames = packageNames2;
                iIntValue = value;
            }
            value$iv = iIntValue;
            index$iv$iv++;
            packageNames2 = packageNames;
            $i$f$reducePackageInAppId = $i$f$reducePackageInAppId2;
        }
        return value$iv;
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00e3, code lost:
    
        if (com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(r3, r2) != false) goto L36;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final boolean shouldGrantPermissionByProtectionFlags(com.android.server.permission.access.MutateStateScope r12, com.android.server.pm.pkg.PackageState r13, com.android.server.permission.access.permission.Permission r14) {
        /*
            Method dump skipped, instruction units count: 632
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.AppIdPermissionPolicy.shouldGrantPermissionByProtectionFlags(com.android.server.permission.access.MutateStateScope, com.android.server.pm.pkg.PackageState, com.android.server.permission.access.permission.Permission):boolean");
    }

    private final boolean shouldGrantPrivilegedOrOemPermission(com.android.server.permission.access.MutateStateScope $this$shouldGrantPrivilegedOrOemPermission, com.android.server.pm.pkg.PackageState packageState, com.android.server.permission.access.permission.Permission permission) {
        java.lang.String permissionName = permission.getPermissionInfo().name;
        java.lang.String packageName = packageState.getPackageName();
        if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 16)) {
            if (packageState.isPrivileged()) {
                if ((packageState.isVendor() || packageState.isOdm()) && !com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 32768)) {
                    android.util.Slog.w(LOG_TAG, "Permission " + permissionName + " cannot be granted to privileged vendor (or odm) app " + packageName + " because it isn't a vendorPrivileged permission");
                    return false;
                }
                return true;
            }
        } else if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 16384) && packageState.isOem()) {
            java.lang.Boolean allowlistState = $this$shouldGrantPrivilegedOrOemPermission.getNewState().getExternalState().getPermissionAllowlist().getOemAppAllowlistState(packageName, permissionName);
            if (allowlistState == null) {
                throw new java.lang.IllegalStateException(("OEM permission " + permissionName + " requested by package " + packageName + " must be explicitly declared granted or not").toString());
            }
            return allowlistState.booleanValue();
        }
        return false;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onSystemReady(com.android.server.permission.access.MutateStateScope $this$onSystemReady) {
        if (!this.privilegedPermissionAllowlistViolations.isEmpty()) {
            throw new java.lang.IllegalStateException("Signature|privileged permissions not in privileged permission allowlist: " + this.privilegedPermissionAllowlistViolations);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void parseSystemState(com.android.modules.utils.BinaryXmlPullParser $this$parseSystemState, com.android.server.permission.access.MutableAccessState state) throws org.xmlpull.v1.XmlPullParserException {
        com.android.server.permission.access.permission.AppIdPermissionPersistence $this$parseSystemState_u24lambda_u2474 = this.persistence;
        $this$parseSystemState_u24lambda_u2474.parseSystemState($this$parseSystemState, state);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void serializeSystemState(com.android.modules.utils.BinaryXmlSerializer $this$serializeSystemState, com.android.server.permission.access.AccessState state) {
        com.android.server.permission.access.permission.AppIdPermissionPersistence $this$serializeSystemState_u24lambda_u2475 = this.persistence;
        $this$serializeSystemState_u24lambda_u2475.serializeSystemState($this$serializeSystemState, state);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) throws org.xmlpull.v1.XmlPullParserException {
        com.android.server.permission.access.permission.AppIdPermissionPersistence $this$parseUserState_u24lambda_u2476 = this.persistence;
        $this$parseUserState_u24lambda_u2476.parseUserState($this$parseUserState, state, userId);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        com.android.server.permission.access.permission.AppIdPermissionPersistence $this$serializeUserState_u24lambda_u2477 = this.persistence;
        $this$serializeUserState_u24lambda_u2477.serializeUserState($this$serializeUserState, state, userId);
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> getPermissionTrees(com.android.server.permission.access.GetStateScope $this$getPermissionTrees) {
        return $this$getPermissionTrees.getState().getSystemState().getPermissionTrees();
    }

    public final com.android.server.permission.access.permission.Permission findPermissionTree(com.android.server.permission.access.GetStateScope $this$findPermissionTree, java.lang.String permissionName) {
        com.android.server.permission.access.permission.Permission permission;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissionTrees = $this$findPermissionTree.getState().getSystemState().getPermissionTrees();
        int index$iv$iv = 0;
        int size = permissionTrees.getSize();
        while (true) {
            permission = null;
            if (index$iv$iv >= size) {
                break;
            }
            java.lang.Object key$iv = permissionTrees.keyAt(index$iv$iv);
            java.lang.Object value$iv = permissionTrees.valueAt(index$iv$iv);
            com.android.server.permission.access.permission.Permission permissionTree = (com.android.server.permission.access.permission.Permission) value$iv;
            java.lang.String permissionTreeName = (java.lang.String) key$iv;
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> indexedMap = permissionTrees;
            if (com.android.server.permission.jarjar.kotlin.text.StringsKt.startsWith$default(permissionName, permissionTreeName, false, 2, (java.lang.Object) null) && permissionName.length() > permissionTreeName.length() && permissionName.charAt(permissionTreeName.length()) == '.') {
                permission = permissionTree;
            }
            if (permission != null) {
                break;
            }
            index$iv$iv++;
            permissionTrees = indexedMap;
        }
        return permission;
    }

    public final void addPermissionTree(com.android.server.permission.access.MutateStateScope $this$addPermissionTree, com.android.server.permission.access.permission.Permission permission) {
        com.android.server.permission.access.MutableAccessState.mutateSystemState$default($this$addPermissionTree.getNewState(), 0, 1, null).mutatePermissionTrees().put(permission.getPermissionInfo().name, permission);
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo> getPermissionGroups(com.android.server.permission.access.GetStateScope $this$getPermissionGroups) {
        return $this$getPermissionGroups.getState().getSystemState().getPermissionGroups();
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> getPermissions(com.android.server.permission.access.GetStateScope $this$getPermissions) {
        return $this$getPermissions.getState().getSystemState().getPermissions();
    }

    public static /* synthetic */ void addPermission$default(com.android.server.permission.access.permission.AppIdPermissionPolicy appIdPermissionPolicy, com.android.server.permission.access.MutateStateScope mutateStateScope, com.android.server.permission.access.permission.Permission permission, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        appIdPermissionPolicy.addPermission(mutateStateScope, permission, z);
    }

    public final void addPermission(com.android.server.permission.access.MutateStateScope $this$addPermission, com.android.server.permission.access.permission.Permission permission, boolean isSynchronousWrite) {
        int writeMode = isSynchronousWrite ? 2 : 1;
        $this$addPermission.getNewState().mutateSystemState(writeMode).mutatePermissions().put(permission.getPermissionInfo().name, permission);
    }

    public final void removePermission(com.android.server.permission.access.MutateStateScope $this$removePermission, com.android.server.permission.access.permission.Permission permission) {
        com.android.server.permission.access.MutableAccessState.mutateSystemState$default($this$removePermission.getNewState(), 0, 1, null).mutatePermissions().remove(permission.getPermissionInfo().name);
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> getUidPermissionFlags(com.android.server.permission.access.GetStateScope $this$getUidPermissionFlags, int appId, int userId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> appIdPermissionFlags;
        com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) $this$getUidPermissionFlags.getState().getUserStates().get(userId);
        if (userState == null || (appIdPermissionFlags = userState.getAppIdPermissionFlags()) == null) {
            return null;
        }
        return (com.android.server.permission.access.immutable.IndexedMap) appIdPermissionFlags.get(appId);
    }

    public final int getPermissionFlags(com.android.server.permission.access.GetStateScope $this$getPermissionFlags, int appId, int userId, java.lang.String permissionName) {
        return getPermissionFlags($this$getPermissionFlags.getState(), appId, userId, permissionName);
    }

    private final int getOldStatePermissionFlags(com.android.server.permission.access.MutateStateScope $this$getOldStatePermissionFlags, int appId, int userId, java.lang.String permissionName) {
        return getPermissionFlags($this$getOldStatePermissionFlags.getOldState(), appId, userId, permissionName);
    }

    private final int getPermissionFlags(com.android.server.permission.access.AccessState state, int appId, int userId, java.lang.String permissionName) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> appIdPermissionFlags;
        com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) state.getUserStates().get(userId);
        return ((java.lang.Number) com.android.server.permission.access.immutable.IndexedMapExtensionsKt.getWithDefault((userState == null || (appIdPermissionFlags = userState.getAppIdPermissionFlags()) == null) ? null : (com.android.server.permission.access.immutable.IndexedMap) appIdPermissionFlags.get(appId), permissionName, 0)).intValue();
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> getAllPermissionFlags(com.android.server.permission.access.GetStateScope $this$getAllPermissionFlags, int appId, int userId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> appIdPermissionFlags;
        com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) $this$getAllPermissionFlags.getState().getUserStates().get(userId);
        if (userState == null || (appIdPermissionFlags = userState.getAppIdPermissionFlags()) == null) {
            return null;
        }
        return (com.android.server.permission.access.immutable.IndexedMap) appIdPermissionFlags.get(appId);
    }

    public final boolean setPermissionFlags(com.android.server.permission.access.MutateStateScope $this$setPermissionFlags, int appId, int userId, java.lang.String permissionName, int flags) {
        return updatePermissionFlags($this$setPermissionFlags, appId, userId, permissionName, -1, flags);
    }

    public final boolean updatePermissionFlags(com.android.server.permission.access.MutateStateScope $this$updatePermissionFlags, int appId, int userId, java.lang.String permissionName, int flagMask, int flagValues) {
        if (!$this$updatePermissionFlags.getNewState().getUserStates().contains(userId)) {
            android.util.Slog.e(LOG_TAG, "Unable to update permission flags for missing user " + userId);
            return false;
        }
        com.android.server.permission.access.immutable.Immutable immutable = $this$updatePermissionFlags.getNewState().getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        int oldFlags = ((java.lang.Number) com.android.server.permission.access.immutable.IndexedMapExtensionsKt.getWithDefault((com.android.server.permission.access.immutable.IndexedMap) ((com.android.server.permission.access.UserState) immutable).getAppIdPermissionFlags().get(appId), permissionName, 0)).intValue();
        int newFlags = com.android.server.permission.access.util.IntExtensionsKt.andInv(oldFlags, flagMask) | (flagValues & flagMask);
        if (oldFlags == newFlags) {
            return false;
        }
        com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$updatePermissionFlags.getNewState(), userId, 0, 2, null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default);
        com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIntReferenceMapMutateAppIdPermissionFlags = mutableUserStateMutateUserState$default.mutateAppIdPermissionFlags();
        com.android.server.permission.access.immutable.Immutable immutableMutate = mutableIntReferenceMapMutateAppIdPermissionFlags.mutate(appId);
        if (immutableMutate == null) {
            com.android.server.permission.access.immutable.Immutable mutableIndexedMap = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
            com.android.server.permission.access.immutable.Immutable it$iv = mutableIndexedMap;
            mutableIntReferenceMapMutateAppIdPermissionFlags.put(appId, it$iv);
            immutableMutate = mutableIndexedMap;
        }
        com.android.server.permission.access.immutable.MutableIndexedMap permissionFlags = (com.android.server.permission.access.immutable.MutableIndexedMap) immutableMutate;
        com.android.server.permission.access.immutable.IndexedMapExtensionsKt.putWithDefault(permissionFlags, permissionName, java.lang.Integer.valueOf(newFlags), 0);
        if (permissionFlags.isEmpty()) {
            com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.minusAssign(mutableIntReferenceMapMutateAppIdPermissionFlags, appId);
        }
        com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener> indexedListSet = this.onPermissionFlagsChangedListeners;
        int size = indexedListSet.getSize();
        int index$iv = 0;
        while (index$iv < size) {
            com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener it = indexedListSet.elementAt(index$iv);
            it.onPermissionFlagsChanged(appId, userId, permissionName, oldFlags, newFlags);
            index$iv++;
            size = size;
            indexedListSet = indexedListSet;
        }
        return true;
    }

    public final void addOnPermissionFlagsChangedListener(com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener listener) {
        synchronized (this.onPermissionFlagsChangedListenersLock) {
            this.onPermissionFlagsChangedListeners = com.android.server.permission.access.immutable.IndexedListSetExtensionsKt.plus(this.onPermissionFlagsChangedListeners, listener);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void removeOnPermissionFlagsChangedListener(com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener listener) {
        synchronized (this.onPermissionFlagsChangedListenersLock) {
            this.onPermissionFlagsChangedListeners = com.android.server.permission.access.immutable.IndexedListSetExtensionsKt.minus(this.onPermissionFlagsChangedListeners, listener);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void migrateSystemState(com.android.server.permission.access.MutableAccessState state) {
        this.migration.migrateSystemState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(state);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void migrateUserState(com.android.server.permission.access.MutableAccessState state, int userId) {
        this.migration.migrateUserState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(state, userId);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void upgradePackageState(com.android.server.permission.access.MutateStateScope $this$upgradePackageState, com.android.server.pm.pkg.PackageState packageState, int userId, int version) {
        com.android.server.permission.access.permission.AppIdPermissionUpgrade $this$upgradePackageState_u24lambda_u2483 = this.upgrade;
        $this$upgradePackageState_u24lambda_u2483.upgradePackageState($this$upgradePackageState, packageState, userId, version);
    }

    /* JADX INFO: compiled from: AppIdPermissionPolicy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/android/server/permission/access/permission/AppIdPermissionPolicy$Companion;", "", "()V", "LOG_TAG", "", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "NEARBY_DEVICES_PERMISSIONS", "Lcom/android/server/permission/access/immutable/IndexedSet;", "NOTIFICATIONS_PERMISSIONS", "NO_IMPLICIT_FLAG_PERMISSIONS", "PLATFORM_PACKAGE_NAME", "STORAGE_AND_MEDIA_PERMISSIONS", "SYSTEM_OR_POLICY_FIXED_MASK", "", "USER_SETTABLE_MASK", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
