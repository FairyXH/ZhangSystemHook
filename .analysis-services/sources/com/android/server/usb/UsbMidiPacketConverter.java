package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbMidiPacketConverter {
    private static final byte CODE_INDEX_NUMBER_SINGLE_BYTE = 15;
    private static final byte CODE_INDEX_NUMBER_SYSEX_END_SINGLE_BYTE = 5;
    private static final byte CODE_INDEX_NUMBER_SYSEX_STARTS_OR_CONTINUES = 4;
    private static final byte FIRST_SYSTEM_MESSAGE_VALUE = -16;
    private static final byte SYSEX_END_EXCLUSIVE = -9;
    private static final byte SYSEX_START_EXCLUSIVE = -16;
    private static final java.lang.String TAG = "UsbMidiPacketConverter";
    private java.io.ByteArrayOutputStream mEncoderOutputStream = new java.io.ByteArrayOutputStream();
    private com.android.server.usb.UsbMidiPacketConverter.UsbMidiDecoder mUsbMidiDecoder;
    private com.android.server.usb.UsbMidiPacketConverter.UsbMidiEncoder[] mUsbMidiEncoders;
    private static final int[] PAYLOAD_SIZE = {-1, -1, 2, 3, 3, 1, 2, 3, 3, 3, 3, 3, 2, 2, 3, 1};
    private static final int[] CODE_INDEX_NUMBER_FROM_SYSTEM_TYPE = {-1, 2, 3, 2, -1, -1, 5, -1, 5, -1, 5, 5, 5, -1, 5, 5};

    public void createEncoders(int size) {
        this.mUsbMidiEncoders = new com.android.server.usb.UsbMidiPacketConverter.UsbMidiEncoder[size];
        for (int i = 0; i < size; i++) {
            this.mUsbMidiEncoders[i] = new com.android.server.usb.UsbMidiPacketConverter.UsbMidiEncoder(i);
        }
    }

    public void encodeMidiPackets(byte[] midiBytes, int size, int encoderId) {
        if (encoderId >= this.mUsbMidiEncoders.length) {
            android.util.Log.w(TAG, "encoderId " + encoderId + " invalid");
            encoderId = 0;
        }
        byte[] encodedPacket = this.mUsbMidiEncoders[encoderId].encode(midiBytes, size);
        this.mEncoderOutputStream.write(encodedPacket, 0, encodedPacket.length);
    }

    public byte[] pullEncodedMidiPackets() {
        byte[] output = this.mEncoderOutputStream.toByteArray();
        this.mEncoderOutputStream.reset();
        return output;
    }

    public void createDecoders(int size) {
        this.mUsbMidiDecoder = new com.android.server.usb.UsbMidiPacketConverter.UsbMidiDecoder(size);
    }

    public void decodeMidiPackets(byte[] usbMidiBytes, int size) {
        this.mUsbMidiDecoder.decode(usbMidiBytes, size);
    }

    public byte[] pullDecodedMidiPackets(int cableNumber) {
        return this.mUsbMidiDecoder.pullBytes(cableNumber);
    }

    private class UsbMidiDecoder {
        java.io.ByteArrayOutputStream[] mDecodedByteArrays;
        int mNumJacks;

        UsbMidiDecoder(int numJacks) {
            this.mNumJacks = numJacks;
            this.mDecodedByteArrays = new java.io.ByteArrayOutputStream[numJacks];
            for (int i = 0; i < numJacks; i++) {
                this.mDecodedByteArrays[i] = new java.io.ByteArrayOutputStream();
            }
        }

        public void decode(byte[] usbMidiBytes, int size) {
            new java.io.ByteArrayOutputStream();
            if (size % 4 != 0) {
                android.util.Log.w(com.android.server.usb.UsbMidiPacketConverter.TAG, "size " + size + " not multiple of 4");
            }
            for (int i = 0; i + 3 < size; i += 4) {
                int cableNumber = (usbMidiBytes[i] >> 4) & 15;
                int codeIndex = usbMidiBytes[i] & 15;
                int numPayloadBytes = com.android.server.usb.UsbMidiPacketConverter.PAYLOAD_SIZE[codeIndex];
                if (numPayloadBytes >= 0) {
                    if (cableNumber >= this.mNumJacks) {
                        android.util.Log.w(com.android.server.usb.UsbMidiPacketConverter.TAG, "cableNumber " + cableNumber + " invalid");
                        cableNumber = 0;
                    }
                    this.mDecodedByteArrays[cableNumber].write(usbMidiBytes, i + 1, numPayloadBytes);
                }
            }
        }

        public byte[] pullBytes(int cableNumber) {
            if (cableNumber >= this.mNumJacks) {
                android.util.Log.w(com.android.server.usb.UsbMidiPacketConverter.TAG, "cableNumber " + cableNumber + " invalid");
                cableNumber = 0;
            }
            byte[] output = this.mDecodedByteArrays[cableNumber].toByteArray();
            this.mDecodedByteArrays[cableNumber].reset();
            return output;
        }
    }

    private class UsbMidiEncoder {
        private byte mShiftedCableNumber;
        private byte[] mStoredSystemExclusiveBytes = new byte[3];
        private int mNumStoredSystemExclusiveBytes = 0;
        private boolean mHasSystemExclusiveStarted = false;
        private byte[] mEmptyBytes = new byte[3];

        UsbMidiEncoder(int cableNumber) {
            this.mShiftedCableNumber = (byte) (cableNumber << 4);
        }

        public byte[] encode(byte[] midiBytes, int size) {
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            int curLocation = 0;
            while (curLocation < size) {
                if (midiBytes[curLocation] >= 0) {
                    if (this.mHasSystemExclusiveStarted) {
                        this.mStoredSystemExclusiveBytes[this.mNumStoredSystemExclusiveBytes] = midiBytes[curLocation];
                        this.mNumStoredSystemExclusiveBytes++;
                        if (this.mNumStoredSystemExclusiveBytes == 3) {
                            outputStream.write(this.mShiftedCableNumber | 4);
                            outputStream.write(this.mStoredSystemExclusiveBytes, 0, 3);
                            this.mNumStoredSystemExclusiveBytes = 0;
                        }
                    } else {
                        writeSingleByte(outputStream, midiBytes[curLocation]);
                    }
                    curLocation++;
                } else {
                    if (midiBytes[curLocation] != -9 && this.mHasSystemExclusiveStarted) {
                        for (int index = 0; index < this.mNumStoredSystemExclusiveBytes; index++) {
                            writeSingleByte(outputStream, this.mStoredSystemExclusiveBytes[index]);
                        }
                        this.mNumStoredSystemExclusiveBytes = 0;
                        this.mHasSystemExclusiveStarted = false;
                    }
                    int index2 = midiBytes[curLocation];
                    if (index2 < -16) {
                        byte codeIndexNumber = (byte) ((midiBytes[curLocation] >> 4) & 15);
                        int channelMessageSize = com.android.server.usb.UsbMidiPacketConverter.PAYLOAD_SIZE[codeIndexNumber];
                        if (curLocation + channelMessageSize <= size) {
                            outputStream.write(this.mShiftedCableNumber | codeIndexNumber);
                            outputStream.write(midiBytes, curLocation, channelMessageSize);
                            outputStream.write(this.mEmptyBytes, 0, 3 - channelMessageSize);
                            curLocation += channelMessageSize;
                        } else {
                            while (curLocation < size) {
                                writeSingleByte(outputStream, midiBytes[curLocation]);
                                curLocation++;
                            }
                        }
                    } else if (midiBytes[curLocation] == -16) {
                        this.mHasSystemExclusiveStarted = true;
                        this.mStoredSystemExclusiveBytes[0] = midiBytes[curLocation];
                        this.mNumStoredSystemExclusiveBytes = 1;
                        curLocation++;
                    } else if (midiBytes[curLocation] == -9) {
                        outputStream.write((this.mNumStoredSystemExclusiveBytes + 5) | this.mShiftedCableNumber);
                        this.mStoredSystemExclusiveBytes[this.mNumStoredSystemExclusiveBytes] = midiBytes[curLocation];
                        this.mNumStoredSystemExclusiveBytes++;
                        outputStream.write(this.mStoredSystemExclusiveBytes, 0, this.mNumStoredSystemExclusiveBytes);
                        outputStream.write(this.mEmptyBytes, 0, 3 - this.mNumStoredSystemExclusiveBytes);
                        this.mHasSystemExclusiveStarted = false;
                        this.mNumStoredSystemExclusiveBytes = 0;
                        curLocation++;
                    } else {
                        int systemType = midiBytes[curLocation] & 15;
                        int codeIndexNumber2 = com.android.server.usb.UsbMidiPacketConverter.CODE_INDEX_NUMBER_FROM_SYSTEM_TYPE[systemType];
                        if (codeIndexNumber2 < 0) {
                            writeSingleByte(outputStream, midiBytes[curLocation]);
                            curLocation++;
                        } else {
                            int systemMessageSize = com.android.server.usb.UsbMidiPacketConverter.PAYLOAD_SIZE[codeIndexNumber2];
                            if (curLocation + systemMessageSize <= size) {
                                outputStream.write(this.mShiftedCableNumber | codeIndexNumber2);
                                outputStream.write(midiBytes, curLocation, systemMessageSize);
                                outputStream.write(this.mEmptyBytes, 0, 3 - systemMessageSize);
                                curLocation += systemMessageSize;
                            } else {
                                while (curLocation < size) {
                                    writeSingleByte(outputStream, midiBytes[curLocation]);
                                    curLocation++;
                                }
                            }
                        }
                    }
                }
            }
            return outputStream.toByteArray();
        }

        private void writeSingleByte(java.io.ByteArrayOutputStream outputStream, byte byteToWrite) {
            outputStream.write(this.mShiftedCableNumber | 15);
            outputStream.write(byteToWrite);
            outputStream.write(0);
            outputStream.write(0);
        }
    }
}
