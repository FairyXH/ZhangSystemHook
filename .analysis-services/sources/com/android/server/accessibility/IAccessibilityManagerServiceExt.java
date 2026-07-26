package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public interface IAccessibilityManagerServiceExt {
    default void init(android.content.Context context) {
    }

    default void onUserStateChangedLocked(java.lang.Object userState) {
    }

    default void updateInputFilter(int flag) {
    }

    default android.content.ComponentName replaceOplusUiIntent(android.content.Context context, int shortcutType, android.content.ComponentName componentName) {
        return componentName;
    }

    default void hookPackageMonitorRegister(android.content.Context context, com.android.internal.content.PackageMonitor monitor) {
    }

    default boolean checkIfInstalledServicesNotChange(java.util.List<android.content.pm.ResolveInfo> installedServices, java.lang.Object userState, java.lang.Object policy) {
        return false;
    }

    default boolean addProxyBinder(android.os.IBinder bpBinder, android.os.IInterface iInterface, int uid, int pid) {
        return false;
    }

    default boolean removeProxyBinder(android.os.IBinder bpBinder, android.os.IInterface iInterface) {
        return false;
    }

    default java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getAccessibilityServiceAfterCheckCustomizeWhiteList(android.content.Context context, java.util.List<android.accessibilityservice.AccessibilityServiceInfo> allList) {
        return new java.util.ArrayList(0);
    }

    default android.view.accessibility.AccessibilityNodeInfo getAccessibilityFocusNotLocked(android.view.accessibility.AccessibilityNodeInfo nodeInfo, android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action) {
        return null;
    }
}
