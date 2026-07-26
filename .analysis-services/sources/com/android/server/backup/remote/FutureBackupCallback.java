package com.android.server.backup.remote;

/* JADX INFO: loaded from: classes.dex */
public class FutureBackupCallback extends android.app.backup.IBackupCallback.Stub {
    private final java.util.concurrent.CompletableFuture<com.android.server.backup.remote.RemoteResult> mFuture;

    FutureBackupCallback(java.util.concurrent.CompletableFuture<com.android.server.backup.remote.RemoteResult> future) {
        this.mFuture = future;
    }

    public void operationComplete(long result) throws android.os.RemoteException {
        this.mFuture.complete(com.android.server.backup.remote.RemoteResult.of(result));
    }
}
