package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public interface IIdleControllerExt {
    public static final java.lang.String ACTION_FAST_IDLE_TRIGGER_INTENT = "android.intent.action.FAST_IDLE_TRIGGER";

    default void initFastIdle(com.android.server.job.JobSchedulerService service) {
    }

    default void removeTasks(com.android.server.job.controllers.JobStatus job) {
    }

    default void addTasks(com.android.server.job.controllers.JobStatus job) {
    }

    default void updateFastIdleflag() {
    }

    default void handleFastIdleTrigger(boolean screenOn, boolean dockIdle, boolean projectionActive) {
    }
}
