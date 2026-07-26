package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayPolicyWrapper {
    default com.android.server.wm.IDisplayPolicyExt getExtImpl() {
        return new com.android.server.wm.IDisplayPolicyExt() { // from class: com.android.server.wm.IDisplayPolicyWrapper.1
        };
    }

    default com.android.internal.util.ScreenshotHelper getScreenshotHelper() {
        return null;
    }

    default java.lang.Object getServiceAcquireLock() {
        return null;
    }

    default android.os.Handler getHandler() {
        return null;
    }

    default com.android.server.wm.WindowManagerService getWindowManagerService() {
        return null;
    }

    default com.android.server.wm.WindowState getFocusedWindow() {
        return null;
    }

    default android.view.WindowLayout getWindowLayout() {
        return null;
    }

    default android.window.ClientWindowFrames getTmpClientFrames() {
        return null;
    }

    default com.android.server.wm.DisplayContent getDisplayContent() {
        return null;
    }

    default void setBottomGestureAdditionalInset(int height) {
    }

    default int getNavBarFrameHeight(int rotation) {
        return 0;
    }

    default int getBottomGestureAdditionalInset() {
        return 0;
    }

    default int getNavBarHeight(int rotation) {
        return 0;
    }

    default com.android.server.wm.WindowState getNavBar() {
        return null;
    }
}
