package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ScreenRotationAnimationSocExtImpl implements com.android.server.wm.IScreenRotationAnimationSocExt {
    com.android.server.wm.ScreenRotationAnimation mAnimation;
    private android.util.BoostFramework mPerf = null;
    private boolean mIsPerfLockAcquired = false;

    public ScreenRotationAnimationSocExtImpl(java.lang.Object animation) {
        this.mAnimation = (com.android.server.wm.ScreenRotationAnimation) animation;
    }

    @Override // com.android.server.wm.IScreenRotationAnimationSocExt
    public void init() {
        this.mPerf = new android.util.BoostFramework();
    }

    @Override // com.android.server.wm.IScreenRotationAnimationSocExt
    public void hookPerfLockAcquired() {
        if (this.mPerf != null && !this.mIsPerfLockAcquired) {
            this.mPerf.perfHint(4240, (java.lang.String) null);
            this.mIsPerfLockAcquired = true;
        }
    }

    @Override // com.android.server.wm.IScreenRotationAnimationSocExt
    public void hookPerfLockRelease() {
        if (this.mPerf != null && this.mIsPerfLockAcquired) {
            this.mPerf.perfLockRelease();
            this.mIsPerfLockAcquired = false;
        }
    }
}
