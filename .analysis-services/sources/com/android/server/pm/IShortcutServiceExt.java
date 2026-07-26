package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IShortcutServiceExt {
    default boolean beforeGetUserShortcutsOnUnlockUser(int userId) {
        return false;
    }

    default void afterGetUserShortcutsOnUnlockUser(boolean needSave, int userId) {
    }

    default java.lang.String hookGetLauncherPkgNameInLoadFromXml(java.lang.String launcherPackageName) {
        return launcherPackageName;
    }

    default boolean adjustPackageEnabledForIsInstalled(boolean enabled, android.content.pm.ApplicationInfo ai, android.content.pm.IApplicationInfoExt aiExt) {
        return false;
    }

    default boolean adjustVerifyCallerInRequestPinItem(int userId, java.lang.String callingPackage, android.content.pm.ShortcutInfo shortcutInfo) {
        return false;
    }

    default boolean adjustRequestPinItemReturn(java.lang.String launcherPackage, android.content.pm.ShortcutInfo shortcutInfo) {
        return false;
    }

    default void hookInPackageShortcutsChanged(int userId, com.android.server.pm.ShortcutPackage sp) {
    }

    default void startRequestConfirmActivity(android.content.pm.LauncherApps.PinItemRequest request, android.content.Intent confirmIntent) {
    }

    default void backupShortcutData(int userId) {
    }

    default void restoreShortcutData(int userId) {
    }

    default void clearBackupShortcutData(int userId) {
    }

    default int adjustFlagsIfNeed(java.lang.String callingPackage, int flags, int queryFlags, int callingUid, int userId) {
        return flags;
    }
}
