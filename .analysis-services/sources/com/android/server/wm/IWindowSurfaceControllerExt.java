package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowSurfaceControllerExt {
    default void updateWindowState(int uid, int pid, int windowId, int windowType, boolean isVisible, boolean shown) {
    }

    default void setShown(boolean surfaceShown, com.android.server.wm.WindowState winState, android.view.SurfaceControl surfaceControl) {
    }

    default void onSecurityPageFlagChanged(com.android.server.wm.WindowState win, boolean surfaceShown, boolean isFromSetSecure) {
    }
}
