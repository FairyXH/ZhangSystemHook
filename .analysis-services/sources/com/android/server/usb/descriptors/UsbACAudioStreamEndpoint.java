package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public class UsbACAudioStreamEndpoint extends com.android.server.usb.descriptors.UsbACEndpoint {
    private static final java.lang.String TAG = "UsbACAudioStreamEndpoint";

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ int getSubclass() {
        return super.getSubclass();
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ byte getSubtype() {
        return super.getSubtype();
    }

    public UsbACAudioStreamEndpoint(int length, byte type, int subclass, byte subtype) {
        super(length, type, subclass, subtype);
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        stream.advance(this.mLength - stream.getReadCount());
        return this.mLength;
    }
}
