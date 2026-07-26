package com.android.server.statusbar;

/* JADX INFO: loaded from: classes3.dex */
public interface IStatusBarManagerServiceExt {
    default void init(android.content.Context context) {
    }

    default boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        return false;
    }

    default void collapsePanels() {
    }

    default void expandNotificationsPanel() {
    }

    default void maybeClearAllNotifications() {
    }
}
