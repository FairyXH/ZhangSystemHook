package com.android.server.ondeviceintelligence.callbacks;

/* JADX INFO: loaded from: classes2.dex */
public class ListenableDownloadCallback extends android.app.ondeviceintelligence.IDownloadCallback.Stub implements java.lang.Runnable {
    private final android.app.ondeviceintelligence.IDownloadCallback callback;
    private final com.android.internal.infra.AndroidFuture future;
    private final android.os.Handler handler;
    private final long idleTimeoutMs;

    public ListenableDownloadCallback(android.app.ondeviceintelligence.IDownloadCallback callback, android.os.Handler handler, com.android.internal.infra.AndroidFuture future, long idleTimeoutMs) {
        this.callback = callback;
        this.handler = handler;
        this.future = future;
        this.idleTimeoutMs = idleTimeoutMs;
        handler.postDelayed(this, idleTimeoutMs);
    }

    public void onDownloadStarted(long bytesToDownload) throws android.os.RemoteException {
        this.callback.onDownloadStarted(bytesToDownload);
        this.handler.removeCallbacks(this);
        this.handler.postDelayed(this, this.idleTimeoutMs);
    }

    public void onDownloadProgress(long bytesDownloaded) throws android.os.RemoteException {
        this.callback.onDownloadProgress(bytesDownloaded);
        this.handler.removeCallbacks(this);
        this.handler.postDelayed(this, this.idleTimeoutMs);
    }

    public void onDownloadFailed(int failureStatus, java.lang.String errorMessage, android.os.PersistableBundle errorParams) throws android.os.RemoteException {
        this.callback.onDownloadFailed(failureStatus, errorMessage, errorParams);
        this.handler.removeCallbacks(this);
        this.future.completeExceptionally(new java.util.concurrent.TimeoutException());
    }

    public void onDownloadCompleted(android.os.PersistableBundle downloadParams) throws android.os.RemoteException {
        this.callback.onDownloadCompleted(downloadParams);
        this.handler.removeCallbacks(this);
        this.future.complete((java.lang.Object) null);
    }

    @Override // java.lang.Runnable
    public void run() {
        this.future.completeExceptionally(new java.util.concurrent.TimeoutException());
    }
}
