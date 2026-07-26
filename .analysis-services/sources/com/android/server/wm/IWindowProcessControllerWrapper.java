package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowProcessControllerWrapper {
    default com.android.server.wm.IWindowProcessControllerExt getExtImpl() {
        return new com.android.server.wm.IWindowProcessControllerExt() { // from class: com.android.server.wm.IWindowProcessControllerWrapper.1
        };
    }

    default java.util.ArrayList<java.lang.String> getPkgList() {
        return new java.util.ArrayList<>();
    }

    default java.util.ArrayList<com.android.server.wm.ActivityRecord> getActivities() {
        return new java.util.ArrayList<>();
    }

    default com.android.server.wm.ActivityTaskManagerService getAtm() {
        return null;
    }

    default boolean hasActivityInVisibleTask() {
        return false;
    }

    default boolean hasResumedActivity() {
        return false;
    }
}
