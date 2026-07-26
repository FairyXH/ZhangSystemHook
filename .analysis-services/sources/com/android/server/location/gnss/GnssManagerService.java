package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssManagerService {
    private static final java.lang.String ATTRIBUTION_ID = "GnssService";
    private static final int DEBUG_LEVEL_DEBUG = 4;
    private static final int DEBUG_LEVEL_ERROR = 1;
    private static final int DEBUG_LEVEL_IMPORTANT = 3;
    private static final int DEBUG_LEVEL_NONE = 0;
    private static final int DEBUG_LEVEL_VERBOSE = 5;
    private static final int DEBUG_LEVEL_WARNING = 2;
    private final com.android.server.location.gnss.GnssManagerService.GnssCapabilitiesHalModule mCapabilitiesHalModule;
    final android.content.Context mContext;
    private final com.android.server.location.gnss.GnssManagerService.GnssGeofenceHalModule mGeofenceHalModule;
    private final com.android.server.location.gnss.GnssAntennaInfoProvider mGnssAntennaInfoProvider;
    private final android.location.IGpsGeofenceHardware mGnssGeofenceProxy;
    private final com.android.server.location.gnss.GnssLocationProvider mGnssLocationProvider;
    private final com.android.server.location.gnss.GnssMeasurementsProvider mGnssMeasurementsProvider;
    private final com.android.server.location.gnss.GnssMetrics mGnssMetrics;
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;
    private final com.android.server.location.gnss.GnssNavigationMessageProvider mGnssNavigationMessageProvider;
    private final com.android.server.location.gnss.GnssNmeaProvider mGnssNmeaProvider;
    private final com.android.server.location.gnss.GnssStatusProvider mGnssStatusProvider;
    private final com.android.server.location.interfaces.ILocationFreezeProc mLocationFreeze;
    private com.android.server.location.interfaces.IVirtualGnssLocationProvider mVirtualProvider;
    public static final java.lang.String TAG = "GnssManager";
    public static boolean D = android.util.Log.isLoggable(TAG, 3);

    public GnssManagerService(android.content.Context context, com.android.server.location.injector.Injector injector, com.android.server.location.gnss.hal.GnssNative gnssNative) {
        this.mVirtualProvider = null;
        this.mContext = context.createAttributionContext(ATTRIBUTION_ID);
        this.mGnssNative = gnssNative;
        this.mGnssMetrics = new com.android.server.location.gnss.GnssMetrics(this.mContext, com.android.internal.app.IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService("batterystats")), this.mGnssNative);
        this.mGnssLocationProvider = new com.android.server.location.gnss.GnssLocationProvider(this.mContext, this.mGnssNative, this.mGnssMetrics);
        this.mGnssStatusProvider = new com.android.server.location.gnss.GnssStatusProvider(injector, this.mGnssNative);
        this.mGnssNmeaProvider = new com.android.server.location.gnss.GnssNmeaProvider(injector, this.mGnssNative);
        this.mGnssMeasurementsProvider = new com.android.server.location.gnss.GnssMeasurementsProvider(injector, this.mGnssNative);
        this.mGnssNavigationMessageProvider = new com.android.server.location.gnss.GnssNavigationMessageProvider(injector, this.mGnssNative);
        this.mGnssAntennaInfoProvider = new com.android.server.location.gnss.GnssAntennaInfoProvider(this.mGnssNative);
        this.mGnssGeofenceProxy = new com.android.server.location.gnss.GnssGeofenceProxy(this.mGnssNative);
        this.mGeofenceHalModule = new com.android.server.location.gnss.GnssManagerService.GnssGeofenceHalModule(this.mGnssNative);
        this.mCapabilitiesHalModule = new com.android.server.location.gnss.GnssManagerService.GnssCapabilitiesHalModule(this.mGnssNative);
        this.mGnssNative.register();
        ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext)).registerLbsConfigListener(new com.android.server.location.interfaces.IOplusConfigListener() { // from class: com.android.server.location.gnss.GnssManagerService.1
            @Override // com.android.server.location.interfaces.IOplusConfigListener
            public void onDebugLevelChanged(int level) {
                com.android.server.location.gnss.GnssManagerService.D = level >= 3;
                android.util.Log.i(com.android.server.location.gnss.GnssManagerService.TAG, "onDebugLevelChanged, level: " + level + ", D: " + com.android.server.location.gnss.GnssManagerService.D);
            }
        });
        this.mLocationFreeze = (com.android.server.location.interfaces.ILocationFreezeProc) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ILocationFreezeProc.DEFAULT, this.mContext);
        this.mVirtualProvider = (com.android.server.location.interfaces.IVirtualGnssLocationProvider) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IVirtualGnssLocationProvider.DEFAULT, this.mContext);
        if (this.mVirtualProvider != null) {
            this.mVirtualProvider.addGnssStatusProvider(this.mGnssStatusProvider);
            this.mVirtualProvider.addGnssNmeaProvider(this.mGnssNmeaProvider.getGnssNmeaProviderWrapper());
        }
    }

    public void onSystemReady() {
        this.mGnssLocationProvider.onSystemReady();
    }

    public com.android.server.location.gnss.GnssLocationProvider getGnssLocationProvider() {
        return this.mGnssLocationProvider;
    }

    public void addGnssStatusProvider() {
        if (this.mVirtualProvider != null) {
            this.mVirtualProvider.addGnssStatusProvider(this.mGnssStatusProvider);
        }
    }

    public void setAutomotiveGnssSuspended(boolean suspended) {
        this.mGnssLocationProvider.setAutomotiveGnssSuspended(suspended);
    }

    public boolean isAutomotiveGnssSuspended() {
        return this.mGnssLocationProvider.isAutomotiveGnssSuspended();
    }

    public android.location.IGpsGeofenceHardware getGnssGeofenceProxy() {
        return this.mGnssGeofenceProxy;
    }

    public int getGnssYearOfHardware() {
        return this.mGnssNative.getHardwareYear();
    }

    public java.lang.String getGnssHardwareModelName() {
        return this.mGnssNative.getHardwareModelName();
    }

    public android.location.GnssCapabilities getGnssCapabilities() {
        return this.mGnssNative.getCapabilities();
    }

    public java.util.List<android.location.GnssAntennaInfo> getGnssAntennaInfos() {
        return this.mGnssAntennaInfoProvider.getAntennaInfos();
    }

    public int getGnssBatchSize() {
        return this.mGnssLocationProvider.getBatchSize();
    }

    public void registerGnssStatusCallback(android.location.IGnssStatusListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION", null);
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, listenerId);
        this.mGnssStatusProvider.addListener(identity, listener);
        if (this.mVirtualProvider != null) {
            this.mVirtualProvider.addGnssStatusProvider(this.mGnssStatusProvider);
            this.mVirtualProvider.registerGnssStatusCallback();
        }
    }

    public void unregisterGnssStatusCallback(android.location.IGnssStatusListener listener) {
        this.mGnssStatusProvider.removeListener(listener);
        if (this.mVirtualProvider != null) {
            this.mVirtualProvider.unregisterGnssStatusCallback();
        }
    }

    public void registerGnssNmeaCallback(android.location.IGnssNmeaListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION", null);
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, listenerId);
        this.mGnssNmeaProvider.addListener(identity, listener);
        if (this.mVirtualProvider != null) {
            this.mVirtualProvider.registerGnssNmeaCallback();
        }
    }

    public void unregisterGnssNmeaCallback(android.location.IGnssNmeaListener listener) {
        this.mGnssNmeaProvider.removeListener(listener);
        if (this.mVirtualProvider != null) {
            this.mVirtualProvider.unregisterGnssNmeaCallback();
        }
    }

    public void addGnssMeasurementsListener(android.location.GnssMeasurementRequest request, android.location.IGnssMeasurementsListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION", null);
        if (request.isCorrelationVectorOutputsEnabled()) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.LOCATION_HARDWARE", null);
        }
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, listenerId);
        if (this.mLocationFreeze != null && !this.mLocationFreeze.storeMeasurementRequest(this.mGnssMeasurementsProvider, request, identity, listener)) {
            android.util.Log.i(TAG, "the app is freeze, don't store measurement.");
        } else {
            this.mGnssMeasurementsProvider.addListener(request, identity, listener);
        }
    }

    public void injectGnssMeasurementCorrections(android.location.GnssMeasurementCorrections corrections) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.LOCATION_HARDWARE", null);
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION", null);
        if (!this.mGnssNative.injectMeasurementCorrections(corrections)) {
            android.util.Log.w(TAG, "failed to inject GNSS measurement corrections");
        }
    }

    public void removeGnssMeasurementsListener(android.location.IGnssMeasurementsListener listener) {
        if (this.mLocationFreeze != null) {
            this.mLocationFreeze.removeMeasurementRequest(listener.asBinder());
        }
        this.mGnssMeasurementsProvider.removeListener(listener);
    }

    public void addGnssNavigationMessageListener(android.location.IGnssNavigationMessageListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION", null);
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, listenerId);
        this.mGnssNavigationMessageProvider.addListener(identity, listener);
    }

    public void removeGnssNavigationMessageListener(android.location.IGnssNavigationMessageListener listener) {
        this.mGnssNavigationMessageProvider.removeListener(listener);
    }

    public void addGnssAntennaInfoListener(android.location.IGnssAntennaInfoListener listener, java.lang.String packageName, java.lang.String attributionTag, java.lang.String listenerId) {
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, listenerId);
        this.mGnssAntennaInfoProvider.addListener(identity, listener);
    }

    public void removeGnssAntennaInfoListener(android.location.IGnssAntennaInfoListener listener) {
        this.mGnssAntennaInfoProvider.removeListener(listener);
    }

    public void dump(java.io.FileDescriptor fd, android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        if (args.length > 0 && args[0].equals("--gnssmetrics")) {
            ipw.append(this.mGnssMetrics.dumpGnssMetricsAsProtoString());
            return;
        }
        ipw.println("Capabilities: " + this.mGnssNative.getCapabilities());
        ipw.println("GNSS Hardware Model Name: " + getGnssHardwareModelName());
        if (this.mGnssStatusProvider.isSupported()) {
            ipw.println("Status Provider:");
            ipw.increaseIndent();
            this.mGnssStatusProvider.dump(fd, ipw, args);
            ipw.decreaseIndent();
        }
        if (this.mGnssMeasurementsProvider.isSupported()) {
            ipw.println("Measurements Provider:");
            ipw.increaseIndent();
            this.mGnssMeasurementsProvider.dump(fd, ipw, args);
            ipw.decreaseIndent();
        }
        if (this.mGnssNavigationMessageProvider.isSupported()) {
            ipw.println("Navigation Message Provider:");
            ipw.increaseIndent();
            this.mGnssNavigationMessageProvider.dump(fd, ipw, args);
            ipw.decreaseIndent();
        }
        if (this.mGnssAntennaInfoProvider.isSupported()) {
            ipw.println("Antenna Info Provider:");
            ipw.increaseIndent();
            ipw.println("Antenna Infos: " + this.mGnssAntennaInfoProvider.getAntennaInfos());
            this.mGnssAntennaInfoProvider.dump(fd, ipw, args);
            ipw.decreaseIndent();
        }
        com.android.server.location.gnss.GnssPowerStats powerStats = this.mGnssNative.getLastKnownPowerStats();
        if (powerStats != null) {
            ipw.println("Last Known Power Stats:");
            ipw.increaseIndent();
            powerStats.dump(fd, ipw, args, this.mGnssNative.getCapabilities());
            ipw.decreaseIndent();
        }
    }

    private class GnssCapabilitiesHalModule implements com.android.server.location.gnss.hal.GnssNative.BaseCallbacks {
        GnssCapabilitiesHalModule(com.android.server.location.gnss.hal.GnssNative gnssNative) {
            gnssNative.addBaseCallbacks(this);
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
        public void onHalRestarted() {
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
        public void onCapabilitiesChanged(android.location.GnssCapabilities oldCapabilities, android.location.GnssCapabilities newCapabilities) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.content.Intent intent = new android.content.Intent("android.location.action.GNSS_CAPABILITIES_CHANGED").putExtra("android.location.extra.GNSS_CAPABILITIES", newCapabilities).addFlags(1073741824).addFlags(268435456);
                com.android.server.location.gnss.GnssManagerService.this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class GnssGeofenceHalModule implements com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks {
        private android.hardware.location.GeofenceHardwareImpl mGeofenceHardwareImpl;

        GnssGeofenceHalModule(com.android.server.location.gnss.hal.GnssNative gnssNative) {
            gnssNative.setGeofenceCallbacks(this);
        }

        private synchronized android.hardware.location.GeofenceHardwareImpl getGeofenceHardware() {
            if (this.mGeofenceHardwareImpl == null) {
                this.mGeofenceHardwareImpl = android.hardware.location.GeofenceHardwareImpl.getInstance(com.android.server.location.gnss.GnssManagerService.this.mContext);
            }
            return this.mGeofenceHardwareImpl;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReportGeofenceTransition$0(int geofenceId, android.location.Location location, int transition, long timestamp) {
            getGeofenceHardware().reportGeofenceTransition(geofenceId, location, transition, timestamp, 0, android.location.FusedBatchOptions.SourceTechnologies.GNSS);
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks
        public void onReportGeofenceTransition(final int geofenceId, final android.location.Location location, final int transition, final long timestamp) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssManagerService$GnssGeofenceHalModule$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReportGeofenceTransition$0(geofenceId, location, transition, timestamp);
                }
            });
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks
        public void onReportGeofenceStatus(final int status, final android.location.Location location) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssManagerService$GnssGeofenceHalModule$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReportGeofenceStatus$1(status, location);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReportGeofenceStatus$1(int status, android.location.Location location) {
            int monitorStatus = 1;
            if (status == 2) {
                monitorStatus = 0;
            }
            getGeofenceHardware().reportGeofenceMonitorStatus(0, monitorStatus, location, android.location.FusedBatchOptions.SourceTechnologies.GNSS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReportGeofenceAddStatus$2(int geofenceId, int status) {
            getGeofenceHardware().reportGeofenceAddStatus(geofenceId, translateGeofenceStatus(status));
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks
        public void onReportGeofenceAddStatus(final int geofenceId, final int status) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssManagerService$GnssGeofenceHalModule$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReportGeofenceAddStatus$2(geofenceId, status);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReportGeofenceRemoveStatus$3(int geofenceId, int status) {
            getGeofenceHardware().reportGeofenceRemoveStatus(geofenceId, translateGeofenceStatus(status));
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks
        public void onReportGeofenceRemoveStatus(final int geofenceId, final int status) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssManagerService$GnssGeofenceHalModule$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReportGeofenceRemoveStatus$3(geofenceId, status);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReportGeofencePauseStatus$4(int geofenceId, int status) {
            getGeofenceHardware().reportGeofencePauseStatus(geofenceId, translateGeofenceStatus(status));
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks
        public void onReportGeofencePauseStatus(final int geofenceId, final int status) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssManagerService$GnssGeofenceHalModule$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReportGeofencePauseStatus$4(geofenceId, status);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReportGeofenceResumeStatus$5(int geofenceId, int status) {
            getGeofenceHardware().reportGeofenceResumeStatus(geofenceId, translateGeofenceStatus(status));
        }

        @Override // com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks
        public void onReportGeofenceResumeStatus(final int geofenceId, final int status) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssManagerService$GnssGeofenceHalModule$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReportGeofenceResumeStatus$5(geofenceId, status);
                }
            });
        }

        private int translateGeofenceStatus(int status) {
            switch (status) {
                case com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_GENERIC /* -149 */:
                    return 5;
                case com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_INVALID_TRANSITION /* -103 */:
                    return 4;
                case com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_ID_UNKNOWN /* -102 */:
                    return 3;
                case com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_ID_EXISTS /* -101 */:
                    return 2;
                case 0:
                    return 0;
                case 100:
                    return 1;
                default:
                    return -1;
            }
        }
    }
}
