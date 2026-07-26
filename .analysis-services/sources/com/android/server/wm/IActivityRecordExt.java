package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityRecordExt {
    public static final int REASON_ADD_STARTING_WINDOW_ABORT = 8192;
    public static final int REASON_ANIMATION_CANCELED = 5888;
    public static final int REASON_ANIM_SCENE_TRANSITION = 1280;
    public static final int REASON_BACKGROUND_PUTT_TASK = 1536;
    public static final int REASON_CREATE_SURFACE_FAIL = 6400;
    public static final int REASON_DO_SHOW = 768;
    public static final int REASON_FLEXIBLE_MINIMIZE = 8960;
    public static final int REASON_ILLEGAL_ARGUMENT = 8704;
    public static final int REASON_KEYGUARD_GOING_AWAY_NO_ANIMATION = 5120;
    public static final int REASON_MAIN_WINDOW_HAS_DRAWN = 4096;
    public static final int REASON_NOT_ALLOW_SNAPSHOT = 4608;
    public static final int REASON_NOT_OK_TO_DISPLAY = 2048;
    public static final int REASON_NO_SNAPSHOT = 5632;
    public static final int REASON_RESOLVED_THEME_FAIL = 5376;
    public static final int REASON_REVISE_FLAG = 1792;
    public static final int REASON_SHOW_PREVIEW_OFF = 256;
    public static final int REASON_STARTING_WINDOW_ALREADY_SHOWN = 2304;
    public static final int REASON_SURFACE_ALREADY_CREATED = 6144;
    public static final int REASON_TASK_BEHIND = 512;
    public static final int REASON_TASK_DESTROYED = 4352;
    public static final int REASON_TASK_OVERLAY = 1024;
    public static final int REASON_TYPE_NO_STARTING_WINDOW = 8448;
    public static final int REASON_ZOOM_MODE = 4864;
    public static final int SUB_REASON_CREATE_SURFACE_EXCEPTION = 6401;
    public static final int SUB_REASON_CREATE_SURFACE_NULL = 6402;
    public static final int SUB_REASON_ENCRYPTED_ACTIVITY = 4609;
    public static final int SUB_REASON_REVISE_FORCE_CLEAR = 1793;
    public static final int SUB_REASON_SNAPSHOT_ACTIVITY_BLACK = 4611;
    public static final int SUB_REASON_SNAPSHOT_PACKAGE_BLACK = 4610;
    public static final int SUB_REASON_THEME_DISABLE = 8;
    public static final int SUB_REASON_THEME_FLOATING = 2;
    public static final int SUB_REASON_THEME_SHOW_WALLPAPER = 4;
    public static final int SUB_REASON_THEME_TRANSLUCENT = 1;
    public static final int SUB_REASON_TYPE_NO_STARTING_WINDOW_BY_COMPAT = 4;
    public static final int SUB_REASON_TYPE_NO_STARTING_WINDOW_BY_FLAG = 1;
    public static final int SUB_REASON_TYPE_NO_STARTING_WINDOW_BY_ORIGIN = 2;

    default void notifyAddStartingWindowFail(int reason) {
    }

    default int getStartingWindowType(boolean newTask, boolean taskSwitch, boolean processRunning, boolean allowTaskSnapshot, boolean activityCreated, boolean activityAllDrawn, android.window.TaskSnapshot snapshot) {
        return -1;
    }

    default void setStaringWindowStyle(boolean translucent, boolean floating, boolean showWallpaper, boolean disableStarting, int launchType) {
    }

    default boolean getRootLockActivity() {
        return false;
    }

    default boolean getNotifyHotStart() {
        return false;
    }

    default void setRootLockActivity(boolean rootLockActivity) {
    }

    default void setNotifyHotStart(boolean notifyHotStart) {
    }

    default void topResumedActivityChanged(com.android.server.wm.ActivityRecord record, boolean onTop, com.android.server.wm.WindowProcessController app) {
    }

    default void finishIfPossible(com.android.server.wm.ActivityRecord record, java.lang.String reason, boolean endTask, com.android.server.wm.Transition newTransition) {
    }

    default void onAnimationFinished(com.android.server.wm.ActivityRecord record) {
    }

    default void onWindowsVisible(com.android.server.wm.ActivityRecord record) {
    }

    default boolean isZoomMode(int mode) {
        return false;
    }

    default boolean isFlexibleZoomWindow(int mode) {
        return false;
    }

    default boolean getMaxBoundsForZoomWindow() {
        return false;
    }

    default void setMaxBoundsForZoomWindow(boolean setMaxBounds) {
    }

    default boolean canSupportSnapshot(com.android.server.wm.ActivityRecord ar, android.window.TaskSnapshot snapshot, boolean activityCreated) {
        return false;
    }

    default boolean isMirageWindowDisplayId(int id) {
        return false;
    }

    default boolean shouldCreateCompatDisplayInsetsForMirageWindow(int id) {
        return false;
    }

    default boolean shouldClearCompat(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default boolean shouldBeVisible(boolean flags, int displayId) {
        return false;
    }

    default void notifySysActivityHotLaunch(java.lang.Class clazz, android.content.ComponentName componentInfo) {
    }

    default boolean shouldUseAppThemeSnapshot(com.android.server.wm.WindowState windowState, boolean isSecure) {
        return false;
    }

    default void setSnapshotStarting(boolean value) {
    }

    default boolean isSnapshotStarting() {
        return false;
    }

    default boolean hasOtherTopActivityOccludesKeyguard(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldBlockOrientationDuringFixedRotation() {
        return false;
    }

    default void setBlockOrientationDuringFixedRotation(boolean blockOrientationDuringFixedRotation) {
    }

    default void setSkipAppTransitionWhenStarting(boolean skipAppTransition) {
    }

    default void enableWaitDrawnForCameraIfNeed() {
    }

    default boolean shouldSkipAppTransitionWhenStarting() {
        return false;
    }

    default boolean shouldSkipAppTransition(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default boolean isSupprotBracketMode(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default void setSupprotBracketMode(com.android.server.wm.ActivityRecord activityRecord, boolean value) {
    }

    default boolean getApplyNewOrientationWhenReuse() {
        return false;
    }

    default void setApplyNewOrientationWhenReuse(boolean value) {
    }

    default boolean getShouldFixConfigOrientation() {
        return false;
    }

    default void setShouldFixConfigOrientation(boolean value) {
    }

    default boolean getShouldUseOriginOrientationConfig() {
        return false;
    }

    default void setShouldUseOriginOrientationConfig(boolean value) {
    }

    default int getFixedScreenOrientation() {
        return -1;
    }

    default void setFixedScreenOrientation(int value) {
    }

    default int getAdjustSizeType() {
        return -1;
    }

    default void setAdjustSizeType(int value) {
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

    default int getCameraDisplayMode() {
        return -1;
    }

    default void setCameraDisplayMode(int value) {
    }

    default float getFixedMinAspectRatio() {
        return -1.0f;
    }

    default void setFixedMinAspectRatio(float value) {
    }

    default float getFixedMaxAspectRatio() {
        return -1.0f;
    }

    default void setFixedMaxAspectRatio(float value) {
    }

    default int getRelaunchConfig() {
        return -1;
    }

    default void setRelaunchConfig(int value) {
    }

    default int getForceLetterBox() {
        return -1;
    }

    default void setForceLetterBox(int value) {
    }

    default void updateCompactFullScreenWindow(com.android.server.wm.ActivityRecord record, com.android.internal.policy.AttributeCache.Entry ent, int realTheme) {
    }

    default int handleStartingWindowForCompactWindow(com.android.server.wm.ActivityRecord record, android.window.TaskSnapshot snapshot, int type) {
        return type;
    }

    default boolean dontAllowsetOccludesParent(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default void handleActivityReparent(com.android.server.wm.ActivityRecord record, com.android.server.wm.Task task) {
    }

    default void onActivityFinish(com.android.server.wm.ActivityRecord record, java.lang.String reason) {
    }

    default boolean isCompactWindowingMode(int windowingMode) {
        return false;
    }

    default void startCompactMask(com.android.server.wm.Task task) {
    }

    default boolean shouldBlockPrepareActivityHideTransitionAnimation(com.android.server.wm.ActivityRecord record, boolean mVisibleRequested) {
        return false;
    }

    default void finishActivity(com.android.server.wm.ActivityRecord record, java.lang.String reason, boolean isresume) {
    }

    default void onShowAllWindowsOfActivity(com.android.server.wm.Task task) {
    }

    default int adujstLayerIfneeded(int layer, com.android.server.wm.ActivityRecord record) {
        return layer;
    }

    default void onCompactWindowAnimationFinished(com.android.server.wm.ActivityRecord record) {
    }

    default boolean shouldExitFixedRotation(com.android.server.wm.DisplayContent mDisplayContent, com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default boolean blockActivityRecordRequestOrientation(com.android.server.wm.ActivityRecord record, int requestedOrientation) {
        return false;
    }

    default boolean dontApplyAspectRatio(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default void setupAppFrameForCompatMode(android.graphics.Rect outFrame, android.graphics.Rect bounds, com.android.server.wm.ActivityRecord record) {
    }

    default boolean performClearTaskLocked(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord boundaryActivity) {
        return true;
    }

    default boolean isFontPageKilled(com.android.server.wm.Task removeHistoryRecordsForApp, com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default void pauseCompactResumedActivity(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord record) {
    }

    default void notifyActivityPaused(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord record) {
    }

    default boolean shouldReviseScreenOrientationForApp(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default void resolveAppOrientationIfNeed(com.android.server.wm.ActivityRecord app, android.content.res.Configuration resolvedConfig, int currentOrientation, android.content.res.Configuration newParentConfig) {
    }

    default float getFixedAspectRatioForActivity(com.android.server.wm.ActivityRecord activityRecord, boolean maxRatio) {
        return -1.0f;
    }

    default void onActivityRecordParentChanged(com.android.server.wm.ConfigurationContainer oldParent, com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.ActivityRecord record) {
    }

    default void onActivityRecordCreated(com.android.server.wm.ActivityRecord record) {
    }

    default void notifyActivityRecordVisible(com.android.server.wm.ActivityRecord record, boolean visible) {
    }

    default boolean setOrientation(com.android.server.wm.ActivityRecord sourceRecord, int requestedOrientation, int orientation) {
        return false;
    }

    default void adjustBracketMode(android.content.res.Configuration newParentConfiguration, android.graphics.Rect parentAppBounds, android.graphics.Rect parentBounds, android.graphics.Rect mTmpBounds, com.android.server.wm.ActivityRecord record) {
    }

    default boolean shouldForceRelaunch(int changes, android.util.MergedConfiguration lastReportedConfiguration, android.content.res.Configuration newMergedOverrideConfig, android.content.res.Configuration changesConfig, boolean displayChanged) {
        return false;
    }

    default boolean adjustActivityWidth(com.android.server.wm.ActivityRecord activityRecord, boolean originValue) {
        return originValue;
    }

    default void onActivityDestroyed(com.android.server.wm.ActivityRecord record) {
    }

    default boolean supportsSplitScreenByVendorPolicy(com.android.server.wm.ActivityRecord r, boolean candidate) {
        return candidate;
    }

    default void updateAllTopApps() {
    }

    default void setAnimationLayer(int layer) {
    }

    default int getAnimationLayer() {
        return 0;
    }

    default void setStateForVisible(com.android.server.wm.ActivityRecord.State preState, com.android.server.wm.ActivityRecord.State nextState, int uid, java.lang.String pkg, android.content.pm.ApplicationInfo appInfo, java.lang.String activityRecordHash) {
    }

    default void makeActiveIfNeeded(int uid, java.lang.String pkg, android.content.pm.ApplicationInfo appInfo) {
    }

    default void onDisplayChanged() {
    }

    default boolean adjustPreserveWindowWhenRelaunch(boolean preserveWindow, int changes, android.content.res.Configuration newMergedOverrideConfig) {
        return preserveWindow;
    }

    default boolean skipPrepareAppTransitionForMirageIfNeed(com.android.server.wm.Task task, int displayContentId, java.lang.String reason) {
        return false;
    }

    default boolean isSettingTaskFragment(com.android.server.wm.TaskFragment tf) {
        return false;
    }

    default void addFlagsOfIntentFromSettingTaskFragment(com.android.server.wm.ActivityRecord sourceRecord, android.content.Intent intent, java.lang.String packageName, com.android.internal.policy.AttributeCache.Entry ent, int theme) {
    }

    default boolean isNotTransferForEmbeded(com.android.server.wm.ActivityRecord fromActivity, com.android.server.wm.ActivityRecord targetActivity) {
        return false;
    }

    default boolean forceRelaunchByNavBarHide() {
        return false;
    }

    default void setForceRelaunchByNavBarHide(boolean shouldRelaunch) {
    }

    default boolean isUpdateFromNavbarHide(android.content.res.Configuration lastConfig, android.content.res.Configuration currentConfig, int height, java.lang.String packageName) {
        return false;
    }

    default java.lang.String getRootLockPkgName() {
        return null;
    }

    default boolean hasSplashWindowFlag() {
        return false;
    }

    default boolean hasPreloadBitmap(com.android.server.wm.ActivityRecord record) {
        return true;
    }

    default boolean notIgnoreWindowDisableStarting(com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default void reviseWindowFlagsForStarting(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.ActivityRecord sourceRecord, boolean newTask, boolean taskSwitch, boolean processRunning, boolean fromRecents, com.android.server.wm.ActivityRecord.State state) {
    }

    default boolean allowUseSnapshot(com.android.server.wm.ActivityRecord record, boolean newTask, boolean taskSwitch, boolean processRunning, boolean activityCreated) {
        return false;
    }

    default int getStartingWindowType(int defaultTypeNone, int defaultTypeSplash, int defaultTypeSnapshot) {
        return -1;
    }

    default boolean clearStartingWindowWhenSnapshotDiffOrientation(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default boolean interceptRemoveStartingWindow(com.android.server.wm.ActivityRecord activity, android.os.Handler handler, com.android.server.wm.StartingSurfaceController.StartingSurface surface, boolean isSnapshot) {
        return false;
    }

    default boolean shouldBlockTransferAnimation(com.android.server.wm.ActivityRecord fromActivity, com.android.server.wm.AnimatingActivityRegistry taskRegistry) {
        return false;
    }

    default void transferPreloadedInfoIfNeed(com.android.server.wm.ActivityRecord fromActivity, com.android.server.wm.ActivityRecord nowActivity) {
    }

    default boolean isWindowSurfaceSaved(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean skipAddStartingWindow() {
        return false;
    }

    default boolean shouldSpeedUnLock(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.AppTransition appTransition) {
        return false;
    }

    default boolean handleDestroySurfaces(java.lang.String packageName, int type) {
        return false;
    }

    default boolean shouldWindowSurfaceSaved(com.android.server.wm.WindowState win, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean skipRemoveOnActivityStopped() {
        return false;
    }

    default void addVisibleWindow(int curUid, java.lang.String curPkgName, android.content.pm.ApplicationInfo appInfo) {
    }

    default void removeUnVisibleWindow(int curUid, java.lang.String curPkgName) {
    }

    default float getMaxAspectRatio(android.content.pm.ActivityInfo info, android.graphics.Rect containingAppBounds) {
        return info.getMaxAspectRatio();
    }

    default int calculateNewChanges(int changes, android.content.res.Configuration configuration, android.window.SizeConfigurationBuckets buckets) {
        return -1;
    }

    default void notifyLaunchTime(android.content.pm.ApplicationInfo appInfo, java.lang.String className, long launchTime) {
    }

    default android.view.RemoteAnimationDefinition getRemoteAnimationDefinition(android.view.RemoteAnimationDefinition definitionDefault) {
        return definitionDefault;
    }

    default android.view.RemoteAnimationTarget obtainLaunchViewInfoIfNeeded(com.android.server.wm.ActivityRecord activityRecord, android.view.RemoteAnimationTarget defaultTarget) {
        return defaultTarget;
    }

    default void resolveScreenOnFlag(com.android.server.wm.ActivityRecord record, boolean turnScreenOn) {
    }

    default boolean isLogToolRun() {
        return false;
    }

    default android.graphics.Insets hookResolveTmpOverridesInsets(com.android.server.wm.TaskFragment.ConfigOverrideHint hint, com.android.server.wm.ActivityRecord record, android.content.res.Configuration parentConfig, com.android.server.wm.DisplayPolicy.DecorInsets.Info decorInsets, android.graphics.Insets insets) {
        return insets;
    }

    default void onPreActivityRecordConfigurationChanged(android.content.res.Configuration newParentConfig) {
    }

    default boolean inOplusCompatMode() {
        return false;
    }

    default void calculateOplusCompatBoundsOffset(int[] outOffset, com.android.server.wm.ActivityRecord record, android.content.res.Configuration config, android.content.ComponentName componentName, android.graphics.Rect parentAppBounds, android.graphics.Rect resolvedBounds, int configOrientation) {
    }

    default void applyOplusCompatAspectRatioIfNeed(android.content.res.Configuration resolvedConfig, android.content.res.Configuration newParentConfiguration) {
    }

    default float getCompatScaleInOplusCompatMode() {
        return 1.0f;
    }

    default boolean hasSizeCompatBoundsInOplusCompatMode() {
        return false;
    }

    default android.graphics.Rect getSizeCompatBoundsInOplusCompatMode() {
        return null;
    }

    default boolean shouldCreateCompatDisplayInsetsForSquare(com.android.server.wm.ActivityRecord record) {
        return true;
    }

    default void adjustAppCutoutInCompactWindow(com.android.server.wm.ActivityRecord activity, android.graphics.Rect parentAppBounds, android.content.res.Configuration resolvedConfig) {
    }

    default boolean inOplusActivityCompatMode() {
        return false;
    }

    default boolean inOplusCompatEnabled() {
        return false;
    }

    default boolean shouldClearSizeCompatMode(android.content.res.Configuration newParentConfiguration) {
        return false;
    }

    default boolean shouldSizeCompatVerticalCenter() {
        return true;
    }

    default void setLaunchedFromMultiSearch(boolean fromMultiSearch) {
    }

    default boolean getLaunchedFromMultiSearch() {
        return false;
    }

    default int toMultiSearchActivityTypeIfNeed(android.content.pm.ActivityInfo info, android.content.pm.IPackageManager pm, int type) {
        return type;
    }

    default boolean isResizeableForMultiSearch(com.android.server.wm.Task task) {
        return false;
    }

    default boolean shouldDelayRemovalInCompleteFinishing(com.android.server.wm.ActivityRecord next) {
        return false;
    }

    default boolean isActivityPreloadDisplay(int displayId, com.android.server.wm.DisplayContent display) {
        return false;
    }

    default void activityPreloadHandleDisplayChanged(android.content.res.Configuration globalConfig, int lastReportedDisplayId) {
    }

    default void activityPreloadAbort(com.android.server.wm.ActivityRecord activityRecord, java.lang.String reason) {
    }

    default void collectAppRequestFinishAr(com.android.server.wm.ActivityRecord activityRecord, java.lang.String reason) {
    }

    default void setAppBoundsIfNeed(com.android.server.wm.ActivityRecord record, android.content.res.Configuration resolvedConfig) {
    }

    default boolean ignoreOrientationRespectedWithInsets(com.android.server.wm.ActivityRecord activityRecord, boolean originOrientationRespectedWithInsets) {
        return originOrientationRespectedWithInsets;
    }

    default void setDisableFeatures(java.lang.String features) {
    }

    default java.lang.String getDisableFeatures() {
        return "";
    }

    default void resetUseTransferredAnimIfRequired(boolean visibleSetFromTransferredStartingWindow, boolean visible) {
    }

    default void resetLeashCropIfNeed(com.android.server.wm.ActivityRecord record, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl animationLeash) {
    }

    default boolean updateActvityState(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default void setLastParentBeforeSplitScreen() {
    }

    default void clearLastParentBeforeSplitScreen() {
    }

    default com.android.server.wm.Task getLastParentBeforeSplitScreen() {
        return null;
    }

    default void registerActivityMultiWindowAllowanceObserver(android.os.IBinder observer) {
    }

    default void unregisterActivityMultiWindowAllowanceObserver(android.os.IBinder observer) {
    }

    default void notifyActivityMultiWindowAllowanceChanged() {
    }

    default boolean hookShouldRelaunchLocked(int changes, int configChanged, android.content.res.Configuration changesConfig) {
        return ((~configChanged) & changes) != 0;
    }

    default com.android.server.wm.DisplayContent getLastReportedDisplay() {
        return null;
    }

    default void setLastReportedDisplay(com.android.server.wm.DisplayContent displayContent) {
    }

    default boolean forceRelaunchWhenActivityIdle(android.content.res.Configuration config) {
        return false;
    }

    default boolean shouldClearStartingPolicyVisibility(com.android.server.wm.ActivityRecord activityRecord) {
        return true;
    }

    default boolean setSimultaneousDisplayState(boolean state) {
        return false;
    }

    default void setLaunchDisplayId(int displayId) {
    }

    default int getLaunchDisplayId() {
        return 0;
    }

    default boolean isCompactRoot(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default boolean assignLayersIfNeed(com.android.server.wm.ActivityRecord record) {
        return true;
    }

    default void updateAllDrawnActivity(com.android.server.wm.ActivityRecord ar) {
    }

    default boolean isBackgroundPuttTask(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default boolean getCompactReparenting() {
        return false;
    }

    default void setCompactReparenting(boolean mCompactReparenting) {
    }

    default boolean isAnimationTarget() {
        return false;
    }

    default void setIsAnimationTarget(boolean isAnimationTarget) {
    }

    default int updateOrSaveResolvedThemeIfNeeded(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activity, boolean newTask, boolean taskSwitch, com.android.server.wm.ActivityRecord sourceRecord, android.app.ActivityOptions startOptions, boolean processRunning, boolean startActivity, int theme, int splashScreenTheme, int resolvedTheme) {
        return resolvedTheme;
    }

    default boolean isInRestoring(com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default int getFixRotationForSnapshot(com.android.server.wm.ActivityRecord activity, int rotation, android.window.TaskSnapshot taskSnapshot) {
        return rotation;
    }

    default boolean activityResumedLocked(com.android.server.wm.ActivityRecord record, boolean handleSplashScreenExit) {
        return false;
    }

    default void setLastIntentReceived(android.content.Intent intent) {
    }

    default android.content.Intent getLastIntentReceived() {
        return null;
    }

    default void hookPrepareSurfaces(boolean show) {
    }

    default void hookTransactionReadyShowSurfaces(android.view.SurfaceControl.Transaction t, boolean show) {
    }

    default boolean isZoomSplashExceptionList(java.lang.String packageName) {
        return false;
    }

    default boolean isActivityConfigOverrideDisable(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.WindowProcessController controller) {
        return false;
    }

    default int onActivityRecordOrientationInit(com.android.server.wm.ActivityRecord activityRecord, int orientation) {
        return orientation;
    }

    default boolean ignoreTimeOut(com.android.server.wm.ActivityRecord activityRecord, java.lang.String reason) {
        return false;
    }

    default boolean ignoreTimeOutForNonFinishing(com.android.server.wm.ActivityRecord activityRecord, java.lang.String reason) {
        return false;
    }

    default void adjustLastReportedConfiguration(android.content.res.Configuration processGlobalConfiguration, android.content.res.Configuration newMergedOverrideConfig, int changes, com.android.server.wm.ActivityRecord record, android.util.MergedConfiguration lastReportedConfiguration) {
    }

    default void setVisibleRequested(boolean visibleRequested, com.android.server.wm.TransitionController transitionController) {
    }

    default int hookRotationForPIPIfNeeded(int rotation, com.android.server.wm.DisplayContent display, com.android.server.wm.ActivityRecord activityRecord) {
        return rotation;
    }

    default void showInTransition(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowManagerService wmService, android.view.SurfaceControl sc) {
    }

    default com.android.server.wm.ActivityRecord.State changeStartActiveStateIfNeed(com.android.server.wm.ActivityRecord.State state) {
        return state;
    }

    default boolean notMakeActiveInCompactMode() {
        return false;
    }

    default void onActivityRecordParentChangedAfter(com.android.server.wm.ConfigurationContainer oldParent, com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.ActivityRecord record) {
    }

    default boolean shouldApplyAnimation(com.android.server.wm.ActivityRecord requestRecord, boolean visible) {
        return true;
    }

    default void setSourceRecordHint(com.android.server.wm.ActivityRecord hint) {
    }

    default int needChangeDiff(int changes, int lastDisplayId, int newDisplayId) {
        return changes;
    }

    default boolean shouldIgnoreOrientationRequests(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void setFlexibleActivityInfo(java.lang.Object info) {
    }

    default java.lang.Object getFlexibleActivityInfo() {
        return null;
    }

    default boolean isForceHidden() {
        return false;
    }

    default void resolveFlexibleActivityConfig(android.content.res.Configuration newParentConfiguration, android.graphics.Rect tmpBounds) {
    }

    default void calculateFlexibleOffset(int[] offset) {
    }

    default boolean shouldInterceptAddStartingWindowForFlexible(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean adjustOccludesParent(boolean originOccludesParent) {
        return originOccludesParent;
    }

    default void removeImmediately() {
    }

    default void onLeashAnimationStarting(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
    }

    default boolean isFlexibleSuitable() {
        return false;
    }

    default void updateActivitySpecificConfig(android.content.res.Configuration config) {
    }

    default android.content.res.Configuration getActivitySpecificConfig() {
        return null;
    }

    default boolean shouldAssociateStartingDataWithTask() {
        return false;
    }

    default boolean disableAssociateStartingDataWithTask(android.window.TaskSnapshot snapshot, com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean syncFinishedForOptimizeStartup(com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default boolean forceCreateRemoteAnimationTarget(com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default android.graphics.Rect calculateInsetsForAnimationTarget(com.android.server.wm.ActivityRecord activity, android.graphics.Rect defaultInsets) {
        return defaultInsets;
    }

    default boolean shouldMakeHomeActivityVisibleOnSecondary(com.android.server.wm.ActivityRecord r, com.android.server.wm.KeyguardController keyguardController) {
        return false;
    }

    default void interceptActivityOnSecondary(com.android.server.wm.ActivityRecord r, com.android.server.wm.KeyguardController keyguardController) {
    }

    default boolean shouldBeVisibleInSecondaryKeyguard() {
        return false;
    }

    default void setShouldBeVisibleInSecondaryKeyguard(boolean shouldBeVisibleInSecondaryKeyguard) {
    }

    default boolean autoResolutionEnable() {
        return false;
    }

    default java.lang.String getFinishReason() {
        return "";
    }

    default void updateActvityResumeTimeStamp(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void clearAccessControlPassPackages(com.android.server.wm.Task task, java.lang.String pkg, int userId, java.lang.String reason, com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void reviseMergedOverrideConfiguration(android.content.res.Configuration newMergedOverrideConfig, int newDisplayId) {
    }

    default void setTransitionForceAHead(boolean transitionForceAHead) {
    }

    default boolean getTransitionForceAHead() {
        return false;
    }

    default void setWindowAnimationTag(boolean tag) {
    }

    default boolean getWindowAnimationTag() {
        return false;
    }

    default boolean shouldSkipTransition(java.lang.String reason) {
        return false;
    }

    default void hookLifecyclePause(java.lang.String reason, java.lang.String shortComponentName, java.lang.String state) {
    }

    default boolean hasSurfaceView() {
        return false;
    }

    default boolean isAttatchSurfaceView() {
        return false;
    }

    default void updateRecordSurfaceViewState(boolean surfaceState, boolean attachedToWindow) {
    }

    default void updateStartingRecords(com.android.server.wm.ActivityRecord activity, boolean isAdd) {
    }

    default boolean shouldRemoveStartingWindowImmediately(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default void resetWaitForSyncTransactionCommitIfNeeded(com.android.server.wm.ActivityRecord toActivity, com.android.server.wm.ActivityRecord fromActivity, com.android.server.wm.StartingData startingData) {
    }

    default void resetWaitForSyncTransactionCommitIfNeeded(com.android.server.wm.ActivityRecord fromActivity) {
    }

    default boolean shouldSkipRemoveStartingWindow(com.android.server.wm.WindowState windowState, com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean taskOverlayStartingWindow(boolean newTask, boolean processRunning, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startRecord) {
        return false;
    }

    default boolean allowAddStartingWindow(com.android.server.wm.ActivityRecord topAttached, android.window.TaskSnapshot snapshot, int rotation) {
        return false;
    }

    default int getSplashscreenTheme(android.app.ActivityOptions options) {
        return 0;
    }

    default void postWindowRemoveStartingWindow(com.android.server.wm.StartingSurfaceController.StartingSurface startingSurface, com.android.server.wm.Task task, com.android.server.wm.ActivityRecord current) {
    }

    default boolean skipCheckKeyguardVisibility() {
        return false;
    }

    default boolean isParentChanged() {
        return false;
    }

    default void setParentChanged(boolean parentChanged) {
    }

    default void onWindowsDrawn(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default boolean shouldSplashDislay(boolean taskVisible, boolean visible, boolean startWindowDisplayed, com.android.server.wm.StartingData starting) {
        return false;
    }

    default boolean ignoreChangePlayingTransition(boolean hasColorMode) {
        return false;
    }

    default void updateActivityStateChanged(com.android.server.wm.ActivityRecord record, com.android.server.wm.TaskFragment tf, com.android.server.wm.ActivityRecord.State state, java.lang.String reason) {
    }

    default void hookSetBinderUxFlag(boolean applyToUx, com.android.server.wm.ActivityRecord activityRecord) {
    }

    default boolean ignoreOplusAppPlayingTransition(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean ignoreSnapShotRotation(android.window.TaskSnapshot snapshot, com.android.server.wm.ActivityRecord ar, com.android.server.wm.Task task) {
        return false;
    }

    default boolean isDisableshowWhenLockByRecents() {
        return false;
    }

    default boolean shouldDeferTaskAppear(com.android.server.wm.Task task) {
        return true;
    }

    default void setDisableshowWhenLockByRecents(boolean disableshowWhenLockByRecents) {
    }

    default void onActivityStateChanged(com.android.server.wm.ActivityRecord.State preState, com.android.server.wm.ActivityRecord.State state) {
    }

    default com.android.server.wm.ActivityRecord.State getPreState() {
        return null;
    }

    default boolean abortTransitionIfNeeded() {
        return false;
    }

    default void setAbortTransition(boolean abortTransition) {
    }

    default boolean canCreateTaskSnapShotSurface(int type, com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void addStartingWindow() {
    }

    default void removeStartingWindow() {
    }

    default void setFullOrientation(boolean fullOrientation) {
    }

    default boolean isFullOrientation() {
        return false;
    }

    default boolean isSupportFreeForm() {
        return false;
    }

    default void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
    }

    default boolean skipCollectWhenSetVisibleRequest(boolean isVisibleRequested) {
        return false;
    }

    default boolean deferCommitVisibilityIfNeed(java.lang.String reason) {
        return false;
    }

    default boolean isForceHideByRemoveTask() {
        return false;
    }

    default void forceHideByRemoveTask(boolean force) {
    }

    default boolean isLaunchedFromTaskBar() {
        return false;
    }

    default boolean isSupportIconAnim() {
        return false;
    }

    default void parseLaunchOptions(android.app.ActivityOptions options) {
    }

    default boolean shouldInterceptReturnOrientation(int candidate) {
        return false;
    }

    default int getFixedRotationForSplashScreen(com.android.server.wm.ActivityRecord activityRecord) {
        return -1;
    }

    default boolean attachExStartingSurface(com.android.server.wm.ActivityRecord ar, int type, android.window.TaskSnapshot taskSnapshot, boolean needCap, android.graphics.Bitmap bitmap, boolean needReplace) {
        return false;
    }

    default boolean hasStartingDataAndMainWindowIsDrawn(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default void notifyStartingWindowDrawn(com.android.server.wm.ActivityRecord ar) {
    }

    default boolean isShowStartingSurfaceLocked(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean useSnapshotIngoreRotationNotMatch() {
        return false;
    }

    default boolean addStartingSurfaceIngoreTaskOverlay(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean addStartingSurfaceForTaskWithPermissionsActivity(com.android.server.wm.ActivityRecord ar, android.window.TaskSnapshot snapshot) {
        return false;
    }

    default int getAppQuickStartingSufaceType() {
        return -1;
    }

    default void notifyFirstWindowDrawn(com.android.server.wm.ActivityRecord ar) {
    }

    default boolean makeActivityLanchFromLauncherOccludesParentIfNeed(com.android.server.wm.ActivityRecord ar, java.lang.String callingPkgName) {
        return false;
    }

    default boolean showingSnapShotStartingSurfaceLocked(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean ignoreShowStartingWindow(com.android.server.wm.ActivityRecord prev, boolean newTask, boolean taskSwitch, boolean processRunning, boolean startActivity, com.android.server.wm.ActivityRecord sourceRecord, android.app.ActivityOptions candidateOptions, com.android.server.wm.ActivityRecord.State state) {
        return false;
    }

    default boolean hideEmbeddedSurfaceBeforeReparent() {
        return false;
    }

    default boolean skipApplySizeOverride(android.content.res.Configuration config) {
        return false;
    }

    default boolean isFlexibleWindowTask() {
        return false;
    }

    default boolean resetDrawStateWhenSetAppVisible() {
        return false;
    }

    default void onActivityStopped(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void transferStartingWindow(com.android.server.wm.ActivityRecord from, com.android.server.wm.ActivityRecord to) {
    }

    default void printSyncState(com.android.server.wm.ActivityRecord a) {
    }

    default boolean isTransferAllowed(com.android.server.wm.ActivityRecord from, com.android.server.wm.ActivityRecord to) {
        return true;
    }

    default boolean loggingWhenFolding() {
        return false;
    }

    default void addDeviceFoldingFlagIfNeed(com.android.server.wm.ActivityRecord starting, android.content.res.Configuration values) {
    }

    default void hideStartingSurfaceIfNeeded(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default int getPrevStartingWindowType() {
        return -1;
    }

    default void setPrevStartingWindowType(int type) {
    }

    default void makeSurePrevStartingWindowType(com.android.server.wm.ActivityRecord ar, boolean newTask, boolean taskSwitch, boolean processRunning, boolean allowTaskSnapshot, boolean activityCreated, boolean activityAllDrawn) {
    }

    default boolean resetDrawStateIfNeed(com.android.server.wm.WindowState w, java.lang.String scene) {
        return true;
    }

    default void topResumedActivityChanged(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void pauseFlexibleResumedActivityIfNeeded(boolean scheduleIdle, boolean idleDelayed, com.android.server.wm.ActivityRecord resumed) {
    }

    default boolean isFrozenByHans(java.lang.String pkgName, int uid) {
        return false;
    }

    default void setSavingWindowSurface(com.android.server.wm.WindowState win, com.android.server.wm.DisplayContent displayContent) {
    }

    default boolean shouldSaveWindowSurface() {
        return false;
    }

    default void setForceOccludeParent(boolean forceOccludeTrue) {
    }

    default boolean isForceOccludeParent() {
        return false;
    }

    default boolean skipAddNoAnimationFlag(com.android.server.wm.ActivityRecord actR) {
        return false;
    }

    default void handleAppDied(com.android.server.wm.ActivityRecord record, com.android.server.wm.Transition newTransition) {
    }

    default boolean shouldAvoidDeferHidingClient(com.android.server.wm.ActivityRecord act) {
        return false;
    }
}
