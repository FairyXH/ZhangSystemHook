package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
public interface IBluetoothManagerServiceWrapper {
    default void setNameAddressOnly(boolean nameAddressOnly) {
    }

    default boolean getNameAddressOnly() {
        return true;
    }

    default boolean getQuietEnable() {
        return true;
    }

    default boolean getEnable() {
        return true;
    }

    default java.util.concurrent.locks.ReentrantReadWriteLock getBluetoothLock() {
        return null;
    }

    default android.bluetooth.IBluetooth getBluetooth() {
        return null;
    }

    default java.lang.Object getHandler() {
        return null;
    }

    default void storeNameAndAddress(java.lang.String name, java.lang.String address) {
    }

    default void setEnableExternal(boolean mEnableExternal) {
    }

    default void clearBleApps() {
    }

    default void handleDisable() {
    }

    default void handleEnable(boolean quietMode) {
    }

    default boolean waitForState(java.util.Set<java.lang.Integer> states) {
        return true;
    }

    default void unbindAndFinish() {
    }

    default void persistBluetoothSetting(int value) {
    }

    default void propagateForegroundUserId(int foregroundUserId) {
    }

    default void OnBrEdrDown(android.content.AttributionSource attributionSource) {
    }

    default void enableBluetooth(boolean quietMode, android.content.AttributionSource attributionSource) {
    }

    default android.os.Bundle syncEnableDisableFlag() {
        return new android.os.Bundle();
    }
}
