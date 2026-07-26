package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
abstract class SnapshotCache<TYPE extends com.android.server.wm.WindowContainer> {
    private static final java.lang.String TAG = "SnapshotCache";
    protected final java.lang.String mName;
    protected final java.lang.Object mLock = new java.lang.Object();
    protected final android.util.ArrayMap<com.android.server.wm.ActivityRecord, java.lang.Integer> mAppIdMap = new android.util.ArrayMap<>();
    protected final android.util.ArrayMap<java.lang.Integer, com.android.server.wm.SnapshotCache.CacheEntry> mRunningCache = new android.util.ArrayMap<>();

    abstract void putSnapshot(TYPE type, android.window.TaskSnapshot taskSnapshot);

    SnapshotCache(java.lang.String name) {
        this.mName = name;
    }

    void clearRunningCache() {
        synchronized (this.mLock) {
            android.util.Slog.d(TAG, "clearRunningCache, current size= " + this.mRunningCache.size() + ", callers=" + android.os.Debug.getCallers(5));
            this.mRunningCache.clear();
        }
    }

    final android.window.TaskSnapshot getSnapshot(java.lang.Integer id) {
        synchronized (this.mLock) {
            com.android.server.wm.SnapshotCache.CacheEntry entry = this.mRunningCache.get(id);
            if (entry != null) {
                return entry.snapshot;
            }
            return null;
        }
    }

    void onAppRemoved(com.android.server.wm.ActivityRecord activity) {
        synchronized (this.mLock) {
            java.lang.Integer id = this.mAppIdMap.get(activity);
            if (id != null) {
                removeRunningEntry(id);
            }
        }
    }

    void onAppDied(com.android.server.wm.ActivityRecord activity) {
        synchronized (this.mLock) {
            java.lang.Integer id = this.mAppIdMap.get(activity);
            if (id != null) {
                removeRunningEntry(id);
            }
        }
    }

    void onIdRemoved(java.lang.Integer index) {
        removeRunningEntry(index);
    }

    void removeRunningEntry(java.lang.Integer id) {
        synchronized (this.mLock) {
            com.android.server.wm.SnapshotCache.CacheEntry entry = this.mRunningCache.get(id);
            if (entry != null) {
                android.util.Slog.d(TAG, "removeRunningEntry id: " + id + ", callers=" + android.os.Debug.getCallers(3));
                this.mAppIdMap.remove(entry.topApp);
                this.mRunningCache.remove(id);
                entry.snapshot.removeReference(2);
            }
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        java.lang.String doublePrefix = prefix + "  ";
        java.lang.String triplePrefix = doublePrefix + "  ";
        pw.println(prefix + "SnapshotCache " + this.mName);
        synchronized (this.mLock) {
            for (int i = this.mRunningCache.size() - 1; i >= 0; i--) {
                com.android.server.wm.SnapshotCache.CacheEntry entry = this.mRunningCache.valueAt(i);
                pw.println(doublePrefix + "Entry token=" + this.mRunningCache.keyAt(i));
                pw.println(triplePrefix + "topApp=" + entry.topApp);
                pw.println(triplePrefix + "snapshot=" + entry.snapshot);
            }
        }
    }

    static final class CacheEntry {
        final android.window.TaskSnapshot snapshot;
        final com.android.server.wm.ActivityRecord topApp;

        CacheEntry(android.window.TaskSnapshot snapshot, com.android.server.wm.ActivityRecord topApp) {
            this.snapshot = snapshot;
            this.topApp = topApp;
        }
    }
}
