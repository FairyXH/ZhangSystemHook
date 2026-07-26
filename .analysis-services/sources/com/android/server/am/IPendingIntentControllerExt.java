package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IPendingIntentControllerExt {
    default void killPendingApplicationIfNeed(int count, com.android.server.am.PendingIntentRecord pir, com.android.server.am.PendingIntentController pic, android.util.SparseIntArray intentsPerUid) {
    }

    default void addPendingIntentUid(int uid, java.lang.String packageName, int userId) {
    }

    default void deletePendingIntentUid(int uid, java.lang.String packageName, int userId) {
    }

    default void removePendingIntentUid(int uid) {
    }

    default void recyclePendingIntentsIfNeed(int count, com.android.server.am.PendingIntentRecord pir, com.android.server.am.PendingIntentController pic, android.util.SparseIntArray intentsPerUid) {
    }
}
