package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class RootWindowContainerSocExtImpl implements com.android.server.wm.IRootWindowContainerSocExt {
    private static final java.lang.String TAG = "RootWindowContainerSocExtImpl";
    com.android.server.wm.RootWindowContainer mRootWindowContainer;
    public android.util.BoostFramework mPerfBoost = null;
    public android.util.BoostFramework mUxPerf = null;

    public RootWindowContainerSocExtImpl(java.lang.Object container) {
        this.mRootWindowContainer = (com.android.server.wm.RootWindowContainer) container;
    }

    @Override // com.android.server.wm.IRootWindowContainerSocExt
    public void acquireAppLaunchPerfLock(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityTaskManagerService service) {
        int i;
        com.android.server.wm.WindowProcessController wpc;
        if (this.mPerfBoost == null) {
            this.mPerfBoost = new android.util.BoostFramework();
        }
        if (this.mPerfBoost != null) {
            if (r != null && com.android.server.wm.ActivityRecord.isMainIntent(r.intent)) {
                int pkgType = this.mPerfBoost.perfGetFeedback(5633, r.packageName);
                int wpcPid = -1;
                if (service != null && r.info != null && r.info.applicationInfo != null && (wpc = service.getProcessController(r.processName, r.info.applicationInfo.uid)) != null && wpc.hasThread()) {
                    wpcPid = wpc.getPid();
                }
                if (this.mPerfBoost.getPerfHalVersion() >= 2.299999952316284d) {
                    this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 1, 2, new int[]{pkgType, wpcPid});
                    com.android.server.wm.RootWindowContainer.mPerfSendTapHint = true;
                    this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 2, 2, new int[]{pkgType, wpcPid});
                    if (wpcPid != -1) {
                        this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, wpcPid, 103, 2, new int[]{pkgType, wpcPid});
                    }
                    if (pkgType == 2) {
                        com.android.server.wm.RootWindowContainer.mPerfHandle = this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 4, 2, new int[]{pkgType, wpcPid});
                        i = -1;
                    } else {
                        com.android.server.wm.RootWindowContainer.mPerfHandle = this.mPerfBoost.perfHintAcqRel(-1, 4225, r.packageName, -1, 3, 2, new int[]{pkgType, wpcPid});
                        i = -1;
                    }
                } else {
                    i = -1;
                    this.mPerfBoost.perfHint(4225, r.packageName, -1, 1);
                    com.android.server.wm.RootWindowContainer.mPerfSendTapHint = true;
                    this.mPerfBoost.perfHint(4225, r.packageName, -1, 2);
                    if (wpcPid != -1) {
                        this.mPerfBoost.perfHint(4225, r.packageName, wpcPid, 103);
                    }
                    if (pkgType == 2) {
                        com.android.server.wm.RootWindowContainer.mPerfHandle = this.mPerfBoost.perfHint(4225, r.packageName, -1, 4);
                    } else {
                        com.android.server.wm.RootWindowContainer.mPerfHandle = this.mPerfBoost.perfHint(4225, r.packageName, -1, 3);
                    }
                }
                if (com.android.server.wm.RootWindowContainer.mPerfHandle > 0) {
                    com.android.server.wm.RootWindowContainer.mIsPerfBoostAcquired = true;
                }
                if (r.info != null && r.info.applicationInfo != null && r.info.applicationInfo.sourceDir != null && this.mPerfBoost.board_first_api_lvl < 33 && this.mPerfBoost.board_api_lvl < 33 && !service.getWrapper().isIOPreloadPkg(r.packageName, r.mUserId)) {
                    this.mPerfBoost.perfIOPrefetchStart(i, r.packageName, r.info.applicationInfo.sourceDir.substring(0, r.info.applicationInfo.sourceDir.lastIndexOf(47)));
                    return;
                }
                return;
            }
            if (r == null) {
                android.util.Slog.w(TAG, "Should not happen! Didn't apply launch boost");
            }
        }
    }

    @Override // com.android.server.wm.IRootWindowContainerSocExt
    public void acquireUxPerfLock(int opcode, java.lang.String packageName) {
        this.mUxPerf = new android.util.BoostFramework();
        if (this.mUxPerf != null) {
            if (this.mUxPerf.board_first_api_lvl < 33 && this.mUxPerf.board_api_lvl < 33) {
                this.mUxPerf.perfUXEngine_events(opcode, 0, packageName, 0);
            } else {
                this.mUxPerf.perfEvent(4257, packageName, 2, new int[]{0, 0});
            }
        }
    }
}
