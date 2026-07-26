package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageManagerNativeExt {
    default void init(com.android.server.pm.PackageManagerService service, android.content.Context context) {
    }

    default boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        return false;
    }
}
