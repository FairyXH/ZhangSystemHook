package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskFragmentExt {
    default void notifyActivityResume(com.android.server.wm.ActivityRecord next) {
    }

    default void topResumedActivityChanged(com.android.server.wm.ActivityRecord r) {
    }

    default void disableSensorScreenShot(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord next, android.content.Context context) {
    }

    default void overrideOrientation(android.content.res.Configuration inOutConfig, com.android.server.wm.DisplayContent dc, int parentOrientation) {
    }

    default void overrideOrientationInFoldDevice(android.content.res.Configuration inOutConfig, com.android.server.wm.DisplayContent dc) {
    }

    default void notifySysActivityColdLaunch(java.lang.Class clazz, android.content.ComponentName componentInfo) {
    }

    default void addChild(com.android.server.wm.ActivityRecord r) {
    }

    default void removeChild(com.android.server.wm.WindowContainer child) {
    }

    default void addTask(com.android.server.wm.WindowContainer parent, com.android.server.wm.Task child, android.content.Intent intent, android.content.pm.ActivityInfo info) {
    }

    default java.lang.String getCounterInfo() {
        return null;
    }

    default void setConfiguration(int windowMode, android.content.res.Configuration config) {
    }

    default void hookHandleTopActivity(com.android.server.wm.ActivityRecord record) {
    }

    default boolean isZoomMode(int mode) {
        return false;
    }

    default void addColorModeOnResume(android.app.servertransaction.ClientTransaction clientTransaction, boolean resume, java.lang.String pkg) {
    }

    default boolean interceptResumeActivity(com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default void setForeAppInfo(com.android.server.wm.ActivityRecord r) {
    }

    default boolean canOccludedBySplitRootTask(com.android.server.wm.TaskFragment me, com.android.server.wm.WindowContainer other) {
        return true;
    }

    default boolean shouldUseParentScreenWidthDp(com.android.server.wm.TaskFragment tf, com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default boolean needMaintainVisibleSate(com.android.server.wm.TaskFragment tf) {
        return false;
    }

    default boolean isPrimaryTopTaskFragment(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.WindowContainer child) {
        return false;
    }

    default int getTaskVisibilityInMultiSearch(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord starting) {
        return -1;
    }

    default boolean isTaskLaunchedFromMultiSearch(com.android.server.wm.Task task) {
        return false;
    }

    default void handleActivityResumed(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task tsk) {
    }

    default boolean isCompactMode(int otherWindowingMode) {
        return false;
    }

    default boolean isBracketMode(int otherWindowingMode) {
        return false;
    }

    default void setPrevWinMode(int windowingMode) {
    }

    default int getPrevWinMode() {
        return -1;
    }

    default void setPreloadTaskFocusedApp(com.android.server.wm.DisplayContent display, com.android.server.wm.ActivityRecord record) {
    }

    default boolean isActivityEmbedded(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default boolean hookIsActivityEmbedded(boolean isEmbedded, com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord activity) {
        return isEmbedded;
    }

    default void onRealActivityStateChanged(com.android.server.wm.ActivityRecord record, com.android.server.wm.ActivityRecord.State state) {
    }

    default boolean shouldRealBeReusmed(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default int reorderIndex(com.android.server.wm.Task ownerTask, com.android.server.wm.ActivityRecord ownerActivity, int index) {
        return index;
    }

    default boolean dontUpdateSmallestWidthInParallelWindow(com.android.server.wm.TaskFragment taskFragment) {
        return false;
    }

    default boolean shouldShipIntersectWithContainingAppBounds(com.android.server.wm.Task task, int windowMode) {
        return false;
    }

    default boolean interceptStartChangeTransitionIfNeed(com.android.server.wm.Task rootTask, android.graphics.Rect startBounds, android.graphics.Rect newBounds) {
        return false;
    }

    default boolean hasFullyOccludedContainer(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.TaskFragment target) {
        return false;
    }

    default void updateWaitActivityToAttachIfNeeded(com.android.server.wm.ActivityRecord resumed, com.android.server.wm.ActivityRecord prev) {
    }

    default boolean affectVisibilityByWindowMode(int otherWindowingMode, com.android.server.wm.WindowContainer other) {
        return false;
    }

    default void disposeFullTfIfNeeded(com.android.server.wm.Task task) {
    }

    default boolean isActivityPreloadDisplay(com.android.server.wm.DisplayContent display) {
        return false;
    }

    default boolean isAllowedToEmbedActivity(com.android.server.wm.ActivityRecord a, int uid) {
        return true;
    }

    default com.android.server.wm.Task getEmbeddedContainerTask(com.android.server.wm.Task origin) {
        return null;
    }

    default void onTaskFragmentPrepareSurface() {
    }

    default boolean canSpecifyOrientationInActivityEmbedding() {
        return false;
    }

    default boolean shouldSkipTaskVisible(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.WindowContainer other) {
        return false;
    }

    default boolean isCreateForMagicWindow() {
        return false;
    }

    default void onTaskFragmentInfoChanged(com.android.server.wm.TaskFragment tf) {
    }

    default boolean shouldRemoveOnLastChildRemoval() {
        return true;
    }

    default void executeAppTransitionForEnterZoomWindowIfNeed(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord resumed) {
    }

    default void triggerAppTransReadyInAdvance(com.android.server.wm.ActivityRecord prev) {
    }

    default void hookSetBinderUxFlag(boolean applyToUx, com.android.server.wm.ActivityRecord prev) {
    }

    default boolean startPausingIfNeed(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord resumedActivity, com.android.server.wm.ActivityRecord resumingActivity) {
        return false;
    }

    default void pauseInRecentsAnim() {
    }

    default void resetPauseStateInRecentsAnim() {
    }

    default boolean shouldDeferResumeUntilRecentsAnimFinished(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default void topActivityHasResumed(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord next, com.android.server.wm.ActivityRecord resumed, com.android.server.wm.TaskDisplayArea taskDisplayArea, android.app.ActivityOptions options) {
    }

    default void applyNoAnimationForResumed(com.android.server.wm.ActivityRecord next, com.android.server.wm.ActivityRecord resumed, android.app.ActivityOptions options) {
    }

    default void hookResumeTopActivityBeforeStartProcess(com.android.server.wm.ActivityRecord next, int displayId) {
    }

    default boolean shouldSkipAdjustAppBounds(com.android.server.wm.TaskFragment taskFragment) {
        return false;
    }

    default boolean canResumeWhilePausing(com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default boolean interceptSetReadyFalse(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord resumed, com.android.server.wm.ActivityRecord resuming) {
        return false;
    }

    default void collectVisibleResumedActivity(com.android.server.wm.ActivityRecord r) {
    }

    default boolean shouldPauseImmediately(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord resuming) {
        return false;
    }

    default void earlyVisibleResumeActivityAndSetReadyIfNeeded(com.android.server.wm.ActivityRecord next) {
    }

    default boolean isStartFromRecents() {
        return false;
    }

    default void resetReadyOnStartPausing(com.android.server.wm.ActivityRecord ar, boolean userLeaving) {
    }

    default boolean isTaskInFlexibleScenario(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isTranslucentSplitTask(com.android.server.wm.ActivityRecord starting) {
        return false;
    }

    default boolean isSkipInsideParentBounds(int windowingMode, boolean insideParentBounds) {
        return false;
    }

    default void ensureVisibilityAndConfigIfPocketStudioExiting(com.android.server.wm.ActivityRecord prev) {
    }

    default void resetOccludeParentWhilePausing(com.android.server.wm.ActivityRecord ar) {
    }
}
