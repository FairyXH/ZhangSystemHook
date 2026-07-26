package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageSettingExt {
    default void afterSetForNonSysAppInCreateNewSetting(android.content.pm.UserInfo userInfo) {
    }

    default void afterCreateWithoutOriginInCreateNewSetting(int pkgFlags, boolean allowInstall, java.util.List<android.content.pm.UserInfo> users) {
    }

    default boolean interceptSetInstalledInUpdatePackageSetting(android.content.pm.UserInfo userInfo) {
        return false;
    }
}
