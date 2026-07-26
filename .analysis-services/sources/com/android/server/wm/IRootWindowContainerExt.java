package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRootWindowContainerExt {
    default boolean skipResolveRootTaskIfNeed(com.android.server.wm.Task task) {
        return false;
    }

    default boolean shouldWindowSurfaceSaved(com.android.server.wm.WindowState win, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default void positionSurface(int defaultDw, int defaultDh) {
    }

    default void checkAnimationReady() {
    }

    default boolean shouldSkipUnFreezeCheck(com.android.server.wm.WindowState window) {
        return false;
    }

    default android.content.pm.ActivityInfo switchDefaultLauncherForBootAware(android.content.Context context, android.content.pm.ActivityInfo aInfo, int userId, android.content.Intent homeIntent) {
        return null;
    }

    default void putExtraIfNeededForDisplayingNewFeatures(java.lang.String reason, android.content.Intent homeIntent, int userId) {
    }

    default void hookAcquireLaunchBoost() {
    }

    default void putTasksToSleep(com.android.server.wm.Task task) {
    }

    default com.android.server.wm.Task checkRootHomeTask(com.android.server.wm.Task task, com.android.server.wm.Task homeTask) {
        return null;
    }

    default void setProcRaiseAdjList(java.lang.Object pr) {
    }

    default void handleResizingWindows() {
    }

    default void hookHandleNotObscuredLocked(com.android.server.wm.WindowState w, boolean obscured, boolean sysWin, float screenBrightnessOverride) {
    }

    default void hookPerformSurfacePlacementNoTraceInit() {
    }

    default void updatePendingScreenBrightnessOverrideMap() {
    }

    default void onDisplayAdded(com.android.server.wm.DisplayContent dc) {
    }

    default void onDisplayRemoved(com.android.server.wm.DisplayContent dc) {
    }

    default boolean resumeFocusedSkipped(com.android.server.wm.DisplayContent display, com.android.server.wm.Task targetRootTask, com.android.server.wm.ActivityRecord target) {
        return false;
    }

    default void removeSleepToken(java.lang.String tag, com.android.server.wm.DisplayContent display) {
    }

    default void moveActivityToPinnedRootTask(com.android.server.wm.Task originTask, com.android.server.wm.ActivityRecord pipActivity) {
    }

    default boolean isTaskOnPuttDisplay(com.android.server.wm.Task task) {
        return false;
    }

    default boolean shouldIgnoreKeyguardOccluedTransition(com.android.server.wm.DisplayContent display) {
        return false;
    }

    default com.android.server.wm.ActivityRecord getStartingActivity(com.android.server.wm.WindowProcessController app) {
        return null;
    }

    public interface IFindTaskResultExt {
        default boolean handleIncomingUser(int callUserId, int targetUserId) {
            return false;
        }

        default boolean shouldSkipReuseTask(com.android.server.wm.Task task, android.content.Intent intent) {
            return false;
        }
    }

    default boolean shouldObscureApplicationContentOnSecondaryDisplay() {
        return true;
    }

    default void hooksetUxThreadValue(int pid, int tid, java.lang.String value) {
    }

    default boolean findTaskOnlyForLaunch(com.android.server.wm.ActivityTaskManagerService atmService, com.android.server.wm.RootWindowContainer.FindTaskResult findTaskResult, java.lang.String taskAffinity, android.content.Intent intent) {
        return false;
    }

    default boolean skipSleepTransition(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default boolean resumeSecondHomeIfNeed(com.android.server.wm.DisplayContent display, com.android.server.wm.Task focusedRoot, com.android.server.wm.Task targetRootTask) {
        return false;
    }

    default boolean isNotLargeFoldDevice() {
        return true;
    }

    default boolean isSecondDisplay(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default boolean isWaitingPausingActivity(com.android.server.wm.Task task) {
        return true;
    }

    default boolean pauseImmediately(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord resume) {
        return false;
    }

    default boolean completePauseForQuickStart(com.android.server.wm.Task task) {
        return false;
    }

    default boolean loggingWhenFolding() {
        return false;
    }

    default void checkCachedSurfaceBufferRelease(com.android.server.wm.RootWindowContainer rootWindowContainer) {
    }
}
