package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityStarterWrapper {
    default void setTargetRootTaskIfNeeded(com.android.server.wm.ActivityRecord intentActivity, com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict) {
    }

    default void setSourceRecord(com.android.server.wm.ActivityRecord record) {
    }

    default void setSourceRootTask(com.android.server.wm.Task task) {
    }

    default void setInTask(com.android.server.wm.Task task) {
    }

    default com.android.server.wm.ActivityTaskManagerService getService() {
        return null;
    }

    default android.app.ActivityOptions getOptions() {
        return null;
    }

    default void setOptions(android.app.ActivityOptions options) {
    }
}
