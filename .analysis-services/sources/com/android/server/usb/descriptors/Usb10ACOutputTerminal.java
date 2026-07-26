package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb10ACOutputTerminal extends com.android.server.usb.descriptors.UsbACTerminal {
    private static final java.lang.String TAG = "Usb10ACOutputTerminal";
    private byte mSourceID;
    private byte mTerminal;

    public Usb10ACOutputTerminal(int length, byte type, byte subtype, int subClass) {
        super(length, type, subtype, subClass);
    }

    public byte getSourceID() {
        return this.mSourceID;
    }

    public byte getTerminal() {
        return this.mTerminal;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mSourceID = stream.getByte();
        this.mTerminal = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Source ID: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getSourceID()));
        canvas.closeList();
    }
}
