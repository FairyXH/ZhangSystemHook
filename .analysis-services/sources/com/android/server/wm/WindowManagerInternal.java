package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class WindowManagerInternal {

    public interface AccessibilityControllerInternal {

        public interface UiChangesForAccessibilityCallbacks {
            void onRectangleOnScreenRequested(int i, int i2, int i3, int i4, int i5);
        }

        boolean isAccessibilityTracingEnabled();

        void logTrace(java.lang.String str, long j, java.lang.String str2, byte[] bArr, int i, java.lang.StackTraceElement[] stackTraceElementArr, long j2, int i2, long j3, java.util.Set<java.lang.String> set);

        void logTrace(java.lang.String str, long j, java.lang.String str2, byte[] bArr, int i, java.lang.StackTraceElement[] stackTraceElementArr, java.util.Set<java.lang.String> set);

        void setUiChangesForAccessibilityCallbacks(com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks uiChangesForAccessibilityCallbacks);

        void startTrace(long j);

        void stopTrace();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ImeClientFocusResult {
        public static final int DISPLAY_ID_MISMATCH = -2;
        public static final int HAS_IME_FOCUS = 0;
        public static final int INVALID_DISPLAY_ID = -3;
        public static final int NOT_IME_TARGET_WINDOW = -1;
    }

    public interface KeyguardExitAnimationStartListener {
        void onAnimationStart(android.view.RemoteAnimationTarget[] remoteAnimationTargetArr, android.view.RemoteAnimationTarget[] remoteAnimationTargetArr2, android.view.IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback);
    }

    public interface MagnificationCallbacks {
        void onDisplaySizeChanged();

        void onImeWindowVisibilityChanged(boolean z);

        void onMagnificationRegionChanged(android.graphics.Region region);

        void onRectangleOnScreenRequested(int i, int i2, int i3, int i4);

        void onUserContextChanged();
    }

    public interface OnHardKeyboardStatusChangeListener {
        void onHardKeyboardStatusChange(boolean z);
    }

    public interface OnImeRequestedChangedListener {
        void onImeRequestedChanged(android.os.IBinder iBinder, boolean z);
    }

    public interface OnWindowRemovedListener {
        void onWindowRemoved(android.os.IBinder iBinder);
    }

    public interface TaskSystemBarsListener {
        void onTransientSystemBarsVisibilityChanged(int i, boolean z, boolean z2);
    }

    public interface WindowsForAccessibilityCallback {
        void onAccessibilityWindowsChanged(boolean z, int i, android.os.IBinder iBinder, android.graphics.Point point, java.util.List<com.android.server.wm.AccessibilityWindowsPopulator.AccessibilityWindow> list);

        void onWindowsForAccessibilityChanged(boolean z, int i, android.os.IBinder iBinder, java.util.List<android.view.WindowInfo> list);
    }

    public abstract void addBlockScreenCaptureForApps(android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> arraySet);

    public abstract void addRefreshRateRangeForPackage(java.lang.String str, float f, float f2);

    public abstract void addTrustedTaskOverlay(int i, android.view.SurfaceControlViewHost.SurfacePackage surfacePackage);

    public abstract void addWindowToken(android.os.IBinder iBinder, int i, int i2, android.os.Bundle bundle);

    public abstract void captureDisplay(int i, android.window.ScreenCapture.CaptureArgs captureArgs, android.window.ScreenCapture.ScreenCaptureListener screenCaptureListener);

    public abstract void clearBlockedApps();

    public abstract void clearDisplaySettings(java.lang.String str, int i);

    public abstract void clearForcedDisplaySize(int i);

    public abstract void clearSnapshotCache();

    public abstract void computeWindowsForAccessibility(int i);

    public abstract android.view.SurfaceControl getA11yOverlayLayer(int i);

    public abstract com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal getAccessibilityController();

    public abstract int getDisplayIdForWindow(android.os.IBinder iBinder);

    public abstract int getDisplayImePolicy(int i);

    public abstract android.os.IBinder getFocusedWindowToken();

    public abstract android.os.IBinder getFocusedWindowTokenFromWindowStates();

    public abstract android.view.SurfaceControl getHandwritingSurfaceForDisplay(int i);

    public abstract int getInputMethodWindowVisibleHeight(int i);

    public abstract com.android.internal.policy.KeyInterceptionInfo getKeyInterceptionInfoFromToken(android.os.IBinder iBinder);

    public abstract void getMagnificationRegion(int i, android.graphics.Region region);

    public abstract android.os.IBinder getTargetWindowTokenFromInputToken(android.os.IBinder iBinder);

    public abstract int getTopFocusedDisplayId();

    public abstract android.content.Context getTopFocusedDisplayUiContext();

    public abstract void getWindowFrame(android.os.IBinder iBinder, android.graphics.Rect rect);

    public abstract java.lang.String getWindowName(android.os.IBinder iBinder);

    public abstract int getWindowOwnerUserId(android.os.IBinder iBinder);

    public abstract android.util.Pair<android.graphics.Matrix, android.view.MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(android.os.IBinder iBinder);

    public abstract int hasInputMethodClientFocus(android.os.IBinder iBinder, int i, int i2, int i3);

    public abstract boolean hasNavigationBar(int i);

    public abstract void hideIme(android.os.IBinder iBinder, int i, android.view.inputmethod.ImeTracker.Token token);

    public abstract boolean isHardKeyboardAvailable();

    public abstract boolean isHomeSupportedOnDisplay(int i);

    public abstract boolean isKeyguardLocked();

    public abstract boolean isKeyguardSecure(int i);

    public abstract boolean isKeyguardShowingAndNotOccluded();

    public abstract boolean isPointInsideWindow(android.os.IBinder iBinder, int i, float f, float f2);

    public abstract boolean isTouchOrFaketouchDevice();

    public abstract boolean isUidAllowedOnDisplay(int i, int i2);

    public abstract boolean isUidFocused(int i);

    public abstract boolean keepSimultaneousDisplay();

    public abstract void lockNow();

    public abstract void moveDisplayToTopIfAllowed(int i);

    public abstract boolean moveFocusToAdjacentEmbeddedActivityIfNeeded();

    public abstract void moveWindowTokenToDisplay(android.os.IBinder iBinder, int i);

    public abstract void onDisplayManagerReceivedDeviceState(int i);

    public abstract com.android.server.wm.WindowManagerInternal.ImeTargetInfo onToggleImeRequested(boolean z, android.os.IBinder iBinder, android.os.IBinder iBinder2, int i);

    public abstract void registerAppTransitionListener(com.android.server.wm.WindowManagerInternal.AppTransitionListener appTransitionListener);

    public abstract void registerDragDropControllerCallback(com.android.server.wm.WindowManagerInternal.IDragDropCallback iDragDropCallback);

    public abstract void registerOnWindowRemovedListener(com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener onWindowRemovedListener);

    public abstract void registerTaskSystemBarsListener(com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener taskSystemBarsListener);

    public abstract void removeBlockScreenCaptureForApps(android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> arraySet);

    public abstract void removeRefreshRateRangeForPackage(java.lang.String str);

    public abstract void removeTrustedTaskOverlay(int i, android.view.SurfaceControlViewHost.SurfacePackage surfacePackage);

    public abstract void removeWindowToken(android.os.IBinder iBinder, boolean z, boolean z2, int i);

    public abstract void reportPasswordChanged(int i);

    public abstract void requestTraversalFromDisplayManager();

    public abstract void requestWindowFocus(android.os.IBinder iBinder);

    public abstract void setAccessibilityIdToSurfaceMetadata(android.os.IBinder iBinder, int i);

    public abstract void setBlockScreenCaptureForAppsSessionId(long j);

    public abstract boolean setContentRecordingSession(android.view.ContentRecordingSession contentRecordingSession);

    public abstract void setDismissImeOnBackKeyPressed(boolean z);

    public abstract void setForcedDisplaySize(int i, int i2, int i3);

    public abstract void setFullscreenMagnificationActivated(int i, boolean z);

    public abstract void setHomeSupportedOnDisplay(java.lang.String str, int i, boolean z);

    public abstract void setInputFilter(android.view.IInputFilter iInputFilter);

    public abstract void setInputMethodTargetChangeListener(com.android.server.wm.ImeTargetChangeListener imeTargetChangeListener);

    public abstract boolean setMagnificationCallbacks(int i, com.android.server.wm.WindowManagerInternal.MagnificationCallbacks magnificationCallbacks);

    public abstract void setMagnificationSpec(int i, android.view.MagnificationSpec magnificationSpec);

    public abstract void setOnHardKeyboardStatusChangeListener(com.android.server.wm.WindowManagerInternal.OnHardKeyboardStatusChangeListener onHardKeyboardStatusChangeListener);

    public abstract void setOnImeRequestedChangedListener(com.android.server.wm.WindowManagerInternal.OnImeRequestedChangedListener onImeRequestedChangedListener);

    public abstract void setOrientationRequestPolicy(boolean z, int[] iArr, int[] iArr2);

    public abstract void setVr2dDisplayId(int i);

    public abstract void setWallpaperCropHints(android.os.IBinder iBinder, android.util.SparseArray<android.graphics.Rect> sparseArray);

    public abstract void setWallpaperCropUtils(com.android.server.wallpaper.WallpaperCropper.WallpaperCropUtils wallpaperCropUtils);

    public abstract void setWallpaperShowWhenLocked(android.os.IBinder iBinder, boolean z);

    public abstract void setWindowsForAccessibilityCallback(int i, com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback windowsForAccessibilityCallback);

    public abstract boolean shouldRestoreImeVisibility(android.os.IBinder iBinder);

    public abstract void showGlobalActions();

    public abstract void showImePostLayout(android.os.IBinder iBinder, android.view.inputmethod.ImeTracker.Token token);

    public abstract android.window.ScreenCapture.ScreenshotHardwareBuffer takeAssistScreenshot(java.util.Set<java.lang.Integer> set);

    public abstract void unregisterOnWindowRemovedListener(com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener onWindowRemovedListener);

    public abstract void unregisterTaskSystemBarsListener(com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener taskSystemBarsListener);

    public abstract void updateInputMethodTargetWindow(android.os.IBinder iBinder, android.os.IBinder iBinder2);

    public abstract void waitForAllWindowsDrawn(android.os.Message message, long j, int i);

    public static abstract class AppTransitionListener {
        public void onAppTransitionPendingLocked() {
        }

        public void onAppTransitionCancelledLocked(boolean keyguardGoingAwayCancelled) {
        }

        public void onAppTransitionTimeoutLocked() {
        }

        public int onAppTransitionStartingLocked(long statusBarAnimationStartTime, long statusBarAnimationDuration) {
            return 0;
        }

        public void onAppTransitionFinishedLocked(android.os.IBinder token) {
        }
    }

    public interface IDragDropCallback {
        default java.util.concurrent.CompletableFuture<java.lang.Boolean> registerInputChannel(final com.android.server.wm.DragState state, android.view.Display display, final com.android.server.input.InputManagerService service, final android.view.InputChannel source) {
            return state.register(display).thenApply(new java.util.function.Function() { // from class: com.android.server.wm.WindowManagerInternal$IDragDropCallback$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Boolean.valueOf(service.startDragAndDrop(source, state.getInputChannel()));
                }
            });
        }

        default boolean prePerformDrag(android.view.IWindow window, android.os.IBinder dragToken, int touchSource, float touchX, float touchY, float thumbCenterX, float thumbCenterY, android.content.ClipData data) {
            return true;
        }

        default void postPerformDrag() {
        }

        default void preReportDropResult(android.view.IWindow window, boolean consumed) {
        }

        default void postReportDropResult() {
        }

        default void preCancelDragAndDrop(android.os.IBinder dragToken) {
        }

        default void postCancelDragAndDrop() {
        }

        default void dragRecipientEntered(android.view.IWindow window) {
        }

        default void dragRecipientExited(android.view.IWindow window) {
        }
    }

    public final void removeWindowToken(android.os.IBinder token, boolean removeWindows, int displayId) {
        removeWindowToken(token, removeWindows, true, displayId);
    }

    public static class ImeTargetInfo {
        public final java.lang.String focusedWindowName;
        public final java.lang.String imeControlTargetName;
        public final java.lang.String imeLayerTargetName;
        public final java.lang.String imeSurfaceParentName;
        public final java.lang.String requestWindowName;

        public ImeTargetInfo(java.lang.String focusedWindowName, java.lang.String requestWindowName, java.lang.String imeControlTargetName, java.lang.String imeLayerTargetName, java.lang.String imeSurfaceParentName) {
            this.focusedWindowName = focusedWindowName;
            this.requestWindowName = requestWindowName;
            this.imeControlTargetName = imeControlTargetName;
            this.imeLayerTargetName = imeLayerTargetName;
            this.imeSurfaceParentName = imeSurfaceParentName;
        }
    }
}
