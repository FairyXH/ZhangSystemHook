package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDescriptorParser {
    public static final boolean DEBUG = false;
    private static final int DESCRIPTORS_ALLOC_SIZE = 128;
    private static final float IN_HEADSET_TRIGGER = 0.75f;
    private static final int MS_MIDI_1_0 = 256;
    private static final int MS_MIDI_2_0 = 512;
    private static final float OUT_HEADSET_TRIGGER = 0.75f;
    private static final java.lang.String TAG = "UsbDescriptorParser";
    private int mACInterfacesSpec;
    private com.android.server.usb.descriptors.UsbConfigDescriptor mCurConfigDescriptor;
    private com.android.server.usb.descriptors.UsbEndpointDescriptor mCurEndpointDescriptor;
    private com.android.server.usb.descriptors.UsbInterfaceDescriptor mCurInterfaceDescriptor;
    private final java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> mDescriptors;
    private final java.lang.String mDeviceAddr;
    private com.android.server.usb.descriptors.UsbDeviceDescriptor mDeviceDescriptor;
    private int mVCInterfacesSpec;

    private native java.lang.String getDescriptorString_native(java.lang.String str, int i);

    private native byte[] getRawDescriptors_native(java.lang.String str);

    public UsbDescriptorParser(java.lang.String deviceAddr, java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors) {
        this.mACInterfacesSpec = 256;
        this.mVCInterfacesSpec = 256;
        this.mDeviceAddr = deviceAddr;
        this.mDescriptors = descriptors;
        this.mDeviceDescriptor = (com.android.server.usb.descriptors.UsbDeviceDescriptor) descriptors.get(0);
    }

    public UsbDescriptorParser(java.lang.String deviceAddr, byte[] rawDescriptors) throws com.android.server.usb.descriptors.UsbDescriptorParser.UsbDescriptorsStreamFormatException {
        this.mACInterfacesSpec = 256;
        this.mVCInterfacesSpec = 256;
        this.mDeviceAddr = deviceAddr;
        this.mDescriptors = new java.util.ArrayList<>(128);
        parseDescriptors(rawDescriptors);
    }

    public java.lang.String getDeviceAddr() {
        return this.mDeviceAddr;
    }

    public int getUsbSpec() {
        if (this.mDeviceDescriptor != null) {
            return this.mDeviceDescriptor.getSpec();
        }
        throw new java.lang.IllegalArgumentException();
    }

    public void setACInterfaceSpec(int spec) {
        this.mACInterfacesSpec = spec;
    }

    public int getACInterfaceSpec() {
        return this.mACInterfacesSpec;
    }

    public void setVCInterfaceSpec(int spec) {
        this.mVCInterfacesSpec = spec;
    }

    public int getVCInterfaceSpec() {
        return this.mVCInterfacesSpec;
    }

    private class UsbDescriptorsStreamFormatException extends java.lang.Exception {
        java.lang.String mMessage;

        UsbDescriptorsStreamFormatException(java.lang.String message) {
            this.mMessage = message;
        }

        @Override // java.lang.Throwable
        public java.lang.String toString() {
            return "Descriptor Stream Format Exception: " + this.mMessage;
        }
    }

    private com.android.server.usb.descriptors.UsbDescriptor allocDescriptor(com.android.server.usb.descriptors.ByteStream stream) throws com.android.server.usb.descriptors.UsbDescriptorParser.UsbDescriptorsStreamFormatException {
        stream.resetReadCount();
        int length = stream.getUnsignedByte();
        byte type = stream.getByte();
        com.android.server.usb.descriptors.UsbDescriptor.logDescriptorName(type, length);
        com.android.server.usb.descriptors.UsbDescriptor descriptor = null;
        switch (type) {
            case 1:
                com.android.server.usb.descriptors.UsbDeviceDescriptor usbDeviceDescriptor = new com.android.server.usb.descriptors.UsbDeviceDescriptor(length, type);
                this.mDeviceDescriptor = usbDeviceDescriptor;
                descriptor = usbDeviceDescriptor;
                break;
            case 2:
                com.android.server.usb.descriptors.UsbConfigDescriptor usbConfigDescriptor = new com.android.server.usb.descriptors.UsbConfigDescriptor(length, type);
                this.mCurConfigDescriptor = usbConfigDescriptor;
                descriptor = usbConfigDescriptor;
                if (this.mDeviceDescriptor == null) {
                    android.util.Log.e(TAG, "Config Descriptor found with no associated Device Descriptor!");
                    throw new com.android.server.usb.descriptors.UsbDescriptorParser.UsbDescriptorsStreamFormatException("Config Descriptor found with no associated Device Descriptor!");
                }
                this.mDeviceDescriptor.addConfigDescriptor(this.mCurConfigDescriptor);
                break;
                break;
            case 4:
                com.android.server.usb.descriptors.UsbInterfaceDescriptor usbInterfaceDescriptor = new com.android.server.usb.descriptors.UsbInterfaceDescriptor(length, type);
                this.mCurInterfaceDescriptor = usbInterfaceDescriptor;
                descriptor = usbInterfaceDescriptor;
                if (this.mCurConfigDescriptor == null) {
                    android.util.Log.e(TAG, "Interface Descriptor found with no associated Config Descriptor!");
                    throw new com.android.server.usb.descriptors.UsbDescriptorParser.UsbDescriptorsStreamFormatException("Interface Descriptor found with no associated Config Descriptor!");
                }
                this.mCurConfigDescriptor.addInterfaceDescriptor(this.mCurInterfaceDescriptor);
                break;
                break;
            case 5:
                com.android.server.usb.descriptors.UsbEndpointDescriptor usbEndpointDescriptor = new com.android.server.usb.descriptors.UsbEndpointDescriptor(length, type);
                this.mCurEndpointDescriptor = usbEndpointDescriptor;
                descriptor = usbEndpointDescriptor;
                if (this.mCurInterfaceDescriptor == null) {
                    android.util.Log.e(TAG, "Endpoint Descriptor found with no associated Interface Descriptor!");
                    throw new com.android.server.usb.descriptors.UsbDescriptorParser.UsbDescriptorsStreamFormatException("Endpoint Descriptor found with no associated Interface Descriptor!");
                }
                this.mCurInterfaceDescriptor.addEndpointDescriptor((com.android.server.usb.descriptors.UsbEndpointDescriptor) descriptor);
                break;
                break;
            case 11:
                descriptor = new com.android.server.usb.descriptors.UsbInterfaceAssoc(length, type);
                break;
            case 33:
                descriptor = new com.android.server.usb.descriptors.UsbHIDDescriptor(length, type);
                break;
            case 36:
                if (this.mCurInterfaceDescriptor != null) {
                    switch (this.mCurInterfaceDescriptor.getUsbClass()) {
                        case 1:
                            descriptor = com.android.server.usb.descriptors.UsbACInterface.allocDescriptor(this, stream, length, type);
                            if (descriptor instanceof com.android.server.usb.descriptors.UsbMSMidiHeader) {
                                this.mCurInterfaceDescriptor.setMidiHeaderInterfaceDescriptor(descriptor);
                            }
                            break;
                        case 14:
                            descriptor = com.android.server.usb.descriptors.UsbVCInterface.allocDescriptor(this, stream, length, type);
                            break;
                        case 16:
                            break;
                        default:
                            android.util.Log.w(TAG, "  Unparsed Class-specific");
                            break;
                    }
                }
                break;
            case 37:
                if (this.mCurInterfaceDescriptor != null) {
                    int subClass = this.mCurInterfaceDescriptor.getUsbClass();
                    switch (subClass) {
                        case 1:
                            java.lang.Byte subType = java.lang.Byte.valueOf(stream.getByte());
                            descriptor = com.android.server.usb.descriptors.UsbACEndpoint.allocDescriptor(this, length, type, subType.byteValue());
                            break;
                        case 14:
                            java.lang.Byte subType2 = java.lang.Byte.valueOf(stream.getByte());
                            descriptor = com.android.server.usb.descriptors.UsbVCEndpoint.allocDescriptor(this, length, type, subType2.byteValue());
                            break;
                        case 16:
                            break;
                        default:
                            android.util.Log.w(TAG, "  Unparsed Class-specific Endpoint:0x" + java.lang.Integer.toHexString(subClass));
                            break;
                    }
                    if (this.mCurEndpointDescriptor != null && descriptor != null) {
                        this.mCurEndpointDescriptor.setClassSpecificEndpointDescriptor(descriptor);
                    }
                }
                break;
        }
        if (descriptor == null) {
            return new com.android.server.usb.descriptors.UsbUnknown(length, type);
        }
        return descriptor;
    }

    public com.android.server.usb.descriptors.UsbDeviceDescriptor getDeviceDescriptor() {
        return this.mDeviceDescriptor;
    }

    public com.android.server.usb.descriptors.UsbInterfaceDescriptor getCurInterface() {
        return this.mCurInterfaceDescriptor;
    }

    public void parseDescriptors(byte[] descriptors) throws com.android.server.usb.descriptors.UsbDescriptorParser.UsbDescriptorsStreamFormatException {
        com.android.server.usb.descriptors.ByteStream stream = new com.android.server.usb.descriptors.ByteStream(descriptors);
        while (stream.available() > 0) {
            com.android.server.usb.descriptors.UsbDescriptor descriptor = null;
            try {
                descriptor = allocDescriptor(stream);
            } catch (java.lang.Exception ex) {
                android.util.Log.e(TAG, "Exception allocating USB descriptor.", ex);
            }
            if (descriptor != null) {
                try {
                    try {
                        descriptor.parseRawDescriptors(stream);
                        descriptor.postParse(stream);
                    } catch (java.lang.Exception ex2) {
                        descriptor.postParse(stream);
                        android.util.Log.w(TAG, "Exception parsing USB descriptors. type:0x" + ((int) descriptor.getType()) + " status:" + descriptor.getStatus());
                        java.lang.StackTraceElement[] stackElems = ex2.getStackTrace();
                        if (stackElems.length > 0) {
                            android.util.Log.i(TAG, "  class:" + stackElems[0].getClassName() + " @ " + stackElems[0].getLineNumber());
                        }
                        if (stackElems.length > 1) {
                            android.util.Log.i(TAG, "  class:" + stackElems[1].getClassName() + " @ " + stackElems[1].getLineNumber());
                        }
                        descriptor.setStatus(4);
                    }
                } finally {
                    this.mDescriptors.add(descriptor);
                }
            }
        }
    }

    public byte[] getRawDescriptors() {
        return getRawDescriptors_native(this.mDeviceAddr);
    }

    public java.lang.String getDescriptorString(int stringId) {
        return getDescriptorString_native(this.mDeviceAddr, stringId);
    }

    public int getParsingSpec() {
        if (this.mDeviceDescriptor != null) {
            return this.mDeviceDescriptor.getSpec();
        }
        return 0;
    }

    public java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> getDescriptors() {
        return this.mDescriptors;
    }

    public android.hardware.usb.UsbDevice.Builder toAndroidUsbDeviceBuilder() {
        if (this.mDeviceDescriptor == null) {
            android.util.Log.e(TAG, "toAndroidUsbDevice() ERROR - No Device Descriptor");
            return null;
        }
        android.hardware.usb.UsbDevice.Builder builder = this.mDeviceDescriptor.toAndroid(this);
        if (builder == null) {
            android.util.Log.e(TAG, "toAndroidUsbDevice() ERROR Creating Device");
        }
        return builder;
    }

    public java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> getDescriptors(byte type) {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> list = new java.util.ArrayList<>();
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : this.mDescriptors) {
            if (descriptor.getType() == type) {
                list.add(descriptor);
            }
        }
        return list;
    }

    public java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> getInterfaceDescriptorsForClass(int usbClass) {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> list = new java.util.ArrayList<>();
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : this.mDescriptors) {
            if (descriptor.getType() == 4) {
                if (descriptor instanceof com.android.server.usb.descriptors.UsbInterfaceDescriptor) {
                    com.android.server.usb.descriptors.UsbInterfaceDescriptor intrDesc = (com.android.server.usb.descriptors.UsbInterfaceDescriptor) descriptor;
                    if (intrDesc.getUsbClass() == usbClass) {
                        list.add(descriptor);
                    }
                } else {
                    android.util.Log.w(TAG, "Unrecognized Interface l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
                }
            }
        }
        return list;
    }

    public java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> getACInterfaceDescriptors(byte subtype, int subclass) {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> list = new java.util.ArrayList<>();
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : this.mDescriptors) {
            if (descriptor.getType() == 36) {
                if (descriptor instanceof com.android.server.usb.descriptors.UsbACInterface) {
                    com.android.server.usb.descriptors.UsbACInterface acDescriptor = (com.android.server.usb.descriptors.UsbACInterface) descriptor;
                    if (acDescriptor.getSubtype() == subtype && acDescriptor.getSubclass() == subclass) {
                        list.add(descriptor);
                    }
                } else {
                    android.util.Log.w(TAG, "Unrecognized Audio Interface len: " + descriptor.getLength() + " type:0x" + java.lang.Integer.toHexString(descriptor.getType()));
                }
            }
        }
        return list;
    }

    public boolean hasInput() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> acDescriptors = getACInterfaceDescriptors((byte) 2, 1);
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : acDescriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbACTerminal) {
                com.android.server.usb.descriptors.UsbACTerminal inDescr = (com.android.server.usb.descriptors.UsbACTerminal) descriptor;
                int type = inDescr.getTerminalType();
                int terminalCategory = type & (-256);
                if (terminalCategory != 256 && terminalCategory != 768) {
                    return true;
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Input terminal l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        return false;
    }

    public boolean hasOutput() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> acDescriptors = getACInterfaceDescriptors((byte) 3, 1);
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : acDescriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbACTerminal) {
                com.android.server.usb.descriptors.UsbACTerminal outDescr = (com.android.server.usb.descriptors.UsbACTerminal) descriptor;
                int type = outDescr.getTerminalType();
                int terminalCategory = type & (-256);
                if (terminalCategory != 256 && terminalCategory != 512) {
                    return true;
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Input terminal l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        return false;
    }

    public boolean hasMic() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> acDescriptors = getACInterfaceDescriptors((byte) 2, 1);
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : acDescriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbACTerminal) {
                com.android.server.usb.descriptors.UsbACTerminal inDescr = (com.android.server.usb.descriptors.UsbACTerminal) descriptor;
                if (inDescr.getTerminalType() == 513 || inDescr.getTerminalType() == 1026 || inDescr.getTerminalType() == 1024 || inDescr.getTerminalType() == 1539) {
                    return true;
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Input terminal l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        return false;
    }

    public boolean hasSpeaker() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> acDescriptors = getACInterfaceDescriptors((byte) 3, 1);
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : acDescriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbACTerminal) {
                com.android.server.usb.descriptors.UsbACTerminal outDescr = (com.android.server.usb.descriptors.UsbACTerminal) descriptor;
                if (outDescr.getTerminalType() == 769 || outDescr.getTerminalType() == 770 || outDescr.getTerminalType() == 1026) {
                    return true;
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Output terminal l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        return false;
    }

    public boolean hasAudioInterface() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors = getInterfaceDescriptorsForClass(1);
        return true ^ descriptors.isEmpty();
    }

    public boolean hasAudioTerminal(int subType, int terminalType) {
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : this.mDescriptors) {
            if ((descriptor instanceof com.android.server.usb.descriptors.UsbACTerminal) && ((com.android.server.usb.descriptors.UsbACTerminal) descriptor).getSubclass() == 1 && ((com.android.server.usb.descriptors.UsbACTerminal) descriptor).getSubtype() == subType && ((com.android.server.usb.descriptors.UsbACTerminal) descriptor).getTerminalType() == terminalType) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAudioTerminalExcludeType(int subType, int excludedTerminalType) {
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : this.mDescriptors) {
            if ((descriptor instanceof com.android.server.usb.descriptors.UsbACTerminal) && ((com.android.server.usb.descriptors.UsbACTerminal) descriptor).getSubclass() == 1 && ((com.android.server.usb.descriptors.UsbACTerminal) descriptor).getSubtype() == subType && ((com.android.server.usb.descriptors.UsbACTerminal) descriptor).getTerminalType() != excludedTerminalType) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAudioPlayback() {
        return hasAudioTerminalExcludeType(3, 257) && hasAudioTerminal(2, 257);
    }

    public boolean hasAudioCapture() {
        return hasAudioTerminalExcludeType(2, 257) && hasAudioTerminal(3, 257);
    }

    public boolean hasVideoCapture() {
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : this.mDescriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbVCInputTerminal) {
                return true;
            }
        }
        return false;
    }

    public boolean hasVideoPlayback() {
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : this.mDescriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbVCOutputTerminal) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHIDInterface() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors = getInterfaceDescriptorsForClass(3);
        return !descriptors.isEmpty();
    }

    public boolean hasStorageInterface() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors = getInterfaceDescriptorsForClass(8);
        return !descriptors.isEmpty();
    }

    public boolean hasMIDIInterface() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors = getInterfaceDescriptorsForClass(1);
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : descriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbInterfaceDescriptor) {
                com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor = (com.android.server.usb.descriptors.UsbInterfaceDescriptor) descriptor;
                if (interfaceDescriptor.getUsbSubclass() == 3) {
                    return true;
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Class Interface l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        return false;
    }

    public boolean containsUniversalMidiDeviceEndpoint() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> interfaceDescriptors = findUniversalMidiInterfaceDescriptors();
        return doesInterfaceContainEndpoint(interfaceDescriptors);
    }

    public boolean containsLegacyMidiDeviceEndpoint() {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> interfaceDescriptors = findLegacyMidiInterfaceDescriptors();
        return doesInterfaceContainEndpoint(interfaceDescriptors);
    }

    public boolean doesInterfaceContainEndpoint(java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> interfaceDescriptors) {
        int outputCount = 0;
        int inputCount = 0;
        for (int interfaceIndex = 0; interfaceIndex < interfaceDescriptors.size(); interfaceIndex++) {
            com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor = interfaceDescriptors.get(interfaceIndex);
            for (int endpointIndex = 0; endpointIndex < interfaceDescriptor.getNumEndpoints(); endpointIndex++) {
                com.android.server.usb.descriptors.UsbEndpointDescriptor endpoint = interfaceDescriptor.getEndpointDescriptor(endpointIndex);
                if (endpoint.getDirection() == 0) {
                    outputCount++;
                } else {
                    inputCount++;
                }
            }
        }
        return outputCount > 0 || inputCount > 0;
    }

    public java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> findUniversalMidiInterfaceDescriptors() {
        return findMidiInterfaceDescriptors(512);
    }

    public java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> findLegacyMidiInterfaceDescriptors() {
        return findMidiInterfaceDescriptors(256);
    }

    private java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> findMidiInterfaceDescriptors(int type) {
        com.android.server.usb.descriptors.UsbDescriptor midiHeaderDescriptor;
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors = getInterfaceDescriptorsForClass(1);
        java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> midiInterfaces = new java.util.ArrayList<>();
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : descriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbInterfaceDescriptor) {
                com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor = (com.android.server.usb.descriptors.UsbInterfaceDescriptor) descriptor;
                if (interfaceDescriptor.getUsbSubclass() == 3 && (midiHeaderDescriptor = interfaceDescriptor.getMidiHeaderInterfaceDescriptor()) != null && (midiHeaderDescriptor instanceof com.android.server.usb.descriptors.UsbMSMidiHeader)) {
                    com.android.server.usb.descriptors.UsbMSMidiHeader midiHeader = (com.android.server.usb.descriptors.UsbMSMidiHeader) midiHeaderDescriptor;
                    if (midiHeader.getMidiStreamingClass() == type) {
                        midiInterfaces.add(interfaceDescriptor);
                    }
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Class Interface l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        return midiInterfaces;
    }

    public int calculateMidiInterfaceDescriptorsCount() {
        com.android.server.usb.descriptors.UsbDescriptor midiHeaderDescriptor;
        int count = 0;
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors = getInterfaceDescriptorsForClass(1);
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : descriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbInterfaceDescriptor) {
                com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor = (com.android.server.usb.descriptors.UsbInterfaceDescriptor) descriptor;
                if (interfaceDescriptor.getUsbSubclass() == 3 && (midiHeaderDescriptor = interfaceDescriptor.getMidiHeaderInterfaceDescriptor()) != null && (midiHeaderDescriptor instanceof com.android.server.usb.descriptors.UsbMSMidiHeader)) {
                    count++;
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Class Interface l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        return count;
    }

    private int calculateNumLegacyMidiPorts(boolean isOutput) {
        com.android.server.usb.descriptors.UsbDescriptor classSpecificEndpointDescriptor;
        com.android.server.usb.descriptors.UsbDescriptor midiHeaderDescriptor;
        com.android.server.usb.descriptors.UsbConfigDescriptor configDescriptor = null;
        java.util.Iterator<com.android.server.usb.descriptors.UsbDescriptor> it = this.mDescriptors.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.usb.descriptors.UsbDescriptor descriptor = it.next();
            if (descriptor.getType() == 2) {
                if (descriptor instanceof com.android.server.usb.descriptors.UsbConfigDescriptor) {
                    configDescriptor = (com.android.server.usb.descriptors.UsbConfigDescriptor) descriptor;
                    break;
                }
                android.util.Log.w(TAG, "Unrecognized Config l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        if (configDescriptor == null) {
            android.util.Log.w(TAG, "Config not found");
            return 0;
        }
        java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> legacyMidiInterfaceDescriptors = new java.util.ArrayList<>();
        for (com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor : configDescriptor.getInterfaceDescriptors()) {
            if (interfaceDescriptor.getUsbClass() == 1 && interfaceDescriptor.getUsbSubclass() == 3 && (midiHeaderDescriptor = interfaceDescriptor.getMidiHeaderInterfaceDescriptor()) != null && (midiHeaderDescriptor instanceof com.android.server.usb.descriptors.UsbMSMidiHeader)) {
                com.android.server.usb.descriptors.UsbMSMidiHeader midiHeader = (com.android.server.usb.descriptors.UsbMSMidiHeader) midiHeaderDescriptor;
                if (midiHeader.getMidiStreamingClass() == 256) {
                    legacyMidiInterfaceDescriptors.add(interfaceDescriptor);
                }
            }
        }
        int count = 0;
        for (com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor2 : legacyMidiInterfaceDescriptors) {
            for (int i = 0; i < interfaceDescriptor2.getNumEndpoints(); i++) {
                com.android.server.usb.descriptors.UsbEndpointDescriptor endpoint = interfaceDescriptor2.getEndpointDescriptor(i);
                if ((endpoint.getDirection() == 0) == isOutput && (classSpecificEndpointDescriptor = endpoint.getClassSpecificEndpointDescriptor()) != null && (classSpecificEndpointDescriptor instanceof com.android.server.usb.descriptors.UsbACMidi10Endpoint)) {
                    com.android.server.usb.descriptors.UsbACMidi10Endpoint midiEndpoint = (com.android.server.usb.descriptors.UsbACMidi10Endpoint) classSpecificEndpointDescriptor;
                    count += midiEndpoint.getNumJacks();
                }
            }
        }
        return count;
    }

    public int calculateNumLegacyMidiInputs() {
        return calculateNumLegacyMidiPorts(false);
    }

    public int calculateNumLegacyMidiOutputs() {
        return calculateNumLegacyMidiPorts(true);
    }

    public float getInputHeadsetProbability() {
        if (hasMIDIInterface()) {
            return 0.0f;
        }
        float probability = 0.0f;
        boolean hasMic = hasMic();
        boolean hasSpeaker = hasSpeaker();
        if (hasMic && hasSpeaker) {
            probability = 0.0f + 0.75f;
        }
        if (hasMic && hasHIDInterface()) {
            return probability + 0.25f;
        }
        return probability;
    }

    public boolean isInputHeadset() {
        return getInputHeadsetProbability() >= 0.75f;
    }

    private int getMaximumChannelCount() {
        int maxChannelCount = 0;
        for (com.android.server.usb.descriptors.report.Reporting reporting : this.mDescriptors) {
            if (reporting instanceof com.android.server.usb.descriptors.UsbAudioChannelCluster) {
                maxChannelCount = java.lang.Math.max(maxChannelCount, (int) ((com.android.server.usb.descriptors.UsbAudioChannelCluster) reporting).getChannelCount());
            }
        }
        return maxChannelCount;
    }

    public float getOutputHeadsetLikelihood() {
        if (hasMIDIInterface()) {
            return 0.0f;
        }
        float likelihood = 0.0f;
        boolean hasSpeaker = false;
        boolean hasAssociatedInputTerminal = false;
        boolean hasHeadphoneOrHeadset = false;
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> acDescriptors = getACInterfaceDescriptors((byte) 3, 1);
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : acDescriptors) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbACTerminal) {
                com.android.server.usb.descriptors.UsbACTerminal outDescr = (com.android.server.usb.descriptors.UsbACTerminal) descriptor;
                if (outDescr.getTerminalType() == 769) {
                    hasSpeaker = true;
                    if (outDescr.getAssocTerminal() != 0) {
                        hasAssociatedInputTerminal = true;
                    }
                } else if (outDescr.getTerminalType() == 770 || outDescr.getTerminalType() == 1026) {
                    hasHeadphoneOrHeadset = true;
                }
            } else {
                android.util.Log.w(TAG, "Undefined Audio Output terminal l: " + descriptor.getLength() + " t:0x" + java.lang.Integer.toHexString(descriptor.getType()));
            }
        }
        if (hasHeadphoneOrHeadset) {
            likelihood = 0.0f + 0.75f;
        } else if (hasSpeaker) {
            likelihood = 0.0f + 0.5f;
            if (hasAssociatedInputTerminal) {
                likelihood += 0.25f;
            }
            if (getMaximumChannelCount() > 2) {
                likelihood -= 0.25f;
            }
        }
        if ((hasHeadphoneOrHeadset || hasSpeaker) && hasHIDInterface()) {
            return likelihood + 0.25f;
        }
        return likelihood;
    }

    public boolean isOutputHeadset() {
        return getOutputHeadsetLikelihood() >= 0.75f;
    }

    public boolean isDock() {
        if (hasMIDIInterface() || hasHIDInterface()) {
            return false;
        }
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> acDescriptors = getACInterfaceDescriptors((byte) 3, 1);
        if (acDescriptors.size() != 1) {
            return false;
        }
        if (acDescriptors.get(0) instanceof com.android.server.usb.descriptors.UsbACTerminal) {
            com.android.server.usb.descriptors.UsbACTerminal outDescr = (com.android.server.usb.descriptors.UsbACTerminal) acDescriptors.get(0);
            if (outDescr.getTerminalType() == 1538) {
                return true;
            }
        } else {
            android.util.Log.w(TAG, "Undefined Audio Output terminal l: " + acDescriptors.get(0).getLength() + " t:0x" + java.lang.Integer.toHexString(acDescriptors.get(0).getType()));
        }
        return false;
    }
}
