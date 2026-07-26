package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
final class ProcessedPackagesJournal {
    private static final boolean DEBUG = true;
    private static final java.lang.String JOURNAL_FILE_NAME = "processed";
    private static final java.lang.String TAG = "ProcessedPackagesJournal";
    private final java.util.Set<java.lang.String> mProcessedPackages = new java.util.HashSet();
    private final java.io.File mStateDirectory;

    ProcessedPackagesJournal(java.io.File stateDirectory) {
        this.mStateDirectory = stateDirectory;
    }

    void init() {
        synchronized (this.mProcessedPackages) {
            loadFromDisk();
        }
    }

    boolean hasBeenProcessed(java.lang.String packageName) {
        boolean zContains;
        synchronized (this.mProcessedPackages) {
            zContains = this.mProcessedPackages.contains(packageName);
        }
        return zContains;
    }

    void addPackage(java.lang.String packageName) {
        java.io.RandomAccessFile out;
        synchronized (this.mProcessedPackages) {
            if (this.mProcessedPackages.add(packageName)) {
                java.io.File journalFile = new java.io.File(this.mStateDirectory, JOURNAL_FILE_NAME);
                try {
                    out = new java.io.RandomAccessFile(journalFile, "rws");
                } catch (java.io.IOException e) {
                    android.util.Slog.e(TAG, "Can't log backup of " + packageName + " to " + journalFile);
                }
                try {
                    out.seek(out.length());
                    out.writeUTF(packageName);
                    out.close();
                } catch (java.lang.Throwable th) {
                    try {
                        out.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        }
    }

    java.util.Set<java.lang.String> getPackagesCopy() {
        java.util.HashSet hashSet;
        synchronized (this.mProcessedPackages) {
            hashSet = new java.util.HashSet(this.mProcessedPackages);
        }
        return hashSet;
    }

    void reset() {
        synchronized (this.mProcessedPackages) {
            this.mProcessedPackages.clear();
            java.io.File journalFile = new java.io.File(this.mStateDirectory, JOURNAL_FILE_NAME);
            journalFile.delete();
        }
    }

    private void loadFromDisk() {
        java.io.File journalFile = new java.io.File(this.mStateDirectory, JOURNAL_FILE_NAME);
        if (!journalFile.exists()) {
            return;
        }
        try {
            java.io.DataInputStream oldJournal = new java.io.DataInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(journalFile)));
            while (true) {
                try {
                    java.lang.String packageName = oldJournal.readUTF();
                    android.util.Slog.v(TAG, "   + " + packageName);
                    this.mProcessedPackages.add(packageName);
                } catch (java.lang.Throwable th) {
                    try {
                        oldJournal.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        } catch (java.io.EOFException e) {
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Error reading processed packages journal", e2);
        }
    }
}
