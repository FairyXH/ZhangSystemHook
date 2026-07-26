package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public final class DataStreamFileCodec<T> {
    private final com.android.server.backup.utils.DataStreamCodec<T> mCodec;
    private final java.io.File mFile;

    public DataStreamFileCodec(java.io.File file, com.android.server.backup.utils.DataStreamCodec<T> codec) {
        this.mFile = file;
        this.mCodec = codec;
    }

    public T deserialize() throws java.io.IOException {
        java.io.FileInputStream fileInputStream = new java.io.FileInputStream(this.mFile);
        try {
            java.io.DataInputStream dataInputStream = new java.io.DataInputStream(fileInputStream);
            try {
                T tDeserialize = this.mCodec.deserialize(dataInputStream);
                dataInputStream.close();
                fileInputStream.close();
                return tDeserialize;
            } finally {
            }
        } catch (java.lang.Throwable th) {
            try {
                fileInputStream.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void serialize(T t) throws java.io.IOException {
        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(this.mFile);
        try {
            java.io.BufferedOutputStream bufferedOutputStream = new java.io.BufferedOutputStream(fileOutputStream);
            try {
                java.io.DataOutputStream dataOutputStream = new java.io.DataOutputStream(bufferedOutputStream);
                try {
                    this.mCodec.serialize(t, dataOutputStream);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    bufferedOutputStream.close();
                    fileOutputStream.close();
                } finally {
                }
            } finally {
            }
        } catch (java.lang.Throwable th) {
            try {
                fileOutputStream.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
