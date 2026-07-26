package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
abstract class UsbACEndpoint extends com.android.server.usb.descriptors.UsbDescriptor {
    public static final byte MS_GENERAL = 1;
    public static final byte MS_GENERAL_2_0 = 2;
    private static final java.lang.String TAG = "UsbACEndpoint";
    protected final int mSubclass;
    protected final byte mSubtype;

    UsbACEndpoint(int length, byte type, int subclass, byte subtype) {
        super(length, type);
        this.mSubclass = subclass;
        this.mSubtype = subtype;
    }

    public int getSubclass() {
        return this.mSubclass;
    }

    public byte getSubtype() {
        return this.mSubtype;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        return this.mLength;
    }

    public static com.android.server.usb.descriptors.UsbDescriptor allocDescriptor(com.android.server.usb.descriptors.UsbDescriptorParser parser, int length, byte type, byte subType) {
        com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDesc = parser.getCurInterface();
        int subClass = interfaceDesc.getUsbSubclass();
        switch (subClass) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                switch (subType) {
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        android.util.Log.w(TAG, "Unknown Midi Endpoint id:0x" + java.lang.Integer.toHexString(subType));
                        break;
                }
                break;
            default:
                android.util.Log.w(TAG, "Unknown Audio Class Endpoint id:0x" + java.lang.Integer.toHexString(subClass));
                break;
        }
        return null;
    }
}
