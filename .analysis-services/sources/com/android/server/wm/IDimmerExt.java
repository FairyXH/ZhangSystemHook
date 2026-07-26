package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDimmerExt {
    default boolean skipDimAnimation(com.android.server.wm.WindowContainer host) {
        return false;
    }

    default boolean useSpeceficDurationForDim(com.android.server.wm.WindowContainer container, com.android.server.wm.WindowContainer host, float endAlpha) {
        return false;
    }

    default void updateDims(com.android.server.wm.WindowContainer lastRequestedDimContainer, android.graphics.Rect bounds, android.view.SurfaceControl dimLayer, android.view.SurfaceControl.Transaction t) {
    }

    default boolean shouldSkipDimAnimation(com.android.server.wm.WindowState windowState) {
        return false;
    }
}
