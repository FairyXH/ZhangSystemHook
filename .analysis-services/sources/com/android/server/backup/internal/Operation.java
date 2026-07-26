package com.android.server.backup.internal;

/* JADX INFO: loaded from: classes.dex */
public class Operation {
    public final com.android.server.backup.BackupRestoreTask callback;
    public int state;
    public final int type;

    public Operation(int initialState, com.android.server.backup.BackupRestoreTask callbackObj, int type) {
        this.state = initialState;
        this.callback = callbackObj;
        this.type = type;
    }
}
