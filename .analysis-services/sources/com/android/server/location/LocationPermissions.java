package com.android.server.location;

/* JADX INFO: loaded from: classes2.dex */
public final class LocationPermissions {
    public static final int PERMISSION_COARSE = 1;
    public static final int PERMISSION_FINE = 2;
    public static final int PERMISSION_NONE = 0;

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PermissionLevel {
    }

    public static java.lang.String asPermission(int permissionLevel) {
        switch (permissionLevel) {
            case 1:
                return "android.permission.ACCESS_COARSE_LOCATION";
            case 2:
                return "android.permission.ACCESS_FINE_LOCATION";
            default:
                throw new java.lang.IllegalArgumentException();
        }
    }

    public static int asAppOp(int permissionLevel) {
        switch (permissionLevel) {
            case 1:
                return 0;
            case 2:
                return 1;
            default:
                throw new java.lang.IllegalArgumentException();
        }
    }

    public static void enforceCallingOrSelfLocationPermission(android.content.Context context, int requiredPermissionLevel) {
        enforceLocationPermission(android.os.Binder.getCallingUid(), getPermissionLevel(context, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid()), requiredPermissionLevel);
    }

    public static void enforceLocationPermission(android.content.Context context, int uid, int pid, int requiredPermissionLevel) {
        enforceLocationPermission(uid, getPermissionLevel(context, uid, pid), requiredPermissionLevel);
    }

    public static void enforceLocationPermission(int uid, int permissionLevel, int requiredPermissionLevel) {
        if (checkLocationPermission(permissionLevel, requiredPermissionLevel)) {
            return;
        }
        com.android.server.location.interfaces.IOplusLBSMainClass oplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null);
        if (requiredPermissionLevel == 1) {
            if (oplusLbsClass != null) {
                oplusLbsClass.handleAppLackLocationPermission(uid, requiredPermissionLevel);
            }
            throw new java.lang.SecurityException("uid " + uid + " does not have android.permission.ACCESS_COARSE_LOCATION or android.permission.ACCESS_FINE_LOCATION.");
        }
        if (requiredPermissionLevel == 2) {
            if (oplusLbsClass != null) {
                oplusLbsClass.handleAppLackLocationPermission(uid, requiredPermissionLevel);
            }
            throw new java.lang.SecurityException("uid " + uid + " does not have android.permission.ACCESS_FINE_LOCATION.");
        }
    }

    public static void enforceCallingOrSelfBypassPermission(android.content.Context context) {
        enforceBypassPermission(context, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
    }

    public static void enforceBypassPermission(android.content.Context context, int uid, int pid) {
        if (context.checkPermission("android.permission.LOCATION_BYPASS", pid, uid) == 0) {
        } else {
            throw new java.lang.SecurityException("uid" + uid + " does not have android.permission.LOCATION_BYPASS.");
        }
    }

    public static boolean checkCallingOrSelfLocationPermission(android.content.Context context, int requiredPermissionLevel) {
        return checkLocationPermission(getCallingOrSelfPermissionLevel(context), requiredPermissionLevel);
    }

    public static boolean checkLocationPermission(android.content.Context context, int uid, int pid, int requiredPermissionLevel) {
        return checkLocationPermission(getPermissionLevel(context, uid, pid), requiredPermissionLevel);
    }

    public static boolean checkLocationPermission(int permissionLevel, int requiredPermissionLevel) {
        return permissionLevel >= requiredPermissionLevel;
    }

    public static int getCallingOrSelfPermissionLevel(android.content.Context context) {
        return getPermissionLevel(context, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
    }

    public static int getPermissionLevel(android.content.Context context, int uid, int pid) {
        if (context.checkPermission("android.permission.ACCESS_FINE_LOCATION", pid, uid) == 0) {
            return 2;
        }
        if (context.checkPermission("android.permission.ACCESS_COARSE_LOCATION", pid, uid) == 0) {
            return 1;
        }
        return 0;
    }

    private LocationPermissions() {
    }
}
