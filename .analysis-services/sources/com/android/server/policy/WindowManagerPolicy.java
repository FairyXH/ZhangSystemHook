package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface WindowManagerPolicy extends android.view.WindowManagerPolicyConstants {
    public static final int ACTION_PASS_TO_USER = 1;
    public static final int COLOR_FADE_LAYER = 1073741825;
    public static final int FINISH_LAYOUT_REDO_ANIM = 8;
    public static final int FINISH_LAYOUT_REDO_CONFIG = 2;
    public static final int FINISH_LAYOUT_REDO_LAYOUT = 1;
    public static final int FINISH_LAYOUT_REDO_WALLPAPER = 4;
    public static final int TRANSIT_ENTER = 1;
    public static final int TRANSIT_EXIT = 2;
    public static final int TRANSIT_HIDE = 4;
    public static final int TRANSIT_PREVIEW_DONE = 5;
    public static final int TRANSIT_SHOW = 3;
    public static final int USER_ROTATION_FREE = 0;
    public static final int USER_ROTATION_LOCKED = 1;

    public interface DisplayContentInfo {
        android.view.Display getDisplay();

        com.android.server.wm.DisplayRotation getDisplayRotation();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface NavigationBarPosition {
    }

    public interface OnKeyguardExitResult {
        void onKeyguardExitResult(boolean z);
    }

    public interface ScreenOffListener {
        void onScreenOff();
    }

    public interface ScreenOnListener {
        void onScreenOn();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface UserRotationMode {
    }

    void adjustConfigurationLw(android.content.res.Configuration configuration, int i, int i2);

    int applyKeyguardOcclusionChange();

    boolean canDismissBootAnimation();

    int checkAddPermission(int i, boolean z, java.lang.String str, int[] iArr);

    android.view.animation.Animation createHiddenByKeyguardExit(boolean z, boolean z2, boolean z3);

    android.view.animation.Animation createKeyguardWallpaperExit(boolean z);

    void dismissKeyguardLw(com.android.internal.policy.IKeyguardDismissCallback iKeyguardDismissCallback, java.lang.CharSequence charSequence);

    android.view.KeyEvent dispatchUnhandledKey(android.os.IBinder iBinder, android.view.KeyEvent keyEvent, int i);

    void dump(java.lang.String str, java.io.PrintWriter printWriter, java.lang.String[] strArr);

    void dumpDebug(android.util.proto.ProtoOutputStream protoOutputStream, long j);

    void enableKeyguard(boolean z);

    void enableScreenAfterBoot();

    void exitKeyguardSecurely(com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult onKeyguardExitResult);

    void finishedGoingToSleep(int i, int i2);

    void finishedGoingToSleepGlobal(int i);

    void finishedWakingUp(int i, int i2);

    void finishedWakingUpGlobal(int i);

    boolean getDisplayEnable(boolean z);

    int getUiMode();

    boolean hasNavigationBar();

    void hideBootMessages();

    boolean inKeyguardRestrictedKeyInputMode();

    void init(android.content.Context context, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs);

    long interceptKeyBeforeDispatching(android.os.IBinder iBinder, android.view.KeyEvent keyEvent, int i);

    int interceptKeyBeforeQueueing(android.view.KeyEvent keyEvent, int i);

    int interceptMotionBeforeQueueingNonInteractive(int i, int i2, int i3, long j, int i4);

    boolean isGlobalKey(int i);

    boolean isKeyguardDrawnLw();

    boolean isKeyguardHostWindow(android.view.WindowManager.LayoutParams layoutParams);

    boolean isKeyguardLocked();

    boolean isKeyguardOccluded();

    boolean isKeyguardSecure(int i);

    boolean isKeyguardShowing();

    boolean isKeyguardShowingAndNotOccluded();

    boolean isKeyguardTrustedLw();

    boolean isScreenOn();

    boolean isUserSetupComplete();

    void keepScreenOnStartedLw();

    void keepScreenOnStoppedLw();

    void lockNow(android.os.Bundle bundle);

    void notifyCameraLensCoverSwitchChanged(long j, boolean z);

    void notifyLidSwitchChanged(long j, boolean z);

    boolean okToAnimate(boolean z);

    void onDisplaySwitchStart(int i);

    void onKeyguardOccludedChangedLw(boolean z);

    void onSystemUiStarted();

    boolean performHapticFeedback(int i, java.lang.String str, int i2, boolean z, java.lang.String str2, boolean z2);

    void registerShortcutKey(long j, com.android.internal.policy.IShortcutService iShortcutService) throws android.os.RemoteException;

    void screenTurnedOff(int i, boolean z);

    void screenTurnedOn(int i);

    void screenTurningOff(int i, com.android.server.policy.WindowManagerPolicy.ScreenOffListener screenOffListener);

    void screenTurningOn(int i, com.android.server.policy.WindowManagerPolicy.ScreenOnListener screenOnListener);

    void setAllowLockscreenWhenOn(int i, boolean z);

    void setCurrentUserLw(int i);

    void setDefaultDisplay(com.android.server.policy.WindowManagerPolicy.DisplayContentInfo displayContentInfo);

    void setDisplayEnable(boolean z, boolean z2);

    void setNavBarVirtualKeyHapticFeedbackEnabledLw(boolean z);

    void setPipVisibilityLw(boolean z);

    void setRecentsVisibilityLw(boolean z);

    void setSafeMode(boolean z);

    void setSecondDefaultDisplay(com.android.server.policy.WindowManagerPolicy.DisplayContentInfo displayContentInfo);

    void setSwitchingUser(boolean z);

    void setTopFocusedDisplay(int i);

    void showBootMessage(java.lang.CharSequence charSequence, boolean z);

    void showDismissibleKeyguard();

    void showGlobalActions();

    void showRecentApps();

    void startKeyguardExitAnimation(long j);

    void startedGoingToSleep(int i, int i2);

    void startedGoingToSleepGlobal(int i);

    void startedWakingUp(int i, int i2);

    void startedWakingUpGlobal(int i);

    void systemBooted();

    void systemReady();

    void userActivity(int i, int i2);

    public interface WindowState {
        boolean canShowWhenLocked();

        int getBaseType();

        java.lang.String getOwningPackage();

        boolean isAnimatingLw();

        default boolean canAddInternalSystemWindow() {
            return false;
        }
    }

    public interface WindowManagerFuncs {
        public static final int CAMERA_LENS_COVERED = 1;
        public static final int CAMERA_LENS_COVER_ABSENT = -1;
        public static final int CAMERA_LENS_UNCOVERED = 0;
        public static final int LID_ABSENT = -1;
        public static final int LID_BEHAVIOR_LOCK = 2;
        public static final int LID_BEHAVIOR_NONE = 0;
        public static final int LID_BEHAVIOR_SLEEP = 1;
        public static final int LID_CLOSED = 0;
        public static final int LID_OPEN = 1;

        void enableScreenIfNeeded();

        int getCameraLensCoverState();

        int getLidState();

        java.lang.Object getWindowManagerLock();

        boolean isAppTransitionStateIdle();

        void lockDeviceNow();

        void moveDisplayToTopIfAllowed(int i);

        void notifyKeyguardTrustedChanged();

        java.util.List<android.content.ComponentName> notifyScreenshotListeners(int i);

        void onKeyguardShowingAndNotOccludedChanged();

        void onPowerKeyDown(boolean z);

        void onUserSwitched();

        void reboot(boolean z);

        void rebootSafeMode(boolean z);

        void registerPointerEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener pointerEventListener, int i);

        void screenTurningOff(int i, com.android.server.policy.WindowManagerPolicy.ScreenOffListener screenOffListener);

        void shutdown(boolean z);

        void triggerAnimationFailsafe();

        void unregisterPointerEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener pointerEventListener, int i);

        void updateRotation(boolean z, boolean z2);

        static java.lang.String lidStateToString(int lid) {
            switch (lid) {
                case -1:
                    return "LID_ABSENT";
                case 0:
                    return "LID_CLOSED";
                case 1:
                    return "LID_OPEN";
                default:
                    return java.lang.Integer.toString(lid);
            }
        }

        static java.lang.String cameraLensStateToString(int lens) {
            switch (lens) {
                case -1:
                    return "CAMERA_LENS_COVER_ABSENT";
                case 0:
                    return "CAMERA_LENS_UNCOVERED";
                case 1:
                    return "CAMERA_LENS_COVERED";
                default:
                    return java.lang.Integer.toString(lens);
            }
        }
    }

    default int getWindowLayerLw(com.android.server.policy.WindowManagerPolicy.WindowState win) {
        return getWindowLayerFromTypeLw(win.getBaseType(), win.canAddInternalSystemWindow());
    }

    default int getWindowLayerFromTypeLw(int type) {
        if (android.view.WindowManager.LayoutParams.isSystemAlertWindowType(type)) {
            throw new java.lang.IllegalArgumentException("Use getWindowLayerFromTypeLw() or getWindowLayerLw() for alert window types");
        }
        return getWindowLayerFromTypeLw(type, false);
    }

    default int getWindowLayerFromTypeLw(int type, boolean canAddInternalSystemWindow) {
        return getWindowLayerFromTypeLw(type, canAddInternalSystemWindow, false);
    }

    default int getWindowLayerFromTypeLw(int type, boolean canAddInternalSystemWindow, boolean roundedCornerOverlay) {
        if (roundedCornerOverlay && canAddInternalSystemWindow) {
            return getMaxWindowLayer();
        }
        if (type >= 1 && type <= 99) {
            return 2;
        }
        switch (type) {
            case 2000:
                return 15;
            case 2001:
                return 4;
            case com.android.server.camera.ICameraServiceProxyExt.MSG_FLOAT_WINDOW_SHOW /* 2002 */:
            case 2030:
            case 2034:
            case 2035:
            case 2037:
                return 3;
            case 2003:
                return canAddInternalSystemWindow ? 12 : 9;
            case 2004:
            case 2014:
            case 2023:
            case 2025:
            case 2028:
            case 2029:
            default:
                android.util.Slog.e("WindowManager", "Unknown window type: " + type);
                return 3;
            case 2005:
                return 7;
            case 2006:
                return canAddInternalSystemWindow ? 23 : 10;
            case 2007:
                return 8;
            case 2008:
                return 6;
            case 2009:
                return 19;
            case 2010:
                return canAddInternalSystemWindow ? 27 : 9;
            case 2011:
                return 13;
            case 2012:
                return 14;
            case 2013:
                return 1;
            case 2015:
                return 33;
            case 2016:
                return 30;
            case 2017:
                return 18;
            case 2018:
                return 35;
            case 2019:
                return 24;
            case 2020:
                return 22;
            case 2021:
                return 34;
            case 2022:
                return 5;
            case 2024:
                return 25;
            case 2026:
                return 29;
            case 2027:
                return 28;
            case 2031:
                return 21;
            case 2032:
                return 31;
            case 2033:
                return 20;
            case 2036:
                return 26;
            case 2038:
                return 11;
            case 2039:
                return 32;
            case 2040:
                return 17;
            case 2041:
                return 16;
        }
    }

    default int getMaxWindowLayer() {
        return 36;
    }

    default int getSubWindowLayerFromTypeLw(int type) {
        switch (type) {
            case 1000:
            case 1003:
                return 1;
            case 1001:
                return -2;
            case 1002:
                return 2;
            case 1004:
                return -1;
            case 1005:
                return 3;
            default:
                android.util.Slog.e("WindowManager", "Unknown sub-window type: " + type);
                return 0;
        }
    }

    default boolean isKeyguardUnoccluding() {
        return false;
    }

    default void setDismissImeOnBackKeyPressed(boolean newValue) {
    }

    static java.lang.String userRotationModeToString(int mode) {
        switch (mode) {
            case 0:
                return "USER_ROTATION_FREE";
            case 1:
                return "USER_ROTATION_LOCKED";
            default:
                return java.lang.Integer.toString(mode);
        }
    }

    default void registerDisplayFoldListener(android.view.IDisplayFoldListener listener) {
    }

    default void unregisterDisplayFoldListener(android.view.IDisplayFoldListener listener) {
    }

    default void setOverrideFoldedArea(android.graphics.Rect area) {
    }

    default android.graphics.Rect getFoldedArea() {
        return new android.graphics.Rect();
    }

    default void onDefaultDisplayFocusChangedLw(com.android.server.policy.WindowManagerPolicy.WindowState newFocus) {
    }
}
