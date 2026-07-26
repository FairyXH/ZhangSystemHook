package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackagePreferencesExt {
    public static final int APP_VISIBILITY = 1;
    public static final int BADEGE_OPTION = 2;
    public static final int MAX_MESSAGES = 1000;
    public static final int STOW_OPTION = 0;

    default void reset() {
    }

    default void setAppRingtonePermission(boolean permissionGranted) {
    }

    default boolean isAppRingtonePermissionGranted() {
        return false;
    }

    default void setAppVibrationPermission(boolean permissionGranted) {
    }

    default boolean isAppVibrationPermissionGranted() {
        return false;
    }

    default void setFold(boolean fold) {
    }

    default boolean getFold() {
        return false;
    }

    default void setOpush(boolean opush) {
    }

    default boolean getOpush() {
        return false;
    }

    default void setShowBanner(boolean showBanner) {
    }

    default boolean getShowBanner() {
        return false;
    }

    default void setShowIcon(boolean showIcon) {
    }

    default boolean getShowIcon() {
        return true;
    }

    default void setMaxMessages(int maxMessages) {
    }

    default int getMaxMessages() {
        return 1000;
    }

    default void setBadgeOption(int badgeOption) {
    }

    default int getBadgeOption() {
        return 2;
    }

    default void setSupportNumBadge(boolean supportNumBadge) {
    }

    default boolean getSupportNumBadge() {
        return false;
    }

    default void setChangeableFold(boolean changeableFold) {
    }

    default boolean getChangeableFold() {
        return true;
    }

    default void setChangeableShowIcon(boolean changeableShowIcon) {
    }

    default boolean getChangeableShowIcon() {
        return true;
    }

    default void setStowOption(int stowOption) {
    }

    default int getStowOption() {
        return 0;
    }

    default void setAppBanner(boolean appBanner) {
    }

    default boolean getAppBanner() {
        return true;
    }

    default void setAppVisibility(int appVisibility) {
    }

    default int getAppVisibility() {
        return 1;
    }
}
