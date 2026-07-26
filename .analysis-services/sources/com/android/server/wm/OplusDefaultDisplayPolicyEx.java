package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class OplusDefaultDisplayPolicyEx implements com.android.server.wm.IOplusDisplayPolicyEx {
    public static final java.lang.String KEY_NAVIGATIONBAR_MODE = "hide_navigationbar_enable";
    public static final java.lang.String KEY_NAV_BAR_HIDE_STATE = "manual_hide_navigationbar";
    public static final java.lang.String KEY_NAV_BAR_IMMERSIVE = "nav_bar_immersive";
    public static final java.lang.String KEY_SWIPE_SIDE_GESTURE_BAR_TYPE = "gesture_side_hide_bar_prevention_enable";
    protected static final int MODE_NAVIGATIONBAR = 0;
    protected static final int MODE_NAVIGATIONBAR_GESTURE = 2;
    protected static final int MODE_NAVIGATIONBAR_GESTURE_SIDE = 3;
    protected static final int MODE_NAVIGATIONBAR_NONE = -1;
    protected static final int MODE_NAVIGATIONBAR_WITH_HIDE = 1;
    public static final int NAV_BAR_HIDE_STATE_HIDE = 1;
    public static final int NAV_BAR_HIDE_STATE_NONE = -1;
    public static final int NAV_BAR_HIDE_STATE_SHOW = 0;
    public static final int SWIPE_SIDE_GESTURE_BAR_TYPE_HIDE = 1;
    public static final int SWIPE_SIDE_GESTURE_BAR_TYPE_SUSPEND = 0;
    public static final java.lang.String SYSTEM_BAR_ID = "systembar_id";
    public static final java.lang.String SYSTEM_BAR_STATE = "systembar_state";
    public static final java.lang.String SYSTEM_DISPLAY_ID = "system_display_id";
    public static final int SYSTEM_UI_FLAG_FOCUS_TOP_OR_LEFT = 64;
    protected static final android.graphics.Rect mTmpNavigationFrameForGesture = new android.graphics.Rect();
    final com.android.server.wm.DisplayPolicy mDisplayPolicy;
    protected int mLastWindowFocusFlags;
    com.android.server.wm.IOplusDisplayPolicyInner mOplusDpInner;
    protected com.android.server.wm.OplusWindowManagerInternal mOplusWindowManagerInternal;
    protected int mSideGestureHideState = 0;
    protected int mLastSideGestureHideState = -1;
    protected int mNavigationBarMode = 0;
    protected int mLastNavigationBarMode = -1;
    protected int mNavigationBarHideState = 0;
    protected int mLastNavigationBarHideState = -1;
    protected boolean mHideNavigationBar = false;
    protected boolean mIsNavBarImmersive = false;
    protected int[] mNavigationBarHeightForRotationGestrue = new int[4];
    protected int mLastNavigationBarState = 0;
    protected int mLastStatusBarState = 0;

    public OplusDefaultDisplayPolicyEx(com.android.server.wm.DisplayPolicy displayPolicy) {
        this.mDisplayPolicy = displayPolicy;
        this.mOplusDpInner = (com.android.server.wm.IOplusDisplayPolicyInner) displayPolicy.getWrapper().getExtImpl().getOplusDisplayPolicyInner();
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isNavGestureMode() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isHideNavBarGestureMode() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateNavigationBarHideState() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isNavBarHidden() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void loadGestureBarHeight(android.content.res.Resources res, int portraitRotation, int upsideDownRotation, int landscapeRotation, int seascapeRotation) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateDisplayConfig() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int caculateDisplayFrame(int position, android.graphics.Rect cutoutSafeUnrestricted, int def) {
        return def;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int calculateGestureNavInset(int def, int navBarHeight) {
        return def;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateFrameProvider(com.android.server.wm.DisplayFrames displayFrames, com.android.server.wm.WindowState windowState, android.graphics.Rect inOutFrame, android.view.WindowManager.LayoutParams lp, android.view.InsetsFrameProvider ifp) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isKeyboardPositionUp() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateNavigationFrame(int position, int rotation, android.graphics.Rect navigationFrame, android.graphics.Rect cutoutSafeUnrestricted) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateKeyboardPosition() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateKeyboardPosition(boolean inputShow) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void onMayUseInputMethod(boolean useIme) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void updateGestureStatus() {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isNavBarImmersive() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void setSystemUiVisibility(com.android.server.statusbar.StatusBarManagerInternal statusBar, int displayId, int vis, int fullscreenStackVis, int dockedStackVis, int mask, android.graphics.Rect fullscreenBounds, android.graphics.Rect dockedBounds, boolean isNavbarColorManagedByIme, com.android.server.wm.WindowState win, int navBarVis) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int getNavBarVisibility(boolean isKeyguard, boolean forceShowNavBar, boolean screenOn) {
        return 0;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void notifyWindowStateChange(int visibility) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void notifyWindowStateChanged(int id, int state, int displayId) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void systemBarStateChange(int visibility, boolean hideStatusBar, boolean hideNavBar) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isWindowTitleEquals(com.android.server.wm.WindowState win, java.lang.String title) {
        return win != null && win.getAttrs().getTitle().equals(title);
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public int getSysUiFlagsForSplitScreen(com.android.server.wm.WindowState win, int sysUiFlag) {
        return sysUiFlag;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isGameDockWindow(com.android.server.wm.WindowState win) {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void layoutGameDockWindowLw(com.android.server.wm.DisplayFrames displayFrames, android.graphics.Rect cf, android.graphics.Rect of, android.graphics.Rect df, android.graphics.Rect pf) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean willSkipSystemUI(java.lang.String title) {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isIncludedNextPage(com.android.server.wm.WindowState win) {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void setForceShowNavbar(boolean force) {
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public boolean isForceShowNavbar() {
        return false;
    }

    @Override // com.android.server.wm.IOplusDisplayPolicyEx
    public void setSystemUiVisibility(int navBarVis, com.android.server.wm.WindowState window) {
    }
}
