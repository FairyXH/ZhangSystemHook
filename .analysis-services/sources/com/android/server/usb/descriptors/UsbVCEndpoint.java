package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
abstract class UsbVCEndpoint extends com.android.server.usb.descriptors.UsbDescriptor {
    private static final java.lang.String TAG = "UsbVCEndpoint";
    public static final byte VCEP_ENDPOINT = 2;
    public static final byte VCEP_GENERAL = 1;
    public static final byte VCEP_INTERRUPT = 3;
    public static final byte VCEP_UNDEFINED = 0;

    UsbVCEndpoint(int length, byte type) {
        super(length, type);
    }

    public static com.android.server.usb.descriptors.UsbDescriptor allocDescriptor(com.android.server.usb.descriptors.UsbDescriptorParser parser, int length, byte type, byte subtype) {
        parser.getCurInterface();
        switch (subtype) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                android.util.Log.w(TAG, "Unknown Video Class Endpoint id:0x" + java.lang.Integer.toHexString(subtype));
                break;
        }
        return null;
    }
}
