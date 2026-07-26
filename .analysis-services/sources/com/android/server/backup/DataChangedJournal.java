package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class DataChangedJournal {
    private static final int BUFFER_SIZE_BYTES = 8192;
    private static final java.lang.String FILE_NAME_PREFIX = "journal";
    private static final java.lang.String TAG = "DataChangedJournal";
    private final java.io.File mFile;

    DataChangedJournal(java.io.File file) {
        this.mFile = (java.io.File) java.util.Objects.requireNonNull(file);
    }

    public void addPackage(java.lang.String packageName) throws java.io.IOException {
        java.io.RandomAccessFile out = new java.io.RandomAccessFile(this.mFile, "rws");
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

    public void forEach(java.util.function.Consumer<java.lang.String> consumer) throws java.io.IOException {
        try {
            java.io.InputStream in = new java.io.FileInputStream(this.mFile);
            try {
                java.io.InputStream bufferedIn = new java.io.BufferedInputStream(in, 8192);
                try {
                    java.io.DataInputStream dataInputStream = new java.io.DataInputStream(bufferedIn);
                    while (true) {
                        try {
                            java.lang.String packageName = dataInputStream.readUTF();
                            consumer.accept(packageName);
                        } finally {
                        }
                    }
                } finally {
                }
            } finally {
            }
        } catch (java.io.EOFException e) {
        }
    }

    public java.util.List<java.lang.String> getPackages() throws java.io.IOException {
        final java.util.List<java.lang.String> packages = new java.util.ArrayList<>();
        java.util.Objects.requireNonNull(packages);
        forEach(new java.util.function.Consumer() { // from class: com.android.server.backup.DataChangedJournal$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                packages.add((java.lang.String) obj);
            }
        });
        return packages;
    }

    public boolean delete() {
        return this.mFile.delete();
    }

    public int hashCode() {
        return this.mFile.hashCode();
    }

    public boolean equals(java.lang.Object object) {
        if (object instanceof com.android.server.backup.DataChangedJournal) {
            com.android.server.backup.DataChangedJournal that = (com.android.server.backup.DataChangedJournal) object;
            return this.mFile.equals(that.mFile);
        }
        return false;
    }

    public java.lang.String toString() {
        return this.mFile.toString();
    }

    static com.android.server.backup.DataChangedJournal newJournal(java.io.File journalDirectory) throws java.io.IOException {
        java.util.Objects.requireNonNull(journalDirectory);
        java.io.File file = java.io.File.createTempFile(FILE_NAME_PREFIX, null, journalDirectory);
        return new com.android.server.backup.DataChangedJournal(file);
    }

    static java.util.ArrayList<com.android.server.backup.DataChangedJournal> listJournals(java.io.File journalDirectory) {
        java.util.ArrayList<com.android.server.backup.DataChangedJournal> journals = new java.util.ArrayList<>();
        java.io.File[] journalFiles = journalDirectory.listFiles();
        if (journalFiles == null) {
            android.util.Slog.w(TAG, "Failed to read journal files");
            return journals;
        }
        for (java.io.File file : journalFiles) {
            journals.add(new com.android.server.backup.DataChangedJournal(file));
        }
        return journals;
    }
}
