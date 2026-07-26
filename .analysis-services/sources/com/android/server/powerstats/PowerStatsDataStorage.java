package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsDataStorage {
    private static final long DELETE_AGE_MILLIS = 172800000;
    private static final long MILLISECONDS_PER_HOUR = 3600000;
    private static final long ROTATE_AGE_MILLIS = 14400000;
    private static final java.lang.String TAG = com.android.server.powerstats.PowerStatsDataStorage.class.getSimpleName();
    private final java.io.File mDataStorageDir;
    private final java.lang.String mDataStorageFilename;
    private final com.android.internal.util.FileRotator mFileRotator;
    private final java.util.concurrent.locks.ReentrantLock mLock = new java.util.concurrent.locks.ReentrantLock();

    public interface DataElementReadCallback {
        void onReadDataElement(byte[] bArr);
    }

    private static class DataElement {
        private static final int LENGTH_FIELD_WIDTH = 4;
        private static final int MAX_DATA_ELEMENT_SIZE = 32768;
        private byte[] mData;

        /* JADX INFO: Access modifiers changed from: private */
        public byte[] toByteArray() throws java.io.IOException {
            java.io.ByteArrayOutputStream data = new java.io.ByteArrayOutputStream();
            data.write(java.nio.ByteBuffer.allocate(4).putInt(this.mData.length).array());
            data.write(this.mData);
            return data.toByteArray();
        }

        protected byte[] getData() {
            return this.mData;
        }

        private DataElement(byte[] data) {
            this.mData = data;
        }

        private DataElement(java.io.InputStream in) throws java.io.IOException {
            byte[] lengthBytes = new byte[4];
            int bytesRead = in.read(lengthBytes);
            this.mData = new byte[0];
            if (bytesRead == 4) {
                int length = java.nio.ByteBuffer.wrap(lengthBytes).getInt();
                if (length > 0 && length < 32768) {
                    this.mData = new byte[length];
                    int bytesRead2 = in.read(this.mData);
                    if (bytesRead2 != length) {
                        throw new java.io.IOException("Invalid bytes read, expected: " + length + ", actual: " + bytesRead2);
                    }
                    return;
                }
                throw new java.io.IOException("DataElement size is invalid: " + length);
            }
            throw new java.io.IOException("Did not read 4 bytes (" + bytesRead + ")");
        }
    }

    private static class DataReader implements com.android.internal.util.FileRotator.Reader {
        private com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback mCallback;

        DataReader(com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback callback) {
            this.mCallback = callback;
        }

        public void read(java.io.InputStream in) throws java.io.IOException {
            while (in.available() > 0) {
                com.android.server.powerstats.PowerStatsDataStorage.DataElement dataElement = new com.android.server.powerstats.PowerStatsDataStorage.DataElement(in);
                this.mCallback.onReadDataElement(dataElement.getData());
            }
        }
    }

    private static class DataRewriter implements com.android.internal.util.FileRotator.Rewriter {
        byte[] mActiveFileData = new byte[0];
        byte[] mNewData;

        DataRewriter(byte[] data) {
            this.mNewData = data;
        }

        public void reset() {
        }

        public void read(java.io.InputStream in) throws java.io.IOException {
            this.mActiveFileData = new byte[in.available()];
            in.read(this.mActiveFileData);
        }

        public boolean shouldWrite() {
            return true;
        }

        public void write(java.io.OutputStream out) throws java.io.IOException {
            out.write(this.mActiveFileData);
            out.write(this.mNewData);
        }
    }

    public PowerStatsDataStorage(android.content.Context context, java.io.File dataStoragePath, java.lang.String dataStorageFilename) {
        this.mDataStorageDir = dataStoragePath;
        this.mDataStorageFilename = dataStorageFilename;
        if (!this.mDataStorageDir.exists() && !this.mDataStorageDir.mkdirs()) {
            android.util.Slog.wtf(TAG, "mDataStorageDir does not exist: " + this.mDataStorageDir.getPath());
            this.mFileRotator = null;
            return;
        }
        java.io.File[] files = this.mDataStorageDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            int versionDot = this.mDataStorageFilename.lastIndexOf(46);
            java.lang.String beforeVersionDot = this.mDataStorageFilename.substring(0, versionDot);
            if (files[i].getName().startsWith(beforeVersionDot) && !files[i].getName().startsWith(this.mDataStorageFilename)) {
                files[i].delete();
            }
        }
        this.mFileRotator = new com.android.internal.util.FileRotator(this.mDataStorageDir, this.mDataStorageFilename, 14400000L, DELETE_AGE_MILLIS);
    }

    public void write(byte[] data) {
        if (data != null && data.length > 0) {
            this.mLock.lock();
            try {
                try {
                    long currentTimeMillis = java.lang.System.currentTimeMillis();
                    com.android.server.powerstats.PowerStatsDataStorage.DataElement dataElement = new com.android.server.powerstats.PowerStatsDataStorage.DataElement(data);
                    this.mFileRotator.rewriteActive(new com.android.server.powerstats.PowerStatsDataStorage.DataRewriter(dataElement.toByteArray()), currentTimeMillis);
                    this.mFileRotator.maybeRotate(currentTimeMillis);
                } catch (java.io.IOException e) {
                    android.util.Slog.e(TAG, "Failed to write to on-device storage: " + e);
                }
            } finally {
                this.mLock.unlock();
            }
        }
    }

    public void read(com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback callback) throws java.io.IOException {
        this.mLock.lock();
        try {
            this.mFileRotator.readMatching(new com.android.server.powerstats.PowerStatsDataStorage.DataReader(callback), Long.MIN_VALUE, Long.MAX_VALUE);
        } finally {
            this.mLock.unlock();
        }
    }

    public void deleteLogs() {
        this.mLock.lock();
        try {
            java.io.File[] files = this.mDataStorageDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                int versionDot = this.mDataStorageFilename.lastIndexOf(46);
                java.lang.String beforeVersionDot = this.mDataStorageFilename.substring(0, versionDot);
                if (files[i].getName().startsWith(beforeVersionDot)) {
                    files[i].delete();
                }
            }
        } finally {
            this.mLock.unlock();
        }
    }

    public void dump(android.util.IndentingPrintWriter ipw) {
        int versionDot;
        java.lang.String beforeVersionDot;
        this.mLock.lock();
        try {
            int i = 46;
            int versionDot2 = this.mDataStorageFilename.lastIndexOf(46);
            java.lang.String beforeVersionDot2 = this.mDataStorageFilename.substring(0, versionDot2);
            java.io.File[] files = this.mDataStorageDir.listFiles();
            int number = 0;
            int dataSize = 0;
            long earliestLogEpochTime = Long.MAX_VALUE;
            int i2 = 0;
            while (i2 < files.length) {
                java.io.File file = files[i2];
                java.lang.String fileName = file.getName();
                if (!files[i2].getName().startsWith(beforeVersionDot2)) {
                    versionDot = versionDot2;
                    beforeVersionDot = beforeVersionDot2;
                } else {
                    int number2 = number + 1;
                    dataSize = (int) (((long) dataSize) + file.length());
                    int firstTimeChar = fileName.lastIndexOf(i) + 1;
                    int endChar = fileName.lastIndexOf(45);
                    try {
                        java.lang.Long startTime = java.lang.Long.valueOf(java.lang.Long.parseLong(fileName.substring(firstTimeChar, endChar)));
                        if (startTime != null && startTime.longValue() < earliestLogEpochTime) {
                            earliestLogEpochTime = startTime.longValue();
                        }
                        versionDot = versionDot2;
                        beforeVersionDot = beforeVersionDot2;
                        number = number2;
                    } catch (java.lang.NumberFormatException nfe) {
                        versionDot = versionDot2;
                        beforeVersionDot = beforeVersionDot2;
                        android.util.Slog.e(TAG, "Failed to extract start time from file : " + fileName, nfe);
                        number = number2;
                    }
                }
                i2++;
                versionDot2 = versionDot;
                beforeVersionDot2 = beforeVersionDot;
                i = 46;
            }
            if (earliestLogEpochTime != Long.MAX_VALUE) {
                ipw.println("Earliest data time : " + new java.util.Date(earliestLogEpochTime));
            } else {
                ipw.println("Failed to parse earliest data time!!!");
            }
            ipw.println("# files : " + number);
            ipw.println("Total data size (B) : " + dataSize);
        } finally {
            this.mLock.unlock();
        }
    }
}
