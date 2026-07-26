package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDeviceDescriptor extends com.android.server.usb.descriptors.UsbDescriptor {
    private static final java.lang.String TAG = "UsbDeviceDescriptor";
    public static final int USBSPEC_1_0 = 256;
    public static final int USBSPEC_1_1 = 272;
    public static final int USBSPEC_2_0 = 512;
    private java.util.ArrayList<com.android.server.usb.descriptors.UsbConfigDescriptor> mConfigDescriptors;
    private int mDevClass;
    private int mDevSubClass;
    private int mDeviceRelease;
    private byte mMfgIndex;
    private byte mNumConfigs;
    private byte mPacketSize;
    private int mProductID;
    private byte mProductIndex;
    private int mProtocol;
    private byte mSerialIndex;
    private int mSpec;
    private int mVendorID;

    UsbDeviceDescriptor(int length, byte type) {
        super(length, type);
        this.mConfigDescriptors = new java.util.ArrayList<>();
        this.mHierarchyLevel = 1;
    }

    public int getSpec() {
        return this.mSpec;
    }

    public int getDevClass() {
        return this.mDevClass;
    }

    public int getDevSubClass() {
        return this.mDevSubClass;
    }

    public int getProtocol() {
        return this.mProtocol;
    }

    public byte getPacketSize() {
        return this.mPacketSize;
    }

    public int getVendorID() {
        return this.mVendorID;
    }

    public int getProductID() {
        return this.mProductID;
    }

    public int getDeviceRelease() {
        return this.mDeviceRelease;
    }

    public java.lang.String getDeviceReleaseString() {
        int hundredths = this.mDeviceRelease & 15;
        int tenths = (this.mDeviceRelease & com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED) >> 4;
        int ones = (this.mDeviceRelease & 3840) >> 8;
        int tens = (this.mDeviceRelease & 61440) >> 12;
        return java.lang.String.format("%d.%d%d", java.lang.Integer.valueOf((tens * 10) + ones), java.lang.Integer.valueOf(tenths), java.lang.Integer.valueOf(hundredths));
    }

    public byte getMfgIndex() {
        return this.mMfgIndex;
    }

    public java.lang.String getMfgString(com.android.server.usb.descriptors.UsbDescriptorParser p) {
        return p.getDescriptorString(this.mMfgIndex);
    }

    public byte getProductIndex() {
        return this.mProductIndex;
    }

    public java.lang.String getProductString(com.android.server.usb.descriptors.UsbDescriptorParser p) {
        return p.getDescriptorString(this.mProductIndex);
    }

    public byte getSerialIndex() {
        return this.mSerialIndex;
    }

    public java.lang.String getSerialString(com.android.server.usb.descriptors.UsbDescriptorParser p) {
        return p.getDescriptorString(this.mSerialIndex);
    }

    public byte getNumConfigs() {
        return this.mNumConfigs;
    }

    void addConfigDescriptor(com.android.server.usb.descriptors.UsbConfigDescriptor config) {
        this.mConfigDescriptors.add(config);
    }

    public android.hardware.usb.UsbDevice.Builder toAndroid(com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        java.lang.String mfgName = getMfgString(parser);
        java.lang.String prodName = getProductString(parser);
        java.lang.String versionString = getDeviceReleaseString();
        java.lang.String serialStr = getSerialString(parser);
        android.hardware.usb.UsbConfiguration[] configs = new android.hardware.usb.UsbConfiguration[this.mConfigDescriptors.size()];
        android.util.Log.d(TAG, "  " + configs.length + " configs");
        for (int index = 0; index < this.mConfigDescriptors.size(); index++) {
            configs[index] = this.mConfigDescriptors.get(index).toAndroid(parser);
        }
        return new android.hardware.usb.UsbDevice.Builder(parser.getDeviceAddr(), this.mVendorID, this.mProductID, this.mDevClass, this.mDevSubClass, this.mProtocol, mfgName, prodName, versionString, configs, serialStr, parser.hasAudioPlayback(), parser.hasAudioCapture(), parser.hasMIDIInterface(), parser.hasVideoPlayback(), parser.hasVideoCapture());
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mSpec = stream.unpackUsbShort();
        this.mDevClass = stream.getUnsignedByte();
        this.mDevSubClass = stream.getUnsignedByte();
        this.mProtocol = stream.getUnsignedByte();
        this.mPacketSize = stream.getByte();
        this.mVendorID = stream.unpackUsbShort();
        this.mProductID = stream.unpackUsbShort();
        this.mDeviceRelease = stream.unpackUsbShort();
        this.mMfgIndex = stream.getByte();
        this.mProductIndex = stream.getByte();
        this.mSerialIndex = stream.getByte();
        this.mNumConfigs = stream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        int spec = getSpec();
        canvas.writeListItem("Spec: " + com.android.server.usb.descriptors.report.ReportCanvas.getBCDString(spec));
        int devClass = getDevClass();
        java.lang.String classStr = com.android.server.usb.descriptors.report.UsbStrings.getClassName(devClass);
        int devSubClass = getDevSubClass();
        java.lang.String subClasStr = com.android.server.usb.descriptors.report.UsbStrings.getClassName(devSubClass);
        canvas.writeListItem("Class " + devClass + ": " + classStr + " Subclass" + devSubClass + ": " + subClasStr);
        canvas.writeListItem("Vendor ID: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getVendorID()) + " Product ID: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getProductID()) + " Product Release: " + com.android.server.usb.descriptors.report.ReportCanvas.getBCDString(getDeviceRelease()));
        com.android.server.usb.descriptors.UsbDescriptorParser parser = canvas.getParser();
        byte mfgIndex = getMfgIndex();
        java.lang.String manufacturer = parser.getDescriptorString(mfgIndex);
        byte productIndex = getProductIndex();
        java.lang.String product = parser.getDescriptorString(productIndex);
        canvas.writeListItem("Manufacturer " + ((int) mfgIndex) + ": " + manufacturer + " Product " + ((int) productIndex) + ": " + product);
        canvas.closeList();
    }
}
