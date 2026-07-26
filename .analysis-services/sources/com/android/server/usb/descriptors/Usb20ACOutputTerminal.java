package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb20ACOutputTerminal extends com.android.server.usb.descriptors.UsbACTerminal {
    private static final java.lang.String TAG = "Usb20ACOutputTerminal";
    private byte mClkSoureID;
    private int mControls;
    private byte mSourceID;
    private byte mTerminalID;

    public Usb20ACOutputTerminal(int length, byte type, byte subtype, int subClass) {
        super(length, type, subtype, subClass);
    }

    public byte getSourceID() {
        return this.mSourceID;
    }

    public byte getClkSourceID() {
        return this.mClkSoureID;
    }

    public int getControls() {
        return this.mControls;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal
    public byte getTerminalID() {
        return this.mTerminalID;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mSourceID = stream.getByte();
        this.mClkSoureID = stream.getByte();
        this.mControls = stream.unpackUsbShort();
        this.mTerminalID = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Source ID:" + ((int) getSourceID()));
        canvas.writeListItem("Clock Source ID: " + ((int) getClkSourceID()));
        canvas.writeListItem("Controls: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getControls()));
        canvas.writeListItem("Terminal Name ID: " + ((int) getTerminalID()));
        canvas.closeList();
    }
}
