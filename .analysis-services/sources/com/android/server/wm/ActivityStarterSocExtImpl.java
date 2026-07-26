package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityStarterSocExtImpl implements com.android.server.wm.IActivityStarterSocExt {
    private static final java.lang.String TAG = "ActivityStarterSocExtImpl";
    com.android.server.wm.ActivityStarter mActivityStarter;
    public android.util.BoostFramework mPerf = null;

    public ActivityStarterSocExtImpl(java.lang.Object service) {
        this.mActivityStarter = (com.android.server.wm.ActivityStarter) service;
    }

    @Override // com.android.server.wm.IActivityStarterSocExt
    public void initSoc() {
        this.mPerf = new android.util.BoostFramework();
    }

    @Override // com.android.server.wm.IActivityStarterSocExt
    public void hookNewTask(java.lang.String packageName, com.android.server.wm.ActivityRecord mStartActivity) {
        if (this.mPerf != null) {
            if (this.mPerf.getPerfHalVersion() >= 2.299999952316284d) {
                int pkgType = this.mPerf.perfGetFeedback(5633, packageName);
                mStartActivity.mActivityRecordSocExt.setPerfActivityBoostHandler(this.mPerf.perfHintAcqRel(mStartActivity.mActivityRecordSocExt.getPerfActivityBoostHandler(), 4225, packageName, -1, 10, 1, new int[]{pkgType}));
            } else {
                if (mStartActivity.mActivityRecordSocExt.getPerfActivityBoostHandler() > 0) {
                    android.util.Slog.i(TAG, "Activity boosted, release it firstly");
                    this.mPerf.perfLockReleaseHandler(mStartActivity.mActivityRecordSocExt.getPerfActivityBoostHandler());
                }
                mStartActivity.mActivityRecordSocExt.setPerfActivityBoostHandler(this.mPerf.perfHint(4225, packageName, -1, 1));
            }
        }
    }
}
