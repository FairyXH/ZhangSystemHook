package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public abstract class DropBoxManagerInternal {
    public abstract void addEntry(java.lang.String str, com.android.server.DropBoxManagerInternal.EntrySource entrySource, int i);

    public interface EntrySource extends java.io.Closeable {
        void writeTo(java.io.FileDescriptor fileDescriptor) throws java.io.IOException;

        default long length() {
            return 0L;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        default void close() throws java.io.IOException {
        }
    }
}
