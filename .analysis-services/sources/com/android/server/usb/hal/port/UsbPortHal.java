package com.android.server.usb.hal.port;

/* JADX INFO: loaded from: classes3.dex */
public interface UsbPortHal {
    public static final int HAL_DATA_ROLE_DEVICE = 2;
    public static final int HAL_DATA_ROLE_HOST = 1;
    public static final int HAL_MODE_DFP = 2;
    public static final int HAL_MODE_UFP = 1;
    public static final int HAL_POWER_ROLE_SINK = 2;
    public static final int HAL_POWER_ROLE_SOURCE = 1;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HalUsbDataRole {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HalUsbPortMode {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HalUsbPowerRole {
    }

    void enableContaminantPresenceDetection(java.lang.String str, boolean z, long j);

    void enableLimitPowerTransfer(java.lang.String str, boolean z, long j, android.hardware.usb.IUsbOperationInternal iUsbOperationInternal);

    boolean enableUsbData(java.lang.String str, boolean z, long j, android.hardware.usb.IUsbOperationInternal iUsbOperationInternal);

    void enableUsbDataWhileDocked(java.lang.String str, long j, android.hardware.usb.IUsbOperationInternal iUsbOperationInternal);

    int getUsbHalVersion() throws android.os.RemoteException;

    void queryPortStatus(long j);

    void resetUsbPort(java.lang.String str, long j, android.hardware.usb.IUsbOperationInternal iUsbOperationInternal);

    void switchDataRole(java.lang.String str, int i, long j);

    void switchMode(java.lang.String str, int i, long j);

    void switchPowerRole(java.lang.String str, int i, long j);

    void systemReady();
}
