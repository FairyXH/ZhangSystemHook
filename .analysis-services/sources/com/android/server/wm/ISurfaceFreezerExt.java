package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISurfaceFreezerExt {
    default android.window.ScreenCapture.ScreenshotHardwareBuffer createFlexibleTaskSnapshotBuffer(android.view.SurfaceControl freezeTarget) {
        return null;
    }

    default void resetFlexibleTaskInfo() {
    }

    default void setFlexibleTaskInfo(android.graphics.Rect snapShotBounds, float scale) {
    }

    default float getFlexibleTaskScale() {
        return 1.0f;
    }
}
