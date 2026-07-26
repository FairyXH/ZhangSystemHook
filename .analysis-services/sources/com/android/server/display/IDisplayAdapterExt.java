package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayAdapterExt {
    default void setDisplayHandler(android.os.Handler handler) {
    }

    default void setListener(com.android.server.display.DisplayAdapter.Listener listener) {
    }

    default void sendDisplayDeviceEventLocked(com.android.server.display.DisplayDevice device, int event, long timestamp) {
    }
}
