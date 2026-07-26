package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public interface IAppOpsServiceExt {
    public static final int CUSTOM_NUM_OP = 10004;
    public static final int LOG_CHECK_OP = 2;
    public static final int LOG_NOTE_OP = 3;
    public static final int LOG_SET_UID_MODE = 0;
    public static final int LOG_START_FINISH_OP = 1;
    public static final int OP_CUSTOM_NONE = 10000;
    public static final int RECORD_NOTE_ALLOWED = 0;
    public static final int RECORD_REJECTED = 2;
    public static final int RECORD_START_ALLOWED = 1;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface RecordRes {
    }

    default void hookServiceStart(com.android.server.appop.AppOpsService appOpsService, android.content.Context context) {
    }

    default void shouldBackupAppOpsXml() {
    }

    default void checkCapabilityFailed(int[] args) {
    }

    default void syncOpForMultiApp(int opCode, int uid, java.lang.String packageName, int mode, android.os.Handler handler) {
    }

    default void notifyPermissionRecordAsUser(java.lang.String packageName, int opCode, int result, int uidState, int uid) {
    }

    default void addCustomSwitchedOps(android.util.SparseArray<int[]> ops) {
    }

    default boolean isActivityPreloadPkg(java.lang.String pkg, int callingUid) {
        return false;
    }

    default void hookSetAudioRestriction(android.content.Context context, int code, int usage, int uid, int mode, android.os.Handler handler) {
    }

    default boolean shouldLog(java.lang.String packageName, int opCode, int type) {
        return false;
    }

    default void handleOpsException(android.content.Context context, int callingUid, long beginTimeMillis, long endTimeMillis, android.os.Handler handler, android.os.RemoteCallback callback) {
    }
}
