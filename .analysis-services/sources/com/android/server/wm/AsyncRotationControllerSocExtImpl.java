package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class AsyncRotationControllerSocExtImpl implements com.android.server.wm.IAsyncRotationControllerSocExt {
    private android.util.BoostFramework mPerf = null;
    private boolean mIsLatencyPerfLockAcquired = false;

    public AsyncRotationControllerSocExtImpl(java.lang.Object controller) {
    }

    @Override // com.android.server.wm.IAsyncRotationControllerSocExt
    public void hookInitPerf() {
        if (this.mPerf == null) {
            this.mPerf = new android.util.BoostFramework();
        }
    }

    @Override // com.android.server.wm.IAsyncRotationControllerSocExt
    public void hookPerfLockRelease() {
        if (this.mPerf != null && this.mIsLatencyPerfLockAcquired) {
            this.mPerf.perfLockRelease();
            this.mIsLatencyPerfLockAcquired = false;
        }
    }

    @Override // com.android.server.wm.IAsyncRotationControllerSocExt
    public void hookPerfHint() {
        if (this.mPerf != null) {
            this.mPerf.perfHint(4233, (java.lang.String) null);
            this.mIsLatencyPerfLockAcquired = true;
        }
    }
}
