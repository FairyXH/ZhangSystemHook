package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IAudioDeviceBrokerWrapper {
    default com.android.server.audio.IAudioDeviceBrokerExt getExtImpl() {
        return new com.android.server.audio.IAudioDeviceBrokerExt() { // from class: com.android.server.audio.IAudioDeviceBrokerWrapper.1
        };
    }

    default com.android.server.audio.AudioDeviceInventory getDeviceInventory() {
        return null;
    }

    default boolean isSpeakerA2dpDevice() {
        return false;
    }

    default int getA2dpVolume(boolean cmpToSafeVolume, int a2dpVolume) {
        return 0;
    }

    default void sendIILMsg(int msg, int existingMsgPolicy, int arg1, int arg2, java.lang.Object obj, int delay) {
    }

    default int getSetAvrcpAbsVolMsg() {
        return 0;
    }

    default boolean isDeviceConnected(android.media.AudioDeviceAttributes device) {
        return false;
    }

    default boolean isAudioRouteSupported() {
        return false;
    }

    default void removeInactiveRouteClientForUid(int uid) {
    }

    default void checkClearSpeakerDevice(int uid) {
    }

    default void removeRouteClientForUid(int uid) {
    }

    default void clearRedundancyClient(int uid, android.os.IBinder cb) {
    }

    default void stopBluethoothScoToBT(java.lang.String eventSource) {
    }

    default void checkBuildRouteForSco(int uid, java.lang.Object obj) {
    }

    default void checkTimeoutInactiveRouteClient() {
    }

    default int getUidByPid(int pid) {
        return -1;
    }

    default int getLatestPreferredDeviceType() {
        return 0;
    }

    default int getLatestModeOwnerUid() {
        return -1;
    }

    default int getLatestModeOwnerPid() {
        return 0;
    }

    default android.os.Looper getBrokerLooper() {
        return null;
    }

    default android.bluetooth.BluetoothDevice getBluetoothDevice() {
        return null;
    }

    default void setBluetoothDevice(com.android.server.audio.AudioDeviceBroker.oplusBtDeviceInfo btDevice) {
    }

    default int getBleVolume(boolean cmpToSafeVolume, int bleVolume) {
        return 0;
    }

    default boolean getBluetoothVolSyncSupported() {
        return false;
    }

    default void checkHoloDeviceSupportState(boolean isWiredHeadSet, boolean isConnect, boolean isBleDevice) {
    }

    default boolean isBluetoothLeTbsDeviceActive() {
        return false;
    }

    default boolean isVendorBeforeAndroidU() {
        return false;
    }

    default boolean isDeviceActiveForCommunication(int deviceType) {
        return false;
    }

    default void postPersistAudioHeadPhoneSettings(boolean headphoneEnabled) {
    }

    default void onPersistAudioHeadPhoneSettings(boolean headphoneEnabled) {
    }

    default int readAudioHeadPhoneSettings() {
        return 0;
    }

    default boolean getAudioHeadPhoneStateFromSettings() {
        return false;
    }
}
