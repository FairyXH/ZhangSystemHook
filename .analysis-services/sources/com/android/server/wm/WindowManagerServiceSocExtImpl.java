package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowManagerServiceSocExtImpl implements com.android.server.wm.IWindowManagerServiceSocExt {
    private android.util.BoostFramework mPerf = null;
    com.android.server.wm.WindowManagerService mWindowManagerService;

    public WindowManagerServiceSocExtImpl(java.lang.Object obj) {
        this.mWindowManagerService = (com.android.server.wm.WindowManagerService) obj;
    }

    @Override // com.android.server.wm.IWindowManagerServiceSocExt
    public void hookStartFreezingDisplay() {
        if (this.mPerf == null) {
            this.mPerf = new android.util.BoostFramework();
        }
        if (this.mPerf != null) {
            this.mPerf.perfHint(4233, (java.lang.String) null);
        }
    }

    @Override // com.android.server.wm.IWindowManagerServiceSocExt
    public void hookStopFreezingDisplayLocked() {
        if (this.mPerf != null) {
            this.mPerf.perfLockRelease();
        }
    }
}
