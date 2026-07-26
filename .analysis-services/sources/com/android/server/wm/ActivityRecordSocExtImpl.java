package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityRecordSocExtImpl implements com.android.server.wm.IActivityRecordSocExt {
    private static final java.lang.String TAG = "ActivityRecordSocExtImpl";
    public boolean launching;
    com.android.server.wm.ActivityRecord mActivityRecord;
    public boolean translucentWindowLaunch;
    public final boolean ENABLE_BOOST_FRAMEWORK = true;
    public int perfActivityBoostHandler = -1;
    public android.util.BoostFramework mPerf = null;

    @Override // com.android.server.wm.IActivityRecordSocExt
    public boolean isEnableBoostFramework() {
        return true;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void initSoc() {
        this.launching = false;
        this.translucentWindowLaunch = false;
        if (this.mPerf == null) {
            this.mPerf = new android.util.BoostFramework();
        }
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public int isAppInfoGame(android.content.pm.ActivityInfo info) {
        if (info.applicationInfo == null) {
            return 0;
        }
        int isGame = (info.applicationInfo.category == 0 || (info.applicationInfo.flags & 33554432) == 33554432) ? 1 : 0;
        return isGame;
    }

    public android.util.BoostFramework getPerf() {
        return this.mPerf;
    }

    public void setPerf(android.util.BoostFramework mPerf) {
        this.mPerf = mPerf;
    }

    public ActivityRecordSocExtImpl(java.lang.Object service) {
        this.mActivityRecord = (com.android.server.wm.ActivityRecord) service;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void perfLockReleaseHandler() {
        if (this.mPerf != null && this.perfActivityBoostHandler > 0) {
            this.mPerf.perfLockReleaseHandler(this.perfActivityBoostHandler);
            this.perfActivityBoostHandler = -1;
        }
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void acquireActivityBoost(java.lang.String packageName, com.android.server.wm.WindowProcessController app, android.content.pm.ActivityInfo info, com.android.server.wm.ActivityTaskManagerService mAtmService, java.lang.String processName) {
        com.android.server.wm.WindowProcessController wpc;
        int pid;
        if (this.mPerf != null) {
            if (this.mPerf.getPerfHalVersion() < 2.299999952316284d) {
                if (this.perfActivityBoostHandler > 0) {
                    android.util.Slog.i(TAG, "Activity boosted, release it firstly");
                    this.mPerf.perfLockReleaseHandler(this.perfActivityBoostHandler);
                }
                this.perfActivityBoostHandler = this.mPerf.perfHint(4225, packageName, -1, 1);
                return;
            }
            int pkgType = this.mPerf.perfGetFeedback(5633, packageName);
            if (app == null && info != null && info.applicationInfo != null && mAtmService != null) {
                com.android.server.wm.WindowProcessController wpc2 = mAtmService.getProcessController(processName, info.applicationInfo.uid);
                wpc = wpc2;
            } else {
                wpc = app;
            }
            if (wpc != null && wpc.hasThread()) {
                int pid2 = wpc.getPid();
                pid = pid2;
            } else {
                pid = -1;
            }
            this.perfActivityBoostHandler = this.mPerf.perfHintAcqRel(this.perfActivityBoostHandler, 4225, packageName, -1, 10, 2, new int[]{pkgType, pid});
        }
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void releaseActivityBoost() {
        if (this.mPerf != null && this.perfActivityBoostHandler > 0) {
            this.mPerf.perfLockReleaseHandler(this.perfActivityBoostHandler);
            this.perfActivityBoostHandler = -1;
        } else if (this.perfActivityBoostHandler > 0) {
            android.util.Slog.w(TAG, "activity boost didn't release as expected");
        }
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public boolean isLaunching() {
        return this.launching;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void setLaunching(boolean launching) {
        this.launching = launching;
    }

    public boolean isTranslucentWindowLaunch() {
        return this.translucentWindowLaunch;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void setTranslucentWindowLaunch(boolean translucentWindowLaunch) {
        this.translucentWindowLaunch = translucentWindowLaunch;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public int getPerfActivityBoostHandler() {
        return this.perfActivityBoostHandler;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void setPerfActivityBoostHandler(int perfActivityBoostHandler) {
        this.perfActivityBoostHandler = perfActivityBoostHandler;
    }

    @Override // com.android.server.wm.IActivityRecordSocExt
    public void hookOnWindowsDrawn() {
        if (this.mPerf != null && this.perfActivityBoostHandler > 0) {
            this.mPerf.perfLockReleaseHandler(this.perfActivityBoostHandler);
            this.perfActivityBoostHandler = -1;
        } else if (this.perfActivityBoostHandler > 0) {
            android.util.Slog.w(TAG, "activity boost didn't release as expected");
        }
    }
}
