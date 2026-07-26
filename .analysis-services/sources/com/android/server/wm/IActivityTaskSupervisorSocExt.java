package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityTaskSupervisorSocExt {
    default void notifyServiceTracker(com.android.server.wm.ActivityRecord.State state, boolean early_notify, com.android.server.wm.ActivityRecord r, long createTime) {
    }

    default void acquireAppLaunchPerfLock(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityTaskManagerService service) {
    }

    default void startSpecificActivityPerfHint(java.lang.String tag, com.android.server.wm.ActivityRecord r, int pid) {
    }

    default void reportActivityLaunchedPerfHint(com.android.server.wm.ActivityRecord r) {
    }

    default void startPreferredApps(java.lang.String tag, com.android.server.wm.ActivityTaskManagerService service) {
    }
}
