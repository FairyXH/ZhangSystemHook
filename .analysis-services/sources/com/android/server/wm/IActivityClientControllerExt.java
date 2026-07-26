package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityClientControllerExt {
    default java.lang.String getCallingPackage(java.lang.String resultPkg, android.os.IBinder token, com.android.server.wm.WindowManagerGlobalLock mGlobalLock) {
        return resultPkg;
    }

    default void hookActivityFinishIfResumeNotOK(com.android.server.wm.ActivityRecord r) {
    }

    default void hookActivityFinishEnd(com.android.server.wm.ActivityRecord r) {
    }

    default void hookActivityResumed(android.os.IBinder token) {
    }

    default void notifyFlexibleWindowTaskVanish(com.android.server.wm.ActivityRecord r, boolean isMoveTaskBack, boolean isNotifyClient) {
    }

    default boolean onBackPressed(com.android.server.wm.ActivityRecord r, android.os.IBinder token) {
        return false;
    }

    default void activityResumed(android.os.IBinder token, int mUserId) {
    }

    default void onActivityRequestOrientation() {
    }

    default boolean setRequestedOrientation(com.android.server.wm.ActivityRecord record, int requestedOrientation, boolean requestToFullScreen) {
        return false;
    }

    default boolean setRequestedOrientationBefore(com.android.server.wm.ActivityRecord record, int requestedOrientation, boolean requestToFullScreen) {
        return false;
    }

    default boolean setRequestedOrientationAfter(com.android.server.wm.ActivityRecord record, int requestedOrientation, boolean requestToFullScreen) {
        return false;
    }

    default boolean ignoringOverridePendingTransition(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default boolean needMoveTaskToBack(android.os.IBinder token, com.android.server.wm.ActivityTaskManagerService sevice, android.content.Context ctx, android.content.ComponentName activity) {
        return false;
    }

    default void moveActivityTaskToBack(com.android.server.wm.Task task, android.os.IBinder token, boolean nonRoot) {
    }

    default void closeRemoteTask(com.android.server.wm.ActivityTaskManagerService sevice, int taskId) {
    }

    default void activityDestroyed(com.android.server.wm.ActivityRecord r) {
    }

    default boolean skipSetTurnScreenOn(com.android.server.wm.ActivityRecord r, boolean turnScreenOn) {
        return false;
    }

    default boolean canOverridePendingTransition(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default boolean moveMainTaskBehindCurrentTaskIfNeed(com.android.server.wm.ActivityRecord activity) {
        return false;
    }
}
