package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusActivityTaskManagerServiceEx extends com.android.server.IOplusCommonManagerServiceEx {
    public static final com.android.server.wm.IOplusActivityTaskManagerServiceEx DEFAULT = new com.android.server.wm.IOplusActivityTaskManagerServiceEx() { // from class: com.android.server.wm.IOplusActivityTaskManagerServiceEx.1
    };
    public static final java.lang.String NAME = "IOplusActivityTaskManagerServiceEx";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusActivityTaskManagerServiceEx;
    }

    default com.android.server.wm.IOplusActivityTaskManagerServiceEx getDefault() {
        return DEFAULT;
    }

    default com.android.server.wm.ActivityTaskManagerService getActivityTaskManagerService() {
        return null;
    }

    default com.android.server.wm.IOplusActivityStackSupervisorInner getColorActivityStackSupervisorInner(com.android.server.wm.ActivityStackSupervisor supervisor) {
        return new com.android.server.wm.IOplusActivityStackSupervisorInner() { // from class: com.android.server.wm.IOplusActivityTaskManagerServiceEx.2
        };
    }

    default com.android.server.wm.IOplusActivityStackInner getColorActivityStackInner(com.android.server.wm.Task stack) {
        return new com.android.server.wm.IOplusActivityStackInner() { // from class: com.android.server.wm.IOplusActivityTaskManagerServiceEx.3
        };
    }

    default void hookAtmsConfigurationChang(int changes, com.android.server.wm.RootWindowContainer mRootWindowContainer, com.android.server.wm.WindowManagerService mWindowManager) {
    }
}
