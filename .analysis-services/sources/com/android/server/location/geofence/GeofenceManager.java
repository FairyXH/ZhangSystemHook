package com.android.server.location.geofence;

/* JADX INFO: loaded from: classes2.dex */
public class GeofenceManager extends com.android.server.location.listeners.ListenerMultiplexer<com.android.server.location.geofence.GeofenceManager.GeofenceKey, android.app.PendingIntent, com.android.server.location.geofence.GeofenceManager.GeofenceRegistration, android.location.LocationRequest> implements android.location.LocationListener {
    private static final java.lang.String ATTRIBUTION_TAG = "GeofencingService";
    private static final long MAX_LOCATION_AGE_MS = 300000;
    private static final long MAX_LOCATION_INTERVAL_MS = 7200000;
    private static final int MAX_SPEED_M_S = 100;
    private static final java.lang.String TAG = "GeofenceManager";
    private static final long WAKELOCK_TIMEOUT_MS = 30000;
    protected final android.content.Context mContext;
    private android.location.Location mLastLocation;
    private android.location.LocationManager mLocationManager;
    protected final com.android.server.location.injector.LocationPermissionsHelper mLocationPermissionsHelper;
    protected final com.android.server.location.injector.LocationUsageLogger mLocationUsageLogger;
    protected final com.android.server.location.injector.SettingsHelper mSettingsHelper;
    protected final com.android.server.location.injector.UserInfoHelper mUserInfoHelper;
    final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.location.injector.UserInfoHelper.UserListener mUserChangedListener = new com.android.server.location.injector.UserInfoHelper.UserListener() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda3
        @Override // com.android.server.location.injector.UserInfoHelper.UserListener
        public final void onUserChanged(int i, int i2) {
            this.f$0.onUserChanged(i, i2);
        }
    };
    private final com.android.server.location.injector.SettingsHelper.UserSettingChangedListener mLocationEnabledChangedListener = new com.android.server.location.injector.SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda4
        @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
        public final void onSettingChanged(int i) {
            this.f$0.onLocationEnabledChanged(i);
        }
    };
    private final com.android.server.location.injector.SettingsHelper.UserSettingChangedListener mLocationPackageBlacklistChangedListener = new com.android.server.location.injector.SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda5
        @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
        public final void onSettingChanged(int i) {
            this.f$0.onLocationPackageBlacklistChanged(i);
        }
    };
    private final com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener mLocationPermissionsListener = new com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener() { // from class: com.android.server.location.geofence.GeofenceManager.1
        @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
        public void onLocationPermissionsChanged(java.lang.String packageName) {
            com.android.server.location.geofence.GeofenceManager.this.onLocationPermissionsChanged(packageName);
        }

        @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
        public void onLocationPermissionsChanged(int uid) {
            com.android.server.location.geofence.GeofenceManager.this.onLocationPermissionsChanged(uid);
        }
    };

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ android.location.LocationRequest mergeRegistrations(java.util.Collection collection) {
        return mergeRegistrations((java.util.Collection<com.android.server.location.geofence.GeofenceManager.GeofenceRegistration>) collection);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ boolean registerWithService(android.location.LocationRequest locationRequest, java.util.Collection collection) {
        return registerWithService2(locationRequest, (java.util.Collection<com.android.server.location.geofence.GeofenceManager.GeofenceRegistration>) collection);
    }

    static class GeofenceKey {
        private final android.location.Geofence mGeofence;
        private final android.app.PendingIntent mPendingIntent;

        GeofenceKey(android.app.PendingIntent pendingIntent, android.location.Geofence geofence) {
            this.mPendingIntent = (android.app.PendingIntent) java.util.Objects.requireNonNull(pendingIntent);
            this.mGeofence = (android.location.Geofence) java.util.Objects.requireNonNull(geofence);
        }

        public android.app.PendingIntent getPendingIntent() {
            return this.mPendingIntent;
        }

        public boolean equals(java.lang.Object o) {
            if (!(o instanceof com.android.server.location.geofence.GeofenceManager.GeofenceKey)) {
                return false;
            }
            com.android.server.location.geofence.GeofenceManager.GeofenceKey that = (com.android.server.location.geofence.GeofenceManager.GeofenceKey) o;
            return this.mPendingIntent.equals(that.mPendingIntent) && this.mGeofence.equals(that.mGeofence);
        }

        public int hashCode() {
            return this.mPendingIntent.hashCode();
        }
    }

    protected class GeofenceRegistration extends com.android.server.location.listeners.PendingIntentListenerRegistration<com.android.server.location.geofence.GeofenceManager.GeofenceKey, android.app.PendingIntent> {
        private static final int STATE_INSIDE = 1;
        private static final int STATE_OUTSIDE = 2;
        private static final int STATE_UNKNOWN = 0;
        private android.location.Location mCachedLocation;
        private float mCachedLocationDistanceM;
        private final android.location.Location mCenter;
        private final android.location.Geofence mGeofence;
        private int mGeofenceState;
        private final android.location.util.identity.CallerIdentity mIdentity;
        private boolean mPermitted;
        private final android.os.PowerManager.WakeLock mWakeLock;

        GeofenceRegistration(android.location.Geofence geofence, android.location.util.identity.CallerIdentity identity, android.app.PendingIntent pendingIntent) {
            super(pendingIntent);
            this.mGeofence = geofence;
            this.mIdentity = identity;
            this.mCenter = new android.location.Location("");
            this.mCenter.setLatitude(geofence.getLatitude());
            this.mCenter.setLongitude(geofence.getLongitude());
            this.mWakeLock = ((android.os.PowerManager) java.util.Objects.requireNonNull((android.os.PowerManager) com.android.server.location.geofence.GeofenceManager.this.mContext.getSystemService(android.os.PowerManager.class))).newWakeLock(1, "GeofenceManager:" + identity.getPackageName());
            this.mWakeLock.setReferenceCounted(true);
            this.mWakeLock.setWorkSource(identity.addToWorkSource((android.os.WorkSource) null));
        }

        public android.location.Geofence getGeofence() {
            return this.mGeofence;
        }

        public android.location.util.identity.CallerIdentity getIdentity() {
            return this.mIdentity;
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public java.lang.String getTag() {
            return com.android.server.location.geofence.GeofenceManager.TAG;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.PendingIntentListenerRegistration
        public android.app.PendingIntent getPendingIntentFromKey(com.android.server.location.geofence.GeofenceManager.GeofenceKey geofenceKey) {
            return geofenceKey.getPendingIntent();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        public com.android.server.location.geofence.GeofenceManager getOwner() {
            return com.android.server.location.geofence.GeofenceManager.this;
        }

        @Override // com.android.server.location.listeners.PendingIntentListenerRegistration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            this.mGeofenceState = 0;
            this.mPermitted = com.android.server.location.geofence.GeofenceManager.this.mLocationPermissionsHelper.hasLocationPermissions(2, this.mIdentity);
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected void onActive() {
            android.location.Location location = com.android.server.location.geofence.GeofenceManager.this.getLastLocation();
            if (location != null) {
                executeOperation(onLocationChanged(location));
            }
        }

        boolean isPermitted() {
            return this.mPermitted;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean onLocationPermissionsChanged(java.lang.String packageName) {
            if (packageName == null || this.mIdentity.getPackageName().equals(packageName)) {
                return onLocationPermissionsChanged();
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean onLocationPermissionsChanged(int uid) {
            if (this.mIdentity.getUid() == uid) {
                return onLocationPermissionsChanged();
            }
            return false;
        }

        private boolean onLocationPermissionsChanged() {
            boolean permitted = com.android.server.location.geofence.GeofenceManager.this.mLocationPermissionsHelper.hasLocationPermissions(2, this.mIdentity);
            if (permitted != this.mPermitted) {
                this.mPermitted = permitted;
                return true;
            }
            return false;
        }

        double getDistanceToBoundary(android.location.Location location) {
            if (!location.equals(this.mCachedLocation)) {
                this.mCachedLocation = location;
                this.mCachedLocationDistanceM = this.mCenter.distanceTo(this.mCachedLocation);
            }
            return java.lang.Math.abs(this.mGeofence.getRadius() - this.mCachedLocationDistanceM);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public com.android.internal.listeners.ListenerExecutor.ListenerOperation<android.app.PendingIntent> onLocationChanged(android.location.Location location) {
            if (this.mGeofence.isExpired()) {
                remove();
                return null;
            }
            this.mCachedLocation = location;
            this.mCachedLocationDistanceM = this.mCenter.distanceTo(this.mCachedLocation);
            int oldState = this.mGeofenceState;
            float radius = java.lang.Math.max(this.mGeofence.getRadius(), location.getAccuracy());
            if (this.mCachedLocationDistanceM <= radius) {
                this.mGeofenceState = 1;
                if (oldState != 1) {
                    return new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.geofence.GeofenceManager$GeofenceRegistration$$ExternalSyntheticLambda1
                        public final void operate(java.lang.Object obj) throws java.lang.Exception {
                            this.f$0.lambda$onLocationChanged$0((android.app.PendingIntent) obj);
                        }
                    };
                }
            } else {
                this.mGeofenceState = 2;
                if (oldState == 1) {
                    return new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.geofence.GeofenceManager$GeofenceRegistration$$ExternalSyntheticLambda2
                        public final void operate(java.lang.Object obj) throws java.lang.Exception {
                            this.f$0.lambda$onLocationChanged$1((android.app.PendingIntent) obj);
                        }
                    };
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLocationChanged$0(android.app.PendingIntent pendingIntent) throws java.lang.Exception {
            sendIntent(pendingIntent, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLocationChanged$1(android.app.PendingIntent pendingIntent) throws java.lang.Exception {
            sendIntent(pendingIntent, false);
        }

        private void sendIntent(android.app.PendingIntent pendingIntent, boolean entering) {
            android.content.Intent intent = new android.content.Intent().putExtra("entering", entering);
            this.mWakeLock.acquire(30000L);
            try {
                pendingIntent.send(com.android.server.location.geofence.GeofenceManager.this.mContext, 0, intent, new android.app.PendingIntent.OnFinished() { // from class: com.android.server.location.geofence.GeofenceManager$GeofenceRegistration$$ExternalSyntheticLambda0
                    @Override // android.app.PendingIntent.OnFinished
                    public final void onSendFinished(android.app.PendingIntent pendingIntent2, android.content.Intent intent2, int i, java.lang.String str, android.os.Bundle bundle) {
                        this.f$0.lambda$sendIntent$2(pendingIntent2, intent2, i, str, bundle);
                    }
                }, null, null, com.android.server.PendingIntentUtils.createDontSendToRestrictedAppsBundle(null));
            } catch (android.app.PendingIntent.CanceledException e) {
                this.mWakeLock.release();
                com.android.server.location.geofence.GeofenceManager.this.removeRegistration(new com.android.server.location.geofence.GeofenceManager.GeofenceKey(pendingIntent, this.mGeofence), this);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sendIntent$2(android.app.PendingIntent pI, android.content.Intent i, int rC, java.lang.String rD, android.os.Bundle rE) {
            this.mWakeLock.release();
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append(this.mIdentity);
            android.util.ArraySet<java.lang.String> flags = new android.util.ArraySet<>(1);
            if (!this.mPermitted) {
                flags.add("na");
            }
            if (!flags.isEmpty()) {
                builder.append(" ").append(flags);
            }
            builder.append(" ").append(this.mGeofence);
            return builder.toString();
        }
    }

    public GeofenceManager(android.content.Context context, com.android.server.location.injector.Injector injector) {
        this.mContext = context.createAttributionContext(ATTRIBUTION_TAG);
        this.mUserInfoHelper = injector.getUserInfoHelper();
        this.mSettingsHelper = injector.getSettingsHelper();
        this.mLocationPermissionsHelper = injector.getLocationPermissionsHelper();
        this.mLocationUsageLogger = injector.getLocationUsageLogger();
    }

    private android.location.LocationManager getLocationManager() {
        android.location.LocationManager locationManager;
        synchronized (this.mLock) {
            if (this.mLocationManager == null) {
                this.mLocationManager = (android.location.LocationManager) java.util.Objects.requireNonNull((android.location.LocationManager) this.mContext.getSystemService(android.location.LocationManager.class));
            }
            locationManager = this.mLocationManager;
        }
        return locationManager;
    }

    public void addGeofence(android.location.Geofence geofence, android.app.PendingIntent pendingIntent, java.lang.String packageName, java.lang.String attributionTag) {
        com.android.server.location.LocationPermissions.enforceCallingOrSelfLocationPermission(this.mContext, 2);
        android.location.util.identity.CallerIdentity identity = android.location.util.identity.CallerIdentity.fromBinder(this.mContext, packageName, attributionTag, android.app.AppOpsManager.toReceiverId(pendingIntent));
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            putRegistration(new com.android.server.location.geofence.GeofenceManager.GeofenceKey(pendingIntent, geofence), new com.android.server.location.geofence.GeofenceManager.GeofenceRegistration(geofence, identity, pendingIntent));
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void removeGeofence(final android.app.PendingIntent pendingIntent) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            removeRegistrationIf(new java.util.function.Predicate() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.location.geofence.GeofenceManager.GeofenceKey) obj).getPendingIntent().equals(pendingIntent);
                }
            });
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public boolean isActive(com.android.server.location.geofence.GeofenceManager.GeofenceRegistration registration) {
        return registration.isPermitted() && isActive(registration.getIdentity());
    }

    private boolean isActive(android.location.util.identity.CallerIdentity identity) {
        return identity.isSystemServer() ? this.mSettingsHelper.isLocationEnabled(this.mUserInfoHelper.getCurrentUserId()) : this.mSettingsHelper.isLocationEnabled(identity.getUserId()) && this.mUserInfoHelper.isVisibleUserId(identity.getUserId()) && !this.mSettingsHelper.isLocationPackageBlacklisted(identity.getUserId(), identity.getPackageName());
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onRegister() {
        this.mUserInfoHelper.addListener(this.mUserChangedListener);
        this.mSettingsHelper.addOnLocationEnabledChangedListener(this.mLocationEnabledChangedListener);
        this.mSettingsHelper.addOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mLocationPermissionsHelper.addListener(this.mLocationPermissionsListener);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onUnregister() {
        this.mUserInfoHelper.removeListener(this.mUserChangedListener);
        this.mSettingsHelper.removeOnLocationEnabledChangedListener(this.mLocationEnabledChangedListener);
        this.mSettingsHelper.removeOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mLocationPermissionsHelper.removeListener(this.mLocationPermissionsListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationAdded(com.android.server.location.geofence.GeofenceManager.GeofenceKey key, com.android.server.location.geofence.GeofenceManager.GeofenceRegistration registration) {
        this.mLocationUsageLogger.logLocationApiUsage(1, 4, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), null, null, false, true, registration.getGeofence(), true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public void onRegistrationRemoved(com.android.server.location.geofence.GeofenceManager.GeofenceKey key, com.android.server.location.geofence.GeofenceManager.GeofenceRegistration registration) {
        this.mLocationUsageLogger.logLocationApiUsage(1, 4, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), null, null, false, true, registration.getGeofence(), true);
    }

    /* JADX INFO: renamed from: registerWithService, reason: avoid collision after fix types in other method */
    protected boolean registerWithService2(android.location.LocationRequest locationRequest, java.util.Collection<com.android.server.location.geofence.GeofenceManager.GeofenceRegistration> registrations) {
        getLocationManager().requestLocationUpdates("fused", locationRequest, com.android.server.FgThread.getExecutor(), this);
        return true;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void unregisterWithService() {
        synchronized (this.mLock) {
            getLocationManager().removeUpdates(this);
            this.mLastLocation = null;
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected android.location.LocationRequest mergeRegistrations(java.util.Collection<com.android.server.location.geofence.GeofenceManager.GeofenceRegistration> registrations) {
        long intervalMs;
        android.location.Location location = getLastLocation();
        long realtimeMs = android.os.SystemClock.elapsedRealtime();
        android.os.WorkSource workSource = null;
        double minFenceDistanceM = Double.MAX_VALUE;
        for (com.android.server.location.geofence.GeofenceManager.GeofenceRegistration registration : registrations) {
            if (!registration.getGeofence().isExpired(realtimeMs)) {
                workSource = registration.getIdentity().addToWorkSource(workSource);
                if (location != null) {
                    double fenceDistanceM = registration.getDistanceToBoundary(location);
                    if (fenceDistanceM < minFenceDistanceM) {
                        minFenceDistanceM = fenceDistanceM;
                    }
                }
            }
        }
        if (java.lang.Double.compare(minFenceDistanceM, Double.MAX_VALUE) < 0) {
            intervalMs = (long) java.lang.Math.min(7200000.0d, java.lang.Math.max(this.mSettingsHelper.getBackgroundThrottleProximityAlertIntervalMs(), (1000.0d * minFenceDistanceM) / 100.0d));
        } else {
            intervalMs = this.mSettingsHelper.getBackgroundThrottleProximityAlertIntervalMs();
        }
        return new android.location.LocationRequest.Builder(intervalMs).setMinUpdateIntervalMillis(0L).setHiddenFromAppOps(true).setWorkSource(workSource).build();
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(final android.location.Location location) {
        synchronized (this.mLock) {
            this.mLastLocation = location;
        }
        deliverToListeners(new java.util.function.Function() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.location.geofence.GeofenceManager.GeofenceRegistration) obj).onLocationChanged(location);
            }
        });
        updateService();
    }

    android.location.Location getLastLocation() {
        android.location.Location location;
        synchronized (this.mLock) {
            location = this.mLastLocation;
        }
        if (location == null) {
            location = getLocationManager().getLastLocation();
        }
        if (location != null && location.getElapsedRealtimeAgeMillis() > 300000) {
            return null;
        }
        return location;
    }

    void onUserChanged(final int userId, int change) {
        if (change == 1 || change == 4) {
            updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda8
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.location.geofence.GeofenceManager.lambda$onUserChanged$2(userId, (com.android.server.location.geofence.GeofenceManager.GeofenceRegistration) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$onUserChanged$2(int userId, com.android.server.location.geofence.GeofenceManager.GeofenceRegistration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    static /* synthetic */ boolean lambda$onLocationEnabledChanged$3(int userId, com.android.server.location.geofence.GeofenceManager.GeofenceRegistration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    void onLocationEnabledChanged(final int userId) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.geofence.GeofenceManager.lambda$onLocationEnabledChanged$3(userId, (com.android.server.location.geofence.GeofenceManager.GeofenceRegistration) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$onLocationPackageBlacklistChanged$4(int userId, com.android.server.location.geofence.GeofenceManager.GeofenceRegistration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    void onLocationPackageBlacklistChanged(final int userId) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.geofence.GeofenceManager.lambda$onLocationPackageBlacklistChanged$4(userId, (com.android.server.location.geofence.GeofenceManager.GeofenceRegistration) obj);
            }
        });
    }

    void onLocationPermissionsChanged(final java.lang.String packageName) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.geofence.GeofenceManager.GeofenceRegistration) obj).onLocationPermissionsChanged(packageName);
            }
        });
    }

    void onLocationPermissionsChanged(final int uid) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.geofence.GeofenceManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.geofence.GeofenceManager.GeofenceRegistration) obj).onLocationPermissionsChanged(uid);
            }
        });
    }
}
