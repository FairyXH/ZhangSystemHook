package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityTaskSupervisorExt {
    public static final int REMOVE_TASK_TYPE_KILL = 3;
    public static final int REMOVE_TASK_TYPE_NONE = 0;
    public static final int REMOVE_TASK_TYPE_NOT_KILL_PKG = 1;
    public static final int REMOVE_TASK_TYPE_NOT_KILL_PROC = 2;

    default void setOplusCallingUid(android.content.Intent intent) {
    }

    default void setLaunchTimeStart(com.android.server.wm.ActivityRecord r) {
    }

    default void handleActivityStart(java.lang.String pkgName, java.lang.String processName, int uid) {
    }

    default void handleRemoveTask(com.android.server.wm.Task task, boolean killProcess, boolean removeFromRecents, java.lang.String reason) {
    }

    default void removeAccessControlPassAsUser(java.lang.String packageName, int userId, boolean allUser) {
    }

    default void updateResumeLostActivity(com.android.server.wm.ActivityRecord resumeLostActivity) {
    }

    default void notifyAppSwitch(com.android.server.wm.ActivityRecord resumeGainActivity, com.android.server.wm.ActivityTaskManagerService atms, boolean userLeaving) {
    }

    default boolean isRunningDisallowed(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.WindowManagerService wms) {
        return false;
    }

    default boolean shouldAvoidMoveHomeToFront(com.android.server.wm.Task task, android.app.ActivityOptions activityOptions) {
        return false;
    }

    default void recordTopActivityWhenScreenOff(com.android.server.wm.ActivityTaskManagerService service) {
    }

    default void cameraPreopenIfNeed(android.content.Context context, com.android.server.wm.ActivityRecord r) {
    }

    default boolean handleNonResizableTaskIfNeeded(com.android.server.wm.Task task) {
        return false;
    }

    default boolean startActivityFromRecents(int taskId, android.app.ActivityOptions activityOptions, int callingUid) {
        return true;
    }

    default void sendTheiaEvent(com.android.server.wm.ActivityRecord topResumedActivity, boolean isLaunchTimeoutMsg, android.content.Context context) {
    }

    default boolean startActivityFromRecents(com.android.server.wm.Task task, android.app.ActivityOptions options) {
        return false;
    }

    default void startActivityFromRecents(com.android.server.wm.Task task) {
    }

    default void findTaskToMoveToFront(android.content.Context context) {
    }

    default void handleActivityStartBeforeStartProc(com.android.server.wm.ActivityRecord activityRecord, boolean andResume, int displayId) {
    }

    default void handleActivityStartAfterStartProc(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void hookAcquireLaunchBoost() {
    }

    default void hookRealStartActivityLocked(com.android.server.wm.ActivityRecord r) {
    }

    default void hookStartSpecificActivity(android.content.Context context) {
    }

    default int getRemoveTaskFilterType(com.android.server.wm.WindowProcessController proc) {
        return 0;
    }

    default void modifyApplicaitonInfoForMirageCarMode(com.android.server.wm.ActivityRecord r) {
    }

    default boolean isActivitySelfNotAnimating(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.Task task) {
        return false;
    }

    default void updateRecentWindowingModeIfNeeded(com.android.server.wm.Task task) {
    }

    default void hookBeforeRemoveTask(com.android.server.wm.Task task, java.lang.String reason) {
    }

    default void hookRecordAppStartCount(int uid, java.lang.String packageName, java.lang.String processName) {
    }

    default void handleRemoveTask(boolean killProc, int userId, java.lang.String pkgName) {
    }

    default void resolveActivity(android.content.Intent intent) {
    }

    default int resolvedCallingUid(com.android.server.wm.ActivityRecord caller, android.content.Intent intent, int oriCallingUid) {
        return oriCallingUid;
    }

    default android.content.pm.ResolveInfo getMultiAppResolveInfoIfNeed(android.content.pm.ResolveInfo rInfo, int userId, com.android.server.wm.ActivityTaskSupervisor supervisor, android.content.Intent intent, java.lang.String resolvedType, int filterCallingUid, int callingPid) {
        return null;
    }

    default void adjustStartActivityIntentIfNeed(com.android.server.wm.ActivityRecord r) {
    }

    default void handleActivityIdle(com.android.server.wm.ActivityRecord r) {
    }

    default void requestStateInternal(int requestedState) {
    }

    default boolean isPuttDisplay(int displayId) {
        return false;
    }

    default void removePuttTask(com.android.server.wm.Task task) {
    }

    default int modifyTransitionType(com.android.server.wm.Task task, int transitionType) {
        return transitionType;
    }

    default boolean canStartActivity(com.android.server.wm.ActivityRecord record) {
        return true;
    }

    default void removeContainerTaskForEmbeddedTask(com.android.server.wm.Task embeddedTask) {
    }

    default void updateFlexibleWindowTask(com.android.server.wm.Task reusedTask, android.app.ActivityOptions options, int callingPid) {
    }

    default boolean intercepTaskStartForFlexibleWindow(com.android.server.wm.Task reusedTask, android.content.Context context) {
        return false;
    }

    default boolean exitFlexibleEmbeddedTask(com.android.server.wm.Task task, android.app.ActivityOptions options, boolean moveToBack) {
        return false;
    }

    default void appLaunchTimeout(com.android.server.wm.RootWindowContainer rootWindowContainer, android.content.Context context) {
    }

    default void beforeStartActivityFromRecents(com.android.server.wm.Task task, android.app.ActivityOptions options) {
    }

    default boolean skipUpdateResumedActivityIfNeeded(com.android.server.wm.Task topRootTask, com.android.server.wm.ActivityRecord prevTopActivity, java.lang.String reason) {
        return false;
    }

    default void notifyActivityLaunching(android.content.Intent intent, com.android.server.wm.ActivityRecord caller) {
    }

    default void notifyActivityLaunched(android.content.Intent intent) {
    }

    default void notifyTransitionCollectActivity(com.android.server.wm.ActivityRecord activity, int syncId) {
    }

    default void markScheduleAddStartingWindow(com.android.server.wm.ActivityRecord activity) {
    }

    default void markStartingWindowAddIfNeed(com.android.server.wm.WindowState win) {
    }

    default void markCreateStartingWindowSurfaceIfNeed(com.android.server.wm.WindowState win) {
    }

    default void markStartingWindowDrawnIfNeed(com.android.server.wm.WindowState win) {
    }

    default void markTransitionReady(int syncId) {
    }

    default void markTransitionCommit(int syncId) {
    }

    default void markTransitionFinish(int syncId) {
    }

    default boolean interceptRecentStartForAsyncRotation(com.android.server.wm.Task task, android.app.ActivityOptions options, boolean isCallerRecents) {
        return false;
    }

    default void setStartRecentsReason(android.app.ActivityOptions options, com.android.server.wm.TransitionController controller) {
    }

    default boolean checkIsValidParentForSplitScreen(com.android.server.wm.Task me, com.android.server.wm.Task parent) {
        return true;
    }

    default boolean isMirageDisplay(int displayId) {
        return false;
    }

    default boolean skipStopLauncherWhenRemotePlaying(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void notifyStartActivityFromRecents(com.android.server.wm.Task task, int callinguid, android.app.ActivityOptions activityOptions) {
    }

    default void notifySysActivityColdLaunch(java.lang.Class clazz, com.android.server.wm.ActivityRecord ar, android.content.ComponentName componentInfo) {
    }

    default boolean isLogToolRun() {
        return false;
    }

    default void hookStartInStoppingState(com.android.server.wm.ActivityRecord ar, com.android.server.wm.RootWindowContainer rootWindowContainer) {
    }

    default void handleStartActivity(com.android.server.wm.ActivityRecord r, boolean andResume) {
    }
}
