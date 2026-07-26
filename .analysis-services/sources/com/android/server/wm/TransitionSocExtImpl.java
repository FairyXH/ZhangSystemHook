package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class TransitionSocExtImpl implements com.android.server.wm.ITransitionSocExt {
    private android.util.BoostFramework mPerf = null;
    private boolean mIsLatencyPerfLockAcquired = false;

    public TransitionSocExtImpl(java.lang.Object controller) {
    }

    @Override // com.android.server.wm.ITransitionSocExt
    public void hookInitPerf() {
        if (this.mPerf == null) {
            this.mPerf = new android.util.BoostFramework();
        }
    }

    @Override // com.android.server.wm.ITransitionSocExt
    public void hookPerfLockRelease() {
        if (this.mPerf != null && this.mIsLatencyPerfLockAcquired) {
            this.mPerf.perfLockRelease();
            this.mIsLatencyPerfLockAcquired = false;
        }
    }

    @Override // com.android.server.wm.ITransitionSocExt
    public void hookPerfHint(int type) {
        if (this.mPerf != null && type == 6) {
            this.mPerf.perfHint(4233, (java.lang.String) null);
            this.mIsLatencyPerfLockAcquired = true;
        }
    }
}
