package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public abstract class UsbDescriptor implements com.android.server.usb.descriptors.report.Reporting {
    public static final int AUDIO_AUDIOCONTROL = 1;
    public static final int AUDIO_AUDIOSTREAMING = 2;
    public static final int AUDIO_MIDISTREAMING = 3;
    public static final int AUDIO_SUBCLASS_UNDEFINED = 0;
    public static final int CLASSID_APPSPECIFIC = 254;
    public static final int CLASSID_AUDIO = 1;
    public static final int CLASSID_AUDIOVIDEO = 16;
    public static final int CLASSID_BILLBOARD = 17;
    public static final int CLASSID_CDC_CONTROL = 10;
    public static final int CLASSID_COM = 2;
    public static final int CLASSID_DEVICE = 0;
    public static final int CLASSID_DIAGNOSTIC = 220;
    public static final int CLASSID_HEALTHCARE = 15;
    public static final int CLASSID_HID = 3;
    public static final int CLASSID_HUB = 9;
    public static final int CLASSID_IMAGE = 6;
    public static final int CLASSID_MISC = 239;
    public static final int CLASSID_PHYSICAL = 5;
    public static final int CLASSID_PRINTER = 7;
    public static final int CLASSID_SECURITY = 13;
    public static final int CLASSID_SMART_CARD = 11;
    public static final int CLASSID_STORAGE = 8;
    public static final int CLASSID_TYPECBRIDGE = 18;
    public static final int CLASSID_VENDSPECIFIC = 255;
    public static final int CLASSID_VIDEO = 14;
    public static final int CLASSID_WIRELESS = 224;
    public static final byte DESCRIPTORTYPE_BOS = 15;
    public static final byte DESCRIPTORTYPE_CAPABILITY = 16;
    public static final byte DESCRIPTORTYPE_CLASSSPECIFIC_ENDPOINT = 37;
    public static final byte DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE = 36;
    public static final byte DESCRIPTORTYPE_CONFIG = 2;
    public static final byte DESCRIPTORTYPE_DEVICE = 1;
    public static final byte DESCRIPTORTYPE_ENDPOINT = 5;
    public static final byte DESCRIPTORTYPE_ENDPOINT_COMPANION = 48;
    public static final byte DESCRIPTORTYPE_HID = 33;
    public static final byte DESCRIPTORTYPE_HUB = 41;
    public static final byte DESCRIPTORTYPE_INTERFACE = 4;
    public static final byte DESCRIPTORTYPE_INTERFACEASSOC = 11;
    public static final byte DESCRIPTORTYPE_PHYSICAL = 35;
    public static final byte DESCRIPTORTYPE_REPORT = 34;
    public static final byte DESCRIPTORTYPE_STRING = 3;
    public static final byte DESCRIPTORTYPE_SUPERSPEED_HUB = 42;
    public static final int REQUEST_CLEAR_FEATURE = 1;
    public static final int REQUEST_GET_ADDRESS = 5;
    public static final int REQUEST_GET_CONFIGURATION = 8;
    public static final int REQUEST_GET_DESCRIPTOR = 6;
    public static final int REQUEST_GET_STATUS = 0;
    public static final int REQUEST_SET_CONFIGURATION = 9;
    public static final int REQUEST_SET_DESCRIPTOR = 7;
    public static final int REQUEST_SET_FEATURE = 3;
    private static final int SIZE_STRINGBUFFER = 256;
    public static final int STATUS_PARSED_OK = 1;
    public static final int STATUS_PARSED_OVERRUN = 3;
    public static final int STATUS_PARSED_UNDERRUN = 2;
    public static final int STATUS_PARSE_EXCEPTION = 4;
    public static final int STATUS_UNPARSED = 0;
    private static final java.lang.String TAG = "UsbDescriptor";
    public static final int USB_CONTROL_TRANSFER_TIMEOUT_MS = 200;
    protected int mHierarchyLevel;
    protected final int mLength;
    private int mOverUnderRunCount;
    private byte[] mRawData;
    private int mStatus = 0;
    protected final byte mType;
    private static byte[] sStringBuffer = new byte[256];
    private static java.lang.String[] sStatusStrings = {"UNPARSED", "PARSED - OK", "PARSED - UNDERRUN", "PARSED - OVERRUN"};

    UsbDescriptor(int length, byte type) {
        if (length < 2) {
            throw new java.lang.IllegalArgumentException();
        }
        this.mLength = length;
        this.mType = type;
    }

    public int getLength() {
        return this.mLength;
    }

    public byte getType() {
        return this.mType;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public int getOverUnderRunCount() {
        return this.mOverUnderRunCount;
    }

    public java.lang.String getStatusString() {
        return sStatusStrings[this.mStatus];
    }

    public byte[] getRawData() {
        return this.mRawData;
    }

    public void postParse(com.android.server.usb.descriptors.ByteStream stream) {
        int bytesRead = stream.getReadCount();
        if (bytesRead < this.mLength) {
            stream.advance(this.mLength - bytesRead);
            this.mStatus = 2;
            this.mOverUnderRunCount = this.mLength - bytesRead;
            android.util.Log.w(TAG, "UNDERRUN t:0x" + java.lang.Integer.toHexString(this.mType) + " r: " + bytesRead + " < l: " + this.mLength);
            return;
        }
        if (bytesRead > this.mLength) {
            stream.reverse(bytesRead - this.mLength);
            this.mStatus = 3;
            this.mOverUnderRunCount = bytesRead - this.mLength;
            android.util.Log.w(TAG, "OVERRRUN t:0x" + java.lang.Integer.toHexString(this.mType) + " r: " + bytesRead + " > l: " + this.mLength);
            return;
        }
        this.mStatus = 1;
    }

    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        int numRead = stream.getReadCount();
        int dataLen = this.mLength - numRead;
        if (dataLen > 0) {
            this.mRawData = new byte[dataLen];
            for (int index = 0; index < dataLen; index++) {
                this.mRawData[index] = stream.getByte();
            }
        }
        int index2 = this.mLength;
        return index2;
    }

    public static java.lang.String getUsbDescriptorString(android.hardware.usb.UsbDeviceConnection connection, byte strIndex) {
        java.lang.String usbStr = "";
        if (strIndex == 0) {
            return "";
        }
        try {
            int rdo = connection.controlTransfer(128, 6, strIndex | 768, 0, sStringBuffer, 255, 200);
            if (rdo >= 0) {
                usbStr = new java.lang.String(sStringBuffer, 2, rdo - 2, "UTF-16LE");
                return usbStr;
            }
            return "?";
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Can not communicate with USB device", e);
            return usbStr;
        }
    }

    private void reportParseStatus(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        int status = getStatus();
        switch (status) {
            case 0:
            case 2:
            case 3:
                canvas.writeParagraph("status: " + getStatusString() + " [" + getOverUnderRunCount() + "]", true);
                break;
        }
    }

    @Override // com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        java.lang.String descTypeStr = com.android.server.usb.descriptors.report.UsbStrings.getDescriptorName(getType());
        java.lang.String text = descTypeStr + ": " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getType()) + " Len: " + getLength();
        if (this.mHierarchyLevel != 0) {
            canvas.writeHeader(this.mHierarchyLevel, text);
        } else {
            canvas.writeParagraph(text, false);
        }
        if (getStatus() != 1) {
            reportParseStatus(canvas);
        }
    }

    @Override // com.android.server.usb.descriptors.report.Reporting
    public void shortReport(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        java.lang.String descTypeStr = com.android.server.usb.descriptors.report.UsbStrings.getDescriptorName(getType());
        java.lang.String text = descTypeStr + ": " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getType()) + " Len: " + getLength();
        canvas.writeParagraph(text, false);
    }

    static java.lang.String getDescriptorName(byte descriptorType, int descriptorLength) {
        java.lang.String name = com.android.server.usb.descriptors.report.UsbStrings.getDescriptorName(descriptorType);
        if (name != null) {
            return name;
        }
        return "Unknown Descriptor Type " + ((int) descriptorType) + " 0x" + java.lang.Integer.toHexString(descriptorType) + " length:" + descriptorLength;
    }

    static void logDescriptorName(byte descriptorType, int descriptorLength) {
    }
}
