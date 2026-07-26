package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IBroadcastHelperExt {
    default void sendCustomizedBroadcastInDoSendBroadcast(android.content.Intent intent, java.lang.String action, int userId, java.lang.String targetPkg, android.content.IIntentReceiver finishedReceiver) {
    }

    default void insertPackageAddedBroadcastData(java.lang.String action, android.content.Context context, android.content.Intent intent, java.lang.String packageName) {
    }
}
