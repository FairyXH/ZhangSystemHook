package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IInsetsPolicyExt {
    default boolean showTransient() {
        return false;
    }

    default android.view.InsetsState getInsetsForWindowMetrics(android.view.WindowManager.LayoutParams attrs, com.android.server.wm.WindowToken token, android.view.InsetsState insetsState, com.android.server.wm.DisplayContent dc) {
        return insetsState;
    }

    default boolean isWindowingZoomMode(com.android.server.wm.WindowState focusedWin) {
        return false;
    }

    default boolean isFlexibleTaskIgnoreSysBar(com.android.server.wm.WindowState focusedWin) {
        return false;
    }

    default com.android.server.wm.WindowState getStatusControlTargetInSplit(com.android.server.wm.WindowState focusedWin) {
        return null;
    }

    default boolean shouldForceShowStatusBar(com.android.server.wm.DisplayContent displayContent) {
        return true;
    }

    default void removeSource(android.view.InsetsState state, com.android.server.wm.DisplayContent displayContent) {
    }

    default boolean hasFoldRemapDisplayDisableFeature() {
        return false;
    }

    default com.android.server.wm.WindowState getContainerWindow(com.android.server.wm.WindowState focusedWin, com.android.server.wm.DisplayContent displayContent, boolean isStatusControl) {
        return null;
    }

    default boolean shouldIgnoreNavControlTarget(com.android.server.wm.InsetsControlTarget target) {
        return false;
    }

    default boolean shouldTopFullOpqWinForceCtrlStatusBar(com.android.server.wm.WindowState focusedWin) {
        return false;
    }

    default boolean shouldTopFullOpqWinForceCtrlNavBar(com.android.server.wm.WindowState focusedWin) {
        return false;
    }
}
