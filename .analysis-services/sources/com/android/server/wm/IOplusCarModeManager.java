package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusCarModeManager extends android.common.IOplusCommonFeature {
    public static final com.android.server.wm.IOplusCarModeManager DEFAULT = new com.android.server.wm.IOplusCarModeManager() { // from class: com.android.server.wm.IOplusCarModeManager.1
    };
    public static final java.lang.String NAME = "IOplusCarModeManager";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusCarModeManager;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean isCarDockBar(android.view.WindowManager.LayoutParams attrs) {
        return false;
    }

    default int validateAddingWindowLw(android.content.Context context, android.view.WindowManager.LayoutParams attrs, int callingPid, int callingUid) {
        return -10;
    }

    default void addWindowLw(com.android.server.wm.WindowState win, android.view.WindowManager.LayoutParams attrs) {
    }

    default void layoutCarDockBar(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.DisplayFrames displayFrames) {
    }

    default void adjustWindowFrameForCarDockBarInsets(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowState win, android.window.ClientWindowFrames outWindowFrames) {
    }

    default void adjustScreenConfigurationForCarLink(com.android.server.wm.DisplayContent displayContent, android.content.res.Configuration outConfig, float density) {
    }
}
