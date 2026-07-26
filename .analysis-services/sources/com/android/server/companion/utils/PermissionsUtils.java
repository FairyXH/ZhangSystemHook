package com.android.server.companion.utils;

/* JADX INFO: loaded from: classes.dex */
public final class PermissionsUtils {
    private static final java.util.Map<java.lang.String, java.lang.String> DEVICE_PROFILE_TO_PERMISSION;
    private static com.android.internal.app.IAppOpsService sAppOpsService;

    static {
        java.util.Map<java.lang.String, java.lang.String> map = new android.util.ArrayMap<>();
        map.put("android.app.role.COMPANION_DEVICE_WATCH", "android.permission.REQUEST_COMPANION_PROFILE_WATCH");
        map.put("android.app.role.COMPANION_DEVICE_APP_STREAMING", "android.permission.REQUEST_COMPANION_PROFILE_APP_STREAMING");
        map.put("android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION", "android.permission.REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION");
        map.put("android.app.role.COMPANION_DEVICE_COMPUTER", "android.permission.REQUEST_COMPANION_PROFILE_COMPUTER");
        map.put("android.app.role.COMPANION_DEVICE_GLASSES", "android.permission.REQUEST_COMPANION_PROFILE_GLASSES");
        map.put("android.app.role.COMPANION_DEVICE_NEARBY_DEVICE_STREAMING", "android.permission.REQUEST_COMPANION_PROFILE_NEARBY_DEVICE_STREAMING");
        DEVICE_PROFILE_TO_PERMISSION = java.util.Collections.unmodifiableMap(map);
        sAppOpsService = null;
    }

    public static void enforcePermissionForCreatingAssociation(android.content.Context context, android.companion.AssociationRequest request, int packageUid) {
        enforcePermissionForRequestingProfile(context, request.getDeviceProfile(), packageUid);
        if (request.isSelfManaged()) {
            enforcePermissionForRequestingSelfManaged(context, packageUid);
        }
    }

    public static void enforcePermissionForRequestingProfile(android.content.Context context, java.lang.String deviceProfile, int packageUid) {
        if (deviceProfile == null) {
            return;
        }
        if (!DEVICE_PROFILE_TO_PERMISSION.containsKey(deviceProfile)) {
            throw new java.lang.IllegalArgumentException("Unsupported device profile: " + deviceProfile);
        }
        java.lang.String permission = DEVICE_PROFILE_TO_PERMISSION.get(deviceProfile);
        if (context.checkPermission(permission, android.os.Binder.getCallingPid(), packageUid) != 0) {
            throw new java.lang.SecurityException("Application must hold " + permission + " to associate with a device with " + deviceProfile + " profile.");
        }
    }

    public static void enforcePermissionForRequestingSelfManaged(android.content.Context context, int packageUid) {
        if (context.checkPermission("android.permission.REQUEST_COMPANION_SELF_MANAGED", android.os.Binder.getCallingPid(), packageUid) != 0) {
            throw new java.lang.SecurityException("Application does not hold android.permission.REQUEST_COMPANION_SELF_MANAGED");
        }
    }

    public static boolean checkCallerCanInteractWithUserId(android.content.Context context, int userId) {
        return android.os.UserHandle.getCallingUserId() == userId || context.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") == 0;
    }

    public static void enforceCallerCanInteractWithUserId(android.content.Context context, int userId) {
        if (android.os.UserHandle.getCallingUserId() == userId) {
            return;
        }
        context.enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", null);
    }

    public static void enforceCallerIsSystemOrCanInteractWithUserId(android.content.Context context, int userId) {
        if (android.os.Binder.getCallingUid() == 1000) {
            return;
        }
        enforceCallerCanInteractWithUserId(context, userId);
    }

    public static void enforceCallerIsSystemOr(int userId, java.lang.String packageName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 1000) {
            return;
        }
        int callingUserId = android.os.UserHandle.getCallingUserId();
        if (android.os.UserHandle.getCallingUserId() != userId) {
            throw new java.lang.SecurityException("Calling UserId (" + callingUserId + ") does not match the expected UserId (" + userId + ")");
        }
        if (!checkPackage(callingUid, packageName)) {
            throw new java.lang.SecurityException(packageName + " doesn't belong to calling uid (" + callingUid + ")");
        }
    }

    public static void enforceCallerCanManageAssociationsForPackage(android.content.Context context, int userId, java.lang.String packageName, java.lang.String actionDescription) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 1000) {
            return;
        }
        boolean canInteractAcrossUsers = context.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS") == 0;
        boolean canManageCompanionDevices = context.checkCallingPermission("android.permission.MANAGE_COMPANION_DEVICES") == 0;
        if (android.os.UserHandle.getCallingUserId() == userId) {
            if (checkPackage(callingUid, packageName) || canManageCompanionDevices) {
                return;
            }
        } else if (canInteractAcrossUsers && canManageCompanionDevices) {
            return;
        }
        throw new java.lang.SecurityException("Caller (uid=" + android.os.Binder.getCallingUid() + ") does not have permissions to " + (actionDescription != null ? actionDescription : "manage associations") + " for u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName);
    }

    public static void enforceCallerCanObserveDevicePresenceByUuid(android.content.Context context, java.lang.String packageName, int userId) {
        if (!hasRequirePermissions(context, packageName, userId)) {
            throw new java.lang.SecurityException("Caller (uid=" + android.os.Binder.getCallingUid() + ") does not have permissions to request observing device presence base on the UUID");
        }
    }

    private static boolean checkPackage(int uid, java.lang.String packageName) {
        try {
            return getAppOpsService().checkPackage(uid, packageName) == 0;
        } catch (android.os.RemoteException e) {
            return true;
        }
    }

    private static com.android.internal.app.IAppOpsService getAppOpsService() {
        if (sAppOpsService == null) {
            synchronized (com.android.server.companion.utils.PermissionsUtils.class) {
                if (sAppOpsService == null) {
                    sAppOpsService = com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
                }
            }
        }
        return sAppOpsService;
    }

    private static boolean hasRequirePermissions(final android.content.Context context, final java.lang.String packageName, final int userId) {
        return context.checkCallingPermission("android.permission.REQUEST_OBSERVE_DEVICE_UUID_PRESENCE") == 0 && context.checkCallingPermission("android.permission.BLUETOOTH_SCAN") == 0 && context.checkCallingPermission("android.permission.BLUETOOTH_CONNECT") == 0 && java.lang.Boolean.TRUE.equals(android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.utils.PermissionsUtils$$ExternalSyntheticLambda0
            public final java.lang.Object getOrThrow() {
                return java.lang.Boolean.valueOf(com.android.server.companion.utils.RolesUtils.isRoleHolder(context, userId, packageName, "android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION"));
            }
        }));
    }

    private PermissionsUtils() {
    }
}
