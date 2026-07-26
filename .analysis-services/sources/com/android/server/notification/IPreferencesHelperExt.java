package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IPreferencesHelperExt {
    public static final int MAX_MESSGAES = 1000;

    default void readXml(com.android.server.notification.IPackagePreferencesExt r, com.android.modules.utils.TypedXmlPullParser parser) {
    }

    default boolean isPreferencesExtDefault(com.android.server.notification.IPackagePreferencesExt r) {
        return false;
    }

    default void writeAttrbute(com.android.modules.utils.TypedXmlSerializer out, com.android.server.notification.IPackagePreferencesExt r) throws java.io.IOException {
    }

    default void updateNotificationChannel(android.app.INotificationChannelExt updatedChannel, com.android.server.notification.IPackagePreferencesExt r) {
    }

    default void setSupportNumBadge(java.lang.String packageName, int uid, boolean support) {
    }

    default boolean getSupportNumBadge(java.lang.String packageName, int uid) {
        return false;
    }

    default void setBadgeOption(java.lang.String packageName, int uid, int option) {
    }

    default int getBadgeOption(java.lang.String packageName, int uid) {
        return 2;
    }

    default void setAppRingtonePermission(java.lang.String packageName, int uid, boolean permissionGranted) {
    }

    default boolean isAppRingtonePermissionGranted(java.lang.String packageName, int uid) {
        return false;
    }

    default void setAppVibrationPermission(java.lang.String packageName, int uid, boolean permissionGranted) {
    }

    default boolean isAppVibrationPermissionGranted(java.lang.String packageName, int uid) {
        return false;
    }

    default void setFold(java.lang.String packageName, int uid, boolean fold) {
    }

    default boolean getFold(java.lang.String packageName, int uid) {
        return false;
    }

    default void setOpush(java.lang.String packageName, int uid, boolean opush) {
    }

    default boolean isOpush(java.lang.String packageName, int uid) {
        return false;
    }

    default void setShowBanner(java.lang.String packageName, int uid, boolean showBanner) {
    }

    default boolean canShowBanner(java.lang.String packageName, int uid) {
        return false;
    }

    default void setShowIcon(java.lang.String packageName, int uid, boolean showIcon) {
    }

    default boolean canShowIcon(java.lang.String packageName, int uid) {
        return true;
    }

    default void setMaxMessages(java.lang.String packageName, int uid, int maxMessages) {
    }

    default int getMaxMessages(java.lang.String packageName, int uid) {
        return 1000;
    }

    default boolean isChangeableFold(java.lang.String packageName, int uid) {
        return true;
    }

    default void setChangeableFold(java.lang.String packageName, int uid, boolean changeable) {
    }

    default boolean isChangeAbleShowIcon(java.lang.String packageName, int uid) {
        return true;
    }

    default void setChangeableShowIcon(java.lang.String packageName, int uid, boolean changeable) {
    }

    default int getStowOption(java.lang.String packageName, int uid) {
        return 0;
    }

    default void setStowOption(java.lang.String packageName, int uid, int option) {
    }

    default boolean getAppBanner(java.lang.String packageName, int uid) {
        return true;
    }

    default void setAppBanner(java.lang.String packageName, int uid, boolean option) {
    }

    default int getAppVisibility(java.lang.String packageName, int uid) {
        return 1;
    }

    default void setAppVisibility(java.lang.String packageName, int uid, int visibility) {
    }

    default java.lang.String getMigMappingPkgName(android.content.Context context, boolean findOldNameByNew, java.lang.String packageName) {
        return packageName;
    }

    default int getMigMappingPkgUid(android.content.Context context, java.lang.String packageName, int uid) {
        return uid;
    }

    default boolean isPkgChanged() {
        return false;
    }

    default void setPkgChanged(boolean value) {
    }
}
