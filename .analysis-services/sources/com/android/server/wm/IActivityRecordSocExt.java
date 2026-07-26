package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityRecordSocExt {
    default void initSoc() {
    }

    default void setTranslucentWindowLaunch(boolean translucentWindowLaunch) {
    }

    default boolean isLaunching() {
        return false;
    }

    default void setLaunching(boolean launching) {
    }

    default void perfLockReleaseHandler() {
    }

    default int isAppInfoGame(android.content.pm.ActivityInfo info) {
        return 0;
    }

    default void acquireActivityBoost(java.lang.String packageName, com.android.server.wm.WindowProcessController app, android.content.pm.ActivityInfo info, com.android.server.wm.ActivityTaskManagerService mAtmService, java.lang.String processName) {
    }

    default void releaseActivityBoost() {
    }

    default int getPerfActivityBoostHandler() {
        return -1;
    }

    default void setPerfActivityBoostHandler(int perfActivityBoostHandler) {
    }

    default boolean isEnableBoostFramework() {
        return false;
    }

    default void hookOnWindowsDrawn() {
    }
}
