package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IInsetsSourceProviderExt {
    default boolean getInputShowStatus() {
        return false;
    }

    default boolean hasFlexibleActivityInfo(com.android.server.wm.WindowState win) {
        return false;
    }

    default void showImeLeashInCarDisplayIfNeed(com.android.server.wm.InsetsSourceProvider provider, com.android.server.wm.InsetsControlTarget controlTarget, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
    }

    default boolean changeStatusBarTransaction(com.android.server.wm.WindowContainer container, android.view.InsetsSource source, boolean seamlessRotating) {
        return false;
    }

    default boolean adjustForceUpdateControlForTarget(com.android.server.wm.InsetsControlTarget target, boolean force) {
        return force;
    }

    default void setHasPendingPosition(boolean hasPendingPosition) {
    }

    default boolean shouldIgnoreTargetCheck() {
        return false;
    }
}
