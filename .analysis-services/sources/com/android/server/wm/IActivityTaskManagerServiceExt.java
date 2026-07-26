package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityTaskManagerServiceExt {
    default com.android.server.wm.ActivityTaskManagerService getActivityTaskManagerService(android.content.Context context) {
        return null;
    }

    default void hookInitOplusATMSEnhance(com.android.server.wm.ActivityTaskManagerService mAtms) {
    }

    default void onSystemReady() {
    }

    default void systemReady() {
    }

    default void onOplusStart() {
    }

    default void init(android.content.Context context) {
    }

    default void publish() {
    }

    default void clearCacheWhenOnConfigurationChange(android.content.res.Configuration configuration, int changes) {
    }

    default void onConfigurationChanged(android.content.res.Configuration configuration) {
    }

    default void checkGoToSleep(com.android.server.wm.ActivityRecord activity, int userId) {
    }

    default void clearSnapshotCacheForPackage(java.lang.String packageName) {
    }

    default boolean ismOplusActivityControlerSchedulerexist() {
        return false;
    }

    default boolean scheduleAppCrash(java.lang.String processName, int pid, java.lang.String shortMsg, java.lang.String longMsg, long timeMillis, java.lang.String stackTrace) throws android.os.RemoteException {
        return false;
    }

    default void exitRunningScheduler() {
    }

    default void updateExtraConfigurationForUser(android.content.Context context, android.content.res.Configuration target, int userId) {
    }

    default void updateGlobalConfigurationEnd(android.content.res.Configuration values, int userId, android.content.Context context, int changes) {
    }

    default void initBurmeseConfigForUser(android.content.ContentResolver resolver, android.content.res.Configuration configuration) {
    }

    default void handleUiModeChanged(int changes) {
    }

    default void handleExtraConfigurationChanges(int changes, android.content.res.Configuration configuration, android.content.Context context, android.os.Handler handler, int userId) {
    }

    default void updateUserIdInExtraConfiguration(android.content.res.Configuration target, int userId) {
    }

    default boolean shouldAbortMoveTaskToFront(com.android.server.wm.Task task, android.app.ActivityOptions realOptions) {
        return false;
    }

    default boolean checkSetTaskWindowingMode(com.android.server.wm.Task task) {
        return false;
    }

    default void handleForcedResizableTaskIfNeeded(com.android.server.wm.Task task) {
    }

    default void hookAtmsConfigurationChang(int changes, com.android.server.wm.RootWindowContainer mRootWindowContainer, com.android.server.wm.WindowManagerService mWindowManager, android.content.res.Configuration newConfig) {
    }

    default void sendApplicationFocusGain(android.os.Handler handler, android.content.Context context, java.lang.String packageName) {
    }

    default void notifySysActivityHotLaunch(java.lang.Class clazz, com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
    }

    default void tryRemoveAllUserRecentTasksLocked() {
    }

    default void setBootstage() {
    }

    default int startZoomWindow(android.content.Intent intent, android.os.Bundle options, int userId, java.lang.String callPkg) {
        return -1;
    }

    default void updataeAccidentPreventionState(android.content.Context context, boolean enable, int newRotation, int oldRotation) {
    }

    default boolean interceptHandleAppDied(com.android.server.wm.WindowProcessController wpc, boolean restarting, boolean hasVisibleActivities) {
        return false;
    }

    default void setScreenOffPlay(boolean offPlay) {
    }

    default void handleCompatibilityException(int changeId, java.lang.String packageName) {
    }

    default boolean hookAtmssendPutConfigurationForUserMsg(android.content.ContentResolver resolver, int userId, android.content.res.Configuration configuration) {
        return false;
    }

    default void adjustConfigurationForUser(android.content.ContentResolver resolver, android.content.res.Configuration configuration, int userId) {
    }

    default void putTaskStackListenerDescriptor(android.app.ITaskStackListener listener, java.lang.String descriptor) {
    }

    default void removeTaskStackListenerDescriptor(android.app.ITaskStackListener listener) {
    }

    default java.lang.String getTaskStackListenerDescriptor(android.app.ITaskStackListener listener) {
        return null;
    }

    default void applySleepTokens(boolean wasSleeping) {
    }

    default void updateSleepTokens(boolean wasSleeping, boolean shouldSleep) {
    }

    default void moveTaskToDefaultDisplaySplitScreenSPrimaryTask(com.android.server.wm.Task task, boolean toTop, com.android.server.wm.ActivityTaskManagerService mAtms) {
    }

    default void moveTaskToDefaultDisplaySplitScreenSecondaryTask(com.android.server.wm.Task task, boolean toTop, com.android.server.wm.ActivityTaskManagerService mAtms) {
    }

    default void setProcRaiseAdjList(java.lang.Object pr) {
    }

    default void preBindApplication(com.android.server.wm.WindowProcessController wps, android.content.res.Configuration outOverrideConfig, android.os.Bundle bundle) {
    }

    default boolean interceptEnterPictureInPictureMode(com.android.server.wm.ActivityRecord r, android.app.PictureInPictureParams params) {
        return false;
    }

    default void clearCompactWindowModeWhenUpdateConfiguration(android.content.res.Configuration newConfig, android.content.res.Configuration oldConfig) {
    }

    default void onPreBindApplication(com.android.server.wm.WindowProcessController wpc) {
    }

    default boolean setAgingTestLockScreenShown(boolean keyguardShowing) {
        return keyguardShowing;
    }

    default boolean interceptOnForceStopPackage(java.lang.String pkgName, int userId) {
        return false;
    }

    default void hookRecordAppDiedCount(int uid, java.lang.String packageName, java.lang.String name) {
    }

    default void updateConfigForLauncherLocked(com.android.server.wm.ActivityRecord starting, int changes) {
    }

    default void onPackageUninstalled(java.lang.String packageName) {
    }

    default void onPackageAdded(java.lang.String packageName) {
    }

    default void onProcessUnMapped(com.android.server.wm.WindowProcessController app) {
    }

    default boolean checkOplusWindowPermission() {
        return false;
    }

    default boolean startPairTaskIfNeed(android.content.Intent[] intents, android.os.Bundle options, int userId) {
        return false;
    }

    default void updateConfigForPocketStudio(android.content.res.Configuration configuration) {
    }

    default boolean inSplitRootTask(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default void taskFocusChanged(com.android.server.wm.Task prevTask, com.android.server.wm.Task currentTask, com.android.server.wm.ActivityRecord lastResumedActivity, java.lang.String reason) {
    }

    default void pidChanged(com.android.server.wm.Task prevTask, com.android.server.wm.Task currentTask, com.android.server.wm.ActivityRecord lastResumedActivity, com.android.server.wm.ActivityRecord curResumedActivity, java.lang.String reason) {
    }

    default com.android.server.wm.IRemoteTaskHandlerManagerExt getRemoteTaskManager() {
        return null;
    }

    default void updateOomAdjForSleep(java.lang.Runnable runnable) {
    }

    default boolean withNoneTransition(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task tr, android.app.ActivityOptions options, int transitToBack, java.lang.String reason) {
        return false;
    }

    default boolean shouldSkipSetFocusedTaskForFlexibleWindow(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isIOPreloadPkg(java.lang.String pkgName, int userId) {
        return false;
    }

    default boolean isFromViewExtract(boolean defaultValue, android.os.Bundle bundle) {
        return defaultValue;
    }

    default boolean isInSkipRelaunchAppListWhenKeyBoardPlug(com.android.server.wm.ActivityTaskManagerService atms, int changes) {
        return false;
    }

    default boolean shouldDisableSnapshotsWithOrientation(com.android.server.wm.Task task) {
        return false;
    }

    default void notifyStartActivity(android.content.Intent intent, java.lang.String callingPackage) {
    }

    default void notifyStartActivity(android.content.Intent intent, java.lang.String callingPackage, com.android.server.wm.ActivityTaskManagerService atms, com.android.server.wm.SafeActivityOptions options) {
    }

    default void notifyStartActivityAsPackage(com.android.server.wm.SafeActivityOptions options, com.android.server.wm.ActivityTaskManagerService atms) {
    }

    default void notifyStartActivityInPackage(com.android.server.wm.SafeActivityOptions options, int callingUid, com.android.server.wm.ActivityTaskManagerService atms) {
    }

    default void beforeDeferLayout(java.lang.String callStack) {
    }

    default void afterContinueLayout(java.lang.String callStack) {
    }

    default boolean logDiffer() {
        return false;
    }

    default java.util.ArrayList<java.lang.String> getTimeoutDeferStacks() {
        return null;
    }

    default boolean isLogToolRun() {
        return false;
    }

    default java.lang.StringBuilder beginLogProcessConfigurationWhenFolding(android.content.res.Configuration configuration) {
        return new java.lang.StringBuilder();
    }

    default void logProcessConfigurationWhenFolding(java.lang.StringBuilder logStringBuilder, java.lang.String appName) {
    }

    default void endLogProcessConfigurationWhenFolding(java.lang.String tag, java.lang.StringBuilder logStringBuilder) {
    }

    default boolean isFrozenByHans(java.lang.String pkgName, int uid) {
        return false;
    }

    default void setUxForStartProcessAsync() {
    }

    default void setLastResumedActivity(com.android.server.wm.ActivityRecord r) {
    }
}
