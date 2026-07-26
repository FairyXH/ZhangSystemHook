package com.android.server.backup.remote;

/* JADX INFO: loaded from: classes.dex */
@java.lang.FunctionalInterface
public interface RemoteCallable<T> {
    void call(T t) throws android.os.RemoteException;
}
