package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
public interface IDeviceStateManagerServiceWrapper {
    default java.util.Optional<android.hardware.devicestate.DeviceState> getCommittedState() {
        return null;
    }

    default java.util.Optional<android.hardware.devicestate.DeviceState> getBaseState() {
        return null;
    }

    default java.util.Optional<android.hardware.devicestate.DeviceState> getStateLocked(int identifier) {
        return null;
    }
}
