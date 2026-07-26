package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ShortcutBitmapSaver {
    private static final boolean ADD_DELAY_BEFORE_SAVE_FOR_TEST = false;
    private static final boolean DEBUG = com.android.server.pm.ShortcutService.DEBUG;
    private static final java.util.concurrent.ThreadFactory FACTORY = new java.util.concurrent.ThreadFactory() { // from class: com.android.server.pm.ShortcutBitmapSaver.1
        private final java.util.concurrent.atomic.AtomicLong mCounter = new java.util.concurrent.atomic.AtomicLong(0);

        @Override // java.util.concurrent.ThreadFactory
        public java.lang.Thread newThread(java.lang.Runnable r) {
            return new java.lang.Thread(r, "ShortcutSave-" + this.mCounter.incrementAndGet());
        }
    };
    private static final long SAVE_DELAY_MS_FOR_TEST = 1000;
    private static final java.lang.String TAG = "ShortcutService";
    private final com.android.server.pm.IShortcutBitmapSaverExt mExt;
    private final com.android.server.pm.ShortcutService mService;
    private final long SAVE_WAIT_TIMEOUT_MS = 5000;
    private final java.util.concurrent.ExecutorService mExecutor = new java.util.concurrent.ThreadPoolExecutor(0, 1, 60, java.util.concurrent.TimeUnit.SECONDS, new java.util.concurrent.LinkedBlockingQueue(), FACTORY);
    private final java.util.Deque<com.android.server.pm.ShortcutBitmapSaver.PendingItem> mPendingItems = new java.util.concurrent.LinkedBlockingDeque();
    private final java.lang.Runnable mRunnable = new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutBitmapSaver$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$1();
        }
    };

    private static class PendingItem {
        public final byte[] bytes;
        private final long mInstantiatedUptimeMillis;
        public final android.content.pm.ShortcutInfo shortcut;

        private PendingItem(android.content.pm.ShortcutInfo shortcut, byte[] bytes) {
            this.shortcut = shortcut;
            this.bytes = bytes;
            this.mInstantiatedUptimeMillis = android.os.SystemClock.uptimeMillis();
        }

        public java.lang.String toString() {
            return "PendingItem{size=" + this.bytes.length + " age=" + (android.os.SystemClock.uptimeMillis() - this.mInstantiatedUptimeMillis) + "ms shortcut=" + this.shortcut.toInsecureString() + "}";
        }
    }

    public ShortcutBitmapSaver(com.android.server.pm.ShortcutService service) {
        this.mService = service;
        this.mExt = (com.android.server.pm.IShortcutBitmapSaverExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IShortcutBitmapSaverExt.class).base(this.mService).create();
    }

    public boolean waitForAllSavesLocked() {
        if (this.mExt != null) {
            try {
                return this.mExt.waitForAllSaves();
            } catch (android.util.AndroidException e) {
            }
        }
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutBitmapSaver$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                latch.countDown();
            }
        });
        try {
            if (latch.await(5000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                return true;
            }
            this.mService.wtf("Timed out waiting on saving bitmaps.");
            return false;
        } catch (java.lang.InterruptedException e2) {
            android.util.Slog.w(TAG, "interrupted");
            return false;
        }
    }

    public java.lang.String getBitmapPathMayWaitLocked(android.content.pm.ShortcutInfo shortcut) {
        boolean success = waitForAllSavesLocked();
        if (success && shortcut.hasIconFile()) {
            return shortcut.getBitmapPath();
        }
        return null;
    }

    public void removeIcon(android.content.pm.ShortcutInfo shortcut) {
        shortcut.setIconResourceId(0);
        shortcut.setIconResName(null);
        shortcut.setBitmapPath(null);
        shortcut.setIconUri(null);
        shortcut.clearFlags(35340);
    }

    public void saveBitmapLocked(android.content.pm.ShortcutInfo shortcut, int maxDimension, android.graphics.Bitmap.CompressFormat format, int quality) {
        android.graphics.drawable.Icon icon = shortcut.getIcon();
        java.util.Objects.requireNonNull(icon);
        android.graphics.Bitmap original = icon.getBitmap();
        if (original == null) {
            android.util.Log.e(TAG, "Missing icon: " + shortcut);
            return;
        }
        android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.getThreadPolicy();
        try {
            try {
                android.os.StrictMode.setThreadPolicy(new android.os.StrictMode.ThreadPolicy.Builder(oldPolicy).permitCustomSlowCalls().build());
                android.graphics.Bitmap shrunk = com.android.server.pm.ShortcutService.shrinkBitmap(original, maxDimension);
                try {
                    java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(65536);
                    try {
                        if (!shrunk.compress(format, quality, out)) {
                            android.util.Slog.wtf(TAG, "Unable to compress bitmap");
                        }
                        out.flush();
                        byte[] bytes = out.toByteArray();
                        out.close();
                        out.close();
                        android.os.StrictMode.setThreadPolicy(oldPolicy);
                        shortcut.addFlags(2056);
                        if (icon.getType() == 5) {
                            shortcut.addFlags(512);
                        }
                        com.android.server.pm.ShortcutBitmapSaver.PendingItem item = new com.android.server.pm.ShortcutBitmapSaver.PendingItem(shortcut, bytes);
                        synchronized (this.mPendingItems) {
                            this.mPendingItems.add(item);
                        }
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Scheduling to save: " + item);
                        }
                        if (this.mExt == null || !this.mExt.processPendingItems(this.mExecutor, this.mRunnable)) {
                            this.mExecutor.execute(this.mRunnable);
                        }
                    } finally {
                    }
                } finally {
                    if (shrunk != original) {
                        shrunk.recycle();
                    }
                }
            } catch (java.io.IOException | java.lang.OutOfMemoryError | java.lang.RuntimeException e) {
                android.util.Slog.wtf(TAG, "Unable to write bitmap to file", e);
                android.os.StrictMode.setThreadPolicy(oldPolicy);
            }
        } catch (java.lang.Throwable th) {
            android.os.StrictMode.setThreadPolicy(oldPolicy);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        if (android.os.Trace.isTagEnabled(8192L)) {
            android.os.Trace.traceBegin(8192L, "ShortcutService:processPendingItems");
        }
        do {
            try {
            } finally {
                android.os.Trace.traceEnd(8192L);
            }
        } while (processPendingItems());
    }

    private boolean processPendingItems() {
        android.content.pm.ShortcutInfo shortcut = null;
        try {
            synchronized (this.mPendingItems) {
                if (this.mPendingItems.size() == 0) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Saved bitmap.");
                    }
                    if (0 == 0) {
                        return false;
                    }
                    if (shortcut.getBitmapPath() == null) {
                        removeIcon(null);
                    }
                    shortcut.clearFlags(2048);
                    return false;
                }
                com.android.server.pm.ShortcutBitmapSaver.PendingItem item = this.mPendingItems.pop();
                shortcut = item.shortcut;
                if (!shortcut.isIconPendingSave()) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Saved bitmap.");
                    }
                    if (shortcut != null) {
                        if (shortcut.getBitmapPath() == null) {
                            removeIcon(shortcut);
                        }
                        shortcut.clearFlags(2048);
                    }
                    return true;
                }
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Saving bitmap: " + item);
                }
                java.io.File file = null;
                try {
                    com.android.server.pm.ShortcutService.FileOutputStreamWithPath out = this.mService.openIconFileForWrite(shortcut.getUserId(), shortcut);
                    java.io.File file2 = out.getFile();
                    try {
                        out.write(item.bytes);
                        libcore.io.IoUtils.closeQuietly(out);
                        java.lang.String path = file2.getAbsolutePath();
                        shortcut.setBitmapPath(path);
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Saved bitmap.");
                        }
                        if (shortcut != null) {
                            if (shortcut.getBitmapPath() == null) {
                                removeIcon(shortcut);
                            }
                            shortcut.clearFlags(2048);
                        }
                        return true;
                    } catch (java.lang.Throwable th) {
                        libcore.io.IoUtils.closeQuietly(out);
                        throw th;
                    }
                } catch (java.io.IOException | java.lang.RuntimeException e) {
                    android.util.Slog.e(TAG, "Unable to write bitmap to file", e);
                    if (0 != 0 && file.exists()) {
                        file.delete();
                    }
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Saved bitmap.");
                    }
                    if (shortcut != null) {
                        if (shortcut.getBitmapPath() == null) {
                            removeIcon(shortcut);
                        }
                        shortcut.clearFlags(2048);
                    }
                    return true;
                }
            }
        } catch (java.lang.Throwable th2) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Saved bitmap.");
            }
            if (shortcut != null) {
                if (shortcut.getBitmapPath() == null) {
                    removeIcon(shortcut);
                }
                shortcut.clearFlags(2048);
            }
            throw th2;
        }
    }

    public void dumpLocked(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this.mPendingItems) {
            int N = this.mPendingItems.size();
            pw.print(prefix);
            pw.println("Pending saves: Num=" + N + " Executor=" + this.mExecutor);
            for (com.android.server.pm.ShortcutBitmapSaver.PendingItem item : this.mPendingItems) {
                pw.print(prefix);
                pw.print("  ");
                pw.println(item);
            }
        }
    }
}
