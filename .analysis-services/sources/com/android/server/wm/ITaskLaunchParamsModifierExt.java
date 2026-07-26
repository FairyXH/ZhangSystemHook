package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskLaunchParamsModifierExt {
    default com.android.server.wm.TaskDisplayArea modifierTaskDisplayAreaIfNeed(com.android.server.wm.ActivityTaskSupervisor mSupervisor, com.android.server.wm.TaskDisplayArea taskDisplayArea, com.android.server.wm.Task task, com.android.server.wm.ActivityRecord source) {
        return taskDisplayArea;
    }

    default com.android.server.wm.TaskDisplayArea modifierTaskDisplayAreaIfNeed(com.android.server.wm.ActivityRecord activityRecord, android.app.ActivityOptions options, com.android.server.wm.TaskDisplayArea taskDisplayArea, com.android.server.wm.Task task, com.android.server.wm.ActivityRecord source) {
        return taskDisplayArea;
    }

    default com.android.server.wm.TaskDisplayArea modifierTaskDisplayAreaIfNeed(com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.TaskDisplayArea taskDisplayArea, com.android.server.wm.ActivityRecord activityRecord) {
        return taskDisplayArea;
    }

    default com.android.server.wm.TaskDisplayArea modifierTaskDisplayAreaIfNeed(com.android.server.wm.ActivityTaskSupervisor mSupervisor, com.android.server.wm.TaskDisplayArea taskDisplayArea, com.android.server.wm.ActivityRecord r, boolean modifierDisplayAreaForRecord, com.android.server.wm.Task task) {
        return taskDisplayArea;
    }

    default boolean shouldSkipBoundsChangeForAppInPocketStudio(com.android.server.wm.Task task, int resolvedMode) {
        return false;
    }
}
