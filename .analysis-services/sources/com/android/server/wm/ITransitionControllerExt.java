package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITransitionControllerExt {
    default void requestStartTransition(android.window.TransitionRequestInfo requestInfo, com.android.server.wm.Transition transition, com.android.server.wm.Task task) {
    }

    default void finishTransition(com.android.server.wm.Transition transition) {
    }

    default void initFoldScreenBlackCoverStrategy() {
    }

    default void notifySysWindowRotation(java.lang.Class clazz, android.content.ComponentName componentInfo, android.window.TransitionRequestInfo.DisplayChange displayChange) {
    }

    default boolean skipRequestCloseTransitionIfNeeded(com.android.server.wm.WindowContainer<?> wc) {
        return false;
    }

    default boolean skipUpdateWallpaperVisibility(boolean visible, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean canAssignLayers(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default boolean canAssignLayersWhenPlaying(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default void setOverrideAnimation(android.window.TransitionInfo.AnimationOptions options, com.android.server.wm.Transition collectingTransition) {
    }

    default boolean skipSyncAssignTrack(com.android.server.wm.Transition transition, android.window.TransitionInfo info, java.util.ArrayList<com.android.server.wm.Transition> playingTransitions) {
        return false;
    }

    default void hookSetBinderUxFlag(boolean applyToUx) {
    }

    default void setAnimThreadUxIfNeed(boolean applyToUx) {
    }

    default void setTransientLaunchIfNeed(com.android.server.wm.ActivityRecord activity, com.android.server.wm.TransitionController transitionController) {
    }

    default void validateKeyguardOcclusion(com.android.server.wm.DisplayContent dc) {
    }

    default boolean isStartedFromHomeTransitionPlaying(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default void requestSysResource(com.android.server.wm.Transition transition, android.window.RemoteTransition remoteTransition, android.window.TransitionRequestInfo.DisplayChange displayChange) {
    }

    default void releaseSysResource(com.android.server.wm.Transition record) {
    }

    default void setWindowSyncMethod(android.window.TransitionRequestInfo.DisplayChange displayChange, com.android.server.wm.Transition displayTransition, com.android.server.wm.DisplayContent dc, com.android.server.wm.BLASTSyncEngine syncEngine) {
    }

    default com.android.server.wm.Transition getRecentsFromRemoteTransition() {
        return null;
    }

    default boolean adjustTrackForRecentsFromRemote(java.util.ArrayList<com.android.server.wm.Transition> mPlayingTransitions, com.android.server.wm.Transition transition, int index, android.window.TransitionInfo info) {
        return false;
    }

    default boolean isTransientLaunchInRecentsFromRemote(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean isTransientHideInRecentsFromRemote(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isTransientLaunchNotFromRemote(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default com.android.server.wm.Transition getRecentsTransition() {
        return null;
    }

    default boolean shouldWaitWallpaperHide() {
        return false;
    }

    default boolean isFixedTransitionCollectingOrPlaying() {
        return false;
    }

    default void hookCreateTransition(com.android.server.wm.Transition transit) {
    }

    default boolean forceAsyncAssignTrackIfNeed(com.android.server.wm.Transition transition, android.window.TransitionInfo info) {
        return false;
    }

    default void setFinishingRecentTransition(com.android.server.wm.Transition transition) {
    }

    default com.android.server.wm.Transition getFinishingRecentTransition() {
        return null;
    }

    default boolean delayTriggerExitAnimationDoneWhenFinish(com.android.server.wm.Transition record) {
        return false;
    }

    default boolean makeIndependentTrackIfNeed(com.android.server.wm.Transition running, com.android.server.wm.Transition incoming) {
        return false;
    }

    default void recordEmbeddedTaskSnapshots(com.android.server.wm.Task container) {
    }

    default void hookAfterAbort(com.android.server.wm.Transition transition) {
    }
}
