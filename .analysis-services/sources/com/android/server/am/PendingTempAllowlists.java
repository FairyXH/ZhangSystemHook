package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class PendingTempAllowlists {
    private final android.util.SparseArray<com.android.server.am.ActivityManagerService.PendingTempAllowlist> mPendingTempAllowlist = new android.util.SparseArray<>();
    private com.android.server.am.ActivityManagerService mService;

    PendingTempAllowlists(com.android.server.am.ActivityManagerService service) {
        this.mService = service;
    }

    void put(int uid, com.android.server.am.ActivityManagerService.PendingTempAllowlist value) {
        synchronized (this.mPendingTempAllowlist) {
            this.mPendingTempAllowlist.put(uid, value);
        }
    }

    void removeAt(int index) {
        synchronized (this.mPendingTempAllowlist) {
            this.mPendingTempAllowlist.removeAt(index);
        }
    }

    com.android.server.am.ActivityManagerService.PendingTempAllowlist get(int uid) {
        com.android.server.am.ActivityManagerService.PendingTempAllowlist pendingTempAllowlist;
        synchronized (this.mPendingTempAllowlist) {
            pendingTempAllowlist = this.mPendingTempAllowlist.get(uid);
        }
        return pendingTempAllowlist;
    }

    int size() {
        int size;
        synchronized (this.mPendingTempAllowlist) {
            size = this.mPendingTempAllowlist.size();
        }
        return size;
    }

    com.android.server.am.ActivityManagerService.PendingTempAllowlist valueAt(int index) {
        com.android.server.am.ActivityManagerService.PendingTempAllowlist pendingTempAllowlistValueAt;
        synchronized (this.mPendingTempAllowlist) {
            pendingTempAllowlistValueAt = this.mPendingTempAllowlist.valueAt(index);
        }
        return pendingTempAllowlistValueAt;
    }

    int indexOfKey(int key) {
        int iIndexOfKey;
        synchronized (this.mPendingTempAllowlist) {
            iIndexOfKey = this.mPendingTempAllowlist.indexOfKey(key);
        }
        return iIndexOfKey;
    }
}
