package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
public interface IDeviceStateManagerServiceExt {
    public static final int DEVICE_STATE_DSI_SWITCH_TRANSITORY = 98;

    default void setDeviceStateInfo(android.hardware.devicestate.DeviceStateInfo info) {
    }

    default void setRequestState(int state, int requeststate, int pid, int flags) {
    }

    default boolean canCancelRequestState() {
        return false;
    }

    default boolean hasFoldRemapDisplayDisableFeature() {
        return false;
    }

    default java.util.List<android.hardware.devicestate.DeviceState> getSupportedStates(java.util.List<android.hardware.devicestate.DeviceState> supportedStates, android.util.SparseArray<android.hardware.devicestate.DeviceState> mDeviceStates) {
        return supportedStates;
    }

    default int[] getSupportedStateIdentifiersLocked(int[] supportedStates, android.util.SparseArray<android.hardware.devicestate.DeviceState> mDeviceStates) {
        return supportedStates;
    }

    default int overrideBaseState(java.util.Optional<android.hardware.devicestate.DeviceState> baseState, int identifier) {
        return identifier;
    }

    default boolean canRequestState(java.util.Optional<android.hardware.devicestate.DeviceState> baseState, int requeStstate) {
        return true;
    }

    default void enableDeviceStateAfterBoot(boolean enabled) {
    }

    default java.util.Optional<android.hardware.devicestate.DeviceState> shouldInjectTransitoryState(java.util.Optional<android.hardware.devicestate.DeviceState> state) {
        return java.util.Optional.empty();
    }

    default boolean notifyPolicyImmediately() {
        return false;
    }

    default void setSwitchingTrackerSensorEventLog() {
    }
}
