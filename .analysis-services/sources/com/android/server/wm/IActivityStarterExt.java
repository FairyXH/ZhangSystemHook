package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityStarterExt {
    default boolean executeBeforeShutdownCheck(com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.ActivityStarter.Request request) {
        return false;
    }

    default boolean executeAfterShutdownCheck(com.android.server.wm.ActivityStarter.Request request) {
        return false;
    }

    default int executeRequestReplaceErrCheck(java.lang.String callingPackage, int callingUid, int callingPid, int err, android.content.Intent intent, int userId, android.content.pm.ActivityInfo aInfo, com.android.server.wm.SafeActivityOptions options, java.lang.String mLastStartReason, int realCallingUid, int realCallingPid) {
        return err;
    }

    default void executeRequestAfterErrSuccess(android.app.IApplicationThread caller, com.android.server.wm.WindowProcessController callerApp, java.lang.String callingPackage, int callingPid, int callingUid, android.content.Intent intent) {
    }

    default void executeRequestBeforeStartActivity(android.content.pm.ActivityInfo aInfo, int userId) {
    }

    default android.util.Pair<java.lang.Integer, android.util.Pair<android.content.Intent, android.content.pm.ActivityInfo>> getMultiAppActivityInfo(int userId, android.content.Intent intent, java.lang.String callingPackage, android.content.pm.ActivityInfo aInfo, int requestCode, int startFlags, com.android.server.wm.SafeActivityOptions options, java.lang.String resolvedType, com.android.server.wm.ActivityTaskSupervisor supervisor, int callingUid, int realCallingUid, int filterCallingUid) {
        return null;
    }

    default android.util.Pair<android.content.Intent, android.content.pm.ActivityInfo> checkStartActivityForAppLock(com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.ActivityRecord sourceRecord, android.content.pm.ActivityInfo aInfo, android.content.Intent intent, int requestCode, int realCallingUid, android.app.ActivityOptions options, android.app.ProfilerInfo pInfo, com.android.server.wm.Task inTask) {
        return null;
    }

    default android.util.Pair<android.util.Pair<android.content.Intent, android.content.pm.ActivityInfo>, java.lang.Boolean> checkStartActivity(com.android.server.wm.ActivityRecord sourceRecord, android.content.pm.ActivityInfo aInfo, android.content.Intent intent, int requestCode, int realCallingUid, java.lang.String callerPkg, android.app.ActivityOptions options, android.app.ProfilerInfo pInfo, com.android.server.wm.Task inTask, boolean abort) {
        return null;
    }

    default android.util.Pair<java.lang.Boolean, com.android.server.wm.Task> isAppUnlockPasswordActivity(com.android.server.wm.RootWindowContainer container, android.app.ActivityOptions options, android.app.ActivityOptions optionsLocal, boolean addtoTask, com.android.server.wm.ActivityRecord record, com.android.server.wm.ActivityRecord sourceRecord) {
        return null;
    }

    default com.android.server.wm.ActivityRecord isAppUnlockPasswordActivity(com.android.server.wm.ActivityRecord intentRecord, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.Task taskLocal) {
        return intentRecord;
    }

    default boolean hansActivityIfNeeded(int callingUid, java.lang.String callingPackage, com.android.server.wm.ActivityRecord info) {
        return false;
    }

    default void setHandleForcedResizableFlag(com.android.server.wm.ActivityRecord record, com.android.server.wm.Task task, com.android.server.wm.Task targetTask, com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict) {
    }

    default void handleNonResizableTask(com.android.server.wm.ActivityTaskSupervisor supervisor, com.android.server.wm.Task task, int preferredWindowingMode, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, com.android.server.wm.Task actualRootTask) {
    }

    default void preloadAppSplash(com.android.server.wm.ActivityStarter.Request request) {
    }

    default void shouldShowStartingwidnowWhenMoveToFront(com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.Task task, com.android.server.wm.ActivityRecord target) {
    }

    default int modifyCallingUidWhenRecentTask(com.android.server.wm.Task task, android.content.pm.ActivityInfo info, com.android.server.wm.ActivityTaskManagerService service, int callingUid, int realCallingUid, android.content.Intent intent) {
        return callingUid;
    }

    default boolean shouldClearReusedActivity(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord reusedActivity, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity) {
        return false;
    }

    default void hooksetUxThreadValue(int pid, int tid, java.lang.String value, android.os.IBinder resultTo) {
    }

    default void hookActivityBoost() {
    }

    default void boostLaunchActivity(com.android.server.wm.WindowProcessController homeProc, android.content.pm.ActivityInfo aInfo) {
    }

    default boolean onStartFromPrimaryScreen(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord record, com.android.server.wm.ActivityRecord startActivity, android.app.ActivityOptions options, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea) {
        return false;
    }

    default boolean onStartFromPrimaryScreen(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord record, com.android.server.wm.ActivityRecord startActivity, android.app.ActivityOptions options, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, android.content.Intent intent) {
        return false;
    }

    default void onStartFromPrimaryScreen(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord record) {
    }

    default boolean isAllowPkgEmbed(java.lang.String pkg) {
        return false;
    }

    default void notifySysActivityStart(java.lang.Class clazz, android.content.ComponentName componentInfo) {
    }

    default boolean interceptStartForMirageCarMode(android.content.Intent intent, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.Task reusedTask, android.app.ActivityOptions options, com.android.server.wm.ActivityStarter starter) {
        return false;
    }

    default boolean interceptActivityForAppShareModeIfNeed(boolean newTask, boolean isNewClearTask, com.android.server.wm.Task targetTask, com.android.server.wm.ActivityRecord mStartActivity, com.android.server.wm.RootWindowContainer mRootWindowContainer, com.android.server.wm.Task mSourceRootTask, com.android.server.wm.ActivityRecord sourceActivity) {
        return false;
    }

    default android.app.ActivityOptions modifyOptionsForCompactModeIfNeed(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord sourceRecord) {
        return options;
    }

    default boolean isAllowedToStartIncompactWindowingmode(com.android.server.wm.ActivityRecord mStartActivity, com.android.server.wm.Task targetTask) {
        return true;
    }

    default void setInitialState(com.android.server.wm.ActivityRecord r, android.app.ActivityOptions options, com.android.server.wm.Task inTask, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord mLastResumedActivity) {
    }

    default boolean isCompactWindowMode(int mPreferredWindowingMode) {
        return false;
    }

    default boolean isNeedFullScreenFromSettingTaskFragment(com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default void interceptInitiatorForAppDetails(java.lang.String callingPackage, android.content.Intent intent, int realCallingUid) {
    }

    default void handlerIntentForAppDetails(com.android.server.wm.ActivityTaskSupervisor supervisor, java.lang.String callingPackage, int callingUid, int userId, android.content.Intent intent, java.lang.String resolvedType) {
    }

    default boolean interceptStartForSplitScreenMode(android.content.Intent intent, com.android.server.wm.ActivityStarter.Request request, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startActivity, android.app.ActivityOptions options, com.android.server.wm.Task reusedTask, java.lang.Boolean newTask) {
        return false;
    }

    default void hookPostStartActivityProcessing(int result, com.android.server.wm.Task targetTask, com.android.server.wm.ActivityRecord r) {
    }

    default void launchIntoCompatMode(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.Task targetTask) {
    }

    default void hookAfterCheckBackgroundActivityStart() {
    }

    default void changeReusedTask(com.android.server.wm.Task reusedTask) {
    }

    default com.android.server.wm.Task changeReusedTaskForAppInner(com.android.server.wm.Task reusedTask, com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea) {
        return reusedTask;
    }

    default boolean acPreloadAbortBgActivityStart(com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.WindowProcessController callerApp) {
        return false;
    }

    default void activityPreloadHandleStartActivity(com.android.server.wm.ActivityRecord activity) {
    }

    default boolean startPreloadActivityWhilePreloading(com.android.server.wm.Task reusedTask, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord targetTaskTop, android.app.ActivityOptions options, java.lang.String callerPackage, java.lang.String reason) {
        return false;
    }

    default boolean interceptStartForActiveSplitScreen(android.content.Intent intent, com.android.server.wm.SafeActivityOptions options, java.lang.String callerPkg) {
        return false;
    }

    default android.app.ActivityOptions hookOptionsForSplit(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.Task targetTask, android.app.ActivityOptions options) {
        return options;
    }

    default void recycleTask(android.app.ActivityOptions options, com.android.server.wm.Task reusedTask) {
    }

    default boolean canClearActivityRecord(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default com.android.server.wm.TaskFragment modifyParentForEmbeddingSettingIfNeed(com.android.server.wm.ActivityRecord mStartActivity, com.android.server.wm.Task task, com.android.server.wm.TaskFragment newParent) {
        return newParent;
    }

    default boolean newTaskFlagDisable(com.android.server.wm.ActivityRecord startRecord, com.android.server.wm.ActivityRecord sourceRecord) {
        return false;
    }

    default boolean pullPuttTaskBack(com.android.server.wm.ActivityStarter starter, com.android.server.wm.ActivityRecord activity, com.android.server.wm.Task targetTask, android.app.ActivityOptions options, android.app.ActivityOptions option2, com.android.server.wm.ActivityRecord source) {
        return false;
    }

    default boolean notReparentForComapctWindow(com.android.server.wm.Task intentTask, com.android.server.wm.ActivityRecord intentActivity, com.android.server.wm.Task mTargetRootTask) {
        return false;
    }

    default android.app.ActivityOptions createOptionsForZoom(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startActivity, int realCallingPid) {
        return options;
    }

    default android.app.ActivityOptions adjustOptionsForZoom(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.Task targetTask, int realCallingPid) {
        return options;
    }

    default boolean isAllowedToStartActivityInZoom(com.android.server.wm.ActivityRecord r, boolean newTask, com.android.server.wm.Task targetTask) {
        return true;
    }

    default void updateTaskForZoom(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.Task targetTask, int realCallingPid, java.lang.String callPkg, com.android.server.wm.Task prevTopRootTask) {
    }

    default boolean isStartZoom(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default boolean isNeedAbortTransition(com.android.server.wm.ActivityRecord startedActivity, android.app.ActivityOptions options, com.android.server.wm.Task targetTask, com.android.server.wm.Transition transition) {
        return false;
    }

    default void updateFlexibleWindowTask(com.android.server.wm.Task targetTask, com.android.server.wm.Task reusedTask, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityRecord sourceRecord, int callingPid) {
    }

    default boolean interceptStartForBootReg(android.content.pm.ActivityInfo aInfo) {
        return false;
    }

    default boolean interceptStartActivityFromFlexibleWindow(com.android.server.wm.Task prevTopRootTask, com.android.server.wm.Task targetTask, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityStarter.Request request, com.android.server.wm.ActivityRecord sourceRecord) {
        return false;
    }

    default boolean interceptStartActivityInVisibleTask(com.android.server.wm.Task prevTopRootTask, com.android.server.wm.Task targetTask, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity) {
        return false;
    }

    default void exitFlexibleEmbeddedTask(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task, android.app.ActivityOptions options, int realCallingPid, boolean moveToBack) {
    }

    default boolean triggerMaskFromIntentIfNeed(com.android.server.wm.ActivityTaskSupervisor supervisor, android.content.Intent intent, com.android.server.wm.ActivityRecord sourceRecord, android.content.pm.ActivityInfo requestInfo, java.lang.String callingPackage, int callingPid) {
        return false;
    }

    default boolean replaceNewTaskIfNeed(com.android.server.wm.ActivityRecord mSourceRecord, com.android.server.wm.ActivityRecord mStartActivity) {
        return false;
    }

    default void triggerMaskFromIntentIfNeed(android.content.Intent intent, java.lang.String packageName, java.lang.String callingPackage) {
    }

    default void parseFlexibleActivityInfo(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord r) {
    }

    default boolean interceptWhenAnr(com.android.server.wm.ActivityTaskManagerService mService, com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default void setForceUpdateWindow(com.android.server.wm.Task targetTask, com.android.server.wm.ActivityRecord startActivity) {
    }

    default android.app.ActivityOptions adjustOptionsForFlexibleWindow(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord r) {
        return options;
    }

    default boolean canAddingToTaskFragment(com.android.server.wm.Task task, com.android.server.wm.TaskFragment tf, com.android.server.wm.ActivityRecord ar) {
        return true;
    }

    default boolean isAppUnlockActivityFromPocketStudio(com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord startActivity) {
        return false;
    }

    default boolean getScenarioTaskOrder(com.android.server.wm.Task task, com.android.server.wm.TaskDisplayArea taskDisplayArea, java.lang.String callingPackage, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord, boolean onTop) {
        return onTop;
    }

    default android.app.ActivityOptions handleRemoteTaskIfNeeded(com.android.server.wm.ActivityRecord r, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, com.android.server.wm.ActivityRecord mSourceRecord, com.android.server.wm.ActivityRecord sourceRecord, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord notTop, boolean newTask, com.android.server.wm.Task reusedTask, int launchFlags, int launchMode, android.content.Intent intent) {
        return options;
    }

    default boolean replaceActivityStartFromLab(com.android.server.wm.ActivityStarter.Request request) {
        return false;
    }

    default void forceCancelTransitionIfNeed(com.android.server.wm.ActivityRecord next, int nextTransit, boolean nextIsRemote) {
    }

    default void markFlexibleSubTaskIfForceStopNeeded(com.android.server.wm.ActivityRecord source, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.Task targetTask) {
    }

    default boolean getSubDifferentTopTask(com.android.server.wm.Task task, com.android.server.wm.TaskDisplayArea taskDisplayArea, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord) {
        return false;
    }

    default com.android.server.wm.ActivityRecord handleReuseActivityForSubDisplayIfNeed(com.android.server.wm.ActivityRecord intentActivity, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord target, com.android.server.wm.TaskDisplayArea preferredDisplay, boolean includeLaunchedFromBubble) {
        return intentActivity;
    }

    default void notifyNoneTransition(boolean withTransition, com.android.server.wm.Transition transition, com.android.server.wm.Task task, com.android.server.wm.TransitionController transitionController) {
    }

    default android.app.ActivityOptions adjustOptionsForAcrossEmbeddedTask(com.android.server.wm.Task targetTask, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord r, int realCallingPid, int realCallingUid) {
        return options;
    }

    default android.app.ActivityOptions adjustOptionsForFlexibleTask(com.android.server.wm.Task targetTask, com.android.server.wm.TaskDisplayArea taskDisplayArea, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.Task prevTopTask, com.android.server.wm.ActivityStarter.Request request) {
        return options;
    }

    default boolean isMirageDisplay(int displayId) {
        return false;
    }

    default boolean isRestoring(android.content.pm.ActivityInfo activityInfo, int userId) {
        return false;
    }

    default boolean checkIsStartToSplit(android.app.ActivityOptions options) {
        return false;
    }

    default boolean checkSendReady(com.android.server.wm.ActivityRecord startActivity, com.android.server.wm.ActivityRecord topActivity, int result, boolean avoidMoveToFront, int balCode) {
        return true;
    }

    default boolean interceptStartForAsyncRotation(com.android.server.wm.ActivityRecord activityRecord, android.content.Intent intent) {
        return false;
    }

    default android.app.ActivityOptions adjustOptionsForSplitScreen(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord r) {
        return options;
    }

    default boolean shouldAvoidMoveToFrontIfNeeded(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.Task targetTask) {
        return false;
    }

    default boolean checkLaunchInSameTaskBackground(com.android.server.wm.ActivityRecord source, com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default boolean skipMoveToFront(com.android.server.wm.ActivityRecord sourceAr, com.android.server.wm.ActivityRecord r, com.android.server.wm.Task targetTask) {
        return false;
    }

    default void notifyActivityLaunched(com.android.server.wm.ActivityTaskManagerService activityTaskManagerService, com.android.server.wm.ActivityTaskSupervisor activityTaskSupervisor, android.content.Intent intent) {
    }

    default void transferLaunchCookie(com.android.server.wm.ActivityRecord intentAr, com.android.server.wm.ActivityRecord startAr, com.android.server.wm.Task targetTask, int launchFlags) {
    }

    default boolean isLaunchingRootActivity(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task targetTask) {
        return false;
    }

    default void commitVisibilityAfterAbort(com.android.server.wm.ActivityRecord ar, com.android.server.wm.Transition transition) {
    }

    default void fixLaunchSourceType(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord targetTaskTop) {
    }

    default void shouldAbortChange(com.android.server.wm.Transition transition, com.android.server.wm.ActivityRecord ar, boolean withTransition, boolean isRemote) {
    }

    default void recordStartTransitionState(com.android.server.wm.Transition startTransition, com.android.server.wm.ActivityRecord startActivity, android.app.ActivityOptions options) {
    }

    default boolean shouldSetReady(com.android.server.wm.ActivityRecord startedActivity) {
        return false;
    }

    default void setTransientLaunchIfNeed(com.android.server.wm.ActivityRecord ar, com.android.server.wm.TransitionController controller) {
    }

    default void setStartRecentsReason(android.app.ActivityOptions options, com.android.server.wm.TransitionController controller) {
    }

    default void setTaskNotInRecent(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task inTask, java.lang.String reason) {
    }

    default void splitTaskForNotificationZoomReply(android.app.ActivityOptions options, android.content.Intent intent, java.lang.String callingPackage, int userId) {
    }

    default com.android.server.wm.Task handleReuseTaskForFlexibleTaskIfNeed(com.android.server.wm.Task reusedTask, com.android.server.wm.ActivityRecord startActivity, android.app.ActivityOptions options) {
        return reusedTask;
    }

    default android.app.ActivityOptions adjustOptionsForSmartMultiWindow(com.android.server.wm.ActivityRecord startActivity, android.app.ActivityOptions options, android.content.Intent intent, com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.Task targetTask) {
        return null;
    }

    default com.android.server.wm.Task handleTargetTaskForInnerTaskJump(com.android.server.wm.ActivityRecord topActivity, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, com.android.server.wm.Task originTask) {
        return null;
    }

    default void updateTransitionInfoBundleIfNeed(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task targetRootTask) {
    }

    default boolean isMultiAppExternalStorageReady(int userId) {
        return true;
    }

    default boolean interceptStartWhenOhideNoAllow(java.lang.String callingPackage, android.content.pm.ActivityInfo targetPackage, int userid) {
        return false;
    }

    default int adjustLaunchFlagsForFlexible(com.android.server.wm.ActivityRecord sourceRecord, android.app.ActivityOptions options, int launchFlags) {
        return launchFlags;
    }

    default boolean putIntoExistingTaskInResolveReusableTask(android.app.ActivityOptions options, com.android.server.wm.ActivityRecord startActivity) {
        return false;
    }

    default void updateIntentExtra(android.content.Intent intent, int callingUid) {
    }

    default boolean isActivityStartWithSpruceKey(android.app.ActivityOptions options) {
        return false;
    }

    default void notifyActivityStartForNewTask(android.content.Intent intent, java.lang.String callingPackage, int flags, com.android.server.wm.ActivityRecord sourceRecord, int launchMode, com.android.server.wm.Task inTask) {
    }

    default void updateLaunchCookies(com.android.server.wm.Task intentTask, android.os.IBinder launchCookie, com.android.server.wm.ActivityRecord sourceRecord) {
    }

    default boolean isInterceptBgActivityStart(com.android.server.wm.ActivityRecord r, android.content.pm.ActivityInfo aInfo, com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict, com.android.server.wm.ActivityRecord sourceRecord, android.content.Intent intent, int realCallingUid, int callingUid) {
        return false;
    }

    default void collapsePanelsForFlexibleWindow(android.app.ActivityOptions options, com.android.server.wm.ActivityTaskManagerService atmService, int realCallingUid, int startResult, com.android.server.wm.Task task, boolean checkTask) {
    }

    default void checkStartActivityToLabFromFlexible(com.android.server.wm.Task targetTask, android.app.ActivityOptions options) {
    }
}
