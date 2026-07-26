package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITransitionExt {

    public interface IStaticExt {
        default boolean canPromote(com.android.server.wm.Transition.ChangeInfo targetChange, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes) {
            return true;
        }

        default boolean hasChanged(com.android.server.wm.WindowContainer container, android.graphics.Rect absoluteBounds) {
            return false;
        }

        default boolean isTaskBarAnim(com.android.server.wm.WindowContainer container) {
            return false;
        }

        default boolean isTaskBarNoAnim(com.android.server.wm.ActivityRecord activity) {
            return false;
        }

        default void adjustChangeAndResetTaskBarAnimStatus(com.android.server.wm.WindowContainer container, android.window.TransitionInfo.Change change) {
        }

        default void updateAnimTargetIfNeed(android.util.ArraySet<com.android.server.wm.WindowContainer> participants, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> outTarget, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes) {
        }

        default void filterAnimTargetIfNeed(java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> sortedTargets, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes, int flag) {
        }

        default void setWindowCropForTransitionIfNeed(android.view.SurfaceControl.Transaction startT, android.view.SurfaceControl surfaceControl, com.android.server.wm.WindowContainer windowContainer) {
        }

        default boolean dontPromoteWhenReparent(com.android.server.wm.Transition.ChangeInfo targetChange, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes) {
            return true;
        }

        default android.view.SurfaceControl getReplaceParentSurface(com.android.server.wm.WindowContainer wc, android.view.SurfaceControl defaultParent) {
            return defaultParent;
        }

        default boolean skipCurrentOrAdjustChange(android.window.TransitionInfo out, int type, android.window.TransitionInfo.Change change, com.android.server.wm.Transition.ChangeInfo info, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> sortedTargets, com.android.server.wm.WindowContainer target) {
            return false;
        }

        default boolean forceSeamlesslyRotated(com.android.server.wm.DisplayContent dc, java.lang.String reason) {
            return false;
        }

        default com.android.server.wm.Task adjustChangeRotationAnimation(com.android.server.wm.Task realTask) {
            return realTask;
        }

        default void setTransitionToken(android.os.IBinder token) {
        }

        default android.view.SurfaceControl getPreReadyRootLeashIfNeed(android.window.TransitionInfo info, com.android.server.wm.WindowContainer leashReference) {
            return null;
        }
    }

    default boolean isRecentTransition() {
        return false;
    }

    default void setIsRecentTransition(boolean isRecent) {
    }

    default boolean isMergedToRecents() {
        return false;
    }

    default void setRecentFinishToHome(boolean toHome) {
    }

    default boolean isRecentFinishToHome() {
        return false;
    }

    default void setRecentFinishSeq(long seqId) {
    }

    default long getRecentFinishSeq() {
        return 0L;
    }

    default void setAssignLayerOnBuildFinish(boolean assignLayer) {
    }

    default boolean canAssignLayerForTransition() {
        return false;
    }

    default void setIsBalAllowIfNeeded(int code) {
    }

    default boolean getIsBalAllow() {
        return false;
    }

    default void setBalCode(int code) {
    }

    default int getBalCode() {
        return -1;
    }

    default void setIsRecentAnimToMini(boolean isEnterFlexibleTask) {
    }

    default boolean isRecentAnimToMini() {
        return false;
    }

    default void setStartActivitySeq(long seqId) {
    }

    default long getStartActivitySeq() {
        return 0L;
    }

    default void setFinishByRecents(boolean byRecents) {
    }

    default boolean isFinishByRecents() {
        return false;
    }

    default android.view.SurfaceControl.Transaction finishTransitionForInterrupt(com.android.server.wm.Transition transition, android.util.ArraySet<com.android.server.wm.WindowContainer> participants) {
        return null;
    }

    default android.view.SurfaceControl.Transaction deferCleanupTransactionApply(com.android.server.wm.Transition transition) {
        return null;
    }

    default boolean isRecentToHomeWithoutRemoteInterrupt() {
        return false;
    }

    default boolean isForceFinishRemote() {
        return false;
    }

    default void setForceFinishRemote(boolean force) {
    }

    default boolean addTaskToTransientHideTasks(com.android.server.wm.Task t, com.android.server.wm.Task restoreBelow) {
        return false;
    }

    default void onTransactionReady(com.android.server.wm.Transition transition, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> outTarget, android.window.TransitionInfo info, android.view.SurfaceControl.Transaction t) {
    }

    default int updateFlag(int flags, com.android.server.wm.DisplayContent dc) {
        return flags;
    }

    default boolean shouldSkipCollect(com.android.server.wm.Transition transition, com.android.server.wm.WindowContainer windowContainer, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes, android.util.ArraySet<com.android.server.wm.WindowContainer> participants) {
        return false;
    }

    default boolean notOccludedCountIfNeed(com.android.server.wm.WindowContainer<?> rootParent, com.android.server.wm.Task transientRoot) {
        return false;
    }

    default void buildFinishTransaction(android.view.SurfaceControl.Transaction t, android.window.TransitionInfo info, com.android.server.wm.WindowContainer target, android.view.SurfaceControl targetLeash) {
    }

    default boolean forceVisibleAtTransitionEnd(com.android.server.wm.Transition transition, com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean deferCommitVisible(com.android.server.wm.Transition transition, com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean deferCommitVisible(com.android.server.wm.Transition transition, com.android.server.wm.TransitionController controller, com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void hideDeferredWallpapersIfNeeded(com.android.server.wm.WindowContainer<?> participant, com.android.server.wm.ActivityRecord ar, com.android.server.wm.TransitionController tc) {
    }

    default boolean checkIfNeedRecordSnapshot(int curTaskId) {
        return true;
    }

    default void recordTaskSnapShot(com.android.server.wm.TaskSnapshotController snapshotController, com.android.server.wm.Task task) {
    }

    default void commitVisibleActivitiesIfNeed(com.android.server.wm.WindowContainer windowContainer) {
    }

    default void finishTransition(com.android.server.wm.Transition transition, android.util.ArraySet<com.android.server.wm.WindowContainer> participants) {
    }

    default void addHandledInfo(android.window.WindowContainerTransaction wct) {
    }

    default int getFeedBackFlags() {
        return 0;
    }

    default int adjustLayerZOrder(com.android.server.wm.WindowContainer wc, int defalutValue) {
        return defalutValue;
    }

    default android.view.SurfaceControl.Transaction fixFinishTransaction(android.view.SurfaceControl.Transaction t, android.window.TransitionInfo info) {
        return t;
    }

    default void setAnimThreadUxIfNeed(boolean applyToUx) {
    }

    default int fixTransitType(int originType, com.android.server.wm.TransitionController controller) {
        return originType;
    }

    default void hookSetBinderUxFlag(int pid, int flag) {
    }

    default void addFlag(int flag) {
    }

    default void setRemoteTransitionRequested(android.window.RemoteTransition remote) {
    }

    default boolean isRemoteTransitionRequested() {
        return false;
    }

    default android.window.RemoteTransition getRemoteTransitionRequested() {
        return null;
    }

    default void markAnimStartingPosition(com.android.server.wm.ActivityRecord r) {
    }

    default int[] getAnimStartingPosition() {
        return null;
    }

    default boolean getReadyInAdvance() {
        return false;
    }

    default boolean isPreReadyForMenuOrHomeKey() {
        return false;
    }

    default boolean showParentIfNeeded(com.android.server.wm.WindowContainer parent) {
        return true;
    }

    default boolean isActivityLevelTransition() {
        return false;
    }

    default boolean isRecentsPlaying() {
        return false;
    }

    default void setSlideRecentTransition(boolean isSlideRecent) {
    }

    default void hookOnAbort() {
    }

    default boolean isSlideRecentsTransition() {
        return false;
    }

    default void markRecentsFromRemote(boolean fromRemote) {
    }

    default boolean isRecentsFromRemote() {
        return false;
    }

    default com.android.server.wm.Task getHomeTask() {
        return null;
    }

    default void setHomeTask(com.android.server.wm.Task home) {
    }

    default com.android.server.wm.Task getLastOpenRootTask() {
        return null;
    }

    default void setLastOpenRootTask(com.android.server.wm.Task root) {
    }

    default void setMergedTransitionStartingFromLauncher(boolean b) {
    }

    default boolean hasMergedTransitionStartingFromLauncher() {
        return false;
    }

    default void removeTransientHideTasks() {
    }

    default boolean isInterruptTransition() {
        return false;
    }

    default void setInterruptTransition(boolean recentInterrupt) {
    }

    default void addRecentsFlagForTransition() {
    }

    default void recordMergedTransition(com.android.server.wm.Transition merge) {
    }

    default void setRemoteInterrupt(boolean interrupt) {
    }

    default boolean shouldSetOverrideOptions(android.window.TransitionInfo.AnimationOptions options, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets) {
        return true;
    }

    default boolean isRemoteInterrupt() {
        return false;
    }

    default java.util.ArrayList<com.android.server.wm.Transition> getMergedTransitions() {
        return null;
    }

    default com.android.server.wm.Transition getActiveMergedRemoteTransition() {
        return null;
    }

    default com.android.server.wm.ActivityRecord updateRecentsActivityForInterruptIfNeed(com.android.server.wm.ActivityRecord origin) {
        return origin;
    }

    default boolean forceCommitVisibility(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void markStartActivity(com.android.server.wm.ActivityRecord startActivity) {
    }

    default int getAbortOnShellReason() {
        return 0;
    }

    default void setAbortTransitonOnShell(int abortReason) {
    }

    default com.android.server.wm.ActivityRecord getStartActivity() {
        return null;
    }

    default boolean isStartedFromHome() {
        return false;
    }

    default void setTaskNotInRecents(boolean inRecents) {
    }

    default boolean getTaskNotInRecents() {
        return false;
    }

    default boolean ignoreConfigChangedIfFixRotation(com.android.server.wm.Transition transition, com.android.server.wm.DisplayContent dc, int type) {
        return false;
    }

    default boolean needConfigChangedIfNotFixRotation(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default void setCollectWhilePausing(boolean collectWhilePausing) {
    }

    default boolean isCollectWhilePausing() {
        return false;
    }

    default boolean hookDispatchLegacyAppTransitionFinished(com.android.server.wm.ActivityRecord ar, com.android.server.wm.TransitionController tc) {
        return false;
    }

    default boolean skipRecordSnapshotWhenReady() {
        return false;
    }

    default void startTransition(int type) {
    }

    default void resetStartFromLauncherBeforeAnimGo(boolean isStart, com.android.server.wm.Transition transition, int state) {
    }

    default boolean canApplyDimWithStartingSurface(com.android.server.wm.Task task) {
        return false;
    }

    default boolean shouldIgnoreOverrideAnimation(android.window.TransitionInfo.AnimationOptions overrideOption, android.window.TransitionInfo.AnimationOptions options) {
        return false;
    }

    default boolean isMinimizedToNormal() {
        return false;
    }

    default void setMinimizedToNormal(boolean isMinimizedToNormal) {
    }

    default void setSkipChangeWhenCanvasNotTop(java.util.ArrayList<com.android.server.wm.Task> onTopTasksStart, com.android.server.wm.Task task) {
    }

    default boolean hasAnimatingFixedRotationTransition(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default void setLightOSFadeAnimController(java.lang.Object controller) {
    }

    default java.lang.Object getLightOSFadeAnimController() {
        return null;
    }

    default void setReadyInAdvance(boolean ready) {
    }

    default void setPreReadyForMenuOrHomeKey(boolean preReadyForMenuOrHomeKey) {
    }

    default void hookCommitVisibleWallpapers(com.android.server.wm.Transition transition, com.android.server.wm.WallpaperWindowToken wallpaper, boolean showWallpaper, android.view.SurfaceControl.Transaction startT) {
    }

    default android.view.SurfaceControl.Transaction deferInputSinkTransactionApply(com.android.server.wm.Transition transition, android.view.SurfaceControl.Transaction t) {
        return null;
    }

    default void continueInputSinkTransactionApply(com.android.server.wm.Transition transition, boolean defer) {
    }

    default boolean isRemoveLauncherFromTarget() {
        return false;
    }

    default void setRemoveLauncherFromTarget(boolean removeLauncherFromTarget) {
    }

    default boolean isMergeToRecentsInLightOs(com.android.server.wm.Transition transition) {
        return false;
    }

    default boolean isLightOsEnable() {
        return false;
    }

    default void shouldPerformSurfacePlacement(com.android.server.wm.Transition transition) {
    }

    default void setDeferPerformSurfacePlacement(boolean deferPerformSurfacePlacement) {
    }

    default boolean getDeferPerformSurfacePlacement() {
        return false;
    }

    default void setDeferLayout(boolean deferLayout) {
    }

    default boolean getDeferLayout() {
        return false;
    }

    default void setTriggerPreReady(boolean trigger) {
    }

    default boolean hasSyncHide() {
        return false;
    }

    default void recordSyncHide(boolean has) {
    }

    default boolean getTriggerPreReady() {
        return false;
    }

    default void hideStartingSurfaceImmediatelyInRotateScene(com.android.server.wm.DisplayContent dc, boolean isDisplayRotation) {
    }

    default void fixTargetInBuildFinishTransaction(android.view.SurfaceControl.Transaction t, android.window.TransitionInfo info, com.android.server.wm.WindowContainer target) {
    }

    default void setAllReadyIfNeeded(com.android.server.wm.Transition transition, com.android.server.wm.DisplayContent dc) {
    }

    default void setFoldChangeType(int type) {
    }

    default int getFoldChangeType() {
        return 0;
    }

    default boolean readyInAdvance(com.android.server.wm.Transition transition, com.android.server.wm.TransitionController transitionController) {
        return false;
    }

    default void resetOccludeParent(android.util.ArraySet<com.android.server.wm.WindowContainer> participants, com.android.server.wm.Transition transition) {
    }
}
