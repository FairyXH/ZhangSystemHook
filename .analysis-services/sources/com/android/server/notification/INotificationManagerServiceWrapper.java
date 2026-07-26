package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface INotificationManagerServiceWrapper {
    default com.android.server.notification.INotificationManagerServiceExt getNMSExt() {
        return null;
    }

    default com.android.server.zenmode.IZenModeManagerExt getZenModeManagerExt() {
        return null;
    }

    default android.content.pm.PackageManager getPackageManagerClient() {
        return null;
    }

    default boolean isTelevision() {
        return false;
    }

    default boolean isAutomotive() {
        return false;
    }

    default boolean notificationEffectsEnabledForAutomotive() {
        return false;
    }

    default boolean systemReady() {
        return false;
    }

    default com.android.server.notification.NotificationUsageStats getNotificationUsageStats() {
        return null;
    }

    default com.android.server.notification.NotificationManagerService.NotificationListeners getNotificationListeners() {
        return null;
    }

    default com.android.server.notification.SnoozeHelper getSnoozeHelper() {
        return null;
    }

    default java.lang.String getSoundNotificationKey() {
        return null;
    }

    default java.lang.String getVibrateNotificationKey() {
        return null;
    }

    default boolean useAttentionLight() {
        return false;
    }

    default com.android.server.lights.LogicalLight getNotificationLight() {
        return null;
    }

    default com.android.server.notification.ZenModeHelper getZenModeHelper() {
        return null;
    }

    default android.app.AlarmManager getAlarmManager() {
        return null;
    }

    default com.android.server.notification.PermissionHelper getPermissionHelper() {
        return null;
    }

    default com.android.server.notification.ShortcutHelper getShortcutHelper() {
        return null;
    }

    default android.app.ActivityManager getActivityManager() {
        return null;
    }

    default android.os.Handler getHandler() {
        return null;
    }

    default android.os.IBinder getAllowListToken() {
        return null;
    }

    default android.os.IBinder getService() {
        return null;
    }

    default void checkCallerIsSameApp(java.lang.String pkg) {
    }

    default void checkCallerIsSystemOrSameApp(java.lang.String pkg) {
    }

    default void checkCallerIsSystem() {
    }

    default boolean isCallerSystemOrPhone() {
        return false;
    }

    default boolean areNotificationsEnabledForPackageInt(java.lang.String pkg, int uid) {
        return false;
    }

    default void doChannelWarningToast(int forUid, java.lang.CharSequence toastText) {
    }

    default boolean checkDisqualifyingFeatures(int userId, int uid, int id, java.lang.String tag, com.android.server.notification.NotificationRecord r, boolean isAutogroup, boolean byForegroundService) {
        return false;
    }

    default boolean isNotificationForCurrentUser(com.android.server.notification.NotificationRecord record) {
        return false;
    }

    default boolean playSound(com.android.server.notification.NotificationRecord record, android.net.Uri soundUri) {
        return false;
    }

    default boolean playVibration(com.android.server.notification.NotificationRecord record, android.os.VibrationEffect effect, boolean delayVibForSound) {
        return false;
    }

    default boolean removeFromNotificationListsLocked(com.android.server.notification.NotificationRecord r) {
        return false;
    }

    default void cancelNotificationLocked(com.android.server.notification.NotificationRecord r, boolean sendDelete, int reason, boolean wasPosted, java.lang.String listenerName, long cancellationElapsedTimeMs) {
    }

    default void updateNotificationPulse() {
    }

    default void clearLightsLocked() {
    }

    default void checkRestrictedCategories(android.app.Notification notification) {
    }

    default boolean isInCall() {
        return false;
    }
}
