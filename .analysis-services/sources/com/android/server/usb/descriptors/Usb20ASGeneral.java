package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb20ASGeneral extends com.android.server.usb.descriptors.UsbACInterface implements com.android.server.usb.descriptors.UsbAudioChannelCluster {
    private static final java.lang.String TAG = "Usb20ASGeneral";
    private int mChannelConfig;
    private byte mChannelNames;
    private byte mControls;
    private byte mFormatType;
    private int mFormats;
    private byte mNumChannels;
    private byte mTerminalLink;

    public Usb20ASGeneral(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    public byte getTerminalLink() {
        return this.mTerminalLink;
    }

    public byte getControls() {
        return this.mControls;
    }

    public byte getFormatType() {
        return this.mFormatType;
    }

    public int getFormats() {
        return this.mFormats;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelCount() {
        return this.mNumChannels;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public int getChannelConfig() {
        return this.mChannelConfig;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelNames() {
        return this.mChannelNames;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mTerminalLink = stream.getByte();
        this.mControls = stream.getByte();
        this.mFormatType = stream.getByte();
        this.mFormats = stream.unpackUsbInt();
        this.mNumChannels = stream.getByte();
        this.mChannelConfig = stream.unpackUsbInt();
        this.mChannelNames = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Terminal Link: " + ((int) getTerminalLink()));
        canvas.writeListItem("Controls: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getControls()));
        canvas.writeListItem("Format Type: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getFormatType()));
        canvas.writeListItem("Formats: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getFormats()));
        canvas.writeListItem("Channel Count: " + ((int) getChannelCount()));
        canvas.writeListItem("Channel Config: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getChannelConfig()));
        canvas.writeListItem("Channel Names String ID: " + ((int) getChannelNames()));
        canvas.closeList();
    }
}
