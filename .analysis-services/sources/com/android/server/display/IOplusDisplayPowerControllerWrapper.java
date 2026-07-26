package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusDisplayPowerControllerWrapper {
    default void setDebug(boolean val) {
    }

    default void updatePowerState() {
    }

    default void sendUpdatePowerState() {
    }

    default void animateScreenBrightness(float target, float sdrTarget, float rate) {
    }

    default void setScreenBrightnessRangeMinimum(float val) {
    }

    default void setScreenBrightnessRangeMaximum(float val) {
    }

    default void setScreenBrightnessNormalMaximum(float val) {
    }

    default void setScreenBrightnessDefault(float val) {
    }

    default void handleSettingsChange() {
    }

    default void setAutoBrightnessAdjustment(float val) {
    }

    default com.android.server.display.DisplayPowerProximityStateController getDisplayPowerProximityStateController() {
        return null;
    }

    default void setLogicalDisplayMapper(com.android.server.display.LogicalDisplayMapper mapper) {
    }

    default com.android.server.display.LogicalDisplayMapper getLogicalDisplayMapper() {
        return null;
    }

    default boolean isScreenOnUnblockerExist() {
        return false;
    }

    default void sendMsgUnblockScreenOn(boolean needBlockedScreenOn) {
    }

    default int getDisplayId() {
        return 0;
    }
}
