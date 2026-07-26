package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowManagerServiceExt {
    public static final java.lang.String SPECIAL_HANDLING_WIN = "android.view.cts/android.view.cts.HandleConfigurationActivity";

    default void oplusOnInitReady(android.content.Context context) {
    }

    default boolean canShowInLockDeviceMode(int attrType) {
        return true;
    }

    default boolean shouldCancelRelayout(com.android.server.wm.WindowState win, int oldVisibility, int viewVisibility) {
        return false;
    }

    default void clearSavedSurfaceIfNeeded(com.android.server.wm.WindowState win, java.util.ArrayList<com.android.server.wm.WindowState> mDestroySurface, int flags, boolean forceClear) {
    }

    default boolean shouldWindowSurfaceSaved(com.android.server.wm.WindowState w, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean isWindowSurfaceSaved(com.android.server.wm.WindowState w) {
        return false;
    }

    default boolean isResidentWindowSurface(com.android.server.wm.WindowState w) {
        return false;
    }

    default void beginHookscreenTurningOff() {
    }

    default int adjustDensityForUser(int density, int userId) {
        return density;
    }

    default void hookDisplayReady(com.android.server.wm.WindowManagerService wms, android.content.Context context) {
    }

    default void endHookperformEnableScreen(com.android.server.wm.WindowManagerService wms, android.content.Context context) {
    }

    default void onSetDensityForUser(int density, int userId) {
    }

    default boolean shouldSkipUnFreezeCheck(com.android.server.wm.WindowState window) {
        return false;
    }

    default void speedWallpaperShowIfNeeded(com.android.server.wm.DisplayContent displayContent) {
    }

    default void setFrozenByUserSwitching(boolean isUserSwitching) {
    }

    default void endHookstopFreezingDisplayLocked(java.lang.String reason) {
    }

    default void handleOplusMessage(android.os.Message msg) {
    }

    default void handleUiModeChanged(java.lang.Boolean isNightMode) {
    }

    default void showCustomizeWatermark(boolean flag, android.content.Context context, com.android.server.wm.DisplayContent displayContent, android.view.SurfaceControl.Transaction mTransaction) {
    }

    default com.android.server.wm.Watermark showWatermark(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.Watermark watermark, android.view.SurfaceControl.Transaction mTransaction) {
        return null;
    }

    default boolean doDumpWindows(java.io.PrintWriter pw, java.lang.String name, java.lang.String[] args, int opti, boolean dumpAll) {
        return true;
    }

    default void setKeyguardExitForNearby(android.content.Context mContext, com.android.server.policy.PhoneWindowManager phoneWindowManager, long tokenHandle, byte[] token) {
    }

    default java.util.ArrayList<com.android.server.wm.WindowState> getDestroySavedSurface() {
        return null;
    }

    default boolean isRotationLockForBootAnimation() {
        return false;
    }

    default void handleAppVisible(com.android.server.wm.ActivityRecord ar) {
    }

    default boolean interceptFloatWindow(com.android.server.wm.WindowManagerService ws, android.content.Context context, com.android.server.wm.WindowState win, boolean keyguardLocked, boolean showDialog) {
        return false;
    }

    default void handleKeyguardGoingAway(boolean keyguardGoingAway) {
    }

    default boolean ignoreFingerprintWindow(android.content.Context context, com.android.server.wm.Task task) {
        return false;
    }

    default void endHookSystemReady() {
    }

    default void hookboost(boolean status) {
    }

    default void hookAddWindowBeforeAttach(int callingUid) {
    }

    default boolean doDump(com.android.server.wm.WindowManagerService wms, java.lang.String cmd, java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti) {
        return false;
    }

    default void enableDefaultLogIfNeed(android.content.Context context) {
    }

    default boolean skipFreezingDisplayIfNeed(com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default void notifySysWindowRotation(java.lang.Class clazz, android.content.ComponentName componentInfo) {
    }

    default void isFingerPrintDimLayerRequestHorizontalLayout(com.android.server.wm.WindowState windowState, int requestedWidth, int requestedHeight, int viewVisibility) {
    }

    default void tryAddActivityToAnimationSourceWhenStartExitingAnimation(com.android.server.wm.WindowState windowState) {
    }

    default boolean isGestureAnimationWapaperTarget(com.android.server.wm.WindowState w) {
        return false;
    }

    default boolean shouldForceStopFreezingScreen() {
        return false;
    }

    default void checkScreenFreezingTimeOut(boolean check) {
    }

    default void pokeDynamicVsyncAnimation(int durationInMs, java.lang.String detail) {
    }

    default void dump(java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default void handleCompactWindowTouchFocusChange(com.android.server.wm.WindowState touchedWindow) {
    }

    default void handleFlexibleLockAppWindow(com.android.server.wm.WindowState touchedWindow) {
    }

    default void updateBracketPanelWindow(com.android.server.wm.WindowState window, boolean add) {
    }

    default void onStopFreezingDisplayLocked() {
    }

    default boolean checkExitingAnimationRationality(com.android.server.wm.WindowState win) {
        return true;
    }

    default void logNoFocusedWindowANRState() {
    }

    default boolean isHomePageOfSettingsTaskFragment(com.android.server.wm.InputTarget newTarget, com.android.server.wm.InputTarget focusedTarget) {
        return false;
    }

    default com.android.server.wm.WindowManagerService getOplusWindowManagerService(android.content.Context context, com.android.server.input.InputManagerService im, boolean showBootMsgs, com.android.server.policy.WindowManagerPolicy policy, com.android.server.wm.ActivityTaskManagerService atm, com.android.server.wm.DisplayWindowSettingsProvider displayWindowSettingsProvider, java.util.function.Supplier<android.view.SurfaceControl.Transaction> transactionFactory, java.util.function.Function<android.view.SurfaceSession, android.view.SurfaceControl.Builder> surfaceControlFactory) {
        return null;
    }

    default boolean shouldShowPresentation(com.android.server.wm.DisplayContent displayContent, java.lang.String packageName) {
        return false;
    }

    default boolean isActivityTypeMultiSearch(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isGestureAnimationTarget(com.android.server.wm.ActivityRecord a) {
        return false;
    }

    default boolean shouldwaitingForFolded() {
        return false;
    }

    default boolean hideForUnFolded(com.android.server.wm.WindowState w) {
        return false;
    }

    default void allWindowsDraw() {
    }

    default void cpuFrequencyBoostIfNeed(com.android.server.wm.ActivityRecord t) {
    }

    default android.view.DisplayInfo getNeedForceSetDensityDisplayInfo(com.android.server.wm.WindowManagerService ws, int curDisplayId, int secDisplayId) {
        return null;
    }

    default boolean shouldSkipCheckWindowDrawn(com.android.server.wm.WindowState win) {
        return false;
    }

    default int extractConfigInfoAndRealFlags(int flags, com.android.server.wm.WindowState win) {
        return flags;
    }

    default boolean isStartingSplitPairFromRecents() {
        return false;
    }

    default boolean currentFoucusWindowModeNotZoomMode(int windowMode) {
        return true;
    }

    default boolean checkOplusWindowPermission(com.android.server.wm.WindowManagerService wms) {
        return false;
    }

    default void addSplitScreenImmersiveFlagIfNeed(com.android.server.wm.WindowState win, android.os.Bundle outBundle) {
    }

    default void onRelayoutWindowForFlexibleWindow(com.android.server.wm.WindowState win, android.view.WindowManager.LayoutParams attrs) {
    }

    default boolean dontWaitDrawForCompactWindow(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean changeFocusForce(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean dontWaitDrawForFlexibleWindow(com.android.server.wm.WindowState win) {
        return false;
    }

    default void clearSkipWaitingForDrawn() {
    }

    default boolean canAddSubWindow(android.content.Context context, android.view.WindowManager.LayoutParams params) {
        return false;
    }

    default boolean isNoCanvasActivity(com.android.server.wm.ActivityRecord touchedActivity) {
        return false;
    }

    default boolean isIMETargetWindowHasFocus(com.android.server.wm.InputTarget target) {
        return true;
    }

    default void addWindow(com.android.server.wm.WindowState win) {
    }

    default void removeWindow(com.android.server.wm.WindowState win) {
    }

    default android.content.res.Configuration hookRegisterWindowContainerListener(com.android.server.wm.DisplayArea<?> da, com.android.server.wm.WindowContextListenerController windowContextListenerController, com.android.server.wm.WindowProcessController wpc, android.os.IBinder clientToken, int type, android.os.Bundle options) {
        return null;
    }

    default boolean isSecondaryhomePackageName(com.android.server.wm.EmbeddedWindowController.EmbeddedWindow win) {
        return false;
    }

    default boolean isFocusChangeWithNonFlexible(com.android.server.wm.WindowState windowState) {
        return false;
    }

    default boolean isFloatingWindow(float displayX, float displayY) {
        return false;
    }

    default void onSystemUiProcessStartedTimeout() {
    }

    default void initOplusWindowTracing(com.android.server.wm.WindowManagerService service, android.view.Choreographer choreographer) {
    }

    default boolean isOplusWindowTracingEnable() {
        return false;
    }

    default void startOplusWindowTracing() {
    }

    default void stopOplusWindowTracing(java.io.File dumpFile) {
    }

    default boolean isOplusWmProtoLogEnable() {
        return false;
    }

    default void startOplusWmProtoLog() {
    }

    default void stopOplusWmProtoLog(java.io.File dumpFile) {
    }

    default void logOplusWindowTracingState(java.lang.String where) {
    }

    default void onPowerKeyDown(boolean isScreenOn) {
    }

    default boolean isShouldInterceptEnterPip() {
        return false;
    }

    default void setShouldInterceptEnterPip(boolean b) {
    }

    default java.util.function.Supplier<android.view.SurfaceControl.Transaction> getLockedTransactionFactory() {
        return null;
    }

    default void notifyTouchAppChange(java.lang.String pkgName) {
    }

    default void notifyTouchOutsideFocus(com.android.server.wm.InputTarget t) {
    }

    default boolean isInNotificationbpList(java.lang.String pkgName) {
        return false;
    }

    default boolean getBfsKeyAllowEvents(android.content.Context context) {
        return false;
    }

    default boolean waitDrawForCameraVolumeQuickLaunch() {
        return false;
    }

    default boolean isClickAtPocketStudioArea(int displayId, int rowX, int rowY) {
        return false;
    }

    default boolean deferPerformSurfacePlacement(com.android.server.wm.WindowState win) {
        return false;
    }
}
