package com.android.server.usb.hal.gadget;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbGadgetHalInstance {
    public static com.android.server.usb.hal.gadget.UsbGadgetHal getInstance(com.android.server.usb.UsbDeviceManager deviceManager, com.android.internal.util.IndentingPrintWriter pw) {
        com.android.server.usb.UsbPortManager.logAndPrint(3, pw, "Querying USB Gadget HAL version");
        if (com.android.server.usb.hal.gadget.UsbGadgetAidl.isServicePresent(null)) {
            com.android.server.usb.UsbPortManager.logAndPrint(4, pw, "USB Gadget HAL AIDL present");
            return new com.android.server.usb.hal.gadget.UsbGadgetAidl(deviceManager, pw);
        }
        if (com.android.server.usb.hal.gadget.UsbGadgetHidl.isServicePresent(null)) {
            com.android.server.usb.UsbPortManager.logAndPrint(4, pw, "USB Gadget HAL HIDL present");
            return new com.android.server.usb.hal.gadget.UsbGadgetHidl(deviceManager, pw);
        }
        com.android.server.usb.UsbPortManager.logAndPrint(6, pw, "USB Gadget HAL AIDL/HIDL not present");
        return null;
    }
}
