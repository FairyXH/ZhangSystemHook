package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusEmbeddingManagerService extends android.common.IOplusCommonFeature {
    public static final com.android.server.wm.IOplusEmbeddingManagerService DEFAULT = new com.android.server.wm.IOplusEmbeddingManagerService() { // from class: com.android.server.wm.IOplusEmbeddingManagerService.1
    };
    public static final java.lang.String NAME = "IOplusEmbeddingManagerService";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusEmbeddingManagerService;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void init(com.android.server.am.IOplusActivityManagerServiceEx amsEx, com.android.server.wm.IOplusActivityTaskManagerServiceEx atmEx) {
    }

    default boolean isFuncEnable() {
        return false;
    }

    default boolean isSettingTaskFragment(com.android.server.wm.TaskFragment tf) {
        return false;
    }

    default void addFlagsOfIntentFromSettingTaskFragment(com.android.server.wm.ActivityRecord sourceRecord, android.content.Intent intent, java.lang.String packageName, com.android.internal.policy.AttributeCache.Entry ent, int theme) {
    }

    default boolean isNeedFullScreenFromSettingTaskFragment(com.android.server.wm.ActivityRecord sourceRecord, com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default boolean isSettingDialog(com.android.server.wm.WindowState win) {
        return false;
    }

    default void resizeTouchRegionForSpecial(com.android.server.wm.ActivityRecord record, com.android.server.wm.WindowFrames frames, android.graphics.Region region, com.android.server.wm.WindowState windowState) {
    }

    default boolean isAllowPkgEmbed(java.lang.String pkg) {
        return false;
    }

    default boolean isNotTransferForEmbeded(com.android.server.wm.ActivityRecord fromActivity, com.android.server.wm.ActivityRecord targetActivity) {
        return false;
    }

    default boolean isActivityEmbedded(com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default boolean hookIsActivityEmbedded(boolean isEmbedded, com.android.server.wm.TaskFragment taskFragment, com.android.server.wm.ActivityRecord activity) {
        return isEmbedded;
    }

    default boolean canApplyDimInEmbedding(com.android.server.wm.WindowState win) {
        return true;
    }

    default boolean shouldSyncWithBuffersIfNeeded(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean isInPhoneGuide(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default void resetLeashCropIfNeed(com.android.server.wm.ActivityRecord record, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl animationLeash) {
    }

    default void cpuFrequencyBoostIfNeed(com.android.server.wm.ActivityRecord t) {
    }

    default com.android.server.wm.TaskFragment modifyParentForEmbeddingSettingIfNeed(com.android.server.wm.ActivityRecord mStartActivity, com.android.server.wm.Task task, com.android.server.wm.TaskFragment newParent) {
        return newParent;
    }

    default int reorderIndex(com.android.server.wm.Task task, com.android.server.wm.WindowContainer child, int index) {
        return index;
    }

    default boolean syncEmbeddedWindowDrawStateIfNeeded(com.android.server.wm.WindowState mWin) {
        return false;
    }

    default void adjustAnimationFrameForExpandedWindow(com.android.server.wm.WindowContainer container, android.graphics.Rect outframe, int transit, boolean enter) {
    }

    default int handleStartingWindowForEmbededWindow(int adjustType, com.android.server.wm.ActivityRecord record, android.window.TaskSnapshot snapshot, int type) {
        return adjustType;
    }

    default boolean shouldRequestFocusForWindow(com.android.server.wm.TaskFragment taskFragment) {
        return false;
    }

    default boolean assignLayersIfNeed(com.android.server.wm.ActivityRecord record) {
        return true;
    }

    default boolean isActivityConfigOverrideDisable(com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.WindowProcessController controller) {
        return false;
    }

    default void disposeFullTfIfNeeded(com.android.server.wm.Task task) {
    }

    default boolean shouldIgnoreOrientationRequests(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void overrideTaskFragmentAnimationIfNeed(com.android.server.wm.DisplayContent dc, com.android.server.wm.Task task, com.android.server.wm.ActivityRecord animLpActivity) {
    }

    default boolean shouldLayoutFullscreenInEmbedding(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean shouldCropAnimationLeashInEmbedding(com.android.server.wm.WindowContainer ar) {
        return true;
    }

    default boolean isAllowedToEmbedActivity(com.android.server.wm.ActivityRecord a, int uid) {
        return true;
    }

    default void onTaskFragmentPrepareSurface(com.android.server.wm.TaskFragment tf) {
    }

    default int getOverrideAppRootTaskClipMode(int appRootTaskClipMode, com.android.server.wm.WindowContainer wc) {
        return appRootTaskClipMode;
    }

    default boolean hasEmbedTaskFragment(com.android.server.wm.Task task) {
        return false;
    }

    default void adjustTouchableRegionInActivityEmbedding(com.android.server.wm.WindowState windowState, android.graphics.Rect region) {
    }

    default boolean canSpecifyOrientationInActivityEmbedding(com.android.server.wm.TaskFragment tf) {
        return false;
    }

    default boolean supportMultiResumeInActivityEmbedding(com.android.server.wm.ActivityRecord resumingActivity) {
        return false;
    }
}
