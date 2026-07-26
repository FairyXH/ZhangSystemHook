package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDeferredDisplayUpdaterExt {
    default boolean applyDisplayInfoChangeImmediately(android.view.DisplayInfo displayInfo, int displayInfoDiff, boolean physicalDisplayUpdated) {
        return false;
    }

    default boolean skipWaitForTransition(boolean switching) {
        return false;
    }

    default void requestDisplayChangeTransition(com.android.server.wm.Transition transition, boolean physicalDisplayUpdated) {
    }

    default boolean onDisplayUpdated(com.android.server.wm.Transition transition) {
        return false;
    }
}
