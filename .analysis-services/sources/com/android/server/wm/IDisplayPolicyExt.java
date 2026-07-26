package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayPolicyExt {
    default void adjustOppoWindowFrame(android.graphics.Rect pf, android.graphics.Rect df, android.view.WindowManager.LayoutParams attrs, com.android.server.wm.DisplayFrames displayFrames) {
    }

    default boolean isSpecialAppWindow(boolean appWindow, android.view.WindowManager.LayoutParams attrs) {
        return appWindow;
    }

    default boolean updateSpecialSystemBar(android.view.WindowManager.LayoutParams lp) {
        return false;
    }

    default boolean skipSystemUiVisibility(android.view.WindowManager.LayoutParams lp) {
        return false;
    }

    default void initOplusDisplayPolicyEx(com.android.server.wm.WindowManagerService wms, com.android.server.wm.DisplayPolicy dp) {
    }

    default void initOplusDisplayPolicy(com.android.server.wm.DisplayPolicy displayPolicy) {
    }

    default java.lang.Object getOplusDisplayPolicyInner() {
        return null;
    }

    default android.os.Handler createPolicyHandler(android.os.Looper looper, android.os.Handler handler) {
        return handler;
    }

    default int calculateGestureNavInset(int def, int navBarHeight) {
        return def;
    }

    default boolean isKeyboardPositionUp() {
        return false;
    }

    default boolean isNavBarHidden() {
        return false;
    }

    default boolean isNavGestureMode() {
        return false;
    }

    default boolean isHideNavBarGestureMode() {
        return false;
    }

    default void reCalculateNavRectdo(com.android.server.wm.DisplayFrames displayFrames, android.graphics.Rect inOutFrame) {
    }

    default void updateNavigationBarHideState() {
    }

    default void loadGestureBarHeight(android.content.res.Resources res, int portraitRotation, int upsideDownRotation, int landscapeRotation, int seascapeRotation) {
    }

    default void updateDisplayConfig() {
    }

    default boolean isDisableExpendNavBar() {
        return false;
    }

    default boolean isDisableExpendStatusBar() {
        return false;
    }

    default boolean requestGameDockIfNecessary() {
        return false;
    }

    default int getSystemUIFlagAfterGesture(int lastSystemUIFlag) {
        return lastSystemUIFlag;
    }

    default void updateGestureStatus() {
    }

    default void notifyWindowStateChanged(int id, int state, int displayId) {
    }

    default int modifyNaviBar(int res) {
        return res;
    }

    default void updateNavigationFrame(int position, int rotation, android.graphics.Rect navigationFrame, android.graphics.Rect cutoutSafeUnrestricted) {
    }

    default boolean hasRotationLock() {
        return false;
    }

    default boolean isDreamWindow(boolean isDreamWindow) {
        return false;
    }

    default boolean isNeedAdjustDisplayCutoutInsets(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowState win, android.view.WindowManager.LayoutParams attrs) {
        return false;
    }

    default boolean isMinimized(com.android.server.wm.DisplayContent mDisplayContent) {
        return false;
    }

    default boolean isWaitForExitSplit() {
        return false;
    }

    default boolean isSplitTaskVisible(com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default boolean makeStatusBarOpaque(com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default int adjustCutoutModeForWinIfNeed(com.android.server.wm.WindowState win, com.android.server.wm.DisplayContent displayContent, int mode) {
        return mode;
    }

    default boolean isNeedForceShowSystemBarsWhenSplit() {
        return true;
    }

    default void pokeDynamicVsyncAnimation(int durationInMs, java.lang.String detail) {
    }

    default int getNavBarVisibility(boolean isKeyguard, boolean forceShowNavBar, boolean screenOn) {
        return 0;
    }

    default int getLastNavBarVisibility() {
        return 0;
    }

    default void setLastNavBarVisibility(int navBarVisibility) {
    }

    default void setSystemUiVisibility(int navBarVis, com.android.server.wm.WindowState win) {
    }

    default com.android.server.wm.WindowState updateSystemBarWindow(com.android.server.wm.WindowState focusWin, com.android.server.wm.WindowState winCandidate) {
        return winCandidate;
    }

    default boolean opaqueNavBar(com.android.server.wm.WindowState mTopFullscreenOpaqueWindowState) {
        return false;
    }

    default android.graphics.Rect isSettingDialog(com.android.server.wm.WindowState win) {
        return null;
    }

    default boolean judgeWindowModeZoom(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean affectsSystemUiInTransition(com.android.server.wm.WindowState win, int lastAppearance) {
        return false;
    }

    default void onTopFullscreenOpaqueWindowUpdated(com.android.server.wm.DisplayPolicy displayPolicy, com.android.server.wm.WindowState topWindow) {
    }

    default android.graphics.Rect layoutInFullScreen(com.android.server.wm.WindowState win, android.graphics.Rect rect) {
        return rect;
    }

    default boolean adjustNavigationBarToBottom(com.android.server.wm.DisplayContent dc) {
        return false;
    }

    default int getInvalidNavigationBarHeight(int rotation) {
        return 0;
    }

    default void adjustWindowParamsLw(com.android.server.wm.WindowState windowState, android.view.WindowManager.LayoutParams attrs) {
    }

    default void finishScreenTurningOn() {
    }

    default float getZoomCurrentScale() {
        return 1.0f;
    }

    default android.graphics.Rect getZoomRectBeforeShowIME() {
        return new android.graphics.Rect();
    }

    default boolean intersectInCompactWindow(com.android.server.wm.WindowState win, android.graphics.Rect statusBarFrame, android.graphics.Rect tmpRect) {
        return false;
    }

    default boolean restrictFullScreenActivityRectInCompactWindow(com.android.server.wm.WindowState win, int fl, int type, int sysUiFl) {
        return false;
    }

    default boolean shouldNoFocusWindowUpdateSystemBarAttributes(com.android.server.wm.WindowState win) {
        return false;
    }

    default void updateBottomGestureAdditionalInset() {
    }

    default void updateFrameProvider(com.android.server.wm.DisplayFrames displayFrames, com.android.server.wm.WindowState win, android.graphics.Rect inOutFrame, android.view.WindowManager.LayoutParams lp, android.view.InsetsFrameProvider ifp) {
    }

    default boolean canBeTopFullscreenOpqWin(com.android.server.wm.WindowState win) {
        return true;
    }

    default boolean isFlexibleTaskIgnoreSysBar(com.android.server.wm.WindowState win) {
        return false;
    }

    default void updateTaskBarAppearanceIfNeed(com.android.server.wm.WindowState win) {
    }

    default int adjustAppHeightForCarDockBar(com.android.server.wm.DisplayContent displayContent, int displayHeight, int rotation) {
        return displayHeight;
    }

    default boolean checkWindowForSimulateLayoutDisplay(com.android.server.wm.WindowState win) {
        return true;
    }

    default boolean isInPocketStudio(int displayId) {
        return false;
    }

    default void handleSwipeUpFromBottom() {
    }

    default void addTaskBar(com.android.server.wm.WindowState win, boolean addWindow) {
    }

    default com.android.server.wm.WindowState getTaskBar() {
        return null;
    }

    default int getTaskBarHeight() {
        return 0;
    }

    default void getTaskBarDecorInsets(com.android.server.wm.WindowState navigationBar, int rotation, android.graphics.Rect ConfigInsets) {
    }

    default int getTaskBarPolicy(com.android.server.wm.WindowState navigationBar, int rotation) {
        return 0;
    }

    default void setTaskBarAppBoundsIfNeed(android.content.res.Configuration resolvedConfig, android.content.res.Configuration newParentConfig, int hookAppBounds) {
    }

    default android.view.InsetsState adjustTaskBarInsetsState(android.view.InsetsState state, com.android.server.wm.WindowState win, int hookAppBounds) {
        return state;
    }

    default android.os.Handler getOplusUIHandler(android.os.Handler handler) {
        return handler;
    }

    default boolean isForceShowNavbar() {
        return false;
    }

    default void setForceShowNavbar(boolean force) {
    }

    default int getBootInvalidNavigationBarHeight(int rotation) {
        return 0;
    }
}
