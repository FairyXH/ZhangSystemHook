package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class WatchdogSocExtImpl implements com.android.server.IWatchdogSocExt {
    private static final java.lang.String TAG = "WatchdogSoxExtImpl";

    public WatchdogSocExtImpl(java.lang.Object watchdog) {
    }

    @Override // com.android.server.IWatchdogSocExt
    public void getExceptionLog() {
    }

    @Override // com.android.server.IWatchdogSocExt
    public void WDTMatterJava(long lParam) {
    }

    @Override // com.android.server.IWatchdogSocExt
    public void switchFtrace(int config) {
    }

    @Override // com.android.server.IWatchdogSocExt
    public long getSfHangTime() {
        return 0L;
    }

    @Override // com.android.server.IWatchdogSocExt
    public int getSfRebootTime() {
        return 0;
    }

    @Override // com.android.server.IWatchdogSocExt
    public void setSfRebootTime() {
    }
}
