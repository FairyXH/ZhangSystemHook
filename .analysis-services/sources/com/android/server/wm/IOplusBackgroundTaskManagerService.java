package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusBackgroundTaskManagerService extends android.common.IOplusCommonFeature {
    public static final com.android.server.wm.IOplusBackgroundTaskManagerService DEFAULT = new com.android.server.wm.IOplusBackgroundTaskManagerService() { // from class: com.android.server.wm.IOplusBackgroundTaskManagerService.1
    };
    public static final java.lang.String NAME = "IOplusBackgroundTaskManagerService";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusBackgroundTaskManagerService;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void setScreenOffPlay(boolean screenOffPlay) {
    }

    default boolean isScreenOffPlay(com.android.server.wm.Task activityStack) {
        return false;
    }

    default boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
        return false;
    }
}
