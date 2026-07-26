package com.android.server.backup.transport;

/* JADX INFO: loaded from: classes.dex */
public class BackupTransportClient {
    private static final java.lang.String TAG = "BackupTransportClient";
    private final com.android.internal.backup.IBackupTransport mTransportBinder;
    private final com.android.server.backup.transport.BackupTransportClient.TransportStatusCallbackPool mCallbackPool = new com.android.server.backup.transport.BackupTransportClient.TransportStatusCallbackPool();
    private final com.android.server.backup.transport.BackupTransportClient.TransportFutures mTransportFutures = new com.android.server.backup.transport.BackupTransportClient.TransportFutures();

    /* JADX WARN: Multi-variable type inference failed */
    BackupTransportClient(com.android.internal.backup.IBackupTransport iBackupTransport) {
        this.mTransportBinder = iBackupTransport;
    }

    public java.lang.String name() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.String> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.name(resultFuture);
        return (java.lang.String) getFutureResult(resultFuture);
    }

    public android.content.Intent configurationIntent() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<android.content.Intent> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.configurationIntent(resultFuture);
        return (android.content.Intent) getFutureResult(resultFuture);
    }

    public java.lang.String currentDestinationString() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.String> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.currentDestinationString(resultFuture);
        return (java.lang.String) getFutureResult(resultFuture);
    }

    public android.content.Intent dataManagementIntent() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<android.content.Intent> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.dataManagementIntent(resultFuture);
        return (android.content.Intent) getFutureResult(resultFuture);
    }

    public java.lang.CharSequence dataManagementIntentLabel() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.CharSequence> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.dataManagementIntentLabel(resultFuture);
        return (java.lang.CharSequence) getFutureResult(resultFuture);
    }

    public java.lang.String transportDirName() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.String> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.transportDirName(resultFuture);
        return (java.lang.String) getFutureResult(resultFuture);
    }

    public int initializeDevice() throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.initializeDevice(callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public int clearBackupData(android.content.pm.PackageInfo packageInfo) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.clearBackupData(packageInfo, callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public int finishBackup() throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.finishBackup(callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public long requestBackupTime() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.Long> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.requestBackupTime(resultFuture);
        java.lang.Long result = (java.lang.Long) getFutureResult(resultFuture);
        if (result == null) {
            return -1000L;
        }
        return result.longValue();
    }

    public int performBackup(android.content.pm.PackageInfo packageInfo, android.os.ParcelFileDescriptor inFd, int flags) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.performBackup(packageInfo, inFd, flags, callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public java.util.List<android.app.backup.RestoreSet> getAvailableRestoreSets() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.util.List<android.app.backup.RestoreSet>> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getAvailableRestoreSets(resultFuture);
        java.util.List<android.app.backup.RestoreSet> result = (java.util.List) getFutureResult(resultFuture);
        return result;
    }

    public long getCurrentRestoreSet() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.Long> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getCurrentRestoreSet(resultFuture);
        java.lang.Long result = (java.lang.Long) getFutureResult(resultFuture);
        if (result == null) {
            return -1000L;
        }
        return result.longValue();
    }

    public int startRestore(long token, android.content.pm.PackageInfo[] packages) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.startRestore(token, packages, callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public android.app.backup.RestoreDescription nextRestorePackage() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<android.app.backup.RestoreDescription> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.nextRestorePackage(resultFuture);
        return (android.app.backup.RestoreDescription) getFutureResult(resultFuture);
    }

    public int getRestoreData(android.os.ParcelFileDescriptor outFd) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.getRestoreData(outFd, callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public void finishRestore() throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.finishRestore(callback);
            callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public long requestFullBackupTime() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.Long> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.requestFullBackupTime(resultFuture);
        java.lang.Long result = (java.lang.Long) getFutureResult(resultFuture);
        if (result == null) {
            return -1000L;
        }
        return result.longValue();
    }

    public int performFullBackup(android.content.pm.PackageInfo targetPackage, android.os.ParcelFileDescriptor socket, int flags) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.performFullBackup(targetPackage, socket, flags, callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public int checkFullBackupSize(long size) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.checkFullBackupSize(size, callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public int sendBackupData(int numBytes) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        this.mTransportBinder.sendBackupData(numBytes, callback);
        try {
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public void cancelFullBackup() throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.cancelFullBackup(callback);
            callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public boolean isAppEligibleForBackup(android.content.pm.PackageInfo targetPackage, boolean isFullBackup) throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.Boolean> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.isAppEligibleForBackup(targetPackage, isFullBackup, resultFuture);
        java.lang.Boolean result = (java.lang.Boolean) getFutureResult(resultFuture);
        return result != null && result.booleanValue();
    }

    public long getBackupQuota(java.lang.String packageName, boolean isFullBackup) throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.Long> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getBackupQuota(packageName, isFullBackup, resultFuture);
        java.lang.Long result = (java.lang.Long) getFutureResult(resultFuture);
        if (result == null) {
            return -1000L;
        }
        return result.longValue();
    }

    public int getNextFullRestoreDataChunk(android.os.ParcelFileDescriptor socket) throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.getNextFullRestoreDataChunk(socket, callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public int abortFullRestore() throws android.os.RemoteException {
        com.android.server.backup.transport.TransportStatusCallback callback = this.mCallbackPool.acquire();
        try {
            this.mTransportBinder.abortFullRestore(callback);
            return callback.getOperationStatus();
        } finally {
            this.mCallbackPool.recycle(callback);
        }
    }

    public int getTransportFlags() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<java.lang.Integer> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getTransportFlags(resultFuture);
        java.lang.Integer result = (java.lang.Integer) getFutureResult(resultFuture);
        if (result == null) {
            return -1000;
        }
        return result.intValue();
    }

    public android.app.backup.IBackupManagerMonitor getBackupManagerMonitor() throws android.os.RemoteException {
        com.android.internal.infra.AndroidFuture<android.app.backup.IBackupManagerMonitor> resultFuture = this.mTransportFutures.newFuture();
        this.mTransportBinder.getBackupManagerMonitor(resultFuture);
        return android.app.backup.IBackupManagerMonitor.Stub.asInterface((android.os.IBinder) getFutureResult(resultFuture));
    }

    void onBecomingUnusable() {
        this.mCallbackPool.cancelActiveCallbacks();
        this.mTransportFutures.cancelActiveFutures();
    }

    private <T> T getFutureResult(com.android.internal.infra.AndroidFuture<T> androidFuture) {
        try {
            try {
                return (T) androidFuture.get(com.android.server.backup.BackupAndRestoreFeatureFlags.getBackupTransportFutureTimeoutMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException | java.util.concurrent.CancellationException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.w(TAG, "Failed to get result from transport:", e);
                this.mTransportFutures.remove(androidFuture);
                return null;
            }
        } finally {
            this.mTransportFutures.remove(androidFuture);
        }
    }

    private static class TransportFutures {
        private final java.util.Set<com.android.internal.infra.AndroidFuture<?>> mActiveFutures;
        private final java.lang.Object mActiveFuturesLock;

        private TransportFutures() {
            this.mActiveFuturesLock = new java.lang.Object();
            this.mActiveFutures = new java.util.HashSet();
        }

        <T> com.android.internal.infra.AndroidFuture<T> newFuture() {
            com.android.internal.infra.AndroidFuture<T> future = new com.android.internal.infra.AndroidFuture<>();
            synchronized (this.mActiveFuturesLock) {
                this.mActiveFutures.add(future);
            }
            return future;
        }

        <T> void remove(com.android.internal.infra.AndroidFuture<T> future) {
            synchronized (this.mActiveFuturesLock) {
                this.mActiveFutures.remove(future);
            }
        }

        void cancelActiveFutures() {
            synchronized (this.mActiveFuturesLock) {
                for (com.android.internal.infra.AndroidFuture<?> future : this.mActiveFutures) {
                    try {
                        future.cancel(true);
                    } catch (java.util.concurrent.CancellationException e) {
                    }
                }
                this.mActiveFutures.clear();
            }
        }
    }

    private static class TransportStatusCallbackPool {
        private static final int MAX_POOL_SIZE = 100;
        private final java.util.Set<com.android.server.backup.transport.TransportStatusCallback> mActiveCallbacks;
        private final java.util.Queue<com.android.server.backup.transport.TransportStatusCallback> mCallbackPool;
        private final java.lang.Object mPoolLock;

        private TransportStatusCallbackPool() {
            this.mPoolLock = new java.lang.Object();
            this.mCallbackPool = new java.util.ArrayDeque();
            this.mActiveCallbacks = new java.util.HashSet();
        }

        com.android.server.backup.transport.TransportStatusCallback acquire() {
            com.android.server.backup.transport.TransportStatusCallback callback;
            synchronized (this.mPoolLock) {
                callback = this.mCallbackPool.poll();
                if (callback == null) {
                    callback = new com.android.server.backup.transport.TransportStatusCallback();
                }
                callback.reset();
                this.mActiveCallbacks.add(callback);
            }
            return callback;
        }

        void recycle(com.android.server.backup.transport.TransportStatusCallback callback) {
            synchronized (this.mPoolLock) {
                this.mActiveCallbacks.remove(callback);
                if (this.mCallbackPool.size() > 100) {
                    android.util.Slog.d(com.android.server.backup.transport.BackupTransportClient.TAG, "TransportStatusCallback pool size exceeded");
                } else {
                    this.mCallbackPool.add(callback);
                }
            }
        }

        void cancelActiveCallbacks() {
            synchronized (this.mPoolLock) {
                for (com.android.server.backup.transport.TransportStatusCallback callback : this.mActiveCallbacks) {
                    try {
                        callback.onOperationCompleteWithStatus(-1000);
                        callback.getOperationStatus();
                    } catch (android.os.RemoteException e) {
                    }
                    if (this.mCallbackPool.size() < 100) {
                        this.mCallbackPool.add(callback);
                    }
                }
                this.mActiveCallbacks.clear();
            }
        }
    }
}
