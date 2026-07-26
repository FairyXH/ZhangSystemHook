package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public class UsbMidiBlockParser {
    public static final int CS_GR_TRM_BLOCK = 38;
    public static final int DEFAULT_MIDI_TYPE = 1;
    public static final int GR_TRM_BLOCK_HEADER = 1;
    public static final int MIDI_BLOCK_HEADER_SIZE = 5;
    public static final int MIDI_BLOCK_SIZE = 13;
    public static final int REQ_GET_DESCRIPTOR = 6;
    public static final int REQ_TIMEOUT_MS = 2000;
    private static final java.lang.String TAG = "UsbMidiBlockParser";
    private java.util.ArrayList<com.android.server.usb.descriptors.UsbMidiBlockParser.GroupTerminalBlock> mGroupTerminalBlocks = new java.util.ArrayList<>();
    protected int mHeaderDescriptorSubtype;
    protected int mHeaderDescriptorType;
    protected int mHeaderLength;
    protected int mTotalLength;

    static class GroupTerminalBlock {
        protected int mBlockItem;
        protected int mDescriptorSubtype;
        protected int mDescriptorType;
        protected int mGroupBlockId;
        protected int mGroupTerminal;
        protected int mGroupTerminalBlockType;
        protected int mLength;
        protected int mMaxInputBandwidth;
        protected int mMaxOutputBandwidth;
        protected int mMidiProtocol;
        protected int mNumGroupTerminals;

        GroupTerminalBlock() {
        }

        public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
            this.mLength = stream.getUnsignedByte();
            this.mDescriptorType = stream.getUnsignedByte();
            this.mDescriptorSubtype = stream.getUnsignedByte();
            this.mGroupBlockId = stream.getUnsignedByte();
            this.mGroupTerminalBlockType = stream.getUnsignedByte();
            this.mGroupTerminal = stream.getUnsignedByte();
            this.mNumGroupTerminals = stream.getUnsignedByte();
            this.mBlockItem = stream.getUnsignedByte();
            this.mMidiProtocol = stream.getUnsignedByte();
            this.mMaxInputBandwidth = stream.unpackUsbShort();
            this.mMaxOutputBandwidth = stream.unpackUsbShort();
            return this.mLength;
        }

        public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
            long token = dump.start(idName, id);
            dump.write("length", 1120986464257L, this.mLength);
            dump.write("descriptor_type", 1120986464258L, this.mDescriptorType);
            dump.write("descriptor_subtype", 1120986464259L, this.mDescriptorSubtype);
            dump.write("group_block_id", 1120986464260L, this.mGroupBlockId);
            dump.write("group_terminal_block_type", 1120986464261L, this.mGroupTerminalBlockType);
            dump.write("group_terminal", 1120986464262L, this.mGroupTerminal);
            dump.write("num_group_terminals", 1120986464263L, this.mNumGroupTerminals);
            dump.write("block_item", 1120986464264L, this.mBlockItem);
            dump.write("midi_protocol", 1120986464265L, this.mMidiProtocol);
            dump.write("max_input_bandwidth", 1120986464266L, this.mMaxInputBandwidth);
            dump.write("max_output_bandwidth", 1120986464267L, this.mMaxOutputBandwidth);
            dump.end(token);
        }
    }

    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mHeaderLength = stream.getUnsignedByte();
        this.mHeaderDescriptorType = stream.getUnsignedByte();
        this.mHeaderDescriptorSubtype = stream.getUnsignedByte();
        this.mTotalLength = stream.unpackUsbShort();
        while (stream.available() >= 13) {
            com.android.server.usb.descriptors.UsbMidiBlockParser.GroupTerminalBlock block = new com.android.server.usb.descriptors.UsbMidiBlockParser.GroupTerminalBlock();
            block.parseRawDescriptors(stream);
            this.mGroupTerminalBlocks.add(block);
        }
        return this.mTotalLength;
    }

    public int calculateMidiType(android.hardware.usb.UsbDeviceConnection connection, int interfaceNumber, int alternateInterfaceNumber) {
        byte[] byteArray = new byte[5];
        try {
            int rdo = connection.controlTransfer(129, 6, alternateInterfaceNumber + 9728, interfaceNumber, byteArray, 5, 2000);
            if (rdo > 0) {
                if (byteArray[1] != 38) {
                    android.util.Log.e(TAG, "Incorrect descriptor type: " + ((int) byteArray[1]));
                    return 1;
                }
                if (byteArray[2] != 1) {
                    android.util.Log.e(TAG, "Incorrect descriptor subtype: " + ((int) byteArray[2]));
                    return 1;
                }
                int newSize = (byteArray[3] & 255) + ((byteArray[4] & 255) << 8);
                if (newSize <= 0) {
                    android.util.Log.e(TAG, "Parsed a non-positive block terminal size: " + newSize);
                    return 1;
                }
                byte[] byteArray2 = new byte[newSize];
                int rdo2 = connection.controlTransfer(129, 6, alternateInterfaceNumber + 9728, interfaceNumber, byteArray2, newSize, 2000);
                if (rdo2 > 0) {
                    com.android.server.usb.descriptors.ByteStream stream = new com.android.server.usb.descriptors.ByteStream(byteArray2);
                    parseRawDescriptors(stream);
                    if (!this.mGroupTerminalBlocks.isEmpty()) {
                        android.util.Log.d(TAG, "MIDI protocol: " + this.mGroupTerminalBlocks.get(0).mMidiProtocol);
                        return this.mGroupTerminalBlocks.get(0).mMidiProtocol;
                    }
                    android.util.Log.e(TAG, "Group Terminal Blocks failed parsing: 1");
                    return 1;
                }
                android.util.Log.e(TAG, "second transfer failed: " + rdo2);
            } else {
                android.util.Log.e(TAG, "first transfer failed: " + rdo);
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Can not communicate with USB device", e);
        }
        return 1;
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("length", 1120986464257L, this.mHeaderLength);
        dump.write("descriptor_type", 1120986464258L, this.mHeaderDescriptorType);
        dump.write("descriptor_subtype", 1120986464259L, this.mHeaderDescriptorSubtype);
        dump.write("total_length", 1120986464260L, this.mTotalLength);
        for (com.android.server.usb.descriptors.UsbMidiBlockParser.GroupTerminalBlock groupTerminalBlock : this.mGroupTerminalBlocks) {
            groupTerminalBlock.dump(dump, "block", 2246267895813L);
        }
        dump.end(token);
    }
}
