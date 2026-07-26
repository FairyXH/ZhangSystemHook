package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb10ACInputTerminal extends com.android.server.usb.descriptors.UsbACTerminal implements com.android.server.usb.descriptors.UsbAudioChannelCluster {
    private static final java.lang.String TAG = "Usb10ACInputTerminal";
    private int mChannelConfig;
    private byte mChannelNames;
    private byte mNrChannels;
    private byte mTerminal;

    public Usb10ACInputTerminal(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelCount() {
        return this.mNrChannels;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public int getChannelConfig() {
        return this.mChannelConfig;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelNames() {
        return this.mChannelNames;
    }

    public byte getTerminal() {
        return this.mTerminal;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mNrChannels = stream.getByte();
        this.mChannelConfig = stream.unpackUsbShort();
        this.mChannelNames = stream.getByte();
        this.mTerminal = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("" + ((int) getChannelCount()) + " Chans. Config: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getChannelConfig()));
        canvas.closeList();
    }
}
