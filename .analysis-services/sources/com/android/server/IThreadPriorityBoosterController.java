package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IThreadPriorityBoosterController extends android.common.IOplusCommonFeature {
    public static final com.android.server.IThreadPriorityBoosterController DEFAULT = new com.android.server.IThreadPriorityBoosterController() { // from class: com.android.server.IThreadPriorityBoosterController.1
    };
    public static final java.lang.String NAME = "IThreadPriorityBoosterController";

    default com.android.server.IThreadPriorityBoosterController getDefault() {
        return DEFAULT;
    }

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IThreadPriorityBoosterController;
    }

    default void init(com.android.server.wm.WindowManagerService wms) {
    }

    default void setLockOwnerThreadBoost(com.android.server.ThreadPriorityBooster mBooster) {
    }

    default void setEnable(boolean enable) {
    }
}
