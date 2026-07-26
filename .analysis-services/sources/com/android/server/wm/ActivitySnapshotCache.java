package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivitySnapshotCache extends com.android.server.wm.SnapshotCache<com.android.server.wm.ActivityRecord> {
    ActivitySnapshotCache() {
        super("Activity");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.SnapshotCache
    public void putSnapshot(com.android.server.wm.ActivityRecord ar, android.window.TaskSnapshot snapshot) {
        int hasCode = java.lang.System.identityHashCode(ar);
        snapshot.addReference(2);
        synchronized (this.mLock) {
            com.android.server.wm.SnapshotCache.CacheEntry entry = this.mRunningCache.get(java.lang.Integer.valueOf(hasCode));
            if (entry != null) {
                this.mAppIdMap.remove(entry.topApp);
                entry.snapshot.removeReference(2);
            }
            this.mAppIdMap.put(ar, java.lang.Integer.valueOf(hasCode));
            this.mRunningCache.put(java.lang.Integer.valueOf(hasCode), new com.android.server.wm.SnapshotCache.CacheEntry(snapshot, ar));
        }
    }
}
