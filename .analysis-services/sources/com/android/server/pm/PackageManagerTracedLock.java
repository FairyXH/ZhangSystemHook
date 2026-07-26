package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerTracedLock implements java.lang.AutoCloseable {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "PackageManagerTracedLock";
    private final com.android.server.pm.PackageManagerTracedLock.RawLock mLock;

    public PackageManagerTracedLock(java.lang.String lockName) {
        this.mLock = new com.android.server.pm.PackageManagerTracedLock.RawLock(lockName);
    }

    public PackageManagerTracedLock() {
        this(null);
    }

    public com.android.server.pm.PackageManagerTracedLock acquireLock() {
        this.mLock.lock();
        return this;
    }

    public com.android.server.pm.PackageManagerTracedLock.RawLock getRawLock() {
        return this.mLock;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mLock.unlock();
    }

    public static class RawLock extends java.util.concurrent.locks.ReentrantLock {
        private final java.lang.String mLockName;

        RawLock(java.lang.String lockName) {
            this.mLockName = lockName;
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void lock() {
            super.lock();
        }

        @Override // java.util.concurrent.locks.ReentrantLock, java.util.concurrent.locks.Lock
        public void unlock() {
            super.unlock();
        }
    }
}
