package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAppTransitionExt {
    default android.view.animation.Animation hookloadAnimationSafely(android.content.Context context, boolean custom, int resId, java.lang.String nextAppTransitionPackage, java.lang.String TAG) {
        return com.android.internal.policy.TransitionAnimation.loadAnimationSafely(context, resId, TAG);
    }

    default android.view.animation.Animation checkAndLoadCustomAnimation(java.lang.String packageName, int transit, boolean enter, int animResId) {
        return null;
    }

    default android.view.animation.Animation loadOplusStyleAnimation(android.view.WindowManager.LayoutParams lp, int transit, boolean enter) {
        return null;
    }

    default void postAppTransitionDelayedCallback(android.os.Handler handler, int transit, com.android.server.wm.RemoteAnimationController remote, com.android.server.wm.DisplayContent dc) {
    }

    default void removeAppTransitionDelayedCallback(android.os.Handler handler) {
    }

    default void appTransitionTimeout(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent dc) {
    }

    default android.view.animation.Animation createHiddenByKeyguardExit(int transitionFlags, boolean onWallpaper, int flags, boolean home) {
        return null;
    }

    default boolean canCustomizeAppTransition(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, com.android.server.wm.WindowContainer windowContainer, java.lang.String overridePackage) {
        return windowContainer.canCustomizeAppTransition();
    }

    default android.view.animation.Animation loadKeyguardUnoccludeAnimation(com.android.server.wm.WindowContainer container) {
        return null;
    }

    default boolean isKeyguardGoingAwayTransit(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent dc, int transit, java.util.ArrayList<java.lang.Integer> appTransitionRequests) {
        return false;
    }

    default android.view.animation.Animation loadTransitCustomCompactWindowAnimation(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, com.android.server.wm.WindowContainer container) {
        return null;
    }

    default android.view.animation.Animation loadCompactWindowAnimation(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, com.android.server.wm.WindowContainer container) {
        return null;
    }

    default android.view.animation.Animation loadOnePuttTransitionAnimation(int transit, boolean enter, com.android.server.wm.WindowContainer container) {
        return null;
    }

    default android.view.animation.Animation loadFlexibleActivityTransitionAnimation(int transit, boolean enter, com.android.server.wm.WindowContainer wc, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps) {
        return null;
    }

    default android.view.animation.Animation loadFlexibleTaskTransitionAnimation(int transit, boolean enter, com.android.server.wm.WindowContainer wc, android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps, android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps) {
        return null;
    }

    default android.view.animation.Animation loadCustomZoomAnimation(int transit, com.android.server.wm.WindowContainer container, android.view.animation.Animation a) {
        return a;
    }

    default android.view.animation.Animation updateAnimationForZoom(int transit, com.android.server.wm.WindowContainer cont, android.view.animation.Animation a) {
        return a;
    }

    default void hookgoodToGo(com.android.server.wm.DisplayContent dc, int transit) {
    }

    default void validateKeyguardOcclusion(com.android.server.wm.DisplayContent dc) {
    }

    default void clearOverrideTransitionForResumed() {
    }

    default void setOverrideTransitionForResumed(boolean canPromoteToTaskInParallelWindow, boolean hasRoundedCornersForTransitionResumed) {
    }

    default boolean isPromoteAnimTargetToTaskInParallelWindow() {
        return false;
    }

    default void setRoundedCornersForCustomAnim(android.view.animation.Animation a) {
    }
}
