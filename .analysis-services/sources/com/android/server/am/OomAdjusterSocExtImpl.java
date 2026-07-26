package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class OomAdjusterSocExtImpl implements com.android.server.am.IOomAdjusterSocExt {
    private static final java.lang.String TAG = "OomAdjusterSocExtImpl";
    boolean mEnableBgt = false;
    com.android.server.am.OomAdjuster mOomAdjuster;
    public static android.util.BoostFramework mPerf = new android.util.BoostFramework();
    public static android.util.BoostFramework mPerfBoost = new android.util.BoostFramework();
    public static int mPerfHandle = -1;
    public static int mCurRenderThreadTid = -1;
    public static boolean mIsTopAppRenderThreadBoostEnabled = false;

    public OomAdjusterSocExtImpl(java.lang.Object oomAdjuster) {
        this.mOomAdjuster = (com.android.server.am.OomAdjuster) oomAdjuster;
    }

    @Override // com.android.server.am.IOomAdjusterSocExt
    public void initPerfConfig() {
        if (mPerf != null) {
            mIsTopAppRenderThreadBoostEnabled = java.lang.Boolean.parseBoolean(mPerf.perfGetProp("vendor.perf.topAppRenderThreadBoost.enable", "false"));
            this.mEnableBgt = java.lang.Boolean.parseBoolean(mPerf.perfGetProp("vendor.perf.bgt.enable", "false"));
        }
    }

    @Override // com.android.server.am.IOomAdjusterSocExt
    public void topAppRenderThreadBoost(com.android.server.am.ProcessRecord app) {
        if (mIsTopAppRenderThreadBoostEnabled && mCurRenderThreadTid != app.getRenderThreadTid() && app.getRenderThreadTid() > 0) {
            mCurRenderThreadTid = app.getRenderThreadTid();
            if (mPerfBoost != null) {
                android.util.Slog.d(TAG, "TOP-APP: pid:" + app.getPid() + ", processName: " + app.processName + ", renderThreadTid: " + app.getRenderThreadTid());
                if (mPerfHandle >= 0) {
                    mPerfBoost.perfLockRelease();
                    mPerfHandle = -1;
                }
                mPerfHandle = mPerfBoost.perfHint(4246, app.processName, app.getRenderThreadTid(), 1);
                android.util.Slog.d(TAG, "VENDOR_HINT_BOOST_RENDERTHREAD perfHint was called. mPerfHandle: " + mPerfHandle);
            }
        }
    }

    @Override // com.android.server.am.IOomAdjusterSocExt
    public void backgroundAppsTransition(com.android.server.am.ProcessRecord app, com.android.server.am.ProcessStateRecord state) {
        if (this.mEnableBgt) {
            if (state.getSetAdj() >= 900 && state.getSetAdj() <= 999 && state.getCurAdj() == 0 && state.hasForegroundActivities()) {
                android.util.Slog.d(TAG, "App adj change from cached state to fg state : " + app.getPid() + " " + app.processName);
                if (mPerf != null) {
                    int[] fgAppPerfLockArgs = {1115815936, app.getPid()};
                    mPerf.perfLockAcquire(10, fgAppPerfLockArgs);
                }
            }
            if (state.getSetAdj() == 700 && state.getCurAdj() >= 900 && state.getCurAdj() <= 999 && app.hasActivities()) {
                android.util.Slog.d(TAG, "App adj change from previous state to cached state : " + app.getPid() + " " + app.processName);
                if (mPerf != null) {
                    int[] bgAppPerfLockArgs = {1115832320, app.getPid()};
                    mPerf.perfLockAcquire(10, bgAppPerfLockArgs);
                }
            }
        }
    }
}
