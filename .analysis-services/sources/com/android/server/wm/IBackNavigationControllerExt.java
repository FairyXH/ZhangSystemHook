package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IBackNavigationControllerExt {
    default boolean shouldIgnoreBackKeyEvent(com.android.server.policy.WindowManagerPolicy.WindowState focusWindow) {
        return false;
    }

    default boolean isBackAnimationEnable() {
        return true;
    }

    default boolean isInAnimationBlackList(com.android.server.policy.WindowManagerPolicy.WindowState focusWindow) {
        return false;
    }

    default boolean skipBackAnim(com.android.server.wm.WindowState w) {
        return false;
    }

    default void setBackAnimationInProgress(boolean backAnimationInProgress) {
    }

    default boolean getBackAnimationInProgress() {
        return false;
    }
}
