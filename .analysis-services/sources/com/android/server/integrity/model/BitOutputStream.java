package com.android.server.integrity.model;

/* JADX INFO: loaded from: classes2.dex */
public class BitOutputStream {
    private static final int BUFFER_SIZE = 4096;
    private final byte[] mBuffer = new byte[4096];
    private int mNextBitIndex = 0;
    private final java.io.OutputStream mOutputStream;

    public BitOutputStream(java.io.OutputStream outputStream) {
        this.mOutputStream = outputStream;
    }

    public void setNext(int numOfBits, int value) throws java.io.IOException {
        if (numOfBits <= 0) {
            return;
        }
        int nextBitMask = 1 << (numOfBits - 1);
        while (true) {
            int numOfBits2 = numOfBits - 1;
            if (numOfBits > 0) {
                setNext((value & nextBitMask) != 0);
                nextBitMask >>>= 1;
                numOfBits = numOfBits2;
            } else {
                return;
            }
        }
    }

    public void setNext(boolean value) throws java.io.IOException {
        int byteToWrite = this.mNextBitIndex / 8;
        if (byteToWrite == 4096) {
            this.mOutputStream.write(this.mBuffer);
            reset();
            byteToWrite = 0;
        }
        if (value) {
            byte[] bArr = this.mBuffer;
            bArr[byteToWrite] = (byte) (bArr[byteToWrite] | (1 << (7 - (this.mNextBitIndex % 8))));
        }
        this.mNextBitIndex++;
    }

    public void setNext() throws java.io.IOException {
        setNext(true);
    }

    public void flush() throws java.io.IOException {
        int endByte = this.mNextBitIndex / 8;
        if (this.mNextBitIndex % 8 != 0) {
            endByte++;
        }
        this.mOutputStream.write(this.mBuffer, 0, endByte);
        reset();
    }

    private void reset() {
        this.mNextBitIndex = 0;
        java.util.Arrays.fill(this.mBuffer, (byte) 0);
    }
}
