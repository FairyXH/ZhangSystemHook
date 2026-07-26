package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IWakelockControllerExt {
    default boolean acquireWakelockCustom(int wakelockType, android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks) {
        return false;
    }

    default boolean releaseWakelockCustom(int wakelockType, android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks) {
        return false;
    }
}
