package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsUidResolver {
    private static final java.lang.String TAG = "PowerStatsUidResolver";
    private final android.util.SparseIntArray mIsolatedUids = new android.util.SparseIntArray();
    private final android.util.SparseIntArray mIsolatedUidRefCounts = new android.util.SparseIntArray();
    private volatile java.util.List<com.android.server.power.stats.PowerStatsUidResolver.Listener> mListeners = java.util.Collections.emptyList();

    public interface Listener {
        void onAfterIsolatedUidRemoved(int i, int i2);

        void onBeforeIsolatedUidRemoved(int i, int i2);

        void onIsolatedUidAdded(int i, int i2);
    }

    public void addListener(com.android.server.power.stats.PowerStatsUidResolver.Listener listener) {
        synchronized (this) {
            java.util.List<com.android.server.power.stats.PowerStatsUidResolver.Listener> newList = new java.util.ArrayList<>(this.mListeners);
            newList.add(listener);
            this.mListeners = java.util.Collections.unmodifiableList(newList);
        }
    }

    public void removeListener(com.android.server.power.stats.PowerStatsUidResolver.Listener listener) {
        synchronized (this) {
            java.util.List<com.android.server.power.stats.PowerStatsUidResolver.Listener> newList = new java.util.ArrayList<>(this.mListeners);
            newList.remove(listener);
            this.mListeners = java.util.Collections.unmodifiableList(newList);
        }
    }

    public void noteIsolatedUidAdded(int isolatedUid, int parentUid) {
        synchronized (this) {
            this.mIsolatedUids.put(isolatedUid, parentUid);
            this.mIsolatedUidRefCounts.put(isolatedUid, 1);
        }
        java.util.List<com.android.server.power.stats.PowerStatsUidResolver.Listener> listeners = this.mListeners;
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.get(i).onIsolatedUidAdded(isolatedUid, parentUid);
        }
    }

    public void noteIsolatedUidRemoved(int isolatedUid, int parentUid) {
        synchronized (this) {
            int curUid = this.mIsolatedUids.get(isolatedUid, -1);
            if (curUid != parentUid) {
                android.util.Slog.wtf(TAG, "Attempt to remove an isolated UID " + isolatedUid + " with the parent UID " + parentUid + ". The registered parent UID is " + curUid);
                return;
            }
            java.util.List<com.android.server.power.stats.PowerStatsUidResolver.Listener> listeners = this.mListeners;
            for (int i = listeners.size() - 1; i >= 0; i--) {
                listeners.get(i).onBeforeIsolatedUidRemoved(isolatedUid, parentUid);
            }
            releaseIsolatedUid(isolatedUid);
        }
    }

    public void retainIsolatedUid(int uid) {
        synchronized (this) {
            int refCount = this.mIsolatedUidRefCounts.get(uid, 0);
            if (refCount <= 0) {
                android.util.Slog.w(TAG, "Attempted to increment ref counted of untracked isolated uid (" + uid + ")");
            } else {
                this.mIsolatedUidRefCounts.put(uid, refCount + 1);
            }
        }
    }

    public void releaseIsolatedUid(int isolatedUid) {
        synchronized (this) {
            int refCount = this.mIsolatedUidRefCounts.get(isolatedUid, 0) - 1;
            if (refCount > 0) {
                this.mIsolatedUidRefCounts.put(isolatedUid, refCount);
                return;
            }
            int idx = this.mIsolatedUids.indexOfKey(isolatedUid);
            if (idx >= 0) {
                int parentUid = this.mIsolatedUids.valueAt(idx);
                this.mIsolatedUids.removeAt(idx);
                this.mIsolatedUidRefCounts.delete(isolatedUid);
                java.util.List<com.android.server.power.stats.PowerStatsUidResolver.Listener> listeners = this.mListeners;
                for (int i = listeners.size() - 1; i >= 0; i--) {
                    listeners.get(i).onAfterIsolatedUidRemoved(isolatedUid, parentUid);
                }
                return;
            }
            android.util.Slog.w(TAG, "Attempted to remove untracked child uid (" + isolatedUid + ")");
        }
    }

    public void releaseUidsInRange(int startUid, int endUid) {
        synchronized (this) {
            int startIndex = this.mIsolatedUids.indexOfKey(startUid);
            int endIndex = this.mIsolatedUids.indexOfKey(endUid);
            if (startIndex < 0) {
                startIndex = ~startIndex;
            }
            if (endIndex < 0) {
                endIndex = (~endIndex) - 1;
            }
            if (startIndex > endIndex) {
                return;
            }
            android.util.IntArray toRelease = new android.util.IntArray(endIndex - startIndex);
            for (int i = startIndex; i <= endIndex; i++) {
                toRelease.add(this.mIsolatedUids.keyAt(i));
            }
            for (int i2 = toRelease.size() - 1; i2 >= 0; i2--) {
                releaseIsolatedUid(toRelease.get(i2));
            }
        }
    }

    public int mapUid(int uid) {
        int i;
        synchronized (this) {
            i = this.mIsolatedUids.get(uid, uid);
        }
        return i;
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("Currently mapped isolated uids:");
        synchronized (this) {
            int numIsolatedUids = this.mIsolatedUids.size();
            for (int i = 0; i < numIsolatedUids; i++) {
                int isolatedUid = this.mIsolatedUids.keyAt(i);
                int ownerUid = this.mIsolatedUids.valueAt(i);
                int refs = this.mIsolatedUidRefCounts.get(isolatedUid);
                pw.println("  " + isolatedUid + "->" + ownerUid + " (ref count = " + refs + ")");
            }
        }
    }
}
