package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IContentRecorderExt {
    default boolean ifNeedRotateSurfaceForOplus(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default void rotateSurface(android.view.SurfaceControl mRecordedSurface, android.view.SurfaceControl.Transaction transaction, float scale, android.graphics.Rect recordedContentBounds, android.graphics.Point surfaceSize, int curRotation) {
    }

    default boolean updateMirroringIfSurfaceSizeChanged() {
        return false;
    }

    default void setSurfaceSize(android.graphics.Point point) {
    }

    default boolean isLinkToWindowsMode(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default boolean isSurfaceSizeChanged() {
        return false;
    }

    default boolean synchronizeMirrorTransactionIfNeeded() {
        return false;
    }

    default boolean shouldInterceptStartRecording(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default void pauseRecording(com.android.server.wm.DisplayContent dc) {
    }

    default void startRecording(com.android.server.wm.DisplayContent dc) {
    }
}
