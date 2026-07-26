package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public abstract class UsbACInterface extends com.android.server.usb.descriptors.UsbDescriptor {
    public static final byte ACI_CLOCK_MULTIPLIER = 12;
    public static final byte ACI_CLOCK_SELECTOR = 11;
    public static final byte ACI_CLOCK_SOURCE = 10;
    public static final byte ACI_EXTENSION_UNIT = 8;
    public static final byte ACI_FEATURE_UNIT = 6;
    public static final byte ACI_HEADER = 1;
    public static final byte ACI_INPUT_TERMINAL = 2;
    public static final byte ACI_MIXER_UNIT = 4;
    public static final byte ACI_OUTPUT_TERMINAL = 3;
    public static final byte ACI_PROCESSING_UNIT = 7;
    public static final byte ACI_SAMPLE_RATE_CONVERTER = 13;
    public static final byte ACI_SELECTOR_UNIT = 5;
    public static final byte ACI_UNDEFINED = 0;
    public static final byte ASI_FORMAT_SPECIFIC = 3;
    public static final byte ASI_FORMAT_TYPE = 2;
    public static final byte ASI_GENERAL = 1;
    public static final byte ASI_UNDEFINED = 0;
    public static final int FORMAT_III_IEC1937AC3 = 8193;
    public static final int FORMAT_III_IEC1937_MPEG1_Layer1 = 8194;
    public static final int FORMAT_III_IEC1937_MPEG1_Layer2 = 8195;
    public static final int FORMAT_III_IEC1937_MPEG2_EXT = 8196;
    public static final int FORMAT_III_IEC1937_MPEG2_Layer1LS = 8197;
    public static final int FORMAT_III_UNDEFINED = 8192;
    public static final int FORMAT_II_AC3 = 4098;
    public static final int FORMAT_II_MPEG = 4097;
    public static final int FORMAT_II_UNDEFINED = 4096;
    public static final int FORMAT_I_ALAW = 4;
    public static final int FORMAT_I_IEEE_FLOAT = 3;
    public static final int FORMAT_I_MULAW = 5;
    public static final int FORMAT_I_PCM = 1;
    public static final int FORMAT_I_PCM8 = 2;
    public static final int FORMAT_I_UNDEFINED = 0;
    public static final byte MSI_ELEMENT = 4;
    public static final byte MSI_HEADER = 1;
    public static final byte MSI_IN_JACK = 2;
    public static final byte MSI_OUT_JACK = 3;
    public static final byte MSI_UNDEFINED = 0;
    private static final java.lang.String TAG = "UsbACInterface";
    protected final int mSubclass;
    protected final byte mSubtype;

    public UsbACInterface(int length, byte type, byte subtype, int subclass) {
        super(length, type);
        this.mSubtype = subtype;
        this.mSubclass = subclass;
    }

    public byte getSubtype() {
        return this.mSubtype;
    }

    public int getSubclass() {
        return this.mSubclass;
    }

    private static com.android.server.usb.descriptors.UsbDescriptor allocAudioControlDescriptor(com.android.server.usb.descriptors.UsbDescriptorParser parser, com.android.server.usb.descriptors.ByteStream stream, int length, byte type, byte subtype, int subClass) {
        switch (subtype) {
            case 1:
                int acInterfaceSpec = stream.unpackUsbShort();
                parser.setACInterfaceSpec(acInterfaceSpec);
                if (acInterfaceSpec == 512) {
                    return new com.android.server.usb.descriptors.Usb20ACHeader(length, type, subtype, subClass, acInterfaceSpec);
                }
                return new com.android.server.usb.descriptors.Usb10ACHeader(length, type, subtype, subClass, acInterfaceSpec);
            case 2:
                if (parser.getACInterfaceSpec() == 512) {
                    return new com.android.server.usb.descriptors.Usb20ACInputTerminal(length, type, subtype, subClass);
                }
                return new com.android.server.usb.descriptors.Usb10ACInputTerminal(length, type, subtype, subClass);
            case 3:
                if (parser.getACInterfaceSpec() == 512) {
                    return new com.android.server.usb.descriptors.Usb20ACOutputTerminal(length, type, subtype, subClass);
                }
                return new com.android.server.usb.descriptors.Usb10ACOutputTerminal(length, type, subtype, subClass);
            case 4:
                if (parser.getACInterfaceSpec() == 512) {
                    return new com.android.server.usb.descriptors.Usb20ACMixerUnit(length, type, subtype, subClass);
                }
                return new com.android.server.usb.descriptors.Usb10ACMixerUnit(length, type, subtype, subClass);
            case 5:
                return new com.android.server.usb.descriptors.UsbACSelectorUnit(length, type, subtype, subClass);
            case 6:
                return new com.android.server.usb.descriptors.UsbACFeatureUnit(length, type, subtype, subClass);
            default:
                android.util.Log.w(TAG, "Unknown Audio Class Interface subtype:0x" + java.lang.Integer.toHexString(subtype));
                return new com.android.server.usb.descriptors.UsbACInterfaceUnparsed(length, type, subtype, subClass);
        }
    }

    private static com.android.server.usb.descriptors.UsbDescriptor allocAudioStreamingDescriptor(com.android.server.usb.descriptors.UsbDescriptorParser parser, com.android.server.usb.descriptors.ByteStream stream, int length, byte type, byte subtype, int subClass) {
        int acInterfaceSpec = parser.getACInterfaceSpec();
        switch (subtype) {
            case 1:
                if (acInterfaceSpec == 512) {
                    return new com.android.server.usb.descriptors.Usb20ASGeneral(length, type, subtype, subClass);
                }
                return new com.android.server.usb.descriptors.Usb10ASGeneral(length, type, subtype, subClass);
            case 2:
                return com.android.server.usb.descriptors.UsbASFormat.allocDescriptor(parser, stream, length, type, subtype, subClass);
            default:
                android.util.Log.w(TAG, "Unknown Audio Streaming Interface subtype:0x" + java.lang.Integer.toHexString(subtype));
                return null;
        }
    }

    private static com.android.server.usb.descriptors.UsbDescriptor allocMidiStreamingDescriptor(int length, byte type, byte subtype, int subClass) {
        switch (subtype) {
            case 1:
                return new com.android.server.usb.descriptors.UsbMSMidiHeader(length, type, subtype, subClass);
            case 2:
                return new com.android.server.usb.descriptors.UsbMSMidiInputJack(length, type, subtype, subClass);
            case 3:
                return new com.android.server.usb.descriptors.UsbMSMidiOutputJack(length, type, subtype, subClass);
            default:
                android.util.Log.w(TAG, "Unknown MIDI Streaming Interface subtype:0x" + java.lang.Integer.toHexString(subtype));
                return null;
        }
    }

    public static com.android.server.usb.descriptors.UsbDescriptor allocDescriptor(com.android.server.usb.descriptors.UsbDescriptorParser parser, com.android.server.usb.descriptors.ByteStream stream, int length, byte type) {
        byte subtype = stream.getByte();
        com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDesc = parser.getCurInterface();
        int subClass = interfaceDesc.getUsbSubclass();
        switch (subClass) {
            case 1:
                return allocAudioControlDescriptor(parser, stream, length, type, subtype, subClass);
            case 2:
                return allocAudioStreamingDescriptor(parser, stream, length, type, subtype, subClass);
            case 3:
                return allocMidiStreamingDescriptor(length, type, subtype, subClass);
            default:
                android.util.Log.w(TAG, "Unknown Audio Class Interface Subclass: 0x" + java.lang.Integer.toHexString(subClass));
                return null;
        }
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        int subClass = getSubclass();
        java.lang.String subClassName = com.android.server.usb.descriptors.report.UsbStrings.getACInterfaceSubclassName(subClass);
        byte subtype = getSubtype();
        java.lang.String subTypeName = com.android.server.usb.descriptors.report.UsbStrings.getACControlInterfaceName(subtype);
        canvas.openList();
        canvas.writeListItem("Subclass: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(subClass) + " " + subClassName);
        canvas.writeListItem("Subtype: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(subtype) + " " + subTypeName);
        canvas.closeList();
    }
}
