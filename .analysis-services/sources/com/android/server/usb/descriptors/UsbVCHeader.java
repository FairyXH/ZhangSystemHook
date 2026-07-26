package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbVCHeader extends com.android.server.usb.descriptors.UsbVCHeaderInterface {
    private static final java.lang.String TAG = "UsbVCHeader";

    public UsbVCHeader(int length, byte type, byte subtype, int spec) {
        super(length, type, subtype, spec);
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        return super.parseRawDescriptors(stream);
    }

    @Override // com.android.server.usb.descriptors.UsbVCHeaderInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
    }
}
