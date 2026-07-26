package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public class LimitInputStream extends java.io.FilterInputStream {
    private final int mLimit;
    private int mReadBytes;

    public LimitInputStream(java.io.InputStream in, int limit) {
        super(in);
        if (limit < 0) {
            throw new java.lang.IllegalArgumentException("limit " + limit + " cannot be negative");
        }
        this.mReadBytes = 0;
        this.mLimit = limit;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws java.io.IOException {
        return java.lang.Math.min(super.available(), this.mLimit - this.mReadBytes);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws java.io.IOException {
        if (this.mReadBytes == this.mLimit) {
            return -1;
        }
        this.mReadBytes++;
        return super.read();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b) throws java.io.IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws java.io.IOException {
        if (len <= 0) {
            return 0;
        }
        int available = available();
        if (available <= 0) {
            return -1;
        }
        int result = super.read(b, off, java.lang.Math.min(len, available));
        this.mReadBytes += result;
        return result;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long n) throws java.io.IOException {
        int available;
        if (n <= 0 || (available = available()) <= 0) {
            return 0L;
        }
        int bytesToSkip = (int) java.lang.Math.min(available, n);
        long bytesSkipped = super.skip(bytesToSkip);
        this.mReadBytes += (int) bytesSkipped;
        return bytesSkipped;
    }
}
