package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IZenModeHelperExt {
    default boolean interceptOnUserUnlocked(int user) {
        return false;
    }

    default void init(com.android.server.notification.ZenModeHelper zenModeHelper, android.content.Context context, android.app.AppOpsManager appOps) {
    }

    default boolean applyRestrictions(int zenMode, long suppressedEffects, android.app.NotificationManager.Policy policy, java.lang.String[] priorityOnlyDndExemptPackages) {
        return false;
    }

    default void applyRestrictions(java.lang.String[] priorityOnlyDndExemptPackages, boolean mute, int usage, int code) {
    }

    default void applyRestrictions(java.lang.String[] priorityOnlyDndExemptPackages, boolean mute, int usage) {
    }

    default void setPriorityOnlyDndExemptPackages(java.lang.String[] packages) {
    }

    default android.service.notification.ZenModeConfig adjustZenModeConfig(android.service.notification.ZenModeConfig config) {
        return config;
    }

    default void setZenModeExtInfoStr(android.content.Context context, java.lang.String info) {
    }

    default boolean isOplusRule() {
        return false;
    }
}
