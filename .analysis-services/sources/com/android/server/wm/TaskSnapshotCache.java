package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskSnapshotCache extends com.android.server.wm.SnapshotCache<com.android.server.wm.Task> {
    private final com.android.server.wm.AppSnapshotLoader mLoader;
    public com.android.server.wm.ITaskSnapshotCacheExt mTaskSnapCacheExt;

    TaskSnapshotCache(com.android.server.wm.AppSnapshotLoader loader) {
        super("Task");
        this.mTaskSnapCacheExt = (com.android.server.wm.ITaskSnapshotCacheExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskSnapshotCacheExt.class).base(this).create();
        this.mLoader = loader;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.SnapshotCache
    public void putSnapshot(com.android.server.wm.Task task, android.window.TaskSnapshot snapshot) {
        synchronized (this.mLock) {
            snapshot.addReference(2);
            com.android.server.wm.SnapshotCache.CacheEntry entry = this.mRunningCache.get(java.lang.Integer.valueOf(task.mTaskId));
            if (entry != null) {
                this.mAppIdMap.remove(entry.topApp);
                entry.snapshot.removeReference(2);
            }
            com.android.server.wm.ActivityRecord top = this.mTaskSnapCacheExt.reviseTopActivity(task, snapshot);
            this.mAppIdMap.put(top, java.lang.Integer.valueOf(task.mTaskId));
            this.mRunningCache.put(java.lang.Integer.valueOf(task.mTaskId), new com.android.server.wm.SnapshotCache.CacheEntry(snapshot, top));
            android.util.Slog.d("TaskSnapshotCache", "putSnapshot mRunningCache top= " + top + ", mTaskId=" + task.mTaskId + ", snapshot=" + snapshot + ", mAppIdMap size= " + this.mAppIdMap.size());
            if (this.mAppIdMap.size() > 1000) {
                this.mAppIdMap.clear();
                this.mRunningCache.clear();
            }
        }
    }

    android.window.TaskSnapshot getSnapshot(int taskId, int userId, boolean restoreFromDisk, boolean isLowResolution) {
        android.window.TaskSnapshot snapshot = getSnapshot(java.lang.Integer.valueOf(taskId));
        if (snapshot != null) {
            return snapshot;
        }
        if (!restoreFromDisk) {
            return null;
        }
        return tryRestoreFromDisk(taskId, userId, isLowResolution);
    }

    private android.window.TaskSnapshot tryRestoreFromDisk(int taskId, int userId, boolean isLowResolution) {
        return this.mLoader.loadTask(taskId, userId, isLowResolution);
    }
}
