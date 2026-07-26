package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class ByteStream {
    private static final java.lang.String TAG = "ByteStream";
    private final byte[] mBytes;
    private int mIndex;
    private int mReadCount;

    public ByteStream(byte[] bytes) {
        if (bytes == null) {
            throw new java.lang.IllegalArgumentException();
        }
        this.mBytes = bytes;
    }

    public void resetReadCount() {
        this.mReadCount = 0;
    }

    public int getReadCount() {
        return this.mReadCount;
    }

    public byte peekByte() {
        if (available() > 0) {
            return this.mBytes[this.mIndex + 1];
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    public byte getByte() {
        if (available() > 0) {
            this.mReadCount++;
            byte[] bArr = this.mBytes;
            int i = this.mIndex;
            this.mIndex = i + 1;
            return bArr[i];
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    public int getUnsignedByte() {
        if (available() > 0) {
            this.mReadCount++;
            byte[] bArr = this.mBytes;
            int i = this.mIndex;
            this.mIndex = i + 1;
            return bArr[i] & 255;
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    public int unpackUsbShort() {
        if (available() >= 2) {
            int b0 = getUnsignedByte();
            int b1 = getUnsignedByte();
            return (b1 << 8) | b0;
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    public int unpackUsbTriple() {
        if (available() >= 3) {
            int b0 = getUnsignedByte();
            int b1 = getUnsignedByte();
            int b2 = getUnsignedByte();
            return (b2 << 16) | (b1 << 8) | b0;
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    public int unpackUsbInt() {
        if (available() >= 4) {
            int b0 = getUnsignedByte();
            int b1 = getUnsignedByte();
            int b2 = getUnsignedByte();
            int b3 = getUnsignedByte();
            return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    public void advance(int numBytes) {
        if (numBytes < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        long longNewIndex = ((long) this.mIndex) + ((long) numBytes);
        if (longNewIndex <= this.mBytes.length) {
            this.mReadCount += numBytes;
            this.mIndex += numBytes;
        } else {
            this.mIndex = this.mBytes.length;
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    public void reverse(int numBytes) {
        if (numBytes < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        if (this.mIndex >= numBytes) {
            this.mReadCount -= numBytes;
            this.mIndex -= numBytes;
        } else {
            this.mIndex = 0;
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    public int available() {
        return this.mBytes.length - this.mIndex;
    }
}
