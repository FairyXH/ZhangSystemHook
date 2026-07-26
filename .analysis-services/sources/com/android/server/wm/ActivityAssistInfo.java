package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityAssistInfo {
    private final android.os.IBinder mActivityToken;
    private final android.os.IBinder mAssistToken;
    private final android.content.ComponentName mComponentName;
    private final int mTaskId;
    private final int mUserId;

    public ActivityAssistInfo(com.android.server.wm.ActivityRecord activityRecord) {
        this.mActivityToken = activityRecord.token;
        this.mAssistToken = activityRecord.assistToken;
        this.mTaskId = activityRecord.getTask().mTaskId;
        this.mComponentName = activityRecord.mActivityComponent;
        this.mUserId = activityRecord.mUserId;
    }

    public android.os.IBinder getActivityToken() {
        return this.mActivityToken;
    }

    public android.os.IBinder getAssistToken() {
        return this.mAssistToken;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    public int getUserId() {
        return this.mUserId;
    }
}
