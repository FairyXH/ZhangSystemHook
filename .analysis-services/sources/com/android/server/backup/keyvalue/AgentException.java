package com.android.server.backup.keyvalue;

/* JADX INFO: loaded from: classes.dex */
class AgentException extends com.android.server.backup.keyvalue.BackupException {
    private final boolean mTransitory;

    static com.android.server.backup.keyvalue.AgentException transitory() {
        return new com.android.server.backup.keyvalue.AgentException(true);
    }

    static com.android.server.backup.keyvalue.AgentException transitory(java.lang.Exception cause) {
        return new com.android.server.backup.keyvalue.AgentException(true, cause);
    }

    static com.android.server.backup.keyvalue.AgentException permanent() {
        return new com.android.server.backup.keyvalue.AgentException(false);
    }

    static com.android.server.backup.keyvalue.AgentException permanent(java.lang.Exception cause) {
        return new com.android.server.backup.keyvalue.AgentException(false, cause);
    }

    private AgentException(boolean transitory) {
        this.mTransitory = transitory;
    }

    private AgentException(boolean transitory, java.lang.Exception cause) {
        super(cause);
        this.mTransitory = transitory;
    }

    boolean isTransitory() {
        return this.mTransitory;
    }
}
