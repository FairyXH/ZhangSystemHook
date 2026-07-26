package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
interface AppOpsUidStateTracker {

    public interface UidStateChangedCallback {
        void onUidStateChanged(int i, int i2, boolean z);
    }

    void addUidStateChangedCallback(java.util.concurrent.Executor executor, com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback uidStateChangedCallback);

    void dumpEvents(java.io.PrintWriter printWriter);

    void dumpUidState(java.io.PrintWriter printWriter, int i, long j);

    int evalMode(int i, int i2, int i3);

    int getUidState(int i);

    boolean isUidInForeground(int i);

    void removeUidStateChangedCallback(com.android.server.appop.AppOpsUidStateTracker.UidStateChangedCallback uidStateChangedCallback);

    void updateAppWidgetVisibility(android.util.SparseArray<java.lang.String> sparseArray, boolean z);

    void updateUidProcState(int i, int i2, int i3);

    static int processStateToUidState(int procState) {
        if (procState == -1) {
            return com.android.server.am.ProcessList.PREVIOUS_APP_ADJ;
        }
        if (procState <= 1) {
            return 100;
        }
        if (procState <= 2) {
            return 200;
        }
        if (procState <= 3) {
            return 500;
        }
        if (procState <= 4) {
            return 400;
        }
        if (procState <= 5) {
            return 500;
        }
        if (procState <= 11) {
            return 600;
        }
        return com.android.server.am.ProcessList.PREVIOUS_APP_ADJ;
    }
}
