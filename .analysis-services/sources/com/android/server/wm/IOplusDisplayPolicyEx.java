package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusDisplayPolicyEx extends android.common.IOplusCommonFeature {
    public static final com.android.server.wm.IOplusDisplayPolicyEx DEFAULT = new com.android.server.wm.IOplusDisplayPolicyEx() { // from class: com.android.server.wm.IOplusDisplayPolicyEx.1
    };
    public static final java.lang.String NAME = "IOplusDisplayPolicyEx";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusDisplayPolicyEx;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean isNavGestureMode() {
        return false;
    }

    default boolean isHideNavBarGestureMode() {
        return false;
    }

    default void updateNavigationBarHideState() {
    }

    default boolean isNavBarHidden() {
        return false;
    }

    default void loadGestureBarHeight(android.content.res.Resources res, int portraitRotation, int upsideDownRotation, int landscapeRotation, int seascapeRotation) {
    }

    default void updateDisplayConfig() {
    }

    default int caculateDisplayFrame(int position, android.graphics.Rect cutoutSafeUnrestricted, int def) {
        return def;
    }

    default int calculateGestureNavInset(int def, int navBarHeight) {
        return def;
    }

    default boolean isKeyboardPositionUp() {
        return false;
    }

    default void updateFrameProvider(com.android.server.wm.DisplayFrames displayFrames, com.android.server.wm.WindowState windowState, android.graphics.Rect inOutFrame, android.view.WindowManager.LayoutParams lp, android.view.InsetsFrameProvider ifp) {
    }

    default void reCalculateNavRectdo(com.android.server.wm.DisplayFrames displayFrames, android.graphics.Rect navFrame) {
    }

    default void updateNavigationFrame(int position, int rotation, android.graphics.Rect navigationFrame, android.graphics.Rect cutoutSafeUnrestricted) {
    }

    default void updateKeyboardPosition() {
    }

    default void updateKeyboardPosition(boolean inputShow) {
    }

    default void onMayUseInputMethod(boolean useIme) {
    }

    default void updateGestureStatus() {
    }

    default boolean isNavBarImmersive() {
        return false;
    }

    default void setSystemUiVisibility(com.android.server.statusbar.StatusBarManagerInternal statusBar, int displayId, int vis, int fullscreenStackVis, int dockedStackVis, int mask, android.graphics.Rect fullscreenBounds, android.graphics.Rect dockedBounds, boolean isNavbarColorManagedByIme, com.android.server.wm.WindowState window, int navBarVis) {
    }

    default int getNavBarVisibility(boolean isKeyguard, boolean forceShowNavBar, boolean screenOn) {
        return 0;
    }

    default void notifyWindowStateChange(int visibility) {
    }

    default void notifyWindowStateChanged(int id, int state, int displayId) {
    }

    default void systemBarStateChange(int visibility, boolean hideStatusBar, boolean hideNavBar) {
    }

    default boolean isWindowTitleEquals(com.android.server.wm.WindowState win, java.lang.String title) {
        return win != null && win.getAttrs().getTitle().equals(title);
    }

    default int getSysUiFlagsForSplitScreen(com.android.server.wm.WindowState win, int sysUiFlag) {
        return sysUiFlag;
    }

    default boolean isGameDockWindow(com.android.server.wm.WindowState win) {
        return false;
    }

    default void layoutGameDockWindowLw(com.android.server.wm.DisplayFrames displayFrames, android.graphics.Rect cf, android.graphics.Rect of, android.graphics.Rect df, android.graphics.Rect pf) {
    }

    default boolean willSkipSystemUI(java.lang.String title) {
        return false;
    }

    default boolean isIncludedNextPage(com.android.server.wm.WindowState win) {
        return false;
    }

    default boolean isDisableExpendNavBar() {
        return false;
    }

    default boolean isDisableExpendStatusBar() {
        return false;
    }

    default void setForceShowNavbar(boolean force) {
    }

    default boolean isForceShowNavbar() {
        return false;
    }

    default void setSystemUiVisibility(int navBarVis, com.android.server.wm.WindowState window) {
    }
}
