package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IAudioDeviceInventoryWrapper {
    default com.android.server.audio.IAudioDeviceInventoryExt getExtImpl() {
        return new com.android.server.audio.IAudioDeviceInventoryExt() { // from class: com.android.server.audio.IAudioDeviceInventoryWrapper.1
        };
    }

    default java.lang.String getConnectedDevices() {
        return null;
    }

    default boolean isBluetoothScoDeviceConnected() {
        return false;
    }
}
