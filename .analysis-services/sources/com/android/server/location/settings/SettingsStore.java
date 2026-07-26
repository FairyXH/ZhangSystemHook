package com.android.server.location.settings;

/* JADX INFO: loaded from: classes2.dex */
abstract class SettingsStore<T extends com.android.server.location.settings.SettingsStore.VersionedSettings> {
    private T mCache;
    private final android.util.AtomicFile mFile;
    private boolean mInitialized;

    interface VersionedSettings {
        public static final int VERSION_DOES_NOT_EXIST = Integer.MAX_VALUE;

        int getVersion();
    }

    protected abstract void onChange(T t, T t2);

    protected abstract T read(int i, java.io.DataInput dataInput) throws java.io.IOException;

    protected abstract void write(java.io.DataOutput dataOutput, T t) throws java.io.IOException;

    protected SettingsStore(java.io.File file) {
        this.mFile = new android.util.AtomicFile(file);
    }

    public final synchronized void initializeCache() {
        if (!this.mInitialized) {
            if (this.mFile.exists()) {
                try {
                    java.io.DataInputStream dataInputStream = new java.io.DataInputStream(this.mFile.openRead());
                    try {
                        this.mCache = (T) read(dataInputStream.readInt(), dataInputStream);
                        com.android.internal.util.Preconditions.checkState(this.mCache.getVersion() < Integer.MAX_VALUE);
                        dataInputStream.close();
                    } catch (java.lang.Throwable th) {
                        try {
                            dataInputStream.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (java.io.IOException e) {
                    android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "error reading location settings (" + this.mFile + "), falling back to defaults", e);
                }
            }
            if (this.mCache == null) {
                try {
                    this.mCache = (T) read(Integer.MAX_VALUE, new java.io.DataInputStream(new java.io.ByteArrayInputStream(new byte[0])));
                    com.android.internal.util.Preconditions.checkState(this.mCache.getVersion() < Integer.MAX_VALUE);
                } catch (java.io.IOException e2) {
                    throw new java.lang.AssertionError(e2);
                }
            }
            this.mInitialized = true;
        }
    }

    public final synchronized T get() {
        initializeCache();
        return this.mCache;
    }

    public synchronized void update(java.util.function.Function<T, T> function) {
        initializeCache();
        T t = this.mCache;
        T t2 = (T) java.util.Objects.requireNonNull(function.apply(t));
        if (t.equals(t2)) {
            return;
        }
        this.mCache = t2;
        com.android.internal.util.Preconditions.checkState(this.mCache.getVersion() < Integer.MAX_VALUE);
        writeLazily(t2);
        onChange(t, t2);
    }

    synchronized void flushFile() throws java.lang.InterruptedException {
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.Executor executor = com.android.internal.os.BackgroundThread.getExecutor();
        java.util.Objects.requireNonNull(latch);
        executor.execute(new com.android.server.location.settings.SettingsStore$$ExternalSyntheticLambda0(latch));
        latch.await();
    }

    synchronized void deleteFile() throws java.lang.InterruptedException {
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        com.android.internal.os.BackgroundThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.settings.SettingsStore$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$deleteFile$0(latch);
            }
        });
        latch.await();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteFile$0(java.util.concurrent.CountDownLatch latch) {
        this.mFile.delete();
        latch.countDown();
    }

    private void writeLazily(final T settings) {
        com.android.internal.os.BackgroundThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.settings.SettingsStore$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$writeLazily$1(settings);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeLazily$1(com.android.server.location.settings.SettingsStore.VersionedSettings settings) {
        java.io.FileOutputStream os = null;
        try {
            os = this.mFile.startWrite();
            java.io.DataOutputStream out = new java.io.DataOutputStream(os);
            out.writeInt(settings.getVersion());
            write(out, settings);
            this.mFile.finishWrite(os);
        } catch (java.io.IOException e) {
            this.mFile.failWrite(os);
            android.util.Log.e(com.android.server.location.LocationManagerService.TAG, "failure serializing location settings", e);
        } catch (java.lang.Throwable e2) {
            this.mFile.failWrite(os);
            throw e2;
        }
    }
}
