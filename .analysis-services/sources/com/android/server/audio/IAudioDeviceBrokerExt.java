package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface IAudioDeviceBrokerExt {
    public static final long ACTION_CHECK_INTERVAL_TIME = 3000;
    public static final int ACTION_SCO_DEVICE_DISCONNECT = 2;
    public static final int ACTION_SET_MODE = 0;
    public static final int ACTION_SET_ROUTE = 1;
    public static final int EVENTID_IDLE_ROUTE_CLEAR_INFO = 20007;
    public static final int EVENTID_INCALL_ROUTE_EXCEPTION_INFO = 20006;
    public static final int MSG_SCO_DEVICE_DISCONNECT = 81;
    public static final java.lang.String REMOVE_INACTIVE_ROUTE_CLIENT = "removeInactiveRouteClient";
    public static final int SENDMSG_QUEUE = 2;
    public static final java.lang.String SET_BLUETOOTH_ACTIVE_DEVICE = "setBluetoothActiveDevice";

    default void initAdbExtInner(android.content.Context context, com.android.server.audio.AudioDeviceBroker mAudioDeviceBroker) {
    }

    default void oplusHeadsetFadeInstantiate(android.content.Context context, com.android.server.audio.AudioService service) {
    }

    default boolean oplusHeadsetFadeInit(int device) {
        return false;
    }

    default void oplusHeadsetFadeSkipFadeIn(int device) {
    }

    default void oplusHeadsetFadeBeginFadeIn() {
    }

    default void sendBtDeviceConnectedEvent(android.bluetooth.BluetoothDevice btDevice) {
    }

    default boolean checkPreviousDeviceIsConnected(android.bluetooth.BluetoothDevice previousDevice, int profile) {
        return false;
    }

    default void clearAvrcpAbsoluteVolume() {
    }

    default void setAvrcpAbsoluteVolumeSupportedwithAddr(java.lang.String address, boolean supported) {
    }

    default void clearAvrcpAbsoluteVolumeSupportedwithAddr(java.lang.String address) {
    }

    default void addAudioRouteEventTrack(int uid, int action, int mode, int deviceType) {
    }

    default void reportRemovedInactiveRouteInfo(int uid, int deviceType, long inactiveTime) {
    }

    default void reportIncallExceptionRouteInfo() {
    }

    default boolean isLeVcAbsoluteVolumeSupported() {
        return false;
    }

    default void setLeVcAbsoluteVolumeSupported(boolean supported) {
    }

    default android.media.AudioDeviceAttributes checkWhetherAnotherLeDevice(android.media.AudioDeviceAttributes cDevice) {
        return null;
    }

    default boolean isSupportFakeHfp() {
        return false;
    }
}
