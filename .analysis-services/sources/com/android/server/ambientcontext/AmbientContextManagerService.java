package com.android.server.ambientcontext;

/* JADX INFO: loaded from: classes.dex */
public class AmbientContextManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.ambientcontext.AmbientContextManagerService, com.android.server.ambientcontext.AmbientContextManagerPerUserService> {
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    private static final java.lang.String KEY_SERVICE_ENABLED = "service_enabled";
    public static final int MAX_TEMPORARY_SERVICE_DURATION_MS = 30000;
    private final android.content.Context mContext;
    private java.util.Set<com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest> mExistingClientRequests;
    boolean mIsServiceEnabled;
    boolean mIsWearableServiceEnabled;
    private static final java.lang.String TAG = com.android.server.ambientcontext.AmbientContextManagerService.class.getSimpleName();
    private static final java.util.Set<java.lang.Integer> DEFAULT_EVENT_SET = com.google.android.collect.Sets.newHashSet(new java.lang.Integer[]{1, 2, 3});

    static class ClientRequest {
        private final android.app.ambientcontext.IAmbientContextObserver mObserver;
        private final java.lang.String mPackageName;
        private final android.app.ambientcontext.AmbientContextEventRequest mRequest;
        private final int mUserId;

        ClientRequest(int userId, android.app.ambientcontext.AmbientContextEventRequest request, java.lang.String packageName, android.app.ambientcontext.IAmbientContextObserver observer) {
            this.mUserId = userId;
            this.mRequest = request;
            this.mPackageName = packageName;
            this.mObserver = observer;
        }

        java.lang.String getPackageName() {
            return this.mPackageName;
        }

        android.app.ambientcontext.AmbientContextEventRequest getRequest() {
            return this.mRequest;
        }

        android.app.ambientcontext.IAmbientContextObserver getObserver() {
            return this.mObserver;
        }

        boolean hasUserId(int userId) {
            return this.mUserId == userId;
        }

        boolean hasUserIdAndPackageName(int userId, java.lang.String packageName) {
            return userId == this.mUserId && packageName.equals(getPackageName());
        }
    }

    public AmbientContextManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.array.config_companionPermSyncEnabledPackages, true), null, 68);
        this.mContext = context;
        this.mExistingClientRequests = java.util.concurrent.ConcurrentHashMap.newKeySet();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("ambient_context", new com.android.server.ambientcontext.AmbientContextManagerService.AmbientContextManagerInternal());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService, com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("ambient_context_manager_service", getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.ambientcontext.AmbientContextManagerService$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$onBootPhase$0(properties);
                }
            });
            this.mIsServiceEnabled = android.provider.DeviceConfig.getBoolean("ambient_context_manager_service", KEY_SERVICE_ENABLED, true);
            this.mIsWearableServiceEnabled = android.provider.DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    void newClientAdded(int userId, android.app.ambientcontext.AmbientContextEventRequest request, java.lang.String callingPackage, android.app.ambientcontext.IAmbientContextObserver observer) {
        android.util.Slog.d(TAG, "New client added: " + callingPackage);
        synchronized (this.mExistingClientRequests) {
            this.mExistingClientRequests.removeAll(findExistingRequests(userId, callingPackage));
            this.mExistingClientRequests.add(new com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest(userId, request, callingPackage, observer));
        }
    }

    void clientRemoved(int userId, java.lang.String packageName) {
        android.util.Slog.d(TAG, "Remove client: " + packageName);
        synchronized (this.mExistingClientRequests) {
            this.mExistingClientRequests.removeAll(findExistingRequests(userId, packageName));
        }
    }

    private java.util.Set<com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest> findExistingRequests(int userId, java.lang.String packageName) {
        java.util.Set<com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest> existingRequests = new android.util.ArraySet<>();
        for (com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest clientRequest : this.mExistingClientRequests) {
            if (clientRequest.hasUserIdAndPackageName(userId, packageName)) {
                existingRequests.add(clientRequest);
            }
        }
        return existingRequests;
    }

    android.app.ambientcontext.IAmbientContextObserver getClientRequestObserver(int userId, java.lang.String packageName) {
        synchronized (this.mExistingClientRequests) {
            for (com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest clientRequest : this.mExistingClientRequests) {
                if (clientRequest.hasUserIdAndPackageName(userId, packageName)) {
                    return clientRequest.getObserver();
                }
            }
            return null;
        }
    }

    private void onDeviceConfigChange(java.util.Set<java.lang.String> keys) {
        if (keys.contains(KEY_SERVICE_ENABLED)) {
            this.mIsServiceEnabled = android.provider.DeviceConfig.getBoolean("ambient_context_manager_service", KEY_SERVICE_ENABLED, true);
            this.mIsWearableServiceEnabled = android.provider.DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.ambientcontext.AmbientContextManagerPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return null;
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected java.util.List<com.android.server.ambientcontext.AmbientContextManagerPerUserService> newServiceListLocked(int resolvedUserId, boolean disabled, java.lang.String[] serviceNames) {
        if (serviceNames == null || serviceNames.length == 0) {
            android.util.Slog.i(TAG, "serviceNames sent in newServiceListLocked is null, or empty");
            return new java.util.ArrayList();
        }
        java.util.List<com.android.server.ambientcontext.AmbientContextManagerPerUserService> serviceList = new java.util.ArrayList<>(serviceNames.length);
        if (serviceNames.length == 2 && !isDefaultService(serviceNames[0]) && !isDefaultWearableService(serviceNames[1])) {
            android.util.Slog.i(TAG, "Not using default services, services provided for testing should be exactly two services.");
            serviceList.add(new com.android.server.ambientcontext.DefaultAmbientContextManagerPerUserService(this, this.mLock, resolvedUserId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.DEFAULT, serviceNames[0]));
            serviceList.add(new com.android.server.ambientcontext.WearableAmbientContextManagerPerUserService(this, this.mLock, resolvedUserId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.WEARABLE, serviceNames[1]));
            return serviceList;
        }
        if (serviceNames.length > 2) {
            android.util.Slog.i(TAG, "Incorrect number of services provided for testing.");
        }
        for (java.lang.String serviceName : serviceNames) {
            android.util.Slog.d(TAG, "newServicesListLocked with service name: " + serviceName);
            if (getServiceType(serviceName) == com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.WEARABLE) {
                serviceList.add(new com.android.server.ambientcontext.WearableAmbientContextManagerPerUserService(this, this.mLock, resolvedUserId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.WEARABLE, serviceName));
            } else {
                serviceList.add(new com.android.server.ambientcontext.DefaultAmbientContextManagerPerUserService(this, this.mLock, resolvedUserId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.DEFAULT, serviceName));
            }
        }
        return serviceList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(com.android.server.ambientcontext.AmbientContextManagerPerUserService service, int userId) {
        android.util.Slog.d(TAG, "onServiceRemoved");
        service.destroyLocked();
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int userId) {
        android.util.Slog.d(TAG, "Restoring remote request. Reason: Service package restarted.");
        restorePreviouslyEnabledClients(userId);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int userId) {
        android.util.Slog.d(TAG, "Restoring remote request. Reason: Service package updated.");
        restorePreviouslyEnabledClients(userId);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 30000;
    }

    void startDetection(int userId, android.app.ambientcontext.AmbientContextEventRequest request, java.lang.String packageName, android.app.ambientcontext.IAmbientContextObserver observer) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
        synchronized (this.mLock) {
            com.android.server.ambientcontext.AmbientContextManagerPerUserService service = getAmbientContextManagerPerUserServiceForEventTypes(userId, request.getEventTypes());
            if (service != null) {
                service.startDetection(request, packageName, observer);
            } else {
                android.util.Slog.i(TAG, "service not available for user_id: " + userId);
            }
        }
    }

    void stopAmbientContextEvent(int userId, java.lang.String packageName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
        synchronized (this.mLock) {
            for (com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest cr : this.mExistingClientRequests) {
                android.util.Slog.i(TAG, "Looping through clients");
                if (cr.hasUserIdAndPackageName(userId, packageName)) {
                    android.util.Slog.i(TAG, "we have an existing client");
                    com.android.server.ambientcontext.AmbientContextManagerPerUserService service = getAmbientContextManagerPerUserServiceForEventTypes(userId, cr.getRequest().getEventTypes());
                    if (service != null) {
                        service.stopDetection(packageName);
                    } else {
                        android.util.Slog.i(TAG, "service not available for user_id: " + userId);
                    }
                }
            }
        }
    }

    void queryServiceStatus(int userId, java.lang.String packageName, int[] eventTypes, android.os.RemoteCallback callback) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
        synchronized (this.mLock) {
            com.android.server.ambientcontext.AmbientContextManagerPerUserService service = getAmbientContextManagerPerUserServiceForEventTypes(userId, intArrayToIntegerSet(eventTypes));
            if (service != null) {
                service.onQueryServiceStatus(eventTypes, packageName, callback);
            } else {
                android.util.Slog.i(TAG, "query service not available for user_id: " + userId);
            }
        }
    }

    private void restorePreviouslyEnabledClients(int userId) {
        synchronized (this.mLock) {
            java.util.List<com.android.server.ambientcontext.AmbientContextManagerPerUserService> services = getServiceListForUserLocked(userId);
            for (com.android.server.ambientcontext.AmbientContextManagerPerUserService service : services) {
                for (com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest clientRequest : this.mExistingClientRequests) {
                    if (clientRequest.hasUserId(userId)) {
                        android.util.Slog.d(TAG, "Restoring detection for " + clientRequest.getPackageName());
                        service.startDetection(clientRequest.getRequest(), clientRequest.getPackageName(), clientRequest.getObserver());
                    }
                }
            }
        }
    }

    public android.content.ComponentName getComponentName(int userId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType serviceType) {
        synchronized (this.mLock) {
            com.android.server.ambientcontext.AmbientContextManagerPerUserService service = getServiceForType(userId, serviceType);
            if (service != null) {
                return service.getComponentName();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.ambientcontext.AmbientContextManagerPerUserService getAmbientContextManagerPerUserServiceForEventTypes(int userId, java.util.Set<java.lang.Integer> eventTypes) {
        if (isWearableEventTypesOnly(eventTypes)) {
            return getServiceForType(userId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.WEARABLE);
        }
        return getServiceForType(userId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.DEFAULT);
    }

    private com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType getServiceType(java.lang.String serviceName) {
        java.lang.String wearableService = this.mContext.getResources().getString(android.R.string.config_displayWhiteBalanceColorTemperatureSensorName);
        if (wearableService != null && wearableService.equals(serviceName)) {
            return com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.WEARABLE;
        }
        return com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.DEFAULT;
    }

    private boolean isDefaultService(java.lang.String serviceName) {
        java.lang.String defaultService = this.mContext.getResources().getString(android.R.string.config_defaultContentSuggestionsService);
        if (defaultService != null && defaultService.equals(serviceName)) {
            return true;
        }
        return false;
    }

    private boolean isDefaultWearableService(java.lang.String serviceName) {
        java.lang.String wearableService = this.mContext.getResources().getString(android.R.string.config_displayWhiteBalanceColorTemperatureSensorName);
        if (wearableService != null && wearableService.equals(serviceName)) {
            return true;
        }
        return false;
    }

    private com.android.server.ambientcontext.AmbientContextManagerPerUserService getServiceForType(int userId, com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType serviceType) {
        android.util.Slog.d(TAG, "getServiceForType with userid: " + userId + " service type: " + serviceType.name());
        synchronized (this.mLock) {
            java.util.List<com.android.server.ambientcontext.AmbientContextManagerPerUserService> services = getServiceListForUserLocked(userId);
            android.util.Slog.d(TAG, "Services that are available: " + (services == null ? "null services" : services.size() + " number of services"));
            if (services == null) {
                return null;
            }
            for (com.android.server.ambientcontext.AmbientContextManagerPerUserService service : services) {
                if (service.getServiceType() == serviceType) {
                    return service;
                }
            }
            return null;
        }
    }

    private boolean isWearableEventTypesOnly(java.util.Set<java.lang.Integer> eventTypes) {
        if (eventTypes.isEmpty()) {
            android.util.Slog.d(TAG, "empty event types.");
            return false;
        }
        for (java.lang.Integer eventType : eventTypes) {
            if (eventType.intValue() < 100000) {
                android.util.Slog.d(TAG, "Not all events types are wearable events.");
                return false;
            }
        }
        android.util.Slog.d(TAG, "only wearable events.");
        return true;
    }

    private boolean isWearableEventTypesOnly(int[] eventTypes) {
        java.lang.Integer[] events = intArrayToIntegerArray(eventTypes);
        return isWearableEventTypesOnly(new java.util.HashSet(java.util.Arrays.asList(events)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean containsMixedEvents(int[] eventTypes) {
        if (isWearableEventTypesOnly(eventTypes)) {
            return false;
        }
        for (int i : eventTypes) {
            java.lang.Integer event = java.lang.Integer.valueOf(i);
            if (!DEFAULT_EVENT_SET.contains(event)) {
                android.util.Slog.w(TAG, "Received mixed event types, this is not supported.");
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] integerSetToIntArray(java.util.Set<java.lang.Integer> integerSet) {
        int[] intArray = new int[integerSet.size()];
        int i = 0;
        for (java.lang.Integer type : integerSet) {
            intArray[i] = type.intValue();
            i++;
        }
        return intArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.Set<java.lang.Integer> intArrayToIntegerSet(int[] eventTypes) {
        java.util.Set<java.lang.Integer> types = new java.util.HashSet<>();
        for (int i : eventTypes) {
            java.lang.Integer i2 = java.lang.Integer.valueOf(i);
            types.add(i2);
        }
        return types;
    }

    private static java.lang.Integer[] intArrayToIntegerArray(int[] integerSet) {
        java.lang.Integer[] intArray = new java.lang.Integer[integerSet.length];
        int i = 0;
        int length = integerSet.length;
        int i2 = 0;
        while (i2 < length) {
            java.lang.Integer type = java.lang.Integer.valueOf(integerSet[i2]);
            intArray[i] = type;
            i2++;
            i++;
        }
        return intArray;
    }

    private final class AmbientContextManagerInternal extends android.app.ambientcontext.IAmbientContextManager.Stub {
        private AmbientContextManagerInternal() {
        }

        public void registerObserver(android.app.ambientcontext.AmbientContextEventRequest request, final android.app.PendingIntent resultPendingIntent, final android.os.RemoteCallback statusCallback) {
            java.util.Objects.requireNonNull(request);
            java.util.Objects.requireNonNull(resultPendingIntent);
            java.util.Objects.requireNonNull(statusCallback);
            final com.android.server.ambientcontext.AmbientContextManagerPerUserService service = com.android.server.ambientcontext.AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(android.os.UserHandle.getCallingUserId(), request.getEventTypes());
            registerObserverWithCallback(request, resultPendingIntent.getCreatorPackage(), new android.app.ambientcontext.IAmbientContextObserver.Stub() { // from class: com.android.server.ambientcontext.AmbientContextManagerService.AmbientContextManagerInternal.1
                public void onEvents(java.util.List<android.app.ambientcontext.AmbientContextEvent> events) throws android.os.RemoteException {
                    service.sendDetectionResultIntent(resultPendingIntent, events);
                }

                public void onRegistrationComplete(int statusCode) throws android.os.RemoteException {
                    service.sendStatusCallback(statusCallback, statusCode);
                }
            });
        }

        public void registerObserverWithCallback(android.app.ambientcontext.AmbientContextEventRequest request, java.lang.String packageName, android.app.ambientcontext.IAmbientContextObserver observer) {
            android.util.Slog.i(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "AmbientContextManagerService registerObserverWithCallback.");
            java.util.Objects.requireNonNull(request);
            java.util.Objects.requireNonNull(packageName);
            java.util.Objects.requireNonNull(observer);
            com.android.server.ambientcontext.AmbientContextManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", com.android.server.ambientcontext.AmbientContextManagerService.TAG);
            com.android.server.ambientcontext.AmbientContextManagerService.this.assertCalledByPackageOwner(packageName);
            com.android.server.ambientcontext.AmbientContextManagerPerUserService service = com.android.server.ambientcontext.AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(android.os.UserHandle.getCallingUserId(), request.getEventTypes());
            if (service == null) {
                android.util.Slog.w(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "onRegisterObserver unavailable user_id: " + android.os.UserHandle.getCallingUserId());
                return;
            }
            int statusCode = checkStatusCode(service, com.android.server.ambientcontext.AmbientContextManagerService.integerSetToIntArray(request.getEventTypes()));
            if (statusCode == 1) {
                service.onRegisterObserver(request, packageName, observer);
            } else {
                service.completeRegistration(observer, statusCode);
            }
        }

        public void unregisterObserver(java.lang.String callingPackage) {
            unregisterObserver_enforcePermission();
            com.android.server.ambientcontext.AmbientContextManagerService.this.assertCalledByPackageOwner(callingPackage);
            synchronized (com.android.server.ambientcontext.AmbientContextManagerService.this.mLock) {
                for (com.android.server.ambientcontext.AmbientContextManagerService.ClientRequest cr : com.android.server.ambientcontext.AmbientContextManagerService.this.mExistingClientRequests) {
                    if (cr != null && cr.getPackageName().equals(callingPackage)) {
                        com.android.server.ambientcontext.AmbientContextManagerPerUserService service = com.android.server.ambientcontext.AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(android.os.UserHandle.getCallingUserId(), cr.getRequest().getEventTypes());
                        if (service != null) {
                            service.onUnregisterObserver(callingPackage);
                        } else {
                            android.util.Slog.w(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "onUnregisterObserver unavailable user_id: " + android.os.UserHandle.getCallingUserId());
                        }
                    }
                }
            }
        }

        public void queryServiceStatus(int[] eventTypes, java.lang.String callingPackage, android.os.RemoteCallback statusCallback) {
            java.util.Objects.requireNonNull(eventTypes);
            java.util.Objects.requireNonNull(callingPackage);
            java.util.Objects.requireNonNull(statusCallback);
            com.android.server.ambientcontext.AmbientContextManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", com.android.server.ambientcontext.AmbientContextManagerService.TAG);
            com.android.server.ambientcontext.AmbientContextManagerService.this.assertCalledByPackageOwner(callingPackage);
            synchronized (com.android.server.ambientcontext.AmbientContextManagerService.this.mLock) {
                com.android.server.ambientcontext.AmbientContextManagerPerUserService service = com.android.server.ambientcontext.AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(android.os.UserHandle.getCallingUserId(), com.android.server.ambientcontext.AmbientContextManagerService.this.intArrayToIntegerSet(eventTypes));
                if (service == null) {
                    android.util.Slog.w(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "queryServiceStatus unavailable user_id: " + android.os.UserHandle.getCallingUserId());
                    return;
                }
                int statusCode = checkStatusCode(service, eventTypes);
                if (statusCode == 1) {
                    service.onQueryServiceStatus(eventTypes, callingPackage, statusCallback);
                } else {
                    service.sendStatusCallback(statusCallback, statusCode);
                }
            }
        }

        public void startConsentActivity(int[] eventTypes, java.lang.String callingPackage) {
            java.util.Objects.requireNonNull(eventTypes);
            java.util.Objects.requireNonNull(callingPackage);
            com.android.server.ambientcontext.AmbientContextManagerService.this.assertCalledByPackageOwner(callingPackage);
            com.android.server.ambientcontext.AmbientContextManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", com.android.server.ambientcontext.AmbientContextManagerService.TAG);
            if (com.android.server.ambientcontext.AmbientContextManagerService.this.containsMixedEvents(eventTypes)) {
                android.util.Slog.d(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "AmbientContextEventRequest contains mixed events, this is not supported.");
                return;
            }
            com.android.server.ambientcontext.AmbientContextManagerPerUserService service = com.android.server.ambientcontext.AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(android.os.UserHandle.getCallingUserId(), com.android.server.ambientcontext.AmbientContextManagerService.this.intArrayToIntegerSet(eventTypes));
            if (service != null) {
                service.onStartConsentActivity(eventTypes, callingPackage);
            } else {
                android.util.Slog.w(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "startConsentActivity unavailable user_id: " + android.os.UserHandle.getCallingUserId());
            }
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.ambientcontext.AmbientContextManagerService.this.mContext, com.android.server.ambientcontext.AmbientContextManagerService.TAG, pw)) {
                synchronized (com.android.server.ambientcontext.AmbientContextManagerService.this.mLock) {
                    com.android.server.ambientcontext.AmbientContextManagerService.this.dumpLocked("", pw);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.ambientcontext.AmbientContextShellCommand(com.android.server.ambientcontext.AmbientContextManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        private int checkStatusCode(com.android.server.ambientcontext.AmbientContextManagerPerUserService service, int[] eventTypes) {
            if (service.getServiceType() == com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.DEFAULT && !com.android.server.ambientcontext.AmbientContextManagerService.this.mIsServiceEnabled) {
                android.util.Slog.d(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "Service not enabled.");
                return 3;
            }
            if (service.getServiceType() == com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType.WEARABLE && !com.android.server.ambientcontext.AmbientContextManagerService.this.mIsWearableServiceEnabled) {
                android.util.Slog.d(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "Wearable Service not available.");
                return 3;
            }
            if (com.android.server.ambientcontext.AmbientContextManagerService.this.containsMixedEvents(eventTypes)) {
                android.util.Slog.d(com.android.server.ambientcontext.AmbientContextManagerService.TAG, "AmbientContextEventRequest contains mixed events, this is not supported.");
                return 2;
            }
            return 1;
        }
    }
}
