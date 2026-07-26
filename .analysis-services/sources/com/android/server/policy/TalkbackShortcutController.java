package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
class TalkbackShortcutController {
    private static final java.lang.String TALKBACK_LABEL = "TalkBack";
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;

    TalkbackShortcutController(android.content.Context context) {
        this.mContext = context;
        this.mPackageManager = this.mContext.getPackageManager();
    }

    boolean toggleTalkback(int userId) {
        java.util.Set<android.content.ComponentName> enabledServices = com.android.internal.accessibility.util.AccessibilityUtils.getEnabledServicesFromSettings(this.mContext, userId);
        android.content.ComponentName componentName = getTalkbackComponent();
        if (componentName == null) {
            return false;
        }
        boolean isTalkbackAlreadyEnabled = enabledServices.contains(componentName);
        if (isTalkBackShortcutGestureEnabled()) {
            isTalkbackAlreadyEnabled = !isTalkbackAlreadyEnabled;
            com.android.internal.accessibility.util.AccessibilityUtils.setAccessibilityServiceState(this.mContext, componentName, isTalkbackAlreadyEnabled);
            if (isTalkbackAlreadyEnabled) {
                logStemTriplePressAccessibilityTelemetry(componentName);
            }
        }
        return isTalkbackAlreadyEnabled;
    }

    private android.content.ComponentName getTalkbackComponent() {
        android.view.accessibility.AccessibilityManager accessibilityManager = (android.view.accessibility.AccessibilityManager) this.mContext.getSystemService(android.view.accessibility.AccessibilityManager.class);
        java.util.List<android.accessibilityservice.AccessibilityServiceInfo> serviceInfos = accessibilityManager.getInstalledAccessibilityServiceList();
        for (android.accessibilityservice.AccessibilityServiceInfo service : serviceInfos) {
            android.content.pm.ServiceInfo serviceInfo = service.getResolveInfo().serviceInfo;
            if (isTalkback(serviceInfo)) {
                return new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
            }
        }
        return null;
    }

    boolean isTalkBackShortcutGestureEnabled() {
        return android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "wear_accessibility_gesture_enabled", 0, -2) == 1;
    }

    private void logStemTriplePressAccessibilityTelemetry(android.content.ComponentName componentName) {
        if (!com.android.internal.accessibility.util.AccessibilityUtils.isUserSetupCompleted(this.mContext)) {
            android.provider.Settings.Secure.putInt(this.mContext.getContentResolver(), "wear_accessibility_gesture_enabled_during_oobe", 1);
        } else {
            com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, componentName, 7, true);
        }
    }

    private boolean isTalkback(android.content.pm.ServiceInfo info) {
        return TALKBACK_LABEL.equals(info.loadLabel(this.mPackageManager).toString()) && (info.applicationInfo.isSystemApp() || info.applicationInfo.isUpdatedSystemApp());
    }
}
