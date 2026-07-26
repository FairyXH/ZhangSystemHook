package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IKeyguardControllerExt {
    default void keyguardGoingAway(int flags) {
    }

    default boolean dismissKeyguard(android.content.Context context, com.android.server.wm.ActivityRecord activityRecord, boolean showDialog) {
        return false;
    }

    default boolean checkKeyguardVisibility(com.android.server.wm.ActivityRecord r, com.android.server.wm.KeyguardController keyguardController) {
        return false;
    }

    default int getKeyguardGoingAwayFlags() {
        return 0;
    }

    default void setKeyguardShown(boolean keyguardChanged, boolean keyguardShowing, int displayId) {
    }

    public interface IStaticExt {
        default void setAppLayoutChanges(boolean occluded, boolean keyguardShowing, com.android.server.wm.DisplayContent display, com.android.server.wm.ActivityRecord ar, int changes) {
        }
    }

    default void enableOrientationListenerWhenKeyguradGoingAway(com.android.server.wm.DisplayContent displayContent, int flag) {
    }

    default boolean ifSkipTransition(int displayId) {
        return false;
    }

    default boolean skipAcquireSleepToken(int displayId) {
        return false;
    }

    default boolean skipShowWallpaper(int flags, com.android.server.wm.RootWindowContainer rootWC) {
        return false;
    }

    default void handleOccludedChangedEnd(boolean executeTransition, com.android.server.wm.KeyguardController kc, int displayId, com.android.server.wm.WindowManagerService wms, com.android.server.wm.TransitionController tc) {
    }

    default boolean shouldSkipTransition(com.android.server.wm.DisplayContent dc, java.lang.String fromReason) {
        return false;
    }
}
