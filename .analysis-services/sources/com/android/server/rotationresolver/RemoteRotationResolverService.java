package com.android.server.rotationresolver;

/* JADX INFO: loaded from: classes3.dex */
class RemoteRotationResolverService extends com.android.internal.infra.ServiceConnector.Impl<android.service.rotationresolver.IRotationResolverService> {
    private static final java.lang.String TAG = com.android.server.rotationresolver.RemoteRotationResolverService.class.getSimpleName();
    private final long mIdleUnbindTimeoutMs;

    RemoteRotationResolverService(android.content.Context context, android.content.ComponentName serviceName, int userId, long idleUnbindTimeoutMs) {
        super(context, new android.content.Intent("android.service.rotationresolver.RotationResolverService").setComponent(serviceName), 67112960, userId, new java.util.function.Function() { // from class: com.android.server.rotationresolver.RemoteRotationResolverService$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.rotationresolver.IRotationResolverService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mIdleUnbindTimeoutMs = idleUnbindTimeoutMs;
        connect();
    }

    protected long getAutoDisconnectTimeoutMs() {
        return -1L;
    }

    public void resolveRotation(final com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest request) {
        final android.service.rotationresolver.RotationResolutionRequest remoteRequest = request.mRemoteRequest;
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.rotationresolver.RemoteRotationResolverService$$ExternalSyntheticLambda0
            public final void runNoResult(java.lang.Object obj) {
                android.service.rotationresolver.IRotationResolverService iRotationResolverService = (android.service.rotationresolver.IRotationResolverService) obj;
                iRotationResolverService.resolveRotation(request.mIRotationResolverCallback, remoteRequest);
            }
        });
        getJobHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.rotationresolver.RemoteRotationResolverService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.rotationresolver.RemoteRotationResolverService.lambda$resolveRotation$1(request);
            }
        }, request.mRemoteRequest.getTimeoutMillis());
    }

    static /* synthetic */ void lambda$resolveRotation$1(com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest request) {
        synchronized (request.mLock) {
            if (!request.mIsFulfilled) {
                request.mCallbackInternal.onFailure(1);
                android.util.Slog.d(TAG, "Trying to cancel the remote request. Reason: Timed out.");
                request.cancelInternal();
            }
        }
    }

    static final class RotationRequest {
        final android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal mCallbackInternal;
        private android.os.ICancellationSignal mCancellation;
        private final android.os.CancellationSignal mCancellationSignalInternal;
        boolean mIsDispatched;
        boolean mIsFulfilled;
        private final java.lang.Object mLock;
        final android.service.rotationresolver.RotationResolutionRequest mRemoteRequest;
        private final android.service.rotationresolver.IRotationResolverCallback mIRotationResolverCallback = new com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest.RotationResolverCallback(this);
        private final long mRequestStartTimeMillis = android.os.SystemClock.elapsedRealtime();

        RotationRequest(android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal callbackInternal, android.service.rotationresolver.RotationResolutionRequest request, android.os.CancellationSignal cancellationSignal, java.lang.Object lock) {
            this.mCallbackInternal = callbackInternal;
            this.mRemoteRequest = request;
            this.mCancellationSignalInternal = cancellationSignal;
            this.mLock = lock;
        }

        void cancelInternal() {
            android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.rotationresolver.RemoteRotationResolverService$RotationRequest$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$cancelInternal$0();
                }
            });
            this.mCallbackInternal.onFailure(0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cancelInternal$0() {
            synchronized (this.mLock) {
                if (this.mIsFulfilled) {
                    return;
                }
                this.mIsFulfilled = true;
                try {
                    if (this.mCancellation != null) {
                        this.mCancellation.cancel();
                        this.mCancellation = null;
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "Failed to cancel request in remote service.");
                }
            }
        }

        void dump(android.util.IndentingPrintWriter ipw) {
            ipw.increaseIndent();
            ipw.println("is dispatched=" + this.mIsDispatched);
            ipw.println("is fulfilled:=" + this.mIsFulfilled);
            ipw.decreaseIndent();
        }

        private static class RotationResolverCallback extends android.service.rotationresolver.IRotationResolverCallback.Stub {
            private final java.lang.ref.WeakReference<com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest> mRequestWeakReference;

            RotationResolverCallback(com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest request) {
                this.mRequestWeakReference = new java.lang.ref.WeakReference<>(request);
            }

            public void onSuccess(int rotation) {
                com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest request = this.mRequestWeakReference.get();
                synchronized (request.mLock) {
                    if (request.mIsFulfilled) {
                        android.util.Slog.w(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "Callback received after the rotation request is fulfilled.");
                        return;
                    }
                    request.mIsFulfilled = true;
                    request.mCallbackInternal.onSuccess(rotation);
                    long timeToCalculate = android.os.SystemClock.elapsedRealtime() - request.mRequestStartTimeMillis;
                    com.android.server.rotationresolver.RotationResolverManagerService.logRotationStatsWithTimeToCalculate(request.mRemoteRequest.getProposedRotation(), request.mRemoteRequest.getCurrentRotation(), com.android.server.rotationresolver.RotationResolverManagerService.surfaceRotationToProto(rotation), timeToCalculate);
                    android.util.Slog.d(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "onSuccess:" + rotation);
                    android.util.Slog.d(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "timeToCalculate:" + timeToCalculate);
                }
            }

            public void onFailure(int error) {
                com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest request = this.mRequestWeakReference.get();
                synchronized (request.mLock) {
                    if (request.mIsFulfilled) {
                        android.util.Slog.w(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "Callback received after the rotation request is fulfilled.");
                        return;
                    }
                    request.mIsFulfilled = true;
                    request.mCallbackInternal.onFailure(error);
                    long timeToCalculate = android.os.SystemClock.elapsedRealtime() - request.mRequestStartTimeMillis;
                    com.android.server.rotationresolver.RotationResolverManagerService.logRotationStatsWithTimeToCalculate(request.mRemoteRequest.getProposedRotation(), request.mRemoteRequest.getCurrentRotation(), com.android.server.rotationresolver.RotationResolverManagerService.errorCodeToProto(error), timeToCalculate);
                    android.util.Slog.d(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "onFailure:" + error);
                    android.util.Slog.d(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "timeToCalculate:" + timeToCalculate);
                }
            }

            public void onCancellable(android.os.ICancellationSignal cancellation) {
                com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest request = this.mRequestWeakReference.get();
                synchronized (request.mLock) {
                    request.mCancellation = cancellation;
                    if (request.mCancellationSignalInternal.isCanceled()) {
                        try {
                            cancellation.cancel();
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.w(com.android.server.rotationresolver.RemoteRotationResolverService.TAG, "Failed to cancel the remote request.");
                        }
                    }
                }
            }
        }
    }
}
