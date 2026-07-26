package com.android.server.backup.keyvalue;

/* JADX INFO: loaded from: classes.dex */
class TaskException extends com.android.server.backup.keyvalue.BackupException {
    private static final int DEFAULT_STATUS = -1000;
    private final boolean mStateCompromised;
    private final int mStatus;

    static com.android.server.backup.keyvalue.TaskException stateCompromised() {
        return new com.android.server.backup.keyvalue.TaskException(true, -1000);
    }

    static com.android.server.backup.keyvalue.TaskException stateCompromised(java.lang.Exception cause) {
        if (cause instanceof com.android.server.backup.keyvalue.TaskException) {
            com.android.server.backup.keyvalue.TaskException exception = (com.android.server.backup.keyvalue.TaskException) cause;
            return new com.android.server.backup.keyvalue.TaskException(cause, true, exception.getStatus());
        }
        return new com.android.server.backup.keyvalue.TaskException(cause, true, -1000);
    }

    static com.android.server.backup.keyvalue.TaskException forStatus(int status) {
        com.android.internal.util.Preconditions.checkArgument(status != 0, "Exception based on TRANSPORT_OK");
        return new com.android.server.backup.keyvalue.TaskException(false, status);
    }

    static com.android.server.backup.keyvalue.TaskException causedBy(java.lang.Exception cause) {
        if (cause instanceof com.android.server.backup.keyvalue.TaskException) {
            return (com.android.server.backup.keyvalue.TaskException) cause;
        }
        return new com.android.server.backup.keyvalue.TaskException(cause, false, -1000);
    }

    static com.android.server.backup.keyvalue.TaskException create() {
        return new com.android.server.backup.keyvalue.TaskException(false, -1000);
    }

    private TaskException(java.lang.Exception cause, boolean stateCompromised, int status) {
        super(cause);
        this.mStateCompromised = stateCompromised;
        this.mStatus = status;
    }

    private TaskException(boolean stateCompromised, int status) {
        this.mStateCompromised = stateCompromised;
        this.mStatus = status;
    }

    boolean isStateCompromised() {
        return this.mStateCompromised;
    }

    int getStatus() {
        return this.mStatus;
    }
}
