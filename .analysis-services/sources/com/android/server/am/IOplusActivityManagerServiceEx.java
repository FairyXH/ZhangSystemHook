package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusActivityManagerServiceEx extends com.android.server.IOplusCommonManagerServiceEx {
    public static final com.android.server.am.IOplusActivityManagerServiceEx DEFAULT = new com.android.server.am.IOplusActivityManagerServiceEx() { // from class: com.android.server.am.IOplusActivityManagerServiceEx.1
    };
    public static final java.lang.String NAME = "IOplusActivityManagerServiceEx";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusActivityManagerServiceEx;
    }

    default com.android.server.am.IOplusActivityManagerServiceEx getDefault() {
        return DEFAULT;
    }

    default com.android.server.am.ActivityManagerService getActivityManagerService() {
        return null;
    }

    default void handleMessage(android.os.Message msg, int whichHandler) {
    }

    default void putProcInfoArray(int pid, int uid) {
    }

    default void deleteProcInfoArray(int pid) {
    }

    default com.android.server.am.ProcessRecord getProcessRecordLocked(java.lang.String processName, int uid, boolean keepIfLarge) {
        return null;
    }

    default void startPersistentApp(java.lang.String packageName) {
    }

    default void forceStopPackageWithoutRestart(java.lang.String packageName, java.lang.String reason) {
    }

    default void enableWmShellProtoLogs(java.lang.String[] args, java.io.PrintWriter pw, java.io.FileDescriptor fd) {
    }

    default boolean isBackupRestoreType(java.lang.String packageName, int uid) {
        return false;
    }

    default boolean isBackupType(java.lang.String processName, int uid) {
        return false;
    }

    default void clearPendingBackupUid(int uid) {
    }
}
