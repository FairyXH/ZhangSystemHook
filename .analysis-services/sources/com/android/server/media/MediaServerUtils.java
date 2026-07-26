package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class MediaServerUtils {
    MediaServerUtils() {
    }

    public static boolean isValidActivityComponentName(android.content.Context context, android.content.ComponentName componentName, java.lang.String action, android.os.UserHandle userHandle) {
        android.content.Intent intent = new android.content.Intent(action);
        intent.setComponent(componentName);
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivitiesAsUser(intent, 0, userHandle);
        return !resolveInfos.isEmpty();
    }

    public static void enforcePackageName(android.content.Context context, java.lang.String packageName, int uid) {
        if (uid == 0 || uid == 2000) {
            return;
        }
        if (android.text.TextUtils.isEmpty(packageName)) {
            throw new java.lang.IllegalArgumentException("packageName may not be empty");
        }
        android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        if (!packageManagerInternal.isSameApp(packageName, uid, android.os.UserHandle.getUserId(uid))) {
            java.lang.String[] uidPackages = context.getPackageManager().getPackagesForUid(uid);
            throw new java.lang.IllegalArgumentException("packageName does not belong to the calling uid; pkg=" + packageName + ", uid=" + uid + " (" + java.util.Arrays.toString(uidPackages) + ")");
        }
    }

    public static boolean checkDumpPermission(android.content.Context context, java.lang.String tag, java.io.PrintWriter pw) {
        if (context.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            pw.println("Permission Denial: can't dump " + tag + " from from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " due to missing android.permission.DUMP permission");
            return false;
        }
        return true;
    }
}
