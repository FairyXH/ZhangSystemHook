package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowStateExt {
    default boolean getAppOpVisibility() {
        return false;
    }

    default boolean isDisplayCompat() {
        return false;
    }

    default boolean isDisplayCompat(java.lang.String packageName, int uid) {
        return false;
    }

    default void setDisplayCompat(boolean displayCompat) {
    }

    default boolean isDisplayHideFullscreenButtonNeeded() {
        return false;
    }

    default void setmDisplayHideFullscreenButton(boolean displayHideFullscreenButton) {
    }

    default void setTrustedOverlay(boolean trustedOverlay) {
    }

    default boolean isTrustedOverlay() {
        return false;
    }

    default boolean isVisibleLw() {
        return false;
    }

    default void updateWindowState(com.android.server.wm.WindowState windowState, com.android.server.wm.Session mSession, com.android.server.wm.WindowStateAnimator mWinAnimator, int windowType, boolean isVisible) {
    }

    default void wakeupInPrepareWindowToDisplayDuringRelayout(java.lang.String appTitleAttrs) {
    }

    default boolean isInSkipWaitingForDrawn(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean isLastInputmethodShow() {
        return false;
    }

    default void setInputmethodShow(boolean lastInputmethodShow) {
    }

    default void notifyWindowStateChange(android.os.Bundle options) {
    }

    default void initColorDisplayCompat(java.lang.String packageName, com.android.server.wm.WindowState windowState) {
    }

    default void updateOrientationChangeIfNeeded(com.android.server.wm.WindowState win, com.android.server.wm.ActivityRecord record, com.android.server.wm.WindowManagerService mWmService) {
    }

    default boolean canShowInLockDeviceMode(int type) {
        return true;
    }

    default boolean isNotReadyForDisplayDuringFixedRotation(com.android.server.wm.WindowState target, com.android.server.wm.DisplayContent displayContent, android.graphics.Rect frame) {
        return false;
    }

    default boolean checkIfHasDrawn(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void needResetDrawStateOnResize(com.android.server.wm.WindowState target, android.graphics.Rect mFrame) {
    }

    default boolean checkIfWindowingModeZoom(int windowingMode) {
        return false;
    }

    default boolean hookCanReceiveKeys(int type, com.android.server.wm.WindowStateAnimator mWinAnimator) {
        return false;
    }

    default boolean canInitAppOpVisibilityLw(java.lang.String pkgName, int uid, int pid) {
        return true;
    }

    default boolean canSetAppOpVisibilityLw(java.lang.String owningPackage, int owningUid) {
        return true;
    }

    default boolean isAttachSurfaceView() {
        return false;
    }

    default void setAttachSurfaceView(boolean hasSurfaceView) {
    }

    default boolean shouldDeferCallOnFirstWindowDrawn(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void putSnapshotWhenStartingWindowExit(int type, boolean mRemoveOnExit, com.android.server.wm.WindowState windowState) {
    }

    default void notifyImeWindowStateChange(boolean hasShow, com.android.server.wm.WindowState windowState) {
    }

    default boolean isOplusTrustedWindow(android.view.WindowManager.LayoutParams attrs) {
        return false;
    }

    default boolean canOverlayWindows() {
        return false;
    }

    default void attach(com.android.server.wm.WindowState windowState) {
    }

    default void removeImmediately(com.android.server.wm.WindowState windowState) {
    }

    default void dispatchWallpaperVisibility(boolean visible) {
    }

    default boolean blockSeamlesslyRotateForFingerPrintWindow(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void setHideByKeyguardExitAnim(boolean hideByKeyguardExitAnim) {
    }

    default boolean getHideByKeyguardExitAnim() {
        return false;
    }

    default void onNonAppSurfaceVisibilityChanged(boolean shown) {
    }

    default void cancelFadeAnimationIfNeed(com.android.server.wm.WindowState windowState) {
    }

    default boolean startAnimationWithRoundedCorners(com.android.server.wm.WindowState windowState, android.view.animation.Animation animation, android.graphics.Point surfacePosition, android.graphics.Rect frame) {
        return false;
    }

    default void onDisplayChanged(com.android.server.wm.DisplayContent curDisplay, com.android.server.wm.DisplayContent prevDisplay, com.android.server.wm.WindowState win) {
    }

    default boolean isOnMirageDisplay(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean isMirageDisplay(int displayId) {
        return false;
    }

    default boolean isWallpaperShow(com.android.server.wm.WindowToken token) {
        return false;
    }

    default boolean canApplyDimInEmbedding(com.android.server.wm.WindowState win) {
        return true;
    }

    default boolean shouldSyncWithBuffersIfNeeded(com.android.server.wm.WindowState win) {
        return false;
    }

    default void setFixedScreenOrientation(int value) {
    }

    default int getFixedScreenOrientation() {
        return -1;
    }

    default boolean isCompactWindowingMode(int windowingMode) {
        return false;
    }

    default boolean isNotFullScreenCompactWindow(com.android.server.wm.WindowState w) {
        return false;
    }

    default void createCompactDimmer(com.android.server.wm.WindowState windowState) {
    }

    default android.graphics.Rect layoutInFullScreen(com.android.server.wm.WindowState windowState, android.graphics.Rect rect) {
        return rect;
    }

    default void updateCompactStartingWindowFrames(com.android.server.wm.WindowState windowState, android.graphics.Rect layoutContainingFrame, android.graphics.Rect layoutDisplayFrame) {
    }

    default boolean supportTransWindowAnim(com.android.server.wm.WindowState windowState, com.android.server.wm.WindowFrames windowFrames) {
        return false;
    }

    default void resizeTouchRegionForCompactWindow(com.android.server.wm.ActivityRecord mActivityRecord, com.android.server.wm.WindowFrames mWindowFrames, android.graphics.Region region, com.android.server.wm.WindowState windowState) {
    }

    default void resizeTouchableRegionForBracketMode(android.graphics.Region region, com.android.server.wm.ActivityRecord activityRecord, com.android.server.wm.WindowState windowState) {
    }

    default void resizeTouchableRegionForBracketPanelWindow(android.graphics.Region region, com.android.server.wm.WindowState windowState) {
    }

    default void resizeExpandTouchRegionForWindowState(android.graphics.Region region, com.android.server.wm.WindowState windowState, android.view.WindowManager.LayoutParams attrs) {
    }

    default void registerRemoteAnimationsExt(android.view.RemoteAnimationDefinition definition) {
    }

    default void unregisterRemoteAnimationsExt() {
    }

    default android.view.RemoteAnimationDefinition getRemoteAnimationDefinitionExt() {
        return null;
    }

    default void setOplusLaunchViewInfo(java.lang.Object launchViewInfo) {
    }

    default java.lang.Object getOplusLaunchViewInfo() {
        return null;
    }

    default boolean letterBoxEnabledForCompactWin(com.android.server.wm.WindowState windowState) {
        return true;
    }

    default boolean shouldBlockWindowMoveAnimation(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default boolean shouldShowLetterboxUi(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void removeStartingBackColorLayerIfNeed(com.android.server.wm.WindowState win) {
    }

    default boolean prepareSurfaces(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default boolean toUpdateCompactDimmer(com.android.server.wm.WindowState ws) {
        return false;
    }

    default boolean shouldAddSettingsWindowToA11y(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default boolean needLetterBoxSurface(boolean surfaceReady, com.android.server.wm.ActivityRecord target, com.android.server.wm.WindowState mainWindow) {
        return false;
    }

    default void onWindowStateCreated(com.android.server.wm.WindowState windowState) {
    }

    default void resizeTouchableRegionInOplusCompatMode(com.android.server.wm.WindowState windowState, android.graphics.Region region) {
    }

    default void resizeTouchRegionForSpecial(com.android.server.wm.ActivityRecord record, com.android.server.wm.WindowFrames frames, android.graphics.Region region, com.android.server.wm.WindowState windowState) {
    }

    default boolean forceUpdateWallpaperOffset(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default boolean hideForUnFolded(com.android.server.wm.WindowState w) {
        return false;
    }

    default boolean syncEmbeddedWindowDrawStateIfNeeded(com.android.server.wm.WindowState mWin) {
        return false;
    }

    default boolean getWindowRelayoutFlag() {
        return false;
    }

    default void setWindowRelayoutFlag(boolean value) {
    }

    default void expandFingerPrintDimLayerSurface(com.android.server.wm.WindowState windowState, boolean displayFrozen) {
    }

    default android.view.InsetsState getCompatInsetsStateForSplit(com.android.server.wm.WindowState win, android.view.InsetsState state) {
        return state;
    }

    default boolean shouldSkipResizeWindow(com.android.server.wm.WindowState win) {
        return false;
    }

    default void setCurrentLaunchCanTurnScreenOn(com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void setSimultaneousDisplayState(boolean state) {
    }

    default boolean canBeImeTarget(com.android.server.wm.WindowState win) {
        return true;
    }

    default void changeStartingWindowParentBounds(com.android.server.wm.WindowState windowState, android.graphics.Rect bounds) {
    }

    default boolean hookRequestDrawIfNeeded(com.android.server.wm.WindowState windowState, int type, java.util.List<com.android.server.wm.WindowState> outWaitingForDrawn) {
        return false;
    }

    default boolean getDeviceFolding() {
        return false;
    }

    default boolean shouldSkipFreezingWhenFolding(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void setLastFinishDrawDp(int lastFinishDrawDp) {
    }

    default int getLastFinishDrawDp() {
        return 0;
    }

    default void setLastClientRotation(int rotation) {
    }

    default int getLastClientRotation() {
        return -1;
    }

    default boolean adjustPosForComapctWindow(com.android.server.wm.WindowState window) {
        return false;
    }

    default boolean isCompactScaledWindowingMode(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default float getCompactScaled(com.android.server.wm.WindowState windowState) {
        return -1.0f;
    }

    default void updateSFPosWithNextSync() {
    }

    default boolean isLogToolRun() {
        return false;
    }

    default boolean translateTouchableRegionInOplusCompatMode(com.android.server.wm.WindowState windowState, android.graphics.Region region) {
        return false;
    }

    default boolean finishDrawingApplyPostDraw(com.android.server.wm.WindowState w, android.view.SurfaceControl.Transaction postDrawTransaction) {
        return false;
    }

    default boolean finishDrawing(boolean skipLayout) {
        return skipLayout;
    }

    default void notifyGameFloatWindowVisibility(boolean hasShow, com.android.server.wm.WindowState windowState) {
    }

    default boolean isIgnoreImeTargetBottomOverlapFlexibleTask(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean isFlexibleTaskInTransitionAnimation(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void setLayoutFullscreenInEmbeddingIfNeed() {
    }

    default boolean layoutFullscreenInEmbedding() {
        return false;
    }

    default void adjustTouchableRegionInActivityEmbedding(com.android.server.wm.WindowState windowState, android.graphics.Rect region) {
    }

    default boolean isNoMoveAnimationOnFlexibleWindow() {
        return false;
    }

    default boolean isFlexibleWindowMinimized() {
        return false;
    }

    default android.view.InsetsState hookGetInsetsState(android.view.InsetsState originInsetsState, boolean includeTransient) {
        return originInsetsState;
    }

    default void onDisplayChangedEnd(com.android.server.wm.InputWindowHandleWrapper inputWindowHandle) {
    }

    default android.view.InsetsState hookGetCompatInsetsState(android.view.InsetsState originInsetsState) {
        return originInsetsState;
    }

    default float hookOverrideScale(com.android.server.wm.WindowState w, com.android.server.wm.WindowState parent, float defaulOverrideScale) {
        return defaulOverrideScale;
    }

    default void updateAttrsBeforeCompute(android.view.WindowManager.LayoutParams attr) {
    }

    default void cancelSplashScreenAnimation(com.android.server.wm.WindowState win) {
    }

    default void cancelFlexibleAppInnerScreenAnimationIfNeed(com.android.server.wm.WindowState win) {
    }

    default void updateSurfacePosition(android.graphics.Point surfacePosition) {
    }

    default boolean wallpaperSeamlesslyRotate(com.android.server.wm.WindowState win) {
        return false;
    }

    default void performShowLocked(com.android.server.wm.WindowState windowState) {
    }

    default boolean isSyncFinished(com.android.server.wm.WindowState windowState, int syncState, com.android.server.wm.IWindowManagerServiceExt wmsExt, java.util.ArrayList<com.android.server.wm.WindowState> mDestroySurface) {
        return false;
    }

    default boolean isMinimizedPocketStudio() {
        return false;
    }

    default boolean cannotBeImeTarget() {
        return false;
    }

    default boolean cannotRelativeLayeringToIme() {
        return false;
    }

    default void onWindowStateHasDrawn(com.android.server.wm.WindowState win) {
    }

    default boolean shouldUpdateWinPos(com.android.server.wm.WindowFrames windowFrames) {
        return false;
    }

    default boolean isVisibleRequestedForActivity(com.android.server.wm.ActivityRecord record, com.android.server.wm.WindowToken windowToken) {
        return false;
    }

    default boolean inRemapViceDisplay(com.android.server.wm.WindowState win) {
        return false;
    }

    default void addExcludeLayers(android.view.SurfaceControl sc) {
    }

    default void removeExcludeLayers(android.view.SurfaceControl sc) {
    }

    default java.util.ArrayList<android.view.SurfaceControl> getExcludeLayers() {
        return null;
    }

    default void clearExcludeLayers(com.android.server.wm.WindowState win) {
    }

    default boolean providesDisplayDecorInsetsForTaskbar(com.android.server.wm.WindowState windowState) {
        return true;
    }

    default void applyCornersAndShadowIfNeed(android.view.SurfaceControl.Transaction syncTransaction) {
    }

    default void resetRecordViewCornersAndShadow() {
    }

    default boolean shouldRelativeLayerInSplitScreenMode(com.android.server.wm.WindowState win) {
        return true;
    }

    default void onDisplayImeChanged(com.android.server.wm.DisplayContent curDisplay, com.android.server.wm.DisplayContent prevDisplay, com.android.server.wm.WindowState win) {
    }

    default boolean getInputShowStatus() {
        return false;
    }

    default void onSecurityPageFlagChanged(com.android.server.wm.WindowState win, boolean surfaceShown, boolean isFromSetSecure) {
    }

    default boolean skipReportDrawWallpaper() {
        return false;
    }

    default void setWallpaperScaleValue(float wallpaperScaleValue) {
    }

    default boolean shouldSkipShowWindow(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default float getWallpaperScaleValue() {
        return -1.0f;
    }

    default boolean needMaintainVisibleSate(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default void setSkipUpdateWallpaperPosition(boolean skipWallpaperPosition) {
    }

    default boolean skipUpdateWallpaperPosition() {
        return false;
    }

    default boolean shouldOrderLayerToImeInTablet(com.android.server.wm.ActivityRecord record) {
        return false;
    }

    default boolean isSyncFinishedDrawing(com.android.server.wm.WindowState win, int drawState) {
        return true;
    }

    default boolean loggingWhenFolding() {
        return false;
    }

    default void setRequestedVisibleTypes(com.android.server.wm.DisplayContent dc) {
    }

    default void dynamicFrameRateStartAnimForDialog(android.view.SurfaceControl sc) {
    }

    default void dynamicFrameRateFinishAnimForDialog(android.view.SurfaceControl sc) {
    }

    default void dispatchFlexibleTaskFrameChange(android.os.Bundle bundle) {
    }

    default void setResidentWindowSurface(boolean b) {
    }

    default boolean isResidentWindowSurface() {
        return false;
    }

    default void setCopySplashScreenFinish(boolean finish) {
    }

    default boolean isCopySplashScreenFinish() {
        return false;
    }

    default boolean checkCachedSurfaceBufferRelease(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default boolean inKeyguardAppearingTransit() {
        return false;
    }

    default boolean forcePlayMoveAnimation(com.android.server.wm.WindowState win) {
        return false;
    }

    default void calculateForceUpdateWallpaperPosition() {
    }

    default void setForceUpdateWallpaperPosition(boolean isForce) {
    }

    default boolean isForceUpdateWallpaperPosition() {
        return false;
    }

    default void hookSetBinderUxFlag(int pid, int flag) {
    }

    default boolean hasWallpaperFrameOrConfigChanged(com.android.server.wm.WindowState win, android.window.ClientWindowFrames frame, android.util.MergedConfiguration configuration) {
        return false;
    }

    default boolean shouldRelativeLayerToImeInCompactWindow(com.android.server.wm.WindowState win, com.android.server.wm.WindowState imeTarget) {
        return true;
    }

    default void setWindowShownAnimation(android.view.animation.Animation animation) {
    }

    default android.view.animation.Animation getWindowShownAnimation() {
        return null;
    }

    default void setAnimationOnShow(boolean show) {
    }

    default boolean isAnimationOnShow() {
        return false;
    }

    default boolean isWindowShownAnimationLeash(android.view.SurfaceControl leash) {
        return false;
    }

    default boolean hasFullSubWinOnLauncher(com.android.server.wm.WindowState win) {
        return false;
    }
}
