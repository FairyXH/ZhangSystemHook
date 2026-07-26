package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IVirtualDisplayAdapterExt {
    default void destroyDisplayLocked(java.lang.String ownerPackageName) {
    }

    default void createDisplayLocked(java.lang.String ownerPackageName) {
    }

    default java.util.Map<java.lang.String, java.lang.Integer> getInvalidVirtualDisplays(int invalidSize) {
        return null;
    }

    default com.android.server.display.DisplayDevice getMediaProjectionStoppedDevice(android.os.IBinder appToken, com.android.server.display.DisplayDevice device) {
        return device;
    }

    default void setMediaProjectionStoppedDevice(android.os.IBinder appToken, com.android.server.display.DisplayDevice device) {
    }
}
