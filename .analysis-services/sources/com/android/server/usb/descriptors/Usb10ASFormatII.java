package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb10ASFormatII extends com.android.server.usb.descriptors.UsbASFormat {
    private static final java.lang.String TAG = "Usb10ASFormatII";
    private int mMaxBitRate;
    private byte mSamFreqType;
    private int[] mSampleRates;
    private int mSamplesPerFrame;

    public Usb10ASFormatII(int length, byte type, byte subtype, byte formatType, int subclass) {
        super(length, type, subtype, formatType, subclass);
    }

    public int getMaxBitRate() {
        return this.mMaxBitRate;
    }

    public int getSamplesPerFrame() {
        return this.mSamplesPerFrame;
    }

    public byte getSamFreqType() {
        return this.mSamFreqType;
    }

    @Override // com.android.server.usb.descriptors.UsbASFormat
    public int[] getSampleRates() {
        return this.mSampleRates;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mMaxBitRate = stream.unpackUsbShort();
        this.mSamplesPerFrame = stream.unpackUsbShort();
        this.mSamFreqType = stream.getByte();
        int numFreqs = this.mSamFreqType == 0 ? 2 : this.mSamFreqType;
        this.mSampleRates = new int[numFreqs];
        for (int index = 0; index < numFreqs; index++) {
            this.mSampleRates[index] = stream.unpackUsbTriple();
        }
        int index2 = this.mLength;
        return index2;
    }

    @Override // com.android.server.usb.descriptors.UsbASFormat, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Max Bit Rate: " + getMaxBitRate());
        canvas.writeListItem("Samples Per Frame: " + getMaxBitRate());
        byte sampleFreqType = getSamFreqType();
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
