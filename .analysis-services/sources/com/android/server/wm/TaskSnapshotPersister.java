package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskSnapshotPersister extends com.android.server.wm.BaseAppSnapshotPersister {
    private final android.util.ArraySet<java.lang.Integer> mPersistedTaskIdsSinceLastRemoveObsolete;

    TaskSnapshotPersister(com.android.server.wm.SnapshotPersistQueue persistQueue, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
        super(persistQueue, persistInfoProvider);
        this.mPersistedTaskIdsSinceLastRemoveObsolete = new android.util.ArraySet<>();
    }

    @Override // com.android.server.wm.BaseAppSnapshotPersister
    void persistSnapshot(int taskId, int userId, android.window.TaskSnapshot snapshot) {
        synchronized (this.mLock) {
            this.mPersistedTaskIdsSinceLastRemoveObsolete.add(java.lang.Integer.valueOf(taskId));
            super.persistSnapshot(taskId, userId, snapshot);
        }
    }

    @Override // com.android.server.wm.BaseAppSnapshotPersister
    void removeSnapshot(int taskId, int userId) {
        synchronized (this.mLock) {
            this.mPersistedTaskIdsSinceLastRemoveObsolete.remove(java.lang.Integer.valueOf(taskId));
            super.removeSnapshot(taskId, userId);
        }
    }

    void removeObsoleteFiles(android.util.ArraySet<java.lang.Integer> persistentTaskIds, int[] runningUserIds) {
        if (runningUserIds.length == 0) {
            return;
        }
        synchronized (this.mLock) {
            this.mPersistedTaskIdsSinceLastRemoveObsolete.clear();
            this.mSnapshotPersistQueue.sendToQueueLocked(new com.android.server.wm.TaskSnapshotPersister.RemoveObsoleteFilesQueueItem(persistentTaskIds, runningUserIds, this.mPersistInfoProvider));
        }
    }

    class RemoveObsoleteFilesQueueItem extends com.android.server.wm.SnapshotPersistQueue.WriteQueueItem {
        private final android.util.ArraySet<java.lang.Integer> mPersistentTaskIds;
        private final int[] mRunningUserIds;

        RemoveObsoleteFilesQueueItem(android.util.ArraySet<java.lang.Integer> persistentTaskIds, int[] runningUserIds, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider provider) {
            super(provider, runningUserIds.length > 0 ? runningUserIds[0] : 0);
            this.mPersistentTaskIds = new android.util.ArraySet<>((android.util.ArraySet) persistentTaskIds);
            this.mRunningUserIds = java.util.Arrays.copyOf(runningUserIds, runningUserIds.length);
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        boolean isReady(com.android.server.pm.UserManagerInternal userManagerInternal) {
            for (int i = this.mRunningUserIds.length - 1; i >= 0; i--) {
                if (!userManagerInternal.isUserUnlocked(this.mRunningUserIds[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void write() {
            android.util.ArraySet<java.lang.Integer> newPersistedTaskIds;
            android.os.Trace.traceBegin(32L, "RemoveObsoleteFilesQueueItem");
            synchronized (com.android.server.wm.TaskSnapshotPersister.this.mLock) {
                newPersistedTaskIds = new android.util.ArraySet<>((android.util.ArraySet<java.lang.Integer>) com.android.server.wm.TaskSnapshotPersister.this.mPersistedTaskIdsSinceLastRemoveObsolete);
            }
            for (int userId : this.mRunningUserIds) {
                java.io.File dir = this.mPersistInfoProvider.getDirectory(userId);
                java.lang.String[] files = dir.list();
                if (files != null) {
                    for (java.lang.String file : files) {
                        int taskId = getTaskId(file);
                        if (!this.mPersistentTaskIds.contains(java.lang.Integer.valueOf(taskId)) && !newPersistedTaskIds.contains(java.lang.Integer.valueOf(taskId))) {
                            new java.io.File(dir, file).delete();
                        }
                    }
                }
            }
            android.os.Trace.traceEnd(32L);
        }

        int getTaskId(java.lang.String fileName) {
            if (!this.mPersistInfoProvider.getForceReduceSnapshot()) {
                if (!fileName.endsWith(".proto") && !fileName.endsWith(".jpg")) {
                    return -1;
                }
            } else if (!fileName.endsWith(".proto") && !fileName.endsWith(".jpg") && !fileName.endsWith(".webp")) {
                return -1;
            }
            int end = fileName.lastIndexOf(46);
            if (end == -1) {
                return -1;
            }
            java.lang.String name = fileName.substring(0, end);
            if (name.endsWith("_reduced")) {
                name = name.substring(0, name.length() - "_reduced".length());
            }
            try {
                return java.lang.Integer.parseInt(name);
            } catch (java.lang.NumberFormatException e) {
                return -1;
            }
        }
    }
}
