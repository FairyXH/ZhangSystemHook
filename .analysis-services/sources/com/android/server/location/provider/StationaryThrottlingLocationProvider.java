package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
public final class StationaryThrottlingLocationProvider extends com.android.server.location.provider.DelegateLocationProvider implements com.android.server.location.injector.DeviceIdleHelper.DeviceIdleListener, com.android.server.DeviceIdleInternal.StationaryListener {
    private static final long MAX_STATIONARY_LOCATION_AGE_MS = 30000;
    private static final long MIN_INTERVAL_MS = 1000;
    com.android.server.location.provider.StationaryThrottlingLocationProvider.DeliverLastLocationRunnable mDeliverLastLocationCallback;
    private boolean mDeviceIdle;
    private final com.android.server.location.injector.DeviceIdleHelper mDeviceIdleHelper;
    private boolean mDeviceStationary;
    private final com.android.server.location.injector.DeviceStationaryHelper mDeviceStationaryHelper;
    private long mDeviceStationaryRealtimeMs;
    private android.location.provider.ProviderRequest mIncomingRequest;
    android.location.Location mLastLocation;
    final java.lang.Object mLock;
    private final java.lang.String mName;
    private android.location.provider.ProviderRequest mOutgoingRequest;
    private com.android.server.location.provider.IStationaryThrottlingLocationProviderWrapper mStationaryThrottlingLocationProviderWrapper;
    long mThrottlingIntervalMs;

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider.Listener
    public /* bridge */ /* synthetic */ void onStateChanged(com.android.server.location.provider.AbstractLocationProvider.State state, com.android.server.location.provider.AbstractLocationProvider.State state2) {
        super.onStateChanged(state, state2);
    }

    public StationaryThrottlingLocationProvider(java.lang.String name, com.android.server.location.injector.Injector injector, com.android.server.location.provider.AbstractLocationProvider delegate) {
        super(com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, delegate);
        this.mLock = new java.lang.Object();
        this.mDeviceIdle = false;
        this.mDeviceStationary = false;
        this.mDeviceStationaryRealtimeMs = Long.MIN_VALUE;
        this.mIncomingRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
        this.mOutgoingRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
        this.mThrottlingIntervalMs = Long.MAX_VALUE;
        this.mDeliverLastLocationCallback = null;
        this.mStationaryThrottlingLocationProviderWrapper = new com.android.server.location.provider.StationaryThrottlingLocationProvider.StationaryThrottlingLocationProviderWrapper();
        this.mName = name;
        this.mDeviceIdleHelper = injector.getDeviceIdleHelper();
        this.mDeviceStationaryHelper = injector.getDeviceStationaryHelper();
        ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null)).onStationaryThrottlingLocationProviderInit(name, this);
        initializeDelegate();
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onReportLocation(android.location.LocationResult locationResult) {
        super.onReportLocation(locationResult);
        synchronized (this.mLock) {
            this.mLastLocation = locationResult.getLastLocation();
            onThrottlingChangedLocked(false);
        }
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    protected void onStart() {
        this.mDelegate.getController().start();
        synchronized (this.mLock) {
            this.mDeviceIdleHelper.addListener(this);
            onDeviceIdleChanged(this.mDeviceIdleHelper.isDeviceIdle());
        }
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    protected void onStop() {
        synchronized (this.mLock) {
            this.mDeviceIdleHelper.removeListener(this);
            onDeviceIdleChanged(false);
            this.mIncomingRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
            this.mOutgoingRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
            this.mThrottlingIntervalMs = Long.MAX_VALUE;
            if (this.mDeliverLastLocationCallback != null) {
                com.android.server.FgThread.getHandler().removeCallbacks(this.mDeliverLastLocationCallback);
                this.mDeliverLastLocationCallback = null;
            }
            this.mLastLocation = null;
        }
        this.mDelegate.getController().stop();
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    protected void onSetRequest(android.location.provider.ProviderRequest request) {
        synchronized (this.mLock) {
            this.mIncomingRequest = request;
            onThrottlingChangedLocked(true);
        }
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper.DeviceIdleListener
    public void onDeviceIdleChanged(boolean deviceIdle) {
        synchronized (this.mLock) {
            if (deviceIdle == this.mDeviceIdle) {
                return;
            }
            this.mDeviceIdle = deviceIdle;
            if (deviceIdle) {
                this.mDeviceStationaryHelper.addListener(this);
            } else {
                this.mDeviceStationaryHelper.removeListener(this);
                this.mDeviceStationary = false;
                this.mDeviceStationaryRealtimeMs = Long.MIN_VALUE;
                onThrottlingChangedLocked(false);
            }
        }
    }

    public void onDeviceStationaryChanged(boolean deviceStationary) {
        synchronized (this.mLock) {
            if (this.mDeviceIdle) {
                if (this.mDeviceStationary == deviceStationary) {
                    return;
                }
                this.mDeviceStationary = deviceStationary;
                if (this.mDeviceStationary) {
                    this.mDeviceStationaryRealtimeMs = android.os.SystemClock.elapsedRealtime();
                } else {
                    this.mDeviceStationaryRealtimeMs = Long.MIN_VALUE;
                }
                onThrottlingChangedLocked(false);
            }
        }
    }

    private void onThrottlingChangedLocked(boolean deliverImmediate) {
        android.location.provider.ProviderRequest newRequest;
        com.android.server.location.interfaces.IOplusLBSMainClass oplusLBSMainClass;
        com.android.server.location.interfaces.IOplusLBSMainClass oplusLBSMainClass2;
        long throttlingIntervalMs = Long.MAX_VALUE;
        if (this.mDeviceStationary && this.mDeviceIdle && !this.mIncomingRequest.isLocationSettingsIgnored() && this.mIncomingRequest.getQuality() != 100 && this.mLastLocation != null && this.mLastLocation.getElapsedRealtimeAgeMillis(this.mDeviceStationaryRealtimeMs) <= 30000) {
            throttlingIntervalMs = java.lang.Math.max(this.mIncomingRequest.getIntervalMillis(), 1000L);
        }
        if (throttlingIntervalMs != Long.MAX_VALUE) {
            newRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
        } else {
            newRequest = this.mIncomingRequest;
        }
        if (!newRequest.equals(this.mOutgoingRequest)) {
            this.mOutgoingRequest = newRequest;
            this.mDelegate.getController().setRequest(this.mOutgoingRequest);
        }
        if (throttlingIntervalMs == this.mThrottlingIntervalMs) {
            return;
        }
        long oldThrottlingIntervalMs = this.mThrottlingIntervalMs;
        this.mThrottlingIntervalMs = throttlingIntervalMs;
        if (this.mThrottlingIntervalMs != Long.MAX_VALUE) {
            if (oldThrottlingIntervalMs == Long.MAX_VALUE) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, this.mName + " provider stationary throttled");
                com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderStationaryThrottled(this.mName, true, this.mOutgoingRequest);
                if (com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(this.mName) && (oplusLBSMainClass2 = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null)) != null && oplusLBSMainClass2.getGnssMeasurementsProvider() != null) {
                    oplusLBSMainClass2.getGnssMeasurementsProvider().getGnssMeasurementsProviderWrapper().stopMeasurementCollection();
                }
            }
            if (this.mDeliverLastLocationCallback != null) {
                com.android.server.FgThread.getHandler().removeCallbacks(this.mDeliverLastLocationCallback);
            }
            this.mDeliverLastLocationCallback = new com.android.server.location.provider.StationaryThrottlingLocationProvider.DeliverLastLocationRunnable();
            com.android.internal.util.Preconditions.checkState(this.mLastLocation != null);
            if (deliverImmediate) {
                com.android.server.FgThread.getHandler().post(this.mDeliverLastLocationCallback);
                return;
            } else {
                long delayMs = this.mThrottlingIntervalMs - this.mLastLocation.getElapsedRealtimeAgeMillis();
                com.android.server.FgThread.getHandler().postDelayed(this.mDeliverLastLocationCallback, delayMs);
                return;
            }
        }
        if (oldThrottlingIntervalMs != Long.MAX_VALUE) {
            com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logProviderStationaryThrottled(this.mName, false, this.mOutgoingRequest);
            if (com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(this.mName) && (oplusLBSMainClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, null)) != null && oplusLBSMainClass.getGnssMeasurementsProvider() != null) {
                oplusLBSMainClass.getGnssMeasurementsProvider().getGnssMeasurementsProviderWrapper().restart();
            }
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, this.mName + " provider stationary unthrottled");
        }
        com.android.server.FgThread.getHandler().removeCallbacks(this.mDeliverLastLocationCallback);
        this.mDeliverLastLocationCallback = null;
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (this.mThrottlingIntervalMs != Long.MAX_VALUE) {
            pw.println("stationary throttled=" + this.mLastLocation);
        } else {
            pw.print("stationary throttled=false");
            if (!this.mDeviceIdle) {
                pw.print(" (not idle)");
            }
            if (!this.mDeviceStationary) {
                pw.print(" (not stationary)");
            }
            pw.println();
        }
        this.mDelegate.dump(fd, pw, args);
    }

    private class DeliverLastLocationRunnable implements java.lang.Runnable {
        DeliverLastLocationRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.location.provider.StationaryThrottlingLocationProvider.this.mLock) {
                if (com.android.server.location.provider.StationaryThrottlingLocationProvider.this.mDeliverLastLocationCallback != this) {
                    return;
                }
                if (com.android.server.location.provider.StationaryThrottlingLocationProvider.this.mLastLocation == null) {
                    return;
                }
                android.location.Location location = new android.location.Location(com.android.server.location.provider.StationaryThrottlingLocationProvider.this.mLastLocation);
                location.setTime(java.lang.System.currentTimeMillis());
                location.setElapsedRealtimeNanos(android.os.SystemClock.elapsedRealtimeNanos());
                if (location.hasSpeed()) {
                    location.removeSpeed();
                    if (location.hasSpeedAccuracy()) {
                        location.removeSpeedAccuracy();
                    }
                }
                if (location.hasBearing()) {
                    location.removeBearing();
                    if (location.hasBearingAccuracy()) {
                        location.removeBearingAccuracy();
                    }
                }
                com.android.server.location.provider.StationaryThrottlingLocationProvider.this.mLastLocation = location;
                com.android.server.FgThread.getHandler().postDelayed(this, com.android.server.location.provider.StationaryThrottlingLocationProvider.this.mThrottlingIntervalMs);
                com.android.server.location.provider.StationaryThrottlingLocationProvider.this.reportLocation(android.location.LocationResult.wrap(new android.location.Location[]{location}));
            }
        }
    }

    public com.android.server.location.provider.IStationaryThrottlingLocationProviderWrapper getStationaryThrottlingLocationProviderWrapper() {
        return this.mStationaryThrottlingLocationProviderWrapper;
    }

    private class StationaryThrottlingLocationProviderWrapper implements com.android.server.location.provider.IStationaryThrottlingLocationProviderWrapper {
        private StationaryThrottlingLocationProviderWrapper() {
        }

        @Override // com.android.server.location.provider.IStationaryThrottlingLocationProviderWrapper
        public void onThrottledModeSwitch(boolean throttled) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "onThrottledModeSwitch:" + throttled);
            android.location.Location loc = new android.location.Location("test_provider");
            int[] testPa = {1, 2, 3, 4};
            loc.setTime(testPa[0]);
            loc.setLatitude(testPa[1]);
            loc.setLongitude(testPa[2]);
            loc.setAccuracy(testPa[3]);
            if (throttled) {
                com.android.server.location.provider.StationaryThrottlingLocationProvider.this.setTestModeProviderStationaryThrottle(loc);
                com.android.server.location.provider.StationaryThrottlingLocationProvider.this.setTestModeProviderStationaryThrottle(loc);
            } else {
                com.android.server.location.provider.StationaryThrottlingLocationProvider.this.setTestModeProviderStationaryUnthrottle(loc);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTestModeProviderStationaryUnthrottle(android.location.Location loc) {
        android.location.provider.ProviderRequest request = new android.location.provider.ProviderRequest.Builder().setIntervalMillis(50L).build();
        onSetRequest(request);
        loc.setElapsedRealtimeNanos(0L);
        onReportLocation(android.location.LocationResult.create(new android.location.Location[]{loc}));
        onDeviceStationaryChanged(true);
        onDeviceIdleChanged(true);
        onSetRequest(android.location.provider.ProviderRequest.EMPTY_REQUEST);
        onReportLocation(android.location.LocationResult.create(new android.location.Location[]{loc}));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTestModeProviderStationaryThrottle(android.location.Location loc) {
        android.location.provider.ProviderRequest request = new android.location.provider.ProviderRequest.Builder().setIntervalMillis(1000L).build();
        onSetRequest(request);
        loc.setElapsedRealtimeNanos(android.os.SystemClock.elapsedRealtime() * 1000 * 1000);
        onReportLocation(android.location.LocationResult.create(new android.location.Location[]{loc}));
        onDeviceStationaryChanged(true);
        onDeviceIdleChanged(true);
        onReportLocation(android.location.LocationResult.create(new android.location.Location[]{loc}));
    }
}
