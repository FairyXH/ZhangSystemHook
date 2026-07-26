package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb20ACInputTerminal extends com.android.server.usb.descriptors.UsbACTerminal implements com.android.server.usb.descriptors.UsbAudioChannelCluster {
    private static final java.lang.String TAG = "Usb20ACInputTerminal";
    private int mChanConfig;
    private byte mChanNames;
    private byte mClkSourceID;
    private int mControls;
    private byte mNumChannels;
    private byte mTerminalName;

    public Usb20ACInputTerminal(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    public byte getClkSourceID() {
        return this.mClkSourceID;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelCount() {
        return this.mNumChannels;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public int getChannelConfig() {
        return this.mChanConfig;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelNames() {
        return this.mChanNames;
    }

    public int getControls() {
        return this.mControls;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mClkSourceID = stream.getByte();
        this.mNumChannels = stream.getByte();
        this.mChanConfig = stream.unpackUsbInt();
        this.mChanNames = stream.getByte();
        this.mControls = stream.unpackUsbShort();
        this.mTerminalName = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACTerminal, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Clock Source: " + ((int) getClkSourceID()));
        canvas.writeListItem("" + ((int) getChannelCount()) + " Channels. Config: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getChannelConfig()));
        canvas.closeList();
    }
}
