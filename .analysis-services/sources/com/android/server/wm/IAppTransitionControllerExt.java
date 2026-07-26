package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAppTransitionControllerExt {
    public static final int TRANSIT_OLD_PUTT_TASK = 100;

    default boolean applyAnimations(android.util.ArraySet<com.android.server.wm.WindowContainer> openingWcs, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps) {
        return false;
    }

    default boolean applyAnimations(android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps, int transit) {
        return false;
    }

    default void collectWcs(android.util.ArraySet<com.android.server.wm.WindowContainer> openingWcs, android.util.ArraySet<com.android.server.wm.WindowContainer> closingWcs) {
    }

    default boolean skipAppTransitionAnimation() {
        return false;
    }

    default boolean applyAnimationForLauncherIfNeed(com.android.server.wm.DisplayContent dc, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps, int transit) {
        return false;
    }

    default void handleAppTransitionReady(int transit) {
    }

    default boolean transitionGoodToGo(com.android.server.wm.ActivityRecord activity, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps) {
        return false;
    }

    default boolean isTransferStartingWindow(com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default void startKeyguardExitOnKeyguardIfNeeded(int transit, int flags, com.android.server.wm.DisplayContent dc) {
    }

    default boolean isDragToSplitState() {
        return false;
    }

    default void onAppTransitionReady(com.android.server.wm.DisplayContent mDisplayContent) {
    }

    default boolean isGoodToGoWhenEnterCompactWindowApp(com.android.server.wm.ActivityRecord activity) {
        return false;
    }

    default boolean changeCanPromote(boolean canPromote, boolean visible, com.android.server.wm.WindowContainer current, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps) {
        return false;
    }

    default boolean skipCheckOtherAncestors(boolean visible, com.android.server.wm.WindowContainer current, com.android.server.wm.WindowContainer currentParent, android.util.ArraySet<com.android.server.wm.ActivityRecord> otherApps) {
        return false;
    }

    default boolean addSiblingToAnimationTargets(com.android.server.wm.WindowContainer record, int transit, boolean visible) {
        return false;
    }

    default boolean isNeedApplyAnimationForLauncher() {
        return false;
    }

    default boolean getParallelWindowAnimationTargets(com.android.server.wm.WindowContainer current, com.android.server.wm.WindowContainer parent, java.util.ArrayList<com.android.server.wm.WindowContainer> siblings, java.util.LinkedList<com.android.server.wm.WindowContainer> candidates, boolean visible, int oldTransit, boolean canPromote) {
        return false;
    }

    default boolean isCompactWindowingMode(int windowingMode) {
        return false;
    }

    default boolean isNeedOverrideWithTaskFragmentRemoteAnimation(com.android.server.wm.Task task) {
        return true;
    }

    default boolean overrideWithSplitScreenRemoteAnimationIfNeed(com.android.server.wm.DisplayContent dc, int transit, android.util.ArraySet<java.lang.Integer> activityTypes) {
        return false;
    }

    default boolean overrideWithRemoteAnimationIfNeed(com.android.server.wm.DisplayContent dc, int transit, android.util.ArraySet<java.lang.Integer> activityTypes, com.android.server.wm.ActivityRecord topClosingApp) {
        return false;
    }

    default boolean shouldDoPuttTransition(android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps) {
        return false;
    }

    default boolean isPrimaryActivityCloseInCompactWindow(android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps) {
        return false;
    }

    default boolean isGoodToGoWhenStartTasks(com.android.server.wm.ActivityRecord activity) {
        return true;
    }

    default boolean isAllInSplitOpening(android.util.ArraySet<? extends com.android.server.wm.WindowContainer> apps) {
        return true;
    }

    default void overrideTaskFragmentAnimationIfNeed(com.android.server.wm.DisplayContent dc, com.android.server.wm.Task task, com.android.server.wm.ActivityRecord animLpActivity) {
    }

    default boolean isNeedIgnoreFlexibleSplitStartupAnimation(android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps) {
        return false;
    }
}
