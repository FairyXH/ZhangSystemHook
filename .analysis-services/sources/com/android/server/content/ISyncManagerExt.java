package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public interface ISyncManagerExt {
    default void init(android.content.Context context) {
    }

    default void onBootPhase(int phase) {
    }

    default void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, boolean dumpAll) {
    }

    default boolean interceptDispatchSyncOperation(int targetUid, com.android.server.content.SyncOperation op, android.content.ComponentName targetComponent) {
        return false;
    }

    default boolean isSyncValid(int uid, java.lang.String packageName) {
        return true;
    }
}
