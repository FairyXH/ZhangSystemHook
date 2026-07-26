package android.hardware.usb;

/* JADX INFO: loaded from: classes.dex */
public @interface UsbDataStatus {
    public static final byte DISABLED_CONTAMINANT = 3;
    public static final byte DISABLED_DEBUG = 6;
    public static final byte DISABLED_DOCK = 4;
    public static final byte DISABLED_DOCK_DEVICE_MODE = 8;
    public static final byte DISABLED_DOCK_HOST_MODE = 7;
    public static final byte DISABLED_FORCE = 5;
    public static final byte DISABLED_OVERHEAT = 2;
    public static final byte ENABLED = 1;
    public static final byte UNKNOWN = 0;
}
