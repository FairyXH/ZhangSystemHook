package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb20ASFormatIIEx extends com.android.server.usb.descriptors.UsbASFormat {
    private static final java.lang.String TAG = "Usb20ASFormatIIEx";
    private byte mHeaderLength;
    private int mMaxBitRate;
    private int mSamplesPerFrame;
    private byte mSidebandProtocol;

    public Usb20ASFormatIIEx(int length, byte type, byte subtype, byte formatType, byte subclass) {
        super(length, type, subtype, formatType, subclass);
    }

    public int getMaxBitRate() {
        return this.mMaxBitRate;
    }

    public int getSamplesPerFrame() {
        return this.mSamplesPerFrame;
    }

    public byte getHeaderLength() {
        return this.mHeaderLength;
    }

    public byte getSidebandProtocol() {
        return this.mSidebandProtocol;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mMaxBitRate = stream.unpackUsbShort();
        this.mSamplesPerFrame = stream.unpackUsbShort();
        this.mHeaderLength = stream.getByte();
        this.mSidebandProtocol = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbASFormat, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Max Bit Rate: " + getMaxBitRate());
        canvas.writeListItem("Samples Per Frame: " + getSamplesPerFrame());
        canvas.writeListItem("Header Length: " + ((int) getHeaderLength()));
        canvas.writeListItem("Sideband Protocol: " + ((int) getSidebandProtocol()));
        canvas.closeList();
    }
}
