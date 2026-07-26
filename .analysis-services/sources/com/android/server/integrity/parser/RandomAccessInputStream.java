package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public class RandomAccessInputStream extends java.io.InputStream {
    private int mPosition = 0;
    private final com.android.server.integrity.parser.RandomAccessObject mRandomAccessObject;

    public RandomAccessInputStream(com.android.server.integrity.parser.RandomAccessObject object) throws java.io.IOException {
        this.mRandomAccessObject = object;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public void seek(int position) throws java.io.IOException {
        this.mRandomAccessObject.seek(position);
        this.mPosition = position;
    }

    @Override // java.io.InputStream
    public int available() throws java.io.IOException {
        return this.mRandomAccessObject.length() - this.mPosition;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws java.io.IOException {
        this.mRandomAccessObject.close();
    }

    @Override // java.io.InputStream
    public int read() throws java.io.IOException {
        if (available() <= 0) {
            return -1;
        }
        this.mPosition++;
        return this.mRandomAccessObject.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws java.io.IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws java.io.IOException {
        if (len <= 0) {
            return 0;
        }
        int available = available();
        if (available <= 0) {
            return -1;
        }
        int result = this.mRandomAccessObject.read(b, off, java.lang.Math.min(len, available));
        this.mPosition += result;
        return result;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws java.io.IOException {
        int available;
        if (n <= 0 || (available = available()) <= 0) {
            return 0L;
        }
        int skipAmount = (int) java.lang.Math.min(available, n);
        this.mPosition += skipAmount;
        this.mRandomAccessObject.seek(this.mPosition);
        return skipAmount;
    }
}
