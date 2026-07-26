package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDragDropControllerExt {
    default void registerCallback(com.android.server.wm.IDragDropControllerExt.IOplusDragDropControllerExtCallback callback) {
    }

    default void postPerSuccessformDrag(android.view.IWindow window, android.view.SurfaceControl surface, int touchSource, float[] pointArray, android.content.ClipData data) {
    }

    default void postEndDrag() {
    }

    default void postCancelDragAndDrop() {
    }

    default boolean getConsumedResult() {
        return false;
    }

    default boolean isInterceptedDrop(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default android.graphics.Point adjustYForZoomWinIfNeed(com.android.server.wm.WindowState touchWin, float x, float y) {
        return null;
    }

    default void grantPermission(com.android.server.wm.WindowState touchedWin, com.android.server.wm.DragAndDropPermissionsHandler dragAndDropPermissions) {
    }

    default android.animation.ValueAnimator createCustormAnimatorIfNeed(int type, com.android.server.wm.DragState state) {
        return null;
    }

    default android.animation.ValueAnimator createReturnAnimationIfNeed(com.android.server.wm.DragState state) {
        return null;
    }

    default boolean vibrateIfNeed(com.android.server.wm.WindowState targetWin, com.android.server.wm.WindowState touchWin) {
        return false;
    }

    default android.view.SurfaceControl createDnDAnimationLeash(com.android.server.wm.DragState dragState, android.view.SurfaceControl.Transaction t, float touchX, float touchY, com.android.server.wm.DisplayContent displayContent) {
        return null;
    }

    default void notifyDnDSplitScreenLocation(float x, float y) {
    }

    default void closeDnDSplitScreenStateIfNeed() {
    }

    default boolean notifyDnDSplitScreenCloseIfNeed() {
        return false;
    }

    default void notifyDnDSplitScreenStartIfNeed(com.android.server.wm.DragState dragState) {
    }

    default boolean getPlayShrinkAnimState() {
        return false;
    }

    default android.animation.ValueAnimator notifyDnDSplitScreenDrop(float x, float y) {
        return null;
    }

    default boolean isSupportDragPkg(java.lang.String pkgName) {
        return false;
    }

    default void handleZoomDrag(float newX, float newY) {
    }

    default boolean isForwardCompatibleVersion(int versionCode) {
        return false;
    }

    public interface IOplusDragDropControllerExtCallback {
        default void postPerSuccessformDrag(android.view.IWindow window, android.view.SurfaceControl surface, int touchSource, float[] pointArray, android.content.ClipData data) {
        }

        default void postEndDrag() {
        }

        default void postCancelDragAndDrop() {
        }

        default boolean getConsumedResult() {
            return false;
        }

        default void onHandleZoomDrag(float newX, float newY) {
        }
    }
}
