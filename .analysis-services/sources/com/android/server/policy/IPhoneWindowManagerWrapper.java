package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface IPhoneWindowManagerWrapper {
    default com.android.server.policy.IPhoneWindowManagerExt getExtImpl() {
        return new com.android.server.policy.IPhoneWindowManagerExt() { // from class: com.android.server.policy.IPhoneWindowManagerWrapper.1
        };
    }

    default void setlocalLOGV(boolean on) {
    }

    default void setDebugInput(boolean on) {
    }

    default void setDebugKeyguard(boolean on) {
    }

    default void setDebugWakeup(boolean on) {
    }

    default boolean performHapticFeedback(int effectId, boolean always, java.lang.String reason) {
        return false;
    }

    default com.android.server.policy.KeyCombinationManager getKeyCombinationManager() {
        return null;
    }

    default void interceptRingerToggleChord() {
    }

    default void cancelPendingRingerToggleChordAction() {
    }

    default void cancelGlobalActionsAction() {
    }

    default com.android.server.policy.SingleKeyGestureDetector getSingleKeyGestureDetector() {
        return null;
    }

    default void launchAssistAction(java.lang.String hint, int deviceId, long eventTime, int invocationType, int launchModeEventNumber) {
    }

    default void finishPowerKeyPress() {
    }

    default void cancelPreloadRecentApps() {
    }

    default java.lang.Object getLock() {
        return new java.lang.Object();
    }

    default void powerPress(long eventTime, boolean beganFromNonInteractive, int count, int displayId) {
    }

    default void wakeUpFromPowerKey(long eventTime) {
    }

    default boolean handleHomeShortcuts(android.os.IBinder focusedToken, android.view.KeyEvent event) {
        return false;
    }
}
