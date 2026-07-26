package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusDisplayPolicyInner {
    default android.content.Context getContext() {
        return null;
    }

    default com.android.server.wm.WindowManagerService getWindowManagerService() {
        return null;
    }

    default com.android.internal.util.ScreenshotHelper getScreenshotHelper() {
        return null;
    }

    default java.lang.Object getServiceAcquireLock() {
        return null;
    }

    default com.android.server.wm.WindowState getStatusBar() {
        return null;
    }

    default com.android.server.wm.WindowState getNavigationBar() {
        return null;
    }

    default com.android.server.wm.WindowState getTopFullscreenOpaqueWindowState() {
        return null;
    }

    default com.android.server.statusbar.StatusBarManagerInternal getStatusBarManagerInternal() {
        return null;
    }

    default android.os.Handler getHandler() {
        return null;
    }

    default void updateNavigationBarCanMove() {
    }

    default com.android.server.wm.WindowState getFocusedWindow() {
        return null;
    }
}
