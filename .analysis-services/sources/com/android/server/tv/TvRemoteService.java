package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
public class TvRemoteService extends com.android.server.SystemService implements com.android.server.Watchdog.Monitor {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "TvRemoteService";
    private final java.lang.Object mLock;
    private final com.android.server.tv.TvRemoteProviderWatcher mWatcher;

    public TvRemoteService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mWatcher = new com.android.server.tv.TvRemoteProviderWatcher(context, this.mLock);
        com.android.server.Watchdog.getInstance().addMonitor(this);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 600) {
            this.mWatcher.start();
        }
    }
}
