package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface INotificationRecordExt {
    public static final int IMPORTANCE_FOR_PACKAGE = 3;

    default long[] modifyVibrationPatternIfNeeded(long[] pattern, boolean insistent) {
        return pattern;
    }

    default boolean hasCustomizeBreathLight() {
        return false;
    }

    default com.android.server.notification.NotificationRecord.Light getCustomizeBreathLight(android.content.Context context) {
        return null;
    }

    default boolean isLoggable() {
        return false;
    }

    default boolean isVersionForJP() {
        return false;
    }

    default android.os.VibrationEffect createDefaultVibration(com.android.server.notification.VibratorHelper helper, boolean insistent) {
        return null;
    }

    default void setAppBanner(boolean bannerOption) {
    }

    default boolean getAppBanner() {
        return true;
    }

    default void setUnimportant(boolean isUnimportant) {
    }

    default boolean isUnimportant() {
        return false;
    }

    default int adjustImportanceForPackage(int importance) {
        return 3;
    }

    default void adjustPackageVisibilityOverride(int appVisibility) {
    }

    default void setSupportConversation(boolean isSupport) {
    }

    default boolean getSupportConversation() {
        return false;
    }

    default com.android.server.notification.NotificationRecord.Light calculateLights(boolean shouldShowLights, com.android.server.notification.NotificationRecord.Light defaultLight) {
        return defaultLight;
    }

    default com.android.server.notification.NotificationRecord.Light calculateLights(boolean isPreChannelsNotification, int channelLightColor, int defaultLightOn, int defaultLightOff) {
        return null;
    }

    default boolean getIsSupportRearLight() {
        return false;
    }

    default int calculateColor(java.lang.String pkg, int userId, int defaultColor) {
        return defaultColor;
    }

    default boolean isMultilLed() {
        return false;
    }

    default boolean isInLightList(java.lang.String pkg, int uid) {
        return false;
    }
}
