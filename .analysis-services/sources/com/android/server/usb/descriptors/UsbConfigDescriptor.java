package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbConfigDescriptor extends com.android.server.usb.descriptors.UsbDescriptor {
    private static final java.lang.String TAG = "UsbConfigDescriptor";
    private int mAttribs;
    private boolean mBlockAudio;
    private byte mConfigIndex;
    private int mConfigValue;
    private java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> mInterfaceDescriptors;
    private int mMaxPower;
    private byte mNumInterfaces;
    private int mTotalLength;

    UsbConfigDescriptor(int length, byte type) {
        super(length, type);
        this.mInterfaceDescriptors = new java.util.ArrayList<>();
        this.mHierarchyLevel = 2;
    }

    public int getTotalLength() {
        return this.mTotalLength;
    }

    public byte getNumInterfaces() {
        return this.mNumInterfaces;
    }

    public int getConfigValue() {
        return this.mConfigValue;
    }

    public byte getConfigIndex() {
        return this.mConfigIndex;
    }

    public int getAttribs() {
        return this.mAttribs;
    }

    public int getMaxPower() {
        return this.mMaxPower;
    }

    void addInterfaceDescriptor(com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDesc) {
        this.mInterfaceDescriptors.add(interfaceDesc);
    }

    java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> getInterfaceDescriptors() {
        return this.mInterfaceDescriptors;
    }

    private boolean isAudioInterface(com.android.server.usb.descriptors.UsbInterfaceDescriptor descriptor) {
        return descriptor.getUsbClass() == 1 && descriptor.getUsbSubclass() == 2;
    }

    android.hardware.usb.UsbConfiguration toAndroid(com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        java.lang.String name = parser.getDescriptorString(this.mConfigIndex);
        android.hardware.usb.UsbConfiguration config = new android.hardware.usb.UsbConfiguration(this.mConfigValue, name, this.mAttribs, this.mMaxPower);
        java.util.ArrayList<android.hardware.usb.UsbInterface> filteredInterfaces = new java.util.ArrayList<>();
        for (com.android.server.usb.descriptors.UsbInterfaceDescriptor descriptor : this.mInterfaceDescriptors) {
            if (!this.mBlockAudio || !isAudioInterface(descriptor)) {
                filteredInterfaces.add(descriptor.toAndroid(parser));
            }
        }
        android.hardware.usb.UsbInterface[] interfaceArray = new android.hardware.usb.UsbInterface[0];
        config.setInterfaces((android.hardware.usb.UsbInterface[]) filteredInterfaces.toArray(interfaceArray));
        return config;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mTotalLength = stream.unpackUsbShort();
        this.mNumInterfaces = stream.getByte();
        this.mConfigValue = stream.getUnsignedByte();
        this.mConfigIndex = stream.getByte();
        this.mAttribs = stream.getUnsignedByte();
        this.mMaxPower = stream.getUnsignedByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Config # " + getConfigValue());
        canvas.writeListItem(((int) getNumInterfaces()) + " Interfaces.");
        canvas.writeListItem("Attributes: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getAttribs()));
        canvas.closeList();
    }
}
