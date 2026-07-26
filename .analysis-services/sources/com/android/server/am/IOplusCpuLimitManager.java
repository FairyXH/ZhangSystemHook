package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusCpuLimitManager extends android.common.IOplusCommonFeature {
    public static final com.android.server.am.IOplusCpuLimitManager DEFAULT = new com.android.server.am.IOplusCpuLimitManager() { // from class: com.android.server.am.IOplusCpuLimitManager.1
    };
    public static final int INVALID_GROUP = -10000;

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusCpuLimitManager;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void onInit(com.android.server.am.IOplusActivityManagerServiceEx amsEx) {
    }

    default void updateConfigList(android.os.Bundle data) {
    }

    default void createProcessInfo(com.android.server.am.ProcessRecord processRecord) {
    }

    default void removeProcessInfo(com.android.server.am.ProcessRecord processRecord) {
    }

    default void updateProcessState(com.android.server.am.ProcessRecord proc, int procState) {
    }

    default void notifyProcessGroupChange(com.android.server.am.ProcessRecord proc, int oldSchedGroup, int curSchedGroup, int processGroup) {
    }

    default boolean skipSetSchedGroup(com.android.server.am.ProcessRecord processRecord, java.lang.String type) {
        return false;
    }

    default boolean cpuSetEnable() {
        return false;
    }

    default android.os.SharedMemory getCpuLimitLatestLog(java.lang.String pkgName) {
        return null;
    }

    default void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default void notifyAppStatusByOSense(com.android.server.oplus.osense.IntegratedData data) {
    }

    default boolean isOclGrpRequestMsgAndSetGroup(android.os.Message msg) {
        return false;
    }
}
