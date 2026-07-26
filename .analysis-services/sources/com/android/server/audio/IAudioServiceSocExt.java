package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IAudioServiceSocExt {
    default void onSystemReadyExt() {
    }

    default void initAudioServiceExtInstance() {
    }

    default void getBleIntentFilters(android.content.IntentFilter intentFilter) {
    }

    default boolean setCommunicationDeviceExt(android.os.IBinder cb, int pid, android.media.AudioDeviceInfo device, java.lang.String eventSource) {
        return false;
    }

    default void setBluetoothLeCgOn(boolean on) {
    }

    default boolean isBluetoothLeTbsDeviceActive() {
        return false;
    }

    default boolean isBluetoothLeCgOn() {
        return false;
    }

    default void startBluetoothLeCg(int pid, int uid, int setMode, android.os.IBinder cb) {
    }

    default void startBluetoothLeCg(android.os.IBinder cb, int targetSdkVersion) {
    }

    default boolean stopBluetoothLeCg(android.os.IBinder cb) {
        return false;
    }

    default void stopBluetoothLeCgLater(android.os.IBinder cb) {
    }

    default void onReceiveExt(android.content.Context context, android.content.Intent intent) {
    }

    default boolean isBleAudioFeatureSupported() {
        return false;
    }

    default void handleMessageExt(android.os.Message msg) {
    }

    default android.media.AudioDeviceAttributes preferredCommunicationDevice() {
        return null;
    }

    default void restartScoInVoipCall() {
    }

    default void setPreferredDeviceForHfpInbandRinging(int pid, int uid, int mode, android.os.IBinder cb, boolean enable) {
    }

    default void startBluetoothLeCgForRecord(android.os.IBinder cb, int uid, int sampleRate) {
    }

    default android.os.IBinder getModeCb() {
        return null;
    }

    default boolean stopBluetoothLeCgForRecord(android.os.IBinder cb, int uid) {
        return false;
    }

    default void restartBleRecord() {
    }

    default android.media.AudioDeviceAttributes getLeAudioDevice() {
        return null;
    }

    default boolean isBluetoothLeCgStateOn() {
        return false;
    }

    default void notifyCgState(boolean state) {
    }
}
