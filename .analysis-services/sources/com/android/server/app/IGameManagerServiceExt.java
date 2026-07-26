package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
public interface IGameManagerServiceExt {
    default boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        return false;
    }

    default void setLooper(android.os.Looper looper) {
    }

    default android.os.Looper getLooper() {
        return null;
    }

    default void init(android.content.Context context, com.android.server.app.GameManagerService gameManagerService) {
    }

    default void onBootPhase(int phase) {
    }

    default void onUserUnlocked(com.android.server.SystemService.TargetUser user) {
    }

    default void onPackageChange(java.lang.String actionName, java.lang.String packageName) {
    }

    default void onFGChange(java.lang.String prePkg, java.lang.String nextPkg, int prePid, int nextPid) {
    }

    default boolean isInGameMode() {
        return false;
    }

    default void setUxParam(java.lang.String pkgname, int ui, int render, int state) {
    }

    default boolean isGamePkg(java.lang.String pkg) {
        return false;
    }

    default boolean isGamePadInterceptEnable() {
        return false;
    }

    default int notifyBacklightAnimFinished(int status, float scale) {
        return 0;
    }

    default void setExtraFrameInsertState(java.lang.String pkgName, int state, int fromRate, int toRate) {
    }
}
