package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
public class LocationTimeZoneManagerService extends android.os.Binder {
    private static final java.lang.String ATTRIBUTION_TAG = "LocationTimeZoneService";
    private static final long BLOCKING_OP_WAIT_DURATION_MILLIS = java.time.Duration.ofSeconds(20).toMillis();
    static final java.lang.String TAG = "LocationTZDetector";
    private final android.content.Context mContext;
    private com.android.server.timezonedetector.location.LocationTimeZoneProviderController mLocationTimeZoneProviderController;
    private com.android.server.timezonedetector.location.LocationTimeZoneProviderControllerEnvironmentImpl mLocationTimeZoneProviderControllerEnvironment;
    private final com.android.server.timezonedetector.ServiceConfigAccessor mServiceConfigAccessor;
    private final com.android.server.timezonedetector.location.LocationTimeZoneManagerService.ProviderConfig mPrimaryProviderConfig = new com.android.server.timezonedetector.location.LocationTimeZoneManagerService.ProviderConfig(0, "primary", "android.service.timezone.PrimaryLocationTimeZoneProviderService");
    private final com.android.server.timezonedetector.location.LocationTimeZoneManagerService.ProviderConfig mSecondaryProviderConfig = new com.android.server.timezonedetector.location.LocationTimeZoneManagerService.ProviderConfig(1, "secondary", "android.service.timezone.SecondaryLocationTimeZoneProviderService");
    private final android.os.Handler mHandler = com.android.server.FgThread.getHandler();
    private final com.android.server.timezonedetector.location.ThreadingDomain mThreadingDomain = new com.android.server.timezonedetector.location.HandlerThreadingDomain(this.mHandler);
    private final java.lang.Object mSharedLock = this.mThreadingDomain.getLockObject();

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.timezonedetector.location.LocationTimeZoneManagerService mService;
        private final com.android.server.timezonedetector.ServiceConfigAccessor mServiceConfigAccessor;

        public Lifecycle(android.content.Context context) {
            super((android.content.Context) java.util.Objects.requireNonNull(context));
            this.mServiceConfigAccessor = com.android.server.timezonedetector.ServiceConfigAccessorImpl.getInstance(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            android.content.Context context = getContext();
            if (this.mServiceConfigAccessor.isGeoTimeZoneDetectionFeatureSupportedInConfig()) {
                this.mService = new com.android.server.timezonedetector.location.LocationTimeZoneManagerService(context, this.mServiceConfigAccessor);
                publishBinderService("location_time_zone_manager", this.mService);
            } else {
                android.util.Slog.d(com.android.server.timezonedetector.location.LocationTimeZoneManagerService.TAG, "Geo time zone detection feature is disabled in config");
            }
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (this.mServiceConfigAccessor.isGeoTimeZoneDetectionFeatureSupportedInConfig()) {
                if (phase == 500) {
                    this.mService.onSystemReady();
                } else if (phase == 600) {
                    this.mService.onSystemThirdPartyAppsCanStart();
                }
            }
        }
    }

    LocationTimeZoneManagerService(android.content.Context context, com.android.server.timezonedetector.ServiceConfigAccessor serviceConfigAccessor) {
        this.mContext = context.createAttributionContext(ATTRIBUTION_TAG);
        this.mServiceConfigAccessor = (com.android.server.timezonedetector.ServiceConfigAccessor) java.util.Objects.requireNonNull(serviceConfigAccessor);
    }

    void onSystemReady() {
        this.mServiceConfigAccessor.addLocationTimeZoneManagerConfigListener(new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda4
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                this.f$0.handleServiceConfigurationChangedOnMainThread();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleServiceConfigurationChangedOnMainThread() {
        this.mThreadingDomain.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.restartIfRequiredOnDomainThread();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restartIfRequiredOnDomainThread() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            if (this.mLocationTimeZoneProviderController != null) {
                stopOnDomainThread();
                startOnDomainThread();
            }
        }
    }

    void onSystemThirdPartyAppsCanStart() {
        startInternal(false);
    }

    void start() {
        enforceManageTimeZoneDetectorPermission();
        startInternal(true);
    }

    private void startInternal(boolean waitForCompletion) {
        java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.startOnDomainThread();
            }
        };
        if (waitForCompletion) {
            this.mThreadingDomain.postAndWait(runnable, BLOCKING_OP_WAIT_DURATION_MILLIS);
        } else {
            this.mThreadingDomain.post(runnable);
        }
    }

    void startWithTestProviders(final java.lang.String testPrimaryProviderPackageName, final java.lang.String testSecondaryProviderPackageName, final boolean recordStateChanges) {
        enforceManageTimeZoneDetectorPermission();
        if (testPrimaryProviderPackageName == null && testSecondaryProviderPackageName == null) {
            throw new java.lang.IllegalArgumentException("One or both test package names must be provided.");
        }
        this.mThreadingDomain.postAndWait(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startWithTestProviders$0(testPrimaryProviderPackageName, testSecondaryProviderPackageName, recordStateChanges);
            }
        }, BLOCKING_OP_WAIT_DURATION_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startWithTestProviders$0(java.lang.String testPrimaryProviderPackageName, java.lang.String testSecondaryProviderPackageName, boolean recordStateChanges) {
        synchronized (this.mSharedLock) {
            stopOnDomainThread();
            this.mServiceConfigAccessor.setTestPrimaryLocationTimeZoneProviderPackageName(testPrimaryProviderPackageName);
            this.mServiceConfigAccessor.setTestSecondaryLocationTimeZoneProviderPackageName(testSecondaryProviderPackageName);
            this.mServiceConfigAccessor.setRecordStateChangesForTests(recordStateChanges);
            startOnDomainThread();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOnDomainThread() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            if (!this.mServiceConfigAccessor.isGeoTimeZoneDetectionFeatureSupported()) {
                debugLog("Not starting location_time_zone_manager: it is disabled in service config");
                return;
            }
            if (this.mLocationTimeZoneProviderController == null) {
                com.android.server.timezonedetector.location.LocationTimeZoneProvider primary = this.mPrimaryProviderConfig.createProvider();
                com.android.server.timezonedetector.location.LocationTimeZoneProvider secondary = this.mSecondaryProviderConfig.createProvider();
                com.android.server.timezonedetector.location.LocationTimeZoneProviderController.MetricsLogger metricsLogger = new com.android.server.timezonedetector.location.RealControllerMetricsLogger();
                boolean recordStateChanges = this.mServiceConfigAccessor.getRecordStateChangesForTests();
                com.android.server.timezonedetector.location.LocationTimeZoneProviderController controller = new com.android.server.timezonedetector.location.LocationTimeZoneProviderController(this.mThreadingDomain, metricsLogger, primary, secondary, recordStateChanges);
                com.android.server.timezonedetector.location.LocationTimeZoneProviderControllerEnvironmentImpl environment = new com.android.server.timezonedetector.location.LocationTimeZoneProviderControllerEnvironmentImpl(this.mThreadingDomain, this.mServiceConfigAccessor, controller);
                com.android.server.timezonedetector.location.LocationTimeZoneProviderControllerCallbackImpl callback = new com.android.server.timezonedetector.location.LocationTimeZoneProviderControllerCallbackImpl(this.mThreadingDomain);
                controller.initialize(environment, callback);
                this.mLocationTimeZoneProviderControllerEnvironment = environment;
                this.mLocationTimeZoneProviderController = controller;
            }
        }
    }

    void stop() {
        enforceManageTimeZoneDetectorPermission();
        this.mThreadingDomain.postAndWait(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.stopOnDomainThread();
            }
        }, BLOCKING_OP_WAIT_DURATION_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopOnDomainThread() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            if (this.mLocationTimeZoneProviderController != null) {
                this.mLocationTimeZoneProviderController.destroy();
                this.mLocationTimeZoneProviderController = null;
                this.mLocationTimeZoneProviderControllerEnvironment.destroy();
                this.mLocationTimeZoneProviderControllerEnvironment = null;
                this.mServiceConfigAccessor.resetVolatileTestConfig();
            }
        }
    }

    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.timezonedetector.location.LocationTimeZoneManagerShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    void clearRecordedProviderStates() {
        enforceManageTimeZoneDetectorPermission();
        this.mThreadingDomain.postAndWait(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$clearRecordedProviderStates$1();
            }
        }, BLOCKING_OP_WAIT_DURATION_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecordedProviderStates$1() {
        synchronized (this.mSharedLock) {
            if (this.mLocationTimeZoneProviderController != null) {
                this.mLocationTimeZoneProviderController.clearRecordedStates();
            }
        }
    }

    com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState getStateForTests() {
        enforceManageTimeZoneDetectorPermission();
        try {
            return (com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState) this.mThreadingDomain.postAndWait(new java.util.concurrent.Callable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return this.f$0.lambda$getStateForTests$2();
                }
            }, BLOCKING_OP_WAIT_DURATION_MILLIS);
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState lambda$getStateForTests$2() throws java.lang.Exception {
        synchronized (this.mSharedLock) {
            if (this.mLocationTimeZoneProviderController == null) {
                return null;
            }
            return this.mLocationTimeZoneProviderController.getStateForTests();
        }
    }

    @Override // android.os.Binder
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
            synchronized (this.mSharedLock) {
                ipw.println("LocationTimeZoneManagerService:");
                ipw.increaseIndent();
                ipw.println("Primary provider config:");
                ipw.increaseIndent();
                this.mPrimaryProviderConfig.dump(ipw, args);
                ipw.decreaseIndent();
                ipw.println("Secondary provider config:");
                ipw.increaseIndent();
                this.mSecondaryProviderConfig.dump(ipw, args);
                ipw.decreaseIndent();
                if (this.mLocationTimeZoneProviderController == null) {
                    ipw.println("{Stopped}");
                } else {
                    this.mLocationTimeZoneProviderController.dump(ipw, args);
                }
                ipw.decreaseIndent();
            }
        }
    }

    static void debugLog(java.lang.String msg) {
        if (android.util.Log.isLoggable(TAG, 3)) {
            android.util.Slog.d(TAG, msg);
        }
    }

    static void infoLog(java.lang.String msg) {
        if (android.util.Log.isLoggable(TAG, 4)) {
            android.util.Slog.i(TAG, msg);
        }
    }

    static void warnLog(java.lang.String msg) {
        warnLog(msg, null);
    }

    static void warnLog(java.lang.String msg, java.lang.Throwable t) {
        if (android.util.Log.isLoggable(TAG, 5)) {
            android.util.Slog.w(TAG, msg, t);
        }
    }

    private void enforceManageTimeZoneDetectorPermission() {
        this.mContext.enforceCallingPermission("android.permission.MANAGE_TIME_AND_ZONE_DETECTION", "manage time and time zone detection");
    }

    private final class ProviderConfig implements com.android.server.timezonedetector.Dumpable {
        private final int mIndex;
        private final java.lang.String mName;
        private final java.lang.String mServiceAction;

        /* JADX WARN: Removed duplicated region for block: B:7:0x000b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        ProviderConfig(int r2, java.lang.String r3, java.lang.String r4) {
            /*
                r0 = this;
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this = r1
                r0.<init>()
                if (r2 < 0) goto Lb
                r1 = 1
                if (r2 > r1) goto Lb
                goto Lc
            Lb:
                r1 = 0
            Lc:
                com.android.internal.util.Preconditions.checkArgument(r1)
                r0.mIndex = r2
                java.lang.Object r1 = java.util.Objects.requireNonNull(r3)
                java.lang.String r1 = (java.lang.String) r1
                r0.mName = r1
                java.lang.Object r1 = java.util.Objects.requireNonNull(r4)
                java.lang.String r1 = (java.lang.String) r1
                r0.mServiceAction = r1
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.timezonedetector.location.LocationTimeZoneManagerService.ProviderConfig.<init>(com.android.server.timezonedetector.location.LocationTimeZoneManagerService, int, java.lang.String, java.lang.String):void");
        }

        com.android.server.timezonedetector.location.LocationTimeZoneProvider createProvider() {
            com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderMetricsLogger providerMetricsLogger = new com.android.server.timezonedetector.location.RealProviderMetricsLogger(this.mIndex);
            java.lang.String mode = getMode();
            if (java.util.Objects.equals(mode, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED)) {
                return new com.android.server.timezonedetector.location.DisabledLocationTimeZoneProvider(providerMetricsLogger, com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mThreadingDomain, this.mName, com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.getRecordStateChangesForTests());
            }
            com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy proxy = createBinderProxy();
            return new com.android.server.timezonedetector.location.BinderLocationTimeZoneProvider(providerMetricsLogger, com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mThreadingDomain, this.mName, proxy, com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.getRecordStateChangesForTests());
        }

        @Override // com.android.server.timezonedetector.Dumpable
        public void dump(android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
            ipw.printf("getMode()=%s\n", new java.lang.Object[]{getMode()});
            ipw.printf("getPackageName()=%s\n", new java.lang.Object[]{getPackageName()});
        }

        private java.lang.String getMode() {
            if (this.mIndex == 0) {
                return com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.getPrimaryLocationTimeZoneProviderMode();
            }
            return com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.getSecondaryLocationTimeZoneProviderMode();
        }

        private com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy createBinderProxy() {
            java.lang.String providerServiceAction = this.mServiceAction;
            boolean isTestProvider = isTestProvider();
            java.lang.String providerPackageName = getPackageName();
            return new com.android.server.timezonedetector.location.RealLocationTimeZoneProviderProxy(com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mContext, com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mHandler, com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mThreadingDomain, providerServiceAction, providerPackageName, isTestProvider);
        }

        private boolean isTestProvider() {
            if (this.mIndex == 0) {
                return com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.isTestPrimaryLocationTimeZoneProvider();
            }
            return com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.isTestSecondaryLocationTimeZoneProvider();
        }

        private java.lang.String getPackageName() {
            if (this.mIndex == 0) {
                return com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.getPrimaryLocationTimeZoneProviderPackageName();
            }
            return com.android.server.timezonedetector.location.LocationTimeZoneManagerService.this.mServiceConfigAccessor.getSecondaryLocationTimeZoneProviderPackageName();
        }
    }
}
