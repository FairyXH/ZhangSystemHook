package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ProcessProviderRecord {
    final com.android.server.am.ProcessRecord mApp;
    private final com.android.server.am.ActivityManagerService mService;
    public com.android.server.am.IProcessProviderRecordExt mProcessProviderRecordExt = (com.android.server.am.IProcessProviderRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IProcessProviderRecordExt.class).create();
    private long mLastProviderTime = Long.MIN_VALUE;
    private final android.util.ArrayMap<java.lang.String, com.android.server.am.ContentProviderRecord> mPubProviders = new android.util.ArrayMap<>();
    private final java.util.ArrayList<com.android.server.am.ContentProviderConnection> mConProviders = new java.util.ArrayList<>();

    long getLastProviderTime() {
        return this.mLastProviderTime;
    }

    void setLastProviderTime(long lastProviderTime) {
        this.mLastProviderTime = lastProviderTime;
    }

    boolean hasProvider(java.lang.String name) {
        return this.mPubProviders.containsKey(name);
    }

    com.android.server.am.ContentProviderRecord getProvider(java.lang.String name) {
        return this.mPubProviders.get(name);
    }

    int numberOfProviders() {
        return this.mPubProviders.size();
    }

    com.android.server.am.ContentProviderRecord getProviderAt(int index) {
        return this.mPubProviders.valueAt(index);
    }

    void installProvider(java.lang.String name, com.android.server.am.ContentProviderRecord provider) {
        this.mPubProviders.put(name, provider);
    }

    void removeProvider(java.lang.String name) {
        this.mPubProviders.remove(name);
    }

    void ensureProviderCapacity(int capacity) {
        this.mPubProviders.ensureCapacity(capacity);
    }

    int numberOfProviderConnections() {
        return this.mConProviders.size();
    }

    com.android.server.am.ContentProviderConnection getProviderConnectionAt(int index) {
        return this.mConProviders.get(index);
    }

    void addProviderConnection(com.android.server.am.ContentProviderConnection connection) {
        this.mConProviders.add(connection);
    }

    boolean removeProviderConnection(com.android.server.am.ContentProviderConnection connection) {
        return this.mConProviders.remove(connection);
    }

    ProcessProviderRecord(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mService = app.mService;
    }

    boolean onCleanupApplicationRecordLocked(boolean allowRestart) {
        boolean restart = false;
        for (int i = this.mPubProviders.size() - 1; i >= 0; i--) {
            com.android.server.am.ContentProviderRecord cpr = this.mPubProviders.valueAt(i);
            if (cpr.proc == this.mApp) {
                boolean alwaysRemove = this.mApp.mErrorState.isBad() || !allowRestart;
                boolean inLaunching = this.mService.mCpHelper.removeDyingProviderLocked(this.mApp, cpr, alwaysRemove);
                if (!alwaysRemove && inLaunching && cpr.hasConnectionOrHandle()) {
                    restart = true;
                }
                cpr.provider = null;
                cpr.setProcess(null);
            }
        }
        this.mPubProviders.clear();
        if (this.mService.mCpHelper.cleanupAppInLaunchingProvidersLocked(this.mApp, this.mProcessProviderRecordExt.checkIfAlwaysCleanupAppInLaunchingProviders(this.mService.mContext, this.mApp, allowRestart))) {
            this.mService.mProcessList.noteProcessDiedLocked(this.mApp);
            restart = true;
        }
        if (!this.mConProviders.isEmpty()) {
            for (int i2 = this.mConProviders.size() - 1; i2 >= 0; i2--) {
                com.android.server.am.ContentProviderConnection conn = this.mConProviders.get(i2);
                conn.provider.connections.remove(conn);
                this.mService.stopAssociationLocked(this.mApp.uid, this.mApp.processName, conn.provider.uid, conn.provider.appInfo.longVersionCode, conn.provider.name, conn.provider.info.processName);
            }
            this.mConProviders.clear();
        }
        return restart;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
        if (this.mLastProviderTime > 0) {
            pw.print(prefix);
            pw.print("lastProviderTime=");
            android.util.TimeUtils.formatDuration(this.mLastProviderTime, nowUptime, pw);
            pw.println();
        }
        if (this.mPubProviders.size() > 0) {
            pw.print(prefix);
            pw.println("Published Providers:");
            int size = this.mPubProviders.size();
            for (int i = 0; i < size; i++) {
                pw.print(prefix);
                pw.print("  - ");
                pw.println(this.mPubProviders.keyAt(i));
                pw.print(prefix);
                pw.print("    -> ");
                pw.println(this.mPubProviders.valueAt(i));
            }
        }
        if (this.mConProviders.size() > 0) {
            pw.print(prefix);
            pw.println("Connected Providers:");
            int size2 = this.mConProviders.size();
            for (int i2 = 0; i2 < size2; i2++) {
                pw.print(prefix);
                pw.print("  - ");
                pw.println(this.mConProviders.get(i2).toShortString());
            }
        }
    }
}
