package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface IPhoneWindowManagerExt {
    default long hookScreenshotChordLongPressDelay() {
        return 0L;
    }

    default com.android.server.policy.IInputExt getInputExtension() {
        return null;
    }

    default void requestKeyguard(java.lang.String command) {
    }

    default boolean isGlobalActionVisible() {
        return false;
    }

    default boolean checkStartingWindowBackground(android.graphics.drawable.Drawable originDrawable) {
        return originDrawable != null;
    }

    default void handleStartingWindowBackground(com.android.internal.policy.PhoneWindow window) {
    }

    default void handleStartingWindow(com.android.internal.policy.PhoneWindow window) {
    }

    default void adjustBrightnessUpOrDownEvent(int step, int direction) {
    }

    default boolean isXiaoBuAssistantFloatWindow() {
        return false;
    }

    default void shutDownXiaoBuAssistant() {
    }

    default boolean skipKeyguardOccludedCheck() {
        return false;
    }

    default void setKeyguardExitUnlock(long tokenHandle, byte[] token) {
    }

    default boolean isCustomize() {
        return false;
    }

    default int getLaunchMode(android.os.Message msg) {
        return -1;
    }

    default void onPwkPressed() {
    }

    default void onPwkReleased() {
    }

    default void startedGoingToSleep() {
    }

    default void hookForInputLogV(java.lang.String msg) {
    }

    default void notePowerkeyProcessEvent(java.lang.String eventStr, boolean cancelWakeCheck, boolean cancelSleepCheck) {
    }

    default void notePowerkeyProcessStagePoint(java.lang.String stage) {
    }

    default void startHwShutdownDectect() {
    }

    default void clearHwShutdownDectect() {
    }

    default void getTpInfo(java.lang.String key, java.lang.String val) {
    }

    default void hookForInit() {
    }

    default void onBackPressedOnTheiaMonitor(android.view.KeyEvent event) {
    }

    default boolean interceptPowerKeyDown() {
        return false;
    }

    default void notifyWakeUpFromPowerKey(java.lang.String key, java.lang.String value) {
    }

    default void interceptPowerKeyUp(boolean handled, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
    }

    default void interceptScreenshotChord() {
    }

    default boolean isSleepByPowerButtonDisabled() {
        return false;
    }

    default void keyEventSpendTimeEventLog(long eventTime) {
    }

    default void onRecentClicked() {
    }

    default boolean interceptKeyEventForAppShareModeIfNeed(android.view.KeyEvent event) {
        return false;
    }

    default void onSpecialKeyPressedOnTheiaMonitor(android.view.KeyEvent event) {
    }

    default void onPowerKeyPressedOnTheiaMonitor(android.view.KeyEvent event) {
    }

    default boolean isUnderWaterCameraStatus() {
        return false;
    }

    default void onScreenShotKeyPressedOnTheiaMonitor() {
    }

    default void resetDeviceFolded() {
    }

    default void extraWorkInCancelPendingPowerKeyAction() {
    }

    default void oplusInterceptPowerKeyDown(android.view.KeyEvent event, boolean interactive) {
    }

    default void oplusInterceptPowerKeyUp(boolean handled) {
    }

    default boolean getSpeechLongPressHandle() {
        return false;
    }

    default boolean oplusInterceptPowerKeyForTelephone(android.view.KeyEvent event, boolean interactive) {
        return false;
    }

    default boolean isMenuLongPressed() {
        return false;
    }

    default void oplusInterceptPowerKeyForAlarm() {
    }

    default boolean oplusInterceptLongPowerPress() {
        return false;
    }

    default boolean oplusInterceptLongHomePress() {
        return false;
    }

    default int oplusUpdateConfigurationDependentBehaviors(int oldValue) {
        return oldValue;
    }

    default boolean oplusInterceptAppSwitchEventBeforeQueueing(android.view.KeyEvent event, boolean oldValue) {
        return false;
    }

    default void oplusPowerPress(long downTime, boolean interactive, int powerKeyPressCounter, int displayId) {
    }

    default boolean interceptRingerChordGesture() {
        return false;
    }

    default void oplusHandleAssistLaunchMode(int launchMode, android.os.Bundle args) {
    }

    default int getKeyMode() {
        return 0;
    }

    default boolean isCameraGestureEnabled() {
        return false;
    }

    default void sendBroadcastByCustomizeKey(java.lang.String action) {
    }

    default void sendSpeechMessage(java.lang.Long downtime) {
    }

    default void setDynamicalLogEnable(boolean on) {
    }

    default com.android.server.policy.PhoneWindowManager getPhoneWindowManager() {
        return null;
    }

    default android.content.Context getContext() {
        return null;
    }

    default void overrideInit(com.android.server.policy.PhoneWindowManager base, android.content.Context context, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
    }

    default int overrideInterceptKeyBeforeQueueing(android.view.KeyEvent event, int policyFlags) {
        return 0;
    }

    default long overrideInterceptKeyBeforeDispatching(android.os.IBinder focusedToken, android.view.KeyEvent event, int policyFlags) {
        return 0L;
    }

    default void overrideSystemReady() {
    }

    default void overrideScreenTurnedOff(int displayId, boolean isSwappingDisplay) {
    }

    default void overrideScreenTurningOn(int displayId, com.android.server.policy.WindowManagerPolicy.ScreenOnListener screenOnListener) {
    }

    default boolean overridePerformHapticFeedback(int uid, java.lang.String packageName, int effectId, boolean always, java.lang.String reason, boolean fromIme) {
        return false;
    }

    default void overrideEnableScreenAfterBoot() {
    }

    default void overrideShowGlobalActionsInternal() {
    }

    default void overrideShowBootMessage(java.lang.CharSequence msg, boolean always) {
    }

    default void overrideHideBootMessages() {
    }

    default int overrideCheckAddPermission(int type, boolean isRoundedCornerOverlay, java.lang.String packageName, int[] outAppOp) {
        return 0;
    }

    default void overrideUpdateSettings(android.os.Handler handler) {
    }

    default void overrideOnDefaultDisplayFocusChangedLw(com.android.server.policy.WindowManagerPolicy.WindowState newFocus) {
    }

    default void overrideSystemBooted() {
    }

    default void overrideDump(java.lang.String prefix, java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default void overrideSetCurrentUserLw(int newUserId) {
    }

    default boolean overrideIsKeyguardShowingAndNotOccluded() {
        return false;
    }

    default int overrideGetWindowLayerFromTypeLw(int type, boolean canAddInternalSystemWindow, boolean roundedCornerOverlay) {
        return 0;
    }

    default int overrideGetMaxWindowLayer() {
        return 0;
    }

    default boolean addCallback(com.android.server.policy.IPhoneWindowManagerExt.Callback callback) {
        return false;
    }

    default boolean removeCallback(com.android.server.policy.IPhoneWindowManagerExt.Callback callback) {
        return false;
    }

    default boolean skipVolumeKeyIfNeeded() {
        return false;
    }

    public interface Callback {
        java.lang.String getName();

        default void updateSettings(android.content.Context context) {
        }

        default void onInterceptKeyBeforeQueueing(android.view.KeyEvent event, boolean down, int keyCode, int policyFlags) {
        }
    }

    default boolean isTargetUserUnlocked(int targetUserId) {
        return true;
    }

    default void setSecondDefaultDisplay(com.android.server.policy.WindowManagerPolicy.DisplayContentInfo displayContentInfo) {
    }

    default void screenTurnedOff(int displayId) {
    }

    default void screenTurnedOn(int displayId, com.android.server.policy.WindowManagerPolicy.ScreenOnListener screenOnListener) {
    }

    default boolean finishWindowsDrawn(int displayId) {
        return false;
    }

    default void updateOrientationListener(int displayId) {
    }

    default void finishScreenTurningOn(int displayId) {
    }

    default void setDisplayEnable(boolean isDefaultDisplay, boolean enable) {
    }

    default boolean getDisplayEnable(boolean isDefaultDisplay) {
        return false;
    }

    default void sendWindowDrawCompleteMsg(int displayId) {
    }

    default void sendWindowDrawCompleteMsgDelay(int displayId) {
    }

    default boolean isDisplaysOnLocked(android.view.Display defaultDisplay) {
        return false;
    }

    default void updateOrientationListenerAsyncIfNeeded(com.android.server.wm.DisplayRotation dr) {
        if (dr != null) {
            dr.updateOrientationListener();
        }
    }

    default boolean isPowerButtonFpSensor() {
        return false;
    }

    default boolean notifyPowerKeyPressed(java.lang.String reason) {
        return false;
    }

    default boolean sendBroadcastForCombinationKeyGrabSystrace() {
        return false;
    }

    default boolean getBlackScreenWindowManagerPowerKeyState() {
        return false;
    }

    default boolean applyKeyguardOcclusionChange(boolean keyguardOccludedChanged) {
        return false;
    }

    default boolean isSecondScreenOn() {
        return false;
    }

    default void setSwitchingTrackerScreenTurningOnEventLog(boolean start) {
    }

    default void setSwitchingTrackerKeyguardOndrawnEventLog() {
    }

    default void onSystemUiProcessStartedTimeout() {
    }

    default boolean interceptKeyEventToLauncherIfNeed(android.view.KeyEvent event) {
        return false;
    }

    default boolean getBfsKeyAllowEvents() {
        return false;
    }

    default boolean shouldGoToSleep() {
        return false;
    }
}
