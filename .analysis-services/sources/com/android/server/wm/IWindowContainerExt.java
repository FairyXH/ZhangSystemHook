package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowContainerExt {
    default float getWindowCornerRadiusForAnimation(com.android.server.wm.WindowContainer container, float originWindowCornerRadius, android.view.animation.Animation animation, android.view.WindowManager.LayoutParams lp, int transit) {
        return originWindowCornerRadius;
    }

    default boolean adjustZBoostForTransit(com.android.server.wm.WindowContainer wc, int transit, boolean isEnter, boolean needsZBoost) {
        return needsZBoost;
    }

    default boolean canPaintAnimation(int transit) {
        return false;
    }

    default void tryPaintAnimation(android.view.animation.Animation animation, int transit, boolean isEnter) {
    }

    default void addAnimationUpdateRecorder(android.view.animation.Animation animation, int transit, boolean isEnter, int width, int height, int parentWidth, int parentHeight) {
    }

    default android.view.animation.Animation createAnimationForLauncherExit() {
        return null;
    }

    default void onAnimationFinished(com.android.server.wm.WindowContainer container, android.os.Handler handler) {
    }

    default boolean hookGetOrientation(com.android.server.wm.WindowContainer container) {
        return false;
    }

    default boolean hookupdateSurfacePosition(int windowMode, com.android.server.wm.Task task, com.android.server.wm.WindowContainer container) {
        return false;
    }

    default boolean isZoomMode(int mode) {
        return false;
    }

    default boolean enableTaskBackgroundColor(com.android.server.wm.WindowContainer container) {
        return false;
    }

    default void removeChild(com.android.server.wm.WindowContainer parent, com.android.server.wm.WindowContainer child) {
    }

    default void addRoundedCornersToAnimationIfNeed(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, boolean isVoiceInteraction, android.view.animation.Animation animation) {
    }

    default boolean isSettingTaskFragment(com.android.server.wm.WindowContainer container) {
        return false;
    }

    default void addChild(com.android.server.wm.WindowContainer parent, com.android.server.wm.WindowContainer child) {
    }

    default void onChildAdded(com.android.server.wm.WindowContainer child, com.android.server.wm.WindowContainer parent) {
    }

    default void onOriginListAdded(com.android.server.wm.WindowContainer thisContainer, com.android.server.wm.WindowContainer parent) {
    }

    default void onChildRemoved(com.android.server.wm.WindowContainer child, com.android.server.wm.WindowContainer parent) {
    }

    default void adjustPointsOffsetForParallelWindowAnimation(com.android.server.wm.WindowContainer record, android.graphics.Point tempPoint) {
    }

    default com.android.server.wm.AnimationAdapter getClipAnimationAdapter(com.android.server.wm.AnimationAdapter adapter, android.view.animation.Animation a, android.graphics.Point tmpPoint, android.graphics.Rect tmpRect, int appStackClipMode, float windowCornerRadius, int transit, boolean enter, com.android.server.wm.WindowContainer container) {
        return adapter;
    }

    default boolean blockUpdateSurfacePosition(com.android.server.wm.WindowContainer record) {
        return false;
    }

    default boolean isFingerPrintToken(com.android.server.wm.WindowContainer record) {
        return false;
    }

    default void onParentConfirmed(com.android.server.wm.WindowContainer child) {
    }

    default boolean forceUpdateConfig(com.android.server.wm.ConfigurationContainer requestingContainer, int requestedOrientation) {
        return false;
    }

    default boolean shouldUpdateConfig(com.android.server.wm.ConfigurationContainer requestingContainer, int requestedOrientation) {
        return false;
    }

    default void adjustAnimationForMultiTask(com.android.server.wm.WindowContainer wc, android.view.animation.Animation a, android.graphics.Rect animationFrame) {
    }

    default android.util.Pair<com.android.server.wm.AnimationAdapter, com.android.server.wm.AnimationAdapter> getRemoteAnimationAdapterForSplitScreen(com.android.server.wm.RemoteAnimationController controller, com.android.server.wm.WindowContainer wc, int transit, boolean enter, boolean isChanging) {
        return null;
    }

    default void adjustAnimationFrameForExpandedWindow(com.android.server.wm.WindowContainer container, android.graphics.Rect outframe, int transit, boolean enter) {
    }

    default void handleComapctReparent(com.android.server.wm.WindowContainer child, boolean beforeReparent, com.android.server.wm.WindowContainer newParent) {
    }

    default int adjustOrientationForBracketMode(int OriginOrientation) {
        return OriginOrientation;
    }

    default boolean shouldIgnorePositionChildAtTop(com.android.server.wm.WindowContainer parent, com.android.server.wm.WindowContainer child) {
        return false;
    }

    default boolean isSyncFinishedInCompactWindow(com.android.server.wm.WindowContainer container, int size) {
        return true;
    }

    default void setMaskIfNeedsInCompactWindow(com.android.server.wm.WindowContainer container, int size) {
    }

    default int getFixedScreenOrientation(com.android.server.wm.WindowContainer container, int originOrientation) {
        return originOrientation;
    }

    default android.util.Pair<com.android.server.wm.AnimationAdapter, com.android.server.wm.AnimationAdapter> hookGetAnimationAdapter(com.android.server.wm.WindowContainer wc, android.view.WindowManager.LayoutParams lp, int transit, boolean enter, boolean isVoiceInteraction) {
        return null;
    }

    default boolean isLightOsCompactWindow(int mode) {
        return false;
    }

    default void onSyncFinishedDrawing(com.android.server.wm.WindowContainer container) {
    }

    default int getOverrideAppRootTaskClipMode(int appRootTaskClipMode, com.android.server.wm.WindowContainer wc) {
        return appRootTaskClipMode;
    }

    default boolean shouldCropAnimationLeashInEmbedding(com.android.server.wm.WindowContainer wc) {
        return true;
    }

    default void onAnimationLeashCreated(com.android.server.wm.WindowContainer wc, android.view.SurfaceControl.Transaction t) {
    }

    default void onAnimationLeashLost(com.android.server.wm.WindowContainer wc, android.view.SurfaceControl.Transaction transaction) {
    }

    default boolean assignLayerForTransition() {
        return false;
    }

    default void adjustAnimationBounds(com.android.server.wm.WindowContainer wc, android.graphics.Rect screenBounds) {
    }

    default void dispatchConfigurationToChild(com.android.server.wm.DisplayContent child, android.content.res.Configuration config) {
    }

    default int[] getAdjustDisplayInfo(android.view.DisplayInfo displayInfo) {
        return new int[]{displayInfo.appWidth, displayInfo.appHeight};
    }

    default boolean shouldSkipAnimationForFlexibleWindow(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default android.util.Pair<com.android.server.wm.AnimationAdapter, com.android.server.wm.AnimationAdapter> getRemoteAnimationAdapterForCompactWindow(com.android.server.wm.RemoteAnimationController controller, com.android.server.wm.WindowContainer wc, int transit, boolean enter) {
        return null;
    }

    default boolean skipCheckSyncFinished(com.android.server.wm.WindowContainer parent, com.android.server.wm.WindowContainer child, int childrenSize) {
        return false;
    }

    default boolean skipCheckSyncFinishedForFlexible(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default boolean skipLoadAnimation() {
        return true;
    }

    default boolean skipSystemCreation(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default boolean forceFinishSync(com.android.server.wm.WindowContainer wc, com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        return false;
    }

    default void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.wm.WindowContainer wc) {
    }

    default void showSurfaceControl(com.android.server.wm.WindowContainer wc) {
    }

    default int getShowOnSyncGroupId() {
        return 0;
    }

    default boolean shouldResumeTaskTopActivity(com.android.server.wm.Task rootTask, com.android.server.wm.ActivityRecord topRunningActivity) {
        return true;
    }

    default void logBehindOrientation(com.android.server.wm.WindowContainer wc) {
    }

    default boolean dependShellTransition(boolean show) {
        return false;
    }

    default boolean notSkipSyncFinishedWhenCanvas(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        return false;
    }

    default void enablePendingApplyTransition(com.android.server.wm.WindowContainer wc, android.view.SurfaceControl.Transaction t) {
    }

    default void hookWaitForSyncTransactionCommit(com.android.server.wm.WindowContainer wc) {
    }

    default void hookPrepareSurfacesEnd() {
    }

    default void applyPendingTransitionIfNeed() {
    }

    default void updateSurfaceVisibility(com.android.server.wm.WindowContainer wc) {
    }

    default void recordSyncHideForCollecting(com.android.server.wm.WindowState win, android.view.SurfaceControl.Transaction transaction) {
    }

    default void preReparent(com.android.server.wm.WindowContainer wc, com.android.server.wm.DisplayContent newParent) {
    }
}
