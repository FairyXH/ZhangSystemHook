package com.android.server.appprediction;

/* JADX INFO: loaded from: classes.dex */
public class AppPredictionPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.appprediction.AppPredictionPerUserService, com.android.server.appprediction.AppPredictionManagerService> implements com.android.server.appprediction.RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks {
    private static final java.lang.String PREDICT_USING_PEOPLE_SERVICE_PREFIX = "predict_using_people_service_";
    private static final java.lang.String REMOTE_APP_PREDICTOR_KEY = "remote_app_predictor";
    private static final java.lang.String TAG = com.android.server.appprediction.AppPredictionPerUserService.class.getSimpleName();
    private com.android.server.appprediction.RemoteAppPredictionService mRemoteService;
    private final android.util.ArrayMap<android.app.prediction.AppPredictionSessionId, com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo> mSessionInfos;
    private boolean mZombie;

    protected AppPredictionPerUserService(com.android.server.appprediction.AppPredictionManagerService master, java.lang.Object lock, int userId) {
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
            this.mRemoteService = null;
        }
        return enabledChanged;
    }

    public void onCreatePredictionSessionLocked(final android.app.prediction.AppPredictionContext context, final android.app.prediction.AppPredictionSessionId sessionId, android.os.IBinder token) {
        boolean usesPeopleService = android.provider.DeviceConfig.getBoolean("systemui", PREDICT_USING_PEOPLE_SERVICE_PREFIX + context.getUiSurface(), false);
        if (context.getExtras() != null && context.getExtras().getBoolean(REMOTE_APP_PREDICTOR_KEY, false) && android.provider.DeviceConfig.getBoolean("systemui", "dark_launch_remote_prediction_service_enabled", false)) {
            usesPeopleService = false;
        }
        boolean serviceExists = resolveService(sessionId, true, usesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda1
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).onCreatePredictionSession(context, sessionId);
            }
        });
        if (serviceExists && !this.mSessionInfos.containsKey(sessionId)) {
            com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = new com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo(sessionId, context, usesPeopleService, token, new android.os.IBinder.DeathRecipient() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda2
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$onCreatePredictionSessionLocked$1(sessionId);
                }
            });
            if (sessionInfo.linkToDeath()) {
                this.mSessionInfos.put(sessionId, sessionInfo);
            } else {
                onDestroyPredictionSessionLocked(sessionId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreatePredictionSessionLocked$1(android.app.prediction.AppPredictionSessionId sessionId) {
        synchronized (this.mLock) {
            onDestroyPredictionSessionLocked(sessionId);
        }
    }

    public void notifyAppTargetEventLocked(final android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.AppTargetEvent event) {
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, false, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda7
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).notifyAppTargetEvent(sessionId, event);
            }
        });
    }

    public void notifyLaunchLocationShownLocked(final android.app.prediction.AppPredictionSessionId sessionId, final java.lang.String launchLocation, final android.content.pm.ParceledListSlice targetIds) {
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, false, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda9
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).notifyLaunchLocationShown(sessionId, launchLocation, targetIds);
            }
        });
    }

    public void sortAppTargetsLocked(final android.app.prediction.AppPredictionSessionId sessionId, final android.content.pm.ParceledListSlice targets, final android.app.prediction.IPredictionCallback callback) {
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, true, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda0
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).sortAppTargets(sessionId, targets, callback);
            }
        });
    }

    public void registerPredictionUpdatesLocked(final android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.IPredictionCallback callback) {
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        boolean serviceExists = resolveService(sessionId, true, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda4
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).registerPredictionUpdates(sessionId, callback);
            }
        });
        if (serviceExists) {
            sessionInfo.addCallbackLocked(callback);
        }
    }

    public void unregisterPredictionUpdatesLocked(final android.app.prediction.AppPredictionSessionId sessionId, final android.app.prediction.IPredictionCallback callback) {
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        boolean serviceExists = resolveService(sessionId, false, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda5
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).unregisterPredictionUpdates(sessionId, callback);
            }
        });
        if (serviceExists) {
            sessionInfo.removeCallbackLocked(callback);
        }
    }

    public void requestPredictionUpdateLocked(final android.app.prediction.AppPredictionSessionId sessionId) {
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, true, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda8
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).requestPredictionUpdate(sessionId);
            }
        });
    }

    public void onDestroyPredictionSessionLocked(final android.app.prediction.AppPredictionSessionId sessionId) {
        if (isDebug()) {
            android.util.Slog.d(TAG, "onDestroyPredictionSessionLocked(): sessionId=" + sessionId);
        }
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.remove(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, false, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda3
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).onDestroyPredictionSession(sessionId);
            }
        });
        sessionInfo.destroy();
    }

    public void requestServiceFeaturesLocked(final android.app.prediction.AppPredictionSessionId sessionId, final android.os.IRemoteCallback callback) {
        com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo = this.mSessionInfos.get(sessionId);
        if (sessionInfo == null) {
            return;
        }
        resolveService(sessionId, true, sessionInfo.mUsesPeopleService, new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda6
            public final void run(android.os.IInterface iInterface) {
                ((android.service.appprediction.IPredictionService) iInterface).requestServiceFeatures(sessionId, callback);
            }
        });
    }

    @Override // com.android.server.appprediction.RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks
    public void onFailureOrTimeout(boolean timedOut) {
        if (isDebug()) {
            android.util.Slog.d(TAG, "onFailureOrTimeout(): timed out=" + timedOut);
        }
    }

    @Override // com.android.server.appprediction.RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks
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

    public void onServiceDied(com.android.server.appprediction.RemoteAppPredictionService service) {
        if (isDebug()) {
            android.util.Slog.w(TAG, "onServiceDied(): service=" + service);
        }
        synchronized (this.mLock) {
            this.mZombie = true;
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
        for (com.android.server.appprediction.AppPredictionPerUserService.AppPredictionSessionInfo sessionInfo : this.mSessionInfos.values()) {
            sessionInfo.resurrectSessionLocked(this, sessionInfo.mToken);
        }
    }

    protected boolean resolveService(android.app.prediction.AppPredictionSessionId sessionId, boolean sendImmediately, boolean usesPeopleService, com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.appprediction.IPredictionService> cb) {
        if (usesPeopleService) {
            android.service.appprediction.IPredictionService service = (android.service.appprediction.IPredictionService) com.android.server.LocalServices.getService(com.android.server.people.PeopleServiceInternal.class);
            if (service != null) {
                try {
                    cb.run(service);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed to invoke service:" + service, e);
                }
            }
            return service != null;
        }
        com.android.server.appprediction.RemoteAppPredictionService service2 = getRemoteServiceLocked();
        if (service2 != null) {
            if (sendImmediately) {
                service2.executeOnResolvedService(cb);
            } else {
                service2.scheduleOnResolvedService(cb);
            }
        }
        return service2 != null;
    }

    private com.android.server.appprediction.RemoteAppPredictionService getRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            java.lang.String serviceName = getComponentNameLocked();
            if (serviceName == null) {
                if (((com.android.server.appprediction.AppPredictionManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "getRemoteServiceLocked(): not set");
                    return null;
                }
                return null;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            this.mRemoteService = new com.android.server.appprediction.RemoteAppPredictionService(getContext(), "android.service.appprediction.AppPredictionService", serviceComponent, this.mUserId, this, ((com.android.server.appprediction.AppPredictionManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.appprediction.AppPredictionManagerService) this.mMaster).verbose);
        }
        return this.mRemoteService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class AppPredictionSessionInfo {
        private static final boolean DEBUG = false;
        private final android.os.RemoteCallbackList<android.app.prediction.IPredictionCallback> mCallbacks = new android.os.RemoteCallbackList<>();
        final android.os.IBinder.DeathRecipient mDeathRecipient;
        private final android.app.prediction.AppPredictionContext mPredictionContext;
        private final android.app.prediction.AppPredictionSessionId mSessionId;
        final android.os.IBinder mToken;
        private final boolean mUsesPeopleService;

        AppPredictionSessionInfo(android.app.prediction.AppPredictionSessionId id, android.app.prediction.AppPredictionContext predictionContext, boolean usesPeopleService, android.os.IBinder token, android.os.IBinder.DeathRecipient deathRecipient) {
            this.mSessionId = id;
            this.mPredictionContext = predictionContext;
            this.mUsesPeopleService = usesPeopleService;
            this.mToken = token;
            this.mDeathRecipient = deathRecipient;
        }

        void addCallbackLocked(android.app.prediction.IPredictionCallback callback) {
            this.mCallbacks.register(callback);
        }

        void removeCallbackLocked(android.app.prediction.IPredictionCallback callback) {
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

        void resurrectSessionLocked(final com.android.server.appprediction.AppPredictionPerUserService service, android.os.IBinder token) {
            this.mCallbacks.getRegisteredCallbackCount();
            service.onCreatePredictionSessionLocked(this.mPredictionContext, this.mSessionId, token);
            this.mCallbacks.broadcast(new java.util.function.Consumer() { // from class: com.android.server.appprediction.AppPredictionPerUserService$AppPredictionSessionInfo$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$resurrectSessionLocked$0(service, (android.app.prediction.IPredictionCallback) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resurrectSessionLocked$0(com.android.server.appprediction.AppPredictionPerUserService service, android.app.prediction.IPredictionCallback callback) {
            service.registerPredictionUpdatesLocked(this.mSessionId, callback);
        }
    }
}
