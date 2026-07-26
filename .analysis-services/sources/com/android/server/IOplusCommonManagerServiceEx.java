package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusCommonManagerServiceEx extends android.common.IOplusCommonFeature {
    default void onStart() {
    }

    default void systemReady() {
    }

    default boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        return false;
    }
}
