package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbACMidi10Endpoint extends com.android.server.usb.descriptors.UsbACEndpoint {
    private static final java.lang.String TAG = "UsbACMidi10Endpoint";
    private byte[] mJackIds;
    private byte mNumJacks;

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ int getSubclass() {
        return super.getSubclass();
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ byte getSubtype() {
        return super.getSubtype();
    }

    public UsbACMidi10Endpoint(int length, byte type, int subclass, byte subtype) {
        super(length, type, subclass, subtype);
        this.mJackIds = new byte[0];
    }

    public byte getNumJacks() {
        return this.mNumJacks;
    }

    public byte[] getJackIds() {
        return this.mJackIds;
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mNumJacks = stream.getByte();
        if (this.mNumJacks > 0) {
            this.mJackIds = new byte[this.mNumJacks];
            for (int jack = 0; jack < this.mNumJacks; jack++) {
                this.mJackIds[jack] = stream.getByte();
            }
        }
        int jack2 = this.mLength;
        return jack2;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.writeHeader(3, "ACMidi10Endpoint: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getType()) + " Length: " + getLength());
        canvas.openList();
        canvas.writeListItem("" + ((int) getNumJacks()) + " Jacks.");
        for (int i = 0; i < getNumJacks(); i++) {
            canvas.writeListItem("Jack " + i + ": " + ((int) this.mJackIds[i]));
        }
        canvas.closeList();
    }
}
