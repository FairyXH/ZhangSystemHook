package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ResilientAtomicFile implements java.io.Closeable {
    private static final java.lang.String LOG_TAG = "ResilientAtomicFile";
    private final java.lang.String mDebugName;
    private final java.io.File mFile;
    private final int mFileMode;
    private final com.android.server.pm.ResilientAtomicFile.ReadEventLogger mReadEventLogger;
    private final java.io.File mReserveCopy;
    private final java.io.File mTemporaryBackup;
    private java.io.FileOutputStream mMainOutStream = null;
    private java.io.FileInputStream mMainInStream = null;
    private java.io.FileOutputStream mReserveOutStream = null;
    private java.io.FileInputStream mReserveInStream = null;
    private java.io.File mCurrentFile = null;
    private java.io.FileInputStream mCurrentInStream = null;

    interface ReadEventLogger {
        void logEvent(int i, java.lang.String str);
    }

    private void finalizeOutStream(java.io.FileOutputStream str) throws java.io.IOException {
        str.flush();
        android.os.FileUtils.sync(str);
        android.os.FileUtils.setPermissions(str.getFD(), this.mFileMode, -1, -1);
    }

    ResilientAtomicFile(java.io.File file, java.io.File temporaryBackup, java.io.File reserveCopy, int fileMode, java.lang.String debugName, com.android.server.pm.ResilientAtomicFile.ReadEventLogger readEventLogger) {
        this.mFile = file;
        this.mTemporaryBackup = temporaryBackup;
        this.mReserveCopy = reserveCopy;
        this.mFileMode = fileMode;
        this.mDebugName = debugName;
        this.mReadEventLogger = readEventLogger;
    }

    public java.io.File getBaseFile() {
        return this.mFile;
    }

    public java.io.FileOutputStream startWrite() throws java.io.IOException {
        if (this.mMainOutStream != null) {
            throw new java.lang.IllegalStateException("Duplicate startWrite call?");
        }
        new java.io.File(this.mFile.getParent()).mkdirs();
        if (this.mFile.exists()) {
            if (!this.mTemporaryBackup.exists()) {
                if (!this.mFile.renameTo(this.mTemporaryBackup)) {
                    throw new java.io.IOException("Unable to backup " + this.mDebugName + " file, current changes will be lost at reboot");
                }
            } else {
                this.mFile.delete();
                android.util.Slog.w(LOG_TAG, "Preserving older " + this.mDebugName + " backup");
            }
        }
        this.mReserveCopy.delete();
        try {
            this.mMainOutStream = new java.io.FileOutputStream(this.mFile);
            this.mMainInStream = new java.io.FileInputStream(this.mFile);
            this.mReserveOutStream = new java.io.FileOutputStream(this.mReserveCopy);
            this.mReserveInStream = new java.io.FileInputStream(this.mReserveCopy);
            return this.mMainOutStream;
        } catch (java.io.IOException e) {
            close();
            throw e;
        }
    }

    public void finishWrite(java.io.FileOutputStream str) throws java.io.IOException {
        android.os.ParcelFileDescriptor mainPfd;
        if (this.mMainOutStream != str) {
            throw new java.lang.IllegalStateException("Invalid incoming stream.");
        }
        java.io.FileOutputStream mainOutStream = this.mMainOutStream;
        try {
            this.mMainOutStream = null;
            finalizeOutStream(mainOutStream);
            if (mainOutStream != null) {
                mainOutStream.close();
            }
            this.mTemporaryBackup.delete();
            try {
                java.io.FileInputStream mainInStream = this.mMainInStream;
                try {
                    java.io.FileInputStream reserveInStream = this.mReserveInStream;
                    try {
                        this.mMainInStream = null;
                        this.mReserveInStream = null;
                        java.io.FileOutputStream reserveOutStream = this.mReserveOutStream;
                        try {
                            this.mReserveOutStream = null;
                            android.os.FileUtils.copy(mainInStream, reserveOutStream);
                            finalizeOutStream(reserveOutStream);
                            if (reserveOutStream != null) {
                                reserveOutStream.close();
                            }
                            try {
                                mainPfd = android.os.ParcelFileDescriptor.dup(mainInStream.getFD());
                            } catch (java.io.IOException e) {
                                android.util.Slog.e(LOG_TAG, "Failed to verity-protect " + this.mDebugName, e);
                            }
                            try {
                                android.os.ParcelFileDescriptor copyPfd = android.os.ParcelFileDescriptor.dup(reserveInStream.getFD());
                                try {
                                    com.android.server.security.FileIntegrity.setUpFsVerity(mainPfd);
                                    com.android.server.security.FileIntegrity.setUpFsVerity(copyPfd);
                                    if (copyPfd != null) {
                                        copyPfd.close();
                                    }
                                    if (mainPfd != null) {
                                        mainPfd.close();
                                    }
                                    if (reserveInStream != null) {
                                        reserveInStream.close();
                                    }
                                    if (mainInStream != null) {
                                        mainInStream.close();
                                    }
                                } finally {
                                }
                            } catch (java.lang.Throwable th) {
                                if (mainPfd != null) {
                                    try {
                                        mainPfd.close();
                                    } catch (java.lang.Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                }
                                throw th;
                            }
                        } finally {
                        }
                    } finally {
                    }
                } finally {
                }
            } catch (java.io.IOException e2) {
                android.util.Slog.e(LOG_TAG, "Failed to write reserve copy " + this.mDebugName + ": " + this.mReserveCopy, e2);
            }
        } catch (java.lang.Throwable th3) {
            if (mainOutStream != null) {
                try {
                    mainOutStream.close();
                } catch (java.lang.Throwable th4) {
                    th3.addSuppressed(th4);
                }
            }
            throw th3;
        }
    }

    public void failWrite(java.io.FileOutputStream str) {
        if (this.mMainOutStream != str) {
            throw new java.lang.IllegalStateException("Invalid incoming stream.");
        }
        close();
        if (this.mFile.exists() && !this.mFile.delete()) {
            android.util.Slog.i(LOG_TAG, "Failed to clean up mangled file: " + this.mFile);
        }
    }

    public java.io.FileInputStream openRead() throws java.io.IOException {
        if (this.mTemporaryBackup.exists()) {
            try {
                this.mCurrentFile = this.mTemporaryBackup;
                this.mCurrentInStream = new java.io.FileInputStream(this.mCurrentFile);
                if (this.mReadEventLogger != null) {
                    this.mReadEventLogger.logEvent(4, "Need to read from backup " + this.mDebugName + " file");
                }
                if (this.mFile.exists()) {
                    android.util.Slog.w(LOG_TAG, "Cleaning up " + this.mDebugName + " file " + this.mFile);
                    this.mFile.delete();
                }
                this.mReserveCopy.delete();
            } catch (java.io.IOException e) {
            }
        }
        if (this.mCurrentInStream != null) {
            return this.mCurrentInStream;
        }
        if (this.mFile.exists()) {
            this.mCurrentFile = this.mFile;
            this.mCurrentInStream = new java.io.FileInputStream(this.mCurrentFile);
        } else if (this.mReserveCopy.exists()) {
            this.mCurrentFile = this.mReserveCopy;
            this.mCurrentInStream = new java.io.FileInputStream(this.mCurrentFile);
            if (this.mReadEventLogger != null) {
                this.mReadEventLogger.logEvent(4, "Need to read from reserve copy " + this.mDebugName + " file");
            }
        }
        if (this.mCurrentInStream == null && this.mReadEventLogger != null) {
            this.mReadEventLogger.logEvent(4, "No " + this.mDebugName + " file");
        }
        return this.mCurrentInStream;
    }

    public void failRead(java.io.FileInputStream str, java.lang.Exception e) {
        if (this.mCurrentInStream != str) {
            throw new java.lang.IllegalStateException("Invalid incoming stream.");
        }
        this.mCurrentInStream = null;
        libcore.io.IoUtils.closeQuietly(str);
        if (this.mReadEventLogger != null) {
            this.mReadEventLogger.logEvent(6, "Error reading " + this.mDebugName + ", removing " + this.mCurrentFile + '\n' + android.util.Log.getStackTraceString(e));
        }
        if (!this.mCurrentFile.delete()) {
            throw new java.lang.IllegalStateException("Failed to remove " + this.mCurrentFile);
        }
        this.mCurrentFile = null;
    }

    public void delete() {
        this.mFile.delete();
        this.mTemporaryBackup.delete();
        this.mReserveCopy.delete();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        libcore.io.IoUtils.closeQuietly(this.mMainOutStream);
        libcore.io.IoUtils.closeQuietly(this.mMainInStream);
        libcore.io.IoUtils.closeQuietly(this.mReserveOutStream);
        libcore.io.IoUtils.closeQuietly(this.mReserveInStream);
        libcore.io.IoUtils.closeQuietly(this.mCurrentInStream);
        this.mMainOutStream = null;
        this.mMainInStream = null;
        this.mReserveOutStream = null;
        this.mReserveInStream = null;
        this.mCurrentInStream = null;
        this.mCurrentFile = null;
    }

    public java.lang.String toString() {
        return this.mFile.getPath();
    }
}
