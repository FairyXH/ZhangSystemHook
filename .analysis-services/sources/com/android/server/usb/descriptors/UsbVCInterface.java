package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public abstract class UsbVCInterface extends com.android.server.usb.descriptors.UsbDescriptor {
    private static final java.lang.String TAG = "UsbVCInterface";
    public static final byte VCI_EXTENSION_UNIT = 6;
    public static final byte VCI_INPUT_TERMINAL = 2;
    public static final byte VCI_OUTPUT_TERMINAL = 3;
    public static final byte VCI_PROCESSING_UNIT = 5;
    public static final byte VCI_SELECTOR_UNIT = 4;
    public static final byte VCI_UNDEFINED = 0;
    public static final byte VCI_VEADER = 1;
    protected final byte mSubtype;

    public UsbVCInterface(int length, byte type, byte subtype) {
        super(length, type);
        this.mSubtype = subtype;
    }

    public static com.android.server.usb.descriptors.UsbDescriptor allocDescriptor(com.android.server.usb.descriptors.UsbDescriptorParser parser, com.android.server.usb.descriptors.ByteStream stream, int length, byte type) {
        byte subtype = stream.getByte();
        parser.getCurInterface();
        switch (subtype) {
            case 0:
            case 6:
                return null;
            case 1:
                int vcInterfaceSpec = stream.unpackUsbShort();
                parser.setVCInterfaceSpec(vcInterfaceSpec);
                return new com.android.server.usb.descriptors.UsbVCHeader(length, type, subtype, vcInterfaceSpec);
            case 2:
                return new com.android.server.usb.descriptors.UsbVCInputTerminal(length, type, subtype);
            case 3:
                return new com.android.server.usb.descriptors.UsbVCOutputTerminal(length, type, subtype);
            case 4:
                return new com.android.server.usb.descriptors.UsbVCSelectorUnit(length, type, subtype);
            case 5:
                return new com.android.server.usb.descriptors.UsbVCProcessingUnit(length, type, subtype);
            default:
                android.util.Log.w(TAG, "Unknown Video Class Interface subtype: 0x" + java.lang.Integer.toHexString(subtype));
                return null;
        }
    }
}
