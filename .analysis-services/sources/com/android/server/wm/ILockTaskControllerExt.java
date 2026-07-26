package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ILockTaskControllerExt {
    default void init(android.content.Context context, com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.LockTaskController taskController, java.util.ArrayList<com.android.server.wm.Task> lockTasks, android.util.SparseArray<java.lang.String[]> lockPackages) {
    }

    default boolean isLockDeviceMode() {
        return false;
    }

    default boolean isLockTaskModeViolationInternal(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default boolean isLockTaskModeViolationInternal(com.android.server.wm.WindowContainer wc, int taskAuth) {
        return false;
    }

    default void stopLockDeviceModeBySystem() {
    }

    default boolean setLockTaskMode(java.util.ArrayList<com.android.server.wm.Task> lockTaskModeTasks, com.android.server.wm.Task task) {
        return false;
    }

    default void dump(java.io.PrintWriter pw, java.lang.String prefix) {
    }

    default boolean shouldSkipBlockedAppActivity() {
        return false;
    }
}
