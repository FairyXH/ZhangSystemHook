package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public class UsbInterfaceDescriptor extends com.android.server.usb.descriptors.UsbDescriptor {
    private static final java.lang.String TAG = "UsbInterfaceDescriptor";
    protected byte mAlternateSetting;
    protected byte mDescrIndex;
    private java.util.ArrayList<com.android.server.usb.descriptors.UsbEndpointDescriptor> mEndpointDescriptors;
    protected int mInterfaceNumber;
    private com.android.server.usb.descriptors.UsbDescriptor mMidiHeaderInterfaceDescriptor;
    protected byte mNumEndpoints;
    protected int mProtocol;
    protected int mUsbClass;
    protected int mUsbSubclass;

    UsbInterfaceDescriptor(int length, byte type) {
        super(length, type);
        this.mEndpointDescriptors = new java.util.ArrayList<>();
        this.mHierarchyLevel = 3;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mInterfaceNumber = stream.getUnsignedByte();
        this.mAlternateSetting = stream.getByte();
        this.mNumEndpoints = stream.getByte();
        this.mUsbClass = stream.getUnsignedByte();
        this.mUsbSubclass = stream.getUnsignedByte();
        this.mProtocol = stream.getUnsignedByte();
        this.mDescrIndex = stream.getByte();
        return this.mLength;
    }

    public int getInterfaceNumber() {
        return this.mInterfaceNumber;
    }

    public byte getAlternateSetting() {
        return this.mAlternateSetting;
    }

    public byte getNumEndpoints() {
        return this.mNumEndpoints;
    }

    public com.android.server.usb.descriptors.UsbEndpointDescriptor getEndpointDescriptor(int index) {
        if (index < 0 || index >= this.mEndpointDescriptors.size()) {
            return null;
        }
        return this.mEndpointDescriptors.get(index);
    }

    public int getUsbClass() {
        return this.mUsbClass;
    }

    public int getUsbSubclass() {
        return this.mUsbSubclass;
    }

    public int getProtocol() {
        return this.mProtocol;
    }

    public byte getDescrIndex() {
        return this.mDescrIndex;
    }

    void addEndpointDescriptor(com.android.server.usb.descriptors.UsbEndpointDescriptor endpoint) {
        this.mEndpointDescriptors.add(endpoint);
    }

    public void setMidiHeaderInterfaceDescriptor(com.android.server.usb.descriptors.UsbDescriptor descriptor) {
        this.mMidiHeaderInterfaceDescriptor = descriptor;
    }

    public com.android.server.usb.descriptors.UsbDescriptor getMidiHeaderInterfaceDescriptor() {
        return this.mMidiHeaderInterfaceDescriptor;
    }

    public android.hardware.usb.UsbInterface toAndroid(com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        java.lang.String name = parser.getDescriptorString(this.mDescrIndex);
        android.hardware.usb.UsbInterface ntrface = new android.hardware.usb.UsbInterface(this.mInterfaceNumber, this.mAlternateSetting, name, this.mUsbClass, this.mUsbSubclass, this.mProtocol);
        android.hardware.usb.UsbEndpoint[] endpoints = new android.hardware.usb.UsbEndpoint[this.mEndpointDescriptors.size()];
        for (int index = 0; index < this.mEndpointDescriptors.size(); index++) {
            endpoints[index] = this.mEndpointDescriptors.get(index).toAndroid(parser);
        }
        ntrface.setEndpoints(endpoints);
        return ntrface;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        int usbClass = getUsbClass();
        int usbSubclass = getUsbSubclass();
        int protocol = getProtocol();
        java.lang.String className = com.android.server.usb.descriptors.report.UsbStrings.getClassName(usbClass);
        java.lang.String subclassName = "";
        if (usbClass == 1) {
            subclassName = com.android.server.usb.descriptors.report.UsbStrings.getAudioSubclassName(usbSubclass);
        }
        canvas.openList();
        canvas.writeListItem("Interface #" + getInterfaceNumber());
        canvas.writeListItem("Class: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(usbClass) + ": " + className);
        canvas.writeListItem("Subclass: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(usbSubclass) + ": " + subclassName);
        canvas.writeListItem("Protocol: " + protocol + ": " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(protocol));
        canvas.writeListItem("Endpoints: " + ((int) getNumEndpoints()));
        canvas.closeList();
    }
}
