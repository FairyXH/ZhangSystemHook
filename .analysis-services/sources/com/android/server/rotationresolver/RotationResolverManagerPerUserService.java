package com.android.server.rotationresolver;

/* JADX INFO: loaded from: classes3.dex */
final class RotationResolverManagerPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.rotationresolver.RotationResolverManagerPerUserService, com.android.server.rotationresolver.RotationResolverManagerService> {
    private static final long CONNECTION_TTL_MILLIS = 60000;
    private static final java.lang.String TAG = com.android.server.rotationresolver.RotationResolverManagerPerUserService.class.getSimpleName();
    private android.content.ComponentName mComponentName;
    com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest mCurrentRequest;
    private com.android.internal.util.LatencyTracker mLatencyTracker;
    com.android.server.rotationresolver.RemoteRotationResolverService mRemoteService;

    RotationResolverManagerPerUserService(com.android.server.rotationresolver.RotationResolverManagerService main, java.lang.Object lock, int userId) {
        super(main, lock, userId);
        this.mLatencyTracker = com.android.internal.util.LatencyTracker.getInstance(getContext());
    }

    void destroyLocked() {
        if (isVerbose()) {
            android.util.Slog.v(TAG, "destroyLocked()");
        }
        if (this.mCurrentRequest == null) {
            return;
        }
        android.util.Slog.d(TAG, "Trying to cancel the remote request. Reason: Service destroyed.");
        cancelLocked();
        if (this.mRemoteService != null) {
            this.mRemoteService.unbind();
            this.mRemoteService = null;
        }
    }

    void resolveRotationLocked(final android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal callbackInternal, android.service.rotationresolver.RotationResolutionRequest request, android.os.CancellationSignal cancellationSignalInternal) {
        if (!isServiceAvailableLocked()) {
            android.util.Slog.w(TAG, "Service is not available at this moment.");
            callbackInternal.onFailure(0);
            com.android.server.rotationresolver.RotationResolverManagerService.logRotationStats(request.getProposedRotation(), request.getCurrentRotation(), 7);
            return;
        }
        ensureRemoteServiceInitiated();
        if (this.mCurrentRequest != null && !this.mCurrentRequest.mIsFulfilled) {
            cancelLocked();
        }
        synchronized (this.mLock) {
            this.mLatencyTracker.onActionStart(9);
        }
        android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal wrapper = new android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal() { // from class: com.android.server.rotationresolver.RotationResolverManagerPerUserService.1
            public void onSuccess(int result) {
                synchronized (com.android.server.rotationresolver.RotationResolverManagerPerUserService.this.mLock) {
                    com.android.server.rotationresolver.RotationResolverManagerPerUserService.this.mLatencyTracker.onActionEnd(9);
                }
                callbackInternal.onSuccess(result);
            }

            public void onFailure(int error) {
                synchronized (com.android.server.rotationresolver.RotationResolverManagerPerUserService.this.mLock) {
                    com.android.server.rotationresolver.RotationResolverManagerPerUserService.this.mLatencyTracker.onActionEnd(9);
                }
                callbackInternal.onFailure(error);
            }
        };
        this.mCurrentRequest = new com.android.server.rotationresolver.RemoteRotationResolverService.RotationRequest(wrapper, request, cancellationSignalInternal, this.mLock);
        cancellationSignalInternal.setOnCancelListener(new android.os.CancellationSignal.OnCancelListener() { // from class: com.android.server.rotationresolver.RotationResolverManagerPerUserService$$ExternalSyntheticLambda0
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                this.f$0.lambda$resolveRotationLocked$0();
            }
        });
        this.mRemoteService.resolveRotation(this.mCurrentRequest);
        this.mCurrentRequest.mIsDispatched = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resolveRotationLocked$0() {
        synchronized (this.mLock) {
            if (this.mCurrentRequest != null && !this.mCurrentRequest.mIsFulfilled) {
                android.util.Slog.d(TAG, "Trying to cancel the remote request. Reason: Client cancelled.");
                this.mCurrentRequest.cancelInternal();
            }
        }
    }

    private void ensureRemoteServiceInitiated() {
        if (this.mRemoteService == null) {
            this.mRemoteService = new com.android.server.rotationresolver.RemoteRotationResolverService(getContext(), this.mComponentName, getUserId(), 60000L);
        }
    }

    android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    boolean isServiceAvailableLocked() {
        if (this.mComponentName == null) {
            this.mComponentName = updateServiceInfoLocked();
        }
        return this.mComponentName != null;
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
            if (serviceInfo != null) {
                java.lang.String permission = serviceInfo.permission;
                if (!"android.permission.BIND_ROTATION_RESOLVER_SERVICE".equals(permission)) {
                    throw new java.lang.SecurityException(java.lang.String.format("Service %s requires %s permission. Found %s permission", serviceInfo.getComponentName(), "android.permission.BIND_ROTATION_RESOLVER_SERVICE", serviceInfo.permission));
                }
            }
            return serviceInfo;
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    private void cancelLocked() {
        if (this.mCurrentRequest == null) {
            return;
        }
        this.mCurrentRequest.cancelInternal();
        this.mCurrentRequest = null;
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
        dumpInternal(new android.util.IndentingPrintWriter(pw, "  "));
    }

    void dumpInternal(android.util.IndentingPrintWriter ipw) {
        synchronized (this.mLock) {
            if (this.mRemoteService != null) {
                this.mRemoteService.dump("", ipw);
            }
            if (this.mCurrentRequest != null) {
                this.mCurrentRequest.dump(ipw);
            }
        }
    }
}
