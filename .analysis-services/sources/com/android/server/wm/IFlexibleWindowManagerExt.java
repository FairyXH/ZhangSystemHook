package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IFlexibleWindowManagerExt {
    public static final java.lang.String KEY_ACTIVITY_NO_ANIM = "androidx.flexible.activityNoAnimation";

    public interface FlexibleWindowTaskStateListener {
        void onStateChanged(int i);
    }

    default void init(com.android.server.wm.ActivityTaskManagerService atms) {
    }

    default void systemReady() {
    }

    default void onSystemReadyEnd(com.android.server.am.ActivityManagerService mAms) {
    }

    default void onFlexibleWindowBackPressedOnTaskRoot(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onFlexibleWindowTaskAppeared(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onFlexibleWindowTaskInfoChanged(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onFlexibleWindowTaskVanished(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void updateFlexibleWindowTask(com.android.server.wm.Task targetTask, com.android.server.wm.Task reusedTask, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityRecord sourceRecord, int callingPid, java.lang.String reason) {
    }

    default void moveTaskToFront(com.android.server.wm.Task tr, android.app.ActivityOptions options, java.lang.String reason) {
    }

    default void prepareSurfaces(com.android.server.wm.Task task) {
    }

    default boolean isSupportEmbedded(int scenario) {
        return false;
    }

    default void adjustInputMethodTargetFrame(com.android.server.wm.DisplayContent dc, com.android.server.wm.WindowState win, android.window.ClientWindowFrames outWindowFrames) {
    }

    default boolean interceptStartActivityFromFlexibleWindow(com.android.server.wm.Task prevTopRootTask, com.android.server.wm.Task targetTask, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityStarter.Request request, com.android.server.wm.ActivityRecord sourceRecord) {
        return false;
    }

    default void onRecentsAnimationExecuting(com.android.server.wm.Task task, boolean executing, int reorderMode) {
    }

    default boolean skipShowRecentTask(com.android.server.wm.Task task, int callingUid) {
        return false;
    }

    default void handleUiModeChanged(int changes) {
    }

    default void preReportDropResult(com.android.server.wm.WindowState win, boolean result) {
    }

    default boolean isEmbbeddingTaskAnimating() {
        return false;
    }

    default void setIsEmbbeddingTaskAnimating(boolean isEmbbeddingTaskAnimating) {
    }

    default boolean isInPocketStudio(int displayId) {
        return false;
    }

    default java.util.ArrayList<com.android.server.wm.WindowState> getSkipWaitingForDrawn() {
        return new java.util.ArrayList<>();
    }

    default void addFlexibleWindowTaskStateListener(com.android.server.wm.IFlexibleWindowManagerExt.FlexibleWindowTaskStateListener listener) {
    }

    default void removeFlexibleWindowTaskStateListener(com.android.server.wm.IFlexibleWindowManagerExt.FlexibleWindowTaskStateListener listener) {
    }

    default boolean toggleFlexibleWindow(java.lang.String toType) {
        return false;
    }

    default boolean needInterceptControlTargetForFlexiblePort() {
        return false;
    }

    default void notifyInsetsChangedLw(com.android.server.wm.DisplayContent dc) {
    }
}
