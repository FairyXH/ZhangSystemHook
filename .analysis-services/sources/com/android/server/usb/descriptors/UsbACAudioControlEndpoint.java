package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public class UsbACAudioControlEndpoint extends com.android.server.usb.descriptors.UsbACEndpoint {
    static final byte ADDRESSMASK_DIRECTION = -128;
    static final byte ADDRESSMASK_ENDPOINT = 15;
    static final byte ATTRIBMASK_TRANS = 3;
    static final byte ATTRIBSMASK_SYNC = 12;
    private static final java.lang.String TAG = "UsbACAudioControlEndpoint";
    private byte mAddress;
    private byte mAttribs;
    private byte mInterval;
    private int mMaxPacketSize;

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ int getSubclass() {
        return super.getSubclass();
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ byte getSubtype() {
        return super.getSubtype();
    }

    public UsbACAudioControlEndpoint(int length, byte type, int subclass, byte subtype) {
        super(length, type, subclass, subtype);
    }

    public byte getAddress() {
        return this.mAddress;
    }

    public byte getAttribs() {
        return this.mAttribs;
    }

    public int getMaxPacketSize() {
        return this.mMaxPacketSize;
    }

    public byte getInterval() {
        return this.mInterval;
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mAddress = stream.getByte();
        this.mAttribs = stream.getByte();
        this.mMaxPacketSize = stream.unpackUsbShort();
        this.mInterval = stream.getByte();
        return this.mLength;
    }
}
