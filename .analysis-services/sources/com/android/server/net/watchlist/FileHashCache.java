package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
public class FileHashCache {
    private static final boolean DEBUG = false;
    private static final boolean VERIFY = false;
    private android.os.Handler mHandler;
    private static final java.lang.String TAG = com.android.server.net.watchlist.FileHashCache.class.getSimpleName();
    private static boolean sLoggedWtf = false;
    static java.lang.String sPersistFileName = "/data/system/file_hash_cache";
    static long sSaveDeferredDelayMillis = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);
    private final java.util.Map<java.io.File, com.android.server.net.watchlist.FileHashCache.Entry> mEntries = new java.util.HashMap();
    private final java.lang.Runnable mLoadTask = new java.lang.Runnable() { // from class: com.android.server.net.watchlist.FileHashCache$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private final java.lang.Runnable mSaveTask = new java.lang.Runnable() { // from class: com.android.server.net.watchlist.FileHashCache$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$1();
        }
    };

    private static class Entry {
        public final long mLastModified;
        public final byte[] mSha256Hash;

        Entry(long lastModified, byte[] sha256Hash) {
            this.mLastModified = lastModified;
            this.mSha256Hash = sha256Hash;
        }
    }

    public FileHashCache(android.os.Handler handler) {
        this.mHandler = handler;
        this.mHandler.post(this.mLoadTask);
    }

    byte[] getSha256HashFromCache(java.io.File file) {
        if (!this.mHandler.getLooper().isCurrentThread()) {
            android.util.Slog.wtf(TAG, "Request from invalid thread", new java.lang.Exception());
            return null;
        }
        com.android.server.net.watchlist.FileHashCache.Entry entry = this.mEntries.get(file);
        if (entry == null) {
            return null;
        }
        try {
            if (entry.mLastModified == android.system.Os.stat(file.getAbsolutePath()).st_ctime) {
                return entry.mSha256Hash;
            }
        } catch (android.system.ErrnoException e) {
        }
        this.mEntries.remove(file);
        return null;
    }

    public byte[] getSha256Hash(java.io.File file) throws java.security.NoSuchAlgorithmException, java.io.IOException {
        byte[] sha256Hash = getSha256HashFromCache(file);
        if (sha256Hash != null) {
            return sha256Hash;
        }
        try {
            byte[] sha256Hash2 = com.android.server.net.watchlist.DigestUtils.getSha256Hash(file);
            this.mEntries.put(file, new com.android.server.net.watchlist.FileHashCache.Entry(android.system.Os.stat(file.getAbsolutePath()).st_ctime, sha256Hash2));
            scheduleSave();
            return sha256Hash2;
        } catch (android.system.ErrnoException e) {
            throw new java.io.IOException(e);
        }
    }

    private static void closeQuietly(java.io.Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (java.io.IOException e) {
            }
        }
    }

    private static void logWtfOnce(java.lang.String s, java.lang.Exception e) {
        if (!sLoggedWtf) {
            android.util.Slog.wtf(TAG, s, e);
            sLoggedWtf = true;
        } else {
            android.util.Slog.w(TAG, s, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: load, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        this.mEntries.clear();
        android.os.SystemClock.currentTimeMicro();
        java.io.File file = new java.io.File(sPersistFileName);
        if (!file.exists()) {
            return;
        }
        java.io.BufferedReader reader = null;
        try {
            try {
                reader = new java.io.BufferedReader(new java.io.FileReader(file));
                reader.lines().forEach(new java.util.function.Consumer() { // from class: com.android.server.net.watchlist.FileHashCache$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$load$2((java.lang.String) obj);
                    }
                });
            } catch (java.io.IOException | java.io.UncheckedIOException e) {
                android.util.Slog.e(TAG, "Failed to read storage file", e);
            }
        } finally {
            closeQuietly(reader);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$load$2(java.lang.String fileEntry) {
        try {
            java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(fileEntry, ",");
            java.io.File testFile = new java.io.File(tokenizer.nextToken());
            long lastModified = java.lang.Long.parseLong(tokenizer.nextToken());
            byte[] sha256 = com.android.internal.util.HexDump.hexStringToByteArray(tokenizer.nextToken());
            this.mEntries.put(testFile, new com.android.server.net.watchlist.FileHashCache.Entry(lastModified, sha256));
        } catch (java.lang.RuntimeException e) {
            logWtfOnce("Invalid entry for " + fileEntry, e);
        }
    }

    private void scheduleSave() {
        this.mHandler.removeCallbacks(this.mSaveTask);
        this.mHandler.postDelayed(this.mSaveTask, sSaveDeferredDelayMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: save, reason: merged with bridge method [inline-methods] */
    public void lambda$new$1() {
        java.io.BufferedWriter writer = null;
        android.os.SystemClock.currentTimeMicro();
        try {
            try {
                writer = new java.io.BufferedWriter(new java.io.FileWriter(sPersistFileName));
                for (java.util.Map.Entry<java.io.File, com.android.server.net.watchlist.FileHashCache.Entry> entry : this.mEntries.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue().mLastModified + "," + com.android.internal.util.HexDump.toHexString(entry.getValue().mSha256Hash) + "\n");
                }
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to save.", e);
            }
        } finally {
            closeQuietly(writer);
        }
    }
}
