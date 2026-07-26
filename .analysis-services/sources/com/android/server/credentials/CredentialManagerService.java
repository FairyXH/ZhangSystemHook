package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public final class CredentialManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.credentials.CredentialManagerService, com.android.server.credentials.CredentialManagerServiceImpl> {
    public static final java.lang.String AUTOFILL_PLACEHOLDER_VALUE = "credential-provider";
    private static final java.lang.String DEVICE_CONFIG_ENABLE_CREDENTIAL_DESC_API = "enable_credential_description_api";
    private static final java.lang.String DEVICE_CONFIG_ENABLE_CREDENTIAL_MANAGER = "enable_credential_manager";
    private static final java.lang.String PERMISSION_DENIED_ERROR = "permission_denied";
    private static final java.lang.String PERMISSION_DENIED_WRITE_SECURE_SETTINGS_ERROR = "Caller is missing WRITE_SECURE_SETTINGS permission";
    private static final java.lang.String TAG = "CredentialManager";
    private final android.content.Context mContext;
    private final android.util.SparseArray<java.util.Map<android.os.IBinder, com.android.server.credentials.RequestSession>> mRequestSessions;
    private final com.android.server.credentials.CredentialManagerService.SessionManager mSessionManager;
    private final android.util.SparseArray<java.util.List<com.android.server.credentials.CredentialManagerServiceImpl>> mSystemServicesCacheList;

    public CredentialManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.SecureSettingsServiceNameResolver(context, "credential_service", true), null, 4);
        this.mSystemServicesCacheList = new android.util.SparseArray<>();
        this.mRequestSessions = new android.util.SparseArray<>();
        this.mSessionManager = new com.android.server.credentials.CredentialManagerService.SessionManager();
        this.mContext = context;
    }

    private java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> constructSystemServiceListLocked(final int resolvedUserId) {
        final java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> services = new java.util.ArrayList<>();
        java.util.List<android.credentials.CredentialProviderInfo> serviceInfos = android.service.credentials.CredentialProviderInfoFactory.getAvailableSystemServices(this.mContext, resolvedUserId, false, new java.util.HashSet());
        serviceInfos.forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$constructSystemServiceListLocked$0(services, resolvedUserId, (android.credentials.CredentialProviderInfo) obj);
            }
        });
        return services;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$constructSystemServiceListLocked$0(java.util.List services, int resolvedUserId, android.credentials.CredentialProviderInfo info) {
        services.add(new com.android.server.credentials.CredentialManagerServiceImpl(this, this.mLock, resolvedUserId, info));
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected java.lang.String getServiceSettingsProperty() {
        return "credential_service";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.credentials.CredentialManagerServiceImpl newServiceLocked(int resolvedUserId, boolean disabled) {
        android.util.Slog.w(TAG, "Should not be here - CredentialManagerService is configured to use multiple services");
        return null;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("credential", new com.android.server.credentials.CredentialManagerService.CredentialManagerServiceStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> newServiceListLocked(int resolvedUserId, boolean disabled, java.lang.String[] serviceNames) {
        getOrConstructSystemServiceListLock(resolvedUserId);
        if (serviceNames == null || serviceNames.length == 0) {
            return new java.util.ArrayList();
        }
        java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> serviceList = new java.util.ArrayList<>(serviceNames.length);
        for (java.lang.String serviceName : serviceNames) {
            if (!android.text.TextUtils.isEmpty(serviceName)) {
                try {
                    serviceList.add(new com.android.server.credentials.CredentialManagerServiceImpl(this, this.mLock, resolvedUserId, serviceName));
                } catch (android.content.pm.PackageManager.NameNotFoundException | java.lang.SecurityException e) {
                    android.util.Slog.e(TAG, "Unable to add serviceInfo : ", e);
                }
            }
        }
        return serviceList;
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void handlePackageRemovedMultiModeLocked(java.lang.String packageName, int userId) {
        updateProvidersWhenPackageRemoved(new com.android.server.credentials.CredentialManagerService.SettingsWrapper(this.mContext), packageName);
        java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> services = peekServiceListForUserLocked(userId);
        if (services == null) {
            return;
        }
        java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> servicesToBeRemoved = new java.util.ArrayList<>();
        for (com.android.server.credentials.CredentialManagerServiceImpl service : services) {
            if (service != null) {
                android.credentials.CredentialProviderInfo credentialProviderInfo = service.getCredentialProviderInfo();
                android.content.ComponentName componentName = credentialProviderInfo.getServiceInfo().getComponentName();
                if (packageName.equals(componentName.getPackageName())) {
                    servicesToBeRemoved.add(service);
                }
            }
        }
        for (com.android.server.credentials.CredentialManagerServiceImpl serviceToBeRemoved : servicesToBeRemoved) {
            removeServiceFromCache(serviceToBeRemoved, userId);
            removeServiceFromSystemServicesCache(serviceToBeRemoved, userId);
            com.android.server.credentials.CredentialDescriptionRegistry.forUser(userId).evictProviderWithPackageName(serviceToBeRemoved.getServicePackageName());
        }
    }

    private void removeServiceFromSystemServicesCache(com.android.server.credentials.CredentialManagerServiceImpl serviceToBeRemoved, int userId) {
        if (this.mSystemServicesCacheList.get(userId) != null) {
            this.mSystemServicesCacheList.get(userId).remove(serviceToBeRemoved);
        }
    }

    private java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> getOrConstructSystemServiceListLock(int resolvedUserId) {
        java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> services = this.mSystemServicesCacheList.get(resolvedUserId);
        if (services == null || services.size() == 0) {
            java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> services2 = constructSystemServiceListLocked(resolvedUserId);
            this.mSystemServicesCacheList.put(resolvedUserId, services2);
            return services2;
        }
        return services;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasWriteSecureSettingsPermission() {
        return hasPermission("android.permission.WRITE_SECURE_SETTINGS");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verifyGetProvidersPermission() throws java.lang.SecurityException {
        if (hasPermission("android.permission.QUERY_ALL_PACKAGES") || hasPermission("android.permission.LIST_ENABLED_CREDENTIAL_PROVIDERS")) {
        } else {
            throw new java.lang.SecurityException("Caller is missing permission: QUERY_ALL_PACKAGES or LIST_ENABLED_CREDENTIAL_PROVIDERS");
        }
    }

    private boolean hasPermission(java.lang.String permission) {
        boolean result = this.mContext.checkCallingOrSelfPermission(permission) == 0;
        if (!result) {
            android.util.Slog.e(TAG, "Caller does not have permission: " + permission);
        }
        return result;
    }

    private void runForUser(java.util.function.Consumer<com.android.server.credentials.CredentialManagerServiceImpl> c) {
        int userId = android.os.UserHandle.getCallingUserId();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> services = getCredentialProviderServicesLocked(userId);
                for (com.android.server.credentials.CredentialManagerServiceImpl s : services) {
                    c.accept(s);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    static java.util.Set<android.content.ComponentName> getPrimaryProvidersForUserId(android.content.Context context, int userId) {
        int resolvedUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "getPrimaryProvidersForUserId", null);
        com.android.server.infra.SecureSettingsServiceNameResolver resolver = new com.android.server.infra.SecureSettingsServiceNameResolver(context, "credential_service_primary", true);
        java.lang.String[] serviceNames = resolver.readServiceNameList(resolvedUserId);
        if (serviceNames == null) {
            return new java.util.HashSet();
        }
        java.util.Set<android.content.ComponentName> services = new java.util.HashSet<>();
        for (java.lang.String serviceName : serviceNames) {
            android.content.ComponentName compName = android.content.ComponentName.unflattenFromString(serviceName);
            if (compName == null) {
                android.util.Slog.w(TAG, "Primary provider component name unflatten from string error: " + serviceName);
            } else {
                services.add(compName);
            }
        }
        return services;
    }

    private java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> getCredentialProviderServicesLocked(int userId) {
        java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> concatenatedServices = new java.util.ArrayList<>();
        java.util.List<com.android.server.credentials.CredentialManagerServiceImpl> userConfigurableServices = getServiceListForUserLocked(userId);
        if (userConfigurableServices != null && !userConfigurableServices.isEmpty()) {
            concatenatedServices.addAll(userConfigurableServices);
        }
        concatenatedServices.addAll(getOrConstructSystemServiceListLock(userId));
        return concatenatedServices;
    }

    public static boolean isCredentialDescriptionApiEnabled() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<com.android.server.credentials.ProviderSession> initiateProviderSessionsWithActiveContainers(com.android.server.credentials.GetRequestSession session, java.util.Set<android.util.Pair<android.credentials.CredentialOption, com.android.server.credentials.CredentialDescriptionRegistry.FilterResult>> activeCredentialContainers) {
        java.util.List<com.android.server.credentials.ProviderSession> providerSessions = new java.util.ArrayList<>();
        for (android.util.Pair<android.credentials.CredentialOption, com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> result : activeCredentialContainers) {
            com.android.server.credentials.ProviderSession providerSession = com.android.server.credentials.ProviderRegistryGetSession.createNewSession(this.mContext, android.os.UserHandle.getCallingUserId(), session, session.mClientAppInfo, ((com.android.server.credentials.CredentialDescriptionRegistry.FilterResult) result.second).mPackageName, (android.credentials.CredentialOption) result.first);
            providerSessions.add(providerSession);
            session.addProviderSession(providerSession.getComponentName(), providerSession);
        }
        return providerSessions;
    }

    private java.util.List<com.android.server.credentials.ProviderSession> initiateProviderSessionsWithActiveContainers(com.android.server.credentials.PrepareGetRequestSession session, java.util.Set<android.util.Pair<android.credentials.CredentialOption, com.android.server.credentials.CredentialDescriptionRegistry.FilterResult>> activeCredentialContainers) {
        java.util.List<com.android.server.credentials.ProviderSession> providerSessions = new java.util.ArrayList<>();
        for (android.util.Pair<android.credentials.CredentialOption, com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> result : activeCredentialContainers) {
            com.android.server.credentials.ProviderSession providerSession = com.android.server.credentials.ProviderRegistryGetSession.createNewSession(this.mContext, android.os.UserHandle.getCallingUserId(), session, session.mClientAppInfo, ((com.android.server.credentials.CredentialDescriptionRegistry.FilterResult) result.second).mPackageName, (android.credentials.CredentialOption) result.first);
            providerSessions.add(providerSession);
            session.addProviderSession(providerSession.getComponentName(), providerSession);
        }
        return providerSessions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.Set<android.util.Pair<android.credentials.CredentialOption, com.android.server.credentials.CredentialDescriptionRegistry.FilterResult>> getFilteredResultFromRegistry(java.util.List<android.credentials.CredentialOption> options) {
        com.android.server.credentials.CredentialDescriptionRegistry registry = com.android.server.credentials.CredentialDescriptionRegistry.forUser(android.os.UserHandle.getCallingUserId());
        java.util.Set<java.util.Set<java.lang.String>> requestedCredentialDescriptions = (java.util.Set) options.stream().map(new java.util.function.Function() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.credentials.CredentialManagerService.lambda$getFilteredResultFromRegistry$1((android.credentials.CredentialOption) obj);
            }
        }).collect(java.util.stream.Collectors.toSet());
        java.util.Set<com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> filterResults = registry.getMatchingProviders(requestedCredentialDescriptions);
        java.util.Set<android.util.Pair<android.credentials.CredentialOption, com.android.server.credentials.CredentialDescriptionRegistry.FilterResult>> result = new java.util.HashSet<>();
        for (com.android.server.credentials.CredentialDescriptionRegistry.FilterResult filterResult : filterResults) {
            for (android.credentials.CredentialOption credentialOption : options) {
                java.util.Set<java.lang.String> requestedElementKeys = new java.util.HashSet<>(credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS"));
                if (com.android.server.credentials.CredentialDescriptionRegistry.checkForMatch(filterResult.mElementKeys, requestedElementKeys)) {
                    result.add(new android.util.Pair<>(credentialOption, filterResult));
                }
            }
        }
        return result;
    }

    static /* synthetic */ java.util.HashSet lambda$getFilteredResultFromRegistry$1(android.credentials.CredentialOption getCredentialOption) {
        return new java.util.HashSet(getCredentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<com.android.server.credentials.ProviderSession> initiateProviderSessions(final com.android.server.credentials.RequestSession session, final java.util.List<java.lang.String> requestOptions) {
        final java.util.List<com.android.server.credentials.ProviderSession> providerSessions = new java.util.ArrayList<>();
        runForUser(new java.util.function.Consumer() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$initiateProviderSessions$2(session, requestOptions, providerSessions, (com.android.server.credentials.CredentialManagerServiceImpl) obj);
            }
        });
        return providerSessions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initiateProviderSessions$2(com.android.server.credentials.RequestSession session, java.util.List requestOptions, java.util.List providerSessions, com.android.server.credentials.CredentialManagerServiceImpl service) {
        synchronized (this.mLock) {
            com.android.server.credentials.ProviderSession providerSession = service.initiateProviderSessionForRequestLocked(session, requestOptions);
            if (providerSession != null) {
                providerSessions.add(providerSession);
            }
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService, com.android.server.SystemService
    public void onUserStopped(com.android.server.SystemService.TargetUser user) {
        super.onUserStopped(user);
        com.android.server.credentials.CredentialDescriptionRegistry.clearUserSession(user.getUserIdentifier());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.service.credentials.CallingAppInfo constructCallingAppInfo(java.lang.String realPackageName, int userId, java.lang.String origin) {
        try {
            android.content.pm.PackageInfo packageInfo = getContext().getPackageManager().getPackageInfoAsUser(realPackageName, android.content.pm.PackageManager.PackageInfoFlags.of(134217728L), userId);
            android.service.credentials.CallingAppInfo callingAppInfo = new android.service.credentials.CallingAppInfo(realPackageName, packageInfo.signingInfo, origin);
            return callingAppInfo;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Issue while retrieving signatureInfo : ", e);
            android.service.credentials.CallingAppInfo callingAppInfo2 = new android.service.credentials.CallingAppInfo(realPackageName, null, origin);
            return callingAppInfo2;
        }
    }

    final class CredentialManagerServiceStub extends android.credentials.ICredentialManager.Stub {
        CredentialManagerServiceStub() {
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public android.os.ICancellationSignal getCandidateCredentials(android.credentials.GetCredentialRequest request, android.credentials.IGetCandidateCredentialsCallback callback, android.os.IBinder clientBinder, java.lang.String callingPackage) {
            android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "starting getCandidateCredentials with callingPackage: " + callingPackage);
            android.os.ICancellationSignal cancelTransport = android.os.CancellationSignal.createTransport();
            int userId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.credentials.GetCandidateRequestSession session = new com.android.server.credentials.GetCandidateRequestSession(com.android.server.credentials.CredentialManagerService.this.getContext(), com.android.server.credentials.CredentialManagerService.this.mSessionManager, com.android.server.credentials.CredentialManagerService.this.mLock, userId, callingUid, callback, request, com.android.server.credentials.CredentialManagerService.this.constructCallingAppInfo(callingPackage, userId, request.getOrigin()), getEnabledProvidersForUser(userId), android.os.CancellationSignal.fromTransport(cancelTransport), clientBinder);
            com.android.server.credentials.CredentialManagerService.this.addSessionLocked(userId, session);
            java.util.List<com.android.server.credentials.ProviderSession> providerSessions = com.android.server.credentials.CredentialManagerService.this.initiateProviderSessions(session, (java.util.List) request.getCredentialOptions().stream().map(new com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda3()).collect(java.util.stream.Collectors.toList()));
            finalizeAndEmitInitialPhaseMetric(session);
            if (providerSessions.isEmpty()) {
                try {
                    callback.onError("android.credentials.GetCandidateCredentialsException.TYPE_NO_CREDENTIAL", "No credentials available on this device.");
                } catch (android.os.RemoteException e) {
                    android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "Issue invoking onError on IGetCredentialCallback callback: " + e.getMessage());
                }
            }
            invokeProviderSessions(providerSessions);
            return cancelTransport;
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public android.os.ICancellationSignal executeGetCredential(android.credentials.GetCredentialRequest request, android.credentials.IGetCredentialCallback callback, java.lang.String callingPackage) {
            long timestampBegan = java.lang.System.nanoTime();
            android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "starting executeGetCredential with callingPackage: " + callingPackage);
            android.os.ICancellationSignal cancelTransport = android.os.CancellationSignal.createTransport();
            int userId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.credentials.CredentialManagerService.this.enforceCallingPackage(callingPackage, callingUid);
            com.android.server.credentials.CredentialManagerService.this.validateGetCredentialRequest(request);
            com.android.server.credentials.GetRequestSession session = new com.android.server.credentials.GetRequestSession(com.android.server.credentials.CredentialManagerService.this.getContext(), com.android.server.credentials.CredentialManagerService.this.mSessionManager, com.android.server.credentials.CredentialManagerService.this.mLock, userId, callingUid, callback, request, com.android.server.credentials.CredentialManagerService.this.constructCallingAppInfo(callingPackage, userId, request.getOrigin()), getEnabledProvidersForUser(userId), android.os.CancellationSignal.fromTransport(cancelTransport), timestampBegan);
            com.android.server.credentials.CredentialManagerService.this.addSessionLocked(userId, session);
            java.util.List<com.android.server.credentials.ProviderSession> providerSessions = prepareProviderSessions(request, session);
            if (providerSessions.isEmpty()) {
                try {
                    callback.onError("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", "No credentials available on this device.");
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Issue invoking onError on IGetCredentialCallback callback: " + e.getMessage());
                }
            }
            invokeProviderSessions(providerSessions);
            return cancelTransport;
        }

        public android.os.ICancellationSignal executePrepareGetCredential(android.credentials.GetCredentialRequest request, android.credentials.IPrepareGetCredentialCallback prepareGetCredentialCallback, android.credentials.IGetCredentialCallback getCredentialCallback, java.lang.String callingPackage) {
            long timestampBegan = java.lang.System.nanoTime();
            android.os.ICancellationSignal cancelTransport = android.os.CancellationSignal.createTransport();
            if (request.getOrigin() != null) {
                com.android.server.credentials.CredentialManagerService.this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ORIGIN", null);
            }
            com.android.server.credentials.CredentialManagerService.this.enforcePermissionForAllowedProviders(request);
            int userId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.credentials.CredentialManagerService.this.enforceCallingPackage(callingPackage, callingUid);
            com.android.server.credentials.PrepareGetRequestSession session = new com.android.server.credentials.PrepareGetRequestSession(com.android.server.credentials.CredentialManagerService.this.getContext(), com.android.server.credentials.CredentialManagerService.this.mSessionManager, com.android.server.credentials.CredentialManagerService.this.mLock, userId, callingUid, getCredentialCallback, request, com.android.server.credentials.CredentialManagerService.this.constructCallingAppInfo(callingPackage, userId, request.getOrigin()), getEnabledProvidersForUser(userId), android.os.CancellationSignal.fromTransport(cancelTransport), timestampBegan, prepareGetCredentialCallback);
            java.util.List<com.android.server.credentials.ProviderSession> providerSessions = prepareProviderSessions(request, session);
            if (providerSessions.isEmpty()) {
                try {
                    try {
                    } catch (android.os.RemoteException e) {
                        e = e;
                    }
                } catch (android.os.RemoteException e2) {
                    e = e2;
                }
                try {
                    prepareGetCredentialCallback.onResponse(new android.credentials.PrepareGetCredentialResponseInternal(android.service.credentials.PermissionUtils.hasPermission(com.android.server.credentials.CredentialManagerService.this.mContext, callingPackage, "android.permission.CREDENTIAL_MANAGER_QUERY_CANDIDATE_CREDENTIALS"), (java.util.Set) null, false, false, (android.app.PendingIntent) null));
                } catch (android.os.RemoteException e3) {
                    e = e3;
                    android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Issue invoking onError on IGetCredentialCallback callback: " + e.getMessage());
                }
            }
            invokeProviderSessions(providerSessions);
            return cancelTransport;
        }

        private java.util.List<com.android.server.credentials.ProviderSession> prepareProviderSessions(android.credentials.GetCredentialRequest request, com.android.server.credentials.GetRequestSession session) {
            java.util.List<com.android.server.credentials.ProviderSession> providerSessions;
            if (com.android.server.credentials.CredentialManagerService.isCredentialDescriptionApiEnabled()) {
                java.util.List<android.credentials.CredentialOption> optionsThatRequireActiveCredentials = request.getCredentialOptions().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.credentials.CredentialManagerService.CredentialManagerServiceStub.lambda$prepareProviderSessions$0((android.credentials.CredentialOption) obj);
                    }
                }).toList();
                java.util.List<android.credentials.CredentialOption> optionsThatDoNotRequireActiveCredentials = request.getCredentialOptions().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.credentials.CredentialManagerService.CredentialManagerServiceStub.lambda$prepareProviderSessions$1((android.credentials.CredentialOption) obj);
                    }
                }).toList();
                java.util.Collection<? extends com.android.server.credentials.ProviderSession> sessionsWithoutRemoteService = com.android.server.credentials.CredentialManagerService.this.initiateProviderSessionsWithActiveContainers(session, (java.util.Set<android.util.Pair<android.credentials.CredentialOption, com.android.server.credentials.CredentialDescriptionRegistry.FilterResult>>) com.android.server.credentials.CredentialManagerService.this.getFilteredResultFromRegistry(optionsThatRequireActiveCredentials));
                java.util.Collection<? extends com.android.server.credentials.ProviderSession> sessionsWithRemoteService = com.android.server.credentials.CredentialManagerService.this.initiateProviderSessions(session, (java.util.List) optionsThatDoNotRequireActiveCredentials.stream().map(new com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda3()).collect(java.util.stream.Collectors.toList()));
                java.util.Set<com.android.server.credentials.ProviderSession> all = new java.util.LinkedHashSet<>();
                all.addAll(sessionsWithRemoteService);
                all.addAll(sessionsWithoutRemoteService);
                providerSessions = new java.util.ArrayList(all);
            } else {
                providerSessions = com.android.server.credentials.CredentialManagerService.this.initiateProviderSessions(session, (java.util.List) request.getCredentialOptions().stream().map(new com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda3()).collect(java.util.stream.Collectors.toList()));
            }
            finalizeAndEmitInitialPhaseMetric(session);
            return providerSessions;
        }

        static /* synthetic */ boolean lambda$prepareProviderSessions$0(android.credentials.CredentialOption credentialOption) {
            return credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS") != null;
        }

        static /* synthetic */ boolean lambda$prepareProviderSessions$1(android.credentials.CredentialOption credentialOption) {
            return credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS") == null;
        }

        private void invokeProviderSessions(java.util.List<com.android.server.credentials.ProviderSession> providerSessions) {
            providerSessions.forEach(new com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda0());
        }

        public android.os.ICancellationSignal executeCreateCredential(android.credentials.CreateCredentialRequest request, android.credentials.ICreateCredentialCallback callback, java.lang.String callingPackage) {
            long timestampBegan = java.lang.System.nanoTime();
            android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "starting executeCreateCredential with callingPackage: " + callingPackage);
            android.os.ICancellationSignal cancelTransport = android.os.CancellationSignal.createTransport();
            if (request.getOrigin() != null) {
                com.android.server.credentials.CredentialManagerService.this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ORIGIN", null);
            }
            int userId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.credentials.CredentialManagerService.this.enforceCallingPackage(callingPackage, callingUid);
            com.android.server.credentials.CreateRequestSession session = new com.android.server.credentials.CreateRequestSession(com.android.server.credentials.CredentialManagerService.this.getContext(), com.android.server.credentials.CredentialManagerService.this.mSessionManager, com.android.server.credentials.CredentialManagerService.this.mLock, userId, callingUid, request, callback, com.android.server.credentials.CredentialManagerService.this.constructCallingAppInfo(callingPackage, userId, request.getOrigin()), getEnabledProvidersForUser(userId), com.android.server.credentials.CredentialManagerService.getPrimaryProvidersForUserId(com.android.server.credentials.CredentialManagerService.this.getContext(), userId), android.os.CancellationSignal.fromTransport(cancelTransport), timestampBegan);
            com.android.server.credentials.CredentialManagerService.this.addSessionLocked(userId, session);
            processCreateCredential(request, callback, session);
            return cancelTransport;
        }

        private void processCreateCredential(android.credentials.CreateCredentialRequest request, android.credentials.ICreateCredentialCallback callback, com.android.server.credentials.CreateRequestSession session) {
            java.util.List<com.android.server.credentials.ProviderSession> providerSessions = com.android.server.credentials.CredentialManagerService.this.initiateProviderSessions(session, java.util.List.of(request.getType()));
            if (providerSessions.isEmpty()) {
                try {
                    callback.onError("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "No create options available.");
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Issue invoking onError on ICreateCredentialCallback callback: ", e);
                }
            }
            finalizeAndEmitInitialPhaseMetric(session);
            providerSessions.forEach(new com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda0());
        }

        private void finalizeAndEmitInitialPhaseMetric(com.android.server.credentials.GetCandidateRequestSession session) {
            com.android.server.credentials.metrics.InitialPhaseMetric initMetric = session.mRequestSessionMetric.getInitialPhaseMetric();
            initMetric.setAutofillSessionId(session.getAutofillSessionId());
            initMetric.setAutofillRequestId(session.getAutofillRequestId());
            finalizeAndEmitInitialPhaseMetric((com.android.server.credentials.RequestSession) session);
        }

        private void finalizeAndEmitInitialPhaseMetric(com.android.server.credentials.RequestSession session) {
            try {
                com.android.server.credentials.metrics.InitialPhaseMetric initMetric = session.mRequestSessionMetric.getInitialPhaseMetric();
                initMetric.setCredentialServiceBeginQueryTimeNanoseconds(java.lang.System.nanoTime());
                com.android.server.credentials.MetricUtilities.logApiCalledInitialPhase(initMetric, session.mRequestSessionMetric.returnIncrementSequence());
            } catch (java.lang.Exception e) {
                android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "Unexpected error during metric logging: ", e);
            }
        }

        public void setEnabledProviders(java.util.List<java.lang.String> primaryProviders, java.util.List<java.lang.String> providers, int userId, android.credentials.ISetEnabledProvidersCallback callback) {
            int callingUid = android.os.Binder.getCallingUid();
            if (!com.android.server.credentials.CredentialManagerService.this.hasWriteSecureSettingsPermission()) {
                try {
                    com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.SET_ENABLED_PROVIDERS, com.android.server.credentials.metrics.ApiStatus.FAILURE, callingUid);
                    callback.onError(com.android.server.credentials.CredentialManagerService.PERMISSION_DENIED_ERROR, com.android.server.credentials.CredentialManagerService.PERMISSION_DENIED_WRITE_SECURE_SETTINGS_ERROR);
                    return;
                } catch (android.os.RemoteException e) {
                    com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.SET_ENABLED_PROVIDERS, com.android.server.credentials.metrics.ApiStatus.FAILURE, callingUid);
                    android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Issue with invoking response: ", e);
                    return;
                }
            }
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "setEnabledProviders", null);
            java.util.Set<java.lang.String> enableProvider = new java.util.HashSet<>(providers);
            enableProvider.addAll(primaryProviders);
            boolean writeEnabledStatus = android.provider.Settings.Secure.putStringForUser(com.android.server.credentials.CredentialManagerService.this.getContext().getContentResolver(), "credential_service", java.lang.String.join(":", enableProvider), userId2);
            boolean writePrimaryStatus = android.provider.Settings.Secure.putStringForUser(com.android.server.credentials.CredentialManagerService.this.getContext().getContentResolver(), "credential_service_primary", java.lang.String.join(":", primaryProviders), userId2);
            if (!writeEnabledStatus || !writePrimaryStatus) {
                android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Failed to store setting containing enabled or primary providers");
                try {
                    com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.SET_ENABLED_PROVIDERS, com.android.server.credentials.metrics.ApiStatus.FAILURE, callingUid);
                    callback.onError("failed_setting_store", "Failed to store setting containing enabled or primary providers");
                } catch (android.os.RemoteException e2) {
                    com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.SET_ENABLED_PROVIDERS, com.android.server.credentials.metrics.ApiStatus.FAILURE, callingUid);
                    android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Issue with invoking error response: ", e2);
                    return;
                }
            }
            try {
                com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.SET_ENABLED_PROVIDERS, com.android.server.credentials.metrics.ApiStatus.SUCCESS, callingUid);
                callback.onResponse();
            } catch (android.os.RemoteException e3) {
                com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.SET_ENABLED_PROVIDERS, com.android.server.credentials.metrics.ApiStatus.FAILURE, callingUid);
                android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Issue with invoking response: ", e3);
            }
        }

        public boolean isEnabledCredentialProviderService(android.content.ComponentName componentName, java.lang.String callingPackage) {
            android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "isEnabledCredentialProviderService with componentName: " + componentName.flattenToString());
            int userId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.credentials.CredentialManagerService.this.enforceCallingPackage(callingPackage, callingUid);
            if (componentName == null) {
                android.util.Slog.w(com.android.server.credentials.CredentialManagerService.TAG, "isEnabledCredentialProviderService componentName is null");
                com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.IS_ENABLED_CREDENTIAL_PROVIDER_SERVICE, com.android.server.credentials.metrics.ApiStatus.FAILURE, callingUid);
                return false;
            }
            if (!componentName.getPackageName().equals(callingPackage)) {
                android.util.Slog.w(com.android.server.credentials.CredentialManagerService.TAG, "isEnabledCredentialProviderService component name does not match requested component");
                com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.IS_ENABLED_CREDENTIAL_PROVIDER_SERVICE, com.android.server.credentials.metrics.ApiStatus.FAILURE, callingUid);
                throw new java.lang.IllegalArgumentException("provided component name does not match does not match requesting component");
            }
            java.util.Set<android.content.ComponentName> enabledProviders = getEnabledProvidersForUser(userId);
            com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.IS_ENABLED_CREDENTIAL_PROVIDER_SERVICE, com.android.server.credentials.metrics.ApiStatus.SUCCESS, callingUid);
            if (enabledProviders == null) {
                return false;
            }
            return enabledProviders.contains(componentName);
        }

        public java.util.List<android.credentials.CredentialProviderInfo> getCredentialProviderServices(int userId, int providerFilter) {
            com.android.server.credentials.CredentialManagerService.this.verifyGetProvidersPermission();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.credentials.MetricUtilities.logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName.GET_CREDENTIAL_PROVIDER_SERVICES, com.android.server.credentials.metrics.ApiStatus.SUCCESS, callingUid);
            return android.service.credentials.CredentialProviderInfoFactory.getCredentialProviderServices(com.android.server.credentials.CredentialManagerService.this.mContext, userId, providerFilter, getEnabledProvidersForUser(userId), com.android.server.credentials.CredentialManagerService.getPrimaryProvidersForUserId(com.android.server.credentials.CredentialManagerService.this.mContext, userId));
        }

        public java.util.List<android.credentials.CredentialProviderInfo> getCredentialProviderServicesForTesting(int providerFilter) {
            com.android.server.credentials.CredentialManagerService.this.verifyGetProvidersPermission();
            int userId = android.os.UserHandle.getCallingUserId();
            return android.service.credentials.CredentialProviderInfoFactory.getCredentialProviderServicesForTesting(com.android.server.credentials.CredentialManagerService.this.mContext, userId, providerFilter, getEnabledProvidersForUser(userId), com.android.server.credentials.CredentialManagerService.getPrimaryProvidersForUserId(com.android.server.credentials.CredentialManagerService.this.mContext, userId));
        }

        public boolean isServiceEnabled() {
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                return android.provider.DeviceConfig.getBoolean("credential_manager", com.android.server.credentials.CredentialManagerService.DEVICE_CONFIG_ENABLE_CREDENTIAL_MANAGER, true);
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }

        private java.util.Set<android.content.ComponentName> getEnabledProvidersForUser(int userId) {
            int resolvedUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, "getEnabledProvidersForUser", null);
            java.util.Set<android.content.ComponentName> enabledProviders = new java.util.HashSet<>();
            java.lang.String directValue = android.provider.Settings.Secure.getStringForUser(com.android.server.credentials.CredentialManagerService.this.mContext.getContentResolver(), "credential_service", resolvedUserId);
            if (!android.text.TextUtils.isEmpty(directValue)) {
                java.lang.String[] components = directValue.split(":");
                for (java.lang.String componentString : components) {
                    android.content.ComponentName component = android.content.ComponentName.unflattenFromString(componentString);
                    if (component != null) {
                        enabledProviders.add(component);
                    }
                }
            }
            return enabledProviders;
        }

        /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
        public android.os.ICancellationSignal clearCredentialState(android.credentials.ClearCredentialStateRequest request, android.credentials.IClearCredentialStateCallback callback, java.lang.String callingPackage) {
            long timestampBegan = java.lang.System.nanoTime();
            android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "starting clearCredentialState with callingPackage: " + callingPackage);
            int userId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.credentials.CredentialManagerService.this.enforceCallingPackage(callingPackage, callingUid);
            android.os.ICancellationSignal cancelTransport = android.os.CancellationSignal.createTransport();
            com.android.server.credentials.ClearRequestSession session = new com.android.server.credentials.ClearRequestSession(com.android.server.credentials.CredentialManagerService.this.getContext(), com.android.server.credentials.CredentialManagerService.this.mSessionManager, com.android.server.credentials.CredentialManagerService.this.mLock, userId, callingUid, callback, request, com.android.server.credentials.CredentialManagerService.this.constructCallingAppInfo(callingPackage, userId, null), getEnabledProvidersForUser(userId), android.os.CancellationSignal.fromTransport(cancelTransport), timestampBegan);
            com.android.server.credentials.CredentialManagerService.this.addSessionLocked(userId, session);
            java.util.List<com.android.server.credentials.ProviderSession> providerSessions = com.android.server.credentials.CredentialManagerService.this.initiateProviderSessions(session, java.util.List.of());
            if (providerSessions.isEmpty()) {
                try {
                    callback.onError("UNKNOWN", "No credentials available on this device");
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.credentials.CredentialManagerService.TAG, "Issue invoking onError on IClearCredentialStateCallback callback: ", e);
                }
            }
            finalizeAndEmitInitialPhaseMetric(session);
            providerSessions.forEach(new com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda0());
            return cancelTransport;
        }

        public void registerCredentialDescription(android.credentials.RegisterCredentialDescriptionRequest request, java.lang.String callingPackage) throws java.lang.IllegalArgumentException, com.android.server.credentials.NonCredentialProviderCallerException {
            android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "registerCredentialDescription with callingPackage: " + callingPackage);
            if (!com.android.server.credentials.CredentialManagerService.isCredentialDescriptionApiEnabled()) {
                throw new java.lang.UnsupportedOperationException("Feature not supported");
            }
            com.android.server.credentials.CredentialManagerService.this.enforceCallingPackage(callingPackage, android.os.Binder.getCallingUid());
            com.android.server.credentials.CredentialDescriptionRegistry session = com.android.server.credentials.CredentialDescriptionRegistry.forUser(android.os.UserHandle.getCallingUserId());
            session.executeRegisterRequest(request, callingPackage);
        }

        public void unregisterCredentialDescription(android.credentials.UnregisterCredentialDescriptionRequest request, java.lang.String callingPackage) throws java.lang.IllegalArgumentException {
            android.util.Slog.i(com.android.server.credentials.CredentialManagerService.TAG, "unregisterCredentialDescription with callingPackage: " + callingPackage);
            if (!com.android.server.credentials.CredentialManagerService.isCredentialDescriptionApiEnabled()) {
                throw new java.lang.UnsupportedOperationException("Feature not supported");
            }
            com.android.server.credentials.CredentialManagerService.this.enforceCallingPackage(callingPackage, android.os.Binder.getCallingUid());
            com.android.server.credentials.CredentialDescriptionRegistry session = com.android.server.credentials.CredentialDescriptionRegistry.forUser(android.os.UserHandle.getCallingUserId());
            session.executeUnregisterRequest(request, callingPackage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validateGetCredentialRequest(android.credentials.GetCredentialRequest request) {
        if (request.getOrigin() != null) {
            this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ORIGIN", null);
        }
        enforcePermissionForAllowedProviders(request);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforcePermissionForAllowedProviders(android.credentials.GetCredentialRequest request) {
        boolean containsAllowedProviders = request.getCredentialOptions().stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.credentials.CredentialManagerService.lambda$enforcePermissionForAllowedProviders$3((android.credentials.CredentialOption) obj);
            }
        });
        if (containsAllowedProviders) {
            this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS", null);
        }
    }

    static /* synthetic */ boolean lambda$enforcePermissionForAllowedProviders$3(android.credentials.CredentialOption option) {
        return (option.getAllowedProviders() == null || option.getAllowedProviders().isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addSessionLocked(int userId, com.android.server.credentials.RequestSession requestSession) {
        synchronized (this.mLock) {
            this.mSessionManager.addSession(userId, requestSession.mRequestId, requestSession);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCallingPackage(java.lang.String callingPackage, int callingUid) {
        android.content.pm.PackageManager pm = this.mContext.createContextAsUser(android.os.UserHandle.getUserHandleForUid(callingUid), 0).getPackageManager();
        try {
            int packageUid = pm.getPackageUid(callingPackage, android.content.pm.PackageManager.PackageInfoFlags.of(0L));
            if (packageUid != callingUid) {
                throw new java.lang.SecurityException(callingPackage + " does not belong to uid " + callingUid);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.SecurityException(callingPackage + " not found");
        }
    }

    private class SessionManager implements com.android.server.credentials.RequestSession.SessionLifetime {
        private SessionManager() {
        }

        @Override // com.android.server.credentials.RequestSession.SessionLifetime
        public void onFinishRequestSession(int userId, android.os.IBinder token) {
            if (com.android.server.credentials.CredentialManagerService.this.mRequestSessions.get(userId) != null) {
                ((java.util.Map) com.android.server.credentials.CredentialManagerService.this.mRequestSessions.get(userId)).remove(token);
            }
        }

        public void addSession(int userId, android.os.IBinder token, com.android.server.credentials.RequestSession requestSession) {
            if (com.android.server.credentials.CredentialManagerService.this.mRequestSessions.get(userId) == null) {
                com.android.server.credentials.CredentialManagerService.this.mRequestSessions.put(userId, new java.util.HashMap());
            }
            ((java.util.Map) com.android.server.credentials.CredentialManagerService.this.mRequestSessions.get(userId)).put(token, requestSession);
        }
    }

    public static void updateProvidersWhenPackageRemoved(com.android.server.credentials.CredentialManagerService.SettingsWrapper settingsWrapper, java.lang.String packageName) {
        android.content.ComponentName cn;
        android.util.Slog.i(TAG, "updateProvidersWhenPackageRemoved");
        java.lang.String rawProviders = settingsWrapper.getStringForUser("credential_service_primary", android.os.UserHandle.myUserId());
        if (rawProviders == null) {
            android.util.Slog.w(TAG, "settings key is null");
            return;
        }
        java.util.Set<java.lang.String> primaryProviders = getStoredProviders(rawProviders, packageName);
        if (!settingsWrapper.putStringForUser("credential_service_primary", java.lang.String.join(":", primaryProviders), android.os.UserHandle.myUserId(), true)) {
            android.util.Slog.e(TAG, "Failed to remove primary package: " + packageName);
            return;
        }
        java.lang.String autofillProvider = settingsWrapper.getStringForUser("autofill_service", android.os.UserHandle.myUserId());
        java.lang.String credentialAutofillService = settingsWrapper.mContext.getResources().getString(android.R.string.config_defaultModuleMetadataProvider);
        if (autofillProvider != null && primaryProviders.isEmpty() && !android.text.TextUtils.equals(autofillProvider, credentialAutofillService) && (cn = android.content.ComponentName.unflattenFromString(autofillProvider)) != null && cn.getPackageName().equals(packageName) && !settingsWrapper.putStringForUser("autofill_service", "", android.os.UserHandle.myUserId(), true)) {
            android.util.Slog.e(TAG, "Failed to remove autofill package: " + packageName);
        }
        java.lang.String rawCredentialProviders = settingsWrapper.getStringForUser("credential_service", android.os.UserHandle.myUserId());
        java.util.Set<java.lang.String> credentialProviders = getStoredProviders(rawCredentialProviders, packageName);
        if (!settingsWrapper.putStringForUser("credential_service", java.lang.String.join(":", credentialProviders), android.os.UserHandle.myUserId(), true)) {
            android.util.Slog.e(TAG, "Failed to remove secondary package: " + packageName);
        }
    }

    public static java.util.Set<java.lang.String> getStoredProviders(java.lang.String rawProviders, java.lang.String packageName) {
        java.util.Set<java.lang.String> providers = new java.util.HashSet<>();
        if (rawProviders == null || packageName == null) {
            return providers;
        }
        for (java.lang.String rawComponentName : rawProviders.split(":")) {
            if (android.text.TextUtils.isEmpty(rawComponentName) || rawComponentName.equals("null")) {
                android.util.Slog.d(TAG, "provider component name is empty or null");
            } else {
                android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(rawComponentName);
                if (cn != null && !cn.getPackageName().equals(packageName)) {
                    providers.add(cn.flattenToString());
                }
            }
        }
        return providers;
    }

    public static class SettingsWrapper {
        private final android.content.Context mContext;

        public SettingsWrapper(android.content.Context context) {
            this.mContext = context;
        }

        android.content.ContentResolver getContentResolver() {
            return this.mContext.getContentResolver();
        }

        public java.lang.String getStringForUser(java.lang.String name, int userHandle) {
            return android.provider.Settings.Secure.getStringForUser(getContentResolver(), name, userHandle);
        }

        public boolean putStringForUser(java.lang.String name, java.lang.String value, int userHandle, boolean overrideableByRestore) {
            return android.provider.Settings.Secure.putStringForUser(getContentResolver(), name, value, null, false, userHandle, overrideableByRestore);
        }
    }
}
