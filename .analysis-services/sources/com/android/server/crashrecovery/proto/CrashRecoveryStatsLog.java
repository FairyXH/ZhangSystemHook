package com.android.server.crashrecovery.proto;

/* JADX INFO: loaded from: classes.dex */
public final class CrashRecoveryStatsLog {
    public static final byte ANNOTATION_ID_DEFAULT_STATE = 6;
    public static final byte ANNOTATION_ID_EXCLUSIVE_STATE = 4;
    public static final byte ANNOTATION_ID_IS_UID = 1;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD = 3;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD_FIRST_UID = 5;
    public static final byte ANNOTATION_ID_STATE_NESTED = 8;
    public static final byte ANNOTATION_ID_TRIGGER_STATE_RESET = 7;
    public static final byte ANNOTATION_ID_TRUNCATE_TIMESTAMP = 2;
    public static final int RESCUE_PARTY_RESET_REPORTED = 122;
    public static final int WATCHDOG_ROLLBACK_OCCURRED = 147;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_APP_CRASH = 3;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_APP_NOT_RESPONDING = 4;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_BOOT_LOOPING = 7;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_EXPLICIT_HEALTH_CHECK = 2;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_NATIVE_CRASH = 1;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_NATIVE_CRASH_DURING_BOOT = 5;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_NETWORK_RELATED_CRASH = 6;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_UNKNOWN = 0;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_BOOT_TRIGGERED = 4;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_FAILURE = 3;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_INITIATE = 1;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_SUCCESS = 2;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__UNKNOWN = 0;

    public static void write(int code, int arg1, java.lang.String arg2) {
        android.util.StatsEvent.Builder builder = android.util.StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.usePooledBuffer();
        android.util.StatsLog.write(builder.build());
    }

    public static void write(int code, int arg1, java.lang.String arg2, int arg3, int arg4, java.lang.String arg5, byte[] arg6) {
        android.util.StatsEvent.Builder builder = android.util.StatsEvent.newBuilder();
        builder.setAtomId(code);
        builder.writeInt(arg1);
        builder.writeString(arg2);
        builder.writeInt(arg3);
        builder.writeInt(arg4);
        builder.writeString(arg5);
        builder.writeByteArray(arg6 == null ? new byte[0] : arg6);
        builder.usePooledBuffer();
        android.util.StatsLog.write(builder.build());
    }
}
