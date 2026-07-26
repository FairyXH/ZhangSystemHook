package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IAudioDeviceInventoryExt {
    default void init(com.android.server.audio.AudioDeviceBroker adb) {
    }

    default void setActiveA2dpDeviceClass(android.bluetooth.BluetoothDevice btDevice, int state) {
    }

    default int getFinalA2dpVolume(int a2dpVolume) {
        return 0;
    }

    default void postAbsoluteA2dpVolume(int a2dpVolume) {
    }

    default void postSyncA2dpVolume(boolean absVolumeSupported) {
    }

    default int getFinalBleVolume(int bleVolume) {
        return 0;
    }

    default void postAbsoluteBleVolume(int bleVolume) {
    }

    default boolean isSpeakerA2dpDevice() {
        return false;
    }

    default void setAudioDeviceDisconnect(int device) {
    }

    default boolean isDhpResetting() {
        return false;
    }

    default boolean isMetaAudioSupport() {
        return false;
    }

    default boolean isHoloVoipSupport() {
        return false;
    }
}
