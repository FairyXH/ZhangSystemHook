package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class AppInstallerUtil {
    private static final java.lang.String LOG_TAG = "AppInstallerUtil";

    private static android.content.Intent resolveIntent(android.content.Context context, android.content.Intent i) {
        android.content.pm.ResolveInfo result = context.getPackageManager().resolveActivity(i, 0);
        if (result != null) {
            return new android.content.Intent(i.getAction()).setClassName(result.activityInfo.packageName, result.activityInfo.name);
        }
        return null;
    }

    public static java.lang.String getInstallerPackageName(android.content.Context context, java.lang.String packageName) {
        java.lang.String installerPackageName = null;
        try {
            installerPackageName = context.getPackageManager().getInstallerPackageName(packageName);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(LOG_TAG, "Exception while retrieving the package installer of " + packageName, e);
        }
        if (installerPackageName == null) {
            return null;
        }
        return installerPackageName;
    }

    public static android.content.Intent createIntent(android.content.Context context, java.lang.String installerPackageName, java.lang.String packageName) {
        android.content.Intent intent = new android.content.Intent("android.intent.action.SHOW_APP_INFO").setPackage(installerPackageName);
        android.content.Intent result = resolveIntent(context, intent);
        if (result != null) {
            result.putExtra("android.intent.extra.PACKAGE_NAME", packageName);
            result.addFlags(268435456);
            return result;
        }
        return null;
    }

    public static android.content.Intent createIntent(android.content.Context context, java.lang.String packageName) {
        java.lang.String installerPackageName = getInstallerPackageName(context, packageName);
        return createIntent(context, installerPackageName, packageName);
    }
}
