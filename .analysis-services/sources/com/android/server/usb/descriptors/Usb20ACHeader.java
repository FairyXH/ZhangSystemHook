package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb20ACHeader extends com.android.server.usb.descriptors.UsbACHeaderInterface {
    private static final java.lang.String TAG = "Usb20ACHeader";
    private byte mCategory;
    private byte mControls;

    public Usb20ACHeader(int length, byte type, byte subtype, int subclass, int spec) {
        super(length, type, subtype, subclass, spec);
    }

    public byte getCategory() {
        return this.mCategory;
    }

    public byte getControls() {
        return this.mControls;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mCategory = stream.getByte();
        this.mTotalLength = stream.unpackUsbShort();
        this.mControls = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACHeaderInterface, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Category: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getCategory()));
        canvas.writeListItem("Controls: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getControls()));
        canvas.closeList();
    }
}
