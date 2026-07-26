package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb20ACMixerUnit extends com.android.server.usb.descriptors.UsbACMixerUnit implements com.android.server.usb.descriptors.UsbAudioChannelCluster {
    private static final java.lang.String TAG = "Usb20ACMixerUnit";
    private int mChanConfig;
    private byte mChanNames;
    private byte[] mControls;
    private byte mControlsMask;
    private byte mNameID;

    public Usb20ACMixerUnit(int length, byte type, byte subtype, int subClass) {
        super(length, type, subtype, subClass);
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelCount() {
        return this.mNumOutputs;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public int getChannelConfig() {
        return this.mChanConfig;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelNames() {
        return this.mChanNames;
    }

    @Override // com.android.server.usb.descriptors.UsbACMixerUnit, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        super.parseRawDescriptors(stream);
        this.mChanConfig = stream.unpackUsbInt();
        this.mChanNames = stream.getByte();
        int controlArraySize = calcControlArraySize(this.mNumInputs, this.mNumOutputs);
        this.mControls = new byte[controlArraySize];
        for (int index = 0; index < controlArraySize; index++) {
            this.mControls[index] = stream.getByte();
        }
        this.mControlsMask = stream.getByte();
        this.mNameID = stream.getByte();
        return this.mLength;
    }
}
