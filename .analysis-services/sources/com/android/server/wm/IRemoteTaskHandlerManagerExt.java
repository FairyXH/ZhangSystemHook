package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRemoteTaskHandlerManagerExt {
    default void setRootWindowContainer(com.android.server.wm.RootWindowContainer container) {
    }

    default android.app.ActivityOptions activateRemoteTaskIfNeeded(com.android.server.wm.RootWindowContainer container, android.app.IApplicationThread thread, int callingPid, int callingUid, int realCallingPid, int realCallingUid, com.android.server.wm.Task reuseTask, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord record, android.app.ActivityOptions options) {
        return null;
    }

    default void updateSurface(java.lang.String uuid, android.view.Surface surface) {
    }

    default boolean isFromBackgroundWhiteList(java.lang.String pkgName) {
        return false;
    }

    default boolean closeRemoteTask(int taskId, int reason) {
        return false;
    }

    default void updateRemoteTaskIfNeeded(com.android.server.wm.Task task, android.app.ActivityOptions options) {
    }

    default boolean anyTaskExist(int taskId) {
        return false;
    }

    default boolean findTaskOnlyForLaunch(android.content.Intent intent, java.lang.String affinity, int taskId) {
        return false;
    }

    default boolean interceptFromRecents(com.android.server.wm.Task task, android.content.Intent intent) {
        return false;
    }

    default void handleRemoveTask(com.android.server.wm.Task task, java.lang.String reason) {
    }

    default void handleFinishActivity(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord finishingRecord) {
    }

    default void resetSession() {
    }

    default com.android.server.wm.TaskDisplayArea queryPreferredDisplayArea(com.android.server.wm.Task task, com.android.server.wm.TaskDisplayArea defaultArea, android.content.Intent intent, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord record, android.app.ActivityOptions options) {
        return null;
    }

    default void handleReuseTaskIfNeeded(android.app.IApplicationThread thread, com.android.server.wm.Task reusedTask, android.content.Intent intent, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord record, android.app.ActivityOptions options) {
    }

    default com.android.server.wm.ActivityRecord findTaskForReuseIfNeeded(com.android.server.wm.ActivityRecord startActivity, android.app.ActivityOptions options, com.android.server.wm.TaskDisplayArea area, int launchFlags) {
        return null;
    }

    default boolean isDisplaySwitchDetected() {
        return false;
    }

    default com.android.server.wm.TaskDisplayArea getFinalPreferredTaskDisplayArea() {
        return null;
    }

    default void handleInterceptSessionIfNeeded() {
    }

    default boolean inAnyInterceptSession() {
        return false;
    }

    default void handleProcessDied(com.android.server.wm.WindowProcessController wpc) {
    }

    default boolean shouldIgnoreRelaunch(boolean displayChanged, int previousDisplayId, int currentDisplayId, int changes) {
        return false;
    }

    default boolean isDeliverToCurrentTop(com.android.server.wm.TaskDisplayArea area, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityRecord notTop, int launchFlags, int launchMode) {
        return false;
    }

    default void onConfigurationChanged(android.content.res.Configuration newConfig, com.android.server.wm.DisplayContent display) {
    }
}
