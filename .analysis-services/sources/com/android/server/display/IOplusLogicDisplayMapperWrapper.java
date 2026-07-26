package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusLogicDisplayMapperWrapper {
    default int getPendingDeviceState() {
        return 0;
    }

    default void setPendingDeviceState(int state) {
    }

    default int getDeviceState() {
        return 0;
    }

    default void dispatchDelayedDeviceState(int delayedState) {
    }

    default android.os.Handler getHandler() {
        return new android.os.Handler();
    }
}
