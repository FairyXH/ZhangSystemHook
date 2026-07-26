package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskOrganizerControllerExt {
    default void hookAddStartingWindow(com.android.server.wm.ActivityRecord activity, android.window.StartingWindowInfo info) {
    }

    default void onBackPressedOnTaskRoot(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onTaskAppeared(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onTaskInfoChanged(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onTaskVanished(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default boolean playShiftUpAnimation(com.android.server.wm.ActivityRecord activity) {
        return true;
    }

    default boolean sameTaskInfoForSplitScreen(android.app.ActivityManager.RunningTaskInfo oldInfo, android.app.ActivityManager.RunningTaskInfo newInfo) {
        return true;
    }

    default void hookSetBinderUxFlag(boolean applyToUx) {
    }

    default boolean shouldDispatchTaskInfoChanged(android.app.ActivityManager.RunningTaskInfo newInfo, android.app.ActivityManager.RunningTaskInfo oldInfo) {
        return false;
    }

    default boolean shouldDispatchTaskInfoChangedForEmbeddedTask(com.android.server.wm.Task newTask, boolean changed) {
        return false;
    }

    default void reportImeDrawnOnTask(int taskId) {
    }

    default void removeStartingWindow(android.window.StartingWindowRemovalInfo removalInfo, com.android.server.wm.Task task) {
    }

    default void checkThreadSafety() {
    }

    default void setWmsLock(java.lang.Object lock) {
    }
}
