package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class BaseAppSnapshotPersister {
    static final java.lang.String BITMAP_EXTENSION = ".jpg";
    static final java.lang.String LOW_RES_FILE_POSTFIX = "_reduced";
    static final java.lang.String PROTO_EXTENSION = ".proto";
    static final java.lang.String WEBP_EXTENSION = ".webp";
    protected final java.lang.Object mLock;
    protected final com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;
    protected final com.android.server.wm.SnapshotPersistQueue mSnapshotPersistQueue;

    interface DirectoryResolver {
        java.io.File getSystemDirectoryForUser(int i);
    }

    BaseAppSnapshotPersister(com.android.server.wm.SnapshotPersistQueue persistQueue, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
        this.mSnapshotPersistQueue = persistQueue;
        this.mPersistInfoProvider = persistInfoProvider;
        this.mLock = persistQueue.getLock();
    }

    void persistSnapshot(int id, int userId, android.window.TaskSnapshot snapshot) {
        synchronized (this.mLock) {
            this.mSnapshotPersistQueue.sendToQueueLocked(this.mSnapshotPersistQueue.createStoreWriteQueueItem(id, userId, snapshot, this.mPersistInfoProvider));
        }
    }

    void removeSnapshot(int id, int userId) {
        synchronized (this.mLock) {
            this.mSnapshotPersistQueue.sendToQueueLocked(this.mSnapshotPersistQueue.createDeleteWriteQueueItem(id, userId, this.mPersistInfoProvider));
        }
    }

    static class PersistInfoProvider {
        private final java.lang.String mDirName;
        protected final com.android.server.wm.BaseAppSnapshotPersister.DirectoryResolver mDirectoryResolver;
        private final boolean mEnableLowResSnapshots;
        private boolean mForceReduceSnapshot;
        private final float mLowResScaleFactor;
        private final boolean mUse16BitFormat;

        PersistInfoProvider(com.android.server.wm.BaseAppSnapshotPersister.DirectoryResolver directoryResolver, java.lang.String dirName, boolean enableLowResSnapshots, float lowResScaleFactor, boolean use16BitFormat, boolean forceReduceSnapshot) {
            this(directoryResolver, dirName, enableLowResSnapshots, lowResScaleFactor, use16BitFormat);
            this.mForceReduceSnapshot = forceReduceSnapshot;
        }

        PersistInfoProvider(com.android.server.wm.BaseAppSnapshotPersister.DirectoryResolver directoryResolver, java.lang.String dirName, boolean enableLowResSnapshots, float lowResScaleFactor, boolean use16BitFormat) {
            this.mDirectoryResolver = directoryResolver;
            this.mDirName = dirName;
            this.mEnableLowResSnapshots = enableLowResSnapshots;
            this.mLowResScaleFactor = lowResScaleFactor;
            this.mUse16BitFormat = use16BitFormat;
        }

        java.io.File getDirectory(int userId) {
            return new java.io.File(this.mDirectoryResolver.getSystemDirectoryForUser(userId), this.mDirName);
        }

        boolean use16BitFormat() {
            return this.mUse16BitFormat;
        }

        boolean createDirectory(int userId) {
            java.io.File dir = getDirectory(userId);
            return dir.exists() || dir.mkdir();
        }

        java.io.File getProtoFile(int index, int userId) {
            return new java.io.File(getDirectory(userId), index + com.android.server.wm.BaseAppSnapshotPersister.PROTO_EXTENSION);
        }

        java.io.File getLowResolutionBitmapFile(int index, int userId) {
            if (this.mForceReduceSnapshot) {
                return new java.io.File(getDirectory(userId), index + com.android.server.wm.BaseAppSnapshotPersister.LOW_RES_FILE_POSTFIX + com.android.server.wm.BaseAppSnapshotPersister.WEBP_EXTENSION);
            }
            return new java.io.File(getDirectory(userId), index + com.android.server.wm.BaseAppSnapshotPersister.LOW_RES_FILE_POSTFIX + com.android.server.wm.BaseAppSnapshotPersister.BITMAP_EXTENSION);
        }

        java.io.File getHighResolutionBitmapFile(int index, int userId) {
            return new java.io.File(getDirectory(userId), index + com.android.server.wm.BaseAppSnapshotPersister.BITMAP_EXTENSION);
        }

        boolean enableLowResSnapshots() {
            return this.mEnableLowResSnapshots;
        }

        float lowResScaleFactor() {
            return this.mLowResScaleFactor;
        }

        boolean getForceReduceSnapshot() {
            return this.mForceReduceSnapshot;
        }
    }
}
