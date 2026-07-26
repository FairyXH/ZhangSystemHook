package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public abstract class RandomAccessObject {
    public abstract void close() throws java.io.IOException;

    public abstract int length();

    public abstract int read() throws java.io.IOException;

    public abstract int read(byte[] bArr, int i, int i2) throws java.io.IOException;

    public abstract void seek(int i) throws java.io.IOException;

    public static com.android.server.integrity.parser.RandomAccessObject ofFile(java.io.File file) throws java.io.IOException {
        return new com.android.server.integrity.parser.RandomAccessObject.RandomAccessFileObject(file);
    }

    public static com.android.server.integrity.parser.RandomAccessObject ofBytes(byte[] bytes) {
        return new com.android.server.integrity.parser.RandomAccessObject.RandomAccessByteArrayObject(bytes);
    }

    private static class RandomAccessFileObject extends com.android.server.integrity.parser.RandomAccessObject {
        private final int mLength;
        private final java.io.RandomAccessFile mRandomAccessFile;

        RandomAccessFileObject(java.io.File file) throws java.io.IOException {
            long length = file.length();
            if (length > 2147483647L) {
                throw new java.io.IOException("Unsupported file size (too big) " + length);
            }
            this.mRandomAccessFile = new java.io.RandomAccessFile(file, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
            this.mLength = (int) length;
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void seek(int position) throws java.io.IOException {
            this.mRandomAccessFile.seek(position);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read() throws java.io.IOException {
            return this.mRandomAccessFile.read();
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read(byte[] bytes, int off, int len) throws java.io.IOException {
            return this.mRandomAccessFile.read(bytes, off, len);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void close() throws java.io.IOException {
            this.mRandomAccessFile.close();
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int length() {
            return this.mLength;
        }
    }

    private static class RandomAccessByteArrayObject extends com.android.server.integrity.parser.RandomAccessObject {
        private final java.nio.ByteBuffer mBytes;

        RandomAccessByteArrayObject(byte[] bytes) {
            this.mBytes = java.nio.ByteBuffer.wrap(bytes);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void seek(int position) throws java.io.IOException {
            this.mBytes.position(position);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read() throws java.io.IOException {
            if (!this.mBytes.hasRemaining()) {
                return -1;
            }
            return this.mBytes.get() & 255;
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read(byte[] bytes, int off, int len) throws java.io.IOException {
            int bytesToCopy = java.lang.Math.min(len, this.mBytes.remaining());
            if (bytesToCopy <= 0) {
                return 0;
            }
            this.mBytes.get(bytes, off, len);
            return bytesToCopy;
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void close() throws java.io.IOException {
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int length() {
            return this.mBytes.capacity();
        }
    }
}
