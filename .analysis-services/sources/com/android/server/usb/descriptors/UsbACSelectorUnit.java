package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbACSelectorUnit extends com.android.server.usb.descriptors.UsbACInterface {
    private static final java.lang.String TAG = "UsbACSelectorUnit";
    private byte mNameIndex;
    private byte mNumPins;
    private byte[] mSourceIDs;
    private byte mUnitID;

    public UsbACSelectorUnit(int length, byte type, byte subtype, int subClass) {
        super(length, type, subtype, subClass);
    }

    public byte getUnitID() {
        return this.mUnitID;
    }

    public byte getNumPins() {
        return this.mNumPins;
    }

    public byte[] getSourceIDs() {
        return this.mSourceIDs;
    }

    public byte getNameIndex() {
        return this.mNameIndex;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mUnitID = stream.getByte();
        this.mNumPins = stream.getByte();
        this.mSourceIDs = new byte[this.mNumPins];
        for (int index = 0; index < this.mNumPins; index++) {
            this.mSourceIDs[index] = stream.getByte();
        }
        this.mNameIndex = stream.getByte();
        return this.mLength;
    }
}
