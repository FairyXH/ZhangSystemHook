package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayRotationSocExt {
    default void hookRegisterWifiDisplay(android.content.Context context, com.android.server.wm.WindowManagerService service) {
    }

    default boolean hookIsWifiDisplayConnected() {
        return false;
    }

    default int hookGetWifiDisplayRotation() {
        return -1;
    }
}
