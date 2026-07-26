package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbMSMidiOutputJack extends com.android.server.usb.descriptors.UsbACInterface {
    private static final java.lang.String TAG = "UsbMSMidiOutputJack";

    public UsbMSMidiOutputJack(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        stream.advance(this.mLength - stream.getReadCount());
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.writeHeader(3, "MS Midi Output Jack: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getType()) + " SubType: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getSubclass()) + " Length: " + getLength());
    }
}
