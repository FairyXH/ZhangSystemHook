package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbVCSelectorUnit extends com.android.server.usb.descriptors.UsbVCInterface {
    private static final java.lang.String TAG = "UsbVCSelectorUnit";

    public UsbVCSelectorUnit(int length, byte type, byte subtype) {
        super(length, type, subtype);
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        return super.parseRawDescriptors(stream);
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
    }
}
