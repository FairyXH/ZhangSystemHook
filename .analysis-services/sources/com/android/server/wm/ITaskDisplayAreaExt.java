package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskDisplayAreaExt {
    default void onRootTaskRemoved(com.android.server.wm.Task task) {
    }

    default boolean isZoomMode(int mode) {
        return false;
    }

    default boolean isFlexibleTask(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isFlexibleTaskSink(com.android.server.wm.Task task) {
        return false;
    }

    default void clearZoomChildren() {
    }

    default void addZoomChildren(com.android.server.wm.Task task) {
    }

    default java.util.ArrayList<com.android.server.wm.WindowContainer> getZoomChildren() {
        return new java.util.ArrayList<>();
    }

    default boolean moveSplitScreenTasksToFullScreen(com.android.server.wm.TaskDisplayArea tda, com.android.server.wm.Task toTop) {
        return false;
    }

    default boolean isValidWindowingMode(int windowingMode, com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
        return false;
    }

    default void notifySysActivityStackChange(java.lang.Class clazz, android.content.ComponentName componentInfo) {
    }

    default boolean pauseResumeActivity(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord resuming) {
        return false;
    }

    default boolean isComactValidWindowingMode(int windowingMode) {
        return false;
    }

    default boolean isBracketValidWindowingMode(int windowingMode) {
        return false;
    }

    default boolean validateWindowingMode(boolean inSplitScreenMode, int windowingMode, com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
        return false;
    }

    default void onFocusedTaskChanged(com.android.server.wm.Task lastFocusedTask, com.android.server.wm.Task currentFocusedTask) {
    }

    default boolean isMultiSearchActivityType(int activityType) {
        return false;
    }

    default void setMultiSearchTask(com.android.server.wm.Task task) {
    }

    default boolean isMultiSearchTask(com.android.server.wm.Task task) {
        return false;
    }

    default com.android.server.wm.Task replaceByMultiSearchIfNeed(com.android.server.wm.Task task) {
        return task;
    }

    default int adjustMaxPositionForSplitRootTask(com.android.server.wm.Task rootTask, int maxPosition) {
        return maxPosition;
    }

    default boolean isActivityPreloadDisplay(com.android.server.wm.DisplayContent display) {
        return false;
    }

    default boolean isFlexibleTaskPriorityLower(com.android.server.wm.WindowContainer curr, com.android.server.wm.Task rootTask) {
        return false;
    }

    default boolean shouldIgnoreRotationForSplitMini() {
        return false;
    }

    default boolean isShouldSkipZoomRootTask(com.android.server.wm.Task rootTask) {
        return false;
    }

    default boolean isAppUnlockPasswordActivity(com.android.server.wm.ActivityRecord passwordActivity) {
        return false;
    }
}
