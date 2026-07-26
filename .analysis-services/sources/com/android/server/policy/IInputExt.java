package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface IInputExt {
    public static final int ASSIST_MANAGER_LAUNCH_MODE_DEFAULT = 1;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_LENS = 2;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_PERSONAL_UPDATES = 5;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_UNKNOWN = 0;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_WALKIE_TALKIE_START = 3;
    public static final int ASSIST_MANAGER_LAUNCH_MODE_WALKIE_TALKIE_STOP = 4;
    public static final int DEFAULT_LONG_PRESS_POWERON_DISPLAY_TIME = 2500;
    public static final int KEY_OFFSET_VALUE = 800;

    default void init(com.android.server.policy.PhoneWindowManager base, android.content.Context context, android.view.IWindowManager windowManager, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
    }

    default void extraWorkInCancelPendingPowerKeyAction() {
    }

    default void interceptPowerKeyDown(android.view.KeyEvent event, boolean interactive) {
    }

    default void interceptPowerKeyUp(boolean handled) {
    }

    default boolean getSpeechLongPressHandle() {
        return false;
    }

    default void handlePowerKeyUpForWallet(boolean handled) {
    }

    default long hookScreenshotChordLongPressDelay() {
        return 0L;
    }

    default boolean interceptPowerKeyForTelephone(android.view.KeyEvent event, boolean interactive) {
        return false;
    }

    default boolean isMenuLongPressed() {
        return false;
    }

    default void interceptPowerKeyForAlarm() {
    }

    default boolean interceptLongPowerPress() {
        return false;
    }

    default boolean interceptLongHomePress() {
        return false;
    }

    default void powerPress(long downTime, boolean interactive, int powerKeyPressCounter, int displayId) {
    }

    default int updateConfigurationDependentBehaviors(int oldValue) {
        return oldValue;
    }

    default boolean interceptAppSwitchEventBeforeQueueing(android.view.KeyEvent event, boolean oldValue) {
        return oldValue;
    }

    default boolean interceptRingerChordGesture() {
        return false;
    }

    default void handleAssistLaunchMode(int launchModeNumber, android.os.Bundle args) {
    }

    default void setLaunchModeInBundleWithDefault(android.os.Message message) {
    }

    default int getKeyMode() {
        return -1;
    }

    default void launchAssistGoogleSpeechAssistantAction(android.os.Message msg) {
    }

    default boolean isCameraGestureEnabled() {
        return false;
    }

    default void setInterceptInputKeyStatus(boolean enable) {
    }

    default boolean interceptKeyEventToLauncherIfNeed(android.view.KeyEvent event) {
        return false;
    }
}
