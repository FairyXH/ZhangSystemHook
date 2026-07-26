package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskWrapper {
    default com.android.server.wm.ITaskExt getExtImpl() {
        return new com.android.server.wm.ITaskExt() { // from class: com.android.server.wm.ITaskWrapper.1
        };
    }

    default void activityInvokeNotification(java.lang.String appName, boolean isFullScreen) {
    }

    default void onARStopTriggered(com.android.server.wm.ActivityRecord r) {
    }

    default com.android.server.wm.WindowProcessController getWindowProcessController() {
        return null;
    }

    default void removeHiddenFlags(int flags) {
    }

    default void callSuperOnConfigurationChanged(android.content.res.Configuration config) {
    }

    default com.android.server.wm.ActivityRecord topRunningActivityLocked() {
        return null;
    }
}
