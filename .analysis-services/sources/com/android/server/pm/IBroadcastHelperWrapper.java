package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IBroadcastHelperWrapper {
    default void sendPackageBroadcastAndNotify(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int flags, java.lang.String targetPkg, android.content.IIntentReceiver finishedReceiver, int[] userIds, int[] instantUserIds, android.util.SparseArray<int[]> broadcastAllowList, android.os.Bundle bOptions) {
    }

    default android.os.Handler getHandler() {
        return null;
    }
}
