package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
public interface IOverlayManagerServiceExt {
    default void init() {
    }

    default void init(android.content.Context context, com.android.server.om.OverlayManagerService overlayManagerService, java.lang.Object mLock, com.android.server.om.OverlayManagerServiceImpl mImpl, android.os.IBinder omsService) {
    }

    default void onSwitchUserWrap(int newUserId, com.android.server.om.IOverlayPackageCacheExt omPmgExt) {
    }

    default boolean handleOnSwitchUser(int newUserId, com.android.server.om.IOverlayPackageCacheExt omPmgExt) {
        return false;
    }

    default void updateAssetsForSwitchUser(int userId, java.util.List<java.lang.String> targetPackageNames) {
    }

    default void initLanguageManager(boolean isSwitchUser, com.android.server.om.OverlayManagerService service, com.android.server.om.OverlayManagerServiceImpl serviceImpl, com.android.server.om.IOverlayPackageCacheExt overlayPmExt, java.lang.Object lock) {
    }

    default void updateLanguagePath(java.lang.String targetPackageName, int userId, java.util.Map<java.lang.String, android.content.pm.overlay.OverlayPaths> pendingChanges, java.util.Collection<java.lang.String> updatedPackages) {
    }

    default void hookAffectedPackages(java.util.List<java.lang.String> affectedPackages, boolean isSwitchUser) {
    }

    default void hookAffectedPackages(java.util.List<java.lang.String> affectedPackages, boolean isSwitchUser, int fromUserId, int userId) {
    }

    default void onStart() {
    }

    default void systemReady() {
    }
}
