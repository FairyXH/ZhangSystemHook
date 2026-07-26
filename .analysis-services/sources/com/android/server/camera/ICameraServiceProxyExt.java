package com.android.server.camera;

/* JADX INFO: loaded from: classes.dex */
public interface ICameraServiceProxyExt {
    public static final int MSG_FLOAT_WINDOW_SHOW = 2002;
    public static final int MSG_RIGISTER_OBSERVER = 2000;

    default void extendNotifyCameraState(int cameraState, java.lang.String clientName, int facing, java.lang.String cameraId) {
    }

    default boolean getNfcSwitchState() {
        return false;
    }

    default void setNfcSwitchState(boolean isEmpty) {
    }

    default boolean checkCameraFloatWindow() {
        return false;
    }

    default void registerAppSwitchObserver() {
    }

    default void unregisterAppSwitchObserver() {
    }

    default boolean getIsRegistered() {
        return false;
    }

    default int getRegisterTimes() {
        return 0;
    }

    default void handleStateForStatistic(android.hardware.CameraSessionStats cameraState, long timestamp) {
    }
}
