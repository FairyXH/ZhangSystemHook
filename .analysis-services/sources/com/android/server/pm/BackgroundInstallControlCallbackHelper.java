package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class BackgroundInstallControlCallbackHelper {
    static final java.lang.String FLAGGED_PACKAGE_NAME_KEY = "packageName";
    static final java.lang.String FLAGGED_USER_ID_KEY = "userId";
    private static final java.lang.String TAG = "BackgroundInstallControlCallbackHelper";
    final android.os.RemoteCallbackList<android.os.IRemoteCallback> mCallbacks = new android.os.RemoteCallbackList<>();
    private final android.os.Handler mHandler;

    BackgroundInstallControlCallbackHelper() {
        android.os.HandlerThread backgroundThread = new com.android.server.ServiceThread("BackgroundInstallControlCallbackHelperBg", 10, true);
        backgroundThread.start();
        this.mHandler = new android.os.Handler(backgroundThread.getLooper());
    }

    public void registerBackgroundInstallCallback(android.os.IRemoteCallback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.register(callback, null);
        }
    }

    public void unregisterBackgroundInstallCallback(android.os.IRemoteCallback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.unregister(callback);
        }
    }

    public void notifyAllCallbacks(int userId, java.lang.String packageName) {
        final android.os.Bundle extras = new android.os.Bundle();
        extras.putCharSequence("packageName", packageName);
        extras.putInt("userId", userId);
        synchronized (this.mCallbacks) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.BackgroundInstallControlCallbackHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyAllCallbacks$1(extras);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyAllCallbacks$1(final android.os.Bundle extras) {
        this.mCallbacks.broadcast(new java.util.function.Consumer() { // from class: com.android.server.pm.BackgroundInstallControlCallbackHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.BackgroundInstallControlCallbackHelper.lambda$notifyAllCallbacks$0(extras, (android.os.IRemoteCallback) obj);
            }
        });
    }

    static /* synthetic */ void lambda$notifyAllCallbacks$0(android.os.Bundle extras, android.os.IRemoteCallback callback) {
        try {
            callback.sendResult(extras);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "error detected: " + e.getLocalizedMessage(), e);
        }
    }
}
