package com.android.server.ambientcontext;

/* JADX INFO: loaded from: classes.dex */
abstract class AmbientContextManagerPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.ambientcontext.AmbientContextManagerPerUserService, com.android.server.ambientcontext.AmbientContextManagerService> {
    private static final java.lang.String TAG = com.android.server.ambientcontext.AmbientContextManagerPerUserService.class.getSimpleName();

    enum ServiceType {
        DEFAULT,
        WEARABLE
    }

    abstract void clearRemoteService();

    abstract void ensureRemoteServiceInitiated();

    abstract int getAmbientContextEventArrayExtraKeyConfig();

    abstract int getAmbientContextPackageNameExtraKeyConfig();

    abstract android.content.ComponentName getComponentName();

    abstract int getConsentComponentConfig();

    abstract java.lang.String getProtectedBindPermission();

    abstract com.android.server.ambientcontext.RemoteAmbientDetectionService getRemoteService();

    abstract com.android.server.ambientcontext.AmbientContextManagerPerUserService.ServiceType getServiceType();

    abstract void setComponentName(android.content.ComponentName componentName);

    AmbientContextManagerPerUserService(com.android.server.ambientcontext.AmbientContextManagerService master, java.lang.Object lock, int userId) {
        super(master, lock, userId);
    }

    public void onQueryServiceStatus(int[] eventTypes, java.lang.String callingPackage, final android.os.RemoteCallback statusCallback) {
        android.util.Slog.d(TAG, "Query event status of " + java.util.Arrays.toString(eventTypes) + " for " + callingPackage);
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                sendStatusCallback(statusCallback, 3);
            } else {
                ensureRemoteServiceInitiated();
                getRemoteService().queryServiceStatus(eventTypes, callingPackage, getServerStatusCallback(new java.util.function.Consumer() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$onQueryServiceStatus$0(statusCallback, (java.lang.Integer) obj);
                    }
                }));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onQueryServiceStatus$0(android.os.RemoteCallback statusCallback, java.lang.Integer statusCode) {
        sendStatusCallback(statusCallback, statusCode.intValue());
    }

    public void onUnregisterObserver(java.lang.String callingPackage) {
        synchronized (this.mLock) {
            stopDetection(callingPackage);
            ((com.android.server.ambientcontext.AmbientContextManagerService) this.mMaster).clientRemoved(this.mUserId, callingPackage);
        }
    }

    public void onStartConsentActivity(int[] eventTypes, java.lang.String callingPackage) {
        android.util.Slog.d(TAG, "Opening consent activity of " + java.util.Arrays.toString(eventTypes) + " for " + callingPackage);
        int userId = getUserId();
        try {
            android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> recentTasks = android.app.ActivityTaskManager.getService().getRecentTasks(1, 0, userId);
            if (recentTasks == null || recentTasks.getList().isEmpty()) {
                android.util.Slog.e(TAG, "Recent task list is empty!");
                return;
            }
            android.app.ActivityManager.RecentTaskInfo task = (android.app.ActivityManager.RecentTaskInfo) recentTasks.getList().get(0);
            if (!callingPackage.equals(task.topActivityInfo.packageName)) {
                android.util.Slog.e(TAG, "Recent task package name: " + task.topActivityInfo.packageName + " doesn't match with client package name: " + callingPackage);
                return;
            }
            android.content.ComponentName consentComponent = getConsentComponent();
            if (consentComponent == null) {
                android.util.Slog.e(TAG, "Consent component not found!");
                return;
            }
            android.util.Slog.d(TAG, "Starting consent activity for " + callingPackage);
            android.content.Intent intent = new android.content.Intent();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    android.content.Context context = getContext();
                    java.lang.String packageNameExtraKey = context.getResources().getString(getAmbientContextPackageNameExtraKeyConfig());
                    java.lang.String eventArrayExtraKey = context.getResources().getString(getAmbientContextEventArrayExtraKeyConfig());
                    intent.setComponent(consentComponent);
                    if (packageNameExtraKey != null) {
                        intent.putExtra(packageNameExtraKey, callingPackage);
                    } else {
                        android.util.Slog.d(TAG, "Missing packageNameExtraKey for consent activity");
                    }
                    if (eventArrayExtraKey != null) {
                        intent.putExtra(eventArrayExtraKey, eventTypes);
                    } else {
                        android.util.Slog.d(TAG, "Missing eventArrayExtraKey for consent activity");
                    }
                    android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic();
                    options.setLaunchTaskId(task.taskId);
                    context.startActivityAsUser(intent, options.toBundle(), context.getUser());
                } catch (android.content.ActivityNotFoundException e) {
                    android.util.Slog.e(TAG, "unable to start consent activity");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(TAG, "Failed to query recent tasks!");
        }
    }

    public void onRegisterObserver(android.app.ambientcontext.AmbientContextEventRequest request, java.lang.String packageName, android.app.ambientcontext.IAmbientContextObserver observer) {
        synchronized (this.mLock) {
            if (!setUpServiceIfNeeded()) {
                android.util.Slog.w(TAG, "Detection service is not available at this moment.");
                completeRegistration(observer, 3);
            } else {
                startDetection(request, packageName, observer);
                ((com.android.server.ambientcontext.AmbientContextManagerService) this.mMaster).newClientAdded(this.mUserId, request, packageName, observer);
            }
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        android.util.Slog.d(TAG, "newServiceInfoLocked with component name: " + serviceComponent.getClassName());
        if (getComponentName() == null || !serviceComponent.getClassName().equals(getComponentName().getClassName())) {
            android.util.Slog.d(TAG, "service name does not match this per user, returning...");
            return null;
        }
        try {
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 0L, this.mUserId);
            if (serviceInfo != null) {
                java.lang.String permission = serviceInfo.permission;
                if (!getProtectedBindPermission().equals(permission)) {
                    throw new java.lang.SecurityException(java.lang.String.format("Service %s requires %s permission. Found %s permission", serviceInfo.getComponentName(), getProtectedBindPermission(), serviceInfo.permission));
                }
            }
            return serviceInfo;
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            super.dumpLocked(prefix, pw);
        }
        com.android.server.ambientcontext.RemoteAmbientDetectionService remoteService = getRemoteService();
        if (remoteService != null) {
            remoteService.dump("", new android.util.IndentingPrintWriter(pw, "  "));
        }
    }

    protected void stopDetection(java.lang.String packageName) {
        android.util.Slog.d(TAG, "Stop detection for " + packageName);
        synchronized (this.mLock) {
            if (getComponentName() != null) {
                ensureRemoteServiceInitiated();
                com.android.server.ambientcontext.RemoteAmbientDetectionService remoteService = getRemoteService();
                remoteService.stopDetection(packageName);
            }
        }
    }

    protected void destroyLocked() {
        android.util.Slog.d(TAG, "Trying to cancel the remote request. Reason: Service destroyed.");
        com.android.server.ambientcontext.RemoteAmbientDetectionService remoteService = getRemoteService();
        if (remoteService != null) {
            synchronized (this.mLock) {
                remoteService.unbind();
                clearRemoteService();
            }
        }
    }

    protected void startDetection(android.app.ambientcontext.AmbientContextEventRequest request, java.lang.String callingPackage, final android.app.ambientcontext.IAmbientContextObserver observer) {
        android.util.Slog.d(TAG, "Requested detection of " + request.getEventTypes());
        synchronized (this.mLock) {
            if (setUpServiceIfNeeded()) {
                ensureRemoteServiceInitiated();
                com.android.server.ambientcontext.RemoteAmbientDetectionService remoteService = getRemoteService();
                remoteService.startDetection(request, callingPackage, createDetectionResultRemoteCallback(), getServerStatusCallback(new java.util.function.Consumer() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$startDetection$1(observer, (java.lang.Integer) obj);
                    }
                }));
            } else {
                android.util.Slog.w(TAG, "No valid component found for AmbientContextDetectionService");
                completeRegistration(observer, 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDetection$1(android.app.ambientcontext.IAmbientContextObserver observer, java.lang.Integer statusCode) {
        completeRegistration(observer, statusCode.intValue());
    }

    protected void completeRegistration(android.app.ambientcontext.IAmbientContextObserver observer, int statusCode) {
        try {
            observer.onRegistrationComplete(statusCode);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to call IAmbientContextObserver.onRegistrationComplete: " + e.getMessage());
        }
    }

    protected void sendStatusCallback(android.os.RemoteCallback statusCallback, int statusCode) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putInt("android.app.ambientcontext.AmbientContextStatusBundleKey", statusCode);
        statusCallback.sendResult(bundle);
    }

    protected void sendDetectionResultIntent(android.app.PendingIntent pendingIntent, java.util.List<android.app.ambientcontext.AmbientContextEvent> events) {
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.app.ambientcontext.extra.AMBIENT_CONTEXT_EVENTS", new java.util.ArrayList(events));
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setPendingIntentBackgroundActivityLaunchAllowed(false);
        try {
            pendingIntent.send(getContext(), 0, intent, null, null, null, options.toBundle());
            android.util.Slog.i(TAG, "Sending PendingIntent to " + pendingIntent.getCreatorPackage() + ": " + events);
        } catch (android.app.PendingIntent.CanceledException e) {
            android.util.Slog.w(TAG, "Couldn't deliver pendingIntent:" + pendingIntent);
        }
    }

    protected android.os.RemoteCallback createDetectionResultRemoteCallback() {
        return new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda0
            public final void onResult(android.os.Bundle bundle) {
                this.f$0.lambda$createDetectionResultRemoteCallback$2(bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDetectionResultRemoteCallback$2(android.os.Bundle result) {
        android.service.ambientcontext.AmbientContextDetectionResult detectionResult = (android.service.ambientcontext.AmbientContextDetectionResult) result.get("android.app.ambientcontext.AmbientContextDetectionResultBundleKey");
        java.lang.String packageName = detectionResult.getPackageName();
        android.app.ambientcontext.IAmbientContextObserver observer = ((com.android.server.ambientcontext.AmbientContextManagerService) this.mMaster).getClientRequestObserver(this.mUserId, packageName);
        if (observer == null) {
            return;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                observer.onEvents(detectionResult.getEvents());
                android.util.Slog.i(TAG, "Got detection result of " + detectionResult.getEvents() + " for " + packageName);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to call IAmbientContextObserver.onEvents: " + e.getMessage());
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean setUpServiceIfNeeded() {
        if (getComponentName() == null) {
            android.content.ComponentName[] componentNames = updateServiceInfoListLocked();
            if (componentNames == null || componentNames.length != 2) {
                android.util.Slog.d(TAG, "updateServiceInfoListLocked returned incorrect componentNames");
                return false;
            }
            switch (getServiceType()) {
                case DEFAULT:
                    setComponentName(componentNames[0]);
                    break;
                case WEARABLE:
                    setComponentName(componentNames[1]);
                    break;
                default:
                    android.util.Slog.d(TAG, "updateServiceInfoListLocked returned unknown service types.");
                    return false;
            }
        }
        if (getComponentName() == null) {
            return false;
        }
        try {
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(getComponentName(), 0L, this.mUserId);
            return serviceInfo != null;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException while setting up service");
            return false;
        }
    }

    private android.os.RemoteCallback getServerStatusCallback(final java.util.function.Consumer<java.lang.Integer> statusConsumer) {
        return new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.ambientcontext.AmbientContextManagerPerUserService$$ExternalSyntheticLambda2
            public final void onResult(android.os.Bundle bundle) {
                com.android.server.ambientcontext.AmbientContextManagerPerUserService.lambda$getServerStatusCallback$3(statusConsumer, bundle);
            }
        });
    }

    static /* synthetic */ void lambda$getServerStatusCallback$3(java.util.function.Consumer statusConsumer, android.os.Bundle result) {
        android.service.ambientcontext.AmbientContextDetectionServiceStatus serviceStatus = (android.service.ambientcontext.AmbientContextDetectionServiceStatus) result.get("android.app.ambientcontext.AmbientContextServiceStatusBundleKey");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int statusCode = serviceStatus.getStatusCode();
            statusConsumer.accept(java.lang.Integer.valueOf(statusCode));
            android.util.Slog.i(TAG, "Got detection status of " + statusCode + " for " + serviceStatus.getPackageName());
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private android.content.ComponentName getConsentComponent() {
        android.content.Context context = getContext();
        java.lang.String consentComponent = context.getResources().getString(getConsentComponentConfig());
        if (android.text.TextUtils.isEmpty(consentComponent)) {
            return null;
        }
        android.util.Slog.i(TAG, "Consent component name: " + consentComponent);
        return android.content.ComponentName.unflattenFromString(consentComponent);
    }
}
