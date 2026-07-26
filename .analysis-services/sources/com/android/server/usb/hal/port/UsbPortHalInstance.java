package com.android.server.usb.hal.port;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbPortHalInstance {
    public static com.android.server.usb.hal.port.UsbPortHal getInstance(com.android.server.usb.UsbPortManager portManager, com.android.internal.util.IndentingPrintWriter pw) {
        com.android.server.usb.UsbPortManager.logAndPrint(3, null, "Querying USB HAL version");
        if (com.android.server.usb.hal.port.UsbPortAidl.isServicePresent(null)) {
            com.android.server.usb.UsbPortManager.logAndPrint(4, null, "USB HAL AIDL present");
            return new com.android.server.usb.hal.port.UsbPortAidl(portManager, pw);
        }
        if (!com.android.server.usb.hal.port.UsbPortHidl.isServicePresent(null)) {
            return null;
        }
        com.android.server.usb.UsbPortManager.logAndPrint(4, null, "USB HAL HIDL present");
        return new com.android.server.usb.hal.port.UsbPortHidl(portManager, pw);
    }
}
