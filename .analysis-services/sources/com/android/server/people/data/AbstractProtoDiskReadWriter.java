package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
abstract class AbstractProtoDiskReadWriter<T> {
    private static final long DEFAULT_DISK_WRITE_DELAY = 120000;
    private static final long SHUTDOWN_DISK_WRITE_TIMEOUT = 5000;
    private static final java.lang.String TAG = com.android.server.people.data.AbstractProtoDiskReadWriter.class.getSimpleName();
    private final java.io.File mRootDir;
    private final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService;
    private java.util.Map<java.lang.String, T> mScheduledFileDataMap = new android.util.ArrayMap();
    private java.util.concurrent.ScheduledFuture<?> mScheduledFuture;

    interface ProtoStreamReader<T> {
        T read(android.util.proto.ProtoInputStream protoInputStream);
    }

    interface ProtoStreamWriter<T> {
        void write(android.util.proto.ProtoOutputStream protoOutputStream, T t);
    }

    abstract com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader<T> protoStreamReader();

    abstract com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter<T> protoStreamWriter();

    AbstractProtoDiskReadWriter(java.io.File rootDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        this.mRootDir = rootDir;
        this.mScheduledExecutorService = scheduledExecutorService;
    }

    void delete(java.lang.String fileName) {
        synchronized (this) {
            this.mScheduledFileDataMap.remove(fileName);
        }
        java.io.File file = getFile(fileName);
        if (file.exists() && !file.delete()) {
            android.util.Slog.e(TAG, "Failed to delete file: " + file.getPath());
        }
    }

    void writeTo(java.lang.String fileName, T data) {
        java.io.File file = getFile(fileName);
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(file);
        try {
            java.io.FileOutputStream fileOutputStream = atomicFile.startWrite();
            try {
                android.util.proto.ProtoOutputStream protoOutputStream = new android.util.proto.ProtoOutputStream(fileOutputStream);
                protoStreamWriter().write(protoOutputStream, data);
                protoOutputStream.flush();
                atomicFile.finishWrite(fileOutputStream);
                fileOutputStream = null;
            } finally {
                atomicFile.failWrite(fileOutputStream);
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write to protobuf file.", e);
        }
    }

    T read(final java.lang.String fileName) {
        java.io.File[] files = this.mRootDir.listFiles(new java.io.FileFilter() { // from class: com.android.server.people.data.AbstractProtoDiskReadWriter$$ExternalSyntheticLambda1
            @Override // java.io.FileFilter
            public final boolean accept(java.io.File file) {
                return com.android.server.people.data.AbstractProtoDiskReadWriter.lambda$read$0(fileName, file);
            }
        });
        if (files == null || files.length == 0) {
            return null;
        }
        if (files.length > 1) {
            android.util.Slog.w(TAG, "Found multiple files with the same name: " + java.util.Arrays.toString(files));
        }
        return parseFile(files[0]);
    }

    static /* synthetic */ boolean lambda$read$0(java.lang.String fileName, java.io.File pathname) {
        return pathname.isFile() && pathname.getName().equals(fileName);
    }

    synchronized void scheduleSave(java.lang.String fileName, T data) {
        this.mScheduledFileDataMap.put(fileName, data);
        if (this.mScheduledExecutorService.isShutdown()) {
            android.util.Slog.e(TAG, "Worker is shutdown, failed to schedule data saving.");
        } else {
            if (this.mScheduledFuture != null) {
                return;
            }
            this.mScheduledFuture = this.mScheduledExecutorService.schedule(new com.android.server.people.data.AbstractProtoDiskReadWriter$$ExternalSyntheticLambda0(this), 120000L, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
    }

    void saveImmediately(java.lang.String fileName, T data) {
        synchronized (this) {
            this.mScheduledFileDataMap.put(fileName, data);
        }
        triggerScheduledFlushEarly();
    }

    private void triggerScheduledFlushEarly() {
        synchronized (this) {
            if (!this.mScheduledFileDataMap.isEmpty() && !this.mScheduledExecutorService.isShutdown()) {
                if (this.mScheduledFuture != null) {
                    this.mScheduledFuture.cancel(true);
                }
                java.util.concurrent.Future<?> future = this.mScheduledExecutorService.submit(new com.android.server.people.data.AbstractProtoDiskReadWriter$$ExternalSyntheticLambda0(this));
                try {
                    future.get(SHUTDOWN_DISK_WRITE_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                    android.util.Slog.e(TAG, "Failed to save data immediately.", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void flushScheduledData() {
        if (this.mScheduledFileDataMap.isEmpty()) {
            this.mScheduledFuture = null;
            return;
        }
        for (java.lang.String fileName : this.mScheduledFileDataMap.keySet()) {
            T data = this.mScheduledFileDataMap.get(fileName);
            writeTo(fileName, data);
        }
        this.mScheduledFileDataMap.clear();
        this.mScheduledFuture = null;
    }

    private T parseFile(java.io.File file) {
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(file);
        try {
            java.io.FileInputStream fileInputStream = atomicFile.openRead();
            try {
                android.util.proto.ProtoInputStream protoInputStream = new android.util.proto.ProtoInputStream(fileInputStream);
                T t = protoStreamReader().read(protoInputStream);
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return t;
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to parse protobuf file.", e);
            return null;
        }
    }

    private java.io.File getFile(java.lang.String fileName) {
        return new java.io.File(this.mRootDir, fileName);
    }
}
