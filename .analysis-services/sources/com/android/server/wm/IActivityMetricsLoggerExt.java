package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityMetricsLoggerExt {
    default boolean isMultiSearchTaskLaunched(com.android.server.wm.ActivityRecord newActivityRecord, com.android.server.wm.ActivityRecord lastActivityRecord) {
        return false;
    }

    default boolean isFlexibleWindowTaskLaunched(com.android.server.wm.ActivityRecord newActivityRecord, com.android.server.wm.ActivityRecord lastActivityRecord) {
        return false;
    }

    default void notifyActivityStarted(java.lang.String packageName, java.lang.String activityName) {
    }

    default int getZoomWindowMode() {
        return 0;
    }

    default int getCompatWindowMode() {
        return 0;
    }

    default int getZoomToFullWindowMode() {
        return 0;
    }

    default int getZoomWindowState() {
        return 0;
    }

    default void startLaunchTrace(boolean processRunning, java.lang.String shortComponentName, int pid, int resultCode, int displayId) {
    }

    default void notifyActivityLaunched(android.content.ComponentName name, int userId, int temperature) {
    }
}
