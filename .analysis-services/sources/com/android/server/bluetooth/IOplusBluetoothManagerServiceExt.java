package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusBluetoothManagerServiceExt {
    public static final int DCS_EVT_RECORD_ADAPTER_STATE_CHANGE = 2;
    public static final int DCS_EVT_RECORD_APP_CALL = 1;
    public static final int DCS_EVT_RECORD_ERROR_EVENT = 3;
    public static final int DCS_EVT_SET_BLE_APP_ACCOUNT = 4;
    public static final java.lang.String FLAG_ENABLE = "enable";
    public static final java.lang.String FLAG_ENABLE_EXTERNAL = "enable_external";
    public static final java.lang.String FLAG_QUITE_ENABLE = "quite_enable";
    public static final java.lang.String NAME = "IOplusBluetoothManagerServiceExt";
    public static final int RECORD_ADAPTER_STATE_CHANGE = 4;
    public static final int RECORD_BLE_START_TIMEOUT = 8;
    public static final int RECORD_BLE_STOP_TIMEOUT = 7;
    public static final int RECORD_BLUETOOTH_CRASH = 17;
    public static final int RECORD_BREDR_CLEANUP_TIMEOUT = 10;
    public static final int RECORD_BREDR_START_TIMEOUT = 5;
    public static final int RECORD_BREDR_STOP_TIMEOUT = 6;
    public static final int RECORD_BT_BIND_FAILURE = 16;
    public static final int RECORD_BT_BIND_TIMEOUT = 13;
    public static final int RECORD_BT_FORCEKILL_TIMEOUT = 11;
    public static final int RECORD_BT_LE_SERVICE_UP_TIMEOUT = 15;
    public static final int RECORD_BT_UNBIND_TIMEOUT = 14;
    public static final int RECORD_CALLED_DISABLE = 19;
    public static final int RECORD_CALLED_ENABLE = 18;
    public static final int RECORD_DISABLE = 2;
    public static final int RECORD_DISABLE_BLE = 21;
    public static final int RECORD_ENABLE = 1;
    public static final int RECORD_ENABLE_BLE = 20;
    public static final int RECORD_ENABLE_QUIET = 3;
    public static final int RECORD_STACK_DISABLE_ERROR = 12;
    public static final int RECORD_STACK_DISABLE_TIMEOUT = 9;

    default void oplusHandleOnBootPhase() {
    }

    default boolean oplusCheckEnablePermitted(java.lang.String packageName) {
        return true;
    }

    default boolean oplusCheckDisablePermitted(java.lang.String packageName) {
        return true;
    }

    default void oplusDcsEventReport(int eventCode, int p1, int p2, java.lang.Object obj, android.os.Bundle data) {
    }

    default void oplusClearBleApp(java.lang.String packageName) {
    }

    default void waitForBluetoothProcesseExit() {
    }

    default void oplusHandleOnbind() {
    }

    default void oplusHandleUnbind() {
    }

    default boolean oplusSaveRemoteNameAndAddress() {
        return false;
    }

    default void oplusRemoveSaveRemoteNameAndAddressMsg() {
    }

    default void oplusFactoryReset() {
    }

    default boolean oplusPropagateForegroundUserId(int foregroundUserId) {
        return false;
    }

    default boolean oplusOnTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        return false;
    }

    default boolean isOplusCusomizeBluetoothEnabled() {
        return false;
    }

    default void setContext(android.content.Context context) {
    }
}
