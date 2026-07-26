package com.android.server.zenmode;

/* JADX INFO: loaded from: classes3.dex */
public interface IZenModeManagerExt {
    public static final boolean DEBUG = false;
    public static final java.lang.String NAME = "IZenModeManager";

    default void initEnv(android.content.Context context) {
    }

    default boolean isZenModeOn() {
        return false;
    }

    default boolean shouldBlockNotifSound(java.lang.String packageName) {
        return false;
    }

    default void addCallback(java.lang.Object callback) {
    }

    default boolean needBlockWakeUp(int reasonUid, java.lang.String opPackageName, java.lang.String reason) {
        return false;
    }

    default boolean canActivityGo(android.content.pm.ActivityInfo aInfo) {
        return true;
    }

    default boolean canEnterPictureInPicture(java.lang.String pkgName, int uid) {
        return true;
    }

    default boolean canInitAppOpVisibilityLw(java.lang.String pkgName, int uid, int pid) {
        return true;
    }

    default boolean canSetAppOpVisibilityLw(java.lang.String pkgName, int uid) {
        return true;
    }

    default boolean canVibrationGo(java.lang.String pkgName) {
        return true;
    }

    default boolean canSetLights(int lightId, int lightMode, int brightnessMode) {
        return true;
    }
}
