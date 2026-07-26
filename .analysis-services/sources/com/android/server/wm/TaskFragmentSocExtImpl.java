package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class TaskFragmentSocExtImpl implements com.android.server.wm.ITaskFragmentSocExt {
    static final com.android.internal.app.ActivityTrigger mActivityTrigger = new com.android.internal.app.ActivityTrigger();
    public android.util.BoostFramework mPerf = null;
    com.android.server.wm.TaskFragment mTaskFragment;

    public TaskFragmentSocExtImpl(java.lang.Object service) {
        this.mTaskFragment = (com.android.server.wm.TaskFragment) service;
    }

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void hookTriggerActivityResume(com.android.server.wm.ActivityRecord next) {
        mActivityTrigger.activityResumeTrigger(next.intent, next.info, next.info.applicationInfo, next.occludesParent());
    }

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void initPerf() {
        if (this.mPerf == null) {
            this.mPerf = new android.util.BoostFramework();
        }
    }

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void hookVendorHintAnimBoost(com.android.server.wm.ActivityRecord prev, com.android.server.wm.ActivityRecord next) {
        if (prev.getTask() != next.getTask() && this.mPerf != null) {
            this.mPerf.perfHint(4227, next.packageName);
        }
    }

    @Override // com.android.server.wm.ITaskFragmentSocExt
    public void hookTriggerActivityPause(com.android.server.wm.ActivityRecord prev) {
        mActivityTrigger.activityPauseTrigger(prev.intent, prev.info, prev.info.applicationInfo);
    }
}
