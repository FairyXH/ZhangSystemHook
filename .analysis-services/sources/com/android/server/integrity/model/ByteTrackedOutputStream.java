package com.android.server.integrity.model;

/* JADX INFO: loaded from: classes2.dex */
public class ByteTrackedOutputStream extends java.io.OutputStream {
    private final java.io.OutputStream mOutputStream;
    private int mWrittenBytesCount = 0;

    public ByteTrackedOutputStream(java.io.OutputStream outputStream) {
        this.mOutputStream = outputStream;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws java.io.IOException {
        this.mWrittenBytesCount++;
        this.mOutputStream.write(b);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bytes) throws java.io.IOException {
        write(bytes, 0, bytes.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws java.io.IOException {
        this.mWrittenBytesCount += len;
        this.mOutputStream.write(b, off, len);
    }

    public int getWrittenBytesCount() {
        return this.mWrittenBytesCount;
    }
}
