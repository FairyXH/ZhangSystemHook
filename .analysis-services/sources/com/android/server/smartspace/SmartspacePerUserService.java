package com.android.server.smartspace;

/* JADX INFO: loaded from: classes3.dex */
public class SmartspacePerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.smartspace.SmartspacePerUserService, com.android.server.smartspace.SmartspaceManagerService> implements com.android.server.smartspace.RemoteSmartspaceService.RemoteSmartspaceServiceCallbacks {
    private static final java.lang.String TAG = com.android.server.smartspace.SmartspacePerUserService.class.getSimpleName();
    private com.android.server.smartspace.RemoteSmartspaceService mRemoteService;
    private final android.util.ArrayMap<android.app.smartspace.SmartspaceSessionId, com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo> mSessionInfos;
    private boolean mZombie;

    protected SmartspacePerUserService(com.android.server.smartspace.SmartspaceManagerService master, java.lang.Object lock, int userId) {
        super(master, lock, userId);
        this.mSessionInfos = new android.util.ArrayMap<>();
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo si = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
            return si;
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected boolean updateLocked(boolean disabled) {
        boolean enabledChanged = super.updateLocked(disabled);
        if (enabledChanged) {
            if (isEnabledLocked()) {
                resurrectSessionsLocked();
            } else {
                updateRemoteServiceLocked();
            }
        }
        return enabledChanged;
    }

    public void onCreateSmartspaceSessionLocked(final android.app.smartspace.SmartspaceConfig smartspaceConfig, final android.app.smartspace.SmartspaceSessionId sessionId, android.os.IBinder token) {
        boolean serviceExists = resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.smartspace.SmartspacePerUserService$$ExternalSyntheticLambda2
            public final void run(android.os.IInterface iInterface) {
                ((android.service.smartspace.ISmartspaceService) iInterface).onCreateSmartspaceSession(smartspaceConfig, sessionId);
            }
        });
        if (serviceExists && !this.mSessionInfos.containsKey(sessionId)) {
            com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo sessionInfo = new com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo(sessionId, smartspaceConfig, token, new android.os.IBinder.DeathRecipient() { // from class: com.android.server.smartspace.SmartspacePerUserService$$ExternalSyntheticLambda3
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$onCreateSmartspaceSessionLocked$1(sessionId);
                }
            });
            if (sessionInfo.linkToDeath()) {
                this.mSessionInfos.put(sessionId, sessionInfo);
            } else {
                onDestroyLocked(sessionId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateSmartspaceSessionLocked$1(android.app.smartspace.SmartspaceSessionId sessionId) {
        synchronized (this.mLock) {
            onDestroyLocked(sessionId);
        }
    }

    public void notifySmartspaceEventLocked(final android.app.smartspace.SmartspaceSessionId sessionId, final android.app.smartspace.SmartspaceTargetEvent event) {
        com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.smartspace.SmartspacePerUserService$$ExternalSyntheticLambda1
            public final void run(android.os.IInterface iInterface) {
                ((android.service.smartspace.ISmartspaceService) iInterface).notifySmartspaceEvent(sessionId, event);
            }
        });
    }

    public void requestSmartspaceUpdateLocked(final android.app.smartspace.SmartspaceSessionId sessionId) {
        com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.smartspace.SmartspacePerUserService$$ExternalSyntheticLambda6
            public final void run(android.os.IInterface iInterface) {
                ((android.service.smartspace.ISmartspaceService) iInterface).requestSmartspaceUpdate(sessionId);
            }
        });
    }

    public void registerSmartspaceUpdatesLocked(final android.app.smartspace.SmartspaceSessionId sessionId, final android.app.smartspace.ISmartspaceCallback callback) {
        com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        boolean serviceExists = resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.smartspace.SmartspacePerUserService$$ExternalSyntheticLambda5
            public final void run(android.os.IInterface iInterface) {
                ((android.service.smartspace.ISmartspaceService) iInterface).registerSmartspaceUpdates(sessionId, callback);
            }
        });
        if (serviceExists) {
            sessionInfo.addCallbackLocked(callback);
        }
    }

    public void unregisterSmartspaceUpdatesLocked(final android.app.smartspace.SmartspaceSessionId sessionId, final android.app.smartspace.ISmartspaceCallback callback) {
        com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        boolean serviceExists = resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.smartspace.SmartspacePerUserService$$ExternalSyntheticLambda4
            public final void run(android.os.IInterface iInterface) {
                ((android.service.smartspace.ISmartspaceService) iInterface).unregisterSmartspaceUpdates(sessionId, callback);
            }
        });
        if (serviceExists) {
            sessionInfo.removeCallbackLocked(callback);
        }
    }

    public void onDestroyLocked(final android.app.smartspace.SmartspaceSessionId sessionId) {
        if (isDebug()) {
            android.util.Slog.d(TAG, "onDestroyLocked(): sessionId=" + sessionId);
        }
        com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo sessionInfo = this.mSessionInfos.remove(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.smartspace.SmartspacePerUserService$$ExternalSyntheticLambda0
            public final void run(android.os.IInterface iInterface) {
                ((android.service.smartspace.ISmartspaceService) iInterface).onDestroySmartspaceSession(sessionId);
            }
        });
        sessionInfo.destroy();
    }

    @Override // com.android.server.smartspace.RemoteSmartspaceService.RemoteSmartspaceServiceCallbacks
    public void onFailureOrTimeout(boolean timedOut) {
        if (isDebug()) {
            android.util.Slog.d(TAG, "onFailureOrTimeout(): timed out=" + timedOut);
        }
    }

    @Override // com.android.server.smartspace.RemoteSmartspaceService.RemoteSmartspaceServiceCallbacks
    public void onConnectedStateChanged(boolean connected) {
        if (isDebug()) {
            android.util.Slog.d(TAG, "onConnectedStateChanged(): connected=" + connected);
        }
        if (connected) {
            synchronized (this.mLock) {
                if (this.mZombie) {
                    if (this.mRemoteService == null) {
                        android.util.Slog.w(TAG, "Cannot resurrect sessions because remote service is null");
                    } else {
                        this.mZombie = false;
                        resurrectSessionsLocked();
                    }
                }
            }
        }
    }

    public void onServiceDied(com.android.server.smartspace.RemoteSmartspaceService service) {
        if (isDebug()) {
            android.util.Slog.w(TAG, "onServiceDied(): service=" + service);
        }
        synchronized (this.mLock) {
            this.mZombie = true;
        }
        updateRemoteServiceLocked();
    }

    private void updateRemoteServiceLocked() {
        if (this.mRemoteService != null) {
            this.mRemoteService.destroy();
            this.mRemoteService = null;
        }
    }

    void onPackageUpdatedLocked() {
        if (isDebug()) {
            android.util.Slog.v(TAG, "onPackageUpdatedLocked()");
        }
        destroyAndRebindRemoteService();
    }

    void onPackageRestartedLocked() {
        if (isDebug()) {
            android.util.Slog.v(TAG, "onPackageRestartedLocked()");
        }
        destroyAndRebindRemoteService();
    }

    private void destroyAndRebindRemoteService() {
        if (this.mRemoteService == null) {
            return;
        }
        if (isDebug()) {
            android.util.Slog.d(TAG, "Destroying the old remote service.");
        }
        this.mRemoteService.destroy();
        this.mRemoteService = null;
        synchronized (this.mLock) {
            this.mZombie = true;
        }
        this.mRemoteService = getRemoteServiceLocked();
        if (this.mRemoteService != null) {
            if (isDebug()) {
                android.util.Slog.d(TAG, "Rebinding to the new remote service.");
            }
            this.mRemoteService.reconnect();
        }
    }

    private void resurrectSessionsLocked() {
        int numSessions = this.mSessionInfos.size();
        if (isDebug()) {
            android.util.Slog.d(TAG, "Resurrecting remote service (" + this.mRemoteService + ") on " + numSessions + " sessions.");
        }
        for (com.android.server.smartspace.SmartspacePerUserService.SmartspaceSessionInfo sessionInfo : this.mSessionInfos.values()) {
            sessionInfo.resurrectSessionLocked(this, sessionInfo.mToken);
        }
    }

    protected boolean resolveService(android.app.smartspace.SmartspaceSessionId sessionId, com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.smartspace.ISmartspaceService> cb) {
        com.android.server.smartspace.RemoteSmartspaceService service = getRemoteServiceLocked();
        if (service != null) {
            service.executeOnResolvedService(cb);
        }
        return service != null;
    }

    private com.android.server.smartspace.RemoteSmartspaceService getRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            java.lang.String serviceName = getComponentNameLocked();
            if (serviceName == null) {
                if (((com.android.server.smartspace.SmartspaceManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "getRemoteServiceLocked(): not set");
                    return null;
                }
                return null;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            this.mRemoteService = new com.android.server.smartspace.RemoteSmartspaceService(getContext(), "android.service.smartspace.SmartspaceService", serviceComponent, this.mUserId, this, ((com.android.server.smartspace.SmartspaceManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.smartspace.SmartspaceManagerService) this.mMaster).verbose);
        }
        return this.mRemoteService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SmartspaceSessionInfo {
        private static final boolean DEBUG = false;
        private final android.os.RemoteCallbackList<android.app.smartspace.ISmartspaceCallback> mCallbacks = new android.os.RemoteCallbackList<>();
        final android.os.IBinder.DeathRecipient mDeathRecipient;
        private final android.app.smartspace.SmartspaceSessionId mSessionId;
        private final android.app.smartspace.SmartspaceConfig mSmartspaceConfig;
        final android.os.IBinder mToken;

        SmartspaceSessionInfo(android.app.smartspace.SmartspaceSessionId id, android.app.smartspace.SmartspaceConfig context, android.os.IBinder token, android.os.IBinder.DeathRecipient deathRecipient) {
            this.mSessionId = id;
            this.mSmartspaceConfig = context;
            this.mToken = token;
            this.mDeathRecipient = deathRecipient;
        }

        void addCallbackLocked(android.app.smartspace.ISmartspaceCallback callback) {
            this.mCallbacks.register(callback);
        }

        void removeCallbackLocked(android.app.smartspace.ISmartspaceCallback callback) {
            this.mCallbacks.unregister(callback);
        }

        boolean linkToDeath() {
            try {
                this.mToken.linkToDeath(this.mDeathRecipient, 0);
                return true;
            } catch (android.os.RemoteException e) {
                return false;
            }
        }

        void destroy() {
            if (this.mToken != null) {
                this.mToken.unlinkToDeath(this.mDeathRecipient, 0);
            }
            this.mCallbacks.kill();
        }

        void resurrectSessionLocked(final com.android.server.smartspace.SmartspacePerUserService service, android.os.IBinder token) {
            this.mCallbacks.getRegisteredCallbackCount();
            service.onCreateSmartspaceSessionLocked(this.mSmartspaceConfig, this.mSessionId, token);
            this.mCallbacks.broadcast(new java.util.function.Consumer() { // from class: com.android.server.smartspace.SmartspacePerUserService$SmartspaceSessionInfo$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$resurrectSessionLocked$0(service, (android.app.smartspace.ISmartspaceCallback) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resurrectSessionLocked$0(com.android.server.smartspace.SmartspacePerUserService service, android.app.smartspace.ISmartspaceCallback callback) {
            service.registerSmartspaceUpdatesLocked(this.mSessionId, callback);
        }
    }
}
