package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbMSMidiHeader extends com.android.server.usb.descriptors.UsbACInterface {
    private static final java.lang.String TAG = "UsbMSMidiHeader";
    private int mMidiStreamingClass;

    public UsbMSMidiHeader(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    public int getMidiStreamingClass() {
        return this.mMidiStreamingClass;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mMidiStreamingClass = stream.unpackUsbShort();
        stream.advance(this.mLength - stream.getReadCount());
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.writeHeader(3, "MS Midi Header: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getType()) + " SubType: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getSubclass()) + " Length: " + getLength() + " MidiStreamingClass :" + getMidiStreamingClass());
    }
}
