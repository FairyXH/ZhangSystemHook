package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb10ASGeneral extends com.android.server.usb.descriptors.UsbACInterface {
    private static final java.lang.String TAG = "Usb10ASGeneral";
    private byte mDelay;
    private int mFormatTag;
    private byte mTerminalLink;

    public Usb10ASGeneral(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    public byte getTerminalLink() {
        return this.mTerminalLink;
    }

    public byte getDelay() {
        return this.mDelay;
    }

    public int getFormatTag() {
        return this.mFormatTag;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mTerminalLink = stream.getByte();
        this.mDelay = stream.getByte();
        this.mFormatTag = stream.unpackUsbShort();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Delay: " + ((int) this.mDelay));
        canvas.writeListItem("Terminal Link: " + ((int) this.mTerminalLink));
        canvas.writeListItem("Format: " + com.android.server.usb.descriptors.report.UsbStrings.getAudioFormatName(this.mFormatTag) + " - " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(this.mFormatTag));
        canvas.closeList();
    }
}
