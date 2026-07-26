package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class MirrorActiveUids {
    private final android.util.SparseIntArray mUidStates = new android.util.SparseIntArray();
    private final android.util.SparseIntArray mNumNonAppVisibleWindowMap = new android.util.SparseIntArray();

    MirrorActiveUids() {
    }

    synchronized void onUidActive(int uid, int procState) {
        this.mUidStates.put(uid, procState);
    }

    synchronized void onUidInactive(int uid) {
        this.mUidStates.delete(uid);
    }

    synchronized void onUidProcStateChanged(int uid, int procState) {
        int index = this.mUidStates.indexOfKey(uid);
        if (index >= 0) {
            this.mUidStates.setValueAt(index, procState);
        }
    }

    synchronized int getUidState(int uid) {
        return this.mUidStates.get(uid, 20);
    }

    synchronized void onNonAppSurfaceVisibilityChanged(int uid, boolean visible) {
        int index = this.mNumNonAppVisibleWindowMap.indexOfKey(uid);
        int i = 1;
        if (index >= 0) {
            int iValueAt = this.mNumNonAppVisibleWindowMap.valueAt(index);
            if (!visible) {
                i = -1;
            }
            int num = iValueAt + i;
            if (num > 0) {
                this.mNumNonAppVisibleWindowMap.setValueAt(index, num);
            } else {
                this.mNumNonAppVisibleWindowMap.removeAt(index);
            }
        } else if (visible) {
            this.mNumNonAppVisibleWindowMap.append(uid, 1);
        }
    }

    synchronized boolean hasNonAppVisibleWindow(int uid) {
        return this.mNumNonAppVisibleWindowMap.get(uid) > 0;
    }

    synchronized void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix + "NumNonAppVisibleWindowUidMap:[");
        for (int i = this.mNumNonAppVisibleWindowMap.size() - 1; i >= 0; i--) {
            pw.print(" " + this.mNumNonAppVisibleWindowMap.keyAt(i) + ":" + this.mNumNonAppVisibleWindowMap.valueAt(i));
        }
        pw.println("]");
    }
}
