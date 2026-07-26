package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb10ASFormatI extends com.android.server.usb.descriptors.UsbASFormat {
    private static final java.lang.String TAG = "Usb10ASFormatI";
    private byte mBitResolution;
    private byte mNumChannels;
    private byte mSampleFreqType;
    private int[] mSampleRates;
    private byte mSubframeSize;

    public Usb10ASFormatI(int length, byte type, byte subtype, byte formatType, int subclass) {
        super(length, type, subtype, formatType, subclass);
    }

    public byte getNumChannels() {
        return this.mNumChannels;
    }

    public byte getSubframeSize() {
        return this.mSubframeSize;
    }

    public byte getBitResolution() {
        return this.mBitResolution;
    }

    public byte getSampleFreqType() {
        return this.mSampleFreqType;
    }

    @Override // com.android.server.usb.descriptors.UsbASFormat
    public int[] getSampleRates() {
        return this.mSampleRates;
    }

    @Override // com.android.server.usb.descriptors.UsbASFormat
    public int[] getBitDepths() {
        int[] depths = {this.mBitResolution};
        return depths;
    }

    @Override // com.android.server.usb.descriptors.UsbASFormat
    public int[] getChannelCounts() {
        int[] counts = {this.mNumChannels};
        return counts;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mNumChannels = stream.getByte();
        this.mSubframeSize = stream.getByte();
        this.mBitResolution = stream.getByte();
        this.mSampleFreqType = stream.getByte();
        if (this.mSampleFreqType == 0) {
            this.mSampleRates = new int[2];
            this.mSampleRates[0] = stream.unpackUsbTriple();
            this.mSampleRates[1] = stream.unpackUsbTriple();
        } else {
            this.mSampleRates = new int[this.mSampleFreqType];
            for (int index = 0; index < this.mSampleFreqType; index++) {
                this.mSampleRates[index] = stream.unpackUsbTriple();
            }
        }
        int index2 = this.mLength;
        return index2;
    }

    @Override // com.android.server.usb.descriptors.UsbASFormat, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("" + ((int) getNumChannels()) + " Channels.");
        canvas.writeListItem("Subframe Size: " + ((int) getSubframeSize()));
        canvas.writeListItem("Bit Resolution: " + ((int) getBitResolution()));
        byte sampleFreqType = getSampleFreqType();
        int[] sampleRates = getSampleRates();
        canvas.writeListItem("Sample Freq Type: " + ((int) sampleFreqType));
        canvas.openList();
        if (sampleFreqType == 0) {
            canvas.writeListItem("min: " + sampleRates[0]);
            canvas.writeListItem("max: " + sampleRates[1]);
        } else {
            for (int index = 0; index < sampleFreqType; index++) {
                canvas.writeListItem("" + sampleRates[index]);
            }
        }
        canvas.closeList();
        canvas.closeList();
    }
}
