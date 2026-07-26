package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ActivityManagerServiceSocExtImpl implements com.android.server.am.IActivityManagerServiceSocExt {
    private static final java.lang.String TAG = "ActivityManagerServiceSocExtImpl";
    com.android.server.am.ActivityManagerService mAms;
    public static android.util.BoostFramework mPerfServiceStartHint = null;
    public static android.util.BoostFramework mUxPerf = new android.util.BoostFramework();
    static int COMPACTION_DELAY_MS = 300000;

    public ActivityManagerServiceSocExtImpl(java.lang.Object ams) {
        this.mAms = (com.android.server.am.ActivityManagerService) ams;
        try {
            COMPACTION_DELAY_MS = java.lang.Integer.valueOf(mUxPerf.perfGetProp("ro.vendor.qti.sys.fw.compaction_delay_sec", "300")).intValue() * 1000;
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "compaction_delay_sec:failed to obtain value", e);
        }
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void addPidLocked(com.android.server.am.ProcessRecord app) {
        com.android.server.ActivityTriggerService atService = (com.android.server.ActivityTriggerService) com.android.server.LocalServices.getService(com.android.server.ActivityTriggerService.class);
        if (atService != null) {
            atService.updateRecord(app.getHostingRecord(), app.info, app.getPid(), 1);
        }
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void removePidLocked(com.android.server.am.ProcessRecord app) {
        com.android.server.ActivityTriggerService atService = (com.android.server.ActivityTriggerService) com.android.server.LocalServices.getService(com.android.server.ActivityTriggerService.class);
        if (atService != null) {
            atService.updateRecord(app.getHostingRecord(), app.info, app.getPid(), 0);
        }
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void appDiedLocked(com.android.server.am.ProcessRecord app, int pid) {
        if (mUxPerf != null && !com.android.server.am.ActivityManagerService.mForceStopKill && !app.mErrorState.isNotResponding() && !app.mErrorState.isCrashing()) {
            if (mUxPerf.board_first_api_lvl < 33 && mUxPerf.board_api_lvl < 33) {
                mUxPerf.perfUXEngine_events(4, 0, app.processName, 0);
            }
            mUxPerf.perfEvent(4243, app.processName, 2, new int[]{0, pid});
        }
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void perfHint(com.android.server.am.ProcessRecord app, int pid) {
        if (mUxPerf == null || app.getHostingRecord() == null || !app.getHostingRecord().isTopApp()) {
            return;
        }
        if (mUxPerf.getPerfHalVersion() >= 2.299999952316284d) {
            int pkgType = mUxPerf.perfGetFeedback(5633, app.processName);
            mUxPerf.perfHintAcqRel(-1, 4225, app.processName, pid, 103, 1, new int[]{pkgType});
        } else {
            mUxPerf.perfHint(4225, app.processName, pid, 103);
        }
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void updateForceStopKillFlag() {
        com.android.server.am.ActivityManagerService.mForceStopKill = true;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void compactAllSystem() {
        this.mAms.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.ActivityManagerServiceSocExtImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$compactAllSystem$0();
            }
        }, COMPACTION_DELAY_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$compactAllSystem$0() {
        this.mAms.mOomAdjuster.mCachedAppOptimizer.compactAllSystem();
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public boolean delayMessage(android.os.Handler mHandler, android.os.Message msg, int msgId, int time) {
        return false;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void addAnrManagerService() {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void startAnrManagerService(int pid) {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void writeBootCompletedEvent() {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public boolean isAnrDeferrable() {
        return false;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void onAddErrorToDropBox(java.lang.String dropboxTag, java.lang.String info, int pid) {
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public java.lang.Object getAnrManager() {
        return null;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public java.lang.Object getAmsExt() {
        return null;
    }

    @Override // com.android.server.am.IActivityManagerServiceSocExt
    public void onNotifyAppCrash(int pid, int uid, java.lang.String packageName) {
    }
}
