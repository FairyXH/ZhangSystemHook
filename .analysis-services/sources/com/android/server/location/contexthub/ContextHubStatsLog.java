package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
public final class ContextHubStatsLog {
    public static final byte ANNOTATION_ID_DEFAULT_STATE = 6;
    public static final byte ANNOTATION_ID_EXCLUSIVE_STATE = 4;
    public static final byte ANNOTATION_ID_IS_UID = 1;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD = 3;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD_FIRST_UID = 5;
    public static final byte ANNOTATION_ID_STATE_NESTED = 8;
    public static final byte ANNOTATION_ID_TRIGGER_STATE_RESET = 7;
    public static final byte ANNOTATION_ID_TRUNCATE_TIMESTAMP = 2;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED = 401;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_AT_HUB = 5;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_BAD_PARAMS = 2;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_BUSY = 4;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_HAL_UNAVAILABLE = 8;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_SERVICE_INTERNAL_FAILURE = 7;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_TIMEOUT = 6;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_UNINITIALIZED = 3;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_FAILED_UNKNOWN = 1;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_RESULT__TRANSACTION_RESULT_SUCCESS = 0;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_TYPE__TYPE_LOAD = 1;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_TYPE__TYPE_UNKNOWN = 0;
    public static final int CHRE_CODE_DOWNLOAD_TRANSACTED__TRANSACTION_TYPE__TYPE_UNLOAD = 2;
    public static final int CONTEXT_HUB_BOOTED = 398;
    public static final int CONTEXT_HUB_LOADED_NANOAPP_SNAPSHOT_REPORTED = 400;
    public static final int CONTEXT_HUB_RESTARTED = 399;

    public static void write(int code, int arg1, long arg2, int arg3) {
        android.util.StatsEvent.Builder builder = android.util.StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeLong(arg2);
        builder.writeInt(arg3);
        builder.usePooledBuffer();
        android.util.StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, int arg2) {
        android.util.StatsEvent.Builder builder = android.util.StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeInt(arg2);
        builder.usePooledBuffer();
        android.util.StatsLog.write(builder.build());
    }

    public static void write(int code, long arg1, int arg2, int arg3, int arg4) {
        android.util.StatsEvent.Builder builder = android.util.StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeLong(arg1);
        builder.writeInt(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.usePooledBuffer();
        android.util.StatsLog.write(builder.build());
    }
}
