package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
public interface RollbackManagerInternal {
    int notifyStagedSession(int i);

    void snapshotAndRestoreUserData(java.lang.String str, java.util.List<android.os.UserHandle> list, int i, long j, java.lang.String str2, int i2);
}
