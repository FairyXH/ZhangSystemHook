package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
class HibernationStateDiskStore<T> {
    private static final long DISK_WRITE_DELAY = 60000;
    private static final java.lang.String STATES_FILE_NAME = "states";
    private static final java.lang.String TAG = "HibernationStateDiskStore";
    private final java.util.concurrent.ScheduledExecutorService mExecutorService;
    private java.util.concurrent.ScheduledFuture<?> mFuture;
    private final java.io.File mHibernationFile;
    private final com.android.server.apphibernation.ProtoReadWriter<java.util.List<T>> mProtoReadWriter;
    private java.util.List<T> mScheduledStatesToWrite;

    HibernationStateDiskStore(java.io.File hibernationDir, com.android.server.apphibernation.ProtoReadWriter<java.util.List<T>> readWriter, java.util.concurrent.ScheduledExecutorService executorService) {
        this(hibernationDir, readWriter, executorService, STATES_FILE_NAME);
    }

    HibernationStateDiskStore(java.io.File hibernationDir, com.android.server.apphibernation.ProtoReadWriter<java.util.List<T>> readWriter, java.util.concurrent.ScheduledExecutorService executorService, java.lang.String fileName) {
        this.mScheduledStatesToWrite = new java.util.ArrayList();
        this.mHibernationFile = new java.io.File(hibernationDir, fileName);
        this.mExecutorService = executorService;
        this.mProtoReadWriter = readWriter;
    }

    void scheduleWriteHibernationStates(java.util.List<T> hibernationStates) {
        synchronized (this) {
            this.mScheduledStatesToWrite = hibernationStates;
            if (this.mExecutorService.isShutdown()) {
                android.util.Slog.e(TAG, "Scheduled executor service is shut down.");
            } else if (this.mFuture != null) {
                android.util.Slog.i(TAG, "Write already scheduled. Skipping schedule.");
            } else {
                this.mFuture = this.mExecutorService.schedule(new java.lang.Runnable() { // from class: com.android.server.apphibernation.HibernationStateDiskStore$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.writeHibernationStates();
                    }
                }, 60000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
        }
    }

    java.util.List<T> readHibernationStates() {
        synchronized (this) {
            if (!this.mHibernationFile.exists()) {
                android.util.Slog.i(TAG, "No hibernation file on disk for file " + this.mHibernationFile.getPath());
                return null;
            }
            android.util.AtomicFile atomicFile = new android.util.AtomicFile(this.mHibernationFile);
            try {
                java.io.FileInputStream inputStream = atomicFile.openRead();
                android.util.proto.ProtoInputStream protoInputStream = new android.util.proto.ProtoInputStream(inputStream);
                return this.mProtoReadWriter.readFromProto(protoInputStream);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to read states protobuf.", e);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeHibernationStates() {
        synchronized (this) {
            writeStateProto(this.mScheduledStatesToWrite);
            this.mScheduledStatesToWrite.clear();
            this.mFuture = null;
        }
    }

    private void writeStateProto(java.util.List<T> states) {
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(this.mHibernationFile);
        try {
            java.io.FileOutputStream fileOutputStream = atomicFile.startWrite();
            try {
                android.util.proto.ProtoOutputStream protoOutputStream = new android.util.proto.ProtoOutputStream(fileOutputStream);
                this.mProtoReadWriter.writeToProto(protoOutputStream, states);
                protoOutputStream.flush();
                atomicFile.finishWrite(fileOutputStream);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to finish write to states protobuf.", e);
                atomicFile.failWrite(fileOutputStream);
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Failed to start write to states protobuf.", e2);
        }
    }
}
