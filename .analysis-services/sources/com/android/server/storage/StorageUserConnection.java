package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public final class StorageUserConnection {
    private static final int DEFAULT_REMOTE_TIMEOUT_SECONDS = 20;
    private static final java.lang.String TAG = "StorageUserConnection";
    private final android.content.Context mContext;
    private final android.os.HandlerThread mHandlerThread;
    private final com.android.server.storage.StorageSessionController mSessionController;
    private final int mUserId;
    private final java.lang.Object mSessionsLock = new java.lang.Object();
    private final com.android.server.storage.StorageUserConnection.ActiveConnection mActiveConnection = new com.android.server.storage.StorageUserConnection.ActiveConnection();
    private final java.util.Map<java.lang.String, com.android.server.storage.StorageUserConnection.Session> mSessions = new java.util.HashMap();
    private final android.util.SparseArray<java.lang.Integer> mUidsBlockedOnIo = new android.util.SparseArray<>();
    private final android.os.storage.StorageManagerInternal mSmInternal = (android.os.storage.StorageManagerInternal) com.android.server.LocalServices.getService(android.os.storage.StorageManagerInternal.class);

    @java.lang.FunctionalInterface
    interface AsyncStorageServiceCall {
        void run(android.service.storage.IExternalStorageService iExternalStorageService, android.os.RemoteCallback remoteCallback) throws android.os.RemoteException;
    }

    public StorageUserConnection(android.content.Context context, int userId, com.android.server.storage.StorageSessionController controller) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mUserId = com.android.internal.util.Preconditions.checkArgumentNonnegative(userId);
        this.mSessionController = controller;
        this.mHandlerThread = new android.os.HandlerThread("StorageUserConnectionThread-" + this.mUserId);
        this.mHandlerThread.start();
    }

    public void startSession(java.lang.String sessionId, android.os.ParcelFileDescriptor pfd, java.lang.String upperPath, java.lang.String lowerPath) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        java.util.Objects.requireNonNull(sessionId);
        java.util.Objects.requireNonNull(pfd);
        java.util.Objects.requireNonNull(upperPath);
        java.util.Objects.requireNonNull(lowerPath);
        com.android.server.storage.StorageUserConnection.Session session = new com.android.server.storage.StorageUserConnection.Session(sessionId, upperPath, lowerPath);
        synchronized (this.mSessionsLock) {
            com.android.internal.util.Preconditions.checkArgument(!this.mSessions.containsKey(sessionId));
            this.mSessions.put(sessionId, session);
        }
        this.mActiveConnection.startSession(session, pfd);
    }

    public void notifyVolumeStateChanged(java.lang.String sessionId, android.os.storage.StorageVolume vol) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        java.util.Objects.requireNonNull(sessionId);
        java.util.Objects.requireNonNull(vol);
        synchronized (this.mSessionsLock) {
            if (!this.mSessions.containsKey(sessionId)) {
                android.util.Slog.i(TAG, "No session found for sessionId: " + sessionId);
            } else {
                this.mActiveConnection.notifyVolumeStateChanged(sessionId, vol);
            }
        }
    }

    public void freeCache(java.lang.String volumeUuid, long bytes) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        synchronized (this.mSessionsLock) {
            for (java.lang.String sessionId : this.mSessions.keySet()) {
                this.mActiveConnection.freeCache(sessionId, volumeUuid, bytes);
            }
        }
    }

    public void notifyAnrDelayStarted(java.lang.String packageName, int uid, int tid, int reason) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        java.util.List<java.lang.String> primarySessionIds = this.mSmInternal.getPrimaryVolumeIds();
        synchronized (this.mSessionsLock) {
            for (java.lang.String sessionId : this.mSessions.keySet()) {
                if (primarySessionIds.contains(sessionId)) {
                    this.mActiveConnection.notifyAnrDelayStarted(packageName, uid, tid, reason);
                    return;
                }
            }
        }
    }

    public com.android.server.storage.StorageUserConnection.Session removeSession(java.lang.String sessionId) {
        com.android.server.storage.StorageUserConnection.Session sessionRemove;
        synchronized (this.mSessionsLock) {
            this.mUidsBlockedOnIo.clear();
            sessionRemove = this.mSessions.remove(sessionId);
        }
        return sessionRemove;
    }

    public void removeSessionAndWait(java.lang.String sessionId) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
        com.android.server.storage.StorageUserConnection.Session session = removeSession(sessionId);
        if (session == null) {
            android.util.Slog.i(TAG, "No session found for id: " + sessionId);
        } else {
            android.util.Slog.i(TAG, "Waiting for session end " + session + " ...");
            this.mActiveConnection.endSession(session);
        }
    }

    public void resetUserSessions() {
        synchronized (this.mSessionsLock) {
            if (this.mSessions.isEmpty()) {
                return;
            }
            this.mSmInternal.resetUser(this.mUserId);
        }
    }

    public void removeAllSessions() {
        synchronized (this.mSessionsLock) {
            android.util.Slog.i(TAG, "Removing  " + this.mSessions.size() + " sessions for user: " + this.mUserId + "...");
            this.mSessions.clear();
        }
    }

    public void close() {
        this.mActiveConnection.close();
        this.mHandlerThread.quit();
    }

    public java.util.Set<java.lang.String> getAllSessionIds() {
        java.util.HashSet hashSet;
        synchronized (this.mSessionsLock) {
            hashSet = new java.util.HashSet(this.mSessions.keySet());
        }
        return hashSet;
    }

    public void notifyAppIoBlocked(java.lang.String volumeUuid, int uid, int tid, int reason) {
        synchronized (this.mSessionsLock) {
            int ioBlockedCounter = this.mUidsBlockedOnIo.get(uid, 0).intValue();
            this.mUidsBlockedOnIo.put(uid, java.lang.Integer.valueOf(ioBlockedCounter + 1));
        }
    }

    public void notifyAppIoResumed(java.lang.String volumeUuid, int uid, int tid, int reason) {
        synchronized (this.mSessionsLock) {
            int ioBlockedCounter = this.mUidsBlockedOnIo.get(uid, 0).intValue();
            if (ioBlockedCounter == 0) {
                android.util.Slog.w(TAG, "Unexpected app IO resumption for uid: " + uid);
            }
            if (ioBlockedCounter <= 1) {
                this.mUidsBlockedOnIo.remove(uid);
            } else {
                this.mUidsBlockedOnIo.put(uid, java.lang.Integer.valueOf(ioBlockedCounter - 1));
            }
        }
    }

    public boolean isAppIoBlocked(int uid) {
        boolean zContains;
        synchronized (this.mSessionsLock) {
            zContains = this.mUidsBlockedOnIo.contains(uid);
        }
        return zContains;
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ActiveConnection implements java.lang.AutoCloseable {
        private final java.lang.Object mLock;
        private final java.util.ArrayList<java.util.concurrent.CompletableFuture<java.lang.Void>> mOutstandingOps;
        private java.util.concurrent.CompletableFuture<android.service.storage.IExternalStorageService> mRemoteFuture;
        private android.content.ServiceConnection mServiceConnection;

        private ActiveConnection() {
            this.mLock = new java.lang.Object();
            this.mOutstandingOps = new java.util.ArrayList<>();
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            android.content.ServiceConnection oldConnection;
            synchronized (this.mLock) {
                android.util.Slog.i(com.android.server.storage.StorageUserConnection.TAG, "Closing connection for user " + com.android.server.storage.StorageUserConnection.this.mUserId);
                oldConnection = this.mServiceConnection;
                this.mServiceConnection = null;
                if (this.mRemoteFuture != null) {
                    this.mRemoteFuture.cancel(true);
                    this.mRemoteFuture = null;
                }
                for (java.util.concurrent.CompletableFuture<java.lang.Void> op : this.mOutstandingOps) {
                    op.cancel(true);
                }
                this.mOutstandingOps.clear();
            }
            if (oldConnection != null) {
                try {
                    com.android.server.storage.StorageUserConnection.this.mContext.unbindService(oldConnection);
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(com.android.server.storage.StorageUserConnection.TAG, "Failed to unbind service", e);
                }
            }
        }

        private void asyncBestEffort(java.util.function.Consumer<android.service.storage.IExternalStorageService> consumer) {
            synchronized (this.mLock) {
                if (this.mRemoteFuture == null) {
                    android.util.Slog.w(com.android.server.storage.StorageUserConnection.TAG, "Dropping async request service is not bound");
                    return;
                }
                android.service.storage.IExternalStorageService service = this.mRemoteFuture.getNow(null);
                if (service == null) {
                    android.util.Slog.w(com.android.server.storage.StorageUserConnection.TAG, "Dropping async request service is not connected");
                } else {
                    consumer.accept(service);
                }
            }
        }

        private void waitForAsyncVoid(com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall asyncCall) throws java.lang.Exception {
            final java.util.concurrent.CompletableFuture<java.lang.Void> opFuture = new java.util.concurrent.CompletableFuture<>();
            android.os.RemoteCallback callback = new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.storage.StorageUserConnection$ActiveConnection$$ExternalSyntheticLambda2
                public final void onResult(android.os.Bundle bundle) {
                    this.f$0.lambda$waitForAsyncVoid$0(opFuture, bundle);
                }
            });
            waitForAsync(asyncCall, callback, opFuture, this.mOutstandingOps, 20L);
        }

        private <T> T waitForAsync(final com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall asyncStorageServiceCall, final android.os.RemoteCallback remoteCallback, final java.util.concurrent.CompletableFuture<T> completableFuture, java.util.ArrayList<java.util.concurrent.CompletableFuture<T>> arrayList, long j) throws java.lang.Exception {
            java.util.concurrent.CompletableFuture<android.service.storage.IExternalStorageService> completableFutureConnectIfNeeded = connectIfNeeded();
            try {
                synchronized (this.mLock) {
                    arrayList.add(completableFuture);
                }
                T t = (T) completableFutureConnectIfNeeded.thenCompose(new java.util.function.Function() { // from class: com.android.server.storage.StorageUserConnection$ActiveConnection$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.storage.StorageUserConnection.ActiveConnection.lambda$waitForAsync$1(asyncStorageServiceCall, remoteCallback, completableFuture, (android.service.storage.IExternalStorageService) obj);
                    }
                }).get(j, java.util.concurrent.TimeUnit.SECONDS);
                synchronized (this.mLock) {
                    arrayList.remove(completableFuture);
                }
                return t;
            } catch (java.lang.Throwable th) {
                synchronized (this.mLock) {
                    arrayList.remove(completableFuture);
                    throw th;
                }
            }
        }

        static /* synthetic */ java.util.concurrent.CompletionStage lambda$waitForAsync$1(com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall asyncCall, android.os.RemoteCallback callback, java.util.concurrent.CompletableFuture opFuture, android.service.storage.IExternalStorageService service) {
            try {
                asyncCall.run(service, callback);
            } catch (android.os.RemoteException e) {
                opFuture.completeExceptionally(e);
            }
            return opFuture;
        }

        public void startSession(final com.android.server.storage.StorageUserConnection.Session session, final android.os.ParcelFileDescriptor fd) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
            try {
                try {
                    waitForAsyncVoid(new com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall() { // from class: com.android.server.storage.StorageUserConnection$ActiveConnection$$ExternalSyntheticLambda5
                        @Override // com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall
                        public final void run(android.service.storage.IExternalStorageService iExternalStorageService, android.os.RemoteCallback remoteCallback) {
                            com.android.server.storage.StorageUserConnection.Session session2 = session;
                            iExternalStorageService.startSession(session2.sessionId, 3, fd, session2.upperPath, session2.lowerPath, remoteCallback);
                        }
                    });
                    try {
                        fd.close();
                    } catch (java.io.IOException e) {
                    }
                } catch (java.lang.Exception e2) {
                    throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("Failed to start session: " + session, e2);
                }
            } catch (java.lang.Throwable th) {
                try {
                    fd.close();
                } catch (java.io.IOException e3) {
                }
                throw th;
            }
        }

        public void endSession(final com.android.server.storage.StorageUserConnection.Session session) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
            try {
                waitForAsyncVoid(new com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall() { // from class: com.android.server.storage.StorageUserConnection$ActiveConnection$$ExternalSyntheticLambda6
                    @Override // com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall
                    public final void run(android.service.storage.IExternalStorageService iExternalStorageService, android.os.RemoteCallback remoteCallback) {
                        iExternalStorageService.endSession(session.sessionId, remoteCallback);
                    }
                });
            } catch (java.lang.Exception e) {
                throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("Failed to end session: " + session, e);
            }
        }

        public void notifyVolumeStateChanged(final java.lang.String sessionId, final android.os.storage.StorageVolume vol) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
            try {
                waitForAsyncVoid(new com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall() { // from class: com.android.server.storage.StorageUserConnection$ActiveConnection$$ExternalSyntheticLambda4
                    @Override // com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall
                    public final void run(android.service.storage.IExternalStorageService iExternalStorageService, android.os.RemoteCallback remoteCallback) {
                        iExternalStorageService.notifyVolumeStateChanged(sessionId, vol, remoteCallback);
                    }
                });
            } catch (java.lang.Exception e) {
                throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("Failed to notify volume state changed for vol : " + vol, e);
            }
        }

        public void freeCache(final java.lang.String sessionId, final java.lang.String volumeUuid, final long bytes) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
            try {
                waitForAsyncVoid(new com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall() { // from class: com.android.server.storage.StorageUserConnection$ActiveConnection$$ExternalSyntheticLambda1
                    @Override // com.android.server.storage.StorageUserConnection.AsyncStorageServiceCall
                    public final void run(android.service.storage.IExternalStorageService iExternalStorageService, android.os.RemoteCallback remoteCallback) {
                        iExternalStorageService.freeCache(sessionId, volumeUuid, bytes, remoteCallback);
                    }
                });
            } catch (java.lang.Exception e) {
                throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("Failed to free " + bytes + " bytes for volumeUuid : " + volumeUuid, e);
            }
        }

        public void notifyAnrDelayStarted(final java.lang.String packgeName, final int uid, final int tid, final int reason) throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
            asyncBestEffort(new java.util.function.Consumer() { // from class: com.android.server.storage.StorageUserConnection$ActiveConnection$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.storage.StorageUserConnection.ActiveConnection.lambda$notifyAnrDelayStarted$6(packgeName, uid, tid, reason, (android.service.storage.IExternalStorageService) obj);
                }
            });
        }

        static /* synthetic */ void lambda$notifyAnrDelayStarted$6(java.lang.String packgeName, int uid, int tid, int reason, android.service.storage.IExternalStorageService service) {
            try {
                service.notifyAnrDelayStarted(packgeName, uid, tid, reason);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.storage.StorageUserConnection.TAG, "Failed to notify ANR delay started", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: setResult, reason: merged with bridge method [inline-methods] */
        public void lambda$waitForAsyncVoid$0(android.os.Bundle result, java.util.concurrent.CompletableFuture<java.lang.Void> future) {
            android.os.ParcelableException ex = (android.os.ParcelableException) result.getParcelable("android.service.storage.extra.error", android.os.ParcelableException.class);
            if (ex != null) {
                future.completeExceptionally(ex);
            } else {
                future.complete(null);
            }
        }

        private java.util.concurrent.CompletableFuture<android.service.storage.IExternalStorageService> connectIfNeeded() throws com.android.server.storage.StorageSessionController.ExternalStorageServiceException {
            android.content.ComponentName name = com.android.server.storage.StorageUserConnection.this.mSessionController.getExternalStorageServiceComponentName();
            if (name == null) {
                throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("Not ready to bind to the ExternalStorageService for user " + com.android.server.storage.StorageUserConnection.this.mUserId);
            }
            synchronized (this.mLock) {
                if (this.mRemoteFuture != null) {
                    return this.mRemoteFuture;
                }
                final java.util.concurrent.CompletableFuture<android.service.storage.IExternalStorageService> future = new java.util.concurrent.CompletableFuture<>();
                this.mServiceConnection = new android.content.ServiceConnection() { // from class: com.android.server.storage.StorageUserConnection.ActiveConnection.1
                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(android.content.ComponentName name2, android.os.IBinder service) {
                        android.util.Slog.i(com.android.server.storage.StorageUserConnection.TAG, "Service: [" + name2 + "] connected. User [" + com.android.server.storage.StorageUserConnection.this.mUserId + "]");
                        handleConnection(service);
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(android.content.ComponentName name2) {
                        android.util.Slog.i(com.android.server.storage.StorageUserConnection.TAG, "Service: [" + name2 + "] disconnected. User [" + com.android.server.storage.StorageUserConnection.this.mUserId + "]");
                        handleDisconnection();
                    }

                    @Override // android.content.ServiceConnection
                    public void onBindingDied(android.content.ComponentName name2) {
                        android.util.Slog.i(com.android.server.storage.StorageUserConnection.TAG, "Service: [" + name2 + "] died. User [" + com.android.server.storage.StorageUserConnection.this.mUserId + "]");
                        handleDisconnection();
                    }

                    @Override // android.content.ServiceConnection
                    public void onNullBinding(android.content.ComponentName name2) {
                        android.util.Slog.wtf(com.android.server.storage.StorageUserConnection.TAG, "Service: [" + name2 + "] is null. User [" + com.android.server.storage.StorageUserConnection.this.mUserId + "]");
                    }

                    private void handleConnection(android.os.IBinder service) {
                        synchronized (com.android.server.storage.StorageUserConnection.ActiveConnection.this.mLock) {
                            future.complete(android.service.storage.IExternalStorageService.Stub.asInterface(service));
                        }
                    }

                    private void handleDisconnection() {
                        com.android.server.storage.StorageUserConnection.ActiveConnection.this.close();
                        com.android.server.storage.StorageUserConnection.this.resetUserSessions();
                    }
                };
                android.util.Slog.i(com.android.server.storage.StorageUserConnection.TAG, "Binding to the ExternalStorageService for user " + com.android.server.storage.StorageUserConnection.this.mUserId);
                if (com.android.server.storage.StorageUserConnection.this.mContext.bindServiceAsUser(new android.content.Intent().setComponent(name), this.mServiceConnection, 65, com.android.server.storage.StorageUserConnection.this.mHandlerThread.getThreadHandler(), android.os.UserHandle.of(com.android.server.storage.StorageUserConnection.this.mUserId))) {
                    android.util.Slog.i(com.android.server.storage.StorageUserConnection.TAG, "Bound to the ExternalStorageService for user " + com.android.server.storage.StorageUserConnection.this.mUserId);
                    this.mRemoteFuture = future;
                    return future;
                }
                throw new com.android.server.storage.StorageSessionController.ExternalStorageServiceException("Failed to bind to the ExternalStorageService for user " + com.android.server.storage.StorageUserConnection.this.mUserId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class Session {
        public final java.lang.String lowerPath;
        public final java.lang.String sessionId;
        public final java.lang.String upperPath;

        Session(java.lang.String sessionId, java.lang.String upperPath, java.lang.String lowerPath) {
            this.sessionId = sessionId;
            this.upperPath = upperPath;
            this.lowerPath = lowerPath;
        }

        public java.lang.String toString() {
            return "[SessionId: " + this.sessionId + ". UpperPath: " + this.upperPath + ". LowerPath: " + this.lowerPath + "]";
        }
    }
}
