package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
class BtPermissionUtils {
    private static final int FLAGS_SYSTEM_APP = 129;
    private static final java.lang.String TAG = com.android.server.bluetooth.BtPermissionUtils.class.getSimpleName();
    private final int mSystemUiUid;

    BtPermissionUtils(android.content.Context ctx) {
        int systemUiUid = -1;
        try {
            systemUiUid = ctx.createContextAsUser(android.os.UserHandle.SYSTEM, 0).getPackageManager().getPackageUid("com.android.systemui", android.content.pm.PackageManager.PackageInfoFlags.of(1048576L));
            com.android.server.bluetooth.Log.d(TAG, "Detected SystemUiUid: " + systemUiUid);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            com.android.server.bluetooth.Log.w(TAG, "Unable to resolve SystemUI's UID.");
        }
        this.mSystemUiUid = systemUiUid;
    }

    static boolean checkConnectPermissionForDataDelivery(android.content.Context ctx, android.permission.PermissionManager permissionManager, android.content.AttributionSource source, java.lang.String message) {
        android.content.AttributionSource currentSource = new android.content.AttributionSource.Builder(ctx.getAttributionSource()).setNext((android.content.AttributionSource) java.util.Objects.requireNonNull(source)).build();
        int result = permissionManager.checkPermissionForDataDeliveryFromDataSource("android.permission.BLUETOOTH_CONNECT", currentSource, message);
        if (result == 0) {
            return true;
        }
        java.lang.String msg = "Need android.permission.BLUETOOTH_CONNECT permission for " + source + ": " + message;
        if (result == 2) {
            throw new java.lang.SecurityException(msg);
        }
        com.android.server.bluetooth.Log.w(TAG, msg);
        return false;
    }

    java.lang.String callerCanToggle(android.content.Context ctx, android.content.AttributionSource source, android.os.UserManager userManager, android.app.AppOpsManager appOpsManager, android.permission.PermissionManager permissionManager, java.lang.String message, boolean requireForeground) {
        if (isBluetoothDisallowed(userManager)) {
            return "Bluetooth is not allowed";
        }
        if (!checkBluetoothPermissions(ctx, source, userManager, appOpsManager, permissionManager, message, requireForeground)) {
            return "Missing Bluetooth permission";
        }
        if (requireForeground && !checkCompatChangeRestriction(source, ctx)) {
            return "Caller does not match restriction criteria";
        }
        return "";
    }

    static void enforcePrivileged(android.content.Context ctx) {
        ctx.enforceCallingOrSelfPermission("android.permission.BLUETOOTH_PRIVILEGED", "Need BLUETOOTH_PRIVILEGED permission");
    }

    static int getCallingAppId() {
        return android.os.UserHandle.getAppId(android.os.Binder.getCallingUid());
    }

    static boolean isCallerSystem(int callingAppId) {
        return callingAppId == 1000;
    }

    static boolean isCallerNfc(int callingAppId) {
        return callingAppId == 1027;
    }

    private static boolean isCallerShell(int callingAppId) {
        return callingAppId == 2000;
    }

    private static boolean isCallerRoot(int callingAppId) {
        return callingAppId == 0;
    }

    private boolean isCallerSystemUi(int callingAppId) {
        return callingAppId == this.mSystemUiUid;
    }

    private static boolean isPrivileged(android.content.Context ctx, int pid, int uid) {
        return ctx.checkPermission("android.permission.BLUETOOTH_PRIVILEGED", pid, uid) == 0 || ctx.getPackageManager().checkSignatures(uid, 1000) == 0;
    }

    private static boolean isProfileOwner(android.content.Context ctx, int uid, java.lang.String packageName) {
        try {
            android.content.Context userContext = ctx.createPackageContextAsUser(ctx.getPackageName(), 0, android.os.UserHandle.getUserHandleForUid(uid));
            if (userContext == null) {
                com.android.server.bluetooth.Log.e(TAG, "Unable to retrieve user context for " + uid);
                return false;
            }
            android.app.admin.DevicePolicyManager devicePolicyManager = (android.app.admin.DevicePolicyManager) userContext.getSystemService(android.app.admin.DevicePolicyManager.class);
            if (devicePolicyManager == null) {
                com.android.server.bluetooth.Log.w(TAG, "isProfileOwner: Error retrieving DevicePolicyManager service");
                return false;
            }
            return devicePolicyManager.isProfileOwnerApp(packageName);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            com.android.server.bluetooth.Log.e(TAG, "Unknown package name");
            return false;
        }
    }

    private static boolean isDeviceOwner(android.content.Context ctx, int uid, java.lang.String packageName) {
        if (packageName == null) {
            com.android.server.bluetooth.Log.e(TAG, "isDeviceOwner: packageName is null, returning false");
            return false;
        }
        android.app.admin.DevicePolicyManager devicePolicyManager = (android.app.admin.DevicePolicyManager) ctx.getSystemService(android.app.admin.DevicePolicyManager.class);
        if (devicePolicyManager == null) {
            com.android.server.bluetooth.Log.w(TAG, "isDeviceOwner: Error retrieving DevicePolicyManager service");
            return false;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.os.UserHandle deviceOwnerUser = devicePolicyManager.getDeviceOwnerUser();
            android.content.ComponentName deviceOwnerComponent = devicePolicyManager.getDeviceOwnerComponentOnAnyUser();
            return (deviceOwnerUser == null || deviceOwnerComponent == null || deviceOwnerComponent.getPackageName() == null || !deviceOwnerUser.equals(android.os.UserHandle.getUserHandleForUid(uid)) || !deviceOwnerComponent.getPackageName().equals(packageName)) ? false : true;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private static boolean isSystem(android.content.Context ctx, java.lang.String packageName, int uid) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.ApplicationInfo info = ctx.getPackageManager().getApplicationInfoAsUser(packageName, 0, android.os.UserHandle.getUserHandleForUid(uid));
            return (info.flags & 129) != 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private static boolean isBluetoothDisallowed(android.os.UserManager userManager) {
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            return userManager.hasUserRestrictionForUser("no_bluetooth", android.os.UserHandle.SYSTEM);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    private static void checkPackage(android.app.AppOpsManager appOpsManager, java.lang.String packageName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (packageName == null) {
            com.android.server.bluetooth.Log.w(TAG, "checkPackage(): called with null packageName from " + callingUid);
            return;
        }
        try {
            appOpsManager.checkPackage(callingUid, packageName);
        } catch (java.lang.SecurityException e) {
            com.android.server.bluetooth.Log.w(TAG, "checkPackage(): " + packageName + " does not belong to uid " + callingUid);
            throw new java.lang.SecurityException(e.getMessage());
        }
    }

    boolean checkIfCallerIsForegroundUser(android.os.UserManager userManager) {
        int callingUid = android.os.Binder.getCallingUid();
        android.os.UserHandle callingUser = android.os.UserHandle.getUserHandleForUid(callingUid);
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            android.os.UserHandle foregroundUser = android.os.UserHandle.of(android.app.ActivityManager.getCurrentUser());
            android.os.UserHandle parentUser = userManager.getProfileParent(callingUser);
            android.os.Binder.restoreCallingIdentity(callingIdentity);
            int callingAppId = android.os.UserHandle.getAppId(callingUid);
            boolean valid = java.util.Objects.equals(callingUser, foregroundUser) || java.util.Objects.equals(parentUser, foregroundUser) || isCallerNfc(callingAppId) || isCallerSystemUi(callingAppId) || isCallerShell(callingAppId);
            if (!valid) {
                com.android.server.bluetooth.Log.d(TAG, "checkIfCallerIsForegroundUser: REJECTED: callingUser=" + callingUser + " parentUser=" + parentUser + " foregroundUser=" + foregroundUser + " callingAppId=" + callingAppId);
            }
            return valid;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
            throw th;
        }
    }

    private boolean checkBluetoothPermissions(android.content.Context ctx, android.content.AttributionSource source, android.os.UserManager userManager, android.app.AppOpsManager appOpsManager, android.permission.PermissionManager permissionManager, java.lang.String message, boolean requireForeground) {
        int callingAppId = getCallingAppId();
        if (isCallerSystem(callingAppId) || isCallerShell(callingAppId) || isCallerRoot(callingAppId)) {
            return true;
        }
        checkPackage(appOpsManager, source.getPackageName());
        if (!requireForeground || checkIfCallerIsForegroundUser(userManager)) {
            return checkConnectPermissionForDataDelivery(ctx, permissionManager, source, message);
        }
        com.android.server.bluetooth.Log.w(TAG, "Not allowed for non-active and non system user");
        return false;
    }

    private static boolean checkCompatChangeRestriction(android.content.AttributionSource source, android.content.Context ctx) {
        java.lang.String packageName = source.getPackageName();
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        if (android.app.compat.CompatChanges.isChangeEnabled(218493289L, callingUid) && !isPrivileged(ctx, callingPid, callingUid) && !isSystem(ctx, packageName, callingUid) && !isDeviceOwner(ctx, callingUid, packageName) && !isProfileOwner(ctx, callingUid, packageName)) {
            com.android.server.bluetooth.Log.e(TAG, "Caller is not one of: privileged | system | deviceOwner | profileOwner");
            return false;
        }
        return true;
    }
}
