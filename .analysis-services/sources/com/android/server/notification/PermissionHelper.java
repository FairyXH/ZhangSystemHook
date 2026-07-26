package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class PermissionHelper {
    private static final java.lang.String NOTIFICATION_PERMISSION = "android.permission.POST_NOTIFICATIONS";
    private static final java.lang.String TAG = "NotificationService--PermissionHelper";
    private final android.content.Context mContext;
    private final android.content.pm.IPackageManager mPackageManager;
    private com.android.server.notification.IPermissionHelperWrapper mPerHWrapper = new com.android.server.notification.PermissionHelper.PermissionHelperWrapper();
    private com.android.server.notification.IPermissionHelperExt mPerHelperExt = (com.android.server.notification.IPermissionHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IPermissionHelperExt.class).base(this).create();
    private final android.permission.IPermissionManager mPermManager;

    public PermissionHelper(android.content.Context context, android.content.pm.IPackageManager packageManager, android.permission.IPermissionManager permManager) {
        this.mContext = context;
        this.mPackageManager = packageManager;
        this.mPermManager = permManager;
    }

    public boolean hasPermission(int uid) {
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            return this.mContext.checkPermission(NOTIFICATION_PERMISSION, -1, uid) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public boolean hasRequestedPermission(java.lang.String permission, java.lang.String pkg, int userId) {
        android.content.pm.PackageInfo pi;
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            try {
                pi = this.mPackageManager.getPackageInfo(pkg, 4096L, userId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.d(TAG, "Could not reach system server", e);
            }
            if (pi != null && pi.requestedPermissions != null) {
                for (java.lang.String perm : pi.requestedPermissions) {
                    if (permission.equals(perm)) {
                        android.os.Binder.restoreCallingIdentity(callingId);
                        return true;
                    }
                }
                return false;
            }
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> getAppsRequestingPermission(int userId) {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> requested = new java.util.HashSet<>();
        java.util.List<android.content.pm.PackageInfo> pkgs = getInstalledPackages(userId);
        for (android.content.pm.PackageInfo pi : pkgs) {
            if (pi.requestedPermissions != null) {
                java.lang.String[] strArr = pi.requestedPermissions;
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        java.lang.String perm = strArr[i];
                        if (!NOTIFICATION_PERMISSION.equals(perm)) {
                            i++;
                        } else {
                            requested.add(new android.util.Pair<>(java.lang.Integer.valueOf(pi.applicationInfo.uid), pi.packageName));
                            break;
                        }
                    }
                }
            }
        }
        return requested;
    }

    private java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int userId) {
        android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> parceledList = null;
        try {
            parceledList = this.mPackageManager.getInstalledPackages(4096L, userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.d(TAG, "Could not reach system server", e);
        }
        if (parceledList == null) {
            return java.util.Collections.emptyList();
        }
        return parceledList.getList();
    }

    java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> getAppsGrantedPermission(int userId) {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> granted = new java.util.HashSet<>();
        android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> parceledList = null;
        try {
            parceledList = this.mPackageManager.getPackagesHoldingPermissions(new java.lang.String[]{NOTIFICATION_PERMISSION}, 0L, userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not reach system server", e);
        }
        if (parceledList == null) {
            return granted;
        }
        for (android.content.pm.PackageInfo pi : parceledList.getList()) {
            granted.add(new android.util.Pair<>(java.lang.Integer.valueOf(pi.applicationInfo.uid), pi.packageName));
        }
        return granted;
    }

    public android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> getNotificationPermissionValues(int userId) {
        android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> notifPermissions = new android.util.ArrayMap<>();
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> allRequestingUids = getAppsRequestingPermission(userId);
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> allApprovedUids = getAppsGrantedPermission(userId);
        for (android.util.Pair<java.lang.Integer, java.lang.String> pair : allRequestingUids) {
            notifPermissions.put(pair, new android.util.Pair<>(java.lang.Boolean.valueOf(allApprovedUids.contains(pair)), java.lang.Boolean.valueOf(isPermissionUserSet((java.lang.String) pair.second, userId))));
        }
        return notifPermissions;
    }

    public void setNotificationPermission(java.lang.String packageName, int userId, boolean grant, boolean userSet) {
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            try {
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Could not reach system server", e);
            }
            if (packageRequestsNotificationPermission(packageName, userId) && !isPermissionFixed(packageName, userId) && (!isPermissionGrantedByDefaultOrRole(packageName, userId) || userSet)) {
                int uid = this.mPackageManager.getPackageUid(packageName, 0L, userId);
                boolean currentlyGranted = hasPermission(uid);
                if (grant && !currentlyGranted) {
                    this.mPermManager.grantRuntimePermission(packageName, NOTIFICATION_PERMISSION, "default:0", userId);
                } else if (!grant && currentlyGranted) {
                    this.mPermManager.revokeRuntimePermission(packageName, NOTIFICATION_PERMISSION, "default:0", userId, TAG);
                }
                if (userSet) {
                    this.mPermManager.updatePermissionFlags(packageName, NOTIFICATION_PERMISSION, 3, 1, true, "default:0", userId);
                } else {
                    this.mPermManager.updatePermissionFlags(packageName, NOTIFICATION_PERMISSION, 3, 0, true, "default:0", userId);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void setNotificationPermission(com.android.server.notification.PermissionHelper.PackagePermission pkgPerm) {
        if (pkgPerm != null && pkgPerm.packageName != null && !isPermissionFixed(pkgPerm.packageName, pkgPerm.userId)) {
            setNotificationPermission(pkgPerm.packageName, pkgPerm.userId, pkgPerm.granted, true);
        }
    }

    public boolean isPermissionFixed(java.lang.String packageName, int userId) {
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            int flags = this.mPermManager.getPermissionFlags(packageName, NOTIFICATION_PERMISSION, "default:0", userId);
            return ((flags & 16) == 0 && (flags & 4) == 0) ? false : true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not reach system server", e);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    boolean isPermissionUserSet(java.lang.String packageName, int userId) {
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            int flags = this.mPermManager.getPermissionFlags(packageName, NOTIFICATION_PERMISSION, "default:0", userId);
            return (flags & 3) != 0;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not reach system server", e);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    boolean isPermissionGrantedByDefaultOrRole(java.lang.String packageName, int userId) {
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            int flags = this.mPermManager.getPermissionFlags(packageName, NOTIFICATION_PERMISSION, "default:0", userId);
            return (32800 & flags) != 0;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not reach system server", e);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean packageRequestsNotificationPermission(java.lang.String packageName, int userId) {
        try {
            android.content.pm.PackageInfo pi = this.mPackageManager.getPackageInfo(packageName, 4096L, userId);
            if (pi != null) {
                java.lang.String[] permissions = pi.requestedPermissions;
                return com.android.internal.util.ArrayUtils.contains(permissions, NOTIFICATION_PERMISSION);
            }
            return false;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not reach system server", e);
            return false;
        }
    }

    public static class PackagePermission {
        public final boolean granted;
        public final java.lang.String packageName;
        public final int userId;
        public final boolean userModifiedSettings;

        public PackagePermission(java.lang.String pkg, int userId, boolean granted, boolean userSet) {
            this.packageName = pkg;
            this.userId = userId;
            this.granted = granted;
            this.userModifiedSettings = userSet;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.notification.PermissionHelper.PackagePermission that = (com.android.server.notification.PermissionHelper.PackagePermission) o;
            if (this.userId == that.userId && this.granted == that.granted && this.userModifiedSettings == that.userModifiedSettings && java.util.Objects.equals(this.packageName, that.packageName)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.packageName, java.lang.Integer.valueOf(this.userId), java.lang.Boolean.valueOf(this.granted), java.lang.Boolean.valueOf(this.userModifiedSettings));
        }

        public java.lang.String toString() {
            return "PackagePermission{packageName='" + this.packageName + "', userId=" + this.userId + ", granted=" + this.granted + ", userSet=" + this.userModifiedSettings + '}';
        }
    }

    public com.android.server.notification.IPermissionHelperWrapper getWrapper() {
        return this.mPerHWrapper;
    }

    private class PermissionHelperWrapper implements com.android.server.notification.IPermissionHelperWrapper {
        private PermissionHelperWrapper() {
        }

        @Override // com.android.server.notification.IPermissionHelperWrapper
        public com.android.server.notification.IPermissionHelperExt getPermissionHelperExt() {
            return com.android.server.notification.PermissionHelper.this.mPerHelperExt;
        }

        @Override // com.android.server.notification.IPermissionHelperWrapper
        public boolean canModifyNotificationPermissionForPackage(java.lang.String packageName, int uid) {
            int userId = android.os.UserHandle.getUserId(uid);
            boolean pkgRequests = com.android.server.notification.PermissionHelper.this.packageRequestsNotificationPermission(packageName, userId);
            boolean permissionFixed = com.android.server.notification.PermissionHelper.this.isPermissionFixed(packageName, userId);
            boolean grantedByDefault = com.android.server.notification.PermissionHelper.this.isPermissionGrantedByDefaultOrRole(packageName, userId);
            if (getPermissionHelperExt() != null && getPermissionHelperExt().isLoggable()) {
                android.util.Slog.d(com.android.server.notification.PermissionHelper.TAG, "preview of modifying notification permission for pkg: " + packageName + "; is Pkg requests Notify Permission: " + pkgRequests + "; is permission fixed for this pkg: " + permissionFixed + "; is permission granted by default: " + grantedByDefault);
            }
            return pkgRequests && !permissionFixed;
        }
    }
}
