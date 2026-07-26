package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityTaskManagerServiceSocExtImpl implements com.android.server.wm.IActivityTaskManagerServiceSocExt {
    com.android.server.wm.ActivityTaskManagerService mService;

    public ActivityTaskManagerServiceSocExtImpl(java.lang.Object service) {
        this.mService = (com.android.server.wm.ActivityTaskManagerService) service;
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onActivityStateChanged(com.android.server.wm.ActivityRecord activity, boolean onTop) {
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onEndOfActivityIdle(android.content.Context context, com.android.server.wm.ActivityRecord activityRecord) {
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onBeforeActivitySwitch(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activityRecord) {
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onAfterActivityResumed(com.android.server.wm.ActivityRecord resumedActivity) {
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void setLastResumedBeforeActivitySwitch(com.android.server.wm.ActivityRecord lastResumed, com.android.server.wm.ActivityRecord mResumedActivity) {
    }

    @Override // com.android.server.wm.IActivityTaskManagerServiceSocExt
    public void onBeforeActivitySwitch(com.android.server.wm.ActivityRecord nextResumedActivity, boolean pausing, int nextResumedActivityType, boolean isKeyguardShowing) {
    }
}
