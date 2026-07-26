package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public enum UnifiedRestoreState {
    INITIAL,
    RUNNING_QUEUE,
    RESTORE_KEYVALUE,
    RESTORE_FULL,
    RESTORE_FINISHED,
    FINAL
}
