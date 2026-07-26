package com.android.server.speech;

/* JADX INFO: loaded from: classes3.dex */
final class SpeechRecognitionManagerServiceImpl extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.speech.SpeechRecognitionManagerServiceImpl, com.android.server.speech.SpeechRecognitionManagerService> {
    private static final int MAX_CONCURRENT_CONNECTIONS_BY_CLIENT = 10;
    private static final java.lang.String TAG = com.android.server.speech.SpeechRecognitionManagerServiceImpl.class.getSimpleName();
    private final java.lang.Object mLock;
    private final java.util.Map<java.lang.Integer, java.util.Set<com.android.server.speech.RemoteSpeechRecognitionService>> mRemoteServicesByUid;
    private final android.util.SparseIntArray mSessionCountByUid;

    SpeechRecognitionManagerServiceImpl(com.android.server.speech.SpeechRecognitionManagerService master, java.lang.Object lock, int userId) {
        super(master, lock, userId);
        this.mLock = new java.lang.Object();
        this.mRemoteServicesByUid = new java.util.HashMap();
        this.mSessionCountByUid = new android.util.SparseIntArray();
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            return android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected boolean updateLocked(boolean disabled) {
        boolean enabledChanged = super.updateLocked(disabled);
        return enabledChanged;
    }

    void createSessionLocked(android.content.ComponentName componentName, final android.os.IBinder clientToken, boolean onDevice, final android.speech.IRecognitionServiceManagerCallback callback) {
        android.content.ComponentName serviceComponent;
        if (((com.android.server.speech.SpeechRecognitionManagerService) this.mMaster).debug) {
            android.util.Slog.i(TAG, java.lang.String.format("#createSessionLocked, component=%s, onDevice=%s", componentName, java.lang.Boolean.valueOf(onDevice)));
        }
        if (!onDevice) {
            serviceComponent = componentName;
        } else {
            android.content.ComponentName serviceComponent2 = getOnDeviceComponentNameLocked();
            serviceComponent = serviceComponent2;
        }
        if (!onDevice && android.os.Process.isIsolated(android.os.Binder.getCallingUid())) {
            android.util.Slog.w(TAG, "Isolated process can only start on device speech recognizer.");
            tryRespondWithError(callback, 5);
            return;
        }
        if (serviceComponent == null) {
            if (((com.android.server.speech.SpeechRecognitionManagerService) this.mMaster).debug) {
                android.util.Slog.i(TAG, "Service component is undefined, responding with error.");
            }
            tryRespondWithError(callback, 5);
            return;
        }
        final int creatorCallingUid = android.os.Binder.getCallingUid();
        final com.android.server.speech.RemoteSpeechRecognitionService service = createService(creatorCallingUid, serviceComponent);
        if (service != null) {
            final android.os.IBinder.DeathRecipient deathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.speech.SpeechRecognitionManagerServiceImpl$$ExternalSyntheticLambda2
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$createSessionLocked$0(clientToken, creatorCallingUid, service);
                }
            };
            try {
                clientToken.linkToDeath(deathRecipient, 0);
                service.connect().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.speech.SpeechRecognitionManagerServiceImpl$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$createSessionLocked$1(callback, service, clientToken, creatorCallingUid, deathRecipient, (android.speech.IRecognitionService) obj);
                    }
                });
                return;
            } catch (android.os.RemoteException e) {
                handleClientDeath(clientToken, creatorCallingUid, service, true);
                return;
            }
        }
        tryRespondWithError(callback, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSessionLocked$0(android.os.IBinder clientToken, int creatorCallingUid, com.android.server.speech.RemoteSpeechRecognitionService service) {
        handleClientDeath(clientToken, creatorCallingUid, service, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSessionLocked$1(android.speech.IRecognitionServiceManagerCallback callback, final com.android.server.speech.RemoteSpeechRecognitionService service, final android.os.IBinder clientToken, final int creatorCallingUid, final android.os.IBinder.DeathRecipient deathRecipient, android.speech.IRecognitionService binderService) {
        if (binderService != null) {
            try {
                callback.onSuccess(new android.speech.IRecognitionService.Stub() { // from class: com.android.server.speech.SpeechRecognitionManagerServiceImpl.1
                    public void startListening(android.content.Intent recognizerIntent, android.speech.IRecognitionListener listener, android.content.AttributionSource attributionSource) throws android.os.RemoteException {
                        attributionSource.enforceCallingUid();
                        if (!attributionSource.isTrusted(((com.android.server.speech.SpeechRecognitionManagerService) com.android.server.speech.SpeechRecognitionManagerServiceImpl.this.mMaster).getContext())) {
                            attributionSource = ((android.permission.PermissionManager) ((com.android.server.speech.SpeechRecognitionManagerService) com.android.server.speech.SpeechRecognitionManagerServiceImpl.this.mMaster).getContext().getSystemService(android.permission.PermissionManager.class)).registerAttributionSource(attributionSource);
                        }
                        service.startListening(recognizerIntent, listener, attributionSource);
                        service.associateClientWithActiveListener(clientToken, listener);
                    }

                    public void stopListening(android.speech.IRecognitionListener listener) throws android.os.RemoteException {
                        service.stopListening(listener);
                    }

                    public void cancel(android.speech.IRecognitionListener listener, boolean isShutdown) throws android.os.RemoteException {
                        service.cancel(listener, isShutdown);
                        if (isShutdown) {
                            com.android.server.speech.SpeechRecognitionManagerServiceImpl.this.handleClientDeath(clientToken, creatorCallingUid, service, false);
                            clientToken.unlinkToDeath(deathRecipient, 0);
                        }
                    }

                    public void checkRecognitionSupport(android.content.Intent recognizerIntent, android.content.AttributionSource attributionSource, android.speech.IRecognitionSupportCallback callback2) {
                        service.checkRecognitionSupport(recognizerIntent, attributionSource, callback2);
                    }

                    public void triggerModelDownload(android.content.Intent recognizerIntent, android.content.AttributionSource attributionSource, android.speech.IModelDownloadListener listener) {
                        service.triggerModelDownload(recognizerIntent, attributionSource, listener);
                    }
                });
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error creating a speech recognition session", e);
                tryRespondWithError(callback, 5);
                return;
            }
        }
        tryRespondWithError(callback, 5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClientDeath(android.os.IBinder clientToken, int callingUid, com.android.server.speech.RemoteSpeechRecognitionService service, boolean invokeCancel) {
        if (invokeCancel) {
            service.shutdown(clientToken);
        }
        synchronized (this.mLock) {
            decrementSessionCountForUidLocked(callingUid);
            if (!service.hasActiveSessions()) {
                removeService(callingUid, service);
            }
        }
    }

    private android.content.ComponentName getOnDeviceComponentNameLocked() {
        java.lang.String serviceName = getComponentNameLocked();
        if (((com.android.server.speech.SpeechRecognitionManagerService) this.mMaster).debug) {
            android.util.Slog.i(TAG, "Resolved component name: " + serviceName);
        }
        if (serviceName == null) {
            if (((com.android.server.speech.SpeechRecognitionManagerService) this.mMaster).verbose) {
                android.util.Slog.v(TAG, "ensureRemoteServiceLocked(): no service component name.");
                return null;
            }
            return null;
        }
        return android.content.ComponentName.unflattenFromString(serviceName);
    }

    private int getSessionCountByUidLocked(int uid) {
        return this.mSessionCountByUid.get(uid, 0);
    }

    private void incrementSessionCountForUidLocked(int uid) {
        this.mSessionCountByUid.put(uid, this.mSessionCountByUid.get(uid, 0) + 1);
        android.util.Log.i(TAG, "Client " + uid + " has opened " + this.mSessionCountByUid.get(uid, 0) + " sessions");
    }

    private void decrementSessionCountForUidLocked(int uid) {
        int newCount = this.mSessionCountByUid.get(uid, 1) - 1;
        if (newCount > 0) {
            this.mSessionCountByUid.put(uid, newCount);
        } else {
            this.mSessionCountByUid.delete(uid);
        }
    }

    private com.android.server.speech.RemoteSpeechRecognitionService createService(int callingUid, final android.content.ComponentName serviceComponent) {
        boolean isPrivileged;
        synchronized (this.mLock) {
            java.util.Set<com.android.server.speech.RemoteSpeechRecognitionService> servicesForClient = this.mRemoteServicesByUid.get(java.lang.Integer.valueOf(callingUid));
            if (servicesForClient != null && servicesForClient.size() >= 10) {
                android.util.Slog.w(TAG, "Number of remote services exceeded for uid: " + callingUid);
                com.android.modules.expresslog.Counter.logIncrementWithUid("speech_recognition.value_exceed_service_connections_count", callingUid);
                return null;
            }
            if (getSessionCountByUidLocked(callingUid) == 10) {
                android.util.Slog.w(TAG, "Number of sessions exceeded for uid: " + callingUid);
                com.android.modules.expresslog.Counter.logIncrementWithUid("speech_recognition.value_exceed_session_count", callingUid);
            }
            if (servicesForClient != null) {
                java.util.Optional<com.android.server.speech.RemoteSpeechRecognitionService> existingService = servicesForClient.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.speech.SpeechRecognitionManagerServiceImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return ((com.android.server.speech.RemoteSpeechRecognitionService) obj).getServiceComponentName().equals(serviceComponent);
                    }
                }).findFirst();
                if (existingService.isPresent()) {
                    if (((com.android.server.speech.SpeechRecognitionManagerService) this.mMaster).debug) {
                        android.util.Slog.i(TAG, "Reused existing connection to " + serviceComponent);
                    }
                    incrementSessionCountForUidLocked(callingUid);
                    return existingService.get();
                }
            }
            if (serviceComponent != null && !componentMapsToRecognitionService(serviceComponent)) {
                return null;
            }
            if (serviceComponent == null) {
                isPrivileged = false;
            } else {
                isPrivileged = checkPrivilege(serviceComponent);
            }
            com.android.server.speech.RemoteSpeechRecognitionService service = new com.android.server.speech.RemoteSpeechRecognitionService(getContext(), serviceComponent, getUserId(), callingUid, isPrivileged);
            java.util.Set<com.android.server.speech.RemoteSpeechRecognitionService> valuesByCaller = this.mRemoteServicesByUid.computeIfAbsent(java.lang.Integer.valueOf(callingUid), new java.util.function.Function() { // from class: com.android.server.speech.SpeechRecognitionManagerServiceImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.speech.SpeechRecognitionManagerServiceImpl.lambda$createService$3((java.lang.Integer) obj);
                }
            });
            valuesByCaller.add(service);
            if (((com.android.server.speech.SpeechRecognitionManagerService) this.mMaster).debug) {
                android.util.Slog.i(TAG, "Creating a new connection to " + serviceComponent);
            }
            incrementSessionCountForUidLocked(callingUid);
            return service;
        }
    }

    static /* synthetic */ java.util.Set lambda$createService$3(java.lang.Integer key) {
        return new java.util.HashSet();
    }

    private boolean checkPrivilege(android.content.ComponentName serviceComponent) {
        android.content.ComponentName defaultComponent = getDefaultRecognitionServiceComponent();
        android.content.ComponentName onDeviceComponent = getOnDeviceComponentNameLocked();
        boolean preinstalled = isPreinstalledApp(serviceComponent);
        return serviceComponent.equals(defaultComponent) || serviceComponent.equals(onDeviceComponent) || preinstalled;
    }

    private boolean isPreinstalledApp(android.content.ComponentName serviceComponent) {
        android.content.pm.PackageManager pm = getContext().getPackageManager();
        if (pm == null) {
            return false;
        }
        try {
            android.content.pm.ApplicationInfo info = pm.getApplicationInfoAsUser(serviceComponent.getPackageName(), 1048576, getUserId());
            return (info.flags & 1) != 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private android.content.ComponentName getDefaultRecognitionServiceComponent() {
        java.lang.String componentName = android.provider.Settings.Secure.getStringForUser(getContext().getContentResolver(), "voice_recognition_service", getUserId());
        if (componentName == null) {
            return null;
        }
        return android.content.ComponentName.unflattenFromString(componentName);
    }

    private boolean componentMapsToRecognitionService(android.content.ComponentName serviceComponent) {
        long identityToken = android.os.Binder.clearCallingIdentity();
        try {
            java.util.List<android.content.pm.ResolveInfo> resolveInfos = getContext().getPackageManager().queryIntentServicesAsUser(new android.content.Intent("android.speech.RecognitionService"), 0, getUserId());
            if (resolveInfos == null) {
                return false;
            }
            for (android.content.pm.ResolveInfo ri : resolveInfos) {
                if (ri.serviceInfo != null && serviceComponent.equals(ri.serviceInfo.getComponentName())) {
                    return true;
                }
            }
            android.util.Slog.w(TAG, "serviceComponent is not RecognitionService: " + serviceComponent);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(identityToken);
        }
    }

    private void removeService(int callingUid, com.android.server.speech.RemoteSpeechRecognitionService service) {
        synchronized (this.mLock) {
            java.util.Set<com.android.server.speech.RemoteSpeechRecognitionService> valuesByCaller = this.mRemoteServicesByUid.get(java.lang.Integer.valueOf(callingUid));
            if (valuesByCaller != null) {
                valuesByCaller.remove(service);
            }
        }
    }

    private static void tryRespondWithError(android.speech.IRecognitionServiceManagerCallback callback, int errorCode) {
        try {
            callback.onError(errorCode);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to respond with error");
        }
    }
}
