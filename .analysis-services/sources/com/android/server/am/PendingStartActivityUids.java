package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class PendingStartActivityUids {
    public static final long INVALID_TIME = 0;
    static final java.lang.String TAG = "ActivityManager";
    private final android.util.SparseArray<android.util.Pair<java.lang.Integer, java.lang.Long>> mPendingUids = new android.util.SparseArray<>();

    PendingStartActivityUids() {
    }

    synchronized boolean add(int uid, int pid) {
        if (this.mPendingUids.get(uid) != null) {
            return false;
        }
        this.mPendingUids.put(uid, new android.util.Pair<>(java.lang.Integer.valueOf(pid), java.lang.Long.valueOf(android.os.SystemClock.elapsedRealtime())));
        return true;
    }

    synchronized void delete(int uid, long nowElapsed) {
        android.util.Pair<java.lang.Integer, java.lang.Long> pendingPid = this.mPendingUids.get(uid);
        if (pendingPid != null) {
            if (nowElapsed < ((java.lang.Long) pendingPid.second).longValue()) {
                android.util.Slog.i("ActivityManager", "updateOomAdj start time is before than pendingPid added, don't delete it");
                return;
            }
            long delay = android.os.SystemClock.elapsedRealtime() - ((java.lang.Long) pendingPid.second).longValue();
            if (delay >= 1000) {
                android.util.Slog.i("ActivityManager", "PendingStartActivityUids startActivity to updateOomAdj delay:" + delay + "ms, uid:" + uid);
            }
            this.mPendingUids.delete(uid);
        }
    }

    synchronized long getPendingTopPidTime(int uid, int pid) {
        long ret;
        ret = 0;
        android.util.Pair<java.lang.Integer, java.lang.Long> pendingPid = this.mPendingUids.get(uid);
        if (pendingPid != null && ((java.lang.Integer) pendingPid.first).intValue() == pid) {
            ret = ((java.lang.Long) pendingPid.second).longValue();
        }
        return ret;
    }

    synchronized boolean isPendingTopUid(int uid) {
        return this.mPendingUids.get(uid) != null;
    }

    synchronized void enqueuePendingTopAppIfNecessaryLocked(com.android.server.am.ActivityManagerService ams) {
        com.android.server.am.ProcessRecord app;
        int size = this.mPendingUids.size();
        for (int i = 0; i < size; i++) {
            android.util.Pair<java.lang.Integer, java.lang.Long> p = this.mPendingUids.valueAt(i);
            synchronized (ams.mPidsSelfLocked) {
                app = ams.mPidsSelfLocked.get(((java.lang.Integer) p.first).intValue());
            }
            if (app != null) {
                ams.lambda$appDiedLocked$2(app);
            }
        }
    }

    synchronized void clear() {
        this.mPendingUids.clear();
    }
}
