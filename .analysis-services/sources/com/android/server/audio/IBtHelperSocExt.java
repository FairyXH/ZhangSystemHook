package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IBtHelperSocExt {
    default boolean isLeAudioDevice(android.content.Intent intent) {
        return false;
    }

    default boolean isNextBtActiveDeviceAvailableForMusic(android.bluetooth.BluetoothA2dp a2dp, android.bluetooth.BluetoothLeAudio leAudio) {
        return false;
    }

    default boolean isBluetoothScoOn() {
        return true;
    }
}
