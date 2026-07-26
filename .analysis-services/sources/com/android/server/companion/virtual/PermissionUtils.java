package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
class PermissionUtils {
    private static final java.lang.String LOG_TAG = "VDM.PermissionUtils";

    PermissionUtils() {
    }

    public static boolean validateCallingPackageName(android.content.Context context, java.lang.String callingPackage) {
        int callingUid = android.os.Binder.getCallingUid();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int packageUid = context.getPackageManager().getPackageUidAsUser(callingPackage, android.os.UserHandle.getUserId(callingUid));
            if (packageUid != callingUid) {
                android.util.Slog.e(LOG_TAG, "validatePackageName: App with package name " + callingPackage + " is UID " + packageUid + " but caller is " + callingUid);
                return false;
            }
            android.os.Binder.restoreCallingIdentity(token);
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(LOG_TAG, "validatePackageName: App with package name " + callingPackage + " does not exist");
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
        android.os.Binder.restoreCallingIdentity(token);
    }
}
