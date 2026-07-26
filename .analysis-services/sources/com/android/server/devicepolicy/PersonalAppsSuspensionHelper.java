package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public final class PersonalAppsSuspensionHelper {
    private static final java.lang.String LOG_TAG = "DevicePolicyManager";
    private static final int PACKAGE_QUERY_FLAGS = 786432;
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;

    public static com.android.server.devicepolicy.PersonalAppsSuspensionHelper forUser(android.content.Context context, int userId) {
        return new com.android.server.devicepolicy.PersonalAppsSuspensionHelper(context.createContextAsUser(android.os.UserHandle.of(userId), 0));
    }

    private PersonalAppsSuspensionHelper(android.content.Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
    }

    java.lang.String[] getPersonalAppsForSuspension() {
        java.util.List<android.content.pm.PackageInfo> installedPackageInfos = this.mPackageManager.getInstalledPackages(786432);
        java.util.Set<java.lang.String> result = new android.util.ArraySet<>();
        for (android.content.pm.PackageInfo packageInfo : installedPackageInfos) {
            android.content.pm.ApplicationInfo info = packageInfo.applicationInfo;
            if ((!info.isSystemApp() && !info.isUpdatedSystemApp()) || hasLauncherIntent(packageInfo.packageName)) {
                result.add(packageInfo.packageName);
            }
        }
        result.removeAll(getCriticalPackages());
        result.removeAll(getSystemLauncherPackages());
        result.removeAll(getAccessibilityServices());
        result.removeAll(getInputMethodPackages());
        result.remove(getDefaultSmsPackage());
        result.remove(getSettingsPackageName());
        java.lang.String[] unsuspendablePackages = this.mPackageManager.getUnsuspendablePackages((java.lang.String[]) result.toArray(new java.lang.String[0]));
        for (java.lang.String pkg : unsuspendablePackages) {
            result.remove(pkg);
        }
        return (java.lang.String[]) result.toArray(new java.lang.String[0]);
    }

    private java.util.List<java.lang.String> getSystemLauncherPackages() {
        android.content.pm.ApplicationInfo applicationInfo;
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        java.util.List<android.content.pm.ResolveInfo> matchingActivities = this.mPackageManager.queryIntentActivities(intent, 786432);
        for (android.content.pm.ResolveInfo resolveInfo : matchingActivities) {
            if (resolveInfo.activityInfo == null || android.text.TextUtils.isEmpty(resolveInfo.activityInfo.packageName)) {
                com.android.server.utils.Slogf.wtf(LOG_TAG, "Could not find package name for launcher app %s", resolveInfo);
            } else {
                java.lang.String packageName = resolveInfo.activityInfo.packageName;
                try {
                    applicationInfo = this.mPackageManager.getApplicationInfo(packageName, 786432);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    com.android.server.utils.Slogf.e(LOG_TAG, "Could not find application info for launcher app: %s", packageName);
                }
                if (applicationInfo.isSystemApp() || applicationInfo.isUpdatedSystemApp()) {
                    result.add(packageName);
                }
            }
        }
        return result;
    }

    private java.util.List<java.lang.String> getAccessibilityServices() {
        android.os.IBinder iBinder = android.os.ServiceManager.getService("accessibility");
        android.view.accessibility.IAccessibilityManager service = iBinder == null ? null : android.view.accessibility.IAccessibilityManager.Stub.asInterface(iBinder);
        android.view.accessibility.AccessibilityManager am = new android.view.accessibility.AccessibilityManager(this.mContext, service, this.mContext.getUserId());
        try {
            java.util.List<android.accessibilityservice.AccessibilityServiceInfo> accessibilityServiceInfos = am.getEnabledAccessibilityServiceList(-1);
            am.removeClient();
            java.util.List<java.lang.String> result = new java.util.ArrayList<>();
            for (android.accessibilityservice.AccessibilityServiceInfo serviceInfo : accessibilityServiceInfos) {
                android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(serviceInfo.getId());
                if (componentName != null) {
                    result.add(componentName.getPackageName());
                }
            }
            return result;
        } catch (java.lang.Throwable th) {
            am.removeClient();
            throw th;
        }
    }

    private java.util.List<java.lang.String> getInputMethodPackages() {
        java.util.List<android.view.inputmethod.InputMethodInfo> enabledImes = com.android.server.inputmethod.InputMethodManagerInternal.get().getEnabledInputMethodListAsUser(this.mContext.getUserId());
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        for (android.view.inputmethod.InputMethodInfo info : enabledImes) {
            result.add(info.getPackageName());
        }
        return result;
    }

    private java.lang.String getSettingsPackageName() {
        android.content.Intent intent = new android.content.Intent("android.settings.SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        android.content.pm.ResolveInfo resolveInfo = this.mPackageManager.resolveActivity(intent, 786432);
        if (resolveInfo != null) {
            return resolveInfo.activityInfo.packageName;
        }
        return null;
    }

    private java.util.List<java.lang.String> getCriticalPackages() {
        return java.util.Arrays.asList(this.mContext.getResources().getStringArray(android.R.array.config_ntpServers));
    }

    private boolean hasLauncherIntent(java.lang.String packageName) {
        android.content.Intent intentToResolve = new android.content.Intent("android.intent.action.MAIN");
        intentToResolve.addCategory("android.intent.category.LAUNCHER");
        intentToResolve.setPackage(packageName);
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = this.mPackageManager.queryIntentActivities(intentToResolve, 786432);
        return (resolveInfos == null || resolveInfos.isEmpty()) ? false : true;
    }

    private java.lang.String getDefaultSmsPackage() {
        if (android.app.admin.flags.Flags.defaultSmsPersonalAppSuspensionFixEnabled()) {
            android.content.ComponentName defaultSmsApp = com.android.internal.telephony.SmsApplication.getDefaultSmsApplicationAsUser(this.mContext, false, this.mContext.getUser());
            if (defaultSmsApp != null) {
                return defaultSmsApp.getPackageName();
            }
            return null;
        }
        return android.provider.Telephony.Sms.getDefaultSmsPackage(this.mContext);
    }

    void dump(android.util.IndentingPrintWriter pw) {
        pw.println("PersonalAppsSuspensionHelper");
        pw.increaseIndent();
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "critical packages", getCriticalPackages());
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "launcher packages", getSystemLauncherPackages());
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "accessibility services", getAccessibilityServices());
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "input method packages", getInputMethodPackages());
        pw.printf("SMS package: %s\n", new java.lang.Object[]{getDefaultSmsPackage()});
        pw.printf("Settings package: %s\n", new java.lang.Object[]{getSettingsPackageName()});
        com.android.server.devicepolicy.DevicePolicyManagerService.dumpApps(pw, "Packages subject to suspension", getPersonalAppsForSuspension());
        pw.decreaseIndent();
    }
}
