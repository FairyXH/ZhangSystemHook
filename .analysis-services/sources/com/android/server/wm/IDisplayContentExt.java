package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayContentExt {
    default void initOplusRefreshRatePolicy(java.lang.Object[] ORRPParams) {
    }

    default void setVendorPreferredRefreshRate(com.android.server.wm.IWindowStateExt wExt, com.android.server.wm.WindowState w) {
    }

    default void applyPreferredMode(com.android.server.wm.IWindowStateExt wExt, com.android.server.wm.WindowState w, boolean topFullScreen) {
    }

    default void applyPreferredMode(com.android.server.wm.IWindowStateExt wExt, com.android.server.wm.WindowState w, boolean topFullScreen, int appModeId, float maxRefreshRate) {
    }

    default int getPreferredModeId(float preferredRefreshRate, int candidateModeId) {
        return candidateModeId;
    }

    default float getPreferredMaxRefreshRate(float preferredMaxRefreshRate) {
        return preferredMaxRefreshRate;
    }

    default float getPreferredMinRefreshRate(float preferredMinRefreshRate) {
        return preferredMinRefreshRate;
    }

    default void addRefreshRateRangeForPackage(java.lang.String packageName, float minRefreshRate, float maxRefreshRate) {
    }

    default void removeRefreshRateRangeForPackage(java.lang.String packageName) {
    }

    default void checkWindowRefreshRateChange() {
    }

    default void notifyIMELayoutChanged(boolean imeVisible, int imeTop, int imeBottom) {
    }

    default void hookPerformLayout(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowState w, com.android.server.wm.WindowState mCurrentFocus) {
    }

    default com.android.server.wm.DisplayPolicy createDisplayPolicy(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        return new com.android.server.wm.DisplayPolicy(service, displayContent);
    }

    default void onPointerEventForTheia(android.view.MotionEvent ev) {
    }

    default void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
    }

    default boolean performLayoutNoTrace(com.android.server.wm.DisplayPolicy displayPolicy, com.android.server.wm.DisplayFrames mDisplayFrames, int uiMode) {
        return false;
    }

    default void onFindFocusedWindow() {
    }

    default boolean shouldBlockUpdateOrientationDuringFixedRotation(com.android.server.wm.WindowContainer orientationSource, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean doRotationAnimation(com.android.server.wm.ActivityRecord r) {
        return true;
    }

    default boolean isReturnNoCutoutForFullScreenDisplay(int rotation, com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default int correctRotationParam(int rotation) {
        return rotation;
    }

    default boolean dontDoFixedRotatinAnimation(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default boolean handleNonOccludesParentActiviy(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default void dispatchWallpaperVisibility(boolean visible) {
    }

    default boolean shouldSkipUnFreezeCheck(com.android.server.wm.WindowState window) {
        return false;
    }

    default void disableStatusBarForSystem(com.android.server.wm.WindowManagerService wms, com.android.server.wm.WindowState ws) {
    }

    default boolean updateOrientation(android.content.res.Configuration configuration, android.content.res.Configuration tmpConfiguration, com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.WindowContainer freezeDisplayWindow, com.android.server.wm.DisplayRotation displayRotation) {
        return false;
    }

    default void positionAnimation(com.android.server.wm.ActivityRecord activityRecord1, com.android.server.wm.ActivityRecord activityRecord2) {
    }

    default boolean isAnimating(com.android.server.wm.WindowToken prevRotatedLaunchingApp, com.android.server.wm.DisplayRotation displayRotation) {
        return prevRotatedLaunchingApp.isAnimating(3);
    }

    default boolean deferChangeTarget(boolean updateImeTarget, com.android.server.wm.WindowState newTarget, com.android.server.wm.WindowState currentTarget, com.android.server.wm.WindowState inputMethodWindow, com.android.server.wm.InsetsControlTarget imeControlTarget) {
        return false;
    }

    default boolean updateRotationUnchecked(boolean b) {
        return false;
    }

    default void savedSurface(com.android.server.wm.WindowManagerService windowManagerService) {
    }

    default void debugForBootTime(java.lang.String tag, com.android.server.wm.WindowState state) {
    }

    default void setFixedRotationForScreenshot(com.android.server.wm.ActivityRecord r, int rotation) {
    }

    default void beginHookUpdateFocusedWindowLocked(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowState newFocus, com.android.server.wm.WindowManagerService windowManagerService) {
    }

    default void endHookUpdateFocusedWindowLocked(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowManagerService windowManagerService, int topFocusedDisplayId) {
    }

    default void mayAddFloatingWindow(com.android.server.wm.WindowState w) {
    }

    default void hookOnDisplayChanged(com.android.server.wm.DisplayContent dc) {
    }

    default void updateWindowTapExcludeRegion(com.android.server.wm.DisplayContent dc, android.graphics.Region mTouchExcludeRegion) {
    }

    default void getScaleBound(com.android.server.wm.Task task, android.graphics.Rect mTmpRect) {
    }

    default boolean setFocusedAppToNormalWindow(com.android.server.wm.ActivityRecord focusedApp, com.android.server.wm.WindowState w) {
        return false;
    }

    default boolean shouldCancelRecentAnimation(com.android.server.wm.DisplayContent displayContent) {
        return true;
    }

    default void hookPrepareSurfaces(com.android.server.wm.DisplayContent dc, android.view.SurfaceControl.Transaction transaction) {
    }

    default void hookAdjustForImeIfNeeded(com.android.server.wm.WindowState win, boolean isVisible, int imeHeight, com.android.server.wm.WindowState inputTarget, com.android.server.wm.WindowState focusWindow) {
    }

    default void applyRotation(int oldRotation, int newRotation, int displayId) {
    }

    default boolean onGoingToSleep(int displayId) {
        return true;
    }

    default boolean hasGestureAnimationController() {
        return false;
    }

    default void checkSetFixedRotationLaunchingApp(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord orientationSource) {
    }

    default boolean startKeyguardExitOnNonAppWindows(com.android.server.wm.WindowState w, boolean onWallpaper, boolean goingToShade, boolean subtle) {
        return false;
    }

    default boolean imeTargetIsMainWindow(com.android.server.wm.WindowState imeTarget) {
        return false;
    }

    default boolean suggestUseFixedRotationAnimation(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default void setAnimationThreadUx(boolean applyToUx, boolean animating, int type) {
    }

    default boolean shouldExitFixedRotation(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default void updateRotation(com.android.server.wm.DisplayContent displayContent, boolean processTasks) {
    }

    default boolean updateImeTarget(com.android.server.wm.WindowState windowState) {
        return true;
    }

    default boolean shouldReviseScreenOrientationForApp(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default int getFixedScreenOrientation(com.android.server.wm.WindowContainer container, int originOrientation) {
        return originOrientation;
    }

    default int getFixedScreenOrientationForFixedRotation(com.android.server.wm.ActivityRecord act, int originOrientation) {
        return originOrientation;
    }

    default boolean hasAdjacentTaskFragmentInActivityEmbeddedState(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void scheduleFoldableDeviceDisplaySwitch(boolean isDefaultDisplay, com.android.server.wm.ActivityTaskManagerService atmService, com.android.server.wm.WindowManagerService mWmService, android.view.DisplayAddress address) {
    }

    default boolean skipAppTransitionAnimation() {
        return false;
    }

    default boolean shouldFixOrientationForSplashScreen(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default int getSplitRequestedOrientation() {
        return -2;
    }

    default boolean isActivityPreloadDisplay(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default void setRotationChange(com.android.server.wm.DisplayContent displayContent, boolean start) {
    }

    default void physicalDisplayChanged(com.android.server.wm.DisplayContent displayContent) {
    }

    default boolean shouldNotWaitForDisplayOnBoot(com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean skipDeferOrientationChangeForEnteringPipFromFullScreen() {
        return false;
    }

    default boolean shouldDisplayRotated(int rotation, java.lang.String displayName, com.android.server.wm.DisplayContent displayContent) {
        return rotation == 1 || rotation == 3;
    }

    default boolean shouldPuttFixedRotation(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default boolean shouldDelayOrientationChangeForCanvas(com.android.server.wm.ActivityRecord activityRecord) {
        return false;
    }

    default void setPuttDisplay(boolean puttDisplay) {
    }

    default boolean isPuttDisplay() {
        return false;
    }

    default void triggerIntoComapct() {
    }

    default boolean isZoomWindowMode(com.android.server.wm.WindowContainer container) {
        return false;
    }

    default boolean isMiniZoomWindow(com.android.server.wm.ActivityRecord newFocus) {
        return false;
    }

    default boolean isFlexibleTaskAndAdjustIme(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void onShowImeRequested() {
    }

    default void setSecondDefaultDisplay(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowManagerService mWmService) {
    }

    default boolean waitingPhysicalDisplayChanged(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default void physicalDisplayChangedAfterConfig(com.android.server.wm.DisplayContent displayContent) {
    }

    default void displayChangeToOn() {
    }

    default boolean HasZoomWindowAboveImeInputTarget(com.android.server.wm.WindowState imeLayeringTarget, com.android.server.wm.InputTarget imeInputTarget, com.android.server.wm.WindowContainer windowContainer) {
        return false;
    }

    default boolean forceUpdateGestureExclusion(com.android.server.wm.WindowState w) {
        return false;
    }

    default void setSensorUpdateWaiting(boolean sensorUpdateWaiting) {
    }

    default boolean isSensorUpdateWaiting() {
        return false;
    }

    public interface IGestureStaticExt {
        default boolean isWinHasGestureExclusionRestrictions(com.android.server.wm.WindowState win) {
            return true;
        }
    }

    default void notifyInsetsChangedLw() {
    }

    default int adjustConstantSystemGestureExclusionLimitDp(int originLimitDp, com.android.server.wm.DisplayContent dc) {
        return originLimitDp;
    }

    default boolean supportDesktopModeOnExternalDisplays(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default boolean isSecondDisplay(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default void setLastImeLayeringTarget(com.android.server.wm.WindowState target) {
    }

    default void removeImeSurfaceImmediately(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowState target) {
    }

    default void onDisplayFocusedAppChanged(com.android.server.wm.DisplayContent dc, com.android.server.wm.ActivityRecord newFocus) {
    }

    default java.lang.Object getFlexibleActivityImeAnimationState() {
        return null;
    }

    default void setFlexibleActivityImeAnimationState(java.lang.Object animationState) {
    }

    default boolean isSupportedIMEOnSecondDisplay(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default boolean isCommercialVersion() {
        return false;
    }

    default void hookForComplexScene(com.android.server.wm.WindowState windowState, boolean updateInputWindows) {
    }

    default void setForcedDisplayInfoForWmSize(int width, int height, int density, int userId, com.android.server.wm.WindowManagerService windowManagerService) {
    }

    default boolean skipTransitionAnimationIfNeed(int changes, android.window.TransitionRequestInfo.DisplayChange displayChange, com.android.server.wm.TransitionController tc, com.android.server.wm.ActivityRecord focusedApp) {
        return false;
    }

    default void adjustScreenConfigurationForCarLink(com.android.server.wm.DisplayContent displayContent, android.content.res.Configuration outConfig, float density) {
    }

    default void adjustDisplayConfig(android.content.res.Configuration configuration) {
    }

    default boolean isTwoScreenShown() {
        return false;
    }

    default boolean requestSeamlessExplicit() {
        return false;
    }

    default boolean isResolutionAnimating() {
        return false;
    }

    default void setResolutionAnimating(boolean animating) {
    }

    default boolean isFoldExternalScreen(com.android.server.wm.RootWindowContainer mRootWindowContainer, int displayId) {
        return false;
    }

    default boolean shouldDisableRecentsTransition(android.window.WindowContainerTransaction t, int type, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean shouldSetFixedRotationForTargetLaunchingApp(com.android.server.wm.ActivityRecord orientationSrc, com.android.server.wm.ActivityRecord target) {
        return true;
    }

    default boolean isMirageDisplay() {
        return false;
    }

    default void onAppTransitionDone() {
    }

    default boolean isProhibitUpdateApp(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default boolean interceptPointerLocationEnable(com.android.server.wm.DisplayContent mDisplayContent) {
        return false;
    }

    default boolean pointWithinAppWindow(int x, int y) {
        return false;
    }

    default void requestTraversalWhenAsyncRotationFinished() {
    }

    default void hideInputMethodMenuIfNeed(com.android.server.wm.WindowState prevTarget, com.android.server.wm.WindowState newTarget) {
    }

    default boolean shouldImeAttachedToApp(com.android.server.wm.WindowState imeTarget) {
        return true;
    }

    default boolean shouldAssignRelativeLayerForIme(com.android.server.wm.WindowState imeTarget) {
        return true;
    }

    default boolean shouldNotShowImeScreenshot(com.android.server.wm.WindowState imeTarget) {
        return false;
    }

    default boolean isActivityInTransition(com.android.server.wm.DisplayContent dc, com.android.server.wm.ActivityRecord r, boolean checkOpening) {
        return false;
    }

    default boolean shouldDeferRotationForFixedRotation(com.android.server.wm.WindowContainer fixedRotationApp) {
        return false;
    }

    default void setIsFixedRotationBlocked(boolean isFixedRotationBlocked) {
    }

    default boolean needPendingTransactionForMirage() {
        return false;
    }

    default boolean isShowStartingSurfaceLocked(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default void startAsyncRotationIfNeeded() {
    }

    default void updateSurfacePosition(boolean removePrepareSurfaceInPlacement, com.android.server.wm.WindowState w) {
    }

    default boolean getTopRunningArForInterruptIfNeeded(com.android.server.wm.ActivityRecord ar) {
        return false;
    }

    default void enableWaitDrawnForCamera(boolean wait) {
    }

    default boolean isWaitDrawnForCamera() {
        return false;
    }

    default boolean hookFocusWindowInQuickBack(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, com.android.server.wm.WindowState findFocusWin) {
        return false;
    }

    default boolean isLauncherActivity(com.android.server.wm.ActivityRecord r) {
        return false;
    }

    default com.android.server.wm.ActivityRecord getFixedRotationAppForMirage() {
        return null;
    }

    default void onPreReady() {
    }

    default boolean isToHomeAnimationPlaying(com.android.server.wm.TransitionController transitionController) {
        return false;
    }

    default void setLightOSFadeAnimController(java.lang.Object controller) {
    }

    default java.lang.Object getLightOSFadeAnimController() {
        return null;
    }

    default void forceStartAsyncRotationIfNeed(com.android.server.wm.Transition transition) {
    }

    default java.lang.Runnable getAsyncRotationStartRunnable() {
        return null;
    }

    default void setDisplayChanged(boolean changed) {
    }

    default boolean isDisplayChanged() {
        return false;
    }

    default void linkFixedRotationTransform(com.android.server.wm.ActivityRecord ar, int rotation) {
    }
}
