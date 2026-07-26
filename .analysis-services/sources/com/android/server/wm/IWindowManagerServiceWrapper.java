package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowManagerServiceWrapper {
    default com.android.server.wm.WindowState getFocusedWindow() {
        return null;
    }

    default int getForcedDisplayDensityForUserLocked(int userId) {
        return 0;
    }

    default void updateAppOpsState() {
    }

    default boolean dumpWindows(java.io.PrintWriter pw, java.lang.String name, boolean dumpAll) {
        return false;
    }

    default void setWindowAnimationScaleSetting(float value) {
    }

    default void setTransitionAnimationScaleSetting(float value) {
    }

    default com.android.server.wm.IWindowManagerServiceExt getExtImpl() {
        return null;
    }

    default void transferTouchFocus(android.os.IBinder fromChannelToken, android.os.IBinder toChannelToken) {
    }

    default com.android.internal.protolog.common.IProtoLog getSingleInstance() {
        return null;
    }
}
