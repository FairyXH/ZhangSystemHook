package com.android.server.integrity.model;

/* JADX INFO: loaded from: classes2.dex */
public class BitInputStream {
    private long mBitsRead;
    private byte mCurrentByte;
    private java.io.InputStream mInputStream;

    public BitInputStream(java.io.InputStream inputStream) {
        this.mInputStream = inputStream;
    }

    public int getNext(int numOfBits) throws java.io.IOException {
        int component = 0;
        int offset = 0;
        while (true) {
            int count = offset + 1;
            if (offset < numOfBits) {
                if (this.mBitsRead % 8 == 0) {
                    this.mCurrentByte = getNextByte();
                }
                int offset2 = 7 - ((int) (this.mBitsRead % 8));
                component = (component << 1) | ((this.mCurrentByte >>> offset2) & 1);
                this.mBitsRead++;
                offset = count;
            } else {
                return component;
            }
        }
    }

    public boolean hasNext() throws java.io.IOException {
        return this.mInputStream.available() > 0;
    }

    private byte getNextByte() throws java.io.IOException {
        return (byte) this.mInputStream.read();
    }
}
