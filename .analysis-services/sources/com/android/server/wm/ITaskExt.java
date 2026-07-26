package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskExt {
    public static final int SCREEN_ORIENTATION_UNFIXED = -100;

    default boolean shouldUseTaskDimmer(com.android.server.wm.Task task, com.android.server.wm.Dimmer dimmer) {
        return !task.isTranslucent(null);
    }

    default boolean isInterruptClearAccessPassword() {
        return false;
    }

    default void setInterruptClearAccessPassword(boolean flag) {
    }

    default boolean isNoAnimationTask(int taskId) {
        return false;
    }

    default int getFlexibleCustomTransitionType() {
        return 0;
    }

    default void addFlexibleCustomTransitionType(int transitionType) {
    }

    default void removeFlexibleCustomTransitionType(int transitionType) {
    }

    default boolean hasFlexibleCustomTransitionType(int transitionType) {
        return false;
    }

    default void saveFixedRotatedTaskWhenKeyGuardGoingAway(com.android.server.wm.Task task, int transit, boolean enter) {
    }

    default void notifyActivityResume(com.android.server.wm.ActivityRecord record) {
    }

    default void cancelTaskChildrenAnimationIfNeed(com.android.server.wm.Task task) {
    }

    default void hansTopOrSecondActivityIfNeeded(com.android.server.wm.ActivityRecord next) {
    }

    default boolean interceptResumeActivity(com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default void disableSensorScreenShot(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord next, android.content.Context context) {
    }

    default void saveAppUsageHistoryRecord(com.android.server.wm.ActivityRecord record) {
    }

    default void setRootLockDeviceTask(boolean flags) {
    }

    default boolean getRootLockDeviceTask() {
        return false;
    }

    default boolean getScreenOffPlay() {
        return false;
    }

    default void setSceenOffPlay(boolean offPlay) {
    }

    default void dump(java.io.PrintWriter pw, java.lang.String prefix) {
    }

    default void notifySysActivityHotLaunch(java.lang.Class clazz, android.content.ComponentName componentInfo) {
    }

    default void notifySysActivityColdLaunch(java.lang.Class clazz, android.content.ComponentName componentInfo) {
    }

    default boolean isScreenOffPlay(com.android.server.wm.Task task) {
        return false;
    }

    default void setForeAppInfo(com.android.server.wm.ActivityRecord r) {
    }

    default boolean isDragZoomTask(com.android.server.wm.Task task) {
        return false;
    }

    default boolean positionChildAtBottom(com.android.server.wm.Task parentTask, com.android.server.wm.Task nextFocusableRootTask, com.android.server.wm.Task child) {
        return false;
    }

    default void setAppTransitionReadyInAdvance(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord activity) {
    }

    default boolean forceCreateRemoteAnimationTarget(com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default void addStartingBackColorLayerIfNeed(com.android.server.wm.ActivityRecord activity) {
    }

    default boolean skipPreapreSurface(com.android.server.wm.TransitionController transitionController) {
        return false;
    }

    default boolean sholdUpdateSplitScreenLauncherDim(com.android.server.wm.Task task, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default void hookHandleTopActivity(com.android.server.wm.ActivityRecord record) {
    }

    default int getFixedScreenOrientation() {
        return -100;
    }

    default void setFixedScreenOrientation(int orientation) {
    }

    default boolean shouldFixConfigOrientation() {
        return false;
    }

    default void setRequestFixConfigOrientation() {
    }

    default void setFixedMinAspectRatio(float ratio) {
    }

    default float getFixedMinAspectRatio() {
        return -1.0f;
    }

    default void setFixedMaxAspectRatio(float ratio) {
    }

    default float getFixedMaxAspectRatio() {
        return -1.0f;
    }

    default void setUseOriginOrientationConfig(boolean useOriginConfig) {
    }

    default boolean shouldUseOriginOrientationConfig() {
        return false;
    }

    default void setShouldRelaunchConfig(int relaunchConfig) {
    }

    default int getShouldRelaunchConfig() {
        return -1;
    }

    default void setEnableRotationWhenFolding(boolean enable) {
    }

    default boolean enableRotationWhenFolding() {
        return false;
    }

    default void setForceAllowAllOrientation(boolean forceAllowAllOrientation) {
    }

    default boolean isForceAllowAllOrientation() {
        return false;
    }

    default void setForceAppOrientationConfig(int forceAppOrientationConfig) {
    }

    default int getForceAppOrientationConfig() {
        return -1;
    }

    default boolean isCompactWindowingMode(int windowingMode) {
        return false;
    }

    default void onWindowingModeChanged(com.android.server.wm.Task task, int currentMode) {
    }

    default boolean supportMultiResume(com.android.server.wm.ActivityRecord resumingActivity) {
        return false;
    }

    default boolean pauseResumeActivity(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default boolean noAnimForRelatedActivity(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default void handleTaskCreated(com.android.server.wm.Task task) {
    }

    default void reparentTask(com.android.server.wm.Task task, com.android.server.wm.Task toRootTask) {
    }

    default void onTaskParentChanged(com.android.server.wm.ConfigurationContainer oldParent, com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.Task task) {
    }

    default boolean canClearActivityRecord(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default boolean onConfigurationChangedOfTask(android.content.res.Configuration newParentConfig, android.graphics.Rect mTmpPrevBounds, com.android.server.wm.Task task) {
        return false;
    }

    default void adjustAppBoundsInCompactWindowMode(com.android.server.wm.Task rootTask, android.graphics.Rect mTmpStableBounds, android.graphics.Rect outAppBounds, android.view.DisplayInfo di, int windowingMode) {
    }

    default boolean updateConfigWidthForPageModeSetting(com.android.server.wm.Task task, int windowMode) {
        return false;
    }

    default boolean updateSWDpInCompactWindowingMode(com.android.server.wm.Task task, int windowingMode) {
        return false;
    }

    default void hideBackgroundSurface(com.android.server.wm.Task task) {
    }

    default void removeCompactMask(com.android.server.wm.Task task, boolean force) {
    }

    default void onApplyNoAnimationOfTask(com.android.server.wm.Task task) {
    }

    default void notifyCompactWindowState(com.android.server.wm.Task task, boolean hasFocus) {
    }

    default boolean isInPendingAnimation(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isDragZoomMode() {
        return false;
    }

    default void launchIntoCompactwindowingMode(com.android.server.wm.Task task, boolean tofront) {
    }

    default void handleRemoveTask(java.lang.String packageName, int mUserId) {
    }

    default void prepareSurfaces(android.view.SurfaceControl.Transaction transaction, com.android.server.wm.DisplayContent displayContent, com.android.server.wm.Task task) {
    }

    default void prepareDimBounds(com.android.server.wm.TaskFragment taskFragment, android.graphics.Rect dimBounds) {
    }

    default void onTaskWindowFocusChanged(com.android.server.wm.Task task, boolean hasFocus) {
    }

    default void onSetTaskIntent(com.android.server.wm.Task task, android.content.pm.ActivityInfo info, boolean updateIdentity) {
    }

    default void applyNewOrientationWhenReuseIfNeed(com.android.server.wm.ActivityRecord reusedActivity, com.android.server.wm.ActivityRecord newActivity) {
    }

    default boolean supportsSplitScreenByVendorPolicy(com.android.server.wm.Task task, boolean candidate) {
        return candidate;
    }

    default void onDescendantOrientationChanged(com.android.server.wm.WindowContainer requestingContainer) {
    }

    default void startFreezingDisplay(com.android.server.wm.Task tr, com.android.server.wm.ActivityTaskManagerService mAtmService) {
    }

    default boolean isForceAlwaysOnTop(com.android.server.wm.Task task) {
        return false;
    }

    default void onTaskWindowingModeChanged(com.android.server.wm.Task task, int prevMode, int newMode) {
    }

    default void notifyChildActivityRecordAdded(com.android.server.wm.ActivityRecord r) {
    }

    default void notifyChildActivityRecordRemoved(com.android.server.wm.ActivityRecord r) {
    }

    default void notifyFlexibleWindowTaskUpdate() {
    }

    default void setCreateForSingleSplit(boolean b) {
    }

    default boolean isCreateForSingleSplit() {
        return false;
    }

    default boolean checkVisibleForSplit(boolean gotSplitScreenStack, boolean gotTranslucentSplitScreenSecondary, com.android.server.wm.Task task) {
        return false;
    }

    default boolean isInSplitBlackList(java.lang.String name) {
        return false;
    }

    default boolean isPendingToBottomTask(int id) {
        return false;
    }

    default void onTaskRemoved(com.android.server.wm.Task task) {
    }

    default void setLaunchedFromMultiSearch(boolean fromMultiSearch) {
    }

    default boolean getLaunchedFromMultiSearch() {
        return false;
    }

    default void addExtraTaskInfo(com.android.server.wm.Task task, android.app.TaskInfo info) {
    }

    default void skipNextLaunchIntoCompactWindowingMode(boolean skip) {
    }

    default boolean isFlexibleAlwaysOnTop() {
        return false;
    }

    default boolean shouldSkipLaunchIntoCompactWindowingMode() {
        return false;
    }

    default boolean dontPauseAfterQActivityIfNeed(com.android.server.wm.Task task) {
        return true;
    }

    default boolean adjustMoveDisplayToTopForMirage(int displayId, boolean moveDisplayToTop) {
        return moveDisplayToTop;
    }

    default void setDisableFeatures(java.lang.String features) {
    }

    default java.lang.String getDisableFeatures() {
        return "";
    }

    default boolean handleActivityReorder(com.android.server.wm.Task task, com.android.server.wm.WindowContainer child, int position, boolean toTop) {
        return false;
    }

    default void excuteAppTransitionForCompactWindowIfNeed(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
    }

    default boolean isTaskInreParent() {
        return false;
    }

    default boolean shouldDoPuttTransition(int taskId) {
        return false;
    }

    default void onTaskTopActivityCrashed(com.android.server.wm.Task task) {
    }

    default void setPuttTask(boolean putt) {
    }

    default boolean isPuttTask() {
        return false;
    }

    default void onTaskParentChanged(com.android.server.wm.DisplayContent oldDisplay, com.android.server.wm.DisplayContent newDisplay, com.android.server.wm.ConfigurationContainer oldParent, com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.Task task) {
    }

    default boolean ignoreResumePuttTask(com.android.server.wm.Task task) {
        return false;
    }

    default void removedFromRecents(com.android.server.wm.Task task) {
    }

    default void handleConfigChanged(android.content.res.Configuration newParentConfig, android.graphics.Rect mTmpRect, com.android.server.wm.Task task) {
    }

    default boolean isNeedMask() {
        return false;
    }

    default void setNeedMask(boolean needMask) {
    }

    default void setLaunchSplashTheme(int theme) {
    }

    default int getLaunchSplashTheme() {
        return 0;
    }

    default void setLaunchSplashThemePackage(java.lang.String packageName) {
    }

    default java.lang.String getLaunchSplashThemePackage() {
        return null;
    }

    default java.lang.Object getZoomStateManager() {
        return null;
    }

    default void setZoomStateManager(java.lang.Object zoomStateManager) {
    }

    default java.lang.Object getFlexibleInfoManager() {
        return null;
    }

    default void setFlexibleInfoManager(java.lang.Object flexibleInfoManager) {
    }

    default void setOriginTaskIdForZoomWindow(int taskId) {
    }

    default int getOriginTaskIdForZoomWindow() {
        return -1;
    }

    default void moveTaskToFront(android.app.ActivityOptions options, com.android.server.wm.Task task) {
    }

    default void moveTaskToBack(com.android.server.wm.Task task, com.android.server.wm.Task root) {
    }

    default boolean isZoomMode(int mode) {
        return false;
    }

    default void notifyZoomModeChanged(int currentWindowMode, int preWindowMode) {
    }

    default boolean isMiniRootTask(com.android.server.wm.Task task) {
        return false;
    }

    default boolean skipFlexibleTask(com.android.server.wm.Task task, boolean allowFocusSelf) {
        return false;
    }

    default void removeChild(com.android.server.wm.WindowContainer child) {
    }

    default void addChild(com.android.server.wm.WindowContainer child) {
    }

    default boolean hasNoSurfaceShowing(com.android.server.wm.Task task, boolean isVisible, boolean lastSurfaceShowing) {
        return false;
    }

    default void setLaunchParams(android.app.ActivityOptions options) {
    }

    default void resetLaunchParams() {
    }

    default int getLaunchScenario() {
        return 0;
    }

    default int getLaunchConfigScenario() {
        return 0;
    }

    default boolean isFlexibleWindowScenario(int... excludeScenarios) {
        return false;
    }

    default boolean isFlexibleWindowScenario(android.app.ActivityOptions options) {
        return false;
    }

    default boolean isStartZoomFormFloatScenario(android.app.ActivityOptions options) {
        return false;
    }

    default void setIsTaskEmbedded(boolean isEmbedded) {
    }

    default boolean isTaskEmbedded() {
        return false;
    }

    default void setIgnoreHiddenFlag(boolean flag) {
    }

    default boolean getIgnoreHiddenFlag() {
        return false;
    }

    default boolean isRemoveTaskWhenDetach() {
        return false;
    }

    default boolean isConfigChangeWithContainer() {
        return false;
    }

    default void setConfigChangeWithContainer(boolean isConfigChangeWithContainer) {
    }

    default boolean isAlwaysOnTop() {
        return false;
    }

    default float getScale() {
        return 1.0f;
    }

    default float getCurrentScale() {
        return 1.0f;
    }

    default boolean isFlexibleTaskMaximized() {
        return false;
    }

    default void setFlexibleTaskMaximized(boolean isFlexibleTaskMaximized) {
    }

    default void setFlexibleTaskDraggingPos(android.graphics.Rect pos) {
    }

    default android.graphics.Rect getFlexibleTaskDraggingPos() {
        return null;
    }

    default android.graphics.Point getFlexibleStartDragPoint() {
        return new android.graphics.Point(0, 0);
    }

    default void setFlexibleStartDragPoint(int x, int y) {
    }

    default boolean isFlexibleTaskChanging() {
        return false;
    }

    default void setFlexibleTaskChanging(boolean isFlexibleTaskChanging) {
    }

    default boolean isFlexibleTaskRecenting() {
        return false;
    }

    default void setFlexibleTaskRecenting(boolean isFlexibleTaskRecenting) {
    }

    default boolean isFlexibleTaskMaximizing() {
        return false;
    }

    default void setFlexibleTaskMaximizing(boolean isFlexibleTaskMaximizing) {
    }

    default void updateSpruceBundle(android.os.Bundle spruceBundle) {
    }

    default android.os.Bundle getSpruceBundle() {
        return null;
    }

    default void updateDriveBundle(android.os.Bundle driveBundle) {
    }

    default android.os.Bundle getDriveBundle() {
        return null;
    }

    default boolean isFlexibleTaskOrientationChangeWhenMaximizing() {
        return false;
    }

    default void setFlexibleTaskOrientationChangeWhenMaximizing(boolean flexibleTaskOrientationChangeWhenMaximizing) {
    }

    default boolean isScaleWhileMaximizing() {
        return false;
    }

    default void setScaleWhileMaximizing(boolean isScaleWhileMaximizing) {
    }

    default boolean isNeedDefaultAnimation() {
        return false;
    }

    default boolean isNeedMaintainTaskState() {
        return false;
    }

    default boolean isResizeForOrientationChange() {
        return false;
    }

    default boolean isHasCaption() {
        return false;
    }

    default boolean isChangeToSplit() {
        return false;
    }

    default boolean isIgnoreSystemBar() {
        return false;
    }

    default int getCurrentShadows() {
        return 0;
    }

    default void setCurrentShadowsRadius(int shadows) {
    }

    default boolean getFullScreenByFullButton() {
        return false;
    }

    default void setFullScreenByFullButton(boolean fullScreenByFullButton) {
    }

    default java.lang.String getSupportRatios() {
        return null;
    }

    default void setCurrentScale(float currentScale) {
    }

    default float getMaxScale() {
        return 1.0f;
    }

    default float getMinScale() {
        return 1.0f;
    }

    default boolean isFocusChangeWithNonFlexible() {
        return false;
    }

    default boolean isShowWindowResizeBar() {
        return true;
    }

    default boolean isAdjustInputMethod() {
        return false;
    }

    default void setScale(float scale) {
    }

    default int getCornerRadius() {
        return 0;
    }

    default int getCurrentCornerRadius() {
        return 0;
    }

    default void setCurrentCornerRadius(int cornerRadius) {
    }

    default int getShadowRadiusFocused() {
        return 0;
    }

    default int getShadowRadiusUnfocused() {
        return 0;
    }

    default void setFlexibleFrame(com.android.server.wm.Task task, android.graphics.Rect frame) {
    }

    default void setFlexibleHandleFrame(android.graphics.Rect frame) {
    }

    default void setFlexibleTaskViewFrame(android.graphics.Rect frame) {
    }

    default android.graphics.Rect getFlexibleTaskViewFrame() {
        return new android.graphics.Rect();
    }

    default void setEmbeddedTaskFrame(int taskId, android.graphics.Rect frame) {
    }

    default android.graphics.Rect getFlexibleFrame() {
        return new android.graphics.Rect();
    }

    default void setEmbeddedTaskReuseFullAnim(boolean reUseAnim) {
    }

    default boolean getEmbeddedTaskReuseFullAnim() {
        return false;
    }

    default android.graphics.Rect getFlexibleHandleFrame() {
        return new android.graphics.Rect();
    }

    default android.graphics.Rect getEmbeddedTaskFrame(int taskId) {
        return new android.graphics.Rect();
    }

    default void removeEmbeddedTaskFrame(int taskId) {
    }

    default void setEmbeddedContainerTask(int containerTaskId) {
    }

    default int getEmbeddedContainerTaskId() {
        return -1;
    }

    default void addEmbeddedChildren(int taskId) {
    }

    default void removeEmbeddedChildren(int taskId) {
    }

    default void removeEmbeddedChildrenList(java.util.List<java.lang.Integer> taskIdList) {
    }

    default int getContainerActivityHash() {
        return 0;
    }

    default void setContainerActivityHash(int containerActivityHash) {
    }

    default android.util.ArraySet<java.lang.Integer> getEmbeddedChildren() {
        return new android.util.ArraySet<>();
    }

    default java.util.List<android.app.ActivityManager.RecentTaskInfo> getEmbeddedRecentTasks() {
        return new java.util.ArrayList();
    }

    default void setContainerType(int containerType) {
    }

    default int getContainerType() {
        return 0;
    }

    default boolean isEmbeddedAcrossTaskSupported() {
        return false;
    }

    default void setFlexibleZoomState(int flexibleZoomState) {
    }

    default void setFlexibleZoomPrevState(int flexibleZoomState) {
    }

    default int getFlexibleZoomPrevState() {
        return 0;
    }

    default int getFlexibleZoomState() {
        return 0;
    }

    default boolean isTaskCanvas() {
        return false;
    }

    default void setTaskCanvas(boolean isTaskCanvas) {
    }

    default java.lang.String getCanvasTaskChildrenInfoForXML() {
        return null;
    }

    default void setTaskCanvasRestore(boolean isTaskCanvas, java.util.List<java.lang.String> embeddedChildrenInfo) {
    }

    default boolean isContainerTask() {
        return false;
    }

    default void setContainerTask(boolean isContainerTask) {
    }

    default boolean isShowRecent() {
        return true;
    }

    default void setShowRecent(boolean isShowRecent) {
    }

    default int getHookAppBounds() {
        return -1;
    }

    default void setHookAppBounds(int value) {
    }

    default boolean getSupportAllMode() {
        return false;
    }

    default void setSupportAllMode(boolean value) {
    }

    default boolean isReparentToTaskView() {
        return false;
    }

    default boolean shouldHideEmbeddedSurfaceBeforeReparent() {
        return false;
    }

    default void setIsReparentToTaskView(boolean isReparentToTaskView) {
    }

    default boolean isAvoidMoveTaskToFront(android.app.ActivityOptions options) {
        return false;
    }

    default com.android.server.wm.ActivityRecord findEnterPipOnTaskSwitchCandidateForPs(com.android.server.wm.Task task) {
        return null;
    }

    default boolean isSkipAnimation() {
        return false;
    }

    default void setIsSkipAnimation(boolean isSkipAnimation) {
    }

    default boolean isSkipBackToPrevTask() {
        return false;
    }

    default void setSkipBackToPrevTask(boolean isSkipBackToPrevTask) {
    }

    default boolean isDispatchTaskInfoChange() {
        return false;
    }

    default void setIsDispatchTaskInfoChange(boolean isDispatchTaskInfoChange) {
    }

    default boolean isSkipWaitingForDrawn() {
        return false;
    }

    default void setIsSkipWaitingForDrawn(boolean isSkipWaitingForDrawn) {
    }

    default int getSimulateDensity() {
        return 0;
    }

    default int getSimulatedWidth() {
        return 0;
    }

    default boolean isFromExtraLauncher() {
        return false;
    }

    default void adjustTaskConfiguration(com.android.server.wm.Task task, android.content.res.Configuration newParentConfig) {
    }

    default android.graphics.Rect getLastBounds() {
        return null;
    }

    default void setLastBounds(android.graphics.Rect bounds) {
    }

    default boolean getForceUpdateSurface() {
        return false;
    }

    default void setForceUpdateSurface(boolean force) {
    }

    default void onTaskOrganizerApplyChanges(com.android.server.wm.WindowContainer wc, int prevMode, int newMode, android.content.res.Configuration changeConfig) {
    }

    default void sendBroadcastResumedActivity(android.os.Handler handler, android.content.Context context, com.android.server.wm.ActivityRecord r) {
    }

    default boolean resumeTopActivityInnerInCompactWindow(boolean[] resumed, com.android.server.wm.ActivityRecord prev, android.app.ActivityOptions options, boolean deferPause) {
        return false;
    }

    default boolean cropWindowsToRootTaskBounds(com.android.server.wm.Task task) {
        return true;
    }

    default boolean shouldUseSelfDimmer() {
        return false;
    }

    default long getLastResumedActivityStamp() {
        return 0L;
    }

    default void setLastResumedActivityStamp(long mLastResumedActivityStamp) {
    }

    default boolean getAllowReparent() {
        return false;
    }

    default void setAllowReparent(boolean allowReparent) {
    }

    default boolean isForceUpdateWindow() {
        return false;
    }

    default void setForceUpdateWindow(boolean forceUpdateWindow) {
    }

    default boolean shouldSkipRotationForFlexibleWindow() {
        return false;
    }

    default void updateAlphaInPinnedMode(com.android.server.wm.Task task, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl surfaceControl) {
    }

    default void setLaunchScenario(int preferredScenario, boolean creating) {
    }

    default boolean shouldInterceptInputEvent() {
        return false;
    }

    default boolean allowTaskDetachFromEmbedded() {
        return false;
    }

    default boolean getFlexibleEmbedding() {
        return false;
    }

    default void setFlexibleEmbedding(boolean state) {
    }

    default boolean isFlexibleEmbedded() {
        return false;
    }

    default boolean isSmartBackend() {
        return false;
    }

    default boolean isParentChanged(com.android.server.wm.Task currentParent, com.android.server.wm.Task oldParent) {
        return false;
    }

    default boolean isMaintainTaskState() {
        return false;
    }

    default void setMaintainTaskState(boolean isMaintainTaskState) {
    }

    default boolean isNeedReCalcBounds() {
        return false;
    }

    default void setNeedReCalcBounds(boolean mNeedReCalBounds) {
    }

    default boolean shouldUpdateTransitLocked(com.android.server.wm.ActivityRecord r, int transit, android.app.ActivityOptions options) {
        return false;
    }

    default boolean isNeedSkipCloseTransition() {
        return false;
    }

    default boolean isNeedSetAlwaysOnTopOnly(boolean alwaysOnTop) {
        return false;
    }

    default void setNeedSkipCloseTransition(boolean needSkipCloseTransition) {
    }

    default boolean isFromSplitToZoom() {
        return false;
    }

    default void setIsFromSplitToZoom(boolean isFromSplitToZoom) {
    }

    default boolean isNeedForceStopWhenSubDisplayScenario() {
        return false;
    }

    default void setNeedForceStopWhenSubDisplayScenario(boolean mNeedForceStopWhenSubDisplayScenario) {
    }

    default void migrateToNewSurfaceControl(android.view.SurfaceControl.Transaction transaction, android.view.SurfaceControl newParent) {
    }

    default boolean isTaskMatchDisplay() {
        return false;
    }

    default void onPreShowStartingWindow(com.android.server.wm.ActivityRecord r, boolean doShow) {
    }

    default void reuseOrCreateTask(android.content.pm.ActivityInfo info, android.content.Intent intent, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options) {
    }

    default com.android.server.wm.Task getPrevTask() {
        return null;
    }

    default com.android.server.wm.Task getNextTask() {
        return null;
    }

    default void setPrevTask(com.android.server.wm.Task prevTask) {
    }

    default void setNextTask(com.android.server.wm.Task nextTask) {
    }

    default boolean isFullScreenTask() {
        return false;
    }

    default boolean isTaskBarAnim() {
        return false;
    }

    default void setIsTaskBarAnim(boolean mIsTaskBarAnim) {
    }

    default boolean isForceHideByRemoveTask() {
        return false;
    }

    default void forceHideByRemoveTask(boolean force) {
    }

    default boolean isInMiniMode(com.android.server.wm.Task task) {
        return false;
    }

    default boolean resumeFocusedTasksTopActivities(com.android.server.wm.Task topActivity, com.android.server.wm.Task task) {
        return false;
    }

    default void removeStartingSurfaceWhenVisibleChange(com.android.server.wm.Task task) {
    }

    default void setSharedStartingWindow(boolean sharedStartingWindow) {
    }

    default boolean getSharedStaringWindow() {
        return false;
    }

    default void removeSharedStartingWindowIfNeeded(com.android.server.wm.Task task) {
    }

    default void setAppQuickStartingSurface(java.lang.Object appQuickStartingSurface) {
    }

    default java.lang.Object getAppQuickStartingSurface() {
        return null;
    }

    default boolean isNeedSkipChangeTransition() {
        return false;
    }

    default void setIsNeedSkipChangeTransition(boolean needSkipChangeTransition) {
    }

    default void setSplitTaskId(int taskId) {
    }

    default int getSplitTaskId() {
        return -1;
    }

    default void setMainTaskId(int taskId) {
    }

    default int getMainTaskId() {
        return -1;
    }

    default void setResetFlexibleTaskReason(java.lang.String reason) {
    }

    default java.lang.String getResetFlexibleTaskReason() {
        return null;
    }

    default void skipToEndFlexibleCustomAnimBeforeTransitionIfNeed(com.android.server.wm.Transition transition, java.lang.String reason) {
    }

    default boolean isSkipMoveTaskToBack() {
        return false;
    }

    default void setSkipMoveTaskToBack(boolean skipMoveTaskToBack) {
    }

    default boolean isDoingFlexibleDragShare() {
        return false;
    }

    default void setDoingFlexibleDragShare(boolean isDoingFlexibleDragShare) {
    }

    default boolean isFlexibleSmartInnerTaskJump() {
        return false;
    }

    default void moveTaskToBackExt(com.android.server.wm.Task task, boolean cancelAnim) {
    }

    default boolean getNeedCancelTaskBackAnimationStatus() {
        return false;
    }

    default boolean isFlexibleTaskAndHasCaption(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isSkipControllingOccluding(com.android.server.wm.Task task) {
        return false;
    }

    default java.lang.Object getRememberPosInfo() {
        return null;
    }

    default void setRememberPosInfo(android.graphics.Rect bounds, float scale) {
    }

    default void setNeedSkipSwitchState(boolean isNeed) {
    }

    default boolean getNeedSkipSwitchState() {
        return false;
    }

    default boolean isStartingWindowAnimatingExit() {
        return false;
    }

    default void setPid(int pid) {
    }

    default int getPid() {
        return -1;
    }

    default void setTranslucentSplitTask(boolean translucentSplitTask) {
    }

    default boolean isTranslucentSplitTask() {
        return false;
    }

    default void setForceAdjustWindowFrame(boolean force) {
    }

    default boolean isForceAdjustWindowFrame() {
        return false;
    }

    default void setSplashScreenViewCopy(boolean viewCopy) {
    }

    default void forceRemoveSplashScreenViewCopyIfNeed(com.android.server.wm.Task task) {
    }

    default boolean isRootTaskBeforeBootComplete() {
        return false;
    }

    default boolean isEmptyTask() {
        return false;
    }

    default void pipToFullScreen(com.android.server.wm.Task originTask, com.android.server.wm.ActivityRecord topActivity) {
    }
}
