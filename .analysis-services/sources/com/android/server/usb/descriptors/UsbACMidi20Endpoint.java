package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbACMidi20Endpoint extends com.android.server.usb.descriptors.UsbACEndpoint {
    private static final java.lang.String TAG = "UsbACMidi20Endpoint";
    private byte[] mBlockIds;
    private byte mNumGroupTerminals;

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ int getSubclass() {
        return super.getSubclass();
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ byte getSubtype() {
        return super.getSubtype();
    }

    public UsbACMidi20Endpoint(int length, byte type, int subclass, byte subtype) {
        super(length, type, subclass, subtype);
        this.mBlockIds = new byte[0];
    }

    public byte getNumGroupTerminals() {
        return this.mNumGroupTerminals;
    }

    public byte[] getBlockIds() {
        return this.mBlockIds;
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mNumGroupTerminals = stream.getByte();
        if (this.mNumGroupTerminals > 0) {
            this.mBlockIds = new byte[this.mNumGroupTerminals];
            for (int block = 0; block < this.mNumGroupTerminals; block++) {
                this.mBlockIds[block] = stream.getByte();
            }
        }
        int block2 = this.mLength;
        return block2;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.writeHeader(3, "AC Midi20 Endpoint: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getType()) + " Length: " + getLength());
        canvas.openList();
        canvas.writeListItem("" + ((int) getNumGroupTerminals()) + " Group Terminals.");
        for (int i = 0; i < getNumGroupTerminals(); i++) {
            canvas.writeListItem("Group Terminal " + i + ": " + ((int) this.mBlockIds[i]));
        }
        canvas.closeList();
    }
}
