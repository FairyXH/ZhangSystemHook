package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IInputMonitorExt {
    default void setOplusInputConfig(com.android.server.wm.InputWindowHandleWrapper inputWindowHandle, com.android.server.wm.WindowState w) {
    }

    default boolean getInputConsumerEnabled() {
        return true;
    }

    default void adjustTouchableRegion(int rotation, android.graphics.Rect rect) {
    }
}
