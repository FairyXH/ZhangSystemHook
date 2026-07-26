package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public abstract class UsbACTerminal extends com.android.server.usb.descriptors.UsbACInterface {
    private static final java.lang.String TAG = "UsbACTerminal";
    protected byte mAssocTerminal;
    protected byte mTerminalID;
    protected int mTerminalType;

    public UsbACTerminal(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    public byte getTerminalID() {
        return this.mTerminalID;
    }

    public int getTerminalType() {
        return this.mTerminalType;
    }

    public byte getAssocTerminal() {
        return this.mAssocTerminal;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mTerminalID = stream.getByte();
        this.mTerminalType = stream.unpackUsbShort();
        this.mAssocTerminal = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        int terminalType = getTerminalType();
        canvas.writeListItem("Type: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(terminalType) + ": " + com.android.server.usb.descriptors.report.UsbStrings.getTerminalName(terminalType));
        canvas.writeListItem("ID: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getTerminalID()));
        canvas.writeListItem("Associated terminal: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getAssocTerminal()));
        canvas.closeList();
    }
}
