package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public class UsbEndpointDescriptor extends com.android.server.usb.descriptors.UsbDescriptor {
    public static final int DIRECTION_INPUT = 128;
    public static final int DIRECTION_OUTPUT = 0;
    public static final byte MASK_ATTRIBS_SYNCTYPE = 12;
    public static final int MASK_ATTRIBS_TRANSTYPE = 3;
    public static final int MASK_ATTRIBS_USEAGE = 48;
    public static final int MASK_ENDPOINT_ADDRESS = 15;
    public static final int MASK_ENDPOINT_DIRECTION = -128;
    public static final byte SYNCTYPE_ADAPTSYNC = 8;
    public static final byte SYNCTYPE_ASYNC = 4;
    public static final byte SYNCTYPE_NONE = 0;
    public static final byte SYNCTYPE_RESERVED = 12;
    private static final java.lang.String TAG = "UsbEndpointDescriptor";
    public static final int TRANSTYPE_BULK = 2;
    public static final int TRANSTYPE_CONTROL = 0;
    public static final int TRANSTYPE_INTERRUPT = 3;
    public static final int TRANSTYPE_ISO = 1;
    public static final int USEAGE_DATA = 0;
    public static final int USEAGE_EXPLICIT = 32;
    public static final int USEAGE_FEEDBACK = 16;
    public static final int USEAGE_RESERVED = 48;
    private int mAttributes;
    private com.android.server.usb.descriptors.UsbDescriptor mClassSpecificEndpointDescriptor;
    private int mEndpointAddress;
    private int mInterval;
    private int mPacketSize;
    private byte mRefresh;
    private byte mSyncAddress;

    public UsbEndpointDescriptor(int length, byte type) {
        super(length, type);
        this.mHierarchyLevel = 4;
    }

    public int getEndpointAddress() {
        return this.mEndpointAddress & 15;
    }

    public int getAttributes() {
        return this.mAttributes;
    }

    public int getPacketSize() {
        return this.mPacketSize;
    }

    public int getInterval() {
        return this.mInterval;
    }

    public byte getRefresh() {
        return this.mRefresh;
    }

    public byte getSyncAddress() {
        return this.mSyncAddress;
    }

    public int getDirection() {
        return this.mEndpointAddress & MASK_ENDPOINT_DIRECTION;
    }

    void setClassSpecificEndpointDescriptor(com.android.server.usb.descriptors.UsbDescriptor descriptor) {
        this.mClassSpecificEndpointDescriptor = descriptor;
    }

    public com.android.server.usb.descriptors.UsbDescriptor getClassSpecificEndpointDescriptor() {
        return this.mClassSpecificEndpointDescriptor;
    }

    public android.hardware.usb.UsbEndpoint toAndroid(com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        return new android.hardware.usb.UsbEndpoint(this.mEndpointAddress, this.mAttributes, this.mPacketSize, this.mInterval);
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mEndpointAddress = stream.getUnsignedByte();
        this.mAttributes = stream.getUnsignedByte();
        this.mPacketSize = stream.unpackUsbShort();
        this.mInterval = stream.getUnsignedByte();
        if (this.mLength == 9) {
            this.mRefresh = stream.getByte();
            this.mSyncAddress = stream.getByte();
        }
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Address: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getEndpointAddress()) + (getDirection() == 0 ? " [out]" : " [in]"));
        int attributes = getAttributes();
        canvas.openListItem();
        canvas.write("Attributes: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(attributes) + " ");
        switch (attributes & 3) {
            case 0:
                canvas.write("Control");
                break;
            case 1:
                canvas.write("Iso");
                break;
            case 2:
                canvas.write("Bulk");
                break;
            case 3:
                canvas.write("Interrupt");
                break;
        }
        canvas.closeListItem();
        if ((attributes & 3) == 1) {
            canvas.openListItem();
            canvas.write("Aync: ");
            switch (attributes & 12) {
                case 0:
                    canvas.write("NONE");
                    break;
                case 4:
                    canvas.write("ASYNC");
                    break;
                case 8:
                    canvas.write("ADAPTIVE ASYNC");
                    break;
            }
            canvas.closeListItem();
            canvas.openListItem();
            canvas.write("Useage: ");
            switch (attributes & 48) {
                case 0:
                    canvas.write("DATA");
                    break;
                case 16:
                    canvas.write("FEEDBACK");
                    break;
                case 32:
                    canvas.write("EXPLICIT FEEDBACK");
                    break;
                case 48:
                    canvas.write("RESERVED");
                    break;
            }
            canvas.closeListItem();
        }
        canvas.writeListItem("Package Size: " + getPacketSize());
        canvas.writeListItem("Interval: " + getInterval());
        canvas.closeList();
    }
}
