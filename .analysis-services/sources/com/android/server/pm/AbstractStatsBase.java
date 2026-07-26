package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractStatsBase<T> {
    private static final int WRITE_INTERVAL_MS;
    private final java.lang.String mBackgroundThreadName;
    private final java.lang.String mFileName;
    private final boolean mLock;
    private final java.lang.Object mFileLock = new java.lang.Object();
    private final java.util.concurrent.atomic.AtomicLong mLastTimeWritten = new java.util.concurrent.atomic.AtomicLong(0);
    private final java.util.concurrent.atomic.AtomicBoolean mBackgroundWriteRunning = new java.util.concurrent.atomic.AtomicBoolean(false);

    protected abstract void readInternal(T t);

    protected abstract void writeInternal(T t);

    static {
        WRITE_INTERVAL_MS = com.android.server.pm.PackageManagerService.DEBUG_DEXOPT ? 0 : android.net.util.DataStallUtils.DEFAULT_DATA_STALL_VALID_DNS_TIME_THRESHOLD_MS;
    }

    protected AbstractStatsBase(java.lang.String fileName, java.lang.String threadName, boolean lock) {
        this.mFileName = fileName;
        this.mBackgroundThreadName = threadName;
        this.mLock = lock;
    }

    protected android.util.AtomicFile getFile() {
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        java.io.File systemDir = new java.io.File(dataDir, "system");
        java.io.File fname = new java.io.File(systemDir, this.mFileName);
        return new android.util.AtomicFile(fname);
    }

    protected void writeNow(T data) {
        writeImpl(data);
        this.mLastTimeWritten.set(android.os.SystemClock.elapsedRealtime());
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.server.pm.AbstractStatsBase$1] */
    protected boolean maybeWriteAsync(final T data) {
        if ((android.os.SystemClock.elapsedRealtime() - this.mLastTimeWritten.get() < WRITE_INTERVAL_MS && !com.android.server.pm.PackageManagerService.DEBUG_DEXOPT) || !this.mBackgroundWriteRunning.compareAndSet(false, true)) {
            return false;
        }
        new java.lang.Thread(this.mBackgroundThreadName) { // from class: com.android.server.pm.AbstractStatsBase.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    com.android.server.pm.AbstractStatsBase.this.writeImpl(data);
                    com.android.server.pm.AbstractStatsBase.this.mLastTimeWritten.set(android.os.SystemClock.elapsedRealtime());
                } finally {
                    com.android.server.pm.AbstractStatsBase.this.mBackgroundWriteRunning.set(false);
                }
            }
        }.start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeImpl(T data) {
        if (this.mLock) {
            synchronized (data) {
                synchronized (this.mFileLock) {
                    writeInternal(data);
                }
            }
            return;
        }
        synchronized (this.mFileLock) {
            writeInternal(data);
        }
    }

    protected void read(T data) {
        if (this.mLock) {
            synchronized (data) {
                synchronized (this.mFileLock) {
                    readInternal(data);
                }
            }
        } else {
            synchronized (this.mFileLock) {
                readInternal(data);
            }
        }
        this.mLastTimeWritten.set(android.os.SystemClock.elapsedRealtime());
    }
}
