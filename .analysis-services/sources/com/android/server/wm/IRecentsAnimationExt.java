package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRecentsAnimationExt {
    default void finishAnimation() {
    }

    default void disableSensorScreenShot(android.content.Context context) {
    }

    default boolean hasGestureAnimationController() {
        return false;
    }

    default void notifyCompactWindowState(com.android.server.wm.Task topDisplayFocusedRootTask, boolean b) {
    }

    default void onRecentAnimationStart() {
    }

    default void onRecentAnimationEnd() {
    }

    default com.android.server.wm.TaskDisplayArea getDefaultTaskDisplayArea(android.content.Intent targetIntent, com.android.server.wm.ActivityTaskManagerService atms) {
        return null;
    }

    default boolean startRecentsWhenKeyguardLocked(com.android.server.wm.ActivityRecord targetActivity, com.android.server.wm.WindowManagerService windowManager) {
        return false;
    }

    default void needHideInputMethod(com.android.server.wm.ActivityRecord targetActivity) {
    }

    default void startSecondHomeActivityInBackground(android.content.Intent targetIntent, android.app.ActivityOptions options) {
    }
}
