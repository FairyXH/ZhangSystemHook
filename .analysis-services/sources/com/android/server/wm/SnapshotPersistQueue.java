package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SnapshotPersistQueue {
    private static final int COMPRESS_QUALITY = 95;
    private static final long DELAY_MS = 100;
    private static final int MAX_STORE_QUEUE_DEPTH = 2;
    private static final java.lang.String TAG = "WindowManager";
    private boolean mPaused;
    private boolean mQueueIdling;
    private boolean mStarted;
    private final java.util.ArrayDeque<com.android.server.wm.SnapshotPersistQueue.WriteQueueItem> mWriteQueue = new java.util.ArrayDeque<>();
    private final java.util.ArrayDeque<com.android.server.wm.SnapshotPersistQueue.StoreWriteQueueItem> mStoreQueueItems = new java.util.ArrayDeque<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.lang.Thread mPersister = new java.lang.Thread("TaskSnapshotPersister") { // from class: com.android.server.wm.SnapshotPersistQueue.1
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            com.android.server.wm.SnapshotPersistQueue.WriteQueueItem next;
            android.os.Process.setThreadPriority(10);
            while (true) {
                boolean isReadyToWrite = false;
                synchronized (com.android.server.wm.SnapshotPersistQueue.this.mLock) {
                    if (com.android.server.wm.SnapshotPersistQueue.this.mPaused) {
                        next = null;
                    } else {
                        next = (com.android.server.wm.SnapshotPersistQueue.WriteQueueItem) com.android.server.wm.SnapshotPersistQueue.this.mWriteQueue.poll();
                        if (next != null) {
                            if (next.isReady(com.android.server.wm.SnapshotPersistQueue.this.mUserManagerInternal)) {
                                isReadyToWrite = true;
                                next.onDequeuedLocked();
                            } else {
                                com.android.server.wm.SnapshotPersistQueue.this.mWriteQueue.addLast(next);
                            }
                        }
                    }
                }
                if (next != null) {
                    if (isReadyToWrite) {
                        next.write();
                    }
                    android.os.SystemClock.sleep(com.android.server.wm.SnapshotPersistQueue.DELAY_MS);
                }
                synchronized (com.android.server.wm.SnapshotPersistQueue.this.mLock) {
                    boolean writeQueueEmpty = com.android.server.wm.SnapshotPersistQueue.this.mWriteQueue.isEmpty();
                    if (writeQueueEmpty || com.android.server.wm.SnapshotPersistQueue.this.mPaused) {
                        try {
                            com.android.server.wm.SnapshotPersistQueue.this.mQueueIdling = writeQueueEmpty;
                            com.android.server.wm.SnapshotPersistQueue.this.mLock.wait();
                            com.android.server.wm.SnapshotPersistQueue.this.mQueueIdling = false;
                        } catch (java.lang.InterruptedException e) {
                        }
                    }
                }
            }
        }
    };
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);

    SnapshotPersistQueue() {
    }

    java.lang.Object getLock() {
        return this.mLock;
    }

    void systemReady() {
        start();
    }

    void start() {
        if (!this.mStarted) {
            this.mStarted = true;
            this.mPersister.start();
        }
    }

    void setPaused(boolean paused) {
        synchronized (this.mLock) {
            this.mPaused = paused;
            if (!paused) {
                this.mLock.notifyAll();
            }
        }
    }

    void waitForQueueEmpty() {
        while (true) {
            synchronized (this.mLock) {
                if (this.mWriteQueue.isEmpty() && this.mQueueIdling) {
                    return;
                }
            }
            android.os.SystemClock.sleep(DELAY_MS);
        }
    }

    int peekQueueSize() {
        int size;
        synchronized (this.mLock) {
            size = this.mWriteQueue.size();
        }
        return size;
    }

    private void addToQueueInternal(com.android.server.wm.SnapshotPersistQueue.WriteQueueItem item, boolean insertToFront) {
        this.mWriteQueue.removeFirstOccurrence(item);
        if (insertToFront) {
            this.mWriteQueue.addFirst(item);
        } else {
            this.mWriteQueue.addLast(item);
        }
        item.onQueuedLocked();
        ensureStoreQueueDepthLocked();
        if (!this.mPaused) {
            this.mLock.notifyAll();
        }
    }

    void sendToQueueLocked(com.android.server.wm.SnapshotPersistQueue.WriteQueueItem item) {
        addToQueueInternal(item, false);
    }

    void insertQueueAtFirstLocked(com.android.server.wm.SnapshotPersistQueue.WriteQueueItem item) {
        addToQueueInternal(item, true);
    }

    private void ensureStoreQueueDepthLocked() {
        while (this.mStoreQueueItems.size() > 2) {
            com.android.server.wm.SnapshotPersistQueue.StoreWriteQueueItem item = this.mStoreQueueItems.poll();
            this.mWriteQueue.remove(item);
            android.util.Slog.i(TAG, "Queue is too deep! Purged item with index=" + item.mId);
        }
    }

    void deleteSnapshot(int index, int userId, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider provider) {
        java.io.File protoFile = provider.getProtoFile(index, userId);
        java.io.File bitmapLowResFile = provider.getLowResolutionBitmapFile(index, userId);
        if (protoFile.exists()) {
            protoFile.delete();
        }
        if (bitmapLowResFile.exists()) {
            bitmapLowResFile.delete();
        }
        java.io.File bitmapFile = provider.getHighResolutionBitmapFile(index, userId);
        if (bitmapFile.exists()) {
            bitmapFile.delete();
        }
    }

    static abstract class WriteQueueItem {
        protected final com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;
        protected final int mUserId;

        abstract void write();

        WriteQueueItem(com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider, int userId) {
            this.mPersistInfoProvider = persistInfoProvider;
            this.mUserId = userId;
        }

        boolean isReady(com.android.server.pm.UserManagerInternal userManager) {
            return userManager.isUserUnlocked(this.mUserId);
        }

        void onQueuedLocked() {
        }

        void onDequeuedLocked() {
        }
    }

    com.android.server.wm.SnapshotPersistQueue.StoreWriteQueueItem createStoreWriteQueueItem(int id, int userId, android.window.TaskSnapshot snapshot, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider provider) {
        return new com.android.server.wm.SnapshotPersistQueue.StoreWriteQueueItem(id, userId, snapshot, provider);
    }

    class StoreWriteQueueItem extends com.android.server.wm.SnapshotPersistQueue.WriteQueueItem {
        private final int mId;
        private final android.window.TaskSnapshot mSnapshot;

        StoreWriteQueueItem(int id, int userId, android.window.TaskSnapshot snapshot, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider provider) {
            super(provider, userId);
            this.mId = id;
            snapshot.addReference(4);
            this.mSnapshot = snapshot;
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void onQueuedLocked() {
            com.android.server.wm.SnapshotPersistQueue.this.mStoreQueueItems.remove(this);
            com.android.server.wm.SnapshotPersistQueue.this.mStoreQueueItems.offer(this);
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void onDequeuedLocked() {
            com.android.server.wm.SnapshotPersistQueue.this.mStoreQueueItems.remove(this);
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void write() {
            if (android.os.Trace.isTagEnabled(32L)) {
                android.os.Trace.traceBegin(32L, "StoreWriteQueueItem#" + this.mId);
            }
            if (!this.mPersistInfoProvider.createDirectory(this.mUserId)) {
                android.util.Slog.e(com.android.server.wm.SnapshotPersistQueue.TAG, "Unable to create snapshot directory for user dir=" + this.mPersistInfoProvider.getDirectory(this.mUserId));
            }
            boolean failed = false;
            if (!writeProto()) {
                failed = true;
            }
            if (!writeBuffer()) {
                failed = true;
            }
            if (failed) {
                com.android.server.wm.SnapshotPersistQueue.this.deleteSnapshot(this.mId, this.mUserId, this.mPersistInfoProvider);
            }
            this.mSnapshot.removeReference(4);
            android.os.Trace.traceEnd(32L);
        }

        boolean writeProto() {
            com.android.server.wm.nano.WindowManagerProtos.TaskSnapshotProto proto = new com.android.server.wm.nano.WindowManagerProtos.TaskSnapshotProto();
            proto.orientation = this.mSnapshot.getOrientation();
            proto.rotation = this.mSnapshot.getRotation();
            proto.taskWidth = this.mSnapshot.getTaskSize().x;
            proto.taskHeight = this.mSnapshot.getTaskSize().y;
            proto.insetLeft = this.mSnapshot.getContentInsets().left;
            proto.insetTop = this.mSnapshot.getContentInsets().top;
            proto.insetRight = this.mSnapshot.getContentInsets().right;
            proto.insetBottom = this.mSnapshot.getContentInsets().bottom;
            proto.letterboxInsetLeft = this.mSnapshot.getLetterboxInsets().left;
            proto.letterboxInsetTop = this.mSnapshot.getLetterboxInsets().top;
            proto.letterboxInsetRight = this.mSnapshot.getLetterboxInsets().right;
            proto.letterboxInsetBottom = this.mSnapshot.getLetterboxInsets().bottom;
            proto.isRealSnapshot = this.mSnapshot.isRealSnapshot();
            proto.windowingMode = this.mSnapshot.getWindowingMode();
            proto.appearance = this.mSnapshot.getAppearance();
            proto.isTranslucent = this.mSnapshot.isTranslucent();
            proto.topActivityComponent = this.mSnapshot.getTopActivityComponent().flattenToString();
            proto.uiMode = this.mSnapshot.getUiMode();
            proto.id = this.mSnapshot.getId();
            byte[] bytes = com.android.server.wm.nano.WindowManagerProtos.TaskSnapshotProto.toByteArray(proto);
            java.io.File file = this.mPersistInfoProvider.getProtoFile(this.mId, this.mUserId);
            android.util.AtomicFile atomicFile = new android.util.AtomicFile(file);
            java.io.FileOutputStream fos = null;
            try {
                fos = atomicFile.startWrite();
                fos.write(bytes);
                atomicFile.finishWrite(fos);
                return true;
            } catch (java.io.IOException e) {
                atomicFile.failWrite(fos);
                android.util.Slog.e(com.android.server.wm.SnapshotPersistQueue.TAG, "Unable to open " + file + " for persisting. " + e);
                return false;
            }
        }

        boolean writeBuffer() {
            android.graphics.Bitmap swBitmap;
            if (com.android.server.wm.AbsAppSnapshotController.isInvalidHardwareBuffer(this.mSnapshot.getHardwareBuffer())) {
                android.util.Slog.e(com.android.server.wm.SnapshotPersistQueue.TAG, "Invalid task snapshot hw buffer, taskId=" + this.mId);
                return false;
            }
            android.graphics.Bitmap bitmap = android.graphics.Bitmap.wrapHardwareBuffer(this.mSnapshot.getHardwareBuffer(), this.mSnapshot.getColorSpace());
            if (bitmap == null) {
                android.util.Slog.e(com.android.server.wm.SnapshotPersistQueue.TAG, "Invalid task snapshot hw bitmap");
                return false;
            }
            if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                boolean z = false;
                bitmap.recycle();
                return z;
            }
            android.graphics.Bitmap swBitmap2 = null;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (!this.mPersistInfoProvider.getForceReduceSnapshot()) {
                android.graphics.Bitmap swBitmap3 = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, false);
                if (swBitmap3 == null) {
                    android.util.Slog.e(com.android.server.wm.SnapshotPersistQueue.TAG, "Bitmap conversion from (config=" + bitmap.getConfig() + ", isMutable=" + bitmap.isMutable() + ") to (config=ARGB_8888, isMutable=false) failed.");
                    return false;
                }
                bitmap.recycle();
                if (swBitmap3 == null) {
                    return false;
                }
                if (swBitmap3.getWidth() > 0 && swBitmap3.getHeight() > 0) {
                    java.io.File file = this.mPersistInfoProvider.getHighResolutionBitmapFile(this.mId, this.mUserId);
                    try {
                        java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                        swBitmap3.compress(android.graphics.Bitmap.CompressFormat.JPEG, 95, fos);
                        fos.close();
                        if (this.mPersistInfoProvider.enableLowResSnapshots()) {
                            swBitmap2 = swBitmap3;
                        } else {
                            swBitmap3.recycle();
                            return true;
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.wm.SnapshotPersistQueue.TAG, "Unable to open " + file + " for persisting.", e);
                        return false;
                    }
                } else {
                    swBitmap3.recycle();
                    return false;
                }
            }
            if (swBitmap2 != null) {
                swBitmap = swBitmap2;
            } else {
                swBitmap = bitmap;
            }
            int dstWidth = (int) (width * this.mPersistInfoProvider.lowResScaleFactor());
            int dstHeight = (int) (height * this.mPersistInfoProvider.lowResScaleFactor());
            if (dstWidth <= 0 || dstHeight <= 0) {
                boolean z2 = false;
                swBitmap.recycle();
                return z2;
            }
            android.graphics.Bitmap lowResBitmap = android.graphics.Bitmap.createScaledBitmap(swBitmap, dstWidth, dstHeight, true);
            swBitmap.recycle();
            java.io.File lowResFile = this.mPersistInfoProvider.getLowResolutionBitmapFile(this.mId, this.mUserId);
            try {
                java.io.FileOutputStream lowResFos = new java.io.FileOutputStream(lowResFile);
                if (!this.mPersistInfoProvider.getForceReduceSnapshot()) {
                    lowResBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 95, lowResFos);
                } else {
                    lowResBitmap.compress(android.graphics.Bitmap.CompressFormat.WEBP_LOSSY, 95, lowResFos);
                }
                lowResFos.close();
                lowResBitmap.recycle();
                return true;
            } catch (java.io.IOException e2) {
                android.util.Slog.e(com.android.server.wm.SnapshotPersistQueue.TAG, "Unable to open " + lowResFile + " for persisting.", e2);
                return false;
            }
        }

        public boolean equals(java.lang.Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.wm.SnapshotPersistQueue.StoreWriteQueueItem other = (com.android.server.wm.SnapshotPersistQueue.StoreWriteQueueItem) o;
            return this.mId == other.mId && this.mUserId == other.mUserId && this.mPersistInfoProvider == other.mPersistInfoProvider;
        }

        public java.lang.String toString() {
            return "StoreWriteQueueItem{ID=" + this.mId + ", UserId=" + this.mUserId + "}";
        }
    }

    com.android.server.wm.SnapshotPersistQueue.DeleteWriteQueueItem createDeleteWriteQueueItem(int id, int userId, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider provider) {
        return new com.android.server.wm.SnapshotPersistQueue.DeleteWriteQueueItem(id, userId, provider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DeleteWriteQueueItem extends com.android.server.wm.SnapshotPersistQueue.WriteQueueItem {
        private final int mId;

        DeleteWriteQueueItem(int id, int userId, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider provider) {
            super(provider, userId);
            this.mId = id;
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void write() {
            android.os.Trace.traceBegin(32L, "DeleteWriteQueueItem");
            com.android.server.wm.SnapshotPersistQueue.this.deleteSnapshot(this.mId, this.mUserId, this.mPersistInfoProvider);
            android.os.Trace.traceEnd(32L);
        }

        public java.lang.String toString() {
            return "DeleteWriteQueueItem{ID=" + this.mId + ", UserId=" + this.mUserId + "}";
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        com.android.server.wm.SnapshotPersistQueue.WriteQueueItem[] items;
        synchronized (this.mLock) {
            items = (com.android.server.wm.SnapshotPersistQueue.WriteQueueItem[]) this.mWriteQueue.toArray(new com.android.server.wm.SnapshotPersistQueue.WriteQueueItem[0]);
        }
        if (items.length == 0) {
            return;
        }
        pw.println(prefix + "PersistQueue contains:");
        for (int i = items.length - 1; i >= 0; i--) {
            pw.println(prefix + "  " + items[i] + "");
        }
    }
}
