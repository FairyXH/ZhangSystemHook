package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityTaskManagerServiceSocExt {
    default void onActivityStateChanged(com.android.server.wm.ActivityRecord activity, boolean onTop) {
    }

    default void onEndOfActivityIdle(android.content.Context context, com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void onBeforeActivitySwitch(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activityRecord) {
    }

    default void onBeforeActivitySwitch(com.android.server.wm.ActivityRecord nextResumedActivity, boolean pausing, int nextResumedActivityType, boolean isKeyguardShowing) {
    }

    default void onAfterActivityResumed(com.android.server.wm.ActivityRecord resumedActivity) {
    }

    default void setLastResumedBeforeActivitySwitch(com.android.server.wm.ActivityRecord lastResumed, com.android.server.wm.ActivityRecord mResumedActivity) {
    }
}
