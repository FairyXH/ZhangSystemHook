package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ProcessReceiverRecord {
    final com.android.server.am.ProcessRecord mApp;
    private int mCurReceiversSize;
    private final com.android.server.am.ActivityManagerService mService;
    private final android.util.ArraySet<com.android.server.am.BroadcastRecord> mCurReceivers = new android.util.ArraySet<>();
    private final android.util.ArraySet<com.android.server.am.ReceiverList> mReceivers = new android.util.ArraySet<>();

    int numberOfCurReceivers() {
        return this.mCurReceiversSize;
    }

    void incrementCurReceivers() {
        this.mCurReceiversSize++;
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 1);
    }

    void decrementCurReceivers() {
        this.mCurReceiversSize--;
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 2);
    }

    @java.lang.Deprecated
    com.android.server.am.BroadcastRecord getCurReceiverAt(int index) {
        return this.mCurReceivers.valueAt(index);
    }

    @java.lang.Deprecated
    boolean hasCurReceiver(com.android.server.am.BroadcastRecord receiver) {
        return this.mCurReceivers.contains(receiver);
    }

    @java.lang.Deprecated
    void addCurReceiver(com.android.server.am.BroadcastRecord receiver) {
        this.mCurReceivers.add(receiver);
        this.mCurReceiversSize = this.mCurReceivers.size();
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 1);
    }

    @java.lang.Deprecated
    void removeCurReceiver(com.android.server.am.BroadcastRecord receiver) {
        this.mCurReceivers.remove(receiver);
        this.mCurReceiversSize = this.mCurReceivers.size();
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 2);
    }

    int numberOfReceivers() {
        return this.mReceivers.size();
    }

    void addReceiver(com.android.server.am.ReceiverList receiver) {
        this.mReceivers.add(receiver);
    }

    void removeReceiver(com.android.server.am.ReceiverList receiver) {
        this.mReceivers.remove(receiver);
    }

    ProcessReceiverRecord(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mService = app.mService;
    }

    void onCleanupApplicationRecordLocked() {
        for (int i = this.mReceivers.size() - 1; i >= 0; i--) {
            this.mService.removeReceiverLocked(this.mReceivers.valueAt(i));
        }
        this.mReceivers.clear();
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
        if (!this.mCurReceivers.isEmpty()) {
            pw.print(prefix);
            pw.println("Current mReceivers:");
            int size = this.mCurReceivers.size();
            for (int i = 0; i < size; i++) {
                pw.print(prefix);
                pw.print("  - ");
                pw.println(this.mCurReceivers.valueAt(i));
            }
        }
        if (this.mReceivers.size() > 0) {
            pw.print(prefix);
            pw.println("mReceivers:");
            int size2 = this.mReceivers.size();
            for (int i2 = 0; i2 < size2; i2++) {
                pw.print(prefix);
                pw.print("  - ");
                pw.println(this.mReceivers.valueAt(i2));
            }
        }
    }
}
