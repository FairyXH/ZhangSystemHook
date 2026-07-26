package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITaskChangeNotificationControllerExt {
    default boolean shouldSkipSendTaskSnapshot(com.android.server.wm.ActivityTaskSupervisor supervisor, android.app.ITaskStackListener listener, android.os.Message msg) throws android.os.RemoteException {
        return false;
    }
}
