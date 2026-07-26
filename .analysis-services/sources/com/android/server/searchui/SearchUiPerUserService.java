package com.android.server.searchui;

/* JADX INFO: loaded from: classes3.dex */
public class SearchUiPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.searchui.SearchUiPerUserService, com.android.server.searchui.SearchUiManagerService> implements com.android.server.searchui.RemoteSearchUiService.RemoteSearchUiServiceCallbacks {
    private static final java.lang.String TAG = com.android.server.searchui.SearchUiPerUserService.class.getSimpleName();
    private com.android.server.searchui.RemoteSearchUiService mRemoteService;
    private final android.util.ArrayMap<android.app.search.SearchSessionId, com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo> mSessionInfos;
    private boolean mZombie;

    protected SearchUiPerUserService(com.android.server.searchui.SearchUiManagerService master, java.lang.Object lock, int userId) {
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
        if (enabledChanged && !isEnabledLocked()) {
            updateRemoteServiceLocked();
        }
        return enabledChanged;
    }

    public void onCreateSearchSessionLocked(final android.app.search.SearchContext context, final android.app.search.SearchSessionId sessionId, android.os.IBinder token) {
        boolean serviceExists = resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.searchui.SearchUiPerUserService$$ExternalSyntheticLambda1
            public final void run(android.os.IInterface iInterface) {
                ((android.service.search.ISearchUiService) iInterface).onCreateSearchSession(context, sessionId);
            }
        });
        if (serviceExists && !this.mSessionInfos.containsKey(sessionId)) {
            com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo sessionInfo = new com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo(sessionId, context, token, new android.os.IBinder.DeathRecipient() { // from class: com.android.server.searchui.SearchUiPerUserService$$ExternalSyntheticLambda2
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$onCreateSearchSessionLocked$1(sessionId);
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
    public /* synthetic */ void lambda$onCreateSearchSessionLocked$1(android.app.search.SearchSessionId sessionId) {
        synchronized (this.mLock) {
            onDestroyLocked(sessionId);
        }
    }

    public void notifyLocked(final android.app.search.SearchSessionId sessionId, final android.app.search.Query query, final android.app.search.SearchTargetEvent event) {
        com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.searchui.SearchUiPerUserService$$ExternalSyntheticLambda0
            public final void run(android.os.IInterface iInterface) {
                ((android.service.search.ISearchUiService) iInterface).onNotifyEvent(sessionId, query, event);
            }
        });
    }

    public void queryLocked(final android.app.search.SearchSessionId sessionId, final android.app.search.Query input, final android.app.search.ISearchCallback callback) {
        com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.searchui.SearchUiPerUserService$$ExternalSyntheticLambda6
            public final void run(android.os.IInterface iInterface) {
                ((android.service.search.ISearchUiService) iInterface).onQuery(sessionId, input, callback);
            }
        });
    }

    public void registerEmptyQueryResultUpdateCallbackLocked(final android.app.search.SearchSessionId sessionId, final android.app.search.ISearchCallback callback) {
        com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        boolean serviceExists = resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.searchui.SearchUiPerUserService$$ExternalSyntheticLambda3
            public final void run(android.os.IInterface iInterface) {
                ((android.service.search.ISearchUiService) iInterface).onRegisterEmptyQueryResultUpdateCallback(sessionId, callback);
            }
        });
        if (serviceExists) {
            sessionInfo.addCallbackLocked(callback);
        }
    }

    public void unregisterEmptyQueryResultUpdateCallbackLocked(final android.app.search.SearchSessionId sessionId, final android.app.search.ISearchCallback callback) {
        com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        boolean serviceExists = resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.searchui.SearchUiPerUserService$$ExternalSyntheticLambda4
            public final void run(android.os.IInterface iInterface) {
                ((android.service.search.ISearchUiService) iInterface).onUnregisterEmptyQueryResultUpdateCallback(sessionId, callback);
            }
        });
        if (serviceExists) {
            sessionInfo.removeCallbackLocked(callback);
        }
    }

    public void onDestroyLocked(final android.app.search.SearchSessionId sessionId) {
        if (isDebug()) {
            android.util.Slog.d(TAG, "onDestroyLocked(): sessionId=" + sessionId);
        }
        com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo sessionInfo = this.mSessionInfos.remove(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.searchui.SearchUiPerUserService$$ExternalSyntheticLambda5
            public final void run(android.os.IInterface iInterface) {
                ((android.service.search.ISearchUiService) iInterface).onDestroy(sessionId);
            }
        });
        sessionInfo.destroy();
    }

    @Override // com.android.server.searchui.RemoteSearchUiService.RemoteSearchUiServiceCallbacks
    public void onFailureOrTimeout(boolean timedOut) {
        if (isDebug()) {
            android.util.Slog.d(TAG, "onFailureOrTimeout(): timed out=" + timedOut);
        }
    }

    @Override // com.android.server.searchui.RemoteSearchUiService.RemoteSearchUiServiceCallbacks
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

    public void onServiceDied(com.android.server.searchui.RemoteSearchUiService service) {
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
        for (com.android.server.searchui.SearchUiPerUserService.SearchSessionInfo sessionInfo : this.mSessionInfos.values()) {
            sessionInfo.resurrectSessionLocked(this, sessionInfo.mToken);
        }
    }

    protected boolean resolveService(android.app.search.SearchSessionId sessionId, com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.search.ISearchUiService> cb) {
        com.android.server.searchui.RemoteSearchUiService service = getRemoteServiceLocked();
        if (service != null) {
            service.executeOnResolvedService(cb);
        }
        return service != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.searchui.RemoteSearchUiService getRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            java.lang.String serviceName = getComponentNameLocked();
            if (serviceName == null) {
                if (((com.android.server.searchui.SearchUiManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "getRemoteServiceLocked(): not set");
                    return null;
                }
                return null;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            this.mRemoteService = new com.android.server.searchui.RemoteSearchUiService(getContext(), "android.service.search.SearchUiService", serviceComponent, this.mUserId, this, ((com.android.server.searchui.SearchUiManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.searchui.SearchUiManagerService) this.mMaster).verbose);
        }
        return this.mRemoteService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SearchSessionInfo {
        private static final boolean DEBUG = true;
        private final android.os.RemoteCallbackList<android.app.search.ISearchCallback> mCallbacks = new android.os.RemoteCallbackList<>();
        final android.os.IBinder.DeathRecipient mDeathRecipient;
        private final android.app.search.SearchContext mSearchContext;
        private final android.app.search.SearchSessionId mSessionId;
        final android.os.IBinder mToken;

        SearchSessionInfo(android.app.search.SearchSessionId id, android.app.search.SearchContext context, android.os.IBinder token, android.os.IBinder.DeathRecipient deathRecipient) {
            android.util.Slog.d(com.android.server.searchui.SearchUiPerUserService.TAG, "Creating SearchSessionInfo for session Id=" + id);
            this.mSessionId = id;
            this.mSearchContext = context;
            this.mToken = token;
            this.mDeathRecipient = deathRecipient;
        }

        void addCallbackLocked(android.app.search.ISearchCallback callback) {
            android.util.Slog.d(com.android.server.searchui.SearchUiPerUserService.TAG, "Storing callback for session Id=" + this.mSessionId + " and callback=" + callback.asBinder());
            this.mCallbacks.register(callback);
        }

        void removeCallbackLocked(android.app.search.ISearchCallback callback) {
            android.util.Slog.d(com.android.server.searchui.SearchUiPerUserService.TAG, "Removing callback for session Id=" + this.mSessionId + " and callback=" + callback.asBinder());
            this.mCallbacks.unregister(callback);
        }

        boolean linkToDeath() {
            try {
                this.mToken.linkToDeath(this.mDeathRecipient, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.searchui.SearchUiPerUserService.TAG, "Caller is dead before session can be started, sessionId: " + this.mSessionId);
                return false;
            }
        }

        void destroy() {
            android.util.Slog.d(com.android.server.searchui.SearchUiPerUserService.TAG, "Removing all callbacks for session Id=" + this.mSessionId + " and " + this.mCallbacks.getRegisteredCallbackCount() + " callbacks.");
            if (this.mToken != null) {
                this.mToken.unlinkToDeath(this.mDeathRecipient, 0);
            }
            this.mCallbacks.kill();
        }

        void resurrectSessionLocked(final com.android.server.searchui.SearchUiPerUserService service, android.os.IBinder token) {
            int callbackCount = this.mCallbacks.getRegisteredCallbackCount();
            android.util.Slog.d(com.android.server.searchui.SearchUiPerUserService.TAG, "Resurrecting remote service (" + service.getRemoteServiceLocked() + ") for session Id=" + this.mSessionId + " and " + callbackCount + " callbacks.");
            service.onCreateSearchSessionLocked(this.mSearchContext, this.mSessionId, token);
            this.mCallbacks.broadcast(new java.util.function.Consumer() { // from class: com.android.server.searchui.SearchUiPerUserService$SearchSessionInfo$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$resurrectSessionLocked$0(service, (android.app.search.ISearchCallback) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resurrectSessionLocked$0(com.android.server.searchui.SearchUiPerUserService service, android.app.search.ISearchCallback callback) {
            service.registerEmptyQueryResultUpdateCallbackLocked(this.mSessionId, callback);
        }
    }
}
